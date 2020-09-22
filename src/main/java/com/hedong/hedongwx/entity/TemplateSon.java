package com.hedong.hedongwx.entity;

import java.util.Date;

public class TemplateSon {
	
	private Integer id;
	
	private Integer tempparid;
	
	private String name;
	
	private Integer status;

	private Integer type;//状态 【1:功率 2:按时间 3:按金额】',
	
	private Double money;
	
	private Integer chargeTime;
	
	private Integer chargeQuantity;
	
	private Date createTime;
	
	private Double remark;
	
	private String common1;
	
	private String common2;
	
	private String common3;

	private String brandName;//品牌名称
	
	private String telephone;//商家联系电话
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTempparid() {
		return tempparid;
	}
	public void setTempparid(Integer tempparid) {
		this.tempparid = tempparid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Integer getChargeTime() {
		return chargeTime;
	}
	public void setChargeTime(Integer chargeTime) {
		this.chargeTime = chargeTime;
	}
	
	public Integer getChargeQuantity() {
		return chargeQuantity;
	}
	public void setChargeQuantity(Integer chargeQuantity) {
		this.chargeQuantity = chargeQuantity;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Double getRemark() {
		return remark;
	}
	public void setRemark(Double remark) {
		this.remark = remark;
	}
	public String getCommon1() {
		return common1;
	}
	public void setCommon1(String common1) {
		this.common1 = common1;
	}
	public String getCommon2() {
		return common2;
	}
	public void setCommon2(String common2) {
		this.common2 = common2;
	}
	public String getCommon3() {
		return common3;
	}
	public void setCommon3(String common3) {
		this.common3 = common3;
	}
	
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	@Override
	public String toString() {
		return "TemplateSon [id=" + id + ", tempparid=" + tempparid + ", name=" + name + ", status=" + status
				+ ", type=" + type + ", money=" + money + ", chargeTime=" + chargeTime + ", chargeQuantity="
				+ chargeQuantity + ", createTime=" + createTime + ", remark=" + remark + ", common1=" + common1
				+ ", common2=" + common2 + ", common3=" + common3 + ", brandName=" + brandName + ", telephone="
				+ telephone + "]";
	}
	
	
}
