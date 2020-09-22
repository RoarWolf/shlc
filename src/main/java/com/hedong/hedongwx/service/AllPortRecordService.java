package com.hedong.hedongwx.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.AllPortRecord;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.utils.PageUtils;

public interface AllPortRecordService {

	List<AllPortRecord> getAllPortRecordByEquipmentnumAndPort(String equipmentnum,Integer port);

	PageUtils<AllPortRecord> queryAllLog(HttpServletRequest request);
	
	int delSevenBeforeLog(String recordTime);
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询设备日志信息
	PageUtils<Parameters> selectEquipmentLog(HttpServletRequest request);

	//=== 前后端分离  ===============================================================================
	/**
	 * separate
	 * @Description： 查询设备日志信息
	 * @author： origin 
	 */
	Object inquireDeviceLogData(HttpServletRequest request);
	
	
	
}
