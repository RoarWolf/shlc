package com.hedong.hedongwx.entity;

import java.util.Date;

public class ChargeRecord {
	
	/** 钱包*/
	public static final Integer WALLETPAYTYPE = 1;
	/** 微信*/
	public static final Integer WEIXINPAYTYPE = 2;
	/** 支付宝*/
	public static final Integer ALIPAYPAYTYPE = 3;
	// 银联
	public static final Integer UNION = 12;
	
	private Integer id;
	
	private Integer merchantid;
	
	private Integer uid;//用户id;
	
	private String	ordernum;//订单号
	
	private Integer number;//退款状态0:正常    1 全额退款、2部分退款
	
	private Integer paytype;//消费类型： '1:钱包  2:微信  3:支付宝  4:包月下发数据 5:投币 6:离线卡 7:在线卡 8:支付宝小程序'
	
	private Integer status;//充电时的状态1充电中,2已结束
	
	private String	equipmentnum;//设备号、机位号
	
	private Integer port;//端口号
	
	private Double	expenditure;//消费金额
	
	private Double 	refundMoney;//退款金额
	
	private String	durationtime;//充电时长
	
	private Integer quantity;//电量
	
	private Date begintime;//开始时间
	
	private Date endtime;//结束时间
	
	private Integer ifcontinue;//是否为续充
	
	private Integer resultinfo;//返回结果
	
	private Date refundTime;
	
	private Double totalMoney;
	
	private Object allPortStatus;
	
	//
	private String username;
	
	private String dealer;
	
    private Integer numPerPage;

    private Integer startIndex;
    
	private String begintimes;
	
	private String endtimes;
	
	private Integer consumeQuantity;//消耗的电量
	
	private Integer consumeTime;//消耗的电量
	
	private Integer flag;
	
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(Integer consumeTime) {
		this.consumeTime = consumeTime;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getConsumeQuantity() {
		return consumeQuantity;
	}

	public void setConsumeQuantity(Integer consumeQuantity) {
		this.consumeQuantity = consumeQuantity;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(Integer merchantid) {
		this.merchantid = merchantid;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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

	public String getEquipmentnum() {
		return equipmentnum;
	}

	public void setEquipmentnum(String equipmentnum) {
		this.equipmentnum = equipmentnum;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Double getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(Double expenditure) {
		this.expenditure = expenditure;
	}

	public Double getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(Double refundMoney) {
		this.refundMoney = refundMoney;
	}

	public String getDurationtime() {
		return durationtime;
	}

	public void setDurationtime(String durationtime) {
		this.durationtime = durationtime;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Date getBegintime() {
		return begintime;
	}

	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public Integer getIfcontinue() {
		return ifcontinue;
	}

	public void setIfcontinue(Integer ifcontinue) {
		this.ifcontinue = ifcontinue;
	}

	public Integer getResultinfo() {
		return resultinfo;
	}

	public void setResultinfo(Integer resultinfo) {
		this.resultinfo = resultinfo;
	}

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Object getAllPortStatus() {
		return allPortStatus;
	}

	public void setAllPortStatus(Object allPortStatus) {
		this.allPortStatus = allPortStatus;
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

	public String getBegintimes() {
		return begintimes;
	}

	public void setBegintimes(String begintimes) {
		this.begintimes = begintimes;
	}

	public String getEndtimes() {
		return endtimes;
	}

	public void setEndtimes(String endtimes) {
		this.endtimes = endtimes;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	@Override
	public String toString() {
		return "ChargeRecord [id=" + id + ", merchantid=" + merchantid + ", uid=" + uid + ", ordernum=" + ordernum
				+ ", number=" + number + ", paytype=" + paytype + ", status=" + status + ", equipmentnum="
				+ equipmentnum + ", port=" + port + ", expenditure=" + expenditure + ", refundMoney=" + refundMoney
				+ ", durationtime=" + durationtime + ", quantity=" + quantity + ", begintime=" + begintime
				+ ", endtime=" + endtime + ", resultinfo=" + resultinfo + ", consumeQuantity=" + consumeQuantity + "]";
	}
	
	
	
	
	
	
	
}
