package com.hedong.hedongwx.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.hedong.hedongwx.entity.TemplateSon;

import net.sf.json.JSON;
import net.sf.json.JSONObject;


/**
 * @author  origin
 * 创建时间：   2019年7月8日 上午10:13:30  
 */
public class CommUtil {
	
	/**
	 * @method_name: gainVersionsType
	 * @Description: 根据版本号获取赋值同类型设备的所有版本信息
	 * @param versions
	 * @Author: origin  创建时间:2020年8月29日 下午2:39:16
	 * @common:
	 */
	public static String gainVersionsType(String versions){
		String versiontype = null;
		try {
			if("00".equals(versions) || "01".equals(versions) || "05".equals(versions) || "06".equals(versions)){//00:出厂默认  01:十路智慧款   05:16路智慧款   06:20路智慧款
				versiontype = "00,01,05,06";
			}else if("02".equals(versions)){//02:电轿款   07:单路交流桩
				versiontype = "02,07";
			}else if("03".equals(versions)){//03:脉冲投币
				versiontype = "03";
			}else if("04".equals(versions)){//04:离线充值机
				versiontype = "04";
			}else if("08".equals(versions)){//08:新版10路智慧款V3
				versiontype = "08";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versiontype ;
	}
	
	public static Map<String, Object> chargeConvert( Integer paystatus, Integer paytype){
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			/* 
			ordersource 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、钱包-5、在线卡-6、包月支付
			orderpaytype 支付方式   1、钱包2、微信3、支付宝 、4微信小程序、5支付宝小程序 6虚拟充值 12银联app
			31:包月    32:投币    33:离线卡  34:在线卡  35:
			orderstatus  订单状态 1：正常    2：全额退款    3：部分退款
			*/
			Integer ordersource = 1;
			Integer orderstatus = 1;
			Integer orderpaytype = null;
			
			//private Integer number;//退款状态0:正常    1 全额退款、2部分退款
			if(paystatus.equals(0)){//正常
				orderstatus = 1;
			}else if(paytype.equals(1)){//微信
				orderstatus = 2;
			}else if(paytype.equals(2)){//部分退款
				orderstatus = 3;
			}
			//消费类型： '1:钱包  2:微信  3:支付宝  4:包月下发数据 5:投币 6:离线卡 7:在线卡 8:支付宝小程序'
			if(paytype.equals(1)){//钱包
				orderpaytype = 1;
			}else if(paytype.equals(2)){//微信
				orderpaytype = 2;
			}else if(paytype.equals(3)){//支付宝
				orderpaytype = 3;
			}else if(paytype.equals(4)){//包月
				orderpaytype = 31;
			}else if(paytype.equals(5)){//投币
				orderpaytype = 32;
			}else if(paytype.equals(6)){//离线卡
				orderpaytype = 33;
			}else if(paytype.equals(7)){//在线卡
				orderpaytype = 34;
			}else if(paytype.equals(8)){//支付宝小程序
				orderpaytype = 5;
			}
			result.put("ordersource", ordersource);
			result.put("orderstatus", orderstatus);
			result.put("orderpaytype", orderpaytype);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result ;
	}
	

	public static Map<String, Object> incoinsConvert( Integer paytype){
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			/* 
			ordersource 支付来源1：充电模块	2：脉冲模块		3：离线充值机	4：钱包	5：在线卡	6：包月支付
			orderpaytype 支付方式   1、钱包2、微信3、支付宝 、4微信小程序、5支付宝小程序 6虚拟充值 12银联app
			31:包月    32:投币    33:离线卡  34:在线卡  35:
			orderstatus  订单状态 1：正常    2：全额退款    3：部分退款
			*/
			Integer ordersource = 2;
			Integer orderstatus = 1; //订单状态 1：正常    2：全额退款    3：部分退款
			Integer orderpaytype = null;
			
//			1:通过微信下发投币、2:通过支付宝下发投币、3:设备投币成功信息上传服务器、4:微信退款、5:支付宝退款、6:钱包支付
//			7:钱包退款、8:微小程序、9:微信小程序退款、10:支付宝小程序、11:支付宝小程序退款、12:银联下发投币、 13:银联退款
//			private Byte recycletype;
			if(paytype.equals(1)){//微信
				orderpaytype = 2;
			}else if(paytype.equals(2)){//支付宝
				orderpaytype = 3;
			}else if(paytype.equals(3)){//设备投币
				orderpaytype = 32;
			}else if(paytype.equals(4)){//微信全额退款
				orderpaytype = 2;
				orderstatus = 2;
			}else if(paytype.equals(5)){//支付宝全额退款
				orderpaytype = 3;
				orderstatus = 2;
			}else if(paytype.equals(6)){//钱包支付
				orderpaytype = 1;
			}else if(paytype.equals(7)){//钱包退款
				orderpaytype = 1;
				orderstatus = 2;
			}else if(paytype.equals(8)){//微小程序
				orderpaytype = 4;
			}else if(paytype.equals(9)){//微信小程序退款
				orderpaytype = 4;
				orderstatus = 2;
			}else if(paytype.equals(10)){//支付宝小程序
				orderpaytype = 5;
			}else if(paytype.equals(11)){//支付宝小程序退款
				orderpaytype = 5;
				orderstatus = 2;
			}else if(paytype.equals(12)){//银联下发投币
				orderpaytype = 12;
			}else if(paytype.equals(13)){//银联退款
				orderpaytype = 12;
				orderstatus = 2;
			}
			result.put("ordersource", ordersource);
			result.put("orderstatus", orderstatus);
			result.put("orderpaytype", orderpaytype);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result ;
	}
	
	public static Map<String, Object> offlineConvert( Integer paystatus, Integer paytype){
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			/* 
			ordersource 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、钱包-5、在线卡-6、包月支付
			orderpaytype 支付方式   1、钱包2、微信3、支付宝 、4微信小程序、5支付宝小程序 6虚拟充值 12银联app
			31:包月    32:投币    33:离线卡  34:在线卡  35:
			orderstatus  订单状态 1：正常    2：全额退款    3：部分退款
			*/
			Integer ordersource = 3;
			Integer orderstatus = 1;
			Integer orderpaytype = null;
			/** 支付类型————1-wx、2-alipay、3-wx refund、4-alipay refund、5-wallet、6-wallet refund 
			 * 7.刷卡消费  8.支付宝小程序 9支付宝小程序退款*/
			if(paytype.equals(1)){//微信
				orderpaytype = 2;
			}else if(paytype.equals(2)){//支付宝
				orderpaytype = 3;
			}else if(paytype.equals(3)){//微信全额退款
				orderpaytype = 2;
				orderstatus = 2;
			}else if(paytype.equals(4)){//支付宝全额退款
				orderpaytype = 3;
				orderstatus = 2;
			}else if(paytype.equals(5)){//钱包支付
				orderpaytype = 1;
			}else if(paytype.equals(6)){//钱包退款
				orderpaytype = 1;
				orderstatus = 2;
			}else if(paytype.equals(7)){//刷卡消费 [离线卡]
				orderpaytype = 33;
			}else if(paytype.equals(8)){//支付宝小程序
				orderpaytype = 4;
			}else if(paytype.equals(9)){//支付宝小程序退款
				orderpaytype = 5;
				orderstatus = 2;
			}else if(paytype.equals(12)){//银联下发投币
				orderpaytype = 12;
			}else if(paytype.equals(13)){//银联退款
				orderpaytype = 12;
				orderstatus = 2;
			}
			result.put("ordersource", ordersource);
			result.put("orderstatus", orderstatus);
			result.put("orderpaytype", orderpaytype);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result ;
	}
	
	public static Map<String, Object> walletConvert( Integer paystatus, Integer paytype){
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			/* 
			ordersource 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、钱包-5、在线卡-6、包月支付
			orderpaytype 支付方式   1、钱包2、微信3、支付宝 、4微信小程序、5支付宝小程序 6虚拟充值 12银联app
			31:包月    32:投币    33:离线卡  34:在线卡  35:
			orderstatus  订单状态 1：正常    2：全额退款    3：部分退款
			*/
			Integer ordersource = 4;
			Integer orderstatus = 1;
			Integer orderpaytype = null;
			//充值类型： 0:充值钱包  1:虚拟充值钱包   2:钱包退费 3:充电记录退费 4:离线卡充值退费 5:模拟投币退费
			//		 6虚拟钱包退费7支付宝 8支付宝退款  9:支付宝小程序*/
			if(paytype.equals(0)){//微信充值钱包
				orderpaytype = 2;
			}else if(paytype.equals(1)){//虚拟充值钱包
				orderpaytype = 6;
			}else if(paytype.equals(2)){//钱包退费
				orderpaytype = 2;
				orderstatus = 2;
			}else if(paytype.equals(3)){//充电记录退费到钱包
				orderpaytype = 1;
				orderstatus = 2;
			}else if(paytype.equals(4)){//离线卡充值退费到钱包
				orderpaytype = 33;
				orderstatus = 2;
			}else if(paytype.equals(5)){//模拟投币退费到钱包
				orderpaytype = 32;
				orderstatus = 2;
			}else if(paytype.equals(6)){//虚拟钱包退费到钱包
				orderpaytype = 6;
				orderstatus = 2;
			}else if(paytype.equals(7)){//支付宝充值
				orderpaytype = 3;
			}else if(paytype.equals(8)){//支付宝退款
				orderpaytype = 3;
				orderstatus = 2;
			}else if(paytype.equals(9)){//支付宝小程序
				orderpaytype = 5;
			}else if(paytype.equals(10)){//支付宝小程序退款
				orderpaytype = 5;
				orderstatus = 2;
			}
			result.put("ordersource", ordersource);
			result.put("orderstatus", orderstatus);
			result.put("orderpaytype", orderpaytype);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result ;
	}
	
	
	public static Integer convertTrade(Integer paysource, Integer status, Integer paytype){
		try {
//			/** 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、钱包-5、在线卡-6、包月支付*/
//			private Integer paysource;
//			/** 支付方式1、钱包2、微信3、支付宝 、4微信小程序、5支付宝小程序 6虚拟充值*/
//			private Integer paytype;
//			/** 订单状态1、正常2、退款 */
			Integer ordersource = null;
			Integer orderstatus = null;
			Integer orderpaytype = null;
			if(paysource==1){//充电
				/*
				private Integer number;//退款状态0:正常    1 全额退款、2部分退款
				private Integer paytype;//消费类型： '1:钱包  2:微信  3:支付宝  4:包月下发数据 5:投币 6:离线卡 7:在线卡 8:支付宝小程序'
				private Integer status;//充电时的状态
				 */
				//消费类型： '1:钱包  2:微信  3:支付宝  4:包月下发数据 5:投币 6:离线卡 7:在线卡 8:支付宝小程序'
				if(paytype.equals(5) && status==1){
					paytype = 8;
				}else if(paytype.equals(5) && status==2){
					paytype = 9;
				}
				status = 0;
			}else if(paysource==2){//脉冲
				status = 7;
			}else if(paysource==3){//离线充值机
				status = 9;
			}else if(paysource==4){//钱包
				status = 2;
			}else if(paysource==5){//在线卡
				status = 1;
			}else if(paysource==6){//包月
				status = 6;
			}else if(paysource==7){
				status = 3;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status ;
	}
	
	
	public static Integer tradeConvert(Integer paysource, Integer status, Integer paytype){
		try {
//			/** 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、钱包-5、在线卡-6、包月支付*/
//			private Integer paysource;
//			/** 支付方式1、钱包2、微信3、支付宝 、4微信小程序、5支付宝小程序 6虚拟充值*/
//			private Integer paytype;
//			/** 订单状态1、正常2、退款 */
			Integer ordersource = paysource;
			Integer orderstatus = paysource;
			Integer orderpaytype = paytype;
			if(paysource==1){//充电
				//消费类型： '1:钱包  2:微信  3:支付宝  4:包月下发数据 5:投币 6:离线卡 7:在线卡 8:支付宝小程序'
				if(paytype.equals(5) && status==1){
					paytype = 8;
				}else if(paytype.equals(5) && status==2){
					paytype = 9;
				}
				status = 0;
			}else if(paysource==2){//脉冲
				status = 7;
			}else if(paysource==3){//离线充值机
				status = 9;
			}else if(paysource==4){//钱包
				status = 2;
			}else if(paysource==5){//在线卡
				status = 1;
			}else if(paysource==6){//包月
				status = 6;
			}else if(paysource==7){
				status = 3;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status ;
	}
	
	
	/**
	 * @Description：在线卡、钱包消费逻辑处理
	 * @param consumemoney: 消费金额     operationtype:操作类型[0:扣费    1:回收退费]
	 * @author： origin 2020年6月28日上午11:25:32
	 * 2000	处理数据成功
	 * 3000  处理出现异常(异常错误)
	 * 3002	处理类型错误（传过来的处理类型不正确）
	 * 3003	在线卡金额不足
	 */
	public static Map<String, Object> onlinecardDataDispose( Double topupmoney, Double sendmoney, Double consumemoney, Integer operationtype) {
		Map<String, Object> resultdata = new HashMap<>();
		try {
			//数据处理
			//============================================================================
			Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额
			//============================================================================
			Double opermoney = consumemoney;//操作总额
			Double opertopupmoney = 0.00;	//操作充值金额
			Double opersendmoney = 0.00;	//操作赠送金额
			//==========================================================================
			Double topupbalance = 0.00;//充值余额
			Double sendbalance = 0.00;//赠送余额
			Double accountbalance = 0.00;//账户余额
			//==========================================================================
			if(operationtype==0){
				Double comparemoney = CommUtil.subBig(accountmoney, opermoney); //总额、操作比较金额差
				if(comparemoney>=0){//金额足够，扣费
					Double contrastmoney = CommUtil.subBig(topupmoney, opermoney); //充值、操作比较金额差
					if(contrastmoney>=0){//此时说明用户充值金额足够
						opertopupmoney = opermoney;
						topupbalance = contrastmoney;
						opersendmoney = 0.00;
						sendbalance = sendmoney;
					}else{
						opertopupmoney = topupmoney;
						topupbalance = 0.00;
						opersendmoney = CommUtil.toDouble(Math.abs(contrastmoney));
						sendbalance = CommUtil.subBig(sendmoney, opersendmoney);
					}
					resultdata.put("code", 2000);
					resultdata.put("message", "金额扣费完成");
				}else{
					topupbalance = topupmoney;
					sendbalance = sendmoney;
					resultdata.put("code", 3003);
					resultdata.put("message", "金额不足");
				}
			}else if (operationtype==1){
				if(topupmoney>0) {
					opertopupmoney = opermoney;
					topupbalance = CommUtil.addBig(topupmoney, opertopupmoney);
					opersendmoney = 0.00;
					sendbalance = sendmoney;
				}else{
					opertopupmoney = 0.00;
					topupbalance = 0.00;
					opersendmoney = opermoney;
					sendbalance = CommUtil.addBig(sendmoney, opersendmoney);
				}
				resultdata.put("code", 2000);
				resultdata.put("message", "回收金额成功");
			}else{
				topupbalance = topupmoney;
				sendbalance = sendmoney;
				resultdata.put("code", 3002);
				resultdata.put("message", "处理类型错误");
			}
			accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
			resultdata.put("topupmoney", topupmoney);
			resultdata.put("sendmoney", sendmoney);
			resultdata.put("accountmoney", accountmoney);
			resultdata.put("opermoney", opermoney);
			resultdata.put("opertopupmoney", opertopupmoney);
			resultdata.put("opersendmoney", opersendmoney);
			resultdata.put("topupbalance", topupbalance);
			resultdata.put("sendbalance", sendbalance);
			resultdata.put("accountbalance", accountbalance);
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			resultdata.put("code", 3000);
			resultdata.put("message", "处理出现异常");
			return resultdata;
		}
		
	}
	
	/**
	 * @method_name: getRequestParam
	 * @Description: 将获取的request对象转换为Map<String,Object>格式数据
	 * @param request 参数
	 * @return Map<String,Object>格式数据
	 * @Author: origin  创建时间:2020年8月29日 下午3:58:12
	 * @common: normal employ
	 */
	public static Map<String,Object> getRequestParam(HttpServletRequest request){
		Map<String,Object> res = new HashMap<String,Object>();
		try {
			Map<String,String[]> map = request.getParameterMap();
			if(map==null||map.isEmpty()){
				return new HashMap<String, Object>();
			}
			Set<String> keys = map.keySet();
			for(String key : keys){
				String[] value = map.get(key) ;
				String v = value!=null&&value.length>0?value[0]:"";
				res.put(key, v) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res ;
	}
	
	public static Map<String,Object> isMapEmpty(Map<String, Object> result){
		try {
			if(result==null||result.isEmpty()){
				return new HashMap<String, Object>();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String,Object>();
		}
	}
	
	public static List<Map<String, Object>> isListMapEmpty(List<Map<String, Object>> result){
		try {
			if(result==null||result.isEmpty()){
				return new ArrayList<>();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	public static boolean isNull(String stringVal) {
		return isNull(stringVal, true);
	}
	
	/**
	 * 判断某个字符是否为空，空字符串作为可选项
	 * 
	 * @param stringVal
	 * @param includeBlankString
	 * @return
	 */
	public static boolean isNull(String stringVal, boolean includeBlankString) {
		if (stringVal == null)
			return true;
		if (includeBlankString && stringVal.trim().equals(""))
			return true;
		return false;
	}
	// --------------------------------------日期相关的字符串处理-------------------------------------
	/**
	 * @Description： 获取指定类型（formatString）的String时间
	 * @author： origin 
	 */
	public static String toDateTime(String formatString) {
		try {
			if (StringUtil.isNull(formatString))
			return "";
			Date d = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat(formatString);
			return formatter.format(d);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * @Description：  将Date类型转换为指定类型（formatString）的String时间
	 * @author： origin 
	 * @return 返回String类型日期时间
	 */
	public static String toDateTime(String formatString, Date d) {
		try {
			if (StringUtil.isNull(formatString))
				return "";
			if (d == null)
				return "";
			SimpleDateFormat formatter = new SimpleDateFormat(formatString);
			return formatter.format(d);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * @Description：  将String类型转换为指定类型（formatString）的Data时间
	 * @author： origin 
	 * @return 返回Date类型日期时间
	 */
	public static Date DateTime(String time, String formatString){ 
		try {
			if (StringUtil.isNull(time))
			return null;
			if (time.equals(""))
			return null;
			SimpleDateFormat formatter = new SimpleDateFormat(formatString);
			return formatter.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
			return new Date();
		}
	}
		
	/** 获取当前日期时间值         返回当前日期时间字符 如2010-09-18 14:12:15 */
	public static String toDateTime() {
		return toDateTime("yyyy-MM-dd HH:mm:ss");
	}
		
	/** 返回日期时间字符 如2010-09-18 14:12:15  */
	public static String toDateTime(Date d) {
		return toDateTime("yyyy-MM-dd HH:mm:ss", d);
	}

    /**
     * 获取过去第几天的日期
     * @param past
     * @return Date时间
     */
    public static Date getPastTime(int past) {
    	try {
    		Calendar calendar = Calendar.getInstance();
    		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
    		Date today = calendar.getTime();
    		return today;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
	/**
     * 获取过去第几天的日期
     * @param past
     * @return String 时间
     */
    public static String getPastDate(int past) {
    	try {
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
	        Date today = calendar.getTime();
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        String result = format.format(today);
	        return result;
    	} catch (Exception e) {
			e.toString();
			return null;
		}
    }
    
    /**
     * @Description：  获取相应时间
     * @param: time:输入的时间		numerical:天数（获取几天前时间）   type:类型 1:凌晨0点时间、2:一天结束时间  3:当前时间
     * @author： origin   2019年11月8日 下午5:25:57
     */
    public static String getRelevantDate(Object time, Integer numerical, Integer type) {
    	try {
    		String val = toString(time);
    		if(val==null || val.equals("")){
    			if(type.equals(1)){
    				val = getPastDate(numerical)+ " 00:00:00";
    			}else if(type.equals(2)){
    				val = getPastDate(numerical)+ " 23:59:59";
    			}else{
    				val = toDateTime();
    			}
    		}
	        return val;
    	} catch (Exception e) {
			e.toString();
			return null;
		}
    }
    
    public static Integer compareStringTime(String startdate, String enddate) {
    	int num = 2;
		SimpleDateFormat  sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		try {
        	Date sd = sf.parse(startdate);
            Date ed = sf.parse(enddate);
            if (sd.getTime() > ed.getTime()) {
            	num = 1;
            } else if (sd.getTime() < ed.getTime()) {
            	num = -1;
            } else {
            	num = 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return num;
    }
    
    public static Integer compareTime(String startdate, String enddate) {
		SimpleDateFormat  sf = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
        	Date sd = sf.parse(startdate);
            Date ed = sf.parse(enddate);
//            long differtime = (sd.getTime() - ed.getTime());
            long sdtime = sd.getTime();
            long edtime = ed.getTime();
            long differtime = (sdtime - edtime);
            Integer time = (int) (differtime/(1000*60*60*24));
            return time;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * 时间比较（比较制定两个时间的差）
     * @author  origin          
     * @version 创建时间：2019年3月5日  下午2:30:37
     */
	public static Integer differentDay(String startTime, String endTime){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
		try {
			startDate = sdf.parse(startTime);
			endDate=sdf.parse(endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
        Calendar stcalen = Calendar.getInstance();
        stcalen.setTime(startDate);
        Calendar encalen = Calendar.getInstance();
        encalen.setTime(endDate);
        Integer strday= stcalen.get(Calendar.DAY_OF_YEAR);//当前年的第几天     
        Integer endday = encalen.get(Calendar.DAY_OF_YEAR);
        Integer stryear = stcalen.get(Calendar.YEAR);//当前年 
        Integer endyear = encalen.get(Calendar.YEAR);
        Integer timeDistance = 0 ;
        if(stryear != endyear){//不同年
            for(int i=stryear; i<endyear; i ++){
                if(i%4==0 && i%100!=0 || i%400==0){//闰年
                    timeDistance += 366;
                }else{//不是闰年
                    timeDistance += 365;
                }
            }
            timeDistance += endday-strday;
            return timeDistance;
        }else{//同年
            timeDistance = endday-strday;
            return timeDistance;
        }
    }
	
	public static boolean isToday(Date inputJudgeDate) {
		boolean flag = false;
		//获取当前系统时间
		long longDate = System.currentTimeMillis();
		Date nowDate = new Date(longDate);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = dateFormat.format(nowDate);
		String subDate = format.substring(0, 10);
		//定义每天的24h时间范围
		String beginTime = subDate + " 00:00:00";
		String endTime = subDate + " 23:59:59";
		Date paseBeginTime = null;
		Date paseEndTime = null;
		try {
			paseBeginTime = dateFormat.parse(beginTime);
			paseEndTime = dateFormat.parse(endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(inputJudgeDate.after(paseBeginTime) && inputJudgeDate.before(paseEndTime)) {
			flag = true;
		}
		return flag;
	}

	public static boolean isTime(Date date, Integer hour) {
		boolean flag = false;
		//获取当前系统时间
		long longDate = System.currentTimeMillis();
		long datetime = date.getTime();
		long secondDiffer = (longDate - datetime)/1000;
//		minute
		long hourDiffer = secondDiffer/3600;
		if(hourDiffer>=2){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * @method_name: isGtNowTime
	 * @Description: 判断指定时间是否小于当前时间
	 * @param date 指定的时间
	 * @Author: origin  创建时间:2020年7月24日 下午6:20:30
	 * @common:
	 */
	public static boolean isLtNowTime(Date date) {
		boolean flag = false;
		try {
			Date datatime = new Date();
			//指定时间不为空且当前时间大于指定时间
			if(date != null && datatime.after(date)){
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	
	public static List<String> StringList(String val) {
		List<String> list = new ArrayList<String>();
		JSONArray listObject = JSONArray.parseArray(val);
        for (Object item : listObject){
        	list.add(CommUtil.toString(item));
        }
		return list;
	}
	
	// 字符串处理
	public static List<String> jsonList(String val) {
		List<String> list = new ArrayList<String>();
		val = val.replace("[", "").replace("]", "").replace( "\"", "");
		//["000008", "000010", "000012"]
		Integer a = val.length();
		for (int i = 0; i < a; i++) {
			if(val.indexOf(",")!=-1){
				Integer index = val.indexOf(",");
				list.add(val.substring(0, index));
				val = val.substring(index+1);
			}else{
				list.add(val);
				a = 0;
			}
		}
		return list;
	}
	
	public static JSON toJson(Map<String, Object> map){
		Map<String, String> newmap = new HashMap<>();
        for(String s : map.keySet()){ 
        	newmap.put(s, map.get(s).toString());
        }
        JSONObject jsonMap = JSONObject.fromObject(newmap);
		return jsonMap;
	 }
	
	public static JSON toListJson(List<Map<String, Object>> listmap){
		JSONArray json = new JSONArray();
		for(Map<String,Object> item : listmap){
			Map<String, String> newmap = new HashMap<>();
            for(String s : item.keySet()){ 
            	newmap.put(s, item.get(s).toString());
            }
            JSONObject jsonMap = JSONObject.fromObject(newmap);
			json.add(jsonMap);
        }
		return (JSON) json;
	 }
	
	public static String toForHex(Integer val) {
		StringBuffer str = new StringBuffer("00000000");
		//str.append("0X");toUpperCase
		try {
			String sca = Integer.toHexString(val).toUpperCase();
			Integer num = sca.length();
			Integer marknum = str.length();
			if(sca.length()<8) sca = str.replace(marknum-num, marknum, sca).toString();
			return sca.toString();
		} catch (Exception e) {
			return str.toString();
		}
	}
	
	public static String strRepAdd(String strbud, String val, Integer divisor){
		StringBuilder strnew = new StringBuilder();
		Integer len = strbud.length();
		Integer mulnum = len/divisor;
		for(int i=0;i<=mulnum; i++){
			int a = i+1;
			String strs = null;
			if(i<mulnum){
				strs = strbud.substring(i*divisor, a*divisor);
				strnew.append(strs+val);
			}else{
				strs = strbud.substring(i*divisor); 
				strnew.append(strs);
			}
        }
		return strnew.toString();
    }
	
	public static Boolean isEmpty(Object val){
		try {
			String str = val == null ? null : val.toString().trim();
			return (str == null) || (str.length() == 0);
		} catch (Exception e) {
			e.printStackTrace();
			return false; 
		}
	}

	public static String toString(Object val){
		String ts = toStringTrim(val);
		return isEmpty(ts) ? null : ts;
	}
	
	public static boolean isEmpty(String str){
		 return (str == null) || (str.length() == 0);
	}
	
	 public static String toStringTrim(Object str){
		 return str == null ? null : str.toString().trim();
	 }
	 
	 public static String trimToEmpty(Object str){
		 return str == null ? "" : str.toString().trim();
	 }
	
	 public static Integer forInteger(Object val){
		 if (val == null || val.toString().trim() == ""){
			 return null;
		 }else{
			return forLong(val).intValue();
		 }
	 }
	public static Long forLong(Object val){
		if (val == null || val.toString().trim() == ""){
			return null;
		}else{
			return forDouble(val).longValue();
		}
	}
		
	public static Double forDouble(Object val){
		if (val == null || val.toString().trim() == ""){
			return null;
		}else{
			try {
				Double dol = Double.valueOf(val.toString().trim());
				BigDecimal bde = new BigDecimal(dol);
				dol = bde.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();   
				return dol;
			} catch (Exception e) {
				return null;
			}
		}
	}
		 
		public static Float forFloat(Object val){
			return toDouble(val).floatValue();
		}
	
	public static Integer toInteger(Object val){
		return toLong(val).intValue();
	}

	public static Long toLong(Object val){
		return toDouble(val).longValue();
	}
	
	public static Double toDouble(Object val){
		if (val == null){
			return 0.00D;
		}else if (val == ""){
			return 0.00D;
		}else if (val.toString().trim() == ""){
			return 0.00D;
		}
		try {
			Double dol = Double.valueOf(val.toString().trim());
			BigDecimal bde = new BigDecimal(dol);
			dol = bde.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();   
			return dol;
		} catch (Exception e) {
			return 0.00D;
		}
	}
	 
	public static Float toFloat(Object val){
		return toDouble(val).floatValue();
	}
	 
	public static BigDecimal toBigDecimal(Object val){
		BigDecimal ret = new BigDecimal("0");
		if (val == null){
			return ret;
		}
		try {
			return new BigDecimal(val.toString().trim());
		} catch (Exception e) {
			return ret;
		}
	}
	
	 // 进行加法运算（保留两位小数）
    public static double addBig(double d1, double d2){
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.add(b2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    // 进行减法运算
    public static double subBig(double d1, double d2){
    	BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.subtract(b2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    

    //保留小数
    public static double decimals(Integer digit, double value){
    	BigDecimal val = new BigDecimal(value);
        return val.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    public static BigDecimal togetBigDecimal(Object value) {
        BigDecimal ret = new BigDecimal("0");
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {//String类型
                ret = new BigDecimal((String) value).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {//double
                ret = new BigDecimal(((Number) value).doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
            }
        }
        return ret;
    }
	
	public static String getExceptInfo(Exception e){
		String str = "";
		try {
			str = "【输出异常信息】   "+e.toString()+ "\r\n";
			StackTraceElement[] trace = e.getStackTrace();
			for (StackTraceElement s : trace) {
				if(s.toString().contains("com.hedong")){
					str += "\tat " + s + "\r\n";
				}
			}
		} catch (Exception ex) {
			str = "【异常信息转换输出错误】   "+ex.toString();
		}
		return str;
	}
	
	
	public static Map<String, Object> responseBuild(Integer code, String message, Object result, Map<String, Object> map){
		map = map == null ? new HashMap<String, Object>() : map;
		try {
			map.put("result_code", toInteger(code));
			map.put("result_info", toString(message));
			map.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static Map<String, Object> responseBuildInfo(Integer code, String message, Map<String, Object> map){
		map = map == null ? new HashMap<String, Object>() : map;
		try {
			map.put("result_code", toInteger(code));
			map.put("result_info", toString(message));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static Map<String, Object> responseBuild(Integer code, String message, Object result){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("result_code", toInteger(code));
			map.put("result_info", toString(message));
			map.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	public static HashMap<String, Object> toHashMaps(Object object) {  
	       HashMap<String, Object> data = new HashMap<String, Object>();  
	       // 将json字符串转换成jsonObject  
	       JSONObject jsonObject = JSONObject.fromObject(object);  
	       Iterator it = jsonObject.keys();  
	       // 遍历jsonObject数据，添加到Map对象  
	       while (it.hasNext()){  
	           String key = String.valueOf(it.next());  
	           Object value = jsonObject.get(key);  
	           data.put(key, value);  
	       }  
	       return data;  
	   }  
	
	
	@SuppressWarnings("rawtypes")
	public static HashMap<String, String> toHashMap(Object object) {  
	       HashMap<String, String> data = new HashMap<String, String>();  
	       // 将json字符串转换成jsonObject  
	       JSONObject jsonObject = JSONObject.fromObject(object);  
	       Iterator it = jsonObject.keys();  
	       // 遍历jsonObject数据，添加到Map对象  
	       while (it.hasNext())  
	       {  
	           String key = String.valueOf(it.next());  
	           String value = (String) jsonObject.get(key);  
	           data.put(key, value);  
	       }  
	       return data;  
	   }  
	/****************************************************************************************/

	public Map<String, Object> historyStatisticsData(HttpServletRequest request) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			
			Map<String, String[]> m = new HashMap<String, String[]>(request.getParameterMap());
			m.put("abc", new String[] { "1" });
			//request = new ParameterRequestWrapper(request, m);
			if(maparam.isEmpty()) return CommUtil.responseBuildInfo(102, "参数传递不正确或为空", resultdata);
			Integer rankmoney =  CommUtil.toInteger(maparam.get("rankmoney"));
			String nickname =  CommUtil.toString(maparam.get("nick"));
			
			CommUtil.responseBuildInfo(200, "成功", resultdata);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", resultdata);
		}
		return resultdata;
	}
	
	/**
	 * @Description：根据设备号(或字段)判断模板类型
	 * @param versions   0:出厂默认  1:离线充值机  2:脉冲投币  3:钱包模板  4:系统在线卡充值  5:系统包月模板  6:系统v3模板  
	 * 					 7:十路智慧款（16路智慧款、20路智慧款） 8:系统大功率模板   9:系统电轿款模板（单路交流桩）
	 * @return Integer 类型的 status返回值
	 * @author： origin  2020年7月4日下午6:18:59
	 * @comment:
	 */
	public static String createTempName(String versions){
		String name = null;
		try {
			/** 设备硬件版本 00-默认出厂版本、01-十路智慧款、02-电轿款、03-脉冲投币、04-离线充值机、
			 * 05-16路智慧款、06-20路智慧款、07单路交流桩 、08新版10路智慧款V3*/
			if(versions=="00"){//00:出厂默认
				name = "默认充电模板";
			}else if(versions=="01" || versions=="05" || versions=="06"){//01:十路智慧款   05:16路智慧款   06:20路智慧款
				name = "充电模板";
			}else if(versions=="02"){//02:电轿款   07:单路交流桩
				name = "电轿款模板";
			}else if(versions=="03"){//03:脉冲投币
				name = "脉冲模板";
			}else if(versions=="04"){//04:离线充值机
				name = "离线充值机模板";
			}else if(versions=="08"){//08:新版10路智慧款V3
				name = "v3模板";
			}else if(versions=="钱包"){
				name = "钱包模板";
			}else if(versions=="在线卡"){
				name = "在线卡模板";
			}else if(versions=="包月"){
				name = "包月模板";
			}else if(versions=="大功率"){
				name = "大功率模板";
			}
			name = "大功率模板";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name ;
	}
	
//	public static Map<String, Object> get(String version, String code, Integer port, Double money3, elec time, elec elec) {
//		Map<String, Object> resultdata = new HashMap<String, Object>();
//		try {
//			if ("07".equals(version) || "08".equals(version)) {
//				logger.info("port=" + port + "--money3=" + money3 + "--time=" + time + "--elec=" + elec + "--code" + code);
//				SendMsgUtil.send_0x27((byte)port, (short)(money3 / 10), (short)time, (short)elec, code, (byte)1);
//				if ("08".equals(version)) {
//					double clacMoney = money;
//					if (clacMoney == 0) {
//						clacMoney = user.getBalance();
//					}
//					SendMsgUtil.resetChargeData(code, port, uid, clacMoney, 0);
//				}
////				WolfHttpRequest.sendNewChargePaydata(port, time, money/10 + "", chargeRecord.getQuantity() + "", code, 1, 0);
//			} else {
//				SendMsgUtil.send_0x14(portchoose, (short) (money3 / 10), time, elec, code);// 支付完成充电开始
////				WolfHttpRequest.sendChargePaydata(port, time, money/10 + "", chargeRecord.getQuantity() + "", code, 0);
//			}
//			Timer timer = new Timer();
//			long session_id = System.currentTimeMillis();
//			timer.schedule(new TimerTask() {
//				@Override
//				public void run() {
//					chargepayTask(code, (byte) port, chargeRecord.getDurationtime(), chargeRecord.getQuantity(), session_id, format);
//				}
//			}, 60000);
//			Server.chargeTimerNumMap.put(session_id, 2);
//			System.out.println("定时任务已开启");
//		} catch (Exception e) {
//			StackTraceElement[] stackTrace = e.getStackTrace();
//			logger.warn("设备编号：--" + code + "--设备端口：--" + port + "--充电异常---" + stackTrace[0].getLineNumber());
//			e.printStackTrace();
//		}
//		return resultdata;
//	}
//	
	
	
	public static Map<String, Object> getEquipmentnum(String equipmentnuminfor) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			Integer equlengch = CommUtil.toInteger(equipmentnuminfor.length());
			String equipmentnum = null;
			String port = null;
			if(equlengch.equals(6)){
				equipmentnum =  equipmentnuminfor;
				resultdata.put("QRcodeType", "device");
			}else if(equlengch.equals(7) || equlengch.equals(8)){
				equipmentnum =  equipmentnuminfor.substring( 0, 6);
				port = CommUtil.toString(equipmentnuminfor.substring(6));
				resultdata.put("QRcodeType","port");
			}else{
				return CommUtil.responseBuildInfo(2004, "设备号传递不正确", resultdata);
			}
			resultdata.put("port", port);
			resultdata.put("equipmentnum", equipmentnum);
			CommUtil.responseBuildInfo(200, "成功", resultdata);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(3000, "异常错误", resultdata);
		}
		return resultdata;
	}
	
	/**
	 * @method_name: verifyEquipment
	 * @Description: 校验设备是否可以使用（如：在线状态、绑定与否、是否到期）
	 * @param bindtype 绑定状态 0:未绑定   1:绑定
	 * @param state 在线状态 0: 不在线   1:在线
	 * @param expirationtime : 设备到期时间
	 * @return Map<String, Object> resultdata 类型数据；判断code不为200 全部为不可使用
	 * @Author: origin  创建时间:2020年7月25日 下午3:56:01
	 * @common:   
	 */
	public static Map<String, Object> verifyEquipment(Integer deviceType, Integer bindtype, Integer state, Date expirationtime) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			bindtype =  CommUtil.toInteger(bindtype);
			state =  CommUtil.toInteger(state);
			if(deviceType!=2){//设备类型   1:联网模块   2:蓝牙模块
				if(state==0) return CommUtil.responseBuildInfo(2001, "设备离线", resultdata);
				if(bindtype==0) return CommUtil.responseBuildInfo(2002, "设备未绑定", resultdata);
			}
			//设备到期时间不为空且当前时间大于到期时间
			if(expirationtime != null && new Date().after(expirationtime)){
				resultdata.put("expirationtime", expirationtime);
				resultdata.put("expirationstatus", 0);
				return CommUtil.responseBuildInfo(2003, "设备已到期", resultdata);
			}
			return CommUtil.responseBuildInfo(200, "成功", resultdata);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(3000, "异常错误", resultdata);
		}
	}
	
	/**
	 * @method_name: getTempDefaultObje
	 * @Description: 获取模板默认指定对象的索引和选中的对象
	 * @param tempson 需要获得索引的子模板
	 * @param hardversion 设备硬件版本号
	 * @return
	 * @Author: origin  创建时间:2020年7月25日 下午4:09:01
	 * @common:
	 */
	public static Map<String, Object> getTempDefaultObje( List<TemplateSon> tempson, String hardversion) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		int index = 0;
		if(tempson.size()>1){
			if("08".equals(hardversion)){
				for (TemplateSon templateSon : tempson) {
					if (templateSon.getName().contains("2元")) {
						resultdata.put("defaultchoose", templateSon.getId());
						resultdata.put("defaultindex", index);
						break;
					}
					index++;
				}
				if(resultdata.get("defaultchoose")==null){
					resultdata.put("defaultchoose", tempson.get(0).getId());
					resultdata.put("defaultindex", 0);
				}
			}else{
				for (TemplateSon templateSon : tempson) {
					if (index == 0 && !templateSon.getName().contains("1元")) {
						resultdata.put("defaultchoose", templateSon.getId());
						resultdata.put("defaultindex", index);
						break;
					} else if (index == 1) {
						resultdata.put("defaultchoose", templateSon.getId());
						resultdata.put("defaultindex", index);
						break;
					}
					index++;
				}
			}
		}else if(tempson.size()==1){
			resultdata.put("defaultchoose", tempson.get(0).getId());
			resultdata.put("defaultindex", 0);
		}else{
			resultdata.put("defaultchoose", null);
			resultdata.put("defaultindex", -1);
		}
		return resultdata;
	}
	
//	@SuppressWarnings("unchecked")
//	public static void addPortStatus(int len,Map<String,String> codeRedisMap,List<Map<String,String>> portStatus) {
//		for (int i = 1; i < len + 1; i++) {
//			String portStatusStr = codeRedisMap.get(i + "");
//			Map<String,String> portStatusMap = (Map<String, String>) JSON.parse(portStatusStr);
//			
//			portStatus.add(portStatusMap);
//		}
//	}
	
	public static Map<String, Object> historyStatisticsData(String devicenum, String hardversion) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			Map<String, String> codeRedisMap = JedisUtils.hgetAll(devicenum);
			List<Map<String, String>> portStatus = new ArrayList<>();
			if (codeRedisMap != null) {
				if ("02".equals(hardversion)) {
					StringUtil.addPortStatus(2, codeRedisMap, portStatus);
				} else if ("01".equals(hardversion)) {
					StringUtil.addPortStatus(10, codeRedisMap, portStatus);
				} else if ("05".equals(hardversion)) {
					StringUtil.addPortStatus(16, codeRedisMap, portStatus);
				} else if ("06".equals(hardversion)) {
					StringUtil.addPortStatus(20, codeRedisMap, portStatus);
				} else {
					StringUtil.addPortStatus(10, codeRedisMap, portStatus);
				}
			}
			if (portStatus.size() > 0) {
				resultdata.put("allPortSize", portStatus.size());
				Map<String, String> map = portStatus.get(0);
				String updateTimeStr = map.get("updateTime");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long time = sdf.parse(updateTimeStr).getTime();
//						long time = portStatus.get(0).getUpdateTime().getTime(); 
				long currentTime = System.currentTimeMillis();
				if ((currentTime - time) > 300000) {
					resultdata.put("flag", true);
				} else {
					resultdata.put("flag", false);
				}
				resultdata.put("portList", portStatus);
			} else {
				resultdata.put("allPortSize", 0);
				resultdata.put("flag", true);
				resultdata.put("portList", null);
			}
			CommUtil.responseBuildInfo(200, "成功", resultdata);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(3000, "异常错误", resultdata);
		}
		return resultdata;
	}
	
	   /** 
     * 从request中获得参数Map，并返回可读的Map 
     *  
     * @param request 
     * @return 
     */  
    @SuppressWarnings({ "unchecked", "rawtypes" })  
    public static Map getParameterMap(HttpServletRequest request) {  
        // 参数Map  
        Map properties = request.getParameterMap();  
        // 返回值Map  
        Map returnMap = new HashMap();  
        Iterator entries = properties.entrySet().iterator();  
        Map.Entry entry;  
        String name = "";  
        String value = "";  
        while (entries.hasNext()) {  
            entry = (Map.Entry) entries.next();  
            name = (String) entry.getKey();  
            Object valueObj = entry.getValue();  
            if(null == valueObj){  
                value = "";  
            }else if(valueObj instanceof String[]){  
                String[] values = (String[])valueObj;  
                for(int i=0;i<values.length;i++){  
                    value = values[i] + ",";  
                }  
                value = value.substring(0, value.length()-1);  
            }else{  
                value = valueObj.toString();  
            }  
            returnMap.put(name, value);  
        }  
        return returnMap;  
    } 
    
    /**
     * 验证是否是正确合法的手机号码
     * 
     * @param telephone
     *            需要验证的打手机号码
     * @return 合法返回true，不合法返回false
     * */
    public static boolean isCellPhoneNo(String telephone) {
        if (CommUtil.trimToEmpty(telephone)!="") {
            return false;
        }
        if (telephone.length() != 11) {
            return false;
        }
        Pattern pattern = Pattern.compile("^1[3,5]\\d{9}||18[6,8,9]\\d{8}$");
        Matcher matcher = pattern.matcher(telephone);
 
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
    
	/**
	 * 通用判断
	 * @param telNum
	 * @return
	 */
	public static boolean isMobiPhoneNum(String telNum){
		String regex = "^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(telNum);
        return m.matches();
	}
	
	/**
	 * 更严格的判断
	 * @param mobiles
	 * @return  /^1[3|4|5|7|8]\d{9}$/
	 */
	public static boolean isMobileNum(String telNum){
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(telNum);
		return m.matches();
	}
	
	

    /**
     * 大陆号码或香港号码都可以
     * @param str
     * @return 符合规则返回true
     * @throws PatternSyntaxException
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     *
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147,149
     * 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的)
     * 166
     * 17+3,5,6,7,8
     * 18+任意数
     * 198,199
     * @param str
     * @return 正确返回true
     * @throws PatternSyntaxException
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     * @param str
     * @return 正确返回true
     * @throws PatternSyntaxException
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

	
}
