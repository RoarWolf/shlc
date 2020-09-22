package com.hedong.hedongwx.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.InCoins;
import com.hedong.hedongwx.entity.PackageMonthRecord;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.utils.PageUtils;

public interface TradeRecordService {

	int insertTradeRecord(TradeRecord tradeRecord);
	
	int insertTrade(Integer merid, Integer uid, String ordernum, Double money, Double mermoney, String code, Integer paysource,
			Integer paytype, Integer status, String hardver);
	
	int insertToTrade(Integer merid, Integer manid, Integer uid, String ordernum, Double money, Double mermoney, 
			Double manmoney, String code, Integer paysource, Integer paytype, Integer status, String hardver);
	
	Integer insertToTrade(Integer merid, Integer manid, Integer uid, String ordernum, Double money, Double mermoney, 
	Double manmoney, String code, Integer paysource, Integer paytype, Integer status, String hardver, String comment);
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//分页查询交易记录所有数据
	PageUtils<Parameters> selectTradeRecord(HttpServletRequest request);
	
	//根据设备号查询订单
	List<Map<String, Object>> seleTradeToWechat(Parameters parame);
	
	//在交易表中查询投币记录查询
	PageUtils<Parameters> selectCoinsInfoByTr(HttpServletRequest request);
	
	/**
	 * @Description： 根据指定条件查询交易信息
	 * @author： origin
	 */
	TradeRecord getTraderecord(String ordernum);
	/**
	 * @Description： 查询交易记录数据与商户、合伙人和详细
	 * @author： origin 
	 */
	Map<String, Object> selectTradeDetails(String ordernum, Integer status);
	
	/**
	 * @Description： 根据条件查询交易数据
	 * @author： origin
	 */
	List<Map<String, Object>> tradeDetailsinquire(Parameters parameter);
	
	/**
	 * @Description： 
	 * @author： origin  
	 */
	TradeRecord getTraderecordList(TradeRecord trade);
	
	/**
	 * @Description： 交易记录查询用户消费信息
	 * @author： origin          
	 * 创建时间：   2019年5月30日 下午5:04:46
	 */
	List<Map<String, Object>> touristorder( Integer merid, String begintime, String endtime, Integer uid, Integer offset);
	/**
	 * @Description： 
	 * @author： origin          
	 */
	Object newestOrder(Integer traid, Integer merid);
	
	/**
	 * @Description： 包月信息查询
	 * @author： origin 创建时间：   2019年7月18日 下午6:25:50 
	 */
	PageUtils<Parameters> selectPackageMonth(HttpServletRequest request);
	
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月19日 下午4:54:32 
	 */
	List<PackageMonthRecord> selectMonthRecordEntiy(String ordernum, Integer status);
	
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月19日 下午7:38:42 
	 */
	TradeRecord selectTradeById(Integer orderid);
	
	/**
	 * 根据条件查出部分交易记录
	 * @param tradeRecord
	 * @return
	 */
	List<TradeRecord> selectPartByParam(Integer merid, String ordernum,String code,Integer paytype,
			Integer status,String startTime,String endTime);
	
	/**
	 * @Description： 根据指定条件查询交易信息
	 * @author： origin
	 */
	List<TradeRecord> getTraderecordList(String ordernum, Integer status);
	
	//=== 前后分离 ===============================================================================================
	/**
	 * separate
	 * @Description： 查询交易记录信息
	 * @author： origin 
	 */
	Object tradeRecordData(HttpServletRequest request);

	/**
	 * PC端查询交易记录
	 * @param request 参数
	 * @return {@link Object}
	 */
	Object selectOrderData(HttpServletRequest request);
	
	/**
	 * @Description： 修改交易记录金额
	 * @param: 
	 * @author： RoarWolf
	 */
	int updateTradeMoney(Double money,String ordernum);
	
	/**
	 * @Description：
	 * @param ordernum
	 * @param traid
	 * @author： origin
	 * @createTime：2020年5月29日下午4:59:30
	 * @comment:
	 */
	Integer updateTradeOrderInfo(String ordernum, Integer traid);
	
	/**
	 * @Description：
	 * @author： origin 2020年5月30日下午5:05:35
	 * @comment:
	 */
	List<String> getTraderecordOrder(Integer status, String bigintime, String endtime);
	
	/**
	 * 银联支付生成预订单
	 * @author:ZZ
	 * @param merUserId 商家的Id
	 * @param equCode 设备的编号
	 * @param money 用户支付的钱
	 */
	Map<String, Object> createUnioPayOrderNum(HttpServletRequest request);
	
	
	/**
	 * 处理银联付款成功后的回调逻辑
	 * @param request 回调请求
	 * @return {@link String}
	 */
	String unionPayBack(HttpServletRequest request);

	/**
	 * 银联退款申请
	 * @param request
	 * @return {@link String}
	 */
	Map<String, Object> unionDoRefond(HttpServletRequest request);

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
	Map<String, Object> tradeComputeMap(Parameters parame);
}
