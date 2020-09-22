package com.hedong.hedongwx.entity;

public class UserBankcard {

	/** 表自增id*/
	private Integer id;
	/** 银行卡号*/
	private String bankcardnum;
	/** 用户真实姓名*/
	private String realname;
	/** 所属银行*/
	private String bankname;
	/** 用户id*/
	private Integer userId;
	/** 卡类型：1、个人银行卡2、对公账户*/
	private Integer type;
	/** 公司名称*/
	private String company;
	/** 提现费率*/
	private Integer rate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBankcardnum() {
		return bankcardnum;
	}
	public void setBankcardnum(String bankcardnum) {
		this.bankcardnum = bankcardnum;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public Integer getRate() {
		return rate;
	}
	public void setRate(Integer rate) {
		this.rate = rate;
	}
	
}
