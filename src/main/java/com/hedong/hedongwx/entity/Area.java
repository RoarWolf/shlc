package com.hedong.hedongwx.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Area {

	/** 表自增id */
	private Integer id;
	/** 小区名 */
	private String name;
	/** 钱包费用模板id */
	private Integer tempid;
	/** 在线卡费用模板id */
	private Integer tempid2;
	/** 商户id */
	private Integer merid;
	/** 管理者id */
	private Integer manid;
	/** 分成比 合伙人/总收益*/
	private Double divideinto;
	/** 具体位置 */
	private String address;
	/** 经度 */
	private BigDecimal lon;
	/** 纬度 */
	private BigDecimal lat;
	/** 创建时间 */
	private Date createTime;
	/** 修改时间 */
	private Date updateTime;
	/** 总在线收益*/
    private Double totalOnlineEarn;
    /** 今日在线收益*/
    private Double nowOnlineEarn;
    /** 总投币收益*/
    private Integer totalCoinsEarn;
    /** 今日投币收益*/
    private Integer nowCoinsEarn;
    /** 钱包充值收益 */
	private Double walletEarn;
	/** 设备收益 */
	private Double equEarn;
	/** 卡充值收益 */
	private Double cardEarn;
	
	//商户名
	private String dealer;
	//商户电话
	private String uphonenum;

	//商户名
	private String manarealname;
	//商户电话
	private String manaphonenum;
	//模板名
	private String temname;
	//设备数
	private String equcount;
	//已到期设备数
	private Integer expiredEquNum;
	//快到期设备数(小于15天)
	private Integer almostExEquNum;
	
	//距离（km）
	private BigDecimal distance;
	//空闲端口数
	private Integer freenum;

	/**站点唯一编号**/
	private String  areaOnlyCode;

	public String getAreaOnlyCode() {
		return areaOnlyCode;
	}
	//直流空闲端口数
	private Integer DCfreenum;
	//直流端口总数
	private Integer DCAllnum;
	//直流电能费
	private Double DCchargeMoney;
	//直流服务费
	private Double DCserverMoney;
	//交流空闲端口数
	private Integer exfreenum;
	//交流端口总数
	private Integer exAllnum;
	//交流电能费
	private Double exchargeMoney;
	//交流服务费
	private Double exserverMoney;

	/**站点唯一编号**/
	private String  areaOnlyCode;

	public String getAreaOnlyCode() {
		return areaOnlyCode;
	}

	public void setAreaOnlyCode(String areaOnlyCode) {
		this.areaOnlyCode = areaOnlyCode;
	}

	public Integer getExpiredEquNum() {
		return expiredEquNum;
	}

	public void setExpiredEquNum(Integer expiredEquNum) {
		this.expiredEquNum = expiredEquNum;
	}

	public Integer getAlmostExEquNum() {
		return almostExEquNum;
	}

	public void setAlmostExEquNum(Integer almostExEquNum) {
		this.almostExEquNum = almostExEquNum;
	}

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

	public Integer getTempid() {
		return tempid;
	}

	public void setTempid(Integer tempid) {
		this.tempid = tempid;
	}

	public Integer getTempid2() {
		return tempid2;
	}

	public void setTempid2(Integer tempid2) {
		this.tempid2 = tempid2;
	}

	public Integer getMerid() {
		return merid;
	}

	public void setMerid(Integer merid) {
		this.merid = merid;
	}

	public Integer getManid() {
		return manid;
	}

	public void setManid(Integer manid) {
		this.manid = manid;
	}

	public Double getDivideinto() {
		return divideinto;
	}

	public void setDivideinto(Double divideinto) {
		this.divideinto = divideinto;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	public Double getWalletEarn() {
		return walletEarn;
	}

	public void setWalletEarn(Double walletEarn) {
		this.walletEarn = walletEarn;
	}

	public Double getEquEarn() {
		return equEarn;
	}

	public void setEquEarn(Double equEarn) {
		this.equEarn = equEarn;
	}

	public Double getCardEarn() {
		return cardEarn;
	}

	public void setCardEarn(Double cardEarn) {
		this.cardEarn = cardEarn;
	}

	public String getDealer() {
		return dealer;
	}

	public void setDealer(String dealer) {
		this.dealer = dealer;
	}

	public String getUphonenum() {
		return uphonenum;
	}

	public void setUphonenum(String uphonenum) {
		this.uphonenum = uphonenum;
	}

	public String getManarealname() {
		return manarealname;
	}

	public void setManarealname(String manarealname) {
		this.manarealname = manarealname;
	}

	public String getManaphonenum() {
		return manaphonenum;
	}

	public void setManaphonenum(String manaphonenum) {
		this.manaphonenum = manaphonenum;
	}

	public String getTemname() {
		return temname;
	}

	public void setTemname(String temname) {
		this.temname = temname;
	}

	public String getEqucount() {
		return equcount;
	}

	public void setEqucount(String equcount) {
		this.equcount = equcount;
	}

	public BigDecimal getDistance() {
		return distance;
	}

	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	public Integer getDCfreenum() {
		return DCfreenum;
	}

	public void setDCfreenum(Integer dCfreenum) {
		DCfreenum = dCfreenum;
	}

	public Integer getDCAllnum() {
		return DCAllnum;
	}

	public void setDCAllnum(Integer dCAllnum) {
		DCAllnum = dCAllnum;
	}

	public Integer getExfreenum() {
		return exfreenum;
	}

	public void setExfreenum(Integer exfreenum) {
		this.exfreenum = exfreenum;
	}

	public Integer getExAllnum() {
		return exAllnum;
	}

	public void setExAllnum(Integer exAllnum) {
		this.exAllnum = exAllnum;
	}

	public Double getDCchargeMoney() {
		return DCchargeMoney;
	}

	public void setDCchargeMoney(Double dCchargeMoney) {
		DCchargeMoney = dCchargeMoney;
	}

	public Double getDCserverMoney() {
		return DCserverMoney;
	}

	public void setDCserverMoney(Double dCserverMoney) {
		DCserverMoney = dCserverMoney;
	}

	public Double getExchargeMoney() {
		return exchargeMoney;
	}

	public void setExchargeMoney(Double exchargeMoney) {
		this.exchargeMoney = exchargeMoney;
	}

	public Double getExserverMoney() {
		return exserverMoney;
	}

	public void setExserverMoney(Double exserverMoney) {
		this.exserverMoney = exserverMoney;
	}

	@Override
	public String toString() {
		return "Area [id=" + id + ", name=" + name + ", tempid=" + tempid + ", tempid2=" + tempid2 + ", merid=" + merid
				+ ", manid=" + manid + ", divideinto=" + divideinto + ", address=" + address + ", lon=" + lon + ", lat="
				+ lat + ", createTime=" + createTime + ", updateTime=" + updateTime + ", totalOnlineEarn="
				+ totalOnlineEarn + ", nowOnlineEarn=" + nowOnlineEarn + ", totalCoinsEarn=" + totalCoinsEarn
				+ ", nowCoinsEarn=" + nowCoinsEarn + ", walletEarn=" + walletEarn + ", equEarn=" + equEarn
				+ ", cardEarn=" + cardEarn + ", dealer=" + dealer + ", uphonenum=" + uphonenum + ", manarealname="
				+ manarealname + ", manaphonenum=" + manaphonenum + ", temname=" + temname + ", equcount=" + equcount
				+ ", expiredEquNum=" + expiredEquNum + ", almostExEquNum=" + almostExEquNum + ", distance=" + distance
				+ ", DCfreenum=" + DCfreenum + ", DCAllnum=" + DCAllnum + "]";
	}
}
