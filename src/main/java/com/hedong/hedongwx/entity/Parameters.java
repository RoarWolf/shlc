package com.hedong.hedongwx.entity;

import java.util.Date;

public class Parameters {
	// 商家的ID
	private Integer merId;
	
	private Integer uid;

	private String nickname;
	
	private String username;
	
	private String phone;
	
	private String dealer;
	
	private String realname;
	
	private String mobile;
	
	private String order;
	
	private String code;
	
	private String sort;
	
	private String level;
	
	private String source;
	
	private String type;
	
	private String state;
	
	private String status;
	
	private String number;
	
	private String remark;
	
	private String paramete;
	
	private String statement;
	
	private String quantity;
	
	private String startTime;
	
	private String endTime;
	
    private Integer pages;

    private Integer startnumber;

    private Integer deviceType;
    
    private String deviceIdIos;
    
    private String deviceIdAndroid;
    // 标记用户是否为微信特约商户
    private Integer subMer;
    // 开启自动提现的标识
    private Integer autoWithdraw;
    // 开始时间
    private Date beginTime;
    // 结束时间
    private Date stopTime;
    // 订单ID
    private Integer id;
    // 支付方式
    private Integer payType;
    // 支付来源
	private Integer paySource;
    // 订单状态
    private Integer orderStatus;
    // 结束原因(手动,充满，超功率)
    private Integer resultInfo;

	public Integer getPaySource() {
		return paySource;
	}

	public void setPaySource(Integer paySource) {
		this.paySource = paySource;
	}

	public Integer getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(Integer resultInfo) {
		this.resultInfo = resultInfo;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public Integer getMerId() {
		return merId;
	}

	public void setMerId(Integer merId) {
		this.merId = merId;
	}

	public Integer getAutoWithdraw() {
		return autoWithdraw;
	}

	public void setAutoWithdraw(Integer autoWithdraw) {
		this.autoWithdraw = autoWithdraw;
	}

	public Integer getSubMer() {
		return subMer;
	}

	public void setSubMer(Integer subMer) {
		this.subMer = subMer;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDealer() {
		return dealer;
	}

	public void setDealer(String dealer) {
		this.dealer = dealer;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getParamete() {
		return paramete;
	}

	public void setParamete(String paramete) {
		this.paramete = paramete;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public Integer getStartnumber() {
		return startnumber;
	}

	public void setStartnumber(Integer startnumber) {
		this.startnumber = startnumber;
	}

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceIdIos() {
		return deviceIdIos;
	}

	public void setDeviceIdIos(String deviceIdIos) {
		this.deviceIdIos = deviceIdIos;
	}

	public String getDeviceIdAndroid() {
		return deviceIdAndroid;
	}

	public void setDeviceIdAndroid(String deviceIdAndroid) {
		this.deviceIdAndroid = deviceIdAndroid;
	}

	@Override
	public String toString() {
		return "Parameters [merId=" + merId + ", uid=" + uid + ", nickname=" + nickname + ", username=" + username
				+ ", phone=" + phone + ", dealer=" + dealer + ", realname=" + realname + ", mobile=" + mobile
				+ ", order=" + order + ", code=" + code + ", sort=" + sort + ", level=" + level + ", source=" + source
				+ ", type=" + type + ", state=" + state + ", status=" + status + ", number=" + number + ", remark="
				+ remark + ", paramete=" + paramete + ", statement=" + statement + ", quantity=" + quantity
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", pages=" + pages + ", startnumber="
				+ startnumber + ", deviceType=" + deviceType + ", deviceIdIos=" + deviceIdIos + ", deviceIdAndroid="
				+ deviceIdAndroid + ", subMer=" + subMer + ", autoWithdraw=" + autoWithdraw + "]";
	}

	

}
