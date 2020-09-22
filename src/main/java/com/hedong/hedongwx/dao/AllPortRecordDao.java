package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.AllPortRecord;
import com.hedong.hedongwx.entity.Parameters;

public interface AllPortRecordDao {

	/**
	 * 
	 * @param equipmentnum
	 * @param port
	 * @return
	 */
	List<AllPortRecord> getAllPortRecordByEquipmentnumAndPort(@Param("equipmentnum") String equipmentnum, @Param("port") Integer port);

	/**
	 *
	 * @param allPortRecord
	 * @return
	 */
	List<AllPortRecord> queryAllLog(AllPortRecord allPortRecord);

	/**
	 *
	 * @param recordTime
	 * @return
	 */
	int delSevenBeforeLog(String recordTime);

	/**
	 * 查询设备日志信息
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> selectEquipmentLog(Parameters parameters);

}
