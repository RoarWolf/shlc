package com.hedong.hedongwx.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.UserHandler;
import com.hedong.hedongwx.entity.User;

import net.sf.json.JSONObject;

public class TempMsgUtil {
	
	public static final String TEMP_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	/**帐户资金变动提醒*/
	public static final String TEMP_ID1 = "EkKbHQgbISE_8XF7jKE1_8c4wNcGw1csYfHMMxXrd08";
	/**提现到账通知*/
	public static final String TEMP_ID2 = "ldENC_ALSkaST23qB9pgHgzEjdfpMhoc0csl_tPswEE";
	/**付款成功通*/
	public static final String TEMP_IDPAY = "M2GF7Ho_PiEK5Q4EA5FRY4e9_05dQ8Q4i7AReCn12yE";
	/**退费结果提醒*/
	public static final String TEMP_IDTUI = "r5PnvoYps9r4noTfPaNBTZD6S48EYrj0zBwqbCIaSDI";
	/**充电结束通知*/
	public static final String TEMP_IDEND = "BLMThDQKFqHKlMFj1qy_4Rtp5ZOwoUd6ZjOaOzvikoc";
	
	//设备状态变更通知
	/**设备故障通知 13*/
	public static final String TEMP_IDFUWU = "uzaexAjHyncrG3cuQ-qTgpS4bqYKZhLhc51dij9ij_o";
	
	
	public static final String TEMP_IDCHARGE = "KQp5PCCsqrZMzk8tXfWxvUZQcDOQaJAU4oIBC2Oop1A";
	
	/** 开始充电  */
	public static String FIRST_PAYSUCCEED = "尊敬的用户，您的付款已经成功，现在开始充电！";
	/** 结束充电  */
	public static String FIRST_ENDMESSAGE = "尊敬的用户，您的充电已经结束。";
	/** 退费  */
	public static String FIRST_RETURNMONEY = "您好，您的退费请求已受理，资金将原路返回到您的账户中。";
	
	/** 充电支付成功消息  */
	public static String MESSAGE_CHARGEPAY = "本次交易付款成功，开始充电，若出现扣费不充电的情况，请换个设备扫码充电，已扣费用会在10分钟内原路退回。";
	/** 充电支付退费消息  */
	public static String MESSAGE_CHARGERETURN = "本次交易付款成功，开始充电，若出现扣费不充电的情况，请换个设备扫码充电，已扣费用会在10分钟内原路退回。";
	/** 充电结束消息  */
	public static String MESSAGE_CHARGEFINISH = "";
	
	/** 设备到期消息 */
	public static String MESSAGE_REMIND= "设备即将到期，请及时续费，设备号如下:";
	
	public static String URL_MESSAGE = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=1&id=";
	
	/**
	 * @Description：
	 * @author： origin
	 * @createTime：2019年12月31日下午5:02:40
	 * @comment:
	 */
	public static void endChargeSendMess(String first, String ordernum, Integer orderid, Integer resultinfo, 
		Integer dealid, Integer uid, Double paymoney, String ordertime, String devicenum, String port, 
		String devicename, Integer deviceaid, String areaname, String servicphone) {
		System.out.println("输出提示   "+first);
		String firstdata = first;
		String resultdata = null;
		if(resultinfo.equals(0)){//0X00 购买的充电时间或电量用完了
			resultdata = "充电完成";
		}else if(resultinfo.equals(1)){//0X01 用户手动停止或拔插头
//			resultdata = "空载断电";
			resultdata = "充电插头松动或已拔掉";
		}else if(resultinfo.equals(2)){//0X02 充电满了，自动停止
			resultdata = "充满自停";
		}else if(resultinfo.equals(3)){//0X03 超功率自停
			resultdata = "超功率自停";
		}else if(resultinfo.equals(4)){//0X04 远程断电	    //ORIGIN  ToUpload	远程断电 结束原因更改为 ： 显示充电线脱落
//			resultdata = "远程断电（注：该退款到平台钱包里。）";
			resultdata = "充电线脱落.";
		}else if(resultinfo.equals(5)){//0X05
			resultdata = " 刷卡断电";
		}else if(resultinfo.equals(11)){//0X0B 设备或端口号出现问题，被迫停止
			resultdata = "充电完成(11)";
//			resultdata = "设备异常，被迫停止";
		}else if(resultinfo.equals(255)){//255 日志结束充电
			resultdata = "充电完成(255)";
//			resultdata = "被迫停止";
		}else{//其它
			resultdata = "充电完成。";
//			resultdata = "空载断电。";
		}
		StringBuffer devicedata = new StringBuffer();
		if(areaname!=null) devicedata.append(areaname).append(" ");
		if(devicename!=null) devicedata.append(devicename).append(" ");
		if(devicenum!=null) devicedata.append(devicenum);
		if(port!=null) devicedata.append("-").append(port);
		
		String remark = "本次充电已结束，如有疑问请咨询商户:"+servicphone;
		String url = TempMsgUtil.URL_MESSAGE;
		User tourist = UserHandler.getUserInfo(uid);
		tourist = tourist == null ? new User() : tourist;
		String oppenid = CommUtil.toString(tourist.getOpenid());
		if(oppenid!= null && oppenid !=""){
			tempChargeEndChoose(firstdata, oppenid, ordernum, devicedata.toString(), resultdata, 
					ordertime, CommUtil.toString(paymoney), remark, url);
		}
	}
	
