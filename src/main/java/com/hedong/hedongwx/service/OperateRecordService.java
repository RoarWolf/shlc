package com.hedong.hedongwx.service;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.utils.PageUtils;

/**
 * @author  origin
 * 创建时间：   2019年5月24日 下午5:41:03  
 */
public interface OperateRecordService {

	/**
	 * @Description： 查询操作记录数据
	 * @author： origin  
	 */
	PageUtils<Parameters> userOperateRecord(HttpServletRequest request);

	/**
	 * @Description： 插入操作记录数据
	 * @author： origin  
	 */
	void insertoperate(String name, Integer opeid, Integer objid, Integer type, Integer source, String remark, String common);

	/**
	 * separate
	 * @Description： 查询获取操作信息
	 * @author： origin  
	 */
	Object accountOperateInfo(HttpServletRequest request);

}
