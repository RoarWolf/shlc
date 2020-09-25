package com.hedong.hedongwx.web.controller.wechat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.ParseException;
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
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.PackageMonth;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.service.AllPortStatusService;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.BasicsService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.GeneralDetailService;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.service.MoneyService;
import com.hedong.hedongwx.service.OnlineCardRecordService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.SystemSetService;
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
import com.hedong.hedongwx.utils.SendMsgUtil;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.TempMsgUtil;
import com.hedong.hedongwx.utils.WXPayConfigImpl;
import com.hedong.hedongwx.utils.WeiXinConfigParam;
import com.hedong.hedongwx.utils.WeixinUtil;
import com.hedong.hedongwx.utils.XMLUtil;
import com.hedong.hedongwx.web.controller.EquipmentController;
import com.hedong.hedongwx.web.controller.HelloController;
import com.hedong.hedongwx.web.controller.WxPayController;

import net.sf.json.JSONObject;

@Controller
@RequestMapping({ "/charge" })
public class ChargeRecordController {

	private final Logger logger = LoggerFactory.getLogger(ChargeRecordController.class);
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private AllPortStatusService allPortStatusService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private GeneralDetailService generalDetailService;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private SystemSetService systemSetService;
	@Autowired
	private BasicsService basicsService;
	@Autowired
	private OnlineCardService onlineCardService;
	@Autowired
	private OnlineCardRecordService onlineCardRecordService;
	@Autowired
	private MerchantDetailService merchantDetailService;
	
