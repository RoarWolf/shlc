package com.hedong.hedongwx.web.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayOpenAuthTokenAppRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.hedong.hedongwx.config.AlipayConfig;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.InCoins;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.Money;
import com.hedong.hedongwx.entity.OfflineCard;
import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.OnlineCardRecord;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.BasicsService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.EquipmentService;
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
import com.hedong.hedongwx.utils.ByteUtils;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.MD5Util;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.TempMsgUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Controller
@RequestMapping("/alipay")
public class AlipayController {

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private ChargeRecordService chargeRecordService;
	private final Logger logger = LoggerFactory.getLogger(AlipayController.class);
	@Autowired
	private TemplateService templateService;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private UserService userService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private InCoinsService inCoinsService;
	@Autowired
	private OfflineCardService offlineCardService;
	@Autowired
	private MerchantDetailService merchantDetailService;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private OnlineCardService onlineCardService;
	@Autowired
	private OnlineCardRecordService onlineCardRecordService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private GeneralDetailService generalDetailService;
	@Autowired
	private BasicsService basicsService;
	
	@RequestMapping("/appletgetuid")
	@ResponseBody
	public Map<String, Object> getuid(String authCode) throws Exception {
		logger.info("授权码===" + authCode);
		Integer code = 200;
		String message = "获取成功";
		Map<String,Object> hashMap = new HashMap<>();
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPLET_APPID, AlipayConfig.APPLET_RSA_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.APPLET_ALIPAY_PUBLIC_KEY, "RSA2");
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setCode(authCode);
		request.setGrantType("authorization_code");
		try {
		    AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
		    System.out.println("小程序获取用户id===" + oauthTokenResponse.getUserId());
		    hashMap.put("userid", oauthTokenResponse.getUserId());
		} catch (AlipayApiException e) {
		    //处理异常
		    e.printStackTrace();
		    code = 201;
		    message = "获取失败";
		}
		return	CommUtil.responseBuildInfo(code, message, hashMap);
	}
	

	/**
	 * @method_name: scanAlipayPayment
	 * @Description: 扫码支付宝支付
	 * @return
	 * @throws IOException
	 * @Author: origin  创建时间:2020年7月30日 下午2:55:30
	 * @common:
	 */
	@RequestMapping(value = "/scanAlipayPayment")
	@ResponseBody
	public Map<String, Object> scanAlipayPayment(HttpServletRequest request) {
		Map<String, Object> hashMap = new HashMap<>();
		try {
			// 获取参数
			String userid = request.getParameter("userid");
			String code = request.getParameter("code");
			String tempid = request.getParameter("tempid");
			String param = request.getParameter("param");
			String hardversion = request.getParameter("hardversion");
//			String ordernum = HttpRequest.createOrdernum(6);
			String ordernum = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
			System.out.println("userid===" + userid+"	code===" + code+"	tempid===" + tempid+"	param===" + param+"	hardversion===" + hardversion);
			// 订单名称，必填
			String subject = null;
			TemplateSon templateSon = templateService.getInfoTemplateOne(Integer.parseInt(tempid));
			Double paymoney = CommUtil.toDouble(templateSon.getMoney());
			Double accountmoney = CommUtil.toDouble(templateSon.getRemark());
			String body = null;//1:充电   2:脉冲   3:离线  4:在线  5:钱包  6:包月
			if(hardversion != null && "03".equals(hardversion)){
				body = "脉冲";
				subject = "自助充电平台（脉冲充电）";
				ordernum = ordernum + code;
			} else if (hardversion != null && "04".equals(hardversion)) {
				body = "离线卡";
				subject = "自助充电平台（离线充值）";
				ordernum = ordernum + code;
			} else if (hardversion != null && "在线卡".equals(hardversion)) {
				body = "在线卡";
				subject = "自助充电平台（在线卡充值）";
			} else {
				body = "充电";
				subject = "自助充电平台（扫码充电）";
				ordernum = ordernum + code + param;
			}

			System.out.println("subject===" + subject);
			System.out.println("ordernum===" + ordernum);
			// 付款金额，必填
			String total_amount = new String(String.format("%.2f", paymoney).getBytes("ISO-8859-1"), "UTF-8");
			//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.create.
			AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPLET_APPID, AlipayConfig.APPLET_RSA_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.APPLET_ALIPAY_PUBLIC_KEY, "RSA2");
			AlipayTradeCreateRequest requestapp = new AlipayTradeCreateRequest();
			//SDK已经封装掉了公共参数，这里只需要传入业务参数。
			requestapp.setBizContent("{" +
					"\"out_trade_no\":\"" + ordernum + "\"," +
					"\"subject\":\"" + subject + "\"," +
					"\"total_amount\":" + total_amount + "," +
					"\"buyer_id\":\"" + userid + "\"," +
					"\"body\":\"" + body + "\"," +
					"\"extend_params\":{" +
					"\"sys_service_provider_id\":\"" + AlipayConfig.XIEZUOPID + "\"" +
					"    }" +
					"  }");
			requestapp.setNotifyUrl(AlipayConfig.NOTIFY_URL + "1");
		    //使用的是execute
		    AlipayTradeCreateResponse response = alipayClient.execute(requestapp);
		    System.out.println("支付宝预订单号返回新===" + response);
		    String trade_no = response.getTradeNo();//获取返回的tradeNO。
		    hashMap.put("trade_no", trade_no);
		    System.out.println("支付宝订单号为===" + trade_no);
		    
		    if(hardversion != null && "03".equals(hardversion)){
				inCoinsService.insertInCoinsRecord(ordernum, code, Byte.parseByte(param), null, paymoney,
						(Byte.parseByte(WxPayController.doubleToString(accountmoney))), (byte) 10, (byte) 0);
			} else if (hardversion != null && "04".equals(hardversion)) {
				offlineCardService.insertChargeMoneyOfflineCardRecord(ordernum, code, param, paymoney,
						accountmoney, 1, 8, null, 0);
			} else if (hardversion != null && "在线卡".equals(hardversion)){
				OnlineCard card = onlineCardService.selectOnlineCardByCardID(param);
				Integer relevawalt = CommUtil.toInteger(card.getRelevawalt());
				Integer uid = CommUtil.toInteger(card.getUid());
				if(card!=null && card.getRelevawalt()==1){
					User touser = userService.selectUserById(card.getUid());
					if(touser!=null){
						card.setMoney(touser.getBalance());
						card.setSendmoney(touser.getSendmoney());
						uid = CommUtil.toInteger(touser.getId());
					}
				}
				//============================================================================
				Double topupmoney = CommUtil.toDouble(card.getMoney());//充值金额
				Double sendmoney = CommUtil.toDouble(card.getSendmoney());//赠送金额
				//Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额
				//==========================================================================
				Double opermoney = CommUtil.toDouble(accountmoney);//操作总额
				Double opertopupmoney = CommUtil.toDouble(paymoney);//操作充值金额
				Double opersendmoney = CommUtil.subBig(opermoney, opertopupmoney);//操作赠送金额
				//==========================================================================
				Double topupbalance = CommUtil.addBig(topupmoney, opertopupmoney);//充值余额
				Double sendbalance = CommUtil.addBig(sendmoney, opersendmoney);//赠送余额
				Double accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
				//==========================================================================
				Date date = new Date();
				Integer merid = CommUtil.toInteger(card.getMerid());
				if(relevawalt==1){
					moneyService.payMoneys(uid, ordernum, 9, 0, opertopupmoney, opersendmoney, opermoney, accountbalance, topupbalance, sendbalance, card.getCardID());
					moneyService.insertWalletInfo(uid, merid, ordernum, 1, 9, 0, opertopupmoney, opersendmoney, 
					opermoney, accountbalance, topupbalance, sendbalance, new Date(), card.getCardID());
				}else{
					if (merid != null) {
						onlineCardRecordService.insertOnlineCardRecord(uid, merid, ordernum, param, null, accountbalance, opertopupmoney, opersendmoney, 
							opermoney, topupbalance, sendbalance, 10, 0, 1, date, 0);
					} else {
						onlineCardRecordService.insertOnlineCardRecord(uid, null, ordernum  , param, null, accountbalance, opertopupmoney, opersendmoney, 
							opermoney, topupbalance, sendbalance, 10, 0, 1, date, 0);
					}
				}
			}else {
				UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
				ChargeRecord chargeRecord = new ChargeRecord();
				chargeRecord.setEquipmentnum(code);// 设备号/机位号
				chargeRecord.setOrdernum(ordernum);// 订单号
				int port = CommUtil.toInteger(param);
				chargeRecord.setPort(port);// 端口号
				chargeRecord.setExpenditure(paymoney);// 金额
				chargeRecord.setDurationtime(templateSon.getChargeTime().toString());// 时长
				chargeRecord.setQuantity(templateSon.getChargeQuantity());// 电量
				chargeRecord.setPaytype(8);// 支付类型 8、小程序支付
				chargeRecord.setMerchantid(userEquipment.getUserId());
				chargeRecord.setStatus(0);
				chargeRecordService.insetRecord(chargeRecord);
			}
			hashMap.put("wolfcode", "1000");
			return CommUtil.responseBuildInfo(200, "支付请求成功", hashMap);
		} catch (Exception e) {
		    e.printStackTrace();
		    hashMap.put("wolfcode", "1001");
		    return CommUtil.responseBuildInfo(201, "支付请求失败", hashMap);
		}
	}


	
	/**
	 * 异步回调
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/notify_url1", method = RequestMethod.POST)
	@ResponseBody
	public String notify_url1() throws Exception {
		// 获取支付宝POST过来反馈信息
		Map<String, String> params1 = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params1.put(name, valueStr);
			System.out.println("返回参数名:"+name +"     返回参数值:"+valueStr);
		}
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 商户订单号
		System.out.println("params1   "+params1);
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
		// 支付宝交易号

		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

		// 交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
		String body = CommUtil.toString(params1.get("body"));
		
		logger.info("支付宝回调订单号：" + out_trade_no);

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		// 计算得出通知验证结果
		// boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String
		// publicKey, String charset, String sign_type)
		boolean verify_result = AlipaySignature.rsaCheckV1(params1, AlipayConfig.ALIPAY_PUBLIC_KEY,
				AlipayConfig.CHARSET, "RSA2");
//		boolean verify_result = AlipaySignature.rsaCheckV1(params1, AlipayConfig.APPLETALIPAY_PUBLIC_NEWKEY,
//				AlipayConfig.CHARSET, "RSA2");
		if (verify_result) {// 验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码

			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

			if (trade_status.equals("TRADE_FINISHED")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				// 如果有做过处理，不执行商户的业务程序

				// 注意：
				// 如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
				// 如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
			} else if (trade_status.equals("TRADE_SUCCESS")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				// 如果有做过处理，不执行商户的业务程序
//				if(body.equals("充电")){
//						
//				}else if(body.equals("脉冲")){
//					
//				}else if(body.equals("离线卡")){
//					
//				}else if(body.equals("在线卡")){
//					
//				}else if(body.equals("钱包")){
//					
//				}
				List<ChargeRecord> chargeRecordlist = chargeRecordService.getOrderByOrdernum(out_trade_no);
				if (chargeRecordlist != null && chargeRecordlist.size() > 0) {
					ChargeRecord chargeRecord = chargeRecordlist.get(0);
					String code = chargeRecord.getEquipmentnum();
					try {
						String money1 = String.valueOf(chargeRecord.getExpenditure() * 100);
						int idx = money1.lastIndexOf(".");
						Short money = Short.valueOf(money1.substring(0, idx));
						Byte port = Byte.valueOf(chargeRecord.getPort().toString());
						Short time = Short.valueOf(chargeRecord.getDurationtime());
						Short elec = Short.valueOf(chargeRecord.getQuantity() + "");
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("status", 1);
						params.put("begintime", StringUtil.toDateTime());
						params.put("ordernum", out_trade_no);
						chargeRecordService.updateByOrdernum(params);
						Equipment equipment = equipmentService.getEquipmentById(code);
//						if ("07".equals(equipment.getHardversion())) {
//							WolfHttpRequest.sendNewChargePaydata(port, time, money / 10 + "", chargeRecord.getQuantity() + "", code, 1, 0);
//						} else {
//							WolfHttpRequest.sendChargePaydata(port, time, money / 10 + "", chargeRecord.getQuantity() + "", code, 0);
//						}
					} catch (Exception e) {
						logger.warn("设备编号：--" + code + "--设备端口：--" + chargeRecord.getPort() + "--充电异常" + e.getMessage());
					}
					if (chargeRecord.getStatus() == null || chargeRecord.getStatus() == 0) {
						//修改本地数据
						Integer uid = CommUtil.toInteger(chargeRecord.getUid());
						Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
						Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
						Integer orderid = CommUtil.toInteger(chargeRecord.getId());
						String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
						Date time = new Date();
						String strtime = CommUtil.toDateTime(time);
						Integer paysource = MerchantDetail.CHARGESOURCE;
						Integer paytype = MerchantDetail.ALIPAY;
						Integer paystatus = MerchantDetail.NORMAL;
						Integer type = 1;
						Double mermoney = 0.00;
						equipmentService.insertRealRecord(orderid, 0, merid, code, chargeRecord.getPort(), 1, Integer.parseInt(chargeRecord.getDurationtime()), chargeRecord.getQuantity(), 0, -1.0);
//						messagenotification( first, uid, merid, paysource, paymoney, strtime, status, remark,  orderid, ordernum);
						Map<String, Object> usequmap = userEquipmentService.getEquipToUserByCode(code);
						String devicehard = CommUtil.toString(usequmap.get("hardversion"));
						Integer aid = CommUtil.toInteger(usequmap.get("aid"));
						Double partmoney = 0.00;
						try {
							List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
							if(aid!=0) partInfo = areaService.inquirePartnerInfo(aid, 2);
							//分成计算
							Map<String, Object> percentmap = percentCalculateDispose(partInfo, merid, paymoney);
							partmoney = CommUtil.toDouble(percentmap.get("partmoney"));
							mermoney = CommUtil.toDouble(percentmap.get("mermoney"));
							List<Map<String, Object>> percentinfo = (List<Map<String, Object>>) percentmap.get("percentinfo");
							String percentresult = CommUtil.toString(JSON.toJSON(percentinfo));
							tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, code, 1, 5, 1, devicehard, percentresult);
							for(Map<String, Object> item : percentinfo){
								Integer mercid = CommUtil.toInteger(item.get("merid"));
								Integer partid = CommUtil.toInteger(item.get("partid"));
								//Double percent = CommUtil.toDouble(item.get("percent"));
								Double money = CommUtil.toDouble(item.get("money"));
								if(mercid.equals(0)) mercid = partid;
								merEearningCalculate( mercid, money, type, ordernum, time, paysource, paytype, paystatus);
							}
							addAPartEarn(code, paymoney, merid, aid,1,1);
//							}
						} catch (Exception e) {
							logger.warn("小区修改余额错误===" + e.getMessage());
						}
//						addAPartEarn(code, consume, userEquipment.getUserId(), equipment.getAid(),1,1);
					}
					// orderService.updateStatusByOrdernum(out_trade_no, 1);
				// 注意：
				// 如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
				} else {
					InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(out_trade_no);
					OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(out_trade_no);
					OnlineCardRecord onlineCard = onlineCardRecordService.selectRecordByOrdernum(out_trade_no, 10, 1);
					Money moneyorder = moneyService.queryMoneyByOrdernum(out_trade_no);
					if (inCoins != null) {
						if (inCoins.getStatus() == null || inCoins.getStatus() == 0){
							try {
								String money = String.valueOf(inCoins.getMoney());
								int idx = money.lastIndexOf(".");
								String total_fee = money.substring(0, idx);
//								WolfHttpRequest.sendIncoinsPaydata(inCoins.getEquipmentnum(), inCoins.getPort(), Byte.parseByte(total_fee));
								inCoinsService.updateInCoinsStatusAndRecycletype(out_trade_no, (byte) 1);
							} catch (Exception e) {
								logger.error("投币异常" + e.getMessage() + e.getStackTrace()[0].getLineNumber());
							}
							String ordernum = CommUtil.toString(inCoins.getOrdernum());
							String devicenum = CommUtil.toString(inCoins.getEquipmentnum());
							Integer uid = CommUtil.toInteger(inCoins.getUid());
							Integer merid = CommUtil.toInteger(inCoins.getMerchantid());
							Double paymoney = CommUtil.toDouble(inCoins.getMoney());
							Integer orderid = CommUtil.toInteger(inCoins.getId());
							Date time = new Date();
							String strtime = CommUtil.toDateTime(time);
							Integer paysource = MerchantDetail.INCOINSSOURCE;
							Integer paytype = MerchantDetail.ALIPAY;
							Integer paystatus = MerchantDetail.NORMAL;
							Integer type = 1;
							Double mermoney = 0.00;
//							messagenotification( first, uid, merid, paysource, paymoney, strtime, status, remark,  orderid, ordernum);
							
							Map<String, Object> usequmap = userEquipmentService.getEquipToUserByCode(devicenum);
							String devicehard = CommUtil.toString(usequmap.get("hardversion"));
						    Integer aid = CommUtil.toInteger(usequmap.get("aid"));
							String resultli = null;
							Double partmoney = 0.00;
							try {
								List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
								if(aid!=0) partInfo = areaService.inquirePartnerInfo(aid, 2);
								//分成计算
								Map<String, Object> percentmap = percentCalculateDispose(partInfo, merid, paymoney);
								partmoney = CommUtil.toDouble(percentmap.get("partmoney"));
								mermoney = CommUtil.toDouble(percentmap.get("mermoney"));
								List<Map<String, Object>> percentinfo = (List<Map<String, Object>>) percentmap.get("percentinfo");
								String percentresult = CommUtil.toString(JSON.toJSON(percentinfo));
								tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 2, 5, 1, devicehard, percentresult);
								for(Map<String, Object> item : percentinfo){
									Integer mercid = CommUtil.toInteger(item.get("merid"));
									Integer partid = CommUtil.toInteger(item.get("partid"));
									//Double percent = CommUtil.toDouble(item.get("percent"));
									Double money = CommUtil.toDouble(item.get("money"));
									if(mercid.equals(0)) mercid = partid;
									merEearningCalculate( mercid, money, type, ordernum, time, paysource, paytype, paystatus);
								}
								addAPartEarn(devicenum, paymoney, merid, aid, 1, 1);
							} catch (Exception e) {
								logger.warn("小区修改余额错误===" + e.getMessage());
							}
						}
					} else if (offlineCard != null) {
						if (offlineCard.getStatus() == null || offlineCard.getStatus() == 0) {
							offlineCard.setStatus(1);
							offlineCardService.updateChargeMoneyOfflineCardRecordPaytype(offlineCard);
						try {
							String cardID = offlineCard.getCardID();
							int parseLong = (int) Long.parseLong(cardID, 16);
							String accountmoney = String.valueOf(offlineCard.getAccountmoney() * 10);
							String money = accountmoney.substring(0, accountmoney.indexOf("."));
							short parseShort = Short.parseShort(money);
//							WolfHttpRequest.sendOfflineCardPaydata(offlineCard.getEquipmentnum(), cardID, parseShort, (byte) 1);
						} catch (Exception e) {
							logger.error("支付宝---离线卡充值异常" + e.getMessage() + e.getStackTrace()[0].getLineNumber());
						}
							//修改本地数据
							String ordernum = CommUtil.toString(offlineCard.getOrdernum());
							Integer uid = CommUtil.toInteger(offlineCard.getUid());
							Integer merid = CommUtil.toInteger(offlineCard.getMerchantid());
							Double paymoney = CommUtil.toDouble(offlineCard.getChargemoney());//实际付款金额
							Integer orderid = CommUtil.toInteger(offlineCard.getId());
							String devicenum = CommUtil.toString(offlineCard.getEquipmentnum());
							Date time = new Date();
							String strtime = CommUtil.toDateTime(time);
							Integer paysource = MerchantDetail.OFFLINESOURCE;
							Integer paytype = MerchantDetail.ALIPAY;
							Integer paystatus = MerchantDetail.NORMAL;
							Integer type = 1;
							Double mermoney = 0.00;
							String first = "";
							String remark = "";
							String status = "付款成功,开始充电。";
//							messagenotification( first, uid, merid, paysource, paymoney, strtime, status, remark,  orderid, ordernum);
							Map<String, Object> usequmap = userEquipmentService.getEquipToUserByCode(devicenum);
							String ramrk = CommUtil.toString(usequmap.get("hardversion"));
							Integer aid = CommUtil.toInteger(usequmap.get("aid"));
							String resultli = null;
							Double partmoney = 0.00;
							try {
								List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
								if(aid!=0) partInfo = areaService.inquirePartnerInfo(aid, 2);
								//分成计算
								Map<String, Object> percentmap = percentCalculateDispose(partInfo, merid, paymoney);
								partmoney = CommUtil.toDouble(percentmap.get("partmoney"));
								mermoney = CommUtil.toDouble(percentmap.get("mermoney"));
								List<Map<String, Object>> percentinfo = (List<Map<String, Object>>) percentmap.get("percentinfo");
								String percentresult = CommUtil.toString(JSON.toJSON(percentinfo));
								tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 3, 5, 1, ramrk, percentresult);
								for(Map<String, Object> item : percentinfo){
									Integer mercid = CommUtil.toInteger(item.get("merid"));
									Integer partid = CommUtil.toInteger(item.get("partid"));
									//Double percent = CommUtil.toDouble(item.get("percent"));
									Double money = CommUtil.toDouble(item.get("money"));
									if(mercid.equals(0)) mercid = partid;
									merEearningCalculate( mercid, money, type, ordernum, time, paysource, paytype, paystatus);
								}
								addAPartEarn(devicenum, paymoney, merid, aid, 1, 1);
							} catch (Exception e) {
								logger.warn("小区修改余额错误===" + e.getMessage());
							}
//							String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=2&id="+offlineCard.getId();
//							String phone = getTempPhone(offlineCard.getEquipmentnum(), offlineCard.getMerchantid(), 1);
//							paychargesendmsg("您好，充值成功，欢迎您的使用", offlineCard.getUid(), phone, urltem,offlineCard.getChargemoney());
						}
					}else if(onlineCard!=null){
						if (onlineCard.getStatus() == null || onlineCard.getStatus() == 0) {
							onlineCardRecordService.updateonlinestatus(out_trade_no, (byte) 1);
							if (onlineCard.getStatus() == 0) {
								OnlineCard online = onlineCardService.selectOnlineCardByCardID(onlineCard.getCardID());
								String ordernum = CommUtil.toString(onlineCard.getOrdernum());
								Integer merid = CommUtil.toInteger(onlineCard.getMerid());
								Double paymoney = CommUtil.toDouble(onlineCard.getMoney());
								Double accountmoney = CommUtil.toDouble(onlineCard.getAccountmoney());
								Integer uid = CommUtil.toInteger(onlineCard.getUid());
								Integer orderid = CommUtil.toInteger(onlineCard.getId());
								String cardID = CommUtil.toString(onlineCard.getCardID());
								Double mermoney = 0.00;
								//Integer merid = online.getMerid();
								OnlineCard onlines = new OnlineCard();
								onlines.setId(online.getId());
								onlines.setMoney((accountmoney*100 + online.getMoney()*100)/100);
								onlineCardService.updateOnlineCard(onlines);
								try {
									if (merid != null && merid != 0) {
										// 在线卡充值金额处理
										Date time = new Date();
										String strtime = CommUtil.toDateTime(time);
										Integer paysource = MerchantDetail.ONLINESOURCE;
										Integer paytype = MerchantDetail.ALIPAY;
										Integer paystatus = MerchantDetail.NORMAL;
										Integer aid = CommUtil.toInteger(online.getAid());
										Integer type = 1;
										String resultli = null;
										Double partmoney = 0.00;
										try {
											List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
											if(aid!=0) partInfo = areaService.inquirePartnerInfo(aid, 2);
											//分成计算
											Map<String, Object> percentmap = percentCalculateDispose(partInfo, merid, paymoney);
											partmoney = CommUtil.toDouble(percentmap.get("partmoney"));
											mermoney = CommUtil.toDouble(percentmap.get("mermoney"));
											List<Map<String, Object>> percentinfo = (List<Map<String, Object>>) percentmap.get("percentinfo");
											String percentresult = CommUtil.toString(JSON.toJSON(percentinfo));
//											tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, cardID, 5, 3, 1, null, resultli);
											tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, cardID, 5, 5, 1, null, percentresult);
											for(Map<String, Object> item : percentinfo){
												Integer mercid = CommUtil.toInteger(item.get("merid"));
												Integer partid = CommUtil.toInteger(item.get("partid"));
												//Double percent = CommUtil.toDouble(item.get("percent"));
												Double money = CommUtil.toDouble(item.get("money"));
												if(mercid.equals(0)) mercid = partid;
												merEearningCalculate( mercid, money, type, ordernum, time, paysource, paytype, paystatus);
											}
											addAPartEarn(null, paymoney, merid, aid, 3, 1);
											
										} catch (Exception e) {
											logger.warn("小区修改余额错误===" + e.getMessage());
										}
									} 
								} catch (Exception e) {
									logger.info(e.getMessage() + "支付宝在线卡充值-" + ByteUtils.getLineNumber(e));
								}
							}
						}
					}else if(moneyorder!=null){
						if (moneyorder.getPaytype() == 9) {
							try {
								//修改本地数据
								String ordernum = CommUtil.toString(moneyorder.getOrdernum());
								Integer uid = CommUtil.toInteger(moneyorder.getUid());
								Double paymoney = CommUtil.toDouble(moneyorder.getMoney());
								Double sendmoney = CommUtil.toDouble(moneyorder.getSendmoney());
								Double accountmoney = CommUtil.toDouble(moneyorder.getTomoney());
								Double balancemoney = CommUtil.toDouble(moneyorder.getBalance());
								Double topupbalance = CommUtil.toDouble(moneyorder.getTopupbalance());
								Double givebalance = CommUtil.toDouble(moneyorder.getGivebalance());
								
								Integer orderid = CommUtil.toInteger(moneyorder.getId());
								Date time = new Date();
								String strtime = CommUtil.toDateTime(time);
								Integer paysource = MerchantDetail.WALLETSOURCE;
								Integer paytype = MerchantDetail.ALIPAY;
								Integer paystatus = MerchantDetail.NORMAL;
								Integer type = 1;
								Double mermoney = 0.00;
								String first = "";
								String remark = "";
								String status = "付款成功,开始充电。";
								User touuser = new User();
								if(uid!=0) touuser = userService.selectUserById(uid);
								Integer merid = CommUtil.toInteger(touuser.getMerid());
								Integer aid = CommUtil.toInteger(touuser.getAid());
								//-----------------------------------------------
								Double userbalance = CommUtil.toDouble(touuser.getBalance());
								Double usersendmoney = CommUtil.toDouble(touuser.getSendmoney());
								
								Double opermoney = CommUtil.toDouble(paymoney);
								Double opersendmoney = CommUtil.toDouble(sendmoney);
								Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
								Double mtopupbalance = CommUtil.addBig(opermoney, userbalance);
								Double mgivebalance = CommUtil.addBig(opersendmoney, usersendmoney);
								Double operbalance = CommUtil.addBig(mtopupbalance, mgivebalance);
								
								//1、充值记录修改
								moneyorder.setBalance(operbalance);
								moneyorder.setStatus(1);
								moneyService.updateMoney(moneyorder);
								//2、修改用户金额
								userService.updateBalanceByOpenid(mtopupbalance, mgivebalance, touuser.getOpenid(),null);
								String mremark = moneyorder.getRemark();
								//3、添加钱包明细
								generalDetailService.insertGenDetail(uid, merid, opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, ordernum, time, 1, mremark);
								
//								String phone = getTempPhone(CommUtil.toString(meruser.getAid()), meruser.getMerid(), 3);
//								String detailurl = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=4&id="+moneyorder.getId();
//								TempMsgUtil.paychargesendmsg("您好，充值成功。", touruser.getId(), phone, detailurl, moneyorder.getMoney());
								String resultli = null;
								Double partmoney = 0.00;
								try {
									List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
									if(aid!=0) partInfo = areaService.inquirePartnerInfo(aid, 2);
									//分成计算
									Map<String, Object> percentmap = percentCalculateDispose(partInfo, merid, paymoney);
									partmoney = CommUtil.toDouble(percentmap.get("partmoney"));
									mermoney = CommUtil.toDouble(percentmap.get("mermoney"));
									List<Map<String, Object>> percentinfo = (List<Map<String, Object>>) percentmap.get("percentinfo");
									String percentresult = CommUtil.toString(JSON.toJSON(percentinfo));
									tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, "钱包", 4, 5, 1, null, percentresult);
									for(Map<String, Object> item : percentinfo){
										Integer mercid = CommUtil.toInteger(item.get("merid"));
										Integer partid = CommUtil.toInteger(item.get("partid"));
										//Double percent = CommUtil.toDouble(item.get("percent"));
										Double money = CommUtil.toDouble(item.get("money"));
										if(mercid.equals(0)) mercid = partid;
										merEearningCalculate( mercid, money, type, ordernum, time, paysource, paytype, paystatus);
									}
									addAPartEarn(null, paymoney, merid, aid, 2, 1);
								} catch (Exception e) {
									logger.warn("小区修改余额错误===" + e.getMessage());
								}
							} catch (Exception e) {
								logger.info(e.getMessage() + "支付宝钱包充值-" + ByteUtils.getLineNumber(e));
								e.printStackTrace();
							}
						}
					}
				}
			}
			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			System.out.println("支付成功订单号=======" + out_trade_no);
			System.out.println("success"); // 请不要修改或删除
			return "success";
			//////////////////////////////////////////////////////////////////////////////////////////
		} else {// 验证失败
			System.out.println("fail");
			return "fail";
		}
	}
	
	@RequestMapping("/getappauthtoken")
	public void getappauthtoken() {
		String url = "";
		url = request.getScheme() +"://" + request.getServerName()
		                        + request.getServletPath();
        if (request.getQueryString() != null){
            url += "?" + request.getQueryString();
        }
		System.out.println("访问链接===" + url);
		String auth_code = request.getParameter("app_auth_code");
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.FUWUPID,
				AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,
				AlipayConfig.SIGNTYPE);
		AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
		request.setBizContent("{" +
		"\"grant_type\":\"authorization_code\"," +
		"\"code\":\"" + auth_code + "\"" +
		"  }");
		try {
			AlipayOpenAuthTokenAppResponse response = alipayClient.execute(request);
			if (response.isSuccess()) {
				System.out.println("调用成功");
				System.out.println("json===" + JSON.toJSONString(response));
				String appAuthToken = response.getAppAuthToken();
				AlipayConfig.APP_AUTH_TOKEN = appAuthToken;
			} else {
				System.out.println("调用失败");
			}
		} catch (AlipayApiException e) {
		    //处理异常
		    e.printStackTrace();
		}
	}
	

	/**
	 * 向设备发送信息判断是否在线，并查询设备是否被绑定
	 * 
	 * @param code
	 * @param model
	 * @return
	 */
	@RequestMapping("/chargeparam")
	public String chargeparam(@RequestParam(value = "equcode", defaultValue = "000001") String equcode, Model model) {
		String url = "";
		url = request.getScheme() +"://" + request.getServerName()
		                        + request.getServletPath();
        if (request.getQueryString() != null){
            url += "?" + request.getQueryString();
        }
		System.out.println("访问链接===" + url);
		String auth_code = request.getParameter("auth_code");
		// 商户的参数
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.NEWFUWUAPPID,
				AlipayConfig.ALIPAY_PRIVATE_NEWKEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.APPLETALIPAY_PUBLIC_NEWKEY,
				AlipayConfig.SIGNTYPE); 
		AlipaySystemOauthTokenRequest requestAlipay = new AlipaySystemOauthTokenRequest();
		requestAlipay.setCode(auth_code);
		requestAlipay.setGrantType("authorization_code");
