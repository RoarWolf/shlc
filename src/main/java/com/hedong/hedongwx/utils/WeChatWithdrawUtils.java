package com.hedong.hedongwx.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

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

/**
 * @Description: 微信提现
 */
public class WeChatWithdrawUtils {

	private static final Logger log = LoggerFactory.getLogger(WeChatWithdrawUtils.class);
	private byte[] certData;

	public InputStream getCertStream() {
		ByteArrayInputStream certBis;
		certBis = new ByteArrayInputStream(this.certData);
		return certBis;
	}
	
	public WeChatWithdrawUtils() throws Exception {
     	// 1526869951商户证书位置
		String certPath = "/usr/local/cert2/apiclient_cert.p12";
//		String certPath ="D://hedongteyue/apiclient_cert.p12";
		// 1526869951商户证书位置
//		String certPath = "/usr/local/elk/apiclient_cert.p12";
		File file = new File(certPath);
		InputStream certStream = new FileInputStream(file);
		this.certData = new byte[(int) file.length()];
		certStream.read(this.certData);
		certStream.close();
	}

	public static void main(String[] args) {
		try {

			String result = withdrawRequestOnce(new HashMap<>(), 3000, 3000, true);
			Map<String, String> resultMap = XMLUtil.doXMLParse(result);
			log.info("---" + resultMap.toString());

			if (resultMap.containsKey("result_code") && "SUCCESS".equals(resultMap.getOrDefault("result_code", ""))) {
				System.out.println("---成功---");
				System.out.println(result);
			} else {
				System.out.println("---失败---");
				System.out.println(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

		SortedMap<String, String> paraMap = new TreeMap<String, String>();
		paraMap.put("mch_appid", WeiXinConfigParam.APPID);
		paraMap.put("mchid", WeiXinConfigParam.SUBMCHID);
		paraMap.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		paraMap.put("partner_trade_no", params.get("ordernum"));
		paraMap.put("openid", params.get("openid"));// "o5mZ40yBjIqco2NzKc19k9oIBI9o");
		// 校验用户姓名选项 NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
		paraMap.put("check_name", "FORCE_CHECK");
		paraMap.put("re_user_name", params.get("realname"));
		paraMap.put("amount", params.get("money"));// "100");
		// 企业付款操作说明信息。必填。
		paraMap.put("desc", params.get("info"));
		//paraMap.put("spbill_create_ip", params.get("ip")); ZZ
		String sign = HttpRequest.createWithdrawSign("UTF-8", paraMap);
		paraMap.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
		String canshu = HttpRequest.getRequestXml(paraMap);

		BasicHttpClientConnectionManager connManager;
		if (useCert) {
			// 证书
			char[] password = WeiXinConfigParam.SUBMCHID.toCharArray();
			InputStream certStream = new WeChatWithdrawUtils().getCertStream();
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

		HttpPost httpPost = new HttpPost(url);

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs)
				.setConnectTimeout(connectTimeoutMs).build();
		httpPost.setConfig(requestConfig);

		StringEntity postEntity = new StringEntity(canshu, "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + WeiXinConfigParam.SUBMCHID); // TODO:mainly
																					// 很重要，用来检测
																					// sdk
																					// 的使用情况，要不要加上商户信息？
		httpPost.setEntity(postEntity);

		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		return EntityUtils.toString(httpEntity, "UTF-8");
	}
	
	
	
	/**
	 * 提现到银行卡
	 * @param params
	 * @param connectTimeoutMs
	 * @param readTimeoutMs
	 * @param useCert
	 * @return
	 * @throws Exception
	 */
	public static String withdrawToPersonBank(SortedMap<String, String> params, int connectTimeoutMs, int readTimeoutMs,
			boolean useCert) throws Exception {

		SortedMap<String, String> paraMap = new TreeMap<String, String>();
		//----------------获取公钥的参数----------------
		//paraMap.put("mch_id", "1526869951");
		//paraMap.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		//String sign = HttpRequest.createWithdrawSign("UTF-8", paraMap);
		//paraMap.put("sign", sign);
		//String url = "https://fraud.mch.weixin.qq.com/risk/getpublickey";
		//String canshu = HttpRequest.getRequestXml(paraMap);
		//--------------提现到银行卡的参数-----------------
		// 付款到银行卡的API
		String url = "https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank";
		// 解析参数
		String canshu = HttpRequest.withDrawToBank(params);
		BasicHttpClientConnectionManager connManager;
		if (useCert) {
			// 证书
			char[] password = WeiXinConfigParam.SUBMCHID.toCharArray();
			InputStream certStream = new WeChatWithdrawUtils().getCertStream();
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

		HttpPost httpPost = new HttpPost(url);

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs)
				.setConnectTimeout(connectTimeoutMs).build();
		httpPost.setConfig(requestConfig);

		StringEntity postEntity = new StringEntity(canshu, "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + WeiXinConfigParam.SUBMCHID); // TODO:mainly
																					// 很重要，用来检测
																					// sdk
																					// 的使用情况，要不要加上商户信息？
		httpPost.setEntity(postEntity);

		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		return EntityUtils.toString(httpEntity, "UTF-8");
	}
}