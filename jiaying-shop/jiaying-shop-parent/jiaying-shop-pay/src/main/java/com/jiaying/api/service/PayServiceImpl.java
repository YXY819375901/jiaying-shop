package com.jiaying.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.config.AlipayConfig;
import com.jiaying.base.BaseApiService;
import com.jiaying.base.ResponseBase;
import com.jiaying.constants.Constants;
import com.jiaying.dao.PaymentInfoDao;
import com.jiaying.entity.PaymentInfo;
import com.jiaying.utils.TokenUtils;

@RestController
public class PayServiceImpl extends BaseApiService implements PayService{
	
	@Autowired
	private PaymentInfoDao paymentInfoDao;
	
	
	@Override
	public ResponseBase createToken(@RequestBody PaymentInfo paymentInfo) {
//		1.创建支付请求信息
		Integer savePaymentType = paymentInfoDao.savePaymentType(paymentInfo);
		if(savePaymentType <= 0) {
			return setResultError("创建支付订单失败");
		}
//		2.生成对应的token
		String payToken = TokenUtils.getPayToken();
//		3.存放在redis中，key为token value为id
		baseRedisService.setString(payToken, paymentInfo.getId()+"", Constants.PAY_TOKEN_MEMBER_TIME);
//		4.返回token
		JSONObject data = new JSONObject();
		data.put("payToken", payToken);		
		return setResultSuccess(data);
	}

	@Override
	public ResponseBase findPayToken(@RequestParam("payToken") String payToken) {
		
		
//		1.参数验证
		if(StringUtils.isEmpty(payToken)) {
			return setResultError("token不能为空");
		}
//		2.判断token有效期
//		3.使用token，查找reids 找到对应支付id
		String payId = baseRedisService.getString(payToken);
		if(StringUtils.isEmpty(payId)) {
			return setResultError("支付请求已经超时！！");
		}
//		4.使用支付id，进行下单
		long payIDL = Long.parseLong(payId);
//		5.使用支付id查询支付信息
		PaymentInfo paymentInfo = paymentInfoDao.getPaymentInfo(payIDL);
		if(paymentInfo == null) {
			return setResultError("未找到支付信息");
		}
//		6.对接支付代码 返回提交支付form表单元素给客户端
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
		
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = paymentInfo.getOrderId();
		//付款金额，必填
		String total_amount = paymentInfo.getPrice()+"";
		//订单名称，必填
		String subject = "jiaying-yxy";
		//商品描述，可空
	//	String body = new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
				+ "\"total_amount\":\""+ total_amount +"\"," 
				+ "\"subject\":\""+ subject +"\"," 
		//		+ "\"body\":\""+ body +"\"," 
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		
		//若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
		//alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
		//		+ "\"total_amount\":\""+ total_amount +"\"," 
		//		+ "\"subject\":\""+ subject +"\"," 
		//		+ "\"body\":\""+ body +"\"," 
		//		+ "\"timeout_express\":\"10m\"," 
		//		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		//请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
		
		//请求
		try {
			String result = alipayClient.pageExecute(alipayRequest).getBody();
			JSONObject data = new JSONObject();
			data.put("payHTML", result);
			return setResultSuccess(data);
		} catch (Exception e) {
			return setResultError("支付异常");
		}
		
	}
	
}







