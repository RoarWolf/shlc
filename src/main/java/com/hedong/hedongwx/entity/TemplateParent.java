package com.hedong.hedongwx.entity;

import java.util.List;

public class TemplateParent {
	
	private Integer id;
	
	private String name;
	
	private Integer merchantid;
	
	private Integer status;//模板类型 0充电模板  1离线卡模板     2模拟投币模板（脉冲模板） 3钱包模板   4在线卡模板  5包月模板  6:v3模板
	
	private String createTime;
	
	private String remark;//品牌名称
	
	private String common1; //售后电话
	
	private Integer common2; //充电记录时：默认1   1:按时间电量最小  2:时间最小  3:电量最小
	
	private String common3;//充电记录时：是否为默认模板  1:是  0:否
	
	private Integer permit;//是否支持退费 1支持  2不支持
	
	private Integer walletpay;//是否强制钱包支付 1:是  2:否[v3时为是否临时充电]
	
	private Integer ifmonth;//是否开放包月功能 1:是  2:否
	
	private Integer ifalipay;//是否支持临时充电  【1:支持   2:不支持】
	
	private Integer grade;//是否开启分等级模板  1:是  2:否
	
	private Integer stairid; 
	
	private Integer rank;//[v3时为是刷卡时间]

	private Integer pitchon;//选中 1:是  2:否
	
	private Object gather;
	
	private Integer hasbagMonth;//是否含有包月模板
	
	private String chargeInfo;//充电详情说明
	
	private List<TemplateParent> tempparList;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getMerchantid() {
		return merchantid;
	}
	public void setMerchantid(Integer merchantid) {
		this.merchantid = merchantid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCommon1() {
		return common1;
	}
	public void setCommon1(String common1) {
		this.common1 = common1;
	}
	public Integer getCommon2() {
		return common2;
	}
	public void setCommon2(Integer common2) {
		this.common2 = common2;
	}
	public String getCommon3() {
		return common3;
	}
	public void setCommon3(String common3) {
		this.common3 = common3;
	}
	public Integer getPermit() {
		return permit;
	}
	public void setPermit(Integer permit) {
		this.permit = permit;
	}
	public Integer getWalletpay() {
		return walletpay;
	}
	public void setWalletpay(Integer walletpay) {
		this.walletpay = walletpay;
	}
	
	public Integer getIfmonth() {
		return ifmonth;
	}
	public void setIfmonth(Integer ifmonth) {
		this.ifmonth = ifmonth;
	}
	public Integer getIfalipay() {
		return ifalipay;
	}
	public void setIfalipay(Integer ifalipay) {
		this.ifalipay = ifalipay;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public Integer getStairid() {
		return stairid;
	}
	public void setStairid(Integer stairid) {
		this.stairid = stairid;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Integer getPitchon() {
		return pitchon;
	}
	public void setPitchon(Integer pitchon) {
		this.pitchon = pitchon;
	}
	public Object getGather() {
		return gather;
	}
	public void setGather(Object gather) {
		this.gather = gather;
	}
	public Integer getHasbagMonth() {
		return hasbagMonth;
	}
	public void setHasbagMonth(Integer hasbagMonth) {
		this.hasbagMonth = hasbagMonth;
	}
	public List<TemplateParent> getTempparList() {
		return tempparList;
	}
	public void setTempparList(List<TemplateParent> tempparList) {
		this.tempparList = tempparList;
	}
	
	
	//充电详情说明
	public String getChargeInfo() {
		return chargeInfo;
	}
	public void setChargeInfo(String chargeInfo) {
		this.chargeInfo = chargeInfo;
	}
	@Override
	public String toString() {
		return "TemplateParent [id=" + id + ", name=" + name + ", merchantid=" + merchantid + ", status=" + status
				+ ", createTime=" + createTime + ", remark=" + remark + ", common1=" + common1 + ", common2=" + common2
				+ ", common3=" + common3 + ", permit=" + permit + ", walletpay=" + walletpay + ", ifmonth=" + ifmonth + ", ifalipay=" + ifalipay
				+ ", grade=" + grade + ", stairid=" + stairid + ", rank=" + rank + ", pitchon=" + pitchon + ", gather="
				+ gather + ", hasbagMonth=" + hasbagMonth + ", tempparList=" + tempparList + ", chargeInfo="
				+ chargeInfo + "]";
	}
	
	
}
