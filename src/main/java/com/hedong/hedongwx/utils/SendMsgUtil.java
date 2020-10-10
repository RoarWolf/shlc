package com.hedong.hedongwx.utils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.thread.Server;

@Service
public class SendMsgUtil {
	
	private static final String DOMAIN = "http://ck.taifengkeji.com/";
	
	/** 预约map*/
	public static ConcurrentHashMap<String, Object> yuyueMap = new ConcurrentHashMap<>(); 
	/** 启动充电map*/
	public static ConcurrentHashMap<String, Object> chargeMap = new ConcurrentHashMap<>(); 
	/** 停止充电map*/
	public static ConcurrentHashMap<String, Object> stopChargeMap = new ConcurrentHashMap<>(); 
	/** 设置服务器IP地址map*/
	public static ConcurrentHashMap<String, Object> serverIpAddrMap = new ConcurrentHashMap<>(); 
	
	private static Logger logger = LoggerFactory.getLogger(SendMsgUtil.class);
	private static final short framestart = 0x4a58;//帧起始
	
	public static void main(String[] args) throws Exception {
//		Map<String, String> billMap = new HashMap<>();
//		byte timenum = 3;//时段数
//		billMap.put("billver", 1 + "");//计费模型版本
//		billMap.put("parkingfee", 0.0 + "");//停车费费率
//		List<Object> list = new ArrayList<>();
//		for (int i = 1; i <= timenum; i++) {
//			Map<String,Object> timeiMap = new HashMap<>();
//			timeiMap.put("hour", i);//时
//			timeiMap.put("minute", 0);//分
//			timeiMap.put("type", 1);//类型
//			timeiMap.put("chargefee", 1.0);//电能费
//			timeiMap.put("serverfee", 0.02);//服务费
//			list.add(timeiMap);
//		}
//		billMap.put("timeInfo", JSON.toJSONString(list));
//		billMap.put("timenum", list.size() + "");
//		JedisUtils.hmset("billingInfo", billMap);
//		Map<String, String> billingParam = JedisUtils.hgetAll("billingInfo");
//		int timenum = Integer.parseInt(billingParam.get("timenum"));
//		if (timenum > 0) {
//			String timeInfoStr = billingParam.get("timeInfo");
//			List<Map<String, Object>> timeInfo = (List<Map<String, Object>>) JSON.parse(timeInfoStr);
//			for (Map<String, Object> map : timeInfo) {
//				System.out.println(JSON.toJSONString(map));
//			}
//		}
//		String str = "沪";
//		byte[] bytes = str.getBytes("GBK");
//		for (byte b : bytes) {
//			System.out.printf("0x%02x ", b);
//		}
	}
	
