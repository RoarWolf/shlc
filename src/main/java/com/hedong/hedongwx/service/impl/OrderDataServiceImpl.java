package com.hedong.hedongwx.service.impl;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.PackageMonthDao;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.InCoins;
import com.hedong.hedongwx.entity.Money;
import com.hedong.hedongwx.entity.OfflineCard;
import com.hedong.hedongwx.entity.OnlineCardRecord;
import com.hedong.hedongwx.entity.PackageMonthRecord;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.Withdraw;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.MoneyService;
import com.hedong.hedongwx.service.OfflineCardService;
import com.hedong.hedongwx.service.OnlineCardRecordService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.OrderDataService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.service.WithdrawService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.PageUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Description： 
 * @author  origin  创建时间：   2019年11月11日 上午11:25:37  
 */
@Service
public class OrderDataServiceImpl implements OrderDataService{
	

	@Autowired
	private OnlineCardRecordService onlineCardRecordService;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private InCoinsService inCoinsService;
	@Autowired
	private OnlineCardService onlineCardService;
	@Autowired
	private OfflineCardService offlineCardService;
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private WithdrawService withdrawService;
	@Autowired
	private PackageMonthDao packageMonthDao;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private UserService userService;
	/**
	 * separate
	 * @Description：交易订单详情明细查看
	 * @author： origin 
	 */
	@Override
	public Object tradeDataDetail(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer orderid =  CommUtil.toInteger(maparam.get("orderid"));//交易订单号
			TradeRecord trade = tradeRecordService.selectTradeById(orderid);
			String ordernum = CommUtil.toString(trade.getOrdernum());
			Integer paysource = CommUtil.toInteger(trade.getPaysource());
			Integer status = CommUtil.toInteger(trade.getStatus());
			Integer paytype = CommUtil.toInteger(trade.getPaytype());
			if(paysource.equals(1)){
				if(paytype.equals(5) && status==1){
					paytype = 8;
				}else if(paytype.equals(5) && status==2){
					paytype = 9;
				}
				List<ChargeRecord> chargeRecord = chargeRecordService.getRecordData(ordernum,paytype);
				ChargeRecord record = chargeRecord.size() > 0 ? chargeRecord.get(0) : new ChargeRecord();
				datamap.put("order", record);
			}else if(paysource.equals(2)){
				InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
				datamap.put("order", inCoins);
			}else if(paysource.equals(3)){
				OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
				offlineCard = offlineCard == null ? new OfflineCard() : offlineCard;
				datamap.put("order", offlineCard);
			}else if(paysource.equals(4)){
				Money money = moneyService.queryMoneyByOrdernum(ordernum);
				money = money == null ? new Money() : money;
				datamap.put("order", money);
			}else if(paysource.equals(5)){
				OnlineCardRecord online = new OnlineCardRecord();
				online.setOrdernum(ordernum);
				List<OnlineCardRecord> onlinelist = onlineCardRecordService.selectCardRecord(online);
				OnlineCardRecord onlinerecord = new OnlineCardRecord();
				for(OnlineCardRecord item : onlinelist){
					Integer type = item.getType();
					if(status==1){
						if(type==1 || type==3 || type==6 || type==8 || type==10) onlinerecord = item;
					}else if(status==2){
						if(type==1 || type==3 || type==6 || type==8 || type==10) onlinerecord = item;
					}
				}
				datamap.put("order", onlinerecord);
			}else if(paysource.equals(6)){
				List<PackageMonthRecord> packagemont = tradeRecordService.selectMonthRecordEntiy(ordernum, status);
				PackageMonthRecord record = packagemont.size() > 0 ? packagemont.get(0) : new PackageMonthRecord();
				datamap.put("order", record);
			}
			datamap.put("ordertrade", trade);
			datamap.put("paysource", paysource);
			datamap.put("paytype", paytype);
			datamap.put("status", status);
			datamap.put("admin", CommonConfig.getAdminReq(request));
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**
	 * separate
	 * @Description：（充电、离线充值、投币、钱包、包月、在线卡）订单详情查看
	 * @author： origin 
	 */
	@Override
	public Object orderDataDetail(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer paysource =  CommUtil.toInteger(maparam.get("paysource"));
			Integer status =  CommUtil.toInteger(maparam.get("status"));
			String ordernum =  CommUtil.toString(maparam.get("ordernum"));
			Integer state = 1;
			Integer source = 1;
			/** 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、钱包-5、在线卡-6、包月支付*/
			if(paysource==1){
				state = status == 0 ? 1 : 2;
				List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
				ChargeRecord record = chargeRecord.size() > 0 ? chargeRecord.get(0) : new ChargeRecord();
				if(record!=null && record.getPaytype()==4){
					source = 2;
					List<PackageMonthRecord> packagemont = tradeRecordService.selectMonthRecordEntiy(ordernum, state);
					PackageMonthRecord partrecord = packagemont.size() > 0 ? packagemont.get(0) : new PackageMonthRecord();
					datamap.put("partrecord", partrecord);
				}
				datamap.put("order", record);
			}else if(paysource==2){
				if(status==2 || status==4 || status==5 || status==7 || status==9 || status==11 ) state = 2;
				InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
				datamap.put("order", inCoins);
			}else if(paysource==3){
				status = status == null ? 2 : status;
				if(status==3 || status==4 || status==6) state = 2;
				else source = 2;
				OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
				offlineCard = offlineCard == null ? new OfflineCard() : offlineCard;
				datamap.put("order", offlineCard);
			}else if(paysource==4){
				if(status==2 || status==3 || status==4 || status==5 || status==6 || status==8) state = 2;
				Money money = moneyService.queryMoneyByOrdernum(ordernum);
				money = money == null ? new Money() : money;
				datamap.put("order", money);
			}else if(paysource==5){
				OnlineCardRecord cardRecord = onlineCardRecordService.selectRecordByOrdernum(ordernum, status, null);
				Integer flag = CommUtil.toInteger(cardRecord.getFlag());
				if(flag.equals(2)) state = 2;
				datamap.put("order", cardRecord);
			}else if(paysource==6){
				if(status==2) state = 2;
				List<PackageMonthRecord> packagemont = tradeRecordService.selectMonthRecordEntiy(ordernum, status);
				PackageMonthRecord record = packagemont.size() > 0 ? packagemont.get(0) : new PackageMonthRecord();
				if(record!=null && record.getPaysource()==2){
					source = 2;
					List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
					ChargeRecord partrecord = chargeRecord.size() > 0 ? chargeRecord.get(0) : new ChargeRecord();
					datamap.put("partrecord", partrecord);
				}
				datamap.put("order", record);
			}
			if(source==1){
				TradeRecord trarec = new TradeRecord();
				trarec.setOrdernum(ordernum);
				trarec.setStatus(state);
				TradeRecord trade = tradeRecordService.getTraderecordList(trarec);
				datamap.put("partrecord", trade);
			}
			datamap.put("paysource", paysource);
			datamap.put("source", source);
			datamap.put("admin", CommonConfig.getAdminReq(request));
			datamap.put("paysource", paysource);
//			datamap.put("paytype", paytype);
			datamap.put("status", status);
			datamap.put("admin", CommonConfig.getAdminReq(request));
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	/**
	 * separate
	 * @Description：包月记录信息查看
	 * @author： origin 
	 */
	@Override
	public Object packageMonthData(HttpServletRequest request) {
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
			if(!rank.equals(0)) parameters.setDealer(CommUtil.toString(user.getId()));//绑定id
			parameters.setNickname(CommUtil.toString(maparam.get("usernick")));
			parameters.setOrder(CommUtil.toString(maparam.get("ordernum")));
			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
//			parameters.setStartTime(CommUtil.getRelevantDate(maparam.get("startTime"), 7, 1));
//			parameters.setEndTime(CommUtil.getRelevantDate(maparam.get("endTime"), 0, 3));
			List<Map<String, Object>> packageMonthdata = packageMonthDao.selectMonthRecordByParam(parameters);
			page.setTotalRows(packageMonthdata.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> packageMonthinfo = packageMonthDao.selectMonthRecordByParam(parameters);
			
			datamap.put("listdata", CommUtil.isListMapEmpty(packageMonthinfo));
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
	 * separate
	 * @Description：商户收益订单明细明信息查询
	 * @author： origin 
	 */
	@Override
	public Object dealerEarningsOrderDetail(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			String ordernum =  CommUtil.toString(maparam.get("ordernum"));
			Integer paysource = CommUtil.toInteger(maparam.get("paysource"));
			Integer type = CommUtil.toInteger(maparam.get("type"));
			if (paysource.equals(1)) {
				List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
				if(chargeRecord==null || chargeRecord.isEmpty()){
					datamap.put("order", new ChargeRecord());
				}else{
					datamap.put("order", chargeRecord.get(0));
				}
			} else if (paysource.equals(2)) {
				InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
				datamap.put("order", inCoins);
			} else if (paysource.equals(3)) {
				OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
				datamap.put("order", offlineCard);
			} else if (paysource.equals(4)) {
				Withdraw withdraw = withdrawService.selectWithdrawByOrdernum(ordernum);
				datamap.put("order", withdraw);
			} else if (paysource.equals(5)) {
				Money money = moneyService.queryMoneyByOrdernum(ordernum);
				datamap.put("order", money);
			} else if (paysource.equals(6)) {
				//TODO 在线卡不完全正确判断
				OnlineCardRecord cardRecord = null;
				if(type==1){
					cardRecord = onlineCardRecordService.selectRecordByOrdernum( ordernum, 3, null);
					if(cardRecord==null) cardRecord = onlineCardRecordService.selectRecordByOrdernum( ordernum, 6, null);
				}else if(type==2){
					cardRecord = onlineCardRecordService.selectRecordByOrdernum( ordernum, 5, 2);
					if(cardRecord==null) cardRecord = onlineCardRecordService.selectRecordByOrdernum( ordernum, 7, 2);
				}
				datamap.put("order", cardRecord);
			}
			datamap.put("paysource", paysource);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * separate
	 * @Description：钱包明细根据订单号和来源查询该条订单
	 * @author： origin
	 */
	@Override
	public Object touristWalletDetail(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
//			User user = CommonConfig.getAdminReq(request);
			String ordernum =  CommUtil.toString(maparam.get("ordernum"));
			Integer paysource =  CommUtil.toInteger(maparam.get("paysource"));
			// 金额来源 1、充值-2、充电-3、投币-4、离线卡充值-5、退款到钱包 6、钱包退款 7虚拟充值  8虚拟退款 
			if (paysource.equals(1) || paysource.equals(5) || paysource.equals(6) || paysource.equals(7) || paysource.equals(8) ) {
				Money money = moneyService.queryMoneyByOrdernum(ordernum);
				datamap.put("order", money);
			} else if (paysource.equals(2)) {
				List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
				datamap.put("order", chargeRecord.get(0));
			} else if (paysource.equals(3)) {
				InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
				datamap.put("order", inCoins);
			} else if (paysource.equals(4)) {
				OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
				datamap.put("order", offlineCard);
			} else if (paysource.equals(9)) {
				OnlineCardRecord cardRecord = new OnlineCardRecord();
				cardRecord = onlineCardRecordService.selectRecordByOrdernum( ordernum, 1, null);
				if(cardRecord==null) cardRecord = onlineCardRecordService.selectRecordByOrdernum( ordernum, 2, null);
				datamap.put("order", cardRecord);
			}
			datamap.put("paysource", paysource);
			datamap.put("ordernum", ordernum);
			datamap.put("genre", request.getParameter("genre"));
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * separate
	 * @Description：交易订单查询合伙人收益信息
	 * @author： origin 
	 */
	@Override
	public Object partnerIncomeDetail(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer orderid =  CommUtil.toInteger(maparam.get("orderid"));
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			TradeRecord tradeorder = tradeRecordService.selectTradeById(CommUtil.toInteger(orderid));
			Integer status = CommUtil.toInteger(tradeorder.getStatus());
			String comment = CommUtil.toString(tradeorder.getComment());
			Integer uid = CommUtil.toInteger(tradeorder.getUid());
			Integer paysource = CommUtil.toInteger(tradeorder.getPaysource());
			String code = CommUtil.toString(tradeorder.getCode());
//			Integer aid = 0;
//			if(paysource==1 || paysource==2 || paysource==3){
//				Map<String, Object> usequmap = userEquipmentService.getEquipToUserByCode(code);
//				aid = CommUtil.toInteger(usequmap.get("aid"));
//			}else if(paysource==4){
//				User touuser = userService.selectUserById(uid);
//				aid = CommUtil.toInteger(touuser.getAid());
//			}else if(paysource==5){
//				OnlineCard online = onlineCardService.selectOnlineCardByCardID(code);
//				aid = CommUtil.toInteger(online.getAid());
//			}else if(paysource==6){
//				
//			}
			
			if(comment!=null){
				JSONArray jsona = JSONArray.fromObject(comment);
				for (int i = 0; i < jsona.size(); i++) {
					Map<String, Object> map = new HashMap<>();
					JSONObject item = jsona.getJSONObject(i);
					Integer partid = CommUtil.toInteger(item.get("partid"));
					//Integer mercid = CommUtil.toInteger(item.get("merid"));
					if(partid!=0){
						String percent = CommUtil.toString(item.get("percent"));
						Double dividemoney = CommUtil.toDouble(item.get("money"));
						User partuser = userService.selectUserById(partid);
						if(status==2) dividemoney = (0.00 - dividemoney);
						map.put("nickname", CommUtil.toString(partuser.getUsername()));  
						map.put("realname", CommUtil.toString(partuser.getRealname()));
						map.put("phone", CommUtil.toString(partuser.getPhoneNum()));
						map.put("partmoney", dividemoney);
						map.put("percent", percent);
						list.add(map);
					}
				}
			}
			datamap.put("ordernum", CommUtil.toString(tradeorder.getOrdernum()));
			///** 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、钱包-5、在线卡-6、包月支付*/
			datamap.put("listdata", CommUtil.isListMapEmpty(list));
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	
	/**
	 * separate
	 * @Description： 订单详情信息
	 * @author： origin 
	 */
	@Override
	public Object orderDetailData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer orderid =  CommUtil.toInteger(maparam.get("orderid"));
			Integer source =  CommUtil.toInteger(maparam.get("source"));//1：交易   2:其他
			Integer paysource =  CommUtil.toInteger(maparam.get("paysource"));
			if(source.equals(1)){
				TradeRecord trade = tradeRecordService.selectTradeById(orderid);
				datamap.put("tradeorder", trade);
				String tradeordernum = CommUtil.toString(trade.getOrdernum());
				Integer tradesource = CommUtil.toInteger(trade.getPaysource());
				Integer tradestatus = CommUtil.toInteger(trade.getStatus());
				datamap = orderDataByOrdernum( tradeordernum, tradesource, 2, tradestatus);
			}else if(source.equals(2)){
				String ordernum = null;
				Integer status = null;
				if(paysource.equals(1)){
					ChargeRecord chargeRecords = chargeRecordService.chargeRecordOne(orderid);
					ordernum = CommUtil.toString(chargeRecords.getOrdernum());
					if(CommUtil.toInteger(chargeRecords.getNumber()).equals(0)) status = 1;
					else status = 2;
					datamap.put("orderdata", chargeRecords);
				}else if(paysource.equals(2)){
					InCoins inCoinses = inCoinsService.selectInCoinsRecordById(orderid);
					Integer paytype = CommUtil.toInteger(inCoinses.getHandletype());
					if(paytype.equals(1) || paytype.equals(2) || paytype.equals(6) || paytype.equals(8) || paytype.equals(10)){
						status = 1;
					}else if(paytype.equals(4) || paytype.equals(5) || paytype.equals(7) || paytype.equals(9) || paytype.equals(11)){
						status = 2;
					}
					ordernum = CommUtil.toString(inCoinses.getOrdernum());
					datamap.put("orderdata", inCoinses);
				}else if(paysource.equals(3)){
					OfflineCard offlineCards = offlineCardService.selectOfflineCardById(orderid);
					Integer paytype = CommUtil.toInteger(offlineCards.getPaytype());
					if(paytype.equals(1) || paytype.equals(2) || paytype.equals(5)){
						status = 1;
					}else if(paytype.equals(3) || paytype.equals(4) || paytype.equals(6)){
						status = 2;
					}
					ordernum = CommUtil.toString(offlineCards.getOrdernum());
					datamap.put("orderdata", offlineCards);
				}else if(paysource.equals(4)){
					Money moneys = moneyService.payMoneyinfo(orderid);
					Integer paytype = CommUtil.toInteger(moneys.getPaytype());
					if(paytype.equals(0) || paytype.equals(1) || paytype.equals(3) || paytype.equals(4) || paytype.equals(5) || paytype.equals(7)){
						status = 1;
					}else if(paytype.equals(2) || paytype.equals(6) || paytype.equals(8)){
						status = 2;
					}
					ordernum = CommUtil.toString(moneys.getOrdernum());
					datamap.put("orderdata", moneys);
				}else if(paysource.equals(5)){
					OnlineCardRecord cardRecords = onlineCardRecordService.selectOnlineCard(orderid);
					Integer paytype = CommUtil.toInteger(cardRecords.getFlag());
					if(paytype.equals(1)){
						status = 1;
					}else if(paytype.equals(2)){
						status = 2;
					}
					ordernum = CommUtil.toString(cardRecords.getOrdernum());
					datamap.put("orderdata", cardRecords);
				}else if(paysource.equals(6)){
					PackageMonthRecord monthRecord = userService.selectMonthRecordById(orderid);
					Integer paytype = CommUtil.toInteger(monthRecord.getStatus());
					if(paytype.equals(1)){
						status = 1;
					}else if(paytype.equals(2)){
						status = 2;
					}
					ordernum = CommUtil.toString(monthRecord.getOrdernum());
					datamap.put("orderdata", monthRecord);
				}
				datamap = orderDataByOrdernum( ordernum, paysource, 1, status);
			}else{
				return CommUtil.responseBuildInfo(102, "类型传递不正确", datamap);
			}
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**
	 * separate
	 * @Description： 订单详情信息   根据订单号查询
	 * @author： origin 
	 */
	@Override
	public Object orderDetailInfo(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			String ordernum =  CommUtil.toString(maparam.get("ordernum"));
			Integer source =  CommUtil.toInteger(maparam.get("source"));//1：交易   2:其他
			Integer paysource =  CommUtil.toInteger(maparam.get("paysource"));
			Integer orderstatus =  CommUtil.toInteger(maparam.get("status"));//1:正常 2:退费  --  3：部分退费
			if(source.equals(1)){
				TradeRecord tradeentey = new TradeRecord();
				tradeentey.setOrdernum(ordernum);
				tradeentey.setStatus(orderstatus);
				TradeRecord tradeitem = tradeRecordService.getTraderecordList(tradeentey);
				datamap.put("tradeorder", tradeitem);
				Integer tradesource = CommUtil.toInteger(tradeitem.getPaysource());
//				Integer tradestatus = CommUtil.toInteger(tradeitem.getStatus());
				datamap = orderDataByOrdernum( ordernum, tradesource, 2, orderstatus);
			}else if(source.equals(2)){
				
				datamap = orderDataByOrdernum( ordernum, paysource, 1, orderstatus);
				datamap = orderDataByOrdernum( ordernum, paysource, 2, orderstatus);
				
			}else{
				return CommUtil.responseBuildInfo(102, "类型传递不正确", datamap);
			}
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	public Map<String, Object> orderDataByOrdernum(String ordernum, Integer paysource, Integer type, Integer status) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		return datamap;
//		try {
//			if(type.equals(1)){
////				Map<String, Object> trade = tradeRecordService.selectTradeDetails(ordernum, status);
//				TradeRecord tradeentey = new TradeRecord();
//				tradeentey.setOrdernum(ordernum);
//				tradeentey.setStatus(status);
//				TradeRecord tradeitem = tradeRecordService.getTraderecordList(tradeentey);
//				datamap.put("tradeorder", tradeitem);
//			}else if(type.equals(2)){
//				if(paysource.equals(1)){
//					List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
//					chargeRecord = chargeRecord == null ? new ArrayList<ChargeRecord>() : chargeRecord;
//					for(ChargeRecord item : chargeRecord){
//						Integer charstatus = CommUtil.toInteger(item.getNumber());
//						if(status.equals(1) && charstatus.equals(0)){
//							datamap.put("orderdata", item);
//						}else if(status.equals(2)){
//							if(charstatus.equals(1) || charstatus.equals(2)){
//								datamap.put("orderdata", item);
//							}
//						}
//					}
//				}else if(paysource.equals(2)){
//					InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
//					inCoins = inCoins == null ? new InCoins() : inCoins;
//					Integer paytype = CommUtil.toInteger(inCoins.getHandletype());
//					if(paytype.equals(1) || paytype.equals(2) || paytype.equals(6) || paytype.equals(8) || paytype.equals(10)){
//						status = 1;
//					}else if(paytype.equals(4) || paytype.equals(5) || paytype.equals(7) || paytype.equals(9) || paytype.equals(11)){
//						status = 2;
//					}
//					if(status.equals(1)){
//						
//					}
//						datamap.put("orderdata", item);
//					}else if(status.equals(2)){
//						if(charstatus.equals(1) || charstatus.equals(2)){
//							datamap.put("orderdata", item);
//						}
//					}
//					
//					/** 操作类型 -- 1通过微信下发投币、2通过支付宝下发投币、3设备投币成功信息上传服务器、4 微信退款、5 支付宝退款、6 钱包支付、7钱包退款、8微信小程序、9微信小程序退款、10支付宝小程序、11支付宝小程序退款*/
//					private Byte handletype;
//					
//					datamap.put("orderdata", inCoins);
//				}else if(paysource.equals(3)){
//					OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
//					datamap.put("orderdata", offlineCard);
//				}else if(paysource.equals(4)){
//					Money money = moneyService.queryMoneyByOrdernum(ordernum);
//					datamap.put("orderdata", money);
//				}
//				
//				
//				if(source.equals(2)){
//					String ordernum = null;
//					Integer status = null;
//					if(paysource.equals(1)){
//						ChargeRecord chargeRecords = chargeRecordService.chargeRecordOne(orderid);
//						ordernum = CommUtil.toString(chargeRecords.getOrdernum());
//						if(CommUtil.toInteger(chargeRecords.getNumber()).equals(0)) status = 1;
//						else status = 2;
//						datamap.put("orderdata", chargeRecords);
//					}else if(paysource.equals(2)){
//						InCoins inCoinses = inCoinsService.selectInCoinsRecordById(orderid);
//						Integer paytype = CommUtil.toInteger(inCoinses.getHandletype());
//						if(paytype.equals(1) || paytype.equals(2) || paytype.equals(6) || paytype.equals(8) || paytype.equals(10)){
//							status = 1;
//						}else if(paytype.equals(4) || paytype.equals(5) || paytype.equals(7) || paytype.equals(9) || paytype.equals(11)){
//							status = 2;
//						}
//						ordernum = CommUtil.toString(inCoinses.getOrdernum());
//						datamap.put("orderdata", inCoinses);
//					}else if(paysource.equals(3)){
//						OfflineCard offlineCards = offlineCardService.selectOfflineCardById(orderid);
//						Integer paytype = CommUtil.toInteger(offlineCards.getPaytype());
//						if(paytype.equals(1) || paytype.equals(2) || paytype.equals(5)){
//							status = 1;
//						}else if(paytype.equals(3) || paytype.equals(4) || paytype.equals(6)){
//							status = 2;
//						}
//						ordernum = CommUtil.toString(offlineCards.getOrdernum());
//						datamap.put("orderdata", offlineCards);
//					}else if(paysource.equals(4)){
//						Money moneys = moneyService.payMoneyinfo(orderid);
//						Integer paytype = CommUtil.toInteger(moneys.getPaytype());
//						if(paytype.equals(0) || paytype.equals(1) || paytype.equals(3) || paytype.equals(4) || paytype.equals(5) || paytype.equals(7)){
//							status = 1;
//						}else if(paytype.equals(2) || paytype.equals(6) || paytype.equals(8)){
//							status = 2;
//						}
//						ordernum = CommUtil.toString(moneys.getOrdernum());
//						datamap.put("orderdata", moneys);
//					}else if(paysource.equals(5)){
//						OnlineCardRecord cardRecords = onlineCardRecordService.selectOnlineCard(orderid);
//						Integer paytype = CommUtil.toInteger(cardRecords.getFlag());
//						if(paytype.equals(1)){
//							status = 1;
//						}else if(paytype.equals(2)){
//							status = 2;
//						}
//						ordernum = CommUtil.toString(cardRecords.getOrdernum());
//						datamap.put("orderdata", cardRecords);
//					}else if(paysource.equals(6)){
//						PackageMonthRecord monthRecord = userService.selectMonthRecordById(orderid);
//						Integer paytype = CommUtil.toInteger(monthRecord.getStatus());
//						if(paytype.equals(1)){
//							status = 1;
//						}else if(paytype.equals(2)){
//							status = 2;
//						}
//						ordernum = CommUtil.toString(monthRecord.getOrdernum());
//						datamap.put("orderdata", monthRecord);
//					}
//				
//				
//				
//				
//				
//				else if(paysource.equals(5)){
//					OnlineCardRecord cardRecord = onlineCardRecordService.selectRecordByOrdernum( ordernum, 3, null);
//					datamap.put("orderdata", cardRecord);
//				}else if(paysource.equals(6)){
//					List<PackageMonthRecord> packagemont = tradeRecordService.selectMonthRecordEntiy(ordernum, status);
//					PackageMonthRecord record = packagemont.size() > 0 ? packagemont.get(0) : new PackageMonthRecord();
//					datamap.put("orderdata", record);
//				}else{
//					datamap = CommUtil.responseBuildInfo(102, "来源传递错误", datamap);
//				}
//			}else{
//				datamap = CommUtil.responseBuildInfo(102, "类型传递错误", datamap);
//			}
//			return datamap;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
//		}
	}

	

}