	/**	充电结束提醒 */
	public static final String TEMP_CHARGEENDCHOOSE = "twPVqIcNNiEG5NGLkOPTEj_CsXd4uv0eeLoJgSnDItM";
	
	/**
	 * @Description： 充电订单结束通知(服务发送充电结束通知)
	 * @author： origin 
	 */
	public static void tempChargeEndChoose(String first, String oppenid, String ordernum, String devicedata, String resultdata,
		String temptime, String paymoney, String remark, String url){
		try {
			JSONObject json = new JSONObject();
			json.put("first", TempMsgUtil.inforData(first,"#FF3333"));
			json.put("keyword1", TempMsgUtil.inforData(ordernum,"#0044BB"));
			json.put("keyword2", TempMsgUtil.inforData(devicedata,"#0044BB"));
			json.put("keyword3", TempMsgUtil.inforData(resultdata,"#0044BB"));
			json.put("keyword4", TempMsgUtil.inforData(temptime,"#0044BB"));
			json.put("keyword5", TempMsgUtil.inforData("¥ "+paymoney,"#0044BB"));
			json.put("remark", TempMsgUtil.inforData(remark,"#0044BB"));
			sendTempMsg(oppenid, TEMP_CHARGEENDCHOOSE, url, json);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**	@ORIGIN
	 * choose
	 * 开始充电提醒
	 * {{first.DATA}} 
	 * 开始时间：{{keyword1.DATA}} 充电地址：{{keyword2.DATA}} 设备编号：{{keyword3.DATA}} 预设时间：{{keyword4.DATA}} 
	 * {{remark.DATA}}
	 * */
	public static final String TEMP_CHARGECHOOSE = "goxdhDVAOZ571ohc23sxdFb4f5WJNLdvnnEwU4qMldc";
	
	/**
	 * @Description： 充电订单通知(服务发送充电通知)
	 * @author： origin 
	 */
	public static void tempChargeChoose(String first, String oppenid, String addressdata, String devicedata, String preinstall,
		String temptime, String remark, String url){
		try {
			JSONObject json = new JSONObject();
			json.put("first", TempMsgUtil.inforData( first,"#FF3333"));
			json.put("keyword1", TempMsgUtil.inforData(temptime,"#0044BB"));
			json.put("keyword2", TempMsgUtil.inforData(addressdata,"#0044BB"));
			json.put("keyword3", TempMsgUtil.inforData(devicedata,"#0044BB"));
			json.put("keyword4", TempMsgUtil.inforData(preinstall,"#0044BB"));
			json.put("remark", TempMsgUtil.inforData(remark,"#0044BB"));
			sendTempMsg(oppenid, TEMP_CHARGECHOOSE, url, json);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**	
	 * 设备状态反馈通知 01
	 * {{first.DATA}} 
	 * 插座编号：{{keyword1.DATA}}  充电地址：{{keyword2.DATA}}  充电时间：{{keyword3.DATA}}  充电耗时：{{keyword4.DATA}} 
	 * 消费金额：{{keyword5.DATA}}  {{remark.DATA}}
	 * */
	public static final String TEMP_CHARGEEND = "1r0DS_sQT2tzC06Zw49gbwxG4C_ujPMwyTOV8nMaoF4";
	
	/**	设备状态反馈通知 01 */
	public static final String TEMP_DEVICEMESS = "4DyQoqyBp6gGwq213AzH-PSsIwo3XCdwVdcat1dcKgA";
	
	/**
	 * 设备状态变更提醒 03
	 * {{first.DATA}}  
	 * 设备名称：{{keyword1.DATA}}  当前状态：{{keyword2.DATA}}   之前状态：{{keyword3.DATA}}
	       变更时间：{{keyword4.DATA}}   操作账号：{{keyword5.DATA}}  {{remark.DATA}}
	 */
	public static final String TEMP_DEVICSTATUS = "DRMx6QQ5fECAsP6Y32CSqzsb_83RGgsAWFrAc2xaBS8";
		
	/**@ORIGIN
	 * 设备状态改变提醒 09
	 * {{first.DATA}}
		设备名称：{{keyword1.DATA}}  当前状态：{{keyword2.DATA}}  变更时间：{{keyword3.DATA}}
		{{remark.DATA}}
	 */
	public static final String TEMP_DEVICESTATUS = "bPBeLcty-Y-upm6kLhy-hC2GUpoydV9P2yqrAfXYD-s";	

	
	/**@ORIGIN
	 * 新订单提醒 12
	 * {{first.DATA}}
		提交时间：{{keyword1.DATA}}  订单类型：{{keyword2.DATA}}  订单状态：{{keyword3.DATA}}
		订单来源：{{keyword4.DATA}}  订单详情：{{keyword5.DATA}}  {{remark.DATA}}
	 */
	public static final String TEMP_ORDERNEW = "mzJ1qeEfj8DfqViTJNBQJTNpJO8IWCHZYYHWTfMEiR4";
	
	/**@ORIGIN
	 * 订单状态变更提醒 15
	 * {{first.DATA}}
		订单类型：{{keyword1.DATA}}  订单状态：{{keyword2.DATA}}  订单详情：{{keyword3.DATA}}
		订单金额：{{keyword4.DATA}}  {{remark.DATA}}
	 */
	public static final String TEMP_ORDERSTATUS = "upxECIoFkIKpL260o5EMUuYKjuPOk6LoYBjo_d6X9lY";	
	
	/**@ORIGIN
	 * 充电订单确认通知 05
	 * {{first.DATA}}
		订单编号：{{keyword1.DATA}}  订单金额：{{keyword2.DATA}}  订单状态：{{keyword3.DATA}}
		订单时间：{{keyword4.DATA}}  咨询电话：{{keyword5.DATA}}  {{remark.DATA}}
	 */
	public static final String TEMP_ORDERCHAREG = "HANXl7ySbeRG8rZXfrrlCsCKkq0kCEbc-q89Vjs4EMA";
	
	/**@ORIGIN
	 * 付款成功通知 06
	 * {{first.DATA}} 
	 * 交易商户：{{keyword1.DATA}}  支付金额：{{keyword2.DATA}}  交易时间：{{keyword3.DATA}}
	        商家电话：{{keyword4.DATA}}  {{remark.DATA}}
	 */
	public static final String TEMP_PAYMONEY = "M2GF7Ho_PiEK5Q4EA5FRY4e9_05dQ8Q4i7AReCn12yE";

	/**
	 * 充电结束通知 02 
	 * {{first.DATA}}  
	 * 设备名称：{{keyword1.DATA}}  开始时间：{{keyword2.DATA}}  结束时间：{{keyword3.DATA}}
	   {{remark.DATA}}
	 */
	public static final String TEMP_CHARGE = "BLMThDQKFqHKlMFj1qy_4Rtp5ZOwoUd6ZjOaOzvikoc";	
	
	/**
	 * 充电结束通知 08
	 * {{first.DATA}}
	 * 结束充电时间：{{keyword1.DATA}}  充电地点：{{keyword2.DATA}}  充电耗时：{{keyword3.DATA}}
	      消费金额：{{keyword4.DATA}}      {{remark.DATA}}
	 */
	public static final String TEMP_CHARGEENDS = "NHNb4b43ifrbaim-Fti9nd0UG_rwPUOoL92XcFCsm6A";	
	
	/**@ORIGIN
	 * 退费结果提醒 14
	 * {{first.DATA}}
		服务名称：{{keyword1.DATA}}  订单编号：{{keyword2.DATA}}  退费时间：{{keyword3.DATA}}
	         退费金额：{{keyword4.DATA}}   咨询电话：{{keyword5.DATA}}  {{remark.DATA}}
	 */
	public static final String TEMP_RETURNMONEY = "r5PnvoYps9r4noTfPaNBTZD6S48EYrj0zBwqbCIaSDI";
	
	/**@ORIGIN
	 * 提现到账通知 11
	 * {{first.DATA}}
		提现渠道：{{keyword1.DATA}}  提现金额：{{keyword2.DATA}}  提现时间：{{keyword3.DATA}}
		{{remark.DATA}}
	 */
	public static final String TEMP_WITHDRAW = "ldENC_ALSkaST23qB9pgHgzEjdfpMhoc0csl_tPswEE";
	
	/**
	 * 设备到期提醒
	 * {{first.DATA}}
		设备数量：{{keyword1.DATA}}  有效剩余时间：{{keyword2.DATA}}  
		{{remark.DATA}}
	 */	
	public static final String TEMP_IDREMIND = "BHb2bodGfKx60tY-O1XZy9VKR0w3qM9iV5F55Oi1eyg";//正式模板idrGKKBRPqZHA_2mu-W3uxtfPv1C1Bh3RYDbz-UVNvtmw
	/**
	 * @Description： 设备到期提醒
	 * @author： ZZ 
	 */
	public static void expireRemind(String first, String oppenid, Integer equipmentNum, Integer days, String url, String remark){
		try {
			if(oppenid != null && !"".equals(oppenid)){
				JSONObject json = new JSONObject();
				json.put("first", TempMsgUtil.inforData( first,"#FF3333"));
				json.put("keyword1", TempMsgUtil.inforData(equipmentNum+"台","#0044BB"));
				json.put("keyword2", TempMsgUtil.inforData(days+"天","#0044BB"));
				json.put("remark", TempMsgUtil.inforData(remark,"#0044BB"));
				sendTempMsg(oppenid, TEMP_IDREMIND, url, json);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	
	private static final Logger logger = LoggerFactory.getLogger(SendMsgUtil.class);
	//==========================================================================================================================

	/**
	 * @Description： 服务订单通知(服务发送通知)
	 * @author： origin 
	 */
	public static void serveSendMsg(String first, Integer merid, String order, String itemname, 
			Double money, String state, String time, String url, String remark){
		try {
			User meruser = UserHandler.getUserInfo(merid);
			if(null!=meruser){
				String oppenid = meruser.getOpenid();
				JSONObject json = new JSONObject();
				json.put("first", TempMsgUtil.inforData( first,"#FF3333"));
				json.put("keyword1", TempMsgUtil.inforData(order,"#0044BB"));
				json.put("keyword2", TempMsgUtil.inforData(itemname,"#0044BB"));
				json.put("keyword3", TempMsgUtil.inforData("¥ "+String.format("%.2f", money),"#0044BB"));
				json.put("keyword4", TempMsgUtil.inforData(state,"#0044BB"));
				json.put("keyword5", TempMsgUtil.inforData(time,"#0044BB")); 
				json.put("remark", TempMsgUtil.inforData(remark,"#0044BB"));
				sendTempMsg(oppenid, TEMP_IDCHARGE, url, json);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
/** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/
	/**
	 * @Description： 订单状态变更提醒 15
	 * @author： origin
	 * {{first.DATA}}
		订单类型：{{keyword1.DATA}}  订单状态：{{keyword2.DATA}}  订单详情：{{keyword3.DATA}}
		订单金额：{{keyword4.DATA}}  {{remark.DATA}}
	 */
	public static void orderStatus(String first, String oppenid, String type, String status, String details, 
			Double money, String remark, String url){
		try {
			JSONObject json = new JSONObject();
			json.put("first", TempMsgUtil.inforData( first,"#FF3333"));
			json.put("keyword1", TempMsgUtil.inforData(type,"#0044BB"));
			json.put("keyword2", TempMsgUtil.inforData(status,"#0044BB"));
			json.put("keyword3", TempMsgUtil.inforData(details,"#0044BB"));
			json.put("keyword4", TempMsgUtil.inforData(CommUtil.toDouble(money).toString(),"#0044BB"));
			json.put("remark", TempMsgUtil.inforData("remark","#0044BB"));
			sendTempMsg(oppenid, TEMP_ORDERSTATUS, url, json);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**
	 * @Description： 付款成功通知 06
	 * @author： origin
	 * {{first.DATA}} 
	 * 交易商户：{{keyword1.DATA}}  支付金额：{{keyword2.DATA}}  交易时间：{{keyword3.DATA}}
	        商家电话：{{keyword4.DATA}}  {{remark.DATA}}
	 */
	public static void orderPayCharge(String first, String oppenid, String merchant, Double money, String time, 
			String phone, String remark, String url){
		try {
			JSONObject json = new JSONObject();
			json.put("first", TempMsgUtil.inforData( first,"#FF3333"));
			json.put("keyword1", TempMsgUtil.inforData(merchant,"#0044BB"));
			json.put("keyword2", TempMsgUtil.inforData(CommUtil.toDouble(money).toString(),"#0044BB"));
			json.put("keyword3", TempMsgUtil.inforData(time,"#0044BB"));
			json.put("keyword4", TempMsgUtil.inforData(phone,"#0044BB"));
			json.put("remark", TempMsgUtil.inforData("remark","#0044BB"));
			sendTempMsg(oppenid, TEMP_PAYMONEY, url, json);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**
	 * @Description： 充电结束通知 02
	 * @author： origin
	 * {{first.DATA}}  
	 * 设备名称：{{keyword1.DATA}}  开始时间：{{keyword2.DATA}}  结束时间：{{keyword3.DATA}}
	   {{remark.DATA}}
	 */
	public static void orderReturnMoney(String first, String oppenid, String order, Double money, String status, 
			String time, String phone, String remark, String url){
		try {
			JSONObject json = new JSONObject();
			json.put("first", TempMsgUtil.inforData( first,"#FF3333"));
			json.put("keyword1", TempMsgUtil.inforData(order,"#0044BB"));
			json.put("keyword2", TempMsgUtil.inforData(CommUtil.toDouble(money).toString(),"#0044BB"));
			json.put("keyword3", TempMsgUtil.inforData(status,"#0044BB"));
			json.put("keyword4", TempMsgUtil.inforData(time,"#0044BB"));
			json.put("keyword5", TempMsgUtil.inforData(phone,"#0044BB"));
//			json.put("remark", TempMsgUtil.inforData("remark","#0044BB"));
			sendTempMsg(oppenid, TEMP_CHARGE, url, json);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	//-------------------------------------------------------------
	
	/**
	 * @Description： 新订单提醒 12
	 * @author： origin
	 * {{first.DATA}}
		提交时间：{{keyword1.DATA}}  订单类型：{{keyword2.DATA}}  订单状态：{{keyword3.DATA}}
		订单来源：{{keyword4.DATA}}  订单详情：{{keyword5.DATA}}  {{remark.DATA}}
	 */
	public static void oederNew(String first, Integer merid, String time, String type, String status, String source, 
			String details, String remark, String url){
		try {
			User meruser = UserHandler.getUserInfo(merid);
			if(null!=meruser){
				String oppenid = meruser.getOpenid();
				JSONObject json = new JSONObject();
				json.put("first", TempMsgUtil.inforData( first,"#FF3333"));
				json.put("keyword1", TempMsgUtil.inforData(time,"#0044BB"));
				json.put("keyword2", TempMsgUtil.inforData(type,"#0044BB"));
				json.put("keyword3", TempMsgUtil.inforData(status,"#0044BB"));
				json.put("keyword4", TempMsgUtil.inforData(source,"#0044BB"));
				json.put("keyword5", TempMsgUtil.inforData(details,"#0044BB"));
//				json.put("remark", TempMsgUtil.inforData("","#0044BB"));
				TempMsgUtil.sendTempMsg(oppenid, TEMP_ORDERNEW, url, json);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**
	 * 充值成功提醒
	 */
	public static final String TEMP_ORDERDEALER = "EOXzcNquBUnbIvMZEuEEGeV0hwbxEjuRR0TSGiouOUI";
	public static void dealerOrderPush(String first, String oppenid, String paymoney, String balance, String paytime,  
		String remark, String url){
		try {
			if(null!=oppenid){
				JSONObject json = new JSONObject();
				json.put("first", inforData(first,"#FF3333"));
				json.put("keyword1", inforData(paymoney,"#0044BB"));
				json.put("keyword2", inforData(paytime,"#0044BB"));
				json.put("keyword3", inforData(balance,"#0044BB"));
				json.put("remark", inforData(remark,"#0044BB"));
				TempMsgUtil.sendTempMsg(oppenid, TEMP_ORDERDEALER, url, json);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**
	 * @Description： 充电订单确认通知 05
	 * @author： origin
	 * {{first.DATA}}
		订单编号：{{keyword1.DATA}}  订单金额：{{keyword2.DATA}}  订单状态：{{keyword3.DATA}}
		订单时间：{{keyword4.DATA}}  咨询电话：{{keyword5.DATA}}  {{remark.DATA}}
	 */
	public static void orderCharge(String first, String oppenid, String order, Double money, String status, 
			String time, String phone, String remark, String url){
		try {
			JSONObject json = new JSONObject();
			json.put("first", TempMsgUtil.inforData(first,"#FF3333"));
			json.put("keyword1", TempMsgUtil.inforData(order,"#0044BB"));
			json.put("keyword2", TempMsgUtil.inforData("¥ "+CommUtil.toDouble(money).toString(),"#0044BB"));
			json.put("keyword3", TempMsgUtil.inforData(status,"#0044BB"));
			json.put("keyword4", TempMsgUtil.inforData(time,"#0044BB"));
			json.put("keyword5", TempMsgUtil.inforData(phone,"#0044BB"));
			json.put("remark", TempMsgUtil.inforData(remark,"#0044BB"));
			sendTempMsg(oppenid, TEMP_ORDERCHAREG, url, json);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**
	 * @Description： 提现到账通知 11
	 * @author： origin
	 * {{first.DATA}}
		提现渠道：{{keyword1.DATA}}  提现金额：{{keyword2.DATA}}  提现时间：{{keyword3.DATA}}
		{{remark.DATA}}
	 */
	public static void withdrawMess(String first, Integer merid, String way, Double money, String time, 
			String remark, String url){
		try {
			User meruser = UserHandler.getUserInfo(merid);
			if(null!=meruser){
				String oppenid = meruser.getOpenid();
				JSONObject json = new JSONObject();
				json.put("first", TempMsgUtil.inforData( first,"#FF3333"));
				json.put("keyword1", TempMsgUtil.inforData(way,"#0044BB"));
				json.put("keyword2", TempMsgUtil.inforData(CommUtil.toDouble(money).toString(),"#0044BB"));
				json.put("keyword3", TempMsgUtil.inforData(time,"#0044BB"));
//				json.put("remark", TempMsgUtil.inforData("remark","#0044BB"));
				sendTempMsg(oppenid, TEMP_WITHDRAW, url, json);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**
	 * @Description： 设备状态变更提醒
	 * @author： origin 创建时间：   2019年8月27日 下午5:12:53
	 * @param   设备状态改变提醒 	{{first.DATA}} 设备名称：{{keyword1.DATA}}  
	 * 			当前状态：{{keyword2.DATA}}  变更时间：{{keyword3.DATA}} {{remark.DATA}}
	 */
	public static void equipSendMsg(String first, Integer merid, String code, String equipname, String equipstate, 
			String remark, String url){
		try {
			User meruser = UserHandler.getUserInfo(merid);
			if(null!=meruser){
				String oppenid = meruser.getOpenid();
				JSONObject json = new JSONObject();
				json.put("first", TempMsgUtil.inforData( first,"#FF3333"));
				json.put("keyword1", TempMsgUtil.inforData(equipname,"#0044BB"));
				json.put("keyword2", TempMsgUtil.inforData(equipstate,"#0044BB"));
				json.put("keyword3", TempMsgUtil.inforData(CommUtil.toDateTime(),"#0044BB"));
//				json.put("remark", TempMsgUtil.inforData("remark","#0044BB"));
				sendTempMsg(oppenid, TEMP_DEVICESTATUS, url, json);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
/** =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/
	/**
	 * @Description： 付款成功消息模板发送
	 * @author： origin  
	 * @param remark 
	 */
	public static void paychargesendmsg(String first, Integer uid, String phone, String url, Double money, String remark){
		try {
			User user = UserHandler.getUserInfo(uid);
			if(null!=user){
				String oppenid = user.getOpenid();
				JSONObject json = new JSONObject();
				json.put("first", TempMsgUtil.inforData( first,"#FF3333"));
				json.put("keyword1", TempMsgUtil.inforData("自助充电平台","#0044BB"));
				json.put("keyword2", TempMsgUtil.inforData("¥ "+String.format("%.2f", money),"#0044BB"));
				json.put("keyword3", TempMsgUtil.inforData(StringUtil.toDateTime(),"#0044BB")); 
				json.put("keyword4", TempMsgUtil.inforData(phone,"#0044BB"));
				json.put("remark", TempMsgUtil.inforData(remark,"#0044BB"));
				sendTempMsg(oppenid, TEMP_IDPAY, null, json);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**
	 * @Description： 充电结束模板发送
	 * @author： origin  
	 */
	public static void finishchargesendmsg(String info,Integer uid, Integer merid, String code, Date beginTime, Date endTime, Integer codeid){
		try {
			User user = UserHandler.getUserInfo(uid);
			if(null!=user){
				String oppenid = user.getOpenid();
				String urltem = CommonConfig.ZIZHUCHARGE+"/general/sendmsgdetails?source=1&id="+codeid;
				User meruser = UserHandler.getUserInfo(merid);
				String phone = "";
				if(meruser!=null) phone = meruser.getPhoneNum();
				JSONObject json = new JSONObject();
				json.put("first", TempMsgUtil.inforData(info,"#FF3333"));
				json.put("keyword1", TempMsgUtil.inforData(code,"#0044BB"));
				json.put("keyword2", TempMsgUtil.inforData(StringUtil.toDateTime("yyyy-MM-dd HH:mm:ss", beginTime),"#0044BB"));
				json.put("keyword3", TempMsgUtil.inforData(StringUtil.toDateTime("yyyy-MM-dd HH:mm:ss", endTime),"#0044BB")); 
				json.put("remark", TempMsgUtil.inforData("如有疑问请联系商户："+phone,"#0044BB"));
				sendTempMsg(oppenid, TEMP_IDEND, urltem, json);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**
	 * @Description： 退费消息模板发送
	 * @author： origin  
	 */
	public static void returnMsgTemp( String first, String oppenid, String servername, String ordernum, 
			String time, Double money, String phone, String remark, String url ){//退费模板
		try {
			if(null!=oppenid){
				JSONObject json = new JSONObject();
				json.put("first", TempMsgUtil.inforData(first,"#FF3333"));
				json.put("keyword1", TempMsgUtil.inforData(servername,"#0044BB"));
				json.put("keyword2", TempMsgUtil.inforData(ordernum,"#0044BB"));
				json.put("keyword3", TempMsgUtil.inforData(time,"#0044BB")); 
				json.put("keyword4", TempMsgUtil.inforData("¥ "+String.format("%.2f", money),"#0044BB"));
				json.put("keyword5", TempMsgUtil.inforData(phone,"#0044BB"));
				json.put("remark", TempMsgUtil.inforData(remark,"#0044BB"));
				sendTempMsg(oppenid, TempMsgUtil.TEMP_IDTUI, null, json);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**
	 * @Description： 退费消息模板发送
	 * @author： origin  
	 */
	public static void returnMsg(String message,Integer uid, String phone, String order, String url, String time, Double money){//退费模板
		try {
			User user = UserHandler.getUserInfo(uid);
			if(null!=user){
				String oppenid = user.getOpenid();
				JSONObject json = new JSONObject();
				json.put("first", TempMsgUtil.inforData("您好，您的退费请求已受理，资金将原路返回到您的账户中","#FF3333"));
				json.put("keyword1", TempMsgUtil.inforData(message,"#0044BB"));
				json.put("keyword2", TempMsgUtil.inforData(order,"#0044BB"));
				json.put("keyword3", TempMsgUtil.inforData(time,"#0044BB")); 
				json.put("keyword4", TempMsgUtil.inforData("¥ "+String.format("%.2f", money),"#0044BB"));
				json.put("keyword5", TempMsgUtil.inforData(phone,"#0044BB"));
				json.put("remark", TempMsgUtil.inforData("如有疑问可咨询服务商。","#0044BB"));
				sendTempMsg(oppenid, TempMsgUtil.TEMP_IDTUI, url, json);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**结算账单通知*/
	public static final String TEMP_BILLINCOMEPUSH = "9MQt9aCaFfuBnIznK0xnXhWX3ZeD7MUMo1nPtYCL8LQ";

	public static void dealerIncomePush(String oppenid, String dealernick, String dealerreal, Double incomemoney, 
			String earningMoney, String time) {
		try {
			JSONObject json = new JSONObject();
			json.put("first", TempMsgUtil.inforData("尊敬的商户您好，昨日线上收益已汇总。","#FF3333"));
			json.put("keyword1", TempMsgUtil.inforData("线上收益","#0044BB"));
//			json.put("keyword2", TempMsgUtil.inforData("¥ "+incomemoney.toString(),"#0044BB"));
			json.put("keyword2", TempMsgUtil.inforData(incomemoney.toString() + "元","#0044BB"));
			json.put("keyword3", TempMsgUtil.inforData(time,"#0044BB")); 
			json.put("remark", TempMsgUtil.inforData("如有疑问请及时咨询。感谢您的支持。","#0044BB"));
			sendTempMsg(oppenid, TempMsgUtil.TEMP_BILLINCOMEPUSH, null, json);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	

	/**设备报警通知*/ //@ORIGIN
	public static final String TEMP_ALARMMESS = "juwvgaa_MrFRJQyPxR4ZUH31vLfGYSkn0UseHhBDBFQ";
	/**
	 * @Description：
	 * @author： origin  2020年7月1日下午6:10:04
	 */
	public static void giveDealerAnAlarmMess(String first, String openid, String deviceData, String devicenum,
			String feedback, String time, String url) {
		try {
			JSONObject json = new JSONObject();
			json.put("first", TempMsgUtil.inforData( first, "#FF3333"));
			json.put("keyword1", TempMsgUtil.inforData( deviceData, "#0044BB"));
			json.put("keyword2", TempMsgUtil.inforData( devicenum, "#0044BB"));
			json.put("keyword3", TempMsgUtil.inforData( feedback, "#0044BB"));
			json.put("keyword4", TempMsgUtil.inforData( time, "#0044BB")); 
			json.put("remark", TempMsgUtil.inforData( "请及时查看处理。感谢您的使用。", "#0044BB"));
			sendTempMsg(openid, TempMsgUtil.TEMP_ALARMMESS, null, json);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		
	}
	
	public static void sendTempMsg(String ToUser, String template_id, String url,  JSONObject jsonb) {
		JSONObject json = new JSONObject();
		json.put("touser", ToUser);
		json.put("template_id", template_id);
		json.put("data", jsonb);
		json.put("url", url);//已经被注释了
		String urlpo = null;
		try {
			urlpo = TempMsgUtil.TEMP_MSG_URL.replace("ACCESS_TOKEN", WeixinUtil.getBasicAccessToken());
		} catch (Exception e) {
			e.printStackTrace();
		}
		PostSendMsg(json, urlpo);
	}
	
//	public static JSONObject information1(String ToUser, String template_id, String url, String first, String keyword1, String keyword2, String keyword3, String keyword4, String remark) {
//		JSONObject json = new JSONObject();
//		JSONObject jsonb = new JSONObject();
//		json.put("touser", ToUser);
//		json.put("template_id", template_id);
//		jsonb.put("first", inforData("您好，你有新的消息："+first,"#FF3333"));
//		jsonb.put("date", inforData(keyword1,"#0044BB"));
//		jsonb.put("adCharge", inforData(keyword2,"#0044BB"));
//		jsonb.put("type", inforData(keyword3,"#0044BB"));
//		jsonb.put("cashBalance", inforData(keyword4,"#0044BB"));
//		jsonb.put("remark", inforData(remark,"#0044BB"));
//		json.put("data", jsonb);
//        
//		//json.put("url", url);
//		return json;
//	}
	public static JSONObject information2(String ToUser, String template_id, String url, String first, String keyword1, String keyword2, String keyword3, String remark) {
		JSONObject json = new JSONObject();
		JSONObject jsonb = new JSONObject();
		json.put("touser", ToUser);
		json.put("template_id", template_id);
		jsonb.put("first", inforData("您好:"+first,"#FF3333"));
		jsonb.put("keyword1", inforData(keyword1,"#0044BB"));
		jsonb.put("keyword2", inforData(keyword2,"#0044BB"));
		jsonb.put("keyword3", inforData(keyword3,"#0044BB"));
		jsonb.put("remark", inforData(remark,"#0044BB"));
		json.put("data", jsonb);
		
		//json.put("url", url);
		return json;
	}
	
	public static JSONObject inforData( String info, String color){
		JSONObject jsobj = new JSONObject();
		jsobj.put("value", info);
		jsobj.put("color", color);		
		return jsobj;
	}
	
	public static String PostSendMsg(JSONObject json, String url) {
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json");
		post.addHeader("Authorization", "Basic YWRtaW46");
		String result = "";
		try {
			StringEntity s = new StringEntity(json.toString(), "utf-8");
			s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(s);
			// 发送请求
			HttpResponse httpResponse = HttpClients.createDefault().execute(post);
			// 获取响应输入流
			InputStream inStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
			StringBuilder strber = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
				strber.append(line + "\n");
			inStream.close();

			result = strber.toString();
			System.out.println(result);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				System.out.println("请求服务器成功，做相应处理");
			} else {
				System.out.println("请求服务端失败");
			}
		} catch (Exception e) {
			System.out.println("请求异常");
			throw new RuntimeException(e);
		}
		return result;
	}

}
