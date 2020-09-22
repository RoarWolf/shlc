package com.hedong.hedongwx.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description： 系统设置控制类
 * @author  origin  创建时间：   2019年9月11日 下午4:15:52  
 */
public interface SystemSetService {

	//===================================================================================================
	/**
	 * separate
	 * @Description：系统结算汇总
	 * @author： origin Settlement
	 */
	Object calculateTotalCollect(HttpServletRequest request);

	/**
	 * separate
	 * @Description：系统模板
	 * @author： origin 
	 */
	Object systemTemplateData(HttpServletRequest request);

	/**
	 * separate
	 * @Description：系统结算个人汇总
	 * @author： origin Settlement
	 */
	Object calculateAloneCollect(HttpServletRequest request);

	/**
	 * @Description：获取服务电话
	 * @author： origin
	 * @createTime：2019年12月26日下午3:34:19
	 */
	String getServicePhone(Integer tempid, Integer areaid, Integer dealid);

	/**
	 * @Description：获取服务模板
	 * @author： origin
	 * @createTime：2019年12月26日下午3:34:19
	 */
	Object getServiceTemp(Integer tempid, Integer type, Integer areaid, Integer dealid);
	
	/**
	 * @Description：获取服务模板
	 * @author： origin
	 * @createTime：2019年12月26日下午3:34:24
	 */
	Object getServeTemp(String val, Integer type, Integer areaid, Integer dealid);

	/**
	 * @Description：付款成功通知
	 * @author： origin
	 * @createTime：2019年12月27日下午3:10:13
	 */
	void sendMmessagePaySucceed(String ordernum, Integer orderid, Integer paysource, Integer merid, Integer uid, 
		Double paymoney, String strtime, String devicenum, String target, String devicename, Integer deviceaid, 
		String areaaddress, String servicphone);
	
	/**
	 * @Description：充电付款成功通知
	 * @author： origin
	 * @createTime：2019年12月27日下午3:10:13
	 */
	void sendMmessageCharge(String ordernum, Integer orderid, Integer paysource, Integer merid, Integer uid, 
		Double paymoney, String strtime, String devicenum, String port, String devicename, Integer deviceaid, 
		String areaname, String servicphone);

	/**
	 * @Description：充电结束通知
	 * @author： origin
	 * @createTime：2019年12月28日下午5:28:20
	 */
	void sendMmessChargeEnd(String ordernum, Integer orderid, Integer resultinfo, Integer paysource, Integer merid, 
			Integer uid, Double paymoney, String strtime, String devicenum, String port, String devicename, Integer deviceaid,
			String areaaddress, Integer tempid);

	/**
	 * @Description：根据商户id，判断处理商户近七天的汇总数据
	 * @author： origin
	 * @createTime：2020年4月16日下午4:30:59
	 */
	Object selfmotionCollect(HttpServletRequest request);

	/**
	 * @Description：根据商户id，type类型[1：商户收益  2：单个设备收益处理 3:商户所有设备处理] pastday:之前多少天开始计算到前一天  devicenum:设备号
	 * 				判断处理商户的汇总数据
	 * @author： origin
	 * @createTime：2020年4月26日下午2:34:26
	 */
	Object selfDynamicCollect(Integer merid, Integer type, Integer pastday, String devicenum);

}
