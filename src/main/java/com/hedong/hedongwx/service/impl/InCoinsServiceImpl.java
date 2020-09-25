package com.hedong.hedongwx.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.AreaDao;
import com.hedong.hedongwx.dao.EquipmentDao;
import com.hedong.hedongwx.dao.InCoinsDao;
import com.hedong.hedongwx.dao.TradeRecordDao;
import com.hedong.hedongwx.dao.UserEquipmentDao;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.InCoins;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Service
public class InCoinsServiceImpl implements InCoinsService {
	private final Logger logger = LoggerFactory.getLogger(InCoinsServiceImpl.class);
	@Autowired
	private InCoinsDao inCoinsDao;
	@Autowired
	private UserEquipmentDao userEquipmentDao;
	@Autowired
	private EquipmentDao equipmentDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private MerchantDetailService merchantDetailService;
	@Autowired
	private TradeRecordDao tradeRecordDao;

	@Override
	public int insertInCoinsRecord(String ordernum,String code,Byte port,Integer uid,
			Double money,Byte coinNum,Byte handletype, Byte status) {
		InCoins inCoins = new InCoins();
		UserEquipment userEquipment = userEquipmentDao.getUserEquipmentByCode(code);
		if (userEquipment != null) {
			inCoins.setMerchantid(userEquipment.getUserId());
		} else {
			inCoins.setMerchantid(0);
		}
		inCoins.setUid(uid);
		inCoins.setOrdernum(ordernum);
		inCoins.setEquipmentnum(code);
		inCoins.setPort(port);
		inCoins.setMoney(money);
		inCoins.setCoinNum(coinNum);
		inCoins.setStatus(status);
		inCoins.setHandletype(handletype);
		inCoins.setRecycletype((byte) 0);
		return inCoinsDao.insertInCoinsRecord(inCoins);
	}

	@Override
	public int updateInCoinsStatusAndRecycletype(String ordernum,Byte status) {
		return inCoinsDao.updateInCoinsStatusAndRecycletype(ordernum, status);
	}
	
	@Override
	public int updateInCoinsStatus(String ordernum,Byte handletype) {
		return inCoinsDao.updateInCoinsStatus(ordernum, handletype);
	}
	
	@Override
	public InCoins selectInCoinsRecordByOrdernum(String ordernum) {
		return inCoinsDao.selectInCoinsRecordByOrdernum(ordernum);
	}
	
