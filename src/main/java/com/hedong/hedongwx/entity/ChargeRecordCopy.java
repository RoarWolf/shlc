package com.hedong.hedongwx.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChargeRecordCopy {
	
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
	
	private Double	paymoney;//消费金额
	
	private Double	chargemoney;//消费金额
	
	private Double 	serverMoney;//退款金额
	
	private Double 	refundMoney;//退款金额
	
	private Integer	charge_time;//充电时长
	
	private Integer charge_elec;//电量
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;//创建时间
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date payTime;//支付时间
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date beginTime;//开始时间
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date endTime;//结束时间
	
	private Integer resultinfo;//返回结果
	
	private Date refundTime;
	
	private Double totalMoney;
	
	private Object allPortStatus;
	
	//
	private String username;
	
	private String dealer;
	
	private Integer useelec;//消耗的电量
	
	private Integer usetime;//充电时长
	
	private Integer ctrlWay;//控制方式
	
	private Integer ctrlParam;//控制参数
	
	private Integer chargeWay;//充电模式
	
	private String equipmentnick;
	
	private Integer equipemnttype;

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

	public Double getPaymoney() {
		return paymoney;
	}

	public void setPaymoney(Double paymoney) {
		this.paymoney = paymoney;
	}

	public Double getChargemoney() {
		return chargemoney;
	}

	public void setChargemoney(Double chargemoney) {
		this.chargemoney = chargemoney;
	}

	public Double getServerMoney() {
		return serverMoney;
	}

	public void setServerMoney(Double serverMoney) {
		this.serverMoney = serverMoney;
	}

	public Double getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(Double refundMoney) {
		this.refundMoney = refundMoney;
	}

	public Integer getCharge_time() {
		return charge_time;
	}

	public void setCharge_time(Integer charge_time) {
		this.charge_time = charge_time;
	}

	public Integer getCharge_elec() {
		return charge_elec;
	}

	public void setCharge_elec(Integer charge_elec) {
		this.charge_elec = charge_elec;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getResultinfo() {
		return resultinfo;
	}

	public void setResultinfo(Integer resultinfo) {
		this.resultinfo = resultinfo;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
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

	public Integer getUseelec() {
		return useelec;
	}

	public void setUseelec(Integer useelec) {
		this.useelec = useelec;
	}

	public Integer getUsetime() {
		return usetime;
	}

	public void setUsetime(Integer usetime) {
		this.usetime = usetime;
	}

	public Integer getCtrlWay() {
		return ctrlWay;
	}

	public void setCtrlWay(Integer ctrlWay) {
		this.ctrlWay = ctrlWay;
	}

	public Integer getCtrlParam() {
		return ctrlParam;
	}

	public void setCtrlParam(Integer ctrlParam) {
		this.ctrlParam = ctrlParam;
	}

	public Integer getChargeWay() {
		return chargeWay;
	}

	public void setChargeWay(Integer chargeWay) {
		this.chargeWay = chargeWay;
	}

	public String getEquipmentnick() {
		return equipmentnick;
	}

	public void setEquipmentnick(String equipmentnick) {
		this.equipmentnick = equipmentnick;
	}

	public Integer getEquipemnttype() {
		return equipemnttype;
	}

	public void setEquipemnttype(Integer equipemnttype) {
		this.equipemnttype = equipemnttype;
	}

	
	
}
