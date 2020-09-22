package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.InCoins;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.entity.Parameters;

public interface InCoinsDao {

	int insertInCoinsRecord(InCoins inCoins);
	
	int updateInCoinsStatusAndRecycletype(@Param("ordernum")String ordernum,@Param("status")Byte status);
	
	InCoins selectInCoinsRecordByOrdernum(String ordernum);

	List<Map<String, Object>> selectCoinsParame(Parameter parameter);

	int updateInCoinsStatus(@Param("ordernum")String ordernum, @Param("handletype")Byte handletype);
	
	Double getInCoinsTodayMoneyByUserid(@Param("begintime") String begintime,
			@Param("endtime") String endtime, @Param("equipmentnum") String equipmentnum,
			@Param("merchantid") Integer merchantid,@Param("ordernum") String ordernum);
	
	List<InCoins> getInCoinsByParam(@Param("begintime") String begintime,
			@Param("endtime") String endtime, @Param("equipmentnum") String equipmentnum,
			@Param("merchantid") Integer merchantid,@Param("ordernum") String ordernum);

	InCoins selectInCoinsRecordById(Integer id);
	
	List<InCoins> selectInCoinsNoReply();

	Map<String, Object> selectsuminfo(Parameters parameter);

	Map<String, Object> selectsuminforefund(Parameters parameter);
	
	int updateByPrimaryKeySelective(InCoins inCoins);
	
	Double selectTotalMoneyByEqunumAndMerid(@Param("merchantid") Integer merchantid,
			@Param("equipmentnum") String equipmentnum,@Param("type") Integer type);

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	/**
	 * @author zz
	 * 查询商户信息总条数
	 * @param parameters 参数
	 * @return {@link Integer}
	 */
	Integer selectIncoinsRecordCount(Parameters parameters);
	
	/**
	 * @author:origin
	 * 查询商户信息
	 * @param parameters 参数
	 * @return {@link List}
	 */
	List<Map<String, Object>> selectIncoinsRecord(Parameters parameters);

	/**
	 * @Description： 根据条件查询所有的不同设备
	 * @author： origin
	 */
	List<Map<String, Object>> distinctcoins(Parameters parameters);

	/**
	 * @Description： 根据脉冲设备汇总线下投币上传信息
	 */
	Map<String, Object> selectcoinsup(Parameters parameters);

	/**
	 * @Description： 脉冲汇总所需信息
	 * @author： origin
	 */
	Map<String, Object> incoinstatol(Parameters parameter);

}
