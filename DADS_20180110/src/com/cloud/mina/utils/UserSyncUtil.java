package com.cloud.mina.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class UserSyncUtil {
	private static Logger log = Logger.getLogger(UserSyncUtil.class);
	/**
	 * 推送用户信息到EMR
	 * /AddPatientInfo/{phone}/{email}/{name}/{sex}/{birth}/{locationCode}/{appType}
	 * @param jo  {'opeType':'delete','deviceId':'xxx12345','phone':'13701115948','name':'郭义华','appType':'WSYD;WSJK;WSXD','ywid':2}
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean pushUserInfoToEMR(JSONObject jo){
		String phone = jo.getString("phone");
		String name = jo.getString("name");
		String appType = jo.getString("appType");
		StringBuffer url = new StringBuffer(PropertiesReader.getProp("EMR_URL_USER"));
		String result = null;
		try {
			url.append("/service/AddPatientInfo")
			.append("/").append(phone)
			.append("/").append(phone+"@139.com")
			.append("/").append(URLEncoder.encode(name,"UTF-8"))
			.append("/").append(1)
			.append("/").append("1970-01-01")
			.append("/").append("CMRI")
			.append("/").append(appType);
		
			result = HttpClientExecute.executeGet(url.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result!=null && result.contains("success")){
			log.info("EMR 删除用户成功：result="+result);
			String sql = "update usertbl set emr_flag='1' where patientID=?";
			C3P0Util.insertOrUpdateData(sql, phone);
			return true;
		}else{
			log.info("EMR 删除用户失败：result="+result);
			return false;
		}
	}
	public static boolean deleteUserFromEMR(JSONObject jo){
		String phone = jo.getString("phone");
		String appType = jo.getString("appType");
		if("".equals(appType)){
			appType = "all";
		}
		StringBuffer url = new StringBuffer(PropertiesReader.getProp("EMR_URL_USER"));
		String result = null;
		try {
			url.append("/service/DeletePatientInfo")
			.append("/").append(phone)
			.append("/").append(appType);
			
			result = HttpClientExecute.executeGet(url.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean ret = false;
		if(result!=null && result.contains("success")){
			log.info("EMR 删除用户成功！"+jo.toString());
			String sql = "delete from usertbl where ywId=?";
			ret = C3P0Util.insertOrUpdateData(sql, jo.getString("ywid"));
			if(ret){
				log.info("DADS删除用户成功！"+jo.toString());
			}else{
				log.info("DADS删除用户失败！"+jo.toString());
			}
		}else{
			log.info("EMR 删除用户失败！"+jo.toString());
		}
		return ret;
	}
}
