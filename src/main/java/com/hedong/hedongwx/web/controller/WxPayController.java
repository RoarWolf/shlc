package com.hedong.hedongwx.web.controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.CommonHandler;
import com.hedong.hedongwx.dao.UserHandler;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.DealerAuthority;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.FeescaleRecord;
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
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.AuthorityService;
import com.hedong.hedongwx.service.BasicsService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.CommonMethodService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.FeescaleService;
import com.hedong.hedongwx.service.GeneralDetailService;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.LostOrderService;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.service.MoneyService;
import com.hedong.hedongwx.service.OfflineCardService;
import com.hedong.hedongwx.service.OnlineCardRecordService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.SystemSetService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.thread.Server;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.JedisUtils;
import com.hedong.hedongwx.utils.MD5Util;
import com.hedong.hedongwx.utils.SendMsgUtil;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.TempMsgUtil;
import com.hedong.hedongwx.utils.WXPayConfigImpl;
import com.hedong.hedongwx.utils.WeiXinConfigParam;
import com.hedong.hedongwx.utils.XMLUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/wxpay")
public class WxPayController {
	
	private final Logger logger = LoggerFactory.getLogger(WxPayController.class);
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private UserService userService;
	@Autowired
	private OfflineCardService offlineCardService;
	@Autowired
	private OnlineCardService onlineCardService;
	@Autowired
	private OnlineCardRecordService onlineCardRecordService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private InCoinsService inCoinsService;
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private GeneralDetailService generalDetailService;
	@Autowired
	private MerchantDetailService merchantDetailService;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private SystemSetService systemSetService;
	@Autowired
	private FeescaleService feescaleService;
	@Autowired
	private BasicsService basicsService;
	@Autowired
	private LostOrderService lostOrderService;
	@Autowired
	private CommonMethodService commonMethodService;

