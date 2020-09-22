package com.hedong.hedongwx.service;

import java.util.Date;
import java.util.List;

import com.hedong.hedongwx.entity.MerchantDetail;

public interface MerchantDetailService {

	int insertMerEarningDetail(Integer merid,Double money,Double balance,String ordernum,Date createTime,Integer paysource
			,Integer paytype, Integer status);
	
	List<MerchantDetail> selectMerDetailListByMerid(Integer merid);
	
	MerchantDetail selectMerEarnInfoById(Integer id);

	/**
	 * 你猜你猜
	 * @param merid
	 * @param money
	 * @param type
	 * @param ordernum
	 * @param paysource
	 * @param paytype
	 * @param status
	 */
	void merEearningCalculate(Integer merid, Double money, Integer type, String ordernum, Integer paysource,
			Integer paytype, Integer status);

	/**
	 * 你猜不到
	 * @param comment
	 * @param merid
	 * @param money
	 * @param ordernum
	 * @param time
	 * @param paysource
	 * @param paytype
	 * @param paystatus
	 * @return
	 */
	Object dealerIncomeRefund(String comment, Integer merid, Double money, String ordernum,
			Integer paysource, Integer paytype, Integer paystatus);
	
	
}
