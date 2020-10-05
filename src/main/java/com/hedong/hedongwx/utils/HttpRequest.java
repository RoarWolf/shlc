package com.hedong.hedongwx.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.jdom.JDOMException;

public class HttpRequest {

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String createSign(String characterEncoding, SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		// hetengfuwu
		sb.append("key=" + WeiXinConfigParam.KEY);// 最后加密时添加商户密钥，由于key值放在最后，所以不用添加到SortMap里面去，单独处理，编码方式采用UTF-8
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}
	
	
	/**
	 * 测试
	 * @param characterEncoding
	 * @param parameters
	 * @return
	 */
	public static String createSignTobank(String characterEncoding, SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		// hetengfuwu
		sb.append("key=" + WeiXinConfigParam.SUBKEY);// 最后加密时添加商户密钥，由于key值放在最后，所以不用添加到SortMap里面去，单独处理，编码方式采用UTF-8
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}
	
	/**
	 * 银联支付签名
	 * @param characterEncoding
	 * @param parameters
	 * @return
	 */
	public static String createUnionpaySign(String characterEncoding, SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		//银联支付的秘钥
		sb.append("key="+WeiXinConfigParam.UNIONKEY);
		String sign = MD5Util.MD5Encode(sb.toString(),characterEncoding).toUpperCase();
		System.out.println("小写的签名数据:"+MD5Util.MD5Encode(sb.toString(),characterEncoding));
		System.out.println();                               
		System.out.println();
		System.out.println();
		System.out.println("大写的签名数据:"+sign);
		return sign;
	}
	
	/**
	 * 企业付款时的签名方法
	 * @param characterEncoding
	 * @param parameters
	 * @return {@link String}
	 */
	public static String createWithdrawSign(String characterEncoding, SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		// hetengfuwu
		// key已经变成SUBKEY
		sb.append("key=" + WeiXinConfigParam.SUBKEY);// 最后加密时添加商户密钥，由于key值放在最后，所以不用添加到SortMap里面去，单独处理，编码方式采用UTF-8
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}

	 public static String toXml(SortedMap<String, String> params){
	        StringBuilder buf = new StringBuilder();
	        List<String> keys = new ArrayList<String>(params.keySet());
	        Collections.sort(keys);
	        buf.append("<xml>");
	        for(String key : keys){
	            buf.append("<").append(key).append(">");
	            buf.append("<![CDATA[").append(params.get(key)).append("]]>");
	            buf.append("</").append(key).append(">\n");
	        }
	        buf.append("</xml>");
	        return buf.toString();
	}
	public static String getRequestXml(SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("sign".equalsIgnoreCase(k)) {

			} else if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("<" + "sign" + ">" + "<![CDATA[" + parameters.get("sign") + "]]></" + "sign" + ">");
		sb.append("</xml>");
		return sb.toString();
	}

	public static String withDrawToBank(SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("sign".equalsIgnoreCase(k)) {

			} else if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("<" + "sign" + ">" + "<![CDATA[" + parameters.get("sign") + "]]></" + "sign" + ">");
		sb.append("</xml>");
		return sb.toString();
	}
	
	
	
	
	/**
	 * 获得随机字符串
	 * 
	 * @return
	 */
	public static String getRandomStringByLength(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	/**
	 * 生成订单号
	 * @param length 传-1则默认为32位，其他则看传入值加17位
	 * @return
	 */
	public static String createOrdernum(int length) {
		String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		if (length == -1) {
			length = 32 - format.length();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(format);
		for (int i = 0; i < length; i++) {
			sb.append((int)(Math.random()*9));
		}
		return sb.toString();
	}
	
	/**
	 * 生成订单号
	 * @param length 传-1则默认为32位，其他则看传入值加17位
	 * @return
	 */
	public static String createNewOrdernum(String equipmentnum) {
		String format = CommUtil.toDateTime("yyMMddHHmmss");
		StringBuffer sb = new StringBuffer();
		sb.append(equipmentnum);
		sb.append(format);
		for (int i = 0; i < 4; i++) {
			sb.append((int)(Math.random()*9));
		}
		return sb.toString();
	}
	

	/**
	 * 获取订单号 商户订单号（每个订单号必须唯一） 组成：mch_id+yyyymmdd+10位一天内不能重复的数字。
	 * 
	 * @param number
	 *            用户ID
	 * @return
	 */
	public static String getMchBillno(String number) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(date) + number;
	}

	/**
	 * 获取时间戳
	 * 
	 * @return
	 */
	public static String getTimeStamp() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(date);
	}

	public static void main(String[] args) throws JDOMException,
			IOException {
		/*
		 * SortedMap<String, String> params = new TreeMap();
		 * params.put("appid", "wx25de598dcd2ccec8");
		 * params.put("mch_id", "1445807202");
		 * params.put("nonce_str",
		 * getRandomStringByLength(30));
		 * 
		 * params.put("openid",
		 * "o_rek0tglPCdv4Wtyaia6cbjeuFA");
		 * params.put("out_trade_no", "20150806652314");
		 * 
		 * params.put("out_refund_no", getMchBillno("666"));
		 * params.put("total_fee", "1");
		 * params.put("refund_fee", "1");
		 * 
		 * 
		 * String sign = createSign("UTF-8", params);
		 * params.put("sign", sign); String url =
		 * "https://api.mch.weixin.qq.com/secapi/pay/refund";
		 * String canshu = getRequestXml(params); String sr
		 * = sendPost(url, canshu); System.out.println(sr);
		 * Map<String, String> map = XMLUtil.doXMLParse(sr);
		 * for (String string : map.keySet()) {
		 * System.out.println(string);
		 * System.out.println((String)map.get(string));
		 * }https://api.weixin.qq.com/cgi-bin/token
		 */

//		String ss = sendGet("https://api.weixin.qq.com/cgi-bin/token",
//				"grant_type=client_credential&appid=wx25de598dcd2ccec8&secret=8787578458794f36b547a53b0aa5455a");
//		System.out.println(ss);
//		JSONObject object = JSONObject.parseObject(ss);
//		System.out.println(object.get("access_token").toString());
//		String aa = sendGet("https://api.weixin.qq.com/cgi-bin/user/info", "access_token="
//				+ object.get("access_token").toString() + "&openid=o_rek0hhi_IhDjHSkhq1KqNgXXbg&lang=zh_CN");
//		JSONObject object1 = JSONObject.parseObject(aa);
//		System.out.println(object1.get("nickname").toString());
		System.out.println(getRandomStringByLength(32));

	}
}
