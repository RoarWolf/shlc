package com.hedong.hedongwx.utils;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;

public class SMSMobileSendUtil {

	public static final String SMS_URL = "https://api.mix2.zthysms.com/v2/sendSms";
	
	public static final String USERNAME = "shzylc";
	
	public static final String PASSWORD = "Sh123456";
	
	public static final String CONTENT = "【中源乐充】尊敬的用户，您的验证码为：RANDOM（10分钟内有效），请及时验证。";
	
	public static void main(String[] args) {
		System.out.println(JSON.toJSONString(mobileSend("15235571294")));
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
	
	public static Object mobileSend(String mobile) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("username", USERNAME);
			String tKey = getTKey();
			jsonObject.put("password", MD5Util.MD5Encode(MD5Util.MD5Encode(PASSWORD, "UTF-8") + tKey, "UTF-8"));
			jsonObject.put("tKey", tKey);
			jsonObject.put("mobile", mobile);
			String random = getRandom(6);
			jsonObject.put("content", CONTENT.replaceAll("RANDOM", random));
			String sendSMSPost = HttpRequest.sendSMSPost(SMS_URL, jsonObject.toString());
			Map<String,Object> backMap = (Map<String, Object>) JSON.parse(sendSMSPost);
			JedisUtils.setnum(mobile, random, 600, 2);
			if (backMap.get("code").equals(200)) {
				backMap.put("result_code", 1000);
				backMap.put("result_info", "获取成功");
			} else {
				backMap.put("result_code", 1001);
				backMap.put("result_info", "获取失败");
			}
			return backMap;
		} catch (JSONException e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(1001, "系统异常", null);
		}
	}
}
