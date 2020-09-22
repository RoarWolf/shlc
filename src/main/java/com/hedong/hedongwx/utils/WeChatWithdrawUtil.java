package com.hedong.hedongwx.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeChatWithdrawUtil {

	private static final Logger log = LoggerFactory.getLogger(WeChatWithdrawUtil.class.getName());
	private byte[] certData;

	public static void main(String[] args) throws Exception {
		String format = "425" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("openid", "openid");
		hashMap.put("ordernum", format);
		hashMap.put("realname", "温**");
		hashMap.put("money", "100");
		hashMap.put("info", format);
		hashMap.put("ip", "127.0.0.1");
		String withdrawRequestOnce = withdrawRequestOnce(hashMap, 3000, 3000, true);
		Map doXMLParse = XMLUtil.doXMLParse(withdrawRequestOnce);
		if (doXMLParse.containsKey("result_code") && "SUCCESS".equals(doXMLParse.getOrDefault("result_code", ""))) {
			System.out.println("提现成功");
			System.out.println("返回结果：" + withdrawRequestOnce);
		} else {
			System.out.println("提现成功");
			System.out.println("返回结果：" + withdrawRequestOnce);
		}
	}

	/**
	 * 加载证书
	 */
	public InputStream getCertStream() {
		ByteArrayInputStream certBis;
		certBis = new ByteArrayInputStream(this.certData);
		return certBis;
	}

	/**
	 * 读取证书
	 */
	public WeChatWithdrawUtil() throws Exception {
		String certPath ="/usr/local/cert2/apiclient_cert.p12";
		//String certPath = "D://hetengserver/apiclient_cert.p12";
		File file = new File(certPath);
		InputStream certStream = new FileInputStream(file);
		this.certData = new byte[(int) file.length()];
		certStream.read(this.certData);
		certStream.close();
	}

	/**
	 *
	 * 提现 请求，只请求一次，不做重试
	 * 
	 * @param connectTimeoutMs
	 * @param readTimeoutMs
	 * @return
	 * @throws Exception
	 */
	public static String withdrawRequestOnce(Map<String, String> params, int connectTimeoutMs, int readTimeoutMs,
			boolean useCert) throws Exception {
		/** 1.拼凑企业支付需要的参数 **/
		SortedMap<String, String> paraMap = new TreeMap<>();
		paraMap.putAll(params);
		// 微信公众号的appid
		paraMap.put("mch_appid", WeiXinConfigParam.APPID);
		// 商户号
		paraMap.put("mchid", WeiXinConfigParam.SUBMCHID);
		// 随机字符串
		paraMap.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		paraMap.put("partner_trade_no", params.get("ordernum"));
		paraMap.put("openid", params.get("openid"));// "o5mZ40yBjIqco2NzKc19k9oIBI9o");
		// 校验用户姓名选项 NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
		paraMap.put("check_name", "FORCE_CHECK");
		paraMap.put("re_user_name", params.get("realname"));
		paraMap.put("amount", params.get("money"));// "100");
		// 企业付款操作说明信息。必填。
		paraMap.put("desc", params.get("info"));
		// ip地址，地址可以不是真实地址
		paraMap.put("spbill_create_ip", "127.0.01");
		paraMap.put("key", WeiXinConfigParam.KEY);

		/** 3.用MD5加密生成签名 **/
		// String sign = "";
		String sign = HttpRequest.createSign("UTF-8", paraMap);

		/** 4.将map拼接成xml格式 **/
		StringBuffer xml = new StringBuffer();
		xml.append("<xml>");
		for (Map.Entry<String, String> entry : paraMap.entrySet()) {
			xml.append("<" + entry.getKey() + ">");
			xml.append(entry.getValue());
			xml.append("</" + entry.getKey() + ">" + "\n");
		}
		xml.append("<sign>");
		xml.append(sign);
		xml.append("</sign>");
		xml.append("</xml>");

		log.info("xml {} ", xml.toString());

		BasicHttpClientConnectionManager connManager;
		/** 5.操作证书 **/
		if (useCert) {
			// 证书
			char[] password = WeiXinConfigParam.SUBMCHID.toCharArray();
			// 加载证书
			InputStream certStream = new WeChatWithdrawUtil().getCertStream();
			KeyStore ks = KeyStore.getInstance("PKCS12");
			ks.load(certStream, password);

			// 实例化密钥库 & 初始化密钥工厂
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, password);

			// 创建 SSLContext
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
					new String[] { "TLSv1" }, null, new org.apache.http.conn.ssl.DefaultHostnameVerifier());

			connManager = new BasicHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", sslConnectionSocketFactory).build(), null, null, null);
		} else {
			connManager = new BasicHttpClientConnectionManager(
					RegistryBuilder.<ConnectionSocketFactory> create()
							.register("http", PlainConnectionSocketFactory.getSocketFactory())
							.register("https", SSLConnectionSocketFactory.getSocketFactory()).build(),
					null, null, null);
		}

		org.apache.http.client.HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connManager)
				.build();

		/** 发送信息到微信服务器 **/
		HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers");

		/** 设置超时时间 **/
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs)
				.setConnectTimeout(connectTimeoutMs).build();
		httpPost.setConfig(requestConfig);

		StringEntity postEntity = new StringEntity(xml.toString(), "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + WeiXinConfigParam.SUBMCHID); // TODO 很重要，用来检测 sdk的使用情况，要不要加上商户信息？
		httpPost.setEntity(postEntity);

		/** 发送后返回支付结果，xml格式 **/
		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		return EntityUtils.toString(httpEntity, "UTF-8");
	}

	/**
	 * 方法用途: 对所有传入参数按照字段名的Unicode码从小到大排序（字典序），并且生成url参数串<br>
	 * 实现步骤: <br>
	 *
	 * @param paraMap
	 *            要排序的Map对象
	 * @param urlEncode
	 *            是否需要URLENCODE
	 * @param keyToLower
	 *            是否需要将Key转换为全小写 true:key转化成小写，false:不转化
	 * @return
	 */
	public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) {
		String buff = "";
		Map<String, String> tmpMap = paraMap;
		try {
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
			// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
				@Override
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});
			// 构造URL 键值对的格式
			StringBuilder buf = new StringBuilder();
			for (Map.Entry<String, String> item : infoIds) {
				if (StringUtils.isNotBlank(item.getKey())) {
					String key = item.getKey();
					String val = item.getValue();
					if (urlEncode) {
						val = URLEncoder.encode(val, "utf-8");
					}
					if (keyToLower) {
						buf.append(key.toLowerCase() + "=" + val);
					} else {
						buf.append(key + "=" + val);
					}
					buf.append("&");
				}

			}
			buff = buf.toString();
			if (buff.isEmpty() == false) {
				buff = buff.substring(0, buff.length() - 1);
			}
		} catch (Exception e) {
			return null;
		}
		return buff;
	}
}