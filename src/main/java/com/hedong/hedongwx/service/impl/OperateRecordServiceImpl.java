package com.hedong.hedongwx.service.impl;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.OperateRecordDao;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.OperateRecordService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.PageUtils;

/**
 * @author  origin
 * 创建时间：   2019年5月24日 下午5:41:29  
 */
@Service
public class OperateRecordServiceImpl implements OperateRecordService{
	
	@Autowired
	private OperateRecordDao operateRecordDao;
	
	/**
	 * separate
	 * @Description： 查询获取操作信息
	 * @author： origin  
	 */
	@Override
	public Object accountOperateInfo(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			User user = CommonConfig.getAdminReq(request);
			//===========================================
			//前端传递代理商名下某一个商家的id
			Integer agentSelectmerid =  CommUtil.toInteger(maparam.get("agentSelectmerid"));
			if(agentSelectmerid != null && !agentSelectmerid.equals(0)){
				user = new User();
				user.setRank(2);
				user.setId(agentSelectmerid);
			}
			//====================================================
			Parameters parameters = new Parameters();
			Integer rank = CommUtil.toInteger(user.getRank());
			if(!rank.equals(0)) parameters.setUid(user.getId());//绑定id
			parameters.setDealer(CommUtil.toString(maparam.get("operator")));
			parameters.setNickname(CommUtil.toString(maparam.get("beoperated")));
			parameters.setRealname(CommUtil.toString(maparam.get("content")));
			//parameters.setType(CommUtil.toString(maparam.get("type.toString()"));
//			parameters.setStartTime(CommUtil.getRelevantDate(maparam.get("startTime"), 7, 1));
//			parameters.setEndTime(CommUtil.getRelevantDate(maparam.get("endTime"), 0, 3));
			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
			List<Map<String, Object>> operate = operateRecordDao.userOperateRecord(parameters);
			page.setTotalRows(operate.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> operateinfo = operateRecordDao.userOperateRecord(parameters);
			datamap.put("listdata", CommUtil.isListMapEmpty(operateinfo));
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
	
	
	
	/**
	 * @Description： 
	 * @author： origin  
	 */
	@Override
	public PageUtils<Parameters> userOperateRecord(HttpServletRequest request){
		Map<String, Object> maparam = CommUtil.getRequestParam(request);
		String dealer =  CommUtil.toString(maparam.get("dealer"));
		String nickname =  CommUtil.toString(maparam.get("nickname"));
		String name =  CommUtil.toString(maparam.get("name"));
		String startTime =  CommUtil.toString(maparam.get("startTime"));
		String endTime =  CommUtil.toString(maparam.get("endTime"));
		Integer type = CommUtil.toInteger(maparam.get("type"));
		Integer numPerPage = CommUtil.toInteger(maparam.get("numPerPage"));
		Integer currentPage = CommUtil.toInteger(maparam.get("currentPage"));
		User user = CommonConfig.getAdminReq(request);
		Integer rank = CommUtil.toInteger(user.getRank());
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters paramet = new Parameters();
		if(!rank.equals(0)) paramet.setUid(user.getId());//绑定id
		paramet.setDealer(dealer);
		paramet.setNickname(nickname);
		paramet.setRealname(name);
		//paramet.setType(type.toString());
		paramet.setStartTime(startTime);
		paramet.setEndTime(endTime);
		List<Map<String, Object>> operate = operateRecordDao.userOperateRecord(paramet);
		page.setTotalRows(operate.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		paramet.setPages(page.getNumPerPage());
		paramet.setStartnumber(page.getStartIndex());
		page.setListMap(operateRecordDao.userOperateRecord(paramet));
		return page;
	}

	/**
	 * @Description： 插入操作记录数据
	 * @author： origin  
	 */
	@Override
	public void insertoperate(String name, Integer opeid, Integer objid, Integer type, Integer source, String remark,
			String common) {
		operateRecordDao.insertoperate( name, opeid, objid, type, source, remark, common);
		
	}

	
	
	
	
	
	
	
}