//		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.MERCHPID,
//				AlipayConfig.RSA_PRIVATE_KEY2, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY2,
//				AlipayConfig.SIGNTYPE);
//		AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
//		request.setBizContent("{" +
//		"\"grant_type\":\"authorization_code\"," +
//		"\"code\":\"" + auth_code + "\"" +
//		"  }");
		try {
		    AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(requestAlipay);
			System.out.println(" ORIGIN 对象   "+oauthTokenResponse);
//			AlipayOpenAuthTokenAppResponse response = alipayClient.execute(request);
			if (oauthTokenResponse.isSuccess()) {
				System.out.println("调用成功");
				System.out.println(" ORIGIN 调用   "+oauthTokenResponse);
				System.out.println("userid===" + oauthTokenResponse.getUserId());
				model.addAttribute("userid", oauthTokenResponse.getUserId());
			} else {
				System.out.println("调用失败");
			}
		} catch (AlipayApiException e) {
		    //处理异常
		    e.printStackTrace();
		}
		model.addAttribute("code", equcode);
		// 获取设备号
		String devicenum = CommUtil.toString(equcode);
		// 根据code查询出相应的设备信息
		Equipment equipment = equipmentService.getEquipmentAndAreaById(devicenum);
		equipment = equipment == null ? new Equipment() : equipment;
		
		//设备信息
		String devicename = CommUtil.toString(equipment.getRegistTime());
		Integer devicestate = CommUtil.toInteger(equipment.getState());
		Integer devicebindtype = CommUtil.toInteger(equipment.getBindtype());
		
		String expiretime = CommUtil.toString(equipment.getExpirationTime());
		String hardversion = CommUtil.toString(equipment.getHardversion());
		String softversionnum = CommUtil.toString(equipment.getSoftversionnum());
		
		Integer tempid = CommUtil.toInteger(equipment.getTempid());
		Integer deviceareaid = CommUtil.toInteger(equipment.getAid());
		Integer several = CommUtil.toInteger(equipment.getSeveral());
		Date expirationTime= equipment.getExpirationTime();
		// 根据设备信息查询小区信息
		Area area = areaService.selectByIdArea(deviceareaid);
		area = area == null ? new Area() : area;
		Integer areaid = CommUtil.toInteger(area.getId());
		String areaname = CommUtil.toString(area.getName());
		//商户信息
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
		
		Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, dealid, devicenum, hardversion);
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
		// ORIGIN 判断是否支持支付宝临时充电
		if(ifalipay.equals(2)){
//			if(hardversion.equals("01") || hardversion.equals("02") || hardversion.equals("05") || hardversion.equals("06")){
//				return "alipay/errorChargeInfo";
//			}
			if(!hardversion.equals("03") && !hardversion.equals("04")){
				return "alipay/errorChargeInfo";
			}
		}
		
		//设备到期时间不为空且当前时间大于到期时间
		if(expirationTime != null && new Date().after(expirationTime)){
			model.addAttribute("errorinfo", "对不起，当前设备已到期");
			model.addAttribute("code", equcode);
			model.addAttribute("time", expirationTime);
			//提示用户设备到期更换设备
			return "chargeporterror";
		}
		if(devicestate.equals(0) || devicebindtype.equals(0) || dealid.equals(0)){//判断设备状态是否正常 和是否绑定
			return "/equipment/equipmentoffline";// 不在线
		} else {
			if ("03".equals(hardversion)) {
//				SendMsgUtil.send_0x82(equcode);
				if(tempson.size()>=2){
					model.addAttribute("defaultTemp", tempson.get(1).getId());
				}else{
					model.addAttribute("defaultTemp", tempson.get(0).getId());
				}
				model.addAttribute("nowtime", System.currentTimeMillis() + "");
				return "alipay/inCoins";
			} else if ("04".equals(hardversion)) {
//				SendMsgUtil.send_0x22(equcode, 0, (short) 0, (byte) 2);
				model.addAttribute("bindtype", 1);
				model.addAttribute("nowtime", System.currentTimeMillis() + "");
				return "/alipay/offlineCard";
			} else {
//				SendMsgUtil.send_15(equcode);
				model.addAttribute("nowtime", System.currentTimeMillis());
				// 查询设备绑定用户给定的收费模板
//				temp(equcode, model);
				Map<String, Object> defaulte = tempDefaultObje(tempson);
				model.addAttribute("defaultchoose", defaulte.get("defaultchoose"));
				model.addAttribute("defaultindex", defaulte.get("defaultindex"));
				return "alipay/chargeallport";
			}
		}
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
	 * 支付宝扫描设备端口充电
	 * @param codeAndPort 设备端口号
	 * @param model
	 * @return {@link String}
	 */
	@RequestMapping(value = "/chargePort")
	public String chargePort(@RequestParam(value = "codeAndPort", defaultValue = "000001") String codeAndPort, Model model) {
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
			
			//根据设备号查询用户设备信息
			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(equcode);
			userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
			Integer dealid = CommUtil.toInteger(userEquipment.getUserId());
			User dealeruser = new User();
			if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
			String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
			String servephone = CommUtil.toString(dealeruser.getServephone());

			//设备获取使用模板
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
			//设备到期时间不为空且当前时间大于到期时间
			if(expirationTime != null && new Date().after(expirationTime)){
				model.addAttribute("errorinfo", "对不起，当前设备已到期");
				model.addAttribute("time", expirationTime);
				//提示用户设备到期更换设备
				return "chargeporterror";
			}
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
//				SendMsgUtil.send_15(equcode);
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


	/**
	 * 根据用户所选取的参数，创建预订单（智慧款充电设备）
	 * 
	 * @param code
	 * @param chargeparam
	 * @param portchoose
	 * @return
	 */
	@RequestMapping(value = "/createOrder", method = RequestMethod.POST)
	public String createOrder(String code, Integer chargeparam, Byte portchoose) {
		String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + code + portchoose;
		UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
		ChargeRecord chargeRecord = new ChargeRecord();
		chargeRecord.setEquipmentnum(code);// 设备号/机位号
		chargeRecord.setOrdernum(format);// 订单号
		int port = portchoose;
		chargeRecord.setPort(port);// 端口号
		TemplateSon templateSon = templateService.getInfoTemplateOne(chargeparam);
		chargeRecord.setExpenditure(templateSon.getMoney());// 金额
		chargeRecord.setDurationtime(templateSon.getChargeTime().toString());// 时长
		chargeRecord.setQuantity(templateSon.getChargeQuantity());// 电量
		chargeRecord.setPaytype(chargeRecord.ALIPAYPAYTYPE);// 支付类型
		chargeRecord.setMerchantid(userEquipment.getUserId());
		chargeRecord.setStatus(0);
		chargeRecordService.insetRecord(chargeRecord);

		return "redirect:/alipay/pay?ordernum=" + format;
	}
	
	/**
	 * 支付宝当面付充电
	 * @param uid
	 * @return
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/userchargepay")
	@ResponseBody
	public Map<String, String> userchargepay(String uid) throws IOException {
		// 获取参数
		String code = request.getParameter("code");
		String port = request.getParameter("port");
		String tempid = request.getParameter("tempid");
		TemplateSon templateSon = templateService.getInfoTemplateOne(Integer.parseInt(tempid));
		Double tempmoney = templateSon.getMoney();
		String ordernum = HttpRequest.createOrdernum(6);
		Map<String, String> hashMap = new HashMap<>();
		// 订单名称，必填
		String subject = "自助充电平台（扫码充电）";
		// 付款金额，必填
		String total_amount = new String(String.format("%.2f", tempmoney).getBytes("ISO-8859-1"), "UTF-8");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.create.
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.FUWUPID, AlipayConfig.RSA_PRIVATE_KEY3, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY3, "RSA2");
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。
		request.setBizContent("{" +
				"\"out_trade_no\":\"" + ordernum + "\"," +
				"\"subject\":\"" + subject + "\"," +
				"\"total_amount\":" + total_amount + "," +
				"\"buyer_id\":\"" + uid + "\"," +
				"\"extend_params\":{" +
				"\"sys_service_provider_id\":\"" + AlipayConfig.XIEZUOPID + "\"" +
				"    }" +
				"  }");
		request.setNotifyUrl(AlipayConfig.NOTIFY_URL);
		try {
		    //使用的是execute
		    AlipayTradeCreateResponse response = alipayClient.execute(request,null,AlipayConfig.APP_AUTH_TOKEN);
		    String trade_no = response.getTradeNo();//获取返回的tradeNO。
		    hashMap.put("trade_no", trade_no);
		    System.out.println("支付宝订单号为===" + trade_no);
			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
			//添加充电记录
			ChargeRecord chargeRecord = new ChargeRecord();
			chargeRecord.setEquipmentnum(code);// 设备号/机位号
			chargeRecord.setOrdernum(ordernum);// 订单号
			chargeRecord.setPort(Integer.parseInt(port));// 端口号
			chargeRecord.setExpenditure(templateSon.getMoney());// 金额
			chargeRecord.setDurationtime(templateSon.getChargeTime().toString());// 时长
			chargeRecord.setQuantity(templateSon.getChargeQuantity());// 电量
			chargeRecord.setPaytype(chargeRecord.ALIPAYPAYTYPE);// 支付类型
			chargeRecord.setMerchantid(userEquipment.getUserId());
			chargeRecord.setStatus(0);
			chargeRecordService.insetRecord(chargeRecord);
			hashMap.put("wolfcode", "1");
		    return hashMap;
		} catch (AlipayApiException e) {
		    e.printStackTrace();
		    hashMap.put("wolfcode", "0");
		    return hashMap;
		}
	}
	
	
	//测试服务商为商家发起支付
	/**
	 * alipay 脉冲投币当面付
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/userincoinspay")
	@ResponseBody
	public Map<String, String> userincoinspay(String uid) throws IOException {
		String code = request.getParameter("code");
		String port = request.getParameter("port");
		String tempid = request.getParameter("chargeparam");
		TemplateSon templateSon = templateService.getInfoTemplateOne(Integer.parseInt(tempid));
		Double remark = templateSon.getRemark();
		Double money = templateSon.getMoney();
		String ordernum = HttpRequest.createOrdernum(6);
		Map<String, String> hashMap = new HashMap<>();
		// 订单名称，必填
		String subject = "自助充电平台（扫码投币）";
		// 付款金额，必填
		String total_amount = new String(String.format("%.2f", money).getBytes("ISO-8859-1"), "UTF-8");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.create.
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.FUWUPID, AlipayConfig.RSA_PRIVATE_KEY3, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY3, "RSA2");
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。
		request.setBizContent("{" +
				"\"out_trade_no\":\"" + ordernum + "\"," +
				"\"subject\":\"" + subject + "\"," +
				"\"total_amount\":" + total_amount + "," +
				"\"buyer_id\":\"" + uid + "\"," +
				"\"extend_params\":{" +
				"\"sys_service_provider_id\":\"" + AlipayConfig.XIEZUOPID + "\"" +
				"    }" +
				"  }");
		request.setNotifyUrl(AlipayConfig.NOTIFY_URL);
		try { 
			//使用的是execute
			AlipayTradeCreateResponse response = alipayClient.execute(request,null,AlipayConfig.APP_AUTH_TOKEN);
			String trade_no = response.getTradeNo();//获取返回的tradeNO。
			hashMap.put("trade_no", trade_no);
		    logger.info("订单===" + trade_no);
			try {
				logger.info("支付的钱===="+money);
				inCoinsService.insertInCoinsRecord(ordernum, code, Byte.parseByte(port), null, money,
						(Byte.parseByte(WxPayController.doubleToString(remark))), (byte) 2, (byte) 0);
				hashMap.put("wolfcode", "1");
			} catch (Exception e) {
				hashMap.put("wolfcode", "0");
			}
			return hashMap;
		} catch (AlipayApiException e) {
			e.printStackTrace();
			hashMap.put("wolfcode", "0");
			return hashMap;
		}
	}
	
	/**
	 * alipay 离线充值机当面付
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/userofflinecardpay")
	@ResponseBody
	public Map<String, String> userofflinecardpay(String uid) throws IOException {
		String code = request.getParameter("code");
		String tempid = request.getParameter("cardSurp");
		String cardnum = request.getParameter("cardnum");
		TemplateSon templateSon = templateService.getInfoTemplateOne(Integer.parseInt(tempid));
		Double remark = templateSon.getRemark();
		Double money = templateSon.getMoney();
		String ordernum = HttpRequest.createOrdernum(6);
		Map<String, String> hashMap = new HashMap<>();
		// 订单名称，必填
		String subject = "自助充电平台（离线卡充值）";
		// 付款金额，必填
		String total_amount = new String(String.format("%.2f", money).getBytes("ISO-8859-1"), "UTF-8");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.create.
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.FUWUPID, AlipayConfig.RSA_PRIVATE_KEY3, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY3, "RSA2");
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。
		request.setBizContent("{" +
				"\"out_trade_no\":\"" + ordernum + "\"," +
				"\"subject\":\"" + subject + "\"," +
				"\"total_amount\":" + total_amount + "," +
				"\"buyer_id\":\"" + uid + "\"," +
				"\"extend_params\":{" +
				"\"sys_service_provider_id\":\"" + AlipayConfig.XIEZUOPID + "\"" +
				"    }" +
				"  }");
		request.setNotifyUrl(AlipayConfig.NOTIFY_URL);
		try {
			//使用的是execute
			AlipayTradeCreateResponse response = alipayClient.execute(request,null,AlipayConfig.APP_AUTH_TOKEN);
			String trade_no = response.getTradeNo();//获取返回的tradeNO。
			hashMap.put("trade_no", trade_no);
			System.out.println("支付宝订单号为===" + trade_no);
			try {
				offlineCardService.insertChargeMoneyOfflineCardRecord(ordernum, code, cardnum, templateSon.getMoney(),
						templateSon.getRemark(), 1, 2, null, 0);
				hashMap.put("wolfcode", "1");
			} catch (Exception e) {
				hashMap.put("wolfcode", "0");
			}
			return hashMap;
		} catch (AlipayApiException e) {
			e.printStackTrace();
			hashMap.put("wolfcode", "0");
			return hashMap;
		}
	}
	
	/**
	 * alipay 离线充值机当面付
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/useronlinecardpay")
	@ResponseBody
	public Map<String, String> useronlinecardpay(String uid) throws IOException {
		String tempid = request.getParameter("tempid");
		String cardnum = request.getParameter("cardnum");
		OnlineCard card = onlineCardService.selectOnlineCardByCardID(cardnum);
		Integer relevawalt = card.getRelevawalt();
		Integer touuid = CommUtil.toInteger(uid);
		if(touuid==0) touuid = CommUtil.toInteger(card.getUid());
		if(card!=null && card.getRelevawalt()==1){
			User touser = userService.selectUserById(card.getUid());
			if(touser!=null){
				card.setMoney(touser.getBalance());
				card.setSendmoney(touser.getSendmoney());
				touuid = CommUtil.toInteger(touser.getId());
			}
		}
		TemplateSon templateSon = templateService.getInfoTemplateOne(Integer.parseInt(tempid));
		//============================================================================
		Double topupmoney = CommUtil.toDouble(card.getMoney());//充值金额
		Double sendmoney = CommUtil.toDouble(card.getSendmoney());//赠送金额
		//Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额
		//==========================================================================
		Double opermoney = CommUtil.toDouble(templateSon.getRemark());//操作总额
		Double opertopupmoney = CommUtil.toDouble(templateSon.getMoney());//操作充值金额
		Double opersendmoney = CommUtil.subBig(opermoney, opertopupmoney);//操作赠送金额
		//==========================================================================
		Double topupbalance = CommUtil.addBig(topupmoney, opertopupmoney);//充值余额
		Double sendbalance = CommUtil.addBig(sendmoney, opersendmoney);//赠送余额
		Double accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
		//==========================================================================
		Integer merid = CommUtil.toInteger(card.getMerid());
		String ordernum = HttpRequest.createOrdernum(6);
		Date date = new Date();
		Map<String, String> hashMap = new HashMap<>();
		// 订单名称，必填
		String subject = "自助充电平台（在线卡充值）";
		// 付款金额，必填
		String total_amount = new String(String.format("%.2f", opertopupmoney).getBytes("ISO-8859-1"), "UTF-8");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.create.
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.FUWUPID, AlipayConfig.RSA_PRIVATE_KEY3, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY3, "RSA2");
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。
		request.setBizContent("{" +
				"\"out_trade_no\":\"" + ordernum + "\"," +
				"\"subject\":\"" + subject + "\"," +
				"\"total_amount\":" + total_amount + "," +
				"\"buyer_id\":\"" + uid + "\"," +
				"\"extend_params\":{" +
				"\"sys_service_provider_id\":\"" + AlipayConfig.XIEZUOPID + "\"" +
				"    }" +
				"  }");
		request.setNotifyUrl(AlipayConfig.NOTIFY_URL);
		try {
			//使用的是execute
			AlipayTradeCreateResponse response = alipayClient.execute(request,null,AlipayConfig.APP_AUTH_TOKEN);
			String trade_no = response.getTradeNo();//获取返回的tradeNO。
			hashMap.put("trade_no", trade_no);
			System.out.println("支付宝订单号为===" + trade_no);
			try {
				if(relevawalt==1){
					moneyService.payMoneys(touuid, ordernum, 7, 0, opertopupmoney, opersendmoney, opermoney, accountbalance, topupbalance, sendbalance, card.getCardID());
					moneyService.insertWalletInfo(touuid, merid, ordernum, 1, 7, 0, opertopupmoney, opersendmoney, 
							opermoney, accountbalance, topupbalance, sendbalance, new Date(), card.getCardID());
				}else{
					if (merid != null) {
						onlineCardRecordService.insertOnlineCardRecord(touuid, merid, ordernum, cardnum, null, accountbalance, 
							opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 6, 0, 1, date, 0);
					} else {
						onlineCardRecordService.insertOnlineCardRecord(touuid, null, ordernum  , cardnum, null, accountbalance, 
							opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 6, 0, 1, date, 0);
					}
				}
				hashMap.put("wolfcode", "1");
			} catch (Exception e) {
				hashMap.put("wolfcode", "0");
			}
			return hashMap;
		} catch (AlipayApiException e) {
			e.printStackTrace();
			hashMap.put("wolfcode", "0");
			return hashMap;
		}
	}

	/**
	 * 根据用户所选取的参数，创建预订单（脉冲模块）
	 * 
	 * @param code
	 * @param portchoose
	 * @return
	 */
	@RequestMapping(value = "/inCoinsCreateOrder", method = RequestMethod.POST)
	public String inCoinsCreateOrder(String code, Integer chargeparam, String port) {
		String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + code;
		TemplateSon templateSon = templateService.getInfoTemplateOne(chargeparam);
		Double money = templateSon.getMoney();
		Double remark = templateSon.getRemark();
		try {
			inCoinsService.insertInCoinsRecord(format, code, Byte.parseByte(port), null, money,
					(Byte.parseByte(WxPayController.doubleToString(remark))), (byte) 2, (byte) 0);
		} catch (Exception e) {
			return "erroruser";
		}
		return "redirect:/alipay/pay?ordernum=" + format + "&hardver=3";
	}

	/**
	 * 根据用户所选取的参数，创建预订单（离线充值机）
	 * 
	 * @param code
	 * @param portchoose
	 * @return
	 */
	@RequestMapping(value = "/offlineCreateOrder", method = RequestMethod.POST)
	public String offlineCreateOrder(String code, Integer cardSurp, String cardnum) {
		String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + code;
		TemplateSon templateSon = templateService.getInfoTemplateOne(cardSurp);
		offlineCardService.insertChargeMoneyOfflineCardRecord(format, code, cardnum, templateSon.getMoney(),
				templateSon.getRemark(), 1, 2, null, 0);
		return "redirect:/alipay/pay?ordernum=" + format + "&hardver=4";
	}

	/**
	 * alipay
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/pay")
	public void pay(String ordernum) throws IOException {
		String out_trade_no = ordernum;
		Double money = 0.0;
		String hardver = request.getParameter("hardver");
		if (hardver != null && "3".equals(hardver)) {
			InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
			money = inCoins.getMoney();
		} else if (hardver != null && "4".equals(hardver)) {
			OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
			money = offlineCard.getChargemoney();
		} else {
			List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
			money = chargeRecord.get(0).getExpenditure();
		}
		// 订单名称，必填
		String subject = ordernum;
		// 付款金额，必填
		String total_amount = new String(String.format("%.2f", money).getBytes("ISO-8859-1"), "UTF-8");
		// 商品描述，可空
		String body = new String("自助充电平台".getBytes("ISO-8859-1"), "UTF-8");
		// 超时时间 可空
		String timeout_express = "2m";
		// 销售产品码 必填
		String product_code = "QUICK_WAP_WAY";
		/**********************/
		// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
		// 调用RSA签名方式
		AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
				AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,
				AlipayConfig.SIGNTYPE);
		AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

		// 封装请求支付信息
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setOutTradeNo(out_trade_no);
		model.setSubject(subject);
		model.setTotalAmount(total_amount);
		model.setBody(body);
		model.setTimeoutExpress(timeout_express);
		model.setProductCode(product_code);
		alipay_request.setBizModel(model);
		// 设置异步通知地址
		alipay_request.setNotifyUrl(AlipayConfig.NOTIFY_URL);
		// 设置同步地址
		alipay_request.setReturnUrl(AlipayConfig.RETURN_URL);

		// form表单生产
		String form = "";
		try {
			// 调用SDK生成表单
			form = client.pageExecute(alipay_request).getBody();
			response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
			try {
				response.getWriter().write(form);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			} // 直接将完整的表单html输出到页面
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		// }
	}

	/**
	 * 异步回调
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/notify_url", method = RequestMethod.POST)
	@ResponseBody
	public String notify_url() throws Exception {
		// 获取支付宝POST过来反馈信息
		Map<String, String> params1 = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params1.put(name, valueStr);
		}
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 商户订单号

		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
		// 支付宝交易号

		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

		// 交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		// 计算得出通知验证结果
		// boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String
		// publicKey, String charset, String sign_type)
		boolean verify_result = AlipaySignature.rsaCheckV1(params1, AlipayConfig.ALIPAY_PUBLIC_KEY,
				AlipayConfig.CHARSET, "RSA2");
//		boolean verify_result = AlipaySignature.rsaCheckV1(params1, AlipayConfig.APPLETALIPAY_PUBLIC_NEWKEY,
//				AlipayConfig.CHARSET, "RSA2");
		if (verify_result) {// 验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码

			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

			if (trade_status.equals("TRADE_FINISHED")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				// 如果有做过处理，不执行商户的业务程序

				// 注意：
				// 如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
				// 如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
			} else if (trade_status.equals("TRADE_SUCCESS")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				// 如果有做过处理，不执行商户的业务程序
				List<ChargeRecord> chargeRecordlist = chargeRecordService.getOrderByOrdernum(out_trade_no);
				if (chargeRecordlist != null && chargeRecordlist.size() > 0) {
					ChargeRecord chargeRecord = chargeRecordlist.get(0);
					String code = chargeRecord.getEquipmentnum();
					try {
						String money1 = String.valueOf(chargeRecord.getExpenditure() * 100);
						int idx = money1.lastIndexOf(".");
						Short money = Short.valueOf(money1.substring(0, idx));
						Byte port = Byte.valueOf(chargeRecord.getPort().toString());
						Short time = Short.valueOf(chargeRecord.getDurationtime());
						Short elec = Short.valueOf(chargeRecord.getQuantity() + "");
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("status", 1);
						params.put("begintime", StringUtil.toDateTime());
						params.put("ordernum", out_trade_no);
						chargeRecordService.updateByOrdernum(params);
//						SendMsgUtil.send_0x14(port, (short) (money / 10), time, elec, code);// 支付完成充电开始
//						WolfHttpRequest.sendChargePaydata(port, time, money / 10 + "", chargeRecord.getQuantity() + "", code);
						Timer timer = new Timer();
						long session_id = System.currentTimeMillis();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								chargepayTask(code, (byte) port, chargeRecord.getDurationtime(), chargeRecord.getQuantity(), session_id, out_trade_no);
							}
						}, 60000);
//						Server.chargeTimerNumMap.put(session_id, 2);
//						System.out.println("定时任务已开启");
					} catch (Exception e) {
						logger.warn("设备编号：--" + code + "--设备端口：--" + chargeRecord.getPort() + "--充电异常" + e.getMessage());
					}
					if (chargeRecord.getStatus() == null || chargeRecord.getStatus() == 0) {
						//修改本地数据
						Integer uid = CommUtil.toInteger(chargeRecord.getUid());
						Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
						Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
						Integer orderid = CommUtil.toInteger(chargeRecord.getId());
						String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
						Date time = new Date();
						String strtime = CommUtil.toDateTime(time);
						Integer paysource = MerchantDetail.CHARGESOURCE;
						Integer paytype = MerchantDetail.ALIPAY;
						Integer paystatus = MerchantDetail.NORMAL;
						Integer type = 1;
						Double mermoney = 0.00;
						equipmentService.insertRealRecord(orderid, 0, merid, code, chargeRecord.getPort(), 1, Integer.parseInt(chargeRecord.getDurationtime()), chargeRecord.getQuantity(), 0, -1.0);
//						messagenotification( first, uid, merid, paysource, paymoney, strtime, status, remark,  orderid, ordernum);
						Map<String, Object> usequmap = userEquipmentService.getEquipToUserByCode(code);
						String devicehard = CommUtil.toString(usequmap.get("hardversion"));
						Integer aid = CommUtil.toInteger(usequmap.get("aid"));
						Double partmoney = 0.00;
						try {
							List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
							if(aid!=0) partInfo = areaService.inquirePartnerInfo(aid, 2);
							//分成计算
							Map<String, Object> percentmap = percentCalculateDispose(partInfo, merid, paymoney);
							partmoney = CommUtil.toDouble(percentmap.get("partmoney"));
							mermoney = CommUtil.toDouble(percentmap.get("mermoney"));
							List<Map<String, Object>> percentinfo = (List<Map<String, Object>>) percentmap.get("percentinfo");
							String percentresult = CommUtil.toString(JSON.toJSON(percentinfo));
							tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, code, paysource, paytype, paystatus, devicehard, percentresult);
							for(Map<String, Object> item : percentinfo){
								Integer mercid = CommUtil.toInteger(item.get("merid"));
								Integer partid = CommUtil.toInteger(item.get("partid"));
								//Double percent = CommUtil.toDouble(item.get("percent"));
								Double money = CommUtil.toDouble(item.get("money"));
								if(mercid.equals(0)) mercid = partid;
								merEearningCalculate( mercid, money, type, ordernum, time, paysource, paytype, paystatus);
							}
							addAPartEarn(code, paymoney, merid, aid,1,1);
//								List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
//								addAPartEarn(code, paymoney, merid, aid,1,1);
//								if (aid != null && aid != 0) {
//	//								areaService.updateAreaEarn(1, paymoney, aid,null,paymoney,null);
//									partInfo = areaService.inquirePartnerInfo(aid, 2);
//								}
//								//商户收益（分成）计算
//								Map<String, Object> listmap = partnerIncomeDispose(partInfo, merid, paymoney, ordernum, time, paysource, paytype, paystatus, type);
//								partmoney = CommUtil.toDouble(listmap.get("partmoney"));
//								resultli = CommUtil.toString(listmap.get("json"));
//								mermoney = CommUtil.subBig(paymoney, partmoney);
//								Integer manid = partmoney == 0 ? 0 : aid;
//								tradeRecordService.insertToTrade(merid, manid, 0, ordernum, paymoney, mermoney, partmoney, code, 1, 3, 1, devicehard, resultli);
//							}
						} catch (Exception e) {
							logger.warn("小区修改余额错误===" + e.getMessage());
						}
//						addAPartEarn(code, consume, userEquipment.getUserId(), equipment.getAid(),1,1);
					}
					// orderService.updateStatusByOrdernum(out_trade_no, 1);
				} else {
					InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(out_trade_no);
					OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(out_trade_no);
					OnlineCardRecord onlineCard = onlineCardRecordService.selectRecordByOrdernum(out_trade_no, 6, 1);
					Money moneyorder = moneyService.queryMoneyByOrdernum(out_trade_no);
					if (inCoins != null) {
						if (inCoins.getStatus() == null || inCoins.getStatus() == 0){
							try {
								String money = String.valueOf(inCoins.getMoney());
								int idx = money.lastIndexOf(".");
								String total_fee = money.substring(0, idx);
//								SendMsgUtil.send_0x83(inCoins.getEquipmentnum(), inCoins.getPort(), Byte.parseByte(total_fee));
//								WolfHttpRequest.sendIncoinsPaydata(inCoins.getEquipmentnum(), inCoins.getPort(), Byte.parseByte(total_fee));
								inCoinsService.updateInCoinsStatusAndRecycletype(out_trade_no, (byte) 1);
							} catch (Exception e) {
								logger.error("投币异常" + e.getMessage() + e.getStackTrace()[0].getLineNumber());
							}
							String ordernum = CommUtil.toString(inCoins.getOrdernum());
							String devicenum = CommUtil.toString(inCoins.getEquipmentnum());
							Integer uid = CommUtil.toInteger(inCoins.getUid());
							Integer merid = CommUtil.toInteger(inCoins.getMerchantid());
							Double paymoney = CommUtil.toDouble(inCoins.getMoney());
							Integer orderid = CommUtil.toInteger(inCoins.getId());
							Date time = new Date();
							String strtime = CommUtil.toDateTime(time);
							Integer paysource = MerchantDetail.INCOINSSOURCE;
							Integer paytype = MerchantDetail.ALIPAY;
							Integer paystatus = MerchantDetail.NORMAL;
							Integer type = 1;
							Double mermoney = 0.00;
//							messagenotification( first, uid, merid, paysource, paymoney, strtime, status, remark,  orderid, ordernum);
							
							Map<String, Object> usequmap = userEquipmentService.getEquipToUserByCode(devicenum);
							String devicehard = CommUtil.toString(usequmap.get("hardversion"));
						    Integer aid = CommUtil.toInteger(usequmap.get("aid"));
							String resultli = null;
							Double partmoney = 0.00;
							try {
								List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
								if(aid!=0) partInfo = areaService.inquirePartnerInfo(aid, 2);
								//分成计算
								Map<String, Object> percentmap = percentCalculateDispose(partInfo, merid, paymoney);
								partmoney = CommUtil.toDouble(percentmap.get("partmoney"));
								mermoney = CommUtil.toDouble(percentmap.get("mermoney"));
								List<Map<String, Object>> percentinfo = (List<Map<String, Object>>) percentmap.get("percentinfo");
								String percentresult = CommUtil.toString(JSON.toJSON(percentinfo));
								tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, paysource, paytype, paystatus, devicehard, percentresult);
								for(Map<String, Object> item : percentinfo){
									Integer mercid = CommUtil.toInteger(item.get("merid"));
									Integer partid = CommUtil.toInteger(item.get("partid"));
									//Double percent = CommUtil.toDouble(item.get("percent"));
									Double money = CommUtil.toDouble(item.get("money"));
									if(mercid.equals(0)) mercid = partid;
									merEearningCalculate( mercid, money, type, ordernum, time, paysource, paytype, paystatus);
								}
								addAPartEarn(devicenum, paymoney, merid, aid, 1, 1);
//								List<TradeRecord> tradelist = tradeRecordService.getTraderecordList(ordernum, 1);
//								if(tradelist.size()==0){
//									List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
//									addAPartEarn(devicenum, paymoney, merid, aid, 1, 1);
//									if (aid != null && aid != 0) {
//										partInfo = areaService.inquirePartnerInfo(aid, 2);
//									}
//									Map<String, Object> listmap = partnerIncomeDispose(partInfo, merid, paymoney, ordernum, time, paysource, paytype, paystatus, type);
//									partmoney = CommUtil.toDouble(listmap.get("partmoney"));
//									resultli = CommUtil.toString(listmap.get("json"));
//									mermoney = CommUtil.subBig(paymoney, partmoney);
//									Integer manid = partmoney == 0 ? 0 : -1;
//									tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 2, 3, 1, devicehard, resultli);
//								}
							} catch (Exception e) {
								logger.warn("小区修改余额错误===" + e.getMessage());
							}
						}
					} else if (offlineCard != null) {
						if (offlineCard.getStatus() == null || offlineCard.getStatus() == 0) {
							offlineCard.setStatus(1);
							offlineCardService.updateChargeMoneyOfflineCardRecordPaytype(offlineCard);
						try {
							String cardID = offlineCard.getCardID();
							int parseLong = (int) Long.parseLong(cardID, 16);
							String accountmoney = String.valueOf(offlineCard.getAccountmoney() * 10);
							String money = accountmoney.substring(0, accountmoney.indexOf("."));
							short parseShort = Short.parseShort(money);
//							SendMsgUtil.send_0x22(offlineCard.getEquipmentnum(), parseLong, parseShort, (byte) 1);
//							WolfHttpRequest.sendOfflineCardPaydata(offlineCard.getEquipmentnum(), cardID, parseShort, (byte) 1);
						} catch (Exception e) {
							logger.error("支付宝---离线卡充值异常" + e.getMessage() + e.getStackTrace()[0].getLineNumber());
						}
							//修改本地数据
							String ordernum = CommUtil.toString(offlineCard.getOrdernum());
							Integer uid = CommUtil.toInteger(offlineCard.getUid());
							Integer merid = CommUtil.toInteger(offlineCard.getMerchantid());
							Double paymoney = CommUtil.toDouble(offlineCard.getChargemoney());//实际付款金额
							Integer orderid = CommUtil.toInteger(offlineCard.getId());
							String devicenum = CommUtil.toString(offlineCard.getEquipmentnum());
							Date time = new Date();
							String strtime = CommUtil.toDateTime(time);
							Integer paysource = MerchantDetail.OFFLINESOURCE;
							Integer paytype = MerchantDetail.ALIPAY;
							Integer paystatus = MerchantDetail.NORMAL;
							Integer type = 1;
							Double mermoney = 0.00;
							String first = "";
							String remark = "";
							String status = "付款成功,开始充电。";
//							messagenotification( first, uid, merid, paysource, paymoney, strtime, status, remark,  orderid, ordernum);
							Map<String, Object> usequmap = userEquipmentService.getEquipToUserByCode(devicenum);
							String ramrk = CommUtil.toString(usequmap.get("hardversion"));
							Integer aid = CommUtil.toInteger(usequmap.get("aid"));
							String resultli = null;
							Double partmoney = 0.00;
							try {
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
								addAPartEarn(devicenum, paymoney, merid, aid, 1, 1);
//								if (aid != 0) areaService.updateAreaEarn(1, paymoney, aid,null,paymoney,null);
//								List<TradeRecord> tradelist = tradeRecordService.getTraderecordList(ordernum, 1);
//								if(tradelist.size()==0){
//									addAPartEarn(devicenum, paymoney, merid, aid, 1, 1);
//									List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
//									if (aid != null && aid != 0) {
////									areaService.updateAreaEarn(1, paymoney, aid,null,paymoney,null);
//										partInfo = areaService.inquirePartnerInfo(aid, 2);
//									}
//									Map<String, Object> listmap = partnerIncomeDispose(partInfo, merid, paymoney, ordernum, time, paysource, paytype, paystatus, type);
//									partmoney = CommUtil.toDouble(listmap.get("partmoney"));
//									resultli = CommUtil.toString(listmap.get("json"));
//									mermoney = CommUtil.subBig(paymoney, partmoney);
//									Integer manid = partmoney == 0 ? 0 : aid;
//									tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 3, 3, 1, ramrk, resultli);
//								}
							} catch (Exception e) {
								logger.warn("小区修改余额错误===" + e.getMessage());
							}
//							String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=2&id="+offlineCard.getId();
//							String phone = getTempPhone(offlineCard.getEquipmentnum(), offlineCard.getMerchantid(), 1);
//							paychargesendmsg("您好，充值成功，欢迎您的使用", offlineCard.getUid(), phone, urltem,offlineCard.getChargemoney());
						}
					}else if(onlineCard!=null){
						if (onlineCard.getStatus() == null || onlineCard.getStatus() == 0) {
							onlineCardRecordService.updateonlinestatus(out_trade_no, (byte) 1);
							if (onlineCard.getStatus() == 0) {
								OnlineCard online = onlineCardService.selectOnlineCardByCardID(onlineCard.getCardID());
								String ordernum = CommUtil.toString(onlineCard.getOrdernum());
								Integer merid = CommUtil.toInteger(onlineCard.getMerid());
								Double paymoney = CommUtil.toDouble(onlineCard.getMoney());
								Double accountmoney = CommUtil.toDouble(onlineCard.getAccountmoney());
								Integer uid = CommUtil.toInteger(onlineCard.getUid());
								Integer orderid = CommUtil.toInteger(onlineCard.getId());
								String cardID = CommUtil.toString(onlineCard.getCardID());
								Double mermoney = 0.00;
								//Integer merid = online.getMerid();
								OnlineCard onlines = new OnlineCard();
								onlines.setId(online.getId());
								onlines.setMoney((accountmoney*100 + online.getMoney()*100)/100);
								onlineCardService.updateOnlineCard(onlines);
								try {
									if (merid != null && merid != 0) {
										// 在线卡充值金额处理
										Date time = new Date();
										String strtime = CommUtil.toDateTime(time);
										Integer paysource = MerchantDetail.ONLINESOURCE;
										Integer paytype = MerchantDetail.ALIPAY;
										Integer paystatus = MerchantDetail.NORMAL;
										Integer aid = CommUtil.toInteger(online.getAid());
										Integer type = 1;
										String resultli = null;
										Double partmoney = 0.00;
										try {
											List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
											if(aid!=0) partInfo = areaService.inquirePartnerInfo(aid, 2);
											//分成计算
											Map<String, Object> percentmap = percentCalculateDispose(partInfo, merid, paymoney);
											partmoney = CommUtil.toDouble(percentmap.get("partmoney"));
											mermoney = CommUtil.toDouble(percentmap.get("mermoney"));
											List<Map<String, Object>> percentinfo = (List<Map<String, Object>>) percentmap.get("percentinfo");
											String percentresult = CommUtil.toString(JSON.toJSON(percentinfo));
//											tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, cardID, 5, 3, 1, null, resultli);
											tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, cardID, paysource, paytype, paystatus, null, percentresult);
											for(Map<String, Object> item : percentinfo){
												Integer mercid = CommUtil.toInteger(item.get("merid"));
												Integer partid = CommUtil.toInteger(item.get("partid"));
												//Double percent = CommUtil.toDouble(item.get("percent"));
												Double money = CommUtil.toDouble(item.get("money"));
												if(mercid.equals(0)) mercid = partid;
												merEearningCalculate( mercid, money, type, ordernum, time, paysource, paytype, paystatus);
											}
											addAPartEarn(null, paymoney, merid, aid, 3, 1);
											
//											List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
//											addAPartEarn(null, paymoney, merid, aid, 3, 1);
//											if (aid != null && aid != 0) {
////												areaService.updateAreaEarn(1, paymoney, aid,null,null,paymoney);
//												partInfo = areaService.inquirePartnerInfo(aid, 2);
//											}
//											//商户收益（分成）计算
//											Map<String, Object> listmap = partnerIncomeDispose(partInfo, merid, paymoney, ordernum, time, paysource, paytype, paystatus, type);
//											partmoney = CommUtil.toDouble(listmap.get("partmoney"));
//											resultli = CommUtil.toString(listmap.get("json"));
										} catch (Exception e) {
											logger.warn("小区修改余额错误===" + e.getMessage());
										}
//										mermoney = CommUtil.subBig(paymoney, partmoney);
//										Integer manid = partmoney == 0 ? 0 : -1;
//										tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, cardID, 5, 3, 1, null, resultli);
									} 
//										String phone = getTempPhone(CommUtil.toString(user.getAid()), user.getMerid(), 4);
//										String detailurl = CommonConfig.ZIZHUCHARGE + "/general/sendmsgdetails?source=5&id=" + onlineCard.getId();
//										TempMsgUtil.paychargesendmsg("您好，在线卡充值成功。", onlineCard.getUid(), phone, detailurl, money);

								} catch (Exception e) {
									logger.info(e.getMessage() + "支付宝在线卡充值-" + ByteUtils.getLineNumber(e));
								}
							}
						}
					}else if(moneyorder!=null){
						if (moneyorder.getPaytype() == 7) {
							try {
								//修改本地数据
								String ordernum = CommUtil.toString(moneyorder.getOrdernum());
								Integer uid = CommUtil.toInteger(moneyorder.getUid());
								Double paymoney = CommUtil.toDouble(moneyorder.getMoney());
								Double sendmoney = CommUtil.toDouble(moneyorder.getSendmoney());
								Double accountmoney = CommUtil.toDouble(moneyorder.getTomoney());
								Double balancemoney = CommUtil.toDouble(moneyorder.getBalance());
								Double topupbalance = CommUtil.toDouble(moneyorder.getTopupbalance());
								Double givebalance = CommUtil.toDouble(moneyorder.getGivebalance());
								
								Integer orderid = CommUtil.toInteger(moneyorder.getId());
								Date time = new Date();
								String strtime = CommUtil.toDateTime(time);
								Integer paysource = MerchantDetail.WALLETSOURCE;
								Integer paytype = MerchantDetail.ALIPAY;
								Integer paystatus = MerchantDetail.NORMAL;
								Integer type = 1;
								Double mermoney = 0.00;
								String first = "";
								String remark = "";
								String status = "付款成功,开始充电。";
								User touuser = new User();
								if(uid!=0) touuser = userService.selectUserById(uid);
								Integer merid = CommUtil.toInteger(touuser.getMerid());
								Integer aid = CommUtil.toInteger(touuser.getAid());
								//-----------------------------------------------
								Double userbalance = CommUtil.toDouble(touuser.getBalance());
								Double usersendmoney = CommUtil.toDouble(touuser.getSendmoney());
								
								Double opermoney = CommUtil.toDouble(paymoney);
								Double opersendmoney = CommUtil.toDouble(sendmoney);
								Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
								Double mtopupbalance = CommUtil.addBig(opermoney, userbalance);
								Double mgivebalance = CommUtil.addBig(opersendmoney, usersendmoney);
								Double operbalance = CommUtil.addBig(mtopupbalance, mgivebalance);
								
								//1、充值记录修改
								moneyorder.setBalance(operbalance);
								moneyorder.setStatus(1);
								moneyService.updateMoney(moneyorder);
								//2、修改用户金额
								userService.updateBalanceByOpenid(mtopupbalance, mgivebalance, touuser.getOpenid(),null);
								String mremark = moneyorder.getRemark();
								//3、添加钱包明细
								generalDetailService.insertGenDetail(uid, merid, opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, ordernum, time, 1, mremark);
								
//								String phone = getTempPhone(CommUtil.toString(meruser.getAid()), meruser.getMerid(), 3);
//								String detailurl = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=4&id="+moneyorder.getId();
//								TempMsgUtil.paychargesendmsg("您好，充值成功。", touruser.getId(), phone, detailurl, moneyorder.getMoney());
								String resultli = null;
								Double partmoney = 0.00;
								try {
									List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
									if(aid!=0) partInfo = areaService.inquirePartnerInfo(aid, 2);
									//分成计算
									Map<String, Object> percentmap = percentCalculateDispose(partInfo, merid, paymoney);
									partmoney = CommUtil.toDouble(percentmap.get("partmoney"));
									mermoney = CommUtil.toDouble(percentmap.get("mermoney"));
									List<Map<String, Object>> percentinfo = (List<Map<String, Object>>) percentmap.get("percentinfo");
									String percentresult = CommUtil.toString(JSON.toJSON(percentinfo));
									tradeRecordService.insertToTrade(merid, aid, uid, ordernum, paymoney, mermoney, partmoney, "钱包", paysource, paytype, paystatus, null, percentresult);
									for(Map<String, Object> item : percentinfo){
										Integer mercid = CommUtil.toInteger(item.get("merid"));
										Integer partid = CommUtil.toInteger(item.get("partid"));
										//Double percent = CommUtil.toDouble(item.get("percent"));
										Double money = CommUtil.toDouble(item.get("money"));
										if(mercid.equals(0)) mercid = partid;
										merEearningCalculate( mercid, money, type, ordernum, time, paysource, paytype, paystatus);
									}
									addAPartEarn(null, paymoney, merid, aid, 2, 1);
//									List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
//									if (aid != null && aid != 0) {
//										Area area = areaService.selectByIdArea(aid);
//										if(area!=null){
//											partInfo = areaService.inquirePartnerInfo(aid, 2);
//										}
//									}
									//商户收益（分成）计算
//									Map<String, Object> listmap = partnerIncomeDispose(partInfo, merid, paymoney, ordernum, time, paysource, paytype, paystatus, type);
//									partmoney = CommUtil.toDouble(listmap.get("partmoney"));
//									resultli = CommUtil.toString(listmap.get("json"));
								} catch (Exception e) {
									logger.warn("小区修改余额错误===" + e.getMessage());
								}
							} catch (Exception e) {
								logger.info(e.getMessage() + "支付宝钱包充值-" + ByteUtils.getLineNumber(e));
								e.printStackTrace();
							}
						}
					}
				}
				// 注意：
				// 如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
			}

			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			System.out.println("支付成功订单号=======" + out_trade_no);

			System.out.println("success"); // 请不要修改或删除
			return "success";

			//////////////////////////////////////////////////////////////////////////////////////////
		} else {// 验证失败
			System.out.println("fail");
			return "fail";
		}
	}
	
	/**
	 * 同步回调
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/return_url", method = RequestMethod.GET)
	public String return_url() throws Exception {
		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 商户订单号

		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

		// 支付宝交易号

		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		// 计算得出通知验证结果
		// boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String
		// publicKey, String charset, String sign_type)
		boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY3, AlipayConfig.CHARSET,
				"RSA2");

		if (verify_result) {// 验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码
			// 该页面可做页面美工编辑
			// out.clear();
			System.out.println("验证成功<br />");
			return "wxpayaccess";
			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

			//////////////////////////////////////////////////////////////////////////////////////////
		} else {
			// 该页面可做页面美工编辑
			// out.clear();
			System.out.println("验证失败");
			return "alipayfail";
		}
	}

	/**
	 * PC端支付宝退款
	 * @param id
	 * @param refundState
	 * @param pwd
	 * @param utype
	 * @return
	 */
	@RequestMapping("/alipayRefund")
	@ResponseBody
	public Object alipayRefund(Integer id, Integer refundState, String pwd, Integer utype) {
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
					map = alirefund(id, refundState, map);
				}
			}
		} else if (utype == 2 || utype == 3) {
			String wolfkey = request.getParameter("wolfkey");
			if (wolfkey != null) {
				map.put("wolfkey", Integer.parseInt(wolfkey));
			}
			map = alirefund(id, refundState, map);
		}
		return map;
	}
	
	@RequestMapping("/aliDoRefund")
	@ResponseBody
	public Object aliDoRefund(Integer id, Integer refundState) {
		Map<String, Object> map = new HashMap<>();
		User userSession = (User) request.getSession().getAttribute("user");
		if (userSession == null) {
			map.put("ok", "error");
		} else {
			String wolfkey = request.getParameter("wolfkey");
			if (wolfkey != null) {
				map.put("wolfkey", Integer.parseInt(wolfkey));
			}
			map = alirefund(id, refundState, map);
		}
		return map;
	}
	
	public Map<String, Object> alirefund(Integer id, Integer refundState, Map<String, Object> map) {
		AlipayClient alipayClient = null;
		int wolfkey = map.get("wolfkey") == null ? 0 : (int) map.get("wolfkey");
		if (wolfkey == 4) {
			alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPLETAPPID,
					AlipayConfig.APPLETRSA_PRIVATE_KEY, "json", "GBK", AlipayConfig.APPLETALIPAY_PUBLIC_KEY, "RSA2");
//			alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.NEWSHANGHUPID,
//					AlipayConfig.PRIVATEKEY, "json", "GBK", AlipayConfig.ALIPAY_PUBLICKEY, "RSA2");
//			alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPLET_APPID,
//					AlipayConfig.APPLET_RSA_PRIVATE_KEY, "json", "GBK", AlipayConfig.APPLET_ALIPAY_PUBLIC_KEY, "RSA2");
		} else {
			alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.FUWUPID,
					AlipayConfig.RSA_PRIVATE_KEY3, "json", "GBK", AlipayConfig.ALIPAY_PUBLIC_KEY3, "RSA2");
