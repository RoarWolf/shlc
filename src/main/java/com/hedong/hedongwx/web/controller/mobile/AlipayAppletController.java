package com.hedong.hedongwx.web.controller.mobile;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.BasicsService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.MD5Util;

/**
 * @Description: 支付宝小程序获取数据
 * @Author: origin  创建时间:2020年7月24日 上午10:51:25
 * @common:   
 */
@Controller
@RequestMapping("/appletAlipay")
public class AlipayAppletController {
	

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	@Autowired
	private UserService userService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private BasicsService basicsService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private OnlineCardService onlineCardService;
	
	/**
	 * @method_name: getHardversion
	 * @Description: 获取设备版本信息并判断设备在线、绑定与过期状态，返回相应值
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年7月27日 下午4:39:51
	 * @common:
	 */
	@RequestMapping("/getHardversion")
	@ResponseBody
	public Object getHardversion(HttpServletRequest request) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()) return CommUtil.responseBuildInfo(2004, "设备号传递不正确", resultdata);
			String equipmentnuminfor =  CommUtil.toString(maparam.get("code"));
			Integer equlengch = CommUtil.toInteger(equipmentnuminfor.length());
			String equipmentnum = null;
			if(equlengch==6){
				equipmentnum =  equipmentnuminfor;
			}else if(equlengch==7 || equlengch==8){
				equipmentnum =  equipmentnuminfor.substring( 0, 6);
				String port =  CommUtil.toString(equipmentnuminfor.substring(6));
				if(port!=null) resultdata.put("port", port);
			}else{
				resultdata.put("equipmentnum", equipmentnuminfor);
				return CommUtil.responseBuildInfo(2004, "设备号传递不正确", resultdata);
			}
			resultdata.put("equipmentnum", equipmentnuminfor);
			Map<String, Object> deviceinfo = equipmentService.getDeviceRelevanceInfo(equipmentnum);
			String devicename = CommUtil.toString(deviceinfo.get("remark"));
			String hardversion = CommUtil.toString(deviceinfo.get("hardversion"));
			Integer bindtype = CommUtil.toInteger(deviceinfo.get("bindtype"));
			Integer state = CommUtil.toInteger(deviceinfo.get("state"));
			Integer deviceType = CommUtil.toInteger(deviceinfo.get("device_type"));
			String expirationtime = CommUtil.toString(deviceinfo.get("expiration_time"));
			Date expirationDate = CommUtil.DateTime(expirationtime, "yyyy-MM-dd HH:mm:ss");
			Integer tempid = CommUtil.toInteger(deviceinfo.get("tempid"));
			String deviceimei = CommUtil.toString(deviceinfo.get("imei"));
			Integer merid = CommUtil.toInteger(deviceinfo.get("merid"));
			Integer submer = CommUtil.toInteger(deviceinfo.get("submer"));
			String merphone = CommUtil.toString(deviceinfo.get("merphone"));
			String merservephone = CommUtil.toString(deviceinfo.get("merservephone"));
			String areaname = CommUtil.toString(deviceinfo.get("areaname"));
			resultdata.put("params", equipmentnuminfor);
			resultdata.put("bindtype", bindtype);
			resultdata.put("deviceType", deviceType);
			resultdata.put("state", state);
			resultdata.put("hardversion", hardversion);
			if(submer==1) return CommUtil.responseBuildInfo(2012, "请使用微信扫码", resultdata);
			
			Map<String, Object> result = CommUtil.verifyEquipment(deviceType, bindtype, state, expirationDate);
			Integer returncode = CommUtil.toInteger(result.get("code"));
			resultdata.putAll(result);
			
			Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, merid, equipmentnum, hardversion);
			Integer ifalipay = CommUtil.toInteger(DirectTemp.get("ifalipay"));
			String temphone = CommUtil.toString(DirectTemp.get("common1"));
			String servephone = basicsService.getServephoneData(temphone, null, merservephone, merphone);

			Map<String, Object> errormap = new HashMap<String, Object>();
			errormap.put("equipmentname", devicename);
			errormap.put("areaname", areaname);
			errormap.put("phonenum", servephone);
			resultdata.put("res_data", errormap);
			if(returncode!=200){
				if(returncode.equals(2003)){
					errormap.put("equipmentimei", deviceimei);
					errormap.put("expirationtime", CommUtil.toDateTime(expirationtime));
				}
				errormap.put("equipmentname", devicename);
				errormap.put("areaname", areaname);
				errormap.put("phonenum", servephone);
				resultdata.put("res_data", errormap);
			}
			if(ifalipay!=1)return CommUtil.responseBuildInfo(2013, "该设备不支持支付宝支付", resultdata);
			
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(3000, "异常错误", resultdata);
		}
	}
	
	/**
	 * @method_name: deviceCharge
	 * @Description: 扫码充电获取设备数据
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年7月27日 下午4:39:09
	 * @common:
	 */
	@RequestMapping("/deviceCharge")
	@ResponseBody
	public Object deviceCharge(HttpServletRequest request) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()) return CommUtil.responseBuildInfo(2004, "设备号传递不正确", resultdata);
			String equipmentnuminfor =  CommUtil.toString(maparam.get("code"));
			Integer equlengch = CommUtil.toInteger(equipmentnuminfor.length());
			String devicenum = null;
			String port = null;
			if(equlengch.equals(6)){
				devicenum =  equipmentnuminfor;
			}else if(equlengch.equals(7) || equlengch.equals(8)){
				devicenum =  equipmentnuminfor.substring( 0, 6);
				port = CommUtil.toString(equipmentnuminfor.substring(6));
				if(port!=null) resultdata.put("port", port);
			}else{
				return CommUtil.responseBuildInfo(2004, "设备号传递不正确", resultdata);
			}
			resultdata.put("equipmentnum", devicenum);
			resultdata.put("params", equipmentnuminfor);
			resultdata.put("nowtime", System.currentTimeMillis() + "");
			Map<String, Object> deviceinfo = equipmentService.getDeviceRelevanceInfo(devicenum);
			//设备信息
			String hardversion = CommUtil.toString(deviceinfo.get("hardversion"));
			String devicename = CommUtil.toString(deviceinfo.get("remark"));
			Integer bindtype = CommUtil.toInteger(deviceinfo.get("bindtype"));
			Integer deviceType = CommUtil.toInteger(deviceinfo.get("device_type"));
			Integer state = CommUtil.toInteger(deviceinfo.get("state"));
			String expirationtime = CommUtil.toString(deviceinfo.get("expiration_time"));
			Date expirationDate = CommUtil.DateTime(expirationtime, "yyyy-MM-dd HH:mm:ss");
			expirationtime = CommUtil.toDateTime(expirationDate);
			Integer tempid = CommUtil.toInteger(deviceinfo.get("tempid"));
			
			//商户信息
			Integer merid = CommUtil.toInteger(deviceinfo.get("merid"));
			String merphone = CommUtil.toString(deviceinfo.get("merphone"));
			String merservephone = CommUtil.toString(deviceinfo.get("merservephone"));
			Integer submer = CommUtil.toInteger(deviceinfo.get("submer"));
			
			//小区信息
			String areaname = CommUtil.toString(deviceinfo.get("areaname"));

			resultdata.put("hardversion", hardversion);
			resultdata.put("equipmentname", devicename);
			resultdata.put("areaname", areaname);
			
			Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( tempid, merid, devicenum, hardversion);
			String brandname = CommUtil.toString(DirectTemp.get("remark"));//品牌名称[十路]
			String temphone = CommUtil.toString(DirectTemp.get("common1"));//服务电话[十路]
			String servephone = basicsService.getServephoneData(temphone, null, merservephone, merphone);
			Integer nowtempid = CommUtil.toInteger(DirectTemp.get("id"));
			Integer temporaryc = CommUtil.toInteger(DirectTemp.get("permit"));
			String chargeInfo = CommUtil.toString(DirectTemp.get("chargeInfo"));
			Integer walletpay = CommUtil.toInteger(DirectTemp.get("walletpay"));

			resultdata.put("brandname", brandname);
			resultdata.put("temporaryc", temporaryc);
			resultdata.put("phonenum",servephone );
			resultdata.put("chargeInfo", chargeInfo);
			resultdata.put("ifwallet", walletpay); //是否强制钱包支付
			
			Map<String, Object> result = CommUtil.verifyEquipment(deviceType, bindtype, state, expirationDate);
			Integer returncode = CommUtil.toInteger(result.get("code"));
			resultdata.putAll(result);
			if(!returncode.equals(200)) return resultdata;
			List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
			tempson = tempson == null ? new ArrayList<>() : tempson;
			List<Map<String, Object>> tempsonlist = new ArrayList<>();
			for(TemplateSon item : tempson){
				Map<String, Object> mapdata = new HashMap<String, Object>();
				Integer sontempid = item.getId();
				Double paymoney = item.getMoney();
				String sonname = item.getName();
				mapdata.put("id", sontempid);
				mapdata.put("money",paymoney);
				mapdata.put("name",sonname);
				tempsonlist.add(mapdata);
			}
			Map<String, Object> defaulteTemp = CommUtil.getTempDefaultObje(tempson, hardversion);
			resultdata.putAll(defaulteTemp);
			resultdata.put("templatelist", tempsonlist);
			resultdata.put("sendmoney", 0); //钱包赠送
			resultdata.put("balance", 0); //钱包充值
			if(port==null && !hardversion.equals("03") && !hardversion.equals("04")){
				Map<String, Object> resultPortStatus = CommUtil.historyStatisticsData(devicenum, hardversion);
				resultdata.putAll(resultPortStatus);
			}
			return resultdata;
			
