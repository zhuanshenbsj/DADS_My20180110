package com.cloud.mina.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.utils.URLEncodedUtils;

public class HttpClientExecute {
	/**
	 * 
	 * @param url 访问路径
	 * @param pairKey 参数Key
	 * @param jsono	参数value
	 * @return JSONObject
	 */
	public static JSONArray execute(String url,String pairKey,Object jsono){
		List<NameValuePair> post = new ArrayList<NameValuePair>();
		//post
		JSONArray jsonArr = null;
		PostMethod postM = new PostMethod(url);
		post.add(new NameValuePair(pairKey,jsono.toString()));	
		for (NameValuePair pair : post) {
	          postM.addParameter(pair);
		}
		HttpClient client = new HttpClient();
		if("true".equalsIgnoreCase(PropertiesReader.getProp("isUseProxy")))
		{
			client.getHostConfiguration().setProxy("proxy.hq.cmcc", 8080);  
			//使用抢先认证  
			client.getParams().setAuthenticationPreemptive(true);
		}
		BufferedReader br = null;
		Reader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			client.executeMethod(postM);
//			char buf[] = new char[20560];
			reader = new InputStreamReader(postM.getResponseBodyAsStream(),"utf8");
			br = new BufferedReader(reader);
			String line = "";
			while (null != (line = br.readLine())) {
				sb.append(line);
			}
//			reader.read(buf, 0, 20560);
			jsonArr =  JSONArray.fromObject(sb.toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			postM.releaseConnection();
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonArr;
	}
	/**
	 * 
	 * @param url 访问路径
	 * @param pairKey 参数Key
	 * @param jsono	参数value
	 * @return JSONObject
	 */
	public static String executeWS(String url,String pairKey,Object jsono){
		List<NameValuePair> post = new ArrayList<NameValuePair>();
		//post
		String responseString="";
		PostMethod postM = new PostMethod(url);
		post.add(new NameValuePair(pairKey,jsono.toString()));	
		for (NameValuePair pair : post) {
	          postM.addParameter(pair);
		}
		HttpClient client = new HttpClient();
		if("true".equalsIgnoreCase(PropertiesReader.getProp("isUseProxy")))
		{
			client.getHostConfiguration().setProxy("proxy.hq.cmcc", 8080);  
			//使用抢先认证  
			client.getParams().setAuthenticationPreemptive(true);
		}
		Reader reader = null;
		try {
			client.executeMethod(postM);
			reader = new InputStreamReader(postM.getResponseBodyAsStream(),"utf8");
			BufferedReader br=new BufferedReader(reader);
			String line="";
			while(null!=(line=br.readLine())){
				responseString=responseString+line;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			postM.releaseConnection();
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return responseString;
	}
	
	
	public static String commExecute(String url,String ...args){
		//get
		String responseString="";
		GetMethod postM = null;
		try {
			url = PropertiesReader.getProp("EMR_URL_DATA");
			postM = new GetMethod(URLEncoder.encode(url,"UTF-8"));
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		HttpMethodParams params = new HttpMethodParams();
		for(int i=0;i<args.length;i+=2)
			params.setParameter(args[i], args[i+1]);
		if(args.length>0)
			postM.setParams(params);
		HttpClient client = new HttpClient();
		if("true".equalsIgnoreCase(PropertiesReader.getProp("isUseProxy")))
		{
			client.getHostConfiguration().setProxy("proxy.hq.cmcc", 8080);  
			//使用抢先认证  
			client.getParams().setAuthenticationPreemptive(true);
		}
		Reader reader = null;
		BufferedReader br = null;
		try {
			//设置连接超时时间
			client.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
			//设置响应超时时间
			client.getHttpConnectionManager().getParams().setSoTimeout(5000);
			client.executeMethod(postM);
			reader = new InputStreamReader(postM.getResponseBodyAsStream(),"utf8");
			br=new BufferedReader(reader);
			String line="";
			while(null!=(line=br.readLine())){
				responseString=responseString+line;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			postM.releaseConnection();
			try {
				if(br!=null)
					br.close();
				if(reader!=null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseString;
	}
	/**
	 * 
	 * @param url 访问路径
	 * 
	 * @param jsono	参数value
	 * @return JSONObject
	 */
	
	public static String executeGet(String url){
		Log.info(url);
		HttpClient client = new HttpClient();
		GetMethod getM = null;
		String responseString = "";
		try {
			getM = new GetMethod(url);
			getM.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");
			client.executeMethod(getM);
			Reader reader = new InputStreamReader(getM.getResponseBodyAsStream(),"utf8");
			BufferedReader br = new BufferedReader(reader);
			StringBuffer buf = new StringBuffer();
			String line = "";
			while (null != (line = br.readLine())) {
				buf.append(line);
			}
			responseString = buf.toString();
			Log.info(responseString);;
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			getM.releaseConnection();
		}
		return responseString;
	}
}
