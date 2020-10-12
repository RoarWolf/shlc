package com.hedong.hedongwx.dao;

import com.hedong.hedongwx.entity.EquipmentNew;
import com.hedong.hedongwx.entity.Parameters;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface EquipmentNewDao {
	
	int insertEquipmentNew(EquipmentNew equipmentNew);
	
	int updateEquipmentNew(EquipmentNew equipmentNew);
	
	String selectDeviceExsit(@Param("devicenum")String devicenum);

	//查询设备列表 (跳转设备列表)
	List<Map<String, Object>> selectEquList(Parameters parameters);
}
