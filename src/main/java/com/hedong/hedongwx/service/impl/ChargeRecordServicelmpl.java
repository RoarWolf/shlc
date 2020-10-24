package com.hedong.hedongwx.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.AreaDao;
import com.hedong.hedongwx.dao.ChargeRecordDao;
import com.hedong.hedongwx.dao.EquipmentDao;
import com.hedong.hedongwx.dao.RealchargerecordDao;
import com.hedong.hedongwx.dao.TradeRecordDao;
import com.hedong.hedongwx.entity.AllPortStatus;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.ChargeRecordCopy;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.GeneralDetail;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.Realchargerecord;
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AllPortStatusService;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.GeneralDetailService;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.WolfHttpRequest;

@Service
public class ChargeRecordServicelmpl implements ChargeRecordService{
	
	@Autowired
	private ChargeRecordDao chargeRecordDao;
	@Autowired
	private AreaService areaService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private RealchargerecordDao realchargerecordDao;
	@Autowired
	private TradeRecordDao tradeRecordDao;
	@Autowired
	private EquipmentDao equipmentDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private MerchantDetailService merchantDetailService;
	@Autowired
	private UserService userService;
	@Autowired
	private GeneralDetailService generalDetailService;
	@Autowired
	private AllPortStatusService allPortStatusService;
	
	@Override
	public List<ChargeRecord> chargeRecordByUid(Integer uid) {
		return chargeRecordDao.chargeRecordByUid(uid);
	}
	
	@Override
	public ChargeRecord chargeRecordOne(Integer id) {
		ChargeRecord result = chargeRecordDao.chargeRecordOne(CommUtil.toInteger(id));
		return result = result == null ? new ChargeRecord() : result;
	}

	public ChargeRecord selectChargeEndUidByCodeAndPort(String equipmentnum, Integer port) {
		return chargeRecordDao.selectChargeEndUidByCodeAndPort(equipmentnum, port);
	}

	@Override
	public void insetRecord(ChargeRecord chargeRecord) {
		chargeRecordDao.insetRecord(chargeRecord);
	}

	@Override
	public List<ChargeRecord> getOrderByOrdernum(String ordernum) {
		List<ChargeRecord> result = chargeRecordDao.getOrderByOrdernum(CommUtil.toString(ordernum));
		return result = result == null ? new ArrayList<ChargeRecord>() : result;
	}

	@Override
	public void updateByOrdernum(Map<String, Object> params) {
		chargeRecordDao.updateByOrdernum(params);
	}
	
	@Override
	public List<ChargeRecord> getChargeRecordListByUseridAndTime(String begintime, String endtime, Integer uid) {
		return chargeRecordDao.getChargeRecordListByUseridAndTime(begintime, endtime, uid);
	}

	@Override
	public List<ChargeRecord> getTodayChargeRecordListByUserid(Integer merchantid) {
		return chargeRecordDao.getTodayChargeRecordListByUserid(merchantid);
	}

	@Override
	public List<ChargeRecord> getChargeRecordListByTimeAndUid(String begintime, String endtime, Integer uid) {
		return chargeRecordDao.getChargeRecordListByTimeAndUid(begintime, endtime, uid);
	}

	@Override
	public List<ChargeRecord> getChargeRecordListByEquipmentnum(String equipmentnum) {
		return chargeRecordDao.getChargeRecordListByEquipmentnum(equipmentnum);
	}

	@Override
	public List<ChargeRecord> getChargeRecordListByTimeAndEquipmentnumMerchantid(String begintime, String endtime,
			String equipmentnum, Integer merchantid, String ordernum) {
		return chargeRecordDao.getChargeRecordListByTimeAndEquipmentnumMerchantid(begintime, endtime, equipmentnum,merchantid,ordernum);
	}

	@Override
	public Map<String, Object> queryChargingByUid(Integer uid, Integer startnum) {
		Map<String,Object> map = new HashMap<>();
		try {
			List<ChargeRecord> charginglist = chargeRecordDao.queryChargingByUid(uid, startnum * 10);
			map.put("charginglist", charginglist);
			map.put("startnum", startnum++);
			if (charginglist.size() > 0) {
				return CommUtil.responseBuildInfo(1000, "获取成功", map);
			} else {
				return CommUtil.responseBuild(1000, "获取成功", null);
			}
		} catch (Exception e) {
			return CommUtil.responseBuild(1002, "系统异常", null);
		}
	}

	@Override
	public Double getChargingTotalMoney(Integer id) {
		return chargeRecordDao.getChargingTotalMoney(id);
	}

	@Override
	public int updateChargeEndtimeById(Integer id) {
		return chargeRecordDao.updateChargeEndtimeById(id);
	}

