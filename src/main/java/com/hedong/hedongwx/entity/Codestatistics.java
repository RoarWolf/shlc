package com.hedong.hedongwx.entity;

import java.util.Date;

public class Codestatistics {
	
	private Integer id;
	/** 设备号  */
	private String code;
	/** 所属商户id  */
	private Integer merid;
	/** 小区id（地区id位置）  */
	private Integer areaid;
	/** 类型 1:设备 2:每日商户自动结算  */
	private Integer type;
	/** 总订单数量  */
	private Integer ordertotal;
	/** 微信订单数量  */
	private Integer wechatorder;
	/** 支付宝订单数量  */
	private Integer alipayorder;
	/** 微信退款订单数量  */
	private Integer wechatretord;
	/** 支付宝退款订单数量  */
	private Integer alipayretord;
	/** 今日总金额  */
	private Double moneytotal;
	/** 微信支付金额  */
	private Double wechatmoney;
	/** 支付宝支付金额  */
	private Double alipaymoney;
	/** 微信退款金额  */
	private Double wechatretmoney;
	/** 支付宝退款金额  */
	private Double alipayretmoney;
	/** 投币记录数量  */
	private Integer incoinsorder;
	/** 投币金额计数  */
	private Double incoinsmoney;
	/** 结算时间  */
	private Date countTime;
	/** 结算时间  */
	private String countIOSTime;
	/** 充电时间 */
	private Integer consumetime;
	/** 充电电量 */
	private Integer consumequantity;
	/** 扣费(缴费)金额 */
	private Double paymentmoney;
	
	private Double offcardmoney;
	
	private Double oncardmoney;
	
	private Double windowpulsemoney;

	private Integer unionpayordernum;
	
	private Double unionpaymoney;
	
	private Double refunionpaymoney;
//	/** 返还金额 */
//	private Double refpaymentmoney;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getMerid() {
		return merid;
	}
	public void setMerid(Integer merid) {
		this.merid = merid;
	}
	public Integer getAreaid() {
		return areaid;
	}
	public void setAreaid(Integer areaid) {
		this.areaid = areaid;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getOrdertotal() {
		return ordertotal;
	}
	public void setOrdertotal(Integer ordertotal) {
		this.ordertotal = ordertotal;
	}
	public Integer getWechatorder() {
		return wechatorder;
	}
	public void setWechatorder(Integer wechatorder) {
		this.wechatorder = wechatorder;
	}
	public Integer getAlipayorder() {
		return alipayorder;
	}
	public void setAlipayorder(Integer alipayorder) {
		this.alipayorder = alipayorder;
	}
	public Integer getWechatretord() {
		return wechatretord;
	}
	public void setWechatretord(Integer wechatretord) {
		this.wechatretord = wechatretord;
	}
	public Integer getAlipayretord() {
		return alipayretord;
	}
	public void setAlipayretord(Integer alipayretord) {
		this.alipayretord = alipayretord;
	}
	public Double getMoneytotal() {
		return moneytotal;
	}
	public void setMoneytotal(Double moneytotal) {
		this.moneytotal = moneytotal;
	}
	public Double getWechatmoney() {
		return wechatmoney;
	}
	public void setWechatmoney(Double wechatmoney) {
		this.wechatmoney = wechatmoney;
	}
	public Double getAlipaymoney() {
		return alipaymoney;
	}
	public void setAlipaymoney(Double alipaymoney) {
		this.alipaymoney = alipaymoney;
	}
	public Double getWechatretmoney() {
		return wechatretmoney;
	}
	public void setWechatretmoney(Double wechatretmoney) {
		this.wechatretmoney = wechatretmoney;
	}
	public Double getAlipayretmoney() {
		return alipayretmoney;
	}
	public void setAlipayretmoney(Double alipayretmoney) {
		this.alipayretmoney = alipayretmoney;
	}
	public Integer getIncoinsorder() {
		return incoinsorder;
	}
	public void setIncoinsorder(Integer incoinsorder) {
		this.incoinsorder = incoinsorder;
	}
	public Double getIncoinsmoney() {
		return incoinsmoney;
	}
	public void setIncoinsmoney(Double incoinsmoney) {
		this.incoinsmoney = incoinsmoney;
	}
	public Date getCountTime() {
		return countTime;
	}
	public void setCountTime(Date countTime) {
		this.countTime = countTime;
	}
	public String getCountIOSTime() {
		return countIOSTime;
	}
	public void setCountIOSTime(String countIOSTime) {
		this.countIOSTime = countIOSTime;
	}
	public Integer getConsumetime() {
		return consumetime;
	}
	public void setConsumetime(Integer consumetime) {
		this.consumetime = consumetime;
	}
	public Integer getConsumequantity() {
		return consumequantity;
	}
	public void setConsumequantity(Integer consumequantity) {
		this.consumequantity = consumequantity;
	}
	public Double getPaymentmoney() {
		return paymentmoney;
	}
	public void setPaymentmoney(Double paymentmoney) {
		this.paymentmoney = paymentmoney;
	}
	public Double getOffcardmoney() {
		return offcardmoney;
	}
	public void setOffcardmoney(Double offcardmoney) {
		this.offcardmoney = offcardmoney;
	}
	public Double getOncardmoney() {
		return oncardmoney;
	}
	public void setOncardmoney(Double oncardmoney) {
		this.oncardmoney = oncardmoney;
	}
	public Double getWindowpulsemoney() {
		return windowpulsemoney;
	}
	public void setWindowpulsemoney(Double windowpulsemoney) {
		this.windowpulsemoney = windowpulsemoney;
	}
	public Integer getUnionpayordernum() {
		return unionpayordernum;
	}
	public void setUnionpayordernum(Integer unionpayordernum) {
		this.unionpayordernum = unionpayordernum;
	}
	public Double getUnionpaymoney() {
		return unionpaymoney;
	}
	public void setUnionpaymoney(Double unionpaymoney) {
		this.unionpaymoney = unionpaymoney;
	}
	public Double getRefunionpaymoney() {
		return refunionpaymoney;
	}
	public void setRefunionpaymoney(Double refunionpaymoney) {
		this.refunionpaymoney = refunionpaymoney;
	}
	
	
	
	
	
	
}
