/**
 * 
 */
/**
 * @author 星宇
 *
 */
package com.jiaying.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@ToString
public class ResponseBase {
	
	private Integer rtnCode;
	private String msg;
	private Object data;
	
	
	
	
	




	public ResponseBase() {
	}




	public ResponseBase(Integer rtnCode, String msg, Object data) {
		this.rtnCode = rtnCode;
		this.msg = msg;
		this.data = data;
	}
	
//	public static void main(String[] args) {
//		ResponseBase responseBase = new ResponseBase();
//		responseBase.setData("yixingyu");
//		responseBase.setMsg("jiaying");
//		responseBase.setRtnCode(200);
//		log.info("jaiying....");
//		System.out.println(responseBase.toString());
//	}
	
}



