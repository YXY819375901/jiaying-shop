package com.jiaying.api.service;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jiaying.base.ResponseBase;
import com.jiaying.entity.UserEntity;

@RequestMapping("/member")
public interface memberService {
	
	//使用userId查找用户信息
	@RequestMapping("/findUserById")
	ResponseBase findUserById(Long userId);
	
	@RequestMapping("/regUser")
	ResponseBase regUser(@RequestBody UserEntity user);
	
	@RequestMapping("/login")
	ResponseBase login(@RequestBody UserEntity user);
	
	@RequestMapping("/findByTokenUser")
	ResponseBase findByTokenUser(@RequestParam("token") String token);
	
	@RequestMapping("/findByOpenIdUser")
	ResponseBase findByOpenIdUser(@RequestParam("openId") String openid);
	
	@RequestMapping("/qqLogin")
	ResponseBase qqLogin(@RequestBody UserEntity user);
	
	
}
