package com.hedong.hedongwx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.DealerAuthority;

/**
 * @author  origin
 * 创建时间：   2019年7月6日 下午2:39:41  
 */
public interface DealerAuthorityDao {
	
	Integer insertAuthority(DealerAuthority authority);
	
	Integer updateAuthority(DealerAuthority authority);
	
	Integer updateMeridAuthority(DealerAuthority author);

	DealerAuthority selectAuthority(@Param("id")Integer id, @Param("merid")Integer merid);
	
	List<DealerAuthority> selectEntiyAuthority(DealerAuthority author);
	
	Integer deleteMeridAuthority(@Param("merid")Integer merid);
	
	Integer deleteAuthority(@Param("id")Integer id);
	
	Integer selectIncoinRefund(Integer merid);

	 
	
	
	
}
