package com.hedong.hedongwx.web.controller.hduser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.Areastatistics;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.Codestatistics;
import com.hedong.hedongwx.entity.DealerAuthority;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.InCoins;
import com.hedong.hedongwx.entity.MerAmount;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.Money;
import com.hedong.hedongwx.entity.OfflineCard;
import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.OnlineCardRecord;
import com.hedong.hedongwx.entity.PackageMonth;
import com.hedong.hedongwx.entity.PackageMonthRecord;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.Privilege;
import com.hedong.hedongwx.entity.Realchargerecord;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserBankcard;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.entity.Withdraw;
import com.hedong.hedongwx.service.AllPortStatusService;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.AuthorityService;
import com.hedong.hedongwx.service.BasicsService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.CollectStatisticsService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.FeescaleService;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.service.MoneyService;
import com.hedong.hedongwx.service.OfflineCardService;
import com.hedong.hedongwx.service.OnlineCardRecordService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.StatisticsService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserBankcardService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.service.WithdrawService;
import com.hedong.hedongwx.utils.ByteUtils;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.JsSignUtil;
import com.hedong.hedongwx.utils.MobileSendUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.SendMsgUtil;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.TempMsgUtil;
import com.hedong.hedongwx.utils.WeChatWithdrawUtils;
import com.hedong.hedongwx.utils.WeiXinConfigParam;
import com.hedong.hedongwx.utils.WeixinUtil;
import com.hedong.hedongwx.utils.XMLUtil;
import com.hedong.hedongwx.web.controller.WxPayController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping({ "/merchant" })
public class MerchantController {
	
	public static Map<String, Long> withdrawMap = new HashMap<>();

