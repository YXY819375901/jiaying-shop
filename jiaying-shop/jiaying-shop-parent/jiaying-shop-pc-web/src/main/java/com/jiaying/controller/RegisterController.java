package com.jiaying.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiaying.base.ResponseBase;
import com.jiaying.constants.Constants;
import com.jiaying.entity.UserEntity;
import com.jiaying.feign.MemberServiceFeign;

@Controller
public class RegisterController {
	
	@Autowired
	private MemberServiceFeign memberServiceFeign;
	private static final String REGISTER = "register";
	private static final String LOGIN = "login";
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String registerGet() {
		return REGISTER;
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerPost(UserEntity userEntity,HttpServletRequest request) {
		//1.验证参数
		//2.调用会员接口
		ResponseBase regUser = memberServiceFeign.regUser(userEntity);
		//3.失败返回注册界面
		if(!regUser.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
			request.setAttribute("error", "注册失败");
			return REGISTER;
		}
		//4.成功跳转登录页面
		return LOGIN;
	}
}
