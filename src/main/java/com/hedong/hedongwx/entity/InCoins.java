package com.hedong.hedongwx.entity;

import java.util.Date;

public class InCoins {

	private Integer id;
	/** 商户id*/
	private Integer merchantid;
	/** 用户id*/
	private Integer uid;
	/** 订单号*/
	private String ordernum;
	/** 机器编号*/
	private String equipmentnum;
	/** 设备投币通道 -- 1可用、0不可用*/
	private Byte port;
	/** 金额*/
	private Double money;
	/** 投币数量*/
	private Byte coinNum;
	/** 支付状态 -- 0未支付、1支付成功*/
	private Byte status;
	/**操作类型 -- 
	 * 1通过微信下发投币、2通过支付宝下发投币、3设备投币成功信息上传服务器、4 微信退款、
	 * 5 支付宝退款、6 钱包支付、7钱包退款、8微 小程序、9微信小程序退款、10支付宝小程序、
	 * 11支付宝小程序退款,12-银联下发投币 13-银联退款
	 */
	private Byte handletype;
	/** 回复类型 -- 0未回复、1回复成功*/
	private Byte recycletype;
	/** 记录时间*/
	private Date beginTime;
	/** 退款时间*/
	private Date refundTime;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMerchantid() {
		return merchantid;
	}
	public void setMerchantid(Integer merchantid) {
		this.merchantid = merchantid;
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
	public String getEquipmentnum() {
		return equipmentnum;
	}
	public void setEquipmentnum(String equipmentnum) {
		this.equipmentnum = equipmentnum;
	}
	public Byte getPort() {
		return port;
	}
	public void setPort(Byte port) {
		this.port = port;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Byte getCoinNum() {
		return coinNum;
	}
	public void setCoinNum(Byte coinNum) {
		this.coinNum = coinNum;
	}
	public Byte getHandletype() {
		return handletype;
	}
	public void setHandletype(Byte handletype) {
		this.handletype = handletype;
	}
	public Byte getRecycletype() {
		return recycletype;
	}
	public void setRecycletype(Byte recycletype) {
		this.recycletype = recycletype;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Date getRefundTime() {
		return refundTime;
	}
	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}
	
	
}
