package com.hedong.hedongwx.web.controller.hduser;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.ParseException;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.Data;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.hedong.hedongwx.config.AlipayConfig;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.Card;
import com.hedong.hedongwx.entity.CardRecord;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.GeneralDetail;
import com.hedong.hedongwx.entity.InCoins;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.Money;
import com.hedong.hedongwx.entity.OfflineCard;
import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.OnlineCardRecord;
import com.hedong.hedongwx.entity.PackageMonth;
import com.hedong.hedongwx.entity.PackageMonthRecord;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.service.AllPortStatusService;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.AuthorityService;
import com.hedong.hedongwx.service.CardRecordService;
import com.hedong.hedongwx.service.CardService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.FeescaleService;
import com.hedong.hedongwx.service.GeneralDetailService;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.service.MoneyService;
import com.hedong.hedongwx.service.OfflineCardService;
import com.hedong.hedongwx.service.OnlineCardRecordService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.thread.Server;
import com.hedong.hedongwx.utils.ByteUtils;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.JedisUtils;
import com.hedong.hedongwx.utils.JsSignUtil;
import com.hedong.hedongwx.utils.MD5Util;
import com.hedong.hedongwx.utils.MobileSendUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.SendMsgUtil;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.TempMsgUtil;
import com.hedong.hedongwx.utils.WeiXinConfigParam;
import com.hedong.hedongwx.utils.WeixinUtil;
import com.hedong.hedongwx.utils.WolfHttpRequest;
import com.hedong.hedongwx.utils.XMLUtil;
import com.hedong.hedongwx.web.controller.WxPayController;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/general")//手机端普通用户
public class GeneralController {

