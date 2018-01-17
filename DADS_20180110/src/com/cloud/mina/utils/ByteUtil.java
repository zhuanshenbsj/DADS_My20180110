package com.cloud.mina.utils;

/**
 * 
 * <ul>
 * <li>锟侥硷拷锟斤拷锟� com.born.util.ByteUtil.java</li>
 * <li>锟侥硷拷锟斤拷锟斤拷: byte转锟斤拷锟斤拷锟斤拷</li>
 * <li>锟斤拷权锟斤拷锟斤拷: 锟斤拷权锟斤拷锟斤拷(C)2001-2006</li>
 * <li>锟斤拷 司: bran</li>
 * <li>锟斤拷锟斤拷摘要:</li>
 * <li>锟斤拷锟斤拷说锟斤拷:</li>
 * <li>锟斤拷锟斤拷锟斤拷冢锟�011-7-18</li>
 * <li>锟睫改硷拷录0锟斤拷锟斤拷</li>
 * </ul>
 * 
 * @version 1.0
 * @author 锟斤拷锟斤拷锟斤拷
 */
public class ByteUtil {
	/**
	 * 转锟斤拷short为byte
	 * 
	 * @param b
	 * @param s
	 *            锟斤拷要转锟斤拷锟斤拷short
	 * @param index
	 */
	public static void putShort(byte b[], short s, int index) {
		b[index + 1] = (byte) (s >> 8);
		b[index + 0] = (byte) (s >> 0);
	}
	public static void putShortByLarge(byte b[], short s, int index)
	{
		b[index + 0] = (byte) (s >> 8);
		b[index + 1] = (byte) (s >> 0);
	}
	/**
	 * 通锟斤拷byte锟斤拷锟斤拷取锟斤拷short
	 * 
	 * @param b
	 * @param index
	 *            锟节硷拷位锟斤拷始取
	 * @return
	 */
	public static short getShort(byte[] b, int index) {
		return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
	}

	/**
	 * 转锟斤拷int为byte锟斤拷锟斤拷
	 * 
	 * @param bb
	 * @param x
	 * @param index
	 */
	public static void putInt(byte[] bb, int x, int index) {
		bb[index + 3] = (byte) (x >> 24);
		bb[index + 2] = (byte) (x >> 16);
		bb[index + 1] = (byte) (x >> 8);
		bb[index + 0] = (byte) (x >> 0);
	}

	/**
	 * 通锟斤拷byte锟斤拷锟斤拷取锟斤拷int
	 * 
	 * @param bb
	 * @param index
	 *            锟节硷拷位锟斤拷始
	 * @return
	 */
	public static int getInt(byte[] bb, int index) {
		return (int) ((((bb[index + 3] & 0xff) << 24)
				| ((bb[index + 2] & 0xff) << 16)
				| ((bb[index + 1] & 0xff) << 8) | ((bb[index + 0] & 0xff) << 0)));
	}

	/**
	 * 转锟斤拷long锟斤拷为byte锟斤拷锟斤拷
	 * 
	 * @param bb
	 * @param x
	 * @param index
	 */
	public static void putLong(byte[] bb, long x, int index) {
		bb[index + 7] = (byte) (x >> 56);
		bb[index + 6] = (byte) (x >> 48);
		bb[index + 5] = (byte) (x >> 40);
		bb[index + 4] = (byte) (x >> 32);
		bb[index + 3] = (byte) (x >> 24);
		bb[index + 2] = (byte) (x >> 16);
		bb[index + 1] = (byte) (x >> 8);
		bb[index + 0] = (byte) (x >> 0);
	}

	/**
	 * 通锟斤拷byte锟斤拷锟斤拷取锟斤拷long
	 * 
	 * @param bb
	 * @param index
	 * @return
	 */
	public static long getLong(byte[] bb, int index) {
		return ((((long) bb[index + 7] & 0xff) << 56)
				| (((long) bb[index + 6] & 0xff) << 48)
				| (((long) bb[index + 5] & 0xff) << 40)
				| (((long) bb[index + 4] & 0xff) << 32)
				| (((long) bb[index + 3] & 0xff) << 24)
				| (((long) bb[index + 2] & 0xff) << 16)
				| (((long) bb[index + 1] & 0xff) << 8) | (((long) bb[index + 0] & 0xff) << 0));
	}

	/**
	 * 锟街凤拷锟街斤拷转锟斤拷
	 * 
	 * @param ch
	 * @return
	 */
	public static void putChar(byte[] bb, char ch, int index) {
		int temp = (int) ch;
		// byte[] b = new byte[2];
		for (int i = 0; i < 2; i ++ ) {
			bb[index + i] = new Integer(temp & 0xff).byteValue(); // 锟斤拷锟斤拷锟轿伙拷锟斤拷锟斤拷锟斤拷锟斤拷位
			temp = temp >> 8; // 锟斤拷锟斤拷锟斤拷8位
		}
	}

	/**
	 * 锟街节碉拷锟街凤拷转锟斤拷
	 * 
	 * @param b
	 * @return
	 */
	public static char getChar(byte[] b, int index) {
		int s = 0;
		if (b[index + 1] > 0)
			s += b[index + 1];
		else
			s += 256 + b[index + 0];
		s *= 256;
		if (b[index + 0] > 0)
			s += b[index + 1];
		else
			s += 256 + b[index + 0];
		char ch = (char) s;
		return ch;
	}

