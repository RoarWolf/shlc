package com.hedong.hedongwx.dao;

import com.hedong.hedongwx.entity.EquipmentNew;
import com.hedong.hedongwx.entity.Parameters;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

public interface EquipmentNewDao {
	
	int insertEquipmentNew(EquipmentNew equipmentNew);
	
	int updateEquipmentNew(EquipmentNew equipmentNew);
	
	String selectDeviceExsit(String devicenum);

	//查询设备列表 (跳转设备列表)
	List<Map<String, Object>> selectEquList(Parameters parameters);
	
	EquipmentNew selectDeviceInfo(String code);
	//删除设备信息
	int delEquipmentNewById(String code);

}
