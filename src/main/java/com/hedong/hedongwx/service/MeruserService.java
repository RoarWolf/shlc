package com.hedong.hedongwx.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Meruser;

/**
*author：RoarWolf
*createtime：2020年11月12日
*/

public interface MeruserService {

	Map<String,Object> insertMeruser(Meruser meruser);
	
	Map<String,Object> updateMeruser(Meruser meruser);
	
	Map<String,Object> selectMeruserList(Meruser meruser);
	
	/**
	 * 添加桩主和身份证
	 * @param uid
	 * @param province
	 * @param city
	 * @param country
	 * @param realname
	 * @param idcardnum
	 * @param cardimgFront
	 * @param cardimgBack
	 * @param areaname
	 * @return
	 */
	Map<String,Object> insertMeruserAndIdcard(Integer uid, Integer province, Integer city,
			Integer country, String realname, String idcardnum, String cardimgFront, 
			String cardimgBack, String areaname);
	
	/**
	 * 添加银行卡
	 * @param uid
	 * @param bankcardnum
	 * @param bankname
	 * @return
	 */
	Map<String,Object> insertBankcard(Integer uid, String bankcardnum, String bankname);
	
	/**
	 * 查询桩主信息
	 * @param uid
	 * @return
	 */
	Map<String, Object> selectMeruserByUid(Integer uid);
	
	/**
	 * 查询用户操作日志
	 * @param opename
	 * @param objname
	 * @param type
	 * @param source
	 * @param startTime
	 * @param endTime
	 * @param startindex
	 * @param pages
	 * @return
	 */
	Map<String,Object> userOperateRecordlist(String opename,
			String objname,Integer type,Integer source,
			String startTime,String endTime,
			Integer startindex,Integer pages);
	
	/**
	 * 修改操作记录状态
	 * @param opeid
	 * @param type
	 * @return
	 */
	Map<String, Object> updateOperaterecord(Integer id,Integer opeid, Integer type);
	
	Map<String, Object> selectUserBankcardlist(Integer uid,Integer type);
}
