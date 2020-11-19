package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

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
	
	int deleteMeruserByid(@Param("uid") Integer uid);
	
	int updateMerUserEarn(@Param("type") Integer type, @Param("money") Double money, @Param("uid") Integer uid);
	
	Map<String, Object> selectMerCollect(@Param("uid") Integer uid);
	
	List<Map<String, Object>> selectMerCollectList(@Param("uid") Integer uid,@Param("createTime") String createTime);
	
	Map<String, Object> selectMerTotalEarn(@Param("uid") Integer uid,@Param("startTime") String startTime,@Param("endTime") String endTime);
	
	int insertMerCollect(@Param("uid") Integer uid,@Param("charge_earn") Double charge_earn,
			@Param("advertise_earn") Double advertise_earn,@Param("ordertotal") Integer ordertotal,
			@Param("withdraw_earn") Double withdraw_earn);
	
	List<Integer> selectNormalMeruser();
}
