package com.hedong.hedongwx.service.impl;

import java.util.ArrayList;
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
import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.OnlineCardRecord;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.OnlineCardRecordService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.OperateRecordService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Service
public class OnlineCardServiceImpl implements OnlineCardService {
	
	@Autowired
	private OnlineCardDao onlineCardDao;
	@Autowired
	private OnlineCardRecordDao onlineCardRecordDao;
	@Autowired
	private OnlineCardRecordService onlineCardRecordService;
	@Autowired
	private OperateRecordService operateRecordService;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest request;

	@Override
	public int insertOnlineCard(OnlineCard onlineCard) {
		return onlineCardDao.insertOnlineCard(onlineCard);
	}

	@Override
	public int updateOnlineCard(OnlineCard onlineCard) {
		return onlineCardDao.updateOnlineCard(onlineCard);
	}

	@Override
	public List<OnlineCard> selectOnlineCardList(Integer uid) {
		return onlineCardDao.selectOnlineCardList(uid);
	}

	@Override
	public OnlineCard selectOnlineCardById(Integer id) {
		return onlineCardDao.selectOnlineCardById(id);
	}
	
	public OnlineCard selectOnlineCardByCardID(String cardID) {
		OnlineCard result = onlineCardDao.selectOnlineCardByCardID(cardID);
		return result = result == null ? new OnlineCard() : result ;
	}

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询在线卡信息
	@Override
	public PageUtils<Parameters> selectOnlinecard(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		User user = CommonConfig.getAdminReq(request);
		Integer rank = user.getRank();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(rank!=0) parameters.setUid(user.getId());//获取用户
		parameters.setCode(request.getParameter("cardnumber"));//卡号
		parameters.setNumber(request.getParameter("figure"));
		parameters.setNickname(request.getParameter("nickname"));//用户
		parameters.setDealer(request.getParameter("dealer"));//商户
		parameters.setStatus(request.getParameter("status"));//状态
		parameters.setUsername(request.getParameter("areaname"));//小区名
		parameters.setRemark(request.getParameter("remark"));//备注
		parameters.setStartTime(request.getParameter("startTime"));
		parameters.setEndTime(request.getParameter("endTime"));
		List<Map<String, Object>> totalmap = onlineCardDao.selectOnlinecard(parameters);
		page.setTotalRows(totalmap.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		List<Map<String, Object>> tradepage = onlineCardDao.selectOnlinecard(parameters);
		page.setListMap(tradepage);
		return page;
	}
	
	//卡操作——删除	   物理删除（删除该条数据）
	@Override
	public int removecardbyId(Integer id) {
		return onlineCardDao.removecardbyId(id);
	}
	
	//根据Id解除绑定用户
	@Override
	public int updateOnlineCarduIdbyId(Integer id) {
		return onlineCardDao.updateOnlineCarduIdbyId(id);
	}

	/**
	 * @Description：根据用户id查询用户名下的所有在线卡信息
	 * @author： origin   创建时间：   2019年7月11日 下午11:59:50 
	 */
	@Override
	public List<Map<String, Object>> selectonlinecardbyuid(Integer uid) {
		List<Map<String, Object>> usonlinecard = onlineCardDao.selectonlinecardbyuid(uid);
		return usonlinecard;
	}

	/**
	 * @Description： 根据卡号修改信息
	 * @author： origin  
	 */
	@Override
	public int updateOnlineCardBycard(OnlineCard oncard) {
		return onlineCardDao.updateOnlineCardBycard(oncard);
	}

	@Override
	public int insertOnline(OnlineCard sourcecard) {
		return onlineCardDao.insertOnline( sourcecard);
	}

	/**
	 * @Description： 根据条件查询在线卡信息
	 * @author： origin          
	 * 创建时间：   2019年5月31日 下午4:20:32 
	 */
	@Override
	public List<OnlineCard> onlineCardList(Integer merid, String onlinecard) {
		OnlineCard online = new OnlineCard();
		online.setMerid(merid);
		if(onlinecard != null) online.setCardID(onlinecard);
		List<OnlineCard> onlinelist = onlineCardDao.selectCardList(online);
		for(OnlineCard item : onlinelist){
			Integer rele = CommUtil.toInteger(item.getRelevawalt());
			Integer uid = CommUtil.toInteger(item.getUid());
			User touser = userService.selectUserById(uid);
			if(rele==1) {
				item.setMoney(touser.getBalance());
			}
		}
		return onlinelist;
	}
	/**
	 * @Description： 扫码绑定在线卡 	  
	 * @author： origin    2019年6月19日 下午2:49:02 
	 */
	@Override
	public Map<String, Object> scanbinding( Integer relevawalt, String cardNum) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		try {
			if (user==null) return CommUtil.responseBuildInfo(400, "用户信息获取不到,无法绑定,请重新扫描", datamap);
			relevawalt = CommUtil.toInteger(relevawalt);
			if(relevawalt!=1 && relevawalt!=2){
				datamap.put("cardNum", cardNum);
				datamap.put("relevawalt", relevawalt);
				return CommUtil.responseBuildInfo(400, "绑定参数传递不正确,无法绑定,请重新扫描或联系管理员", datamap);
			}
			Integer touruid = CommUtil.toInteger(user.getId());
			Integer touraid = CommUtil.toInteger(user.getAid());
			Integer tourdealid = CommUtil.toInteger(user.getMerid());
			String figure;
			if(cardNum.length()==10){
				figure = cardNum;
				cardNum = CommUtil.toForHex(CommUtil.toInteger(cardNum));
			}else if(cardNum.length()==8){
				figure = cardNum;
			}else{
				return CommUtil.responseBuildInfo(405, "在线卡输入格式不正确！", datamap);
			}
			OnlineCard findCard = onlineCardDao.selectOnlineCardByCardID(cardNum);
			findCard = findCard == null ? new OnlineCard() : findCard;
			Double cardtopupmoney = CommUtil.toDouble(findCard.getMoney());//充值金额
			Double cardsendmoney = CommUtil.toDouble(findCard.getSendmoney());//赠送金额
			Double cardaccountmoney = CommUtil.addBig(cardtopupmoney, cardsendmoney);//账户金额
			Integer cardid = CommUtil.toInteger(findCard.getId());
			Integer carddealid = CommUtil.toInteger(findCard.getMerid());
			Integer carduid = CommUtil.toInteger(findCard.getUid());
			Integer cardaid = CommUtil.toInteger(findCard.getAid());
			if(carddealid.equals(0))  return CommUtil.responseBuildInfo(201, "在线卡未绑定商户，不能操作", datamap);
			if(relevawalt==1){//关联钱包时处理用户信息
				User touuser = new User();
				touuser.setId(user.getId());
				if(tourdealid.equals(0)){
					touuser.setMerid(carddealid);
				}else if(!tourdealid.equals(carddealid)){
					return CommUtil.responseBuildInfo(203, "在线卡商户与钱包商户不统一，不能关联钱包！", datamap);
				}
				if(touraid.equals(0) && !cardaid.equals(0)){
					touuser.setAid(cardaid);
				}else if(!touraid.equals(cardaid)){
					return CommUtil.responseBuildInfo(203, "在线卡所属小区与钱包所属小区不一致，不能关联钱包！", datamap);
				}
				if(touuser.getMerid()!=null || touuser.getAid()!=null){
					userService.updateUserById(touuser);
				}
				if(cardaccountmoney>0){
					return CommUtil.responseBuildInfo(406, "在线卡存在金额，不能与钱包关联！请联系商户处理。", datamap);
				}
			}
			Date date = new Date();
			cardNum = cardNum.toUpperCase();
			if(carduid.equals(0)) {//没有人绑定
				OnlineCard onlineCard = new OnlineCard();
				onlineCard.setId(cardid);
				onlineCard.setUid(touruid);
//				if(carddealid.equals(0)) onlineCard.setMerid(tourdealid);
//				onlineCard.setCardID(cardNum);
//				onlineCard.setFigure(figure);
				onlineCard.setRelevawalt(relevawalt);
				onlineCard.setCreateTime(date);
				onlineCardDao.updateOnlineCard(onlineCard);
				onlineCardRecordService.insertOnlineCardRecord(carduid, carddealid, HttpRequest.createOrdernum(6), cardNum, null, null, null, null, null, null, null, 4, 3, 1, date, touruid);
				CommUtil.responseBuildInfo(200, "在线卡绑定成功。", datamap);
			}else{
				CommUtil.responseBuildInfo(101, "该在线卡已经被其他用户绑定！", datamap);
			}
			return datamap;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**
	 * @Description：
	 * @param tourid
	 * @param cardnum
	 * @return
	 * @author： origin
	 * @createTime：2020年3月25日下午6:21:31
	 * @comment:
	 */
	@Override
	public Map<String, Object> bindingOnlineByUid(Integer tourid, String cardnum) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		try {
			String figure;
			if(cardnum.length()==10){
				figure = cardnum;
				cardnum = CommUtil.toForHex(CommUtil.toInteger(cardnum));
			}else if(cardnum.length()==8){
				figure = cardnum;
			}else{
				return CommUtil.responseBuildInfo(405, "在线卡输入格式不正确！", datamap);
			}
			OnlineCard findCard = onlineCardDao.selectOnlineCardByCardID(cardnum);
			findCard = findCard == null ? new OnlineCard() : findCard;
			Integer cuid = CommUtil.toInteger(findCard.getId());
			Integer dealid = CommUtil.toInteger(findCard.getMerid());
			Integer uid = CommUtil.toInteger(findCard.getUid());
			Date date = new Date();
			cardnum = cardnum.toUpperCase();
			if(cuid.equals(0)){//数据库不存在时
				OnlineCard onlineCard = new OnlineCard();
				onlineCard.setUid(tourid);
				onlineCard.setCardID(cardnum);
				onlineCard.setFigure(figure);
				onlineCard.setRelevawalt(2);
				onlineCard.setCreateTime(date);
				onlineCardDao.insertEntiyOnline(onlineCard);
				onlineCardRecordService.insertOnlineCardRecord(tourid, null, HttpRequest.createOrdernum(6), cardnum, null, null, null, null, null, null, null, 4, 3, 1, date,user.getId());
				CommUtil.responseBuildInfo(200, "在线卡绑定成功！", datamap);
			}else if(uid.equals(0)) {//没有人绑定
				OnlineCard onlineCard = new OnlineCard();
				onlineCard.setId(findCard.getId());
				onlineCard.setUid(tourid);
				onlineCard.setCardID(cardnum);
				onlineCard.setFigure(figure);
				if(dealid.equals(0)) onlineCard.setStatus(0);
				onlineCard.setRelevawalt(2);
				onlineCard.setCreateTime(date);
				onlineCardDao.updateOnlineCard(onlineCard);
				onlineCardRecordService.insertOnlineCardRecord(tourid, null, HttpRequest.createOrdernum(6), cardnum, null, null, null, null, null, null, null, 4, 3, 1, date,user.getId());
				CommUtil.responseBuildInfo(200, "在线卡绑定成功。", datamap);
			}else{
				CommUtil.responseBuildInfo(101, "该在线卡已经被其他用户绑定！", datamap);
			}
			return datamap;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**
	 * @Description： 扫码时修改卡号
	 * @author： origin          
	 */
	@Override
	public Map<String, Object> scanamendonline( Integer relevawalt, String oricard, String nowcard) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		try {
			if (user == null) return CommonConfig.messg(400);
			Integer uid = user.getId();
			//OnlineCard onoriCard = onlineCardDao.selectOnlineCardByCardID(oricard);
			OnlineCard onoriCard = onlineCardDao.selectOnlineCardByFigure(oricard);
			if (onoriCard == null) {
				codemap.put("code", 103);
				return codemap;
			}else if(onoriCard.getStatus()==1){
				codemap.put("code", 104);
				return codemap;
			}
			Integer statu = onoriCard.getStatus();
			String figure;
			nowcard = nowcard.toUpperCase();
			if(nowcard.length()==10){
				figure = nowcard;
				nowcard = CommUtil.toForHex(CommUtil.toInteger(nowcard));
			}else if(nowcard.length()==8){
				figure = nowcard;
			}else{
				codemap.put("code", 105);
				return codemap;
			}
			Integer orid = onoriCard.getId();
			OnlineCard online =  onoriCard;
			
			Double cardaccountmoney = CommUtil.toDouble(onoriCard.getMoney());
			Double cardsendmoney = CommUtil.toDouble(onoriCard.getSendmoney());
			Double cardmoney = CommUtil.addBig(cardaccountmoney, cardsendmoney);
			Double topupbalance = cardaccountmoney;
			Double givebalance = cardsendmoney;
			
			onoriCard.setCardID(nowcard);
			onoriCard.setFigure(figure);
			if(statu==2)onoriCard.setStatus(1);
			onoriCard.setCreateTime(new Date());
			OnlineCard onnowCard = onlineCardDao.selectOnlineCardByCardID(nowcard);
			if (onnowCard != null) {
				if(onnowCard.getUid()!=0 || onnowCard.getUid()==null){
					codemap.put("code", 106);
					return codemap;
				}
				onoriCard.setId(onnowCard.getId());
				onlineCardDao.updateOnlineCard(onoriCard);//根据id更新存在的数据
			}else{
				onoriCard.setId(null);
				onlineCardDao.insertEntiyOnline(onoriCard);//根据实体类添加
			}
			onlineCardRecordService.insertOnlineCardRecord(uid, online.getMerid(), HttpRequest.createOrdernum(6), oricard, 
					nowcard, cardmoney, 0.00, 0.00, 0.00, topupbalance, givebalance, 4, 7, null, new Date(),user.getId());
			online.setId(orid);
			online.setUid(0);
			online.setMerid(0);
			online.setStatus(4);
			online.setMoney(0.00);
			online.setSendmoney(0.00);
			online.setRelevawalt(1);
			onlineCardDao.updateOnlineCard(online);//根据id更新
			codemap.put("code", 200);
			return codemap;
		} catch (Exception e) {
			codemap.put("code", 403);
			return codemap;
		}
	}

	@Override
	public  Map<String, Object> mercVirtualOnlinePay(User user, Double querymoney, Double sendmoney, Integer id, Integer status) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			if(user == null || CommUtil.toInteger(user.getId()).equals(0)){
				return CommUtil.responseBuildInfo(102, "获取用户信息为空或不正确", datamap);
			}
			OnlineCard online = onlineCardDao.selectOnlineCardById(id);
			if(online == null){
				return CommUtil.responseBuildInfo(102, "在线卡信息获取不存在", datamap);
			}
			online = online == null ? new OnlineCard() : online;
			Integer dealid = CommUtil.toInteger(online.getMerid());
			String ordernum = HttpRequest.createOrdernum(6);
			Double givemoney = CommUtil.toDouble(online.getSendmoney());
			Double balancemoney = CommUtil.toDouble(online.getMoney());
			
			querymoney = CommUtil.toDouble(querymoney);
			sendmoney = CommUtil.toDouble(sendmoney);
			givemoney = CommUtil.addBig(givemoney, sendmoney);
			if(givemoney<0){
				sendmoney = CommUtil.toDouble(0 - CommUtil.toDouble(online.getSendmoney()));
				givemoney = 0.00;
			}
			balancemoney = CommUtil.addBig(balancemoney, querymoney);
			if(balancemoney<0){
				querymoney = CommUtil.toDouble(0 - CommUtil.toDouble(online.getMoney()));
				balancemoney = 0.00;
			}
			Double paymoney = CommUtil.addBig(querymoney, sendmoney);
			Double balance = CommUtil.addBig(givemoney, balancemoney);
			
			Date time = new Date();
			online.setMoney(balancemoney);
			online.setSendmoney(givemoney);
			onlineCardDao.updateOnlineCard(online);
			operateRecordService.insertoperate( "在线卡虚拟充值", user.getId(), online.getUid(), 2, 5, ordernum,null);
			onlineCardRecordService.insertOnlineCardRecord(online.getUid(), user.getId(), ordernum,
					online.getCardID(), online.getCardID(), balance, querymoney, sendmoney,  paymoney, balancemoney, givemoney, 8, 1, 1, time, user.getId());
			
			tradeRecordService.insertTrade(dealid, online.getUid(), ordernum, paymoney, 0.00, online.getCardID(), 5, 6, 1, "在线卡虚拟充值");
//			generalDetailService.insertGenWalletDetail(touruser.getId(), user.getMerid(), money, 
//					balance, ordernum, time, 7);
			datamap.put("type", 2);
			datamap.put("ordernum", ordernum);
			datamap.put("uid", CommUtil.toString(online.getUid()));
			datamap.put("cardID", CommUtil.toString(online.getCardID()));
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	/**
	 * 虚拟充值退款
	 */
	@Override
	public Map<String, Object> mercVirtualReturn(Integer id) {
		try {
			OnlineCardRecord onlineRecord = onlineCardRecordService.selectOnlineCard(id);
			OnlineCard online = onlineCardDao.selectOnlineCardByCardID(onlineRecord.getCardID());
			
			Double paymoney = CommUtil.toDouble(onlineRecord.getMoney());//充值金额
			Double sendmoney = CommUtil.toDouble(onlineRecord.getSendmoney());//赠送金额
			Double accountmoney = CommUtil.toDouble(onlineRecord.getAccountmoney());//到账金额

			Double cardaccountmoney = CommUtil.toDouble(online.getMoney());
			Double cardsendmoney = CommUtil.toDouble(online.getSendmoney());
			
			Double topupbalance = CommUtil.subBig(cardaccountmoney, paymoney);
			Double givebalance = CommUtil.subBig(cardsendmoney, sendmoney);
//			Double topupbalance = CommUtil.addBig(cardaccountmoney, paymoney);
//			Double givebalance = CommUtil.addBig(cardsendmoney, sendmoney);
			
			if(topupbalance<0){
				topupbalance = 0.00;
				paymoney = CommUtil.toDouble(0 - cardaccountmoney);
			}
			if(givebalance<0){
				givebalance = 0.00;
				sendmoney = CommUtil.toDouble(0 - cardsendmoney);
			}
			Double calcMoney = CommUtil.addBig(paymoney, sendmoney);
			Double balance = CommUtil.addBig(topupbalance, givebalance);
			Date time = new Date();
			online.setMoney(topupbalance);
			online.setSendmoney(givebalance);
			onlineCardDao.updateOnlineCard(online);
			Integer merid = CommUtil.toInteger(onlineRecord.getMerid());
			String order = CommUtil.toString(onlineRecord.getOrdernum());
			operateRecordService.insertoperate( "在线卡虚拟充值退费", merid, online.getUid(), 2, 5, order,null);
			onlineRecord.setFlag(2);
			onlineCardRecordService.updateRecord(onlineRecord);
			onlineCardRecordService.insertOnlineCardRecord(online.getUid(),merid, order,online.getCardID(), 
					online.getCardID(), balance, paymoney, sendmoney, calcMoney, topupbalance, givebalance, 9, 1, 2, time, online.getUid());
			tradeRecordService.insertTrade(merid, online.getUid(), order, calcMoney, 0.00, online.getCardID(), 5, 6, 2, "在线卡虚拟充值退费");
			return CommonConfig.messg(200);
//			Double maney = (online.getMoney()*100 - onlineRecord.getAccountmoney()*100)/100;
//			Double maney = CommUtil.subBig(CommUtil.toDouble(online.getMoney()), onlineRecord.getMoney());
//			Double sendmaney = CommUtil.subBig(CommUtil.toDouble(online.getSendmoney()), onlineRecord.getSendmoney());
//			Date time = new Date();
//			online.setMoney(maney);
//			online.setSendmoney(sendmaney);
//			onlineCardDao.updateOnlineCard(online);
//			Integer merid = CommUtil.toInteger(onlineRecord.getMerid());
//			String order = CommUtil.toString(onlineRecord.getOrdernum());
//			Double paymoney = CommUtil.toDouble(onlineRecord.getMoney());
//			Double accountmoney = CommUtil.toDouble(onlineRecord.getAccountmoney());
//			Double sendmoney = CommUtil.toDouble(onlineRecord.getSendmoney());
//			Double balance = CommUtil.addBig(maney, sendmaney);
//			operateRecordService.insertoperate( "在线卡虚拟充值退费", merid, online.getUid(), 2, 5, order,null);
//			onlineRecord.setFlag(2);
//			onlineCardRecordService.updateRecord(onlineRecord);
//			onlineCardRecordService.insertOnlineCardRecord(online.getUid(),merid, order,online.getCardID(), 
//					online.getCardID(), balance, paymoney, sendmoney, accountmoney, 9, 1, 2, time, online.getUid());
//			tradeRecordService.insertTrade(merid, online.getUid(), order, accountmoney, 0.00, online.getCardID(), 5, 6, 2, "在线卡虚拟充值退费");
//			return CommonConfig.messg(200);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonConfig.messg(403);
		}
	}

	@Override
	public OnlineCard selectOnlineCardByFigure(String card) {
		return onlineCardDao.selectOnlineCardByFigure(card);
	}
	
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	// ==========================================================================================
	@Override
	public  Map<String, Object> virtualTopOnline(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			User user = CommonConfig.getAdminReq(request);
			Integer id =  CommUtil.toInteger(maparam.get("id"));
			Double money =  CommUtil.toDouble(maparam.get("money"));
			Double sendmoney =  CommUtil.toDouble(maparam.get("sendmoney"));
			OnlineCard online = onlineCardDao.selectOnlineCardById(id);
			String ordernum = HttpRequest.createOrdernum(6);

			Double onlinemoney = CommUtil.addBig(CommUtil.toDouble(online.getMoney()), money);
			Double onlinesendmoney = CommUtil.addBig(CommUtil.toDouble(online.getSendmoney()), sendmoney);
			//充值负数金额时判断
			if(onlinemoney<0){
				money = CommUtil.toDouble(0 - CommUtil.toDouble(online.getMoney()));
				onlinemoney = 0.00;
			}
			if(onlinesendmoney<0){
				sendmoney = CommUtil.toDouble(0 - CommUtil.toDouble(online.getSendmoney()));
				onlinesendmoney = 0.00;
			}
			Double accountmoney = CommUtil.addBig(money, sendmoney);
			Double balance = CommUtil.addBig(onlinemoney, onlinesendmoney);
			Date time = new Date();
			online.setMoney(onlinemoney);
			online.setSendmoney(onlinesendmoney);
			onlineCardDao.updateOnlineCard(online);
			operateRecordService.insertoperate( "在线卡虚拟充值", user.getId(), online.getUid(), 2, 5, ordernum,null);
			onlineCardRecordService.insertOnlineCardRecord(online.getUid(), user.getId(), ordernum, online.getCardID(), online.getCardID(), 
					balance, money, sendmoney, accountmoney, onlinemoney, onlinesendmoney, 8, 1, 1, time, user.getId());
			tradeRecordService.insertTrade(user.getMerid(), online.getUid(), ordernum, accountmoney, 0.00,
					online.getCardID(), 5, 6, 1, "在线卡虚拟充值");
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * separate
	 * @Description： 查询用户在线卡信息
	 * @author： origin   2019年11月9日 上午11:47:04
	 */
	@Override
	public Object inquireTouristOnlineData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer uid =  CommUtil.toInteger(maparam.get("uid"));
			List<Map<String, Object>> usonlinecard = onlineCardDao.selectonlinecardbyuid(uid);
			datamap.put("listdata", usonlinecard);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * separate
	 * @Description：在线卡卡信息查看
	 * @author： origin 
	 */
	@Override
	public Object onlineCardData(HttpServletRequest request) {
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

			parameters.setCode(CommUtil.toString(maparam.get("cardnumber")));
			parameters.setNumber(CommUtil.toString(maparam.get("figurecard")));
			parameters.setNickname(CommUtil.toString(maparam.get("usernick")));
			parameters.setDealer(CommUtil.toString(maparam.get("dealer")));
			parameters.setRealname(CommUtil.toString(maparam.get("realname")));
			parameters.setPhone(CommUtil.toString(maparam.get("phone")));
			parameters.setStatus(CommUtil.toString(maparam.get("status")));
			parameters.setUsername(CommUtil.toString(maparam.get("areaname")));
			parameters.setRemark(CommUtil.toString(maparam.get("remark")));
			parameters.setMobile(CommUtil.toString(maparam.get("mobile")));
//			parameters.setStartTime(CommUtil.getRelevantDate(maparam.get("startTime"), 90, 1));
//			parameters.setEndTime(CommUtil.getRelevantDate(maparam.get("endTime"), 0, 3));
			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
			List<Map<String, Object>> onlineCardData = onlineCardDao.selectOnlinecard(parameters);
			page.setTotalRows(onlineCardData.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> onlineCardInfo = onlineCardDao.selectOnlinecard(parameters);
			
			datamap.put("listdata", CommUtil.isListMapEmpty(onlineCardInfo));
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
	 * newest WeChat
	 * @Description： 查询在线卡信息
	 * @author： origin   2019年12月2日 上午11:11:02 
	 */
	
	
	@Override
	public Map<String, Object> inquireOnlineCardData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			User user = (User)request.getSession().getAttribute("user");
			Integer dealid = CommUtil.toInteger(user.getId());
			String areaid =  CommUtil.toString(maparam.get("areaId"));
			String keywords =  CommUtil.toString(maparam.get("keywords"));
			//type 1是卡号 2是电话
			Integer type =  CommUtil.toInteger(maparam.get("type"));
			Parameters parame = new Parameters();
			parame.setUid(dealid);
			String val = "-1";
			if(val.equals(areaid)){
				parame.setParamete("0");
			}else {
				parame.setState(CommUtil.toString(areaid));
			}
			if(type.equals(1)){
				parame.setCode(keywords);
			}else if(type.equals(2)){
				parame.setMobile(keywords);
			}
			List<Map<String, Object>> onlineinfo = CommUtil.isListMapEmpty(onlineCardDao.selectOnlinecard(parame));
			Double tatolmoney = 0.00;
			Double tatoltopupmoney = 0.00;
			Double tatolsendmoney = 0.00;
			for(Map<String, Object> item : onlineinfo){
				Double topupmoney = CommUtil.toDouble(item.get("money"));//充值金额
				Double sendmoney = CommUtil.toDouble(item.get("sendmoney"));//赠送金额
				Double amountmoney = CommUtil.addBig(topupmoney, sendmoney);
				tatolmoney = CommUtil.addBig(tatolmoney, amountmoney);
				tatoltopupmoney = CommUtil.addBig(tatoltopupmoney, topupmoney);
				tatolsendmoney = CommUtil.addBig(tatolsendmoney, sendmoney);
			}
			Integer datasize = CommUtil.toInteger(onlineinfo.size());
			datamap.put("datamoney", CommUtil.toDouble(tatolmoney));
			datamap.put("datatopupmoney", CommUtil.toDouble(tatoltopupmoney));
			datamap.put("datasendmoney", CommUtil.toDouble(tatolsendmoney));
			datamap.put("datasize", datasize);
			
			int numPerPage =  CommUtil.toInteger(maparam.get("limit"));
			numPerPage = numPerPage == 0 ? 35 : numPerPage;
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			page.setTotalRows(datasize);
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parame.setPages(page.getNumPerPage());
			parame.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> onlinedata = CommUtil.isListMapEmpty(onlineCardDao.selectOnlinecard(parame));
			for(Map<String, Object> item : onlinedata){
				Integer relevawalt = CommUtil.toInteger(item.get("relevawalt"));
				Integer uid = CommUtil.toInteger(item.get("uid"));
				if(relevawalt.equals(1) && !uid.equals(0)) {
					User touser = userService.selectUserById(uid);
					if(touser!=null){
						item.put("money", CommUtil.toDouble(touser.getBalance()));
						item.put("sendmoney", CommUtil.toDouble(touser.getSendmoney()));//赠送金额
					}
				}
			}
			datamap.put("datalist", onlinedata);
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	public Integer insertEntiyCard(Integer uid, Integer merid, Integer areaid, String cardnumber, 
			String figure, Double money, Double sendmoney, Integer type, Integer status, Integer relevawalt, String remark) {
		try {
			Date date = new Date();
			OnlineCard card = new OnlineCard();
			card.setUid(CommUtil.toInteger(uid));
			card.setMerid(CommUtil.toInteger(merid));
			card.setAid(CommUtil.toInteger(areaid));
			card.setCardID(CommUtil.toString(cardnumber));
			card.setFigure(CommUtil.toString(figure));
			card.setMoney(CommUtil.toDouble(money));
			card.setSendmoney(CommUtil.toDouble(sendmoney));
			card.setType(CommUtil.toInteger(type));
			card.setStatus(CommUtil.toInteger(status));
			card.setRelevawalt(CommUtil.toInteger(relevawalt));
			card.setRemark(CommUtil.toString(remark));
			card.setCreateTime(date);
			Integer num = onlineCardDao.insertEntiyOnline(card);
			return num;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * newest WeChat
	 * @Description： 商户绑定在线卡
	 * @author： origin   2019年12月2日 上午11:27:51 
	 */
	@Override
	public Map<String, Object> dealBindingOnlineCard(Integer merid, Integer areaid, String cardID, Integer status) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) {
				CommUtil.responseBuildInfo(102, "未获取到操作人信息", datamap);
				return datamap;
			}
			if (CommUtil.toString(cardID) == null) {
				return CommUtil.responseBuildInfo(107, "在线卡卡号获取失败", datamap);
			}
			OnlineCard findCard = onlineCardDao.selectOnlineCardByCardID(cardID);
			cardID = cardID.toUpperCase();
			String figure = cardID;
			if (findCard == null) {
				//TODO 待处理数据
				insertEntiyCard(null, merid, areaid, cardID, figure, 0.00, 0.00, 1, 1, 2, null);
				CommUtil.responseBuildInfo(200, "绑定成功", datamap);
			} else {
				Integer cardmerid = CommUtil.toInteger(findCard.getMerid());
				Double cardmoney = CommUtil.toDouble(findCard.getMoney());
				if(cardmoney.equals(0.00) && cardmerid.equals(0)){
					findCard.setMerid(merid);
					findCard.setAid(areaid);
					findCard.setStatus(1);
					onlineCardDao.updateOnlineCard(findCard);
					CommUtil.responseBuildInfo(200, "绑定成功", datamap);
				}else{
					CommUtil.responseBuildInfo(106, "该在线卡已被绑定", datamap);
				}
			}
			Parameters parame = new Parameters();
			parame.setUid(merid);
			parame.setState(CommUtil.toString(areaid));
			List<Map<String, Object>> onlineinfo = CommUtil.isListMapEmpty(onlineCardDao.selectOnlinecard(parame));
			datamap.put("listdata", onlineinfo);
			datamap.put("datasize", onlineinfo.size());
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	@Override
	public Map<String, Object> inquireOnlineInfo(HttpServletRequest request, User user) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Parameters parame = new Parameters();
			parame.setUid(user.getId());
			String aid = CommUtil.toString(maparam.get("aid"));
			if(aid.equals("-1")){
				parame.setParamete("0");
			}else{
				parame.setState(CommUtil.toString(maparam.get("aid")));
			}
			List<Map<String, Object>> onlineCardData = CommUtil.isListMapEmpty(onlineCardDao.selectOnlinecard(parame));
			datamap.put("listdata", CommUtil.isListMapEmpty(onlineCardData));
			datamap.put("datasize", onlineCardData.size());
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	@Override
	public List<Map<String, Object>> selectOonlineConsume(Parameters parame) {
		try {
			List<Map<String, Object>> listdata =  CommUtil.isListMapEmpty(onlineCardRecordDao.selectOonlineConsume(parame));
			return listdata;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String,Object>>();
		}
	}

	/**
	 * @Description： 解除绑定的在线卡
	 * @author： origin   2019年12月23日 下午2:29:11 
	 */
	@Override
	public Object unbindingOnlineCard(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				return CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
			}
			Integer onlineid = CommUtil.toInteger(maparam.get("onlineid"));
			Integer conti = CommUtil.toInteger(maparam.get("conti"));
			OnlineCard linecard = onlineCardDao.selectOnlineCardById(onlineid);
			Integer uid = CommUtil.toInteger(linecard.getUid());
			String cardnum = CommUtil.toString(linecard.getCardID());
			Integer dealid = CommUtil.toInteger(linecard.getMerid());
			Double money = CommUtil.toDouble(linecard.getMoney());
			Double sendmoney = CommUtil.toDouble(linecard.getSendmoney());
			Integer relevawalt = CommUtil.toInteger(linecard.getRelevawalt());
			Date datatimes = new Date();
			if(conti.equals(0)){//有金额时返回
				if(relevawalt.equals(1)){
					User tourist = userService.selectUserById(uid);
					Double usermoney = CommUtil.toDouble(tourist.getBalance());
					if(usermoney>0){
						return CommUtil.responseBuildInfo(103, "该在线卡关联钱包，且钱包存在金额，是否继续解绑", datamap);
					}else{
						return CommUtil.responseBuildInfo(103, "该在线卡关联钱包，是否继续解绑", datamap);
					}
				}else if(money>0 || sendmoney>0){
					return CommUtil.responseBuildInfo(103, "该在线卡还有金额: "+money+" 元", datamap);
				}
			}else if(conti.equals(1)){//有金额时强制执行
				Double accountmoney = CommUtil.addBig(money, sendmoney);
				if(relevawalt.equals(2)){
					onlineCardRecordService.insertOnlineCardRecord(uid, dealid, HttpRequest.createOrdernum(6), 
							cardnum, null, 0.00, money, sendmoney, accountmoney, 0.00, 0.00, 9, 1, 2, datatimes,dealid);
				}
				onlineCardRecordService.insertOnlineCardRecord(uid, linecard.getMerid(), HttpRequest.createOrdernum(6), 
				linecard.getCardID(), null, 0.00, linecard.getMoney(), sendmoney, 0.00, 0.00, 0.00, 4, 4, 1, datatimes,linecard.getMerid());
			}
			OnlineCard online = new OnlineCard();
			online.setId(onlineid);
			online.setUid(0);
			online.setMoney(0.00);
			online.setSendmoney(0.00);
			online.setStatus(1);
			online.setRelevawalt(2);
			online.setRemark("");
			onlineCardDao.updateOnlineCard(online);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * @Description：删除绑定的在线卡
	 * @author： origin
	 * @createTime：2019年12月26日上午9:40:46
	 */
	@Override
	public Object deleteOnlineCard(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				return CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
			}
			Integer onlineid = CommUtil.toInteger(maparam.get("onlineid"));
			OnlineCard linecard = onlineCardDao.selectOnlineCardById(onlineid);
			Integer uid = CommUtil.toInteger(linecard.getUid());
			String cardnum = CommUtil.toString(linecard.getCardID());
			Integer dealid = CommUtil.toInteger(linecard.getMerid());
			Double money = CommUtil.toDouble(linecard.getMoney());
			Double sendmoney = CommUtil.toDouble(linecard.getSendmoney());
			Double accountmoney = CommUtil.addBig(money, sendmoney);
			Date datatimes = new Date();
			if(money>0 || sendmoney>0){
				onlineCardRecordService.insertOnlineCardRecord(uid, dealid, HttpRequest.createOrdernum(6), 
						cardnum, null, 0.00, money, sendmoney, accountmoney, 0.00, 0.00, 9, 1, 2, datatimes,dealid);
			}
			onlineCardRecordService.insertOnlineCardRecord(uid, dealid, HttpRequest.createOrdernum(6), cardnum, null, 
					0.00, money, sendmoney, 0.00, 0.00, 0.00, 4, 4, 1, datatimes, dealid);
//			onlineCardDao.removecardbyId(onlineid);
			OnlineCard online = new OnlineCard();
			online.setId(onlineid);
			online.setUid(0);
			online.setMerid(0);
			online.setAid(0);
			online.setMoney(0.00);
			online.setSendmoney(0.00);
			online.setStatus(0);
			online.setRelevawalt(2);
			online.setRemark("");
			onlineCardDao.updateOnlineCard(online);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	@Override
	public Map<String, Object> seleSubMerByCardNumber(String cardNumber) {
		if(cardNumber == null || "".equals(cardNumber)){
			return new HashMap<String, Object>();
		}else{
			Map<String, Object> subMerData = onlineCardDao.seleSubMerByCardNumber(cardNumber);
			if(subMerData == null){
				return new HashMap<String, Object>();
			}else{
				return subMerData;
			}
		}
	}

	/**
	 * @Description：获取在线卡数量
	 * @author： origin 2020年6月5日下午5:49:22
	 */
	@Override
	public Integer onlinecount(OnlineCard online) {
		Integer onlincount = onlineCardDao.onlinecount(online);
		return onlincount;
	}
	
	@Override
	public Map<String, Object> selectOnlineCardByCardNum(String cardNum) {
		try {
			Map<String, Object> resultdata = onlineCardDao.selectOnlineCardByCardNum(cardNum);
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	
}
