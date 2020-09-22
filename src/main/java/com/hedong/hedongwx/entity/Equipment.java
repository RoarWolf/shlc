package com.hedong.hedongwx.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Equipment {
	/** 设备号 */
	private String code;
	/** 设备ccid */
	private String ccid;
	/** 设备IMEI */
	private String imei;
	/** 设备主板id */
	private String mainid;
	/** 设备信号强度 */
	private String csq;
	/** 设备IP、端口 */
	private String ipaddr;
	/** 设备绑定状态1、已绑定0、未绑定 */
	private Byte bindtype;
	/** 设备在线状态1、在线0、不在线 */
	private Byte state;
	/** 设备硬件版本 00-默认出厂版本、01-十路智慧款、02-电轿款、03-脉冲投币、04-离线充值机、
	 * 05-16路智慧款、06-20路智慧款、07单路交流桩 、08新版10路智慧款V3、
	 * 09-新版2路智慧款V3、10-新版20路智慧款V3*/
	private String hardversion;
	/** 设备硬件版本号 00-2G模块、01-4G模块、02-蓝牙模块 */
	private String hardversionnum;
	/** 设备软件版本号 */
	private String softversionnum;
	/** 设备主板版本 */
	private String mainType;
	/** 设备主板硬件版本 */
	private String mainHardver;
	/** 设备主板软件版本 */
	private String mainSoftver;
	/** 设备地理位置 */
	private String location;
	/** 经度 */
	private BigDecimal lon;
	/** 纬度 */
	private BigDecimal lat;
	/** 设备创建时间 */
	private Date createTime;
	/** 设备绑定时间 */
	private Date registTime;
	/** 收费模板id */
	private Integer tempid;
	/** 蓝牙设备最近最新活跃时间 */
	private Date liveTime;
	/** 设备类型1、联网模块2、蓝牙模块 */
	private Integer deviceType;
	/** 蓝牙设备id IOS系统 */
	private String deviceIdIos;
	/** 蓝牙设备id Android系统 */
	private String deviceIdAndroid;
	/** 收费模板名字 */
	private String tempname;
	/** 小区id */
	private Integer aid;
	/** 测试次数 */
	private Integer several;
	/** 测试次数 */
	private String remark;
	/** 总收益 */
	private Double totalMoney;
	/** 总收益 */
	private Double coinsMoney;
	/** 总在线收益 */
	private Double totalOnlineEarn;
	/** 今日在线收益 */
	private Double nowOnlineEarn;
	/** 总投币收益 */
	private Integer totalCoinsEarn;
	/** 今日投币收益 */
	private Integer nowCoinsEarn;

	private String username;

	private String phoneNum;

	private Integer numPerPage;

	private Integer startIndex;

	private String name;

	private String address;

	private Integer manid;

	/** 使用 */
	private int usenum;

	/** 空闲 */
	private int freenum;

	/** 故障 */
	private int failnum;

	/** 到期时间 */
	private Date expirationTime;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}

	public String getCcid() {
		return ccid;
	}

	public void setCcid(String ccid) {
		this.ccid = ccid;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei == null ? null : imei.trim();
	}

	public String getMainid() {
		return mainid;
	}

	public void setMainid(String mainid) {
		this.mainid = mainid;
	}

	public String getCsq() {
		return csq;
	}

	public void setCsq(String csq) {
		this.csq = csq;
	}

	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr == null ? null : ipaddr.trim();
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
		this.hardversion = hardversion;
	}

	public String getHardversionnum() {
		return hardversionnum;
	}

	public void setHardversionnum(String hardversionnum) {
		this.hardversionnum = hardversionnum;
	}

	public String getSoftversionnum() {
		return softversionnum;
	}

	public void setSoftversionnum(String softversionnum) {
		this.softversionnum = softversionnum;
	}

	public String getMainType() {
		return mainType;
	}

	public void setMainType(String mainType) {
		this.mainType = mainType;
	}

	public String getMainHardver() {
		return mainHardver;
	}

	public void setMainHardver(String mainHardver) {
		this.mainHardver = mainHardver;
	}

	public String getMainSoftver() {
		return mainSoftver;
	}

	public void setMainSoftver(String mainSoftver) {
		this.mainSoftver = mainSoftver;
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

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public Date getLiveTime() {
		return liveTime;
	}

	public void setLiveTime(Date liveTime) {
		this.liveTime = liveTime;
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

	public String getTempname() {
		return tempname;
	}

	public void setTempname(String tempname) {
		this.tempname = tempname;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getSeveral() {
		return several;
	}

	public void setSeveral(Integer several) {
		this.several = several;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Double getCoinsMoney() {
		return coinsMoney;
	}

	public void setCoinsMoney(Double coinsMoney) {
		this.coinsMoney = coinsMoney;
	}

	public Double getTotalOnlineEarn() {
		return totalOnlineEarn;
	}

	public void setTotalOnlineEarn(Double totalOnlineEarn) {
		this.totalOnlineEarn = totalOnlineEarn;
	}

	public Double getNowOnlineEarn() {
		return nowOnlineEarn;
	}

	public void setNowOnlineEarn(Double nowOnlineEarn) {
		this.nowOnlineEarn = nowOnlineEarn;
	}

	public Integer getTotalCoinsEarn() {
		return totalCoinsEarn;
	}

	public void setTotalCoinsEarn(Integer totalCoinsEarn) {
		this.totalCoinsEarn = totalCoinsEarn;
	}

	public Integer getNowCoinsEarn() {
		return nowCoinsEarn;
	}

	public void setNowCoinsEarn(Integer nowCoinsEarn) {
		this.nowCoinsEarn = nowCoinsEarn;
	}

	public Integer getManid() {
		return manid;
	}

	public void setManid(Integer manid) {
		this.manid = manid;
	}

	public int getUsenum() {
		return usenum;
	}

	public void setUsenum(int usenum) {
		this.usenum = usenum;
	}

	public int getFreenum() {
		return freenum;
	}

	public void setFreenum(int freenum) {
		this.freenum = freenum;
	}

	public int getFailnum() {
		return failnum;
	}

	public void setFailnum(int failnum) {
		this.failnum = failnum;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	@Override
	public String toString() {
		return "Equipment [code=" + code + ", ccid=" + ccid + ", imei=" + imei + ", csq=" + csq + ", ipaddr=" + ipaddr
				+ ", bindtype=" + bindtype + ", state=" + state + ", hardversion=" + hardversion + ", hardversionnum="
				+ hardversionnum + ", softversionnum=" + softversionnum + ", location=" + location + ", lon=" + lon
				+ ", lat=" + lat + ", createTime=" + createTime + ", registTime=" + registTime + ", tempid=" + tempid
				+ ", liveTime=" + liveTime + ", deviceType=" + deviceType + ", deviceIdIos=" + deviceIdIos
				+ ", deviceIdAndroid=" + deviceIdAndroid + ", tempname=" + tempname + ", aid=" + aid + ", several="
				+ several + ", remark=" + remark + ", totalMoney=" + totalMoney + ", coinsMoney=" + coinsMoney
				+ ", totalOnlineEarn=" + totalOnlineEarn + ", nowOnlineEarn=" + nowOnlineEarn + ", totalCoinsEarn="
				+ totalCoinsEarn + ", nowCoinsEarn=" + nowCoinsEarn + ", username=" + username + ", phoneNum="
				+ phoneNum + ", numPerPage=" + numPerPage + ", startIndex=" + startIndex + ", name=" + name
				+ ", address=" + address + ", manid=" + manid + ", usenum=" + usenum + ", freenum=" + freenum
				+ ", failnum=" + failnum + ", expirationTime=" + expirationTime + "]";
	}

	

}
