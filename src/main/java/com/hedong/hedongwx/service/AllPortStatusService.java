package com.hedong.hedongwx.service;

import java.util.List;

import com.hedong.hedongwx.entity.AllPortStatus;

public interface AllPortStatusService {

	AllPortStatus findPortStatusByEquipmentnumAndPort(String equipmentnum, Integer port);

	int updateAllPortStatus(AllPortStatus allPortStatus);
	
	List<AllPortStatus> findPortStatusListByEquipmentnum(String equipmentnum,Integer neednum);
	
	List<Integer> findPortStatusValListByEquipmentnum(String equipmentnum,Integer neednum);
	
	int insertPortStatus(String equipment,int port);
}
