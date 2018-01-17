package com.cloud.mina.filter.decode.mh.sports;

import org.apache.mina.core.buffer.IoBuffer;

import com.cloud.mina.entity.packageData.PackageData;
import com.cloud.mina.entity.packageData.sports.PkgMHSportLogin;
import com.cloud.mina.filter.decode.mh.PkgDecMHSportsComponent;
import com.cloud.mina.utils.DateUtil;
import com.cloud.mina.utils.DeviceIDResolver;

public class PkgDecMHSportLoginLeaf extends PkgDecMHSportsComponent {

	@Override
	public boolean checkHeadData(IoBuffer buffer) {
		if (buffer.get(8) == 1 && buffer.get(9) == -128) {
			return true;
		}
		return false;
	}

	@Override
	public PackageData getPackage(IoBuffer buffer) {
		//		log.info(this.getClass().getSimpleName()
		//				+ ".generateRealPackageData() begin...");
		PkgMHSportLogin data = new PkgMHSportLogin();
		data.setDeviceID(DeviceIDResolver.getDeviceIDFromBytes(buffer.array(), 10));
		data.setPatientID(DeviceIDResolver.searchPatientidByDeviceid(data.getDeviceID()));
		data.setAppType(DeviceIDResolver.searchAppTypeByDeviced(data.getDeviceID()));
		data.setLoginTime(DateUtil.getCurrentTime());
		data.setName("sports");
		data.setType("login");
		//		if (data.getDeviceID() != null && data.getDeviceID().length() > 4) {
		//			data.setCompany(DeviceIDResolver.searchCompanyByDeviceid(data
		//					.getDeviceID()));
		//		}
		//		Logger.writeLog("NO.1 package handled device_id:" + data.getDeviceID()
		//				+ " patientID:" + data.getPatientID() + " company:"
		//				+ data.getCompany());
		// checkin success login user, add by renchm on 2013/08/27  PWS0012 
		//checkExceptionUserIn(data.getLoginTime(),data.getDeviceID(),data.getPatientID());
		//		log.info(this.getClass().getSimpleName()
		//				+ ".generateRealPackageData() end.");
		return data;

	}

}
