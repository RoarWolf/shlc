package com.hedong.hedongwx.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.OnlineCardDao;
import com.hedong.hedongwx.dao.OnlineCardRecordDao;
import com.hedong.hedongwx.dao.UserDao;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.OnlineCardRecord;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.OnlineCardRecordService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Service
public class OnlineCardRecordServiceImpl implements OnlineCardRecordService {
	
	@Autowired
	private OnlineCardRecordDao onlineCardRecordDao;
	@Autowired
	private OnlineCardDao onlineCardDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private AreaService areaService;
	
	
	@Override
	public List<OnlineCardRecord> selectOnlineCardRecordListByCardID(String cardID) {
		return onlineCardRecordDao.selectOnlineCardRecordListByCardID(cardID);
	}

	public OnlineCardRecord selectRecordByOrdernum(String ordernum, Integer type, Integer flag) {
		return onlineCardRecordDao.selectRecordByOrdernum(ordernum, type, flag);
	}

	@Override
	public int insertOnlineCardRecord(Integer uid,Integer merid,String ordernum,String cardID,String code,Double balance,
		Double money, Double sendmoney, Double accountmoney, Double topupbalance, Double givebalance,Integer type,Integer status,
		Integer flag,Date createTime, Integer operid) {
		OnlineCardRecord onlineCardRecord = new OnlineCardRecord();
		onlineCardRecord.setUid(uid);
		onlineCardRecord.setCardID(cardID);
		onlineCardRecord.setOrdernum(ordernum);
		onlineCardRecord.setCreateTime(createTime);
		onlineCardRecord.setType(type);
		onlineCardRecord.setStatus(status);
		onlineCardRecord.setFlag(flag);
		onlineCardRecord.setBalance(CommUtil.toDouble(balance));
		onlineCardRecord.setMoney(CommUtil.toDouble(money));
		onlineCardRecord.setSendmoney(CommUtil.toDouble(sendmoney));
		onlineCardRecord.setAccountmoney(CommUtil.toDouble(accountmoney));
		onlineCardRecord.setTopupbalance(CommUtil.toDouble(topupbalance));
		onlineCardRecord.setGivebalance(CommUtil.toDouble(givebalance));
		if (merid != null) {
			onlineCardRecord.setMerid(merid);
		}
		if (code != null) {
			onlineCardRecord.setCode(code);
		}
		if (operid != null) {
			onlineCardRecord.setOperid(operid);
		}
		return onlineCardRecordDao.insertOnlineCardRecord(onlineCardRecord);
	}
	
	
	public int updateRecord(OnlineCardRecord onlineCardRecord) {
		return onlineCardRecordDao.updateRecord(onlineCardRecord);
	}

