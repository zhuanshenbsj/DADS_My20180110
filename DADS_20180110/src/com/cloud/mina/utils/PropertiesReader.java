package com.cloud.mina.utils;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

public class PropertiesReader {

	private static Properties prop = new Properties();
	
	static
	{
		try {
//			InputStream SystemInMQ = new ClassPathResource("com/Config/ActiveMQConf.properties").getInputStream();
			InputStream SystemIn = new ClassPathResource("com/Config/SysConf.properties").getInputStream();
//			prop.load(SystemInMQ);
			prop.load(SystemIn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getProp(String name)
	{
		if(prop!=null && prop.containsKey(name))
		{
			return prop.getProperty(name, "");
		}else{
			return "";
		}
		
	}
	
	public static void main(String[] args)
	{
		String list = PropertiesReader.getProp("sendWayList");
		System.out.println(list);
		String[] arr = list.split(",");
		for (String string : arr) {
			System.out.println(string);
		}
	}
}
