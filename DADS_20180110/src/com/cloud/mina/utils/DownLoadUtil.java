package com.cloud.mina.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;
/***
 * 下载文件工具类
 */

public class DownLoadUtil {
	private static final String CONTENT_TYPE = "application/x-msdownload";
	/**
	 * 下载文件
	 * @param response HttpServletResponse
	 * @param title 下载时候的文件名称
	 * @param file 要下载的文件对象
	 * @return true 下载成功    false 下载失败
	 */
	public static boolean downLoadFile(HttpServletResponse response,String filename,File file){
		boolean result = true;
		FileInputStream input = null;
		OutputStream out = null;
		try {
			//设置response的编码方式
			response.setContentType(CONTENT_TYPE);
			//写明要下载的文件的大小
			response.setContentLength((int)file.length());
			//设置附加文件名
			response.setHeader("Content-Disposition","attachment;filename=\""+new String
					(filename.getBytes("UTF-8"),"iso-8859-1")+"\"");
			//读出文件到i/o流
			input =new FileInputStream(file);
			//从response对象中得到输出流,准备下载
			out = response.getOutputStream();
			if(input!=null && out!=null){	// 判断输入或输出是否准备好
				int temp = 0 ;	
				try{
					while((temp=input.read())!=-1){	// 开始拷贝
						out.write(temp) ;	// 边读边写
					}
				}catch(IOException e){
					e.printStackTrace() ;
					result = false;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			result = false;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
		} finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
					result = false; 
				}
			}
			if(out!=null){
				try {
					out.flush();
					out.close();//关闭输出流
				} catch (IOException e) {
					e.printStackTrace();
					result = false; 
				}
			}
			
		}
		return result;
	}
}