	@Override
	public Integer updateonlinestatus(String ordernum, Byte status) {
		return onlineCardRecordDao.updateonlinestatus(ordernum, status);
	}
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询在线卡操作记录信息
	@Override
	public PageUtils<Parameters> selectOnlineOperation(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		User user = CommonConfig.getAdminReq(request);
		Integer rank = user.getRank();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(rank!=0) parameters.setUid(user.getId());//获取用户（商户）
		
		parameters.setRank(request.getParameter("id"));//id
		parameters.setOrder(request.getParameter("ordernum"));//订单号
		parameters.setNumber(request.getParameter("cardID"));//卡号
		parameters.setCode(request.getParameter("code"));//设备号
		parameters.setStatus(request.getParameter("status"));//状态
		parameters.setNickname(request.getParameter("nickname"));//昵称
		parameters.setPhone(request.getParameter("phone"));//用户电话
		parameters.setDealer(request.getParameter("dealer"));//商户
		parameters.setStartTime(request.getParameter("startTime"));
		parameters.setEndTime(request.getParameter("endTime"));
		List<Map<String, Object>> totalmap = onlineCardRecordDao.selectOnlineOperation(parameters);
		page.setTotalRows(totalmap.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		List<Map<String, Object>> tradepage = onlineCardRecordDao.selectOnlineOperation(parameters);
		page.setListMap(tradepage);
		return page;
	}
	
	//查询在线卡消费记录信息
	@Override
	public PageUtils<Parameters> selectOonlineConsume(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		User user = CommonConfig.getAdminReq(request);
		Integer rank = user.getRank();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(rank!=0) parameters.setUid(user.getId());//获取用户（商户）
				
		parameters.setRank(request.getParameter("id"));//id
		parameters.setOrder(request.getParameter("ordernum"));//订单号
		parameters.setNumber(request.getParameter("cardID"));//卡号
		parameters.setCode(request.getParameter("code"));//设备号
		parameters.setType(request.getParameter("type"));//类型
		parameters.setNickname(request.getParameter("nickname"));//昵称
		parameters.setPhone(request.getParameter("phone"));//用户电话
		parameters.setDealer(request.getParameter("dealer"));//商户
		parameters.setStartTime(request.getParameter("startTime"));
		parameters.setEndTime(request.getParameter("endTime"));
		List<Map<String, Object>> totalmap = onlineCardRecordDao.selectOonlineConsume(parameters);
		page.setTotalRows(totalmap.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		List<Map<String, Object>> tradepage = onlineCardRecordDao.selectOonlineConsume(parameters);
		page.setListMap(tradepage);
		return page;
	}
	
	//添加操作记录
	@Override
	public int additionOnlineCardRecord(OnlineCardRecord onlineCardRecord) {
		return onlineCardRecordDao.insertOnlineCardRecord(onlineCardRecord);
	}
	
	/**
	 * @Description： 根据订单id查询
	 * @author： origin
	 */
	@Override
	public OnlineCardRecord selectOnlineCard(Integer id) {
		return onlineCardRecordDao.selectOnlineCard(id);
	}
	
	/**
	 * @Description： 根据实体类查询
	 * @author： origin 
	 */
	@Override
	public List<OnlineCardRecord> selectCardRecord(OnlineCardRecord onlin) {
		return onlineCardRecordDao.selectCardRecord(onlin);
	}

	/**
	 * @Description： 
	 * @author： origin          
	 * 创建时间：   2019年6月5日 上午11:40:04 
	 */
	@Override
	public List<Map<String, Object>> icCardRecord(String cardNum, Integer merid, String beginTime, String endTime, Integer uid,
			Integer offset) {
		Parameters parame = new Parameters();
		if(merid!=null && !merid.equals(""))parame.setUid(merid);
		if(uid!=null && !uid.equals(""))parame.setStatus(uid.toString());
		parame.setNumber(cardNum);
		parame.setStartTime(beginTime);
		parame.setEndTime(endTime);
		//parame.setStartnumber(offset);
		//parame.setPages(CommonConfig.numerical);
		List<Map<String, Object>> icrecord = onlineCardRecordDao.selectOonlineConsume(parame);
		return icrecord;
	}

	@Override
	public Map<String, Object> scanonlinewecpay( String card) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		OnlineCard online = onlineCardDao.selectOnlineCardByCardID(card);
		codemap = scanVerify( online);
		return codemap;
	}

	@Override
	public Map<String, Object> scanonlinealipay(String card) {
		Map<String, Object> map = new HashMap<String, Object>();
		//OnlineCard online = onlineCardService.selectOnlineCardByCardID(cardNumber);
		OnlineCard online = onlineCardDao.selectOnlineCardByFigure(card);
		map = scanVerify( online);
		if(!map.isEmpty()) return map;
		
		Integer merid = online.getMerid();
		merid = merid == null ? 0 : merid;
		User meruser = userDao.selectUserById(merid);
		map.put("meruser", meruser);
		User users = userDao.selectUserById(online.getUid());
		map.put("users", users);
		map = geticonlinetemp(users, online);
		return map;
	}
	
	public Map<String, Object> scanVerify(OnlineCard online){
		Map<String, Object> map = new HashMap<String, Object>();
		String telphone = null;
		if(online==null){
			map.put("code", 101);
		}else if(online.getUid()==null || online.getUid()==0){
			map.put("code", 101);
		}else if(online.getMoney()>0 && online.getStatus()==0){
			map.put("code", 102);
		}else if( online.getStatus()==2){
			map.put("code", 105);
		}else if( online.getUid()!=0 && online.getStatus()==4){
			map.put("code", 101);
		}
		if(online!=null && online.getUid()!=null){
			Integer merid = online.getMerid();
			if(merid==null || merid==0){
				Integer uid = online.getUid();
				User touuser = userDao.selectUserById(uid);
				if(touuser!=null) merid = touuser.getMerid();
			}
			User meruser = userDao.selectUserById(merid);
			if(meruser!=null) telphone = meruser.getPhoneNum(); 
		}
		map.put("telphone", telphone);
		return map;
	}
	
	/**
	 * @Description： 根据用户信息获取在线卡充值模板
	 * @author： origin          
	 */
	public Map<String, Object> geticonlinetemp(User oriuser, OnlineCard online) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		Integer vawalt = online.getRelevawalt();
		User user = userDao.selectUserById(online.getUid());
		if(user==null){
			codemap.put("code", 102);
			codemap.put("messagecue", "该IC卡绑定的用户不存在");
			return codemap;
		}
		List<TemplateSon> sontemp = null;
		if(vawalt==1){
			List<TemplateParent> parentTemplist = templateService.getParentTemplateListByMerchantid(user.getMerid(), 3);
			if (parentTemplist == null || parentTemplist.size() == 0) {
				parentTemplist = templateService.getParentTemplateListByMerchantid(0, 3);
			}
			sontemp = templateService.getSonTemplateLists(parentTemplist.get(0).getId());
		}else{
			if (online.getAid() != 0) {
				Area area = areaService.selectByIdArea(user.getAid());
				if (area != null && area.getTempid2() != null) {
					sontemp = templateService.getSonTemplateLists(area.getTempid2());
					codemap.put("code", 200);
					codemap.put("sontemp", sontemp);
					return codemap;
				} 
			}
			Integer merid = online.getMerid();
			if (merid == null || merid == 0) merid = user.getMerid();
			List<TemplateParent> parentTemplist = templateService.getParentTemplateListByMerchantid(merid, 4);
			if (parentTemplist == null || parentTemplist.size() == 0) {
				parentTemplist = templateService.getParentTemplateListByMerchantid(0, 4);
			}
			sontemp = templateService.getSonTemplateLists(parentTemplist.get(0).getId());
			codemap.put("pertem", parentTemplist.get(0).getId());
			codemap.put("ptemphone", parentTemplist.get(0).getCommon1());
		}
		codemap.put("code", 200);
		codemap.put("sontemp", sontemp);
		return codemap;
	}