	/** 获取用户信息
	 * @throws IOException 
	 * @throws ParseException */
	@RequestMapping("/getuserinfo")
	public String getuserinfo(Model model) throws ParseException, IOException {
		try {
			String code = this.request.getParameter("code");
			// 利用code获取openid和access_token
			String str = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WeiXinConfigParam.APPID
					+ "&secret=" + WeiXinConfigParam.APPSECRET + "&code=" + code + "&grant_type=authorization_code";
			JSONObject doGetStr = WeixinUtil.doGetStr(str);
			if (!doGetStr.has("openid")) {
				return "erroruser";
			}
			String openid = doGetStr.getString("openid");
			String accesstoken = doGetStr.getString("access_token");
			// 利用code获取openid和access_token
			String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accesstoken + "&openid="
					+ openid + "&lang=zh_CN";

			JSONObject doGetStr1 = WeixinUtil.doGetStr(getUserInfoUrl);
			User userByOpenid = this.userService.getUserByOpenid(openid);
			if (userByOpenid == null) {
				User user = new User();
				if (doGetStr1.has("unionid")) {
					String nickname = doGetStr1.getString("nickname");
					user.setUnionid(doGetStr1.getString("unionid"));
					String replaceEmoji = ByteUtils.replaceEmoji(nickname);
					user.setUsername(replaceEmoji);
				}
				user.setOpenid(openid);
				user.setEarnings(Double.valueOf(0.00));
				user.setLevel(1);
				user.setCreateTime(new Date());
				this.userService.addUser(user);
				User user2 = userService.getUserByOpenid(openid);
				this.request.getSession().setAttribute("user", user2);
				model.addAttribute("openiderror", "公众号已转移");
				model.addAttribute("name", "'自助充电平台'");
				return "openiderror";
//				return "redirect:/charge/queryCharging?uid=" + user2.getId();
			} else {
				if (userByOpenid.getLevel() == null || userByOpenid.getLevel() != 0) {
					model.addAttribute("openiderror", "公众号已转移");
					model.addAttribute("name", "'自助充电平台'");
					return "openiderror";
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
				this.request.getSession().setAttribute("user", userByOpenid);
				return "redirect:/charge/queryCharging?uid=" + userByOpenid.getId();
			} 
		} catch (Exception e) {
			logger.warn("扫码添加用户失败：" + e.getMessage() + "---报错位置：" + ByteUtils.getLineNumber(e));
			return "erroruser";
		}
	}


//	/**
//	 * 通过充电用户id查询正在充电中信息
//	 * @param uid 用户id
//	 * @param model
//	 * @return {@link String}
//	 */
//	@RequestMapping("queryCharging")
//	public String queryCharging(Integer uid, Model model) {
//		List<ChargeRecord> chargingList = chargeRecordService.queryChargingByUid(uid, 0);
//		List<ChargeRecord> chargedList = chargeRecordService.queryChargedByUid(uid, 0);
//		List<ChargeRecord> chargeRecords = new ArrayList<>();
//		model.addAttribute("chargedList", chargedList);
//		model.addAttribute("uid", uid);
//		model.addAttribute("manageclick", 1);
//		String str = "1";
//		if (chargingList.size() > 0) {
//			for (ChargeRecord chargeRecord : chargingList) {
//				String equipmentnum = chargeRecord.getEquipmentnum();
//				Integer port = chargeRecord.getPort();
//				long begintime = chargeRecord.getBegintime().getTime();
//				long currentTime = System.currentTimeMillis();
//				long intervaltime = currentTime - begintime;
//				// 判断充电记录大于48小时的订单，还未结束的不显示
//				if (intervaltime > (48 * 60 * 60 * 1000)) {
//					continue;
//				}
//				//根据设备和端口获取所有端口状态
////				AllPortStatus allPortStatus = allPortStatusService.findPortStatusByEquipmentnumAndPort(equipmentnum, port);
//				Map<String,String> allPortStatus = DisposeUtil.addPortStatus(equipmentnum, port);
//				if (allPortStatus == null) {//端口为空
//					continue;
//				} else if ("2".equals(allPortStatus.get("portStatus"))) {
//					str = "0";
//					//根据id查询总钱数
//					Double totalMoney = chargeRecordService.getChargingTotalMoney(chargeRecord.getId());
//					chargeRecord.setTotalMoney(totalMoney);
//					//获取当前功率
//					Short power = stringToShort(allPortStatus.get("power"));
//					//获取时间
//					Short time = stringToShort(allPortStatus.get("time"));
//					String predict = "";
//					if (power == 0 || power == null) {//判断功率是否为0或空
//						//预计剩余时间
//						predict = time + "";
//					} else {
//						Short elec = stringToShort(allPortStatus.get("elec"));//获取所有当前电量
//						Short surptime = (short) (elec * 1000 / power * 60);
//						if (surptime < time && surptime > 0) {
//							predict = surptime + "";
//						} else {
//							predict = time + "";
//						}
//					}
//					System.out.println("剩余时间---" + predict + "分钟");
//					allPortStatus.put("predict", predict);
//					chargeRecord.setAllPortStatus(allPortStatus);
//					chargeRecords.add(chargeRecord);
//				}
//			}
//			model.addAttribute("chargingList", chargeRecords);
//		}
//		model.addAttribute("errormsg", str);
//		return "record/chargingByUid";
//	}
	
	/**
	 * @Description： 查询指定人员的充电记录信息（Ajax）
	 * @author： origin 创建时间：   2019年8月23日 上午10:23:04
	 */
	@RequestMapping({ "/chargeRecordInfo" })
	@ResponseBody
	public Object chargeRecordInfo(Integer uid, Integer offset, Integer page, Model model) {
		Object result = chargeRecordService.chargeRecordInfo(uid, offset, page);
		return result;
	}

	// 充电记录(多条记录查询)对应人员的充电信息详情
	@RequestMapping({ "/chargeRecordList" })
	public String chaRecordList(Integer uid, Model model) {
		List<ChargeRecord> chargeRecord = chargeRecordService.chargeRecordByUid(uid);
		model.addAttribute("chargeRecord", chargeRecord);
		return "record/chargerecord";
	}

	// 充电记录(单条记录查询) 输出详情
	@RequestMapping({ "/chargeRecordOne" })
	public String chaRecordListOne(Integer id, Model model) {

		ChargeRecord chargeinfo = chargeRecordService.chargeRecordOne(CommUtil.toInteger(id));
		String devicenum = CommUtil.toString(chargeinfo.getEquipmentnum());
		Map<String, Object> deviceInfo = equipmentService.selectEquipAreaInfo(devicenum);
		model.addAttribute("deviceInfo", deviceInfo);
		model.addAttribute("chargeinfo", chargeinfo);
		Double expenditure = CommUtil.toDouble(chargeinfo.getExpenditure());
		Double refundMoney = CommUtil.toDouble(chargeinfo.getRefundMoney());
		Double useMoney = 0.0;
		if (expenditure > refundMoney) {
			useMoney = CommUtil.subBig(expenditure, refundMoney);
			model.addAttribute("useMoney", useMoney);
		} else {
			useMoney = expenditure;
			model.addAttribute("useMoney", useMoney);
		}
		return "record/chargerecordinfo";
	}
	/**
	 * 更新正在充电信息
	 * @param code 设备号
	 * @param port 端口号
	 * @param type 判断更新方式
	 * @param model
	 * @return
	 */
	
	@RequestMapping("updateChargeData")
	@ResponseBody
	public Object updateChargeData(String code, String port, Model model) {
		//创建一个Map用来返回postMap
		Map portMap=new HashMap<>();
		//当前时间
		long currentTime = System.currentTimeMillis();
		//使用发送信息工具
		SendMsgUtil.send_21(Byte.parseByte(port), code);
		boolean flag = true;
		int temp = 0;
		while (flag) {
			try {
				//如果响应时间大于10，返回错误
				if (temp > 10) {
					Map<String, String> map = new HashMap<>();
					map.put("wolfcode", "1001");
					map.put("wolfmsg", "网络连接失败，稍后请重试...");
					return map;
				}
				Map map = Server.recycleMap.get(code);
				//判断设备是否为空
				if (map == null) {
					temp++;
					Thread.sleep(1000);
					continue;
				}
				Map portmap = (Map) map.get("port" + port);
				//判断端口是否为空
				if (portmap == null) {
					temp++;
					Thread.sleep(1000);
					continue;
				}
				String updatetime = (String) portmap.get("updatetime");
				long updatetimelong = Long.parseLong(updatetime);
				//判断更新时间是否为null或""
				if (updatetime == null || "".equals(updatetime)) {
					temp++;
					Thread.sleep(1000);
					continue;
				} else if ((updatetimelong - currentTime) > 0 && (updatetimelong - currentTime) < 10000) {
					flag = false;
					//发送Ajax请求把端口数据放进Map集合
					portMap.putAll(portmap);
					break;
				} else {
					temp++;
					Thread.sleep(1000);
					continue;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return portMap;
	}
	/** 断电  */
	@RequestMapping("remoteOutage")
	@ResponseBody
	public Map<String, String> remoteOutage(String code, String port,Integer chargeid) {
		Map<String,String> map = new HashMap<>();
		map.put("port", port + "");
		map.put("code", code);
		boolean checkTimeIfExceed = DisposeUtil.checkTimeIfExceed(code, 2);
		if (!checkTimeIfExceed) {
			SendMsgUtil.send_13(Byte.parseByte(port), code);
		}
		long currentTime = System.currentTimeMillis();
//		ChargeRecord chargeRecord = chargeRecordService.getChargeRecordById(chargeid);
		List<ChargeRecord> chargeRecordList = chargeRecordService.selectChargeAndContinueById(chargeid);
		ChargeRecord chargeRecord = chargeRecordList.get(chargeRecordList.size() - 1);
//		AllPortStatus allPortStatus = allPortStatusService.findPortStatusByEquipmentnumAndPort(chargeRecord.getEquipmentnum(), chargeRecord.getPort());
		Map<String, String> allPortStatus = DisposeUtil.addPortStatus(chargeRecord.getEquipmentnum(), chargeRecord.getPort());
//		boolean flag = true;
		Map<String, String> backmap = new HashMap<>();
//		int temp = 0;
//		while (flag) {
//			try {
//				if (temp > 20) {
//					backmap.put("status", "0");
//					backmap.put("errinfo", "网络连接失败，稍后请重试...");
//					break;
//				}
//				Map map = Server.stopmap.get(code);
//				if (map == null) {
//					temp++;
//					Thread.sleep(1000);
//					continue;
//				}
//				Map portmap = (Map) map.get("port" + port);
//				if (portmap == null) {
//					temp++;
//					Thread.sleep(1000);
//					continue;
//				}
//				String updatetime = (String) portmap.get("updatetime");
//				long updatetimelong = Long.parseLong(updatetime);
//				if (updatetime == null || "".equals(updatetime)) {
//					temp++;
//					Thread.sleep(1000);
//					continue;
//				} else if ((updatetimelong - currentTime) > 0 && (updatetimelong - currentTime) < 20000) {
					long begintime = chargeRecord.getBegintime().getTime();
					Integer maxPower = chargeRecordService.selectMaxPowerByChargeid(chargeid);
					Integer consumeQuantity = chargeRecord.getConsumeQuantity();
					Integer consumeTime = chargeRecord.getConsumeTime();
					Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
					Double refundMoney = 0.0;
					if ((currentTime - begintime) > 0 && ((currentTime - begintime) < 180000 || 
							((currentTime - begintime) < 600000 && maxPower <= 20)) || (checkTimeIfExceed 
									&& consumeQuantity == 0 && consumeTime == 0)) {
						if (chargeRecord.getNumber() != null && chargeRecord.getNumber() == 0) {
							if (chargeRecord.getPaytype() == 2) {
								wxRefund(chargeRecord.getExpenditure(), chargeRecord.getOrdernum(), chargeRecord);
							} else if (chargeRecord.getPaytype() == 1) {
								if (chargeRecord.getExpenditure() != 0) {
									walletRefund(chargeRecord.getExpenditure(), chargeRecord.getOrdernum(), chargeRecord);
								}
							}
						}
						backmap.put("status", "1");
					} else {
						int permit = 1;
						int norm = 1;
						Equipment equipment = equipmentService.getEquipmentAndAreaById(code);
						TemplateParent templateParent = templateService.getParentTemplateOne(equipment.getTempid());
						if (templateParent != null) {
							Integer permit2 = templateParent.getPermit();
							Integer common2 = templateParent.getCommon2();
							permit = permit2 == null ? 1 : permit2;
							norm = common2 == null ? 1 : common2;
						}
						logger.info("permit===" + permit + "---norm===" + norm);
						String hardversion = equipment.getHardversion();
						if (DisposeUtil.checkIfHasV3(equipment.getHardversion())) {
							Integer uid = chargeRecord.getUid();
//							String subFee = JedisUtils.get("uid" + code + port + uid);
//							Map<String,String> parse = (Map<String, String>) JSON.parse(subFee);
//							String totalfee = parse.get("totalfee");
//							double round = Double.parseDouble(totalfee);
							double round = SendMsgUtil.getV3FeeMoney(code, Byte.parseByte(port), uid, chargeRecord.getConsumeTime());
							String ordernum = chargeRecord.getOrdernum();
							Integer merchantid = chargeRecord.getMerchantid();
							Map<String, String> chargeInfoMap = JedisUtils.hgetAll(ordernum);
							if (DisposeUtil.checkMapIfHasValue(chargeInfoMap)) {
								String type = chargeInfoMap.get("type");
								if ("1".equals(type)) {//在线卡 只扣费
									paymoney = round;
									String cardid = chargeInfoMap.get("cardid");
									OnlineCard onlineCard = onlineCardService.selectOnlineCardByCardID(cardid);
									if (onlineCard != null) {
										Integer endChargeId = chargeRecord.getId();
										String cardID = onlineCard.getCardID();
										Integer merid = CommUtil.toInteger(onlineCard.getMerid());
										//============================================================================
										Double topupmoney = CommUtil.toDouble(onlineCard.getMoney());//充值金额
										Double sendmoney = CommUtil.toDouble(onlineCard.getSendmoney());//赠送金额
										Integer relevawalt = CommUtil.toInteger(onlineCard.getRelevawalt());
										String oppenid = null;
										if(relevawalt==1){
											User user = userService.selectUserById(uid);
											user = user == null ? new User() : user;
											topupmoney = CommUtil.toDouble(user.getBalance());
											sendmoney = CommUtil.toDouble(user.getSendmoney());
										}
										//======================================================
										Double consumemoney = CommUtil.toDouble(paymoney);
										//============================================================================
										Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额
										//============================================================================
										Double opermoney = consumemoney;//操作总额
										Double comparemoney = CommUtil.subBig(accountmoney, opermoney); //总额、操作比较金额差
										if(comparemoney>=0){//金额足够，扣费
											Double opertopupmoney = 0.00;	//操作充值金额
											Double opersendmoney = 0.00;	//操作赠送金额
											//==========================================================================
											Double topupbalance = 0.00;//充值余额
											Double sendbalance = 0.00;//赠送余额
											Double accountbalance = 0.00;//账户余额
											//==========================================================================
											Double contrastmoney = CommUtil.subBig(topupmoney, opermoney); //充值、操作比较金额差
											if(contrastmoney>=0){//此时说明用户在线卡充值金额足够
												opertopupmoney = opermoney;
												topupbalance = contrastmoney;
												opersendmoney = 0.00;
												sendbalance = sendmoney;
											}else{
												opertopupmoney = topupmoney;
												topupbalance = 0.00;
												opersendmoney = CommUtil.toDouble(Math.abs(contrastmoney));
												sendbalance = CommUtil.subBig(sendmoney, opersendmoney);
											}
											accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
											if(relevawalt==2){//不关联钱包
												OnlineCard onlineCard2 = new OnlineCard();
												onlineCard2.setId(onlineCard.getId());
												onlineCard2.setMoney(topupbalance);
												onlineCard2.setSendmoney(sendbalance);
												onlineCardService.updateOnlineCard(onlineCard2);
												onlineCardRecordService.insertOnlineCardRecord(uid, merchantid, ordernum, cardID, code, accountbalance, 
													opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 1, 1, 1, new Date(), uid);
												System.out.println("当前卡号为：" + cardID + "---实时操作金额为：" + opermoney + "元");
											}else if(relevawalt==1){//关联钱包
												//修改用户钱包余额
												userService.updateBalanceByOpenid(topupbalance, sendbalance, oppenid, null);
												//3、添加钱包明细
												generalDetailService.insertGenDetail(uid, merid, opertopupmoney, opersendmoney, opermoney, 
													accountbalance, topupbalance, sendbalance, ordernum, new Date(), 2, cardID);
												onlineCardRecordService.insertOnlineCardRecord(uid, merchantid, ordernum, cardID, code, accountbalance, 
														opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 1, 1, 1, new Date(), uid);
												System.out.println("当前卡号为：" + cardID + "---实时操作金额为：" + opermoney + "元");
											}
										}else{//金额不够，返回
											System.out.println("订单号   "+ordernum+"  该笔订单卡号为：" + cardID + "余额不足---实时操作金额为：" + opermoney + "元" + "当前余额为 ：  "+accountmoney+ "元");
										}
										chargeRecordService.updateNumberById(null, null, endChargeId, null, round, null, CommUtil.toDateTime(), null);
									}
								}
							} else {
								logger.info(ordernum + "不是在线卡计费");
								if (chargeRecord.getExpenditure() == 0) {
									logger.info(ordernum + "开始按时间计费");
									paymoney = round;
									User uUser = userService.selectUserById(chargeRecord.getUid());
									Double userBalance = CommUtil.toDouble(uUser.getBalance());
									Double usersendmoney = CommUtil.toDouble(uUser.getSendmoney());
									Double opermoney = CommUtil.toDouble(round);
									Double opersendmoney = 0.00;
									Double opertomoney = CommUtil.subBig(opermoney, opersendmoney);
									Double topupbalance = CommUtil.subBig(userBalance, opermoney);
									Double givebalance = CommUtil.addBig(opersendmoney, usersendmoney);
									Double operbalance = CommUtil.addBig(topupbalance, givebalance);
									
									uUser.setBalance(topupbalance);
									uUser.setSendmoney(givebalance);
									// 1、修改用户钱包金额
									userService.updateUserById(uUser);
									// 2、修改充电记录结束订单
									chargeRecordService.updateNumberById(null, null, chargeRecord.getId(), null, round, null, CommUtil.toDateTime(), null);
									// 3、添加用户钱包明细
									generalDetailService.insertGenWalletDetail(uUser.getId(), chargeRecord.getMerchantid(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, chargeRecord.getOrdernum(), new Date(), 2);
									// 4、修改交易记录金额
									tradeRecordService.updateTradeMoney(round, ordernum);
								} else {
									logger.info(ordernum + "开始部分退款");
									double allMoney = 0.0;
									for (ChargeRecord charge : chargeRecordList) {
										allMoney += charge.getExpenditure();
									}
									paymoney = allMoney;
									refundMoney = CommUtil.subBig(allMoney, round);
									User uUser = userService.selectUserById(chargeRecord.getUid());
									Double userBalance = CommUtil.toDouble(uUser.getBalance());
									Double usersendmoney = CommUtil.toDouble(uUser.getSendmoney());
									Double opermoney = CommUtil.toDouble(refundMoney);
									Double opersendmoney = 0.00;
									Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
									Double topupbalance = CommUtil.addBig(opermoney, userBalance);
									Double givebalance = CommUtil.addBig(opersendmoney, usersendmoney);
									Double operbalance = CommUtil.addBig(topupbalance, givebalance);
									
									uUser.setBalance(topupbalance);
									uUser.setSendmoney(givebalance);
									// 1、修改用户钱包金额
									userService.updateUserById(uUser);
									// 2、添加用户钱包明细
									generalDetailService.insertGenWalletDetail(uUser.getId(), chargeRecord.getMerchantid(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, chargeRecord.getOrdernum(), new Date(), 5);
									double clacRefundMoney = refundMoney;
									for (ChargeRecord charge : chargeRecordList) {
										Integer id = charge.getId();
										String ordernum2 = charge.getOrdernum();
										Double expenditure = charge.getExpenditure();
										System.out.println("refundMoney===" + clacRefundMoney);
										System.out.println("expenditure===" + expenditure);
										if (clacRefundMoney > expenditure) {
											// 3、修改充电记录改为全额退款
											chargeRecordService.updateNumberById(null, 1, id, null, null, expenditure, CommUtil.toDateTime(), CommUtil.toDateTime());
											// 4、添加交易记录
											tradeRecordService.insertTrade(merchantid, uid, ordernum2, expenditure, 0.00, code, 1, 1, 2, hardversion);
										} else if (clacRefundMoney > 0) {
											// 3、修改充电记录改为部分退款
											chargeRecordService.updateNumberById(null, 2, id, null, null, clacRefundMoney, CommUtil.toDateTime(), CommUtil.toDateTime());
											// 4、添加交易记录
											tradeRecordService.insertTrade(merchantid, uid, ordernum2, clacRefundMoney, 0.00, code, 1, 1, 2, hardversion);
										}
										System.out.println("refundMoney===" + refundMoney);
										clacRefundMoney = CommUtil.subBig(clacRefundMoney, expenditure);
									}
								}
							}
							SendMsgUtil.resetChargeData(code, chargeRecord.getPort(), uid, 0, 0);
						} else if (permit == 1) {
							if (chargeRecord.getNumber() != null && chargeRecord.getNumber() == 0) {
								User user = userService.selectUserById(chargeRecord.getUid());
								Integer merid = user.getMerid() == null ? 0 : user.getMerid();
								if (chargeRecord.getMerchantid().equals(merid)) {
									Double surpTime = Integer.parseInt(allPortStatus.get("time")) + 0.0;
									int power = Integer.parseInt(allPortStatus.get("power"));
									Double elec = Integer.parseInt(allPortStatus.get("elec")) + 0.0;
									Double allTime = 0.0;
									Double quantity = 0.0;
									Double allMoney = 0.0;
									for (ChargeRecord charge : chargeRecordList) {
										allTime += Integer.parseInt(charge.getDurationtime());
										quantity += charge.getQuantity();
										allMoney += charge.getExpenditure();
									}
									paymoney = allMoney;
									Double useTime = allTime - surpTime;
									if (elec != null && elec != 0) {
//										Double refundMoney = 0.0;
										refundMoney = SendMsgUtil.clacRefund(code,(short) power, quantity + 0.0, elec + 0.0, allTime, surpTime, allMoney,norm,chargeRecord.getBegintime().getTime(),System.currentTimeMillis());
										double round = (Math.round(refundMoney * 100) + 0.0) / 100;
										logger.info(chargeRecord.getOrdernum() + "退款金额===" + round);
										for (ChargeRecord charge : chargeRecordList) {
											if (round > charge.getExpenditure()) {
												User uUser = userService.selectUserById(chargeRecord.getUid());
//												Double calcBalance = (uUser.getBalance() * 100 + charge.getExpenditure() * 100) / 100;
												
												Double userBalance = CommUtil.toDouble(uUser.getBalance());
												Double usersendmoney = CommUtil.toDouble(uUser.getSendmoney());
												Double opermoney = CommUtil.toDouble(charge.getExpenditure());
												Double opersendmoney = 0.00;
												Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
												Double topupbalance = CommUtil.addBig(opermoney, userBalance);
												Double givebalance = CommUtil.addBig(opersendmoney, usersendmoney);
												Double operbalance = CommUtil.addBig(topupbalance, givebalance);
												
												uUser.setBalance(topupbalance);
												uUser.setSendmoney(givebalance);
												// 1、修改用户钱包金额
												userService.updateUserById(uUser);
												// 2、修改充电记录改为部分退款
												chargeRecordService.updateNumberById(null, 2, charge.getId(), null, opermoney, opermoney, CommUtil.toDateTime(), CommUtil.toDateTime());
												// 3、添加退款记录
												moneyService.payMoneys( uUser.getId(), charge.getOrdernum(), 3, 1, opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, "wallet");
												moneyService.insertWalletInfo(uUser.getId(), charge.getMerchantid(), charge.getOrdernum(), 1, 3, 1, opermoney, opersendmoney, 
														opertomoney, operbalance, topupbalance, givebalance, new Date(), "");
												// 4、添加用户钱包明细
												generalDetailService.insertGenWalletDetail(uUser.getId(), charge.getMerchantid(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, charge.getOrdernum(), new Date(), 5);
//												generalDetailService.insertGenWalletDetail(uUser.getId(), charge.getMerchantid(), charge.getExpenditure(), 0.00, uUser.getBalance(), charge.getOrdernum(), new Date(), 5);
												// 5、添加交易记录
												tradeRecordService.insertTrade(charge.getMerchantid(), charge.getUid(), charge.getOrdernum(), charge.getExpenditure(), 0.00, charge.getEquipmentnum(), 1, 1, 2, equipmentService.getEquipmentById(code).getHardversion());
											} else if (round > 0) {
												User uUser = userService.selectUserById(chargeRecord.getUid());
//												Double calcBalance = (uUser.getBalance() * 100 + round * 100) / 100;
												Double userBalance = CommUtil.toDouble(uUser.getBalance());
												Double usersendmoney = CommUtil.toDouble(uUser.getSendmoney());
												Double opermoney = CommUtil.toDouble(round);
												Double opersendmoney = 0.00;
												Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
												Double topupbalance = CommUtil.addBig(opermoney, userBalance);
												Double givebalance = CommUtil.addBig(opersendmoney, usersendmoney);
												Double operbalance = CommUtil.addBig(topupbalance, givebalance);
												
												uUser.setBalance(topupbalance);
												uUser.setSendmoney(givebalance);
												// 1、修改用户钱包金额
												userService.updateUserById(uUser);
												// 2、修改充电记录改为部分退款
												chargeRecordService.updateNumberById(null, 2, charge.getId(), null, CommUtil.toDouble(charge.getExpenditure()), round, CommUtil.toDateTime(), CommUtil.toDateTime());
												// 3、添加退款记录
												moneyService.payMoneys( uUser.getId(), charge.getOrdernum(), 3, 1, opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, "wallet");
												moneyService.insertWalletInfo(uUser.getId(), charge.getMerchantid(), charge.getOrdernum(), 1, 3, 1, opermoney, opersendmoney, 
														opertomoney, operbalance, topupbalance, givebalance, new Date(), "");
												// 4、添加用户钱包明细
												generalDetailService.insertGenWalletDetail(uUser.getId(), charge.getMerchantid(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, charge.getOrdernum(), new Date(), 5);
//												generalDetailService.insertGenWalletDetail(uUser.getId(), charge.getMerchantid(), round, 0.00, uUser.getBalance(), charge.getOrdernum(), new Date(), 5);
												// 5、添加交易记录
												tradeRecordService.insertTrade(charge.getMerchantid(), charge.getUid(), charge.getOrdernum(), round, 0.00, charge.getEquipmentnum(), 1, 1, 2, equipmentService.getEquipmentById(code).getHardversion());
											} else {
												break;
											}
											logger.info("退款金额===" + round);
											round -= charge.getExpenditure();
										}
									} else {
										logger.info("电量已使用完不予退款:设备号===" + code);
									}
								} else {
						logger.info("用户绑定商户与设备绑定商户不一致不予退款:设备号===" + code + "---用户绑定商户id===" + merid + "---设备绑定商户id==="
								+ chargeRecord.getMerchantid());
								}
							}
						} else {
							logger.info("商户设置不予钱包退款:设备号===" + code);
						}
						backmap.put("status", "1");
						chargeRecordService.updateChargeEndtimeById(chargeid);
						
						Integer uid = CommUtil.toInteger(chargeRecord.getUid());
						Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());

						//ORIGIN  ToUpload	远程断电 没有部分退款
						String firstdata = "尊敬的用户，您的充电已经结束。";
						if (refundMoney > 0 && refundMoney< paymoney) {
							firstdata = "尊敬的用户，您的充电已经结束，已退款(¥" + CommUtil.toDouble(refundMoney) + ")。 \r\n 注：该退款到平台钱包里。";
						}
						Double consumemoney = CommUtil.subBig(paymoney, refundMoney);
						Integer orderid = CommUtil.toInteger(chargeRecord.getId());
						String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
						String startTime = CommUtil.toDateTime(chargeRecord.getBegintime());
						String endTime = CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date());
						String chargetime = "开始时间："+startTime + "\r\n结束时间：" + endTime;
						System.out.println(chargetime);
						String devicenum = code;
						String deviceport = CommUtil.toString(port);
						Integer devicetempid = CommUtil.toInteger(equipment.getTempid());
						Integer deviceaid = CommUtil.toInteger(equipment.getAid());
						String devicename = CommUtil.toString(equipment.getRemark());
						String areaaddress = CommUtil.toString(equipment.getAddress());
						String servicphone = systemSetService.getServicePhone(devicetempid, deviceaid, merid);
						Integer resultinfo = 4;
						//ORIGIN  ToUpload	远程断电 结束原因更改为 ： 显示充电线脱落
						TempMsgUtil.endChargeSendMess(firstdata, ordernum, orderid, resultinfo, merid, uid, consumemoney, 
								chargetime, devicenum, deviceport, devicename, deviceaid, areaaddress, servicphone);
//						systemSetService.sendMmessChargeEnd(ordernum, orderid, 1, 1, merid, uid, consumemoney, chargetime,
//								devicenum, port, devicename, deviceaid, areaaddress, devicetempid);
					}
					int surpTime = Integer.parseInt(allPortStatus.get("time")) + 0;
					equipmentService.insertRealRecord(chargeid, chargeRecord.getUid(), chargeRecord.getMerchantid(),
							code, Integer.parseInt(port), 1, surpTime, Integer.parseInt(allPortStatus.get("elec")), Integer.parseInt(allPortStatus.get("power")), -1.0);
//					allPortStatus.setEquipmentnum(code);
//					allPortStatus.setPort(Integer.parseInt(port));
//					allPortStatus.setPortStatus((byte) 1);
//					allPortStatus.setElec((short) 0);
//					allPortStatus.setPower((short) 0);
//					allPortStatus.setTime((short) 0);
//					allPortStatusService.updateAllPortStatus(allPortStatus);
//					Server.stopmap.get(code).remove("port" + port);
//					flag = false;
//					break;
//				} else {
//					temp++;
//					Thread.sleep(1000);
//					continue;
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		return backmap;
	}

	/**
	 * 用户点击续充的按钮
	 * @param chargeid 充电记录的id
	 * @param model
	 * @return
	 */
	@RequestMapping("chargeContinue")
	public String chargeContinue(Integer chargeid, Model model) {
		ChargeRecord chargeRecord = chargeRecordService.chargeRecordOneContinueEnd(chargeid);
		model.addAttribute("chargeRecord", chargeRecord);
		Equipment equipment = equipmentService.getEquipmentById(chargeRecord.getEquipmentnum());
		//设备到期时间
		Date expirationTime = equipment.getExpirationTime();
		//origin 续充查询模板
		// 获取设备号
		String devicenum = CommUtil.toString(equipment.getCode());
		Integer tempid = CommUtil.toInteger(equipment.getTempid());
		String version = CommUtil.toString(equipment.getHardversion());
		
		Integer dealid = CommUtil.toInteger(chargeRecord.getMerchantid());
		Double paymponey = CommUtil.toDouble(chargeRecord.getExpenditure());
		User dealeruser = new User();
		if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
		String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
		String servephone = CommUtil.toString(dealeruser.getServephone());
		
		Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, dealid, devicenum, version);
		String brandname = CommUtil.toString(DirectTemp.get("remark"));
		String temphone = CommUtil.toString(DirectTemp.get("common1"));
		servephone = basicsService.getServephoneData(temphone, null, servephone, dealerphne);
		Integer nowtempid = CommUtil.toInteger(DirectTemp.get("id"));
		Integer temporaryc = CommUtil.toInteger(DirectTemp.get("permit"));
		String chargeInfo = CommUtil.toString(DirectTemp.get("chargeInfo"));
		Integer walletpay = CommUtil.toInteger(DirectTemp.get("walletpay"));
		List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
		tempson = tempson == null ? new ArrayList<>() : tempson;
		model.addAttribute("phonenum", servephone);
		model.addAttribute("brandname", brandname);
		model.addAttribute("DirectTemp", DirectTemp);
		model.addAttribute("templatelist", tempson);
		model.addAttribute("chargeInfo", chargeInfo);
		model.addAttribute("ifwallet", walletpay);
		//设备到期时间不为空且当前时间大于到期时间
		if(expirationTime != null && new Date().after(expirationTime)){
			model.addAttribute("errorinfo", "对不起，当前设备已到期");
			model.addAttribute("time", expirationTime);
			model.addAttribute("code", equipment.getCode());
			//提示用户设备到期更换设备
			return "chargeporterror";
		}
		// 判断设备号不能为空
		if(devicenum == null || "".equals(devicenum) || devicenum.length() != 6){
			return "erroruser";
		}
		boolean data = userEquipmentService.subMerByCode(devicenum);
		if(data){
			// 微信code
			String wxCode = CommUtil.toString(request.getParameter("code"));
			// 设备号作为重定向回来的标识
			String deviceNum = CommUtil.toString(request.getParameter("deviceNum"));
			if(wxCode == null && deviceNum == null){
				logger.info("1-重定向记录id"+chargeid+"===设备号"+devicenum);
				String url = "/insideChargeContinue?chargeid="+chargeid+"&equcode="+devicenum;
				return "redirect:"+url;
			}
			if(wxCode != null && deviceNum != null){
				logger.info("2-重定回来");
				try {
					JSONObject doGetStr = WeixinUtil.WeChatTokenSubMer(wxCode, devicenum);
					// 判读返回的数据是否有openid
					if(!doGetStr.has("openid")){
					model.addAttribute("openiderror", "1您的账户暂时无法访问");
					return "openiderror";
					}
					// 用户openid换取调取微信支付的参数
			    	String openid = doGetStr.getString("openid");
			    	// 查询服务商下的用户
			    	User subMer = userService.selectSubUserByOpenid(openid);
			    	// 用户为空就添加的子用户表中
			    	logger.info("用户的id=="+chargeRecord.getUid());
			    	if(subMer == null && chargeRecord.getUid() != null){
			    		User fuWuUser = new User();
			    		fuWuUser.setId(chargeRecord.getUid());
			    		fuWuUser.setOpenid(openid);
			    		userService.addSubUser(fuWuUser);
			    		User user = (User) request.getSession().getAttribute("user");
			    		user.setOpenid(openid);
						model.addAttribute("user", user);
						model.addAttribute("openid", openid);
			    	}else{
					    // 更新session中的用户信息
			    		User user = (User) request.getSession().getAttribute("user");
						model.addAttribute("user", user);
						model.addAttribute("openid", openid);
			    	}
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}else{
			User user = (User) request.getSession().getAttribute("user");
			user = userService.selectUserById(user.getId());
			request.getSession().setAttribute("user", user);
			model.addAttribute("user", user);
			model.addAttribute("openid", user.getOpenid());
		}
		

		Integer temptype = 1;//1：默认为 按金额  2: 为按时间
//		List<TemplateSon> tempson = templateService.getSonTemplateLists(tempid);
		if (DisposeUtil.checkIfHasV3(version)) {
			if(paymponey==0.00) temptype = 2;
			tempson = tempson == null ? new ArrayList<>() : tempson;
			List<TemplateSon> temtime =  new ArrayList<>();
			List<TemplateSon> temmoney =  new ArrayList<>();
			for(TemplateSon item : tempson){
				Integer type = CommUtil.toInteger(item.getType());
				if(type.equals(2)){
					temtime.add(item);
				}else if(type.equals(3)){
					temmoney.add(item);
				} 
			}
			model.addAttribute("temporaryc", temporaryc);
			model.addAttribute("temtime", temtime);
			model.addAttribute("temmoney", temmoney);
		}else{
			temptype = 0;
			for (TemplateSon templateSon : tempson) {
				if (templateSon.getMoney().doubleValue() == chargeRecord.getExpenditure().doubleValue()
						&& Integer.parseInt(chargeRecord.getDurationtime()) == templateSon.getChargeTime()
						&& templateSon.getChargeQuantity().intValue() == chargeRecord.getQuantity().intValue()) {
							model.addAttribute("tempid", templateSon.getId());
							model.addAttribute("tempindex", tempson.indexOf(templateSon));
				}
			}
		}
		model.addAttribute("temptype", temptype);
		model.addAttribute("uid", chargeRecord.getUid());
		SendMsgUtil.send_15(chargeRecord.getEquipmentnum());
		model.addAttribute("nowtime", System.currentTimeMillis() + "");
		//--------------------------------------------
//		User user = (User) request.getSession().getAttribute("user");
//		user = userService.selectUserById(user.getId());
//		request.getSession().setAttribute("user", user);
//		model.addAttribute("user", user);
		return "record/chargeContinue";
	}
	/**
	 * 公众号内用户使用微信付款续充
	 * @param chargeparam 模板的id
	 * @param chargeRecordId 充电id
	 * @param model
	 * @return {@link Object}
	 * @throws Exception
	 */
	@RequestMapping("chargeContinueAccess")
	@ResponseBody
	public Object chargeContinueAccess(Integer chargeparam, Integer chargeRecordId, String openid, Model model) throws Exception {
		User user = (User) request.getSession().getAttribute("user");
		// 用户登陆,openid不能为空
		if (user != null && openid != null && !"".equals(openid)) {
			ChargeRecord chargeRecord2 = chargeRecordService.chargeRecordOne(chargeRecordId);
			String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + chargeRecord2.getEquipmentnum() + chargeRecord2.getPort();
			Double money2 = 0.0;
			if (chargeparam == null || chargeparam == 0) {
				money2 = chargeRecord2.getExpenditure();
			} else {
				TemplateSon templateSon = templateService.getInfoTemplateOne(chargeparam);
				money2 = templateSon.getMoney();
			}
			// 判断设备号不能为空
			String code = chargeRecord2.getEquipmentnum();
			if(code == null || "".equals(code) || code.length() != 6){
				return "erroruser";
			}
			// 根据设备号判断商家是否为微信特约商户
			boolean data = userEquipmentService.subMerByCode(code);
			String subAppid = null;
			SortedMap<String, String> params = null;
			if(data){
				logger.info("使用续充接口:chargeContinueAccess对特约商户进行支付");
				// 获取用户在服务商下的openid
				//String wxCode = CommUtil.toString(request.getParameter("code"));
				//if(wxCode == null){
				//	logger.info("1-重定向"+chargeparam+"==="+chargeRecordId);
				//	String url = "/insideChargeContinueAccess?tId="+chargeparam+"&cId="+chargeRecordId+"&dId="+code;
				//	return "redirect:"+url;
				//}
				//if(wxCode != null){
				//logger.info("2-重定回来");
				//JSONObject doGetStr = WeixinUtil.WeChatTokenSubMer(wxCode, code);
				// 判读返回的数据是否有openid
				//if(!doGetStr.has("openid")){
				//	model.addAttribute("openiderror", "1您的账户暂时无法访问");
				//	return "openiderror";
				//}
		    	//String openid = doGetStr.getString("openid");
				// 用户openid换取调取微信支付的参数
				request.setAttribute("subMerCode", code);
				// 获取商户的微信配置信息
				Map<String, Object> subMerDate = userService.selectConfigDataByCode(code);
				subAppid = CommUtil.toString(subMerDate.get("subappid"));
				params = packagePayParamsToMapSub(request, money2, openid, "wxpay/payback", format, "续充");
			}else{
				logger.info("使用续充接口:chargeContinueAccess对普通商户进行支付");
				subAppid = WeiXinConfigParam.APPID;
				params = WxPayController.packagePayParamsToMap(request, money2, user.getOpenid(), "wxpay/payback", format, "续充");
			}
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
			
			ChargeRecord chargeRecord = new ChargeRecord();
			chargeRecord.setEquipmentnum(chargeRecord2.getEquipmentnum());// 设备号/机位号
			chargeRecord.setOrdernum(format);// 订单号
			chargeRecord.setPort(chargeRecord2.getPort());// 端口号
			if (chargeparam == null || chargeparam == 0) {
				chargeRecord.setExpenditure(chargeRecord2.getExpenditure());// 金额
				chargeRecord.setDurationtime(chargeRecord2.getDurationtime());// 时长
				chargeRecord.setQuantity(chargeRecord2.getQuantity());// 电量
			} else {
				TemplateSon templateSon = templateService.getInfoTemplateOne(chargeparam);
				chargeRecord.setExpenditure(templateSon.getMoney());//金额
				Equipment equipment = equipmentService.getEquipmentById(code);
				String durationtime = templateSon.getChargeTime().toString();
				if (DisposeUtil.checkIfHasV3(equipment.getHardversion()) && templateSon.getType() == 3) {
					short clacV3TimeBytemp = templateService.clacV3TimeBytemp(templateSon.getTempparid(), templateSon.getMoney());
					durationtime = clacV3TimeBytemp + "";
				}
				chargeRecord.setDurationtime(durationtime);// 时长
				chargeRecord.setDurationtime(templateSon.getChargeTime().toString());//时长
				chargeRecord.setQuantity(templateSon.getChargeQuantity() );//电量
			}
			chargeRecord.setPaytype(chargeRecord.WEIXINPAYTYPE);// 支付类型
			chargeRecord.setMerchantid(chargeRecord2.getMerchantid());
			chargeRecord.setUid(chargeRecord2.getUid());
			if (chargeRecord2.getIfcontinue() != null) {
				chargeRecord.setIfcontinue(chargeRecord2.getIfcontinue());
			} else {
				chargeRecord.setIfcontinue(chargeRecordId);
			}
			chargeRecord.setStatus(0);
			chargeRecordService.insetRecord(chargeRecord);
			
			Map<String, Object> paramMap1 = new HashMap<>();
			
			paramMap1.put("appId", subAppid);
			paramMap1.put("prepay_id", map.get("prepay_id"));
			paramMap1.put("date", time);
			paramMap1.put("paySign", sign2);
			paramMap1.put("packagess", "prepay_id=" + (String) map.get("prepay_id"));
			paramMap1.put("nonceStr", sjzf);
			paramMap1.put("signType", "MD5");
			paramMap1.put("out_trade_no", params.get("out_trade_no"));
			return paramMap1;
		} else {
			return "erroruser";
		}
	}
	
	/**
	 * 常用电站
	 * @param model
	 * @return
	 */
	@RequestMapping("/numCharge")
	public String numCharge(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		user = userService.getUserByOpenid(user.getOpenid());
		request.getSession().setAttribute("user", user);
		logger.info("点击下一步进行充电==用户信息"+user);
		if (user != null) {
			model.addAttribute("user", user);
			model.addAttribute("codelist", DisposeUtil.quChong(chargeRecordService.findUserLatelyfourRecord(user.getId())));
			return "record/numCharge";
		} else {
			return "erroruser";
		}
	}
	
	@RequestMapping("/codeCharge")
	public String codeCharge(String code, Model model) {
		User tourist = (User) request.getSession().getAttribute("user");
		Equipment equipment = equipmentService.getEquipmentById(code);//根据code查询出相应的设备信息
		//设备信息
		String devicename = CommUtil.toString(equipment.getRemark());
		Integer devicestate = CommUtil.toInteger(equipment.getState());
		Integer devicebindtype = CommUtil.toInteger(equipment.getBindtype());
		String hardversion = CommUtil.toString(equipment.getHardversion());
		Integer tempid = CommUtil.toInteger(equipment.getTempid());
		Integer deviceareaid = CommUtil.toInteger(equipment.getAid());
		//设备到期时间
		Date expirationTime=equipment.getExpirationTime();
		Area area = areaService.selectByIdArea(equipment.getAid());
		model.addAttribute("equipment", equipment);
		model.addAttribute("user", tourist);
		model.addAttribute("area", area);
		model.addAttribute("openid", tourist.getOpenid());
		model.addAttribute("code", code);
		model.addAttribute("hardversion", hardversion);
		model.addAttribute("nowtime", System.currentTimeMillis() + "");

		//绑定商户
		UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
		userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
		Integer dealid = CommUtil.toInteger(userEquipment.getUserId());
		//绑定商户信息
		User dealeruser = new User();
		if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
		String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
		String servephone = CommUtil.toString(dealeruser.getServephone());
		//判断获取模板信息
		Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, dealid, code, hardversion);
		String brandname = CommUtil.toString(DirectTemp.get("remark"));
		String temphone = CommUtil.toString(DirectTemp.get("common1"));
		servephone = basicsService.getServephoneData(temphone, null, servephone, dealerphne);
		model.addAttribute("brandname", brandname);
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
		
		if(devicestate.equals(0)){//判断设备和设备状态是否正常
			return "equipment/equipmentoffline";
		} else if (devicebindtype.equals(0) || dealid.equals(0)) {//判断设备是否被绑定
			model.addAttribute("existuser", request.getParameter("existuser"));
			return "equipment/equipmentunbind";
		} else {
			SendMsgUtil.send_15(code);
//			User user = userService.selectUserById(userEquipment.getUserId());
//			TemplateParent temp =  templateService.getParentTemplateOne(equipment1.getTempid()); 
			List<String> forbidport =  new ArrayList<String>();
			if("01".equals(hardversion)){
//				temp = getTempInfo(temp, tourist.getId(), dealid);
				forbidport = getAssignPotr(tourist.getId(), code, dealid);
			}
			model.addAttribute("forbidport", JSON.toJSONString(forbidport));
			//设备到期时间不为空且当前时间大于到期时间
			if(expirationTime != null && new Date().after(expirationTime)){
				model.addAttribute("errorinfo", "对不起，当前设备已到期");
				model.addAttribute("time", expirationTime);
				model.addAttribute("code", code);
				//提示用户设备到期更换设备
				return "chargeporterror";
			}
			return "record/chargeallport";
		}
	}
	
//	/**
//	 * 输出位扫码
//	 * @param equcode 设备号 
//	 * @param model 
//	 * @return String 
//	 * @throws Exception
//	 */
//	
//	@RequestMapping(value = "/allChargePort")
//	public String allChargePort(@RequestParam("code") String code, Model model) throws Exception {
//		User user = (User) request.getSession().getAttribute("user");
//		if (user != null) {
//			try {
//				// 根据code查询出相应的设备信息
//				Equipment equipment = equipmentService.getEquipmentAndAreaById(code);
//				equipment = equipment == null ? new Equipment() : equipment;
//				String hardversion = CommUtil.toString(equipment.getHardversion());
//				//获取设备版本信息
//				if ("03".equals(hardversion) || "04".equals(hardversion)) {
//					model.addAttribute("errorinfo", "此二维码请退出公众号使用微信扫一扫进行扫码");
//					return "chargeporterror";
//				}
//				Integer deviceareaid = CommUtil.toInteger(equipment.getAid());
//				//根据设备信息查询区域信息
//				Area area = areaService.selectByIdArea(deviceareaid);
//				area = area == null ? new Area() : area;
//				
//				//设备信息
//				String devicename = CommUtil.toString(equipment.getRemark());
//				Integer devicestate = CommUtil.toInteger(equipment.getState());
//				Integer devicebindtype = CommUtil.toInteger(equipment.getBindtype());
//				
//				String softversionnum = CommUtil.toString(equipment.getSoftversionnum());
//				Integer tempid = CommUtil.toInteger(equipment.getTempid());
//				Integer several = CommUtil.toInteger(equipment.getSeveral());
//				// 获取设备版本信息
//				String equi = CommUtil.toString(equipment.getHardversion());
//
//				// 根据设备信息查询区域信息
//				Integer areaid = CommUtil.toInteger(area.getId());
//				String areaname = CommUtil.toString(area.getName());
//
//				UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
//				userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
//				Integer dealid = CommUtil.toInteger(userEquipment.getUserId());
//				User dealeruser = new User();
//				if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
//				String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
//				String servephone = CommUtil.toString(dealeruser.getServephone());
//				String dealservephone = servephone == null ? dealerphne : servephone;
//				
//				model.addAttribute("userinfo", user);//用户信息
//				model.addAttribute("code", code);
//				model.addAttribute("equipment", equipment);
//				model.addAttribute("area", area);
//				model.addAttribute("existuser", request.getParameter("existuser"));
//				
//				//设备到期时间
//				Date expirationTime=equipment.getExpirationTime();
//				//设备到期时间不为空且当前时间大于到期时间
//				if(expirationTime != null && new Date().after(expirationTime)){
//					model.addAttribute("errorinfo", "对不起，当前设备已到期");
//					model.addAttribute("time", expirationTime);
//					//提示用户设备到期更换设备
//					return "chargeporterror";
//				}
//
//				model.addAttribute("several", several);
//				model.addAttribute("hardversion", hardversion);
//				String openid = user.getOpenid();
//				model.addAttribute("openid", openid);
//				String tempphone = null;
//				TemplateParent templ = new TemplateParent();
//				if(!tempid.equals(0)){
//					templ = templateService.getParentTemplateOne(tempid);
//					if(templ!=null) tempphone = CommUtil.toString(templ.getCommon1());
//					tempphone = tempphone == null ? dealservephone : tempphone;
//				}
//				model.addAttribute("phonenum", tempphone);
//				if (dealid.equals(0)) return "equipment/equipmentunbind";
//				if (devicestate.equals(0)) return "equipment/equipmentoffline";
//				model.addAttribute("balance", user.getBalance());
//				
//				model.addAttribute("sendmoney", CommUtil.toDouble(user.getSendmoney()));
//					
//				//判断设备和设备状态是否正常
//				if {
//					SendMsgUtil.send_15(code);
//							List<String> forbidport =  new ArrayList<String>();
//							if("01".equals(equipment.getHardversion())){
//								temp = getTempInfo(temp, user.getId(), userEquipment.getUserId());
//								forbidport = getAssignPotr(user.getId(), equipment.getCode(), userEquipment.getUserId());
//							}
//							model.addAttribute("forbidport", JSON.toJSONString(forbidport));
//							
//							
//							if (temp != null) {
//								
//								String chargeInfo = temp.getChargeInfo();
//								model.addAttribute("chargeInfo", chargeInfo);
//								if (temp.getRemark() != null && !"".equals(temp.getRemark())) {
//									model.addAttribute("brandname", temp.getRemark());
//								} else {
//									model.addAttribute("brandname", null);
//								}
//								String common1 = temp.getCommon1();
//								if (common1 != null && !"".equals(common1.trim())) {
//									model.addAttribute("phonenum", common1);
//								} else {
//									model.addAttribute("phonenum", meruser.getPhoneNum());
//								}
//								model.addAttribute("ifwallet", temp.getWalletpay());
//							} else {
//								model.addAttribute("chargeInfo", null);
//								model.addAttribute("phonenum", meruser.getPhoneNum());
//							}
//							if (!"02".equals(equipment.getHardversion())) {
//								String checkMonthBackVal = checkMonthBackVal(meruser.getId());
//								if (!"2".equals(checkMonthBackVal)) {
//									PackageMonth packageMonth = userService.selectPackageMonthByUid(user.getId());
//									model.addAttribute("packageMonth", packageMonth);
//								} else {
//									model.addAttribute("packageMonth", null);
//								}
//								model.addAttribute("ifmonth", checkMonthBackVal);
//							} else {
//								model.addAttribute("ifmonth", "0");
//							}
//							List<Map<String, String>> portStatus = new ArrayList<>();
//							Map<String, String> codeRedisMap = JedisUtils.hgetAll(code);
//							if (codeRedisMap != null) {
//								if ("02".equals(equipment.getHardversion())) {
//									EquipmentController.addPortStatus(2, codeRedisMap, portStatus);
//								} else if ("01".equals(equipment.getHardversion())) {
//									EquipmentController.addPortStatus(10, codeRedisMap, portStatus);
//								} else if ("05".equals(equipment.getHardversion())) {
//									EquipmentController.addPortStatus(16, codeRedisMap, portStatus);
//								} else if ("06".equals(equipment.getHardversion())) {
//									EquipmentController.addPortStatus(20, codeRedisMap, portStatus);
//								} else {
//									EquipmentController.addPortStatus(10, codeRedisMap, portStatus);
//								}
//							}
//							if (portStatus.size() > 0) {
//								model.addAttribute("allPortSize", portStatus.size());
//								Map<String, String> map = portStatus.get(0);
//								String updateTimeStr = map.get("updateTime");
//								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//								long time = sdf.parse(updateTimeStr).getTime();
////								long time = portStatus.get(0).getUpdateTime().getTime(); 
//								long currentTime = System.currentTimeMillis();
//								if ((currentTime - time) > 300000) {
//									model.addAttribute("flag", true);
//								} else {
//									model.addAttribute("flag", false);
//								}
//							} else {
//								model.addAttribute("allPortSize", 0);
//								model.addAttribute("flag", true);
//								model.addAttribute("portStatus", null);
//							}
//							model.addAttribute("allPortSize", portStatus.size());
//							model.addAttribute("portStatus", portStatus);
//							model.addAttribute("code", code);
//							model.addAttribute("hardversion", equipment.getHardversion());
//							model.addAttribute("equname", equipment.getRemark());
//							model.addAttribute("areaname", equipment.getName());
//							temp(code, temp, model);
//							model.addAttribute("nowtime", System.currentTimeMillis() + "");
//							model.addAttribute("wolfstatus", "2");
//							return "chargeallport1";
//						}
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				return "openiderror";
//			}
//		} else {
//			return "openiderror";
//		}
//	}
	
	/**
	 * 输出位扫码(公众号内部扫码)
	 * @param equcode 设备号 
	 * @param model 
	 * @return String 
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/allChargePort")
	public String allChargePort(@RequestParam("deviceNum") String deviceNum, Model model) throws Exception {
		User user = (User) request.getSession().getAttribute("user");
		logger.info("用户存在====="+(user != null));
		if (user != null) {
			try {
				// 根据code查询出相应的设备信息
				Equipment equipment = equipmentService.getEquipmentAndAreaById(deviceNum);
				equipment = equipment == null ? new Equipment() : equipment;
				//设备到期时间
				Date expirationTime=equipment.getExpirationTime();
				//获取设备版本信息
				String hardversion = CommUtil.toString(equipment.getHardversion());
				if ("03".equals(hardversion) || "04".equals(hardversion)) {
					model.addAttribute("errorinfo", "此二维码请退出公众号使用微信扫一扫进行扫码");
					return "chargeporterror";
				}
				//设备信息
				String devicename = CommUtil.toString(equipment.getRemark());
				Integer devicestate = CommUtil.toInteger(equipment.getState());
				Integer devicebindtype = CommUtil.toInteger(equipment.getBindtype());
				
				String expiretime = CommUtil.toString(equipment.getExpirationTime());
				String softversionnum = CommUtil.toString(equipment.getSoftversionnum());
				
				Integer tempid = CommUtil.toInteger(equipment.getTempid());
				Integer deviceareaid = CommUtil.toInteger(equipment.getAid());
				Integer several = CommUtil.toInteger(equipment.getSeveral());
				//根据设备号判断是否为特约商户设备
				boolean subMer = userEquipmentService.subMerByCode(deviceNum);
				if(subMer){
					// 微信code信息
					String wxCode = CommUtil.toString(request.getParameter("code"));
					// 根据设备号判断是否为特约商户设备
					// 用户扫描特约商户的设备需要重定向获取WXcode
					logger.info("1-使用服务商设备==="+deviceNum+"微信参数=="+wxCode);
					if(wxCode == null){
						String url ="/insideAllChargePort?deviceNum="+deviceNum;
						return "redirect:"+url;
					}
					if(wxCode != null){
						logger.info("2-重定向后的数据==="+deviceNum+"微信参数=="+wxCode);
						JSONObject doGetStr = WeixinUtil.WeChatTokenSubMer(wxCode, deviceNum);
						if(!doGetStr.has("openid")){
							model.addAttribute("openiderror", "1您的账户暂时无法访问");
							return "openiderror";
						}
						String openid = doGetStr.getString("openid");
						// 根据服务商的openid查询用户
						User subUser = userService.selectSubUserByOpenid(openid);
						logger.info("子用户信息=="+(subUser==null)+"用户id="+user.getId());
						if(subUser == null && user != null){
							User user2 = new User();
							user2.setId(user.getId());
							user2.setOpenid(openid);
							userService.addSubUser(user2);
						}
						// 添加成功,根据重定向后的服务商openid查询用户
						User subUserA = userService.selectSubUserByOpenid(openid);
						if(subUserA == null){
							model.addAttribute("openiderror", "2您的账户暂时无法访问");
							return "openiderror";
						}
						logger.info("服务商返回前端数据==="+subUserA.getId()+"支付参数=="+openid);
						model.addAttribute("userinfo", subUserA);
						model.addAttribute("openid", openid);
					}
				}else{
					model.addAttribute("userinfo", user);
					String openid = user.getOpenid();
					model.addAttribute("openid", openid);
				}
				//根据设备信息查询区域信息
				Area area = areaService.selectByIdArea(deviceareaid);

				model.addAttribute("hardversion", hardversion);
				model.addAttribute("equname", devicename);
				model.addAttribute("areaname", area.getName());
				model.addAttribute("nowtime", System.currentTimeMillis() + "");
				
				model.addAttribute("equipment", equipment);
				model.addAttribute("area", area);
				model.addAttribute("several", several);
				//String openid = "odVHPwtil1qLhywcJ1fQ0NXCftb8";
				model.addAttribute("code", deviceNum);
				
				
				UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(deviceNum);
				userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
				Integer dealid = CommUtil.toInteger(userEquipment.getUserId());
				User dealeruser = new User();
				if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
				String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
				String servephone = CommUtil.toString(dealeruser.getServephone());
				//获取主模板信息
				Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, dealid, deviceNum, hardversion);
				String brandname = CommUtil.toString(DirectTemp.get("remark"));
				String temphone = CommUtil.toString(DirectTemp.get("common1"));
				servephone = basicsService.getServephoneData(temphone, null, servephone, dealerphne);
				Integer nowtempid = CommUtil.toInteger(DirectTemp.get("id"));
				Integer permit = CommUtil.toInteger(DirectTemp.get("permit"));
				String chargeInfo = CommUtil.toString(DirectTemp.get("chargeInfo"));
				Integer walletpay = CommUtil.toInteger(DirectTemp.get("walletpay"));
				List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
				tempson = tempson == null ? new ArrayList<>() : tempson;
				Map<String, Object> defaulte = tempDefaultObje(tempson);
				model.addAttribute("defaultchoose", defaulte.get("defaultchoose"));
				model.addAttribute("defaultindex", defaulte.get("defaultindex"));
				model.addAttribute("phonenum", servephone);
				model.addAttribute("brandname", brandname);
				model.addAttribute("chargeInfo", chargeInfo);
				model.addAttribute("ifwallet", walletpay);
				model.addAttribute("DirectTemp", DirectTemp);
				model.addAttribute("templatelist", tempson);
				//设备到期时间不为空且当前时间大于到期时间
				if(expirationTime != null && new Date().after(expirationTime)){
					model.addAttribute("errorinfo", "对不起，当前设备已到期");
					model.addAttribute("time", expirationTime);
					//提示用户设备到期更换设备
					return "chargeporterror";
				}
				if(devicestate.equals(0)){//判断设备和设备状态是否正常
					return "equipment/equipmentoffline";
				} else if (devicebindtype.equals(0) || dealid.equals(0)) {//判断设备是否被绑定
					model.addAttribute("existuser", request.getParameter("existuser"));
					return "equipment/equipmentunbind";
				} else {
					SendMsgUtil.send_15(deviceNum);
//					List<TemplateSon> templatelist = templateService.getSonTemplateLists(tempid);
					model.addAttribute("balance", user.getBalance());
					model.addAttribute("sendmoney", CommUtil.toDouble(user.getSendmoney()));
					List<String> forbidport =  new ArrayList<String>();
					if("01".equals(hardversion)){
//						tempson(tempid, model);
//						temp = getTempInfo(temp, user.getId(), userEquipment.getUserId());//判断分等级模板
						forbidport = getAssignPotr(user.getId(), equipment.getCode(), userEquipment.getUserId());
					}else if (DisposeUtil.checkIfHasV3(equipment.getHardversion())) {
//						List<TemplateSon> tempson = templateService.getSonTemplateLists(tempid);
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
						tempson(tempid, model);
					}*/
					model.addAttribute("forbidport", JSON.toJSONString(forbidport));
					
					if (!"02".equals(equipment.getHardversion())) {
						String checkMonthBackVal = checkMonthBackVal(dealeruser.getId());
						if (!"2".equals(checkMonthBackVal)) {
							PackageMonth packageMonth = userService.selectPackageMonthByUid(user.getId());
							model.addAttribute("packageMonth", packageMonth);
						} else {
							model.addAttribute("packageMonth", null);
						}
						model.addAttribute("ifmonth", checkMonthBackVal);
					} else {
						model.addAttribute("ifmonth", "0");
					}
					List<Map<String, String>> portStatus = new ArrayList<>();
					Map<String, String> codeRedisMap = JedisUtils.hgetAll(deviceNum);
					if (codeRedisMap != null) {
						if ("02".equals(equipment.getHardversion()) || "09".equals(equipment.getHardversion())) {
							EquipmentController.addPortStatus(2, codeRedisMap, portStatus);
						} else if ("01".equals(equipment.getHardversion())) {
							EquipmentController.addPortStatus(10, codeRedisMap, portStatus);
						} else if ("05".equals(equipment.getHardversion())) {
							EquipmentController.addPortStatus(16, codeRedisMap, portStatus);
						} else if ("06".equals(equipment.getHardversion()) || "10".equals(equipment.getHardversion())) {
							EquipmentController.addPortStatus(20, codeRedisMap, portStatus);
						} else {
							EquipmentController.addPortStatus(10, codeRedisMap, portStatus);
						}
					}
					if (portStatus.size() > 0) {
						model.addAttribute("allPortSize", portStatus.size());
						Map<String, String> map = portStatus.get(0);
						String updateTimeStr = map.get("updateTime");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						long time = sdf.parse(updateTimeStr).getTime();
//								long time = portStatus.get(0).getUpdateTime().getTime(); 
						long currentTime = System.currentTimeMillis();
						if ((currentTime - time) > 300000) {
							model.addAttribute("flag", true);
						} else {
							model.addAttribute("flag", false);
						}
					} else {
						model.addAttribute("allPortSize", 0);
						model.addAttribute("flag", true);
						model.addAttribute("portStatus", null);
					}
					
					model.addAttribute("allPortSize", portStatus.size());
					model.addAttribute("portStatus", portStatus);
					model.addAttribute("wolfstatus", "2");
					if(DisposeUtil.checkIfHasV3(equipment.getHardversion())){
						return "chargeallportV3";
					}else{
						return "chargeallport1";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("捕获异常"+e);
				return "openiderror";
			}
		} else {
			return "openiderror";
		}
	}
	
	/**
	 * 扫描端口进行充电
	 * @param codeAndPort 设备端口号
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/chargePort")
	public String chargePort(@RequestParam(value = "codeAndPort", defaultValue = "000001") String codeAndPort, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			model.addAttribute("errorinfo", "登陆已过期，请关闭页面重新进入公众号打开");
			return "chargeporterror";
		}
		try {
			int length = codeAndPort.length();
			if(length==7 || length ==8){
				String val = codeAndPort.substring(6);
				System.out.println("  **** "+val);
				if(length ==8 && !val.equals("10")){
					model.addAttribute("errorinfo", "A二维码有误");
					return "chargeporterror";
				}
			}else{
				model.addAttribute("errorinfo", "B二维码有误");
				return "chargeporterror";
			}
			String equcode = codeAndPort.substring(0, 6);
			String port = codeAndPort.substring(6);
			Equipment equipment = equipmentService.getEquipmentAndAreaById(equcode);// 根据code查询出相应的设备信息
			//设备信息
			String devicename = CommUtil.toString(equipment.getRemark());
			Integer devicestate = CommUtil.toInteger(equipment.getState());
			Integer devicebindtype = CommUtil.toInteger(equipment.getBindtype());
			
			String expiretime = CommUtil.toString(equipment.getExpirationTime());
			String softversionnum = CommUtil.toString(equipment.getSoftversionnum());
			
			Integer tempid = CommUtil.toInteger(equipment.getTempid());
			Integer deviceareaid = CommUtil.toInteger(equipment.getAid());
			Integer several = CommUtil.toInteger(equipment.getSeveral());
			String hardversion = CommUtil.toString(equipment.getHardversion());
			//设备到期时间
			Date expirationTime=equipment.getExpirationTime();
			Area area = areaService.selectByIdArea(equipment.getAid());
			model.addAttribute("equipment", equipment);
			model.addAttribute("area", area);
			model.addAttribute("hardversion", hardversion);
			model.addAttribute("equname", devicename);
			model.addAttribute("areaname", area.getName());
			boolean subMer = userEquipmentService.subMerByCode(equcode);
			if(subMer){
				// 微信code信息
				String wxCode = CommUtil.toString(request.getParameter("code"));
				// 根据设备号判断是否为特约商户设备
				// 用户扫描特约商户的设备需要重定向获取WXcode
				logger.info("使用服务商设备==="+codeAndPort+"微信参数=="+wxCode);
				if(wxCode == null){
					String url ="/insideChargePort?codeAndPort="+codeAndPort;
					return "redirect:"+url;
				}
				if(wxCode != null){
					JSONObject doGetStr = WeixinUtil.WeChatTokenSubMer(wxCode, equcode);
					if(!doGetStr.has("openid")){
						model.addAttribute("openiderror", "1您的账户暂时无法访问");
						return "openiderror";
					}
					String openid = doGetStr.getString("openid");
					// 根据服务商的openid查询用户
					User subUser = userService.selectSubUserByOpenid(openid);
					logger.info("子用户信息=="+(subUser==null)+"用户id="+user.getId());
					if(subUser == null && user != null){
						User user2 = new User();
						user2.setId(user.getId());
						user2.setOpenid(openid);
						userService.addSubUser(user2);
						// 替换用户的openid
						user.setOpenid(openid);
						model.addAttribute("userinfo", user);
						model.addAttribute("openid", openid);
					}else{
						model.addAttribute("userinfo", subUser);
						model.addAttribute("openid", openid);
					}
//					// 添加成功,根据重定向后的服务商openid查询用户
//					User subUserA = userService.selectSubUserByOpenid(openid);
//					if(subUserA == null){
//						model.addAttribute("openiderror", "3您的账户暂时无法访问");
//						return "openiderror";
//					}
//					logger.info("服务商返回前端数据==="+subUserA.getId()+"支付参数=="+openid);
//					model.addAttribute("userinfo", subUserA);
//					model.addAttribute("openid", openid);
				}
			}else{
				model.addAttribute("userinfo", user);
				String openid = user.getOpenid();
				model.addAttribute("openid", openid);
			}
			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(equcode);
			userEquipment = userEquipment == null ? new UserEquipment() : userEquipment;
			Integer dealid = CommUtil.toInteger(userEquipment.getUserId());
			User dealeruser = new User();
			if(!dealid.equals(0)) dealeruser = userService.selectUserById(dealid);
			String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
			String servephone = CommUtil.toString(dealeruser.getServephone());
			//获取主模板信息
			Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, dealid, equcode, hardversion);
			String brandname = CommUtil.toString(DirectTemp.get("remark"));
			String temphone = CommUtil.toString(DirectTemp.get("common1"));
			servephone = basicsService.getServephoneData(temphone, null, servephone, dealerphne);
			model.addAttribute("phonenum", servephone);
			model.addAttribute("nowtime", System.currentTimeMillis() + "");
			
//				Integer tempid = equipment.getTempid();
//				if (tempid != null) {
//					TemplateParent templateParent = templateService.getParentTemplateOne(tempid);
//					if (templateParent != null) {
//						if (templateParent.getCommon1() != null && 
//								!"".equals(templateParent.getCommon1().trim())) {
//							model.addAttribute("phonenum", templateParent.getCommon1());
//						} else {
//							UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(equcode);
//							if (userEquipment != null) {
//								User meruser = userService.selectUserById(userEquipment.getUserId());
//								model.addAttribute("phonenum", meruser.getPhoneNum());
//							}
//						}
//					}
//				}
			if (!"01".equals(equipment.getHardversion())) {
				model.addAttribute("errorinfo", "当前充电站不支持扫描端口充电");
				return "chargeporterror";
			}
			//设备到期时间不为空且当前时间大于到期时间
			if(expirationTime != null && new Date().after(expirationTime)){
				model.addAttribute("errorinfo", "对不起，当前设备已到期");
				model.addAttribute("code", equcode);
				model.addAttribute("time", expirationTime);
				//提示用户设备到期更换设备
				return "chargeporterror";
			}
			Integer nowtempid = CommUtil.toInteger(DirectTemp.get("id"));
			Integer temporaryc = CommUtil.toInteger(DirectTemp.get("permit"));
			String chargeInfo = CommUtil.toString(DirectTemp.get("chargeInfo"));
			Integer walletpay = CommUtil.toInteger(DirectTemp.get("walletpay"));
			List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
			tempson = tempson == null ? new ArrayList<>() : tempson;
			Map<String, Object> defaulte = tempDefaultObje(tempson);
			model.addAttribute("defaultchoose", defaulte.get("defaultchoose"));
			model.addAttribute("defaultindex", defaulte.get("defaultindex"));
			model.addAttribute("brandname", brandname);
			model.addAttribute("chargeInfo", chargeInfo);
			model.addAttribute("ifwallet", walletpay);
			model.addAttribute("DirectTemp", DirectTemp);
			model.addAttribute("templatelist", tempson);
			
			
			model.addAttribute("code", equcode);
			model.addAttribute("port", port);
			
			if(devicestate.equals(0)){//判断设备和设备状态是否正常
				return "equipment/equipmentoffline";
			} else if (devicebindtype.equals(0) || dealid.equals(0)) {//判断设备是否被绑定
				model.addAttribute("existuser", request.getParameter("existuser"));
				return "equipment/equipmentunbind";
			} else {
				SendMsgUtil.send_15(equcode);
				model.addAttribute("balance", user.getBalance());
				model.addAttribute("sendmoney", CommUtil.toDouble(user.getSendmoney()));
				System.out.println("********************   "+ CommUtil.toDouble(user.getSendmoney()));
				
//				TemplateParent temp = templateService.getParentTemplateOne(equipment.getTempid());
				List<String> forbidport =  new ArrayList<String>();
//				temp = getTempInfo(temp, user.getId(), userEquipment.getUserId());
				forbidport = getAssignPotr(user.getId(), equipment.getCode(), userEquipment.getUserId());
				model.addAttribute("forbidport", JSON.toJSONString(forbidport));
				
				String checkMonthBackVal = checkMonthBackVal(dealid);
				if (!"2".equals(checkMonthBackVal)) {
					PackageMonth packageMonth = userService.selectPackageMonthByUid(user.getId());
					model.addAttribute("packageMonth", packageMonth);
				} else {
					model.addAttribute("packageMonth", null);
				}
//							int portval = Integer.parseInt(port);
//							AllPortStatus portStatus = allPortStatusService.findPortStatusByEquipmentnumAndPort(equcode, portval);
//							if (portStatus != null) {
//								long time = portStatus.getUpdateTime().getTime();
//								long currentTime = System.currentTimeMillis();
//								model.addAttribute("portStatus", portStatus.getPortStatus());
//								if ((currentTime - time) > 300000) {
//									model.addAttribute("flag", true);
//								} else {
//									model.addAttribute("flag", false);
//								}
//							} else {
//								model.addAttribute("portStatus", 0);
//								model.addAttribute("flag", true);
//							}
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
				model.addAttribute("wolfstatus", "2");
				return "chargeport1";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorinfo", "扫码失败，请重新扫码");
			return "chargeporterror";
		}
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
				System.out.println("输出list000：        "+list);
				boolean res = slelecToPortINfo.contains(port);
				if(res){
					Iterator<String> it = forbidport.iterator();
					while(it.hasNext()){
			            String str = CommUtil.toString(it.next());
			            if(port.equals(str)){
			            	it.remove();
			            }        
			        }
					System.out.println("输出forbidport00：        "+forbidport);
				}
			}
		}
		return forbidport;
	}
	
	public TemplateParent getTempInfo(TemplateParent temp, Integer uid, Integer merid) {
		TemplateParent tempinfo = null;
		if(temp.getGrade()==1){//分等级
			Map<String, Object> urole = userService.selectUserRoleInfo(uid, merid);
			if(urole==null || urole.size()==0){
				tempinfo = temp;
			}else{
				Integer proleid = CommUtil.toInteger(urole.get("proleid"));
				if(proleid==6){
					tempinfo = templateService.templateGradeClassify( merid, 0, temp.getId(), 2);
				}else if(proleid==7){
					tempinfo = templateService.templateGradeClassify( merid, 0, temp.getId(), 3);
				}
			}
		}else{
			tempinfo = temp;
		}
		return tempinfo;
	}
	
	/**
	 * 选择支付方式
	 * @param code 设备号
	 * @param openid 用户的openid
	 * @param portchoose 
	 * @param chargeparam
	 * @param model
	 * @return
	 */
	@RequestMapping("/choosepay")
	public String choosepay(String code,String openid, String portchoose,Integer chargeparam,Model model) {
		User user = (User) request.getSession().getAttribute("user");
		user = userService.selectUserById(user.getId());
		request.getSession().setAttribute("user", user);
		if (user == null) {
			return "erroruser";
		} else {
			model.addAttribute("code", code);
			model.addAttribute("openid", openid);
			model.addAttribute("portchoose", portchoose);
			model.addAttribute("chargeparam", chargeparam);
			model.addAttribute("balance", user.getBalance() == null ? 0.0 : user.getBalance());
			System.out.println("用户余额===" + user.getBalance());
			TemplateSon templateSon = templateService.getInfoTemplateOne(chargeparam);
			Double money2 = templateSon.getMoney();
			TemplateParent templateParent = templateService.getParentTemplateOne(templateSon.getTempparid());
			model.addAttribute("ifwallet", templateParent.getWalletpay());
			model.addAttribute("money", money2);
			return "record/choosepay";
		}
	}
	
	/**
	 * 公众号内钱包续充
	 * @param chargeparam 充电参数
	 * @param chargeRecordId 充电记录id
	 * @param model
	 * @return
	 */
	@RequestMapping("/walletContinuePay")
	@ResponseBody
	public String walletContinuePay(Integer chargeparam, Integer chargeRecordId, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		ChargeRecord chargeRecord2 = chargeRecordService.getChargeRecordById(chargeRecordId);
		Double money2 = 0.0;
		String code = chargeRecord2.getEquipmentnum();
		Integer port2 = chargeRecord2.getPort();
		int tmpTime = 0;
		int tmpElec = 0;
		if (chargeparam == null || chargeparam == 0) {
			money2 = chargeRecord2.getExpenditure();
			tmpTime = Integer.parseInt(chargeRecord2.getDurationtime());
			tmpElec = chargeRecord2.getQuantity();
		} else {
			TemplateSon templateSon = templateService.getInfoTemplateOne(chargeparam);
			money2 = templateSon.getMoney();
			tmpTime = templateSon.getChargeTime();
			tmpElec = templateSon.getChargeQuantity();
		}
		if (money2 > user.getBalance() || user.getBalance() <= 0) {
			return "0";
		}
		Integer merid = user.getMerid() == null ? 0 : user.getMerid();
		if (!merid.equals(chargeRecord2.getMerchantid())) {
			return "2";
		}
		if (money2 > 0) {
			String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + chargeRecord2.getEquipmentnum() + chargeRecord2.getPort();
			ChargeRecord chargeRecord = new ChargeRecord();
			chargeRecord.setEquipmentnum(chargeRecord2.getEquipmentnum());// 设备号/机位号
			chargeRecord.setOrdernum(format);// 订单号
			chargeRecord.setPort(chargeRecord2.getPort());// 端口号
			Equipment equipment = equipmentService.getEquipmentById(chargeRecord.getEquipmentnum());
			if (chargeparam == null || chargeparam == 0) {
				chargeRecord.setExpenditure(chargeRecord2.getExpenditure());// 金额
				chargeRecord.setDurationtime(chargeRecord2.getDurationtime());// 时长
				chargeRecord.setQuantity(chargeRecord2.getQuantity());// 电量
			} else {
				TemplateSon templateSon = templateService.getInfoTemplateOne(chargeparam);
				chargeRecord.setExpenditure(templateSon.getMoney());//金额
				String durationtime = templateSon.getChargeTime().toString();
				if (DisposeUtil.checkIfHasV3(equipment.getHardversion()) && templateSon.getType() == 3) {
					short clacV3TimeBytemp = templateService.clacV3TimeBytemp(templateSon.getTempparid(), templateSon.getMoney());
					durationtime = clacV3TimeBytemp + "";
				}
				chargeRecord.setDurationtime(durationtime);//时长
				chargeRecord.setQuantity(templateSon.getChargeQuantity() );//电量
			}
			chargeRecord.setPaytype(chargeRecord.WALLETPAYTYPE);// 支付类型
			chargeRecord.setMerchantid(chargeRecord2.getMerchantid());
			chargeRecord.setUid(chargeRecord2.getUid());
			if (chargeRecord2.getIfcontinue() != null) {
				chargeRecord.setIfcontinue(chargeRecord2.getIfcontinue());
			} else {
				chargeRecord.setIfcontinue(chargeRecordId);
			}
			chargeRecord.setStatus(1);
			chargeRecord.setBegintime(new Date());
			//1、添加充电记录
			chargeRecordService.insetRecord(chargeRecord);
			String money1 = String.valueOf(chargeRecord.getExpenditure() * 100);
			int idx = money1.lastIndexOf(".");
			Short money = Short.valueOf(money1.substring(0, idx));
			Byte port = Byte.valueOf(port2.toString());
			Short time = Short.valueOf(chargeRecord.getDurationtime());
			Short elec = Short.valueOf(chargeRecord.getQuantity() + "");
			try {
				// 2、修改用户钱包金额
				if ("07".equals(equipment.getHardversion()) || DisposeUtil.checkIfHasV3(equipment.getHardversion())) {
					SendMsgUtil.send_0x27((byte)port, (short)(money / 10), (short)time, (short)elec, chargeRecord.getEquipmentnum(), (byte)1);
					if (DisposeUtil.checkIfHasV3(equipment.getHardversion())) {
						SendMsgUtil.resetChargeData(code, port, chargeRecord2.getUid(), money2, 1);
					}
				} else {
					SendMsgUtil.send_0x14(port, (short) (money / 10), time, elec, chargeRecord.getEquipmentnum());// 支付完成充电开始
				}
				String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=1&ordernum="+chargeRecord.getOrdernum();
				String phone = userService.selectUserById(merid).getPhoneNum();
				TempMsgUtil.paychargesendmsg("你好，续充付款成功。", chargeRecord.getUid(), phone,  urltem, chargeRecord.getExpenditure(),CommonConfig.PAYSUCCEEDMES);
				//-------------------------------------------------------------
				Double userbalance = CommUtil.toDouble(user.getBalance());
				Double usersendmoney = CommUtil.toDouble(user.getSendmoney());
				Double usermoney = CommUtil.addBig(userbalance, usersendmoney);
				Double coumoney = CommUtil.toDouble(chargeRecord.getExpenditure());
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
				
				user.setBalance(topupbalance);
				user.setSendmoney(givebalance);
				userService.updateUserById(user);
				// 3、添加用户钱包明细
				Date date = new Date();
				generalDetailService.insertGenWalletDetail(user.getId(), chargeRecord.getMerchantid(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, format, date, 2);
				tradeRecordService.insertTrade(chargeRecord.getMerchantid(), chargeRecord.getUid(), format, coumoney, 0.00, chargeRecord.getEquipmentnum(), 1, 1, 1 , equipmentService.getEquipmentById(chargeRecord.getEquipmentnum()).getHardversion());
				//----------------------------------------------
			} catch (Exception e) {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss----");
				System.out.println(sf.format(new Date()) + "设备编号：--" + chargeRecord.getEquipmentnum() + "--设备端口：--"
						+ port + "--充电异常");
			}
		} else if (money2 == 0) {
			String durationtime = chargeRecord2.getDurationtime();
			int clacTime = Integer.parseInt(durationtime) + tmpTime;
			chargeRecordService.updateNumberById(null, null, chargeRecordId, clacTime + "", null, null, null, null);
			Equipment equipment = equipmentService.getEquipmentById(code);
			String money1 = String.valueOf(money2 * 100);
			int idx = money1.lastIndexOf(".");
			Short money = Short.valueOf(money1.substring(0, idx));
			Byte port = Byte.valueOf(port2.toString());
			Short time = Short.valueOf(tmpTime + "");
			Short elec = Short.valueOf(tmpElec + "");
			if ("07".equals(equipment.getHardversion()) || DisposeUtil.checkIfHasV3(equipment.getHardversion())) {
				SendMsgUtil.send_0x27(port, (short)(money / 10), (short)time, (short)elec, code, (byte)1);
			} else {
				SendMsgUtil.send_0x14(port, (short) (money / 10), time, elec, code);// 支付完成充电开始
			}
		}
//		try {
//			AllPortStatus portStatus = allPortStatusService.findPortStatusByEquipmentnumAndPort(code, port2);
//			if (portStatus != null) {
//				AllPortStatus allPortStatus = new AllPortStatus();
//				if (portStatus.getTime() > 0) {
//					allPortStatus.setTime(Short.parseShort(tmpTime + portStatus.getTime() + ""));
//				} else {
//					allPortStatus.setTime((short) tmpTime);
//				}
//				allPortStatus.setEquipmentnum(code);
//				allPortStatus.setPort(port2);
//				allPortStatus.setPortStatus((byte) 2);
//				allPortStatus.setElec((short) (tmpElec + portStatus.getElec()));
//				allPortStatus.setPower((short) 0);
//				allPortStatusService.updateAllPortStatus(allPortStatus);
//				equipmentService.insertRealRecord(chargeRecordId, chargeRecord2.getUid(), chargeRecord2.getMerchantid(),
//						code, port2, 1, allPortStatus.getTime() + 0, allPortStatus.getElec() + 0, 0, -1.0);
//			} 
//		} catch (Exception e) {
//			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss----");
//			System.out.println(sf.format(new Date()) + "端口实时状态数据修改失败");
//		}
		return "1";
	}
	
	/**
	 * @Description： 钱包付款充电
	 * @author：
	 */
	@RequestMapping("/walletCharge")
	@ResponseBody
	public String walletCharge(String code,String openid, Integer portchoose,Integer chargeparam,Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			TemplateSon templateSon = templateService.getInfoTemplateOne(chargeparam);
			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
			int userId = userEquipment == null ? -1 : userEquipment.getUserId().intValue();
			int merid = user.getMerid() == null ? 0 : user.getMerid().intValue();
			if (userId != merid) {
				return "2";
			}
			String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + code + portchoose;
			ChargeRecord chargeRecord = new ChargeRecord();
			chargeRecord.setEquipmentnum(code);// 设备号/机位号
			chargeRecord.setOrdernum(format);// 订单号
			chargeRecord.setPort(portchoose);// 端口号
			chargeRecord.setExpenditure(templateSon.getMoney());//金额
			chargeRecord.setDurationtime(templateSon.getChargeTime().toString());//时长
			chargeRecord.setQuantity(templateSon.getChargeQuantity() );//电量
			chargeRecord.setPaytype(chargeRecord.WALLETPAYTYPE);// 支付类型
			chargeRecord.setMerchantid(userEquipment.getUserId());
			chargeRecord.setUid(user.getId());
			chargeRecord.setStatus(0);
			// 1、添加充电记录
			chargeRecordService.insetRecord(chargeRecord);
			String money1 = String.valueOf(chargeRecord.getExpenditure() * 100);
			int idx = money1.lastIndexOf(".");
			Short money = Short.valueOf(money1.substring(0, idx));
			Byte port = Byte.valueOf(chargeRecord.getPort().toString());
			Short time = Short.valueOf(chargeRecord.getDurationtime());
			Short elec = Short.valueOf(chargeRecord.getQuantity() + "");
			if (chargeRecord.getStatus() == null || chargeRecord.getStatus() == 0) {
				List<ChargeRecord> chargelist = chargeRecordService.getOrderByOrdernum(format);
				equipmentService.insertRealRecord(chargelist.get(0).getId(), chargeRecord.getUid(), chargeRecord.getMerchantid(),
						code, port + 0, 1, time + 0, elec + 0, 0, -1.0);
				try {
					// 2、修改用户钱包金额
					SendMsgUtil.send_0x14(port, (short) (money / 10), time, elec, code);//支付完成充电开始
//					WolfHttpRequest.sendChargePaydata(port, time, money/10 + "", chargeRecord.getQuantity() + "", code);
					String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=1&ordernum="+chargeRecord.getOrdernum();
					String phone = userService.selectUserById(merid).getPhoneNum();
					TempMsgUtil.paychargesendmsg("你好，钱包付款成功，开始充电。", chargeRecord.getUid(), phone,  urltem, chargeRecord.getExpenditure(),CommonConfig.PAYSUCCEEDMES);
					//-------------------------------------------------------------
					Double userbalance = CommUtil.toDouble(user.getBalance());
					Double usersendmoney = CommUtil.toDouble(user.getSendmoney());
					Double usermoney = CommUtil.addBig(userbalance, usersendmoney);
					Double coumoney = CommUtil.toDouble(chargeRecord.getExpenditure());
					Double opermoney = 0.00;
					Double opersendmoney = 0.00;
					Double topupbalance = 0.00;
					Double givebalance = 0.00;
					Double recharge = CommUtil.subBig(userbalance, coumoney);
					if(recharge>=0){
						opermoney = coumoney;
						opersendmoney = 0.00;
						topupbalance = CommUtil.subBig(userbalance, opermoney);
//						topupbalance = CommUtil.subBig(money, opermoney);
						givebalance = usersendmoney;
					}else{
						opermoney = userbalance;
						opersendmoney = CommUtil.toDouble(Math.abs(recharge));
						topupbalance = 0.00;
						givebalance = CommUtil.subBig(usersendmoney, opersendmoney);
					}
					Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
					Double operbalance = CommUtil.addBig(topupbalance, givebalance);
					
					user.setBalance(topupbalance);
					user.setSendmoney(givebalance);
					userService.updateUserById(user);
					Map<String, Object> maps = new HashMap<String, Object>();
					maps.put("status", 1);
					maps.put("begintime", StringUtil.toDateTime());
					maps.put("ordernum", format);
					chargeRecordService.updateByOrdernum(maps);
					// 3、添加用户钱包明细
					Date date = new Date();
					generalDetailService.insertGenWalletDetail(user.getId(), chargeRecord.getMerchantid(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, format, date, 2);
					tradeRecordService.insertTrade(chargeRecord.getMerchantid(), chargeRecord.getUid(), format, coumoney, 0.00, chargeRecord.getEquipmentnum(), 1, 1, 1 , equipmentService.getEquipmentById(chargeRecord.getEquipmentnum()).getHardversion());
					//----------------------------------------------
//					Double balance = user.getBalance() == null ? 0.0 :user.getBalance();
//					user.setBalance((balance * 100 - chargeRecord.getExpenditure() * 100) / 100);
//					userService.updateUserById(user);
//					Map<String, Object> maps = new HashMap<String, Object>();
//					maps.put("status", 1);
//					maps.put("begintime", StringUtil.toDateTime());
//					maps.put("ordernum", format);
//					chargeRecordService.updateByOrdernum(maps);
//					// 3、添加用户钱包明细
//					Date date = new Date();
//					generalDetailService.insertGenWalletDetail(user.getId(), chargeRecord.getMerchantid(), chargeRecord.getExpenditure(), 0.00, user.getBalance(), format, date, 2);
//					tradeRecordService.insertTrade(chargeRecord.getMerchantid(), user.getId(), format, chargeRecord.getExpenditure(), 0.00, chargeRecord.getEquipmentnum(), 1, 1, 1, equipmentService.getEquipmentById(chargeRecord.getEquipmentnum()).getHardversion());
				} catch (Exception e) {
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss----");
					System.out.println(sf.format(new Date()) + "设备编号：--" + code + "--设备端口：--"
							+ port + "--充电异常");
				}
			}
			
//			try {
//				AllPortStatus portStatus = allPortStatusService.findPortStatusByEquipmentnumAndPort(code, port + 0);
//				if (portStatus != null) {
//					AllPortStatus allPortStatus = new AllPortStatus();
//					if (portStatus.getTime() > 0) {
//						allPortStatus.setTime((short) (time + portStatus.getTime()));
//					} else {
//						allPortStatus.setTime(time);
//					}
//					allPortStatus.setEquipmentnum(code);
//					allPortStatus.setPort(port + 0);
//					allPortStatus.setPortStatus((byte) 2);
//					allPortStatus.setElec(elec);
//					allPortStatus.setPower((short) 0);
//					allPortStatusService.updateAllPortStatus(allPortStatus);
//				} 
//			} catch (Exception e) {
//				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss----");
//				System.out.println(sf.format(new Date()) + "端口实时状态数据修改失败");
//			}
			return "1";
		} else {
			return "0";
		}
	}
	/**
	 * 扫描设备号进行充电
	 * @param code 设备号
	 * @param openid 用户参数
	 * @param portchoose 端口的选择
	 * @param chargeparam 充电的参数
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/codePaywx")
	@ResponseBody
	public Map<String, Object> codePaywx(String code,String openid, Integer portchoose,Integer chargeparam,Model model) throws Exception {
		User user = (User) request.getSession().getAttribute("user");
		user = userService.selectUserById(user.getId());
		Map<String, Object> paramMap1 = new HashMap<>();
		if (user != null) {
			if (portchoose == null) {
				paramMap1.put("wolferror", "1");//端口为空
				return paramMap1;
			}
			if (chargeparam == null) {
				paramMap1.put("wolferror", "2");//收费模板为空
				return paramMap1;
			}
			if(code == null || "".equals(code)){
				paramMap1.put("wolferror", "1");//设备号为空
				return paramMap1;
			}
			TemplateSon templateSon = templateService.getInfoTemplateOne(chargeparam);
			String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + code + portchoose;
			Double money2 = templateSon.getMoney();
			// 判断设备的所属商户是不是微信特约商户
			boolean data = userEquipmentService.subMerByCode(code);
			String subAppid = null; 
			SortedMap<String, String> params = null;
			if(data){
				logger.info("通过接口:codePaywx====对特约商户进行支付");
				// 获取商户的微信配置信息
				request.setAttribute("subMerCode", code);
				Map<String, Object> subMerDate = userService.selectConfigDataByCode(code);
				subAppid = CommUtil.toString(subMerDate.get("subappid"));
				params = packagePayParamsToMapSub(request, money2, openid, "wxpay/payback", format, "充电");
			}else{
				logger.info("通过接口:codePaywx====对服务平台支付");
				subAppid = WeiXinConfigParam.APPID;
				params = WxPayController.packagePayParamsToMap(request, money2, user.getOpenid(), "wxpay/payback", format, "充电");
			}
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
			
			UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
			
			ChargeRecord chargeRecord = new ChargeRecord();
			chargeRecord.setEquipmentnum(code);// 设备号/机位号
			chargeRecord.setOrdernum(format);// 订单号
			chargeRecord.setPort(portchoose);// 端口号
			chargeRecord.setExpenditure(templateSon.getMoney());//金额
			chargeRecord.setDurationtime(templateSon.getChargeTime().toString());//时长
			chargeRecord.setQuantity(templateSon.getChargeQuantity() );//电量
			chargeRecord.setPaytype(chargeRecord.WEIXINPAYTYPE);// 支付类型
			chargeRecord.setMerchantid(userEquipment.getUserId());
			chargeRecord.setUid(user.getId());
			chargeRecord.setStatus(0);
			chargeRecordService.insetRecord(chargeRecord);
			
			
			paramMap1.put("appId", subAppid);
			paramMap1.put("prepay_id", map.get("prepay_id"));
			paramMap1.put("date", time);
			paramMap1.put("paySign", sign2);
			paramMap1.put("packagess", "prepay_id=" + (String) map.get("prepay_id"));
			paramMap1.put("nonceStr", sjzf);
			paramMap1.put("signType", "MD5");
			paramMap1.put("out_trade_no", params.get("out_trade_no"));
			return paramMap1;
		} else {
			return null;
		}
	}
	
	@RequestMapping("/codeExist")
	@ResponseBody
	public String codeExist(String deviceNum) {
		Equipment equipment = equipmentService.getEquipmentById(deviceNum);
		if (equipment == null || equipment.getCode() == null) {
			return "0";
		} else {
			logger.info("用户点击下一步查询设备"+equipment.getCode());
			return "1";
		}
	}
	
	
	
//	/**
//	 * @Description：获取主模板信息
//	 * @author： origin
//	 * @createTime：2020年3月24日下午5:40:10
//	 */
//	public  Map<String, Object> inquireDirectTempData(Integer tempid, Integer dealid, String version) {
//		Map<String, Object> datamap = new HashMap<String, Object>();
//		try {
//			Integer tempstatus = 0;
//			if(version.equals("03")){
//				tempstatus = 2;
//			} else if(version.equals("04")){
//				tempstatus = 1;
//			} else if(version.equals("08")){
//				tempstatus = 6;
//			}
//			TemplateParent tempdata = null;
//			Integer mercid = null;
//			if(!tempid.equals(0)){//模板不为0时（即设备没有选中模板时）
//				tempdata = templateService.inquireDirectTemp(tempid);
//				mercid = CommUtil.toInteger(tempdata.getMerchantid());
//			}
//			if(tempid.equals(0) || !mercid.equals(dealid)){//设备绑定的不是自己的模板 或 未绑定模板
//				List<TemplateParent> tempinfo = templateService.inquireTempByStatus(0, tempstatus);
//				/** 设备硬件版本 00-默认出厂版本、01-十路智慧款、02-电轿款、03-脉冲投币、04-离线充值机、05-16路智慧款、
//				 * 06-20路智慧款、07单路交流桩 、08新版10路智慧款V3*/
//				if(version.equals("02") || version.equals("07")){
//					for(TemplateParent item : tempinfo){
//						String temname = CommUtil.toString(item.getName());
//						if(temname.equals("1元1小时") || temname.equals("电轿系统模板")){
//							tempdata = item;
//							break;
//						}
//					}
//				} else if(version.equals("00") || version.equals("01") || version.equals("05") || version.equals("06")){
//					tempdata = tempinfo.get(0);
//				}
//			}
//			datamap = JSON.parseObject(JSON.toJSONString(tempdata), Map.class);
//			return datamap;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return datamap;
//	}
	
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
//	public void tempson(Integer tempid, Model model) {
//		List<TemplateSon> templatelist = templateService.getSonTemplateLists(tempid);
//		if (templatelist == null || templatelist.size() == 0) {
//			templatelist = templateService.getSonTemp(0);
//		}
//		
//		int temp1 = 0;
//		for (TemplateSon templateSon : templatelist) {
//			if (temp1==0 && !templateSon.getName().contains("1元")) {
//				model.addAttribute("defaultchoose", templateSon.getId());
//				model.addAttribute("defaultindex", temp1);
//				break;
//			}else if(temp1==1){
//				model.addAttribute("defaultchoose", templateSon.getId());
//				model.addAttribute("defaultindex", temp1);
//				break;
//			}
//			temp1++;
//		}
//		model.addAttribute("templatelist", templatelist);
//	}
	
//	public void temp(String equcode, TemplateParent temp, Model model) {
//		List<TemplateSon> templatelist = null;
//		if(temp!=null){
//			templatelist = templateService.getSonTemplateLists(temp.getId());
//		}else{
//			templatelist = templateService.getEquSonTem(equcode);
//		}
//		//List<TemplateSon> templatelist = templateService.getEquSonTem(equcode);
//		if (templatelist == null || templatelist.size() == 0) {
//			templatelist = templateService.getSonTemp(0);
//		}
//		int temp1 = 0;
//		for (TemplateSon templateSon : templatelist) {
//			if (temp1==0 && !templateSon.getName().contains("1元")) {
//				model.addAttribute("defaultchoose", templateSon.getId());
//				model.addAttribute("defaultindex", temp1);
//				break;
//			}else if(temp1==1){
//				model.addAttribute("defaultchoose", templateSon.getId());
//				model.addAttribute("defaultindex", temp1);
//				break;
//			}
//			temp1++;
//		}
//		model.addAttribute("templatelist", templatelist);
//	}
	
	public void walletRefund(Double money,String ordernum,ChargeRecord chargeRecord) {
		Integer uid = chargeRecord.getUid();
		TradeRecord traderecord = tradeRecordService.getTraderecord(ordernum);
		User user = userService.selectUserById(uid);
		//-------------------------------------------------------------
		Double userbalance = CommUtil.toDouble(user.getBalance());
		Double usersendmoney = CommUtil.toDouble(user.getSendmoney());
		Double usermoney = CommUtil.addBig(userbalance, usersendmoney);
		Double opermoney = CommUtil.toDouble(money);
		Double opersendmoney = 0.00;
		Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
		
		Double topupbalance = CommUtil.addBig(userbalance, opermoney);
		Double givebalance = CommUtil.addBig(usersendmoney, opersendmoney);
		Double operbalance = CommUtil.addBig(topupbalance, givebalance);
		
		user.setBalance(topupbalance);
		user.setSendmoney(givebalance);
		userService.updateUserById(user);
		generalDetailService.insertGenWalletDetail(user.getId(), user.getMerid(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, ordernum, new Date(), 5);
		//----------------------------------------------
//		Double balance = (user.getBalance()*100 + money*100)/100;
//		user.setBalance(balance);
//		userService.updateUserById(user);
//		generalDetailService.insertGenWalletDetail(user.getId(), user.getMerid(), money, 0.00, balance, ordernum, new Date(), 5);
		tradeRecordService.insertToTrade(traderecord.getMerid(), traderecord.getManid(), traderecord.getUid(), traderecord.getOrdernum(), opermoney, traderecord.getMermoney(), 
				traderecord.getManmoney(), traderecord.getCode(), traderecord.getPaysource(), traderecord.getPaytype(), 2, traderecord.getHardver());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		chargeRecordService.updateNumberById(null, 1, chargeRecord.getId(), null, null,
				chargeRecord.getExpenditure(), sdf.format(chargeRecord.getBegintime()), CommUtil.toDateTime());
		String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=1&id="+chargeRecord.getId();
		returnMsgTemp(chargeRecord.getUid(), chargeRecord.getEquipmentnum(), chargeRecord.getOrdernum(), urltem, StringUtil.toDateTime(), chargeRecord.getExpenditure());
	}
	
	public void wxRefund(Double money,String ordernum,ChargeRecord chargeRecord) {
		TradeRecord traderecord = tradeRecordService.getTraderecord(ordernum);
		if ((traderecord == null)) {
			logger.warn("手动断电全额退款失败");
			return;
		}
		WXPayConfigImpl config;
		try {
			config = WXPayConfigImpl.getInstance();
			WXPay wxpay = new WXPay(config);
			String total_fee = "";
			String out_trade_no = "";
			String moneyStr = String.valueOf(money * 100);
			int idx = moneyStr.lastIndexOf(".");
			total_fee = moneyStr.substring(0, idx);
			out_trade_no = ordernum;// 退款订单号
			SortedMap<String, String> params = new TreeMap<>();
			params.put("appid", WeiXinConfigParam.FUWUAPPID);
			params.put("mch_id", WeiXinConfigParam.MCHID);
			params.put("sub_mch_id", WeiXinConfigParam.SUBMCHID);
			params.put("out_trade_no", out_trade_no);
			params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
			String sign = HttpRequest.createSign("UTF-8", params);
			params.put("sign", sign);
			String url = "https://api.mch.weixin.qq.com/pay/orderquery";
			String canshu = HttpRequest.getRequestXml(params);
			String sr = HttpRequest.sendPost(url, canshu);
			// 获取商家的ID
			Integer merId = CommUtil.toInteger(traderecord.getMerid());
			String subMchId = null;
			User merUser = null;
			if (merId != 0) {
				// 查询商家信息,判断商户是否为特约商户
				merUser = userService.selectUserById(merId);
				if(merUser != null && merUser.getSubMer() == 1){
					Map<String, Object> configData = userService.selectSubMerConfigById(merId);
					subMchId = CommUtil.toString(configData.get("submchid"));
				}else{
					subMchId = WeiXinConfigParam.SUBMCHID;
				}
			}else{
				subMchId = WeiXinConfigParam.SUBMCHID;
			}
			Map<String, String> resultMap = XMLUtil.doXMLParse(sr);
			if (resultMap.get("return_code").toString().equalsIgnoreCase("SUCCESS")) {
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("appid", WeiXinConfigParam.FUWUAPPID);
				data.put("mch_id", WeiXinConfigParam.MCHID);
				data.put("sub_mch_id", subMchId);
				data.put("transaction_id", resultMap.get("transaction_id"));
				data.put("out_trade_no", out_trade_no);// 定单号
				data.put("out_refund_no", "t" + out_trade_no);
				data.put("total_fee", total_fee);
				data.put("refund_fee", total_fee);
				data.put("refund_fee_type", "CNY");
				data.put("op_user_id", config.getMchID());
				try {
					Map<String, String> r = wxpay.refund(data);
					// 处理退款后的订单 成功
					if ("SUCCESS".equals(r.get("result_code"))) {
						ordernum = CommUtil.toString(out_trade_no);
						//修改本地数据
						String devicenum = CommUtil.toString(chargeRecord.getEquipmentnum());
						Integer uid = CommUtil.toInteger(chargeRecord.getUid());
						Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
						Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
						Integer orderid = CommUtil.toInteger(chargeRecord.getId());
						Date datetime = new Date();
						String strtime = CommUtil.toDateTime(datetime);
						Integer paysource = MerchantDetail.CHARGESOURCE;
						Integer paytype = MerchantDetail.WEIXINPAY;
						Integer paystatus = MerchantDetail.REFUND;
						Double mermoney = 0.00;
						String devicehard = "";
						equipmentService.updateEquEarn(devicenum, paymoney, 0);
						if (chargeRecord.getEndtime() == null) {
							chargeRecordService.updateNumberById(null, 1, orderid, null, null, paymoney, CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", chargeRecord.getBegintime()), CommUtil.toDateTime());
						} else {
							chargeRecordService.updateNumberById(null, 1, orderid, null, null, paymoney, null, CommUtil.toDateTime());
						}
						if (chargeRecord.getNumber() != 1) {
							String comment = traderecord.getComment();
							try {
								Equipment equipment = equipmentService.getEquipmentById(devicenum);
								devicehard = CommUtil.toString(equipment.getHardversion());
								Integer aid = CommUtil.toInteger(equipment.getAid());
								if (aid != null && aid != 0) {
									areaService.updateAreaEarn(0, paymoney, aid,null,paymoney,null);
								} 
								if(merUser != null && merUser.getSubMer() == 1){
									//商户收益总额  1为加  0为减
									userService.updateMerAmount(0, paymoney, merid);
									merchantDetailService.insertMerEarningDetail(merid, paymoney, merUser.getEarnings(), ordernum, datetime, paysource, paytype, paystatus);
								}else{
									//普通商家充电退费数据
									HelloController.dealerIncomeRefund(comment, merid, paymoney, ordernum, datetime, paysource, paytype, paystatus);
								}
							} catch (Exception e) {
								logger.warn("小区修改余额错误===" + e.getMessage());
							}
							mermoney = traderecord.getMermoney();
							Double partmoney = traderecord.getManmoney();
							Integer manid = partmoney == 0 ? 0 : -1;
							tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 1, 2, 2, devicehard, comment);
						}
						String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=1&id="+chargeRecord.getId();
						returnMsgTemp(chargeRecord.getUid(), chargeRecord.getEquipmentnum(), chargeRecord.getOrdernum(), urltem, StringUtil.toDateTime(), chargeRecord.getExpenditure());
					}
					if ("FAIL".equals(r.get("result_code"))) {
						// ChargeRecordHandler.updateChargeRefundNumer(endChargeId);
					}
				} catch (Exception e) {
					logger.error(ordernum + e.getMessage() + "微信退款失败1");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			logger.error(ordernum + e.getMessage() + "微信退款失败2");
			e.printStackTrace();
		}
	}
	
	public void returnMsgTemp(Integer uid, String code, String order, String url, String time, Double money){//退费模板
		try {
			User user = userService.selectUserById(uid);
			if(null!=user){
				String oppenid = user.getOpenid();
				UserEquipment uscode = userEquipmentService.getUserEquipmentByCode(code);
				User meruser = userService.selectUserById(uscode.getUserId());
				JSONObject json = new JSONObject();
				json.put("first", TempMsgUtil.inforData("您好，您的退费请求已受理，资金将原路返回到您的账户中","#FF3333"));
				json.put("keyword1", TempMsgUtil.inforData("充电退费","#0044BB"));
				json.put("keyword2", TempMsgUtil.inforData(order,"#0044BB"));
				json.put("keyword3", TempMsgUtil.inforData(time,"#0044BB")); 
				json.put("keyword4", TempMsgUtil.inforData("¥ "+String.format("%.2f", money),"#0044BB"));
				json.put("keyword5", TempMsgUtil.inforData(meruser.getPhoneNum(),"#0044BB"));
				json.put("remark", TempMsgUtil.inforData("如有疑问可咨询服务商。","#0044BB"));
				TempMsgUtil.sendTempMsg(oppenid, TempMsgUtil.TEMP_IDTUI, url, json);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	public boolean checkUserIfRich(TradeRecord tradeRecord) {
		Integer manid = tradeRecord.getManid();
		Integer merid = tradeRecord.getMerid();
		Double manmoney = tradeRecord.getManmoney();
		Double mermoney = tradeRecord.getMermoney();
		Double money = tradeRecord.getMoney();
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
	}
	
	/**
	 * 电量汇总            电量要除一百
	 * @param equipmentnum 设备号
	 * @return  
	 */
//	public Object selectConsumeQuantity(Integer type,String begintime,String endtime,String equipmentnum){
//		Map<String,Object> map=new HashMap<String,Object>();
//		//时间不能为空
//		begintime = begintime.replace("/", "-") + " 00:00:00";
//		endtime = endtime.replace("/", "-") + " 23:59:59";
//		//获取到商家的登陆信息
//		User user = (User) this.request.getSession().getAttribute("user");
//		if(user!=null&&user.getAid()!=null){
//			//1，根据时间查
//			if(type==1){
//			//2：根据设备查 	
//			}else if(type==2){
//				//根据商家的id计算具体时间内每台设备耗电量
//				List<ChargeRecord> list=chargeRecordService.selectEveryConsumeQuantity(user.getId(), begintime, endtime);
//				//计算所有设备的总耗电量
//				int allConsumeQuantity=0;
//				for (ChargeRecord chargeRecord : list) {
//					allConsumeQuantity=allConsumeQuantity+chargeRecord.getConsumeQuantity();
//				}
//				map.put("总耗电量",allConsumeQuantity);
//				map.put("每台设备耗电量",list);
//				return map;
//			//3根据小区查
//			}else if(type==3){
//				
//			}else{
//				//根据设备号搜索当前设备的耗电量
//				chargeRecordService.selectConsumeQuantity(equipmentnum, begintime, endtime);
//			}
//			return null;	
//		}else{
//			map.put("商家一定要登陆才能看见","空");
//			return map;
//		}
//		
//	}
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
	public SortedMap<String, String> packagePayParamsToMapSub(HttpServletRequest request, Double money2, String openid,
			String notify_url, String format,String body) {
		SortedMap<String, String> params = new TreeMap<>();
		String code = CommUtil.toString(request.getAttribute("subMerCode"));
		logger.info("设备号"+code+"=========");
		// 根据设备号查询商户的微信配置数据
		Map<String, Object> data = userService.selectConfigDataByCode(code);
		// 参数
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
	
	public static short stringToShort(String val) {
		try {
			return Short.parseShort(val);
		} catch (Exception e) {
			return 0;
		}
	}
	
}
