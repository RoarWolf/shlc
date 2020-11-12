package com.hedong.hedongwx.service;

import java.util.List;
import java.util.Map;

import com.hedong.hedongwx.entity.Meruser;

/**
*author：RoarWolf
*createtime：2020年11月12日
*/

public interface MeruserService {

	Map<String,Object> insertMeruser(Meruser meruser);
	
	Map<String,Object> updateMeruser(Meruser meruser);
	
	Map<String,Object> selectMeruserList(Meruser meruser);
}
