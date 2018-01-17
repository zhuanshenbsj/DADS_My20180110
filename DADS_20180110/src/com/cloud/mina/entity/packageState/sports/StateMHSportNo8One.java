package com.cloud.mina.entity.packageState.sports;

import org.apache.mina.core.session.IoSession;

import com.cloud.mina.dao.DaoMHSportsNo8;
import com.cloud.mina.entity.packageData.sports.PkgMHSportNo8One;

/** */
public class StateMHSportNo8One implements StateMHSport {

	@Override
	public boolean handlePackageObject(IoSession session, Object message) {

		//1.验证参数
		if (message != null && message instanceof PkgMHSportNo8One) {
			PkgMHSportNo8One pkgData = (PkgMHSportNo8One) message;

			session.setAttribute("patientID", pkgData.getPatientID());
			session.setAttribute("deviceID", pkgData.getDeviceID());
			session.setAttribute("company", pkgData.getCompany());

			//2.数据包入库
			boolean resault = saveOldSport(session, pkgData);
			//3.回应设备

		}

		return false;

	}

	/**
	 * 以旧协议格式存储运动数据-历史包
	 * @param session
	 * @param packet
	 * @return
	 */
	private boolean saveOldSport(IoSession session, PkgMHSportNo8One pkgData) {
		//调用util数据包入库
		return DaoMHSportsNo8.insertNo8DataForOneWay1(session, pkgData);
	}
}
