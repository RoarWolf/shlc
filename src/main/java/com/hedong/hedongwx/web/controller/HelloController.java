package com.hedong.hedongwx.web.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.hedong.hedongwx.dao.TradeRecordHandler;
import com.hedong.hedongwx.dao.UserHandler;
import com.hedong.hedongwx.entity.AllPortStatus;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.InCoins;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.mqtt.MqttPushClient;
import com.hedong.hedongwx.service.AllPortStatusService;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.AuthorityService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.GeneralDetailService;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.service.OfflineCardService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.thread.Server;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.JedisUtils;
import com.hedong.hedongwx.utils.JsSignUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.SendMsgUtil;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.TempMsgUtil;
import com.hedong.hedongwx.utils.WXPayConfigImpl;
import com.hedong.hedongwx.utils.WeiXinConfigParam;
import com.hedong.hedongwx.utils.WeixinUtil;
import com.hedong.hedongwx.utils.WolfHttpRequest;
import com.hedong.hedongwx.utils.XMLUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@EnableScheduling
public class HelloController {
	
	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private AllPortStatusService allPortStatusService;
	@Autowired
	private OfflineCardService offlineCardService;
	@Autowired
	private InCoinsService inCoinsService;
	@Autowired
	private MerchantDetailService merchantDetailService;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private GeneralDetailService generalDetailService;
	
	@RequestMapping("/wolfConstomSendData")
	@ResponseBody
	public Map<String, Object> wolfConstomSendData(String param, String devicenum) {
		SendMsgUtil.send_Param(param, devicenum);
		return CommUtil.responseBuild(200, "数据发送成功", null);
	}
	
	@RequestMapping("/wolfchargeInfo")
	@ResponseBody
	public Map<String,String> wolfchargeInfo(String ordernum) {
		Map<String, String> chargeInfoMap = JedisUtils.hgetAll(ordernum);
		if (!DisposeUtil.checkMapIfHasValue(chargeInfoMap)) {
			chargeInfoMap = new HashMap<>();
			chargeInfoMap.put("wolfcode", "1001");
		} else {
			chargeInfoMap.put("wolfcode", "1000");
		}
		return chargeInfoMap;
	}
	
