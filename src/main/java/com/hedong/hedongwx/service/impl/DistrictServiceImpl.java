package com.hedong.hedongwx.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hedong.hedongwx.dao.DistrictDao;
import com.hedong.hedongwx.service.DistrictService;
import com.hedong.hedongwx.utils.CommUtil;

/**
*author：RoarWolf
*createtime：2020年11月10日
*/

@Service
public class DistrictServiceImpl implements DistrictService {
	
	@Resource
	private DistrictDao districtDao;

	@Override
	public Map<String, Object> selectDistrict() {
		Map<String,Object> hashMap = new HashMap<>();
		hashMap.put("districtinfo", districtDao.selectDistrict());
		return CommUtil.responseBuildInfo(1000, "获取成功", hashMap);
	}

}