	@RequestMapping({ "/withdrawWalletRefund" })
	@ResponseBody
	public Object withdrawWalletRefund(Integer id, Integer source) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String ordernum = null;
			if(CommUtil.toInteger(source)==1){
				TradeRecord trade = tradeRecordService.selectTradeById(CommUtil.toInteger(id));
				ordernum = CommUtil.toString(trade.getOrdernum());
				List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
				if(chargeRecord != null && chargeRecord.size()>0){
					id = CommUtil.toInteger(chargeRecord.get(0).getId());
				}
			}
			ChargeRecord chargeRecord = chargeRecordService.chargeRecordOne(CommUtil.toInteger(id));
			if(ordernum==null) CommUtil.toString(ordernum = chargeRecord.getOrdernum());
//			Integer paytype = CommUtil.toInteger(chargeRecord.getPaytype());
			Integer uid = chargeRecord.getUid();
			if (chargeRecord.getNumber() == 2) {//部分退款修改用户数据
				Double refundmoney = CommUtil.toDouble(chargeRecord.getRefundMoney());
				User tourist = userService.selectUserById(uid);

				Double userbalance = CommUtil.toDouble(tourist.getBalance());
				Double usersendmoney = CommUtil.toDouble(tourist.getSendmoney());
				Double usermoney = CommUtil.addBig(userbalance, usersendmoney);
				Double  balance = CommUtil.subBig(usermoney, refundmoney);
				
				Double opermoney = 0.00;
				Double opersendmoney = 0.00;
				Double topupbalance = 0.00;
				Double givebalance = 0.00;
				if(balance>=0){
					Double recharge = CommUtil.subBig(userbalance, refundmoney);
					if(recharge>=0){
						opermoney = refundmoney;
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
					tourist.setBalance(topupbalance);
					tourist.setSendmoney(givebalance);
					userService.updateUserById(tourist);
					generalDetailService.insertGenWalletDetail(uid, chargeRecord.getMerchantid(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, chargeRecord.getOrdernum(), new Date(), 6);
					chargeRecordService.updateNumberById(null, 0, id, null, null, null, CommUtil.toDateTime(), CommUtil.toDateTime());
					List<TradeRecord> trade = tradeRecordService.getTraderecordList(ordernum, 2);
					if(trade.size()>0){
						Integer traid = CommUtil.toInteger(trade.get(0).getId());
						tradeRecordService.updateTradeOrderInfo("ch"+ordernum, traid);
					}
					map.put("ok", "ok");
					map.put("messg", "操作成功");
				}else{
					map.put("ok", "error");
					map.put("messg", "用户钱包金额不足，操作失败。");
				}
			}else{
				map.put("ok", "error");
				map.put("messg", "该订单不是部分退款，不能执行该操作！");
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return CommonConfig.messg(403);
		}
	}
	
	/**
	 * @Description：虚拟充值退款 
	 * @author： origin 创建时间：   2019年7月15日 上午10:01:46
	 */
	@RequestMapping({ "/mercVirtualReturn" })
	@ResponseBody
	public Object mercVirtualReturn(Integer id, Integer type) throws Exception {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> result = null;
		try {
//			User userSession = (User) request.getSession().getAttribute("user");
//			if(userSession==null) return map = CommonConfig.messg(400);
			if(type == 1){
				result = moneyService.mercVirtualReturn(id);
			}else if(type == 2){
				result = onlineCardService.mercVirtualReturn(id);
			}
			map.put("ok", "ok");
			Integer code = CommUtil.toInteger(result.get("code"));
			if(code != 200) map.put("ok", "error");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return CommonConfig.messg(403);
		}
	}
	
	@RequestMapping({ "/doWalletReturn" })
	@ResponseBody
	public Object doWalletReturn(Integer id, Integer refundState, String pwd, Integer utype) throws Exception {
		Map<String, Object> map = new HashMap<>();
		User userSession = (User) request.getSession().getAttribute("admin");
		if(utype==null) utype = 2;
		if (utype == 1) {
			if (pwd == null || userSession == null) {
				map.put("ok", "pwderror");
			} else {
				String md5Pwd = MD5Util.MD5Encode(pwd, "utf-8");
				String password = userSession.getPassword();
				if (!md5Pwd.equals(password)) {
					map.put("ok", "pwderror");
				}
			}
		}
		String ordernum = null;//1:充电、 2:脉冲、 3:离线 
		if(refundState==1){
			ChargeRecord order = chargeRecordService.chargeRecordOne(id);
			ordernum = order.getOrdernum();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			chargeRecordService.updateNumberById(null, 1, order.getId(), null, null,
					order.getExpenditure(), sdf.format(order.getBegintime()), CommUtil.toDateTime());
		}else if(refundState==2){
			OfflineCard order = offlineCardService.selectOfflineCardById(id);
			ordernum = order.getOrdernum();
			offlineCardService.offlineCardRefund(6, id);
		}else if(refundState==3){
			InCoins order = inCoinsService.selectInCoinsRecordById(id);
			ordernum = order.getOrdernum();
			inCoinsService.updateInCoinsStatus(ordernum, (byte) 7);
		}
		TradeRecord trade = tradeRecordService.getTraderecord(ordernum);
		if(trade==null){
			map.put("code", 100);
		}
		User user = userService.selectUserById(trade.getUid());
		if(user==null){
			map.put("code", 101);
		}
		Double tomoney = CommUtil.toDouble(trade.getMoney());
		Double userbalance = CommUtil.toDouble(user.getBalance());
		Double usersendmoney = CommUtil.toDouble(user.getSendmoney());
		
		Double opermoney = tomoney;
		Double opersendmoney = 0.00;
		Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
		Double topupbalance = CommUtil.addBig(userbalance, opermoney);
		Double givebalance = CommUtil.addBig(usersendmoney, opersendmoney);
		Double operbalance = CommUtil.addBig(topupbalance, givebalance);
		user.setBalance(topupbalance);
		user.setSendmoney(givebalance);
		userService.updateUserById(user);
		
		generalDetailService.insertGenWalletDetail(user.getId(), user.getMerid(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, trade.getOrdernum(), new Date(), 5);
		tradeRecordService.insertToTrade(trade.getMerid(), trade.getManid(), trade.getUid(), trade.getOrdernum(), tomoney, trade.getMermoney(), 
				trade.getManmoney(), trade.getCode(), trade.getPaysource(), trade.getPaytype(), 2, trade.getHardver());
		map.put("ok", "ok");
		return map;
	}
	
	/**
	 * 微信支付获取参数
	 * 
	 * @param chargeparam
	 * @param portchoose
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pay")
	@ResponseBody
	public Map<String,String> pay(@RequestParam("chargeparam") Integer chargeparam, @RequestParam("portchoose") Byte portchoose,
			Model model) throws Exception {
		Map<String,String> paramMap = new HashMap<>();
		if (chargeparam == null) {
			paramMap.put("wolferror", "1");
			paramMap.put("wolferrorinfo", "充电时间未选择");
			return paramMap;
		} else if (portchoose == null) {
			paramMap.put("wolferror", "2");
			paramMap.put("wolferrorinfo", "充电端口未选择");
			return paramMap;
		} else {
			//根据模板id查询金额
			Double money2 = templateService.getInfoTemplateOne(chargeparam).getMoney();
			String openid = request.getParameter("openid");
			String code = request.getParameter("code");
			// 获取JSAPI支付时所有的必填参数
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String format = df.format(new Date()) + code + portchoose;
			//-------------------------------------------------
			// 判断设备的所属商户是不是微信特约商户
			logger.info("/pay接口传入的设备号"+"=============="+code);
			boolean data = userEquipmentService.subMerByCode(code);
			SortedMap<String, String> params = null;
			String 	subAppid = null;
			if(data){
				logger.info("使用特约商户的参数对:"+code+"进行支付");
				params = packagePayParamsToMapSub(request, money2, openid, "wxpay/payback", format, "扫码充电");
				// 获取商户的微信配置信息
				Map<String, Object> subData = userService.selectConfigDataByCode(code);
				subAppid = CommUtil.toString(subData.get("subappid"));
			}else{
				logger.info("使用微信服务商的参数对:"+code+"进行支付");
				params = packagePayParamsToMap(request, money2, openid, "wxpay/payback", format, "扫码充电");
				subAppid = WeiXinConfigParam.APPID;
			}
			//------------------------------------------------------
			// 生成签名并放入map集合里
			String sign = HttpRequest.createSign("UTF-8", params);
			params.put("sign", sign);
			String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

			String canshu = HttpRequest.getRequestXml(params);
			String sr = HttpRequest.sendPost(url, canshu);
			Map<String, String> map = XMLUtil.doXMLParse(sr);

			logger.info("微信支付预订单参数：" + JSON.toJSONString(map));

			SortedMap<String, String> seconde = new TreeMap<>();
			seconde.put("appId", subAppid);
			String time = HttpRequest.getTimeStamp();
			seconde.put("timeStamp", time);
			String sjzf = HttpRequest.getRandomStringByLength(30);

			seconde.put("nonceStr", sjzf);
			seconde.put("package", "prepay_id=" + (String) map.get("prepay_id"));
			seconde.put("signType", "MD5");
			//签名
			String sign2 = HttpRequest.createSign("UTF-8", seconde);
			//根据设备号查询用户设备
			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
			ChargeRecord chargeRecord = new ChargeRecord();
			chargeRecord.setEquipmentnum(code);// 设备号/机位号
			chargeRecord.setOrdernum(params.get("out_trade_no"));// 订单号
			int port = portchoose;
			chargeRecord.setPort(port);// 端口号
			//根据模板id查询金额
			TemplateSon templateSon = templateService.getInfoTemplateOne(chargeparam);
			chargeRecord.setExpenditure(templateSon.getMoney());// 金额
			Equipment equipment = equipmentService.getEquipmentById(code);
			String durationtime = templateSon.getChargeTime().toString();
			if (DisposeUtil.checkIfHasV3(equipment.getHardversion()) && templateSon.getType() == 3) {
				short clacV3TimeBytemp = templateService.clacV3TimeBytemp(templateSon.getTempparid(), templateSon.getMoney());
				durationtime = clacV3TimeBytemp + "";
			}
			chargeRecord.setDurationtime(durationtime);// 时长
			chargeRecord.setQuantity(templateSon.getChargeQuantity());// 电量
			chargeRecord.setPaytype(ChargeRecord.WEIXINPAYTYPE);// 支付类型
			chargeRecord.setMerchantid(userEquipment.getUserId());
			chargeRecord.setStatus(0);
			//根据openid查询用户
			User user = userService.getUserByOpenid(openid);
			Integer uid = user == null ? 0 : user.getId();
			chargeRecord.setUid(uid);
			String ifcontinue = request.getParameter("ifcontinue");
			//判断是否续充
			if (ifcontinue != null && !"".equals(ifcontinue)) {
				//根据id查询充电记录
				ChargeRecord chargeRecordById = chargeRecordService.getChargeRecordById(Integer.parseInt(ifcontinue));
				if (chargeRecordById != null && chargeRecordById.getIfcontinue() != null) {
					chargeRecord.setIfcontinue(chargeRecordById.getIfcontinue());
				} else {
					chargeRecord.setIfcontinue(Integer.parseInt(ifcontinue));
				}
			}
			//插入充电记录
			chargeRecordService.insetRecord(chargeRecord);

			paramMap.put("appId", subAppid);
			paramMap.put("prepay_id", map.get("prepay_id"));
			paramMap.put("date", time);
			paramMap.put("paySign", sign2);
			paramMap.put("packagess", "prepay_id=" + (String) map.get("prepay_id"));
			paramMap.put("nonceStr", sjzf);
			paramMap.put("signType", "MD5");
			paramMap.put("out_trade_no", params.get("out_trade_no"));
			paramMap.put("attention", request.getParameter("attention"));
			return paramMap;
		}
	}
	
	/**
	 * 商家微信缴费
	 * @param user 用户参数
	 * @param listEquipments 设备参数(数组类型)
	 * @return Map<String,String>
	 */
	@RequestMapping("/merWxPayment")
	@ResponseBody
	public Map<String,String> merWxPayment() {
		int accountId = 72;
		String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		Map<String, String> paramMap = new HashMap<String, String>();
		//用户参数
		String user = request.getParameter("users");
		//设备参数
		String equipments = request.getParameter("devices");
		//小区id
		Integer aid = CommUtil.toInteger(request.getParameter("id"));
		if(user == null || "".equals(user)){
			paramMap.put("wolferror", "1");
			paramMap.put("wolferrorinfo", "商家参数错误");
			return paramMap;
		}
		if(equipments == null || "".equals(equipments)){
			paramMap.put("wolferror", "2");
			paramMap.put("wolferrorinfo", "设备参数错误");
			return paramMap;
		} else{
			
			User merUser = JSON.parseObject(user,User.class);
			List<Equipment> listEquipment = JSON.parseArray(equipments, Equipment.class);
			Double merPayMoney = CommUtil.toDouble(merUser.getPayMonet());
			logger.info("微信缴费的用户参数"+merUser);
			logger.info("微信缴费的设备参数"+listEquipment);
			if(merUser.getOpenid() != null && !"".equals(merUser.getOpenid()) &&  merPayMoney != 0.00 ){
				//根据openid查询商家
				User merUser1 = userService.getUserByOpenid(merUser.getOpenid());
				//缴费记录对象
				FeescaleRecord feescaleRecord = new FeescaleRecord();
				Integer random=(int)(Math.random()*90000)+10000;
				String  feescaleRecordNum= format + random;
				//遍历设备,生成缴费的订单,非正常状态
				for (int i = 0; i < listEquipment.size(); i++) {
					//设备号
					String code = listEquipment.get(i).getCode();
					//设备应缴金额
					Double totalMoney = CommUtil.toDouble(listEquipment.get(i).getTotalMoney());
					if(code != null && !"".equals(code) && totalMoney != 0.00 && merUser1 != null){
						//插入缴费的记录
						feescaleRecord.setAid(aid);//小区id
						feescaleRecord.setMerName(merUser1.getUsername());//商家姓名
						feescaleRecord.setMerId(merUser1.getId());//商家id
						feescaleRecord.setMerPayMoney(totalMoney);//商家支付的钱
						feescaleRecord.setEquipmentNum(code);//设备号
						feescaleRecord.setOrderNum(feescaleRecordNum);//订单号
						feescaleRecord.setRenewal(12);//续约时间
						feescaleRecord.setPrice(totalMoney);//设备应缴金额
						feescaleRecord.setCreateTime(new Date());
						feescaleRecord.setPayType(4);//4为微信缴费
						feescaleRecord.setNote(JSON.toJSONString(new ArrayList<String>()));
						feescaleRecord.setReviceId(accountId);//收款人id
						feescaleRecord.setState(0);//微信缴费预订单状态为0
						feescaleService.insertWxFeescaleRecord(feescaleRecord);
					}else{
						paramMap.put("wolferror", "1");
						paramMap.put("wolferrorinfo", "商家参数错误");
						return paramMap;
					}
				}
				//生成前端调取接口的参数
				SortedMap<String, String> params = packagePayParamsToMap(request, merPayMoney, merUser.getOpenid(), "wxpay/merWxpayBack", feescaleRecordNum, "微信设备缴费");
				String sign = HttpRequest.createSign("UTF-8", params);
				params.put("sign", sign);
				String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
				String canshu = HttpRequest.getRequestXml(params);
				String sr = HttpRequest.sendPost(url, canshu);
				Map<String, String> map = null;
				try {
					map = XMLUtil.doXMLParse(sr);
				} catch (JDOMException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				SortedMap<String, String> seconde = new TreeMap<>();
				seconde.put("appId", WeiXinConfigParam.APPID);
				String time = HttpRequest.getTimeStamp();
				seconde.put("timeStamp", time);
				String sjzf = HttpRequest.getRandomStringByLength(30);
				seconde.put("nonceStr", sjzf);
				seconde.put("package", "prepay_id=" + (String) map.get("prepay_id"));
				seconde.put("signType", "MD5");
				String sign2 = HttpRequest.createSign("UTF-8", seconde);
				paramMap.put("appId", WeiXinConfigParam.APPID);
				paramMap.put("prepay_id", map.get("prepay_id"));
				paramMap.put("date", time);
				paramMap.put("paySign", sign2);
				paramMap.put("packagess", "prepay_id=" + (String) map.get("prepay_id"));
				paramMap.put("nonceStr", sjzf);
				paramMap.put("signType", "MD5");
				paramMap.put("out_trade_no", params.get("out_trade_no"));
				return paramMap;
			}else{
				paramMap.put("wolferror", "1");
				paramMap.put("wolferrorinfo", "商家参数错误");
				return paramMap;
			}
		}
	}
	
			
	/**
	 * 商家微信缴费回调
	 * 
	 * @return
	 * @throws JDOMException
	 */
	@RequestMapping({ "/merWxpayBack" })
	@ResponseBody
	public String merWxpayBack() throws JDOMException {
		String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
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
				try {
					logger.info("微信支付的预订单"+ map.get("out_trade_no"));
					//将微信订单状态变为正常
					feescaleService.updateFeescaleRecordStatue((String) map.get("out_trade_no"));
					//根据商家订单号查询缴费信息
					List<Map<String, Object>> maps = feescaleService.selectWxFeescaleRecord((String) map.get("out_trade_no"));
					logger.info("微信支付订单信息"+ maps);
					Double merPayMoney = 0.00;
					Integer merId = null;
					String merName = null;
					int accountId = 72;
					if(!maps.isEmpty()){
						//遍历缴费的设备,推迟1年,计算商家总缴费
						for (int i = 0; i < maps.size(); i++) {
							merPayMoney =CommUtil.addBig(merPayMoney, CommUtil.toDouble(maps.get(i).get("merpaymoney")));
							//设备推迟一年
							logger.info("设备号"+ maps.get(i).get("equipmentnum"));
							equipmentService.updateExpirationTime((String)maps.get(i).get("equipmentnum"));
							merId = (Integer) maps.get(i).get("merid");
							merName = (String) maps.get(i).get("username");
						}
						//记录商家的资金明细单号
						//Integer random1=(int)(Math.random()*90000)+10000;
						//String feescaleRecord1=format+random1;
						//User user1 = userService.selectUserById(CommUtil.toInteger(merId));
						logger.info("商家支付的钱----"+merPayMoney);
						logger.info("商家支付的名字--"+merName);
						logger.info("商家支付的id---"+merId);
						//logger.info("商家的余额-----"+user1.getEarnings());
						//记录商家的资金变动明细
						//merchantDetailService.insertMerEarningDetail(merId, merPayMoney, user1.getEarnings(), feescaleRecord1, new Date(), 8, 2, 2);
						//变更收款账号的收益
						userService.updateUserEarnings(1, CommUtil.toDouble(merPayMoney), accountId);
						//记录收款账号收款的明细
						User accountUser = userService.selectUserById(accountId);
						//Integer random3=(int)(Math.random()*90000)+10000;
						merchantDetailService.insertMerEarningDetail(accountId, merPayMoney, accountUser.getEarnings(), (String) map.get("out_trade_no"), new Date(), 8, 2, 1);
					}else{
						logger.info("这是预订单数据"+maps);
					}
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
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
			e.printStackTrace();
		}
		return resXml;
	}
	
	/**
	 * @Description： 计算合伙人与商户分别收益的计算处理
	 * @author： origin 创建时间：2020年5月22日下午4:46:55
	 */
	public Map<String, Object> percentCalculateDispose(List<Map<String, Object>> partInfo, Integer merid, Double money) {
		Double mermoney = 0.00;//商户金额
		Double partmoney = 0.00;//合伙人金额
		Double tolpercent = 0.00;//分成比
		Map<String, Object> mapresult = new HashMap<>();
		List<Map<String, Object>> percentinfo = new ArrayList<>();
		try {
			if(partInfo.size()>0){//分成
				System.out.println("输出分成");
				for(Map<String, Object> item : partInfo){
					Map<String, Object> mappartner = new HashMap<>();
					Integer partid = CommUtil.toInteger(item.get("partid"));
					Double percent = CommUtil.toDouble(item.get("percent"));
					Double partnetmoney = CommUtil.toDouble((money * (percent*100))/100);
					mappartner.put("partid", partid);
					mappartner.put("percent", percent);
					mappartner.put("money", partnetmoney);
					percentinfo.add(mappartner);
					tolpercent = tolpercent + percent;
				}
			}
			mermoney = CommUtil.toDouble(money * (1- tolpercent));
			partmoney = CommUtil.subBig(money, mermoney);
			Map<String, Object> mermap = new HashMap<>();
			mermap.put("merid", merid);
			mermap.put("percent", (1-tolpercent));
			mermap.put("money", mermoney);
			percentinfo.add(mermap);
			mapresult.put("mermoney", mermoney);
			mapresult.put("partmoney", partmoney);
			mapresult.put("percentinfo", percentinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapresult;
	}
	
	public void dealerDataDispose(Integer orderid, String devicenum, Integer merid,
		   Integer paysource, Integer paytype, Integer paystatus, TradeRecord traderecord) {
		Date time = new Date();
		String strtime = CommUtil.toDateTime(time);
		Integer uid = CommUtil.toInteger(traderecord.getUid());
		Integer merId = CommUtil.toInteger(traderecord.getMerid());
		Double paymoney = CommUtil.toDouble(traderecord.getMoney());
		String ordernum = CommUtil.toString(traderecord.getOrdernum());
		Integer traPaysource = CommUtil.toInteger(traderecord.getPaysource()); 
		Integer traPaytype = CommUtil.toInteger(traderecord.getPaytype());
		Integer traStatus = 2;
		String devicehard = null;
		try {
			// 更新设备收益
			equipmentService.updateEquEarn(devicenum, paymoney, 0);
			String comment = traderecord.getComment();
			try {
				Equipment equipment = equipmentService.getEquipmentById(devicenum);
				devicehard = CommUtil.toString(equipment.getHardversion());
				Integer aid = CommUtil.toInteger(equipment.getAid());
				// 更新小区收益
				if (aid != null && aid != 0) {
					areaService.updateAreaEarn(0, paymoney, aid,null,paymoney,null);
				} 
				User subMer = userService.selectUserById(merId);
				// 普通商家修改退费数据
				if(subMer != null && subMer.getSubMer().equals(0)){
					//充电退费数据
					dealerIncomeRefund(comment, merid, paymoney, ordernum, time, paysource, paytype, paystatus);
				}else{
					// 特约商户修改退费数据
					userService.updateMerAmount(0, paymoney, merid);//商户收益总额  1为加  0为减
					merchantDetailService.insertMerEarningDetail(merid, paymoney, subMer.getEarnings(), ordernum, time, paysource, paytype, paystatus);
				}
			} catch (Exception e) {
				logger.warn("小区修改余额错误===" + e.getMessage());
			}
			Double mermoney = traderecord.getMermoney();
			Double partmoney = traderecord.getManmoney();
			Integer manid = 0;
			tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, traPaysource, traPaytype, traStatus, devicehard, comment);
//			String phone = getTempPhone(incoins.getEquipmentnum(), incoins.getMerchantid(), 2);
//			Equipment devicedata = equipmentService.getEquipmentAndAreaById(devicenum);
//			Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
//			Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
//			String devicename = CommUtil.toString(devicedata.getRemark());
//			String areaaddress = CommUtil.toString(devicedata.getAddress());
//			String servicphone = systemSetService.getServicePhone(devicetempid, deviceaid, merid);
//			systemSetService.sendMmessChargeEnd(ordernum, orderid, paysource, merid, uid, paymoney, strtime,
//					devicenum, port, devicename, deviceaid, areaaddress, servicphone);
//			
			
			String phone = getTempPhone(devicenum, merid, 0);
			String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=1&id="+orderid;
			returnMsgTemp(uid, phone, devicenum, ordernum, urltem, strtime, paymoney);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
	}
	
	public Object dealerIncomeRefund(String comment, Integer merid, Double money, String ordernum, 
			Date time, Integer paysource, Integer paytype, Integer paystatus) {
		Integer type = 0;
		List<Map<String, Object>> merchlist = new ArrayList<>();
		try {
			JSONArray jsona = JSONArray.fromObject(comment);
			for (int i = 0; i < jsona.size(); i++) {
				Map<String, Object> map = new HashMap<>();
				JSONObject item = jsona.getJSONObject(i);
				Integer partid = CommUtil.toInteger(item.get("partid"));
				Integer mercid = CommUtil.toInteger(item.get("merid"));
				Double partmoney = CommUtil.toDouble(item.get("money"));
				if(partid!=0){//合伙人信息
					merEearningCalculate( partid, partmoney, type, ordernum, time, paysource, paytype, paystatus);
				}else{//商户信息
					merEearningCalculate( mercid, partmoney, type, ordernum, time, paysource, paytype, paystatus);
				}
				map = item;
				merchlist.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
		return JSON.toJSON(merchlist);
	}
	
	/**
	 * @Description： 商户收益计算
	 * @author： origin 创建时间：   2019年9月12日 下午5:54:22
	 */
	public void merEearningCalculate(Integer merid, Double money, Integer type, String ordernum, Date operattime,
			Integer paysource, Integer paytype, Integer status){
		try {
			User user = userService.selectUserById(merid);
			Double earnings = user.getEarnings();
			if(type==1){//  1为加  0为减
				earnings = (earnings * 100 + money * 100) / 100;
			}else{
				earnings = (earnings * 100 - money * 100) / 100;
			}
			user.setEarnings(earnings);
			merchantDetailService.insertMerEarningDetail(merid, money, earnings, ordernum, operattime, paysource, paytype, status);
			
			userService.updateUserEarnings(type, money, user.getId());//商户收益  1为加  0为减
			userService.updateMerAmount(type, money, user.getId());//商户收益总额  1为加  0为减
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 微信支付回调
	 * 
	 * @return
	 * @throws JDOMException
	 */
	@RequestMapping({ "/payback" })
	@ResponseBody
	public String payback() throws JDOMException {

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
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				out.write(resXml.getBytes());
				out.flush();
				out.close();
				// 获取商户订单号
				String ordernum = CommUtil.toString(map.get("out_trade_no"));
				logger.warn(ordernum + "  [origin] 扫码     "+new Date());
				// 根据商户订单号查询充电记录i
				List<ChargeRecord> chargeRecordlist = chargeRecordService.getOrderByOrdernum(ordernum);
				// 查询第一条充电记录
				ChargeRecord chargeRecord = chargeRecordlist.get(0);
				// 判断充电记录是否大于1
				if (chargeRecordlist.size() > 1) {
					for (int i = 1; i < chargeRecordlist.size() - 1; i++) {
						String ordernumcha = chargeRecordlist.get(i).getOrdernum();
						Integer id = chargeRecordlist.get(i).getId();
						// 根据订单id修改订单号
						chargeRecordService.updateNumberById(ordernumcha.substring(4), null, id, null, null, null, null, null);
					}
				}
				Map<String, Object> maps = new HashMap<String, Object>();
				maps.put("status", 1);
				maps.put("begintime", StringUtil.toDateTime());
				maps.put("ordernum", map.get("out_trade_no").toString());
				maps.put("id", chargeRecord.getId());
				// 根据订单号更新
				chargeRecordService.updateByOrdernum(maps);
				String code = CommUtil.toString(chargeRecord.getEquipmentnum());
				try {
					if (chargeRecord.getStatus() == null || chargeRecord.getStatus() == 0) {
						//修改本地数据
						Integer uid = CommUtil.toInteger(chargeRecord.getUid());
						Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
						Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
						Integer orderid = CommUtil.toInteger(chargeRecord.getId());
						Date time = new Date();
						String strtime = CommUtil.toDateTime(time);
						Integer paysource = MerchantDetail.CHARGESOURCE;
						Integer paytype = MerchantDetail.WEIXINPAY;
						Integer paystatus = MerchantDetail.NORMAL;
						String devicenum = code;
						String port = CommUtil.toString(chargeRecord.getPort());
						Integer type = 1;
						Double mermoney = 0.00;
						Equipment devicedata = equipmentService.getEquipmentAndAreaById(code);
						Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
						Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
						String devicename = CommUtil.toString(devicedata.getRemark());
						String areaname = CommUtil.toString(devicedata.getName());
						String servicphone = systemSetService.getServicePhone(devicetempid, deviceaid, merid);
						
						systemSetService.sendMmessageCharge(ordernum, orderid, paysource, merid, uid, paymoney, strtime,
								devicenum, port, devicename, deviceaid, areaname, servicphone);
						Map<String, Object> usequmap = userEquipmentService.getEquipToUserByCode(code);
						String ramrk = CommUtil.toString(usequmap.get("hardversion"));
						Integer aid = CommUtil.toInteger(usequmap.get("aid"));
						Double partmoney = 0.00;
						try {
							User subMerUser = userService.selectUserById(merid);
							Integer submerid = CommUtil.toInteger(subMerUser.getSubMer());
							if(!submerid.equals(0)){//特约商户
								String percentresult = "1";
								tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, code, paysource, paytype, paystatus, ramrk, percentresult);
								merchantDetailService.insertMerEarningDetail(merid, paymoney, subMerUser.getEarnings(), ordernum, time, paysource, paytype, paystatus);
								userService.updateMerAmount(1, paymoney, merid);//商户收益总额  1为加  0为减
							}else{
								List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
								if(aid!=0) partInfo = areaService.inquirePartnerInfo(aid, 2);
								//分成计算
								Map<String, Object> percentmap = percentCalculateDispose(partInfo, merid, paymoney);
								partmoney = CommUtil.toDouble(percentmap.get("partmoney"));
								mermoney = CommUtil.toDouble(percentmap.get("mermoney"));
								List<Map<String, Object>> percentinfo = (List<Map<String, Object>>) percentmap.get("percentinfo");
								String percentresult = CommUtil.toString(JSON.toJSON(percentinfo));
								//tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, code, 1, 2, 1, ramrk, percentresult);
								tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, code, paysource, paytype, paystatus, ramrk, percentresult);
								for(Map<String, Object> item : percentinfo){
									Integer mercid = CommUtil.toInteger(item.get("merid"));
									Integer partid = CommUtil.toInteger(item.get("partid"));
									//Double percent = CommUtil.toDouble(item.get("percent"));
									Double money = CommUtil.toDouble(item.get("money"));
									if(mercid.equals(0)) mercid = partid;
									merEearningCalculate( mercid, money, type, ordernum, time, paysource, paytype, paystatus);
								}
							}
							equipmentService.updateEquEarn(code, chargeRecord.getExpenditure(), 1);
							if (aid != 0) areaService.updateAreaEarn(1, paymoney, aid,null,paymoney,null);
							//============================特约商户============================
                            logger.info("/payback接口传入的设备号"+"================="+code);
							//============================特约商户============================
						} catch (Exception e) {
							e.printStackTrace();
							logger.warn("数据处理出现重复或错误===" + e.getMessage());
						}
					}
				} catch (Exception e) {
					StackTraceElement[] stackTrace = e.getStackTrace();
					logger.warn("修改金额异常" + e.getMessage() + stackTrace[0].getLineNumber());
				}
				String money1 = String.valueOf(chargeRecord.getExpenditure() * 100);
				int idx = money1.lastIndexOf(".");
				Short money = Short.valueOf(money1.substring(0, idx));
				Short time = 0;
				Short elec = 0;
				if (chargeRecord.getStatus() == null || chargeRecord.getStatus() == 0) {
//					try {
//						Byte port = Byte.valueOf(chargeRecord.getPort().toString());
//						time = Short.valueOf(chargeRecord.getDurationtime());
//						elec = Short.valueOf(chargeRecord.getQuantity() + "");
//						Equipment equipment = equipmentService.getEquipmentById(code);
//						if ("07".equals(equipment.getHardversion()) || DisposeUtil.checkIfHasV3(equipment.getHardversion())) {
//							SendMsgUtil.send_0x27((byte)port, (short)(money / 10), (short)time, (short)elec, code, (byte)1);
//							if (DisposeUtil.checkIfHasV3(equipment.getHardversion())) {
//								SendMsgUtil.resetChargeData(code, port, chargeRecord.getUid(), chargeRecord.getExpenditure(), 0);
//							}
//						} else {
//							SendMsgUtil.send_0x14(port, (short) (money / 10), time, elec, code);// 支付完成充电开始
//						}
//						long session_id = System.currentTimeMillis();
//						Timer timer = new Timer();
//						timer.schedule(new TimerTask() {
//							@Override
//							public void run() {
//								chargepayTask(code, port, chargeRecord.getDurationtime(), chargeRecord.getQuantity(), session_id, ordernum);
//							}
//						}, 60000);
//						Server.chargeTimerNumMap.put(session_id, 2);
//						System.out.println("定时任务已开启");
//					} catch (Exception e) {
//						logger.warn(ordernum + "  [origin] 脉冲     "+new Date());
//						e.printStackTrace();
//					}
				}
//				try {
//					AllPortStatus portStatus = allPortStatusService.findPortStatusByEquipmentnumAndPort(code, chargeRecord.getPort() + 0);
//					if (chargeRecord.getPort() != null && portStatus != null) {
//						AllPortStatus allPortStatus = new AllPortStatus();
//						if (portStatus.getTime() > 0) {
//							allPortStatus.setTime(Short.parseShort(time + portStatus.getTime() + ""));
//						} else {
//							allPortStatus.setTime(time);
//						}
//						allPortStatus.setEquipmentnum(code);
//						allPortStatus.setPort(chargeRecord.getPort());
//						allPortStatus.setPortStatus((byte) 2);
//						allPortStatus.setElec((short) (elec + portStatus.getElec()));
//						allPortStatus.setPower((short) 0);
//						allPortStatusService.updateAllPortStatus(allPortStatus);
//						if (chargeRecord.getIfcontinue() != null && chargeRecord.getIfcontinue() != 0) {
//							equipmentService.insertRealRecord(chargeRecord.getIfcontinue(), chargeRecord.getUid(), chargeRecord.getMerchantid(),
//									code, chargeRecord.getPort(), 1, allPortStatus.getTime() + 0, allPortStatus.getElec() + 0, 0, -1.0);
//						} else {
//							equipmentService.insertRealRecord(chargeRecord.getId(), chargeRecord.getUid(), chargeRecord.getMerchantid(),
//									code, chargeRecord.getPort(), 1, allPortStatus.getTime() + 0, allPortStatus.getElec() + 0, 0, -1.0);
//						}
//					}
//				} catch (Exception e) {
//					logger.warn("端口实时状态数据修改失败" + e.getMessage() + e.getStackTrace()[0].getLineNumber());
//				}
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
			// logger.error("支付回调发布异常：" + e);SortedMap<String, String> params = packagePayParamsToMap(request, money, openid, "wxpay/inCoinsPayback", format, "扫码投币");
			logger.warn("  [origin] 扫码     "+new Date());
			e.printStackTrace();
		}
		return resXml;
	}

	/**
	 * 公众号外部扫描离线卡机充值
	 * @param openid 用户的openid
	 * @param code 设备号
	 * @param model
	 * @return {@link String}
	 */
	@RequestMapping("/offlineCardCharge")
	public String offlineCardCharge(String openid, String code, Model model) {
		Equipment equipment = equipmentService.getEquipmentById(code);
		if (equipment == null) return "erroruser";
//		int send_0x22 = SendMsgUtil.send_0x22(code, 0, (short) 0, (byte) 2);
		String devicenum = CommUtil.toString(equipment.getCode());
		String hardversion = CommUtil.toString(equipment.getHardversion());
		Integer tempid = CommUtil.toInteger(equipment.getTempid());
		Integer bindtype = CommUtil.toInteger(equipment.getBindtype());
		
		UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(devicenum);
		userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
		Integer dealid = CommUtil.toInteger(userEquipment.getUserId());
		User dealeruser = new User();
		if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
		String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
		String servephone = CommUtil.toString(dealeruser.getServephone());
		
		Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, dealid, code, hardversion);
		String brandname = CommUtil.toString(DirectTemp.get("remark"));
		String temphone = CommUtil.toString(DirectTemp.get("common1"));
		servephone = basicsService.getServephoneData(temphone, null, servephone, dealerphne);
		model.addAttribute("phonenum", servephone);
		model.addAttribute("brandname", brandname);
		Integer nowtempid = CommUtil.toInteger(DirectTemp.get("id"));
		Integer temporaryc = CommUtil.toInteger(DirectTemp.get("permit"));
		String chargeInfo = CommUtil.toString(DirectTemp.get("chargeInfo"));
		Integer walletpay = CommUtil.toInteger(DirectTemp.get("walletpay"));
		List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
		tempson = tempson == null ? new ArrayList<>() : tempson;
		model.addAttribute("walletpay", walletpay);
		model.addAttribute("DirectTemp", DirectTemp);
		model.addAttribute("templateLists", tempson);
		if(tempson.size()>=2){
			model.addAttribute("defaultTemp", tempson.get(1).getId());
		}else if(tempson.size()==1){
			model.addAttribute("defaultTemp", tempson.get(0).getId());
		}
		if (bindtype.equals(0)) {
			model.addAttribute("bindtype", 0);
		}else{
			model.addAttribute("bindtype", 1);
		}
		model.addAttribute("code", code);
		model.addAttribute("openid", openid);
		model.addAttribute("nowtime", System.currentTimeMillis() + "");
		return "offlineCard";
	}

	/**
	 * 用户公众号外扫描脉冲设备
	 * @param openid 用户的openid
	 * @param code 设备号
	 * @param model
	 * @return {@link String}
	 */
	@RequestMapping("/inCoins")
	public String inCoins(String openid, String code, Model model) {
		Equipment equipment = equipmentService.getEquipmentById(code);
		if (equipment != null) {
//			SendMsgUtil.send_0x82(code);
			String devicenum = CommUtil.toString(equipment.getCode());
			String hardversion = CommUtil.toString(equipment.getHardversion());
			Integer tempid = CommUtil.toInteger(equipment.getTempid());
			
			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(devicenum);
			userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
			Integer dealid = CommUtil.toInteger(userEquipment.getUserId());
			User dealeruser = new User();
			if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
			String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
			String servephone = CommUtil.toString(dealeruser.getServephone());
			
			Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, dealid, code, hardversion);
			String brandname = CommUtil.toString(DirectTemp.get("remark"));
			String temphone = CommUtil.toString(DirectTemp.get("common1"));
			servephone = basicsService.getServephoneData(temphone, null, servephone, dealerphne);
			model.addAttribute("phonenum", servephone);
			model.addAttribute("brandname", brandname);
			Integer nowtempid = CommUtil.toInteger(DirectTemp.get("id"));
			Integer temporaryc = CommUtil.toInteger(DirectTemp.get("permit"));
			String chargeInfo = CommUtil.toString(DirectTemp.get("chargeInfo"));
			Integer walletpay = CommUtil.toInteger(DirectTemp.get("walletpay"));
			List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
			tempson = tempson == null ? new ArrayList<>() : tempson;
			model.addAttribute("walletpay", walletpay);
			model.addAttribute("DirectTemp", DirectTemp);
			model.addAttribute("templateLists", tempson);
			if(tempson.size()>=2){
				model.addAttribute("defaultTemp", tempson.get(1).getId());
			}else{
				model.addAttribute("defaultTemp", tempson.get(0).getId());
			}
			model.addAttribute("code", code);
			model.addAttribute("openid", openid);
			model.addAttribute("nowtime", System.currentTimeMillis() + "");
			return "inCoins";
		}
		return "erroruser";
	}
	
	/**
	 * 用户扫描脉冲设备进行微信支付
	 * @param request 请求
	 * @return {@link Map}
	 * @throws Exception
	 */
	@RequestMapping("/inCoinsPay")
	@ResponseBody
	public Map<String, Object> inCoinsPay(HttpServletRequest request) throws Exception {
		Map<String, Object> paramMap = new HashMap<>();
		String openid = request.getParameter("openId");
		String code = request.getParameter("code");
		String port = request.getParameter("port");
		String tempid = request.getParameter("tempid");
		String attention = request.getParameter("attention");
		// 根据设备查询设备信息
		Equipment equipmentById = equipmentService.getEquipmentById(code);
		if (equipmentById.getState() == 0) {
			paramMap.put("line", "0");
			return paramMap;
		}
		// 查询子模版信息
		TemplateSon templateSon = templateService.getInfoTemplateOne(Integer.parseInt(tempid));
		Double money = templateSon.getMoney();
		Double remark = templateSon.getRemark();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// 查询用户信息
		User user = userService.getUserByOpenid(openid);
		Integer uid = user == null ? 0 : user.getId();
		String format = df.format(new Date()) + code;
		try {
			// 插入投币预订单
			inCoinsService.insertInCoinsRecord(format, code, Byte.parseByte(port), uid, money,
					(Byte.parseByte(doubleToString(remark))), (byte) 1, (byte) 0);
		} catch (Exception e) {
			logger.warn("投币表添加微信支付数据失败订单号" + format + "：" + e.getMessage());
			paramMap.put("wolferror", "0");
			return paramMap;
		}
		//-------------------------------------------------
		// 判断设备的所属商户是不是微信特约商户
		logger.info("用户扫码投币的设备:"+code);
		boolean data = userEquipmentService.subMerByCode(code);
		// 如果是特约商户
		String subAppid = null;
		SortedMap<String, String> params = null;
		if(data){
			logger.info("用户使用接口:inCoinsPay对特约商户进行付款");
			params = packagePayParamsToMapSub(request, money, openid, "wxpay/inCoinsPayback", format, "扫码投币");
			// 获取商户的微信配置信息
			Map<String, Object> subData = userService.selectConfigDataByCode(code);
			subAppid = CommUtil.toString(subData.get("subappid"));
		}else{
			logger.info("用户使用接口:inCoinsPay对服务平台进行付款");
			// 服务商的配置信息
			params = packagePayParamsToMap(request, money, openid, "wxpay/inCoinsPayback", format, "扫码投币");
			subAppid = WeiXinConfigParam.APPID;
		}
		//------------------------------------------------
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String canshu = HttpRequest.getRequestXml(params);
		String sr = HttpRequest.sendPost(url, canshu);
		Map<String, String> map = XMLUtil.doXMLParse(sr);
		SortedMap<String, String> seconde = new TreeMap<>();
		seconde.put("appId", subAppid);
		String time = HttpRequest.getTimeStamp();
		seconde.put("timeStamp", time);
		String sjzf = HttpRequest.getRandomStringByLength(30);

		seconde.put("nonceStr", sjzf);
		seconde.put("package", "prepay_id=" + (String) map.get("prepay_id"));
		seconde.put("signType", "MD5");

		String sign2 = HttpRequest.createSign("UTF-8", seconde);
		paramMap.put("appId", subAppid);
		paramMap.put("prepay_id", map.get("prepay_id"));
		paramMap.put("date", time);
		paramMap.put("paySign", sign2);
		paramMap.put("packagess", "prepay_id=" + (String) map.get("prepay_id"));
		paramMap.put("nonceStr", sjzf);
		paramMap.put("signType", "MD5");
		paramMap.put("out_trade_no", params.get("out_trade_no"));
		if (attention != null && "1".equals(attention)) {
			paramMap.put("attention", attention);
		}
		return paramMap;
	}
	/**
	 * 离线卡充值
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/offlineCharge")
	@ResponseBody
	public Map<String, Object> offlineCharge(HttpServletRequest request) throws Exception {
		String openid = request.getParameter("openId");
		String code = request.getParameter("code");
		String card_id = request.getParameter("card_id");
		String card_ope = request.getParameter("card_ope");
		String tempid = request.getParameter("tempid");
		TemplateSon templateSon = templateService.getInfoTemplateOne(Integer.parseInt(tempid));
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String format = df.format(new Date()) + code;
		
		//-------------------------------------------------
		// 判断设备的所属商户是不是微信特约商户
		logger.info("offlineCharge接口传入的设备号"+"============"+code);
		boolean data = userEquipmentService.subMerByCode(code);
		SortedMap<String, String> params = null;
		String subAppid = null; 
		if(data){
			logger.info("使用接口:offlineCharge对特约商户进行支付");
			params = packagePayParamsToMapSub(request, templateSon.getMoney(), openid, "wxpay/offlinePayback", format, "离线卡充值");
			// 获取商户的微信配置信息
			Map<String, Object> subData = userService.selectConfigDataByCode(code);
			subAppid = CommUtil.toString(subData.get("subappid"));
		}else{
			 logger.info("使用接口:offlineCharge对自助充电平台进行支付");
			 params = packagePayParamsToMap(request, templateSon.getMoney(), openid, "wxpay/offlinePayback", format, "离线卡充值");
			 subAppid = WeiXinConfigParam.APPID;
		}
		//-------------------------------------------------------
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String canshu = HttpRequest.getRequestXml(params);
		String sr = HttpRequest.sendPost(url, canshu);
		Map<String, String> map = XMLUtil.doXMLParse(sr);

		SortedMap<String, String> seconde = new TreeMap<>();
		seconde.put("appId", subAppid);
		String time = HttpRequest.getTimeStamp();
		seconde.put("timeStamp", time);
		String sjzf = HttpRequest.getRandomStringByLength(30);

		seconde.put("nonceStr", sjzf);
		seconde.put("package", "prepay_id=" + (String) map.get("prepay_id"));
		seconde.put("signType", "MD5");

		String sign2 = HttpRequest.createSign("UTF-8", seconde);

		Map<String, Object> paramMap = new HashMap<>();

		User user = userService.getUserByOpenid(openid);
		Integer uid = user == null ? 0 : user.getId();
		offlineCardService.insertChargeMoneyOfflineCardRecord(params.get("out_trade_no"), code, card_id,
				templateSon.getMoney(), templateSon.getRemark(), Integer.parseInt(card_ope), 1, uid,0);

		paramMap.put("appId", subAppid);
		paramMap.put("prepay_id", map.get("prepay_id"));
		paramMap.put("date", time);
		paramMap.put("paySign", sign2);
		paramMap.put("packagess", "prepay_id=" + (String) map.get("prepay_id"));
		paramMap.put("nonceStr", sjzf);
		paramMap.put("signType", "MD5");
		paramMap.put("out_trade_no", params.get("out_trade_no"));
		return paramMap;
	}

	/**
	 * 离线充值机微信支付回调
	 * @throws JDOMException
	 */
	@RequestMapping({ "/offlinePayback" })
	@ResponseBody
	public String offlinePayback() throws JDOMException {

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
				// 处理业务完毕
				// ------------------------------
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				out.write(resXml.getBytes());
				out.flush();
				out.close();
				String ordernum = CommUtil.toString(map.get("out_trade_no"));
				OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
				String cardID = offlineCard.getCardID();
				logger.warn(ordernum + "  [origin] 离线卡充值     "+cardID +"  "+offlineCard.getBeginTime());
				try {
					int parseLong = (int) Long.parseLong(cardID, 16);
					String accountmoney = String.valueOf(offlineCard.getAccountmoney() * 10);
					String money = accountmoney.substring(0, accountmoney.indexOf("."));
					short parseShort = Short.parseShort(money);
//					SendMsgUtil.send_0x22(offlineCard.getEquipmentnum(), parseLong, parseShort, (byte) 1);
					//WolfHttpRequest.sendOfflineCardPaydata(offlineCard.getEquipmentnum(), cardID, parseShort, (byte) 1);
				} catch (Exception e) {
					logger.warn(ordernum + "离线卡充值异常[origin]");
					e.printStackTrace();
				}
				
				if (offlineCard.getStatus() == null || offlineCard.getStatus() == 0) {
					offlineCard.setStatus(1);
					offlineCardService.updateChargeMoneyOfflineCardRecordPaytype(offlineCard);
					//修改本地数据
					Integer uid = CommUtil.toInteger(offlineCard.getUid());
					Integer merid = CommUtil.toInteger(offlineCard.getMerchantid());
					Double paymoney = CommUtil.toDouble(offlineCard.getChargemoney());//实际付款金额
					Double accountmoney = CommUtil.toDouble(offlineCard.getAccountmoney());//到账金额
					Integer orderid = CommUtil.toInteger(offlineCard.getId());
					String devicenum = CommUtil.toString(offlineCard.getEquipmentnum());
					String port = null;
					Date time = new Date();
					String strtime = CommUtil.toDateTime(time);
					Integer paysource = MerchantDetail.OFFLINESOURCE;
					Integer paytype = MerchantDetail.WEIXINPAY;
					Integer paystatus = MerchantDetail.NORMAL;
					Integer type = 1;
					Double mermoney = 0.00;
//					Equipment devicedata = equipmentService.getEquipmentAndAreaById(devicenum);
//					Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
//					Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
//					String devicename = CommUtil.toString(devicedata.getRemark());
//					String areaaddress = CommUtil.toString(devicedata.getAddress());
//					String servicphone = systemSetService.getServicePhone(devicetempid, deviceaid, merid);
//					
//					systemSetService.sendMmessageCharge(ordernum, orderid, paysource, merid, uid, paymoney, strtime, 
//						devicenum, port, devicename, deviceaid, areaaddress, servicphone);
					
//					messagenotification( first, uid, merid, paysource, paymoney, strtime, status, remark,  orderid, ordernum);
					Map<String, Object> usequmap = userEquipmentService.getEquipToUserByCode(devicenum);
					String ramrk = CommUtil.toString(usequmap.get("hardversion"));
					Integer aid = CommUtil.toInteger(usequmap.get("aid"));
					Double partmoney = 0.00;
					try {
						User subMerUser = userService.selectUserById(merid);
						Integer submerid = CommUtil.toInteger(subMerUser.getSubMer());
						if(!submerid.equals(0)){//特约商户
							String percentresult = "1";
							tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, paysource, paytype, paystatus, ramrk, percentresult);
							merchantDetailService.insertMerEarningDetail(merid, paymoney, subMerUser.getEarnings(), ordernum, time, paysource, paytype, paystatus);
							userService.updateMerAmount(1, paymoney, merid);//商户收益总额  1为加  0为减
						}else{
							List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
							if(aid!=0) partInfo = areaService.inquirePartnerInfo(aid, 2);
							//分成计算
							Map<String, Object> percentmap = percentCalculateDispose(partInfo, merid, paymoney);
							partmoney = CommUtil.toDouble(percentmap.get("partmoney"));
							mermoney = CommUtil.toDouble(percentmap.get("mermoney"));
							List<Map<String, Object>> percentinfo = (List<Map<String, Object>>) percentmap.get("percentinfo");
							String percentresult = CommUtil.toString(JSON.toJSON(percentinfo));
							tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, paysource, paytype, paystatus, ramrk, percentresult);
							for(Map<String, Object> item : percentinfo){
								Integer mercid = CommUtil.toInteger(item.get("merid"));
								Integer partid = CommUtil.toInteger(item.get("partid"));
								//Double percent = CommUtil.toDouble(item.get("percent"));
								Double money = CommUtil.toDouble(item.get("money"));
								if(mercid.equals(0)) mercid = partid;
								merEearningCalculate( mercid, money, type, ordernum, time, paysource, paytype, paystatus);
							}
						}
						equipmentService.updateEquEarn(offlineCard.getEquipmentnum(), offlineCard.getChargemoney(), 1);
						if (aid != 0) areaService.updateAreaEarn(1, paymoney, aid,null,paymoney,null);
						// 根据离线卡充值机商家的id查询商家的信息
						logger.info("offlinePayback接口传入的设备号"+"========="+devicenum);
						
						String phone = getTempPhone(offlineCard.getEquipmentnum(), offlineCard.getMerchantid(), 1);
						String detailurl = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=2&id="+offlineCard.getId();
						String first = "您好，卡充值成功，欢迎您的使用。\r\n订单编号："+ordernum;
						TempMsgUtil.paychargesendmsg( first, offlineCard.getUid(), phone, detailurl, paymoney, CommonConfig.PAYRETURNMES);
						
						if(merid!=0 && authorityService.authorSwitch(merid, 3)){
							User touuser = userService.selectUserById(uid);
							User meruser = userService.selectUserById(merid);
							String fistdata = "您好，您的用户"+CommUtil.trimToEmpty(touuser.getUsername()) +"["+StringUtil.StringNumer(uid.toString())+"]离线卡["+cardID+"]充值成功。";
							String meroppenid = CommUtil.toString(meruser.getOpenid());
							String msgpaymoney = "充值金额："+accountmoney + "元，\r\n实际支付：" + paymoney + "元"; 
							OfflineCard offlineCardInfo = offlineCardService.gainOfflineData(CommUtil.toString(cardID));
							Double balance = CommUtil.toDouble(offlineCardInfo.getBalance()) + accountmoney;
							String msgbalance = balance + "元"; 
							String remark = "订单号: "+ordernum;
							String url = "";
							TempMsgUtil.dealerOrderPush(fistdata, meroppenid, msgpaymoney, msgbalance, strtime, remark, url);
						}
					} catch (Exception e) {
						logger.warn("小区修改余额错误===" + e.getMessage());
					}
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
			logger.error("[origin]离线充值支付回调异常：" + new Date());
			e.printStackTrace();
		}
		return resXml;
	}

	/**
	 * 微信支付回调
	 * 
	 * @return
	 * @throws JDOMException
	 */
	@RequestMapping({ "/inCoinsPayback" })
	@ResponseBody
	public String inCoinsPayback() throws JDOMException {

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
				// 处理业务完毕
				// ------------------------------
				String ordernum = map.get("out_trade_no").toString();
				InCoins inCoins = null;
				try {
					inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
				} catch (Exception e) {
					resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
							+ "<return_msg><![CDATA[INCOINS ERROR]]></return_msg>" + "</xml> ";
					BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
					out.write(resXml.getBytes());
					out.flush();
					out.close();
					return resXml;
				}
				if (inCoins == null) {
					resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
							+ "<return_msg><![CDATA[INCOINS IS NULL]]></return_msg>" + "</xml> ";
					BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
					out.write(resXml.getBytes());
					out.flush();
					out.close();
					return resXml;
				}
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				out.write(resXml.getBytes());
				out.flush();
				out.close();
				String money = String.valueOf(inCoins.getMoney());
				int idx = money.lastIndexOf(".");
				String total_fee = money.substring(0, idx);
				logger.warn(ordernum + "  [origin] 脉冲     "+new Date());
				// 设备号
				String devicenum = CommUtil.toString(inCoins.getEquipmentnum());
				// 空投币订单或未支付的订单
				if (inCoins.getStatus() == null || inCoins.getStatus() == 0) {
					// 根据订单号将预订单改变为正常
					inCoinsService.updateInCoinsStatusAndRecycletype(ordernum, (byte) 1);
					try {
//						SendMsgUtil.send_0x83(devicenum, inCoins.getPort(), Byte.parseByte(total_fee));
//						WolfHttpRequest.sendIncoinsPaydata(devicenum, inCoins.getPort(), Byte.parseByte(total_fee));
					} catch (Exception e) {
						logger.warn(
								inCoins.getOrdernum() + "：投币充电异常！" + e.getMessage() + e.getStackTrace()[0].getLineNumber());
					}
					Integer uid = CommUtil.toInteger(inCoins.getUid());
					Integer merid = CommUtil.toInteger(inCoins.getMerchantid());
					Double paymoney = CommUtil.toDouble(inCoins.getMoney());
					Integer orderid = CommUtil.toInteger(inCoins.getId());
					String port = CommUtil.toString(inCoins.getPort());
					Date time = new Date();
					String strtime = CommUtil.toDateTime(time);
					Integer paysource = MerchantDetail.INCOINSSOURCE;
					Integer paytype = MerchantDetail.WEIXINPAY;
					Integer paystatus = MerchantDetail.NORMAL;
					Integer type = 1;
					Double mermoney = 0.00;
					String first = "";
					String remark = "";
					String status = "付款成功,开始充电。";
					// 根据设备号查询设备信息
					Equipment devicedata = equipmentService.getEquipmentAndAreaById(devicenum);
					Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
					Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
					String devicename = CommUtil.toString(devicedata.getRemark());
					String areaname = CommUtil.toString(devicedata.getName());
					// 获取服务电话
					String servicphone = systemSetService.getServicePhone(devicetempid, deviceaid, merid);
					// 发送信息
					systemSetService.sendMmessageCharge(ordernum, orderid, paysource, merid, uid, paymoney, strtime, 
						devicenum, port, devicename, deviceaid, areaname, servicphone);
					
					Map<String, Object> usequmap = userEquipmentService.getEquipToUserByCode(devicenum);
					String ramrk = CommUtil.toString(usequmap.get("hardversion"));
					Integer aid = CommUtil.toInteger(usequmap.get("aid"));
					Double partmoney = 0.00;
					try {

						User subMerUser = userService.selectUserById(merid);
						Integer submerid = CommUtil.toInteger(subMerUser.getSubMer());
						if(!submerid.equals(0)){//特约商户
							String percentresult = "1";
							tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, paysource, paytype, paystatus, ramrk, percentresult);
							merchantDetailService.insertMerEarningDetail(merid, paymoney, subMerUser.getEarnings(), ordernum, time, paysource, paytype, paystatus);
							userService.updateMerAmount(1, paymoney, merid);//商户收益总额  1为加  0为减
						}else{
							List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
							if(aid!=0) partInfo = areaService.inquirePartnerInfo(aid, 2);
							//分成计算
							Map<String, Object> percentmap = percentCalculateDispose(partInfo, merid, paymoney);
							partmoney = CommUtil.toDouble(percentmap.get("partmoney"));
							mermoney = CommUtil.toDouble(percentmap.get("mermoney"));
							List<Map<String, Object>> percentinfo = (List<Map<String, Object>>) percentmap.get("percentinfo");
							String percentresult = CommUtil.toString(JSON.toJSON(percentinfo));
							tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, paysource, paytype, paystatus, ramrk, percentresult);
							for(Map<String, Object> item : percentinfo){
								Integer mercid = CommUtil.toInteger(item.get("merid"));
								Integer partid = CommUtil.toInteger(item.get("partid"));
								//Double percent = CommUtil.toDouble(item.get("percent"));
								Double moneyp = CommUtil.toDouble(item.get("money"));
								if(mercid.equals(0)) mercid = partid;
								merEearningCalculate( mercid, moneyp, type, ordernum, time, paysource, paytype, paystatus);
							}
						}
						// 支付成功更新设备的投币收益
						equipmentService.updateEquEarn(devicenum, inCoins.getMoney(), 1);
						if (aid != 0)  areaService.updateAreaEarn(1, paymoney, aid,null,paymoney,null);
					} catch (Exception e) {
						logger.warn("小区修改余额错误===" + e.getMessage());
					}
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
			logger.warn("  [origin] 脉冲     "+new Date());
			e.printStackTrace();
		}
		return resXml;
	}

