package com.jiaying.api.service.Impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

import com.jiaying.api.service.TestApiService;
import com.jiaying.base.BaseApiService;
import com.jiaying.base.ResponseBase;

@RestController
public class TestApiServiceImpl extends BaseApiService implements TestApiService{

	@Override
	public Map<String, Object> test(Integer id, String name) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rtnCode", "200");
		result.put("rtnmsg", "success");
		result.put("data", "id:"+id+",name:"+name);
		return result;
	}

	@Override
	public ResponseBase testResponseBase() {
		return setResultSuccess();
	}

	@Override
	public ResponseBase testRedis(String key, String value) {
		baseRedisService.setString(key, value, null);
		return setResultSuccess();
	}

	
	
}
