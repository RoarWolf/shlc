package com.hedong.hedongwx.entity;

import java.util.Date;

public class AllPortStatus {

	/** 自增id*/
	private Integer id;
	/** 设备编号*/
	private String equipmentnum;
	/** 设备端口号*/
	private Integer port;
	/** 端口状态*/
	private Byte portStatus;
	/** 剩余充电时间*/
	private Short time;
	/** 当前充电功率*/
	private Short power;
	/** 当前充电电量*/ 
	private Short elec;
	/** 当前充电电量*/ 
	private Double surp;
	/** 当前端口电压*/ 
	private Double portV;
	/** 当前端口电流*/ 
	private Double portA;
	/** 数据更新时间*/ 
	private Date updateTime;
	/** 预计剩余时间*/ 
	private String predict;
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
	public Byte getPortStatus() {
		return portStatus;
	}
	public void setPortStatus(Byte portStatus) {
		this.portStatus = portStatus;
	}
	public Short getTime() {
		return time;
	}
	public void setTime(Short time) {
		this.time = time;
	}
	public Short getPower() {
		return power;
	}
	public void setPower(Short power) {
		this.power = power;
	}
	public Short getElec() {
		return elec;
	}
	public void setElec(Short elec) {
		this.elec = elec;
	}
	public Double getSurp() {
		return surp;
	}
	public void setSurp(Double surp) {
		this.surp = surp;
	}
	public Double getPortV() {
		return portV;
	}
	public void setPortV(Double portV) {
		this.portV = portV;
	}
	public Double getPortA() {
		return portA;
	}
	public void setPortA(Double portA) {
		this.portA = portA;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getPredict() {
		return predict;
	}
	public void setPredict(String predict) {
		this.predict = predict;
	}
	@Override
	public String toString() {
		return "AllPortStatus [id=" + id + ", equipmentnum=" + equipmentnum + ", port=" + port + ", portStatus="
				+ portStatus + ", time=" + time + ", power=" + power + ", elec=" + elec + ", surp=" + surp
				+ ", updateTime=" + updateTime + ", predict=" + predict + "]";
	}
	
	
}
