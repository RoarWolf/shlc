package com.hedong.hedongwx.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.hedong.hedongwx.config.AlipayConfig;

public class AlipayUtil {

	public static void main(String[] args) throws AlipayApiException {
		//实例化客户端
		AlipayClient alipayClient = new 	DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, "RSA2");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.create.
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。
		request.setBizContent("{" +
				"\"out_trade_no\":\""+ HttpRequest.createOrdernum(6) +"\"," +
				"\"total_amount\":0.01," +
				"\"subject\":\"Iphone616G\"" +
				"}");
		try {
		    //使用的是execute
		    AlipayTradeCreateResponse response = alipayClient.execute(request);
		    String trade_no = response.getTradeNo();//获取返回的tradeNO。
		    System.out.println(trade_no);
		} catch (AlipayApiException e) {
		    e.printStackTrace();
		}
	}
}
