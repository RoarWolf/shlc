package com.hedong.hedongwx.entity;

import java.util.Date;

public class OfflineCard {

	/** 离线卡记录id*/
	private Integer id;
	/** 离线充值机商户id*/
	private Integer merchantid;
	/** 操作用户id*/
	private Integer uid;
	/** 离线卡记录订单号*/
	private String ordernum;
	/** 离线充值机编号*/
	private String equipmentnum;
	/** 离线卡卡号*/
	private String cardID;
	/** 离线卡卡余额*/
	private Double balance;
	/** 离线卡充值金额*/
	private Double chargemoney;
	/** 离线卡到账金额*/
	private Double accountmoney;
	/** 离线卡充值优惠金额*/
	private Double discountmoney;
	/** 离线卡操作类型*/
	private Integer handletype;// 0：扣费||1：充值||2：查询
	/** 离线卡操作回复类型*/
	private Integer recycletype;// 0：操作成功||1：余额不足||2：无卡||3：卡号不统一||4：其他错误
	/** 离线卡操作时间*/
	private Date beginTime;
	/** 支付类型————1-wx、2-alipay、3-wx refund、4-alipay refund、5-wallet、6-wallet refund 7.刷卡消费  8.支付宝小程序 9支付宝小程序退款*/
	private Integer paytype;
	/** 支付状态--0未支付1支付成功*/
	private Integer status;
	/** 退款时间*/
	private Integer refundTime;
	
	//条件
	private String username;//消费人
	
	private String dealer;//代理商
	
	private Date endTime;
	
	private String startTime; 
	
	private String endTimes;
	
    private Integer numPerPage;

    private Integer startIndex;
	
	
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
	public Double getChargemoney() {
		return chargemoney;
	}
	public void setChargemoney(Double chargemoney) {
		this.chargemoney = chargemoney;
	}
	public Double getAccountmoney() {
		return accountmoney;
	}
	public void setAccountmoney(Double accountmoney) {
		this.accountmoney = accountmoney;
	}
	public Double getDiscountmoney() {
		return discountmoney;
	}
	public void setDiscountmoney(Double discountmoney) {
		this.discountmoney = discountmoney;
	}
	public Integer getHandletype() {
		return handletype;
	}
	public void setHandletype(Integer handletype) {
		this.handletype = handletype;
	}
	public Integer getRecycletype() {
		return recycletype;
	}
	public void setRecycletype(Integer recycletype) {
		this.recycletype = recycletype;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
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
	public Integer getRefundTime() {
		return refundTime;
	}
	public void setRefundTime(Integer refundTime) {
		this.refundTime = refundTime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDealer() {
		return dealer;
	}
	public void setDealer(String dealer) {
		this.dealer = dealer;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTimes() {
		return endTimes;
	}
	public void setEndTimes(String endTimes) {
		this.endTimes = endTimes;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getNumPerPage() {
		return numPerPage;
	}
	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}
	public Integer getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	@Override
	public String toString() {
		return "OfflineCard [id=" + id + ", merchantid=" + merchantid + ", uid=" + uid + ", ordernum=" + ordernum
				+ ", equipmentnum=" + equipmentnum + ", cardID=" + cardID + ", balance=" + balance + ", chargemoney="
				+ chargemoney + ", accountmoney=" + accountmoney + ", discountmoney=" + discountmoney + ", handletype="
				+ handletype + ", recycletype=" + recycletype + ", beginTime=" + beginTime + ", username=" + username
				+ ", dealer=" + dealer + ", endTime=" + endTime + ", numPerPage=" + numPerPage + ", startIndex="
				+ startIndex + ", paytype=" + paytype + "]";
	}
	
	
	
}
