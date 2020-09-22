package com.hedong.hedongwx.entity;

import java.util.Date;

/**
 * 用户包月
 * @author wolf
 *
 */
public class PackageMonth {

	/** 表自增id*/
	private Integer id;
	/** 用户id*/
	private Integer uid;
	/** 总剩余次数*/
	private Integer surpnum;
	/** 今日剩余次数*/
	private Integer todaysurpnum;
	/** 每日指定总次数*/
	private Integer everydaynum;
	/** 每月总次数*/
	private Integer everymonthnum;
	/** 包月下发时间*/
	private Integer time;
	/** 包月下发电量*/
	private Double elec;
	/** 到期时间*/
	private Date endTime;
	/** 到期时间*/
	private Integer monthnum;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getSurpnum() {
		return surpnum;
	}
	public void setSurpnum(Integer surpnum) {
		this.surpnum = surpnum;
	}
	public Integer getTodaysurpnum() {
		return todaysurpnum;
	}
	public void setTodaysurpnum(Integer todaysurpnum) {
		this.todaysurpnum = todaysurpnum;
	}
	public Integer getEverydaynum() {
		return everydaynum;
	}
	public void setEverydaynum(Integer everydaynum) {
		this.everydaynum = everydaynum;
	}
	public Integer getEverymonthnum() {
		return everymonthnum;
	}
	public void setEverymonthnum(Integer everymonthnum) {
		this.everymonthnum = everymonthnum;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public Double getElec() {
		return elec;
	}
	public void setElec(Double elec) {
		this.elec = elec;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getMonthnum() {
		return monthnum;
	}
	public void setMonthnum(Integer monthnum) {
		this.monthnum = monthnum;
	}
	
}