//			alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.NEWSHANGHUPID,
//					AlipayConfig.ALIPAY_PRIVATE_NEWKEY, "json", "GBK", AlipayConfig.APPLETALIPAY_PUBLIC_NEWKEY, "RSA2");
		}
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		Double paymoney = 0.0;
		String ordernum = "";
		String comment = "";
		if (refundState == 1) {
			ChargeRecord chargeRecord = chargeRecordService.chargeRecordOne(id);
			if (chargeRecord.getPaytype() == 8) {
				alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPLETAPPID,
						AlipayConfig.APPLETRSA_PRIVATE_KEY, "json", "GBK", AlipayConfig.APPLETALIPAY_PUBLIC_KEY, "RSA2");
			}
			paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
			ordernum = CommUtil.toString(chargeRecord.getOrdernum());
			TradeRecord traderecord = tradeRecordService.getTraderecord(ordernum);
			comment = CommUtil.toString(traderecord.getComment());
			if ((traderecord == null) || (checkUserIfRich(traderecord) == false)) {
				map.put("ok", "moneyerror");
				return map;
			}
		} else if (refundState == 2) {
			OfflineCard offlineCard = offlineCardService.selectOfflineCardById(id);
			if (offlineCard.getPaytype() == 8) {
				alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPLETAPPID,
						AlipayConfig.APPLETRSA_PRIVATE_KEY, "json", "GBK", AlipayConfig.APPLETALIPAY_PUBLIC_KEY, "RSA2");
			}
			paymoney = CommUtil.toDouble(offlineCard.getChargemoney());
			ordernum = CommUtil.toString(offlineCard.getOrdernum());
			TradeRecord traderecord1 = tradeRecordService.getTraderecord(ordernum);
			comment = CommUtil.toString(traderecord1.getComment());
			if ((traderecord1 == null) || (checkUserIfRich(traderecord1) == false)) {
				map.put("ok", "moneyerror");
				return map;
			}
		} else if (refundState == 3) {
			// 根据投币订单ID查询投币信息
			InCoins incoins = inCoinsService.selectInCoinsRecordById(id);
			paymoney = CommUtil.toDouble(incoins.getMoney());
			ordernum = CommUtil.toString(incoins.getOrdernum());
			// 根据投币订单的订单号,查询交易信息
			TradeRecord traderecord1 = tradeRecordService.getTraderecord(ordernum);
			comment = CommUtil.toString(traderecord1.getComment());
			if ((traderecord1 == null) || (checkUserIfRich(traderecord1) == false)) {
				map.put("ok", "moneyerror");
				return map;
			}
		} else if (refundState == 4) {
			Money moneyrec = moneyService.payMoneyinfo(id);
			paymoney = CommUtil.toDouble(moneyrec.getMoney());
			ordernum = CommUtil.toString(moneyrec.getOrdernum());// 退款订单号
			if (!walletRich(moneyrec)){
				map.put("ok", "error");
				return map;
			}
			
		}else if(refundState == 5){
			OnlineCardRecord onlinerec = onlineCardRecordService.selectOnlineCard( id);
			if(onlinerec==null){
				map.put("ok", "error");
				return map;
			}
			if (onlinerec.getType() == 10) {
				alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPLETAPPID,
						AlipayConfig.APPLETRSA_PRIVATE_KEY, "json", "GBK", AlipayConfig.APPLETALIPAY_PUBLIC_KEY, "RSA2");
			}
			paymoney = CommUtil.toDouble(onlinerec.getMoney());
			ordernum = CommUtil.toString(onlinerec.getOrdernum());
			OnlineCard oncard = onlineCardService.selectOnlineCardByCardID(onlinerec.getCardID());
			if (oncard.getMerid() != null && oncard.getMerid() != 0) {
			  if(oncard.getStatus()!=0){
				TradeRecord traderecord1 = tradeRecordService.getTraderecord(ordernum);
				comment = CommUtil.toString(traderecord1.getComment());
				if ((traderecord1 == null) || (checkUserIfRich(traderecord1) == false)) {
					map.put("ok", "moneyerror");
					return map;
				}
			  }
			}
		}
		request.setBizContent("{" + "\"out_trade_no\":\"" + ordernum + "\"," + "\"refund_amount\":" + paymoney + "  }");
		AlipayTradeRefundResponse response;
		try {
			response = alipayClient.execute(request);
			if (response.isSuccess()) {
				Integer paytype = MerchantDetail.ALIPAY;
				Integer paystatus = MerchantDetail.REFUND;
				if(comment.equals("0") || comment==null){
					System.out.println("AAAAA*****之前数据");
					amendmysql( refundState, id, paymoney, ordernum, wolfkey, map);
				}else{
					System.out.println("AAAAA*****新数据");
					if (refundState == 1) {
						chargeRefund( id, ordernum, paymoney, paytype, paystatus, map);
					} else if (refundState == 2) {
						offlineRefund( id, ordernum, paymoney, paytype, paystatus, map);
					} else if (refundState == 3) {
						inCoinsRefund( id, ordernum, paymoney, paytype, paystatus, wolfkey, map);
					} else if (refundState == 4) {
						map = walletRefund( id, refundState, ordernum);
					}else if(refundState == 5){
						map = onlinecardData( id, refundState, ordernum);
					}
					map.put("ok", "ok");
				}
				return map;
			} else {
				map.put("ok", "error");
				return map;
			}
		} catch (AlipayApiException e) {
			logger.error(e.getMessage() + e.getStackTrace()[0].getLineNumber());
			map.put("ok", "error");
			return map;
		}
		
	}
	
	/**
	 * @Description： 充电记录支付宝退费
	 * @author： origin 创建时间：   2019年7月20日 下午5:40:08 
	 */
	private void chargeRefund(Integer id, String ordernum, Double paymoney, Integer paytype, 
			Integer paystatus, Map<String, Object> map) {
		ChargeRecord chargeRecord = chargeRecordService.chargeRecordOne(id);
		Integer orderid = CommUtil.toInteger(id);
		Date time = new Date();
		String strtime = CommUtil.toDateTime(time);
		String devicenum = CommUtil.toString(chargeRecord.getEquipmentnum());
		Integer uid = CommUtil.toInteger(chargeRecord.getUid());
		Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
		Integer paysource = MerchantDetail.CHARGESOURCE;
		Double mermoney = 0.00;
		String devicehard = "";
		
		//equipmentService.updateEquEarn(devicenum, paymoney, 0);
		if (chargeRecord.getEndtime() == null) {
			chargeRecordService.updateNumberById(null, 1, orderid, null, null, paymoney, CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", chargeRecord.getBegintime()), CommUtil.toDateTime());
		} else {
			chargeRecordService.updateNumberById(null, 1, orderid, null, null, paymoney, null, CommUtil.toDateTime());
		}
		if (chargeRecord.getNumber() != 1) {
			TradeRecord traderecord = tradeRecordService.getTraderecord(ordernum);
			String comment = traderecord.getComment();
			try {
				Equipment equipment = equipmentService.getEquipmentById(devicenum);
				devicehard = CommUtil.toString(equipment.getHardversion());
				Integer aid = CommUtil.toInteger(equipment.getAid());
				addAPartEarn(devicenum, paymoney, merid, aid, 1, 0);
				if (aid != null && aid != 0) {
					areaService.updateAreaEarn(0, paymoney, aid,null,paymoney,null);
				} 
				//充电退费数据
				dealerIncomeRefund(comment, merid, paymoney, ordernum, time, paysource, paytype, paystatus);
			} catch (Exception e) {
				logger.warn("小区修改余额错误===" + e.getMessage());
			}
			mermoney = traderecord.getMermoney();
			Double partmoney = traderecord.getManmoney();
			Integer manid = partmoney == 0 ? 0 : -1;
			tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 1, 5, 2, devicehard, comment);
		}
		/*
	//		messagenotification( first, uid, merid, paysource, paymoney, strtime, status, remark,  orderid, ordernum);
		 */
	//	String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=1&id="+orderid;
	//	returnMsgTemp(uid, devicenum, ordernum, urltem, strtime, paymoney);
	}
	
	/**
	 * @Description： 离线充值机支付宝退费
	 * @author： origin 创建时间：   2019年7月20日 下午5:40:08 
	 */
	private void offlineRefund(Integer id, String ordernum, Double paymoney, Integer paytype, 
			Integer paystatus, Map<String, Object> map) {
		
		Integer orderid = CommUtil.toInteger(id);
		Date time = new Date();
		String strtime = CommUtil.toDateTime(time);
		OfflineCard offlineCard = offlineCardService.selectOfflineCardById(id);
		Integer opppaytype = CommUtil.toInteger(offlineCard.getPaytype());
		if(opppaytype==2) {
			offlineCardService.offlineCardRefund(4, id);
		}else if(opppaytype==8){
			offlineCardService.offlineCardRefund(9, id);
		}
		//修改本地数据
		String devicenum = CommUtil.toString(offlineCard.getEquipmentnum());
		Integer uid = CommUtil.toInteger(offlineCard.getUid());
		Integer merid = CommUtil.toInteger(offlineCard.getMerchantid());
		Double accountmoney = CommUtil.toDouble(offlineCard.getAccountmoney());
		Integer paysource = MerchantDetail.OFFLINESOURCE;
		
		Double mermoney = 0.00;
		String devicehard = "";
		if (offlineCard.getPaytype() != 4 && offlineCard.getPaytype() != 9) {
			//equipmentService.updateEquEarn(devicenum, paymoney, 0);
			TradeRecord traderecord = tradeRecordService.getTraderecord(ordernum);
			String comment = CommUtil.toString(traderecord.getComment());
			String resultli = null;
			try {
				Equipment equipment = equipmentService.getEquipmentById(devicenum);
				devicehard = CommUtil.toString(equipment.getHardversion());
				Integer aid = CommUtil.toInteger(equipment.getAid());
				addAPartEarn(devicenum, paymoney, merid, aid, 1, 0);
				//addAPartEarn(String code,double money,Integer merid,Integer aid,int type,int status)
				List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
				if (aid != null && aid != 0) {
//					areaService.updateAreaEarn(0, paymoney, aid,null,paymoney,null);
					partInfo = areaService.inquirePartnerInfo(aid, 2);
				} 
				//离线充值机退费处理数据
				dealerIncomeRefund(comment, merid, paymoney, ordernum, time, paysource, paytype, paystatus);
			} catch (Exception e) {
				logger.warn("小区修改余额错误===" + e.getMessage());
			}
			mermoney = traderecord.getMermoney();
			Double partmoney = traderecord.getManmoney();
			Integer manid = partmoney == 0 ? 0 : -1;
			if(opppaytype==2) {
				tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 3, 3, 2, devicehard, comment);
			}else if(opppaytype==8){
				tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 3, 5, 2, devicehard, comment);
			}
		}