	/**
	 * separate
	 * @Description：在线卡消费记录信息查看
	 * @author： origin 
	 */
	@Override
	public Object onlineCardRecordData(HttpServletRequest request) {
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
			if(!rank.equals(0)) parameters.setUid(user.getId());
			parameters.setOrder(CommUtil.toString(maparam.get("ordernum")));
			parameters.setNickname(CommUtil.toString(maparam.get("usernick")));
			parameters.setNumber(CommUtil.toString(maparam.get("cardnumber")));
			parameters.setDealer(CommUtil.toString(maparam.get("dealer")));
			parameters.setRealname(CommUtil.toString(maparam.get("realname")));
			parameters.setPhone(CommUtil.toString(maparam.get("phone")));
			parameters.setType(CommUtil.toString(maparam.get("type")));
			parameters.setCode(CommUtil.toString(maparam.get("devicenum")));
			parameters.setRank(CommUtil.toString(maparam.get("id")));
//			parameters.setStartTime(CommUtil.getRelevantDate(maparam.get("startTime"), 90, 1));
//			parameters.setEndTime(CommUtil.getRelevantDate(maparam.get("endTime"), 0, 3));
			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
			List<Map<String, Object>> cardRecordData = onlineCardRecordDao.selectOonlineConsume(parameters);
			page.setTotalRows(cardRecordData.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> cardRecordInfo = onlineCardRecordDao.selectOonlineConsume(parameters);
			for(Map<String, Object> item : cardRecordInfo){
				Double paymoney = CommUtil.toDouble(item.get("money"));
				Double sendmoney = CommUtil.toDouble(item.get("sendmoney"));
				Double tomoney = CommUtil.toDouble(item.get("accountmoney"));
				Double balance = CommUtil.toDouble(item.get("balance"));
				Double topupbalance = CommUtil.toDouble(item.get("topupbalance"));
				Double givebalance = CommUtil.toDouble(item.get("givebalance"));
				if(topupbalance.equals(0.00) && givebalance.equals(0.00)){
					topupbalance = balance;
					item.put("topupbalance", topupbalance);
				}
			}
			datamap.put("listdata", CommUtil.isListMapEmpty(cardRecordInfo));
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	/**
	 * separate
	 * @Description：在线卡操作记录信息查看
	 * @author： origin 
	 */
	@Override
	public Object onlineCardOperateRecord(HttpServletRequest request) {
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
			parameters.setOrder(CommUtil.toString(maparam.get("ordernum")));
			parameters.setNickname(CommUtil.toString(maparam.get("usernick")));
			parameters.setDealer(CommUtil.toString(maparam.get("dealer")));
			parameters.setPhone(CommUtil.toString(maparam.get("phone")));
			parameters.setRank(CommUtil.toString(maparam.get("id")));
			parameters.setNumber(CommUtil.toString(maparam.get("cardnumber")));
			parameters.setCode(CommUtil.toString(maparam.get("devicenum")));
			parameters.setStatus(CommUtil.toString(maparam.get("status")));
			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
//			parameters.setStartTime(CommUtil.getRelevantDate(maparam.get("startTime"), 90, 1));
//			parameters.setEndTime(CommUtil.getRelevantDate(maparam.get("endTime"), 0, 3));
			List<Map<String, Object>> operateRecordData = onlineCardRecordDao.selectOnlineOperation(parameters);
			page.setTotalRows(operateRecordData.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> operateRecordInfo = onlineCardRecordDao.selectOnlineOperation(parameters);
			for(Map<String, Object> item : operateRecordInfo){
				Double paymoney = CommUtil.toDouble(item.get("money"));
				Double sendmoney = CommUtil.toDouble(item.get("sendmoney"));
				Double tomoney = CommUtil.toDouble(item.get("accountmoney"));
				Double balance = CommUtil.toDouble(item.get("balance"));
				Double topupbalance = CommUtil.toDouble(item.get("topupbalance"));
				Double givebalance = CommUtil.toDouble(item.get("givebalance"));
				if(topupbalance.equals(0.00) && givebalance.equals(0.00)){
					topupbalance = balance;
					item.put("topupbalance", topupbalance);
				}
			}
			datamap.put("listdata", CommUtil.isListMapEmpty(operateRecordInfo));
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/



	
}
