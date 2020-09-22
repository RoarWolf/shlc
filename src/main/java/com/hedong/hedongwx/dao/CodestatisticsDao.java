package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Codestatistics;
import com.hedong.hedongwx.entity.Parameters;

public interface CodestatisticsDao {

	//实体类插入
	void insertCodestatis(Codestatistics codestatistics);
	
	//通过设备号查询每日收益
	List<Codestatistics> selectAllByCode(@Param("code") String code,@Param("merid") Integer merid);
	
	//通过设备号查询每日收益（区间查询）
	List<Codestatistics> selectAllByCodeAndLimit(@Param("code") String code,@Param("merid") Integer merid,
			@Param("beginnum") Integer beginnum,@Param("endnum") Integer endnum);
	
	//通过用户查询每日收益
	List<Codestatistics> selectAllByMerid(@Param("merid") Integer merid,@Param("begintime")String begintime,@Param("endtime")String endtime);

	List<Map<String, Object>> getcodestatistics(Parameters parameters);

	//根据设备汇总，查询出商户的金额汇总信息
	Map<String, Object> agentmoneycollect(Parameters parame);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月22日 下午5:49:02 
	 */
	List<Map<String, Object>> agentmoneycollectBycode(Parameters params);

	/**
	 * @Description： 根据实体类查询统计信息
	 * @author： origin  
	 */
	Codestatistics selectStatistics(Codestatistics codestatistics);

	/**
	 * @Description： 实体类条件更新，根据id
	 * @author： origin       
	 */
	void updatecodestatistics(Codestatistics codestatistics);
	
	/**
	 * 根据设备号和时间查询部分字段（线上金额、投币金额）
	 * @param code
	 * @param countTime
	 * @return
	 */
	List<Codestatistics> selectOneByCodeAndTime(@Param("code") String code,@Param("countTime") String countTime);
	
	/**
	 * 根据商户id和时间查询部分字段（线上金额、投币金额）
	 * @param merid
	 * @param countTime
	 * @return
	 */
	Codestatistics selectMerYestEarn(@Param("merid") Integer merid,@Param("countTime") String countTime);
	 /**
     * 查询商家耗电数据
     * @param merId
     * @return
     */
    Map<String, Object> selectMerConsumData(@Param("merId") Integer merId);

	/**
	 * @Description：
	 * @author： origin
	 * @createTime：2020年3月13日下午6:13:19
	 */
	List<Codestatistics> selectStatisticsData(Codestatistics statistic);

	/**
	 * @Description：根据表（汇总表）查询以汇总的商户数量
	 * @author： origin
	 * @createTime：2020年4月17日下午6:08:00
	 */
	List<Map<String, Object>> inquireCollectMerid(Parameters parame);

	/**
	 * @Description：按月查询商户收益信息
	 * @author： origin
	 * @createTime：2020年4月24日下午3:38:05
	 */
	List<Map<String, Object>> inquireCollectMonth(Parameters parameters);

	/**
	 * @Description：按天查询商户收益信息
	 * @author： origin
	 * @createTime：2020年4月24日下午3:56:56
	 */
	List<Map<String, Object>> inquireCollectDay(Parameters parameters);

	/**
	 * @Description：按月查询商户收益信息(关联用户查询)
	 * @author： origin
	 * @createTime：2020年4月25日下午4:42:07
	 */
	List<Map<String, Object>> inquireCollectMonthInfo(Parameters parameters);

	/**
	 * @Description：
	 * @author： origin 2020年6月17日下午3:08:20
	 */
	Map<String, Object> inquireMerchEarningDay(@Param("merid")Integer dealid, @Param("time")String time);

	/**
	 * @method_name: getCodeEarnings
	 * @Description: 查询昨日设备收益记录【设备类型为智慧款充电设备】
	 * @param parame
	 * @Author: origin  创建时间:2020年9月5日 下午5:00:26
	 * @common:   
	 */
	List<Map<String, Object>> getCodeEarnings(Parameters parame);
	
	
}
