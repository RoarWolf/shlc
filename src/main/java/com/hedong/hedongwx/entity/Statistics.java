package com.hedong.hedongwx.entity;

import java.util.Date;

public class Statistics {
	
	private Integer id;
	/** 总订单数量  */
	private Integer ordertotal;
	/** 微信订单数量  */
	private Integer wecorder;
	/** 支付宝订单数量  */
	private Integer aliorder;
	/** 微信退款订单数量  */
	private Integer wecretord;
	/** 支付宝退款订单数量  */
	private Integer aliretord;
	/** 今日总金额  */
	private Double moneytotal;
	/** 微信支付金额  */
	private Double wecmoney;
	/** 支付宝支付金额  */
	private Double alimoney;
	/** 微信退款金额  */
	private Double wecretmoney;
	/** 支付宝退款金额  */
	private Double aliretmoney;
	/** 投币记录数量  */
	private Integer incoinsorder;
	/** 投币金额计数  */
	private Double incoinsmoney;
	/** 结算时间  */
	private Date countTime;

	
	


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrdertotal() {
		return ordertotal;
	}

	public void setOrdertotal(Integer ordertotal) {
		this.ordertotal = ordertotal;
	}

	public Integer getWecorder() {
		return wecorder;
	}

	public void setWecorder(Integer wecorder) {
		this.wecorder = wecorder;
	}

	public Integer getAliorder() {
		return aliorder;
	}

	public void setAliorder(Integer aliorder) {
		this.aliorder = aliorder;
	}

	public Integer getWecretord() {
		return wecretord;
	}

	public void setWecretord(Integer wecretord) {
		this.wecretord = wecretord;
	}

	public Integer getAliretord() {
		return aliretord;
	}

	public void setAliretord(Integer aliretord) {
		this.aliretord = aliretord;
	}

	public Double getMoneytotal() {
		return moneytotal;
	}

	public void setMoneytotal(Double moneytotal) {
		this.moneytotal = moneytotal;
	}

	public Double getWecmoney() {
		return wecmoney;
	}

	public void setWecmoney(Double wecmoney) {
		this.wecmoney = wecmoney;
	}

	public Double getAlimoney() {
		return alimoney;
	}

	public void setAlimoney(Double alimoney) {
		this.alimoney = alimoney;
	}

	public Double getWecretmoney() {
		return wecretmoney;
	}

	public void setWecretmoney(Double wecretmoney) {
		this.wecretmoney = wecretmoney;
	}

	public Double getAliretmoney() {
		return aliretmoney;
	}

	public void setAliretmoney(Double aliretmoney) {
		this.aliretmoney = aliretmoney;
	}

	public Date getCountTime() {
		return countTime;
	}

	public void setCountTime(Date countTime) {
		this.countTime = countTime;
	}
	
	public Integer getIncoinsorder() {
		return incoinsorder;
	}

	public void setIncoinsorder(Integer incoinsorder) {
		this.incoinsorder = incoinsorder;
	}

	public Double getIncoinsmoney() {
		return incoinsmoney;
	}

	public void setIncoinsmoney(Double incoinsmoney) {
		this.incoinsmoney = incoinsmoney;
	}
	
}
