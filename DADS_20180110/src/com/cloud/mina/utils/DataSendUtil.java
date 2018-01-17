package com.cloud.mina.utils;

public class DataSendUtil {
	/**
	 * 校验版本号：如果为空则返回0，不为空则返回原值
	 * @param version
	 * @return
	 */
	public static String getVersion(String version){
		if(version!=null && !"".equals(version.trim())){
			return version;
		}else{
			return "0";
		}
		
	}
	/**
	 * 校验设备类型：计步器|手机
	 * 如果为空则返回0，不为空则返回原值
	 * @param deviceType
	 * @return
	 */
	public static String getDeviceType(String deviceType){
		if(deviceType!=null && !"".equals(deviceType.trim())){
			return deviceType;
		}else{
			return "0";
		}
	}
	/**
	 * 校验设备ID： 如果为空则返回0，不为空则返回原值
	 * @param deviceType
	 * @return
	 */
	public static String getDeviceId(String getDeviceId){
		if(getDeviceId!=null && !"".equals(getDeviceId.trim())){
			return getDeviceId;
		}else{
			return "0";
		}
	}
	
}
