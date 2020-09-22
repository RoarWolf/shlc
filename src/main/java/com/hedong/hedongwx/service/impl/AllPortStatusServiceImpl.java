package com.hedong.hedongwx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.dao.AllPortStatusDao;
import com.hedong.hedongwx.entity.AllPortStatus;
import com.hedong.hedongwx.service.AllPortStatusService;

@Service
public class AllPortStatusServiceImpl implements AllPortStatusService {
	
	@Autowired
	private AllPortStatusDao allPortStatusDao;

	@Override
	public AllPortStatus findPortStatusByEquipmentnumAndPort(String equipmentnum, Integer port) {
		return allPortStatusDao.findPortStatusByEquipmentnumAndPort(equipmentnum, port);
	}

	@Override
	public int updateAllPortStatus(AllPortStatus allPortStatus) {
		return allPortStatusDao.updateAllPortStatus(allPortStatus);
	}

	@Override
	public List<AllPortStatus> findPortStatusListByEquipmentnum(String equipmentnum,Integer neednum) {
		return allPortStatusDao.findPortStatusListByEquipmentnum(equipmentnum,neednum);
	}

	@Override
	public List<Integer> findPortStatusValListByEquipmentnum(String equipmentnum,Integer neednum) {
		return allPortStatusDao.findPortStatusValListByEquipmentnum(equipmentnum,neednum);
	}

	@Override
	public int insertPortStatus(String equipmentnum,int port) {
		AllPortStatus allPortStatus = new AllPortStatus();
		allPortStatus.setEquipmentnum(equipmentnum);
		allPortStatus.setPort(port);
		allPortStatus.setPortStatus((byte) 1);
		allPortStatus.setTime((short) 0);
		allPortStatus.setPower((short) 0);
		allPortStatus.setElec((short) 0);
		return allPortStatusDao.insertPortStatus(allPortStatus);
	}

}