//		String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=2&id="+orderid;
//		returnMsgTemp(offlineCard.getUid(), devicenum, ordernum, urltem, strtime, offlineCard.getChargemoney());
	}
	
	/**
	 * @Description： 脉冲支付宝退费
	 * @author： origin 创建时间：   2019年7月20日 下午5:40:08 
	 */
	private void inCoinsRefund(Integer id, String ordernum, Double paymoney, Integer paytype, 
			Integer paystatus, Integer wolfkey, Map<String, Object> map) {
		Integer orderid = CommUtil.toInteger(id);
		Date time = new Date();
		String strtime = CommUtil.toDateTime(time);
		InCoins incoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
		//修改本地数据
		String devicenum = CommUtil.toString(incoins.getEquipmentnum());
		Integer uid = CommUtil.toInteger(incoins.getUid());
		Integer merid = CommUtil.toInteger(incoins.getMerchantid());
		Integer paysource = MerchantDetail.INCOINSSOURCE;
		Integer type = 0;
		Double mermoney = 0.00;
		String devicehard = "";
		Integer aid = 0;
		String first = "";
		String remark = "";
		String status = "退款成功！。";
		if (wolfkey == 4) {
			inCoinsService.updateInCoinsStatus(ordernum, (byte) 11);
		} else {
			inCoinsService.updateInCoinsStatus(ordernum, (byte) 5);
		}
		if (incoins.getHandletype() != 5 && incoins.getHandletype() != 11) {
			TradeRecord traderecord = tradeRecordService.getTraderecord(ordernum);
			String comment = CommUtil.toString(traderecord.getComment());
			String resultli = null;
			try {
				Equipment equipment = equipmentService.getEquipmentById(devicenum);
				devicehard = CommUtil.toString(equipment.getHardversion());
				aid = CommUtil.toInteger(equipment.getAid());
				addAPartEarn(devicenum, paymoney, merid, aid, 1, 0);
//				List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
//				if (aid != null && aid != 0) {
//					partInfo = areaService.inquirePartnerInfo(aid, 2);
//				} 
				//脉冲退费处理数据
				dealerIncomeRefund(comment, merid, paymoney, ordernum, time, paysource, paytype, paystatus);
			} catch (Exception e) {
				logger.warn("小区修改余额错误===" + e.getMessage());
			}
			mermoney = traderecord.getMermoney();
			Double partmoney = traderecord.getManmoney();
			Integer manid = partmoney == 0 ? 0 : aid;
			if (wolfkey == 4) {
				tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 2, 5, 2, devicehard, comment);
			} else {
				tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 2, 3, 2, devicehard, comment);
			}
		}
