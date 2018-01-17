package com.cloud.mina.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.cloud.mina.entity.packageData.PackageData;

public class StrategyFactoryHandler extends IoHandlerAdapter {
	public static Logger log = Logger.getLogger(StrategyFactoryHandler.class);
	//定义变量区
	private IHandlerMHData chain = null;
	private PackageData pkgData = null;
	private static Map<String, Class> classMap = new HashMap<>();

	static {
		/**
		 * 不同厂家对应不同策略,milink的sport/BP
		 * 通过数据包的名字来匹配类名，如sports和bloodpressure
		 */
		classMap.put("sports", HandlerMHSportData.class);
		//classMap.put("bloodpressure", HandlerMHBloodpressureData.class);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		log.info(this.getClass().getSimpleName() + ".messageReceived() begin...");
		if (message != null && message instanceof PackageData) {
			//1.参数验证
			pkgData = (PackageData) message;
			//2.调用具体设备类型数据包处理类
			if (pkgData != null) {
				chain = (IHandlerMHData) classMap.get(pkgData.getName()).newInstance();
				chain.handle(session, message);
			} else {
				log.error("无效数据包！");
			}
		} else {
			log.error("数据格式错误！！！");
		}
		log.info(this.getClass().getSimpleName() + ".messageReceived() end.");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		log.error("StrategyFactoryHandler caught exception");
		cause.printStackTrace();
	}
}
