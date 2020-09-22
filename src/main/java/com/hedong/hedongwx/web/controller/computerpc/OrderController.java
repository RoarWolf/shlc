package com.hedong.hedongwx.web.controller.computerpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.InCoins;
import com.hedong.hedongwx.entity.Money;
import com.hedong.hedongwx.entity.OfflineCard;
import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.OnlineCardRecord;
import com.hedong.hedongwx.entity.PackageMonthRecord;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.MoneyService;
import com.hedong.hedongwx.service.OfflineCardService;
import com.hedong.hedongwx.service.OnlineCardRecordService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.service.WithdrawService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="/pcorder")
public class OrderController {
	
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private OfflineCardService offlineCardService;
	@Autowired
	private InCoinsService inCoinsService;
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private OnlineCardRecordService onlineCardRecordService;
	@Autowired
	private WithdrawService withdrawService;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private OnlineCardService onlineCardService;
	@Autowired
	private UserService userService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private HttpServletRequest request;
	
	
	/**
	 * @Description： 查询合伙人金额记录
	 * @author： origin 创建时间：   2019年7月18日 下午6:19:19 hd_packagemonth_record
	 */
	@RequestMapping(value = "/partnerIncomeDetail")
	@ResponseBody
	public Object partnerIncomeDetail(Integer orderid){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			TradeRecord tradeorder = tradeRecordService.selectTradeById(CommUtil.toInteger(orderid));
			Integer uid = CommUtil.toInteger(tradeorder.getUid());
			Double money = CommUtil.toDouble(tradeorder.getMoney());
			Integer paysource = CommUtil.toInteger(tradeorder.getPaysource());
			Integer status = CommUtil.toInteger(tradeorder.getStatus());
			String code = CommUtil.toString(tradeorder.getCode());
			String comment = CommUtil.toString(tradeorder.getComment());
			Integer aid = 0;
			if(paysource==1 || paysource==2 || paysource==3){
				Map<String, Object> usequmap = userEquipmentService.getEquipToUserByCode(code);
				aid = CommUtil.toInteger(usequmap.get("aid"));
			}else if(paysource==4){
				User touuser = userService.selectUserById(uid);
				aid = CommUtil.toInteger(touuser.getAid());
			}else if(paysource==5){
				OnlineCard online = onlineCardService.selectOnlineCardByCardID(code);
				aid = CommUtil.toInteger(online.getAid());
			}else if(paysource==6){
				
			}
			if(comment.equals("0") || comment==null){
				
			}else{
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
			result.put("body", list);
			result.put("ordernum", CommUtil.toString(tradeorder.getOrdernum()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		///** 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、钱包-5、在线卡-6、包月支付*/
		return result;
	}
	
	/**
	 * @Description： 查询包月使用记录
	 * @author： origin 创建时间：   2019年7月18日 下午6:19:19 hd_packagemonth_record
	 */
	@RequestMapping(value = "/selectPackageMonth")
	public String selectPackageMonth(HttpServletRequest request, HttpServletResponse response, Model model){
		PageUtils<Parameters> pageBean = tradeRecordService.selectPackageMonth(request);
		String startTime = CommUtil.toString(request.getParameter("startTime"));
		String endTime = CommUtil.toString(request.getParameter("endTime"));
		startTime = startTime==null ? StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss",  StringUtil.toDateTime(), 7) : startTime;
		endTime = endTime==null ? StringUtil.getCurrentDateTime() : endTime;
		model.addAttribute("nickname", CommUtil.toString(request.getParameter("nickname")));
		model.addAttribute("ordernum", CommUtil.toString(request.getParameter("ordernum")));
		
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("order", pageBean.getListMap());
		return "comorder/orderPackageMonth";
	}
	
//==========================================================================================================================
	
	//在交易记录中查询上传的投币记录
	@RequestMapping(value = "/selectCoinsInfoByTr")
	public String selectCoinsInfoByTr(HttpServletRequest request, HttpServletResponse response, Model model){
		PageUtils<Parameters> pageBean = tradeRecordService.selectCoinsInfoByTr(request);
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(null==startTime || startTime.equals("")) startTime = StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss",  StringUtil.toDateTime(), 7);
		if(null==endTime || endTime.equals(""))endTime = StringUtil.getCurrentDateTime();
		model.addAttribute("ordernum", request.getParameter("ordernum"));
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("mobile", request.getParameter("mobile"));
		model.addAttribute("dealer", request.getParameter("dealer"));
		model.addAttribute("code", request.getParameter("code"));
		model.addAttribute("cardnum", request.getParameter("cardnum"));
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("order", pageBean.getListMap());
		return "comorder/ordercoinstra";
	}
	
//==========================================================================================================================
	
	//查询交易记录订单  hd_traderecord
	@RequestMapping(value = "/selectTradeRecord")
	public String selectTradeRecord(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageUtils<Parameters> pageBean = tradeRecordService.selectTradeRecord(request);
		
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(null==startTime || startTime.equals("")) startTime = StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss",  StringUtil.toDateTime(), 7);
		if(null==endTime || endTime.equals(""))endTime = StringUtil.getCurrentDateTime();
		
		model.addAttribute("mobile", request.getParameter("mobile"));
		model.addAttribute("ordernum", request.getParameter("ordernum"));
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("dealer", request.getParameter("dealer"));
		model.addAttribute("code", request.getParameter("code"));
		model.addAttribute("status", request.getParameter("status"));
		model.addAttribute("paysource", request.getParameter("paysource"));
		model.addAttribute("paytype", request.getParameter("paytype"));
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("totalMap", pageBean.getMap());
		model.addAttribute("traderecord", pageBean.getListMap());
		return "computer/orderTraderecord";
	}
	
	//查询充电记录订单  hd_chargerecord
	@RequestMapping(value = "/selectChargeRecord")
	public String selectChargeRecord(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageUtils<Parameters> pageBean = chargeRecordService.selectChargeRecord(request);
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(null==startTime || startTime.equals("")) startTime = StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss",  StringUtil.toDateTime(), 7);
		if(null==endTime || endTime.equals(""))endTime = StringUtil.getCurrentDateTime();
		
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("dealer", request.getParameter("dealer"));
		model.addAttribute("ordernum", request.getParameter("ordernum"));
		model.addAttribute("equipmentnum", request.getParameter("equipmentnum"));
		
		model.addAttribute("paytype", request.getParameter("paytype"));
		model.addAttribute("number", request.getParameter("number"));
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("orderCharge", pageBean.getListMap());
		User user = (User) request.getSession().getAttribute("admin");
		model.addAttribute("pwd", user.getPassword());
		return "computer/orderCharge";
	}
	
	//查询离线卡记录订单  hd_offlinecard
	@RequestMapping(value = "/selectOfflineRecord")
	public String selectOfflineRecord(HttpServletRequest request, HttpServletResponse response, Model model){
		PageUtils<Parameters> pageBean = offlineCardService.selectOfflineRecord(request);
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(null==startTime || startTime.equals("")) startTime = StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss",  StringUtil.toDateTime(), 7);
		if(null==endTime || endTime.equals(""))endTime = StringUtil.getCurrentDateTime();
		
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("dealer", request.getParameter("dealer"));
		model.addAttribute("ordernum", request.getParameter("ordernum"));
		model.addAttribute("equipmentnum", request.getParameter("equipmentnum"));
		model.addAttribute("cardID", request.getParameter("cardID"));
		model.addAttribute("handletype", request.getParameter("handletype"));
		model.addAttribute("recycletype", request.getParameter("recycletype"));
		model.addAttribute("paytype", request.getParameter("paytype"));
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("orderOffline", pageBean.getListMap());
		return "computer/orderOfflineCard";
	}
	
	//查询模拟投币记录订单  hd_incoins
	@RequestMapping(value = "/selectIncoinsRecord")
	public String selectIncoinsRecord(HttpServletRequest request, HttpServletResponse response, Model model){
		PageUtils<Parameters> pageBean = inCoinsService.selectIncoinsRecord(request);
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(null==startTime || startTime.equals("")) startTime = StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss",  StringUtil.toDateTime(), 7);
		if(null==endTime || endTime.equals(""))endTime = StringUtil.getCurrentDateTime();
		model.addAttribute("ordernum", request.getParameter("ordernum"));
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("phoneNum", request.getParameter("phoneNum"));
		model.addAttribute("dealer", request.getParameter("dealer"));
		model.addAttribute("equipmentnum", request.getParameter("equipmentnum"));
		//model.addAttribute("port", request.getParameter("port"));
		model.addAttribute("type", request.getParameter("type"));
		model.addAttribute("paystatus", request.getParameter("paystatus"));
		model.addAttribute("orderstatu", CommUtil.toString(request.getParameter("orderstatu")));
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("orderSimuCoins", pageBean.getListMap());
		return "computer/orderSimuCoins";
	}
	
	/**
	 * 钱包、卡充值信息查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/selectWalletRecord")
	public String selectWalletRecord(HttpServletRequest request, HttpServletResponse response, Model model){
		PageUtils<Parameters> pageBean = moneyService.selectWalletRecord(request);
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(null==startTime || startTime.equals("")) startTime = StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss",  StringUtil.toDateTime(), 7);
		if(null==endTime || endTime.equals(""))endTime = StringUtil.getCurrentDateTime();
		model.addAttribute("ordernum", request.getParameter("ordernum"));
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("realname", request.getParameter("realname"));
		model.addAttribute("phoneNum", request.getParameter("phoneNum"));
		model.addAttribute("paytype", request.getParameter("paytype"));
		model.addAttribute("cardID", request.getParameter("cardID"));
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("orderWalletPay", pageBean.getListMap());
		return "computer/ordermoney";
	}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	//查询交易记录订单详情     
	@RequestMapping(value = "/detailsRestsOrderinfo")
	public String detailsRestsOrderinfo(Integer paysource, Integer status, String ordernum, Model model) {
		Integer state = 1;
		Integer source = 1;
		if(paysource==1){
			state = status == 0 ? 1 : 2;
			List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
			ChargeRecord record = chargeRecord.size() > 0 ? chargeRecord.get(0) : new ChargeRecord();
			if(record!=null && record.getPaytype()==4){
				source = 2;
				List<PackageMonthRecord> packagemont = tradeRecordService.selectMonthRecordEntiy(ordernum, state);
				PackageMonthRecord partrecord = packagemont.size() > 0 ? packagemont.get(0) : new PackageMonthRecord();
				model.addAttribute("partrecord", partrecord);
			}
			model.addAttribute("order", record);
		}else if(paysource==2){
			if(status==4 || status==4 || status==5 || status==7 || status==9 || status==11 ) state = 2;
			InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
			model.addAttribute("order", inCoins);
		}else if(paysource==3){
			status = status == null ? 2 : status;
			if(status==3 || status==4 || status==6) state = 2;
			else source = 2;
			OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
			offlineCard = offlineCard == null ? new OfflineCard() : offlineCard;
			model.addAttribute("order", offlineCard);
		}else if(paysource==4){
			if(status==2 || status==3 || status==4 || status==5 || status==6 || status==8) state = 2;
			Money money = moneyService.queryMoneyByOrdernum(ordernum);
			money = money == null ? new Money() : money;
			model.addAttribute("order", money);
		}else if(paysource==5){
			if(status==2) state = 2;
			OnlineCardRecord cardRecord = onlineCardRecordService.selectRecordByOrdernum(ordernum, null, status);
			model.addAttribute("order", cardRecord);
		}else if(paysource==6){
			if(status==2) state = 2;
			List<PackageMonthRecord> packagemont = tradeRecordService.selectMonthRecordEntiy(ordernum, status);
			PackageMonthRecord record = packagemont.size() > 0 ? packagemont.get(0) : new PackageMonthRecord();
			if(record!=null && record.getPaysource()==2){
				source = 2;
				List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
				ChargeRecord partrecord = chargeRecord.size() > 0 ? chargeRecord.get(0) : new ChargeRecord();
				model.addAttribute("partrecord", partrecord);
			}
			model.addAttribute("order", record);
		}
		if(source==1){
			TradeRecord trarec = new TradeRecord();
			trarec.setOrdernum(ordernum);
			trarec.setStatus(state);
			TradeRecord trade = tradeRecordService.getTraderecordList(trarec);
			model.addAttribute("partrecord", trade);
		}
		model.addAttribute("paysource", paysource);
		model.addAttribute("source", source);
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		return "comorder/orderDetailsInfo";
	}
	
	//查询交易记录订单详情     
	@RequestMapping(value = "/detailsTradeOrderinfo")
	public String TraderecordDetails(Integer orderid, Model model) {
		TradeRecord trade = tradeRecordService.selectTradeById(orderid);
		String ordernum = trade.getOrdernum();
		Integer paysource = trade.getPaysource();
		Integer status = trade.getStatus();
		Integer paytype = trade.getPaytype();
		if(paysource==1){
			List<ChargeRecord> chargeRecord = chargeRecordService.getRecordData(ordernum,paytype);
			ChargeRecord record = chargeRecord.size() > 0 ? chargeRecord.get(0) : new ChargeRecord();
			model.addAttribute("order", record);
		}else if(paysource==2){
			InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
			model.addAttribute("order", inCoins);
		}else if(paysource==3){
			OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
			offlineCard = offlineCard == null ? new OfflineCard() : offlineCard;
			model.addAttribute("order", offlineCard);
		}else if(paysource==4){
			Money money = moneyService.queryMoneyByOrdernum(ordernum);
			money = money == null ? new Money() : money;
			model.addAttribute("order", money);
		}else if(paysource==5){
			OnlineCardRecord online = new OnlineCardRecord();
			online.setOrdernum(ordernum);
			List<OnlineCardRecord> onlinelist = onlineCardRecordService.selectCardRecord(online);
			OnlineCardRecord onlinerecord = new OnlineCardRecord();
			for(OnlineCardRecord item : onlinelist){
				Integer type = item.getType();
				if(status==1){
					if(type==1 || type==3 || type==6 || type==8) onlinerecord = item;
				}else if(status==2){
					if(type==1 || type==3 || type==6 || type==8) onlinerecord = item;
				}
			}
			//OnlineCardRecord cardRecord = onlineCardRecordService.selectRecordByOrdernum(ordernum, null, status);
			model.addAttribute("order", onlinerecord);
		}else if(paysource==6){
			List<PackageMonthRecord> packagemont = tradeRecordService.selectMonthRecordEntiy(ordernum, status);
			PackageMonthRecord record = packagemont.size() > 0 ? packagemont.get(0) : new PackageMonthRecord();
			model.addAttribute("order", record);
		}
		model.addAttribute("partrecord", trade);
		model.addAttribute("paysource", paysource);
		model.addAttribute("paytype", paytype);
		model.addAttribute("status", status);
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		return "comorder/orderDetailsInfo";
	}
	
	//查询交易记录订单详情     
	@RequestMapping(value = "/TraderecordDetails")
	public String TraderecordDetails(HttpServletRequest request, HttpServletResponse response, Model model) {
		String ordernum = request.getParameter("ordernum");
		String paysource = request.getParameter("paysource");
		String state = CommUtil.toString(request.getParameter("status"));
		Integer status = null;
		//支付来源 1、充电模块-2、脉冲模块-3、离线充值机-4、用户充值钱包',
		if (paysource.equals("1")) {
			List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
			status = chargeRecord.get(0).getNumber() == 0 ? 1 : 2;
			model.addAttribute("order", chargeRecord.get(0));
		} else if (paysource.equals("2")) {
			InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
			status = inCoins.getHandletype() == 4 ? 2 : inCoins.getHandletype() == 5 ? 2 : inCoins.getHandletype() == 7 ? 2 : 1 ;
			if(status==4 || status==5 || status==7){
				 status = 2;
			}else{
				status = 1;
			}
			model.addAttribute("order", inCoins);
		} else if (paysource.equals("3")) {
			OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
			status = offlineCard.getPaytype();
			if(status==3 || status==4 || status==6){
				 status = 2;
			}else{
				status = 1;
			}
			model.addAttribute("order", offlineCard);
		} else if (paysource.equals("4")) {
			Money money = moneyService.queryMoneyByOrdernum(ordernum);
			status = money.getPaytype();
			if(status==0 || status==1 ){
				 status = 1;
			}else{
				status = 2;
			}
			model.addAttribute("order", money);
		} else if (paysource.equals("5")) {
			Integer obje = StringUtil.getIntString(request.getParameter("obje"));
			Integer orderid = StringUtil.getIntString(request.getParameter("orderid"));
			OnlineCardRecord cardRecord = null;
			if(obje==1){
				TradeRecord trade = new TradeRecord();
				trade.setId(orderid);
				TradeRecord tradereco = (TradeRecord) tradeRecordService.getTraderecordList(trade);
				Integer stat = tradereco.getStatus();
				Integer paytype = tradereco.getPaytype();
				Integer type = null;
				if(stat==1 && paytype==2){
					type = 3;
				}else if(stat==1 && paytype==3){
					type = 6;
				}else if(stat==2 && paytype==2){
					type = 5;
				}else if(stat==2 && paytype==3){
					type = 7;
				}
				cardRecord = onlineCardRecordService.selectRecordByOrdernum(ordernum, type, null);
			}else{
				cardRecord = onlineCardRecordService.selectOnlineCard(orderid);
			}
			if(cardRecord!=null){
				status = cardRecord.getType();
				if(status==2){
					status = 2;
				}else{
					status = 1;
				}
			}
			model.addAttribute("order", cardRecord);
		} else if (paysource.equals("6")) {
			status = CommUtil.toInteger(state);
			if(status==null) status = 1;
			List<PackageMonthRecord> packagemont = tradeRecordService.selectMonthRecordEntiy(ordernum, status);
			model.addAttribute("order", packagemont.get(0));
		} 
		if(!paysource.equals("6")){
			Map<String, Object> trade = tradeRecordService.selectTradeDetails(ordernum,status);
			model.addAttribute("trade", trade);
		}
		model.addAttribute("paysource", paysource);
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		return "comorder/ordertradetails";
	}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	//查询提现记录
	@RequestMapping(value = "/selectWithdrawRecord")
	public String selectWithdrawRecord(HttpServletRequest request, HttpServletResponse response, Model model){
		PageUtils<Parameters> pageBean = withdrawService.selectWithdrawRecord(request);
		model.addAttribute("type", request.getParameter("type")==null ? 0 : request.getParameter("type"));
		model.addAttribute("withdrawnum", request.getParameter("withdrawnum"));
		model.addAttribute("realname", request.getParameter("realname"));
		model.addAttribute("phoneNum", request.getParameter("phoneNum"));
		model.addAttribute("bankcardnum", request.getParameter("bankcardnum"));
		model.addAttribute("bankname", request.getParameter("bankname"));
		model.addAttribute("status", request.getParameter("status"));
		model.addAttribute("startTime", request.getParameter("startTime")!=null ? request.getParameter("startTime") : StringUtil.getPastDate(30) + " 00:00:00");
		model.addAttribute("endTime", request.getParameter("endTime")!=null ? request.getParameter("endTime") : StringUtil.toDateTime());
		model.addAttribute("withdrawList", pageBean.getListMap());
		model.addAttribute("pageBean", pageBean);
		return "computer/managewithdraw";
	}
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	
	/**
	 * 判断密码是否正确
	 * @param password
	 * @return
	 */
	public boolean judge( String password){
		User admin = (User) this.request.getSession().getAttribute("admin");
		String pwd = DigestUtils.md5Hex(password);
		if(!admin.getPassword().equals(pwd)){
			return false;
		}
		return true;
	}
	
	
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/

}
