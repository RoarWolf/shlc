package com.hedong.hedongwx.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.MoneyDao;
import com.hedong.hedongwx.entity.Money;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.GeneralDetailService;
import com.hedong.hedongwx.service.MoneyService;
import com.hedong.hedongwx.service.OperateRecordService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Service
public class MoneyServiceImpl implements MoneyService{

	@Autowired
	private MoneyDao moneyDao;
	@Autowired
	private UserService userService;
	@Autowired
	private OperateRecordService operateRecordService;
	@Autowired
	private GeneralDetailService generalDetailService;
	@Autowired
	private TradeRecordService tradeRecordService;
	
	/*-- *** 请暂时不要在*号范围内添加   thanks ***********************************************************   --*/
	
	public Integer insertWallet(Money money) {
		return moneyDao.insertWalletRecord(money);
	}
	
	/**
	 * @Description: 根据实体类插入钱包记录信息
	 * @param uid
	 * @return
	 * @Author: origin  创建时间:2020年8月17日 下午5:38:25
	 * @common:
	 */
	@Override
	public Integer insertWalletInfo(Integer uid, Integer merid, String ordernum, Integer paysource, 
		Integer paytype, Integer status, Double paymoney, Double sendmoney, Double tomoney, Double balance, 
		Double topupbalance, Double givebalance, Date paytime, String remark) {
		Money money = new Money();
		money.setUid(uid);
		money.setUid(merid);
		money.setOrdernum(ordernum);
		money.setPaytype(paysource);
		money.setPaytype(paytype);
		money.setStatus(status);
		money.setMoney(CommUtil.toDouble(paymoney));
		money.setSendmoney(CommUtil.toDouble(sendmoney));
		money.setTomoney(CommUtil.toDouble(tomoney));
		money.setTopupbalance(CommUtil.toDouble(topupbalance));
		money.setGivebalance(CommUtil.toDouble(givebalance));
		money.setBalance(CommUtil.toDouble(balance));
		money.setPaytime(new Date());
		money.setRemark(remark);
		return insertWallet(money);
	}
	/*-- ****************************************************************************************  --*/
	
	
	@Override
	public List<Money> MoneyRecordByid(Integer uid) {
		return moneyDao.MoneyRecordByid(uid);
	}

	@Override
	public Money payMoneyinfo(Integer id) {
		return moneyDao.payMoneyinfo(id);
	}

	@Override
	public Money payMoney(Integer uid,Double paymoney,Double sendmoney,Double accountmoney,Double topupbalance,Double givebalance,
		String ordernum,Date date, Integer paytype,String remark) {
		Money money = new Money();
		money.setMoney(CommUtil.toDouble(paymoney));
		money.setSendmoney(CommUtil.toDouble(sendmoney));
		money.setTomoney(CommUtil.toDouble(accountmoney));
		money.setTopupbalance(CommUtil.toDouble(topupbalance));
		money.setGivebalance(CommUtil.toDouble(givebalance));
		money.setOrdernum(ordernum);
		money.setPaytime(date);
		money.setPaytype(paytype);
		money.setStatus(0);
		money.setRemark(remark);
		money.setBalance(0.0);
		money.setUid(uid);
		moneyDao.payMoneys(money);
		return null;
	}

	@Override
	public int updateMoney(Money money) {
		return moneyDao.updateMoney(money);
	}

	@Override
	public Money queryMoneyByOrdernum(String ordernum) {
		return moneyDao.queryMoneyByOrdernum(ordernum);
	}

