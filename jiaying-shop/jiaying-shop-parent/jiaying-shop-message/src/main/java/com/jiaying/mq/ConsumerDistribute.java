package com.jiaying.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.fastjson.JSONObject;
import com.jiaying.adapter.MessageAdapter;
import com.jiaying.constants.Constants;
import com.jiaying.email.service.EmailService;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class ConsumerDistribute {
	
	@Autowired
	private EmailService emailService;
	private MessageAdapter messageAdapter;
	
	
	//监听消息
	@JmsListener(destination="messages_queue")
	public void distribute( String json) {
		log.info("####消息服务平台接受消息内容：{}###",json);
		if(StringUtils.isEmpty(json)) {
			return;
		}
		JSONObject rootJSON = new JSONObject().parseObject(json);
		JSONObject header = rootJSON.getJSONObject("header");
		String interfaceType = header.getString("interfaceType");
		if(StringUtils.isEmpty(interfaceType)) {
			return;
		}
		//判断接口类型是否为发邮件
		if(interfaceType.equals(Constants.MSG_EMAIL)) {
			//调用
			messageAdapter =  (MessageAdapter) emailService;
		}
		JSONObject contentObject = rootJSON.getJSONObject("content");
		messageAdapter.sendMsg(contentObject);
	}	
		
		
}
