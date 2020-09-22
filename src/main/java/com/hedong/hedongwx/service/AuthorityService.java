package com.hedong.hedongwx.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.DealerAuthority;

/**
 * @Description： 
 * @author  origin  创建时间：   2019年7月8日 下午4:00:39  
 */
public interface AuthorityService {

	/**
	 * @Description： 开启指定商户某个消息权限（如果不存在添加之后开启）
	 * @author： origin 创建时间：   2019年7月8日 下午4:03:54 
	 * @param merid:商户id   switchs:开关开闭    status:类型(指定对象)
	 */
	Map<String, Object> messAuthority( Integer merid, Integer switchs, Integer status);

	/**
	 * @Description：  开启指定商户某个消息权限（如果不存在添加之后开启）
	 * @author： origin 创建时间：   2019年7月8日 下午4:31:55 
	 * @param incoinrefund 
	 */
	Map<String, Object> messSwitchAuthority(HttpServletRequest request);

	/**
	 * @Description： 查询商户消息权限
	 * @author： origin 创建时间：   2019年7月8日 下午5:11:29 
	 */
	DealerAuthority selectMessSwitch(Integer merid);
	
	/** 根据商户id获取对应的商户消息权限信息 */
	DealerAuthority getAuthorList(Integer merid);
	
	/** 判断对应的商户消息权限信息是否存在   */
	boolean existSwitch(Integer merid);

	/** 判断对应的商户消息权限信息是否存在 */
	boolean existSwitchList(Integer merid);
	
	Integer selectIncoinRefund(Integer merid);
	
	
	Boolean authorSwitch(Integer merid, Integer type);

	/**
	 * separate
	 * @Description：开启指定商户某个消息权限（如果不存在添加之后开启）
	 * @author： origin   2019年11月8日 上午9:57:32 
	 */
	Object dealerauthoritySeting(HttpServletRequest request);
	
	
	
	
	

}
