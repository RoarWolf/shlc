package com.hedong.hedongwx.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description： 
 * @author  origin  创建时间：   2019年11月11日 上午11:25:17  
 */
public interface OrderDataService {
	
	
	/**
	 * separate
	 * @Description：交易订单详情查看
	 * @author： origin 
	 */
	Object tradeDataDetail(HttpServletRequest request);

	/**
	 * separate
	 * @Description：包月记录信息查看
	 * @author： origin 
	 */
	Object packageMonthData(HttpServletRequest request);


	/**
	 * separate
	 * @Description：商户收益订单明细明信息查询
	 * @author： origin 
	 */
	Object dealerEarningsOrderDetail(HttpServletRequest request);

	/**
	 * separate
	 * @Description：钱包明细根据订单号和来源查询该条订单
	 * @author： origin
	 */
	Object touristWalletDetail(HttpServletRequest request);

	/**
	 * separate
	 * @Description：交易订单查询合伙人收益信息
	 * @author： origin 
	 */
	Object partnerIncomeDetail(HttpServletRequest request);

	/**
	 * separate
	 * @Description：（充电、离线充值、投币、钱包、包月、在线卡）订单详情查看
	 * @author： origin 
	 */
	Object orderDataDetail(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 订单详情信息
	 * @author： origin 
	 */
	Object orderDetailData(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 订单详情信息   根据订单号查询
	 * @author： origin 
	 */
	Object orderDetailInfo(HttpServletRequest request);
	
	
}
