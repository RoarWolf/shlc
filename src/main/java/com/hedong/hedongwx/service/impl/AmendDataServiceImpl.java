package com.hedong.hedongwx.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hedong.hedongwx.dao.AmendDataDao;
import com.hedong.hedongwx.dao.ChargeRecordDao;
import com.hedong.hedongwx.dao.CodestatisticsDao;
import com.hedong.hedongwx.dao.InCoinsDao;
import com.hedong.hedongwx.dao.MerchantDetailDao;
import com.hedong.hedongwx.dao.TradeRecordDao;
import com.hedong.hedongwx.dao.UserEquipmentDao;
import com.hedong.hedongwx.entity.Codestatistics;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.service.AmendDataService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.StringUtil;

/**
 * @Description： 
 * @author  origin  创建时间：   2020年6月12日 下午5:45:53  
 */
@Service
public class AmendDataServiceImpl implements AmendDataService{
	
	
	@Autowired
	private AmendDataDao amendDataDao;
	@Autowired
	private ChargeRecordDao chargeRecordDao;
	@Autowired
	private InCoinsDao inCoinsDao;
	@Autowired
	private TradeRecordDao tradeRecordDao;
	@Autowired
	private MerchantDetailDao merchantDetailDao;
	@Autowired
	private CodestatisticsDao codestatisticsDao;
	@Autowired
	private UserEquipmentDao userEquipmentDao;
	
	
//========================================================================================================
	

