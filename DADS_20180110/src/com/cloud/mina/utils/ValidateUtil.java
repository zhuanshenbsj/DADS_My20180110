package com.cloud.mina.utils;

import java.util.regex.Pattern;

public class ValidateUtil {
	/**
	 * @param string
	 * @return Boolean
	 */
	public static Boolean isValid(String string)
	{
		if(string==null || "".equals(string.trim()))
			return false;
		else
			return true;
	}
	
	/**
	 * @param pageno
	 * @param total
	 * @return pageno
	 */
	@SuppressWarnings("finally")
	public static int isLegalPagenoUtil(int pageno, int total) {
		try {
			if (pageno < 1)
				pageno = 1;
			else if (pageno > total)
				pageno = total;
		} catch (Exception e) {
			pageno = 1;
		}finally{
			return pageno;
		}
	}
	/**
	 * check the parameters is empty(null || "") or not if has empty return false,
	 * @param param
	 * @return
	 */
	public static Boolean paramCheck(Object...param) {
		for(Object o : param){
			if(o instanceof String){
				if(!ValidateUtil.isValid((String) o)){
					return false;
				}
			}else{
				if(o == null){
					return false;
				}
			}
		}
		return true;
	}
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 }
	/***
	 * check restful interface datetime param
	 * @param dateTime
	 * @return
	 */
	public static String checkDateTime(String dateTime){
		String returnstr = "";
		int length = dateTime==null?0:dateTime.length();
		if(length==14&&isPositiveInteger(dateTime)){
			returnstr = "";
		}else if(length==10&&dateTime.indexOf('-')!=-1){
			returnstr = "";
		}else if(length==8&&dateTime.indexOf(':')!=-1){
			returnstr = "";
		}else if(length==19&&dateTime.indexOf(':')!=-1){
			returnstr = "";
		}else{
			returnstr = "无效的时间 ！";
		}
		return returnstr;
	}

	/***
	 * validate the idcard
	 * @param idcard
	 * @return
	 */
	public static String checkIdCard(String idcard){
		String isIdcard = "";
		if(idcard==null||(idcard.length()!=15&&idcard.length()!=18)){
			isIdcard = "无效的身份证号！";
		}
		return isIdcard;
	}
	/***
	 * validate the number
	 * @param number
	 * @param filedname
	 * @return  
	 */
	public static String checkNumber(String number,String filedname){
		return isNonNegativeInteger(number)?"":filedname+"字段值无效！  ";
	}
	/***
	 * validate the number
	 * @param number
	 * @param filedname
	 * @return  
	 */
	public static String checkNumber(int number,String filedname){
		return number>=0?"":filedname+"字段值无效！ ";
	}
	/***
	 * validate the decimal
	 * @param number
	 * @param filedname
	 * @return
	 */
	public static String checkDecimal(String number,String filedname){
		return isPositiveDouble(number)?"":filedname+"字段值无效！ ";
	}
	/***
	 * validate the apptype
	 * @param appType
	 * @return
	 */
	public static String checkAppType(String appType){
		boolean app_boolean = false;
		if("tx".equals(appType)||"ws".equals(appType)||"sleep".equals(appType)||"etcomm".equals(appType)||"chronic".equals(appType))
			app_boolean = true;
		return app_boolean==true?"":"系统类型无效！";
	}
	/***
	 * validate the positive double/float number
	 * @param number
	 * @return
	 */
	public static boolean isPositiveDouble(String number) {    
	    boolean isNumber = false;    
	    isNumber = number.matches("^0|([1-9]+[0-9]*)(\\.[0-9]+)?$");    
	    return isNumber;    
	}
	/***
	 * validate the positive int number
	 * @param number
	 * @return
	 */
	public static boolean isPositiveInteger(String number){
		boolean isNumber = false;
		isNumber = number.matches("^([1-9]+[0-9]*)$");
		return isNumber;
	}
	/***
	 * validate the NonNegative int number
	 * @param number
	 * @return
	 */
	public static boolean isNonNegativeInteger(String number){
		boolean isNumber = false;
		isNumber = number.matches("^0|([1-9]+[0-9]*)$");
		return isNumber;
	}
	/**
	 * 验证计步器自动上传时间格式是否规范
	 * @param time
	 * @return
	 */
	public static boolean checkAutoUploadTime(String time){
		if(time==null)
			return false;
	    return time.matches("^(([0][1-4])|([1]([2-6]|9))|([2][0-3])):[0-5][0-9]$");    
	}
	public static void main(String[] args) {
		System.out.println(isPositiveInteger("2"));
		System.out.println(isNonNegativeInteger("222"));
		System.out.println(isPositiveDouble(1+".1"));
	}
}
