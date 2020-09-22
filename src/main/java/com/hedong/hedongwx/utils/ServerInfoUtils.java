package com.hedong.hedongwx.utils;

import java.util.ArrayList;
import java.util.List;

public class ServerInfoUtils {

	public static void main(String[] args) {
		// checkServerReceiveInfo("AA01234567898765432100");
		/*
		 * if (checkServerReceiveInfo("550604010a2c2a0f")) {
		 * System.out.println("信息接收成功"); } else { System.out.println("信息接收失败");
		 * }
		 */
//		serverReceiveInfoHandle("AA050401573c6b");
//		byte b = -86;
//		int int1 = Byte.toUnsignedInt(b);
//		String hexString = Integer.toHexString(int1);
//		System.out.println(hexString);
		byte[] message = packagingMessage();
		System.out.println(new String(message));
	}

	public static byte[] packagingMessage() {
		byte sop = (byte) 0xAA;
		String data = "wolf";
		byte[] datas = data.getBytes();
		byte len = (byte) (data.length() + 3);
		byte cmd = 0x01;
		byte result = 0x01;
		byte[] bytes1 = new byte[] { sop, len, cmd, result };
		byte[] addBytes = addBytes(bytes1, datas);
		return addBytes;
	}
	
	public static String parseReceiveInfo(byte sop,byte len,byte cmd,byte result,byte[] data) {
		return null;
	}

	/**
	 * 
	 * @param data1
	 * @param data2
	 * @return data1 与 data2拼接的结果
	 */
	public static byte[] addBytes(byte[] data1, byte[] data2) {
		byte[] data3 = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, data3, 0, data1.length);
		System.arraycopy(data2, 0, data3, data1.length, data2.length);
		return data3;

	}

	/**
	 * server sent message handle, packaging data package
	 * 
	 * @param cmdValue
	 * @param message
	 * @return
	 */
	public static String serverSendMessageHandle(int cmdValue, String message) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		String len = null;
		String cmd = "0" + Integer.toHexString(cmdValue);
		String result = "00";
		String data = null;
		sb.append("AA");
		if (message == null) {
			data = "00";
			// sb2.append(len);//len
			sb2.append(cmd);// cmd
			sb2.append(result);// result
			sb2.append(data);// data
			int length = sb2.toString().length() / 2 + 1;
			len = "0" + length;
			sb.append(len);
			sb.append(sb2);
			long sum = Long.parseLong(len) ^ Long.parseLong(cmd) ^ Long.parseLong(result) ^ Long.parseLong(data);
			String hexString = Long.toHexString(sum);
			if (hexString.length() % 2 != 0) {
				sb.append("0" + hexString);
			} else {
				sb.append(hexString);
			}
		} else {
			sb2.append(cmd);
			sb2.append(result);
			double parseDouble = Double.parseDouble(message);
			long parseLong = (long) (parseDouble * 100);

			String hexString2 = Long.toHexString(parseLong);
			if (hexString2.length() % 2 != 0) {
				data = "0" + hexString2;
			} else {
				data = hexString2;
			}
			// System.out.println(data);
			sb2.append(data);
			int length = sb2.toString().length() / 2 + 1;
			len = "0" + length;
			sb.append(len);
			sb.append(sb2);
			char[] charArray = sb.toString().substring(2).toCharArray();
			List<String> strList = new ArrayList<>();
			for (int i = 0; i < charArray.length; i += 2) {
				strList.add(charArray[i] + "" + charArray[i + 1]);
			}
			int sum = Integer.parseInt(strList.get(0));
			for (int i = 1; i < strList.size(); i++) {
				sum ^= Integer.parseInt(strList.get(i), 16);
			}
			if (sum < 16) {
				sb.append("0" + Integer.toHexString(sum));
			} else {
				sb.append(Integer.toHexString(sum));
			}
		}
		return sb.toString();
	}

	/**
	 * verification equipment sent message whether qualified
	 * 
	 * @param message
	 * @return
	 */
	public static boolean checkServerReceiveInfo(String message) {
		// 将接收的信息去掉空格
		String info = message.trim();
		// 取出接收新的头部分，55为正确
		String infoheader = info.substring(0, 2);
		// 取出接收信息的len
		int infolen = Integer.parseInt(info.substring(2, 4));
		// 截取接收信息的len、cmd、result、data
		String infosub = info.substring(2, info.length() - 2);
		char[] charArray = infosub.toCharArray();
		List<String> strList = new ArrayList<>();
		for (int i = 0; i < charArray.length; i += 2) {
			strList.add(charArray[i] + "" + charArray[i + 1]);
		}
		int sumInt = Integer.parseInt(strList.get(0));
		for (int i = 1; i < strList.size(); i++) {
			// 将16进制字符串转为10进制输出
			sumInt ^= Integer.parseInt(strList.get(i), 16);
		}
		String sum;
		if (sumInt < 16) {
			sum = "0" + Integer.toHexString(sumInt);
		} else {
			sum = Integer.toHexString(sumInt);
		}
		if (info.length() == 0 || !"55".equals(infoheader)) {
			System.out.println("发送的信息为空或者信息头不正确");
			return false;
		} else if (infolen != info.substring(4).length() / 2) {
			System.out.println("信息中的len计算不正确");
			return false;
		} else if (!sum.equals(info.substring(info.length() - 2))) {
			System.out.println("信息中的sum计算不正确");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * parse receive message
	 * 
	 * @param message
	 * @return need info
	 */
	public static String serverReceiveInfoHandle(String message) {
		String cmdtodata = message.substring(4, message.length() - 2);
		String cmd = cmdtodata.substring(0, 2);
		String result = cmdtodata.substring(2, 4);
		String data = cmdtodata.substring(4);
		System.out.println(cmd);
		System.out.println(result);
		System.out.println(data);
		System.out.println(Long.parseLong(data, 16));
		return null;
	}
}