package com.hedong.hedongwx.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.utils.MD5Util;

public class CommonConfig {

	private final Logger logger = LoggerFactory.getLogger(CommonConfig.class);
	
	public static  String ZIZHUCHARGE = "http://www.tengfuchong.com.cn";
	public static  String ZIZHUCHARGES = "http://www.tengfuchong.com.cn/";
//	public static  String ZIZHUCHARGES = "http://www.he360.cn/";
	public static  String ZIZHUCHARG = "http://www.he360.cn/";
	/** 微信开放平台 AppID*/
	public static String OPEN_APPID = "wx695275de73b7dad4";
	/** 微信开放平台AppSecret*/
	public static String OPEN_APPSECRET = "28d047d2f5b77f121473df8747037825";
	//短信验证存储key
	public static String SMS_VALI_CODE = "SMS_VALI_CODE";
	
	public static String PAYSUCCEEDMES = "本次交易付款成功，开始充电，若出现扣费不充电的情况，请换个设备扫码充电，已扣费用会在10分钟内原路退回。";
	public static String PAYMoneySUCCEEDMES = "如有疑问，请及时联系商家。";
	public static String PAYRETURNMES = "如有疑问请及时咨询商户。";
	public static String REFUNDSUCCEEDMES = "";
	
	//默认返回JSONArray格式
	public static JSONArray DEFAULT_JSONARRAY = new JSONArray();
	//默认返回JSONObject格式
	public static JSONObject DEFAULT_JSONOBJECT = new JSONObject();
	
	public static Integer numerical = 10;
	
	
	public static boolean isExistSessionUser(HttpServletRequest request) {
		 Object session = request.getSession().getAttribute("admin");
		if(session==null){
			return true;
		}
		return false;
	} 
	
	public static User getAdminReq(HttpServletRequest request) {
		User admin =  new User();
		if (request != null) {  
			admin = (User) request.getSession().getAttribute("admin");
		}
		return admin;
	} 
	
	public static boolean verifyPass(User user, String  password) {
		if (password == null || user == null) {
			return false;
		} else {
			String md5Pwd = MD5Util.MD5Encode(password, "utf-8");
			String oripass = user.getPassword();
			if (!md5Pwd.equals(oripass)) {
				return false;
			}
		}
		return true;
	} 

	public static Map<String, Object> messg(Integer code) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		if(code==200){
			map.put("messg", "succeed");
		}else if(code==400){
			map.put("messg", "登录信息获取失败，请重新登录");
		}else if(code==401){
			map.put("messg", "获取对象失败");
		}else if(code==402){
			map.put("messg", "获取对象为空");
		}else if(code==403){
			map.put("messg", "异常错误");
		}else if(code==405){
			map.put("messg", "对象输入的格式不正确");
		}
		return map;
	}
	
//	protected Map<String, Object> buildResponse(int ret, String message, Object body) {
//		Map<String, Object> response = new HashMap<String, Object>();
//		response.put("ret", ret);
//		response.put("message", message);
//		if(null == body || "".equals(body)){
//			response.put("body", DEFAULT_RESPONSE);
//		}else{
//			response.put("body", body);
//		}
//		return response;
//	}
//	
	
	
}
