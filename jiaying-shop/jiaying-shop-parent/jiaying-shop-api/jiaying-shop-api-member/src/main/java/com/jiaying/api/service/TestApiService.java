package com.jiaying.api.service;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jiaying.base.ResponseBase;

@RequestMapping("/member")
public interface TestApiService {
	@RequestMapping("/test")
	public Map<String,Object> test(Integer id,String name);
	@RequestMapping("/testResponseBase")
	public ResponseBase testResponseBase();
	@RequestMapping("/testRedis")
	public ResponseBase testRedis(String key,String value);
}
