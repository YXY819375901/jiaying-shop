package com.jiaying.controller;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.cookie.SetCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jiaying.base.ResponseBase;
import com.jiaying.constants.Constants;
import com.jiaying.entity.UserEntity;
import com.jiaying.feign.MemberServiceFeign;
import com.jiaying.utils.CookieUtil;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;

@Controller
public class LoginController {

	@Autowired
	private MemberServiceFeign memberServiceFeign;
	
	private static final String LOGIN = "login";
	
	private static final String INDEX = "redirect:/";
	
	private static final String qqrelation = "qqrelation";
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String LoginGet() {
		return LOGIN;
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String LoginPost(UserEntity userEntity,HttpServletRequest request,HttpServletResponse response) {
		//1.验证参数
		//2.调用登录接口，获取token信息
		ResponseBase loginBase = memberServiceFeign.login(userEntity);
		if(!loginBase.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
			request.setAttribute("error", "账号或密码错误!!!");
			return LOGIN;
		}
		
		LinkedHashMap loginData = (LinkedHashMap) loginBase.getData();
		String memberToken = (String) loginData.get("memberToken");
		if(StringUtils.isEmpty(memberToken)) {
			request.setAttribute("error", "会话已失效！！！");
			return LOGIN;
		}
		
		//3.将token信息存放在cookie里面
		SetCookie(memberToken,response);
		return INDEX;
	}
	
	private void SetCookie(String memberToken,HttpServletResponse response) {
		CookieUtil.addCookie(response, Constants.COOKIE_MEMBER_TOKEN, memberToken, Constants.COOKIE_TOKEN_MEMBER_TIME);
	}
	
	
	//生成QQ授权登录链接
	@RequestMapping("/locaQQLogin")
	public String locaQQLogin(HttpServletRequest request) throws QQConnectException {
		//生成授权连接
		String authorizeURL = new Oauth().getAuthorizeURL(request);
		return "redirect:"+authorizeURL;
	}
	
	@RequestMapping("/qqLoginCallback")
	public String qqLoginCallback(HttpServletRequest request,HttpSession httpSession,HttpServletResponse response) throws QQConnectException {
		//1.获取授权码Code
		//2.使用授权码Code获取accessToken
		AccessToken accessTokenOj = new Oauth().getAccessTokenByRequest(request);
		if(accessTokenOj == null) {
			request.setAttribute("error", "QQ授权失败");
			return "error";
		}
		String accessToken = accessTokenOj.getAccessToken();
		if(accessToken == null) {
			request.setAttribute("error", "accessToken为null");
			return "error";
		}
		//3.使用accessToken获取openId
		OpenID openIdOj = new OpenID(accessToken);
		String userOpenId = openIdOj.getUserOpenID();
		//4.调用会员服务接口 使用userOpenId 查找是否已经关联过账号
		ResponseBase openUserBase = memberServiceFeign.findByOpenIdUser(userOpenId);
		if(openUserBase.getRtnCode().equals(Constants.HTTP_RES_CODE_201)) {
			//5.如果没有关联账号，跳转到关联账号页面
			httpSession.setAttribute("qqOpenId", userOpenId);
			return qqrelation;
		}
		//6.已经绑定账号，自动登录 将用户token信息存放在cookie中
		LinkedHashMap dataTokenMap = (LinkedHashMap) openUserBase.getData();
		String memberToken = (String) dataTokenMap.get("memberToken");
		SetCookie(memberToken,response);
		return INDEX;
		
	}
	
	@RequestMapping(value="/qqRelation", method=RequestMethod.POST)
	public String qqRelation(UserEntity userEntity,HttpServletRequest request,HttpServletResponse response,HttpSession httpSession) {
		//1.验证参数
		String qqOpenId = (String) httpSession.getAttribute("qqqqOpenId");
		if(StringUtils.isEmpty(qqOpenId)) {
			request.setAttribute("error", "没有获取到openId");
			return "error";
		}
		//2.调用登录接口，获取token信息
		ResponseBase loginBase = memberServiceFeign.qqLogin(userEntity);
		if(!loginBase.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
			request.setAttribute("error", "账号或密码错误!!!");
			return LOGIN;
		}
		
		LinkedHashMap loginData = (LinkedHashMap) loginBase.getData();
		String memberToken = (String) loginData.get("memberToken");
		if(StringUtils.isEmpty(memberToken)) {
			request.setAttribute("error", "会话已失效！！！");
			return LOGIN;
		}
		
		//3.将token信息存放在cookie里面
		SetCookie(memberToken,response);
		return INDEX;
	}
}





