package com.cloud.mina.utils;

public class Constants {
    // activate syno
	public final static String[] COMPANY_ARR = {
		"PHR2.0"
//		,
//		"HLJP"
		};
	public final static String[] ACTIVATE_URL_ARR = {
//		"http://183.224.40.209/iactivity/PhrUserManageJ.do?action=queryActiveUserInfo"  //云南
//		"http://mhealth.cmri.cn/iactivity/PhrUserManageJ.do?action=queryActiveUserInfo" //工会
		"http://10.2.56.34/iactivity/PhrUserManageJ.do?action=queryActiveUserInfo" //基地
//		,
//		"http://10.117.91.132/iactivity/PhrUserManageJ.do?action=queryActiveUserInfo"
		};
	
	/**
	 * 位置信息开关
	 */
	public static final String LocationFlag="off";
	/**
	 * EMR插入血压数据接口路径
	 */
	public static final String BP2EMR_INSERT = "http://localhost:8080/EMR/service/bloodPressureDataInsert/%s/%s/%s/%s/%s/%s";
	
	public static final String PHR_UserParam = "http://phr.cmri.cn/data/openApi.do?action=queryAllUserParam";
	
	/**
	 * send to PHR2.0 IP:183  all
	 * 我尚运动：http://phr.cmri.cn/datav2/DataGatePlatformInteract.do?action=JBQUploadData
	 * 我尚健康：http://phr.cmri.cn/datav4/receiveData.do?action=JBQUploadData
	 * 蓝汛机房: http://223.202.47.136/datav4/receiveData.do?action=JBQUploadData
	 */	
	public static final String PHR_V2_OPENAPI = "";
	/**
	 * 蓝汛机房EMR: http://10.2.56.32/EMR/service/WsSportDataInsert
	 * 本地测试EMR: http://10.2.44.105:8080/EMR/service/WsSportDataInsert?format=json
	 */
	public static final String EMR1PHR_OPENAPI = "";
/*****************************************************************************************************************************************/
//                                         URL        华丽分割线                 dataType
/*****************************************************************************************************************************************/
	// pager default parameters
	/**
	 * 详细包
	 */
	public final static String DATATYPE_STEPDETAIL = PropertiesReader.getProp("DATATYPE_STEPDETAIL");
	/**
	 * 有效步数
	 */
	public final static String DATATYPE_STEPEFFECTIVE = PropertiesReader.getProp("DATATYPE_STEPEFFECTIVE");
	/**
	 * 简要包
	 */
	public final static String DATATYPE_STEPCOUNT = PropertiesReader.getProp("DATATYPE_STEPCOUNT");
	/**
	 * 睡眠
	 */
	public final static String DATATYPE_SLEEP = PropertiesReader.getProp("DATATYPE_SLEEP");
	/**
	 * 心功能运动
	 */
	public final static String DATATYPE_HEARTFUNCTION = PropertiesReader.getProp("DATATYPE_HEARTFUNCTION");
	/**
	 * 日常运动
	 */
	public final static String DATATYPE_DAILYEXERCISE = PropertiesReader.getProp("DATATYPE_DAILYEXERCISE");
	/**
	 * 血压
	 */
	public final static String DATATYPE_BLOODPRESSURE = PropertiesReader.getProp("DATATYPE_BLOODPRESSURE");
	/**
	 * 血糖
	 */
	public final static String DATATYPE_BLOODGLUCOSE = PropertiesReader.getProp("DATATYPE_BLOODGLUCOSE");
	/**
	 * 心电
	 */
	public final static String DATATYPE_ECG = PropertiesReader.getProp("DATATYPE_ECG");
	/**
	 * 体重
	 */
	public final static String DATATYPE_WEIGHT = PropertiesReader.getProp("DATATYPE_WEIGHT");
	/**
	 * 血氧
	 */
	public final static String DATATYPE_SPO2 = PropertiesReader.getProp("DATATYPE_SPO2");
	
}
