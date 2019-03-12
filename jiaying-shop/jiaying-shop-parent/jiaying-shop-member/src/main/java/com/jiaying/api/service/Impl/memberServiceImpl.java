package com.jiaying.api.service.Impl;


import java.util.Date;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jiaying.api.service.memberService;
import com.jiaying.base.BaseApiService;
import com.jiaying.base.ResponseBase;
import com.jiaying.constants.Constants;
import com.jiaying.dao.MemberDao;
import com.jiaying.entity.UserEntity;
import com.jiaying.mq.RegisterMailboxProducer;
import com.jiaying.utils.MD5Util;
import com.jiaying.utils.TokenUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class memberServiceImpl extends BaseApiService implements memberService{
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private RegisterMailboxProducer registerMailboxProducer;
	
	@Value("${messages.queue}")
	private String MESSAGEQUEUE;
	
	
	//发送消息给消息服务平台
	private void sendMsg(String json) {
		ActiveMQQueue activeMQQueue = new ActiveMQQueue(MESSAGEQUEUE);
		registerMailboxProducer.sendMsg(activeMQQueue, json);
	}
	
	@Override
	public ResponseBase findUserById(Long userId) {
		UserEntity user = memberDao.findByID(userId);
		if(user==null) {
			return setResultError("未查到....");
		}
		return  setResultSuccess(user);
	}

	@Override
	public ResponseBase regUser(@RequestBody UserEntity user) {
		String password = user.getPassword();
		if(StringUtils.isEmpty(password)) {
			return setResultError("密码不能为空");
		}
		String newPassword = MD5Util.MD5(password);
		user.setPassword(newPassword);
		user.setCreated(new Date());
		user.setUpdated(new Date());
		Integer insertUser = memberDao.insertUser(user);
		if(insertUser <=0) {
			return setResultError("注册失败");
		}
		//采用异步方式发送消息
		String email = user.getEmail();
		String json = emailJson(email);
		log.info("###会员服务消息到消息服务平台####");
		sendMsg(json);
		return setResultSuccess("注册成功");
	}
	
	private String emailJson(String email) {
		JSONObject rootJson = new JSONObject();
		JSONObject header = new JSONObject();
		header.put("interfaceType", Constants.MSG_EMAIL);
		JSONObject content = new JSONObject();
		content.put("email", email);
		rootJson.put("header", header);
		rootJson.put("content", content);
		return rootJson.toJSONString();
	}
	


	@Override
	public ResponseBase login(@RequestBody UserEntity user) {
		//1.验证参数
		String userName = user.getUsername();
		if(StringUtils.isEmpty(userName)) {
			return setResultError("用户名不可为空");
		}
		String password = user.getPassword();
		if(StringUtils.isEmpty(password)) {
			return setResultError("密码不可为空");
		}		
		//2.数据库验证用户和密码是否正确
		String newPassword = MD5Util.MD5(password);
		UserEntity userEntity = memberDao.login(userName, newPassword); 
		return setLogin(userEntity);
		
	}
	
	private ResponseBase setLogin(UserEntity userEntity) {
		if(userEntity == null) {
			return setResultError("密码或账号错误");
		}
		//3.如果正确，生成token
		String memberToken = TokenUtils.getMemberToken();
		
		//4.存入redis，key为token，value为userId
		Integer userId = userEntity.getId();
		log.info("###用户信息token存放在redis中... key为:{},value为:{}",memberToken,userId);
		baseRedisService.setString(memberToken, userId+"", Constants.TOKEN_MEMBER_TIME);
		
		//5.返回token
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("memberToken", memberToken);
		return setResultSuccess(jsonObject);
	}

	@Override
	public ResponseBase findByTokenUser(@RequestParam("token") String token) {
		//1.token验证
		if(StringUtils.isEmpty(token)) {
			return setResultError("token不能为空");
		}
		//2.在redis中通过token拿到userId
		String struserId = baseRedisService.getString(token);
		if(StringUtils.isEmpty(struserId)) {
			return setResultError("token无效或已过期");
		}
		
		//3.通过userId在数据库中查找
		long userId = Long.parseLong(struserId);
		UserEntity user = memberDao.findByID(userId);
		if(user == null) {
			return setResultError("未找到该用户");
		}
		user.setPassword(null);
		//4.返回用户
		return setResultSuccess(user);
	}

	//使用openid查询用户信息是否已经管理
	@Override
	public ResponseBase findByOpenIdUser(@RequestParam("openId")String openId) {
		//1.验证参数
		if(StringUtils.isEmpty(openId)) {
			return setResultError("openId不能为空");
		}
		//2.使用openid查询数据库user表对应数据信息
		UserEntity userEntity = memberDao.findByOpenIdUser(openId);
		if(userEntity == null) {
			return setResultError(Constants.HTTP_RES_CODE_201,"openId没有关联");
		}
		//3.自动登录
		return setLogin(userEntity);
	}

	//登录并且绑定openid
	@Override
	public ResponseBase qqLogin(@RequestBody UserEntity user) {
		//1.验证参数
		String openId = user.getOpenId();
		if(StringUtils.isEmpty(openId)) {
			return setResultError("openid不能为空");
		}
		//2.先进行账号登录
		ResponseBase setLogin = login(user);
		if(!setLogin.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
			return setLogin;
		}
		//3.自动登录
		JSONObject jsonObject = (JSONObject) setLogin.getData();
		//4.获取token信息
		String memberToken = jsonObject.getString("memberToken");
		ResponseBase userToken = findByTokenUser(memberToken);
		if(!userToken.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
			return userToken;
		}
		UserEntity userEntity = (UserEntity) userToken.getData();
		//5.修改用户openid
		Integer userId = userEntity.getId();
		Integer updateByOpenIdUser = memberDao.updateByOpenIdUser(openId, userId);
		if(updateByOpenIdUser <= 0) {
			return setResultError("QQ关联失败");
		}
		return setLogin;
	}
}