	@Override
	public Map<String, Object> queryChargedByUid(Integer uid, Integer startnum) {
		Map<String,Object> map = new HashMap<>();
		try {
			List<ChargeRecord> charginglist = chargeRecordDao.queryChargedByUid(uid, startnum * 10);
			map.put("charginglist", charginglist);
			map.put("startnum", startnum++);
			map.put("recordsize", charginglist.size());
			if (charginglist.size() > 0) {
				return CommUtil.responseBuildInfo(1000, "获取成功", map);
			} else {
				return CommUtil.responseBuild(1000, "获取成功", null);
			}
		} catch (Exception e) {
			return CommUtil.responseBuild(1002, "系统异常", null);
		}
	}

	//ＲＺＣ（ＰＣ）　查询订单记录
	@Override
	public PageUtils<ChargeRecord> selectOrderInfo(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		
		PageUtils<ChargeRecord> page  = new PageUtils<>(numPerPage, currentPage);
		
		ChargeRecord chargeRecord = new ChargeRecord();
		String paytype = request.getParameter("paytype");
		if( null != paytype && !paytype.equals("-1")) chargeRecord.setPaytype(Integer.parseInt(paytype));
		chargeRecord.setUsername(request.getParameter("username"));
		chargeRecord.setDealer(request.getParameter("dealer"));
		chargeRecord.setOrdernum(request.getParameter("ordernum"));
		chargeRecord.setEquipmentnum(request.getParameter("equipmentnum"));
		String number = request.getParameter("number");
		if (number != null && !number.equals("-1")) chargeRecord.setNumber(Integer.parseInt(number));
		chargeRecord.setBegintimes(request.getParameter("startTime"));
		chargeRecord.setEndtimes(request.getParameter("endTime"));
		List<ChargeRecord> equi = chargeRecordDao.selectOrderInfo(chargeRecord);
		page.setTotalRows(equi.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		chargeRecord.setNumPerPage(page.getNumPerPage());
		chargeRecord.setStartIndex(page.getStartIndex());
		page.setList( chargeRecordDao.selectOrderInfo(chargeRecord));
		return page;
	}

	@Override
	public Double getTotalMoneyByTimeAndEquipmentnumMerchantid(String begintime, String endtime,
			String equipmentnum, Integer merchantid, String ordernum) {
		return chargeRecordDao.getTotalMoneyByTimeAndEquipmentnumMerchantid(begintime, endtime, equipmentnum, merchantid, ordernum);
	}

	@Override
	public int updateNumberById(String ordernum,Integer number, Integer id, String durationtime, Double expenditure, Double refundMoney, String endTime, String refundTime) {
		return chargeRecordDao.updateNumberById(ordernum,number, durationtime, expenditure, refundMoney, endTime, refundTime, id);
	}

	@Override
	public ChargeRecord chargeRecordOneContinueEnd(Integer id) {
		return chargeRecordDao.chargeRecordOneContinueEnd(id);
	}

	@Override
	public List<ChargeRecord> chargeRecordList() {
		return chargeRecordDao.chargeRecordList();
	}

	@Override
	public Map<String, Object> selectcollect() {
		Parameters parameter = new Parameters();
		Map<String, Object> collectmap = chargeRecordDao.selectcollect(parameter);
		String newtime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
		String endtime = StringUtil.toDateTime("yyyy-MM-dd 23:59:59");
		parameter.setStartTime(newtime);
		parameter.setEndTime(endtime);
		collectmap.put("nowcollectmap", chargeRecordDao.selectcollect(parameter));
		return collectmap;
	}

	@Override
	public Double getTodayMoneyByUserid(String begintime,String endtime,Integer merchantid) {
		return chargeRecordDao.getTodayMoneyByUserid(begintime, endtime, merchantid);
	}

	@Override
	public ChargeRecord getChargeRecordById(Integer id) {
		return chargeRecordDao.getChargeRecordById(id);
	}

	@Override
	public List<String> findUserLatelyfourRecord(Integer uid) {
		return chargeRecordDao.findUserLatelyfourRecord(uid);
	}

	@Override
	public List<ChargeRecord> selectChargeAndContinueById(Integer id) {
		return chargeRecordDao.selectChargeAndContinueById(id);
	}

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	
	@Override
	public PageUtils<Parameters> selectChargeRecord(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		String startTime = CommUtil.toString(request.getParameter("startTime"));
		String endTime = CommUtil.toString(request.getParameter("endTime"));
		String continues = CommUtil.toString(request.getParameter("continue"));
		if(continues==null){
			if(null==startTime || startTime.equals(""))startTime = StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss", StringUtil.toDateTime(), 7);
			if(null==endTime || endTime.equals(""))endTime = StringUtil.getCurrentDateTime();
		}
		parameters.setStartTime(startTime);
		parameters.setEndTime(endTime);
		User user = CommonConfig.getAdminReq(request);
		Integer rank = user.getLevel();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(rank!=0) parameters.setUid(user.getId());//获取用户
		parameters.setParamete(CommUtil.toString(request.getParameter("orderId")));
		parameters.setOrder(CommUtil.toString(request.getParameter("ordernum")));
		parameters.setNickname(request.getParameter("username"));
		parameters.setDealer(request.getParameter("dealer"));
		parameters.setCode(request.getParameter("equipmentnum"));//设备号
		parameters.setType(StringUtil.getIntString(request.getParameter("paytype")).toString());//消费类型
		String number = request.getParameter("number");
		if (number != null && !number.equals("-1"))  parameters.setNumber(number);//订单状态:
		List<Map<String, Object>> chargemap = chargeRecordDao.selectChargeRecord(parameters);
		page.setTotalRows(chargemap.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		List<Map<String, Object>> chargemapli = chargeRecordDao.selectChargeRecord(parameters);
		page.setListMap(chargemapli);
		return page;
	}

	/**
	 * @Description： 根据参数记录Id（orderId）查询充电信息【含此次充电的续充
	 * @author： origin 
	 */
	@Override
	public List<ChargeRecord> chargeRecordRele(Integer orderId) {
		List<ChargeRecord> charge = chargeRecordDao.chargeRecordRele(orderId);
		return charge;
	}

	@Override
	public List<ChargeRecord> getRecordData(String ordernum, Integer paytype) {
		ChargeRecord record = new ChargeRecord();
		record.setOrdernum(CommUtil.toString(ordernum));
		record.setPaytype(CommUtil.toInteger(paytype));
		List<ChargeRecord> recordinfo = chargeRecordDao.selectOrderInfo(record);
		if(recordinfo==null) recordinfo = new ArrayList<ChargeRecord>();
		return recordinfo;                                       
	}

	
	@Override
	public Object chargeRecordInfo(Integer uid, Integer offset, Integer page) {
		try {
			int numPerPage = CommUtil.toInteger(offset);//条数
			int currentPage = CommUtil.toInteger(page);//页码
			PageUtils<ChargeRecord> pageut  = new PageUtils<>(numPerPage, currentPage);
			
			ChargeRecord chargeRecord = new ChargeRecord();
			chargeRecord.setUid(CommUtil.toInteger(uid));
			List<ChargeRecord> equi = chargeRecordDao.selectOrderInfo(chargeRecord);
			pageut.setTotalRows(equi.size());
			pageut.setTotalPages();
			pageut.setStart();
			pageut.setEnd();
			chargeRecord.setNumPerPage(pageut.getNumPerPage());
			chargeRecord.setStartIndex(pageut.getStartIndex());
			List<ChargeRecord> result = chargeRecordDao.selectOrderInfo(chargeRecord);
			for(ChargeRecord item : result){
				item.setBegintimes(StringUtil.toDateTime(item.getBegintime()));
				item.setEndtimes(StringUtil.toDateTime(item.getEndtime()));
			}
			pageut.setList(result);
			return pageut;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ChargeRecord> selectChargeNoReply() {
		return chargeRecordDao.selectChargeNoReply();
	}
	
	
	@Override
	public int selectConsumeQuantity(String equipmentnum,String begintime,String endtime){
		
			return chargeRecordDao.selectConsumeQuantity(equipmentnum, begintime, endtime);
		
	}

	

	@Override
	public Integer totalConsumeQuantity(int merchantid) {
		Integer totalConsume=chargeRecordDao.totalConsumeQuantity(merchantid);
		if(totalConsume!=null){
			return totalConsume;
		}
		return 0;
	}

	@Override
	public Integer todayConsumeQuantity(Integer merchantid, String begintime, String endtime) {
		if(merchantid  != null && begintime != null && !"".equals(begintime) && endtime != null && !"".equals(endtime)){
			Integer todayConsume=chargeRecordDao.todayConsumeQuantity(merchantid, begintime, endtime);
			if(todayConsume != null){
				return todayConsume;
			}else{
				return 0;
			}
		}else{
			return 0;
		}
	}

	@Override
	public Integer yesterdayConsumeQuantity(int merchantid, String begintime) {
		Integer yesterdayConsume=chargeRecordDao.yesterdayConsumeQuantity(merchantid, begintime);
		if(yesterdayConsume!=null){
			return yesterdayConsume;
		} 
		return 0;
	}

	@Override
	public List<Map<String, Object>> selectConsumeByTime(int merchantid, String begintime, String endtime) {
		List<Map<String, Object>> statis=chargeRecordDao.selectConsumeByTime(merchantid, begintime, endtime);
		return statis == null ? new ArrayList<Map<String, Object>>() : statis;
	}

	@Override
	public List<Map<String, Object>> selectEveryConsumeQuantity(int merchantid, String begintime, String endtime) {
		List<Map<String, Object>> list=chargeRecordDao.selectEveryConsumeQuantity(merchantid, begintime, endtime);
		 return list == null ? new ArrayList<Map<String, Object>>() : list;
	}

	@Override
	public List<Map<String, Object>> selectConsumeByArea(int merchantid, String begintime, String endtime) {
		List<Map<String, Object>> list=chargeRecordDao.selectConsumeByArea(merchantid, begintime, endtime);
		return list == null ? new ArrayList<Map<String, Object>>() : list;
	}
	
	/**
	 * @Description：   根据条件查询用户消费信息
	 * @author： origin 
	 * @return hd_realchargerecord
	 */
	@Override
	public List<Realchargerecord> realChargeRecordList(Integer orderId) {
		orderId = CommUtil.toInteger(orderId);
		List<Realchargerecord> realrecord = realchargerecordDao.realChargeRecordList(orderId, null, null, null, null);
		return realrecord == null ? new ArrayList<Realchargerecord>() : realrecord;
	}
	
	/**
	 * @Description：   根据条件查询数据的函数情况  1：根据充电id(chargeid)
	 * @author： origin 
	 * @return	maxeRecord  hd_realchargerecord
	 */
	@Override
	public Map<String, Object> functionRecord(Integer orderId) {
		try {
			Parameters arame = new Parameters();
			arame.setOrder(orderId.toString());
			Map<String, Object> powerdata = realchargerecordDao.functionRecord(arame);
			return powerdata = powerdata == null ? new HashMap<>() :powerdata;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}

	/**
	 * @Description： 根据订单id查询该订单的功率信息
	 * @param: orderId 订单id
	 * @author： origin   2019年10月8日 下午5:25:55 
	 */
	@Override
	public Map<String, Object> powerBrokenLine(Integer orderId) {
		Map<String, Object> map = new HashMap<>();
		ChargeRecord chargeRecord = chargeRecordDao.chargeRecordOne(orderId);
		if(chargeRecord==null) chargeRecord = new ChargeRecord();
		Integer orderidpar = orderId;
		Integer iscontinue = 0;
		if(CommUtil.toInteger(chargeRecord.getIfcontinue())!=0){//续充
			iscontinue =1;
			orderidpar = CommUtil.toInteger(chargeRecord.getIfcontinue());
		}
		Integer useelec = CommUtil.toInteger(chargeRecord.getConsumeQuantity());
		Integer usetime = CommUtil.toInteger(chargeRecord.getConsumeTime());
		map.put("paytype", CommUtil.toInteger(chargeRecord.getPaytype()));
		map.put("ordernum", CommUtil.toString(chargeRecord.getOrdernum()));
		map.put("port", CommUtil.toInteger(chargeRecord.getPort()));
		map.put("refundMoney", chargeRecord.getRefundMoney());
		try {
			List<Realchargerecord> realrecord = new ArrayList<>();
			Map<String, Object> powerinfo = new HashMap<>();
			
			if(orderidpar!=null && !orderidpar.equals("")){
				realrecord = realChargeRecordList(orderidpar);//所有的功率记录
				powerinfo = functionRecord(orderidpar);//最大、最小、平均功率
			}
			Realchargerecord realfisrt = new Realchargerecord();
			Realchargerecord reallast = new Realchargerecord();
			if(realrecord!=null && realrecord.size()>0){
				realfisrt = realrecord.get(0);
				reallast = realrecord.get(realrecord.size()-1);
			}
			List<ChargeRecord> chargelist = chargeRecordDao.chargeRecordRele(orderidpar);
			Integer include = 0;
			if(chargelist==null || chargelist.size()==0){
				chargelist = new ArrayList<>();
			}else if(chargelist.size()>1){
				include = 1;
			}
			List<Map<String, Object>> listobj = new ArrayList<Map<String, Object>>();
			Integer obtaintime = 0;
			Integer obtainelec = 0; 
			Double paymoney = 0.00;
			for(ChargeRecord item : chargelist){
				Map<String, Object> mapdata = new HashMap<>();
				mapdata.put("ordernum", item.getOrdernum());
				mapdata.put("begintime", item.getBegintime());
				mapdata.put("moany", item.getExpenditure());
				mapdata.put("ifcontinue", CommUtil.toInteger(item.getIfcontinue()));
				listobj.add(mapdata);
				obtaintime = obtaintime + CommUtil.toInteger(item.getDurationtime());
				obtainelec = obtainelec + CommUtil.toInteger(item.getQuantity());
				paymoney = paymoney + CommUtil.toDouble(item.getExpenditure());
			}
			Integer predicttime = CommUtil.toInteger(obtaintime-CommUtil.toInteger(reallast.getChargetime()));
//			if(useelec.equals(0)) useelec = CommUtil.toInteger(obtainelec-CommUtil.toInteger(reallast.getSurpluselec()));
			if(usetime<0) usetime =  CommUtil.toInteger(0 - usetime);
			if(useelec<0) useelec =  CommUtil.toInteger(0 - useelec);
			map.put("predicttime", predicttime);
			map.put("usetime", usetime);
			map.put("useelec", useelec);
			map.put("begintime", chargelist.get(0).getBegintime());
			map.put("endtime", chargelist.get(0).getEndtime());
			map.put("number", chargelist.get(0).getNumber());
			map.put("paymoney", paymoney);
			String devicenum = CommUtil.toString(chargelist.get(0).getEquipmentnum());
			map.put("devicenum", devicenum);
			Equipment equipment = equipmentService.getEquipmentById(devicenum);
			map.put("devicename", CommUtil.toString(equipment.getRemark()));
			if (equipment.getAid() != null && equipment.getAid() != 0) {
				Area area = areaService.selectByIdArea(equipment.getAid());
				map.put("areaname", area.getName());
			} else {
				map.put("areaname", null);
			}
			
			map.put("include", include);
			map.put("iscontinue", iscontinue);
			map.put("listcharge", listobj);
			map.put("orderId", orderidpar);
			map.put("mapfunc", powerinfo);
			map.put("mapinfo", map);
			map.put("realfisrt", realfisrt);
			map.put("reallast", reallast);
			map.put("realrecord", realrecord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	//=================================================================================
	/**
	 * separate
	 * @Description：充电记录信息查看
	 * @author： origin 
	 */
	@Override
	public Object chargeRecordData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			Integer isAlllData = CommUtil.toInteger(maparam.get("isAlllData"));

			Parameters parameters = new Parameters();

			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
			parameters.setParamete(CommUtil.toString(maparam.get("orderId")));
			parameters.setOrder(CommUtil.toString(maparam.get("ordernum")));
			parameters.setNickname(CommUtil.toString(maparam.get("usernick")));
			parameters.setRealname(CommUtil.toString(maparam.get("realname")));
			parameters.setPhone(CommUtil.toString(maparam.get("mobile")));
			parameters.setCode(CommUtil.toString(maparam.get("devicenum")));//设备号
			parameters.setState(CommUtil.toString(maparam.get("port")));
			String number = CommUtil.toString(maparam.get("orderstatus"));
			parameters.setNumber(number);//订单状态:
			List<Map<String, Object>> chargeData = chargeRecordDao.selectChargeRecord(parameters);
			page.setTotalRows(chargeData.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> chargeinfo = chargeRecordDao.selectChargeRecord(parameters);
			for(int i=0;i<chargeinfo.size();i++){
				Double payMoney=(Double) chargeinfo.get(i).get("paymoney")-(Double) chargeinfo.get(i).get("refund_money");
				chargeinfo.get(i).put("paymoney",payMoney);
			}
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			if(isAlllData.equals(1)){
				datamap.put("listdata", CommUtil.isListMapEmpty(chargeData));
				Integer datasize = CommUtil.toInteger(chargeData.size());
				if(datasize>10000){
					return CommUtil.responseBuildInfo(201, "当前条数为"+datasize+"条，超过最大导出条数（10000条），请缩短查询时间", datamap);
				}
			}else{
				datamap.put("listdata", CommUtil.isListMapEmpty(chargeinfo));
			}
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	/**
	 * separate
	 * @Description：查询充电记录中的折现图
	 * @author： origin 
	 */
	@Override
	public Object inquirePowerBrokenLine(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer orderId =  CommUtil.toInteger(maparam.get("orderid"));
			
			ChargeRecord chargeRecord = chargeRecordDao.chargeRecordOne(orderId);
			if(chargeRecord==null) chargeRecord = new ChargeRecord();
			
			Integer iscontinue = 0;
			if(CommUtil.toInteger(chargeRecord.getIfcontinue())!=0){//续充
				iscontinue =1;
				orderId = CommUtil.toInteger(chargeRecord.getIfcontinue());
			}
			
			Integer useelec = CommUtil.toInteger(chargeRecord.getConsumeQuantity());
			Integer usetime = CommUtil.toInteger(chargeRecord.getConsumeTime());
			datamap.put("ordernum", chargeRecord.getOrdernum());
			datamap.put("port", chargeRecord.getPort());
			datamap.put("refundMoney", chargeRecord.getRefundMoney());
			datamap.put("paytype", chargeRecord.getPaytype());

			List<Realchargerecord> realrecord = new ArrayList<>();
			Map<String, Object> powerinfo = new HashMap<>();
			if(orderId!=null && !orderId.equals("")){
				realrecord = realChargeRecordList(orderId);//所有的功率记录
				powerinfo = functionRecord(orderId);//最大、最小、平均功率
			}
			Realchargerecord realfisrt = new Realchargerecord();
			Realchargerecord reallast = new Realchargerecord();
			if(realrecord!=null && realrecord.size()>0){
				realfisrt = realrecord.get(0);
				reallast = realrecord.get(realrecord.size()-1);
			}

			Integer include = 0;
			List<Map<String, Object>> listobj = new ArrayList<Map<String, Object>>();
			List<ChargeRecord> chargelist = chargeRecordDao.chargeRecordRele(orderId);//有续充查询出续充和最初记录
			if(chargelist==null || chargelist.size()==0){
				chargelist = new ArrayList<>();
			}else if(chargelist.size()>1){
				include = 1;
			}
			Integer obtaintime = 0;
			Integer obtainelec = 0; 
			Double paymoney = 0.00;
			for(ChargeRecord item : chargelist){
				Map<String, Object> mapdata = new HashMap<>();
				mapdata.put("ordernum", item.getOrdernum());
				mapdata.put("begintime", item.getBegintime());
				mapdata.put("moany", item.getExpenditure());
				listobj.add(mapdata);
				mapdata.put("ifcontinue", CommUtil.toInteger(item.getIfcontinue()));
				obtaintime = obtaintime + CommUtil.toInteger(item.getDurationtime());
				obtainelec = obtainelec + CommUtil.toInteger(item.getQuantity());
				paymoney = paymoney + CommUtil.toDouble(item.getExpenditure());
			}
			Integer predicttime = CommUtil.toInteger(obtaintime-CommUtil.toInteger(reallast.getChargetime()));
			if(useelec.equals(0)) useelec = CommUtil.toInteger(obtainelec-CommUtil.toInteger(reallast.getSurpluselec()));
			if(usetime<0) usetime =  CommUtil.toInteger(0 - usetime);
			if(useelec<0) useelec =  CommUtil.toInteger(0 - useelec);
			datamap.put("predicttime", predicttime);
			datamap.put("usetime", usetime);
			datamap.put("useelec", useelec);
			datamap.put("begintime", chargelist.get(0).getBegintime());
			datamap.put("endtime", chargelist.get(0).getEndtime());
			datamap.put("paymoney", paymoney);
			String devicenum = CommUtil.toString(chargelist.get(0).getEquipmentnum());
			datamap.put("devicenum", devicenum);
			Equipment equipment = equipmentService.getEquipmentById(devicenum);
			datamap.put("devicename", CommUtil.toString(equipment.getRemark()));
			if (equipment.getAid() != null && equipment.getAid() != 0) {
				Area area = areaService.selectByIdArea(equipment.getAid());
				datamap.put("areaname", area.getName());
			} else {
				datamap.put("areaname", null);
			}
			datamap.put("include", include);
			datamap.put("iscontinue", iscontinue);
			datamap.put("listcharge", listobj);
			datamap.put("orderId", orderId);
			datamap.put("mapdata", powerinfo);//功率信息
//			datamap.put("realfisrt", realfisrt);
//			datamap.put("reallast", reallast);
			datamap.put("listdata", realrecord);
			List<Map<String, Object>> result = new ArrayList<>();
			if(realrecord.size()>0){
				ChargeRecord charge = chargeRecordOne(CommUtil.toInteger(orderId));
				Date begintime = charge.getBegintime();
				for(Realchargerecord pLog : realrecord){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("chargetime", pLog.getChargetime());
					map.put("surpluselec", pLog.getSurpluselec());
					map.put("power", pLog.getPower());
					String time = StringUtil.toDateTime("MM-dd HH:mm", pLog.getCreatetime());
					map.put("createtime", time);
					map.put("minuteTime", StringUtil.minuteTime(begintime, pLog.getCreatetime()));
					result.add(map);
				}
			}
			datamap.put("listpower", CommUtil.isListMapEmpty(result));
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	@Override
	public Integer selectMaxPowerByChargeid(Integer chargeid) {
		try {
			Integer maxPower = realchargerecordDao.selectMaxPowerByChargeid(chargeid);
			if (maxPower != null) {
				return maxPower;
			} else {
				return 0;
			} 
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @Description：测试数据 测试使用
	 * @param datamap
	 * @author： origin
	 * @createTime：2020年3月31日下午2:41:32
	 * @comment:
	 */
	@Override
	public void insertCeShiBatch(Map<String, Object> datamap) {
		chargeRecordDao.insertCeShiBatch(datamap);
	}

	@Override
	public void insertCeShiDataBatch(Date createtime, String out_trade_no, Integer dealid, Integer tourid,
			Integer total_fee, String remark, String comment) {
		chargeRecordDao.insertCeShiDataBatch( createtime, out_trade_no, dealid, tourid, total_fee, remark, comment);
	}

	@Override
	public List<ChargeRecord> select24HoursUnfinshByCode(String code) {
		return chargeRecordDao.select24HoursUnfinshByCode(code);
	}

	@Override
	public int delectRealChargeByTime(String createtime) {
		if (createtime == null || "".equals(createtime)) return 0;
		return realchargerecordDao.delectRealChargeByTime(createtime);
	}
	@Override
	public Map<String, Object> selectRecordByOrderNum(String orderNum){
		Map<String, Object> map = chargeRecordDao.selectRecordByOrderNum(orderNum);
		if(map == null) return new HashMap<String, Object>();
		return map;
	}

	@Override
	public void updateRecoderStatus(String orderNum) {
		chargeRecordDao.updateRecoderStatus(orderNum);
	}

	@Override
	public void chargeRefund(ChargeRecord chargeRecord) {
		// 获取订单ID,订单编号,支付的钱
		String orderNum = CommUtil.toString(chargeRecord.getOrdernum());
		Double payMoney = CommUtil.toDouble(chargeRecord.getExpenditure());
		Integer merId = CommUtil.toInteger(chargeRecord.getMerchantid());
		// 根据订单ID和订单号查询交易记录
		TradeRecord trade = new TradeRecord();
		trade.setOrdernum(orderNum);
		TradeRecord traderecord = tradeRecordDao.getTraderecordList(trade);
		if(traderecord == null) return;
		// 获取交易记录中的信息
		String deviceNum = CommUtil.toString(traderecord.getCode());
		Integer paySource = MerchantDetail.CHARGESOURCE;
		Integer payType = 12;
		Integer paystatus = MerchantDetail.REFUND;
		Integer aId = 0;
		// 变更减少设备的收益
		equipmentDao.updateEquEarn(deviceNum, payMoney, 0);
		// 变更充电记录订单为退款
		ChargeRecord record = new ChargeRecord();
		record.setNumber(1);
		record.setOrdernum(orderNum);
		chargeRecordDao.updateChargeRecode(record);
		// 查询设备获取设备的小区ID
		Equipment equipment = equipmentDao.getEquipmentById(deviceNum);
		// 设备存在小区减少小区下的收益
		if(equipment != null && equipment.getAid() != 0) aId = equipment.getAid();
		if(aId != 0) areaDao.updateAreaEarn(0, payMoney, aId, null, payMoney, null);
		// 设备存在合伙人减少合伙人的收益
		String comment = CommUtil.toString(traderecord.getComment());
		merchantDetailService.dealerIncomeRefund(comment, merId, payMoney, orderNum, paySource, payType, paystatus);
		// 添加退款的交易记录(交易记录只改变订单状态)
		trade.setMerid(traderecord.getMerid());
		trade.setUid(traderecord.getUid());
		trade.setManid(0);
		trade.setOrdernum(orderNum);
		trade.setMoney(traderecord.getMoney());
		trade.setMermoney(traderecord.getMermoney());
		trade.setManmoney(traderecord.getManmoney());
		trade.setCode(deviceNum);
		trade.setPaysource(paySource);
		trade.setPaytype(payType);
		trade.setStatus(paystatus);
		trade.setHardver(equipment.getHardversion());
		trade.setCreateTime(new Date());
		trade.setComment(comment);
		// 添加退款交易信息
		tradeRecordDao.insertToTradeRecord(trade);
	}

	/**
	 * @method_name: chargeRecordCompute
	 * @Description: 查询昨日收入的窗口投币、离线卡刷卡、在线卡刷卡数据
	 * @param merid:商户id   startTime:开始时间    endTime:结束时间
	 * @Author: origin  创建时间:2020年8月31日 下午5:49:36
	 * @common:   
	 */
	@Override
	public Map<String, Object> chargeRecordCompute(Integer merid, String startTime, String endTime) {
		try {
			Parameters parame = new Parameters();
			parame.setDealer(CommUtil.toString(merid));
			parame.setStartTime(startTime);
			parame.setEndTime(endTime);
			Map<String,Object>  resultMap = CommUtil.isMapEmpty(chargeRecordDao.chargeRecordCompute(parame));
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.isMapEmpty(null);
		}
	}

	@Override
	public Map<String, Object> queryChargeRecord(Integer uid, Integer status, Integer startnum) {
		Map<String,Object> map = new HashMap<>();
		try {
			List<ChargeRecordCopy> chargelist = chargeRecordDao.queryChargeRecord(uid, status, startnum * 10);
			if (chargelist == null) {
				chargelist = new ArrayList<>();
			}
			if (chargelist != null) {
				for (ChargeRecordCopy chargeRecordCopy : chargelist) {
					String equipmentnum = chargeRecordCopy.getEquipmentnum();
					int parseInt = Integer.parseInt(equipmentnum.substring(4, 6));
					if (parseInt >= 1 && parseInt <= 31) {
						chargeRecordCopy.setEquipemnttype(1);
					} else if (parseInt >= 51 && parseInt <= 81) {
						chargeRecordCopy.setEquipemnttype(2);
					} else {
						chargeRecordCopy.setEquipemnttype(1);
					}
				}
			}
			map.put("charginglist", chargelist);
			map.put("startnum", startnum + 1);
			map.put("listsize", chargelist.size());
			return CommUtil.responseBuildInfo(1000, "获取成功", map);
		} catch (Exception e) {
			return CommUtil.responseBuildInfo(1002, "系统异常", null);
		}
	}

	@Override
	public Map<String, Object> queryChargeRecordInfo(Integer id) {
		Map<String,Object> map = new HashMap<>();
		try {
			ChargeRecordCopy chargeRecordCopy = chargeRecordDao.queryChargeRecordInfo(id);
			if (chargeRecordCopy != null) {
				String equipmentnum = chargeRecordCopy.getEquipmentnum();
				int parseInt = Integer.parseInt(equipmentnum.substring(4, 6));
				if (parseInt >= 1 && parseInt <= 31) {
					chargeRecordCopy.setEquipemnttype(1);
				} else if (parseInt >= 51 && parseInt <= 81) {
					chargeRecordCopy.setEquipemnttype(2);
				} else {
					chargeRecordCopy.setEquipemnttype(1);
				}
				map.put("chargeinfo", chargeRecordCopy);
				return CommUtil.responseBuildInfo(1000, "获取成功", map);
			} else {
				return CommUtil.responseBuild(1001, "未查询到数据", null);
			}
		} catch (Exception e) {
			return CommUtil.responseBuild(1002, "系统异常", e.getMessage());
		}
	}

	@Transactional
	@Override
	public Map<String, Object> insertChargeRecord(String equipmentnum, int port, int ctrlWay, int ctrlParam, int chargeWay,
			Integer uid, Integer paytype, Double paymoney, String ordernum) {
		User user = userService.selectUserById(uid);
		if (user == null || user.getBalance() < paymoney) {
			return CommUtil.responseBuildInfo(1001, "余额不足", null);
		}
		WolfHttpRequest.sendChargePaydata(equipmentnum, port, uid, paymoney, ordernum);
		AllPortStatus allPortStatus = new AllPortStatus();
		allPortStatus.setEquipmentnum(equipmentnum);
		allPortStatus.setPort(port);
		allPortStatus.setPortStatus((byte) 2);
		allPortStatusService.updateAllPortStatus(allPortStatus);
		ChargeRecordCopy charge = new ChargeRecordCopy();
		charge.setOrdernum(ordernum);
		charge.setUid(uid);
		charge.setEquipmentnum(equipmentnum);
		charge.setPort(port);
		charge.setPaymoney(paymoney);
		charge.setCtrlWay(ctrlWay);
		charge.setCtrlParam(ctrlParam);
		charge.setChargeWay(chargeWay);
		Date date = new Date();
		charge.setCreateTime(date);
		charge.setPayTime(date);
		chargeRecordDao.insertChargeRecord(charge);
		User edituser = new User();
		edituser.setId(uid);
		double balance = CommUtil.subBig(user.getBalance(), paymoney);
		edituser.setBalance(balance);
		userService.updateUserById(edituser);
		generalDetailService.insertGenDetail(uid, 0, paymoney, 0.0, paymoney, balance, balance, 0.0, ordernum, date, 2, "充电");
		Map<String,Object> map = new HashMap<>();
		map.put("ordernum", ordernum);
		return CommUtil.responseBuildInfo(1000, "付款成功，余额修改成功", map);
	}

	@Override
	public Map<String, Object> stopChargeRecord(String ordernum, String begintime, String endtime,
			Double chargeMoney,Double serverMoney, Integer resultInfo, Integer useElec) {
		try {
			ChargeRecordCopy chargeRecordCopy = chargeRecordDao.selectChargeRecordInfo(ordernum);
			if (chargeRecordCopy != null) {
				Double paymoney = chargeRecordCopy.getPaymoney();
				double chargeAllMoney = CommUtil.addBig(chargeMoney, serverMoney);
				ChargeRecordCopy chargeRecord = new ChargeRecordCopy();
				chargeRecord.setId(chargeRecordCopy.getId());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date begintimeDate = sdf.parse(begintime);
				Date endtimeDate = sdf.parse(endtime);
				chargeRecord.setBeginTime(begintimeDate);
				chargeRecord.setEndTime(endtimeDate);
				chargeRecord.setChargemoney(chargeMoney);
				chargeRecord.setServerMoney(serverMoney);
				chargeRecord.setResultinfo(resultInfo);
				int usetime = (int) ((endtimeDate.getTime() - begintimeDate.getTime())/60/1000);
				chargeRecord.setUsetime(usetime);
				chargeRecord.setUseelec(useElec);
				chargeRecord.setStatus(1);
				if (chargeAllMoney == 0) {
					chargeRecord.setNumber(1);
				} else {
					chargeRecord.setNumber(2);
				}
				System.out.println("chargeRecord===" + JSON.toJSONString(chargeRecord));
//				if (paymoney > chargeAllMoney && chargeAllMoney >= 0) {
//					Double refundMoney = CommUtil.subBig(paymoney, chargeAllMoney);
//					Integer uid = chargeRecordCopy.getUid();
//					User user = userService.selectUserById(uid);
//					User edituser = new User();
//					edituser.setId(uid);
//					double balance = CommUtil.addBig(user.getBalance(), refundMoney);
//					edituser.setBalance(balance);
//					userService.updateUserById(edituser);
//					generalDetailService.insertGenDetail(uid, 0, refundMoney, 0.0, refundMoney, balance, balance, 0.0, ordernum, new Date(), 5, "充电");
//				}
//				chargeRecordDao.updateChargeRecord(chargeRecordCopy);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
