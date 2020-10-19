package com.hedong.hedongwx.entity.systemRole;

import lombok.Data;

/**
 * @Author xuke
 * @Description 权限-用户角色
 * @Date 2020/10/17 14:20
 * @Param
 **/
@Data
public class SysUserRole {

	private static final long serialVersionUID = 1L;



	/** 用户ID **/
	private Long userId;

	/** 角色ID **/
	private Long roleId;

	/** 获取用户ID **/
	public Long getUserId() {
		return userId;
	}

	/** 设置用户ID **/
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/** 获取角色ID **/
	public Long getRoleId() {
		return roleId;
	}

	/** 设置角色ID **/
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

    @Override
    public String toString() {
        return "PerUserRole{" +
                ", userId='" + userId + '\'' +
                ", roleId='" + roleId + '\'' +
                '}';
    }
}
