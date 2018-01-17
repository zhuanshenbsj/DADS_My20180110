package com.cloud.mina.filter.decode.mh.sports;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.mina.core.buffer.IoBuffer;

import com.cloud.mina.entity.packageData.PackageData;
import com.cloud.mina.entity.packageData.sports.PkgMHSportNo8Two;
import com.cloud.mina.filter.decode.mh.PkgDecMHSportsComponent;
import com.cloud.mina.utils.DataTypeChangeHelper;
import com.cloud.mina.utils.DateUtil;
import com.cloud.mina.utils.DeviceIDResolver;
import com.cloud.mina.utils.EtcommDataTransferUtil;
import com.cloud.mina.utils.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PkgDecMHSport8TwoLeaf extends PkgDecMHSportsComponent {

	@Override
	public PackageData getPackage(IoBuffer buffer) {
		log.info(this.getClass().getSimpleName() + ".generateRealPackageData() begin...");
		log.info("buffer.length=" + buffer.array().length);
		String prefix = DeviceIDResolver.getNo8PackageDevicePrefix(buffer.array());
		PkgMHSportNo8Two packet = null;
		//		if(PropertiesReader.getProp("specificPrefix")!=null && PropertiesReader.getProp("specificPrefix").contains(prefix)){
		//			//益体康、易兴等1分钟数据包
		//			packet = handle1MinuteDetailPacket(buffer.array());
		//		}else{
		packet = handle5MinutesDetailPacket(buffer);
		//		}
		log.info(this.getClass().getSimpleName() + ".generateRealPackageData() end.");
		return packet;
	}

	@Override
	public boolean checkHeadData(IoBuffer buffer) {
		if (buffer.get(8) == 8 && buffer.get(9) == 2) {
			return true;
		}
		return false;
	}

	/**
	 * 盟联等5分钟详细包数据
	 * @param buffer
	 * @return
	 */
	private PkgMHSportNo8Two handle5MinutesDetailPacket(IoBuffer buffer) {
		PkgMHSportNo8Two packet = new PkgMHSportNo8Two();
		int number = 0;
		byte length[] = new byte[4];
		byte year[] = new byte[2];
		byte stepcount[] = new byte[2];
		byte stepkcal[] = new byte[2];
		byte data[] = new byte[2];
		int year_u[] = new int[24];
		int month_u[] = new int[24];
		int day_u[] = new int[24];
		int Hour[] = new int[24];
		int hourdata[][] = new int[24][72];
		int hourdata_real[][] = new int[24][72];
		length[0] = buffer.get(4);

		length[1] = buffer.get(5);
		length[2] = buffer.get(6);
		length[3] = buffer.get(7);
		long lengthvalue = DataTypeChangeHelper.unsigned4BytesToInt(length, 0);

		String deviceID = DeviceIDResolver.getDeviceIDFromBytes(buffer.array(), (int) (lengthvalue - 18));
		String patientID = DeviceIDResolver.searchPatientidByDeviceid(deviceID);
		String company = DeviceIDResolver.searchCompanyByDeviceid(deviceID);
		packet.setDeviceID(deviceID);
		packet.setPatientID(patientID);
		packet.setCompany(company);
		//old protocol unUSEDATA bytes is 12 ****** 2013/07/01 *******
		int unUsedataNum = 12;
		if ((lengthvalue - 12) % 114 == 0) {
			unUsedataNum = 12;
		} else {
			//new protocol unUSEDATA bytes is 33 ****** 2013/06/13 *******
			unUsedataNum = 33;
		}
		long max_times_tran = (lengthvalue - unUsedataNum) / 114;
		Logger.writeLog(
				"in method handleNo8DataForTwoWay detail package carry " + max_times_tran + " hours exercise data");
		for (int i = 0; i <= 24; i++) {
			if (number == (lengthvalue - unUsedataNum)) {
				break;
			}
			year[1] = buffer.get(10 + number);
			year[0] = buffer.get(11 + number);
			year_u[i] = DataTypeChangeHelper.byte2int(year);
			month_u[i] = buffer.get(13 + number);
			day_u[i] = buffer.get(14 + number);
			Hour[i] = buffer.get(15 + number);
			for (int j = 0; j < 12; j++) {
				stepcount[0] = buffer.get(16 + j * 2 + number);
				stepcount[1] = buffer.get(17 + j * 2 + number);
				hourdata[i][j] = DataTypeChangeHelper.byte2int(stepcount);
				hourdata_real[i][j] = DataTypeChangeHelper.byte2int(stepcount);
			}
			for (int j = 0; j < 12; j++) {
				stepkcal[0] = buffer.get(40 + j * 2 + number);
				stepkcal[1] = buffer.get(41 + j * 2 + number);
				hourdata[i][j + 12] = DataTypeChangeHelper.byte2int(stepkcal);
			}
			for (int j = 0; j < 12; j++) {
				if (buffer.get(64 + j + number) < 0) {
					hourdata[i][j + 24] = buffer.get(64 + j + number) + 256;
					hourdata[i][j + 24] = hourdata[i][j + 24] * 2;
				} else {
					hourdata[i][j + 24] = buffer.get(64 + j + number);
					hourdata[i][j + 24] = hourdata[i][j + 24] * 2;
				}
				if (buffer.get(76 + j + number) < 0) {
					hourdata[i][j + 36] = buffer.get(76 + j + number) + 256;
					hourdata[i][j + 36] = hourdata[i][j + 36] * 2;
				} else {
					hourdata[i][j + 36] = buffer.get(76 + j + number);
					hourdata[i][j + 36] = hourdata[i][j + 36] * 2;
				}
				if (buffer.get(88 + j + number) < 0) {
					hourdata[i][j + 48] = buffer.get(88 + j + number) + 256;
					hourdata[i][j + 48] = hourdata[i][j + 48] * 2;
				} else {
					hourdata[i][j + 48] = buffer.get(88 + j + number);
					hourdata[i][j + 48] = hourdata[i][j + 48] * 2;
				}
			}
			for (int j = 0; j < 12; j++) {
				data[0] = buffer.get(100 + j * 2 + number);
				data[1] = buffer.get(101 + j * 2 + number);
				hourdata[i][j + 60] = DataTypeChangeHelper.byte2int(data);
			}
			number = number + 114;
		}

		for (int times_tran = 0; times_tran < max_times_tran; times_tran++) {
			//要传的字段
			StringBuffer stepcount2data = new StringBuffer();
			//			stepcount2data.append("\"");// alter on 2014/06/30

			stepcount2data = stepcount2data.append("{\"data\":{\"datatype\":\"STEPCOUNT2\",");
			stepcount2data = stepcount2data.append(
					"\"hour\"" + ":\"" + String.valueOf(Hour[times_tran]) + "\"," + "\"datavalue\":[{\"snp5\":");
			stepcount2data.append("\"");
			for (int i = 0; i < 12; i++) {
				if (i == 11) {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i]));
				} else {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i])).append(",");
				}
			}
			stepcount2data.append("\"");

			stepcount2data.append("},{\"knp5\":");
			stepcount2data.append("\"");
			for (int i = 0; i < 12; i++) {
				if (i == 11) {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 12]));
				} else {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 12])).append(",");
				}
			}
			stepcount2data.append("\"");

			stepcount2data.append("},{\"level2p5\":");
			stepcount2data.append("\"");
			for (int i = 0; i < 12; i++) {
				if (i == 11) {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 24]));
				} else {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 24])).append(",");
				}
			}
			stepcount2data.append("\"");

			stepcount2data.append("},{\"level3p5\":");
			stepcount2data.append("\"");
			for (int i = 0; i < 12; i++) {
				if (i == 11) {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 36]));
				} else {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 36])).append(",");
				}
			}
			stepcount2data.append("\"");

			stepcount2data.append("},{\"level4p5\":");
			stepcount2data.append("\"");
			for (int i = 0; i < 12; i++) {
				if (i == 11) {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 48]));
				} else {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 48])).append(",");
				}
			}
			stepcount2data.append("\"");

			stepcount2data.append("},{\"yuanp5\":");
			stepcount2data.append("\"");
			for (int i = 0; i < 12; i++) {
				if (i == 11) {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 60]));
				} else {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 60])).append(",");
				}
			}
			stepcount2data.append("\"");

			stepcount2data.append("}]}}");

			//		    stepcount2data.append("\"");// alter on 2014/06/30

			//alter by rencm on 20130911 format:yyyy-MM-dd
			String stepdate = DateUtil.format(year_u[times_tran] + "-" + month_u[times_tran] + "-" + day_u[times_tran]);
			stepdate = stepdate.replaceAll("-", "");
			packet.getStepcount2data().add(stepcount2data.toString());
			packet.getStepdate().add(stepdate);
		}
		return packet;
	}

	/**
	 * 处理益体康8号包方式二
	 * @param patientID
	 * @param out
	 * @param b
	 * @param company
	 * @param deviceID
	 * @throws IOException
	 * @throws HttpException
	 * void
	 */
	public PkgMHSportNo8Two handle1MinuteDetailPacket(byte[] b) {
		PkgMHSportNo8Two packet = new PkgMHSportNo8Two();
		packet.setHasEffective(true);
		int number = 0;
		byte length[] = new byte[4];
		byte year[] = new byte[2];
		byte stepcount[] = new byte[2];
		byte stepkcal[] = new byte[2];
		byte data[] = new byte[2];
		int year_u[] = new int[24];
		int month_u[] = new int[24];
		int day_u[] = new int[24];
		int Hour[] = new int[24];
		int hourdata[][] = new int[24][360];
		length[0] = b[4];
		length[1] = b[5];
		length[2] = b[6];
		length[3] = b[7];
		// the bytes number of current package
		long lengthvalue = DataTypeChangeHelper.unsigned4BytesToInt(length, 0);

		//the bytes num front deviceID
		String deviceID = DeviceIDResolver.getDeviceIDFromBytes(b, (int) (lengthvalue - 18));
		String patientID = DeviceIDResolver.searchPatientidByDeviceid(deviceID);
		String company = DeviceIDResolver.searchCompanyByDeviceid(deviceID);
		Logger.writeLog("in method handleNo8DataForTwoWay device_id:" + deviceID + " patientID=" + patientID
				+ " company" + company);

		// unUSEDATA bytes is 33 
		int unUsedataNum = 33;
		// per USEDATA bytes is 546 
		int perUsedataNum = 546;
		long max_times_tran = (lengthvalue - unUsedataNum) / perUsedataNum;
		Logger.writeLog(
				"in method handleNo8DataForTwoWay detail package carry " + max_times_tran + " hours exercise data");
		for (int i = 0; i <= 24; i++) {
			if (number == (lengthvalue - unUsedataNum)) {
				break;
			}
			year[1] = b[10 + number];
			year[0] = b[11 + number];
			year_u[i] = DataTypeChangeHelper.byte2int(year);
			//b[12+number] is reversed
			month_u[i] = b[13 + number];
			day_u[i] = b[14 + number];
			Hour[i] = b[15 + number];
			// per minute steps
			for (int j = 0; j < 60; j++) {
				stepcount[0] = b[16 + j * 2 + number];
				stepcount[1] = b[17 + j * 2 + number];
				hourdata[i][j] = DataTypeChangeHelper.byte2int(stepcount);
			}
			// per minute kcal
			for (int j = 0; j < 60; j++) {
				stepkcal[0] = b[136 + j * 2 + number];
				stepkcal[1] = b[137 + j * 2 + number];
				hourdata[i][j + 60] = DataTypeChangeHelper.byte2int(stepkcal);
			}
			int level2Begin = 256;
			int level3Begin = 316;
			int level4Begin = 376;
			// per minute level
			for (int j = 0; j < 60; j++) {
				if (b[level2Begin + j + number] < 0) {
					hourdata[i][j + 120] = b[level2Begin + j + number] + 256;
					hourdata[i][j + 120] = hourdata[i][j + 120] * 2;
				} else {
					hourdata[i][j + 120] = b[level2Begin + j + number];
					hourdata[i][j + 120] = hourdata[i][j + 120] * 2;
				}
				if (b[level3Begin + j + number] < 0) {
					hourdata[i][j + 180] = b[level3Begin + j + number] + 256;
					hourdata[i][j + 180] = hourdata[i][j + 180] * 2;
				} else {
					hourdata[i][j + 180] = b[level3Begin + j + number];
					hourdata[i][j + 180] = hourdata[i][j + 180] * 2;
				}
				if (b[level4Begin + j + number] < 0) {
					hourdata[i][j + 240] = b[level4Begin + j + number] + 256;
					hourdata[i][j + 240] = hourdata[i][j + 240] * 2;
				} else {
					hourdata[i][j + 240] = b[level4Begin + j + number];
					hourdata[i][j + 240] = hourdata[i][j + 240] * 2;
				}
			}
			// 每1分钟内的3轴源数据的平方和
			for (int j = 0; j < 60; j++) {
				data[0] = b[436 + j * 2 + number];
				data[1] = b[437 + j * 2 + number];
				hourdata[i][j + 300] = DataTypeChangeHelper.byte2int(data);
			}
			number = number + perUsedataNum;
		}
		//******************** 组装数据  ***********************
		for (int times_tran = 0; times_tran < max_times_tran; times_tran++) {
			//要传的字段
			StringBuffer stepcount2data = new StringBuffer();
			//			stepcount2data.append("\"");

			stepcount2data = stepcount2data.append("{\"data\":{\"datatype\":\"STEPCOUNT2\",");
			stepcount2data = stepcount2data.append(
					"\"hour\"" + ":\"" + String.valueOf(Hour[times_tran]) + "\"," + "\"datavalue\":[{\"snp5\":");
			stepcount2data.append("\"");
			for (int i = 0; i < 60; i++) {
				if (i == 59) {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i]));
				} else {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i])).append(",");
				}
			}
			stepcount2data.append("\"");

			stepcount2data.append("},{\"knp5\":");
			stepcount2data.append("\"");
			for (int i = 0; i < 60; i++) {
				if (i == 59) {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 60]));
				} else {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 60])).append(",");
				}
			}
			stepcount2data.append("\"");

			stepcount2data.append("},{\"level2p5\":");
			stepcount2data.append("\"");
			for (int i = 0; i < 60; i++) {
				if (i == 59) {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 120]));
				} else {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 120])).append(",");
				}
			}
			stepcount2data.append("\"");

			stepcount2data.append("},{\"level3p5\":");
			stepcount2data.append("\"");
			for (int i = 0; i < 60; i++) {
				if (i == 59) {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 180]));
				} else {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 180])).append(",");
				}
			}
			stepcount2data.append("\"");

			stepcount2data.append("},{\"level4p5\":");
			stepcount2data.append("\"");
			for (int i = 0; i < 60; i++) {
				if (i == 59) {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 240]));
				} else {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 240])).append(",");
				}
			}
			stepcount2data.append("\"");

			stepcount2data.append("},{\"yuanp5\":");
			stepcount2data.append("\"");
			for (int i = 0; i < 60; i++) {
				if (i == 59) {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 300]));
				} else {
					stepcount2data.append(String.valueOf(hourdata[times_tran][i + 300])).append(",");
				}
			}
			stepcount2data.append("\"");

			stepcount2data.append("}]}}");

			//		    stepcount2data.append("\"");

			//alter by rencm on 20130911 format:yyyy-MM-dd
			String stepdate = DateUtil.format(year_u[times_tran] + "-" + month_u[times_tran] + "-" + day_u[times_tran]);
			stepdate = stepdate.replaceAll("-", "");
			//转换为五分钟格式
			transferToFiveNo8TwoWay(stepcount2data.toString(), stepdate, packet);
		}
		return packet;
	}

	/**
	 * 将一分钟数据转换为5分钟数据,并分析出有效步数
	 * @param patientID
	 * @param company
	 * @param deviceID
	 * @param stepcount2data
	 * @param stepdate
	 * void
	 */
	private PkgMHSportNo8Two transferToFiveNo8TwoWay(String detail_data, String stepdate, PkgMHSportNo8Two packet) {
		JSONObject steps = JSONObject.fromObject(detail_data);
		//详细包转为5分钟数据
		steps = EtcommDataTransferUtil.oneToFive(steps);
		packet.getStepcount2data().add(steps.toString());
		packet.getStepdate().add(stepdate);

		//解析出有效步数
		JSONObject effeSteps = JSONObject.fromObject(detail_data);
		effeSteps.getJSONObject("data").put("datatype", "STEPCOUNT3");
		JSONArray effeArr = effeSteps.getJSONObject("data").getJSONArray("datavalue");
		effeArr.set(0, EtcommDataTransferUtil.toEffective("snp5", effeArr.getJSONObject(0)));
		//有效步数转为5分钟数据
		effeSteps = EtcommDataTransferUtil.oneToFive(effeSteps);
		packet.getStepEffective().add(effeSteps.toString());
		return packet;
	}
}
