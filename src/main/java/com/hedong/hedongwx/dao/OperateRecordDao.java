package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Parameters;

/**
 * @author  origin
 * 创建时间：   2019年5月24日 下午5:45:41  
 */
public interface OperateRecordDao {

	/**
	 * @Description： 查询操作记录数据
	 * @author： origin  
	 */
	List<Map<String, Object>> userOperateRecord(Parameters paramet);

	/**
	 * @Description： 插入操作记录数据
	 * @author： origin  
	 */
	void insertoperate(@Param("name")String name, @Param("opeid")Integer opeid, @Param("objid")Integer objid,
			@Param("type")Integer type, @Param("source")Integer source, @Param("remark")String remark, @Param("common")String common);

}
