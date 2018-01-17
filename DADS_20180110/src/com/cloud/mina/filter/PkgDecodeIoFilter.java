package com.cloud.mina.filter;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

import com.cloud.mina.entity.packageData.PackageData;
import com.cloud.mina.filter.decode.IPkgDecodeComponent;

/**
 * 实现IoFilter
 * IoFilterAdapter IoFilter接口的缺省适配器
 * @author user
 *
 */
public class PkgDecodeIoFilter extends IoFilterAdapter {
	public IPkgDecodeComponent decode;

	public PkgDecodeIoFilter(IPkgDecodeComponent decode) {
		this.decode = decode;
	}

	/**
	 * 数据来了就进入了这个方法
	 * 在这里将数据流转成java对象的过程
	 * nextFilter是mina内部流程，可能还有logfilter之类
	 * nextFilter
	 */
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		PackageData pkgData = null;
		if (message instanceof IoBuffer) {
			//强转成Iobuffer
			IoBuffer buffer = (IoBuffer) message;
			//将数据解析成java对象
			pkgData = decode.generatePackageData(buffer);
		}
		if (pkgData == null) {
			nextFilter.messageReceived(session, message);
			//如果所有解码器都没解析出来，执行这块会自动跳转到Iohandler
		} else {
			//解析出来，也调用下一个iofilter 传递到Iohandler
			nextFilter.messageReceived(session, pkgData);
		}
	}
}