	/**
	 * float转锟斤拷byte
	 * 
	 * @param bb
	 * @param x
	 * @param index
	 */
	public static void putFloat(byte[] bb, float x, int index) {
		// byte[] b = new byte[4];
		int l = Float.floatToIntBits(x);
		for (int i = 0; i < 4; i++) {
			bb[index + i] = new Integer(l).byteValue();
			l = l >> 8;
		}
	}

	/**
	 * 通锟斤拷byte锟斤拷锟斤拷取锟斤拷float
	 * 
	 * @param bb
	 * @param index
	 * @return
	 */
	public static float getFloat(byte[] b, int index) {
		int l;
		l = b[index + 0];
		l &= 0xff;
		l |= ((long) b[index + 1] << 8);
		l &= 0xffff;
		l |= ((long) b[index + 2] << 16);
		l &= 0xffffff;
		l |= ((long) b[index + 3] << 24);
		return Float.intBitsToFloat(l);
	}

	/**
	 * double转锟斤拷byte
	 * 
	 * @param bb
	 * @param x
	 * @param index
	 */
	public static void putDouble(byte[] bb, double x, int index) {
		// byte[] b = new byte[8];
		long l = Double.doubleToLongBits(x);
		for (int i = 0; i < 4; i++) {
			bb[index + i] = new Long(l).byteValue();
			l = l >> 8;
		}
	}

	/**
	 * 通锟斤拷byte锟斤拷锟斤拷取锟斤拷float
	 * 
	 * @param bb
	 * @param index
	 * @return
	 */
	public static double getDouble(byte[] b, int index) {
		long l;
		l = b[0];
		l &= 0xff;
		l |= ((long) b[1] << 8);
		l &= 0xffff;
		l |= ((long) b[2] << 16);
		l &= 0xffffff;
		l |= ((long) b[3] << 24);
		l &= 0xffffffffl;
		l |= ((long) b[4] << 32);
		l &= 0xffffffffffl;
		l |= ((long) b[5] << 40);
		l &= 0xffffffffffffl;
		l |= ((long) b[6] << 48);
		l &= 0xffffffffffffffl;
		l |= ((long) b[7] << 56);
		return Double.longBitsToDouble(l);
	}
	/**
	 * 
	 * @param intValue
	 * @return
	 */
//   add by guoyh for PWS0006  20130731 ; This is used to send param info to stepcounter(MainThread.java)
	public static byte intToUnsignedByte(int intValue) {
		byte resultByte = 0;
		int temp = intValue % 256;   
		if ( intValue < 0) {   
			resultByte =   (byte)(temp < -128 ? 256 + temp : temp);   
		}   
		else {   
			resultByte =   (byte)(temp > 127 ? temp - 256 : temp);   
		} 
		return resultByte;
	}
	
	/** 
	 * int值转成4字节的byte数组 
	 * @param num 
	 * @return 
	 */  
	public static byte[] int2byteArray(int num) {  
	    byte[] result = new byte[4];  
	    result[0] = (byte)(num >>> 24);//取最高8位放到0下标  
	    result[1] = (byte)(num >>> 16);//取次高8为放到1下标  
	    result[2] = (byte)(num >>> 8); //取次低8位放到2下标   
	    result[3] = (byte)(num );      //取最低8位放到3下标  
	    return result;  
	}  

	/** 
	 * 将4字节的byte数组转成int值 
	 * @param b 
	 * @return 
	 */  
	public static int byteArray2int(byte[] b){  
	    byte[] a = new byte[4];  
	    int i = a.length - 1,j = b.length - 1;  
	    for (; i >= 0 ; i--,j--) {//从b的尾部(即int值的低位)开始copy数据  
	        if(j >= 0)  
	            a[i] = b[j];  
	        else  
	            a[i] = 0;//如果b.length不足4,则将高位补0  
	    }  
	    int v0 = (a[0] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位  
	    int v1 = (a[1] & 0xff) << 16;  
	    int v2 = (a[2] & 0xff) << 8;  
	    int v3 = (a[3] & 0xff) ;  
	    return v0 + v1 + v2 + v3;  
	}
	/**
	 * 将int类型存入到字节数组 大端
	 * @param bb
	 * @param x
	 * @param index
	 * void
	 */
	public static void putIntByLarge(byte[] bb, int x, int index) {
		bb[index + 0] = (byte) (x >> 24);
		bb[index + 1] = (byte) (x >> 16);
		bb[index + 2] = (byte) (x >> 8);
		bb[index + 3] = (byte) (x >> 0);
	}
	/**
	 * 通过byte数组取到int 小端模式
	 * 
	 * @param bb  oxff 二进制 1111 1111
	 * @param index
	 *            第几位开始
	 * @return
	 */
	public static int getIntByLittlePattern(byte[] bb, int index) {
		return (int) ((((bb[index + 3] & 0xff) << 24)
				| ((bb[index + 2] & 0xff) << 16)
				| ((bb[index + 1] & 0xff) << 8) | ((bb[index + 0] & 0xff) << 0)));
	}
	/**
	 * 通过byte数组取到short 小端
	 * 
	 * @param b
	 * @param index
	 *            第几位开始取
	 * @return  	>>n想右移动n位        <<n 像左移动n位
	 */
	public static short getShortByLittlePattern(byte[] b, int index) {
		return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
	}
	/**
	 * 通过byte数组取到short 大端
	 * 
	 * @param b
	 * @param index
	 *            第几位开始取
	 * @return
	 */
	public static short getShortByLargePattern(byte[] b, int index) {
		return (short) (((b[index + 0] << 8) | b[index + 1] & 0xff));
	}
	public static void main(String[] args) {
		System.out.println(ByteUtil.intToUnsignedByte(189));
	}
	
	
	
}