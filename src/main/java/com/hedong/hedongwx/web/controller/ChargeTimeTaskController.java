package com.hedong.hedongwx.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.github.wxpay.sdk.WXPay;
import com.hedong.hedongwx.config.AlipayConfig;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.ChargeRecordHandler;
import com.hedong.hedongwx.dao.Equipmenthandler;
import com.hedong.hedongwx.dao.UserHandler;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.service.AllPortStatusService;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.GeneralDetailService;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.service.MoneyService;
import com.hedong.hedongwx.service.OnlineCardRecordService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.JedisUtils;
import com.hedong.hedongwx.utils.SendMsgUtil;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.TempMsgUtil;
import com.hedong.hedongwx.utils.WXPayConfigImpl;
import com.hedong.hedongwx.utils.WeiXinConfigParam;
import com.hedong.hedongwx.utils.XMLUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 有关订单的定时任务、数据处理
 * @author RoarWolf
 * @time 2020-04-25 9:20
 *
 */
@Controller
@EnableScheduling
@RequestMapping("/setTimerTaskSys")
public class ChargeTimeTaskController {

	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(ChargeTimeTaskController.class);
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private GeneralDetailService generalDetailService;
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private AllPortStatusService allPortStatusService;
	@Autowired
	private OnlineCardService onlineCardService;
	@Autowired
	private OnlineCardRecordService onlineCardRecordService;
	@Autowired
	private MerchantDetailService merchantDetailService;
	
	/**
	 * 设置设备处理订单的离线时间
	 * @param sysOffline 系统时间
	 * @param handOffline 手动断电的时间
	 * @return
	 */
//	@PostMapping("/setOfflineTime")
//	@ResponseBody
//	public String setTime(String sysOffline,String handOffline) {
//		Map<String,String> map = new HashMap<>();
//		boolean isnumberhand = SendMsgUtil.checkIfAllNumber(handOffline);
//		boolean isnumbersys = SendMsgUtil.checkIfAllNumber(handOffline);
//		if (!isnumberhand || !isnumbersys) {
//			return "1001";
//		} else {
//			int sysInt = Integer.parseInt(sysOffline);
//			int handInt = Integer.parseInt(handOffline);
//			if (sysInt == 0 && handInt == 0) {
//				return "1001";
//			}
//		}
//		map.put("sysOffline", sysOffline);
//		map.put("handOffline", handOffline);
//		String hmset = JedisUtils.hmset("sysTime", map);
//		if ("OK".equals(hmset)) {
//			return "1000";
//		} else {
//			return "1001";
//		}
//	}
	
	/**
	 * 查询离线设备，超过系统时间的处理未结束的订单
	 */
//	@Scheduled(cron = "0 0/5 * * * *")
	@RequestMapping("/offlineDisposeDataTask")
	@ResponseBody
    public Object offlineDisposeData(){
		// 获取离线设备
		try {
			Map<String, String> diviceOffline = JedisUtils.hgetAll("diviceOffline");
			if (diviceOffline != null) {
				Set<Entry<String, String>> entrySet = diviceOffline.entrySet();
				for (Entry<String, String> entry : entrySet) {
					String code = entry.getKey();
					System.out.println("设备号===" + code);
//					boolean isAllNumber = SendMsgUtil.checkIfAllNumber(code);
//					if (!isAllNumber) {
//						continue;
//					}
					// 设备离线的时间
					String updateTime = entry.getValue();
					long updateTimeLong = CommUtil.DateTime(updateTime, "yyyy-MM-dd HH:mm:ss").getTime();
					// 系统时间
					long currentTimeLong = System.currentTimeMillis();
					long differTime = (currentTimeLong - updateTimeLong) / 1000 / 60;
					int offlineTime = DisposeUtil.getOfflineTime(1);
					// 获取判断设备断网时间大于1小时
					if (differTime >= offlineTime) {
						Equipment equipment = equipmentService.getEquipmentById(code);
						String hardversion = equipment.getHardversion();
						// 根据设备号查询24小时未结束的订单
						List<ChargeRecord> chargingList = chargeRecordService.select24HoursUnfinshByCode(code);
						if (chargingList != null && chargingList.size() > 0) {
							// 新型设备
							if (DisposeUtil.checkIfHasV3(hardversion)) {
								for (ChargeRecord chargeRecord : chargingList) {
									v3DisposeData(chargeRecord.getId(), hardversion);
								}
							} else {
								// 老设备
								for (ChargeRecord chargeRecord : chargingList) {
									commonChargeDisposeData(chargeRecord.getId(), hardversion);
								}
							}
						}
						Equipmenthandler.redisDeciceStateAddOrRemove(1, code, "00");
					} else {
						logger.info(code + "号设备离线时间不足8小时,离线时间===" + updateTime);
					}
				}
			} else {
				logger.info("暂未有离线设备加入，本次扫描不做处理");
			} 
			return CommUtil.responseBuildInfo(200, "离线设备处理未结束订单成功", null);
		} catch (Exception e) {
			return CommUtil.responseBuildInfo(201, "离线设备处理未结束订单失败" + e.getMessage(), null);
		}
	}
	
