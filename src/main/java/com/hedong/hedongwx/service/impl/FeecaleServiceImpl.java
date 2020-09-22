package com.hedong.hedongwx.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.AreaDao;
import com.hedong.hedongwx.dao.DealerAuthorityDao;
import com.hedong.hedongwx.dao.EquipmentDao;
import com.hedong.hedongwx.dao.FeescaleDao;
import com.hedong.hedongwx.dao.MerchantDetailDao;
import com.hedong.hedongwx.dao.UserDao;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.AreaRelevance;
import com.hedong.hedongwx.entity.DealerAuthority;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.FeescaleRecord;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.FeescaleService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Service
@Transactional
public class FeecaleServiceImpl implements FeescaleService {
	private final Logger logger = LoggerFactory.getLogger(FeecaleServiceImpl.class);
	@Autowired
	private FeescaleDao feescaleDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private EquipmentDao equipmentDao;
	@Autowired
	private MerchantDetailDao merchantDetailDao;
	@Autowired
	private DealerAuthorityDao dealerAuthorityDao;
	
	/**
	 * 查询资金池的总收益
	 */
	@Override
	public Map<String, Object> selectFeescaleTotalEarnings() {
		return feescaleDao.selectFeescaleTotalEarnings();
	}
	/**
	 * 查询缴费订单的详情
	 */
	@Override
	public Object selectFeescaleDetails(HttpServletRequest request){
		Object result=null;
		String ordernum = request.getParameter("ordernum");
		Map<String,Object> datamap=new HashMap<>();
		if(ordernum != null && !"".equals(ordernum)){
			//缴费详情的信息
			List<Map<String, Object>> maps=feescaleDao.selectFeescaleDetails(ordernum);
			if(!maps.isEmpty()){
				//商家的总缴费金额
				datamap.put("messsdata", maps);
			}
			result=CommUtil.responseBuildInfo(200, "成功", datamap);
		}else{
			result=CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return result;
	}
	/**
	 * 	修改系统收费信息
	 */
	@Override
	public boolean updateSystemFeescale(Map<String, Map<String,Double>> netMap, Map<String, Map<String,Double>> blueMap) {
		if(!netMap.isEmpty() && !blueMap.isEmpty()){
			//將map轉換成對象
			for (Entry<String, Map<String, Double>> entry  : netMap.entrySet()) {
				//systemNet=00
				String systemNet=entry.getKey();
				Map<String, Double> sysBlue=(Map<String, Double>) entry.getValue();
				if(systemNet!=null&&!"".equals(systemNet)&&!sysBlue.isEmpty()){
					for (Entry<String, Double> entry2 : sysBlue.entrySet()) {
						FeescaleRecord list=new FeescaleRecord();
						list.setNetType(systemNet);
						list.setEquipmentType(entry2.getKey());
						list.setPrice(entry2.getValue());
						feescaleDao.updateSystemFeescale(list);
						logger.info("修改--------"+systemNet+"----"+entry2.getKey()+"--"+entry2.getValue());
					}
				}else{
					return false;
				}
			}
			for (Entry<String, Map<String, Double>> blueMap1  : blueMap.entrySet()) {
				//blueMap1=01
				String systemBlue=blueMap1.getKey();
				Map<String, Double> sysBlue=(Map<String, Double>) blueMap1.getValue();
				if(systemBlue!=null && !"".equals(systemBlue) && !sysBlue.isEmpty()){
					for (Entry<String, Double> sysBlue1 : sysBlue.entrySet()) {
						FeescaleRecord list1=new FeescaleRecord();
						list1.setNetType(systemBlue);
						list1.setEquipmentType(sysBlue1.getKey());
						list1.setPrice(sysBlue1.getValue());
						feescaleDao.updateSystemFeescale(list1);
						logger.info("修改--------"+systemBlue+"----"+sysBlue1.getKey()+"--"+sysBlue1.getValue());
					}
				}else{
					return false;
				}
			}
			return true;
		}
		return false;
	}
	/**
	 * 获取系统收费标准
	 */
	@Override
	public Map<String,Object> getSystemFeescale() {
		Map<String,Object> systemFeescale=new HashMap<String,Object>();
		Map<String, Object> netType=new HashMap<String, Object>();
		Map<String, Object> blueType=new HashMap<String,Object>();
		List<Map<String, Object>> systemList=feescaleDao.getSystemFeescale();
		//遍历系统的收费标准
		for (int i = 0; i < systemList.size(); i++) {
			String net=(String)systemList.get(i).get("nettype");
			String equipment=(String)systemList.get(i).get("equipmenttype");
			if("00".equals(net)){
				netType.put(equipment, systemList.get(i).get("feescale"));
			}else if("01".equals(net)){
				blueType.put(equipment, systemList.get(i).get("feescale"));
			}
		}
		systemFeescale.put("00", netType);
		systemFeescale.put("01", blueType);
		return systemFeescale;
	}
	
	/**
	 * PC根据商家的id查询商家的收费模板
	 */
	@Override
	public Object getFeescalBymerid(Integer merId,Model model) {
		Object result = null;
		if(merId!=null){
			//商家收费标准
			List<Map<String, Object>> merList=feescaleDao.getFeescalBymerid(merId);
			//系统收费标准
			List<Map<String,Object>> systemList=feescaleDao.getSystemFeescale();
			Map<String, Object> netType=new HashMap<String, Object>();
			Map<String, Object> blueType=new HashMap<String,Object>();
			//遍历系统的收费标准
			if(!merList.isEmpty()){
				for (int i = 0; i < systemList.size(); i++) {
					//获取所有网络
					String net=(String)systemList.get(i).get("nettype");
					//获取设备
					String equipment=(String)systemList.get(i).get("equipmenttype");
					if("00".equals(net)){
						netType.put(equipment, systemList.get(i).get("feescale"));
					}else if("01".equals(net)){
						blueType.put(equipment, systemList.get(i).get("feescale"));
					}
				}
				//遍历商家的收费标准
				for (int j = 0; j < merList.size(); j++) {
					String merNet=(String)merList.get(j).get("nettype");
					String merEquipment=(String)merList.get(j).get("equipmenttype");
					if("00".equals(merNet)){
						netType.put(merEquipment, merList.get(j).get("feescale"));
					}else if("01".equals(merNet)){
						blueType.put(merEquipment, merList.get(j).get("feescale"));
					}
				model.addAttribute("00", netType);
				model.addAttribute("01", blueType);
				}
			}else{
				//遍历系统的收费标准
				for (int i = 0; i < systemList.size(); i++) {
					String net=(String)systemList.get(i).get("nettype");
					String equipment=(String)systemList.get(i).get("equipmenttype");
					if("00".equals(net)){
						netType.put(equipment, systemList.get(i).get("feescale"));
					}else if("01".equals(net)){
						blueType.put(equipment, systemList.get(i).get("feescale"));
					}
				}
				model.addAttribute("00", netType);
				model.addAttribute("01", blueType);
			}	
			return JSON.toJSONString(model);
		}else{
			return result = CommUtil.responseBuild(400, "参数错误", "");
		}
	}
	/**
	 * 手机端获取商家收费标准
	 * 将多个Map转换成两个Map
	 */
	@Override
	public List<Map<String, Object>> getMerFeescale(Integer merid) {
		if(merid != null){
			// 查询商家的收费标准
			List<Map<String, Object>> merList = feescaleDao.getFeescalBymerid(merid);
			// 商家收费标准不存在
			logger.info("商家收费标准"+merList);
			if(merList != null && !merList.isEmpty() && merList.size() > 0){
				// 判断商家的收费标准是否和系统的收费标准一致
				List<Map<String, Object>> systemList=feescaleDao.getSystemFeescale();
				// 一致使用商家的收费标准
				if(merList.size() == systemList.size()){
					// 遍历
					Map<String, Object> netType = new HashMap<String, Object>();
					Map<String, Object> blueType = new HashMap<String,Object>();
					List<Map<String, Object>> fee = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < merList.size(); i++) {
						//获取系统网络
						String net=(String)merList.get(i).get("nettype");
						//获取系统蓝牙
						String equipment=(String)merList.get(i).get("equipmenttype");
						if("00".equals(net)){
							netType.put(equipment, merList.get(i).get("feescale"));
						}else if("01".equals(net)){
							blueType.put(equipment, merList.get(i).get("feescale"));
						}
					}
					fee.add(netType);
					fee.add(blueType);
					return fee;
				}else{
					Map<String, Object> netType = new HashMap<String, Object>();
					Map<String, Object> blueType = new HashMap<String,Object>();
					List<Map<String, Object>> fee = new ArrayList<Map<String, Object>>();
					// 遍历
					for (int i = 0; i < systemList.size(); i++) {
						//获取系统网络
						String net=(String)systemList.get(i).get("nettype");
						//获取系统蓝牙
						String equipment=(String)systemList.get(i).get("equipmenttype");
						if("00".equals(net)){
							netType.put(equipment, systemList.get(i).get("feescale"));
						}else if("01".equals(net)){
							blueType.put(equipment, systemList.get(i).get("feescale"));
						}
					}
					//遍历商家的收费标准
					for (int j = 0; j < merList.size(); j++) {
						String merNet=(String)merList.get(j).get("nettype");
						String merEquipment=(String)merList.get(j).get("equipmenttype");
						if("00".equals(merNet)){
							netType.put(merEquipment, merList.get(j).get("feescale"));
						}else if("01".equals(merNet)){
							blueType.put(merEquipment, merList.get(j).get("feescale"));
						}
					}
					logger.info("商家网络收费标准"+netType);
					logger.info("商家蓝牙收费标准"+blueType);
					fee.add(netType);
					fee.add(blueType);
					logger.info("商家的总收费标准"+fee);
					return fee;
				}
			}else{
				List<Map<String, Object>> systemList=feescaleDao.getSystemFeescale();
				Map<String, Object> netType = new HashMap<String, Object>();
				Map<String, Object> blueType = new HashMap<String,Object>();
				List<Map<String, Object>> fee = new ArrayList<Map<String, Object>>();
				// 遍历
				for (int i = 0; i < systemList.size(); i++) {
					//获取系统网络
					String net=(String)systemList.get(i).get("nettype");
					//获取系统蓝牙
					String equipment=(String)systemList.get(i).get("equipmenttype");
					if("00".equals(net)){
						netType.put(equipment, systemList.get(i).get("feescale"));
					}else if("01".equals(net)){
						blueType.put(equipment, systemList.get(i).get("feescale"));
					}
				}
				fee.add(netType);
				fee.add(blueType);
				logger.info("系统收费标准"+systemList);
				return fee;
			}
		}else{
			return new ArrayList<Map<String ,Object>>();
		}
	}