	@RequestMapping("/offlinePayfail")
	public String offlinePayfail(@RequestParam("out_trade_no") String ordernum) {
		offlineCardService.deleteOfflineCardByOrdernum(ordernum);
		return "1";
	}
	
	/**
	 * /wxpay/doWeChatRefund?id=&ordersource=&refundtype=&password=
	 * @method_name: doWeChatRefund
	 * @Description: 退款统一测试
	 * @param orderid：订单号    ordersource：订单来源 [以交易记录为准]   password：退款类型   refundtype：密码
	 * @return
	 * @Author: origin  创建时间:2020年8月27日 下午7:08:15
	 * @common:
	 */
	@RequestMapping({ "/doWeChatRefund" })
	@ResponseBody
	public Object doWeChatRefund(Integer orderid, Integer ordersource, String password, Integer refundtype) throws Exception {// utype
		Map<String, Object> map = new HashMap<>();
		Map<String, Object>  result = commonMethodService.doWeChatRefund( orderid, ordersource, refundtype);
		return map;
	}
	
	/**
	 * 微信退费
	 * @param id 充电记录中的id
	 * @param refundState 不同类型的充电
	 * @param pwd 密码
	 * @param utype 
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping({ "/doRefund" })
	@ResponseBody
	public Object doRefund(Integer id, Integer refundState, String pwd, Integer utype) throws Exception {// utype
		Map<String, Object> map = new HashMap<>();
		User userSession = (User) request.getSession().getAttribute("admin");
		if (utype == 1) {
			if (pwd == null || userSession == null) {
				map.put("ok", "pwderror");
			} else {
				String md5Pwd = MD5Util.MD5Encode(pwd, "utf-8");
				String password = userSession.getPassword();
				if (!md5Pwd.equals(password)) {
					map.put("ok", "pwderror");
				} else {
					String wolfkey = request.getParameter("wolfkey");
					if (wolfkey != null) {
						map.put("wolfkey", Integer.parseInt(wolfkey));
					}
					map = dorefund(id, refundState, map);
				}
			}
		} else if (utype == 2 || utype == 3) {
			String wolfkey = request.getParameter("wolfkey");
			if (wolfkey != null) {
				map.put("wolfkey", Integer.parseInt(wolfkey));
			}
			map = dorefund(id, refundState, map);
		}
		return map;
	}
	
	/**
	 * 根据id查询充电信息
	 * @param refundState 充电类型:
	 * @param id 充电id
	 * @return
	 */
	public Map<String, Object> doRefundMoney(Integer refundState, Integer id){
		Map<String, Object> map = new HashMap<String, Object>();
		String check = "ok";
		String total_fee = "";
		String out_trade_no = "";// 退款订单号
		Integer merid = 0;
		// 扫码充电记录
		if (refundState == 1) {
			ChargeRecord chargeRecord = chargeRecordService.chargeRecordOne(id);
			String money = CommUtil.toString(CommUtil.toDouble(chargeRecord.getExpenditure()) * 100);
			int idx = money.lastIndexOf(".");
			total_fee = money.substring(0, idx);
			out_trade_no = CommUtil.toString(chargeRecord.getOrdernum());// 退款订单号
			merid = chargeRecord.getMerchantid();
			logger.info("商家的id"+merid+"======================================");
		// 离线卡记录
		} else if (refundState == 2) {
			OfflineCard offlineCard = offlineCardService.selectOfflineCardById(id);
			check = "error";
			if(offlineCard!=null){
				if(offlineCard.getHandletype()!=2){//充值退费
					String money = String.valueOf(CommUtil.toDouble(offlineCard.getChargemoney()) * 100);
					int idx = money.lastIndexOf(".");
					total_fee = money.substring(0, idx);
					out_trade_no = offlineCard.getOrdernum();// 退款订单号
					merid = offlineCard.getMerchantid();
					check = "ok";
				}
			}
		// 投币充电记录
		} else if (refundState == 3) {
			InCoins incoins = inCoinsService.selectInCoinsRecordById(id);
			String money = String.valueOf(incoins.getMoney() * 100);
			int idx = money.lastIndexOf(".");
			total_fee = money.substring(0, idx);
			out_trade_no = incoins.getOrdernum();// 退款订单号
			merid = incoins.getMerchantid();
		// 钱包充值记录
		} else if (refundState == 4) {
			Money money = moneyService.payMoneyinfo(id);
//			String paymoney = String.valueOf(money.getMoney() * 100);
			String paymoney = CommUtil.toString(money.getMoney() * 100);
			int idx = paymoney.lastIndexOf(".");
			total_fee = paymoney.substring(0, idx);
			out_trade_no = money.getOrdernum();// 退款订单号
			// 根据钱包充值的订单查询商家的信息
			merid = moneyService.selectMerByMoneyOrdernum(out_trade_no);
			if (walletRich(money) == false) check = "error";
		// 在线充电记录
		} else if (refundState == 5) {
			OnlineCardRecord cardRecord = onlineCardRecordService.selectOnlineCard(id);
			OnlineCard oncard = onlineCardService.selectOnlineCardByCardID(cardRecord.getCardID());
			Integer statuse = CommUtil.toInteger(oncard.getStatus());
			if(statuse.equals(0)){//在线卡未激活
				map.put("statuse", 0);
			}
			merid = CommUtil.toInteger(oncard.getMerid());
			if(oncard.getStatus()==0) merid = 0;
			String paymoney = String.valueOf(cardRecord.getMoney() * 100);
			int idx = paymoney.lastIndexOf(".");
			total_fee = paymoney.substring(0, idx);
			out_trade_no = cardRecord.getOrdernum();// 退款订单号
			if (checkOnlineRich(cardRecord) == false) check = "error";
		// 包月充电记录
		} else if (refundState == 6) {
			PackageMonthRecord monthRecord = userService.selectMonthRecordById(id);
			PackageMonth packageMonth = userService.selectPackageMonthByUid(monthRecord.getUid());
			long currentTime = System.currentTimeMillis();
			long endTime = packageMonth.getEndTime().getTime();
			System.out.println("剩余次数比较===" + (packageMonth.getSurpnum() < monthRecord.getChangenum()));
			System.out.println("时间比较===" + (currentTime > endTime));
//			if (packageMonth.getSurpnum() < monthRecord.getChangenum() || currentTime > endTime) {
//				check = "error";
//			}
			merid = monthRecord.getMerid();
			String paymoney = String.valueOf(monthRecord.getMoney() * 100);
			int idx = paymoney.lastIndexOf(".");
			total_fee = paymoney.substring(0, idx);
			out_trade_no = monthRecord.getOrdernum();// 退款订单号
		}
		map.put("merid", merid);
		map.put("check", check);
		map.put("total_fee", total_fee);
		map.put("out_trade_no", out_trade_no);
		return map;
	}
	
