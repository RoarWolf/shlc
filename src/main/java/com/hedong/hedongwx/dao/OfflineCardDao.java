package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.OfflineCard;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.entity.Parameters;

public interface OfflineCardDao {

	/** 添加查询离线卡记录*/
	int insertOfflineCard(OfflineCard offlineCard);
	
	/** 修改查询离线卡记录*/
	int updateQuery(OfflineCard offlineCard);
	
	/** 查询离线卡记录*/
	OfflineCard selectOfflineCardByOrdernum(String ordernum);
	
	/** 删除未支付订单通过订单号*/
	int deleteOfflineCardByOrdernum(String ordernum);

	List<OfflineCard> selectOrderInfo(OfflineCard offlineCard);
	/** 根据离线卡更改状态*/
	int offlineCardRefund(@Param("paytype")Integer paytype,@Param("id")Integer id);
	
	OfflineCard selectOfflineCardById(Integer id);
	
	Double getOfflineTodayMoneyByUserid(@Param("begintime") String begintime,
			@Param("endtime") String endtime, @Param("equipmentnum") String equipmentnum,
			@Param("merchantid") Integer merchantid,@Param("ordernum") String ordernum);
	
	List<OfflineCard> getOfflineByParma(@Param("begintime") String begintime,
			@Param("endtime") String endtime, @Param("equipmentnum") String equipmentnum,
			@Param("merchantid") Integer merchantid,@Param("ordernum") String ordernum);

	Map<String, Object> selecttotaloff(Parameters parameter);

	Map<String, Object> selecttotaloffrefund(Parameters parameter);
	
	Double selectTotalMoneyByMeridAndEqunum(@Param("equipmentnum") String equipmentnum,
			@Param("merchantid") Integer merchantid);

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	List<Map<String, Object>> selectOfflineRecord(Parameters parameters);

	/**
	 * @Description： 离线卡汇总所需信息
	 * @author： origin 
	 */
	Map<String, Object> offlinecardtotal(Parameters parameter);

	/**
	 * @Description：
	 * @param cardID
	 * @author： origin  2020年6月5日下午5:33:28
	 */
	OfflineCard gainOfflineData(@Param("cardID")String cardID);
	
}
