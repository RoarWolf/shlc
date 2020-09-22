package com.hedong.hedongwx.utils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hedong.hedongwx.thread.Server2;

/**
 * 
 * @author RoarWolf
 * @description 新1拖2设备的通信发送包、解析包处理
 *
 */
public class NewSendmsgUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(NewSendmsgUtil.class);
	
	public static void main(String[] args) {
		send_0x0A("000001", "00000001");
	}
	
	public static byte clacSumVal(byte[] sums) {
		byte sum = 0x00;
		for (int i = 0; i < sums.length - 1; i++) {
			sum ^= sums[i];
		}
		return sum;
	}

	/**
	 * 
	 * @param code
	 * @param addr
	 */
	public static void send_0x0A(String code, String addr) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = (byte) 0x55;
		byte len = (byte) 0x08;
		byte cmd = (byte) 0x10;
		int addrInt = DisposeUtil.hexToInt(addr);
		byte result = (byte) 0x01;
		byte data1 = (byte) 0x00;
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.putInt(addrInt);
		buffer.put(result);
		buffer.put(data1);
		buffer.position(1);
		byte[] sums = new byte[len & 0xff];
		buffer.get(sums, 0, len);
		buffer.put(clacSumVal(sums));
		buffer.flip();
//		while (buffer.hasRemaining()) {
//			System.out.printf("0x%02x ", buffer.get());
//		}
//		buffer.position(0);
		Server2.sendMsg(code, buffer);
	}
	
	public static void parse_0x0A(AsynchronousSocketChannel channel, byte len, byte cmd, byte result,
			ByteBuffer buffer, String addr) {
//		Map<String, Map> allMap = new HashMap<>();
//		HashMap<String, String> map = new HashMap<>();
		byte port = buffer.get();
		short error_code = buffer.getShort();
		byte sum = buffer.get();
		String code = SendMsgUtil.getCodeByIpaddr(channel);
		String errorinfo = "";
		if (error_code == 1) {
			errorinfo = "疑似继电器粘连";
		} else {
			errorinfo = "其他错误";
		}
		SendMsgUtil.deviceStatusFeedback(code, port + 0, errorinfo);
		logger.info(code + "号机端口：" + port + ", 错误码：" + error_code + "上传设备故障已接收");
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.put(len);
		byteBuffer.put(cmd);
		byteBuffer.put(result);
		byteBuffer.put(port);
		byteBuffer.putShort(error_code);
		byteBuffer.flip();
		byte[] datas = new byte[100];
		byteBuffer.get(datas, 0, len);
		byte checkoutSum = SendMsgUtil.checkoutSum(datas);
		if (sum == checkoutSum) {
			logger.info("wolflog---" + "数据发送验证成功");
		} else {
			logger.info("wolflog---" + "数据发送验证失败");
		}
		send_0x0A(code, addr);
	}
}
