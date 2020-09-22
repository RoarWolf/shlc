package com.hedong.hedongwx.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.dao.LostOrderDao;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.service.LostOrderService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Service
public class LostOrderServiceImpl implements LostOrderService{
	@Autowired
	private LostOrderDao lostOrderDao;

	@Override
	public void insertLostListOrder(List<Map<String, Object>> listcontent) {
		try {
			lostOrderDao.insertLostListOrder(listcontent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> inquireListLostInfo(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			
			int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
			int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
			
			PageUtils<Map<String, Object>> page  = new PageUtils<>(numPerPage, currentPage);
			
			String nickname =  CommUtil.toString(maparam.get("nick"));//用户昵称
			String username =  CommUtil.toString(maparam.get("username"));//用户姓名
			String phone =  CommUtil.toString(maparam.get("phone"));//用户电话
			String order =  CommUtil.toString(maparam.get("order"));//商户订单号
			String number =  CommUtil.toString(maparam.get("number"));//微信订单号
			String state =  CommUtil.toString(maparam.get("state"));//交易状态
			
			String time =  CommUtil.toString(maparam.get("time"));//查询时间
			String startTime = null;
			String endTime = null;
			if(time!=null){
				startTime = time + " 00:00:00";//查询起始时间
				endTime = time + " 23:59:59";//查询结束时间
			}
//			String startTime =  CommUtil.toString(maparam.get("startTime"));//查询起始时间
//			String endTime =  CommUtil.toString(maparam.get("endTime"));//查询结束时间
			
			Parameters parame = new Parameters();
			parame.setNickname(nickname);
			parame.setUsername(username);
			parame.setPhone(phone);
			parame.setNumber(number);
			parame.setOrder(order);
			parame.setState(state);
			parame.setStartTime(startTime);
			parame.setEndTime(endTime);
			List<Map<String, Object>> result = lostOrderDao.inquireListLostInfo(parame);
			page.setTotalRows(result.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parame.setPages(page.getNumPerPage());
			parame.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> resultinfo = lostOrderDao.inquireListLostInfo(parame);
			
			datamap.put("listdata", CommUtil.isListMapEmpty(resultinfo));
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	@Override
	public Map<String, Object> updataLostRefundOrder(Integer id, String outrefundno, String wxrefundno, String refundfeemoney) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			String RefundType = "ORIGINAL"; 
			String RefundStatus = "SUCCESS";
			lostOrderDao.updataLostRefundOrder(id, outrefundno, wxrefundno, refundfeemoney, RefundType, RefundStatus);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
}
