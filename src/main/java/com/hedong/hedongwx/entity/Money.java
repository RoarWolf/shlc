package com.hedong.hedongwx.entity;

import java.util.Date;

public class Money {
	
	private Integer id;
	
	private Integer uid;//用户id;
	
	private String	ordernum;//充值订单号
	
	/** 充值类型： 0:充值钱包  1:虚拟充值钱包   2:钱包退费 3:充电记录退费 4:离线卡充值退费 5:模拟投币退费 6虚拟钱包退费7支付宝 8支付宝退款  9:支付宝小程序*/
	private Integer paytype;
	
	private Integer status;//订单类型：0：预支付     1：支付成功
	
	private Double	money;//充值付款金额
	
	private Double	sendmoney;//赠送金额
	
	private Double	tomoney;//充值到账金额
	
	private Double 	balance;//充值后的金额
	
	private Double 	topupbalance;//充值后的余额
	
	private Double 	givebalance;//赠送后余额
	
	private Date paytime;//充值时间
	
	private String 	remark;//备注

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

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getSendmoney() {
		return sendmoney;
	}

	public void setSendmoney(Double sendmoney) {
		this.sendmoney = sendmoney;
	}

	public Double getTomoney() {
		return tomoney;
	}

	public void setTomoney(Double tomoney) {
		this.tomoney = tomoney;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Double getTopupbalance() {
		return topupbalance;
	}

	public void setTopupbalance(Double topupbalance) {
		this.topupbalance = topupbalance;
	}

	public Double getGivebalance() {
		return givebalance;
	}

	public void setGivebalance(Double givebalance) {
		this.givebalance = givebalance;
	}

	public Date getPaytime() {
		return paytime;
	}

	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
}
