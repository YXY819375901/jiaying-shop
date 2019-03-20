package com.jiaying.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.jiaying.api.service.CallBackService;

@Component
@FeignClient("pay")
public interface CallBackServiceFeign extends CallBackService{
	
}