	private final Logger logger = LoggerFactory.getLogger(MerchantController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private UserBankcardService userBankcardService;
	@Autowired
	private WithdrawService withdrawService;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private AllPortStatusService allPortStatusService;
	@Autowired
	private InCoinsService inCoinsService;
	@Autowired
	private OfflineCardService offlineCardService;
	@Autowired
	private MerchantDetailService merchantDetailService;
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private OnlineCardService onlineCardService;
	@Autowired
	private OnlineCardRecordService onlineCardRecordService;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private FeescaleService feescaleService;
	@Autowired
	private BasicsService basicsService;
	
	/**
	 * @method_name: verifyMerchIsUser
	 * @Description: 检查指定商户是否绑定有设备或者是合伙人
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月21日 下午3:15:26
	 * @common:
	 */
	@RequestMapping(value = "/verifyMerchIsUser")
	@ResponseBody
	public Object verifyMerchIsUser(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			Integer merid =  CommUtil.toInteger(maparam.get("merid"));
			Integer rank =  CommUtil.toInteger(maparam.get("rank"));
			Map<String, Object> bindingdevice = userEquipmentService.selectustoequ(merid);
			Integer totalline =  CommUtil.toInteger(bindingdevice.get("totalline"));
			if(rank==1){
				if(totalline>0) return CommUtil.responseBuildInfo(3001, "该商户绑定有设备，不能操作！", datamap);
			}
			Map<String, Object> areaPartner = areaService.inquireAreaService(merid);
			Integer size =  CommUtil.toInteger(areaPartner.get("size"));
			if(size>0) return CommUtil.responseBuildInfo(3002, "该商户存在合伙设备，不能操作！", datamap);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	/**
	 * @method_name: editAccountType
	 * @Description: 编辑更改用户的类型
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月21日 下午3:19:38
	 * @common:
	 */
	@RequestMapping(value = "/editAccountType")
	@ResponseBody
	public Object editAccountType(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			Integer id =  CommUtil.toInteger(maparam.get("id"));
			Integer rank =  CommUtil.toInteger(maparam.get("rank"));
			User user = new User();
			user.setId(id);
			user.setLevel(rank);
			userService.updateUserById(user);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	
	/**
	 * 模板预览
	 */
	@RequestMapping(value = "/tempPreview")
	public String tempPreview(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			if(user==null) return "erroruser";
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return "erroruser";
			}
			String code = CommUtil.toString(maparam.get("code"));
			if(code==null) return "erroruser";
			String paratem = CommUtil.toString(maparam.get("paratem"));
			JSONObject jsonMap = JSONObject.fromObject(paratem);
			Integer tempid = CommUtil.toInteger(jsonMap.get("id"));
			String temname = CommUtil.toString(jsonMap.get("temName"));
			String brandname = CommUtil.toString(jsonMap.get("loName"));
			String servephone = CommUtil.toString(jsonMap.get("tel"));
			Integer walletpay = CommUtil.toInteger(jsonMap.get("ref"));
			Integer permit = CommUtil.toInteger(jsonMap.get("temporaryc"));
			Integer ifalipay = CommUtil.toInteger(jsonMap.get("ifalipay"));
			ifalipay = ifalipay == 0 ? 1 : ifalipay;
			String chargeinfo = CommUtil.toStringTrim(jsonMap.get("chargeInfo"));
			TemplateParent temppare = new TemplateParent();
			temppare.setId(tempid);
			temppare.setName(temname);
			temppare.setRemark(brandname);
			temppare.setCommon1(servephone);
			temppare.setWalletpay(walletpay);
			temppare.setPermit(permit);
			temppare.setIfalipay(ifalipay);
			temppare.setChargeInfo(chargeinfo);
			
			String tempowerlist = CommUtil.toString(jsonMap.get("mid1"));
			String temtimelist = CommUtil.toString(jsonMap.get("mid2"));
			String temmoneylist = CommUtil.toString(jsonMap.get("mid3"));
			JSONArray tempower = JSONArray.fromObject(tempowerlist);
			JSONArray temtime = JSONArray.fromObject(temtimelist);
			JSONArray temmoney = JSONArray.fromObject(temmoneylist);
			
			List<TemplateSon> listtempower = new ArrayList<>();
			List<TemplateSon> listtemtime =  new ArrayList<>();
			List<TemplateSon> listtemmoney =  new ArrayList<>();
			for (int i = 0; i < tempower.size(); i++) {
				JSONObject item = tempower.getJSONObject(i);
				Integer tempsonid = CommUtil.toInteger(item.get("id"));
				Double everymoney = CommUtil.toDouble(item.get("everymoney"));
				String powerstart = CommUtil.toString(item.get("powerstart"));
				String powerend = CommUtil.toString(item.get("powerend"));
				TemplateSon tempson = new TemplateSon();
				tempson.setId(tempsonid);
				tempson.setMoney(everymoney);
				tempson.setCommon1(powerstart);
				tempson.setCommon2(powerend);
				tempson.setChargeQuantity(0);
				listtempower.add(tempson);
			}
			for (int i = 0; i < temtime.size(); i++) {
				JSONObject item = temtime.getJSONObject(i);
				Integer tempsonid = CommUtil.toInteger(item.get("id"));
				String showtitle = CommUtil.toString(item.get("showtitle"));
				Integer chargetime = CommUtil.toInteger(item.get("chargetime"));
				TemplateSon tempson = new TemplateSon();
				tempson.setId(tempsonid);
				tempson.setName(showtitle);
				tempson.setChargeTime(chargetime);
				tempson.setChargeQuantity(0);
				listtemtime.add(tempson);
			}
			for (int i = 0; i < temmoney.size(); i++) {
				JSONObject item = temmoney.getJSONObject(i);
				Integer tempsonid = CommUtil.toInteger(item.get("id"));
				String showmoney = CommUtil.toString(item.get("showmoney"));
				Double money = CommUtil.toDouble(item.get("money"));
				Integer chargetime = 0;
				Integer quantity = 0;
				TemplateSon tempson = new TemplateSon();
				tempson.setId(tempsonid);
				tempson.setName(showmoney);
				tempson.setMoney(money);
				tempson.setChargeQuantity(0);
				chargetime = CommUtil.toInteger(money * 60);
				tempson.setChargeTime(chargetime);
				quantity = CommUtil.toInteger(money * 100);
				tempson.setChargeQuantity(quantity);
				listtemmoney.add(tempson);
			}
			Map<String, Object> devicedata = equipmentService.selectEquipAreaInfo(code);
			String version = CommUtil.toString(devicedata.get("hardversion"));
			List<TemplateSon> tempson = new ArrayList<>();
			tempson.addAll(listtempower);
			tempson.addAll(listtemtime);
			tempson.addAll(listtemmoney);
			Integer temporaryc = CommUtil.toInteger(temppare.getPermit());
			model.addAttribute("temporaryc", temporaryc);
			model.addAttribute("tempower", listtempower);
			model.addAttribute("temtime", listtemtime);
			model.addAttribute("temmoney", listtemmoney);
			model.addAttribute("tempson", tempson);
			model.addAttribute("deviceType", version);
			model.addAttribute("code", code);
			model.addAttribute("devicedata", devicedata);
			model.addAttribute("temppare", temppare);
			return "merchant/previewChargeTem";
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
			return "erroruser";
		}
	}
	
	/**
	 * @Description：v3充电提示信息
	 * @author： origin 
	 */
	@RequestMapping(value="/timetemdata")
    public Object timetemdata(HttpServletRequest request, HttpServletResponse response, Model model) {
		Integer tempid = CommUtil.toInteger(request.getParameter("tempid"));
		if(tempid>0){
//			TemplateParent temppare = templateService.getParentTemplateOne(tempid);
//			temppare = temppare == null ? new TemplateParent() : temppare;
			TemplateParent directtemp = templateService.inquireDirectTemp(tempid);
			List<TemplateSon> tempson = templateService.getSonTemplateLists(tempid);
			tempson = tempson == null ? new ArrayList<>() : tempson;
			Map<String, Object> mapinfo = new HashMap<>();
			List<TemplateSon> tempower = new ArrayList<>();
			List<TemplateSon> temtime =  new ArrayList<>();
			List<TemplateSon> temmoney =  new ArrayList<>();
			for(TemplateSon item : tempson){
				Integer type = CommUtil.toInteger(item.getType());
				if(type.equals(1)){
					tempower.add(item);
				}else if(type.equals(2)){
					temtime.add(item);
				}else if(type.equals(3)){
					temmoney.add(item);
				} 
			}
//			mapinfo.put("temppare", directtemp);
			mapinfo.put("tempower", tempower);
			mapinfo.put("temtime", temtime);
			mapinfo.put("temmoney", temmoney);
			directtemp.setGather(mapinfo);
			model.addAttribute("mapinfo", mapinfo);
			model.addAttribute("templatelist", directtemp);
			model.addAttribute("directtemp", directtemp);
			model.addAttribute("tempson", tempson);
		}else{
			model.addAttribute("templatelist", null);
			model.addAttribute("tempson", null);
		}
		String devicenum =  CommUtil.toString(request.getParameter("code"));
		model.addAttribute("code", devicenum);
		return "/merchant/timeTemplate";
	}
	
	/**
	 * @Description：设备端口二维码
	 * @param request
	 * @param response
	 * @return
	 * @author： origin
	 * @createTime：2020年3月25日上午9:57:30
	 * @comment:
	 */
	@RequestMapping(value="/devicePortQRCode")
    public Object devicePortQRCode(String code, Model model) {
		try {
			String devicenum = CommUtil.toString(code);
			Equipment devicedata = equipmentService.getEquipmentById(devicenum);
			String version = CommUtil.toString(devicedata.getHardversion());
			model.addAttribute("version", version);
			model.addAttribute("code", devicenum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/equipment/devicePortQRCode";
	}
	
	/**
	 * @Description： 查询收益统计、默认为十五天数据
	 * @author： origin 
	 */
	@RequestMapping("/earningcollecttime")
	public Object earningcollect( Model model) {
		User user = (User) this.request.getSession().getAttribute("user"); 
		DealerAuthority jurisdiction = authorityService.selectMessSwitch(user.getId());
		Integer showincoins = CommUtil.toInteger(jurisdiction.getShowincoins());
		String begintime = StringUtil.getPastDate(15) + " 00:00:00";
		String endtime = StringUtil.getPastDate(0) + " 23:59:59";
		List<Map<String, Object>> result = statisticsService.earningcollectinfo( user, begintime, endtime, 3, null, showincoins);
		
		Parameters paramet = new Parameters();
		paramet.setUid(user.getId());
		paramet.setType("2");
		paramet.setLevel("3");
		paramet.setStartTime(begintime);
		paramet.setEndTime(endtime);
		List<Map<String, Object>> codestmap = statisticsService.getStatistics(paramet);
		
		for(Map<String, Object> item : codestmap){
			Double moneytotal = CommUtil.toDouble(item.get("moneytotal"));
//			Double wechatmoney = CommUtil.toDouble(codestmap.get("wechatmoney"));
//			Double alipaymoney = CommUtil.toDouble(codestmap.get("alipaymoney"));
			Double wechatretmoney = CommUtil.toDouble(item.get("wechatretmoney"));
			Double alipayretmoney = CommUtil.toDouble(item.get("alipayretmoney"));
			Double paymentmoney = CommUtil.toDouble(item.get("paymentmoney"));
			Double returnmoney = StringUtil.addBig(wechatretmoney, alipayretmoney);
			moneytotal = StringUtil.subBig(moneytotal, returnmoney);
			moneytotal = StringUtil.subBig(moneytotal, paymentmoney);
			item.put("moneytotal", moneytotal);
//--------------------------------------------------------
			Double incoinsmoney = CommUtil.toDouble(item.get("incoinsmoney"));
			Double oncardmoney = CommUtil.toDouble(item.get("oncardmoney"));
			Double windowpulsemoney = CommUtil.toDouble(item.get("windowpulsemoney"));
			Double coinsmoney = CommUtil.addBig(windowpulsemoney, incoinsmoney);
//--------------------------------------------------------
			item.put("totalcoinsearn", coinsmoney);
			if(showincoins.equals(2) || showincoins.equals(0)){
				item.put("totalmoney",  moneytotal);
			}else{
				Double totalmoney = StringUtil.addBig(moneytotal, coinsmoney);
				totalmoney = StringUtil.addBig(totalmoney, oncardmoney);
				item.put("totalmoney",  totalmoney);
			}
			item.put("consumeQuantity",CommUtil.toDouble(item.get("consumequantity"))/100);
			item.put("paymentmoney", paymentmoney);
		}
		model.addAttribute("showincoins", showincoins);
		model.addAttribute("resultotal", result);
		model.addAttribute("result", codestmap);
		model.addAttribute("user", user);
		model.addAttribute("begintime", begintime);
		model.addAttribute("endtime", endtime);
		return "merchant/earningcollectinfo";
	}
	
	@RequestMapping("/earningcollectajax")
	@ResponseBody
	public Object earningcollectinfo(String begintime, String endtime, Integer type, String param) {
		User user = (User) this.request.getSession().getAttribute("user");
		DealerAuthority jurisdiction = authorityService.selectMessSwitch(user.getId());
		Integer showincoins = CommUtil.toInteger(jurisdiction.getShowincoins());
		if(begintime!=null) begintime = CommUtil.trimToEmpty(begintime).replace("/", "-") + " 00:00:00";
		if(endtime!=null) endtime = CommUtil.trimToEmpty(endtime).replace("/", "-") + " 23:59:59";
		List<Map<String, Object>> result = statisticsService.earningcollectinfo( user, begintime, endtime, type, param, showincoins);
		Map<String, Object> map = new HashMap<String, Object>();
		if(type==3){
			map.put("data", result);
			map.put("type", type);
			Parameters paramet = new Parameters();
			paramet.setUid(user.getId());
			paramet.setType("2");
			paramet.setLevel("3");
			paramet.setStartTime(begintime);
			paramet.setEndTime(endtime);
			List<Map<String, Object>> codestmap = statisticsService.getStatistics(paramet);
			//发送Ajax请求计算每天消耗的电量
			List<Map<String, Object>> consumebytime=chargeRecordService.selectConsumeByTime(user.getId(), begintime, endtime);
			
			for(Map<String, Object> item : codestmap){
				Double moneytotal = CommUtil.toDouble(item.get("moneytotal"));
				Double wechatretmoney = CommUtil.toDouble(item.get("wechatretmoney"));
				Double alipayretmoney = CommUtil.toDouble(item.get("alipayretmoney"));
				Double paymentmoney = CommUtil.toDouble(item.get("paymentmoney"));
				
				Double returnmoney = StringUtil.addBig(wechatretmoney, alipayretmoney);
				moneytotal = StringUtil.subBig(moneytotal, returnmoney);
				moneytotal = StringUtil.subBig(moneytotal, paymentmoney);
				
				Double incoinsmoney = CommUtil.toDouble(item.get("incoinsmoney"));
				Double oncardmoney = CommUtil.toDouble(item.get("oncardmoney"));
				Double windowpulsemoney = CommUtil.toDouble(item.get("windowpulsemoney"));
				Double coinsmoney = CommUtil.addBig(windowpulsemoney, incoinsmoney);
				
				item.put("moneytotal", moneytotal);
				item.put("totalcoinsearn", coinsmoney);
				item.put("totaloncardmoney", coinsmoney);
				
				if(showincoins.equals(2) || showincoins.equals(0)){
					item.put("totalmoney",  moneytotal);
				}else{
					Double totalmoney = StringUtil.addBig(moneytotal, coinsmoney);
					totalmoney = StringUtil.addBig(totalmoney, oncardmoney);
					item.put("totalmoney",  totalmoney);
				}
				item.put("consumeQuantity",CommUtil.toDouble(item.get("consumequantity"))/100);
				item.put("paymentmoney", paymentmoney);
//				//发送ajax查询一段时间内每天的总耗电
//				for (Map<String, Object> jtem : consumebytime) {
//					int xiangDeng=StringUtil.compareStringTime(item.get("count_time")+" 00:00:00",jtem.get("begintime")+" 00:00:00");
//					if(xiangDeng==0){
//						item.put("consumeQuantity",CommUtil.toDouble(jtem.get("consumeQuantity"))/100);
//					}
//				}
			}
			map.put("result", codestmap);
		}else{
			map.put("result", result);
			map.put("type", type);
		}
		return JSON.toJSONString(map);
	}
	/**
	 * 进入首页
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/index" })
	public String index(Model model) {
		User user = (User) this.request.getSession().getAttribute("user");
		if(user == null) return "erroruser";
		//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
		Integer merid = CommUtil.toInteger(user.getId());
		//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
		model.addAttribute("rank", user.getLevel());
		if (user.getLevel() == 1) {
			model.addAttribute("openiderror", "没有权限进行后续访问");
			model.addAttribute("name", null);
			return "openiderror";
		}
		if (user.getLevel() == 6) {
			List<Privilege> userPrivilege = userService.selectUserPrivilege(user.getId());
			System.out.println("privilege===" + JSON.toJSONString(userPrivilege));
			if (userPrivilege.size() > 0) {
				for (Privilege privilege : userPrivilege) {
					model.addAttribute(privilege.getExplain(), "1");
				}
				
			}
			user = userService.selectUserById(user.getMerid());
			merid = CommUtil.toInteger(user.getId());
		}
		DealerAuthority jurisdiction = authorityService.selectMessSwitch(user.getId());
		model.addAttribute("showincoins", CommUtil.toInteger(jurisdiction.getShowincoins()));
		model.addAttribute("merid", merid);
		model.addAttribute("homeclick", 1);
		System.out.println("首页数据查询完毕，等待加载页面");
		return "merchant/home";
	}
	
	
	
	@RequestMapping("/jssdkWxGet")
	@ResponseBody
	public Map<String, String> jssdkWxGet(String pageUrl) {
		System.out.println("pageUrl===" + pageUrl);
		try {
			Map<String, String> map = JsSignUtil.sign(pageUrl);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@RequestMapping("/queryMoneyByTime")
	@ResponseBody
	public Map<String,Object> queryMoneyByTime(String begintime, String endtime) {
		User user = (User) this.request.getSession().getAttribute("user");
		Map<String,Object> hashMap = new HashMap<>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String currenttime = sf.format(new Date());
		if (user != null) {
			if ((currenttime.compareTo(endtime) < 0) || (currenttime.compareTo(begintime) < 0)) {
				hashMap.put("status", "0");
				return hashMap;
			} else {
				user = userService.getUserByOpenid(user.getOpenid());
				request.getSession().setAttribute("user", user);
				hashMap.put("status", "1");
				Map<String, Object> agentnowdata = userService.agentnowmoneycollect(user,null,
						begintime + " 00:00:00",endtime + " 23:59:59");
				hashMap.put("moneytotal", agentnowdata.get("moneytotal"));
				return hashMap;
			}
		} else {
			hashMap.put("status", "0");
			return hashMap;
		}
	}
	
	/**
	 * 授权登陆判断
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/home" })
	public String home(Model model) throws Exception {
		try {
			String code = this.request.getParameter("code");
			String state = this.request.getParameter("state");
			// 利用code获取openid和access_token
			String str = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WeiXinConfigParam.APPID
					+ "&secret=" + WeiXinConfigParam.APPSECRET + "&code=" + code + "&grant_type=authorization_code";
			JSONObject doGetStr = WeixinUtil.doGetStr(str);
			if (!doGetStr.has("openid")) {
				logger.warn("商户登陆获取openid失败：" + doGetStr.toString());
				return "erroruser";
			}
			String openid = doGetStr.getString("openid");
			String accesstoken = doGetStr.getString("access_token");
			
			String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accesstoken + "&openid="
					+ openid + "&lang=zh_CN";
//			String getUserInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + WeixinUtil.getBasicAccessToken() + "&openid="
//					+ openid + "&lang=zh_CN";
			JSONObject doGetStr1 = WeixinUtil.doGetStr(getUserInfoUrl);
			
			if (openid != null) {
				User userByOpenid = this.userService.getUserByOpenid(openid);
				if (userByOpenid == null) {
					User user = new User();
					if (doGetStr1.has("unionid")) {
						user.setUnionid(doGetStr1.getString("unionid"));
						String nickname = doGetStr1.getString("nickname");
						String replaceEmoji = ByteUtils.replaceEmoji(nickname);
						user.setUsername(replaceEmoji);
						logger.info("username===" + nickname);
					}
					user.setOpenid(openid);
					user.setEarnings(Double.valueOf(0.00));
					if ("2".equals(state)) {
						user.setLevel(6);
					} else {
						user.setLevel(4);
					}
					user.setCreateTime(new Date());
					this.userService.addUser(user);
					this.request.getSession().setAttribute("user", user);
					System.out.println("此用户刚添加");
					model.addAttribute("openiderror", "公众号已转移");
					model.addAttribute("name", "'自助充电平台'");
					if ("2".equals(state)) {
						return "redirect:/merchant/skipRegister?param=" + state;
					} else {
						return "openiderror";
					}
				} else {
					if ("2".equals(state)) {
						if (userByOpenid.getPhoneNum() == null || userByOpenid.getPhoneNum() == ""
								|| userByOpenid.getPhoneNum().length() == 0) {
							this.request.getSession().setAttribute("user", userByOpenid);
							return "redirect:/merchant/skipRegister?param=" + state;
						}
						if (userByOpenid.getLevel() != 6) {
							model.addAttribute("openiderror", "没有权限进行后续访问");
							model.addAttribute("name", null);
							return "openiderror";
						} 
					} else if ("1".equals(state)) {
//						if (userByOpenid.getLevel() == null || userByOpenid.getLevel() != 0) {
//							model.addAttribute("openiderror", "公众号已转移");
//							model.addAttribute("name", "'自助充电平台'");
//							return "openiderror";
//						}
					}
					if (doGetStr1.has("unionid")) {
						String nickname = doGetStr1.getString("nickname");
						String unionid = doGetStr1.getString("unionid");
						if (!"".equals(unionid) && (userByOpenid.getUnionid() == null || "".equals(userByOpenid.getUnionid()))) {
							User user = new User();
							user.setId(userByOpenid.getId());
							user.setUnionid(unionid);
							userService.updateUserById(user);
						}
						if (nickname != null && !"".equals(nickname)) {
							if (userByOpenid.getUsername() == null || !nickname.equals(userByOpenid.getUsername())){
								User user = new User();
								user.setId(userByOpenid.getId());
								String replaceEmoji = ByteUtils.replaceEmoji(nickname);
								user.setUsername(replaceEmoji);
								userService.updateUserById(user);
							}
						}
					}
					
					if (userByOpenid.getPhoneNum() == null || userByOpenid.getPhoneNum() == ""
							|| userByOpenid.getPhoneNum().length() == 0) {
						this.request.getSession().setAttribute("user", userByOpenid);
						return "redirect:/merchant/skipRegister";
					} else {
						this.request.getSession().setAttribute("user", userByOpenid);
						return index(model);
//						return "redirect:/merchant/index";
					}
				}
			} else {
				return "redirect:/merchant/index";
			}
		} catch (Exception e) {
			logger.warn("用户登陆异常===" + e.getMessage() + "===报错位置："+ ByteUtils.getLineNumber(e));
			return "erroruser";
		}

	}
	
	@RequestMapping({"/addoppenid"})
	public String addoppenid(Model model) throws Exception {
		String code = request.getParameter("code");
		JSONObject JsonCode = WeixinUtil.WeChatToken(code);
		if (!JsonCode.has("openid")) {
			model.addAttribute("openiderror", "您的账户暂时无法访问");
			return "openiderror";
		}
		JSONObject JsonInfor = WeixinUtil.getuserinfo( JsonCode.getString("access_token"),  JsonCode.getString("openid"));
		User user = new User();
		if (JsonInfor.has("nickname")) {
			String replaceEmoji = ByteUtils.replaceEmoji(JsonInfor.getString("nickname"));
			user.setUsername(replaceEmoji);
		}
		user.setOpenid(JsonInfor.getString("openid"));
		user.setEarnings(Double.valueOf(0.00));
		user.setLevel(2);
		user.setCreateTime(new Date());
		this.userService.addUser(user);
		this.request.getSession().setAttribute("user", user);
		return "redirect:/merchant/skipRegister?param=1&existuser=1";
	}
	
	@RequestMapping({ "/infoverdict" })
	public String infoverdict() throws Exception {
		String existuser = request.getParameter("existuser");
		if (existuser != null && "1".equals(existuser)) {
			return "redirect:/merchant/skipRegister?param=1&code=" + request.getParameter("code") + "&existuser=" + existuser;
		}
		/*String code = this.request.getParameter("wxpaycode");
		JSONObject JsonCode = WeixinUtil.WeChatToken(code);
		if (!JsonCode.has("openid")) {
			System.out.println("code错误");
			return "erroruser";
		}*/
		
//		String openid = JsonCode.getString("openid");
		String openid = request.getParameter("openid");
		User userByOpenid = this.userService.getUserByOpenid(openid);
		if (userByOpenid == null) {//不存在该用户 
			return WeixinUtil.loginWeChats(CommonConfig.ZIZHUCHARGE+"/merchant/addoppenid");
		} else {//存在该公户openid信息
			if (userByOpenid.getPhoneNum() == null || userByOpenid.getPhoneNum() == ""
					|| userByOpenid.getPhoneNum().length() == 0) {//不存在手机号
				this.request.getSession().setAttribute("user", userByOpenid);
				return "redirect:/merchant/skipRegister?param=1&existuser=1&code=" + request.getParameter("code");
			} else {//存在手机号
				this.request.getSession().setAttribute("user", userByOpenid);
				return "redirect:/merchant/manage";
			}
		}
	}
	
	
	@RequestMapping({ "/skipRegister" })
	public String skipRegister( String param, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && user.getId()!=null) {
			verifyTemplate(0);
			verifyTemplate(1);
			verifyTemplate(2);
			verifyTemplate(3);
			verifyTemplate(4);
			verifyTemplate(5);
		}else{
			return "erroruser";
		}
		if(param==null) param = "0";
		model.addAttribute("paramstate", param);
		String code = request.getParameter("code");
		model.addAttribute("code", code);
		String existuser = request.getParameter("existuser");
		model.addAttribute("existuser", existuser);
		return "merchant/register";
	}
	
	/**
	 * 判断个人模板是否存在，存在跳过，不存在添加
	 */
	public void verifyTemplate(Integer type) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			if(user!=null && user.getId() != null){
				if(type==0){//充电
					int fale = templateService.verifyTemplate(user.getId(), 0);
					if (fale==0) templateService.insertDufaultTemplate(user.getId());
				}else if(type==1){//离线模板
					int fale = templateService.verifyTemplate(user.getId(), 1);
					if (fale==0) templateService.insertOfflineTemp(user.getId());
				}else if(type==2){//脉冲（投币）
					int fale = templateService.verifyTemplate(user.getId(), 2);
					if (fale==0)  templateService.insertInCoinsTemp(user.getId());
				}else if(type==3){//钱包
					int fale = templateService.verifyTemplate(user.getId(), 3);
					if (fale==0)  templateService.insertWalletTemp(user.getId());
				}else if(type==4){//在线卡
					int fale = templateService.verifyTemplate(user.getId(), 4);
					if (fale==0)  templateService.insertOnlineTemp(user.getId());
				}else if(type==5){//在线卡
					int fale = templateService.verifyTemplate(user.getId(), 5);
					if (fale==0)  templateService.insertMonthTemp(user.getId());
				}
			}
		} catch (Exception e) {
			logger.info(CommUtil.getExceptInfo(e));
		}
	}
	
	/**
	 * 判断用户表中是否存在该用户
	 * @return
	 * @throws Exception 
	 */
	public boolean existMobile(String mobile) {
		User user =  userService.existAdmin(mobile);
		if(user==null) return true;
		return false;
	}
	
	/**
	 * @Description： 判断用户表中用户是否存在，并返回
	 * @author： origin 
	 */
	@RequestMapping({ "/checkAccount" })
	@ResponseBody
	public User checkAccount(String mobile){
		User user = null;
		if(null == mobile || mobile.equals("")) return user;
		user = userService.existAdmin(mobile);
		return user;
	}
	
	@RequestMapping({ "/existaccount" })
	@ResponseBody
	public Map<String, Object> existAccounts( String mobile, String invitecode, Integer type) {
		Map<String, Object> codeMap = new HashMap<String, Object>();
		User user =  userService.existAdmin(mobile);
		if(type==1){
			int length = invitecode.length();
			if (length == 6) {
				if(!invitecode.equals("888888")){
					Equipment equip = equipmentService.getEquipmentById(invitecode);
					Byte bindtype = equip.getBindtype();
					if(bindtype==null || bindtype == 1){
						codeMap.put("code", 202);
						return codeMap;
					}
				}
			} else if (length == 11) {
				User merUser = userService.existAdmin(invitecode);
				if (merUser != null) {
					if (merUser.getLevel() != 2 && merUser.getLevel() != 0) {
						codeMap.put("code", 202);
						return codeMap;
					}
				} else {
					codeMap.put("code", 202);
					return codeMap;
				}
			} else {
				codeMap.put("code", 202);
				return codeMap;
			}
		}
		if(user!=null){
			codeMap.put("code", 200);
			codeMap.put("user", user);
		}else{
			codeMap.put("code", 201);
		}
		return codeMap;
	}
	
	@RequestMapping({ "/captcha" })
	@ResponseBody
	public Map<String, Object> Captcha( String mobile) {
		Map<String, Object> code = new HashMap<String, Object>();
		String authcode = StringUtil.getRandNum();
		String[] params = { authcode, "3" };
		String[] mobilephon = { mobile };
		MobileSendUtil.TemplateMobileSend(params, mobilephon);
		code.put("authcode", authcode);
		return code;
	}
	
	@RequestMapping({ "/register" })
	@ResponseBody
	public Object register(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Object result = userService.registerUser(request);
		return result;
	}

	@RequestMapping(value = { "/querypointto" }, method = {org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public String querypointto(String begintime, String endtime) {
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String currenttime = sf.format(date);
		if ((currenttime.compareTo(endtime) < 0) || (currenttime.compareTo(begintime) < 0)) {
			return "0";
		}
		return "1";
	}

	@RequestMapping({ "/querybytime" })
	public String querybytime(String begintime, String endtime, Model model) {
		User user = (User) this.request.getSession().getAttribute("user");
		if (user != null) {
			User user2 = userService.getUserByOpenid(user.getOpenid());
			double todaymoney = 0.00;
			model.addAttribute("begintime", begintime);
			model.addAttribute("endtime", endtime);
			model.addAttribute("homeclick", 1);
			if (user2 != null) {
				begintime = begintime + " 00:00:00";
				endtime = endtime + " 24:00:00";
				System.out.println("查询时间：" + begintime + "---" + endtime);
				Integer merchantid = user2.getId();
				Double offlineTodayMoney = offlineCardService.getOfflineTodayMoneyByUserid(begintime,endtime,null,merchantid,null);
				Double inCoinsTodayMoney = inCoinsService.getInCoinsTodayMoneyByUserid(begintime,endtime,null,merchantid,null);
				Double todayMoney = chargeRecordService.getTodayMoneyByUserid(begintime,endtime,merchantid);
				System.out.println("offlineTodayMoney===" + offlineTodayMoney);
				System.out.println("inCoinsTodayMoney===" + inCoinsTodayMoney);
				System.out.println("todayMoney===" + todayMoney);
				offlineTodayMoney = offlineTodayMoney == null ? 0.0 : offlineTodayMoney;
				inCoinsTodayMoney = inCoinsTodayMoney == null ? 0.0 : inCoinsTodayMoney;
				todayMoney = todayMoney == null ? 0.0 : todayMoney;
				todaymoney = (offlineTodayMoney * 100 + inCoinsTodayMoney * 100 + todayMoney * 100) / 100;
				model.addAttribute("todaymoney", todaymoney);
				model.addAttribute("title", "查询收益");
			}
			return "merchant/home";
		}
		return "erroruser";
	}
	
	/**
	 * @Description： 
	 * @param: 
	 * @author： origin   2019年12月21日 上午11:36:57
	 */
	@RequestMapping({ "/appointTemdata" })
	public String appointTemdata( Integer tempid, String code, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		try {
			if(user==null) return "erroruser";
			Map<String, Object> devicedata = equipmentService.selectEquipAreaInfo(code);
			String version = CommUtil.toString(devicedata.get("hardversion"));
			TemplateParent temppare = templateService.getParentTemplateOne(CommUtil.toInteger(tempid));
			temppare = temppare == null ? new TemplateParent() : temppare;
			if(version.equals("08") || version.equals("09") || version.equals("10")){
				Integer temporaryc = CommUtil.toInteger(temppare.getPermit());
				model.addAttribute("temporaryc", temporaryc);
				List<TemplateSon> tempson = templateService.getSonTemplateLists(tempid);
				tempson = tempson == null ? new ArrayList<>() : tempson;
				List<TemplateSon> tempower = new ArrayList<>();
				List<TemplateSon> temtime =  new ArrayList<>();
				List<TemplateSon> temmoney =  new ArrayList<>();
				for(TemplateSon item : tempson){
					Integer type = CommUtil.toInteger(item.getType());
					if(type.equals(1)){
						tempower.add(item);
					}else if(type.equals(2)){
						temtime.add(item);
					}else if(type.equals(3)){
						temmoney.add(item);
					} 
				}
				model.addAttribute("tempower", tempower);
				model.addAttribute("temtime", temtime);
				model.addAttribute("temmoney", temmoney);
				model.addAttribute("tempson", tempson);
			}else{
				List<TemplateSon> tempson = (List<TemplateSon>) temppare.getGather();
				model.addAttribute("tempson", tempson);
			}
			model.addAttribute("deviceType", version);
			model.addAttribute("code", code);
			model.addAttribute("devicedata", devicedata);
			model.addAttribute("temppare", temppare);
			return "merchant/previewChargeTem";
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	/**
	 * @method_name: appointTeminfo
	 * @Description: 指定[任命]模板信息 [设备管理—管理项的收费模板显示]
	 * @param tempid：存在模板id是修改，需要同类型设备信息；不存在模板id，是添加模板，不需要同类型设备信息
	 * @param code：设备号
	 * @param model
	 * @return
	 * @Author: origin 2019年12月21日 上午11:40:45    （更改）  创建时间:2020年8月22日 上午11:08:02
	 * @common:
	 */
	@RequestMapping({ "/appointTeminfo" })
	public String appointTeminfo( Integer tempid, String code, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		try {
			if(user==null || code==null) return "erroruser";
			model.addAttribute("code", code);
			model.addAttribute("arecode", code);
			model.addAttribute("tempid", tempid);
			model.addAttribute("source", 1);
			model.addAttribute("user", user);
			
			Map<String, Object> devicedata = equipmentService.selectEquipAreaInfo(code);
			String versions = CommUtil.toString(devicedata.get("hardversion"));
			Integer merid = CommUtil.toInteger(devicedata.get("dealid"));
			List<TemplateParent> templatelist = new ArrayList<>();
			TemplateParent temppare = new TemplateParent();
			if(tempid!=null){
				List<Map<String, Object>> deviceresult = equipmentService.selectAssignTypeEqui(merid, versions);
				List<Map<String, Object>> result = new ArrayList<>();
				for(Map<String, Object> item : deviceresult){
					Integer codetempid = CommUtil.toInteger(item.get("tempid"));
					if(codetempid.equals(tempid)){
						item.put("pitchon", 1);
						result.add(item);
					}else{
						item.put("pitchon", 2);
					}
				}
				model.addAttribute("employresult", result);
				model.addAttribute("deviceresult", deviceresult);
				temppare = templateService.getParentTemplateOne(CommUtil.toInteger(tempid));
				temppare = temppare == null ? new TemplateParent() : temppare;
				Integer grade = CommUtil.toInteger(temppare.getGrade());
				if(grade.equals(1)){
					Integer dealid = CommUtil.toInteger(temppare.getMerchantid());
					templatelist = templateService.templateGradeInfo(dealid, 0, tempid);
				}
			}else{
				if(temppare.getName()==null){
					if("00".equals(versions) || "01".equals(versions)) model.addAttribute("tempname", "充电模板"+CommUtil.toString(System.currentTimeMillis()).substring(4));
				}
			}
			templatelist.add(temppare);
			model.addAttribute("templatelist", templatelist);
//			return "merchant/templatecharge";
			return "merchant/chargeTemplate";
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	/**
	 * @Description： 
	 * @param: 
	 * @author： origin   2019年12月21日 上午11:36:57
	 */
	@RequestMapping({ "/addchargeTem" })
	public String addchargeTem( Integer tempid, String code, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		try {
			List<TemplateParent> templatelist = new ArrayList<>();
			List<TemplateSon> tempson = null;
			if (user != null) {
				if(!CommUtil.toInteger(tempid).equals(0)){
					TemplateParent temppare = templateService.getParentTemplateOne(CommUtil.toInteger(tempid));
					temppare = temppare == null ? new TemplateParent() : temppare;
					tempson = (List<TemplateSon>) temppare.getGather();
					templatelist.add(temppare);
				}
//				Map<String, Object> devicedata = equipmentService.selectEquipAreaInfo(code);
//				List<TemplateSon> tempson = templateService.getSonTemplateLists(CommUtil.toInteger(tempid));
				model.addAttribute("code", code);
//				model.addAttribute("devicedata", devicedata);
				model.addAttribute("templatelist", templatelist);
				model.addAttribute("tempson", tempson);
				return "merchant/addchargeTem";
			}else{
				return "erroruser";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	/**
	 * @method_name: getDeviceData  
	 * @Description: 根据设备号获取商户名下同类型所有设备信息[点击实现模板复用时弹出设备信息、与模板添加时多选设备时使用]
	 * @param code：设备号   tempid：模板    model
	 * @return
	 * @Author: origin  创建时间:2020年8月22日 下午3:35:25
	 * @common:
	 */
	@RequestMapping({ "/getDeviceData" })
	@ResponseBody
//	public Object getDeviceData( String code, Integer tempid, Model model) {
	public Object getDeviceData(HttpServletRequest request) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()) return CommUtil.responseBuildInfo(1001, "参数传递不正确或为空", resultdata);
			Integer type =  CommUtil.toInteger(maparam.get("type"));
			Integer tempid =  CommUtil.toInteger(maparam.get("tempid"));
			String code =  CommUtil.toString(maparam.get("code"));
			Map<String, Object> devicedata = equipmentService.selectEquipAreaInfo(code);
			String version = CommUtil.toString(devicedata.get("hardversion"));
			Integer dealid = CommUtil.toInteger(devicedata.get("dealid"));
			List<Map<String, Object>> deviceresult = equipmentService.selectAssignTypeEqui(dealid, version);
			List<Map<String, Object>> result = new ArrayList<>();
			for(Map<String, Object> item : deviceresult){
				Integer codetempid = CommUtil.toInteger(item.get("tempid"));
				if(codetempid.equals(tempid)){
					item.put("pitchon", 1);
				}else{
					result.add(item);
					item.put("pitchon", 2);
				}
			}
			if(type==1){
				resultdata.put("noresult", result);
				return CommUtil.responseBuildInfo(200, "SUCCEED", resultdata);
			}
			return deviceresult;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(3001, "异常错误", resultdata);
		}
	}
	
	/**
	 * @method_name: updateDeviceTemplate
	 * @Description:  更改【多个】设备模板
	 * @param deviceList:设备号所属的list集合    tempid:模板id
	 * @return
	 * @Author: origin  创建时间:2020年8月22日 下午4:06:31
	 * @common:
	 */
	@RequestMapping(value="/updateDeviceTemplate")
	@ResponseBody
    public Object updateDeviceTemplate(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if(user!=null){
			Object result = equipmentService.updateDeviceTemplate(request);
			datamap.put("result", result);
			return CommUtil.responseBuildInfo(200, "SUCCEED", datamap);
		}else{
			return CommUtil.responseBuildInfo(901, "session缓存失效", datamap);
		}
	}
	
	/**
	 * @method_name: removeDeviceTemplate
	 * @Description: 删除设备模板信息【删除后默认使用系统模板】
	 * @param code：设备号
	 * @return
	 * @Author: origin  创建时间:2020年8月22日 下午4:50:43
	 * @common:
	 */
	@RequestMapping(value="/removeDeviceTemplate")
	@ResponseBody
    public Object removeDeviceTemplate(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()) return CommUtil.responseBuildInfo(102, "参数传递不正确或为空", resultdata);
			String code =  CommUtil.toString(maparam.get("code"));
			Map<String, Object> devicedata = equipmentService.selectEquipAreaInfo(code);
			String version = CommUtil.toString(devicedata.get("hardversion"));
			Integer dealid = CommUtil.toInteger(devicedata.get("dealid"));
			Integer temid = CommUtil.toInteger(devicedata.get("tempid"));
			Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( null, dealid, code, version);
			Integer tempid =  CommUtil.toInteger(DirectTemp.get("id"));
			int num = equipmentService.updateTempidByEquipmentCode(code, tempid);
			CommUtil.responseBuildInfo(200, "成功", resultdata);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(3001, "异常错误", resultdata);
		}
		return resultdata;
	}
	
	/**
	 * @Description： 通过设备号给设备选择模板(设备管理中模板选择)
	 * @author： origin          
	 * 创建时间：   2019年6月5日 下午5:13:48
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping({ "/devicetemdata" })
	public String devicetemdata(String code, Model model) {
		try {
			String url = "merchant/devicetempcharge";
			model.addAttribute("code", code);
			User user = (User) request.getSession().getAttribute("user");
			if(user==null) return "erroruser";
			Equipment equipment = equipmentService.getEquipmentById(code);
			String version = CommUtil.toString(equipment.getHardversion());
			model.addAttribute("arecode", code);
			model.addAttribute("source", 1);
			model.addAttribute("user", user);
			Integer tempid = CommUtil.toInteger(equipment.getTempid());
			Integer dealid = CommUtil.toInteger(user.getId());
			if (user.getLevel() == 6) {
				dealid = CommUtil.toInteger(user.getMerid());
			}
			Integer status = 0;
			if ("04".equals(version)) {
				status = 1;
				url = "merchant/templateoffline";
			} else if ("03".equals(version)) {
				status = 2;
				url = "merchant/templateincoins";
			} else if ("08".equals(version)  || "09".equals(version) || "10".equals(version)) {
				status = 6;
				model.addAttribute("deviceType", version);
				url = "merchant/devicetempcharge";
			} else {
				status = 0;
				model.addAttribute("deviceType", version);
				url = "merchant/devicetempcharge";
			}
			Map<String, Object> systemTempData = templateService.inquireSystemTempData(status, version);
			Integer tempidsys = CommUtil.toInteger(systemTempData.get("id"));
			if(tempid == 0 || tempid.equals(tempidsys)){
				systemTempData.put("pitchon", 1);
			}
			List<Map<String, Object>> tempListData = new ArrayList<>();
			List<TemplateParent> templatelist = templateService.inquireTempByStatus(dealid, status);//名下所有模板
			Map<String, Object> tempemploy = null;
			for (int i = templatelist.size() - 1; i >= 0; i--) {
				TemplateParent templates = templatelist.get(i);
				Integer templaid = CommUtil.toInteger(templates.getId());
				if (tempid.equals(templaid)) {
					templates.setPitchon(1);
					tempemploy = JSON.parseObject(JSON.toJSONString(templates), Map.class);
				}else{
					tempListData.add(JSON.parseObject(JSON.toJSONString(templates), Map.class));
				}
			}
			List<Map<String, Object>> list = new ArrayList<>();
			list.add(systemTempData);
			if(tempemploy!=null) list.add(tempemploy);
			list.addAll(tempListData);
			if("03".equals(version) || "04".equals(version)){
				for(Map<String, Object> item : list){
					Integer templataid = CommUtil.toInteger(item.get("id"));
					List<TemplateSon> tempsonlist = templateService.getSonTemplateLists(templataid);
					item.put("gather", tempsonlist);
				}
			}
			model.addAttribute("templatelist", list);
			return url;
			
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	/**
	 * @Description： 通过设备号给设备选择模板(设备管理中模板选择)
	 * @author： origin          
	 * 创建时间：   2019年6月5日 下午5:13:48
	 */
	@RequestMapping({ "/equiptemp" })
	public String equipTemp(String code, Model model) {
		String url = "";
		model.addAttribute("code", code);
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			Equipment equipment = this.equipmentService.getEquipmentById(code);
			if (equipment != null) {
				model.addAttribute("arecode", code);
				model.addAttribute("source", 1);
				model.addAttribute("user", user);
				List<TemplateParent> templatelist = null;
				if ("04".equals(equipment.getHardversion())) {
					templatelist = templateService.getParentTemplateListByMerchantidwolf(user.getId(), 1);
					url = "merchant/templateoffline";
				} else if ("03".equals(equipment.getHardversion())) {
					templatelist = templateService.getParentTemplateListByMerchantidwolf(user.getId(), 2);
					url = "merchant/templateincoins";
				} else {
					templatelist = templateService.getParentTemplateListByMerchantidwolf(user.getId(), 0);
					url = "merchant/templatecharge";
				}
				List<TemplateParent> templatelist1 = new ArrayList<>();
				List<TemplateParent> tempgather = new ArrayList<TemplateParent>();
				if (templatelist != null && templatelist.size() > 0) {
					if (equipment.getTempid() != null) {
						for (int i = templatelist.size() - 1; i >= 0; i--) {
							TemplateParent templateParent = templatelist.get(i);
							if (equipment.getTempid().equals(templateParent.getId())) {
								templateParent.setPitchon(1);
								templatelist1.add(templateParent);
								templatelist.remove(templateParent);
							}
							if(CommUtil.toInteger(templateParent.getGrade())==1){
								tempgather = templateService.getTempDetails(templateParent);
							}
				        }
						templatelist1.addAll(templatelist);
						model.addAttribute("templatelist", templatelist1);
						model.addAttribute("tempgather", tempgather);
					} else {
						model.addAttribute("templatelist", templatelist);
					}
				}
			}
			return url;
		} else {
			return "erroruser";
		}
	}
	
	
	/**
	 * 通过设备号给设备选择充电模板 （@unused暂时不用[origin]）
	 * @param code
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/tempall" })
	public String tempall(String code, Model model) {
		model.addAttribute("code", code);
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			Equipment equipment = this.equipmentService.getEquipmentById(code);
			if (equipment != null) {
				if ("04".equals(equipment.getHardversion())) {
					List<TemplateParent> templatelist = templateService.getParentTemforOffLine(user.getId(), 1);
					model.addAttribute("templatelist", templatelist);
				} else if ("03".equals(equipment.getHardversion())) {
					List<TemplateParent> templatelist = templateService.getParentTemforOffLine(user.getId(), 2);
					model.addAttribute("templatelist", templatelist);
				} else {
					List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantid(user.getId(),0);
					model.addAttribute("templatelist", templatelist);
				}
				Integer tempid = equipment.getTempid();
				if (tempid != null) {
					model.addAttribute("tempid", tempid);
				}
				return "merchant/choosetemp";
			} else {
				return "erroruser";
			}
		} else {
			return "erroruser";
		}
	}

	/**
	 * 通过查询条件获取订单列表
	 * @param code
	 * @param begintime
	 * @param endtime
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/getorderlistbycondition" })
	@ResponseBody
	public String getorderlistbycondition(String code, String begintime, String endtime, Model model) {
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String currenttime = sf.format(date);
		if ((currenttime.compareTo(endtime) < 0) || (currenttime.compareTo(begintime) < 0)) {
			return "0";
		}
		return "1";
	}

	@RequestMapping(value = { "/equbindtemp" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public String equbindtemp(@RequestParam("tempid") Integer tempid, @RequestParam("code") String code) {
		Equipment equipmentById = equipmentService.getEquipmentById(code);
		UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
		Integer merid = userEquipment == null ? 0 : userEquipment.getUserId();
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			equipmentService.insertCodeoperatelog(code, 3, 2,merid, user.getId(), equipmentById.getTempid() + "");
		} else {
			equipmentService.insertCodeoperatelog(code, 3, 2, merid, 0, equipmentById.getTempid() + "");
		}
		this.equipmentService.updateTempidByEquipmentCode(code, tempid);
		return "1";
	}

	@RequestMapping(value = "/remotecharge")
	public String remotecharge(Model model) {
		User user = (User) this.request.getSession().getAttribute("user");
		if (user != null) {
			List<UserEquipment> userEquipmentList = this.userEquipmentService.getUserEquipmentById(user.getId());
			List<Equipment> equipmentList = new ArrayList<>();
			for (UserEquipment userEquipment : userEquipmentList) {
				Equipment equipment = this.equipmentService.getEquipmentById(userEquipment.getEquipmentCode());
				if (equipment != null) {
					Integer tempid = equipment.getTempid();
					if (tempid != null) {
						TemplateParent templateOne = this.templateService.getParentTemplateOne(tempid);
						if (templateOne != null) {
							equipment.setTempname(templateOne.getName());
						}
					}
					equipmentList.add(equipment);
				}
			}
			model.addAttribute("equipmentList", equipmentList);
		}
		return "merchant/remotecharge";
	}

	@RequestMapping({ "/charge" })
	public String charge(String code, Model model) {
		try {
			model.addAttribute("code", code);
			Equipment equipment = equipmentService.getEquipmentById(code);
			//		int neednum = EquipmentController.getAllportNeednum(equipment.getHardversion());
			//		List<AllPortStatus> allPortStatusList = allPortStatusService.findPortStatusListByEquipmentnum(code, neednum);
			String version = CommUtil.toString(equipment.getHardversion());
			List<Map<String, String>> allPortStatusList = DisposeUtil.addPortStatus(code, version);
			model.addAttribute("allPortStatusList", allPortStatusList);
			if (allPortStatusList != null && allPortStatusList.size() > 0) {
				model.addAttribute("updateTime", allPortStatusList.get(0).get("updateTime"));
			}
			return "merchant/charge";
		} catch (Exception e) {
			return "erroruser";
		}
	}

	@RequestMapping(value = "/remotechargechoose")
	public String remote(String code, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			Equipment equipment = equipmentService.getEquipmentById(code);
			String hardversion = equipment.getHardversion();
			model.addAttribute("code", code);
			model.addAttribute("hardversion", hardversion);
			if ("03".equals(hardversion)) {
				model.addAttribute("remoteparam", 1);
				return "testpay";
			} else if ("04".equals(hardversion)) {
				List<TemplateSon> templateLists = templateService.getEquSonTem(code);
				model.addAttribute("templateLists", templateLists);
//				SendMsgUtil.send_0x22(code, 0, (short) 0, (byte) 2);
				model.addAttribute("nowtime", System.currentTimeMillis());
				model.addAttribute("openid", user.getOpenid());
				return "merchant/offlineCard";
			} else {
				List<TemplateSon> templatelist = templateService.getEquSonTem(code);
				model.addAttribute("templatelist", templatelist);
//				SendMsgUtil.send_15(code);
				return "merchant/remotechargechoose";
			}
		} else {
			return "erroruser";
		}
	}

	@RequestMapping(value = "/remotechargeaccess", method = RequestMethod.POST)
	@ResponseBody
	public String remotechargeaccess(String code, Integer chargeparam, Byte portchoose) {
		if (chargeparam == null || portchoose == null) {
			return "0";
		} else {
			TemplateSon templateSon = templateService.getInfoTemplateOne(chargeparam);
			if (templateSon != null) {
				Double money = templateSon.getMoney();
				money = money * 100;
				String chargeTime = templateSon.getChargeTime() + "";
				String chargeQuantity = templateSon.getChargeQuantity() + "";
				Short time = Short.valueOf(chargeTime);
				Short elec = Short.valueOf(chargeQuantity);
				Equipment equipment = equipmentService.getEquipmentById(code);
				if ("07".equals(equipment.getHardversion())) {
//					SendMsgUtil.send_0x27((byte)portchoose, (short)(money / 10), (short)time, (short)elec, code, (byte)1);
				} else {
//					SendMsgUtil.send_0x14(portchoose, (short) (money / 10), time, elec, code);// 支付完成充电开始
				}
//				SendMsgUtil.send_0x14(portchoose, (short) (money / 10), time, elec, code);
//				WolfHttpRequest.sendChargePaydata(portchoose, time, (short) (money / 10) + "", chargeQuantity, code);
			}
			return "1";
			// return "redirect:/merchant/remotecharge";
		}
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
	
	@RequestMapping({ "/tradorderdetail" })
	public String tradorderdetail(Integer orderid, String order, Integer source, Model model){
		///** 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、用户充值钱包-5、用户在线卡充值*/
		Integer refundnum = null;
		if (source == 1) {
			refundnum = 1;
			model.addAttribute("order", chargeRecordService.getOrderByOrdernum(order).get(0));
		} else if (source == 2) {
			refundnum = 3;
			model.addAttribute("order", inCoinsService.selectInCoinsRecordByOrdernum(order));
		} else if (source == 3) {
			refundnum = 2;
			model.addAttribute("order", offlineCardService.selectOfflineCardByOrdernum(order));
		} else if (source == 4) {
			refundnum = 4;
			model.addAttribute("order", moneyService.queryMoneyByOrdernum(order));
		} else if (source == 5) {
			refundnum = 5;
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
			model.addAttribute("order", onlineCardRecordService.selectRecordByOrdernum(order, type, null));
		} else if (source == 6) {
			PackageMonthRecord monthRecord = userService.selectMonthRecordByOrdernum(order, 1);
			model.addAttribute("order", monthRecord);
			refundnum = 6;
		} else {
			model.addAttribute("order", null);
		}
		TradeRecord tra = new TradeRecord();
		tra.setId(orderid);
		//model.addAttribute("traorder", tradeRecordService.getTraderecordList(tra));
		model.addAttribute("refundnum", refundnum);
		model.addAttribute("source", source);
		return "merchant/tradorderdetail";
	}
	
	@RequestMapping({ "/orderlist" })
	public String orderlist(Model model) {
		User user = (User) this.request.getSession().getAttribute("user");
		Double totalMoney = 0.0;
		if (user != null) {
			String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String begintime = format + " 00:00:00";
			String endtime = format + " 24:00:00";
			Double inCoinsTotalMoney = inCoinsService.getInCoinsTodayMoneyByUserid(begintime, endtime, null, user.getId(), null);
			model.addAttribute("inCoinsRecords", inCoinsService.getInCoinsByParam(begintime, endtime, null, user.getId(), null));
			Double offlineTotalMoney = offlineCardService.getOfflineTodayMoneyByUserid(begintime, endtime, null, user.getId(), null);
			model.addAttribute("offlineRecords", offlineCardService.getOfflineByParma(begintime, endtime, null, user.getId(), null));
			Double chargeTotalMoney = chargeRecordService.getTotalMoneyByTimeAndEquipmentnumMerchantid(begintime,
					endtime, null,user.getId(),null);
			model.addAttribute("chargeRecords", chargeRecordService.getChargeRecordListByTimeAndEquipmentnumMerchantid(begintime,
					endtime, null,user.getId(),null));
			offlineTotalMoney = offlineTotalMoney == null ? 0.0 : offlineTotalMoney;
			inCoinsTotalMoney = inCoinsTotalMoney == null ? 0.0 : inCoinsTotalMoney;
			chargeTotalMoney = chargeTotalMoney == null ? 0.0 : chargeTotalMoney;
			totalMoney = (offlineTotalMoney * 100 + inCoinsTotalMoney * 100 + chargeTotalMoney * 100) / 100;
		}
		model.addAttribute("totalMoney", totalMoney);
		model.addAttribute("hardversion", 1);
		return "merchant/orderlist";
	}

	@RequestMapping({ "/orderlistbycondition" })
	public String orderlistbycondition(String code, String ordernum, String begintime, String endtime, Integer hardversion, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		model.addAttribute("code", code);
		model.addAttribute("ordernum", ordernum);
		model.addAttribute("begintime", begintime);
		model.addAttribute("endtime", endtime);
		Double totalMoney = 0.0;
		if (user != null) {
			user = userService.selectUserById(user.getId());
			begintime += " 00:00:00";
			endtime += " 24:00:00";
			Double inCoinsTotalMoney = inCoinsService.getInCoinsTodayMoneyByUserid(begintime, endtime, code, user.getId(), ordernum);
			model.addAttribute("inCoinsRecords", inCoinsService.getInCoinsByParam(begintime, endtime, code, user.getId(), ordernum));
			Double offlineTotalMoney = offlineCardService.getOfflineTodayMoneyByUserid(begintime, endtime, code, user.getId(), ordernum);
			model.addAttribute("offlineRecords", offlineCardService.getOfflineByParma(begintime, endtime, code, user.getId(), ordernum));
			Double chargeTotalMoney = chargeRecordService.getTotalMoneyByTimeAndEquipmentnumMerchantid(begintime,
					endtime, code,user.getId(),ordernum);
			model.addAttribute("chargeRecords", chargeRecordService.getChargeRecordListByTimeAndEquipmentnumMerchantid(begintime,
					endtime, code,user.getId(),ordernum));
			offlineTotalMoney = offlineTotalMoney == null ? 0.0 : offlineTotalMoney;
			inCoinsTotalMoney = inCoinsTotalMoney == null ? 0.0 : inCoinsTotalMoney;
			chargeTotalMoney = chargeTotalMoney == null ? 0.0 : chargeTotalMoney;
			totalMoney = (offlineTotalMoney * 100 + inCoinsTotalMoney * 100 + chargeTotalMoney * 100) / 100;
			model.addAttribute("totalMoney", totalMoney);
		}
		return "merchant/orderlist";
	}

	@RequestMapping({ "/manage" })
	public String manage(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			MerAmount merAmount = userService.selectMerAmountByMerid(user.getId());
			if (merAmount == null) {
				try {
					userService.insertMerAmount(user.getId(), 0.0, 0.0, 0, 0);
				} catch (Exception e) {
					logger.warn("商户总计表添加失败===" + e.getMessage());
				}
			}
		} else {
			return "erroruser";
		}
		try {
			Map<String, String> map = JsSignUtil.sign(CommonConfig.ZIZHUCHARGE+"/merchant/manage");

			model.addAttribute("nonceStr", map.get("nonceStr"));
			model.addAttribute("timestamp", map.get("timestamp"));
			model.addAttribute("signature", map.get("signature"));
			model.addAttribute("jsapi_ticket", map.get("jsapi_ticket"));
			model.addAttribute("appId", map.get("appId"));
			model.addAttribute("url", map.get("url"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("manageclick", 1);
		return "merchant/manage";
	}
	
	/**
	 * @Description：微信端修改用户信息
	 * @author： origin：2020年3月3日下午5:05:28
	 */
	@RequestMapping(value="/redactAccountInfo")
	@ResponseBody
    public Object redactAccountInfo(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) this.request.getSession().getAttribute("user");
		if (user == null) {
			return "erroruser";
		}
		Object result = userService.redactAccountInfo(request);
		return result;
	}
	
	/**
	 * @Description： 商户个人中心加载
	 * @author： origin   2019年9月28日 下午2:30:15
	 */
	@RequestMapping({ "/personcenter" })
	public String personcenter(Model model) {
		User user = (User) this.request.getSession().getAttribute("user");
		if (user == null) {
			return "erroruser";
		}
		user = userService.selectUserById(user.getId());
		DealerAuthority result = authorityService.selectMessSwitch(user.getId());
		model.addAttribute("username", user.getUsername());
		model.addAttribute("user", user);
		model.addAttribute("messdata", result);
		List<User> childUserList = userService.selectUserByRankMid(6, user.getId());
		model.addAttribute("childUserList", childUserList);
		return "merchant/personcenter";
	}
	
	/**
	 * @Description： 点击查询钱包信息
	 * @author： origin   2019年9月28日 下午2:29:22
	 */
	@RequestMapping({ "/mywallet" })
	public String mywallet(Model model) {
		User user = (User) this.request.getSession().getAttribute("user");
		user = userService.selectUserById(user.getId());
		DealerAuthority result = authorityService.selectMessSwitch(user.getId());
		System.out.println("*****"+result);
		model.addAttribute("user", user);
		model.addAttribute("messdata", result);
		return "merchant/mywallet";
	}

	@RequestMapping({ "/eqipmentall" })
	public String eqipmentall() {
		return "equipment/equipmentList";
	}

	@RequestMapping({ "/orderdetail" })
	public String orderdetail(Integer orderid, Integer hardversion, Model model) {
		if (hardversion == 2) {
			model.addAttribute("chargeRecord", offlineCardService.selectOfflineCardById(orderid));
		} else if (hardversion == 3) {
			model.addAttribute("chargeRecord", inCoinsService.selectInCoinsRecordById(orderid));
		} else {
			model.addAttribute("chargeRecord", chargeRecordService.getChargeRecordById(orderid));
		}
		model.addAttribute("hardversion", hardversion);
		return "merchant/orderdetail";
	}

	@RequestMapping({ "/addeqipment" })
	public String addeqipment() {
		return "merchant/index";
	}
	
	/**
	 * 微信提现
	 * @param model
	 * @return
	 */
	@RequestMapping("/weChatWithdraw")
	public String weChatWithdraw(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		user = userService.selectUserById(user.getId());
		model.addAttribute("user", user);
		model.addAttribute("withdrawWay", 1);
		model.addAttribute("rate", user.getFeerate());
		return "merchant/withdraw";
	}
	
	/**
	 * 商家微信提现成功
	 * @param money 钱
	 * @param realname 真实姓名
	 * @param model
	 * @return {@link String}
	 */
	@RequestMapping("/weChatWithdrawaccess")
	@ResponseBody
	public String weChatWithdrawaccess(Double money, String realname, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		// 用户不能为空
		if (user != null) {
			// 根据用户的id查询用户
			user = userService.selectUserById(user.getId());
			long nowtime = System.currentTimeMillis();
			if (DisposeUtil.checkMapIfHasValue(withdrawMap)) {
				Long updateTime = withdrawMap.get("uid" + user.getId());
				if (updateTime == null || updateTime == 0) {
					withdrawMap.put("uid" + user.getId(), nowtime);
				} else {
					long calcTime = nowtime - updateTime;
					if (calcTime < 1500) {
						return "5";
					} else {
						withdrawMap.put("uid" + user.getId(), nowtime);
					}
				}
			} else {
				withdrawMap.put("uid" + user.getId(), nowtime);
			}
			// 获取当前时间
			Date date = new Date();
			// 时间格式化
			String format2 = new SimpleDateFormat("yyyy-MM-dd").format(date);
			// 用户提现的钱小于自己的收入
			if (money > user.getEarnings()) {
				return "0";
			}
			// 通过时间和用户id查询提现总额
			Double todayMoney = withdrawService.selectAllMoneyByUidAndTime(user.getId(), format2 + " 00:00:00", format2 + "23:59:59");
			todayMoney = todayMoney == null ? 0.0 : todayMoney;
			Double withMoney = todayMoney + money;
			if (withMoney > 10000) {
				return "4";
			}
			String format = "425" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + user.getId();
			HashMap<String,String> hashMap = new HashMap<>();
			// 商家的openid
			hashMap.put("openid", user.getOpenid());
			// 商家的订单号
			hashMap.put("ordernum", format);
			if (realname == null || "".equals(realname)) {
				hashMap.put("realname", user.getRealname());
			} else {
				hashMap.put("realname", realname);
			}
			// 商家的费率
			Integer feerate = user.getFeerate();
			// 四舍五入
			Double calcServMoney = (money * feerate + 4 + 0.000)/10;
			Double round = Math.round(calcServMoney) + 0.0;
			Double calcMoney = money * 100 - round.intValue();
			// 提现的钱
			hashMap.put("money", calcMoney.intValue() + "");
			// 提现的信息
			hashMap.put("info", format);
			// IP地址
			hashMap.put("ip", WxPayController.getIpAddress(request));
			try {
				// 提现的请求
				String result = WeChatWithdrawUtils.withdrawRequestOnce(hashMap, 3000, 3000, true);
				// 提现的信息
				Map<String, String> resultMap = XMLUtil.doXMLParse(result);
				if (resultMap.containsKey("result_code") && "SUCCESS".equals(resultMap.getOrDefault("result_code", ""))) {
					logger.info("用户" + user.getRealname() + "---微信零钱提现成功---");
					Withdraw withdraw = new Withdraw();
					withdraw.setWithdrawnum(format);
					withdraw.setBankcardnum("0");
					withdraw.setBankname("微信零钱");
					withdraw.setMoney(money);
					withdraw.setStatus(3);
					withdraw.setUserId(user.getId());
					// 费率为空或0
					if (feerate == null || feerate == 0) {
						// 计算手续费
						withdraw.setServicecharge((money * 6 + 4 + 0.000)/1000);
					} else {
						// 根据费率计算手续费
						withdraw.setServicecharge((money * feerate + 4 + 0.000)/1000);
					}
					withdraw.setUserMoney(user.getEarnings());
					Double userMoney = user.getEarnings();
					// 用户的钱减去提现的钱
					userMoney = userMoney * 100 - money * 100;
					// 修改用户提现后的金额
					userService.updateUserEarnings(0, money, user.getId());
					Date date2 = new Date();
					withdraw.setCreateTime(date2);
					withdrawService.addWithdraw(withdraw);
					// 添加用户资金变动明细
					merchantDetailService.insertMerEarningDetail(user.getId(), money, userMoney / 100, withdraw.getWithdrawnum(), new Date(), MerchantDetail.WITHDRAWSOURCE, 0, MerchantDetail.WITHDRAWACCESS);
					String url = TempMsgUtil.TEMP_MSG_URL.replace("ACCESS_TOKEN", WeixinUtil.getBasicAccessToken());
					Double money1 = withdraw.getMoney();
					Double servicecharge = withdraw.getServicecharge();
					double round1 = Math.round((money1*100 - servicecharge*100)) + 0.0;
					JSONObject jsonbj = TempMsgUtil.information2(user.getOpenid(), TempMsgUtil.TEMP_ID2, "http://www.he360.cn/checkOrderDetail?ordernum=" + withdraw.getWithdrawnum(), 
							"提现已到账", "微信零钱", round1/100 + "", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), "如有疑问，请尽快联系客服");
//					logger.info("输出jsonbj：   "+jsonbj);
					// 发送提现信息
					TempMsgUtil.PostSendMsg(jsonbj, url);
					if (realname != null && !"".equals(realname)) {
						User user2 = new User();
						user2.setId(user.getId());
						user2.setRealname(realname);
						// 修改用户的真实姓名
						userService.updateUserById(user2);
					}
					return "1";
				} else {
					logger.info("用户" + user.getRealname() + "---微信零钱提现失败---");
					String url = TempMsgUtil.TEMP_MSG_URL.replace("ACCESS_TOKEN", WeixinUtil.getBasicAccessToken());
					JSONObject jsonbj = TempMsgUtil.information2(user.getOpenid(), TempMsgUtil.TEMP_ID2, CommonConfig.ZIZHUCHARGE+"/oauth2login", 
							"提现到账失败", "微信零钱", calcMoney/100 + "", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), "如有疑问，请尽快联系客服");
//					logger.info("输出jsonbj：   "+jsonbj);
					TempMsgUtil.PostSendMsg(jsonbj, url);
					return resultMap.get("err_code_des");
				}
			} catch (Exception e) {
				logger.warn(e.getMessage());
				String url;
				try {
					url = TempMsgUtil.TEMP_MSG_URL.replace("ACCESS_TOKEN", WeixinUtil.getBasicAccessToken());
					JSONObject jsonbj = TempMsgUtil.information2(user.getOpenid(), TempMsgUtil.TEMP_ID2, CommonConfig.ZIZHUCHARGE+"/oauth2login", 
							"提现到账失败", "微信零钱", calcMoney/100 + "", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), "如有疑问，请尽快联系客服");
//					logger.info("输出jsonbj：   "+jsonbj);
					logger.info("用户" + user.getRealname() + "---微信零钱提现失败---");
					TempMsgUtil.PostSendMsg(jsonbj, url);
				} catch (Exception e1) {
					logger.warn(e1.getMessage());
				}
				return "2";
			}
		} else {
			return "2";
		}
	}
	
	/**
	 * 商家开启自动提现前设置真实姓名
	 * @param merId 商家的id
	 * @param realName 商家的真实姓名
	 * @return {@link Object}
	 */
	@RequestMapping("/setMername")
	@ResponseBody
	public Object autoWithDrawSetMername(Integer merId, String realName){
		if(merId == null || realName == null || "".equals(realName)){
			return CommUtil.responseBuild(400, "参数错误", "");
		}
		try {
			User user2 = new User();
			user2.setId(merId);
			user2.setRealname(realName);
			// 修改用户的真实姓名
			userService.updateUserById(user2);
			return CommUtil.responseBuild(200, "添加成功", "");
		} catch (Exception e) {
			return CommUtil.responseBuild(400, "参数错误", "");
		}
	}

	@RequestMapping({ "/withdraw" })
	public String withdraw(Integer type,Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			user = userService.selectUserById(user.getId());
			model.addAttribute("user", user);
			if (type == 1) {
				List<UserBankcard> userBankcardlist = userBankcardService.selectUserBankcardByUserid(user.getId(),type);
				if (userBankcardlist == null || userBankcardlist.size() == 0) {
					model.addAttribute("userBankcardlist", null);
				} else {
					Withdraw withdraw = withdrawService.getEndRecordByUserId(user.getId());
					List<UserBankcard> userbankcardlist = new ArrayList<>();
					for (UserBankcard userBankcard : userBankcardlist) {
						String bankcardnum = userBankcard.getBankcardnum();
						if (withdraw != null) {
							if (withdraw.getBankcardnum().equals(bankcardnum)) {
								model.addAttribute("userBankcardId", userBankcard.getId());
							}
						}
						bankcardnum = "**** " + bankcardnum.substring(bankcardnum.length() - 4,bankcardnum.length());
						userBankcard.setBankcardnum(bankcardnum);
						userbankcardlist.add(userBankcard);
						model.addAttribute("rate", user.getFeerate());
					}
					model.addAttribute("userbankcardlist", userbankcardlist);
				}
				return "merchant/withdraw";
			} else {
				List<UserBankcard> userBankcardlist = userBankcardService.selectUserBankcardByUserid(user.getId(),type);
				if (userBankcardlist != null && userBankcardlist.size() == 1) {
					model.addAttribute("bankcard", userBankcardlist.get(0));
				} else {
					model.addAttribute("bankcard", null);
				}
			}
			return "merchant/withdrawCompany";
		} else {
			return "erroruser";
		}
	}

	
	/**
	 * 提现到个人银行卡
	 * @param bankcardid 银行卡的编号
	 * @param money 钱
	 * @param model
	 * @return {@link String}
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/withdrawaccess", method = RequestMethod.POST)
	@ResponseBody
	public String withdrawaccess(Integer bankcardid, double money, Model model) throws Exception {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			long nowtime = System.currentTimeMillis();
			if (DisposeUtil.checkMapIfHasValue(withdrawMap)) {
				Long updateTime = withdrawMap.get("uid" + user.getId());
				if (updateTime == null || updateTime == 0) {
					withdrawMap.put("uid" + user.getId(), nowtime);
				} else {
					long calcTime = nowtime - updateTime;
					if (calcTime < 1500) {
						return "5";
					} else {
						withdrawMap.put("uid" + user.getId(), nowtime);
					}
				}
			} else {
				withdrawMap.put("uid" + user.getId(), nowtime);
			}
			// 查询商家信息
			user = userService.selectUserById(user.getId());
			// 参数的判断
			if (bankcardid == null || bankcardid == 0 || money == 0 || money < 5 || (user.getEarnings() - money) < 0) {
				return "0";
			} else {
				// 根据银行卡编号查询
				UserBankcard userBankcard = userBankcardService.selectUserBankcardByid(bankcardid);
				Withdraw withdraw = new Withdraw();
				String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + user.getId();
				// 订单
				withdraw.setWithdrawnum("425" + format);
				// 银行卡号
				String bankcardnum = userBankcard.getBankcardnum();
				// 设置参数
				withdraw.setBankcardnum(bankcardnum);
				withdraw.setBankname(userBankcard.getBankname());
				withdraw.setMoney(money);
				withdraw.setStatus(0);
				withdraw.setUserId(user.getId());
				// 获取商家的费率
				Integer feerate = user.getFeerate();
				// type:1-个人,2-对公
				// 商家提现的方式是个人直接到账
				if (userBankcard.getType() == 2) {
					feerate = userBankcard.getRate();
					withdraw.setStatus(4);
				}
				// 设置商家的费率
				if (feerate == null || feerate == 0) {
					withdraw.setServicecharge((money * 6 + 4 + 0.000)/1000);
				} else {
					withdraw.setServicecharge((money * feerate + 4 + 0.000)/1000);
				}
				// 设置提现钱的金额
				withdraw.setUserMoney(user.getEarnings());
				Double userMoney = user.getEarnings();
				// 提现前的总额度减去提现的钱
				userMoney = userMoney * 100 - money * 100;
				// 更新商家的收益(减掉提现的钱)
				userService.updateUserEarnings(0, money, user.getId());
				Date date2 = new Date();
				withdraw.setCreateTime(date2);
				// 添加提现的订单信息
				withdrawService.addWithdraw(withdraw);
				// 插入商家的收益明细
				merchantDetailService.insertMerEarningDetail(user.getId(), money, userMoney / 100, withdraw.getWithdrawnum(), new Date(), MerchantDetail.WITHDRAWSOURCE, 0, MerchantDetail.WITHDRAWACCESS);
				Map<String, Object> map = null;
				// 个人银行卡提现直接到账
				// userMoney >1000.00 && userMoney<5000.00
				if(userBankcard.getType() == 1 && userMoney >1000.00 && userMoney<10000.00){
					map = (Map<String, Object>) withdrawService.withdrawPersonBank(bankcardnum, withdraw.getWithdrawnum());
					logger.info("返回的状态信息==="+map);
					// 返回的状态码
					Integer code = CommUtil.toInteger(map.get("code"));				
					if(code != 0 && code == 200){
						return "1";
					}else{
						return "0";
					}
				}
				return "1";
			}
		} else {
			return "0";
		}
	}

	/** @Description： 商户提现记录查看 */
	@RequestMapping(value = "/withdrawrecord")
	public String withdrawrecord(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			List<Withdraw> withdrawList = withdrawService.getWithdrawListByUserId(user.getId());
			if (withdrawList == null || withdrawList.size() == 0) {
				model.addAttribute("withdrawList", null);
			} else {
				model.addAttribute("withdrawList", withdrawList);
			}
			return "merchant/withdrawrecord";
		} else {
			return "erroruser";
		}
	}

	@RequestMapping(value = "/mybankcard")
	public String mybankcard(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		String havCompany = "0";
		if (user != null) {
			List<UserBankcard> userBankcardlist = userBankcardService.selectUserBankcardByUserid(user.getId(),null);
			if (userBankcardlist == null || userBankcardlist.size() == 0) {
				model.addAttribute("userBankcardlist", null);
			} else {
				List<UserBankcard> userbankcardlist = new ArrayList<>();
				for (UserBankcard userBankcard : userBankcardlist) {
					String bankcardnum = userBankcard.getBankcardnum();
					bankcardnum = "**** " + bankcardnum.substring(bankcardnum.length() - 4,bankcardnum.length());
					userBankcard.setBankcardnum(bankcardnum);
					userbankcardlist.add(userBankcard);
					if (userBankcard.getType() == 2) {
						havCompany = "1";
					}
				}
				model.addAttribute("userbankcardlist", userbankcardlist);
			}
		}
		model.addAttribute("havCompany", havCompany);
		return "merchant/mybankcard";
	}
	
	@RequestMapping(value = "mybankcardinfo")
	public String mybankcardinfo(Integer id, Model model) {
		UserBankcard bankcard = userBankcardService.selectUserBankcardByid(id);
		model.addAttribute("bankcard", bankcard);
		return "merchant/mybankcardinfo";

	}
	@RequestMapping(value = "/unbindbankcard")
	@ResponseBody
	public String unbindbankcard(Integer id, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			userBankcardService.deleteUserBankcardByid(id);
			return "1";
		} else {
			return "0";
		}
	}
	
	@RequestMapping(value = "/editmybankcard")
	public String editmybankcard(Integer id, Model model) {
		UserBankcard bankcard = userBankcardService.selectUserBankcardByid(id);
		if (bankcard.getType() == 2) {
			List<Withdraw> withdrawList = withdrawService.getWithdrawListByBankcardnum(bankcard.getBankcardnum());
			if (withdrawList != null && withdrawList.size() > 0) {
				model.addAttribute("isedit", "1");
			} else {
				model.addAttribute("isedit", "0");
			}
		} else {
			model.addAttribute("isedit", "0");
		}
		model.addAttribute("bankcard", bankcard);
		return "merchant/editmybankcard";
	}
	
	@RequestMapping(value = "/editbankcardaccess", method = RequestMethod.POST)
	@ResponseBody
	public String editbankcardaccess(Integer id, String realname, String bankcardnum, 
			String bankname, String company, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			if (realname == null || bankcardnum == null || bankname == null) {
				return "0";
			} else {
				UserBankcard userBankcard = userBankcardService.selectUserBankcardByid(id);
				userBankcard.setRealname(realname);
				userBankcard.setBankcardnum(bankcardnum);
				userBankcard.setUserId(user.getId());
				userBankcard.setBankname(bankname);
				if (company != null && !"".equals(company)) {
					userBankcard.setCompany(company);
				}
				userBankcardService.updateUserBankcard(userBankcard);
				return "1";
			}
		} else {
			return "0";
		}
	}
	
	@RequestMapping(value = "/mybankcardinfo1")
	public String mybankcardinfo1(Model model) {
		return "merchant/mybankcardinfo";
	}

	@RequestMapping(value = "/addbankcard")
	public String addbankcard(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			return "merchant/addbankcard";
		} else {
			return "/erroruser";
		}
	}
	
	@RequestMapping(value = "/addCompanyCard")
	public String addCompanyCard(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			return "merchant/addCompanyCard";
		} else {
			return "/erroruser";
		}
	}

	@RequestMapping(value = "/addCompanyCardAccess", method = RequestMethod.POST)
	@ResponseBody
	public String addCompanyCardAccess(String name, String cardNum, String info, String company) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			List<UserBankcard> userbankcardlist = userBankcardService.selectUserBankcardByUserid(user.getId(),null);
			for (UserBankcard userBankcard : userbankcardlist) {
				if (cardNum.equals(userBankcard.getBankcardnum())) {
					return "2";
				}
			}
			UserBankcard userBankcard = new UserBankcard();
			userBankcard.setRealname(name);
			userBankcard.setBankcardnum(cardNum);
			userBankcard.setUserId(user.getId());
			userBankcard.setBankname(info);
			userBankcard.setType(2);
			userBankcard.setCompany(company);
			userBankcard.setRate(10);
			userBankcardService.addUserBankcard(userBankcard);
			return "1";
		} else {
			return "0";
		}
	}
	
	@RequestMapping(value = "/addbankcardaccess", method = RequestMethod.POST)
	@ResponseBody
	public String addbankcardaccess(String realname, String bankcardnum, String bankname, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			List<UserBankcard> userbankcardlist = userBankcardService.selectUserBankcardByUserid(user.getId(),null);
			for (UserBankcard userBankcard : userbankcardlist) {
				if (bankcardnum.equals(userBankcard.getBankcardnum())) {
					return "2";
				}
			}
			UserBankcard userBankcard = new UserBankcard();
			userBankcard.setRealname(realname);
			userBankcard.setBankcardnum(bankcardnum);
			userBankcard.setUserId(user.getId());
			userBankcard.setBankname(bankname);
			if (user.getRealname() == null) {
				userBankcardService.addUserBankcard(userBankcard);
				user.setRealname(userBankcard.getRealname());
				userService.updateUserById(user);
				return "1";
			} else {
				if (user.getRealname().equals(realname)) {
					userBankcardService.addUserBankcard(userBankcard);
					return "1";
				} else {
					return "3";
				}
			}
		} else {
			return "0";
		}
	}
	
	@RequestMapping(value = "/withcardinfoOauth")
	public String withcardinfoOauth(String ordernum,Model model) throws Exception {
		String code = this.request.getParameter("code");
		// 利用code获取openid和access_token
		if (code == null || "".equals(code)) {
			return "erroruser";
		}
		String str = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WeiXinConfigParam.APPID
				+ "&secret=" + WeiXinConfigParam.APPSECRET + "&code=" + code + "&grant_type=authorization_code";
		JSONObject doGetStr = WeixinUtil.doGetStr(str);
		if (!doGetStr.has("openid")) {
			return "erroruser";
		}
		String openid = doGetStr.getString("openid");
		User user = userService.getUserByOpenid(openid);
		//查询提取详情记录
		if (user == null) {
			return "erroruser";
		} else if (ordernum == null) {
			return "erroruser";
		} else if (withdrawService.selectWithdrawByOrdernum(ordernum) != null) {
			Withdraw withdraw = withdrawService.selectWithdrawByOrdernum(ordernum);
			String num =withdraw.getBankcardnum();
			if (!"0".equals(num) || num.length() > 4) {
				withdraw.setBankcardnum(num.substring(num.length()-4)); ;
			}
			model.addAttribute("withdraw", withdraw);
			return "merchant/withcardinfo";
		} else {
			return "erroruser";
		}
	}
	
	@RequestMapping(value = "/withcardinfo")
	public String withcardinfo(Integer id,Model model) {
		//查询提取详情记录
		if (id == null) {
			return "erroruser";
		} else if (withdrawService.selectExtractInfo(id) != null) {
			Withdraw withdraw = withdrawService.selectExtractInfo(id);
			String num =withdraw.getBankcardnum();
			if (!"0".equals(num) || num.length() > 4) {
				withdraw.setBankcardnum(num.substring(num.length()-4)); ;
			}
			model.addAttribute("withdraw", withdraw);
			return "merchant/withcardinfo";
		} else {
			return "erroruser";
		}
	}
	
	@RequestMapping("/merEarningsDetail")
	public String merEarningsDetail(Integer merid,Model model) {
		List<MerchantDetail> merEarnList = merchantDetailService.selectMerDetailListByMerid(merid);
		model.addAttribute("merEarnList", merEarnList);
		return "merchant/merEarningsDetail";
	}
	
	@RequestMapping("/merEarnDetailInfo")
	public String merEarnDetailInfo(Integer id, Model model) {
		MerchantDetail merchantDetail = merchantDetailService.selectMerEarnInfoById(id);
		model.addAttribute("merchantDetail", merchantDetail);
		Integer paysource = merchantDetail.getPaysource();
		String ordernum = merchantDetail.getOrdernum();
		model.addAttribute("paysource", paysource);
		if (paysource == 1) {
			List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
			model.addAttribute("order", chargeRecord.get(0));
		} else if (paysource == 2) {
			InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
			model.addAttribute("order", inCoins);
		} else if (paysource == 3) {
			OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
			model.addAttribute("order", offlineCard);
		} else if (paysource == 4) {
			Withdraw withdraw = withdrawService.selectWithdrawByOrdernum(ordernum);
			model.addAttribute("order", withdraw);
		} else if (paysource == 5) {
			Money money = moneyService.queryMoneyByOrdernum(ordernum);
			model.addAttribute("order", money);
		}
		return "merchant/merEarnDetailInfo";
	}
	
	
	
	/**
	 * @Description： 小区管理进入页面
	 * @author： origin 
	 */
	@RequestMapping("/areaManage")
	public String areaManage(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			Area area = new Area();
			area.setMerid(user.getId());
			List<Area> areaSelf =  areaService.selectByArea(area);
			model.addAttribute("areaSelf", areaSelf);
			 
			List<Area> areaPart =  areaService.selectPartAreaById(user.getId());
			model.addAttribute("areaPart", areaPart);
			List<Equipment> codelist = equipmentService.selectUnbindEqulist(user.getId());
			model.addAttribute("unbingnum", codelist.size());
			DealerAuthority jurisdiction = authorityService.selectMessSwitch(user.getId());
			model.addAttribute("showincoins", CommUtil.toInteger(jurisdiction.getShowincoins()));
			return "merchant/areainfo";
		} else {
			return "erroruser";
		}
	}
	
	/**
	 * @Description： 查询指定商户名下未绑定小区的设备信息
	 */
	@RequestMapping("/disRelEqu")
	public String disRelEqu(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		List<Equipment> disRelEqu = equipmentService.selectEquDisRelAid(user.getId());
		model.addAttribute("arealist", disRelEqu);
		return "merchant/areainfo";
	}
	
	/**
	 * 通过实体类 Area 传入参数添加 设备地点（小区）的信息
	 * @param request, response, model
	 * @return
	 */
	@RequestMapping(value = { "/insertArea" })
	@ResponseBody
	public Object insertArea(String name, String address, Model model){
		User user = (User) request.getSession().getAttribute("user");
		Object map = areaService.insetAreaInfo( user, name, address);
		return map;
		
	}
	
	/**
	 * @Description： 编辑修改小区信息
	 * @author： origin 创建时间：   2019年9月27日 下午3:47:08
	 */
	@RequestMapping(value = { "/editAreaInfo" })
	@ResponseBody
	public Object editAreaInfo(Integer aid, String name, String address){
		Object map = areaService.editAreaInfo( aid, name, address);
		return map;
	}
	
	/**
	 * @Description： 通过小区id 删除小区信息、并同步删除小区名下合伙人信息
	 * @author： origin 创建时间：   2019年9月27日 上午11:31:35
	 */
	@RequestMapping(value = { "/delArea" })
	@ResponseBody
	public Map<String, Object> deleteByArea(Integer aid, Model model){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			areaService.deleteAreaByAid(aid);
			User user = (User) request.getSession().getAttribute("user");
			Integer opeid = user == null ? 0 : user.getId();
			List<Equipment> equlistByAid = equipmentService.selectEqulistByAid(aid);
			if (equlistByAid != null && equlistByAid.size() > 0) {
				for (Equipment equipment : equlistByAid) {
					String code = equipment.getCode();
					equipmentService.insertCodeoperatelog(code, 4, 2, userEquipmentService.getUserEquipmentByCode(code).getUserId(), opeid, equipment.getAid() + "");
				}
			}
			equipmentService.updateEquAidByAid(aid);
			userService.unbindUserAndAid(aid);
			map.put("code", 200);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", 403);
		}
		return map;
	}
	