	@Override
	/**
	 * 根据商家的id更改设备的收费标准
	 */
	public boolean updateFeescaleBymerid(Integer id,String userName, Map<String, Map<String,Double>> netType,Map<String ,Map<String,Double>> blueType) {
		//商家的id不能为空
		if(id !=null){
			//map不能为空
			if(!netType.isEmpty() && !blueType.isEmpty()){
				//遍历网络类型
				for (Entry<String, Map<String, Double>> entry  : netType.entrySet()) {
					//systemNet=00
					String merNet=entry.getKey();
					Map<String,Double> merNetMap=entry.getValue();
					//判断
					logger.info(merNet+"正常******"+merNetMap+"正常******");
					if(merNet != null && !merNetMap.isEmpty()){
						for (Entry<String, Double> entry1 : merNetMap.entrySet()) {
							String merNetMapKey=entry1.getKey();
							Double merNetMapValue=entry1.getValue();
							logger.info(merNetMapKey+"正常"+merNetMapValue+"正常");
							if(merNetMapKey!=null && !"".equals(merNetMapKey) && merNetMapValue!=null){
								//查询是否存在
								logger.info("网络"+merNet+"-----"+"型号"+merNetMapKey);
								Map<String,Object> map = feescaleDao.selectMerFeescaleByid(id, merNet, merNetMapKey);
								//不存在插入,這裏有空指針異常
								// map = map != null ?  map : new HashMap<String,Object>();
								logger.info("查询的参数"+map);
								logger.info((map == null)+"為真表示插入");
								if(map == null){
									logger.info(map+"插入"+id+"---"+userName+"----"+merNet+"---"+merNetMapKey+"--"+merNetMapValue);
									feescaleDao.insertFeescal(id, userName, merNet, merNetMapKey, merNetMapValue);
								}else{
									logger.info(map+"存在更新"+id+"---"+merNet+"---"+merNetMapKey+"--"+merNetMapValue);
									//更新
									feescaleDao.updateFeescaleBymerid(id, merNet, merNetMapKey, merNetMapValue);
									logger.info(map+"更新完成"+merNet+"---"+merNetMapKey+"--"+merNetMapValue+"成功");
								}
							}else{
								return false;
							}
						}
						logger.info("完成一波循環");
					}else{
						return false;
					}
				}
				//遍历蓝牙类型
				for (Entry<String, Map<String, Double>> entry2  : blueType.entrySet()) {
					//blueType=01
					String merBlue=entry2.getKey();
					Map<String, Double> merBlueMap=entry2.getValue();
					if(merBlue != null && !"".equals(merBlue) && !merBlueMap.isEmpty()){
						//遍历蓝牙类型
						for (Entry<String, Double> entry3 : merBlueMap.entrySet()) {
							String merBlueMapKey=entry3.getKey();
							Double merBlueMapValue=entry3.getValue();
							if(merBlueMapKey!=null && !"".equals(merBlueMapKey) && merBlueMapValue!=null){
								//查询
								Map<String,Object> map1=feescaleDao.selectMerFeescaleByid(id, merBlue, merBlueMapKey);
								//map1 = map1 != null ?  map1 : new HashMap<String,Object>();
								logger.info("网络模块的标准"+map1);
								if(map1 == null){
									logger.info("插入"+id+"---"+merBlue+"---"+merBlueMapKey+"--"+merBlueMapValue);
									feescaleDao.insertFeescal(id, userName, merBlue, merBlueMapKey, merBlueMapValue);
								}else{
									logger.info("存在更新"+id+"---"+merBlue+"---"+merBlueMapKey+"--"+merBlueMapValue);
									feescaleDao.updateFeescaleBymerid(id, merBlue, merBlueMapKey, merBlueMapValue);
								}
							}else{
								return false;
							}
						}
					}else{
						return false;
					}
				}
				return true;//遍历完成，参数正常返回true
			}
			return false;
		}
		return false;
	}	
	//根据商家的id去查询商家的小区(2020-4-27加注释)
	@Override
	public List<Area> getAllAreaAndPartner(Area area) {
		List<Area> aread = areaDao.selectByArea(area);
		// 遍历获取小区id
		for (Area item : aread) {
			if (item.getId() != null) {
				Map<String, Object> equData = areaDao.selectEquDataByaid(item.getId());
				if(equData != null){
					item.setEqucount(CommUtil.toString(equData.get("equcount")));
					item.setExpiredEquNum(CommUtil.toInteger(equData.get("expirednum")));
					item.setAlmostExEquNum(CommUtil.toInteger(equData.get("almoexnum")));
				}else{
					item.setEqucount("0");
					item.setExpiredEquNum(0);
					item.setAlmostExEquNum(0);
				}
				// 根据小区号查询合伙人数 
				Integer partnerNum = feescaleDao.getAllPartner(item.getId());
				if(partnerNum != null){
					item.setManid(partnerNum);
				}else{
					item.setManid(0);
				}
			}
		}
		return aread;
	}

