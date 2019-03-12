package com.jiaying.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.jiaying.api.service.memberService;

@Component
@FeignClient("member")
public interface MemberServiceFeign extends memberService{

}
