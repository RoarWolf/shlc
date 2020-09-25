package com.hedong.hedongwx.web.controller.computerpc;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.Withdraw;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.StatisticsService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.service.WithdrawService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Controller
@RequestMapping(value="/pcstatistics")
public class StatisticsController { //统计
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private WithdrawService withdrawService;
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * @Description： PC首页统计汇总信息(now)
	 * @author： origin          
	 * 创建时间：   2019年5月31日 上午11:49:07
	 */
	@RequestMapping(value="/collectinfo")
	public Object collectinfo(HttpServletRequest request, HttpServletResponse response, Model model){
		//设备
		List<Equipment> equipment = equipmentService.getEquipmentList();
		int online = 0;
		int binding = 0;
		for(Equipment eqm : equipment){
			if(eqm.getState()==1) online += 1;
			if(eqm.getBindtype()==1) binding += 1;
		}
		String ditequ = equipmentService.selectdisequ();//计算有多少个有效商户
		
//		Map<String, Object> codelines = CommUtil.isMapEmpty(userEquipmentService.selectustoequ(user.getId()));
		
		
		Withdraw withdraw = new Withdraw();
		Map<String, Object> extractTotal = withdrawService.selectmoneysum(withdraw);//extractmoney:提现金额  sumservicecharge:手续费
		Map<String, Object> pendingTotal = withdrawService.pendingmoney(null, "0,4", null, null);//查询待提现金额
		Integer uid = null;
		Double earningsMoney = CommUtil.toDouble(userService.earningsMoney(uid));
		Double extractmoney = CommUtil.toDouble(extractTotal.get("extractmoney"));
		Double sumservicecharge = CommUtil.toDouble(extractTotal.get("sumservicecharge"));
		Double penextractmoney = CommUtil.toDouble(pendingTotal.get("extractmoney"));
		Double penservicecharge = CommUtil.toDouble(pendingTotal.get("sumservicecharge"));
		
		model.addAttribute("equnum", equipment.size());
		model.addAttribute("online", online);
		model.addAttribute("binding", binding);
		model.addAttribute("ditequ", ditequ);
		

		//历史总订单信息 dealerIncomeCount
		Map<String, Object> agentdata = CommUtil.isMapEmpty(statisticsService.dealerMoneyCollect(null));
		//当天订单统计
		Map<String, Object> agentnowdata = CommUtil.isMapEmpty(statisticsService.dealerNowMoneyCollect(null));
		
//		Map<String, Object>  totalmessage = statisticsService.totalmessagerecord();
//		Map<String, BigDecimal>  earningsnow = statisticsService.earningsinfonow();
//		totalmessage = (Map<String, Object>) totalmessage.get("totalmap");
		Map<String, Object> datainfo = new HashMap<String, Object>();

		datainfo = agentdata;
		datainfo.put("extractmoney", extractmoney);
		datainfo.put("sumservicecharge", sumservicecharge);
		datainfo.put("earningsmoney", earningsMoney);
		datainfo.put("penextractmoney", penextractmoney);
		datainfo.put("penservicecharge", penservicecharge);
		model.addAttribute("datainfo", datainfo);
		
		model.addAttribute("totalmap", agentdata);
		model.addAttribute("totalNowmap", agentnowdata);
		return "computer/collectmessage";
	}
	
