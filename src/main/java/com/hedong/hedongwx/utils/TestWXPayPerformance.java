package com.hedong.hedongwx.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jdom.JDOMException;

import com.github.wxpay.sdk.WXPay;

public class TestWXPayPerformance {


    private WXPay wxpay;
    private WXPayConfigImpl config;
    private String out_trade_no;
    private String total_fee;

    public TestWXPayPerformance() throws Exception {
        config = WXPayConfigImpl.getInstance();
        wxpay = new WXPay(config);
        total_fee = "200";
        out_trade_no = "20190423175945891";
    }

    /**
     * 退款测试
     * @throws IOException 
     * @throws JDOMException 
     * 
     */
    public void doRefund() throws JDOMException, IOException {
    	SortedMap<String, String> params = new TreeMap<>();
		params.put("appid", WeiXinConfigParam.FUWUAPPID);
		params.put("mch_id", WeiXinConfigParam.MCHID);
//		params.put("sub_appid", WeiXinConfigParam.SMALLAPPID);
		params.put("sub_mch_id", WeiXinConfigParam.SUBMCHID);
		params.put("out_trade_no", out_trade_no);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/orderquery";
		String canshu = HttpRequest.getRequestXml(params);
		String sr = HttpRequest.sendPost(url, canshu);
		Map<String, String> resultMap = XMLUtil.doXMLParse(sr);
		Set<Entry<String,String>> entrySet = resultMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			System.out.println(entry.getKey() + "---" + entry.getValue());
		}
		if (resultMap.get("return_code").toString().equalsIgnoreCase("SUCCESS")) {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("appid", resultMap.get("appid"));
			data.put("mch_id", resultMap.get("mch_id"));
			data.put("sub_appid", resultMap.get("sub_appid"));
			data.put("sub_mch_id", resultMap.get("sub_mch_id"));
			data.put("transaction_id", resultMap.get("transaction_id"));
	        data.put("out_trade_no", out_trade_no);
	        data.put("out_refund_no", "t" + out_trade_no);
	        data.put("total_fee", total_fee);
	        data.put("refund_fee", total_fee);
	        data.put("refund_fee_type", "CNY");

	        try {
	            Map<String, String> r = wxpay.refund(data);
	            System.out.println(r.toString());
	            System.out.println(r.get("result_code"));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}

    }


    public static void main(String[] args) throws Exception {
        System.out.println("--------------->");
        TestWXPayPerformance dodo = new TestWXPayPerformance();

        //dodo.doUnifiedOrder();
        dodo.doRefund();

        // dodo.doMicropayWithPos();

        // dodo.testUnifiedOrderSpeed();
        // dodo.testUnifiedOrderSpeedWithMultiThread();
        // dodo.testRefundSpeedWithMultiThread();
        // dodo.testRefundSpeed();
        // dodo.testRefundSpeed();
        // dodo.testUnifiedOrderSpeed();
        // dodo.testRefundSpeed();
        // dodo.testHelloWorld();
        System.out.println("<---------------"); // wx2016112510573077
        Thread.sleep(5000);
    }
}

