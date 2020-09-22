package com.hedong.hedongwx.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.Money;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.utils.PageUtils;

public interface MoneyService {

	//根据用户id（uid）查询充值订单
	List<Money> MoneyRecordByid(Integer uid);
	
	//根据订单id查询充值订单
	Money payMoneyinfo(Integer id);

	Money payMoney(Integer uid,Double paymoney,Double sendmoney,Double accountmoney,Double topupbalance,Double givebalance,String ordernum,Date date,Integer paytype,String remark);

	//根据实体类更新
	int updateMoney(Money money);
	
	//根据订单号查询
	Money queryMoneyByOrdernum(String ordernum);
	
	//根据实体类插入
	int payMoneys(Integer uid,String ordernum,Integer paytype,Integer status, Double paymoney, Double sendmoney,
			Double tomoney, Double balance, Double topupbalance,Double givebalance, String remark);
	
	//查询参数（根据Parameter）
	PageUtils<Parameter> selectwalletinfo(HttpServletRequest request);

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询钱包充值记录（含在线卡）
	PageUtils<Parameters> selectWalletRecord(HttpServletRequest request);
	
	/**
	 * @Description： 根据订单号更改状态
	 * @author： origin   
	 */
	int updateMoneyByOrder(String ordernum, Integer paytype);

	/**
	 * @Description：  商户虚拟充值钱包
	 * @author： origin 创建时间：   2019年7月10日 下午2:26:12 
	 */
	Map<String, Object> mercVirtualMoneyPay(User user, Double paymoney, Double sendmoney, Integer id, String val, Integer status);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月15日 上午10:29:40 
	 */
	Map<String, Object> mercVirtualReturn(Integer id);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月16日 下午3:31:17 
	 */
	List<Map<String, Object>> cardPayRecord(Integer uid, String remark, String beginTime, String dateTime);

	//============================================================================================
	/**
	 * separate
	 * @Description： 虚拟充值钱包操作
	 * @author： origin   2019年11月9日 上午11:47:04
	 */
	Object virtualTopUpWallet(HttpServletRequest request);

	/**
	 * separate
	 * @Description：钱包记录信息查看
	 * @author： origin 
	 */
	Object walletRecordData(HttpServletRequest request);
	
	List<Map<String, Object>> selectWalletRecord(Parameters parame);

	/**
	 * 根据钱包充值的订单号查询商家id
	 * @param moneyOrder
	 * @return
	 */
	Integer selectMerByMoneyOrdernum(String moneyOrder);

	/**
	 * @method_name: insertWalletInfo
	 * @Description: 
	 * @return
	 * @Author: origin  创建时间:2020年8月17日 下午5:15:36
	 * @common:   
	 */
	Integer insertWalletInfo(Integer uid, Integer merid, String ordernum, Integer paysource, Integer paytype,
			Integer status, Double paymoney, Double sendmoney, Double tomoney, Double balance, Double topupbalance,
			Double givebalance, Date paytime, String remark);
	
}
