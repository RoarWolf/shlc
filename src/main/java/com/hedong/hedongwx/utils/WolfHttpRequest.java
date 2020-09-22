package com.hedong.hedongwx.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
	
	/** 付款下发充电*/
	public static final String SEND_PAY_URL = "http://www.tengfuchong.com.cn/sendinstruct/informequipment";
	/** 设置设备参数*/
	public static final String SEND_SETSYSTEM_URL = "http://www.tengfuchong.com.cn/sendinstruct/setsystemparam";
	/** 读取设备参数*/
	public static final String SEND_READSYSTEM_URL = "http://www.tengfuchong.com.cn/sendinstruct/querysystemparam";
	/** 锁定解锁端口*/
	public static final String SEND_SETPORTSTATUS_URL = "http://www.tengfuchong.com.cn/sendinstruct/setportstatus";
	/** 远程停止端口*/
	public static final String SEND_STOPPORT_URL = "http://www.tengfuchong.com.cn/sendinstruct/setstopport";
	/** 远程停止端口(加载等待回复)*/
	public static final String SEND_STOPPORTLOAD_URL = "http://www.tengfuchong.com.cn/sendinstruct/setstopportload";
	/** 查询当前端口充电信息*/
	public static final String SEND_QUERYPORTCHARGESTATUS_URL = "http://www.tengfuchong.com.cn/sendinstruct/queryportstatus";
	/** 查询所有端口状态*/
	public static final String SEND_QUERYALLPORTSTATUS_URL = "http://www.tengfuchong.com.cn/sendinstruct/queryallportstatus";
	/** 查询当前端口状态*/
	public static final String SEND_QUERYPORTSTATUS_URL = "http://www.tengfuchong.com.cn/sendinstruct/quryportusestatus";
	/** 查询离线充值机当前卡号*/
	public static final String SEND_QUERYOFFLINECARD_URL = "http://www.tengfuchong.com.cn/sendinstruct/queryOfflineCard";
	/** 充值离线充值机当前卡号*/
	public static final String SEND_CHARGEOFFLINECARD_URL = "http://www.tengfuchong.com.cn/sendinstruct/chargeCard";
	/** 充值离线充值机当前卡号无回复*/
	public static final String SEND_CHARGEOFFLINECARDNOREPLY_URL = "http://www.tengfuchong.com.cn/sendinstruct/chargeCardNoReply";
	/** 线上支付模拟投币*/
	public static final String SEND_INCOINSPAY_URL = "http://www.tengfuchong.com.cn/sendinstruct/incoinsPay";
	/** 付款下发充电异步加载等待回复*/
	public static final String SEND_PAYLOAD_URL = "http://www.tengfuchong.com.cn/sendinstruct/chargePayAsyn";
	/** 获取主板信息*/
	public static final String SEND_MAININFO_URL = "http://www.tengfuchong.com.cn/sendinstruct/wolfmaininfo";
	/** 新版设置设备系统参数*/
	public static final String SEND_NEWSETSYS_URL = "http://www.tengfuchong.com.cn/sendinstruct/wolfsetsys";
	/** 新版获取设备系统参数*/
	public static final String SEND_NEWREADSYS_URL = "http://www.tengfuchong.com.cn/sendinstruct/wolfreadsys";
	/** 新版付款下发充电异步加载等待回复*/
	public static final String SEND_NEWPAYLOAD_URL = "http://www.tengfuchong.com.cn/sendinstruct/wolfnewpayload";
	/** 新版付款下发充电*/
	public static final String SEND_NEWPAY_URL = "http://www.tengfuchong.com.cn/sendinstruct/wolfnewpay";

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
	
	public static Map<String,String> sendChargePaydata(byte payport,short time,String money,String elecs,String code, int temp) {
		Map<String,String> map = new HashMap<>();
		map.put("port", payport + "");
		map.put("money", money);
		map.put("time", time + "");
		map.put("elec", elecs + "");
		map.put("code", code);
		if (temp == 0) {
			return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_PAY_URL);
		} else {
			return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_PAYLOAD_URL);
		}
	}
	
	public static Map<String,String> sendNewChargePaydata(byte payport,short time,String money,String elecs,String code, int chargeType, int temp) {
		Map<String,String> map = new HashMap<>();
		map.put("port", payport + "");
		map.put("money", money);
		map.put("time", time + "");
		map.put("elec", elecs + "");
		map.put("code", code);
		map.put("chargeType", chargeType + "");
		if (temp == 0) {
			return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_NEWPAY_URL);
		} else {
			return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_NEWPAYLOAD_URL);
		}
	}
	
	public static Map<String,String> sendOfflineCardPaydata(String code,String card_id,Short card_surp,Byte card_ope) {
		Map<String,String> map = new HashMap<>();
		map.put("code", code);
		map.put("card_id", card_id);
		map.put("card_surp", card_surp * 10 + "");
		map.put("card_ope", card_ope + "");
		return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_CHARGEOFFLINECARDNOREPLY_URL);
	}
	
	public static Map<String,String> sendIncoinsPaydata(String code, Byte num, Byte money) {
		Map<String,String> map = new HashMap<>();
		map.put("code", code);
		map.put("num", num + "");
		map.put("money", money + "");
		return WolfHttpRequest.httpconnectwolf(map, WolfHttpRequest.SEND_INCOINSPAY_URL);
	}
}
