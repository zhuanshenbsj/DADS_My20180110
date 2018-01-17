package com.cloud.mina.filter.decode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

import com.cloud.mina.entity.packageData.PackageData;

import common.Logger;

/**
 * 组合模式实现解码(Component,Leaf,Composite)
 */
public abstract class PkgDecodeComposite implements IPkgDecodeComponent {

	//记录日志
	public static Logger log = Logger.getLogger(PkgDecodeComposite.class);

	//存放子解码器
	public List<IPkgDecodeComponent> decodePool = new ArrayList<>();

	//添加解码器
	public void addDecode(IPkgDecodeComponent decode) {
		decodePool.add(decode);
	}

	//删除解码器
	public void removeDecode(IPkgDecodeComponent decode) {
		decodePool.remove(decode);
	}

	/*
	 * 迭代解码
	 */
	@Override
	public PackageData generatePackageData(IoBuffer buffer) {
		PackageData pkgData = null;

		if (decodePool == null || decodePool.size() == 0) {//判断当前解码器是否为子解码器，==null排除空指针异常
			//如果为子解码器，则直接解析
			return this.getPackage(buffer);
		}
		Iterator iterator = decodePool.iterator();
		while (iterator.hasNext()) {
			IPkgDecodeComponent decode = (IPkgDecodeComponent) iterator.next();
			if (decode.checkHeadData(buffer)) {
				return decode.generatePackageData(buffer);
			}
		}
		return null;
	}
}
