package com.jiaying.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiaying.base.ResponseBase;
import com.jiaying.constants.Constants;
import com.jiaying.feign.PayServiceFeign;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class payController {

	@Autowired
	private PayServiceFeign payServiceFeign;
	
	@RequestMapping("/aliPay")
	public void alipay(String payToken,HttpServletResponse response) throws IOException {
		//设置页面展示结果
		response.setContentType("text/html;charset=utf-8");
		
		PrintWriter writer = response.getWriter();
		//1.验证参数
		if(StringUtils.isEmpty(payToken)) {
			return;
		}
		//2.调用支付服务接口 获取支付宝html元素
		ResponseBase payTokenResult = payServiceFeign.findPayToken(payToken);
		if(!payTokenResult.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
			String msg = payTokenResult.getMsg();
			writer.println(msg);
			return;
		}
			
		//3.返回可以执行的html元素给客户端
		LinkedHashMap data =(LinkedHashMap) payTokenResult.getData();
		String  payHtml = (String)data.get("payHTML");
		log.info("#####PayController###payHtml:{}",payHtml);
		//4.页面上进行渲染
		writer.println(payHtml);
		writer.close();
	}
}
