	/**
	 * 退款支付回调
	 * 根据不同的商户号进行退款
	 * @param id 充电id
	 * @param refundState 消费的类型(1:充电,2:离线卡,3:投币,4:钱包,5:在线卡,6:包月)
	 * @param map worfkey
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> dorefund(Integer id, Integer refundState, Map<String, Object> map) throws Exception {
		WXPayConfigImpl config = WXPayConfigImpl.getInstance();
		WXPay wxpay = new WXPay(config);
		// 根据id查询充电信息
		Map<String, Object> refundmap = doRefundMoney(refundState, id);
		String total_fee = CommUtil.toString(refundmap.get("total_fee"));
		String out_trade_no = CommUtil.toString(refundmap.get("out_trade_no"));
		Integer merid = CommUtil.toInteger(refundmap.get("merid"));
		String check = CommUtil.toString(refundmap.get("check"));
		String statuse = CommUtil.toString(refundmap.get("statuse"));
		if(check!=null && check.equals("error")){
			map.put("ok", "usererror");
			return map;
		}
		TradeRecord traderecord = new TradeRecord();
		// 根据订单号查询正常的订单
		if(statuse != "0"){
			List<TradeRecord> tradelist = tradeRecordService.getTraderecordList(out_trade_no, 1);
			if(tradelist.size()<1){
				map.put("ok", "error");
				return map;
			}else if(tradelist.size()>=1){
				traderecord = tradelist.get(0);
			}
		}
		String subMchId = null;
		String subAppid = null;
		// 检查用户的分成信息
		logger.info("A设备商家的id"+"============"+merid);
		if (merid != null && merid != 0) {
			// 判断商户是否为特约商户
			User merUser = userService.selectUserById(merid);
			if(merUser.getSubMer().equals(1)){
				Map<String, Object> configData = userService.selectSubMerConfigById(merid);
				subMchId = CommUtil.toString(configData.get("submchid"));
				subAppid = CommUtil.toString(configData.get("subappid"));
				logger.info("C我是特约商户的参数:"+subMchId+"============"+subAppid);
			}else{
				subMchId = WeiXinConfigParam.SUBMCHID;
				if (checkUserIfRich(traderecord) == false) {
					map.put("ok", "moneyerror");
					return map;
				}
				logger.info("D我是特约商户退款:"+subMchId+"============"+subAppid);
			}
		}
		// 获取交易订单合伙人信息
		String comment = CommUtil.toString(traderecord.getComment());
		// 配置商户退款信息
		SortedMap<String, String> params = new TreeMap<>();
		params.put("appid", WeiXinConfigParam.FUWUAPPID);
		params.put("mch_id", WeiXinConfigParam.MCHID);
		params.put("sub_mch_id",subMchId);
		int wolfkey = (int) (map.get("wolfkey") == null ? 0 : map.get("wolfkey"));
		if (wolfkey == 3) {
			params.put("sub_appid", WeiXinConfigParam.SMALLAPPID);
		}
		params.put("out_trade_no", out_trade_no);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/orderquery";
		String canshu = HttpRequest.getRequestXml(params);
		String sr = HttpRequest.sendPost(url, canshu);
		Map<String, String> resultMap = XMLUtil.doXMLParse(sr);
		// 退款后的业务处理
		logger.info("F退款的参数"+"======================"+resultMap);
		if (resultMap.get("return_code").toString().equalsIgnoreCase("SUCCESS")) {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("appid", WeiXinConfigParam.FUWUAPPID);
			data.put("mch_id", WeiXinConfigParam.MCHID);
			data.put("sub_mch_id", subMchId);
			if (wolfkey == 3) {
				data.put("sub_appid", resultMap.get("sub_appid"));
			}
			data.put("transaction_id", resultMap.get("transaction_id"));
			data.put("out_trade_no", out_trade_no);// 定单号
			data.put("out_refund_no", "t" + out_trade_no);
			data.put("total_fee", total_fee);
			data.put("refund_fee", total_fee);
			data.put("refund_fee_type", "CNY");
			data.put("op_user_id", config.getMchID());

			try {
				Map<String, String> r = wxpay.refund(data);
				logger.info("G退款后的参数"+"========================"+r.toString());
				// 处理退款后的订单 成功
				if ("SUCCESS".equals(r.get("result_code"))) {
					// comment字段更改前的处理方式
					if(comment == null || comment.equals("0")){//之前数据
						amendmysql( refundState, id, out_trade_no, map);
					}else{
						logger.info("I开始处理特约商户的退款数据"+"======================");
						// 根据消费的类型去更改对应的数据
						if (refundState == 1) {
							chargeRefund(id, out_trade_no, map);
						} else if (refundState == 2) {
							offlineCardRefund(id, out_trade_no, map);
						} else if (refundState == 3) {
							inCoinsRefund(id, out_trade_no, map);
						} else if (refundState == 4) {
							walletRefund(id, out_trade_no, map);
						} else if (refundState == 5) {
							onlinecardRefund(id, out_trade_no, map);
						} else if (refundState == 6) {
							packageMonthRefund(id, out_trade_no, map);
						}
					}
					
					map.put("ok", "ok");
				}
				if ("FAIL".equals(r.get("result_code"))) {

					map.put("ok", "error");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("退款失败");
				map.put("ok", "error");
			}
		} else {
			map.put("ok", "error");
		}
		return map;
	}
	
	/**
	 * @Description： 退款完成后处理数据(不存在合伙人)
	 * @author： origin   
	 */
	public void amendmysql(Integer refundState, Integer id, String out_trade_no, Map<String, Object> map){
		try {
			// 修改充电的记录
			if (refundState == 1) {
				// 根据id查询充电记录
				ChargeRecord chargeRecord = chargeRecordService.chargeRecordOne(id);
				// 结束时间为空
				if (chargeRecord.getEndtime() == null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					// 根据id将订单更能改为已退款
					chargeRecordService.updateNumberById(null, 1, chargeRecord.getId(), null, null,
							chargeRecord.getExpenditure(), sdf.format(chargeRecord.getBegintime()), CommUtil.toDateTime());
				} else {
					// 根据id将订单更能改为已退款
					chargeRecordService.updateNumberById(null, 1, chargeRecord.getId(), null, null,
							chargeRecord.getExpenditure(), null, CommUtil.toDateTime());
				}
				// 订单不是全额退款
				if (chargeRecord.getNumber() != 1) {
//					TradeRecord traderecord = tradeRecordService.getTraderecord(chargeRecord.getOrdernum());
					// 根据订单查询订单正常的订单
					List<TradeRecord> tradelist = tradeRecordService.getTraderecordList(out_trade_no, 1);
					TradeRecord traderecord = new TradeRecord();
					if(tradelist.size()>=1){
						traderecord = tradelist.get(0);
					}
					// 获取管理者id
					Integer manid = traderecord.getManid();
					// 根据商户id查询商户
					User user = userService.selectUserById(chargeRecord.getMerchantid());
					// 不存在管理者时更改的数据
					if( null == manid || manid == 0){
						// 特约微信商户资金不用变动
						if(user.getSubMer().equals(0)){
							user.setEarnings((user.getEarnings() * 100 - chargeRecord.getExpenditure() * 100) / 100);
							// 减少商家的收益
							userService.updateUserEarnings(0, chargeRecord.getExpenditure(), user.getId());
							System.out.println("---充电不分成 退费 修改成功---");
						}
						// 记录资金变动明细
						merchantDetailService.insertMerEarningDetail(user.getId(), chargeRecord.getExpenditure(),
								user.getEarnings(), out_trade_no, new Date(), MerchantDetail.CHARGESOURCE,
								MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
						// 记录交易信息
						tradeRecordService.insertTrade(user.getId(), chargeRecord.getUid(), out_trade_no,
								chargeRecord.getExpenditure(), chargeRecord.getExpenditure(), chargeRecord.getEquipmentnum(), 1, 2, 2,
								equipmentService.getEquipmentById(chargeRecord.getEquipmentnum()).getHardversion());
					}else{
						// 存在管理者时更改收益
						User manuser = userService.selectUserById(manid);
						userService.updateUserEarnings(0, traderecord.getMermoney(), user.getId());
						userService.updateUserEarnings(0, traderecord.getManmoney(), manid);
						manuser.setEarnings((manuser.getEarnings() * 100 - traderecord.getManmoney() * 100) / 100);
						user.setEarnings((user.getEarnings() * 100 - traderecord.getMermoney() * 100) / 100);
						System.out.println("--- 充电分成 退费  修改成功---");
						// 插入明细
						merchantDetailService.insertMerEarningDetail(user.getId(), traderecord.getMermoney(), user.getEarnings(), 
								out_trade_no, new Date(), MerchantDetail.CHARGESOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
						merchantDetailService.insertMerEarningDetail(manuser.getId(), traderecord.getManmoney(), manuser.getEarnings(), 
								out_trade_no, new Date(), MerchantDetail.CHARGESOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
						// 记录交易
						tradeRecordService.insertToTrade(user.getId(), manid, chargeRecord.getUid(), out_trade_no,
								chargeRecord.getExpenditure(), traderecord.getMermoney(), traderecord.getManmoney(), chargeRecord.getEquipmentnum(), 1, 2, 2,
								equipmentService.getEquipmentById(chargeRecord.getEquipmentnum()).getHardversion());
					}
				}
				String phone = getTempPhone(chargeRecord.getEquipmentnum(), chargeRecord.getMerchantid(), 0);
				String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=1&id="+chargeRecord.getId();
				// 发送消息
				returnMsgTemp(chargeRecord.getUid(), phone, chargeRecord.getEquipmentnum(), chargeRecord.getOrdernum(), urltem, StringUtil.toDateTime(), chargeRecord.getExpenditure());
			// 更改离线卡数据
			}else if (refundState == 2) {
				// 根据id插询
				OfflineCard offlineCard = offlineCardService.selectOfflineCardById(id);
				// 离线卡微信退费
				offlineCardService.offlineCardRefund(3, id);
				if (offlineCard.getPaytype() != 3) {
					// 查询离线卡数据
					TradeRecord traderecord = tradeRecordService.getTraderecord(offlineCard.getOrdernum());
					Integer manid = traderecord.getManid();
					User user = userService.selectUserById(offlineCard.getMerchantid());
					// 微信特约商户收益不变化
					if( null == manid || manid.equals(0)){
						// 减去商户的收益
						if(user.getSubMer().equals(0)){
							userService.updateUserEarnings(0, offlineCard.getChargemoney(), user.getId());
							user.setEarnings((user.getEarnings() * 100 - offlineCard.getChargemoney() * 100) / 100);
							System.out.println("---输出离线卡退费信息 修改成功---");
						}
						// 记录资金变动明细
						merchantDetailService.insertMerEarningDetail(user.getId(), offlineCard.getChargemoney(), user.getEarnings(), 
								out_trade_no, new Date(), MerchantDetail.OFFLINESOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
						// 插入交易记录
						tradeRecordService.insertTrade(user.getId(), offlineCard.getUid(), out_trade_no, offlineCard.getChargemoney(), 
								offlineCard.getChargemoney(), offlineCard.getEquipmentnum(), 3, 2, 2,
								equipmentService.getEquipmentById(offlineCard.getEquipmentnum()).getHardversion());
					}else{
						System.out.println("---输出离线卡退费信息 分成修改成功---");
						User manuser = userService.selectUserById(manid);
						userService.updateUserEarnings(0, traderecord.getMermoney(), user.getId());
						userService.updateUserEarnings(0, traderecord.getManmoney(), manid);
						manuser.setEarnings((manuser.getEarnings() * 100 - traderecord.getManmoney() * 100) / 100);
//						userService.updateUserById(manuser);
//						if(user.getId()==manuser.getId()) user = userService.selectUserById(offlineCard.getMerchantid());
						user.setEarnings((user.getEarnings() * 100 - traderecord.getMermoney() * 100) / 100);
//						userService.updateUserById(user);
						merchantDetailService.insertMerEarningDetail(user.getId(), traderecord.getMermoney(), user.getEarnings(), 
								out_trade_no, new Date(), MerchantDetail.OFFLINESOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
						merchantDetailService.insertMerEarningDetail(manuser.getId(), traderecord.getManmoney(), manuser.getEarnings(), 
								out_trade_no, new Date(), MerchantDetail.OFFLINESOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
						tradeRecordService.insertToTrade(user.getId(), manid, offlineCard.getUid(), out_trade_no, offlineCard.getChargemoney(), 
								traderecord.getMermoney(),traderecord.getManmoney(), offlineCard.getEquipmentnum(), 3, 2, 2,
								equipmentService.getEquipmentById(offlineCard.getEquipmentnum()).getHardversion());
					}
				}
				String phone = getTempPhone(offlineCard.getEquipmentnum(), offlineCard.getMerchantid(), 1);
				String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=2&id="+offlineCard.getId();
				returnMsgTemp(offlineCard.getUid(), phone, offlineCard.getEquipmentnum(), offlineCard.getOrdernum(), urltem, StringUtil.toDateTime(), offlineCard.getChargemoney());
			} else if (refundState == 3) {
				InCoins incoins = inCoinsService.selectInCoinsRecordById(id);
//				inCoinsService.updateInCoinsStatusAndRecycletype(incoins.getOrdernum(), (byte) 4);
				int wolfkey = (int) (map.get("wolfkey") == null ? 0 : map.get("wolfkey"));
				if (wolfkey == 3) {
					inCoinsService.updateInCoinsStatus(out_trade_no, (byte) 9);
				} else {
					inCoinsService.updateInCoinsStatus(out_trade_no, (byte) 4);
				}
				if (incoins.getHandletype() != 4 && incoins.getHandletype() != 9) {
					TradeRecord traderecord = tradeRecordService.getTraderecord(incoins.getOrdernum());
					Integer manid = traderecord.getManid();
					User user = userService.selectUserById(incoins.getMerchantid());
					if( manid == null || manid == 0){
						// 微信特约商户的资金不变动
						if(user.getSubMer().equals(0)){
							userService.updateUserEarnings(0, incoins.getMoney(), user.getId());
							user.setEarnings((user.getEarnings() * 100 - incoins.getMoney() * 100) / 100);
//							userService.updateUserById(user);
						}
						merchantDetailService.insertMerEarningDetail(user.getId(), incoins.getMoney(), user.getEarnings(), 
								out_trade_no, new Date(), MerchantDetail.INCOINSSOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
						Equipment equipmentById = equipmentService.getEquipmentById(incoins.getEquipmentnum());
						if (wolfkey == 3) {
							tradeRecordService.insertTrade(user.getId(), incoins.getUid(), out_trade_no, incoins.getMoney(), incoins.getMoney(),
									incoins.getEquipmentnum(), 2, 4, 2, equipmentById.getHardversion());
						} else {
							tradeRecordService.insertTrade(user.getId(), incoins.getUid(), out_trade_no, incoins.getMoney(), incoins.getMoney(),
									incoins.getEquipmentnum(), 2, 2, 2, equipmentById.getHardversion());
						}
					}else{
						User manuser = userService.selectUserById(manid);
						userService.updateUserEarnings(0, traderecord.getMermoney(), user.getId());
						userService.updateUserEarnings(0, traderecord.getManmoney(), manid);
						manuser.setEarnings((manuser.getEarnings() * 100 - traderecord.getManmoney() * 100) / 100);
//						userService.updateUserById(manuser);
//						if(user.getId()==manuser.getId()) user = userService.selectUserById(incoins.getMerchantid());
						user.setEarnings((user.getEarnings() * 100 - traderecord.getMermoney() * 100) / 100);
//						userService.updateUserById(user);
						merchantDetailService.insertMerEarningDetail(user.getId(), traderecord.getMermoney(), user.getEarnings(), 
								out_trade_no, new Date(), MerchantDetail.INCOINSSOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
						merchantDetailService.insertMerEarningDetail(manuser.getId(), traderecord.getManmoney(), manuser.getEarnings(), 
								out_trade_no, new Date(), MerchantDetail.INCOINSSOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
						Equipment equipmentById = equipmentService.getEquipmentById(incoins.getEquipmentnum());
						if (wolfkey == 3) {
							tradeRecordService.insertToTrade(user.getId(), manid, incoins.getUid(), out_trade_no, incoins.getMoney(), 
									traderecord.getMermoney(),traderecord.getManmoney(), incoins.getEquipmentnum(), 2, 4, 2, equipmentById.getHardversion());
						} else {
							tradeRecordService.insertToTrade(user.getId(), manid, incoins.getUid(), out_trade_no, incoins.getMoney(), 
									traderecord.getMermoney(),traderecord.getManmoney(), incoins.getEquipmentnum(), 2, 2, 2, equipmentById.getHardversion());
						}
					}
				}
				String phone = getTempPhone(incoins.getEquipmentnum(), incoins.getMerchantid(), 2);
				String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=3&id="+incoins.getId();
				returnMsgTemp(incoins.getUid(), phone, incoins.getEquipmentnum(), incoins.getOrdernum(), urltem, StringUtil.toDateTime(), incoins.getMoney());
			} else if (refundState == 4) {
				// 通过id查询钱包充值的记录
				Money money = moneyService.payMoneyinfo(id);
				// 更改订单的状态
				moneyService.updateMoneyByOrder(money.getOrdernum(), 2);
				// 查询用户
				User user = userService.selectUserById(money.getUid());
				// 用户支付的钱
				Double paymoney = CommUtil.toDouble(money.getMoney());
				// 赠送的金额
				Double sendmoney = CommUtil.toDouble(money.getSendmoney());
				Double tomoney = CommUtil.toDouble(money.getTomoney());
				// 商家的id
				Integer merid = CommUtil.toInteger(user.getMerid());
				// 用户的余额
				Double userbalance = CommUtil.toDouble(user.getBalance());
				// 赠送的钱
				Double usersendmoney = CommUtil.toDouble(user.getSendmoney());
				// 退款
				userbalance = CommUtil.addBig(userbalance, paymoney);
				usersendmoney = CommUtil.addBig(usersendmoney, sendmoney);
				if(userbalance<0) userbalance = 0.00;
				if(usersendmoney<0) usersendmoney = 0.00;
				// 余额,到账前的金额,赠送的金额
				Double balance = CommUtil.addBig(userbalance, usersendmoney);
				Double opertomoney = CommUtil.addBig(paymoney, sendmoney);
				Double topupbalance = userbalance;
				Double givebalance = usersendmoney;
//				Double walletmo = (user.getBalance()*100-money.getMoney()*100)/100;
//				if(walletmo<0) walletmo = 0.00;
//				user.setBalance(walletmo);
				user.setBalance(userbalance);
				user.setSendmoney(usersendmoney);
				// 变更用户的钱包余额
				userService.updateUserById(user);
				if (money.getPaytype() != 2) {
//					TradeRecord traderecord = tradeRecordService.getTraderecord(money.getOrdernum());
					List<TradeRecord> tradelist = tradeRecordService.getTraderecordList(out_trade_no, 1);
					TradeRecord traderecord = new TradeRecord();
					if(tradelist.size()>=1){
						traderecord = tradelist.get(0);
					}
					Integer manid = traderecord.getManid();
					// 查询商家信息
					User meruser = userService.selectUserById(user.getMerid());
					// 插入钱包明细
					generalDetailService.insertGenWalletDetail(user.getId(), merid, paymoney, sendmoney, opertomoney, balance, topupbalance, givebalance, money.getOrdernum(), new Date(), 6);
					// 开始处理不分成商家的数据
					if( null == manid || manid == 0){
						// 微信特约商户资金不用变动
						if(meruser.getSubMer().equals(0)){
							userService.updateUserEarnings(0, money.getMoney(), meruser.getId());
							meruser.setEarnings((meruser.getEarnings() * 100 - money.getMoney() * 100) / 100);
//							userService.updateUserById(meruser);
						}
						// 记录资金变动明细
						merchantDetailService.insertMerEarningDetail(meruser.getId(), money.getMoney(), meruser.getEarnings(), 
								out_trade_no, new Date(), MerchantDetail.WALLETSOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
						// 记录交易信息
						tradeRecordService.insertTrade(meruser.getId(), user.getId(), out_trade_no, money.getMoney(), money.getMoney(),
								"钱包", 4, 2, 2, "00");
						System.out.println("==================钱包退费分成记录结束");
					}else{
						User manuser = userService.selectUserById(manid);
						userService.updateUserEarnings(0, traderecord.getMermoney(), meruser.getId());
						userService.updateUserEarnings(0, traderecord.getManmoney(), manid);
						manuser.setEarnings((manuser.getEarnings() * 100 - traderecord.getManmoney() * 100) / 100);
//						userService.updateUserById(manuser);
//						if(user.getId()==manuser.getId()) meruser = userService.selectUserById(user.getMerid());
						meruser.setEarnings((meruser.getEarnings() * 100 - traderecord.getMermoney() * 100) / 100);
//						userService.updateUserById(meruser);
						merchantDetailService.insertMerEarningDetail(meruser.getId(), traderecord.getMermoney(), meruser.getEarnings(), 
								out_trade_no, new Date(), MerchantDetail.WALLETSOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
						merchantDetailService.insertMerEarningDetail(manuser.getId(), traderecord.getManmoney(), manuser.getEarnings(), 
								out_trade_no, new Date(), MerchantDetail.WALLETSOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
						tradeRecordService.insertToTrade(meruser.getId(), manid, money.getUid(), out_trade_no, money.getMoney(), 
								traderecord.getMermoney(),traderecord.getManmoney(), "钱包", 4, 2, 2, "00");
						System.out.println("==================钱包退费分成记录结束");
					}
				}
				System.out.println("发送消息");
				String phone = getTempPhone(CommUtil.toString(user.getAid()), user.getMerid(), 3);
				String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=4&id="+money.getId();
				TempMsgUtil.returnMsg("钱包退费", money.getUid(), phone, money.getOrdernum(), urltem, StringUtil.toDateTime(), money.getMoney());
				System.out.println("发送消息完成");
			} else if (refundState == 5) {
				// 根据订单id查询
				OnlineCardRecord cardRecord = onlineCardRecordService.selectOnlineCard(id);
				// 根据在线卡id查询
				OnlineCard oncard = onlineCardService.selectOnlineCardByCardID(cardRecord.getCardID());
				// 在线卡的钱减去充值的钱
				double mone = oncard.getMoney()-cardRecord.getAccountmoney();
				oncard.setMoney(mone);
				// 更新卡里的钱
				onlineCardService.updateOnlineCardBycard(oncard);
				System.out.println("在线卡退费");
				// 卡绑定商家
				if (oncard.getMerid() != null && oncard.getMerid() != 0) {
					// 卡的状态是激活,已付款
					if (oncard.getStatus() != 0 && cardRecord.getType() != 5) {
						// 订单号查询
						TradeRecord traderecord = tradeRecordService.getTraderecord(cardRecord.getOrdernum());
						// 合伙人id
						Integer manid = traderecord.getManid();
						// 商家信息
						User user = userService.selectUserById(cardRecord.getMerid());
						if( null == manid || manid == 0){
							System.out.println("在线卡退费不分成");
							// 特约商户不需要变更商家的收益
							if(user.getSubMer().equals(0)){
								// 更新商家的收益
								user.setEarnings((user.getEarnings() * 100 - cardRecord.getMoney() * 100) / 100);
								userService.updateUserById(user);
								// 插入商家资金变动明细
							}
							merchantDetailService.insertMerEarningDetail(user.getId(), cardRecord.getMoney(), user.getEarnings(), 
									out_trade_no, new Date(), MerchantDetail.ONLINESOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
							// 插入交易信息
							tradeRecordService.insertTrade(user.getId(), cardRecord.getUid(), out_trade_no, cardRecord.getMoney(), cardRecord.getMoney(),
									traderecord.getCode(), 5, 2, 2, null);
						}else{
							System.out.println("在线卡退费分成");
							User manuser = userService.selectUserById(manid);
							manuser.setEarnings((manuser.getEarnings() * 100 - traderecord.getManmoney() * 100) / 100);
							userService.updateUserById(manuser);
							if(user.getId()==manuser.getId()) user = userService.selectUserById(traderecord.getMerid());
							user.setEarnings((user.getEarnings() * 100 - traderecord.getMermoney() * 100) / 100);
							userService.updateUserById(user);
							merchantDetailService.insertMerEarningDetail(user.getId(), traderecord.getMermoney(), user.getEarnings(), 
									out_trade_no, new Date(), MerchantDetail.ONLINESOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
							merchantDetailService.insertMerEarningDetail(manuser.getId(), traderecord.getManmoney(), manuser.getEarnings(), 
									out_trade_no, new Date(), MerchantDetail.ONLINESOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
							tradeRecordService.insertToTrade(user.getId(), manid, traderecord.getUid(), out_trade_no, cardRecord.getMoney(), 
									traderecord.getMermoney(),traderecord.getManmoney(), traderecord.getCode(), 5, 2, 2, null);
						}
					}
				}
				cardRecord.setFlag(2);
				onlineCardRecordService.updateRecord(cardRecord);
				cardRecord.setId(null);
				cardRecord.setType(5);
				cardRecord.setBalance(mone);
				cardRecord.setCreateTime(new Date());
				
				onlineCardRecordService.additionOnlineCardRecord(cardRecord);
				System.out.println("在线卡退费发送消息完成");
				String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=5&id="+cardRecord.getId();
				String phone = getTempPhone(cardRecord.getCardID(), cardRecord.getMerid(), 4);
				TempMsgUtil.returnMsg("在线卡退费",cardRecord.getUid(), phone, cardRecord.getOrdernum(), urltem, StringUtil.toDateTime(), cardRecord.getMoney());
			}else if (refundState == 6){
				// 根据充值订单查询包月记录信息
				PackageMonthRecord monthRecord = userService.selectMonthRecordById(id);
				// 用户id
				Integer uid = monthRecord.getUid();
				// 根据用户id查询
				PackageMonth packageMonth = userService.selectPackageMonthByUid(uid);
				Integer merid = monthRecord.getMerid();
				Double money = monthRecord.getMoney();
				Integer changenum = monthRecord.getChangenum();
				Integer surpnum = packageMonth.getSurpnum();
				Date endTime = packageMonth.getEndTime();
				Date date = new Date();
				//添加交易记录
				tradeRecordService.insertToTrade(merid, 0, uid, monthRecord.getOrdernum(), money, 
						money,0.0, "0", 6, 2, 2, null);
				//添加包月退款记录
				int endsurpnum = surpnum-changenum;
				if (endsurpnum < 0) {
					endsurpnum = 0;
				}
				userService.insertPackageMonthRecord(uid, merid, monthRecord.getOrdernum(), money, 1, 2, monthRecord.getEverydaynum(),
						changenum, endsurpnum, monthRecord.getChangemonth(), monthRecord.getTime(),
						monthRecord.getElec(), date);
				//修改包月充值记录，标记为已退款
				userService.updatePackageMonthRecord(id, 1, null, null);
				PackageMonth packageMonth2 = new PackageMonth();
				packageMonth2.setUid(uid);
				packageMonth2.setSurpnum(endsurpnum);
				packageMonth2.setEndTime(endTime);
				packageMonth2.setMonthnum(0-monthRecord.getChangemonth());
				//修改包月信息，根据退款信息来修改
				userService.updatePackageMonthByEntity(packageMonth2);
				//修改商户余额
				User user = userService.selectUserById(merid);
				// 微信特约商户不用更改收益信息
				if(user.getSubMer().equals(0)){
					userService.updateUserEarnings(0, money, merid);
				}
				//添加商户余额变更记录
				merchantDetailService.insertMerEarningDetail(merid, money, user.getEarnings()-money, monthRecord.getOrdernum(), 
						date, 7, 2, 2);
				try {
					userService.updateMerAmount(0, money, merid);
				} catch (Exception e) {
					logger.warn("商户总计更改出错");
				}
			}
			map.put("ok", "ok");
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("退款失败");
			map.put("ok", "error");
		}
	}
	
	/**
	 * @Description： 充电退费处理数据
	 */
	public void chargeRefund(Integer id, String out_trade_no, Map<String, Object> map){
		ChargeRecord chargeRecord = chargeRecordService.chargeRecordOne(id);
		String ordernum = CommUtil.toString(out_trade_no);
		//修改本地数据
		String devicenum = CommUtil.toString(chargeRecord.getEquipmentnum());
		String port = CommUtil.toString(chargeRecord.getPort());
		Integer uid = CommUtil.toInteger(chargeRecord.getUid());
		Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
		Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
		Double refundMoney = paymoney;//全额退款退款金额等于付款金额
		Double consumemoney =  CommUtil.subBig(paymoney, refundMoney);//消费金额
		Integer orderid = CommUtil.toInteger(chargeRecord.getId());
		Integer paysource = MerchantDetail.CHARGESOURCE;
		Integer paytype = MerchantDetail.WEIXINPAY;
		Integer paystatus = MerchantDetail.REFUND;
		
		Integer resultinfo = CommUtil.toInteger(chargeRecord.getResultinfo());
		String startTime = CommUtil.toDateTime(chargeRecord.getBegintime());
		String endTime = CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date());
		String strtime = "开始时间："+startTime + "\r\n结束时间：" + endTime;
		
		Equipment devicedata = equipmentService.getEquipmentAndAreaById(devicenum);
		Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
		Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
		String devicename = CommUtil.toString(devicedata.getRemark());
		String areaaddress = CommUtil.toString(devicedata.getAddress());
		systemSetService.sendMmessChargeEnd(ordernum, orderid, resultinfo, paysource, merid, uid, consumemoney, strtime,
				devicenum, port, devicename, deviceaid, areaaddress, devicetempid);
		if (chargeRecord.getEndtime() == null) {
			chargeRecordService.updateNumberById(null, 1, orderid, null, null, paymoney, CommUtil.toDateTime(), CommUtil.toDateTime());
		} else {
			chargeRecordService.updateNumberById(null, 1, orderid, null, null, paymoney, null, CommUtil.toDateTime());
		}
		if (chargeRecord.getNumber() != 1) {
			/*if (chargeRecord.getNumber() == 2) {//部分退款修改用户数据
				Double refundmoney = CommUtil.toDouble(chargeRecord.getRefundMoney());
				User tourist = userService.selectUserById(uid);
				userService.editUserBalance(refundmoney, 2, uid);
				Double  balance = CommUtil.subBig(CommUtil.toDouble(tourist.getBalance()), refundmoney);
				generalDetailService.insertGenWalletDetail(uid, merid, refundmoney, balance, ordernum, new Date(), 6);
			}*/
			List<TradeRecord> tradelist = tradeRecordService.getTraderecordList(ordernum, 1);
			if(tradelist.size()>=1){
				for(TradeRecord item : tradelist){
					dealerDataDispose(orderid, devicenum, merid, paysource, paytype, paystatus, item);
				}
			}/*else{
				traderecord = tradeRecordService.getTraderecord(ordernum);
				dealerDataDispose(orderid, devicenum, merid, paysource, paytype, paystatus, traderecord);
			}*/
		}
	}
	
	/**
	 * @Description： 离线充值机退费处理数据
	 */
	public void offlineCardRefund(Integer id, String out_trade_no, Map<String, Object> map){
		OfflineCard offlineCard = offlineCardService.selectOfflineCardById(id);
		offlineCardService.offlineCardRefund(3, id);
		//修改本地数据
		String ordernum = CommUtil.toString(out_trade_no);
		String devicenum = CommUtil.toString(offlineCard.getEquipmentnum());
		Integer merid = CommUtil.toInteger(offlineCard.getMerchantid());
		Integer orderid = CommUtil.toInteger(offlineCard.getId());
		Integer paysource = MerchantDetail.OFFLINESOURCE;
		Integer paytype = MerchantDetail.WEIXINPAY;
		Integer paystatus = MerchantDetail.REFUND;
		if (offlineCard.getPaytype() != 3) {
			List<TradeRecord> tradelist = tradeRecordService.getTraderecordList(ordernum, 1);
			TradeRecord traderecord = new TradeRecord();
			if(tradelist.size()>1){
				for(TradeRecord item : tradelist){
					dealerDataDispose(orderid, devicenum, merid, paysource, paytype, paystatus, item);
				}
			}else{
				traderecord = tradeRecordService.getTraderecord(ordernum);
				dealerDataDispose(orderid, devicenum, merid, paysource, paytype, paystatus, traderecord);
			}
		}
	}
	
	/**
	 * @Description： 脉冲退费处理数据
	 */
	public void inCoinsRefund(Integer id, String out_trade_no, Map<String, Object> map){
		InCoins incoins = inCoinsService.selectInCoinsRecordById(id);
		//修改本地数据
		String ordernum = CommUtil.toString(out_trade_no);
		String devicenum = CommUtil.toString(incoins.getEquipmentnum());
		Integer merid = CommUtil.toInteger(incoins.getMerchantid());
		Integer orderid = CommUtil.toInteger(incoins.getId());
		Integer paysource = MerchantDetail.INCOINSSOURCE;
		Integer paytype = MerchantDetail.WEIXINPAY;
		Integer paystatus = MerchantDetail.REFUND;
		Integer wolfkey = CommUtil.toInteger(map.get("wolfkey"));
		if (wolfkey == 3) {
			inCoinsService.updateInCoinsStatus(ordernum, (byte) 9);
		} else {
			inCoinsService.updateInCoinsStatus(ordernum, (byte) 4);
		}
		if (incoins.getHandletype() != 4 && incoins.getHandletype() != 9) {
			List<TradeRecord> tradelist = tradeRecordService.getTraderecordList(ordernum, 1);
			TradeRecord traderecord = new TradeRecord();
			if(tradelist.size()>1){
				for(TradeRecord item : tradelist){
					dealerDataDispose(orderid, devicenum, merid, paysource, paytype, paystatus, item);
				}
			}else{
				traderecord = tradeRecordService.getTraderecord(ordernum);
				dealerDataDispose(orderid, devicenum, merid, paysource, paytype, paystatus, traderecord);
			}
		}
	}
	
	/**
	 * 钱包充值退款
	 * @param refundState
	 * @param id
	 * @param out_trade_no
	 * @param map
	 */
	public void walletRefund(Integer id, String out_trade_no, Map<String, Object> map){
		Money money = moneyService.payMoneyinfo(id);
		//修改本地数据
		String ordernum = CommUtil.toString(out_trade_no);
		Integer uid = CommUtil.toInteger(money.getUid());
		Double paymoney = CommUtil.toDouble(money.getMoney());
		Double sendmoney = CommUtil.toDouble(money.getSendmoney());
		Double tomoney = CommUtil.toDouble(money.getTomoney());
		Double moneytopup = CommUtil.toDouble(money.getTopupbalance());
		Double moneygive = CommUtil.toDouble(money.getGivebalance());
		Double moneybalance = CommUtil.toDouble(money.getBalance());
		System.out.println("钱包退款");
		Integer orderid = CommUtil.toInteger(money.getId());
		Date time = new Date();
		String strtime = CommUtil.toDateTime(time);
		Integer paysource = MerchantDetail.WALLETSOURCE;
		Integer paytype = MerchantDetail.WEIXINPAY;
		Integer paystatus = MerchantDetail.REFUND;
		Integer type = 0;
		Double mermoney = 0.00;
		String first = "";
		String remark = "";
		String status = "退款成功！。";
		moneyService.updateMoneyByOrder(ordernum, 2);
		//修改用户钱包金额
		User user = userService.selectUserById(uid);
		Integer merid = CommUtil.toInteger(user.getMerid());
		Double userbalance = CommUtil.toDouble(user.getBalance());
		Double usersendmoney = CommUtil.toDouble(user.getSendmoney());
		
		if(sendmoney.equals(0) && moneytopup.equals(0) && moneygive.equals(0)){//未分离赠送金额
			sendmoney = CommUtil.subBig(tomoney, paymoney);
			userbalance = CommUtil.subBig(userbalance, tomoney);
			usersendmoney = CommUtil.toDouble(sendmoney);
		}else{
			userbalance = CommUtil.subBig(userbalance, paymoney);
			usersendmoney = CommUtil.subBig(usersendmoney, sendmoney);
		}
		if(userbalance<0) userbalance = 0.00;
		if(usersendmoney<0) usersendmoney = 0.00;
		Double opertomoney = CommUtil.addBig(paymoney, sendmoney);
		Double topupbalance = userbalance;
		Double givebalance = usersendmoney;
		Double balance = CommUtil.addBig(userbalance, usersendmoney);
//		userbalance = CommUtil.subBig(userbalance, paymoney);
//		usersendmoney = CommUtil.subBig(usersendmoney, sendmoney);
//		if(userbalance<0) userbalance = 0.00;
//		if(usersendmoney<0) usersendmoney = 0.00;
//		Double opertomoney = CommUtil.addBig(paymoney, sendmoney);
//		Double topupbalance = userbalance;
//		Double givebalance = usersendmoney;
//		Double balance = CommUtil.addBig(userbalance, usersendmoney);
//		Double walletmo = (user.getBalance()*100-money.getTomoney()*100)/100;
//		if(walletmo<0) walletmo = 0.00;
//		user.setBalance(walletmo);
		user.setBalance(userbalance);
		user.setSendmoney(usersendmoney);
		userService.updateUserById(user);
//		(Integer uid,Integer merid,Double paymoney, Double sendmoney, Double balance,String ordernum,Date createTime,Integer paysource);
		generalDetailService.insertGenWalletDetail(uid, merid, paymoney, sendmoney, opertomoney, balance, topupbalance, givebalance, ordernum, time, 6);
		Integer operid = 0;
		if (money.getPaytype() != 2) {
			List<TradeRecord> tradelist = tradeRecordService.getTraderecordList(out_trade_no, 1);
//			TradeRecord traderecord = tradeRecordService.getTraderecord(ordernum);
			TradeRecord traderecord = new TradeRecord();
			if(tradelist.size()>=1){
				traderecord = tradelist.get(0);
			}
			String comment = traderecord.getComment();
			try {
				Integer aid = CommUtil.toInteger(user.getAid());
				if (aid != null && aid != 0) {
					areaService.updateAreaEarn( 0, paymoney, aid, paymoney, null, null);
				}
				// 判断用户是不是特约商户
				User subMer = userService.selectUserById(merid);
				if(subMer != null && subMer.getSubMer().equals(0)){
					// 钱包充值退款数据
					dealerIncomeRefund(comment, merid, paymoney, ordernum, time, paysource, paytype, paystatus);
				}else{
					// 记录特约商户的数据
					userService.updateMerAmount(0, paymoney, merid);//商户收益总额  1为加  0为减
					merchantDetailService.insertMerEarningDetail(merid, paymoney, subMer.getEarnings(), ordernum, time, paysource, paytype, paystatus);
				}
			} catch (Exception e) {
				logger.warn("小区修改余额错误===" + e.getMessage());
			}
			mermoney = traderecord.getMermoney();
			Double partmoney = traderecord.getManmoney();
			Integer manid = partmoney == 0 ? 0 : -1;
			operid = traderecord.getUid();
			tradeRecordService.insertToTrade(merid, manid, operid, ordernum, paymoney, mermoney, partmoney, "钱包", 4, 2, 2, traderecord.getHardver(), comment);
		}
		System.out.println("发送消息");
//		String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=4&id="+money.getId();
//		TempMsgUtil.returnMsg("钱包退费", money.getUid(), user.getMerid(), money.getOrdernum(), urltem, StringUtil.toDateTime(), money.getMoney());
		if(operid==0) operid = uid;
		String phone = getTempPhone(CommUtil.toString(user.getAid()), user.getMerid(), 3);
		String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=4&id="+money.getId();
		TempMsgUtil.returnMsg("钱包退费", operid, phone, money.getOrdernum(), urltem, StringUtil.toDateTime(), money.getMoney());
		System.out.println("发送消息完成");
	}
	
	/**
	 * 在线卡充值退款
	 * @param refundState
	 * @param id
	 * @param out_trade_no
	 * @param map
	 */
	public void onlinecardRefund(Integer id, String out_trade_no, Map<String, Object> map){
		try{
			OnlineCardRecord cardRecord = onlineCardRecordService.selectOnlineCard(id);
			//修改本地数据
			String ordernum = CommUtil.toString(out_trade_no);
			Integer uid = CommUtil.toInteger(cardRecord.getUid());
			Integer merid = CommUtil.toInteger(cardRecord.getMerid());
			Integer orderid = CommUtil.toInteger(cardRecord.getId());
			String devicenum = CommUtil.toString(cardRecord.getCode());
			String cardID = CommUtil.toString(cardRecord.getCardID());
			
			
			Double paymoney = CommUtil.toDouble(cardRecord.getMoney());
			Double accountmoney = CommUtil.toDouble(cardRecord.getAccountmoney());
			Double sendmoney = CommUtil.subBig(accountmoney, paymoney);
//			Double sendmoney = CommUtil.toDouble(cardRecord.getSendmoney());
			Date time = new Date();
			String strtime = CommUtil.toDateTime(time);
			Integer paysource = MerchantDetail.ONLINESOURCE;
			Integer paytype = MerchantDetail.WEIXINPAY;
			Integer paystatus = MerchantDetail.REFUND;
			Integer type = 0;
			Double mermoney = 0.00;
			//处理在线卡信息
			OnlineCard oncard = onlineCardService.selectOnlineCardByCardID(cardID);
			Double cardamoney = CommUtil.toDouble(oncard.getMoney());
			Double cardsendmoney = CommUtil.toDouble(oncard.getSendmoney());
			Double topupbalance = 0.00;
			Double givebalance = 0.00;
			if(sendmoney.equals(0.00) && topupbalance.equals(0.00) && givebalance.equals(0.00)){//未分离赠送金额
				sendmoney = CommUtil.subBig(accountmoney, paymoney);
				topupbalance = CommUtil.subBig(cardamoney, accountmoney);
				givebalance = CommUtil.toDouble(sendmoney);
			}else{
				topupbalance = CommUtil.subBig(cardamoney, paymoney);
				givebalance = CommUtil.subBig(cardsendmoney, sendmoney);
			}
			Double cardbalance = CommUtil.addBig(topupbalance, givebalance);
			Integer aid = CommUtil.toInteger(oncard.getAid());
			oncard.setMoney(topupbalance);
			oncard.setSendmoney(givebalance);
			onlineCardService.updateOnlineCardBycard(oncard);
			
			if (oncard.getMerid() != null && oncard.getMerid() != 0) {
				if (oncard.getStatus()!=0 && cardRecord.getType() != 5) {
					TradeRecord traderecord = tradeRecordService.getTraderecord(ordernum);
					String comment = traderecord.getComment();
					try {
						if (aid != null && aid != 0) {
							areaService.updateAreaEarn(0, paymoney, aid, null, null, paymoney);
						}
						// 根据商户的id查询商户信息
						User subMer = userService.selectUserById(merid);
						// 判断商户是不是特约商户
						if(subMer != null && subMer.getSubMer().equals(0)){
							// 在线卡充值退款数据
							dealerIncomeRefund(comment, merid, paymoney, ordernum, time, paysource, paytype, paystatus);
						}else{
							userService.updateMerAmount(0, paymoney, merid);//商户收益总额  1为加  0为减
							merchantDetailService.insertMerEarningDetail(merid, paymoney, subMer.getEarnings(), ordernum, time, paysource, paytype, paystatus);
						}
						mermoney = traderecord.getMermoney();
						Double partmoney = traderecord.getManmoney();
						Integer manid = partmoney == 0 ? 0 : -1;
						tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, traderecord.getCode(), 5, 2, 2, null, comment);
					} catch (Exception e) {
						logger.warn("小区修改余额错误===" + e.getMessage());
					}
				}
			}
			cardRecord.setFlag(2);
			onlineCardRecordService.updateRecord(cardRecord);
			cardRecord.setId(null);
			cardRecord.setType(5);
			cardRecord.setTopupbalance(topupbalance);
			cardRecord.setGivebalance(givebalance);
			cardRecord.setBalance(cardbalance);
			cardRecord.setCreateTime(new Date());
			onlineCardRecordService.additionOnlineCardRecord(cardRecord);
//			String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=5&id="+cardRecord.getId();
//			TempMsgUtil.returnMsg("在线卡退费",cardRecord.getUid(), cardRecord.getMerid(), cardRecord.getOrdernum(), urltem, StringUtil.toDateTime(), cardRecord.getMoney());
			String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=5&id="+cardRecord.getId();
			String phone = getTempPhone(cardRecord.getCardID(), cardRecord.getMerid(), 4);
			TempMsgUtil.returnMsg("在线卡退费",cardRecord.getUid(), phone, cardRecord.getOrdernum(), urltem, StringUtil.toDateTime(), cardRecord.getMoney());
			
			map.put("ok", "ok");
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("退款失败");
			map.put("ok", "error");
		}
	}
	//===============================商家微信设备缴费============================
	/**
	 * 展示商家小区下的设备
	 * @param areaId 小区id
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/merShowAreaDevice")
	public String merShowAreaDevice(Integer areaId,Model model){
		User user=(User)request.getSession().getAttribute("user");
		if(user==null) return "erroruser";
		
		//商家的收费标准
		List<Map<String,Object>> merList=feescaleService.getMerFeescale(user.getId());
		DealerAuthority message = authorityService.selectMessSwitch(user.getId());
		//获取小区信息
		Area area = areaService.selectByIdArea(areaId);
		//小区下的合伙人
		List<Map<String, Object>> result = areaService.selectAreaRelev(areaId);
		List<Object> listscanpa = new ArrayList<>();
		for(Map<String, Object> item : result){
			Integer type = CommUtil.toInteger(item.get("type"));
			if(type==2){
				listscanpa.add(item);
			}
		}
		//小区合下的所有设备
		List<Equipment> equlistByAid = equipmentService.selectAreaEqulistOrderyByExpire(areaId);
		List<Map<String,Object>> equiresult = new ArrayList<Map<String,Object>>();
		String nowtime = CommUtil.toDateTime("yyyy-MM-dd", new Date());
		for(Equipment item : equlistByAid){
			Date expiration = item.getExpirationTime();
			String expiratime = CommUtil.toDateTime("yyyy-MM-dd", expiration);
			Integer differnum = CommUtil.compareTime(expiratime, nowtime);
			Map<String,Object> mapdata = JSON.parseObject(JSON.toJSONString(item), Map.class);
			mapdata.put("differnum", differnum);
			mapdata.put("expiratimes", expiratime);
			equiresult.add(mapdata);
		}
		model.addAttribute("net", merList.get(0));//网络
		model.addAttribute("blue", merList.get(1));//蓝牙
		model.addAttribute("area", area);//小区
		model.addAttribute("merid", user.getId());//商家id
		model.addAttribute("mername",user.getUsername());//商家真实姓名
		model.addAttribute("mer",user);//商户的信息
		model.addAttribute("listscanpa", result);//合伙人
		model.addAttribute("equlistByAid", equlistByAid);//合伙的设备
		model.addAttribute("equiresult", equiresult);//合伙的设备
		model.addAttribute("authority", message);
		return "merchant/merShowAreaDevice";
	}
	/**
	 * 商家进首页
	 * 展示未绑定小区设备和
	 * 和绑定小区信息
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/merShowDeviceAndDervice")
	public String merShowDeviceAndAreaDervice(Model model){
		User user=(User)request.getSession().getAttribute("user");
		if(user !=null && user.getId() != null){
			List<Map<String,Object>> merList=feescaleService.getMerFeescale(user.getId());
			//获取商家的小区信息
			Area area = new Area();
			area.setMerid(user.getId());
			List<Area> areas  =  feescaleService.getAllAreaAndPartner(area);
			//商家未绑定小区的设备
			List<Equipment> unbindEquipments = feescaleService.getUnbindEquipment(user.getId());
			List<Map<String,Object>> equiresult = new ArrayList<Map<String,Object>>();
			String nowtime = CommUtil.toDateTime("yyyy-MM-dd", new Date());
			for(Equipment item : unbindEquipments){
				Date expiration = item.getExpirationTime();
				String expiratime = CommUtil.toDateTime("yyyy-MM-dd", expiration);
				Integer differnum = CommUtil.compareTime(expiratime, nowtime);
				Map<String,Object> mapdata = JSON.parseObject(JSON.toJSONString(item), Map.class);
				mapdata.put("differnum", differnum);
				mapdata.put("expiratimes", expiratime);
				equiresult.add(mapdata);
			}
			model.addAttribute("equiresult", equiresult);
			model.addAttribute("mer", user);
			model.addAttribute("net", merList.get(0));
			model.addAttribute("blue", merList.get(1));
			model.addAttribute("areaInfo",areas);
			model.addAttribute("device", unbindEquipments);
			return "merchant/merShowDevice";
		}else{
			return "erroruser";
		}
	}

	/**
	 * 商家通过设备提示的信息超链接到设备缴费
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/equipmentExpireRemindLink")
	public String equipmentExpireRemindLink(Model model,Integer id){
		if(id !=null){
			//用户
			User user = userService.selectUserById(id);
			request.getSession().setAttribute("user", user);
			List<Map<String,Object>> merList=feescaleService.getMerFeescale(id);
			//获取商家的小区信息
			Area area = new Area();
			area.setMerid(id);
			List<Area> areas  =  feescaleService.getAllAreaAndPartner(area);
			//商家未绑定小区的设备
			List<Equipment> unbindEquipments = feescaleService.getUnbindEquipment(id);
			List<Map<String,Object>> equiresult = new ArrayList<Map<String,Object>>();
			String nowtime = CommUtil.toDateTime("yyyy-MM-dd", new Date());
			for(Equipment item : unbindEquipments){
				Date expiration = item.getExpirationTime();
				String expiratime = CommUtil.toDateTime("yyyy-MM-dd", expiration);
				Integer differnum = CommUtil.compareTime(expiratime, nowtime);
				Map<String,Object> mapdata = JSON.parseObject(JSON.toJSONString(item), Map.class);
				mapdata.put("differnum", differnum);
				mapdata.put("expiratimes", expiratime);
				equiresult.add(mapdata);
			}
			model.addAttribute("equiresult", equiresult);
			model.addAttribute("mer", user);
			model.addAttribute("net", merList.get(0));
			model.addAttribute("blue",merList.get(1));
			model.addAttribute("areaInfo",areas);
			model.addAttribute("device", unbindEquipments);
			return "merchant/merShowDevice";
		}else{
			return "erroruser";
		}
	}
	//===============================商家微信设备缴费============================
	
	public void packageMonthRefund(Integer id,String ordernum,Map<String, Object> map) {
		PackageMonthRecord monthRecord = userService.selectMonthRecordById(id);
		Integer uid = monthRecord.getUid();
		PackageMonth packageMonth = userService.selectPackageMonthByUid(uid);
		Integer merid = monthRecord.getMerid();
		Double money = monthRecord.getMoney();
		Integer changenum = monthRecord.getChangenum();
		Integer surpnum = packageMonth.getSurpnum();
		Date endTime = packageMonth.getEndTime();
		Date date = new Date();
		//添加交易记录
		tradeRecordService.insertToTrade(merid, 0, uid, ordernum, money, 
				money,0.0, "0", 6, 2, 2, null);
		//添加包月退款记录
		int endsurpnum = surpnum-changenum;
		if (endsurpnum < 0) {
			endsurpnum = 0;
		}
		userService.insertPackageMonthRecord(uid, merid, ordernum, money, 1, 2, monthRecord.getEverydaynum(),
				changenum, endsurpnum, monthRecord.getChangemonth(), monthRecord.getTime(),
				monthRecord.getElec(), date);
		//修改包月充值记录，标记为已退款
		userService.updatePackageMonthRecord(id, 1, null, null);
		PackageMonth packageMonth2 = new PackageMonth();
		packageMonth2.setUid(uid);
		packageMonth2.setSurpnum(endsurpnum);
		packageMonth2.setEndTime(endTime);
		packageMonth2.setMonthnum(0-monthRecord.getChangemonth());
		//修改包月信息，根据退款信息来修改
		userService.updatePackageMonthByEntity(packageMonth2);
		//普通商户修改商户余额
		User user = userService.selectUserById(merid);
		try {
			if(user != null && user.getSubMer().equals(0)){
				userService.updateUserEarnings(0, money, merid);
			}
			//添加商户余额变更记录
			merchantDetailService.insertMerEarningDetail(merid, money, user.getEarnings()-money, ordernum, 
					date, 7, 2, 2);
			userService.updateMerAmount(0, money, merid);
		} catch (Exception e) {
			logger.warn("商户总计更改出错");
		}
	}
	
	@RequestMapping({ "/wxDoRefund" })
	@ResponseBody
	public Object wxDoRefund(Integer id, Integer refundState) throws Exception {
		Map<String, Object> map = new HashMap<>();
		User userSession = (User) request.getSession().getAttribute("user");
		if (userSession == null) {
			map.put("ok", "error");
		} else {
			String wolfkey = request.getParameter("wolfkey");
			if (wolfkey != null && !"".equals(wolfkey)) {
				map.put("wolfkey", Integer.parseInt(wolfkey));
			}
			dorefund(id, refundState, map);
		}
		return map;
	}
	
	/**
	 * 公众号外部扫码判断用户是否在充电
	 * @param code 设备号
	 * @param openid 用户的openid
	 * @param portchoose 选择的端口
	 * @return {@link Map}
	 */
	@RequestMapping("/checkUserIfCharge")
	@ResponseBody
	public Map<String, String> checkUserIfCharge(String code,String openid,Integer portchoose) {
		User user = userService.getUserByOpenid(openid);
		Map<String, String> map = new HashMap<>();
		if (user != null) {
			ChargeRecord chargeRecord = chargeRecordService.selectChargeEndUidByCodeAndPort(code, portchoose);
			if (chargeRecord != null && chargeRecord.getUid() != null && chargeRecord.getUid() != 0 && chargeRecord.getEndtime() != null) {
				if (chargeRecord.getPaytype() != 1 && chargeRecord.getPaytype() != 2) {
					map.put("ok", "0");
				} else if (chargeRecord.getUid().equals(user.getId()) && chargeRecord.getNumber() == 0 && chargeRecord.getEndtime() == null) {
					long begintime = chargeRecord.getBegintime().getTime();
					int paytime = Integer.parseInt(chargeRecord.getDurationtime()) * 60 * 1000;
					long nowtime = System.currentTimeMillis();
					if ((begintime + paytime) > nowtime) {
						map.put("ok", "1");
						if (chargeRecord.getIfcontinue() != null) {
							map.put("info", chargeRecord.getIfcontinue() + "");
						} else {
							map.put("info", chargeRecord.getId() + "");
						}
					} else {
						map.put("ok", "0");
					}
				} else {
					map.put("ok", "0");
				}
			} else {
				map.put("ok", "0");
			}
		} else {
			map.put("ok", "0");
		}
		return map;
	}

	@RequestMapping("/wxpayaccess")
	public void wxpayaccess(@RequestParam("out_trade_no") String out_trade_no) throws Exception {
		/*
		 * System.out.println("制定单号为：" + out_trade_no); SortedMap<String,
		 * String> params = new TreeMap<>(); params.put("appid",
		 * "wx3debe4a9c562c52a"); params.put("mch_id", "1364987302");
		 * params.put("out_trade_no", out_trade_no); params.put("nonce_str",
		 * HttpRequest.getRandomStringByLength(30)); String sign =
		 * HttpRequest.createSign("UTF-8", params); System.out.println(sign);
		 * params.put("sign", sign); String url =
		 * "https://api.mch.weixin.qq.com/pay/orderquery"; String canshu =
		 * HttpRequest.getRequestXml(params); String sr =
		 * HttpRequest.sendPost(url, canshu); System.out.println(sr);
		 * Map<String, String> map = XMLUtil.doXMLParse(sr); for (String string
		 * : map.keySet()) { System.out.println(string);
		 * System.out.println((String) map.get(string)); if
		 * ("openid".equals(string)) { } }
		 */
		// return "wxpayaccess";
	}

	/**
	 * package wxpay advance order param
	 * 
	 * @param request
	 * @param money2
	 * @param openid
	 * @return
	 */
	public static SortedMap<String, String> packagePayParamsToMap(HttpServletRequest request, Double money2, String openid,
			String notify_url, String format,String body) {
		SortedMap<String, String> params = new TreeMap<>();
		params.put("appid", WeiXinConfigParam.FUWUAPPID);
		params.put("body", "自助充电平台(" + body + ")");
		params.put("mch_id", WeiXinConfigParam.MCHID);
		params.put("sub_appid", WeiXinConfigParam.APPID);
		params.put("sub_mch_id", WeiXinConfigParam.SUBMCHID);
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
	
	/**
	 * 代替子商户唤起支付 ZZ 
	 * @param request
	 * @param money2
	 * @param openid
	 * @param notify_url
	 * @param format
	 * @param body
	 * @return
	 */
	public  SortedMap<String, String> packagePayParamsToMapSub(HttpServletRequest request, Double money2, String openid,
			String notify_url, String format,String body) {
		SortedMap<String, String> params = new TreeMap<>();
		String code = request.getParameter("code");
		// 根据设备号查询商户的微信配置数据
		Map<String, Object> data = userService.selectConfigDataByCode(code);
		logger.info("特约商户付款"+"====="+"设备号"+code);
		// 参数
		String subMchid = CommUtil.toString(data.get("submchid")); 
		String subAppid = CommUtil.toString(data.get("subappid"));
		logger.info("特约商户subMchid"+"===="+subMchid);
		logger.info("特约商户subAppid"+"===="+subAppid);
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

	public static String doubleToString(Double d) {
		String money1 = String.valueOf(d);
		int idx = money1.lastIndexOf(".");
		String total_fee = money1.substring(0, idx);
		return total_fee;
	}
	
	public void returnMsgTemp(Integer uid, String phone, String code, String order, String url, String time, Double money){//退费模板
		try {
			User user = userService.selectUserById(uid);
			if(null!=user){
				String oppenid = user.getOpenid();
				JSONObject json = new JSONObject();
				json.put("first", TempMsgUtil.inforData("您好，您的退费请求已受理，资金将原路返回到您的账户中","#FF3333"));
				json.put("keyword1", TempMsgUtil.inforData("充电退费","#0044BB"));
				json.put("keyword2", TempMsgUtil.inforData(order,"#0044BB"));
				json.put("keyword3", TempMsgUtil.inforData(time,"#0044BB")); 
				json.put("keyword4", TempMsgUtil.inforData("¥ "+String.format("%.2f", money),"#0044BB"));
				json.put("keyword5", TempMsgUtil.inforData(phone,"#0044BB"));
				json.put("remark", TempMsgUtil.inforData("如有疑问可咨询服务商。","#0044BB"));
				TempMsgUtil.sendTempMsg(oppenid, TempMsgUtil.TEMP_IDTUI, url, json);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年9月16日 下午6:03:09 
	 */
	public void messagenotification(String first, Integer uid, Integer merid, Integer paysource, Double money, 
			String strtime, String status, String remark, Integer orderid, String ordernum) {
		try {
			String url = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source="+paysource+"&id="+orderid;
			User user = userService.selectUserById(uid);
			User meruser = userService.selectUserById(merid);
			String oppenid = CommUtil.toString(user.getOpenid());
			String meroppenid = CommUtil.toString(meruser.getOpenid());
			String phone = CommUtil.toString(meruser.getServephone());
			if(phone==null) phone = CommUtil.toString(meruser.getPhoneNum());
			TempMsgUtil.orderCharge( first, oppenid, ordernum, money, status, strtime, phone, remark, url);
		} catch (Exception e) {
			e.printStackTrace();
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
	
//	}
//=========================================================================================================	
//	服务订单通知(服务发送通知):
//	TempMsgUtil.serveSendMsg( first, merid, order, itemname, money, state, time, url, remark)
	public void paychrgesendmsg( Integer merid, Integer type, String first, String phone, String url, Double money){//付款成功
		try {
			Map<String, String> map = CommonHandler.selectEealerauthority(merid, type);
			String onoff = map.get("switch");
			if(onoff.equals("1")){
				User meruser = UserHandler.getUserInfo(merid);
				if(null!=meruser){
					String oppenid = meruser.getOpenid();
					JSONObject json = new JSONObject();
					json.put("first", TempMsgUtil.inforData( first,"#FF3333"));
					json.put("keyword1", TempMsgUtil.inforData("自助充电平台","#0044BB"));
					json.put("keyword2", TempMsgUtil.inforData("¥ "+String.format("%.2f", money),"#0044BB"));
					json.put("keyword3", TempMsgUtil.inforData(StringUtil.toDateTime(),"#0044BB")); 
					json.put("keyword4", TempMsgUtil.inforData(phone,"#0044BB"));
					json.put("remark", TempMsgUtil.inforData(CommonConfig.PAYSUCCEEDMES,"#0044BB"));
					TempMsgUtil.sendTempMsg(oppenid, TempMsgUtil.TEMP_IDPAY, url, json);
				}
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
//***************==============================================================================================	
	public boolean checkUserIfRich(TradeRecord tradeRecord) {
		String comment = CommUtil.toString(tradeRecord.getComment());
		Integer merid = tradeRecord.getMerid();
		Double money = tradeRecord.getMoney();
		if(comment==null || comment.equals("0")){//之前数据
			Integer manid = tradeRecord.getManid();
			Double manmoney = tradeRecord.getManmoney();
			Double mermoney = tradeRecord.getMermoney();
			if (manid != null && manid != 0) {
				User manUser = userService.selectUserById(manid);
				User merUser = userService.selectUserById(merid);
				if ((manUser.getEarnings() >= manmoney) && (merUser.getEarnings() >= mermoney)) {
					return true;
				} else {
					return false;
				}
			} else {
				User merUser = userService.selectUserById(merid);
				if ((merUser.getEarnings() >= money)) {
					return true;
				} else {
					return false;
				}
			}
		}else{
			JSONArray jsona = JSONArray.fromObject(comment);
			Boolean fsal = true;
			for (int i = 0; i < jsona.size(); i++) {
				JSONObject item = jsona.getJSONObject(i);
				Integer partid = CommUtil.toInteger(item.get("partid"));
				Integer mercid = CommUtil.toInteger(item.get("merid"));
				//String percent = CommUtil.toString(item.get("percent"));
				Double partmoney = CommUtil.toDouble(item.get("money"));
				if(partid!=0){//合伙人信息
					User partUser = userService.selectUserById(partid);
					if (partUser.getEarnings() >= partmoney) {
						return true;
					} else {
						fsal =  false;
						break;
					}
				}else{//商户信息
					User merUser = userService.selectUserById(mercid);
					if (merUser.getEarnings() >= partmoney) {
						return true;
					} else {
						fsal = false;
						break;
					}
				}
			}
			return fsal;
		}
	}

	/**
	 * @Description： 查看判断钱包金额与退款金额对应的到账金额差
	 * @author： origin  
	 */
	public boolean walletRich(Money moneyRecord) {
		if (moneyRecord != null) {
			Double paymoney = CommUtil.toDouble(moneyRecord.getMoney());
			Double sendmoney = CommUtil.toDouble(moneyRecord.getSendmoney());
			Double tomoney = CommUtil.toDouble(moneyRecord.getTomoney());
			Double topupbalance = CommUtil.toDouble(moneyRecord.getTopupbalance());
			Double givebalance = CommUtil.toDouble(moneyRecord.getGivebalance());
			Double balance = CommUtil.toDouble(moneyRecord.getBalance());
			Integer uid = CommUtil.toInteger(moneyRecord.getUid());
			User user = userService.selectUserById(uid);
			if(user==null) return false;
			Double userbalance = CommUtil.toDouble(user.getBalance());
			Double usersendmoney = CommUtil.toDouble(user.getSendmoney());
			
			if(sendmoney.equals(0.00) && topupbalance.equals(0.00) && givebalance.equals(0.00)){//未分离赠送金额
				sendmoney = CommUtil.subBig(tomoney, paymoney);
				userbalance = CommUtil.subBig(userbalance, tomoney);
				usersendmoney = CommUtil.toDouble(sendmoney);
				if( userbalance >= 0 && usersendmoney >= 0 ){
					return true;
				}
				return false;
			}else{
				userbalance = CommUtil.subBig(userbalance, paymoney);
				usersendmoney = CommUtil.subBig(usersendmoney, sendmoney);
				if( userbalance >= 0 && usersendmoney >= 0 ){
					return true;
				}
				return false;
			}
//			Double balance = CommUtil.toDouble(user.getBalance());
//			if(balance >= tomoney){
//				return true;
//			}else{
//				return false;
//			}
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @Description： 查看判断在线卡金额与退款金额差
	 * @author： origin
	 */
	public boolean checkOnlineRich(OnlineCardRecord cardRecord) {
		OnlineCard onCard = onlineCardService.selectOnlineCardByCardID(cardRecord.getCardID());
		if (onCard != null) {
			Double paymoney = CommUtil.toDouble(cardRecord.getMoney());
			Double accountmoney = CommUtil.toDouble(cardRecord.getAccountmoney());
			Double sendmoney = CommUtil.subBig(accountmoney, paymoney);
//			Double sendmoney = CommUtil.toDouble(cardRecord.getSendmoney());
			
			Integer merid = CommUtil.toInteger(onCard.getMerid());
			Double cardamoney = CommUtil.toDouble(onCard.getMoney());
			Double cardsendmoney = CommUtil.toDouble(onCard.getSendmoney());
			
			Double topupbalance = 0.00;
			Double givebalance = 0.00;
			if(sendmoney.equals(0.00) && topupbalance.equals(0.00) && givebalance.equals(0.00)){//未分离赠送金额
				sendmoney = CommUtil.subBig(accountmoney, paymoney);
				topupbalance = CommUtil.subBig(cardamoney, accountmoney);
				givebalance = CommUtil.toDouble(sendmoney);
				if( topupbalance >= 0 && givebalance >= 0 ){
					return true;
				}
				return false;
			}else{
				topupbalance = CommUtil.subBig(cardamoney, paymoney);
				givebalance = CommUtil.subBig(cardsendmoney, sendmoney);
				if( topupbalance >= 0 && givebalance >= 0 ){
					return true;
				}
				return false;
			}
//			Double  money = onCard.getMoney();
//			Double accmoney = cardRecord.getAccountmoney();
//			if(Double.doubleToLongBits(money) >= Double.doubleToLongBits(accmoney)){
//			//if(onCard.getMoney()>=cardRecord.getAccountmoney()){
//				return true;
//			}else{
//				return false;
//			}
		} else {
			return false;
		}
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
				Integer vawalt = CommUtil.toInteger(online.getRelevawalt());
				if(vawalt==1){
					type=3;
					Area area = areaService.selectByIdArea(CommUtil.toInteger(argumentval));
					tempid = CommUtil.toInteger(area.getTempid());
				}else{
					Area area = areaService.selectByIdArea(online.getUid());
					tempid = CommUtil.toInteger(area.getTempid());
				}
			}else if(type==5){
				
			}
			TemplateParent temppa = templateService.getParentTemplateOne(tempid);
			if(temppa==null){
				List<TemplateParent> parentTemplist = templateService.getParentTemplateListByMerchantid(merid, type);
				if (parentTemplist == null || parentTemplist.size() == 0) {
					parentTemplist = templateService.getParentTemplateListByMerchantid(0, type);
				}
				temppa = parentTemplist.get(0);
			}
			phone = temppa.getCommon1();
			if(phone==null || phone.equals("")){
				User user = userService.selectUserById(merid);
				phone = CommUtil.toString(user.getServephone());
				if(phone==null) CommUtil.toString(phone = user.getPhoneNum());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phone;
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
	 * 微信下载交易账单
	 * @param chargeparam
	 * @param portchoose
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadbill")
	@ResponseBody
	public Map<String, Object> downloadbill(String time, Integer billtype, String appid, String mch_id,
			 String sub_appid, String sub_mch_id) throws Exception {
		//-------------------------------------------------
		String bill_date = time.replace("-", "");
		String bigintime = time + " 00:00:00";
		String endtime = time + " 23:59:59";
		String bill_type = "ALL";//返回当日所有订单信息（不含充值退款订单）
		billtype = CommUtil.toInteger(billtype);
		Integer status = null;
		if(billtype.equals(1)){//返回当日成功支付的订单（不含充值退款订单）
			bill_type = "SUCCESS";
		}else if(billtype.equals(2)){//返回当日退款订单（不含充值退款订单）
			bill_type = "REFUND";
			status = 2;
		}else if(billtype.equals(3)){//返回当日充值退款订单
			bill_type = "RECHARGE_REFUND";
			status = null;
			bigintime = CommUtil.toDateTime( "yyyy-MM-dd 00:00:00", new Date());
			endtime = CommUtil.toDateTime( "yyyy-MM-dd 23:59:59", new Date());
		}
		//-------------------------------------------------
		if(CommUtil.trimToEmpty(appid).equals("")) appid = WeiXinConfigParam.FUWUAPPID;
		if(CommUtil.trimToEmpty(mch_id).equals("")) mch_id = WeiXinConfigParam.MCHID;
		SortedMap<String, String> params = new TreeMap<>();
		params.put("appid", appid);
		params.put("mch_id", mch_id);
		if(!CommUtil.trimToEmpty(sub_appid).equals("")) params.put("sub_appid", sub_appid);
		if(!CommUtil.trimToEmpty(sub_mch_id).equals("")) params.put("sub_mch_id", sub_mch_id);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		params.put("bill_date", bill_date);
		params.put("bill_type", bill_type);
//		params.put("tar_type", "");
		//------------------------------------------------------
		// 生成签名并放入map集合里
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/downloadbill";
		String canshu = HttpRequest.getRequestXml(params);
		String result = HttpRequest.sendPost(url, canshu);
		
		Map<String, Object> mapinfo = new HashMap<String, Object>();
		if(result.contains("<xml>")){
			Map<String, String> map = XMLUtil.doXMLParse(result);
			String returncode = CommUtil.trimToEmpty(map.get("return_code"));
			String returnmsg = CommUtil.trimToEmpty(map.get("return_msg")).toUpperCase();
			String errorcode = CommUtil.trimToEmpty(map.get("error_code"));
			mapinfo.put("mapinfo", map);
			mapinfo.put("returncode", returncode);
			mapinfo.put("returnmsg", returnmsg);
			mapinfo.put("errorcode", errorcode);

			String news = null;
			if(errorcode.equals("100") || errorcode.equals("20003")){
				news = "下载失败。系统超时;请尝试再次查询。";
			} else if(errorcode.equals("20001")){
				news = "参数不正确。请求参数未按要求进行填写。";
			} else if(errorcode.equals("20002")){
				news = "账单未生成或不存在。当前商户号没有已成交的订单或对账单尚未生成。";
			} else if(errorcode.equals("20007")){
				news = "当前商户号账单API权限已经关闭。";
			} else if(errorcode.equals("20100")){
				news = "下载失败。系统错误。";
			} else{
				news = "下载失败！未知错误。";
			} 
			mapinfo.put("news", news);
			StringBuffer hint = new StringBuffer(returncode);
			hint.append(";").append(errorcode).append(";").append(returnmsg).append(";");
			String hintinfo = null;
			if(returnmsg.equals(("SYSTEMERROR").toUpperCase())){
				hintinfo = "下载失败;系统超时;请尝试再次查询。";
			}else if(returnmsg.equals(("sign error").toUpperCase())){
				hintinfo = "签名错误;请求参数未按要求进行填写;签名错误，请重新检查参数和签名密钥是否正确";
			}else if(returnmsg.equals(("nonce_str too long").toUpperCase())){
				hintinfo = "参数nonce_str错误;请求参数未按要求填写;参数nonce_str长度超长";
			}else if(returnmsg.equals(("invalid tar_type, Only GZIP supported").toUpperCase())){
				hintinfo = "参数tar_type错误;请求参数未按指引进行填写;请重新检查参数invalid tar_typ是否正确";
			}else if(returnmsg.equals(("invalid bill_type").toUpperCase())){
				hintinfo = "参数bill_type错误;请求参数未按指引进行填写;请重新检查参数bill_type是否正确";
			}else if(returnmsg.equals(("invalid bill_date").toUpperCase())){
				hintinfo = "参数bill_date错误;请求参数未按指引进行填写;请重新检查参数bill_date是否符合要求";
			}else if(returnmsg.equals(("require POST method").toUpperCase())){
				hintinfo = "请求方式错误;请求方式不符合要求;请求检查参数请求方式是否为post";
			}else if(returnmsg.equals(("empty post data").toUpperCase())){
				hintinfo = "请求报文错误;请求报文为空;请重新检查请求报文是否正确";
			}else if(returnmsg.equals(("data format error").toUpperCase())){
				hintinfo = "参数格式错误;请求参数要求为xml格式;请重新检查请求参数格式是否为xml";
			}else if(returnmsg.equals(("missing parameter").toUpperCase())){
				hintinfo = "缺少参数;有必传的参数未上传;请重新检查是否所有必传参数都上传了，且不为空";
			}else if(returnmsg.equals(("invalid appid").toUpperCase())){
				hintinfo = "appid错误;请求参数appid有误;请重新检查参数appid是否正确";
			}else if(returnmsg.equals(("invalid parameter").toUpperCase())){
				hintinfo = "参数错误;有未知的请求参数;请重新检查是否所有参数都与文档相符";
			}else if(returnmsg.equals(("No Bill Exist").toUpperCase())){
				hintinfo = "账单不存在;当前商户号没有已成交的订单，不生成对账单;请检查当前商户号在指定日期内是否有成功的交易。";
			}else if(returnmsg.equals(("Bill Creating").toUpperCase())){
				hintinfo = "账单未生成;当前商户号没有已成交的订单或对账单尚未生成;请先检查当前商户号在指定日期内是否有成功的交易，如指定日期有交易则表示账单正在生成中，请在上午10点以后再下载。";
			}else if(returnmsg.equals("当前商户号账单API权限已经关闭")){
				hintinfo = "当前商户号账单API权限已经关闭;当前商户号账单API权限已经关闭;当前商户号账单API权限已经关闭，请联系微信支付解决";
			}else if(returnmsg.equals(("system error").toUpperCase())){
				hintinfo = "下载失败;系统超时;请尝试再次查询。";
			}else{
				hintinfo = "下载失败;未知原因。";
			}
			hint.append(hintinfo);
			mapinfo.put("hint", hint);
			String message = errorcode + hintinfo;
			mapinfo.put("message", message);
			CommUtil.responseBuildInfo(101, message, mapinfo);
		}else{
			String headername = result.substring(0, result.indexOf("`"));//表头数据
			List<String> listheader = Arrays.asList(headername.split(","));
			mapinfo.put("listheader", listheader);
			String tradeMsg = result.substring(result.indexOf("`"));//除表头所有数据
			//获取数据
			String contentInfo = tradeMsg.substring(0, tradeMsg.indexOf("总"));//数据内容
			String[] tradeArray = contentInfo.split("``");//将数据内容  根据``来区分 转换为数组
			List<Map<String, Object>> listcontent = new ArrayList<>();
			List<String> listWeChartOrder = new ArrayList<>();
			for (String tradeDetailInfo : tradeArray) {
				String[] tradeDetailArray = tradeDetailInfo.split(",");
				String WXordernum = CommUtil.toString(tradeDetailArray[6].replace("`", ""));
				listWeChartOrder.add(WXordernum);
				Map<String, Object> mapdata = new HashMap<String, Object>();
				mapdata.put("creattime", CommUtil.toString(tradeDetailArray[0].replace("`", "")));// 交易时间
				mapdata.put("commonId", CommUtil.toString(tradeDetailArray[1].replace("`", "")));// 公众账号ID
				mapdata.put("businessnum", CommUtil.toString(tradeDetailArray[2].replace("`", "")));// 商户号
				mapdata.put("childBusinessnum", CommUtil.toString(tradeDetailArray[3].replace("`", "")));// 子商户号
				mapdata.put("equipmentnum", CommUtil.toString(tradeDetailArray[4].replace("`", "")));// 设备号
				mapdata.put("WxOrderNo", tradeDetailArray[5].replace("`", ""));// 微信订单号
				mapdata.put("BusinessOrderNo", tradeDetailArray[6].replace("`", ""));// 商户订单号
				mapdata.put("UserIdentity", tradeDetailArray[7].replace("`", ""));// 用户标识
				mapdata.put("TransType", tradeDetailArray[8].replace("`", ""));// 交易类型
				mapdata.put("TransStatus", tradeDetailArray[9].replace("`", ""));// 交易状态
				mapdata.put("PaymentBank", tradeDetailArray[10].replace("`", ""));// 付款银行
				mapdata.put("Currency", tradeDetailArray[11].replace("`", ""));// 货币种类
				mapdata.put("TotalAmount", tradeDetailArray[12].replace("`", ""));// 总金额
				mapdata.put("RedEnvelopesAmount", tradeDetailArray[13].replace("`", ""));// 企业红包金额
				mapdata.put("WxRefundNo", tradeDetailArray[14].replace("`", ""));// 微信退款单号
				mapdata.put("BusinessRefundNo", tradeDetailArray[15].replace("`", ""));// 商户退款单号
				mapdata.put("RefundAmount", tradeDetailArray[16].replace("`", ""));// 退款金额
				mapdata.put("RedEnvelopesRefundAmount", tradeDetailArray[17].replace("`", ""));// 企业红包退款金额
				mapdata.put("RefundType", tradeDetailArray[18].replace("`", ""));// 退款类型
				mapdata.put("RefundStatus", tradeDetailArray[19].replace("`", ""));// 退款状态
				mapdata.put("BusinessName", tradeDetailArray[20].replace("`", ""));// 商品名称
				mapdata.put("BusinessData", tradeDetailArray[21].replace("`", ""));// 商户数据包
				mapdata.put("Fee", tradeDetailArray[22].replace("`", ""));// 手续费
				mapdata.put("Rate", tradeDetailArray[23].replace("`", "") + "%");// 费率
				mapdata.put("CreateDate", new Date());
				listcontent.add(mapdata);
			}
			mapinfo.put("listcontent", listcontent);
			mapinfo.put("listWeChartOrder", listWeChartOrder);
			
			List<String> tradOrder = tradeRecordService.getTraderecordOrder(status, bigintime, endtime);
			mapinfo.put("listTradOrder", tradOrder);

			List<String> lostWxOrder = new ArrayList<>();
			
			lostWxOrder.addAll(listWeChartOrder); 
			lostWxOrder.removeAll(tradOrder);
			List<Map<String, Object>> lostcontent = new ArrayList<>();
			for(int i=0; i< lostWxOrder.size(); i++){
				String lowxorder = lostWxOrder.get(i);
				for(int j=0; j< listcontent.size(); j++){
					String BusinessName = CommUtil.toString(listcontent.get(j).get("BusinessName"));
					String wxorder = CommUtil.toString(listcontent.get(j).get("BusinessOrderNo"));
					if(lowxorder.equals(wxorder) && !BusinessName.contains("微信设备缴费")) lostcontent.add(listcontent.get(j));
				}
			}
			lostOrderService.insertLostListOrder(lostcontent);
			CommUtil.responseBuildInfo(200, "SUCCESS", mapinfo);
		}
		return mapinfo;
	}	
	
	/**
	 * @author origin
	 * @param ordernum
	 * @param submchid
	 * @param totalfee
	 * @param refundfee
	 * @return
	 */
	@RequestMapping(value = "/lostDoReturn")
	@ResponseBody
	public Object lostDoReturn(Integer id, String ordernum, String submchid, String totalfee, String refundfee){
		Map<String, Object> datamap = new HashMap<>();
		try {
			WXPayConfigImpl config = WXPayConfigImpl.getInstance();
			WXPay wxpay = new WXPay(config);
			// 根据id查询充电信息
			String out_trade_no = CommUtil.trimToEmpty(ordernum);
			String total_fee = CommUtil.trimToEmpty(totalfee);
			String refund_fee = CommUtil.trimToEmpty(refundfee);
			if(CommUtil.trimToEmpty(submchid).equals("")) submchid = WeiXinConfigParam.SUBMCHID;
			//------------------------------------------------------
			// 生成签名并放入map集合里
			SortedMap<String, String> params = new TreeMap<>();
			params.put("appid", WeiXinConfigParam.FUWUAPPID);
			params.put("mch_id", WeiXinConfigParam.MCHID);
			params.put("sub_mch_id", submchid);
			params.put("out_trade_no", out_trade_no);
			params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
			String sign = HttpRequest.createSign("UTF-8", params);
			params.put("sign", sign);
			String url = "https://api.mch.weixin.qq.com/pay/orderquery";
			String canshu = HttpRequest.getRequestXml(params);
			String sr = HttpRequest.sendPost(url, canshu);
			Map<String, String> resultMap = XMLUtil.doXMLParse(sr);
			
			if(total_fee.equals("")) total_fee = CommUtil.toString(resultMap.get("total_fee"));
			if(refund_fee.equals("")) refund_fee = CommUtil.toString(resultMap.get("total_fee"));
			datamap.put("resultMap", resultMap);
			// 退款后的业务处理
			if (resultMap.get("return_code").toString().equalsIgnoreCase("SUCCESS")) {
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("appid", WeiXinConfigParam.FUWUAPPID);
				data.put("mch_id", WeiXinConfigParam.MCHID);
				data.put("sub_mch_id", submchid);
				data.put("transaction_id", resultMap.get("transaction_id"));
				data.put("out_trade_no", out_trade_no);// 定单号
				data.put("out_refund_no", "t" + out_trade_no);
				data.put("total_fee", total_fee);
				data.put("refund_fee", refund_fee);
				data.put("refund_fee_type", "CNY");
				data.put("op_user_id", config.getMchID());
				try {
					Map<String, String> r = wxpay.refund(data);
					datamap.put("resultrefMap", r);
					logger.info("G退款后的参数"+"========================"+r.toString());
					// 处理退款后的订单 成功
					if ("SUCCESS".equals(r.get("result_code"))) {
						String outrefundno = CommUtil.toString(r.get("out_refund_no"));
						String wxrefundno = CommUtil.toString(r.get("refund_id"));
						double feerefund = CommUtil.toDouble(r.get("refund_fee"))/100;
						String refundfeemoney = CommUtil.toString(feerefund);
						lostOrderService.updataLostRefundOrder( id,outrefundno,wxrefundno,refundfeemoney);
						CommUtil.responseBuildInfo(200, "SUCCESS", datamap);
					}else if ("FAIL".equals(r.get("result_code"))) {
						CommUtil.responseBuildInfo(3001, "ERROR,退款失败", datamap);
					}
				} catch (Exception e) {
					e.printStackTrace();
					CommUtil.responseBuildInfo(3002, "ERROR,退款失败", datamap);
				}
			} else {
				CommUtil.responseBuildInfo(3003, "ERROR,未查到该订单", datamap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(3003, "ERROR,服务器发生错误", datamap);
		}
		return datamap;
	}
	
	/**
	 * @Description：查询订单号信息
	 * @author origin
	 * @param ordernum 商户订单号   traordernum:交易订单号（微信订单号）  submchid:(服务商下的)子商户号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/inquireWXOrderData")
	@ResponseBody
	public Map<String, Object> inquireWXOrderData(String ordernum, String traordernum, 
			String submchid) throws Exception {
		//-------------------------------------------------
		if(CommUtil.trimToEmpty(submchid).equals("")) submchid = WeiXinConfigParam.SUBMCHID;
		SortedMap<String, String> params = new TreeMap<>();
		params.put("appid", WeiXinConfigParam.FUWUAPPID);
		params.put("mch_id", WeiXinConfigParam.MCHID);
//		params.put("sub_appid", "");
		params.put("sub_mch_id", submchid);
		if(!CommUtil.trimToEmpty(traordernum).equals(""))params.put("transaction_id", traordernum);
		if(!CommUtil.trimToEmpty(ordernum).equals(""))params.put("out_trade_no", ordernum);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		//------------------------------------------------------
		// 生成签名并放入map集合里
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/orderquery";
		String canshu = HttpRequest.getRequestXml(params);
		String result = HttpRequest.sendPost(url, canshu);
		Map<String, String> map = XMLUtil.doXMLParse(result);
		String returncode = CommUtil.trimToEmpty(map.get("return_code"));
		String returnmsg = CommUtil.trimToEmpty(map.get("return_msg")).toUpperCase();
		
		Map<String, Object> mapinfo = new HashMap<String, Object>();
		mapinfo.put("mapinfo", map);
		mapinfo.put("returncode", returncode);
		mapinfo.put("returnmsg", returnmsg);
		
		Map<String, Object> mapdate = new HashMap<String, Object>();
		if(returncode.contains("SUCCESS")){
			mapdate.put("服务商的APPID", map.get("appid"));
			mapdate.put("商户号", map.get("mch_id"));
			mapdate.put("子商户号", map.get("sub_mch_id"));
			mapdate.put("微信订单号", map.get("transaction_id"));
			mapdate.put("商户订单号", map.get("out_trade_no"));
			mapdate.put("随机字符串", map.get("nonce_str"));
			mapdate.put("签名", map.get("sign"));
			
			mapdate.put("交易状态", map.get("trade_state"));
			mapdate.put("付款银行", map.get("bank_type"));
			mapdate.put("用户标识", map.get("openid"));

			mapdate.put("返回信息", map.get("return_msg"));
			mapdate.put("标价币种", map.get("fee_type"));
			mapdate.put("现金支付金额", map.get("cash_fee"));
			mapdate.put("现金支付货币类型", map.get("cash_fee_type"));
			mapdate.put("标价金额", map.get("total_fee"));
			mapdate.put("交易状态描述", map.get("trade_state_desc"));
			mapdate.put("交易类型", map.get("trade_type"));
			mapdate.put("业务结果", map.get("result_code"));
			mapdate.put("返回状态码", map.get("return_code"));
			mapdate.put("是否关注公众账号", map.get("is_subscribe"));
			mapdate.put("支付完成时间", map.get("time_end"));
			mapinfo.put("mapdate", mapdate);
			System.out.println("成功输出  mapinfo    "+mapinfo);
		}else{
			System.out.println("其他输出  mapinfo    "+mapinfo);
		}
			CommUtil.responseBuildInfo(200, "SUCCESS", mapinfo);
		return mapinfo;
	}
	
	/**
	 * @Description：查询订单号信息
	 * @author origin
	 * @param ordernum 商户订单号   traordernum:交易订单号（微信订单号）  submchid:(服务商下的)子商户号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/inquireWXRefOrderData")
	@ResponseBody
	public Map<String, Object> inquireWXRefOrderData( String submchid, String ordernum, 
			String traordernum, String refundid, String outrefundno, String offset) throws Exception{
		//-------------------------------------------------
		if(CommUtil.trimToEmpty(submchid).equals("")) submchid = WeiXinConfigParam.SUBMCHID;
		SortedMap<String, String> params = new TreeMap<>();
		params.put("appid", WeiXinConfigParam.FUWUAPPID);
		params.put("mch_id", WeiXinConfigParam.MCHID);
//		params.put("sub_appid", "");
		params.put("sub_mch_id", submchid);
		
		//同时存在优先级为： refund_id > out_refund_no > transaction_id > out_trade_no
		if(!CommUtil.trimToEmpty(refundid).equals(""))params.put("refund_id", refundid);
		if(!CommUtil.trimToEmpty(outrefundno).equals(""))params.put("out_refund_no", outrefundno);
		if(!CommUtil.trimToEmpty(traordernum).equals(""))params.put("transaction_id", traordernum);
		if(!CommUtil.trimToEmpty(ordernum).equals(""))params.put("out_trade_no", ordernum);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		//------------------------------------------------------
		// 生成签名并放入map集合里
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/refundquery";
		String canshu = HttpRequest.getRequestXml(params);
		String result = HttpRequest.sendPost(url, canshu);
		Map<String, String> map = XMLUtil.doXMLParse(result);
		String returncode = CommUtil.trimToEmpty(map.get("return_code"));
		String returnmsg = CommUtil.trimToEmpty(map.get("return_msg")).toUpperCase();
		
		Map<String, Object> mapinfo = new HashMap<String, Object>();
		mapinfo.put("mapinfo", map);
		mapinfo.put("returncode", returncode);
		mapinfo.put("returnmsg", returnmsg);
		
		Map<String, Object> mapdate = new HashMap<String, Object>();
		if(returncode.contains("SUCCESS")){
			mapdate.put("服务商的APPID", map.get("appid"));
			mapdate.put("商户号", map.get("mch_id"));
			mapdate.put("子商户号", map.get("sub_mch_id"));
			mapdate.put("微信订单号", map.get("transaction_id"));
			mapdate.put("商户订单号", map.get("out_trade_no"));
			mapdate.put("随机字符串", map.get("nonce_str"));
			mapdate.put("签名", map.get("sign"));
			mapdate.put("返回信息", map.get("return_msg"));
			mapdate.put("业务结果", map.get("result_code"));
			mapdate.put("返回状态码", map.get("return_code"));
			
			mapdate.put("订单金额", map.get("total_fee"));
			mapdate.put("现金支付金额", map.get("cash_fee"));
			mapdate.put("退款笔数", map.get("refund_count"));
			mapdate.put("退款金额", map.get("refund_fee"));
			Integer refundcount = CommUtil.toInteger(map.get("refund_count"));
			for(int i = 0; i< refundcount; i++){
				String timenum = CommUtil.toString(i +1);
				String name = "第 "+timenum+" 笔";
				String refundfee = "refund_fee_"+i;
				String buoutrefundno = "out_refund_no_"+i;
				String wxrefundid = "refund_id_"+i;
				String refundstatus = "refund_status_"+i;
				String refundaccount = "refund_account_"+i;
				String refundchannel = "refund_channel_"+i;
				String refundsuccesstime = "refund_success_time_"+i;
				String refundrecvaccout = "refund_recv_accout_"+i;
				
				mapdate.put(name +"申请退款金额", map.get(refundfee));
				mapdate.put(name +"商户退款单号", map.get(buoutrefundno));
				mapdate.put(name +"微信退款单号", map.get(wxrefundid));
				mapdate.put(name +"退款状态", map.get(refundstatus));
				mapdate.put(name +"退款资金来源", map.get(refundaccount));
				mapdate.put(name +"退款渠道", map.get(refundchannel));
				mapdate.put(name +"退款成功时间", map.get(refundsuccesstime));
				mapdate.put(name +"退款入账账户", map.get(refundrecvaccout));
			}
			mapinfo.put("mapdata", mapdate);
			System.out.println("成功输出  mapinfo    "+mapinfo);
		}else{
			System.out.println("其他输出  mapinfo    "+mapinfo);
		}
			CommUtil.responseBuildInfo(200, "SUCCESS", mapinfo);
		return mapinfo;
	}
	
}