//			checkUserAndDispose(openid,accesstoken,equcode);
//			touristuser = userService.getUserByOpenid(openid);
//			if(touristuser == null){
//				model.addAttribute("openiderror", "6您的账户暂时无法访问");
//				return "openiderror";
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(3000, "异常错误", resultdata);
		}
	}

	@RequestMapping("/scanOnlinCard")
	@ResponseBody
	public Map<String, Object> scanOnlinCard(HttpServletRequest request) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()) return CommUtil.responseBuildInfo(1001, "参数传递不正确或为空", resultdata);
			String onlinCardNum =  CommUtil.toString(maparam.get("cardNum"));
			OnlineCard onlinCardData = onlineCardService.selectOnlineCardByCardID(onlinCardNum);
			//在线卡信息
			Integer cardid = CommUtil.toInteger(onlinCardData.getId());
			Integer carduid = CommUtil.toInteger(onlinCardData.getUid());
			Integer carddealid = CommUtil.toInteger(onlinCardData.getMerid());
			Double cardtopupmoney = CommUtil.toDouble(onlinCardData.getMoney());
			Double cardsendmoney = CommUtil.toDouble(onlinCardData.getSendmoney());
			Double cardmoney = CommUtil.addBig(cardtopupmoney, cardsendmoney);
			String cardnum = CommUtil.toString(onlinCardData.getCardID());
			
			Integer cardstatus = CommUtil.toInteger(onlinCardData.getStatus());
			Integer relewallet = CommUtil.toInteger(onlinCardData.getRelevawalt());
			Integer cardareaid = CommUtil.toInteger(onlinCardData.getAid());
			resultdata.put("onlinCardNum", cardnum);
			resultdata.put("cardstatus", cardstatus);
			resultdata.put("carduid", carduid);
			resultdata.put("cardtopupmoney", cardtopupmoney);
			resultdata.put("cardsendmoney", cardsendmoney);
			resultdata.put("cardmoney", cardmoney);
			
			if(cardid == 0 || carddealid == 0) return CommUtil.responseBuildInfo(2005, "该在线卡未激活，请联系商户绑定激活", resultdata);
			if(carduid.equals(0)) return CommUtil.responseBuildInfo(2006, "该在线卡未被绑定，请先使用微信绑定", resultdata);
			if(cardtopupmoney>0 && cardstatus.equals(0)){
				CommUtil.responseBuildInfo(2007, "在线卡未激活只能充值一次", resultdata);
			}else if(cardstatus.equals(2) || cardstatus.equals(4)){
				CommUtil.responseBuildInfo(2008, "在线卡状态不正确，请联系商户处理。", resultdata);
			}
			
			Map<String, Object> onlinCardInfo =   onlineCardService.selectOnlineCardByCardNum(cardnum);
			//商户信息
			Integer merid = CommUtil.toInteger(onlinCardInfo.get("merid"));
			String merphone = CommUtil.toString(onlinCardInfo.get("mercphone"));
			String merservephone = CommUtil.toString(onlinCardInfo.get("mercservephone"));
			Integer submer = CommUtil.toInteger(onlinCardInfo.get("submer"));
			//Integer payhint = CommUtil.toInteger(onlinCardInfo.get("payhint"));
			//小区信息
			String areaname = CommUtil.toString(onlinCardInfo.get("areaname"));
			Integer onlinetempid = CommUtil.toInteger(onlinCardInfo.get("onlinetempid"));
			//用户信息
			Integer tourmerid = CommUtil.toInteger(onlinCardInfo.get("tourmerid"));
			String tournick = CommUtil.toString(onlinCardInfo.get("tournick"));
			String tourphone = CommUtil.toString(onlinCardInfo.get("tourphone"));
			Double tourtopupmoney = CommUtil.toDouble(onlinCardInfo.get("tourbalance"));
			Double toursendmoney = CommUtil.toDouble(onlinCardInfo.get("toursendmoney"));
			resultdata.put("areaname", areaname);
			resultdata.put("tournick", tournick);
			resultdata.put("tourphone", tourphone);
			
			if(relewallet==1){//1:关联用户钱包
				if(carddealid!=tourmerid) CommUtil.responseBuildInfo(2009, "该在线卡商户与钱包商户不一致，不能充值", resultdata);
				onlinCardData.setMoney(tourtopupmoney);
				onlinCardData.setSendmoney(toursendmoney);
			}
			
			Map<String, Object> DirectTemp = basicsService.inquireDirectTempData( onlinetempid, carddealid, null, "在线卡");
			String brandname = CommUtil.toString(DirectTemp.get("remark"));//品牌名称[十路]
			String temphone = CommUtil.toString(DirectTemp.get("common1"));//服务电话[十路]
			String servephone = basicsService.getServephoneData(temphone, null, merservephone, merphone);
			Integer nowtempid = CommUtil.toInteger(DirectTemp.get("id"));
