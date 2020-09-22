package com.hedong.hedongwx.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.Parameters;

public interface ChargeRecordDao {

	List<ChargeRecord> chargeRecordByUid(Integer uid);

	ChargeRecord chargeRecordOne(@Param("id") Integer id);

	void insetRecord(ChargeRecord chargeRecord);

	List<ChargeRecord> getOrderByOrdernum(String ordernum);

	void updateByOrdernum(@Param(value = "params") Map<String, Object> params);

	List<ChargeRecord> getChargeRecordListByUseridAndTime(@Param("begintime") String begintime,
			@Param("endtime") String endtime, @Param("merchantid") Integer merchantid);

	List<ChargeRecord> getTodayChargeRecordListByUserid(Integer merchantid);

	List<ChargeRecord> getChargeRecordListByTimeAndUid(@Param("begintime") String begintime,
			@Param("endtime") String endtime, @Param("merchantid") Integer merchantid);

	List<ChargeRecord> getChargeRecordListByTimeAndEquipmentnumMerchantid(@Param("begintime") String begintime,
			@Param("endtime") String endtime, @Param("equipmentnum") String equipmentnum,
			@Param("merchantid") Integer merchantid,@Param("ordernum") String ordernum);

	List<ChargeRecord> getChargeRecordListByEquipmentnum(String equipmentnum);
	
	List<ChargeRecord> queryChargingByUid(Integer uid);
	
	List<ChargeRecord> queryChargedByUid(Integer uid);
	
	Double getChargingTotalMoney(Integer id);
	
	int updateChargeEndtimeById(Integer id);

	List<ChargeRecord> selectOrderInfo(ChargeRecord chargeRecord);
	
	Double getTotalMoneyByTimeAndEquipmentnumMerchantid(@Param("begintime") String begintime,
			@Param("endtime") String endtime, @Param("equipmentnum") String equipmentnum,
			@Param("merchantid") Integer merchantid,@Param("ordernum") String ordernum);
	
	int updateNumberById(@Param("ordernum") String ordernum,@Param("number") Integer number,@Param("durationtime") String durationtime
			,@Param("expenditure") Double expenditure,@Param("refundMoney") Double refundMoney,@Param("endTime") String endTime,
			@Param("refundTime") String refundTime,@Param("id") Integer id);
	
	ChargeRecord chargeRecordOneContinueEnd(Integer id);
	
	ChargeRecord getChargeRecordById(Integer id);

	List<ChargeRecord> chargeRecordList();

	Map<String, Object> selectcollect(Parameters parameter);
	
	Map<String, Object> selectcollectrefund(Parameters parameter);
	
	Double getTodayMoneyByUserid(@Param("begintime") String begintime,
			@Param("endtime") String endtime,@Param("merchantid") Integer merchantid);
	
	/** query user finally four record equipmentnum*/
	List<String> findUserLatelyfourRecord(Integer uid);

	Map<String, Object> selecwalletdetail(@Param("uid")Integer id);
	
	/** query all charge and continue charge by charge id*/
	List<ChargeRecord> selectChargeAndContinueById(Integer id);
	
	ChargeRecord selectChargeEndUidByCodeAndPort(@Param("equipmentnum")String equipmentnum, @Param("port")Integer port);
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	List<Map<String, Object>> selectChargeRecord(Parameters parameters);

	/** 根据参数记录Id（orderId）查询充电信息【含此次充电的续充 */
	List<ChargeRecord> chargeRecordRele(@Param("id")Integer orderId);

	/**
	 * @Description： 充电汇总所需信息
	 * @author： origin  
	 */
	Map<String, Object> chargetotal(Parameters parameter);
	
	Double selectEquipmentTotalMoneyByMerid(@Param("merchantid")Integer merchantid,@Param("equipmentnum")String equipmentnum);
	
	/**
	 * 查询10分钟内支付成功没回复的充电订单
	 * @return
	 */
	List<ChargeRecord> selectChargeNoReply();
	/**
	 * 查询一台设备截至到目前的耗电量
	 * @param equipmentnum 设备号
	 * @param begintime 开始时间
	 * @param endtime 结束时间
	 * @return int
	 */
	int selectConsumeQuantity(@Param("equipmentnum")String equipmentnum,@Param("begintime") String begintime,
			@Param("endtime") String endtime);
	/**
	 *  根据商家id查询所有设备一天消耗电量
	 * @param merchantid 商家id
	 * @param begintime 开始时间
	 * @param endtime 结束时间
	 * @return list 返回的所有设备
	 */
	