	@Override
	public int payMoneys(Integer uid, String ordernum, Integer paytype, Integer status, Double paymoney, Double sendmoney,
			Double tomoney, Double balance, Double topupbalance,Double givebalance, String remark) {
		Money money = new Money();
		money.setUid(uid);
		money.setOrdernum(ordernum);
		money.setPaytype(paytype);
		money.setStatus(status);
		money.setMoney(CommUtil.toDouble(paymoney));
		money.setSendmoney(CommUtil.toDouble(sendmoney));
		money.setTomoney(CommUtil.toDouble(tomoney));
		money.setTopupbalance(CommUtil.toDouble(topupbalance));
		money.setGivebalance(CommUtil.toDouble(givebalance));
		money.setBalance(CommUtil.toDouble(balance));
		money.setPaytime(new Date());
		money.setRemark(remark);
		return moneyDao.payMoneys(money);
	}
	
	@Override
	public PageUtils<Parameter> selectwalletinfo(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameter> page  = new PageUtils<>(numPerPage, currentPage);
		Parameter parameter = new Parameter();
		parameter.setOrdernum(request.getParameter("ordernum"));
		parameter.setUsername(request.getParameter("username"));
		parameter.setRealname(request.getParameter("realname"));
		parameter.setPhoneNum(request.getParameter("phoneNum"));
//		id、uid、ordernum、paytype、status、money、
//		balance、paytime、remark
		String paytype = request.getParameter("paytype");
		if("1".equals(paytype)) parameter.setType("1,2");
		if("2".equals(paytype)) parameter.setType("3,4,5");
		parameter.setStatus(request.getParameter("cardID"));
		parameter.setStartTime(request.getParameter("startTime"));
		parameter.setEndTime(request.getParameter("endTime"));
		List<Map<String, Object>> ordermoney = moneyDao.selectwalletinfo(parameter);
		page.setTotalRows(ordermoney.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameter.setNumPerPage(page.getNumPerPage());
		parameter.setStartIndex(page.getStartIndex());
		List<Map<String, Object>> orderli = moneyDao.selectwalletinfo(parameter);
		
		page.setListMap(orderli);
		return page;
	}

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	@Override
	public PageUtils<Parameters> selectWalletRecord(HttpServletRequest request) {
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
		parameters.setOrder(request.getParameter("ordernum"));
		parameters.setNickname(request.getParameter("username"));
		parameters.setUsername(request.getParameter("realname"));
		
		String paytype = request.getParameter("paytype");
		if("1".equals(paytype)) parameters.setType("0,1");
		if("2".equals(paytype)) parameters.setType("2,3,4,5");
		List<Map<String, Object>> orderWallet = moneyDao.selectWalletRecord(parameters);
		page.setTotalRows(orderWallet.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		List<Map<String, Object>> orderoney = moneyDao.selectWalletRecord(parameters);
		page.setListMap(orderoney);
		return page;
	
	}

	/**
	 * @Description： 根据订单号更改状态
	 * @author： origin   
	 */
	@Override
	public int updateMoneyByOrder(String ordernum, Integer paytype) {
		return moneyDao.updateMoneyByOrder( ordernum, paytype);
	}
	
	@Override
	public Map<String, Object> mercVirtualMoneyPay(User user, Double paymoney, Double sendmoney, Integer id, String val, Integer status) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			if(user == null || CommUtil.toInteger(user.getId()).equals(0)){
				return CommUtil.responseBuildInfo(102, "获取用户信息为空或不正确", datamap);
			}
			if(val==null) val = "vriwallet";
			User touruser = userService.selectUserById(id);
			if(touruser == null || CommUtil.toInteger(touruser.getId()).equals(0)){
				return CommUtil.responseBuildInfo(102, "获取用户信息为空或不正确", datamap);
			}
			paymoney = CommUtil.toDouble(paymoney);
			sendmoney = CommUtil.toDouble(sendmoney);
			
			Double opermoney = paymoney;
			Double opersendmoney = sendmoney;
			Double userbalance = CommUtil.toDouble(touruser.getBalance());
			Double usersendmoney = CommUtil.toDouble(touruser.getSendmoney());
			
			Double opertopupmoney = CommUtil.addBig(userbalance, opermoney);
			Double opergivemoney = CommUtil.addBig(usersendmoney, opersendmoney);
			if(opertopupmoney<0){
				opermoney = CommUtil.subBig(0.00, userbalance);
			}
			if(opergivemoney<0){
				opersendmoney = CommUtil.subBig(0.00, usersendmoney);
			}
			Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
			Double topupbalance = CommUtil.addBig(opermoney, userbalance);
			Double givebalance = CommUtil.addBig(opersendmoney, usersendmoney);
			Double operbalance = CommUtil.addBig(topupbalance, givebalance);
			
			touruser.setBalance(topupbalance);
			touruser.setSendmoney(givebalance);
			userService.updateUserById(touruser);
			String ordernum = HttpRequest.createOrdernum(6);
			Date time = new Date();
			
			insertionMoneyData(touruser.getId(), ordernum, 1, 1, opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, time, val);
			operateRecordService.insertoperate( "虚拟充值钱包", user.getId(), id, 2, 4, ordernum,null);
			tradeRecordService.insertTrade(CommUtil.toInteger(user.getMerid()), touruser.getId(), ordernum, opertomoney, 0.00, "钱包", 4, 6, 1, "虚拟充值钱包");
			generalDetailService.insertGenWalletDetail(touruser.getId(), CommUtil.toInteger(user.getMerid()), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, ordernum, time, 7);
			datamap.put("type", 1);
			datamap.put("ordernum", ordernum);
			datamap.put("uid", CommUtil.toString(touruser.getId()));
			datamap.put("cardID", null);
			
//			Parameters parame = new Parameters();
//			parame.setOrder(ordernum);
//			parame.setParamete(CommUtil.toString(touruser.getId()));
//			List<Map<String, Object>> listdata =  CommUtil.isListMapEmpty(moneyDao.selectWalletRecord(parame));
//			datamap.put("orderdata", listdata);
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	@Override
	public Map<String, Object> mercVirtualReturn(Integer id) {
		try {
			Money moneyrecord = moneyDao.payMoneyinfo(id);
			User touruser = userService.selectUserById(moneyrecord.getUid());

			Double opermoney = CommUtil.toDouble(moneyrecord.getMoney());
			Double opersendmoney = CommUtil.toDouble(moneyrecord.getSendmoney());
			Double opertmoney = CommUtil.toDouble(moneyrecord.getTomoney());
			Double userbalance = CommUtil.toDouble(touruser.getBalance());
			Double usersendmoney = CommUtil.toDouble(touruser.getSendmoney());
			
			Double opertopupmoney = CommUtil.subBig(userbalance, opermoney);
			Double opergivemoney = CommUtil.subBig(usersendmoney, opersendmoney);
			if(opertopupmoney<0){
				opermoney = CommUtil.subBig(0.00, userbalance);
			}
			if(opergivemoney<0){
				opersendmoney = CommUtil.subBig(0.00, usersendmoney);
			}
			
			Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
			
			Double topupbalance = CommUtil.subBig(userbalance, opermoney);
			Double givebalance = CommUtil.subBig(usersendmoney, opersendmoney);
			
//			Double topupbalance = CommUtil.addBig(opermoney, userbalance);
//			Double givebalance = CommUtil.addBig(opersendmoney, usersendmoney);
			Double operbalance = CommUtil.addBig(topupbalance, givebalance);
			
			touruser.setBalance(topupbalance);
			touruser.setSendmoney(givebalance);
			Date time = new Date();
			userService.updateUserById(touruser);
			
			moneyrecord.setPaytype(6);
			moneyDao.updateMoney(moneyrecord);
			generalDetailService.insertGenWalletDetail(touruser.getId(), touruser.getMerid(), opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, moneyrecord.getOrdernum(), time, 8);
//			generalDetailService.insertGenWalletDetail(touruser.getId(), touruser.getMerid(), paymoney, sendmoney, balance, objmaney.getOrdernum(), time, 8);
			tradeRecordService.insertTrade(touruser.getMerid(), touruser.getId(), moneyrecord.getOrdernum(), 
					CommUtil.toDouble(moneyrecord.getMoney()), 0.00, "钱包", 4, 6, 2, "虚拟充值钱包退费");
			return CommonConfig.messg(200);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonConfig.messg(403);
		}
	}

	@Override
	public List<Map<String, Object>> cardPayRecord(Integer uid, String remark, String beginTime, String endTime) {
		
		Parameters parame = new Parameters();
		parame.setParamete(CommUtil.toString(uid));
		//parame.setRemark(remark);
		parame.setStartTime(beginTime);
		parame.setEndTime(endTime);
		List<Map<String, Object>> icmoney =  moneyDao.selectWalletRecord(parame);
		return icmoney;
	}
	
	/**
	 * separate
	 * @Description： 虚拟充值钱包操作
	 * @author： origin   2019年11月9日 上午11:47:04
	 */
	@Override
	public Object virtualTopUpWallet(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			User user = CommonConfig.getAdminReq(request);
			Integer id =  CommUtil.toInteger(maparam.get("id"));
			Double paymoney =  CommUtil.toDouble(maparam.get("money"));
			Double sendmoney =  CommUtil.toDouble(maparam.get("sendmoney"));
			String val =  CommUtil.toString(maparam.get("val"));
			if(val==null || val.equals("")) val = "vriwallet";
			User touruser = userService.selectUserById(id);
			
			Double opermoney = paymoney;
			Double opersendmoney = sendmoney;
			Double userbalance = CommUtil.toDouble(touruser.getBalance());
			Double usersendmoney = CommUtil.toDouble(touruser.getSendmoney());
			
			Double opertopupmoney = CommUtil.addBig(userbalance, opermoney);
			Double opergivemoney = CommUtil.addBig(usersendmoney, opersendmoney);
			if(opertopupmoney<0){
				opermoney = CommUtil.subBig(0.00, userbalance);
			}
			if(opergivemoney<0){
				opersendmoney = CommUtil.subBig(0.00, usersendmoney);
			}
			Double opertomoney = CommUtil.addBig(opermoney, opersendmoney);
			Double topupbalance = CommUtil.addBig(opermoney, userbalance);
			Double givebalance = CommUtil.addBig(opersendmoney, usersendmoney);
			Double operbalance = CommUtil.addBig(topupbalance, givebalance);
			
			touruser.setBalance(topupbalance);
			touruser.setSendmoney(givebalance);
			userService.updateUserById(touruser);
			String ordernum = HttpRequest.createOrdernum(6);
			Date time = new Date();
			insertionMoneyData(touruser.getId(), ordernum, 1, 1, opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, time, val);
			operateRecordService.insertoperate( "虚拟充值钱包", user.getId(), id, 2, 4, ordernum,null);
			tradeRecordService.insertTrade(CommUtil.toInteger(user.getMerid()), touruser.getId(), ordernum, opertomoney, 0.00, "钱包", 4, 6, 1, "虚拟充值钱包");
			
			generalDetailService.insertGenWalletDetail(touruser.getId(), CommUtil.toInteger(user.getMerid()),  opermoney, opersendmoney, opertomoney, operbalance, topupbalance, givebalance, ordernum, time, 7);
			CommUtil.responseBuildInfo(200, "成功", datamap);
//			Double accountmoney = CommUtil.addBig(paymoney, sendmoney);
//			Double balance = CommUtil.addBig(userbalance, usersendmoney);
//			Double topupbalance = userbalance;
//			Double givebalance = usersendmoney;
//			touruser.setBalance(userbalance);
//			touruser.setSendmoney(usersendmoney);
//			userService.updateUserById(touruser);
//			String ordernum = HttpRequest.createOrdernum(6);
//			Date time = new Date();
//			insertionMoneyData(touruser.getId(), ordernum, 1, 1, paymoney, sendmoney, accountmoney, balance, topupbalance, givebalance, time, val);
//			operateRecordService.insertoperate( "虚拟充值钱包", user.getId(), id, 2, 4, ordernum,null);
//			tradeRecordService.insertTrade(CommUtil.toInteger(user.getMerid()), touruser.getId(), ordernum, accountmoney, 0.00, "钱包", 4, 6, 1, "虚拟充值钱包");
//			
//			generalDetailService.insertGenWalletDetail(touruser.getId(), CommUtil.toInteger(user.getMerid()), paymoney, sendmoney, balance, ordernum, time, 7);
//			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	/**
	 * separate
	 * @Description： 插入钱包信息
	 * @author： origin   2019年11月9日 下午3:57:45
	 * @param givebalance 
	 * @param topupbalance 
	 */
	public Integer insertionMoneyData(Integer uid, String ordernum, Integer paytype, Integer status, Double paymoney, 
		Double sendmoney, Double accountmoney, Double balance, Double topupbalance, Double givebalance, Date date, String remark) {
		Money money = new Money();
		money.setUid(uid);
		money.setOrdernum(ordernum);
		money.setPaytype(paytype);
		money.setStatus(status);
		money.setMoney(paymoney);
		money.setSendmoney(sendmoney);
		money.setTomoney(accountmoney);
		money.setTopupbalance(topupbalance);
		money.setGivebalance(givebalance);
		money.setBalance(balance);
		money.setPaytime(date);
		money.setRemark(remark);
		return moneyDao.payMoneys(money);
	}

	/**
	 * separate
	 * @Description：钱包记录信息查看
	 * @author： origin 
	 */
	@Override
	public Object walletRecordData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			User user = CommonConfig.getAdminReq(request);
			Parameters parameters = new Parameters();
			Integer rank = CommUtil.toInteger(user.getRank());
			if(!rank.equals(0)) parameters.setUid(user.getId());//绑定id
			
			parameters.setOrder(CommUtil.toString(maparam.get("ordernum")));
			parameters.setNickname(CommUtil.toString(maparam.get("usernick")));
			parameters.setUsername(CommUtil.toString(maparam.get("username")));

			parameters.setMobile(CommUtil.toString(maparam.get("phone")));
			parameters.setDealer(CommUtil.toString(maparam.get("dealer")));
			parameters.setRealname(CommUtil.toString(maparam.get("realname")));
			parameters.setParamete(CommUtil.toString(maparam.get("uid")));
			
			String paytype = CommUtil.toString(maparam.get("paytype"));
			if("1".equals(paytype)) parameters.setType("0,1");
			if("2".equals(paytype)) parameters.setType("2,3,4,5");
			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
//			parameters.setStartTime(CommUtil.getRelevantDate(maparam.get("startTime"), 7, 1));
//			parameters.setEndTime(CommUtil.getRelevantDate(maparam.get("endTime"), 0, 3));
			List<Map<String, Object>> orderWalletData = moneyDao.selectWalletRecord(parameters);
			page.setTotalRows(orderWalletData.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> orderWalletInfo = moneyDao.selectWalletRecord(parameters);
			
			datamap.put("listdata", CommUtil.isListMapEmpty(orderWalletInfo));
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

	
	@Override
	public List<Map<String, Object>> selectWalletRecord(Parameters parame) {
		try {
			List<Map<String, Object>> listdata =  CommUtil.isListMapEmpty(moneyDao.selectWalletRecord(parame));
			return listdata;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String,Object>>();
		}
	}
	/**
	 * 根据钱包充值的订单号查询商家的id
	 */
	@Override
	public Integer selectMerByMoneyOrdernum(String moneyOrder) {
		Integer merId = moneyDao.selectMerByMoneyOrdernum(moneyOrder);
		if(merId != null){
			return merId;
		}else{
			return 0;
		}
	}
}



