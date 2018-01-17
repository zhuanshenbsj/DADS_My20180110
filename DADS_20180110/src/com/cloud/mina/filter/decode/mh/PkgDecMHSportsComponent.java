package com.cloud.mina.filter.decode.mh;

import org.apache.mina.core.buffer.IoBuffer;

import com.cloud.mina.entity.packageData.PackageData;

/**
 * 盟联运动解码器
 * @author user
 *
 */
public class PkgDecMHSportsComponent extends PkgDecMHComponent {

	@Override
	public PackageData getPackage(IoBuffer buffer) {
		// TODO Auto-generated method stub
		return super.getPackage(buffer);
	}

	@Override
	public boolean checkHeadData(IoBuffer buffer) {
		log.info("byte[0]=" + buffer.get(0) + " byte[1]=" + buffer.get(1)
				+ " byte[2]=" + buffer.get(2) + " byte[3]=" + buffer.get(3));
		log.info("byte[4]=" + buffer.get(4) + " byte[5]=" + buffer.get(5)
				+ " byte[6]=" + buffer.get(6) + " byte[7]=" + buffer.get(7));
		log.info("byte[8]=" + buffer.get(8) + " byte[9]=" + buffer.get(9));
		if ((buffer.get(0) == -89) && (buffer.get(1) == -72)
				&& (buffer.get(2) == 0) && (buffer.get(3) == 1)) {
			log.info("buffer.length=" + buffer.array().length);
			log.info(this.getClass().getSimpleName() + ".check() return true");
			return true;
		}
		log.info(this.getClass().getSimpleName() + ".check() return false");
		return false;
	}

}