//		String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=3&id="+incoins.getId();
//		returnMsgTemp(incoins.getUid(), incoins.getEquipmentnum(), incoins.getOrdernum(), urltem, StringUtil.toDateTime(), incoins.getMoney());
	}
	
	/**
	 * @Description： 钱包退费
	 * @author： origin 创建时间：   2019年7月20日 下午5:40:08 
	 */
	private Map<String, Object> walletRefund(Integer id, Integer refundState, String ordernum) {
		Map<String, Object> map = new HashMap<>();
		try {
			Money money = moneyService.payMoneyinfo(id);
			//修改本地数据
			Integer uid = CommUtil.toInteger(money.getUid());
			Double paymoney = CommUtil.toDouble(money.getMoney());
			Double sendmoney = CommUtil.toDouble(money.getSendmoney());
			Double tomoney = CommUtil.toDouble(money.getTomoney());
			Double moneytopup = CommUtil.toDouble(money.getTopupbalance());
			Double moneygive = CommUtil.toDouble(money.getGivebalance());
			Double moneybalance = CommUtil.toDouble(money.getBalance());
			Integer orderid = CommUtil.toInteger(money.getId());
			Date time = new Date();
			String strtime = CommUtil.toDateTime(time);
			Integer paysource = MerchantDetail.WALLETSOURCE;
			Integer paytype = MerchantDetail.ALIPAY;
			Integer paystatus = MerchantDetail.REFUND;
			Double mermoney = 0.00;
			Integer moneypaytype = CommUtil.toInteger(money.getPaytype());
			if(moneypaytype==7) {
				moneyService.updateMoneyByOrder(ordernum, 8);
			}else if(moneypaytype==9){
				moneyService.updateMoneyByOrder(ordernum, 10);
			}
			
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
//			Double walletmo = (user.getBalance()*100-money.getMoney()*100)/100;
//			if(walletmo<0) walletmo = 0.00;
//			user.setBalance(walletmo);
			user.setBalance(userbalance);
			user.setSendmoney(usersendmoney);
			userService.updateUserById(user);
			if (money.getPaytype() != 8 && money.getPaytype() != 10) {
				TradeRecord traderecord = tradeRecordService.getTraderecord(ordernum);
				String comment = traderecord.getComment();
				String resultli = null;
				try {
					generalDetailService.insertGenWalletDetail(uid, merid, paymoney, sendmoney, opertomoney, balance, topupbalance, givebalance, ordernum, new Date(), 6);
//					generalDetailService.insertGenWalletDetail(uid, merid, tomoney, walletmo, ordernum, time, 6);
					addAPartEarn(null, traderecord.getMoney(), user.getMerid(), user.getAid(), 2, 0);
					Integer aid = CommUtil.toInteger(user.getAid());
					List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
//					if (aid != null && aid != 0) {
//						areaService.updateAreaEarn( 0, paymoney, aid, paymoney, null, null);
//						partInfo = areaService.inquirePartnerInfo(aid, 2);
//					} 
					//钱包充值退款数据
					dealerIncomeRefund(comment, merid, paymoney, ordernum, time, paysource, paytype, paystatus);
				} catch (Exception e) {
					logger.warn("小区修改余额错误===" + e.getMessage());
				}
				mermoney = traderecord.getMermoney();
				Double partmoney = traderecord.getManmoney();
				Integer manid = partmoney == 0 ? 0 : -1;
				tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, "钱包", 4, 3, 2, "00", comment);
			}
			System.out.println("发送消息");
			String phone = getTempPhone(CommUtil.toString(user.getAid()), user.getMerid(), 3);
			String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=4&id="+money.getId();
			TempMsgUtil.returnMsg("钱包退费", money.getUid(), phone, money.getOrdernum(), urltem, StringUtil.toDateTime(), money.getMoney());
			System.out.println("发送消息完成");
			map.put("ok", "ok");
		} catch (Exception e) {
			map.put("ok", "error");
		}
		return map;
	}

	/**
	 * @Description:  在线卡退款时数据处理
	 * @author： origin          
	 */
	public Map<String, Object> onlinecardData(Integer id, Integer refundState, String ordernum){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			OnlineCardRecord cardRecord = onlineCardRecordService.selectOnlineCard(id);
			//修改本地数据
			Integer uid = CommUtil.toInteger(cardRecord.getUid());
			Integer merid = CommUtil.toInteger(cardRecord.getMerid());
			Double paymoney = CommUtil.toDouble(cardRecord.getMoney());
			Double tomoney = CommUtil.toDouble(cardRecord.getAccountmoney());
			Integer orderid = CommUtil.toInteger(cardRecord.getId());
			String devicenum = CommUtil.toString(cardRecord.getCode());
			String cardID = CommUtil.toString(cardRecord.getCardID());
			Date time = new Date();
			String strtime = CommUtil.toDateTime(time);
			Integer paysource = MerchantDetail.ONLINESOURCE;
			Integer paytype = MerchantDetail.ALIPAY;
			Integer paystatus = MerchantDetail.REFUND;
			Integer type = 0;
			Double mermoney = 0.00;
			String first = "";
			String remark = "";
			String status = "退款成功！。";
			//处理在线卡信息
			OnlineCard oncard = onlineCardService.selectOnlineCardByCardID(cardID);
			Integer aid = CommUtil.toInteger(oncard.getAid());
			Double mone = CommUtil.subBig(CommUtil.toDouble(oncard.getMoney()), tomoney);
			oncard.setMoney(mone);
			onlineCardService.updateOnlineCardBycard(oncard);
			System.out.println("在线卡退费");
			if (oncard.getMerid() != null && oncard.getMerid() != 0) {
				if (oncard.getStatus()!=0 && cardRecord.getType() == 7 && cardRecord.getFlag()==2){
					TradeRecord traderecord = tradeRecordService.getTraderecord(ordernum);
					String comment = traderecord.getComment();
					try {
//						List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
//						if (aid != null && aid != 0) {
//							areaService.updateAreaEarn(0, paymoney, aid, null, null, paymoney);
//							partInfo = areaService.inquirePartnerInfo(aid, 2);
//						} 
						addAPartEarn(null, paymoney, merid, aid, 3, 0);
						// 在线卡充值退款数据
						dealerIncomeRefund(comment, merid, paymoney, ordernum, time, paysource, paytype, paystatus);
						mermoney = traderecord.getMermoney();
						Double partmoney = traderecord.getManmoney();
						Integer manid = partmoney == 0 ? 0 : -1;
						tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, traderecord.getCode(), 5, 3, 2, null, comment);
					} catch (Exception e) {
						logger.warn("小区修改余额错误===" + e.getMessage());
					}
				}
			}
			cardRecord.setFlag(2);
			onlineCardRecordService.updateRecord(cardRecord);
			cardRecord.setId(null);
			cardRecord.setType(7);
			cardRecord.setBalance(mone);
			cardRecord.setCreateTime(new Date());
			onlineCardRecordService.additionOnlineCardRecord(cardRecord);
			String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=5&id="+cardRecord.getId();
			String phone = getTempPhone(cardRecord.getCardID(), cardRecord.getMerid(), 4);
			TempMsgUtil.returnMsg("在线卡退费",cardRecord.getUid(), phone, cardRecord.getOrdernum(), urltem, StringUtil.toDateTime(), cardRecord.getMoney());
			map.put("ok", "ok");
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("退款失败");
			map.put("ok", "error");
		}
		return map;
	}
	
	public boolean checkUserIfRich(TradeRecord tradeRecord) {
		String comment = CommUtil.toString(tradeRecord.getComment());
		Integer merid = tradeRecord.getMerid();
		Double money = tradeRecord.getMoney();
		if(comment.equals("0") || comment==null){//之前数据
			System.out.println("之前数据");
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
	
//	public boolean checkUserIfRich(TradeRecord tradeRecord) {
//		Integer manid = tradeRecord.getManid();
//		Integer merid = tradeRecord.getMerid();
//		Double manmoney = tradeRecord.getManmoney();
//		Double mermoney = tradeRecord.getMermoney();
//		Double money = tradeRecord.getMoney();
//		if (manid != null && manid != 0) {
//			User manUser = userService.selectUserById(manid);
//			User merUser = userService.selectUserById(merid);
//			if ((manUser.getEarnings() >= manmoney) && (merUser.getEarnings() >= mermoney)) {
//				return true;
//			} else {
//				return false;
//			}
//		} else {
//			User merUser = userService.selectUserById(merid);
//			if ((merUser.getEarnings() >= money)) {
//				return true;
//			} else {
//				return false;
//			}
//		}
//	}
	
	/**
	 * @Description： 在线卡扫码支付宝充值
	 * @author： origin          
	 * 创建时间：   2019年6月15日 上午9:46:25
	 * @throws Exception 
	 */
	@RequestMapping(value = "/scanqronline", method = RequestMethod.POST)
	@ResponseBody
	public void scanQROnline(String openid, String cardnum, Integer tempid ) throws Exception {
		OnlineCard card = onlineCardService.selectOnlineCardByCardID(cardnum);
		Integer relevawalt = CommUtil.toInteger(card.getRelevawalt());
		Integer uid = CommUtil.toInteger(card.getUid());
		if(card!=null && card.getRelevawalt()==1){
			User touser = userService.selectUserById(card.getUid());
			if(touser!=null){
				card.setMoney(touser.getBalance());
				card.setSendmoney(touser.getSendmoney());
				uid = CommUtil.toInteger(touser.getId());
			}
		}
		TemplateSon templateSon = templateService.getInfoTemplateOne(tempid);
		//============================================================================
		Double topupmoney = CommUtil.toDouble(card.getMoney());//充值金额
		Double sendmoney = CommUtil.toDouble(card.getSendmoney());//赠送金额
		//Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额
		//==========================================================================
		Double opermoney = CommUtil.toDouble(templateSon.getRemark());//操作总额
		Double opertopupmoney = CommUtil.toDouble(templateSon.getMoney());//操作充值金额
		Double opersendmoney = CommUtil.subBig(opermoney, opertopupmoney);//操作赠送金额
		//==========================================================================
		Double topupbalance = CommUtil.addBig(topupmoney, opertopupmoney);//充值余额
		Double sendbalance = CommUtil.addBig(sendmoney, opersendmoney);//赠送余额
		Double accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
		//==========================================================================
		Date date = new Date();
		String ordernum = HttpRequest.createOrdernum(6);
		
		Integer merid = CommUtil.toInteger(card.getMerid());
		if(relevawalt==1){
			moneyService.payMoneys(uid, ordernum, 7, 0, opertopupmoney, opersendmoney, opermoney, accountbalance, topupbalance, sendbalance, card.getCardID());
			moneyService.insertWalletInfo(uid, merid, ordernum, 1, 7, 0, opertopupmoney, opersendmoney, 
					opermoney, accountbalance, topupbalance, sendbalance, new Date(), card.getCardID());
		}else{
			if (merid != null) {
				onlineCardRecordService.insertOnlineCardRecord(uid, merid, ordernum, cardnum, null, accountbalance, opertopupmoney, opersendmoney, 
					opermoney, topupbalance, sendbalance, 6, 0, 1, date, 0);
			} else {
				onlineCardRecordService.insertOnlineCardRecord(uid, null, ordernum  , cardnum, null, accountbalance, opertopupmoney, opersendmoney, 
					opermoney, topupbalance, sendbalance, 6, 0, 1, date, 0);
			}
		}
		// 订单名称，必填
		String subject = ordernum;
		// 付款金额，必填
		String total_amount = new String(String.format("%.2f", opertopupmoney).getBytes("ISO-8859-1"), "UTF-8");
		// 商品描述，可空
		String body = new String("自助充电平台".getBytes("ISO-8859-1"), "UTF-8");
		// 超时时间 可空
		String timeout_express = "2m";
		// 销售产品码 必填
		String product_code = "QUICK_WAP_WAY";
		/**********************/
		// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
		// 调用RSA签名方式
		AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
				AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,
				AlipayConfig.SIGNTYPE);
		AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

		// 封装请求支付信息
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setOutTradeNo(ordernum);
		model.setSubject(subject);
		model.setTotalAmount(total_amount);
		model.setBody(body);
		model.setTimeoutExpress(timeout_express);
		model.setProductCode(product_code);
		alipay_request.setBizModel(model);
		// 设置异步通知地址
		alipay_request.setNotifyUrl(AlipayConfig.NOTIFY_URL);
		// 设置同步地址
		alipay_request.setReturnUrl(AlipayConfig.RETURN_URL);

		// form表单生产
		String form = "";
		try {
			// 调用SDK生成表单
			form = client.pageExecute(alipay_request).getBody();
			response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
			try {
				response.getWriter().write(form);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			} // 直接将完整的表单html输出到页面
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}
	
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
//			Double tomoney = moneyRecord.getTomoney();
//			User user = userService.selectUserById(moneyRecord.getUid());
//			Double balance = user.getBalance();
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
	 * @param code
	 * @param money
	 * @param merid
	 * @param aid
	 * @param type 1、设备收益   2、钱包收益  3、在线卡收益
	 * @param status 1、增加  0、减少
	 */
	public void addAPartEarn(String code,double money,Integer merid,Integer aid,int type,int status) {
		//设备添加收益	(devicenum, paymoney, merid, aid, 1, 0);
		if (type == 1) {
			equipmentService.updateEquEarn(code, money, status);
		}
		//小区添加收益
		try {
			if (aid != null && aid != 0) {
				if (type == 1) {
					areaService.updateAreaEarn(status, money, aid,null,money,null);
				} else if (type == 2) {
					areaService.updateAreaEarn(status, money, aid,money,null,null);
				} else if (type == 3) {
					areaService.updateAreaEarn(status, money, aid,null,null,money);
				}
			} 
		} catch (Exception e) {
			logger.warn("小区修改余额错误===" + e.getMessage());
		}
	}
	
	public void amendmysql(Integer refundState, Integer id, Double money, String ordernum, Integer wolfkey, 
			Map<String, Object> map){
		if (refundState == 1) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ChargeRecord chargeRecord = chargeRecordService.chargeRecordOne(id);
			chargeRecordService.updateNumberById(null, 1, id, "0", 0.0, chargeRecord.getExpenditure(), sdf.format(chargeRecord.getBegintime()), CommUtil.toDateTime());
			String equipmentnum = chargeRecord.getEquipmentnum();
			Equipment equipment = equipmentService.getEquipmentById(equipmentnum);
			addAPartEarn(equipmentnum, money, chargeRecord.getMerchantid(), equipment.getAid(), 1, 0);
			if (chargeRecord.getId() != 1) {
				TradeRecord traderecord = tradeRecordService.getTraderecord(chargeRecord.getOrdernum());
				Integer manid = traderecord.getManid();
				if( null==manid || manid==0){
					User user = userService.selectUserById(chargeRecord.getMerchantid());
					user.setEarnings((user.getEarnings() * 100 - chargeRecord.getExpenditure() * 100) / 100);
					userService.updateUserEarnings(0, chargeRecord.getExpenditure(), user.getId());
					try {
						userService.updateMerAmount(0, chargeRecord.getExpenditure(), user.getId());
					} catch (Exception e) {
						logger.warn("商户总计更改出错");
					}
					System.out.println("---支付宝不分成 退费 修改成功---");
					merchantDetailService.insertMerEarningDetail(user.getId(), money, user.getEarnings(), ordernum,
							new Date(), MerchantDetail.CHARGESOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
					tradeRecordService.insertTrade(user.getId(), 0, ordernum, money, money, chargeRecord.getEquipmentnum(), 
							1, 3, 2, equipmentService.getEquipmentById(chargeRecord.getEquipmentnum()).getHardversion());
				}else{
					User manuser = userService.selectUserById(manid);
					manuser.setEarnings((manuser.getEarnings() * 100 - traderecord.getManmoney() * 100) / 100);
					User user = userService.selectUserById(chargeRecord.getMerchantid());
					user.setEarnings((user.getEarnings() * 100 - traderecord.getMermoney() * 100) / 100);
					userService.updateUserEarnings(0, traderecord.getMermoney(), user.getId());
					userService.updateUserEarnings(0, traderecord.getManmoney(), manid);
					try {
						userService.updateMerAmount(0, traderecord.getMermoney(), user.getId());
						userService.updateMerAmount(0, traderecord.getManmoney(), manid);
					} catch (Exception e) {
						logger.warn("商户总计更改出错");
					}
					System.out.println("--- 支付宝分成 充电退费  修改成功---");
					merchantDetailService.insertMerEarningDetail(user.getId(), traderecord.getMermoney(), user.getEarnings(), 
						ordernum, new Date(), MerchantDetail.CHARGESOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
					merchantDetailService.insertMerEarningDetail(manuser.getId(), traderecord.getManmoney(), manuser.getEarnings(), 
						ordernum, new Date(), MerchantDetail.CHARGESOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
					tradeRecordService.insertToTrade(user.getId(), manid, 0, ordernum, money, traderecord.getMermoney(), 
							traderecord.getManmoney(), chargeRecord.getEquipmentnum(), 1, 3, 2, 
							equipmentService.getEquipmentById(chargeRecord.getEquipmentnum()).getHardversion());
				}
			}
		} else if (refundState == 2) {
			OfflineCard offlineCard = offlineCardService.selectOfflineCardById(id);
			offlineCardService.offlineCardRefund(4, id);
			if (offlineCard.getPaytype() != 4) {
				TradeRecord traderecord = tradeRecordService.getTraderecord(offlineCard.getOrdernum());
				Integer manid = traderecord.getManid();
				String equipmentnum = offlineCard.getEquipmentnum();
				Equipment equipment = equipmentService.getEquipmentById(equipmentnum);
				addAPartEarn(equipmentnum, money, offlineCard.getMerchantid(), equipment.getAid(), 1, 0);
				if( null==manid || manid==0){
					User user = userService.selectUserById(offlineCard.getMerchantid());
					user.setEarnings((user.getEarnings() * 100 - offlineCard.getChargemoney() * 100) / 100);
					userService.updateUserEarnings(0, traderecord.getMermoney(), user.getId());
					try {
						userService.updateMerAmount(0, traderecord.getMermoney(), user.getId());
					} catch (Exception e) {
						logger.warn("商户总计更改出错");
					}
					System.out.println("---修改成功---");
					merchantDetailService.insertMerEarningDetail(user.getId(), money, user.getEarnings(), ordernum,
							new Date(), MerchantDetail.OFFLINESOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
					tradeRecordService.insertTrade(user.getId(), 0, ordernum, money, money, offlineCard.getEquipmentnum(), 
							3, 3, 2, equipmentService.getEquipmentById(offlineCard.getEquipmentnum()).getHardversion());
				}else{
					User manuser = userService.selectUserById(manid);
					manuser.setEarnings((manuser.getEarnings() * 100 - traderecord.getManmoney() * 100) / 100);
					User user = userService.selectUserById(offlineCard.getMerchantid());
					user.setEarnings((user.getEarnings() * 100 - traderecord.getMermoney() * 100) / 100);
					userService.updateUserEarnings(0, traderecord.getMermoney(), user.getId());
					userService.updateUserEarnings(0, traderecord.getManmoney(), manid);
					try {
						userService.updateMerAmount(0, traderecord.getMermoney(), user.getId());
						userService.updateMerAmount(0, traderecord.getManmoney(), manid);
					} catch (Exception e) {
						logger.warn("商户总计更改出错");
					}
					merchantDetailService.insertMerEarningDetail(user.getId(), traderecord.getMermoney(), user.getEarnings(), 
						ordernum, new Date(), MerchantDetail.OFFLINESOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
					merchantDetailService.insertMerEarningDetail(manuser.getId(), traderecord.getManmoney(), manuser.getEarnings(), 
						ordernum, new Date(), MerchantDetail.OFFLINESOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
					tradeRecordService.insertToTrade(user.getId(), manid, 0, ordernum, money, traderecord.getMermoney(), 
							traderecord.getManmoney(), offlineCard.getEquipmentnum(), 3, 3, 2, equipmentService.getEquipmentById(offlineCard.getEquipmentnum()).getHardversion());
					System.out.println("--- 支付宝分成 离线退费  修改成功---");
				}
			}
		} else if (refundState == 3) {
			InCoins incoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
			if (wolfkey == 4) {
				inCoinsService.updateInCoinsStatus(ordernum, (byte) 11);
			} else {
				inCoinsService.updateInCoinsStatus(ordernum, (byte) 5);
			}
			if (incoins.getId() != 1) {
				TradeRecord traderecord = tradeRecordService.getTraderecord(incoins.getOrdernum());
				Integer manid = traderecord.getManid();
				String equipmentnum = incoins.getEquipmentnum();
				Equipment equipment = equipmentService.getEquipmentById(equipmentnum);
				addAPartEarn(equipmentnum, money, incoins.getMerchantid(), equipment.getAid(), 1, 0);
				if( null==manid || manid==0){
					User user = userService.selectUserById(incoins.getMerchantid());
					user.setEarnings((user.getEarnings() * 100 - incoins.getMoney() * 100) / 100);
					userService.updateUserEarnings(0, traderecord.getMermoney(), user.getId());
					try {
						userService.updateMerAmount(0, traderecord.getMermoney(), user.getId());
					} catch (Exception e) {
						logger.warn("商户总计更改出错");
					}
					merchantDetailService.insertMerEarningDetail(user.getId(), incoins.getMoney(), user.getEarnings(), incoins.getOrdernum(), new Date(), MerchantDetail.INCOINSSOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
					if (wolfkey == 4) {
						tradeRecordService.insertTrade(user.getId(), 0, incoins.getOrdernum(), incoins.getMoney(), incoins.getMoney(), incoins.getEquipmentnum(), 2, 5, 2,equipmentService.getEquipmentById(incoins.getEquipmentnum()).getHardversion());
					} else {
						tradeRecordService.insertTrade(user.getId(), 0, incoins.getOrdernum(), incoins.getMoney(), incoins.getMoney(), incoins.getEquipmentnum(), 2, 3, 2,equipmentService.getEquipmentById(incoins.getEquipmentnum()).getHardversion());
					}
				}else{
					User manuser = userService.selectUserById(manid);
					manuser.setEarnings((manuser.getEarnings() * 100 - traderecord.getManmoney() * 100) / 100);
					User user = userService.selectUserById(incoins.getMerchantid());
					user.setEarnings((user.getEarnings() * 100 - traderecord.getMermoney() * 100) / 100);
					userService.updateUserEarnings(0, traderecord.getMermoney(), user.getId());
					userService.updateUserEarnings(0, traderecord.getManmoney(), manid);
					try {
						userService.updateMerAmount(0, traderecord.getMermoney(), user.getId());
						userService.updateMerAmount(0, traderecord.getManmoney(), manid);
					} catch (Exception e) {
						logger.warn("商户总计更改出错");
					}
					merchantDetailService.insertMerEarningDetail(user.getId(), traderecord.getMermoney(), user.getEarnings(), 
					  incoins.getOrdernum(), new Date(), MerchantDetail.INCOINSSOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
					merchantDetailService.insertMerEarningDetail(manuser.getId(), traderecord.getManmoney(), manuser.getEarnings(), incoins.getOrdernum(), 
							new Date(), MerchantDetail.INCOINSSOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
					if (wolfkey == 4) {
						tradeRecordService.insertToTrade(user.getId(), manid, 0, incoins.getOrdernum(), incoins.getMoney(), traderecord.getMermoney(),
								traderecord.getManmoney(), incoins.getEquipmentnum(), 2, 5, 2, equipmentService.getEquipmentById(incoins.getEquipmentnum()).getHardversion());
					} else {
						tradeRecordService.insertToTrade(user.getId(), manid, 0, incoins.getOrdernum(), incoins.getMoney(), traderecord.getMermoney(),
								traderecord.getManmoney(), incoins.getEquipmentnum(), 2, 3, 2, equipmentService.getEquipmentById(incoins.getEquipmentnum()).getHardversion());
					}
					System.out.println("--- 支付宝分成 脉冲退费  修改成功---");
				}
			}
		} else if (refundState == 4) {
			try {
				Money moneyrecord = moneyService.payMoneyinfo(id);
				moneyService.updateMoneyByOrder(moneyrecord.getOrdernum(), 8);
				User user = userService.selectUserById(moneyrecord.getUid());
				
				Double paymoney = CommUtil.toDouble(moneyrecord.getMoney());
				Double sendmoney = CommUtil.toDouble(moneyrecord.getSendmoney());
				Double tomoney = CommUtil.toDouble(moneyrecord.getTomoney());
				
				Integer merid = CommUtil.toInteger(user.getMerid());
				Double userbalance = CommUtil.toDouble(user.getBalance());
				Double usersendmoney = CommUtil.toDouble(user.getSendmoney());
				
				userbalance = CommUtil.addBig(userbalance, paymoney);
				usersendmoney = CommUtil.addBig(usersendmoney, sendmoney);
				if(userbalance<0) userbalance = 0.00;
				if(usersendmoney<0) usersendmoney = 0.00;
				Double balance = CommUtil.addBig(userbalance, usersendmoney);
				Double opertomoney = CommUtil.addBig(paymoney, sendmoney);
				Double topupbalance = userbalance;
				Double givebalance = usersendmoney;
				user.setBalance(userbalance);
				user.setSendmoney(usersendmoney);
//				Double walletmo = (user.getBalance()*100-moneyrecord.getTomoney()*100)/100;
//				if(walletmo<0) walletmo = 0.00;
//				user.setBalance(walletmo);
				userService.updateUserById(user);
				if (moneyrecord.getPaytype() != 8) {
					TradeRecord traderecord = tradeRecordService.getTraderecord(moneyrecord.getOrdernum());
					Integer manid = traderecord.getManid();
					User meruser = userService.selectUserById(user.getMerid());
					generalDetailService.insertGenWalletDetail(user.getId(), merid, paymoney, sendmoney, opertomoney, balance, topupbalance, givebalance, moneyrecord.getOrdernum(), new Date(), 6);
//					generalDetailService.insertGenWalletDetail(user.getId(),  user.getMerid(), moneyrecord.getTomoney(), walletmo, moneyrecord.getOrdernum(), new Date(), 6);
					addAPartEarn(null, traderecord.getMoney(), user.getMerid(), user.getAid(), 2, 0);
					if( null==manid || manid==0){
						userService.updateUserEarnings(0, moneyrecord.getMoney(), meruser.getId());
						meruser.setEarnings((meruser.getEarnings() * 100 - moneyrecord.getMoney() * 100) / 100);
						merchantDetailService.insertMerEarningDetail(meruser.getId(), moneyrecord.getMoney(), meruser.getEarnings(), 
								moneyrecord.getOrdernum(), new Date(), MerchantDetail.WALLETSOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
						tradeRecordService.insertTrade(meruser.getId(), user.getId(), moneyrecord.getOrdernum(), moneyrecord.getMoney(), moneyrecord.getMoney(),
								"钱包", 4, 3, 2, "00");
						System.out.println("==================钱包退费记录结束");
					}else{
						User manuser = userService.selectUserById(manid);
						userService.updateUserEarnings(0, traderecord.getMermoney(), meruser.getId());
						userService.updateUserEarnings(0, traderecord.getManmoney(), manid);
						manuser.setEarnings((manuser.getEarnings() * 100 - traderecord.getManmoney() * 100) / 100);
//					userService.updateUserById(manuser);
//					if(user.getId()==manuser.getId()) meruser = userService.selectUserById(user.getMerid());
						meruser.setEarnings((meruser.getEarnings() * 100 - traderecord.getMermoney() * 100) / 100);
//					userService.updateUserById(meruser);
						merchantDetailService.insertMerEarningDetail(meruser.getId(), traderecord.getMermoney(), meruser.getEarnings(), 
								moneyrecord.getOrdernum(), new Date(), MerchantDetail.WALLETSOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
						merchantDetailService.insertMerEarningDetail(manuser.getId(), traderecord.getManmoney(), manuser.getEarnings(), 
								moneyrecord.getOrdernum(), new Date(), MerchantDetail.WALLETSOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
						tradeRecordService.insertToTrade(meruser.getId(), manid, moneyrecord.getUid(), moneyrecord.getOrdernum(), moneyrecord.getMoney(), 
								traderecord.getMermoney(),traderecord.getManmoney(), "钱包", 4, 3, 2, "00");
						System.out.println("==================钱包退费分成记录结束");
					}
				}
				System.out.println("发送消息");
				String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=4&id="+moneyrecord.getId();
				String phone = getTempPhone(CommUtil.toString(user.getAid()), user.getMerid(), 3);
				TempMsgUtil.returnMsg("钱包退费", moneyrecord.getUid(), phone, moneyrecord.getOrdernum(), urltem, StringUtil.toDateTime(), moneyrecord.getMoney());
				System.out.println("发送消息完成");
				map.put("ok", "ok");
			} catch (Exception e) {
				map.put("ok", "error");
			}
		}else if(refundState == 5){
			OnlineCardRecord onlinerecode = onlineCardRecordService.selectOnlineCard(id);
			OnlineCard oncard = onlineCardService.selectOnlineCardByCardID(onlinerecode.getCardID());
			
			Double paymoney = CommUtil.toDouble(onlinerecode.getMoney());
			Double accountmoney = CommUtil.toDouble(onlinerecode.getAccountmoney());
			Double sendmoney = CommUtil.subBig(accountmoney, paymoney);
//			Double sendmoney = CommUtil.toDouble(cardRecord.getSendmoney());
			
			Integer merid = CommUtil.toInteger(oncard.getMerid());
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
//			double mone = oncard.getMoney()-onlinerecode.getAccountmoney();
//			oncard.setMoney(mone);
			oncard.setMoney(topupbalance);
			oncard.setSendmoney(givebalance);
			onlineCardService.updateOnlineCardBycard(oncard);
			System.out.println("在线卡退费");

			onlinerecode.setFlag(2);
			onlineCardRecordService.updateRecord(onlinerecode);
			
			onlinerecode.setId(null);
			onlinerecode.setType(7);
			onlinerecode.setTopupbalance(topupbalance);
			onlinerecode.setGivebalance(givebalance);
			onlinerecode.setBalance(cardbalance);
			onlinerecode.setBalance(cardbalance);
			onlinerecode.setCreateTime(new Date());
			onlineCardRecordService.additionOnlineCardRecord(onlinerecode);
			if (oncard.getMerid() != null && oncard.getMerid() != 0) {
				if (oncard.getStatus()!=0 && onlinerecode.getType() == 7 && onlinerecode.getFlag()==2) {
					TradeRecord traderecord = tradeRecordService.getTraderecord(onlinerecode.getOrdernum());
					Integer manid = traderecord.getManid();
					User meruser = userService.selectUserById(onlinerecode.getMerid());
					addAPartEarn(null, traderecord.getMoney(), oncard.getMerid(), oncard.getAid(), 3, 0);
					if( null==manid || manid==0){
						System.out.println("********************在线卡退费不分成");
						meruser.setEarnings((meruser.getEarnings() * 100 - onlinerecode.getMoney() * 100) / 100);
						userService.updateUserById(meruser);
						merchantDetailService.insertMerEarningDetail(meruser.getId(), onlinerecode.getMoney(), meruser.getEarnings(), 
								ordernum, new Date(), MerchantDetail.ONLINESOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
						tradeRecordService.insertTrade(meruser.getId(), onlinerecode.getUid(), ordernum, onlinerecode.getMoney(), onlinerecode.getMoney(),
								traderecord.getCode(), 5, 3, 2, null);
					}else{
						System.out.println("*********************在线卡退费分成");
						User manuser = userService.selectUserById(manid);
						manuser.setEarnings((manuser.getEarnings() * 100 - traderecord.getManmoney() * 100) / 100);
						userService.updateUserById(manuser);
						if(meruser.getId()==manuser.getId()) meruser = userService.selectUserById(traderecord.getMerid());
						meruser.setEarnings((meruser.getEarnings() * 100 - traderecord.getMermoney() * 100) / 100);
						userService.updateUserById(meruser);
						merchantDetailService.insertMerEarningDetail(meruser.getId(), traderecord.getMermoney(), meruser.getEarnings(), 
								ordernum, new Date(), MerchantDetail.ONLINESOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
						merchantDetailService.insertMerEarningDetail(manuser.getId(), traderecord.getManmoney(), manuser.getEarnings(), 
								ordernum, new Date(), MerchantDetail.ONLINESOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
						tradeRecordService.insertToTrade(meruser.getId(), manid, traderecord.getUid(), ordernum, onlinerecode.getMoney(), 
								traderecord.getMermoney(),traderecord.getManmoney(), traderecord.getCode(), 5, 3, 2, null);
					}
				}
				map.put("ok", "ok");
			}else{
				map.put("ok", "error");
			}
		}
		map.put("ok", "ok");
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
//				String percent = CommUtil.toString(item.get("percent"));
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
			userService.updateUserEarnings(type, money, user.getId());//商户收益  1为加  0为减
			try {
				userService.updateMerAmount(type, money, user.getId());//商户收益总额  1为加  0为减
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
				tempid = equipment.getTempid();
			}else if(type==3){
				Area area = areaService.selectByIdArea(CommUtil.toInteger(argumentval));
				tempid = area.getTempid();
			}else if(type==4){
				OnlineCard online = onlineCardService.selectOnlineCardByCardID(CommUtil.toString(argumentval));
				Integer vawalt = online.getRelevawalt();
				if(vawalt==1){
					type=3;
					Area area = areaService.selectByIdArea(CommUtil.toInteger(argumentval));
					tempid = area.getTempid();
				}else{
					Area area = areaService.selectByIdArea(online.getUid());
					tempid = area.getTempid();
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
//						Integer num = 1;
//						System.out.println("num===" + num);
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
	
}
