package com.cloud.mina.filter.decode;

import org.apache.mina.core.buffer.IoBuffer;

import com.cloud.mina.entity.packageData.PackageData;

/**
 * 解码器父类
 */
public interface IPkgDecodeComponent {
	//从Iobuffer解码出java对象
	public PackageData getPackage(IoBuffer buffer);

	//检测包头信息，每个子类必须实现自己的判断逻辑
	public boolean checkHeadData(IoBuffer buffer);

	public PackageData generatePackageData(IoBuffer buffer);
}
