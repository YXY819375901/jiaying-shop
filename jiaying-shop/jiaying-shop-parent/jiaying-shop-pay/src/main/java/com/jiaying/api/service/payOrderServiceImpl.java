package com.jiaying.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codingapi.tx.annotation.TxTransaction;
import com.jiaying.Feign.OrderServiceFeign;
import com.jiaying.base.ResponseBase;
import com.jiaying.constants.Constants;
import com.jiaying.dao.PaymentInfoDao;
import com.jiaying.entity.PaymentInfo;

@RestController
public class payOrderServiceImpl implements PayOrderService{
	@Autowired
	private PaymentInfoDao paymentInfoDao;
	@Autowired
	private OrderServiceFeign orderServiceFeign;

	@TxTransaction(isStart = true)
	@Transactional
	//測試分佈式事務
	public String payOrder(@RequestParam("orderId") String orderId,@RequestParam("temp") int temp) {
		PaymentInfo paymentInfo = paymentInfoDao.getByOrderIdPayInfo(orderId);
		if(paymentInfo == null) {
			return Constants.PAY_FAIL;
		}
		Integer state = paymentInfo.getState();
		if(state == 1) {
			return Constants.PAY_SUCCESS;
		}
		
		//支付宝交易号
		String tradeNo = "1787551";	
		paymentInfo.setState(1);
		paymentInfo.setPayMessage("test");
		paymentInfo.setPlatformorderId(tradeNo);
		//手動begin begin
		Integer updatePayInfo = paymentInfoDao.updatePayInfo(paymentInfo);
		if(updatePayInfo <= 0) {
			return Constants.PAY_FAIL;
		}
		//調用訂單接口，支付狀態
		ResponseBase orderResult = orderServiceFeign.updateOrderId(1L, tradeNo, orderId);
		if(!orderResult.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
			//回滾 手動回滾 rollback
			return Constants.PAY_FAIL;
		}
		int i=1/temp;
		//手動提交 commit
		return null;
	}

	
	
}
