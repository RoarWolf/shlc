
package com.hedong.hedongwx.entity;

import java.util.Date;

/**
 * @author  origin
 * 创建时间：   2019年2月25日 下午4:03:29  
 */
public class Codeoperatelog {
	
	private Integer id;//设备操作包id
	
	private String code;//设备号
	
	private Integer sort;//种类：1上下线  2绑定与解绑 3修改设备硬件版本  4小区修改  6转移
	
	private Integer type;//状态：1上线 2离(掉)线    1绑定 2解绑    1修改版本号 2修改模板  1绑定小区 2解绑小区
	
	private Integer merid;//所属商户id
	
	private Integer opeid;//操作人id
	
	private String pushmes;//推送消息（次数）
	
	private Date operateTime;//操作时间
	
	private String remark;//备注

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getMerid() {
		return merid;
	}

	public void setMerid(Integer merid) {
		this.merid = merid;
	}

	public Integer getOpeid() {
		return opeid;
	}

	public void setOpeid(Integer opeid) {
		this.opeid = opeid;
	}

	public String getPushmes() {
		return pushmes;
	}

	public void setPushmes(String pushmes) {
		this.pushmes = pushmes;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}
