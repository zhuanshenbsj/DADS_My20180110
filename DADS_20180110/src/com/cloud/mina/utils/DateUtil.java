package com.cloud.mina.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	/**
	 * yyyy-MM-dd
	 */
	public static SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * yyyyMMddHHmmss
	 */
	public static SimpleDateFormat compact_formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 获得当前的日期
	 * 格式：yyyy-MM-dd
	 */
	public static String getToday(){
		Calendar cal = Calendar.getInstance();
		synchronized (ft) {
			return ft.format(cal.getTime());
		}
	}
	/**
	 * 获得当前时间
	 * 格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		synchronized (formatter) {
			return formatter.format(cal.getTime());
		}
	}
	/**
	 * 将给定格式的时间字符串格式化为：yyyy-MM-dd
	 * 例：2013-1-1经格式化后为：2013-01-01
	 * @param str 格式：yyyy-M-d
	 * @return 格式：yyyy-MM-dd
	 */
	public static String formatDate(String str){
		try {
			synchronized (ft) {
				return ft.format(ft.parse(str));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 将给定格式的时间字符串格式化 
	 * <br>例：2013-1-1 1:1:0 经格式化后为：2013-01-01 01:01:00
	 * <br>2013-1-1 经格式化后为：2013-01-01
	 * <br>20130101 经格式化后为：2013-01-01
	 * @param str 可接受格式：yyyy-M-d H:m:s || yyyy-M-d || yyyyMMdd
	 * @return 标准格式：yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || yyyy-MM-dd
	 */
	public static synchronized String format(String str){
		if(str == null){
			return null;
		}
		try {
			if(str.contains(":")){
				return formatter.format(formatter.parse(str));
			}else if (str.contains("-")) {
				return ft.format(ft.parse(str));
			}else if(str.length()==8){
				String date = str.substring(0,4)+"-"+str.substring(4,6)+"-"+str.substring(6,8);
				return date;
			}else if(str.length()==14){
				return formatRestfulDate(str);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 将给定格式的时间字符串格式化 
	 * <br>例：2013-12-1 13:14:0 经格式化后为：20131201131400
	 * @param str 可接受格式：yyyy-M-d H:m:s
	 * @return 标准格式：yyyyMMddHHmmss
	 */
	public static synchronized String getCompactDatetime(String str){
		try {
			return compact_formatter.format(formatter.parse(str));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 指定时间指定分钟偏移量后的时间
	 * @param str 格式：yyyy-MM-dd HH:mm:ss
	 * @param add 分钟偏移量，正数向后，负数向后
	 * @return 格式：yyyy-MM-dd HH:mm:ss
	 */
	public static synchronized String addMinutes(String str,int add){
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(formatter.parse(str));
			cal.add(Calendar.MINUTE, add);
			return formatter.format(cal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 指定时间指定小时偏移量后的时间
	 * @param str 格式：yyyy-MM-dd HH:mm:ss
	 * @param add 分钟偏移量，正数向后，负数向后
	 * @return 格式：yyyy-MM-dd HH:mm:ss
	 */
	public static synchronized String addHour(String str,int add){
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(formatter.parse(str));
			cal.add(Calendar.HOUR, add);
			return formatter.format(cal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 * @return stepdate
	 */
	public static String getStepdate(int year, int month, int day) {
		try {
			if(year<100){
				year += 2000;
			}
			String date = year+"-"+month+"-"+day;
			date = DateUtil.format(date);
			//The Same Day' data
			if(date.equals(DateUtil.getToday())){
				Calendar cal = Calendar.getInstance();
				synchronized (compact_formatter) {
					return compact_formatter.format(cal.getTime());
				}
			}else{
				date = date.replace("-", "");
				return date+"235959";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获取简要包数据时间，若不是今天则默认为23点59分59秒
	 * @param date  yyyy-MM-dd
	 * @return stepdate
	 */
	public static String getStepdate(String date) {
		try {
			date = formatDate(date);
			//The Same Day' data
			if(date.equals(getToday())){
				Calendar cal = Calendar.getInstance();
				synchronized (compact_formatter) {
					return compact_formatter.format(cal.getTime());
				}
			}else{
				date = date.replace("-", "");
				return date+"235959";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 将给定的数字年、月、日格式化为 YYYY-MM-DD
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String getDate(int year,int month,int day){
		Calendar cal = Calendar.getInstance();
		if(year<100)year =year+2000;
		cal.set(year, month-1, day);
		synchronized (ft) {
			return ft.format(cal.getTime());
		}
	}
	public static Date getDate(String str){
		try {
			synchronized (ft) {
				return ft.parse(str);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String getTime(int hour,int minute){
		StringBuffer sb = new StringBuffer();
		if(hour<10){
			sb.append("0");
		}
		sb.append(hour).append(":");
		if (minute<10) {
			sb.append("0");
		}
		sb.append(minute);
		return sb.toString();
	}
	/**
	 * 用于格式化restful协议中的时间传输格式的解析
	 * @param date yyyyMMddHHmmss
	 * @return  符合规范则返回 yyyy-MM-dd HH:mm:ss 类型数据，不符合规范的不做处理
	 */
	public static String formatRestfulDate(String date){
		//判断时间是否为空，长度是否满足解析要求
		if(null!=date&&date.length()==14){
			StringBuffer sb = new StringBuffer();
			sb.append(date.substring(0, 4)).append('-').append(date.substring(4, 6)).append('-').append(date.substring(6, 8))
			.append(' ').append(date.substring(8, 10)).append(':').append(date.substring(10, 12)).append(':').append(date.substring(12, 14));
			return sb.toString();
		}
		return date;
	}
	/**
	 * 获得指定格式的日期的指定偏移量后的日期
	 * @param dateString 格式：yyyy-MM-dd
	 * @param num 偏移量 正数代表向后偏移，负数向前偏移
	 * @return 格式：yyyy-MM-dd
	 */
	public static synchronized String getDayAdd(String dateString,int num){
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(ft.parse(dateString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.add(Calendar.DAY_OF_MONTH, num);
		return ft.format(cal.getTime());
	}
	/**
	 * 将给定格式的时间字符串转换为Calendar对象
	 * @param currentTime yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static synchronized Calendar getCalendar(String currentTime) {
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(formatter.parse(currentTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal;
	}
	/**
	 * 测试方法
	 * @throws Exception 
	 */
	public static void main(String[] args){
		System.out.println(getToday());
		System.out.println(getCurrentTime());
		System.out.println(format("2013-1-1 1:1:0"));
		System.out.println(format("20131101"));
		System.out.println(getStepdate(13, 9, 11));
		System.out.println(getCompactDatetime("2013-12-1 13:14:0"));
	}
}
