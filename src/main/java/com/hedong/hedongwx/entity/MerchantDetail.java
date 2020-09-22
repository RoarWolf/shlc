package com.hedong.hedongwx.entity;

import java.util.Date;

/**
 * 商户资金明细
 * @author RoarWolf
 *
 */
public class MerchantDetail {
	
	//paysource
	/** 充电模块 */
	public static final int CHARGESOURCE = 1;
	/** 投币脉冲模块 */
	public static final int INCOINSSOURCE = 2;
	/** 离线充值机模块 */
	public static final int OFFLINESOURCE = 3;
	/** 提现 */
	public static final int WITHDRAWSOURCE = 4;
	/** 用户钱包充值 */
	public static final int WALLETSOURCE = 5;
	/** 用户在线卡充值 */
	public static final int ONLINESOURCE = 6;
	
	//paytype
	/** 钱包支付 */
	public static final int WALLETPAY = 1;
	/** 微信支付 */
	public static final int WEIXINPAY = 2;
	/** 支付宝支付 */
	public static final int ALIPAY = 3;
	/** 银联云闪付支付*/
	public static final int UNIONPAY = 4;
	//status
	/** 正常 */
	public static final int NORMAL = 1;
	/** 退款 */
	public static final int REFUND = 2;
	/** 提现通过 */
	public static final int WITHDRAWACCESS = 2;
	/** 提现失败 */
	public static final int WITHDRAWFAIL = 1;

	/** 表主键id*/
	private Integer id;
	/** 商户id*/
	private Integer merid;
	/** 订单号*/
	private String ordernum;
	/** 变动金额*/
	private Double money;
	/** 变动金额*/
	private Double balance;
	/** 金额来源
	 * 1、充电模块-2、脉冲模块-3、离线充值机-4、提现-5、用户充值钱包-6、用户在线卡充值-7、包月支付 ,8-缴费*/
	private Integer paysource;
	/** 支付方式：1-商户收益,2-微信,3-支付宝*/
	private Integer paytype;
	/** 订单状态（设备1、正常-2、退款）、（提现1、拒绝-2、待处理/成功）      (缴费：  1-收入,2-支出)*/
	private Integer status;
	/** 记录时间*/
	private Date createTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public Integer getMerid() {
		return merid;
	}
	public void setMerid(Integer merid) {
		this.merid = merid;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
