package com.hedong.hedongwx.utils;

/**
 * equipment communication data send and reply
 */
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.sql.SQLException;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.github.wxpay.sdk.WXPay;
import com.hedong.hedongwx.config.AlipayConfig;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.AllPortStatusHandler;
import com.hedong.hedongwx.dao.AreaHandler;
import com.hedong.hedongwx.dao.ChargeRecordHandler;
import com.hedong.hedongwx.dao.Equipmenthandler;
import com.hedong.hedongwx.dao.OfflineCardHandler;
import com.hedong.hedongwx.dao.OnlineCardHandler;
import com.hedong.hedongwx.dao.TradeRecordHandler;
import com.hedong.hedongwx.dao.UserHandler;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.CodeSystemParam;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.Money;
import com.hedong.hedongwx.entity.OfflineCard;
import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.OnlineCardRecord;
import com.hedong.hedongwx.entity.PackageMonth;
import com.hedong.hedongwx.entity.Realchargerecord;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.thread.Server;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SendMsgUtil {

	public static boolean SETCOINICFLAG = false;
	private static String setcoin;
	private static String setic;
	public static String onelock;
	public static String twolock;
	public static boolean SETSYSTEMSEND = false;
	public static boolean SETSYSTEMREPLY = false;
	public static byte clickport;
	public static byte clickpayport;
	private static final Logger logger = LoggerFactory.getLogger(SendMsgUtil.class);
	//防止v3设备重复提交结束充电
	public static Map<String, Long> v3DisposeDataMap = new HashMap<>();

	public static void main(String[] args) throws Exception {
 	}
	
	public static byte checkoutSum(byte[] array) {
		byte v = 0;
		for (int i = 0; i < array.length; i++) {
			v ^= array[i];
		}
		return v;
	}

	/**
	 * 获取过去第几天的日期
	 *
	 * @param past
	 * @return
	 */
	public static String getPastDayDate(int past) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String result = format.format(today);
		return result;
	}
	
	/**
	 * 向设备发送自定义数据包
	 * @param param
	 * @param code
	 */
	public static void send_Param(String param, String devicenum) {
		if (param.length()%2 == 0) {
			try {
				ByteBuffer buffer = ByteBuffer.allocate(65522);
				for(int i=0;i<param.length()/2;i++){
					String subParam = param.substring(2*i, 2*i+2);
					int parseInt = Integer.parseInt(subParam, 16);
					buffer.put((byte) parseInt);
				}
				buffer.flip();
				Server.sendMsg(devicenum, buffer);
			} catch (Exception e) {
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss------").format(new Date()) + e.getMessage());
			}
		}
	}

	/**
	 * @Description：设置设备当前的参数
	 * @param type  value  code
	 * @author： origin   2020年5月9日上午11:51:56
	 * @comment:0x32
	 */
	public static void send_0x32(byte type, int value, String code) {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			byte sop = (byte) 0xAA;
			byte len = (byte) 0x08;
			byte cmd = (byte) 0x32;
			byte result = (byte) 0x01;
			buffer.put(sop);
			buffer.put(len);
			buffer.put(cmd);
			buffer.put(result);
			buffer.put(type);
			buffer.putInt(value);
			buffer.position(0);
			buffer.get();
			byte[] bytes = new byte[8];
			buffer.get(bytes, 0, len);
			byte sum = 0x00;
			for (byte b : bytes) {
				sum ^= b;
			}
			System.out.println(sum);
			buffer.put(sum);
			buffer.flip();
			while (buffer.hasRemaining()) {
				System.out.printf("send_0x32 ", buffer.get());
			}
			buffer.position(0);
			Server.sendMsg(code, buffer);
		} catch (Exception e) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss------").format(new Date()) + e.getMessage());
		}
	}
	
	/**
	 * @Description：设置设备当前的参数
	 * @param type  value  code
	 * @author： origin   2020年5月9日上午11:51:56
	 * @comment:0x32
	 */
//	public static void send_0x33( byte now_temp, String code) {
	public static void send_0x33( String code) {
		try {
//			NOW_TEMP now_temp
//			0x0000
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			byte sop = (byte) 0xAA;
			byte len = (byte) 0x05;
			byte cmd = (byte) 0x33;
			byte result = (byte) 0x01;
			short now_temp = 0x0000;
			buffer.put(sop);
			buffer.put(len);
			buffer.put(cmd);
			buffer.put(result);
			buffer.putShort(now_temp);
			buffer.position(0);
			
//			byte sum = (byte) (len ^ cmd ^ result ^ now_temp);
//			buffer.put(sum);
//			buffer.flip();
			
			buffer.get();
			byte[] bytes = new byte[5];
			buffer.get(bytes, 0, len);
			byte sum = 0x00;
			for (byte b : bytes) {
				sum ^= b;
			}
			System.out.println(sum);
			buffer.put(sum);
			buffer.flip();
			while (buffer.hasRemaining()) {
				System.out.printf("send_0x33 ", buffer.get());
			}
			buffer.position(0);
			Server.sendMsg(code, buffer);
		} catch (Exception e) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss------").format(new Date()) + e.getMessage());
		}
	}
	
	/**
	 * @Description：获取当前设备的参数
	 * @param type  code  例子：0x00
	 * @author： origin   2020年5月9日上午11:51:56
	 * @comment:0x32
	 */
	public static void send_0x34(byte type, String code) {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			byte sop = (byte) 0xAA;
			byte len = (byte) 0x04;
			byte cmd = (byte) 0x34;
			byte result = (byte) 0x01;
			buffer.put(sop);
			buffer.put(len);
			buffer.put(cmd);
			buffer.put(result);
			buffer.put(type);
			buffer.position(0);
			buffer.get();
			byte[] bytes = new byte[4];
			buffer.get(bytes, 0, len);
			byte sum = 0x00;
			for (byte b : bytes) {
				sum ^= b;
			}
			System.out.println(sum);
			buffer.put(sum);
			buffer.flip();
			while (buffer.hasRemaining()) {
				System.out.printf("send_0x34 ", buffer.get());
			}
			buffer.position(0);
			Server.sendMsg(code, buffer);
		} catch (Exception e) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss------").format(new Date()) + e.getMessage());
		}
	}
	
	/**
	 * @Description：获取设备设置的参数
	 * @param type  value  code
	 * @author： origin   2020年5月9日上午11:51:56
	 * @comment:0x32
	 */
	public static void send_0x35(byte type, String code) {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			byte sop = (byte) 0xAA;
			byte len = (byte) 0x04;
			byte cmd = (byte) 0x35;
			byte result = (byte) 0x01;
			buffer.put(sop);
			buffer.put(len);
			buffer.put(cmd);
			buffer.put(result);
			buffer.put(type);
			buffer.position(0);
			buffer.get();
			byte[] bytes = new byte[4];
			buffer.get(bytes, 0, len);
			byte sum = 0x00;
			for (byte b : bytes) {
				sum ^= b;
			}
			System.out.println(sum);
			buffer.put(sum);
			buffer.flip();
			while (buffer.hasRemaining()) {
				System.out.printf("0x35 ", buffer.get());
			}
			buffer.position(0);
			Server.sendMsg(code, buffer);
		} catch (Exception e) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss------").format(new Date()) + e.getMessage());
		}
	}
	
	/**
	 * @Description：报警
	 * @param type  value  code
	 * @author： origin   2020年5月9日上午11:51:56
	 * @comment:0x36
	 */
	public static void send_0x36(byte type, byte port, int value, String code) {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			byte sop = (byte) 0xAA;
			byte len = (byte) 0x09;
			byte cmd = (byte) 0x36;
			byte result = (byte) 0x01;
			buffer.put(sop);
			buffer.put(len);
			buffer.put(cmd);
			buffer.put(result);
			buffer.put(type);
			buffer.put(port);
			buffer.putInt(value);
			buffer.position(0);
			buffer.get();
			byte[] bytes = new byte[9];
			buffer.get(bytes, 0, len);
			byte sum = 0x00;
			for (byte b : bytes) {
				sum ^= b;
			}
			System.out.println(sum);
			buffer.put(sum);
			buffer.flip();
			while (buffer.hasRemaining()) {
				System.out.printf("0x36 ", buffer.get());
			}
			buffer.position(0);
			Server.sendMsg(code, buffer);
		} catch (Exception e) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss------").format(new Date()) + e.getMessage());
		}
	}
	

	/**
	 * @Description：设置设备当前的参数	0x32
	 * @param channel
	 * @param len:字节长度【单字节。从CMD到SUM的字节数（含CMD和SUM）】
	 * @param cmd：单字节，命令字节
	 * @param result：单字节，表示命令是否成功
	 * @param buffer
	 * @author： origin 2020年5月9日下午5:51:08
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Map> parse_0x32(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Map<String,String> map = new HashMap<>();
		Map<String, Map> hasmap = new HashMap<>();
		String code = SendMsgUtil.getCodeByIpaddr(channel);
//		String code = getCodeByIpaddr(channel);
		try {
			//0x00表示成功，0x01表示失败，0x02 表示不支持
			System.out.println("输出parse_0x32的回复数据  len   " + len );
			System.out.println("输出parse_0x32的回复数据  cmd   " + cmd );
			System.out.println("输出parse_0x32的回复数据  result   " + result );
			System.out.println("输出parse_0x32的回复数据  buffer   " + buffer );
			System.out.println("输出parse_0x32的回复数据  code   " + code );
			byte res = buffer.get();
			System.out.println("输出parse_0x32的回复数据     " + res );
			byte sum = buffer.get();
			System.out.println("输出parse_0x32的回复数据     " + sum );
			map.put("code", code);
			map.put("sum", sum+"");
			map.put("res", res+"");
			map.put("insttime", System.currentTimeMillis() + "");
			System.out.println(code + "号机parse_0x32信息已接收成功         "+map);
			
			Server.thresholdvalue.put(code, map);;
			System.out.println("输出parse_0x32的回复数据map     " + map );
			
			hasmap.put(code, map);
		} catch (Exception e) {
			System.out.println(code + "号设备读取系统参数回复异常---" + e.getMessage());
		}
		return hasmap;
	}

	/**
	 * @Description：上传当前充电桩温度（0x33）
	 * @param channel
	 * @param len:字节长度【单字节。从CMD到SUM的字节数（含CMD和SUM）】
	 * @param cmd：单字节，命令字节
	 * @param result：单字节，表示命令是否成功
	 * @param buffer
	 * @author： origin 2020年5月9日下午5:51:08
	 * @return 
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Map> parse_0x33(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Map<String,String> map = new HashMap<>();
		Map<String, Map> hasmap = new HashMap<>();
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		try {
			byte NULL = buffer.get();
			
			byte sum = buffer.get();
			
			System.out.println("输出parse_0x33的回复数据  len   " + len );
			System.out.println("输出parse_0x33的回复数据  cmd   " + cmd );
			System.out.println("输出parse_0x33的回复数据  result   " + result );
			System.out.println("输出parse_0x33的回复数据  buffer   " + buffer );
			System.out.println("输出parse_0x33的回复数据  code   " + code );
			System.out.println("输出parse_0x33的回复数据  NULL   " + NULL );
			System.out.println("输出parse_0x33的回复数据 sum    " + sum );
			map.put("code", code);
			map.put("sum", sum+"");
			map.put("res", NULL+"");
			map.put("insttime", System.currentTimeMillis() + "");
			System.out.println(code + "号机parse_0x33信息已接收成功         "+map);
			Server.uploadingMap.put(code, map);;
			System.out.println("输出parse_0x33的回复数据map     " + map );
			hasmap.put(code, map);
		} catch (Exception e) {
			System.out.println(code + "号设备读取系统参数回复异常---" + e.getMessage());
		}
		return hasmap;
	}

	/**
	 * @Description：获取当前设备的参数	0x34
	 * @param channel
	 * @param len:字节长度【单字节。从CMD到SUM的字节数（含CMD和SUM）】
	 * @param cmd：单字节，命令字节
	 * @param result：单字节，表示命令是否成功
	 * @param buffer
	 * @author： origin 2020年5月9日下午5:51:08
	 * @return 
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Map> parse_0x34(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Map<String,String> map = new HashMap<>();
		Map<String, Map> hasmap = new HashMap<>();
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		try {
			byte type = buffer.get();
			int value = buffer.getInt();
			byte res = buffer.get();
			byte sum = buffer.get();
			System.out.println("输出parse_0x34的回复数据  len   " + len );
			System.out.println("输出parse_0x34的回复数据  cmd   " + cmd );
			System.out.println("输出parse_0x34的回复数据  result   " + result );
			System.out.println("输出parse_0x34的回复数据  buffer   " + buffer );
			System.out.println("输出parse_0x34的回复数据  code   " + code );
			System.out.println("输出parse_0x34的回复数据  type   " + type );
			System.out.println("输出parse_0x34的回复数据  value   " + value );
			System.out.println("输出parse_0x34的回复数据  res   " + res );
			System.out.println("输出parse_0x34的回复数据  sum   " + sum );
			int sumyihuo = 0;
			if(sum<0) sumyihuo = 256 + sum;
			map.put("code", code);
			map.put("type", type+"");
			map.put("value", value+"");
			map.put("res", res+"");
			map.put("sum", sum+"");
			
			System.out.println("输出parse_0x34的回复数据  异或 sumyihuo   " + sumyihuo);
			map.put("insttime", System.currentTimeMillis() + "");
			System.out.println(code + "号机parse_0x34信息已接收成功         "+map);
			Server.getParameMap.put(code, map);
//			Integer merid = Equipmenthandler.findUserEquipment(code);
//			Equipmenthandler.insertioWarnparams( code, merid CommUtil.toInteger(type), CommUtil.toInteger(value));
			
			System.out.println("输出parse_0x34的回复数据map     " + map );
			hasmap.put(code, map);
		} catch (Exception e) {
			System.out.println(code + "号设备读取系统参数回复异常---" + e.getMessage());
		}
		return hasmap;
	}

	/**
	 * @Description：获取设备设置的参数	0x35
	 * @param channel
	 * @param len:字节长度【单字节。从CMD到SUM的字节数（含CMD和SUM）】
	 * @param cmd：单字节，命令字节
	 * @param result：单字节，表示命令是否成功
	 * @param buffer
	 * @author： origin 2020年5月9日下午5:51:08
	 * @return 
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Map> parse_0x35(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Map<String,String> map = new HashMap<>();
		Map<String, Map> hasmap = new HashMap<>();
		String code = SendMsgUtil.getCodeByIpaddr(channel);
//		String code = getCodeByIpaddr(channel);
		try {
			byte type = buffer.get();
			int value = buffer.getInt();
			byte res = buffer.get();
			byte sum = buffer.get();
			
			System.out.println("输出parse_0x35的回复数据  len   " + len );
			System.out.println("输出parse_0x35的回复数据  cmd   " + cmd );
			System.out.println("输出parse_0x35的回复数据  result   " + result );
			System.out.println("输出parse_0x35的回复数据  buffer   " + buffer );
			System.out.println("输出parse_0x35的回复数据  code   " + code );
			System.out.println("输出parse_0x35的回复数据  type   " + type );
			System.out.println("输出parse_0x35的回复数据  value   " + value );
			System.out.println("输出parse_0x35的回复数据  res   " + res );
			System.out.println("输出parse_0x35的回复数据  sum   " + sum );
			map.put("code", code);
			map.put("type", type+"");
			map.put("value", value+"");
			map.put("res", res+"");
			map.put("sum", sum+"");
			map.put("insttime", System.currentTimeMillis() + "");
			System.out.println(code + "号机parse_0x35信息已接收成功         "+map);
			Server.getArgumentMap.put(code, map);;
			System.out.println("输出parse_0x35的回复数据map     " + map );
			
			Equipmenthandler.redactCodeSysParam( code, CommUtil.toInteger(type), CommUtil.toInteger(value));
			
			hasmap.put(code, map);
		} catch (Exception e) {
			System.out.println(code + "号设备读取系统参数回复异常---" + e.getMessage());
		}
		return hasmap;
	}

	/**
	 * @Description：报警(温度烟感功率保险丝继电器报警)	0x36
	 * @param channel
	 * @param len:字节长度【单字节。从CMD到SUM的字节数（含CMD和SUM）】
	 * @param cmd：单字节，命令字节
	 * @param result：单字节，表示命令是否成功
	 * @param buffer
	 * @author： origin 2020年5月9日下午5:51:08
	 * @return 
	 */
	public static Map<String, Map> parse_0x36(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Map<String,String> map = new HashMap<>();
		Map<String, Map> hasmap = new HashMap<>();
		String code = SendMsgUtil.getCodeByIpaddr(channel);
//		String code = getCodeByIpaddr(channel);
		try {
			byte NULL = buffer.get();
			byte sum = buffer.get();
			System.out.println("输出parse_0x36的回复数据  len   " + len );
			System.out.println("输出parse_0x36的回复数据  cmd   " + cmd );
			System.out.println("输出parse_0x36的回复数据  result   " + result );
			System.out.println("输出parse_0x36的回复数据  buffer   " + buffer );
			System.out.println("输出parse_0x36的回复数据  code   " + code );
			System.out.println("输出parse_0x36的回复数据  NULL   " + NULL );
			System.out.println("输出parse_0x36的回复数据  sum    " + sum );
			map.put("code", code);
			map.put("NULL", NULL+"");
			map.put("sum", sum+"");
			map.put("insttime", System.currentTimeMillis() + "");
			System.out.println(code + "号机parse_0x36信息已接收成功         "+map);
			Server.alarmMap.put(code, map);;
			System.out.println("输出parse_0x36的回复数据map     " + map );
			hasmap.put(code, map);
		} catch (Exception e) {
			System.out.println(code + "号设备读取系统参数回复异常---" + e.getMessage());
		}
		return hasmap;
	}
	
	/**
	 * Check the total number of charging station ports
	 * 
	 * @return ByteBuffer
	 */
	public static void send_1(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xAA;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x01;
		byte result = (byte) 0x00;
		byte data1 = (byte) 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data1);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data1);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机查询充电站端口信息已发送");
	}

	/**
	 * query all free charge station port
	 * 
	 * @return ByteBuffer
	 */
	public static void send_2(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xAA;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x02;
		byte result = (byte) 0x00;
		byte data = (byte) 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机查询空闲充电站端口已发送");
	}

	public static void send_0x14(byte port, short money, short time, short elec, String code) {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			byte sop = (byte) 0xAA;
			byte len = (byte) 0x0a;
			byte cmd = (byte) 0x14;
			byte result = (byte) 0x00;
			buffer.put(sop);
			buffer.put(len);
			buffer.put(cmd);
			buffer.put(result);
			buffer.put(port);
			buffer.putShort(money);
			buffer.putShort(time);
			buffer.putShort(elec);
			buffer.position(0);
			buffer.get();
			byte[] bytes = new byte[10];
			buffer.get(bytes, 0, len);
			byte sum = 0x00;
			for (byte b : bytes) {
				sum ^= b;
			}
			buffer.put(sum);
			buffer.flip();
//			while (buffer.hasRemaining()) {
//				System.out.printf("0x%02x ", buffer.get());
//			}
//			buffer.position(0);
			Server.sendMsg(code, buffer);
			operRedisAllPortStatus(code, port, time, elec, "2");
			logger.info("wolflog---" + code + "号机支付信息已发送");
		} catch (Exception e) {
			logger.info("wolflog---" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss------").format(new Date()) + e.getMessage());
		}
	}

	public static void send_5(byte port, String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xAA;
		byte len = 0x04;
		byte cmd = 0x05;
		byte result = 0x00;
		byte data = port;
		byte sum = (byte) (len ^ cmd ^ result ^ data);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机查询当前的充电状态信息已发送");
	}

	public static void send_7(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xAA;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x07;
		byte result = (byte) 0x00;
		byte data = (byte) 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机查询消费总额数据信息已发送");
	}

	public static void send_9(byte coin, byte ic, String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xAA;
		byte len = (byte) 0x05;
		byte cmd = (byte) 0x09;
		byte result = (byte) 0x00;
		byte[] data = new byte[2];
		if (coin == 0) {
			data[0] = 0x00;
			setSetcoin("不可用");
		} else if (coin == 1) {
			data[0] = 0x01;// 投币器默认设置为可用
			setSetcoin("可用");
		} else if (coin == -1) {
			String wolf = "wolf";
		} else {
			return;
		}
		if (ic == 0) {
			data[1] = 0x00;
			setSetic("不可用");
		} else if (ic == 1) {
			data[1] = 0x01;
			setSetic("可用");
		} else if (ic == -1) {
			String wolf = "wolf";
		} else {
			return;
		}
		byte sum = (byte) (len ^ cmd ^ result ^ data[0] ^ data[1]);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机设置信息已发送");
	}

	public static void send_11() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0x55;
		byte len = (byte) 0x07;
		byte cmd = (byte) 0x0b;
		byte result = (byte) 0x01;
		int data = (int) (System.currentTimeMillis() / 1000);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.putInt(data);
		buffer.position(0);
		byte[] datas = new byte[9];
		buffer.get(datas, 0, 8);
		byte sum = 0x00;
		for (int i = 1; i < datas.length - 1; i++) {
			sum ^= datas[i];
		}
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg("1", buffer);
		logger.info("wolflog---" + "系统时间信息已发送");
	}

	public static void send_12(byte port, byte status, String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xAA;
		byte len = (byte) 0x05;
		byte cmd = (byte) 0x0c;
		byte result = (byte) 0x00;
		byte data1 = port;
		byte data2 = status;
		byte sum = (byte) (len ^ cmd ^ result ^ data1 ^ data2);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data1);
		buffer.put(data2);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		if (status == 1) {
			logger.info("wolflog---" + code + "号机解锁设置信息已发送");
		} else if (status == 0) {
			logger.info("wolflog---" + code + "号机锁定设置信息已发送");
		}

	}

	public static void send_13(byte port, String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xAA;
		byte len = (byte) 0x05;
		byte cmd = (byte) 0x0d;
		byte result = (byte) 0x00;
		byte data1 = port;
		byte data2 = 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data1 ^ data2);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data1);
		buffer.put(data2);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机远程停止" + port + "号 端口的充电信息已发送");
	}

	public static int send_15(String code) {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			byte sop = (byte) 0xAA;
			byte len = (byte) 0x04;
			byte cmd = (byte) 0x0f;
			byte result = (byte) 0x00;
			byte data1 = (byte) 0x00;
			byte sum = (byte) (len ^ cmd ^ result ^ data1);
			buffer.put(sop);
			buffer.put(len);
			buffer.put(cmd);
			buffer.put(result);
			buffer.put(data1);
			buffer.put(sum);
			buffer.flip();
//			while (buffer.hasRemaining()) {
//				System.out.printf("0x%02x ", buffer.get());
//			}
//			buffer.position(0);
			int sendMsg = Server.sendMsg(code, buffer);
			logger.info("wolflog---" + code + "号机读取设备每个端口的状态信息已发送");
			return sendMsg;
		} catch (Exception e) {
			logger.info("wolflog---" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss------").format(new Date()) + e.getMessage());
			return 0;
		}
	}

	public static void send_16(AsynchronousSocketChannel channel, Object data, Object money) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0x55;
		byte len = (byte) 0x06;
		byte cmd = (byte) 0x10;
		byte result = (byte) 0x01;
		byte data1 = (byte) data;
		short moneyValue = (short) money;
		// Server.moneyValue;
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data1);
		buffer.putShort(moneyValue);
		buffer.position(0);
		byte[] sums = new byte[8];
		buffer.get(sums, 0, 7);
		byte sum = 0x00;
		for (int i = 1; i < sums.length - 1; i++) {
			sum ^= sums[i];
			System.out.printf("0x%02x ", sums[i]);
		}
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机提交在线卡信息已回复");
	}

	public static void send_0x10(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0x55;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x10;
		byte result = (byte) 0x01;
		byte data1 = (byte) 0x00;
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data1);
		buffer.position(0);
		byte[] sums = new byte[8];
		buffer.get(sums, 0, 7);
		byte sum = 0x00;
		for (int i = 1; i < sums.length - 1; i++) {
			sum ^= sums[i];
		}
		buffer.put(sum);
		buffer.flip();
		Server.sendMsg(code, buffer);
		logger.info(code + "号机上传设备故障已回复");
	}

	public static void send_22(byte port, String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0x55;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x16;
		byte result = (byte) 0x01;
		byte data1 = port;
		byte sum = (byte) (len ^ cmd ^ result ^ data1);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data1);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机提交充电结束状态信息已回复");
	}

	public static void send_24(short coin_min, short card_min, byte coin_elec, byte card_elec, byte cst,
			short power_max_1, short power_max_2, short power_max_3, short power_max_4, byte power_2_tim,
			byte power_3_tim, byte power_4_tim, byte sp_rec_mon, byte sp_full_empty, byte full_power_min,
			byte full_charge_time, byte elec_time_first, String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xAA;
		byte len = 0x1A;
		byte cmd = 0x18;
		byte result = 0x00;
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.putShort(coin_min);
		buffer.putShort(card_min);
		buffer.put(coin_elec);
		buffer.put(card_elec);
		buffer.put(cst);
		buffer.putShort(power_max_1);
		buffer.putShort(power_max_2);
		buffer.putShort(power_max_3);
		buffer.putShort(power_max_4);
		buffer.put(power_2_tim);
		buffer.put(power_3_tim);
		buffer.put(power_4_tim);
		buffer.put(sp_rec_mon);
		buffer.put(sp_full_empty);
		buffer.put(full_power_min);
		buffer.put(full_charge_time);
		buffer.put(elec_time_first);
		buffer.position(0);
		byte[] sums = new byte[28];
		buffer.get(sums, 0, 27);
		byte sum = 0x00;
		for (int i = 1; i < sums.length - 1; i++) {
			sum ^= sums[i];
		}
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机设置设备系统参数信息已发送");
		SETSYSTEMSEND = true;
	}

	public static void send_30(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xAA;
		byte len = 0x04;
		byte cmd = 0x1e;
		byte result = 0x00;
		byte data = 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机读取设备系统参数信息已发送");
	}

	public static void send_21(byte port, String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xAA;
		byte len = 0x04;
		byte cmd = 0x15;
		byte result = 0x00;
		byte data = port;
		byte sum = (byte) (len ^ cmd ^ result ^ data);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
		setClickport(port);
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机查询当前的充电状态信息已发送");
	}

	public static void send_32(AsynchronousSocketChannel channel) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0x55;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x20;
		byte result = (byte) 0x01;
		byte data1 = (byte) 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data1);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data1);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机上报投币打开的信息已回复");
	}

	public static int send_33(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0x55;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x21;
		byte result = (byte) 0x01;
		byte data1 = (byte) 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data1);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data1);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		logger.info("wolflog---" + code + "号机上传端口的实时状态信息已回复");
		int sendMsg = Server.sendMsg(code, buffer);
		return sendMsg;
	}

	public static int send_0x22(String code, int card_id, short card_surp, byte card_ope) {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			byte sop = (byte) 0xAA;
			byte len = (byte) 0x0A;
			byte cmd = (byte) 0x22;
			byte result = 0x01;
			buffer.put(sop);
			buffer.put(len);
			buffer.put(cmd);
			buffer.put(result);
			buffer.putInt(card_id);
			buffer.putShort(card_surp);
			buffer.put(card_ope);
			buffer.position(0);
			byte[] bytes = new byte[11];
			buffer.get(bytes, 0, 11);
			byte sum = 0x00;
			for (int i = 1; i < bytes.length; i++) {
				sum ^= bytes[i];
			}
			buffer.put(sum);
			buffer.flip();
//			while (buffer.hasRemaining()) {
//				System.out.printf("0x%02x ", buffer.get());
//			}
//			buffer.position(0);
			int sendMsg = Server.sendMsg(code, buffer);
			String str = card_ope == 0 ? "扣费" : card_ope == 1 ? "充值" : card_ope == 2 ? "查询" : "错误数据";
			logger.info("wolflog---" + code + "号机" + str + "离线卡信息已发送");
			return sendMsg;
		} catch (Exception e) {
			logger.info("wolflog---" + "信息发送失败" + e.getMessage());
			return 0;
		}
	}

	public static void send_0x81(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0x55;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x81;
		byte result = (byte) 0x01;
		byte data1 = (byte) 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data1);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data1);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机上传模块信息（硬件，软件，IMEI, +CCID）已回复");
	}

	public static int send_0x82(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xAA;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x82;
		byte result = 0x01;
		byte data = 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		int sendMsg = Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机查询模块支持投币数量已发送");
		return sendMsg;
	}

	public static void send_0x83(String code, Byte num, Byte money) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xAA;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x83;
		byte result = 0x01;
		byte sum = (byte) (len ^ cmd ^ result ^ num ^ money);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(num);
		buffer.put(money);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机选择投币通道支付已发送");
	}

	public static void send_0x84(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0x55;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x84;
		byte result = 0x01;
		byte num = 0x01;
		byte money = 0x01;
		byte sum = (byte) (len ^ cmd ^ result ^ num ^ money);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(num);
		buffer.put(money);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号机投币一次已回复");
	}

	public static ByteBuffer send_0x85() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xAA;
		byte len = 0x04;
		byte cmd = (byte) 0x85;
		byte result = 0x00;
		byte data = 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		return buffer;
	}

	/** 心跳数据*/
	public static int send_0x99(String code) {
		int sendMsg = 0;
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			byte cmd = (byte) 0x99;
			buffer.put(cmd);
			buffer.flip();
//			while (buffer.hasRemaining()) {
//				System.out.printf("0x%02x ", buffer.get());
//			}
//			buffer.position(0);
			sendMsg = Server.sendMsg(code, buffer);
			logger.info("wolflog---" + code + "号机状态信息已发送");
		} catch (Exception e) {
			logger.info("wolflog---" + "信息发送失败" + e.getMessage());
		}
		return sendMsg;
	}
	
	public static void send_0x28(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0x55;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x28;
		byte result = 0x01;
		byte data = 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "设备上传地理位置信息已回复");
	}
	
	public static void send_0x29(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xaa;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x29;
		byte result = 0x01;
		byte data = 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "服务器请求设备上传地理位置信息已发送");
	}
	
	public static void send_0x23(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xaa;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x23;
		byte result = 0x01;
		byte data = 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "服务器请求收到充电站主板的信息已发送");
	}
	
	public static void send_0x24(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xaa;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x25;
		byte result = 0x01;
		byte data = 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "服务器请求收到充电站主板的信息已发送");
	}
	
	public static void send_0x25(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xaa;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x23;
		byte result = 0x01;
		byte data = 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "服务器请求收到充电站主板的信息已发送");
	}
	
	public static void send_0x26(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0x55;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x26;
		byte result = 0x01;
		byte data = 0x00;
		byte sum = (byte) (len ^ cmd ^ result ^ data);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号设备新版上传端口的实时状态已回复");
	}
	
	public static void send_0x27(byte port, short money, short time, short elec, String code, byte chargeType) {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			byte sop = (byte) 0xAA;
			byte len = (byte) 0x0f;
			byte cmd = (byte) 0x27;
			byte result = (byte) 0x00;
			buffer.put(sop);
			buffer.put(len);
			buffer.put(cmd);
			buffer.put(result);
			buffer.put(port);
			buffer.putShort(money);
			buffer.putShort(time);
			buffer.putShort(elec);
			int session_id = (int) System.currentTimeMillis();
			buffer.putInt(session_id);
			buffer.put(chargeType);
			buffer.position(0);
			buffer.get();
			byte[] bytes = new byte[20];
			buffer.get(bytes, 0, len);
			byte sum = 0x00;
			for (byte b : bytes) {
				sum ^= b;
			}
			buffer.put(sum);
			buffer.flip();
//			while (buffer.hasRemaining()) {
//				System.out.printf("0x%02x ", buffer.get());
//			}
//			buffer.position(0);
			operRedisAllPortStatus(code, port, time, elec, "2");
			Server.sendMsg(code, buffer);
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					send_0x27dingshi(port, money, time, elec, code, chargeType, session_id);
				}
			}, 20000);
			Server.timerMap.put(session_id, timer);
			Server.timerNumMap.put(session_id, 2);
			logger.info("wolflog---" + code + "号机新版支付信息已发送" + "定时任务已开启:" + "session_id===" + session_id);
		} catch (Exception e) {
			logger.info("wolflog---" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss------").format(new Date()) + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void send_0x27dingshi(byte port, short money, short time, short elec, String code, byte chargeType
			, int session_id) {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			byte sop = (byte) 0xAA;
			byte len = (byte) 0x0f;
			byte cmd = (byte) 0x27;
			byte result = (byte) 0x00;
			buffer.put(sop);
			buffer.put(len);
			buffer.put(cmd);
			buffer.put(result);
			buffer.put(port);
			buffer.putShort(money);
			buffer.putShort(time);
			buffer.putShort(elec);
			buffer.putInt(session_id);
			buffer.put(chargeType);
			buffer.position(0);
			buffer.get();
			byte[] bytes = new byte[20];
			buffer.get(bytes, 0, len);
			byte sum = 0x00;
			for (byte b : bytes) {
				sum ^= b;
			}
			buffer.put(sum);
			buffer.flip();
//			while (buffer.hasRemaining()) {
//				System.out.printf("0x%02x ", buffer.get());
//			}
//			buffer.position(0);
			Server.sendMsg(code, buffer);
			Integer num = Server.timerNumMap.get(session_id);
			logger.info("wolflog---" + code + "号机新版支付信息已发送---" + "num===" + num + "---session_id===" + session_id);
			if (num != null && num > 1) {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						send_0x27dingshi(port, money, time, elec, code, chargeType, session_id);
					}
				}, 20000);
				logger.info("wolflog---" + "定时任务已开启");
				num = num - 1;
				Server.timerNumMap.put(session_id, num);
			}
		} catch (Exception e) {
			logger.info("wolflog---" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss------").format(new Date()) + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void send_0x2c(String code, byte port, int session_id) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0x55;
		byte len = (byte) 0x08;
		byte cmd = (byte) 0x2c;
		byte result = 0x01;
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(port);
		buffer.putInt(session_id);
		buffer.position(1);
		byte[] bytes = new byte[10];
		buffer.get(bytes, 0, len);
		byte sum = 0x00;
		for (byte b : bytes) {
			sum ^= b;
		}
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号设备新版提交充电结束状态数据已回复");
	}
	
	public static void send_0x2d(String code, int session_id) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0x55;
		byte len = (byte) 0x08;
		byte cmd = (byte) 0x2d;
		byte result = 0x01;
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put((byte) 0x00);
		buffer.putInt(session_id);
		buffer.position(1);
		byte[] bytes = new byte[10];
		buffer.get(bytes, 0, len);
		byte sum = 0x00;
		for (byte b : bytes) {
			sum ^= b;
		}
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号设备新版上报开始充电信息数据已回复");
	}
	
	public static void send_0x2a(String code,short cst,short elec_pri) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xaa;
		byte len = (byte) 0x07;
		byte cmd = (byte) 0x2a;
		byte result = 0x01;
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.putShort(cst);
		buffer.putShort(elec_pri);
		buffer.position(1);
		byte[] bytes = new byte[10];
		buffer.get(bytes, 0, len);
		byte sum = 0x00;
		for (byte b : bytes) {
			sum ^= b;
		}
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号设备新版设置系统参数已发送");
	}
	
	public static void send_0x2b(String code) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xaa;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x2b;
		byte result = 0x01;
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put((byte) 0x00);
		buffer.position(1);
		byte[] bytes = new byte[10];
		buffer.get(bytes, 0, len);
		byte sum = 0x00;
		for (byte b : bytes) {
			sum ^= b;
		}
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号设备新版读取系统参数已发送");
	}
	
	public static void send_0x2E(String code,byte port) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0xaa;
		byte len = (byte) 0x04;
		byte cmd = (byte) 0x2e;
		byte result = 0x01;
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(port);
		buffer.position(1);
		byte[] bytes = new byte[10];
		buffer.get(bytes, 0, len);
		byte sum = 0x00;
		for (byte b : bytes) {
			sum ^= b;
		}
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号设备新版查询当前的充电状态已发送");
	}
	
	public static void send_0x2F(String code,byte port) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		String str = CommonConfig.ZIZHUCHARGES + "oauth2Portpay?codeAndPort=" + code + port;
		byte[] strBytes = str.getBytes();
		byte[] strfubytes = new byte[70];
		strfubytes = buquan70wei(strfubytes, strBytes);
		byte sop = (byte) 0x55;
		byte len = (byte) 0x4d;
		byte cmd = (byte) 0x2f;
		byte result = 0x01;
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.putInt(Integer.parseInt(code));
		for (byte b : strfubytes) {
			buffer.put(b);
		}
		buffer.position(1);
		byte[] bytes = new byte[100];
		buffer.get(bytes, 0, len);
		byte sum = 0x00;
		for (byte b : bytes) {
			sum ^= b;
		}
		buffer.put(sum);
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server.sendMsg(code, buffer);
		logger.info("wolflog---" + code + "号设备获取主机二维码已回复");
	}
	
