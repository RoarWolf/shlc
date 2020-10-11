package com.hedong.hedongwx.dao;

import com.hedong.hedongwx.entity.EquipmentNew;

public interface EquipmentNewDao {
	
	int insertEquipmentNew(EquipmentNew equipmentNew);
	
	int updateEquipmentNew(EquipmentNew equipmentNew);
	
	String selectDeviceExsit(String devicenum);
}