//			Integer temporaryc = CommUtil.toInteger(DirectTemp.get("permit"));
//			String chargeInfo = CommUtil.toString(DirectTemp.get("chargeInfo"));
//			Integer walletpay = CommUtil.toInteger(DirectTemp.get("walletpay"));
			
			resultdata.put("brandname", brandname);
			resultdata.put("servephone", servephone);
			if(tourphone==null) return  CommUtil.responseBuildInfo(2010, "该在线卡用户未绑定手机号，请绑定手机号", resultdata);
			String returncode = CommUtil.toString(resultdata.get("code"));
			if(returncode!=null) return resultdata;
			List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
			List<Map<String, Object>> tempsonlist = new ArrayList<>();
			for(TemplateSon item : tempson){
				Map<String, Object> mapdata = new HashMap<String, Object>();
				Integer sontempid = item.getId();
				Double paymoney = item.getMoney();
				String sonname = item.getName();
				Double accountmoney = item.getRemark();
				mapdata.put("id", sontempid);
				mapdata.put("money",paymoney);
				mapdata.put("name",sonname);
				mapdata.put("accountmoney",accountmoney);
				tempsonlist.add(mapdata);
			}
			resultdata.put("tempsonlist", tempsonlist);
			return CommUtil.responseBuildInfo(200, "成功", resultdata);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(3000, "异常错误", resultdata);
		}
	}
	
	@RequestMapping(value = "/bindingPhone")
	@ResponseBody
	public Map<String, Object> bindingPhone(Integer uid, String phone){
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			phone = CommUtil.trimToEmpty(phone);
			//phone = CommUtil.toStringTrim(phone);
			if(phone=="") return CommUtil.responseBuildInfo(2011, "手机号不能为空", resultdata);
			User touuser = userService.getUserByPhoneNum(phone);
			if(touuser==null){
				User user = new User();
				user.setId(uid);
				user.setPhoneNum(phone);
				String password = phone.substring(4);
				user.setPassword(MD5Util.MD5Encode(password, "utf-8"));
				user.setUpdateTime(new Date());
				userService.updateUserById(user);
//				touuser = userService.getUserByPhoneNum(phone);
//				tourid = CommUtil.toInteger(touuser.getId());
				CommUtil.responseBuildInfo(200, "手机号绑定成功", resultdata);
			}else{
				resultdata.put("tourid", touuser.getId());
				resultdata.put("tournick", touuser.getUsername());
				CommUtil.responseBuildInfo(2012, "该手机号已被其他用户占用", resultdata);
			}
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(3000, "异常错误", resultdata);
		}
	}
	
	
	
	
}
