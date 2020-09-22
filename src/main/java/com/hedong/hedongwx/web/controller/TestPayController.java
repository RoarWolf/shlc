package com.hedong.hedongwx.web.controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.InCoins;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.InfluxDbUtil;
import com.hedong.hedongwx.utils.RSAUtil;
import com.hedong.hedongwx.utils.WXPayConfigImpl2;
import com.hedong.hedongwx.utils.WeChatWithdrawUtils;
import com.hedong.hedongwx.utils.WeiXinConfigParam;
import com.hedong.hedongwx.utils.XMLUtil;

@Controller
@RequestMapping("/testpay")
public class TestPayController {
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private UserService userService;
	@Autowired
	private InCoinsService inCoinsService;
	private final Logger logger = LoggerFactory.getLogger(TestPayController.class);
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private MerchantDetailService merchantDetailService;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private TemplateService templateService;

	@RequestMapping(value = "testpay")
	public String testpay() {
//		serverService.moneyIn(money);
		String code = request.getParameter("code");
		request.getSession().setAttribute("code", code);
		return "testpay";
	}
	
	@RequestMapping(value = "/pay")
	@ResponseBody
	public Map<String, Object> unifiedOrder( Model model) throws Exception {
		String code = request.getParameter("code");
		String port = request.getParameter("port");
		String openid = request.getParameter("openId");
		String tempid = request.getParameter("tempid");
		TemplateSon templateSon = templateService.getInfoTemplateOne(Integer.parseInt(tempid));
		Double tempmoney = templateSon.getMoney();
		Double remark = templateSon.getRemark();
		String format = HttpRequest.createOrdernum(6);
		User user = userService.getUserByOpenidApplet(openid);
		Integer uid = user == null ? 0 : user.getId();
		inCoinsService.insertInCoinsRecord(format, code, Byte.parseByte(port), uid, tempmoney,
				(Byte.parseByte(WxPayController.doubleToString(remark))), (byte) 8, (byte) 0);
		SortedMap<String, String> params = new TreeMap<>();
		params.put("appid", WeiXinConfigParam.FUWUAPPID);
		params.put("body", "自助充电平台");
		params.put("mch_id", WeiXinConfigParam.MCHID);
		params.put("sub_appid", WeiXinConfigParam.SMALLAPPID);
		params.put("sub_mch_id", WeiXinConfigParam.SUBMCHID);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		params.put("notify_url", CommonConfig.ZIZHUCHARGE+"/testpay/payback");
		params.put("sub_openid", openid);
		params.put("out_trade_no", format);
		String ipAddr = getIpAddress(request);
		params.put("spbill_create_ip", ipAddr);
		String money = String.valueOf(tempmoney * 100);
		int idx = money.lastIndexOf(".");
		String total_fee = money.substring(0, idx);
		params.put("total_fee", total_fee);
		params.put("trade_type", "JSAPI");
		System.out.println("---" + params.get("out_trade_no"));

		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String canshu = HttpRequest.getRequestXml(params);
		String sr = HttpRequest.sendPost(url, canshu);
		Map<String, String> map = XMLUtil.doXMLParse(sr);
		Set<Entry<String,String>> entrySet = map.entrySet();
		for (Entry<String, String> entry : entrySet) {
			System.out.println(entry.getKey() + "---" + entry.getValue());
		}
		SortedMap<String, String> seconde = new TreeMap<>();
		seconde.put("appId", WeiXinConfigParam.SMALLAPPID);
		String time = HttpRequest.getTimeStamp();
		seconde.put("timeStamp", time);
		String sjzf = HttpRequest.getRandomStringByLength(30);

		seconde.put("nonceStr", sjzf);
		seconde.put("package", "prepay_id=" + (String) map.get("prepay_id"));
		seconde.put("signType", "MD5");

		String sign2 = HttpRequest.createSign("UTF-8", seconde);
		

		Map<String, Object> paramMap = new HashMap<>();

		paramMap.put("appId", WeiXinConfigParam.SMALLAPPID);
		paramMap.put("prepay_id", map.get("prepay_id"));
		paramMap.put("timeStamp", time);
		paramMap.put("paySign", sign2);
		paramMap.put("packagess", "prepay_id=" + (String) map.get("prepay_id"));
		paramMap.put("nonceStr", sjzf);
		paramMap.put("signType", "MD5");
		paramMap.put("out_trade_no", params.get("out_trade_no"));
		return paramMap;
	}
	
