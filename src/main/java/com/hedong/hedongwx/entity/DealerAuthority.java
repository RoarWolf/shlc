package com.hedong.hedongwx.entity;

/**
 * @Description 商户权限
 * @author  origin
 * 创建时间：   2019年7月5日 下午7:36:19  
 */
public class DealerAuthority {
	
	/** 商户信息权限表id  */
	private Integer id;
	
	/** 商户id  */
	private Integer merid;

	/** 提现通知：1.接受通知 2.不接受通知  */
	private Integer withmess;

	/** 设备上下线通知：1.接受通知 2.不接受通知  */
	private Integer equipmess;

	/** 收益通知：1.接受通知 2.不接受通知  */
	private Integer incomemess;

	/** 订单收入通知：1.接受通知 2.不接受通知  */
	private Integer ordermess;
	
	/** 脉冲模块自动退款：1.系统扫描退款 2.用户自己手动退款  */
	private Integer incoinrefund;
	
	/** 是否展示投币收益信息 1:是  2:否  */
	private Integer showincoins;

	/**   */
	private Integer newsmess;

	/**   */
	private Integer inform;

	/**   */
	private Integer informess;

	/**   */
	private Integer sendmess;
	/** 自动续费：0-默认关闭，1-开启*/
	private Integer autopay;
	
	/** 开启分摊：0-默认关闭，1-开启分摊*/
	private Integer apportion;
	
	/** 开启自动提现:1-默认关闭,2-开启自动提现*/
	private Integer autoWithdraw;
	
	
	
	public Integer getAutoWithdraw() {
		return autoWithdraw;
	}

	public void setAutoWithdraw(Integer autoWithdraw) {
		this.autoWithdraw = autoWithdraw;
	}

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

	public Integer getWithmess() {
		return withmess;
	}

	public void setWithmess(Integer withmess) {
		this.withmess = withmess;
	}

	public Integer getEquipmess() {
		return equipmess;
	}

	public void setEquipmess(Integer equipmess) {
		this.equipmess = equipmess;
	}

	public Integer getIncomemess() {
		return incomemess;
	}

	public void setIncomemess(Integer incomemess) {
		this.incomemess = incomemess;
	}

	public Integer getOrdermess() {
		return ordermess;
	}

	public void setOrdermess(Integer ordermess) {
		this.ordermess = ordermess;
	}

	public Integer getIncoinrefund() {
		return incoinrefund;
	}

	public void setIncoinrefund(Integer incoinrefund) {
		this.incoinrefund = incoinrefund;
	}

	public Integer getNewsmess() {
		return newsmess;
	}

	public void setNewsmess(Integer newsmess) {
		this.newsmess = newsmess;
	}

	public Integer getInform() {
		return inform;
	}

	public void setInform(Integer inform) {
		this.inform = inform;
	}

	public Integer getInformess() {
		return informess;
	}

	public void setInformess(Integer informess) {
		this.informess = informess;
	}

	public Integer getSendmess() {
		return sendmess;
	}

	public void setSendmess(Integer sendmess) {
		this.sendmess = sendmess;
	}
	
	public Integer getShowincoins() {
		return showincoins;
	}

	public void setShowincoins(Integer showincoins) {
		this.showincoins = showincoins;
	}

	public Integer getAutopay() {
		return autopay;
	}

	public void setAutopay(Integer autopay) {
		this.autopay = autopay;
	}

	public Integer getApportion() {
		return apportion;
	}

	public void setApportion(Integer apportion) {
		this.apportion = apportion;
	}

	@Override
	public String toString() {
		return "DealerAuthority [id=" + id + ", merid=" + merid + ", withmess=" + withmess + ", equipmess=" + equipmess
				+ ", incomemess=" + incomemess + ", ordermess=" + ordermess + ", incoinrefund=" + incoinrefund
				+ ", showincoins=" + showincoins + ", newsmess=" + newsmess + ", inform=" + inform + ", informess="
				+ informess + ", sendmess=" + sendmess + ", autopay=" + autopay + ", apportion=" + apportion
				+ ", autoWithdraw=" + autoWithdraw + "]";
	}

	
	
}
