package com.cloud.mina.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class LoginUserInfo {
	public static boolean isLogin(HttpServletRequest request){
		Object obj = request.getSession().getAttribute("userInfo");
		if (obj == null) {
			return false;
		}
		return true;
	}
	public static String getCurrentUserInfo(HttpServletRequest request)
	{

		Map userMap = (Map) request.getSession().getAttribute("userInfo");
		if (userMap != null) {
			userMap.get("name");
			userMap.get("appType");
			StringBuffer buffer = new StringBuffer("");
			buffer.append("用户姓名:").append(userMap.get("name")).append("\t");
			return buffer.toString();
		} else
			return "未登录用户";
	}
}
