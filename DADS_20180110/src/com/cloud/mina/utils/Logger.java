package com.cloud.mina.utils;


public class Logger {
	static  org.apache.log4j.Logger logger = null;
	static{
		logger = org.apache.log4j.Logger.getLogger(Logger.class); 
	}
	/**
	 * write common log
	 * @param log
	 */
	public static void writeLog(String log){
		logger.info(log);
	}
	/**
	 * write error log
	 * @param log
	 */
	public static void errorLog(String log){
		logger.error(log);
	}
}
