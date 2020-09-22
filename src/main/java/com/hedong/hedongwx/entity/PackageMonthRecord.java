package com.hedong.hedongwx.entity;

import java.util.Date;

public class PackageMonthRecord {

	/** 表自增id*/
	private Integer id;
	/** 用户id*/
	private Integer uid;
	/** 商户id*/
	private Integer merid;
	/** 订单号*/
	private String ordernum;
	/** 金额*/
	private Double money;
	/** 支付来源1、支付开通包月   2、包月充电*/
	private Integer paysource;
	/** 状态 0、未成功    1、正常  2、退回*/
	private Integer status;
	/** 每日次数*/
	private Integer everydaynum;
	/** 变动次数*/
	private Integer changenum;
	/** 剩余次数*/
	private Integer surpnum;
	/** 变化月数只在充值包月中显示*/
	private Integer changemonth;
	/** 模板时间*/
	private Integer time;
	/** 模板电量*/
	private Double elec;
	/** 是否退款   0否   1是*/
	private Integer ifrefund;
	/** 记录时间*/
	private Date createTime;
	
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
	public Integer getMerid() {
		return merid;
	}
	public void setMerid(Integer merid) {
		this.merid = merid;
	}
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Integer getPaysource() {
		return paysource;
	}
	public void setPaysource(Integer paysource) {
		this.paysource = paysource;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getEverydaynum() {
		return everydaynum;
	}
	public void setEverydaynum(Integer everydaynum) {
		this.everydaynum = everydaynum;
	}
	public Integer getChangenum() {
		return changenum;
	}
	public void setChangenum(Integer changenum) {
		this.changenum = changenum;
	}
	public Integer getSurpnum() {
		return surpnum;
	}
	public void setSurpnum(Integer surpnum) {
		this.surpnum = surpnum;
	}
	public Integer getChangemonth() {
		return changemonth;
	}
	public void setChangemonth(Integer changemonth) {
		this.changemonth = changemonth;
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
	public Integer getIfrefund() {
		return ifrefund;
	}
	public void setIfrefund(Integer ifrefund) {
		this.ifrefund = ifrefund;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
