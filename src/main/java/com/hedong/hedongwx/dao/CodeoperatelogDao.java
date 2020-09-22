package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import com.hedong.hedongwx.entity.Codeoperatelog;
import com.hedong.hedongwx.entity.Parameters;

/**
 * @author  origin
 * 创建时间：   2019年2月25日 下午5:05:47  
 */
public interface CodeoperatelogDao {

	//实体类插入数据
	int insertCodeoperatelog( Codeoperatelog  codeoperatelog);
	//查询设备操作日志 origin   创建时间：2019年2月25日  下午5:14:39
	List<Map<String, Object>> selectoperatelog(Parameters parameters);

}
