package com.cloud.mina.utils;

import java.util.HashMap;
import java.util.List;

public class SportDataStatisticsUtil {
	private static String date = DateUtil.getToday();
	private static String time1 = "01:00-04:00";
	private static String time2 = "12:00-16:00";
	private static String time3 = "19:00-23:00";
	private static String time1begin = " 01:00:00";
	private static String time1end = " 04:03:00";
	private static String time2begin = " 12:00:00";
	private static String time2end = " 16:03:00";
	private static String time3begin = " 19:00:00";
	private static String time3end = " 23:59:59";

	/**
	 * 每日数据统计
	 */
	public static void statistics() {
		Logger.writeLog("start data statistics...!");
		date = DateUtil.getDayAdd(DateUtil.getToday(), -1);
		String company = null;
		List<HashMap<String, String>> companyList = C3P0Util
				.getData("select distinct company from usertbl");
		if (companyList == null || companyList.size() == 0)
			return;
		for (int i = 0; i < companyList.size(); i++) {
			company = companyList.get(i).get("company");
			if (company == null || "".equals(company) || "test".equals(company)
					|| "TTTT".equals(company))
				continue;
			// yesterday data statistics
			statisticsByTime(company, date, time1, time1begin, time1end);
			statisticsByTime(company, date, time2, time2begin, time2end);
			statisticsByTime(company, date, time3, time3begin, time3end);

			// today morning data statistics
			statisticsByTime(company, DateUtil.getToday(), time1, time1begin,
					time1end);
			statisticsByTime(company, DateUtil.getToday(), time2, time2begin,
					time2end);
		}
		statisticsByTime("ALL", date, time1, time1begin, time1end);
		statisticsByTime("ALL", date, time2, time2begin, time2end);
		statisticsByTime("ALL", date, time3, time3begin, time3end);
		statisticsByTime("ALL", DateUtil.getToday(), time1, time1begin,
				time1end);
		statisticsByTime("ALL", DateUtil.getToday(), time2, time2begin,
				time2end);
		Logger.writeLog("end data statistics...!");
	}

	/**
	 * 
	 * @param company ��˾
	 * @param date ����
	 * @param time ʱ���
	 * @param timebegin ��ʼʱ��
	 * @param timeend ����ʱ��
	 */
	private static void statisticsByTime(String company, String date,
			String time, String timebegin, String timeend) {
		String sql = null;
		String condition1 = "";
		String condition2 = "";
		int receiveNum;
		int sendNum;
		int exceptionNum;
		int zeroValueNum;
		if (company != null && !"All".equalsIgnoreCase(company)) {
			condition1 = " and company='" + company + "'";
			condition2 = " and p.deviceID like '" + company + "%'";
		}
		String statisticsTime = DateUtil.getCurrentTime();
		sql = "select count(distinct patientID) from packagedata p where p.receiveTime between '"
				+ date
				+ timebegin
				+ "' and '"
				+ date
				+ timeend
				+ "'"
				+ condition1;
		receiveNum = C3P0Util.getCount(sql);
		sql = "select count(distinct patientID) from packagedata p where p.receiveTime between '"
				+ date
				+ timebegin
				+ "' and '"
				+ date
				+ timeend
				+ "' and p.flag != '0'" + condition1;
		sendNum = C3P0Util.getCount(sql);
		sql = "select count(distinct patientID) from exceptionuser p where p.loginTime between '"
				+ date
				+ timebegin
				+ "' and '"
				+ date
				+ timeend
				+ "'"
				+ condition2;
		exceptionNum = C3P0Util.getCount(sql);
		sql = "select count(distinct patientID) from packagedata p where p.receiveTime between '"
				+ date
				+ timebegin
				+ "' and '"
				+ date
				+ timeend
				+ "' and p.packageNum in('8-1','8-3') and p.totalSteps != '0' "
				+ condition1;
		zeroValueNum = receiveNum - C3P0Util.getCount(sql);
		sql = "select count(1) from datastatistics where date='" + date
				+ "' and time='" + time + "' and company='" + company + "'";
		if (C3P0Util.getCount(sql) > 0) {
			sql = "update datastatistics set receiveNum='" + receiveNum
					+ "',sendNum='" + sendNum + "',exceptionNum='"
					+ exceptionNum + "',zeroValueNum='" + zeroValueNum
					+ "',statisticsTime='" + statisticsTime + "' where  date='"
					+ date + "' and time='" + time + "' and company='"
					+ company + "'";
		} else {
			sql = "insert into datastatistics(date,time,company,receiveNum,sendNum,exceptionNum,zeroValueNum,statisticsTime) values('"
					+ date
					+ "','"
					+ time
					+ "','"
					+ company
					+ "',"
					+ receiveNum
					+ ","
					+ sendNum
					+ ","
					+ exceptionNum
					+ ","
					+ zeroValueNum
					+ ",'" + statisticsTime + "')";
		}
		C3P0Util.executeUpdate(sql);
	}

	/**
	 * ���Է���
	 * @param args
	 */
	public static void main(String[] args) {
		//		statisticsByTime("M828","2013-08-26",time1," 12:00:00", " 15:00:00");
		statistics();
		//		System.out.println(DateUtil.getCurrentTime());
		//		System.out.println(DateUtil.getCurrentTime());
	}
}
