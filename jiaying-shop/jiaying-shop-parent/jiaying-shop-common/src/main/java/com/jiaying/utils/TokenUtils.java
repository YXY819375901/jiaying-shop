package com.jiaying.utils;

import java.util.UUID;

import com.jiaying.constants.Constants;

public class TokenUtils {
	
	public static String getMemberToken() {
		return Constants.TOKEN_MEMBER+"-"+UUID.randomUUID();
	}
}
