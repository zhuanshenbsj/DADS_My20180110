package com.cloud.mina.entity.packageState.sports;

import org.apache.mina.core.session.IoSession;

import com.cloud.mina.entity.packageData.sports.PkgMHSportNo8Three;
import com.cloud.mina.utils.PropertiesReader;
import com.cloud.mina.utils.SaveSportsNo8PacketUtil;

/** */
public class StateMHSportNo8Three implements StateMHSport {
	@Override
	public boolean handlePackageObject(IoSession session, Object message) {
		PkgMHSportNo8Three packet = null;
		if (message != null && message instanceof PkgMHSportNo8Three) {
			packet = (PkgMHSportNo8Three) message;
			if (packet.getPatientID() != null && !"".equals(packet.getPatientID())) {
				session.setAttribute("patientId", packet.getPatientID());
				session.setAttribute("deviceId", packet.getDeviceID());
				session.setAttribute("company", packet.getCompany());
			}
			boolean result = false;
			if ("true".equals(PropertiesReader.getProp("isNewSport"))) {
				result = SaveSportsNo8PacketUtil.saveNewSportSimple(session, packet);
			} else {
				result = saveOldSport(session, packet);
			}
			if (result) {
				//handle over,return ACK
				SaveSportsNo8PacketUtil.sendNo8Ack(session, result, 3);
			} else {
			}

			//通过观察者发送数据
			if ("true".equals(PropertiesReader.getProp("isNewSport"))) {
				//				SubjectFactory.getSubject((String)session.getAttribute("appType"), PropertiesReader.getProp("DATATYPE_STEPCOUNT")).notifyObserver();
			}
			return true;
		}
		return false;
	}

	/**
	 * 以旧协议格式存储运动数据
	 * @param session
	 * @param packet
	 * @return
	 */
	private boolean saveOldSport(IoSession session, PkgMHSportNo8Three packet) {
		StringBuffer datacontent = new StringBuffer(80);
		datacontent.append(session.getAttribute("patientId")).append(" PHR ").append(packet.getStepdate()).append(" ")
				.append(packet.getBattery()).append(" ").append(packet.getWeight()).append(" ")
				.append(packet.getStride()).append(" ").append(packet.getKcal()).append(" ").append(packet.getStep())
				.append(" ").append(packet.getDistance()).append(" ").append(packet.getLevel1()).append(" ")
				.append(packet.getLevel2()).append(" ").append(packet.getLevel3()).append(" ")
				.append(packet.getLevel4()).append(" ").append(packet.getTran_type()).append(" ")
				.append(packet.getEffective_step());

		boolean result = SaveSportsNo8PacketUtil.insertNo8DataForThreeWay((String) session.getAttribute("patientId"),
				(String) session.getAttribute("company"), (String) session.getAttribute("deviceId"),
				String.valueOf(packet.getStep()), datacontent.toString(), packet.getLocationInfoStr(),
				packet.getStepdate(), packet.getFirmware_version(), packet.getPrefix());
		return result;
	}

}
