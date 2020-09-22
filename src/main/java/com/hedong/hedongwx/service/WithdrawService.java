package com.hedong.hedongwx.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.Withdraw;
import com.hedong.hedongwx.utils.PageUtils;

public interface WithdrawService {

	/**
	 * 添加订单
	 * @param userBankcard
	 * @return
	 */
	int addWithdraw(Withdraw withdraw);
	
	/**
	 * 修改修改订单状态
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
	 * 通过ID获取指定对象的详细信息
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
	PageUtils<Withdraw> getAllWithdrawAndBankcard(HttpServletRequest request);

	/**
	 * 通过用用户id获取最后一条记录
	 * @param userId
	 * @return
	 */
	Withdraw getEndRecordByUserId(Integer userId);
	
	/**
	 * @Description： 查询提现总和与手续费总和
	 * @author： origin          
	 * @param withdraw
	 */
	Map<String, Object> selectmoneysum( Withdraw withdraw);
	
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
	Double selectAllMoneyByUidAndTime(Integer uid,String begintime,String endtime);

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询提现记录信息
	PageUtils<Parameters> selectWithdrawRecord(HttpServletRequest request);

	/**
	 * 查询出提现金额（包含以提现的和待提现的）
	 * @author  origin          
	 * @version 创建时间：2019年3月1日  下午3:17:37
	 * @param id
	 * @return
	 */
	Map<String, Object> selectwitmoney(Integer id);

	/**
	 * @Description： 
	 * @author： origin 查询待处理金额
	 */
	Map<String, Object> pendingmoney(Integer merid, String status, String createTime, String accountTime);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月10日 下午6:58:02 
	 */
	Object withdrawDispose(Integer id, Integer status);

	
	//=============================================================================
	/**
	 * separate
	 * @Description：提现记录信息查看
	 * @author： origin 
	 */
	Object withdrawRecordData(HttpServletRequest request);

	/**
	 * separate
	 * @Description：  提现判断
	 * @author： origin   2019年11月15日 下午6:00:58
	 */
	Object withdrawResolve(HttpServletRequest request);
	
	/**
	 * @Description:商家收益自动提现
	 * @author： ZZ
	 * 创建时间：   2020年6月9日 上午9:89:89(^_^)    
	 */
	void merAutoWithdraw();
	
	/**
	 * 提现到个人银行卡
	 * @param bankcardnum 银行卡号
	 * @param withDrawNum 提现的单号
	 * @author： ZZ
	 * 创建时间：   2020年6月20日 上午10:01:01(^_^)    
	 * @return {@link Object}
	 */
	Object withdrawPersonBank(String bankcardnum, String withDrawNum);
	
}
