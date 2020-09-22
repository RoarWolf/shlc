package com.hedong.hedongwx.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.utils.PageUtils;

public interface AdminiStratorService {

	List<User> deleteAdminiStrator();

	List<User> updateAdminiStrator();

	List<User> insetAdminiStrator();

	List<User> selectAdminList();

	List<Map<String, Object>> selectAdmini(HttpServletRequest request);

	PageUtils<Parameter> selectUserInfos(HttpServletRequest request);

	int updateUserById(User user);

	PageUtils<Parameter> selectGeneralInfo(HttpServletRequest request);

	PageUtils<Parameters> selecwalletdetail(HttpServletRequest request);

	PageUtils<Parameters> selecearningsdetail(HttpServletRequest request);
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询商户信息
	PageUtils<Parameters> selectDealerUserInfo(HttpServletRequest request);
	
	//查询普通用户信息
	PageUtils<Parameters> selectGeneralUserInfo(HttpServletRequest request);

	/**
	 * @Description： 根据商户查询合伙人信息
	 * @author： origin
	 */
	List<User> selectPartnerInfo(HttpServletRequest request);

	/**
	 * @Description： 修改银行卡费率
	 * @author： origin  创建时间：   2019年7月8日 上午9:12:33 
	 */
	Map<String, Object> updateBankRate(Integer bankid, Integer rate);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月23日 下午5:58:53 
	 */
	Object selectPackagemonth(Integer montid, Integer uid);
	
}