	@Override
	public PageUtils<Parameter> selectCoinsParame(HttpServletRequest request) {
		
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		
  		PageUtils<Parameter> page  = new PageUtils<>(numPerPage, currentPage);

		Parameter parameter = new Parameter();
		String orderstatu = request.getParameter("orderstatu");
		String type = request.getParameter("type");
		if(orderstatu==null || orderstatu.equals("0")) orderstatu ="";
		if(type==null || type.equals("0")) type ="";
		
		if(orderstatu.equals("") && type.equals("")){
			parameter.setType(type);
			parameter.setStatus(orderstatu);
		}else if(!orderstatu.equals("") && !type.equals("")){
			if(orderstatu.equals("1")){//正常
				parameter.setType(type);
				parameter.setStatus("");
			}else if(orderstatu.equals("2")){//退费
				if(type.equals("1")){//微信
					parameter.setType("4");
				}else if(type.equals("2")){//支付宝
					parameter.setType("5");
				}
				parameter.setStatus("");
			}
		}else if(!orderstatu.equals("") && type.equals("")){
			if(orderstatu.equals("1")){//正常
				parameter.setStatus("1,2,3");
			}else if(orderstatu.equals("2")){//退费
				parameter.setStatus("4,5");
			}
			parameter.setType(type);
		}else if(orderstatu.equals("") && !type.equals("")){
			parameter.setStatus(orderstatu);
			parameter.setType(type);
		}
		parameter.setOrdernum(request.getParameter("ordernum"));
		parameter.setUsername(request.getParameter("username"));
		parameter.setPhoneNum(request.getParameter("phoneNum"));
		parameter.setDealer(request.getParameter("dealer"));
		parameter.setEqui((request.getParameter("equipmentnum")));
		parameter.setParem(request.getParameter("port"));
		parameter.setStartTime(request.getParameter("startTime"));
		parameter.setEndTime(request.getParameter("endTime"));
		
		List<Map<String, Object>> inCoinsList = inCoinsDao.selectCoinsParame(parameter);
		
		page.setTotalRows(inCoinsList.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameter.setNumPerPage(page.getNumPerPage());
		parameter.setStartIndex(page.getStartIndex());
		page.setListMap(inCoinsDao.selectCoinsParame(parameter));
		return page;
	}

	@Override
	public Double getInCoinsTodayMoneyByUserid(String begintime, String endtime,
			String equipmentnum, Integer merchantid, String ordernum) {
		return inCoinsDao.getInCoinsTodayMoneyByUserid(begintime, endtime, equipmentnum,merchantid,ordernum);
	}

	@Override
	public List<InCoins> getInCoinsByParam(String begintime, String endtime, String equipmentnum, Integer merchantid,
			String ordernum) {
		return inCoinsDao.getInCoinsByParam(begintime, endtime, equipmentnum, merchantid, ordernum);
	}

	@Override
	public InCoins selectInCoinsRecordById(Integer id) {
		return inCoinsDao.selectInCoinsRecordById(id);
	}
	
	@Override
	public List<InCoins> selectInCoinsNoReply() {
		return inCoinsDao.selectInCoinsNoReply();
	}

	@Override
	public Map<String, Object> selectsuminfo() {
		Parameters parameter = new Parameters();
		Map<String, Object> incoinstmap = inCoinsDao.selectsuminfo(parameter);
		String newtime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
		String endtime = StringUtil.toDateTime("yyyy-MM-dd 23:59:59");
		parameter.setStartTime(newtime);
		parameter.setEndTime(endtime);
		incoinstmap.put("nowincmap", inCoinsDao.selectsuminfo(parameter));
		return incoinstmap;
	}

	public int updateByPrimaryKeySelective(InCoins inCoins) {
		return inCoinsDao.updateByPrimaryKeySelective(inCoins);
	}

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	@Override
	public PageUtils<Parameters> selectIncoinsRecord(HttpServletRequest request) {
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
		Integer rank = user.getLevel();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(rank!=0) parameters.setUid(user.getId());//获取用户
		parameters.setOrder(request.getParameter("ordernum"));
		parameters.setNickname(request.getParameter("username"));
		parameters.setPhone(request.getParameter("phoneNum"));
		parameters.setDealer(request.getParameter("dealer"));
		parameters.setCode(request.getParameter("equipmentnum"));
		parameters.setRemark(request.getParameter("port"));
		parameters.setParamete(CommUtil.toString(request.getParameter("paystatus")));
		
		String orderstatu = CommUtil.toString(request.getParameter("orderstatu"));
		String type = request.getParameter("type");
		if(orderstatu==null || orderstatu.equals("0")) orderstatu ="";
		if(type==null || type.equals("0")) type ="";
		
		if(orderstatu.equals("") && type.equals("")){
			parameters.setType(type);
			parameters.setStatus(orderstatu);
		}else if(!orderstatu.equals("") && !type.equals("")){
			if(orderstatu.equals("1")){//正常
				parameters.setType(type);
				parameters.setStatus("");
			}else if(orderstatu.equals("2")){//退费
				if(type.equals("1")){//微信
					parameters.setType("4");
				}else if(type.equals("2")){//支付宝
					parameters.setType("5");
				}
				parameters.setStatus("");
			}
		}else if(!orderstatu.equals("") && type.equals("")){
			if(orderstatu.equals("1")){//正常
				parameters.setStatus("1,2,3");
			}else if(orderstatu.equals("2")){//退费
				parameters.setStatus("4,5");
			}
			parameters.setType(type);
		}else if(orderstatu.equals("") && !type.equals("")){
			parameters.setStatus(orderstatu);
			parameters.setType(type);
		}
		
		List<Map<String, Object>> inCoinsList = inCoinsDao.selectIncoinsRecord(parameters);
		
		page.setTotalRows(inCoinsList.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		page.setListMap(inCoinsDao.selectIncoinsRecord(parameters));
		return page;
	}

	// @Description：WeChat   查询脉冲设备投币总额
	@Override
	public Map<String, Object> selectcodesuminfo(Integer uid, String code, String startTime, String endTime) {
		Parameters parameter = new Parameters();
		parameter.setUid(uid);
		parameter.setCode(code);
		parameter.setStartTime(startTime);
		parameter.setEndTime(endTime);
		Map<String, Object> coinstmap = inCoinsDao.selectsuminfo(parameter);
		return coinstmap;
	}

	//@Description：WeChat  根据条件查询投币订单
	@Override
	public List<Map<String, Object>> selectcoinsRecord(Integer uid, String code, String startTime, String endTime) {
		Parameters parame = new Parameters();
		parame.setUid(uid);
		parame.setCode(code);
		parame.setStartTime(startTime);
		parame.setEndTime(endTime);
		parame.setType("3");
		List<Map<String, Object>>  coinstmap = inCoinsDao.selectIncoinsRecord(parame);
		return coinstmap;
	}
	
	/**
	 *  根据脉冲设备汇总线下投币上传信息
	 */
	@Override
	public Map<String, Object> selectcoinsup(Parameters parameters) {
		Map<String, Object> coins = inCoinsDao.selectcoinsup(parameters);
		return coins;
	}

	
	//============================================================================================
	/**
	 * separate
	 * @Description：投币记录信息查看
	 * @author： origin 
	 */
	@Override
	public Object inCoinsRecordData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			User user = CommonConfig.getAdminReq(request);
			//前端传递代理商名下某一个商家的id
			Integer agentSelectmerid =  CommUtil.toInteger(maparam.get("agentSelectmerid"));
			if(agentSelectmerid != null && !agentSelectmerid.equals(0)){
				user = new User();
				user.setLevel(2);
				user.setId(agentSelectmerid);
			}
			Parameters parameters = new Parameters();
			Integer rank = CommUtil.toInteger(user.getLevel());
			if(!rank.equals(0)) parameters.setUid(user.getId());//绑定id
			
			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
//			parameters.setStartTime(CommUtil.getRelevantDate(maparam.get("startTime"), 7, 1));
//			parameters.setEndTime(CommUtil.getRelevantDate(maparam.get("endTime"), 0, 3));

			parameters.setOrder(CommUtil.toString(maparam.get("ordernum")));
			parameters.setNickname(CommUtil.toString(maparam.get("usernick")));
			parameters.setPhone(CommUtil.toString(maparam.get("phone")));
			parameters.setDealer(CommUtil.toString(maparam.get("dealer")));
			parameters.setRealname(CommUtil.toString(maparam.get("realname")));
			parameters.setCode(CommUtil.toString(maparam.get("devicenum")));
			parameters.setRemark(CommUtil.toString(maparam.get("port")));
			parameters.setParamete(CommUtil.toString(maparam.get("paystatus")));//是否为预订单
			
			String orderstatu = CommUtil.toString(maparam.get("orderstatus"));
			String type = CommUtil.toString(maparam.get("paytype"));// 1:微信   2:支付宝  3:投币器  4:微信小程序  5:支付宝小程序
			if(orderstatu==null || orderstatu.equals("0")) orderstatu ="";
			if(type==null || type.equals("0")) type ="";
			if(orderstatu.equals("") && type.equals("")){
				parameters.setType(type);
				parameters.setStatus(orderstatu);
			}else if(!orderstatu.equals("") && !type.equals("")){
				if(orderstatu.equals("1")){//正常
					if ("4".equals(type)) {
						parameters.setType("8");
					} else if ("5".equals(type)) {
						parameters.setType("10");
					} else {
						parameters.setType(type);
					}
					parameters.setStatus("");
				}else if(orderstatu.equals("2")){//退费
					if(type.equals("1")){//微信
						parameters.setType("4");
					}else if(type.equals("2")){//支付宝
						parameters.setType("5");
					} else if ("4".equals(type)) {
						parameters.setType("9");
					} else if ("5".equals(type)) {
						parameters.setType("11");
					} else {
						parameters.setType(type);
					}
					parameters.setStatus("");
				}
			}else if(!orderstatu.equals("") && type.equals("")){
				if(orderstatu.equals("1")){//正常
					parameters.setStatus("1,2,3,8,10");
				}else if(orderstatu.equals("2")){//退费
					parameters.setStatus("4,5,9,11");
				}
				parameters.setType(type);
			}else if(orderstatu.equals("") && !type.equals("")){
				parameters.setStatus(orderstatu);
				if(type.equals("1")){//微信
					parameters.setStatus("1,4");
				}else if(type.equals("2")){//支付宝
					parameters.setStatus("2,5");
				} else if ("4".equals(type)) {
					parameters.setStatus("8,9");
				} else if ("5".equals(type)) {
					parameters.setStatus("10,11");
				} else {
					parameters.setType(type);
				}
			}
			Integer count = inCoinsDao.selectIncoinsRecordCount(parameters);
			page.setTotalRows(count);
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> inCoinsListinfo = inCoinsDao.selectIncoinsRecord(parameters);
			datamap.put("listdata", CommUtil.isListMapEmpty(inCoinsListinfo));
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	//============================================================================================
	
	@Override
	public void inCoinsRefund(InCoins incoins) {
		// 获取订单ID,订单编号,支付的钱
		String orderNum = CommUtil.toString(incoins.getOrdernum());
		Double payMoney = CommUtil.toDouble(incoins.getMoney());
		Integer merId = CommUtil.toInteger(incoins.getMerchantid());
		// 根据订单ID和订单号查询交易记录
		TradeRecord trade = new TradeRecord();
		trade.setOrdernum(incoins.getOrdernum());
		TradeRecord traderecord = tradeRecordDao.getTraderecordList(trade);
		if(traderecord == null) return;
		// 获取交易记录中的信息
		String deviceNum = CommUtil.toString(traderecord.getCode());
		Integer paySource = MerchantDetail.INCOINSSOURCE;
		Integer payType = MerchantDetail.UNIONPAY;
		Integer paystatus = MerchantDetail.REFUND;
		Integer aId = 0;
		// 变更减少设备的收益
		equipmentDao.updateEquEarn(deviceNum, payMoney, 0);
		// 变更脉冲投币订单为退款
		inCoinsDao.updateInCoinsStatus(orderNum, (byte) 13);
		// 查询设备获取设备的小区ID
		Equipment equipment = equipmentDao.getEquipmentById(deviceNum);
		// 设备存在小区减少小区下的收益
		if(equipment != null && equipment.getAid() != 0) aId = equipment.getAid();
		if(aId != 0) areaDao.updateAreaEarn(0, payMoney, aId, null, payMoney, null);
		// 设备存在合伙人减少合伙人的收益
		String comment = CommUtil.toString(traderecord.getComment());
		merchantDetailService.dealerIncomeRefund(comment, merId, payMoney, orderNum, paySource, payType, paystatus);
		// 添加退款的交易记录(交易记录只改变订单状态)
		TradeRecord tradeRecord = new TradeRecord();
		tradeRecord.setMerid(traderecord.getMerid());
		tradeRecord.setUid(traderecord.getUid());
		tradeRecord.setManid(0);
		tradeRecord.setOrdernum(orderNum);
		tradeRecord.setMoney(traderecord.getMoney());
		tradeRecord.setMermoney(traderecord.getMermoney());
		tradeRecord.setManmoney(traderecord.getManmoney());
		tradeRecord.setCode(equipment.getCode());
		tradeRecord.setPaysource(2);
		tradeRecord.setPaytype(12);
		tradeRecord.setStatus(2);
		tradeRecord.setHardver(equipment.getHardversion());
		tradeRecord.setCreateTime(new Date());
		tradeRecord.setComment(comment);
		// 添加退款交易信息
		tradeRecordDao.insertToTradeRecord(tradeRecord);
	}
}
