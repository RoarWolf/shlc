package com.hedong.hedongwx.utils;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

public class CheckUtil {

	public static final String TOKEN = "hdwxtoken";
//	public static final String TOKEN = "hetengtoken";

	public static boolean checkSignature(HttpServletRequest request) {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String[] strarr = new String[] { TOKEN, timestamp, nonce };
		// 排序
		Arrays.sort(strarr);

		// 生成字符串
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strarr.length; i++) {
			sb.append(strarr[i]);
		}

		// sha1加密
		String temp = getSha1(sb.toString());

		return temp.equals(signature);
	}

	public static String getSha1(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));

			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}
}
