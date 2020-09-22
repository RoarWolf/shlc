package com.hedong.hedongwx.entity;

import java.util.List;

/**
 * 权限
 * 
 * @author RoarWolf
 *
 */
public class Privilege {

	/* 表自增id */
	private Integer id;
	/* 名字 */
	private String name;
	/* 注册说明 */
	private String explain;
	/* 父菜单id */
	private Integer parentId;
	
	private List<Privilege> chilePrivilege;

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

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	

	public List<Privilege> getChilePrivilege() {
		return chilePrivilege;
	}

	public void setChilePrivilege(List<Privilege> chilePrivilege) {
		this.chilePrivilege = chilePrivilege;
	}

	@Override
	public String toString() {
		return "Privilege [id=" + id + ", name=" + name + ", explain=" + explain + ", parentId=" + parentId
				+ ", chilePrivilege=" + chilePrivilege + "]";
	}


}
