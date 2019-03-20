package com.jiaying.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.jiaying.api.service.PayService;
import com.jiaying.base.ResponseBase;
import com.jiaying.entity.PaymentInfo;

@Component
@FeignClient("pay")
public interface PayServiceFeign extends PayService{
	
}