	private final Logger logger = LoggerFactory.getLogger(GeneralController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private CardService cardService;
	@Autowired
	private CardRecordService cardRecordService;
	@Autowired
	private GeneralDetailService generalDetailService;
	@Autowired
	private MerchantDetailService merchantDetailService;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private OfflineCardService offlineCardService;
	@Autowired
	private InCoinsService inCoinsService;
	@Autowired
	private TradeRecordService tradeRecordService; 
	@Autowired
	private OnlineCardService onlineCardService;
	@Autowired
	private OnlineCardRecordService onlineCardRecordService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private AllPortStatusService allPortStatusService;
	@Autowired
	private FeescaleService feescaleService; 
	@Autowired
	private AuthorityService authorityService;
	
	@RequestMapping(value="/getCaptchaData")
	@ResponseBody
    public Object getCaptchaData(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			String phone =  CommUtil.toString(maparam.get("mobile"));
			String captcha = StringUtil.getRandNum();
			String[] params = { captcha, "3" };
			String[] cellphone = { phone };
			MobileSendUtil.TemplateMobileSend(params, cellphone);
			datamap.put("captcha", captcha);
			datamap.put("dataTime", new Data());
			datamap.put("sendtime", CommUtil.toDateTime());
			return CommUtil.responseBuildInfo(200, "验证码发送成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**
	 * 保存手机号
	 * @param uid 用户id
	 * @param phone 手机号
	 * @param checkcode 
	 * @param authcode 
	 * @param sendtime 
	 * @return
	 */
	@RequestMapping(value = "/savePhone")
	@ResponseBody
	public Object savePhone(Integer uid, String phone, String checkcode, String authcode, String sendtime){
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			if(!checkcode.equals(authcode)){
				return CommUtil.responseBuildInfo(102, "验证码不正确", datamap);
			}
			if(sendtime!=null){
//				long time = Long.parseLong(sendtime);
				Date times = CommUtil.DateTime(sendtime, "yyyy-MM-dd HH:mm:ss");
				int compTime = StringUtil.comparTime(new Date().getTime(),times.getTime());
				if(compTime>3){
					return CommUtil.responseBuildInfo(104, "验证码超时，请重新输入！", datamap);
				}
			}else{
				return CommUtil.responseBuildInfo(104, "时间验证失败！", datamap);
			}
			User user = userService.existAdmin(phone);
			if(user!=null){
				datamap.put("resultuser", user);
				return CommUtil.responseBuildInfo(102, "手机号已被占用", datamap);
			}
			User tourist = userService.editServicePhone(uid, phone);
			datamap.put("resultuser", tourist);
			return CommUtil.responseBuildInfo(200, "添加成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	} 

	/**
	 * 保存用户的手机号
	 * @param uid 用户的id 
	 * @param phone 手机号码
	 * @return {@link Object}
	 */
	@RequestMapping(value = "/savePhone2")
	@ResponseBody
	public Object savePhone2(Integer uid, String phone){
		Map<String, Object> datamap = new HashMap<String, Object>();
		// 参数
		if(uid == null){
			return CommUtil.responseBuildInfo(301, "参数错误", datamap);
		}
		if(phone == null || "".equals(phone)){
			return CommUtil.responseBuildInfo(301, "参数错误", datamap);
		}
		User user = userService.existAdmin(phone);
		if(user != null){
			datamap.put("resultuser", user);
			return CommUtil.responseBuildInfo(102, "手机号已被占用", datamap);
		}
		try {
			User tourist = userService.editServicePhone(uid, phone);
			datamap.put("resultuser", tourist);
			return CommUtil.responseBuildInfo(200, "添加成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	} 
	
	
	/**
	 * 用户个人中心
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index(Model model) {
		
//		String openid = "odVHPwsQE-vkZnp697UUPslMZN6k";
//		User newUsers = userService.getUserByOpenid(openid);
//		this.request.getSession().setAttribute("user", newUsers);
		
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			user = userService.selectUserById(user.getId());
			if (user != null) {
				String numerical = StringUtil.StringNumer(CommUtil.toString(user.getId()));
				model.addAttribute("numerical", numerical);
				//model.addAttribute("user", user);
				model.addAttribute("homeclick", 1);
				model.addAttribute("areaname", null);
				Integer aid = CommUtil.toInteger(user.getAid());
				Integer merid = CommUtil.toInteger(user.getMerid());
				String phoneNum = null;
				String areaname = null;
				String servephone = null;
				User merUser = userService.selectUserById(merid);
				if (!aid.equals(0)) {
					Area area = areaService.selectByAreaId(aid);
					area = area == null ? new Area() : area;
					areaname = CommUtil.toString(area.getName());
				}
				model.addAttribute("areaname", areaname);
				
				if (!merid.equals(0) && merid > 0) {
					User dealeruser = userService.selectUserById(merid);
					dealeruser = dealeruser == null ? new User() :dealeruser;
					phoneNum = CommUtil.toString(dealeruser.getPhoneNum());
					servephone = CommUtil.toString(dealeruser.getServephone());
				}
				model.addAttribute("phoneNum", phoneNum);
				model.addAttribute("servephone", servephone);
				// 查询用户所属的商户
				if(merid != 0){
					// 微信code
					String wxCode = CommUtil.toString(request.getParameter("code"));
					// 重定向回来的标识
					Integer identify = CommUtil.toInteger(request.getParameter("identify"));
					// 判断商户是否为特约商
					if(merUser != null && merUser.getSubMer().equals(1)){
						try {
							if(identify == 0 && wxCode == null){
								logger.info("1-开始重定向用户的所属商户是特约商户");
								String url = "/insideIndex?identify="+merid;
								return "redirect:"+url;
							}
							if(identify != 0 && wxCode != null){
								logger.info("2-重定向回来用户登陆个人中心");
								JSONObject doGetStr = WeixinUtil.WeChatTokenSubMerByMerId(wxCode, merid);
								if(!doGetStr.has("openid")){
									model.addAttribute("openiderror", "2您的账户暂时无法访问");
									return "openiderror";
								}
								// 根据服务商appid查询用户信息
								User subUser = userService.selectSubUserByOpenid(doGetStr.getString("openid"));
								if(subUser == null && user != null){
									// 添加服务商下的用户
									User fuWuUser = new User();
									fuWuUser.setId(user.getId());
									fuWuUser.setOpenid(doGetStr.getString("openid"));
									userService.addSubUser(fuWuUser);
									// 将用户的openid换成服务商下的openid
									user.setOpenid(doGetStr.getString("openid"));
									// 特约商户下的用户首次钱包充值
									model.addAttribute("user", user);
								}else{
									// 用户存在
									model.addAttribute("user", subUser);
								}
							}
						} catch (ParseException e) {
							logger.info("发生异常");
							e.printStackTrace();
						} catch (IOException e) {
							logger.info("发生异常");
							e.printStackTrace();
						}
					}else{
						// 用户存在商家,商家不是特约商户
						model.addAttribute("user", user);
					}
				}else{
					//用户不存在商家
					model.addAttribute("user", user);
				}
				
				return "general/index";
			} else {
				return "erroruser";
			}
		} else {
			return "erroruser";
		}
	}
	
	//*************************************************************************************	
	/**
	 * 进入个人信息界面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/generalinfo")
	public String generalinfo(Integer uid, Model model) {
		try {
			if(CommUtil.toInteger(uid)!=0){
				User user = userService.selectUserById(uid);
				String numerical = StringUtil.StringNumer(CommUtil.toInteger(uid).toString());
				model.addAttribute("numerical", numerical);
				model.addAttribute("user", user);
				return "general/egocenter";
			}else{
				return "erroruser";
			}
		}  catch (Exception e) {
			logger.info(CommUtil.getExceptInfo(e));
			return "erroruser";
		}
	}
	
	/**
	 * 进入注册页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/generalregist")
	public String regist(Model model) {
		model.addAttribute("rank", 3);
		return "general/regist";
	}
	
	/**
	 * 用户信息修改、添加
	 * @return
	 */
	@RequestMapping(value = "/saveuser")
	@ResponseBody
	public String saveuser(HttpServletRequest request, HttpServletResponse response) {
		String num = userService.saveuser(request);
		return num;
	}
	
	public User user( String openid){
		User user = (User) request.getSession().getAttribute("user");
		if(user==null){
			user = userService.getUserByOpenid(openid);
			request.getSession().setAttribute("user", user);
		}
		return user;
	}
	
	@RequestMapping("/scanQrcodeBingMer")
	@ResponseBody
	public String scanQrcodeBingMer(String code) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (user != null) {
				UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
				if (userEquipment != null) {
					User user2 = new User();
					user2.setId(user.getId());
					user2.setMerid(userEquipment.getUserId());
					Equipment equipment = equipmentService.getEquipmentById(code);
					Integer aid = equipment.getAid();
					if (aid != null && aid != 0) {
						user2.setAid(aid);
					}
					userService.updateUserById(user2);
					return "1";
				} else {
					return "0";
				}
			} else {
				return "2";
			}
		} catch (Exception e) {
			return "3";
		}
	}
	
	@RequestMapping("/checkBindInfo")
	public String checkBindInfo(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			user = userService.selectUserById(user.getId());
			request.getSession().setAttribute("user", user);
			Integer merid = user.getMerid();
			if (merid != null && merid > 0) {
				User merUser = userService.selectUserById(merid);
				model.addAttribute("merUser", merUser);
			}
			return "general/checkBindInfo";
		} else {
			return "erroruser";
		}
	}
	
	@RequestMapping("/userBindMid")
	@ResponseBody
	public String userBindMid(String phonenum, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			Integer merid = user.getMerid();
			if (merid != null && merid > 0) {
				return "2";
			} else {
				User userByPhoneNum = userService.getUserByPhoneNum(phonenum);
				if (userByPhoneNum == null) {
					return "3";
				} else {
					User user2 = new User();
					user2.setId(user.getId());
					user2.setMerid(userByPhoneNum.getId());
					userService.updateUserById(user2);
					return "1";
				}
			}
		} else {
			return "0";
		}
	}

	/**
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/check" })
	public String check(Model model) throws Exception {
		String code = this.request.getParameter("code");
		// 利用code获取openid和access_token
		if (code == null || "".equals(code)) {
			return "erroruser";
		}
		String str = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WeiXinConfigParam.APPID
				+ "&secret=" + WeiXinConfigParam.APPSECRET + "&code=" + code + "&grant_type=authorization_code";
		JSONObject doGetStr = WeixinUtil.doGetStr(str);
		if (!doGetStr.has("openid")) {
			logger.warn("用户登陆失败：" + doGetStr.toString());
			return "erroruser";
		}
		String openid = doGetStr.getString("openid");
		if (openid != null) {
			User userByOpenid = this.userService.getUserByOpenid(openid);
			if (userByOpenid == null) {
				String accesstoken = doGetStr.getString("access_token");
				//获取用户信息
				String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accesstoken + "&openid="
						+ openid + "&lang=zh_CN";
				
				JSONObject doGetStr1 = WeixinUtil.doGetStr(getUserInfoUrl);
				String nickname = doGetStr1.getString("nickname");
				User user = new User();
				user.setUsername(nickname);
				user.setOpenid(openid);
				user.setEarnings(Double.valueOf(0.00));
				user.setLevel(1);
				user.setCreateTime(new Date());
				this.userService.addUser(user);
				model.addAttribute("openiderror", "正在开发，暂时无法访问");
				return "openiderror";
//				this.request.getSession().setAttribute("user", this.userService.getUserByOpenid(openid));
//				return "redirect:/general/index";
			}else{
				int rank = userByOpenid.getLevel();
				if(rank==1){
					model.addAttribute("openiderror", "正在开发，暂时无法访问");
					return "openiderror";
				}else{
					this.request.getSession().setAttribute("user", userByOpenid);
				}
				
				return "redirect:/general/index";
			}
		} else {
			return "erroruser";
		}
	}
	
	//*** RZC 卡操作部分   *************************************************************************
	
	/**
	 * 公众号内部个人中心(点击ic卡操作)
	 * @param uid 用户的id
	 * @param openid 用户的openid
	 * @param model
	 * @return {@link String}
	 */
	@RequestMapping(value = "/icoperate")
	public String icoperate(Integer uid,Model model){
		// 参数的判断
		if(uid == null){
			//logger.info("个人中心点击IC卡操作id错误＝"＋uid);
			return "erroruser";
		}
		try {
			Map<String, String> map = JsSignUtil.sign(CommonConfig.ZIZHUCHARGE+"/general/icoperate?uid=" + uid);
			model.addAttribute("nonceStr", map.get("nonceStr"));
			model.addAttribute("timestamp", map.get("timestamp"));
			model.addAttribute("signature", map.get("signature"));
			model.addAttribute("jsapi_ticket", map.get("jsapi_ticket"));
			model.addAttribute("appId", map.get("appId"));
			model.addAttribute("url", map.get("url"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//根据uid查询该用户绑定的ic卡
		List<OnlineCard> onlineCardList = onlineCardService.selectOnlineCardList(uid);
		User touser = userService.selectUserById(uid);
		int amount = 0;
		if (onlineCardList != null && onlineCardList.size() > 0) {
			amount = onlineCardList.size();
			for(OnlineCard item : onlineCardList){
				if(item.getRelevawalt()==1){
					item.setMoney(touser.getBalance());
					item.setSendmoney(touser.getSendmoney());
				}
			}
		}
		model.addAttribute("amount", amount);
		model.addAttribute("cardList", onlineCardList);
		// 从session中获取用户信息
		User user = (User)this.request.getSession().getAttribute("user");
		// 返回给前端
		model.addAttribute("user", user);
		return "general/icoperate";
	}
	
	/**
	 * IC卡页面(点击操作)
	 * @param id 卡id
	 * @param openid 用户的openid
	 * @param model
	 * @return {@link String}
	 */
	@RequestMapping(value = "/operation")
	public String operation(Integer id, Model model){
		// 参数的判断
		if(id == null){
			logger.info("id错误");
			return "erroruser";
		}
		try {
			Map<String, String> map = JsSignUtil.sign(CommonConfig.ZIZHUCHARGE+"/general/operation?id=" + id);
			model.addAttribute("nonceStr", map.get("nonceStr"));
			model.addAttribute("timestamp", map.get("timestamp"));
			model.addAttribute("signature", map.get("signature"));
			model.addAttribute("jsapi_ticket", map.get("jsapi_ticket"));
			model.addAttribute("appId", map.get("appId"));
			model.addAttribute("url", map.get("url"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		OnlineCard onlineCard = onlineCardService.selectOnlineCardById(id);
		model.addAttribute("card", onlineCard);
		User user = (User) this.request.getSession().getAttribute("user");
		model.addAttribute("user", user);
		// 判断在线卡用户是否为特约商户下的用户
		Integer subMerId = CommUtil.toInteger(onlineCard.getMerid());
		if(subMerId != 0){
			// 根据商家的id查询商户
			User subMer = userService.selectUserById(subMerId);
			// 判断商家是不是特约商户
			if(subMer != null && subMer.getSubMer().equals(1)){
				// 标识用户为特约商户下的用户
				model.addAttribute("isSubUser", 1);
				// 用户的商家是特约商户,使用服务商的参数获取openid
//				// 微信code信息
//				String wxCode = CommUtil.toString(request.getParameter("code"));
//				// 重定向回来的标识
//				Integer merId = CommUtil.toInteger(request.getParameter("merId"));
//				if(wxCode == null && merId == 0){
//					String url = "/insideOperation?cId="+id+"&merId="+subMerId;
//					return "redirect:"+url;
//				}
//				if(wxCode != null && merId != 0){
//					try {
//						// 通过code换取网页授权access_token和openid
//						JSONObject doGetStr = WeixinUtil.WeChatTokenSubMerByMerId(wxCode, merId);
//						if(!doGetStr.has("openid")){
//							model.addAttribute("openiderror", "1您的账户暂时无法访问");
//							return "openiderror";
//						}
//						String subOpenid = doGetStr.getString("openid");
//						// 根据服务商下的openid去查询用户的信息
//						User subMerUser = userService.selectSubUserByOpenid(subOpenid);
//						// 用户为空就添加子用户
//						if(subMerUser == null){
//							// 从session中获取用户的信息
//							User subUser = (User) request.getSession().getAttribute("user");
//							User fuWuUser = new User();
//							fuWuUser.setId(subUser.getId());
//							fuWuUser.setOpenid(subOpenid);
//							// 添加
//							userService.addSubUser(fuWuUser);
//							// 将服务商下的用户openid更换,返回给前端
//							subUser.setOpenid(subOpenid);
//							model.addAttribute("card", onlineCard);
//							model.addAttribute("user", subUser);
//						}else{
//							model.addAttribute("card", onlineCard);
//							model.addAttribute("user", subMerUser);
//						}
//					} catch (ParseException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}else{
//				model.addAttribute("card", onlineCard);
//				model.addAttribute("user", this.request.getSession().getAttribute("user"));
//				}else{
//				model.addAttribute("card", onlineCard);
//				model.addAttribute("user", this.request.getSession().getAttribute("user"));
			}
		}
		return "general/operation";
	}
	
	
	@RequestMapping("/onlinecardWeChat")
	public String onlinecardWeChat(Model model) {
		try {
			Map<String, String> map = JsSignUtil.sign(CommonConfig.ZIZHUCHARGE+"/general/onlinecardWeChat");
			model.addAttribute("nonceStr", map.get("nonceStr"));
			model.addAttribute("timestamp", map.get("timestamp"));
			model.addAttribute("signature", map.get("signature"));
			model.addAttribute("jsapi_ticket", map.get("jsapi_ticket"));
			model.addAttribute("appId", map.get("appId"));
			model.addAttribute("url", map.get("url"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		User user = (User) request.getSession().getAttribute("user");
		model.addAttribute("myclick", 1);
		if (user != null) {
			return "general/onlinescanqrcode";
		} else {
			return "erroruser";
		}
	}
	
	/**
	 * @Description：扫码绑定在线卡
	 * @author： origin   code 200:绑定成功	  100:用户信息获取失败	101:该在线卡已经被绑定或出现异常  
	 */
	@RequestMapping(value = "/bindingonline")
	@ResponseBody
	public Object scanbinding( Integer relevawalt, String cardNum){
		Map<String, Object> result = onlineCardService.scanbinding(relevawalt, cardNum);
		//Integer code = (Integer) result.get("code");
		return result;
	}
	
	/**
	 * @Description：扫码修改在线卡卡号
	 * @author： origin   code 200:绑定成功	  100:用户信息获取失败	101:该在线卡已经被绑定或出现异常  
	 */
	//public int icamend(Integer id, String cardID, Integer userId, String cardName){
	@RequestMapping(value = "/scanamendonline")
	@ResponseBody
	public Object scanAmendOnline( Integer relevawalt, String oricard, String nowcard){
		Map<String, Object> result = onlineCardService.scanamendonline(relevawalt, oricard, nowcard);
		//Integer code = (Integer) result.get("code");
		return result;
	}
	//==========================================================================================================
	
	/**
	 * @Description： IC卡解绑
	 * @author： origin
	 */
	@RequestMapping(value = "/icunbind")
	@ResponseBody
	public int icunbind( String cardID){
		//根据指IC卡的卡号信息和用户id，将卡和用户绑定
		int num = cardService.setIcunbindCard( cardID);
		return num;
	}	
	
	
	/**
	 * @Description： IC卡挂失与解挂
	 * @author： origin
	 */
	@RequestMapping(value = "/icunregister")
	@ResponseBody
	public int icunregister(Integer id, Integer num, Integer userId){
		if (id == null || id==0) {
			return 0;
		} else {
			OnlineCard onlineCard = onlineCardService.selectOnlineCardById(id);
			Double cardaccountmoney = CommUtil.toDouble(onlineCard.getMoney());
			Double cardsendmoney = CommUtil.toDouble(onlineCard.getSendmoney());
			Double cardmoney = CommUtil.addBig(cardaccountmoney, cardsendmoney);
//			Double topupbalance = CommUtil.addBig(cardaccountmoney, paymoney);
//			Double givebalance = CommUtil.addBig(cardsendmoney, sendmoney);
			if(num==1){//解挂
				onlineCard.setStatus(1);
				onlineCardRecordService.insertOnlineCardRecord(onlineCard.getUid(), onlineCard.getMerid(), HttpRequest.createOrdernum(6), 
						onlineCard.getCardID(), null, cardmoney, 0.00, 0.00, 0.00, cardaccountmoney, cardsendmoney, 4, 6, null, new Date(),userId);
			}else if(num==2){//挂失
				onlineCard.setStatus(2);
				onlineCardRecordService.insertOnlineCardRecord(onlineCard.getUid(), onlineCard.getMerid(), HttpRequest.createOrdernum(6), 
						onlineCard.getCardID(), null, cardmoney, 0.00, 0.00, 0.00, cardaccountmoney, cardsendmoney, 4, 5, null, new Date(),userId);
			}
			onlineCardService.updateOnlineCard(onlineCard);
			return 1;
		}
	}
	
	/**
	 * @Description：卡操作——删除	   逻辑删除（只删除该卡的关联）
	 * @author： origin
	 */
	@RequestMapping(value = "/movecardrelevance")
	@ResponseBody
	public int movecardrelevance(Integer id, Integer userId) {//物理删除（删除该条数据）
		OnlineCard onlineCard = onlineCardService.selectOnlineCardById(id);
		Integer uid = onlineCard.getUid();
		Double operpaymoney = CommUtil.toDouble(onlineCard.getMoney());
		Double opersendmoney = CommUtil.toDouble(onlineCard.getSendmoney());
		Double opermoney = CommUtil.addBig(operpaymoney, opersendmoney);
		onlineCard.setUid(0);
		onlineCard.setMoney(0.00);
		onlineCard.setSendmoney(0.00);
		onlineCard.setStatus(1);
		onlineCard.setRelevawalt(2);
		onlineCard.setRemark("");
		int num = onlineCardService.updateOnlineCard(onlineCard);
		onlineCardRecordService.insertOnlineCardRecord(uid, onlineCard.getMerid(), HttpRequest.createOrdernum(6), 
				onlineCard.getCardID(), null, 0.00, operpaymoney, opersendmoney, opermoney, 0.00, 0.00, 4, 4, null, new Date(),userId);
		return num;
	}
	
	/**
	 * @Description：IC卡修改备注
	 * @author： origin
	 */
	@RequestMapping(value = "/editRemark")
	@ResponseBody
	public int editRemark(Integer id, String remark, Integer userId){
		if (remark == null || "".equals(remark)) {
			return 0;
		} else {
			OnlineCard onlineCard = onlineCardService.selectOnlineCardById(id);
			onlineCard.setRemark(remark);
			onlineCardService.updateOnlineCard(onlineCard);
			Double operpaymoney = CommUtil.toDouble(onlineCard.getMoney());
			Double opersendmoney = CommUtil.toDouble(onlineCard.getSendmoney());
			Double opermoney = CommUtil.addBig(operpaymoney, opersendmoney);
			onlineCardRecordService.insertOnlineCardRecord(onlineCard.getUid(), onlineCard.getMerid(), HttpRequest.createOrdernum(6), 
					onlineCard.getCardID(), remark, opermoney, 0.00, 0.00, 0.00, operpaymoney, opermoney,  4, 8, null, new Date(), userId);
			return 1;
		}
	}
	
	/**
	 * @Description： 商户查询IC卡消费信息记录
	 * @author： origin          
	 * 创建时间：   2019年6月6日 上午11:06:50
	 */
	@RequestMapping(value = "/onlinecardrecord")
	public String icCardRecord(Integer uid,String cardID, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) return "erroruser";
		OnlineCard online = onlineCardService.selectOnlineCardByCardID(cardID);
		//OnlineCard online = onlineCardService.selectOnlineCardByFigure(cardnum);
		Integer relevawalt = online.getRelevawalt();
		String time = StringUtil.getPastDate(180);
		String beginTime = time + " 00:00:00";
		List<Map<String, Object>> iccardrecord = null;
		if(relevawalt==2){
			iccardrecord = onlineCardRecordService.icCardRecord( cardID, null, beginTime, StringUtil.toDateTime(), null, 0);
			for(Map<String, Object> item : iccardrecord){
				item.put("topupmoney", CommUtil.toDouble(item.get("money")));//充值金额
				item.put("accountmoney", CommUtil.toDouble(item.get("accountmoney")));//账户金额
				Double topupbalance = CommUtil.toDouble(item.get("topupbalance"));//充值余额
				Double sendbalance = CommUtil.toDouble(item.get("givebalance"));//赠送余额
				item.put("sendbalance", CommUtil.toDouble(item.get("givebalance")));//赠送余额
				item.put("accountbalance", CommUtil.addBig(topupbalance, sendbalance));//账户余额
			}
		}else{
			iccardrecord = generalDetailService.selectWalletDetail(online.getUid());
			for(Map<String, Object> item : iccardrecord){
				item.put("topupmoney", CommUtil.toDouble(item.get("money")));//充值金额
				item.put("accountmoney", CommUtil.toDouble(item.get("tomoney")));//账户金额
				item.put("sendbalance", CommUtil.toDouble(item.get("givebalance")));//赠送余额
				item.put("accountbalance", CommUtil.toDouble(item.get("balance")));//赠送余额
			}
		}
		model.addAttribute("iccardrecord", iccardrecord);
		model.addAttribute("size", iccardrecord.size());
		model.addAttribute("relevawalt", relevawalt);
		model.addAttribute("cardID", cardID);
		model.addAttribute("uid", uid);
		model.addAttribute("user", this.request.getSession().getAttribute("user"));
		return "general/onlinecardrecord";
	}
	
	/**
	 * @Description： 根据id或卡号查询在线卡
	 * @author： origin  
	 */
	@RequestMapping(value = "/selonline")
	@ResponseBody
	public OnlineCard selonline(String cardID){
		OnlineCard online = onlineCardService.selectOnlineCardByCardID(cardID);
		return online;
	}
	
	//IC卡金钱转移查询
	@RequestMapping(value = "/transfer")
	public String transfer(Integer uid, Model model){
		List<Card> cardList = cardService.getCardListByStatus(uid, 1);
		List<Card> cardLoss = cardService.getCardListByStatus(uid, 2);
		model.addAttribute("cardList", cardList);
		model.addAttribute("cardLoss", cardLoss);
		model.addAttribute("user", this.request.getSession().getAttribute("user"));
		return "general/transfer";
	}
	
	//查询已提交的申请
	@RequestMapping(value = "/submitApply")
	public String submitApply(Integer uid, Model model) {
		List<CardRecord> submitApply = cardRecordService.submitApply(uid, 0);
		model.addAttribute("submitApply", submitApply);
		model.addAttribute("user", this.request.getSession().getAttribute("user"));
		return "general/submitapply";
	}
	
	//***********************************************************************************************
	@RequestMapping({ "/existaccount" })
	@ResponseBody
	public Map<String, Object> existAccounts( String mobile) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			User user =  userService.existAdmin(mobile);
			if(user!=null){
				datamap.put("code", 200);
				datamap.put("result", user);
				datamap.put("message", "该手机号存在");
			}else{
				datamap.put("code", 201);
				datamap.put("message", "该手机号不存在");
			}
			return datamap;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	
	 /**
	  * 公众号内钱包充值
	  * @param openid 用户的openid
 	  * @param from 
	  * @param model
	  * @return {@link String}
	  */
	@RequestMapping(value = "/walletcharge")
	public String walletcharge(String openid, Integer from, Model model) {
		User user = userService.getUserByOpenid(openid);
		Map<String, Object> datamap = new HashMap<String, Object>();
		if (user != null) {
			model.addAttribute("user", user);
			Integer areaid = CommUtil.toInteger(user.getAid());
			String phone = CommUtil.toString(user.getPhoneNum());
			Integer merid = CommUtil.toInteger(user.getMerid());
			datamap.put("id", CommUtil.toInteger(user.getId()));
			datamap.put("nick", user.getUsername());
			datamap.put("name", user.getRealname());
			datamap.put("phone", user.getPhoneNum());
			datamap.put("topupmoney", user.getBalance());
			datamap.put("sendmoney", user.getSendmoney());
			model.addAttribute("from", from);
			if(phone==null){
				model.addAttribute("source", 1);
				return "merchant/bindPhone";
			}
			String areaname = null;
			Integer wallettempid = 0;
			if (!areaid.equals(0)) {
				Area area = areaService.selectByAreaId(areaid);
				area = area == null ? new Area() : area;
				areaname = CommUtil.toString(area.getName());
				wallettempid = CommUtil.toInteger(area.getTempid());
			}
			model.addAttribute("areaname", areaname);
			User dealeruser = new User();
			if (!merid.equals(0) && merid > 0) dealeruser = userService.selectUserById(merid);
			datamap.put("areaname", areaname);
			datamap.put("dealernick", CommUtil.toString(dealeruser.getUsername()));
			datamap.put("dealename", CommUtil.toString(dealeruser.getRealname()));
			datamap.put("mobile", CommUtil.toString(dealeruser.getPhoneNum()));
			datamap.put("serverphone", CommUtil.toString(dealeruser.getServephone()));
			model.addAttribute("datamap", datamap);
			if(!wallettempid.equals(0)){
				List<TemplateSon> sonTemplateLists = templateService.getSonTemplateLists(wallettempid);
				model.addAttribute("templist", sonTemplateLists);
			}else{
				List<TemplateParent> parentTemplist = templateService.getParentTemplateListByMerchantid(merid, 3);
				if (parentTemplist == null || parentTemplist.size() == 0) {
					parentTemplist = templateService.getParentTemplateListByMerchantid(0, 3);
				}
				List<TemplateSon> sonTemplateLists = templateService.getSonTemplateLists(parentTemplist.get(0).getId());
				model.addAttribute("templist", sonTemplateLists);
			}
			return "general/walletcharge";
		} else {
			logger.info("用户查询发生错误=="+openid);
			return "erroruser";
		}
	}
	
	/**
	 * @Description：信息校验（校验设备信息与用户信息，判断是否允许用户在该设备下使用钱包）
	 * @param code 设备号
	 * @author： origin 2020年7月11日下午3:58:07
	 * @comment:
	 */
	@RequestMapping(value = "/messageverify")
	@ResponseBody
	public Map<String, Object> messageverify(String openid, String code, Model model) {
		Map<String, Object> datamap = new HashMap<String, Object>();
//		User user = (User) request.getSession().getAttribute("user");
		User user = userService.getUserByOpenid(openid);
		if(user==null) return CommUtil.responseBuildInfo(3002, "获取用户信息失败", datamap);
		try {
			Map<String, Object> devicemap = userEquipmentService.getEquipToUserByCode(code);
			// 设备参数
			Integer devdealid = CommUtil.toInteger(devicemap.get("dealid"));
			String devicename = CommUtil.toString(devicemap.get("devicename"));
			String dealnick = CommUtil.toString(devicemap.get("dealnick"));
			String realname = CommUtil.toString(devicemap.get("realname"));
			String telphone = CommUtil.toString(devicemap.get("telphone"));
			Integer devaid = CommUtil.toInteger(devicemap.get("aid"));
			String devareaname = CommUtil.toString(devicemap.get("areaname"));

			datamap.put("devdealid", devdealid);
			datamap.put("devicename", devicename);
			datamap.put("devdealnick", dealnick);
			datamap.put("devrealname", realname);
			datamap.put("devtelphone", telphone);
			datamap.put("devaid", devaid);
			datamap.put("devareaname", devareaname);

			Integer toudealid = CommUtil.toInteger(user.getMerid());
			// 有没有绑定商家(会有空指针)
			if (toudealid.equals(0)) {
				return CommUtil.responseBuildInfo(3001, "用户未绑定商户", datamap);
			}
			Integer tourid = CommUtil.toInteger(user.getId());
			if(!tourid.equals(devdealid) || !tourid.equals(toudealid)){
				// 用户钱包所属商家和设备所属商家是否一致
				if (getUserIsUseCode(user, code) == false) {
					Map<String, Object> touruser = userService.selectGeneralUserInfo(tourid);
					String touid = StringUtil.StringNumer(touruser.get("id").toString());
					String tourusername = CommUtil.toString(touruser.get("username"));
					String toudealnick = CommUtil.toString(touruser.get("muusername"));
					String tourealname = CommUtil.toString(touruser.get("murealname"));
					String toutelphone = CommUtil.toString(touruser.get("muphone_num"));
					String touareaid = CommUtil.toString(touruser.get("aid"));
					String touareaname = CommUtil.toString(touruser.get("arename"));
					datamap.put("touid", touid);
					datamap.put("tourusername", tourusername);
					datamap.put("toudealnick", toudealnick);
					datamap.put("tourealname", tourealname);
					datamap.put("toutelphone", toutelphone);
					datamap.put("touareaid", touareaid);
					datamap.put("touareaname", touareaname);
					return CommUtil.responseBuildInfo(4, "用户钱包所属与设备所属信息不一致", datamap);
				}
			}
			return CommUtil.responseBuildInfo(200, "SUCCEED", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(3000, "系统错误，请联系管理员处理", datamap);
		}
	}
	
	/**
	 * 用户公众号内点击IC卡充值
	 * @param openid 用户的openid
	 * @param cardID 卡号
	 * @param model
	 * @return {@link String}
	 */
	@RequestMapping(value = "/iccharge")
	public String iccharge(String openid, String cardID, Model model) {
		// 参数的判断
		if(cardID == null || "".equals(cardID)){
			logger.info("IC卡ID错误");
			return "erroruser";
		}
		if(openid == null || "".equals(openid)){
			logger.info("IC卡充值用户openid错误");
			return "erroruser";
		}
		model.addAttribute("cardnum", cardID);
		model.addAttribute("openid", openid);
		model.addAttribute("chargesource", "IC");
		User user = userService.getUserByOpenid(openid);
		String phone = CommUtil.toString(user.getPhoneNum());
		if(phone==null){
			model.addAttribute("source", 2);
			return "merchant/bindPhone";
		}
		OnlineCard online = onlineCardService.selectOnlineCardByCardID(cardID);
		Map<String, Object> map = geticonlinetemp(user, online);
		Object templist = map.get("sontemp");
		model.addAttribute("relevawalt", online.getRelevawalt());
		model.addAttribute("user", user);
		model.addAttribute("templist", templist);
		return "general/iccharge";
	}
	
	/**
	 * 微信钱包充值
	 * @param openId 用户的openID
	 * @param choosemoney 支付的钱
	 * @param tempid 模板的id
	 * @param val
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/walletRecharge", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> walletRecharge(String openId, Double choosemoney, Integer tempid, 
			String val){
		Map<String, Object> backmap = new HashMap<>();
		try {
			User tourist = null;
			String mark = "wallet";
			if(!val.equals("2")){//此时为在线卡关联钱包充值
				mark = val;//卡号
				OnlineCard card = onlineCardService.selectOnlineCardByFigure(mark);
				tourist = userService.selectUserById(card.getUid());
			}else{
				tourist = userService.getUserByOpenid(openId);
			}
			String touopenId = tourist.getOpenid();
			if(!openId.equals(touopenId)){
				User opertourist = userService.getUserByOpenid(openId);
				Integer ooperid = opertourist.getId();
				mark = mark + "["+ooperid+"]";
			}
			
			// 用户钱包充值时必须绑定商家
			if (tourist.getMerid() == null || tourist.getMerid() == 0) {
				backmap.put("err", "0");
				return backmap;
			}
			// 参数
			TemplateSon templateSon = templateService.getInfoTemplateOne(tempid);
			Double paymoney = CommUtil.toDouble(templateSon.getMoney());//充值金额
			Double accountmoney = CommUtil.toDouble(templateSon.getRemark());//到账金额
			if(paymoney>accountmoney){
				backmap.put("error", "到账金额小于充值金额。");
				return backmap;
			}
			// 根据商家的id查询商家信息
			User merUser = userService.selectUserById(tourist.getMerid());
			if(merUser == null){
				backmap.put("err", "0");
				return backmap;
			}
			Double opersendmoney = CommUtil.subBig(accountmoney, paymoney);
			
			Double topupmoney = CommUtil.toDouble(tourist.getBalance());
			Double sendmoney = CommUtil.toDouble(tourist.getSendmoney());
			
			Double topupbalance = CommUtil.addBig(topupmoney, paymoney);
			Double sendbalance = CommUtil.addBig(sendmoney, opersendmoney);
			Double accountbalance = CommUtil.addBig(topupbalance, sendbalance);
			String format = HttpRequest.createOrdernum(6);
			
			SortedMap<String, String> params = null;
			String appId = null;
			// 查询用户钱包所属的商家    判断商家是否为微信特约商户
			if(merUser.getSubMer().equals(1) && merUser.getId() != null){
				logger.info("用户:"+tourist.getId()+"使用接口:walletRecharge对微信特约商户付款");
				// 传递商家的id
				request.setAttribute("subMerId", merUser.getId());
				// 根据商家id查询微信特约商户的配置信息
				Map<String, Object> merConfigdata = userService.selectSubMerConfigById(tourist.getMerid());
				appId = CommUtil.toString( merConfigdata.get("subappid"));
				// 特约商户下微信钱包充值的openid不用更改
				params = packagePayParamsToMapSub(request, paymoney, openId, "general/walletPayback", format, "钱包充值");
			}else{
				logger.info("用户:"+tourist.getId()+"使用接口:walletRecharge对服务商平台付款");
				appId = WeiXinConfigParam.APPID;
				params = WxPayController.packagePayParamsToMap(request, paymoney, openId, "general/walletPayback", format, "钱包充值");
			}
			String sign = HttpRequest.createSign("UTF-8", params);
			params.put("sign", sign);
			String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
			String canshu = HttpRequest.getRequestXml(params);
			String sr = HttpRequest.sendPost(url, canshu);
			Map<String, String> map = XMLUtil.doXMLParse(sr);
			SortedMap<String, String> seconde = new TreeMap<>();
			seconde.put("appId", appId);
			String time = HttpRequest.getTimeStamp();
			seconde.put("timeStamp", time);
			String sjzf = HttpRequest.getRandomStringByLength(30);

			seconde.put("nonceStr", sjzf);
			seconde.put("package", "prepay_id=" + (String) map.get("prepay_id"));
			seconde.put("signType", "MD5");

			String sign2 = HttpRequest.createSign("UTF-8", seconde);

			Map<String, Object> paramMap = new HashMap<>();
			moneyService.payMoneys(tourist.getId(), format, 0, 0, paymoney, opersendmoney, accountmoney, accountbalance, topupbalance, sendbalance , mark);
			
			paramMap.put("appId", appId);
			paramMap.put("prepay_id", map.get("prepay_id"));
			paramMap.put("date", time);
			paramMap.put("paySign", sign2);
			paramMap.put("packagess", "prepay_id=" + (String) map.get("prepay_id"));
			paramMap.put("nonceStr", sjzf);
			paramMap.put("signType", "MD5");
			paramMap.put("out_trade_no", params.get("out_trade_no"));
			return paramMap;
		} catch (Exception e) {
			e.printStackTrace();
			return backmap;
		}
	}
	
	/**
	 * 在线卡微信充值
	 * @param openId 用户参数
	 * @param cardnum 在线卡号
	 * @param tempid 模板id
	 * @param source 0:
	 * @return JSON
	 * @throws Exception
	 */
	@RequestMapping(value = "/icRecharge", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> icRecharge(String openId, String cardnum, Integer tempid, Integer source) throws Exception {
		//OnlineCard card = onlineCardService.selectOnlineCardByCardID(cardnum);
		// 根据卡号查询卡的信息
		OnlineCard card = onlineCardService.selectOnlineCardByFigure(cardnum);
		Map<String, Object> paramMap1 = new HashMap<>();
		User user = null;
		// 登录状态下的充值
		if(source == 1){
			user = (User) request.getSession().getAttribute("user");
			// 登录过期
			if (user == null || !user.getOpenid().equals(openId)) {
				paramMap1.put("cardifexist", "3");
				return paramMap1;
			}
		}else{
			// 未登录状态下的充值
			user = userService.getUserByOpenid(openId);
		}
		// 卡号不存在
		if (card == null) {
			paramMap1.put("cardifexist", "0");
			return paramMap1;
		// 未激活只可充值一次商户
		} else if (card.getStatus() == 0 && card.getMoney() > 0) {
			// 判断在线卡
			paramMap1.put("cardifexist", "2");
			return paramMap1;
		// 在线卡未激活 
		} else if(card.getMerid().equals(0) || card.getStatus().equals(0)){
			paramMap1.put("cardifexist", "4");
			return paramMap1;
		} else{
			// 根据模板id查询模板信息
			TemplateSon templateSon = templateService.getInfoTemplateOne(tempid);
			// 参数
			Double paymoney = CommUtil.toDouble(templateSon.getMoney());//充值金额
			Double accountmoney = CommUtil.toDouble(templateSon.getRemark());//到账金额
			Double sendmoney = CommUtil.subBig(accountmoney, paymoney);
			
			Integer merid = CommUtil.toInteger(card.getMerid());
			Double cardamoney = CommUtil.toDouble(card.getMoney());
			Double cardsendmoney = CommUtil.toDouble(card.getSendmoney());
			
			Double topupbalance = CommUtil.addBig(cardamoney, paymoney);
			Double givebalance = CommUtil.addBig(cardsendmoney, sendmoney);
			
			Double cardbalance = CommUtil.addBig(topupbalance, givebalance);

			Date date = new Date();
			String ordernum = HttpRequest.createOrdernum(6);
			// 查询在线卡商家的信息
			User subMer = userService.selectUserById(merid);
			SortedMap<String, String> params = null;
			String appId = null;
			// 判断在线卡商家是否为微信特约商户
			if(subMer != null && subMer.getSubMer().equals(1)){
				// 查询微信特约商户的配置信息
				logger.info(user.getId()+":用户使用接口:icRecharge对微信特约商户付款");
				request.setAttribute("subMerId", merid);
				Map<String, Object> subMerData = userService.selectSubMerConfigById(merid);
				appId = CommUtil.toString(subMerData.get("subappid"));
				logger.info("微信特约商户APPID:"+appId);
				// 特约商户在线卡微信充值的openID无需更改
				params = packagePayParamsToMapSub(request, paymoney, openId, "general/icPayback", ordernum, "在线卡充值");
			}else{
				logger.info(user.getId()+":用户使用接口:icRecharge对服务平台付款");
				appId = WeiXinConfigParam.APPID;
				params = WxPayController.packagePayParamsToMap(request, paymoney, openId, "general/icPayback", ordernum, "在线卡充值模拟测试");
			}
			String sign = HttpRequest.createSign("UTF-8", params);
			params.put("sign", sign);
			String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
			String canshu = HttpRequest.getRequestXml(params);
			String sr = HttpRequest.sendPost(url, canshu);
			Map<String, String> map = XMLUtil.doXMLParse(sr);
			SortedMap<String, String> seconde = new TreeMap<>();
			seconde.put("appId", appId);
			String time = HttpRequest.getTimeStamp();
			seconde.put("timeStamp", time);
			String sjzf = HttpRequest.getRandomStringByLength(30);
			
			seconde.put("nonceStr", sjzf);
			seconde.put("package", "prepay_id=" + (String) map.get("prepay_id"));
			seconde.put("signType", "MD5");
			
			String sign2 = HttpRequest.createSign("UTF-8", seconde);
			if (merid != null) {
				onlineCardRecordService.insertOnlineCardRecord(card.getUid(), merid, ordernum, cardnum, null, 
					cardbalance, paymoney, sendmoney, accountmoney, topupbalance, givebalance, 3, 0, 1, date,user.getId());
			} else {
				onlineCardRecordService.insertOnlineCardRecord(card.getUid(), null, ordernum  , cardnum, null, 
					cardbalance, paymoney, sendmoney, accountmoney, topupbalance, givebalance, 3, 0, 1, date,user.getId());
			}
			Map<String, Object> paramMap = new HashMap<>();
			
			paramMap.put("appId", appId);
			paramMap.put("prepay_id", map.get("prepay_id"));
			paramMap.put("date", time);
			paramMap.put("paySign", sign2);
			paramMap.put("packagess", "prepay_id=" + (String) map.get("prepay_id"));
			paramMap.put("nonceStr", sjzf);
			paramMap.put("signType", "MD5");
			paramMap.put("out_trade_no", params.get("out_trade_no"));
			paramMap.put("cardifexist", "1");
			return paramMap;
		}
	}
	
	/**
	 * 微信支付回调
	 * 
	 * @return
	 * @throws JDOMException
	 */
	@RequestMapping({ "/icPayback" })
	@ResponseBody
	public String icPayback() throws JDOMException {
		
		String resXml = "";
		Map<String, String> backxml = new HashMap<String, String>();
		
		InputStream inStream;
		try {
			inStream = request.getInputStream();
			
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			// logger.error("微信支付----付款成功----");
			outSteam.close();
			inStream.close();
			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
			// logger.error("微信支付----result----=" + result);
			// Map<Object, Object> map = Xmlunit.xml2map(result, false);
			Map<Object, Object> map = XMLUtil.doXMLParse(result);
			
			if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
				// logger.error("微信支付----返回成功");
				// ------------------------------
				String ordernum = CommUtil.toString(map.get("out_trade_no"));
				OnlineCardRecord cardRecord = onlineCardRecordService.selectRecordByOrdernum(ordernum, 3, 1);
				if (cardRecord.getStatus() == 0) {
					OnlineCard onlineCard = onlineCardService.selectOnlineCardByCardID(cardRecord.getCardID());
					Double paymoney = CommUtil.toDouble(cardRecord.getMoney());
					Double accountmoney = CommUtil.toDouble(cardRecord.getAccountmoney());
					Double sendmoney = CommUtil.subBig(accountmoney, paymoney);
					
					Integer merid = CommUtil.toInteger(onlineCard.getMerid());
					Double cardamoney = CommUtil.toDouble(onlineCard.getMoney());
					Double cardsendmoney = CommUtil.toDouble(onlineCard.getSendmoney());
					
					Double topupbalance = CommUtil.addBig(cardamoney, paymoney);
					Double givebalance = CommUtil.addBig(cardsendmoney, sendmoney);
					
					Double cardbalance = CommUtil.addBig(topupbalance, givebalance);
					
					Integer uid = CommUtil.toInteger(cardRecord.getUid());
					Integer orderid = CommUtil.toInteger(cardRecord.getId());
					String cardID = CommUtil.toString(cardRecord.getCardID());
					User touuser = userService.selectUserById(uid);
					Double mermoney = 0.00;
					try {
						if(onlineCard.getStatus()!=0){
							if (merid != null && merid != 0){
								// 在线卡充值金额处理
								Date time = new Date();
								String strtime = CommUtil.toDateTime(time);
								Integer paysource = MerchantDetail.ONLINESOURCE;
								Integer paytype = MerchantDetail.WEIXINPAY;
								Integer paystatus = MerchantDetail.NORMAL;
								Integer aid = CommUtil.toInteger(onlineCard.getAid());
//								Integer aid = CommUtil.toInteger(touuser.getAid());
								Integer type = 1;
								String resultli = null;
								Double partmoney = 0.00;
								try {
									List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
									if (aid != null && aid != 0) {
										areaService.updateAreaEarn(1, paymoney, aid,null,null,paymoney);
										partInfo = areaService.inquirePartnerInfo(aid, 2);
									}
									// 微信特约商户无合伙人,不需要计算分成
									User subMer = userService.selectUserById(merid);
									// subMer为0是普通商家
									if(subMer != null && subMer.getSubMer().equals(0)){
										//商户收益（分成）计算
										logger.info("开始普通商户的分成计算");
										Map<String, Object> listmap = partnerIncomeDispose(partInfo, merid, paymoney, ordernum, time, paysource, paytype, paystatus, type);
										partmoney = CommUtil.toDouble(listmap.get("partmoney"));
										resultli = CommUtil.toString(listmap.get("json"));
									}else{
										userService.updateMerAmount(1, paymoney, merid);//商户收益总额  1为加  0为减
										merchantDetailService.insertMerEarningDetail(merid, paymoney, subMer.getEarnings(), ordernum, time, paysource, paytype, paystatus);
										//微信特约商户在线卡充值comment为1
										resultli = "1";
									}
								} catch (Exception e) {
									logger.warn("小区修改余额错误===" + e.getMessage());
								}
								mermoney = CommUtil.subBig(paymoney, partmoney);
								Integer manid = partmoney == 0 ? 0 : -1;
								tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, cardID, 5, 2, 1, null, resultli);
								String phone = getTempPhone(CommUtil.toString(touuser.getAid()), merid, 4);
								String detailurl = CommonConfig.ZIZHUCHARGE + "/general/sendmsgdetails?source=5&id=" + cardRecord.getId();
								String first = "您好，在线卡充值成功，欢迎您的使用。\r\n订单编号："+ordernum;
								TempMsgUtil.paychargesendmsg( first, onlineCard.getUid(), phone, detailurl, paymoney, CommonConfig.PAYRETURNMES);
								if(merid!=0 && authorityService.authorSwitch(merid, 3)){
									User meruser = userService.selectUserById(merid);
									String fistdata = "您好，您的用户"+CommUtil.trimToEmpty(touuser.getUsername())
									+"["+StringUtil.StringNumer(uid.toString())+"]在线卡["+cardID+"]充值成功。";
									String meroppenid = CommUtil.toString(meruser.getOpenid());
									String msgpaymoney = "充值金额："+accountmoney + "元，\r\n实际支付：" + paymoney + "元"; 
									String msgbalance = cardbalance + "元"; 
									String remark = "订单号: "+ordernum;
									String url = "";
									TempMsgUtil.dealerOrderPush(fistdata, meroppenid, msgpaymoney, msgbalance, strtime, remark, url);
								}
						}
					}
					} catch (Exception e) {
						logger.info(e.getMessage() + "---" + ByteUtils.getLineNumber(e));
					}
					OnlineCard onlineCard2 = new OnlineCard();
					onlineCard2.setId(onlineCard.getId());
					onlineCard2.setMoney(topupbalance);
					onlineCard2.setSendmoney(givebalance);
//					onlineCard2.setSendmoney(CommUtil.addBig(sendmoney, onlineCard.getSendmoney()));
//					onlineCard2.setMoney((accountmoney*100 + onlineCard.getMoney()*100)/100);
					onlineCardService.updateOnlineCard(onlineCard2);
					OnlineCardRecord onlineCardRecord = new OnlineCardRecord();
					onlineCardRecord.setId(cardRecord.getId());
					onlineCardRecord.setStatus(1);
					onlineCardRecordService.updateRecord(onlineCardRecord);
					// 处理业务完毕
					// ------------------------------
					
					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
					BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
					out.write(resXml.getBytes());
					out.flush();
					out.close();
				}
			} else {
				// logger.info("支付失败,错误信息：" + map.get("err_code"));
				System.out.println(map.get("err_code") + "----" + map.get("out_trade_no").toString());
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				out.write(resXml.getBytes());
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			// logger.error("支付回调发布异常：" + e);
			e.printStackTrace();
		}
		return resXml;
	}
	
//	/**
//	 * 用户微信支付充值钱包的回调
//	 * 
//	 * @return
//	 * @throws JDOMException
//	 */
//	@RequestMapping({ "/walletPayback" })
//	@ResponseBody
//	public String walletPayback() throws JDOMException {
//		String resXml = "";
//		Map<String, String> backxml = new HashMap<String, String>();
//		InputStream inStream;
//		try {
//			inStream = request.getInputStream();
//			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
//			byte[] buffer = new byte[1024];
//			int len = 0;
//			while ((len = inStream.read(buffer)) != -1) {
//				outSteam.write(buffer, 0, len);
//			}
//			outSteam.close();
//			inStream.close();
//			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
//			// logger.error("微信支付----result----=" + result);
//			// Map<Object, Object> map = Xmlunit.xml2map(result, false);
//			Map<Object, Object> map = XMLUtil.doXMLParse(result);
//			if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
//				// logger.error("微信支付----返回成功");
//				// 处理业务
//				String ordernum = CommUtil.toString(map.get("out_trade_no"));
//				// 根据商家订单查询数据
//				Money money = moneyService.queryMoneyByOrdernum(ordernum);
//				if (money.getStatus() == null || money.getStatus() == 0) {
//						//修改本地数据
//						Integer uid = CommUtil.toInteger(money.getUid());
//						Double paymoney = CommUtil.toDouble(money.getMoney());
//						Double sendmoney = CommUtil.toDouble(money.getSendmoney());
//						Double accountmoney = CommUtil.toDouble(money.getTomoney());
//						Double balancemoney = CommUtil.toDouble(money.getBalance());
//
//						Double topupbalance = CommUtil.toDouble(money.getTopupbalance());
//						Double givebalance = CommUtil.toDouble(money.getGivebalance());
//						
//						Integer orderid = CommUtil.toInteger(money.getId());
//						Date time = new Date();
//						String strtime = CommUtil.toDateTime(time);
//						Integer paysource = MerchantDetail.WALLETSOURCE;
//						Integer paytype = MerchantDetail.WEIXINPAY;
//						Integer paystatus = MerchantDetail.NORMAL;
//						Integer type = 1;
//						Double mermoney = 0.00;
//						
//						User touuser = userService.selectUserById(uid);
//						Integer merid = CommUtil.toInteger(touuser.getMerid());
//						User meruser = userService.selectUserById(merid);
//						Integer aid = CommUtil.toInteger(touuser.getAid());
//						
//						Double userbalance = CommUtil.toDouble(touuser.getBalance());
//						Double usersendmoney = CommUtil.toDouble(touuser.getSendmoney());
//						Double usermoney = CommUtil.addBig(userbalance, usersendmoney);
//
////						Double opermoney = CommUtil.addBig(paymoney, userbalance);
////						Double opersendmoney = CommUtil.addBig(sendmoney, usersendmoney);
//						Double opermoney = CommUtil.toDouble(paymoney);
//						Double opersendmoney = CommUtil.toDouble(sendmoney);
//						Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
//						
//						Double mtopupbalance = CommUtil.addBig(opermoney, userbalance);
//						Double mgivebalance = CommUtil.addBig(opersendmoney, usersendmoney);
//						Double operbalance = CommUtil.addBig(mtopupbalance, mgivebalance);
//						//1、充值记录修改
//						money.setBalance(operbalance);
//						money.setStatus(1);
//						moneyService.updateMoney(money);
//						//2、修改用户金额
//						userService.updateBalanceByOpenid(mtopupbalance, mgivebalance, touuser.getOpenid(),null);
////						userService.updateBalanceByOpenid( userBalance, usergivebalance, touuser.getOpenid(),null);
//						String mremark = money.getRemark();
//						//3、添加钱包明细
//						//generalDetailService.insertGenDetail(uid, merid, tomoney, userCalcBalance, ordernum, money.getPaytime(), 1, mremark);
//						generalDetailService.insertGenDetail(uid, merid, opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, ordernum, time, 1, mremark);
//						String resultli = null;
//						Double partmoney = 0.00;
//						try {
//							List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
//							if (aid != null && aid != 0) {
//								areaService.updateAreaEarn(1, paymoney, aid, paymoney, null, null);
////								areaService.updateAreaEarn(1, paymoney, aid,null,paymoney,null);
//								partInfo = areaService.inquirePartnerInfo(aid, 2);
//							}
//							// 微信特约商户无合伙人,不需要计算分成
//							User subMerUser = userService.selectUserById(merid);
//							// subMer为0是普通商家
//							if(subMerUser != null && subMerUser.getSubMer().equals(0)){
//								//商户收益（分成）计算
//								logger.info("开始普通商户的分成计算");
//								Map<String, Object> listmap = partnerIncomeDispose(partInfo, merid, paymoney, ordernum, time, paysource, paytype, paystatus, type);
//								partmoney = CommUtil.toDouble(listmap.get("partmoney"));
//								resultli = CommUtil.toString(listmap.get("json"));
//							}else{
//								// 特约商户收益计算
//								logger.info("开始特约商户的收益");
//								userService.updateMerAmount(1, paymoney, merid);
//								merchantDetailService.insertMerEarningDetail(merid, paymoney, subMerUser.getEarnings(), ordernum, time, paysource, paytype, paystatus);
//								resultli = "1";
//							}
//						} catch (Exception e) {
//							logger.warn("小区修改余额错误===" + e.getMessage());
//						}
//						mermoney = CommUtil.subBig(paymoney, partmoney);
//						Integer manid = partmoney == 0 ? 0 : -1;
//						tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, "钱包", 4, 2, 1, null, resultli);
//						String detailurl = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=4&id="+money.getId();
//						TempMsgUtil.paychargesendmsg("您好，钱包充值成功。", uid, meruser.getPhoneNum(), null, money.getMoney());
//						
//						if(merid!=0 && authorityService.authorSwitch(merid, 3)){
////							String fistdata = "商户["+CommUtil.toString(meruser.getPhoneNum())+"]您好，您的用户"+touuser.getUsername()+"["+StringUtil.StringNumer(uid.toString())+"]钱包充值成功。";
//							String fistdata = "您好，您的用户"+CommUtil.trimToEmpty(touuser.getUsername())+"["+StringUtil.StringNumer(uid.toString())+"]钱包充值成功。";
//							String meroppenid = CommUtil.toString(meruser.getOpenid());
//							String sendmsgpaymoney = "充值金额："+accountmoney + "元，\r\n实际支付：" + paymoney + "元"; 
//							String sendmsgbalance = operbalance + "元"; 
//							String remark = "订单号: "+ordernum;
//							String url = "";
//							TempMsgUtil.dealerOrderPush(fistdata, meroppenid, sendmsgpaymoney, sendmsgbalance, strtime, remark, url);
//						}
//				}
//				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
//						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
//				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//				out.write(resXml.getBytes());
//				out.flush();
//				out.close();
//			} else {
//				// logger.info("支付失败,错误信息：" + map.get("err_code"));
//				System.out.println(map.get("err_code") + "----" + map.get("out_trade_no").toString());
//				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
//						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
//				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//				out.write(resXml.getBytes());
//				out.flush();
//				out.close();
//			}
//		} catch (IOException e) {
//			// logger.error("支付回调发布异常：" + e);
//			e.printStackTrace();
//		}
//		return resXml;
//	}
	

	/**
	 * 用户微信支付充值钱包的回调
	 * 
	 * @return
	 * @throws JDOMException
	 */
	@RequestMapping({ "/walletPayback" })
	@ResponseBody
	public String walletPayback() throws JDOMException{
		String resXml = "";
		Map<String, String> backxml = new HashMap<String, String>();
		InputStream inStream;
		try {
			inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();
			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
			// logger.error("微信支付----result----=" + result);
			// Map<Object, Object> map = Xmlunit.xml2map(result, false);
			Map<Object, Object> map = XMLUtil.doXMLParse(result);
			if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
				// 处理业务
				String ordernum = CommUtil.toString(map.get("out_trade_no"));
				// 根据商家订单查询数据
				Money money = moneyService.queryMoneyByOrdernum(ordernum);
				if (money.getStatus() == null || money.getStatus() == 0) {//该订单处于预订单状态
						//修改本地数据
						Integer uid = CommUtil.toInteger(money.getUid());
						Double paymoney = CommUtil.toDouble(money.getMoney());//付款总额
						Double opertopupmoney = CommUtil.toDouble(paymoney);//操作总额
						Double opersendmoney = CommUtil.toDouble(money.getSendmoney());//操作充值金额
						Double opermoney = CommUtil.toDouble(money.getTomoney());//操作赠送金额
						
						String mremark = money.getRemark();//来源
						
						Integer orderid = CommUtil.toInteger(money.getId());
						Date time = new Date();
						String strtime = CommUtil.toDateTime(time);
						Integer paysource = MerchantDetail.WALLETSOURCE;
						Integer paytype = MerchantDetail.WEIXINPAY;
						Integer paystatus = MerchantDetail.NORMAL;
						Integer type = 1;
						Double mermoney = 0.00;
						
						User touuser = userService.selectUserById(uid);
						Integer merid = CommUtil.toInteger(touuser.getMerid());
						Integer aid = CommUtil.toInteger(touuser.getAid());
						User meruser = userService.selectUserById(merid);
						//============================================================================
						Double topupmoney = CommUtil.toDouble(touuser.getBalance());//账户充值余额
						Double sendmoney = CommUtil.toDouble(touuser.getSendmoney());//账户赠送余额
						Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额（充值与赠送的合）
						//============================================================================
						Double topupbalance = CommUtil.addBig(opertopupmoney, topupmoney);//充值余额
						Double sendbalance = CommUtil.addBig(opersendmoney, sendmoney);//赠送余额
						Double accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
						//==========================================================================
						//1、充值记录修改
						money.setBalance(accountbalance);
						money.setStatus(1);
						moneyService.updateMoney(money);
						//2、修改用户金额
						userService.updateBalanceByOpenid(topupbalance, sendbalance, touuser.getOpenid(),null);
						
						//3、添加钱包明细
						generalDetailService.insertGenDetail(uid, merid, opertopupmoney, opersendmoney, 
								opermoney, accountbalance, topupbalance, sendbalance, ordernum, time, 1, mremark);
						String resultli = null;
						Double partmoney = 0.00;
						try {
							List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
							if (aid != null && aid != 0) {
								areaService.updateAreaEarn(1, paymoney, aid, paymoney, null, null);
//								areaService.updateAreaEarn(1, paymoney, aid,null,paymoney,null);
								partInfo = areaService.inquirePartnerInfo(aid, 2);
							}
							// 微信特约商户无合伙人,不需要计算分成
							User subMerUser = userService.selectUserById(merid);
							// subMer为0是普通商家
							if(subMerUser != null && subMerUser.getSubMer().equals(0)){
								//商户收益（分成）计算
								Map<String, Object> listmap = partnerIncomeDispose(partInfo, merid, paymoney, ordernum, time, paysource, paytype, paystatus, type);
								partmoney = CommUtil.toDouble(listmap.get("partmoney"));
								resultli = CommUtil.toString(listmap.get("json"));
							}else{
								// 特约商户收益计算
								userService.updateMerAmount(1, paymoney, merid);
								merchantDetailService.insertMerEarningDetail(merid, paymoney, subMerUser.getEarnings(), ordernum, time, paysource, paytype, paystatus);
								resultli = "1";
							}
						} catch (Exception e) {
							logger.warn("小区修改余额错误===" + e.getMessage());
						}
						mermoney = CommUtil.subBig(paymoney, partmoney);
						Integer manid = partmoney == 0 ? 0 : -1;
						int oper = mremark.indexOf("[");
						Integer operateid = 0;
						if(oper>0){
							String operid = mremark.substring(oper).replace("[", "").replace("]", "");
							operateid = CommUtil.toInteger(operid);
						}
						if(operateid!=0){
							tradeRecordService.insertToTrade(merid, manid, operateid, ordernum, paymoney, mermoney, partmoney, "钱包", 4, 2, 1, null, resultli);
							String detailurl = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=4&id="+money.getId();
//							String first = "您好，钱包充值成功，欢迎您的使用。\r\n订单编号："+ordernum;
//							TempMsgUtil.paychargesendmsg(first, operateid, meruser.getPhoneNum(), detailurl, money.getMoney(), CommonConfig.PAYRETURNMES);
							String operatefirst = "您好，会员["+StringUtil.StringNumer(operateid.toString())+"]帮您钱包充值成功，欢迎您的使用。\r\n订单编号："+ordernum;
							TempMsgUtil.paychargesendmsg(operatefirst, uid, meruser.getPhoneNum(), detailurl, money.getMoney(), CommonConfig.PAYRETURNMES);
						}else{
							tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, "钱包", 4, 2, 1, null, resultli);
							String detailurl = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=4&id="+money.getId();
							String first = "您好，钱包充值成功，欢迎您的使用。\r\n订单编号："+ordernum;
							TempMsgUtil.paychargesendmsg(first, uid, meruser.getPhoneNum(), detailurl, money.getMoney(), CommonConfig.PAYRETURNMES);
						}
						if(merid!=0 && authorityService.authorSwitch(merid, 3)){
//							String fistdata = "商户["+CommUtil.toString(meruser.getPhoneNum())+"]您好，您的用户"+touuser.getUsername()+"["+StringUtil.StringNumer(uid.toString())+"]钱包充值成功。";
							String fistdata = "您好，您的用户"+CommUtil.trimToEmpty(touuser.getUsername())+"["+StringUtil.StringNumer(uid.toString())+"]钱包充值成功。";
							String meroppenid = CommUtil.toString(meruser.getOpenid());
							String sendmsgpaymoney = "充值金额："+opermoney + "元，\r\n实际支付：" + paymoney + "元"; 
							String sendmsgbalance = accountbalance + "元"; 
							String remark = "订单号: "+ordernum;
							String url = "";
							TempMsgUtil.dealerOrderPush(fistdata, meroppenid, sendmsgpaymoney, sendmsgbalance, strtime, remark, url);
						}
				}
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				out.write(resXml.getBytes());
				out.flush();
				out.close();
			} else {
				// logger.info("支付失败,错误信息：" + map.get("err_code"));
				System.out.println(map.get("err_code") + "----" + map.get("out_trade_no").toString());
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				out.write(resXml.getBytes());
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			// logger.error("支付回调发布异常：" + e);
			e.printStackTrace();
		}
		return resXml;
	}
	
	@RequestMapping("/nearbystation")
	public String nearbyStation( Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if(null == user){
			return "erroruser";
		}
		List<Map<String, Object>> listmap = new ArrayList<Map<String,Object>>();
		PageUtils<List<Map<String, Object>>> listpage = null;
		try {
			model.addAttribute("uid", user.getId());
			if(user.getAid()!=null && user.getAid()!=0){
				listpage = equipmentService.selfAreaEquipInfo(user.getAid(), 1, 5);
//				listmap = listpage.getListMap();
//				listmap = portEstimate(listmap);
			}else{
				Integer merid = user.getMerid();
				listpage = equipmentService.selfAreaEquipMerInfo( merid, 1, 5);
			}
			listmap = listpage.getListMap();
			listmap = portEstimate(listmap);
			model.addAttribute("totalRows", listpage.getTotalRows());
			model.addAttribute("lastIndex", listpage.getLastIndex());
			model.addAttribute("currentPage", listpage.getCurrentPage());
			model.addAttribute("currentPage", listpage.getCurrentPage());
			model.addAttribute("pitchonstation", 1);
			model.addAttribute("listmap", listmap);
			return "general/nearbystation";
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	public List<Map<String, Object>> portEstimate (List<Map<String, Object>> listmap){
		User user = (User) request.getSession().getAttribute("user");
		try {
			for(Map<String, Object> item : listmap){
				String code = CommUtil.toString(item.get("code"));
				String hardversion = CommUtil.toString(item.get("hardversion"));
//				int neednum = EquipmentController.getAllportNeednum(hardversion);
//				List<AllPortStatus> allPortStatus = allPortStatusService.findPortStatusListByEquipmentnum(code, neednum);
				List<Map<String, String>> allPortStatus = DisposeUtil.addPortStatus(code, hardversion);
				List<String> forbidport = new ArrayList<String>();
				List<String> usableport = new ArrayList<String>();
				List<String> userport = new ArrayList<String>();
				List<String> failureport = new ArrayList<String>();
				if("01".equals(hardversion)){
					forbidport = getAssignPotr(user.getId(), code, user.getMerid());
				}
				Integer num = 0;
				for(Map<String, String> itemsen : allPortStatus){
					Integer port = CommUtil.toInteger(itemsen.get("port"));
					Integer portStatus = CommUtil.toInteger(itemsen.get("portStatus"));
					if(portStatus==1){
						Boolean bool = forbidport.contains(port.toString());
						if(!bool){
							num = num +1;
						}
						usableport.add(port.toString());
					}else if(portStatus==2){
						userport.add(port.toString());
					}else{
						failureport.add(port.toString());
					}
				}
				item.put("forbidport", forbidport);
				item.put("portusable", num);
				item.put("portamount", allPortStatus.size());
				item.put("usableport", usableport);
				item.put("userport", userport);
				item.put("failureport", failureport);
			}
			return listmap;
		} catch (Exception e) {
			return listmap;
		}
	}
	
	@RequestMapping("/nearbystationajax")
	@ResponseBody
	public String nearbyStationAjax(Integer currentPage, Integer numPerPage, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		List<Map<String, Object>> listmap = new ArrayList<Map<String,Object>>();
		PageUtils<List<Map<String, Object>>> listpage = null;
		try {
			if(user==null ){
				return "erroruser";
			}else if(user.getAid()!=null && user.getAid()!=0){
				listpage = equipmentService.selfAreaEquipInfo(user.getAid(), currentPage, numPerPage);
			}else{
				Integer merid = user.getMerid();
				listpage = equipmentService.selfAreaEquipMerInfo( merid, currentPage, numPerPage);
			}
			listmap = listpage.getListMap();
			listmap = portEstimate(listmap);
			
			listpage.setListMap(listmap);
			return JSON.toJSONString(listpage);
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	@RequestMapping("/chargepay")
	public String chargePay(String code, Integer port, Model model) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) {
				return "erroruser";
			}
			Equipment equipment = equipmentService.getEquipmentById(code);
			TemplateParent temp = templateService.assignTempInfo(user.getId(), user.getMerid(), equipment.getTempid(), equipment.getHardversion());
			model.addAttribute("openid", user.getOpenid());
			model.addAttribute("tempinfo", temp);
			model.addAttribute("code", code);
			model.addAttribute("port", port);
			return "general/paytempinfo";
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}

	public List<String> getAssignPotr(Integer uid, String code, Integer merid) {
		//获取该用户在此设备中被指定的所有端口号
		List<Map< String, Object>> assignport = equipmentService.assignportinfo(uid, code, merid);
		//获取该设备被指定的所有端口号
		List<String> slelecToPortINfo = equipmentService.slelecToPortINfo(merid, code);
		List<String> forbidport = slelecToPortINfo;
		if(assignport == null || assignport.size()>0){
			List<String> list = new ArrayList<String>();
			for(Map< String, Object> item : assignport){
				String port = CommUtil.toString(item.get("port"));
				list.add(port);
				boolean res = slelecToPortINfo.contains(port);
				if(res){
					Iterator<String> it = forbidport.iterator();
					while(it.hasNext()){
			            String str = CommUtil.toString(it.next());
			            if(port.equals(str)){
			            	it.remove();
			            }        
			        }
				}
			}
		}
		return forbidport;
	
	}
	
	//TODO wantToRecharge
	@RequestMapping("/wantToRecharge")
	public String wantToRecharge(Model model) {
		try {
			Map<String, String> map = JsSignUtil.sign(CommonConfig.ZIZHUCHARGE+"/general/wantToRecharge");

			model.addAttribute("nonceStr", map.get("nonceStr"));
			model.addAttribute("timestamp", map.get("timestamp"));
			model.addAttribute("signature", map.get("signature"));
			model.addAttribute("jsapi_ticket", map.get("jsapi_ticket"));
			model.addAttribute("appId", map.get("appId"));
			model.addAttribute("url", map.get("url"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		User user = (User) request.getSession().getAttribute("user");
		model.addAttribute("userinfo", user);
		model.addAttribute("myclick", 1);
		return "general/wantToRecharge";
	}
	
	@RequestMapping(value = "/payaccess")
	public String payaccess() {
		return "redirect:/general/index";
	}
	
	@RequestMapping(value = "/paywin")
	public String paywin(@RequestParam(value = "money")Integer money, Model model) {
		model.addAttribute("money", money);
		model.addAttribute("rechargeforname", "二楞");
		return "general/paywin";
	}
	
	@RequestMapping("/genWalletDetailInfo")
	public String genWalletDetailInfo(Integer id, Model model) {
		GeneralDetail generalDetail = generalDetailService.selectGenWalletInfoById(id);
		if (id == null || generalDetail == null) {
			return "erroruser";
		} else {
			Integer paysource = generalDetail.getPaysource();
			String ordernum = generalDetail.getOrdernum();
			/** 金额来源 1、充值-2、充电-3、投币-4、离线卡充值-5、退款到钱包 6、钱包退款 7虚拟充值  8虚拟退款 9在线卡记录 */
			if (paysource == 1 || paysource == 6 || paysource == 7 || paysource == 8) {
				Money money = moneyService.queryMoneyByOrdernum(ordernum);
				model.addAttribute("order", money);
			} else if (paysource == 2) {
				List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
				if(chargeRecord.size()>0){
					model.addAttribute("order", chargeRecord.get(0));
				}else{
					ChargeRecord order = new ChargeRecord();
					model.addAttribute("order", order);
				}
			}
			model.addAttribute("generalDetail", generalDetail);
			return "general/genWalletDetailInfo";
		}
	}

	/**
	 * 用户在公众号外部扫码使用钱包支付
	 * @param chargeparam 模板的id
	 * @param portchoose 设备的端口
	 * @param model
	 * @return {@link Map}
	 */
	@RequestMapping("/chargePortWalletPay")
	@ResponseBody
	public Map<String, Object> chargePortWalletPay(Integer chargeparam, Byte portchoose, Model model) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			String openid = CommUtil.toString(request.getParameter("openid"));
			String code = CommUtil.toString(request.getParameter("code"));
			// 获取JSAPI支付时所有的必填参数
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Date date = new Date();
			String format = df.format(date) + code + portchoose;
			TemplateSon templateSon = templateService.getInfoTemplateOne(chargeparam);
			Double money = CommUtil.toDouble(templateSon.getMoney());
			User user = userService.getUserByOpenid(openid);
			user = user == null ? new User() : user;
			Double topupmoney = CommUtil.toDouble(user.getBalance());
			Double sendmoney = CommUtil.toDouble(user.getSendmoney());
			Double balance = CommUtil.addBig(topupmoney, sendmoney);
			Integer tourid = CommUtil.toInteger(user.getId());
			Integer toudealid = CommUtil.toInteger(user.getMerid());
			Map<String, Object> devicemap = userEquipmentService.getEquipToUserByCode(code);
			// 设备参数
			Integer devdealid = CommUtil.toInteger(devicemap.get("dealid"));
			String devicename = CommUtil.toString(devicemap.get("devicename"));
			String dealnick = CommUtil.toString(devicemap.get("dealnick"));
			String realname = CommUtil.toString(devicemap.get("realname"));
			String telphone = CommUtil.toString(devicemap.get("telphone"));
			Integer devaid = CommUtil.toInteger(devicemap.get("aid"));
			String devareaname = CommUtil.toString(devicemap.get("areaname"));

			datamap.put("devicename", devicename);
			datamap.put("devdealnick", dealnick);
			datamap.put("devrealname", realname);
			datamap.put("devtelphone", telphone);
			datamap.put("devaid", devaid);
			datamap.put("devareaname", devareaname);
			// 判断用户钱包余额是否充足
			if (balance < money || balance < 0) {
				return CommUtil.responseBuildInfo(2, "用户不存在或钱包金额不足", datamap);
			}
			if(!tourid.equals(devdealid) || !tourid.equals(toudealid)){
				// 有没有绑定商家(会有空指针)
				if (toudealid.equals(0)) {
					return CommUtil.responseBuildInfo(4, "用户未绑定商户", datamap);
				}
				// 用户钱包所属商家和设备所属商家是否一致
				if (getUserIsUseCode(user, code) == false) {
					Map<String, Object> touruser = userService.selectGeneralUserInfo(tourid);
					String touid = StringUtil.StringNumer(touruser.get("id").toString());
					String tourusername = CommUtil.toString(touruser.get("username"));
					String toudealnick = CommUtil.toString(touruser.get("muusername"));
					String tourealname = CommUtil.toString(touruser.get("murealname"));
					String toutelphone = CommUtil.toString(touruser.get("muphone_num"));
					String touareaid = CommUtil.toString(touruser.get("aid"));
					String touareaname = CommUtil.toString(touruser.get("arename"));
					datamap.put("touid", touid);
					datamap.put("tourusername", tourusername);
					datamap.put("toudealnick", toudealnick);
					datamap.put("tourealname", tourealname);
					datamap.put("toutelphone", toutelphone);
					datamap.put("touareaid", touareaid);
					datamap.put("touareaname", touareaname);
					return CommUtil.responseBuildInfo(4, "用户不存在或钱包金额不足", datamap);
				}
			}
//			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
			Equipment equipment = equipmentService.getEquipmentById(code);
			ChargeRecord chargeRecord = new ChargeRecord();
			chargeRecord.setEquipmentnum(code);// 设备号/机位号
			chargeRecord.setOrdernum(format);// 订单号
			int port = portchoose;
			chargeRecord.setPort(port);// 端口号
			chargeRecord.setExpenditure(templateSon.getMoney());// 金额
			String durationtime = templateSon.getChargeTime().toString();
			if (DisposeUtil.checkIfHasV3(equipment.getHardversion()) && templateSon.getType() == 3) {
				short clacV3TimeBytemp = templateService.clacV3TimeBytemp(templateSon.getTempparid(), templateSon.getMoney());
				durationtime = clacV3TimeBytemp + "";
			}
			chargeRecord.setDurationtime(durationtime);// 时长
			chargeRecord.setQuantity(templateSon.getChargeQuantity());// 电量
			chargeRecord.setPaytype(ChargeRecord.WALLETPAYTYPE);// 支付类型
			chargeRecord.setMerchantid(devdealid);
//			chargeRecord.setMerchantid(userEquipment.getUserId());
			chargeRecord.setStatus(1);
			Integer uid = user.getId();
			chargeRecord.setUid(uid);
			chargeRecord.setBegintime(date);
//			if (user != null) {
//				chargeRecord.setUid(userService.getUserByOpenid(openid).getId());
//			}
			String ifcontinue = request.getParameter("ifcontinue");
			if (ifcontinue != null && !"".equals(ifcontinue)) {
				ChargeRecord chargeRecordById = chargeRecordService.getChargeRecordById(Integer.parseInt(ifcontinue));
				if (chargeRecordById != null && chargeRecordById.getIfcontinue() != null) {
					chargeRecord.setIfcontinue(chargeRecordById.getIfcontinue());
				} else {
					chargeRecord.setIfcontinue(Integer.parseInt(ifcontinue));
				}
			}
			chargeRecordService.insetRecord(chargeRecord);
			String money1 = String.valueOf(chargeRecord.getExpenditure() * 100);
			int idx = money1.lastIndexOf(".");
			Short money3 = Short.valueOf(money1.substring(0, idx));
			short time = Short.valueOf(chargeRecord.getDurationtime());
			short elec = Short.valueOf(chargeRecord.getQuantity() + "");
			//-------------------------------------------------------------
			Double userbalance = CommUtil.toDouble(user.getBalance());
			Double usersendmoney = CommUtil.toDouble(user.getSendmoney());
			Double usermoney = CommUtil.addBig(userbalance, usersendmoney);
			Double coumoney = CommUtil.toDouble(money);
			Double opermoney = 0.00;
			Double opersendmoney = 0.00;
			Double topupbalance = 0.00;
			Double givebalance = 0.00;
			Double recharge = CommUtil.subBig(userbalance, coumoney);
			if(recharge>=0){
				opermoney = coumoney;
				opersendmoney = 0.00;
				topupbalance = CommUtil.subBig(userbalance, opermoney);
				givebalance = usersendmoney;
			}else{
				opermoney = userbalance;
				opersendmoney = CommUtil.toDouble(Math.abs(recharge));
				topupbalance = 0.00;
				givebalance = CommUtil.subBig(usersendmoney, opersendmoney);
			}
			Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
			Double operbalance = CommUtil.addBig(topupbalance, givebalance);
//			User meruser = userService.selectUserById(devdealid);//设备商户信息
//			User meruser = userService.selectUserById(userEquipment.getUserId());
			// 更新用户钱包的余额
			if (money != 0) {
				User user2 = new User();
				user2.setId(user.getId());
				user2.setBalance(topupbalance);
				user2.setSendmoney(givebalance);
				if (user.getMerid() == null || user.getMerid() == 0) {
					user2.setMerid(devdealid);
				}
				userService.updateUserById(user2);
				//--------------------------------------
				generalDetailService.insertGenWalletDetail(user.getId(), devdealid, opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, format, date, 2);
			}
			String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=1&ordernum=" + format;
			String phone = getTempPhone(chargeRecord.getEquipmentnum(), chargeRecord.getMerchantid(), 0);
			TempMsgUtil.paychargesendmsg("您好，付款成功，开始充电", user.getId(), phone, urltem,chargeRecord.getExpenditure(),CommonConfig.PAYSUCCEEDMES);
			tradeRecordService.insertTrade(devdealid, user.getId(), format,
					money, money, code, 1, 1, 1,equipmentService.getEquipmentById(code).getHardversion());
			
//			String servicphone = systemSetService.getServicePhone(devicetempid, deviceaid, merid);
//			
//			systemSetService.sendMmessageCharge(ordernum, orderid, paysource, merid, uid, paymoney, strtime,
//					devicenum, port, devicename, deviceaid, areaname, servicphone);
//			
			
			// 将订单数据返回前端
			datamap.put("orderNum",format);
			datamap.put("payMoney", templateSon.getMoney());
			return CommUtil.responseBuildInfo(1, "用户未绑定商户", datamap);
//			return "1";
		} catch (Exception e) {
			e.printStackTrace();
//			logger.warn("订单号：" + format + "---充电异常！" + e.getMessage() + e.getStackTrace()[0].getLineNumber());
			return CommUtil.responseBuildInfo(3, "充电异常错误", datamap);
//			return "3";
		}
	}
	
	/**
	 * 脉冲设备钱包支付
	 * @param model
	 * @return {@link Object}
	 */
	@RequestMapping("/inCoinsWalletPay")
	@ResponseBody
	public Object inCoinsWalletPay() {
		String openid = request.getParameter("openId");
		String code = request.getParameter("code");
		String port = request.getParameter("port");
		String tempid = request.getParameter("tempid");
		TemplateSon templateSon = templateService.getInfoTemplateOne(Integer.parseInt(tempid));
		Double money = CommUtil.toDouble(templateSon.getMoney());
		Double remark = templateSon.getRemark();
		User user = userService.getUserByOpenid(openid);
		if (user == null || user.getBalance() < money) {
			return CommUtil.responseBuild(2, "失败", "失败");
		}
		if (user.getMerid() == null || user.getMerid() == 0 || getUserIsUseCode(user, code) == false) {
			return CommUtil.responseBuild(4, "失败", "失败");
		}
		String ordernum = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + code;
		try {
			inCoinsService.insertInCoinsRecord(ordernum, code, Byte.parseByte(port), user.getId(), money,
					Byte.parseByte(WxPayController.doubleToString(remark)), (byte) 6,(byte) 1);
//			SendMsgUtil.send_0x83(code, Byte.parseByte(port), Byte.parseByte(WxPayController.doubleToString(remark)));
//			WolfHttpRequest.sendIncoinsPaydata(code, Byte.parseByte(port), Byte.parseByte(WxPayController.doubleToString(remark)));
			UserEquipment userEquipment = userEquipmentService.getDivideintoByCode(code);
			InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
			if (inCoins == null) {
				return CommUtil.responseBuild(3, "失败", "失败");
			}
			if (userEquipment != null) {
				//-------------------------------------------------------------
				Double userbalance = CommUtil.toDouble(user.getBalance());
				Double usersendmoney = CommUtil.toDouble(user.getSendmoney());
				Double usermoney = CommUtil.addBig(userbalance, usersendmoney);
				Double coumoney = CommUtil.toDouble(money);
				Double opermoney = 0.00;
				Double opersendmoney = 0.00;
				Double topupbalance = 0.00;
				Double givebalance = 0.00;
				Double recharge = CommUtil.subBig(userbalance, coumoney);
				if(recharge>=0){
					opermoney = coumoney;
					opersendmoney = 0.00;
					topupbalance = CommUtil.subBig(userbalance, opermoney);
					givebalance = usersendmoney;
				}else{
					opermoney = userbalance;
					opersendmoney = CommUtil.toDouble(Math.abs(recharge));
					topupbalance = 0.00;
					givebalance = CommUtil.subBig(usersendmoney, opersendmoney);
				}
				Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
				Double operbalance = CommUtil.addBig(topupbalance, givebalance);
				
				User user2 = new User();
				user2.setId(user.getId());
				user2.setBalance(topupbalance);
				user2.setSendmoney(givebalance);
				userService.updateUserById(user2);
				//--------------------------------------
				User meruser = userService.selectUserById(userEquipment.getUserId());
				generalDetailService.insertGenWalletDetail(user.getId(), meruser.getId(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, ordernum, new Date(), 3);
				String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=3&id="+inCoins.getId();
				String phone = getTempPhone(inCoins.getEquipmentnum(), inCoins.getMerchantid(), 2);
				TempMsgUtil.paychargesendmsg("您好，付款成功，开始充电", inCoins.getUid(), phone, urltem,inCoins.getMoney(),CommonConfig.PAYSUCCEEDMES);
				
				tradeRecordService.insertTrade(meruser.getId(), inCoins.getUid(), inCoins.getOrdernum(),
						inCoins.getMoney(), inCoins.getMoney(), inCoins.getEquipmentnum(), 2, 1, 1,
						"03");
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("orderNum", ordernum);
			data.put("payMoney", money);
			return CommUtil.responseBuild(1, "成功", data);
		} catch (Exception e) {
			logger.warn("订单号：" + ordernum + "---投币充电异常！" + e.getMessage() + e.getStackTrace()[0].getLineNumber());
			return CommUtil.responseBuild(3, "失败", "失败");
		}
	}
	
	@RequestMapping("/offlineWalletPay")
	@ResponseBody
	public String offlineWalletPay() {
		String openid = request.getParameter("openId");
		String code = request.getParameter("code");
		String tempid = request.getParameter("tempid");
		String card_id = request.getParameter("card_id");
		String card_ope = request.getParameter("card_ope");
		TemplateSon templateSon = templateService.getInfoTemplateOne(Integer.parseInt(tempid));
		Double money = CommUtil.toDouble(templateSon.getMoney());
		Double remark = templateSon.getRemark();
		Integer uId  = CommUtil.toInteger(request.getParameter("uid"));
		User user = userService.getUserByOpenid(openid);
		//User user = userService.selectUserById(uId);
		if (user == null || user.getBalance() < money) {
			return "2";
		}
		if (user.getMerid() == null || user.getMerid() == 0 || getUserIsUseCode(user, code) == false) {
			return "4";
		}
		String ordernum = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + code;
		try {
			Integer uid = user.getId();
			offlineCardService.insertChargeMoneyOfflineCardRecord(ordernum, code, card_id,
					templateSon.getMoney(), templateSon.getRemark(), Integer.parseInt(card_ope), 5, uid,1);
			UserEquipment userEquipment = userEquipmentService.getDivideintoByCode(code);
			OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
			try {
				int parseLong = (int) Long.parseLong(card_id, 16);
				String accountmoney = String.valueOf(remark * 10);
				String sendmoney = accountmoney.substring(0, accountmoney.indexOf("."));
				short parseShort = Short.parseShort(sendmoney);
//				SendMsgUtil.send_0x22(code, parseLong, parseShort, (byte) 1);
			} catch (Exception e) {
				logger.warn(ordernum + "离线卡充值异常" + e.getMessage() + e.getStackTrace()[0].getLineNumber());
			}
			if (userEquipment != null) {
				//-------------------------------------------------------------
				Double userbalance = CommUtil.toDouble(user.getBalance());
				Double usersendmoney = CommUtil.toDouble(user.getSendmoney());
				Double usermoney = CommUtil.addBig(userbalance, usersendmoney);
				Double coumoney = CommUtil.toDouble(money);
				Double opermoney = 0.00;
				Double opersendmoney = 0.00;
				Double topupbalance = 0.00;
				Double givebalance = 0.00;
				Double recharge = CommUtil.subBig(userbalance, coumoney);
				if(recharge>=0){
					opermoney = coumoney;
					opersendmoney = 0.00;
					topupbalance = CommUtil.subBig(userbalance, opermoney);
					givebalance = usersendmoney;
				}else{
					opermoney = userbalance;
					opersendmoney = CommUtil.toDouble(Math.abs(recharge));
					topupbalance = 0.00;
					givebalance = CommUtil.subBig(usersendmoney, opersendmoney);
				}
				Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
				Double operbalance = CommUtil.addBig(topupbalance, givebalance);
				
				User user2 = new User();
				user2.setId(user.getId());
				user2.setBalance(topupbalance);
				user2.setSendmoney(givebalance);
				userService.updateUserById(user2);
				//--------------------------------------
				User meruser = userService.selectUserById(userEquipment.getUserId());
				generalDetailService.insertGenWalletDetail(user.getId(), meruser.getId(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, ordernum, new Date(), 4);
				String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=2&id="+offlineCard.getId();
				String phone = getTempPhone(offlineCard.getEquipmentnum(), offlineCard.getMerchantid(), 1);
				String first = "您好，离线卡充值成功，欢迎您的使用。\r\n订单编号："+ordernum;
				TempMsgUtil.paychargesendmsg(first, uid, phone, urltem,offlineCard.getChargemoney(), CommonConfig.PAYSUCCEEDMES);
				
				tradeRecordService.insertTrade(meruser.getId(), uid, ordernum,money, money, code, 3, 1, 1,"03");
			}
			return "1";
		} catch (Exception e) {
			logger.warn("订单号：" + ordernum + "---离线卡充值异常！" + e.getMessage() + e.getStackTrace()[0].getLineNumber());
			return "3";
		}
	}
	
	public void paychargesendmsg(String first, Integer uid, String phone, String url, Double money){//付款成功
		try {
			User user = userService.selectUserById(uid);
			if(null!=user){
				String oppenid = user.getOpenid();
				JSONObject json = new JSONObject();
				json.put("first", TempMsgUtil.inforData( first,"#FF3333"));
				json.put("keyword1", TempMsgUtil.inforData("自助充电平台","#0044BB"));
				json.put("keyword2", TempMsgUtil.inforData("¥ "+String.format("%.2f", money),"#0044BB"));
				json.put("keyword3", TempMsgUtil.inforData(StringUtil.toDateTime(),"#0044BB")); 
				json.put("keyword4", TempMsgUtil.inforData(phone,"#0044BB"));
				json.put("remark", TempMsgUtil.inforData(CommonConfig.PAYSUCCEEDMES,"#0044BB"));
				TempMsgUtil.sendTempMsg(oppenid, TempMsgUtil.TEMP_IDPAY, url, json);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	public boolean getUserIsUseCode(User user, String code) {
		Map<String, Object> devicemap = userEquipmentService.getEquipToUserByCode(code);

		Integer devareaid = CommUtil.toInteger(devicemap.get("aid"));
		Integer touareaid = CommUtil.toInteger(user.getAid());

		Integer devdealid = CommUtil.toInteger(devicemap.get("dealid"));
		Integer toudealid = CommUtil.toInteger(user.getMerid());
		if(devdealid.equals(toudealid)){//判断设备商户id（）与用户所属id（）是否一致
			if(devareaid.equals(touareaid)){//判断设备所属小区id（）与用户所属小区id（）是否一致
				return true;
			}else{
				List<Map<String, Object>> touarealist = areaService.inquirePartnerInfo(touareaid, 2);
				Integer areapartner = touarealist.size();//用户所属小区合伙人数量
				if(areapartner>0){
					return false;
				}else{
					List<Map<String, Object>> devarealist = areaService.inquirePartnerInfo(devareaid, 2);
					Integer devpartner = devarealist.size();//设备合伙人数量
					if(devpartner>0) return false;
					return true;
				}
			}
		}else{
			return false;
		}
	}
	
	/**
	 * get ipaddr
	 * 
	 * @param request
	 * @return ip
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * @author  origin          
	 * @param source order id
	 * @param model
	 */
	@RequestMapping("/sendmsgdetails")
	public String sendmsgdetails(Integer source, String ordernum, Integer id, Model model) {
		if(source==1){
			ChargeRecord order;
			if(ordernum!=null && !ordernum.equals("")){
				List<ChargeRecord> orderlist = chargeRecordService.getOrderByOrdernum(ordernum);
				order = orderlist.get(0);
			}else{
				order = chargeRecordService.chargeRecordOne(id);
			}
			model.addAttribute("order", order);
		}else if(source==2){
			OfflineCard order = offlineCardService.selectOfflineCardById(id);
			model.addAttribute("order", order);
		}else if(source==3){
			InCoins order = inCoinsService.selectInCoinsRecordById(id);
			model.addAttribute("order", order);
		}else if(source==4){
			Money order = moneyService.payMoneyinfo(id);
			model.addAttribute("order", order);
		}else if(source==5){
			OnlineCardRecord order = onlineCardRecordService.selectOnlineCard(id);
			model.addAttribute("order", order);
		}
		model.addAttribute("source", source);
		return "general/sendmsgdetails";
	}
	
	/**
	 * @Description： 商户查询，交易记录查询用户消费信息
	 * @author： origin          
	 * 创建时间：   2019年5月30日 下午5:04:46
	 */
	@RequestMapping("/touristmoneychange")
	public String touristMoneyChange( Integer uid, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		String time = StringUtil.getPastDate(180);
		String beginTime = time + " 00:00:00";
		List<Map<String, Object>> consumeinfo = generalDetailService.touristMoneyChange( user.getId(), beginTime, StringUtil.toDateTime(), uid, 0);
		for(Map<String, Object> item : consumeinfo){
			item.put("topupmoney", CommUtil.toDouble(item.get("money")));//充值金额
			item.put("accountmoney", CommUtil.toDouble(item.get("tomoney")));//账户金额
//			Double topupbalance = CommUtil.toDouble(item.get("topupbalance"));//充值余额
//			Double sendbalance = CommUtil.toDouble(item.get("givebalance"));//赠送余额
			item.put("sendbalance", CommUtil.toDouble(item.get("givebalance")));//赠送余额
			item.put("accountbalance", CommUtil.toDouble(item.get("balance")));//账户余额
		}
		model.addAttribute("consumeinfo", consumeinfo);
		model.addAttribute("size", consumeinfo.size());
		model.addAttribute("uid", uid);
		model.addAttribute("beginTime", beginTime);
		model.addAttribute("endTime", StringUtil.toDateTime());
		model.addAttribute("offset", 0);
		return "general/touristconsumeinfo";
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
				User touuser = userService.selectUserById(uid);
				if(touuser!=null) merid = touuser.getMerid();
			}
			User meruser = userService.selectUserById(merid);
			if(meruser!=null) telphone = meruser.getPhoneNum(); 
		}
		map.put("telphone", telphone);
		return map;
	}
	//=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*=-~*
	/**
	 * @Description：
	 * @param cardnum
	 * @param phone
	 * @param model
	 * @return
	 * @author： origin
	 * @createTime：2020年3月25日下午6:22:26
	 * @comment:
	 */
	@RequestMapping(value = "/bindingOnlineByPhone")
	@ResponseBody
	public Map<String, Object> bindingOnlineByPhone(String cardnum, String phone){
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			phone = CommUtil.toString(phone);
			cardnum = CommUtil.toString(cardnum);
			if(phone==null || cardnum==null){
				return CommUtil.responseBuildInfo(201, "手机号或卡号不能为空", datamap);
			}
			User touuser = userService.getUserByPhoneNum(phone);
			Integer tourid = null;
			if(touuser==null){
				User user = new User();
				user.setPhoneNum(phone);
				user.setServephone(phone);
				String password = phone.substring(4);
				user.setPassword(MD5Util.MD5Encode(password, "utf-8"));
				user.setLevel(2);
				user.setCreateTime(new Date());
				userService.addUser(user);
				touuser = userService.getUserByPhoneNum(phone);
				tourid = CommUtil.toInteger(touuser.getId());
//				CommUtil.responseBuildInfo(101, "该在线卡未被绑定", datamap);
			}else{
				tourid = CommUtil.toInteger(touuser.getId());
			}
			Map<String, Object> result = onlineCardService.bindingOnlineByUid(tourid, cardnum);
			datamap = result;
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	/**
	 * @Description： 用户扫码充值在线卡 (公众号外扫卡充值)
	 * @author： origin          
	 * 创建时间：   2019年6月3日 下午4:19:24
	 */
	@RequestMapping(value = "/onlinecardpay")
	public String onlinecardPay(String cardNumber, Integer source, Model model) throws Exception {
		String url = "/general/onlinecardwechattopup";
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			if(source==null || cardNumber==null){
				CommUtil.responseBuildInfo(106, "未获取到路径信息，不能充值，请从新扫描", datamap);
				model.addAttribute("datamap", datamap);
				return "/general/onlinecardwechattopup";
			}
			if(source.equals(2)) url = "/general/onlinecardalitopup";
			model.addAttribute("cardID", cardNumber);
			
			OnlineCard online = onlineCardService.selectOnlineCardByFigure(cardNumber);
			online = online == null ? new OnlineCard() : online;
			//在线卡信息
			Integer onlineid = CommUtil.toInteger(online.getId());
			Integer uid = CommUtil.toInteger(online.getUid());
			Integer dealid = CommUtil.toInteger(online.getMerid());
			Double money = CommUtil.toDouble(online.getMoney());
			Integer status = CommUtil.toInteger(online.getStatus());
			Integer relewallet = CommUtil.toInteger(online.getRelevawalt());
			Integer areaid = CommUtil.toInteger(online.getAid());
			User touser = new User();
			User meruser = new User();
			if(!uid.equals(0)) touser = userService.selectUserById(uid);
			// 用户id等于0或者商家id等于0
			logger.info("在线卡商家的id======"+dealid+"用户id"+uid);
			
			if(dealid == 0){
				CommUtil.responseBuildInfo(201, "该在线卡未激活", datamap);
			}else if(uid.equals(0)){
				CommUtil.responseBuildInfo(101, "该在线卡未被绑定", datamap);
			}else if(money>0 && status.equals(0)){
				CommUtil.responseBuildInfo(103, "在线卡未激活只能充值一次", datamap);
			}else if(status.equals(2)){
				CommUtil.responseBuildInfo(104, "在线卡为挂失卡，不能充值", datamap);
			}else if(!uid.equals(0) && status.equals(4)){
				CommUtil.responseBuildInfo(105, "在线卡已被注销，不能充值", datamap);
			}
			if(!onlineid.equals(0) && relewallet.equals(1)){//1:关联用户钱包
				online.setMoney(touser.getBalance());
				online.setSendmoney(touser.getSendmoney());
				Integer tousmerid = CommUtil.toInteger(touser.getMerid());
				if(!dealid.equals(tousmerid)) CommUtil.responseBuildInfo(106, "该在线卡关联钱包商户与卡商户不一致", datamap);
			}
			if(dealid != 0) meruser = userService.selectUserById(dealid);
			
			String phone = CommUtil.toString(touser.getPhoneNum());
			String dealerphne = CommUtil.toString(meruser.getPhoneNum());
			String servephone = CommUtil.toString(meruser.getServephone());
			String dealservephone = servephone == null ? dealerphne : servephone;
			
			datamap.put("telphone", dealservephone);
			Area area = areaService.selectByIdArea(areaid);
			
			model.addAttribute("areadata", area);
			model.addAttribute("onlineuser", touser);
			model.addAttribute("online", online);
			model.addAttribute("user", touser);
			model.addAttribute("meruser", meruser);
			
			String codedata = CommUtil.toString(datamap.get("code"));
			if(source==2){//支付宝
				if(codedata!= null){//此时判断不满足条件
					model.addAttribute("datamap", datamap);
					return url;
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
				} catch (AlipayApiException e) {//处理异常
					e.printStackTrace();
				}
				if(phone==null){
					CommUtil.responseBuildInfo(108, "该在线卡用户未绑定手机号，请现在微信绑定手机号", datamap);
					return url;
				}
				datamap = getOnlineCardTemp(touser, online, datamap);//获取模板信息
				CommUtil.responseBuildInfo(200, "成功", datamap);
				model.addAttribute("datamap", datamap);
			}else if(source==1){//微信
				// 微信返回的code
				String wxCode = request.getParameter("code");
				// 特约商户下用户的openid
				String subOpenid = CommUtil.toString(request.getParameter("subOpenid"));
				Integer opeid = null;
				JSONObject doGetStr = null;
				String openid = null;
				User user = null;
				// 在线卡绑定商家,绑定用户
				if(meruser.getSubMer() != null && meruser.getSubMer().equals(1) && uid != 0){
					if(subOpenid == null){
						// 对服务商的Appid授权
						doGetStr = WeixinUtil.WeChatTokenSubMerByMerId(wxCode, dealid);
				    	if(!doGetStr.has("openid")){
							model.addAttribute("openiderror", "1您的账户暂时无法访问");
							return "openiderror";
						}
				    	// 根据服务商的openid查询用户
				    	openid = doGetStr.getString("openid");
				    	user = userService.selectSubUserByOpenid(openid);
				    	logger.info("用户重定向前的数据==="+cardNumber+"==="+openid);
				    	if(user == null){
				    		logger.info("开始重定向");
				    		String addr = "/oauth2onlineSubUser?cardNumber="+cardNumber+"&subOpenid="+openid;
				    		return "redirect:"+addr;
				    	}
				    	model.addAttribute("user", user);
				    	opeid = CommUtil.toInteger(user.getId());
					}
					if(subOpenid != null){
						// 对平台appid授权
				    	logger.info("用户有自己的subopenid========="+subOpenid);
						JSONObject heTengUserOpenid  = WeixinUtil.WeChatToken(wxCode);
						if(!heTengUserOpenid.has("openid")){
							model.addAttribute("openiderror", "2您的账户暂时无法访问");
							return "openiderror";
						}
						// 根据平台appid获取平台用户的信息
						User user2 = userService.getUserByOpenid(heTengUserOpenid.getString("openid"));
						if(user2 == null){
							model.addAttribute("openiderror", "3您的账户暂时无法访问");
							return "openiderror";
						}
						openid = subOpenid;
						// 将用户的信息和服务商的openid进行存储
						User fuWuUser = new User();
						fuWuUser.setId(user2.getId());
						fuWuUser.setOpenid(subOpenid);
						userService.addSubUser(fuWuUser);
						// 查询服务商下的用户
						user = userService.selectSubUserByOpenid(openid);
						if(user == null){
							model.addAttribute("openiderror", "4您的账户暂时无法访问");
							return "openiderror";
						}
						model.addAttribute("user", user);
						opeid = CommUtil.toInteger(user.getId());
					}
				}else{
					try {
						doGetStr = WeixinUtil.WeChatToken(wxCode);
						if (!doGetStr.has("openid")) {
							CommUtil.responseBuildInfo(106, "未获取到微信信息，不能充值，请从新扫描", datamap);
							model.addAttribute("datamap", datamap);
							return "/general/onlinecardwechattopup";
						}
						openid = doGetStr.getString("openid");
						user = userService.getUserByOpenid(openid);//查询该微信用户是否存在
						if(user==null){//添加（数据库不存在用户信息）
							checkDisposeUser(openid, dealid, areaid);
//							checkUserAndDispose(openid);
							user = userService.getUserByOpenid(openid);
						}
						request.getSession().setAttribute("user",user);
						if(codedata!= null){//此时判断不满足条件
							model.addAttribute("datamap", datamap);
							return url;
						}
						if(user==null){
							CommUtil.responseBuildInfo(107, "未获取到扫码用户信息，不能充值", datamap);
							model.addAttribute("datamap", datamap);
							return "/general/onlinecardwechattopup";
						}
						opeid = CommUtil.toInteger(user.getId());
						model.addAttribute("user", user);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(phone==null && opeid.equals(uid)){//在线卡用户不存在电话且操作人和在线卡拥有人一致
					model.addAttribute("source", 2);
					model.addAttribute("datamap", datamap);
					return "merchant/bindPhone";
				}
				datamap = getOnlineCardTemp(touser, online, datamap);//获取模板信息
				CommUtil.responseBuildInfo(200, "成功", datamap);
				model.addAttribute("datamap", datamap);
			}
		}catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
			model.addAttribute("datamap", datamap);
		}
		return url;
	}
	
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月13日 下午5:12:18
	 */
	@RequestMapping(value = "/onlinebingrefresh")
	public String onlineBingreFresh(String card, Model model) throws Exception {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			User touser = (User) request.getSession().getAttribute("user");
			OnlineCard online = onlineCardService.selectOnlineCardByFigure(card);
			online = online == null ? new OnlineCard() : online;
			Integer onlineid = CommUtil.toInteger(online.getId());
			Integer uid = CommUtil.toInteger(online.getUid());
			Integer dealid = CommUtil.toInteger(online.getMerid());
			Double money = CommUtil.toDouble(online.getMoney());
			Integer areaid = CommUtil.toInteger(online.getAid());
			Integer status = CommUtil.toInteger(online.getStatus());
			Integer relewallet = CommUtil.toInteger(online.getRelevawalt());
			User meruser = new User();
			if(uid.equals(0)){
				CommUtil.responseBuildInfo(101, "该在线卡未被绑定", datamap);
			}else if(dealid.equals(0)){
				CommUtil.responseBuildInfo(100, "该在线卡未激活", datamap);
			}else if(money>0 && status.equals(0)){
				CommUtil.responseBuildInfo(103, "在线卡未激活只能充值一次", datamap);
			}else if(status.equals(2)){
				CommUtil.responseBuildInfo(104, "在线卡为挂失卡，不能充值", datamap);
			}else if(!uid.equals(0) && status.equals(4)){
				CommUtil.responseBuildInfo(105, "在线卡已被注销，不能充值", datamap);
			}
			if(!dealid.equals(0)) meruser = userService.selectUserById(dealid);
			if(!onlineid.equals(0) && relewallet.equals(1)){
				online.setMoney(touser.getBalance());
				online.setSendmoney(touser.getSendmoney());
			}
			String phone = CommUtil.toString(touser.getPhoneNum());
			String dealerphne = CommUtil.toString(meruser.getPhoneNum());
			String servephone = CommUtil.toString(meruser.getServephone());
			String dealservephone = servephone == null ? dealerphne : servephone;
			datamap.put("telphone", dealservephone);
			
			Area area = areaService.selectByIdArea(areaid);
			model.addAttribute("areadata", area);
			model.addAttribute("source", 1);
			model.addAttribute("cardID", card);
			model.addAttribute("online", online);
			model.addAttribute("onlineuser", touser);
			model.addAttribute("user", touser);
			model.addAttribute("meruser", meruser);
			
			String codedata = CommUtil.toString(datamap.get("code"));
			if(codedata!= null){
				model.addAttribute("datamap", datamap);
				return "/general/onlinecardwechattopup";
			}
//			datamap = verifyOnlineCard(online);//判断在线卡状态
			if(phone==null){
				model.addAttribute("source", 2);
				model.addAttribute("datamap", datamap);
				return "merchant/bindPhone";
			}
			datamap = getOnlineCardTemp(touser, online, datamap);
			CommUtil.responseBuildInfo(200, "成功绑定", datamap);
			model.addAttribute("datamap", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return  "/general/onlinecardwechattopup";
	}
	
	/**
	 * @Description： 根据用户信息获取在线卡充值模板
	 * @author： origin          
	 */
	public Map<String, Object> getOnlineCardTemp(User tourist, OnlineCard online, Map<String, Object> datamap) {
		Integer wallet = CommUtil.toInteger(online.getRelevawalt());
		Integer uid = CommUtil.toInteger(online.getUid());
		Integer areaid = CommUtil.toInteger(online.getAid());
		Integer dealid = CommUtil.toInteger(online.getMerid());
		List<TemplateSon> sontemp = null;
		if(wallet.equals(1)){
			if (!areaid.equals(0)) {
				Area area = areaService.selectByIdArea(areaid);
				Integer tempid = CommUtil.toInteger(area.getTempid());
				if(!tempid.equals(0)){
					sontemp = templateService.getSonTemplateLists(tempid);
				}else{
					List<TemplateParent> parentTemplist = templateService.getParentTemplateListByMerchantid(0, 3);
//					List<TemplateParent> parentTemplist = templateService.getParentTemplateListByMerchantid(dealid, 3);
//					if (parentTemplist == null || parentTemplist.size() == 0) {
//					}
					sontemp = templateService.getSonTemplateLists(parentTemplist.get(0).getId());
				}
			}else{
				List<TemplateParent> parentTemplist = templateService.getParentTemplateListByMerchantid(dealid, 3);
				if (parentTemplist == null || parentTemplist.size() == 0) {
					parentTemplist = templateService.getParentTemplateListByMerchantid(0, 3);
				}
				sontemp = templateService.getSonTemplateLists(parentTemplist.get(0).getId());
				datamap.put("pertem", parentTemplist.get(0).getId());
				datamap.put("ptemphone", parentTemplist.get(0).getCommon1());
			}
		}else{
			if (!areaid.equals(0)) {
				Area area = areaService.selectByIdArea(areaid);
				Integer tempid = CommUtil.toInteger(area.getTempid2());
				if(!tempid.equals(0)){
					sontemp = templateService.getSonTemplateLists(CommUtil.toInteger(tempid));
				}
				if(sontemp==null){
					List<TemplateParent> templist = templateService.getParentTemplateListByMerchantid(0, 4);
					sontemp = templateService.getSonTemplateLists(templist.get(0).getId());
				}
			}else{
				List<TemplateParent> parentTemplist = templateService.getParentTemplateListByMerchantid(0, 4);
//				List<TemplateParent> parentTemplist = templateService.getParentTemplateListByMerchantid(dealid, 4);
//				if (parentTemplist == null || parentTemplist.size() == 0) {
//					parentTemplist = templateService.getParentTemplateListByMerchantid(0, 4);
//				}
				sontemp = templateService.getSonTemplateLists(parentTemplist.get(0).getId());
				datamap.put("pertem", parentTemplist.get(0).getId());
				datamap.put("ptemphone", parentTemplist.get(0).getCommon1());
			}
		}
		datamap.put("sontemp", sontemp);
		return datamap;
	}
	
	public void checkDisposeUser(String openid, Integer dealid, Integer areaid){
		User user = new User();
		user.setOpenid(openid);
		user.setEarnings(Double.valueOf(0.00));
		user.setLevel(1);
		user.setCreateTime(new Date());
		userService.addUser(user);
	}
	
	public void checkUserAndDispose(String openid) {
		try {
			User userByOpenid = userService.getUserByOpenid(openid);
			if (userByOpenid == null) {
				User user = new User();
				user.setOpenid(openid);
				user.setEarnings(Double.valueOf(0.00));
				user.setLevel(1);
				user.setCreateTime(new Date());
				userService.addUser(user);
			}
		} catch (Exception e) {
			logger.warn("在线卡添加用户失败：" + e.getMessage() + "---报错位置：" + ByteUtils.getLineNumber(e));
		}
	}
	
	/**
	 * @Description： 根据用户信息获取在线卡充值模板
	 * @author： origin          
	 */
	public Map<String, Object> geticonlinetemp(User oriuser, OnlineCard online) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		Integer vawalt = CommUtil.toInteger(online.getRelevawalt());
		User user = userService.selectUserById(online.getUid());
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
//				Area area = areaService.selectByIdArea(user.getAid());
				Area area = areaService.selectByIdArea(online.getAid());
				if (area != null && area.getTempid2() != null) {
					sontemp = templateService.getSonTemplateLists(area.getTempid2());
					codemap.put("code", 200);
					codemap.put("sontemp", sontemp);
					return codemap;
				} 
			}
			Integer merid = CommUtil.toInteger(online.getMerid());
			if (merid == null || merid == 0) merid = CommUtil.toInteger(user.getMerid());
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
	 * @Description： 扫码时查询IC卡消费信息记录
	 * @author： origin          
	 * 创建时间：   2019年6月22日 上午11:15:53
	 */
	@RequestMapping(value = "/scanonlinedemand")
	public String scanOnlinedEmand(String cardnum, Model model) {
		String time = StringUtil.getPastDate(100);
		String beginTime = time + " 00:00:00";
		OnlineCard online = onlineCardService.selectOnlineCardByCardID(cardnum);
		//OnlineCard online = onlineCardService.selectOnlineCardByFigure(cardnum);
		Integer relevawalt = CommUtil.toInteger(online.getRelevawalt());
		List<Map<String, Object>> iccardrecord = null;
//		if(relevawalt==2){
//			iccardrecord = onlineCardRecordService.icCardRecord( cardnum, null, beginTime, StringUtil.toDateTime(), null, 0);
//		}else{
//			iccardrecord = generalDetailService.selectWalletDetail(online.getUid());
//		}
		if(relevawalt==2){
			iccardrecord = onlineCardRecordService.icCardRecord( cardnum, null, beginTime, StringUtil.toDateTime(), null, 0);
			for(Map<String, Object> item : iccardrecord){
				item.put("topupmoney", CommUtil.toDouble(item.get("money")));//充值金额
				item.put("accountmoney", CommUtil.toDouble(item.get("accountmoney")));//账户金额
				Double topupbalance = CommUtil.toDouble(item.get("topupbalance"));//充值余额
				Double sendbalance = CommUtil.toDouble(item.get("givebalance"));//赠送余额
				item.put("sendbalance", CommUtil.toDouble(item.get("givebalance")));//赠送余额
				item.put("accountbalance", CommUtil.addBig(topupbalance, sendbalance));//账户余额
			}
		}else{
			iccardrecord = generalDetailService.selectWalletDetail(online.getUid());
			for(Map<String, Object> item : iccardrecord){
				item.put("topupmoney", CommUtil.toDouble(item.get("money")));//充值金额
				item.put("accountmoney", CommUtil.toDouble(item.get("tomoney")));//账户金额
				item.put("sendbalance", CommUtil.toDouble(item.get("givebalance")));//赠送余额
				item.put("accountbalance", CommUtil.toDouble(item.get("balance")));//赠送余额
			}
		}
		model.addAttribute("iccardrecord", iccardrecord);
		model.addAttribute("size", iccardrecord.size());
		model.addAttribute("cardID", online.getFigure());
		model.addAttribute("relevawalt", relevawalt);
		return "general/onlinecardrecord";
	}
	
	/**
	 * 用户公众号内部点击包月信息
	 * @param uid 用户的id
	 * @param model
	 * @return {@link String}
	 */
	@RequestMapping("packageMonthInfo")
	public String packageMonthInfo(Integer uid,String openid, Model model) {
		User user = userService.selectUserById(uid);
		if (user != null && openid != null && !"".equals(openid)) {
			if (user.getMerid() == null || user.getMerid() == 0) {
				model.addAttribute("errortype", "1");
			} else {
				List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantidwolf(user.getMerid(), 5);
				if (templatelist != null && templatelist.size() != 0) {
					PackageMonth packageMonth = userService.selectPackageMonthByUid(uid);
					TemplateParent templateParent = templatelist.get(0);
					if (packageMonth != null) {//TODO
						model.addAttribute("hasMonth", "1");
						try {
							packageMonth.setEverymonthnum(Integer.parseInt(templateParent.getCommon1()));
							packageMonth.setEverydaynum(Integer.parseInt(templateParent.getRemark()));
						} catch (Exception e) {
						}
						model.addAttribute("packageMonth", packageMonth);
					} else {
						model.addAttribute("hasMonth", "0");
					}
					model.addAttribute("ifmonth", templateParent.getIfmonth());
					List<TemplateSon> sonTemplateLists = templateService.getSonTemplateLists(templateParent.getId());
					model.addAttribute("errortype", "3");
					model.addAttribute("templatelist", sonTemplateLists);
				} else {
					model.addAttribute("errortype", "2");
				}
			}
			model.addAttribute("openid", openid);
			return "general/packageMonthInfo";
		} else {
			logger.info("发生错误="+uid+"="+openid);
			return "erroruser";
		}
	}
	
	/**
	 * 用户公众号包月点击确认充值
	 * @param tempid 模板id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/packageMonthPay", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> packageMonthPay(Integer tempid,String openid) throws Exception {
		if(tempid == null){
			logger.info("模板的id错误");
		}
		if(openid == null || "".equals(openid)){
			logger.info("用户的openid错误");
		}
		User user = (User) request.getSession().getAttribute("user");
		TemplateSon templateSon = templateService.getInfoTemplateOne(tempid);
		Double money2 = templateSon.getMoney();
		String common1 = templateSon.getCommon1();
		Date date = new Date();
		String ordernum = HttpRequest.createOrdernum(6);
		Map<String, Object> data =  new HashMap<String, Object>();
		if(user == null || user.getMerid() == null){
			data.put("error", "用户参数错误");
			return data;
		}
		// 根据商户的id查询商户的信息
		User subMer = userService.selectUserById(user.getMerid());
		SortedMap<String, String> params = null;
		String appId = null;
		// 判断商户是否为微信特约商户
		if(subMer != null && subMer.getSubMer().equals(1)){
			// 查询微信特约商户的配置信息
			logger.info("使用接口:packageMonthPay对微信特约商户付款");
			request.setAttribute("subMerId", subMer.getId());
			Map<String, Object> merConfigdata = userService.selectSubMerConfigById(user.getMerid());
			appId = CommUtil.toString( merConfigdata.get("subappid"));
			// 此时的openID应该传入特约商户的openID
			// 不能传入服务商户的id
			params = packagePayParamsToMapSub(request, money2, openid, "general/monthPayback", ordernum, "包月支付");
		}else{
			logger.info("使用接口:packageMonthPay对服务商平台付款");
			params = WxPayController.packagePayParamsToMap(request, money2, user.getOpenid(), "general/monthPayback", ordernum, "包月支付");
			appId = WeiXinConfigParam.APPID;
		}
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String canshu = HttpRequest.getRequestXml(params);
		String sr = HttpRequest.sendPost(url, canshu);
		Map<String, String> map = XMLUtil.doXMLParse(sr);
		SortedMap<String, String> seconde = new TreeMap<>();
		seconde.put("appId", appId);
		String time = HttpRequest.getTimeStamp();
		seconde.put("timeStamp", time);
		String sjzf = HttpRequest.getRandomStringByLength(30);
		
		seconde.put("nonceStr", sjzf);
		seconde.put("package", "prepay_id=" + (String) map.get("prepay_id"));
		seconde.put("signType", "MD5");
		
		String sign2 = HttpRequest.createSign("UTF-8", seconde);
		Integer monthnum = Integer.parseInt(common1);
		TemplateParent templateParent = templateService.getParentTemplateOne(templateSon.getTempparid());
		String remark = templateParent.getRemark();
		Integer everydaynum = Integer.parseInt(remark);
		String everymonthnumStr = templateParent.getCommon1();
		Integer everymonthnum = Integer.parseInt(everymonthnumStr);
		Integer surpnum = everymonthnum * monthnum;
		Integer timedata = templateParent.getCommon2();
		String elecStr = templateParent.getCommon3();
		double elec = Double.parseDouble(elecStr);
		Integer changenum = everymonthnum * monthnum;
		PackageMonth packageMonth = userService.selectPackageMonthByUid(user.getId());
		if (packageMonth != null) {
			surpnum = surpnum + packageMonth.getSurpnum();
		}
		if (everydaynum == 0) {
			everydaynum = -1;
		}
		
		if (everymonthnum == 0) {
			surpnum = -1;
		}
		
		userService.insertPackageMonthRecord(user.getId(),user.getMerid(), ordernum, money2, 1, 0, everydaynum, changenum, surpnum,monthnum,timedata,elec, date);
		
		Map<String, Object> paramMap = new HashMap<>();
		
		paramMap.put("appId", appId);
		paramMap.put("prepay_id", map.get("prepay_id"));
		paramMap.put("date", time);
		paramMap.put("paySign", sign2);
		paramMap.put("packagess", "prepay_id=" + (String) map.get("prepay_id"));
		paramMap.put("nonceStr", sjzf);
		paramMap.put("signType", "MD5");
		paramMap.put("out_trade_no", params.get("out_trade_no"));
		paramMap.put("cardifexist", "1");
		return paramMap;
	}
	
	/**
	 * 包月微信支付回调
	 * @return
	 * @throws JDOMException
	 */
	@RequestMapping({ "/monthPayback" })
	@ResponseBody
	public String monthPayback() throws JDOMException {
		
		String resXml = "";
		Map<String, String> backxml = new HashMap<String, String>();
		
		InputStream inStream;
		try {
			inStream = request.getInputStream();
			
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			// logger.error("微信支付----付款成功----");
			outSteam.close();
			inStream.close();
			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
			// logger.error("微信支付----result----=" + result);
			// Map<Object, Object> map = Xmlunit.xml2map(result, false);
			Map<Object, Object> map = XMLUtil.doXMLParse(result);
			
			if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
				// ------------------------------
				String ordernum = map.get("out_trade_no").toString();
				logger.error("包月微信支付----返回成功" + ordernum);
				// 根据商家订单查询包月充值记录
				PackageMonthRecord monthRecord = userService.selectMonthRecordByOrdernum(ordernum,null);
				Integer status = monthRecord.getStatus();
				if (status == 0) {
					// 更新用户的包月记录的状态
					userService.updatePackageMonthRecord(null,null,ordernum, 1);
					Double money = monthRecord.getMoney();
					Integer changenum = monthRecord.getChangenum();
					Integer uid = monthRecord.getUid();
					Integer everydaynum = monthRecord.getEverydaynum();
					 Integer time = monthRecord.getTime();
					double elec = monthRecord.getElec();
					Integer changemonth = monthRecord.getChangemonth();
					// 根据用户的id查询用户得包月信息
					PackageMonth packageMonth = userService.selectPackageMonthByUid(uid);
					Integer surpnum = changenum;
					if (everydaynum == -1) {
						everydaynum = 0;
					}
					if (packageMonth == null) {
						userService.insertPackageMonth(uid, surpnum, everydaynum, everydaynum,changenum, changenum/changemonth,time,elec, monthRecord.getCreateTime(),changemonth);
					} else {
						surpnum = packageMonth.getSurpnum() + changenum;
						Date nowDate = new Date();
						Date endTime = packageMonth.getEndTime();
						if (nowDate.getTime() > endTime.getTime()) {
							endTime.setTime(nowDate.getTime());
						}
						userService.updatePackageMonth(uid, surpnum, everydaynum, everydaynum,changenum, changenum/changemonth,time,elec, endTime,changemonth);
					}
					// 根据用户的id查询用户信息
					User user = userService.selectUserById(uid);
					Integer merid = user.getMerid();
					// 根据商家的id查询商家的信息
					User subMerUser = userService.selectUserById(user.getMerid());
					Double earnings = subMerUser.getEarnings();
					// 普通商家执行更新商家收益
					String comment = "0";
					// 微信包月充值不分成
					if(subMerUser != null && subMerUser.getSubMer().equals(0)){
						userService.updateUserEarnings(1, money, merid);
					}else{
						// 特约商户的用户进行包月充值
						comment = "1";
					}
					// 插入交易记录
					tradeRecordService.insertToTrade(merid, 0, uid, ordernum, money, money, 0.0, comment, 6, 2, 1, comment);
					// 插入商家资金变动明细
					merchantDetailService.insertMerEarningDetail(merid, money, earnings + money, ordernum, monthRecord.getCreateTime(), 7, 2, 1);
					try {
						// 更新商户总计
						userService.updateMerAmount(1, money, merid);
					} catch (Exception e) {
						logger.warn("商户总计更改出错");
					}
				}
				// 处理业务完毕
				// ------------------------------
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				out.write(resXml.getBytes());
				out.flush();
				out.close();
			} else {
				// logger.info("支付失败,错误信息：" + map.get("err_code"));
				System.out.println(map.get("err_code") + "----" + map.get("out_trade_no").toString());
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				out.write(resXml.getBytes());
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			// logger.error("支付回调发布异常：" + e);
			e.printStackTrace();
		}
		return resXml;
	}
	
	@RequestMapping("/queryMonthRecord")
	public String queryMonthRecord(Integer uid,Model model) {
		List<PackageMonthRecord> monthList = userService.selectMonthRecordListByUid(uid);
		model.addAttribute("monthList", monthList);
		return "general/monthrecord";
	}
	
	@RequestMapping("/monthSendData")
	@ResponseBody
	public Map<String, String> monthSendData(String code,String openid, Byte portchoose) {
		User user = userService.getUserByOpenid(openid);
		Map<String, String> returnMap = new HashMap<>();
		if (user != null) {
			PackageMonth month = userService.selectPackageMonthByUid(user.getId());
			if (month != null) {
				UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
				if (userEquipment == null || !userEquipment.getUserId().equals(user.getMerid())) {
					returnMap.put("wolfval", "6");
					return returnMap;//开通包月的商户名下没有此设备
				}
				Integer everydaynum = month.getEverydaynum();
				Integer everymonthnum = month.getEverymonthnum();
				Integer surpnum = month.getSurpnum();
				Integer todaysurpnum = month.getTodaysurpnum();
				Date date = new Date();
				long nowtime = date.getTime();
				long endtime = month.getEndTime().getTime();
				String ordernum = HttpRequest.createOrdernum(6);
				if (nowtime > endtime) {
					returnMap.put("wolfval", "3");
					return returnMap;//代表时间已过期
				}
				String timeStr = month.getTime() + "";
				String elecStr = month.getElec() * 100 + "";
				try {
					Integer merid = user.getMerid();
					List<TemplateParent> tempParList = templateService.getParentTemplateListByMidwolf(merid, 5);
					if (tempParList.size() > 0) {
						TemplateParent templateParent = tempParList.get(0);
						everymonthnum = Integer.parseInt(templateParent.getCommon1());
						everydaynum = Integer.parseInt(templateParent.getRemark());
						timeStr = templateParent.getCommon2() + "";
						elecStr = Double.parseDouble(templateParent.getCommon3()) * 100 + "";
					}
				} catch (Exception e) {
					logger.warn("使用商户包月模板失败：");
					e.printStackTrace();
				}
				if (everydaynum != 0 && everymonthnum == 0) {
					if (todaysurpnum > 0) {
						PackageMonth packageMonth = new PackageMonth();
						packageMonth.setTodaysurpnum(todaysurpnum - 1);
						packageMonth.setUid(user.getId());
						userService.updatePackageMonthByEntity(packageMonth);
						userService.insertPackageMonthRecord(user.getId(),userEquipment.getUserId(), ordernum, 0.0, 2, 1, 
								todaysurpnum - 1, 1, -1, 0, month.getTime(), month.getElec(), 
								date);
					} else {
						returnMap.put("wolfval", "4");
						return returnMap;//今日次数已用完
					}
				} else if (everydaynum == 0 && everymonthnum != 0) {
					if (surpnum > 0) {
						PackageMonth packageMonth = new PackageMonth();
						packageMonth.setSurpnum(surpnum - 1);
						packageMonth.setUid(user.getId());
						userService.updatePackageMonthByEntity(packageMonth);
						userService.insertPackageMonthRecord(user.getId(), userEquipment.getUserId(), ordernum, 0.0, 2, 1, 
								-1, 1, surpnum - 1, 0, month.getTime(), month.getElec(), 
								date);
					} else {
						returnMap.put("wolfval", "5");
						return returnMap;//总次数已用完
					}
				} else if (everydaynum != 0 && everymonthnum != 0) {
					if (todaysurpnum > 0 && surpnum > 0) {
						PackageMonth packageMonth = new PackageMonth();
						packageMonth.setTodaysurpnum(todaysurpnum - 1);
						packageMonth.setSurpnum(surpnum - 1);
						packageMonth.setUid(user.getId());
						userService.updatePackageMonthByEntity(packageMonth);
						userService.insertPackageMonthRecord(user.getId(), userEquipment.getUserId(), ordernum, 0.0, 2, 1, 
								todaysurpnum - 1, 1, surpnum - 1, 0, month.getTime(), month.getElec(), 
								date);
					} else if (todaysurpnum <= 0 && surpnum > 0) {
						returnMap.put("wolfval", "4");
						return returnMap;//今日次数已用完
					} else if (todaysurpnum > 0 && surpnum <= 0) {
						returnMap.put("wolfval", "5");
						return returnMap;//总次数已用完
					} else {
						returnMap.put("wolfval", "5");
						return returnMap;
					}
				} else {
					userService.insertPackageMonthRecord(user.getId(), userEquipment.getUserId(), ordernum, 0.0, 2, 1, 
							-1, 1, -1, 0, month.getTime(), month.getElec(), 
							date);
				}
				int idx = elecStr.lastIndexOf(".");
				Short elec = Short.valueOf(elecStr.substring(0, idx));
				Short time = Short.valueOf(timeStr);
				try {
					Equipment equipment = equipmentService.getEquipmentById(code);
					if ("07".equals(equipment.getHardversion()) || DisposeUtil.checkIfHasV3(equipment.getHardversion())) {
//						SendMsgUtil.send_0x27(portchoose, (short) 10, time, elec, code, (byte)1);
//						WolfHttpRequest.sendNewChargePaydata(portchoose, time, "10", elec + "", code, 1, 0);
					} else {
//						SendMsgUtil.send_0x14(portchoose, (short) 10, time, elec, code);
//						WolfHttpRequest.sendChargePaydata(portchoose, time, "10", elec + "", code, 0);
					}
				} catch (Exception e) {
					logger.warn("包月下发充电失败");
				}
				insertChargeRecord(code, ordernum, portchoose, 1.0, month.getTime() + "", 
						elec, user.getMerid(), user.getId(),date);
				returnMap.put("uid", user.getId() + "");
				returnMap.put("wolfval", "1");
				return returnMap;
			} else {
				returnMap.put("wolfval", "2");
				return returnMap;//代表用户未开通包月
			}
		} else {
			returnMap.put("wolfval", "0");
			return returnMap;//代表用户未注册，不存在此用户
		}
	}
	
	@RequestMapping("/lookUserBagMonthInfo")
	public String lookUserBagMonthInfo(Integer uid,Model model) {
		PackageMonth month = userService.selectPackageMonthByUid(uid);
		model.addAttribute("packageMonth", month);
		return "general/lookUserBagMonthInfo";
	}
	
	public void insertChargeRecord(String code,String ordernum,int port,double money,String time,
			int elec,int merid,int uid,Date date) {
		ChargeRecord chargeRecord = new ChargeRecord();
		chargeRecord.setEquipmentnum(code);// 设备号/机位号
		chargeRecord.setOrdernum(ordernum);// 订单号
		chargeRecord.setPort(port);// 端口号
		chargeRecord.setExpenditure(money);// 金额
		chargeRecord.setDurationtime(time);// 时长
		chargeRecord.setQuantity(elec);// 电量
		chargeRecord.setPaytype(4);// 支付类型
		chargeRecord.setMerchantid(merid);
		chargeRecord.setStatus(1);
		chargeRecord.setUid(uid);
		chargeRecord.setBegintime(date);
		chargeRecordService.insetRecord(chargeRecord);
	}
	
	/**
	 * @Description： 收入信息处理
	 * @author： origin 创建时间：   2019年9月15日 上午11:44:49
	 */
	public Map<String, Object> partnerIncomeDispose(List<Map<String, Object>> partInfo, Integer merid, Double money, String ordernum, 
			Date time, Integer paysource, Integer paytype, Integer paystatus, Integer type) {
		Double mermoney = 0.00;//商户金额
		Double manmoney = 0.00;//商户金额
		Double tolpercent = 0.00;//分成比
		Map<String, Object> mapresult = new HashMap<>();
		List<Map<String, Object>> merchlist = new ArrayList<>();
		try {
			if(partInfo.size()>0){//分成
				System.out.println("输出分成");
				for(Map<String, Object> item : partInfo){
					Map<String, Object> map = new HashMap<>();
					Integer partid = CommUtil.toInteger(item.get("partid"));
					Double percent = CommUtil.toDouble(item.get("percent"));
					Double partmoney = CommUtil.toDouble((money * (percent*100))/100);
					map.put("partid", partid);
					map.put("percent", percent);
					map.put("money", partmoney);
					merchlist.add(map);
					tolpercent = tolpercent + percent;
					merEearningCalculate( partid, partmoney, type, ordernum, time, paysource, paytype, paystatus);
				}
			}
			mermoney = CommUtil.toDouble((money *100 * (1- tolpercent))/100);
			manmoney = CommUtil.subBig(money, mermoney);
			Map<String, Object> map = new HashMap<>();
			map.put("merid", merid);
			map.put("percent", (1-tolpercent));
			map.put("money", mermoney);
			merchlist.add(map);
			merEearningCalculate( merid, mermoney, type, ordernum, time, paysource, paytype, paystatus);
			mapresult.put("partmoney", manmoney);
			mapresult.put("json", JSON.toJSON(merchlist));
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
		return mapresult;
	}
	
	/**
	 * @Description： 商户收益计算
	 * @author： origin 创建时间：   2019年9月12日 下午5:54:22
	 */
	public Object merEearningCalculate(Integer merid, Double money, Integer type, String ordernum, Date operattime,
			Integer paysource, Integer paytype, Integer status){
		try {
			User user = userService.selectUserById(merid);
			user = user == null ? new User() : user;
			Double balance = CommUtil.toDouble(user.getEarnings());
			if(type==1){//  1为加  0为减
				balance = CommUtil.addBig(balance, money);
//				balance = (balance * 100 + money * 100) / 100;
			}else{
				balance = CommUtil.subBig(balance, money);
//				balance = (balance * 100 - money * 100) / 100;
			}
			user.setEarnings(balance);
			userService.updateUserEarnings(type, money, merid);//商户收益  1为加  0为减
			try {
				userService.updateMerAmount(type, money, merid);//商户收益总额  1为加  0为减
			} catch (Exception e) {
				logger.warn("商户总计更改出错");
			}
			merchantDetailService.insertMerEarningDetail(merid, money, balance, ordernum, operattime, paysource, paytype, status);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
		return null;
	}
	
		/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年9月20日 上午10:46:28 
	 * 设备硬件版本 00-默认出厂版本、01-十路智慧款、02-电轿款、03-脉冲投币、04-离线充值机
	 * 模板类型 0充电模板  1离线卡模板     2模拟投币模板（脉冲模板） 3钱包模板   4在线卡模板  5包月模板
	 */
	public String getTempPhone(String argumentval, Integer merid, Integer type) {
		String phone = "";
		try {
			Integer tempid = null;
			if(type==0 || type==1 || type==2){
				Equipment equipment = equipmentService.getEquipmentById(argumentval);
				tempid = CommUtil.toInteger(equipment.getTempid());
			}else if(type==3){
				Area area = areaService.selectByIdArea(CommUtil.toInteger(argumentval));
				tempid = CommUtil.toInteger(area.getTempid());
			}else if(type==4){
				OnlineCard online = onlineCardService.selectOnlineCardByCardID(CommUtil.toString(argumentval));
				System.out.println(CommUtil.toInteger(online.getId()));
				Integer vawalt = CommUtil.toInteger(online.getRelevawalt());
				if(vawalt==1){//关联
					type=3;
					Area area = areaService.selectByIdArea(CommUtil.toInteger(argumentval));
					tempid = CommUtil.toInteger(area.getTempid());
				}else{
					Area area = areaService.selectByIdArea(CommUtil.toInteger(online.getAid()));
					System.out.println(CommUtil.toInteger(area.getTempid()));
					tempid = CommUtil.toInteger(area.getTempid());
				}
			}else if(type==5){
				
			}
			TemplateParent temppa = templateService.getParentTemplateOne(tempid);
			if(temppa==null){
				List<TemplateParent> parentTemplist = templateService.getParentTemplateListByMerchantid(merid, type);
				if (parentTemplist == null || parentTemplist.size() == 0) {
					parentTemplist = templateService.getParentTemplateListByMerchantid(0, 3);
				}
				temppa = parentTemplist.get(0);
			}
			phone = temppa.getCommon1();
			if(phone==null || phone.equals("")){
				User user = userService.selectUserById(merid);
				user = user == null ? new User() : user;
				phone = CommUtil.toString(user.getServephone());
				if(phone==null) CommUtil.toString(phone = user.getPhoneNum());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phone;
	}
	
	
	@RequestMapping(value="/editAccountAloneData")
	@ResponseBody
    public Object editAccountData(HttpServletRequest request, HttpServletResponse response) {
		Object result = userService.editAccountAloneData(request);
		return result;
	}
	/**
	 * 商户手机端查询缴费记录
	 * @param request
	 * @param model
	 * @return Model
	 */
	@RequestMapping(value="/merSelectFeescale")
	public Object merSelectFeescaleRecoder(HttpServletRequest request,Model model){
		Object result = feescaleService.merSelectFeescaleRecord(request);
		model.addAttribute("listMap", result);
		return "record/merFeeScaleRecord";
	}
	
	/**
	 * 用户钱包付款成功后的跳转
	 * @param req 参数
	 * @return {@link String}
	 */
	@GetMapping("/walletPayment")
	public String  walletPayment(String orderNum,String payMoney,Model model){
		if(!StringUtils.isEmpty(orderNum))orderNum = orderNum.substring(orderNum.length()-4);
		model.addAttribute("orderNum", orderNum);
		model.addAttribute("payMoney", payMoney);
		return "/general/walletpayad";
	}
	
	
	//定时任务
	public void chargepayTask(String code, byte port, String times, int elec, long session_id, String ordernum) {
//		try {
//			List<ChargeRecord> chargelist = chargeRecordService.getOrderByOrdernum(ordernum);
//			if (chargelist.size() > 0) {
//				ChargeRecord chargeRecord = chargelist.get(0);
//				Integer resultinfo = chargeRecord.getResultinfo();
//				if (resultinfo == null) {
//					Map<String, String> codeMap = JedisUtils.hgetAll(code);
//					System.out.println(JSON.toJSONString(codeMap));
//					if (codeMap != null && codeMap.size() > 0) {
//						Map<String, String> parse = (Map<String, String>) JSON.parse(codeMap.get(port + ""));
//						String timeStr = parse.get("time");
//						String elecStr = parse.get("elec");
//						int timeInt = Integer.parseInt(timeStr);
//						int elecInt = Integer.parseInt(elecStr);
//						int time = Integer.parseInt(times);
//						Integer num = Server.chargeTimerNumMap.get(session_id);
//						if (num != null && num >= 1) {
//							if (time == timeInt && elec == elecInt) {
//								SendMsgUtil.send_21(port, code);
//								Timer timer = new Timer();
//								timer.schedule(new TimerTask() {
//									@Override
//									public void run() {
//										chargepayTask(code, port, timeStr, elecInt, session_id, ordernum);
//									}
//								}, 60000);
//								System.out.println(ordernum + "定时任务再开启==session_id:" + session_id);
//								num--;
//								Server.chargeTimerMap.put(session_id, timer);
//								Server.chargeTimerNumMap.put(session_id, num);
//							} else {
//								System.out.println(ordernum + "时间电量都不为额定电量，定时任务结束");
//								Server.chargeTimerMap.remove(session_id);
//								Server.chargeTimerNumMap.remove(session_id);
//							}
//						} else {
//							System.out.println(ordernum + "次数已达最大，定时任务结束");
//							Server.chargeTimerMap.remove(session_id);
//							Server.chargeTimerNumMap.remove(session_id);
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			System.out.println("定时任务异常---");
//			e.printStackTrace();
//		}
	}
	/**
	 * 代替子商户唤起支付 ZZ 
	 * @param request 请求参数
	 * @param money2 支付的钱
	 * @param openid 用户的参数
	 * @param notify_url 回调地址
	 * @param format 商家单号
	 * @param body 说明
	 * @return
	 */
	public SortedMap<String, String> packagePayParamsToMapSub(HttpServletRequest request, Double money2, String openid,
			String notify_url, String format,String body) {
		SortedMap<String, String> params = new TreeMap<>();
		// 根据商户号查询商家的微信配置信息
		Integer subMerId = CommUtil.toInteger(request.getAttribute("subMerId"));
		Map<String, Object> data = userService.selectSubMerConfigById(subMerId);
		// 参数
		logger.info("商家的id"+(subMerId != null));
		logger.info("商家得微信配置参数"+(data != null));
		String subMchid = CommUtil.toString(data.get("submchid")); 
		String subAppid = CommUtil.toString(data.get("subappid"));
		params.put("appid", WeiXinConfigParam.FUWUAPPID);
		params.put("body", "自助充电平台(" + body + ")");   
		params.put("mch_id", WeiXinConfigParam.MCHID);
		params.put("sub_appid", subAppid);
		params.put("sub_mch_id", subMchid);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		params.put("notify_url", CommonConfig.ZIZHUCHARGES + notify_url);
//		params.put("openid", openid);
		params.put("sub_openid", openid);
		params.put("out_trade_no", format);
		String ipAddr = getIpAddress(request);
		params.put("spbill_create_ip", ipAddr);
		String money = String.valueOf(money2 * 100);
		int idx = money.lastIndexOf(".");
		String total_fee = money.substring(0, idx);
		params.put("total_fee", total_fee);
		params.put("trade_type", "JSAPI");
		System.out.println("---" + params.get("out_trade_no"));
		return params;
	}
}
