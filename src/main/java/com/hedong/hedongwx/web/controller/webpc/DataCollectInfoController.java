package com.hedong.hedongwx.web.controller.webpc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.Withdraw;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.FeescaleService;
import com.hedong.hedongwx.service.StatisticsService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.service.WithdrawService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.JedisUtils;
import com.hedong.hedongwx.utils.StringUtil;

/**
 * @Description： 数据汇总信息控制类
 * @author  origin  创建时间：   2019年11月1日 下午5:27:16  
 */
@Controller
@RequestMapping(value="/dataCollectInfo")
public class DataCollectInfoController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private WithdrawService withdrawService;
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private FeescaleService feescaleService;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private TradeRecordService tradeRecordService;
	
	
	/**
	 * @method_name: tradeRealtimeInfo
	 * @Description: 进入图形收益查询交易实时数据
	 * @Author: origin  创建时间:2020年9月3日 下午3:05:58
	 */
	@RequestMapping(value="/tradeRealtimeInfo")
	@ResponseBody
	public Object tradeRealtimeInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			if(CommonConfig.isExistSessionUser(request)){
				resultdata = CommUtil.responseBuild(901, "session缓存失效", "");
			}else{
				User admin = (User) request.getSession().getAttribute("admin");
				Parameters parame = new Parameters();
				if(admin.getLevel()==2) parame.setUid(admin.getId());
				parame.setStartnumber(0);
				parame.setPages(14);
				List<Map<String, Object>>  trade = tradeRecordService.tradeDetailsinquire(parame);
				if(trade==null) trade = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>>  resulttrade = new ArrayList<Map<String, Object>>();
				for(Map<String, Object> item : trade){
					Map<String, Object> map = new HashMap<String, Object>();
					String ordernum = CommUtil.toString(item.get("ordernum"));
					map.put("code", CommUtil.toString(item.get("code")));
					map.put("money", CommUtil.toString(item.get("money")));
					map.put("paytype", CommUtil.toString(item.get("paytype")));
					map.put("type", CommUtil.toString(item.get("paysource")));
					map.put("order", ordernum.substring(ordernum.length()-4));
					resulttrade.add(map);
				}
				resultdata.put("resulttrade", resulttrade);
				CommUtil.responseBuildInfo(200, "成功", resultdata);
			}
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", resultdata);
		}
		return resultdata;
	}
	
	
	/**
	 * @method_name: inquireDeviceIncomeInfo
	 * @Description: 查询地图首页当行数据
	 * @param 无需传参
	 * @return
	 * @Author: origin  创建时间:2020年8月31日 上午11:23:13
	 * @common:
	 */
	@RequestMapping(value="/inquireGraphInfo")
	@ResponseBody
	public Object inquireGraphInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if(CommonConfig.isExistSessionUser(request)){
				result = CommUtil.responseBuild(901, "session缓存失效", "");
			}else{
				Map<String, Object> maparam = CommUtil.getRequestParam(request);
				//datatype 1: 直接从redis缓存中获取数据    其他:刷新获取最新数据并存入缓存中以便于下次获取
				Integer datatype = CommUtil.toInteger(maparam.get("type"));
				String paramredis = "Graph";
				String graphData = JedisUtils.getnum(paramredis,1);
				if(graphData!=null){//redis中存在
					if(datatype.equals(0)){//进入读取数据、redis存在数据
						result = CommUtil.toHashMaps(JSON.parse(graphData));
						String redisrenewalTime = CommUtil.toString(result.get("renewalTime"));
						Date redisrenewalDate = StringUtil.DateTime( redisrenewalTime, "yyyy-MM-dd HH:mm:ss");
						if(CommUtil.isToday(redisrenewalDate)){//在当天
							result.put("refresh", 1);
							CommUtil.responseBuildInfo(200, "SUCCEED", result);
						}else{//不在当天
							result.put("refresh", 2);
							CommUtil.responseBuildInfo(200, "", result);
						}
						return result;
					}
				}
				Integer dealid = null;
				//人数
				Map<String, Object>  tourist = statisticsService.getTouristPeople( dealid, 1);

				//String validdealer = equipmentService.selectdisequ();//计算有多少个有效商户
				//设备
				Map<String, Object> deviceCount = equipmentService.inquireDeviceCount(dealid);
				//今日营业额
				Map<String, Object>  earningsnow = statisticsService.earningsinfonow();
				String time = StringUtil.getPastDate(1);
				String startTime = time + " 00:00:00";
				String endTime = time + " 23:59:59";
				//昨日营业额
				Map<String, Object>  earningsyester = statisticsService.earningsinfoyes(startTime, endTime);
				//总的营业额
				Map<String, Object>  totalmessage = statisticsService.earningsinfoyes(null, null);
				result.put("alltourist", tourist.get("alltourist"));
				result.put("ispeople", tourist.get("ispeople"));
				result.put("daytourist", tourist.get("daytourist"));
				result.put("monthtourist", tourist.get("monthtourist"));
				result.put("devicetotal", deviceCount.get("devicetotal"));
//				result.put("onbinding",  deviceCount.get("onbinding"));
//				result.put("online",  deviceCount.get("online"));
//				result.put("disline",  deviceCount.get("disline"));
//				result.put("disbinding",  deviceCount.get("disbinding"));
				result.put("earningsnowmoney",  earningsnow.get("nowonlineearn"));
				result.put("earningsyestotal", earningsyester.get("ordertotal"));
				result.put("earningsyesmoney", earningsyester.get("moneytotal"));
				result.put("earningstotal", totalmessage.get("ordertotal"));
				result.put("earningsmoney", totalmessage.get("moneytotal"));
				Date renewalDate = new Date();
				String renewalTime = CommUtil.toDateTime(renewalDate);
				result.put("renewalDate", renewalDate);
				result.put("renewalTime", renewalTime);
				JedisUtils.setnum(paramredis, JSON.toJSONString(result), 864000, 1);//1296600 172800
				result.put("refresh", 1);
				CommUtil.responseBuildInfo(200, "成功", result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", result);
		}
		return result;
	}
	

	
	
	/**
	 * @method_name: inquireDeviceIncomeInfo
	 * @Description: 查询昨日支付占比
	 * @param 无需传参
	 * @return
	 * @Author: origin  创建时间:2020年8月31日 上午11:23:13
	 * @common:
	 */
	@RequestMapping(value="/inquirePaymentratioInfo")
	@ResponseBody
	public Object inquirePaymentratioInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			if(CommonConfig.isExistSessionUser(request)){
				resultdata = CommUtil.responseBuild(901, "session缓存失效", "");
			}else{
				String beginTime = DisposeUtil.getPastDate(1,1);
				String endTime = DisposeUtil.getPastDate(1,1);
				beginTime = beginTime + " 00:00:00";
				endTime = endTime + " 23:59:59";
				Map<String, Object> result = statisticsService.statisticsData(null, 2, beginTime, endTime);
				Parameters parame = new Parameters();
				parame.setSource("4");
				parame.setType("1");
				parame.setStartTime(beginTime);
				parame.setEndTime(endTime);
				Map<String, Object> resultwallet = tradeRecordService.tradeComputeMap(parame);
				//查询昨日收入的窗口投币、离线卡刷卡、在线卡刷卡数据
				Map<String, Object> resultMap = chargeRecordService.chargeRecordCompute(null, beginTime, endTime);
				

				Integer incoinsmoney = CommUtil.toInteger(result.get("incoinsmoney"));
				CommUtil.toInteger(result.get("consumetime"));
				Integer windowpulsenum = CommUtil.toInteger(resultMap.get("windowpulsenum"));
				Integer offcardnum = CommUtil.toInteger(resultMap.get("offcardnum"));
				Integer oncardnum = CommUtil.toInteger(resultMap.get("oncardnum"));
				
				Integer incoinsratio = CommUtil.toInteger(incoinsmoney + windowpulsenum);
				Integer cardratio = CommUtil.toInteger(offcardnum + oncardnum);

				resultdata.put("WeChatratio", CommUtil.toInteger(result.get("wechatorder")));
				resultdata.put("alipayratio", CommUtil.toInteger(result.get("alipayorder")));
				resultdata.put("walletratio", CommUtil.toInteger(resultwallet.get("num")));
				resultdata.put("consumetime", CommUtil.toInteger(result.get("consumetime")));
				resultdata.put("unionpayratio", CommUtil.toInteger(result.get("unionpayordernum")));
				resultdata.put("cardratio", cardratio);
				resultdata.put("incoinsratio", incoinsratio);
				//今日营业额
				Map<String, Object>  earningsnow = statisticsService.earningsinfonow();
				resultdata.put("earningsnowmoney",  CommUtil.toDouble(earningsnow.get("nowonlineearn")));
				
				CommUtil.responseBuildInfo(200, "成功", resultdata);
			}
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", resultdata);
			return resultdata;
		}
	}
	
	/**
	 * @method_name: inquireDeviceIncomeInfo
	 * @Description: 查询昨日设备收益记录
	 * @param 无需传参
	 * @return
	 * @Author: origin  创建时间:2020年8月31日 上午11:23:13
	 * @common:
	 */
	@RequestMapping(value="/inquireDeviceIncomeInfo")
	@ResponseBody
	public Object inquireDeviceIncomeInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			if(CommonConfig.isExistSessionUser(request)){
				resultdata = CommUtil.responseBuild(901, "session缓存失效", "");
			}else{
				List<Map<String, Object>> deviceIncome = statisticsService.inquireDeviceIncomeInfo(request);
				resultdata.put("deviceIncome", deviceIncome);
				CommUtil.responseBuildInfo(200, "成功", resultdata);
			}
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", resultdata);
			return resultdata;
		}
	}
	
	/**
	 * @method_name: inquirePlatformIncomeInfo
	 * @Description: 查询最近十五天交易金额
	 * @param 不需传参
	 * @Author: origin  创建时间:2020年8月31日 上午11:22:37
	 * @common:
	 */
	@RequestMapping(value="/inquirePlatformIncomeInfo")
	@ResponseBody
	public Object inquirePlatformIncomeInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			if(CommonConfig.isExistSessionUser(request)){
				resultdata = CommUtil.responseBuild(901, "session缓存失效", "");
			}else{
				List<Map<String, Object>> platform = statisticsService.inquirePlatformIncomeInfo(request);
				resultdata.put("platform", platform);
				CommUtil.responseBuildInfo(200, "成功", resultdata);
			}
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", resultdata);
			return resultdata;
		}
	}
	
	
	/**
	 * @Description： PC首页统计汇总信息(now)
	 * @author： origin          
	 * 创建时间：   2019年5月31日 上午11:49:07
	 */
	@RequestMapping(value="/dataCollectQuery")
	@ResponseBody
	public Object dataCollectQuery(HttpServletRequest request, HttpServletResponse response){
		System.err.println("进来了=================");
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			if(CommonConfig.isExistSessionUser(request)){
				resultdata = CommUtil.responseBuild(901, "session缓存失效", "");
			}else{
				//设备信息
				List<Equipment> devisedata = equipmentService.getEquipmentList();
				int online = 0;
				int binding = 0;
				for(Equipment eqm : devisedata){
					if(eqm.getState()==1) online += 1;
					if(eqm.getBindtype()==1) binding += 1;
				}
				Integer clientsnum = userService.inquireClientsNum(0);
				String validdealer = equipmentService.selectdisequ();//计算有多少个有效商户
				Integer devisenum = devisedata.size();
				// 设备总数
				resultdata.put("devisenum", devisenum);
				// 在线数
				resultdata.put("online", online);
				// 离线数
				resultdata.put("disline", CommUtil.toInteger(devisenum - online));
				// 绑定数
				resultdata.put("onbinding", binding);
				// 未绑定数
				resultdata.put("disbinding", CommUtil.toInteger(devisenum - binding));
				// 客户总数
				resultdata.put("clientsnum", clientsnum);
				// 有效商户
				resultdata.put("validdealer", validdealer);
				
				Withdraw withdraw = new Withdraw();
				Map<String, Object> extractTotal = withdrawService.selectmoneysum(withdraw);//extractmoney:提现金额  sumservicecharge:手续费
				Map<String, Object> pendingTotal = withdrawService.pendingmoney(null, "0,4", null, null);//查询待提现金额
				//缴费的数据(总缴费,微信,钱包)
				Map<String, Object> feescaleData = feescaleService.selectFeescaleTotalEarnings();
				// 未提现金额()
				Double earningsMoney = CommUtil.toDouble(userService.earningsMoney(null));
				// 提现总额()
				Double extractmoney = CommUtil.toDouble(extractTotal.get("extractmoney"));
				// 提现费用()
				Double sumservicecharge = CommUtil.toDouble(extractTotal.get("sumservicecharge"));
				// 待提现金额
				Double penextractmoney = CommUtil.toDouble(pendingTotal.get("extractmoney"));
				Double penservicecharge = CommUtil.toDouble(pendingTotal.get("sumservicecharge"));
				
				//历史总订单信息 dealerIncomeCount
				Map<String, Object> agentdata = CommUtil.isMapEmpty(statisticsService.dealerMoneyCollect(null));
				//当天订单统计
				Map<String, Object> agentnowdata = CommUtil.isMapEmpty(statisticsService.dealerNowMoneyCollect(null));
				
				Map<String, Object> datainfo = new HashMap<String, Object>();
				datainfo = agentdata;
				datainfo.put("extractmoney", extractmoney);
				datainfo.put("sumservicecharge", sumservicecharge);
				datainfo.put("earningsMoney", earningsMoney);
				datainfo.put("penextractmoney", penextractmoney);
				datainfo.put("penservicecharge", penservicecharge);
				// 总缴费
				datainfo.put("feescaleEarns", feescaleData.get("feescaleEarns"));
				// 微信缴费
				datainfo.put("wxFeescale",feescaleData.get("wxfeescale"));
				// 钱包缴费
				datainfo.put("wallentFeescale", feescaleData.get("wallentfeescale"));
				resultdata.put("totaltodayinfo", agentnowdata);
				resultdata.put("totaldatainfo", datainfo);
				CommUtil.responseBuildInfo(200, "成功", resultdata);
			}
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", resultdata);
			return resultdata;
		}
	}
	
	
	// 商户统计信息
	@RequestMapping(value="/agentdatacollect")
	@ResponseBody
	public Object agentdatacollect(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			if(CommonConfig.isExistSessionUser(request)){
				resultdata = CommUtil.responseBuild(901, "session缓存失效", "");
			}else{
				//设备信息
				User user = CommonConfig.getAdminReq(request);
				Integer merid = CommUtil.toInteger(user.getId());
				Map<String, Object> codelines = CommUtil.isMapEmpty(userEquipmentService.selectustoequ(user.getId()));
				Integer clientsnum = userService.inquireClientsNum(merid);
				Double earningsmoney = CommUtil.toDouble(user.getEarnings());
				
				resultdata.put("devisenum", CommUtil.toInteger(codelines.get("totalline")));
				resultdata.put("online", CommUtil.toInteger(codelines.get("onlines")));
				resultdata.put("disline", CommUtil.toInteger(codelines.get("disline")));
				resultdata.put("onbinding", CommUtil.toInteger(codelines.get("onbinding")));
				resultdata.put("disbinding", CommUtil.toInteger(codelines.get("disbinding")));
				resultdata.put("validdealer", 1);
				resultdata.put("clientsnum", clientsnum);
				
				//历史总订单信息 dealerIncomeCount
				Map<String, Object> agentdata = CommUtil.isMapEmpty(statisticsService.dealerMoneyCollect(merid));
				//当天订单统计
				Map<String, Object> agentnowdata = CommUtil.isMapEmpty(statisticsService.dealerNowMoneyCollect(merid));
				
				//历史总订单信息
//				Map<String, Object> agentdata = CommUtil.isMapEmpty(statisticsService.agentmoneycollect());
//				//当天订单统计
//				Map<String, Object> agentnowdata = CommUtil.isMapEmpty(statisticsService.agentnowmoneycollect());
				
				//累计提现金额
				Withdraw withdraw = new Withdraw();
				withdraw.setUserId(merid);
				Map<String, Object> extractTotal = CommUtil.isMapEmpty(withdrawService.selectmoneysum(withdraw));//extractmoney:提现金额  sumservicecharge:手续费
				Double extractmoney = CommUtil.toDouble(extractTotal.get("extractmoney"));//提现金额
				Double sumservicecharge = CommUtil.toDouble(extractTotal.get("sumservicecharge"));//手续费金额
				
				Map<String, Object> pendingTotal = withdrawService.pendingmoney(merid, "0,4", null, null);//查询待提现金额
				Double penextractmoney = CommUtil.toDouble(pendingTotal.get("extractmoney"));
				Double penservicecharge = CommUtil.toDouble(pendingTotal.get("sumservicecharge"));
				
				Map<String, Object> datainfo = new HashMap<String, Object>();
				datainfo = agentdata;
				datainfo.put("extractmoney", extractmoney);
				datainfo.put("sumservicecharge", sumservicecharge);
				datainfo.put("penextractmoney", penextractmoney);
				datainfo.put("penservicecharge", penservicecharge);
				datainfo.put("earningsMoney", earningsmoney);
				resultdata.put("totalinfo", agentdata);
				resultdata.put("totaltodayinfo", agentnowdata);
				resultdata.put("totaldatainfo", datainfo);
				resultdata.put("useradmin", user);
				}
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", resultdata);
			return resultdata;
		}
	}
	
	/**
	 * separate
	 * @Description： 数据汇总信息查询
	 * @author： origin   2019年11月1日 下午5:32:14
	 */
	@RequestMapping(value="/dataCollectInquire")
	@ResponseBody
    public Object dataCollectInquire(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = statisticsService.dataCollectInquire(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 历史统计信息
	 * @author： origin   2019年11月1日 下午5:32:14
	 */
	@RequestMapping(value="/historyStatisticsData")
	@ResponseBody
    public Object historyStatisticsData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = statisticsService.historyStatisticsData(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 商户收益汇总信息
	 * @author： origin   2019年11月1日 下午5:32:14
	 */
	@RequestMapping(value="/dealerEarningsData")
	@ResponseBody
    public Object dealerEarningsData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = statisticsService.earningsData(request, 2);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 商户收益下载
	 * @author： origin   2019年11月1日 下午5:32:14
	 */
	@RequestMapping(value="/dealerEarningsDownload")
	@ResponseBody
    public Object dealerEarningsDownload(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = statisticsService.dealerEarningsDownload(request);
		}
		return result;
	}
	
	/**
	 * separate
	 * @Description： 设备收益汇总信息
	 * @author： origin   2019年11月1日 下午5:32:14
	 */
	@RequestMapping(value="/deviceEarningsData")
	@ResponseBody
    public Object deviceEarningsData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = statisticsService.earningsData(request, 1);
		}
		return result;
	}
	
	/**
	 * 获取位置信息
	 * @return String
	 * @author zz
	 */
	@RequestMapping("/locationData")
	@ResponseBody
	public String getLocationData(){
		return equipmentService.getLocationData();
	}
	
}