	/**
	 * @Description： PC端【图形首页】页面查询、处理
	 * @author： origin          
	 * 创建时间：   2019年5月31日 上午11:49:21
	 */
	@RequestMapping(value="/graphindex")
    public String graphindex( Model model) throws ParseException{
		User admin = (User) request.getSession().getAttribute("admin");
		Integer merid = null;
		if(admin.getLevel()==2){
			merid = admin.getId();
			Withdraw withdraw = new Withdraw();
			withdraw.setUserId(merid);
			Map<String, Object> extractTotal = withdrawService.selectmoneysum(withdraw);//extractmoney:提现金额  sumservicecharge:手续费
			Double extractmoney = 0.00;
			Double sumservicecharge = 0.00;
			if(extractTotal!=null){
				extractmoney = StringUtil.togetBigDecimal(extractTotal.get("extractmoney")).doubleValue();
				sumservicecharge = StringUtil.togetBigDecimal(extractTotal.get("sumservicecharge")).doubleValue();
			}
			Double totalmoney = (admin.getEarnings()*100 + extractmoney*100 + sumservicecharge*100)/100;
			Map<String, Double> totalmap = new HashMap<String, Double>();
			totalmap.put("totalmoney", totalmoney);
			model.addAttribute("totalmap", totalmap);
		}else{
			//总的营业额
			Map<String, Object>  totalmessage = statisticsService.totalmessagerecord();
			model.addAttribute("totalmap", totalmessage.get("totalmap"));
		}
		
		//人数
		Map<String, Object>  tourist = statisticsService.getTouristPeople( merid, 1);
		//设备
		List<Equipment> equipment = equipmentService.getEquipmentList();
		int online = 0;
		int binding = 0;
		for(Equipment eqm : equipment){
			if(eqm.getState()==1) online += 1;
			if(eqm.getBindtype()==1) binding += 1;
		}
		tourist.put("online", online);
		tourist.put("binding", binding);
		tourist.put("qeuamount", equipment.size());
		//今日营业额
//		Map<String, BigDecimal>  earningsnow = statisticsService.earningsinfonow();
		Map<String, Object>  earningsnow = statisticsService.earningsinfonow();
		model.addAttribute("earningsnow", earningsnow);
		//昨日营业额
		String time = StringUtil.getPastDate(1);
		String startTime = time + " 00:00:00";
		String endTime = time + " 23:59:59";
		Map<String, Object>  earningsyester = statisticsService.earningsinfoyes(startTime, endTime);
		model.addAttribute("earningsyest", earningsyester);
		
		//近一周营业额的趋势
		String weekstartTime = StringUtil.getPastDate(7) + " 00:00:00";
		String weekendTime = StringUtil.getPastDate(0) + " 23:59:59";
		Object hebdomadmap = statisticsService.sometimeturnover( weekstartTime, weekendTime);
		model.addAttribute("hebdomadmap", hebdomadmap);
		//最近订单信息（交易）
		Object sometimeorder = statisticsService.sometimeorder();
		model.addAttribute("sometimeorder", sometimeorder);
		//昨日设备营业收入排行统计
		List<Map<String, Object>> codeincome = statisticsService.codeincome();
		if(codeincome==null) codeincome = new ArrayList<Map<String, Object>>();
		if(codeincome.size()>16){
			codeincome = codeincome.subList(0,16);
		}
		model.addAttribute("codeincome", codeincome);
		model.addAttribute("user", admin);
		model.addAttribute("tourist", tourist);
		return "computer/indexgrap";
		//return "computer/indexgraph";
	}
	
	@RequestMapping(value="/newestOrder")
	@ResponseBody
    public Object newestOrder(Integer traid){
		Integer merid = null;
		User admin = (User) request.getSession().getAttribute("admin");
		if(admin.getLevel()==2) merid = admin.getId();
		Object sometimeorder = tradeRecordService.newestOrder(traid+1, merid);
		return sometimeorder;
	}
	
	/**
	 * @Description： 商户历史每日金额汇总信息查询
	 * @author： origin      
	 */
	@RequestMapping(value="/agentcollectinfo")
    public String agentcollectinfo( Model model) throws ParseException{
		
		PageUtils<Parameters> pageBean = statisticsService.codeEarningsCollect(2);
		
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("startTime", request.getParameter("startTime"));
		model.addAttribute("endTime", request.getParameter("endTime"));

		model.addAttribute("sort", request.getParameter("sort"));
		model.addAttribute("rank", request.getParameter("rank"));
		
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("codeEarnings", pageBean.getListMap());
		model.addAttribute("calculate", pageBean.getMap());
		model.addAttribute("traderecord", pageBean.getListMap());
		return "computer/agentcollectinfo";
	}
	
	
	