	public void commonChargeDisposeData(Integer chargeid,String hardversion) {
		List<ChargeRecord> chargeRecordList = chargeRecordService.selectChargeAndContinueById(chargeid);
		ChargeRecord chargeRecord = chargeRecordList.get(chargeRecordList.size() - 1);
		String ordernum = chargeRecord.getOrdernum();
		String code = chargeRecord.getEquipmentnum();
		Integer port = chargeRecord.getPort();
		Integer uid = chargeRecord.getUid();
		Integer consumeTime = chargeRecord.getConsumeTime();
		Integer consumeQuantity = chargeRecord.getConsumeQuantity();
//		AllPortStatus allPortStatus = allPortStatusService.findPortStatusByEquipmentnumAndPort(chargeRecord.getEquipmentnum(), chargeRecord.getPort());
		if (consumeQuantity == 0 && consumeTime == 0) {
			// 微信支付
			if (chargeRecord.getPaytype() == 2) {
				wxRefund(chargeRecord.getExpenditure(), chargeRecord.getOrdernum(), chargeRecord);
			} else if (chargeRecord.getPaytype() == 1) {
				walletRefund(chargeRecord.getExpenditure(), chargeRecord.getOrdernum(), chargeRecord);
			} else if (chargeRecord.getPaytype() == 3) {
				aliRefund(chargeRecord);
			}
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
			if (permit == 1) {
				if (chargeRecord.getNumber() != null && chargeRecord.getNumber() == 0) {
					User user = userService.selectUserById(chargeRecord.getUid());
					Integer merid = user.getMerid() == null ? 0 : user.getMerid();
					if (chargeRecord.getMerchantid().equals(merid)) {
						double allTimeParseDouble = Double.parseDouble(chargeRecord.getDurationtime());
						Double surpTime = allTimeParseDouble - chargeRecord.getConsumeTime();
						Short power = 0;
						Double elec = chargeRecord.getConsumeQuantity() + 0.0;
						Double allTime = 0.0;
						Double quantity = 0.0;
						Double allMoney = 0.0;
						for (ChargeRecord charge : chargeRecordList) {
							allTime += Integer.parseInt(charge.getDurationtime());
							quantity += charge.getQuantity();
							allMoney += charge.getExpenditure();
						}
						Double useTime = allTime - surpTime;
						
						if (elec != null && elec != 0) {
							Double refundMoney = 0.0;
							refundMoney = 0.0;
							double round = (Math.round(refundMoney * 100) + 0.0) / 100;
							logger.info("退款金额===" + round);
							for (ChargeRecord charge : chargeRecordList) {
								if (round > charge.getExpenditure()) {
									User uUser = userService.selectUserById(chargeRecord.getUid());
									
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
									chargeRecordService.updateNumberById(null, 1, charge.getId(), null, opermoney, opermoney, CommUtil.toDateTime(), CommUtil.toDateTime());
									// 3、添加退款记录
									moneyService.payMoneys( uUser.getId(), charge.getOrdernum(), 3, 1, opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, "wallet");
									moneyService.insertWalletInfo(uid, merid, ordernum, 1, 3, 1, opermoney, opersendmoney, 
											opertomoney, operbalance, topupbalance, givebalance, new Date(),"");
									// 4、添加用户钱包明细
									generalDetailService.insertGenWalletDetail(uUser.getId(), charge.getMerchantid(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, charge.getOrdernum(), new Date(), 5);
									// 5、添加交易记录
									tradeRecordService.insertTrade(charge.getMerchantid(), charge.getUid(), charge.getOrdernum(), charge.getExpenditure(), 0.00, charge.getEquipmentnum(), 1, 1, 2, equipmentService.getEquipmentById(code).getHardversion());
								} else if (round > 0) {
									User uUser = userService.selectUserById(chargeRecord.getUid());
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
									chargeRecordService.updateNumberById(null, 2, charge.getId(), useTime.intValue() + "", CommUtil.toDouble(charge.getExpenditure()), round, CommUtil.toDateTime(), CommUtil.toDateTime());
									// 3、添加退款记录
									moneyService.payMoneys( uUser.getId(), charge.getOrdernum(), 3, 1, opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, "wallet");
									moneyService.insertWalletInfo(uid, merid, ordernum, 1, 3, 1, opermoney, opersendmoney, 
											opertomoney, operbalance, topupbalance, givebalance, new Date(),"");
									// 4、添加用户钱包明细
									generalDetailService.insertGenWalletDetail(uUser.getId(), charge.getMerchantid(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, charge.getOrdernum(), new Date(), 5);
									// 5、添加交易记录
									tradeRecordService.insertTrade(charge.getMerchantid(), charge.getUid(), charge.getOrdernum(), round, 0.00, charge.getEquipmentnum(), 1, 1, 2, equipmentService.getEquipmentById(code).getHardversion());
								} else {
									break;
								}
								logger.info("退款金额===" + round);
								round -= charge.getExpenditure();
							}
						} else {
							logger.info(ordernum + "电量已使用完不予退款:设备号===" + code);
							chargeRecordService.updateNumberById(null, null, chargeRecord.getId(), null, null, null, CommUtil.toDateTime(), null);
						}
					} else {
						chargeRecordService.updateNumberById(null, null, chargeRecord.getId(), null, null, null, CommUtil.toDateTime(), null);
						logger.info(ordernum + "用户绑定商户与设备绑定商户不一致不予退款:设备号===" + code + "---用户绑定商户id===" + merid
								+ "---设备绑定商户id===" + chargeRecord.getMerchantid());
					}
				}
			} else {
				logger.info(ordernum + "商户设置不予钱包退款:设备号===" + code);
				chargeRecordService.updateNumberById(null, null, chargeRecord.getId(), null, null, null, CommUtil.toDateTime(), null);
			}
		}
	}
	
	public void v3DisposeData(Integer chargeid,String hardversion) {
		List<ChargeRecord> chargeRecordList = chargeRecordService.selectChargeAndContinueById(chargeid);
		ChargeRecord chargeRecord = chargeRecordList.get(chargeRecordList.size() - 1);
		String code = chargeRecord.getEquipmentnum();
		Integer port = chargeRecord.getPort();
		Integer uid = chargeRecord.getUid();
		String subFee = JedisUtils.get("uid" + code + port + uid);
		Map<String,String> parse = (Map<String, String>) JSON.parse(subFee);
		String totalfee = parse.get("totalfee");
		double round = Double.parseDouble(totalfee);
		String ordernum = chargeRecord.getOrdernum();
		Integer merchantid = chargeRecord.getMerchantid();
		if (round != 0) {
//			SendMsgUtil.resetChargeData(code, chargeRecord.getPort(), uid, 0, 0);
			Map<String, String> chargeInfoMap = JedisUtils.hgetAll(ordernum);
			if (DisposeUtil.checkMapIfHasValue(chargeInfoMap)) {
				String type = chargeInfoMap.get("type");
				if ("1".equals(type)) {//在线卡 只扣费
					double paymoney = round;
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
						chargeRecordService.updateNumberById(null, null, chargeRecord.getId(), null, round, null, CommUtil.toDateTime(), null);
					}
				}
			} else if (chargeRecord.getExpenditure() == 0) {
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
				// 2、修改充电记录改为部分退款
				chargeRecordService.updateNumberById(null, null, chargeRecord.getId(), null, round, null, CommUtil.toDateTime(), null);
				// 3、添加用户钱包明细
				generalDetailService.insertGenWalletDetail(uUser.getId(), chargeRecord.getMerchantid(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, chargeRecord.getOrdernum(), new Date(), 2);
				// 4、修改交易记录金额
				tradeRecordService.updateTradeMoney(round, ordernum);
			} else {
				double allMoney = 0.0;
				for (ChargeRecord charge : chargeRecordList) {
					allMoney += charge.getExpenditure();
				}
				double refundMoney = CommUtil.subBig(allMoney, round);
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
				for (ChargeRecord charge : chargeRecordList) {
					Integer id = charge.getId();
					String ordernum2 = charge.getOrdernum();
					Double expenditure = charge.getExpenditure();
					if (refundMoney > expenditure) {
						// 3、修改充电记录改为全额退款
						chargeRecordService.updateNumberById(null, 2, id, null, null, expenditure, CommUtil.toDateTime(), CommUtil.toDateTime());
						// 4、添加交易记录
						tradeRecordService.insertTrade(merchantid, uid, ordernum2, expenditure, 0.00, code, 1, 1, 2, hardversion);
					} else if (refundMoney > 0) {
						// 3、修改充电记录改为部分退款
						chargeRecordService.updateNumberById(null, 2, id, null, null, refundMoney, CommUtil.toDateTime(), CommUtil.toDateTime());
						// 4、添加交易记录
						tradeRecordService.insertTrade(merchantid, uid, ordernum2, refundMoney, 0.00, code, 1, 1, 2, hardversion);
					}
					System.out.println("refundMoney===" + refundMoney);
					refundMoney = CommUtil.subBig(refundMoney, expenditure);
				}
			}
		} else {
			if (chargeRecord.getExpenditure() != 0) {
				if (chargeRecord.getNumber() != null && chargeRecord.getNumber() == 0) {
					if (chargeRecord.getPaytype() == 2) {
						wxRefund(chargeRecord.getExpenditure(), chargeRecord.getOrdernum(), chargeRecord);
					} else if (chargeRecord.getPaytype() == 1) {
						walletRefund(chargeRecord.getExpenditure(), chargeRecord.getOrdernum(), chargeRecord);
					} else if (chargeRecord.getPaytype() == 3) {
						aliRefund(chargeRecord);
					}
				}
			} else {
				chargeRecordService.updateNumberById(null, null, chargeRecord.getId(), null, null,
						chargeRecord.getExpenditure(), CommUtil.toDateTime(), null);
			}
		}
	}
	
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
			logger.warn(ordernum + "设备断网轮询退款失败");
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
			params.put("appid", WeiXinConfigParam.FUWUAPPID);
			params.put("mch_id", WeiXinConfigParam.MCHID);
			params.put("sub_mch_id", subMchId);
			params.put("out_trade_no", out_trade_no);
			params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
			String sign = HttpRequest.createSign("UTF-8", params);
			params.put("sign", sign);
			String url = "https://api.mch.weixin.qq.com/pay/orderquery";
			String canshu = HttpRequest.getRequestXml(params);
			String sr = HttpRequest.sendPost(url, canshu);
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
						// 修改设备的收益
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
									// 修改小区的收益
									areaService.updateAreaEarn(0, paymoney, aid,null,paymoney,null);
								} 
								// 特约商户修改退费数据
								if(merUser != null && merUser.getSubMer() == 1){
									//商户收益总额  1为加  0为减
									userService.updateMerAmount(0, paymoney, merid);
									merchantDetailService.insertMerEarningDetail(merid, paymoney, merUser.getEarnings(), ordernum, datetime, paysource, paytype, paystatus);
								}else{
									//普通商家充电退费数据
//									HelloController.dealerIncomeRefund(comment, merid, paymoney, ordernum, datetime, paysource, paytype, paystatus);
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
	
	/**
	 * 支付宝退款
	 * @param ordernum 订单号
	 * @param type 支付类型，1、充电  2、脉冲投币
	 */
	public void aliRefund(ChargeRecord chargeRecord) {
		String ordernum = chargeRecord.getOrdernum();
		AlipayClient alipayClient = null;
		alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPID,
				AlipayConfig.RSA_PRIVATE_KEY, "json", "GBK", AlipayConfig.ALIPAY_PUBLIC_KEY, "RSA2");
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		TradeRecord traderecord = tradeRecordService.getTraderecord(ordernum);
		Double money = traderecord.getMoney();
		if ((traderecord == null) || (checkUserIfRich(traderecord) == false)) {
			logger.warn("扫描退款失败，商户或合伙人余额不足");
			return;
		}
		request.setBizContent("{" + "\"out_trade_no\":\"" + ordernum + "\"," + "\"refund_amount\":" + money + "  }");
		AlipayTradeRefundResponse response;
		try {
			response = alipayClient.execute(request);
			if (response.isSuccess()) {
				ordernum = CommUtil.toString(ordernum);
				//修改本地数据
				String devicenum = CommUtil.toString(chargeRecord.getEquipmentnum());
				Integer uid = CommUtil.toInteger(chargeRecord.getUid());
				Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
				Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
				int orderid = CommUtil.toInteger(chargeRecord.getId());
				Date time = new Date();
				//String strtime = CommUtil.toDateTime(time);
				Integer paysource = MerchantDetail.CHARGESOURCE;
				Integer paytype = MerchantDetail.ALIPAY;
				Integer paystatus = MerchantDetail.REFUND;
				Double mermoney = 0.00;
				String devicehard = "";
				chargeRecordService.updateNumberById(null, 1, orderid, null, null, paymoney, CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", chargeRecord.getBegintime()), CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date()));
				try {
					equipmentService.updateEquEarn(devicenum, paymoney, 0);
				} catch (Exception e) {
					logger.warn("设备收益修改异常");
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
						//充电退费数据
						dealerIncomeRefund(comment, merid, paymoney, ordernum, time, paysource, paytype, paystatus);
					} catch (Exception e) {
						logger.warn("小区修改余额错误===" + e.getMessage());
					}
					mermoney = traderecord.getMermoney();
					Double partmoney = traderecord.getManmoney();
					Integer manid = partmoney == 0 ? 0 : -1;
					tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 1, 3, 2, devicehard, comment);
				}
			} else {
			}
		} catch (AlipayApiException e) {
			logger.error(e.getMessage() + e.getStackTrace()[0].getLineNumber());
		}
	}
	
