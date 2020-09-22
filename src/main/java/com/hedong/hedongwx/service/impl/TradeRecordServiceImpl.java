package com.hedong.hedongwx.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.AreaDao;
import com.hedong.hedongwx.dao.ChargeRecordDao;
import com.hedong.hedongwx.dao.EquipmentDao;
import com.hedong.hedongwx.dao.InCoinsDao;
import com.hedong.hedongwx.dao.PackageMonthDao;
import com.hedong.hedongwx.dao.TemplateSonDao;
import com.hedong.hedongwx.dao.TradeRecordDao;
import com.hedong.hedongwx.dao.UserEquipmentDao;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.InCoins;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.PackageMonthRecord;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.SendMsgUtil;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.WeiXinConfigParam;
import com.hedong.hedongwx.utils.yinlian.SignUtils;
import com.hedong.hedongwx.utils.yinlian.XmlUtils;
import com.hedong.hedongwx.web.controller.MyWebSocketServer;

@Service
public class TradeRecordServiceImpl implements TradeRecordService {
	private final Logger logger = LoggerFactory.getLogger(TradeRecordServiceImpl.class);
	@Autowired
	private TradeRecordDao tradeRecordDao;
	@Autowired
	private PackageMonthDao packageMonthDao;
	@Autowired
	private TemplateSonDao templateSonDao; 
	@Autowired
	private UserEquipmentDao userEquipmentDao;
	@Autowired
	private InCoinsDao inCoinsDao;
	@Autowired
	private InCoinsService inCoinsService;
	@Autowired
	private EquipmentDao equipmentDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private MerchantDetailService merchantDetailService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private UserService userService;
	@Autowired
	private ChargeRecordDao chargeRecordDao;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private MyWebSocketServer myWebSocketServer;
	
	@Override
	public int insertTradeRecord(TradeRecord tradeRecord) {
		return tradeRecordDao.insertTradeRecord(tradeRecord);
	}
	
	@Override
	public int insertTrade(Integer merid, Integer uid, String ordernum, Double money, Double mermoney,
			String code, Integer paysource, Integer paytype, Integer status, String hardver) {
		TradeRecord tradeRecord = new TradeRecord();
		tradeRecord.setMerid(merid);
		tradeRecord.setUid(uid);
		tradeRecord.setOrdernum(ordernum);
		tradeRecord.setMoney(money);
		tradeRecord.setMermoney(mermoney);
		tradeRecord.setCode(code);
		tradeRecord.setPaysource(paysource);
		tradeRecord.setPaytype(paytype);
		tradeRecord.setStatus(status);
		tradeRecord.setHardver(hardver);
		tradeRecord.setCreateTime(new Date());
		
		if (status == 1) {
			myWebSocketServer.wolfSendOrder(code, paysource, paytype, ordernum, money);
		}
		
		return tradeRecordDao.insertTradeRecord(tradeRecord);
	}
	
	@Override
	public int insertToTrade(Integer merid, Integer manid, Integer uid, String ordernum, Double money, Double mermoney, 
			Double manmoney, String code, Integer paysource, Integer paytype, Integer status, String hardver) {
		TradeRecord tradeRecord = new TradeRecord();
		tradeRecord.setMerid(merid);
		tradeRecord.setUid(uid);
		tradeRecord.setManid(manid);
		tradeRecord.setOrdernum(ordernum);
		tradeRecord.setMoney(money);
		tradeRecord.setMermoney(mermoney);
		tradeRecord.setManmoney(manmoney);
		tradeRecord.setCode(code);
		tradeRecord.setPaysource(paysource);
		tradeRecord.setPaytype(paytype);
		tradeRecord.setStatus(status);
		tradeRecord.setHardver(hardver);
		tradeRecord.setCreateTime(new Date());
		
		if (status == 1) {
			myWebSocketServer.wolfSendOrder(code, paysource, paytype, ordernum, money);
		}

		return tradeRecordDao.insertToTradeRecord(tradeRecord);
	}

	@Override
	public Integer insertToTrade(Integer merid, Integer manid, Integer uid, String ordernum, Double money, Double mermoney, 
	Double manmoney, String code, Integer paysource, Integer paytype, Integer status, String hardver, String comment) {
		TradeRecord tradeRecord = new TradeRecord();
		tradeRecord.setMerid(merid);
		tradeRecord.setUid(uid);
		tradeRecord.setManid(manid);
		tradeRecord.setOrdernum(ordernum);
		tradeRecord.setMoney(money);
		tradeRecord.setMermoney(mermoney);
		tradeRecord.setManmoney(manmoney);
		tradeRecord.setCode(code);
		tradeRecord.setPaysource(paysource);
		tradeRecord.setPaytype(paytype);
		tradeRecord.setStatus(status);
		tradeRecord.setHardver(hardver);
		tradeRecord.setCreateTime(new Date());
		tradeRecord.setComment(comment);
		
		if (status == 1) {
			myWebSocketServer.wolfSendOrder(code, paysource, paytype, ordernum, money);
		}
		
		return tradeRecordDao.insertToTradeRecord(tradeRecord);
	}
	//根据设备号查询订单
	@Override
	public List<Map<String, Object>> seleTradeToWechat(Parameters parame) {
		List<Map<String, Object>> codeorder = tradeRecordDao.seleTradeToWechat(parame);
		return codeorder;
	}
	
