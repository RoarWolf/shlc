package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.AdminiStrator;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.UserBankcard;

public interface UserBankcardDao {

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
	List<UserBankcard> selectUserBankcardByUserid(@Param("userId")Integer userId,@Param("type")Integer type);
	
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

	/**
	 * @Description： 查询用户银行卡信息
	 * @author： origin          
	 * 创建时间：   2019年7月6日 下午4:54:01 
	 */
	List<Map<String, Object>> selectUserBankinfo(Parameters parame);
	
	Integer checkBankcardExist(@Param("bankcardnum")String bankcardnum);
	
	Integer selectBankcardnumUid(@Param("bankcardnum")String bankcardnum);
}
