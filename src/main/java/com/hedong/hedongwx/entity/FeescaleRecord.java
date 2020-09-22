package com.hedong.hedongwx.entity;

import java.util.Date;

/**
 * 
 * @author zz 设备缴费记录
 */
public class FeescaleRecord {
	// 商家id
	private Integer merId;
	// 商家姓名
	private String merName;
	// 昵称
	private String nickbName;
	// 等级:0超级管理员，2管理员，-1合伙人
	private Integer rank;
	// 缴费的设备号
	private String equipmentNum;
	// 缴费订单号
	private String orderNum;
	// 缴费的金额
	private Double merPayMoney;
	// 设备的应缴费用
	private Double price;
	// 订单的创建时间
	private Date createTime;
	// 缴费的方式：0:手动自付一台，1:手动自付多台，2:手动合伙分摊一台，3：手动合伙分摊多台，4:为商户微信缴费
	private Integer payType;
	// 每页显示条数
	private Integer numPerPage;
	// 开始索引
	private Integer startIndex;
	// 总页数
	private Integer totalPages;
	// 网络类型
	private String netType;
	// 设备类型
	private String equipmentType;
	// 记录合伙人的信息
	private String note;
	//开始时间
	private String begintime;
	//结束时间
	private String endtime;
	//小区id
	private Integer aid;
	//续约时长
	private Integer renewal;
	//手机号
	private String phone;
	//收款人id
	private Integer reviceId;
	//状态
	private Integer state;
	
	
	
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getReviceId() {
		return reviceId;
	}

	public void setReviceId(Integer reviceId) {
		this.reviceId = reviceId;
	}

	public String getNickbName() {
		return nickbName;
	}

	public void setNickbName(String nickbName) {
		this.nickbName = nickbName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getRenewal() {
		return renewal;
	}

	public void setRenewal(Integer renewal) {
		this.renewal = renewal;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getMerId() {
		return merId;
	}

	public void setMerId(Integer merId) {
		this.merId = merId;
	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public String getEquipmentNum() {
		return equipmentNum;
	}

	public void setEquipmentNum(String equipmentNum) {
		this.equipmentNum = equipmentNum;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	

	public Double getMerPayMoney() {
		return merPayMoney;
	}

	public void setMerPayMoney(Double merPayMoney) {
		this.merPayMoney = merPayMoney;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
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

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}

	@Override
	public String toString() {
		return "FeescaleRecord [merId=" + merId + ", merName=" + merName + ", nickbName=" + nickbName + ", rank=" + rank
				+ ", equipmentNum=" + equipmentNum + ", orderNum=" + orderNum + ", merPayMoney=" + merPayMoney
				+ ", price=" + price + ", createTime=" + createTime + ", payType=" + payType + ", numPerPage="
				+ numPerPage + ", startIndex=" + startIndex + ", totalPages=" + totalPages + ", netType=" + netType
				+ ", equipmentType=" + equipmentType + ", note=" + note + ", begintime=" + begintime + ", endtime="
				+ endtime + ", aid=" + aid + ", renewal=" + renewal + ", phone=" + phone + ", reviceId=" + reviceId
				+ ", state=" + state + "]";
	}

	

	

	
	
	
}
