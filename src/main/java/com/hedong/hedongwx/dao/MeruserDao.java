package com.hedong.hedongwx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Meruser;

/**
*author：RoarWolf
*createtime：2020年11月12日
*/

public interface MeruserDao {

	int insertMeruser(Meruser meruser);
	
	int updateMeruser(Meruser meruser);
	
	List<Meruser> selectMeruserList(Meruser meruser);
	
	int selectTotalnum(Meruser meruser);
	
	Integer selectMeruserByPhonenum(@Param("phonenum") String phonenum);
	
	Meruser selectMeruserByUid(Integer uid);
}
