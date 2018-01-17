package com.cloud.mina.utils;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class OldSportDataHandleUtil {
	private static Logger log = Logger.getLogger(OldSportDataHandleUtil.class);
	/**
	 * 处理详细包
	 * @param data
	 * @param result
	 * void
	 */
	public static void handleStepDetail(JSONObject data, JSONObject result) {
		log.info("begin call handleStepDetail...");
		try {
			String deviceID = data.getString("deviceId");
			String patientID = DeviceIDResolver.searchPatientidByDeviceid(deviceID);
			String company = DeviceIDResolver.searchCompanyByDeviceid(deviceID);
			String date = data.getString("collectDate");
			date = date.substring(0,10).replace("-", "");
			String filename = date+".txt";
			JSONArray datavalue = data.getJSONArray("dataValue");
			data.remove("appType");
			data.remove("deviceId");
			data.remove("collectDate");
			data.remove("dataType");
			data.put("datatype", "STEPCOUNT2");
			boolean ret = false;
			if(datavalue.size()==0){
				ret = true;
			}else if(datavalue.get(0) instanceof JSONObject){
				log.info("单个小时数据--详细包！");
				 ret = saveStepDetail(data, deviceID, patientID, company,
						date, filename, datavalue);
			}else{
				log.info("多个小时数据--详细包！");
				for(int i=0;i<datavalue.size();i++){
					ret = saveStepDetail(data, deviceID, patientID, company,
							date, filename, datavalue.getJSONArray(i));
				}
			}
			if(ret){
				result.put("status", "SUCCESS");
			}else{
				result.put("status", "FAIL");
			}
			
			log.info("end call handleStepDetail.");
		} catch (Exception e) {
			result.put("status", "FAIL");
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	/**
	 * 保存一小时的运动详细包
	 * @param data
	 * @param deviceID
	 * @param patientID
	 * @param company
	 * @param date
	 * @param filename
	 * @param datavalue
	 * @return
	 */
	private static boolean saveStepDetail(JSONObject data, String deviceID,
			String patientID, String company, String date, String filename,
			JSONArray datavalue) {
		JSONObject jo = new JSONObject();
		JSONObject hour = datavalue.getJSONObject(5);//JSONObject.fromObject(datavalue.remove(5));
		//hour 的索引可能是 5，或者是 6
		if(!hour.containsKey("hour"))
			hour = datavalue.getJSONObject(6);
		//旧协议中，stepDetail datavalue定长为6， 多则移除
		while(datavalue.size()>6){
			datavalue.remove(6);
		}
		data.put("hour", hour.getString("hour"));
		data.remove("dataValue");
		data.put("datavalue", datavalue);
		jo.put("data", data);
		String detail_data = filename+" "+patientID+" \""+jo.toString()+"\"";
		String tablename = ValidateUtil.isValid(patientID)?"packagedata":"exceptiondata";
		String sql = "insert into "+tablename+" (patientID,company,deviceID,STEP2NEWVALUE,packageNum,receiveTime,realTime,deviceType) values(?,?,?,?,?,now(),?,?)";
		boolean ret = C3P0Util.insertOrUpdateData(sql, patientID,company,deviceID,detail_data,"8-2",date,"PHONE");
		return ret;
	}
	/**
	 * 处理有效步数包
	 * @param data
	 * @param result
	 * void
	 */
	public static void handleStepEffective(JSONObject data, JSONObject result) {
		log.info("begin call handleStepEffective...");
		try {
			String deviceID = data.getString("deviceId");
			String patientID = DeviceIDResolver.searchPatientidByDeviceid(deviceID);
			String company = DeviceIDResolver.searchCompanyByDeviceid(deviceID);
			String date = data.getString("collectDate");
			date = date.substring(0,10).replace("-", "");
			String filename = date+"_yxbs.txt";
			JSONArray datavalue = data.getJSONArray("dataValue");
			data.remove("appType");
			data.remove("deviceId");
			data.remove("collectDate");
			data.remove("dataType");
			data.put("datatype", "STEPCOUNT3");
			boolean ret = false;
			if(datavalue.size()==0){
				ret = true;
			}else if(datavalue.get(0) instanceof JSONObject){
				log.info("单个小时数据--有效步数！");
				 ret = saveStepEffective(data, deviceID, patientID, company,
						date, filename, datavalue);
			}else{
				log.info("多个小时数据--有效步数！");
				for(int i=0;i<datavalue.size();i++){
					ret = saveStepEffective(data, deviceID, patientID, company,
							date, filename, datavalue.getJSONArray(i));
				}
			}
			
			if(ret){
				result.put("status", "SUCCESS");
			}else{
				result.put("status", "FAIL");
			}
			
			log.info("end call handleStepEffective.");
		} catch (Exception e) {
			result.put("status", "FAIL");
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	/**
	 * 保存一小时的运动有效步数
	 * @param data
	 * @param deviceID
	 * @param patientID
	 * @param company
	 * @param date
	 * @param filename
	 * @param jsonArray
	 * @return
	 */
	private static boolean saveStepEffective(JSONObject data, String deviceID,
			String patientID, String company, String date, String filename,
			JSONArray datavalue) {
		JSONObject jo = new JSONObject();
		JSONObject hour = datavalue.getJSONObject(5);//JSONObject.fromObject(datavalue.remove(5));
		//hour 的索引可能是 5，或者是 6
		if(!hour.containsKey("hour"))
			hour = datavalue.getJSONObject(6);
		//旧协议中，stepDetail datavalue定长为6， 多则移除
		while(datavalue.size()>6){
			datavalue.remove(6);
		}
		data.put("hour", hour.getString("hour"));
		data.remove("dataValue");
		data.put("datavalue", datavalue);
		jo.put("data", data);
		String effective_data = filename+" "+patientID+" \""+jo.toString()+"\"";
		String tablename = ValidateUtil.isValid(patientID)?"packagedata":"exceptiondata";
		String sql = "insert into "+tablename+" (patientID,company,deviceID,STEP3NEWVALUE,packageNum,receiveTime,realTime,deviceType) values(?,?,?,?,?,now(),?,?)";
		boolean ret = C3P0Util.insertOrUpdateData(sql, patientID,company,deviceID,effective_data,"8-4",date,"PHONE");
		return ret;
	}
	/**
	 * 处理简要包
	 * @param data
	 * @param result
	 * void
	 */
	public static void handleStepCount(JSONObject data, JSONObject result) {
		log.info("begin call handleStepCount...");
		try {
			String deviceID = data.getString("deviceId");
			String patientID = DeviceIDResolver.searchPatientidByDeviceid(deviceID);
			String company = DeviceIDResolver.searchCompanyByDeviceid(deviceID);
			String date = data.getString("collectDate");
			String packageNum = (DateUtil.getToday().equals(date))?"8-3":"8-1";
			String realTime = DateUtil.getStepdate(date);
			StringBuffer datacontent = new StringBuffer(80);
			JSONArray dataValue = data.getJSONArray("dataValue");
			String stepSum = JsonUtil.getJsonParamterString(dataValue.getJSONObject(0), "stepSum");
			datacontent.append(patientID).append(" PHR ")
				.append(realTime).append(" ")
				.append(String.valueOf(100)).append(" ")
				.append(JsonUtil.getJsonParamterString(dataValue.getJSONObject(4), "weight")).append(" ")
				.append(JsonUtil.getJsonParamterString(dataValue.getJSONObject(5), "stride")).append(" ")
				.append(String.valueOf((int)(Float.parseFloat(JsonUtil.getJsonParamterString(dataValue.getJSONObject(1), "calSum"))*10))).append(" ")
				.append(stepSum).append(" ")
				.append(JsonUtil.getJsonParamterString(dataValue.getJSONObject(2), "distanceSum")).append(" ")
				.append(JsonUtil.getJsonParamterString(dataValue.getJSONObject(6), "degreeOne")).append(" ")
				.append(JsonUtil.getJsonParamterString(dataValue.getJSONObject(7), "degreeTwo")).append(" ")
				.append(JsonUtil.getJsonParamterString(dataValue.getJSONObject(8), "degreeThree")).append(" ")
				.append(JsonUtil.getJsonParamterString(dataValue.getJSONObject(9), "degreeFour")).append(" ")
				.append(JsonUtil.getJsonParamterString(dataValue.getJSONObject(10), "uploadType")).append(" ")
				.append("0");//yxbssum
			
			String tablename = ValidateUtil.isValid(patientID)?"packagedata":"exceptiondata";
			String sql = "insert into "+tablename+" (patientID,company,deviceID,data_newcontent_stepcount2,packageNum,receiveTime,totalSteps,realTime,deviceType) values(?,?,?,?,?,now(),?,?,?)";
			boolean ret = C3P0Util.insertOrUpdateData(sql, patientID,company,deviceID,datacontent.toString(),packageNum,stepSum,realTime,"PHONE");
			if(ret){
				result.put("status", "SUCCESS");
				log.info("数据保存成功！");
			}else{
				result.put("status", "FAIL");
				log.info("数据保存失败！");
			}
			log.info("end call handleStepCount.");
		} catch (Exception e) {
			result.put("status", "FAIL");
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
}
