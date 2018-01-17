package com.cloud.mina.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getMD5("SJWG@&@%2013"));
	}
	/**
	 * change the str to md5
	 * @param str  The string that needs change.
	 * @return String  If success will return 32 length String,else return empty string .
	 */
	public static String getMD5(String str){
		if(str ==null) return "";
		String temp = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest(); 
			StringBuffer buf = new StringBuffer(""); 
			for (int offset = 0,i = 0; offset < b.length; offset++) { 
				i = b[offset]; 
				if(i<0) 
					i+= 256; 
				if(i<16) 
					buf.append("0"); 
				buf.append(Integer.toHexString(i)); 
			} 
			temp = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return temp;
	}
}