	/**
	 * @Description: 
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月13日 下午6:08:00
	 * @common:   
	 */
	@Override
	public Object calculateCollect(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			String type =  CommUtil.toString(maparam.get("type"));
			String time =  CommUtil.toString(maparam.get("time"));
			
			Integer datanum = CommUtil.toInteger(maparam.get("datanum"));
			//商户收益汇总
			for(int i = datanum; i >= 0; i--){
				//商户收益汇总
				System.out.println("【calculateCollect】汇总开始时间       "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
				String startTimes = StringUtil.getnumterday("yyyy-MM-dd", time, i);
				if(CommUtil.toInteger(type)==1){
					//经销商收益汇总
					System.out.println("【calculateCollect】商户汇总时间       "+startTimes);
					dealIncoCollectAll(startTimes);
					System.out.println("【calculateCollect】时间       "+startTimes+"  号，商户数据汇总完成   ");
				}else if(CommUtil.toInteger(type)==2){
					//经销商所拥有的设备收益汇总
					System.out.println("【calculateCollect】设备汇总时间       "+startTimes);
					deviceIncomeDataGain(startTimes);
					System.out.println("【calculateCollect】时间       "+startTimes+"  号，设备数据汇总完成   ");
				}else{
					//经销商收益汇总
					System.out.println("【calculateCollect】商户汇总时间       "+startTimes);
					dealIncoCollectAll(startTimes);
					System.out.println("【calculateCollect】时间       "+startTimes+"  号，商户数据汇总完成   ");
					//经销商所拥有的设备收益汇总
					System.out.println("【calculateCollect】设备汇总时间       "+startTimes);
					deviceIncomeDataGain(startTimes);
					System.out.println("【calculateCollect】时间       "+startTimes+"  号，设备数据汇总完成   ");
				}
				System.out.println("【calculateCollect】汇总结束时间       "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			}
			return CommUtil.responseBuildInfo(200, "结算完成", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**
	 * @Description: 
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月12日 下午2:41:54
	 * @common:   
	 */
	@Override
	public Object calculateTotalCollect(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			String type =  CommUtil.toString(maparam.get("type"));
			String time =  CommUtil.toString(maparam.get("time"));
			
			Integer datanum = CommUtil.toInteger(maparam.get("datanum"));
			//商户收益汇总
			for(int i = datanum; i >= 0; i--){
				//商户收益汇总
				System.out.println("【calculateTotalCollect】汇总开始时间       "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
				String startTimes = StringUtil.getnumterday("yyyy-MM-dd", time, i);
				if(CommUtil.toInteger(type)==1){
					//经销商收益汇总
					System.out.println("【calculateTotalCollect】商户汇总时间       "+startTimes);
					dealIncoCollectAll(startTimes);
					System.out.println("【calculateTotalCollect】时间       "+startTimes+"  号，商户数据汇总完成   ");
				}else if(CommUtil.toInteger(type)==2){
					//经销商所拥有的设备收益汇总
					System.out.println("【calculateTotalCollect】设备汇总时间       "+startTimes);
					deviceIncomeDataGain(startTimes);
					System.out.println("【calculateTotalCollect】时间       "+startTimes+"  号，设备数据汇总完成   ");
				}
				System.out.println("【calculateTotalCollect】汇总结束时间       "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			}
			return CommUtil.responseBuildInfo(200, "结算完成", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	
	public void dealIncoCollectAll(String time) {
		try {
			List<Map<String, Object>> merusermap = amendDataDao.inquireAllmeridInfo();
			
			String begintime = time + " 00:00:00";
			String endtime = time + " 23:59:59";
			
			for(Map<String, Object> item : merusermap){
				Integer merid = CommUtil.toInteger(item.get("merid"));
				if(merid>0) dealerIncomeDataGain("000000", merid, "00", 2,  begintime, endtime, time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Integer dealerIncomeDataGain(String code, Integer merid, String areaid, Integer type, String begintime, 
			String endtime, String time) {
		try{
			Parameters parame = new Parameters();
			parame.setStartTime(begintime);
			parame.setEndTime(endtime);
			parame.setDealer(CommUtil.toString(merid));
			//查询作为商户时的在线收益
			Map<String, Object> result = CommUtil.isMapEmpty(merchantDetailDao.dealerIncomeCount(parame));
			//脉冲投币收益
			Map<String, Object> coinsincome = CommUtil.isMapEmpty(inCoinsDao.selectcoinsup(parame));
			
			//根据表（充电记录表）查询充电设备上传的窗口投币、离线刷卡消费、在线刷卡消费的信息
			Map<String, Object> chargeresult = CommUtil.isMapEmpty(chargeRecordDao.chargeInfoCollect(parame));
			
			Integer incoins = CommUtil.toInteger(coinsincome.get("incoins"));
			Double incoinsmoney = CommUtil.toDouble(coinsincome.get("incoinsmoney"));

			Integer consumequantity = CommUtil.toInteger(chargeresult.get("consumequantity"));//consumequantity
			Integer consumetime = CommUtil.toInteger(chargeresult.get("consumetime"));//consumetime
			Double windowpulsemoney = CommUtil.toDouble(chargeresult.get("windowpulsemoney"));
			Double offcardmoney = CommUtil.toDouble(chargeresult.get("offcardmoney"));
			Double oncardmoney = CommUtil.toDouble(chargeresult.get("oncardmoney"));
			result.put("windowpulsemoney", windowpulsemoney);
			result.put("offcardmoney", offcardmoney);
			result.put("oncardmoney", oncardmoney);
			result.put("consumequantity", consumequantity);
			result.put("consumetime", consumetime);
			result.put("incoins", incoins);
			result.put("incoinsmoney", incoinsmoney);
			insertCollectData(code, merid, CommUtil.toInteger(areaid), type, result, time);
			return 200;
		}catch (Exception e) {
			e.printStackTrace();
			return 300;
		}
	}
	
	public void insertCollectData(String code, Integer merid, Integer areaid, Integer type, Map<String, Object> result, String time){
		try{
			code = CommUtil.toString(code);
			merid = CommUtil.toInteger(merid);
			areaid = CommUtil.toInteger(areaid);
			type = CommUtil.toInteger(type);
			
			//支付宝、微信   充值、退费金额
			Integer wechatnum = CommUtil.toInteger(result.get("wechatnum"));
			Integer alipaynum = CommUtil.toInteger(result.get("alipaynum"));
			Integer refwechatnum = CommUtil.toInteger(result.get("refwechatnum"));
			Integer refalipaynum = CommUtil.toInteger(result.get("refalipaynum"));
			
			Double wechatmoney = CommUtil.toDouble(result.get("wechatmoney"));
			Double alipaymoney = CommUtil.toDouble(result.get("alipaymoney"));
			Double refwechatmoney = CommUtil.toDouble(result.get("refwechatmoney"));
			Double refalipaymoney = CommUtil.toDouble(result.get("refalipaymoney"));
			

			//银联支付、退费金额
			Integer unionpaynum = CommUtil.toInteger(result.get("unionpaynum"));
			Integer unionpaymoney = CommUtil.toInteger(result.get("unionpaymoney"));
			Double refunionpaynum = CommUtil.toDouble(result.get("refunionpaynum"));
			Double refunionpaymoney = CommUtil.toDouble(result.get("refunionpaymoney"));
			//银联支付   收益金额
			Double incomeunionpaymoney = CommUtil.subBig(unionpaymoney, refunionpaymoney);
			
			//支付宝小程序、微信小程序   充值、退费金额
			Double wecappletmoney = CommUtil.toDouble(result.get("wecappletmoney"));
			Double aliappletmoney = CommUtil.toDouble(result.get("aliappletmoney"));
			Double refwecappletmoney = CommUtil.toDouble(result.get("refwecappletmoney"));
			Double refaliappletmoney = CommUtil.toDouble(result.get("refaliappletmoney"));
			
			//小程序    充值、退费金额
			Double appletmoney = CommUtil.addBig(wecappletmoney, aliappletmoney);
			Double appletmoneref = CommUtil.addBig(refwecappletmoney, refaliappletmoney);
			//小程序   收益金额
			Double incomeappletmoney = CommUtil.subBig(appletmoney, appletmoneref);
			
			Double incomepaymentmoney = CommUtil.toDouble(result.get("incomepaymentmoney"));
			Double wechatpaymentmoney = CommUtil.toDouble(result.get("wechatpaymentmoney"));
			Double paymentmoney = CommUtil.addBig(wechatpaymentmoney, incomepaymentmoney);
			
			//脉冲收入信息
			Integer incoins = CommUtil.toInteger(result.get("incoins"));
			Double incoinsmoney = CommUtil.toDouble(result.get("incoinsmoney"));
			
			//消耗时间电量信息
			Integer consumetime = CommUtil.toInteger(result.get("consumetime"));
			Integer consumequantity = CommUtil.toInteger(result.get("consumequantity"));
			
			//离线刷卡、在线刷卡、窗口脉冲
			Double offcardmoney = CommUtil.toDouble(result.get("offcardmoney"));
			Double oncardmoney = CommUtil.toDouble(result.get("oncardmoney"));
			Double windowpulsemoney = CommUtil.toDouble(result.get("windowpulsemoney"));
			
			Double totalmoney = CommUtil.addBig(wechatmoney, alipaymoney);
			totalmoney = CommUtil.addBig(totalmoney, incomeappletmoney);
			totalmoney = CommUtil.addBig(totalmoney, incomeunionpaymoney);
			
			Double onlinereturnmoney = CommUtil.addBig(refwechatmoney, refalipaymoney);
			Double onlinemoney = CommUtil.subBig(totalmoney, onlinereturnmoney);
			Double incomemoney = CommUtil.subBig(onlinemoney, incomepaymentmoney);
			
			Integer totalorder =  CommUtil.toInteger(wechatnum + alipaynum);
			Integer onreturnorder = CommUtil.toInteger(refwechatnum + refalipaynum);
			Integer onlineorder = CommUtil.toInteger(totalorder - onreturnorder);
			Codestatistics collectData = new Codestatistics();
			
			collectData.setCode(code);
			collectData.setMerid(merid);
			collectData.setAreaid(areaid);
			collectData.setType(type);
			
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
			
			collectData.setUnionpayordernum(unionpaynum);
			collectData.setUnionpaymoney(incomeunionpaymoney);
			collectData.setRefunionpaymoney(refunionpaymoney);
			Date codetime = CommUtil.DateTime(time, "yyyy-MM-dd");
			collectData.setCountTime(codetime);
			amendDataDao.insertCodestatis(collectData);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void deviceIncomeDataGain(String times) {
		try {
			String startTime = times + " 00:00:00";
			String endTime = times + " 23:59:59";
			Date time = CommUtil.DateTime(startTime, "yyyy-MM-dd");
			Integer type = 1;
			Parameters parameters = new Parameters();
			List<Map<String, Object>> udeequlist = CommUtil.isListMapEmpty(userEquipmentDao.coderelevance());//查询所有绑定设备
			for(Map<String, Object> item : udeequlist){
				String code = CommUtil.toString(item.get("code"));
				String merid = CommUtil.toString(item.get("merid"));
				String areaid = CommUtil.toString(item.get("aid"));
				String hardversion = CommUtil.toString(item.get("hardversion"));
				parameters.setStartTime(startTime);
				parameters.setEndTime(endTime);
				parameters.setDealer(merid);
				parameters.setCode(code);
				Map<String, Object> collectmoney = CommUtil.isMapEmpty(tradeRecordDao.timingCollectMoney(parameters));
				int incoins = 0;
				double incoinsmoney = 0.00;
				if(!hardversion.equals("04")){
					Map<String, Object> coinscollect = CommUtil.isMapEmpty(inCoinsDao.selectcoinsup(parameters));
					incoins = CommUtil.toInteger(coinscollect.get("incoins"));
					incoinsmoney = CommUtil.toDouble(coinscollect.get("incoinsmoney"));
					collectmoney.put("incoins", incoins);
					collectmoney.put("incoinsmoney", incoinsmoney);
				}
				Map<String, Object> consumebytimes = CommUtil.isMapEmpty(chargeRecordDao.selectCodeConsumeQuantity(parameters));
				collectmoney.put("consumequantity", CommUtil.toInteger(consumebytimes.get("consumeQuantity")));
				collectmoney.put("consumetime", CommUtil.toInteger(consumebytimes.get("consumetime")));
				insertCollectData(code, CommUtil.toInteger(merid), CommUtil.toInteger(areaid), type, collectmoney, startTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//========================================================================================================
	
	/**
	 * @Description: 
	 * @param startTimes
	 * @return
	 * @Author: origin  创建时间:2020年8月12日 上午11:34:29
	 * @common:   
	 */
	@Override
	public List<Map<String, Object>> dealerIncomeInfo(String time) {
		try {
			String begintime = time + " 00:00:00";
			String endtime = time + " 23:59:59";
			Parameters parame = new Parameters();
			parame.setStartTime(begintime);
			parame.setEndTime(endtime);
			System.out.println("001  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			
			List<Map<String, Object>> merusermap = amendDataDao.inquireAllmeridInfo();
			
			System.out.println("002  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			
			List<Map<String, Object>> resultMerIncome = dealerIncomeData(merusermap, begintime, endtime);
			List<Map<String, Object>> resultChargeIncfo = disposeChargeConsume(resultMerIncome, begintime, endtime);
			List<Map<String, Object>> result = disposeIncoinsNum(resultChargeIncfo, begintime, endtime);
			amendDataDao.insertionDealerIncome(result);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	/**
	 * @Description: 
	 * @param startTimes
	 * @return 
	 * @Author: origin  创建时间:2020年8月11日 下午12:41:08
	 * @common:   
	 */
	@Override
	public List<Map<String, Object>> dealerIncomeData(String time) {
		try {
			String begintime = time + " 00:00:00";
			String endtime = time + " 23:59:59";
			Parameters parame = new Parameters();
			parame.setStartTime(begintime);
			parame.setEndTime(endtime);
			System.out.println("0001  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			List<Map<String, Object>> merusermap = amendDataDao.inquireAllmeridInfo();
			//List<Map<String, Object>> meralluser = amendDataDao.inquireMeridData();//查询绑定设备的商户与未绑定设备的合伙人
			System.out.println("0002  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			List<Map<String, Object>> result = dealerIncomeDataGain(merusermap, begintime, endtime);
			amendDataDao.inserDealerIncome(result);
//			List<Map<String, Object>> resultincfo = dealerIncomeData(meralluser, begintime, endtime);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	
	public List<Map<String, Object>> dealerIncomeDataGain(List<Map<String, Object>> meralluser, String beginTime, String endTime) {
		try{
			Parameters parame = new Parameters();
			parame.setStartTime(beginTime);
			parame.setEndTime(endTime);
			//查询所有商户在指定时间的在线收益
			System.out.println("004  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			List<Map<String, Object>> resultmer = CommUtil.isListMapEmpty(amendDataDao.dealerIncomeStatistics(parame));

			System.out.println("005  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			//脉冲投币收益
			List<Map<String, Object>> chargeresult = CommUtil.isListMapEmpty(amendDataDao.chargeConsumeData(parame));

			System.out.println("006  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			//根据表（充电记录表）查询充电设备上传的窗口投币、离线刷卡消费、在线刷卡消费的信息
			List<Map<String, Object>> coinsincome = CommUtil.isListMapEmpty(amendDataDao.incoinsNumStatistics(parame));

			System.out.println("007  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			
			//将线上收益数据放入对应list<Map>中
			for(Map<String, Object> item : meralluser){
				item.put("code", "000000");
				item.put("areaid", "00");
				item.put("type", 2);
				item.put("count_time", beginTime.substring(0, 10));
				Integer meriditem = CommUtil.toInteger(item.get("merid"));
				for(Map<String, Object> data : resultmer){
					Integer meriddata = CommUtil.toInteger(data.get("merid"));
					if(meriditem.equals(meriddata)){
						item.put("wechatorder", CommUtil.toInteger(data.get("wechatnum")));
						item.put("wechatretord", CommUtil.toInteger(data.get("refwechatnum")));
						item.put("alipayorder", CommUtil.toInteger(data.get("alipaynum")));
						item.put("alipayretord", CommUtil.toInteger(data.get("refalipaynum")));
						item.put("unionpayordernum", CommUtil.toInteger(data.get("unionpaynum")));
						item.put("refunionpaynum", CommUtil.toInteger(data.get("refunionpaynum")));

						item.put("wechatmoney", CommUtil.toDouble(data.get("wechatmoney")));
						item.put("alipaymoney", CommUtil.toDouble(data.get("alipaymoney")));
						item.put("wechatretmoney", CommUtil.toDouble(data.get("refwechatmoney")));
						item.put("wechatretmoney", CommUtil.toDouble(data.get("refalipaymoney")));
						item.put("unionpaymoney", CommUtil.toDouble(data.get("unionpaymoney")));
						item.put("refunionpaymoney", CommUtil.toDouble(data.get("refunionpaymoney")));
						
						
						Integer wechatorder = CommUtil.toInteger(data.get("wechatnum"));
						Integer alipayorder = CommUtil.toInteger(data.get("alipaynum"));
						Integer unionpayordernum = CommUtil.toInteger(data.get("unionpaynum"));

						Double wechatmoney = CommUtil.toDouble(data.get("wechatmoney"));
						Double alipaymoney = CommUtil.toDouble(data.get("alipaymoney"));
						Double unionpaymoney = CommUtil.toDouble(data.get("unionpaymoney"));
						

						item.put("wechatorder", wechatorder);
						item.put("wechatretord", CommUtil.toInteger(data.get("refwechatnum")));
						item.put("alipayorder", alipayorder);
						item.put("alipayretord", CommUtil.toInteger(data.get("refalipaynum")));
						item.put("unionpayordernum", unionpayordernum);
						item.put("refunionpaynum", CommUtil.toInteger(data.get("refunionpaynum")));

						item.put("wechatmoney", wechatmoney);
						item.put("alipaymoney", alipaymoney);
						item.put("wechatretmoney", unionpaymoney);
						item.put("wechatretmoney", CommUtil.toDouble(data.get("refalipaymoney")));
						item.put("unionpaymoney", CommUtil.toDouble(data.get("unionpaymoney")));
						item.put("refunionpaymoney", CommUtil.toDouble(data.get("refunionpaymoney")));
						Integer ordertotal = CommUtil.toInteger(wechatorder + alipayorder + unionpayordernum);
						Integer moneytotal = CommUtil.toInteger(wechatmoney + alipaymoney + unionpaymoney);
						item.put("ordertotal", ordertotal);
						item.put("moneytotal", moneytotal);
					}
				}
			}
			//将电量数据放入对应list<Map>中
			for(Map<String, Object> item : meralluser){
				System.out.println(item);
				Integer meriditem = CommUtil.toInteger(item.get("merid"));
				System.out.println(meriditem);
				for(Map<String, Object> data : chargeresult){
					Integer meriddata = CommUtil.toInteger(data.get("merid"));
					if(meriditem.equals(meriddata)){
						item.put("consumequantity", CommUtil.toInteger(data.get("consumequantity")));
						item.put("consumetime", CommUtil.toInteger(data.get("consumetime")));
						item.put("windowpulsemoney", CommUtil.toDouble(data.get("windowpulsemoney")));
						item.put("offcardmoney", CommUtil.toDouble(data.get("offcardmoney")));
						item.put("oncardmoney", CommUtil.toDouble(data.get("oncardmoney")));
					}
				}		
			}
			
			//将脉冲投币数据放入对应list<Map>中
			for(Map<String, Object> item : meralluser){
				Integer meriditem = CommUtil.toInteger(item.get("merid"));
				for(Map<String, Object> data : coinsincome){
					Integer meriddata = CommUtil.toInteger(data.get("merid"));
					if(meriditem.equals(meriddata)){
						item.put("incoinsorder", CommUtil.toInteger(data.get("incoins")));
						item.put("incoinsmoney", CommUtil.toDouble(data.get("incoinsmoney")));
					}
				}
			}
			return meralluser;
		}catch (Exception e) {
			e.printStackTrace();
			return meralluser;
		}
	}
	
	public List<Map<String, Object>> disposeChargeConsume(List<Map<String, Object>> meralluser, String beginTime, String endTime) {
		try {
			Parameters parame = new Parameters();
			parame.setStartTime(beginTime);
			parame.setEndTime(endTime);
			///根据表（充电记录表）查询充电设备上传的窗口投币、离线刷卡消费、在线刷卡消费的信息
			List<Map<String, Object>> chargeresult = CommUtil.isListMapEmpty(amendDataDao.chargeConsumeData(parame));
			//将电量数据放入对应list<Map>中
			for(Map<String, Object> item : meralluser){
				Integer meriditem = CommUtil.toInteger(item.get("merid"));
				for(Map<String, Object> data : chargeresult){
					Integer meriddata = CommUtil.toInteger(data.get("merid"));
					if(meriditem.equals(meriddata)){
						item.put("consumetotalquantity", CommUtil.toInteger(data.get("consumequantity")));
						item.put("consumetotaltime", CommUtil.toInteger(data.get("consumetime")));
						item.put("windowpulsemoney", CommUtil.toDouble(data.get("windowpulsemoney")));
						item.put("offcardmoney", CommUtil.toDouble(data.get("offcardmoney")));
						item.put("oncardmoney", CommUtil.toDouble(data.get("oncardmoney")));
					}
				}		
			}
			return meralluser;
		} catch (Exception e) {
			e.printStackTrace();
			return meralluser;
		}
	}
	
	public List<Map<String, Object>> disposeIncoinsNum(List<Map<String, Object>> meralluser, String beginTime, String endTime) {
		try {
			Parameters parame = new Parameters();
			parame.setStartTime(beginTime);
			parame.setEndTime(endTime);
			System.out.println("006  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			//查询脉冲数据信息
			List<Map<String, Object>> coinsincome = CommUtil.isListMapEmpty(amendDataDao.incoinsNumStatistics(parame));
			//将脉冲投币数据放入对应list<Map>中
			for(Map<String, Object> item : meralluser){
				Integer meriditem = CommUtil.toInteger(item.get("merid"));
				for(Map<String, Object> data : coinsincome){
					Integer meriddata = CommUtil.toInteger(data.get("merid"));
					if(meriditem.equals(meriddata)){
						item.put("incoins", CommUtil.toInteger(data.get("incoins")));
						item.put("incoinsmoney", CommUtil.toDouble(data.get("incoinsmoney")));
					}
				}
			}
			return meralluser;
		} catch (Exception e) {
			e.printStackTrace();
			return meralluser;
		}
	}
	
	
	public List<Map<String, Object>> dealerIncomeData(List<Map<String, Object>> meralluser, String beginTime, String endTime) {
		try{
			Parameters parame = new Parameters();
			parame.setStartTime(beginTime);
			parame.setEndTime(endTime);
			//查询所有商户在指定时间的在线收益
			System.out.println("004  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			List<Map<String, Object>> merIncomeOrder = CommUtil.isListMapEmpty(amendDataDao.dealerIncomeOrder(parame));

			System.out.println("005  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			//脉冲投币收益
			parame.setSource("5,6,7");
			List<Map<String, Object>> merIncomeTopup = CommUtil.isListMapEmpty(amendDataDao.dealerTopupIncome(parame));

			System.out.println("006  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			//根据表（充电记录表）查询充电设备上传的窗口投币、离线刷卡消费、在线刷卡消费的信息
			parame.setSource("1,2,3");
			List<Map<String, Object>> merIncomeDevice = CommUtil.isListMapEmpty(amendDataDao.dealerDeviceIncome(parame));

			System.out.println("007  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			
			//将线上收益订单数据放入对应list<Map>中
			for(Map<String, Object> item : meralluser){
				item.put("type", 1);
				item.put("createtime", beginTime.substring(0, 10));
				item.put("remark", CommUtil.toDateTime());
				item.put("detail", "充值设备收益测试");
				Integer meriditem = CommUtil.toInteger(item.get("merid"));
				for(Map<String, Object> data : merIncomeOrder){
					Integer meriddata = CommUtil.toInteger(data.get("merid"));
					if(meriditem.equals(meriddata)){
						Integer wechatnum = CommUtil.toInteger(data.get("wechatnum"));
						Integer refwechatnum = CommUtil.toInteger(data.get("refwechatnum"));
						Integer alipaynum = CommUtil.toInteger(data.get("alipaynum"));
						Integer refalipaynum = CommUtil.toInteger(data.get("refalipaynum"));
						Integer unionpaynum = CommUtil.toInteger(data.get("unionpaynum"));
						Integer refunionpaynum = CommUtil.toInteger(data.get("refunionpaynum"));
						
						Integer totalordernum = CommUtil.toInteger(wechatnum + alipaynum +unionpaynum);
						Integer reftotalordernum = CommUtil.toInteger(refwechatnum + refalipaynum +refunionpaynum);
						
						item.put("wechatordernum", wechatnum);
						item.put("refwechatnum", refwechatnum);
						item.put("alipayordernum", alipaynum);
						item.put("refalipaynum", refalipaynum);
						item.put("unionpayordernum", unionpaynum);
						item.put("refunionpaynum", refunionpaynum);
						item.put("totalordernum", totalordernum);
						item.put("reftotalordernum", reftotalordernum);
					}
				}
			}
			//将充值收益数据放入对应list<Map>中
			for(Map<String, Object> item : meralluser){
				Integer meriditem = CommUtil.toInteger(item.get("merid"));
				for(Map<String, Object> data : merIncomeTopup){
					Integer meriddata = CommUtil.toInteger(data.get("merid"));
					if(meriditem.equals(meriddata)){
						
						Double wechatmoney = CommUtil.toDouble(data.get("wechatmoney"));
						Double alipaymoney = CommUtil.toDouble(data.get("alipaymoney"));
						Double refwechatmoney = CommUtil.toDouble(data.get("refwechatmoney"));
						Double refalipaymoney = CommUtil.toDouble(data.get("refalipaymoney"));
						Double unionpaymoney = CommUtil.toDouble(data.get("unionpaymoney"));
						Double refunionpaymoney = CommUtil.toDouble(data.get("refunionpaymoney"));
						Double incomepaymentmoney = CommUtil.toDouble(data.get("incomepaymentmoney"));
						
						Double wechatIncome = CommUtil.subBig(wechatmoney, refwechatmoney);
						Double alipayIncome = CommUtil.subBig(alipaymoney, refalipaymoney);
						Double unionpayIncome = CommUtil.subBig(unionpaymoney, refunionpaymoney);
						Double topupIncome = CommUtil.toDouble(wechatIncome + alipayIncome + unionpayIncome);
						
						item.put("wechatmoney", wechatmoney);
						item.put("alipaymoney", alipaymoney);
						item.put("unionpaymoney", unionpaymoney);
						
						item.put("wechatreturnmoney", refwechatmoney);
						item.put("alipayreturnmoney", refalipaymoney);
						item.put("unionpayreturnmoney", refunionpaymoney);
						
//						item.put("refwechatmoney", refwechatmoney);
//						item.put("refalipaymoney", refalipaymoney);
//						item.put("refunionpaymoney", refunionpaymoney);
						item.put("incomepaymentmoney", incomepaymentmoney);
						item.put("wechatincome", wechatIncome);
						item.put("alipayincome", alipayIncome);
						item.put("unionpayincome", unionpayIncome);
						item.put("topupincome", topupIncome);
						
					}
				}		
			}
			
			//将设备收益数据放入对应list<Map>中
			for(Map<String, Object> item : meralluser){
				Integer meriditem = CommUtil.toInteger(item.get("merid"));
				for(Map<String, Object> data : merIncomeDevice){
					Integer meriddata = CommUtil.toInteger(data.get("merid"));
					if(meriditem.equals(meriddata)){
						Double towechatmoney = CommUtil.toDouble(item.get("wechatmoney"));
						Double toalipaymoney = CommUtil.toDouble(item.get("alipaymoney"));
						Double tounionpaymoney = CommUtil.toDouble(item.get("unionpaymoney"));
						Double torefwechatmoney = CommUtil.toDouble(item.get("wechatreturnmoney"));
						Double torefalipaymoney = CommUtil.toDouble(item.get("alipayreturnmoney"));
						Double torefunionpaymoney = CommUtil.toDouble(item.get("unionpayreturnmoney"));
						
						Double towechatIncome = CommUtil.toDouble(item.get("wechatincome"));
						Double toalipayIncome = CommUtil.toDouble(item.get("alipayincome"));
						Double tounionpayIncome = CommUtil.toDouble(item.get("unionpayincome"));
						Double topupIncome = CommUtil.toDouble(item.get("topupincome"));
						
						Double dewechatmoney = CommUtil.toDouble(data.get("wechatmoney"));
						Double dealipaymoney = CommUtil.toDouble(data.get("alipaymoney"));
						Double deunionpaymoney = CommUtil.toDouble(data.get("unionpaymoney"));
						Double derefwechatmoney = CommUtil.toDouble(data.get("refwechatmoney"));
						Double derefalipaymoney = CommUtil.toDouble(data.get("refalipaymoney"));
						Double derefunionpaymoney = CommUtil.toDouble(data.get("refunionpaymoney"));
						
						Double dewechatIncome = CommUtil.subBig(dewechatmoney, derefwechatmoney);
						Double dealipayIncome = CommUtil.subBig(dealipaymoney, derefalipaymoney);
						Double deunionpayIncome = CommUtil.subBig(deunionpaymoney, derefunionpaymoney);
						Double deviceIncome = CommUtil.toDouble(dewechatIncome + dealipayIncome + deunionpayIncome);
						
						Double wechatmoney = CommUtil.addBig(towechatmoney, dewechatmoney);
						Double alipaymoney = CommUtil.addBig(toalipaymoney, dealipaymoney);
						Double unionpaymoney = CommUtil.addBig(tounionpaymoney, deunionpaymoney);
						Double refwechatmoney = CommUtil.addBig(torefwechatmoney, derefwechatmoney);
						Double refalipaymoney = CommUtil.addBig(torefalipaymoney, derefalipaymoney);
						Double refunionpaymoney = CommUtil.addBig(torefunionpaymoney, derefunionpaymoney);
						
						Double returnmoney = CommUtil.toDouble(refwechatmoney + refalipaymoney + refunionpaymoney);
						Double wechatIncome = CommUtil.addBig(towechatIncome, dewechatIncome);
						Double alipayIncome = CommUtil.addBig(toalipayIncome, dealipayIncome);
						Double unionpayIncome = CommUtil.addBig(tounionpayIncome, deunionpayIncome);
						Double incomemoney = CommUtil.addBig(topupIncome, deviceIncome);
						
						item.put("wechatmoney", wechatmoney);
						item.put("alipaymoney", alipaymoney);
						item.put("unionpaymoney", unionpaymoney);
						item.put("wechatreturnmoney", refwechatmoney);
						item.put("alipayreturnmoney", refalipaymoney);
						item.put("unionpayreturnmoney", refunionpaymoney);
						item.put("wechatincome", wechatIncome);
						item.put("alipayincome", alipayIncome);
						item.put("unionpayincome", unionpayIncome);
						item.put("deviceincome", deviceIncome);
						item.put("incomemoney", incomemoney);
						item.put("returnmoney", returnmoney);
					}
				}
			}
			return meralluser;
		}catch (Exception e) {
			e.printStackTrace();
			return meralluser;
		}
	}


	/**
	 * @method_name: inquireDealerIncomeData
	 * @Description: 查询商户收益汇总信息（设备收益与充值收益分开记录）
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月13日 下午5:50:46
	 * @common:
	 */
	@Override
	public Object inquireDealerIncomeData(HttpServletRequest request) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()) return CommUtil.responseBuildInfo(102, "参数传递不正确或为空", resultdata);
			Integer source =  CommUtil.toInteger(maparam.get("source"));
			if(source==1){//source来源    1:PC端页面   2:手机端页面
				
			}else{
				
			}
			Integer merid =  CommUtil.toInteger(maparam.get("merid"));
			String nickname =  CommUtil.toString(maparam.get("nick"));
			//默认十五天数据
			String begintime = StringUtil.getPastDate(15);
			String endtime = StringUtil.getPastDate(0);
			
			
			CommUtil.responseBuildInfo(200, "成功", resultdata);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", resultdata);
		}
		return resultdata;
	}
	
	public Object pcinquireDealerIncomeData(HttpServletRequest request) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()) return CommUtil.responseBuildInfo(102, "参数传递不正确或为空", resultdata);
			Integer source =  CommUtil.toInteger(maparam.get("source"));
			if(source==1){//source来源    1:PC端页面   2:手机端页面
				
			}else{
				
			}
			Integer merid =  CommUtil.toInteger(maparam.get("merid"));
			String nickname =  CommUtil.toString(maparam.get("nick"));
			//默认十五天数据
			String begintime = StringUtil.getPastDate(15);
			String endtime = StringUtil.getPastDate(0);
			
			
			CommUtil.responseBuildInfo(200, "成功", resultdata);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", resultdata);
		}
		return resultdata;
	}
	
	public Object webinquireDealerIncomeData(HttpServletRequest request) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()) return CommUtil.responseBuildInfo(102, "参数传递不正确或为空", resultdata);
			Integer source =  CommUtil.toInteger(maparam.get("source"));
			if(source==1){//source来源    1:PC端页面   2:手机端页面
				
			}else{
				
			}
//			String begintime = CommUtil.toDateTime();
//			String endtime = CommUtil.toDateTime();
////			 upEarningName(info, type, merid, begintime, endtime, showincoins);
//			 Date timebe = StringUtil.DateTime(begintime, "yyyy-MM-dd HH:mm:ss");
//				Date timeen = StringUtil.DateTime(endtime, "yyyy-MM-dd HH:mm:ss");
//				Boolean fale = StringUtil.belongCalendar(new Date(), timebe, timeen);
//				Map<String, Object> map = new HashMap<String, Object>();
//				if(fale){
//					Parameters paramet = new Parameters();
//					paramet.setUid(merid.toString());
//					map = equipmentDao.agentEquipCollect(paramet);
//					if(null==map) map = new HashMap<>();
//					nowonearn = CommUtil.toDouble(map.get("nowonearn"));
//					nowcoinsearn = CommUtil.toDouble(map.get("nowcoinsearn"));
//				}
//				Parameters params = new Parameters();
//				params.setUid(merid.toString());
//				params.setType("2");
//				params.setStartTime(begintime);
//				params.setEndTime(endtime);
//				Map<String, Object> codestmap = CommUtil.isMapEmpty(codestatisticsDao.agentmoneycollect(params));
//				//根据时间计算商家一段时间的总耗电
////				Integer totalConsume=chargeRecordDao.todayConsumeQuantity(merid, begintime, endtime);
////				if(totalConsume==null){totalConsume=0;}
//				
//				Double moneytotal = CommUtil.toDouble(codestmap.get("moneytotal"));
//				Double wechatretmoney = CommUtil.toDouble(codestmap.get("wechatretmoney"));
//				Double alipayretmoney = CommUtil.toDouble(codestmap.get("alipayretmoney"));
//				Double paymentmoney = CommUtil.toDouble(codestmap.get("paymentmoney"));
//				
//				Double returnmoney = StringUtil.addBig(wechatretmoney, alipayretmoney);
//				moneytotal = StringUtil.subBig(moneytotal, returnmoney);
//				moneytotal = StringUtil.subBig(moneytotal, paymentmoney);
//				
//				Double incoinsmoney = CommUtil.toDouble(codestmap.get("incoinsmoney"));
//				Double oncardmoney = CommUtil.toDouble(codestmap.get("oncardmoney"));
//				Double windowpulsemoney = CommUtil.toDouble(codestmap.get("windowpulsemoney"));
//				
//				totalonearn = CommUtil.addBig(nowonearn, moneytotal);
//				Double coinsmoney = CommUtil.addBig(windowpulsemoney, incoinsmoney);
//				totalcoinsearn = CommUtil.addBig(coinsmoney, nowcoinsearn);
//				totaloncardmoney = oncardmoney;
//				
//				totalConsume = CommUtil.toDouble(codestmap.get("consumequantity"));
//				
//				Double totalmoney = totalonearn;
//				if(showincoins.equals(1)) totalmoney = StringUtil.addBig(totalonearn,totalcoinsearn);
//				//营收总额（ajax请求和默认请求）
//				map.put("paymentmoney", paymentmoney);
//				
//				
//				map.put("totalConsume", CommUtil.toDouble(totalConsume)/100);
//				map.put("totalmoney", totalmoney);
//				map.put("totalonearn", totalonearn);
//				map.put("totalcoinsearn", totalcoinsearn);
//				map.put("totaloncardmoney", totaloncardmoney);
//				map.put("totalmoney", totalmoney);
//				map.put("counttime", StringUtil.getPastDate(0));
//				result.add(map);
//			
//			Integer merid =  CommUtil.toInteger(maparam.get("merid"));
//			String nickname =  CommUtil.toString(maparam.get("nick"));
//			//默认十五天数据
//			String begintime = StringUtil.getPastDate(15);
//			String endtime = StringUtil.getPastDate(0);
//			
			
			CommUtil.responseBuildInfo(200, "成功", resultdata);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", resultdata);
		}
		return resultdata;
	}
	
	/**
	 * @method_name: inquireDealerIncomeData
	 * @Description: 查询商户汇总数据信息
	 * @Author: origin  创建时间:2020年8月13日 下午7:34:21
	 */
	public List<Map<String, Object>> inquireDealerIncomeData() {
		try {
			Parameters parame = new Parameters();
			List<Map<String,Object>> reslut = CommUtil.isListMapEmpty(amendDataDao.inquireDealerIncomeData(parame));
			return reslut;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String, Object>>();
			//return CommUtil.isListMapEmpty(null);
		}
	}


	

	


	
	
	
}
