package com.hedong.hedongwx.entity;

import java.math.BigDecimal;
import java.util.Date;

public class EquipmentNew {
	
    private String code;//充电桩编号

    private Byte bindtype;//绑定状态（暂不需要，后续有桩主在开启）

    private Byte state;//在线状态 0离线 1在线

    private String hardversion;//硬件版本

    private String softversion;//软件版本

    private String subHardversion;//次单元硬件版本

    private String subSoftversion;//次单元软件版本

    private Integer dcModeltype;//直流模块类型

    private Integer dcModelnum;//直流模块总数

    private Integer dcModelpower;//直流模块单模块功率

    private Integer billtype;//计费版本

    private String location;//地址详情

    private BigDecimal lon;//经度

    private BigDecimal lat;//纬度

    private Date createTime;//创建时间

    private Date registTime;//绑定时间（暂不用）

    private Integer tempid;//模板id

    private Integer aid;//站点id

    private String remark;//充电桩名称

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Byte getBindtype() {
        return bindtype;
    }

    public void setBindtype(Byte bindtype) {
        this.bindtype = bindtype;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getHardversion() {
        return hardversion;
    }

    public void setHardversion(String hardversion) {
        this.hardversion = hardversion == null ? null : hardversion.trim();
    }

    public String getSoftversion() {
        return softversion;
    }

    public void setSoftversion(String softversion) {
        this.softversion = softversion == null ? null : softversion.trim();
    }

    public String getSubHardversion() {
        return subHardversion;
    }

    public void setSubHardversion(String subHardversion) {
        this.subHardversion = subHardversion == null ? null : subHardversion.trim();
    }

    public String getSubSoftversion() {
        return subSoftversion;
    }

    public void setSubSoftversion(String subSoftversion) {
        this.subSoftversion = subSoftversion == null ? null : subSoftversion.trim();
    }

    public Integer getDcModeltype() {
        return dcModeltype;
    }

    public void setDcModeltype(Integer dcModeltype) {
        this.dcModeltype = dcModeltype;
    }

    public Integer getDcModelnum() {
        return dcModelnum;
    }

    public void setDcModelnum(Integer dcModelnum) {
        this.dcModelnum = dcModelnum;
    }

    public Integer getDcModelpower() {
        return dcModelpower;
    }

    public void setDcModelpower(Integer dcModelpower) {
        this.dcModelpower = dcModelpower;
    }

    public Integer getBilltype() {
        return billtype;
    }

    public void setBilltype(Integer billtype) {
        this.billtype = billtype;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    public Integer getTempid() {
        return tempid;
    }

    public void setTempid(Integer tempid) {
        this.tempid = tempid;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	@Override
	public String toString() {
		return "EquipmentNew [code=" + code + ", bindtype=" + bindtype + ", state=" + state + ", hardversion="
				+ hardversion + ", softversion=" + softversion + ", subHardversion=" + subHardversion
				+ ", subSoftversion=" + subSoftversion + ", dcModeltype=" + dcModeltype + ", dcModelnum=" + dcModelnum
				+ ", dcModelpower=" + dcModelpower + ", billtype=" + billtype + ", location=" + location + ", lon="
				+ lon + ", lat=" + lat + ", createTime=" + createTime + ", registTime=" + registTime + ", tempid="
				+ tempid + ", aid=" + aid + ", remark=" + remark + "]";
	}

    
}
