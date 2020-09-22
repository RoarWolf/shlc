package com.hedong.hedongwx.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.AreaDao;
import com.hedong.hedongwx.dao.ChargeRecordDao;
import com.hedong.hedongwx.dao.CodestatisticsDao;
import com.hedong.hedongwx.dao.DealerAuthorityDao;
import com.hedong.hedongwx.dao.EquipmentDao;
import com.hedong.hedongwx.dao.InCoinsDao;
import com.hedong.hedongwx.dao.MerchantDetailDao;
import com.hedong.hedongwx.dao.MoneyDao;
import com.hedong.hedongwx.dao.OfflineCardDao;
import com.hedong.hedongwx.dao.OnlineCardRecordDao;
import com.hedong.hedongwx.dao.StatisticsDao;
import com.hedong.hedongwx.dao.TradeRecordDao;
import com.hedong.hedongwx.dao.UserDao;
import com.hedong.hedongwx.dao.UserEquipmentDao;
import com.hedong.hedongwx.entity.Codestatistics;
import com.hedong.hedongwx.entity.DealerAuthority;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.Statistics;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.entity.Withdraw;
import com.hedong.hedongwx.service.StatisticsService;
import com.hedong.hedongwx.service.WithdrawService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.TempMsgUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class StatisticsServiceImpl implements StatisticsService{
	
	@Autowired
	private StatisticsDao statisticsDao;
	@Autowired
	private ChargeRecordDao chargeRecordDao;
	@Autowired
	private InCoinsDao inCoinsDao;
	@Autowired
	private OfflineCardDao offlineCardDao;
	@Autowired
	private OnlineCardRecordDao onlineCardRecordDao;
	@Autowired
	private MoneyDao moneyDao;
	@Autowired
	private TradeRecordDao tradeRecordDao;
	@Autowired
	private EquipmentDao equipmentDao;
	@Autowired
	private UserEquipmentDao userEquipmentDao;
	@Autowired
	private CodestatisticsDao codestatisticsDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private MerchantDetailDao merchantDetailDao;
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private WithdrawService withdrawService;
	@Autowired
	private DealerAuthorityDao dealerAuthorityDao;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * @Description： 将对应的 Map<String, Object> 的数据转换为 Map<String, BigDecimal> 类型数据并计算该map的汇总
	 * @author： origin    
	 */
	public Map<String, BigDecimal> totalBigDecimal(Map<String, Object> map) {
		if(map==null) map = new HashMap<>();
		Map<String, BigDecimal> mapdata = new HashMap<>();
/*		//钱包
		BigDecimal walletnum = StringUtil.togetBigDecimal(map.get("walletnum"));
		BigDecimal walletmoney = StringUtil.togetBigDecimal(map.get("walletmoney"));
		BigDecimal refwalletnum = StringUtil.togetBigDecimal(map.get("refwalletnum"));
		BigDecimal refwalletmoney = StringUtil.togetBigDecimal(map.get("refwalletmoney"));*/
		mapdata.put("wechatnum", StringUtil.togetBigDecimal(map.get("wechatnum")));
		mapdata.put("alipaynum", StringUtil.togetBigDecimal(map.get("alipaynum")));
		mapdata.put("wecappletnum", StringUtil.togetBigDecimal(map.get("wecappletnum")));
		mapdata.put("aliappletnum", StringUtil.togetBigDecimal(map.get("aliappletnum")));
		
		mapdata.put("refwechatnum", StringUtil.togetBigDecimal(map.get("refwechatnum")));
		mapdata.put("refalipaynum", StringUtil.togetBigDecimal(map.get("refalipaynum")));
		mapdata.put("refwecappletnum", StringUtil.togetBigDecimal(map.get("refwecappletnum")));
		mapdata.put("refaliappletnum", StringUtil.togetBigDecimal(map.get("refaliappletnum")));
		
		mapdata.put("wechatmoney", StringUtil.togetBigDecimal(map.get("wechatmoney")));
		mapdata.put("alipaymoney", StringUtil.togetBigDecimal(map.get("alipaymoney")));
		mapdata.put("wecappletmoney", StringUtil.togetBigDecimal(map.get("wecappletmoney")));
		mapdata.put("aliappletmoney", StringUtil.togetBigDecimal(map.get("aliappletmoney")));
		
		mapdata.put("refwechatmoney", StringUtil.togetBigDecimal(map.get("refwechatmoney")));
		mapdata.put("refalipaymoney", StringUtil.togetBigDecimal(map.get("refalipaymoney")));
		mapdata.put("refwecappletmoney", StringUtil.togetBigDecimal(map.get("refwecappletmoney")));
		mapdata.put("refaliappletmoney", StringUtil.togetBigDecimal(map.get("refaliappletmoney")));
		
		mapdata.put("incoins", StringUtil.togetBigDecimal(map.get("incoins")));
		mapdata.put("incoinsmoney", StringUtil.togetBigDecimal(map.get("incoinsmoney")));
		
		mapdata.put("totalorder", mapdata.get("wechatnum").add(mapdata.get("alipaynum")).add(mapdata.get("wecappletnum")).add(mapdata.get("aliappletnum")));
		mapdata.put("totalreforder", mapdata.get("refwechatnum").add(mapdata.get("refalipaynum")).add(mapdata.get("refwecappletnum")).add(mapdata.get("refaliappletnum")));
		mapdata.put("totalmoney", mapdata.get("wechatmoney").add(mapdata.get("alipaymoney")).add(mapdata.get("wecappletmoney")).add(mapdata.get("aliappletmoney")));
		mapdata.put("totalrefmoney", mapdata.get("refwechatmoney").add(mapdata.get("refalipaymoney")).add(mapdata.get("refwecappletmoney")).add(mapdata.get("refaliappletmoney")));
		mapdata.put("incomemoney", mapdata.get("totalmoney").subtract(mapdata.get("totalrefmoney")));
		return mapdata;
	}
	
	/**
	 * @Description： 商户收益信息计算处理（相加）
	 * @author： origin
	 */
	public Map<String, BigDecimal> agentincomeTotal(Map<String, BigDecimal> agentincome, Map<String, BigDecimal> partnerincome) {
		if(agentincome==null) agentincome = new HashMap<>();
		if(partnerincome==null) partnerincome = new HashMap<>();
		Map<String, BigDecimal> totalincome = new HashMap<>();
		
		totalincome.put("wechatnum", agentincome.get("wechatnum").add(partnerincome.get("wechatnum")));
		totalincome.put("alipaynum", agentincome.get("alipaynum").add(partnerincome.get("alipaynum")));
		totalincome.put("wecappletnum", agentincome.get("wecappletnum").add(partnerincome.get("wecappletnum")));
		totalincome.put("aliappletnum", agentincome.get("aliappletnum").add(partnerincome.get("aliappletnum")));
		
		totalincome.put("refwechatnum", agentincome.get("refwechatnum").add(partnerincome.get("refwechatnum")));
		totalincome.put("refalipaynum", agentincome.get("refalipaynum").add(partnerincome.get("refalipaynum")));
		totalincome.put("refwecappletnum", agentincome.get("refwecappletnum").add(partnerincome.get("refwecappletnum")));
		totalincome.put("refaliappletnum", agentincome.get("refaliappletnum").add(partnerincome.get("refaliappletnum")));
		
		totalincome.put("wechatmoney", agentincome.get("wechatmoney").add(partnerincome.get("wechatmoney")));
		totalincome.put("alipaymoney", agentincome.get("alipaymoney").add(partnerincome.get("alipaymoney")));
		totalincome.put("wecappletmoney", agentincome.get("wecappletmoney").add(partnerincome.get("wecappletmoney")));
		totalincome.put("aliappletmoney", agentincome.get("aliappletmoney").add(partnerincome.get("aliappletmoney")));
		
		totalincome.put("refwechatmoney", agentincome.get("refwechatmoney").add(partnerincome.get("refwechatmoney")));
		totalincome.put("refalipaymoney", agentincome.get("refalipaymoney").add(partnerincome.get("refalipaymoney")));
		totalincome.put("refwecappletmoney", agentincome.get("refwecappletmoney").add(partnerincome.get("refwecappletmoney")));
		totalincome.put("refaliappletmoney", agentincome.get("refaliappletmoney").add(partnerincome.get("refaliappletmoney")));
		
		totalincome.put("incoins", agentincome.get("incoins"));
		totalincome.put("incoinsmoney", agentincome.get("incoinsmoney"));
		
		totalincome.put("totalorder", agentincome.get("totalorder").add(partnerincome.get("totalorder")));
		totalincome.put("totalreforder", agentincome.get("totalreforder").add(partnerincome.get("totalreforder")));
		totalincome.put("totalmoney", agentincome.get("totalmoney").add(partnerincome.get("totalmoney")));
		totalincome.put("totalrefmoney", agentincome.get("totalrefmoney").add(partnerincome.get("totalrefmoney")));
		totalincome.put("incomemoney", totalincome.get("totalmoney").subtract(totalincome.get("totalrefmoney")));
		return totalincome;
	}
	
	/*** @origin 定时汇总计算  ===============================================================================  ***/
	
	/*** @origin 商户的定时汇总计算 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ***/
	
	/**
	 * @Description： 所有有效商户的定时汇总计算
	 * @author： origin @RZC 
	 */
	@Override
	public int agentearningscollect(String time) {
		try {
			List<Map<String, Object>> binduser = userEquipmentDao.distinctuid();//商户
			List<Map<String, Object>> bindpartner = userEquipmentDao.bindingcodepartnerinfo();
			int num = 0;
			String startTime = time+" 00:00:00";
			String endTime = time+" 23:59:59";
			for(Map<String, Object> item : binduser){
				String merid = item.get("user_id").toString();
				Map<String, BigDecimal> merincome = agentincomecollect( Integer.valueOf(merid), startTime, endTime);
				incomeCollectInset("000000", merid, "00", 2, merincome, time);
			}
			
			for(Map<String, Object> item : bindpartner){
				if(null !=item){
					Integer manid = StringUtil.togetBigDecimal(item.get("partnider")).intValue();//合伙人id
					Map<String, BigDecimal> parincome = partnerincomecollect( manid, startTime, endTime);
					parIncomeCollectInset(manid, time, parincome);
				}
			}
			return num;
		} catch (Exception e) {
			logger.info(CommUtil.getExceptInfo(e));
			return 0;
		}
		
	}
		
	public int agentincomecollect(String time) {
		List<Map<String, Object>> binduser = userEquipmentDao.distinctuid();//商户
		List<Map<String, Object>> bindpartner = userEquipmentDao.bindingcodepartnerinfo();
		int num = 0;
		for(Map<String, Object> item : binduser){
			String merid = item.get("user_id").toString();//商户id
			Parameters parameters = new Parameters();
			parameters.setStartTime(time+" 00:00:00");
			parameters.setEndTime(time+" 23:59:59");
			parameters.setDealer(merid);
			Map<String, Object> collectmoney = tradeRecordDao.merchantCollect(parameters);
			if(null==collectmoney) collectmoney = new HashMap<>();
			Map<String, Object> coinscollect = inCoinsDao.selectcoinsup(parameters);
			if(null==coinscollect) coinscollect = new HashMap<>();
			int incoins = 0;
			double incoinsmoney = 0.00;
			incoins = StringUtil.togetBigDecimal(coinscollect.get("incoins")).intValue();
			incoinsmoney = StringUtil.togetBigDecimal(coinscollect.get("incoinsmoney")).doubleValue();
			collectmoney.put("incoins", incoins);
			collectmoney.put("incoinsmoney", incoinsmoney);
			collectrecord("000000", merid, "00", 2, collectmoney, time);
		}
		
		for(Map<String, Object> item : bindpartner){
			if(null !=item){
				Integer manid = StringUtil.togetBigDecimal(item.get("manid")).intValue();//合伙人id
				Parameters parame = new Parameters();
				parame.setStartTime(time+" 00:00:00");
				parame.setEndTime(time+" 23:59:59");
				parame.setUid(manid);
				Map<String, Object> collectmoney = tradeRecordDao.partnerCollect(parame);
				if(null==collectmoney) collectmoney = new HashMap<>();
				partnerCalculate(manid, time, collectmoney);
			}
		}
		return num;
	}
	
	/** 
	 * 历史统计查询
	 */
	@Override
	public PageUtils<Parameters> selectInfo(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameters>  page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameter = new Parameters();
		//ordertotal ordermoney
		String ordertotal = request.getParameter("ordertotal");//总订单 1由大到小 2有小到大
		String ordermoney = request.getParameter("ordermoney");//总金额 1由大到小 2有小到大
		if(ordertotal==null) ordertotal = "0";
		if(ordermoney==null) ordermoney = "0";
		if(ordertotal.equals("0") && ordermoney.equals("0")){
			parameter.setParamete("count_time DESC");
		}else if(!ordertotal.equals("0") && ordermoney.equals("0")){ 
			if(ordertotal.equals("1")){
				parameter.setParamete("ordertotal DESC");
			}else if(ordertotal.equals("2")){
				parameter.setParamete("ordertotal ASC");
			}
		}else if(ordertotal.equals("0") && !ordermoney.equals("0")){ 
			if(ordermoney.equals("1")){
				parameter.setParamete("moneytotal DESC");
			}else if(ordermoney.equals("2")){
				parameter.setParamete("moneytotal ASC");
			}
		}else if(!ordertotal.equals("0") && !ordermoney.equals("0")){ 
			if(ordertotal.equals("1") && ordermoney.equals("1")){
				parameter.setParamete("ordertotal DESC,moneytotal DESC");
			}else if(ordertotal.equals("1") && ordermoney.equals("2")){
				parameter.setParamete("ordertotal DESC,moneytotal ASC");
			}else if(ordertotal.equals("2") && ordermoney.equals("1")){
				parameter.setParamete("ordertotal ASC,moneytotal DESC");
			}else if(ordertotal.equals("2") && ordermoney.equals("2")){
				parameter.setParamete("ordertotal ASC,moneytotal ASC");
			}
		}
		
		parameter.setStartTime(request.getParameter("begintime"));
		parameter.setEndTime(request.getParameter("endtime"));
		
		List<Map<String, Object>> statis = statisticsDao.selectInfo(parameter);
		Map<String, Object> statotal = statisticsDao.selectInfototal(parameter);
		page.setMap(statotal);
		page.setTotalRows(statis.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameter.setPages(page.getNumPerPage());
		parameter.setStartnumber(page.getStartIndex());
		page.setListMap(statisticsDao.selectInfo(parameter));
		return page;
		
	}

	@Override
	public int insertStatis(Statistics statistics) {
		return  statisticsDao.insertStatis(statistics);
	}

	@Override
	public int insertStatisMap(Map<String, Object> params) {
		return  statisticsDao.insertStatismap(params);
	}

	@Override
	public Map<String, Object> selectcollectincometotal() {
		Map<String, Object> collect = new HashMap<>();
		Parameters parameter = new Parameters();
		Map<String, Object> chargermap = chargeRecordDao.selectcollect(parameter);
		Map<String, Object> incoinstmap = inCoinsDao.selectsuminfo(parameter);
		Map<String, Object> offlinemap = offlineCardDao.selecttotaloff(parameter);
		Map<String, Object> moneymap = moneyDao.selectmoneytotal(parameter);
		Map<String, Object> onlinemap = onlineCardRecordDao.onlinecardcollect(parameter);
		if(chargermap==null) chargermap = new HashMap<>();
		if(incoinstmap==null) incoinstmap = new HashMap<>();
		if(offlinemap==null) offlinemap = new HashMap<>();
		if(moneymap==null) moneymap = new HashMap<>();
		if(onlinemap==null) onlinemap = new HashMap<>();
		Map<String, Object> totalinfo = totalincome(chargermap, incoinstmap, offlinemap, moneymap);
		String newtime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
		String endtime = StringUtil.toDateTime("yyyy-MM-dd 23:59:59");
		parameter.setStartTime(newtime);
		parameter.setEndTime(endtime);
		Map<String, Object> chargerNowmap = chargeRecordDao.selectcollect(parameter);
		Map<String, Object> incoinstNowmap = inCoinsDao.selectsuminfo(parameter);
		Map<String, Object> offlineNowmap = offlineCardDao.selecttotaloff(parameter);
		Map<String, Object> moneyNowmap = moneyDao.selectmoneytotal(parameter);
		Map<String, Object> onlineNowmap = onlineCardRecordDao.onlinecardcollect(parameter);
		if(chargerNowmap==null) chargerNowmap = new HashMap<>();
		if(incoinstNowmap==null) incoinstNowmap = new HashMap<>();
		if(offlineNowmap==null) offlineNowmap = new HashMap<>();
		if(moneyNowmap==null) moneyNowmap = new HashMap<>();
		if(onlineNowmap==null) onlineNowmap = new HashMap<>();
		Map<String, Object> nowtotalinfo = totalincome(chargerNowmap, incoinstNowmap, offlineNowmap, moneyNowmap);
		collect.put("totalinfo", totalinfo);
		collect.put("nowtotalinfo", nowtotalinfo);
		return collect;
	}
	
	@Override
	public Map<String, Object> selectcollectrefundtotal() {
		Map<String, Object> collect = new HashMap<>();
		Parameters parameter = new Parameters();
		Map<String, Object> chargermap = chargeRecordDao.selectcollectrefund(parameter);
		Map<String, Object> incoinstmap = inCoinsDao.selectsuminforefund(parameter);
		Map<String, Object> offlinemap = offlineCardDao.selecttotaloffrefund(parameter);
		Map<String, Object> moneymap = moneyDao.selectmoneytotalrefund(parameter);
		if(chargermap==null) chargermap = new HashMap<>();
		if(incoinstmap==null) incoinstmap = new HashMap<>();
		if(offlinemap==null) offlinemap = new HashMap<>();
		if(moneymap==null) moneymap = new HashMap<>();
		Map<String, Object> totalinfo = totalrefund(chargermap, incoinstmap, offlinemap, moneymap);
		String newtime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
		String endtime = StringUtil.toDateTime("yyyy-MM-dd 23:59:59");
		parameter.setStartTime(newtime);
		parameter.setEndTime(endtime);
		Map<String, Object> chargerNowmap = chargeRecordDao.selectcollectrefund(parameter);
		Map<String, Object> incoinstNowmap = inCoinsDao.selectsuminforefund(parameter);
		Map<String, Object> offlineNowmap = offlineCardDao.selecttotaloffrefund(parameter);
		Map<String, Object> moneyNowmap = moneyDao.selectmoneytotalrefund(parameter);
		if(chargerNowmap==null) chargerNowmap = new HashMap<>();
		if(incoinstNowmap==null) incoinstNowmap = new HashMap<>();
		if(offlineNowmap==null) offlineNowmap = new HashMap<>();
		if(moneyNowmap==null) moneyNowmap = new HashMap<>();
		Map<String, Object> nowtotalinfo = totalrefund(chargerNowmap, incoinstNowmap, offlineNowmap, moneyNowmap);
		collect.put("totalinfo", totalinfo);
		collect.put("nowtotalinfo", nowtotalinfo);
		return collect;
	}
	
	@Override
	public Map<String, Object> selectYestecollect() {
		Map<String, Object> totalorder = new HashMap<>();
		String endtime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
		String starttime = StringUtil.getnumterday("yyyy-MM-dd 00:00:00", endtime, 1);
		Parameters parameter = new Parameters();
		parameter.setStartTime(starttime);
		parameter.setEndTime(endtime);
		Map<String, Object> chargermap = chargeRecordDao.selectcollect(parameter);
		Map<String, Object> incoinstmap = inCoinsDao.selectsuminfo(parameter);
		Map<String, Object> offlinemap = offlineCardDao.selecttotaloff(parameter);
		Map<String, Object> moneymap = moneyDao.selectmoneytotal(parameter);
		if(chargermap==null) chargermap = new HashMap<>();
		if(incoinstmap==null) incoinstmap = new HashMap<>();
		if(offlinemap==null) offlinemap = new HashMap<>();
		if(moneymap==null) moneymap = new HashMap<>();
		Map<String, Object> totalincome = totalincome(chargermap, incoinstmap, offlinemap, moneymap);

		Map<String, Object> refchargermap = chargeRecordDao.selectcollectrefund(parameter);
		Map<String, Object> refincoinstmap = inCoinsDao.selectsuminforefund(parameter);
		Map<String, Object> refofflinemap = offlineCardDao.selecttotaloffrefund(parameter);
		Map<String, Object> refmoneymap = moneyDao.selectmoneytotalrefund(parameter);
		if(refchargermap==null) refchargermap = new HashMap<>();
		if(refincoinstmap==null) refincoinstmap = new HashMap<>();
		if(refofflinemap==null) refofflinemap = new HashMap<>();
		if(refmoneymap==null) refmoneymap = new HashMap<>();
		Map<String, Object> totalrefund = totalrefund(refchargermap, refincoinstmap, refofflinemap, refmoneymap);
		totalorder.putAll(totalincome);
		totalorder.putAll(totalrefund);
		return totalorder;
	}
	
	//充值收入总订单
	private Map<String, Object> totalincome(Map<String, Object> chargermap, Map<String, Object> incoinstmap, Map<String, Object> offlinemap,
			Map<String, Object> moneymap) {
		
		//充电 charger
		BigDecimal charwalletnum = StringUtil.togetBigDecimal(chargermap.get("walletnum"));
		BigDecimal charwechatnum = StringUtil.togetBigDecimal(chargermap.get("wechatnum"));
		BigDecimal charalipaynum = StringUtil.togetBigDecimal(chargermap.get("alipaynum"));
		BigDecimal charwalletmoney = StringUtil.togetBigDecimal(chargermap.get("walletmoney"));
		BigDecimal charwechatmoney = StringUtil.togetBigDecimal(chargermap.get("wechatmoney"));
		BigDecimal charalipaymoney = StringUtil.togetBigDecimal(chargermap.get("alipaymoney"));
		//投币  incoins
		BigDecimal incowalletnum = StringUtil.togetBigDecimal(chargermap.get("walletnum"));
		BigDecimal incowechatnum = StringUtil.togetBigDecimal(incoinstmap.get("wechatnum"));
		BigDecimal incoalipaynum = StringUtil.togetBigDecimal(incoinstmap.get("alipaynum"));
		BigDecimal incowalletmoney = StringUtil.togetBigDecimal(chargermap.get("walletmoney"));
		BigDecimal incowechatmoney = StringUtil.togetBigDecimal(incoinstmap.get("wechatmoney"));
		BigDecimal incoalipaymoney = StringUtil.togetBigDecimal(incoinstmap.get("alipaymoney"));
		BigDecimal incoins = StringUtil.togetBigDecimal(incoinstmap.get("incoins"));
		BigDecimal incoinsmoney = StringUtil.togetBigDecimal(incoinstmap.get("incoinsmoney"));
		//离线 offline
		BigDecimal offlwalletnum = StringUtil.togetBigDecimal(chargermap.get("walletnum")); 
		BigDecimal offlwechatnum = StringUtil.togetBigDecimal(offlinemap.get("wechatnum"));
		BigDecimal offlalipaynum = StringUtil.togetBigDecimal(offlinemap.get("alipaynum"));
		BigDecimal offlwalletmoney = StringUtil.togetBigDecimal(chargermap.get("walletmoney"));
		BigDecimal offlwechatmoney = StringUtil.togetBigDecimal(offlinemap.get("wechatmoney"));
		BigDecimal offlalipaymoney = StringUtil.togetBigDecimal(offlinemap.get("alipaymoney"));
		//钱包充值 wallet
		BigDecimal wallwechatnum = StringUtil.togetBigDecimal(moneymap.get("walletnum"));
		//BigDecimal wallonlinenum = StringUtil.togetBigDecimal(moneymap.get("onlinenum"));
		BigDecimal wallwechatmoney = StringUtil.togetBigDecimal(moneymap.get("walletmoney"));
		//BigDecimal wallonlinemoney = StringUtil.togetBigDecimal(moneymap.get("onlinemoney"));
		
		//微信充值总订单与总金额
		BigDecimal wechatnumber = charwechatnum.add(incowechatnum).add(offlwechatnum).add(wallwechatnum);
		BigDecimal wechatmoneye = (charwechatmoney.add(incowechatmoney).add(offlwechatmoney).add(wallwechatmoney)).setScale(2, BigDecimal.ROUND_HALF_UP);
		//支付宝总订单与总金额
		BigDecimal alipaynumber = charalipaynum.add(incoalipaynum).add(offlalipaynum);
		BigDecimal alipaymoneye = (charalipaymoney.add(incoalipaymoney).add(offlalipaymoney)).setScale(2, BigDecimal.ROUND_HALF_UP);
		//钱包总订单与总金额
		BigDecimal walletnumber = charwalletnum.add(incowalletnum).add(offlwalletnum);
		BigDecimal walletmoneye = (charwalletmoney.add(incowalletmoney).add(offlwalletmoney)).setScale(2, BigDecimal.ROUND_HALF_UP);
		//总订单
		BigDecimal ordernumber = wechatnumber.add(alipaynumber);
		//消费总额
		BigDecimal ordermoney = (wechatmoneye.add(alipaymoneye)).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		Map<String, Object> totalinfo = new HashMap<>();
		totalinfo.put("walletnumber", walletnumber);
		totalinfo.put("walletmoneye", walletmoneye);
		totalinfo.put("wechatnumber", wechatnumber);
		totalinfo.put("wechatmoneye", wechatmoneye);
		totalinfo.put("alipaynumber", alipaynumber);
		totalinfo.put("alipaymoneye", alipaymoneye);
		totalinfo.put("incoins", incoins);
		totalinfo.put("incoinsmoney", incoinsmoney);
		totalinfo.put("ordernumber", ordernumber);
		totalinfo.put("ordermoney", ordermoney);
		return totalinfo;
	}
	
	private Map<String, Object> totalrefund(Map<String, Object> chargermap, Map<String, Object> incoinstmap, Map<String, Object> offlinemap,
			Map<String, Object> moneymap) {
		//充电 charger
		BigDecimal charwalletnum = StringUtil.togetBigDecimal(chargermap.get("refwalletnum"));
		BigDecimal charwechatnum = StringUtil.togetBigDecimal(chargermap.get("refwechatnum"));
		BigDecimal charalipaynum = StringUtil.togetBigDecimal(chargermap.get("refalipaynum"));
		BigDecimal charwalletmoney = StringUtil.togetBigDecimal(chargermap.get("refwalletmoney"));
		BigDecimal charwechatmoney = StringUtil.togetBigDecimal(chargermap.get("refwechatmoney"));
		BigDecimal charalipaymoney = StringUtil.togetBigDecimal(chargermap.get("refalipaymoney"));
		
		//投币  incoins
		BigDecimal incowalletnum = StringUtil.togetBigDecimal(incoinstmap.get("refwalletnum"));
		BigDecimal incowechatnum = StringUtil.togetBigDecimal(incoinstmap.get("refwechatnum"));
		BigDecimal incoalipaynum = StringUtil.togetBigDecimal(incoinstmap.get("refalipaynum"));
		BigDecimal incowalletmoney = StringUtil.togetBigDecimal(incoinstmap.get("refwalletmoney"));
		BigDecimal incowechatmoney = StringUtil.togetBigDecimal(incoinstmap.get("refwechatmoney"));
		BigDecimal incoalipaymoney = StringUtil.togetBigDecimal(incoinstmap.get("refalipaymoney"));
		//离线 offline
		BigDecimal offlwalletnum = StringUtil.togetBigDecimal(offlinemap.get("refwalletnum")); 
		BigDecimal offlwechatnum = StringUtil.togetBigDecimal(offlinemap.get("refwechatnum"));
		BigDecimal offlalipaynum = StringUtil.togetBigDecimal(offlinemap.get("refalipaynum"));
		BigDecimal offlwalletmoney = StringUtil.togetBigDecimal(chargermap.get("refwalletmoney"));
		BigDecimal offlwechatmoney = StringUtil.togetBigDecimal(offlinemap.get("refwechatmoney"));
		BigDecimal offlalipaymoney = StringUtil.togetBigDecimal(offlinemap.get("refalipaymoney"));
		//退款到钱包 wallet
		BigDecimal wallonlinenum = StringUtil.togetBigDecimal(moneymap.get("onlinerefnum"));
		BigDecimal wallchargenum = StringUtil.togetBigDecimal(moneymap.get("chargenum"));
		BigDecimal wallofflinenum = StringUtil.togetBigDecimal(moneymap.get("offlinenum"));
		BigDecimal wallincoinsnum = StringUtil.togetBigDecimal(moneymap.get("incoinsnum"));
		BigDecimal wallonlinemoney = StringUtil.togetBigDecimal(moneymap.get("onlinerefmoney"));
		BigDecimal wallchamoney = StringUtil.togetBigDecimal(moneymap.get("chargewalmoney"));
		BigDecimal walloffmoney = StringUtil.togetBigDecimal(moneymap.get("offlinewalmoney"));
		BigDecimal wallincomoney = StringUtil.togetBigDecimal(moneymap.get("incoinswalmoney"));
		
		//钱包退款订单、金额（退款）
		BigDecimal walletrefnum = charwalletnum.add(incowalletnum).add(offlwalletnum);
		BigDecimal walletrefmoneye = (charwalletmoney.add(incowalletmoney).add(offlwalletmoney)).setScale(2, BigDecimal.ROUND_HALF_UP);
		//微信退款总订单与总金额（全额退款：退款到微信）
		BigDecimal wechatnumber = charwechatnum.add(incowechatnum).add(offlwechatnum);
		BigDecimal wechatmoneye = (charwechatmoney.add(incowechatmoney).add(offlwechatmoney)).setScale(2, BigDecimal.ROUND_HALF_UP);
		//支付宝退款总订单与总金额（全额退款：退款到支付宝）
		BigDecimal alipaynumber = charalipaynum.add(incoalipaynum).add(offlalipaynum);
		BigDecimal alipaymoneye = (charalipaymoney.add(incoalipaymoney).add(offlalipaymoney)).setScale(2, BigDecimal.ROUND_HALF_UP);
		//退款总订单
		BigDecimal ordernumber = wechatnumber.add(alipaynumber);
		//退款总额
		BigDecimal ordermoney = (wechatmoneye.add(alipaymoneye)).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		BigDecimal refwalletnum = wallonlinenum.add(wallchargenum).add(wallofflinenum).add(wallincoinsnum);
		BigDecimal refwalletmoneye = (wallonlinemoney.add(wallchamoney).add(walloffmoney).add(wallincomoney)).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		Map<String, Object> totalinfo = new HashMap<>();
		totalinfo.put("refwechatnumber", wechatnumber);
		totalinfo.put("refwechatmoneye", wechatmoneye);
		totalinfo.put("refalipaynumber", alipaynumber);
		totalinfo.put("refalipaymoneye", alipaymoneye);
		totalinfo.put("refordernumber", ordernumber);
		totalinfo.put("refordermoney", ordermoney);
		totalinfo.put("refwalletnum", refwalletnum);
		totalinfo.put("refwalletmoneye", refwalletmoneye);
		totalinfo.put("walletrefnum", walletrefnum);
		totalinfo.put("walletrefmoneye", walletrefmoneye);
//		totalinfo.put("wallonlinenum", wallonlinenum);
//		totalinfo.put("wallchargenum", wallchargenum);
//		totalinfo.put("wallofflinenum", wallofflinenum);
//		totalinfo.put("wallincoinsnum", wallincoinsnum);
//		totalinfo.put("wallonlinemoney", wallonlinemoney);
//		totalinfo.put("wallchamoney", wallchamoney);
//		totalinfo.put("walloffmoney", walloffmoney);
//		totalinfo.put("wallincomoney", wallincomoney);
		return totalinfo;
	}
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	/**
	 * @Description：交易记录定时汇总金额
	 * @author： origin 
	 */
	@Override
	public Map<String, Object> timingCollectMoney(Parameters parameters) {
		Map<String, Object> collectmoney = tradeRecordDao.timingCollectMoney(parameters);
		return collectmoney != null ? collectmoney : new HashMap<String, Object>();
	}
	
	/**
	 * @Description： 根据条件汇总脉冲设备线下投币上传信息
	 * @author： origin 
	 */
	@Override
	public Map<String, Object> selectcoinsup(Parameters parameters) {
		Map<String, Object> coinscollect = inCoinsDao.selectcoinsup(parameters);
		return coinscollect != null ? coinscollect : new HashMap<String, Object>();
	}

	//历史订单信息汇总
	@Override
	public Map<String, Object> collectinfo() {
		Map<String, Object> collectmoney = statisticsDao.collectinfo();
		return collectmoney != null ? collectmoney : new HashMap<String, Object>();
	}
	

	//设备历史每日收益汇总信息查询
	@Override
	public PageUtils<Parameters> codeEarningsCollect(Integer souce) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		User user = CommonConfig.getAdminReq(request);
		Integer userrank = user.getRank();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(userrank!=0) parameters.setUid(user.getId());//获取用户
		parameters.setType(souce.toString());
		parameters.setCode(request.getParameter("code"));//设备号
		parameters.setNickname(request.getParameter("nickname"));
		parameters.setUsername(request.getParameter("username"));
		parameters.setPhone(request.getParameter("phone"));
		parameters.setSource(request.getParameter("areaname"));//小区名
		parameters.setRemark(request.getParameter("remark"));//小区地址
		String sort = request.getParameter("sort");
		String pararank = request.getParameter("rank");
		if(sort==null) sort = "0";
		if(pararank==null) pararank = "0";
		if(sort.equals("0") && pararank.equals("0")){
			parameters.setParamete("c.count_time DESC");
		}else if(!sort.equals("0") && !pararank.equals("0")){
			parameters.setRank(pararank);
		}else{
			parameters.setRank(pararank);
			parameters.setSort(sort);
		}
		parameters.setStartTime(request.getParameter("startTime"));
		parameters.setEndTime(request.getParameter("endTime"));
		List<Map<String, Object>>  codestatismap = codestatisticsDao.getcodestatistics(parameters);
		Map<String, Object>  codestatis = calculatedata(codestatismap);
		page.setMap(codestatis);
		page.setTotalRows(codestatismap.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		List<Map<String, Object>> chargemapli = codestatisticsDao.getcodestatistics(parameters);
		page.setListMap(chargemapli);
		return page;
	}
	
	//设备当天每日收益汇总信息查询
	@Override
	public PageUtils<Parameters> nowcodeearnings() {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		String startTime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
		//String endTime = StringUtil.toDateTime();
		String endTime = StringUtil.toDateTime("yyyy-MM-dd 23:59:59");
		
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		User user = CommonConfig.getAdminReq(request);
		Integer userrank = user.getRank();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理  
		if(userrank!=0) parameters.setUid(user.getId());//获取用户
		parameters.setCode(request.getParameter("code"));//设备号
		parameters.setStatus("1");//设备被绑定
		List<Map<String, Object>> equlist = equipmentDao.selectEquList(parameters);
		List<Map<String, Object>>  nowcodestatis = new ArrayList<Map<String, Object>>();
		if(equlist!=null){
			Parameters parame = new Parameters(); 
			for(Map<String, Object> item : equlist){
				String code = (String) item.get("code");
				String merid = StringUtil.togetBigDecimal(item.get("merid")).toString();
//				String areaid = StringUtil.togetBigDecimal(item.get("aid")).toString();
				String name = (String)item.get("name");
				String username = (String)item.get("username");
				parame.setDealer(merid);
				parame.setCode(code);
				parame.setStartTime(startTime);
				parame.setEndTime(endTime);
				Map<String, Object> collectmoney = tradeRecordDao.timingCollectMoney(parame);
				if(collectmoney!=null){
					collectmoney.put("counttime", StringUtil.toDateTime("yyyy-MM-dd"));
					collectmoney.put("code", code);
					collectmoney.put("name", name);
					collectmoney.put("username", username);
					
					int wechatnum = StringUtil.togetBigDecimal(collectmoney.get("wechatnum")).intValue();
					int alipaynum = StringUtil.togetBigDecimal(collectmoney.get("alipaynum")).intValue();
					double wechatmoney = (double) collectmoney.get("wechatmoney");
					double alipaymoney = (double) collectmoney.get("alipaymoney");
					double refwechatmoney = (double) collectmoney.get("refwechatmoney");
					double refalipaymoney = (double) collectmoney.get("refalipaymoney");
					int ordertotal = wechatnum + alipaynum;
					double moneytotal = wechatmoney + alipaymoney-refwechatmoney-refalipaymoney;

					collectmoney.put("ordertotal", ordertotal);
					collectmoney.put("moneytotal", moneytotal);
					nowcodestatis.add(collectmoney);
				}
			}
		}
		Map<String, Object>  codestatis = calculatedata(nowcodestatis);
		page.setMap(codestatis);
		page.setTotalRows(nowcodestatis.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		int tatolpage = nowcodestatis.size();
		int perpage = page.getNumPerPage();//条数 10
		int beginpage = currentPage*perpage-perpage;
		int endpage = currentPage*perpage;
		if(currentPage==0 || beginpage>tatolpage){
			beginpage = 0;
			if(tatolpage>perpage) endpage = perpage;
			if(tatolpage<perpage) endpage = tatolpage;
		}else{
			if(tatolpage<=endpage) endpage = tatolpage;
		}
		page.setListMap(nowcodestatis.subList(beginpage, endpage));
		return page;
	}
	
	//设备（历史）汇总金额查询是计算总额
	public Map<String, Object> calculatedata(List<Map<String, Object>> codestatismap) {
		Map<String, Object>  codestatis = new HashMap<String, Object>();;
		BigDecimal moneytotal = new BigDecimal(0);
		BigDecimal ordertotal = new BigDecimal(0);
		BigDecimal wechatmoney = new BigDecimal(0);
		BigDecimal wechatorder = new BigDecimal(0);
		BigDecimal alipaymoney = new BigDecimal(0);
		BigDecimal alipayorder = new BigDecimal(0);
		BigDecimal wechatretmoney = new BigDecimal(0);
		BigDecimal wechatretord = new BigDecimal(0);
		BigDecimal alipayretmoney = new BigDecimal(0);
		BigDecimal alipayretord = new BigDecimal(0);
		BigDecimal incoinsmoney = new BigDecimal(0);
		BigDecimal incoinsorder = new BigDecimal(0);
		for(Map<String, Object> item : codestatismap){
			moneytotal = moneytotal.add(StringUtil.togetBigDecimal(item.get("moneytotal")));
			ordertotal = ordertotal.add(StringUtil.togetBigDecimal(item.get("ordertotal")));
			wechatmoney = wechatmoney.add(StringUtil.togetBigDecimal(item.get("wechatmoney")));
			wechatorder = wechatorder.add(StringUtil.togetBigDecimal(item.get("wechatorder")));
			alipaymoney = alipaymoney.add(StringUtil.togetBigDecimal(item.get("alipaymoney")));
			alipayorder = alipayorder.add(StringUtil.togetBigDecimal(item.get("alipayorder")));
			wechatretmoney = wechatretmoney.add(StringUtil.togetBigDecimal(item.get("wechatretmoney")));
			wechatretord = wechatretord.add(StringUtil.togetBigDecimal(item.get("wechatretord")));
			alipayretmoney = alipayretmoney.add(StringUtil.togetBigDecimal(item.get("alipayretmoney")));
			alipayretord = alipayretord.add(StringUtil.togetBigDecimal(item.get("alipayretord")));
			incoinsmoney = incoinsmoney.add(StringUtil.togetBigDecimal(item.get("incoinsmoney")));
			incoinsorder = incoinsorder.add(StringUtil.togetBigDecimal(item.get("incoinsorder")));
		}
		codestatis.put("moneytotal", moneytotal);
		codestatis.put("ordertotal", ordertotal);
		codestatis.put("wechatmoney", wechatmoney);
		codestatis.put("wechatorder", wechatorder);
		codestatis.put("alipaymoney", alipaymoney);
		codestatis.put("alipayorder", alipayorder);
		codestatis.put("wechatretmoney", wechatretmoney);
		codestatis.put("wechatretord", wechatretord);
		codestatis.put("alipayretmoney", alipayretmoney);
		codestatis.put("alipayretord", alipayretord);
		codestatis.put("incoinsmoney", incoinsmoney);
		codestatis.put("incoinsorder", incoinsorder);
		return codestatis;
	}
	
	@Override
	public Map<String, Object> agentmoneycollect() {
		try {
			User user = CommonConfig.getAdminReq(request);
			Parameters parame = new Parameters();
			parame.setType("2");
			parame.setUid(user.getId());
			Map<String, Object> agentdata = codestatisticsDao.agentmoneycollect(parame);
			if(agentdata==null) agentdata = new HashMap<>();
			return agentdata;
		} catch (Exception e) {
			return new HashMap<String, Object>();
		}
	}

	//商户当天金额汇总
	@Override
	public Map<String, Object> agentnowmoneycollect() {
		try {
			String startTime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
			String endTime = StringUtil.toDateTime("yyyy-MM-dd 23:59:59");
			Parameters parameters = new Parameters();
			User user = CommonConfig.getAdminReq(request);
			parameters.setDealer(user.getId().toString());
			parameters.setStartTime(startTime);
			parameters.setEndTime(endTime);
			
			Map<String, Object> collectmoney = CommUtil.isMapEmpty(tradeRecordDao.timingCollectMoney(parameters));
			Integer wechatnum = CommUtil.toInteger(collectmoney.get("wechatnum"));
			Integer alipaynum = CommUtil.toInteger(collectmoney.get("alipaynum"));
			Integer wecappletnum = CommUtil.toInteger(collectmoney.get("wecappletnum"));
			Integer aliappletnum = CommUtil.toInteger(collectmoney.get("aliappletnum"));
			Double wechatmoney = CommUtil.toDouble(collectmoney.get("wechatmoney"));
			Double alipaymoney = CommUtil.toDouble(collectmoney.get("alipaymoney"));
			Double wecappletmoney = CommUtil.toDouble(collectmoney.get("wecappletmoney"));
			Double aliappletmoney = CommUtil.toDouble(collectmoney.get("aliappletmoney"));
			
			Integer refwechatnum = CommUtil.toInteger(collectmoney.get("refwechatnum"));
			Integer refalipaynum = CommUtil.toInteger(collectmoney.get("refalipaynum"));
			Integer refwecappletnum = CommUtil.toInteger(collectmoney.get("refwecappletnum"));
			Integer refaliappletnum = CommUtil.toInteger(collectmoney.get("refaliappletnum"));
			Double refwechatmoney = CommUtil.toDouble(collectmoney.get("refwechatmoney"));
			Double refalipaymoney = CommUtil.toDouble(collectmoney.get("refalipaymoney"));
			Double refwecappletmoney = CommUtil.toDouble(collectmoney.get("refwecappletmoney"));
			Double refaliappletmoney = CommUtil.toDouble(collectmoney.get("refaliappletmoney"));
			
			collectmoney.put("wechatnum", wechatnum);
			collectmoney.put("alipaynum", alipaynum);
			collectmoney.put("wecappletnum", wecappletnum);
			collectmoney.put("aliappletnum", aliappletnum);
			collectmoney.put("wechatmoney", wechatmoney);
			collectmoney.put("alipaymoney", alipaymoney);
			collectmoney.put("wecappletmoney", wecappletmoney);
			collectmoney.put("aliappletmoney", aliappletmoney);
			
			collectmoney.put("refwechatnum", refwechatnum);
			collectmoney.put("refalipaynum", refalipaynum);
			collectmoney.put("refwecappletnum", refwecappletnum);
			collectmoney.put("refaliappletnum", refaliappletnum);
			collectmoney.put("refwechatmoney", refwechatmoney);
			collectmoney.put("refalipaymoney", refalipaymoney);
			collectmoney.put("refwecappletmoney", refwecappletmoney);
			collectmoney.put("refaliappletmoney", refaliappletmoney);
			Integer ordertotal = CommUtil.toInteger(wechatnum + alipaynum);
			Double moneytotal = CommUtil.toDouble(wechatmoney + alipaymoney-refwechatmoney-refalipaymoney);
			collectmoney.put("ordertotal", ordertotal);
			collectmoney.put("moneytotal", moneytotal);
			return collectmoney;
		} catch (Exception e) {
			return new HashMap<String, Object>();
		}
		
	}

/*** ***  汇总记录操作       *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/	
//==============================================================================================================	
//==============================================================================================================
	/**
	 * @Description： 商户每天金额汇总插入
	 * @author： origin   
	 */
	@Override
	public int agentTimingmoneycollect(String startTime, String endTime) {
		Parameters parame = new Parameters();
		List<Map<String, Object>> binduser = userEquipmentDao.distinctuid();
		//List<Map<String, Object>> userinfo = userDao.selectDealerUserInfo(parame);
		int num = 0;
		for(Map<String, Object> item : binduser){
			//String merid = item.get("id").toString();
			String merid = item.get("user_id").toString();
			parame.setUid(Integer.parseInt(merid));
			parame.setType("1");//选择设备汇总
			parame.setStartTime(startTime);
			parame.setEndTime(endTime);
			Map<String, Object> agentcollect = codestatisticsDao.agentmoneycollect(parame);
			try {
				collectmoney("000000", merid, "00", 2, agentcollect, startTime);
				 num =1;
			} catch (Exception e) {
				 num =2;
				e.printStackTrace();
			}
		}
		return num;
	}
//==============================================================================================================
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
//==============================================================================================================	
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * @Description： 根据脉冲记录计算设备汇总
	 * @author： origin  
	 */
	public int coinscodecollect(String startTime, String endTime) {
		Parameters parameters = new Parameters();
		parameters.setType("3");
		parameters.setStartTime(startTime);
		parameters.setEndTime(endTime);
		List<Map<String, Object>> discoins = inCoinsDao.distinctcoins(parameters);//查询相互不同的设备
		for(Map<String, Object> item : discoins){
			if(null !=item){
				String coinscode = StringUtil.togetBigDecimal(item.get("equipmentnum")).toString();//设备号
				Integer merid = StringUtil.togetBigDecimal(item.get("merchantid")).intValue();//商户id
				parameters.setType(null);
				parameters.setCode(coinscode);
				Map<String, Object> coinscollect = inCoinsDao.selectcoinsup(parameters);
				Equipment equip = equipmentDao.getEquipmentAndAreaById(coinscode);
				indetcoinsinfo(coinscode, merid, equip.getAid(), 1, coinscollect, startTime.substring(0, 10));
			}
		}
		return 0;
	}
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//==============================================================================================================
	
	/**
	 * @Description： 脉冲判断插入设备汇总信息（1判断设备是否存在、存在更新，不存在插入） 
	 * @author： origin   
	 */
	private void indetcoinsinfo(String coinscode, Integer merid, Integer aid, Integer type, Map<String, Object> coinscollect,
			String date) {
		try {
			Codestatistics codestat = new Codestatistics();
			codestat.setCode(coinscode);
//			codestat.setCountTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
			codestat.setCountTime(CommUtil.DateTime(date, "yyyy-MM-dd"));
			Codestatistics statistic = codestatisticsDao.selectStatistics(codestat);
			if(null==statistic){
				collectrecord( coinscode, merid.toString(), aid.toString(), type, coinscollect, date);
			}else{
				if(null==coinscollect) coinscollect = new HashMap<String, Object>();
				int incoins = StringUtil.togetBigDecimal(coinscollect.get("incoins")).intValue();
				double incoinsmoney = StringUtil.togetBigDecimal(coinscollect.get("incoinsmoney")).doubleValue();
				statistic.setIncoinsorder(incoins);
				statistic.setIncoinsmoney(incoinsmoney);
				codestatisticsDao.updatecodestatistics(statistic);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//==============================================================================================================
		
	/**
	 * @Description： 合伙人数据判断
	 * @author： origin     
	 */
	private void partnerCalculate(Integer manid, String time, Map<String, Object> collectmoney) {
		Codestatistics codestatistics = new Codestatistics();
		codestatistics.setMerid(manid);
		try {
			Parameters parame = new Parameters();
			parame.setStartTime(time+" 00:00:00");
			parame.setEndTime(time+" 23:59:59");
			parame.setUid(manid);
			Map<String, Object> coinscollect = inCoinsDao.selectcoinsup(parame);
			if(null==coinscollect) coinscollect = new HashMap<>();
			int incoins = 0;
			double incoinsmoney = 0.00;
			incoins = StringUtil.togetBigDecimal(coinscollect.get("incoins")).intValue();
			incoinsmoney = StringUtil.togetBigDecimal(coinscollect.get("incoinsmoney")).doubleValue();
			collectmoney.put("incoins", incoins);
			collectmoney.put("incoinsmoney", incoinsmoney);
			codestatistics.setType(2);
			codestatistics.setCountTime(CommUtil.DateTime(time, "yyyy-MM-dd"));
			Codestatistics statistic = codestatisticsDao.selectStatistics(codestatistics);
			if(null==statistic){
				collectrecord("000000", manid.toString(), "01", 2, collectmoney, time);
			}else{

				System.out.println("合伙人数据判断 01 **************2101       ");
				int wechatnum = StringUtil.togetBigDecimal(collectmoney.get("wechatnum")).intValue();
				int alipaynum = StringUtil.togetBigDecimal(collectmoney.get("alipaynum")).intValue();
				int refwechatnum = StringUtil.togetBigDecimal(collectmoney.get("refwechatnum")).intValue();
				int refalipaynum = StringUtil.togetBigDecimal(collectmoney.get("refalipaynum")).intValue();
				double wechatmoney = StringUtil.togetBigDecimal(collectmoney.get("wechatmoney")).doubleValue();
				double alipaymoney = StringUtil.togetBigDecimal(collectmoney.get("alipaymoney")).doubleValue();
				double refwechatmoney = StringUtil.togetBigDecimal(collectmoney.get("refwechatmoney")).doubleValue();
				double refalipaymoney = StringUtil.togetBigDecimal(collectmoney.get("refalipaymoney")).doubleValue();
				int ordertotal = wechatnum + alipaynum;
				double moneytotal = wechatmoney + alipaymoney-refwechatmoney-refalipaymoney;
				System.out.println("合伙人数据判断======================001       ");
				statistic.setOrdertotal(ordertotal+statistic.getOrdertotal());
				statistic.setWechatorder(wechatnum+statistic.getWechatorder());
				statistic.setAlipayorder(alipaynum+statistic.getAlipayorder());
				statistic.setMoneytotal(moneytotal+statistic.getMoneytotal());
				statistic.setWechatmoney(wechatmoney+statistic.getWechatmoney());
				statistic.setAlipaymoney(alipaymoney+statistic.getAlipaymoney());
				statistic.setWechatretord(refwechatnum+statistic.getWechatretord());
				statistic.setAlipayretord(refalipaynum+statistic.getAlipayretord());
				statistic.setWechatretmoney(refwechatmoney+statistic.getWechatretmoney());
				statistic.setAlipayretmoney(refalipaymoney+statistic.getAlipayretmoney());

				System.out.println("合伙人数据判断======================002 statistic      "+statistic);
//				codestatistics.setIncoinsorder(incoins);
//				codestatistics.setIncoinsmoney(incoinsmoney);
				System.out.println("合伙人数据判断 **************2101       ");
				codestatisticsDao.updatecodestatistics(statistic);
				System.out.println("合伙人数据判断 **************04       ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description： 设备计算插入汇总记录信息
	 * @author： origin   
	 */
	private void collectrecord(String code, String merid, String areaid, Integer type, Map<String, Object> collectmoney,
			String startTime) {
		Codestatistics codestatistics = new Codestatistics();
		codestatistics.setCode(code);
		codestatistics.setMerid(StringUtil.getIntString(merid));
		codestatistics.setAreaid(StringUtil.getIntString(areaid));
		codestatistics.setType(type);
		Double onsumeQuantity = CommUtil.toDouble(collectmoney.get("consumeQuantity"));
		Double consumetime = CommUtil.toDouble(collectmoney.get("consumetime"));
		codestatistics.setConsumequantity(CommUtil.toInteger(onsumeQuantity));
		codestatistics.setConsumetime(CommUtil.toInteger(consumetime));
		codestatistics.setPaymentmoney(CommUtil.toDouble(collectmoney.get("paymentmoney")));
		//if(collectmoney==null) collectmoney = new HashMap<String, Object>();
		try {
			if(type==1){
				int wechatnum = StringUtil.togetBigDecimal(collectmoney.get("wechatnum")).intValue();
				int alipaynum = StringUtil.togetBigDecimal(collectmoney.get("alipaynum")).intValue();
				int refwechatnum = StringUtil.togetBigDecimal(collectmoney.get("refwechatnum")).intValue();
				int refalipaynum = StringUtil.togetBigDecimal(collectmoney.get("refalipaynum")).intValue();
				double wechatmoney = StringUtil.togetBigDecimal(collectmoney.get("wechatmoney")).doubleValue();
				double alipaymoney = StringUtil.togetBigDecimal(collectmoney.get("alipaymoney")).doubleValue();
				double refwechatmoney = StringUtil.togetBigDecimal(collectmoney.get("refwechatmoney")).doubleValue();
				double refalipaymoney = StringUtil.togetBigDecimal(collectmoney.get("refalipaymoney")).doubleValue();
				int ordertotal = wechatnum + alipaynum;
				double moneytotal = wechatmoney + alipaymoney-refwechatmoney-refalipaymoney;
				codestatistics.setOrdertotal(ordertotal);
				codestatistics.setWechatorder(wechatnum);
				codestatistics.setAlipayorder(alipaynum);
				codestatistics.setMoneytotal(moneytotal);
				codestatistics.setWechatmoney(wechatmoney);
				codestatistics.setAlipaymoney(alipaymoney);
				codestatistics.setWechatretord(refwechatnum);
				codestatistics.setAlipayretord(refalipaynum);
				codestatistics.setWechatretmoney(refwechatmoney);
				codestatistics.setAlipayretmoney(refalipaymoney);
			}else if(type==2){
				codestatistics.setOrdertotal(StringUtil.togetBigDecimal(collectmoney.get("ordertotal")).intValue());
				codestatistics.setWechatorder(StringUtil.togetBigDecimal(collectmoney.get("wechatorder")).intValue());
				codestatistics.setAlipayorder(StringUtil.togetBigDecimal(collectmoney.get("alipayorder")).intValue());
				codestatistics.setMoneytotal(StringUtil.togetBigDecimal(collectmoney.get("moneytotal")).doubleValue());
				codestatistics.setWechatmoney(StringUtil.togetBigDecimal(collectmoney.get("wechatmoney")).doubleValue());
				codestatistics.setAlipaymoney(StringUtil.togetBigDecimal(collectmoney.get("alipaymoney")).doubleValue());
				codestatistics.setWechatretord(StringUtil.togetBigDecimal(collectmoney.get("wechatretord")).intValue());
				codestatistics.setAlipayretord(StringUtil.togetBigDecimal(collectmoney.get("alipayretord")).intValue());
				codestatistics.setWechatretmoney(StringUtil.togetBigDecimal(collectmoney.get("wechatretmoney")).doubleValue());
				codestatistics.setAlipayretmoney(StringUtil.togetBigDecimal(collectmoney.get("alipayretmoney")).doubleValue());
			}
			codestatistics.setIncoinsorder(StringUtil.togetBigDecimal(collectmoney.get("incoins")).intValue());
			codestatistics.setIncoinsmoney(StringUtil.togetBigDecimal(collectmoney.get("incoinsmoney")).doubleValue());
			codestatistics.setCountTime(CommUtil.DateTime(startTime, "yyyy-MM-dd"));
//			codestatistics.setCountTime(new SimpleDateFormat("yyyy-MM-dd").parse(startTime));
			codestatisticsDao.insertCodestatis(codestatistics);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//汇总数据插入
	private void collectmoney(String code, String merid, String areaid, Integer type,Map<String, Object> collectmoney, String startTime){
		Codestatistics codestatistics = new Codestatistics();
		codestatistics.setCode(code);
		codestatistics.setMerid(StringUtil.getIntString(merid));
		codestatistics.setAreaid(StringUtil.getIntString(areaid));
		codestatistics.setType(type);
		codestatistics.setPaymentmoney(CommUtil.toDouble(collectmoney.get("paymentmoney")));
		if(type==1){
			int wechatnum = StringUtil.togetBigDecimal(collectmoney.get("wechatnum")).intValue();
			int alipaynum = StringUtil.togetBigDecimal(collectmoney.get("alipaynum")).intValue();
			int refwechatnum = StringUtil.togetBigDecimal(collectmoney.get("refwechatnum")).intValue();
			int refalipaynum = StringUtil.togetBigDecimal(collectmoney.get("refalipaynum")).intValue();
			double wechatmoney = StringUtil.togetBigDecimal(collectmoney.get("wechatmoney")).doubleValue();
			double alipaymoney = StringUtil.togetBigDecimal(collectmoney.get("alipaymoney")).doubleValue();
			double refwechatmoney = StringUtil.togetBigDecimal(collectmoney.get("refwechatmoney")).doubleValue();
			double refalipaymoney = StringUtil.togetBigDecimal(collectmoney.get("refalipaymoney")).doubleValue();
			int ordertotal = wechatnum + alipaynum;
			double moneytotal = wechatmoney + alipaymoney-refwechatmoney-refalipaymoney;
			codestatistics.setOrdertotal(ordertotal);
			codestatistics.setWechatorder(wechatnum);
			codestatistics.setAlipayorder(alipaynum);
			codestatistics.setMoneytotal(moneytotal);
			codestatistics.setWechatmoney(wechatmoney);
			codestatistics.setAlipaymoney(alipaymoney);
			codestatistics.setWechatretord(refwechatnum);
			codestatistics.setAlipayretord(refalipaynum);
			codestatistics.setWechatretmoney(refwechatmoney);
			codestatistics.setAlipayretmoney(refalipaymoney);
		}else if(type==2){
			codestatistics.setOrdertotal(StringUtil.togetBigDecimal(collectmoney.get("ordertotal")).intValue());
			codestatistics.setWechatorder(StringUtil.togetBigDecimal(collectmoney.get("wechatorder")).intValue());
			codestatistics.setAlipayorder(StringUtil.togetBigDecimal(collectmoney.get("alipayorder")).intValue());
			codestatistics.setMoneytotal(StringUtil.togetBigDecimal(collectmoney.get("moneytotal")).doubleValue());
			codestatistics.setWechatmoney(StringUtil.togetBigDecimal(collectmoney.get("wechatmoney")).doubleValue());
			codestatistics.setAlipaymoney(StringUtil.togetBigDecimal(collectmoney.get("alipaymoney")).doubleValue());
			codestatistics.setWechatretord(StringUtil.togetBigDecimal(collectmoney.get("wechatretord")).intValue());
			codestatistics.setAlipayretord(StringUtil.togetBigDecimal(collectmoney.get("alipayretord")).intValue());
			codestatistics.setWechatretmoney(StringUtil.togetBigDecimal(collectmoney.get("wechatretmoney")).doubleValue());
			codestatistics.setAlipayretmoney(StringUtil.togetBigDecimal(collectmoney.get("alipayretmoney")).doubleValue());
		}
		try {
			codestatistics.setIncoinsorder(StringUtil.togetBigDecimal(collectmoney.get("ordertotal")).intValue());
			codestatistics.setIncoinsmoney(StringUtil.togetBigDecimal(collectmoney.get("incoinsmoney")).doubleValue());
			codestatistics.setCountTime(CommUtil.DateTime(startTime, "yyyy-MM-dd"));
//			codestatistics.setCountTime(new SimpleDateFormat("yyyy-MM-dd").parse(startTime));
			codestatisticsDao.insertCodestatis(codestatistics);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
/**  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~***/
	
	/**
	 * @Description： 计算商户指定时间的汇总金额
	 * @author： origin
	 */
	@Override
	public Map<String, BigDecimal> agentIncomeNowTotal(Integer merid, String startTime, String endTime) {
		Map<String, BigDecimal> agentincome = agentincomecollect( merid, startTime, endTime);
		Map<String, BigDecimal> partnerincome = partnerincomecollect( merid, startTime, endTime);
		
		Map<String, BigDecimal> totalincome = agentincomeTotal( agentincome, partnerincome);
		return totalincome;
	}
	
	/**
	 * @Description： 根据uid和时间查询当天商户的收益信心并插入数据库
	 * @author： origin @RZC 
	 */
	public int objIncomeCollect(Integer uid, String time) {
		int num = 0;
		try {
			String startTime = time+" 00:00:00";
			String endTime = time+" 23:59:59";
			Map<String, BigDecimal> merincome = agentincomecollect( uid, startTime, endTime);
			incomeCollectInset("000000", uid.toString(), "00", 2, merincome, time);
			
			Map<String, BigDecimal> parincome = partnerincomecollect( uid, startTime, endTime);
			parIncomeCollectInset(uid, time, parincome);
			
		} catch (Exception e) {
			num = 1;
		}
		return num;
	}
	
	


	/*** ========================================================================================================= ***/
	/**
	 * @Description：查询商户所有收益
	 * @author： origin
	 * @createTime：2020年3月7日上午11:13:11
	 */
	@Override
	public Map<String, Object> dealerMoneyCollect(Integer dealid) {
		try {
			Parameters parame = new Parameters();
			if(!CommUtil.toInteger(dealid).equals(0)) parame.setDealer(CommUtil.toString(dealid));
			//查询作为商户时的在线收益
			Map<String, Object> result = CommUtil.isMapEmpty(merchantDetailDao.dealerIncomeCount(parame));
			
			Double wechatmoney = CommUtil.toDouble(result.get("wechatmoney"));
			Double alipaymoney = CommUtil.toDouble(result.get("alipaymoney"));
			Double refwechatmoney = CommUtil.toDouble(result.get("refwechatmoney"));
			Double refalipaymoney = CommUtil.toDouble(result.get("refalipaymoney"));
			Double incomepaymentmoney = CommUtil.toDouble(result.get("incomepaymentmoney"));
			Double wechatpaymentmoney = CommUtil.toDouble(result.get("wechatpaymentmoney"));
			Double paymentmoney = CommUtil.addBig(wechatpaymentmoney, incomepaymentmoney);
			
			Integer wechatnum = CommUtil.toInteger(result.get("wechatnum"));
			Integer alipaynum = CommUtil.toInteger(result.get("alipaynum"));
			Integer refwechatnum = CommUtil.toInteger(result.get("refwechatnum"));
			Integer refalipaynum = CommUtil.toInteger(result.get("refalipaynum"));
			
			Double totalmoney = CommUtil.addBig(wechatmoney, alipaymoney);
			Double refonlinemoney = CommUtil.addBig(refwechatmoney, refalipaymoney);
			Double onlinemoney = CommUtil.subBig(totalmoney, refonlinemoney);
			Double incomemoney = CommUtil.subBig(onlinemoney, incomepaymentmoney);
			// 微信订单数
			result.put("wechatnum", wechatnum);
			// 支付宝订单数
			result.put("alipaynum", alipaynum);
			// 微信退款订单数
			result.put("refwechatnum", refwechatnum);
			// 支付宝退款订单数
			result.put("refalipaynum", refalipaynum);
			// 微信收益
			result.put("wechatmoney", wechatmoney);
			// 支付宝收益
			result.put("alipaymoney", alipaymoney);
			// 微信退款金额
			result.put("refwechatmoney", refwechatmoney);
			// 支付宝退款金额
			result.put("refalipaymoney", refalipaymoney);
			result.put("incomepaymentmoney", incomepaymentmoney);
			result.put("wechatpaymentmoney", wechatpaymentmoney);
			result.put("paymentmoney", paymentmoney);
			
			result.put("onlinemoney", onlinemoney);
			result.put("refonlinemoney", refonlinemoney);
			result.put("totalmoney", totalmoney);
			result.put("incomemoney", incomemoney);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}

	/**
	 * @Description：查询商户当天收益
	 * @author： origin
	 * @createTime：2020年3月7日上午11:13:17
	 */
	@Override
	public Map<String, Object> dealerNowMoneyCollect(Integer dealid) {
		try {
			Parameters parame = new Parameters();
			if(!CommUtil.toInteger(dealid).equals(0))parame.setDealer(CommUtil.toString(dealid));
			String newtime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
			String endtime = StringUtil.toDateTime("yyyy-MM-dd 23:59:59");
			parame.setStartTime(newtime);
			parame.setEndTime(endtime);
			//查询作为商户时的在线收益
			Map<String, Object> result = CommUtil.isMapEmpty(merchantDetailDao.dealerIncomeCount(parame));
			Double wechatmoney = CommUtil.toDouble(result.get("wechatmoney"));
			Double alipaymoney = CommUtil.toDouble(result.get("alipaymoney"));
			Double refwechatmoney = CommUtil.toDouble(result.get("refwechatmoney"));
			Double refalipaymoney = CommUtil.toDouble(result.get("refalipaymoney"));
			/*Double paymentmoney = CommUtil.toDouble(result.get("paymentmoney"));*/
			Double incomepaymentmoney = CommUtil.toDouble(result.get("incomepaymentmoney"));
			Double wechatpaymentmoney = CommUtil.toDouble(result.get("wechatpaymentmoney"));
			Double paymentmoney = CommUtil.addBig(wechatpaymentmoney, incomepaymentmoney);
			
			Double totalmoney = CommUtil.addBig(wechatmoney, alipaymoney);
			Double refonlinemoney = CommUtil.addBig(refwechatmoney, refalipaymoney);
			Double onlinemoney = CommUtil.subBig(totalmoney, refonlinemoney);
			Double incomemoney = CommUtil.subBig(onlinemoney, incomepaymentmoney);
//			Double onlinemoney = CommUtil.addBig(wechatmoney, alipaymoney);
//			Double refonlinemoney = CommUtil.addBig(refwechatmoney, refalipaymoney);
//			
//			Double totalmoney = CommUtil.addBig(onlinemoney, paymentmoney);
//			
//			Double incomemoney = CommUtil.subBig(totalmoney, refonlinemoney);
			
			Integer wechatnum = CommUtil.toInteger(result.get("wechatnum"));
			Integer alipaynum = CommUtil.toInteger(result.get("alipaynum"));
			Integer refwechatnum = CommUtil.toInteger(result.get("refwechatnum"));
			Integer refalipaynum = CommUtil.toInteger(result.get("refalipaynum"));
			
			result.put("wechatnum", wechatnum);
			result.put("alipaynum", alipaynum);
			result.put("refwechatnum", refwechatnum);
			result.put("refalipaynum", refalipaynum);
			result.put("wechatmoney", wechatmoney);
			result.put("alipaymoney", alipaymoney);
			result.put("refwechatmoney", refwechatmoney);
			result.put("refalipaymoney", refalipaymoney);
			result.put("incomepaymentmoney", incomepaymentmoney);
			result.put("wechatpaymentmoney", wechatpaymentmoney);
			result.put("paymentmoney", paymentmoney);
			
			result.put("onlinemoney", onlinemoney);
			result.put("refonlinemoney", refonlinemoney);
			result.put("totalmoney", totalmoney);
			result.put("incomemoney", incomemoney);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	
	/**
	 * @Description： 统计所有微信、支付宝数据
	 * @author： origin  
	 */
	@Override
	public Map<String, Object> totalmessagerecord() {
		Map<String, Object> collect = new HashMap<>();
		Parameters parameter = new Parameters();
		//总的数据(包含当天的数据)
		Map<String, BigDecimal> totalmap = totalcalculate(parameter);
		String newtime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
		String endtime = StringUtil.toDateTime("yyyy-MM-dd 23:59:59");
		parameter.setStartTime(newtime);
		parameter.setEndTime(endtime);
		//今日的数据
		Map<String, BigDecimal> totalNowmap = totalcalculate(parameter);
		collect.put("totalmap", totalmap);
		collect.put("totalNowmap", totalNowmap);
		return collect;
	}
	/*** @origin 收益汇总信息插入数据库 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ***/
	/**
	 * @Description： 收益汇总信息插入数据库(实体类插入) agent 代理商
	 * @author： origin   
	 */
	private void incomeCollectInset(String code, String merid, String areaid, Integer type, Map<String, BigDecimal> incomeinfo,
			String startTime) {
		Codestatistics codestatistics = new Codestatistics();
		codestatistics.setCode(code);
		codestatistics.setMerid(StringUtil.getIntString(merid));
		codestatistics.setAreaid(StringUtil.getIntString(areaid));
		codestatistics.setType(type);
		try{

			codestatistics.setPaymentmoney(CommUtil.toDouble(incomeinfo.get("paymentmoney")));
			codestatistics.setOrdertotal(incomeinfo.get("totalorder").intValue());
			codestatistics.setMoneytotal(incomeinfo.get("totalmoney").doubleValue());
			

			codestatistics.setWechatorder(incomeinfo.get("wechatnum").intValue());
			codestatistics.setWechatmoney(incomeinfo.get("wechatmoney").doubleValue());
			codestatistics.setWechatretord(incomeinfo.get("refwechatnum").intValue());
			codestatistics.setWechatretmoney(incomeinfo.get("refwechatmoney").doubleValue());
			
			codestatistics.setAlipayorder(incomeinfo.get("alipaynum").intValue());
			codestatistics.setAlipaymoney(incomeinfo.get("alipaymoney").doubleValue());
			codestatistics.setAlipayretord(incomeinfo.get("refalipaynum").intValue());
			codestatistics.setAlipayretmoney(incomeinfo.get("refalipaymoney").doubleValue());
			
			codestatistics.setIncoinsorder(StringUtil.togetBigDecimal(incomeinfo.get("incoins")).intValue());
			codestatistics.setIncoinsmoney(StringUtil.togetBigDecimal(incomeinfo.get("incoinsmoney")).doubleValue());
			codestatistics.setCountTime(CommUtil.DateTime(startTime, "yyyy-MM-dd"));
//			Date time = CommUtil.DateTime(startTime, "yyyy-MM-dd");
//			if(time==null) time = new Date();
//			codestatistics.setCountTime(time);
			codestatisticsDao.insertCodestatis(codestatistics);
			
//			totalincome.put("totalorder", agentincome.get("totalorder").add(partnerincome.get("totalorder")));
//			totalincome.put("totalreforder", agentincome.get("totalreforder").add(partnerincome.get("totalreforder")));
//			totalincome.put("totalmoney", agentincome.get("totalmoney").add(partnerincome.get("totalmoney")));
//			totalincome.put("totalrefmoney", agentincome.get("totalrefmoney").add(partnerincome.get("totalrefmoney")));
//			totalincome.put("incomemoney", totalincome.get("totalmoney").subtract(totalincome.get("totalrefmoney")));
//			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @Description： 合伙人收益汇总信息插入数据库(实体类插入) partner合伙人
	 * @author： origin   
	 */
	private void  parIncomeCollectInset(Integer manid, String time, Map<String, BigDecimal> incomeinfo) {
		Codestatistics codestatistics = new Codestatistics();
		codestatistics.setMerid(manid);
		try {
			codestatistics.setType(2);
			codestatistics.setCountTime(CommUtil.DateTime(time, "yyyy-MM-dd"));
			Codestatistics statistic = codestatisticsDao.selectStatistics(codestatistics);
			if(null==statistic){
				incomeCollectInset("000000", manid.toString(), "01", 2, incomeinfo, time);
			}else{

				Integer totalorder = incomeinfo.get("totalorder").intValue() + statistic.getOrdertotal();
				Double totalmoney = incomeinfo.get("totalmoney").doubleValue()+statistic.getMoneytotal();
				
				Integer wechatnum = incomeinfo.get("wechatnum").intValue() + statistic.getWechatretord();
				Double wechatmoney = incomeinfo.get("wechatmoney").doubleValue()+statistic.getWechatmoney();
				Integer refwechatnum = incomeinfo.get("refwechatnum").intValue() + statistic.getWechatretord();
				Double refwechatmoney = incomeinfo.get("refwechatmoney").doubleValue()+statistic.getWechatretmoney();
				
				Integer alipaynum = incomeinfo.get("alipaynum").intValue() + statistic.getAlipayorder();
				Double alipaymoney = incomeinfo.get("alipaymoney").doubleValue()+statistic.getAlipaymoney();
				Integer refalipaynum = incomeinfo.get("refalipaynum").intValue() + statistic.getAlipayretord();
				Double refalipaymoney = incomeinfo.get("refalipaymoney").doubleValue()+statistic.getAlipayretmoney();

				Integer incoins = incomeinfo.get("incoins").intValue() + statistic.getIncoinsorder();
				Double incoinsmoney = incomeinfo.get("incoinsmoney").doubleValue()+statistic.getIncoinsmoney();
				
				codestatistics.setOrdertotal(totalorder);
				codestatistics.setMoneytotal(totalmoney);
				
				codestatistics.setWechatorder(wechatnum);
				codestatistics.setWechatmoney(wechatmoney);
				codestatistics.setWechatretord(refwechatnum);
				codestatistics.setWechatretmoney(refwechatmoney);
				
				codestatistics.setAlipayorder(alipaynum);
				codestatistics.setAlipaymoney(alipaymoney);
				codestatistics.setAlipayretord(refalipaynum);
				codestatistics.setAlipayretmoney(refalipaymoney);
				
				codestatistics.setIncoinsorder(incoins);
				codestatistics.setIncoinsmoney(incoinsmoney);
				
				codestatisticsDao.updatecodestatistics(statistic);
				System.out.println("合伙人数据插入完成 **************    ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*** @origin 计算汇总充电、脉冲、离线、钱包、在线卡金额信息 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ***/
	/**
	 * @Description： 根据传递的参数查询充电、脉冲、离线、钱包、在线记录并将记录汇总
	 * @author： origin   
	 */
	public Map<String, BigDecimal> totalcalculate(Parameters parameter) {
		Map<String, Object> chargermap = chargeRecordDao.chargetotal(parameter);
		Map<String, Object> incoinstmap = inCoinsDao.incoinstatol(parameter);
		Map<String, Object> offlinemap = offlineCardDao.offlinecardtotal(parameter);
		Map<String, Object> moneymap = moneyDao.moneytotal(parameter);
		Map<String, Object> onlinemap = onlineCardRecordDao.onlinecardtotal(parameter);
	
		if(chargermap==null) chargermap = new HashMap<>();
		if(incoinstmap==null) incoinstmap = new HashMap<>();
		if(offlinemap==null) offlinemap = new HashMap<>();
		if(moneymap==null) moneymap = new HashMap<>();
		if(onlinemap==null) onlinemap = new HashMap<>();
		
		Map<String, BigDecimal> charger = totalBigDecimal(chargermap);
		Map<String, BigDecimal> incoinst = totalBigDecimal(incoinstmap);
		Map<String, BigDecimal> offline = totalBigDecimal(offlinemap);
		Map<String, BigDecimal> money = totalBigDecimal(moneymap);
		Map<String, BigDecimal> online = totalBigDecimal(onlinemap);
		
		Map<String, BigDecimal> mapdata = new HashMap<>();
		mapdata.put("wechatnum", charger.get("wechatnum").add(incoinst.get("wechatnum")).add(offline.get("wechatnum")).add(money.get("wechatnum")).add(online.get("wechatnum")));
		mapdata.put("alipaynum", charger.get("alipaynum").add(incoinst.get("alipaynum")).add(offline.get("alipaynum")));
		mapdata.put("refwechatnum", charger.get("refwechatnum").add(incoinst.get("refwechatnum")).add(offline.get("refwechatnum")).add(money.get("refwechatnum")).add(online.get("refwechatnum")));
		mapdata.put("refalipaynum", charger.get("refalipaynum").add(incoinst.get("refalipaynum")).add(offline.get("refalipaynum")));
		mapdata.put("wechatmoney", charger.get("wechatmoney").add(incoinst.get("wechatmoney")).add(offline.get("wechatmoney")).add(money.get("wechatmoney")).add(online.get("wechatmoney")));
		mapdata.put("alipaymoney", charger.get("alipaymoney").add(incoinst.get("alipaymoney")).add(offline.get("alipaymoney")));
		mapdata.put("refwechatmoney", charger.get("refwechatmoney").add(incoinst.get("refwechatmoney")).add(offline.get("refwechatmoney")).add(money.get("refwechatmoney")).add(online.get("refwechatmoney")));
		mapdata.put("refalipaymoney", charger.get("refalipaymoney").add(incoinst.get("refalipaymoney")).add(offline.get("refalipaymoney")));
		mapdata.put("incoins", incoinst.get("incoins"));
		mapdata.put("incoinsmoney", incoinst.get("incoinsmoney"));
		
		mapdata.put("totalorder", charger.get("totalorder").add(incoinst.get("totalorder")).add(offline.get("totalorder")).add(money.get("totalorder")).add(online.get("totalorder")));
		mapdata.put("totalreforder", charger.get("totalreforder").add(incoinst.get("totalreforder")).add(offline.get("totalreforder")).add(money.get("totalreforder")).add(online.get("totalreforder")));
		mapdata.put("totalmoney", charger.get("totalmoney").add(incoinst.get("totalmoney")).add(offline.get("totalmoney")).add(money.get("totalmoney")).add(online.get("totalmoney")));
		mapdata.put("totalrefmoney", charger.get("totalrefmoney").add(incoinst.get("totalrefmoney")).add(offline.get("totalrefmoney")).add(money.get("totalrefmoney")).add(online.get("totalrefmoney")));
		
		return mapdata;
	}
	


	/*** @origin 商户的计算汇总信息(交易表) ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ***/
	
	/**
	 * @Description： 计算作为商户时指定时间的汇总金额
	 * @author： origin 
	 */
	public Map<String, BigDecimal> agentincomecollect(Integer merid, String startTime, String endTime) {
		Parameters parame = new Parameters();
		parame.setStartTime(startTime);
		parame.setEndTime(endTime);
		parame.setDealer(CommUtil.toString(merid));
		//查询作为商户时的收益
		Map<String, Object> agentincome = tradeRecordDao.merchantCollect(parame);
		if(null==agentincome) agentincome = new HashMap<>();
		//脉冲投币收益
		Map<String, Object> coinsincome = inCoinsDao.selectcoinsup(parame);
		if(null==coinsincome) coinsincome = new HashMap<>();
		int incoins = 0;
		double incoinsmoney = 0.00;
		incoins = CommUtil.toInteger(coinsincome.get("incoins"));
		incoinsmoney = CommUtil.toDouble(coinsincome.get("incoinsmoney"));
		agentincome.put("incoins", incoins);
		agentincome.put("incoinsmoney", incoinsmoney);
		
		Map<String, BigDecimal> agentearning = totalBigDecimal(agentincome);
		return agentearning;
	}
	
	/**
	 * @Description： 计算作为合伙人指定时间的汇总金额
	 * @author： origin 
	 */
	public Map<String, BigDecimal> partnerincomecollect(Integer parid, String startTime, String endTime) {
		Parameters parame = new Parameters();
		parame.setStartTime(startTime);
		parame.setEndTime(endTime);
		parame.setUid(parid);
		//查询作为商户时的收益
		Map<String, Object> agentincome = tradeRecordDao.partnerCollect(parame);
		if(null==agentincome) agentincome = new HashMap<>();
/*		//脉冲投币收益
		Map<String, Object> coinsincome = inCoinsDao.selectcoinsup(parame);
		if(null==coinsincome) coinsincome = new HashMap<>();
		int incoins = 0;
		double incoinsmoney = 0.00;
		incoins = StringUtil.togetBigDecimal(coinsincome.get("incoins")).intValue();
		incoinsmoney = StringUtil.togetBigDecimal(coinsincome.get("incoinsmoney")).doubleValue();
		agentincome.put("incoins", incoins);
		agentincome.put("incoinsmoney", incoinsmoney);
		*/
		Map<String, BigDecimal> partnerearning = totalBigDecimal(agentincome);
		return partnerearning;
	}
	
	/**
	 * @Description： 计算所有绑定设备在指定时间的汇总金额
	 * @author： origin 
	 */
	public int equIncomeCollect(String startTime, String endTime) {
		Parameters parameters = new Parameters();
		List<Map<String, Object>> udeequlist = userEquipmentDao.coderelevance();//查询所有绑定设备
		for(Map<String, Object> item : udeequlist){
			String code = item.get("code").toString();
			String merid = item.get("merid").toString();
			String areaid = item.get("aid").toString();
			String hardversion = item.get("hardversion").toString();
			parameters.setStartTime(startTime);
			parameters.setEndTime(endTime);
			parameters.setDealer(merid);
			parameters.setCode(code);
			Map<String, Object> collectmoney = tradeRecordDao.timingCollectMoney(parameters);
			if(null==collectmoney) collectmoney = new HashMap<String, Object>();
			int incoins = 0;
			double incoinsmoney = 0.00;
			if(!hardversion.equals("04")){
				Map<String, Object> coinscollect = inCoinsDao.selectcoinsup(parameters);
				if(null!=coinscollect){
					incoins = StringUtil.togetBigDecimal(coinscollect.get("incoins")).intValue();
					incoinsmoney = StringUtil.togetBigDecimal(coinscollect.get("incoinsmoney")).doubleValue();
				}
				collectmoney.put("incoins", incoins);
				collectmoney.put("incoinsmoney", incoinsmoney);
			}
			Map<String, BigDecimal> equincomecollect = totalBigDecimal(collectmoney);
			incomeCollectInset(code, merid, areaid, 1, equincomecollect, startTime);
		}
		return 0;
	}

	
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*** @origin 首页图形获取数据 =========================================================================================== ***/
	/**
	 * @Description： 获得用户人数(总人数、月增人数、日增人数、在线消费人数)
	 * @author： origin          
	 * 创建时间：   2019年5月31日 上午10:15:36 
	 */
	@Override
	public Map<String, Object> getTouristPeople(Integer merid, Integer rank) {
		Map<String, Object> tourist = new HashMap<String, Object>();
		Parameters parame = new Parameters();
		if(merid!=null) parame.setUid(merid);
		Map<String, Object> clientsdata = CommUtil.isMapEmpty(userDao.inquireClientsNum(parame));
		Integer clientsnum = CommUtil.toInteger(clientsdata.get("clientsnum"));
		tourist.put("alltourist", clientsnum);
		
		String time = StringUtil.getPastDate(0);
		String startTime = time + " 00:00:00";
		String endTime = time + " 23:59:59";
		parame.setStartTime(startTime);
		parame.setEndTime(endTime);
		Map<String, Object> daytourist = CommUtil.isMapEmpty(userDao.inquireClientsNum(parame));//当天
		tourist.put("daytourist", CommUtil.toInteger(daytourist.get("clientsnum")));
		
		String monthTime = time.substring(0, 8) + "01 00:00:00";
		parame.setStartTime(monthTime);
		Map<String, Object> monthtourist = CommUtil.isMapEmpty(userDao.inquireClientsNum(parame));//当天
		tourist.put("monthtourist", CommUtil.toInteger(monthtourist.get("clientsnum")));
		
//		parame.setStatus("1");
//		List<Map<String, Object>> ispeople = tradeRecordDao.expendtotourist(parame);
//		tourist.put("ispeople", ispeople.size());
		List<Map<String, Object>> ispeople = new ArrayList<Map<String, Object>>();
		tourist.put("ispeople", ispeople.size());
		
		return tourist;
	}
	

	
	//@origin 获取指定时间所有收益
	public Map<String, BigDecimal> earningsnow(Integer merid, String startTime, String endTime){
		Parameters parame = new Parameters();
		if(merid!=null)parame.setDealer(merid.toString());
		parame.setStartTime(startTime);
		parame.setEndTime(endTime);
		Map<String, Object> turnover = tradeRecordDao.timingCollectMoney(parame);
		Map<String, BigDecimal> turnovers = totalBigDecimal(turnover);
		return turnovers;
	}
	
	//@origin 获取指定时间商户收益金额
	public Map<String, BigDecimal> earningsmernow(Integer merid, String startTime, String endTime){
		Map<String, BigDecimal> turnover = null;
		Parameters parame = new Parameters();
		parame.setStartTime(startTime);
		parame.setEndTime(endTime);
		parame.setDealer(merid.toString());
		Map<String, Object> merturnover = tradeRecordDao.merchantCollect(parame);
		
		parame.setDealer(null);
		parame.setUid(merid);
		Map<String, Object> manturnover = tradeRecordDao.partnerCollect(parame);
		turnover = agentincomeTotal(totalBigDecimal(merturnover), totalBigDecimal(manturnover));
		return turnover;
	}
	
	//origin 今日营业额
	@Override
	public Map<String, Object> earningsinfonow() {
		User admin = (User) request.getSession().getAttribute("admin");
		Integer merid = null;
		if(admin.getRank()!=0) merid = admin.getId();
		Map<String, Object> resultamount = userDao.amountStatisticsInfo(merid);
		return resultamount;
	}
//	@Override
//	public Map<String, Object> earningsinfonow() {
//		User admin = (User) request.getSession().getAttribute("admin");
//		String timenow = StringUtil.getPastDate(0);
//		Map<String, BigDecimal> turnovernow = null;
//		if(admin.getRank()==2){
////			turnovernow = earningsmernow(admin.getId(), timenow+" 00:00:00", timenow + " 23:59:59"); 
//			turnovernow = earningsnow(admin.getId(), timenow+" 00:00:00", timenow + " 23:59:59");
//		}else if(admin.getRank()==0){
//			turnovernow = earningsnow(null, timenow+" 00:00:00", timenow + " 23:59:59");
//		}
//		CommUtil.toHashMap(object)
//		Map<String, Object> resultamount = userDao.amountStatisticsInfo(null);
//		turnovernow = turnovernow == null ? new HashMap<String, BigDecimal>() : turnovernow;
//		return resultamount;
//	}	
	
	//@origin 获取昨日收益金额
	@Override
	public Map<String, Object> earningsinfoyes(String startTime, String endTime) {
	//public Object earningsyester(Integer merid, Integer rank){
		Map<String, Object> turnovernow = new HashMap<>();
		User admin = (User) request.getSession().getAttribute("admin");
		Integer merid = admin.getId();
		Integer rank = admin.getRank();
		Parameters parame = new Parameters();
		parame.setStartTime(startTime);
		parame.setEndTime(endTime);
		Map<String, Object> turnover = new HashMap<>();
		if(rank==0){
			turnover = statisticsDao.selectInfototal(parame);
		}else if(rank==2){
			parame.setUid(merid);
			parame.setType("2");
			turnover = codestatisticsDao.agentmoneycollect(parame);
		}
		turnover = CommUtil.isMapEmpty(turnover);
		turnovernow.put("ordertotal", turnover.get("tordertotal")!= null ? turnover.get("tordertotal") : 0);
		turnovernow.put("moneytotal", turnover.get("tmoneytotal")!= null ? turnover.get("tmoneytotal") : 0.00);
		return turnovernow;
	}
	
	//@origin 近一周营业额的趋势
	@Override
	public Object sometimeturnover( String weekstartTime, String weekendTime) {
		User admin = (User) request.getSession().getAttribute("admin");
		List<Map<String, Object>>  turnover = null;
		Parameters parame = new Parameters();
		parame.setStartTime(weekstartTime);
		parame.setEndTime(weekendTime);
		if(admin.getRank()==0){
			parame.setParamete("count_time DESC");
			turnover = statisticsDao.selectInfo(parame);///List<Map<String, Object>>
		}else if(admin.getRank()==2){
			parame.setUid(admin.getId());
			parame.setType("2");
			parame.setParamete("c.count_time DESC");
			turnover = codestatisticsDao.getcodestatistics(parame);
		}
		JSONArray json = new JSONArray();
		for(Map<String,Object> item : turnover){
			Map<String, String> newmap = new HashMap<>();
            for(String s : item.keySet()){ 
            	newmap.put(s, item.get(s).toString());
            }
            JSONObject jsonMap = JSONObject.fromObject(newmap);
			json.add(jsonMap);
        }
		return json;
	}

	//@origin 最近订单信息（交易）
	@Override
	public Object sometimeorder() {
		User admin = (User) request.getSession().getAttribute("admin");
		Parameters parame = new Parameters();
		if(admin.getRank()==2) parame.setUid(admin.getId());
		parame.setStartnumber(0);
		parame.setPages(14);
		List<Map<String, Object>>  trade = tradeRecordDao.selectTradeRecord(parame);
		if(trade==null) trade = new ArrayList<Map<String, Object>>();
		return trade;
	}

	//@origin 昨日设备营业收入排行统计
	@Override
	public List<Map<String, Object>> codeincome() {
		User admin = (User) request.getSession().getAttribute("admin");
		Parameters parame = new Parameters();
		if(admin.getRank()==2) parame.setUid(admin.getId());
		String time = StringUtil.getPastDate(1);
		String startTime = time + " 00:00:00";
		String endTime = StringUtil.toDateTime("yyyy-MM-dd") + " 23:59:59";
		parame.setType("1");
		parame.setStartTime(startTime);
		parame.setEndTime(endTime);
		parame.setStartnumber(0);
		parame.setPages(10);
		parame.setParamete(" c.moneytotal DESC ");
		List<Map<String, Object>> turnover = codestatisticsDao.getcodestatistics(parame);
		return turnover;
	}

	/**
	 * @Description： 查询汇总信息
	 * 				 1:按设备统计  2:按小区统计  3:按时间统计
	 * @author： origin 创建时间：   2019年8月19日 下午3:46:22 
	 * @return 
	 */
	@Override
	public List<Map<String, Object>> earningcollectinfo( User meruser, String begintime, String endtime, 
			Integer type, String param, Integer showincoins) {
		List<Map<String, Object>> info =  new ArrayList<Map<String, Object>>();
		Integer merid = meruser.getId();
		Date timebe = StringUtil.DateTime(begintime, "yyyy-MM-dd HH:mm:ss");
		Date timeen = StringUtil.DateTime(endtime, "yyyy-MM-dd HH:mm:ss");
		if(type==1){
			Boolean fale = StringUtil.belongCalendar(new Date(), timebe, timeen);
			info = equapIncomeStatistics(fale, type, merid, param, begintime, endtime, showincoins);
		}else if(type==2){
			Parameters paramet = new Parameters();
			paramet.setUid(merid);
			paramet.setRemark(param);
			info = areaDao.selectByParame(paramet);//查询商户名下的小区
			info = upEarningName(info, type, merid, begintime, endtime, showincoins);
		}else if(type==3){
			info = upEarningName(info, type, merid, begintime, endtime, showincoins);
		}
		return info == null ? new ArrayList<Map<String, Object>>() : info;
	}
	
	/**
	 * @Description： 查询设备汇总数据
	 * @author： origin 创建时间：   2019年8月22日 下午2:26:14 
	 */
	public List<Map<String, Object>> equapIncomeStatistics(Boolean fale, Integer type, Integer merid, String param,
		String begintime, String endtime, Integer showincoins){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Double totalonearn = 0.00;
		Double totalcoinsearn = 0.00;
		Double totaloncardmoney = 0.00;
		Double nowonearn = 0.00;
		Double nowcoinsearn = 0.00;
		try {
			List<Map<String, Object>> info =  new ArrayList<Map<String, Object>>();
			//根据设备查询商家每台设备在一段时间内每天的消耗总电量
			List<Map<String, Object>> list=chargeRecordDao.selectEveryConsumeQuantity(merid, begintime, endtime);
		
			if(fale){
				Parameters paramet = new Parameters();
				paramet.setCode(CommUtil.toString(param));
				paramet.setDealer(merid.toString());
				info = equipmentDao.selectEquipmentParameter(paramet);
				for(Map<String, Object> item : info){
					Map<String, Object> mapitem = new HashMap<String, Object>();
					String code = CommUtil.toString(item.get("code"));
					nowonearn = CommUtil.toDouble(item.get("now_online_earn"));
					nowcoinsearn = CommUtil.toDouble(item.get("now_coins_earn"));
					mapitem.put("code", code);
					Parameters parames = new Parameters();
					parames.setCode(code);
					parames.setUid(merid);
					parames.setStartTime(begintime);
					parames.setEndTime(endtime);
					Map<String, Object> codestmap = codestatisticsDao.agentmoneycollect(parames);
					
					if(null != codestmap && codestmap.size()>0){
						//遍历所有设备每天消耗的总电量
						for (Map<String, Object> map : list){
							if(map.get("equipmentnum").equals(item.get("code"))&& map.get("consumeQuantity")!=null){
								mapitem.put("consumeQuantity",CommUtil.toDouble(map.get("consumeQuantity"))/100);
							}
						}
						Double moneytotal = CommUtil.toDouble(codestmap.get("moneytotal"));
						Double wechatretmoney = CommUtil.toDouble(codestmap.get("wechatretmoney"));
						Double alipayretmoney = CommUtil.toDouble(codestmap.get("alipayretmoney"));
						Double incoinsmoney = CommUtil.toDouble(codestmap.get("incoinsmoney"));
						Double oncardmoney = CommUtil.toDouble(codestmap.get("oncardmoney"));
						Double windowpulsemoney = CommUtil.toDouble(codestmap.get("windowpulsemoney"));
						Double returnmoney = StringUtil.addBig(wechatretmoney, alipayretmoney);
						moneytotal = StringUtil.subBig(moneytotal, returnmoney);
						totalonearn = CommUtil.addBig(nowonearn, moneytotal);
						Double coinsmoney = CommUtil.addBig(windowpulsemoney, incoinsmoney);
						totalcoinsearn = CommUtil.addBig(coinsmoney, nowcoinsearn);
						totaloncardmoney = oncardmoney;
					}else{
						totalonearn = nowonearn;
						totalcoinsearn = nowcoinsearn;
						//遍历所有设备每天消耗的总电量
						for (Map<String, Object> map : list){
							if(map.get("equipmentnum").equals(item.get("code"))&& map.get("consumeQuantity")!=null){
								mapitem.put("consumeQuantity",CommUtil.toDouble(map.get("consumeQuantity"))/100);
							}
						}
					}
					Double totalmoney = totalonearn;
					if(showincoins.equals(1)) totalmoney = StringUtil.addBig(totalonearn,totalcoinsearn);
					mapitem.put("totalmoney", totalmoney);
					mapitem.put("totalonearn", totalonearn);
					mapitem.put("totalcoinsearn", totalcoinsearn);
					mapitem.put("totaloncardmoney", totaloncardmoney);
					mapitem.put("counttime", StringUtil.getPastDate(0));
					result.add(mapitem);
				}
			}else{
				Parameters params = new Parameters();
				params.setUid(merid);
				params.setType("1");
				params.setStartTime(begintime);
				params.setEndTime(endtime);
				result = codestatisticsDao.agentmoneycollectBycode(params);
				for(Map<String, Object> item : result){
					//遍历所有设备每天消耗的总电量
					for (Map<String, Object> map : list){
						if(map.get("equipmentnum").equals(item.get("code"))&&map.get("consumeQuantity")!=null){
							item.put("consumeQuantity",CommUtil.toDouble(map.get("consumeQuantity"))/100);
							System.out.println(CommUtil.toDouble(map.get("consumeQuantity"))/100+"19号");
						}
					}
					Double moneytotal = CommUtil.toDouble(item.get("moneytotal"));
					Double wechatretmoney = CommUtil.toDouble(item.get("wechatretmoney"));
					Double alipayretmoney = CommUtil.toDouble(item.get("alipayretmoney"));
					
					Double incoinsmoney = CommUtil.toDouble(item.get("incoinsmoney"));
					Double oncardmoney = CommUtil.toDouble(item.get("oncardmoney"));
					Double windowpulsemoney = CommUtil.toDouble(item.get("windowpulsemoney"));
					Double returnmoney = StringUtil.addBig(wechatretmoney, alipayretmoney);
					moneytotal = StringUtil.subBig(moneytotal, returnmoney);
					
					totalonearn = CommUtil.addBig(nowonearn, moneytotal);
					Double coinsmoney = CommUtil.addBig(windowpulsemoney, incoinsmoney);
					totalcoinsearn = CommUtil.addBig(coinsmoney, nowcoinsearn);
					totaloncardmoney = oncardmoney;
					
					Double totalmoney = totalonearn;
					if(showincoins.equals(1)) totalmoney = StringUtil.addBig(totalonearn,totalcoinsearn);
					item.put("totalmoney", totalmoney);
					item.put("totalonearn", totalonearn);
					item.put("totalcoinsearn", totalcoinsearn);
					item.put("totaloncardmoney", totaloncardmoney);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public  List<Map<String, Object>> upEarningName(List<Map<String, Object>> info, Integer type, Integer merid, 
			String begintime, String endtime, Integer showincoins){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Double totalonearn = 0.00;
		Double totalcoinsearn = 0.00;
		Double totaloncardmoney = 0.00;
		Double nowonearn = 0.00;
		Double nowcoinsearn = 0.00;
		Double totalConsume = 0.00; 
		try {
			if(type==2){//小区汇总金额计算
				//根据小区计算电量消耗
				List<Map<String,Object>> list=chargeRecordDao.selectConsumeByArea(merid, begintime, endtime);
				for(Map<String, Object> item : info){
					Double totalmoney = 0.00;
					Map<String, Object> mapitem = new HashMap<String, Object>();
					String areaname = CommUtil.toString(item.get("name"));
					Integer aid = CommUtil.toInteger(item.get("id"));
					mapitem.put("name", areaname);
					Parameters param = new Parameters();
					param.setState(aid.toString());
					param.setStartTime(begintime);
					param.setEndTime(endtime);
					if(!aid.equals(0)){
						Map<String, Object> maparea = CommUtil.isMapEmpty( areaDao.selectAreastatisticsByParam(param));
						totalonearn = CommUtil.toDouble(maparea.get("onlineearn"));
						totalcoinsearn = CommUtil.toDouble(maparea.get("coinearn"));
						totaloncardmoney = CommUtil.toDouble(maparea.get("cardearn"));
						if(showincoins.equals(0) || showincoins.equals(1)){
							Double othermoney = StringUtil.addBig(totalcoinsearn,totaloncardmoney);
							totalmoney = StringUtil.addBig(totalonearn,othermoney);
						}else{
							totalmoney = totalonearn;
						}
						//循环遍历
						for (Map<String, Object> map : list) {
							if(CommUtil.toString(map.get("name")).equals(areaname)){
								mapitem.put("consumeQuantity",CommUtil.toDouble(map.get("consumeQuantity"))/100);
							}
						}
						Date datime = CommUtil.DateTime(begintime, "yyyy-MM-dd");
						mapitem.put("totalmoney", totalmoney);
						mapitem.put("totalonearn", totalonearn);
						mapitem.put("totalcoinsearn", totalcoinsearn);
						mapitem.put("counttime", CommUtil.toDateTime(datime));
						result.add(mapitem);
					}
				}
			}else if(type==3){
				Date timebe = StringUtil.DateTime(begintime, "yyyy-MM-dd HH:mm:ss");
				Date timeen = StringUtil.DateTime(endtime, "yyyy-MM-dd HH:mm:ss");
				Boolean fale = StringUtil.belongCalendar(new Date(), timebe, timeen);
				Map<String, Object> map = new HashMap<String, Object>();
				if(fale){
					Parameters paramet = new Parameters();
					paramet.setUid(merid);
					map = equipmentDao.agentEquipCollect(paramet);
					if(null==map) map = new HashMap<>();
					nowonearn = CommUtil.toDouble(map.get("nowonearn"));
					nowcoinsearn = CommUtil.toDouble(map.get("nowcoinsearn"));
				}
				Parameters params = new Parameters();
				params.setUid(merid);
				params.setType("2");
				params.setStartTime(begintime);
				params.setEndTime(endtime);
				Map<String, Object> codestmap = CommUtil.isMapEmpty(codestatisticsDao.agentmoneycollect(params));
				//根据时间计算商家一段时间的总耗电
//				Integer totalConsume=chargeRecordDao.todayConsumeQuantity(merid, begintime, endtime);
//				if(totalConsume==null){totalConsume=0;}
				
				Double moneytotal = CommUtil.toDouble(codestmap.get("moneytotal"));
				Double wechatretmoney = CommUtil.toDouble(codestmap.get("wechatretmoney"));
				Double alipayretmoney = CommUtil.toDouble(codestmap.get("alipayretmoney"));
				Double paymentmoney = CommUtil.toDouble(codestmap.get("paymentmoney"));
				
				Double returnmoney = StringUtil.addBig(wechatretmoney, alipayretmoney);
				moneytotal = StringUtil.subBig(moneytotal, returnmoney);
				moneytotal = StringUtil.subBig(moneytotal, paymentmoney);
				
				Double incoinsmoney = CommUtil.toDouble(codestmap.get("incoinsmoney"));
				Double oncardmoney = CommUtil.toDouble(codestmap.get("oncardmoney"));
				Double windowpulsemoney = CommUtil.toDouble(codestmap.get("windowpulsemoney"));
				
				totalonearn = CommUtil.addBig(nowonearn, moneytotal);
				Double coinsmoney = CommUtil.addBig(windowpulsemoney, incoinsmoney);
				totalcoinsearn = CommUtil.addBig(coinsmoney, nowcoinsearn);
				totaloncardmoney = oncardmoney;
				
				totalConsume = CommUtil.toDouble(codestmap.get("consumequantity"));
				
				Double totalmoney = totalonearn;
				if(showincoins.equals(1)) totalmoney = StringUtil.addBig(totalonearn,totalcoinsearn);
				//营收总额（ajax请求和默认请求）
				map.put("paymentmoney", paymentmoney);
				map.put("totalConsume", CommUtil.toDouble(totalConsume)/100);
				map.put("totalmoney", totalmoney);
				map.put("totalonearn", totalonearn);
				map.put("totalcoinsearn", totalcoinsearn);
				map.put("totaloncardmoney", totaloncardmoney);
				map.put("totalmoney", totalmoney);
				map.put("counttime", StringUtil.getPastDate(0));
				result.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	@Override
	public List<Map<String, Object>> getStatistics(Parameters paramet) {
		List<Map<String, Object>> statis = codestatisticsDao.getcodestatistics(paramet);
		return statis == null ? new ArrayList<Map<String, Object>>() : statis;
	}
	
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	
//	/**
//	 * @Description： 添加设备插入Codestatistics表数据
//	 * @param: 实体类传参
//	 * @author： origin   2019年9月29日 下午2:16:32
//	 */
//	public void additionCount(String code, Integer merid, Integer areaid, Integer type, Map<String, Object> result,
//			String time){
//		try{
//			Integer wechatnum = CommUtil.toInteger(result.get("wechatnum"));
//			Integer alipaynum = CommUtil.toInteger(result.get("alipaynum"));
//			Integer refwechatnum = CommUtil.toInteger(result.get("refwechatnum"));
//			Integer refalipaynum = CommUtil.toInteger(result.get("refalipaynum"));
//			Double wechatmoney = CommUtil.toDouble(result.get("wechatmoney"));
//			Double alipaymoney = CommUtil.toDouble(result.get("alipaymoney"));
//			Double refwechatmoney = CommUtil.toDouble(result.get("refwechatmoney"));
//			Double refalipaymoney = CommUtil.toDouble(result.get("refalipaymoney"));
//			Double paymentmoney = CommUtil.toDouble(result.get("paymentmoney"));
//			Integer incoins = CommUtil.toInteger(result.get("incoins"));
//			Double incoinsmoney = CommUtil.toDouble(result.get("incoinsmoney"));
//			Date codetime = CommUtil.DateTime(time, "yyyy-MM-dd");
//			Integer totalorder = (wechatnum+alipaynum);
//			Double totalmoney = CommUtil.addBig(wechatmoney, alipaymoney);
//			Double onsumeQuantity = CommUtil.toDouble(result.get("consumeQuantity"));
//			Double consumetime = CommUtil.toDouble(result.get("consumetime"));
//			Double windowpulsemoney = CommUtil.toDouble(result.get("windowpulsemoney"));
//			Double offcardmoney = CommUtil.toDouble(result.get("offcardmoney"));
//			Double oncardmoney = CommUtil.toDouble(result.get("oncardmoney"));
//			Codestatistics codestatistics = new Codestatistics();
//			codestatistics.setConsumequantity(CommUtil.toInteger(onsumeQuantity));
//			codestatistics.setConsumetime(CommUtil.toInteger(consumetime));
//			codestatistics.setCode(code);
//			codestatistics.setMerid(merid);
//			codestatistics.setAreaid(areaid);
//			codestatistics.setType(type);
//			codestatistics.setOrdertotal(totalorder);
//			codestatistics.setMoneytotal(totalmoney);
//			codestatistics.setWechatorder(wechatnum);
//			codestatistics.setWechatmoney(wechatmoney);
//			codestatistics.setWechatretord(refwechatnum);
//			codestatistics.setWechatretmoney(refwechatmoney);
//			codestatistics.setAlipayorder(alipaynum);
//			codestatistics.setAlipaymoney(alipaymoney);
//			codestatistics.setAlipayretord(refalipaynum);
//			codestatistics.setAlipayretmoney(refalipaymoney);
//			codestatistics.setPaymentmoney(paymentmoney);
//			codestatistics.setIncoinsorder(incoins);
//			codestatistics.setIncoinsmoney(incoinsmoney);
//			codestatistics.setWindowpulsemoney(windowpulsemoney);
//			codestatistics.setOffcardmoney(offcardmoney);
//			codestatistics.setOncardmoney(oncardmoney);
//			codestatistics.setCountTime(codetime);
//			codestatisticsDao.insertCodestatis(codestatistics);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	/**
//	 * @Description： 指定经销商当前所绑定的设备收益汇总
//	 * @param:  deaid:经销商id	 time:计算时间
//	 * @author： origin   2019年9月27日 下午7:55:02 
//	 */
//	@Override
//	public void dealerDeviceIncomecollect(Integer deaid, String time) {
//		try {
//			Parameters param = new Parameters();
//			List<UserEquipment> deviceinfo = userEquipmentDao.getUserEquipmentById(deaid);
//			String begintime = time + " 00:00:00";
//			String endtime = time + " 23:59:59";
//			for(UserEquipment item : deviceinfo){
//				String code = CommUtil.toString(item.getEquipmentCode());
//				param.setStartTime(begintime);
//				param.setEndTime(endtime);
//				param.setDealer(deaid.toString());
//				param.setCode(code);
//				
//				Codestatistics statistic = new Codestatistics();
//				statistic.setCode(code);
//				statistic.setType(1);
//				statistic.setMerid(deaid);
//				statistic.setCountTime(CommUtil.DateTime(time, "yyyy-MM-dd"));
//				Codestatistics collect = codestatisticsDao.selectStatistics(statistic);
//				if(collect==null){
//					Map<String, Object> result = tradeRecordDao.timingCollectMoney(param);
//					if(null==result) result = new HashMap<String, Object>();
//					Equipment equlist = equipmentDao.getEquipmentById(code);
//					if(null==equlist) equlist = new Equipment();
//					String harderversion = CommUtil.toString(equlist.getHardversion());
//					if(!harderversion.equals("04")){
//						Map<String, Object> coinscollect = inCoinsDao.selectcoinsup(param);
//						if(null==coinscollect) coinscollect = new HashMap<String, Object>();
//						Integer incoins = CommUtil.toInteger(coinscollect.get("incoins"));
//						Double incoinsmoney = CommUtil.toDouble(coinscollect.get("incoinsmoney"));
//						result.put("incoins", incoins);
//						result.put("incoinsmoney", incoinsmoney);
//					}
//					additionCount(code, deaid, CommUtil.toInteger(equlist.getAid()), 1, result, time);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	/**
//	 * @Description： 设备收益 计算汇总数据(所有绑定的设备)
//	 * @param: (time:汇总数据的时间）
//	 * @author： origin   2019年9月27日 下午8:13:07 
//	 */
//	@Override
//	public void dealDeviIncoCollectAll(String time) {
//		try {
//			String begintime = time + " 00:00:00";
//			String endtime = time + " 23:59:59";
//			List<Map<String, Object>> udeequlist = userEquipmentDao.coderelevance();//查询所有绑定设备
//			for(Map<String, Object> item : udeequlist){
//				String code = CommUtil.toString(item.get("code"));
//				Integer merid = CommUtil.toInteger(item.get("merid"));
//				Integer aid = CommUtil.toInteger(item.get("aid"));
//				String hardversion = CommUtil.toString(item.get("hardversion"));
//				
//				Codestatistics statistic = new Codestatistics();
//				statistic.setCode(code);
//				statistic.setType(1);
//				statistic.setMerid(merid);
//				statistic.setCountTime(CommUtil.DateTime(time, "yyyy-MM-dd"));
//				Codestatistics collect = codestatisticsDao.selectStatistics(statistic);
//				if(collect==null){
//					Parameters param = new Parameters();
//					param.setStartTime(begintime);
//					param.setEndTime(endtime);
//					param.setDealer(merid.toString());
//					param.setCode(code);
//					Map<String, Object> collectmoney = tradeRecordDao.timingCollectMoney(param);
//					if(null==collectmoney) collectmoney = new HashMap<String, Object>();
//					if(!hardversion.equals("04")){
//						Map<String, Object> coinscollect = inCoinsDao.selectcoinsup(param);
//						if(null==coinscollect) coinscollect = new HashMap<String, Object>();
//						Integer incoins = StringUtil.togetBigDecimal(coinscollect.get("incoins")).intValue();
//						Double incoinsmoney = StringUtil.togetBigDecimal(coinscollect.get("incoinsmoney")).doubleValue();
//						collectmoney.put("incoins", incoins);
//						collectmoney.put("incoinsmoney", incoinsmoney);
//					}
//					additionCount(code, CommUtil.toInteger(merid), aid, 1, collectmoney, time);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	//*** 汇总数据计算  **********************************************************************************************
	
	
	//ROTO ORIGIN
	/**
	 * @Description： 设备收入信息汇总记录
	 * @author： origin  
	 */
	public int codecollect(String startTime, String endTime) {
		try {
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
//					incoins = StringUtil.togetBigDecimal(coinscollect.get("incoins")).intValue();
//					incoinsmoney = StringUtil.togetBigDecimal(coinscollect.get("incoinsmoney")).doubleValue();
					incoins = CommUtil.toInteger(coinscollect.get("incoins"));
					incoinsmoney = CommUtil.toDouble(coinscollect.get("incoinsmoney"));
					collectmoney.put("incoins", incoins);
					collectmoney.put("incoinsmoney", incoinsmoney);
				}
				List<Map<String, Object>> consumebytimes=chargeRecordDao.selectEveryConsumeQuantity(CommUtil.toInteger(merid), startTime, endTime);
//				List<Map<String, Object>> consumebytime = chargeRecordDao.selectConsumeByTime(CommUtil.toInteger(merid), startTime, endTime);
				consumebytimes = CommUtil.isListMapEmpty(consumebytimes);
				for(Map<String, Object> items : consumebytimes){
					String equipmentnum = CommUtil.toString(items.get("equipmentnum"));
					if(equipmentnum.equals(code)){
						collectmoney.put("consumeQuantity",CommUtil.toDouble(items.get("consumeQuantity")));
						collectmoney.put("consumetime",CommUtil.toDouble(items.get("consumetime")));
					}
				}
				collectrecord(code, merid, areaid, 1, collectmoney, startTime);
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
		return 0;
	}
	//--- 设备收益汇总 -----------------------------------------------------------------------------------------------
	/**
	 * @Description： 根据交易记录计算设备汇总金额与计算脉冲设备中线下投币上传数据信息
	 * @author： origin   
	 */
	@Override
	public void devivceTimingCollect(String time){
		try {
			String begintime = time + " 00:00:00";
			String endtime = time + " 23:59:59";
			deviceIncomeDataGain(begintime, endtime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description：商户定时更新电量、时间
	 * @author： origin
	 * @createTime：2020年3月13日下午5:52:37
	 */
	@Override
	public void dealerupdataquantity(String time) {
		try {
			String startTime = time + " 00:00:00";
			String endTime = time + " 23:59:59";
			Parameters parameters = new Parameters();
			parameters.setStartTime(startTime);
			parameters.setEndTime(endTime);
			Integer dealid = null;
//			List<Map<String,Object>>  consumebytimes = CommUtil.isListMapEmpty(chargeRecordDao.selectEveryConsumeQuantity( dealid, startTime, endTime));
			List<Map<String,Object>>  consumebytimes = CommUtil.isListMapEmpty(chargeRecordDao.inquireInfoCollect(parameters));
			Codestatistics statistic = new Codestatistics();
			statistic.setType(2);
			statistic.setMerid(dealid);
			statistic.setCountTime(CommUtil.DateTime(time, "yyyy-MM-dd"));
			List<Codestatistics> itemcollect = codestatisticsDao.selectStatisticsData(statistic);
			
			for(Codestatistics item : itemcollect){
				Integer merid = CommUtil.toInteger(item.getMerid());
				Integer statisid = CommUtil.toInteger(item.getId());
				for(Map<String,Object> itemqua : consumebytimes){
					Integer consmerid = CommUtil.toInteger(itemqua.get("merid"));
					if(merid!=0 && merid.equals(consmerid)){
						Integer consumeQuantity = CommUtil.toInteger(itemqua.get("consumequantity"));
						Integer consumetime = CommUtil.toInteger(itemqua.get("consumetime"));
						Codestatistics statis = new Codestatistics();
						statis.setId(statisid);
						statis.setConsumequantity(consumeQuantity);
						statis.setConsumetime(consumetime);
						codestatisticsDao.updatecodestatistics(statis);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
		
	}
	
	/**
	 * @Description：  指绑定的设备某一天收益的汇总处理
	 * @param: (time:汇总数据的时间）
	 * @author： origin 
	 */
	@Override
	public void dealDeviIncoCollectAll(String time) {
		try {
			String begintime = time + " 00:00:00";
			String endtime = time + " 23:59:59";
			deviceIncomeDataGain(begintime, endtime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description： 指定商户绑定的设备某一天收益的汇总处理
	 * @param:  deaid:经销商id	 time:计算时间
	 * @author： origin   2019年9月27日 下午7:55:02 
	 */
	@Override
	public void dealerDeviceIncomeDispose(Integer deaid, String time) {
		try {
			Parameters paramet = new Parameters();
			List<UserEquipment> deviceinfo = userEquipmentDao.getUserEquipmentById(deaid);
			String begintime = time + " 00:00:00";
			String endtime = time + " 23:59:59";
			Integer type = 1;
			for(UserEquipment item : deviceinfo){
				String code = CommUtil.toString(item.getEquipmentCode());
				String merid = CommUtil.toString(deaid);
				
				paramet.setStartTime(begintime);
				paramet.setEndTime(endtime);
				paramet.setDealer(deaid.toString());
				paramet.setCode(code);
				
				Codestatistics statistic = new Codestatistics();
				statistic.setType(1);
				statistic.setMerid(deaid);
				statistic.setCountTime(CommUtil.DateTime(time, "yyyy-MM-dd"));
				Codestatistics collect = codestatisticsDao.selectStatistics(statistic);
				if(collect==null){
					Equipment equlist = equipmentDao.getEquipmentById(code);
					if(null==equlist) equlist = new Equipment();
					String hardversion = CommUtil.toString(equlist.getHardversion());
					String areaid = CommUtil.toString(equlist.getAid());
					
					Map<String, Object> collectmoney = CommUtil.isMapEmpty(tradeRecordDao.timingCollectMoney(paramet));
					Integer incoins = 0;
					Double incoinsmoney = 0.00;
					if(!hardversion.equals("04")){
						Map<String, Object> coinscollect = CommUtil.isMapEmpty(inCoinsDao.selectcoinsup(paramet));
						incoins = CommUtil.toInteger(coinscollect.get("incoins"));
						incoinsmoney = CommUtil.toDouble(coinscollect.get("incoinsmoney"));
						collectmoney.put("incoins", incoins);
						collectmoney.put("incoinsmoney", incoinsmoney);
					}
					Map<String, Object> consumebytimes = CommUtil.isMapEmpty(chargeRecordDao.selectCodeConsumeQuantity(paramet));
					collectmoney.put("consumequantity", CommUtil.toInteger(consumebytimes.get("consumeQuantity")));
					collectmoney.put("consumetime", CommUtil.toInteger(consumebytimes.get("consumetime")));
					insertCollectData(code, CommUtil.toInteger(merid), CommUtil.toInteger(areaid), type, collectmoney, time);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * employ
	 * @Description： 设备收入信息获取记录并插入
	 * @author： origin  
	 */
	public void deviceIncomeDataGain(String startTime, String endTime) {
		try {
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
			logger.info(CommUtil.getExceptInfo(e));
		}
	}
	
	//--- 商户收益汇总 -----------------------------------------------------------------------------------------------
	/**
	 * employ
	 * @Description：（手动结算时） 经销商收益汇总
	 * @param:  deaid:经销商id	 time:计算时间
	 * @author： origin   2019年9月27日 下午7:54:51 
	 */
	@Override
	public void dealerIncomecollect(Integer deaid, String time) {
		try {
			Codestatistics statistic = new Codestatistics();
			statistic.setType(2);
			statistic.setMerid(deaid);
			statistic.setCountTime(CommUtil.DateTime(time, "yyyy-MM-dd"));
			Codestatistics collect = codestatisticsDao.selectStatistics(statistic);
			if(collect==null){
				String begintime = time + " 00:00:00";
				String endtime = time + " 23:59:59";
				dealerIncomeDataGain("000000", CommUtil.toInteger(deaid), "00", 2, begintime, endtime, time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * employ
	 * @Description：（手动结算时）  商户收益 计算汇总数据(所有绑定的设备的商户与合伙人)
	 * @param: (time:汇总数据的时间）
	 * @author： origin 
	 */
	@Override
	public void dealIncoCollectAll(String time) {
		try {
			String begintime = time + " 00:00:00";
			String endtime = time + " 23:59:59";
			List<Map<String, Object>> binddeviceuser = userEquipmentDao.distinctuid();//绑定设备的商户
			Codestatistics statistic = new Codestatistics();
			statistic.setType(2);
			for(Map<String, Object> item : binddeviceuser){
				Integer merid = CommUtil.toInteger(item.get("user_id"));
				if(merid>0){
					statistic.setMerid(merid);
					statistic.setCountTime(CommUtil.DateTime(time, "yyyy-MM-dd"));
					Codestatistics collect = codestatisticsDao.selectStatistics(statistic);
					if(collect==null){
						dealerIncomeDataGain("000000", merid, "00", 2,  begintime, endtime, time);
					}
				}
			}
			
			List<Map<String, Object>> partuser = areaDao.areaRelPartidInfo();
			for(Map<String, Object> item : partuser){
				Integer merid = CommUtil.toInteger(item.get("partid"));
				if(merid>0){
					statistic.setMerid(merid);
					statistic.setCountTime(CommUtil.DateTime(time, "yyyy-MM-dd"));
					Codestatistics collect = codestatisticsDao.selectStatistics(statistic);
					if(collect==null){
						dealerIncomeDataGain("000000", merid, "00", 2,  begintime, endtime, time);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * employ
	 * @Description： 商户收益自动汇总
	 * @author： origin 创建时间：   2019年9月18日 下午5:42:58 
	 */
	@Override
	public void dealerIncomeCollect(String time) {
		try {
			String begintime = time + " 00:00:00";
			String endtime = time + " 23:59:59";
			Parameters parame = new Parameters();
			parame.setStartTime(begintime);
			parame.setEndTime(endtime);
			
			List<Map<String, Object>> collectuser = codestatisticsDao.inquireCollectMerid(parame);
			List<Map<String, Object>> meralluser = userEquipmentDao.inquireAllmeridInfo();//查询绑定设备的商户与未绑定设备的合伙人
//			List<Map<String, Object>> meruser = merchantDetailDao.inquireMeridInfo(parame);
			meralluser.removeAll(collectuser);
			for(Map<String, Object> item : meralluser){
				Integer dealid = CommUtil.toInteger(item.get("merid"));
				if(dealid>0) dealerIncomeDataGain("000000", dealid, "0", 2,  begintime, endtime, time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * employ
	 * @return 
	 * @Description：商户收益信息获取计算
	 * @author： origin
	 */
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
			
//			//商户收益中在线卡、串口投币、离线卡统计
//			Map<String, Object> cardandpulsecollect = CommUtil.isMapEmpty(chargeRecordDao.seleCardAndPulseCollect(parame));
//			//查询商家的耗电量和耗电时间
//			Map<String, Object> codeStatisticMer = CommUtil.isMapEmpty(chargeRecordDao.codeStatisticMer(merid, begintime, endtime));
//			Integer consumequantity = CommUtil.toInteger(codeStatisticMer.get("consumequantity"));
//			Integer consumetime = CommUtil.toInteger(codeStatisticMer.get("consumetime"));
//			Double windowpulsemoney = CommUtil.toDouble(cardandpulsecollect.get("windowpulsemoney"));
//			Double offcardmoney = CommUtil.toDouble(cardandpulsecollect.get("offcardmoney"));
//			Double oncardmoney = CommUtil.toDouble(cardandpulsecollect.get("oncardmoney"));
			Integer incoins = CommUtil.toInteger(coinsincome.get("incoins"));
			Double incoinsmoney = CommUtil.toDouble(coinsincome.get("incoinsmoney"));

			Integer consumequantity = CommUtil.toInteger(chargeresult.get("consumequantity"));//consumequantity
			Integer consumetime = CommUtil.toInteger(chargeresult.get("consumetime"));//consumetime
			Double windowpulsemoney = CommUtil.toDouble(chargeresult.get("windowpulsemoney"));
			Double offcardmoney = CommUtil.toDouble(chargeresult.get("offcardmoney"));
			Double oncardmoney = CommUtil.toDouble(chargeresult.get("oncardmoney"));
//			result.put("code", code);
//			result.put("merid", merid);
//			result.put("areaid", areaid);
//			result.put("type", type);
//			result.put("time", time);
			result.put("windowpulsemoney", windowpulsemoney);
			result.put("offcardmoney", offcardmoney);
			result.put("oncardmoney", oncardmoney);
			result.put("consumeQuantity", consumequantity);
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
	
	/**
	 * employ
	 * @Description：直接插入汇总数据
	 * @author： origin
	 */
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
			codestatisticsDao.insertCodestatis(collectData);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//原有汇总数据 增加
	public void accretionCollectData(Codestatistics collectinfo, Map<String, Object> result){
		try{
//			code = CommUtil.toString(result.get("code"));
//			merid = CommUtil.toInteger(result.get("merid"));
//			areaid = CommUtil.toInteger(result.get("areaid"));
//			type = CommUtil.toInteger(result.get("type"));
			
			Integer wechatnum = CommUtil.toInteger(result.get("wechatnum"));
			Integer alipaynum = CommUtil.toInteger(result.get("alipaynum"));
			Integer refwechatnum = CommUtil.toInteger(result.get("refwechatnum"));
			Integer refalipaynum = CommUtil.toInteger(result.get("refalipaynum"));
			
			Double wechatmoney = CommUtil.toDouble(result.get("wechatmoney"));
			Double alipaymoney = CommUtil.toDouble(result.get("alipaymoney"));
			Double refwechatmoney = CommUtil.toDouble(result.get("refwechatmoney"));
			Double refalipaymoney = CommUtil.toDouble(result.get("refalipaymoney"));
			
			Double paymentmoney = CommUtil.toDouble(result.get("paymentmoney"));
			
			Integer incoins = CommUtil.toInteger(result.get("incoins"));
			Double incoinsmoney = CommUtil.toDouble(result.get("incoinsmoney"));
			
			Integer consumetime = CommUtil.toInteger(result.get("consumetime"));
			Integer consumequantity = CommUtil.toInteger(result.get("consumequantity"));
			
			Double offcardmoney = CommUtil.toDouble(result.get("offcardmoney"));
			Double oncardmoney = CommUtil.toDouble(result.get("oncardmoney"));
			Double windowpulsemoney = CommUtil.toDouble(result.get("windowpulsemoney"));
			
//			Integer offconsumetime = CommUtil.toInteger(result.get("offconsumetime"));
//			Integer offconsumequantity = CommUtil.toInteger(result.get("offconsumequantity"));
//			Integer onconsumetime = CommUtil.toInteger(result.get("onconsumetime"));
//			Integer onconsumequantity = CommUtil.toInteger(result.get("onconsumequantity"));
//			Integer pulseconsumetime = CommUtil.toInteger(result.get("pulseconsumetime"));
//			Integer pulseconsumequantity = CommUtil.toInteger(result.get("pulseconsumequantity"));

			Double totalmoney = CommUtil.addBig(wechatmoney, alipaymoney);
			Double onlinereturnmoney = CommUtil.addBig(refwechatmoney, refalipaymoney);
			Double onlinemoney = CommUtil.subBig(totalmoney, onlinereturnmoney);
			Double incomemoney = CommUtil.subBig(onlinemoney, paymentmoney);
			
			Integer totalorder =  CommUtil.toInteger(wechatnum + alipaynum);
			Integer onreturnorder = CommUtil.toInteger(refwechatnum + refalipaynum);
			Integer onlineorder = CommUtil.toInteger(totalorder - onreturnorder);
			
			collectinfo.setOrdertotal(CommUtil.toInteger(collectinfo.getOrdertotal()) + totalorder);
			collectinfo.setMoneytotal(CommUtil.addBig(CommUtil.toDouble(collectinfo.getMoneytotal()) ,totalmoney));
			
			collectinfo.setWechatorder(CommUtil.toInteger(collectinfo.getWechatorder()) + wechatnum);
			collectinfo.setAlipayorder(CommUtil.toInteger(collectinfo.getAlipayorder()) + alipaynum);
			collectinfo.setWechatretord(CommUtil.toInteger(collectinfo.getWechatretord()) + refwechatnum);
			collectinfo.setAlipayretord(CommUtil.toInteger(collectinfo.getAlipayretord()) + refalipaynum);
			
			collectinfo.setWechatmoney(CommUtil.addBig(CommUtil.toDouble(collectinfo.getWechatmoney()) ,wechatmoney));
			collectinfo.setAlipaymoney(CommUtil.addBig(CommUtil.toDouble(collectinfo.getAlipaymoney()) ,alipaymoney));
			collectinfo.setWechatretmoney(CommUtil.addBig(CommUtil.toDouble(collectinfo.getWechatretmoney()) ,refwechatmoney));
			collectinfo.setAlipayretmoney(CommUtil.addBig(CommUtil.toDouble(collectinfo.getAlipayretmoney()) ,refalipaymoney));
			
			collectinfo.setPaymentmoney(CommUtil.addBig(CommUtil.toDouble(collectinfo.getPaymentmoney()) ,paymentmoney));
			
			collectinfo.setIncoinsorder(CommUtil.toInteger(collectinfo.getIncoinsorder()) + incoins);
			collectinfo.setIncoinsmoney(CommUtil.addBig(CommUtil.toDouble(collectinfo.getIncoinsmoney()) ,incoinsmoney));
			
			collectinfo.setConsumequantity(CommUtil.toInteger(collectinfo.getConsumequantity()) + consumequantity);
			collectinfo.setConsumetime(CommUtil.toInteger(collectinfo.getConsumetime()) + consumetime);
			
			collectinfo.setOffcardmoney(CommUtil.addBig(CommUtil.toDouble(collectinfo.getOffcardmoney()) ,offcardmoney));
//			collectinfo.setOffconsumetime(CommUtil.toInteger(collectinfo.get) + offconsumetime);;
//			collectinfo.setOffconsumequantity(CommUtil.toInteger(collectinfo.get) + offconsumequantity);
			collectinfo.setWindowpulsemoney(CommUtil.addBig(CommUtil.toDouble(collectinfo.getWindowpulsemoney()) ,windowpulsemoney));
//			collectinfo.setPulseconsumetime(CommUtil.toInteger(collectinfo.get) + pulseconsumetime);
//			collectinfo.setPulseconsumequantity(CommUtil.toInteger(collectinfo.get) + pulseconsumequantity);
			collectinfo.setOncardmoney(CommUtil.addBig(CommUtil.toDouble(collectinfo.getOncardmoney()) ,oncardmoney));
//			collectinfo.setOnconsumetime(CommUtil.toInteger(collectinfo.get) + onconsumetime);
//			collectinfo.setOnconsumequantity(CommUtil.toInteger(collectinfo.get) + onconsumequantity);
			codestatisticsDao.insertCodestatis(collectinfo);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// === 前后段分离处理数据信息 开始  ==================================================================================
	/**
	 * separate
	 * @Description： 收益汇总信息 type 1:设备收益信息  2：商户收益信息
	 * @author： origin   2019年11月2日 下午4:19:46 
	 */
	@Override
	public Object earningsData(HttpServletRequest request, Integer type) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			Parameters parameters = new Parameters();
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage = CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			User user = CommonConfig.getAdminReq(request);
			//===========================================
			//前端传递代理商名下某一个商家的id
			Integer agentSelectmerid =  CommUtil.toInteger(maparam.get("agentSelectmerid"));
			if(agentSelectmerid != null && !agentSelectmerid.equals(0)){
				user = new User();
				user.setRank(2);
				user.setId(agentSelectmerid);
			}
			//====================================================
			
			parameters.setType(CommUtil.toString(type));
			Integer rankmoney =  CommUtil.toInteger(maparam.get("rankmoney"));//金额
			Integer rankorder =  CommUtil.toInteger(maparam.get("rankorder"));//订单
			if(rankmoney.equals(0) && rankorder.equals(0)){
				parameters.setParamete("c.count_time DESC");
			}else if(!rankmoney.equals(0) && rankorder.equals(0)){ 
				if(rankmoney.equals(1)){
					parameters.setParamete("c.moneytotal DESC");
				}else if(rankmoney.equals(2)){
					parameters.setParamete("c.moneytotal ASC");
				}
			}else if(rankmoney.equals(0) && !rankorder.equals(0)){ 
				if(rankorder.equals(1)){
					parameters.setParamete("c.ordertotal DESC");
				}else if(rankorder.equals(2)){
					parameters.setParamete("c.ordertotal ASC");
				}
			}else if(!rankmoney.equals(0) && !rankorder.equals(0)){ 
				if(rankmoney.equals(1) && rankorder.equals(1)){
					parameters.setParamete("c.moneytotal DESC, c.ordertotal DESC");
				}else if(rankmoney.equals(1) && rankorder.equals(2)){
					parameters.setParamete("c.moneytotal DESC, c.ordertotal ASC");
				}else if(rankmoney.equals(2) && rankorder.equals(1)){
					parameters.setParamete("c.moneytotal ASC, c.ordertotal DESC");
				}else if(rankmoney.equals(2) && rankorder.equals(2)){
					parameters.setParamete("c.moneytotal ASC, c.ordertotal ASC");
				}
			}
			parameters.setCode(CommUtil.toString(maparam.get("devicenum")));
			parameters.setPhone(CommUtil.toString(maparam.get("phone")));
//			parameters.setNickname(CommUtil.toString(maparam.get("nick")));
			parameters.setUsername(CommUtil.toString(maparam.get("realname")));
//			parameters.setSource(CommUtil.toString(maparam.get("areaname")));
			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
			
			
			Integer userrank = user.getRank();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
			
			Integer exportType =  CommUtil.toInteger(maparam.get("exportType"));//月份 1：天   2：月
			if(exportType.equals(1)){
				if(userrank!=0) parameters.setUid(user.getId());//获取用户
				List<Map<String, Object>>  codestatismap = codestatisticsDao.getcodestatistics(parameters);
				page.setTotalRows(codestatismap.size());
				page.setTotalPages();
				page.setStart();
				page.setEnd();
				parameters.setPages(page.getNumPerPage());
				parameters.setStartnumber(page.getStartIndex());
				List<Map<String, Object>> chargemapli = codestatisticsDao.getcodestatistics(parameters);
				Map<String, Object>  codestatis = amountStatisticsData(codestatismap);
				datamap.put("listdata", chargemapli);
				datamap.put("totaldata", codestatis);
				datamap.put("totalRows", page.getTotalRows());
				datamap.put("totalPages", page.getTotalPages());
				datamap.put("currentPage", page.getCurrentPage());
			}else if(exportType.equals(2)){
				if(userrank!=0) parameters.setDealer(user.getId().toString());//获取用户
				List<Map<String, Object>>  codestatismap = codestatisticsDao.inquireCollectMonthInfo(parameters);
				page.setTotalRows(codestatismap.size());
				page.setTotalPages();
				page.setStart();
				page.setEnd();
				parameters.setPages(page.getNumPerPage());
				parameters.setStartnumber(page.getStartIndex());
				List<Map<String, Object>> chargemapli = codestatisticsDao.inquireCollectMonthInfo(parameters);
				Map<String, Object>  codestatis = amountStatisticsData(codestatismap);
				datamap.put("listdata", chargemapli);
				datamap.put("totaldata", codestatis);
				datamap.put("totalRows", page.getTotalRows());
				datamap.put("totalPages", page.getTotalPages());
				datamap.put("currentPage", page.getCurrentPage());
			}
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	/**
	 * separate
	 * @Description： 商户收益下载
	 * @author： origin   2019年11月2日 下午4:19:46 
	 */
	@Override
	public Object dealerEarningsDownload(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			Parameters parameters = new Parameters();
			User user = CommonConfig.getAdminReq(request);
			//===========================================
//			//前端传递代理商名下某一个商家的id
//			Integer agentSelectmerid =  CommUtil.toInteger(maparam.get("agentSelectmerid"));
//			if(agentSelectmerid != null && !agentSelectmerid.equals(0)){
//				user = new User();
//				user.setRank(2);
//				user.setId(agentSelectmerid);
//			}
			//====================================================
			Integer userrank = user.getRank();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
			Integer type = userrank;
			Integer dealid = user.getId();

			String startTime = CommUtil.toString(maparam.get("startTime"));
			String endTime = CommUtil.toString(maparam.get("endTime"));
			String phone = CommUtil.toString(maparam.get("phone"));
			Integer exportType = CommUtil.toInteger(maparam.get("exportType"));
			
			parameters.setDealer(dealid.toString());
			parameters.setStartTime(startTime);
			parameters.setEndTime(endTime);
			if(type==0 && phone != null){
				if(phone.length()!=11) return CommUtil.responseBuildInfo(401, "请输入正确的11位手机号", datamap);
				User dealuser = userDao.getUserByPhoneNum(phone);
				if(dealuser==null) return CommUtil.responseBuildInfo(402, "所属手机号用户不存在", datamap);
				parameters.setDealer(dealuser.getId().toString());
			}
			parameters.setType("2");
			List<Map<String, Object>>  codestatis = null;
			if(exportType.equals(1)){//按天导出
				codestatis = CommUtil.isListMapEmpty(codestatisticsDao.inquireCollectDay(parameters));
			} else if(exportType.equals(2)){//按月导出
				codestatis = CommUtil.isListMapEmpty(codestatisticsDao.inquireCollectMonth(parameters));
			}
			datamap.put("listdata", codestatis);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	public Map<String, Object> amountStatisticsData(List<Map<String, Object>> codestatismap) {
		Map<String, Object>  codestatis = new HashMap<String, Object>();
		Double moneytotal = 0.00,wechatmoney = 0.00,alipaymoney = 0.00,wechatretmoney = 0.00,alipayretmoney = 0.00,
				incoinsmoney = 0.00, oncardmoney = 0.00, windowpulsemoney = 0.00;
		Integer ordertotal = 0,wechatorder = 0,alipayorder = 0,wechatretord = 0,alipayretord = 0,incoinsorder = 0;
		for(Map<String, Object> item : codestatismap){
			moneytotal = CommUtil.addBig(moneytotal, CommUtil.toDouble(item.get("moneytotal")));
			ordertotal = ordertotal + CommUtil.toInteger(item.get("ordertotal"));
			wechatmoney = CommUtil.addBig(wechatmoney, CommUtil.toDouble(item.get("wechatmoney")));
			wechatorder = wechatorder + CommUtil.toInteger(item.get("wechatorder"));
			alipaymoney = CommUtil.addBig(alipaymoney, CommUtil.toDouble(item.get("alipaymoney")));
			alipayorder = alipayorder + CommUtil.toInteger(item.get("alipayorder"));
			wechatretmoney = CommUtil.addBig(wechatretmoney, CommUtil.toDouble(item.get("wechatretmoney")));
			wechatretord = wechatretord + CommUtil.toInteger(item.get("wechatretord"));
			alipayretmoney = CommUtil.addBig(alipayretmoney, CommUtil.toDouble(item.get("alipayretmoney")));
			alipayretord = alipayretord + CommUtil.toInteger(item.get("alipayretord"));
			incoinsmoney = CommUtil.addBig(incoinsmoney, CommUtil.toDouble(item.get("incoinsmoney")));
			incoinsorder = incoinsorder + CommUtil.toInteger(item.get("incoinsorder"));

			oncardmoney = CommUtil.addBig(oncardmoney, CommUtil.toDouble(item.get("oncardmoney")));
			windowpulsemoney = CommUtil.addBig(windowpulsemoney, CommUtil.toDouble(item.get("windowpulsemoney")));
		}
		Double refundmoney = CommUtil.addBig(wechatretmoney, alipayretmoney);
		codestatis.put("earningstotal", CommUtil.subBig(moneytotal, refundmoney));
		codestatis.put("refundmoney", refundmoney);
		codestatis.put("moneytotal", moneytotal);
		codestatis.put("ordertotal", ordertotal);
		codestatis.put("wechatmoney", wechatmoney);
		codestatis.put("wechatorder", wechatorder);
		codestatis.put("alipaymoney", alipaymoney);
		codestatis.put("alipayorder", alipayorder);
		codestatis.put("wechatretmoney", wechatretmoney);
		codestatis.put("wechatretord", wechatretord);
		codestatis.put("alipayretmoney", alipayretmoney);
		codestatis.put("alipayretord", alipayretord);
		codestatis.put("incoinsmoney", incoinsmoney);
		codestatis.put("incoinsorder", incoinsorder);
		codestatis.put("oncardmoney", oncardmoney);
		codestatis.put("windowpulsemoney", windowpulsemoney);
		return codestatis;
	}

	/**
	 * separate
	 * @Description： 历史统计信息
	 * @author： origin   2019年11月1日 下午5:32:14
	 */
	@Override
	public Object historyStatisticsData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage = CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			Parameters parameter = new Parameters();
			Integer rankmoney =  CommUtil.toInteger(maparam.get("rankmoney"));//金额
			Integer rankorder =  CommUtil.toInteger(maparam.get("rankorder"));//订单
			if(rankmoney.equals(0) && rankorder.equals(0)){
				parameter.setParamete("count_time DESC");
			}else if(!rankmoney.equals(0) && rankorder.equals(0)){ 
				if(rankmoney.equals(1)){
					parameter.setParamete("moneytotal DESC");
				}else if(rankmoney.equals(2)){
					parameter.setParamete("moneytotal ASC");
				}
			}else if(rankmoney.equals(0) && !rankorder.equals(0)){ 
				if(rankorder.equals(1)){
					parameter.setParamete("ordertotal DESC");
				}else if(rankorder.equals(2)){
					parameter.setParamete("ordertotal ASC");
				}
			}else if(!rankmoney.equals(0) && !rankorder.equals(0)){ 
				if(rankmoney.equals(1) && rankorder.equals(1)){
					parameter.setParamete("moneytotal DESC,ordertotal DESC");
				}else if(rankmoney.equals(1) && rankorder.equals(2)){
					parameter.setParamete("moneytotal DESC,ordertotal ASC");
				}else if(rankmoney.equals(2) && rankorder.equals(1)){
					parameter.setParamete("moneytotal ASC,ordertotal DESC");
				}else if(rankmoney.equals(2) && rankorder.equals(2)){
					parameter.setParamete("moneytotal ASC,ordertotal ASC");
				}
			}
			parameter.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameter.setEndTime(CommUtil.toString(maparam.get("endTime")));
			List<Map<String, Object>> statisticsData = statisticsDao.selectInfo(parameter);
			Map<String, Object> statisticstotal = statisticsDao.selectInfototal(parameter);
			page.setTotalRows(statisticsData.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameter.setPages(page.getNumPerPage());
			parameter.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> statisticsInfo = statisticsDao.selectInfo(parameter);
			datamap.put("listdata", statisticsInfo);
			datamap.put("totaldata", statisticstotal);
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			CommUtil.responseBuildInfo(200, "成功", datamap);
			
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	public Map<String, Object> dealerCollectData(Integer dealid, Integer type, String devicenum, Integer areaid, 
			String startTime, String endTime) {
		try {
			Parameters parame = new Parameters();
			parame.setUid(dealid);
			parame.setType(CommUtil.toString(type));
			parame.setCode(CommUtil.toString(devicenum));
			parame.setSource(CommUtil.toString(areaid));
			parame.setStartTime(CommUtil.toString(startTime));
			parame.setEndTime(CommUtil.toString(endTime));
			Map<String, Object> dealerCollect = codestatisticsDao.agentmoneycollect(parame);
			dealerCollect = dealerCollect == null ? new HashMap<>() : dealerCollect;
			return dealerCollect;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}
	
	public Map<String, Object> dealerTodayCollectData() {
		try {
			String startTime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
			String endTime = StringUtil.toDateTime("yyyy-MM-dd 23:59:59");
			Parameters parameters = new Parameters();
			User user = CommonConfig.getAdminReq(request);
			parameters.setDealer(user.getId().toString());
			parameters.setStartTime(startTime);
			parameters.setEndTime(endTime);
			
			Map<String, Object> collectmoney = tradeRecordDao.timingCollectMoney(parameters);
			if(collectmoney!=null){
				Integer wechatnum = CommUtil.toInteger(collectmoney.get("wechatnum"));
				Integer alipaynum = CommUtil.toInteger(collectmoney.get("alipaynum"));
				Double wechatmoney = CommUtil.toDouble(collectmoney.get("wechatmoney"));
				Double alipaymoney = CommUtil.toDouble(collectmoney.get("alipaymoney"));
				Double refwechatmoney = CommUtil.toDouble(collectmoney.get("refwechatmoney"));
				Double refalipaymoney = CommUtil.toDouble(collectmoney.get("refalipaymoney"));
				Integer ordertotal = wechatnum + alipaynum;
				Double moneytotal = wechatmoney + alipaymoney-refwechatmoney-refalipaymoney;
				collectmoney.put("ordertotal", ordertotal);
				collectmoney.put("moneytotal", moneytotal);
			}
			if(collectmoney==null) collectmoney = new HashMap<>();
			return collectmoney;
		} catch (Exception e) {
			return new HashMap<String, Object>();
		}
	}

	/**
	 * separate
	 * @Description： 数据汇总信息查询
	 * @author： origin   2019年11月1日 下午5:32:14
	 */
	@Override
	public Object dataCollectInquire(HttpServletRequest request) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			//设备信息
			User user = CommonConfig.getAdminReq(request);
			Integer userrank = CommUtil.toInteger(user.getRank());
			Integer userid = null;
			if(userrank.equals(2)) userid = CommUtil.toInteger(user.getId());
			Map<String, Object> deviceData = userEquipmentDao.selectustoequ(userid);
			Integer devicenum = CommUtil.toInteger(deviceData.get("onlines")) + CommUtil.toInteger(deviceData.get("disline"));
			deviceData.put("devicenum", devicenum);
			String validdealer = equipmentDao.selectdisequ();//计算有多少个有效商户
			resultdata.put("validdealer", validdealer);
			resultdata.put("deviceData", deviceData);
			
			//历史总订单信息
			Map<String, Object> agentdatas = dealerCollectData(userid, 2, null, null, null, null);
			
			CommUtil.responseBuildInfo(200, "成功", resultdata);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", resultdata);
		}
		//历史总订单信息
		Map<String, Object> agentdata = agentmoneycollect();
		//当天订单统计
		Map<String, Object> agentnowdata = dealerTodayCollectData();
		//累计提现金额
		Withdraw withdraw = new Withdraw();
		Map<String, Object> extractTotal = withdrawService.selectmoneysum(withdraw);//extractmoney:提现金额  sumservicecharge:手续费
		
		Double extractmoney = CommUtil.toDouble(extractTotal.get("extractmoney"));
		Double sumservicecharge = CommUtil.toDouble(extractTotal.get("sumservicecharge"));
		
		Double totalmoney = CommUtil.addBig(CommUtil.toDouble(agentdata.get("moneytotal")), CommUtil.toDouble(agentnowdata.get("moneytotal")));
		Integer totalorder = CommUtil.toInteger(agentdata.get("ordertotal")) + CommUtil.toInteger(agentnowdata.get("ordertotal"));
//		Double incomemoney = CommUtil.subBig(totalmoney, extractmoney);
		
		Double wechatmoney = CommUtil.addBig(CommUtil.toDouble(agentdata.get("wechatmoney")), CommUtil.toDouble(agentnowdata.get("wechatmoney")));
		Integer wechatorder = CommUtil.toInteger(agentdata.get("wechatorder")) + CommUtil.toInteger(agentnowdata.get("wechatnum"));
		
		Double alipaymoney = CommUtil.addBig(CommUtil.toDouble(agentdata.get("alipaymoney")), CommUtil.toDouble(agentnowdata.get("alipaymoney")));
		Integer alipayorder = CommUtil.toInteger(agentdata.get("alipayorder")) + CommUtil.toInteger(agentnowdata.get("alipaynum"));

		Double refwechatmoney = CommUtil.addBig(CommUtil.toDouble(agentdata.get("wechatretmoney")), CommUtil.toDouble(agentnowdata.get("refwechatmoney")));
		Integer refwechatorder = CommUtil.toInteger(agentdata.get("wechatretord")) + CommUtil.toInteger(agentnowdata.get("refwechatnum"));
		
		Double refalipaymoney = CommUtil.addBig(CommUtil.toDouble(agentdata.get("alipayretmoney")), CommUtil.toDouble(agentnowdata.get("refalipaymoney")));
		Integer refalipayorder = CommUtil.toInteger(agentdata.get("alipayretord")) + CommUtil.toInteger(agentnowdata.get("refalipaynum"));
		
		Double onlinemoney = CommUtil.addBig(wechatmoney, alipaymoney);
		Double refonlinemoney = CommUtil.addBig(refwechatmoney, refalipaymoney);
		Double incomemoney = CommUtil.subBig(totalmoney, refonlinemoney);
		
		resultdata.put("incomemoney", incomemoney);
		resultdata.put("onlinemoney", onlinemoney);
		resultdata.put("refonlinemoney", refonlinemoney);
		resultdata.put("totalmoney", totalmoney);
		resultdata.put("totalorder", totalorder);
		resultdata.put("wechatmoney", wechatmoney);
		resultdata.put("wechatorder", wechatorder);
		resultdata.put("alipaymoney", alipaymoney);
		resultdata.put("alipayorder", alipayorder);
		resultdata.put("refwechatmoney", refwechatmoney);
		resultdata.put("refwechatorder", refwechatorder);
		resultdata.put("refalipaymoney", refalipaymoney);
		resultdata.put("refalipayorder", refalipayorder);
		resultdata.put("extractmoney", extractmoney);
		resultdata.put("sumservicecharge", sumservicecharge);

		resultdata.put("nowdata", agentnowdata);
		resultdata.put("history", agentdata);
		
		return resultdata;
	}

	/**
	 * separate
	 * @Description： 获取昨日统计值
	 * @author： origin   2019年11月21日 下午5:23:21 
	 */
	@Override
	public Map<String, Object> statisticsData(Integer merid, Integer type, String startTime, String endTime) {
		try {
			Parameters parame = new Parameters();
			parame.setUid(merid);
			parame.setType(CommUtil.toString(type));
			parame.setStartTime(startTime);
			parame.setEndTime(endTime);
			Map<String, Object> dealerCollect = codestatisticsDao.agentmoneycollect(parame);
			dealerCollect = dealerCollect == null ? new HashMap<String, Object>() :dealerCollect;
			return dealerCollect;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}

	@Override
	public Map<String, Object> selectMerConsumData(Integer merId) {
		if(merId != null){
			Map<String,Object> map=codestatisticsDao.selectMerConsumData(merId);
			if(map != null && !map.isEmpty()){
				return map;
			}else{
				return new HashMap<String, Object>();
			}
		}else{
			return new HashMap<String, Object>();
		}
	}

	/**
	 * @Description：商户收益推送
	 * @author： origin 2020年6月13日下午6:33:18
	 * @comment:	//ORIGIN  ToUpload	商户收益推送
	 */
	@Override
	public void dealerIncomePush() {
		try {
			List<Map<String, Object>> meralluser = userEquipmentDao.inquireAllmeridInfo();//查询绑定设备的商户与未绑定设备的合伙人
			String time = CommUtil.getPastDate(1);
			for(Map<String, Object> item : meralluser){
				Integer dealid = CommUtil.toInteger(item.get("merid"));
				if(dealid>0){
					DealerAuthority deaut = dealerAuthorityDao.selectAuthority(null, dealid);
					if(deaut!=null){
						Integer incomePush = CommUtil.toInteger(deaut.getIncomemess());
						if(incomePush.equals(1)){
							Map<String, Object> merchEarning = codestatisticsDao.inquireMerchEarningDay(dealid, time);
							if(merchEarning == null){
								String begintime = time + " 00:00:00";
								String endtime = time + " 23:59:59";
								Integer code = dealerIncomeDataGain("000000", dealid, "00", 2, begintime, endtime, time);
								if(code==200) merchEarning = codestatisticsDao.inquireMerchEarningDay(dealid, time);
							}
							Double wechatmoney = CommUtil.toDouble(merchEarning.get("wechatmoney"));
							Double alipaymoney = CommUtil.toDouble(merchEarning.get("alipaymoney"));
							Double wechatretmoney = CommUtil.toDouble(merchEarning.get("refwechatmoney"));
							Double alipayretmoney = CommUtil.toDouble(merchEarning.get("refalipaymoney"));
							Double moneytotal = CommUtil.toDouble(merchEarning.get("moneytotal"));
							Double returnmoney = CommUtil.addBig(wechatretmoney, alipayretmoney);
							Double incomemoney = StringUtil.subBig(moneytotal, returnmoney);
							String dealernick = CommUtil.toString(merchEarning.get("dealernick"));
							String dealerreal = CommUtil.toString(merchEarning.get("dealerreal"));
							String phone = CommUtil.toString(merchEarning.get("dealerphone"));
							String oppenid = CommUtil.toString(merchEarning.get("openid"));
							String earningMoney = CommUtil.toString(merchEarning.get("earnings"));
							String timedata = CommUtil.toString(merchEarning.get("count_time"));
							TempMsgUtil.dealerIncomePush( oppenid, dealernick, dealerreal, incomemoney, earningMoney, timedata);
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description: 查询昨日设备收益记录
	 * @Author: origin  创建时间:2020年8月29日 下午5:16:35
	 * @common:   
	 */
	@Override
	public List<Map<String, Object>> inquireDeviceIncomeInfo(HttpServletRequest request) {
		try {
			String beginTime = DisposeUtil.getPastDate(1,1);
			String endTime = DisposeUtil.getPastDate(1,1);
			Parameters parame = new Parameters();
			User user = CommonConfig.getAdminReq(request);
			Integer userrank = user.getRank();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
			if(userrank!=0) parame.setUid(user.getId());//获取用户
			parame.setParamete("c.moneytotal DESC");
			parame.setRemark("00,01,02,05,06,08");
			parame.setStartTime(beginTime);
			parame.setEndTime(endTime);
			parame.setPages(8);
			parame.setStartnumber(0);
			List<Map<String, Object>> result = CommUtil.isListMapEmpty(codestatisticsDao.getCodeEarnings(parame));
			List<Map<String, Object>> resultdata = new ArrayList<Map<String,Object>>();
			for(Map<String, Object> item : result){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", CommUtil.toInteger(item.get("code")));
				map.put("value", CommUtil.toDouble(item.get("moneytotal")));
				resultdata.add(map);
			}
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.isListMapEmpty(null);
		}
	}

	/**
	 * @Description: 查询最近十五天交易金额
	 * @Author: origin  创建时间:2020年8月29日 下午5:16:35
	 * @common:   
	 */
	@Override
	public List<Map<String, Object>> inquirePlatformIncomeInfo(HttpServletRequest request) {
		try {
			String beginTime = DisposeUtil.getPastDate(15,1);
			String endTime = DisposeUtil.getPastDate(0,1);
			Parameters parame = new Parameters();
			parame.setParamete("count_time ASC");
			parame.setStartTime(beginTime);
			parame.setEndTime(endTime);
//			parame.setPages(8);
//			parame.setStartnumber(0);
			List<Map<String, Object>> result = statisticsDao.selectInfo(parame);
			//List<Map<String, Object>> result = codestatisticsDao.getcodestatistics(parame);
			List<Map<String, Object>> resultdata = new ArrayList<Map<String,Object>>();
			for(Map<String, Object> item : result){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ordertotal", CommUtil.toInteger(item.get("ordertotal")));
				map.put("countTime", CommUtil.toString(item.get("count_time")));
				map.put("moneytotal", CommUtil.toDouble(item.get("moneytotal")));
				resultdata.add(map);
			}
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.isListMapEmpty(null);
		}
	}

}

// === 前后段分离处理数据信息 结束  =================================================================================
/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	


