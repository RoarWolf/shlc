package com.hedong.hedongwx.entity;

import java.util.Date;

public class OnlineCardRecord {

	/** 表主键id */
	private Integer id;
	/** 用户id */
	private Integer uid;
	/** 商户id */
	private Integer merid;
	/** 操作人id */
	private Integer operid;
	/** 订单号/记录单号 */
	private String ordernum;
	/** 卡号 */
	private String cardID;
	/** 设备号 */
	private String code;
	/** 余额 */
	private Double balance;
	/** 充值金额*/
	private Double money;
	/** 赠送金额*/
	private Double sendmoney;
	/** 到账金额*/
	private Double accountmoney;
	/** 充值后的充值余额 */
	private Double 	topupbalance;//
	/** 赠送后赠送余额 */
	private Double 	givebalance;
	/** 类型  1消费、2余额回收、3微信充值 4:卡操作 5:微信退款 6：支付宝充值 7:支付宝退费 8:虚拟充值 9虚拟退款 10 支付宝小程序*/
	private Integer type;
	/** 状态 0:支付未成功、 1:支付完成 、2:激活、3:绑定、4:删除、5:挂失、6:解挂、7:卡号修改、8:修改备注 */
	private Integer status;
	/** 标记 1为正常订单未退费  2为该订单已退过费 */
	private Integer flag;
	/** 创建时间 */
	private Date createTime;

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

	public Integer getOperid() {
		return operid;
	}

	public void setOperid(Integer operid) {
		this.operid = operid;
	}

	public String getCardID() {
		return cardID;
	}

	public void setCardID(String cardID) {
		this.cardID = cardID;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getSendmoney() {
		return sendmoney;
	}

	public void setSendmoney(Double sendmoney) {
		this.sendmoney = sendmoney;
	}

	public Double getAccountmoney() {
		return accountmoney;
	}

	public void setAccountmoney(Double accountmoney) {
		this.accountmoney = accountmoney;
	}

	public Double getTopupbalance() {
		return topupbalance;
	}

	public void setTopupbalance(Double topupbalance) {
		this.topupbalance = topupbalance;
	}

	public Double getGivebalance() {
		return givebalance;
	}

	public void setGivebalance(Double givebalance) {
		this.givebalance = givebalance;
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

}
