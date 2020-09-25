package com.hedong.hedongwx.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 普通用户钱包明细
 * @author RoarWolf
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GeneralDetail {

	/** 表主键id*/
	private Integer id;
	/** 用户id*/
	private Integer uid;
	/** 用户id*/
	private Integer merid;
	/** 订单号*/
	private String ordernum;
	/** 变动金额*/
	private Double money;
	/** 变动金额*/
	private Double sendmoney;
	/** 充值到账金额*/
	private Double	tomoney;
	/** 实时余额*/
	private Double balance;
	/** 充值后的余额*/
	private Double 	topupbalance;
	/** 赠送后余额*/
	private Double 	givebalance;
	/** 金额来源 1、充值-2、充电-3、投币-4、离线卡充值-5、退款到钱包 6、钱包退款 7虚拟充值  8虚拟退款 9在线卡记录 */
	private Integer paysource;
	/** 备注部分信息 */
	private String remark;
	/** 记录时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
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
	public Integer getPaysource() {
		return paysource;
	}
	public void setPaysource(Integer paysource) {
		this.paysource = paysource;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
