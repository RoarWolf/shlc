package com.hedong.hedongwx.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.jdom.JDOMException;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;

public class WxpayUtil {

	public static Map<String, Object> payParam(String openid, String notify_url, String ordernum,
			HttpServletRequest request, Double money2) throws Exception {
		String appId = WeixinUtil.APPID;
		SortedMap<String, String> params = new TreeMap<>();
		// 根据商户号查询商家的微信配置信息
		params.put("appid", appId);
		params.put("body", "中源乐充钱包充值");   
		params.put("mch_id", WeixinUtil.MCHID);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		params.put("notify_url", CommonConfig.ZIZHUCHARGES + notify_url);
		params.put("openid", openid);
		params.put("out_trade_no", ordernum);
		String ipAddr = getIpAddress(request);
		params.put("spbill_create_ip", ipAddr);
		String money = String.valueOf(money2 * 100);
		int idx = money.lastIndexOf(".");
		String total_fee = money.substring(0, idx);
		params.put("total_fee", total_fee);
		params.put("trade_type", "JSAPI");
		System.out.println("---" + params.get("out_trade_no"));
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String canshu = HttpRequest.getRequestXml(params);
		System.out.println("转xml：" + canshu);
		String sr = HttpRequest.sendPost(url, canshu);
		Map<String, String> map = XMLUtil.doXMLParse(sr);
		System.out.println("预订单返回信息：" + JSON.toJSONString(map));
		SortedMap<String, String> seconde = new TreeMap<>();
		seconde.put("appId", appId);
		String time = HttpRequest.getTimeStamp();
		seconde.put("timeStamp", time);
		String sjzf = HttpRequest.getRandomStringByLength(30);

		seconde.put("nonceStr", sjzf);
		seconde.put("package", "prepay_id=" + (String) map.get("prepay_id"));
		seconde.put("signType", "MD5");

		String sign2 = HttpRequest.createSign("UTF-8", seconde);

		Map<String, Object> paramMap = new HashMap<>();
		
		paramMap.put("appId", appId);
		paramMap.put("prepay_id", map.get("prepay_id"));
		paramMap.put("timeStamp", time);
		paramMap.put("paySign", sign2);
		paramMap.put("packagess", "prepay_id=" + (String) map.get("prepay_id"));
		paramMap.put("nonceStr", sjzf);
		paramMap.put("signType", "MD5");
		paramMap.put("out_trade_no", params.get("out_trade_no"));
		return paramMap;
	}
	
	/**
	 * get ipaddr
	 * 
	 * @param request
	 * @return ip
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.contains(",")) {
			ip = ip.split(",")[0].trim();
		}
		return ip;
	}
	
}
