package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.TradeRecord;

public interface TradeRecordDao {

	int insertTradeRecord(TradeRecord tradeRecord);
	
	/**
	 * @author zz
	 * 查询交易记录所有数据总条数
	 * @param parameters 参数
	 * @return {@link Integer}
	 */
	Integer selectTradeRecordCount(Parameters parameters);
	
	/**
	 * @author origin
	 * 查询交易记录所有数据
	 * @param parameters 参数
	 * @return {@link List}
	 */
	List<Map<String, Object>> selectTradeRecord(Parameters parameters);
	
	/**
	 * @author origin
	 * 查询交易记录所有数据
	 * @param parameters 参数
	 * @return {@link List}
	 */
	List<Map<String, Object>> selectTradeRecordA(Parameters parameters);
	
	
	//根据设备号查询订单
	List<Map<String, Object>> seleTradeToWechat(Parameters parame);
	
	/**
	 * @Description： 插入
	 * @author： origin 
	 */
	int insertToTradeRecord(TradeRecord tradeRecord);
	/**
	 * @Description： 根据条件查询交易信息
	 * @author： origin  
	 */
	TradeRecord getTraderecordList(TradeRecord trade);
	
	/**
	 * @Description： 查询交易记录数据与商户、合伙人和详细
	 * @author： origin   
	 */
	List<Map<String, Object>> selectTradeDetails(Parameters parameter);
	
	/** ====================================================================================== */
	/**
	 * @Description： 商户汇总查询
	 * @author： origin 
	 */
	Map<String, Object> merchantCollect(Parameters parameters);
	
	/**
	 * @Description： 合伙人汇总查询
	 * @author： origin 
	 */
	Map<String, Object> partnerCollect(Parameters parame);
	
	/**
	 * @Description： 查询交易记录收入支出平台金额
	 * @author： origin          
	 * @param parameters
	 */
	Map<String, Object> timingCollectMoney(Parameters parameters);
	
	/**
	 * @Description： 交易中查询消费人
	 * @author： origin          
	 * 创建时间：   2019年5月31日 上午11:30:14 
	 */
	List<Map<String, Object>> expendtotourist(Parameters parame);
	
	/**
	 * 根据条件查出部分交易记录
	 * @param tradeRecord
	 * @return
	 */
	List<TradeRecord> selectPartByParam(Parameters parame);
	/*List<TradeRecord> selectPartByParam(@Param("merid") Integer merid,@Param("ordernum") String ordernum,@Param("code") String code,
			@Param("paytype") Integer paytype,@Param("status") Integer status,@Param("startTime") String startTime,
			@Param("endTime") String endTime);*/
	
	/**
	 * @Description： 
	 * @param: 
	 * @author： origin   2019年10月7日 下午4:29:22 
	 */
	List<TradeRecord> getTradeRecordListInfo(TradeRecord trade);
	
	/**
	 * @Description： 
	 * @param: 
	 * @author： RoarWolf   2020年4月21日 下午18:43:22 
	 */
	int updateTradeMoney(@Param("money")Double money,@Param("ordernum")String ordernum);
	
	/**
	 * @Description：
	 * @author： origin  2020年5月29日下午5:00:20
	 */
	Integer updateTradeOrderInfo(@Param("ordernum")String ordernum, @Param("id")Integer traid);
	
	/**
	 * @Description：
	 * @author： origin 2020年5月30日下午5:05:35
	 * @comment:
	 */
	List<String> getTraderecordOrder(@Param("status")Integer status, @Param("begintime")String bigintime, @Param("endtime")String endtime);

	/**
	 * @method_name: refundTradeInquire 【该接口在退款时使用】
	 * @Description: 根据订单号查询交易记录中订单信息
	 * @param ordernum:订单号
	 * @return
	 * @Author: origin  创建时间:2020年8月28日 上午10:40:15
	 * @common:   
	 */
	List<Map<String, Object>> refundTradeInquire(String ordernum);

	/**
	 * @method_name: tradeComputeMap
	 * @Description: 查询指定时间的类型、来源的订单数量和金额
	 * @param parame
	 * @Author: origin  创建时间:2020年8月31日 下午6:35:36
	 */
	Map<String, Object> tradeRecordCompute(Parameters parame);
    //==========================================================
	/**
	 * 查询交易记录主键ID
	 * @param parameters 参数
	 * @return {@link Integer}
	 */
	Integer selectTradeId(Parameters parameters);

	/**
	 * 查询满足条件的总条数据
	 * @param parameters 参数
	 * @return {@link Integer}
	 */
	Integer selectTradeCount(Parameters parameters);


	/**
	 * 进入首页的交易总数据
	 * @param parameters 参数
	 * @return {@link Map}
	 */
	Map<String, Object> selectTradeCollect(Parameters parameters);


	/**
	 * 条件搜索时的交易总数据
	 * @param parameters
	 * @return {@link Map}
	 */
	Map<String,Object> selectMerTradeCollect(Parameters parameters);

	/**
	 * 分页查询交易订单数据
	 * @param parameters 参数
	 * @return {@link List}
	 */
	List<Map<String, Object>> selectTradeData(Parameters parameters);
}
