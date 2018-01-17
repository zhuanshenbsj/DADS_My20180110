package com.cloud.mina.utils;

import java.util.Map;


public class GetPushDownAckUtil {
	/**
	 * get user param ack ，such as weight hieght sex age。。
	 * @param userParamMap
	 * @return
	 */
//  add by guoyh for PWS0008 20130819 
	public static byte []  getUserParamAck(Map<String, String> userParamMap){
		Logger.writeLog("begin to get userParamAck....");
		/*返回结果共19字节：
		 * header 4字节  存放： -89 -72 0 1 
		 * length 4字节 存放      0  0  0  19
		 * type   2字节 存放      -98   1
		 * 数据（tag） 2字节  存放   -96  6 
		 * 数据（length） 1字节  存放   4
		 * 数据（value）4字节  存放      体重实际kg值强制转换为byte       身高cm实际值强制转换为byte        性别标示（1男0女）    年龄段（待定）
		 * crc 2字节
		 */
		byte [] ack = new byte[19];
		byte [] crc_c = new byte[2];                      
		//header 4bytes  content： -89 -72 0 1 
		ack[0] = -89;
		ack[1] = -72;
		ack[2] = 0;
		ack[3] = 1;
		//length 4bytes  content：      0  0  0  19
		ack[4] = 0;
		ack[5] = 0;
		ack[6] = 0;
		ack[7] = 19;
		//type   2bytes  content：      -98   1
		ack[8] = -98;
		ack[9] = 1;
		//data（tag） 2bytes  content：   -96  6 
		ack[10] = -96;
		ack[11] = 6;
		//data（length） 1bytes  content：   4
		ack[12] = 4;
		//data（value）4bytes  content：    
		
		if(userParamMap!=null){//weight+"#"+height+"#"+sex+"#"+agelevel;
			// 体重实际kg值强制转换为无符号的byte  
			ack[13] = ByteUtil.intToUnsignedByte(Integer.parseInt((String)userParamMap.get("weight")));
			//  身高cm实际值强制转换为无符号的byte  
			ack[14] = ByteUtil.intToUnsignedByte(Integer.parseInt((String)userParamMap.get("height")));
			ack[15] =  ByteUtil.intToUnsignedByte(Integer.parseInt((String)userParamMap.get("sex")));// 性别标示（1男0女）
			ack[16] =  ByteUtil.intToUnsignedByte(Integer.parseInt((String)userParamMap.get("age")));//年龄段（待定）
			crc_c = MLinkCRC.crc16(ack);
			ack[17] = crc_c[0];
			ack[18] = crc_c[1];						
			return ack;
		}else{
			return null;
		}
	}
	/**
	 * get dataip info to change stepcounter's linking gatewey ip
	 * @param userParamMap
	 * @return
	 */
//  add by guoyh for PWS0008 20130819
	public static byte []  getDataIpAndDomainAck(Map<String, String> userParamMap){
		Logger.writeLog("begin to get dataip ack....");
		if(userParamMap==null) return null;
		//验证ip的有效性
		String ip = (String)userParamMap.get("datafromip");
		Logger.writeLog("The new gateway ip is:"+ip);		
		String[] ipIntArr = (ip==null? null:ip.split("\\."));
		if(ipIntArr==null||ipIntArr.length!=4
		||Integer.parseInt(ipIntArr[0])>255||Integer.parseInt(ipIntArr[1])>255||Integer.parseInt(ipIntArr[2])>255||Integer.parseInt(ipIntArr[3])>255
		||Integer.parseInt(ipIntArr[0])<0||Integer.parseInt(ipIntArr[1])<0||Integer.parseInt(ipIntArr[2])<0||Integer.parseInt(ipIntArr[3])<0){
			//ip不合格，返回null
			return null;
		}
		//获取域名
		String domain = (String)userParamMap.get("datafromdomain");
		Logger.writeLog("The new gateway domian is:"+domain);		
		if(domain==null) domain=ip;
		
		//获取端口
		String portstr = (String)userParamMap.get("port");
		int portint = 8889;
		if(portstr!=null) portint = Integer.parseInt(portstr);
		Logger.writeLog("The new gateway port is:"+portint);
		
		//修改标记位needsend 为 0
		String sql  = "update userparaminfo_gateway set needsend='4',changetime=NOW() where phone='"+userParamMap.get("phone")+"'";
		C3P0Util.executeUpdate(sql);
		
		/*返回结果共103字节：
		 * header 4字节  存放： -89 -72 0 1 
		 * length 4字节 存放      0  0  0  31
		 * type   2字节 存放      -98   1
		 * ip数据（tag） 2字节  存放   0  6 
		 * 数据（length） 1字节  存放   16
		 * 数据（value）16字节  存放   ip的每个符号对应的ascii码，不足的补OX00
		 * 
		 *  port数据（tag） 2字节  存放   0  7 
		 * 数据（length） 1字节  存放   2
		 * 数据（value）2字节  存放   port的2字节数据
		 * 
		 *  domain数据（tag） 2字节  存放   0  5
		 * 数据（length） 1字节  存放   64
		 * 数据（value）64字节  存放   domain的每个符号对应的ascii码，不足的补OX00
		 * 
		 * crc 2字节
		 */
		byte [] ack = new byte[103];
		byte [] crc_c = new byte[2];                      
		//header 4bytes  content： -89 -72 0 1 
		ack[0] = -89;		ack[1] = -72;		ack[2] = 0;		ack[3] = 1;
		//length 4bytes  content：      0  0  0  19
		ack[4] = 0;		ack[5] = 0;		ack[6] = 0;		ack[7] = 103;
		//type   2bytes  content：      -98   1
		ack[8] = -98;		ack[9] = 1;
				
		
		
		//封装 ip  ； put byte[16] to ack
		//data（tag） 2bytes  content：   0  6 
		ack[10] = 0;		ack[11] = 6;
		//data（length） 1bytes  content：   16
		ack[12] = 16;
		for(int i =0;i<16;i++){
			ack[13+i]=0;
		}
		char[] ipCharArr = ip.toCharArray();
		for(int i=0;i<ipCharArr.length;i++){
			ack[13+i]= (byte) ipCharArr[i];
		}
		//封装端口
		ack[29] = 0;
		ack[30] = 7;
		ack[31] = 2;
		ack[32] = (byte)(portint >>> 8);
		ack[33] = (byte)(portint );
		
		
		//封装域名
		ack[34] = 0;
		ack[35] = 5;
		ack[36] = 64;
		for(int i =0;i<64;i++){
			ack[37+i]=0;
		}
		char[] domainCharArr = domain.toCharArray();
		for(int i=0;i<domainCharArr.length;i++){
			ack[37+i]= (byte) domainCharArr[i];
		}
		crc_c = MLinkCRC.crc16(ack);
		ack[101] = crc_c[0];
		ack[102] = crc_c[1];						
		return ack;
		
	}
	/**
	 * get auto-upload time ack
	 * @param userParamMap
	 * @return
	 */
	public static byte[] getAutoUploadTimeAck(Map<String, String> userParamMap) {
		Logger.writeLog("begin to get AutoUploadTime ack....");
		if(userParamMap==null) return null;
		//验证ip的有效性
		String autouploadtime = (String)userParamMap.get("autouploadtime");
		Logger.writeLog("The new autouploadtime is:"+autouploadtime);		
		String[] timeArr = (autouploadtime==null? null:autouploadtime.split("#"));
		int h1,m1,h2,m2,h3,m3;
		if(timeArr==null||timeArr.length!=3
				||!ValidateUtil.checkAutoUploadTime(timeArr[0])
				||!ValidateUtil.checkAutoUploadTime(timeArr[1])
				||!ValidateUtil.checkAutoUploadTime(timeArr[2])){
			//时间格式不合格，返回null
			return null;
		}else{
			String[] hm = timeArr[0].split(":");
			h1 = Integer.parseInt(hm[0]);
			m1 = Integer.parseInt(hm[1]);
			
			hm = timeArr[1].split(":");
			h2 = Integer.parseInt(hm[0]);
			m2 = Integer.parseInt(hm[1]);
			
			hm = timeArr[2].split(":");
			h3 = Integer.parseInt(hm[0]);
			m3 = Integer.parseInt(hm[1]);
		}

		/*返回结果共22字节：
		 * header 4字节  存放： -89 -72 0 1 
		 * length 4字节 存放      0  0  0  31
		 * type   2字节 存放      -98   1
		 * time数据（tag） 2字节  存放   -96 1
		 * 数据（length） 1字节  存放   7
		 * 数据（value）7字节  存放   三个时间点小时和分钟对应的ascii码，不足的补OX00
		 * 
		 * crc 2字节
		 */
		byte [] ack = new byte[22];
		byte [] crc_c = new byte[2];                      
		//header 4bytes  content： -89 -72 0 1 
		ack[0] = -89;		ack[1] = -72;		ack[2] = 0;		ack[3] = 1;
		//length 4bytes  content：      0  0  0  19
		ack[4] = 0;		ack[5] = 0;		ack[6] = 0;		ack[7] = 22;
		//type   2bytes  content：      -98   1
		ack[8] = -98;		ack[9] = 1;
				
		
		
		//封装 time  ； put byte[7] to ack
		//data（tag） 2bytes  content：   -96  2 
		ack[10] = -96;		ack[11] = 2;
		//data（length） 1bytes  content：   7
		ack[12] = 7;
		//Byte0 每天自动上传次数
		ack[13] = 3;
		//Byte1-Byte2:上传时间1（高字节为hour,低字节为minute）
		ack[14] =  (byte) h1;
		ack[15] =  (byte) m1;
		//Byte3-Byte4:上传时间2
		ack[16] =  (byte) h2;
		ack[17] =  (byte) m2;
		//Byte5-Byte6:上传时间3
		ack[18] =  (byte) h3;
		ack[19] =  (byte) m3;
		
		crc_c = MLinkCRC.crc16(ack);
		ack[20] = crc_c[0];
		ack[21] = crc_c[1];
		Logger.writeLog("end to get AutoUploadTime ack....");
		return ack;
	}
}
