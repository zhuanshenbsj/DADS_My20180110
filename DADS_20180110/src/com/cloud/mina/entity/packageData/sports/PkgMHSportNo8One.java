package com.cloud.mina.entity.packageData.sports;

import com.cloud.mina.entity.packageData.PackageData;

public class PkgMHSportNo8One extends PackageData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int battery = 0;
	private int weight;
	private int stride;
	private long kcal;
	private long step;
	private long distance = 0;
	private int level1;
	private int level2;
	private int level3;
	private int level4;
	private int tran_type;
	private long effective_step;
	private String stepdate = ""; //数据真实时间
	private String firmware_version = ""; //固件版本
	private String prefix = ""; //设备前缀

	public PkgMHSportNo8One() {
		this.name = "sports";
		this.type = "No8-1";
	}

	public int getBattery() {
		return battery;
	}

	public void setBattery(int battery) {
		this.battery = battery;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getStride() {
		return stride;
	}

	public void setStride(int stride) {
		this.stride = stride;
	}

	public long getKcal() {
		return kcal;
	}

	public void setKcal(long kcal) {
		this.kcal = kcal;
	}

	public long getStep() {
		return step;
	}

	public void setStep(long step) {
		this.step = step;
	}

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

	public int getLevel1() {
		return level1;
	}

	public void setLevel1(int level1) {
		this.level1 = level1;
	}

	public int getLevel2() {
		return level2;
	}

	public void setLevel2(int level2) {
		this.level2 = level2;
	}

	public int getLevel3() {
		return level3;
	}

	public void setLevel3(int level3) {
		this.level3 = level3;
	}

	public int getLevel4() {
		return level4;
	}

	public void setLevel4(int level4) {
		this.level4 = level4;
	}

	public int getTran_type() {
		return tran_type;
	}

	public void setTran_type(int tran_type) {
		this.tran_type = tran_type;
	}

	public long getEffective_step() {
		return effective_step;
	}

	public void setEffective_step(long effective_step) {
		this.effective_step = effective_step;
	}

	public String getStepdate() {
		return stepdate;
	}

	public void setStepdate(String stepdate) {
		this.stepdate = stepdate;
	}

	public String getFirmware_version() {
		return firmware_version;
	}

	public void setFirmware_version(String firmware_version) {
		this.firmware_version = firmware_version;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
