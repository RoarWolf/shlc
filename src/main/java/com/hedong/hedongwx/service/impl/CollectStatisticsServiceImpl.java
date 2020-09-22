package com.hedong.hedongwx.service.impl;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.dao.CodestatisticsDao;
import com.hedong.hedongwx.dao.CollectStatisticsDao;
import com.hedong.hedongwx.entity.Codestatistics;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.service.CollectStatisticsService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.StringUtil;

/**
 * @Description：  汇总统计方法
 * @author  origin  创建时间：   2019年9月7日 下午2:39:50  
 */
@Service
public class CollectStatisticsServiceImpl implements CollectStatisticsService {
	

	@Autowired
	private UserEquipmentService userEquipmentServece;
	@Autowired
	private CodestatisticsDao codestatisticsDao;
	@Autowired
	private CollectStatisticsDao collectStatisticsDao;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Map<String, Object> yesterdaydata(Integer merid, Integer type, String startTime, String endTime) {
		try {
			Map<String, Object> resultmap = dealerIncomeSettlement(merid, startTime, endTime);
			Double wechatmoney = CommUtil.toDouble(resultmap.get("wechatmoney"));
			Double alipaymoney = CommUtil.toDouble(resultmap.get("alipaymoney"));
			Double wechatretmoney = CommUtil.toDouble(resultmap.get("refwechatmoney"));
			Double alipayretmoney = CommUtil.toDouble(resultmap.get("refalipaymoney"));
			Double moneytotal = CommUtil.addBig(wechatmoney, alipaymoney);
			resultmap.put("moneytotal", moneytotal);
			resultmap.put("wechatretmoney", wechatretmoney);
			resultmap.put("alipayretmoney", alipayretmoney);
			return resultmap;
		} catch (Exception e) {
			return new HashMap<String, Object>();
		}
	}
	
