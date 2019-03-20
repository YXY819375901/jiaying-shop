package com.jiaying.Feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.jiaying.api.order.OrderService;

@Component
@FeignClient("order")
public interface OrderServiceFeign extends OrderService{
	
}
