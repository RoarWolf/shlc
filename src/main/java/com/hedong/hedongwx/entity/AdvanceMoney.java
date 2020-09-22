package com.hedong.hedongwx.entity;

import com.alipay.api.domain.Data;

public class AdvanceMoney {
	
	private Integer id;
	
	private Integer merid;
	
	private Integer uid;
	
	private Double money;
	
	private Data updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMerid() {
		return merid;
	}

	public void setMerid(Integer merid) {
		this.merid = merid;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Data getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Data updateTime) {
		this.updateTime = updateTime;
	}

	
	
	
}
