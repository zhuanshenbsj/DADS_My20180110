package com.cloud.mina.entity.packageData;

import java.io.Serializable;

/*
 * 数据包父类，存放 子公司的公共属性
 */
public class PackageData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String name = "";//表示传输数据类型
	protected String type = "";//表示数据包类型
	protected String deviceID = "";//设备号
	protected String patientID = "";//用户唯一标识
	protected String company = "";//单位代码
	protected String password = "";//密码
	protected String appType = "";//用户所属应用类型

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getPatientID() {
		return patientID;
	}

	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

}
