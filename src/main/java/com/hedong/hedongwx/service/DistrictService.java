package com.hedong.hedongwx.service;

import java.util.List;
import java.util.Map;

import com.hedong.hedongwx.entity.District;

/**
*author：RoarWolf
*createtime：2020年11月10日
*/

public interface DistrictService {

	Map<String, Object> selectDistrict();
	
	List<District> selectDistrictByParam(District district);
}
