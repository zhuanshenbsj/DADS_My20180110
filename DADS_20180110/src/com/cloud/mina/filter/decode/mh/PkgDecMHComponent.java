package com.cloud.mina.filter.decode.mh;

import org.apache.mina.core.buffer.IoBuffer;

import com.cloud.mina.entity.packageData.PackageData;
import com.cloud.mina.filter.decode.PkgDecodeComposite;

public class PkgDecMHComponent extends PkgDecodeComposite {

	@Override
	public PackageData getPackage(IoBuffer buffer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkHeadData(IoBuffer buffer) {
		return false;
	}

}