	@Override
	/**
	 * 商家设置自动支付和分摊费用
	 */
	public Object updateSwitch(Integer merid, Integer autoPay, Integer apportion, Integer autowithdraw) {
		Object result = null;
		if (merid != null) {
			// 查询商家有按钮信息
			DealerAuthority authority = dealerAuthorityDao.selectAuthority(null, merid);
			// 无按钮信息就添加
			if(authority == null){
				DealerAuthority dealerAuthority = new DealerAuthority();
				dealerAuthority.setMerid(merid);
				dealerAuthorityDao.insertAuthority(dealerAuthority);
			}
			// 查询商家未提现的钱,不能为负数。
			User user = userDao.selectUserById(merid);
			if (user != null && user.getEarnings()> 0 || autowithdraw != null) {
				// 开启自动续费
				if (autoPay != null) {
					logger.info("商家设置自动续费");
					Area area = new Area();
					area.setMerid(merid);
					List<Area> listArea = areaDao.selectByArea(area);
					// 开启
					if (autoPay==1&&listArea != null && !listArea.isEmpty()) {
						// 商家有小区但是没有合伙人也能开启自动续费
						for (Area area2 : listArea) {
							Parameters parame = new Parameters();
							logger.info(area2.getId() + "商家的小区");
							parame.setState(CommUtil.toString(area2.getId()));
							parame.setType(CommUtil.toString(2));
							List<Map<String, Object>> resultli = CommUtil.isListMapEmpty(areaDao.selectaRearelInfo(parame));
							//商家的小区有合伙人暂时不支持自动续费
							if (!resultli.isEmpty()) {
								result = CommUtil.responseBuild(201, "有合伙人暂时不支持自动续费", "");
								return result;
							}
						}
						feescaleDao.updateSwitch(user.getId(), autoPay, null,null);
						result = CommUtil.responseBuild(200, "成功", "");
						return result;
					} else {
						//关闭
						feescaleDao.updateSwitch(user.getId(), autoPay, null,null);
						result = CommUtil.responseBuild(200, "成功", "");
						return result;
					}
					// 开启分摊
				} else if (apportion != null) {
					logger.info("商家设置分摊");
					Area area = new Area();
					area.setMerid(merid);
					List<Area> listArea = areaDao.selectByArea(area);
					// 商家有小区存在合伙人可以开启
					if (apportion ==1 && listArea != null && !listArea.isEmpty()) {
						for (Area area2 : listArea) {
							Parameters parame = new Parameters();
							logger.info(area2.getId() + "商家的小区");
							parame.setState(CommUtil.toString(area2.getId()));
							parame.setType(CommUtil.toString(2));
							List<Map<String, Object>> resultli = CommUtil
									.isListMapEmpty(areaDao.selectaRearelInfo(parame));
							logger.info("商家小区有没有合伙人" + resultli);
							if (!resultli.isEmpty()) {
								logger.info("商家的小区存在合伙人");
								feescaleDao.updateSwitch(user.getId(), null, apportion,null);
								result = CommUtil.responseBuild(200, "分摊设置成功", "");
								return result;
							}
						}
						result = CommUtil.responseBuild(201, "商家小区没有合伙人", "");
						return result;
						//关闭分摊
					}else if(apportion ==0 ){
						feescaleDao.updateSwitch(user.getId(), null, apportion, null);
						result = CommUtil.responseBuild(200, "关闭分摊", "");
						return result;
					}
					result = CommUtil.responseBuild(201, "商家没有小区", "");
					return result;
				// 设置自动提现
				}else if(autowithdraw != null){
					// 商家开启自动提现时判断是否存在真是姓名
					logger.info("商家设置自动提现");
					if(user.getRealname() != null && !"".equals(user.getRealname())){
						// 用户有真实姓名可以开启
						if(autowithdraw.equals(1)){
							feescaleDao.updateSwitch(merid, null, null,autowithdraw);
							result = CommUtil.responseBuild(200, "开启成功", "");
							return result;
						}else{
							feescaleDao.updateSwitch(merid, null, null,autowithdraw);
							result = CommUtil.responseBuild(200, "关闭成功", "");
							return result;
						}
					}else{
						result = CommUtil.responseBuild(201, "真实姓名不存在", "");
						return result;
					}
				}else {
					result = CommUtil.responseBuild(201, "修改参数错误", "");
					return result;
				}
			} else {
				result = CommUtil.responseBuild(201, "余额不足", "");
				return result;
			}
		} else {
			result = CommUtil.responseBuild(201, "商家id錯誤", "");
			return result;
		}
	}