	@RequestMapping({ "/payback" })
	@ResponseBody
	public String payback() throws JDOMException {

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
				String ordernum = map.get("out_trade_no").toString();
				InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
//				String money = String.valueOf(inCoins.getMoney());
//				int idx = money.lastIndexOf(".");
//				String total_fee = money.substring(0, idx);
//				try {
//					SendMsgUtil.send_0x83(inCoins.getEquipmentnum(), inCoins.getPort(), Byte.parseByte(total_fee));
//				} catch (Exception e) {
//					logger.warn(
//							inCoins.getOrdernum() + "：投币充电异常！" + e.getMessage() + e.getStackTrace()[0].getLineNumber());
//				}
				inCoinsService.updateInCoinsStatusAndRecycletype(ordernum, (byte) 1);
				if (inCoins.getStatus() == null || inCoins.getStatus() == 0) {
					UserEquipment userEquipment = userEquipmentService.getDivideintoByCode(inCoins.getEquipmentnum());
					//UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(inCoins.getEquipmentnum());
					if (userEquipment != null) {
						Integer manid = userEquipment.getManid();
						Double divideinto  = userEquipment.getDivideinto();
						User user = userService.selectUserById(userEquipment.getUserId());
						
						Double consume = inCoins.getMoney();
						if( null==manid || manid==0){
							user.setEarnings((user.getEarnings() * 100 + inCoins.getMoney() * 100) / 100);
							userService.updateUserById(user);
							System.out.println("---修改成功---");
							merchantDetailService.insertMerEarningDetail(user.getId(), inCoins.getMoney(),
									user.getEarnings(), ordernum, new Date(), MerchantDetail.INCOINSSOURCE,
									MerchantDetail.WEIXINPAY, MerchantDetail.NORMAL);
							tradeRecordService.insertTrade(user.getId(), inCoins.getUid(), inCoins.getOrdernum(),
									inCoins.getMoney(), inCoins.getMoney(), inCoins.getEquipmentnum(), 2, 4, 1,
									equipmentService.getEquipmentById(inCoins.getEquipmentnum()).getHardversion());
						}else{
							User manuser = userService.selectUserById(userEquipment.getManid());
							Double manmoney = consume * (divideinto*100)/100;
							manuser.setEarnings((manuser.getEarnings() * 100 + manmoney * 100) / 100);
							userService.updateUserById(manuser);
							System.out.println("---合伙人分成修改成功---");
							if(user.getId()==manuser.getId()) user = userService.selectUserById(userEquipment.getUserId());
							user.setEarnings((user.getEarnings() * 100 + (consume - manmoney) * 100) / 100);
							userService.updateUserById(user);
							System.out.println("---修改成功---");
							merchantDetailService.insertMerEarningDetail(user.getId(), (consume - manmoney), user.getEarnings(), 
									ordernum, new Date(), MerchantDetail.INCOINSSOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.NORMAL);
							merchantDetailService.insertMerEarningDetail(manuser.getId(), manmoney, manuser.getEarnings(), 
									ordernum, new Date(), MerchantDetail.INCOINSSOURCE, MerchantDetail.WEIXINPAY, MerchantDetail.NORMAL);
							tradeRecordService.insertToTrade(user.getId(), manuser.getId(), inCoins.getUid(), inCoins.getOrdernum(),
									inCoins.getMoney(), (consume - manmoney), manmoney, inCoins.getEquipmentnum(), 2, 4, 1,
									equipmentService.getEquipmentById(inCoins.getEquipmentnum()).getHardversion());
						}
					}
				}
				
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
	
	/**
	 * get ipaddr
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
		return ip;
	}
	
	/**
	 * 时序数据库写入测试
	 * @param cId 充电id
	 * @param uId 用户id
	 * @param mId 商家id
	 * @param code 设备号
	 * @param type 类型
	 * @param port 端口
	 * @param cTime 时间
	 * @param elec 剩余电量
	 * @param power 功率
	 * @param money 余额
	 * @return {@link String}
	 */
	@RequestMapping("/influxdbTest")
	@ResponseBody
	public String influxDBTest(Integer cId,Integer uId,
			Integer mId,String code,Integer type,Integer port,
			Integer cTime,Integer elec,Integer power,double money){
		InfluxDbUtil dbUtil = new InfluxDbUtil();
		dbUtil.insertRealData(cId, uId, mId, code, port, type, cTime, elec, power, money);
		return "";
	}
	
	/**
	 * 持续改变用户的收益
	 * @param merid 商家的id
	 * @param money 
	 */
	@RequestMapping("/addEarn")
	public void add(Integer merid,Double money){
		userService.updateUserEarnings(1, money, merid);
	}
	 /**
	 * 获取公钥
	 * @return {@link String}
	 * @throws Exception
	 */
	@RequestMapping("/getPublicKey")
	@ResponseBody
	public String getPublicKey() throws Exception{
		String result = WeChatWithdrawUtils.withdrawToPersonBank(null, 3000, 30000, true);
		System.out.println("微信公钥文件====="+result);
		return JSON.toJSONString(result);
	}
	 /**
		 * 获取公钥
		 * @return {@link String}
		 * @throws Exception
		 */
		@RequestMapping("/getPublicKeyA")
		@ResponseBody
		public String getPublicKeyA() throws Exception{
			String publicKey = null;
			SortedMap<String, String> paraMap = new TreeMap<String, String>();
			/*paraMap.put("nonce_str", "257d7niwicpzvtnjs871vpy0g1ddoz");
			paraMap.put("mch_id", "1526869951");
			paraMap.put("partner_trade_no", "425202006281508381635039");
			paraMap.put("sign", "A7E2292B6C56A259800FB7728B0201B4");
			paraMap.put("enc_bank_no", "XDS7xM3R+VprinTFzh5nysGiXH2maSq98QUgUunEzHa80UMSUrClb57WSK+03/acR9Hh4X0chdVqUlavySnCmFwZAgvJhTGIdq85/BNVG6b+bliv1HhAUufQCcmDf9VWZzi09Hl7N35lcNm+TOWVaHDqoj0lDDnzqt+TNFlD0Qm9hRWInsX6MzghL2sYVbj7dajBJ0VYidE89muaMi4cw1L07G5gQW9jIFthXyBBNs/oQzgbQktvqdRN4RTVePQJzkF1V1sFMl2O4F5lGMjvDd2uky8hjct0577fyizkiCbXqjyqkBp7c1qY3FKcZaGD0eA05Z9IipXq3SUFqBEKNA==");
			paraMap.put("enc_true_name","JlrSAJqsjlgKvdQBrIjcANYuN3e/a+4Y+B93oz5p3x8+728KPa7cxtoSWQB8OFSUrB9hLX96YQbhAGFcv4sg2dfaXKjWPgBeLlMflOkZxcN+iS1J9lnzyFb6W4Ktd3UF21ob7sDvBbnefHAVf6+mSFWE4j7o4whXyWB5oyPIrPtzH8lVYePX/V8l+aZh3uRmoLb8Z4CkW1MkD+0KRa/Z5vCVwy/Qlh/GdfXPgkEYSrKwVX7IMjytTMFtslxYlRoOB6InQ2/tHt3w1a6rhK0yxLVJI/oMsVGr++pTaq7c8vM+Z5/0xbWJpec4A/DyDWGkx+Ufeun00zBqUw6FQE7g3w==");
			paraMap.put("bank_code", "1003");
			paraMap.put("amount", 497+"");*/
			//需要被加密的字符串
			//String encBankAcctName = "小郑"; //加密的银行账户名
			//定义自己公钥的路径
			//String keyfile = "C:/book/pksc8_public.pem"; //
			//RSA工具类提供了，根据加载PKCS8密钥文件的方法
			//PublicKey pub=RSAUtil.getPubKey(keyfile,"RSA"); 
			//rsa是微信付款到银行卡要求我们填充的字符串
			//String rsa ="RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING";
			// 进行加密
			//byte[] estr=RSAUtil.encrypt(encBankAcctName.getBytes(),pub,2048, 11,rsa);   //对银行账号进行base加密
			//String A = Base64.encodeBase64String(estr);
			//测试输出
			//System.out.println(A);
			// 鍟嗘埛鍙�
			paraMap.put("mch_id", "1526869951");
			paraMap.put("nonce_str", HttpRequest.getRandomStringByLength(30));
			String sign = HttpRequest.createSign("UTF-8", paraMap);
			paraMap.put("sign_type", "MD5");
			paraMap.put("sign", sign);
			WXPayConfigImpl2 config = WXPayConfigImpl2.getInstance();
			WXPay wxpay = new WXPay(config);		
			String urlSuffix = "/risk/getpublickey";
			String result = wxpay.requestWithCert(urlSuffix, paraMap, 3000,3000);
			System.out.println("微信返回的公钥==="+result);
			return JSON.toJSONString(result);
//			try {
//				publicKey = WeChatWithdrawUtils.withdrawToPersonBank(paraMap, 3000, 3000, true);
//				Map<String, String> map = XMLUtil.doXMLParse(publicKey);
//				System.out.println("微信返回的数据===="+map);
//				return publicKey;
//			} catch (Exception e) {
//				e.printStackTrace();
//				return publicKey;
//			}
//			SortedMap<String, String> paraMap = new TreeMap<>();
		}
} 