	/**
	 * @Description：设置设备当前的参数	0x32
	 * @param code:  设备号
	 * @param type：   0x01,表示 温度报警温度， 0x02表示 烟感的阈值 0x03 表示 当前设备的总功率
	 * @param value： 参数的值
	 * @author： origin
	 * @createTime：2020年5月11日下午4:48:13
	 */
	@RequestMapping("/setDeviceArgument")
	@ResponseBody
	public Map<String,String> setdeviceargument(String code, Byte type, Integer value) {
		if (type == null) {
			type = 0x01;
		}
		if (value == null) {
			value = 0x00000064;
		}
		SendMsgUtil.send_0x32(type, value, code);
		
		Map<String,String> map = new HashMap<>();
		long currentTime = System.currentTimeMillis();
		boolean flag = true;
		int temp = 0;
		while (flag) {
			if (temp >=20) {
				map.put("code", "0");
				break;
			}
			Map<String, String> mapReply = Server.thresholdvalue.get(code);
			if (mapReply == null || mapReply.size() < 1) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				temp++;
				continue;
			}
			long udpatetime = Long.parseLong(mapReply.get("insttime"));
			if ((udpatetime - currentTime) > 0 && (udpatetime - currentTime) < 20000) {
				map.putAll(mapReply);
				map.put("readtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				flag = false;
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
		System.out.println("设置设备当前的参数  setDeviceArgument      "+map);
		return map;
	}
	
	/**
	 * @Description：上传当前充电桩温度（0x33）
	 * @param code： 设备号 		NOW_TEMP（now_temp）：当前充电桩温度
	 * @author： origin
	 * @createTime：2020年5月11日下午4:49:13
	 * @comment:
	 */
	@RequestMapping(value = "/uploadingTempe")
	@ResponseBody
	public Map<String, String> uploadingTempe(String code) {
		Map<String, String> hashMap = new HashMap<>();
		hashMap.put("code", code);
//		Map<String, String> map = WolfHttpRequest.httpconnectwolf(hashMap, WolfHttpRequest.SEND_READSYSTEM_URL);
		SendMsgUtil.send_0x33(code);
		long currentTime = System.currentTimeMillis();
		boolean flag = true;
		int temp = 0;
		Map<String, String> map = new HashMap<>();
		while (flag) {
			if (temp >=20) {
				map.put("status", "0");
				break;
			}
			Map<String, String> mapRevice = Server.uploadingMap.get(code);
			if (mapRevice == null || mapRevice.size() < 1) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				temp++;
				continue;
			}
			long udpatetime = Long.parseLong(mapRevice.get("insttime"));
			if ((udpatetime - currentTime) > 0 && (udpatetime - currentTime) < 20000) {
				map.putAll(mapRevice);
				map.put("readtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				flag = false;
				Server.readsystemMap.remove(code);
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
		System.out.println("===" + JSON.toJSONString(map));
		System.out.println("上传当前充电桩温度  uploadingTempe      "+map);
		return map;
	}
	
	/**
	 * @Description：获取当前设备的参数	0x34
	 * @param code：设备号   
	 * @param  type [设置参数类型]  0x01,表示 当前温度；0x02表示 当前烟量；0x03 表示 当前设备的总功率；0x04 表示 当前设备电表电量
	 * @author： origin
	 * @createTime：2020年5月11日下午4:51:43
	 * @comment:
	 */
	@RequestMapping(value = "/getDeviceNowArgument")
	@ResponseBody
	public Map<String, String> getDeviceNowArgument(byte type, String code) {
		Map<String, String> hashMap = new HashMap<>();
		hashMap.put("code", code);
		//		Map<String, String> map = WolfHttpRequest.httpconnectwolf(hashMap, WolfHttpRequest.SEND_READSYSTEM_URL);
		SendMsgUtil.send_0x34(type, code);
		long currentTime = System.currentTimeMillis();
		boolean flag = true;
		int temp = 0;
		Map<String, String> map = new HashMap<>();
		while (flag) {
			if (temp >=20) {
				map.put("status", "0");
				break;
			}
			Map<String, String> mapRevice = Server.getParameMap.get(code);
			if (mapRevice == null || mapRevice.size() < 1) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				temp++;
				continue;
			}
			long udpatetime = Long.parseLong(mapRevice.get("insttime"));
			if ((udpatetime - currentTime) > 0 && (udpatetime - currentTime) < 20000) {
				map.putAll(mapRevice);
				map.put("readtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				flag = false;
				Server.readsystemMap.remove(code);
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
		System.out.println("===" + JSON.toJSONString(map));
		System.out.println("获取当前设备的参数度  getDeviceNowArgument      "+map);
		return map;
	}
	
	/**
	 * @Description：获取设备设置的参数	0x35
	 * @param type(设置参数类型):0x01,表示 板子保存的报警温度；0x02表示 板子保存的报警烟感；0x03 表示 板子保存的报警总功率
	 * @param code 设备号
	 * @author： origin 2020年5月11日下午5:27:42
	 */
	@RequestMapping(value = "/getDeviceSetArgument")
	@ResponseBody
	public Map<String, String> getDeviceSetArgument( byte type, String code) {
		Map<String, String> hashMap = new HashMap<>();
		hashMap.put("code", code);
//		Map<String, String> map = WolfHttpRequest.httpconnectwolf(hashMap, WolfHttpRequest.SEND_READSYSTEM_URL);
		SendMsgUtil.send_0x35( type, code);
		long currentTime = System.currentTimeMillis();
		boolean flag = true;
		int temp = 0;
		Map<String, String> map = new HashMap<>();
		while (flag) {
			if (temp >=20) {
				map.put("status", "0");
				break;
			}
			Map<String, String> mapRevice = Server.getArgumentMap.get(code);
			if (mapRevice == null || mapRevice.size() < 1) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				temp++;
				continue;
			}
			long udpatetime = Long.parseLong(mapRevice.get("insttime"));
			if ((udpatetime - currentTime) > 0 && (udpatetime - currentTime) < 20000) {
				map.putAll(mapRevice);
				map.put("readtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				flag = false;
				Server.readsystemMap.remove(code);
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
		System.out.println("===" + JSON.toJSONString(map));
		System.out.println("获取设备设置的参数   getDeviceSetArgument      "+map);
		return map;
	}
	
	/**
	 * 
	 * @Description：报警(温度烟感功率保险丝继电器报警)	0x36
	 * @param type(设置参数类型):	0x01,温度报警；0x02烟量报警;0x03总功率报警;0x04 继电器粘连报警;0x05 保险丝断掉报警
	 * @param port:	端口号，参数只在TYPE为0x04和0x05时使用，其他为0xFF
	 * @param value:	温度 烟感 总功率的值 TYPE为0x04和0x05时 为0xffffffff
	 * @param code
	 * @author： origin 2020年5月11日下午5:29:39
	 */
	@RequestMapping(value = "/giveAnAlarm")
	@ResponseBody
	public Map<String, String> giveAnAlarm( byte type, byte port, int value, String code) {
		Map<String, String> hashMap = new HashMap<>();
		hashMap.put("code", code);
//		Map<String, String> map = WolfHttpRequest.httpconnectwolf(hashMap, WolfHttpRequest.SEND_READSYSTEM_URL);
		if(type==4 || type==5){
//			温度 烟感 总功率的值 TYPE为0x04和0x05时 为0xffffffff
			value = 0xffffffff;
		}else{
			port = (byte) 0xFF;
		}
		SendMsgUtil.send_0x36(type, port, value, code);
		long currentTime = System.currentTimeMillis();
		boolean flag = true;
		int temp = 0;
		Map<String, String> map = new HashMap<>();
		while (flag) {
			if (temp >=20) {
				map.put("status", "0");
				break;
			}
			Map<String, String> mapRevice = Server.alarmMap.get(code);
			if (mapRevice == null || mapRevice.size() < 1) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				temp++;
				continue;
			}
			long udpatetime = Long.parseLong(mapRevice.get("insttime"));
			if ((udpatetime - currentTime) > 0 && (udpatetime - currentTime) < 20000) {
				map.putAll(mapRevice);
				map.put("readtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				flag = false;
				Server.readsystemMap.remove(code);
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
		System.out.println("===" + JSON.toJSONString(map));
		System.out.println("报警(温度烟感功率保险丝继电器报警)   giveAnAlarm      "+map);
		return map;
	}
	
	
	
	@RequestMapping("/updradeInform")
	@ResponseBody
	public Map<String,String> updradeInform(String code) {
//		SendMsgUtil.send_0x2F(code);
		Map<String,String> map = new HashMap<>();
		map.put("wolfcode", "1000");
		map.put("wolfmsg", "success");
		return map;
	}
	
	@RequestMapping("/updradeDataSend")
	@ResponseBody
	public Map<String,String> updradeDataSend() {
		Map<String,String> map = new HashMap<>();
		try {
			MqttPushClient.sendUpgradeData();
			map.put("wolfcode", "1000");
			map.put("wolfmsg", "success");
		} catch (Exception e) {
			map.put("wolfcode", "1001");
			map.put("wolfmsg", "fail");
		}
		return map;
	}
	
	@RequestMapping("/wolfmaininfo")
	@ResponseBody
	public Map<String,String> maininfo(String code) {
		Map<String,String> map = new HashMap<>();
		map.put("code", code);
		return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_MAININFO_URL);
//		SendMsgUtil.send_0x23(code);
//		long nowTime = System.currentTimeMillis();
//		boolean flag = true;
//		int temp = 0;
//		while (flag) {
//			if (temp >= 20) {
//				Map<String,String> map = new HashMap<>();
//				map.put("wolferror", "1001");
//				map.put("wolfmsg", "连接超时，请检查设备是否在线或设备不含此功能");
//				return map;
//			}
//			if (Server.maininfoMap != null) {
//				Map<String, String> map = Server.maininfoMap.get(code);
//				if (map != null) {
//					long updateTime = Long.parseLong(map.get("updateTime"));
//					if (updateTime - nowTime <= 20000 && updateTime - nowTime >= 0) {
//						return map;
//					} else {
//						temp++;
//						try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						continue;
//					}
//				} else {
//					temp++;
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					continue;
//				}
//			} else {
//				temp++;
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				continue;
//			}
//		}
//		return null;
	}
	
	@RequestMapping("/wolfsetsys")
	@ResponseBody
	public Map<String, String> wolfsetsys(String code,int cst,int elec_pri) {
		Map<String,String> map = new HashMap<>();
		map.put("code", code);
		map.put("cst", cst + "");
		map.put("elec_pri", elec_pri + "");
		return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_NEWREADSYS_URL);
//		SendMsgUtil.send_0x2a(code, (short)cst, (short)elec_pri);
//		long nowTime = System.currentTimeMillis();
//		boolean flag = true;
//		int temp = 0;
//		while (flag) {
//			if (temp >= 20) {
//				Map<String,String> map = new HashMap<>();
//				map.put("wolferror", "1001");
//				map.put("wolfmsg", "连接超时，请检查设备是否在线或设备不含此功能");
//				return map;
//			}
//			if (Server.maininfoMap != null) {
//				Map<String, String> map = Server.setSystemMap.get(code);
//				if (map != null) {
//					long updateTime = Long.parseLong(map.get("updateTime"));
//					if (updateTime - nowTime <= 20000 && updateTime - nowTime >= 0) {
//						return map;
//					} else {
//						temp++;
//						try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						continue;
//					}
//				} else {
//					temp++;
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					continue;
//				}
//			} else {
//				temp++;
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				continue;
//			}
//		}
//		return null;
	}
	
	@RequestMapping("/wolfreadsys")
	@ResponseBody
	public Map<String,String> wolfreadsys(String code) {
		Map<String,String> map = new HashMap<>();
		map.put("code", code);
		return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_NEWREADSYS_URL);
//		SendMsgUtil.send_0x2b(code);
//		long nowTime = System.currentTimeMillis();
//		boolean flag = true;
//		int temp = 0;
//		while (flag) {
//			if (temp >= 20) {
//				Map<String,String> map = new HashMap<>();
//				map.put("wolferror", "1001");
//				map.put("wolfmsg", "连接超时，请检查设备是否在线或设备不含此功能");
//				return map;
//			}
//			if (Server.maininfoMap != null) {
//				Map<String, String> map = Server.readsystemMap.get(code);
//				if (map != null) {
//					long updateTime = Long.parseLong(map.get("updateTime"));
//					if (updateTime - nowTime <= 20000 && updateTime - nowTime >= 0) {
//						return map;
//					} else {
//						temp++;
//						try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						continue;
//					}
//				} else {
//					temp++;
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					continue;
//				}
//			} else {
//				temp++;
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				continue;
//			}
//		}
//		return null;
	}
	
	@RequestMapping("/wolftestpay")
	@ResponseBody
	public String wolftestpay(String code,int port,int money, int time, int elec, int chargeType) {
		SendMsgUtil.send_0x27((byte)port, (short)money, (short)time, (short)elec, code, (byte)chargeType);
		return "发送远程充电成功";
	}

	@RequestMapping("/getLocationBySendData")
	@ResponseBody
	public String getLocationBySendData(String code) {
		SendMsgUtil.send_0x29(code);
		return "1";
	}
	
	@RequestMapping("/getNormalAccessToken")
	@ResponseBody
	public String getNormalAccessToken() {
		String accessToken = "";
		try {
			accessToken = WeixinUtil.getBasicAccessToken();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accessToken;
	}
	
	@RequestMapping("/wolfquery")
	@ResponseBody
	public Object wolfquery() {
		System.out.println("---" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "---");
		return Equipmenthandler.getAllSoftVersionIs00();
	}
	
//	@Scheduled(cron = "0 0 0 * * *")
	@RequestMapping("/packageMonthTodaynumResetTask")
	@ResponseBody
	public Object packageMonthTodaynumReset() {
		try {
			userService.everydaynumReset();
			System.out.println("每日重置包月当日剩余次数");
			equipmentService.everydayResetEquEarn();
			System.out.println("每日重置当日设备在线和投币收益");
			userService.everydayResetNowEarn();
			System.out.println("每日重置当日商户在线和投币收益");
			List<Area> selectAllArea = areaService.selectAllArea();
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
			Date today = calendar.getTime();
			for (Area area : selectAllArea) {
				areaService.insertAreastatistics(area.getId(), area.getNowOnlineEarn(), area.getNowCoinsEarn(),
						area.getWalletEarn(), area.getEquEarn(), area.getCardEarn(), today);
			}
			areaService.everydayAreaReset();
			System.out.println("每日重置当日小区在线和投币收益");
			return CommUtil.responseBuildInfo(200, "重置成功", null);
		} catch (Exception e) {
			return CommUtil.responseBuildInfo(201, "重置失败：" + e.getMessage(), null);
		}
	}
	
//	@Scheduled(cron = "0 0 1 * * *")
	@RequestMapping(value="/deleteRedundanceLogsTask")
	@ResponseBody
	public Object deleteRedundanceLogs() {
		try {
			System.out.println("删除日志开始");
			chargeRecordService.delectRealChargeByTime(DisposeUtil.getPastDate(6, 2));
			System.out.println("删除日志结束");
			return CommUtil.responseBuildInfo(200, "删除日志完成", null);
		} catch (Exception e) {
			return CommUtil.responseBuildInfo(201, "删除日志失败", null);
		}
	}
	
//	@Scheduled(cron = "0 0/5 * * * *")
	@RequestMapping(value="/wolfChargeTask")
	@ResponseBody
    public Object timer(){
		try {
	        //获取当前时间
			logger.info("每隔5分钟扫描事件触发");
			//查询最近10分钟之内的投币记录没有回复且没有退款的记录
			List<InCoins> incoinsList = inCoinsService.selectInCoinsNoReply();
			if (incoinsList != null && incoinsList.size() > 0) {
				logger.info("已搜索到最近10分钟没有回复且没有退款的记录,记录长度===" + incoinsList.size());
				for (InCoins incoins : incoinsList) {
					Integer merchantid = incoins.getMerchantid();
					long time = incoins.getBeginTime().getTime();
					long currentTime = System.currentTimeMillis();
					if ((currentTime - time) > 120000) {
						Integer incoinRefund = authorityService.selectIncoinRefund(merchantid);
						if (incoinRefund == null || incoinRefund == 1) {
							logger.info(incoins.getOrdernum() + "--可退款--时间比较==" + currentTime + ":" + time);
							if (incoins.getHandletype() == 1) {
								WXPayConfigImpl config;
								try {
									config = WXPayConfigImpl.getInstance();
									WXPay wxpay = new WXPay(config);
									String total_fee = "";
									String out_trade_no = "";
									String moneyStr = String.valueOf(incoins.getMoney() * 100);
									int idx = moneyStr.lastIndexOf(".");
									total_fee = moneyStr.substring(0, idx);
									out_trade_no = incoins.getOrdernum();// 退款订单号
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
									Map<String, String> resultMap = XMLUtil.doXMLParse(sr);
									if (resultMap.get("return_code").toString().equalsIgnoreCase("SUCCESS")) {
										HashMap<String, String> data = new HashMap<String, String>();
										data.put("appid", WeiXinConfigParam.FUWUAPPID);
										data.put("mch_id", WeiXinConfigParam.MCHID);
										data.put("sub_mch_id", WeiXinConfigParam.SUBMCHID);
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
												inCoinsService.updateInCoinsStatus(out_trade_no, (byte) 4);
												if (incoins.getHandletype() != 4 && incoins.getHandletype() != 9) {
													//脉冲退费
													String ordernum = CommUtil.toString(out_trade_no);
													String devicenum = CommUtil.toString(incoins.getEquipmentnum());
													Integer uid = CommUtil.toInteger(incoins.getUid());
													Integer merid = CommUtil.toInteger(incoins.getMerchantid());
													Double paymoney = CommUtil.toDouble(incoins.getMoney());
													Integer orderid = CommUtil.toInteger(incoins.getId());
													Date datetime = new Date();
													String strtime = CommUtil.toDateTime(datetime);
													Integer paysource = MerchantDetail.INCOINSSOURCE;
													Integer paytype = MerchantDetail.WEIXINPAY;
													Integer paystatus = MerchantDetail.REFUND;
													Double mermoney = 0.00;
													String devicehard = "";
													
													TradeRecord traderecord = tradeRecordService.getTraderecord(incoins.getOrdernum());
													String comment = CommUtil.toString(traderecord.getComment());
													try {
														Equipment equipment = equipmentService.getEquipmentById(devicenum);
														devicehard = CommUtil.toString(equipment.getHardversion());
														Integer aid = CommUtil.toInteger(equipment.getAid());
														if (aid != null && aid != 0) {
															areaService.updateAreaEarn(0, paymoney, aid,null,paymoney,null);
														} 
													} catch (Exception e) {
														logger.warn("小区修改余额错误===" + e.getMessage());
													}
													try {
														equipmentService.updateEquEarn(devicenum, paymoney, 0);
													} catch (Exception e) {
														logger.warn("设备收益修改异常");
													}
													//脉冲退费处理数据
													dealerIncomeRefund(comment, merid, paymoney, ordernum, datetime, paysource, paytype, paystatus);
													mermoney = traderecord.getMermoney();
													Double partmoney = traderecord.getManmoney();
													Integer manid = partmoney == 0 ? 0 : -1;
													tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 2, traderecord.getPaytype(), 2, devicehard, comment);
													String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=3&id="+orderid;
													returnMsgTemp(uid, devicenum, ordernum, urltem, strtime, paymoney);
	//											}
	//											mermoney = traderecord.getMermoney();
	//											Double partmoney = traderecord.getManmoney();
	//											Integer manid = partmoney == 0 ? 0 : -1;
	//											if (wolfkey == 3) {
	//												tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 2, 4, 2, devicehard, comment);
	//											} else {
	//												tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 2, 2, 2, devicehard, comment);
	//											}
	//										}
													
	//												if( null==manid || manid==0){
	//													userService.updateUserEarnings(0, incoins.getMoney(), user.getId());
	//													user.setEarnings((user.getEarnings() * 100 - incoins.getMoney() * 100) / 100);
	//													merchantDetailService.insertMerEarningDetail(user.getId(), incoins.getMoney(), user.getEarnings(), 
	//															out_trade_no, new Date(), MerchantDetail.INCOINSSOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
	//													Equipment equipmentById = equipmentService.getEquipmentById(incoins.getEquipmentnum());
	//													tradeRecordService.insertTrade(user.getId(), incoins.getUid(), out_trade_no, incoins.getMoney(), incoins.getMoney(),
	//															incoins.getEquipmentnum(), 2, 2, 2, equipmentById.getHardversion());
	//													try {
	//														userService.updateMerAmount(0, incoins.getMoney(), incoins.getMerchantid());
	//													} catch (Exception e) {
	//														logger.warn("商户总计更改出错");
	//													}
	//												}else{
	//													User manuser = userService.selectUserById(manid);
	//													userService.updateUserEarnings(0, traderecord.getMermoney(), user.getId());
	//													userService.updateUserEarnings(0, traderecord.getManmoney(), manid);
	//													manuser.setEarnings((manuser.getEarnings() * 100 - traderecord.getManmoney() * 100) / 100);
	//													user.setEarnings((user.getEarnings() * 100 - traderecord.getMermoney() * 100) / 100);
	//													merchantDetailService.insertMerEarningDetail(user.getId(), traderecord.getMermoney(), user.getEarnings(), 
	//															out_trade_no, new Date(), MerchantDetail.INCOINSSOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
	//													merchantDetailService.insertMerEarningDetail(manuser.getId(), traderecord.getManmoney(), manuser.getEarnings(), 
	//															out_trade_no, new Date(), MerchantDetail.INCOINSSOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.REFUND);
	//													Equipment equipmentById = equipmentService.getEquipmentById(incoins.getEquipmentnum());
	//													tradeRecordService.insertToTrade(user.getId(), manid, incoins.getUid(), out_trade_no, incoins.getMoney(), 
	//															traderecord.getMermoney(),traderecord.getManmoney(), incoins.getEquipmentnum(), 2, 2, 2, equipmentById.getHardversion());
	//													try {
	//														userService.updateMerAmount(0, traderecord.getMermoney(), user.getId());
	//														userService.updateMerAmount(0, traderecord.getManmoney(), manid);
	//													} catch (Exception e) {
	//														logger.warn("商户总计更改出错");
	//													}
	//												}
												}
	//											String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=3&id="+incoins.getId();
	//											returnMsgTemp(incoins.getUid(), incoins.getEquipmentnum(), incoins.getOrdernum(), urltem, StringUtil.toDateTime(), incoins.getMoney());
											}
											if ("FAIL".equals(r.get("result_code"))) {
												// ChargeRecordHandler.updateChargeRefundNumer(endChargeId);
											}
										} catch (Exception e) {
											logger.error(incoins.getOrdernum() + e.getMessage() + "微信退款失败");
										}
									}
								} catch (Exception e) {
									logger.error(incoins.getOrdernum() + e.getMessage() + "微信退款失败");
								}
							} else if (incoins.getHandletype() == 2) {
								aliRefund(incoins.getOrdernum(), 2, 1);
							}
						} else {
							logger.info("商户设置不开启系统自动扫描退款");
						}
					} else {
						logger.info(incoins.getOrdernum() + "--不可退款--时间比较==" + currentTime + ":" +  time);
					}
				}
			} else {
				logger.info("未搜索到最近10分钟没有回复且没有退款的记录");
			}
			chargeQueryNoReply();
			return CommUtil.responseBuildInfo(200, "success", null);
		} catch (Exception e) {
			return CommUtil.responseBuildInfo(201, "fail", null);
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
	 * 支付宝退款
	 * @param ordernum 订单号
	 * @param type 支付类型，1、充电  2、脉冲投币
	 */
	public void aliRefund(String ordernum,int type,int orderid) {
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
				if (type == 1) {
					ChargeRecord chargeRecord = chargeRecordService.chargeRecordOne(orderid);
					ordernum = CommUtil.toString(ordernum);
					//修改本地数据
					String devicenum = CommUtil.toString(chargeRecord.getEquipmentnum());
					Integer uid = CommUtil.toInteger(chargeRecord.getUid());
					Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
					Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
					orderid = CommUtil.toInteger(orderid);
					Date time = new Date();
					//String strtime = CommUtil.toDateTime(time);
					Integer paysource = MerchantDetail.CHARGESOURCE;
					Integer paytype = MerchantDetail.ALIPAY;
					Integer paystatus = MerchantDetail.REFUND;
					Double mermoney = 0.00;
					String devicehard = "";
					chargeRecordService.updateNumberById(null, 1, orderid, null, null, paymoney, CommUtil.toDateTime(), CommUtil.toDateTime());
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
				} else if (type == 2) {
					inCoinsService.updateInCoinsStatus(ordernum, (byte) 5);
					orderid = CommUtil.toInteger(orderid);
					Date time = new Date();
					InCoins incoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
					//修改本地数据
					String devicenum = CommUtil.toString(incoins.getEquipmentnum());
					Integer uid = CommUtil.toInteger(incoins.getUid());
					Integer merid = CommUtil.toInteger(incoins.getMerchantid());
					Double paymoney = CommUtil.toDouble(incoins.getMoney());
					Integer paysource = MerchantDetail.INCOINSSOURCE;
					Integer paytype = MerchantDetail.ALIPAY;
					Integer paystatus = MerchantDetail.REFUND;
					Double mermoney = 0.00;
					String devicehard = "";
					Integer aid = 0;
					try {
						equipmentService.updateEquEarn(devicenum, traderecord.getMoney(), 0);
					} catch (Exception e) {
						logger.warn("设备收益修改异常");
					}
//					if (incoins.getHandletype() != 5 && incoins.getHandletype() != 10) {
						try {
							Equipment equipment = equipmentService.getEquipmentById(devicenum);
							devicehard = CommUtil.toString(equipment.getHardversion());
							aid = CommUtil.toInteger(equipment.getAid());
							String comment = CommUtil.toString(traderecord.getComment());
							if (aid != null && aid != 0) {
								areaService.updateAreaEarn(0, traderecord.getMoney(), aid,null,traderecord.getMoney(),null);
							} 
							//脉冲退费处理数据
							dealerIncomeRefund(comment, merid, paymoney, ordernum, time, paysource, paytype, paystatus);
							mermoney = traderecord.getMermoney();
							Double partmoney = traderecord.getManmoney();
							Integer manid = partmoney == 0 ? 0 : aid;
							tradeRecordService.insertToTrade(merid, manid, uid, ordernum, paymoney, mermoney, partmoney, devicenum, 2, 3, 2, devicehard, comment);
						} catch (Exception e) {
							logger.warn("小区修改余额错误===" + e.getMessage());
						}
//					}
//					Integer manid = traderecord.getManid();
//					try {
//						Equipment equipment = equipmentService.getEquipmentById(traderecord.getCode());
//						Integer aid = equipment.getAid();
//						if (aid != null && aid != 0) {
//							areaService.updateAreaEarn(0, traderecord.getMoney(), aid,null,traderecord.getMoney(),null);
//						} 
//					} catch (Exception e) {
//						logger.warn("小区修改余额错误===" + e.getMessage());
//					}
//					try {
//						equipmentService.updateEquEarn(traderecord.getCode(), traderecord.getMoney(), 0);
//					} catch (Exception e) {
//						logger.warn("设备收益修改异常");
//					}
//					if( null==manid || manid==0){
//						User user = userService.selectUserById(traderecord.getMerid());
//						user.setEarnings((user.getEarnings() * 100 - traderecord.getMoney() * 100) / 100);
//						userService.updateUserEarnings(0, traderecord.getMoney(), traderecord.getMerid());
//						try {
//							userService.updateMerAmount(0, traderecord.getMoney(), traderecord.getMerid());
//						} catch (Exception e) {
//							logger.warn("商户总计更改出错");
//						}
//						merchantDetailService.insertMerEarningDetail(user.getId(), traderecord.getMoney(), user.getEarnings(), ordernum, new Date(), MerchantDetail.INCOINSSOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
//						tradeRecordService.insertTrade(user.getId(), 0, ordernum, traderecord.getMoney(), traderecord.getMoney(), traderecord.getCode(), 2, 3, 2,equipmentService.getEquipmentById(traderecord.getCode()).getHardversion());
//					}else{
//						User manuser = userService.selectUserById(manid);
//						manuser.setEarnings((manuser.getEarnings() * 100 - traderecord.getManmoney() * 100) / 100);
//						User user = userService.selectUserById(traderecord.getMerid());
//						user.setEarnings((user.getEarnings() * 100 - traderecord.getMermoney() * 100) / 100);
//						userService.updateUserEarnings(0, traderecord.getManmoney(), traderecord.getManid());
//						userService.updateUserEarnings(0, traderecord.getMermoney(), traderecord.getMerid());
//						try {
//							userService.updateMerAmount(0, traderecord.getManmoney(), traderecord.getManid());
//							userService.updateMerAmount(0, traderecord.getMermoney(), traderecord.getMerid());
//						} catch (Exception e) {
//							logger.warn("商户总计更改出错");
//						}
//						merchantDetailService.insertMerEarningDetail(user.getId(), traderecord.getMermoney(), user.getEarnings(), 
//						  ordernum, new Date(), MerchantDetail.INCOINSSOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
//						merchantDetailService.insertMerEarningDetail(manuser.getId(), traderecord.getManmoney(), manuser.getEarnings(), ordernum, 
//								new Date(), MerchantDetail.INCOINSSOURCE, MerchantDetail.ALIPAY, MerchantDetail.REFUND);
//						tradeRecordService.insertToTrade(user.getId(), manid, 0, ordernum, traderecord.getMoney(), traderecord.getMermoney(),
//								traderecord.getManmoney(), traderecord.getCode(), 2, 3, 2, equipmentService.getEquipmentById(traderecord.getCode()).getHardversion());
//						System.out.println("--- 支付宝分成 脉冲退费  修改成功---");
//					}
				}
			} else {
			}
		} catch (AlipayApiException e) {
			logger.error(e.getMessage() + e.getStackTrace()[0].getLineNumber());
		}
	}
	
	public void chargeQueryNoReply() {
		try {
			List<ChargeRecord> chargelist = chargeRecordService.selectChargeNoReply();
			if (chargelist != null && chargelist.size() != 0) {
				long currentTime = System.currentTimeMillis();
				for (ChargeRecord chargeRecord : chargelist) {
					Integer merid = chargeRecord.getMerchantid();
					long begintime = chargeRecord.getBegintime().getTime();
					Double money = chargeRecord.getExpenditure();
					String ordernum = chargeRecord.getOrdernum();
					Integer port = chargeRecord.getPort();
					String equipmentnum = chargeRecord.getEquipmentnum();
//					AllPortStatus portStatus = allPortStatusService.findPortStatusByEquipmentnumAndPort(equipmentnum, port);
					Map<String, String> codeMap = JedisUtils.hgetAll(equipmentnum);
					if (codeMap == null || codeMap.size() == 0) {
						break;
					}
					Map<String, String> parse = (Map<String, String>) JSON.parse(codeMap.get(port + ""));
					if (parse != null && parse.size() > 0) {
						Short time = Short.parseShort(parse.get("time"));
						Short elec = Short.parseShort(parse.get("elec"));
						int portstatus = Integer.parseInt(parse.get("portStatus"));
						int ratedTime = Integer.parseInt(chargeRecord.getDurationtime());
						int ratedElec = chargeRecord.getQuantity();
						if ((time == ratedTime && elec == ratedElec) || portstatus == 1) {
							if ((currentTime - begintime) > 210000) {
								TradeRecord traderecord = tradeRecordService.getTraderecord(ordernum);
								logger.info(ordernum + "--可退款--时间比较==" + currentTime + ":" + begintime);
								if (chargeRecord.getPaytype() == 2) {
									WXPayConfigImpl config;
									try {
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
													merid = CommUtil.toInteger(chargeRecord.getMerchantid());
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
														chargeRecordService.updateNumberById(null, 1, orderid, null, null, paymoney, CommUtil.toDateTime(), CommUtil.toDateTime());
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
															// 特约商户修改退费数据
															if(merUser != null && merUser.getSubMer() == 1){
																//商户收益总额  1为加  0为减
																userService.updateMerAmount(0, paymoney, merid);
																merchantDetailService.insertMerEarningDetail(merid, paymoney, merUser.getEarnings(), ordernum, datetime, paysource, paytype, paystatus);
															}else{
																//充电退费数据
																dealerIncomeRefund(comment, merid, paymoney, ordernum, datetime, paysource, paytype, paystatus);
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
								} else if (chargeRecord.getPaytype() == 3) {
									aliRefund(ordernum,1,chargeRecord.getId());
								} else if (chargeRecord.getPaytype() == 1) {
									Integer uid = chargeRecord.getUid();
									User user = userService.selectUserById(uid);

									Double userBalance = CommUtil.toDouble(user.getBalance());
									Double usersendmoney = CommUtil.toDouble(user.getSendmoney());
									
									Double opermoney = CommUtil.toDouble(money);
									Double opersendmoney = 0.00;
									Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
									Double topupbalance = CommUtil.addBig(opermoney, userBalance);
									Double givebalance = CommUtil.addBig(opersendmoney, usersendmoney);
									Double operbalance = CommUtil.addBig(topupbalance, givebalance);
									
									user.setBalance(topupbalance);
									user.setSendmoney(givebalance);
									userService.updateUserById(user);
									generalDetailService.insertGenWalletDetail(user.getId(), user.getMerid(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance , ordernum, new Date(), 5);
									tradeRecordService.insertToTrade(traderecord.getMerid(), traderecord.getManid(), traderecord.getUid(), traderecord.getOrdernum(), opertomoney, traderecord.getMermoney(), 
											traderecord.getManmoney(), traderecord.getCode(), traderecord.getPaysource(), traderecord.getPaytype(), 2, traderecord.getHardver());
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									chargeRecordService.updateNumberById(null, 1, chargeRecord.getId(), null, null,
											chargeRecord.getExpenditure(), sdf.format(chargeRecord.getBegintime()), CommUtil.toDateTime());
								}
							} else {
								logger.info(ordernum + "--不可退款--时间比较==" + currentTime + ":" +  begintime);
							}
						} else {
							logger.info(ordernum + "--不可退款--端口时间或电量已改变");
						}
					} else {
						logger.info(ordernum + "端口状态不在空闲不予退款");
					}
				}
			} else {
				logger.info("未搜到10分钟内付款成功设备为回复的订单");
			}
		} catch (Exception e) {
			logger.warn("扫描充电未回复退款异常");
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
	
	@RequestMapping("/a")
	public String login() {
		return "computer/login";
	}
	
//	@RequestMapping("/onLogin")
//	@ResponseBody
//	public JSONObject onLogin(String code) {
//		JSONObject weChatToken = null;
//		try {
//			weChatToken = WeixinUtil.smallWeChatToken(code);
//		} catch (Exception e) {
//		}
//		return weChatToken;
//	}
	
	@RequestMapping("/bluetooth1")
	public String bluetooth1(Model model) {
		try {
			Map<String, String> map = JsSignUtil.sign(CommonConfig.ZIZHUCHARGE+"/bluetooth1");

			model.addAttribute("nonceStr", map.get("nonceStr"));
			model.addAttribute("timestamp", map.get("timestamp"));
			model.addAttribute("signature", map.get("signature"));
			model.addAttribute("appId", map.get("appId"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "bluetooth/bluetooth1";
	}
	
	@RequestMapping("/")
	public String home() {
		return "home";
	}
	
	@RequestMapping("/testTemp")
	public String testTemp() {
		return "testTemp";
	}
	
	@RequestMapping("/helpdoc")
	public String helpdoc() {
		return "helpdoc";
	}
	
	@RequestMapping("/testpay")
	public String testpay() {
		return "testpay";
	}
	
	@RequestMapping("selectHardversion")
	public String selectHardversion() {
		return "selectHardversion";
	}
	
	@RequestMapping("incoins")
	@ResponseBody
	public Map<String, String> incoins(String code,Byte money) {
		money = money == null ? 0 : money;
//		SendMsgUtil.send_0x83(code, (byte)1, money);
		Map<String, String> map = WolfHttpRequest.sendIncoinsPaydata(code, (byte)1, money);
//		long nowTime = System.currentTimeMillis();
//		boolean flag = true;
//		int temp = 0;
//		Map<String, String> map = new HashMap<>();
//		while (flag) {
//			map = Server.inCoinsMap.get(code);
//			if (temp >= 15) {
//				map = new HashMap<>();
//				map.put("err", "1");
//				map.put("errinfo", "连接超时...");
//				flag = false;
//				break;
//			}
//			if (map == null || map.size() == 0) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					System.out.println(e.getMessage());
//				}
//				temp++;
//				continue;
//			} else {
//				String testNowtime = map.get("testNowtime");
//				if (testNowtime != null) {
//					long nowtimes =  Long.parseLong(map.get("testNowtime"));
//					if (nowtimes - nowTime > 0 && nowtimes - nowTime < 20000) {
//						flag = false;
//						break;
//					} else {
//						try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							System.out.println(e.getMessage());
//						}
//						temp++;
//						continue;
//					}
//				} else {
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						System.out.println(e.getMessage());
//					}
//					temp++;
//					continue;
//				}
//			}
//		}
		return map;
	}
	
	@RequestMapping(value = "/connectInCoins",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> connectInCoins(String code,String nowtime) {
		long nowTime = System.currentTimeMillis();
		if (nowtime != null) {
			nowTime = Long.parseLong(nowtime);
		}
		boolean flag = true;
		int temp = 0;
		Map<String, String> map = new HashMap<>();
		while (flag) {
			map = Server.inCoinsMap.get(code);
			if (temp >= 15) {
				map = new HashMap<>();
				map.put("err", "1");
				map.put("errinfo", "连接超时...");
				flag = false;
				break;
			}
			if (map == null || map.size() == 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
				temp++;
				continue;
			} else {
				if (map.get("nowtime") == null) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
					temp++;
					continue;
				}
				long nowtimes =  Long.parseLong(map.get("nowtime"));
				if (nowtimes - nowTime > 0 && nowtimes - nowTime < 20000) {
					flag = false;
					break;
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
					temp++;
					continue;
				}
			}
		}
		return map;
	}
	
	@RequestMapping("/connectus")
	public String connectus() {
		return "connectus";
	}
	
	@RequestMapping("/offlineCard")
	public String offlineCard(Model model) {
		model.addAttribute("code", "000001");
		return "offlineCard";
	}
	
	/**
	 * 离线卡充值前的查询
	 * @param code 设备号
	 * @param openid 用户的openid
	 * @param nowtime 系统时间
	 * @return {@link Map}
	 *
	 */
	@RequestMapping("/queryOfflineCard")
	@ResponseBody
	public Map<String, String> queryOfflineCard(String code,String openid,String nowtime) {
		String ordernum = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		//查询记录
		offlineCardService.insertQueryOfflineCardRecord(ordernum, code,openid);
		SendMsgUtil.send_0x22(code, 0, (short) 0, (byte) 2);
//		Map<String, String> hashMap = new HashMap<>();
//		hashMap.put("code", code);
//		Map<String, String> map = WolfHttpRequest.httpconnectwolf(hashMap, WolfHttpRequest.SEND_QUERYOFFLINECARD_URL);
		long nowTime = Long.parseLong(nowtime);
		boolean flag = true;
		int temp = 0;
		Map<String, String> map = new HashMap<>();
		while (flag) {
			map = Server.offlineMap.get(code);
			if (temp >= 10) {
				map = new HashMap<>();
				map.put("err", "1");
				map.put("errinfo", "连接超时...");
				flag = false;
				System.out.println("----查询超时已退出----");
				break;
			}
			if (map == null || map.size() == 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
				temp++;
				continue;
			} else {
				long nowtimes = Long.parseLong(map.get("nowtime"));
				if (nowtimes - nowTime > 0 && nowtimes - nowTime < 20000) {
					if (map.get("err") != null && "1".equals(map.get("err"))) {
						return map;
					}
					double card_surp = Double.parseDouble(map.get("card_surp")) / 10;
					int recycletype = Integer.parseInt(map.get("result"));
					offlineCardService.updateQueryOfflineCardRecord(ordernum, map.get("card_id"), card_surp,recycletype);
					flag = false;
					break;
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
					temp++;
					continue;
				}
			}
		}
		return map;
	}
	
	@RequestMapping("/chargeCard")
	@ResponseBody
	public Map<String, String> chargeCard(String code,String card_id,Short card_surp,Byte card_ope) {
		Map<String,String> map = new HashMap<>();
		map.put("code", code);
		map.put("card_id", card_id);
		map.put("card_surp", card_surp * 10 + "");
		map.put("card_ope", card_ope + "");
		return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_CHARGEOFFLINECARD_URL);
//		Map<String, String> map = new HashMap<>();
//		try {
//			int parseLong = (int) Long.parseLong(card_id,16);
//			SendMsgUtil.send_0x22(code, parseLong, (short) (card_surp * 10), card_ope);
//			boolean flag = true;
//			long nowTime = System.currentTimeMillis();
//			int temp = 0;
//			while (flag) {
//				map = Server.offlineMap.get(code);
//				if (temp >= 10) {
//					map = new HashMap<>();
//					map.put("err", "1");
//					map.put("errinfo", "连接超时...");
//					flag = false;
//					System.out.println("----查询超时已退出----");
//					break;
//				}
//				if (map == null || map.size() == 0) {
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						System.out.println(e.getMessage());
//					}
//					temp++;
//					continue;
//				} else {
//					long nowtimes = Long.parseLong(map.get("nowtime"));
//					if (nowtimes - nowTime > 0 && nowtimes - nowTime < 20000) {
//						flag = false;
//						break;
//					} else {
//						try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							System.out.println(e.getMessage());
//						}
//						temp++;
//						continue;
//					}
//				}
//			}
//			return map;
//		} catch (Exception e) {
//			map.put("err", "1");
//			map.put("errinfo", "充值异常");
//			return map;
//		}
	}
	
	@RequestMapping("/chargingByUid")
	public String chargingByUid() {
		return "record/chargingByUid";
	}
	
	@RequestMapping("/getuseragent")
	public void getuseragent() {
		String parameter = request.getHeader("user-agent");
		System.out.println(parameter);
	}
	
	@RequestMapping(value = "/lunbo")
	public String lunbo() {
		return "lunbo";
	}
	
	@RequestMapping(value = "/pcequlist")
	public String equlists(HttpServletRequest request, HttpServletResponse response,Model model){
		PageUtils<Equipment> pageBean = equipmentService.selectEquipment(request);
		List<Equipment> equipment = equipmentService.getEquipmentList();
		int online = 0;
		int binding = 0;
		for(Equipment eqm : equipment){
			if(eqm.getState()==1){//在线
				online += 1;
			}
			if(eqm.getBindtype()==1){//绑定
				binding += 1;
			}
		}
		model.addAttribute("online", online);
		model.addAttribute("disonline", equipment.size() - online);
		model.addAttribute("binding", binding);
		model.addAttribute("disbinding", equipment.size() - binding);
		model.addAttribute("totalRows", equipment.size());
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("state", request.getParameter("state"));
		model.addAttribute("code", request.getParameter("code"));
		model.addAttribute("imei", request.getParameter("imei"));
		model.addAttribute("ccid", request.getParameter("ccid"));
		model.addAttribute("hardversion", request.getParameter("hardversion"));
		model.addAttribute("softversionnum", request.getParameter("softversionnum"));
		model.addAttribute("csq", request.getParameter("csq"));
		model.addAttribute("phoneNum", request.getParameter("phoneNum"));
		model.addAttribute("line", request.getParameter("line"));
		model.addAttribute("equipmentList",pageBean.getList());
		return "computer/equlist";
	}
	
//	@RequestMapping(value = "/equlist")
//	public String equlist(@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,Model model) {
//		int pageSize = 10;
//		PageBean<Equipment> pageBean = equipmentService.findAllEquipmentPage(pageNum, pageSize);
//		model.addAttribute("pageBean", pageBean);
//		model.addAttribute("equipmentList", pageBean.getList());
//		return "equlist";
//	}

	/*
	 * @RequestMapping("/oauth2")
	 * @ResponseBody public void oauth2() throws Exception { String url =
	 * URLEncoder.encode("http://www.he360.com.cn", "utf-8"); String str =
	 * "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx3debe4a9c562c52a&"
	 * + "redirect_uri=" + url +
	 * "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect"; //
	 * request.getRequestDispatcher(str); response.sendRedirect(str); }
	 */

	@RequestMapping("/roarwolf")
	public String roarwolf(HttpServletRequest request) {
		return "home";
	}

	@RequestMapping(value = "/index")
	public String index(Model model) {
		model.addAttribute("name", "xiaobao");
		return "user/index";
	}

	@RequestMapping(value = "scancode")
	public Object scancode(Model model) {
		try {
			Map<String, String> map = JsSignUtil.sign("http://www.he360.com.cn/scancode/");
			// Map<String, Object> map = JsSignUtil.sign("");
			model.addAttribute("nonceStr", map.get("nonceStr"));
			model.addAttribute("timestamp", map.get("timestamp"));
			model.addAttribute("signature", map.get("signature"));
			model.addAttribute("jsapi_ticket", map.get("jsapi_ticket"));
			model.addAttribute("appId", map.get("appId"));
			model.addAttribute("url", map.get("url"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "scancode";
	}

	@RequestMapping(value = "/hello")
	public String hello(Map<String, Object> map) {
		map.put("test", "God is a gril");
		return "hello";
	}

	@RequestMapping(value = "/qrcode")
	public String qrcode(Model model) {
		model.addAttribute("qrcodeinfo", "It's me!!!");
		return "qrcode";
	}

	/**
	 * 判断是微信还是支付宝扫的二维码 ，随后进入相应的控制层
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/wolf")
	public String wolf(HttpServletRequest request) {
		System.out.println(request.getHeader("User-Agent"));
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.contains("MicroMessenger")) {
			return "agency/index";
		} else if (userAgent.contains("AlipayClient")) {
			return "redirect:/user/list";
		} else {
			return "index";
		}
	}

	@RequestMapping("/pctest")
	public String test(@RequestParam("code") String code, Model model) {
		model.addAttribute("code", code);
		Equipment equipment = equipmentService.getEquipmentById(code);
//		int neednum = EquipmentController.getAllportNeednum(equipment.getHardversion());
//		List<AllPortStatus> allPortStatusList = allPortStatusService.findPortStatusListByEquipmentnum(code,neednum);
		List<Map<String, String>> allPortStatusList = DisposeUtil.addPortStatus(code, equipment.getHardversion());
		System.out.println("设备---" + code + "共有" + allPortStatusList.size() + "路");
		model.addAttribute("allPortStatusList", allPortStatusList);
		model.addAttribute("equipment", equipment);
		UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
		if (userEquipment == null) {
			model.addAttribute("username", "0");
			return "computer/test";
		}
		User user = userService.selectUserById(userEquipment.getUserId());
		if (user != null) {
			model.addAttribute("username", user.getUsername());
		} else {
			model.addAttribute("username", "0");
		}
		return "computer/test";
	}

	@RequestMapping(value = "/getinfo")
	@ResponseBody
	public Map<String, String> getinfo(String code) {
		Map<String, Map> map = Server.getMap();
		Map map2 = map.get(code);
		return map2;
	}

	@RequestMapping(value = "/testmutual", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> testMutual(String param, Model model, String code) throws Exception {
//		Class<?> clazz = Class.forName("com.hedong.hedongwx.utils.SendMsgUtil");
//		SendMsgUtil sendMsgUtil = (SendMsgUtil) clazz.newInstance();
//		Method method = clazz.getMethod("send_" + param, String.class);
//		method.invoke(sendMsgUtil, code);
//		Map<String, Map> map = Server.getMap();
//		Map map2 = map.get(code);
		return null;
	}

	@RequestMapping(value = "/testpaytoport", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> testpaytoport(byte payport,short time,double elec,String code) {
		String elecstr = elec * 100 + "";
		String elecClac = elecstr.substring(0, elecstr.indexOf("."));
		Short elecs = Short.valueOf(elecstr.substring(0, elecstr.indexOf(".")));
		Map<String,String> map = new HashMap<>();
//		map.put("port", payport + "");
//		map.put("money", "10");
//		map.put("time", time + "");
//		map.put("elec", elecs + "");
//		map.put("code", code);
//		WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_PAY_URL);
		try {
			SendMsgUtil.send_0x14(payport,(short) 10,time,(short) (elecs * 100),code);
			map.put("wolfcode", "1000");
			map.put("wolfmsg", "success");
//			return WolfHttpRequest.sendChargePaydata(payport, time, "10", elecClac, code, 1);
		} catch (Exception e) {
			map.put("wolfcode", "1001");
			map.put("wolfmsg", "fail");
		}
		return map;
	}

	@RequestMapping(value = "/querystate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> querystate(byte port,String code) {
//		Map<String,String> hashMap = new HashMap<>();
//		hashMap.put("port", port + "");
//		hashMap.put("code", code);
//		try {
//			return WolfHttpRequest.httpconnectwolf(hashMap, WolfHttpRequest.SEND_QUERYPORTCHARGESTATUS_URL);
//		} catch (Exception e) {
//			HashMap<String,String> map = new HashMap<>();
//			map.put("wolfcode", "1001");
//			map.put("wolfmsg", "fail");
//			return map;
//		}
		long currentTime = System.currentTimeMillis();
//		SendMsgUtil.send_21(port,code);
		if (port == 2) {
			port = 0;
		}
		SendMsgUtil.send_0x2E(code, port);
		boolean flag = true;
		int temp = 0;
		while (flag) {
			try {
				if (temp > 20) {
					Map<String, String> map = new HashMap<>();
					map.put("wolfcode", "1001");
					map.put("wolfmsg", "网络连接失败，稍后请重试...");
					return map;
				}
				Map map = Server.recycleMap.get(code);
				if (map == null) {
					temp++;
					Thread.sleep(1000);
					continue;
				}
				Map portmap = (Map) map.get("port" + port);
				if (portmap == null) {
					temp++;
					Thread.sleep(1000);
					continue;
				}
				String updatetime = (String) portmap.get("updatetime");
				long updatetimelong = Long.parseLong(updatetime);
				System.out.println("指令回复时间---" + updatetimelong + "发起时间---" + currentTime);
				if (updatetime == null || "".equals(updatetime)) {
					temp++;
					Thread.sleep(1000);
					continue;
				} else if ((updatetimelong - currentTime) > 0 && (updatetimelong - currentTime) < 20000) {
					flag = false;
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
		Map<String,Map<String,String>> map = Server.recycleMap.get(code);
		Map<String, String> portmap = map.get("port" + port);
		return portmap;
	}

	@RequestMapping(value = "/seticandcoin", method = RequestMethod.POST)
	@ResponseBody
	public String seticandcoin(byte setcoin, byte setic, String code) {
		SendMsgUtil.send_9(setcoin, setic, code);
		return "设置成功";
	}

	@RequestMapping(value = "/lock", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> lock(byte port, byte status,String code) {
//		long currentTime = System.currentTimeMillis();
		AllPortStatus allPortStatus = allPortStatusService.findPortStatusByEquipmentnumAndPort(code, (int) port);
		Byte portStatus = allPortStatus.getPortStatus();
		Map<String, String> hashMap = new HashMap<>();
		if (portStatus != 1 && portStatus != 2) {
			if (status == 0) {
				hashMap.put("err", "1");
				hashMap.put("errinfo", "当前端口已锁定或有故障，不可再次锁定");
				return hashMap;
			}
		} else if (portStatus == 1 || portStatus == 2) {
			if (status == 1) {
				hashMap.put("err", "1");
				hashMap.put("errinfo", "当前端口正常使用，解锁失效");
				return hashMap;
			}
		}
//		SendMsgUtil.send_12(port, status, code);
		hashMap.put("code", code);
		hashMap.put("port", port + "");
		hashMap.put("status", status + "");
		Map<String, String> map = WolfHttpRequest.httpconnectwolf(hashMap, WolfHttpRequest.SEND_SETPORTSTATUS_URL);
//		boolean flag = true;
//		int temp = 0;
//		while (flag) {
//			try {
//				if (temp > 20) {
//					Map<String, String> map = new HashMap<>();
//					map.put("err", "0");
//					map.put("errinfo", "网络连接错误，稍后请重试...");
//					return map;
//				}
//				Map<String, Map<String,String>> map = Server.islockMap.get(code);
//				if (map == null) {
//					temp++;
//					Thread.sleep(1000);
//					continue;
//				}
//				Map<String,String> portmap = map.get("port" + port);
//				if (portmap == null) {
//					temp++;
//					Thread.sleep(1000);
//					continue;
//				}
//				String updatetime = portmap.get("updatetime");
//				long updatetimelong = Long.parseLong(updatetime);
//				if (updatetime == null || "".equals(updatetime)) {
//					temp++;
//					Thread.sleep(1000);
//					continue;
//				} else if ((updatetimelong - currentTime) > 0 && (updatetimelong - currentTime) < 20000) {
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
		if (status == 0) {
			allPortStatus.setPortStatus((byte) 3);
			allPortStatus.setElec((short) 0);
			allPortStatus.setPower((short) 0);
			allPortStatus.setTime((short) 0);
		} else if (status == 1) {
			allPortStatus.setPortStatus((byte) 1);
		}
		allPortStatusService.updateAllPortStatus(allPortStatus);
		return map;
	}

	/*@RequestMapping(value = "/lock2", method = RequestMethod.POST)
	@ResponseBody
	public String lock2(byte port, byte status) {
		SendMsgUtil.send_12(port, status);
		if (SendMsgUtil.LOCK) {
			return SendMsgUtil.getTwolock();
		} else {
			return "";
		}
	}*/

	@RequestMapping(value = "/stopRechargeByPort", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> stopRechargeByPort(byte port, String code) {
		Map<String,String> map = new HashMap<>();
		map.put("port", port + "");
		map.put("code", code);
		Map<String,String> resultmap = new HashMap<>();
		try {
			SendMsgUtil.send_13(port, code);
			resultmap.put("wolfcode", "1000");
			resultmap.put("wolfmsg", "success");
//			return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_STOPPORTLOAD_URL);
		} catch (Exception e) {
			resultmap.put("wolfcode", "1001");
			resultmap.put("wolfmsg", "fail");
		}
		return resultmap;
	}

	@RequestMapping(value = "/setsystem", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> setsystem(Short coin_min, Short card_min, Byte coin_elec, Byte card_elec, Byte cst, Short power_max_1,
			Short power_max_2, Short power_max_3, Short power_max_4, Byte power_2_tim, Byte power_3_tim,
			Byte power_4_tim, Byte sp_rec_mon, Byte sp_full_empty, Byte full_power_min, Byte full_charge_time,
			String elec_time_first, String code) {
		if (coin_min == null) {
			coin_min = 0;
		}
		if (card_min == null) {
			card_min = 0;
		}
		if (coin_elec == null) {
			coin_elec = 0;
		}
		if (card_elec == null) {
			card_elec = 0;
		}
		if (cst == null) {
			cst = 0;
		}
		if (power_max_1 == null) {
			power_max_1 = 0;
		}
		if (power_max_2 == null) {
			power_max_2 = 0;
		}
		if (power_max_3 == null) {
			power_max_3 = 0;
		}
		if (power_max_4 == null) {
			power_max_4 = 0;
		}
		if (power_2_tim == null) {
			power_2_tim = 0;
		}
		if (power_3_tim == null) {
			power_3_tim = 0;
		}
		if (power_4_tim == null) {
			power_4_tim = 0;
		}
		if (sp_rec_mon == null) {
			sp_rec_mon = 0;
		}
		if (sp_full_empty == null) {
			sp_full_empty = 0;
		}
		if (full_power_min == null) {
			full_power_min = 0;
		}
		if (full_charge_time == null) {
			sp_full_empty = 0;
		}
		System.out.println("elec_time_first===" + elec_time_first);
		Byte elec_time_firsts = (byte) 0xff;
		if (elec_time_first == null || elec_time_first.indexOf('-') != -1) {
			elec_time_firsts = (byte) 0xff;
		} else if ("1".equals(elec_time_first) || "0".equals(elec_time_first)) {
			elec_time_firsts = Byte.parseByte(elec_time_first);
		}
		SendMsgUtil.send_24(coin_min, card_min, coin_elec, card_elec, cst, power_max_1, power_max_2, power_max_3,
				power_max_4, power_2_tim, power_3_tim, power_4_tim, sp_rec_mon, sp_full_empty, full_power_min,
				full_charge_time, elec_time_firsts, code);
		long currentTime = System.currentTimeMillis();
		boolean flag = true;
		int temp = 0;
		Map<String, String> map = new HashMap<>();
		while (flag) {
			if (temp >=20) {
				map.put("status", "0");
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
	
	@RequestMapping(value = "/readsysteminfo")
	@ResponseBody
	public Map<String, String> readsysteminfo(String code) {
		Map<String, String> hashMap = new HashMap<>();
		hashMap.put("code", code);
		Map<String, String> map = WolfHttpRequest.httpconnectwolf(hashMap, WolfHttpRequest.SEND_READSYSTEM_URL);
//		SendMsgUtil.send_30(code);
//		long currentTime = System.currentTimeMillis();
//		boolean flag = true;
//		int temp = 0;
//		Map<String, String> map = new HashMap<>();
//		while (flag) {
//			if (temp >=20) {
//				map.put("status", "0");
//				break;
//			}
//			Map<String, String> mapRevice = Server.readsystemMap.get(code);
//			if (mapRevice == null || mapRevice.size() < 1) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				temp++;
//				continue;
//			}
//			long udpatetime = Long.parseLong(mapRevice.get("updatetime"));
//			if ((udpatetime - currentTime) > 0 && (udpatetime - currentTime) < 20000) {
//				map.putAll(mapRevice);
//				map.put("readtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//				flag = false;
//				Server.readsystemMap.remove(code);
//				break;
//			} else {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				temp++;
//				continue;
//			}
//			
//		}
		System.out.println("===" + JSON.toJSONString(map));
		return map;
	}
	
	@RequestMapping(value = "/portstate")
	@ResponseBody
	public Map<String,String> portstate(String code) {
		Map<String, Map<String,String>> everyportMap = Server.everyportMap;
		if (everyportMap != null) {
			Map<String,String> map = everyportMap.get(code);
			if (map != null) {
				int temp = 0;
				Set<Entry<String,String>> entrySet = map.entrySet();
				for (Entry<String, String> entry : entrySet) {
					temp++;
				}
				if ((temp - 1) == 2) {
					Equipment equipment = equipmentService.getEquipmentById(code);
					if (!"00".equals(equipment.getHardversion()) && equipment.getTempid() == 0) {
						Equipment equipment2 = new Equipment();
						equipment2.setCode(code);
						equipment2.setTempid(1);
						equipmentService.updateEquipment(equipment2);
					}
				}
				return map;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	@RequestMapping(value = "/portstate1")
	@ResponseBody
	public Map<String,String> portstate1(String code) {
		boolean flag = true;
		int temp = 0;
		String nowtime = request.getParameter("nowtime");
//		Map<String,String> map = new HashMap<>();
//		map.put("code", code);
//		map.put("nowtime", nowtime);
//		Map<String, String> hashMap = WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_QUERYALLPORTSTATUS_URL);
		SendMsgUtil.send_15(code);
		System.out.println("当前时间：" + nowtime);
		int i = 0;
		while (flag) {
			if (temp >= 10) {
				System.out.println("等待回复超时");
				HashMap<String,String> hashMap = new HashMap<>();
				hashMap.put("state", "error");
				return hashMap;
			}
			Map<String, Map<String,String>> everyportMap = Server.everyportMap;
			Map<String,String> map = everyportMap.get(code);
			if (map == null || map.size() < 1) {
				if (i >= 10) {
					HashMap<String,String> hashMap = new HashMap<>();
					hashMap.put("state", "error");
					return hashMap;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
				continue;
			}
			if (map == null || map.size() < 1) {
				HashMap<String,String> hashMap = new HashMap<>();
				hashMap.put("state", "error");
				return hashMap;
			}
			if (nowtime == null || "".equals(nowtime)) {
				HashMap<String,String> hashMap = new HashMap<>();
				hashMap.put("state", "error");
				return hashMap;
			} else {
				String replytime = (String) map.get("paramdate");
				long nowtimeparseLong = Long.parseLong(nowtime);
				long replytimeparseLong = Long.parseLong(replytime);
				if ((replytimeparseLong - nowtimeparseLong) < 0) {
					temp++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				} else if ((replytimeparseLong - nowtimeparseLong) > 0 && (replytimeparseLong - nowtimeparseLong) < 10000) {
					Map<String, Map<String,String>> everyportMap1 = Server.everyportMap;
					Map<String,String> map1 = everyportMap1.get(code);
					return map1;
				} else if ((replytimeparseLong - nowtimeparseLong) < 0 && (replytimeparseLong - nowtimeparseLong) > -2000) {
					Map<String, Map<String,String>> everyportMap1 = Server.everyportMap;
					Map<String,String> map1 = everyportMap1.get(code);
					return map1;
				}
			}
		}
		HashMap<String,String> hashMap = new HashMap<>();
		hashMap.put("state", "error");
		return hashMap;
	}
	
	@RequestMapping(value = "/portstate2")
	@ResponseBody
	public Map<String,String> portstate2(String code,Integer port) {
		boolean flag = true;
		int temp = 0;
		String nowtime = request.getParameter("nowtime");
		System.out.println("当前时间：" + nowtime);
//		Map<String,String> map = new HashMap<>();
//		map.put("code", code);
//		map.put("nowtime", nowtime);
//		map.put("port", port + "");
//		Map<String, String> hashMap = WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_QUERYPORTSTATUS_URL);
		SendMsgUtil.send_15(code);
		int i = 0;
		while (flag) {
			if (temp >= 10) {
				System.out.println("等待回复超时");
				HashMap<String,String> hashMap = new HashMap<>();
				hashMap.put("state", "error");
				return hashMap;
			}
			Map<String, Map<String,String>> everyportMap = Server.everyportMap;
			Map<String,String> map = everyportMap.get(code);
			if (map == null || map.size() < 1) {
				if (i >= 10) {
					HashMap<String,String> hashMap = new HashMap<>();
					hashMap.put("state", "error");
					return hashMap;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
				continue;
			}
			if (map == null || map.size() < 1) {
				HashMap<String,String> hashMap = new HashMap<>();
				hashMap.put("state", "error");
				return hashMap;
			}
			if (nowtime == null || "".equals(nowtime)) {
				HashMap<String,String> hashMap = new HashMap<>();
				hashMap.put("state", "error");
				return hashMap;
			} else {
				String replytime = (String) map.get("paramdate");
				long nowtimeparseLong = Long.parseLong(nowtime);
				long replytimeparseLong = Long.parseLong(replytime);
				if ((replytimeparseLong - nowtimeparseLong) < 0) {
					temp++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				} else if ((replytimeparseLong - nowtimeparseLong) > 0 && (replytimeparseLong - nowtimeparseLong) < 10000) {
					Map<String, Map<String,String>> everyportMap1 = Server.everyportMap;
					Map<String,String> map1 = everyportMap1.get(code);
					String portstatus = map1.get("param" + port);
					map1.put("portstatus", portstatus);
					return map1;
				} else if ((replytimeparseLong - nowtimeparseLong) < 0 && (replytimeparseLong - nowtimeparseLong) > -2000) {
					Map<String, Map<String,String>> everyportMap1 = Server.everyportMap;
					Map<String,String> map1 = everyportMap1.get(code);
					String portstatus = map1.get("param" + port);
					map1.put("portstatus", portstatus);
					return map1;
				}
			}
		}
		HashMap<String,String> hashMap = new HashMap<>();
		hashMap.put("state", "error");
		return hashMap;
	}
	
	@RequestMapping(value = "/readeveryport")
	@ResponseBody
	public Map<String, String> readeveryport(String code) {
		Map<String, Map> isfreeMap = Server.isfreeMap;
		Map map = isfreeMap.get(code);
		return map;
	}

//	@RequestMapping(value = "/stateall")
//	@ResponseBody
//	public Map<String, String> stateall(String code) {
//		Map<String, List<String>> stateMap = Server.stateMap;
//		Map<String, String> map = new HashMap<>();
//		List<String> list2 = stateMap.get(code);
//		if (list2 != null) {
//			int temp = 1;
//			for (String string : list2) {
//				map.put("param" + temp, string);
//				temp++;
//			}
//		}
//		return map;
//	}

	@RequestMapping(value = "/modelinfo")
	@ResponseBody
	public Map<String, List<String>> modelinfo(String code) {
		Map<String, Map> infoMap = Server.infoMap;
		Map map = infoMap.get(code);
		return map;
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
	
//	ORIGIN
//	/**
//	 * @Description： 收入信息处理
//	 * @author： origin 创建时间：   2019年9月15日 上午11:44:49
//	 */
//	public static Map<String, Object> partnerIncomeDispose(List<Map<String, Object>> partInfo, Integer merid, Double money, String ordernum, 
//			Date time, Integer paysource, Integer paytype, Integer paystatus) {
//		Double mermoney = 0.00;//商户金额
//		Double manmoney = 0.00;//商户金额
//		Double tolpercent = 0.00;//分成比
//		Map<String, Object> mapresult = new HashMap<>();
//		List<Map<String, Object>> merchlist = new ArrayList<>();
//		try {
//			if(partInfo.size()>0){//分成
//				System.out.println("输出分成");
//				for(Map<String, Object> item : partInfo){
//					Map<String, Object> map = new HashMap<>();
//					Integer partid = CommUtil.toInteger(item.get("partid"));
//					Double percent = CommUtil.toDouble(item.get("percent"));
//					Double partmoney = CommUtil.toDouble((money * (percent*100))/100);
//					map.put("partid", partid);
//					map.put("percent", percent);
//					map.put("money", partmoney);
//					merchlist.add(map);
//					tolpercent = tolpercent + percent;
//					merEearningCalculate( partid, partmoney, 3, ordernum, time, paysource, paytype, paystatus);
//				}
//			}
//			mermoney = CommUtil.toDouble((money *100 * (1- tolpercent))/100);
//			manmoney = CommUtil.subBig(money, mermoney);
//			logger.info("输出商户收益mermoney "+mermoney+"    ****   "+manmoney);
//			Map<String, Object> map = new HashMap<>();
//			map.put("merid", merid);
//			map.put("percent", (1-tolpercent));
//			map.put("money", mermoney);
//			merchlist.add(map);
//			merEearningCalculate( merid, mermoney, 3, ordernum, time, paysource, paytype, paystatus);
//			mapresult.put("partmoney", manmoney);
//			mapresult.put("json", JSON.toJSON(merchlist));
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.info(CommUtil.getExceptInfo(e));
//		}
//		return mapresult;
//	}
	
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
	
}
