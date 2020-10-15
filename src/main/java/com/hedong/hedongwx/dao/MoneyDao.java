package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Money;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.entity.Parameters;

public interface MoneyDao {

	List<Money> MoneyRecordByid(@Param("uid")Integer uid);

	Money payMoneyinfo(@Param("id") Integer id);

	int payMoneys(Money money);

//	int insertWalletRecord(Money money);
//	int insertWalletData(Money money);

	int updateMoney(Money money);
	
	Money queryMoneyByOrdernum(String ordernum);

	Map<String, Object> selectmoneytotal(Parameters parameter);
	
	Map<String, Object> selectmoneytotalrefund(Parameters parameter);

	List<Map<String, Object>> selectwalletinfo(Parameter parameter);

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	List<Map<String, Object>> selectWalletRecord(Parameters parameters);

	int selectWalletRecordTotal(Parameters parameters);

	/**
	 * @Description：  根据订单号更改状态
	 * @author： origin  
	 */
	int updateMoneyByOrder(@Param("ordernum")String ordernum, @Param("paytype")Integer paytype);

	/**
	 * @Description： 钱包汇总所需信息
	 * @author： origin 
	 */
	Map<String, Object> moneytotal(Parameters parameter);

	/**
	 * 根据钱包充值的订单号查询商家的id
	 * @param moneyOrder
	 * @return {@link Integer}
	 */
	Integer selectMerByMoneyOrdernum(@Param("moneyOrder")String moneyOrder);

	/*-- *** 请暂时不要在*号范围内添加   thanks ***********************************************************   --*/
	/**
	 * @method_name: insertWalletRecord
	 * @Description: 根据实体类插入钱包记录信息
	 * @param money 表hd_moneyrecord的实体类数据
	 * @return
	 * @Author: origin  创建时间:2020年8月17日 下午4:54:30
	 */
	Integer insertWalletRecord(Money money);
	/*-- ****************************************************************************************  --*/
	
}