	//查询商家所有的总耗电
	Integer totalConsumeQuantity(@Param("merchantid")int merchantid); 
	//查询一天的总消耗
	Integer todayConsumeQuantity(@Param("merchantid")int merchantid,@Param("begintime") String begintime,
			@Param("endtime") String endtime);
	//查询昨天的总消耗电量 
	Integer yesterdayConsumeQuantity (@Param("merchantid")int merchantid,@Param("begintime") String begintime);
	//根据时间查询商家设备一段时间的总消耗
	List<Map<String,Object>>selectConsumeByTime(@Param("merchantid")int merchantid,@Param("begintime") String begintime,
			@Param("endtime") String endtime);
	//根据设备查询商家每台设备在一段时间内每天的总消耗电量
	List<Map<String,Object>> selectEveryConsumeQuantity(@Param("merchantid")int merchantid,@Param("begintime") String begintime,
			@Param("endtime") String endtime);
	//根据小区查询商家每个小区在一段时间内每天的总消耗
	List<Map<String,Object>> selectConsumeByArea(@Param("merchantid")int merchantid,@Param("begintime") String begintime,
			@Param("endtime") String endtime);
	//汇总商家的总耗电
	Map<String, Object> codeStatisticMer(@Param("merchantid")int merchantid,@Param("begintime") String begintime,
			@Param("endtime") String endtime);

	/**
	 * @Description：商户收益中在线卡、串口投币、离线卡统计
	 * @author： origin
	 */
	Map<String, Object> seleCardAndPulseCollect(Parameters parame);

	/**
	 * @Description：
	 * @author： origin
	 */
	Map<String, Object> selectCodeConsumeQuantity(Parameters parameters);

	/**
	 * @Description：
	 * @param datamap
	 * @author： origin
	 * @createTime：2020年3月31日下午2:42:38
	 * @comment:
	 */
	void insertCeShiBatch(@Param("params")Map<String, Object> datamap);

	/**
	 * @Description：
	 * @param createtime
	 * @param out_trade_no
	 * @param dealid
	 * @param tourid
	 * @param total_fee
	 * @param remark
	 * @param comment
	 * @author： origin
	 * @createTime：2020年3月31日下午5:55:06
	 * @comment:
	 */
	void insertCeShiDataBatch(@Param("createtime")Date createtime, 
			@Param("ordernum")String out_trade_no, @Param("dealid")Integer dealid, 
			@Param("tourid")Integer tourid, @Param("ordermoney")Integer total_fee,
			@Param("remark")String remark, @Param("comment")String comment);

	/**
	 * @Description：根据表（充电记录表）查询充电设备上传的窗口投币、离线刷卡消费、在线刷卡消费的信息
	 * @author： origin
	 * @createTime：2020年4月17日下午5:18:42
	 */
	Map<String, Object> chargeInfoCollect(Parameters parame);

	/**
	 * @Description：查询指定条件下的充电设备里的消耗时间与消耗电量，并分组
	 * @author： origin
	 * @createTime：2020年4月20日上午11:07:20
	 */
	List<Map<String, Object>> inquireInfoCollect(Parameters parameters);
	
	/**
	 * @Description 通过设备号查询最近24小时内未结束的订单
	 * @param code
	 * @return List
	 * @author RoarWolf
	 * @createTime 2020-04-27
	 */
	List<ChargeRecord> select24HoursUnfinshByCode(@Param("code")String code);
	
	/**
	 * @author zz
	 * 根据订单号查询一条预订单
	 * @param orderNum 订单号
	 * @return {@link Map}
	 */
	Map<String, Object> selectRecordByOrderNum(@Param("orderNum")String orderNum);
	
	/**
	 * @author zz
	 * 根据订单号将预订单更改为正常订单
	 * @param orderNum 订单号
	 */
	void updateRecoderStatus(@Param("orderNum")String orderNum); 
	
	/**
	 * @author zz
	 * 根据订单ID查询一条成功付款的订单(订单号,商家ID,交易金额)
	 * @param orderId 订单ID
	 * @return {@link Map}
	 */
	Map<String,Object> selectOneRecode(@Param("orderId")Integer orderId);
	
	/**
	 * 根据充电记录的订单编号更改信息
	 * @param record 订单
	 */
	void updateChargeRecode(ChargeRecord record);

	/**
	 * @method_name: chargeRecordCompute
	 * @Description: 查询昨日收入的窗口投币、离线卡刷卡、在线卡刷卡数据
	 * @param merid:商户id   startTime:开始时间    endTime:结束时间
	 * @Author: origin  创建时间:2020年8月31日 下午5:49:36
	 * @common:   
	 */
	Map<String, Object> chargeRecordCompute(Parameters parame);
	
	/**
	 * 查询满足条件的总条数
	 * @param parameters 参数
	 * @return {@link Integer}
	 */
	Integer selectChargeRecordNumber(Parameters parameters);
	
	/**
	 * PC端分页查询充电记录信息
	 * @param parameters 参数
	 * @return {@link List}
	 */
	List<Map<String, Object>> selectChargeRecords(Parameters parameters);
	
}
