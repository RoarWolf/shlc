package com.hedong.hedongwx.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description： 
 * @author  origin  创建时间：   2020年6月12日 下午5:45:39  
 */
public interface AmendDataService {

	/**
	 * @method_name: dealerIncomeData
	 * @Description: 
	 * @param startTimes
	 * @Author: origin  创建时间:2020年8月11日 下午12:40:20
	 * @common:   
	 */
	
	List<Map<String, Object>> dealerIncomeData(String times);

	/**
	 * @method_name: dealerIncomeInfo
	 * @Description: 
	 * @param startTimes
	 * @return
	 * @Author: origin  创建时间:2020年8月12日 上午11:34:08
	 * @common:   
	 */
	List<Map<String, Object>> dealerIncomeInfo(String times);

	/**
	 * @method_name: calculateTotalCollect
	 * @Description: 
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月12日 下午2:41:30
	 * @common:   
	 */
	Object calculateTotalCollect(HttpServletRequest request);
	
	/**
	 * @method_name: calculateCollect
	 * @Description: 
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月13日 下午6:07:40
	 * @common:   
	 */
	Object calculateCollect(HttpServletRequest request);

	/**
	 * @method_name: inquireDealerIncomeData
	 * @Description: 查询商户收益汇总信息（设备收益与充值收益分开记录）
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月13日 下午5:50:46
	 * @common:
	 */
	Object inquireDealerIncomeData(HttpServletRequest request);

	
	
	
}
