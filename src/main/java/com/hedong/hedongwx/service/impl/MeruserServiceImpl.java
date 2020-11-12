package com.hedong.hedongwx.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hedong.hedongwx.dao.MeruserDao;
import com.hedong.hedongwx.entity.Meruser;
import com.hedong.hedongwx.service.MeruserService;
import com.hedong.hedongwx.utils.CommUtil;

/**
*author：RoarWolf
*createtime：2020年11月12日
*/

@Service
public class MeruserServiceImpl implements MeruserService {
	
	@Autowired
	private MeruserDao meruserDao;

	@Transactional
	@Override
	public Map<String,Object> insertMeruser(Meruser meruser) {
		meruser.setCreateTime(new Date());
		int insertMeruser = meruserDao.insertMeruser(meruser);
		Map<String,Object> map = new HashMap<>();
		Integer merid = meruserDao.selectMeruserByPhonenum(meruser.getPhonenum());
		map.put("merid", merid);
		return CommUtil.responseBuildInfo(200, "添加成功", map);
	}

	@Override
	public Map<String,Object> updateMeruser(Meruser meruser) {
		int updateMeruser = meruserDao.updateMeruser(meruser);
		return CommUtil.responseBuildInfo(200, "修改成功", null);
	}

	@Override
	public Map<String,Object> selectMeruserList(Meruser meruser) {
		if (meruser.getStartindex() == null || meruser.getStartindex() <= 0) {
			meruser.setStartindex(0);
		} else {
			meruser.setStartindex(meruser.getStartindex() * 10);
		}
		List<Meruser> selectMeruserList = meruserDao.selectMeruserList(meruser);
		Map<String,Object> map = new HashMap<>();
		map.put("meruserlist", selectMeruserList);
		map.put("totalnum", meruserDao.selectTotalnum(meruser));
		return CommUtil.responseBuildInfo(200, "查询成功", map);
	}

}