	//设备当天金额汇总信息查询
	@RequestMapping(value="/nowcodeearnings")
    public String nowcodeearnings( Model model) throws ParseException{
		PageUtils<Parameters> pageBean = statisticsService.nowcodeearnings();
		
		model.addAttribute("code", request.getParameter("code"));
//		model.addAttribute("nickname", request.getParameter("nickname"));
//		model.addAttribute("areaname", request.getParameter("areaname"));
		
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("calculate", pageBean.getMap());
		model.addAttribute("codeEarnings", pageBean.getListMap());
		//model.addAttribute("statotal", pageBean.getMap());
		return "computer/codenowearnings";
	}
		
	//设备历史每日金额汇总信息查询
	@RequestMapping(value="/codeearningscollect")
    public String codeearningscollect( Model model) throws ParseException{
		PageUtils<Parameters> pageBean = statisticsService.codeEarningsCollect(1);
		
		model.addAttribute("code", request.getParameter("code"));
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("areaname", request.getParameter("areaname"));
		model.addAttribute("startTime", request.getParameter("startTime"));
		model.addAttribute("endTime", request.getParameter("endTime"));

		model.addAttribute("sort", request.getParameter("sort"));
		model.addAttribute("rank", request.getParameter("rank"));
		
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("codeEarnings", pageBean.getListMap());
		model.addAttribute("calculate", pageBean.getMap());
		model.addAttribute("traderecord", pageBean.getListMap());
		return "computer/codeearningscollect";
	}
	
