package com.hedong.hedongwx.entity;

import java.util.Date;

/**
 * @Description： 
 * @author  origin  创建时间：   2019年8月30日 下午5:25:24  
 */
public class AreaRelevance {
	
	/** 小区合伙关联信息表  自增id */
	private Integer id;
	/** 小区id */
	private Integer aid;
	private String partName;
	/** 合伙人id */
	private Integer partid;
	/** 分成百分比（两位小数） */
	private Double percent;
	/** 排序、等级 */
	private Integer sort;
	/** 人员所属的类型，如:1:商户、2:合伙人、3:管理人员、4:维护 */
	private Integer type;
	/** 状态 1:正常使用  2:停止使用 */
	private Integer status;
	/** 备注 */
	private String remark;
	/** 创建时间 */
	private Date createtime;
	/** 操作时间 */
	private Date operatetime;
	
	
	
	
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAid() {
		return aid;
	}
	public void setAid(Integer aid) {
		this.aid = aid;
	}
	public Integer getPartid() {
		return partid;
	}
	public void setPartid(Integer partid) {
		this.partid = partid;
	}
	public Double getPercent() {
		return percent;
	}
	public void setPercent(Double percent) {
		this.percent = percent;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getOperatetime() {
		return operatetime;
	}
	public void setOperatetime(Date operatetime) {
		this.operatetime = operatetime;
	}
	
	
	
	
}
