package com.hedong.hedongwx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Privilege;

public interface PrivilegeDao {

	/**
	 * 根据子账号id查询权限列表
	 * @param uid
	 * @return
	 */
	List<Privilege> selectUserPrivilege(@Param("uid") Integer uid);
	
	/**
	 * 根据权限id查询权限列表
	 * @param uid
	 * @return
	 */
	List<Privilege> selectAllPrivilege(@Param("id") Integer id,@Param("parentId") Integer parentId);
	
	/**
	 * 添加子账号权限
	 * @param uid
	 * @param pid
	 * @return
	 */
	int insertUserPrivliege(@Param("uid") Integer uid,@Param("pid") Integer pid);
	
	/**
	 * 删除子账号权限
	 * @param uid
	 * @param pid
	 * @return
	 */
	int deleteUserPrivliege(@Param("uid") Integer uid,@Param("pid") Integer pid);
}
