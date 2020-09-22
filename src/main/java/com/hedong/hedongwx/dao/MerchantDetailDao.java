package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.Parameters;

public interface MerchantDetailDao {

	int insertMerEarningDetail(MerchantDetail merchantDetail);
	
	List<MerchantDetail> selectMerDetailListByMerid(Integer merid);

	List<Map<String, Object>> selecearningsdetail(Parameters parameter);
	
	MerchantDetail selectMerEarnInfoById(Integer id);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年9月23日 上午11:25:11 
	 */
	Map<String, Object> dealerIncomeCount(Parameters params);
	
	List<Map<String, Object>> dealerIncomeCollect(Parameters params);
	
	/**
	 * @Description：根据表（商户收益明细表）查询不同商户数量
	 * @author： origin
	 * @createTime：2020年4月17日下午4:57:13
	 */
	List<Map<String, Object>> inquireMeridInfo(Parameters parame);
	
}
