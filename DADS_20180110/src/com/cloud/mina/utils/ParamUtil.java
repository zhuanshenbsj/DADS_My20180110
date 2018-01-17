package com.cloud.mina.utils;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取request参数工具类，当参数中包含中文信息时使用
 * 作用:统一转码
 * @date 2013-5-31
 */
public class ParamUtil {
	public static String srcEncode = null;
//	public static String srcEncode = "ISO8859-1";
	public static String tagEncode = "UTF-8";
	/**
	 * 根据指定编码格式从request中获取参数值，并转为目标格式<br>
	 * 调用此方法前可通过setSrcEncode()、setTagEccode()方法设置源编码格式和目标编码格式<br>
	 * 默认源编码格式为null,目标格式为UTF-8
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getParameter(HttpServletRequest request,String parameterName){
		try {
			if(srcEncode == null || tagEncode == null){
				return request.getParameter(parameterName)==null?"":request.getParameter(parameterName);
			}
			String parameterValue = request.getParameter(parameterName);
			if(parameterValue!=null){
				return new String(parameterValue.getBytes(srcEncode), tagEncode);
			}else{
				return "";
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 设置源编码格式
	 * @param srcEncode
	 */
	public static void setSrcEncode(String srcEncode) {
		ParamUtil.srcEncode = srcEncode;
	}
	/**
	 * 设置目标编码格式
	 * @param tagEncode
	 */
	public static void setTagEncode(String tagEncode) {
		ParamUtil.tagEncode = tagEncode;
	}
}
