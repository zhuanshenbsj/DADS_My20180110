package com.cloud.mina.filter.decode.mh.sports;

import org.apache.mina.core.buffer.IoBuffer;

import com.cloud.mina.entity.packageData.PackageData;
import com.cloud.mina.entity.packageData.sports.PkgMHSportLogout;
import com.cloud.mina.filter.decode.mh.PkgDecMHSportsComponent;

public class PkgDecMHSportLogoutLeaf extends PkgDecMHSportsComponent {

	@Override
	public PackageData getPackage(IoBuffer buffer) {
		log.info(this.getClass().getSimpleName() + ".generateRealPackageData() begin...");
		PkgMHSportLogout packet = new PkgMHSportLogout();
		packet.setName("sports");
		packet.setType("logout");
		log.info(this.getClass().getSimpleName() + ".generateRealPackageData() end.");
		return packet;
	}

	@Override
	public boolean checkHeadData(IoBuffer buffer) {
		if (buffer.get(8) == 1 && buffer.get(9) == 3) {
			return true;
		}
		return false;
	}

}
