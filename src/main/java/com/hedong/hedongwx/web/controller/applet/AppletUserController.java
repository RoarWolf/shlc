package com.hedong.hedongwx.web.controller.applet;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.GeneralDetailService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.thread.Server;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.JedisUtils;
import com.hedong.hedongwx.utils.SendMsgUtil;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.XMLUtil;

/**
 * 用户操作
 * @author RoarWolf
 *
 */
@RequestMapping("/applet")
@RestController
public class AppletUserController {
	
	private final Logger logger = LoggerFactory.getLogger(AppletUserController.class);
	@Autowired
	private GeneralDetailService generalDetailService;
	@Autowired
	private ChargeRecordService chargeService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	public static Map<String, Long> orderMap = new HashMap<>();

	/**
	 * 获取用户openid
	 * @param code
	 * @return
	 */
	@PostMapping("/getopenid")
	public 	Object getopenid(String code, String username, String imageUrl) {
		return userService.addUserByAuth_code(code, username, imageUrl);
	}
	
	/**
	 * 获取用户信息
	 * @param code
	 * @return
	 */
	@PostMapping("/getUserInfo")
	public 	Object getUserInfo(Integer userid) {
		return userService.queryUserInfo(userid);
	}
	
	/**
	 * 获取用户钱包余额
	 * @param code
	 * @return
	 */
	@PostMapping("/getUserWallet")
	public 	Object getUserWallet(Integer userid) {
		return userService.getUserWallet(userid);
	}
	
	/**
	 * 查询用户钱包记录
	 * @param userid
	 * @param startnum
	 * @return
	 */
	@PostMapping("/getWalletRecord")
	public 	Object getWalletRecord(Integer userid, Integer startnum) {
		return generalDetailService.selectGenWalletDetailByUid(userid, startnum);
	}
	
	/**
	 * 查询用户订单记录
	 * @param userid
	 * @param startnum
	 * @param status
	 * @return
	 */
	@PostMapping("/getChargeRecord")
	public 	Object getChargeRecord(Integer userid, Integer startnum, Integer status) {
		return chargeService.queryChargeRecord(userid, status, startnum);
	}
	
	/**
	 * 查询用户订单信息
	 * @param userid
	 * @return
	 */
	@PostMapping("/getChargeRecordInfo")
	public 	Object getChargeRecordInfo(Integer orderid) {
		return chargeService.queryChargeRecordInfo(orderid);
	}
	
	/**
	 * 查询站点
	 * @param userid
	 * @return
	 */
	@PostMapping("/getAreaRecently")
	public 	Object getAreaRecently(Double lon, Double lat, Double distance, Integer startnum, 
			Integer distanceSort) {
		return areaService.queryAreaRecently(lon, lat, distance, startnum, distanceSort);
	}
	
	@PostMapping("/payChargerecord")
	public Object payChargerecord(String devicenum, Integer port, Integer userid, Double paymoney) {
		try {
			Double ctrlParamVal = paymoney * 100;
			String useridStr = DisposeUtil.completeNum(userid + "", 8);
			SendMsgUtil.send_0x1F(devicenum, port.byteValue(), "1", useridStr, (short) 21, "0", (byte) 3, 
					ctrlParamVal.intValue(), (byte) 1, (byte) 1, "0", (byte)1, null);
			return chargeService.insertChargeRecord(devicenum, port, 3, ctrlParamVal.intValue(), 1, userid, 1, paymoney);
		} catch (Exception e) {
			return CommUtil.responseBuildInfo(1002, "系统异常", null);
		}
	}
	
	/**
	 * 用户钱包充值预订单
	 * @param userid
	 * @param money
	 * @param request
	 * @return
	 */
	@PostMapping("/walletCharge")
	public Object walletCharge(Integer userid, Double money,HttpServletRequest request) {
		return userService.walletAppointCharge(userid, money, request);
	}
	
	/**
	 * 微信支付回调
	 * 
	 * @return
	 * @throws JDOMException
	 */
	@RequestMapping({ "/wolfnotify" })
	@ResponseBody
	public String wolfnotify() throws JDOMException {

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
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				out.write(resXml.getBytes());
				out.flush();
				out.close();
				// 获取商户订单号
				String ordernum = CommUtil.toString(map.get("out_trade_no"));
				String openid = CommUtil.toString(map.get("openid"));
				Long paytime = orderMap.get(ordernum);
				long nowtime = System.currentTimeMillis();
				if (paytime != null && (nowtime - paytime) < 5000) {
					return resXml;
				} else {
					try {
						userService.walletCharge(map, openid, ordernum);
					} catch (Exception e) {
					}
				}
			} else {
				logger.info("支付失败,错误信息：" + map.get("err_code"));
				System.out.println(map.get("err_code") + "----" + map.get("out_trade_no").toString());
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				out.write(resXml.getBytes());
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			// logger.error("支付回调发布异常：" + e);SortedMap<String, String> params = packagePayParamsToMap(request, money, openid, "wxpay/inCoinsPayback", format, "扫码投币");
			logger.warn("  [origin] 扫码     ");
			e.printStackTrace();
		}
		return resXml;
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping("/getBillingInfo")
	public Object getBillingInfo() {
		Map<String, String> hgetAll = JedisUtils.hgetAll("billingInfo");
		Map<String, Object> map = new HashMap<>();
		map.putAll(hgetAll);
		String timeInfoStr = hgetAll.get("timeInfo");
		List<Object> timeInfoList = (List<Object>) JSON.parse(timeInfoStr);
		map.remove("timeInfo");
		map.put("timeInfo", timeInfoList);
		return CommUtil.responseBuildInfo(1000, "获取成功", map);
	}
	
}