	// 商户统计信息
	@RequestMapping(value="/agentdatacollect")
	public Object agentdatacollect(HttpServletRequest request, HttpServletResponse response, Model model){
		//设备信息
		User user = CommonConfig.getAdminReq(request);
		Integer dealid = CommUtil.toInteger(user.getId());
		Map<String, Object> codelines = CommUtil.isMapEmpty(userEquipmentService.selectustoequ(user.getId()));
		//历史总订单信息 dealerIncomeCount
		Map<String, Object> agentdata = CommUtil.isMapEmpty(statisticsService.dealerMoneyCollect(dealid));
		//当天订单统计
		Map<String, Object> agentnowdata = CommUtil.isMapEmpty(statisticsService.dealerNowMoneyCollect(dealid));
		//累计提现金额
		Withdraw withdraw = new Withdraw();
		withdraw.setUserId(dealid);
		Map<String, Object> extractTotal = CommUtil.isMapEmpty(withdrawService.selectmoneysum(withdraw));//extractmoney:提现金额  sumservicecharge:手续费
		Double extractmoney = CommUtil.toDouble(extractTotal.get("extractmoney"));
		Double sumservicecharge = CommUtil.toDouble(extractTotal.get("sumservicecharge"));
		Map<String, Object> pendingTotal = withdrawService.pendingmoney(dealid, "0,4", null, null);//查询待提现金额
		Double penextractmoney = CommUtil.toDouble(pendingTotal.get("extractmoney"));
		Double penservicecharge = CommUtil.toDouble(pendingTotal.get("sumservicecharge"));
		Double earningsmoney = CommUtil.toDouble(user.getEarnings());
		Map<String, Object> datainfo = new HashMap<String, Object>();
		datainfo = agentdata;
		datainfo.put("extractmoney", extractmoney);
		datainfo.put("sumservicecharge", sumservicecharge);
		datainfo.put("penextractmoney", penextractmoney);
		datainfo.put("penservicecharge", penservicecharge);
		datainfo.put("earningsmoney", earningsmoney);
		model.addAttribute("datainfo", datainfo);
		
		model.addAttribute("codelines", codelines);
		model.addAttribute("agentdata", agentdata);
		model.addAttribute("agentnowdata", agentnowdata);
		model.addAttribute("useradmin", user);
		return "computer/collectagentmessage";
	}
//	// 商户统计信息
//	@RequestMapping(value="/agentdatacollect")
//	public Object agentdatacollect(HttpServletRequest request, HttpServletResponse response, Model model){
//		//设备信息
//		User user = CommonConfig.getAdminReq(request);
//		Integer merid = CommUtil.toInteger(user.getId());
//		Map<String, Object> codelines = CommUtil.isMapEmpty(userEquipmentService.selectustoequ(user.getId()));
//		//历史总订单信息
//		Map<String, Object> agentdata = CommUtil.isMapEmpty(statisticsService.agentmoneycollect());
//		//当天订单统计
//		Map<String, Object> agentnowdata = CommUtil.isMapEmpty(statisticsService.agentnowmoneycollect());
//		//累计提现金额
//		Withdraw withdraw = new Withdraw();
//		withdraw.setUserId(merid);
//		Map<String, Object> extractTotal = CommUtil.isMapEmpty(withdrawService.selectmoneysum(withdraw));//extractmoney:提现金额  sumservicecharge:手续费
//		
//		Double extractmoney = CommUtil.toDouble(extractTotal.get("extractmoney"));
//		Double sumservicecharge = CommUtil.toDouble(extractTotal.get("sumservicecharge"));
//		
//		Map<String, Object> datainfo = new HashMap<String, Object>();
//
//		Double totalmoney = CommUtil.addBig(CommUtil.toDouble(agentdata.get("moneytotal")), CommUtil.toDouble(agentnowdata.get("moneytotal")));
//		Integer totalorder = CommUtil.toInteger(agentdata.get("ordertotal")) + CommUtil.toInteger(agentnowdata.get("ordertotal"));
//		
//		Double wechatmoney = CommUtil.addBig(CommUtil.toDouble(agentdata.get("wechatmoney")), CommUtil.toDouble(agentnowdata.get("wechatmoney")));
//		Integer wechatorder = CommUtil.toInteger(agentdata.get("wechatorder")) + CommUtil.toInteger(agentnowdata.get("wechatnum"));
//		
//		Double alipaymoney = CommUtil.addBig(CommUtil.toDouble(agentdata.get("alipaymoney")), CommUtil.toDouble(agentnowdata.get("alipaymoney")));
//		Integer alipayorder = CommUtil.toInteger(agentdata.get("alipayorder")) + CommUtil.toInteger(agentnowdata.get("alipaynum"));
//
//		Double refwechatmoney = CommUtil.addBig(CommUtil.toDouble(agentdata.get("wechatretmoney")), CommUtil.toDouble(agentnowdata.get("refwechatmoney")));
//		Integer refwechatorder = CommUtil.toInteger(agentdata.get("wechatretord")) + CommUtil.toInteger(agentnowdata.get("refwechatnum"));
//		
//		Double refalipaymoney = CommUtil.addBig(CommUtil.toDouble(agentdata.get("alipayretmoney")), CommUtil.toDouble(agentnowdata.get("refalipaymoney")));
//		Integer refalipayorder = CommUtil.toInteger(agentdata.get("alipayretord")) + CommUtil.toInteger(agentnowdata.get("refalipaynum"));
//		
//		Double onlinemoney = CommUtil.addBig(wechatmoney, alipaymoney);
//		Double refonlinemoney = CommUtil.addBig(refwechatmoney, refalipaymoney);
//		Double incomemoney = CommUtil.subBig(totalmoney, extractmoney);
//		Double earningmoney = CommUtil.subBig(onlinemoney, refonlinemoney);
//		datainfo.put("onlinemoney", onlinemoney);
//		datainfo.put("refonlinemoney", refonlinemoney);
//		datainfo.put("earningmoney", earningmoney);
//		datainfo.put("incomemoney", incomemoney);
//		datainfo.put("totalmoney", totalmoney);
//		datainfo.put("totalorder", totalorder);
//		datainfo.put("wechatmoney", wechatmoney);
//		datainfo.put("wechatorder", wechatorder);
//		datainfo.put("alipaymoney", alipaymoney);
//		datainfo.put("alipayorder", alipayorder);
//		datainfo.put("refwechatmoney", refwechatmoney);
//		datainfo.put("refwechatorder", refwechatorder);
//		datainfo.put("refalipaymoney", refalipaymoney);
//		datainfo.put("refalipayorder", refalipayorder);
//		datainfo.put("extractmoney", extractmoney);
//		datainfo.put("sumservicecharge", sumservicecharge);
//		model.addAttribute("datainfo", datainfo);
//		
//		model.addAttribute("codelines", codelines);
//		model.addAttribute("agentdata", agentdata);
//		model.addAttribute("agentnowdata", agentnowdata);
//		model.addAttribute("useradmin", user);
//		return "computer/collectagentmessage";
//	}
	