/*	@RequestMapping(value = { "/updateByArea" })
	@ResponseBody
	public Object updateByArea(Integer aid, String name, String address, String phone, Double divideinto, Model model){
		User user = (User) request.getSession().getAttribute("user");
		Object map = areaService.updateByArea( user, aid, name, address, phone, divideinto);
		return map;
	}
	*/
	/**
	 * 根据id查询单个area数据信息
	 * @param id, model
	 * @return
	 */
	@RequestMapping("/selectByIdArea")
	@ResponseBody
	public Area selectByIdArea(Integer id, Model model) {
		Area arealist =  areaService.selectByIdArea(id);
		return arealist;
	}
	
	/**
	 * @Description： 点击进入小区（显示的信息）
	 * @author： origin 创建时间：   2019年9月27日 上午11:39:35
	 */
	@RequestMapping("/areaBelongInfo")
	public String areaBelongInfo(Integer id,Model model) {
		Area area =  areaService.selectByIdArea(id);
		List<Equipment> equlistByAid = equipmentService.selectEqulistByAid(id);
		model.addAttribute("area", area);
		List<Equipment> codelist = equipmentService.selectUnbindEqulist(area.getMerid());
		model.addAttribute("codelist", codelist);
		model.addAttribute("equlist", equlistByAid);
		List<Map<String, Object>> result = areaService.selectAreaRelev(id);
		List<Object> listscanma = new ArrayList<>();
		List<Object> listscanpa = new ArrayList<>();
		for(Map<String, Object> item : result){
			Integer type = CommUtil.toInteger(item.get("type"));
			if(type==3){
				listscanma.add(item);
			}else if(type==2){
				listscanpa.add(item);
			}
		}
		User meruser = userService.selectUserById(area.getMerid());
		model.addAttribute("meruser", meruser); 
		model.addAttribute("listscanma", listscanma);
		model.addAttribute("listscanpa", listscanpa);
		return "merchant/areaBelongInfo";
	}
	
	/**
	 * @Description： 绑定小区合伙人(aid:小区id   type: 类型 1管理员  2合伙人  phone:添加对象的电话  percent:分成比)
	 * @author： origin 创建时间：   2019年9月27日 上午11:42:00
	 */
	@RequestMapping("/bindAreaPartner")
	@ResponseBody
	public Object bindAreaPartner(Integer aid, Integer type, String phone, Double percent) {
		Object result = areaService.bindAreaPartner(aid, type, phone, percent);
		return result;
	}
	
	/**
	 * @Description： 修改绑定小区合伙人的分成比(aid:小区id   percent:分成比)
	 * @author： origin 创建时间：   2019年9月27日 上午11:42:00
	 */
	@RequestMapping("/editBindAreaPartner")
	@ResponseBody
	public Object editBindAreaPartner(Integer id, Double percent) {
		Object result = areaService.editBindAreaPartner(id, percent);
		return result;
	}
	
	/**
	 * @Description： 绑定小区的管理人员(aid:小区id  phone:添加对象的电话  nickname:昵称 name:姓名 )
	 * @author： origin 创建时间：   2019年9月27日 上午11:42:00
	 */
	@RequestMapping("/bindAreaManage")
	@ResponseBody
	public Object bindAreaManage(Integer aid, String nickname, String phone, String name) {
		Object result = areaService.bindAreaPartner(aid, nickname, phone, name);
		return result;
	}
	
	/**
	 * @Description： 修改绑定小区的管理人员(aid:小区id   type: 类型 1管理员  2合伙人  phone:添加对象的电话  percent:分成比)
	 * @author： origin 创建时间：   2019年9月27日 上午11:42:00
	 */
	@RequestMapping("/editBindAreaManage")
	@ResponseBody
	public Object editBindAreaManage(Integer id, Integer aid, String phone) {
		Object result = areaService.editBindAreaPartner(id, aid, 3, phone);
		return result;
	}
	
	/**
	 * @Description：删除小区合伙人(id：小区合伙关联记录 表id【hd_arearelevance】)
	 * @author： origin 创建时间：   2019年9月27日 上午11:42:00
	 */
	@RequestMapping("/removeAreaPartner")
	@ResponseBody
	public Object removeAreaPartner(Integer id) {
		Object result = areaService.removeAreaPartner(id);
		return result;
	}
	
	@RequestMapping("/queryAreaTemp")
	@ResponseBody
	public Map<String, String> queryAreaTemp(Integer id,Model model) {
		Map<String, String> map = new HashMap<>();
		Area area =  areaService.selectByIdArea(id);
		Integer tempid = area.getTempid();
		Integer tempid2 = area.getTempid2();
		if (tempid != null) {
			TemplateParent parentTemplateOne = templateService.getParentTemplateOne(tempid);
			map.put("walletTempName", parentTemplateOne.getName());
		} else {
			map.put("walletTempName", null);
		}
		if (tempid2 != null) {
			TemplateParent parentTemplateOne = templateService.getParentTemplateOne(tempid2);
			map.put("onlineTempName", parentTemplateOne.getName());
		} else {
			map.put("onlineTempName", null);
		}
		return map;
	}
	
	@RequestMapping("/addEquArea")
	@ResponseBody
	public String addEquArea(String code,Integer aid) {
		Area area =  areaService.selectByIdArea(aid);
		if (area != null) {
			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
			if (userEquipment == null) {
				return "2";
			} else {
				User user = (User) request.getSession().getAttribute("user");
				Integer opeid = user == null ? 0 : user.getId();
				equipmentService.insertCodeoperatelog(code, 4, 1, userEquipment.getUserId(), opeid, equipmentService.getEquipmentById(code).getAid() + "");
				Equipment equipment = new Equipment();
				equipment.setCode(code);
				equipment.setAid(aid);
				equipmentService.updateEquipment(equipment);
				return "1";
			}
		} else {
			return "0";
		}
	}
	
	@RequestMapping("/delAreaCode")
	@ResponseBody
	public String delAreaCode(String code) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			Integer opeid = user == null ? 0 : user.getId();
			equipmentService.insertCodeoperatelog(code, 4, 2, userEquipmentService.getUserEquipmentByCode(code).getUserId(), opeid, equipmentService.getEquipmentById(code).getAid() + "");
			Equipment equipment = new Equipment();
			equipment.setCode(code);
			equipment.setAid(0);
			equipmentService.updateEquipment(equipment);
			return "1";
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return "0";
		}
	}
	
	@RequestMapping("/lookUnbindAreaEqu")
	public String lookUnbindEqu(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		List<Equipment> equlist = equipmentService.selectUnbindEqulist(user.getId());
		model.addAttribute("equlist", equlist);
		return "merchant/lookUnbindAreaEqu";
	}
	
	/**
	 * @Description： 进入在线卡界面
	 * @author： origin
	 * @createTime：2019年12月27日上午11:28:51
	 * @comment:
	 */
	@RequestMapping("/onlineCardList")
	public Object onlineCardList(HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (user != null) {
				Integer dealid = CommUtil.toInteger(user.getId());
				Map<String, Object> result =  onlineCardService.inquireOnlineCardData(request);
				//查询商户名下所有小区信息
				List<Map<String, Object>> areaData = CommUtil.isListMapEmpty(areaService.inquireDealAreaData(dealid));
				model.addAttribute("areaData", areaData);
				model.addAttribute("datasize", result.get("datasize"));
				model.addAttribute("datamoney", CommUtil.toDouble(result.get("datamoney")));
				model.addAttribute("datatopupmoney", CommUtil.toDouble(result.get("datatopupmoney")));
				model.addAttribute("datasendmoney", CommUtil.toDouble(result.get("datasendmoney")));
				model.addAttribute("resultdata", result.get("datalist"));
				model.addAttribute("totalRows", result.get("totalRows"));
				model.addAttribute("totalPages", result.get("totalPages"));
				model.addAttribute("currentPage", result.get("currentPage"));
				return "merchant/onlineCardList";
			} else {
				return "erroruser";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	/**
	 * @Description：上拉加载在线卡信息
	 * @author： origin
	 * @createTime：2019年12月27日上午11:28:11
	 */
	@RequestMapping("/inquireOnlineData")
	@ResponseBody
	public Object inquireOnlineData(HttpServletRequest request, HttpServletResponse response) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (user != null) {
				Map<String, Object> result =  onlineCardService.inquireOnlineCardData(request);
				return result;
			} else {
				return CommUtil.responseBuild(102, "信息获取失败", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuild(301, "异常错误", "");
		}
	}
	

	@RequestMapping("/inquireOnlineInfo")
	@ResponseBody
	public Object inquireOnlineInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (null == user) {
				return CommUtil.responseBuildInfo(103, "获取用户信息失败", datamap);
			}
			Map<String, Object> result =  onlineCardService.inquireOnlineInfo(request, user);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**
	 * @Description：商户绑定在线卡
	 * @author： origin
	 * @createTime：2019年12月24日下午5:32:52
	 * @return:Object
	 */
	@RequestMapping("/dealerBindingOnline")
	@ResponseBody
	public Object dealerBindingOnline(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (null == user) {
				return "erroruser";
			}
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer dealid =  CommUtil.toInteger(user.getId());
			Integer areaid =  CommUtil.toInteger(maparam.get("areaid"));
			if(areaid.equals(-1)) areaid = 0;
			String cardnum = CommUtil.toString(maparam.get("cardnum"));
			Integer status = 1;
			Map<String, Object> result = onlineCardService.dealBindingOnlineCard(dealid, areaid, cardnum, status);
			Area area =  areaService.selectByIdArea(areaid);
			area = area == null ? new Area() : area;
			result.put("areaname", area.getName());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	@RequestMapping("/checkHistoryEarn")
	public String checkHistoryEarn(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			List<Codestatistics> daysEarn = userService.selectAllByMerid(user.getId(),DisposeUtil.getPastDate(7,1),DisposeUtil.getPastDate(0,1));
			model.addAttribute("daysEarn", daysEarn);
			Double allTotalMoney = 0.0;
			Double allcoinsmoney = 0.0;
			for (Codestatistics earn : daysEarn) {
				Double income = earn.getMoneytotal()-earn.getWechatretmoney()-earn.getAlipayretmoney();
				allTotalMoney += income;
				allcoinsmoney += earn.getIncoinsmoney();
			}
			model.addAttribute("daysEarn", daysEarn);
			model.addAttribute("allcoinsmoney", allcoinsmoney);
			model.addAttribute("allTotalMoney", allTotalMoney);
			return "merchant/checkHistoryEarn";
		} else {
			return "erroruser";
		}
	}
	
	@RequestMapping("/queryDaysEarn")
	@ResponseBody
	public Map<String, Object> queryDaysEarn(Model model,String begintime,String endtime) {
		User user = (User) request.getSession().getAttribute("user");
		Map<String, Object> map = new HashMap<>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String currenttime = sf.format(new Date());
		if (user != null) {
			if ((currenttime.compareTo(endtime) < 0) || (currenttime.compareTo(begintime) < 0)) {
				map.put("status", "0");
				return map;
			} else {
				List<Codestatistics> daysEarn = userService.selectAllByMerid(user.getId(),begintime,endtime);
				map.put("status", "1");
				map.put("daysEarn", daysEarn);
				Double allTotalMoney = 0.0;
				Double allcoinsmoney = 0.0;
				for (Codestatistics earn : daysEarn) {
					Double income = earn.getMoneytotal()-earn.getWechatretmoney()-earn.getAlipayretmoney();
					allTotalMoney += income;
					allcoinsmoney += earn.getIncoinsmoney();
					earn.setCountIOSTime(new SimpleDateFormat("yyyy-MM-dd").format(earn.getCountTime()));
				}
				map.put("allTotalMoney", allTotalMoney);
				map.put("allcoinsmoney", allcoinsmoney);
				return map;
			}
		} else {
			map.put("status", "0");
			return map;
		}
	}
	
	@RequestMapping("/setCardStatus")
	@ResponseBody
	public Map<String, Object> setCardStatus(Integer id, Integer status) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		OnlineCard onlineCardById = onlineCardService.selectOnlineCardById(id);
		if (null == onlineCardById) {
			codemap.put("code", 101);
			return codemap;
		}

		Double cardaccountmoney = CommUtil.toDouble(onlineCardById.getMoney());
		Double cardsendmoney = CommUtil.toDouble(onlineCardById.getSendmoney());
		Double cardmoney = CommUtil.addBig(cardaccountmoney, cardsendmoney);
		OnlineCard onlineCard = new OnlineCard();
		onlineCard.setId(id);
		onlineCard.setStatus(status);
		onlineCardService.updateOnlineCard(onlineCard);
		int statusrecord = 0;
		if (status == 1) {
			statusrecord = 6;
		} else if (status == 2) {
			statusrecord = 5;
		}
		onlineCardRecordService.insertOnlineCardRecord(onlineCardById.getUid(), onlineCardById.getMerid(), HttpRequest.createOrdernum(6), 
			onlineCardById.getCardID(), null, cardmoney, 0.00, 0.00, 0.00, cardaccountmoney, cardsendmoney, 4, statusrecord, null, new Date(),onlineCardById.getMerid());
		codemap.put("code", 200);
		return codemap;
	}
	
	@RequestMapping({ "/regist" })
	public String regist(Model model) {
		model.addAttribute("rank", Integer.valueOf(3));
		return "merchant/regist";
	}

	@RequestMapping({ "/save" })
	public String save(User user) {
		this.userService.addUser(user);
		return "merchant/index";
	}
	
	@RequestMapping(value = "/verifyorder")
	@ResponseBody
	public int verifyOrder(HttpServletRequest request, HttpServletResponse response, Model model) {
		String ordernum = request.getParameter("ordernum");
		String paysource = request.getParameter("paysource");
		int verify = 0;
		int statu = 0;
		//支付来源 1、充电模块-2、脉冲模块-3、离线充值机-4、用户充值钱包',
		if (paysource.equals("1")) {
			List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
			//`number` int(11) DEFAULT '0' COMMENT '退款状态 0 交易正常 1 已退款',
			statu = chargeRecord.get(0).getNumber();
			if(statu==0) verify = chargeRecord.get(0).getId();
		} else if (paysource.equals("2")) {
			InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
			// status  操作类型 -- 1通过微信下发投币、2通过支付宝下发投币、3设备投币成功信息上传服务器、4 微信退款、5 支付宝退款、6 钱包支付、7钱包退款 
			statu = inCoins.getStatus();
			if(statu==1 || statu==2 || statu==8 || statu==10) verify = inCoins.getId();
		} else if (paysource.equals("3")) {
			//paytype 支付类型————1-wx、2-alipay、3-wx refund、4-alipay refund、5-wallet、6-wallet refund
			OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
			statu = offlineCard.getPaytype();
			if(statu==1 || statu==2 || statu==8) verify = offlineCard.getId();
		} else if (paysource.equals("4")) {
			Money money = moneyService.queryMoneyByOrdernum(ordernum);
			statu = money.getPaytype();
			if(statu==0) verify = money.getId();
		}/* else if (paysource.equals("5")) {
			OnlineCardRecord cardRecord = onlineCardRecordService.selectRecordByOrdernum(ordernum);
			statu = cardRecord.getType();
			if(statu==3) verify = cardRecord.getId();
			
		}*/
		return verify;
	}
	
	/**
	 * @Description：   根据条件查询用户消费信息
	 * @author： origin 
	 * @return hd_realchargerecord
	 */
	@RequestMapping(value = "/powerbrokenline")
	public String realChargeRecordList(Integer source, Integer orderId, Model model) {
		Map<String, Object> result = new HashMap<>();
		String ordernum = null;
		try {
			orderId = CommUtil.toInteger(orderId);
			if(orderId==0){
				return "erroruser";
			}
			if(CommUtil.toInteger(source)==1){
				TradeRecord trade = tradeRecordService.selectTradeById(CommUtil.toInteger(orderId));
				ordernum = CommUtil.toString(trade.getOrdernum());
				if(ordernum!=null && !ordernum.equals("")){
					List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
					if(chargeRecord != null && chargeRecord.size()>0){
						orderId = CommUtil.toInteger(chargeRecord.get(0).getId());
					}
				}
			}
			result = chargeRecordService.powerBrokenLine(CommUtil.toInteger(orderId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String chaordernum = CommUtil.toString(result.get("ordernum"));
		chaordernum = chaordernum == null ? ordernum : chaordernum;
		model.addAttribute("orderId", result.get("orderId"));
		model.addAttribute("include", result.get("include"));
		model.addAttribute("iscontinue", result.get("iscontinue"));
		model.addAttribute("listcharge", result.get("listcharge"));
		model.addAttribute("mapfunc", result.get("mapfunc"));
		model.addAttribute("number", result.get("number"));
		model.addAttribute("mapinfo", result);
		model.addAttribute("ordernum", chaordernum);
		model.addAttribute("realfisrt", result.get("realfisrt"));
		model.addAttribute("reallast", result.get("reallast"));
		model.addAttribute("realrecord", result.get("realrecord"));
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		return "equipment/powerbrokenline";
	}
	
	/**
	 * @Description： 根据订单查询功率信息生成图像
	 * @author： origin 
	 */
	@RequestMapping(value = "/powerdrawing")
	@ResponseBody
	public Object graphjson(Integer orderId, Model model) {
		List<Realchargerecord> realrecord = new ArrayList<>();
		orderId = CommUtil.toInteger(orderId);
		if(orderId!=0){
			realrecord = equipmentService.realChargeRecordList(CommUtil.toInteger(orderId));
		}
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			if(realrecord.size()>0){
				ChargeRecord charge = chargeRecordService.chargeRecordOne(CommUtil.toInteger(orderId));
				Date begintime = charge.getBegintime();
				for(Realchargerecord pLog : realrecord){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("chargetime", pLog.getChargetime());
					map.put("surpluselec", pLog.getSurpluselec());
					map.put("power", pLog.getPower());
					String time = StringUtil.toDateTime("MM-dd HH:mm", pLog.getCreatetime());
					map.put("createtime", time);
					map.put("minuteTime", StringUtil.minuteTime(begintime, pLog.getCreatetime()));
					map.put("portV", pLog.getPortV());
					map.put("portA", pLog.getPortA());
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return list;
	}
	//@RZC origin
	@RequestMapping(value = "/areaWalletTemp")
	public String areaWalletTemp(Integer id,Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			model.addAttribute("arecode", id);
			model.addAttribute("source", 2);
			Area area = areaService.selectByIdArea(id);
			List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantidwolf(user.getId(), 3);
			if (templatelist != null && templatelist.size() > 0) {
				if (area.getTempid() != null) {
					List<TemplateParent> templatelist1 = new ArrayList<>();
					for (int i = 0; i < templatelist.size(); i++) {
						TemplateParent templateParent = templatelist.get(i);
						if (area.getTempid().equals(templateParent.getId())) {
							templateParent.setPitchon(1);
							templatelist1.add(templateParent);
							templatelist.remove(templateParent);
						}
					}
					templatelist1.addAll(templatelist);
					model.addAttribute("templatelist", templatelist1);
				} else {
					model.addAttribute("templatelist", templatelist);
				}
			}
			model.addAttribute("user", user);
			model.addAttribute("aid", id);
			return "/merchant/templatewallet";
		} else {
			return "erroruser";
		}
	}
	
	@RequestMapping(value = "/getMerAllWalletTemp")
	public String getMerAllWalletTemp(Integer merid,Integer aid, Model model) {
		User user = userService.selectUserById(merid);
		if (user != null) {
			model.addAttribute("arecode", aid);
			model.addAttribute("source", 2);
			List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantid(merid, 3);
			model.addAttribute("templatelist", templatelist);
			model.addAttribute("user", user);
			return "/merchant/templatewallet";
		} else {
			return "erroruser";
		}
	}
	
	//@RZC origin
	@RequestMapping(value = "/areaOnlineCardTemp")
	public String areaOnlineCardTemp(Integer id,Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			model.addAttribute("arecode", id);
			model.addAttribute("source", 3);
			Area area = areaService.selectByIdArea(id);
			List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantidwolf(user.getId(), 4);
			if (templatelist != null && templatelist.size() > 0) {
				if (area.getTempid2() != null) {
					List<TemplateParent> templatelist1 = new ArrayList<>();
					for (int i = 0; i < templatelist.size(); i++) {
						TemplateParent templateParent = templatelist.get(i);
						if (area.getTempid2().equals(templateParent.getId())) {
							templateParent.setPitchon(1);
							templatelist1.add(templateParent);
							templatelist.remove(templateParent);
						}
					}
					templatelist1.addAll(templatelist);
					model.addAttribute("templatelist", templatelist1);
				} else {
					model.addAttribute("templatelist", templatelist);
				}
			}
			model.addAttribute("user", user);
			model.addAttribute("aid", id);
			return "/merchant/templateonline";
		} else {
			return "erroruser";
		}
	}
	
	@RequestMapping(value = "/getMerAllOnlineCardTemp")
	public String getMerAllOnlineCardTemp(Integer merid,Integer aid, Model model) {
		User user = userService.selectUserById(merid);
		if (user != null) {
			model.addAttribute("arecode", aid);
			model.addAttribute("source", 3);
			List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantid(merid, 4);
			model.addAttribute("templatelist", templatelist);
			model.addAttribute("user", user);
			return "/merchant/templateonline";
		} else {
			return "erroruser";
		}
	}
	
	@RequestMapping(value="/membersystem")
    public Object membercentre(HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (user != null) {
				Integer dealid = CommUtil.toInteger(user.getId());
				Map<String, Object> reslet = userService.selectmembercentre(request);
				List<Map<String, Object>> areaData = CommUtil.isListMapEmpty(areaService.inquireDealAreaData(dealid));
				model.addAttribute("areaData", areaData);
				model.addAttribute("memberdata", reslet.get("datalist"));
				model.addAttribute("datasize", CommUtil.toInteger(reslet.get("datasize")));
				model.addAttribute("datamoney", CommUtil.toDouble(reslet.get("datamoney")));
				model.addAttribute("datasendmoney", CommUtil.toDouble(reslet.get("datasendmoney")));
				model.addAttribute("totalRows", reslet.get("totalRows"));
				model.addAttribute("totalPages", reslet.get("totalPages"));
				model.addAttribute("currentPage", reslet.get("currentPage"));
				return "/membersys/membersystem";
			} else {
				return "erroruser";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	@RequestMapping(value = "/ajaxMemberCentre")
	@ResponseBody
	public Object ajaxMemberCentre(HttpServletRequest request, HttpServletResponse response) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (user != null) {
				Map<String, Object> result = userService.ajaxMemberCentre(request);
				return result;
			} else {
				return CommUtil.responseBuild(102, "商户信息获取失败", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuild(301, "异常错误", "");
		}
	}
	
	@RequestMapping(value = "/memberrole")
	@ResponseBody
	public Map<String, Object> memberRole( String member, Integer role, Model model){
		User user = (User) request.getSession().getAttribute("user");
		Map<String, Object> result = userService.memberRole(user, member, role);
		return result;
	}
	
	
	/**
	 * @Description： 在线卡绑定小区 （商户）
	 * @author： origin          
	 * 创建时间：   2019年6月3日 下午4:41:58
	 */
	@RequestMapping(value = "/onlinebindingarea")
	@ResponseBody
	public Map<String, Object> onlineBindingArea( String cardNumber, Integer aid, Model model){
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		OnlineCard online = onlineCardService.selectOnlineCardByCardID(cardNumber);
		if (null == online) {
			codemap.put("code", 101);
			return codemap;
		}
		online.setAid(aid); 
		onlineCardService.updateOnlineCardBycard(online);
		codemap.put("online", online);
		codemap.put("code", 200);
		return codemap;
	}
	
	/**
	 * @Description： 解绑在线卡
	 * @param: 
	 * @author： origin   2019年12月23日 下午2:28:35
	 */
	@RequestMapping(value="/unbindingOnlineCard")
	@ResponseBody
    public Object unbindingOnlineCard(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			CommUtil.responseBuildInfo(100, "获取商户信息失败", datamap);
			return datamap;
		}
		Object result =  onlineCardService.unbindingOnlineCard(request);
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description： 解绑在线卡
	 * @param: 
	 * @author： origin   2019年12月23日 下午2:28:35
	 */
	@RequestMapping(value="/deleteOnlineCard")
	@ResponseBody
    public Object deleteOnlineCard(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			CommUtil.responseBuildInfo(100, "获取商户信息失败", datamap);
			return datamap;
		}
		Object result =  onlineCardService.deleteOnlineCard(request);
		return JSON.toJSON(result);
	}
	
	/**
	 * @Description： 跳转到虚拟充值页面
	 * @author： origin 创建时间：   2019年7月16日 上午9:41:35
	 */
	@RequestMapping(value = "/mercVirtualPayMoney")
	public Object mercVirtualPayMoney( Integer type, Integer id, Model model){
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "erroruser";
		}
		type = (type == null ? 1 : type );
		Object order = null;
		if(type==1){
			User tourist = userService.selectUserById(CommUtil.toInteger(id));
			order = tourist;
			Integer areaid = CommUtil.toInteger(tourist.getAid());
			model.addAttribute("areadata", areaService.selectByIdArea(areaid));
		}else if(type==2){
			OnlineCard online = onlineCardService.selectOnlineCardById(CommUtil.toInteger(id));
			User orderus = userService.selectUserById(online.getUid());
			if(online.getRelevawalt()==1){
				online.setMoney(orderus.getBalance());
				online.setSendmoney(orderus.getSendmoney());
			}
			order = online;
			Integer areaid = CommUtil.toInteger(online.getAid());
			model.addAttribute("areadata", areaService.selectByIdArea(areaid));
			model.addAttribute("useronline", orderus);
		}
		PackageMonth packageMonth = userService.selectPackageMonthByUid(id);
		model.addAttribute("packageMonth", packageMonth);
		model.addAttribute("order", order);
		model.addAttribute("user", user);
		model.addAttribute("type", type);
		model.addAttribute("id", id);
		return "/merchant/mercVirtualPay";
	}
	
	/**
	 * @Description： 商户虚拟充值
	 * @author： origin 创建时间：   2019年7月10日 下午4:43:06
	 */
	@RequestMapping(value = "/mercVirtualPay")
	@ResponseBody
	public Object mercVirtualPay( Double money, Double sendmoney, Integer type, Integer id, Integer status, Model model){
		User user = (User) request.getSession().getAttribute("user");
		if(user == null || CommUtil.toInteger(user.getId()).equals(0)){
			return CommonConfig.messg(400);
		}
		Object result = null;
		if(type==1){
			result = moneyService.mercVirtualMoneyPay(user, money, sendmoney, id, null, status);//id:充值钱包对象的id（用户id）
		}else if(type==2){
			OnlineCard online = onlineCardService.selectOnlineCardById(id);
			if(online.getRelevawalt()==2){
				result = onlineCardService.mercVirtualOnlinePay(user, money, sendmoney, id, status);//id:指定在线卡id
			}else{
				result = moneyService.mercVirtualMoneyPay(user, money, sendmoney, online.getUid(), online.getCardID(), status);//id:充值钱包对象的id（用户id）
			}
		}
		return result;
	}
	
	/**
	 * @Description： 商户虚拟充值结果
	 * @author： origin 创建时间：   2019年7月10日 下午4:43:06
	 */
	@RequestMapping(value = "/mercVirtualResult")
	public Object mercVirtualPay( Integer type, String ordernum, Integer uid, String cardID, Model model){
		Map<String, Object> datamap = new HashMap<String, Object>();
		Parameters parame = new Parameters();
		if(type.equals(1)){
			parame.setOrder(ordernum);
			parame.setParamete(CommUtil.toString(uid));
			List<Map<String, Object>> listdata =  CommUtil.isListMapEmpty(moneyService.selectWalletRecord(parame));
			Map<String, Object> result = listdata.get(0);
			datamap.put("nick", result.get("username"));
			datamap.put("realname", result.get("realname"));
			datamap.put("ordernum", result.get("ordernum"));
			datamap.put("paymoney", result.get("money"));
			datamap.put("sendmoney", result.get("sendmoney"));
			datamap.put("accountmoney", result.get("tomoney"));
			datamap.put("balance", result.get("balance"));
			datamap.put("paytime", result.get("paytime"));
//			model.addAttribute("result", listdata);
		}else if(type.equals(2)){
			parame.setOrder(ordernum);
			parame.setNumber(CommUtil.toString(cardID));
			parame.setStatus(CommUtil.toString(uid));
			List<Map<String, Object>> listdata =  CommUtil.isListMapEmpty(onlineCardService.selectOonlineConsume(parame));
			Map<String, Object> result = listdata.get(0);
			datamap.put("nick", result.get("nickname"));
			datamap.put("realname", result.get("username"));
			datamap.put("ordernum", result.get("ordernum"));
			datamap.put("paymoney", result.get("money"));
			datamap.put("sendmoney", result.get("sendmoney"));
			datamap.put("accountmoney", result.get("accountmoney"));
			datamap.put("balance", result.get("balance"));
			datamap.put("paytime", result.get("create_time"));
//			model.addAttribute("result", listdata);
		}
		datamap.put("ordernum", ordernum);
		datamap.put("uid", uid);
		datamap.put("cardID", cardID);
		datamap.put("type", type);
		model.addAttribute("result", datamap);
		return "/merchant/virtualRechangeInfo";
	}
	
	@RequestMapping("/queryAreaEarnRecord")
	public String queryAreaEarnRecord(Integer aid,Model model) {
		User user = (User) this.request.getSession().getAttribute("user");
		List<Areastatistics> areaEarnlist = areaService.selectAreastatisticsByAid(aid, 0, 30);
		model.addAttribute("areaEarnlist", areaEarnlist);
		DealerAuthority jurisdiction = authorityService.selectMessSwitch(user.getId());
		model.addAttribute("showincoins", CommUtil.toInteger(jurisdiction.getShowincoins()));
		return "/merchant/areaEarnRecord";
	}
	
	@RequestMapping("/queryAjaxAreaEarnRecord")
	public List<Areastatistics> queryAjaxAreaEarnRecord(Integer aid,Integer beginnum,Model model) {
		model.addAttribute("aid", aid);
		List<Areastatistics> areaEarnlist = areaService.selectAreastatisticsByAid(aid, beginnum, 25);
		return areaEarnlist;
	}
	
	/**
	 * @Description： 管理中心订单统 查询（手机端）
	 * @author： origin  
	 */
	@RequestMapping({ "/orderinquire" })
	public String orderinquire(Model model) {
		User user = (User) this.request.getSession().getAttribute("user");
		if (user != null) {
			String pastDate = DisposeUtil.getPastDate(0,1);
			List<TradeRecord> selectPartByParam = tradeRecordService.selectPartByParam(user.getId(), 
					null, null, null, null, pastDate + " 00:00:00", pastDate + " 23:59:59");
			if(null == selectPartByParam || selectPartByParam.isEmpty()){
				selectPartByParam = tradeRecordService.selectPartByParam(user.getId(), 
						null, null, null, null, null, null);
			}
			model.addAttribute("tradelist", selectPartByParam);
			return "merchant/orderinquire";
		} else {
			return "erroruser";
		}
	}
	
	/**
	 * @Description： 管理中心订单统 查询Ajax（手机端）
	 * @author： origin  
	 */
	@RequestMapping("/queryTradeByParam")
	@ResponseBody
	public List<TradeRecord> queryTradeByParam(String ordernum,String code,Integer paytype,
			Integer status,String startTime,String endTime) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			List<TradeRecord> selectPartByParam = tradeRecordService.selectPartByParam(user.getId(), ordernum, code, paytype, status, startTime, endTime);
			for(TradeRecord item : selectPartByParam){
				item.setRemark(StringUtil.toDateTime(item.getCreateTime()));
			}
			return selectPartByParam;
		} else {
			return new ArrayList<TradeRecord>();
		}
	}
	
	/**
	 * @Description：管理中心订单统计详情查看 
	 * @author： origin 创建时间：   2019年8月21日 上午9:37:53
	 */
	@RequestMapping("/orderinquiredetails")
	public String queryTradeByParam( String ordernum, Integer orderid, Model model) {
		try {
			Integer oderid = 0;
			User meruser = (User) request.getSession().getAttribute("user");
			TradeRecord trade = tradeRecordService.selectTradeById(CommUtil.toInteger(orderid));
			Map<String, Object>  equip = equipmentService.selectEquipAreaInfo(trade.getCode());
			User tourist = userService.selectUserById(CommUtil.toInteger(trade.getUid()));
			Integer paysource = trade.getPaysource();
			Integer status = trade.getStatus();
			Integer paytype = trade.getPaytype();
			model.addAttribute("tourist", tourist);
			model.addAttribute("equip", equip);
			if(paysource==1){
				List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
//				List<ChargeRecord> chargeRecord = chargeRecordService.getRecordData(ordernum,paytype);
				ChargeRecord record = chargeRecord.size() > 0 ? chargeRecord.get(0) : new ChargeRecord();
				oderid = CommUtil.toInteger(record.getId());
				model.addAttribute("order", record);
			}else if(paysource==2){
				InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
				inCoins = inCoins == null ? new InCoins() : inCoins;
				oderid = CommUtil.toInteger(inCoins.getId());
				model.addAttribute("order", inCoins);
			}else if(paysource==3){
				OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
				offlineCard = offlineCard == null ? new OfflineCard() : offlineCard;
				oderid = CommUtil.toInteger(offlineCard.getId());
				model.addAttribute("order", offlineCard);
			}else if(paysource==4){
				Money money = moneyService.queryMoneyByOrdernum(ordernum);
				money = money == null ? new Money() : money;
				oderid = CommUtil.toInteger(money.getId());
				model.addAttribute("order", money);
			}else if(paysource==5){//origin 在线卡详情
				OnlineCardRecord online = new OnlineCardRecord();
				online.setOrdernum(ordernum);
				List<OnlineCardRecord> onlinelist = onlineCardRecordService.selectCardRecord(online);
				OnlineCardRecord onlinerecord = new OnlineCardRecord();
				for(OnlineCardRecord item : onlinelist){
					Integer type = item.getType();
					if(status==1){
						if(type==1 || type==3 || type==6 || type==8 || type==10) onlinerecord = item;
					}else if(status==2){
						if(type==2 || type==5 || type==7 || type==9 || type==11) onlinerecord = item;
					}
				}
				oderid = CommUtil.toInteger(onlinerecord.getId());
				model.addAttribute("order", onlinerecord);
			}else if(paysource==6){
				List<PackageMonthRecord> packagemont = tradeRecordService.selectMonthRecordEntiy(ordernum, status);
				PackageMonthRecord record = packagemont.size() > 0 ? packagemont.get(0) : new PackageMonthRecord();
				oderid = CommUtil.toInteger(record.getId());
				model.addAttribute("order", record);
			}
			model.addAttribute("orderid", oderid);
			model.addAttribute("partrecord", trade);
			model.addAttribute("paysource", paysource);
			model.addAttribute("paytype", paytype);
			model.addAttribute("status", status);
			model.addAttribute("user", meruser);
			return "merchant/orderinquiredetails";
		} catch (Exception e) {
			return "erroruser";
		}
	}
	
	
	
//	/**
//	 * 展示商家小区下的设备
//	 * @param areaId 小区id
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping("/merShowAreaDevice")
//	public String merShowAreaDevice(Integer areaId,Model model){
//		User user=(User)request.getSession().getAttribute("user");
//		//商家的收费标准
//		if(user != null && user.getId() != null){
//			List<Map<String,Object>> merList=feescaleService.getMerFeescale(user.getId());
//			DealerAuthority message = authorityService.selectMessSwitch(user.getId());
//			//获取小区信息
//			Area area = areaService.selectByIdArea(areaId);
//			//小区下的合伙人
//			List<Map<String, Object>> result = areaService.selectAreaRelev(areaId);
//			List<Object> listscanpa = new ArrayList<>();
//			//小区合伙人的设备
//			List<Equipment> equlistByAid = equipmentService.selectAreaEqulistOrderyByExpire(areaId);
//			for(Map<String, Object> item : result){
//				Integer type = CommUtil.toInteger(item.get("type"));
//				if(type==2){
//					listscanpa.add(item);
//				}
//			}
//			model.addAttribute("net", merList.get(0));//网络
//			model.addAttribute("blue", merList.get(1));//蓝牙
//			model.addAttribute("area", area);//小区
//			model.addAttribute("merid", user.getId());//商家id
//			model.addAttribute("mername",user.getUsername());//商家真实姓名
//			model.addAttribute("mer",user);//商户的信息
//			model.addAttribute("listscanpa", listscanpa);//合伙人
//			model.addAttribute("equlistByAid", equlistByAid);//合伙的设备
//			model.addAttribute("authority", message);
//			return "merchant/merShowAreaDevice";
//		}else{
//			return "erroruser";
//		}
//	}
	/**
	 * 商家为设备缴费(钱包缴费)
	 * @return {@link String}
	 */
	@RequestMapping("/merPayment")
	@ResponseBody
	public String merPayment(String users,String devices,Integer paytype,Integer id){
		Object result=null;
		User user = (User) request.getSession().getAttribute("user");
		if(user != null){
			List<User> listUser= JSON.parseArray(users, User.class);
			List<Equipment> listEquipment=JSON.parseArray(devices, Equipment.class);
			
			System.out.println("这是用户的信息"+listUser);
			System.out.println("这是设备的信息"+listEquipment);
			
			result=feescaleService.merPayment(listUser, listEquipment,paytype,id);
			return JSON.toJSONString(result);
		}else{
			result=CommUtil.responseBuild(405,"用户信息错误", "");
			return JSON.toJSONString(result);
		}
	}
	
	/**
	 * 商家扫描设备的编号换IMEI号
	 * @return
	 */
	@RequestMapping("/merTranspositionImei")
	@ResponseBody
	public Object merTranspositionImei(HttpServletRequest request, Model model){
		try {
			Map<String, Object>  result = new HashMap<String, Object>();
			User user=(User)request.getSession().getAttribute("user");
			if(user == null){
				return "erroruser";
			}else{
				result = equipmentService.merTranspositionImei(request);
				System.out.println("返回数据"+result);
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			return CommUtil.responseBuildInfo(301, "异常错误", dataMap);
		}
	}
	
	@RequestMapping("/addOrDelChileUser")
	@ResponseBody
	public Map<String, Object> addOrDelChileUser() {
		String type = request.getParameter("type");
		User meruser = (User) request.getSession().getAttribute("user");
		if (meruser == null) {
			return CommUtil.responseBuildInfo(301, "当前账号登陆已过期，请关闭此页面，重新登陆后再次进行此操作", null);
		}
		if ("1".equals(type)) {//添加用户
			String phonenum = request.getParameter("phoneNum");
			User user = userService.existAdmin(phonenum);
			if (user == null) {
				return CommUtil.responseBuildInfo(302, "此账户不存在，注册流程‘进入自助平台公众号’->商家登陆->填写手机号进行注册", null);
			} else if (user.getLevel() != 1) {
				return CommUtil.responseBuildInfo(303, "此账号不可设置为子账号" + user.getLevel(), null);
			} else if (!meruser.getId().equals(user.getMerid()) && (user.getMerid() != null && user.getMerid() != 0)) {
				return CommUtil.responseBuildInfo(304, "此账号所属商户不属于当前商户，如有疑问，请联系管理员" + user.getLevel(), null);
			} else if (user.getLevel() != 1) {
				return CommUtil.responseBuildInfo(303, "此账号已设置为子账号，无需再次设置" + user.getLevel(), null);
			} else {
				User editUser = new User();
				editUser.setId(user.getId());
				editUser.setLevel(6);
				if (user.getMerid() == null || user.getMerid() == 0) {
					editUser.setMerid(meruser.getId());
				}
				userService.updateUserById(editUser);
				Map<String, Object> hashMap = new HashMap<>();
				hashMap.put("username", user.getUsername());
				hashMap.put("phoneNum", user.getPhoneNum());
				userService.insertUserPrivliege(user.getId(), 3);
				userService.insertUserPrivliege(user.getId(), 7);
				return CommUtil.responseBuildInfo(200, "添加成功", hashMap);
			}
		} else if ("2".equals(type)) {//删除用户
			String phonenum = request.getParameter("phoneNum");
			User user = userService.existAdmin(phonenum);
			if (user == null) {
				return CommUtil.responseBuildInfo(302, "此账户不存在", null);
			} else {
				User editUser = new User();
				editUser.setId(user.getId());
				editUser.setLevel(1);
				userService.updateUserById(editUser);
				userService.deleteUserPrivliege(user.getId(), 3);
				try {
					userService.deleteUserPrivliege(user.getId(), 7);
				} catch (Exception e) {
				}
				return CommUtil.responseBuildInfo(200, "删除成功", null);
			}
		} else {
			return CommUtil.responseBuildInfo(305, "参数异常，请联系管理进行后续处理", null);
		}
	}
	

	/*** 手机端处理模板信息  ************************************************************************************/
	/**
	 * @method_name: additionTemp
	 * @Description: 模板处理
	 * @param request   response
	 * @return
	 * @Author: origin  创建时间:2020年8月24日 下午5:15:59
	 * @common:
	 */
	@RequestMapping(value="/additionAndEditTemp")
	@ResponseBody
    public Object additionTemp(HttpServletRequest request, HttpServletResponse response) {
		Object result =  templateService.additionAndEditTemp(request);
		return result;
	}
	
	/**
	 * @method_name: templataPreview
	 * @Description: 模板预览处理
	 * @param request   response
	 * @return
	 * @Author: origin  创建时间:2020年8月24日 下午5:16:14
	 * @common:
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/templataPreview")
    public Object templataPreview(HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()) return "erroruser";
			Map<String, Object> result =  templateService.templataPreview(request);
			String code = CommUtil.toString(maparam.get("code"));
			Map<String, Object> devicedata = equipmentService.selectEquipAreaInfo(code);
			String version = CommUtil.toString(devicedata.get("hardversion"));
			List<TemplateSon> tempson = new ArrayList<>();
			
			TemplateParent temppare = (TemplateParent) result.get("template");
			Integer temporaryc = CommUtil.toInteger(temppare.getPermit());
			List<TemplateSon> listtempson = (List<TemplateSon>) result.get("listtempson");
			List<TemplateSon> listtempower = (List<TemplateSon>) result.get("listtempower");
			List<TemplateSon> listtemtime = (List<TemplateSon>) result.get("listtemtime");
			List<TemplateSon> listtemmoney = (List<TemplateSon>) result.get("listtemmoney");

			tempson.addAll(listtempson);
			tempson.addAll(listtempower);
			tempson.addAll(listtemtime);
			tempson.addAll(listtemmoney);
			
			model.addAttribute("temporaryc", temporaryc);
			model.addAttribute("tempson", tempson);
			model.addAttribute("deviceType", version);
			model.addAttribute("code", code);
			model.addAttribute("devicedata", devicedata);
			model.addAttribute("temppare", temppare);
			return "merchant/previewChargeTem";
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	/**
	 * @method_name: editTempForInfo
	 * @Description: 修改模板充电提示信息
	 * @param request  response
	 * @Author: origin  创建时间:2020年8月20日 下午4:27:31
	 * @common:
	 */
	@RequestMapping(value="/editTempForInfo")
	@ResponseBody
    public Object editTempForInfo(HttpServletRequest request, HttpServletResponse response) {
		Object result =  templateService.editTempForInfo(request);
		return result;
	}

	/**
	 * @method_name: additionTempSon
	 * @Description: 模板处理： 单独添加子模板
	 * @param request  response
	 * @Author: origin  创建时间:2020年8月20日 下午3:15:48
	 * @common:
	 */
	@RequestMapping(value="/additionTempSon")
	@ResponseBody
    public Object additionTempSon(HttpServletRequest request, HttpServletResponse response) {
		Object result =  templateService.additionTempSonData(request);
		return result;
	}
	
	/**
	 * @method_name: deleteTemplateSonData
	 * @Description: 删除子模板信息
	 * @param request  response
	 * @return
	 * @Author: origin  创建时间:2020年8月20日 下午4:28:15
	 * @common:
	 */
	@RequestMapping(value="/deleteTemplateSonData")
	@ResponseBody
    public Object deleteTemplateSonData(HttpServletRequest request, HttpServletResponse response) {
		Object result =  templateService.deleteTemplateSonData(request);
		return result;
	}
	
	/**
	 * @method_name: deleteTemplateData
	 * @Description: 删除所有模板信息（主模板、子模板）（如果使用，不能删除）
	 * @param request  response
	 * @return
	 * @Author: origin  创建时间:2020年8月20日 下午5:00:49
	 * @common:
	 */
	@RequestMapping(value="/deleteTemplateData")
	@ResponseBody
    public Object deleteTemplateData(HttpServletRequest request, HttpServletResponse response) {
		Object result = templateService.deleteTemplateData(request);
		return result;
	}
	

	/**
	 * separate
	 * @Description： 选中模板信息
	 * @author： origin 
	 */
	@RequestMapping(value="/pitchOnTemplate")
	@ResponseBody
    public Object pitchOnTemplate(HttpServletRequest request, HttpServletResponse response) {
		Object result = templateService.pitchOnTemplate(request);
		return result;
	}

	/**
	 * separate
	 * @Description： 选中默认模板信息
	 * @author： origin 
	 */
	@RequestMapping(value="/defaultPitchOnTemplate")
	@ResponseBody
    public Object defaultPitchOnTemplate(HttpServletRequest request, HttpServletResponse response) {
		Object result = templateService.defaultPitchOnTemplate(request);
		return result;
	}
	/***********************************************************************************************/
	
}
