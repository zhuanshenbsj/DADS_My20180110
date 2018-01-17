package com.cloud.mina.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


/**
 * 统计指定文件夹内、指定格式文件的内容行数
 */
public class CodeCount {
	private static String filePath ;//源文件路径
	private int num; //记录行数
	private String extendsName = "";//文件格式
	private BufferedWriter out;
	static{
		//获取项目根目录
		String path = CodeCount.class.getResource("").toString();
		filePath = path.substring(6, path.indexOf("/build"));
	}
	public CodeCount(String extendsName) {
		this.extendsName = extendsName;
		String newName = filePath.substring(filePath.lastIndexOf("/"));
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(
					"e:/"+newName+"_"+extendsName+"_code.txt"))));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		count(new File(filePath));

		try {
			out.write("//共 " + num + " 行");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.print(extendsName+" num = " + num);
	}

	// 读取文件内容并统计行数
	public void count(File file) {
		File[] files = file.listFiles();
		for (int filesIndex = 0; filesIndex < files.length; filesIndex++) {
			if (files[filesIndex].isDirectory()) {
				// 递归调用
				count(files[filesIndex]);
			} else if (files[filesIndex].getName().endsWith(extendsName)) {
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(
							new FileInputStream(files[filesIndex])));
					String str = in.readLine();
					while (str != null) {
//						if (str.trim().length() != 0)//跳过空行，若注释此行则不跳过空行
							num++;
						out.write(str + "\r\n");
						str = in.readLine();
					}
					in.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new CodeCount("java");
		System.out.println();
		new CodeCount("html");
		System.out.println();
		new CodeCount("js");
	}
}
