package com.cloud.mina.entity.packageState.sports;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.cloud.mina.entity.packageData.sports.PkgMHSportLogout;
import com.cloud.mina.utils.C3P0Util;
import com.cloud.mina.utils.Logger;
import com.cloud.mina.utils.MLinkCRC;

/** */
public class StateMHSportLogout implements StateMHSport {
	@Override
	public boolean handlePackageObject(IoSession session, Object message) {
		if (message != null && message instanceof PkgMHSportLogout) {
			Logger.writeLog("logout package be handled patientID:" + session.getAttribute("patientId") + " company:"
					+ session.getAttribute("company") + " device_id:" + session.getAttribute("deviceId"));
			handleLogoutData(session);
			// checkout success logout user, add by renchm on 2013/08/27  PWS0012
			checkExceptionUserOut(session.getAttribute("loginTime").toString(),
					session.getAttribute("deviceId").toString());
			session.close(true);
			return true;
		}
		return false;
	}

	private void handleLogoutData(IoSession session) {
		//ACK消息回去，告知成功收到logout消息
		byte[] ack = new byte[12];
		byte[] crc_c = new byte[2];
		ack[0] = -89;
		ack[1] = -72;
		ack[2] = 0;
		ack[3] = 1;
		ack[4] = 0;
		ack[5] = 0;
		ack[6] = 0;
		ack[7] = 12;
		ack[8] = 1;
		ack[9] = 3;

		crc_c = MLinkCRC.crc16(ack);
		ack[10] = crc_c[0];
		ack[11] = crc_c[1];
		session.write(IoBuffer.wrap(ack));
		Logger.writeLog(
				"in method handleLogoutData end the ack:" + "-89 -72 0 1 0 0 0 12 1 3 " + crc_c[0] + " " + crc_c[1]);
	}

	private void checkExceptionUserOut(String loginTime, String device_id) {
		try {
			String sql = "delete from exceptionuser where loginTime='" + loginTime + "' and deviceID='" + device_id
					+ "'";
			C3P0Util.executeUpdate(sql);
			Logger.writeLog("logout success! deviceID=" + device_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
