package com.hedong.hedongwx.service;

import java.util.List;

import com.hedong.hedongwx.entity.UserBankcard;

public interface UserBankcardService {

	/**
	 * 添加银行卡
	 * @param userBankcard
	 * @return
	 */
	int addUserBankcard(UserBankcard userBankcard);
	
	/**
	 * 修改银行卡
	 * @param userBankcard
	 * @return
	 */
	int updateUserBankcard(UserBankcard userBankcard);
	
	/**
	 * 通过用户id查询拥有的银行卡
	 * @param userId
	 * @return
	 */
	List<UserBankcard> selectUserBankcardByUserid(Integer userId,Integer type);
	
	/**
	 * 通过id查询银行卡
	 * @param userId
	 * @return
	 */
	UserBankcard selectUserBankcardByid(Integer id);
	
	/**
	 * 通过id删除银行卡
	 * @param userId
	 * @return
	 */
	int deleteUserBankcardByid(Integer id);
}
