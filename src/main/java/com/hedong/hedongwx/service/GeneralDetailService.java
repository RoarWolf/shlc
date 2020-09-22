package com.hedong.hedongwx.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.GeneralDetail;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.utils.PageUtils;

public interface GeneralDetailService {

	int insertGenWalletDetail(Integer uid,Integer merid,Double paymoney, Double sendmoney, Double tomoney, Double balance, 
		Double topupbalance, Double givebalance, String ordernum,Date createTime,Integer paysource);
	
//	int insertGenDetail(Integer uid, Integer merid, Double money, Double sendmoney, Double balance, String ordernum, Date createTime, Integer paysource, String remark);
	int insertGenDetail(Integer uid, Integer merid, Double paymoney, Double sendmoney, Double tomoney, Double balance, Double topupbalance, Double givebalance, String ordernum, Date createTime, Integer paysource, String remark);
	
	List<GeneralDetail> selectGenWalletDetailByUid(Integer uid);
	
	GeneralDetail selectGenWalletInfoById(Integer id);

	PageUtils<Parameter> advanceDetail(HttpServletRequest request);

	/**
	 * @Description： 用户在商户名下消费详情
	 * @author： origin          
	 * 创建时间：   2019年6月5日 上午9:52:37 
	 */
	List<Map<String, Object>> touristMoneyChange( Integer merid, String begintime, String endtime, Integer uid, Integer offset);

	List<Map<String, Object>>  selectWalletDetail(Integer uid);

}
