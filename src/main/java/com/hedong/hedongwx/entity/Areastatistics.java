package com.hedong.hedongwx.entity;

import java.util.Date;

public class Areastatistics {

	/** 表自增id */
	private Integer id;
	/** 小区id */
	private Integer aid;
	/** 在线收益 */
	private Double onlineEarn;
	/** 投币收益 */
	private Integer coinEarn;
	/** 钱包充值收益 */
	private Double walletEarn;
	/** 设备收益 */
	private Double equEarn;
	/** 卡充值收益 */
	private Double cardEarn;
	/** 记录时间 */
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Double getOnlineEarn() {
		return onlineEarn;
	}

	public void setOnlineEarn(Double onlineEarn) {
		this.onlineEarn = onlineEarn;
	}

	public Integer getCoinEarn() {
		return coinEarn;
	}

	public void setCoinEarn(Integer coinEarn) {
		this.coinEarn = coinEarn;
	}

	public Double getWalletEarn() {
		return walletEarn;
	}

	public void setWalletEarn(Double walletEarn) {
		this.walletEarn = walletEarn;
	}

	public Double getEquEarn() {
		return equEarn;
	}

	public void setEquEarn(Double equEarn) {
		this.equEarn = equEarn;
	}

	public Double getCardEarn() {
		return cardEarn;
	}

	public void setCardEarn(Double cardEarn) {
		this.cardEarn = cardEarn;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
