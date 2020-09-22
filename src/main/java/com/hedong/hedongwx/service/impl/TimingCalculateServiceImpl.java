package com.hedong.hedongwx.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.dao.CollectStatisticsDao;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.service.TimingCalculateService;
import com.hedong.hedongwx.utils.CommUtil;

/**
 * @author  origin
 * 创建时间：   2019年3月2日 下午5:38:35  
 */
@Service
public class TimingCalculateServiceImpl implements TimingCalculateService {
	
	@Autowired
	private CollectStatisticsDao collectStatisticsDao;
	

	
	/**
	 * @Description: 充值、设备收益分开计算商户汇总信息
	 * @param time 计算[汇总]的时间
	 * @return
	 * @Author: origin  创建时间:2020年8月14日 下午3:57:13
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
			/** 获取绑定设备的商户id与合伙人的商户id */
			List<Map<String, Object>> merusermap = collectStatisticsDao.inquireAllmeridInfo();
			/** 根据商户id添加对应的商户收益信息数据   */
			List<Map<String, Object>> resultMerIncome = dealerIncomeData(merusermap, begintime, endtime);
			/** 根据商户id添加对应的消耗电量、时间、窗口投币、在线卡、离线卡  信息数据   */
			List<Map<String, Object>> resultChargeIncfo = disposeChargeConsume(resultMerIncome, begintime, endtime);
			/** 根据商户id添加对应的脉冲投币[投钢镚]的信息数据    */
			List<Map<String, Object>> result = disposeIncoinsNum(resultChargeIncfo, begintime, endtime);
			/** 将汇总出来的数据插入到数据库里   */
			collectStatisticsDao.insertionDealerIncome(result);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	
	

