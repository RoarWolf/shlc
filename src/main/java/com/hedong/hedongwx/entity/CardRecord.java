package com.hedong.hedongwx.entity;

import java.util.Date;

public class CardRecord {
	
	private Integer id;
	
	private Integer uid;//申请人id

	private Integer cid;//卡id
	
	private String cardID;//卡号

	private Double balance;
	
	private Integer status;//申请处理状态
	
	private String applycase;
	
	private Integer type;//申请类型
	
	private Date recordTime;//记录时间
	
	private Integer replyid;//审核人id
	
	private Date replyTime;//记录时间
	
	private String replyinfo;
	
	private String remark;//备注
	
	private Double common;//备用金额
	
	private Double transfer;
	//**********************************************
	private String username;
	
	private String phoneNum;
	
	private String replyname;
	
	private String replyphone;
	//*********************************************
	private String startTime;
	
	private String endTime;
	
	//- - - - - - - - - - - - - - - - - - - - - - - 
	
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

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getCardID() {
		return cardID;
	}

	public void setCardID(String cardID) {
		this.cardID = cardID;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getApplycase() {
		return applycase;
	}

	public void setApplycase(String applycase) {
		this.applycase = applycase;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public Integer getReplyid() {
		return replyid;
	}

	public void setReplyid(Integer replyid) {
		this.replyid = replyid;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public String getReplyinfo() {
		return replyinfo;
	}

	public void setReplyinfo(String replyinfo) {
		this.replyinfo = replyinfo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Double getCommon() {
		return common;
	}

	public void setCommon(Double common) {
		this.common = common;
	}

	public Double getTransfer() {
		return transfer;
	}

	public void setTransfer(Double transfer) {
		this.transfer = transfer;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getReplyname() {
		return replyname;
	}

	public void setReplyname(String replyname) {
		this.replyname = replyname;
	}

	public String getReplyphone() {
		return replyphone;
	}

	public void setReplyphone(String replyphone) {
		this.replyphone = replyphone;
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
	
}