	public static Map<String, Object> backCahrgeInfo(String ordernum) {
		long nowtime = System.currentTimeMillis();
		int temp = 0;
		boolean flag = true;
		Map<String, Object> chargeback = (Map<String, Object>) chargeMap.get(ordernum);
		while (flag) {
			if (temp >= 15) {
				return CommUtil.responseBuildInfo(1001, "连接超时", null);
			}
			if (chargeback == null || chargeback.size() == 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
				temp++;
				continue;
			} else {
				long updatetime = (long) chargeback.get("updatetime");
				if (nowtime - updatetime > -5000 && nowtime - updatetime < 15000) {
					return CommUtil.responseBuildInfo(1000, "连接成功", chargeback);
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
		return CommUtil.responseBuildInfo(1002, "系统异常", null);
	}
	
	public static byte clacSumVal(byte[] bytes) {
		byte sum = 0x00;
		for (int i = 0; i < bytes.length - 1; i++) {
			sum ^= bytes[i];
		}
		return sum;
	}
	
	/**
	 * 桩请求连接
	 * @param devicenum 设备编号
	 * @param channel 连接
	 * @param buffer 设备发送的数据包
	 * @param encryptionWay
	 * @param datalen
	 */
	public static void parse_0x01(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		short keyver = buffer.getShort();//密钥版本
		byte[] bytes = new byte[8];//校验密文包
		buffer.get(bytes);
		byte sum = buffer.get();//cmd-data最后一个字节的异或值
		logger.info(devicenum + "桩请求连接");
		String areaAndAddress = devicenum.substring(6);
		if (Integer.parseInt(areaAndAddress) == 0) {
			String string = JedisUtils.get("devicenumindex");
			String create_devicenum = DisposeUtil.completeNum(Integer.parseInt(string) + 1 + "", 10);
			JedisUtils.set("devicenumindex", create_devicenum, 0);
			devicenum = devicenum.substring(0, 6) + create_devicenum;
		}
		byte portnum = Byte.parseByte(devicenum.substring(4, 6));
		if (portnum > 50 && portnum <= 81) {
			portnum = (byte) (portnum - 50);
		}
		send_0x02(devicenum, (byte) 1, (byte) 0, portnum);
	}
	
	/**
	 * 请求连接结果
	 */
	public static void send_0x02(String devicenum, byte reqResult, byte resultInfo, byte portnum) {
		ByteBuffer buffer = ByteBuffer.allocate(65522);
		String str = DOMAIN + "scanDevice?devicenum=";//二维码固定段
		buffer.putShort(framestart);
		buffer.put((byte) 0x02);//cmd
		buffer.put(AESUtil.String_BCD(devicenum));
		buffer.put((byte) 0x01);//数据加密方式
		short datalen = (short) (108 + portnum * 20);
		buffer.put(DisposeUtil.converIntData(datalen, 2));
		buffer.put(DisposeUtil.getDateFlag(0,0));
		buffer.put((byte) 0x01);//0x01-允许；0x02-不允许
		buffer.put((byte) 0x00);
		buffer.put(DisposeUtil.complateBytes(str.getBytes(), 100));
		buffer.put(portnum);
		for (int i = 1; i <= portnum; i++) {
			String devicenumAndPort = "";
			if (i < 10) {
				devicenumAndPort = devicenum + "0" + i;
			} else {
				devicenumAndPort = devicenum + i;
			}
			buffer.put(DisposeUtil.complateBytes(devicenumAndPort.getBytes(), 20));
		}
		buffer.position(1);
		byte[] bytes = new byte[datalen & 0xffff + 12];
		buffer.get(bytes);
		buffer.put(clacSumVal(bytes));
		buffer.flip();
		Server.sendMsg(devicenum, buffer);
	}
	
	/**
	 * 登录信息
	 */
	public void parse_0x03(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		byte[] deviceVer = new byte[16];
		buffer.get(deviceVer);
		String deviceVerStr = new String(deviceVer);//桩型号
		byte[] hardver = new byte[2];//硬件版本
		buffer.get(hardver);
		String hardverStr = Double.parseDouble(AESUtil.BCD_String(hardver))/100 + "";
		byte[] softver = new byte[2];//软件版本
		buffer.get(softver);
		String softverStr = Double.parseDouble(AESUtil.BCD_String(softver))/100 + "";
		byte[] subHardver = new byte[2];//次级单元硬件版本
		buffer.get(subHardver);
		String subHardverStr = Double.parseDouble(AESUtil.BCD_String(subHardver))/100 + "";
		byte[] subSoftver = new byte[2];//次级单元软件版本
		buffer.get(subSoftver);
		String subSoftverStr = Double.parseDouble(AESUtil.BCD_String(subSoftver))/100 + "";
		byte DCType = buffer.get();//直流模块类型
		String DCTypeStr = DisposeUtil.intToHex(DCType & 0xff);
		byte DCTypeNum = buffer.get();//直流模块总数
		String DCTypeNumStr = DisposeUtil.intToHex(DCTypeNum & 0xff);
		int DCOnePower = buffer.get() & 0xff;//直流模块总数直流模块单模块功率
		int chargingModelVer = buffer.getShort() & 0xffff;//计费模型版本
		int beginNum = buffer.getShort() & 0xffff;//启动次数
		byte[] dateBytes = new byte[6];
		buffer.get(dateBytes);
		String beginTime = DisposeUtil.disposeDate(dateBytes);//开机时间
		int heartbeatCycle = buffer.getShort() & 0xffff;//心跳周期
		int heartbeatAfterNum = buffer.get() & 0xff;//心跳超时次数
		int yaoxinCycle = buffer.getShort() & 0xffff;//遥信周期
		int yaoceCycle = buffer.getShort() & 0xffff;//遥测周期
		int workInfoCycle = buffer.getShort() & 0xffff;//工作信息周期
		int BMSInfoCycle = buffer.getShort() & 0xffff;//BMS 信息周期
		int BMVCycle = buffer.getShort() & 0xffff;//BMV 周期
		int BMTCycle = buffer.getShort() & 0xffff;//BMT 周期
		int lat1 = buffer.get() & 0xff;//纬度（度）
		byte[] latBytes = new byte[3];
		buffer.get(latBytes);
		double lat = lat1 + DisposeUtil.bytes2Double(latBytes)/10000;
		int lon1 = buffer.get() & 0xff;//经度（度）
		byte[] lonBytes = new byte[3];
		buffer.get(latBytes);
		double lon = lon1 + DisposeUtil.bytes2Double(lonBytes)/10000;
		byte sum = buffer.get();//校验码
	}
	
	/**
	 * 桩请求对时命令
	 */
	public static void parse_0x05(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		send_0x06(devicenum);
	}
	
	/**
	 * 平台下发对时命令
	 */
	public static void send_0x06(String devicenum) {
		ByteBuffer buffer = ByteBuffer.allocate(65522);
		buffer.putShort(framestart);
		buffer.put((byte) 0x06);//cmd
		buffer.put(AESUtil.String_BCD(devicenum));
		buffer.put((byte) 0x01);//数据加密方式
		short datalen = 18;
		buffer.put(DisposeUtil.converIntData(datalen, 2));
		byte[] dateFlag = DisposeUtil.getDateFlag(0,0);
		buffer.put(dateFlag);
		byte[] sendTime = new byte[12];
		buffer.put(DisposeUtil.complateBytes(dateFlag, sendTime.length));
		buffer.position(1);
		byte[] bytes = new byte[datalen & 0xffff + 12];
		buffer.get(bytes);
		buffer.put(clacSumVal(bytes));
		buffer.flip();
		Server.sendMsg(devicenum, buffer);
	}
	
	/**
	 * 桩回复对时命令
	 */
	public static void parse_0x07(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		byte result_info = buffer.get();//1-成功；2-失败
		byte fail_info = buffer.get();//0-无；1-数据格式异常
		byte sum = buffer.get();
	}
	
	/**
	 * 平台下发总召命令
	 */
	public static void send_0x08() {
		
	}
	
	/**
	 * 桩遥信
	 */
	public static void parse_0x09(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
	}
	
	/**
	 * 桩遥测
	 */
	public static void parse_0x0A(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		double UaVal = ((buffer.getShort() & 0xffff) + 0.0)/10;//A相电压
		double UbVal = ((buffer.getShort() & 0xffff) + 0.0)/10;//B相电压
		double UcVal = ((buffer.getShort() & 0xffff) + 0.0)/10;//C相电压
		double AaVal = ((buffer.getShort() & 0xffff) + 0.0)/100;//A相电流
		double AbVal = ((buffer.getShort() & 0xffff) + 0.0)/100;//B相电流
		double AcVal = ((buffer.getShort() & 0xffff) + 0.0)/100;//C相电流
		double totalElec = (buffer.getInt() + 0.0)/100;//总电表电量
		int deviceT = buffer.get() & 0xff;//桩内温度
		int deviceInT = buffer.get() & 0xff;//进风口温度
		int deviceOutT = buffer.get() & 0xff;//出风口温度
		int ctrlT = buffer.get() & 0xff;//控制板温度
		int humidity = buffer.get() & 0xff;//桩内湿度
		long b = buffer.getLong();//预留
		byte portnum = buffer.get();//充电枪数量
		if (portnum <= 30 && portnum > 0) {
			double U = ((buffer.getShort() & 0xffff) + 0.0)/10;//电表电压
			double A = ((buffer.getShort() & 0xffff) + 0.0)/100;//电表电流
			double elec = (buffer.getInt() + 0.0)/100;//电表电量
			double chargeU = ((buffer.getShort() & 0xffff) + 0.0)/10;//充电模块电压
			double chargeA = ((buffer.getShort() & 0xffff) + 0.0)/100;//充电模块电流
			int chargeModelT = buffer.get() & 0xff;//充电模块温度
			int chargePortT = buffer.get() & 0xff;//充电枪温度
			int yuliu = buffer.getInt();//预留
		}
		byte sum = buffer.get();
		
	}
	
	/**
	 * 平台心跳
	 */
	public static void send_0x0B(String devicenum) {
		ByteBuffer buffer = ByteBuffer.allocate(65522);
		buffer.putShort(framestart);
		buffer.put((byte) 0x0B);//cmd
		buffer.put(AESUtil.String_BCD(devicenum));
		buffer.put((byte) 0x01);//数据加密方式
		short datalen = 0x08;
		buffer.put(DisposeUtil.converIntData(datalen, 2));
		buffer.put(DisposeUtil.getDateFlag(0,0));
		buffer.put((byte) 0x00);//桩心跳超时次数
		buffer.position(1);
		byte[] bytes = new byte[21];
		buffer.get(bytes);
		buffer.put(clacSumVal(bytes));
		buffer.flip();
		Server.sendMsg(devicenum, buffer);
	}
	
	/**
	 * 桩心跳
	 */
	public static void parse_0x0C(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		byte timeoutnum = buffer.get();//平台心跳超时次数
		byte portnum = buffer.get();//充电枪总数
		if (portnum > 0 && portnum <= 30) {
			byte portStatus = buffer.get();//充电枪状态
			byte workWay = buffer.get();//工作模式
		}
		byte sum = buffer.get();
	}
	
	/**
	 * 平台下发预约命令(即查询充电枪状态)
	 */
	public static Map<String, Object> send_0x1B(String devicenum, byte port, String userid, short userType, String phonenum) {
		ByteBuffer buffer = ByteBuffer.allocate(65522);
		buffer.putShort(framestart);
		buffer.put((byte) 0x1B);//cmd+
		buffer.put(AESUtil.String_BCD(devicenum));
		buffer.put((byte) 0x01);//数据加密方式
		short datalen = 58;
		buffer.put(DisposeUtil.converIntData(datalen, 2));
		buffer.put(DisposeUtil.getDateFlag(0,0));//6字节
		buffer.put(port);//1字节
		buffer.put(DisposeUtil.complateBytes(userid.getBytes(), 32));//32字节
		buffer.put(DisposeUtil.converIntData(userType,2));//2字节
		buffer.put(DisposeUtil.getDateFlag(6, 15));//6字节
		buffer.put(DisposeUtil.convertPhonenum(phonenum).getBytes());//11字节
		buffer.position(1);
		byte[] bytes = new byte[datalen & 0xffff + 12];
		buffer.get(bytes);
		buffer.put(clacSumVal(bytes));
		buffer.flip();
		Server.sendMsg(devicenum, buffer);
		long nowtime = System.currentTimeMillis();
		int temp = 0;
		boolean flag = true;
		Map<String, Object> yuyueback = (Map<String, Object>) yuyueMap.get(devicenum + port);
		while (flag) {
			if (temp >= 15) {
				return CommUtil.responseBuildInfo(1001, "连接超时", null);
			}
			if (yuyueback == null || yuyueback.size() == 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
				temp++;
				continue;
			} else {
				long updatetime = (long) yuyueback.get("updatetime");
				if (nowtime - updatetime > 0 && nowtime - updatetime < 20000) {
					return CommUtil.responseBuildInfo(1000, "连接成功", yuyueback);
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
		return CommUtil.responseBuildInfo(1002, "系统异常", null);
	}
	
	/**
	 * 桩回复预约命令
	 */
	public static void parse_0x1C(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		byte port = (byte) (buffer.get() + 1);//枪号0-29
		byte[] useridBs = new byte[32];
		buffer.get(useridBs);
		String userid = new String(useridBs);//用户id
		short userType = buffer.getShort();//用户类型
		byte[] dateBytes = new byte[6];
		buffer.get(dateBytes);
		String deviceDataOutTime = DisposeUtil.disposeDate(dateBytes);
		byte result = buffer.get();//预约结果 1-成功；2-失败
		byte reason = buffer.get();//预约失败原因 1- 设备故障 	2- 充电枪使用中 3- 已被预约 4- 不允许预约 5- 预约超时点异常 6- 其他原因
		byte sum = buffer.get();//校验值
		Map<String,Object> yuyueback = new HashMap<>();
		yuyueback.put("device_result", result);
		yuyueback.put("device_reason", reason);
		yuyueback.put("updatetime", System.currentTimeMillis());
		yuyueMap.put(devicenum + port, yuyueback);
	}
	
	/**
	 * 平台取消预约命令
	 */
	public static void send_0x1D(String devicenum, byte port) {
		ByteBuffer buffer = ByteBuffer.allocate(65522);
		buffer.putShort(framestart);
		buffer.put((byte) 0x1D);//cmd+
		buffer.put(AESUtil.String_BCD(devicenum));
		buffer.put((byte) 0x01);//数据加密方式
		short datalen = 0x07;
		buffer.put(DisposeUtil.converIntData(datalen, 2));
		buffer.put(DisposeUtil.getDateFlag(0,0));//6字节
		buffer.put((byte) (port - 1));//1字节
		buffer.position(1);
		byte[] bytes = new byte[datalen & 0xffff + 12];
		buffer.get(bytes);
		buffer.put(clacSumVal(bytes));
		buffer.flip();
		Server.sendMsg(devicenum, buffer);
	}
	
	/**
	 * 桩回复取消预约
	 */
	public static void parse_0x1E(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		byte port = buffer.get();//枪号
		byte sum = buffer.get();
	}
	
	/**
	 * 平台下发充电命令
	 */
	public static void send_0x1F(String devicenum, byte port, String ordernum, String userid, short userType,
			String groupCode, byte ctrlWay, int ctrlParam, byte chargeWay, byte startWay, String userOperCode,
			byte billingWay, Map<String,String> billingParam) {
		ByteBuffer buffer = ByteBuffer.allocate(65522);
		buffer.putShort(framestart);
		buffer.put((byte) 0x1F);//cmd+
		buffer.put(AESUtil.String_BCD(devicenum));
		buffer.put((byte) 0x01);//数据加密方式
		short datalen = 102;
		if (billingWay == 2) {
			datalen = (short) (datalen + Integer.parseInt(billingParam.get("billnum")) * 11 + 7);
		}
		buffer.put(DisposeUtil.converIntData(datalen, 2));
		buffer.put(DisposeUtil.getDateFlag(0,0));//6字节
		buffer.put((byte) (port - 1));//1字节
		buffer.put(ordernum.getBytes());//32字节
		buffer.put(userid.getBytes());//32字节
		buffer.put(DisposeUtil.converIntData(userType, 2));
		buffer.put(DisposeUtil.completeNum(groupCode, 9).getBytes());//组织机构代码
		buffer.put(ctrlWay);//控制模式
		buffer.put(DisposeUtil.converIntData(ctrlParam, 4));//控制参数
		buffer.put(chargeWay);//充电模式
		buffer.put(startWay);//启动方式
		buffer.put(DisposeUtil.getDateFlag(0, 0));//定时启动时间
		buffer.put(DisposeUtil.completeNum(userOperCode, 6).getBytes());
		buffer.put(billingWay);//计费模型选择
		if (billingWay == 2) {
			buffer.put(DisposeUtil.converIntData(Integer.parseInt(billingParam.get("billVer")),2));
			buffer.put(DisposeUtil.converIntData(Integer.parseInt(billingParam.get("parkingfee")),2));
			int timenum = Integer.parseInt(billingParam.get("timenum"));
			buffer.put((byte) timenum);
			if (timenum > 0) {
				String timeInfoStr = billingParam.get("timeInfo");
				List<Map<String, Object>> timeInfo = (List<Map<String, Object>>) JSON.parse(timeInfoStr);
				for (Map<String, Object> map : timeInfo) {
					buffer.put((byte)(map.get("hour")));//段 N 起始-时
					buffer.put((byte)(map.get("minute")));//段 N 起始-分
					buffer.put((byte)(map.get("type")));//段 N 类型
					buffer.put(DisposeUtil.converIntData((int)(map.get("chargefee")),2));//段 N 电价费率
					buffer.put(DisposeUtil.converIntData((int)(map.get("serverfee")),2));//段 N 服务费率
				}
			}
		}
		buffer.position(1);
		byte[] bytes = new byte[datalen & 0xffff + 12];
		buffer.get(bytes);
		buffer.put(clacSumVal(bytes));
		buffer.flip();
		Server.sendMsg(devicenum, buffer);
	}
	
	/**
	 * 桩回复充电命令
	 */
	public static void parse_0x20(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		byte port = (byte) (buffer.get() + 1);
		byte[] orderBytes = new byte[32];
		buffer.get(orderBytes);
		String ordernum = new String(orderBytes);
		byte[] useridBytes = new byte[32];
		buffer.get(useridBytes);
		String userid = new String(useridBytes);
		byte userType = buffer.get();
		byte[] groupBytes = new byte[9];
		buffer.get(groupBytes);
		String groupCode = new String(groupBytes);
		byte ctrlWay = buffer.get();
		byte[] ctrlParamBytes = new byte[4];
		buffer.get(ctrlParamBytes);
		int ctrlParam = DisposeUtil.converData(ctrlParamBytes);//参数
		byte chargeWay = buffer.get();
		byte startWay = buffer.get();
		byte[] dateBytes = new byte[6];
		buffer.get(dateBytes);
		String timingStartTime = DisposeUtil.disposeDate(dateBytes);
		byte[] operBytes = new byte[6];
		buffer.get(operBytes);
		String userOperCode = new String(operBytes);
		byte billingWay = buffer.get();
		byte result = buffer.get();
		byte reason = buffer.get();
		byte sum = buffer.get();
		Map<String, Object> map = new HashMap<>();
		map.put("device_result", result);
		map.put("device_reason", reason);
		map.put("updatetime", System.currentTimeMillis());
		chargeMap.put(ordernum, map);
	}
	
	/**
	 * 桩启动充电结果
	 */
	public static void parse_0x21(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		byte port = (byte) (buffer.get() + 1);//枪号
		byte[] orderBytes = new byte[32];
		buffer.get(orderBytes);
		String ordernum = new String(orderBytes);//订单号
		byte[] useridBytes = new byte[32];
		buffer.get(useridBytes);
		String userid = new String(useridBytes);//用户id
		byte userType = buffer.get();//用户类型
		byte[] groupBytes = new byte[9];
		buffer.get(groupBytes);
		String groupCode = new String(groupBytes);//组织机构代码
		byte[] carBytes = new byte[2];
		buffer.get(carBytes);
		String carNum = new String(carBytes);//车牌号
		byte[] carLocalBytes = new byte[7];
		buffer.get(carBytes);
		String carLocalNum = new String(carBytes);//车牌号
		byte ctrlWay = buffer.get();//控制方式
		int ctrlParam = buffer.getInt();//控制参数
		byte chargeWay = buffer.get();//充电模式
		byte deviceType = buffer.get();//充电桩类型1-交流；2-直流
		byte result = buffer.get();//启动结果1-成功；2-失败
		short reason = buffer.getShort();
		if (reason == 1) {
			byte[] dateBytes = new byte[6];
			buffer.get(dateBytes);
			String chargeTime = DisposeUtil.disposeDate(dateBytes);
			int startElec = buffer.getInt();
		}
		send_0x22(devicenum, port);
	}
	
	/**
	 * 平台回复桩启动充电结果
	 */
	public static void send_0x22(String devicenum, byte port) {
		ByteBuffer buffer = ByteBuffer.allocate(65522);
		buffer.putShort(framestart);
		buffer.put((byte) 0x22);//cmd+
		buffer.put(AESUtil.String_BCD(devicenum));
		buffer.put((byte) 0x01);//数据加密方式
		short datalen = 0x07;
		buffer.put(DisposeUtil.getDateFlag(0, 0));
		buffer.put(port);
		buffer.position(1);
		byte[] bytes = new byte[datalen & 0xffff + 12];
		buffer.get(bytes);
		buffer.put(clacSumVal(bytes));
		buffer.flip();
		Server.sendMsg(devicenum, buffer);
	}
	
	/**
	 * 桩充电停止
	 */
	public static void parse_0x26(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		byte port = buffer.get();
		int chargeV = buffer.getShort() & 0xffff;
	}
	
	/**
	 * 平台回复桩充电停止
	 */
	public static void send_0x27(String devicenum,byte port) {
		ByteBuffer buffer = ByteBuffer.allocate(65522);
		buffer.putShort(framestart);
		buffer.put((byte) 0x24);//cmd+
		buffer.put(AESUtil.String_BCD(devicenum));
		buffer.put((byte) 0x01);//数据加密方式
		short datalen = 0x07;
		buffer.put(DisposeUtil.getDateFlag(0, 0));
		buffer.put(port);
		buffer.position(1);
		byte[] bytes = new byte[datalen & 0xffff + 12];
		buffer.get(bytes);
		buffer.put(clacSumVal(bytes));
		buffer.flip();
		Server.sendMsg(devicenum, buffer);
	}
	
	/**
	 * 充电桩工作信息
	 */
	public static void parse_0x25(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		byte port = buffer.get();
		if (port < 0 || port > 29) {
			return;
		}
		int chargeV = DisposeUtil.converIntDataBackInt(buffer.getShort() & 0xffff, 2);//充电电压0.1v
		int chargeA = DisposeUtil.converIntDataBackInt(buffer.getShort() & 0xffff, 2);//充电电流0.1A
		int chargeElec = DisposeUtil.converIntDataBackInt(buffer.getInt(), 4);//充电电量0.01kWh
		int chargeTime = DisposeUtil.converIntDataBackInt(buffer.getInt(), 4);//充电时长1s
		int chargeMoney = DisposeUtil.converIntDataBackInt(buffer.getInt(), 4);//充电金额0.01 元
		byte chargenum = buffer.get();//充电模块接入数量
	}
	
	/**
	 * 平台下发停止充电命令
	 */
	public static Map<String, Object> send_0x26(String devicenum, byte port) {
		ByteBuffer buffer = ByteBuffer.allocate(65522);
		buffer.putShort(framestart);
		buffer.put((byte) 0x26);//cmd+
		buffer.put(AESUtil.String_BCD(devicenum));
		buffer.put((byte) 0x01);//数据加密方式
		short datalen = 0x07;
		buffer.put(DisposeUtil.getDateFlag(0, 0));
		buffer.put((byte) (port - 1));
		buffer.position(1);
		byte[] bytes = new byte[datalen & 0xffff + 12];
		buffer.get(bytes);
		buffer.put(clacSumVal(bytes));
		buffer.flip();
		Server.sendMsg(devicenum, buffer);
		long nowtime = System.currentTimeMillis();
		int temp = 0;
		boolean flag = true;
		long updatetime = (long) chargeMap.get(devicenum + port);
		while (flag) {
			if (temp >= 15) {
				return CommUtil.responseBuildInfo(1001, "连接超时", null);
			}
			if (updatetime != 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
				temp++;
				continue;
			} else {
				if (nowtime - updatetime > 0 && nowtime - updatetime < 15000) {
					return CommUtil.responseBuildInfo(1000, "停止成功", null);
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
		return CommUtil.responseBuildInfo(1002, "系统异常", null);
	}
	
	/**
	 * 桩回复停止充电命令
	 */
	public static void parse_0x27(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		byte port = (byte) (buffer.get() + 1);
		stopChargeMap.put(devicenum + port, System.currentTimeMillis());
	}
	
	/**
	 * 桩上送 BMS 状态需求报文
	 */
	public static void parse_0x30(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		
	}
	
	/**
	 * 桩上送 BMS 电压采集信息
	 */
	public static void parse_0x31(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		
	}
	
	/**
	 * 桩上送 BMS 温度采集信息
	 */
	public static void parse_0x32(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		
	}
	
	/**
	 * 桩上送充电订单
	 */
	public static void parse_0x23(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		byte port = buffer.get();//
		int recordIndex = buffer.getInt();//
		byte[] orderBytes = new byte[32];
		buffer.get(orderBytes);
		String ordernum = new String(orderBytes);//
		byte[] useridBytes = new byte[32];
		buffer.get(useridBytes);
		String userid = new String(useridBytes);//
		byte userType = buffer.get();
		byte[] groupBytes = new byte[9];
		buffer.get(groupBytes);//
		String groupCode = new String(groupBytes);//
		int cardMoney = buffer.getInt();
		byte[] VINBytes = new byte[17];
		buffer.get(VINBytes);
		String VIN = new String(VINBytes);//
		byte[] startBytes = new byte[6];
		buffer.get(startBytes);
		String startTime = DisposeUtil.disposeDate(startBytes);//
		byte[] endBytes = new byte[6];
		buffer.get(endBytes);
		String endTime = DisposeUtil.disposeDate(endBytes);//
		int startElec = DisposeUtil.converIntDataBackInt(buffer.getInt(), 4);//开始充电电量
		int endElec = DisposeUtil.converIntDataBackInt(buffer.getInt(), 4);//结束充电电量
		byte startSOC = buffer.get();//
		byte endSOC = buffer.get();//
		byte ctrlWay = buffer.get();//
		int ctrlParam = buffer.getInt();//
		byte startType = buffer.get();//
		byte[] timingBytes = new byte[6];
		buffer.get(timingBytes);
		String timingTime = DisposeUtil.disposeDate(timingBytes);//
		byte chargeType = buffer.get();//
		short stopReason = buffer.getShort();//
		byte billingType = buffer.get();//
		short billingVer = buffer.getShort();//计费模型版本
		int chargeMoney = buffer.getInt();//电能费用
		int serverMoney = buffer.getInt();//服务费费用
		int parkMoney = buffer.getInt();//停车费费用
		int timenum = buffer.get() & 0xff;
		for (int i = 0; i < timenum; i++) {
			byte timeIndex = buffer.get();//段 N 计费模型索引
			short timeElec = buffer.getShort();//段 N 电量
		}
		
		send_0x24(devicenum, port, recordIndex);
		
	}
	
	/**
	 * 平台确认充电订单
	 */
	public static void send_0x24(String devicenum, byte port, int recordIndex) {
		ByteBuffer buffer = ByteBuffer.allocate(65522);
		buffer.putShort(framestart);
		buffer.put((byte) 0x24);//cmd+
		buffer.put(AESUtil.String_BCD(devicenum));
		buffer.put((byte) 0x01);//数据加密方式
		short datalen = 11;
		buffer.put(DisposeUtil.getDateFlag(0, 0));
		buffer.put(port);
		buffer.put(DisposeUtil.converIntData(recordIndex,2));
		buffer.position(1);
		byte[] bytes = new byte[datalen & 0xffff + 12];
		buffer.get(bytes);
		buffer.put(clacSumVal(bytes));
		buffer.flip();
		Server.sendMsg(devicenum, buffer);
	}
	
	/**
	 * 桩上送充电订单
	 */
	public static void parse_0x33(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		byte port = buffer.get();//
		int recordIndex = buffer.getInt();//
		byte[] orderBytes = new byte[32];
		buffer.get(orderBytes);
		String ordernum = new String(orderBytes);//
		byte[] useridBytes = new byte[32];
		buffer.get(useridBytes);
		String userid = new String(useridBytes);//
		byte userType = buffer.get();
		byte[] groupBytes = new byte[9];
		buffer.get(groupBytes);//
		String groupCode = new String(groupBytes);//
		int cardMoney = buffer.getInt();
		byte[] VINBytes = new byte[17];
		buffer.get(VINBytes);
		String VIN = new String(VINBytes);//
		byte[] startBytes = new byte[6];
		buffer.get(startBytes);
		String startTime = DisposeUtil.disposeDate(startBytes);//
		byte[] endBytes = new byte[6];
		buffer.get(endBytes);
		String endTime = DisposeUtil.disposeDate(endBytes);//
		int startElec = buffer.getInt();//
		int endElec = buffer.getInt();//
		byte startSOC = buffer.get();//
		byte endSOC = buffer.get();//
		byte ctrlWay = buffer.get();//
		int ctrlParam = buffer.getInt();//
		byte startType = buffer.get();//
		byte[] timingBytes = new byte[6];
		buffer.get(timingBytes);
		String timingTime = DisposeUtil.disposeDate(timingBytes);//
		byte chargeType = buffer.get();//
		short stopReason = buffer.getShort();//
		byte billingType = buffer.get();//
		short billingVer = buffer.getShort();//计费模型版本
		int chargeMoney = buffer.getInt();//电能费用
		int serverMoney = buffer.getInt();//服务费费用
		int parkMoney = buffer.getInt();//停车费费用
		int timenum = buffer.get() & 0xff;
		for (int i = 0; i < timenum; i++) {
			byte timeIndex = buffer.get();//段 N 计费模型索引
			short timeElec = buffer.getShort();//段 N 电量
		}
		
		send_0x34(devicenum, port, recordIndex);
		
	}
	
	/**
	 * 平台确认充电订单
	 */
	public static void send_0x34(String devicenum, byte port, int recordIndex) {
		ByteBuffer buffer = ByteBuffer.allocate(65522);
		buffer.putShort(framestart);
		buffer.put((byte) 0x34);//cmd+
		buffer.put(AESUtil.String_BCD(devicenum));
		buffer.put((byte) 0x01);//数据加密方式
		short datalen = 11;
		buffer.put(DisposeUtil.getDateFlag(0, 0));
		buffer.put(port);
		buffer.put(DisposeUtil.converIntData(recordIndex,2));
		buffer.position(1);
		byte[] bytes = new byte[datalen & 0xffff + 12];
		buffer.get(bytes);
		buffer.put(clacSumVal(bytes));
		buffer.flip();
		Server.sendMsg(devicenum, buffer);
	}
	
	/**
	 * 平台设置新 IP 地址
	 */
	public static Map<String, Object> send_0x35(String devicenum, String serverIP, short serverPort) {
		ByteBuffer buffer = ByteBuffer.allocate(65522);
		buffer.putShort(framestart);
		buffer.put((byte) 0x35);//cmd+
		buffer.put(AESUtil.String_BCD(devicenum));
		buffer.put((byte) 0x01);//数据加密方式
		short datalen = 58;
		buffer.put(DisposeUtil.getDateFlag(0, 0));
		buffer.put(DisposeUtil.complateBytes(serverIP.getBytes(), 50));
		buffer.put(DisposeUtil.converIntData(serverPort,2));
		buffer.position(1);
		byte[] bytes = new byte[datalen & 0xffff + 12];
		buffer.get(bytes);
		buffer.put(clacSumVal(bytes));
		buffer.flip();
		Server.sendMsg(devicenum, buffer);
		long nowtime = System.currentTimeMillis();
		int temp = 0;
		boolean flag = true;
		Map<String, Object> serverIpAddrback = (Map<String, Object>) serverIpAddrMap.get(devicenum);
		while (flag) {
			if (temp >= 15) {
				return CommUtil.responseBuildInfo(1001, "连接超时", null);
			}
			if (serverIpAddrback == null || serverIpAddrback.size() == 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
				temp++;
				continue;
			} else {
				long updatetime = (long) serverIpAddrback.get("updatetime");
				if (nowtime - updatetime > 0 && nowtime - updatetime < 15000) {
					return CommUtil.responseBuildInfo(1000, "连接成功", serverIpAddrback);
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
		return CommUtil.responseBuildInfo(1002, "系统异常", null);
	}
	
	/**
	 * 桩回复新 IP 地址设置
	 */
	public static void parse_0x36(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		byte[] ipBytes = new byte[50];
		buffer.get(ipBytes);
		String serverIP = new String(ipBytes);
		int serverPort = DisposeUtil.converIntDataBackInt(buffer.getShort(), 2);
		Map<String, Object> map = new HashMap<>();
		map.put("serverIP", serverIP);
		map.put("serverPort", serverPort);
		map.put("updatetime", System.currentTimeMillis());
		serverIpAddrMap.put(devicenum, map);
	}
	
	/**
	 * 平台设置计费模型
	 */
	public static void send_0x37(String devicenum, Map<String, String> billMap) {
		ByteBuffer buffer = ByteBuffer.allocate(65522);
		buffer.putShort(framestart);
		buffer.put((byte) 0x37);//cmd+
		buffer.put(AESUtil.String_BCD(devicenum));
		buffer.put((byte) 0x01);//数据加密方式
		byte timenum = Byte.parseByte( billMap.get("timenum"));//时段数
		short datalen = (short) (13 + (timenum * 11));
		buffer.put(DisposeUtil.converIntData(datalen, 2));
		buffer.put(DisposeUtil.getDateFlag(0, 0));
		short billver = Short.parseShort( billMap.get("billver"));//计费模型版本
		buffer.put(DisposeUtil.converIntData(billver, 2));
		int parkingfee = Integer.parseInt( billMap.get("parkingfee"));//停车费费率
		buffer.put(DisposeUtil.converIntData(parkingfee, 4));
		buffer.put(timenum);
		String timeInfoStr = billMap.get("timeInfo");
		List<Map<String, Object>> timeInfo = (List<Map<String, Object>>) JSON.parse(timeInfoStr);
		for (Map<String, Object> timeMap : timeInfo) {
			buffer.put((byte) timeMap.get("hour"));//时
			buffer.put((byte) timeMap.get("minute"));//分
			buffer.put((byte) timeMap.get("type"));//类型
			Double chargefee = (Double) timeMap.get("chargefee");//电能费
			chargefee = chargefee * 10000;
			buffer.put(DisposeUtil.converIntData(chargefee.intValue(), 4));
			Double serverfee = (Double) timeMap.get("serverfee");//服务费
			serverfee = serverfee * 10000;
			buffer.put(DisposeUtil.converIntData(serverfee.intValue(), 4));
		}
		buffer.position(1);
		byte[] bytes = new byte[datalen & 0xffff + 12];
		buffer.get(bytes);
		buffer.put(clacSumVal(bytes));
		buffer.flip();
		Server.sendMsg(devicenum, buffer);
	}
	
	/**
	 * 桩回复计费模型设置
	 */
	public static void parse_0x38(String devicenum,AsynchronousSocketChannel channel,ByteBuffer buffer,
			byte encryptionWay, int datalen, String deviceDataTime) {
		byte[] billverBytes = new byte[2];
		int billver = DisposeUtil.converData(billverBytes);//计费版本
		byte result_info = buffer.get();//设置结果1-成功；2-失败
		byte fail_info = buffer.get();//失败原因1- 时间段穿插、2- 时间段数错误、3- 数据格式错误、4- 其它
	}
	
}
