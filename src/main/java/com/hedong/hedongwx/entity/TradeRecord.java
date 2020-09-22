package com.hedong.hedongwx.entity;

import java.util.Date;

public class TradeRecord {

	/** 表自增id */
	private Integer id;
	/** 商户id */
	private Integer merid;
	/** 管理者id */
	private Integer manid;
	/** 用户id */
	private Integer uid;
	/** 订单号 */
	private String ordernum;
	/** 金额 */
	private Double money;
	/** 商户收入金额 */
	private Double mermoney;
	/** 合伙人收入金额 */
	private Double manmoney;
	/** 设备号 */
	private String code;
	/** 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、钱包-5、在线卡-6、包月支付  */
	private Integer paysource;
	/** 支付方式1、钱包2、微信3、支付宝 、4微信小程序、5支付宝小程序 6虚拟充值  12银联app*/
	private Integer paytype;
	/** 订单状态1、正常2、退款 */
	private Integer status;
	/** 硬件版本号 */
	private String hardver;
	/** 描述字段 */
	private String comment;
	/** 创建时间 */
	private Date createTime;
	
	private String remark;

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

	public Integer getManid() {
		return manid;
	}

	public void setManid(Integer manid) {
		this.manid = manid;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getMermoney() {
		return mermoney;
	}

	public void setMermoney(Double mermoney) {
		this.mermoney = mermoney;
	}

	public Double getManmoney() {
		return manmoney;
	}

	public void setManmoney(Double manmoney) {
		this.manmoney = manmoney;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getPaysource() {
		return paysource;
	}

	public void setPaysource(Integer paysource) {
		this.paysource = paysource;
	}

	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getHardver() {
		return hardver;
	}

	public void setHardver(String hardver) {
		this.hardver = hardver;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	@Override
	public String toString() {
		return "TradeRecord [id=" + id + ", merid=" + merid + ", manid=" + manid + ", uid=" + uid + ", ordernum="
				+ ordernum + ", money=" + money + ", mermoney=" + mermoney + ", manmoney=" + manmoney + ", code=" + code
				+ ", paysource=" + paysource + ", paytype=" + paytype + ", status=" + status + ", hardver=" + hardver
				+ ", comment=" + comment + ", createTime=" + createTime + ", remark=" + remark + "]";
	}
	
	

}
