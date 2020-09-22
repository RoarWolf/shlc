package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Codestatistics;
import com.hedong.hedongwx.entity.Parameters;

/**
 * @Description： 统计汇总
 * @author  origin  创建时间：   2019年9月7日 下午2:41:42  
 */
public interface CollectStatisticsDao {

	/**
	 * @Description： 根据表（商户收益明细表）查询商户收益信息
	 * @author： origin
	 * @createTime：2020年4月16日上午9:16:13
	 */
	List<Map<String, Object>> dealerIncomeCollect(Parameters parame);

	/**
	 * @Description：根据表（脉冲记录表）查询脉冲设备上传投币信息
	 * @author： origin
	 * @createTime：2020年4月16日上午9:17:27
	 */
	List<Map<String, Object>> incoinsIncomeCollect(Parameters parame);

	/**
	 * @Description：根据表（充电记录表）查询充电设备上传的窗口投币、离线刷卡消费、在线刷卡消费的信息
	 * @author： origin
	 * @createTime：2020年4月16日上午9:20:13
	 */
	List<Map<String, Object>> chargeIncomeCollect(Parameters parame);

	/**
	 * @Description：根据表（充电记录表）查询充电设备上传的消耗的电量、时间信息
	 * @author： origin
	 * @createTime：2020年4月16日上午9:20:19
	 */
	List<Map<String, Object>> chargeConsumeCollect(Parameters parame);

	/**
	 * @Description：根据表（充电记录表）查询充电设备上传的窗口投币、离线刷卡消费、在线刷卡消费的信息
	 * @author： origin
	 * @createTime：2020年4月16日上午9:20:24
	 */
	List<Map<String, Object>> chargeInfoCollect(Parameters parame);
	
	/**
	 * @Description：根据表（交易记录表）查询设备信息
	 * @author： origin
	 * @createTime：2020年4月16日下午6:26:41
	 */
	List<Map<String, Object>> timingCollectMoney(Parameters parame);

	/**
	 * @Description：根据表（商户收益明细表）查询不同商户数量
	 * @author： origin
	 * @createTime：2020年4月16日上午11:35:09
	 */
	List<Map<String, Object>> inquireMeridInfo(Parameters param);
	
	/**
	 * @Description：根据表（汇总表）查询以汇总的商户数量
	 * @author： origin
	 * @createTime：2020年4月17日下午6:03:51
	 */
	List<Map<String, Object>> inquireCollectMerid(Parameters param);
	
	/**
	 * @Description：
	 * @param mapinfo
	 * @author： origin
	 * @createTime：2020年4月16日下午12:59:45
	 * @comment:
	 */
	void insertionCollectData(@Param("list")List<Map<String, Object>> mapinfo);

	/**
	 * @Description：
	 * @param statis
	 * @author： origin
	 * @createTime：2020年4月16日下午2:57:00
	 * @comment:
	 */
	void insertionCollectInfo(@Param("list")List<Codestatistics> statis);

	/**
	 * @Description：根据map插入单条数据
	 * @param resultmap
	 * @author： origin
	 * @createTime：2020年4月16日下午5:11:05
	 * @comment:
	 */
	void insertionCollectMap(@Param("params")Map<String, Object> resultmap);

	/**
	 * @Description：根据实体类更新单条数据
	 * @author： origin
	 * @createTime：2020年4月24日下午12:45:42
	 * @comment:
	 */
	void updateCollectInfo(Codestatistics collect);

	
	
	/*-- *** 请暂时不要在*号范围内添加   thanks ***********************************************************   --*/
	/**
	 * @method_name: inquireAllmeridInfo
	 * @Description: 查询没有绑定设备合伙人信息
	 * @return
	 * @Author: origin  创建时间:2020年8月14日 下午4:53:37
	 * @common:   
	 */
	List<Map<String, Object>> inquireAllmeridInfo();

	/**
	 * @method_name: dealerIncomeOrder
	 * @Description: 根据表（商户收益明细表）查询商户收益订单数量信息
	 * @param parame
	 * @return
	 * @Author: origin  创建时间:2020年8月14日 下午4:53:53
	 * @common:   
	 */
	List<Map<String, Object>> dealerIncomeOrder(Parameters parame);

	/**
	 * @method_name: dealerTopupIncome
	 * @Description: 根据表（商户收益明细表）查询商户除设备外的充值收益信息
	 * @param parame
	 * @return
	 * @Author: origin  创建时间:2020年8月14日 下午4:54:00
	 * @common:   
	 */
	List<Map<String, Object>> dealerTopupIncome(Parameters parame);

	/**
	 * @method_name: dealerDeviceIncome
	 * @Description: 根据表（商户收益明细表）查询商户设备的收益信息
	 * @param parame
	 * @return
	 * @Author: origin  创建时间:2020年8月14日 下午4:54:04
	 * @common:   
	 */
	List<Map<String, Object>> dealerDeviceIncome(Parameters parame);

	/**
	 * @method_name: chargeConsumeData
	 * @Description: 根据表（充电记录表）查询充电设备上传的窗口投币、离线刷卡消费、在线刷卡消费的电量与时间信息
	 * @param parame
	 * @return
	 * @Author: origin  创建时间:2020年8月14日 下午4:54:13
	 * @common:   
	 */
	List<Map<String, Object>> chargeConsumeData(Parameters parame);

	/**
	 * @method_name: incoinsNumStatistics
	 * @Description: 根据条件汇总脉冲上传记录信息
	 * @param parame
	 * @return
	 * @Author: origin  创建时间:2020年8月14日 下午4:54:21
	 * @common:   
	 */
	List<Map<String, Object>> incoinsNumStatistics(Parameters parame);

	/**
	 * @method_name: insertionDealerIncome
	 * @Description: 
	 * @param result
	 * @Author: origin  创建时间:2020年8月14日 下午4:53:46
	 * @common:   
	 */
	void insertionDealerIncome(List<Map<String, Object>> result);

	/*-- ****************************************************************************************  --*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
