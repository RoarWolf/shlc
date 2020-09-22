package com.hedong.hedongwx.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description： 汇总统计
 * @author  origin  创建时间：   2019年9月7日 下午2:38:42  
 */
public interface CollectStatisticsService {
	
	
	/**
	 * @Description：商户收益汇总
	 * @param time
	 * @author： origin
	 * @createTime：2020年4月15日上午11:05:20
	 */
	void dealerIncomeCollect(String time);
	
	/**
	 * @Description：设备收益汇总
	 * @param time
	 * @author： origin
	 * @createTime：2020年4月15日上午11:05:20
	 */
	void devivceIncomeCollect(String time);

	/**
	 * @Description：商户定时汇总
	 * @param time
	 * @author： origin
	 * @createTime：2020年4月15日上午11:05:20
	 */
	void dealerTimingCollect(String time);

	/**
	 * @Description：设备定时汇总
	 * @param time
	 * @author： origin
	 * @createTime：2020年4月15日上午11:05:20
	 */
	void devivceTimingCollect(String time);	
	

	
	/**
	 * @Description：根据信息计算收益结算情况 1 type: 1 计算商户收益    2:计算设备收益
	 * 				 merid：商户id   time 指定时间    devicenum：设备号
	 * @param time
	 * @author： origin
	 * @createTime：2020年4月15日上午11:05:20
	 */
	void incomeSettlement(Integer type, Integer merid, String time, String devicenum);
	
	/**
	 * @Description：商户定时汇总
	 * @param time
	 * @author： origin
	 * @createTime：2020年4月15日上午11:05:20
	 */
	void dealerTimingSettlement(HttpServletRequest request);

	/**
	 * @Description：设备定时汇总
	 * @param time
	 * @author： origin
	 * @createTime：2020年4月15日上午11:05:20
	 */
	void devivceTimingSettlement(HttpServletRequest request);

	/**
	 * @Description：
	 * @param merid
	 * @author： origin
	 * @createTime：2020年4月17日上午10:58:49
	 */
	Map<String, Object> yesterdaydata(Integer merid, Integer type, String startTime, String endTime);
	
}
