package com.cloud.mina.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class NetworkUtil {
	/**
	 * request the uri to post data
	 * @param uri 
	 * @param map
	 * @return
	 */
	public static String postData(String uri,Map map){
		String responseString="";
		if(uri==null||map==null) return responseString;
		PostMethod postM = new PostMethod(uri);
		for (Iterator ite = map.entrySet().iterator(); ite.hasNext();) {
			Map.Entry entry = (Map.Entry) ite.next();
			postM.addParameter(new NameValuePair(entry.getKey().toString(),entry.getValue().toString()));
		}
		HttpClient client = new HttpClient();
		Reader reader = null;
		BufferedReader br = null;
		try {
			client.executeMethod(postM);
			reader = new InputStreamReader(postM.getResponseBodyAsStream(),"utf8");
			br=new BufferedReader(reader);
			String line="";
			while(null!=(line=br.readLine())){
				responseString+=line;
			}
		} catch (HttpException e) {
			responseString="NetworkUtil error :"+e.getMessage();
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			responseString="NetworkUtil error :"+e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			responseString="NetworkUtil error :"+e.getMessage();
			e.printStackTrace();
		}finally{
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
	 * request the uri to post data
	 * @param uri 
	 * @param map
	 * @return
	 */
	public static int postDataCode(String uri,Map map){
		int re_code = 500;
		if(uri==null||map==null) return re_code;
		PostMethod postM = new PostMethod(uri);
		for (Iterator ite = map.entrySet().iterator(); ite.hasNext();) {
			Map.Entry entry = (Map.Entry) ite.next();
			postM.addParameter(new NameValuePair(entry.getKey().toString(),entry.getValue().toString()));
		}
		HttpClient client = new HttpClient();
		Reader reader = null;
		BufferedReader br = null;
		try {
			re_code = client.executeMethod(postM);
			reader = new InputStreamReader(postM.getResponseBodyAsStream(),"utf8");
			br=new BufferedReader(reader);
			if(re_code!=200){
				String line="";
				String sum ="";
				while(null!=(line=br.readLine())){
					sum+=line;
				}
				System.err.println("发送异常:"+sum);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(br!=null)
					br.close();
				if(reader!=null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return re_code;
	}

}