	// 设置商家最大的负收入
	@Override
	public boolean setMerMaxNegative(Integer merid, Double maxNegative) {
		if (merid != null && maxNegative != null) {
			feescaleDao.setMerMaxNegative(merid, maxNegative);
			return true;
		} else {
			return false;
		}
	}

	// 设置设备的到期时间
	@Override
	public boolean setEquipmentExpire(String code, String expireTime) {
		// TODO Auto-generated method stub
		if (code != null && !"".equals(code) && expireTime != null && !"".equals(expireTime)) {
			feescaleDao.setEquipmentExpire(code, expireTime);
			return true;
		} else {
			return false;
		}
	}


	/*// 用户扫码判断设备是否到期,自动缴费
	@Override
	public Object merRenew(String code) {
		Double merFeescale = 30.00;// 默认费用30.0
		Date nowDate = new Date();
		// 查询设备的到期时间
		Equipment equipment = equipmentDao.getEquipmentAndAreaById(code);
		if (equipment != null && nowDate != null) {
			// 判断设备到期
			if (!nowDate.before(equipment.getExpirationTime())) {
				// 根据设备号查询出该设备所属商户
				UserEquipment userEquipment = userEquipmentDao.getDivideintoByCode(code);
				Integer merId = userEquipment.getUserId();// 商家id
				String netType = equipment.getHardversion();// 网络型号
				String equipmentType = equipment.getHardversionnum();// 设备型号
				// 用户扫码判断设备是否到期,触发自动缴费
				// 默认费用30.0
				// 查询判断设备的到期时间 包括设备信息
				// 根据设备号查询出该设备所属商户 商户id
				// 根据商户id查询当前设备的型号收费标准 有就直接用,没有就默认30.00
				// 查看商家是否开启自动续费,只有自动续费的设备才满足条件,设备到期手动续费的设备就提示暂时不能使用
				// 判断设备有没有合伙人,没有就按照商家的收费标准扣费
				-------设备没有合伙人--------
				// 商家开启分摊了,但是当前的设备不存在小区信息,没有合伙人分摊费用
				// 直接根据当前型号的设备收费标准进行扣费
				// 扣费成功将扣费的信息记录
				// 设备有合伙人,中间添加合伙人也需要缴纳费用
				// 查询设备所在的小区的合伙人,分成比,根据合伙人、分成比,计算每个合伙人应该缴纳多少钱
				// 扣除商家及合伙人的费用,更新商家及合伙人的收益
				// 更新超级管理员的收入
				// 生成订单信息,插入设备的缴费记录
				if (merId != null) {
					// 根据商家id查询设备的型号收费标准
					Map<String, Object> merFeescaleMap = feescaleDao.selectMerFeescaleByid(merId, netType,
							equipmentType);
					// 商家有自己的收费标准就用,没有就使用默认30.00
					if (!merFeescaleMap.isEmpty()) {
						merFeescale = (Double) merFeescaleMap.get("feescale");
					}
					// 查看商家是否开启自动续费
					DealerAuthority dealerAuthority = dealerAuthorityDao.selectAuthority(null, merId);
					// 开启自动和分摊
					if (dealerAuthority != null && dealerAuthority.getAutopay() == 1
							&& dealerAuthority.getApportion() == 1) {
						// 判断设备有没有合伙人,没有就按照商家的收费标准计费
						// 查询设备所在的小区的合伙人,分成比,根据合伙人、分成比,设备的型号,收费的标准计算费用
						// 计算当前设备费用
						// 扣除商家及合伙人的费用
						// 添加超级管理员的收入
						// 生成订单信息,插入设备的缴费记录
						// 开启自动,未开启分摊
					} else if (dealerAuthority != null && dealerAuthority.getAutopay() == 1
							&& dealerAuthority.getApportion() == 0) {
						// 扣除商家的设备费用
						// 添加超级管理员的收入
						// 更新设备的到期时间
						// 生成订单信息,插入设备的缴费信息
					} else {
						return "设备到期请及时为设备续费";
					}
				}
				return null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}*/

	

