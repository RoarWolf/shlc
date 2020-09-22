package com.hedong.hedongwx.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description： 
 * @author  origin  创建时间：   2019年10月6日 上午10:02:03  
 */
public interface BasicsService {

	//=======================================================================================================

	/**
	 * separate
	 * @Description： 判断手机号是否存在，存在获取并发送验证码信息
	 * @author： origin 
	 */
	Object getCaptchaData(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 判断密码是否正确
	 * @author： origin 
	 */
	Object estimatePassword(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 获取所有商户信息，包含管理员
	 * @author： origin   2019年11月20日 上午9:58:08
	 */
	Object getAllDealerData(HttpServletRequest request);
	
	
	
	
	/**
	 * employ 选择	ORIGIN
	 * @Description：获取设备模板信息
	 * @param 商户模板:tempid、商户id:dealid、设备硬件版本号：version
	 * @author： origin
	 * @return 主模板信息
	 * @createTime：2020年3月21日下午4:04:17
	 */
	Map<String, Object> inquireDeviceTempData(Integer tempid, Integer dealid, String version);

	/**
	 * @Description：获取服务电话
	 * @author： origin 2019年12月26日下午3:34:19
	 */
	String getServicePhone(Integer tempid, Integer areaid, Integer dealid);


	/**
	 * employ 选择	ORIGIN
	 * @Description：获取小区服务电话信息
	 * @param  小区id:areaid
	 * @author： origin 2020年3月21日下午4:04:17
	 */
	String inquireAreaServeTel(Integer areaid);

	/**
	 * employ 选择	ORIGIN
	 * @Description：获取服务电话信息
	 * @param tempPhone:模板电话     managePhone:小区管理电话   servePhone:商户服务电话   phone:商户电话
	 * @return  String类型的服务电话 
	 * @author： origin 2020年3月24日下午5:49:48
	 */
	String getServephoneData(String tempPhone, String managePhone, String servePhone, String phone );

	/**
	 * employ 选择	ORIGIN
	 * @Description：设备获取使用模板
	 * @param tempid:模板id  dealid:商户id  version:版本信息
	 * @author： origin ：2020年5月5日下午7:17:09
	 */
	Map<String, Object> inquireDirectTempData(Integer tempid, Integer dealid, String devicenum, String version);
	

	/**
	 * employ 选择	ORIGIN
	 * @Description：获取除设备模板的其他模板信息
	 * @param  小区id:areaid、商户id:dealid、模板状态：tempstatus
	 * @author： origin
	 * @return 主模板信息
	 * @createTime：2020年3月21日下午4:04:17
	 */
	Map<String, Object> inquireOtherTempData(Integer areaid, Integer dealid, Integer tempstatus);

	/**
	 * employ 选择	ORIGIN
	 * @Description：获取子模板的模板信息
	 * @param 模板:tempid
	 * @return 返回子(下级)模板的集合
	 * @author： origin
	 * @createTime：2020年3月24日下午5:52:10
	 */
	Object inquireSubordinateTempData(Integer tempid);
	
	//=======================================================================================================
}
