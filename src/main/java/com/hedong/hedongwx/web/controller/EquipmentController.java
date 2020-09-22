package com.hedong.hedongwx.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.hedong.hedongwx.config.AlipayConfig;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.CodeSystemParam;
import com.hedong.hedongwx.entity.Codestatistics;
import com.hedong.hedongwx.entity.DealerAuthority;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.PackageMonth;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.AuthorityService;
import com.hedong.hedongwx.service.BasicsService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.thread.Server;
import com.hedong.hedongwx.utils.ByteUtils;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.JedisUtils;
import com.hedong.hedongwx.utils.SendMsgUtil;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.WeixinUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/equipment")
public class EquipmentController {

	private final Logger logger = LoggerFactory.getLogger(EquipmentController.class);
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private UserService userService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private InCoinsService inCoinsService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private BasicsService basicsService;
	
	/**
	 * @Description： 设置报警系统
	 */
	@RequestMapping(value="/alarmtemperatureline")
    public Object alarmtemperatureline(String code, Model model) {
		model.addAttribute("code", code);
		return "equipment/alarmTemperatureLine";
	}
	
	/**
	 * @Description： 设置报警系统
	 */
	@RequestMapping(value="/alarmsystem")
    public Object devicePortQRCode(String code, Model model) {
		try {
			CodeSystemParam codeSystemParam = equipmentService.selectCodeSystemParamByCode(code);
			Integer hotDoorsill =CommUtil.toInteger(codeSystemParam.getHotDoorsill());
			Integer smokeDoorsill =CommUtil.toInteger(codeSystemParam.getSmokeDoorsill());
			Integer powerTotal =CommUtil.toInteger(codeSystemParam.getPowerTotal());
			model.addAttribute("hotDoorsill", hotDoorsill);
			model.addAttribute("smokeDoorsill", smokeDoorsill);
			model.addAttribute("powerTotal", powerTotal);
			Integer merid = 0;
			equipmentService.getWarnParamsInfo(code, merid, 3, null);
			equipmentService.getWarnParamsInfo(code, merid, 3, 1);
			equipmentService.getWarnParamsInfo(code, merid, 3, 2);
			equipmentService.getWarnParamsInfo(code, merid, 3, 3);
			equipmentService.getWarnParamsInfo(code, merid, 3, 4);
			
//			
//
//			  `id` int(200) NOT NULL AUTO_INCREMENT COMMENT '表自增id',
//			  `code` varchar(255) NOT NULL COMMENT '设备号信息',
//			  `merid` int(200) NOT NULL COMMENT '商户id信息',
//			  `opertype` int(25) NOT NULL DEFAULT '1' COMMENT '类型 1:设置的  2:报警的  3:查询的   4:',
//			  `type` int(25) NOT NULL DEFAULT '1' COMMENT '类型 1:温度   2:电量   3:烟感   4:功率   ',
//			  `operid` int(25) NOT NULL DEFAULT '0' COMMENT '操作人id',
//			  `updatetime` datetime NOT NULL COMMENT '创建时间',
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("code", code);
		return "equipment/alarmSystem";
	}
	
	/**
	 * @Description： 跳转设备未绑定页面
	 */
	@RequestMapping(value="/histemper")
    public Object histemper(String code, Model model) {
		return "equipment/hisTemper";
	}
	
	/**
	 * @Description： 查询设备的订单信息
	 * @author： origin 
	 * @return 
	 */
	@RequestMapping(value = "/codetotrade")
	public String selectChargeRecord(HttpServletRequest request, HttpServletResponse response, Model model) {
		//用户
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) return "erroruser";
		if (user.getRank() == 6) {
			user = userService.selectUserById(user.getMerid());
		}
		//设备号
		String code = CommUtil.toString(request.getParameter("code"));
		//根据设备号查询
		UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
		Parameters parame = new Parameters();
		// 判断
		if(user.getId().equals(userEquipment.getUserId())){
			parame.setUid(user.getId());
		}else{
			Map<String, Object> map = new HashMap<>();
			map.put("partid", CommUtil.toString(user.getId()));
			Object partid = JSON.toJSON(map);
			String derf = partid.toString().substring(1, 10);
			String partner = derf + CommUtil.toString(user.getId());
			parame.setDealer(partner);
		}
		parame.setCode(code);
		//时间
		String time = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
		String startTime = StringUtil.getnumterday("yyyy-MM-dd 00:00:00", time, 7);
		parame.setStartTime(startTime);
		parame.setEndTime(StringUtil.toDateTime("yyyy-MM-dd 23:59:59"));
		// 根据参数去查询
		List<Map<String, Object>> codetotrade = tradeRecordService.seleTradeToWechat(parame);
		//遍历
		for(Map<String, Object> item : codetotrade){
			//支付方式
			Integer paysource = CommUtil.toInteger(item.get("paysource"));
			//订单号
			String ordernum = CommUtil.toString(item.get("ordernum"));
			Integer charorder = 0;
			Integer number = 0;
			if (paysource == 1) {
				//根据订单号查
				List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
				if (chargeRecord.size() > 0) {
					charorder = CommUtil.toInteger(chargeRecord.get(0).getId());
					number = CommUtil.toInteger(chargeRecord.get(0).getNumber());
				}
			}
			item.put("charorder", charorder);
			item.put("number", number);
		}
		model.addAttribute("sizenu", codetotrade.size());
		model.addAttribute("user", user);
		model.addAttribute("codeorder", codetotrade);
		return "equipment/codeOrder";
	}

