package com.hedong.hedongwx.service.impl;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.OfflineCardDao;
import com.hedong.hedongwx.dao.UserDao;
import com.hedong.hedongwx.dao.UserEquipmentDao;
import com.hedong.hedongwx.entity.OfflineCard;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.service.OfflineCardService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Service
public class OfflineCardServiceImpl implements OfflineCardService {
	
	@Autowired
	private OfflineCardDao offlineCardDao;
	@Autowired
	private UserEquipmentDao userEquipmentDao;
	@Autowired
	private UserDao userDao;

	@Override
	public int insertQueryOfflineCardRecord(String ordernum,String equipmentnum,String openid) {
		OfflineCard offlineCard = new OfflineCard();
		UserEquipment equipment = userEquipmentDao.getUserEquipmentByCode(equipmentnum);
		if (equipment == null) {
			offlineCard.setMerchantid(0);
		} else {
			offlineCard.setMerchantid(equipment.getUserId());
		}
		User user = userDao.getUserByOpenid(openid);
		if (user != null) {
			offlineCard.setUid(user.getId());
		} else {
			offlineCard.setUid(1);
		}
		offlineCard.setEquipmentnum(equipmentnum);
		offlineCard.setOrdernum(ordernum);
		offlineCard.setHandletype(2);
		return offlineCardDao.insertOfflineCard(offlineCard);
	}

	@Override
	public int updateQueryOfflineCardRecord(String ordernum, String cardID, double balance,Integer recycletype) {
		OfflineCard offlineCard = selectOfflineCardByOrdernum(ordernum);
		offlineCard.setCardID(cardID);
		offlineCard.setBalance(balance);
		offlineCard.setRecycletype(recycletype);
		return offlineCardDao.updateQuery(offlineCard);
	}

	@Override
	public OfflineCard selectOfflineCardByOrdernum(String ordernum) {
		return offlineCardDao.selectOfflineCardByOrdernum(ordernum);
	}

	@Override
	public int insertChargeMoneyOfflineCardRecord(String ordernum, String equipmentnum, String cardID, 
			double chargemoney,double accountmoney,Integer handletype,Integer paytype,Integer uid,Integer status) {
		OfflineCard offlineCard = new OfflineCard();
		UserEquipment equipment = userEquipmentDao.getUserEquipmentByCode(equipmentnum);
		if (equipment == null) {
			offlineCard.setMerchantid(0);
		} else {
			offlineCard.setMerchantid(equipment.getUserId());
		}
		if (uid == null) {
			offlineCard.setUid(0);
		} else {
			offlineCard.setUid(uid);
		}
		offlineCard.setEquipmentnum(equipmentnum);
		offlineCard.setOrdernum(ordernum);
		offlineCard.setHandletype(handletype);
		offlineCard.setCardID(cardID);
		offlineCard.setChargemoney(chargemoney);
		offlineCard.setAccountmoney(accountmoney);
		offlineCard.setPaytype(paytype);
		offlineCard.setStatus(status);
		return offlineCardDao.insertOfflineCard(offlineCard);
	}

	@Override
	public int updateChargeMoneyOfflineCardRecord(String ordernum, double balance, Integer recycletype,Integer paytype) {
		OfflineCard offlineCard = selectOfflineCardByOrdernum(ordernum);
		offlineCard.setBalance(balance);
		offlineCard.setRecycletype(recycletype);
		offlineCard.setPaytype(paytype);
		return offlineCardDao.updateQuery(offlineCard);
	}
	
	@Override
	public int updateChargeMoneyOfflineCardRecordPaytype(OfflineCard offlineCard) {
		return offlineCardDao.updateQuery(offlineCard);
	}

