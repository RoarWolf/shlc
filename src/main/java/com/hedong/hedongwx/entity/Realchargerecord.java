package com.hedong.hedongwx.entity;

import java.util.Date;

/**
 * @author  origin
 * 创建时间：   2019年4月16日 上午9:50:52  
 */
public class Realchargerecord {
	
	/**用户实时数据信息表*/
	private Integer id;
	
	/**用户id*/
	private Integer uid;

	/**商户id*/
	private Integer merid;
	
	/**关联充电记录对应数据的Id*/
	private Integer chargeid;
	
	/**设备号*/
	private String code; 
	
	/**设备端口号*/
	private Integer port;
			  
	/**状态*/
	private Integer type;
	
	/**充电时间*/
	private Integer chargetime;
	
	/**剩余时间*/
	private Integer surpluselec;

	/**实时功率*/
	private Integer power;
	
	/**实时功率*/
	private Double portV;
	
	/**实时功率*/
	private Double portA;
	
	/**实时扣费金额*/
	private Double money;

	/**创建时间*/
	private Date createtime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getMerid() {
		return merid;
	}

	public void setMerid(Integer merid) {
		this.merid = merid;
	}

	public Integer getChargeid() {
		return chargeid;
	}

	public void setChargeid(Integer chargeid) {
		this.chargeid = chargeid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getChargetime() {
		return chargetime;
	}

	public void setChargetime(Integer chargetime) {
		this.chargetime = chargetime;
	}

	public Integer getSurpluselec() {
		return surpluselec;
	}

	public void setSurpluselec(Integer surpluselec) {
		this.surpluselec = surpluselec;
	}

	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}

	public Double getPortV() {
		return portV;
	}

	public void setPortV(Double portV) {
		this.portV = portV;
	}

	public Double getPortA() {
		return portA;
	}

	public void setPortA(Double portA) {
		this.portA = portA;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	
	
}
