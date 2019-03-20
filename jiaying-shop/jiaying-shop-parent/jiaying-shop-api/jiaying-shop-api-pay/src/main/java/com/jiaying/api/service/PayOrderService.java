package com.jiaying.api.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/pay")
public interface PayOrderService {
	//測試分佈式事務
	@RequestMapping("/payOrder")
	public String payOrder(@RequestParam("orderId") String orderId,@RequestParam("temp") int temp);
}
