package com.hedong.hedongwx.entity;

import java.util.Date;

public class AdminiStrator {
	
	private Integer id;//用户id
	
	private String  username;//用户昵称
	
	private String  realname;//用户真实姓名
	
	private String  openid;//微信中openid
	
	private String  password;//密码
	
	private String  phoneNum;//电话
	
	private Double  earnings;//用户收益
	
	private Double  balance;//用户钱包余额
	
	private Integer  feerate;//费率
	
	private Integer rank;//用户等级 
	
	private Date createTime;//用户注册时间
	
	private Date updateTime;//用户修改时间
	
	private String order;//排序
	
	//银行卡
	private String bankname;//银行名字
	
	private String bankcardnum;//银行卡号
	
    private Integer numPerPage;

    private Integer startIndex;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public Double getEarnings() {
		return earnings;
	}

	public void setEarnings(Double earnings) {
		this.earnings = earnings;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getFeerate() {
		return feerate;
	}

	public void setFeerate(Integer feerate) {
		this.feerate = feerate;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getBankcardnum() {
		return bankcardnum;
	}

	public void setBankcardnum(String bankcardnum) {
		this.bankcardnum = bankcardnum;
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
