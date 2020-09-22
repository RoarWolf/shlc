package com.hedong.hedongwx.utils;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class ReplyMsgUtil {
	
	public static void main(String[] args) {
		
		try {
			Class clazz = Class.forName("com.hedong.hedongwx.utils.SendMsgUtil");
			Method method = clazz.getMethod("send_1");
			method.invoke(method, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static ByteBuffer reply_1() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte sop = 0x55;
		byte len = 0x05;
		byte cmd = 0x01;
		byte result = 0x01;
		byte[] data = new byte[]{0x0B,0x02};
		byte sum = (byte) (len ^ cmd ^ result ^ data[0] ^ data[1]);
		buffer.put(sop);
		buffer.put(len);
		buffer.put(cmd);
		buffer.put(result);
		buffer.put(data);
		buffer.put(sum);
		buffer.flip();
		return buffer;
	}
}
