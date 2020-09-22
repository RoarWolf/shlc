package com.hedong.hedongwx.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.InCoins;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.utils.PageUtils;

public interface InCoinsService {

	int insertInCoinsRecord(String ordernum,String code,Byte port,Integer uid,
			Double money,Byte coinNum,Byte handletype, Byte status);
	
	int updateInCoinsStatusAndRecycletype(String ordernum,Byte status);
	
	InCoins selectInCoinsRecordByOrdernum(String ordernum);

	PageUtils<Parameter> selectCoinsParame(HttpServletRequest request);

	int updateInCoinsStatus(String ordernum, Byte status);
	
	Double getInCoinsTodayMoneyByUserid(String begintime, String endtime,
			String equipmentnum, Integer merchantid, String ordernum);
	
	List<InCoins> getInCoinsByParam(String begintime, String endtime,
			String equipmentnum, Integer merchantid, String ordernum);
	
	InCoins selectInCoinsRecordById(Integer id);
	
	/**
	 * 查询未回复的扫码投币订单
	 * @return
	 */
	List<InCoins> selectInCoinsNoReply();

	Map<String, Object> selectsuminfo();

	int updateByPrimaryKeySelective(InCoins inCoins);
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	PageUtils<Parameters> selectIncoinsRecord(HttpServletRequest request);

	// @Description： 查询脉冲设备投币总额
	Map<String, Object> selectcodesuminfo(Integer uid, String code, String startTime, String endTime);

	//@Description： 根据条件查询投币订单
	List<Map<String, Object>> selectcoinsRecord(Integer id, String parameter, String startTime, String dateTime);
	/**
	 * @Description：  根据脉冲设备汇总线下投币上传信息
	 * @author： origin  
	 */
	Map<String, Object> selectcoinsup(Parameters parameters);
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/

	//============================================================================================
	/**
	 * separate
	 * @Description：投币记录信息查看
	 * @author： origin 
	 */
	Object inCoinsRecordData(HttpServletRequest request);
	
	//============================================================================================
	/**
	 * 银联投币退款
	 * @param incoins
	 */
	void inCoinsRefund(InCoins incoins);
}
