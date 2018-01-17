package com.cloud.mina.dao;

import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.cloud.mina.entity.packageData.sports.PkgMHSportNo8One;
import com.cloud.mina.entity.packageData.sports.PkgMHSportNo8Three;
import com.cloud.mina.utils.C3P0Util;
import com.cloud.mina.utils.Constants;
import com.cloud.mina.utils.DateUtil;
import com.cloud.mina.utils.JsonUtil;
import com.cloud.mina.utils.Logger;
import com.cloud.mina.utils.MLinkCRC;
import com.cloud.mina.utils.ValidateUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Tcp协议计步器八号包数据入库通用方法
 * 项目名称：DADS   
 * 类名称：SaveSportsNo8PacketUtil   
 * 类描述：暂无 
 * 创建人：RCM
 * 创建时间：2014-5-14 下午06:35:57   
 * 修改人：
 * 修改时间：
 * 修改备注： 
 * @version
 */
public class DaoMHSportsNo8 {
	/**
	 * 8-1号历史包数据入库-老协议
	 * @param patientID
	 * @param company
	 * @param deviceID
	 * @param stepvalue
	 * @param data_newcontent_stepcount2
	 * @param realTime
	 * @param firmware_version
	 * @param deviceType
	 * @return
	 */
	public static boolean insertNo8DataForOneWay(String patientID, String company, String deviceID, String stepvalue,
			String data_newcontent_stepcount2, String realTime, String firmware_version, String deviceType) {
		// modify by renchuanmin for PWS0002 on 20130712
		//		boolean isValid = checkRetransmission(patientID,stepvalue,"8-1",5);
		boolean isValid = true;
		boolean result = true;

		// add by renchuanmin for PWS0009 on 20130821 begin *******
		String tablename = ValidateUtil.isValid(patientID) ? "packagedata" : "exceptiondata";
		// add by renchuanmin for PWS0009 on 20130821 end *******

		if (isValid) {
			String sql = "insert into " + tablename + "(patientID,company,deviceID,data_newcontent_stepcount2,"
					+ "packageNum,receiveTime,totalSteps,realTime,firmware_version,deviceType) values(?,?,?,?,?,?,?,?,?,?)";
			result = C3P0Util.insertOrUpdateData(sql, patientID, company, deviceID, data_newcontent_stepcount2, "8-1",
					DateUtil.getCurrentTime(), stepvalue, realTime, firmware_version, deviceType);
			if (result) {
				Logger.writeLog("insertNo8DataForOneWay execute result : success. sql:" + sql);
			} else {
				Logger.writeLog("insertNo8DataForOneWay execute result : failure. sql:" + sql);
			}
		} else {
			Logger.writeLog("Retransmission No.8-1：" + data_newcontent_stepcount2);
		}
		return result;
	}

	public static boolean insertNo8DataForOneWay1(IoSession session, PkgMHSportNo8One pkgData) {

		//拼接入库的一个字段
		StringBuffer datacontent = new StringBuffer(80);
		datacontent.append(session.getAttribute("patientId")).append(" PHR ").append(pkgData.getStepdate()).append(" ")
				.append(pkgData.getBattery()).append(" ").append(pkgData.getWeight()).append(" ")
				.append(pkgData.getStride()).append(" ").append(pkgData.getKcal()).append(" ").append(pkgData.getStep())
				.append(" ").append(pkgData.getDistance()).append(" ").append(pkgData.getLevel1()).append(" ")
				.append(pkgData.getLevel2()).append(" ").append(pkgData.getLevel3()).append(" ")
				.append(pkgData.getLevel4()).append(" ").append(pkgData.getTran_type()).append(" ")
				.append(pkgData.getEffective_step());

		//判断是否为有效包
		boolean isValid = true;
		String tablename = ValidateUtil.isValid(session.getAttribute("patientId").toString()) ? "packagedata"
				: "exceptiondata";

		boolean result = true;
		//插入
		if (isValid) {
			String sql = "insert into " + tablename + "(patientID,company,deviceID,data_newcontent_stepcount2,"
					+ "packageNum,receiveTime,totalSteps,realTime,firmware_version,deviceType) values(?,?,?,?,?,?,?,?,?,?)";
			result = C3P0Util.insertOrUpdateData(sql, session.getAttribute("patientId").toString(),
					session.getAttribute("company").toString(), session.getAttribute("deviceID").toString(),
					datacontent.toString(), "8-1", DateUtil.getCurrentTime(), String.valueOf(pkgData.getStep()),
					pkgData.getStepdate(), pkgData.getFirmware_version(), pkgData.getPrefix());

			if (result) {
				Logger.writeLog("insertNo8DataForOneWay execute result : success. sql:" + sql);
			} else {
				Logger.writeLog("insertNo8DataForOneWay execute result : failure. sql:" + sql);
			}
		} else {
			Logger.writeLog("Retransmission No.8-1：" + datacontent);
		}
		return result;
	}

