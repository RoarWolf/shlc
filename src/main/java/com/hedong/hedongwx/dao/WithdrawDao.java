package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.Withdraw;

public interface WithdrawDao {

	/**
	 * 添加提现记录
	 * @param userBankcard
	 * @return
	 */
	int addWithdraw(Withdraw withdraw);
	
	/**
	 * 修改提现记录状态
	 * @param userBankcard
	 * @return
	 */
	int updateWithdraw(Withdraw withdraw);
	
	/**
	 * 通过userid获取提现记录
	 * @param userid
	 * @return
	 */
	List<Withdraw> getWithdrawListByUserId(Integer userId);
	
	/**
	 * 通过id获取提现记录详情
	 * @param id
	 * @return
	 */
	Withdraw selectExtractInfo(Integer id);
	
	/**
	 * 获取所有提现记录
	 * @return
	 */
	List<Withdraw> getWithdrawList();
	
	/**
	 * 通过银行卡号获取提现记录
	 * @return
	 */
	List<Withdraw> getWithdrawListByBankcardnum(String bankcardnum);
	
	/**
	 * 获取所有提现记录与银行卡关联
	 * @return
	 */
	List<Withdraw> getAllWithdrawAndBankcard(Withdraw withdraw);
	
	/**
	 * 通过用用户id获取最后一条记录
	 * @param userId
	 * @return
	 */
	Withdraw getEndRecordByUserId(Integer userId);
	
	Map<String, Object> selectmoneysum(Withdraw withdraw);
	
	/**
	 * 获取个人提现总额
	 * @param integer
	 * @return
	 */
	Map<String, Object> selectwitmoney(@Param("userId")Integer id);
	
	/**
	 * 通过提现单号查询记录
	 * @param ordernum
	 * @return
	 */
	Withdraw selectWithdrawByOrdernum(String ordernum);
	
	/**
	 * 通过时间和用户id查询提现总额
	 * @param uid
	 * @param begintime
	 * @param endtime
	 * @return
	 */
	Double selectAllMoneyByUidAndTime(@Param("uid")Integer uid,@Param("begintime")String begintime,@Param("endtime")String endtime);

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	List<Map<String, Object>> selectWithdrawRecord(Parameters parameters);

	/**
	 * @Description： 查询待提现金额
	 * @author： origin          
	 * @param withdraw
	 */
	Map<String, Object> pendingmoney(@Param("userId")Integer merid, @Param("status")String status, 
			@Param("createTime")String createTime, @Param("accountTime")String accountTime);
	
	/**
	 * 根据银行卡号和订单号查询待确认的提现账单
	 * @author： ZZ 
	 * @param bankcardNum 银行卡号
	 * @param withdrawNum 提现的订单
	 * @return {@link Map}
	 */
	Map<String, Object> selectWithdrawByBankId(@Param("bankcardNum")String bankcardNum, @Param("withdrawNum")String withdrawNum);
	

	
}
