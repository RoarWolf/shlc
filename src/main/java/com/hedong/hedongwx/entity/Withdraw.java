package com.hedong.hedongwx.entity;

import java.util.Date;

public class Withdraw {

	/** 表自增id*/
	private Integer id;
	/** 提现单号*/
	private String withdrawnum;
	/** 用户id*/
	private Integer userId;
	/** 银行卡号*/
	private String bankcardnum;
	/** 银行卡所属银行*/
	private String bankname;
	/** 提现状态0:待处理  1:已通过  2:被拒绝  3:提现至零钱 4:待开发票*/
	private Integer status;
	/** 提现金额*/
	private Double money;
	/** 提现手续费*/
	private Double servicecharge;
	/** 提现前总金额*/
	private Double userMoney;
	/** 提现申请时间*/
	private Date createTime;
	/** 提现到账时间*/
	private Date accountTime;
	/** 银行卡*/
	private String realname;
	
	private String phoneNum;
	
	private String begintime;

	private String endtime;
	
    private Integer numPerPage;

    private Integer startIndex;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getWithdrawnum() {
		return withdrawnum;
	}
	public void setWithdrawnum(String withdrawnum) {
		this.withdrawnum = withdrawnum;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getBankcardnum() {
		return bankcardnum;
	}
	public void setBankcardnum(String bankcardnum) {
		this.bankcardnum = bankcardnum;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
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
	public Double getServicecharge() {
		return servicecharge;
	}
	public void setServicecharge(Double servicecharge) {
		this.servicecharge = servicecharge;
	}
	public Double getUserMoney() {
		return userMoney;
	}
	public void setUserMoney(Double userMoney) {
		this.userMoney = userMoney;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getAccountTime() {
		return accountTime;
	}
	public void setAccountTime(Date accountTime) {
		this.accountTime = accountTime;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getBegintime() {
		return begintime;
	}
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
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
	@Override
	public String toString() {
		return "Withdraw [id=" + id + ", withdrawnum=" + withdrawnum + ", userId=" + userId + ", bankcardnum="
				+ bankcardnum + ", bankname=" + bankname + ", status=" + status + ", money=" + money
				+ ", servicecharge=" + servicecharge + ", userMoney=" + userMoney + ", createTime=" + createTime
				+ ", accountTime=" + accountTime + ", realname=" + realname + ", phoneNum=" + phoneNum + ", begintime="
				+ begintime + ", endtime=" + endtime + ", numPerPage=" + numPerPage + ", startIndex=" + startIndex
				+ "]";
	}
	
}