	@Override
	public PageUtils<Parameters> selectTradeRecord(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(null==startTime || startTime.equals(""))startTime = StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss", StringUtil.toDateTime(), 7);
		if(null==endTime || endTime.equals(""))endTime = StringUtil.getCurrentDateTime();
		
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		User user = CommonConfig.getAdminReq(request);
		Integer rank = user.getRank();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(rank!=0) parameters.setUid(user.getId());//获取用户
		parameters.setMobile(CommUtil.toString(request.getParameter("mobile")));
		parameters.setOrder(request.getParameter("ordernum"));
		parameters.setNickname(request.getParameter("username"));
		parameters.setDealer(request.getParameter("dealer"));
		parameters.setCode(request.getParameter("code"));
		parameters.setState(request.getParameter("status"));//订单状态
		parameters.setType(request.getParameter("paytype"));//支付方式
		String paysource = request.getParameter("paysource");//交易类型
		if(!"0".equals(paysource) && null!=paysource){
			parameters.setSource(paysource);
		}else if("1".equals(paysource)){
			parameters.setSource("1,2");
		}
		parameters.setStartTime(startTime);
		parameters.setEndTime(endTime);
		List<Map<String, Object>> trade = tradeRecordDao.selectTradeRecord(parameters);
		Double walletmoney = 0.00;
		Double incomemoney = 0.00;
		Double totalmoney = 0.00;
		
		Double refwalletmoney = 0.00;
		Double reftotalmoney = 0.00;
		for(Map<String, Object> item : trade){
			Integer status = CommUtil.toInteger(item.get("status"));
			Integer type = CommUtil.toInteger(item.get("paytype"));
			Double money = CommUtil.toDouble(item.get("money"));
			if(status==1){
				if(type==1){
					walletmoney = CommUtil.addBig(walletmoney, money);
				}else if(type==2 || type ==3 || type ==4){
					totalmoney = CommUtil.addBig(totalmoney, money);
				}
			}else if(status==2){
				if(type==1){
					refwalletmoney = CommUtil.addBig(refwalletmoney, money);
				}else if(type==2 || type ==3 || type ==4){
					reftotalmoney = CommUtil.addBig(reftotalmoney, money);
				}
			}
		}
		incomemoney = CommUtil.subBig(totalmoney, reftotalmoney);
		Map<String, Object> totalmap = new HashMap<>();
		totalmap.put("walletmoney", walletmoney);
		totalmap.put("refwalletmoney", refwalletmoney);

		totalmap.put("totalmoney", totalmoney);
		totalmap.put("reftotalmoney", reftotalmoney);
		totalmap.put("incomemoney", incomemoney);
		page.setMap(totalmap);
		page.setTotalRows(trade.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		List<Map<String, Object>> tradepage = tradeRecordDao.selectTradeRecord(parameters);
		page.setListMap(tradepage);
		return page;
	}

	//在交易表中查询投币记录查询
	@Override
	public PageUtils<Parameters> selectCoinsInfoByTr(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(null==startTime || startTime.equals(""))startTime = StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss", StringUtil.toDateTime(), 7);
		if(null==endTime || endTime.equals(""))endTime = StringUtil.getCurrentDateTime();
		parameters.setStartTime(startTime);
		parameters.setEndTime(endTime);
		User user = CommonConfig.getAdminReq(request);
		Integer rank = user.getRank();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(rank!=0) parameters.setUid(user.getId());//获取用户
		parameters.setSource("2");
		parameters.setType("4");
		parameters.setOrder(request.getParameter("ordernum"));
		parameters.setNickname(request.getParameter("username"));
		parameters.setDealer(request.getParameter("dealer"));
		parameters.setMobile(request.getParameter("mobile"));
		parameters.setCode(request.getParameter("cardnum"));
		parameters.setNumber(request.getParameter("code"));
		Integer count = tradeRecordDao.selectTradeRecordCount(parameters);
		page.setTotalRows(count);
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		List<Map<String, Object>> tradepage = tradeRecordDao.selectTradeRecord(parameters);
		page.setListMap(tradepage);
		return page;
	}

	/**
	 * 根据条件查询交易记录信息
	 */
	@Override
	public TradeRecord getTraderecord(String ordernum) {
		TradeRecord trade = new TradeRecord();
		trade.setOrdernum(ordernum);
		return tradeRecordDao.getTraderecordList(trade);
	}
	
	@Override
	public TradeRecord getTraderecordList(TradeRecord trade) {
		return tradeRecordDao.getTraderecordList(trade);
	}

	/**
	 * 查询交易记录数据与商户、合伙人和详细
	 */
	@Override
	public Map<String, Object> selectTradeDetails(String ordernum, Integer status) {
		Map<String, Object> keyobj = new HashMap<>();
		Parameters  parameter = new Parameters();
		parameter.setOrder(ordernum);
		parameter.setStatus(status.toString());
		List<Map<String, Object>> trade = tradeRecordDao.selectTradeDetails(parameter);
		trade = CommUtil.isListMapEmpty(trade);
		for(Map<String, Object> item : trade){
			String statusitem = CommUtil.toString(item.get("status"));
			if(statusitem.equals(status)){
				keyobj = item;
			}
		}
		return keyobj;
	}

	/**
	 * @Description： 根据条件查询交易数据
	 * @author： origin
	 */
	@Override
	public List<Map<String, Object>> tradeDetailsinquire(Parameters parameter) {
		return tradeRecordDao.selectTradeRecord(parameter);
	}

	/**
	 * @Description： 交易记录查询用户消费信息
	 * @author： origin          
	 * 创建时间：   2019年5月30日 下午5:04:46
	 */
	@Override
	public List<Map<String, Object>> touristorder( Integer merid, String beginTime, String endTime, Integer uid, Integer offset) {
		Parameters  parame = new Parameters();
		parame.setUid(merid);
		parame.setState(uid.toString());
		parame.setStartTime(beginTime);
		parame.setEndTime(endTime);
		//parame.setStartnumber(offset);
		//parame.setPages(CommonConfig.numerical);
		List<Map<String, Object>> trade = tradeRecordDao.selectTradeDetails(parame);
		return trade;
	}

	@Override
	public Object newestOrder(Integer traid, Integer merid) {
		
		Parameters parame = new Parameters();
		if(merid!=null) parame.setUid(merid);
		parame.setRank(traid.toString());
//		parame.setStartnumber(0);
//		parame.setPages(10);
		List<Map<String, Object>>  trade = tradeRecordDao.selectTradeRecord(parame);
		return trade;
	}

	@Override
	public PageUtils<Parameters> selectPackageMonth(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		
		String startTime = CommUtil.toString(request.getParameter("startTime"));
		String endTime = CommUtil.toString(request.getParameter("endTime"));
		startTime = startTime==null ? StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss",  StringUtil.toDateTime(), 7) : startTime;
		endTime = endTime==null ? StringUtil.getCurrentDateTime() : endTime;
		String nickname = CommUtil.toString(request.getParameter("nickname"));
		String order = CommUtil.toString(request.getParameter("order"));
		
		User user = CommonConfig.getAdminReq(request);
		Integer rank = user.getRank();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		Parameters  parame = new Parameters();
		if(rank!=0) parame.setDealer(user.getId().toString());//获取用户
		parame.setNickname(nickname);
		parame.setOrder(order);
		
		parame.setStartTime(startTime);
		parame.setEndTime(endTime);
		
		List<Map<String, Object>> pacmonth = packageMonthDao.selectMonthRecordByParam(parame);
		page.setTotalRows(pacmonth.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parame.setPages(page.getNumPerPage());
		parame.setStartnumber(page.getStartIndex());
		List<Map<String, Object>> tpacmonth = packageMonthDao.selectMonthRecordByParam(parame);
		page.setListMap(tpacmonth);
		return page;
	}

	@Override
	public List<PackageMonthRecord> selectMonthRecordEntiy(String ordernum, Integer status) {
		PackageMonthRecord packmonthrec = new PackageMonthRecord();
		packmonthrec.setOrdernum(ordernum);
		packmonthrec.setStatus(status);
		List<PackageMonthRecord> packmoth = packageMonthDao.selectMonthRecordByEntiy(packmonthrec);
		return packmoth;
	}
	
	@Override
	public TradeRecord selectTradeById(Integer orderid) {
		try {
			TradeRecord trade =  new TradeRecord();
			trade.setId(orderid);
			TradeRecord tradedata = tradeRecordDao.getTraderecordList(trade);
			return tradedata = tradedata == null ? trade : tradedata;
		} catch (Exception e) {
			e.printStackTrace();
			return new TradeRecord();
		}
	}

	@Override
	public List<TradeRecord> selectPartByParam(Integer merid, String ordernum, String code, Integer paytype, 
			Integer status, String startTime, String endTime) {
		try {
			Parameters parame = new Parameters();
			parame.setDealer(merid.toString());
			parame.setOrder(ordernum);
			parame.setCode(code);
			String type = null;
			paytype = CommUtil.toInteger(paytype);
			if(paytype==1){
				type = "1,6";
			}else if(paytype==2){
				type = "2,4";
			}else if(paytype==3){
				type = "3,5";
			}else if(paytype == 4){
				type = "12,13";
			}
			parame.setType(type);
			parame.setStatus(CommUtil.toString(status));
			if(startTime==null && endTime==null){
				parame.setStartnumber(0);
				parame.setPages(20);
			}else{
				parame.setStartTime(startTime);
				parame.setEndTime(endTime);
			}
			List<TradeRecord> tralist = tradeRecordDao.selectPartByParam(parame);
			//List<TradeRecord> tralist = tradeRecordDao.selectPartByParam(merid, ordernum, code, paytype, status, startTime, endTime);
			
			return tralist == null ? new ArrayList<TradeRecord>() : tralist;
		} catch (Exception e) {
			e.printStackTrace();
			return  new ArrayList<TradeRecord>();
		}
	}

	
	@Override
	public List<TradeRecord> getTraderecordList(String ordernum, Integer status) {
		List<TradeRecord> tralist = null;
		try {
			TradeRecord trade = new TradeRecord();
			trade.setOrdernum(ordernum);
			trade.setStatus(status);
			tralist = tradeRecordDao.getTradeRecordListInfo(trade);
		} catch (Exception e) {
			e.printStackTrace();
			tralist = new ArrayList<>();
		}
		return tralist;
	}

	//=== 前后分离 ===============================================================================================
	/**
	 * separate
	 * @Description： 查询交易记录信息
	 * @author： origin 
	 */
	@Override
	public Object tradeRecordData(HttpServletRequest request) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			// 获取用户登陆信息
			User user = CommonConfig.getAdminReq(request);
			if(user == null){
				dataMap.put("result", "请先登陆");
				return CommUtil.responseBuildInfo(301, "异常错误", dataMap);
			}
			//前端传递代理商名下某一个商家的id
			Integer agentSelectmerid =  CommUtil.toInteger(maparam.get("agentSelectmerid"));
			if(agentSelectmerid != null && !agentSelectmerid.equals(0)){
				user = new User();
				user.setRank(2);
				user.setId(agentSelectmerid);
			}
			// 参数
			Parameters parameters = new Parameters();
			Integer rank = CommUtil.toInteger(user.getRank());
			// 绑定id
			if(!rank.equals(0)) parameters.setUid(user.getId());
			// 设置参数
			parameters.setOrder(CommUtil.toString(maparam.get("ordernum")));
			parameters.setNickname(CommUtil.toString(maparam.get("usernick")));
			parameters.setDealer(CommUtil.toString(maparam.get("dealer")));
			parameters.setMobile(CommUtil.toString(maparam.get("mobile")));
			parameters.setCode(CommUtil.toString(maparam.get("code")));
			// 订单状态 1正常 2 退款
			parameters.setState(CommUtil.toString(maparam.get("status")));
			// 支付方式1、钱包2、微信3、支付宝 、4微信小程序、5支付宝小程序 6虚拟充值 ,12银联支付
			parameters.setType(CommUtil.toString(maparam.get("paytype")));
			// 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、钱包-5、在线卡-6、包月支付（显示为虚拟充值）
			Integer paysource = CommUtil.toInteger(maparam.get("paysource"));
			if(!paysource.equals(0)){
				parameters.setSource(paysource.toString());
			}
			// 设置开始和结束时间
			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
			Integer recordId = 0;
			Integer tradeRecordCount = 0;
			// 第一次查询分页或条件查询时,需要总条数和汇总信息
			if(currentPage == 1 || currentPage == 0){
				// 查询满足条件的第一个主键
				recordId = CommUtil.toInteger(tradeRecordDao.selectTradeId(parameters));
				if (recordId == 0){
					logger.info("商家一段时间内没有交易记录");
					dataMap.put("result", "此时间端内没有交易记录");
					return CommUtil.responseBuildInfo(301, "异常错误", dataMap);
				} 
				// 设置交易主键ID
				parameters.setId(recordId);
				// 查询满足条件的总条数
				tradeRecordCount = tradeRecordDao.selectTradeCount(parameters);
				if (tradeRecordCount == 0){
					logger.info("商家一段时间内没有交易记录");
					dataMap.put("result", "此时间端内没有交易记录");
					return CommUtil.responseBuildInfo(301, "异常错误", dataMap);
				} 
				Map<String, Object> collectData = tradeRecordDao.selectMerTradeCollect( parameters );
				// 查询满足条件的汇总数据
				logger.info("查询的汇总数据===="+collectData.isEmpty());
				Double walletMoney = CommUtil.toDouble( collectData.get( "walletmoney" ) );
				Double totalMoney = CommUtil.toDouble( collectData.get( "totalmoney" ) );
				Double refWalletMoney = CommUtil.toDouble( collectData.get( "refwalletmoney" ) );
				Double refTotalMoney = CommUtil.toDouble( collectData.get( "reftotalmoney" ) );
				Double incomeMoney = CommUtil.subBig(totalMoney, refTotalMoney);
				// 组装汇总数据
				Map<String, Object> totalmap = new HashMap<>();
				totalmap.put("walletmoney", walletMoney);
				totalmap.put("refwalletmoney", refWalletMoney);
				totalmap.put("totalmoney", totalMoney);
				totalmap.put("reftotalmoney", refTotalMoney);
				totalmap.put("incomemoney", incomeMoney);
				// 设置分页信息
				page.setMap(totalmap);
				page.setTotalRows(tradeRecordCount);
				page.setTotalPages();
				page.setStart();
				page.setEnd();
				parameters.setPages(page.getNumPerPage());
				parameters.setStartnumber(page.getStartIndex());
				// 分页查询
				List<Map<String, Object>> tradeinfo = tradeRecordDao.selectTradeRecordA(parameters);
				dataMap.put("listdata", CommUtil.isListMapEmpty(tradeinfo));
				dataMap.put("mapdata", totalmap);
				dataMap.put("totalRows", page.getTotalRows());
				dataMap.put("totalPages", page.getTotalPages());
				dataMap.put("currentPage", page.getCurrentPage());
				CommUtil.responseBuildInfo(200, "成功", dataMap);
				return dataMap;
			}else if(currentPage > 1){
				parameters.setPages(page.getNumPerPage());
				parameters.setStartnumber(page.getStartIndex());
				List<Map<String, Object>> tradeinfo = tradeRecordDao.selectTradeRecordA(parameters);
				dataMap.put("listdata", CommUtil.isListMapEmpty(tradeinfo));
				CommUtil.responseBuildInfo(200, "成功", dataMap);
				return dataMap;
			}
//			List<Map<String, Object>> trade = tradeRecordDao.selectTradeRecord(parameters);
//			Double walletmoney = 0.00;
//			Double incomemoney = 0.00;
//			Double totalmoney = 0.00;
//			Double refwalletmoney = 0.00;
//			Double reftotalmoney = 0.00;
//			for(Map<String, Object> item : trade){
//				Integer status = CommUtil.toInteger(item.get("status"));
//				Integer type = CommUtil.toInteger(item.get("paytype"));
//				Double money = CommUtil.toDouble(item.get("money"));
//				if(status==1){//正常
//					if(type==1){
//						walletmoney = CommUtil.addBig(walletmoney, money);
//					}else if(type==2 || type ==3 || type ==4 || type ==5 || type ==12){
//						totalmoney = CommUtil.addBig(totalmoney, money);
//					}
//				}else if(status==2){//退款
//					if(type==1){
//						refwalletmoney = CommUtil.addBig(refwalletmoney, money);
//					}else if(type==2 || type ==3 || type ==4 || type ==5 || type ==12){
//						reftotalmoney = CommUtil.addBig(reftotalmoney, money);
//					}
//				}
//			}
//			incomemoney = CommUtil.subBig(totalmoney, reftotalmoney);
//			Map<String, Object> totalmap = new HashMap<>();
//			totalmap.put("walletmoney", walletmoney);
//			totalmap.put("refwalletmoney", refwalletmoney);
//
//			totalmap.put("totalmoney", totalmoney);
//			totalmap.put("reftotalmoney", reftotalmoney);
//			totalmap.put("incomemoney", incomemoney);
//			page.setMap(totalmap);
//			page.setTotalRows(trade.size());
//			page.setTotalPages();
//			page.setStart();
//			page.setEnd();
//			parameters.setPages(page.getNumPerPage());
//			parameters.setStartnumber(page.getStartIndex());
//			List<Map<String, Object>> tradeinfo = tradeRecordDao.selectTradeRecord(parameters);
//			Integer isAlllData = CommUtil.toInteger(maparam.get("isAlllData"));
//			if(isAlllData.equals(1)){
//				dataMap.put("listdata", CommUtil.isListMapEmpty(trade));
//				Integer datasize = CommUtil.toInteger(trade.size());
//				if(datasize>10000){
//					return CommUtil.responseBuildInfo(201, "当前条数为"+datasize+"条，超过最大导出条数（10000条），请缩短查询时间", dataMap);
//				}
//			}else{
//				dataMap.put("listdata", CommUtil.isListMapEmpty(tradeinfo));
//			}
//			
//			dataMap.put("totalRows", page.getTotalRows());
//			dataMap.put("totalPages", page.getTotalPages());
//			dataMap.put("currentPage", page.getCurrentPage());
//			CommUtil.responseBuildInfo(200, "成功", dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", dataMap);
		}
		return dataMap;
		
	}

