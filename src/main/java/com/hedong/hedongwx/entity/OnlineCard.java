package com.hedong.hedongwx.entity;

import java.util.Date;

public class OnlineCard {

	/** 表主键id */
	private Integer id;
	/** 用户id */
	private Integer uid;
	/** 商户id */
	private Integer merid;
	/** 小区id */
	private Integer aid;
	/** 卡号 */
	private String cardID;
	/** 数字卡号 */
	private String figure;
	/** 余额 */
	private Double money;
	/** 赠送金额 */
	private Double sendmoney;
	/** 卡类型 1：在线卡 */
	private Integer type;
	/** 卡状态：0未激活、1 正常，2 挂失，4 注销 */
	private Integer status;
	/** 在线卡是否关联钱包   1关联  2不关联 */
	private Integer relevawalt;
	/** 创建时间 */
	private Date createTime;
	/** 备注 */
	private String remark;

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

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public String getCardID() {
		return cardID;
	}

	public void setCardID(String cardID) {
		this.cardID = cardID;
	}

	public String getFigure() {
		return figure;
	}

	public void setFigure(String figure) {
		this.figure = figure;
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
	
	public Integer getRelevawalt() {
		return relevawalt;
	}

	public void setRelevawalt(Integer relevawalt) {
		this.relevawalt = relevawalt;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
