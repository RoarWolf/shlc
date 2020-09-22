package com.hedong.hedongwx.entity;

import java.util.Date;

public class CardOrder {
	
	private Integer id;//id

	private Integer uid;//用户id
	
	private Integer merchantid;//商户id
	
	private String ordernum;//订单号
	
	private String cardID;//卡号
	
	private Integer type;//类型
	
	private Integer status;//状态
	
	private String equipmentnum;//设备号/机位号 
	
	private Double expenditure;//金额	
	
	private Double balance;//余额	
	
	private Date begintime;//开始时间
	
	private String remark;//备注  
	
	private String common;//备注

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

	public Integer getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(Integer merchantid) {
		this.merchantid = merchantid;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public String getCardID() {
		return cardID;
	}

	public void setCardID(String cardID) {
		this.cardID = cardID;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEquipmentnum() {
		return equipmentnum;
	}

	public void setEquipmentnum(String equipmentnum) {
		this.equipmentnum = equipmentnum;
	}

	public Double getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(Double expenditure) {
		this.expenditure = expenditure;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Date getBegintime() {
		return begintime;
	}

	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCommon() {
		return common;
	}

	public void setCommon(String common) {
		this.common = common;
	}
	
	
	
}
