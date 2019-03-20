package com.jiaying.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codingapi.tx.annotation.ITxTransaction;
import com.jiaying.api.order.OrderService;
import com.jiaying.base.BaseApiService;
import com.jiaying.base.ResponseBase;
import com.jiaying.dao.OrderDao;

@RestController
public class OrderServiceImpl extends BaseApiService implements OrderService,ITxTransaction{

	@Autowired
	private OrderDao orderDao;
	
	@Transactional
	public ResponseBase updateOrderId(@RequestParam("isPay") Long isPay,@RequestParam("payId")String aliPayId,@RequestParam("orderNumber")String orderNumber) {
		int updateOrder = orderDao.updateOrder(isPay, aliPayId, orderNumber);
		if(updateOrder <= 0) {
			return setResultError("系統錯誤");
		}	
		return setResultSuccess();
	}
	
}
