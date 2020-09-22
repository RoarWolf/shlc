package com.hedong.hedongwx.entity;

import java.util.Date;

/**
 * @author  origin
 * 创建时间：   2019年5月25日 上午9:56:02  
 */
public class Operaterecord {
	
	/** 操作表自增id */
	private Integer id;
	
	/** 操作内容（名字） */
	private String name;
	
	/** 操作人id 默认为0为系统执行  */
	private Integer opeid;
	
	/** 操作对象id */
	private Integer objid;
	
	/** 类型 */
	private Integer type;
	
	/**  */
	private Integer source;
	
	/** 备注 */
	private String remark;
	
	/** 备用字段 */
	private String common;
	
	/** 操作时间 */
	private Date operateTime;

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

	public Integer getOpeid() {
		return opeid;
	}

	public void setOpeid(Integer opeid) {
		this.opeid = opeid;
	}

	public Integer getObjid() {
		return objid;
	}

	public void setObjid(Integer objid) {
		this.objid = objid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCommon() {
		return common;
	}

	public void setCommon(String common) {
		this.common = common;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	
	
	
	
	
	
}