	public List<Map<String, Object>> dealerIncomeData(List<Map<String, Object>> meralluser, String beginTime, String endTime) {
		try{
			Parameters parame = new Parameters();
			parame.setStartTime(beginTime);
			parame.setEndTime(endTime);
			
			System.out.println("004  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			/** 商户收益部分订单信息汇总  */
			List<Map<String, Object>> merIncomeOrder = CommUtil.isListMapEmpty(collectStatisticsDao.dealerIncomeOrder(parame));

			System.out.println("005  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			/** 商户收益部分充值金额汇总  */
			parame.setSource("5,6,7");
			List<Map<String, Object>> merIncomeTopup = CommUtil.isListMapEmpty(collectStatisticsDao.dealerTopupIncome(parame));

			/** 商户收益部分设备收入金额汇总  */
			parame.setSource("1,2,3");
			List<Map<String, Object>> merIncomeDevice = CommUtil.isListMapEmpty(collectStatisticsDao.dealerDeviceIncome(parame));

			
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
	 * @method_name: disposeChargeConsume
	 * @Description: 处理充电中消耗电量、时间并填入对应list里[根据商户id添加对应的消耗电量、时间、窗口投币、在线卡、离线卡  信息数据]
	 * @param meralluser:需要汇总用户信息    beginTime:开始时间     endTime:结束时间
	 * @return
	 * @Author: origin  创建时间:2020年8月14日 下午4:09:36
	 * @common:
	 */
	public List<Map<String, Object>> disposeChargeConsume(List<Map<String, Object>> meralluser, String beginTime, String endTime) {
		try {
			Parameters parame = new Parameters();
			parame.setStartTime(beginTime);
			parame.setEndTime(endTime);
			///根据表（充电记录表）查询充电设备上传的窗口投币、离线刷卡消费、在线刷卡消费的信息
			List<Map<String, Object>> chargeresult = CommUtil.isListMapEmpty(collectStatisticsDao.chargeConsumeData(parame));
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
	
	/**
	 * @method_name: disposeIncoinsNum
	 * @Description: 处理脉冲投币金额并填入对应list里[根据商户id添加对应的脉冲投币[投钢镚]的信息数据]
	 * @param meralluser:需要汇总用户信息    beginTime:开始时间     endTime:结束时间
	 * @return
	 * @Author: origin  创建时间:2020年8月14日 下午4:07:07
	 * @common:
	 */
	public List<Map<String, Object>> disposeIncoinsNum(List<Map<String, Object>> meralluser, String beginTime, String endTime) {
		try {
			Parameters parame = new Parameters();
			parame.setStartTime(beginTime);
			parame.setEndTime(endTime);
			System.out.println("006  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			//查询脉冲数据信息
			List<Map<String, Object>> coinsincome = CommUtil.isListMapEmpty(collectStatisticsDao.incoinsNumStatistics(parame));
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
	
//	/**
//	 * @Description： 将对应的 Map<String, Object> 的数据转换为 Map<String, BigDecimal> 类型数据并计算该map的汇总
//	 * @author： origin    
//	 */
//	public Map<String, BigDecimal> totalBigDecimal(Map<String, Object> map) {
//		if(map==null) map = new HashMap<>();
//		Map<String, BigDecimal> mapdata = new HashMap<>();
///*		//钱包
//		BigDecimal walletnum = StringUtil.togetBigDecimal(map.get("walletnum"));
//		BigDecimal walletmoney = StringUtil.togetBigDecimal(map.get("walletmoney"));
//		BigDecimal refwalletnum = StringUtil.togetBigDecimal(map.get("refwalletnum"));
//		BigDecimal refwalletmoney = StringUtil.togetBigDecimal(map.get("refwalletmoney"));*/
//		mapdata.put("wechatnum", StringUtil.togetBigDecimal(map.get("wechatnum")));
//		mapdata.put("alipaynum", StringUtil.togetBigDecimal(map.get("alipaynum")));
//		mapdata.put("wecappletnum", StringUtil.togetBigDecimal(map.get("wecappletnum")));
//		mapdata.put("aliappletnum", StringUtil.togetBigDecimal(map.get("aliappletnum")));
//		
//		mapdata.put("refwechatnum", StringUtil.togetBigDecimal(map.get("refwechatnum")));
//		mapdata.put("refalipaynum", StringUtil.togetBigDecimal(map.get("refalipaynum")));
//		mapdata.put("refwecappletnum", StringUtil.togetBigDecimal(map.get("refwecappletnum")));
//		mapdata.put("refaliappletnum", StringUtil.togetBigDecimal(map.get("refaliappletnum")));
//		
//		mapdata.put("wechatmoney", StringUtil.togetBigDecimal(map.get("wechatmoney")));
//		mapdata.put("alipaymoney", StringUtil.togetBigDecimal(map.get("alipaymoney")));
//		mapdata.put("wecappletmoney", StringUtil.togetBigDecimal(map.get("wecappletmoney")));
//		mapdata.put("aliappletmoney", StringUtil.togetBigDecimal(map.get("aliappletmoney")));
//		
//		mapdata.put("refwechatmoney", StringUtil.togetBigDecimal(map.get("refwechatmoney")));
//		mapdata.put("refalipaymoney", StringUtil.togetBigDecimal(map.get("refalipaymoney")));
//		mapdata.put("refwecappletmoney", StringUtil.togetBigDecimal(map.get("refwecappletmoney")));
//		mapdata.put("refaliappletmoney", StringUtil.togetBigDecimal(map.get("refaliappletmoney")));
//		
//		mapdata.put("incoins", StringUtil.togetBigDecimal(map.get("incoins")));
//		mapdata.put("incoinsmoney", StringUtil.togetBigDecimal(map.get("incoinsmoney")));
//		
//		mapdata.put("totalorder", mapdata.get("wechatnum").add(mapdata.get("alipaynum")).add(mapdata.get("wecappletnum")).add(mapdata.get("aliappletnum")));
//		mapdata.put("totalreforder", mapdata.get("refwechatnum").add(mapdata.get("refalipaynum")).add(mapdata.get("refwecappletnum")).add(mapdata.get("refaliappletnum")));
//		mapdata.put("totalmoney", mapdata.get("wechatmoney").add(mapdata.get("alipaymoney")).add(mapdata.get("wecappletmoney")).add(mapdata.get("aliappletmoney")));
//		mapdata.put("totalrefmoney", mapdata.get("refwechatmoney").add(mapdata.get("refalipaymoney")).add(mapdata.get("refwecappletmoney")).add(mapdata.get("refaliappletmoney")));
//		mapdata.put("incomemoney", mapdata.get("totalmoney").subtract(mapdata.get("totalrefmoney")));
//		return mapdata;
//	}
//	
//	/**
//	 * @Description： 商户收益信息计算处理（相加）
//	 * @author： origin
//	 */
//	public Map<String, BigDecimal> agentincomeTotal(Map<String, BigDecimal> agentincome, Map<String, BigDecimal> partnerincome) {
//		if(agentincome==null) agentincome = new HashMap<>();
//		if(partnerincome==null) partnerincome = new HashMap<>();
//		Map<String, BigDecimal> totalincome = new HashMap<>();
//		
//		totalincome.put("wechatnum", agentincome.get("wechatnum").add(partnerincome.get("wechatnum")));
//		totalincome.put("alipaynum", agentincome.get("alipaynum").add(partnerincome.get("alipaynum")));
//		totalincome.put("wecappletnum", agentincome.get("wecappletnum").add(partnerincome.get("wecappletnum")));
//		totalincome.put("aliappletnum", agentincome.get("aliappletnum").add(partnerincome.get("aliappletnum")));
//		
//		totalincome.put("refwechatnum", agentincome.get("refwechatnum").add(partnerincome.get("refwechatnum")));
//		totalincome.put("refalipaynum", agentincome.get("refalipaynum").add(partnerincome.get("refalipaynum")));
//		totalincome.put("refwecappletnum", agentincome.get("refwecappletnum").add(partnerincome.get("refwecappletnum")));
//		totalincome.put("refaliappletnum", agentincome.get("refaliappletnum").add(partnerincome.get("refaliappletnum")));
//		
//		totalincome.put("wechatmoney", agentincome.get("wechatmoney").add(partnerincome.get("wechatmoney")));
//		totalincome.put("alipaymoney", agentincome.get("alipaymoney").add(partnerincome.get("alipaymoney")));
//		totalincome.put("wecappletmoney", agentincome.get("wecappletmoney").add(partnerincome.get("wecappletmoney")));
//		totalincome.put("aliappletmoney", agentincome.get("aliappletmoney").add(partnerincome.get("aliappletmoney")));
//		
//		totalincome.put("refwechatmoney", agentincome.get("refwechatmoney").add(partnerincome.get("refwechatmoney")));
//		totalincome.put("refalipaymoney", agentincome.get("refalipaymoney").add(partnerincome.get("refalipaymoney")));
//		totalincome.put("refwecappletmoney", agentincome.get("refwecappletmoney").add(partnerincome.get("refwecappletmoney")));
//		totalincome.put("refaliappletmoney", agentincome.get("refaliappletmoney").add(partnerincome.get("refaliappletmoney")));
//		
//		totalincome.put("incoins", agentincome.get("incoins"));
//		totalincome.put("incoinsmoney", agentincome.get("incoinsmoney"));
//		
//		totalincome.put("totalorder", agentincome.get("totalorder").add(partnerincome.get("totalorder")));
//		totalincome.put("totalreforder", agentincome.get("totalreforder").add(partnerincome.get("totalreforder")));
//		totalincome.put("totalmoney", agentincome.get("totalmoney").add(partnerincome.get("totalmoney")));
//		totalincome.put("totalrefmoney", agentincome.get("totalrefmoney").add(partnerincome.get("totalrefmoney")));
//		totalincome.put("incomemoney", totalincome.get("totalmoney").subtract(totalincome.get("totalrefmoney")));
//		return totalincome;
//	}
///*** @author origin ************************************************************************************/	
//	
////	
//	/**
//	 * 根据给定时间，代理商每日汇总计算
//	 * @author  origin          
//	 * @version 创建时间：2019年3月2日  下午5:41:45
//	 */
//	@Override
//	public int agentTimingmoneycollect(String startTime, String endTime) {
//		Parameters paracode = new Parameters();
//		List<UserEquipment> usercode = userEquipmentDao.findAllUserEquipment();
//		int num = 0;
//		for(UserEquipment item : usercode){
//			Integer merid = item.getUserId();
//			//String code = item.getEquipmentCode();
//			paracode.setUid(merid.toString());
//			paracode.setType("1");//选择设备汇总
//			paracode.setStartTime(startTime);
//			paracode.setEndTime(endTime);
//			Map<String, Object> agentcollect = codestatisticsDao.agentmoneycollect(paracode);
//			Map<String, Object> moneyearnings = moneyDao.selectmoneytotal(paracode);
//			Map<String, BigDecimal> money = moneydata(1, moneyearnings);
//			String monorder = money.get("wechatorder").toString(); 
//			BigDecimal monmoney = money.get("wechatmoney");
//			try {
//				collectmoney(monorder, merid,monmoney, 2, agentcollect, startTime);
//				 num =1;
//			} catch (Exception e) {
//				 num =2;
//				e.printStackTrace();
//			}
//		}
//		return num;
//	}
//	
//	
//
//
//	/**
//	 *
//	 * @author  origin          
//	 * @version 创建时间：2019年3月5日  下午3:07:10
//	 */
//	private void collectmoney(String monorder, Integer merid, BigDecimal monmoney, int type, Map<String, Object> totalData,
//			String startTime) {
//		Codestatistics codestatistics = new Codestatistics();
//		codestatistics.setCode(monorder);
//		codestatistics.setMerid(merid);
//		codestatistics.setAreaid(monmoney.intValue());
//		codestatistics.setType(type);
//		if(totalData==null){		
//			codestatistics.setOrdertotal(0);
//			codestatistics.setWechatorder(0);
//			codestatistics.setAlipayorder(0);
//			codestatistics.setMoneytotal(0.00);
//			codestatistics.setWechatmoney(0.00);
//			codestatistics.setAlipaymoney(0.00);
//			codestatistics.setWechatretord(0);
//			codestatistics.setAlipayretord(0);
//			codestatistics.setWechatretmoney(0.00);
//			codestatistics.setAlipayretmoney(0.00);
//			codestatistics.setIncoinsorder(0);
//			codestatistics.setIncoinsmoney(0.00);
//		}else{
//			int ordertotal = StringUtil.togetBigDecimal(totalData.get("ordertotal")).intValue();
//			int wechatorder = StringUtil.togetBigDecimal(totalData.get("wechatorder")).intValue();
//			int alipayorder = StringUtil.togetBigDecimal(totalData.get("alipayorder")).intValue();
//			int wechatretord = StringUtil.togetBigDecimal(totalData.get("wechatretord")).intValue();
//			int alipayretord = StringUtil.togetBigDecimal(totalData.get("alipayretord")).intValue();
//			int incoinsorder = StringUtil.togetBigDecimal(totalData.get("incoinsorder")).intValue();
//			double moneytotal = StringUtil.togetBigDecimal(totalData.get("moneytotal")).add(monmoney).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//			codestatistics.setOrdertotal(ordertotal);
//			codestatistics.setWechatorder(wechatorder);
//			codestatistics.setAlipayorder(alipayorder);
//			codestatistics.setMoneytotal(moneytotal);
//			codestatistics.setWechatmoney((double) totalData.get("wechatmoney"));
//			codestatistics.setAlipaymoney((double) totalData.get("alipaymoney"));
//			codestatistics.setWechatretord(wechatretord);
//			codestatistics.setAlipayretord(alipayretord);
//			codestatistics.setWechatretmoney((double) totalData.get("wechatretmoney"));
//			codestatistics.setAlipayretmoney((double) totalData.get("alipayretmoney"));
//			codestatistics.setIncoinsorder(incoinsorder);
//			codestatistics.setIncoinsmoney((double) totalData.get("incoinsmoney"));
//		}
//		try {
//			codestatistics.setCountTime(new SimpleDateFormat("yyyy-MM-dd").parse(startTime));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		codestatisticsDao.insertCodestatis(codestatistics);
//		
//	}
//
//
//
//
//	/**
//	 * 根据给定时间，设备每日汇总计算（数据从充电、脉冲、离线卡充值、钱包充值查询）
//	 * @author  origin          
//	 * @version 创建时间：2019年3月2日  下午5:40:12
//	 * @throws Exception 
//	 */
//	@Override
//	public int codeTimingCollect(String startTime, String endTime){
//		
//		Parameters paracode = new Parameters();
//		paracode.setStatus("1");//设备被绑定
//		List<Map<String, Object>> equlist = equipmentDao.selectEquList(paracode);
//		//System.out.println("输出设备：       "+equlist);
//		Parameters parame = new Parameters();
//		for(Map<String, Object> item : equlist){
//			String code = (String) item.get("code");
//			String arid = StringUtil.togetBigDecimal(item.get("aid")).toString();
//			String merid = StringUtil.togetBigDecimal(item.get("merid")).toString();
//			parame.setUid(merid);
//			parame.setCode(code);
//			parame.setStartTime(startTime);
//			parame.setEndTime(endTime);
//			collectData(parame, arid);
//		}
//		return 0;
//	}
//
//	/**
//	 * 分别计算充电、离线充值、脉冲、钱包充值数据
//	 * @author  origin          
//	 * @version 创建时间：2019年3月4日  上午10:53:24
//	 * @param parame 
//	 * @throws Exception 
//	 */
//	private void collectData(Parameters parame, String arid) {
//		Map<String, Object> chargeearnings = chargeRecordDao.selectcollect(parame);
//		Map<String, Object> chargereimburse = chargeRecordDao.selectcollectrefund(parame);
//		Map<String, Object> inCoinsearnings = inCoinsDao.selectsuminfo(parame);
//		Map<String, Object> inCoinsreimburse = inCoinsDao.selectsuminforefund(parame);
//		Map<String, Object> offlineearnings = offlineCardDao.selecttotaloff(parame);
//		Map<String, Object> offlineeimburse = offlineCardDao.selecttotaloffrefund(parame);
//		
//		Map<String, BigDecimal> charge = dataSwitch(1, chargeearnings, 0 );
//		Map<String, BigDecimal> inCoins = dataSwitch(1, inCoinsearnings, 1 );
//		Map<String, BigDecimal> offline = dataSwitch(1, offlineearnings, 0 );
//		
//		Map<String, BigDecimal> chargereim = dataSwitch(2, chargereimburse, 0 );
//		Map<String, BigDecimal> inCoinsreim = dataSwitch(2, inCoinsreimburse, 1 );
//		Map<String, BigDecimal> offlineeim = dataSwitch(2, offlineeimburse, 0 );
//		System.out.println();
//		System.out.println("**************************************************************************************************");
//		System.out.println("输出设备号：       "+parame.getCode());
//		
//
//		System.out.println("输出chargeearnings:     "+chargeearnings);
//		System.out.println("输出charge:     "+charge);
//		System.out.println("输出inCoinsearnings:     "+inCoinsearnings);
//		System.out.println("输出inCoins:     "+inCoins);
//		System.out.println("输出offlineearnings:     "+offlineearnings);
//		System.out.println("输出offline:     "+offline);
//		System.out.println("输出chargereim:     "+chargereim);
//		System.out.println("输出inCoinsreim:     "+inCoinsreim);
//		System.out.println("输出offlineeim:     "+offlineeim);
//		Map<String, BigDecimal> totalData = calculateData(charge, inCoins, offline, chargereim, inCoinsreim, offlineeim);
//		collectmoney(parame.getCode(), parame.getUid(), arid, 1, parame.getStartTime(), totalData);
//		
//	}
//	
//	/**
//	 * 汇总插入数据
//	 * @author  origin          
//	 * @version 创建时间：2019年3月5日  上午8:41:01
//	 */
//	private void collectmoney(String code, String merid , String arid, Integer type, String startTime, Map<String, BigDecimal> totalData) {
//		
//		Codestatistics codestatistics = new Codestatistics();
//		codestatistics.setCode(code);
//		codestatistics.setMerid(StringUtil.getIntString(merid));
//		codestatistics.setAreaid(StringUtil.getIntString(arid));
//		codestatistics.setType(type);
//		double moneytotal = (totalData.get("alipaymoney")).subtract(totalData.get("reimtotalmoney")).doubleValue();
//		codestatistics.setOrdertotal(totalData.get("totalorder").intValue());
//		codestatistics.setWechatorder(totalData.get("wechatorder").intValue());
//		codestatistics.setAlipayorder(totalData.get("alipayorder").intValue());
//		
//		codestatistics.setMoneytotal(moneytotal);
//		codestatistics.setWechatmoney(totalData.get("wechatmoney").doubleValue());
//		codestatistics.setAlipaymoney(totalData.get("alipaymoney").doubleValue());
//		
//		codestatistics.setWechatretord(totalData.get("reimwechatorder").intValue());
//		codestatistics.setAlipayretord(totalData.get("reimalipayorder").intValue());
//		
//		codestatistics.setWechatretmoney(totalData.get("reimwechatmoney").doubleValue());
//		codestatistics.setAlipayretmoney(totalData.get("reimalipaymoney").doubleValue());
//		
//		codestatistics.setIncoinsorder(totalData.get("incoins").intValue());
//		codestatistics.setIncoinsmoney(totalData.get("incoinsmoney").doubleValue());
//		try {
//			codestatistics.setCountTime(new SimpleDateFormat("yyyy-MM-dd").parse(startTime));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		codestatisticsDao.insertCodestatis(codestatistics);
//	}
//	
//	/**
//	 * 微信、支付宝、钱包的消费与退款数据汇总计算
//	 * @author  origin          
//	 * @version 创建时间：2019年3月4日  下午5:02:22
//	 */
//	private Map<String, BigDecimal> calculateData(Map<String, BigDecimal> charge, Map<String, BigDecimal> inCoins, Map<String, BigDecimal> offline, 
//			Map<String, BigDecimal> chargereim,Map<String, BigDecimal> inCoinsreim, Map<String, BigDecimal> offlineeim) {
//		Map<String, BigDecimal> totalData = new HashMap<>();
//		System.out.println("************************************************************************************************************************************");
//		//微信 支付宝 投币 钱包
//		BigDecimal wechatorder = (charge.get("wechatorder")).add(inCoins.get("wechatorder")).add(offline.get("wechatorder"));
//		BigDecimal wechatmoney = (charge.get("wechatmoney")).add(inCoins.get("wechatmoney")).add(offline.get("wechatmoney")).setScale(2, BigDecimal.ROUND_HALF_UP);
//		BigDecimal alipayorder = (charge.get("alipayorder")).add(inCoins.get("alipayorder")).add(offline.get("alipayorder"));
//		BigDecimal alipaymoney = (charge.get("alipaymoney")).add(inCoins.get("alipaymoney")).add(offline.get("alipaymoney")).setScale(2, BigDecimal.ROUND_HALF_UP);
//		BigDecimal incoins = inCoins.get("incoins");
//		BigDecimal incoinsmoney = inCoins.get("incoinsmoney").setScale(2, BigDecimal.ROUND_HALF_UP);
//		
//		//退费 微信 支付宝 投币 钱包
//		BigDecimal reimwechatorder = (chargereim.get("reimwechatorder")).add(inCoinsreim.get("reimwechatorder")).add(offlineeim.get("reimwechatorder"));
//		BigDecimal reimwechatmoney = (chargereim.get("reimwechatmoney")).add(inCoinsreim.get("reimwechatmoney")).add(offlineeim.get("reimwechatmoney")).setScale(2, BigDecimal.ROUND_HALF_UP);
//		BigDecimal reimalipayorder = (chargereim.get("reimalipayorder")).add(inCoinsreim.get("reimalipayorder")).add(offlineeim.get("reimalipayorder"));
//		BigDecimal reimalipaymoney = (chargereim.get("reimalipaymoney")).add(inCoinsreim.get("reimalipaymoney")).add(offlineeim.get("reimalipaymoney")).setScale(2, BigDecimal.ROUND_HALF_UP);
//		
//		BigDecimal totalorder = wechatorder.add(alipayorder);
//		BigDecimal totalmoney = wechatmoney.add(alipaymoney).setScale(2, BigDecimal.ROUND_HALF_UP);;
//		BigDecimal reimtotalorder = reimwechatorder.add(reimalipayorder);
//		BigDecimal reimtotalmoney = reimwechatmoney.add(reimalipaymoney).setScale(2, BigDecimal.ROUND_HALF_UP);;
//		
//		totalData.put("wechatorder", wechatorder);
//		totalData.put("wechatmoney", wechatmoney);
//		totalData.put("alipayorder", alipayorder);
//		totalData.put("alipaymoney", alipaymoney);
//		totalData.put("incoins", incoins);
//		totalData.put("incoinsmoney", incoinsmoney);
//		totalData.put("reimwechatorder", reimwechatorder);
//		totalData.put("reimwechatmoney", reimwechatmoney);
//		totalData.put("reimalipayorder", reimalipayorder);
//		totalData.put("reimalipaymoney", reimalipaymoney);
//		totalData.put("totalorder", totalorder);
//		totalData.put("totalmoney", totalmoney);
//		totalData.put("reimtotalorder", reimtotalorder);
//		totalData.put("reimtotalmoney", reimtotalmoney);
//		return totalData;
//	}
//	
//	/** 
//	 * 微信、支付宝、钱包的消费与退款数据信息转换为BigDecimal
//	 * sort 为1:充值  2:退费  souce为 1:投币
//	 * @author  origin          
//	 * @version 创建时间：2019年3月4日  上午11:32:56
//	 */
//	public Map<String, BigDecimal> dataSwitch(Integer sort, Map<String, Object> mapdata, Integer souce){
//		/*
//		 * Map<String, BigDecimal> charge = dataSwitch(1, chargeearnings, 0 );
//		Map<String, BigDecimal> inCoins = dataSwitch(1, inCoinsearnings, 1 );
//		Map<String, BigDecimal> offline = dataSwitch(1, offlineearnings, 0 );
//		 */
//		
//		Map<String, BigDecimal> map = new HashMap<>();
//		if(mapdata==null){
//			map = nullCalculate(sort, souce);
//		}else{
//			map = earningsCalculate(sort, mapdata, souce);
//		}	
//		return map;
//	}
//	
//	/**
//	 * 对象为空时转换为BigDecimal类型数据，并赋值
//	 * sort 为1:充值  2:退费  souce为 1:投币
//	 * @author  origin          
//	 * @version 创建时间：2019年3月5日  下午4:43:41
//	 */
//	public Map<String, BigDecimal> nullCalculate(Integer sort, Integer souce){
//		Map<String, BigDecimal> map = new HashMap<>();
//		BigDecimal parems = new BigDecimal(0);
//		//reimburse  偿还；赔偿
//		if(sort==1){
//			map.put("walletorder", parems);
//			map.put("wechatorder", parems);
//			map.put("alipayorder", parems);
//			map.put("walletmoney", parems);
//			map.put("wechatmoney", parems);
//			map.put("alipaymoney", parems);
//			if(souce==1){
//				map.put("incoins", parems);//投币订单
//				map.put("incoinsmoney", parems);//投币金额
//			}
//		}else if(sort==2){
//			map.put("reimwalletorder", parems);
//			map.put("reimwechatorder", parems);
//			map.put("reimalipayorder", parems);
//			map.put("reimwalletmoney", parems);
//			map.put("reimwechatmoney", parems);
//			map.put("reimalipaymoney", parems);
//		}
//		
//		return map;
//	}
//	
//	/**
//	 * 对象不为空时转换为BigDecimal类型数据，并赋值
//	 * sort 为1:充值  2:退费  souce为 1:投币
//	 * @author  origin          
//	 * @version 创建时间：2019年3月5日  下午4:43:53
//	 */
//	public Map<String, BigDecimal> earningsCalculate(Integer sort, Map<String, Object> mapdata, Integer souce){
//		Map<String, BigDecimal> map = new HashMap<>(); 
//		if(sort==1){
//			map.put("walletorder", StringUtil.togetBigDecimal(mapdata.get("walletnum")));
//			map.put("wechatorder", StringUtil.togetBigDecimal(mapdata.get("wechatnum")));
//			map.put("alipayorder", StringUtil.togetBigDecimal(mapdata.get("alipaynum")));
//			map.put("walletmoney", StringUtil.togetBigDecimal(mapdata.get("walletmoney")));
//			map.put("wechatmoney", StringUtil.togetBigDecimal(mapdata.get("wechatmoney")));
//			map.put("alipaymoney", StringUtil.togetBigDecimal(mapdata.get("alipaymoney")));
//			if(souce==1){
//				map.put("incoins", StringUtil.togetBigDecimal(mapdata.get("incoins")));//投币订单
//				map.put("incoinsmoney", StringUtil.togetBigDecimal(mapdata.get("incoinsmoney")));//投币金额
//			}
//		}else if(sort==2){
//			map.put("reimwalletorder", StringUtil.togetBigDecimal(mapdata.get("refundwalnum")));
//			map.put("reimwechatorder", StringUtil.togetBigDecimal(mapdata.get("refundwecnum")));
//			map.put("reimalipayorder", StringUtil.togetBigDecimal(mapdata.get("refundalinum")));
//			map.put("reimwalletmoney", StringUtil.togetBigDecimal(mapdata.get("refundwalmoney")));
//			map.put("reimwechatmoney", StringUtil.togetBigDecimal(mapdata.get("refundwecmoney")));
//			map.put("reimalipaymoney", StringUtil.togetBigDecimal(mapdata.get("refundalimoney")));
//		}
//		return map;
//	}
//	
//
//	
//	/**
//	 * 充电记录数据记录计算
//	 * @author  origin          
//	 * @version 创建时间：2019年3月4日  上午11:32:56
//	 */
//	public Map<String, BigDecimal> chargerCalculate(Integer sort, Map<String, Object> chargermap){
//		Map<String, BigDecimal> map = new HashMap<>();
//		BigDecimal parem = new BigDecimal(0);
//		if(sort==1){//充值
//			if(chargermap==null){
//				map.put("walletorder", parem);
//				map.put("wechatorder", parem);
//				map.put("alipayorder", parem);
//				map.put("walletmoney", parem);
//				map.put("wechatmoney", parem);
//				map.put("alipaymoney", parem);
//			}else{
//				map.put("walletorder", StringUtil.togetBigDecimal(chargermap.get("walletnum")));
//				map.put("wechatorder", StringUtil.togetBigDecimal(chargermap.get("wechatnum")));
//				map.put("alipayorder", StringUtil.togetBigDecimal(chargermap.get("alipaynum")));
//				map.put("walletmoney", StringUtil.togetBigDecimal(chargermap.get("walletmoney")));
//				map.put("wechatmoney", StringUtil.togetBigDecimal(chargermap.get("wechatmoney")));
//				map.put("alipaymoney", StringUtil.togetBigDecimal(chargermap.get("alipaymoney")));
//			}
//		}else if(sort==2){//tuifei
//			if(chargermap==null){
//				map.put("reimwalletorder", parem);
//				map.put("reimwechatorder", parem);
//				map.put("reimalipayorder", parem);
//				map.put("reimwalletmoney", parem);
//				map.put("reimwechatmoney", parem);
//				map.put("reimalipaymoney", parem);
//			}else{
//				map.put("reimwalletorder", StringUtil.togetBigDecimal(chargermap.get("refundwalnum")));
//				map.put("reimwechatorder", StringUtil.togetBigDecimal(chargermap.get("refundwecnum")));
//				map.put("reimalipayorder", StringUtil.togetBigDecimal(chargermap.get("refundalinum")));
//				map.put("reimwalletmoney", StringUtil.togetBigDecimal(chargermap.get("refundwalmoney")));
//				map.put("reimwechatmoney", StringUtil.togetBigDecimal(chargermap.get("refundwecmoney")));
//				map.put("reimalipaymoney", StringUtil.togetBigDecimal(chargermap.get("refundalimoney")));
//			}
//		}
//		return map;
//	}
//	
//	/**
//	 * 投币记录信息数据记录计算
//	 * @author  origin          
//	 * @version 创建时间：2019年3月4日  上午11:34:09
//	 */
//	public Map<String, BigDecimal> incoinsCalculate(Integer sort, Map<String, Object> incoinstmap){
//		Map<String, BigDecimal> map = new HashMap<>();
//		BigDecimal parem = new BigDecimal(0);
//		if(sort==1){//充值
//			if(incoinstmap==null){
//				map.put("walletorder", parem);
//				map.put("wechatorder", parem);
//				map.put("alipayorder", parem);
//				map.put("walletmoney", parem);
//				map.put("wechatmoney", parem);
//				map.put("alipaymoney", parem);
//				map.put("incoins", parem);//投币订单
//				map.put("incoinsmoney", parem);//投币金额
//			}else{
//				map.put("walletorder", StringUtil.togetBigDecimal(incoinstmap.get("walletnum")));
//				map.put("wechatorder", StringUtil.togetBigDecimal(incoinstmap.get("wechatnum")));
//				map.put("alipayorder", StringUtil.togetBigDecimal(incoinstmap.get("alipaynum")));
//				map.put("walletmoney", StringUtil.togetBigDecimal(incoinstmap.get("walletmoney")));
//				map.put("wechatmoney", StringUtil.togetBigDecimal(incoinstmap.get("wechatmoney")));
//				map.put("alipaymoney", StringUtil.togetBigDecimal(incoinstmap.get("alipaymoney")));
//				map.put("incoins", StringUtil.togetBigDecimal(incoinstmap.get("incoins")));//投币订单
//				map.put("incoinsmoney", StringUtil.togetBigDecimal(incoinstmap.get("incoinsmoney")));//投币金额
//			}
//		}else if(sort==2){//tuifei
//			if(incoinstmap==null){
//				map.put("reimwalletorder", parem);
//				map.put("reimwechatorder", parem);
//				map.put("reimalipayorder", parem);
//				map.put("reimwalletmoney", parem);
//				map.put("reimwechatmoney", parem);
//				map.put("reimalipaymoney", parem);
//			}else{
//				map.put("reimwalletorder", StringUtil.togetBigDecimal(incoinstmap.get("refundwalnum")));
//				map.put("reimwechatorder", StringUtil.togetBigDecimal(incoinstmap.get("refundwecnum")));
//				map.put("reimalipayorder", StringUtil.togetBigDecimal(incoinstmap.get("refundalinum")));
//				map.put("reimwalletmoney", StringUtil.togetBigDecimal(incoinstmap.get("refundwalmoney")));
//				map.put("reimwechatmoney", StringUtil.togetBigDecimal(incoinstmap.get("refundwecmoney")));
//				map.put("reimalipaymoney", StringUtil.togetBigDecimal(incoinstmap.get("refundalimoney")));
//			}
//		}
//		return map;
//	}
//	
//	/**
//	 * 离线卡充值信息数据记录计算
//	 * @author  origin          
//	 * @version 创建时间：2019年3月4日  上午11:34:39
//	 */
//	@SuppressWarnings("unused")
//	private Map<String, BigDecimal> offlineCalculate(Integer sort, Map<String, Object> offlinemap) {
//		Map<String, BigDecimal> map = new HashMap<>();
//		BigDecimal parem = new BigDecimal(0);
//		if(sort==1){//充值
//			if(offlinemap==null){
//				map.put("walletorder", parem);
//				map.put("wechatorder", parem);
//				map.put("alipayorder", parem);
//				map.put("walletmoney", parem);
//				map.put("wechatmoney", parem);
//				map.put("alipaymoney", parem);
//			}else{
//				map.put("walletorder", StringUtil.togetBigDecimal(offlinemap.get("walletnum")));
//				map.put("wechatorder", StringUtil.togetBigDecimal(offlinemap.get("wechatnum")));
//				map.put("alipayorder", StringUtil.togetBigDecimal(offlinemap.get("alipaynum")));
//				map.put("walletmoney", StringUtil.togetBigDecimal(offlinemap.get("walletmoney")));
//				map.put("wechatmoney", StringUtil.togetBigDecimal(offlinemap.get("wechatmoney")));
//				map.put("alipaymoney", StringUtil.togetBigDecimal(offlinemap.get("alipaymoney")));
//			}
//		}else if(sort==2){//tuifei
//			if(offlinemap==null){
//				map.put("reimwalletorder", parem);
//				map.put("reimwechatorder", parem);
//				map.put("reimalipayorder", parem);
//				map.put("reimwalletmoney", parem);
//				map.put("reimwechatmoney", parem);
//				map.put("reimalipaymoney", parem);
//			}else{
//				map.put("reimwalletorder", StringUtil.togetBigDecimal(offlinemap.get("refundwalnum")));
//				map.put("reimwechatorder", StringUtil.togetBigDecimal(offlinemap.get("refundwecnum")));
//				map.put("reimalipayorder", StringUtil.togetBigDecimal(offlinemap.get("refundalinum")));
//				map.put("reimwalletmoney", StringUtil.togetBigDecimal(offlinemap.get("refundwalmoney")));
//				map.put("reimwechatmoney", StringUtil.togetBigDecimal(offlinemap.get("refundwecmoney")));
//				map.put("reimalipaymoney", StringUtil.togetBigDecimal(offlinemap.get("refundalimoney")));
//			}
//		}
//		return map;
//	}
//	
//	/**
//	 * 充值信息数据计算
//	 * @author  origin          
//	 * @version 创建时间：2019年3月4日  上午11:35:41
//	 */
//	private Map<String, BigDecimal> moneydata(Integer sort, Map<String, Object> moneymap) {
//		Map<String, BigDecimal> map = new HashMap<>();
//		BigDecimal parem = new BigDecimal(0);
//		if(sort==1){//充值  wechatorder wechatmoney
//			if(moneymap==null){
//				map.put("wechatorder", parem);
//				map.put("wechatmoney", parem);
//				map.put("onlineorder", parem);
//				map.put("onlinemoney", parem);
//			}else{
//				map.put("wechatorder", StringUtil.togetBigDecimal(moneymap.get("walletnum")));
//				map.put("wechatmoney", StringUtil.togetBigDecimal(moneymap.get("walletmoney")));
//				map.put("onlineorder", StringUtil.togetBigDecimal(moneymap.get("onlinenum")));
//				map.put("onlinemoney", StringUtil.togetBigDecimal(moneymap.get("onlinemoney")));
//			}
//		}else if(sort==2){
//			if(moneymap==null){
//				map.put("walletorder", parem);
//				map.put("walletmoney", parem);
//				map.put("onlineorder", parem);
//				map.put("onlinemoney", parem);
//				map.put("walletorder", parem);
//				map.put("walletmoney", parem);
//				map.put("onlineorder", parem);
//				map.put("onlinemoney", parem);
//			}else{
//				map.put("reimonlineorder", StringUtil.togetBigDecimal(moneymap.get("onlinerefnum")));
//				map.put("reimonlinemoney", StringUtil.togetBigDecimal(moneymap.get("onlinerefmoney")));
//				map.put("reimchargeorder", StringUtil.togetBigDecimal(moneymap.get("chargenum")));
//				map.put("reimchargemoney", StringUtil.togetBigDecimal(moneymap.get("chargewalmoney")));
//				map.put("reimofflineorder", StringUtil.togetBigDecimal(moneymap.get("offlinenum")));
//				map.put("reimofflinemoney", StringUtil.togetBigDecimal(moneymap.get("offlinewalmoney")));
//				map.put("reimincoinsrder", StringUtil.togetBigDecimal(moneymap.get("incoinsnum")));
//				map.put("reimincoinsmoney", StringUtil.togetBigDecimal(moneymap.get("incoinswalmoney")));
//			}
//		}
//		return map;
//	}
}