	// 统计信息
	@RequestMapping(value="/forthwithcollectinfo")
	public Object forthwithcollectinfo(HttpServletRequest request, HttpServletResponse response, Model model){
		//设备信息
		Parameters parameters = new Parameters();
		User user = CommonConfig.getAdminReq(request);
		List<Map<String, Object>> equimap = equipmentService.selectEquListByParam(parameters);
		int totalRows = equimap.size();
		int online = 0;
		int binding = 0;
		for( Map<String, Object> iteam : equimap){
			if(iteam.get("state").toString().equals("1")) online += 1;
			if(iteam.get("bindtype").toString().equals("1")) binding += 1;
		}
		model.addAttribute("user", user);
		model.addAttribute("totalRows", totalRows);
		model.addAttribute("online", online);
		model.addAttribute("disonline", totalRows - online);
		model.addAttribute("binding", binding);
		model.addAttribute("disbinding", totalRows - binding);
		
		//有效客户（有效商户；绑定设备的人员）
		model.addAttribute("validclient", equipmentService.selectdisequ());
		
		Withdraw withdraw = new Withdraw();
		Map<String, Object> extractTotal = withdrawService.selectmoneysum(withdraw);//extractmoney:提现金额  sumservicecharge:手续费
		Double extractmoney = 0.00;
		Double sumservicecharge = 0.00;
		if(extractTotal!=null){
			extractmoney = StringUtil.togetBigDecimal(extractTotal.get("extractmoney")).doubleValue();
			sumservicecharge = StringUtil.togetBigDecimal(extractTotal.get("sumservicecharge")).doubleValue();
		}
		model.addAttribute("extractmoney", extractmoney);
		model.addAttribute("sumservicecharge", sumservicecharge);
		
		//历史订单信息汇总
		Map<String, Object> collectinfo = statisticsService.collectinfo();
		Map<String, Object> collectincometotal = statisticsService.selectcollectincometotal();
		Map<String, Object> collectrefundtotal = statisticsService.selectcollectrefundtotal();

		model.addAttribute("collectinfo", collectinfo);
		model.addAttribute("totalinfo", collectincometotal.get("totalinfo"));
		model.addAttribute("nowtotalinfo", collectincometotal.get("nowtotalinfo"));
		model.addAttribute("reftotalinfo", collectrefundtotal.get("totalinfo"));
		model.addAttribute("refnowtotalinfo", collectrefundtotal.get("nowtotalinfo"));
		
		return "computer/collectmessage";
	}
	
	//历史统计信息查询
	@RequestMapping(value="/statisticsinfo")
	public Object statisticsinfo(HttpServletRequest request, HttpServletResponse response, Model model){
		PageUtils<Parameters> pageBean = statisticsService.selectInfo(request);
		
		model.addAttribute("ordertotal", request.getParameter("ordertotal"));
		model.addAttribute("ordermoney", request.getParameter("ordermoney"));
		model.addAttribute("begintime", request.getParameter("begintime"));
		model.addAttribute("endtime", request.getParameter("endtime"));
		
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("statistics", pageBean.getListMap());
		model.addAttribute("statotal", pageBean.getMap());
		return "computer/statisticstotal";
	}
	
	
}
