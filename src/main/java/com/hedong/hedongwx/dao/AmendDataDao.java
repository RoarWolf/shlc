package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Codestatistics;
import com.hedong.hedongwx.entity.Parameters;

/**
 * @Description： 
 * @author  origin  创建时间：   2020年6月12日 下午5:56:39  
 */
public interface AmendDataDao {

	/**
	 * @method_name: dealerIncomeStatistics
	 * @Description: 
	 * @param parame
	 * @return
	 * @Author: origin  创建时间:2020年8月11日 上午11:33:09
	 * @common:   
	 */
	List<Map<String, Object>> dealerIncomeStatistics(Parameters parame);

	/**
	 * @method_name: chargeConsumeData
	 * @Description: 
	 * @param parame
	 * @return
	 * @Author: origin  创建时间:2020年8月11日 上午11:34:54
	 * @common:   
	 */
	List<Map<String, Object>> chargeConsumeData(Parameters parame);

	/**
	 * @method_name: incoinsNumStatistics
	 * @Description: 
	 * @param parame
	 * @return
	 * @Author: origin  创建时间:2020年8月11日 上午11:35:08
	 * @common:   
	 */
	List<Map<String, Object>> incoinsNumStatistics(Parameters parame);

	/**
	 * @method_name: inquireMeridData
	 * @Description: 
	 * @return
	 * @Author: origin  创建时间:2020年8月11日 上午11:37:31
	 * @common:   
	 */
	List<Map<String, Object>> inquireMeridData();

	/**
	 * @method_name: inquireAllmeridInfo
	 * @Description: 
	 * @return
	 * @Author: origin  创建时间:2020年8月11日 下午2:13:13
	 * @common:   
	 */
	List<Map<String, Object>> inquireAllmeridInfo();

	/**
	 * @method_name: dealerTopupIncome
	 * @Description: 
	 * @param parame
	 * @return
	 * @Author: origin  创建时间:2020年8月11日 下午3:56:01
	 * @common:   
	 */
	List<Map<String, Object>> dealerTopupIncome(Parameters parame);

	/**
	 * @method_name: dealerDeviceIncome
	 * @Description: 
	 * @param parame
	 * @return
	 * @Author: origin  创建时间:2020年8月11日 下午3:56:05
	 * @common:   
	 */
	List<Map<String, Object>> dealerDeviceIncome(Parameters parame);

	/**
	 * @method_name: dealerIncomeOrder
	 * @Description: 
	 * @param parame
	 * @return
	 * @Author: origin  创建时间:2020年8月11日 下午4:40:56
	 * @common:   
	 */
	List<Map<String, Object>> dealerIncomeOrder(Parameters parame);

	/**
	 * @method_name: insertionDealerIncome
	 * @Description: 
	 * @param result
	 * @Author: origin  创建时间:2020年8月12日 下午12:49:34
	 * @common:   
	 */
	void insertionDealerIncome(@Param("list")List<Map<String, Object>> result);

	/**
	 * @method_name: insertCodestatis
	 * @Description: 
	 * @param collectData
	 * @Author: origin  创建时间:2020年8月12日 下午2:52:08
	 * @common:   
	 */
	void insertCodestatis(Codestatistics collectData);

	/**
	 * @method_name: inserDealerIncome
	 * @Description: 
	 * @param result
	 * @Author: origin  创建时间:2020年8月12日 下午3:01:03
	 * @common:   
	 */
	void inserDealerIncome(List<Map<String, Object>> result);

	/**
	 * @method_name: inquireDealerIncomeData
	 * @Description: 
	 * @Author: origin  创建时间:2020年8月13日 下午7:28:49
	 * @common:   
	 */
	List<Map<String,Object>> inquireDealerIncomeData(Parameters parame);

	
	
	
}
