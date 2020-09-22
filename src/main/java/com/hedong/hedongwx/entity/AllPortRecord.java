package com.hedong.hedongwx.entity;

import java.util.Date;

public class AllPortRecord {

	private Integer id;
	/** 设备编号*/
	private String equipmentnum;
	/** 设备端口号*/
	private Integer port;
	/** 设备端口状态*/
	private Integer status;
	/** 设备端口剩余时间*/
	private Integer time;
	/** 设备端口功率*/
	private Integer power;
	/** 设备端口剩余电量*/
	private Integer elec;
	/** 设备端口余额*/
	private Integer surp;
	/** 设备端口记录时间*/
	private Date recordTime;
	
	private String startTime;
	
	private String endTime;
	
    private Integer numPerPage;

    private Integer startIndex;
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEquipmentnum() {
		return equipmentnum;
	}
	public void setEquipmentnum(String equipmentnum) {
		this.equipmentnum = equipmentnum;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public Integer getPower() {
		return power;
	}
	public void setPower(Integer power) {
		this.power = power;
	}
	public Integer getElec() {
		return elec;
	}
	public void setElec(Integer elec) {
		this.elec = elec;
	}
	public Integer getSurp() {
		return surp;
	}
	public void setSurp(Integer surp) {
		this.surp = surp;
	}
	public Date getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getNumPerPage() {
		return numPerPage;
	}
	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}
	public Integer getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	
	
}