//	public static void send_0x2F(String code) {
//		ByteBuffer buffer = ByteBuffer.allocate(1024);
//		byte sop = (byte) 0xaa;
//		byte len = (byte) 0x04;
//		byte cmd = (byte) 0x2f;
//		byte result = 0x01;
//		byte data = 0x01;
//		buffer.put(sop);
//		buffer.put(len);
//		buffer.put(cmd);
//		buffer.put(result);
//		buffer.put(data);
//		buffer.position(1);
//		byte[] bytes = new byte[10];
//		buffer.get(bytes, 0, len);
//		byte sum = 0x00;
//		for (byte b : bytes) {
//			sum ^= b;
//		}
//		buffer.put(sum);
//		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
//		Server.sendMsg(code, buffer);
//		logger.info("wolflog---" + code + "号设备通知设备准备升级已发送");
//	}

	// ---------------------------------below is parse
	// data-----------------------------------------------

	/**
	 * parse reply message
	 * 
	 * @return map
	 */
	public static Map<String, Map> parse_1(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Map<String, Map> allMap = new HashMap<>();
		HashMap<String, String> map = new HashMap<>();
		byte[] data = new byte[1024];
		buffer.get(data, 0, len - 3);
		System.out.printf("0x%02x ", data[0]);
		System.out.printf("0x%02x ", data[1]);
		map.put("param1", data[0] + "");
		map.put("param2", data[1] + "");
		byte sum = buffer.get();
		System.out.printf("0x%02x ", sum);
		String ipaddr = "";
		try {
			ipaddr = channel.getRemoteAddress().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		logger.info("wolflog---" + code + "号机查询所有充电站端口信息已接收");
//		byte[] wolfbyte = new byte[] { len, cmd, result, data[0], data[1] };
//		byte checkoutSum = checkoutSum(wolfbyte);
//		logger.info("wolflog---" + sum + "------" + checkoutSum);
//		if (sum == checkoutSum) {
//			logger.info("wolflog---" + "数据发送验证成功");
//		} else {
//			logger.info("wolflog---" + "数据发送验证失败");
//		}
		allMap.put(code, map);
		return allMap;
	}

	public static Map<String, Map> parse_2(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Map<String, Map> allMap = new HashMap<>();
		HashMap<String, String> map = new HashMap<>();
		byte[] data = new byte[12];
		buffer.get(data, 0, len - 3);
		System.out.printf("0x%02x ", data[0]);
		/*
		 * Charset cs = Charset.forName ("GBK"); CharBuffer cb =
		 * cs.decode(buffer.); String msg = cb.toString();
		 */
		for (int i = 1; i < data.length; i++) {
			if (data[i] == 0) {
				break;
			}
			System.out.printf("0x%02x ", data[i]);
			map.put("param" + data[i], "是");
		}
		byte sum = buffer.get();
		System.out.printf("0x%02x ", sum);
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		logger.info("wolflog---" + code + "号机查询所有空闲（可用）的充电站端口信息已接收");
//		byte[] verfiy = new byte[] { len, cmd, result, data[0], data[1], data[2], data[3], data[4], data[5], data[6],
//				data[7], data[8], data[9], data[10] };
//		byte checkoutSum = checkoutSum(verfiy);
//		logger.info("wolflog---" + sum + "------" + checkoutSum);
//		if (sum == checkoutSum) {
//			logger.info("wolflog---" + "数据发送验证成功");
//		} else {
//			logger.info("wolflog---" + "数据发送验证失败");
//		}
		allMap.put(code, map);
		return allMap;
	}

	public Map<String, Map> parse_0x14(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Map<String, Map> allMap = new HashMap<>();
		HashMap<String, String> map = new HashMap<>();
		byte[] data = new byte[1024];
		buffer.get(data, 0, len - 3);
		if (data[1] == 1) {
			map.put("param" + getClickpayport(), "充电成功");
		} else if (data[1] == 11) {
			map.put("param" + getClickpayport(), "充电站故障");
		} else if (data[1] == 12) {
			map.put("param" + getClickpayport(), "端口已经被使用");
		}
		for (byte b : data) {
			if (b == 0) {
				break;
			}
			System.out.printf("0x%02x ", b);
		}
		byte sum = buffer.get();
		System.out.printf("0x%02x ", sum);
		byte port = data[0];
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		Server.beginChargeInfoMap.put("updatetime" + code + port, System.currentTimeMillis() + "");
		logger.info(code + "号机" + port + "端口用户付款成功信息已接收");
		ChargeRecordHandler.updateChargeResultinfo(code, port);
//		chargeRecordService.updateChargeResultinfo(code, port);
//		byte[] verfiy = new byte[] { len, cmd, result, data[0], data[1] };
//		byte checkoutSum = checkoutSum(verfiy);
//		if (sum == checkoutSum) {
//			logger.info("wolflog---" + "数据发送验证成功");
//		} else {
//			logger.info("wolflog---" + "数据发送验证失败");
//		}
		allMap.put(code, map);
		return allMap;
	}

	public static Map<String, Map> parse_5(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Map<String, Map> allMap = new HashMap<>();
		HashMap<String, String> map = new HashMap<>();
		byte port = buffer.get();
		System.out.printf("0x%02x ", port);
		short time = buffer.getShort();
		System.out.printf("0x%02x ", time);
		short power = buffer.getShort();
		System.out.printf("0x%02x ", power);
		byte sum = buffer.get();
		System.out.printf("0x%02x ", sum);
		// byte[] checks = new byte[] { len, cmd, result, port, time, power };
		// byte checkoutSun = checkoutSum(checks);
		map.put("param5", "端口：" + port + " , 剩余时间：" + time + "分钟, 当前充电功率：" + power + "W");
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		logger.info("wolflog---" + code + "号机查询当前的充电状态信息已接收");
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.put(len);
		byteBuffer.put(cmd);
		byteBuffer.put(result);
		byteBuffer.put(port);
		byteBuffer.putShort(time);
		byteBuffer.putShort(power);
		byteBuffer.flip();
//		byte[] datas = new byte[100];
//		byteBuffer.get(datas, 0, len);
//		byte checkoutSum = checkoutSum(datas);
//		if (sum == checkoutSum) {
//			logger.info("wolflog---" + "数据发送验证成功");
//		} else {
//			logger.info("wolflog---" + "数据发送验证失败");
//		}
		allMap.put(code, map);
		return allMap;
	}

	public static Map<String, Map> parse_7(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Map<String, Map> allMap = new HashMap<>();
		HashMap<String, String> map = new HashMap<>();
		short card_money = buffer.getShort();
		System.out.printf("0x%02x ", card_money);
		short coin_money = buffer.getShort();
		System.out.printf("0x%02x ", coin_money);
		byte sum = buffer.get();
		System.out.printf("0x%02x ", sum);
		map.put("param6", "" + (double) card_money / 10);
		map.put("param7", "" + coin_money);
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		logger.info("wolflog---" + code + "号机查询消费总额数据信息已接收");
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.put(len);
		byteBuffer.put(cmd);
		byteBuffer.put(result);
		byteBuffer.putShort(card_money);
		byteBuffer.putShort(coin_money);
		byteBuffer.flip();
//		byte[] datas = new byte[100];
//		byteBuffer.get(datas, 0, len);
//		byte checkoutSum = checkoutSum(datas);
//		if (sum == checkoutSum) {
//			logger.info("wolflog---" + "数据发送验证成功");
//		} else {
//			logger.info("wolflog---" + "数据发送验证失败");
//		}
		allMap.put(code, map);
		return allMap;
	}

	public static void parse_9(AsynchronousSocketChannel channel, byte len, byte cmd, byte result, ByteBuffer buffer) {
		byte data = buffer.get();
		System.out.printf("0x%02x ", data);
		byte sum = buffer.get();
		System.out.printf("0x%02x ", sum);
		SETCOINICFLAG = true;
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		logger.info("wolflog---" + code + "号机设置IC卡、投币器是否可用信息已接收");
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.put(len);
		byteBuffer.put(cmd);
		byteBuffer.put(result);
		byteBuffer.put(data);
		byteBuffer.flip();
//		byte[] datas = new byte[100];
//		byteBuffer.get(datas, 0, len);
//		byte checkoutSum = checkoutSum(datas);
//		if (sum == checkoutSum) {
//			logger.info("wolflog---" + "数据发送验证成功");
//		} else {
//			logger.info("wolflog---" + "数据发送验证失败");
//		}
	}

	public static void parse_10(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
//		Map<String, Map> allMap = new HashMap<>();
//		HashMap<String, String> map = new HashMap<>();
		byte port = buffer.get();
		short error_code = buffer.getShort();
		byte sum = buffer.get();
		String code = SendMsgUtil.getCodeByIpaddr(channel);
//		logger.info("wolflog---" + code + "号机上传设备故障已接收");
		String errorinfo = "";
		if (error_code == 1) {
			errorinfo = "疑似继电器粘连";
		} else {
			errorinfo = "其他错误";
		}
		deviceStatusFeedback(code, port + 0, errorinfo);
		logger.info(code + "号机端口：" + port + ", 错误码：" + error_code + "上传设备故障已接收");
//		map.put("param10", "端口：" + port + ", 错误码：" + error_code);
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.put(len);
		byteBuffer.put(cmd);
		byteBuffer.put(result);
		byteBuffer.put(port);
		byteBuffer.putShort(error_code);
		byteBuffer.flip();
		byte[] datas = new byte[100];
		byteBuffer.get(datas, 0, len);
		byte checkoutSum = checkoutSum(datas);
		if (sum == checkoutSum) {
			logger.info("wolflog---" + "数据发送验证成功");
		} else {
			logger.info("wolflog---" + "数据发送验证失败");
		}
		send_0x10(code);
//		allMap.put(code, map);
//		return allMap;
	}

	public static void parse_11(AsynchronousSocketChannel channel, byte len, byte cmd, byte result, ByteBuffer buffer) {
		byte data = buffer.get();
		byte sum = buffer.get();
		System.out.printf("0x%02x ", sum);
		SETCOINICFLAG = true;
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		logger.info("wolflog---" + code + "号机设备获取当前时间信息已接收");
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.put(len);
		byteBuffer.put(cmd);
		byteBuffer.put(result);
		byteBuffer.put(data);
		byteBuffer.flip();
		byte[] datas = new byte[100];
		byteBuffer.get(datas, 0, len);
		byte checkoutSum = checkoutSum(datas);
		if (sum == checkoutSum) {
			logger.info("wolflog---" + "数据发送验证成功");
		} else {
			logger.info("wolflog---" + "数据发送验证失败");
		}
	}

	public static Map<String, Map<String, Map<String, String>>> parse_12(AsynchronousSocketChannel channel, byte len,
			byte cmd, byte result, ByteBuffer buffer) {
		Map<String, Map<String, Map<String, String>>> map = new HashMap<>();
		Map<String, Map<String, String>> codemap = new HashMap<>();
		Map<String, String> portmap = new HashMap<>();
		byte data = buffer.get();
		byte sum = buffer.get();
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		long currentTimeMillis = System.currentTimeMillis();
		portmap.put("updatetime", currentTimeMillis + "");
		codemap.put("port" + data, portmap);
		logger.info("wolflog---" + code + "号机锁定解锁信息已接收-" + "端口---" + data + ", 当前时间为-：" + currentTimeMillis);
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.put(len);
		byteBuffer.put(cmd);
		byteBuffer.put(result);
		byteBuffer.put(data);
		byteBuffer.flip();
//		byte[] datas = new byte[100];
//		byteBuffer.get(datas, 0, len);
//		byte checkoutSum = checkoutSum(datas);
//		if (sum == checkoutSum) {
//			logger.info("wolflog---" + "数据发送验证成功");
//		} else {
//			logger.info("wolflog---" + "数据发送验证失败");
//		}
		map.put(code, codemap);
		return map;
	}

	public static Map<String, Map<String, Map<String, String>>> parse_13(AsynchronousSocketChannel channel, byte len,
			byte cmd, byte result, ByteBuffer buffer) {
		Map<String, Map<String, Map<String, String>>> allMap = new HashMap<>();
		Map<String, Map<String, String>> portmap = new HashMap<>();
		Map<String, String> statusmap = new HashMap<>();
		byte port = buffer.get();
		short time = buffer.getShort();
		byte sum = buffer.get();
		// map.put("param13", "剩余时间为： " + time + " 分钟");
		statusmap.put("updatetime", System.currentTimeMillis() + "");
		statusmap.put("surpTime", time + "");
		portmap.put("port" + port, statusmap);
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		logger.info("wolflog---" + code + "号机远程停止" + port + "号端口的充电信息已接收");
		try {
//			AllPortStatusHandler.updateAllPortStatus(code, port, (byte) 1, (short) 0, (short) 0, (short) 0, 0.0, 0.0);
			operRedisAllPortStatus(code, port, time, 0, "1");
		} catch (Exception e) {
			logger.warn("数据库不存在" + code + "号设备");
		}
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.put(len);
		byteBuffer.put(cmd);
		byteBuffer.put(result);
		byteBuffer.put(port);
		byteBuffer.putShort(time);
		byteBuffer.flip();
//		byte[] datas = new byte[100];
//		byteBuffer.get(datas, 0, len);
//		byte checkoutSum = checkoutSum(datas);
//		if (sum == checkoutSum) {
//			logger.info("wolflog---" + "数据发送验证成功");
//		} else {
//			logger.info("wolflog---" + "数据发送验证失败");
//		}
		allMap.put(code, portmap);
		return allMap;
	}

	public static Map<String, String> parse_15(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		// Map<String, Map> allMap = new HashMap<>();
		Map<String, String> map = new HashMap<>();
		byte port_num = buffer.get();
		byte[] data = new byte[port_num * 2];
		if (port_num != 0) {
			buffer.get(data, 0, len - 4);
			byte temp = 1;
			for (int i = 1; i < data.length; i += 2) {
				if (data[i] == 1) {
					map.put("param" + temp, "空闲");
				} else if (data[i] == 2) {
					map.put("param" + temp, "使用");
				} else if (data[i] == 3) {
					map.put("param" + temp, "禁用");
				} else if (data[i] == 4) {
					map.put("param" + temp, "故障");
				}
				temp++;
			}
		} else {
			// map.put("param11", "无");
		}
		map.put("paramdate", System.currentTimeMillis() + "");
//		for (byte b : data) {
//			System.out.printf("0x%02x ", b);
//		}
		byte sum = buffer.get();
		buffer.position(1);
//		byte[] datas = new byte[100];
//		buffer.get(datas, 0, len);
//		byte checkoutSum = checkoutSum(datas);
		String code = getCodeByIpaddr(channel);
		logger.info("wolflog---" + code + "号机读取设备每个端口状态信息已接收");
//		if (sum == checkoutSum) {
//			logger.info("wolflog---" + "数据发送验证成功");
//		} else {
//			logger.info("wolflog---" + "数据发送验证失败");
//		}
		// allMap.put(code, map);
		Server.everyportMap.put(code, map);
		return map;
	}

	// 在线卡 卡号、金额、消费类型
	public Map<String, Object> cardPara(String card_id, byte card_cst, byte card_ope,String code) throws SQLException {
		OnlineCard card = OnlineCardHandler.cardIsExist(card_id);
		Map<String, Object> map = new HashMap<>();
		Integer merid = Equipmenthandler.findUserEquipment(code);
		Byte res = 0;
		Short card_surp = 0;
		int changeMoney = 0xff & card_cst;
		logger.info(card_id + "输出操作金额==========="+changeMoney);
		if (card == null) {// 插入操作（插入卡片记录）
			logger.info(card_id + "===========无卡");
			res = 2;
		} else {// 更改操作
			logger.info(card_id + "===========有卡继续");
			Integer status = card.getStatus();
			Integer uid = CommUtil.toInteger(card.getUid());
			Integer cardmerid = CommUtil.toInteger(card.getMerid());
			//数据处理
			//============================================================================
			String cardID = card_id;
			Double topupmoney = CommUtil.toDouble(card.getMoney());//充值金额
			Double sendmoney = CommUtil.toDouble(card.getSendmoney());//赠送金额
			Integer relevawalt = CommUtil.toInteger(card.getRelevawalt());
			if(relevawalt==1){
				User user = UserHandler.getUserInfo(uid);
				topupmoney = CommUtil.toDouble(user.getBalance());
				sendmoney = CommUtil.toDouble(user.getSendmoney());
			}
			logger.info(card_id + "输出消费金额=（角）=========="+changeMoney);
			Double consumemoney = CommUtil.toDouble(changeMoney)/10;//消费金额
			Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额
			String ordernum = HttpRequest.createOrdernum(6);
			if (status == 1 && card_cst != 0) {// 卡状态正常
				Integer operationtype = CommUtil.toInteger(card_ope);
//				Map<String, Object> disposeDatas = onlinecardDataDisposes(topupmoney, sendmoney, 
//				consumemoney, relevawalt, operationtype, uid,  merid, ordernum, cardID, code);
				//============================================================================
				//cardopetype  1:消费   2:回收
				Integer cardopetype = 1;
				if(card_ope==1) cardopetype = 2;
				Map<String, Object> disposeData = onlinecardDataDispose(topupmoney, sendmoney, consumemoney, cardopetype);
				//============================================================================
				Double opermoney = CommUtil.toDouble(disposeData.get("opermoney"));//操作总额
				Double opertopupmoney = CommUtil.toDouble(disposeData.get("opertopupmoney"));	//操作充值金额
				Double opersendmoney = CommUtil.toDouble(disposeData.get("opersendmoney"));	//操作赠送金额
				//==========================================================================
				Double topupbalance = CommUtil.toDouble(disposeData.get("topupbalance"));//充值余额
				Double sendbalance = CommUtil.toDouble(disposeData.get("sendbalance"));//赠送余额
				Double accountbalance = CommUtil.toDouble(disposeData.get("accountbalance"));//账户余额
				//==========================================================================
				if (card_ope == 1) {// 退费(回收)
					if(relevawalt==2){//不关联钱包
						OnlineCardHandler.updateCardMoney(opertopupmoney, cardID, 1, sendbalance);
						OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 2, 1);
						logger.info("wolflog---" + "当前卡号为退费IC：" + card_id + "---实时操作金额为：" + opermoney + "元");
					}else{//关联钱包
						//修改用户钱包余额
						UserHandler.updateWalltMoney(opertopupmoney, opersendmoney, 1, uid);
						//添加用户明细记录
						UserHandler.addGenDetail(uid, merid, ordernum, opertopupmoney, opersendmoney, 
							opermoney, accountbalance, topupbalance, sendbalance , 5, cardID);
						//添加在线卡记录
						OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, 
							opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 2, 1);
						logger.info("wolflog---" + "当前卡号为退费：" + card_id + "---实时操作金额为：" + opermoney + "元");
					}
					res = 0;
				} else if (card_ope == 0) {//扣费（消费）
					Double comparemoney = CommUtil.subBig(accountmoney, opermoney); //总额、操作比较金额差
					if(comparemoney>=0){//金额足够，扣费
						if(relevawalt==2){//不关联钱包
							res = 0;
							OnlineCardHandler.updateCardMoney(opertopupmoney, cardID, 0, sendbalance);
							OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 1, 1);
							logger.info("wolflog---" + "当前卡号为退费IC：" + card_id + "---实时扣费金额为：" + opermoney + "元");
						}else{//关联钱包
							//修改用户钱包余额
							UserHandler.updateWalltMoney(opertopupmoney, opersendmoney, 0, uid);
							//添加用户明细记录
							UserHandler.addGenDetail(uid, merid, ordernum, opertopupmoney, opersendmoney, 
									opermoney, accountbalance, topupbalance, sendbalance , 9, cardID);
							//添加在线卡记录
							OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, 
									opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 1, 1);
							logger.info("wolflog---" + "当前卡号为退费：" + card_id + "---实时操作金额为：" + opermoney + "元");
						}
					}else{//金额不够，返回
						res = 1;
					}
				}
				card_surp = (short) (accountbalance * 10);
			} else if (status == 0) {//激活在线卡
				if (merid != null && relevawalt==2) {
					OnlineCardHandler.activeCard(merid,card_id);//更改卡状态为正常
					//添加操作记录
					OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, card_id,
							code, accountmoney, 0.00, 0.00, 0.00, topupmoney, sendmoney, 4, 2);
					logger.info(card_id + "激活卡成功");
					if (topupmoney > 0) {//存在充值金额，更改
						OnlineCardRecord cardRecord = OnlineCardHandler.getCardRecordEndByCardID(uid, card_id);
						if (cardRecord != null) {
							String operordernum = cardRecord.getOrdernum();
							Double opertopupmoney = CommUtil.toDouble(cardRecord.getMoney());
							Integer tradeid = OnlineCardHandler.getTradeRecordByOrdernum(operordernum);
							if (tradeid == 0 || tradeid == null) {
								logger.info("激活卡开始");
								Integer aid = Equipmenthandler.getEquipmentByCode(code);
								logger.info("激活卡: "+cardID+"   查询激活时是否存在小区  "+aid);
								String resultli = null;
								Double partmoney = 0.00;
								List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
								if (aid != null && aid != 0) partInfo = AreaHandler.getPartnerInfo(aid, 2);
								
								logger.info("激活卡 商户分成开始");
								//商户收益（分成）计算
								Map<String, Object> listmap = partnerIncomeDispose(partInfo, merid, opertopupmoney, ordernum, new Date(),  MerchantDetail.ONLINESOURCE, 2, MerchantDetail.NORMAL);
								partmoney = CommUtil.toDouble(listmap.get("partmoney"));
								resultli = CommUtil.toString(listmap.get("json"));
								Double mermoney = CommUtil.subBig(opertopupmoney, partmoney);
								Integer manid = aid;
								logger.info("激活卡 商户分成完成");
								TradeRecordHandler.addTradeRecordInfo(merid, manid, uid, ordernum, opertopupmoney, mermoney, partmoney, card_id, 5, 2, 1, null, resultli);
								logger.info("激活卡打印007");
								OnlineCardHandler.updateCardRecordMid(ordernum, merid);
							} else {
								logger.info("二次激活卡，无添加金额");
							}
						}
					}
				} else {
					logger.info(card_id + "激活卡失败");
				}
				res = 2;
				card_surp = (short) (accountmoney * 10);
			} else {// 卡状态不对
				res = 2;
				if (card_cst == 0) {
					res = 0;
				}
				card_surp = (short) (accountmoney * 10);
			}
		}
		map.put("RES", res);
		map.put("CARD_SURP", card_surp);
		return map;
	}

	

	public Map<String, Map> parse_16(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Map<String, Map> allMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		int card_id = buffer.getInt();
		byte card_cst = buffer.get();
		byte card_ope = buffer.get();
		byte sum = buffer.get();
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		String hexCard_id = Integer.toHexString(card_id);
		try {
			map = cardPara(buquan8wei(hexCard_id).toUpperCase(), card_cst, card_ope, code);// 卡号、金额、消费类型
		} catch (SQLException e1) {
			logger.info("wolflog---" + "在线卡信息错误");
			e1.printStackTrace();
		}
		logger.info("wolflog---" + code + "号机在线卡上传卡号，预扣费信息已接收");
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.put(len);
		byteBuffer.put(cmd);
		byteBuffer.put(result);
		byteBuffer.putInt(card_id);
		byteBuffer.put(card_cst);
		byteBuffer.put(card_ope);
		byteBuffer.flip();
//		byte[] datas = new byte[100];
//		byteBuffer.get(datas, 0, len);
//		byte checkoutSum = checkoutSum(datas);
//		if (sum == checkoutSum) {
//			logger.info("wolflog---" + "数据发送验证成功");
//		} else {
//			logger.info("wolflog---" + "数据发送验证失败");
//		}
		allMap.put(code, map);
		allMap.put("goback", map);
		return allMap;
	}

	/**
	 * WJS
	 * @Description： 充电结束
	 * @author： 
	 */
	public static void parse_22(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Map<String, String> map = new HashMap<>();
		Equipment equipment = getCodeHardverByIpaddr(channel);
		if (equipment != null) {
			String code = equipment.getCode();
			String hardversion = equipment.getHardversion();
			byte port = buffer.get();
			map.put("port", port + "");
			short time = buffer.getShort();
			short elec = buffer.getShort();
			byte reason = buffer.get();
			byte sum = buffer.get();
			String str = "";
			if (code == null || "".equals(code)) {
				return;
			}
			send_22(port, code);
			logger.info(code + "号机" + port + "端口提交充电结束状态信息已接收");
			Integer endChargeId = ChargeRecordHandler.getEndChargeRecord(code, port + 0);
			if (endChargeId == 0) {
//				AllPortStatusHandler.updateAllPortStatus(code, port, (byte) 1, (short) 0, (short) 0, (short) 0, 0.0, 0.0);
				operRedisAllPortStatus(code, port, 0, 0, "1");
				return;
			} else if (endChargeId == -1) {
//				AllPortStatusHandler.updateAllPortStatus(code, port, (byte) 1, (short) 0, (short) 0, (short) 0, 0.0, 0.0);
				return;
			}
			List<ChargeRecord> chargeList = ChargeRecordHandler.getChargeListUnfinish(endChargeId);
			ChargeRecord chargeRecord = chargeList.get(chargeList.size() - 1);
			if (DisposeUtil.checkIfHasV3(hardversion) && (chargeRecord.getPaytype()==1 || chargeRecord.getPaytype()==2)) {
				v3DisposeData(chargeRecord, code, port, endChargeId, time, reason);
			} else if (reason == 0) {
				str = "购买的充电时或者电量用完了";
				if (endChargeId != 0) {
					logger.info("wolflog---" + "订单号===" + chargeRecord.getOrdernum() + "结束充电，原因===" + reason);
					int allTime = 0;
					int allelec = 0;
					for (ChargeRecord charge : chargeList) {
						allTime += Integer.parseInt(charge.getDurationtime());
						allelec += charge.getQuantity();
					}
					long currentTime = System.currentTimeMillis();
					long begintime = chargeRecord.getBegintime().getTime();
					if (((currentTime - begintime) > 0) && (currentTime - begintime) < (allTime * 60 * 1000 + 120000)) {
						//TODO 充电结束时间
						Integer uid = CommUtil.toInteger(chargeRecord.getUid());
						Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
						Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
						Double consumemoney = paymoney;//购买的充电时或者电量用完了，没有退费
						Integer orderid = CommUtil.toInteger(chargeRecord.getId());
						String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
						String deviceport = CommUtil.toString(port);
						Integer resultinfo = CommUtil.toInteger(reason);
						String startTime = CommUtil.toDateTime(chargeRecord.getBegintime());
						String endTime = CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date());
						String chargetime = "开始时间："+startTime + "\r\n结束时间：" + endTime;
						logger.info("wolflog---" + "001   "+chargetime);
						String devicenum = code;
						Equipment devicedata = Equipmenthandler.getEquipmentData(devicenum);
						Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
						Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
						String devicename = CommUtil.toString(devicedata.getRemark());
						String areaname = CommUtil.toString(devicedata.getName());
						String servicphone = getServicePhone(devicetempid, deviceaid, merid);
						TempMsgUtil.endChargeSendMess("尊敬的用户，您的充电已经结束。", ordernum, orderid, resultinfo, merid, uid, consumemoney, chargetime, 
								devicenum, deviceport, devicename, deviceaid, areaname, servicphone);
					}
					if (((currentTime - begintime) > 0)
							&& ((currentTime - begintime) < (allTime * 60 * 1000 + 120000))) {
						Equipmenthandler.addRealchargerecord(endChargeId, chargeRecord.getUid(), chargeRecord.getMerchantid(), code, port, time, elec, 0, -1.0, -1.0, -1.0);
						if ((time <= allTime) && (elec <= allelec)) {
							int realusetime = (int) ((System.currentTimeMillis() - begintime) / 1000 / 60);
							int elecInt = elec & 0xffff;
							Integer surpElec = null;
							if (elecInt <= allelec) {
								surpElec = allelec - elecInt;
							}
							if (allTime > realusetime) {
								ChargeRecordHandler.updateChargeEndInfo(endChargeId, realusetime, surpElec, reason + 0);
							} else {
								ChargeRecordHandler.updateChargeEndInfo(endChargeId, allTime - time, surpElec, reason + 0);
							}
						} else {
							ChargeRecordHandler.updateChargeRecordStopCharging(endChargeId, reason + 0);
						}
					} else {
						ChargeRecordHandler.updateChargeRecordStopCharging(endChargeId, reason + 0);
					}
				}
			} else {
//				 if (reason == 1 || reason == 2 || reason == 3 || reason == 0x0B)
				if (endChargeId != 0) {
					logger.info("wolflog---" + "订单号===" + chargeRecord.getOrdernum() + "结束充电，原因===" + reason);
					long currentTime = System.currentTimeMillis();
					Date begintimeDate = chargeRecord.getBegintime();
					long begintime = begintimeDate == null ? (currentTime + 50) : begintimeDate.getTime();
					if (chargeRecord.getNumber() != null && chargeRecord.getNumber() != 1) {
						Double money = chargeRecord.getExpenditure();
						Integer merchantid = chargeRecord.getMerchantid();
						Integer uid = chargeRecord.getUid();
//					Double userBalance = ChargeRecordHandler.getUserBalance(uid);
						User touser = UserHandler.getUserInfo(uid);
						touser = touser == null ? new User() : touser;
						Double userBalance = CommUtil.toDouble(touser.getBalance());
						Double usersendmoney = CommUtil.toDouble(touser.getSendmoney());
						Integer paytype = chargeRecord.getPaytype();
						short maxPower = Equipmenthandler.queryRealchargerecordMaxPower(endChargeId);
						if ((currentTime - begintime) > 0 && ((currentTime - begintime) <= 180000 || 
								((currentTime - begintime) < 600000 && maxPower <= 20))) {
							if (paytype == 4) {
								logger.info("wolflog---" + "包月下发充电数据退款");
								ChargeRecordHandler.updateChargeRefundNumer(1, endChargeId,
										chargeRecord.getExpenditure(), 0.0, 0, 0);
								PackageMonth packageMonth = UserHandler.getPackageMonth(uid);
								if (packageMonth != null) {
									Integer everydaynum = packageMonth.getEverydaynum();
									Integer everymonthnum = packageMonth.getEverymonthnum();
									if (everydaynum != 0 || everymonthnum != 0) {
										UserHandler.updatePackageMonth(everydaynum, everymonthnum, uid);
										UserHandler.addPackageMonthRecord(uid, chargeRecord.getOrdernum(), 
												money, packageMonth.getTodaysurpnum(), packageMonth.getSurpnum(), 
												Integer.parseInt(chargeRecord.getDurationtime()), 
												(chargeRecord.getQuantity() + 0.0)/100);
									}
								}
							} else if (paytype == 1) {//钱包
								for (ChargeRecord chargeRecord2 : chargeList) {
									if (chargeRecord2.getPaytype() == 1) {
										walletAllRefund(chargeRecord2, userBalance, usersendmoney, code, reason);
									}
								}
//								ChargeRecordHandler.updateUserMoney(money, uid, 2);
//								ChargeRecordHandler.updateChargeRefundNumer(1, endChargeId,
//										chargeRecord.getExpenditure(), money, 0, 0);
//								logger.info(chargeRecord.getOrdernum() + "钱包支付充电失败，已退款");
//								
//								Double opermoney = CommUtil.toDouble(chargeRecord.getExpenditure());
//								Double opersendmoney = 0.00;
//								Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
//								Double topupbalance = CommUtil.addBig(opermoney, userBalance);
//								Double givebalance = CommUtil.addBig(opersendmoney, usersendmoney);
//								Double operbalance = CommUtil.addBig(topupbalance, givebalance);
//								
//								Money moneyEntity = new Money();
//								moneyEntity.setUid(uid);
//								moneyEntity.setOrdernum(chargeRecord.getOrdernum());
//								moneyEntity.setPaytype(3);
//								moneyEntity.setStatus(1);
//								
//								moneyEntity.setMoney(opermoney);
//								moneyEntity.setSendmoney(opersendmoney);
//								moneyEntity.setTomoney(opertomoney);
//								moneyEntity.setBalance(operbalance);
//								moneyEntity.setTopupbalance(topupbalance);
//								moneyEntity.setGivebalance(givebalance);
//								
//								moneyEntity.setRemark("wallet");
//								ChargeRecordHandler.addMoneyRecord(moneyEntity);
//								UserHandler.addGenDetail(chargeRecord.getUid(), merchantid, chargeRecord.getOrdernum(),
//										opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, 5, null);
//								TradeRecordHandler.addTradeRecord(chargeRecord.getMerchantid(), 0,
//										chargeRecord.getUid(), chargeRecord.getOrdernum(),
//										chargeRecord.getExpenditure(), 0, 0, code, 1, 1, 2);
//								//TODO 发送消息    钱包付款 充电异常返回
//								uid = CommUtil.toInteger(uid);
//								Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
//								Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
//								Integer orderid = CommUtil.toInteger(chargeRecord.getId());
//								String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
//								String deviceport = CommUtil.toString(port);
//								Integer resultinfo = CommUtil.toInteger(reason);
//								String startTime = CommUtil.toDateTime(chargeRecord.getBegintime());
//								String endTime = CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date());
//								String chargetime = "开始时间："+startTime + "\r\n结束时间：" + endTime;
//								String devicenum = code;
//								Equipment devicedata = Equipmenthandler.getEquipmentData(devicenum);
//								Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
//								Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
//								String devicename = CommUtil.toString(devicedata.getRemark());
//								String areaname = CommUtil.toString(devicedata.getName());
//								String servicphone = getServicePhone(devicetempid, deviceaid, merid);
//								TempMsgUtil.endChargeSendMess("尊敬的用户，您的充电已经结束。", ordernum, orderid, resultinfo, merid, uid, paymoney, chargetime, 
//										devicenum, deviceport, devicename, deviceaid, areaname, servicphone);
							} else if (paytype == 2) {// 微信
								// 微信充电全额退费
								for (ChargeRecord chargeRecord2 : chargeList) {
									if (chargeRecord2.getPaytype() == 2) {
										wxpayAllRefund(chargeRecord2, reason, code);
									}
								}
//								WXPayConfigImpl config;
//								try {
//									config = WXPayConfigImpl.getInstance();
//									WXPay wxpay = new WXPay(config);
//									String total_fee = "";
//									String out_trade_no = "";
//									String moneyStr = String.valueOf(chargeRecord.getExpenditure() * 100);
//									int idx = moneyStr.lastIndexOf(".");
//									total_fee = moneyStr.substring(0, idx);
//									out_trade_no = chargeRecord.getOrdernum();// 退款订单号
//									// 根据充电订单号中商家id查询商家的信息
//									String subMer = UserHandler.selectSubMerConfig(chargeRecord.getMerchantid());
//									SortedMap<String, String> params = new TreeMap<>();
//									String subMchId = null;
//									// subMer为空表示为普通商户
//									if(subMer != null && !"".equals(subMer)){
//										subMchId = subMer;
//										logger.info("使用微信特约商户号:"+subMchId+"进行退款");
//									}else{
//										subMchId = WeiXinConfigParam.SUBMCHID;
//										logger.info("使用服务平台商户号:"+subMchId+"进行退款");
//									}
//									params.put("appid", WeiXinConfigParam.FUWUAPPID);
//									params.put("mch_id", WeiXinConfigParam.MCHID);
//									params.put("sub_mch_id", subMchId);
//									params.put("out_trade_no", out_trade_no);
//									params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
//									String sign = HttpRequest.createSign("UTF-8", params);
//									params.put("sign", sign);
//									String url = "https://api.mch.weixin.qq.com/pay/orderquery";
//									String canshu = HttpRequest.getRequestXml(params);
//									String sr = HttpRequest.sendPost(url, canshu);
//									Map<String, String> resultMap = XMLUtil.doXMLParse(sr);
//									if (resultMap.get("return_code").toString().equalsIgnoreCase("SUCCESS")) {
//										HashMap<String, String> data = new HashMap<String, String>();
//										data.put("appid", WeiXinConfigParam.FUWUAPPID);
//										data.put("mch_id", WeiXinConfigParam.MCHID);
//										data.put("sub_mch_id", subMchId);
//										data.put("transaction_id", resultMap.get("transaction_id"));
//										data.put("out_trade_no", out_trade_no);// 定单号
//										data.put("out_refund_no", "t" + out_trade_no);
//										data.put("total_fee", total_fee);
//										data.put("refund_fee", total_fee);
//										data.put("refund_fee_type", "CNY");
//										data.put("op_user_id", config.getMchID());
//										
//										try {
//											Map<String, String> r = wxpay.refund(data);
//											// 处理退款后的订单 成功
//											if ("SUCCESS".equals(r.get("result_code"))) {
//												// 根据退款订单查询交易记录
//												TradeRecord tradere = TradeRecordHandler.getTradereInfo(out_trade_no);
//												String comment = CommUtil.toString(tradere.getComment());
//												Integer manid = tradere.getManid();
//												// 根据设备号查询小区id
//												int aid = Equipmenthandler.getEquipmentByCode(code);
//												if (aid != 0) {
//													UserHandler.areaRefund(aid, money);
//												}
//												UserHandler.equRefund(code, money);
//												//TODO 充电退费
//												if(subMer != null && !"".equals(subMer)){
//													logger.info("开始统计微信特约商户:"+merchantid+"资金信息");
//													//查询特约商户余额
//													Double merEarnings = CommUtil.toDouble(UserHandler.getUserEarningsById(merchantid));
//													// 微信特约商户只更新收益信息
//													UserHandler.merAmountRefund(merchantid, money);
//													//添加商户余额明细
//													UserHandler.addMerDetail(merchantid, out_trade_no, money, merEarnings, MerchantDetail.CHARGESOURCE, paytype, MerchantDetail.REFUND);
//												}else{
//												// 计算合伙人分成
//												dealerIncomeRefund(comment, chargeRecord.getMerchantid(), money, out_trade_no, 
//														new Date(), MerchantDetail.CHARGESOURCE, paytype, MerchantDetail.REFUND);
//											}
//											// 添加交易记录
//											TradeRecordHandler.addTradeRecordInfo(chargeRecord.getMerchantid(),
//													manid, chargeRecord.getUid(), chargeRecord.getOrdernum(),
//													chargeRecord.getExpenditure(), tradere.getMermoney(),
//													tradere.getManmoney(), code, 1, 2, 2,tradere.getHardver(),comment);
//											// 更新充电退款数据
//											ChargeRecordHandler.updateChargeRefundNumer(1, endChargeId,
//													chargeRecord.getExpenditure(), 0.0, 0, 0);
//											uid = CommUtil.toInteger(uid);
//											// 参数
//											Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
//											Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
//											Integer orderid = CommUtil.toInteger(chargeRecord.getId());
//											String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
//											String deviceport = CommUtil.toString(port);
//											Integer resultinfo = CommUtil.toInteger(reason);
//											String startTime = CommUtil.toDateTime(chargeRecord.getBegintime());
//											String endTime = CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date());
//											String chargetime = "开始时间："+startTime + "\r\n结束时间：" + endTime;
//											
//											String devicenum = code;
//											Equipment devicedata = Equipmenthandler.getEquipmentData(devicenum);
//											Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
//											Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
//											String devicename = CommUtil.toString(devicedata.getRemark());
//											String areaname = CommUtil.toString(devicedata.getName());
//											String servicphone = getServicePhone(devicetempid, deviceaid, merid);
//											TempMsgUtil.endChargeSendMess("尊敬的用户，您的充电已经结束。", ordernum, orderid, resultinfo, merid, uid, paymoney, chargetime, 
//												devicenum, deviceport, devicename, deviceaid, areaname, servicphone);
//											
////											TempMsgUtil.finishchargesendmsg("您好，充电已完成 ，已退款", chargeRecord.getUid(),
////													chargeRecord.getMerchantid(), code, chargeRecord.getBegintime(),
////													new Date(), endChargeId);
//											}
//											if ("FAIL".equals(r.get("result_code"))) {
//												// ChargeRecordHandler.updateChargeRefundNumer(endChargeId);
//											}
//										} catch (Exception e) {
//											logger.error(chargeRecord.getOrdernum() + e.getMessage() + "微信退款失败");
//										}
//									}
//								} catch (Exception e) {
//									logger.error(chargeRecord.getOrdernum() + e.getMessage() + "微信退款失败");
//								}
//								// 结束充电微信发送通知
//								logger.info("wolflog---" + "微信支付充电失败，已退款");
							} else if (paytype == 3 || paytype == 8) {
								AlipayClient alipayClient = new DefaultAlipayClient(
										"https://openapi.alipay.com/gateway.do", AlipayConfig.FUWUPID,
										AlipayConfig.RSA_PRIVATE_KEY3, "json", "GBK", AlipayConfig.ALIPAY_PUBLIC_KEY3,
										"RSA2");
								if (paytype == 8) {
									alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPLETAPPID,
											AlipayConfig.APPLETRSA_PRIVATE_KEY, "json", "GBK", AlipayConfig.APPLETALIPAY_PUBLIC_KEY, "RSA2");
									paytype = 3;
								}
								AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
								request.setBizContent("{" + "\"out_trade_no\":\"" + chargeRecord.getOrdernum() + "\","
										+ "\"refund_amount\":" + chargeRecord.getExpenditure() + "  }");
								
								AlipayTradeRefundResponse response;
								try {
									response = alipayClient.execute(request);
									if (response.isSuccess()) {//TODO  origin RZC  支付宝充电异常 自动退费处理
										logger.error("支付宝支付充电成功，已退款");
										TradeRecord tradere = TradeRecordHandler.getTradereInfo(chargeRecord.getOrdernum());
										String comment = CommUtil.toString(tradere.getComment());
										Integer manid = tradere.getManid();
										int aid = Equipmenthandler.getEquipmentByCode(code);
										if (aid != 0) {
											UserHandler.areaRefund(aid, money);
										}
										UserHandler.equRefund(code, money);
										//TODO 充电退费
										dealerIncomeRefund(comment, chargeRecord.getMerchantid(), money, chargeRecord.getOrdernum(), 
												new Date(), MerchantDetail.CHARGESOURCE, paytype, MerchantDetail.REFUND);
										logger.error("支付宝支付充电成功，已退款");
										//TODO 添加交易记录
										TradeRecordHandler.addTradeRecordInfo(chargeRecord.getMerchantid(),
												0, chargeRecord.getUid(), chargeRecord.getOrdernum(),
												chargeRecord.getExpenditure(), tradere.getMermoney(),
												tradere.getManmoney(), code, 1, 3, 2,tradere.getHardver(),comment);
										ChargeRecordHandler.updateChargeRefundNumer(1, endChargeId,
												chargeRecord.getExpenditure(), money, 0, 0);
									} else {
										logger.error(chargeRecord.getOrdernum() + "支付宝支付充电失败");
									}
								} catch (AlipayApiException e) {
									logger.error(chargeRecord.getOrdernum() + e.getMessage() + "支付宝支付充电失败");
								}
							} else {
								logger.warn("根据实用充电信息计算退款信息");
							}
						} else {
							if (paytype == 1 || paytype == 2) {
								boolean checkBind = UserHandler.checkUserBindMidIsMerId(uid, merchantid);
								TemplateParent templateParent = Equipmenthandler.getTempPermit(code);
								int permit = 0;
								int norm = 1;
								if (templateParent != null) {
									permit = templateParent.getPermit();
									norm = templateParent.getCommon2();
								}
								if (permit == 1) {
									if (checkBind) {
										short power = Equipmenthandler.queryRealchargerecordFirstPower(endChargeId);
										loopRefund(endChargeId, currentTime, begintime, code, port, time, elec, norm, power, reason, hardversion);
									} else {
										logger.warn(chargeRecord.getOrdernum() + "商户绑定与充电设备绑定的商户不一致，不予退款");
									}
								} else {
									logger.warn(chargeRecord.getOrdernum() + "商户设置不可钱包退款");
								}
							}
						}
					}
					ChargeRecordHandler.updateChargeRecordStopCharging(endChargeId, reason + 0);
					Equipmenthandler.addRealchargerecord(endChargeId, chargeRecord.getUid(), chargeRecord.getMerchantid(), code, port, time, elec, 0, -1.0, -1.0, -1.0);
				}
			} 
