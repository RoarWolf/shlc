package com.hedong.hedongwx.web.controller.mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.MerAmount;
import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.Privilege;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AllPortStatusService;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.AuthorityService;
import com.hedong.hedongwx.service.BasicsService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.CollectStatisticsService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.FeescaleService;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.service.MoneyService;
import com.hedong.hedongwx.service.OfflineCardService;
import com.hedong.hedongwx.service.OnlineCardRecordService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.StatisticsService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserBankcardService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.service.WithdrawService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.JedisUtils;
import com.hedong.hedongwx.utils.SendMsgUtil;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.WXPayConfigImpl;
import com.hedong.hedongwx.utils.WeiXinConfigParam;
import com.hedong.hedongwx.utils.XMLUtil;

/**
 * @Description： 
 * @author  origin  创建时间：   2020年6月24日 下午5:02:33  
 */
@Controller
@RequestMapping(value = "/mobileMerchant")//手机端普通用户
public class MerchantInfoController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private UserBankcardService userBankcardService;
	@Autowired
	private WithdrawService withdrawService;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private AllPortStatusService allPortStatusService;
	@Autowired
	private InCoinsService inCoinsService;
	@Autowired
	private OfflineCardService offlineCardService; 
	@Autowired
	private MerchantDetailService merchantDetailService;
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private OnlineCardService onlineCardService;
	@Autowired
	private OnlineCardRecordService onlineCardRecordService;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private FeescaleService feescaleService;
	@Autowired
	private CollectStatisticsService collectStatisticsService;
	@Autowired
	private BasicsService basicsService;
	
	

	/**
	 * 微信下载交易账单
	 * @param chargeparam
	 * @param portchoose
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadbill")
	@ResponseBody
	public Map<String, Object> downloadbill(String time, Integer billtype, String appid, String mch_id,
			 String sub_appid, String sub_mch_id, Model model) throws Exception {
		//-------------------------------------------------
		String bill_date = time.replace("-", "");
		String bigintime = time + " 00:00:00";
		String endtime = time + " 23:59:59";
		String bill_type = "ALL";//返回当日所有订单信息（不含充值退款订单）
		billtype = CommUtil.toInteger(billtype);
		Integer status = null;
		if(billtype.equals(1)){//返回当日成功支付的订单（不含充值退款订单）
			bill_type = "SUCCESS";
		}else if(billtype.equals(2)){//返回当日退款订单（不含充值退款订单）
			bill_type = "REFUND";
			status = 2;
		}else if(billtype.equals(3)){//返回当日充值退款订单
			bill_type = "RECHARGE_REFUND";
			status = null;
			bigintime = CommUtil.toDateTime( "yyyy-MM-dd 00:00:00", new Date());
			endtime = CommUtil.toDateTime( "yyyy-MM-dd 23:59:59", new Date());
		}
		//-------------------------------------------------
		if(CommUtil.trimToEmpty(appid).equals("")) appid = WeiXinConfigParam.FUWUAPPID;
		if(CommUtil.trimToEmpty(mch_id).equals("")) mch_id = WeiXinConfigParam.MCHID;
		SortedMap<String, String> params = new TreeMap<>();
		params.put("appid", appid);
		params.put("mch_id", mch_id);
		if(!CommUtil.trimToEmpty(sub_appid).equals("")) params.put("sub_appid", sub_appid);
		if(!CommUtil.trimToEmpty(sub_mch_id).equals("")) params.put("sub_mch_id", sub_mch_id);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		params.put("bill_date", bill_date);
		params.put("bill_type", bill_type);
//		params.put("tar_type", "");
		//------------------------------------------------------
		// 生成签名并放入map集合里
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/downloadbill";
		String canshu = HttpRequest.getRequestXml(params);
		String result = HttpRequest.sendPost(url, canshu);
		
		Map<String, Object> mapinfo = new HashMap<String, Object>();
		if(result.contains("<xml>")){
			Map<String, String> map = XMLUtil.doXMLParse(result);
			String returncode = CommUtil.trimToEmpty(map.get("return_code"));
			String returnmsg = CommUtil.trimToEmpty(map.get("return_msg")).toUpperCase();
			String errorcode = CommUtil.trimToEmpty(map.get("error_code"));
			mapinfo.put("mapinfo", map);
			mapinfo.put("returncode", returncode);
			mapinfo.put("returnmsg", returnmsg);
			mapinfo.put("errorcode", errorcode);

			String news = null;
			if(errorcode.equals("100") || errorcode.equals("20003")){
				news = "下载失败。系统超时;请尝试再次查询。";
			} else if(errorcode.equals("20001")){
				news = "参数不正确。请求参数未按要求进行填写。";
			} else if(errorcode.equals("20002")){
				news = "账单未生成或不存在。当前商户号没有已成交的订单或对账单尚未生成。";
			} else if(errorcode.equals("20007")){
				news = "当前商户号账单API权限已经关闭。";
			} else if(errorcode.equals("20100")){
				news = "下载失败。系统错误。";
			} else{
				news = "下载失败！未知错误。";
			} 
			mapinfo.put("news", news);
			StringBuffer hint = new StringBuffer(returncode);
			hint.append(";").append(errorcode).append(";").append(returnmsg).append(";");
			String hintinfo = null;
			if(returnmsg.equals(("SYSTEMERROR").toUpperCase())){
				hintinfo = "下载失败;系统超时;请尝试再次查询。";
			}else if(returnmsg.equals(("sign error").toUpperCase())){
				hintinfo = "签名错误;请求参数未按要求进行填写;签名错误，请重新检查参数和签名密钥是否正确";
			}else if(returnmsg.equals(("nonce_str too long").toUpperCase())){
				hintinfo = "参数nonce_str错误;请求参数未按要求填写;参数nonce_str长度超长";
			}else if(returnmsg.equals(("invalid tar_type, Only GZIP supported").toUpperCase())){
				hintinfo = "参数tar_type错误;请求参数未按指引进行填写;请重新检查参数invalid tar_typ是否正确";
			}else if(returnmsg.equals(("invalid bill_type").toUpperCase())){
				hintinfo = "参数bill_type错误;请求参数未按指引进行填写;请重新检查参数bill_type是否正确";
			}else if(returnmsg.equals(("invalid bill_date").toUpperCase())){
				hintinfo = "参数bill_date错误;请求参数未按指引进行填写;请重新检查参数bill_date是否符合要求";
			}else if(returnmsg.equals(("require POST method").toUpperCase())){
				hintinfo = "请求方式错误;请求方式不符合要求;请求检查参数请求方式是否为post";
			}else if(returnmsg.equals(("empty post data").toUpperCase())){
				hintinfo = "请求报文错误;请求报文为空;请重新检查请求报文是否正确";
			}else if(returnmsg.equals(("data format error").toUpperCase())){
				hintinfo = "参数格式错误;请求参数要求为xml格式;请重新检查请求参数格式是否为xml";
			}else if(returnmsg.equals(("missing parameter").toUpperCase())){
				hintinfo = "缺少参数;有必传的参数未上传;请重新检查是否所有必传参数都上传了，且不为空";
			}else if(returnmsg.equals(("invalid appid").toUpperCase())){
				hintinfo = "appid错误;请求参数appid有误;请重新检查参数appid是否正确";
			}else if(returnmsg.equals(("invalid parameter").toUpperCase())){
				hintinfo = "参数错误;有未知的请求参数;请重新检查是否所有参数都与文档相符";
			}else if(returnmsg.equals(("No Bill Exist").toUpperCase())){
				hintinfo = "账单不存在;当前商户号没有已成交的订单，不生成对账单;请检查当前商户号在指定日期内是否有成功的交易。";
			}else if(returnmsg.equals(("Bill Creating").toUpperCase())){
				hintinfo = "账单未生成;当前商户号没有已成交的订单或对账单尚未生成;请先检查当前商户号在指定日期内是否有成功的交易，如指定日期有交易则表示账单正在生成中，请在上午10点以后再下载。";
			}else if(returnmsg.equals("当前商户号账单API权限已经关闭")){
				hintinfo = "当前商户号账单API权限已经关闭;当前商户号账单API权限已经关闭;当前商户号账单API权限已经关闭，请联系微信支付解决";
			}else if(returnmsg.equals(("system error").toUpperCase())){
				hintinfo = "下载失败;系统超时;请尝试再次查询。";
			}else{
				hintinfo = "下载失败;未知原因。";
			}
			hint.append(hintinfo);
			mapinfo.put("hint", hint);
			String message = errorcode + hintinfo;
			mapinfo.put("message", message);
			CommUtil.responseBuildInfo(101, message, mapinfo);
		}else{
			String headername = result.substring(0, result.indexOf("`"));//表头数据
			List<String> listheader = Arrays.asList(headername.split(","));
			mapinfo.put("listheader", listheader);
			String tradeMsg = result.substring(result.indexOf("`"));//除表头所有数据
			//获取数据
			String contentInfo = tradeMsg.substring(0, tradeMsg.indexOf("总"));//数据内容
			String[] tradeArray = contentInfo.split("``");//将数据内容  根据``来区分 转换为数组
			List<Map<String, Object>> listcontent = new ArrayList<>();
			List<String> listWeChartOrder = new ArrayList<>();
			for (String tradeDetailInfo : tradeArray) {
				String[] tradeDetailArray = tradeDetailInfo.split(",");
				String WXordernum = CommUtil.toString(tradeDetailArray[6].replace("`", ""));
				listWeChartOrder.add(WXordernum);
				Map<String, Object> mapdata = new HashMap<String, Object>();
				mapdata.put("creattime", CommUtil.toString(tradeDetailArray[0].replace("`", "")));// 交易时间
				mapdata.put("commonId", CommUtil.toString(tradeDetailArray[1].replace("`", "")));// 公众账号ID
				mapdata.put("businessnum", CommUtil.toString(tradeDetailArray[2].replace("`", "")));// 商户号
				mapdata.put("childBusinessnum", CommUtil.toString(tradeDetailArray[3].replace("`", "")));// 子商户号
				mapdata.put("equipmentnum", CommUtil.toString(tradeDetailArray[4].replace("`", "")));// 设备号
				mapdata.put("WxOrderNo", tradeDetailArray[5].replace("`", ""));// 微信订单号
				mapdata.put("BusinessOrderNo", tradeDetailArray[6].replace("`", ""));// 商户订单号
				mapdata.put("UserIdentity", tradeDetailArray[7].replace("`", ""));// 用户标识
				mapdata.put("TransType", tradeDetailArray[8].replace("`", ""));// 交易类型
				mapdata.put("TransStatus", tradeDetailArray[9].replace("`", ""));// 交易状态
				mapdata.put("PaymentBank", tradeDetailArray[10].replace("`", ""));// 付款银行
				mapdata.put("Currency", tradeDetailArray[11].replace("`", ""));// 货币种类
				mapdata.put("TotalAmount", tradeDetailArray[12].replace("`", ""));// 总金额
				mapdata.put("RedEnvelopesAmount", tradeDetailArray[13].replace("`", ""));// 企业红包金额
				mapdata.put("WxRefundNo", tradeDetailArray[14].replace("`", ""));// 微信退款单号
				mapdata.put("BusinessRefundNo", tradeDetailArray[15].replace("`", ""));// 商户退款单号
				mapdata.put("RefundAmount", tradeDetailArray[16].replace("`", ""));// 退款金额
				mapdata.put("RedEnvelopesRefundAmount", tradeDetailArray[17].replace("`", ""));// 企业红包退款金额
				mapdata.put("RefundType", tradeDetailArray[18].replace("`", ""));// 退款类型
				mapdata.put("RefundStatus", tradeDetailArray[19].replace("`", ""));// 退款状态
				mapdata.put("BusinessName", tradeDetailArray[20].replace("`", ""));// 商品名称
				mapdata.put("BusinessData", tradeDetailArray[21].replace("`", ""));// 商户数据包
				mapdata.put("Fee", tradeDetailArray[22].replace("`", ""));// 手续费
				mapdata.put("Rate", tradeDetailArray[23].replace("`", "") + "%");// 费率
				mapdata.put("CreateDate", new Date());
				listcontent.add(mapdata);
			}
			mapinfo.put("listcontent", listcontent);
			mapinfo.put("listWeChartOrder", listWeChartOrder);
			
			List<String> tradOrder = tradeRecordService.getTraderecordOrder(status, bigintime, endtime);
			mapinfo.put("listTradOrder", tradOrder);

			List<String> lostWxOrder = new ArrayList<>();
			
			lostWxOrder.addAll(listWeChartOrder); 
			lostWxOrder.removeAll(tradOrder);
			List<Map<String, Object>> lostcontent = new ArrayList<>();
			for(int i=0; i< lostWxOrder.size(); i++){
				String lowxorder = lostWxOrder.get(i);
				for(int j=0; j< listcontent.size(); j++){
					String wxorder = CommUtil.toString(listcontent.get(j).get("BusinessOrderNo"));
					if(lowxorder.equals(wxorder)) lostcontent.add(listcontent.get(j));
				}
			}
			CommUtil.responseBuildInfo(200, "SUCCESS", mapinfo);
		}
		return mapinfo;
	}	
	
	/**
	 * @author origin
	 * @param ordernum
	 * @param submchid
	 * @param totalfee
	 * @param refundfee
	 * @return
	 */
	@RequestMapping(value = "/lostDoReturn")
	@ResponseBody
	public Object lostDoReturn(Integer id, String ordernum, String submchid, String totalfee, String refundfee){
		Map<String, Object> datamap = new HashMap<>();
		try {
			WXPayConfigImpl config = WXPayConfigImpl.getInstance();
			WXPay wxpay = new WXPay(config);
			// 根据id查询充电信息
			String out_trade_no = CommUtil.trimToEmpty(ordernum);
			String total_fee = CommUtil.trimToEmpty(totalfee);
			String refund_fee = CommUtil.trimToEmpty(refundfee);
			if(CommUtil.trimToEmpty(submchid).equals("")) submchid = WeiXinConfigParam.SUBMCHID;
			//------------------------------------------------------
			// 生成签名并放入map集合里
			SortedMap<String, String> params = new TreeMap<>();
			params.put("appid", WeiXinConfigParam.FUWUAPPID);
			params.put("mch_id", WeiXinConfigParam.MCHID);
			params.put("sub_mch_id", submchid);
			params.put("out_trade_no", out_trade_no);
			params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
			String sign = HttpRequest.createSign("UTF-8", params);
			params.put("sign", sign);
			String url = "https://api.mch.weixin.qq.com/pay/orderquery";
			String canshu = HttpRequest.getRequestXml(params);
			String sr = HttpRequest.sendPost(url, canshu);
			Map<String, String> resultMap = XMLUtil.doXMLParse(sr);
			
			if(total_fee.equals("")) total_fee = CommUtil.toString(resultMap.get("total_fee"));
			if(refund_fee.equals("")) refund_fee = CommUtil.toString(resultMap.get("total_fee"));
			datamap.put("resultMap", resultMap);
			// 退款后的业务处理
			if (resultMap.get("return_code").toString().equalsIgnoreCase("SUCCESS")) {
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("appid", WeiXinConfigParam.FUWUAPPID);
				data.put("mch_id", WeiXinConfigParam.MCHID);
				data.put("sub_mch_id", submchid);
				data.put("transaction_id", resultMap.get("transaction_id"));
				data.put("out_trade_no", out_trade_no);// 定单号
				data.put("out_refund_no", "t" + out_trade_no);
				data.put("total_fee", total_fee);
				data.put("refund_fee", refund_fee);
				data.put("refund_fee_type", "CNY");
				data.put("op_user_id", config.getMchID());
				try {
					Map<String, String> r = wxpay.refund(data);
					datamap.put("resultrefMap", r);
					System.out.println("G退款后的参数"+"========================"+r.toString());
					// 处理退款后的订单 成功
					if ("SUCCESS".equals(r.get("result_code"))) {
						String outrefundno = CommUtil.toString(r.get("out_refund_no"));
						String wxrefundno = CommUtil.toString(r.get("refund_id"));
						String refundfeemoney = CommUtil.toString(r.get("refund_fee"));
						CommUtil.responseBuildInfo(200, "SUCCESS", datamap);
					}else if ("FAIL".equals(r.get("result_code"))) {
						CommUtil.responseBuildInfo(3001, "ERROR,退款失败", datamap);
					}
				} catch (Exception e) {
					e.printStackTrace();
					CommUtil.responseBuildInfo(3002, "ERROR,退款失败", datamap);
				}
			} else {
				CommUtil.responseBuildInfo(3003, "ERROR,未查到该订单", datamap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(3003, "ERROR,服务器发生错误", datamap);
		}
		return datamap;
	}
	
	/**
	 * @Description：查询订单号信息
	 * @author origin
	 * @param ordernum 商户订单号   traordernum:交易订单号（微信订单号）  submchid:(服务商下的)子商户号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/inquireWXOrderData")
	@ResponseBody
	public Map<String, Object> inquireWXOrderData(String ordernum, String traordernum, 
			String submchid) throws Exception {
		//-------------------------------------------------
		//-------------------------------------------------
		if(CommUtil.trimToEmpty(submchid).equals("")) submchid = WeiXinConfigParam.SUBMCHID;
		SortedMap<String, String> params = new TreeMap<>();
		params.put("appid", WeiXinConfigParam.FUWUAPPID);
		params.put("mch_id", WeiXinConfigParam.MCHID);
//		params.put("sub_appid", "");
		params.put("sub_mch_id", submchid);
		if(!CommUtil.trimToEmpty(traordernum).equals(""))params.put("transaction_id", traordernum);
		if(!CommUtil.trimToEmpty(ordernum).equals(""))params.put("out_trade_no", ordernum);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		//------------------------------------------------------
		// 生成签名并放入map集合里
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/orderquery";
		String canshu = HttpRequest.getRequestXml(params);
		String result = HttpRequest.sendPost(url, canshu);
		Map<String, String> map = XMLUtil.doXMLParse(result);
		String returncode = CommUtil.trimToEmpty(map.get("return_code"));
		String returnmsg = CommUtil.trimToEmpty(map.get("return_msg")).toUpperCase();
		
		Map<String, Object> mapinfo = new HashMap<String, Object>();
		mapinfo.put("mapinfo", map);
		mapinfo.put("returncode", returncode);
		mapinfo.put("returnmsg", returnmsg);
		
		Map<String, Object> mapdate = new HashMap<String, Object>();
		if(returncode.contains("SUCCESS")){
			mapdate.put("服务商的APPID", map.get("appid"));
			mapdate.put("商户号", map.get("mch_id"));
			mapdate.put("子商户号", map.get("sub_mch_id"));
			mapdate.put("微信订单号", map.get("transaction_id"));
			mapdate.put("商户订单号", map.get("out_trade_no"));
			mapdate.put("随机字符串", map.get("nonce_str"));
			mapdate.put("签名", map.get("sign"));
			
			mapdate.put("交易状态", map.get("trade_state"));
			mapdate.put("付款银行", map.get("bank_type"));
			mapdate.put("用户标识", map.get("openid"));

			mapdate.put("返回信息", map.get("return_msg"));
			mapdate.put("标价币种", map.get("fee_type"));
			mapdate.put("现金支付金额", map.get("cash_fee"));
			mapdate.put("现金支付货币类型", map.get("cash_fee_type"));
			mapdate.put("标价金额", map.get("total_fee"));
			mapdate.put("交易状态描述", map.get("trade_state_desc"));
			mapdate.put("交易类型", map.get("trade_type"));
			mapdate.put("业务结果", map.get("result_code"));
			mapdate.put("返回状态码", map.get("return_code"));
			mapdate.put("是否关注公众账号", map.get("is_subscribe"));
			mapdate.put("支付完成时间", map.get("time_end"));
			mapinfo.put("mapdate", mapdate);
			System.out.println("成功输出  mapinfo    "+mapinfo);
		}else{
			System.out.println("其他输出  mapinfo    "+mapinfo);
		}
			CommUtil.responseBuildInfo(200, "SUCCESS", mapinfo);
		return mapinfo;
	}
	
	/**
	 * @Description：查询订单号信息
	 * @author origin
	 * @param ordernum 商户订单号   traordernum:交易订单号（微信订单号）  submchid:(服务商下的)子商户号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/inquireWXRefOrderData")
	@ResponseBody
	public Map<String, Object> inquireWXRefOrderData( String submchid, String ordernum, 
			String traordernum, String refundid, String outrefundno, String offset) throws Exception{
		//-------------------------------------------------
		if(CommUtil.trimToEmpty(submchid).equals("")) submchid = WeiXinConfigParam.SUBMCHID;
		SortedMap<String, String> params = new TreeMap<>();
		params.put("appid", WeiXinConfigParam.FUWUAPPID);
		params.put("mch_id", WeiXinConfigParam.MCHID);
//		params.put("sub_appid", "");
		params.put("sub_mch_id", submchid);
		
		//同时存在优先级为： refund_id > out_refund_no > transaction_id > out_trade_no
		if(!CommUtil.trimToEmpty(refundid).equals(""))params.put("refund_id", refundid);
		if(!CommUtil.trimToEmpty(outrefundno).equals(""))params.put("out_refund_no", outrefundno);
		if(!CommUtil.trimToEmpty(traordernum).equals(""))params.put("transaction_id", traordernum);
		if(!CommUtil.trimToEmpty(ordernum).equals(""))params.put("out_trade_no", ordernum);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		//------------------------------------------------------
		// 生成签名并放入map集合里
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/refundquery";
		String canshu = HttpRequest.getRequestXml(params);
		String result = HttpRequest.sendPost(url, canshu);
		Map<String, String> map = XMLUtil.doXMLParse(result);
		String returncode = CommUtil.trimToEmpty(map.get("return_code"));
		String returnmsg = CommUtil.trimToEmpty(map.get("return_msg")).toUpperCase();
		
		Map<String, Object> mapinfo = new HashMap<String, Object>();
		mapinfo.put("mapinfo", map);
		mapinfo.put("returncode", returncode);
		mapinfo.put("returnmsg", returnmsg);
		
		Map<String, Object> mapdate = new HashMap<String, Object>();
		if(returncode.contains("SUCCESS")){
			mapdate.put("服务商的APPID", map.get("appid"));
			mapdate.put("商户号", map.get("mch_id"));
			mapdate.put("子商户号", map.get("sub_mch_id"));
			mapdate.put("微信订单号", map.get("transaction_id"));
			mapdate.put("商户订单号", map.get("out_trade_no"));
			mapdate.put("随机字符串", map.get("nonce_str"));
			mapdate.put("签名", map.get("sign"));
			mapdate.put("返回信息", map.get("return_msg"));
			mapdate.put("业务结果", map.get("result_code"));
			mapdate.put("返回状态码", map.get("return_code"));
			
			mapdate.put("订单金额", map.get("total_fee"));
			mapdate.put("现金支付金额", map.get("cash_fee"));
			mapdate.put("退款笔数", map.get("refund_count"));
			mapdate.put("退款金额", map.get("refund_fee"));
			Integer refundcount = CommUtil.toInteger(map.get("refund_count"));
			for(int i = 0; i< refundcount; i++){
				String timenum = CommUtil.toString(i +1);
				String name = "第 "+timenum+" 笔";
				String refundfee = "refund_fee_"+i;
				String buoutrefundno = "out_refund_no_"+i;
				String wxrefundid = "refund_id_"+i;
				String refundstatus = "refund_status_"+i;
				String refundaccount = "refund_account_"+i;
				String refundchannel = "refund_channel_"+i;
				String refundsuccesstime = "refund_success_time_"+i;
				String refundrecvaccout = "refund_recv_accout_"+i;
				
				mapdate.put(name +"申请退款金额", map.get(refundfee));
				mapdate.put(name +"商户退款单号", map.get(buoutrefundno));
				mapdate.put(name +"微信退款单号", map.get(wxrefundid));
				mapdate.put(name +"退款状态", map.get(refundstatus));
				mapdate.put(name +"退款资金来源", map.get(refundaccount));
				mapdate.put(name +"退款渠道", map.get(refundchannel));
				mapdate.put(name +"退款成功时间", map.get(refundsuccesstime));
				mapdate.put(name +"退款入账账户", map.get(refundrecvaccout));
			}
			mapinfo.put("mapdata", mapdate);
			System.out.println("成功输出  mapinfo    "+mapinfo);
		}else{
			System.out.println("其他输出  mapinfo    "+mapinfo);
		}
			CommUtil.responseBuildInfo(200, "SUCCESS", mapinfo);
		return mapinfo;
	}
	
	@RequestMapping(value = "/testOrignReturn")
	@ResponseBody
	public Map<String, Object> testOrignReturn(String ordernum, String traordernum, 
			String refordernum, String submchid, String totalfee, String refundfee) throws Exception {
		Map<String, Object> map = new HashMap<>();
		WXPayConfigImpl config = WXPayConfigImpl.getInstance();
		WXPay wxpay = new WXPay(config);
		// 根据id查询充电信息
		String out_trade_no = CommUtil.trimToEmpty(ordernum);
		String out_refund_no = CommUtil.trimToEmpty(refordernum);
		String total_fee = CommUtil.trimToEmpty(totalfee);
		String refund_fee = CommUtil.trimToEmpty(refundfee);
		if(CommUtil.trimToEmpty(submchid).equals("")) submchid = WeiXinConfigParam.SUBMCHID;
		
		//------------------------------------------------------
		// 生成签名并放入map集合里
		SortedMap<String, String> params = new TreeMap<>();
		params.put("appid", WeiXinConfigParam.FUWUAPPID);
		params.put("mch_id", WeiXinConfigParam.MCHID);
		params.put("sub_mch_id", submchid);
		params.put("out_trade_no", out_trade_no);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/orderquery";
		String canshu = HttpRequest.getRequestXml(params);
		String sr = HttpRequest.sendPost(url, canshu);
		Map<String, String> resultMap = XMLUtil.doXMLParse(sr);
		
		if(out_refund_no.equals("")) out_refund_no = "t" + CommUtil.toString(resultMap.get("out_trade_no"));
		if(total_fee.equals("")) total_fee = CommUtil.toString(resultMap.get("total_fee"));
		if(refund_fee.equals("")) refund_fee = CommUtil.toString(resultMap.get("total_fee"));
		map.put("resultMap", resultMap);
		// 退款后的业务处理
		if (resultMap.get("return_code").toString().equalsIgnoreCase("SUCCESS")) {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("appid", WeiXinConfigParam.FUWUAPPID);
			data.put("mch_id", WeiXinConfigParam.MCHID);
			data.put("sub_mch_id", submchid);
			data.put("transaction_id", resultMap.get("transaction_id"));
			data.put("out_trade_no", out_trade_no);// 定单号
			data.put("out_refund_no", out_refund_no);
			data.put("total_fee", total_fee);
			data.put("refund_fee", refund_fee);
			data.put("refund_fee_type", "CNY");
			data.put("op_user_id", config.getMchID());

			try {
				Map<String, String> r = wxpay.refund(data);
				map.put("resultrefMap", r);
				System.out.println("G退款后的参数"+"========================"+r.toString());
				// 处理退款后的订单 成功
				if ("SUCCESS".equals(r.get("result_code"))) {
					map.put("ok", "ok");
				}else if ("FAIL".equals(r.get("result_code"))) {
					map.put("ok", "error");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("退款失败");
				map.put("ok", "error");
			}
		} else {
			map.put("ok", "error");
		}
		return map;
	}
	
	@RequestMapping(value = "/originTestReturn")
	@ResponseBody
	public Map<String, Object> originTestReturn(String ordernum, String traordernum, 
			String refordernum, String submchid, String totalfee, String refundfee) throws Exception {
		Map<String, Object> map = new HashMap<>();
		// 根据id查询充电信息
		String out_trade_no = CommUtil.toString(ordernum);
		String out_refund_no = CommUtil.toString(refordernum);
		String total_fee = CommUtil.toString(totalfee);
		String refund_fee = CommUtil.toString(refundfee);
		if(CommUtil.trimToEmpty(submchid).equals("")) submchid = WeiXinConfigParam.SUBMCHID;
		// 生成签名并放入map集合里
		SortedMap<String, String> params = new TreeMap<>();
		params.put("appid", WeiXinConfigParam.FUWUAPPID);
		params.put("mch_id", WeiXinConfigParam.MCHID);
		params.put("sub_mch_id", submchid);
		params.put("out_trade_no", out_trade_no);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		String sign = HttpRequest.createSign("UTF-8", params);
		params.put("sign", sign);
		String url = "https://api.mch.weixin.qq.com/pay/orderquery";
		String canshu = HttpRequest.getRequestXml(params);
		String sr = HttpRequest.sendPost(url, canshu);
		//查询订单信息
		Map<String, String> resultMap = XMLUtil.doXMLParse(sr);
		if(out_refund_no.equals("")) out_refund_no = "t" + CommUtil.toString(resultMap.get("out_trade_no"));
		if(total_fee.equals("")) total_fee = CommUtil.toString(resultMap.get("total_fee"));
		if(refund_fee.equals("")) refund_fee = CommUtil.toString(resultMap.get("total_fee"));
		map.put("resultMap", resultMap);
		// 退款后的业务处理
		if (resultMap.get("return_code").toString().equalsIgnoreCase("SUCCESS")) {
			try {
//				HashMap<String, String> data = new HashMap<String, String>();
				SortedMap<String, String> data = new TreeMap<>();
				data.put("appid", WeiXinConfigParam.FUWUAPPID);
				data.put("mch_id", WeiXinConfigParam.MCHID);
				data.put("sub_mch_id", submchid);
				data.put("transaction_id", resultMap.get("transaction_id"));
				data.put("out_trade_no", out_trade_no);// 定单号
				data.put("out_refund_no", out_refund_no);
				data.put("total_fee", total_fee);
				data.put("refund_fee", refund_fee);
				data.put("refund_fee_type", "CNY");
				String signref = HttpRequest.createSign("UTF-8", data);
				data.put("sign", signref);
				String canshuref = HttpRequest.getRequestXml(data);
				String urlref = "https://api.mch.weixin.qq.com/secapi/pay/refund";
				String srref = HttpRequest.sendPost(urlref, canshuref);
				Map<String, String> resultinfo = XMLUtil.doXMLParse(srref);

				if (resultinfo.get("return_code").equals("SUCCESS")) {
					// 退款成功
					System.out.println("returnMap为:" + resultinfo);
					map.put("ok", "ok");
				}else if ("FAIL".equals(resultinfo.get("result_code"))) {
					map.put("ok", "error");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("退款失败");
				map.put("ok", "error");
			}
		} else {
			map.put("ok", "error");
		}
		return map;
	}
	
//	@SuppressWarnings("unchecked")
	public Map<String, Object> judgeSession(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if(user==null) result = CommUtil.responseBuildInfo(901, "session缓存失效", result);
//		Map<String, Object> datamap = JSON.parseObject(JSON.toJSONString(user), Map.class);
//		result.put("user", user);
		result = CommUtil.responseBuildInfo(2000, "获取商户信息", result);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/homePageData")
	@ResponseBody
    public Object homePageData(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("sessionid=" + request.getSession().getId());
		Map<String, Object> datainfo = judgeSession(request);
		String codeinfo = CommUtil.toString(datainfo.get("code"));
		if(codeinfo.equals("901")) return datainfo;
		try {
			//-------------------------------------------------
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			//datatype 1: 直接从redis缓存中获取数据    其他:刷新获取最新数据并存入缓存中以便于下次获取
			Integer datatype = CommUtil.toInteger(maparam.get("type"));
			//-------------------------------------------------
			User user = (User) request.getSession().getAttribute("user");
			Integer merid = CommUtil.toInteger(user.getId());
			if(datatype.equals(1)){
				user = userService.selectUserById(merid);
				request.getSession().setAttribute("user", user);
			}
			Integer rank = CommUtil.toInteger(user.getRank());
			String openid = CommUtil.toString(user.getOpenid());
			String paramredis = "merchHomePageData_" + merid;
			Date newday = new Date();
			String merchHomePageData = JedisUtils.getnum(paramredis,1);
			if(datatype.equals(0) && merchHomePageData==null){//第一次进入redis中不存在书
				datainfo.put("hasdata", 0);
				return CommUtil.responseBuildInfo(200, "数据过期，请刷新获取最新数据！", datainfo);
			}else if(datatype.equals(0) && merchHomePageData!=null){//进入读取数据、redis存在数据
				datainfo = (Map<String, Object>) JSON.parse(merchHomePageData);
//				Date redisrenewalDate = (Date) datainfo.get("renewalDate");
				String redisrenewalTime = CommUtil.toString(datainfo.get("renewalTime"));
				Date redisrenewalDate = StringUtil.DateTime( redisrenewalTime, "yyyy-MM-dd HH:mm:ss");
				if(CommUtil.isToday(redisrenewalDate)){//在当天
					datainfo.put("hasdata", 2);
					CommUtil.responseBuildInfo(200, "SUCCEED", datainfo);
				}else{//不在当天
					datainfo.put("hasdata", 2);
					CommUtil.responseBuildInfo(200, "", datainfo);
				}
				return datainfo;
			}
				datainfo.put("hasdata", 1);
				datainfo.put("rank", rank);
				//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
				if (rank == 6) {
					List<Privilege> userPrivilege = userService.selectUserPrivilege(merid);
					System.out.println("privilege===" + JSON.toJSONString(userPrivilege));
					if (userPrivilege.size() > 0) {
						for (Privilege privilege : userPrivilege) {
							datainfo.put(privilege.getExplain(), "1");
						}
					}
					user = userService.selectUserById(user.getMerid());
					merid = CommUtil.toInteger(user.getId());
				}
				
				Double earnings = CommUtil.toDouble(user.getEarnings());
				datainfo.put("earnings", earnings);
				MerAmount merAmount = userService.selectMerAmountByMerid(merid);
				if(merAmount==null){
					merAmount = new MerAmount();
					userService.insertMerAmount(user.getId(), 0.0, 0.0, 0, 0);
				}
				String begintime = DisposeUtil.getPastDate(0,1) + " 00:00:00";
				String endtime = DisposeUtil.getPastDate(0,1) + " 23:00:00";
				//指定时间消耗电量
				int todayConsume=chargeRecordService.todayConsumeQuantity(user.getId(), begintime, endtime);
				
				String startTime = CommUtil.getRelevantDate(null, 1, 1);//指定时间的起始时间 如： yyyy-MM-dd 00:00:00
				String endTime = CommUtil.getRelevantDate(null, 1, 2);//指定时间的终止时间 如： yyyy-MM-dd 23:59:59
				//获取昨日信息数据
				Map<String, Object>  yesterdaydata = statisticsService.statisticsData(merid, 2, startTime, endTime);
				Integer yestertotal = yesterdaydata.size();
				Integer yesterdayConsume = CommUtil.toInteger(yesterdaydata.get("consumequantity"));
				if(yestertotal==0 || yesterdayConsume.equals(0)){
					datainfo.put("isCalculate", 1);
					yesterdaydata = collectStatisticsService.yesterdaydata(merid, 2, startTime, endTime);
					yesterdayConsume = CommUtil.toInteger(yesterdaydata.get("consumequantity"));
				}else{
					datainfo.put("isCalculate", 2);
				}
				Map<String, Object>  totaldata = statisticsService.statisticsData(merid, 2, null, null);
				
				Integer totalConsume = CommUtil.toInteger(totaldata.get("consumequantity"));
				totalConsume = CommUtil.toInteger(totalConsume+todayConsume);
				//昨日
				Double moneytotal = CommUtil.toDouble(yesterdaydata.get("moneytotal"));
				Double wechatretmoney = CommUtil.toDouble(yesterdaydata.get("wechatretmoney"));
				Double alipayretmoney = CommUtil.toDouble(yesterdaydata.get("alipayretmoney"));
				Double returnmoney = CommUtil.addBig(wechatretmoney, alipayretmoney);
				
				Double pulsemoney = CommUtil.toDouble(yesterdaydata.get("incoinsmoney"));
				Double windowpulsemoney = CommUtil.toDouble(yesterdaydata.get("windowpulsemoney"));
				Double oncardmoney = CommUtil.toDouble(yesterdaydata.get("oncardmoney"));
				
				Double unionpaymoney = CommUtil.toDouble(yesterdaydata.get("unionpaymoney"));
				
				Double incomemoney = StringUtil.subBig(moneytotal, returnmoney);
				incomemoney = StringUtil.addBig(incomemoney, unionpaymoney);
				
				Double incoinsmoney = CommUtil.addBig(pulsemoney, windowpulsemoney);
				
				datainfo.put("codeyestcoins", incoinsmoney);
				datainfo.put("oncardmoney", oncardmoney);
				datainfo.put("yestMoneys", moneytotal);
				datainfo.put("yestMoney", incomemoney);
				
				datainfo.put("todayConsume",todayConsume);
				datainfo.put("yesterdayConsume",yesterdayConsume);
				datainfo.put("totalConsume",totalConsume);
				datainfo.put("allMoney", CommUtil.toDouble(merAmount.getTotalOnlineEarn()));
				datainfo.put("nowMoney", CommUtil.toDouble(merAmount.getNowOnlineEarn()));
				datainfo.put("totalcoins", CommUtil.toInteger(merAmount.getTotalCoinsEarn()));
				datainfo.put("codenowcoins", CommUtil.toInteger(merAmount.getNowCoinsEarn()));
				
				//设备绑定信息
				Map<String, Object> codelines = userEquipmentService.selectustoequ(user.getId());
				Integer onlines = CommUtil.toInteger(codelines.get("onlines"));
				Integer disline = CommUtil.toInteger(codelines.get("disline"));
				//合伙人绑定的设备
				List<Equipment> partnercode = equipmentService.selectpartnercode(user.getId());
				if(partnercode!=null){
					for( Equipment item : partnercode){
						Byte line = item.getState();
						if(line==1) onlines += 1;
						if(line==0) disline += 1;
					}
				}
				datainfo.put("onlines", onlines);
				datainfo.put("disline", disline);
				//在线卡信息
				OnlineCard online = new OnlineCard();
				online.setMerid(merid);
				Integer onlincardcount = onlineCardService.onlinecount(online);
				datainfo.put("onlincardcount", onlincardcount);
				//商户名下会员信息
				Integer clientsnum = userService.inquireClientsNum(merid);
				datainfo.put("clientsnum", clientsnum);
				//商户名下小区信息
				Area area = new Area();
				area.setMerid(merid);
				List<Area> areaSelf =  areaService.selectAreaList(area);
				if(areaSelf==null) areaSelf = new ArrayList<Area>();
				Integer areanum = CommUtil.toInteger(areaSelf.size());
				datainfo.put("areanum", areanum);
				Date renewalDate = new Date();
				String renewalTime = CommUtil.toDateTime(renewalDate);
				datainfo.put("renewalDate", renewalDate);
				datainfo.put("renewalTime", renewalTime);
				CommUtil.responseBuildInfo(200, "SUCCEED", datainfo);
				JedisUtils.setnum(paramredis, JSON.toJSONString(datainfo), 864000, 1);//1296600 172800
				datainfo.put("hasdata", 1);
				CommUtil.responseBuildInfo(200, "", datainfo);
				return datainfo;
//			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuild(300, "数据错误异常，请重新操作", "");
		}
	}
	
}
