package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.OnlineCardRecord;
import com.hedong.hedongwx.entity.Parameters;

public interface OnlineCardRecordDao {

	List<OnlineCardRecord> selectOnlineCardRecordListByCardID(String cardID);
	
	int insertOnlineCardRecord(OnlineCardRecord onlineCardRecord);

	OnlineCardRecord selectRecordByOrdernum(@Param("ordernum")String ordernum, @Param("type")Integer type, @Param("flag")Integer flag);
	
	int updateRecord(OnlineCardRecord onlineCardRecord);
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询在线卡操作记录信息
	List<Map<String, Object>> selectOnlineOperation(Parameters parameters);
	
	//查询在线卡消费记录信息
	List<Map<String, Object>> selectOonlineConsume(Parameters parameters);

	/**
	 * @Description： 根据订单id查询
	 * @author： origin
	 */
	OnlineCardRecord selectOnlineCard(Integer id);

	/**
	 * @Description： 根据实体类查询
	 * @author： origin 
	 */
	List<OnlineCardRecord> selectCardRecord(OnlineCardRecord onlin);

	/**
	 * @Description： 在线卡汇总金额
	 * @author： origin   
	 */
	Map<String, Object> onlinecardcollect(Parameters parameter);

	/**
	 * @Description： 在线卡汇总所需信息
	 * @author： origin
	 */
	Map<String, Object> onlinecardtotal(Parameters parameter);

	/**
	 * @Description：  根据订单号更改状态
	 * @author： origin          
	 * 创建时间：   2019年6月15日 下午12:00:37 
	 */
	Integer updateonlinestatus(@Param("ordernum")String ordernum, @Param("status")Byte status);
	
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	
	
}