	@Override
	public int updateTradeMoney(Double money, String ordernum) {
		return tradeRecordDao.updateTradeMoney(money, ordernum);
	}

	@Override
	public Integer updateTradeOrderInfo(String ordernum, Integer traid) {
		return tradeRecordDao.updateTradeOrderInfo(ordernum, traid);
	}

	/**
	 * @Description：
	 * @author： origin 2020年5月30日下午5:05:35
	 * @comment:
	 */
	@Override
	public List<String> getTraderecordOrder(Integer status, String bigintime, String endtime) {
		List<String> result = tradeRecordDao.getTraderecordOrder(status, bigintime, endtime);
		return  result == null ? new ArrayList<>() : result;
	}

	@Override
	public Map<String, Object> createUnioPayOrderNum(HttpServletRequest req) {
		//银联用户ID
		String unionUserId = CommUtil.toString(req.getParameter("unionUserId"));
		String equCode = CommUtil.toString(req.getParameter("code"));
		Integer merUserId = CommUtil.toInteger(req.getParameter("merUserId"));
		Integer tempId = CommUtil.toInteger(req.getParameter("tempId"));
		if(merUserId == 0 || tempId == 0) {
			return CommUtil.responseBuild(400, "失败", "商户错误");
		}
		//根据模板ID查询模板信息
		TemplateSon templateSon = templateSonDao.getInfoTemplateOne(tempId);
		if(templateSon == null || templateSon.getMoney()==0.00){
			return CommUtil.responseBuild(400, "失败", "模板错误");
		} 
		if(unionUserId == null){
			return CommUtil.responseBuild(400, "失败", "银联用户错误");
		} 
		if(equCode == null || "".equals(equCode) || equCode.length() != 6){
			return CommUtil.responseBuild(400, "失败", "设备号错误");
		}
		// 查询设备信息
		Equipment equipment = equipmentDao.getEquipmentById(equCode);
		if(equipment == null) return CommUtil.responseBuild(400, "失败", "设备型号错误");
		String handVersion = CommUtil.toString(equipment.getHardversion());
		// 如何区分(十路,脉冲投币,离线卡,在线卡充值)根据设备号区分,在线卡,离线卡依靠前端传值区分
		Double money = templateSon.getMoney()*100;
		Double remark = templateSon.getRemark();
		String orderNum = HttpRequest.createOrdernum(6);
		SortedMap<String, String> params = new TreeMap<>();
		//商品描述
		params.put("body", "扫码充电");
		//根据设备型号区分订单类型(离线卡,在线卡,钱包充值)
		params.put("attach", handVersion);
		//接口类型：pay.unionpay.jspay
		params.put("service","pay.unionpay.jspay");
		//可选值 UTF-8 ，默认为 UTF-8
		params.put("charset","UTF-8");
		//签名类型，取值：MD5默认：MD5
		params.put("sign_type","MD5");
		//商户号，由平台分配 QRA507157343N6D
		params.put("mch_id", WeiXinConfigParam.UNIONPAYMERCHID);
		//商户订单号
		params.put("out_trade_no", orderNum);
		//银联用户ID
		params.put("user_id", unionUserId);
		//用户的外网ip，需要与访问银联支付页面的ip一致，银联会进行校验
		params.put("customer_ip", req.getRemoteAddr());
		//付款的钱
		params.put("total_fee",money.intValue()+"");
		//订单生成的机器 IP
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
			String hostname = addr.getHostName();
			params.put("mch_create_ip", hostname);
		} catch (UnknownHostException e) {
			logger.info("获取本机Ip地址错误");
			e.printStackTrace();
		}
		//回调地址
		params.put("notify_url", CommonConfig.ZIZHUCHARGE+"/unionpay/unionPayBack");
		//随机字符串
		params.put("nonce_str", HttpRequest.createOrdernum(10));
		//版本号，version默认值是2.0
		params.put("version", "2.0");
		Map<String,String> data = SignUtils.sendPostToUnionpay(params);
		// 1、下单时，需要传入获取的用户标识和用户客户端的真实 ip 地址（银联将对此进行安全控制）。
	    // 2、下单成功拿到 pay_url,后端直接 301 重定向返回给浏览器，即可拉起对应 APP 的支付控件完成支付。
		if("0".equals(data.get("status")) && "0".equals(data.get("result_code"))){
			String pay_url = CommUtil.toString(data.get("pay_url"));
			//UserEquipment userEquipment = userEquipmentDao.getUserEquipmentByCode(equCode);
			// 根据不同的型号设备生成不同的订单
			if("01".equals(handVersion) ||"00".equals(handVersion)){
				// 端口号
				Integer port = CommUtil.toInteger(req.getParameter("portChoose"));
				ChargeRecord chargeRecord = new ChargeRecord();
				chargeRecord.setMerchantid(merUserId);
				chargeRecord.setEquipmentnum(equCode);
				chargeRecord.setPort(port);
				chargeRecord.setOrdernum(orderNum);
				chargeRecord.setExpenditure(templateSon.getMoney());
				chargeRecord.setDurationtime(templateSon.getChargeTime().toString());
				chargeRecord.setQuantity(templateSon.getChargeQuantity());
				chargeRecord.setPaytype(ChargeRecord.UNION);
				chargeRecord.setStatus(0);
				chargeRecord.setUid(0);
				chargeRecordDao.insetRecord(chargeRecord);
				return CommUtil.responseBuild(200, "成功", pay_url);
			}else if("03".equals(handVersion)){
				Byte coinNum = null;
				//模板中的投币数
				if(remark != null){
					coinNum = Byte.parseByte(StringUtil.doubleToString(remark));
				} 
				InCoins inCoins = new InCoins();
				inCoins.setMerchantid(merUserId);
				inCoins.setUid(0);
				inCoins.setOrdernum(orderNum);
				inCoins.setEquipmentnum(equCode);
				inCoins.setPort((byte)1);
				inCoins.setMoney(templateSon.getMoney());
				inCoins.setCoinNum(coinNum);
				inCoins.setStatus((byte)0);
				inCoins.setHandletype((byte)12);
				inCoins.setRecycletype((byte) 0);
				inCoinsDao.insertInCoinsRecord(inCoins);
				return CommUtil.responseBuild(200, "成功", pay_url);
			}else{
				return CommUtil.responseBuild(400, "失败", "设备类型错误");
			}
		}else{
			return CommUtil.responseBuild(400, "失败", "银联订单错误");
		}
	}

	/**
	 * 处理银联付款成功后的回调逻辑
	 * @param orderNum 平台订单号
	 * @return {@link String}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String unionPayBack(HttpServletRequest request){
		// 接受回调的结果转Map
		String notifyData = "";
		Map<String, String> mapData = new HashMap<>();
		try {
			notifyData = new String(IOUtils.toByteArray(request.getInputStream()),"UTF-8");
			if(notifyData == null || "".equals(notifyData)){
				return "fail";
			}
			mapData = XmlUtils.toMap(notifyData.getBytes(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "fail";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		logger.info("回调的状态===="+mapData.get("status")+"回调的结果===="+mapData.get("result_code"));
		// 判断返回结果
		if("0".equals(mapData.get("status")) && "0".equals(mapData.get("result_code"))){
			// 根据付款中attach附加信息区分设备类型	
			String hardVersion = mapData.get("attach");
			String orderNum = mapData.get("out_trade_no");
			if(hardVersion == null || orderNum == null) return "fail";
			// 十路智慧款设备付款
			if("01".equals(hardVersion)){
				// 根据充电订单号查询预订单
				Map<String, Object> map = chargeRecordDao.selectRecordByOrderNum(orderNum);
				if(map == null) return "fail";
				//Integer chargeId = CommUtil.toInteger(map.get("id"));
				String code = CommUtil.toString(map.get("equipmentnum"));
				//Integer port = CommUtil.toInteger(map.get("port"));
				Integer merId = CommUtil.toInteger(map.get("merchantid"));
				Double payMoney = CommUtil.toDouble(map.get("expenditure"));
				Integer paySource = MerchantDetail.CHARGESOURCE;
				Integer payType = 12;
				Integer payStatus = MerchantDetail.NORMAL;
				Integer type = 1;
				// 更新订单的状态
				logger.info("更新十路智慧款预订单的状态");
				chargeRecordDao.updateRecoderStatus(orderNum);
				// 获取设备所在小区信息
				Equipment equipment = equipmentDao.getEquipmentById(code);
				Integer aId = CommUtil.toInteger(equipment.getAid());
				List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
				// 设备不存在小区就没有分成
				Double merMoney = 0.00;
				Double partMoney = 0.00;
				if(aId != 0){
					Parameters parame = new Parameters();
					parame.setState(CommUtil.toString(aId));
					parame.setType(CommUtil.toString(2));
					parame.setSort("ORDER BY av.sort");
					// 查询合伙人信息
					partInfo = CommUtil.isListMapEmpty(areaDao.selectaRearelInfo(parame));
				}
				logger.info("十路智慧款计算分成==========================");
				// 分成计算
				Map<String, Object> percentmap = StringUtil.percentCalculateDispose(partInfo, merId, payMoney);
				partMoney = CommUtil.toDouble(percentmap.get("partmoney"));
				merMoney = CommUtil.toDouble(percentmap.get("mermoney"));
				// 分成信息转成JSON
				String percentresult = CommUtil.toString(JSON.toJSON(percentmap.get("percentinfo")));
				// 交易记录数据
				TradeRecord tradeRecord = new TradeRecord();
				tradeRecord.setMerid(merId);
				tradeRecord.setUid(0);
				tradeRecord.setManid(aId);
				tradeRecord.setOrdernum(orderNum);
				tradeRecord.setMoney(payMoney);
				tradeRecord.setMermoney(merMoney);
				tradeRecord.setManmoney(partMoney);
				tradeRecord.setCode(code);
				tradeRecord.setPaysource(1);
				tradeRecord.setPaytype(payType);
				tradeRecord.setStatus(payStatus);
				tradeRecord.setHardver(hardVersion);
				tradeRecord.setCreateTime(new Date());
				tradeRecord.setComment(percentresult);
				// 添加交易信息
				tradeRecordDao.insertToTradeRecord(tradeRecord);
				logger.info("十路智慧款添加交易记录==========================");
				List<Map<String, Object>> percentinfo = (List<Map<String, Object>>) percentmap.get("percentinfo");
				//更改用户的收益
				for(Map<String, Object> item : percentinfo){
					Integer mercid = CommUtil.toInteger(item.get("merid"));
					Integer partid = CommUtil.toInteger(item.get("partid"));
					Double moneyA = CommUtil.toDouble(item.get("money"));
					if(mercid.equals(0)) mercid = partid;
					// 商家明细 payType = 4
					merchantDetailService.merEearningCalculate(mercid, moneyA, type, orderNum, paySource, 4, payStatus);
					logger.info("十路智慧款添加商家明细记录--------------------------------");
				}
				// 更改小区的收益
				areaService.addAPartEarn(code, payMoney, merId, aId, type, payStatus);
				logger.info("十路智慧款添加小区交易记录--------------------------------");
				return "success";
			}else if("03".equals(hardVersion)){
				// 脉冲扫码付款
				InCoins inCoins = inCoinsDao.selectInCoinsRecordByOrdernum(orderNum);
				logger.info("查询投币预订单"+(inCoins == null));
				logger.info("查询投币预状态"+(inCoins.getStatus() == 0));
				// 处理脉冲投币的逻辑
				if(inCoins != null || inCoins.getStatus() == 0){
					String code = inCoins.getEquipmentnum();
					String money = String.valueOf(inCoins.getMoney());
					Integer merId = CommUtil.toInteger(inCoins.getMerchantid());
					Double payMoney = CommUtil.toDouble(inCoins.getMoney());
					Integer uId = CommUtil.toInteger(inCoins.getUid());
					Integer paySource = MerchantDetail.INCOINSSOURCE;
					Integer payType = MerchantDetail.UNIONPAY;
					Integer payStatus = MerchantDetail.NORMAL;
					Integer type = 1;
					int idx = money.lastIndexOf(".");
					String totalFee = money.substring(0, idx);
					// 发送指令
					SendMsgUtil.send_0x83(inCoins.getEquipmentnum(), inCoins.getPort(), Byte.parseByte(totalFee));
					//WolfHttpRequest.sendIncoinsPaydata(inCoins.getEquipmentnum(), inCoins.getPort(), Byte.parseByte(totalFee));
					// 更新脉冲已支付状态
					inCoinsDao.updateInCoinsStatusAndRecycletype(orderNum, (byte)1);
					logger.info("更新预订单的状态");
					//--------------------------------------------------------------------
					// 获取设备所在小区信息
					Equipment equipment = equipmentDao.getEquipmentById(code);
					Integer aId = CommUtil.toInteger(equipment.getAid());
					List<Map<String, Object>> partInfo = new ArrayList<Map<String,Object>>();
					// 设备不存在小区就没有分成
					Double merMoney = 0.00;
					Double partMoney = 0.00;
					if(aId != 0){
						Parameters parame = new Parameters();
						parame.setState(CommUtil.toString(aId));
						parame.setType(CommUtil.toString(2));
						parame.setSort("ORDER BY av.sort");
						// 查询合伙人信息
						partInfo = CommUtil.isListMapEmpty(areaDao.selectaRearelInfo(parame));
					}
					// 分成计算
					Map<String, Object> percentmap = StringUtil.percentCalculateDispose(partInfo, merId, payMoney);
					logger.info("计算分成==========================");
					partMoney = CommUtil.toDouble(percentmap.get("partmoney"));
					merMoney = CommUtil.toDouble(percentmap.get("mermoney"));
					// 分成信息转成JSON
					String percentresult = CommUtil.toString(JSON.toJSON(percentmap.get("percentinfo")));
					// 交易记录数据
					TradeRecord tradeRecord = new TradeRecord();
					tradeRecord.setMerid(merId);
					tradeRecord.setUid(uId);
					tradeRecord.setManid(aId);
					tradeRecord.setOrdernum(orderNum);
					tradeRecord.setMoney(payMoney);
					tradeRecord.setMermoney(merMoney);
					tradeRecord.setManmoney(partMoney);
					tradeRecord.setCode(code);
					tradeRecord.setPaysource(2);
					tradeRecord.setPaytype(12);
					tradeRecord.setStatus(payStatus);
					tradeRecord.setHardver(hardVersion);
					tradeRecord.setCreateTime(new Date());
					tradeRecord.setComment(percentresult);
					// 添加交易信息
					tradeRecordDao.insertToTradeRecord(tradeRecord);
					logger.info("添加交易记录==========================");
					List<Map<String, Object>> percentinfo = (List<Map<String, Object>>) percentmap.get("percentinfo");
					//更改用户的收益
					for(Map<String, Object> item : percentinfo){
						Integer mercid = CommUtil.toInteger(item.get("merid"));
						Integer partid = CommUtil.toInteger(item.get("partid"));
						Double moneyA = CommUtil.toDouble(item.get("money"));
						if(mercid.equals(0)) mercid = partid;
						merchantDetailService.merEearningCalculate(mercid, moneyA, type, orderNum, paySource, payType, payStatus);
						logger.info("添加商家明细记录--------------------------------");
					}
					// 更改小区的收益
					areaService.addAPartEarn(code, payMoney, merId, aId, type, payStatus);
					logger.info("添加小区交易记录--------------------------------");
					return "success";
				}
			}
		}
		return "fail";
	}

	
	@Override
	public Map<String, Object> unionDoRefond(HttpServletRequest request) {
		// 获取订单ID
		// refundState:1-充电 2-离线卡 3-脉冲投币 4-钱包 5-在线卡
		Map<String, Object> map = new HashMap<String, Object>();
		Integer orderId = CommUtil.toInteger(request.getParameter("id"));
		Integer refundState = CommUtil.toInteger(request.getParameter("refundState"));
		if(orderId == 0 || refundState == 0){
			logger.info("参数错误===================");
			map.put("ok", "error");
			return map;
		}
		Double payMoney = 0.00;
		String orderNum = null;
		Integer merId = 0;
		// 查询投币记录
		if(refundState == 3){
			InCoins incoins = inCoinsDao.selectInCoinsRecordById(orderId);
			orderNum = CommUtil.toString(incoins.getOrdernum());
			payMoney = CommUtil.toDouble(incoins.getMoney());
			merId = CommUtil.toInteger(incoins.getMerchantid());
		// 查询充电记录
		}else if(refundState == 1){
			Map<String,Object> chargeMap = chargeRecordDao.selectOneRecode(orderId);
			if(chargeMap == null) {
				logger.info("查询充电记录时记录不存在");
				map.put("ok", "error");
				return map;
			}
			orderNum = CommUtil.toString(chargeMap.get("ordernum"));
			payMoney = CommUtil.toDouble(chargeMap.get("expenditure"));
			merId = CommUtil.toInteger(chargeMap.get("merchantid"));
		}else{
			logger.info("1未知类型的退款");
			map.put("ok", "error");
			return map;
		}
		// 参数判断
		if(payMoney == 0.00 || merId == 0 || orderNum == null){
			logger.info("查询的订单数据存在异常");
			map.put("ok", "error");
			return map;
		}
		// 根据订单ID和单号查询交易记录
		TradeRecord trade = new TradeRecord();
		trade.setOrdernum(orderNum);
		TradeRecord tradeRecord = tradeRecordDao.getTraderecordList(trade);
		// 判断商家及合伙人的收益是否充足
		if ((tradeRecord == null) || userService.checkUserIfRich(tradeRecord) == false) {
			map.put("ok", "error");
			return map;
		}
		logger.info("投币交易订单==="+orderNum);
		logger.info("投币交易订单商家ID==="+merId);
		logger.info("投币支付的钱==="+payMoney);
		SortedMap<String, String> param = new TreeMap<>();
		param.put("service", "unified.trade.refund");
		param.put("version", "2.0");
		param.put("charset", "UTF-8");
		param.put("mch_id", WeiXinConfigParam.UNIONPAYMERCHID);
		// 操作员ID必传(默认为商户号)
		param.put("op_user_id", WeiXinConfigParam.UNIONPAYMERCHID);
		// 商家交易或平台订单号(必传一个)
		param.put("out_trade_no", orderNum);
		// 退款订单号
		param.put("out_refund_no", "t"+orderNum);
		// 原路退回
		param.put("refund_channel", "ORIGINAL");
		param.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		param.put("total_fee", (int)(payMoney*100)+"");
		param.put("refund_fee",(int)(payMoney*100)+"");
		Map<String,String> refundData = SignUtils.sendPostToUnionpay(param);
		//判断退款交易的结果
		logger.info("请求银联退款接口返回的数据===="+refundData);
		// 查询银联订单交易是否成功
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			logger.info("线程休息时发生了异常");
			e.printStackTrace();
		}
		SortedMap<String, String> params = new TreeMap<>();
		params.put("service", "unified.trade.query");
		params.put("version", "2.0");
		params.put("charset", "UTF-8");
		params.put("mch_id", WeiXinConfigParam.UNIONPAYMERCHID);
		params.put("out_trade_no", orderNum);
		params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		params.put("version", "2.0");
		Map<String,String> dataMap = SignUtils.sendPostToUnionpay(params);
		// 判断交易状态
		logger.info("请求银联查询订单接口返回的数据===="+dataMap);		
		if("0".equals(dataMap.get("status")) && "0".equals(dataMap.get("result_code")) && "REFUND".equals(dataMap.get("trade_state"))){
				logger.info("开始执行退款逻辑===="+orderNum);
				logger.info("开始执行退款逻辑===="+payMoney);
				logger.info("开始执行退款逻辑的商家ID===="+merId);
				// 脉冲投币退款
				if(refundState == 3){
					// 脉冲投币退款
					InCoins inCoins = new InCoins();
					inCoins.setId(orderId);
					inCoins.setOrdernum(orderNum);
					inCoins.setMoney(payMoney);
					inCoins.setMerchantid(merId);
					// 处理脉冲退款
					inCoinsService.inCoinsRefund(inCoins);
					logger.info("脉冲投币退款成功");
					map.put("ok", "ok");
					return map;
				// 智慧款设备退款
				}else if(refundState == 1){
					ChargeRecord chargeRecord = new ChargeRecord();
					chargeRecord.setId(orderId);
					chargeRecord.setOrdernum(orderNum);
					chargeRecord.setExpenditure(payMoney);
					chargeRecord.setMerchantid(merId);
					chargeRecordService.chargeRefund(chargeRecord);
					logger.info("智慧款设备退款成功");
					map.put("ok", "ok");
					return map;
				}else{
					logger.info("2未知类型的退款");
					map.put("ok", "error");
					return map;
				}
			}else{
				logger.info("退款失败----银联交易错误");
				map.put("ok", "error");
				return map;
			}
	}
	
	/**
	 * @method_name: refundTradeInquire 【该接口在退款时使用】
	 * @Description: 根据订单号查询交易记录中订单信息
	 * @param ordernum:订单号
	 * @return
	 * @Author: origin  创建时间:2020年8月28日 上午10:40:15
	 * @common:   
	 */
	@Override
	public List<Map<String, Object>> refundTradeInquire(String ordernum) {
		List<Map<String, Object>> result = tradeRecordDao.refundTradeInquire(ordernum);
		return CommUtil.isListMapEmpty(result);
	}

	/**
	 * @method_name: tradeComputeMap
	 * @Description: 查询指定时间的类型、来源的订单数量和金额
	 * @param parame
	 * @Author: origin  创建时间:2020年8月31日 下午6:35:36
	 */
	@Override
	public Map<String, Object> tradeComputeMap(Parameters parame) {
		Map<String, Object> result = tradeRecordDao.tradeRecordCompute(parame);
		return CommUtil.isMapEmpty(result);
	}


	@Override
	public Object selectOrderData(HttpServletRequest request){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			// 参数
			Map<String, Object> mapParam = CommUtil.getRequestParam(request);
			// 条数
			int numPerPage =  CommUtil.toInteger(mapParam.get("numPerPage"));
			// 当前页数
			int currentPage =  CommUtil.toInteger(mapParam.get("currentPage"));
			// 分页类
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			// 获取登录用户信息
			User user = CommonConfig.getAdminReq(request);
			System.out.println("叽叽叽叽叽叽叽叽"+(null == user));
			if(null == user){
				dataMap.put("result","用户未登录");
				return CommUtil.responseBuildInfo(301, "异常错误", mapParam);
			}
			//前端传递代理商名下某一个商家的id
			Integer agentSelectmerId =  CommUtil.toInteger(mapParam.get("agentSelectmerid"));
			if(agentSelectmerId!= null && !agentSelectmerId.equals(0)){
				user = new User();
				user.setRank(2);
				user.setId(agentSelectmerId);
			}
			// 参数类
			Parameters parameters = new Parameters();
			String startTime = CommUtil.toString(mapParam.get("startTime"));
			String endTime = CommUtil.toString(mapParam.get("endTime"));
			logger.info("开始时间A===="+startTime);
			logger.info("结束时间A===="+endTime);
			// 查询时必须带时间
			if(StringUtils.isEmpty(startTime)){
				startTime = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(new Date());
			}
			if(StringUtils.isEmpty(endTime)){
				endTime = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(new Date());
			}
			logger.info("开始时间===="+startTime);
			logger.info("结束时间===="+endTime);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			// 时间跨度不能超过一个月
			Date date1 = format.parse(startTime);
			Date date2 = format.parse(endTime);
			int interval = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
			logger.info("时间跨度===="+interval);
			if(0 > interval || interval >30){
				dataMap.put("result", "时间跨度不能大于30天");
				return CommUtil.responseBuildInfo(301, "异常错误", dataMap);
			}
			logger.info("时间跨度===="+interval);
			// 设置时间参数
			parameters.setStartTime(startTime);
			parameters.setEndTime(endTime);
			// 设置用户ID参数
			Integer rank = CommUtil.toInteger(user.getRank());
			if(!rank.equals(0)) parameters.setUid(user.getId());
			// 订单号
			parameters.setOrder(CommUtil.toString(mapParam.get("ordernum")));
			// 用户昵称
			parameters.setNickname(CommUtil.toString(mapParam.get("usernick")));
			// 商家昵称
			parameters.setDealer(CommUtil.toString(mapParam.get("dealer")));
			// 商家的手机号
			parameters.setMobile(CommUtil.toString(mapParam.get("mobile")));
			// 设备号
			parameters.setCode(CommUtil.toString(mapParam.get("code")));
			//订单状态 1正常 2 退款
			Integer orderStatus = CommUtil.toInteger( mapParam.get( "status" ) );
			if(orderStatus != 0){
				parameters.setOrderStatus(orderStatus);
			}
			Integer payType = CommUtil.toInteger( mapParam.get( "paytype" ) );
			//支付方式1、钱包2、微信3、支付宝 、4微信小程序、5支付宝小程序 6虚拟充值 ,12银联支付
			if(payType != 0){
				parameters.setPayType( payType );
			}
			//支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、钱包-5、在线卡-6、包月支付（显示为虚拟充值）
			Integer paySource = CommUtil.toInteger(mapParam.get("paysource"));
			if(paySource != 0){
				parameters.setPaySource(paySource);
			}
			// 根据条件查询交易记录主键的ID
			Integer recordId = 0;
			logger.info("当前的页数===="+currentPage);
			if(currentPage == 0){
				// 无分页查询符合条件的第一条交易记录的主键ID
				recordId = CommUtil.toInteger(tradeRecordDao.selectTradeId(parameters));
				logger.info("无分页交易记录的主键ID===="+recordId);
				if (recordId == 0){
					logger.info("商家一段时间内没有交易记录");
					dataMap.put("result", "此时间端内没有交易记录");
					return CommUtil.responseBuildInfo(301, "异常错误", dataMap);
				}
				// 有分页条件时,根据页数计算分页的第一条记录主键ID
			}else if(currentPage >= 1){
				logger.info("分页下标索引===="+(currentPage -1)*10);
				parameters.setStartnumber((currentPage -1)*10);
				recordId = CommUtil.toInteger(tradeRecordDao.selectTradeId(parameters));
				logger.info("有分页交易记录的主键ID===="+recordId);
				if (recordId == 0){
					logger.info("商家一段时间内没有交易记录");
					dataMap.put("result", "此时间端内没有交易记录");
					return CommUtil.responseBuildInfo(301, "异常错误", dataMap);
				}
			}
			// 设置交易记录的主键
			parameters.setId(recordId);
			// 无分页时查询总条数,总收益，净收入，支出，钱包消费，钱包退费
			Integer tradeRecordCount = 0;
			if(currentPage == 0){
				// 查询符合条件的总条数
				tradeRecordCount = tradeRecordDao.selectTradeCount(parameters);
				logger.info("无分页的总条数===="+tradeRecordCount);
				// 根据click判断是否进入首页
				Integer click = CommUtil.toInteger( mapParam.get( "click" ) );
				logger.info("是否条件查询===="+click);
				Map<String,Object> collectData = new HashMap<String, Object>(  );
				// 进入首页查询汇总
				if (click == 0) collectData = tradeRecordDao.selectTradeCollect( parameters );
				// 根据条件查询汇总
				if (click == 1) collectData = tradeRecordDao.selectMerTradeCollect( parameters );
				logger.info("查询的汇总数据===="+collectData.isEmpty());
				Double walletMoney = CommUtil.toDouble( collectData.get( "walletmoney" ) );
				Double totalMoney = CommUtil.toDouble( collectData.get( "totalmoney" ) );
				Double refWalletMoney = CommUtil.toDouble( collectData.get( "refwalletmoney" ) );
				Double refTotalMoney = CommUtil.toDouble( collectData.get( "reftotalmoney" ) );
				Double incomeMoney = CommUtil.subBig(totalMoney, refTotalMoney);
				Map<String, Object> totalmap = new HashMap<>();
				// 向前端返回总数据
				totalmap.put("walletmoney", walletMoney);
				totalmap.put("refwalletmoney", refWalletMoney);
				totalmap.put("totalmoney", totalMoney);
				totalmap.put("reftotalmoney", refTotalMoney);
				totalmap.put("incomemoney", incomeMoney);
				page.setMap(totalmap);
				// 分页查询
				page.setTotalRows(tradeRecordCount);
				page.setTotalPages();
				page.setStart();
				page.setEnd();
				parameters.setPages(page.getNumPerPage());
				parameters.setStartnumber(page.getStartIndex());
				List<Map<String, Object>> mapList = tradeRecordDao.selectTradeData( parameters );
				logger.info("查询的交易数据===="+mapList.isEmpty());
				dataMap.put("mapdata", totalmap);
				dataMap.put("listdata", CommUtil.isListMapEmpty(mapList));
				dataMap.put("totalRows", page.getTotalRows());
				dataMap.put("totalPages", page.getTotalPages());
				dataMap.put("currentPage", page.getCurrentPage());
				CommUtil.responseBuildInfo(200, "成功", dataMap);
				// 有分页时的查询
			}else if(currentPage >=1){
				
				
				parameters.setPages(10);
				List<Map<String, Object>> mapList = tradeRecordDao.selectTradeData( parameters );
				logger.info("根据分页条件进行查询===="+mapList.isEmpty());
				dataMap.put("listdata", CommUtil.isListMapEmpty(mapList));
				CommUtil.responseBuildInfo(200, "成功", dataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", dataMap);
		}
		return dataMap;
	}
}
