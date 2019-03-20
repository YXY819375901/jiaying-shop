package com.jiaying.api.order;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jiaying.base.ResponseBase;

@RequestMapping("/order")
public interface OrderService {
	
	@RequestMapping("/updateOrderId")
	public ResponseBase updateOrderId(@RequestParam("isPay") Long isPay,@RequestParam("payId")String aliPayId,@RequestParam("orderNumber")String orderNumber);
	
}
