package com.cloud.mina.entity.packageState.sports;

import java.util.Calendar;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.cloud.mina.entity.packageData.sports.PkgMHSportLogin;
import com.cloud.mina.utils.DataTypeChangeHelper;
import com.cloud.mina.utils.Logger;
import com.cloud.mina.utils.MLinkCRC;
import com.cloud.mina.utils.ParamInfoSendUtil;

public class StateMHSportLogin implements StateMHSport {

	@Override
	public boolean handlePackageObject(IoSession session, Object message) {
		PkgMHSportLogin packet = null;
		if (message != null && message instanceof PkgMHSportLogin) {
			packet = (PkgMHSportLogin) message;
			/**
			 * 这样处理将会使未注册的设备上传不成功
			 */
			if (packet.getPatientID() == null || "".equals(packet.getPatientID().trim())) {
				//回复NAK
				//responseToClient(session);
				return false;
			}
			session.setAttribute("patientId", packet.getPatientID());
			session.setAttribute("deviceId", packet.getDeviceID());
			session.setAttribute("company", packet.getCompany());
			session.setAttribute("loginTime", packet.getLoginTime());
			session.setAttribute("appType", packet.getAppType());
			//回复ACK
			responseToClient(session);
			return true;
		} else {
			//回复NAK
			//responseToClient(session);
			return false;
		}

	}

	private void responseToClient(IoSession session) {
		byte[] ack = new byte[19];
		byte[] crc_c = new byte[4];
		ack[0] = -89;
		ack[1] = -72;
		ack[2] = 0;
		ack[3] = 1;
		ack[4] = 0;
		ack[5] = 0;
		ack[6] = 0;
		ack[7] = 19;
		ack[8] = 1;
		/***************************begin:guoyh 20130701 check need send config to stepcounter (1:loginOk   4:loginOk and needSend)***********/
		//  add by guoyh for PWS0006  20130731 
		if (ParamInfoSendUtil.checkNeedAskParamInfo((String) session.getAttribute("patientId"))) {
			ack[9] = 4;
			Logger.writeLog("patientID" + session.getAttribute("patientId") + " has param data to send to stepcounter");
		} else {
			ack[9] = 1;
			Logger.writeLog(
					"patientID" + session.getAttribute("patientId") + " has no param data to send to stepcounter");
		}
		/***************************end：guoyh 20130701 check need send config to stepcounter***********/
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		byte[] year_b = DataTypeChangeHelper.int2byte(year);
		byte[] month_b = DataTypeChangeHelper.int2byte(month);
		byte[] day_b = DataTypeChangeHelper.int2byte(day);
		byte[] hour_b = DataTypeChangeHelper.int2byte(hour);
		byte[] minute_b = DataTypeChangeHelper.int2byte(minute);
		byte[] second_b = DataTypeChangeHelper.int2byte(second);
		ack[10] = year_b[1];
		ack[11] = year_b[0];
		ack[12] = month_b[0];
		ack[13] = day_b[0];
		ack[14] = hour_b[0];
		ack[15] = minute_b[0];
		ack[16] = second_b[0];

		crc_c = MLinkCRC.crc16(ack);
		ack[17] = crc_c[0];
		ack[18] = crc_c[1];

		session.write(IoBuffer.wrap(ack));
	}
}