	/**
	 * query equipment list by user
	 */
	@RequestMapping(value = "/list")
	public String list(Model model) {
		String source = request.getParameter("source");
		String parameter = CommUtil.trimToEmpty(request.getParameter("parameter"));
		if ("".equals(parameter)) source = null;
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			if (user.getRank() == 6) {
				user = userService.selectUserById(user.getMerid());
			}
			Integer merid = CommUtil.toInteger(user.getId());
			List<Map<String, Object>> onlineList = equipmentService.selectrelatedcode(user.getId(), (byte) 1, source, parameter, 0, 5);
			onlineList = disposeDeviceData( onlineList, merid);
			List<Map<String, Object>> offlineList = equipmentService.selectrelatedcode(user.getId(), (byte) 0, source, parameter,0, 5);
			offlineList = disposeDeviceData( offlineList, merid);
			List<Map<String, Object>> allList = equipmentService.selectrelatedcode(user.getId(), null, source, parameter, 0, 5);
			allList = disposeDeviceData( allList, merid);
			List<Map<String, Object>> codeList = equipmentService.selectrelatedcode(user.getId(), null, null, null, null, null);
			Integer onlines = 0;
			Integer disline = 0;
			if (codeList != null) {
				for (Map<String, Object> item : codeList) {
					Integer line = CommUtil.toInteger(item.get("state"));
					if(line==1) onlines += 1;
					if(line==0) disline += 1;
				}
			}
			DealerAuthority jurisdiction = authorityService.selectMessSwitch(user.getId());
			model.addAttribute("showincoins", CommUtil.toInteger(jurisdiction.getShowincoins()));
			model.addAttribute("source", source);
			model.addAttribute("parameter", parameter);
			model.addAttribute("onlineList", onlineList);
			model.addAttribute("offlineList", offlineList);
			model.addAttribute("allList", allList);
			model.addAttribute("onNum", onlines);
			model.addAttribute("offNum", disline);
			model.addAttribute("allNum", onlines+disline);
			model.addAttribute("wolfparam", request.getParameter("wolfparam"));
			model.addAttribute("uid", user.getId());
		}
		return "equipment/equipmentlist01";
		// return "equipment/equipmentlist";
	}
	

	public static List<Map<String, Object>> disposeDeviceData(List<Map<String, Object>> list, Integer merid) {
		try {
			if (list != null && list.size() > 0) {
				for (Map<String, Object> item : list) {
					int usenum = 0;
					int freenum = 0;
					int failnum = 0;
					String code = CommUtil.toString(item.get("code"));
					Integer state = CommUtil.toInteger(item.get("state"));
					String hardversion = CommUtil.toString(item.get("hardversion"));
					Integer dealid = CommUtil.toInteger(item.get("dealid"));
					item.put("totalOnlineEarn", CommUtil.toDouble(item.get("total_online_earn")));
					item.put("nowOnlineEarn", CommUtil.toDouble(item.get("now_online_earn")));
					item.put("totalCoinsEarn", CommUtil.toDouble(item.get("total_coins_earn")));
					item.put("nowCoinsEarn", CommUtil.toDouble(item.get("now_coins_earn")));
					item.put("device_type", CommUtil.toInteger(item.get("device_type")));
					if(merid.equals(dealid)){
						item.put("classify", 1);
					}else{
						item.put("classify", 2);
					}
					if (state == 1) {
						if (!"03".equals(hardversion) && !"04".equals(hardversion)) {
							List<Integer> stautsList = DisposeUtil.getPortStatusInt(code, hardversion);
							if (stautsList != null && stautsList.size() > 0) {
								for (Integer status : stautsList) {
									if (status == 1) {
										freenum += 1;
									} else if (status == 2) {
										usenum += 1;
									} else {
										failnum += 1;
									}
								}
							}
							item.put("usenum", usenum);
							item.put("freenum", freenum);
							item.put("failnum", failnum);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping(value = "/getEquipAidInfo")
	@ResponseBody
	public Map<String, Object> getEquipAidInfo( String codenum) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			if(user==null) return CommUtil.responseBuildInfo(901, "缓存失效", datamap);
			Equipment deviceinfo = equipmentService.getEquipmentById(codenum);
			Integer aid = CommUtil.toInteger(deviceinfo.getAid());
			Integer dealid = CommUtil.toInteger(user.getId());
			if (user.getRank() == 6) {
				dealid = CommUtil.toInteger(user.getMerid());
			}
			List<Map<String, Object>> areainfo = areaService.inquireDealAreaData(dealid);
			datamap.put("nowcodeaid", aid);
			datamap.put("arealist", areainfo);
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	@RequestMapping(value = "/updataEquipAidInfo")
	@ResponseBody
	public Map<String, Object> updataEquipAidInfo(Integer aid, String codenum) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			if(user==null) return CommUtil.responseBuildInfo(901, "缓存失效", datamap);
			Equipment deviceinfo = equipmentService.getEquipmentById(codenum);
			Integer aidnow = CommUtil.toInteger(deviceinfo.getAid());
			Integer dealid = CommUtil.toInteger(user.getId());
			Equipment equipment = new Equipment();
			equipment.setCode(codenum);
			equipment.setAid(aid);
			equipmentService.updateEquipment(equipment);
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	/**
	 * @Description： 根据设备号与商户id查询投币总额
	 * @author： origin
	 * @return 返回double类型的投币总额
	 */
	public double coinsmoney(Integer uid, Equipment equipment, String startTime, String endTime) {
		double coinsmoney = 0.00;
		if(!equipment.getHardversion().equals("04")){
			Map<String, Object> suminfo = inCoinsService.selectcodesuminfo(uid, equipment.getCode(), startTime, endTime);
			if (suminfo != null) {
				coinsmoney = StringUtil.togetBigDecimal(suminfo.get("incoinsmoney")).doubleValue();
				// Object incoins = suminfo.get("incoins");
				// equipment.setCoinsMoney(coinsmoney);
			}
		}
		return coinsmoney;
	}

	@RequestMapping(value = "/getAjaxEquList")
	@ResponseBody
	public Map<String, Object> getAjaxEquList(Integer uid, String source, String parameter, Integer equnum,
			Integer querynum) {
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> equlist = new ArrayList<>();
		parameter = CommUtil.trimToEmpty(parameter);
		if ("".equals(parameter)) source = null;
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			if (user.getRank() == 6) {
				user = userService.selectUserById(user.getMerid());
			}
		}
		if (querynum == 1) {
			equlist = equipmentService.selectrelatedcode(uid, (byte) 1, source, parameter, equnum, 5);
		} else if (querynum == 2) {
			equlist = equipmentService.selectrelatedcode(uid, (byte) 0, source, parameter, equnum, 5);
		} else if (querynum == 3) {
			equlist = equipmentService.selectrelatedcode(uid, null, source, parameter, equnum, 5);
		}
		Integer merid = CommUtil.toInteger(user.getId());
		equlist = disposeDeviceData( equlist, merid);
		map.put("equnum", equnum + 5);
		map.put("equlist", equlist);
		map.put("listnum", equlist.size());
		return map;
	}
	
	@RequestMapping(value = "/redactEquipentName")
	@ResponseBody
	public Map<String, Object> redactEquipentName(String code,String name) {
		try {
			Integer equnum = equipmentService.updateEquipmentRemark( name, code);
			if(equnum==1){
				return CommUtil.responseBuild(200, "修改成功", "");
			}else{
				return CommUtil.responseBuild(501, "修改失败", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuild(301, "异常错误", "");
		}
	}
	
	
	@RequestMapping(value = "/useronbound")
	@ResponseBody
	public String useronbound(@RequestParam("code") String code, Model model) {
		UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
		userEquipmentService.deleteUserEquipmentByEquipmentCode(code);
		Equipment equipment = equipmentService.getEquipmentById(code);
		equipmentService.codeCollectmoney(code, userEquipment.getUserId(), equipment.getAid(), equipment.getHardversion());
		equipment.setBindtype((byte) 0);
		equipment.setSeveral(0);
		equipment.setAid(0);
		equipment.setTotalOnlineEarn(0.0);
		equipment.setNowOnlineEarn(0.0);
		equipment.setTotalCoinsEarn(0);
		equipment.setNowCoinsEarn(0);
		if ("03".equals(equipment.getHardversion())) {
			List<TemplateParent> templateList = templateService.getParentTemplateListByMerchantid(0, 2);
			equipment.setTempid(templateList.get(0).getId());
		} else if ("04".equals(equipment.getHardversion())) {
			List<TemplateParent> templateList = templateService.getParentTemplateListByMerchantid(0, 1);
			equipment.setTempid(templateList.get(0).getId());
		} else if ("02".equals(equipment.getHardversion())) {
			List<TemplateParent> templateList = templateService.getParentTemplateListByMerchantidwolf(0, 0);
			equipment.setTempid(templateList.get(0).getId());
		} else {
			equipment.setTempid(0);
		}
		equipmentService.updateEquipment(equipment);
		equipmentService.updateEquipmentRemark(null, code);
		equipmentService.insertCodeoperatelog(code, 2, 2, userEquipment.getUserId(), userEquipment.getUserId(), null);
		return "1";
	}

	public Map<String, Object> tempDefaultObje( List<TemplateSon> tempson) {
		Map<String, Object> map = new HashMap<String, Object>();
		int temp1 = 0;
		for (TemplateSon templateSon : tempson) {
			if (temp1 == 0 && !templateSon.getName().contains("1元")) {
				map.put("defaultchoose", templateSon.getId());
				map.put("defaultindex", temp1);
				break;
			} else if (temp1 == 1) {
				map.put("defaultchoose", templateSon.getId());
				map.put("defaultindex", temp1);
				break;
			}
			temp1++;
		}
		return map;
	}
	
	/**
	 * @Description： v3选择默认
	 * @param tempson
	 * @author： origin 2020年7月6日下午6:45:07
	 */
	public Map<String, Object> tempDefault( List<TemplateSon> tempson) {
		Map<String, Object> map = new HashMap<String, Object>();
		int temp1 = 0;
		for (TemplateSon templateSon : tempson) {
			if (templateSon.getName().contains("2元")) {
				map.put("defaultchoose", templateSon.getId());
				map.put("defaultindex", temp1);
				break;
			}
			temp1++;
		}
		if(map.get("defaultchoose")==null){
			map.put("defaultchoose", tempson.get(0).getId());
			map.put("defaultindex", 0);
		}
		return map;
	}
	
	public void temp(String equcode, TemplateParent temp, Model model) {
		List<TemplateSon> templatelist = null;
		if(temp!=null){
			templatelist = templateService.getSonTemplateLists(temp.getId());
		}else{
			templatelist = templateService.getEquSonTem(equcode);
		}
		//List<TemplateSon> templatelist = templateService.getEquSonTem(equcode);
		if (templatelist == null || templatelist.size() == 0) {
			templatelist = templateService.getSonTemp(0);
		}
//		int temp1 = 0;
//		for (TemplateSon templateSon : templatelist) {
//			if (templateSon.getName().contains("2元")) {
//				model.addAttribute("defaultchoose", templateSon.getId());
//				model.addAttribute("defaultindex", temp1);
//				break;
//			}
//			temp1++;
//		}
		int temp1 = 0;
		for (TemplateSon templateSon : templatelist) {
			if (temp1 == 0 && !templateSon.getName().contains("1元")) {
				model.addAttribute("defaultchoose", templateSon.getId());
				model.addAttribute("defaultindex", temp1);
				break;
			} else if (temp1 == 1) {
				model.addAttribute("defaultchoose", templateSon.getId());
				model.addAttribute("defaultindex", temp1);
				break;
			}
			temp1++;
		}
		model.addAttribute("templatelist", templatelist);
	}
	/**
	 * 用户公众外部扫描端口进行充电
	 * @param codeAndPort  设备端口号          
	 * @param model
	 * @return {@link String}
	 */
	@RequestMapping(value = "/chargePort")
	public String chargePort(@RequestParam(value = "codeAndPort", defaultValue = "000001") String codeAndPort, Model model) {
		try {
			//微信code信息
			String code = CommUtil.toString(request.getParameter("code"));
			String existuser = CommUtil.toString(request.getParameter("existuser"));
			int length = codeAndPort.length();
			if (length == 7 || length == 8) {
				String val = codeAndPort.substring(6);
				int port = Integer.parseInt(val);
				if (length == 8 && port > 20) {
					model.addAttribute("errorinfo", "A二维码有误");
					return "chargeporterror";
				}
			} else {
				model.addAttribute("errorinfo", "B二维码有误");
				return "chargeporterror";
			}
			String equcode = codeAndPort.substring(0, 6);
			String port = codeAndPort.substring(6);
			// 根据设备号判断商户是否为特约商户
			boolean subMer = userEquipmentService.subMerByCode(equcode);
			logger.info("是不是特约商户"+"======="+subMer);
			JSONObject doGetStr = null;
			String openid = null;
			String accesstoken = null;
			User touristuser = null;
			if(subMer){
				String subOpenid = CommUtil.toString(request.getParameter("subOpenid"));
			    if(subOpenid == null){
			    	logger.info("1-使用服务商设备端口==="+codeAndPort+"支付参数=="+subOpenid);
			    	// 扫描服务商下的商家设备,就获取用户在服务商下的openid
			    	doGetStr = WeixinUtil.WeChatTokenSubMer(code, equcode);
			    	if(!doGetStr.has("openid")){
						model.addAttribute("openiderror", "1您的账户暂时无法访问");
						return "openiderror";
					}
					openid = doGetStr.getString("openid");
					accesstoken = doGetStr.getString("access_token");
					// 根据服务商的openid查询用户
					touristuser = userService.selectSubUserByOpenid(openid);
					logger.info("服务商下存在用户==="+(touristuser != null)+"设备信息=="+codeAndPort+"支付参数=="+openid);
					// 服务商用户表中没有用户信息,就重定向准备添加服务商下用户的openid
					if(touristuser == null){
						logger.info("开始重定向=="+codeAndPort+"subOpenid=="+openid);
						String url = "/oauth2PortPaySubUser?codeAndPort="+codeAndPort+"&subOpenid="+openid;
						return "redirect:"+url;
					}
					model.addAttribute("userinfo", touristuser);
					model.addAttribute("openid", openid);
			    }
			    // 重定向携带服务商下openid回来
			    if(subOpenid != null){
					// 对平台appid授权
			    	logger.info("2-重定向后====用户在服务商下openid=="+subOpenid);
					JSONObject heTengUserOpenid  = WeixinUtil.WeChatToken(code);
					if(!heTengUserOpenid.has("openid")){
						model.addAttribute("openiderror", "2您的账户暂时无法访问");
						return "openiderror";
					}
					// 根据平台appid获取平台用户的信息
					User user2 = userService.getUserByOpenid(heTengUserOpenid.getString("openid"));
					if(user2 == null){
						model.addAttribute("openiderror", "3请先关注公众号");
						return "openiderror";
					}
					// openid属于服务商openid
					openid = subOpenid;
					// 将用户的信息和服务商的openid进行存储
					User fuWuUser = new User();
					fuWuUser.setId(user2.getId());
					fuWuUser.setOpenid(subOpenid);
					userService.addSubUser(fuWuUser);
					// 查询服务商下的用户
					touristuser = userService.selectSubUserByOpenid(openid);
					if(touristuser == null){
						model.addAttribute("openiderror", "4您的账户暂时无法访问");
						return "openiderror";
					}
					logger.info("服务商返回前端数据==="+touristuser.getId()+"支付参数=="+openid);
					model.addAttribute("userinfo", touristuser);
					model.addAttribute("openid", openid);
				}
			}else{
				// 对平台appid授权
				doGetStr = WeixinUtil.WeChatToken(code);
				// 判断设备包含不包含openid
				if (!doGetStr.has("openid")) {
	                model.addAttribute("name", "'自助充电平台'");
					model.addAttribute("openiderror", "5您的账户暂时无法访问");
					return "openiderror";
				}
				openid = doGetStr.getString("openid");
				accesstoken = doGetStr.getString("access_token");
				try {
					// 根据openid和accesstoken获取用户是否关注的信息
					String getUserInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="
							+ WeixinUtil.getBasicAccessToken() + "&openid=" + openid + "&lang=zh_CN";
					// 将get请求返回的信息，转换成JSON对象
					JSONObject jsonObject = WeixinUtil.doGetStr(getUserInfoUrl);
					// 判段用户的是否关注:0为未关注，1为已关注
					if ("0".equals(jsonObject.getString("subscribe"))) {// 如果没有关注跳转到一个关注的页面
						return "attention";
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("判断用户是否关注公众号失败===" + e.getMessage());
				}
				checkUserAndDispose(openid, accesstoken, equcode);
				touristuser = userService.getUserByOpenid(openid);
				if(touristuser == null){
					model.addAttribute("openiderror", "6您的账户暂时无法访问");
					return "openiderror";
				}
				model.addAttribute("userinfo", touristuser);
				model.addAttribute("openid", openid);
			}
//			String openid = "odVHPwtil1qLhywcJ1fQ0NXCftb8";
			model.addAttribute("code", equcode);
			
			// 根据code查询出相应的设备信息
			Equipment equipment = equipmentService.getEquipmentAndAreaById(equcode);
			equipment = equipment == null ? new Equipment() : equipment;
			//设备信息
			String devicename = CommUtil.toString(equipment.getRemark());
			Integer devicestate = CommUtil.toInteger(equipment.getState());
			Integer devicebindtype = CommUtil.toInteger(equipment.getBindtype());
			String hardversion = CommUtil.toString(equipment.getHardversion());
			Integer tempid = CommUtil.toInteger(equipment.getTempid());
			Integer deviceareaid = CommUtil.toInteger(equipment.getAid());
			Date expirationTime= equipment.getExpirationTime();
			// 根据设备关联小区查询相关小区信息
			Area area = areaService.selectByIdArea(deviceareaid);
			area = area == null ? new Area() : area;
			Integer areaid = CommUtil.toInteger(area.getId());
			String areaname = CommUtil.toString(area.getName());
			
			//绑定商户
			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(equcode);
			userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
			Integer dealid = CommUtil.toInteger(userEquipment.getUserId());
			//绑定商户信息
			User dealeruser = new User();
			if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
			String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
			String servephone = CommUtil.toString(dealeruser.getServephone());
			//扫码用户信息
			//User touristuser = userService.getUserByOpenid(openid);
			touristuser = touristuser == null ? new User() : touristuser;
			Integer touristid = CommUtil.toInteger(touristuser.getId());
			Integer mercid = CommUtil.toInteger(touristuser.getMerid());
			Double touristbalance = CommUtil.toDouble(touristuser.getBalance());
			Double touristsendmoney = CommUtil.toDouble(touristuser.getSendmoney());
			
			//判断获取模板信息
			Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, dealid, equcode, hardversion);
			String brandname = CommUtil.toString(DirectTemp.get("remark"));
			String temphone = CommUtil.toString(DirectTemp.get("common1"));
			servephone = basicsService.getServephoneData(temphone, null, servephone, dealerphne);
			model.addAttribute("phonenum", servephone);
			
			Integer nowtempid = CommUtil.toInteger(DirectTemp.get("id"));
			Integer temporaryc = CommUtil.toInteger(DirectTemp.get("permit"));
			String chargeInfo = CommUtil.toString(DirectTemp.get("chargeInfo"));
			Integer walletpay = CommUtil.toInteger(DirectTemp.get("walletpay"));
			List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
			tempson = tempson == null ? new ArrayList<>() : tempson;
			//判断获取模板信息
//			String tempphone = null;
//			TemplateParent temp = templateService.getParentTemplateOne(tempid);
			if ("08".equals(hardversion) || "09".equals(hardversion) || "10".equals(hardversion)) {
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
				model.addAttribute("temporaryc", walletpay);
				model.addAttribute("tempower", tempower);
				model.addAttribute("temtime", temtime);
				model.addAttribute("temmoney", temmoney);
				Map<String, Object> defaulte = tempDefault(temmoney);
				model.addAttribute("defaultchoose", defaulte.get("defaultchoose"));
				model.addAttribute("defaultindex", defaulte.get("defaultindex"));
			}else{
				Map<String, Object> defaulte = tempDefaultObje(tempson);
				model.addAttribute("defaultchoose", defaulte.get("defaultchoose"));
				model.addAttribute("defaultindex", defaulte.get("defaultindex"));
			}
				
			
			model.addAttribute("existuser", existuser);
			model.addAttribute("equipment", equipment);
			model.addAttribute("area", area);
			//model.addAttribute("userinfo", touristuser);

			model.addAttribute("code", equcode);
			model.addAttribute("port", port);
			model.addAttribute("hardversion", hardversion);
			model.addAttribute("equname", devicename);
			model.addAttribute("areaname", areaname);
			
			model.addAttribute("chargeInfo", chargeInfo);
			model.addAttribute("brandname", brandname);
			model.addAttribute("ifwallet", walletpay);
			model.addAttribute("DirectTemp", DirectTemp);

			Integer iswallet = 1;
			if(!dealid.equals(mercid)) iswallet = 2;
			model.addAttribute("iswallet", iswallet);
			model.addAttribute("balance", touristbalance);
			model.addAttribute("sendmoney", touristsendmoney);
			//设备到期时间不为空且当前时间大于到期时间
			if(expirationTime != null && new Date().after(expirationTime)){
				model.addAttribute("errorinfo", "对不起，当前设备已到期");
				model.addAttribute("time", expirationTime);
				//提示用户设备到期更换设备
				return "chargeporterror";
			}
//			if (!"01".equals(hardversion) && !"05".equals(hardversion) && !"06".equals(hardversion)
//					&& !"00".equals(hardversion) && !"02".equals(hardversion) && !"07".equals(hardversion)) {
//				model.addAttribute("errorinfo", "当前充电站不支持扫描端口充电");
//				return "chargeporterror";
//			}
			if ("03".equals(hardversion) || "04".equals(hardversion) ) {
				model.addAttribute("errorinfo", "当前充电站不支持扫描端口充电");
				return "chargeporterror";
			}
			if (devicestate.equals(0)) {
				return "equipment/equipmentoffline";
			} else if (devicebindtype.equals(0)) {
//				temp(equcode, null, model);
				return "equipment/equipmentunbind";
			} else if (dealid.equals(0)) {
//				temp(equcode, null, model);
				return "equipment/equipmentunbind";
			} else {
				List<String> forbidport = new ArrayList<String>();
//				temp = getTempInfo(temp, touristid, dealid);
				forbidport = getAssignPotr(touristid, equipment.getCode(),
						userEquipment.getUserId());
				model.addAttribute("forbidport", JSON.toJSONString(forbidport));
				String checkMonthBackVal = checkMonthBackVal(dealid);
				if (!"2".equals(checkMonthBackVal)) {
					PackageMonth packageMonth = userService.selectPackageMonthByUid(touristid);
					model.addAttribute("packageMonth", packageMonth);
				} else {
					model.addAttribute("packageMonth", null);
				}
//						int portval = Integer.parseInt(port);
//						AllPortStatus portStatus = allPortStatusService.findPortStatusByEquipmentnumAndPort(equcode,
//								portval);
//						if (portStatus != null) {
//							long time = portStatus.getUpdateTime().getTime();
//							long currentTime = System.currentTimeMillis();
//							model.addAttribute("portStatus", portStatus.getPortStatus());
//							if ((currentTime - time) > 300000) {
//								model.addAttribute("flag", true);
//							} else {
//								model.addAttribute("flag", false);
//							}
//						} else {
//							model.addAttribute("portStatus", 0);
//							model.addAttribute("flag", true);
//						}
				Map<String, String> codeRedisMap = JedisUtils.hgetAll(equcode);
				if (codeRedisMap != null) {
					String portMapStr = codeRedisMap.get(port);
					Map<String,String> portMap = (Map<String, String>) JSON.parse(portMapStr);
					if (portMap != null) {
						String updateTimeStr = portMap.get("updateTime");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						long time = sdf.parse(updateTimeStr).getTime();
						long currentTime = System.currentTimeMillis();
						model.addAttribute("portStatus", portMap.get("portStatus"));
						if ((currentTime - time) > 300000) {
							model.addAttribute("flag", true);
						} else {
							model.addAttribute("flag", false);
						}
					} else {
						model.addAttribute("portStatus", 0);
						model.addAttribute("flag", true);
					}
				} else {
					model.addAttribute("portStatus", 0);
					model.addAttribute("flag", true);
				}
				model.addAttribute("ifmonth", checkMonthBackVal);
				model.addAttribute("templatelist", tempson);
//				temp(equcode, temp, model);
				model.addAttribute("nowtime", System.currentTimeMillis() + "");
				checkUserBindAndDispose(openid, equcode);
				model.addAttribute("wolfstatus", "1");
//				return "chargeport1";
				if(DisposeUtil.checkIfHasV3(hardversion)){
					return "chargeportV3";
				}else{
					return "chargeport1";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorinfo", "扫码失败，请重新扫码" + e.getMessage());
			return "chargeporterror";
		}
	}

	/**
	 * 附近电站端口进行充电(公众号内部)
	 * @param codeAndPort  设备端口号          
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/nearbyChargePort")
	public String nearbyChargePort(@RequestParam(value = "codeAndPort", defaultValue = "000001") String codeAndPort,
			Model model) {
		try {
			int length = codeAndPort.length();
			if (length == 7 || length == 8) {
				String val = codeAndPort.substring(6);
				int port = Integer.parseInt(val);
				if (length == 8 && port > 20) {
					model.addAttribute("errorinfo", "端口信息有误");
					return "chargeporterror";
				}
			} else {
				model.addAttribute("errorinfo", "端口信息有误");
				return "chargeporterror";
			}
			String equcode = codeAndPort.substring(0, 6);
			String port = codeAndPort.substring(6);
			model.addAttribute("port", port);
			model.addAttribute("code", equcode);
			String existuser = CommUtil.toString(request.getParameter("existuser"));
			// 根据code查询出相应的设备信息
			Equipment equipment = equipmentService.getEquipmentAndAreaById(equcode);
			equipment = equipment == null ? new Equipment() : equipment;
			//设备信息
			String devicename = CommUtil.toString(equipment.getRemark());
			Integer devicestate = CommUtil.toInteger(equipment.getState());
			Integer devicebindtype = CommUtil.toInteger(equipment.getBindtype());
			String hardversion = CommUtil.toString(equipment.getHardversion());
			Integer tempid = CommUtil.toInteger(equipment.getTempid());
			Integer deviceareaid = CommUtil.toInteger(equipment.getAid());
		
			Date expirationTime= equipment.getExpirationTime();
			//设备到期时间不为空且当前时间大于到期时间
			if(expirationTime != null && new Date().after(expirationTime)){
				model.addAttribute("errorinfo", "对不起，当前设备已到期");
				model.addAttribute("code", equcode);
				model.addAttribute("time", expirationTime);
				//提示用户设备到期更换设备
				return "chargeporterror";
			}
			boolean subMer = userEquipmentService.subMerByCode(equcode);
			logger.info("是不是特约商户"+"======="+subMer);
			String openid = null;
			User touristuser = null;
			// 为true表示特约商户
			if(subMer){
				// 微信code
			    String wxCode = CommUtil.toString(request.getParameter("code"));
				// 对服务商的appid授权
				logger.info("1-使用服务商下的商户设备======="+equcode);
			    if(wxCode == null){
			    	logger.info("用户重定向前的数据==="+codeAndPort+"===微信数据"+wxCode);
			    	String url = "/insideNearbyChargePort?codeAndPort="+codeAndPort;
			    	return "redirect:"+url;
			    }
			    if(wxCode != null){
			    	logger.info("2-用户重定向后数据======="+codeAndPort);
					JSONObject doGetStr = WeixinUtil.WeChatTokenSubMer(wxCode, equcode);
					if(!doGetStr.has("openid")){
						model.addAttribute("openiderror", "1您的账户暂时无法访问");
						return "openiderror";
					}
					openid = doGetStr.getString("openid");
					// 根据服务商的openid查询用户
					User subUser = userService.selectSubUserByOpenid(openid);
					// 用户存在session中
					User user = (User) request.getSession().getAttribute("user");
					if(subUser == null && user != null){
						User user2 = new User();
						user2.setId(user.getId());
						user2.setOpenid(openid);
						userService.addSubUser(user2);
					}
					// 添加成功,根据重定向后的服务商openid查询用户
					touristuser = userService.selectSubUserByOpenid(openid);
					if(touristuser == null){
						model.addAttribute("openiderror", "2您的账户暂时无法访问");
						return "openiderror";
					}
					model.addAttribute("userinfo", touristuser);
					model.addAttribute("openid", openid);
				}
			}else{
				//获取用户信息
				User userinfo = (User) request.getSession().getAttribute("user");
				openid = CommUtil.toString(userinfo.getOpenid());
				touristuser = userService.getUserByOpenid(openid);
				String accesstoken = "";
				model.addAttribute("openid", openid);
				checkUserAndDispose(openid, accesstoken, equcode);
				model.addAttribute("userinfo", userinfo);
			}
			// 根据设备信息查询区域信息
			Area area = areaService.selectByIdArea(deviceareaid);
			area = area == null ? new Area() : area;
			Integer areaid = CommUtil.toInteger(area.getId());
			String areaname = CommUtil.toString(area.getName());
			model.addAttribute("equipment", equipment);
			model.addAttribute("area", area);
			model.addAttribute("hardversion", hardversion);
			model.addAttribute("equname", devicename);
			model.addAttribute("areaname", areaname);
			model.addAttribute("equipment", equipment);
			model.addAttribute("area", area);	
			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(equcode);
			userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
			Integer dealid = CommUtil.toInteger(userEquipment.getUserId());
			User dealeruser = new User();
			if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
			String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
			String servephone = CommUtil.toString(dealeruser.getServephone());
			String dealservephone = servephone == null ? dealerphne : servephone;
			
			String tempphone = null;
			TemplateParent temp = templateService.getParentTemplateOne(tempid);
			Integer walletpay = CommUtil.toInteger(temp.getWalletpay());
			if ("08".equals(hardversion) || "09".equals(hardversion) || "10".equals(hardversion)) {
				temp = temp == null ? new TemplateParent() : temp;
				if(CommUtil.toInteger(temp.getId()).equals(0)) {
					List<TemplateParent>  tempdata = templateService.inquireTempByStatus(0 , 6);
					if(tempdata.size()>0) temp = tempdata.get(0);
				}
				Integer nowtempid = CommUtil.toInteger(temp.getId());
				Integer temporaryc = CommUtil.toInteger(temp.getPermit());
				List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
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
				model.addAttribute("temporaryc", walletpay);
				model.addAttribute("tempower", tempower);
				model.addAttribute("temtime", temtime);
				model.addAttribute("temmoney", temmoney);
			}else{
				temp = temp == null ? new TemplateParent() : temp;
				if(CommUtil.toInteger(temp.getId()).equals(0)) {
					List<TemplateParent>  tempdata = templateService.inquireTempByStatus(0 , 0);
					if(tempdata.size()>0) temp = tempdata.get(0);
				}
			}
			temp = temp == null ? new TemplateParent() : temp;
			tempphone = tempphone == null ? dealservephone : tempphone;
			String chargeInfo = CommUtil.toString(temp.getChargeInfo());
			String brandname = CommUtil.toString(temp.getRemark());
				
			model.addAttribute("chargeInfo", chargeInfo);
			model.addAttribute("brandname", brandname);
			model.addAttribute("ifwallet", walletpay);
			model.addAttribute("phonenum", tempphone);
			
			model.addAttribute("phonenum", tempphone);
			if (!"01".equals(hardversion) && !"05".equals(hardversion) && !"06".equals(hardversion)
					&& !"00".equals(hardversion) && !"02".equals(hardversion)) {
				model.addAttribute("errorinfo", "当前充电站不支持附近端口充电");
				return "chargeporterror";
			}
			touristuser = touristuser == null ? new User() : touristuser;
			Integer touristid = CommUtil.toInteger(touristuser.getId());
			Integer mercid = CommUtil.toInteger(touristuser.getMerid());
			Double touristbalance = CommUtil.toDouble(touristuser.getBalance());
			Double touristsendmoney = CommUtil.toDouble(touristuser.getSendmoney());
			Integer iswallet = 1;
			if(!dealid.equals(mercid)) iswallet = 2;

			model.addAttribute("iswallet", iswallet);
			model.addAttribute("balance", touristbalance);
			model.addAttribute("sendmoney", touristsendmoney);
			model.addAttribute("hardversion", hardversion);
			model.addAttribute("existuser", existuser);
			
//			String accesstoken = "";
			model.addAttribute("code", equcode);
//			model.addAttribute("openid", openid);
//			checkUserAndDispose(openid, accesstoken, equcode);
//			model.addAttribute("userinfo", userinfo);
			if (devicestate.equals(0)) {
				return "equipment/equipmentoffline";
			} else if (devicebindtype.equals(0)) {
				temp(equcode, null, model);
				return "equipment/equipmentunbind";
			} else if (dealid.equals(0)) {
				temp(equcode, null, model);
				return "equipment/equipmentunbind";
			} else {
				List<String> forbidport = new ArrayList<String>();
				temp = getTempInfo(temp,touristid, dealid);
				forbidport = getAssignPotr(touristid, equcode, dealid);
				model.addAttribute("forbidport", JSON.toJSONString(forbidport));
				
//				temp = temp == null ? new TemplateParent() : temp;
//				String chargeInfo = CommUtil.toString(temp.getChargeInfo());
//				String brandname = CommUtil.toString(temp.getRemark());
//				Integer walletpay = CommUtil.toInteger(temp.getWalletpay());
//					
//				model.addAttribute("chargeInfo", chargeInfo);
//				model.addAttribute("brandname", brandname);
//				model.addAttribute("ifwallet", walletpay);
//				model.addAttribute("phonenum", tempphone);
				
				String checkMonthBackVal = checkMonthBackVal(dealid);
				if (!"2".equals(checkMonthBackVal)) {
					PackageMonth packageMonth = userService.selectPackageMonthByUid(touristid);
					model.addAttribute("packageMonth", packageMonth);
				} else {
					model.addAttribute("packageMonth", null);
				}
				Map<String, String> codeRedisMap = JedisUtils.hgetAll(equcode);
				if (codeRedisMap != null) {
					String portMapStr = codeRedisMap.get(port);
					Map<String,String> portMap = (Map<String, String>) JSON.parse(portMapStr);
//						int portval = Integer.parseInt(port);
//						AllPortStatus portStatus = allPortStatusService.findPortStatusByEquipmentnumAndPort(equcode,
//								portval);
					if (portMap != null) {
						String updateTimeStr = portMap.get("updateTime");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						long time = sdf.parse(updateTimeStr).getTime();
//							long time = portStatus.getUpdateTime().getTime();
						long currentTime = System.currentTimeMillis();
						model.addAttribute("portStatus", portMap.get("portStatus"));
						if ((currentTime - time) > 300000) {
							model.addAttribute("flag", true);
						} else {
							model.addAttribute("flag", false);
						}
					} else {
						model.addAttribute("portStatus", 0);
						model.addAttribute("flag", true);
					}
				}
				model.addAttribute("ifmonth", checkMonthBackVal);
				temp(equcode, temp, model);
				model.addAttribute("nowtime", System.currentTimeMillis() + "");
				// 根据opeind更新用户所属商家的信息
				checkUserBindAndDispose(openid, equcode);
				model.addAttribute("wolfstatus", "1");
				return "chargeport1";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorinfo", "失败，请重选择");
			return "chargeporterror";
		}
	}
	
	/**
	 * 引导用户关注公共号 暂时没有用
	 * 
	 * @param code
	 *            设备号
	 * @return
	 */
	@RequestMapping(value = "/whetherAttention")
	public String whetherAttention(@RequestParam("equcode") String equcode) {
		// 用户扫描设备编号
		String code = request.getParameter(equcode);
		// 通过code换取网页授权access_token
		try {
			JSONObject jsonObject = WeixinUtil.WeChatToken(code);
			String openid = jsonObject.getString("openid");
			String accesstoken = jsonObject.getString("access_token");
			if (openid != null && accesstoken != null) {
				// 根据openid和accesstoken获取用户是否关注的信息
				String getUserInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accesstoken
						+ "&openid=" + openid + "&lang=zh_CN";
				// 将get请求返回的信息，转换成JSON对象
				JSONObject doGetStr1 = WeixinUtil.doGetStr(getUserInfoUrl);
				// 判段用户的是否关注:0为未关注，1为已关注
				if ("0".equals(doGetStr1.getString("subscribe"))) {
					return "去跳转到前端，引导用户关注公共号二维码";
				}
			} else {
				return "跳转到前端，提示您的账户暂时无法访问";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "跳转到温哥的接口进行扫码充电";
	}
	
	/**
	 * 用户公众号外部扫设备号充电
	 * @param equcode 设备号            
	 * @param model
	 * @return {@link String}
	 * @throws Exception
	 */
	@RequestMapping(value = "/allChargePort")
	public String allChargePort(@RequestParam("equcode") String equcode, Model model) throws Exception {
		try {
			if(equcode == null || "".equals(equcode)){
				model.addAttribute("openiderror", "扫描的设备号错误");
				return "openiderror";
			}
			//微信code信息
			String code = CommUtil.toString(request.getParameter("code"));
			String existuser = CommUtil.toString(request.getParameter("existuser"));
			// 获取设备号
			String devicenum = CommUtil.toString(equcode);
			// 根据code查询出相应的设备信息
			Equipment equipment = equipmentService.getEquipmentAndAreaById(devicenum);
			equipment = equipment == null ? new Equipment() : equipment;
			
			//设备信息
			String devicename = CommUtil.toString(equipment.getRemark());
			Integer devicestate = CommUtil.toInteger(equipment.getState());
			Integer devicebindtype = CommUtil.toInteger(equipment.getBindtype());
			
			String hardversion = CommUtil.toString(equipment.getHardversion());
			String softversionnum = CommUtil.toString(equipment.getSoftversionnum());
			Integer tempid = CommUtil.toInteger(equipment.getTempid());
			Integer deviceareaid = CommUtil.toInteger(equipment.getAid());
			Integer several = CommUtil.toInteger(equipment.getSeveral());
			Date expirationTime= equipment.getExpirationTime();
			// 获取设备版本信息
			String equi = CommUtil.toString(equipment.getHardversion());
			// 根据设备信息查询区域信息
			Area area = areaService.selectByIdArea(deviceareaid);
			area = area == null ? new Area() : area;
			Integer areaid = CommUtil.toInteger(area.getId());
			String areaname = CommUtil.toString(area.getName());
			
			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(devicenum);
			userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
			Integer dealid = CommUtil.toInteger(userEquipment.getUserId());
			User dealeruser = new User();
			if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
			String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
			String servephone = CommUtil.toString(dealeruser.getServephone());
			
			model.addAttribute("equipment", equipment);
			model.addAttribute("area", area);
			// equipmentService.slelectPortInfo(equipment.getCode(),);
			model.addAttribute("several", several);
			model.addAttribute("hardversion", hardversion);
			// 根据设备号判断是特约商户还是普通用户
			boolean subMer = userEquipmentService.subMerByCode(equcode);
			logger.info("是不是特约商户"+"======="+subMer);
			JSONObject doGetStr = null;
			String openid = null;
			String accesstoken = null;
			User touristuser = null;
			// 为true表示特约商户
			if(subMer){
				String subMerCode = new StringBuffer(equcode).substring(0,6);
				String subOpenid = CommUtil.toString(request.getParameter("subOpenid"));
				// 对服务商的appid授权
				logger.info("1-使用服务商设备==="+equcode+"支付参数=="+subOpenid);
			    if(subOpenid == null){
			    	doGetStr = WeixinUtil.WeChatTokenSubMer(code, subMerCode);
			    	if(!doGetStr.has("openid")){
						model.addAttribute("openiderror", "1您的账户暂时无法访问");
						return "openiderror";
					}
					openid = doGetStr.getString("openid");
					accesstoken = doGetStr.getString("access_token");
					// 根据服务商的openid查询用户
					touristuser = userService.selectSubUserByOpenid(openid);
					logger.info("服务商下存在用户==="+(touristuser != null)+"设备=="+devicenum+"支付参数=="+openid);
					if(touristuser == null){
						logger.info("开始重定向=="+devicenum+"subOpenid=="+openid);
						String url ="/oauth2PaySubUser?code="+devicenum+"&subOpenid="+openid;
						return "redirect:"+url;
					}else{
						model.addAttribute("userinfo", touristuser);
						model.addAttribute("openid", openid);
					}
			    }
				if(subOpenid != null){
					// 对平台appid授权
					logger.info("2-重定向后====用户在服务商下openid=="+subOpenid);
					JSONObject heTengUserOpenid  = WeixinUtil.WeChatToken(code);
					if(!heTengUserOpenid.has("openid")){
						model.addAttribute("openiderror", "2您的账户暂时无法访问");
						return "openiderror";
					}
					openid = subOpenid;
					// 根据平台appid获取平台用户的信息
					User user2 = userService.getUserByOpenid(heTengUserOpenid.getString("openid"));
					if(user2 == null){
						model.addAttribute("openiderror", "3请先关注公众号");
						return "openiderror";
					}
					// 将用户的信息和服务商的openid进行存储
					logger.info("服务商下用户id=="+user2.getId()+"支付参数=="+openid);
					User fuWuUser = new User();
					fuWuUser.setId(user2.getId());
					fuWuUser.setOpenid(subOpenid);
					userService.addSubUser(fuWuUser);
					// 查询服务商下的用户
					touristuser = userService.selectSubUserByOpenid(subOpenid);
					if(touristuser == null){
						model.addAttribute("openiderror", "4您的账户暂时无法访问");
						return "openiderror";
					}
					logger.info("服务商返回前端数据==="+touristuser.getId()+"支付参数=="+openid);
					model.addAttribute("userinfo", touristuser);
					model.addAttribute("openid", openid);
				}
			}else{
				// 对平台appid授权
				doGetStr  = WeixinUtil.WeChatToken(code);
				// 判断设备包含不包含openid
				if (!doGetStr.has("openid")) {
					model.addAttribute("openiderror", "5您的账户暂时无法访问");
					return "openiderror";
				}
				openid = doGetStr.getString("openid");
				accesstoken = doGetStr.getString("access_token");
				// 检查用户和清除
				checkUserAndDispose(openid,accesstoken,equcode);
				touristuser = userService.getUserByOpenid(openid);
				if(touristuser == null){
					model.addAttribute("openiderror", "6您的账户暂时无法访问");
					return "openiderror";
				}
				model.addAttribute("userinfo", touristuser);
				model.addAttribute("openid", openid);
			}
			// String openid = "odVHPwtil1qLhywcJ1fQ0NXCftb8";
			model.addAttribute("code", equcode);
			// 检查用户和清除
			// checkUserAndDispose(openid,accesstoken,equcode);
			touristuser = touristuser == null ? new User() : touristuser;
			Integer touristid = CommUtil.toInteger(touristuser.getId());
			Integer mercid = CommUtil.toInteger(touristuser.getMerid());
			Double touristbalance = CommUtil.toDouble(touristuser.getBalance());
			Double touristsendmoney = CommUtil.toDouble(touristuser.getSendmoney());
			
			Integer iswallet = 1;
			if(!dealid.equals(mercid)) iswallet = 2;
			String tempphone = null;	

			model.addAttribute("iswallet", iswallet);
			model.addAttribute("balance", touristbalance);
			model.addAttribute("sendmoney", touristsendmoney);
			model.addAttribute("hardversion", hardversion);
			model.addAttribute("existuser", existuser);
			
			Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, dealid, devicenum, hardversion);
			String brandname = CommUtil.toString(DirectTemp.get("remark"));
			String temphone = CommUtil.toString(DirectTemp.get("common1"));
			servephone = basicsService.getServephoneData(temphone, null, servephone, dealerphne);
			Integer nowtempid = CommUtil.toInteger(DirectTemp.get("id"));
			Integer permit = CommUtil.toInteger(DirectTemp.get("permit"));
			String chargeInfo = CommUtil.toString(DirectTemp.get("chargeInfo"));
			Integer walletpay = CommUtil.toInteger(DirectTemp.get("walletpay"));
			List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
			tempson = tempson == null ? new ArrayList<>() : tempson;
			model.addAttribute("walletpay", walletpay);
			model.addAttribute("DirectTemp", DirectTemp);
			model.addAttribute("templateLists", tempson);
			model.addAttribute("phonenum", servephone);
			model.addAttribute("brandname", brandname);
			model.addAttribute("chargeInfo", chargeInfo);
			model.addAttribute("ifwallet", walletpay);
			//设备到期时间不为空且当前时间大于到期时间
			if(expirationTime != null && new Date().after(expirationTime)){
				model.addAttribute("errorinfo", "对不起，当前设备已到期");
				model.addAttribute("code", equcode);
				model.addAttribute("time", expirationTime);
				//提示用户设备到期更换设备
				return "chargeporterror";
			}
			if(devicestate.equals(0)){//判断设备和设备状态是否正常
				return "equipment/equipmentoffline";
			} else if (devicebindtype.equals(0) || dealid.equals(0)) {//判断设备是否被绑定
				if (!"00".equals(softversionnum) && "00".equals(hardversion)) {
					return "selectHardversion";
				} else if ("03".equals(hardversion)) {
					return "testpay";
				} else if ("02".equals(hardversion)) {
					model.addAttribute("defaultchoose", nowtempid);
					return "equipment/equipmentunbind";
				} else if ("04".equals(hardversion)) {
					return "redirect:/wxpay/offlineCardCharge?openid=" + openid + "&code=" + equcode;
				} else {
					temp(equcode, null, model);
					return "equipment/equipmentunbind";
				}
			} else if ("04".equals(hardversion)) {//在线状态的离线充值机
				checkUserBindAndDispose(openid, equcode);
				return "redirect:/wxpay/offlineCardCharge?openid=" + openid + "&code=" + equcode;
			} else if ("03".equals(hardversion)) {//在线状态的脉冲投币
				checkUserBindAndDispose(openid, equcode);
				return "redirect:/wxpay/inCoins?openid=" + openid + "&code=" + equcode;
			} else {//在线状态的其他
				int send_15 = SendMsgUtil.send_15(equcode);
				if (send_15 == 0) {
					return "equipment/equipmentoffline";
				}
				List<String> forbidport = new ArrayList<String>();
				if ("01".equals(hardversion)) {
					Map<String, Object> defaulte = tempDefaultObje(tempson);
					model.addAttribute("defaultchoose", defaulte.get("defaultchoose"));
					model.addAttribute("defaultindex", defaulte.get("defaultindex"));
//					temp = getTempInfo(temp, touristid, dealid);//判断是否为分等级模板
//					temp = getTempInfo(temp, userByOpenid.getId(), userEquipment.getUserId());
					forbidport = getAssignPotr(touristid, devicenum, dealid);
				}else if ("08".equals(hardversion) || "09".equals(hardversion) || "10".equals(hardversion)) {
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
					model.addAttribute("temporaryc", walletpay);
					model.addAttribute("tempower", tempower);
					model.addAttribute("temtime", temtime);
					model.addAttribute("temmoney", temmoney);
					Map<String, Object> defaulte = tempDefault(temmoney);
					model.addAttribute("defaultchoose", defaulte.get("defaultchoose"));
					model.addAttribute("defaultindex", defaulte.get("defaultindex"));
				}else{
					Map<String, Object> defaulte = tempDefaultObje(tempson);
					model.addAttribute("defaultchoose", defaulte.get("defaultchoose"));
					model.addAttribute("defaultindex", defaulte.get("defaultindex"));
				}
				model.addAttribute("forbidport", JSON.toJSONString(forbidport));
				if (DirectTemp != null && !subMer) {//主模板信息
					try {
						// 当设备不是脉冲设备且用户支持退费的情况下去引导用户关注
						if (!"03".equals(equi) && !"04".equals(equi) && permit == 1) {
							// 根据openid和accesstoken获取用户是否关注的信息
							String getUserInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="
									+ WeixinUtil.getBasicAccessToken() + "&openid=" + openid + "&lang=zh_CN";
							// 将get请求返回的信息，转换成JSON对象
							JSONObject doGetStr1 = WeixinUtil.doGetStr(getUserInfoUrl);
							// 判段用户的是否关注:0为未关注，1为已关注
							if ("0".equals(doGetStr1.getString("subscribe"))) {
								// 如果没有关注跳转到一个关注的页面
								return "attention";
							}
						}
					} catch (Exception e) {
						logger.warn("判断用户是否关注公众号失败===" + e.getMessage());
					}

				}
				if (!"02".equals(hardversion)) {// 02 电轿款
					String checkMonthBackVal = checkMonthBackVal(dealid);
					if (!"2".equals(checkMonthBackVal)) {
						PackageMonth packageMonth = userService.selectPackageMonthByUid(touristid);
						packageMonth = packageMonth == null ? new PackageMonth() : packageMonth;
						model.addAttribute("packageMonth", packageMonth);
					} else {
						model.addAttribute("packageMonth", null);
					}
					model.addAttribute("ifmonth", checkMonthBackVal);
				} else {
					model.addAttribute("ifmonth", "0");
				}
				List<Map<String, String>> portStatus = new ArrayList<>();
				Map<String, String> codeRedisMap = JedisUtils.hgetAll(devicenum);
				if (codeRedisMap != null) {
					if ("02".equals(equipment.getHardversion()) || "09".equals(equipment.getHardversion())) {
						addPortStatus(2, codeRedisMap, portStatus);
					} else if ("01".equals(equipment.getHardversion())) {
						addPortStatus(10, codeRedisMap, portStatus);
					} else if ("05".equals(equipment.getHardversion())) {
						addPortStatus(16, codeRedisMap, portStatus);
					} else if ("06".equals(equipment.getHardversion()) || "10".equals(equipment.getHardversion())) {
						addPortStatus(20, codeRedisMap, portStatus);
					} else if ("07".equals(equipment.getHardversion())) {
						addPortStatus(1, codeRedisMap, portStatus);
					} else {
						addPortStatus(10, codeRedisMap, portStatus);
					}
				}
				if (portStatus.size() > 0) {
					model.addAttribute("allPortSize", portStatus.size());
					Map<String, String> map = portStatus.get(0);
					String updateTimeStr = map.get("updateTime");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					long time = sdf.parse(updateTimeStr).getTime();
//							long time = portStatus.get(0).getUpdateTime().getTime(); 
					long currentTime = System.currentTimeMillis();
					if ((currentTime - time) > 300000) {
						model.addAttribute("flag", true);
					} else {
						model.addAttribute("flag", false);
					}
					model.addAttribute("portStatus", portStatus);
				} else {
					model.addAttribute("allPortSize", 0);
					model.addAttribute("flag", true);
					model.addAttribute("portStatus", null);
				}
				
				model.addAttribute("allPortSize", portStatus.size());
				model.addAttribute("code", equcode);
				model.addAttribute("hardversion", equipment.getHardversion());
				model.addAttribute("equname", equipment.getRemark());
				model.addAttribute("areaname", equipment.getName());
				model.addAttribute("templatelist", tempson);
//				Map<String, Object> defaulte = tempDefaultObje(tempson);
//				model.addAttribute("defaultchoose", defaulte.get("defaultchoose"));
//				model.addAttribute("defaultindex", defaulte.get("defaultindex"));
				model.addAttribute("nowtime", System.currentTimeMillis() + "");
				checkUserBindAndDispose(openid, equcode);
				model.addAttribute("wolfstatus", "1");
				if(DisposeUtil.checkIfHasV3(hardversion)){
					return "chargeallportV3";
				}else{
					return "chargeallport1";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "openiderror";
		}

	}

//	temp = getTempInfo(temp, touristid, dealid);
	/**
	 * @Description：判断是否为分等级模板
	 * @author： origin
	 * @comment:
	 */
	public TemplateParent getTempInfo(TemplateParent temp, Integer uid, Integer merid) {
		TemplateParent tempinfo = new TemplateParent();
		if(temp == null) temp = tempinfo;
		Integer tempgrade = CommUtil.toInteger(temp.getGrade());
		if (tempgrade.equals(1)) {// 分等级
			Map<String, Object> urole = userService.selectUserRoleInfo(uid, merid);
			if (urole == null || urole.size() == 0) {
				tempinfo = temp;
			} else {
				Integer proleid = CommUtil.toInteger(urole.get("proleid"));
				if (proleid == 6) {
					tempinfo = templateService.templateGradeClassify(merid, 0, temp.getId(), 2);
				} else if (proleid == 7) {
					tempinfo = templateService.templateGradeClassify(merid, 0, temp.getId(), 3);
				}
			}
		} else {
			tempinfo = temp;
		}
		return tempinfo;
	}

	public List<String> getAssignPotr(Integer uid, String code, Integer merid) {
		// 获取该用户在此设备中被指定的所有端口号
		List<Map<String, Object>> assignport = equipmentService.assignportinfo(uid, code, merid);

		// 获取该设备被指定的所有端口号
		List<String> slelecToPortINfo = equipmentService.slelecToPortINfo(merid, code);
		List<String> forbidport = slelecToPortINfo;
		if (assignport == null || assignport.size() > 0) {
			List<String> list = new ArrayList<String>();
			for (Map<String, Object> item : assignport) {
				String port = CommUtil.toString(item.get("port"));
				list.add(port);
				System.out.println("输出list000：        " + list);
				boolean res = slelecToPortINfo.contains(port);
				if (res) {
					Iterator<String> it = forbidport.iterator();
					while (it.hasNext()) {
						String str = CommUtil.toString(it.next());
						if (port.equals(str)) {
							it.remove();
						}
					}
					System.out.println("输出forbidport00：        " + forbidport);
				}
			}
		}
		return forbidport;
	}

	public String checkMonthBackVal(Integer merid) {
		List<TemplateParent> monthTempList = templateService.getParentTemplateListByMerchantid(merid, 5);
		if (monthTempList != null && monthTempList.size() > 0) {
			TemplateParent templateParent = monthTempList.get(0);
			if (templateParent.getIfmonth() == 1) {
				return "1";
			} else {
				return "0";
			}
		} else {
			return "0";
		}
	}

	/**
	 * test pay by code & portchoose & chargeparam
	 * 
	 * @param code
	 * @param portchoose
	 * @param chargeparam
	 * @return
	 */
	@RequestMapping(value = "/paytest")
	@ResponseBody
	public String paytest(String code, Byte portchoose, Integer chargeparam, Integer several,
			String hardversion) {
		equipmentService.updateEquiTestSeveral(code, several);
		TemplateSon templateSon = templateService.getInfoTemplateOne(chargeparam);
		Double money = templateSon.getMoney();
		String chargeTime = templateSon.getChargeTime() + "";
		String chargeQuantity = templateSon.getChargeQuantity() + "";
		String money2 = String.valueOf(money * 100);
		int idx = money2.lastIndexOf(".");
		String total_fee = money2.substring(0, idx);
		Short money3 = Short.valueOf(total_fee);
		Short time = Short.valueOf(chargeTime);
		Short elec = Short.valueOf(chargeQuantity);
		System.out.println("hardversion===" + hardversion);
		if ("07".equals(hardversion) || DisposeUtil.checkIfHasV3(hardversion)) {
			SendMsgUtil.send_0x27((byte)portchoose, (short)(money3 / 10), (short)time, (short)elec, code, (byte)1);
		} else {
			SendMsgUtil.send_0x14(portchoose, (short) (money3 / 10), time, elec,
					code);
//			WolfHttpRequest.sendChargePaydata(portchoose, time, money3 / 10 + "", chargeQuantity, code , 0);
		}
		return "success";
	}

	/** bind equipment */
	@RequestMapping(value = "/allChargePortBind")
	public String allChargePortBind(String code, Model model) throws Exception {
		UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			model.addAttribute("rank", user.getRank());
			if (userEquipment != null) {
				model.addAttribute("code", code);
				return "equipment/equipmentisuse";
			} else {
				if (user.getRank() == 6) {
					user = userService.selectUserById(user.getMerid());
				}
				Equipment equipmentById = equipmentService.getEquipmentById(code);
				equipmentService.insertCodeoperatelog(code, 2, 1, user.getId(), user.getId(),equipmentById.getTempid() + "");
				// 到期时间不为为空第二次绑定
				if ( equipmentById.getExpirationTime() != null) {
					// 判断到期时间是否过期
					if(new Date().before(equipmentById.getExpirationTime())){
						Integer temid = templateService.getDefaultPartemp(user.getId(), equipmentById.getHardversion());
						equipmentById.setTempid(temid);
						equipmentById.setBindtype((byte) 1);
						equipmentService.updateEquipment(equipmentById);
						model.addAttribute("code", code);
						UserEquipment userEquipment1 = new UserEquipment();
						userEquipment1.setUserId(user.getId());
						userEquipment1.setEquipmentCode(code);
						userEquipmentService.addUserEquipment(userEquipment1);
						logger.info(user.getUsername()+"二次绑定"+equipmentById.getCode()+"设备成功");
						return "equipment/equipmentbind";
					}else {
						// 到期可以跳转到一个设备到期提示的页面
						logger.info(user.getUsername()+"二次绑定"+equipmentById.getCode()+"设备到期时间过期了");
						model.addAttribute("info",code);
						return "equipment/equipmentbind";
					}
				} else {
					//到期时间为空，为第一次绑定
					Calendar cal = Calendar.getInstance();
					cal.setTime(equipmentById.getCreateTime());
					cal.add(Calendar.YEAR, 1);
					//判断IMEI上传的创建时间是否过期
					if( new Date().before(cal.getTime())){
						Integer temid = templateService.getDefaultPartemp(user.getId(), equipmentById.getHardversion());
						equipmentById.setTempid(temid);
						equipmentById.setBindtype((byte) 1);
						//获取当前时间加一年，添加设备的到期时间
						cal.setTime(new Date());
						cal.add(Calendar.YEAR, 1);
						equipmentById.setExpirationTime(cal.getTime());
						logger.info("设备"+equipmentById.getCode()+"没有绑定过,IMEI创建时间有效");
						equipmentService.updateEquipment(equipmentById);
						model.addAttribute("code", code);
						UserEquipment userEquipment1 = new UserEquipment();
						userEquipment1.setUserId(user.getId());
						userEquipment1.setEquipmentCode(code);
						userEquipmentService.addUserEquipment(userEquipment1);
						return "equipment/equipmentbind";
					}else{
						//IMEI创建时间已经过期,联系超级管理员为流量卡充值
						//超级管理员可以为设备流量卡充值,手动添加一个到期时间
						logger.info(user.getUsername()+"一次绑定"+equipmentById+"设备IMEI时间过期了");
						model.addAttribute("imei",code);
						return "equipment/equipmentbind";
					}
				}
			}
		}
		return null;
	}

	/**
	 * const bind equipment in user
	 */
	@RequestMapping(value = "/pcbindequ")
	public String bindequ(@RequestParam("code") String code, Model model) {
		model.addAttribute("code", code);
		List<User> userList = userService.getUserList();
		model.addAttribute("userlist", userList);
		return "computer/constbind";
	}

	/** coerce bind equipment */
	@RequestMapping(value = "/pcuserbingequ")
	public String userbingequ(Integer userId, String code, Model model) {
		UserEquipment userEquipment = new UserEquipment();
		userEquipment.setUserId(userId);
		userEquipment.setEquipmentCode(code);
		userEquipmentService.addUserEquipment(userEquipment);
		Equipment equipment = equipmentService.getEquipmentById(code);
		equipment.setBindtype((byte) 1);
		if ("03".equals(equipment.getHardversion())) {
			List<TemplateParent> templateList = templateService.getParentTemplateListByMerchantid(0, 2);
			equipment.setTempid(templateList.get(0).getId());
		} else if ("04".equals(equipment.getHardversion())) {
			List<TemplateParent> templateList = templateService.getParentTemplateListByMerchantid(0, 1);
			equipment.setTempid(templateList.get(0).getId());
		} else {
			equipment.setTempid(0);
		}
		equipmentService.updateEquipment(equipment);
		model.addAttribute("code", code);
		model.addAttribute("username", userService.selectUserById(userId).getUsername());
		return "redirect:/pctest?code=" + code;
	}

	/**
	 * unbind equipment in user
	 */
	@RequestMapping(value = "/pcunbound")
	public String unbound(@RequestParam("code") String code, Model model) {
		userEquipmentService.deleteUserEquipmentByEquipmentCode(code);
		model.addAttribute("code", code);
		Equipment equipment = equipmentService.getEquipmentById(code);
		equipment.setBindtype((byte) 0);
		if ("03".equals(equipment.getHardversion())) {
			List<TemplateParent> templateList = templateService.getParentTemplateListByMerchantid(0, 2);
			equipment.setTempid(templateList.get(0).getId());
		} else if ("04".equals(equipment.getHardversion())) {
			List<TemplateParent> templateList = templateService.getParentTemplateListByMerchantid(0, 1);
			equipment.setTempid(templateList.get(0).getId());
		} else if ("04".equals(equipment.getHardversion())) {
			List<TemplateParent> templateList = templateService.getParentTemplateListByMerchantid(0, 3);
			equipment.setTempid(templateList.get(0).getId());
		} else {
			equipment.setTempid(0);
		}
		equipmentService.updateEquipment(equipment);
		UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
		if (userEquipment == null) {
			model.addAttribute("username", '0');
			return "redirect:/pctest?code=" + code;
		}
		User user = userService.selectUserById(userEquipment.getUserId());
		if (user == null) {
			model.addAttribute("username", '0');
		} else {
			model.addAttribute("username", user.getUsername());
		}
		return "redirect:/pctest?code=" + code;
	}

	@RequestMapping(value = "/editHardversion", method = RequestMethod.POST)
	public String editHardversion(Model model) {
		String hardversion = request.getParameter("hardversion");
		String code = request.getParameter("code");
		String openid = request.getParameter("openid");
		String existuser = request.getParameter("existuser");
		equipmentService.updateEquHardversionByCode(code, hardversion);
		model.addAttribute("code", code);
		model.addAttribute("openid", openid);
		model.addAttribute("existuser", existuser);
		if ("03".equals(hardversion)) {
			List<TemplateParent> templateLists = templateService.getParentTemplateListByMerchantid(0, 2);
			equipmentService.updateTempidByEquipmentCode(code, templateLists.get(0).getId());
			return "testpay";
		} else if ("04".equals(hardversion)) {
			List<TemplateParent> templateLists = templateService.getParentTemplateListByMerchantid(0, 1);
			equipmentService.updateTempidByEquipmentCode(code, templateLists.get(0).getId());
			return "redirect:/wxpay/offlineCardCharge?openid=" + openid + "&code=" + code;
		} else if ("02".equals(hardversion) || "07".equals(hardversion)) {
			List<TemplateParent> templist = templateService.getParentTemplateListByMerchantid(0, 0);
			equipmentService.updateTempidByEquipmentCode(code, templist.get(0).getId());
			model.addAttribute("hardversion", "02");
			List<TemplateSon> templateLists = templateService.getSonTemplateLists(templist.get(0).getId());
			model.addAttribute("templateLists", templateLists);
			if(templateLists.size()>=2){
				model.addAttribute("defaultchoose", templateLists.get(1).getId());
			}else{
				model.addAttribute("defaultchoose", templateLists.get(0).getId());
			}
			model.addAttribute("hardversion", "02");
			return "equipment/equipmentunbind";
		} else if (DisposeUtil.checkIfHasV3(hardversion)) {
			List<TemplateParent> templist = templateService.getParentTemplateListByMerchantid(0, 6);
			equipmentService.updateTempidByEquipmentCode(code, templist.get(0).getId());
			model.addAttribute("hardversion", hardversion);
			List<TemplateSon> templateLists = templateService.getSonTemplateLists(templist.get(0).getId());
			model.addAttribute("templateList", templateLists);
			return "equipment/equipmentunbind";
		} else {
			equipmentService.updateTempidByEquipmentCode(code, 0);
			temp(code, null, model);
			return "equipment/equipmentunbind";
		}
	}

	@RequestMapping(value = "/portpaypage", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> portpaypage(Byte payport, String code, Model model) {
		model.addAttribute("payport", payport);
		model.addAttribute("code", code);
		Map<String, String> map = new HashMap<>();
		map.put("param", "1");
		return map;
	}

	@RequestMapping(value = "/portpaytest")
	public String portpaytest(Byte payport, String code, Model model) {
		model.addAttribute("payport", payport);
		model.addAttribute("code", code);
		return "portpaytest";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/codeDayEarn")
	public String codeDayEarn(String code, Model model) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			if(user==null) return "erroruser";
			Integer merid = user.getId();
			if (user.getRank() == 6) {
				merid = CommUtil.toInteger(user.getMerid());
			}
			model.addAttribute("merid", merid);
			List<Codestatistics> earnlist = equipmentService.selectAllByCodeAndLimit(code, merid, 0, 30);
			List<Map<String, Object>> list = new ArrayList<>();
			for(Codestatistics item : earnlist){
				Double moneytotal = CommUtil.toDouble(item.getMoneytotal());
				Double wechatretmoney = CommUtil.toDouble(item.getWechatretmoney());
				Double alipayretmoney = CommUtil.toDouble(item.getAlipayretmoney());
				Double returnmoney = CommUtil.addBig(wechatretmoney, alipayretmoney);
				Double pulsemoney = CommUtil.toDouble(item.getIncoinsmoney());
				Double windowpulsemoney = CommUtil.toDouble(item.getWindowpulsemoney());
				//Double oncardmoney = CommUtil.toDouble(item.getOncardmoney());
				Double incomemoney = StringUtil.subBig(moneytotal, returnmoney);
				Double incoinstotalmoney = CommUtil.addBig(pulsemoney, windowpulsemoney);
				Date datetime = item.getCountTime();
				Map<String, Object> datamap = JSON.parseObject(JSON.toJSONString(item), Map.class);
				
				datamap.put("countTimes", CommUtil.toDateTime("yyyy-MM-dd", datetime));
				datamap.put("incomemoney", incomemoney);
				datamap.put("incoinstotalmoney", incoinstotalmoney);
				list.add(datamap);
			}
			//===origin 单个设备判断 ============================================================
			if(earnlist.size()>0){
				Codestatistics codest = earnlist.get(earnlist.size()-1);
				Integer dealid = codest.getMerid();
				String time = CommUtil.getPastDate(30);
				String timestart = CommUtil.toDateTime("yyyy-MM-dd", codest.getCountTime());
				Integer comparenum = CommUtil.compareStringTime( time + " 00:00:00", timestart + " 00:00:00");
				System.out.println(comparenum);
				if(comparenum!=0){
					//执行单个设备汇总
//				Object result = systemSetService.selfDynamicCollect( 2, dealid, 25, code);
					model.addAttribute("isCalculate", 1);
					model.addAttribute("daysnum", 30);
				}
			}
			//===origin ============================================================
			Equipment equipment = equipmentService.getEquipmentById(code);
			DealerAuthority jurisdiction = authorityService.selectMessSwitch(merid);
			model.addAttribute("showincoins", CommUtil.toInteger(jurisdiction.getShowincoins()));
			model.addAttribute("equipment", equipment);
			model.addAttribute("today", new Date());
			model.addAttribute("earnlist", list);
			return "/equipment/codeDayEarn";
		} catch (Exception e) {
			return "erroruser";
		}
	}

	@RequestMapping(value = "/ajaxCodeDayEarn")
	@ResponseBody
	public Map<String, Object> ajaxCodeDayEarn(String code, Integer beginnum) {
		Map<String, Object> map = new HashMap<>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			if(user==null) return CommUtil.responseBuild(901, "登录过期，请重新登录", map);
			List<Codestatistics> earnlist = equipmentService.selectAllByCodeAndLimit(code, user.getId(), beginnum, 30);
			map.put("merid", user.getId());
//			map.put("earnlist", earnlist);
			List<Map<String, Object>> list = new ArrayList<>();
			for(Codestatistics item : earnlist){
				Double moneytotal = CommUtil.toDouble(item.getMoneytotal());
				Double wechatretmoney = CommUtil.toDouble(item.getWechatretmoney());
				Double alipayretmoney = CommUtil.toDouble(item.getAlipayretmoney());
				Double returnmoney = CommUtil.addBig(wechatretmoney, alipayretmoney);
				Double pulsemoney = CommUtil.toDouble(item.getIncoinsmoney());
				Double windowpulsemoney = CommUtil.toDouble(item.getWindowpulsemoney());
				//Double oncardmoney = CommUtil.toDouble(item.getOncardmoney());
				Double incomemoney = StringUtil.subBig(moneytotal, returnmoney);
				Double incoinstotalmoney = CommUtil.addBig(pulsemoney, windowpulsemoney);
				Map<String, Object> datamap = JSON.parseObject(JSON.toJSONString(item), Map.class);
				datamap.put("incomemoney", incomemoney);
				datamap.put("incoinstotalmoney", incoinstotalmoney);
				list.add(datamap);
			}
			map.put("earnlist", list);
			//===origin 单个设备判断 ============================================================
			if(earnlist.size()>0){
				Codestatistics codest = earnlist.get(earnlist.size()-1);
				Integer dealid = codest.getMerid();
				String time = CommUtil.getPastDate(30);
				String timestart = CommUtil.toDateTime("yyyy-MM-dd", codest.getCountTime());
				Integer comparenum = CommUtil.compareStringTime( time + " 00:00:00", timestart + " 00:00:00");
				System.out.println(comparenum);
				if(comparenum!=0){
					//执行单个设备汇总
					map.put("isCalculate", 1);
					map.put("daysnum", beginnum + 30);
				}
			}
			//===origin ============================================================
			return map;
		} catch (Exception e) {
			return CommUtil.responseBuildInfo(301, "异常错误", map);
		}
	}

	@RequestMapping(value = "/editRemark")
	@ResponseBody
	public String editRemark(String code, String remark) {
		Equipment equipment = new Equipment();
		equipment.setCode(code);
		equipment.setRemark(remark);
		equipmentService.updateEquipment(equipment);
		return "1";
	}

	@RequestMapping(value = "/equinfo")
	public String equinfo(String code, Model model) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) {
				return "erroruser";
			}
			Equipment equipment = equipmentService.getEquipmentById(code);
			if(equipment!=null){
				Integer dealid = CommUtil.toInteger(user.getId());
				String phone = CommUtil.toString(user.getPhoneNum());
				String servephone = CommUtil.toString(user.getServephone());
				servephone = servephone == null ? phone : servephone;
				String version = CommUtil.toString(equipment.getHardversion());
				Integer status = 0;
				if(version.equals("03")){
					status = 2;
				} else if(version.equals("04")){
					status = 1;
				} else if(version.equals("08")){
					status = 6;
				}
				Map<String, Object> listdata = templateService.copyDirectTemp(status, dealid, servephone);
				model.addAttribute("listdata", listdata);
			}
//			if(equipment!=null && equipment.getHardversion()!=null) verifyTemplate(equipment.getHardversion());
			model.addAttribute("equipment", equipment);
			return "/equipment/equinfo";
		} catch (Exception e) {
			return "erroruser";
		}
	}
	
	@RequestMapping(value = "/deviceDetail")
	public String deviceDetail(String code, Model model) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) return "erroruser";
//			Equipment equipment = equipmentService.getEquipmentById(code);
			Map<String, Object> equipmentinfo = equipmentService.selectEquipAreaInfo(code);
			model.addAttribute("equipmentinfo", equipmentinfo);
//			model.addAttribute("equipment", equipment);
			return "equipment/deviceDetail";
		} catch (Exception e) {
			return "erroruser";
		}
	}

	@RequestMapping(value = "/specifieduseportinfo")
	public String specifiedUsePortInfo(String code, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		model.addAttribute("portInfo1", equipmentService.specifiedUsePortInfo( user.getId(), code, 1));
		model.addAttribute("portInfo2", equipmentService.specifiedUsePortInfo( user.getId(), code, 2));
		model.addAttribute("portInfo3", equipmentService.specifiedUsePortInfo( user.getId(), code, 3));
		model.addAttribute("portInfo4", equipmentService.specifiedUsePortInfo( user.getId(), code, 4));
		model.addAttribute("portInfo5", equipmentService.specifiedUsePortInfo( user.getId(), code, 5));
		model.addAttribute("portInfo6", equipmentService.specifiedUsePortInfo( user.getId(), code, 6));
		model.addAttribute("portInfo7", equipmentService.specifiedUsePortInfo( user.getId(), code, 7));
		model.addAttribute("portInfo8", equipmentService.specifiedUsePortInfo( user.getId(), code, 8));
		model.addAttribute("portInfo9", equipmentService.specifiedUsePortInfo( user.getId(), code, 9));
		model.addAttribute("portInfo10", equipmentService.specifiedUsePortInfo( user.getId(), code, 10));
		model.addAttribute("code", code);
		return "/equipment/portInfo";
	}
	
	@RequestMapping(value = "/addspecifiedport")
	@ResponseBody
	public Map< String, Object> addSpecifiedPort(String equip, Integer port, String member, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		Map< String, Object> result = equipmentService.addSpecifiedPort(user, equip, port, member);
		return result;
	}
	
	@RequestMapping(value = "/deletespecifiedport")
	@ResponseBody
	public Map< String, Object> deleteSpecifiedPort(String memberList,  Model model) {
		Map< String, Object> result = equipmentService.deleteSpecifiedPort(memberList);
		return result;
	}
	
	
	/**
	 * 判断个人模板是否存在，存在跳过，不存在添加
	 */
	public void verifyTemplate(String val) {
		try {
			User user = (User) request.getSession().getAttribute("admin");
			if(user!=null && user.getId() != null){
				if(val.equals("00") || val.equals("01") || val.equals("02")){//充电 默认、十路、电轿
					int fale = templateService.verifyTemplate(user.getId(), 0);
					if (fale==0) templateService.insertDufaultTemplate(user.getId());
				}else if(val.equals("04")){//离线模板
					int fale = templateService.verifyTemplate(user.getId(), 1);
					if (fale==0) templateService.insertOfflineTemp(user.getId());
				}else if(val.equals("03")){//脉冲（投币）
					int fale = templateService.verifyTemplate(user.getId(), 2);
					if (fale==0)  templateService.insertInCoinsTemp(user.getId());
				}/*else if(val.equals("00")){//钱包
					int fale = templateService.verifyTemplate(user.getId(), 3);
					if (fale==0)  templateService.insertWalletTemp(user.getId());
				}else if(val.equals("00")){//在线卡
					int fale = templateService.verifyTemplate(user.getId(), 4);
					if (fale==0)  templateService.insertOnlineTemp(user.getId());
				}*/
			}
		} catch (Exception e) {
			logger.info(CommUtil.getExceptInfo(e));
		}
	}
	
	@RequestMapping(value = "/codesystem")
	public String codesystem(String code, Model model) {
		Map<String, Object> equipmentmap = CommUtil.isMapEmpty(equipmentService.selectEquipAreaInfo(code));
		CodeSystemParam codeSystemParam = equipmentService.selectCodeSystemParamByCode(code);
		model.addAttribute("sysparam", codeSystemParam);
		model.addAttribute("code", code);
		model.addAttribute("hwVerson", CommUtil.toString(equipmentmap.get("hardversion")));
		model.addAttribute("merid", CommUtil.toInteger(equipmentmap.get("dealid")));
		model.addAttribute("equipmentmap", equipmentmap);
//		return "/equipment/codesystem";
		return "/equipment/codesystem2";
	}
	
	/**
	 * separate
	 * @Description：搜索商户名下同类型设备信息（并按小区排序）
	 * @author： origin
	 * @createTime：2019年12月31日上午10:28:42
	 */
	@RequestMapping(value="/searchDeviceData")
	@ResponseBody
    public Object searchDeviceData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		User usersession = (User) request.getSession().getAttribute("user");
		if(usersession==null){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = equipmentService.searchDeviceData(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * 手机端设置系统参数
	 * @param codeSystemParam
	 * @return {@link Map}
	 */
	@RequestMapping(value = "/setSysParam")
	@ResponseBody
	public Map<String, String> setSysParam(CodeSystemParam codeSystemParam) {
		String code = codeSystemParam.getCode();
		Integer coinMin = codeSystemParam.getCoinMin();
		Integer cardMin = codeSystemParam.getCardMin();
		Double coinElec = codeSystemParam.getCoinElec();
		Double cardElec = codeSystemParam.getCardElec();
		Double cst = codeSystemParam.getCst();
		Integer powerMax1 = codeSystemParam.getPowerMax1();
		Integer powerMax2 = codeSystemParam.getPowerMax2();
		Integer powerMax3 = codeSystemParam.getPowerMax3();
		Integer powerMax4 = codeSystemParam.getPowerMax4();
		Integer powerTim2 = codeSystemParam.getPowerTim2();
		Integer powerTim3 = codeSystemParam.getPowerTim3();
		Integer powerTim4 = codeSystemParam.getPowerTim4();
		Integer spRecMon = codeSystemParam.getSpRecMon();
		Integer spFullEmpty = codeSystemParam.getSpFullEmpty();
		Integer fullPowerMin = codeSystemParam.getFullPowerMin();
		Integer fullChargeTime = codeSystemParam.getFullChargeTime();
		Integer elecTimeFirst = codeSystemParam.getElecTimeFirst();
		Double clacCoinElec = coinElec * 10;
		Double clacCardElec = cardElec * 10;
		Double clacCst = cst * 10;
		String clacCoinElecStr = WxPayController.doubleToString(clacCoinElec);
		String clacCardElecStr = WxPayController.doubleToString(clacCardElec);
		String clacCstStr = WxPayController.doubleToString(clacCst);
		// HashMap<String, String> hashMap = new HashMap<>();
		// hashMap.put("code", code);
		// hashMap.put("coinMin", coinMin + "");
		// hashMap.put("cardMin", cardMin + "");
		// hashMap.put("coinElec", clacCoinElecStr);
		// hashMap.put("cardElec", clacCardElecStr);
		// hashMap.put("cst", clacCstStr);
		// hashMap.put("powerMax1", powerMax1 + "");
		// hashMap.put("powerMax2", powerMax2 + "");
		// hashMap.put("powerMax3", powerMax3 + "");
		// hashMap.put("powerMax4", powerMax4 + "");
		// hashMap.put("powerTim2", powerTim2 + "");
		// hashMap.put("powerTim3", powerTim3 + "");
		// hashMap.put("powerTim4", powerTim4 + "");
		// hashMap.put("spRecMon", spRecMon + "");
		// hashMap.put("spFullEmpty", spFullEmpty + "");
		// hashMap.put("fullPowerMin", fullPowerMin + "");
		// hashMap.put("fullChargeTime", fullChargeTime + "");
		// hashMap.put("elecTimeFirst", elecTimeFirst + "");
		// Map<String, String> map = WolfHttpRequest.httpconnectwolf(hashMap,
		// WolfHttpRequest.SEND_SETSYSTEM_URL);
		SendMsgUtil.send_24(parseShort(coinMin + ""), parseShort(cardMin + ""), parseByte(clacCoinElecStr),
				parseByte(clacCardElecStr), parseByte(clacCstStr), parseShort(powerMax1 + ""),
				parseShort(powerMax2 + ""), parseShort(powerMax3 + ""), parseShort(powerMax4 + ""),
				parseByte(powerTim2 + ""), parseByte(powerTim3 + ""), parseByte(powerTim4 + ""),
				parseByte(spRecMon + ""), parseByte(spFullEmpty + ""), parseByte(fullPowerMin + ""),
				parseByte(fullChargeTime + ""), parseByte(elecTimeFirst + ""), code);
		long currentTime = System.currentTimeMillis();
		boolean flag = true;
		int temp = 0;
		Map<String, String> map = new HashMap<>();
		while (flag) {
			if (temp >= 20) {
				map.put("wolfcode", "1001");
				map.put("wolfmsg", "连接超时");
				break;
			}
			Map<String, String> mapRecive = Server.setSystemMap.get(code);
			if (mapRecive == null || mapRecive.size() < 1) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				temp++;
				continue;
			}
			long udpatetime = Long.parseLong(mapRecive.get("updatetime"));
			if ((udpatetime - currentTime) > 0 && (udpatetime - currentTime) < 20000) {
				CodeSystemParam systemParam = equipmentService.selectCodeSystemParamByCode(code);
				if (systemParam == null) {
					equipmentService.insertCodeSystemParam(codeSystemParam);
					System.out.println("添加设备系统参数");
				} else {
					System.out.println("这是系统参数"+codeSystemParam);
					equipmentService.updateCodeSystemParam(codeSystemParam);
					System.out.println("修改设备系统参数");
				}
				map.put("readtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				map.putAll(mapRecive);
				flag = false;
				Server.setSystemMap.remove(code);
				break;
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				temp++;
				continue;
			}
		}
		return map;
	}

	/**
	 * 检查和更新用户
	 * @param openid
	 * @param accesstoken
	 * @param equcode
	 */
	public void checkUserAndDispose(String openid, String accesstoken, String equcode) {
		try {
			// 根据openID查询用户表的用户
			User userByOpenid = userService.getUserByOpenid(openid);
			// 根据设备号查询设备信息
			Equipment equipment = equipmentService.getEquipmentById(equcode);
			// 用户为空
			if (userByOpenid == null) {
				User user = new User();
				user.setOpenid(openid);
				user.setEarnings(Double.valueOf(0.00));
				user.setRank(1);
				user.setCreateTime(new Date());
				// 存在设备和设备绑定小区
				if (equipment != null) {
					// 根据设备号查询用户的设备信息
					UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(equcode);
					// 查询小区信息
					Area area = areaService.selectByIdArea(equipment.getAid());
					// 商家的设备不为空
					if (userEquipment != null) {
						// 小区不为空,用户设备的用户id和小区的商家id相同
						if (area != null && userEquipment.getUserId().equals(area.getMerid())) {
							user.setAid(equipment.getAid());
						}
						user.setMerid(userEquipment.getUserId());
					}
				}
				// 添加用户
				this.userService.addUser(user);
			} else {
				Integer tourid = CommUtil.toInteger(userByOpenid.getId());
				Integer merid = CommUtil.toInteger(userByOpenid.getMerid());
				Integer aid = CommUtil.toInteger(userByOpenid.getAid());
				Double balance = CommUtil.toDouble(userByOpenid.getBalance());
				Double sendmoney = CommUtil.toDouble(userByOpenid.getSendmoney());
				balance = CommUtil.addBig(balance, sendmoney);
				Integer deviceaid = CommUtil.toInteger(equipment.getAid());
				UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(equcode);
				userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
				Integer devicemerid = CommUtil.toInteger(userEquipment.getUserId());
				if(!devicemerid.equals(0)){
					if(balance==0){//ORIGIN	用户金额为空
						User user = new User();
						user.setId(tourid);
						user.setAid(deviceaid);
						user.setMerid(devicemerid);
						userService.updateUserById(user);
					} else if(merid.equals(devicemerid)){//ORIGIN	已绑定商户
						if(aid.equals(0) && balance<5){
							User user = new User();
							user.setId(tourid);
							user.setAid(deviceaid);
							userService.updateUserById(user);
						}
					}else if(merid == 0 && balance<5){
						User user = new User();
						user.setId(tourid);
						user.setAid(deviceaid);
						user.setMerid(devicemerid);
						userService.updateUserById(user);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description：检查处理用户
	 * @param openid
	 * @param accesstoken
	 * @param equcode
	 * @author： origin 2020年5月29日下午5:13:05
	 */
	public void checkDisposeUser(Integer tourid, Integer dealid, Integer mercid, Integer aid, 
			String openid, Double balance) {
		try {
			if(tourid.equals(0)){//不存在，添加
				User user = new User();
				user.setOpenid(openid);
				user.setEarnings(Double.valueOf(0.00));
				user.setRank(1);
				user.setAid(aid);
				user.setMerid(mercid);
				user.setCreateTime(new Date());
				userService.addUser(user);
			}else if(dealid.equals(0) || balance==0){//未绑定商户或绑定的商户时钱包为0
				User user = new User();
				user.setId(tourid);
				user.setAid(aid);
				user.setMerid(mercid);
				userService.updateUserById(user);
			}else if(dealid.equals(mercid)) {//绑定商户但小区不存在
				if(aid.equals(0)){
					User user = new User();
					user.setId(tourid);
					user.setAid(mercid);
					userService.updateUserById(user);
				}
			}
		} catch (Exception e) {
			logger.warn("扫码添加用户失败：" + e.getMessage() + "---报错位置：" + ByteUtils.getLineNumber(e));
		}
	}

	public void checkUserBindAndDispose(String openid, String code) {
		User user = userService.getUserByOpenid(openid);
		if (user != null) {
			if (user.getMerid() == null || user.getMerid() == 0) {
				UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
				userService.bindUserBelongMerid(user.getId(), userEquipment.getUserId());
			}
		}
	}

	public static int getAllportNeednum(String hardversion) {
		int neednum = 10;
		if ("05".equals(hardversion)) {
			neednum = 16;
		} else if ("06".equals(hardversion)) {
			neednum = 20;
		} else if ("02".equals(hardversion)) {
			neednum = 2;
		}
		return neednum;
	}
	
	public static void addPortStatus(int len,Map<String,String> codeRedisMap,List<Map<String,String>> portStatus) {
		for (int i = 1; i < len + 1; i++) {
			String portStatusStr = codeRedisMap.get(i + "");
			Map<String,String> portStatusMap = (Map<String, String>) JSON.parse(portStatusStr);
			portStatus.add(portStatusMap);
		}
	}

	public byte parseByte(String val) {
		Integer i = Integer.parseInt(val);
		return i.byteValue();
	}

	public short parseShort(String val) {
		return Short.parseShort(val);
	}
	/**
	 * @param equcode 设备号            
	 * @param model
	 * @return String
	 * @throws Exception
	 */

	@RequestMapping(value = "/allChargePortTest")
	public String allChargePortTest(Model model) throws Exception {
		try {
			String equcode = "007490";
			
			if(equcode == null || "".equals(equcode)){
				model.addAttribute("openiderror", "扫描的设备号错误");
				return "openiderror";
			}
			//微信code信息
			//String code = CommUtil.toString(request.getParameter("code"));
			//String existuser = CommUtil.toString(request.getParameter("existuser"));
			// 获取设备号
			String devicenum = CommUtil.toString(equcode);
			// 根据code查询出相应的设备信息
			Equipment equipment = equipmentService.getEquipmentAndAreaById(devicenum);
			equipment = equipment == null ? new Equipment() : equipment;
			
			//设备信息
			String devicename = CommUtil.toString(equipment.getRemark());
			Integer devicestate = CommUtil.toInteger(equipment.getState());
			Integer devicebindtype = CommUtil.toInteger(equipment.getBindtype());
			
			String hardversion = CommUtil.toString(equipment.getHardversion());
			String softversionnum = CommUtil.toString(equipment.getSoftversionnum());
			Integer tempid = CommUtil.toInteger(equipment.getTempid());
			Integer deviceareaid = CommUtil.toInteger(equipment.getAid());
			Integer several = CommUtil.toInteger(equipment.getSeveral());
			Date expirationTime= equipment.getExpirationTime();
			// 获取设备版本信息
			String equi = CommUtil.toString(equipment.getHardversion());
			// 根据设备信息查询区域信息
			Area area = areaService.selectByIdArea(deviceareaid);
			area = area == null ? new Area() : area;
			Integer areaid = CommUtil.toInteger(area.getId());
			String areaname = CommUtil.toString(area.getName());
			
			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(devicenum);
			userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
			Integer dealid = CommUtil.toInteger(userEquipment.getUserId());
			User dealeruser = new User();
			if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
			String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
			String servephone = CommUtil.toString(dealeruser.getServephone());
			
			model.addAttribute("equipment", equipment);
			model.addAttribute("area", area);
			// equipmentService.slelectPortInfo(equipment.getCode(),);
			model.addAttribute("several", several);
			model.addAttribute("hardversion", hardversion);
			// 用户同意授权，通过code换取网页授权access_token
			// 根据设备号判断是特约商户还是普通用户
			boolean subMer = userEquipmentService.subMerByCode(equcode);
			logger.info("是不是特约商户"+"======="+subMer);
			String openid = "odVHPwsWH3V7ysdKsriIuzr8cEus";
			//JSONObject doGetStr = null;
			//if(subMer){
			//     String subMerCode = new StringBuffer(equcode).substring(0,6);
				// 对特约商户的appid授权
				//doGetStr = WeixinUtil.WeChatTokenSubMer(code, subMerCode);
			//}else{
				// 对平台appid授权
				//doGetStr  = WeixinUtil.WeChatToken(code);
			//}
			// 判断设备包含不包含openid
			//if (!doGetStr.has("openid")) {
				//model.addAttribute("openiderror", "您的账户暂时无法访问");
				//return "openiderror";
			//}
			//String openid = doGetStr.getString("openid");
			//String accesstoken = doGetStr.getString("access_token");
			// String openid = "odVHPwtil1qLhywcJ1fQ0NXCftb8";
			model.addAttribute("code", equcode);
			model.addAttribute("openid", openid);
			// 检查用户和清除
			// checkUserAndDispose(openid,accesstoken,equcode);
			User user = new User();
			user.setId(0);
			user.setPayhint(1);
			model.addAttribute("userinfo", user);
			User touristuser = userService.getUserByOpenid(openid);
			touristuser = touristuser == null ? new User() : touristuser;
			Integer touristid = CommUtil.toInteger(touristuser.getId());
			Integer mercid = CommUtil.toInteger(touristuser.getMerid());
			Double touristbalance = CommUtil.toDouble(touristuser.getBalance());
			Double touristsendmoney = CommUtil.toDouble(touristuser.getSendmoney());
			
			Integer iswallet = 1;
			if(!dealid.equals(mercid)) iswallet = 2;
			String tempphone = null;	

			model.addAttribute("iswallet", iswallet);
			model.addAttribute("balance", touristbalance);
			model.addAttribute("sendmoney", touristsendmoney);
			model.addAttribute("hardversion", hardversion);
			//model.addAttribute("existuser", existuser);
			
			Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, dealid, devicenum, hardversion);
			String brandname = CommUtil.toString(DirectTemp.get("remark"));
			String temphone = CommUtil.toString(DirectTemp.get("common1"));
			servephone = basicsService.getServephoneData(temphone, null, servephone, dealerphne);

			Integer nowtempid = CommUtil.toInteger(DirectTemp.get("id"));
			Integer permit = CommUtil.toInteger(DirectTemp.get("permit"));
			String chargeInfo = CommUtil.toString(DirectTemp.get("chargeInfo"));
			Integer walletpay = CommUtil.toInteger(DirectTemp.get("walletpay"));
			List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
			tempson = tempson == null ? new ArrayList<>() : tempson;
			model.addAttribute("walletpay", walletpay);
			model.addAttribute("DirectTemp", DirectTemp);
			model.addAttribute("templateLists", tempson);
			model.addAttribute("phonenum", servephone);
			//设备到期时间不为空且当前时间大于到期时间
			if(expirationTime != null && new Date().after(expirationTime)){
				model.addAttribute("errorinfo", "对不起，当前设备已到期");
				model.addAttribute("code", equcode);
				model.addAttribute("time", expirationTime);
				//提示用户设备到期更换设备
				return "chargeporterror";
			}
			if(devicestate.equals(0)){//判断设备和设备状态是否正常
				return "equipment/equipmentoffline";
			} else if (devicebindtype.equals(0) || dealid.equals(0)) {//判断设备是否被绑定
				if (!"00".equals(softversionnum) && "00".equals(hardversion)) {
					return "selectHardversion";
				} else if ("03".equals(hardversion)) {
					return "testpay";
				} else if ("02".equals(hardversion)) {
					model.addAttribute("defaultchoose", nowtempid);
					return "equipment/equipmentunbind";
				} else if ("04".equals(hardversion)) {
					return "redirect:/wxpay/offlineCardCharge?openid=" + openid + "&code=" + equcode;
				} else {
					temp(equcode, null, model);
					return "equipment/equipmentunbind";
				}
			} else if ("04".equals(hardversion)) {//在线状态的离线充值机
				checkUserBindAndDispose(openid, equcode);
				return "redirect:/wxpay/offlineCardCharge?openid=" + openid + "&code=" + equcode;
			} else if ("03".equals(hardversion)) {//在线状态的脉冲投币
				checkUserBindAndDispose(openid, equcode);
				return "redirect:/wxpay/inCoins?openid=" + openid + "&code=" + equcode;
			} else {//在线状态的其他
				int send_15 = SendMsgUtil.send_15(equcode);
				if (send_15 == 0) {
					return "equipment/equipmentoffline";
				}
//				TemplateParent temp = templateService.getParentTemplateOne(tempid);
//				User user = userService.selectUserById(userEquipment.getUserId());
//				TemplateParent temp = templateService.getParentTemplateOne(equipment.getTempid());
				
				List<String> forbidport = new ArrayList<String>();
				if ("01".equals(hardversion)) {
//					temp = getTempInfo(temp, touristid, dealid);//判断是否为分等级模板
//					temp = getTempInfo(temp, userByOpenid.getId(), userEquipment.getUserId());
					forbidport = getAssignPotr(touristid, devicenum, dealid);
				}else if ("08".equals(hardversion) || "09".equals(hardversion) || "10".equals(hardversion)) {
//					temp = temp == null ? new TemplateParent() : temp;
//					if(CommUtil.toInteger(temp.getId()).equals(0)) {
//						List<TemplateParent>  tempdata = templateService.inquireTempByStatus(0 , 6);
//						if(tempdata.size()>0) temp = tempdata.get(0);
//					}
//					Integer nowtempid = CommUtil.toInteger(temp.getId());
//					Integer temporaryc = CommUtil.toInteger(temp.getPermit());
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
					model.addAttribute("temporaryc", walletpay);
					model.addAttribute("tempower", tempower);
					model.addAttribute("temtime", temtime);
					model.addAttribute("temmoney", temmoney);
				}
				model.addAttribute("forbidport", JSON.toJSONString(forbidport));
				if (DirectTemp != null && !subMer) {//主模板信息
					try {
						// 当设备不是脉冲设备且用户支持退费的情况下去引导用户关注
						if (!"03".equals(equi) && !"04".equals(equi) && permit == 1) {
							// 根据openid和accesstoken获取用户是否关注的信息
							String getUserInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="
									+ WeixinUtil.getBasicAccessToken() + "&openid=" + openid + "&lang=zh_CN";
							// 将get请求返回的信息，转换成JSON对象
							JSONObject doGetStr1 = WeixinUtil.doGetStr(getUserInfoUrl);
							// 判段用户的是否关注:0为未关注，1为已关注
							if ("0".equals(doGetStr1.getString("subscribe"))) {
								// 如果没有关注跳转到一个关注的页面
								return "attention";
							}
						}
					} catch (Exception e) {
						logger.warn("判断用户是否关注公众号失败===" + e.getMessage());
					}

//					String chargeInfo = CommUtil.toString(temp.getChargeInfo());
//					String brandname = CommUtil.toString(temp.getRemark());
//					tempphone = CommUtil.toString(temp.getCommon1());
//					tempphone = tempphone == null ? dealservephone : tempphone;
//					Integer walletpay = CommUtil.toInteger(temp.getWalletpay());
					model.addAttribute("brandname", brandname);
					model.addAttribute("chargeInfo", chargeInfo);
					model.addAttribute("phonenum", tempphone);
					model.addAttribute("ifwallet", 2);
				}/* else {
					model.addAttribute("chargeInfo", null);
					String servephone = CommUtil.toString(user.getServephone());
					if(servephone==null) servephone = CommUtil.toString(user.getPhoneNum());
					model.addAttribute("phonenum", servephone);
				}*/
				if (!"02".equals(hardversion)) {// 02 电轿款
					String checkMonthBackVal = checkMonthBackVal(dealid);
					if (!"2".equals(checkMonthBackVal)) {
						PackageMonth packageMonth = userService.selectPackageMonthByUid(touristid);
						packageMonth = packageMonth == null ? new PackageMonth() : packageMonth;
						model.addAttribute("packageMonth", packageMonth);
					} else {
						model.addAttribute("packageMonth", null);
					}
					model.addAttribute("ifmonth", checkMonthBackVal);
				} else {
					model.addAttribute("ifmonth", "0");
				}
				List<Map<String, String>> portStatus = new ArrayList<>();
				Map<String, String> codeRedisMap = JedisUtils.hgetAll(devicenum);
				if (codeRedisMap != null) {
					if ("02".equals(equipment.getHardversion())) {
						addPortStatus(2, codeRedisMap, portStatus);
					} else if ("01".equals(equipment.getHardversion())) {
						addPortStatus(10, codeRedisMap, portStatus);
					} else if ("05".equals(equipment.getHardversion())) {
						addPortStatus(16, codeRedisMap, portStatus);
					} else if ("06".equals(equipment.getHardversion())) {
						addPortStatus(20, codeRedisMap, portStatus);
					} else {
						addPortStatus(10, codeRedisMap, portStatus);
					}
				}
				if (portStatus.size() > 0) {
					model.addAttribute("allPortSize", portStatus.size());
					Map<String, String> map = portStatus.get(0);
					String updateTimeStr = map.get("updateTime");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					long time = sdf.parse(updateTimeStr).getTime();
//							long time = portStatus.get(0).getUpdateTime().getTime(); 
					long currentTime = System.currentTimeMillis();
					if ((currentTime - time) > 300000) {
						model.addAttribute("flag", true);
					} else {
						model.addAttribute("flag", false);
					}
					model.addAttribute("portStatus", portStatus);
				} else {
					model.addAttribute("allPortSize", 0);
					model.addAttribute("flag", true);
					model.addAttribute("portStatus", null);
				}
				//设备到期时间不为空且当前时间大于到期时间
				if(expirationTime != null && new Date().after(expirationTime)){
					model.addAttribute("errorinfo", "对不起，当前设备已到期");
					model.addAttribute("code", equcode);
					model.addAttribute("time", expirationTime);
					//提示用户设备到期更换设备
					return "chargeporterror";
				}
				model.addAttribute("allPortSize", portStatus.size());
				model.addAttribute("code", equcode);
				model.addAttribute("hardversion", equipment.getHardversion());
				model.addAttribute("equname", equipment.getRemark());
				model.addAttribute("areaname", equipment.getName());
				model.addAttribute("templatelist", tempson);
				Map<String, Object> defaulte = tempDefaultObje(tempson);
				model.addAttribute("defaultchoose", defaulte.get("defaultchoose"));
				model.addAttribute("defaultindex", defaulte.get("defaultindex"));
				model.addAttribute("nowtime", System.currentTimeMillis() + "");
				//checkUserBindAndDispose(openid, equcode);
				model.addAttribute("wolfstatus", "1");
				if("08".equals(hardversion) || "09".equals(hardversion) || "10".equals(hardversion)){
					return "chargeallportV3";
				}else{
					return "chargeallport2";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "openiderror";
		}

	}
	
	/*-- *** 请暂时不要在*号范围内添加   thanks ***********************************************************   --*/
	/**
	 * ORIGIN 测试代码（微信扫描端口进行充电） 
	 * @Description：用户公众外部扫描端口进行充电
	 * @param codeAndPort:设备端和口号
	 * @return
	 * @author： origin 2020年7月3日下午4:35:58
	 * @comment:
	 */
	@RequestMapping(value = "/scanAQRCode")
	public String scanAQRCode(String codeAndPort, Model model) {
		try {
			//微信code信息
			String code = CommUtil.toString(request.getParameter("code"));
			String existuser = CommUtil.toString(request.getParameter("existuser"));
			int length = codeAndPort.length();
			if (length == 7 || length == 8) {
				String val = codeAndPort.substring(6);
				int port = Integer.parseInt(val);
				if (length == 8 && port > 20) {
					model.addAttribute("errorinfo", "A二维码有误");
					return "chargeporterror";
				}
			} else {
				model.addAttribute("errorinfo", "B二维码有误");
				return "chargeporterror";
			}
			String equcode = codeAndPort.substring(0, 6);
			String port = codeAndPort.substring(6);
			// 根据设备号判断商户是否为特约商户
			boolean subMer = userEquipmentService.subMerByCode(equcode);
			logger.info("是不是特约商户"+"======="+subMer);
			JSONObject doGetStr = null;
			String openid = null;
			String accesstoken = null;
			User touristuser = null;
			
			if(subMer){
				String subOpenid = CommUtil.toString(request.getParameter("subOpenid"));
			    if(subOpenid == null){
			    	logger.info("1-使用服务商设备端口==="+codeAndPort+"支付参数=="+subOpenid);
			    	// 扫描服务商下的商家设备,就获取用户在服务商下的openid
			    	doGetStr = WeixinUtil.WeChatTokenSubMer(code, equcode);
			    	if(!doGetStr.has("openid")){
						model.addAttribute("openiderror", "1您的账户暂时无法访问");
						return "openiderror";
					}
					openid = doGetStr.getString("openid");
					accesstoken = doGetStr.getString("access_token");
					// 根据服务商的openid查询用户
					touristuser = userService.selectSubUserByOpenid(openid);
					logger.info("服务商下存在用户==="+(touristuser != null)+"设备信息=="+codeAndPort+"支付参数=="+openid);
					// 服务商用户表中没有用户信息,就重定向准备添加服务商下用户的openid
					if(touristuser == null){
						logger.info("开始重定向=="+codeAndPort+"subOpenid=="+openid);
						String url = "/oauth2PortPaySubUser?codeAndPort="+codeAndPort+"&subOpenid="+openid;
						return "redirect:"+url;
					}
					model.addAttribute("userinfo", touristuser);
					model.addAttribute("openid", openid);
			    }
			    // 重定向携带服务商下openid回来
			    if(subOpenid != null){
					// 对平台appid授权
			    	logger.info("2-重定向后====用户在服务商下openid=="+subOpenid);
					JSONObject heTengUserOpenid  = WeixinUtil.WeChatToken(code);
					if(!heTengUserOpenid.has("openid")){
						model.addAttribute("openiderror", "2您的账户暂时无法访问");
						return "openiderror";
					}
					// 根据平台appid获取平台用户的信息
					User user2 = userService.getUserByOpenid(heTengUserOpenid.getString("openid"));
					if(user2 == null){
						model.addAttribute("openiderror", "3请先关注公众号");
						return "openiderror";
					}
					// openid属于服务商openid
					openid = subOpenid;
					// 将用户的信息和服务商的openid进行存储
					User fuWuUser = new User();
					fuWuUser.setId(user2.getId());
					fuWuUser.setOpenid(subOpenid);
					userService.addSubUser(fuWuUser);
					// 查询服务商下的用户
					touristuser = userService.selectSubUserByOpenid(openid);
					if(touristuser == null){
						model.addAttribute("openiderror", "4您的账户暂时无法访问");
						return "openiderror";
					}
					logger.info("服务商返回前端数据==="+touristuser.getId()+"支付参数=="+openid);
					model.addAttribute("userinfo", touristuser);
					model.addAttribute("openid", openid);
				}
			}else{
				// 对平台appid授权
				doGetStr = WeixinUtil.WeChatToken(code);
				// 判断设备包含不包含openid
				if (!doGetStr.has("openid")) {
	                model.addAttribute("name", "'自助充电平台'");
					model.addAttribute("openiderror", "5您的账户暂时无法访问");
					return "openiderror";
				}
				openid = doGetStr.getString("openid");
				accesstoken = doGetStr.getString("access_token");
				try {
					// 根据openid和accesstoken获取用户是否关注的信息
					String getUserInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="
							+ WeixinUtil.getBasicAccessToken() + "&openid=" + openid + "&lang=zh_CN";
					// 将get请求返回的信息，转换成JSON对象
					JSONObject jsonObject = WeixinUtil.doGetStr(getUserInfoUrl);
					// 判段用户的是否关注:0为未关注，1为已关注
					if ("0".equals(jsonObject.getString("subscribe"))) {// 如果没有关注跳转到一个关注的页面
						return "attention";
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("判断用户是否关注公众号失败===" + e.getMessage());
				}
				checkUserAndDispose(openid, accesstoken, equcode);
				touristuser = userService.getUserByOpenid(openid);
				if(touristuser == null){
					model.addAttribute("openiderror", "6您的账户暂时无法访问");
					return "openiderror";
				}
				model.addAttribute("userinfo", touristuser);
				model.addAttribute("openid", openid);
			}
//			String openid = "odVHPwtil1qLhywcJ1fQ0NXCftb8";
			model.addAttribute("code", equcode);
			
			// 根据code查询出相应的设备信息
			Equipment equipment = equipmentService.getEquipmentAndAreaById(equcode);
			equipment = equipment == null ? new Equipment() : equipment;
			//设备信息
			String devicename = CommUtil.toString(equipment.getRemark());
			Integer devicestate = CommUtil.toInteger(equipment.getState());
			Integer devicebindtype = CommUtil.toInteger(equipment.getBindtype());
			String hardversion = CommUtil.toString(equipment.getHardversion());
			Integer tempid = CommUtil.toInteger(equipment.getTempid());
			Integer deviceareaid = CommUtil.toInteger(equipment.getAid());
			Date expirationTime= equipment.getExpirationTime();
			// 根据设备关联小区查询相关小区信息
			Area area = areaService.selectByIdArea(deviceareaid);
			area = area == null ? new Area() : area;
			Integer areaid = CommUtil.toInteger(area.getId());
			String areaname = CommUtil.toString(area.getName());
			
			//绑定商户
			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(equcode);
			userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
			Integer dealid = CommUtil.toInteger(userEquipment.getUserId());
			//绑定商户信息
			User dealeruser = new User();
			if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
			String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
			String servephone = CommUtil.toString(dealeruser.getServephone());
			//扫码用户信息
			//User touristuser = userService.getUserByOpenid(openid);
			touristuser = touristuser == null ? new User() : touristuser;
			Integer touristid = CommUtil.toInteger(touristuser.getId());
			Integer mercid = CommUtil.toInteger(touristuser.getMerid());
			Double touristbalance = CommUtil.toDouble(touristuser.getBalance());
			Double touristsendmoney = CommUtil.toDouble(touristuser.getSendmoney());
			
			//判断获取模板信息
			Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, dealid, equcode, hardversion);
			String brandname = CommUtil.toString(DirectTemp.get("remark"));
			String temphone = CommUtil.toString(DirectTemp.get("common1"));
			servephone = basicsService.getServephoneData(temphone, null, servephone, dealerphne);
			model.addAttribute("phonenum", servephone);
			
			Integer nowtempid = CommUtil.toInteger(DirectTemp.get("id"));
			Integer temporaryc = CommUtil.toInteger(DirectTemp.get("permit"));
			String chargeInfo = CommUtil.toString(DirectTemp.get("chargeInfo"));
			Integer walletpay = CommUtil.toInteger(DirectTemp.get("walletpay"));
			List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
			tempson = tempson == null ? new ArrayList<>() : tempson;
			Map<String, Object> defaulte = tempDefaultObje(tempson);
			model.addAttribute("defaultchoose", defaulte.get("defaultchoose"));
			model.addAttribute("defaultindex", defaulte.get("defaultindex"));
			//判断获取模板信息
//			String tempphone = null;
//			TemplateParent temp = templateService.getParentTemplateOne(tempid);
			if ("08".equals(hardversion)) {
//				temp = temp == null ? new TemplateParent() : temp;
//				if(CommUtil.toInteger(temp.getId()).equals(0)) {//判断查询的模板是否存在
//					List<TemplateParent>  tempdata = templateService.inquireTempByStatus(0 , 6);
//					if(tempdata.size()>0) temp = tempdata.get(0);
//				}
//				Integer nowtempid = CommUtil.toInteger(temp.getId());
//				Integer temporaryc = CommUtil.toInteger(temp.getPermit());
//				List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
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
				model.addAttribute("temporaryc", walletpay);
				model.addAttribute("tempower", tempower);
				model.addAttribute("temtime", temtime);
				model.addAttribute("temmoney", temmoney);
			}/*else{
				temp = temp == null ? new TemplateParent() : temp;
				if(CommUtil.toInteger(temp.getId()).equals(0)) {
//					temp = templateService.getParentTemplateOne(407);//正式充电系统模板id为0
					List<TemplateParent>  tempdata = templateService.inquireTempByStatus(0 , 0);
					if(tempdata.size()>0) temp = tempdata.get(0);
				}
			}*/
//			temp = temp == null ? new TemplateParent() : temp;
//			tempphone = tempphone == null ? dealservephone : tempphone;
//			String chargeInfo = CommUtil.toString(temp.getChargeInfo());
//			String brandname = CommUtil.toString(temp.getRemark());
//			Integer walletpay = CommUtil.toInteger(temp.getWalletpay());
				
			
			model.addAttribute("existuser", existuser);
			model.addAttribute("equipment", equipment);
			model.addAttribute("area", area);
			//model.addAttribute("userinfo", touristuser);

			model.addAttribute("code", equcode);
			model.addAttribute("port", port);
			model.addAttribute("hardversion", hardversion);
			model.addAttribute("equname", devicename);
			model.addAttribute("areaname", areaname);
			
			model.addAttribute("chargeInfo", chargeInfo);
			model.addAttribute("brandname", brandname);
			model.addAttribute("ifwallet", walletpay);

			Integer iswallet = 1;
			if(!dealid.equals(mercid)) iswallet = 2;
			model.addAttribute("iswallet", iswallet);
			model.addAttribute("balance", touristbalance);
			model.addAttribute("sendmoney", touristsendmoney);
			//设备到期时间不为空且当前时间大于到期时间
			if(expirationTime != null && new Date().after(expirationTime)){
				model.addAttribute("errorinfo", "对不起，当前设备已到期");
				model.addAttribute("time", expirationTime);
				//提示用户设备到期更换设备
				return "chargeporterror";
			}
//			if (!"01".equals(hardversion) && !"05".equals(hardversion) && !"06".equals(hardversion)
//					&& !"00".equals(hardversion) && !"02".equals(hardversion) && !"07".equals(hardversion)) {
//				model.addAttribute("errorinfo", "当前充电站不支持扫描端口充电");
//				return "chargeporterror";
//			}
			if ("03".equals(hardversion) || "04".equals(hardversion) ) {
				model.addAttribute("errorinfo", "当前充电站不支持扫描端口充电");
				return "chargeporterror";
			}
			if (devicestate.equals(0)) {
				return "equipment/equipmentoffline";
			} else if (devicebindtype.equals(0)) {
//				temp(equcode, null, model);
				return "equipment/equipmentunbind";
			} else if (dealid.equals(0)) {
//				temp(equcode, null, model);
				return "equipment/equipmentunbind";
			} else {
				List<String> forbidport = new ArrayList<String>();
//				temp = getTempInfo(temp, touristid, dealid);
				forbidport = getAssignPotr(touristid, equipment.getCode(),
						userEquipment.getUserId());
				model.addAttribute("forbidport", JSON.toJSONString(forbidport));
				String checkMonthBackVal = checkMonthBackVal(dealid);
				if (!"2".equals(checkMonthBackVal)) {
					PackageMonth packageMonth = userService.selectPackageMonthByUid(touristid);
					model.addAttribute("packageMonth", packageMonth);
				} else {
					model.addAttribute("packageMonth", null);
				}
//						int portval = Integer.parseInt(port);
//						AllPortStatus portStatus = allPortStatusService.findPortStatusByEquipmentnumAndPort(equcode,
//								portval);
//						if (portStatus != null) {
//							long time = portStatus.getUpdateTime().getTime();
//							long currentTime = System.currentTimeMillis();
//							model.addAttribute("portStatus", portStatus.getPortStatus());
//							if ((currentTime - time) > 300000) {
//								model.addAttribute("flag", true);
//							} else {
//								model.addAttribute("flag", false);
//							}
//						} else {
//							model.addAttribute("portStatus", 0);
//							model.addAttribute("flag", true);
//						}
				Map<String, String> codeRedisMap = JedisUtils.hgetAll(equcode);
				if (codeRedisMap != null) {
					String portMapStr = codeRedisMap.get(port);
					Map<String,String> portMap = (Map<String, String>) JSON.parse(portMapStr);
					if (portMap != null) {
						String updateTimeStr = portMap.get("updateTime");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						long time = sdf.parse(updateTimeStr).getTime();
						long currentTime = System.currentTimeMillis();
						model.addAttribute("portStatus", portMap.get("portStatus"));
						if ((currentTime - time) > 300000) {
							model.addAttribute("flag", true);
						} else {
							model.addAttribute("flag", false);
						}
					} else {
						model.addAttribute("portStatus", 0);
						model.addAttribute("flag", true);
					}
				} else {
					model.addAttribute("portStatus", 0);
					model.addAttribute("flag", true);
				}
				model.addAttribute("ifmonth", checkMonthBackVal);
				model.addAttribute("templatelist", tempson);
//				temp(equcode, temp, model);
				model.addAttribute("nowtime", System.currentTimeMillis() + "");
				checkUserBindAndDispose(openid, equcode);
				model.addAttribute("wolfstatus", "1");
//				return "chargeport1";
				if("08".equals(hardversion) || "09".equals(hardversion) || "10".equals(hardversion)){
					return "chargeportV3";
				}else{
					return "chargeport1";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorinfo", "扫码失败，请重新扫码" + e.getMessage());
			return "chargeporterror";
		}
	}
	
	/**
	 * ORIGIN 测试代码（支付宝扫描端口进行充电） 
	 * @Description：用户公众外部扫描端口进行充电
	 * @param codeAndPort:设备端和口号
	 * @return
	 * @author： origin 2020年7月3日下午4:35:58
	 * @comment:
	 */
	@RequestMapping(value = "/commscanAQRCode")
	@ResponseBody
	public Map<String, Object> commscanAQRCode(String codeAndPort, Model model) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			String devicenum = null;
			Integer port = null;
			int length = CommUtil.trimToEmpty(codeAndPort).length();
			if (length == 7 || length == 8) {
				devicenum = codeAndPort.substring(0, 6);
				port = Integer.parseInt(codeAndPort.substring(6));
				if (length == 8 && port > 20) {
					resultdata.put("errorinfo", "A二维码有误");
					return resultdata;
				}
			} else {
				resultdata.put("errorinfo", "B二维码有误");
				return resultdata;
			}
			Map<String, Object> deviceinfo = equipmentService.getDeviceRelevanceInfo(devicenum);
			//设备信息
//			String devicenum = CommUtil.toString(deviceinfo.get("code"));
			String hardversion = CommUtil.toString(deviceinfo.get("hardversion"));
			String devicename = CommUtil.toString(deviceinfo.get("remark"));
			String devicecsq = CommUtil.toString(deviceinfo.get("csq"));
			Integer bindtype = CommUtil.toInteger(deviceinfo.get("bindtype"));
			Integer state = CommUtil.toInteger(deviceinfo.get("state"));
			Integer several = CommUtil.toInteger(deviceinfo.get("several"));
			Integer aid = CommUtil.toInteger(deviceinfo.get("aid"));
			String expirationtime = CommUtil.toString(deviceinfo.get("expiration_time"));
			Integer tempid = CommUtil.toInteger(deviceinfo.get("tempid"));
			
			//商户信息
			Integer merid = CommUtil.toInteger(deviceinfo.get("merid"));
			String dealnick = CommUtil.toString(deviceinfo.get("mernick"));
			String realname = CommUtil.toString(deviceinfo.get("realname"));
			String openid = CommUtil.toString(deviceinfo.get("openid"));
			String merphone = CommUtil.toString(deviceinfo.get("merphone"));
			String merservephone = CommUtil.toString(deviceinfo.get("merservephone"));
			Integer payhint = CommUtil.toInteger(deviceinfo.get("payhint"));
			Integer submer = CommUtil.toInteger(deviceinfo.get("submer"));
			
			//小区信息
			String areaname = CommUtil.toString(deviceinfo.get("areaname"));
			String areaaddress = CommUtil.toString(deviceinfo.get("areaaddress"));
			Integer waltempid = CommUtil.toInteger(deviceinfo.get("waltempid"));
			Integer onltempid = CommUtil.toInteger(deviceinfo.get("onltempid"));
			
			Date expirationDate = CommUtil.DateTime(expirationtime, "yyyy-MM-dd HH:mm:ss");
			Date datatime = new Date();
			
			//设备到期时间不为空且当前时间大于到期时间
			if(expirationDate != null && datatime.after(expirationDate)){
				resultdata.put("message", "对不起，当前设备已到期");
				resultdata.put("errorinfo", "对不起，当前设备已到期");
				resultdata.put("code", devicenum);
				resultdata.put("time", expirationtime);
				//提示用户设备到期更换设备
				return resultdata;
			}
			//设备绑定状态bindtype  1、已绑定0、未绑定 			设备在线状态state     1、在线0、不在线 
			if(state==0){//判断设备和设备状态是否正常
//				return "equipment/equipmentoffline";
			} else if (bindtype.equals(0) || merid.equals(0)) {//判断设备是否被绑定，此状态是未被绑定
				
			} else if(bindtype==0){
				
			}
			
			/*
			if(devicestate.equals(0)){//判断设备和设备状态是否正常
				return "equipment/equipmentoffline";
			} else if (devicebindtype.equals(0) || dealid.equals(0)) {//判断设备是否被绑定
				if (!"00".equals(softversionnum) && "00".equals(hardversion)) {
					return "selectHardversion";
				} else if ("03".equals(hardversion)) {
					return "testpay";
				} else if ("02".equals(hardversion)) {
					model.addAttribute("defaultchoose", nowtempid);
					return "equipment/equipmentunbind";
				} else if ("04".equals(hardversion)) {
					return "redirect:/wxpay/offlineCardCharge?openid=" + openid + "&code=" + equcode;
				} else {
					temp(equcode, null, model);
					return "equipment/equipmentunbind";
				}
			} else if ("04".equals(hardversion)) {//在线状态的离线充值机
				checkUserBindAndDispose(openid, equcode);
				return "redirect:/wxpay/offlineCardCharge?openid=" + openid + "&code=" + equcode;
			} else if ("03".equals(hardversion)) {//在线状态的脉冲投币
				checkUserBindAndDispose(openid, equcode);
				return "redirect:/wxpay/inCoins?openid=" + openid + "&code=" + equcode;
			}
			 */
			
			
			
//			model.addAttribute("code", equcode);
//			model.addAttribute("port", port);
//			// 根据code查询出相应的设备信息
//			Equipment equipment = equipmentService.getEquipmentAndAreaById(equcode);
//			equipment = equipment == null ? new Equipment() : equipment;
//			//设备信息
//			String devicename = CommUtil.toString(equipment.getRemark());
//			Integer devicestate = CommUtil.toInteger(equipment.getState());
//			Integer devicebindtype = CommUtil.toInteger(equipment.getBindtype());
//			String hardversion = CommUtil.toString(equipment.getHardversion());
//			Integer tempid = CommUtil.toInteger(equipment.getTempid());
//			Integer deviceareaid = CommUtil.toInteger(equipment.getAid());
//			Date expirationTime= equipment.getExpirationTime();
//			// 根据设备关联小区查询相关小区信息
//			Area area = areaService.selectByIdArea(deviceareaid);
//			area = area == null ? new Area() : area;
//			Integer areaid = CommUtil.toInteger(area.getId());
//			String areaname = CommUtil.toString(area.getName());
//			model.addAttribute("equipment", equipment);
//			model.addAttribute("area", area);
//
//			model.addAttribute("hardversion", hardversion);
//			model.addAttribute("equname", devicename);
//			model.addAttribute("areaname", areaname);
//			
//			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(equcode);
//			userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
//			Integer dealid = CommUtil.toInteger(userEquipment.getUserId());
//			User dealeruser = new User();
//			if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
//			String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
//			String servephone = CommUtil.toString(dealeruser.getServephone());
//
//			Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, dealid, equcode, hardversion);
//			String brandname = CommUtil.toString(DirectTemp.get("remark"));
//			String temphone = CommUtil.toString(DirectTemp.get("common1"));
//			servephone = basicsService.getServephoneData(temphone, null, servephone, dealerphne);
//			Integer nowtempid = CommUtil.toInteger(DirectTemp.get("id"));
//			Integer temporaryc = CommUtil.toInteger(DirectTemp.get("permit"));
//			String chargeInfo = CommUtil.toString(DirectTemp.get("chargeInfo"));
//			Integer walletpay = CommUtil.toInteger(DirectTemp.get("walletpay"));
//			Integer ifalipay = CommUtil.toInteger(DirectTemp.get("ifalipay"));
//			List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
//			tempson = tempson == null ? new ArrayList<>() : tempson;
//			model.addAttribute("phonenum", servephone);
//			model.addAttribute("brandname", brandname);
//			model.addAttribute("DirectTemp", DirectTemp);
//			model.addAttribute("templatelist", tempson);
//			if(ifalipay.equals(2)){
//				if(!hardversion.equals("03") && !hardversion.equals("04")){
//					return "alipay/errorChargeInfo";
//				}
//			}
//			if (!"01".equals(hardversion) && !"05".equals(hardversion) && !"06".equals(hardversion)
//					&& !"00".equals(hardversion) && !"02".equals(hardversion) && !"07".equals(hardversion)) {
//				model.addAttribute("errorinfo", "当前充电站不支持扫描端口充电");
//				return "chargeporterror";
//			}
//			String auth_code = request.getParameter("auth_code");
//			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
//					AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,
//					AlipayConfig.SIGNTYPE); 
//			AlipaySystemOauthTokenRequest requestAlipay = new AlipaySystemOauthTokenRequest();
//			requestAlipay.setCode(auth_code);
//			requestAlipay.setGrantType("authorization_code");
//			try {
//			    AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(requestAlipay);
//			    System.out.println("userid===" + oauthTokenResponse.getUserId());
//			    model.addAttribute("userid", oauthTokenResponse.getUserId());
//			} catch (AlipayApiException e) {
//			    //处理异常
//			    e.printStackTrace();
//			}
//
////			if (equipment == null || equipment.getState() == 0) {
////				return "equipment/equipmentoffline";
////			} else if (equipment.getBindtype() == null || equipment.getBindtype() == 0) {
////				return "equipment/equipmentunbind";
////			} 
//			if(devicestate.equals(0)){//判断设备和设备状态是否正常
//				return "equipment/equipmentoffline";
//			} else if (devicebindtype.equals(0) || dealid.equals(0)) {//判断设备是否被绑定
//				model.addAttribute("existuser", request.getParameter("existuser"));
//				return "equipment/equipmentunbind";
//			} else {
//				SendMsgUtil.send_15(equcode);
//				//设备到期时间不为空且当前时间大于到期时间
//				if(expirationTime != null && new Date().after(expirationTime)){
//					model.addAttribute("errorinfo", "对不起，当前设备已到期");
//					model.addAttribute("time", expirationTime);
//					//提示用户设备到期更换设备
//					return "chargeporterror";
//				}
////				temp(equcode, model);
//				Map<String, Object> defaulte = tempDefaultObje(tempson);
//				model.addAttribute("defaultchoose", defaulte.get("defaultchoose"));
//				model.addAttribute("defaultindex", defaulte.get("defaultindex"));
//				model.addAttribute("nowtime", System.currentTimeMillis() + "");
//				return "alipay/chargeport";
//			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorinfo", "扫码失败，请重新扫码");
			return resultdata;
		}
		return resultdata;
	}
	
	/**
	 * ORIGIN 测试代码（支付宝扫描端口进行充电） 
	 * @Description：用户公众外部扫描端口进行充电
	 * @param codeAndPort:设备端和口号
	 * @return
	 * @author： origin 2020年7月3日下午4:35:58
	 * @comment:
	 */
	@RequestMapping(value = "/alipayscanAQRCode")
	public String alipayscanAQRCode(String codeAndPort, Model model) {
		try {
			int length = codeAndPort.length();
			if (length == 7 || length == 8) {
				String val = codeAndPort.substring(6);
				int port = Integer.parseInt(val);
				if (length == 8 && port > 20) {
					model.addAttribute("errorinfo", "A二维码有误");
					return "chargeporterror";
				}
			} else {
				model.addAttribute("errorinfo", "B二维码有误");
				return "chargeporterror";
			}
			String equcode = codeAndPort.substring(0, length-1);
			String port = codeAndPort.substring(length-1);
			model.addAttribute("code", equcode);
			model.addAttribute("port", port);
			// 根据code查询出相应的设备信息
			Equipment equipment = equipmentService.getEquipmentAndAreaById(equcode);
			equipment = equipment == null ? new Equipment() : equipment;
			//设备信息
			String devicename = CommUtil.toString(equipment.getRemark());
			Integer devicestate = CommUtil.toInteger(equipment.getState());
			Integer devicebindtype = CommUtil.toInteger(equipment.getBindtype());
			String hardversion = CommUtil.toString(equipment.getHardversion());
			Integer tempid = CommUtil.toInteger(equipment.getTempid());
			Integer deviceareaid = CommUtil.toInteger(equipment.getAid());
			Date expirationTime= equipment.getExpirationTime();
			// 根据设备关联小区查询相关小区信息
			Area area = areaService.selectByIdArea(deviceareaid);
			area = area == null ? new Area() : area;
			Integer areaid = CommUtil.toInteger(area.getId());
			String areaname = CommUtil.toString(area.getName());
			model.addAttribute("equipment", equipment);
			model.addAttribute("area", area);

			model.addAttribute("hardversion", hardversion);
			model.addAttribute("equname", devicename);
			model.addAttribute("areaname", areaname);
			
			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(equcode);
			userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
			Integer dealid = CommUtil.toInteger(userEquipment.getUserId());
			User dealeruser = new User();
			if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
			String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
			String servephone = CommUtil.toString(dealeruser.getServephone());

			Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, dealid, equcode, hardversion);
			String brandname = CommUtil.toString(DirectTemp.get("remark"));
			String temphone = CommUtil.toString(DirectTemp.get("common1"));
			servephone = basicsService.getServephoneData(temphone, null, servephone, dealerphne);
			Integer nowtempid = CommUtil.toInteger(DirectTemp.get("id"));
			Integer temporaryc = CommUtil.toInteger(DirectTemp.get("permit"));
			String chargeInfo = CommUtil.toString(DirectTemp.get("chargeInfo"));
			Integer walletpay = CommUtil.toInteger(DirectTemp.get("walletpay"));
			Integer ifalipay = CommUtil.toInteger(DirectTemp.get("ifalipay"));
			List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
			tempson = tempson == null ? new ArrayList<>() : tempson;
			model.addAttribute("phonenum", servephone);
			model.addAttribute("brandname", brandname);
			model.addAttribute("DirectTemp", DirectTemp);
			model.addAttribute("templatelist", tempson);
			if(ifalipay.equals(2)){
				if(!hardversion.equals("03") && !hardversion.equals("04")){
					return "alipay/errorChargeInfo";
				}
			}
			if (!"01".equals(hardversion) && !"05".equals(hardversion) && !"06".equals(hardversion)
					&& !"00".equals(hardversion) && !"02".equals(hardversion) && !"07".equals(hardversion)) {
				model.addAttribute("errorinfo", "当前充电站不支持扫描端口充电");
				return "chargeporterror";
			}
			String auth_code = request.getParameter("auth_code");
			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
					AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,
					AlipayConfig.SIGNTYPE); 
			AlipaySystemOauthTokenRequest requestAlipay = new AlipaySystemOauthTokenRequest();
			requestAlipay.setCode(auth_code);
			requestAlipay.setGrantType("authorization_code");
			try {
			    AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(requestAlipay);
			    System.out.println("userid===" + oauthTokenResponse.getUserId());
			    model.addAttribute("userid", oauthTokenResponse.getUserId());
			} catch (AlipayApiException e) {
			    //处理异常
			    e.printStackTrace();
			}

//			if (equipment == null || equipment.getState() == 0) {
//				return "equipment/equipmentoffline";
//			} else if (equipment.getBindtype() == null || equipment.getBindtype() == 0) {
//				return "equipment/equipmentunbind";
//			} 
			if(devicestate.equals(0)){//判断设备和设备状态是否正常
				return "equipment/equipmentoffline";
			} else if (devicebindtype.equals(0) || dealid.equals(0)) {//判断设备是否被绑定
				model.addAttribute("existuser", request.getParameter("existuser"));
				return "equipment/equipmentunbind";
			} else {
				SendMsgUtil.send_15(equcode);
				//设备到期时间不为空且当前时间大于到期时间
				if(expirationTime != null && new Date().after(expirationTime)){
					model.addAttribute("errorinfo", "对不起，当前设备已到期");
					model.addAttribute("time", expirationTime);
					//提示用户设备到期更换设备
					return "chargeporterror";
				}
//				temp(equcode, model);
				Map<String, Object> defaulte = tempDefaultObje(tempson);
				model.addAttribute("defaultchoose", defaulte.get("defaultchoose"));
				model.addAttribute("defaultindex", defaulte.get("defaultindex"));
				model.addAttribute("nowtime", System.currentTimeMillis() + "");
				return "alipay/chargeport";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorinfo", "扫码失败，请重新扫码");
			return "chargeporterror";
		}
	}
	/*-- *** 请暂时不要在*号范围内添加   thanks ***********************************************************   --*/
	
}