	@Override
	public int deleteOfflineCardByOrdernum(String ordernum) {
		return offlineCardDao.deleteOfflineCardByOrdernum(ordernum);
	}
	//ＲＺＣ（ＰＣ）　查询订单记录
	@Override
	public PageUtils<OfflineCard> selectOrderInfo(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		
		PageUtils<OfflineCard> page  = new PageUtils<>(numPerPage, currentPage);
		
		OfflineCard offlineCard = new OfflineCard();
		String handletype = request.getParameter("handletype");//操作类型 
		String recycletype = request.getParameter("recycletype");//回复类型 -1 请选择 、0 未绑定、1 已绑定
		String paytype = request.getParameter("paytype");//支付状态（仅扣费协议使用）1成功
		if( null != handletype && !handletype.equals("-1")) offlineCard.setHandletype(Integer.parseInt(handletype));
		if( null != recycletype && !recycletype.equals("-1"))offlineCard.setRecycletype(Integer.parseInt(recycletype));
		if( null != paytype && !paytype.equals("-1"))offlineCard.setRecycletype(Integer.parseInt(paytype));
		offlineCard.setUsername(request.getParameter("username"));
		offlineCard.setDealer(request.getParameter("dealer"));
		offlineCard.setOrdernum(request.getParameter("ordernum"));
		offlineCard.setEquipmentnum(request.getParameter("equipmentnum"));
		offlineCard.setCardID(request.getParameter("cardID"));
		offlineCard.setStartTime(request.getParameter("startTime"));
		offlineCard.setEndTimes(request.getParameter("endTime"));
		List<OfflineCard> equi = offlineCardDao.selectOrderInfo(offlineCard);
		page.setTotalRows(equi.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		offlineCard.setNumPerPage(page.getNumPerPage());
		offlineCard.setStartIndex(page.getStartIndex());
		page.setList( offlineCardDao.selectOrderInfo(offlineCard));
		return page;
	}

	@Override
	public int offlineCardRefund(Integer handletype,Integer id) {
		return offlineCardDao.offlineCardRefund(handletype,id);
	}

	@Override
	public OfflineCard selectOfflineCardById(Integer id) {
		OfflineCard result = offlineCardDao.selectOfflineCardById(id);
		return result = result == null ? new OfflineCard() : result;
	}

	@Override
	public Double getOfflineTodayMoneyByUserid(String begintime, String endtime, String equipmentnum, Integer merchantid,
			String ordernum) {
		return offlineCardDao.getOfflineTodayMoneyByUserid(begintime, endtime, equipmentnum, merchantid, ordernum);
	}

	@Override
	public List<OfflineCard> getOfflineByParma(String begintime, String endtime, String equipmentnum,
			Integer merchantid, String ordernum) {
		return offlineCardDao.getOfflineByParma(begintime, endtime, equipmentnum, merchantid, ordernum);
	}

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	@Override
	public PageUtils<Parameters> selectOfflineRecord(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(null==startTime || startTime.equals(""))startTime = StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss", StringUtil.toDateTime(), 7);
		if(null==endTime || endTime.equals(""))endTime = StringUtil.getCurrentDateTime();
		parameters.setStartTime(startTime);
		parameters.setEndTime(endTime);
		User user = CommonConfig.getAdminReq(request);
		Integer rank = user.getLevel();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(rank!=0) parameters.setUid(user.getId());//获取用户
		parameters.setOrder(request.getParameter("ordernum"));
		parameters.setNickname(request.getParameter("username"));
		parameters.setDealer(request.getParameter("dealer"));
		parameters.setCode(request.getParameter("equipmentnum"));
		parameters.setNumber(request.getParameter("cardID"));
		
		String handletype = request.getParameter("handletype");//操作类型 
		String recycletype = request.getParameter("recycletype");//回复类型 -1 请选择 、0 未绑定、1 已绑定
		String paytype = request.getParameter("paytype");//支付状态（仅扣费协议使用）1成功
		if( null != handletype && !handletype.equals("-1"))	 parameters.setType(handletype);
		if( null != recycletype && !recycletype.equals("-1")) parameters.setSort(recycletype);	
		if( null != paytype && !paytype.equals("0")) parameters.setStatus(paytype); 
		
		List<Map<String, Object>> offline = offlineCardDao.selectOfflineRecord(parameters);
		page.setTotalRows(offline.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		page.setListMap(offlineCardDao.selectOfflineRecord(parameters));
		return page;
	}

	//=========================================================================================
	/**
	 * separate
	 * @Description：离线充值机记录信息查看
	 * @author： origin 
	 */
	@Override
	public Object offlineRecordData(HttpServletRequest request) {
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
				user.setLevel(2);
				user.setId(agentSelectmerid);
			}
			//====================================================
			Parameters parameters = new Parameters();
			Integer rank = CommUtil.toInteger(user.getLevel());
			if(!rank.equals(0)) parameters.setUid(user.getId());//绑定id

			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
//			parameters.setStartTime(CommUtil.getRelevantDate(maparam.get("startTime"), 7, 1));
//			parameters.setEndTime(CommUtil.getRelevantDate(maparam.get("endTime"), 0, 3));

			parameters.setOrder(CommUtil.toString(maparam.get("ordernum")));
			parameters.setNickname(CommUtil.toString(maparam.get("usernick")));
			parameters.setDealer(CommUtil.toString(maparam.get("dealer")));
			parameters.setRealname(CommUtil.toString(maparam.get("realname")));
			parameters.setMobile(CommUtil.toString(maparam.get("mobile")));
			parameters.setCode(CommUtil.toString(maparam.get("devicenum")));
			parameters.setNumber(CommUtil.toString(maparam.get("cardID")));
			parameters.setType(CommUtil.toString(maparam.get("type")));
			parameters.setSort(CommUtil.toString(maparam.get("recycletype")));	
			parameters.setStatus(CommUtil.toString(maparam.get("paytype"))); 
			
			List<Map<String, Object>> offlineData = offlineCardDao.selectOfflineRecord(parameters);
			page.setTotalRows(offlineData.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> offlineinfo = offlineCardDao.selectOfflineRecord(parameters);
			datamap.put("listdata", CommUtil.isListMapEmpty(offlineinfo));
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

	
	@Override
	public OfflineCard gainOfflineData(String cardID) {
		try {
			OfflineCard offlineinfo = offlineCardDao.gainOfflineData(cardID);
			return offlineinfo = offlineinfo == null ? new OfflineCard() : offlineinfo;
		} catch (Exception e) {
			e.printStackTrace();
			return new OfflineCard();
		}
	}


	
	//=========================================================================================
}
