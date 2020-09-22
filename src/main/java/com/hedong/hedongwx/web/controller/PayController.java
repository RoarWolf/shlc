package com.hedong.hedongwx.web.controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.WXPayConfigImpl;
import com.hedong.hedongwx.utils.XMLUtil;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxService;

@Controller
@RequestMapping({ "/pay" })
public class PayController {
	IService iService = new WxService();
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	@RequestMapping({ "/pay" })
	public String unifiedOrder(Model model) throws Exception {
		// RealNameEntity realNameEntity = (RealNameEntity)
		// this.request.getSession().getAttribute("realName");

		System.out.println("用户地址" + this.request.getRemoteAddr());
		SortedMap<String, String> params = new TreeMap();
		params.put("appid", "wx25de598dcd2ccec8");
		params.put("body", "money-money");
		params.put("mch_id", "1445807202");
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		params.put("notify_url", "http://xgjd.yanzhi.cc/spring-boot-hibernate5/pay/payBack");

		// params.put("openid", realNameEntity.getWxcode());
		// 商户订单号码
		String str = (String) request.getSession().getAttribute("ordernum");
		params.put("out_trade_no", str);

		params.put("spbill_create_ip", this.request.getRemoteAddr());

		Double strNum = (Double) this.request.getSession().getAttribute("payNum");
		strNum = strNum * 100;
		System.out.println(strNum + "***************************");
		String money = String.valueOf(strNum);
		int idx = money.lastIndexOf(".");
		String total_fee = money.substring(0, idx);

		params.put("total_fee", total_fee);

		params.put("trade_type", "JSAPI");
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String canshu = HttpRequest.getRequestXml(params);
		String sr = HttpRequest.sendPost(url, canshu);
		System.out.println(sr);
		Map<String, String> map = XMLUtil.doXMLParse(sr);
		for (String string : map.keySet()) {
			System.out.println(string);
			System.out.println((String) map.get(string));
		}
		SortedMap<String, String> seconde = new TreeMap();
		seconde.put("appId", "wx25de598dcd2ccec8");
		String time = HttpRequest.getTimeStamp();
		seconde.put("timeStamp", time);
		String sjzf = HttpRequest.getRandomStringByLength(30);

		seconde.put("nonceStr", sjzf);
		seconde.put("package", "prepay_id=" + (String) map.get("prepay_id"));
		seconde.put("signType", "MD5");

		String sign2 = HttpRequest.createSign("UTF-8", seconde);

		model.addAttribute("prepay_id", map.get("prepay_id"));
		model.addAttribute("date", time);
		model.addAttribute("paySign", sign2);
		model.addAttribute("packagess", "prepay_id=" + (String) map.get("prepay_id"));
		model.addAttribute("nonceStr", sjzf);
		return "pay3.jsp";
	}

	@RequestMapping({ "/payBack" })
	@ResponseBody
	public String payBack() throws JDOMException {

		String resXml = "";
		Map<String, String> backxml = new HashMap<String, String>();

		InputStream inStream;
		try {
			inStream = request.getInputStream();

			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			// logger.error("微信支付----付款成功----");
			outSteam.close();
			inStream.close();
			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
			// logger.error("微信支付----result----=" + result);
			// Map<Object, Object> map = Xmlunit.xml2map(result, false);
			Map<Object, Object> map = XMLUtil.doXMLParse(result);

			if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
				// logger.error("微信支付----返回成功");
				// ------------------------------
				// 处理业务完毕
				// ------------------------------
				System.out.println(map.get("out_trade_no").toString() + "*****************");

				// getOrderService.updPay(map.get("out_trade_no").toString(),
				// new Date());

				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				out.write(resXml.getBytes());
				out.flush();
				out.close();
			}
			// else {
			// logger.info("支付失败,错误信息：" + map.get("err_code"));
			// resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
			// + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			// }
		} catch (IOException e) {
			// logger.error("支付回调发布异常：" + e);
			e.printStackTrace();
		}
		return resXml;
	}

	@RequestMapping({ "/doRefund" })
	@ResponseBody
	public Object doRefund(Integer id, String info) throws Exception {

		Map<String, Object> map = new HashMap<>();

		WXPayConfigImpl config = WXPayConfigImpl.getInstance();
		WXPay wxpay = new WXPay(config);

		// GetOrder getOrder = getOrderService.findById(id);
		// Double price = getOrder.getPrice();
		// price = price * 100;

		// String money = String.valueOf(price);
		// int idx = money.lastIndexOf(".");
		// String total_fee = money.substring(0, idx);

		// String total_fee = money.substring(0, idx);

		// --------------------------
		// List<GetOrder> findByOrderNum =
		// getOrderService.findByOrderNum(getOrder.getOrdernum());

		// double size = (double) findByOrderNum.size();
		// double total_pri = price * size;
		// String money2 = String.valueOf(total_pri);
		// int idx2 = money.lastIndexOf(".");
		// String total_fee = money2.substring(0, idx2);
		// --------------------------

		// String out_trade_no =orders.getPayNumber();//退款订单号
		// String out_trade_no = getOrder.getOrdernum();
		HashMap<String, String> data = new HashMap<String, String>();
		// data.put("out_trade_no", out_trade_no);// 定单号
		// data.put("out_refund_no", "t" + out_trade_no);
		// data.put("total_fee", total_fee);
		// data.put("refund_fee", total_fee);
		data.put("refund_fee_type", "CNY");
		data.put("op_user_id", config.getMchID());

		try {
			Map<String, String> r = wxpay.refund(data);
			System.out.println("***************" + r);
			System.out.println(r.get("result_code"));
			// 处理退款后的订单 成功
			if ("SUCCESS".equals(r.get("result_code"))) {

				if (info != null && info.equals("tuikuan")) {
					// getOrder.setType((short) 8);
					// getOrder.setRefundlTime(new Date());
				}
				if (info != null && info.equals("quxiao")) {
					// getOrder.setType((short) 7);
					// getOrder.setCancelTime(new Date());
				}

				// getOrderService.saveAndFlush(getOrder);
				map.put("ok", "ok");
			}
			if ("FAIL".equals(r.get("result_code"))) {

				map.put("ok", "error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("退款失败");
			map.put("ok", "error");
		}

		return JSON.toJSON(map);
	}

	public static void main(String[] args) {
		double a = 2.0;
		a = a * 100;
		String money = String.valueOf(a);
		int idx = money.lastIndexOf(".");
		String strNum = money.substring(0, idx);
		System.out.println(strNum);

	}
}