	// 获取未绑定的设备,并展示设备的收费标准
	@Override
	public List<Equipment> getUnbindEquipment(Integer merId) {
		List<Equipment> equlist = equipmentDao.selectUnbindEqulistOrderyByExpire(merId);
		if (equlist != null) {
			return equlist;
		} else {
			return new ArrayList<Equipment>();
		}
	}
	//商家为小区下的设备缴费
	@Override
	@Transactional
	public Object merPayment(List<User> users, List<Equipment> equipmentNum,Integer paytype,Integer aid) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS");
		Object result=null;
		//记录缴费的总金额
		Double superIncome=0.00;
		if(!users.isEmpty() && !equipmentNum.isEmpty() && paytype!=null && aid != null){
				// 判断商家及合伙人的收益
				for (int i = 0; i < users.size(); i++) {
					//判断用户的参数是否正常
					logger.info("用户参数"+users);
					logger.info("设备参数"+equipmentNum);
					logger.info("A-a开始判断所有商家的收益"+"-----------"+dateFormat.format(System.currentTimeMillis()));
					if(users.get(i).getId() != null && users.get(i).getEarnings() != null && users.get(i).getPayMonet() != null && users.get(i).getRank() != null){
						// 查询用户
						User user=userDao.selectUserById(users.get(i).getId());
						// 用户未提现的钱
						Double userBalance=user.getEarnings();
						// 商家姓名
						String userName=user.getUsername();
						// 用户的openid
						String openId=user.getOpenid();
						// 获取用户支付的金额
						Double payMonet=users.get(i).getPayMonet();
						// 查询用户未提现的钱大于0且大于缴费的金额
						logger.info("商家"+userName+"商家未提现的钱"+userBalance);
						logger.info("商家的openId"+openId);
						if(userBalance >0 && userBalance > users.get(i).getPayMonet() && openId != null && !"".equals(openId)){
							// 用户未提现的钱去减去支付金额
							users.get(i).setBalance(CommUtil.toDouble(userBalance-payMonet));
							// 设置用户的oppenId
							users.get(i).setOpenid(openId);
							// 商家姓名
							users.get(i).setUsername(userName+"");
						} else{
							// 余额不足返回
							result=CommUtil.responseBuild(100, user.getUsername()+"余额不足", "");
							return result;
						}
					}else{
						// 参数错误返回
						result=CommUtil.responseBuild(402, "提交数据异常", "");
						return result;
					}
				}
				//---------------------------------------------------------------
				logger.info("A判断所有商家的收益正常"+"-----------"+dateFormat.format(System.currentTimeMillis()));
				//捕获异常回滚
				try {
					// 根据openid更新商家的未提现的钱
					for (int i = 0; i < users.size(); i++) {
						userDao.updateBalanceByOpenid(null, null, users.get(i).getOpenid(),users.get(i).getBalance());
						superIncome=superIncome+CommUtil.toDouble(users.get(i).getPayMonet());
					}
					//---------------------------------------------------------------------------------------------
					logger.info("B更新商家的收益成功"+"-----------"+dateFormat.format(System.currentTimeMillis()));
					// 更新总资金池收益
					logger.info("超级管理员的收益"+superIncome);
					userDao.updateUserEarnings(1, CommUtil.toDouble(superIncome), 72);
					// 查询总资金池
					User user=userDao.selectUserById(72);
					//记录总资金的收益
					String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
					Integer random=(int)(Math.random()*90000)+10000;
					String feescaleRecord1=format+random;
					MerchantDetail merchantDetail=new MerchantDetail();
					merchantDetail.setMerid(72);
					merchantDetail.setMoney(superIncome);//支出或者收入的钱
					merchantDetail.setBalance(user.getEarnings());//记录支出或收入变动后的
					merchantDetail.setOrdernum(feescaleRecord1);
					merchantDetail.setPaysource(8);
					merchantDetail.setCreateTime(new Date());
					merchantDetail.setPaytype(1);
					merchantDetail.setStatus(1);
					// 添加总资金变动明细
					merchantDetailDao.insertMerEarningDetail(merchantDetail);
					//---------------------------------------------------------------------------------------------
					logger.info("C更新总资金池的收益成功"+"-----------"+dateFormat.format(System.currentTimeMillis()));
					// 设备到期时间增加一年
					for (int i = 0; i < equipmentNum.size(); i++) {
						String code=equipmentNum.get(i).getCode();
						if(code!=null && !"".equals(code) && equipmentNum.get(i).getTotalMoney() != null){
							logger.info("更新"+code+"到期时间");
							//设备时间为空,缴费更新不能成功,必须先要绑定才能缴费,前端判断时间为空不能点击提交ok
							equipmentDao.updateExpirationTime(code);
						}else{
							result=CommUtil.responseBuild(401, "设备信息错误", "");
							return result;
						}
					}
					//---------------------------------------------------------------------------------------------
					logger.info("D更新设备的到期时间"+"-----------"+dateFormat.format(System.currentTimeMillis()));
				} catch (Exception e) {
					e.printStackTrace();
					result=CommUtil.responseBuild(401, "扣费失败", "");
					return result;
				}
				logger.info("扣费成功,记录缴费信息");
				//记录缴费信息
				//---------------------------------------------------------------------------------------------
				logger.info("E开始执行插入缴费记录"+"-----------"+dateFormat.format(System.currentTimeMillis()));
				insertFeescaleRecord(users,equipmentNum,paytype,aid);
				result=CommUtil.responseBuild(200, "扣费成功", "");
				return result;
		}else{
			logger.info("商家及合伙人的信息"+users+"设备的信息"+equipmentNum);
			result=CommUtil.responseBuild(400, "提交信息错误", "");
			return result;
		}
	}
	
	// 添加缴费记录和商家收入记录
	@Override
	public void insertFeescaleRecord(List<User> users ,List<Equipment> equipmentNum,Integer paytype,Integer aid){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS");
		int merId=0;//商家的id
		String merName="";
		// 生成订单号
		String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		Integer random=(int)(Math.random()*90000)+10000;
		String feescaleRecord=format+random;
		if(!users.isEmpty() && !equipmentNum.isEmpty()){
			//遍历设备,计算每个人为设备缴费的详情
			//---------------------------------------------------------------------------------------------
			logger.info("F开始遍历计算每个人为设备付的钱"+"-----------"+dateFormat.format(System.currentTimeMillis()));
			for (int j = 0; j < equipmentNum.size(); j++) {
				// 商家分成比
				Double merPercent=0.00;
				// 合伙人为每个设备的缴费信息记录
				List<AreaRelevance> listUser= new ArrayList<AreaRelevance>();
				for (int i = 0; i < users.size(); i++) {
					//商家
					if(users.get(i).getRank()!=null && users.get(i).getRank()==2){
						//商家id
						merId=users.get(i).getId();
						//商家分成比由前台传过来
						merPercent=users.get(i).getEarnings();
						merName =users.get(i).getUsername();
						// 大于1有合伙缴费
					}else if(users.size() > 1 && users.get(i).getEarnings() !=null ){
						// 把合伙人添加到一个集合
						AreaRelevance areaRelevance=new AreaRelevance();
						// 分成比
						Double percent=users.get(i).getEarnings();
						// 合伙人缴费金额=设备缴费金额*合伙人的分成
						Double fee=CommUtil.toDouble(equipmentNum.get(j).getTotalMoney()*percent);
						// 合伙人姓名
						String userName=CommUtil.toString(users.get(i).getUsername());
						areaRelevance.setPercent(percent);
						areaRelevance.setPartid(users.get(i).getId());
						areaRelevance.setRemark(fee+"");
						areaRelevance.setPartName(userName+"");
						listUser.add(areaRelevance);
					}
					//---------------------------------------------------------------------------------------------
					logger.info("G结束遍历计算每个人为设备付的钱"+"-----------"+dateFormat.format(System.currentTimeMillis()));
				}
				//---------------------------------------------------------------------------------------------
				logger.info("H准备插入的缴费记录数据"+"-----------"+dateFormat.format(System.currentTimeMillis()));
				FeescaleRecord record=new FeescaleRecord();
				record.setAid(aid);
				record.setMerName(merName);//商家姓名
				record.setMerId(merId);//商家id
				record.setMerPayMoney(CommUtil.toDouble(merPercent * equipmentNum.get(j).getTotalMoney()));//商家支付的钱
				record.setEquipmentNum(equipmentNum.get(j).getCode());//设备号
				record.setOrderNum(feescaleRecord);//订单号
				record.setRenewal(12);//续约时间
				record.setPrice(equipmentNum.get(j).getTotalMoney());//设备应缴金额
				record.setCreateTime(new Date());
				record.setPayType(paytype);//缴费的方式
				record.setNote(JSON.toJSONString(listUser));//合伙人缴费的详情
				record.setReviceId(72);//收款人id
				record.setState(1);//订单状态:1钱包缴费得正常订单，0:微信缴费得预订单
				// 插入缴费记录
				feescaleDao.insertFeescaleRecord(record);
				//---------------------------------------------------------------------------------------------
				logger.info("I插入缴费记录成功"+"-----------"+dateFormat.format(System.currentTimeMillis()));
			}
			//---------------------------------------------------------------------------------------------
			logger.info("J开始执行插入用户资金变动明细"+"-----------"+dateFormat.format(System.currentTimeMillis()));
			// 记录用户资金变动明细
			for (int i = 0; i < users.size(); i++) {
				String format1 = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
				Integer random1=(int)(Math.random()*90000)+10000;
				String feescaleRecord1=format1+random1;
				MerchantDetail merchantDetail=new MerchantDetail();
				merchantDetail.setMerid(users.get(i).getId());//用户id
				merchantDetail.setMoney(users.get(i).getPayMonet());//支付金额
				merchantDetail.setBalance(users.get(i).getBalance());//缴费后的收益余额
				merchantDetail.setOrdernum(feescaleRecord1);//资金变动的订单号
				merchantDetail.setPaysource(8);//设备缴费
				merchantDetail.setCreateTime(new Date());//订单创建的时间
				merchantDetail.setPaytype(1);//钱包支付
				merchantDetail.setStatus(2);//订单为正常
				// 添加资金变动明细
				merchantDetailDao.insertMerEarningDetail(merchantDetail);
			}
			//---------------------------------------------------------------------------------------------
			logger.info("K完成插入用户资金变动明细"+"-----------"+dateFormat.format(System.currentTimeMillis()));
		}
	}
	// 查看缴费记录
	@Override
	public Object selectFeescaleRecord(HttpServletRequest request) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			//条数
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			//页数
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<FeescaleRecord> page  = new PageUtils<>(numPerPage, currentPage);
			FeescaleRecord feescaleRecord = new FeescaleRecord();
			String startTime = CommUtil.toString(request.getParameter("startTime"));
			String endTime = CommUtil.toString(request.getParameter("endTime"));
			startTime = startTime==null ? StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss",  StringUtil.toDateTime(), 7) : startTime;
			endTime = endTime==null ? StringUtil.getCurrentDateTime() : endTime;
			User user = CommonConfig.getAdminReq(request);
			Integer rank = CommUtil.toInteger(user.getRank());
			//设置参数
			if(!rank.equals(0)) feescaleRecord.setMerId(user.getId()); 
			feescaleRecord.setEquipmentNum(CommUtil.toString(maparam.get("code")));
			feescaleRecord.setOrderNum(CommUtil.toString(maparam.get("ordernum")));
			feescaleRecord.setPayType((Integer)maparam.get("paytype"));
			feescaleRecord.setMerName(CommUtil.toString(maparam.get("usernick")));
			feescaleRecord.setPhone(CommUtil.toString(maparam.get("mobile")));
			feescaleRecord.setBegintime(CommUtil.toString(startTime));
			feescaleRecord.setEndtime(CommUtil.toString(endTime));
			feescaleRecord.setStartIndex(page.getStartIndex());
			feescaleRecord.setNumPerPage(page.getNumPerPage());
			System.out.println("参数"+feescaleRecord);
			//总条数
			Integer totalCount = feescaleDao.selectFeescaleRecordCount(feescaleRecord);
			//分页展示数据
			List<Map<String, Object>> maps = feescaleDao.selectFeescaleRecord(feescaleRecord);
			page.setListMap(maps);;
			page.setTotalRows(totalCount);
			page.setTotalPages();
			page.setLastIndex();
			page.setStartIndex();
			page.setStart();
			page.setEnd();
			dataMap.put("dateMap", page);
			CommUtil.responseBuildInfo(200, "成功", dataMap);
			return dataMap;
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", dataMap);
			return dataMap;
		}
	}
	@Override
	// 插入微信缴费记录
	public void insertWxFeescaleRecord(FeescaleRecord feescaleRecord){
		feescaleDao.insertFeescaleRecord(feescaleRecord);
	}
	//根据订单号查询微信缴费记录
	@Override
	public List<Map<String, Object>> selectWxFeescaleRecord(String ordernum) {
		if(ordernum != null && !"".equals(ordernum)){
			List<Map<String, Object>> maps = feescaleDao.selectWxFeescaleRecord(ordernum);
			return maps;
		}else{
			return new ArrayList<Map<String, Object>>();
		}
	}
	//根据订单号变更微信支付订单得状态
	@Override
	public void updateFeescaleRecordStatue(String ordernum) {
		if(ordernum != null && !"".equals(ordernum)){
			feescaleDao.updateFeescaleRecordStatue(ordernum);
		}
	};
	// 商家手机端查看缴费记录
	@Override
	public Object merSelectFeescaleRecord(HttpServletRequest request){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取商家的id
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return new  ArrayList<Map<String, Object>>();
		}
		//查询缴费记录
		FeescaleRecord feescaleRecord = new FeescaleRecord();
		feescaleRecord.setMerId(user.getId());
		// 用户的订单数据
		List<Map<String, Object>> list = feescaleDao.selectFeescaleRecord(feescaleRecord);
		// 订单编号
		List<Map<String,Object>> listOrderNum = feescaleDao.selectFeescaleRecordNum(feescaleRecord);
		// 遍历
		if(!list.isEmpty() && !listOrderNum.isEmpty()){
			List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < listOrderNum.size(); i++) {
				Map<String, Object> dataMap = new ConcurrentHashMap<String, Object>();
				StringBuffer code = new StringBuffer();
				Double money = 0.00;
				String orderNum = (String) listOrderNum.get(i).get("ordernum");
				Integer renewal = (Integer) listOrderNum.get(i).get("renewal");
				Integer paytype = (Integer) listOrderNum.get(i).get("paytype");
				String createtime = dateFormat.format((Date) listOrderNum.get(i).get("createtime"));
				// 比较订单号是否相同
				for (int j = 0; j < list.size(); j++) {
					// 如果订单号相同
					String nextOrderNum = (String) list.get(j).get("ordernum");
					if(nextOrderNum.equals(orderNum)){
						code.append(list.get(j).get("equipmentnum")+"，");
						money = CommUtil.addBig(money, (Double)list.get(j).get("price"));
					}
				}
				code.replace(code.length()-1, code.length(), "");
				dataMap.put("ordernum", orderNum);
				dataMap.put("money", money);
				dataMap.put("code", code);
				dataMap.put("renewal", renewal);
				dataMap.put("paytype", paytype);
				dataMap.put("createtime", createtime);
				listMap.add(dataMap);
				}
				return listMap;
		}else{
			return new  ArrayList<Map<String, Object>>();
		}
	}
	@Override
	public Map<String, Object> testPCIndex(HttpServletRequest request) {
		// 判断用户是否登陆
		//User session = (User) request.getSession().getAttribute("user");
		//System.out.println("用户信息=="+request.getSession().getAttribute("user"));
		//System.out.println("用户信息=="+request.getSession().getAttribute("admin"));
		//if (session != null){
			// 判断用户是否登陆
			// 从session中获取用户的ID
			// 如果用户拥有超级管理的权限就可以看到所有数据
			// 超级管理员查询首页数据慢
			// 把历史数据计算保存到缓存中
			// 定时器触发计算昨天的数据加到历史数据中
			Map<String, Object> indexPage = new HashMap<String, Object>();
			// 获取参数
			String merUserId = request.getParameter("merId");
			System.out.println("参数"+merUserId);
			Integer merId = null;
			if(merUserId != null)merId = Integer.parseInt(merUserId);
			System.out.println("商家的ID=="+merId);
			// 1-PC端首页设备数据
			Map<String, Object> equData = equipmentDao.homePageEquip(merId);
			if (equData != null){
				Integer equipmentNum = CommUtil.toInteger(equData.get("equipmentNum"));
				Integer onlineNum = CommUtil.toInteger(equData.get("onlineNum"));
				Integer bingedNum = CommUtil.toInteger(equData.get("bingedNum"));
				Integer offlineNum = equipmentNum - onlineNum;
				Integer unBingedNum = equipmentNum - bingedNum;
				indexPage.put("equipmentNum",equipmentNum);
				indexPage.put("onlineNum",onlineNum);
				indexPage.put("bingedNum",bingedNum);
				indexPage.put("offlineNum",offlineNum);
				indexPage.put("unBingedNum",unBingedNum);
			}else{
				indexPage.put("equipmentNum",0);
				indexPage.put("onlineNum",0);
				indexPage.put("bingedNum",0);
				indexPage.put("offlineNum",0);
				indexPage.put("unBindedNum",0);
			}

			//2-PC端首页用户数据
			Map<String, Object> userData = equipmentDao.homePageUser(merId);
			if (userData != null) {
				indexPage.put("userNum",CommUtil.toInteger(userData.get("userNum")));
				indexPage.put("merNum",CommUtil.toInteger(userData.get("merNum")));
				indexPage.put("earn",CommUtil.toDouble(userData.get("earn")));
			}else {
				indexPage.put("userNum",0);
				indexPage.put("merNum",0);
				indexPage.put("earn",0.00);
			}
			
			//3-PC端首页获取缴费信息和已提现总额,提现总手续费,待确认提现金额,和手续费
			Map<String, Object> feecaleData = equipmentDao.homePageFeescale(merId);
			if (feecaleData != null){
				indexPage.put("feescaleEarns",CommUtil.toDouble(feecaleData.get("feescaleEarns")));
				indexPage.put("wallentFeescale",CommUtil.toDouble(feecaleData.get("wallentFeescale")));
				indexPage.put("wxFeescale",CommUtil.toDouble(feecaleData.get("wxFeescale")));
				indexPage.put("totalExtract",CommUtil.toDouble(feecaleData.get("totalExtract")));
				indexPage.put("totalService",CommUtil.toDouble(feecaleData.get("totalService")));
				indexPage.put("extrac",CommUtil.toDouble(feecaleData.get("extrac")));
				indexPage.put("service",CommUtil.toDouble(feecaleData.get("service")));
			}else{
				indexPage.put("feescaleEarns",0.00);
				indexPage.put("wallentFeescale",0.00);
				indexPage.put("wxFeescale",0.00);
				indexPage.put("totalExtract",0.00);
				indexPage.put("totalService",0.00);
				indexPage.put("extrac",0.00);
				indexPage.put("service",0.00);
			}

			//4-PC端首页今日收益数据
			Map<String, Object> earnData =equipmentDao.homePageTodayEarn(merId);
			if (earnData != null){
				Double tWechatMoney = CommUtil.toDouble(earnData.get("tWechatMoney"));
				Double tAlipayMoney = CommUtil.toDouble(earnData.get("tAlipayMoney"));
				Double tUnionMoney = CommUtil.toDouble(earnData.get("tUnionPayMoney"));
				Double tRefWechatMoney = CommUtil.toDouble(earnData.get("tRefWechatMoney"));
				Double tRefAlipayMoney = CommUtil.toDouble(earnData.get("tRefAlipayMoney"));
				Double tRefUnionPayMoney = CommUtil.toDouble(earnData.get("tRefUnionPayMoney"));
				//  今日总收益
				Double totalMoney = tWechatMoney + tAlipayMoney + tUnionMoney;
				//  今日退款
				Double refMoney = tRefWechatMoney + tRefAlipayMoney + tRefUnionPayMoney;
				// 今日在线净收益
				Double onlineMoney = CommUtil.subBig(totalMoney, refMoney);
				System.out.println("今日总收益"+totalMoney);
				System.out.println("今日总退款"+refMoney);
				indexPage.put("todayTotalMoney",totalMoney);
				indexPage.put("todayRefMoney",refMoney);
				indexPage.put("todayOnlineMoney",onlineMoney);
			}else{
				indexPage.put("todayTotalMoney",0.00);
				indexPage.put("todayRefMoney",0.00);
				indexPage.put("todayOnlineMoney",0.00);
			}
			// 5-PC端首页历史总收益数据
			Map<String, Object> totalEarnData =equipmentDao.homePageTotalEarn(merId);
			if (totalEarnData != null){
				Double wxMoney = CommUtil.toDouble(totalEarnData.get("wxMoney"));
				Double aliMoney = CommUtil.toDouble(totalEarnData.get("aliMoney"));
				Double unionMoney = CommUtil.toDouble(totalEarnData.get("unionMoney"));
				Double rWxMoney = CommUtil.toDouble(totalEarnData.get("rWxMoney"));
				Double rAliMoney = CommUtil.toDouble(totalEarnData.get("rAliMoney"));
				Double rUnionMoney = CommUtil.toDouble(totalEarnData.get("rUnionMoney"));
				Double earn = CommUtil.toDouble(totalEarnData.get("earn"));
				// 历史总收益
				Double totalMoney = wxMoney + aliMoney + unionMoney;
				// 在线退款
				Double refOnlineMoney = rWxMoney + rAliMoney + rUnionMoney;
				// 在线收款
				Double onlineMoney = CommUtil.subBig(totalMoney,refOnlineMoney);
				System.out.println("totalMoney=="+totalMoney);
				System.out.println("refOnlineMoney=="+refOnlineMoney);
				indexPage.put("totalMoney",totalMoney);
				indexPage.put("refOnlineMoney",refOnlineMoney);
				indexPage.put("onlineMoney",onlineMoney);
				indexPage.put("earn",earn);
			}else {
				indexPage.put("totalMoney",0.00);
				indexPage.put("refOnlineMoney",0.00);
				indexPage.put("onlineMoney",0.00);
				indexPage.put("earn",0.00);
			}
			return CommUtil.responseBuild(200,"成功",indexPage);
		/*}else{
			return CommUtil.responseBuild(400, "请先登陆", "失败");
		}*/
	};
}
