package com.hedong.hedongwx.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.Realchargerecord;
import com.hedong.hedongwx.utils.PageUtils;

public interface ChargeRecordService {

	/** query charge record by user id*/
	List<ChargeRecord> chargeRecordByUid(Integer uid);

	/** query charge record by charge id*/
	ChargeRecord chargeRecordOne(Integer id);

	/** add record*/
	void insetRecord(ChargeRecord chargeRecord);

	/** query charge record by charge ordernum*/
	List<ChargeRecord> getOrderByOrdernum(String ordernum);

	void updateByOrdernum(Map<String, Object> params);//根据map类型参数更新数据信息
	
	/** query charge records by time and user id*/
	List<ChargeRecord> getChargeRecordListByUseridAndTime(String begintime, String endtime, Integer uid);
	
	/** query today charge records by merchant id*/
	List<ChargeRecord> getTodayChargeRecordListByUserid(Integer merchantid);
	
	/** query charge records by time and user id*/
	List<ChargeRecord> getChargeRecordListByTimeAndUid(String begintime, String endtime, Integer uid);
	
	/** query charge records by equipmentnum*/
	List<ChargeRecord> getChargeRecordListByEquipmentnum(String equipmentnum);
	
	/** query charge records by time and equipmentnum and merchat id and ordernum*/
	List<ChargeRecord> getChargeRecordListByTimeAndEquipmentnumMerchantid(String begintime, String endtime,
			String equipmentnum, Integer merchantid, String ordernum);
	
	/** query is charging records by user id*/
	Map<String, Object> queryChargingByUid(Integer uid, Integer startnum);
	
	/** query is charged records by user id*/
	Map<String, Object> queryChargedByUid(Integer uid, Integer startnum);
	
	/** query charge and continue charge total money by charge id*/
	Double getChargingTotalMoney(Integer id);
	
	int updateChargeEndtimeById(Integer id);

	PageUtils<ChargeRecord> selectOrderInfo(HttpServletRequest request);////ＲＺＣ（ＰＣ）　查询订单记录
	
	Double getTotalMoneyByTimeAndEquipmentnumMerchantid(String begintime, String endtime, String equipmentnum, Integer merchantid,String ordernum);
	
	int updateNumberById(String ordernum,Integer number,Integer id,String durationtime, Double expenditure, Double refundMoney, String endTime, String refundTime);
	
	ChargeRecord chargeRecordOneContinueEnd(Integer id);

	List<ChargeRecord> chargeRecordList();

	Map<String, Object> selectcollect();
	
	Double getTodayMoneyByUserid(String begintime,String endtime,Integer merchantid);
	
	ChargeRecord getChargeRecordById(Integer id);
	
	List<String> findUserLatelyfourRecord(Integer uid);
	
	/** query all charge and continue charge by charge id*/
	List<ChargeRecord> selectChargeAndContinueById(Integer id);
	
	/**
	 * 根据设备号和端口号查询充电记录
	 * @param equipmentnum 设备号
	 * @param port 端口号
	 * @return {@link ChargeRecord}
	 */
	ChargeRecord selectChargeEndUidByCodeAndPort(String equipmentnum, Integer port);
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询交易记录信息
	PageUtils<Parameters> selectChargeRecord(HttpServletRequest request);

	/**
	 * @Description： 根据参数记录Id（orderId）查询充电信息【含此次充电的续充
	 * @author： origin 
	 */
	List<ChargeRecord> chargeRecordRele(Integer orderId);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月20日 上午9:47:34 
	 */
	List<ChargeRecord> getRecordData(String ordernum, Integer paytype);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月23日 上午10:58:11 
	 */
	Object chargeRecordInfo(Integer uid, Integer offset, Integer page);
	
	/**
	 * 查询10分钟内支付成功没回复的充电订单
	 * @return
	 */
	List<ChargeRecord> selectChargeNoReply();
	/**
	 * 根据设备号查询一天内的耗电量
	 * @param equipmentnum 设备号
	 * @param begintime 一天的开始时间
	 * @param endtime 一天的结束时间
	 * @return int
	 */
	int selectConsumeQuantity(String equipmentnum,String begintime,String endtime);
	/**
	 *  根据商家id查询所有设备一天消耗电量
	 * @param merchantid 商家id
	 * @param begintime 开始时间
	 * @param endtime 结束时间
	 * @return list 返回的所有设备
	 */
	
