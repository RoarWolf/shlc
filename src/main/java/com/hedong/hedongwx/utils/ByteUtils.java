package com.hedong.hedongwx.utils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;

public class ByteUtils {

	public static void main(String[] args) {
		// System.out.println(byteToBitString((byte) 0x12));
//		byte[] byteArray = getByteArray((byte) 0x4f);
//		System.out.println((byte) 0x4f >> 4);
//		for (int i = 0; i < 4; i++) {
//			System.out.println(byteArray[i]);
//		}
	}

	/**
	 * 获取一个字节的bit数组
	 *
	 * @param value
	 * @return
	 */
	public static byte[] getByteArray(byte value) {
		byte[] byteArr = new byte[8]; // 一个字节八位
		for (int i = 0; i < 4; i++) {
			byteArr[i] = (byte) (value & 1); // 获取最低位
			value = (byte) (value >> 1); // 每次右移一位
		}
		return byteArr;
	}

	/**
	 * 把byte转为字符串的bit
	 *
	 * @param b
	 * @return
	 */
	public static String byteToBitString(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1)
				+ (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1)
				+ (byte) ((b >> 0) & 0x1);
	}

	/**
	 * 获取一个字节第n位, 思路：右移n位，与1
	 *
	 * @param value
	 * @param index
	 * @return
	 */
	public static int get(byte value, int index) {
		return (value >> index) & 0x1;
	}

	/**
	 * 获取一个字节的第m到第n位
	 *
	 * @param value
	 * @param start
	 *            >0
	 * @param end
	 *            >0
	 * @return
	 */
	public static byte[] getBitRange(byte value, int start, int end) {
		byte[] rangeArray = new byte[end - start + 1];
		if (start > 7 || start < 0) {
			throw new RuntimeException("illegal start param");
		}
		if (end > 7 || end < 0) {
			throw new RuntimeException("illegal end param");
		}
		if (start > end) {
			throw new RuntimeException("start can not bigger than end");
		}
		if (start == end) {
			rangeArray[0] = (byte) ByteUtils.get(value, start);
			return rangeArray;
		}
		for (int i = end; i < start; i--) {
			rangeArray[i] = (byte) ByteUtils.get(value, start);
		}
		return rangeArray;
	}

	/**
	 * 位字符串转字节
	 * 
	 * @param str
	 * @return
	 */
	public static byte bitStringToByte(String str) {
		if (null == str) {
			throw new RuntimeException("when bit string convert to byte, Object can not be null!");
		}
		if (8 != str.length()) {
			throw new RuntimeException("bit string'length must be 8");
		}
		try {
			// 判断最高位，决定正负
			if (str.charAt(0) == '0') {
				return (byte) Integer.parseInt(str, 2);
			} else if (str.charAt(0) == '1') {
				return (byte) (Integer.parseInt(str, 2) - 256);
			}
		} catch (NumberFormatException e) {
			throw new RuntimeException("bit string convert to byte failed, byte String must only include 0 and 1!");
		}

		return 0;
	}

	public static byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
		int contentLength = request.getContentLength();
		if (contentLength < 0) {
			return null;
		}
		byte buffer[] = new byte[contentLength];
		for (int i = 0; i < contentLength;) {
			int readlen = request.getInputStream().read(buffer, i, contentLength - i);
			if (readlen == -1) {
				break;
			}
			i += readlen;
		}
		return buffer;
	}

	public static String getRequestPostStr(HttpServletRequest request) throws IOException {
		byte buffer[] = getRequestPostBytes(request);
		String charEncoding = request.getCharacterEncoding();
		if (charEncoding == null) {
			charEncoding = "UTF-8";
		}
		return new String(buffer, charEncoding);
	}

	public static String getRequestJsonString(HttpServletRequest request) throws IOException {
		String submitMehtod = request.getMethod();
		// GET
		if (submitMehtod.equals("GET")) {
			return new String(request.getQueryString().getBytes("iso-8859-1"), "utf-8").replaceAll("%22", "\"");
			// POST
		} else {
			return getRequestPostStr(request);
		}
	}
	
	public static int getLineNumber(Exception e) {
		return e.getStackTrace()[0].getLineNumber();
	}
	
	public static String replaceEmoji(String str) {
        String patternString = "[\uD83C\uDC04-\uD83C\uDE1A]|[\uD83D\uDC66-\uD83D\uDC69]|[\uD83D\uDC66\uD83C\uDFFB-\uD83D\uDC69\uD83C\uDFFF]|[\uD83D\uDE45\uD83C\uDFFB-\uD83D\uDE4F\uD83C\uDFFF]|[\uD83C\uDC00-\uD83D\uDFFF]|[\uD83E\uDD10-\uD83E\uDDC0]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEF6]";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }
}