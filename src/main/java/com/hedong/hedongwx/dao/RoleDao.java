package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Parameters;

/**
 * @Description： 角色处理
 * @author  origin  创建时间：   2019年8月1日 下午5:03:39  
 */
public interface RoleDao {
	
	Map<String, Object> selectRole(Parameters param);
	
	
/*********************************************************************************************************************/	
	/**
	 * @Description： merid、uid、roleid
	 * @author： origin 创建时间：   2019年7月31日 下午6:10:53 
	 */
	Integer insertUserRole(@Param("merid")Integer merid, @Param("uid")Integer uid, @Param("roleid")Integer roleid);
	
	/**
	 * @Description： 根据id删除
	 * @author： origin 创建时间：   2019年8月2日 下午2:55:20
	 */
	Integer deleteUserRoleById(Integer id);
	
	/**
	 * @Description：  根据参数删除
	 * @author： origin 创建时间：   2019年7月31日 下午6:26:43 
	 */
	Integer deleteUserRolee(@Param("merid")Integer merid, @Param("member")Integer member, @Param("role")Integer role);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月31日 下午6:38:46 
	 */
	Map<String, Object> selectUserRole(Parameters param);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月31日 下午6:38:53 
	 */
	List<Map<String, Object>> selectUserRoleList(Parameters param);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月31日 下午7:18:02 
	 */
	Integer updateUserRoleByParam(@Param("merid")Integer merid, @Param("uid")Integer uid, @Param("role")Integer role);
    
	Integer updateUserRoleById(@Param("id")Integer id, @Param("role")Integer role);
	
}
