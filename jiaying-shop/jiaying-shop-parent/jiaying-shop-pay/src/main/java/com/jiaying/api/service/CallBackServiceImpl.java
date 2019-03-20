package com.jiaying.api.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.config.AlipayConfig;
import com.codingapi.tx.annotation.TxTransaction;
import com.jiaying.Feign.OrderServiceFeign;
import com.jiaying.base.BaseApiService;
import com.jiaying.base.ResponseBase;
import com.jiaying.constants.Constants;
import com.jiaying.dao.PaymentInfoDao;
import com.jiaying.entity.PaymentInfo;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CallBackServiceImpl extends BaseApiService implements CallBackService{

	@Autowired
	private PaymentInfoDao paymentInfoDao;
	@Autowired
	private OrderServiceFeign orderServiceFeign;
	
	@Override
	public ResponseBase synCallBack(@RequestParam Map<String, String> params) {
		//1.日志记录
		log.info("#####支付宝同步通知synCallBack#####开始#####，params:{}",params);
		try {
			//2.验签操作
			boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

			log.info("#####支付宝同步通知signVerified:{}#####",signVerified);
			
			if(!signVerified) {
				return setResultError("验签失败");
			}
			//——请在这里编写您的程序（以下代码仅作参考）——
			
			//商户订单号
			String outTradeNo = params.get("out_trade_no");
		
			//支付宝交易号
			String tradeNo = params.get("trade_no");
		
			//付款金额
			String totalAmount = params.get("total_amount");

			JSONObject data = new JSONObject();
			data.put("out_trade_no", outTradeNo);
			data.put("trade_no", tradeNo);
			data.put("total_amount", totalAmount);
			return setResultSuccess(data);
		} catch (Exception e) {
			log.error("###支付宝出现异常：error",e);
			return setResultError("系统错误");
		}finally {
			log.info("#####支付宝同步通知synCallBack#####结束#####，params:{}",params);
		}
		
	}

	@TxTransaction(isStart = true)
	@Transactional
	public synchronized String asynCallBack(@RequestParam Map<String, String> params) {
		//1.日志记录
		log.info("#####支付宝同步通知synCallBack#####开始#####，params:{}",params);
		try {
			//2.验签操作
			boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

			log.info("#####支付宝同步通知signVerified:{}#####",signVerified);
			
			if(!signVerified) {
				return Constants.PAY_FAIL;
			}
			//——请在这里编写您的程序（以下代码仅作参考）——
			
			//商户订单号
			String outTradeNo = params.get("out_trade_no");
			PaymentInfo paymentInfo = paymentInfoDao.getByOrderIdPayInfo(outTradeNo);
			if(paymentInfo == null) {
				return Constants.PAY_FAIL;
			}
			Integer state = paymentInfo.getState();
			if(state == 1) {
				return Constants.PAY_SUCCESS;
			}
			
			//支付宝交易号
			String tradeNo = params.get("trade_no");
		
			//付款金额
			String totalAmount = params.get("total_amount");

			//判斷實際付款金額與商品金額是否一致
			
			//修改支付表狀態
			
			paymentInfo.setState(1);
			paymentInfo.setPayMessage(params.toString());
			paymentInfo.setPlatformorderId(tradeNo);
			//手動begin begin
			Integer updatePayInfo = paymentInfoDao.updatePayInfo(paymentInfo);
			if(updatePayInfo <= 0) {
				return Constants.PAY_FAIL;
			}
			//調用訂單接口，支付狀態
			ResponseBase orderResult = orderServiceFeign.updateOrderId(1L, tradeNo, outTradeNo);
			if(!orderResult.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
				//回滾 手動回滾 rollback
				return Constants.PAY_FAIL;
			}
			int i=1/0;
			//手動提交 commit
			return Constants.PAY_SUCCESS;
		} catch (Exception e) {
			log.error("###支付宝出现异常：error",e);
			//回滾 手動回滾 rollback
			return Constants.PAY_FAIL;
		}finally {
			log.info("#####支付宝同步通知synCallBack#####结束#####，params:{}",params);
		}
	}
	
}
