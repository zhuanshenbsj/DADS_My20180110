package com.cloud.mina.filter.decode.mh.sports;

import org.apache.mina.core.buffer.IoBuffer;

import com.cloud.mina.entity.packageData.PackageData;
import com.cloud.mina.entity.packageData.sports.PkgMHSportNo8Three;
import com.cloud.mina.filter.decode.mh.PkgDecMHSportsComponent;
import com.cloud.mina.utils.Constants;
import com.cloud.mina.utils.DataTypeChangeHelper;
import com.cloud.mina.utils.DateUtil;
import com.cloud.mina.utils.DeviceIDResolver;
import com.cloud.mina.utils.Logger;

public class PkgDecMHSport8ThreeLeaf extends PkgDecMHSportsComponent {

	@Override
	public boolean checkHeadData(IoBuffer buffer) {
		if (buffer.get(8) == 8 && buffer.get(9) == 3) {
			return true;
		}
		return false;
	}

	@Override
	public PackageData getPackage(IoBuffer buffer) {
		log.info(this.getClass().getSimpleName() + ".generateRealPackageData() begin...");
		PkgMHSportNo8Three packet = new PkgMHSportNo8Three();
		byte kcal_b[] = new byte[4];
		byte step_b[] = new byte[4];
		//add by changyb for PWS0004 validate stride in 20130718
		byte effective_step_b[] = new byte[4];
		byte distance_b[] = new byte[4];
		byte level1_b[] = new byte[2];
		byte level2_b[] = new byte[2];
		byte level3_b[] = new byte[2];
		byte level4_b[] = new byte[2];
		int tran_type = buffer.get(10);
		//		String auto_tran = String.valueOf(tran_type);
		int year = buffer.get(11);
		int month = buffer.get(12);
		int day = buffer.get(13);
		//add begin by changyb for PWS0004 validate stride in 20130718
		effective_step_b[0] = buffer.get(14);
		effective_step_b[1] = buffer.get(15);
		effective_step_b[2] = buffer.get(16);
		effective_step_b[3] = buffer.get(17);
		//add end by changyb for PWS0004 validate stride in 20130718
		int battery = buffer.get(22);
		int weight = buffer.get(23);
		int stride = buffer.get(24);
		kcal_b[0] = buffer.get(25);
		kcal_b[1] = buffer.get(26);
		kcal_b[2] = buffer.get(27);
		kcal_b[3] = buffer.get(28);
		step_b[0] = buffer.get(29);
		step_b[1] = buffer.get(30);
		step_b[2] = buffer.get(31);
		step_b[3] = buffer.get(32);
		distance_b[0] = buffer.get(33);
		distance_b[1] = buffer.get(34);
		distance_b[2] = buffer.get(35);
		distance_b[3] = buffer.get(36);
		level1_b[1] = buffer.get(37);
		level1_b[0] = buffer.get(38);
		level2_b[1] = buffer.get(39);
		level2_b[0] = buffer.get(40);
		level3_b[1] = buffer.get(41);
		level3_b[0] = buffer.get(42);
		level4_b[1] = buffer.get(43);
		level4_b[0] = buffer.get(44);
		long kcal = DataTypeChangeHelper.unsigned4BytesToInt(kcal_b, 0);
		long step = DataTypeChangeHelper.unsigned4BytesToInt(step_b, 0);
		//add  by changyb for PWS0004 validate stride in 20130718
		long effective_step = DataTypeChangeHelper.unsigned4BytesToInt(effective_step_b, 0);
		long distance = DataTypeChangeHelper.unsigned4BytesToInt(distance_b, 0);
		int level1 = DataTypeChangeHelper.byte2int(level1_b) * 2;
		int level2 = DataTypeChangeHelper.byte2int(level2_b) * 2;
		int level3 = DataTypeChangeHelper.byte2int(level3_b) * 2;
		int level4 = DataTypeChangeHelper.byte2int(level4_b) * 2;
		//********** 2013/06/13 begin *************
		String firmware_version = DeviceIDResolver.getFirmwareVersion(buffer.array(), 18);
		String prefix = DeviceIDResolver.getDeviceIDPrefix(buffer.array(), 65);
		String deviceID = DeviceIDResolver.getDeviceIDFromBytes(buffer.array(), 70);
		String patientID = DeviceIDResolver.searchPatientidByDeviceid(deviceID);
		String company = DeviceIDResolver.searchCompanyByDeviceid(deviceID);
		packet.setDeviceID(deviceID);
		packet.setPatientID(patientID);
		packet.setCompany(company);
		packet.setFirmware_version(firmware_version);
		packet.setPrefix(prefix);
		//*********** 2013/06/13 end **************

		//alter by rencm on 20130911 format:yyyyMMddHHmmSS
		String stepdate = DateUtil.getStepdate(year, month, day);

		packet.setBattery(battery);
		packet.setStepdate(stepdate);
		packet.setWeight(weight);
		packet.setStride(stride);
		packet.setKcal(kcal);
		packet.setStep(step);
		packet.setDistance(distance);
		packet.setEffective_step(effective_step);
		packet.setLevel1(level1);
		packet.setLevel2(level2);
		packet.setLevel3(level3);
		packet.setLevel4(level4);
		packet.setTran_type(tran_type);

		/****************guoyh 20130627 获取基站信息*************/
		//  add by guoyh for PWS0006  20130731 
		//  over add by guoyh for PWS0006  20130731 ;add basestation info to packagedata table 
		String locationInfoStr = getGPSLocaiton(buffer.array());

		packet.setLocationInfoStr(locationInfoStr);

		log.info(this.getClass().getSimpleName() + ".generateRealPackageData() end.");
		return packet;
	}

	private String getGPSLocaiton(byte[] b) {
		String locationInfoStr = "";
		String locationFlag = Constants.LocationFlag;
		if (locationFlag != null && locationFlag.equalsIgnoreCase("on")) {
			Logger.writeLog("--开始读取基站信息");

			//b[45]到b[54]保留字段；b[55]b[56]是基站强度；b[57]b[58]是MCC；b[59]b[60]是MNC；b[61]b[62]是LAC;b[63]b[64]是cellid
			byte rxlevel[] = new byte[2];
			byte mcc[] = new byte[2];//
			byte mnc[] = new byte[2];
			byte lac[] = new byte[2];
			byte cellid[] = new byte[2];
			rxlevel[1] = b[55];
			rxlevel[0] = b[56];
			mcc[1] = b[57];
			mcc[0] = b[58];
			mnc[1] = b[59];
			mnc[0] = b[60];
			lac[1] = b[61];
			lac[0] = b[62];
			cellid[1] = b[63];
			cellid[0] = b[64];
			int rxlevel_int = DataTypeChangeHelper.byte2int(rxlevel);
			int mcc_int = DataTypeChangeHelper.byte2int(mcc);
			int mnc_int = DataTypeChangeHelper.byte2int(mnc);
			int lac_int = DataTypeChangeHelper.byte2int(lac);
			int cellid_int = DataTypeChangeHelper.byte2int(cellid);
			locationInfoStr = rxlevel_int + "#" + mcc_int + "#" + mnc_int + "#" + lac_int + "#" + cellid_int;
			Logger.writeLog("get basestation info over the data is " + locationInfoStr);
		} else {
			Logger.writeLog("basestation info flag is off ");
		}
		return locationInfoStr;
	}

}
