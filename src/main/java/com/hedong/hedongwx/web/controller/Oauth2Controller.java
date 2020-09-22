package com.hedong.hedongwx.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.AlipayConfig;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.WeiXinConfigParam;

@Controller
public class Oauth2Controller {
	@Autowired
	private static Logger logger = LoggerFactory.getLogger(Oauth2Controller.class);
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private UserService userService;
	@Autowired
	private OnlineCardService onlineCardService;  

	@RequestMapping("/oauth2")
	public String oauth2() {
		try {
			logger.info("路径:/oauth2");
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/pcadminiStrator/orcode", "utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx3debe4a9c562c52a&"
					+ "redirect_uri=" + url + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@RequestMapping("/oauth2Testpay")
	public String oauth2Testpay() {
		try {
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/wxpay/bluetooth1", "utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx3debe4a9c562c52a&"
					+ "redirect_uri=" + url + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 扫描设备号支付授权
	 * @param code
	 * @return
	 */
	@RequestMapping("/oauth2pay")
	public String oauth2pay(String code,Model model) {
		String userAgent = request.getHeader("user-agent");
		// 扫描设备号使用支付宝充电
		// 判断设备的商户是不是微信特约商
		logger.info("oauth2pay接口传入的设备号"+"======"+code);
		if(code == null || "".equals(code) || code.length() != 6 ){
			return "erroruser";
		}
		// 扫描设备号使用支付宝充电
		// 判断设备的商户是不是微信特约商
		boolean subMer = userEquipmentService.subMerByCode(code);
		String appid = null;
		if (userAgent != null && userAgent.contains("AlipayClient")) {
			try {
				if(subMer){
					// 当前设备仅支持微信支付
					model.addAttribute("type", 1);
					model.addAttribute("code", code);
					return "merchant/onlineCardErrorInfo";
				}else{
					String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/alipay/chargeparam?equcode=" + code,"utf-8");
					String str = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=" + AlipayConfig.NEWFUWUAPPID
							+ "&scope=auth_base&redirect_uri=" + url;
					logger.info("跳转页面===" + str);
					return "redirect:" + str;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "erroruser";
			}
		// 扫描设备号使用微信支付进行充电
		} else if (userAgent != null && userAgent.contains("MicroMessenger")) {
			try {
				if(subMer){
					logger.info("对特约商户的设备:"+code+"进行付款");
					Map<String, Object> subMerData = userService.selectConfigDataByCode(code);
					appid = CommUtil.toString(subMerData.get("subappid"));
				}else{
					logger.info("对普通商户的设备:"+code+"进行付款");
					appid = WeiXinConfigParam.APPID;
				}
				String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/equipment/allChargePort?equcode=" + code,
						"utf-8");
				String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid
						+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&connect_redirect=1&state=1#wechat_redirect";
				return "redirect:" + str;
			} catch (Exception e) {
				e.printStackTrace();
			}
		// 扫描设备号使用银联支付进行充电
		} else if(userAgent != null && userAgent.contains("UnionPay")){
			if(subMer){
				// 当前设备仅支持微信支付
				model.addAttribute("type", 1);
				model.addAttribute("code", code);
				return "merchant/onlineCardErrorInfo";
			}else{
				try {
					//商户接收userAuthCode的地址
					String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/unionpay/userAuth?equCode=" +code ,"utf-8");
					//银联重定向url		
					String str = "https://qr.95516.com/qrcGtwWeb-web/api/userAuth?version=1.0.0&redirectUrl=" + url;
					return "redirect:" + str;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			return "erroruser";
		}
		return "erroruser";
	}
	
	/**
	 * 扫描端口号支付授权
	 * @param codeAndPort
	 * @return
	 */
	@RequestMapping("/oauth2Portpay")
	public String oauth2Portpay(String codeAndPort, Model model) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String,String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();
			String value = entry.getValue()[0];
			if ("code".equals(key)) {
				break;
			} else {
				if (value != null && (value.length() == 7 || value.length() == 8)) {
					codeAndPort = value;
				} else {
					return "erroruser";
				}
			}
		}
		String userAgent = request.getHeader("user-agent");
		logger.info("user-agent===" + userAgent);
		logger.info("oauth2Portpay接口传入的设备号"+"==========="+codeAndPort);
		if(codeAndPort == null || "".equals(codeAndPort)){
			return "erroruser";
		}
		String code = new StringBuffer(codeAndPort).substring(0,6);
		// 判断设备的商户是不是微信特约商
		boolean subMer = userEquipmentService.subMerByCode(code);
		String appid = null;
		// 扫描端口使用支付宝充电,特约商户不支持支付宝
		if (userAgent != null && userAgent.contains("AlipayClient")) {
			if(subMer){
				// 当前设备仅支持微信支付
				model.addAttribute("type", 1);
				model.addAttribute("code", code);
				return "merchant/onlineCardErrorInfo";
			}else{
				try {
					String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/alipay/chargePort?codeAndPort=" + codeAndPort,"utf-8");
					String str = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=" + AlipayConfig.APPID
							+ "&scope=auth_base&redirect_uri=" + url;
					logger.info("跳转页面===" + str);
					return "redirect:" + str;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return "erroruser";
				}
			}
		// 扫描端口微信支付充电
		} else if (userAgent != null && userAgent.contains("MicroMessenger")) {
			if(subMer){
				logger.info("对微信特约商户的:"+code+"进行付款");
				Map<String, Object> subMerData = userService.selectConfigDataByCode(code);
				appid = CommUtil.toString(subMerData.get("subappid"));
			}else{
				appid = WeiXinConfigParam.APPID;
			}
			try {
				String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/equipment/chargePort?codeAndPort=" + codeAndPort,
						"utf-8");
				String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid
						+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&connect_redirect=1&state=1#wechat_redirect";
				return "redirect:" + str;
			} catch (Exception e) {
				e.printStackTrace();
			}
		// 扫描端口使用银联支付充电,特约商户不支持银联    appUpIdentifier
		} else if (userAgent != null && userAgent.contains("UnionPay")) {
			if(subMer){
				// 提示当前设备仅支持微信支付
				model.addAttribute("type", 1);
				model.addAttribute("code", code);
				return "merchant/onlineCardErrorInfo";
			}else{
				try {
					//商户接收userAuthCode的地址
					String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/unionpay/userAuth?codeAndPort=" + codeAndPort,"utf-8");
					//银联重定向url		
					String str = "https://qr.95516.com/qrcGtwWeb-web/api/userAuth?version=1.0.0&redirectUrl=" + url;
					return "redirect:" + str;
				} catch (Exception e) {
					logger.info("银联发生异常了");
					e.printStackTrace();
				}
			}
		} else {
			return "erroruser";
		}
		return "erroruser";
	}

	@RequestMapping("/oauth2login")
	public String oauth2login() {
		try {
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/merchant/home", "utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinConfigParam.APPID
					+ "&redirect_uri=" + url + "&response_type=code&connect_redirect=1&scope=snsapi_userinfo&state=1#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "erroruser";
	}
	
	@RequestMapping("/oauth2loginChild")
	public String oauth2loginChild() {
		try {
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/merchant/home", "utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinConfigParam.APPID
					+ "&redirect_uri=" + url + "&response_type=code&connect_redirect=1&scope=snsapi_userinfo&state=2#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "erroruser";
	}
	
	@RequestMapping("/infoverdictOauth")
	public String infoverdictOauth(String existuser) {
		try {
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/equipment/infoverdict", "utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinConfigParam.APPID
					+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "erroruser";
	}
	
	/**
	 * 用户点击充电中心
	 * @return {@link String}
	 */
	@RequestMapping("chargingoauth2")
	public String chargingOauth() {
		try {
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/charge/getuserinfo", "utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinConfigParam.APPID
					+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "erroruser";
	}
	
	@RequestMapping("/generalLogin")
	public String generalLogin() {
		try {
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/general/check", "utf-8");
			String str1 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinConfigParam.APPID
					+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
			return "redirect:" + str1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "erroruser";
	}
	
	@RequestMapping("/checkOrderDetail")
	public String checkOrderDetail(String ordernum) {
		try {
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/merchant/withcardinfoOauth?ordernum=" + ordernum, "utf-8");
			String str1 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinConfigParam.APPID
					+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
			return "redirect:" + str1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "erroruser";
	}
	
	/**
	 * 在线卡充值()
	 * @param cardNumber
	 * @return
	 */
	@RequestMapping("/oauth2online")
	public String oauth2online(String cardNumber, Model model) {
		String userAgent = request.getHeader("user-agent");
		if(cardNumber == null || "".equals(cardNumber)){
			return "erroruser";
		}
		System.out.println("***** 0000001  ");
		// 根据卡号查找商家的信息
		Map<String, Object> subMerDate = onlineCardService.seleSubMerByCardNumber(cardNumber);
		System.out.println("***** 0000002  ");
		// 为空是未绑定商家
		if(subMerDate.isEmpty()){
			System.out.println("***** 0000003  ");
			// 提示绑定商家
			model.addAttribute("type", 2);
			model.addAttribute("cardStatus",1);
			model.addAttribute("cardId", cardNumber);
			System.out.println("***** 0000004  ");
			return "merchant/onlineCardErrorInfo";
		}else{

			System.out.println("***** 0000005  ");
			// 获取微信特约商户得标识
			Integer subMer = CommUtil.toInteger(subMerDate.get("submer"));
			// subMer为0:普通商户,1:微信特约商户
			// 微信特约商户不支持支付宝扫码
			System.out.println("***** 0000006  ");
			if (userAgent != null && userAgent.contains("AlipayClient") && subMer.equals(0)) {
				System.out.println("***** 0000007  ");
				try {
					String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/general/onlinecardpay?source=2&cardNumber=" + cardNumber,"utf-8");
					String str = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=" + AlipayConfig.APPID
							+ "&scope=auth_base&redirect_uri=" + url;
					System.out.println("跳转页面===" + str);
					return "redirect:" + str;
				} catch (UnsupportedEncodingException e) {
					System.out.println("***** 0000008  ");
					e.printStackTrace();
					return "erroruser";
				}
			// 对特约商户得appid授权
			} else if (userAgent != null && userAgent.contains("MicroMessenger")) {
				System.out.println("***** 0000009  ");
				// 判断普通商户和微信特约商户
				String appId = null;
				if(subMer.equals(1)){
					System.out.println("***** 0000010  ");
					// 在线卡的商家id
					Integer subMerId = CommUtil.toInteger(subMerDate.get("id"));
					Map<String, Object> subMerConfig = userService.selectSubMerConfigById(subMerId);
				    appId = CommUtil.toString(subMerConfig.get("subappid"));
				    logger.info("特约商户的id="+(subMerId != null));
				    logger.info("特约商户的appid="+(appId != null));
				}else{
					System.out.println("***** 0000011  ");
					appId = WeiXinConfigParam.APPID;
				}
				try {
					System.out.println("***** 00000012  ");
					String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/general/onlinecardpay?source=1&cardNumber=" + cardNumber, "utf-8");
					String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId
							+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&connect_redirect=1&state=1#wechat_redirect";
					return "redirect:" + str;
				} catch (Exception e) {
					System.out.println("***** 00000013  ");
					e.printStackTrace();
				}
				System.out.println("***** 0000014  ");
			// 提示此卡仅支持微信支付
			} else {
				System.out.println("***** 0000015  ");
				model.addAttribute("type", 2);
				model.addAttribute("cardStatus",2);
				model.addAttribute("cardId", cardNumber);
				return "merchant/onlineCardErrorInfo";
			}
			System.out.println("***** 0000016  ");
			return "erroruser";
		}
	}
	//---------------------------------------------------------------------------
	
	/**
	 * 特约商户下的用户扫码充电(站外)
	 * @param code 设备号
	 * @param subOpenid  
	 * @return {@link String}
	 */
	@RequestMapping("/oauth2PaySubUser")
	public String oauth2PaySubUser(String code, String subOpenid){
		try {
			if(code == null || "".equals(code) || code.length() != 6 ){
				return "erroruser";
			}
			if(subOpenid == null || "".equals(subOpenid)){
				return "erroruser";
			}
			String appId = WeiXinConfigParam.APPID;
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/equipment/allChargePort?equcode="+code +"&subOpenid="+subOpenid ,"utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId
					+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&connect_redirect=1&state=2#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	/**
	 * 特约商户下的用户设备端口充电(站外)
	 * @param codeAndPort
	 * @param subOpenid
	 * @return {@link String}
	 */
	@RequestMapping("/oauth2PortPaySubUser")
	public String oauth2PortPaySubUser(String codeAndPort, String subOpenid){
		if(codeAndPort == null || "".equals(codeAndPort)){
			return "erroruser";
		}
		if(subOpenid == null || "".equals(subOpenid)){
			return "erroruser";
		}
		try {
			String appid = WeiXinConfigParam.APPID;
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/equipment/chargePort?codeAndPort="+codeAndPort+"&subOpenid="+subOpenid,"utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid
					+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&connect_redirect=1&state=2#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	/**
	 * 特约商户下微信充值在线卡(站外)
	 * @param cardNumber 卡号
	 * @param model 
	 * @return {@link String}
	 */
	@RequestMapping("/oauth2onlineSubUser")
	public String oauth2onlineSubUser(String cardNumber, String subOpenid) {
		if(cardNumber == null || "".equals(cardNumber)){
			logger.info("卡号错误");
			return "erroruser";
		}
		if(subOpenid == null || "".equals(subOpenid)){
			logger.info("用户的openid错误");
			return "erroruser";
		}
		String appId = WeiXinConfigParam.APPID;
		try {
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/general/onlinecardpay?cardNumber=" + cardNumber+"&source=1"+"&subOpenid="+subOpenid, "utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId
					+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&connect_redirect=1&state=1#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	/**
	 * 用户在公众号内扫描特约商设备号充电
	 * @param code 设备号
	 * @return {@link String}
	 */
	@RequestMapping("/insideAllChargePort")
	public String insideAllChargePort(String deviceNum){
		if(deviceNum == null || "".equals(deviceNum) || deviceNum.length() != 6 ){
			return "erroruser";
		}
		try {
			Map<String, Object> subMerData = userService.selectConfigDataByCode(deviceNum);
			String appId = CommUtil.toString(subMerData.get("subappid"));
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/charge/allChargePort?deviceNum="+deviceNum,"utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId
					+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&connect_redirect=1&state=2#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	
	/**
	 * 用户在公众号内扫描特约商设备端口充电
	 * @param code 设备号
	 * @return {@link String}
	 */
	@RequestMapping("/insideChargePort")
	public String insideChargePort(String codeAndPort){
		
		String equcode = codeAndPort.substring(0, 6);
		if(equcode == null || "".equals(equcode) || equcode.length() != 6 ){
			return "erroruser";
		}
		try {
			Map<String, Object> subMerData = userService.selectConfigDataByCode(equcode);
			String appId = CommUtil.toString(subMerData.get("subappid"));
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/charge/chargePort?codeAndPort="+codeAndPort,"utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId
					+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&connect_redirect=1&state=1#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	/**
	 * 用户在公众号内特约商户名下钱包充值
	 * @param 
	 * @return {@link String}
	 */
	@RequestMapping("/insideWalletcharge")
	public String insideWalletcharge(String openid,Integer from,Integer merId){
		if(openid == null || "".equals(openid)){
			return "erroruser";
		}
		if(merId == null){
			return "erroruser";
		}
		try {
			Map<String, Object> subMerData = userService.selectSubMerConfigById(merId);
			String appId = CommUtil.toString(subMerData.get("subappid"));
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/charge/walletcharge?openid="+openid+"&from="+from+"&number=2","utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId
					+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&connect_redirect=1&state=1#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	/**
	 * 公众号内部输入设备的编号进行充电
	 * @param codeAndPort 设备号
	 * @return {@link String}
	 */
	@RequestMapping("/insideNearbyChargePort")
	public String insideNearbyChargePort(String codeAndPort){
		if(codeAndPort == null || "".equals(codeAndPort)){
			return "erroruser";
		}
		String equcode = codeAndPort.substring(0,6);
		try {
			Map<String, Object> subMerData = userService.selectConfigDataByCode(equcode);
			String appId = CommUtil.toString(subMerData.get("subappid"));
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/equipment/nearbyChargePort?codeAndPort="+codeAndPort,"utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId
					+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&connect_redirect=1&state=1#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			e.printStackTrace();
			return "erroruser";
		}
	}
	
	/**
	 * 公众号内部点击按钮进行续充
	 * @param tId 模板的id
	 * @param cId 充电记录的id
	 * @param dId 设备的id
	 * @return {@link String}
	 */
	@RequestMapping("/insideChargeContinue")
	public String insideChargeContinue(Integer chargeid,String equcode){
		if(chargeid == null){
			logger.info("充电记录id错误==="+chargeid);
			return "erroruser";
		}
		if(equcode == null || "".equals(equcode)){
			logger.info("设备号错误==="+chargeid);
			return "erroruser";
		}
		try {
			// 获取服务商的appid
			String dId = equcode.substring(0,6);
			// 根据设备号查询用户的配置信息
			Map<String, Object> subMerData = userService.selectConfigDataByCode(equcode);
			String appId = CommUtil.toString(subMerData.get("subappid"));
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/charge/chargeContinue?chargeid="+chargeid+"&deviceNum="+dId,"utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId
					+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&connect_redirect=1&state=1#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			return "erroruser";
		}
	}

    /**
     * 微信公众号内部点击个人中心
	 * @param identify 商家的id
	 * @return {@link String}
	 */
	@RequestMapping("/insideIndex")
	public String insideIndex(Integer identify){
		if(identify == null){
			return "erroruser";
		}
		try {
			// 获取服务商的appid
			Map<String, Object> subMerData = userService.selectSubMerConfigById(identify);
			String appId = CommUtil.toString(subMerData.get("subappid"));
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/general/index?identify=1","utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId
					+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&connect_redirect=1&state=1#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			return "erroruser";
		}
	} 
	
	
	/**
	 * 公众号内点击IC卡操作进行充值
	 * @param cId 卡id
	 * @param merId 商家的id
	 * @return {@link String}
	 */
	@RequestMapping("insideOperation")
	public String insideOperation(Integer cId, Integer merId){
		if(cId == null || merId == null){
			logger.info("卡id="+cId+"商家id="+merId);
			return "erroruser";
		}
		try {
			// 获取服务商的appid
			Map<String, Object> subMerData = userService.selectSubMerConfigById(merId);
			String appId = CommUtil.toString(subMerData.get("subappid"));
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/general/operation?id="+cId+"&merId="+merId,"utf-8");
			String str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId
					+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&connect_redirect=1&state=1#wechat_redirect";
			return "redirect:" + str;
		} catch (Exception e) {
			return "erroruser";
		}
	}
}
