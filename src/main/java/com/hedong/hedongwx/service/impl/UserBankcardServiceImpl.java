package com.hedong.hedongwx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.dao.UserBankcardDao;
import com.hedong.hedongwx.entity.UserBankcard;
import com.hedong.hedongwx.service.UserBankcardService;

@Service
public class UserBankcardServiceImpl implements UserBankcardService {
	
	@Autowired
	private UserBankcardDao userBankcardDao;

	@Override
	public int addUserBankcard(UserBankcard userBankcard) {
		return userBankcardDao.addUserBankcard(userBankcard);
	}

	@Override
	public int updateUserBankcard(UserBankcard userBankcard) {
		return userBankcardDao.updateUserBankcard(userBankcard);
	}

	@Override
	public List<UserBankcard> selectUserBankcardByUserid(Integer userId,Integer type) {
		return userBankcardDao.selectUserBankcardByUserid(userId,type);
	}

	@Override
	public UserBankcard selectUserBankcardByid(Integer id) {
		return userBankcardDao.selectUserBankcardByid(id);
	}

	@Override
	public int deleteUserBankcardByid(Integer id) {
		return userBankcardDao.deleteUserBankcardByid(id);
	}

}
