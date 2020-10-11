package com.hedong.hedongwx.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class SMSMobileSendUtil {

	public static final String SMS_URL = "https://api.mix2.zthysms.com/v2/sendSms";
	
	public static final String USERNAME = "shzylc";
	
	public static final String PASSWORD = "Sh123456";
	
	public static final String CONTENT = "【中源乐充】尊敬的用户，您的验证码为：RANDOM（10分钟内有效），请及时验证。";
	
	public static void main(String[] args) {
		mobileSend("18621563709");
	}
	
	public static String getRandom(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append((int)(Math.random()*9));
		}
		return sb.toString();
	}
	
	public static String getTKey() {
		String timeStr = System.currentTimeMillis() + "";
		return timeStr.substring(0, 10);
	}
	
	public static void mobileSend(String mobile) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("username", USERNAME);
			String tKey = getTKey();
			jsonObject.put("password", MD5Util.MD5Encode(MD5Util.MD5Encode(PASSWORD, "UTF-8") + tKey, "UTF-8"));
			jsonObject.put("tKey", tKey);
			jsonObject.put("mobile", mobile);
			String random = getRandom(6);
			jsonObject.put("content", CONTENT.replaceAll("RANDOM", random));
			System.out.println("sendinfo:" + jsonObject.toString());
			String sendSMSPost = HttpRequest.sendSMSPost(SMS_URL, jsonObject.toString());
			System.out.println("backinfo:" + sendSMSPost);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
