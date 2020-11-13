package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import com.hedong.hedongwx.entity.District;

public interface DistrictDao {

	List<Map<String, Object>> selectDistrict();
	
	List<District> selectDistrictByParam(District district);
}