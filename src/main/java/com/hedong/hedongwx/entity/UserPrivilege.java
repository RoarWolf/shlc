package com.hedong.hedongwx.entity;

/**
 * 用户权限关联
 * @author RoarWolf
 *
 */
public class UserPrivilege {

	/* 子账号id */
	private Integer uid;
	/* 权限id */
	private Integer pid;
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	
}
