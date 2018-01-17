package com.cloud.mina.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;


/**
 * 向设备下推配置信息、用户信息
 * add by guoyh for PWS0006  20130731 ;This is used to do things of sending param info to stepcounter
 */
public class ParamInfoSendUtil {

	/**
	 * get need update info
	 */	
	@SuppressWarnings("finally")
	public static boolean  checkNeedAskParamInfo(String phone){
		boolean needCheck = false;
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		 try{   
			 conn = C3P0Util.getConnection();			
			 st = conn.createStatement();     
			 String query = "SELECT needsend FROM userparaminfo_gateway WHERE phone='"+phone+"'";   
			 rs = st.executeQuery(query); 
			 if(rs!=null&&rs.next()){
				 needCheck = (rs.getString("needsend")!=null&&(rs.getString("needsend").equals("1")||rs.getString("needsend").equals("2")||rs.getString("needsend").equals("3"))) ? true:false; 
			 }
		 }catch(Exception e){			 
			 System.out.println(e.getMessage());
		 }finally{
			 C3P0Util.releaseResource(conn, st, rs);
			 return needCheck;
		 }
		
		
	}
	
	/**
	 *
	 */	
	@SuppressWarnings("finally")
	public static Map<String, String>  readParamInfo(String phone){
		HashMap<String,String> resultMap = new HashMap<String,String>();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try{   
			 conn = C3P0Util.getConnection();
			
			 st = conn.createStatement();     
			 String query = "SELECT * FROM userparaminfo_gateway WHERE phone='"+phone+"'";   
			 /*rs = st.executeQuery(query); 
			 if(rs!=null&&rs.next()){
				 String weight = rs.getString("weight")==null ? "": rs.getString("weight");
				 String height = rs.getString("height")==null ? "": rs.getString("height");
				 String sex = rs.getString("sex")==null ? "": rs.getString("sex");
				 String agelevel = rs.getString("age")==null ? "": rs.getString("age");
				 if(!weight.equals("")&&!height.equals("")&&!sex.equals("")&&!agelevel.equals("")){
					 result = weight+"#"+height+"#"+sex+"#"+agelevel;
				 }
			 }*/
			 
		 	rs=st.executeQuery(query);
        	ResultSetMetaData rsmd = rs.getMetaData();
        	if(rs.next())
        	{
        		for(int i=0;i<rsmd.getColumnCount();i++)
        		{
        			resultMap.put(rsmd.getColumnLabel(i+1), rs.getString(i+1));
        		}
        	}
			 
		}catch(Exception e){			 
			 System.out.println(e.getMessage());
		}finally{
			 C3P0Util.releaseResource(conn, st, rs);
			 return resultMap;
		}
		
	}
	
	/**
	 * 
	 */	
	public static void  closeParamInfoSendFlag(String phone){
		String sql = "UPDATE userparaminfo_gateway SET needsend='0' WHERE phone='"+phone+"' ";//配置下发成功，标识位改为0
		Logger.writeLog("======"+sql) ;
		C3P0Util.executeUpdate(sql);
	}
	
	/**
	 * 
	 */	
	public static void  updateParamInfoFromPlatform(){
		Logger.writeLog("======begin to get user params from platform....") ;
		//
		String responseString =  "";
		PostMethod postM = new PostMethod(Constants.PHR_UserParam);
	     HttpClient client = new HttpClient();
	      try {
	    	    client.executeMethod(postM);
				Reader reader = new InputStreamReader(postM.getResponseBodyAsStream(),"utf8");
				BufferedReader br = new BufferedReader(reader);
				String line = "";
				while (null != (line = br.readLine())) {
					responseString = responseString + line;
				} 
	      }catch(Exception e){
	    	  
	      }
		System.out.println(responseString);
		
		//
		String[] userArr = responseString.split("@@");
		
		for(int i=0;i<userArr.length;i++){
			String[] paraArr = userArr[i].split("#");
			if(paraArr.length==6){
				ParamInfoSendUtil.dosql(paraArr);
			}
		}
		Logger.writeLog("=====end to get user params from platform....") ;
		
	}
	
	
	@SuppressWarnings("finally")
	public static boolean  dosql(String[] paraArr){
		//13911580708#1970-01-01#50#170#40#1
		String phone,changetime,weight,height,sex,age;
		phone=paraArr[0];
		changetime=paraArr[1];
		weight=paraArr[2];
		
		height=paraArr[3];
		age=paraArr[4];
		sex=paraArr[5];
		
		Connection conn = null;
		Statement st = null;
		Statement st2 = null;
		Statement st3 = null;
		
		ResultSet rs = null;
		ResultSet rs2 = null;
		try{   
			 conn = C3P0Util.getConnection();
			
			 st = conn.createStatement();  
			 st2 = conn.createStatement(); 
			 st3 = conn.createStatement(); 
			
			 rs = st.executeQuery("SELECT count(*) as num FROM userparaminfo_gateway WHERE phone='"+phone+"'"); 
			 if(rs!=null&&rs.next()){
				if(rs.getString("num").equals("0")){//
					String sql = "insert into userparaminfo_gateway (phone,weight,age,height,sex,changetime,needsend) "
						+"values('"+phone+"','"+weight+"','"+age+"','"+height+"','"+sex+"','"+changetime+"','1')"; 
					st2.execute(sql);
					Logger.writeLog(sql) ;
				}else{//
					rs2 = st2.executeQuery("SELECT count(*) as num2 FROM userparaminfo_gateway WHERE phone='"+phone+"' and changetime='"+changetime+"'"); 
					if(rs2!=null&&rs2.next()){
						if(rs2.getString("num2").equals("0")){//
							String sql = "UPDATE userparaminfo_gateway SET weight='"+weight+"',height='"+height+"',age='"+age+"',sex='"+sex+"',changetime='"+changetime+"',needsend='1' WHERE phone='"+phone+"'";
							st3.execute(sql);
							Logger.writeLog(sql) ;
						}
					}
					
				}
				
			 }
		}catch(Exception e){			 
			 System.out.println(e.getMessage());
		}finally{
			 C3P0Util.releaseResource(null, st3,null);
			 C3P0Util.releaseResource(null, st2,rs2);
			 C3P0Util.releaseResource(conn, st, rs);
			 return false;
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParamInfoSendUtil.updateParamInfoFromPlatform();		
	}

}
