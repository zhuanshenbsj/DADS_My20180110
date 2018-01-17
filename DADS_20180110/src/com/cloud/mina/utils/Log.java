package com.cloud.mina.utils;

import org.apache.log4j.PropertyConfigurator;

public class Log {
	static  org.apache.log4j.Logger logger = null;
	static{
		PropertyConfigurator.configure("d:\\log\\log4j.properties");
		logger = org.apache.log4j.Logger.getLogger(Log.class); 
	}
	/**
	 * write debug log
	 * @param log
	 */
	public static void debug(String log){
		logger.debug(log);
	}
	/**
	 * write info log
	 * @param log
	 */
	public static void info(String log){
		logger.info(log);
	}
	/**
	 * write error log
	 * @param log
	 */
	public static void error(String log){
		logger.error(log);
	}
	public static void main(String[] args) {
//		while (true) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					String str = "this is a log";
//					Logger.writeLog(str);
//					Logger.errorLog(str);
//					System.out.println(str);
//				}
//			}).start();
//			try {
//				Thread.sleep(1000l);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}
}
