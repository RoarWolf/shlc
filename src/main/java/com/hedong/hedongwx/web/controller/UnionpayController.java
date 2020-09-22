package com.hedong.hedongwx.web.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.FeescaleService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.utils.CommUtil;
import com.taosdata.jdbc.TSDBDriver;

@Controller
@RequestMapping("/unionpay")
public class UnionpayController {
    private final static String version = "2.0";
    private final static String charset = "UTF-8";
    private final static String sign_type = "MD5";
	
	private final Logger logger = LoggerFactory.getLogger(UnionpayController.class);
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private FeescaleService feecaleService;
	/**
	 * 银联APP扫码
	 * @param equCode 设备号
	 * @param codeAndPort 设备和端口号
	 * @param model
	 * @return {@link String}
	 */
	@RequestMapping("/userAuth")
	public String userauth(String equCode,String codeAndPort,Model model){
		return equipmentService.unionScan(equCode,codeAndPort, model);
	}
	
	/**
	 * 银联支付
	 * @param req 请求
	 * @param resp 响应
	 * @throws Exception
	 */
	@RequestMapping("/pay")
	public String pay(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String, Object> data = tradeRecordService.createUnioPayOrderNum(req);
		if(data.get("code").equals(200)){
			String url = CommUtil.toString(data.get("result"));
			return "redirect:" + url;
		}else if(data.get("code").equals(400)){
			logger.info("错误信息=="+data);
			return "erroruser";
		}
		return "erroruser";
	}
	/**
	 * 银联支付的回调
	 */
	@PostMapping(value = "/unionPayBack")
	@ResponseBody
	public String unionPayBack(HttpServletRequest request){
		String result = tradeRecordService.unionPayBack(request);
		logger.info("银联支付的回调返回的信息===="+result);
		return result;
	}
	/**
	 * 银联退款申请
	 * @return {@link String}
	 */
	@GetMapping("/doRefund")
	@ResponseBody
	public Map<String, Object> doRefund(HttpServletRequest request) {
		Map<String, Object> result = tradeRecordService.unionDoRefond(request);
		logger.info("银联退款申请返回的信息=="+result);
		return result;
	}
	/**
	 * 银联手机退款申请
	 * @return {@link String}
	 */
	@PostMapping("/doRefund")
	@ResponseBody
	public Map<String, Object> phoneDoRefund(HttpServletRequest request) {
		Map<String, Object> result = tradeRecordService.unionDoRefond(request);
		logger.info("银联手机退款申请返回的信息=="+result);
		return result;
	}
	
//	@RequestMapping("/test")
//	public Object userAuth(String equCode,Model model){
//		String respCode = request.getParameter("respCode");
//		logger.info("银联应答码"+"-------"+respCode);
//		
//		
//		
//		if(equCode == null || "".equals(equCode)) return "erroruser";
//        //00 – 成功、34 – 不支持获取用户信息、其它 – 失败
//		if ("00".equals(respCode)) {
//			// 根据银联的应答码和授权码获取用户唯一标识
//			String userAuthCode = request.getParameter("userAuthCode");
//			SortedMap<String, String> params = new TreeMap<>();
//			params.put("service", "pay.unionpay.userid");
//			params.put("sign_type", "MD5");
//			params.put("mch_id", WeiXinConfigParam.UNIONPAYMERCHID);
//			params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
//			params.put("user_auth_code", userAuthCode);
//			logger.info("用户授权码"+"-------"+userAuthCode);
//			params.put("app_up_identifier", "UnionPay/1.0 CloudPay");
//			// 生成签名并放入map集合里
//			Map<String,String> data = SignUtils.sendPostToUnionpay(params);
//			if("0".equals(data.get("status")) && "0".equals(data.get("result_code"))){
//			// 银联用户唯一标识
//				String unionUserId = data.get("user_id");
//				// 查询设备信息
//				Equipment equipment = equipmentService.getEquipmentAndAreaById(equCode);
//				if(equipment == null) return "erroruser";
//				Date expirationTime= equipment.getExpirationTime();
//				Integer deviceState = CommUtil.toInteger(equipment.getState());
//				Integer bindType = CommUtil.toInteger(equipment.getBindtype());
//				//判断设备状态是否正常和是否绑定
//				if(deviceState.equals(0) || bindType.equals(0)){
//					return "/equipment/equipmentoffline";
//				} 
//				//设备到期时间不为空且当前时间大于到期时间
//				if(expirationTime != null && new Date().after(expirationTime)){
//					model.addAttribute("errorinfo", "对不起，当前设备已到期");
//					model.addAttribute("time", expirationTime);
//					//提示用户设备到期更换设备
//					return "chargeporterror";
//				}
//				// 获取设备模板的信息
//				Integer tId = CommUtil.toInteger(equipment.getTempid());
//				// 根据设备号查询商户信息
//				UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(equCode);
//				if(userEquipment == null || userEquipment.getUserId() == null) return "erroruser";
//				// 设备所属商家
//				Integer dealId = CommUtil.toInteger(userEquipment.getUserId());
//				// 查询商家
//				User user = userService.selectUserById(dealId);
//				if(user == null) return "erroruser";
//				// 各种电话
//				String dealerPhone = CommUtil.toString(user.getPhoneNum());
//				String servePhone = CommUtil.toString(user.getServephone());
//				String hardVersion = CommUtil.toString(equipment.getHardversion());
//				// 各种模板
//				Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tId, dealId, equCode, hardVersion);
//				String brandName = CommUtil.toString(DirectTemp.get("remark"));
//				Integer nowTempId = CommUtil.toInteger(DirectTemp.get("id"));
//				String temphone = CommUtil.toString(DirectTemp.get("common1"));
//				servePhone = basicsService.getServephoneData(temphone, null, servePhone, dealerPhone);
//				// 属于设备的模板
//				List<TemplateSon> tempson = templateService.getSonTemplateLists(nowTempId);
//				tempson = tempson == null ? new ArrayList<>() : tempson;
//				if("03".equals(hardVersion)){
//					SendMsgUtil.send_0x82(equCode);
//					if(tempson.size()>=2){
//						model.addAttribute("defaultTemp", tempson.get(1).getId());
//					}else{
//						model.addAttribute("defaultTemp", tempson.get(0).getId());
//					}
//					model.addAttribute("brandname", brandName);
//					model.addAttribute("phonenum", servePhone);
//					model.addAttribute("code",equCode);
//					model.addAttribute("templatelist", tempson);
//					model.addAttribute("userid",userId);
//					model.addAttribute("merUserId", dealId);
//					return "unionpay/inCoins";
//				}else{
//					return "erroruser";
//				}
//			}else{
//				return "erroruser";
//			}
//		}else{
//			return "erroruser";
//		}
//	}
//	
//	
//	
//	
//	/**
//	 * 查询订单测试
//	 * @param req
//	 * @param resp
//	 * @throws Exception
//	 */
//	@RequestMapping("/query")
//	public void query(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		SortedMap<String, String> params = new TreeMap<>();
//		params.put("service", "unified.trade.query");
//		params.put("version", version);
//		params.put("charset", charset);
//        params.put("sign_type", sign_type);
//        params.put("mch_id", "QRA507157343N6D");
//        params.put("nonce_str", HttpRequest.createOrdernum(10));
//		// 实例化对象
//		StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
//	    // 创建支付参数
//		SignUtils.buildPayParams(buf, params, false);
//	    String preStr = buf.toString();
//	    // MD5对参数加密生成签名
//	    String sign = MD5.sign(preStr, "&key=" + WeiXinConfigParam.UNIONKEY, "utf-8");
//	    params.put("sign", sign);//gateway.test.95516.com/gateway/api/order.do
//		// 银联JS支付接口
//		String url = "https://qra.95516.com/pay/gateway";
//		logger.info("请求的地址：" + url);
//		logger.info("生成的签名：" +sign);
//		logger.info("请求的参数:" + XmlUtils.parseXML(params));
//		String res = null;
//		// 创建接收返回的参数
//	    CloseableHttpClient client =  null;
//	    CloseableHttpResponse response = null;
//	    Map<String, String> resultMap = null;
//		try {
//			// 发送POST
//			HttpPost httpPost = new HttpPost(url);
//	        // 解析请求内容
//			StringEntity entityParams = new StringEntity(XmlUtils.parseXML(params), "utf-8");
//			// 设置请求体
//			httpPost.setEntity(entityParams);
//	        // 设置请求头
//	        httpPost.setHeader("Content-Type", "text/xml;utf-8");
//	        // 接收返回的参数
//	        client = HttpClients.createDefault();
//	        client.execute(httpPost);
//	        response = client.execute(httpPost);
//	        // 判断返回的参数
//	        if (response != null && response.getEntity() != null) {
//	        	resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
//	        	res  = XmlUtils.toXml(resultMap);
//	            logger.info("请求结果：" + res);
//	            if (!SignUtils.checkParam(resultMap, WeiXinConfigParam.UNIONKEY)) {
//	                res = "验证签名不通过";
//	                logger.info("验证签名不通过");
//	            } else {
//            	 if("0".equals(resultMap.get("status"))){
//                        if("0".equals(resultMap.get("result_code"))){
//                        	logger.debug("业务成功，在这里做相应的逻辑处理");
//                            String trade_state = resultMap.get("trade_state");
//                            logger.debug("trade_state : " + trade_state);
//                            logger.debug("这里商户需要同步自己的订单状态。。。");
//                        }else{
//                        	logger.debug("业务失败，尝试重新请求，并查看错误代码描叙");
//                        }
//                    }else{
//                    	logger.debug("这里是请求参数有问题...");
//                    }
//	            }
//	        } else {
//	        	logger.info("操作失败");
//	            res = "操作失败";
//	        }
//			
//		} catch (Exception e) {
//			logger.error("操作失败，原因：",e);
//            res = "系统异常";
//		}finally {
//            if (response != null) {
//                response.close();
//            }
//            if (client != null) {
//                client.close();
//            }
//        }
//        Map<String,String> result = new HashMap<String,String>();
//        if("ok".equals(res)){
//        	logger.info("OK="+res);
//            result = resultMap;
//        }else{
//        	logger.info("status="+"500");
//            result.put("status", "500");
//            result.put("msg", res);
//        }
//        resp.getWriter().write(JSON.toJSONString(resultMap));
//		//return JSON.toJSONString(map);
//	}
//	
//	/**
//	 * PC端首页数据展示
//	 * @param req
//	 * @return {@link Map}
//	 */
//	
	@PostMapping("/index")
	@ResponseBody
	public Map<String,Object> testPCIndex(HttpServletRequest req){
		  try {
			  Class.forName("com.taosdata.jdbc.TSDBDriver");
			  String jdbcUrl = "jdbc:TAOS://localhost:6030/log?user=root&password=taosdata";
			  Properties connProps = new Properties();
			  connProps.setProperty(TSDBDriver.PROPERTY_KEY_USER, "root");
			  connProps.setProperty(TSDBDriver.PROPERTY_KEY_PASSWORD, "taosdata");
			  connProps.setProperty(TSDBDriver.PROPERTY_KEY_CONFIG_DIR, "/etc/taos");
			  connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
			  connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
			  connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
			  Connection conn = DriverManager.getConnection(jdbcUrl, connProps);
			  Map<String, Object> map =  new HashMap<>();
			  map.put("200", "OK");
			  return map;
		} catch (Exception e) {
			  Map<String, Object> map =  new HashMap<>();
			  map.put("400", e);
			  return map;
		}
	}
}
