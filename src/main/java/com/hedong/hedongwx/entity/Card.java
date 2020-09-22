package com.hedong.hedongwx.entity;

public class Card {
	
	private Integer id;//

	private Integer uid;//
	
	private String cardNum;//卡编号
	
	private Double cardMoney;//卡金额
	
	private String cardID;//卡ID（十六进制
	
	private Integer type;//卡类型。0：，1：
	
	private Integer status;//卡状态：1:正常，2 :挂失，2 停用，3 异常
	
	private String creationTime;//创建时间
	
	private String remark;//创建时间


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

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public Double getCardMoney() {
		return cardMoney;
	}

	public void setCardMoney(Double cardMoney) {
		this.cardMoney = cardMoney;
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

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	

}
