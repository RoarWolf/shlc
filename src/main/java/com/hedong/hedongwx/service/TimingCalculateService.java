package com.hedong.hedongwx.service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 定时计算接口
 * @Author: origin  创建时间:2020年8月14日 下午3:55:31
 * @common:
 */
public interface TimingCalculateService {

	/**
	 * @method_name: dealerIncomeInfo
	 * @Description: 充值、设备收益分开计算商户汇总信息
	 * @param time 计算[汇总]的时间
	 * @return
	 * @Author: origin  创建时间:2020年8月14日 下午3:55:17
	 * @common:   
	 */
	List<Map<String, Object>> dealerIncomeInfo(String time);

//	/**
//	 * 根据给定时间，设备每日汇总计算（数据从充电、脉冲、离线卡充值、钱包充值查询）
//	 * @author  origin          
//	 * @version 创建时间：2019年3月2日  下午5:40:12
//	 */
//	int codeTimingCollect(String startTime, String endTime);
//
//	/**
//	 * 根据给定时间，代理商每日汇总计算
//	 * @author  origin          
//	 * @version 创建时间：2019年3月2日  下午5:41:45
//	 */
//	int agentTimingmoneycollect(String startTime, String endTime);

}