//			else {
//				str = "数据传递错误";
//				ChargeRecordHandler.updateChargeRecordStopCharging(endChargeId, reason + 0);
//			}
			map.put("param22", "端口：" + port + "， 用户剩余充电时间：" + time + " 分钟， 用户的充电电量：" + (double) elec / 100 + "原因：" + str);
//			AllPortStatusHandler.updateAllPortStatus(code, port, (byte) 1, (short) 0, (short) 0, (short) 0, 0.0, 0.0);
			operRedisAllPortStatus(code, port, time, elec, "1");
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			byteBuffer.put(len);
			byteBuffer.put(cmd);
			byteBuffer.put(result);
			byteBuffer.put(port);
			byteBuffer.putShort(time);
			byteBuffer.putShort(elec);
			byteBuffer.put(reason);
			byteBuffer.flip();
//			byte[] datas = new byte[100];
//			byteBuffer.get(datas, 0, len);
//			byte checkoutSum = checkoutSum(datas);
//			if (sum == checkoutSum) {
//				logger.info("wolflog---" + "数据发送验证成功");
//			} else {
//				logger.info("wolflog---" + "数据发送验证失败");
//			}
		}
	}

	public static Map<String, Map<String, String>> parse_24(AsynchronousSocketChannel channel, byte len, byte cmd,
			byte result, ByteBuffer buffer) {
		Map<String, Map<String, String>> allMap = new HashMap<>();
		Map<String, String> map = new HashMap<>();
		byte data = buffer.get();
		byte sum = buffer.get();
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		logger.info("wolflog---" + code + "号机设置系统参数回复已接收");
		map.put("updatetime", System.currentTimeMillis() + "");
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.put(len);
		byteBuffer.put(cmd);
		byteBuffer.put(result);
		byteBuffer.put(data);
		byteBuffer.flip();
//		byte[] datas = new byte[100];
//		byteBuffer.get(datas, 0, len);
//		byte checkoutSum = checkoutSum(datas);
//		if (sum == checkoutSum) {
//			logger.info("wolflog---" + "数据发送验证成功");
//		} else {
//			logger.info("wolflog---" + "数据发送验证失败");
//		}
		allMap.put(code, map);
		return allMap;
	}

	public static Map<String, Map<String, String>> parse_30(AsynchronousSocketChannel channel, byte len, byte cmd,
			byte result, ByteBuffer buffer) {
		Map<String, Map<String, String>> allMap = new HashMap<>();
		Map<String, String> map = new HashMap<>();
		short coin_min = buffer.getShort();
		map.put("param1", (coin_min & 0xffff) + "");

		short card_min = buffer.getShort();
		map.put("param2", (card_min & 0xffff) + "");

		byte coin_elec = buffer.get();
		map.put("param3", (coin_elec & 0xff) + "");

		byte card_elec = buffer.get();
		map.put("param4", (card_elec & 0xff) + "");

		byte cst = buffer.get();
		map.put("param5", (cst & 0xff) + "");

		short power_max_1 = buffer.getShort();
		map.put("param6", (power_max_1 & 0xffff) + "");

		short power_max_2 = buffer.getShort();
		map.put("param7", (power_max_2 & 0xffff) + "");

		short power_max_3 = buffer.getShort();
		map.put("param8", (power_max_3 & 0xffff) + "");

		short power_max_4 = buffer.getShort();
		map.put("param9", (power_max_4 & 0xffff) + "");

		byte power_2_tim = buffer.get();
		map.put("param10", (power_2_tim & 0xff) + "");

		byte power_3_tim = buffer.get();
		map.put("param11", (power_3_tim & 0xff) + "");

		byte power_4_tim = buffer.get();
		map.put("param12", (power_4_tim & 0xff) + "");

		byte sp_rec_mon = buffer.get();
		map.put("param13", (sp_rec_mon & 0xff) + "");

		byte sp_full_empty = buffer.get();
		map.put("param14", (sp_full_empty & 0xff) + "");

		byte full_power_min = buffer.get();
		map.put("param15", (full_power_min & 0xff) + "");

		byte full_charge_time = buffer.get();
		map.put("param16", (full_charge_time & 0xff) + "");

		byte elec_time_first = buffer.get();

		byte sum = buffer.get();
		String str1 = "";
		String str2 = "";
		String str3 = "";
		if (sp_rec_mon == 1) {
			str1 = "支持";
		} else if (sp_rec_mon == 0) {
			str1 = "不支持";
		} else {
			str1 = "数据传递错误";
		}
		if (sp_full_empty == 1) {
			str2 = "支持";
		} else if (sp_full_empty == 0) {
			str2 = "不支持";
		} else {
			str2 = "数据传递错误";
		}
		if (elec_time_first == 1) {
			str3 = "屏幕初始显示剩余电量";
		} else if (elec_time_first == 0) {
			str3 = "初始时间";
		} else if (elec_time_first == -1) {
			str3 = "不支持";
		} else {
			str3 = "数据传递错误";
		}
		map.put("param17", (elec_time_first & 0xff) + "");
		

		map.put("param30",
				"当前系统设备各项参数如下： 投币充电时间：" + coin_min + "分钟 --刷卡充电时间：" + card_min + "分钟 --单次投币使用最大电量： "
						+ (double) coin_elec / 10 + "度 --单次刷卡最大用电量: " + (double) card_elec / 10 + "度 --刷卡扣费金额"
						+ (double) cst / 10 + "元 --第一档最大充电功率: " + power_max_1 + "W --第二档最大充电功率: " + power_max_2
						+ "W --第三档最大充电功率: " + power_max_3 + "W --第四档最大充电功率" + power_max_4 + "W --第二档充电时间百分比: "
						+ power_2_tim + "% --第三档充电时间百分比: " + power_3_tim + "% --第四档充电时间百分比: " + power_4_tim
						+ "--是否支持余额回收: " + str1 + " --是否支持断电自停: " + str2 + " --充电器最大浮充功率: " + full_power_min
						+ "W --浮充时间: " + full_charge_time + "分钟 --是否初始显示电量: " + str3);
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		CodeSystemParam codeSysParam = Equipmenthandler.getCodeSysParam(code);
		if (codeSysParam == null) {
			Equipmenthandler.addCodeSysParam(code,coin_min & 0xffff,card_min & 0xffff,((coin_elec & 0xff) + 0.0)/10,((coin_elec & 0xff) + 0.0)/10,((cst & 0xff) + 0.0)/10,
					power_max_1 & 0xffff,power_max_2 & 0xffff,power_max_3 & 0xffff
					,power_max_4 & 0xffff,power_2_tim & 0xff,power_3_tim & 0xff,power_4_tim & 0xff,
					sp_rec_mon & 0xff,sp_full_empty & 0xff,full_power_min & 0xff
					,full_charge_time & 0xff,elec_time_first & 0xff);
		} else {
			Equipmenthandler.editCodeSysParam(code,coin_min & 0xffff,card_min & 0xffff,((coin_elec & 0xff) + 0.0)/10,((coin_elec & 0xff) + 0.0)/10,((cst & 0xff) + 0.0)/10,
					power_max_1 & 0xffff,power_max_2 & 0xffff,power_max_3 & 0xffff
					,power_max_4 & 0xffff,power_2_tim & 0xff,power_3_tim & 0xff,power_4_tim & 0xff,
					sp_rec_mon & 0xff,sp_full_empty & 0xff,full_power_min & 0xff
					,full_charge_time & 0xff,elec_time_first & 0xff);
		}
		logger.info("wolflog---" + code + "号机读取设备系统参数信息已接收");
		map.put("updatetime", System.currentTimeMillis() + "");
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.put(len);
		byteBuffer.put(cmd);
		byteBuffer.put(result);
		byteBuffer.putShort(coin_min);
		byteBuffer.putShort(card_min);
		byteBuffer.put(coin_elec);
		byteBuffer.put(card_elec);
		byteBuffer.put(cst);
		byteBuffer.putShort(power_max_1);
		byteBuffer.putShort(power_max_2);
		byteBuffer.putShort(power_max_3);
		byteBuffer.putShort(power_max_4);
		byteBuffer.put(power_2_tim);
		byteBuffer.put(power_3_tim);
		byteBuffer.put(power_4_tim);
		byteBuffer.put(sp_rec_mon);
		byteBuffer.put(sp_full_empty);
		byteBuffer.put(full_power_min);
		byteBuffer.put(full_charge_time);
		byteBuffer.put(elec_time_first);
		byteBuffer.flip();
//		byte[] datas = new byte[200];
//		byteBuffer.get(datas, 0, len);
//		byte checkoutSum = checkoutSum(datas);
//		if (sum == checkoutSum) {
//			logger.info("wolflog---" + "数据发送验证成功");
//		} else {
//			logger.info("wolflog---" + "数据发送验证失败");
//		}
		allMap.put(code, map);
		return allMap;
	}

	public Map<String, Map<String, Map<String, String>>> parse_21(AsynchronousSocketChannel channel, byte len,
			byte cmd, byte result, ByteBuffer buffer) {
		boolean flag = true;
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		Map<String, Map<String, Map<String, String>>> allMap = new HashMap<>();
		Map<String, Map<String, String>> map = new HashMap<>();
		Map<String, String> portmap = new HashMap<>();
		StringBuffer sb = new StringBuffer();
		byte port = buffer.get();
		if (port != 0 && port > 0) {
			sb.append("端口：" + port);
			short time = buffer.getShort();
			sb.append("，剩余充电时间为：" + time + " 分钟");
			short power = buffer.getShort();
			if (power == 0xffff) {
				sb.append("，当前充电功率：没有该数据");
				power = 0;
			} else {
				sb.append("，当前充电功率：" + power + "W");
			}
			short elec = buffer.getShort();
			if (elec == 0xffff) {
				sb.append("，当前剩余电量：没有该数据");
				elec = 0;
			} else {
				// sb.append("，当前剩余电量：" + (double) (elec) / 100 + "度");
				logger.info("wolflog---" + "当前剩余电量：" + elec);
			}
			short surp = buffer.getShort();
			if (surp == 0xffff) {
				sb.append("，可收回余额：没有该数据");
				surp = 0;
			} else {
				// sb.append("，可收回余额：" + (double) (surp) / 10 + "元");
			}
			portmap.put("updatetime", System.currentTimeMillis() + "");
			portmap.put("time", time + "");
			portmap.put("power", power + "");
			portmap.put("elec", elec + "");
			byte portstatus = 1;
			if (time > 0) {
				portstatus = 2;
			}
			portmap.put("portstatus", portstatus + "");
			flag = checkAndDispose(code, port, portstatus, time, power, elec, 0.0, 0.0);
			if (time > 0 && elec > 0) {
				operRedisAllPortStatus(code, port, time, elec, "2");
			}
		} else {
			buffer.getShort();
			buffer.getShort();
			buffer.getShort();
			buffer.getShort();
			portmap.put("updatetime", System.currentTimeMillis() + "");
			// portmap.put("updatetime", System.currentTimeMillis() + "");
		}
		map.put("port" + port, portmap);
		byte sum = buffer.get();
		logger.info("wolflog---" + code + "号机查询当前的充电状态已接收---" + sb.toString());
//		buffer.position(1);
//		byte[] datas = new byte[100];
//		buffer.get(datas, 0, len);
//		byte checkoutSum = checkoutSum(datas);
//		if (sum == checkoutSum) {
//			logger.info("wolflog---" + "数据发送验证成功");
//		} else {
//			logger.info("wolflog---" + "数据发送验证失败");
//		}
		if (flag == false) {
//			buffer.position(0);
			logger.info("wolflog---" + "端口实时状态error" + code + "---");
//			while (buffer.hasRemaining()) {
//				System.out.printf("0x%02x, ", buffer.get());
//			}
		}
		allMap.put(code, map);
		return allMap;
	}

	public static Map<String, Map> parse_32(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Equipment equipment = getCodeHardverByIpaddr(channel);
		Map<String, Map> allMap = new HashMap<>();
		Map<String, String> map = new HashMap<>();
		if (equipment != null) {
			String code = equipment.getCode();
			byte port = buffer.get();
			short time = buffer.getShort();
			short elec = buffer.getShort();
			byte type = buffer.get();
			byte money = buffer.get();
			byte sum = buffer.get();
			String str = "";
			if (type == 0) {
				str = "投币";
				if (money != 0) {
					String hardversion = equipment.getHardversion();
					if (!"03".equals(hardversion) && !"04".equals(hardversion)) {
						try {
							int ifcontinue = getIfcontinue(code, port, time);
							int moneyInt = money & 0xff;
							String ordernum = HttpRequest.createOrdernum(6);
							Integer merchantid = Equipmenthandler.findUserEquipment(code);
							ChargeRecordHandler.addChargeRecord(merchantid, 0, ordernum, 5, 1, code, port + 0, (moneyInt + 0.0)/10, time + "", elec + 0, -1, ifcontinue);
							Integer coinsmoney = CommUtil.toInteger(moneyInt/10);
							int aid = Equipmenthandler.getEquipmentByCode(code);
							if (aid != 0) {
								Equipmenthandler.updateAreaCoinEarn(aid, coinsmoney);
							}
							Equipmenthandler.updateEquCoinEarn(code, coinsmoney);
//							Equipmenthandler.updateMerAmountCoinEarn(merchantid, coinsmoney);
							
							Equipmenthandler.updateMerAmountCoinEarn( merchantid, moneyInt/10);
						} catch (Exception e) {
							logger.warn("投币添加记录异常");
						}
//						if (merid != 0) {
//							ChargeRecordHandler.addInCoinsRecord(merid, code,port,(byte) (moneyInt/10));
//							int aid = Equipmenthandler.getEquipmentByCode(code);
//							if (aid != 0) {
//								Equipmenthandler.updateAreaCoinEarn(aid, moneyInt/10);
//							}
//							Equipmenthandler.updateEquCoinEarn(code, moneyInt/10);
//							Equipmenthandler.updateMerAmountCoinEarn(merid, moneyInt/10);
//						}
					} else {
						logger.warn(code + "号设备发送不符投币信息，此为串口专属");
					}
				}
			} else if (type == 1) {
				str = "刷卡";
				try {
					int moneyInt = money & 0xff;
					String ordernum = HttpRequest.createOrdernum(6);
					Integer merchantid = Equipmenthandler.findUserEquipment(code);
					int ifcontinue = getIfcontinue(code, port, time);
					ChargeRecordHandler.addChargeRecord(merchantid, 0, ordernum, 6, 1, code, port + 0, (moneyInt + 0.0)/10, time + "", elec + 0, -1, ifcontinue);
					OfflineCardHandler.addOfflineCard(merchantid, 0, ordernum, code, "--", -1.0, (moneyInt + 0.0)/10, (moneyInt + 0.0)/10, 0.0, 7, 1, 0, 0);
				} catch (Exception e) {
					logger.info("wolflog---" + "刷卡添加记录异常");
				}
			} else if (type == 3) {
				str = "远程启动";
			} else if (type == 4) {
				str = "其他原因";
			} else {
				str = "数据传递错误";
			}
			map.put("param32", "端口：" + port + "， 用户增加的充电时间：" + time + "分钟， 用户增加的充电电量：" + (double) elec / 100 + "度， 充电原因："
					+ str + ", 充值金额：" + (double) money / 10 + " 元");
			logger.info("wolflog---" + code + "号机上报投币打开的信息已接收");
//			buffer.position(1);
//			byte[] datas = new byte[100];
//			buffer.get(datas, 0, len);
//			byte checkoutSum = checkoutSum(datas);
//			if (sum == checkoutSum) {
//				logger.info("wolflog---" + "数据发送验证成功");
//			} else {
//				logger.info("wolflog---" + "数据发送验证失败");
//			}
			allMap.put(code, map);
		}
		return allMap;
	}

	//设备上传端口实时日志信息
	public void parse_33(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		boolean flag = true;
		Equipment equipment = getCodeHardverByIpaddr(channel);
		if (equipment != null) {
			String code = equipment.getCode();
			String hardversion = equipment.getHardversion();
			send_33(code);
			logger.info(code + "号机上传端口的实时状态已接收");
			Map<String, String> codeMap = null;
			if (code != null && !"".equals(code)) {
				codeMap = JedisUtils.hgetAll(code);
			}
			Map<String, List<String>> map = new HashMap<>();
			List<String> list = new ArrayList<>();
			byte port_num = buffer.get();
			StringBuffer sb = new StringBuffer();
			Map<String, String> redisMap = new HashMap<>();
			if (DisposeUtil.checkMapIfHasValue(codeMap)) {
				redisMap.putAll(codeMap);
			}
			if (port_num != 0 && port_num > 0) {
				String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				for (int i = 0; i < port_num; i++) {
					Map<String,String> portMap = new HashMap<>();
					byte port = buffer.get();
					if (port < 0 || port > 20) {
						break;
					}
					if (DisposeUtil.checkMapIfHasValue(codeMap)) {
						Map<String,String> parse = (Map<String, String>) JSON.parse(codeMap.get(port + ""));
						if (DisposeUtil.checkMapIfHasValue(parse)) {
							portMap.putAll(parse);
						}
					}
					byte portStatus = buffer.get();
					short  time = buffer.getShort();
					short power = buffer.getShort();
					short elec = buffer.getShort();
					if (time >= 0) {
						portMap.put("time", time + "");
					} else if (!DisposeUtil.checkMapIfHasValue(portMap)) {
						portMap.put("time", 0 + "");
					}
					if (power >= 0) {
						portMap.put("power", power + "");
					} else if (!DisposeUtil.checkMapIfHasValue(portMap)) {
						portMap.put("power", 0 + "");
					}
					portMap.put("portStatus", portStatus + "");
					if (DisposeUtil.checkIfHasV3(hardversion)) {
						portMap.put("elec", (elec & 0x7fff) + "");
					} else {
						if (elec < 0) {
							portMap.put("elec", (elec & 0x7fff) + "");
						} else {
							portMap.put("elec", (elec & 0xffff) + "");
						}
					}
					portMap.put("port", port + "");
					portMap.put("updateTime", format);
					redisMap.put(port + "", JSON.toJSONString(portMap));
					sb.append("端口：" + port);
					if (portStatus == 1) {
						sb.append("， 空闲");
						list.add("空闲");
						try {
							if (codeMap != null && codeMap.size() > 0) {
								Map<String,String> parse = (Map<String, String>) JSON.parse(codeMap.get(port + ""));
								if (parse != null && parse.size() > 0) {
									String portStatusStr = parse.get("portStatus");
									if ("2".equals(portStatusStr)) {
										if (time == 0 && power == 0 && elec == 0) {
											logDataDispose(code, port, time, elec, equipment.getHardversion());
											logger.info(code + "号设备" + port + "端口需要日志结束充电");
										}
									}
								}
							}
						} catch (Exception e) {
							logger.error("端口日志结束异常：");
							e.printStackTrace();
						}
					} else if (portStatus == 2) {
						sb.append("， 使用");
						list.add("使用");
						Integer endChargeId = ChargeRecordHandler.getEndChargeRecord(code, port + 0);
						disposeLogData(endChargeId, power, equipment, elec, time, code, port);
				} else if (portStatus == 3) {
					sb.append("， 禁用");
					list.add("禁用");
				} else if (portStatus == 4) {
					sb.append("， 故障");
					list.add("故障");
				}
				sb.append("， 充电时间 ：" + time + "分钟");
				sb.append("， 充电器实时功率 ：" + power + "W");
				sb.append("， 剩余电量 ：" + (double) elec / 100 + "度");
				list.add("" + time);
				list.add("" + power);
				list.add("" + elec / 100);
				}
			}
			JedisUtils.addEquipmentCache(code, redisMap);
			byte sum = buffer.get();
			buffer.position(1);
			map.put(code, list);
//			byte[] datas = new byte[100];
//			buffer.get(datas, 0, len);
//			byte checkoutSum = checkoutSum(datas);
//			if (sum == checkoutSum) {
//				logger.info("wolflog---" + "数据发送验证成功");
//			} else {
//				logger.info("wolflog---" + "数据发送验证失败");
//			}
		if (!flag) {
				buffer.position(0);
				logger.info("wolflog---" + "端口实时状态error" + code + "---");
//				while (buffer.hasRemaining()) {
//					System.out.printf("0x%02x, ", buffer.get());
//				}
			}
		}
	}
	//TODO 日志信息处理
	public static void logDataDispose(String code, byte port,int time,int elec, String hardversion) {
		try {
			List<Integer> chargeUnfinshIdList = ChargeRecordHandler.getChargeUnfinshId(code, port + 0);
			if (chargeUnfinshIdList != null && chargeUnfinshIdList.size() > 0) {
				for (Integer endChargeId : chargeUnfinshIdList) {
					long currentTime = System.currentTimeMillis();
					ChargeRecord chargeRecord = ChargeRecordHandler.getChargeRecordById(endChargeId);
					if (chargeRecord.getPaytype() == 1 || chargeRecord.getPaytype() == 2) {
						long begintime = chargeRecord.getBegintime().getTime();
						Integer consumeTime = chargeRecord.getConsumeTime();
						Integer consumeQuantity = chargeRecord.getConsumeQuantity();
						if ((currentTime - begintime) > 10 * 60 * 1000 && consumeTime > 0 && consumeQuantity > 1) {
							if (DisposeUtil.checkIfHasV3(hardversion)) {
								Realchargerecord realchargerecord = Equipmenthandler.queryRealchargerecord(endChargeId);
								if (realchargerecord != null) {
									v3DisposeData(chargeRecord, code, port, endChargeId, time, 255);
								}
							} else {
								TemplateParent templateParent = Equipmenthandler.getTempPermit(code);
								int permit = 0;
								int norm = 1;
								if (templateParent != null) {
									permit = templateParent.getPermit();
									norm = templateParent.getCommon2();
								}
								if (permit == 1) {
									Realchargerecord realchargerecord = Equipmenthandler.queryRealchargerecord(endChargeId);
									if (realchargerecord != null) {
										time = realchargerecord.getChargetime().shortValue();
										elec = realchargerecord.getSurpluselec().shortValue();
										currentTime = realchargerecord.getCreatetime().getTime();
									}
									Integer uid = chargeRecord.getUid();
									Integer merchantid = chargeRecord.getMerchantid();
									boolean checkBind = UserHandler.checkUserBindMidIsMerId(uid, merchantid);
									if (checkBind) {
										short power = Equipmenthandler.queryRealchargerecordMaxPower(endChargeId);
										byte reason = (byte) 0xff;
										loopRefund(endChargeId, currentTime, begintime, code, port, time, elec, norm, power, reason, hardversion);
									} else {
										logger.warn(chargeRecord.getOrdernum() + "商户绑定与充电设备绑定的商户不一致，不予退款");
									}
								} else {
									logger.warn(chargeRecord.getOrdernum() + "商户设置不可钱包退款");
								}
								ChargeRecordHandler.updateChargeRecordStopCharging(endChargeId, 255);
								Equipmenthandler.addRealchargerecord(endChargeId, chargeRecord.getUid(), chargeRecord.getMerchantid(), code, port, 0, 0, 0, -1.0, -1.0, -1.0);
							}
						} else if ((currentTime - begintime) > 3 * 60 * 1000) {
							logAllRefund(chargeRecord, 255, code, port);
							ChargeRecordHandler.updateChargeRecordStopCharging(endChargeId, 255);
							Equipmenthandler.addRealchargerecord(endChargeId, chargeRecord.getUid(), chargeRecord.getMerchantid(), code, port, 0, 0, 0, -1.0, -1.0, -1.0);
							logger.warn(chargeRecord.getOrdernum() + "10分钟内日志全额退款");
						}
					}
				}
			}
		} catch (Exception e) {
			logger.warn("根据日志结束充电异常:");
			e.printStackTrace();
		}
	}
	
	public static void loopRefund(Integer endChargeId,long currentTime,long begintime,String code,
			byte port,int time,int elec,int norm, short clacpower, byte reason, String hardversion) {
		List<ChargeRecord> chargeList = ChargeRecordHandler.getChargeListUnfinish(endChargeId);
		ChargeRecord chargeRecord = chargeList.get(chargeList.size() - 1);
		Integer uid = chargeRecord.getUid();
		Double allTime = 0.0;
		Integer quantity = 0;
		Double allMoney = 0.0;
		for (ChargeRecord charge : chargeList) {
			allTime += Integer.parseInt(charge.getDurationtime());
			quantity += charge.getQuantity();
			allMoney += charge.getExpenditure();
		}
		if (hardversion != null && "07".equals(hardversion)) {
			norm = 3;
		}
		logger.info("时间对比：" + (currentTime - begintime < allTime));
		if ((currentTime - begintime) < (allTime * 60 * 1000)) {
//			AllPortStatus allPortStatus = AllPortStatusHandler.getEquipmentStatusByCodeAndPort(code, port + 0);
			Map<String, String> allPortStatus = DisposeUtil.addPortStatus(code, port);
			if (allPortStatus != null) {
				Double surpTime = time + 0.0;
				Double useTime = allTime - time;
				double useTimeInt = (currentTime - begintime) / 1000 / 60 + 0.0;
				if (useTime > useTimeInt || time < 0 || time > allTime) {
					useTime = useTimeInt;
				}
				double refundMoney = 0.0;
				refundMoney = clacRefund(code,clacpower, quantity + 0.0, elec + 0.0, allTime, surpTime, allMoney, norm, begintime, currentTime);
				refundMoney = (Math.round(refundMoney * 100) + 0.0) / 100;
				Integer useElec = quantity - elec;
				logger.info(chargeRecord.getOrdernum() + "退款金额===" + refundMoney);
				String firstdata = "尊敬的用户，您的充电已经结束。";
				if (refundMoney > 0) {
					firstdata = "尊敬的用户，您的充电已经结束，已退款(¥" + (refundMoney + 0.00) + ")。 \r\n 注：该退款到平台钱包里。";
				}
				uid = CommUtil.toInteger(uid);
				Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
				Double paymoney = CommUtil.toDouble(allMoney);
				Double consumemoney = CommUtil.subBig(paymoney, refundMoney);
				Integer orderid = CommUtil.toInteger(chargeRecord.getId());
				String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
				String deviceport = CommUtil.toString(port);
				Integer resultinfo = CommUtil.toInteger(reason);
				String startTime = CommUtil.toDateTime(chargeRecord.getBegintime());
				String endTime = CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date());
				String chargetime = "开始时间："+startTime + "\r\n结束时间：" + endTime;
				String devicenum = code;
				Equipment devicedata = Equipmenthandler.getEquipmentData(devicenum);
				Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
				Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
				String devicename = CommUtil.toString(devicedata.getRemark());
				String areaaddress = CommUtil.toString(devicedata.getAddress());
				String servicphone = getServicePhone(devicetempid, deviceaid, merid);
				TempMsgUtil.endChargeSendMess(firstdata, ordernum, orderid, resultinfo, merid, uid, consumemoney, 
				chargetime, devicenum, deviceport, devicename, deviceaid, areaaddress, servicphone);
				for (ChargeRecord charge : chargeList) {
					if (refundMoney > charge.getExpenditure()) {
//						Double userBalance2 = ChargeRecordHandler.getUserBalance(uid);
						User touser = UserHandler.getUserInfo(uid);
						Double userBalance = CommUtil.toDouble(touser.getBalance());
						Double usersendmoney = CommUtil.toDouble(touser.getSendmoney());
//						double calcBalance = (userBalance2 * 100 + charge.getExpenditure() * 100) / 100;
						Double calcBalance = CommUtil.addBig(userBalance, CommUtil.toDouble(charge.getExpenditure()));
						ChargeRecordHandler.updateUserMoney(charge.getExpenditure(), uid, null, 2);
						ChargeRecordHandler.updateChargeRefundNumer(2, charge.getId(),
								charge.getExpenditure(), charge.getExpenditure(), 0, 0);
						
//						double calcBalance = (userBalance * 100 + chargeRecord.getExpenditure() * 100) / 100;
						Double opermoney = CommUtil.toDouble(charge.getExpenditure());
						Double opersendmoney = 0.00;
						Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
						Double topupbalance = CommUtil.addBig(opermoney, userBalance);
						Double givebalance = CommUtil.addBig(opersendmoney, usersendmoney);
						Double operbalance = CommUtil.addBig(topupbalance, givebalance);
						
						Money moneyEntity = new Money();
						moneyEntity.setUid(uid);
						moneyEntity.setOrdernum(charge.getOrdernum());
						moneyEntity.setPaytype(3);
						moneyEntity.setStatus(1);
//						moneyEntity.setMoney(charge.getExpenditure());
//						moneyEntity.setBalance(calcBalance);
						moneyEntity.setMoney(opermoney);
						moneyEntity.setSendmoney(opersendmoney);
						moneyEntity.setTomoney(opertomoney);
						moneyEntity.setBalance(operbalance);
						moneyEntity.setTopupbalance(topupbalance);
						moneyEntity.setGivebalance(givebalance);
						moneyEntity.setRemark("wallet");
						ChargeRecordHandler.addMoneyRecord(moneyEntity);
						UserHandler.addGenDetail(charge.getUid(), charge.getMerchantid(), charge.getOrdernum(),
								opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance , 5, null);
//						UserHandler.addGenDetail(charge.getUid(), charge.getMerchantid(),
//								charge.getOrdernum(), charge.getExpenditure(), calcBalance, 5, null);
						TradeRecordHandler.addTradeRecord(charge.getMerchantid(), 0,
								charge.getUid(), charge.getOrdernum(), charge.getExpenditure(),
								0.00, 0.00, code, 1, 1, 2);
					} else if (refundMoney > 0) {
//						Double userBalance2 = ChargeRecordHandler.getUserBalance(uid);
						User touser = UserHandler.getUserInfo(uid);
						Double userBalance = CommUtil.toDouble(touser.getBalance());
						Double usersendmoney = CommUtil.toDouble(touser.getSendmoney());
						ChargeRecordHandler.updateUserMoney(refundMoney, uid, null, 2);//充电记录部分退款，退款到钱包
						if (useElec > quantity) {
							useElec = null;
						}
						Integer userTimeInt = null;
						if (useTime < allTime && useTime > 0) {
							userTimeInt = useTime.intValue();
						}
						ChargeRecordHandler.updateChargeRefundNumer(2, charge.getId(), charge.getExpenditure(), 
								refundMoney, userTimeInt, useElec);
						
//						double calcBalance = (userBalance2 * 100 + refundMoney * 100) / 100;
//						Double calcBalance = CommUtil.addBig(userBalance, CommUtil.toDouble(refundMoney));
						Double opermoney = CommUtil.toDouble(refundMoney);
						Double opersendmoney = 0.00;
						Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
						Double topupbalance = CommUtil.addBig(opermoney, userBalance);
						Double givebalance = CommUtil.addBig(opersendmoney, usersendmoney);
						Double operbalance = CommUtil.addBig(topupbalance, givebalance);
						
						Money moneyEntity = new Money();
						moneyEntity.setUid(uid);
						moneyEntity.setOrdernum(charge.getOrdernum());
						moneyEntity.setPaytype(3);
						moneyEntity.setStatus(1);
//						moneyEntity.setMoney(refundMoney);
//						moneyEntity.setBalance(calcBalance);
						moneyEntity.setMoney(opermoney);
						moneyEntity.setSendmoney(opersendmoney);
						moneyEntity.setTomoney(opertomoney);
						moneyEntity.setBalance(operbalance);
						moneyEntity.setTopupbalance(topupbalance);
						moneyEntity.setGivebalance(givebalance);
						moneyEntity.setRemark("wallet");
						ChargeRecordHandler.addMoneyRecord(moneyEntity);
						UserHandler.addGenDetail(chargeRecord.getUid(), charge.getMerchantid(), chargeRecord.getOrdernum(),
							opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance , 5, null);
//						UserHandler.addGenDetail(charge.getUid(), charge.getMerchantid(),
//								charge.getOrdernum(), refundMoney, calcBalance, 5, null);
						TradeRecordHandler.addTradeRecord(charge.getMerchantid(), 0, charge.getUid(), 
							charge.getOrdernum(), refundMoney, 0.00, 0.00, code, 1, 1, 2);
					} else {
						break;
					}
					refundMoney -= charge.getExpenditure();
				}
				
			} else {
				logger.warn(chargeRecord.getOrdernum() + "设备没有上传实时端口信息，不予退款");
			}
		} else {
//			Equipmenthandler.addRealchargerecord(endChargeId, chargeRecord.getUid(), chargeRecord.getMerchantid(), code, port, 0, 0, 0);
			logger.warn(chargeRecord.getOrdernum() + "时间已过期，不予退款");
		}
	}
	
	public static void parse_0x23(AsynchronousSocketChannel channel, byte len, byte cmd, byte result, ByteBuffer buffer) {
		String code = getCodeByIpaddr(channel);
		try {
			byte type = buffer.get();
			logger.info("wolflog---" + "主板版本===" + (type & 0xff));
			short hardver = buffer.getShort();
			logger.info("wolflog---" + "主板硬件版本===" + (hardver & 0xffff));
			short softver = buffer.getShort();
			logger.info("wolflog---" + "主板软件版本===" + (softver & 0xffff));
			int mainid_before4Int = buffer.getInt();
			int mainid_middle4Int = buffer.getInt();
			int mainid_after4Int = buffer.getInt();
			String mainid_before4 = buquan8wei(Integer.toHexString(mainid_before4Int));
			String mainid_middle4 = buquan8wei(Integer.toHexString(mainid_middle4Int));
			String mainid_after4 = buquan8wei(Integer.toHexString(mainid_after4Int));
//			long long1 = buffer.getLong() << 32;
//			int int1 = buffer.getInt();
//			long mainidLong = long1 + int1;
//			String mainid = Long.toHexString(mainidLong);
			String mainid = mainid_before4 + mainid_middle4 + mainid_after4;
//			for (int i = 0; i < 12; i++) {
//				mainid += (char) buffer.get();
//			}
			logger.info("wolflog---" + "主板id===" + mainid.toUpperCase());
			try {
				Equipmenthandler.updateEquMainInfo(mainid, (type & 0xff) + "", (hardver & 0xffff) + "",
						(softver & 0xffff) + "", code);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("修改设备主板信息异常");
			}
			byte sum = buffer.get();
			logger.info("wolflog---" + code + "号设备服务器请求收到充电站主板的信息已接收");
//			buffer.position(1);
//			byte[] datas = new byte[100];
//			buffer.get(datas, 0, len);
//			byte checkoutSum = checkoutSum(datas);
//			if (sum == checkoutSum) {
//				logger.info("wolflog---" + "数据发送验证成功");
//			} else {
//				logger.info("wolflog---" + "数据发送验证失败");
//			} 
			Map<String,String> map = new HashMap<>();
			map.put("mainType", (type & 0xff) + "");
			map.put("mainHardver", (hardver & 0xffff) + "");
			map.put("mainSoftver", (softver & 0xffff) + "");
			map.put("mainid", mainid);
			map.put("updateTime", System.currentTimeMillis() + "");
			Server.maininfoMap.put(code, map);
		} catch (Exception e) {
			logger.info("wolflog---" + code + "号设备服务器请求收到充电站主板的信息已接收解析数据异常");
		}
	}
	
	public static String buquan8wei(String hexString) {
		if (hexString.length() < 8) {
			String str = "";
			for (int i = 0; i < 8 - hexString.length(); i++) {
				str += "0";
			}
			return str + hexString;
		} else {
			return hexString;
		}
		
	}
	
	public void parse_0x26(AsynchronousSocketChannel channel, byte len, byte cmd, byte result, ByteBuffer buffer) {
		Equipment equipment = getCodeHardverByIpaddr(channel);
		if (equipment == null) {
			return;
		}
		String code = equipment.getCode();
		try {
			Map<String, String> codeMap = null;
			if (code != null && !"".equals(code)) {
				codeMap = JedisUtils.hgetAll(code);
			}
			byte port_num = buffer.get();
			logger.info("wolflog---" + "端口总数===" + (port_num & 0xff));
			if (port_num > 0) {
				Map<String, String> redisMap = new HashMap<>();
				for (int i = 0; i < port_num; i++) {
					byte port = buffer.get();
					int type = buffer.get() & 0xff;
					byte port_status = buffer.get();
					short time = buffer.getShort();
					short power = buffer.getShort();
					short elec = buffer.getShort();
					int port_v = buffer.getShort() & 0xffff;
					int port_a = buffer.getShort() & 0xffff;
					Map<String,String> map = new HashMap<>();
					map.put("port", port + "");
					map.put("type", type + "");
					map.put("port_status", port_status + "");
					map.put("time", time + "");
					map.put("power", power + "");
					map.put("elec", elec + "");
					map.put("port_v", port_v + "");
					map.put("port_a", port_a + "");
					Map<String,String> portMap = new HashMap<>();
					if (port < 0 || port > 20 || port_status < 0) {
						break;
					}
					portMap.put("portStatus", port_status + "");
					portMap.put("time", (time & 0xffff) + "");
					portMap.put("power", (power & 0xffff) + "");
					portMap.put("elec", (elec & 0x7fff) + "");
					portMap.put("port", port + "");
					portMap.put("updateTime", CommUtil.toDateTime());
					portMap.put("portV", port_v + "");
					portMap.put("portA", port_a + "");
					redisMap.put(port + "", JSON.toJSONString(portMap));
//					checkAndDispose(code, port, port_status, time, power, elec, (port_v + 0.0)/10, (port_a + 0.0)/100);
					if (port_status == 1 || port_status == 4) {
						try {
							if (codeMap != null && codeMap.size() > 0) {
								Map<String,String> parse = (Map<String, String>) JSON.parse(codeMap.get(port + ""));
								if (parse != null && parse.size() > 0) {
									String portStatusStr = parse.get("portStatus");
									if ("2".equals(portStatusStr)) {
										if (time == 0 && power == 0 && elec == 0) {
											logDataDispose(code, port, time, elec, equipment.getHardversion());
											logger.info(code + "号设备" + port + "端口需要日志结束充电");
										}
									}
								}
							}
						} catch (Exception e) {
							logger.error("端口日志结束异常：");
							e.printStackTrace();
						}
					} else if (port_status == 2) {
						Integer endChargeId = ChargeRecordHandler.getEndChargeRecord(code, port + 0);
						disposeLogData(endChargeId, power, equipment, elec, time, code, port);
					}
				}
				JedisUtils.addEquipmentCache(code, redisMap);
			}
			byte sum = buffer.get();
			logger.info("wolflog---" + code + "号设备新版上传端口的实时状态数据已接收");
			send_0x26(code);
		} catch (Exception e) {
			logger.info("wolflog---" + code + "号设备新版上传端口的实时状态数据解析异常---");
			e.printStackTrace();
		}
	}
	
	public static void parse_0x27(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		String code = getCodeByIpaddr(channel);
		try {
			byte port = buffer.get();
			Server.newBeginChargeInfoMap.put("updatetime" + code + port, "" + System.currentTimeMillis());
			byte backresult = buffer.get();
			int session_id = buffer.getInt();
			byte sum = buffer.get();
			Map<String,String> map = new HashMap<>();
			map.put("port", port + "");
			map.put("reason", backresult + "");
			map.put("session_id", session_id + "");
			try {
				Timer timer = Server.timerMap.get(session_id);
				if (timer != null) {
					timer.cancel();
					Server.timerMap.remove(session_id);
					Server.timerNumMap.remove(session_id);
					logger.info("wolflog---" + "定时任务已关闭");
				} else {
					logger.info("wolflog---" + "定时任务关闭失败");
				}
			} catch (Exception e) {
				logger.info("wolflog---" + "定时任务关闭失败");
				e.printStackTrace();
			}
			logger.info(code + "号机" + port + "端口新版付款成功已接收：" + JSON.toJSONString(map));
			ChargeRecordHandler.updateChargeResultinfo(code, port);
		} catch (Exception e) {
			logger.info(code + "号设备新版付款成功数据解析异常---" + e.getMessage());
		}
	}
	
	/**
	 * 设备充电退款
	 * @param channel
	 * @param len
	 * @param cmd
	 * @param result
	 * @param buffer
	 */
	public static void parse_0x2c(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Equipment equipment = getCodeHardverByIpaddr(channel);
		String code = equipment.getCode();
		String hardversion = equipment.getHardversion();
		logger.info("wolflog---" + code + "号设备新版提交充电结束状态数据已接收");
		try {
			byte port = buffer.get();
			short time = buffer.getShort();
			short elec = buffer.getShort();
			int card_idInt = buffer.getInt();
			String card_id = Integer.toHexString(card_idInt);
			short card_cst = buffer.getShort();
			byte card_ope = buffer.get();
			byte card_type = buffer.get();
			byte reason = buffer.get();
			int session_id = buffer.getInt();
			byte sum = buffer.get();
			Map<String,String> map = new HashMap<>();
			map.put("port", port + "");
			map.put("time", time + "");
			map.put("elec", elec + "");
			map.put("card_idInt", buquan8wei(card_id));
			map.put("card_cst", card_cst + "");
			map.put("card_ope", card_ope + "");
			map.put("card_type", card_type + "");
			map.put("reason", reason + "");
			map.put("session_id", session_id + "");
			logger.info("wolflog---" + JSON.toJSONString(map));
			buffer.position(1);
			byte[] datas = new byte[100];
			buffer.get(datas, 0, len);
			byte checkoutSum = checkoutSum(datas);
			if (sum == checkoutSum) {
				logger.info("wolflog---" + "数据发送验证成功");
			} else {
				logger.info("wolflog---" + "数据发送验证失败");
			}
			send_0x2c(code, port, session_id);
			Map<String, String> session_idMap = Server.endChargeInfoMap.get(code + port);
			boolean checkSessionId = checkSessionId(session_idMap, session_id);
			if (checkSessionId) {
				Integer endChargeId = ChargeRecordHandler.getEndChargeRecord(code, port + 0);
				if (endChargeId <= 0) {
					return;
				} else {
					operRedisAllPortStatus(code, port, time, elec, "1");
				}
				ChargeRecord chargeRecord = ChargeRecordHandler.getChargeRecordById(endChargeId);
				long currentTime = System.currentTimeMillis();
				long begintime = chargeRecord.getBegintime().getTime();
				//  ORIGIN 处理在线卡数据
				if (card_type == 1 && card_idInt != 0) {//card_type 1:在线卡   card_ope 0:消费  1:回收  
					String ordernum = chargeRecord.getOrdernum();
					if (endChargeId != 0 && endChargeId != -1) {//判断订单存不存在
						String card_idStr = Integer.toHexString(card_idInt).toUpperCase();//卡号
						String cardID = card_idStr;
						OnlineCard onlineCard = OnlineCardHandler.cardIsExist(card_idStr);
						if (onlineCard != null && card_cst > 0) {
							//在线卡数据处理
							Integer merid = CommUtil.toInteger(onlineCard.getMerid());
							Integer uid = CommUtil.toInteger(onlineCard.getUid());
							//============================================================================
							Double topupmoney = CommUtil.toDouble(onlineCard.getMoney());//充值金额
							Double sendmoney = CommUtil.toDouble(onlineCard.getSendmoney());//赠送金额
							Integer relevawalt = CommUtil.toInteger(onlineCard.getRelevawalt());
							if(relevawalt==1){
								User user = UserHandler.getUserInfo(uid);
								topupmoney = CommUtil.toDouble(user.getBalance());
								sendmoney = CommUtil.toDouble(user.getSendmoney());
							}
							Double consumemoney = CommUtil.toDouble(card_cst)/10;//消费金额
							//cardopetype  1:消费   2:回收
							Integer cardopetype = 1;
							if(card_ope==1) cardopetype = 2;
							Map<String, Object> disposeData = onlinecardDataDispose(topupmoney, sendmoney, consumemoney, cardopetype);
							Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额
							//============================================================================
							Double opermoney = CommUtil.toDouble(disposeData.get("opermoney"));//操作总额
							Double opertopupmoney = CommUtil.toDouble(disposeData.get("opertopupmoney"));	//操作充值金额
							Double opersendmoney = CommUtil.toDouble(disposeData.get("opersendmoney"));	//操作赠送金额
							//==========================================================================
							Double topupbalance = CommUtil.toDouble(disposeData.get("topupbalance"));//充值余额
							Double sendbalance = CommUtil.toDouble(disposeData.get("sendbalance"));//赠送余额
							Double accountbalance = CommUtil.toDouble(disposeData.get("accountbalance"));//账户余额
							//==========================================================================
							if (card_ope == 1) {// 退费(回收)
								if(relevawalt==2){//不关联钱包
									OnlineCardHandler.updateCardMoney(opertopupmoney, cardID, 1, sendbalance);
									OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 2, 1);
									logger.info("wolflog---" + "当前卡号为退费IC：" + card_id + "---实时操作金额为：" + opermoney + "元");
								}else{//关联钱包
									//修改用户钱包余额
									UserHandler.updateWalltMoney(opertopupmoney, opersendmoney, 1, uid);
									//添加用户明细记录
									UserHandler.addGenDetail(uid, merid, ordernum, opertopupmoney, opersendmoney, 
										opermoney, accountbalance, topupbalance, sendbalance , 5, cardID);
									//添加在线卡记录
									OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, 
										opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 2, 1);
									logger.info("wolflog---" + "当前卡号为退费：" + card_id + "---实时操作金额为：" + opermoney + "元");
								}
							} else if (card_ope == 0) {//扣费（消费）
								Double comparemoney = CommUtil.subBig(accountmoney, opermoney); //总额、操作比较金额差
								if(comparemoney>=0){//金额足够，扣费
									if(relevawalt==2){//不关联钱包
										OnlineCardHandler.updateCardMoney(opertopupmoney, cardID, 0, sendbalance);
										OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 1, 1);
										logger.info("wolflog---" + "当前卡号为：" + card_id + "---实时扣费金额为：" + opermoney + "元");
									}else{//关联钱包
										//修改用户钱包余额
										UserHandler.updateWalltMoney(opertopupmoney, opersendmoney, 0, uid);
										//添加用户明细记录
										UserHandler.addGenDetail(uid, merid, ordernum, opertopupmoney, opersendmoney, 
												opermoney, accountbalance, topupbalance, sendbalance , 9, cardID);
										//添加在线卡记录
										OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, 
												opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 1, 1);
										logger.info("wolflog---" + "当前卡号为：" + card_id + "---实时操作金额为：" + opermoney + "元");
									}
								}else{//金额不够，返回
									logger.info("wolflog---" + "订单号   "+ordernum+"  该笔订单卡号为：" + card_id + "余额不足---实时操作金额为：" + opermoney + "元");
								}
							}
						}
						ChargeRecordHandler.updateChargeRecordStopCharging(endChargeId, reason + 0);
						if (card_cst > 0) {
							ChargeRecordHandler.updateChargeRefundNumer(2, endChargeId, chargeRecord.getExpenditure(), CommUtil.toDouble(card_cst)/10, null, null);
						} else {
							ChargeRecordHandler.updateChargeRefundNumer(0, endChargeId, chargeRecord.getExpenditure(), 0.0, null, null);
						}
					}
				} else if (chargeRecord.getPaytype() == 5 || chargeRecord.getPaytype() == 6) {
					ChargeRecordHandler.updateChargeRecordStopCharging(endChargeId, reason + 0);
				}
				else if (DisposeUtil.checkIfHasV3(hardversion) && ((currentTime - begintime) > 60000)) {
					v3DisposeData(chargeRecord, code, port, endChargeId, time, reason);
				} else if (reason == 0) {
					if (endChargeId != 0) {
						List<ChargeRecord> chargeList = ChargeRecordHandler.getChargeListUnfinish(endChargeId);
						int allTime = 0;
						int allelec = 0;
						for (ChargeRecord charge : chargeList) {
							allTime += Integer.parseInt(charge.getDurationtime());
							allelec += charge.getQuantity();
						}
						if (((currentTime - begintime) > 0) && (currentTime - begintime) < (allTime * 60 * 1000 + 120000)) {
							//TODO 没用的通知  充电结束时间
							Integer uid = CommUtil.toInteger(chargeRecord.getUid());
							Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
							Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
							Integer orderid = CommUtil.toInteger(chargeRecord.getId());
							String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
							String deviceport = CommUtil.toString(port);
							Integer resultinfo = CommUtil.toInteger(reason);
							String startTime = CommUtil.toDateTime(chargeRecord.getBegintime());
							String endTime = CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date());
							String chargetime = "开始时间："+startTime + "\r\n结束时间：" + endTime;
							logger.info("wolflog---" + "001   "+chargetime);
							String devicenum = code;
							Equipment devicedata = Equipmenthandler.getEquipmentData(devicenum);
							Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
							Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
							String devicename = CommUtil.toString(devicedata.getRemark());
							String areaaddress = CommUtil.toString(devicedata.getAddress());
							String servicphone = getServicePhone(devicetempid, deviceaid, merid);
							TempMsgUtil.endChargeSendMess("您好，充电已结束", ordernum, orderid, resultinfo, merid, uid, paymoney, chargetime, 
									devicenum, deviceport, devicename, deviceaid, areaaddress, servicphone);
						}
						if (((currentTime - begintime) > 0)
								&& ((currentTime - begintime) < (allTime * 60 * 1000 + 120000))) {
							Equipmenthandler.addRealchargerecord(endChargeId, chargeRecord.getUid(), chargeRecord.getMerchantid(), code, port, time, elec, 0, -1.0, -1.0, -1.0);
							if ((time <= allTime) && (elec <= allelec)) {
								int realusetime = (int) ((System.currentTimeMillis() - begintime) / 1000 / 60);
								int elecInt = elec & 0xffff;
								Integer surpElec = null;
								if (elecInt <= allelec) {
									surpElec = allelec - elecInt;
								}
								if (allTime > realusetime) {
									ChargeRecordHandler.updateChargeEndInfo(endChargeId, realusetime, surpElec, reason + 0);
								} else {
									ChargeRecordHandler.updateChargeEndInfo(endChargeId, allTime - time, surpElec, reason + 0);
								}
							} else {
								ChargeRecordHandler.updateChargeRecordStopCharging(endChargeId, reason + 0);
							}
						} else {
							ChargeRecordHandler.updateChargeRecordStopCharging(endChargeId, reason + 0);
						}
					}
				} else {
					if (endChargeId != 0) {
						Date begintimeDate = chargeRecord.getBegintime();
						begintime = begintimeDate == null ? (currentTime + 50) : begintimeDate.getTime();
						if (chargeRecord.getNumber() != null && chargeRecord.getNumber() != 1) {
							Double money = chargeRecord.getExpenditure();
							Integer merchantid = chargeRecord.getMerchantid();
							Integer uid = chargeRecord.getUid();
//							Double userBalance = ChargeRecordHandler.getUserBalance(uid);
							User touser = UserHandler.getUserInfo(uid);
							Double userBalance = CommUtil.toDouble(touser.getBalance());
							Double usersendmoney = CommUtil.toDouble(touser.getSendmoney());
							Integer paytype = chargeRecord.getPaytype();
							short maxPower = Equipmenthandler.queryRealchargerecordMaxPower(endChargeId);
							if ((currentTime - begintime) > 0 && ((currentTime - begintime) <= 180000 || 
									((currentTime - begintime) < 600000 && maxPower <= 20))) {
								if (paytype == 4) {
									logger.info("wolflog---" + "包月下发充电数据退款");
									ChargeRecordHandler.updateChargeRefundNumer(1, endChargeId,
											money, money, 0, 0);
									PackageMonth packageMonth = UserHandler.getPackageMonth(uid);
									if (packageMonth != null) {
										Integer everydaynum = packageMonth.getEverydaynum();
										Integer everymonthnum = packageMonth.getEverymonthnum();
										if (everydaynum != 0 || everymonthnum != 0) {
											UserHandler.updatePackageMonth(everydaynum, everymonthnum, uid);
											UserHandler.addPackageMonthRecord(uid, chargeRecord.getOrdernum(), 
													money, packageMonth.getTodaysurpnum(), packageMonth.getSurpnum(), 
													Integer.parseInt(chargeRecord.getDurationtime()), 
													(chargeRecord.getQuantity() + 0.0)/100);
										}
									}
								} else if (paytype == 1) {//钱包
									ChargeRecordHandler.updateUserMoney(money, uid, null, 2);
									ChargeRecordHandler.updateChargeRefundNumer(1, endChargeId, money, money, 0, 0);
									logger.info("wolflog---" + "钱包支付充电失败，已退款。\r\n 注：该退款到平台钱包里。");
									
									//============================================================================
									Double topupmoney = CommUtil.toDouble(userBalance);//充值金额
									Double sendmoney = CommUtil.toDouble(usersendmoney);//赠送金额
									Double accountmoney = CommUtil.addBig(userBalance, usersendmoney);//账户金额
									//============================================================================
									Double opermoney = CommUtil.toDouble(money);//操作总额
									Double opertopupmoney = 0.00;	//操作充值金额
									Double opersendmoney = 0.00;	//操作赠送金额
									//==========================================================================
									Double topupbalance = 0.00;//充值余额
									Double sendbalance = 0.00;//赠送余额
									if(topupmoney>0) {
										opertopupmoney = opermoney;
										topupbalance = CommUtil.addBig(topupmoney, opertopupmoney);
										opersendmoney = 0.00;
										sendbalance = sendmoney;
									}else{
										opertopupmoney = 0.00;
										topupbalance = 0.00;
										opersendmoney = opermoney;
										sendbalance = CommUtil.addBig(sendmoney, opersendmoney);
									}
									Double accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
									
									Money moneyEntity = new Money();
									moneyEntity.setUid(uid);
									moneyEntity.setOrdernum(chargeRecord.getOrdernum());
									moneyEntity.setPaytype(3);
									moneyEntity.setStatus(1);
									moneyEntity.setMoney(opertopupmoney);
									moneyEntity.setSendmoney(opersendmoney);
									moneyEntity.setTomoney(opermoney);
									moneyEntity.setBalance(accountbalance);
									moneyEntity.setTopupbalance(topupbalance);
									moneyEntity.setGivebalance(sendbalance);
									moneyEntity.setRemark("wallet");
									ChargeRecordHandler.addMoneyRecord(moneyEntity);
									UserHandler.addGenDetail(chargeRecord.getUid(), merchantid, chargeRecord.getOrdernum(),
											opertopupmoney, opersendmoney, opermoney, accountbalance, topupbalance, sendbalance, 5, null);
//									UserHandler.addGenDetail(chargeRecord.getUid(), merchantid, chargeRecord.getOrdernum(),
//											opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance , 5, null);
//									UserHandler.addGenDetail(chargeRecord.getUid(), merchantid, chargeRecord.getOrdernum(),
//											chargeRecord.getExpenditure(), calcBalance, 5, null);
									TradeRecordHandler.addTradeRecord(chargeRecord.getMerchantid(), 0,
											chargeRecord.getUid(), chargeRecord.getOrdernum(),
											chargeRecord.getExpenditure(), 0, 0, code, 1, 1, 2);
									uid = CommUtil.toInteger(uid);
									Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
									Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
									Double refundMoney = paymoney;//全额退款退款金额等于付款金额
									Double consumemoney =  CommUtil.subBig(paymoney, refundMoney);//消费金额
									Integer orderid = CommUtil.toInteger(chargeRecord.getId());
									String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
									String deviceport = CommUtil.toString(port);
									Integer resultinfo = CommUtil.toInteger(reason);
									String startTime = CommUtil.toDateTime(chargeRecord.getBegintime());
									String endTime = CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date());
									String chargetime = "开始时间："+startTime + "\r\n结束时间：" + endTime;
									String devicenum = code;
									Equipment devicedata = Equipmenthandler.getEquipmentData(devicenum);
									Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
									Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
									String devicename = CommUtil.toString(devicedata.getRemark());
									String areaaddress = CommUtil.toString(devicedata.getAddress());
									String servicphone = getServicePhone(devicetempid, deviceaid, merid);
									
									String urltem = "";
									User user = UserHandler.getUserInfo(uid);
									String oppenid = user.getOpenid();
									String returnfirst = "您好，支付充电失败，金额自动原路退款到账户。";
									String servername = "钱包全额退款";
									TempMsgUtil.returnMsgTemp(returnfirst, oppenid, servername, ordernum, endTime, paymoney, servicphone, CommonConfig.PAYRETURNMES, urltem);
									
									TempMsgUtil.endChargeSendMess("您好，充电已结束", ordernum, orderid, resultinfo, merid, uid, consumemoney, chargetime, 
											devicenum, deviceport, devicename, deviceaid, areaaddress, servicphone);
									
								} else if (paytype == 2) {// 微信
									WXPayConfigImpl config;
									try {
										config = WXPayConfigImpl.getInstance();
										WXPay wxpay = new WXPay(config);
										String total_fee = "";
										String out_trade_no = "";
										String moneyStr = String.valueOf(chargeRecord.getExpenditure() * 100);
										int idx = moneyStr.lastIndexOf(".");
										total_fee = moneyStr.substring(0, idx);
										out_trade_no = chargeRecord.getOrdernum();// 退款订单号
										SortedMap<String, String> params = new TreeMap<>();
										String subMer = UserHandler.selectSubMerConfig(chargeRecord.getMerchantid());
										String subMchId = null;
										// subMer为空表示为普通商户
										if(subMer != null && !"".equals(subMer)){
											subMchId = subMer;
											logger.info("使用微信特约商户号:"+subMchId+"进行退款");
										}else{
											subMchId = WeiXinConfigParam.SUBMCHID;
											logger.info("使用服务平台商户号:"+subMchId+"进行退款");
										}
										params.put("appid", WeiXinConfigParam.FUWUAPPID);
										params.put("mch_id", WeiXinConfigParam.MCHID);
										params.put("sub_mch_id",subMchId);
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
													TradeRecord tradere = TradeRecordHandler.getTradereInfo(out_trade_no);
													String comment = CommUtil.toString(tradere.getComment());
													Integer manid = tradere.getManid();
													int aid = Equipmenthandler.getEquipmentByCode(code);
													if (aid != 0) {
														UserHandler.areaRefund(aid, money);
													}
													UserHandler.equRefund(code, money);
													//TODO 充电退费
													// 更新微信特约商户的
													if(subMer != null && !"".equals(subMer)){
														// 查询特约商户余额
														Double merEarnings = CommUtil.toDouble(UserHandler.getUserEarningsById(chargeRecord.getMerchantid()));
														// 微信特约商户只更新收益统计信息
														UserHandler.merAmountRefund(chargeRecord.getMerchantid(), money);
														// 添加特约商户余额明细
														UserHandler.addMerDetail(chargeRecord.getMerchantid(), out_trade_no, money, merEarnings, MerchantDetail.CHARGESOURCE, paytype, MerchantDetail.REFUND);
													}else{
														// 更新合伙人收益信息
														dealerIncomeRefund(comment, chargeRecord.getMerchantid(), money, out_trade_no, 
																new Date(), MerchantDetail.CHARGESOURCE, paytype, MerchantDetail.REFUND);
													}
													//TODO 添加交易记录
													TradeRecordHandler.addTradeRecordInfo(chargeRecord.getMerchantid(),
															manid, chargeRecord.getUid(), chargeRecord.getOrdernum(),
															chargeRecord.getExpenditure(), tradere.getMermoney(),
															tradere.getManmoney(), code, 1, 2, 2,tradere.getHardver(),comment);
													ChargeRecordHandler.updateChargeRefundNumer(1, endChargeId,
															money, money, 1, 0);
													uid = CommUtil.toInteger(uid);
													Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
													Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
													Double refundMoney = paymoney;//全额退款退款金额等于付款金额
													Double consumemoney =  CommUtil.subBig(paymoney, refundMoney);//消费金额
													Integer orderid = CommUtil.toInteger(chargeRecord.getId());
													String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
													String deviceport = CommUtil.toString(port);
													Integer resultinfo = CommUtil.toInteger(reason);
													String startTime = CommUtil.toDateTime(chargeRecord.getBegintime());
													String endTime = CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date());
													String chargetime = "开始时间："+startTime + "\r\n结束时间：" + endTime;
													
													String devicenum = code;
													Equipment devicedata = Equipmenthandler.getEquipmentData(devicenum);
													Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
													Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
													String devicename = CommUtil.toString(devicedata.getRemark());
													String areaaddress = CommUtil.toString(devicedata.getAddress());
													String servicphone = getServicePhone(devicetempid, deviceaid, merid);
													String urltem = "";
													User user = UserHandler.getUserInfo(uid);
													String oppenid = user.getOpenid();
													String returnfirst = "您好，支付充电失败，金额自动原路退款到账户。";
													String servername = "微信全额退款";
													TempMsgUtil.returnMsgTemp(returnfirst, oppenid, servername, ordernum, endTime, paymoney, servicphone, CommonConfig.PAYRETURNMES, urltem);
													
													TempMsgUtil.endChargeSendMess("您好，充电已结束", ordernum, orderid, resultinfo, merid, uid, consumemoney, chargetime, 
															devicenum, deviceport, devicename, deviceaid, areaaddress, servicphone);
													
												}
												if ("FAIL".equals(r.get("result_code"))) {
												}
											} catch (Exception e) {
												logger.error(chargeRecord.getOrdernum() + e.getMessage() + "微信退款失败");
											}
										}
									} catch (Exception e) {
										logger.error(chargeRecord.getOrdernum() + e.getMessage() + "微信退款失败");
									}
									// 结束充电微信发送通知
									logger.info("wolflog---" + "微信支付充电失败，已退款");
								} else if (paytype == 3 || paytype == 8) {
									AlipayClient alipayClient = new DefaultAlipayClient(
											"https://openapi.alipay.com/gateway.do", AlipayConfig.FUWUPID,
											AlipayConfig.RSA_PRIVATE_KEY3, "json", "GBK", AlipayConfig.ALIPAY_PUBLIC_KEY3,
											"RSA2");
									if (paytype == 8) {
										alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPLETAPPID,
												AlipayConfig.APPLETRSA_PRIVATE_KEY, "json", "GBK", AlipayConfig.APPLETALIPAY_PUBLIC_KEY, "RSA2");
										paytype = 3;
									}
									AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
									request.setBizContent("{" + "\"out_trade_no\":\"" + chargeRecord.getOrdernum() + "\","
											+ "\"refund_amount\":" + chargeRecord.getExpenditure() + "  }");
									
									AlipayTradeRefundResponse response;
									try {
										response = alipayClient.execute(request);
										if (response.isSuccess()) {//TODO  origin RZC  支付宝充电异常 自动退费处理
											logger.error("支付宝支付充电成功，已退款");
											TradeRecord tradere = TradeRecordHandler.getTradereInfo(chargeRecord.getOrdernum());
											String comment = CommUtil.toString(tradere.getComment());
											Integer manid = tradere.getManid();
											int aid = Equipmenthandler.getEquipmentByCode(code);
											if (aid != 0) {
												UserHandler.areaRefund(aid, money);
											}
											UserHandler.equRefund(code, money);
											//TODO 充电退费
											dealerIncomeRefund(comment, chargeRecord.getMerchantid(), money, chargeRecord.getOrdernum(), 
													new Date(), MerchantDetail.CHARGESOURCE, paytype, MerchantDetail.REFUND);
											logger.error("支付宝支付充电成功，已退款");
											//TODO 添加交易记录
											TradeRecordHandler.addTradeRecordInfo(chargeRecord.getMerchantid(),
													0, chargeRecord.getUid(), chargeRecord.getOrdernum(),
													chargeRecord.getExpenditure(), tradere.getMermoney(),
													tradere.getManmoney(), code, 1, 3, 2,tradere.getHardver(),comment);
											ChargeRecordHandler.updateChargeRefundNumer(1, endChargeId,
													chargeRecord.getExpenditure(), money, 0, 0);
										} else {
											logger.error(chargeRecord.getOrdernum() + "支付宝支付充电失败");
										}
									} catch (AlipayApiException e) {
										logger.error(chargeRecord.getOrdernum() + e.getMessage() + "支付宝支付充电失败");
									}
								} else {
									logger.warn("根据实用充电信息计算退款信息");
								}
							} else {
								if (paytype == 1 || paytype == 2) {
									boolean checkBind = UserHandler.checkUserBindMidIsMerId(uid, merchantid);
									TemplateParent templateParent = Equipmenthandler.getTempPermit(code);
									int permit = 0;
									int norm = 1;
									if (templateParent != null) {
										permit = templateParent.getPermit();
										norm = templateParent.getCommon2();
									}
									if (permit == 1) {
										if (checkBind) {
											short power = Equipmenthandler.queryRealchargerecordMaxPower(endChargeId);
											loopRefund(endChargeId, currentTime, begintime, code, port, time, elec, norm, power, reason, hardversion);
										} else {
											logger.warn(chargeRecord.getOrdernum() + "商户绑定与充电设备绑定的商户不一致，不予退款");
										}
									} else {
										logger.warn(chargeRecord.getOrdernum() + "商户设置不可钱包退款");
									}
								}
							}
						}
						ChargeRecordHandler.updateChargeRecordStopCharging(endChargeId, reason + 0);
						Equipmenthandler.addRealchargerecord(endChargeId, chargeRecord.getUid(), chargeRecord.getMerchantid(), code, port, 0, 0, 0, -1.0, -1.0, -1.0);
					}
				} 
