package com.hedong.hedongwx.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author RoarWolf
 * @Date 2019年10月21日
 */
public class WolfHttpRequest {
	
	public static final String domain_url = "http://139.224.255.156/";
	
	/** 付款下发充电*/
	public static final String SEND_PAY_URL = domain_url + "deviceConnect/startCharge";
	/** 付款下发充电,等待回复*/
	public static final String SEND_PAYBACK_URL = domain_url + "deviceConnect/startChargeback";
	/** 预约设备*/
	public static final String SEND_YUYUE_URL = domain_url + "deviceConnect/yuyueCharge";
	/** 停止充电*/
	public static final String SEND_STOP_URL = domain_url + "deviceConnect/stopCharge";

	public static Map<String,String> httpconnectwolf(Map<String, String> map,String url) {
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
        try {
            List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
            Set<Entry<String,String>> entrySet = map.entrySet();
            for (Entry<String, String> entry : entrySet) {
            	parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
            post.setEntity(new UrlEncodedFormEntity(parameters));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            Map<String,String> parse = (Map) JSON.parse(result);
            if (parse == null || parse.isEmpty()) {
            	Map<String,String> resultMap = new HashMap<>();
            	resultMap.put("wolfcode", "1001");
            	resultMap.put("wolfmsg", "数据发送异常");
            	return resultMap;
            }
            return parse;
        } catch (Exception e) {
        	Map<String,String> resultMap = new HashMap<>();
        	resultMap.put("wolfcode", "1001");
        	resultMap.put("wolfmsg", "数据发送异常");
        	return resultMap;
//            e.printStackTrace();
        }
	}
	
	public static Map<String,String> sendChargePaydata(String devicenum, Integer port, Integer userid, 
			Double paymoney, String ordernum) {
		Map<String,String> map = new HashMap<>();
		map.put("devicenum", devicenum);
		map.put("port", port + "");
		map.put("userid", userid + "");
		map.put("paymoney", paymoney + "");
		map.put("ordernum", ordernum);
		return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_PAY_URL);
	}
	
	public static Map<String,String> sendChargePaydata(String ordernum) {
		Map<String,String> map = new HashMap<>();
		map.put("ordernum", ordernum);
		return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_PAYBACK_URL);
	}
	
	public static Map<String,String> sendYuyueChargedata(String devicenum, String port, Integer userid, String userType,
			String phonenum) {
		Map<String,String> map = new HashMap<>();
		map.put("devicenum", devicenum);
		map.put("port", port + "");
		map.put("userid", userid + "");
		map.put("userType", userType + "");
		map.put("phonenum", phonenum + "");
		return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_YUYUE_URL);
	}
	
	public static Map<String,String> sendStopChargedata(String devicenum, Integer port) {
		Map<String,String> map = new HashMap<>();
		map.put("devicenum", devicenum);
		map.put("port", port + "");
		return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_STOP_URL);
	}
	
}
