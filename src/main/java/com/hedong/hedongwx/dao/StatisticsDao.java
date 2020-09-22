package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.Statistics;

public interface StatisticsDao {

	List<Map<String, Object>> selectInfo(Parameters parame);

	int insertStatis(Statistics statistics);

	int insertStatismap(@Param("params")Map<String, Object> params);
	
	/** 根据时间计算查询总数 */
	Map<String, Object> selectInfototal(Parameters parameter);

	//历史订单信息汇总
	Map<String, Object> collectinfo(); 
}