//				else {
//					if (chargeRecord != null) {
//						ChargeRecordHandler.updateChargeRecordStopCharging(chargeRecord.getId(), reason + 0);
//					}
//				}
				Map<String,String> hashMap = new HashMap<>();
				hashMap.put("session_id", session_id + "");
				hashMap.put("updateTime", System.currentTimeMillis() + "");
				Server.endChargeInfoMap.put("" + code + port, hashMap);
			} else {
				logger.info("wolflog---" + code + "号机提交充电结束状态信息重复，不予处理");
			}
			logger.info("wolflog---" + code + "号机提交充电结束状态信息已接收");
//			AllPortStatusHandler.updateAllPortStatus(code, port, (byte) 1, (short) 0, (short) 0, (short) 0, 0.0, 0.0);
			operRedisAllPortStatus(code, port, time, elec, "1");
		} catch (Exception e) {
			logger.info("wolflog---" + code + "号设备新版提交充电结束状态数据解析异常---");
			e.printStackTrace();
		}
	}
	
	public static void parse_0x2d(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		String code = getCodeByIpaddr(channel);
		if (code == null || "".equals(code)) {
			return;
		}
		try {
			byte port = buffer.get();
			short time = buffer.getShort();
			short elec = buffer.getShort();
			byte type = buffer.get();
			byte coin_num = buffer.get();
			int card_idInt = buffer.getInt();
			short card_cst = buffer.getShort();
			byte card_ope = buffer.get();
			byte card_type = buffer.get();
			int session_id = buffer.getInt();
			byte sum = buffer.get();
			String card_idStr = buquan8wei(Integer.toHexString(card_idInt)).toUpperCase();
			Map<String,String> map = new HashMap<>();
			map.put("port", port + "");
			map.put("time", time + "");
			map.put("elec", elec + "");
			map.put("type", type + "");
			map.put("coin_num", coin_num + "");
			map.put("card_idInt", card_idStr);
			map.put("card_cst", card_cst + "");
			map.put("card_ope", card_ope + "");
			map.put("card_type", card_type + "");
			map.put("session_id", session_id + "");
			logger.info("wolflog---" + code + "号设备" + port + "端口新版上报开始充电信息数据已接收");
			send_0x2d(code, session_id);
			String session_idStr = Server.beginChargeInfoMap.get(code + port + "");
			logger.info("wolflog---" + JSON.toJSONString(map));
			if (session_idStr == null || !session_idStr.equals(session_id + "")) {
				String ordernum = HttpRequest.createOrdernum(6);
				Integer merchantid = Equipmenthandler.findUserEquipment(code);
//				if (type == 0) {
//					int ifcontinue = getIfcontinue(code, port, time);
//					ChargeRecordHandler.addChargeRecord(merchantid, 0, ordernum, 5, 1, code, port + 0, coin_num, time + "", elec + 0, -1, ifcontinue);
//				} else 
				if (type == 1) {
					OnlineCard onlineCard = OnlineCardHandler.cardIsExist(card_idStr);
					if (card_type == 0) {
						int ifcontinue = getIfcontinue(code, port, time);
						ChargeRecordHandler.addChargeRecord(merchantid, 0, ordernum, 6, 1, code, port + 0, (card_cst + 0.0)/10, time + "", elec + 0, -1, ifcontinue);
						OfflineCardHandler.addOfflineCard(merchantid, 0, ordernum, code, card_idStr, -1.0, (card_cst + 0.0)/10, (card_cst + 0.0)/10, 0.0, 7, 1, 0, 0);
					} else if (card_type == 1) {
//				OnlineCardHandler.addOnlineCardOperate(onlineCard.getUid(), onlineCard.getMerid(), ordernum, card_idStr, code, balance, (card_cst + 0.0)/10, (card_cst + 0.0)/10, 1, 1);
						Double money = CommUtil.toDouble(onlineCard.getMoney());
						Double sendmoney = CommUtil.toDouble(onlineCard.getSendmoney());
						Double balance = CommUtil.addBig(money, sendmoney);
						Double consumemoney = CommUtil.toDouble(card_cst)/10;
						Double topupmoney = 0.00;
						Double givemoney = 0.00;
						if (onlineCard != null) {
							int ifcontinue = getIfcontinue(code, port, time);
							ChargeRecordHandler.addChargeRecord(onlineCard.getMerid(), onlineCard.getUid(), ordernum, 7, 1, code, port + 0, (card_cst + 0.0)/10, time + "", elec + 0, -1, ifcontinue);
							
							Double recharge = CommUtil.subBig(money, consumemoney);
							if(recharge>=0){
								topupmoney = consumemoney;
								money = CommUtil.subBig(money, topupmoney);
							}else{
								topupmoney = money;
								givemoney = CommUtil.toDouble(Math.abs(recharge));
								sendmoney = CommUtil.subBig(sendmoney, givemoney);
								money = 0.00;
							}
							Double topupbalance = money;
							Double givebalance = sendmoney;
							Double cardmoney = CommUtil.subBig(topupbalance, givebalance); 
							OnlineCardHandler.updateCardMoney( topupmoney, card_idStr, 0, sendmoney);
							OnlineCardHandler.addOnlineCardOperate(onlineCard.getUid(), onlineCard.getMerid(), ordernum, card_idStr, code, cardmoney, topupmoney, givemoney, consumemoney, topupbalance, givebalance, 1, 1);
						}
					} else if (card_type == 4) {
						Integer relevawalt = onlineCard.getRelevawalt();
						Double money = onlineCard.getMoney();
						Double sendmoney = onlineCard.getSendmoney();
						double balance = CommUtil.addBig(money, sendmoney);
						if (relevawalt == 1) {
							User user = UserHandler.getUserInfo(onlineCard.getUid());
							balance = CommUtil.addBig(user.getBalance(), user.getSendmoney());
						}
						if (balance > 0) {
							int tempRank = Equipmenthandler.getTempRank(code);
							logger.info("充满自停卡下发时间---" + tempRank);
							ChargeRecordHandler.addChargeRecord(onlineCard.getMerid(), onlineCard.getUid(), ordernum, 7, 1, code, port + 0, 0, "0", 0, -1, 0);
							operRedisAllPortStatus(code, port, time, elec, "2");
							resetChargeData(code, port, onlineCard.getUid(), balance, 0);
							send_0x27(port, (short) 0, (short) tempRank, (short) 0, code, (byte) 3);
							Map<String,String> chargeInfoMap = new HashMap<>();
							chargeInfoMap.put("cardid", card_idStr);
							chargeInfoMap.put("type", "1");
							JedisUtils.hmset(ordernum, chargeInfoMap);
						}
					}
				}
			}
			buffer.position(1);
			byte[] datas = new byte[100];
			buffer.get(datas, 0, len);
			byte checkoutSum = checkoutSum(datas);
			if (sum == checkoutSum) {
				logger.info("wolflog---" + "数据发送验证成功");
			} else {
				logger.info("wolflog---" + "数据发送验证失败");
			}
		} catch (Exception e) {
			logger.info("wolflog---" + code + "号设备新版上报开始充电信息数据解析异常---" + e.getMessage());
		}
	}
	
	public static void parse_0x2a(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		String code = getCodeByIpaddr(channel);
		try {
			byte data = buffer.get();
			byte sum = buffer.get();
			buffer.position(1);
			byte[] datas = new byte[100];
			buffer.get(datas, 0, len);
			byte checkoutSum = checkoutSum(datas);
			if (sum == checkoutSum) {
				logger.info("wolflog---" + "数据发送验证成功");
			} else {
				logger.info("wolflog---" + "数据发送验证失败");
			}
			logger.info("wolflog---" + code + "号设备设置系统参数回复信息已接收");
			Map<String,String> map = new HashMap<>();
			map.put("updateTime", System.currentTimeMillis() + "");
			Server.setSystemMap.put(code, map);
		} catch (Exception e) {
			logger.info("wolflog---" + code + "号设备设置系统参数回复异常---" + e.getMessage());
		}
	}
	
	public static void parse_0x2b(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		String code = getCodeByIpaddr(channel);
		try {
			short cst = buffer.getShort();
			short elec_pri = buffer.getShort();
			logger.info("wolflog---" + "设置刷卡扣费金额(单位为角)===" + (cst & 0xffff) + "-设置电量价格(单位为分)==="
					+ (elec_pri & 0xffff));
			byte sum = buffer.get();
			buffer.position(1);
			byte[] datas = new byte[100];
			buffer.get(datas, 0, len);
			byte checkoutSum = checkoutSum(datas);
			if (sum == checkoutSum) {
				logger.info("wolflog---" + "数据发送验证成功");
			} else {
				logger.info("wolflog---" + "数据发送验证失败");
			}
			logger.info("wolflog---" + code + "号设备读取系统参数回复信息已接收");
			Map<String,String> map = new HashMap<>();
			double cstDouble = ((cst & 0xffff) + 0.0)/10;
			double elec_priDouble = ((elec_pri & 0xffff) + 0.0)/100;
			map.put("cst", cstDouble + "");
			map.put("elec_pri", elec_priDouble + "");
			map.put("updateTime", System.currentTimeMillis() + "");
			Server.readsystemMap.put(code, map);
		} catch (Exception e) {
			logger.info("wolflog---" + code + "号设备读取系统参数回复异常---" + e.getMessage());
		}
	}
	
	public static void parse_0x2E(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		String code = getCodeByIpaddr(channel);
		if (code != null) {
			try {
				byte port = buffer.get();//端口号
				short time = buffer.getShort();//时间
				short power = buffer.getShort();//功率
				short elec = buffer.getShort();//电量
				short surp = buffer.getShort();//可回收余额
				short portV = buffer.getShort();//当前电压
				short portA = buffer.getShort();//当前电流
				short maxTime = buffer.getShort();//最长充电时间
				byte sum = buffer.get();
				Map<String,String> map = new HashMap<>();
				map.put("port", port + "");
				map.put("time", time + "");
				map.put("power", power + "");
				map.put("elec", elec + "");
				map.put("surp", surp + "");
				map.put("portV", portV + "");
				map.put("portA", portA + "");
				map.put("maxTime", maxTime + "");
				logger.info(code + "号设备新版查询当前的充电状态已接收，详细参数：" + JSON.toJSONString(map));
			} catch (Exception e) {
				logger.info(code + "号设备新版查询当前的充电状态数据解析异常");
				e.printStackTrace();
			}
		}
	}
	
	public static void parse_0x2F(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		String code = getCodeByIpaddr(channel);
		if (code != null) {
			try {
				byte port = buffer.get();
				if (port <= 0) {
					port = 1;
				}
				logger.info("wolflog---" + code + "号设备获取设备二维码已接收，端口为:" + port);
				send_0x2F(code, port);
			} catch (Exception e) {
				logger.info("wolflog---" + code + "号设备获取设备二维码数据接收异常---" + e.getMessage());
			}
		} else {
			logger.info("wolflog---" + "获取设备号为空，不予处理");
		}
	}
	
