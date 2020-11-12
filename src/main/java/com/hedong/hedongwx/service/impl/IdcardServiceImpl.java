package com.hedong.hedongwx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.dao.IdcardDao;
import com.hedong.hedongwx.entity.Idcard;
import com.hedong.hedongwx.service.IdcardService;

/**
*author：RoarWolf
*createtime：2020年11月12日
*/

@Service
public class IdcardServiceImpl implements IdcardService {
	
	@Autowired
	private IdcardDao idcardDao;

	@Override
	public int insertIdcard(Idcard idcard) {
		return idcardDao.insertIdcard(idcard);
	}

	@Override
	public int updateIdcard(Idcard idcard) {
		return idcardDao.updateIdcard(idcard);
	}

	@Override
	public Integer selectIdcardByUid(Integer uid) {
		return idcardDao.selectIdcardByUid(uid);
	}

}
