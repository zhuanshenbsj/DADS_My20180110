package com.cloud.mina.utils;

import java.util.Random;

public class MyRandomUtil {
	/**
	 * 获得指定范围整数
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getInt(int min,int max){
		Random random = new Random();
		int s = random.nextInt(max)%(max-min+1) + min;
		System.out.println(s);
		return s;
		
	}
	/**
	 * 获得指定范围1位小数
	 * @param min
	 * @param max
	 * @return
	 */
	public static float getFloat(int min,int max){
		Random random = new Random();
		float s = random.nextInt(max)%(max-min+1) + min;
		s+=(int)(Math.random()*10)/10.0;
		System.out.println(s);
		return s;
		
	}
	public static void main(String[] args) {
		getInt(10, 20);
		getFloat(10, 20);
	}
}