	/**
	 * @Description： 退费（商户或商户与合伙）数据处理
	 * @author： origin 创建时间：   2019年9月21日 下午4:07:16
	 */
	public static Object dealerIncomeRefund(String comment, Integer merid, Double money, String ordernum, 
			Date time, Integer paysource, Integer paytype, Integer paystatus) {
		
		Integer type = 1;
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
	 * @return 
	 */
	public static void merEearningCalculate(Integer merid, Double money, Integer type, String ordernum, Date operattime,
			Integer paysource, Integer paytype, Integer status){
		try {
			//查询商户余额
			Double merEarnings = CommUtil.toDouble(UserHandler.getUserEarningsById(merid));
			//修改商户余额	1:商户收益减少  2:用户钱包金额添加   3:商户收益添加
			ChargeRecordHandler.updateUserMoney(money, merid, null, type);
			Double nowEarnings = 0.00;
			if(merEarnings!=0){
				if(type==1){
					nowEarnings = (merEarnings * 100 - money * 100) / 100;
				}else if(type==3){
					nowEarnings = (merEarnings * 100 + money * 100) / 100;
				}
			}
			try {
				UserHandler.merAmountRefund(merid, money);
			} catch (Exception e) {
				logger.warn("修改商户总计表失败-_-");
			}
			//添加商户余额明细
			UserHandler.addMerDetail(merid, ordernum, money, nowEarnings, paysource, paytype, status);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
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
}
