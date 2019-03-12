package com.jiaying.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiaying.constants.Constants;

@Component
public class BaseApiService {
	@Autowired
	protected BaseRedisService baseRedisService;
	
	
	public ResponseBase setResultError(int code,String msg) {
		return setResult(code, msg, null);
	}
	
	public ResponseBase setResultError(String msg) {
		return setResult(Constants.HTTP_RES_CODE_500, msg, null);
	}
	
	public ResponseBase setResultSuccess(String msg) {
		return setResult(Constants.HTTP_RES_CODE_200,msg,null);
	}
	
	public ResponseBase setResultSuccess(Object data) {
		return setResult(Constants.HTTP_RES_CODE_200,Constants.HTTP_RES_CODE_200_VALUE,data);
	}
	
	//返回成功，没有data值
	public ResponseBase setResultSuccess() {
		return setResult(Constants.HTTP_RES_CODE_200,Constants.HTTP_RES_CODE_200_VALUE,null);
	}
	
	
	//通用封装
	public ResponseBase setResult(Integer code,String msg,Object data) {
		return new ResponseBase(code,msg,data);
		
	}
}
