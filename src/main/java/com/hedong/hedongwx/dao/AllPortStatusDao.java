package com.hedong.hedongwx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.AllPortStatus;

public interface AllPortStatusDao {

	/**
	 *
	 * @param equipmentnum
	 * @param port
	 * @return
	 */
	AllPortStatus findPortStatusByEquipmentnumAndPort(@Param("equipmentnum")String equipmentnum, @Param("port")Integer port);

	/**
	 *
	 * @param allPortStatus
	 * @return
	 */
	int updateAllPortStatus(AllPortStatus allPortStatus);

	/**
	 *
	 * @param equipmentnum
	 * @param neednum
	 * @return
	 */
	List<AllPortStatus> findPortStatusListByEquipmentnum(@Param("equipmentnum")String equipmentnum,
			@Param("neednum") Integer neednum);

	/**
	 *
	 * @param equipmentnum
	 * @param neednum
	 * @return
	 */
	List<Integer> findPortStatusValListByEquipmentnum(@Param("equipmentnum")String equipmentnum,
			@Param("neednum") Integer neednum);

	/**
	 *
	 * @param allPortStatus
	 * @return
	 */
	int insertPortStatus(AllPortStatus allPortStatus);

	/**
	 *新增设备同步信息
	 * @param allPortStatus
	 * @return
	 */
	int insertPortStatusWeb(AllPortStatus allPortStatus);
	
}
