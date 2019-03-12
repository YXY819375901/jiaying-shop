package com.jiaying.controller;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiaying.base.ResponseBase;
import com.jiaying.constants.Constants;
import com.jiaying.feign.MemberServiceFeign;
import com.jiaying.utils.CookieUtil;


@Controller
public class indexController {
	
	@Autowired
	private MemberServiceFeign memberServiceFeign;

	private static final String INDEX = "index";
	
	//主页
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request) {
		//1.从cookie中获取token的信息
		String token = CookieUtil.getUid(request, Constants.COOKIE_MEMBER_TOKEN);
		//2.如果cookie存在token，调用会员服务接口，使用token查询用户信息
		if(!StringUtils.isEmpty(token)) {
			ResponseBase responseBase = memberServiceFeign.findByTokenUser(token);
			if(responseBase.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
				LinkedHashMap userData = (LinkedHashMap) responseBase.getData();
				String userName = (String)userData.get("username");
				request.setAttribute("username", userName);
			}
		}
		return INDEX;
	}
}