	//查询商家所有的总耗电
	Integer totalConsumeQuantity(int merchantid); 
	//查询一天的总消耗
	Integer todayConsumeQuantity(Integer merchantid,String begintime,String endtime);
	//查询昨天的总消耗电量 
	Integer yesterdayConsumeQuantity (int merchantid,String begintime);
	
	
	//根据时间查询商家设备一段时间的总消耗
	List<Map<String,Object>>selectConsumeByTime(int merchantid,String begintime,String endtime);
	//根据设备查询
	List<Map<String,Object>> selectEveryConsumeQuantity(int merchantid,String begintime,String endtime);
	//根据小区查询商家每个小区在一段时间内每天的总消耗
	List<Map<String,Object>> selectConsumeByArea(int merchantid,String begintime, String endtime);

	/**
	 * @Description：    根据条件查询数据的函数情况  1：根据充电id(chargeid)
	 * @author： origin     
	 */
	Map<String, Object> functionRecord(Integer orderId);
	
	/**
	 * @Description：   根据条件查询用户消费信息
	 * @author： origin 
	 * @return hd_realchargerecord
	 */
	List<Realchargerecord> realChargeRecordList(Integer orderId);
	
	
	/**
	 * @Description： 根据订单id查询该订单的功率信息
	 * @param: orderId 订单id
	 * @author： origin   2019年10月8日 下午5:25:55 
	 */
	Map<String, Object> powerBrokenLine(Integer orderId);

	/**
	 * separate
	 * @Description：充电记录信息查看
	 * @author： origin 
	 */
	Object chargeRecordData(HttpServletRequest request);

	/**
	 * separate
	 * @Description：查询充电记录中的折现图
	 * @author： origin 
	 */
	Object inquirePowerBrokenLine(HttpServletRequest request);
	
	/**
	 * 根据订单id查询最大功率
	 * @param chargeid
	 * @return max power
	 */
	Integer selectMaxPowerByChargeid(Integer chargeid);

	/**
	 * @Description：测试数据 测试使用
	 * @param datamap
	 * @author： origin
	 * @createTime：2020年3月31日下午2:41:32
	 * @comment:
	 */
	void insertCeShiBatch(Map<String, Object> datamap);

	/**
	 * @Description：
	 * @param data
	 * @param out_trade_no
	 * @param tourid
	 * @param tourid2
	 * @param total_fee
	 * @param string
	 * @param string2
	 * @author： origin
	 * @createTime：2020年3月31日下午5:53:14
	 * @comment:
	 */
	void insertCeShiDataBatch(Date createtime, String out_trade_no, Integer dealid, Integer tourid, Integer total_fee,
			String remark, String comment);
					
	/**
	 * @Description 通过设备号查询最近24小时内未结束的订单
	 * @param code
	 * @return List
	 * @author RoarWolf
	 * @createTime 2020-04-27
	 */
	List<ChargeRecord> select24HoursUnfinshByCode(String code);
	
	/**
	 * 根据时间删除功率表
	 * @param createtime
	 * @return
	 * @author RoarWolf
	 * @createTime 2020-04-27
	 */
	int delectRealChargeByTime(String createtime);
	
	/**
	 * @author zz
	 * 根据充电订单查询一条充电预订单
	 * @param orderNum 充电单号
	 * @return {@link Map}
	 */
	Map<String, Object> selectRecordByOrderNum(String orderNum);
	
	
	/**
	 * @author zz
	 * 根据订单号将预订单更改为正常订单
	 * @param orderNum 订单号
	 */
	void updateRecoderStatus(String orderNum); 
	
	/**
	 * @author zz
	 * 银联充电退款
	 * @param chargeRecord
	 */
	void chargeRefund(ChargeRecord chargeRecord);

	/**
	 * @method_name: chargeRecordCompute
	 * @Description: 查询昨日收入的窗口投币、离线卡刷卡、在线卡刷卡数据
	 * @param merid:商户id   startTime:开始时间    endTime:结束时间
	 * @Author: origin  创建时间:2020年8月31日 下午5:49:36
	 * @common:   
	 */
	Map<String, Object> chargeRecordCompute(Integer merid, String startTime, String endTime);

	
}