	/**
	 * 8-2号详细包数据入库-老协议
	 * @param patientID
	 * @param company
	 * @param deviceID
	 * @param stepcount2data
	 * @param stepdate
	 * void
	 */
	public static boolean insertNo8DataForTwoWay(String patientID, String company, String deviceID,
			String stepcount2data, String stepdate) {
		String STEP2FILENAME = stepdate + ".txt";
		String STEP2NEWVALUE = STEP2FILENAME + " " + patientID + " \"" + stepcount2data + "\"";

		// add by renchuanmin for PWS0009 on 20130821 begin *******
		String tablename = ValidateUtil.isValid(patientID) ? "packagedata" : "exceptiondata";
		// add by renchuanmin for PWS0009 on 20130821 end *******

		String sql = "insert into " + tablename
				+ "(patientID,company,deviceID,STEP2NEWVALUE,packageNum,receiveTime,realTime) values(?,?,?,?,?,now(),?)";
		boolean result = C3P0Util.insertOrUpdateData(sql, patientID, company, deviceID, STEP2NEWVALUE, "8-2", stepdate);
		if (result) {
			Logger.writeLog("insertNo8DataForTwoWay execute success");
		} else {
			Logger.writeLog("insertNo8DataForTwoWay execute failure");
		}
		return result;
	}

	/**
	 * 8-3号简要包数据入库-老协议
	 * @param patientID
	 * @param company
	 * @param device_id
	 * @param stepvalue
	 * @param data_newcontent_stepcount2
	 * @param locationInfoStr
	 * @param realTime
	 * @param firmware_version
	 * @param deviceType
	 * @return
	 */
	public static boolean insertNo8DataForThreeWay(String patientID, String company, String device_id, String stepvalue,
			String data_newcontent_stepcount2, String locationInfoStr, String realTime, String firmware_version,
			String deviceType) {

		// modify by renchuanmin for PWS0002 on 20130712
		//		boolean isValid = checkRetransmission(patientID,stepvalue,"8-3",5);
		boolean isValid = true;
		boolean result = true;
		if (isValid) {
			// add by renchuanmin for PWS0009 on 20130821 begin *******
			String tablename = ValidateUtil.isValid(patientID) ? "packagedata" : "exceptiondata";
			// add by renchuanmin for PWS0009 on 20130821 end *******

			String sql = "insert into " + tablename + "(patientID,company,deviceID,data_newcontent_stepcount2,"
					+ "packageNum,receiveTime,totalSteps,location,realTime,firmware_version,deviceType) values(?,?,?,?,?,?,?,?,?,?,?)";
			C3P0Util.insertOrUpdateData(sql, patientID, company, device_id, data_newcontent_stepcount2, "8-3",
					DateUtil.getCurrentTime(), stepvalue, locationInfoStr, realTime, firmware_version, deviceType);
			if (result) {
				Logger.writeLog("insertNo8DataForThreeWay execute success, sql : " + sql);
			} else {
				Logger.writeLog("insertNo8DataForThreeWay execute failure, sql : " + sql);
			}
		} else {
			Logger.writeLog("Retransmission No.8-3：  data_newcontent_stepcount2" + data_newcontent_stepcount2);
		}
		return result;
	}

