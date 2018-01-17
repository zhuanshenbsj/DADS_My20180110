package com.cloud.mina.entity.packageData.sports;

import java.util.ArrayList;
import java.util.List;

import com.cloud.mina.entity.packageData.PackageData;

public class PkgMHSportNo8Two extends PackageData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> stepcount2data = new ArrayList<String>();
	private List<String> stepdate = new ArrayList<String>();
	/**
	 * 根据1分钟数据计算出的有效步数包
	 */
	private List<String> stepEffective = new ArrayList<String>();
	/**
	 * 标识是否有有效步数
	 */
	private boolean hasEffective;

	public PkgMHSportNo8Two() {
		this.name = "sports";
		this.type = "No8-2";
	}

	public List<String> getStepcount2data() {
		return stepcount2data;
	}

	public void setStepcount2data(List<String> stepcount2data) {
		this.stepcount2data = stepcount2data;
	}

	public List<String> getStepdate() {
		return stepdate;
	}

	public void setStepdate(List<String> stepdate) {
		this.stepdate = stepdate;
	}

	public List<String> getStepEffective() {
		return stepEffective;
	}

	public void setStepEffective(List<String> stepEffective) {
		this.stepEffective = stepEffective;
	}

	public boolean isHasEffective() {
		return hasEffective;
	}

	public void setHasEffective(boolean hasEffective) {
		this.hasEffective = hasEffective;
	}
}
