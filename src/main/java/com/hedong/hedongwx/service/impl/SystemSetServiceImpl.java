package com.hedong.hedongwx.service.impl;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.AuthorityService;
import com.hedong.hedongwx.service.CollectStatisticsService;
import com.hedong.hedongwx.service.FeescaleService;
import com.hedong.hedongwx.service.StatisticsService;
import com.hedong.hedongwx.service.SystemSetService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.JedisUtils;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.TempMsgUtil;

/**
 * @Description： 系统设置控制类
 * @author  origin  创建时间：   2019年9月11日 下午4:16:15  
 */
@Service
public class SystemSetServiceImpl implements SystemSetService{
	
	public final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AreaService areaService;
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private UserService userService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private FeescaleService feescaleService;
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private SystemSetService systemSetService;	
	@Autowired
	private CollectStatisticsService collectStatisticsService;

	/**
	 * @Description：根据商户id，type类型[1：商户收益  2：单个设备收益处理 3:商户所有设备处理] pastday:之前多少天开始计算到前一天  devicenum:设备号
	 * 				判断处理商户的汇总数据
	 * @author： origin
	 * @createTime：2020年4月16日下午4:30:59
	 */
	@Override
	public Object selfDynamicCollect(Integer merid, Integer type, Integer pastday, String devicenum) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			User user = userService.selectUserById(merid);
			if(user==null){
				return CommUtil.responseBuildInfo(101, "用户不存在", datamap);
			}
			Date userTime = user.getCreateTime();
			String creatTime = CommUtil.toDateTime(userTime);
			for(int i=1; i<=pastday; i++){
				String time = CommUtil.getPastDate(i);
				String timedata = StringUtil.aftertoStringTime(time);
				Integer comparenum = CommUtil.compareStringTime(timedata, creatTime);
				if(comparenum>=0){
					collectStatisticsService.incomeSettlement( type, merid, time, devicenum);
				}
			}
			return CommUtil.responseBuildInfo(200, "结算完成", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**
	 * @Description：根据商户id，判断处理商户近七天的汇总数据
	 * @author： origin
	 * @createTime：2020年4月16日下午4:30:59
	 */
	@Override
	public Object selfmotionCollect(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer merid =  CommUtil.toInteger(maparam.get("merid"));
			Integer type =  CommUtil.toInteger(maparam.get("type"));
			
			User user = userService.selectUserById(merid);
			if(user==null){
				return CommUtil.responseBuildInfo(101, "用户不存在", datamap);
			}
			Date userTime = user.getCreateTime();
			String creatTime = CommUtil.toDateTime(userTime);
			for(int i=1; i<=7; i++){
				String time = CommUtil.getPastDate(i);
				String timedata = StringUtil.aftertoStringTime(time);
				Integer comparenum = CommUtil.compareStringTime(timedata, creatTime);
				if(comparenum>=0){
					if(CommUtil.toInteger(type)==1){
						//经销商收益汇总
						statisticsService.dealerIncomecollect(merid, time);
					}else if(CommUtil.toInteger(type)==2){
						//经销商所拥有的设备收益汇总
						statisticsService.dealerDeviceIncomeDispose(merid, time);
					}
				}
			}
			return CommUtil.responseBuildInfo(200, "结算完成", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**
	 * separate
	 * @Description：系统模板
	 * @author： origin 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object systemTemplateData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		Map<String, Object> systemFeescaleMap=feescaleService.getSystemFeescale();
		try {
//			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			//充电模板
			Object chargesontem = templateService.getSonTemp(0);
			TemplateParent tempcharge = new TemplateParent();
			tempcharge.setName("十路智慧款系统模板");
			tempcharge.setRemark("");
			tempcharge.setCommon1("");
			tempcharge.setPermit(1);
			tempcharge.setWalletpay(2);
			tempcharge.setIfalipay(1);
			tempcharge.setGather(chargesontem);
			datamap.put("tempcharge", tempcharge);
			List<TemplateParent>  templatelist = templateService.getParentTemplateListByMerchantidwolf(0, null);
			for(TemplateParent tem : templatelist){
				Integer status = tem.getStatus();
				//模板类型 0充电模板  1离线卡模板     2模拟投币模板（脉冲模板） 3钱包模板   4在线卡模板
				if(status==0){
					datamap.put("temelectriccar", tem);
				}else if(status==1){
					datamap.put("temoffline", tem);
				}else if(status==2){
					datamap.put("temincoins", tem);
				}else if(status==3){
					datamap.put("temwallet", tem);
				}else if(status==4){
					datamap.put("temonline", tem);
				}else if(status==6){
					List<TemplateSon> tempson = (List<TemplateSon>) tem.getGather();
//					List<TemplateSon> tempson = templateService.getSonTemplateLists(tem.getId());
					tempson = tempson == null ? new ArrayList<>() : tempson;
					List<TemplateSon> tempower = new ArrayList<>();
					List<TemplateSon> temtime =  new ArrayList<>();
					List<TemplateSon> temmoney =  new ArrayList<>();
					for(TemplateSon item : tempson){
						Integer type = CommUtil.toInteger(item.getType());
						if(type.equals(1)){
							tempower.add(item);
						}else if(type.equals(2)){
							temtime.add(item);
						}else if(type.equals(3)){
							temmoney.add(item);
						} 
					}
					Map<String, Object> testMap = JSON.parseObject(JSON.toJSONString(tem), Map.class);
					testMap.put("gather1", tempower);
					testMap.put("gather2", temtime);
					testMap.put("gather3", temmoney);
					datamap.put("templatev3", testMap);
				}
			}
			templatelist.add(tempcharge);
			datamap.put("chargesontem", chargesontem);
			datamap.put("templatelist", templatelist);
			System.out.println("系统收费模板"+systemFeescaleMap);
			datamap.put("payTemData", systemFeescaleMap);
			Map<String, String> hgetAll = JedisUtils.hgetAll("sysTime");
			if (hgetAll == null || hgetAll.isEmpty()) {
				hgetAll = new HashMap<String, String>();
				hgetAll.put("sysOffline", 8 * 60 + "");
				hgetAll.put("handOffline", "30");
				JedisUtils.hmset("sysTime", hgetAll);
			}
			datamap.put("sysTime", hgetAll);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	
	/**
	 * separate
	 * @Description：系统结算汇总
	 * @author： origin Settlement
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
//			User user = CommonConfig.getAdminReq(request);
			String type =  CommUtil.toString(maparam.get("type"));
			String time =  CommUtil.toString(maparam.get("time"));
			if(CommUtil.toInteger(type)==1){
				//经销商收益汇总
				statisticsService.dealIncoCollectAll(time);
			}else if(CommUtil.toInteger(type)==2){
				//经销商所拥有的设备收益汇总
				statisticsService.dealDeviIncoCollectAll(time);
			}else{
				
			}
			return CommUtil.responseBuildInfo(200, "结算完成", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	/**
	 * separate
	 * @Description：系统结算个人汇总
	 * @author： origin Settlement
	 */
	@Override
	public Object calculateAloneCollect(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			String type =  CommUtil.toString(maparam.get("type"));
			String phone =  CommUtil.toString(maparam.get("phone"));
			String time =  CommUtil.toString(maparam.get("time"));
			
			User user = userService.existAdmin(phone);
			if(user==null){
				return CommUtil.responseBuildInfo(101, "用户不存在", datamap);
			}
//			String creatTime = CommUtil.toString(user.getCreateTime());
//			Integer comparenum = CommUtil.compareStringTime(creatTime, time);
			Date userTime = user.getCreateTime();
			String creatTime = CommUtil.toDateTime(userTime);
			String timedata = StringUtil.aftertoStringTime(time);
			Integer comparenum = CommUtil.compareStringTime(timedata, creatTime);
			if(comparenum>=0){
				Integer deaid = CommUtil.toInteger(user.getId());
				if(CommUtil.toInteger(type)==1){
					//经销商收益汇总
					statisticsService.dealerIncomecollect(deaid, time);
				}else if(CommUtil.toInteger(type)==2){
					//经销商所拥有的设备收益汇总
					statisticsService.dealerDeviceIncomeDispose(deaid, time);
				}
				return CommUtil.responseBuildInfo(200, "结算完成", datamap);
			}else{
				return CommUtil.responseBuildInfo(203, "商户汇总时间大于注册时间", datamap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	/**
	 * @Description：获取服务电话
	 * @author： origin
	 * @createTime：2019年12月26日下午3:34:19
	 */
	@Override
	public String getServicePhone(Integer tempid, Integer areaid, Integer dealid) {
		try {
			tempid = CommUtil.toInteger(tempid);
			areaid = CommUtil.toInteger(areaid);
			dealid = CommUtil.toInteger(dealid);
			String phone = null;
			if(!tempid.equals(0)){
				TemplateParent temppare = templateService.getParentTemplateOne(tempid);
				phone = CommUtil.toString(temppare.getCommon1());
			}
//			if(phone==null && !areaid.equals(0)){
//				List<Map<String, Object>> result =  areaService.selectAreaRelev(areaid);
//				for(Map<String, Object> item : result){
//					Integer type = CommUtil.toInteger(item.get("type"));
//					if(type.equals(3)){
//						Integer manageid =  CommUtil.toInteger(item.get("partid"));
//						User manageuser = userService.selectUserById(manageid);
//						phone = CommUtil.toString(manageuser.getPhoneNum());
//					}
//				}
//			}
			if(phone==null && !dealid.equals(0)){
				User dealer = userService.selectUserById(dealid);
				phone = CommUtil.toString(dealer.getServephone());
				if(phone==null) CommUtil.toString(phone = dealer.getPhoneNum());
			}
			return phone;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @Description：获取服务模板
	 * @author： origin
	 * @createTime：2019年12月26日下午3:34:19
	 */
	@Override
	public Object getServiceTemp(Integer tempid, Integer type, Integer areaid, Integer dealid) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			tempid = CommUtil.toInteger(tempid);
			type = CommUtil.toInteger(type);
			areaid = CommUtil.toInteger(areaid);
			dealid = CommUtil.toInteger(dealid);
			List<TemplateSon> tempson = new ArrayList<TemplateSon>();
			String phone = null;
			if(!tempid.equals(0)){
				TemplateParent temppare = templateService.getParentTemplateOne(tempid);
				tempson = templateService.getSonTemplateLists(tempid);
			}
			
			if(tempson.size()==0 && !areaid.equals(0)){
				
			}
//			if(tempson.size()==0 && !areaid.equals(0)){
//				List<Map<String, Object>> result =  areaService.selectAreaRelev(areaid);
//				for(Map<String, Object> item : result){
//					Integer type = CommUtil.toInteger(item.get("type"));
//					if(type.equals(3)){
//						Integer manageid =  CommUtil.toInteger(item.get("partid"));
//						User manageuser = userService.selectUserById(manageid);
//						phone = CommUtil.toString(manageuser.getPhoneNum());
//					}
//				}
//			}
//			if(phone==null && !dealid.equals(0)){
//				User dealer = userService.selectUserById(dealid);
//				phone = CommUtil.toString(dealer.getPhoneNum());
//			}
			return CommUtil.responseBuildInfo(200, "获取服务电话成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	/**
	 * @Description：获取服务模板
	 * @author： origin
	 * @createTime：2019年12月26日下午3:34:24
	 */
	@Override
	public Object getServeTemp(String val, Integer type, Integer areaid, Integer dealid) {
		return dealid;
	}
	
	/**
	 * @Description：付款成功通知
	 * @author： origin
	 * @createTime：2019年12月27日下午3:10:13
	 */
	@Override
	public void sendMmessagePaySucceed(String ordernum, Integer orderid, Integer paysource, Integer dealid, Integer uid,
			Double paymoney, String ordertime, String devicenum, String target, String devicename, Integer deviceaid,
			String areaaddress, String servicphone) {
		
		String firstcharge = null;
		if(paysource.equals(3)){//离线充值
			firstcharge = "您好，充值成功，欢迎您的使用";
		}else if(paysource.equals(5)){//钱包充值
			firstcharge = "您好，充值成功，欢迎您的使用";
		}else if(paysource.equals(6)){//在线卡充值
			firstcharge = "您好，充值成功，欢迎您的使用";
		}else{
			firstcharge = "您好，充值成功，欢迎您的使用。";
		}
		
			
		StringBuffer deviceinfo = new StringBuffer();
		StringBuffer devicedata = new StringBuffer();
		if(areaaddress!=null) deviceinfo.append("地址信息："+areaaddress);
		if(devicename!=null) deviceinfo.append(" ").append(devicename);
		if(devicenum!=null) devicedata.append("设备信息："+devicenum);
		
		firstcharge = firstcharge + "\r\n\r\n" + deviceinfo+ "\r\n\r\n" + devicedata;
		String orderstatus = "支付成功";
		String remark = TempMsgUtil.MESSAGE_CHARGEPAY;
		String url = TempMsgUtil.URL_MESSAGE;
		User tourist = userService.selectUserById(uid);
		String oppenid = CommUtil.toString(tourist.getOpenid());
		TempMsgUtil.orderCharge(firstcharge, oppenid, ordernum, paymoney, orderstatus, ordertime, servicphone, remark, url);
		if(authorityService.authorSwitch(dealid, 3)){
//				TempMsgUtil.oederNew( first, merid, strtime, "微信", status,  "充电",  ordernum,  remark, url);
		}
	}
	
	/**
	 * @Description：充电付款成功通知
	 * @author： origin
	 * @createTime：2019年12月27日下午3:10:13
	 */
	@Override
	public void sendMmessageCharge(String ordernum, Integer orderid, Integer paysource, Integer dealid, Integer uid, 
			Double paymoney, String ordertime, String devicenum, String port, String devicename, Integer deviceaid, 
			String areaname, String servicphone) {
		StringBuffer first = new StringBuffer(TempMsgUtil.FIRST_PAYSUCCEED);
		StringBuffer deviceinfo = new StringBuffer();
		StringBuffer devicedata = new StringBuffer();
		if(areaname!=null){
			deviceinfo.append("地址信息：   "+areaname).append(" ");
		}
		if(devicename!=null && deviceinfo.length()==0){
			deviceinfo.append("地址信息：   "+devicename);
		}else if(devicename!=null && deviceinfo.length()>0){
			deviceinfo.append(devicename);
		}
		if(devicenum!=null) devicedata.append("设备信息：   "+devicenum);
		if(port!=null) devicedata.append("-").append(port);
		
		if(deviceinfo.length()>0) first.append("\r\n").append(deviceinfo);
		if(devicedata.length()>0) first.append("\r\n").append(devicedata);
		
		String orderstatus = "支付成功";
		String remark = TempMsgUtil.MESSAGE_CHARGEPAY;
		String url = TempMsgUtil.URL_MESSAGE;
		User tourist = userService.selectUserById(uid);
		String oppenid = CommUtil.toString(tourist.getOpenid());
		TempMsgUtil.orderCharge(first.toString(), oppenid, ordernum, paymoney, orderstatus, ordertime, servicphone, remark, url);
		if(authorityService.authorSwitch(dealid, 3)){
//				TempMsgUtil.oederNew( first, merid, strtime, "微信", status,  "充电",  ordernum,  remark, url);
		}
	}
	
	
	/**
	 * @Description：充电结束通知
	 * @author： origin
	 * @createTime：2019年12月28日下午5:28:20
	 */
	@Override
	public void sendMmessChargeEnd(String ordernum, Integer orderid, Integer resultinfo, Integer paysource, Integer dealid, 
			Integer uid, Double paymoney, String ordertime, String devicenum, String port, String devicename, Integer deviceaid, 
			String areaaddress, Integer devicetempid) {
		
		String servicphone = systemSetService.getServicePhone(devicetempid, deviceaid, dealid);
		String firstdata = TempMsgUtil.FIRST_ENDMESSAGE;
		String resultdata = null;
		if(resultinfo.equals(0)){//0X00 购买的充电时间或电量用完了
			resultdata = "充电完成";
		}else if(resultinfo.equals(1)){//0X01 用户手动停止或拔插头
			resultdata = "充电插头松动或已拔掉";
		}else if(resultinfo.equals(2)){//0X02 充电满了，自动停止
			resultdata = "充满自停";
		}else if(resultinfo.equals(3)){//0X03 超功率自停
			resultdata = "超功率自停";
		}else if(resultinfo.equals(4)){//0X04 远程断电
//			resultdata = "远程断电（注：该退款到平台钱包里。）";
			resultdata = "充电线脱落.";
		}else if(resultinfo.equals(5)){//0X05
			resultdata = " 刷卡断电";
		}else if(resultinfo.equals(11)){//0X0B 设备或端口号出现问题，被迫停止
			resultdata = "充电完成(11)";
		}else if(resultinfo.equals(255)){//255 日志结束充电
			resultdata = "充电完成(255)";
		}else{//其它
			resultdata = "充电完成。";
		}
		StringBuffer devicedata = new StringBuffer();
		if(areaaddress!=null) devicedata.append(areaaddress).append(" ");
		if(devicename!=null) devicedata.append(devicename).append(" ");
		if(devicenum!=null) devicedata.append(devicenum);
		if(port!=null) devicedata.append("-").append(port);
		
		String remark = "本次充电已结束，如有疑问请咨询商户:"+servicphone;
		String url = TempMsgUtil.URL_MESSAGE;
		User tourist = userService.selectUserById(uid);
		String oppenid = CommUtil.toString(tourist.getOpenid());
		TempMsgUtil.tempChargeEndChoose(firstdata, oppenid, ordernum, devicedata.toString(), resultdata, 
			ordertime, CommUtil.toString(paymoney), remark, url);
	}


}