	/**
	 * 8-4号有效步数数据入库-老协议
	 * @param patientID
	 * @param company
	 * @param deviceID
	 * @param stepcount3data_tran
	 * @param stepdate
	 * @return
	 */
	public static boolean insertNo8DataForFourWay(String patientID, String company, String deviceID,
			String stepcount3data_tran, String stepdate) {
		String STEP3FILENAME = stepdate + "_yxbs.txt";
		String STEP3NEWVALUE = STEP3FILENAME + " " + patientID + " \"" + stepcount3data_tran + "\"";
		// add by renchuanmin for PWS0009 on 20130821 begin *******
		String tablename = ValidateUtil.isValid(patientID) ? "packagedata" : "exceptiondata";
		// add by renchuanmin for PWS0009 on 20130821 end *******
		String sql = "insert into " + tablename
				+ "(patientID,company,deviceID,STEP3NEWVALUE,packageNum,receiveTime,realTime) values(?,?,?,?,?,now(),?)";
		boolean result = C3P0Util.insertOrUpdateData(sql, patientID, company, deviceID, STEP3NEWVALUE, "8-4", stepdate);
		if (result) {
			Logger.writeLog("insertNo8DataForFourWay execute success");
		} else {
			Logger.writeLog("insertNo8DataForFourWay execute failure");
		}
		return result;
	}

	/**
	 * 以新协议格式存储运动数据-历史包
	 * @param session
	 * @param packet
	 * @return
	 */
	public static boolean saveNewSportHistory(IoSession session, PkgMHSportNo8One packet) {
		Logger.writeLog("以新协议格式存储-历史包数据！");
		JSONArray jsArr = new JSONArray();
		JsonUtil.addEntryToJsonArray(jsArr, "stepSum", packet.getStep() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "calSum", packet.getKcal() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "distanceSum", packet.getDistance() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "yxbsSum", packet.getEffective_step() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "weight", packet.getWeight() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "stride", packet.getStride() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "degreeOne", packet.getLevel1() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "degreeTwo", packet.getLevel2() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "degreeThree", packet.getLevel3() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "degreeFour", packet.getLevel4() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "uploadType", packet.getTran_type() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "measureTime", DateUtil.formatRestfulDate(packet.getStepdate()));

		String sql = "insert into sports(phone,deviceId,apptype,dataType,realTime,datavalue,receiveTime) values(?,?,?,?,?,?,now())";
		boolean ret = C3P0Util.insertOrUpdateData(sql, (String) session.getAttribute("patientId"),
				(String) session.getAttribute("deviceId"), (String) session.getAttribute("appType"),
				Constants.DATATYPE_STEPCOUNT, DateUtil.format(packet.getStepdate()), jsArr.toString());
		return ret;
	}

	/**
	 * 存储新协议格式-详细包
	 * @param session
	 * @param data
	 * @param stepdate
	 */
	public static boolean saveNewSportDetail(IoSession session, String data, String stepdate) {
		Logger.writeLog("以新协议格式存储-详细包数据！");
		JSONObject jo = JSONObject.fromObject(data);
		JSONObject dataJson = jo.getJSONObject("data");
		String hour = dataJson.getString("hour");
		JSONArray dataValue = dataJson.getJSONArray("datavalue");
		JsonUtil.addEntryToJsonArray(dataValue, "hour", hour);
		JsonUtil.addEntryToJsonArray(dataValue, "measureTime", DateUtil.format(stepdate));

		String sql = "insert into sports(phone,deviceId,apptype,dataType,realTime,datavalue,receiveTime) values(?,?,?,?,?,?,now())";
		boolean ret = C3P0Util.insertOrUpdateData(sql, (String) session.getAttribute("patientId"),
				(String) session.getAttribute("deviceId"), (String) session.getAttribute("appType"),
				Constants.DATATYPE_STEPDETAIL, DateUtil.format(stepdate), dataValue.toString());
		return ret;
	}

