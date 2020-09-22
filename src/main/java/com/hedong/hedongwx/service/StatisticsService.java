package com.hedong.hedongwx.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.Statistics;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.utils.PageUtils;

public interface StatisticsService {

	PageUtils<Parameters> selectInfo(HttpServletRequest request);

	int insertStatis(Statistics statistics);

	int insertStatisMap(Map<String, Object> map);

	Map<String, Object> selectYestecollect();

	Map<String, Object> selectcollectincometotal();

	Map<String, Object> selectcollectrefundtotal();
	
	//历史订单信息汇总
	Map<String, Object> collectinfo();
	
	//设备历史每日收益汇总信息查询
	PageUtils<Parameters> codeEarningsCollect(Integer souce);
	
	//设备当天每日收益汇总信息查询
	PageUtils<Parameters> nowcodeearnings();
	
	//根据设备汇总，查询出商户的金额汇总信息
	Map<String, Object> agentmoneycollect();

	//商户当天金额汇总
	Map<String, Object> agentnowmoneycollect();
	 
    // 查询商家耗电数据(昨日和总耗)
    Map<String, Object> selectMerConsumData(@Param("merId") Integer merId);

	
/*** ***  汇总记录操作       *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/	
	
	//设备定时汇总每日金额情况
	void devivceTimingCollect(String time);	

	/**
	 * @Description：设备定时更新电量、时间
	 * @author： origin
	 * @createTime：2020年3月13日下午5:52:37
	 */
	void dealerupdataquantity(String time);
	
	//设备定时汇总每日金额情况
	int agentTimingmoneycollect(String startTime, String endTime);
	int agentearningscollect(String time);//商户与合伙人按某一天时间汇总	

	//定时汇总金额
	Map<String, Object> timingCollectMoney(Parameters parameters);

	/**
	 * @Description： 根据条件汇总脉冲设备线下投币上传信息
	 * @author： origin 
	 */
	Map<String, Object> selectcoinsup(Parameters parameters);

	/**
	 * @Description： 统计所有微信、支付宝数据
	 * @author： origin  
	 */
	Map<String, Object> totalmessagerecord();

	/**
	 * @Description： 计算商户指定时间的汇总金额
	 * @author： origin
	 */
	Map<String, BigDecimal> agentIncomeNowTotal(Integer merid, String startTime, String endTime);

/*** @origin 汇总统计计算   ==================================================================================  ***/	

	/**
	 * @Description： 获得用户人数(总人数、月增人数、日增人数、在线消费人数)
	 * @author： origin          
	 * 创建时间：   2019年5月31日 上午10:15:36 
	 */
	Map<String, Object> getTouristPeople(Integer merid, Integer rank);
	
	/**
	 * @Description： 
	 * @author： origin          
	 */
	Object sometimeturnover( String weekstartTime, String weekendTime);

	/**
	 * @Description： 
	 * @author： origin          
	 */
	Object sometimeorder();

	/**
	 * @Description： 
	 * @author： origin          
	 */
	List<Map<String, Object>> codeincome();

	/**
	 * @Description： 
	 * @author： origin          
	 */
	Map<String, Object> earningsinfonow();

	/**
	 * @Description： 
	 * @author： origin          
	 */
	Map<String, Object> earningsinfoyes(String startTime, String endTime);

	/**
	 * @Description： 查询汇总信息
	 * @author： origin 创建时间：   2019年8月19日 下午3:46:22 
	 */
	List<Map<String, Object>> earningcollectinfo( User user, String begintime, String endtime, Integer type, String param, Integer showincoins);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月20日 下午4:48:43 
	 */
	List<Map<String, Object>> getStatistics(Parameters paramet);

	/**
	 * @Description： 商户收益汇总
	 * @author： origin 创建时间：   2019年9月18日 下午5:42:58 
	 */
	void dealerIncomeCollect(String time);

	/**
	 * @Description： 经销商收益汇总
	 * @param:  deaid:经销商id	 time:计算时间
	 * @author： origin   2019年9月27日 下午7:54:51 
	 */
	void dealerIncomecollect(Integer deaid, String time);

	/**
	 * @Description： 指定经销商当前所绑定的设备收益汇总
	 * @param:  deaid:经销商id	 time:计算时间
	 * @author： origin   2019年9月27日 下午7:55:02 
	 */
	void dealerDeviceIncomeDispose(Integer deaid, String time);

	/**
	 * @Description： 商户收益 计算汇总数据(所有绑定的设备的商户与合伙人)
	 * @param: (time:汇总数据的时间）
	 * @author： origin   2019年9月27日 下午8:13:07 
	 */
	void dealIncoCollectAll(String time);

	/**
	 * @Description： 设备收益 计算汇总数据(所有绑定的设备)
	 * @param: (time:汇总数据的时间）
	 * @author： origin   2019年9月27日 下午8:13:07 
	 */
	void dealDeviIncoCollectAll(String time);

	// === 前后段分离处理数据信息 开始  ==================================================================================

	/**
	 * separate
	 * @Description： 收益汇总信息 type 1:设备收益信息  2：商户收益信息
	 * @author： origin   2019年11月4日 下午4:28:33 
	 */
	Object earningsData(HttpServletRequest request, Integer type);


	/**
	 * @Description：商户收益下载
	 * @author： origin
	 * @createTime：2020年4月24日下午3:14:46
	 */
	Object dealerEarningsDownload(HttpServletRequest request);

	
	/**
	 * separate
	 * @Description： 历史统计信息
	 * @author： origin   2019年11月1日 下午5:32:14
	 */
	Object historyStatisticsData(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 数据汇总信息查询
	 * @author： origin   2019年11月1日 下午5:32:14
	 */
	Object dataCollectInquire(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 获取昨日统计值
	 * @author： origin   2019年11月21日 下午5:23:21 
	 */
	Map<String, Object> statisticsData(Integer merid, Integer type, String startTime, String endTime);

	/**
	 * @Description：查询商户所有收益
	 * @author： origin
	 * @createTime：2020年3月7日上午11:13:11
	 */
	Map<String, Object> dealerMoneyCollect(Integer dealid);

	/**
	 * @Description：查询商户当天收益
	 * @author： origin
	 * @createTime：2020年3月7日上午11:13:17
	 */
	Map<String, Object> dealerNowMoneyCollect(Integer dealid);

	/**
	 * @Description：商户收益推送
	 * @author： origin 2020年6月13日下午6:33:18
	 * @comment:	//ORIGIN  ToUpload	商户收益推送
	 */
	void dealerIncomePush();

	/**
	 * @method_name: inquireDeviceIncomeInfo
	 * @Description: 查询昨日设备收益记录
	 * @Author: origin  创建时间:2020年8月29日 下午5:16:20
	 * @common:   
	 */
	List<Map<String, Object>> inquireDeviceIncomeInfo(HttpServletRequest request);

	/**
	 * @method_name: inquirePlatformIncomeInfo
	 * @Description: 查询最近十五天交易金额
	 * @Author: origin  创建时间:2020年8月29日 下午5:16:23
	 * @common:   
	 */
	List<Map<String, Object>> inquirePlatformIncomeInfo(HttpServletRequest request);

	// === 前后段分离处理数据信息 结束  =================================================================================
	
	
}
