package com.hedong.hedongwx.dao;

import com.hedong.hedongwx.entity.Idcard;

/**
*author：RoarWolf
*createtime：2020年11月12日
*/

public interface IdcardDao {

	int insertIdcard(Idcard idcard);
	
	int updateIdcard(Idcard idcard);
	
	Integer selectIdcardByUid(Integer uid);
}