	/**
	 * 以新协议格式存储运动数据-简要包
	 * @param session
	 * @param packet
	 * @return
	 */
	public static boolean saveNewSportSimple(IoSession session, PkgMHSportNo8Three packet) {
		Logger.writeLog("以新协议格式存储-简要包数据！");
		JSONArray jsArr = new JSONArray();
		JsonUtil.addEntryToJsonArray(jsArr, "stepSum", packet.getStep() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "calSum", packet.getKcal() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "distanceSum", packet.getDistance() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "yxbsSum", packet.getEffective_step() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "weight", packet.getWeight() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "stride", packet.getStride() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "degreeOne", packet.getLevel1() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "degreeTwo", packet.getLevel2() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "degreeThree", packet.getLevel3() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "degreeFour", packet.getLevel4() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "uploadType", packet.getTran_type() + "");
		JsonUtil.addEntryToJsonArray(jsArr, "measureTime", DateUtil.formatRestfulDate(packet.getStepdate()));

		String sql = "insert into sports(phone,deviceId,apptype,dataType,realTime,datavalue,receiveTime) values(?,?,?,?,?,?,now())";
		boolean ret = C3P0Util.insertOrUpdateData(sql, (String) session.getAttribute("patientId"),
				(String) session.getAttribute("deviceId"), (String) session.getAttribute("appType"),
				Constants.DATATYPE_STEPCOUNT, DateUtil.format(packet.getStepdate()), jsArr.toString());
		return ret;
	}

	/**
	 * 存储新协议格式-有效步数
	 * @param session
	 * @param data
	 * @param stepdate
	 */
	public static boolean saveNewSportEffective(IoSession session, String data, String stepdate) {
		Logger.writeLog("以新协议格式存储-有效步数包！");
		JSONObject jo = JSONObject.fromObject(data);
		JSONObject dataJson = jo.getJSONObject("data");
		String hour = dataJson.getString("hour");
		JSONArray dataValue = dataJson.getJSONArray("datavalue");
		JsonUtil.addEntryToJsonArray(dataValue, "hour", hour);
		JsonUtil.addEntryToJsonArray(dataValue, "measureTime", DateUtil.format(stepdate));

		String sql = "insert into sports(phone,deviceId,apptype,dataType,realTime,datavalue,receiveTime) values(?,?,?,?,?,?,now())";
		boolean ret = C3P0Util.insertOrUpdateData(sql, (String) session.getAttribute("patientId"),
				(String) session.getAttribute("deviceId"), (String) session.getAttribute("appType"),
				Constants.DATATYPE_STEPEFFECTIVE, DateUtil.format(stepdate), dataValue.toString());
		return ret;
	}

	/**
	 * add by renchuanmin for PWS0002 on 20130712<br>
	 * check package8 8-1,8-3 retransmission is or not.<br>
	 * @param patientID
	 * @param stepvalue
	 * @param minutes
	 * @return
	 */
	private static boolean checkRetransmission(String patientID, String stepvalue, String packageNum, int minutes) {
		String endTime = DateUtil.getCurrentTime();
		String startTime = DateUtil.addMinutes(endTime, -minutes);
		String sql = "SELECT count(*) FROM packagedata WHERE patientID='" + patientID + "' AND packageNum='"
				+ packageNum + "'  AND receiveTime " + "BETWEEN '" + startTime + "' AND '" + endTime
				+ "' AND totalSteps='" + stepvalue + "';";
		int count = C3P0Util.getCount(sql);
		if (count == 0) {
			return true;
		} else {
			Logger.writeLog("find repeat package data, sql is : " + sql);
			return false;
		}
	}

	/**
	 * add by rencm on 2013-9-12下午01:52:27
	 */
	public static void sendNo8Ack(IoSession out, boolean result, int type) {
		byte[] ack = new byte[13];
		byte[] crc_c = new byte[2];
		ack[0] = -89;
		ack[1] = -72;
		ack[2] = 0;
		ack[3] = 1;
		ack[4] = 0;
		ack[5] = 0;
		ack[6] = 0;
		ack[7] = 13;
		ack[8] = 8;
		ack[9] = (byte) type;
		if (result)
			ack[10] = 14;//success
		else
			ack[10] = 15;//fail

		crc_c = MLinkCRC.crc16(ack);
		ack[11] = crc_c[0];
		ack[12] = crc_c[1];
		out.write(IoBuffer.wrap(ack));
		Logger.writeLog("return No8-" + type + " ACK end" + Arrays.toString(ack));
	}
}
