package com.hedong.hedongwx.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.AllPortRecordDao;
import com.hedong.hedongwx.entity.AllPortRecord;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AllPortRecordService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Service
public class AllPortRecordServiceImpl implements AllPortRecordService {
	
	@Autowired
	private AllPortRecordDao allPortRecordDao;

	@Override
	public List<AllPortRecord> getAllPortRecordByEquipmentnumAndPort(String equipmentnum, Integer port) {
		return allPortRecordDao.getAllPortRecordByEquipmentnumAndPort(equipmentnum, port);
	}

	@Override
	public PageUtils<AllPortRecord> queryAllLog(HttpServletRequest request) {
		
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		
		PageUtils<AllPortRecord> page  = new PageUtils<>(numPerPage, currentPage);
		AllPortRecord allPortRecord = new AllPortRecord();
		String status = request.getParameter("status");
		String parm = request.getParameter("parm");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(null==parm){
			startTime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
			endTime = StringUtil.toDateTime("yyyy-MM-dd 23:59:59");
		}
		if(null!=status && !status.equals("0") ) allPortRecord.setStatus(StringUtil.getStringInt(status));
		allPortRecord.setStartTime(startTime);
		allPortRecord.setEndTime(endTime);
		
		allPortRecord.setId(StringUtil.getStringInt(request.getParameter("id")));
		allPortRecord.setPort(StringUtil.getStringInt(request.getParameter("port")));

		allPortRecord.setTime(StringUtil.getStringInt(request.getParameter("time")));
		allPortRecord.setPower(StringUtil.getStringInt(request.getParameter("power")));
		allPortRecord.setEquipmentnum(request.getParameter("equipmentnum"));//编号
		allPortRecord.setElec(StringUtil.getStringInt(request.getParameter("elec")));
		
		List<AllPortRecord> equi = allPortRecordDao.queryAllLog(allPortRecord);
		page.setTotalRows(equi.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		allPortRecord.setNumPerPage(page.getNumPerPage());
		allPortRecord.setStartIndex(page.getStartIndex());
		page.setList(allPortRecordDao.queryAllLog(allPortRecord));
		return page;
	}

	@Override
	public int delSevenBeforeLog(String recordTime) {
		return allPortRecordDao.delSevenBeforeLog(recordTime);
	}

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询设备日志信息
	@Override
	public PageUtils<Parameters> selectEquipmentLog(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		User user = CommonConfig.getAdminReq(request);
		Integer rank = user.getRank();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(rank!=0) parameters.setUid(user.getId());//获取用户
		
		String status = request.getParameter("status");
		String parm = request.getParameter("parm");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(null==parm){
			startTime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
			endTime = StringUtil.toDateTime("yyyy-MM-dd 23:59:59");
		}
		if(null!=status && !status.equals("0") ) parameters.setStatus(status);
		parameters.setCode(request.getParameter("equipmentnum"));//设备编号
		parameters.setOrder(request.getParameter("port"));//端口号
		parameters.setStatement(request.getParameter("time"));//剩余时间
		parameters.setNumber(request.getParameter("power"));//实时功率 
		parameters.setRemark(request.getParameter("elec"));//剩余电量
		parameters.setStartTime(startTime);
		parameters.setEndTime(endTime);
		
		List<Map<String, Object>> portMap = allPortRecordDao.selectEquipmentLog(parameters);
		page.setTotalRows(portMap.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		page.setListMap(allPortRecordDao.selectEquipmentLog(parameters));
		return page;
		
	}

	//=== 前后端分离  ===============================================================================
	/**
	 * separate
	 * @Description： 查询设备日志信息
	 * @author： origin 
	 */
	@Override
	public Object inquireDeviceLogData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			User user = CommonConfig.getAdminReq(request);
			Parameters parameters = new Parameters();
			//===========================================
			//前端传递代理商名下某一个商家的id
			Integer agentSelectmerid =  CommUtil.toInteger(maparam.get("agentSelectmerid"));
			if(agentSelectmerid != null && !agentSelectmerid.equals(0)){
				user = new User();
				user.setRank(2);
				user.setId(agentSelectmerid);
			}
			//====================================================
			Integer rank = CommUtil.toInteger(user.getRank());
			if(!rank.equals(0)) parameters.setUid(user.getId());//绑定id
			parameters.setCode(CommUtil.toString(maparam.get("devicenum")));
			
//			Integer status =  CommUtil.toInteger(maparam.get("status"));//状态
//			parameters.setStatus(CommUtil.toString(maparam.get("status")));
					
			parameters.setOrder(CommUtil.toString(maparam.get("port")));//端口号
			parameters.setNumber(CommUtil.toString(maparam.get("power")));//实时功率
			parameters.setRemark(CommUtil.toString(maparam.get("elec")));//剩余时间  

			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
			
			List<Map<String, Object>> portdata = allPortRecordDao.selectEquipmentLog(parameters);
			page.setTotalRows(portdata.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> portinfo = allPortRecordDao.selectEquipmentLog(parameters);
			
			datamap.put("listdata", portinfo);
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

}