	/**
	 * @Description：根据信息计算收益结算情况 1 type: 1 计算商户收益    2:计算设备收益
	 * 				 merid：商户id   time 指定时间    devicenum：设备号
	 * @param time
	 * @author： origin
	 * @createTime：2020年4月15日上午11:05:20
	 */
	@Override
	public void incomeSettlement(Integer type, Integer merid, String time, String devicenum){
		try {
			String begintime = time + " 00:00:00";
			String endtime = time + " 23:59:59";
			
			Codestatistics statistic = new Codestatistics();
			statistic.setMerid(merid);
			statistic.setCountTime(CommUtil.DateTime(time, "yyyy-MM-dd"));

			if(type.equals(1)){//汇总某个商户的收益
				statistic.setType(2);
				Codestatistics collect = codestatisticsDao.selectStatistics(statistic);
				if(collect==null){
					Map<String, Object> resultmap = dealerIncomeSettlement(merid, begintime, endtime);
					incomeSettlement(resultmap, begintime);
				}else if(collect.getConsumequantity()==0 || collect.getConsumetime()==0){//
					Map<String, Object> resultmap = dealerIncomeSettlement(merid, begintime, endtime);
					renewalSettlement( resultmap, begintime);//更新结算信息
				}
			}else if(type.equals(2)){//汇总某个商户下的某个设备收益
				statistic.setType(1);
				statistic.setCode(devicenum);
				Codestatistics collect = codestatisticsDao.selectStatistics(statistic);
				if(collect==null){
					Map<String, Object> resultmap = devivceIncomeSettlement(merid, devicenum, begintime, endtime);
					incomeSettlement( resultmap, begintime);//插入结算信息
				}else if(collect.getConsumequantity()==0 || collect.getConsumetime()==0){
					Map<String, Object> resultmap = devivceIncomeSettlement(merid, devicenum, begintime, endtime);
					renewalSettlement( resultmap, begintime);//更新结算信息
				}
			}else if(type.equals(3)){//汇总某个商户下的所有设备收益
				allDevivceIncomeSettlement(merid, begintime, endtime);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * origin
	 * 根据设备号、商户id和起止时间查询该商户的收益情况
	 */
	public void allDevivceIncomeSettlement(Integer merid, String begintime, String endtime) {
		try {
			Codestatistics statistic = new Codestatistics();
			statistic.setMerid(merid);
			statistic.setCountTime(CommUtil.DateTime(begintime, "yyyy-MM-dd"));
			List<UserEquipment> deviceinfo = userEquipmentServece.getUserEquipmentById(merid);
			for(UserEquipment item : deviceinfo){
				String devicenum = CommUtil.toString(item.getEquipmentCode());
				statistic.setType(1);
				statistic.setCode(devicenum);
				Codestatistics collect = codestatisticsDao.selectStatistics(statistic);
				if(collect==null){
					Map<String, Object> resultmap = devivceIncomeSettlement(merid, devicenum, begintime, endtime);
					renewalSettlement( resultmap, begintime);//更新结算信息
				}else if(collect.getConsumequantity()==0 || collect.getConsumetime()==0){//TODO
					Map<String, Object> resultmap = devivceIncomeSettlement(merid, devicenum, begintime, endtime);
					renewalSettlement( resultmap, begintime);//更新结算信息
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * origin
	 * 根据商户id和起止时间查询该商户的收益情况
	 */
	public Map<String, Object> dealerIncomeSettlement( Integer merid, String begintime, String endtime) {
		Map<String, Object> resultmap = new HashMap<String, Object>();
		try{
			Parameters parame = new Parameters();
			parame.setStartTime(begintime);
			parame.setEndTime(endtime);
			parame.setDealer(CommUtil.toString(merid));
			//查询在指定时间所有收益变化的商户的收益
			List<Map<String, Object>> result = CommUtil.isListMapEmpty(collectStatisticsDao.dealerIncomeCollect(parame));
			if(result.size()>0){
				resultmap = result.get(0);
				resultmap.put("code", "000000");
				resultmap.put("areaid", 0);
				resultmap.put("type", 2);
				resultmap.put("merid", merid);
			}else{
				resultmap.put("code", "000000");
				resultmap.put("areaid", 0);
				resultmap.put("type", 2);
				resultmap.put("merid", merid);
			}
			List<Map<String, Object>> chargeresult = CommUtil.isListMapEmpty(collectStatisticsDao.chargeInfoCollect(parame));
			if(chargeresult.size()>0){
				Map<String, Object> obje = chargeresult.get(0);
				Integer consumequantity = CommUtil.toInteger(obje.get("consumequantity"));
				Integer consumetime = CommUtil.toInteger(obje.get("consumetime"));
				Double windowpulsemoney = CommUtil.toDouble(obje.get("windowpulsemoney"));
				Double offcardmoney = CommUtil.toDouble(obje.get("offcardmoney"));
				Double oncardmoney = CommUtil.toDouble(obje.get("oncardmoney"));
				resultmap.put("consumequantity", consumequantity);
				resultmap.put("consumetime", consumetime);
				resultmap.put("windowpulsemoney", windowpulsemoney);
				resultmap.put("offcardmoney", offcardmoney);
				resultmap.put("oncardmoney", oncardmoney);
			}else{
				resultmap.put("consumequantity", 0);
				resultmap.put("consumetime", 0);
				resultmap.put("windowpulsemoney", 0);
				resultmap.put("offcardmoney", 0);
				resultmap.put("oncardmoney", 0);
			}
			List<Map<String, Object>> inCoinsresult = CommUtil.isListMapEmpty(collectStatisticsDao.incoinsIncomeCollect(parame));
			if(inCoinsresult.size()>0){
				Map<String, Object> obje = inCoinsresult.get(0);
				Integer incoins = CommUtil.toInteger(obje.get("incoins"));
				Double incoinsmoney = CommUtil.toDouble(obje.get("incoinsmoney"));
				resultmap.put("incoins", incoins);
				resultmap.put("incoinsmoney", incoinsmoney);
			}else{
				resultmap.put("incoins", 0);
				resultmap.put("incoinsmoney", 0);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return resultmap;
	}
	
	/**
	 * origin
	 * 根据设备号、商户id和起止时间查询该商户的收益情况
	 * @return 
	 */
	public Map<String, Object> devivceIncomeSettlement(Integer merid, String devicenum, String begintime, String endtime) {
		Map<String, Object> resultmap = new HashMap<String, Object>();
		try {
			Parameters parame = new Parameters();
			parame.setStartTime(begintime);
			parame.setEndTime(endtime);
			parame.setDealer(CommUtil.toString(merid));
			parame.setCode(devicenum);
			List<Map<String, Object>> traderesult = CommUtil.isListMapEmpty(collectStatisticsDao.timingCollectMoney(parame));
			
			if(traderesult.size()>0){
				resultmap = traderesult.get(0);
				resultmap.put("code", devicenum);
				resultmap.put("areaid", 0);
				resultmap.put("type", 1);
				resultmap.put("merid", merid);
			}else{
				resultmap.put("code", devicenum);
				resultmap.put("areaid", 0);
				resultmap.put("type", 1);
				resultmap.put("merid", merid);
			}
			List<Map<String, Object>> chargeresult = CommUtil.isListMapEmpty(collectStatisticsDao.chargeInfoCollect(parame));
			if(chargeresult.size()>0){
				Map<String, Object> obje = chargeresult.get(0);
				Integer consumequantity = CommUtil.toInteger(obje.get("consumequantity"));
				Integer consumetime = CommUtil.toInteger(obje.get("consumetime"));
				Double windowpulsemoney = CommUtil.toDouble(obje.get("windowpulsemoney"));
				Double offcardmoney = CommUtil.toDouble(obje.get("offcardmoney"));
				Double oncardmoney = CommUtil.toDouble(obje.get("oncardmoney"));
				resultmap.put("consumequantity", consumequantity);
				resultmap.put("consumetime", consumetime);
				resultmap.put("windowpulsemoney", windowpulsemoney);
				resultmap.put("offcardmoney", offcardmoney);
				resultmap.put("oncardmoney", oncardmoney);
			}else{
				resultmap.put("consumequantity", 0);
				resultmap.put("consumetime", 0);
				resultmap.put("windowpulsemoney", 0);
				resultmap.put("offcardmoney", 0);
				resultmap.put("oncardmoney", 0);
			}
			List<Map<String, Object>> inCoinsresult = CommUtil.isListMapEmpty(collectStatisticsDao.incoinsIncomeCollect(parame));
			if(inCoinsresult.size()>0){
				Map<String, Object> obje = inCoinsresult.get(0);
				Integer incoins = CommUtil.toInteger(obje.get("incoins"));
				Double incoinsmoney = CommUtil.toDouble(obje.get("incoinsmoney"));
				resultmap.put("incoins", incoins);
				resultmap.put("incoinsmoney", incoinsmoney);
			}else{
				resultmap.put("incoins", 0);
				resultmap.put("incoinsmoney", 0);
			}
//			incomeSettlement(resultmap, begintime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultmap;
	}
	
	/**
	 * origin
	 * 根据result结果，转换统一汇总数据字段并插入数据库
	 */
	public void incomeSettlement(Map<String, Object> result, String time){
		try{
			Integer wechatnum = CommUtil.toInteger(result.get("wechatnum"));
			Integer alipaynum = CommUtil.toInteger(result.get("alipaynum"));
			Integer refwechatnum = CommUtil.toInteger(result.get("refwechatnum"));
			Integer refalipaynum = CommUtil.toInteger(result.get("refalipaynum"));
			
			Double wechatmoney = CommUtil.toDouble(result.get("wechatmoney"));
			Double alipaymoney = CommUtil.toDouble(result.get("alipaymoney"));
			Double refwechatmoney = CommUtil.toDouble(result.get("refwechatmoney"));
			Double refalipaymoney = CommUtil.toDouble(result.get("refalipaymoney"));
			/*Double paymentmoney = CommUtil.toDouble(result.get("paymentmoney"));*/
			Double incomepaymentmoney = CommUtil.toDouble(result.get("incomepaymentmoney"));
			Double wechatpaymentmoney = CommUtil.toDouble(result.get("wechatpaymentmoney"));
			Double paymentmoney = CommUtil.addBig(wechatpaymentmoney, incomepaymentmoney);
			
			//银联支付、退费金额
			Integer unionpaynum = CommUtil.toInteger(result.get("unionpaynum"));
			Integer unionpaymoney = CommUtil.toInteger(result.get("unionpaymoney"));
			Double refunionpaynum = CommUtil.toDouble(result.get("refunionpaynum"));
			Double refunionpaymoney = CommUtil.toDouble(result.get("refunionpaymoney"));
			//银联支付   收益金额
			Double incomeunionpaymoney = CommUtil.subBig(unionpaymoney, refunionpaymoney);
			
			Integer incoins = CommUtil.toInteger(result.get("incoins"));
			Double incoinsmoney = CommUtil.toDouble(result.get("incoinsmoney"));
			
			Integer consumetime = CommUtil.toInteger(result.get("consumetime"));
			Integer consumequantity = CommUtil.toInteger(result.get("consumequantity"));
			
			Double offcardmoney = CommUtil.toDouble(result.get("offcardmoney"));
			Double oncardmoney = CommUtil.toDouble(result.get("oncardmoney"));
			Double windowpulsemoney = CommUtil.toDouble(result.get("windowpulsemoney"));
			
			Double totalmoney = CommUtil.addBig(wechatmoney, alipaymoney);
			totalmoney = CommUtil.addBig(totalmoney, incomeunionpaymoney);
			Double onlinereturnmoney = CommUtil.addBig(refwechatmoney, refalipaymoney);
			Double onlinemoney = CommUtil.subBig(totalmoney, onlinereturnmoney);
			Double incomemoney = CommUtil.subBig(onlinemoney, incomepaymentmoney);
			
			Integer totalorder =  CommUtil.toInteger(wechatnum + alipaynum);
			Integer onreturnorder = CommUtil.toInteger(refwechatnum + refalipaynum);
			Integer onlineorder = CommUtil.toInteger(totalorder - onreturnorder);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", CommUtil.toString(result.get("code")));
			map.put("merid", CommUtil.toInteger(result.get("merid")));
			map.put("areaid", CommUtil.toInteger(result.get("areaid")));
			map.put("type", CommUtil.toInteger(result.get("type")));
			map.put("ordertotal", totalorder);
			map.put("wechatorder", wechatnum);
			
			map.put("alipayorder", alipaynum);
			map.put("wechatretord", refwechatnum);
			map.put("alipayretord", refalipaynum);
			map.put("moneytotal", totalmoney);
			map.put("wechatmoney", wechatmoney);
			map.put("alipaymoney", alipaymoney);
			map.put("wechatretmoney", refwechatmoney);
			map.put("alipayretmoney", refalipaymoney);
			map.put("incoinsorder", incoins);
			map.put("incoinsmoney", incoinsmoney);
			map.put("unionpayordernum", unionpaynum);
			map.put("unionpaymoney", incomeunionpaymoney);
			
			Date codetime = CommUtil.DateTime(time, "yyyy-MM-dd");
			map.put("count_time", codetime);
			map.put("consumetime", consumetime);
			map.put("consumequantity", consumequantity);
			map.put("paymentmoney", paymentmoney);
			map.put("offcardmoney", offcardmoney);
			map.put("offconsumetime", 0);
			map.put("offconsumequantity", 0);
			map.put("oncardmoney", oncardmoney);
			map.put("onconsumetime", 0);
			map.put("onconsumequantity", 0);
			map.put("windowpulsemoney", windowpulsemoney);
			map.put("pulseconsumetime", 0);
			map.put("pulseconsumequantity", 0);
			collectStatisticsDao.insertionCollectMap(map);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * @Description：//更新结算信息
	 * @author： origin
	 * @createTime：2020年4月24日下午12:32:16
	 */
	private void renewalSettlement(Map<String, Object> result, String time) {
		try{
			Integer wechatnum = CommUtil.toInteger(result.get("wechatnum"));
			Integer alipaynum = CommUtil.toInteger(result.get("alipaynum"));
			Integer refwechatnum = CommUtil.toInteger(result.get("refwechatnum"));
			Integer refalipaynum = CommUtil.toInteger(result.get("refalipaynum"));
			
			Double wechatmoney = CommUtil.toDouble(result.get("wechatmoney"));
			Double alipaymoney = CommUtil.toDouble(result.get("alipaymoney"));
			Double refwechatmoney = CommUtil.toDouble(result.get("refwechatmoney"));
			Double refalipaymoney = CommUtil.toDouble(result.get("refalipaymoney"));
			/*Double paymentmoney = CommUtil.toDouble(result.get("paymentmoney"));*/
			
			Double incomepaymentmoney = CommUtil.toDouble(result.get("incomepaymentmoney"));
			Double wechatpaymentmoney = CommUtil.toDouble(result.get("wechatpaymentmoney"));
			Double paymentmoney = CommUtil.addBig(wechatpaymentmoney, incomepaymentmoney);
			
			//银联支付、退费金额
			Integer unionpaynum = CommUtil.toInteger(result.get("unionpaynum"));
			Integer unionpaymoney = CommUtil.toInteger(result.get("unionpaymoney"));
			Double refunionpaynum = CommUtil.toDouble(result.get("refunionpaynum"));
			Double refunionpaymoney = CommUtil.toDouble(result.get("refunionpaymoney"));
			//银联支付   收益金额
			Double incomeunionpaymoney = CommUtil.subBig(unionpaymoney, refunionpaymoney);
			
			
			Integer incoins = CommUtil.toInteger(result.get("incoins"));
			Double incoinsmoney = CommUtil.toDouble(result.get("incoinsmoney"));
			
			Integer consumetime = CommUtil.toInteger(result.get("consumetime"));
			Integer consumequantity = CommUtil.toInteger(result.get("consumequantity"));
			
			Double offcardmoney = CommUtil.toDouble(result.get("offcardmoney"));
			Double oncardmoney = CommUtil.toDouble(result.get("oncardmoney"));
			Double windowpulsemoney = CommUtil.toDouble(result.get("windowpulsemoney"));
			
			Double totalmoney = CommUtil.addBig(wechatmoney, alipaymoney);
			totalmoney = CommUtil.addBig(totalmoney, incomeunionpaymoney);
			Double onlinereturnmoney = CommUtil.addBig(refwechatmoney, refalipaymoney);
			Double onlinemoney = CommUtil.subBig(totalmoney, onlinereturnmoney);
			Double incomemoney = CommUtil.subBig(onlinemoney, incomepaymentmoney);
			
			Integer totalorder =  CommUtil.toInteger(wechatnum + alipaynum);
			Integer onreturnorder = CommUtil.toInteger(refwechatnum + refalipaynum);
			Integer onlineorder = CommUtil.toInteger(totalorder - onreturnorder);

			String code = CommUtil.toString(result.get("code"));
			Integer merid = CommUtil.toInteger(result.get("merid"));
//			Integer areaid = CommUtil.toInteger(result.get("areaid"));
			Integer type = CommUtil.toInteger(result.get("type"));
			
			Codestatistics statistic = new Codestatistics();
			statistic.setType(type);
			statistic.setMerid(merid);
			statistic.setCountTime(CommUtil.DateTime(time, "yyyy-MM-dd"));
			if(type.equals(1)){//设备
				statistic.setCode(code);
			}
			Codestatistics collect = codestatisticsDao.selectStatistics(statistic);
			if(collect!=null){
				collect.setCode(CommUtil.toString(result.get("code")));
				collect.setMerid(CommUtil.toInteger(result.get("merid")));
				collect.setAreaid(CommUtil.toInteger(result.get("areaid")));
				collect.setType(CommUtil.toInteger(result.get("type")));
				collect.setOrdertotal(totalorder);
				collect.setWechatorder(wechatnum);
				
				collect.setAlipayorder(alipaynum);
				collect.setWechatretord(refwechatnum);
				collect.setAlipayretord(refalipaynum);
				collect.setMoneytotal(totalmoney);
				collect.setWechatmoney(wechatmoney);
				collect.setAlipaymoney(alipaymoney);
				collect.setWechatretmoney(refwechatmoney);
				collect.setAlipayretmoney(refalipaymoney);
				collect.setIncoinsorder(incoins);
				collect.setIncoinsmoney(incoinsmoney);
			    collect.setUnionpayordernum(unionpaynum);
			    collect.setUnionpaymoney(incomeunionpaymoney);
			
				Date codetime = CommUtil.DateTime(time, "yyyy-MM-dd");
				collect.setCountTime(codetime);
				collect.setConsumetime(consumetime);
				collect.setConsumequantity(consumequantity);
				collect.setPaymentmoney(paymentmoney);
				collect.setOffcardmoney(offcardmoney);
				collect.setOncardmoney(oncardmoney);
				collect.setWindowpulsemoney(windowpulsemoney);
				collectStatisticsDao.updateCollectInfo(collect);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//=======================================================================================

	/**
	 * @Description：
	 * @author origin
	 * @createTime：2020年4月16日下午10:48:38
	 * @param request
	 */
	public void allincomeCollect(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
			}
			String time = CommUtil.toString(maparam.get("time"));
			String begintime = time + " 00:00:00";
			String endtime = time + " 23:59:59";
			List<Map<String, Object>> meralluser = collectStatisticsDao.inquireAllmeridInfo();//查询绑定设备的商户与未绑定设备的合伙人
			for(Map<String, Object> item : meralluser){
				Integer dealid = CommUtil.toInteger(item.get("merid"));
				Map<String, Object> resultmap = dealerIncomeSettlement(dealid, begintime, endtime);
				incomeSettlement(resultmap, begintime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description：
	 * @author origin
	 * @createTime：2020年4月16日下午10:48:38
	 * @param request
	 */
	public void alldeviceincomeCollect(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
			}
			String time = CommUtil.toString(maparam.get("time"));
			String begintime = time + " 00:00:00";
			String endtime = time + " 23:59:59";
			List<Map<String, Object>> meralluser = collectStatisticsDao.inquireAllmeridInfo();//查询绑定设备的商户与未绑定设备的合伙人
			for(Map<String, Object> item : meralluser){
				Integer dealid = CommUtil.toInteger(item.get("merid"));
				Map<String, Object> resultmap = dealerIncomeSettlement(dealid, begintime, endtime);
				incomeSettlement(resultmap, begintime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 商户的定时汇总
	 */
	@Override
	public void dealerTimingSettlement(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				
			}
			Integer merid = CommUtil.toInteger(maparam.get("merid"));
			String time = CommUtil.toString(maparam.get("time"));
			String begintime = time + " 00:00:00";
			String endtime = time + " 23:59:59";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设备的定时汇总
	 */
	@Override
	public void devivceTimingSettlement(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				
			}
			Integer merid = CommUtil.toInteger(maparam.get("merid"));
			String time = CommUtil.toString(maparam.get("time"));
			String begintime = time + " 00:00:00";
			String endtime = time + " 23:59:59";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void allDealerIncomeData(String begintime, String endtime) {
		try{
			Parameters parame = new Parameters();
			parame.setStartTime(begintime);
			parame.setEndTime(endtime);
//			parame.setDealer(CommUtil.toString(merid));
			
			//查询在指定时间所有收益变化的商户的收益
			List<Map<String, Object>> result = CommUtil.isListMapEmpty(collectStatisticsDao.dealerIncomeCollect(parame));
			System.out.println("数据条数：result    "+result.size());
			List<Map<String, Object>> chargeresult = CommUtil.isListMapEmpty(collectStatisticsDao.chargeInfoCollect(parame));
			for(Map<String, Object> item : result){
				item.put("code", "000000");
				item.put("areaid", 0);
				item.put("type", 2);
				Integer dealid = CommUtil.toInteger(item.get("merid"));
				for(Map<String, Object> obje : chargeresult){
					Integer mercid = CommUtil.toInteger(obje.get("merid"));
					Integer consumequantity = CommUtil.toInteger(obje.get("consumequantity"));
					Integer consumetime = CommUtil.toInteger(obje.get("consumetime"));
					Double windowpulsemoney = CommUtil.toDouble(obje.get("windowpulsemoney"));
					Double offcardmoney = CommUtil.toDouble(obje.get("offcardmoney"));
					Double oncardmoney = CommUtil.toDouble(obje.get("oncardmoney"));
					if(dealid.equals(mercid)){
						item.put("consumequantity", consumequantity);
						item.put("consumetime", consumetime);
						item.put("windowpulsemoney", windowpulsemoney);
						item.put("offcardmoney", offcardmoney);
						item.put("oncardmoney", oncardmoney);
						break;
					}
				}
			}
			System.out.println("电量、时间、刷卡对比完成：result    "+result.size());
			//根据表（脉冲记录表）查询脉冲设备上传投币信息
			List<Map<String, Object>> inCoinsresult = CommUtil.isListMapEmpty(collectStatisticsDao.incoinsIncomeCollect(parame));
			for(Map<String, Object> item : inCoinsresult){
				Integer dealid = CommUtil.toInteger(item.get("merid"));
				for(Map<String, Object> obje : result){
					Integer mercid = CommUtil.toInteger(obje.get("merid"));
					if(dealid.equals(mercid)){
						Integer incoins = CommUtil.toInteger(item.get("incoins"));
						Double incoinsmoney = CommUtil.toDouble(item.get("incoinsmoney"));
						item.put("incoins", incoins);
						item.put("incoinsmoney", incoinsmoney);
						break;
					}
				}
			}
			System.out.println("脉冲对比完成：result    "+result.size());
			System.out.println("第一次开始计算所有数据完成：    "+System.currentTimeMillis());
			insertionCollectData(result, begintime);
			System.out.println("第一次开始计算所有数据插入完成：    "+System.currentTimeMillis());
//			inserCollectData(result, begintime);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void allDealerIncomeInfo(String begintime, String endtime) {
		try{
			Parameters parame = new Parameters();
			parame.setStartTime(begintime);
			parame.setEndTime(endtime);
//			parame.setDealer(CommUtil.toString(merid));
			
			//查询在指定时间所有收益变化的商户的收益
			List<Map<String, Object>> result = CommUtil.isListMapEmpty(collectStatisticsDao.dealerIncomeCollect(parame));
			
			List<Map<String, Object>> collectuser = collectStatisticsDao.inquireCollectMerid(parame);
			List<Map<String, Object>> meralluser = collectStatisticsDao.inquireAllmeridInfo();//查询绑定设备的商户与未绑定设备的合伙人
			List<Map<String, Object>> meruser = collectStatisticsDao.inquireMeridInfo(parame);
			boolean fal = meralluser.removeAll(meruser);
			if(fal){
				result.addAll(meralluser);
			}
			for(Map<String, Object> item : result){
				String dealid = CommUtil.toInteger(item.get("merid")).toString();
				item.put("code", "000000");
				item.put("areaid", 0);
				item.put("type", 2);
				parame.setDealer(dealid);
				List<Map<String, Object>> chargeresult = CommUtil.isListMapEmpty(collectStatisticsDao.chargeInfoCollect(parame));
				if(chargeresult.size()>0){
					Map<String, Object> obje = chargeresult.get(0);
					Integer consumequantity = CommUtil.toInteger(obje.get("consumequantity"));
					Integer consumetime = CommUtil.toInteger(obje.get("consumetime"));
					Double windowpulsemoney = CommUtil.toDouble(obje.get("windowpulsemoney"));
					Double offcardmoney = CommUtil.toDouble(obje.get("offcardmoney"));
					Double oncardmoney = CommUtil.toDouble(obje.get("oncardmoney"));
					item.put("consumequantity", consumequantity);
					item.put("consumetime", consumetime);
					item.put("windowpulsemoney", windowpulsemoney);
					item.put("offcardmoney", offcardmoney);
					item.put("oncardmoney", oncardmoney);
				}else{
					item.put("consumequantity", 0);
					item.put("consumetime", 0);
					item.put("windowpulsemoney", 0);
					item.put("offcardmoney", 0);
					item.put("oncardmoney", 0);
				}
				List<Map<String, Object>> inCoinsresult = CommUtil.isListMapEmpty(collectStatisticsDao.incoinsIncomeCollect(parame));
				if(inCoinsresult.size()>0){
					Map<String, Object> obje = inCoinsresult.get(0);
					Integer incoins = CommUtil.toInteger(obje.get("incoins"));
					Double incoinsmoney = CommUtil.toDouble(obje.get("incoinsmoney"));
					item.put("incoins", incoins);
					item.put("incoinsmoney", incoinsmoney);
				}else{
					item.put("incoins", 0);
					item.put("incoinsmoney", 0);
				}
			}
//			insertionCollectData(result, begintime);
			inserCollectData(result, begintime);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void insertionCollectData(List<Map<String, Object>> result, String time){
		try{
			System.out.println("第一次开始所有数据转换开始：    "+System.currentTimeMillis());
			List<Map<String, Object>> mapinfo = new ArrayList<Map<String, Object>>();
			int num = 0;
			for(Map<String, Object> item : result){
				num +=1;
				System.out.println(num);
				Integer wechatnum = CommUtil.toInteger(item.get("wechatnum"));
				Integer alipaynum = CommUtil.toInteger(item.get("alipaynum"));
				Integer refwechatnum = CommUtil.toInteger(item.get("refwechatnum"));
				Integer refalipaynum = CommUtil.toInteger(item.get("refalipaynum"));
				
				Double wechatmoney = CommUtil.toDouble(item.get("wechatmoney"));
				Double alipaymoney = CommUtil.toDouble(item.get("alipaymoney"));
				Double refwechatmoney = CommUtil.toDouble(item.get("refwechatmoney"));
				Double refalipaymoney = CommUtil.toDouble(item.get("refalipaymoney"));
				/*Double paymentmoney = CommUtil.toDouble(item.get("paymentmoney"));*/
				Double incomepaymentmoney = CommUtil.toDouble(item.get("incomepaymentmoney"));
				Double wechatpaymentmoney = CommUtil.toDouble(item.get("wechatpaymentmoney"));
				Double paymentmoney = CommUtil.addBig(wechatpaymentmoney, incomepaymentmoney);
				
				Integer incoins = CommUtil.toInteger(item.get("incoins"));
				Double incoinsmoney = CommUtil.toDouble(item.get("incoinsmoney"));
				
				Integer consumetime = CommUtil.toInteger(item.get("consumetime"));
				Integer consumequantity = CommUtil.toInteger(item.get("consumequantity"));
				
				Double offcardmoney = CommUtil.toDouble(item.get("offcardmoney"));
				Double oncardmoney = CommUtil.toDouble(item.get("oncardmoney"));
				Double windowpulsemoney = CommUtil.toDouble(item.get("windowpulsemoney"));
				
				Double totalmoney = CommUtil.addBig(wechatmoney, alipaymoney);
				Double onlinereturnmoney = CommUtil.addBig(refwechatmoney, refalipaymoney);
				Double onlinemoney = CommUtil.subBig(totalmoney, onlinereturnmoney);
				Double incomemoney = CommUtil.subBig(onlinemoney, incomepaymentmoney);
				
				Integer totalorder =  CommUtil.toInteger(wechatnum + alipaynum);
				Integer onreturnorder = CommUtil.toInteger(refwechatnum + refalipaynum);
				Integer onlineorder = CommUtil.toInteger(totalorder - onreturnorder);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", CommUtil.toString(item.get("code")));
				map.put("merid", CommUtil.toInteger(item.get("merid")));
				map.put("areaid", CommUtil.toInteger(item.get("areaid")));
				map.put("type", CommUtil.toInteger(item.get("type")));
				map.put("ordertotal", totalorder);
				map.put("wechatorder", wechatnum);
				
				map.put("alipayorder", alipaynum);
				map.put("wechatretord", refwechatnum);
				map.put("alipayretord", refalipaynum);
				map.put("moneytotal", totalmoney);
				map.put("wechatmoney", wechatmoney);
				map.put("alipaymoney", alipaymoney);
				map.put("wechatretmoney", refwechatmoney);
				map.put("alipayretmoney", refalipaymoney);
				map.put("incoinsorder", incoins);
				map.put("incoinsmoney", incoinsmoney);
				
				Date codetime = CommUtil.DateTime(time, "yyyy-MM-dd");
				map.put("count_time", codetime);
				map.put("consumetime", consumetime);
				map.put("consumequantity", consumequantity);
				map.put("paymentmoney", paymentmoney);
				map.put("offcardmoney", offcardmoney);
				map.put("offconsumetime", 0);
				map.put("offconsumequantity", 0);
				map.put("oncardmoney", oncardmoney);
				map.put("onconsumetime", 0);
				map.put("onconsumequantity", 0);
				map.put("windowpulsemoney", windowpulsemoney);
				map.put("pulseconsumetime", 0);
				map.put("pulseconsumequantity", 0);
				mapinfo.add(map);
			}
			System.out.println("第一次开始所有数据转换完成：    "+System.currentTimeMillis());
			collectStatisticsDao.insertionCollectData(mapinfo);
			System.out.println("第一次开始所有数据转换完成插入*****：    "+System.currentTimeMillis());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void inserCollectData(List<Map<String, Object>> result, String time){
		try{
			List<Codestatistics> statis = new ArrayList<Codestatistics>();
			for(Map<String, Object> item : result){
				Integer wechatnum = CommUtil.toInteger(item.get("wechatnum"));
				Integer alipaynum = CommUtil.toInteger(item.get("alipaynum"));
				Integer refwechatnum = CommUtil.toInteger(item.get("refwechatnum"));
				Integer refalipaynum = CommUtil.toInteger(item.get("refalipaynum"));
				
				Double wechatmoney = CommUtil.toDouble(item.get("wechatmoney"));
				Double alipaymoney = CommUtil.toDouble(item.get("alipaymoney"));
				Double refwechatmoney = CommUtil.toDouble(item.get("refwechatmoney"));
				Double refalipaymoney = CommUtil.toDouble(item.get("refalipaymoney"));
				
				/*Double paymentmoney = CommUtil.toDouble(item.get("paymentmoney"));*/
				
				Double incomepaymentmoney = CommUtil.toDouble(item.get("incomepaymentmoney"));
				Double wechatpaymentmoney = CommUtil.toDouble(item.get("wechatpaymentmoney"));
				Double paymentmoney = CommUtil.addBig(wechatpaymentmoney, incomepaymentmoney);
				
				Integer incoins = CommUtil.toInteger(item.get("incoins"));
				Double incoinsmoney = CommUtil.toDouble(item.get("incoinsmoney"));
				
				Integer consumetime = CommUtil.toInteger(item.get("consumetime"));
				Integer consumequantity = CommUtil.toInteger(item.get("consumequantity"));
				
				Double offcardmoney = CommUtil.toDouble(item.get("offcardmoney"));
				Double oncardmoney = CommUtil.toDouble(item.get("oncardmoney"));
				Double windowpulsemoney = CommUtil.toDouble(item.get("windowpulsemoney"));
				
				Double totalmoney = CommUtil.addBig(wechatmoney, alipaymoney);
				Double onlinereturnmoney = CommUtil.addBig(refwechatmoney, refalipaymoney);
				Double onlinemoney = CommUtil.subBig(totalmoney, onlinereturnmoney);
				Double incomemoney = CommUtil.subBig(onlinemoney, incomepaymentmoney);
				
				Integer totalorder =  CommUtil.toInteger(wechatnum + alipaynum);
				Integer onreturnorder = CommUtil.toInteger(refwechatnum + refalipaynum);
				Integer onlineorder = CommUtil.toInteger(totalorder - onreturnorder);

				Codestatistics collectData = new Codestatistics();
				
				collectData.setCode(CommUtil.toString(item.get("code")));
				collectData.setMerid(CommUtil.toInteger(item.get("merid")));
				collectData.setAreaid(CommUtil.toInteger(item.get("areaid")));
				collectData.setType(CommUtil.toInteger(item.get("type")));
				
				collectData.setOrdertotal(totalorder);
				collectData.setMoneytotal(totalmoney);
				
				collectData.setWechatorder(wechatnum);
				collectData.setAlipayorder(alipaynum);
				collectData.setWechatretord(refwechatnum);
				collectData.setAlipayretord(refalipaynum);
				
				collectData.setWechatmoney(wechatmoney);
				collectData.setAlipaymoney(alipaymoney);
				collectData.setWechatretmoney(refwechatmoney);
				collectData.setAlipayretmoney(refalipaymoney);
				
				collectData.setPaymentmoney(paymentmoney);
				collectData.setIncoinsorder(incoins);
				collectData.setIncoinsmoney(incoinsmoney);
				collectData.setConsumequantity(consumequantity);
				collectData.setConsumetime(consumetime);
				collectData.setOffcardmoney(offcardmoney);
				collectData.setWindowpulsemoney(windowpulsemoney);
				collectData.setOncardmoney(oncardmoney);
				Date codetime = CommUtil.DateTime(time, "yyyy-MM-dd");
				collectData.setCountTime(codetime);
				statis.add(collectData);
			}
			collectStatisticsDao.insertionCollectInfo(statis);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @Description：商户收益汇总
	 * @param time
	 * @author： origin
	 * @createTime：2020年4月15日上午11:05:20
	 */
	@Override
	public void dealerIncomeCollect(String time) {
		String begintime = time + " 00:00:00";
		String endtime = time + " 23:59:59";

//		allDealerIncomeData
//		allDealerIncomeInfo
		
//		System.out.println("第一次开始：    "+System.currentTimeMillis());
//		allDealerIncomeData( begintime, endtime);
//		System.out.println("第一次结束：    "+System.currentTimeMillis());
		System.out.println("第二次开始：    "+System.currentTimeMillis());
		allDealerIncomeInfo( begintime, endtime);
		System.out.println("第二次结束：    "+System.currentTimeMillis());
		
	}

	/**
	 * @Description：设备收益汇总
	 * @param time
	 * @author： origin
	 * @createTime：2020年4月15日上午11:05:20
	 */
	@Override
	public void devivceIncomeCollect(String time) {
		String begintime = time + " 00:00:00";
		String endtime = time + " 23:59:59";
		
	}

	/**
	 * @Description：商户定时汇总
	 * @param time
	 * @author： origin
	 * @createTime：2020年4月15日上午11:05:20
	 */
	@Override
	public void dealerTimingCollect(String time) {
		String begintime = time + " 00:00:00";
		String endtime = time + " 23:59:59";
		
	}

	/**
	 * @Description：设备定时汇总
	 * @param time
	 * @author： origin
	 * @createTime：2020年4月15日上午11:05:20
	 */
	@Override
	public void devivceTimingCollect(String time) {
		String begintime = time + " 00:00:00";
		String endtime = time + " 23:59:59";
		
	}
	
}