//	public static void parse_0x2F(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
//			ByteBuffer buffer) {
//		String code = getCodeByIpaddr(channel);
//		logger.info("wolflog---" + code + "号设备已知需要升级，准备中");
//	}

	public String parse_0x81(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Map<String, Map> allMap = new HashMap<>();
		Map<String, List<String>> map = new HashMap<>();
		String code = disposeImei(channel, len, cmd, result, buffer, allMap, map);
		return code;
	}
	
	/**
	 * 离线充值机退款
	 * @param channel
	 * @param len
	 * @param cmd
	 * @param result
	 * @param buffer
	 * @return
	 */
	public static Map<String, Map<String, String>> parse_0x22(AsynchronousSocketChannel channel, byte len, byte cmd,
			byte result, ByteBuffer buffer) {
		Map<String, Map<String, String>> allMap = new HashMap<>();
		Map<String, String> map = new HashMap<>();
		int card_id = buffer.getInt();
		short card_surp = buffer.getShort();
		byte res = buffer.get();
		map.put("card_id", Integer.toHexString(card_id));
		map.put("card_surp", card_surp + "");
		String str;
		if (res == 0) {
			str = "操作成功";
		} else if (res == 1) {
			str = "余额不足";
		} else if (res == 2) {
			str = "无卡";
		} else if (res == 3) {
			str = "下发卡号和充卡机上卡号不对";
		} else if (res == 4) {
			str = "其他错误";
		} else {
			str = "网络错误";
		}
		map.put("res", str);
		map.put("result", res + "");
		map.put("nowtime", System.currentTimeMillis() + "");
		byte sum = buffer.get();
		buffer.position(1);
		byte[] datas = new byte[100];
		buffer.get(datas, 0, len);
		byte checkoutSum = checkoutSum(datas);
		if (sum == checkoutSum) {
			logger.info("wolflog---" + "数据发送验证成功");
		} else {
			logger.info("wolflog---" + "数据发送验证失败");
		}
		long currentTime = System.currentTimeMillis();
		String code = getCodeByIpaddr(channel);
		if (res != 0) {
			OfflineCard offlineCard = OfflineCardHandler.getOfflineCardEndByParam(code);
			if (offlineCard != null) {
				long begintime = offlineCard.getBeginTime().getTime();
				Integer paytype = offlineCard.getPaytype();
				String ordernum = offlineCard.getOrdernum();
				Double chargemoney = offlineCard.getChargemoney();
				Integer merchantid = offlineCard.getMerchantid();
				Integer uid = offlineCard.getUid();
				Integer id = offlineCard.getId();
				Double money = offlineCard.getChargemoney();
				String sendurl = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=2&id="+offlineCard.getId();
				try {
					if ((currentTime - begintime) > 0 && (currentTime - begintime) <= 120000) {
						if (paytype == 1) {// 微信
							WXPayConfigImpl config;
							try {
								config = WXPayConfigImpl.getInstance();
								WXPay wxpay = new WXPay(config);
								String total_fee = "";
								String out_trade_no = "";
								String moneyStr = String.valueOf(chargemoney * 100);
								int idx = moneyStr.lastIndexOf(".");
								total_fee = moneyStr.substring(0, idx);
								out_trade_no = ordernum;// 退款订单号
								String subMer = UserHandler.selectSubMerConfig(offlineCard.getMerchantid());
								SortedMap<String, String> params = new TreeMap<>();
								String subMchId = null;
								// subMer为空表示为普通商户
								if(subMer != null && !"".equals(subMer)){
									subMchId = subMer;
									logger.info("使用微信特约商户号:"+subMchId+"进行退款");
								}else{
									subMchId = WeiXinConfigParam.SUBMCHID;
									logger.info("使用服务平台商户号:"+subMchId+"进行退款");
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
											TradeRecord tradere = TradeRecordHandler.getTradereInfo(out_trade_no);
											String comment = CommUtil.toString(tradere.getComment());
											Integer manid = tradere.getManid();
											int aid = Equipmenthandler.getEquipmentByCode(code);
											if (aid != 0) {
												UserHandler.areaRefund(aid, money);
											}
											UserHandler.equRefund(code, money);
											OfflineCardHandler.findChargeCardEndIdIsUnfinished(id, 3);
											User meruser = UserHandler.getUserInfo(merchantid);
											// 微信特约商户只记录资金统计信息
											if(subMer != null && !"".equals(subMer)){
												logger.info("开始统计微信特约商户:"+merchantid+"资金信息");
												//查询商户余额
												Double merEarnings = CommUtil.toDouble(UserHandler.getUserEarningsById(merchantid));
												UserHandler.merAmountRefund(merchantid, money);
												//添加商户余额明细
												UserHandler.addMerDetail(merchantid, ordernum, money, merEarnings, MerchantDetail.OFFLINESOURCE, 2, MerchantDetail.REFUND);
											}else{
												logger.info("开始统计普通商户:"+merchantid+"资金信息");
												// 离线充值机微信退费
												dealerIncomeRefund(comment, merchantid, money, ordernum, new Date(), 
														MerchantDetail.OFFLINESOURCE, 2, MerchantDetail.REFUND);
											}
											// 添加交易记录
											TradeRecordHandler.addTradeRecordInfo(merchantid, manid, uid, ordernum, 
												chargemoney, tradere.getMermoney(), tradere.getManmoney(), code, 
												3, 2, 2,tradere.getHardver(),comment);
											TempMsgUtil.returnMsg("微信退费", uid, meruser.getPhoneNum(), ordernum, sendurl, 
													new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), chargemoney);
											OfflineCardHandler.findChargeCardEndIdIsUnfinished(id, 3);
											/*
											if (null == manid || manid == 0) {
												ChargeRecordHandler.updateUserMoney(chargemoney, merchantid, 1);
												Double earningsById = UserHandler.getUserEarningsById(merchantid);
												UserHandler.addMerDetail(merchantid, out_trade_no, chargemoney,
														earningsById, MerchantDetail.CHARGESOURCE, paytype,
														MerchantDetail.REFUND);
												TradeRecordHandler.addTradeRecord(merchantid, 0, uid, ordernum,
														chargemoney, chargemoney, 0.00, code, 3, 2, 2);
												try {
													UserHandler.merAmountRefund(merchantid, money);
												} catch (Exception e) {
													logger.warn("修改商户总计表失败-_-");
												}
											} else {
												OfflineCardHandler.findChargeCardEndIdIsUnfinished(id, 3);
												ChargeRecordHandler.updateUserMoney(tradere.getManmoney(), manid, 1);
												Double manearn = UserHandler.getUserEarningsById(manid);
												UserHandler.addMerDetail(manid, out_trade_no, tradere.getManmoney(),
														manearn, MerchantDetail.CHARGESOURCE, paytype,
														MerchantDetail.REFUND);
												Double merearn = UserHandler.getUserEarningsById(merchantid);
												UserHandler.addMerDetail(merchantid, out_trade_no,
														tradere.getMermoney(), merearn, MerchantDetail.CHARGESOURCE,
														paytype, MerchantDetail.REFUND);
												TradeRecordHandler.addTradeRecord(merchantid, manid, uid, ordernum,
														chargemoney, tradere.getMermoney(), tradere.getManmoney(), code,
														3, 2, 2);
												try {
													UserHandler.merAmountRefund(manid, tradere.getManmoney());
													UserHandler.merAmountRefund(merchantid, tradere.getMermoney());
												} catch (Exception e) {
													logger.warn("修改商户总计表失败-_-");
												}
											}
											OfflineCardHandler.findChargeCardEndIdIsUnfinished(id, 3);
											TempMsgUtil.returnMsg("微信退费", uid, merchantid, ordernum, sendurl, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), chargemoney);
										*/
										}
										if ("FAIL".equals(r.get("result_code"))) {
											// ChargeRecordHandler.updateChargeRefundNumer(endChargeId);
										}
									} catch (Exception e) {
										logger.error(ordernum + e.getMessage() + "微信退款失败");
									}
								}
							} catch (Exception e) {
								logger.error(ordernum + e.getMessage() + "微信退款失败");
							}
							// 结束充电微信发送通知
							logger.info("wolflog---" + "微信支付充电失败，已退款");
						} else if (paytype == 2 || paytype == 8) {
							AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
									AlipayConfig.FUWUPID, AlipayConfig.RSA_PRIVATE_KEY3, "json", "GBK",
									AlipayConfig.ALIPAY_PUBLIC_KEY3, "RSA2");
							if (paytype == 8) {
								alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPLETAPPID,
										AlipayConfig.APPLETRSA_PRIVATE_KEY, "json", "GBK", AlipayConfig.APPLETALIPAY_PUBLIC_KEY, "RSA2");
								paytype = 3;
							}
							AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
							request.setBizContent("{" + "\"out_trade_no\":\"" + ordernum + "\"," + "\"refund_amount\":"
									+ chargemoney + "  }");

							AlipayTradeRefundResponse response;
							try {
								response = alipayClient.execute(request);
								if (response.isSuccess()) {
									// 支付宝退款 分合伙人
									TradeRecord tradere = TradeRecordHandler.getTradereInfo(ordernum);
									String comment = CommUtil.toString(tradere.getComment());
									Integer manid = tradere.getManid();
									int aid = Equipmenthandler.getEquipmentByCode(code);
									if (aid != 0) {
										UserHandler.areaRefund(aid, money);
									}
									UserHandler.equRefund(code, money);
									
									OfflineCardHandler.findChargeCardEndIdIsUnfinished(id, 4);
									// 离线充值机支付宝退费
									dealerIncomeRefund(comment, merchantid, money, ordernum, new Date(), 
											MerchantDetail.OFFLINESOURCE, 3, MerchantDetail.REFUND);
									// 添加交易记录
									TradeRecordHandler.addTradeRecordInfo(merchantid, manid, uid, ordernum, 
										chargemoney, tradere.getMermoney(), tradere.getManmoney(), code, 
										3, 3, 2,tradere.getHardver(),comment);
									
									OfflineCardHandler.findChargeCardEndIdIsUnfinished(id, 4);
									logger.error("支付宝支付，已退款");
								} else {
									// ChargeRecordHandler.updateChargeRefundNumer(endChargeId);
									logger.error(ordernum + "支付宝退款失败");
								}
							} catch (AlipayApiException e) {
								logger.error(ordernum + e.getMessage() + "支付宝退款失败");
							}
						}
					} else {
						logger.warn("根据实用充电信息计算退款信息");
					}
				} catch (Exception e) {
					logger.warn("退款失败，订单号为：" + ordernum + e.getMessage());
				}
			}
		}
		OfflineCardHandler.findChargeCardEndIdIsUnfinished(res, Integer.toHexString(card_id), card_surp);
		allMap.put(getCodeByIpaddr(channel), map);
		return allMap;
	}
	
	/**
	 * @Description： 结束充电发送消息模板处理
	 * @author： origin 创建时间：   2019年9月21日 下午4:06:32
	 */
	public static void finishchargesendmsg(String info, Integer uid, Integer merid, String code, Date beginTime,
			Date endTime, Integer codeid) {
		try {
			User user = UserHandler.getUserInfo(uid);
			if (null != user) {
				String oppenid = user.getOpenid();
				String urltem = CommonConfig.ZIZHUCHARGE + "/general/sendmsgdetails?source=1&id=" + codeid;
				User meruser = UserHandler.getUserInfo(merid);
				String phone = "";
				if (meruser != null)
					phone = meruser.getPhoneNum();
				JSONObject json = new JSONObject();
				json.put("first", TempMsgUtil.inforData(info, "#FF3333"));
				json.put("keyword1", TempMsgUtil.inforData(code, "#0044BB"));
				json.put("keyword2",
						TempMsgUtil.inforData(StringUtil.toDateTime("yyyy-MM-dd HH:mm:ss", beginTime), "#0044BB"));
				json.put("keyword3",
						TempMsgUtil.inforData(StringUtil.toDateTime("yyyy-MM-dd HH:mm:ss", endTime), "#0044BB"));
				json.put("remark", TempMsgUtil.inforData("如有疑问请联系商户：" + phone, "#0044BB"));
				TempMsgUtil.sendTempMsg(oppenid, TempMsgUtil.TEMP_IDEND, urltem, json);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
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
				if(partid != 0){//合伙人信息
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
	 * @Description： 收入信息处理
	 * @author： origin 创建时间：   2019年9月15日 上午11:44:49
	 */
	public static Map<String, Object> partnerIncomeDispose(List<Map<String, Object>> partInfo, Integer merid, Double money, String ordernum, 
			Date time, Integer paysource, Integer paytype, Integer paystatus) {
		Double mermoney = 0.00;//商户金额
		Double manmoney = 0.00;//商户金额
		Double tolpercent = 0.00;//分成比
		Map<String, Object> mapresult = new HashMap<>();
		List<Map<String, Object>> merchlist = new ArrayList<>();
		try {
			if(partInfo.size()>0){//分成
				logger.info("wolflog---" + "输出分成");
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
					merEearningCalculate( partid, partmoney, 3, ordernum, time, paysource, paytype, paystatus);
				}
			}
			mermoney = CommUtil.toDouble((money *100 * (1- tolpercent))/100);
			manmoney = CommUtil.subBig(money, mermoney);
			logger.info("输出商户收益mermoney "+mermoney+"    ****   "+manmoney);
			Map<String, Object> map = new HashMap<>();
			map.put("merid", merid);
			map.put("percent", (1-tolpercent));
			map.put("money", mermoney);
			merchlist.add(map);
			merEearningCalculate( merid, mermoney, 3, ordernum, time, paysource, paytype, paystatus);
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
			try {
				if(merEarnings!=0){
					if(type==1){
						nowEarnings = (merEarnings * 100 - money * 100) / 100;
						UserHandler.merAmountRefund(merid, money);
					}else if(type==3){
						nowEarnings = (merEarnings * 100 + money * 100) / 100;
						UserHandler.merAmountAddMoney(merid, money);
					}
				}
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

	public static Map<String, Map<String, String>> parse_0x82(AsynchronousSocketChannel channel, byte len, byte cmd,
			byte result, ByteBuffer buffer) {
		Map<String, Map<String, String>> allMap = new HashMap<>();
		Map<String, String> map = new HashMap<>();
		String codeByIpaddr = getCodeByIpaddr(channel);
		byte coinsSum = buffer.get();
		byte[] coinsSums = ByteUtils.getByteArray(coinsSum);
		byte sum = buffer.get();
		StringBuffer sb = new StringBuffer();
		sb.append(codeByIpaddr + "号机支持" + (coinsSum >> 4) + "个投币通道,其中");
		for (int i = 0; i < 4; i++) {
			if (coinsSums[i] == 1) {
				sb.append((i + 1) + ", ");
			}
		}
		sb.toString().substring(sb.toString().lastIndexOf(","));
		sb.append("可用");
		logger.info("wolflog---" + sb.toString());
		buffer.position(1);
		byte[] datas = new byte[100];
		buffer.get(datas, 0, len);
		byte checkoutSum = checkoutSum(datas);
		if (sum == checkoutSum) {
			logger.info("wolflog---" + "数据发送验证成功");
		} else {
			logger.info("wolflog---" + "数据发送验证失败");
		}
		map.put("coinsSums", coinsSums + "");
		map.put("sums", (coinsSum >> 4) + "");
		map.put("nowtime", System.currentTimeMillis() + "");
		allMap.put(codeByIpaddr, map);
		return allMap;
	}

	public Map<String, Map<String, String>> parse_0x83(AsynchronousSocketChannel channel, byte len, byte cmd,
			byte result, ByteBuffer buffer) {
		Map<String, Map<String, String>> allMap = new HashMap<>();
		Map<String, String> map = new HashMap<>();
		String codeByIpaddr = getCodeByIpaddr(channel);
		byte results = buffer.get();
		byte sum = buffer.get();
		logger.info("wolflog---" + codeByIpaddr + "号机投币完成已接收");
		map.put("testNowtime", System.currentTimeMillis() + "");
		allMap.put(codeByIpaddr, map);
		buffer.position(1);
		if (results == 0) {
			ChargeRecordHandler.editInCoinsRecord(codeByIpaddr);
//			chargeRecordService.editInCoinsRecord(codeByIpaddr);
		}
		byte[] datas = new byte[100];
		buffer.get(datas, 0, len);
		byte checkoutSum = checkoutSum(datas);
		if (sum == checkoutSum) {
			logger.info("wolflog---" + "数据发送验证成功");
		} else {
			logger.info("wolflog---" + "数据发送验证失败");
		}
		return allMap;
	}

	public void parse_0x84(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Equipment equipment = getCodeHardverByIpaddr(channel);
		if (equipment != null) {
			String code = equipment.getCode();
			byte coinsSum = buffer.get();
			byte money = buffer.get();
			byte sum = buffer.get();
			logger.info("wolflog---" + code + "号机" + coinsSum + "通道投币 " + money + "元已接收");
			String hardversion = equipment.getHardversion();
			if ("03".equals(hardversion)) {
				Integer merid = Equipmenthandler.findUserEquipment(code);
//				Integer merid = equipmentService.findUserEquipment(code);
				if (merid != null) {
					ChargeRecordHandler.addInCoinsRecord(merid, code,coinsSum,(byte) 1);
//					chargeRecordService.addInCoinsRecord(merid, code, coinsSum, (byte) 1);
					int aid = Equipmenthandler.getEquipmentByCode(code);
//					Integer aid = equipmentService.getEquipmentByCode(code);
					if (aid != -1) {
						Equipmenthandler.updateAreaCoinEarn(aid, 1);
//						equipmentService.updateAreaCoinEarn(aid, 1);
					}
					Equipmenthandler.updateEquCoinEarn(code, 1);
//					equipmentService.updateEquCoinEarn(code, 1);
					Equipmenthandler.updateMerAmountCoinEarn(merid, 1);
//					equipmentService.updateMerAmountCoinEarn(merid, 1);
				}
			} else {
				logger.warn(code + "号设备发送不符投币信息，此为脉冲专属");
			}
			buffer.position(1);
			byte[] datas = new byte[100];
			buffer.get(datas, 0, len);
			byte checkoutSum = checkoutSum(datas);
			if (sum == checkoutSum) {
				logger.info("wolflog---" + "数据发送验证成功");
			} else {
				logger.info("wolflog---" + "数据发送验证失败");
			}
			send_0x84(code);
		}
	}
	
	public static void parse_0x28(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		String codeByIpaddr = getCodeByIpaddr(channel);
		logger.info("wolflog---" + codeByIpaddr + "号设备发送地址信息已接收");
		String loStr = "";
		String laStr = "";
		for (int i = 0; i < 11; i++) {
			byte b = buffer.get();
			if (b == 0x2e) {
				laStr += ".";
			} else {
				laStr += (b - 0x30) + "";
			}
		}
		for (int i = 0; i < 11; i++) {
			byte b = buffer.get();
			if (b == 0x2e) {
				loStr += ".";
			} else {
				loStr += (b - 0x30) + "";
			}
		}
		logger.info("wolflog---" + "经度===" + loStr);
		logger.info("wolflog---" + "纬度===" + laStr);
		BigDecimal lon = new BigDecimal(loStr);
		BigDecimal lat = new BigDecimal(laStr);
		Equipmenthandler.updateEquAddr(codeByIpaddr, lon, lat);
		byte sum = buffer.get();
		buffer.position(1);
		byte[] datas = new byte[100];
		buffer.get(datas, 0, len);
		byte checkoutSum = checkoutSum(datas);
		if (sum == checkoutSum) {
			logger.info("wolflog---" + "数据发送验证成功");
			send_0x28(codeByIpaddr);
		} else {
			logger.info("wolflog---" + "数据发送验证失败");
		}
	}
	
	public static void parse_0x29(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		String codeByIpaddr = getCodeByIpaddr(channel);
		logger.info("wolflog---" + codeByIpaddr + "号设备回复地址信息已接收");
		String loStr = "";
		String laStr = "";
		for (int i = 0; i < 11; i++) {
			byte b = buffer.get();
			if (b == 0x2e) {
				laStr += ".";
			} else {
				laStr += (b - 0x30) + "";
			}
		}
		for (int i = 0; i < 11; i++) {
			byte b = buffer.get();
			if (b == 0x2e) {
				loStr += ".";
			} else {
				loStr += (b - 0x30) + "";
			}
		}
		logger.info("wolflog---" + "经度===" + loStr);
		logger.info("wolflog---" + "纬度===" + laStr);
		BigDecimal lon = new BigDecimal(loStr);
		BigDecimal lat = new BigDecimal(laStr);
		Equipmenthandler.updateEquAddr(codeByIpaddr, lon, lat);
		byte sum = buffer.get();
		buffer.position(1);
		byte[] datas = new byte[100];
		buffer.get(datas, 0, len);
		byte checkoutSum = checkoutSum(datas);
		if (sum == checkoutSum) {
			logger.info("wolflog---" + "数据发送验证成功");
			send_0x28(codeByIpaddr);
		} else {
			logger.info("wolflog---" + "数据发送验证失败");
		}
	}

	public String parse_0x85(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer) {
		Map<String, Map> allMap = new HashMap<>();
		Map<String, List<String>> map = new HashMap<>();
		String code = disposeImei(channel, len, cmd, result, buffer, allMap, map);
		return code;
	}

	public String disposeImei(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer, Map<String, Map> allMap, Map<String, List<String>> map) {
		try {
			List<String> list = new ArrayList<>();
			String ccid = "";
			for (int i = 0; i < 20; i++) {
				byte b = buffer.get();
				ccid += (char) b;
			}
			list.add(ccid);
			String imei = "";
			for (int i = 0; i < 15; i++) {
				byte b = buffer.get();
				if (b >= 48) {
					imei += b - 48;
				}
			}
			list.add(imei);
			byte hardversion1 = (byte) (buffer.get() - 0x30);
			byte hardversion2 = (byte) (buffer.get() - 0x30);
			byte hardversionnum1 = (byte) (buffer.get() - 0x30);
			byte hardversionnum2 = (byte) (buffer.get() - 0x30);
			byte softversionnum1 = (byte) (buffer.get() - 0x30);
			byte softversionnum2 = (byte) (buffer.get() - 0x30);
			byte csq = buffer.get();
			String hardversion = "" + hardversion1 + hardversion2;
			String hardversionnum = "" + hardversionnum1 + hardversionnum2;
			String softversionnum = "" + softversionnum1 + softversionnum2;
			list.add(hardversion);
			list.add(hardversionnum);
			list.add(softversionnum);
			if (checkIfAllNumber(imei) == false || checkIfAllNumber(ccid) == false) {
				return null;
			}
			list.add("" + csq);
			byte sum = buffer.get();
			String csq1 = "" + csq;
			logger.info("wolflog---" + "上传模块信息（硬件，软件，IMEI, +CCID）信息已接收");
			buffer.position(1);
//			byte[] datas = new byte[100];
//			buffer.get(datas, 0, len);
//			byte checkoutSum = checkoutSum(datas);
//			if (sum == checkoutSum) {
//				logger.info("wolflog---" + "数据发送验证成功");
//			} else {
//				logger.info("wolflog---" + "数据发送验证失败");
//			}
			Equipment equipment = Equipmenthandler.getEquipment(imei);
			if (equipment != null) {
				String code = equipment.getCode();
				list.add(code);
				map.put("param1", list);
				Equipmenthandler.addCodeOperateLog(code, 1, 1, Equipmenthandler.findUserEquipment(code), 0);
				try {
					String ipaddr = channel.getRemoteAddress().toString();
					Equipmenthandler.updateEquipment(code, ipaddr.substring(1, ipaddr.length()), ccid, csq1, (byte) 1, softversionnum, hardversionnum);
					String hardversion3 = equipment.getHardversion();
					if (!"03".equals(hardversion3)) {
						String mainid = equipment.getMainid();
						if (mainid == null || "".equals(mainid)) {
							Timer timer = new Timer();
							timer.schedule(new TimerTask() {
								@Override
								public void run() {
									send_0x23(code);
								}
							}, 20000);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				Server.codeAndIPMap.put(code, channel.getRemoteAddress().toString());
				Server.codeAndIPMap.put(code + "hardver", equipment.getHardversion());
				Equipmenthandler.redisDeciceStateAddOrRemove(1, code, equipment.getHardversion());
				return code;
			}
			String code = "";
			String info = Equipmenthandler.queryEndInfo();
			if (info == null || "".equals(info)) {
				code = "000001";
			} else {
				code = createNewCode(info);
			}
			list.add(code);
			String ipaddr;
			try {
				ipaddr = channel.getRemoteAddress().toString();
				Equipmenthandler.addEquipment(code, ccid, imei, csq1, ipaddr.substring(1, ipaddr.length()), hardversion,
						hardversionnum, softversionnum);
				Equipmenthandler.addCodeOperateLog(code, 1, 1, 0, 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Map<String, String> codeMap = new HashMap<>();
			codeMap.put("code", code);
			allMap.put(code, map);
			allMap.put("code", codeMap);
			Server.setInfoMap(allMap);
			return code;
		} catch (Exception e) {
			logger.info("wolflog---" + "第一次连接上传数据错误:");
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static String createNewCode(String info) {
		int parseInt = Integer.parseInt(info);
		String code = "";
		if ((parseInt + 1) < 10) {
			code = "00000" + (parseInt + 1);
		} else if ((parseInt + 1) >= 10 && (parseInt + 1) < 100) {
			code = "0000" + (parseInt + 1);
		} else if ((parseInt + 1) >= 100 && (parseInt + 1) < 1000) {
			code = "000" + (parseInt + 1);
		} else if ((parseInt + 1) >= 1000 && (parseInt + 1) < 10000) {
			code = "00" + (parseInt + 1);
		} else if ((parseInt + 1) >= 10000 && (parseInt + 1) < 100000) {
			code = "0" + (parseInt + 1);
		} else if ((parseInt + 1) >= 100000 && (parseInt + 1) < 1000000) {
			code = "" + (parseInt + 1);
		}
		return code;
	}

	/**
	 * get equipmentnum by ipaddr
	 * 
	 * @param channel
	 * @return equipmentnum
	 */
	public static String getCodeByIpaddr(AsynchronousSocketChannel channel) {
		Map<String, String> codeAndIPMap = Server.codeAndIPMap;
		Set<Entry<String,String>> entrySet = codeAndIPMap.entrySet();
		String ipaddr = "";
		String code = null;
		try {
			ipaddr = channel.getRemoteAddress().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!codeAndIPMap.isEmpty()) {
			for (Entry<String, String> entry : entrySet) {
				if (ipaddr.equals(entry.getValue())) {
					code = entry.getKey();
				}
			}
		}
		return code;
	}
	
	/**
	 * get CodeHardver by ipaddr
	 * 
	 * @param channel
	 * @return equipmentnum
	 */
	public static Equipment getCodeHardverByIpaddr(AsynchronousSocketChannel channel) {
		Map<String, String> codeAndIPMap = Server.codeAndIPMap;
		Set<Entry<String,String>> entrySet = codeAndIPMap.entrySet();
		String ipaddr = "";
		Equipment equipment = null;
		try {
			ipaddr = channel.getRemoteAddress().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!codeAndIPMap.isEmpty()) {
			for (Entry<String, String> entry : entrySet) {
				if (ipaddr.equals(entry.getValue())) {
					equipment = new Equipment();
					equipment.setCode(entry.getKey());
					equipment.setHardversion(Server.codeAndIPMap.get(equipment.getCode() + "hardver"));
				}
			}
		}
		return equipment;
	}
	
	/**
	 * get equipmentnum by ipaddr
	 * 
	 * @param channel
	 * @return equipmentnum
	 */
	public static Equipment getEquipmentByIpaddr(AsynchronousSocketChannel channel) {
		String ipaddr = "";
		try {
			ipaddr = channel.getRemoteAddress().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Equipment equipment = Equipmenthandler.queryEquipmentByIpaddr(ipaddr.substring(1, ipaddr.length()));
		return equipment;
	}

	/**
	 * check all port status and dispose
	 * 
	 * @param code
	 * @param port
	 * @param portStatus
	 * @param time
	 * @param power
	 * @param elec
	 */
	public boolean checkAndDispose(String code, byte port, Byte portStatus, short time, short power, short elec, double port_v, double port_a) {
//		if (port >= 1 && port <= 20 && time >= 0 && power >= 0 && elec >= 0) {
//			if (code != null && !"".equals(code)) {
//				String equipmentnum = AllPortStatusHandler.getAllPortStatusByEquipmentnum(code, port);
//				if (!"wolferror".equals(equipmentnum)) {
//					if (equipmentnum == null || "".equals(equipmentnum)) {
//						AllPortStatusHandler.addAllPortStatus(code, port, portStatus, time, power, elec, port_v, port_a);
//						allPortStatusHandler.addAllPortStatus1(code, port, portStatus, time, power, elec, port_v, port_a);
//					} else {
//						AllPortStatusHandler.updateAllPortStatus(equipmentnum, port, portStatus, time, power, elec, port_v, port_a);
//						allPortStatusHandler.updateAllPortStatus1(code, port, portStatus, time, power, elec, port_v, port_a);
//					}
//				} else {
//					logger.warn(code + "设备查询端口" + port + "查询异常");
//				}
//			}
			return true;
//		} else {
//			logger.warn("code==" + code + "-port==" + port + "-portStatus==" + portStatus + "-time=="
//		+ time + "-power==" + power +"-elec==" + elec);
//			return false;
//		}
	}
	
	/**
	 * 根据退费标准来进行退费
	 * @param power
	 * @param quantity
	 * @param elec
	 * @param allTime
	 * @param surpTime
	 * @param refundMoney
	 * @param allMoney
	 * @param norm
	 * @param begintime 
	 */
	public static double clacRefund(String code,Short power,Double quantity,Double elec,Double allTime,Double surpTime
			,Double allMoney, int norm, long begintime, long endtime) {
		logger.info("功率==" + power + "--总电量==" + quantity + "--剩余电量===" + elec
				+ "--总时间===" + allTime + "--剩余时间===" + surpTime + "--总金额" + allMoney);
		int powerMax1 = 200;
		int powerMax2 = 400;
		int powerMax3 = 600;
		double powerTime2 = 0.75;
		double powerTime3 = 0.50;
		double powerTime4 = 0.25;
		if (norm == 1) {
			logger.info("执行时间和电量最小");
			double l = (endtime - begintime + 0.0) / 1000 / 60;
			double clactime = l + surpTime;
			logger.info("l=" + l + "--clactime=" + clactime);
			if (allTime <= 999) {
				allTime = clactime;
			} else {
				CodeSystemParam codeSysParam = Equipmenthandler.selectCodeSysParam(code);
				if (codeSysParam != null) {
					powerMax1 = codeSysParam.getPowerMax1();
					powerMax2 = codeSysParam.getPowerMax2();
					powerMax3 = codeSysParam.getPowerMax3();
					powerTime2 = (codeSysParam.getPowerTim2() + 0.0) / 100;
					powerTime3 = (codeSysParam.getPowerTim3() + 0.0) / 100;
					powerTime4 = (codeSysParam.getPowerTim4() + 0.0) / 100;
				}
				if (power > powerMax3) {
					allTime = allTime * powerTime4;
				} else if (power > powerMax2 && power <= powerMax3) {
					allTime = allTime * powerTime3;
				} else if (power > powerMax1 && power <= powerMax2) {
					allTime = allTime * powerTime2;
				}
				if (clactime <= 990 && allTime > 999) {
					allTime = allTime * clactime / 999;
					surpTime = allTime - l;
				} else {
					surpTime = allTime - l;
				}
				logger.info("wolflog---" + "allTime===" + allTime + "surpTime===" + surpTime);
			}
			double refundMoney = surpTime * allMoney / allTime;
			logger.info("退款金额" + (refundMoney));
			double refundElecMoney = elec / quantity * allMoney;
			if (refundMoney > refundElecMoney && (refundMoney > 0 && refundElecMoney > 0)) {
				refundMoney = refundElecMoney;
			}
			if (refundMoney < 0) {
				refundMoney = 0;
			}
			return refundMoney;
		} else if (norm == 2) {
			logger.info("执行时间最小");
			long currentTimeMillis = System.currentTimeMillis();
			double l = (currentTimeMillis - begintime + 0.0) / 1000 / 60;
			double clactime = l + surpTime;
			if (allTime <= 999) {
				allTime = clactime;
			} else {
				CodeSystemParam codeSysParam = Equipmenthandler.selectCodeSysParam(code);
				if (codeSysParam != null) {
					powerMax1 = codeSysParam.getPowerMax1();
					powerMax2 = codeSysParam.getPowerMax2();
					powerMax3 = codeSysParam.getPowerMax3();
					powerTime2 = (codeSysParam.getPowerTim2() + 0.0) / 100;
					powerTime3 = (codeSysParam.getPowerTim3() + 0.0) / 100;
					powerTime4 = (codeSysParam.getPowerTim4() + 0.0) / 100;
				}
				if (power > powerMax3) {
					allTime = allTime * powerTime4;
				} else if (power > powerMax2 && power <= powerMax3) {
					allTime = allTime * powerTime3;
				} else if (power > powerMax1 && power <= powerMax2) {
					allTime = allTime * powerTime2;
				}
				if (clactime <= 990 && allTime > 999) {
					allTime = allTime * clactime / 999;
					surpTime = allTime - l;
				} else {
					surpTime = allTime - l;
				}
			}
			logger.info("wolflog---" + "计算总时间===" + allTime
					 + "---使用时间===" + l
					 + "---计算剩余时间===" + surpTime
					 + "---退款金额" + (surpTime * allMoney / allTime)
					);
			double refundMoney = surpTime * allMoney / allTime;
			if (refundMoney < 0) {
				refundMoney = 0;
			}
			return refundMoney;
		} else if (norm == 3) {
			logger.info("执行电量最小:" + "退款金额" + (elec / quantity * allMoney));
			return (elec / quantity * allMoney);
		} else {
			return surpTime * allMoney / allTime;
		}
	}
	
	public static int getRedisPortstatus(String code,String port) {
		Map<String, String> codeMap = JedisUtils.hgetAll(code);
		Map<String,String> parse = (Map<String, String>) JSON.parse(codeMap.get(port));
		String portStatus = parse.get("portStatus");
		return Integer.parseInt(portStatus);
	}
	
	public static int getIfcontinue(String code,int port,short time) {
		ChargeRecord chargeRecord = ChargeRecordHandler.getEndChargeRecordUnfinish(code, port);
		int ifcontinue = 0;
		try {
			long nowtime = System.currentTimeMillis();
			long begintime = chargeRecord.getBegintime().getTime();
			if (chargeRecord != null) {
				if (nowtime - begintime > 0 && (nowtime - begintime) < time * 60 * 1000) {
					Integer id = chargeRecord.getId();
					ifcontinue = id;
				}
			} 
		} catch (Exception e) {
			logger.warn("判断是否已有充电记录");
		}
		return ifcontinue;
	}
	
	public static double byTimeTempSubFee(String code,int uid,int power,int port, int flag) {
		String uidSubFee = JedisUtils.get("uid" + code + port + uid);
		Map<String,String> parse = (Map<String, String>) JSON.parse(uidSubFee);
		if (!DisposeUtil.checkMapIfHasValue(parse)) {
			return 0.0;
		}
		String powerItStr = parse.get("power");
		int powerIt = Integer.parseInt(powerItStr);
		String totalfeeStr = parse.get("totalfee");
		double totalfee = Double.parseDouble(totalfeeStr);
		double subFee = 0.0;
		long nowtime = System.currentTimeMillis();
		String wolfnumStr = parse.get("wolfnum");
		int wolfnum = Integer.parseInt(wolfnumStr);
		logger.info("wolflog---" + "power===" + power + "--redis详情===" + JSON.toJSONString(parse));
		if (power > 0 && powerIt > 0 && (power / powerIt) >= 3) {
			parse.put("wolfnum", (wolfnum + 1) + "");
		} else {
			if (powerIt > power) {
				power = powerIt;
			}
			String timeStr = parse.get("time");
			List<TemplateSon> v3TempsonList = Equipmenthandler.getV3TempsonList(code);
			double oneHour = 0.0;
			for (TemplateSon templateSon : v3TempsonList) {
				int powerMin = Integer.parseInt(templateSon.getCommon1());
				int powerMax = Integer.parseInt(templateSon.getCommon2());
				if ((power >= powerMin) && power <= powerMax) {
					oneHour = templateSon.getMoney();
					break;
				} else {
					oneHour = templateSon.getMoney();
				}
			}
			long intervalTime = 5;
			try {
				long updateTime = Long.parseLong(timeStr);
				intervalTime = Math.round(((nowtime - updateTime)/1000 + 0.0)/60);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (intervalTime >= 10) {
				subFee = (Math.round(oneHour*intervalTime/60*100) + 0.0)/100;
			}
			double clacMoney = CommUtil.subBig(subFee, totalfee);
			String moneyStr = parse.get("money");
			double money = Double.parseDouble(moneyStr);
			if (flag == 0) {
				if (CommUtil.addBig(clacMoney, subFee) > money) {
					send_13(Byte.parseByte(port + ""), code);
				}
			}
			parse.put("power", power + "");
			parse.put("totalfee", subFee + "");
			logger.info("wolflog---" + "uid" + code + port + uid + ":totalfee===" + subFee);
//			parse.put("time", nowtime + "");
		}
		JedisUtils.set("uid" + code + port + uid, JSON.toJSONString(parse), 0);
		return subFee;
	}
	
	public static void resetChargeData(String code, int port, int uid, double money, int state) {
		String uidSubFee = JedisUtils.get("uid" + code + port + uid);
		Map<String,String> parse = (Map<String, String>) JSON.parse(uidSubFee);
		if (state == 1) {
			String moneyStr = parse.get("money");
			double moneyParse = Double.parseDouble(moneyStr);
			if (moneyParse != 0) {
				parse.put("money", CommUtil.addBig(money, moneyParse) + "");
			}
		} else if (state == 0) {
			if (!DisposeUtil.checkMapIfHasValue(parse)) {
				parse = new HashMap<>();
			}
			parse.put("power", "0");
			parse.put("totalfee", "0.0");
			parse.put("time", System.currentTimeMillis() + "");
			parse.put("wolfnum", "0");
			parse.put("money", money + "");
		}
		JedisUtils.set("uid" + code + port + uid, JSON.toJSONString(parse), 0);
	}
	
	public static boolean checkIfAllNumber(String str) {
		String reg = "\\d+";
		return str.matches(reg);
	}

	public static String getSetcoin() {
		return setcoin;
	}

	public static void setSetcoin(String setcoin) {
		SendMsgUtil.setcoin = setcoin;
	}

	public static String getSetic() {
		return setic;
	}

	public static void setSetic(String setic) {
		SendMsgUtil.setic = setic;
	}

	public static String getOnelock() {
		return onelock;
	}

	public static void setOnelock(String onelock) {
		SendMsgUtil.onelock = onelock;
	}

	public static String getTwolock() {
		return twolock;
	}

	public static void setTwolock(String twolock) {
		SendMsgUtil.twolock = twolock;
	}

	public static byte getClickport() {
		return clickport;
	}

	public static void setClickport(byte clickport) {
		SendMsgUtil.clickport = clickport;
	}

	public static byte getClickpayport() {
		return clickpayport;
	}

	public static void setClickpayport(byte clickpayport) {
		SendMsgUtil.clickpayport = clickpayport;
	}
	
	public static String getServicePhone(Integer tempid, Integer areaid, Integer dealid) {
		try {
			tempid = CommUtil.toInteger(tempid);
			areaid = CommUtil.toInteger(areaid);
			dealid = CommUtil.toInteger(dealid);
			String phone = null;
			if(!tempid.equals(0)){
				TemplateParent temppare = Equipmenthandler.getEquipmentTempData(tempid);
				phone = CommUtil.toString(temppare.getCommon1());
			}
			if(phone==null && !areaid.equals(0)){
				List<Map<String, Object>> result = AreaHandler.getPartnerInfo(areaid, 3);
				for(Map<String, Object> item : result){
					Integer type = CommUtil.toInteger(item.get("type"));
					if(type.equals(3)){
						Integer manageid =  CommUtil.toInteger(item.get("partid"));
						User manageuser = UserHandler.getUserInfo(manageid);
						phone = CommUtil.toString(manageuser.getPhoneNum());
					}
				}
			}
			if(phone==null && !dealid.equals(0)){
				User dealer = UserHandler.getUserInfo(dealid);
				phone = CommUtil.toString(dealer.getServephone());
				if(phone==null) phone = CommUtil.toString(dealer.getPhoneNum());
			}
			return phone;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void operRedisAllPortStatus(String code, int port, int time, int elec, String status) {
		try {
			Map<String, String> codeMap = JedisUtils.hgetAll(code);
			logger.info("wolflog---" + JSON.toJSONString(codeMap));
			if (codeMap != null && codeMap.size() > 0) {
				Map<String, String> parse = (Map<String, String>) JSON.parse(codeMap.get(port + ""));
				logger.info("wolflog---" + JSON.toJSONString(parse));
				if (parse != null && parse.size() > 0) {
					parse.put("portStatus", status);
					parse.put("time", time + "");
					parse.put("elec", elec + "");
				}
				codeMap.put(port + "", JSON.toJSONString(parse));
			}
			JedisUtils.addEquipmentCache(code, codeMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 日志结束充电
	 * @param chargeRecord
	 * @param reason
	 * @param code
	 * @param port
	 */
	public static void logAllRefund(ChargeRecord chargeRecord, int reason, String code, int port) {
		Integer endChargeId = chargeRecord.getId();
		Integer uid = chargeRecord.getUid();
		Double money = chargeRecord.getExpenditure();
		Integer merchantid = chargeRecord.getMerchantid();
		User touser = UserHandler.getUserInfo(uid);
		touser = touser == null ? new User() : touser;
		Double userBalance = CommUtil.toDouble(touser.getBalance());
		Double usersendmoney = CommUtil.toDouble(touser.getSendmoney());
		Integer paytype = chargeRecord.getPaytype();
		if (paytype == 1) {//钱包
			ChargeRecordHandler.updateChargeRefundNumer(1, endChargeId,
					chargeRecord.getExpenditure(), 0.0, 0, 0);
			logger.info("wolflog---" + "钱包支付充电失败，已退款");
			//============================================================================
			Double topupmoney = CommUtil.toDouble(userBalance);//充值金额
			Double sendmoney = CommUtil.toDouble(usersendmoney);//赠送金额
			Double accountmoney = CommUtil.addBig(userBalance, usersendmoney);//账户金额
			Double opermoney = CommUtil.toDouble(chargeRecord.getExpenditure());//操作总额
			Double opertopupmoney = 0.00;	//操作充值金额
			Double opersendmoney = 0.00;	//操作赠送金额
			Double topupbalance = 0.00;//充值余额
			Double sendbalance = 0.00;//赠送余额
			if(topupmoney>0) {
				opertopupmoney = opermoney;
				topupbalance = CommUtil.addBig(topupmoney, opertopupmoney);
				opersendmoney = 0.00;
				sendbalance = sendmoney;
			}else{
				opertopupmoney = 0.00;
				topupbalance = 0.00;
				opersendmoney = opermoney;
				sendbalance = CommUtil.addBig(sendmoney, opersendmoney);
			}
			Double accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额

			ChargeRecordHandler.updateUserMoney(opertopupmoney, uid, opersendmoney, 5);
			
			Money moneyEntity = new Money();
			moneyEntity.setUid(uid);
			moneyEntity.setOrdernum(chargeRecord.getOrdernum());
			moneyEntity.setPaytype(3);
			moneyEntity.setStatus(1);

			moneyEntity.setMoney(opertopupmoney);
			moneyEntity.setSendmoney(opersendmoney);
			moneyEntity.setTomoney(opermoney);
			moneyEntity.setBalance(accountbalance);
			moneyEntity.setTopupbalance(topupbalance);
			moneyEntity.setGivebalance(sendbalance);
			
			moneyEntity.setRemark("wallet");
			ChargeRecordHandler.addMoneyRecord(moneyEntity);
			UserHandler.addGenDetail(chargeRecord.getUid(), merchantid, chargeRecord.getOrdernum(),
					opertopupmoney, opersendmoney, opermoney, accountbalance, topupbalance, sendbalance, 5, null);
			TradeRecordHandler.addTradeRecord(chargeRecord.getMerchantid(), 0,
					chargeRecord.getUid(), chargeRecord.getOrdernum(),
					chargeRecord.getExpenditure(), 0, 0, code, 1, 1, 2);
			uid = CommUtil.toInteger(uid);
			Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
			Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
			Double refundMoney = paymoney;//全额退款退款金额等于付款金额
			Double consumemoney =  CommUtil.subBig(paymoney, refundMoney);//消费金额
			Integer orderid = CommUtil.toInteger(chargeRecord.getId());
			String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
			String deviceport = CommUtil.toString(port);
			Integer resultinfo = CommUtil.toInteger(reason);
			String startTime = CommUtil.toDateTime(chargeRecord.getBegintime());
//			String endTime = CommUtil.toDateTime(chargeRecord.getEndtime());
			String endTime = CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date());
			String chargetime = "开始时间："+startTime + "\r\n结束时间：" + endTime;
			String devicenum = code;
			Equipment devicedata = Equipmenthandler.getEquipmentData(devicenum);
			Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
			Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
			String devicename = CommUtil.toString(devicedata.getRemark());
			String areaname = CommUtil.toString(devicedata.getName());
			String servicphone = getServicePhone(devicetempid, deviceaid, merid);
			
			String urltem = "";
			User user = UserHandler.getUserInfo(uid);
			String oppenid = user.getOpenid();
			String returnfirst = "您好，支付充电失败，金额自动原路退款到账户。";
			String servername = "钱包全额退款";
			TempMsgUtil.returnMsgTemp(returnfirst, oppenid, servername, ordernum, endTime, paymoney, servicphone, CommonConfig.PAYRETURNMES, urltem);
			
			TempMsgUtil.endChargeSendMess("尊敬的用户，您的充电已经结束。", ordernum, orderid, resultinfo, merid, uid, consumemoney, chargetime, 
				devicenum, deviceport, devicename, deviceaid, areaname, servicphone);
			
		} else if (paytype == 2) {// 微信
			WXPayConfigImpl config;
			try {
				config = WXPayConfigImpl.getInstance();
				WXPay wxpay = new WXPay(config);
				String total_fee = "";
				String out_trade_no = "";
				String moneyStr = String.valueOf(chargeRecord.getExpenditure() * 100);
				int idx = moneyStr.lastIndexOf(".");
				total_fee = moneyStr.substring(0, idx);
				out_trade_no = chargeRecord.getOrdernum();// 退款订单号
				// 查询配置信息
				String subMer = UserHandler.selectSubMerConfig(chargeRecord.getMerchantid());
				String subMchId = null;
				SortedMap<String, String> params = new TreeMap<>();
				// subMer为空表示为普通商户
				if(subMer != null && !"".equals(subMer)){
					subMchId = subMer;
					logger.info("使用微信特约商户号:"+subMchId+"进行退款");
				}else{
					subMchId = WeiXinConfigParam.SUBMCHID;
					logger.info("使用服务平台商户号:"+subMchId+"进行退款");
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
							TradeRecord tradere = TradeRecordHandler.getTradereInfo(out_trade_no);
							String comment = CommUtil.toString(tradere.getComment());
							Integer manid = tradere.getManid();
							int aid = Equipmenthandler.getEquipmentByCode(code);
							if (aid != 0) {
								UserHandler.areaRefund(aid, money);
							}
							UserHandler.equRefund(code, money);
							// 微信特约商户只记录资金统计信息
							if(subMer != null && !"".equals(subMer)){
								//查询特约商户余额
								Double merEarnings = CommUtil.toDouble(UserHandler.getUserEarningsById(chargeRecord.getMerchantid()));
								UserHandler.merAmountRefund(chargeRecord.getMerchantid(), money);
								//添加特约商户余额明细
								UserHandler.addMerDetail(chargeRecord.getMerchantid(), out_trade_no, money, merEarnings, MerchantDetail.CHARGESOURCE, paytype,  MerchantDetail.REFUND);
							}else{
								//TODO 充电退费
								dealerIncomeRefund(comment, chargeRecord.getMerchantid(), money, out_trade_no, 
										new Date(), MerchantDetail.CHARGESOURCE, paytype, MerchantDetail.REFUND);
							}
							//TODO 添加交易记录
							TradeRecordHandler.addTradeRecordInfo(chargeRecord.getMerchantid(),
									manid, chargeRecord.getUid(), chargeRecord.getOrdernum(),
									chargeRecord.getExpenditure(), tradere.getMermoney(),
									tradere.getManmoney(), code, 1, 2, 2,tradere.getHardver(),comment);
							ChargeRecordHandler.updateChargeRefundNumer(1, endChargeId,
									chargeRecord.getExpenditure(), chargeRecord.getExpenditure(), 0, 0);
							uid = CommUtil.toInteger(uid);
							Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
							Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
							Double refundMoney = paymoney;//全额退款退款金额等于付款金额
							Double consumemoney =  CommUtil.subBig(paymoney, refundMoney);//消费金额
							Integer orderid = CommUtil.toInteger(chargeRecord.getId());
							String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
							String deviceport = CommUtil.toString(port);
							Integer resultinfo = CommUtil.toInteger(reason);
							String startTime = CommUtil.toDateTime(chargeRecord.getBegintime());
							String endTime = CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date());
							String chargetime = "开始时间："+startTime + "\r\n结束时间：" + endTime;
							
							String devicenum = code;
							Equipment devicedata = Equipmenthandler.getEquipmentData(devicenum);
							Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
							Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
							String devicename = CommUtil.toString(devicedata.getRemark());
							String areaname = CommUtil.toString(devicedata.getName());
							String servicphone = getServicePhone(devicetempid, deviceaid, merid);
							
							String urltem = "";
							User user = UserHandler.getUserInfo(uid);
							String oppenid = user.getOpenid();
							String returnfirst = "您好，支付充电失败，金额自动原路退款到账户。";
							String servername = "微信全额退款";
							TempMsgUtil.returnMsgTemp(returnfirst, oppenid, servername, ordernum, endTime, paymoney, servicphone, CommonConfig.PAYRETURNMES, urltem);
							
							TempMsgUtil.endChargeSendMess("尊敬的用户，您的充电已经结束。", ordernum, orderid, resultinfo, merid, uid, consumemoney, chargetime, 
								devicenum, deviceport, devicename, deviceaid, areaname, servicphone);
							
//							TempMsgUtil.finishchargesendmsg("您好，充电已完成 ，已退款", chargeRecord.getUid(),
//									chargeRecord.getMerchantid(), code, chargeRecord.getBegintime(),
//									new Date(), endChargeId);
						}
						if ("FAIL".equals(r.get("result_code"))) {
							// ChargeRecordHandler.updateChargeRefundNumer(endChargeId);
						}
					} catch (Exception e) {
						logger.error(chargeRecord.getOrdernum() + e.getMessage() + "微信退款失败");
					}
				}
			} catch (Exception e) {
				logger.error(chargeRecord.getOrdernum() + e.getMessage() + "微信退款失败");
			}
			// 结束充电微信发送通知
			logger.info("wolflog---" + "微信支付充电失败，已退款");
		} else if (paytype == 3 || paytype == 8) {
			AlipayClient alipayClient = new DefaultAlipayClient(
					"https://openapi.alipay.com/gateway.do", AlipayConfig.APPID,
					AlipayConfig.RSA_PRIVATE_KEY, "json", "GBK", AlipayConfig.ALIPAY_PUBLIC_KEY,
					"RSA2");
			if (paytype == 8) {
				alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPLETAPPID,
						AlipayConfig.APPLETRSA_PRIVATE_KEY, "json", "GBK", AlipayConfig.APPLETALIPAY_PUBLIC_KEY, "RSA2");
				paytype = 3;
			}
			AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
			request.setBizContent("{" + "\"out_trade_no\":\"" + chargeRecord.getOrdernum() + "\","
					+ "\"refund_amount\":" + chargeRecord.getExpenditure() + "  }");

			AlipayTradeRefundResponse response;
			try {
				response = alipayClient.execute(request);
				if (response.isSuccess()) {//TODO  origin RZC  支付宝充电异常 自动退费处理
					logger.error("支付宝支付充电成功，已退款");
					TradeRecord tradere = TradeRecordHandler.getTradereInfo(chargeRecord.getOrdernum());
					String comment = CommUtil.toString(tradere.getComment());
					Integer manid = tradere.getManid();
					int aid = Equipmenthandler.getEquipmentByCode(code);
					if (aid != 0) {
						UserHandler.areaRefund(aid, money);
					}
					UserHandler.equRefund(code, money);
					//TODO 充电退费
					dealerIncomeRefund(comment, chargeRecord.getMerchantid(), money, chargeRecord.getOrdernum(), 
							new Date(), MerchantDetail.CHARGESOURCE, 3, MerchantDetail.REFUND);
					logger.error("支付宝支付充电成功，已退款");
					//TODO 添加交易记录
					TradeRecordHandler.addTradeRecordInfo(chargeRecord.getMerchantid(),
							0, chargeRecord.getUid(), chargeRecord.getOrdernum(),
							chargeRecord.getExpenditure(), tradere.getMermoney(),
							tradere.getManmoney(), code, 1, 3, 2,tradere.getHardver(),comment);
					ChargeRecordHandler.updateChargeRefundNumer(1, endChargeId,
							chargeRecord.getExpenditure(), chargeRecord.getExpenditure(), 0, 0);
				} else {
					logger.error(chargeRecord.getOrdernum() + "支付宝支付充电失败");
				}
			} catch (AlipayApiException e) {
				logger.error(chargeRecord.getOrdernum() + e.getMessage() + "支付宝支付充电失败");
			}
		} else {
			logger.warn("根据实用充电信息计算退款信息");
		}
	}
	
	public static int sumAllTime(int endChargeId) {
		List<ChargeRecord> chargeList = ChargeRecordHandler.getChargeListUnfinish(endChargeId);
		int allTime = 0;
		for (ChargeRecord charge : chargeList) {
			allTime += Integer.parseInt(charge.getDurationtime());
		}
		return allTime;
	}
	public static double sumAllMoney(int endChargeId) {
		List<ChargeRecord> chargeList = ChargeRecordHandler.getChargeListUnfinish(endChargeId);
		double allMoney = 0;
		for (ChargeRecord charge : chargeList) {
			allMoney += charge.getExpenditure();
		}
		return allMoney;
	}
	
	public static double getV3FeeMoney(String code,int port,int uid,long time) {
		String subFee = JedisUtils.get("uid" + code + port + uid);
		Map<String,String> parse = (Map<String, String>) JSON.parse(subFee);
		if (!DisposeUtil.checkMapIfHasValue(parse)) {
			return 0;
		}
		String totalfee = parse.get("totalfee");
		long nowtime = System.currentTimeMillis();
		long updatetime = Long.parseLong(parse.get("time")) + (time * 60 * 1000);
		double operallmoney = Double.parseDouble(totalfee);//总扣费金额
		if ((nowtime - updatetime) < (10 * 60 * 1000)) {
			String powerItStr = parse.get("power");
			int powerIt = Integer.parseInt(powerItStr);
			operallmoney = byTimeTempSubFee(code, uid, powerIt, port, 1);
		}
		return operallmoney;
	}
	
	public static void v3DisposeData(ChargeRecord chargeRecord,String code,int port,int endChargeId,int time
			,int reason) {
		String ordernum = chargeRecord.getOrdernum();
		boolean checkOrdernumBoolean = checkStringParamIfResubmit(ordernum);
		if (!checkOrdernumBoolean) {
			return;
		}
		Map<String, String> chargeInfoMap = JedisUtils.hgetAll(ordernum);
		Integer uid = chargeRecord.getUid();
		Integer paytype = chargeRecord.getPaytype();
		if (DisposeUtil.checkMapIfHasValue(chargeInfoMap)) {
			String type = chargeInfoMap.get("type");
			if ("1".equals(type) && paytype == 7) {
				String cardid = chargeInfoMap.get("cardid");
				onlinecardEndChargeDispose(cardid, "08", code, port, 0, chargeRecord, reason, time, 0);
				resetChargeData(code, port, uid, 0.0, 0);
				return;
			}
		}
		if (paytype != 1 && paytype != 2) {
			return;
		}
		Integer merchantid = chargeRecord.getMerchantid();
		User genuser = UserHandler.getUserInfo(uid);
//		Double userBalance = CommUtil.toDouble(genuser.getBalance());//用户余额
//		Double usersendmoney = CommUtil.toDouble(genuser.getSendmoney());//用户所拥有赠送金额
		int allTime = sumAllTime(endChargeId);
//		Double opersendmoney = 0.00;//赠送操作金额
		double operallmoney = getV3FeeMoney(code, port, uid, allTime - time);

		//============================================================================
	 	Double topupmoney = CommUtil.toDouble(genuser.getBalance());//充值金额
		Double sendmoney = CommUtil.toDouble(genuser.getSendmoney());//赠送金额
		Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额
		//============================================================================
		Double opermoney = CommUtil.toDouble(operallmoney);//操作总额
		Double opertopupmoney = 0.00;	//操作充值金额
		Double opersendmoney = 0.00;	//操作赠送金额
		//============================================================================
		Double topupbalance = 0.00;//充值余额
		Double sendbalance = 0.00;//赠送余额
		//============================================================================
		Double contrastmoney = CommUtil.subBig(topupmoney, opermoney); //充值、操作比较金额差
		if(contrastmoney>=0){//此时说明用户充值金额足够
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
		Double accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
//		double topupbalance = CommUtil.subBig(userBalance, operallmoney);//用户余额减去扣费金额剩余金额
//		double opermoney = operallmoney;
//		if (topupbalance < 0) {
//			if (usersendmoney > 0) {
//				if (usersendmoney + topupbalance > 0) {
//					usersendmoney = CommUtil.addBig(usersendmoney, topupbalance);
//					topupbalance = 0;
//					opermoney = 0 - topupbalance;
//					opersendmoney = CommUtil.subBig(operallmoney, opermoney);
//				}
//			}
//		}
//		double operbalance = topupbalance + usersendmoney;
		
		int useTime = allTime - time;
		logger.info("wolflog---" + "allTime===" + allTime + "---useTime===" + useTime);
		String firstdata = "尊敬的用户，您的充电已经结束。";
		resetChargeData(code, port, uid, 0.0, 0);
		if (chargeRecord.getExpenditure() == 0 && !"0".equals(chargeRecord.getDurationtime())) {
			ChargeRecordHandler.updateUserMoney(opertopupmoney, uid, opersendmoney, 6);
			UserHandler.addGenDetail(chargeRecord.getUid(), merchantid, chargeRecord.getOrdernum(),
					opertopupmoney, opersendmoney, opermoney, accountbalance, topupbalance, sendbalance, 2, null);
//			UserHandler.addGenDetail(chargeRecord.getUid(), merchantid, chargeRecord.getOrdernum(),
//					opermoney, opersendmoney, operallmoney, operbalance, topupbalance, usersendmoney, 2, null);
			ChargeRecordHandler.updateChargeResult(1, endChargeId, chargeRecord.getExpenditure(), operallmoney, reason + 0, 2, useTime, 0);
			chargeRecord.setExpenditure(opermoney);
			TradeRecordHandler.updateTrade(chargeRecord.getOrdernum(), opermoney);
		} else if (chargeRecord.getPaytype() == 1 || chargeRecord.getPaytype() == 2) {
			boolean checkIfRefund = checkIfRefund(uid, merchantid, code);
			List<ChargeRecord> chargeList = ChargeRecordHandler.getChargeListUnfinish(endChargeId);
			double allMoney = 0.0;
			for (ChargeRecord charge : chargeList) {
				allMoney += charge.getExpenditure();
			}
			if (operallmoney > allMoney) {
				operallmoney = allMoney;
			}
			long begintime = chargeRecord.getBegintime().getTime();
			long nowtime = System.currentTimeMillis();
			long clacTime = nowtime - begintime;
			if (checkIfRefund && clacTime <= 172800000) {
				double differ = CommUtil.subBig(allMoney, operallmoney);
				if (differ <= 0) {
					ChargeRecordHandler.updateChargeRecordStopCharging(endChargeId, reason + 0);
				} else {
					if (differ > 0)  firstdata = "尊敬的用户，您的充电已经结束，已退款(¥" + (differ + 0.00) + ")。 \r\n 注：该退款到平台钱包里。";
					double operUserbalance = CommUtil.addBig(topupmoney, differ);
					double operUserAllMoney = CommUtil.addBig(operUserbalance, sendmoney);
					ChargeRecordHandler.updateUserMoney(differ, uid, null, 2);
					
					Money moneyEntity = new Money();
					moneyEntity.setUid(uid);
					moneyEntity.setOrdernum(chargeRecord.getOrdernum());
					moneyEntity.setPaytype(3);
					moneyEntity.setStatus(1);
					
					moneyEntity.setMoney(differ);
					moneyEntity.setSendmoney(0.0);
					moneyEntity.setTomoney(differ);
					moneyEntity.setBalance(operUserAllMoney);
					moneyEntity.setTopupbalance(operUserbalance);
					moneyEntity.setGivebalance(sendmoney);
					
					moneyEntity.setRemark("wallet");
					ChargeRecordHandler.addMoneyRecord(moneyEntity);
					UserHandler.addGenDetail(chargeRecord.getUid(), merchantid, chargeRecord.getOrdernum(),
							differ, 0, differ, operUserAllMoney, operUserbalance, sendmoney, 5, null);
					for (ChargeRecord charge : chargeList) {
						Integer id = charge.getId();
						Double expenditure = charge.getExpenditure();
						if (differ > expenditure) {
							ChargeRecordHandler.updateChargeResult(2, id, expenditure, expenditure, reason, 1, useTime, 0);
							TradeRecordHandler.addTradeRecord(chargeRecord.getMerchantid(), 0,
									chargeRecord.getUid(), chargeRecord.getOrdernum(),
									expenditure, 0, 0, code, 1, 1, 2);
						} else if (differ > 0) {
							ChargeRecordHandler.updateChargeResult(2, id, expenditure, differ, reason, 1, useTime, 0);
							TradeRecordHandler.addTradeRecord(chargeRecord.getMerchantid(), 0,
									chargeRecord.getUid(), chargeRecord.getOrdernum(),
									differ, 0, 0, code, 1, 1, 2);
						}
						differ = CommUtil.subBig(differ, expenditure);
					}
				}
			} else {
				operallmoney = allMoney;
				ChargeRecordHandler.updateChargeRecordStopCharging(endChargeId, reason + 0);
			}
		} else {
			ChargeRecordHandler.updateChargeRecordStopCharging(endChargeId, reason + 0);
		}

		String devicenum = code;
		Equipment devicedata = Equipmenthandler.getEquipmentData(devicenum);
		Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
		Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
		String devicename = CommUtil.toString(devicedata.getRemark());
		String areaname = CommUtil.toString(devicedata.getName());
		String deviceport = CommUtil.toString(chargeRecord.getPort());
//		Double paymoney = chargeRecord.getExpenditure();
		Double paymoney = operallmoney;
		Integer resultinfo = 255;
		String startTime = CommUtil.toDateTime(chargeRecord.getBegintime());
		String endTime = CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date());
		String chargetime = "开始时间："+startTime + "\r\n结束时间：" + endTime;
		String servicphone = getServicePhone(devicetempid, deviceaid, merchantid);
		TempMsgUtil.endChargeSendMess(firstdata, ordernum, endChargeId, resultinfo, merchantid, uid, paymoney,
			chargetime, devicenum, deviceport, devicename, deviceaid, areaname, servicphone);
	}
	
	/**
	 * RoarWolf 2020-5-12
	 * 在线卡结束充电订单处理
	 * @param card_idStr
	 * @param hardversion
	 * @param code
	 * @param port
	 * @param card_cst
	 * @param chargeRecord
	 * @param reason
	 * @param time
	 * @param elec
	 */
	public static void onlinecardEndChargeDispose(String card_idStr,String hardversion,String code,int port,int card_cst,
			ChargeRecord chargeRecord, int reason, int time, int elec) {
		//在线卡数据处理
		OnlineCard onlineCard = OnlineCardHandler.cardIsExist(card_idStr);
		if (onlineCard != null) {
			Integer endChargeId = chargeRecord.getId();
			String ordernum = chargeRecord.getOrdernum();
			String cardID = card_idStr;
			Integer merid = CommUtil.toInteger(onlineCard.getMerid());
			Integer uid = CommUtil.toInteger(onlineCard.getUid());
			//============================================================================
			Double topupmoney = CommUtil.toDouble(onlineCard.getMoney());//充值金额
			Double sendmoney = CommUtil.toDouble(onlineCard.getSendmoney());//赠送金额
			Integer relevawalt = CommUtil.toInteger(onlineCard.getRelevawalt());
			if(relevawalt==1){
				User user = UserHandler.getUserInfo(uid);
				topupmoney = CommUtil.toDouble(user.getBalance());
				sendmoney = CommUtil.toDouble(user.getSendmoney());
			}
			//======================================================
			//cardopetype  1:消费   2:回收
			Integer cardopetype = 2;
			Double consumemoney = CommUtil.toDouble(card_cst)/10;
			logger.info(card_idStr + "应扣金额1=" + consumemoney);
			if (DisposeUtil.checkIfHasV3(hardversion)) {//如果是08则为消费 不是则为回收
				cardopetype = 1;
				consumemoney = getV3FeeMoney(code, port, uid, 0);
				logger.info(card_idStr + "应扣金额2=" + consumemoney);
				logger.info(card_idStr + "获取金额参数=" + code + port + uid);
			}
			logger.info(card_idStr + "应扣金额3=" + consumemoney);
			Map<String, Object> disposeData = onlinecardDataDispose(topupmoney, sendmoney, consumemoney, cardopetype);
			Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额
			//============================================================================
			Double opermoney = CommUtil.toDouble(disposeData.get("opermoney"));//操作总额
			Double opertopupmoney = CommUtil.toDouble(disposeData.get("opertopupmoney"));	//操作充值金额
			Double opersendmoney = CommUtil.toDouble(disposeData.get("opersendmoney"));	//操作赠送金额
			//==========================================================================
			Double topupbalance = CommUtil.toDouble(disposeData.get("topupbalance"));//充值余额
			Double sendbalance = CommUtil.toDouble(disposeData.get("sendbalance"));//赠送余额
			Double accountbalance = CommUtil.toDouble(disposeData.get("accountbalance"));//账户余额
			//==========================================================================
			if (cardopetype == 2) {// 退费(回收)
				if(relevawalt==2){//不关联钱包
					OnlineCardHandler.updateCardMoney(opertopupmoney, cardID, 1, sendbalance);
					OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 2, 1);
					logger.info("wolflog---" + "当前卡号为：" + cardID + "---实时操作金额为：" + opermoney + "元");
				}else{//关联钱包
					//修改用户钱包余额
					UserHandler.updateWalltMoney(opertopupmoney, opersendmoney, 1, uid);
					//添加用户明细记录
					UserHandler.addGenDetail(uid, merid, ordernum, opertopupmoney, opersendmoney, 
						opermoney, accountbalance, topupbalance, sendbalance , 5, cardID);
					//添加在线卡记录
					OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, 
						opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 2, 1);
					logger.info("wolflog---" + "当前卡号为：" + cardID + "---实时操作金额为：" + opermoney + "元");
				}
			} else if (cardopetype == 1) {//扣费（消费）
				Double comparemoney = CommUtil.subBig(accountmoney, opermoney); //总额、操作比较金额差
				if(comparemoney>=0){//金额足够，扣费
					if(relevawalt==2){//不关联钱包
						OnlineCardHandler.updateCardMoney(opertopupmoney, cardID, 0, sendbalance);
						OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 1, 1);
						logger.info("wolflog---" + "当前卡号为：" + cardID + "---实时操作金额为：" + opermoney + "元");
					}else{//关联钱包
						//修改用户钱包余额
						UserHandler.updateWalltMoney(opertopupmoney, opersendmoney, 0, uid);
						//添加用户明细记录
						UserHandler.addGenDetail(uid, merid, ordernum, opertopupmoney, opersendmoney, 
								opermoney, accountbalance, topupbalance, sendbalance , 9, cardID);
						//添加在线卡记录
						OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, 
								opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 1, 1);
						logger.info("wolflog---" + "当前卡号为：" + cardID + "---实时操作金额为：" + opermoney + "元");
					}
				}else{//金额不够，返回
					logger.info("wolflog---" + "订单号   "+ordernum+"  该笔订单卡号为：" + cardID + "余额不足---实时操作金额为：" + opermoney + "元");
				}
			}
			Equipmenthandler.addRealchargerecord(endChargeId, uid, merid, code, (byte) port, time, elec, 0, 0.0, 0.0, 0.0);
			long begintime = chargeRecord.getBegintime().getTime();
			long nowtime = System.currentTimeMillis();
			int useTime = (int) ((nowtime - begintime)/60/1000);
			ChargeRecordHandler.updateChargeResult(1, endChargeId, chargeRecord.getExpenditure(), consumemoney, reason + 0, 2, useTime, 0);
			chargeRecord.setExpenditure(consumemoney);
		}
	}
	
	public static Map<String, Object> onlinecardDataDispose( Double topupmoney, Double sendmoney, Double consumemoney, Integer type) {
		//在线卡数据处理
		//============================================================================
		Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额
		//============================================================================
		Double opermoney = consumemoney;//操作总额
		Double opertopupmoney = 0.00;	//操作充值金额
		Double opersendmoney = 0.00;	//操作赠送金额
		//==========================================================================
		Double topupbalance = 0.00;//充值余额
		Double sendbalance = 0.00;//赠送余额
		Double accountbalance = 0.00;//账户余额
		//==========================================================================
		if(type==1){
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
		}else if (type==2){
			if(topupmoney>0) {
				opertopupmoney = opermoney;
				topupbalance = CommUtil.addBig(topupmoney, opertopupmoney);
				opersendmoney = 0.00;
				sendbalance = sendmoney;
			}else{
				opertopupmoney = 0.00;
				topupbalance = 0.00;
				opersendmoney = opermoney;
				sendbalance = CommUtil.addBig(sendmoney, opersendmoney);
			}
			accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
		}
		Map<String, Object> mapinfo = new HashMap<>();
		mapinfo.put("topupmoney", topupmoney);
		mapinfo.put("sendmoney", sendmoney);
		mapinfo.put("accountmoney", accountmoney);
		mapinfo.put("opermoney", opermoney);
		mapinfo.put("opertopupmoney", opertopupmoney);
		mapinfo.put("opersendmoney", opersendmoney);
		mapinfo.put("topupbalance", topupbalance);
		mapinfo.put("sendbalance", sendbalance);
		mapinfo.put("accountbalance", accountbalance);
		return mapinfo;
	}
	

	/**
	 * @Description：在线卡消费逻辑处理
	 * @param consumemoney: 消费金额     operationtype:操作类型[0:扣费    1:回收退费]
	 * @param ordernum:订单号    cardID:卡号    code:设备号
	 * @author： origin 2020年6月28日上午11:25:32
	 * 3000  	处理出现异常(异常错误)
	 * 3001	在线卡查询不到
	 * 3002	处理类型错误（传过来的处理类型不正确）
	 * 3003	在线卡金额不足
	 */
	public static Map<String, Object> onlinecardDataDisposes(Double consumemoney,
			Integer operationtype, String ordernum, String cardID, String code) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			OnlineCard onlineCard = OnlineCardHandler.cardIsExist(cardID);
			if(onlineCard!=null){
				//在线卡数据处理
				//============================================================================
				Integer merid = CommUtil.toInteger(onlineCard.getMerid());
				Integer uid = CommUtil.toInteger(onlineCard.getUid());
				Double topupmoney = CommUtil.toDouble(onlineCard.getMoney());//充值金额
				Double sendmoney = CommUtil.toDouble(onlineCard.getSendmoney());//赠送金额
				Integer relevawalt = CommUtil.toInteger(onlineCard.getRelevawalt());
				if(relevawalt==1){
					User user = UserHandler.getUserInfo(uid);
					topupmoney = CommUtil.toDouble(user.getBalance());
					sendmoney = CommUtil.toDouble(user.getSendmoney());
				}
				Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额
				//============================================================================
				Double opermoney = consumemoney;//操作总额
				Double opertopupmoney = 0.00;	//操作充值金额
				Double opersendmoney = 0.00;	//操作赠送金额
				//==========================================================================
				Double topupbalance = 0.00;//充值余额
				Double sendbalance = 0.00;//赠送余额
				Double accountbalance = 0.00;//账户余额
				//==========================================================================
				//1:退费(回收)  0:扣费（消费）
				if(operationtype==0){//扣费（消费）
					Double comparemoney = CommUtil.subBig(accountmoney, opermoney); //总额、操作比较金额差
					if(comparemoney>=0){//金额足够，扣费
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
							OnlineCardHandler.updateCardMoney(opertopupmoney, cardID, 0, sendbalance);
							OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 1, 1);
							logger.info("wolflog---" + "当前卡号为退费IC：" + cardID + "---实时扣费金额为：" + opermoney + "元");
						}else{//关联钱包
							//修改用户钱包余额
							UserHandler.updateWalltMoney(opertopupmoney, opersendmoney, 0, uid);
							//添加用户明细记录
							UserHandler.addGenDetail(uid, merid, ordernum, opertopupmoney, opersendmoney, 
									opermoney, accountbalance, topupbalance, sendbalance , 9, cardID);
							//添加在线卡记录
							OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, 
									opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 1, 1);
							logger.info("wolflog---" + "当前卡号为退费：" + cardID + "---实时操作金额为：" + opermoney + "元");
						}
						resultdata.put("code", 2000);
						resultdata.put("message", "金额扣费完成");
					}else{
						resultdata.put("code", 3003);
						resultdata.put("message", "在线卡金额不足");
					}
				}else if (operationtype==1){//退费(回收)
					if(topupmoney>0) {
						opertopupmoney = opermoney;
						topupbalance = CommUtil.addBig(topupmoney, opertopupmoney);
						opersendmoney = 0.00;
						sendbalance = sendmoney;
					}else{
						opertopupmoney = 0.00;
						topupbalance = 0.00;
						opersendmoney = opermoney;
						sendbalance = CommUtil.addBig(sendmoney, opersendmoney);
					}
					accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
					if(relevawalt==2){//不关联钱包
						OnlineCardHandler.updateCardMoney(opertopupmoney, cardID, 1, sendbalance);
						OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 2, 1);
						logger.info("wolflog---" + "当前卡号为退费IC：" + cardID + "---实时操作金额为：" + opermoney + "元");
					}else{//关联钱包
						//修改用户钱包余额
						UserHandler.updateWalltMoney(opertopupmoney, opersendmoney, 1, uid);
						//添加用户明细记录
						UserHandler.addGenDetail(uid, merid, ordernum, opertopupmoney, opersendmoney, 
							opermoney, accountbalance, topupbalance, sendbalance , 5, cardID);
						//添加在线卡记录
						OnlineCardHandler.addOnlineCardOperate(uid, merid, ordernum, cardID, code, accountbalance, 
							opertopupmoney, opersendmoney, opermoney, topupbalance, sendbalance, 2, 1);
						logger.info("wolflog---" + "当前卡号为退费：" + cardID + "---实时操作金额为：" + opermoney + "元");
					}
					resultdata.put("code", 2000);
					resultdata.put("message", "回收金额成功");
				}else{
					resultdata.put("code", 3002);
					resultdata.put("message", "处理类型错误");
				}
				resultdata.put("accountbalance", accountbalance);
			}else{
				resultdata.put("code", 3001);
				resultdata.put("message", "在线卡查询不到");
//				map.put("result", result);
			}
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			resultdata.put("code", 3000);
			resultdata.put("message", "处理出现异常");
			return resultdata;
		}
	}
	
	/**
	 * @Description：设备报警通知
	 * @param devicenum 设备号   port:端口号   feedback：反馈原因
	 * @author： origin  2020年7月1日下午6:19:15
	 */
	public static void deviceStatusFeedback( String devicenum, Integer port, String feedback) {
		try {
			Map<String, Object> deviceinfo = Equipmenthandler.acquireDeviceRelevaData(devicenum);
			//设备信息
			String devicename = CommUtil.trimToEmpty(deviceinfo.get("devicename"));
			//商户信息
			Integer merid = CommUtil.toInteger(deviceinfo.get("merid"));
			String openid = CommUtil.toString(deviceinfo.get("openid"));
			//小区信息
			String areaname = CommUtil.trimToEmpty(deviceinfo.get("areaname"));
			String areaaddress = CommUtil.trimToEmpty(deviceinfo.get("areaaddress"));
			String time = CommUtil.toDateTime();
			String first = "尊敬的用户，监测系统检测到您的设备发生异常，请及时处理。"; 
			
			StringBuffer devicedata = new StringBuffer(areaname);
			if(areaname!=null) devicedata.append(" ");
			devicedata.append(devicename);
			
			StringBuffer devicenumber = new StringBuffer(devicenum);
			if(port!=null) devicenumber.append("-").append(port);
			String url = null;
			TempMsgUtil.giveDealerAnAlarmMess( first, openid, devicedata.toString(), devicenumber.toString(), feedback, time, url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据日志处理正在充电的记录数据
	 * @param endChargeId
	 * @param power
	 * @param equipment
	 * @param elec
	 * @param time
	 * @param code
	 * @param port
	 */
	public static void disposeLogData(Integer endChargeId, short power, Equipment equipment,
			short elec, short time, String code, byte port) {
		if (endChargeId != 0 && endChargeId != -1) {
			String hardversion = equipment.getHardversion();
			ChargeRecord chargeRecord = ChargeRecordHandler.getChargeRecordById(endChargeId);
			Integer paytype = chargeRecord.getPaytype();
			List<ChargeRecord> chargeList = ChargeRecordHandler.getChargeListUnfinish(endChargeId);
			int allTime = 0;
			int allelec = 0;
			double allMoney = 0.0;
			for (ChargeRecord charge : chargeList) {
				allTime += Integer.parseInt(charge.getDurationtime());
				allelec += charge.getQuantity();
				allMoney += charge.getExpenditure();
			}
			long currentTime = System.currentTimeMillis();
			long begintime = chargeRecord.getBegintime().getTime();
			boolean wolfFlag1 = (currentTime - begintime) < (allTime * 60 * 1000 + 120000);
			if (DisposeUtil.checkIfHasV3(hardversion) && allTime == 0) {
				wolfFlag1 = true;
			}
			if (((currentTime - begintime) > 0) && wolfFlag1) {
				if (power >= 0) {  
					double money = -1.0;
					int normalclac = elec;
					boolean wolfflag = false;
					if (DisposeUtil.checkIfHasV3(hardversion)) {
						if (elec < 0) {
							normalclac = elec & 0x7fff;
						} else {
							normalclac = allelec - elec;
						}
						wolfflag = true;
					} else {
						if ("07".equals(hardversion)) {
							normalclac = elec & 0xffff;
						} else {
							normalclac = allelec - (elec & 0xffff);
						}
						if (allTime == 0 && allelec == 0) {
							wolfflag = true;
						} else {
							wolfflag = (time <= allTime) && (normalclac <= allelec);
						}
					}
					if (wolfflag) {
						try {
							int rateusetime = allTime - time;
							if (allTime == 0 || rateusetime < 0 || rateusetime > allTime) {
								rateusetime = (int) ((currentTime - begintime)/60/1000);
							}
							int rateuseelec = normalclac;
							int realusetime = (int) ((System.currentTimeMillis() - begintime) / 1000 / 60);
							if (DisposeUtil.checkIfHasV3(hardversion)) {
								if (rateuseelec >= 0) {
									ChargeRecordHandler.updateChargeInfo(endChargeId, realusetime, rateuseelec);
								}
							} else if (rateuseelec >= 0 && rateuseelec <= allelec) {
								if (rateusetime > realusetime) {
									ChargeRecordHandler.updateChargeInfo(endChargeId, realusetime, rateuseelec);
								} else {
									ChargeRecordHandler.updateChargeInfo(endChargeId, rateusetime, rateuseelec);
								}
							}
						} catch (Exception e) {
							logger.warn("修改使用电量时间错误：");
							e.printStackTrace();
						}
					}
					if (DisposeUtil.checkIfHasV3(hardversion)) {
						Integer uid = chargeRecord.getUid();
						if ((paytype == 7 && allMoney == 0.0) || (paytype == 1 || paytype == 2)) {
							money = byTimeTempSubFee(code, uid, power, port, 0);
						}
						if (time >= 0 && power >= 0) {
							if (elec < 0) {
								Equipmenthandler.addRealchargerecord(endChargeId, chargeRecord.getUid(), chargeRecord.getMerchantid(), code, port, time & 0xffff, elec & 0x7fff, power, -1.0, -1.0, money);
							} else {
								Equipmenthandler.addRealchargerecord(endChargeId, chargeRecord.getUid(), chargeRecord.getMerchantid(), code, port, time & 0xffff, elec, power, -1.0, -1.0, money);
							}
						}
					} else {
						if (time >= 0 && elec >= 0 && power >= 0) {
							Equipmenthandler.addRealchargerecord(endChargeId, chargeRecord.getUid(), chargeRecord.getMerchantid(), code, port, time & 0xffff, elec & 0xffff, power, -1.0, -1.0, money);
						}
					}
				}
			}
		}
	}
	
	public static boolean checkSessionId(Map<String,String> map,int session_id) {
		if (DisposeUtil.checkMapIfHasValue(map)) {
			String session_idStr = map.get("session_id");
			String updateTime = map.get("updateTime");
			int session_id1 = Integer.parseInt(session_idStr);
			long nowTime = System.currentTimeMillis();
			long updateTimeLong = Long.parseLong(updateTime);
			if (session_id1 == session_id && (nowTime - updateTimeLong) > 600000) {
				return true;
			} else if (session_id1 == session_id) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	public static byte[] buquan70wei(byte[] bytes, byte[] strBytes) {
		int length = strBytes.length;
		for (int i = 0; i < length; i++) {
			bytes[i] = strBytes[i];
		}
		return bytes;
	}
	
	public static boolean checkStringParamIfResubmit(String param) {
		boolean flag = true;
		Long updateTime = v3DisposeDataMap.get(param);
		if (updateTime != null) {
			long nowTime = System.currentTimeMillis();
			if ((nowTime - updateTime) < 60 * 1000) {
				flag = false;
			}
		}
		return flag;
	}
	
	/**
	 * 钱包全额退款
	 * @param chargeRecord
	 * @param userBalance
	 * @param usersendmoney
	 * @param code
	 * @param reason
	 */
	public static void walletAllRefund(ChargeRecord chargeRecord,Double userBalance,
			Double usersendmoney,String code,int reason) {
		double money = chargeRecord.getExpenditure();
		int endChargeId = chargeRecord.getId();
		int uid = chargeRecord.getUid();
		int merchantid = chargeRecord.getMerchantid();
		Integer port = chargeRecord.getPort();
		ChargeRecordHandler.updateChargeRefundNumer(1, endChargeId, money, money, 0, 0);
		logger.info(chargeRecord.getOrdernum() + "钱包支付充电失败，已退款");
		
		//============================================================================
		Double topupmoney = CommUtil.toDouble(userBalance);//充值金额
		Double sendmoney = CommUtil.toDouble(usersendmoney);//赠送金额
		Double accountmoney = CommUtil.addBig(userBalance, usersendmoney);//账户金额
		Double opermoney = CommUtil.toDouble(chargeRecord.getExpenditure());//操作总额
		Double opertopupmoney = 0.00;	//操作充值金额
		Double opersendmoney = 0.00;	//操作赠送金额
		Double topupbalance = 0.00;//充值余额
		Double sendbalance = 0.00;//赠送余额
		if(topupmoney>0) {
			opertopupmoney = opermoney;
			topupbalance = CommUtil.addBig(topupmoney, opertopupmoney);
			opersendmoney = 0.00;
			sendbalance = sendmoney;
		}else{
			opertopupmoney = 0.00;
			topupbalance = 0.00;
			opersendmoney = opermoney;
			sendbalance = CommUtil.addBig(sendmoney, opersendmoney);
		}
		Double accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额

		ChargeRecordHandler.updateUserMoney(opertopupmoney, uid, opersendmoney, 5);
		
		Money moneyEntity = new Money();
		moneyEntity.setUid(uid);
		moneyEntity.setOrdernum(chargeRecord.getOrdernum());
		moneyEntity.setPaytype(3);
		moneyEntity.setStatus(1);
		
		moneyEntity.setMoney(opertopupmoney);
		moneyEntity.setSendmoney(opersendmoney);
		moneyEntity.setTomoney(opermoney);
		moneyEntity.setBalance(accountbalance);
		moneyEntity.setTopupbalance(topupbalance);
		moneyEntity.setGivebalance(sendbalance);
		
		moneyEntity.setRemark("wallet");
		ChargeRecordHandler.addMoneyRecord(moneyEntity);
		UserHandler.addGenDetail(chargeRecord.getUid(), merchantid, chargeRecord.getOrdernum(),
				opertopupmoney, opersendmoney, opermoney, accountbalance, topupbalance, sendbalance, 5, null);
		TradeRecordHandler.addTradeRecord(chargeRecord.getMerchantid(), 0,
				chargeRecord.getUid(), chargeRecord.getOrdernum(),
				chargeRecord.getExpenditure(), 0, 0, code, 1, 1, 2);
		//TODO 发送消息    钱包付款 充电异常返回
		uid = CommUtil.toInteger(uid);
		Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
		Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
		Double refundMoney = paymoney;//全额退款退款金额等于付款金额
		Double consumemoney =  CommUtil.subBig(paymoney, refundMoney);//消费金额
		Integer orderid = CommUtil.toInteger(chargeRecord.getId());
		String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
		String deviceport = CommUtil.toString(port);
		Integer resultinfo = CommUtil.toInteger(reason);
		String startTime = CommUtil.toDateTime(chargeRecord.getBegintime());
		String endTime = CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date());
		String chargetime = "开始时间："+startTime + "\r\n结束时间：" + endTime;
		String devicenum = code;
		Equipment devicedata = Equipmenthandler.getEquipmentData(devicenum);
		Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
		Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
		String devicename = CommUtil.toString(devicedata.getRemark());
		String areaname = CommUtil.toString(devicedata.getName());
		String servicphone = getServicePhone(devicetempid, deviceaid, merid);
		
		String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=1&id="+orderid;
		User user = UserHandler.getUserInfo(uid);
		String oppenid = user.getOpenid();
		String returnfirst = "您好，钱包支付充电失败，原路自动退款到账户。";
		String servername = "钱包退款";
		TempMsgUtil.returnMsgTemp(returnfirst, oppenid, servername, ordernum, endTime, paymoney, servicphone, CommonConfig.PAYRETURNMES, urltem);
		TempMsgUtil.endChargeSendMess("尊敬的用户，您的充电已经结束。", ordernum, orderid, resultinfo, merid, uid, consumemoney, chargetime, 
				devicenum, deviceport, devicename, deviceaid, areaname, servicphone);
	}
	
	/**
	 * 微信全额退款
	 * @param chargeRecord
	 * @param reason
	 * @param code
	 */
	public static void wxpayAllRefund(ChargeRecord chargeRecord, int reason, String code) {
		Double money = chargeRecord.getExpenditure();
		Integer merchantid = chargeRecord.getMerchantid();
		Integer paytype = chargeRecord.getPaytype();
		Integer uid = chargeRecord.getUid();
		Integer port = chargeRecord.getPort();
		WXPayConfigImpl config;
		try {
			config = WXPayConfigImpl.getInstance();
			WXPay wxpay = new WXPay(config);
			String total_fee = "";
			String out_trade_no = "";
			String moneyStr = String.valueOf(chargeRecord.getExpenditure() * 100);
			int idx = moneyStr.lastIndexOf(".");
			total_fee = moneyStr.substring(0, idx);
			out_trade_no = chargeRecord.getOrdernum();// 退款订单号
			// 根据充电订单号中商家id查询商家的信息
			String subMer = UserHandler.selectSubMerConfig(chargeRecord.getMerchantid());
			SortedMap<String, String> params = new TreeMap<>();
			String subMchId = null;
			// subMer为空表示为普通商户
			if(subMer != null && !"".equals(subMer)){
				subMchId = subMer;
				logger.info("使用微信特约商户号:"+subMchId+"进行退款");
			}else{
				subMchId = WeiXinConfigParam.SUBMCHID;
				logger.info("使用服务平台商户号:"+subMchId+"进行退款");
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
						// 根据退款订单查询交易记录
						TradeRecord tradere = TradeRecordHandler.getTradereInfo(out_trade_no);
						String comment = CommUtil.toString(tradere.getComment());
						Integer manid = tradere.getManid();
						// 根据设备号查询小区id
						int aid = Equipmenthandler.getEquipmentByCode(code);
						if (aid != 0) {
							UserHandler.areaRefund(aid, money);
						}
						UserHandler.equRefund(code, money);
						//TODO 充电退费
						if(subMer != null && !"".equals(subMer)){
							logger.info("开始统计微信特约商户:"+merchantid+"资金信息");
							//查询特约商户余额
							Double merEarnings = CommUtil.toDouble(UserHandler.getUserEarningsById(merchantid));
							// 微信特约商户只更新收益信息
							UserHandler.merAmountRefund(merchantid, money);
							//添加商户余额明细
							UserHandler.addMerDetail(merchantid, out_trade_no, money, merEarnings, MerchantDetail.CHARGESOURCE, paytype, MerchantDetail.REFUND);
						}else{
						// 计算合伙人分成
						dealerIncomeRefund(comment, chargeRecord.getMerchantid(), money, out_trade_no, 
								new Date(), MerchantDetail.CHARGESOURCE, paytype, MerchantDetail.REFUND);
					}
					// 添加交易记录
					TradeRecordHandler.addTradeRecordInfo(chargeRecord.getMerchantid(),
							manid, chargeRecord.getUid(), chargeRecord.getOrdernum(),
							chargeRecord.getExpenditure(), tradere.getMermoney(),
							tradere.getManmoney(), code, 1, 2, 2,tradere.getHardver(),comment);
					// 更新充电退款数据
					ChargeRecordHandler.updateChargeRefundNumer(1, chargeRecord.getId(),
							chargeRecord.getExpenditure(), 0.0, 0, 0);
					uid = CommUtil.toInteger(uid);
					// 参数
					Integer merid = CommUtil.toInteger(chargeRecord.getMerchantid());
					Double paymoney = CommUtil.toDouble(chargeRecord.getExpenditure());
					Double refundMoney = paymoney;//全额退款退款金额等于付款金额
					Double consumemoney =  CommUtil.subBig(paymoney, refundMoney);//消费金额
					Integer orderid = CommUtil.toInteger(chargeRecord.getId());
					String ordernum = CommUtil.toString(chargeRecord.getOrdernum());
					String deviceport = CommUtil.toString(port);
					Integer resultinfo = CommUtil.toInteger(reason);
					String startTime = CommUtil.toDateTime(chargeRecord.getBegintime());
					String endTime = CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss", new Date());
					String chargetime = "开始时间："+startTime + "\r\n结束时间：" + endTime;
					
					String devicenum = code;
					Equipment devicedata = Equipmenthandler.getEquipmentData(devicenum);
					Integer devicetempid = CommUtil.toInteger(devicedata.getTempid());
					Integer deviceaid = CommUtil.toInteger(devicedata.getAid());
					String devicename = CommUtil.toString(devicedata.getRemark());
					String areaname = CommUtil.toString(devicedata.getName());
					String servicphone = getServicePhone(devicetempid, deviceaid, merid);
					
					String urltem = "";
					User user = UserHandler.getUserInfo(uid);
					String oppenid = user.getOpenid();
					String returnfirst = "您好，支付充电失败，金额自动原路退款到账户。";
					String servername = "微信全额退款";
					TempMsgUtil.returnMsgTemp(returnfirst, oppenid, servername, ordernum, endTime, paymoney, servicphone, CommonConfig.PAYRETURNMES, urltem);
					
					TempMsgUtil.endChargeSendMess("尊敬的用户，您的充电已经结束。", ordernum, orderid, resultinfo, merid, uid, consumemoney, chargetime, 
						devicenum, deviceport, devicename, deviceaid, areaname, servicphone);
					
					}
					if ("FAIL".equals(r.get("result_code"))) {
						// ChargeRecordHandler.updateChargeRefundNumer(endChargeId);
					}
				} catch (Exception e) {
					logger.error(chargeRecord.getOrdernum() + e.getMessage() + "微信退款失败");
				}
			}
		} catch (Exception e) {
			logger.error(chargeRecord.getOrdernum() + e.getMessage() + "微信退款失败");
		}
		// 结束充电微信发送通知
		logger.info("wolflog---" + "微信支付充电失败，已退款");
	}
	
	public static boolean checkIfRefund(int uid,int merchantid, String code) {
		boolean flag = false;
		boolean checkBind = UserHandler.checkUserBindMidIsMerId(uid, merchantid);
		TemplateParent templateParent = Equipmenthandler.getTempPermit(code);
		int permit = 0;
		if (templateParent != null) {
			permit = templateParent.getPermit();
		}
		if (permit == 1) {
			if (checkBind) {
				flag = true;
			}
		}
		return flag;
	}
}
