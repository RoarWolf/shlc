package com.hedong.hedongwx.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.AreaDao;
import com.hedong.hedongwx.dao.CodeSystemParamDao;
import com.hedong.hedongwx.dao.EquipmentDao;
import com.hedong.hedongwx.dao.RoleDao;
import com.hedong.hedongwx.dao.TemplateDao;
import com.hedong.hedongwx.dao.TemplateSonDao;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.CodeSystemParam;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class TemplateServiceImpl implements TemplateService{


	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TemplateDao templateDao;
	@Autowired
	private TemplateSonDao templateSonDao;
	@Autowired
	private EquipmentDao equipmentDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private CodeSystemParamDao codeSystemParamDao;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	/**
	 * @Description： 根据设备类型信息和商户id查询指定类型设备使用的服务电话
	 * @param: val:数据信息，如设备版本号，小区id等    dealid:商户id、 type:类型 1 设备；2 在线卡； 3 钱包； 4 包月
	 * @author： origin   2019年10月8日 下午3:54:55 
	 */
	@Override
	public Object genServicePhone(String devicenum, String val, Integer dealid, Integer status, Integer type) {
		//status 模板类型 0充电模板  1离线卡模板     2模拟投币模板（脉冲模板） 3钱包模板   4在线卡模板  5包月模板
		if(type==1){//设备  冲电、脉冲、离线充值机
			if("00".equals(val)) {//出厂默认设置
				status = 0;
			}else if("01".equals(val)) {//十路智慧款
				status = 0;
			}else if("02".equals(val)) {//电轿款
				status = 0;
			}
			//equipmentDao.getEquipmentById(CommUtil.toString(devicenum));
		}else if(type==2){//在线卡
			status = 4;
		}else if(type==3){//钱包
			status = 3;
		}else if(type==4){//钱包
			status = 5;
		}
		TemplateParent temp = new TemplateParent();
		temp.setMerchantid(dealid);
		temp.setStatus(status);
		temp.setCommon3("1");
		//查询该类型默认模板信息，如果没有默认模板
		List<TemplateParent> temlist = templateDao.selectpartemp(temp);
		
		return type;
	}
	
	
	//验证模板是否存在
	@Override
	public int verifyTemplate(Integer merid, Integer status) {
		try {
			Integer num = 0;
			merid = CommUtil.toInteger(merid);
			if(CommUtil.toInteger(merid)!=0){
				num = templateDao.verifyTemplate( merid, status);
			}
			return  num;
		} catch (Exception e) {
			logger.info(CommUtil.getExceptInfo(e));
			return 0;
		}
	}
	
	@Override
	public void insertParentTemplate(HttpServletRequest request){
		TemplateParent templateParent = new TemplateParent();
		templateParent.setStatus(StringUtil.getIntString(request.getParameter("status")));
		templateParent.setName(request.getParameter("name"));
		templateParent.setMerchantid(StringUtil.getIntString(request.getParameter("merchantid")));
		templateParent.setRemark(request.getParameter("remark"));
		templateParent.setPermit(StringUtil.getIntString(request.getParameter("permit")));
		templateParent.setWalletpay(StringUtil.getIntString(request.getParameter("walletpay")));
		templateParent.setCommon1(request.getParameter("common1"));
//		String common2 = request.getParameter("common2");
//		String common3 = request.getParameter("common3");
//		if(null!=common2 && !common2.equals("")) templateParent.setCommon2(Double.parseDouble(common2));
//		if(null!=common3 && !common3.equals("")) templateParent.setCommon2(Double.parseDouble(common3));
		templateDao.insertStairTemp(templateParent);
		//templateDao.insertParentTemplate(templateParent);
	}
	
	@Override
	public void deleteParentTemplate(Integer id) {
		TemplateParent temp = templateDao.getParentTemplateOne(id);
		if(temp!=null){
			Equipment code = new Equipment();
			code.setTempid(id);
			List<Equipment> equcode = equipmentDao.selectEqucode(code);
			if(equcode.size()>0){
				for(Equipment item : equcode){
					String version = CommUtil.toString(item.getHardversion());
					String devicenum = CommUtil.toString(item.getCode());
					if ("03".equals(version)) {
						List<TemplateParent> templateLists = templateDao.getParentTemplateListByMerchantid(0, 2);
						equipmentDao.updateTempidByEquipmentCode( devicenum, templateLists.get(0).getId());
					} else if ("04".equals(version)) {
						List<TemplateParent> templateLists = templateDao.getParentTemplateListByMerchantid(0, 1);
						equipmentDao.updateTempidByEquipmentCode( devicenum, templateLists.get(0).getId());
					} else if ("02".equals(version)) {
						List<TemplateParent> templist = templateDao.getParentTemplateListByMerchantid(0, 3);
						equipmentDao.updateTempidByEquipmentCode( devicenum, templist.get(0).getId());
					} else if ("08".equals(version) || "09".equals(version) || "10".equals(version)) {
						List<TemplateParent> templist = templateDao.getParentTemplateListByMerchantid(0, 6);
						equipmentDao.updateTempidByEquipmentCode( devicenum, templist.get(0).getId());
					} else {
						equipmentDao.updateTempidByEquipmentCode( devicenum, 0);
					}
				}
			}
			Area area = new Area();
			if(temp.getStatus()==3) area.setTempid(id);
			if(temp.getStatus()==4) area.setTempid2(id);
			List<Area>  arealist = areaDao.selectByArea(area);
			if(arealist.size()>0){
				if(temp.getStatus()==3) areaDao.updatetempid( null, id);
				if(temp.getStatus()==4) areaDao.updatetotempid( null, id);
				
			}
			templateDao.deleteParentTemplate(id);//根据id删除
			templateSonDao.delSonTempmanage(id);//根据模板id删除
		}
	}
	
	@Override
	public void updateParentTemplate(TemplateParent templateParent) {
		templateDao.updateParentTemplate(templateParent);
		
	}
	
	@Override
	public List<TemplateParent> getParentTemplateList() {
		List<TemplateParent> list = templateDao.getParentTemplateList();
		for(TemplateParent str : list){
			Object listSon = templateSonDao.getSonTemplateLists(str.getId());
			str.setGather(listSon);
        }
		return list;
	}
	
//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	@Override
	public void insertSonTemplate(TemplateSon templateSon) {
		templateSonDao.insertSonTemplate(templateSon);
	}
	
	@Override
	public void deleteSonTempmanage(Integer id) {
		templateSonDao.deleteSonTempmanage(id);
	}
	
	@Override
	public TemplateSon getInfoTemplateOne(Integer id) {
		return templateSonDao.getInfoTemplateOne(id);
	}
	
	@Override
	public String updateSonTemplate(HttpServletRequest request) {
		TemplateSon templateSon = new TemplateSon();
		String remark = request.getParameter("remark");
		String status = request.getParameter("status");
		
		if(null!=remark && !remark.equals("")) templateSon.setRemark(Double.parseDouble(remark));
//		Integer chargeTime = StringUtil.getIntString(request.getParameter("chargeTime"));
//		Double parseDouble = Double.parseDouble((request.getParameter("chargeQuantity"))) * 100;
		Double money = Double.parseDouble(request.getParameter("money"));
		if (money > 30000) {
			return "0";
		}
		if(status.equals("0")){
			Integer chargeTime = StringUtil.getIntString(request.getParameter("chargeTime"));
			Double parseDouble = Double.parseDouble((request.getParameter("chargeQuantity"))) * 100;
			if (parseDouble.intValue() > 30000 || chargeTime > 30000) {
				return "0";
			}
			templateSon.setChargeTime(chargeTime);
			templateSon.setChargeQuantity(parseDouble.intValue());
		}
		templateSon.setId(StringUtil.getIntString(request.getParameter("id")));
		templateSon.setName(request.getParameter("name"));
		templateSon.setMoney(money);
		templateSon.setStatus(StringUtil.getIntString(status));
		templateSonDao.updateSonTemplate(templateSon);
		return "1";
	}
	
	@Override
	public List<TemplateSon> getSonTemplateList() {
		return templateSonDao.getSonTemplateList();
	}

	@Override
	public TemplateParent getParentTemplateOne(Integer tempid) {
		TemplateParent parentTemplateOne = templateDao.getParentTemplateOne(tempid);
		if (parentTemplateOne != null) {
			Object listSon = templateSonDao.getSonTemplateLists(tempid);
			parentTemplateOne.setGather(listSon);
		}
		return parentTemplateOne;
	}

	@Override
	public List<TemplateParent> getParentTemplateListByMerchantid(Integer merchantid, Integer status) { 
		List<TemplateParent> list = templateDao.getParentTemplateListByMerchantid(merchantid, status);
		for(TemplateParent str : list){
			int temp = 0;
			if("1".equals(str.getCommon3())) str.setPitchon(1);
			List<TemplateSon> sonTemplateLists = templateSonDao.getSonTemplateLists(str.getId());
			for (TemplateSon templateSon : sonTemplateLists) {
				if (templateSon.getStatus() != null && templateSon.getStatus() == 5) {
					temp = 1;
				}
			}
			str.setHasbagMonth(temp);
			str.setGather(sonTemplateLists);
        }
		return list;
	}
	
	@Override
	public List<TemplateParent> getParentTemplateListByMerchantidwolf(Integer merchantid, Integer status) { 
		List<TemplateParent> list = templateDao.getParentTemplateListByMerchantidwolf(merchantid, status);
		for(TemplateParent str : list){
			Object listSon = templateSonDao.getSonTemplateLists(str.getId());
			str.setGather(listSon);
		}
		return list;
	}
	
	@Override
	public List<TemplateParent> getParentTemplateListByMidwolf(Integer merchantid, Integer status) { 
		List<TemplateParent> list = templateDao.getParentTemplateListByMerchantidwolf(merchantid, status);
		return list;
	}
	
	
	@Override
	public List<TemplateParent> getParentTemforOffLine(Integer merchantid, Integer status) {
		List<TemplateParent> list = templateDao.getParentTemforOffLine(0,merchantid, status);
		for(TemplateParent str : list){
			Object listSon = templateSonDao.getSonTemplateLists(str.getId());
			str.setGather(listSon);
        }
		return list;
	}


	@Override
	public List<TemplateSon> getSonTemp(Integer id) {
		return  templateSonDao.getSonTemplateLists(id);
	}

	@Override
	public List<TemplateSon> getEquSonTem(String code){
		return templateSonDao.getEquSonTem( code);
		
	}

	@Override
	public void insertDufaultTemplate(Integer uid) {
		try {
			if(CommUtil.toInteger(uid)!=0){
				templateDao.insertDufaultTemplate(uid);
			}
		} catch (Exception e) {
			logger.info(CommUtil.getExceptInfo(e));
		}
	}
	
	/**
	 * 根据函数插入离线模板
	 */
	@Override
	public void insertOfflineTemp(Integer merid) {
		try {
			if(CommUtil.toInteger(merid)!=0){
				templateDao.insertOfflineTemp(merid);
			}
		} catch (Exception e) {
			logger.info(CommUtil.getExceptInfo(e));
		}
	}
	
	/**
	 * 根据函数插入脉冲模板
	 */
	@Override
	public void insertInCoinsTemp(Integer merid) {
		try {
			if(CommUtil.toInteger(merid)!=0){
				templateDao.insertInCoinsTemp( merid);
			}
		} catch (Exception e) {
			logger.info(CommUtil.getExceptInfo(e));
		}
	}
	
	/**
	 * 根据函数插入钱包模板
	 */
	@Override
	public void insertWalletTemp(Integer merid) {
		try {
			if(CommUtil.toInteger(merid)!=0){
				templateDao.insertWalletTemp( merid);
			}
		} catch (Exception e) {
			logger.info(CommUtil.getExceptInfo(e));
		}
	}

	/**
	 * 根据函数插入在线卡模板
	 */
	@Override
	public void insertOnlineTemp(Integer merid) {
		try {
			if(CommUtil.toInteger(merid)!=0){
				templateDao.insertOnlineTemp( merid);
			}
		} catch (Exception e) {
			logger.info(CommUtil.getExceptInfo(e));
		}
	}
	
	@Override
	public boolean merchantTempIfExist(Integer merchantid) {
		List<TemplateParent> list = templateDao.getParentTemplateListByMerchantid(merchantid, 0);
		if (list == null || list.size() <= 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<TemplateSon> getSonTemplateLists(Integer id) {
		return templateSonDao.getSonTemplateLists( id);
	}
	
	/** =================================================================================================== */
	
	public Map<String, Object> inquireDirectTempData(Integer tempid, Integer dealid, String version) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			TemplateParent tempdata = new TemplateParent();
			Integer tempstatus = 0;
			//模板类型 0充电模板  1离线卡模板     2模拟投币模板（脉冲模板） 3钱包模板   4在线卡模板  5包月模板  6:v3模板
			if("03".equals(version)){
				tempstatus = 2;
			} else if("04".equals(version)){
				tempstatus = 1;
			} else if("08".equals(version) || "09".equals(version) || "10".equals(version)){
				tempstatus = 6;
			} else if("在线卡".equals(version)){
				tempstatus = 4;
			} else if("钱包".equals(version)){
				tempstatus = 3;
			} else if("包月".equals(version)){
				tempstatus = 5;
			}
			if(version==null){
				version = "00";
				System.out.println("ORIGIN  获取模板时输出版本为空(版本为空)     "+"tempid:"+tempid +"    dealid:"+dealid+"   version:"+version);
			}
			Integer mercid = null;
			tempid = CommUtil.toInteger(tempid);
			if(!tempid.equals(0)){//模板不为0时（即设备没有选中模板时）
				tempdata = templateDao.getParentTemplateOne(tempid);
				mercid = CommUtil.toInteger(tempdata.getMerchantid());
				if(mercid!=0 && !mercid.equals(dealid)) tempid = 0;
			} 
			if(tempid.equals(0)){//未绑定模板
				List<TemplateParent> tempinfo = templateDao.getParentTemplateListByMerchantidwolf(0, tempstatus);
				if(version.equals("02") || version.equals("07")){//大功率电轿款
					for(TemplateParent item : tempinfo){
						Integer teid = CommUtil.toInteger(item.getId());
						if(teid.equals(1)){
							tempdata = item;
							break;
						}
					}
				}else{//其他
					tempdata = tempinfo.get(0);
				}
				/* else if(version.equals("00") || version.equals("01") || version.equals("05") || version.equals("06")){//智慧款设备
					tempdata = tempinfo.get(0);
				}else{//03   04 和其他
					tempdata = tempinfo.get(0);
				}*/
			}
			datamap = JSON.parseObject(JSON.toJSONString(tempdata), Map.class);
//			datamap.put("tempdata", tempdata);
			return datamap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datamap;
	}
	
	public TemplateParent getDefaultTemplate(Integer dealid, String version) {
		TemplateParent tempdata = new TemplateParent();
		Integer tempstatus = 0;
		//模板类型 0充电模板  1离线卡模板     2模拟投币模板（脉冲模板） 3钱包模板   4在线卡模板  5包月模板  6:v3模板
		if("03".equals(version)){
			tempstatus = 2;
		} else if("04".equals(version)){
			tempstatus = 1;
		} else if("08".equals(version) || "09".equals(version) || "10".equals(version)){
			tempstatus = 6;
		} else if("在线卡".equals(version)){
			tempstatus = 4;
		} else if("钱包".equals(version)){
			tempstatus = 3;
		} else if("包月".equals(version)){
			tempstatus = 5;
		}
		TemplateParent temp = new TemplateParent();
		temp.setMerchantid(dealid);
		temp.setCommon3("1");
		temp.setStatus(tempstatus);
		List<TemplateParent> temlist = templateDao.selectpartemp(temp);
		if(temlist.size()==0){
			temlist = templateDao.getParentTemplateListByMerchantidwolf(0, tempstatus);
		}
		if(version.equals("02") || version.equals("07")){//大功率电轿款
			for(TemplateParent item : temlist){
				Integer teid = CommUtil.toInteger(item.getId());
				if(teid.equals(1)){
					tempdata = item;
					break;
				}
			}
		}else{//其他
			tempdata = temlist.get(0);
		}
		return tempdata;
	}
	
	
	/**
	 * @Description： 根据商户id和设备类型获取默认模板
	 * @author： origin          
	 * 创建时间：   2019年6月1日 上午11:55:01 
	 */
	@Override
	public Integer getDefaultPartemp(Integer merid, String hardversion) {
		Integer temid = 0;
		TemplateParent tempdata = getDefaultTemplate(merid, hardversion);
		temid = tempdata.getId();
//		List<TemplateParent> temlist = null;
//		TemplateParent temp = new TemplateParent();
//		temp.setMerchantid(merid);
//		temp.setCommon3("1");
//		if ("02".equals(hardversion)) {
//			temp.setStatus(0);
//			temlist = templateDao.selectpartemp(temp);
//			if(temlist.size()==0){
//				temp.setMerchantid(0);
//				temp.setCommon3(null);
//				temlist = templateDao.selectpartemp(temp);
//			}
//			temid = temlist.get(0).getId();
//		} else if ("03".equals(hardversion)) {
//			temp.setStatus(2);
//			temlist = templateDao.selectpartemp(temp);
//			if(temlist.size()==0){
//				temp.setMerchantid(0);
//				temp.setCommon3(null);
//				temlist = templateDao.selectpartemp(temp);
//			}
//			temid = temlist.get(0).getId();
//		} else if ("04".equals(hardversion)) {
//			temp.setStatus(1);
//			temlist = templateDao.selectpartemp(temp);
//			if(temlist.size()==0){
//				temp.setMerchantid(0);
//				temp.setCommon3(null);
//				temlist = templateDao.selectpartemp(temp);
//			}
//			temid = temlist.get(0).getId();
//		} else {
//			temp.setStatus(0);
//			temlist = templateDao.selectpartemp(temp);
//			if(temlist.size()==0){
//				temid = 0;
//			}else {
//				temid = temlist.get(0).getId();
//			}
//		}
		return temid;
	}
	
	 /**
	  * @Description： 添加一级充电模板
	  * @author： origin   
	  */
	@Override
	public void addStairCharge(Integer merid, String name, String brandName, String telephone,
			Integer standard, Integer permit, Integer WalletPay, Integer ifmonth) {
		TemplateParent template = new TemplateParent();
		template.setName(name);
		template.setMerchantid(merid);
		template.setStatus(0);
		template.setRemark(brandName);
		template.setCommon1(telephone);
		template.setCommon2(standard);
		template.setPermit(permit);
		template.setWalletpay(WalletPay);
		template.setIfmonth(ifmonth);
		//templateDao.insertParentTemplate(template);
		templateDao.insertStairTemp(template);
	}

	/**
	 * @Description： 修改充电模板的子类模板
	 * @author： origin   
	 */
	@Override
	public int updateSubclassCharge(Integer id, String name, Double money, String chargeTime, Integer chargeQuantity) {		
		TemplateSon subclasscharge = new TemplateSon();
		subclasscharge.setId(id);
		if (money > 30000 || chargeQuantity.intValue() > 30000 || CommUtil.toInteger(chargeTime) > 30000) {
			return 0;
		}
		subclasscharge.setName(name);
		subclasscharge.setMoney(money);
		subclasscharge.setChargeTime(CommUtil.toInteger(chargeTime));
		subclasscharge.setChargeQuantity(chargeQuantity);
		//templateSon.setStatus(StringUtil.getIntString(status));
		templateSonDao.updateSonTemplate(subclasscharge);
		return 1;
	}

	/**
	  * @Description： 添加一级离线模板
	  * @author： origin   
	  */
	@Override
	public void addStairOffline( Integer merid, String name, String brandName, String telephone) {
		TemplateParent template = new TemplateParent();
		template.setName(name);
		template.setMerchantid(merid);
		template.setStatus(1);
		template.setPermit(1);
		template.setWalletpay(2);
		template.setIfalipay(1);
		template.setRemark(brandName);
		template.setCommon1(telephone);
		template.setCreateTime(CommUtil.toDateTime());
//		templateDao.insertParentTemplate(template);
		templateDao.insertStairTemp(template);
	}

	/**
	 * @Description： 修改离线模板的子类模板
	 * @author： origin 
	 */
	@Override
	public int updateSubclassOffline(Integer id, String name, Double money, Double remark) {
		TemplateSon subclassoffline = new TemplateSon();
		subclassoffline.setId(id);
		subclassoffline.setName(name);
		subclassoffline.setMoney(money);
		subclassoffline.setRemark(remark);
		templateSonDao.updateSonTemplate(subclassoffline);
		return 1;
	}
	
	/**
	  * @Description： 添加一级脉冲（投币）模板
	  * @author： origin   
	  */
	@Override
	public void addStairIncoins(Integer merid, String name, String brandName, String telephone, Integer permit,
			Integer walletpay) {
		TemplateParent template = new TemplateParent();
		template.setName(name);
		template.setMerchantid(merid);
		template.setStatus(2);
		template.setRemark(brandName);
		template.setCommon1(telephone);
		template.setPermit(permit);
		template.setWalletpay(walletpay);
		//templateDao.insertParentTemplate(template);
		templateDao.insertStairTemp(template);
	}
	
	/**
	 * @Description： 修改脉冲（投币）模板的子类模板
	 * @author： origin 
	 */
	@Override
	public int updateSubclassIncoins(Integer id, String name, Double money, Double remark) {
		TemplateSon subclassincoins = new TemplateSon();
		subclassincoins.setId(id);
		subclassincoins.setName(name);
		subclassincoins.setMoney(money);
		subclassincoins.setRemark(remark);
		templateSonDao.updateSonTemplate(subclassincoins);
		return 1;
	}
	
	/**
	  * @Description： 添加一级钱包模板
	  * @author： origin 
	  */
	@Override
	public void addStairWallet(Integer merid, String name, String brandName, String telephone) {
		TemplateParent template = new TemplateParent();
		template.setName(name);
		template.setMerchantid(merid);
		template.setStatus(3);
		template.setPermit(1);
		template.setWalletpay(2);
		template.setRemark(brandName);
		template.setCommon1(telephone);
		//templateDao.insertParentTemplate(template);
		templateDao.insertStairTemp(template);
	}
	
	/**
	 * @Description： 修改钱包模板的子类模板
	 * @author： origin 
	 */
	@Override
	public int updateSubclassWwallet(Integer id, String name, Double money, Double remark) {
		TemplateSon subclasswwallet = new TemplateSon();
		subclasswwallet.setId(id);
		subclasswwallet.setName(name);
		subclasswwallet.setMoney(money);
		subclasswwallet.setRemark(remark);
		templateSonDao.updateSonTemplate(subclasswwallet);
		return 1;
	}

	/**
	  * @Description： 添加一级在线卡模板
	  * @author： origin 
	  */
	@Override
	public void addStairOnline(Integer merid, String name, String brandName, String telephone) {		
		TemplateParent template = new TemplateParent();
		template.setName(name);
		template.setMerchantid(merid);
		template.setStatus(4);
		template.setPermit(1);
		template.setWalletpay(2);
		template.setRemark(brandName);
		template.setCommon1(telephone);
		//templateDao.insertParentTemplate(template);
		templateDao.insertStairTemp(template);
	}

	/**
	 * @Description： 修改在线卡模板的子类模板
	 * @author： origin 
	 */
	@Override
	public int updateSubclassOnline(Integer id, String name, Double money, Double remark) {
		TemplateSon subclasswwallet = new TemplateSon();
		subclasswwallet.setId(id);
		subclasswwallet.setName(name);
		subclasswwallet.setMoney(money);
		subclasswwallet.setRemark(remark);
		templateSonDao.updateSonTemplate(subclasswwallet);
		return 1;
	}

	/**
	 * @Description： 模板的选择
	 * @author： origin          
	 * @param source 1:设备绑定  2:钱包绑定  3:在线卡绑定
	 * @param obj  设备时为设备号  钱包、在线卡选择时为小区id
	 * @param temid 模板id
	 * @return
	 */
	@Override
	public int templatechoice(Integer source, String obj, Integer temid) {
		Integer num = 0;
		if(source==1){
			num = equipmentDao.updateTempidByEquipmentCode(obj, temid);
		}else if(source==2){
			Area area = new Area();
			area.setId(StringUtil.getIntString(obj));
			area.setTempid(temid);
			num = areaDao.updateByArea(area);
		}else if(source==3){
			Area area = new Area();
			area.setId(StringUtil.getIntString(obj));
			area.setTempid2(temid);
			num = areaDao.updateByArea(area);
		}
		return num;
	}

	/**
	 * @Description： 通过模板名查询一级模板信息
	 * @author： origin          
	 * 创建时间：   2019年5月27日 下午5:32:57 
	 */
	@Override
	public TemplateParent templateByName( Integer merid, Integer status, String name) {
		TemplateParent template = templateDao.templateByName( merid, status, name);
		return template;
	}

	/**
	 * @Description： 根据上级模板id和当前名字查询下级模板
	 * @author： origin          
	 * 创建时间：   2019年5月27日 下午5:58:03 
	 */
	@Override
	public TemplateSon subtemplatefind(Integer parid, String name) {
		TemplateSon template = templateSonDao.subtemplatefind( parid,  name);
		return template;
	}

	/**
	 * @Description： 模板的默认选择
	 * @author： origin          
	 * 创建时间：   2019年5月28日 下午3:04:09
	 */
	@Override
	public Integer templateDefault( Integer merid, Integer source, Integer temid) {
		templateDao.updatepartemcom( source, "0", merid);
		TemplateParent template = new TemplateParent();
		template.setId(temid);
		template.setCommon3("1");
		//template.setStatus(source);
		return templateDao.updateParentTemplate(template);
	}

	//==========================================================================================================
	/**
	 * @Description： 添加模板
	 * @author： origin 创建时间：   2019年8月2日 下午5:34:44 
	 */
	@Override
	public Map<String, Object> insertStairTempVal(String name, Integer merid, String remark, String common1,
			Integer status, Integer permit, Integer common2, Integer walletpay, Integer grade,Integer ifalipay,
			String chargeInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			boolean flas = verifyExist( name, merid, status);
			if(!flas){
				if(grade==1){
					templateDao.insertClassifyTem(name+"(等级一)",name+"(等级二)",name+"(等级三)", merid,  status, permit, walletpay, grade, remark, common1, common2);
					TemplateParent template = templateDao.templateByName( merid, status, name+"(等级一)");
					map.put("data", getTempDetails(template));
				}else if(grade==2){
					insertStairTempOperate( name, merid, status, permit, walletpay, null, grade, ifalipay, remark, 
							common1, common2, null, null, null,chargeInfo);
					
					TemplateParent template = templateDao.templateByName( merid, status, name);
					List<TemplateParent> list = new ArrayList<TemplateParent>();
					list.add(getTemplateInfo(template));
					map.put("data", list);
				}
				map.put("code", 200);
				map.put("messg", "succeed");
			}else{
				map.put("code", 402);
				map.put("messg", "该类型模板名字重复");
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return CommonConfig.messg(403);
		}
	}
	
	/**
	 * @Description： 删除一级模板（如果使用，不能删除）
	 * @author： origin 创建时间：   2019年8月2日 下午5:34:44 
	 */
	@Override
	public Map<String, Object> deleteStairTemp(Integer parid) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			TemplateParent temp = templateDao.getParentTemplateOne(parid);
			Equipment code = new Equipment();
			code.setTempid(parid);
			List<Equipment> equcode = equipmentDao.selectEqucode(code);
			if (equcode.size()>0) {//该模板在设备中被绑定使用
				map.put("code", 410);
				map.put("messg", "该模板在设备中存在被绑定使用状况");
				return map;
			}
			Area area = new Area();
			if(temp.getStatus()==3) area.setTempid(parid);// 3钱包模板  
			if(temp.getStatus()==4) area.setTempid2(parid);// 4在线卡模板
			List<Area> arealist = areaDao.selectByArea(area);
			if (arealist.size()>0) {//该模板在设备中被绑定使用
				map.put("code", 411);
				map.put("messg", "该模板在小区中存在被绑定使用状况");
				return map;
			}
			templateDao.deleteParentTemplate(parid);//根据id删除
			templateSonDao.delSonTempmanage(parid);//根据模板id删除
			return CommonConfig.messg(200);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonConfig.messg(403);
		}
	}
	
	/**
	  * @Description： 删除(强制)一级模板
	  * @author： origin   
	  */
	@Override
	public Map<String, Object> deleteforcestairtemp(Integer parid) {
		try {
			TemplateParent temp = templateDao.getParentTemplateOne(parid);
			Equipment code = new Equipment();
			code.setTempid(parid);
			List<Equipment> equcode = equipmentDao.selectEqucode(code);
			if (equcode.size()>0) {//该模板在设备中被绑定使用
				for(Equipment item : equcode){
					if ("02".equals(item.getHardversion())) {
						List<TemplateParent> templist = templateDao.getParentTemplateListByMerchantid(0, 3);
						equipmentDao.updateTempidByEquipmentCode( item.getCode(), templist.get(0).getId());
					} else if ("03".equals(item.getHardversion())) {
						List<TemplateParent> templateLists = templateDao.getParentTemplateListByMerchantid(0, 2);
						equipmentDao.updateTempidByEquipmentCode( item.getCode(), templateLists.get(0).getId());
					} else if ("04".equals(item.getHardversion())) {
						List<TemplateParent> templateLists = templateDao.getParentTemplateListByMerchantid(0, 1);
						equipmentDao.updateTempidByEquipmentCode( item.getCode(), templateLists.get(0).getId());
					}else {
						equipmentDao.updateTempidByEquipmentCode( item.getCode(), 0);
					}
				}
			}
			Area area = new Area();
			if(temp.getStatus()==3) area.setTempid(parid);
			if(temp.getStatus()==4) area.setTempid2(parid);
			List<Area>  arealist = areaDao.selectByArea(area);
			if(arealist.size()>0){
				if(temp.getStatus()==3) areaDao.updatetempid( null, parid);
				if(temp.getStatus()==4) areaDao.updatetotempid( null, parid);
			}
			if(temp.getGrade()==1){
				List<TemplateParent> list = templateDao.templateGradeInfo(temp.getMerchantid(), temp.getStatus(), parid);
				for(TemplateParent item : list){
					templateDao.deleteParentTemplate(item.getId());//根据id删除
					templateSonDao.delSonTempmanage(item.getId());//根据模板id删除
				}
				templateDao.deleteParentTemplate(parid);//根据id删除
				templateSonDao.delSonTempmanage(parid);//根据模板id删除
			}else if(temp.getGrade()==2){
				templateDao.deleteParentTemplate(parid);//根据id删除
				templateSonDao.delSonTempmanage(parid);//根据模板id删除
			}
			return CommonConfig.messg(200);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonConfig.messg(403);
		}
	}
	
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月2日 下午5:34:44 
	 */
	@Override
	public Map<String, Object> updateStairTemp(Integer id, String name, String remark, String common1, Integer permit,
			Integer status, Integer common2, Integer walletpay, Integer grade) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			TemplateParent oritem = templateDao.getParentTemplateOne(id);
			oritem = oritem == null ? new TemplateParent() :oritem;
			if(!oritem.getName().equals(name)){
				boolean flas = verifyExist( name, oritem.getMerchantid(), status);
				if(flas){
					map.put("code", 402);
					map.put("messg", "该类型模板名字重复");
					return map;
				}
			}
			updateStairTempOperate(id, name, null, status, permit, walletpay, null, grade, remark, common1, common2, null);
			map = CommonConfig.messg(200);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return CommonConfig.messg(403);
		}
	}
	
	/**
	 * @Description： 查询模板
	 * @author： origin 创建时间：   2019年8月2日 下午5:34:44 
	 */
	@Override
	public List<TemplateParent> selectStairTempVal(User user, Integer status) {
		List<TemplateParent> list = templateDao.getParentTemplateListByMerchantid(user.getId(), status);
//		for(TemplateParent str : list){
//			Integer grade =	str.getGrade();
//		    Integer rank =	str.getRank();
//			int temp = 0;
//			List<TemplateSon> sonTemplateLists = templateSonDao.getSonTemplateLists(str.getId());
//			for (TemplateSon templateSon : sonTemplateLists) {
//				if (templateSon.getStatus() != null && templateSon.getStatus() == 5) {
//					temp = 1;
//				}
//			}
//			str.setHasbagMonth(temp);
//			if("1".equals(str.getCommon3())) str.setPitchon(1);
//			str.setGather(sonTemplateLists);
//			if(grade==1 && rank ==1){
//				List<TemplateParent> lower = getTempDetails(str);
//				str.setTempparList(lower);
////				str.setGather(lower);
//			}
//        }
		return list;
	}
	
	@Override
	public TemplateParent templateGradeClassify(Integer merid, Integer status, Integer stairid, Integer rank) {
		return templateDao.templateGradeClassify( merid, status, stairid, rank);
	}
	
	public Integer updateStairTempOperate(Integer id, String name, Integer merid, Integer status, Integer permit, Integer walletpay, 
			Integer ifmonth, Integer grade, String remark, String common1, Integer common2, String common3) {
		TemplateParent template = new TemplateParent();
		template.setId(id);
		template.setName(name);
		template.setMerchantid(merid);
		template.setStatus(status);
		template.setPermit(permit);
		template.setWalletpay(walletpay);
		template.setIfmonth(ifmonth);
		template.setGrade(grade);
		template.setRemark(remark);
		template.setCommon1(common1);
		template.setCommon2(common2);
		template.setCommon3(common3);
		return templateDao.updateParentTemplate(template);
	}
	
	public Integer insertStairTempOperate( String name, Integer merid, Integer status, Integer permit, Integer walletpay, Integer ifmonth,  
			Integer grade, Integer ifalipay, String remark, String common1, Integer common2, String common3, Integer stairid, Integer rank,String chargeInfo) {
		ifalipay = ifalipay == 0 ? 1 : ifalipay;
		TemplateParent template = new TemplateParent();
		template.setName(name);
		template.setMerchantid(merid);
		template.setStatus(status);
		template.setPermit(permit);
		template.setIfalipay(ifalipay);
		template.setWalletpay(walletpay);
		template.setIfmonth(ifmonth);
		template.setGrade(grade);
		template.setStairid(stairid);
		template.setRank(rank);
		template.setRemark(remark);
		template.setCommon1(common1);
		template.setCommon2(common2);
		template.setCommon3(common3);
		template.setChargeInfo(chargeInfo);
		template.setCreateTime(StringUtil.toDateTime());
		return templateDao.insertStairTemp(template);
	}
	
	public Integer insertSecondTempOpe( String name, Integer temid, Integer status, Double money, Integer chargeTime, 
		Integer quantity, Double remark, String common1, String common2, String common3) {
		TemplateSon tempson = new TemplateSon();
		tempson.setTempparid(temid);
		tempson.setName(name);
		tempson.setStatus(status);
		tempson.setType(0);
		tempson.setMoney(money);
		tempson.setChargeTime(chargeTime);
		tempson.setChargeQuantity(quantity);
		tempson.setRemark(remark);
		tempson.setCommon1(common1);
		tempson.setCommon2(common2);
		tempson.setCommon3(common3);
		tempson.setCreateTime(new Date());
		return templateSonDao.insertSonTemplate(tempson);
	}
	
	
	/**
	 * @Description： 判断模板是否存在
	 * @author： origin 创建时间：   2019年8月3日 下午3:21:20
	 */
	public boolean verifyExist(String name, Integer merid, Integer status) {
		TemplateParent template = templateDao.templateByName( merid, status, name);
		if(null == template) return false;
		return true;
	}
	
	/**
	 * @Description： 获取指定模板的子模板并包含进一级模板中返回
	 * @author： origin 创建时间：   2019年8月3日 上午11:44:32
	 */
	public TemplateParent getTemplateInfo(TemplateParent template) {
		if(template!=null){
			List<TemplateSon> listSon = templateSonDao.getSonTemplateLists(template.getId());
			template.setGather(listSon);
		}
		return template;
	}
	
	/**
	 * @Description： 获取等级模板所有详情（关联的和下级）
	 * @author： origin 创建时间：   2019年8月3日 下午3:20:38
	 */
	public List<TemplateParent> getTempDetails(TemplateParent template) { 
		TemplateParent temprank1 = getTemplateInfo(template);
		TemplateParent temprank2 = getTemplateInfo(templateDao.templateGradeClassify( template.getMerchantid(), 
				template.getStatus(), template.getId(), 2));
		TemplateParent temprank3 = getTemplateInfo(templateDao.templateGradeClassify(  template.getMerchantid(), 
				template.getStatus(), template.getId(), 3));
		List<TemplateParent> list = new ArrayList<TemplateParent>();
		list.add(temprank1);
		list.add(temprank2);
		list.add(temprank3);
		return list;
	}
	
	@Override
	public TemplateParent assignTempInfo(Integer uid, Integer merid, Integer tempid, String hardversion) {
		TemplateParent temp = null;
		try {
			temp = templateDao.getParentTemplateOne(tempid);
			if(null==temp){
				temp = getTempInfoToNull(merid, hardversion);
			}else{
				if(hardversion=="01"){
					temp = getTempInfo(temp, uid, merid);
				}
			}
			temp = getTemplateInfo(temp);
			return temp;
		} catch (Exception e) {
			return temp;
		}
		
	}
	
	
	public TemplateParent getTempInfoToNull( Integer merid, String hardversion){
		Integer status = null;
		if(hardversion=="00" || hardversion=="01" || hardversion=="02"){//模板类型 0充电模板  1离线卡模板     2模拟投币模板（脉冲模板） 3钱包模板   4在线卡模板  5包月模板
			status = 0;
		}else if(hardversion=="03"){
			status = 3;
		}else if(hardversion=="04"){
			status = 4;
		}
		List<TemplateParent> templateList = templateDao.getParentTemplateListByMerchantid(merid, status);
		TemplateParent temp = null;
		for(TemplateParent item : templateList){
			if(CommUtil.toInteger(item.getCommon3())==1){
				temp = item;
			}
		}
		if(temp==null){
			templateList = templateDao.getParentTemplateListByMerchantid(merid, status);
			temp = templateList.get(0);
		}
		return temp;
	}
	
	//获取用户分等级模板
	public TemplateParent getTempInfo(TemplateParent temp, Integer uid, Integer merid) {
		TemplateParent tempinfo = null;
		if(CommUtil.toInteger(temp.getGrade())==1){//分等级
			Parameters param = new Parameters();
			param.setUid(uid);
			param.setDealer(merid.toString());
			Map<String, Object>  userole = roleDao.selectUserRole(param);
			if(userole==null || userole.size()==0){
				tempinfo = temp;
			}else{
				Integer proleid = CommUtil.toInteger(userole.get("proleid"));
				if(proleid==6){
					tempinfo = templateDao.templateGradeClassify( merid, 0, temp.getId(), 2);
				}else if(proleid==7){
					tempinfo = templateDao.templateGradeClassify( merid, 0, temp.getId(), 3);
				}
			}
		}else{
			tempinfo = temp;
		}
		return tempinfo;
	}

	//==========================================================================================================
	/**
	  * @Description： 添加一级包月模板
	  * @author： wolf 
	  */
	@Override
	public void addPackageMonth(Integer merid, String name, String remark, String common1,Integer common2,String common3,Integer ifmonth) {		
		TemplateParent template = new TemplateParent();
		template.setName(name);
		template.setMerchantid(merid);
		template.setStatus(5);
		template.setPermit(1);
		template.setWalletpay(2);
		template.setRemark(remark);
		template.setCommon1(common1);
		template.setCommon2(common2);
		template.setCommon3(common3);
		template.setIfmonth(ifmonth);
		//templateDao.insertParentTemplate(template);
		templateDao.insertStairTemp(template);
	}

	/**
	 * @Description： 修改包月模板的子类模板
	 * @author： wolf 
	 */
	@Override
	public int updateSubclassPackageMonth(Integer id, String name, Double money, String common1) {
		TemplateSon subclasswwallet = new TemplateSon();
		subclasswwallet.setId(id);
		subclasswwallet.setName(name);
		subclasswwallet.setMoney(money);
		subclasswwallet.setCommon1(common1);
		templateSonDao.updateSonTemplate(subclasswwallet);
		return 1;
	}

	@Override
	public void insertMonthTemp(Integer merid) {
		try {
			if(CommUtil.toInteger(merid)!=0){
				templateDao.insertMonthTemp(merid);
			}
		} catch (Exception e) {
			logger.info(CommUtil.getExceptInfo(e));
		}
	}

	public List<TemplateParent> getTemlist( TemplateParent temp) {
		List<TemplateParent> temlist = templateDao.selectpartemp(temp);
		if(null==temlist ||temlist.size()==0){
			temp.setMerchantid(0);
			temp.setCommon3(null);
			temlist = templateDao.selectpartemp(temp);
		}
		return temlist;
	}
	
	/**
	 * @Description： 根据设备类型获取指定模板
	 * @author： origin 创建时间：   2019年8月23日 下午5:35:51 
	 */
	@Override
	public List<TemplateParent> getAppointTempByType(String type, Integer merid) {
		List<TemplateParent> temlist = null;
		TemplateParent temp = new TemplateParent();
		temp.setMerchantid(merid);
		temp.setCommon3("1");
		Integer status = 0;
		if(type.equals("03")){
			status = 2;
		}else if(type.equals("04")){
			status = 1;
		}
		/** 设备硬件版本 00-默认出厂版本、01-十路智慧款、02-电轿款、03-脉冲投币、04-离线充值机*/
		//模板类型 0充电模板  1离线卡模板     2模拟投币模板（脉冲模板） 3钱包模板   4在线卡模板  5包月模板
		temp.setStatus(status);
		temlist = getTemlist(temp);
		return temlist;
	}

	public List<TemplateParent> getTemplateData(Integer merchantid, Integer status) { 
		List<TemplateParent> list = templateDao.getParentTemplateListByMerchantidwolf(merchantid, status);
		for(TemplateParent str : list){
			Object listSon = templateSonDao.getSonTemplateLists(str.getId());
			str.setGather(listSon);
		}
		return list;
	}

	/**
	 * separate
	 * @Description： 查询设备模板信息 
	 * @author： origin 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object inquireDeviceTemplateData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			String devcenum =  CommUtil.toString(maparam.get("devicenum"));
			Integer merid =  CommUtil.toInteger(maparam.get("merid"));
			Equipment equipment = equipmentDao.getEquipmentById(devcenum);
			equipment = equipment == null ? new Equipment() : equipment;
			String hardversion =CommUtil.toString(equipment.getHardversion());
			datamap.put("devcenum", devcenum);
			datamap.put("arecode", devcenum);
			datamap.put("source", 1);
			
			List<TemplateParent> templateData = null;
			//模板类型 0充电模板  1离线卡模板     2模拟投币模板（脉冲模板） 3钱包模板   4在线卡模板  5包月模板
			Integer status = 0;
			Integer typeson = 0;
			if(equipment!=null){
				if ("04".equals(hardversion)) {//离线充值机
					status = 1;
					datamap.put("hardver", 3);
				} else if ("03".equals(hardversion)) {//脉冲信息
					status = 2;
					datamap.put("hardver", 2);
				} else if ("08".equals(hardversion) || "09".equals(hardversion) || "10".equals(hardversion)) {//新版10路智慧款V3
					typeson = 1;
					status = 6;
					datamap.put("hardver", 6);
				} else {
					datamap.put("hardver", 1);
				}
				Integer tempid = CommUtil.toInteger(equipment.getTempid());
				
				List<TemplateParent> tempinfo = templateDao.getParentTemplateListByMerchantidwolf(0, status);
				Map<String, Object> systemTempData = new HashMap<String, Object>();
				 TemplateParent systemTemp = null;
				 if("02".equals(hardversion) || "07".equals(hardversion)){
					 for(TemplateParent item : tempinfo){
						 Integer teid = CommUtil.toInteger(item.getId());
						 if(teid.equals(1)) systemTemp = item;
					 }
				 } else if("00".equals(hardversion) || "01".equals(hardversion) || "05".equals(hardversion) || "06".equals(hardversion)){
					 systemTemp = tempinfo.get(0);
				 }else{//03   04 和其他
					 systemTemp = tempinfo.get(0);
				 }
				 systemTempData = JSON.parseObject(JSON.toJSONString(systemTemp), Map.class);
				
				Integer tempidsys = CommUtil.toInteger(systemTempData.get("id"));
				if(tempid == 0 || tempid == tempidsys){
					systemTempData.put("pitchon", 1);
				}
				if(typeson==1){
					systemTempData.put("gather", inquireSystemSonTempData(tempidsys, typeson));
				}else{
					systemTempData.put("gather", inquireSystemSonTempData(tempidsys, typeson).get("gather"));
				}
				List<Map<String, Object>> tempListData = new ArrayList<>();
				List<TemplateParent> templatelist = templateDao.getParentTemplateListByMerchantidwolf(merid, status);
				Map<String, Object> tempemploy = null;
				for (int i = templatelist.size() - 1; i >= 0; i--) {
					TemplateParent templates = templatelist.get(i);
					Integer templaid = CommUtil.toInteger(templates.getId());
					if (tempid.equals(templaid)) {
						templates.setPitchon(1);
						tempemploy = JSON.parseObject(JSON.toJSONString(templates), Map.class);
						if(typeson==1){
							tempemploy.put("gather", inquireSystemSonTempData(templaid, typeson));
						}else{
							tempemploy.put("gather", inquireSystemSonTempData(templaid, typeson).get("gather"));
						}
					}else{
						Map<String, Object> tempcontent = JSON.parseObject(JSON.toJSONString(templates), Map.class);
						if(typeson==1){
							tempcontent.put("gather", inquireSystemSonTempData(templaid, typeson));
						}else{
							tempcontent.put("gather", inquireSystemSonTempData(templaid, typeson).get("gather"));
						}
						tempListData.add(tempcontent);
					}
				}
				List<Map<String, Object>> list = new ArrayList<>();
				list.add(systemTempData);
				if(tempemploy!=null) list.add(tempemploy);
				list.addAll(tempListData);
				datamap.put("templatelist", list);
			}
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	/**
	 * 
	 * @Description： 修改主（一级）模板
	 * @author： origin 
	 */
	public void redactFirstTemplateData(Integer id, String name, Integer merid, Integer status, Integer permit, 
			Integer walletpay, Integer ifmonth, String brandName, String telephone, Integer standard) {
		
		
		
	}
	
	/**
	 * separate
	 * @Description： 修改主模板信息
	 * @author： origin 
	 */
	@Override
	public Object updateFirstTemplateData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
//			User user = CommonConfig.getAdminReq(request);
			Integer id =  CommUtil.toInteger(maparam.get("id"));
			String name =  CommUtil.toString(maparam.get("name"));
//			String remark =  CommUtil.toString(maparam.get("remark"));
//			String common1 =  CommUtil.toString(maparam.get("common1"));
			String remark =  (String) maparam.get("remark");//可以获取""空字符串
			String common1 =  (String) maparam.get("common1");
			Integer common2 =  CommUtil.toInteger(maparam.get("common2"));
			Integer permit =  CommUtil.toInteger(maparam.get("permit"));
			Integer walletpay =  CommUtil.toInteger(maparam.get("walletpay"));
			Integer status =  CommUtil.toInteger(maparam.get("status"));
			Integer ifmonth =  CommUtil.toInteger(maparam.get("ifmonth"));
			String chargeInfo =  CommUtil.toString(maparam.get("chargeInfo"));
			Integer ifalipay =  CommUtil.toInteger(maparam.get("ifalipay"));
			ifalipay = ifalipay == 0 ? 1 : ifalipay;
			
			TemplateParent oritem = templateDao.getParentTemplateOne(id);
			oritem = oritem == null ? new TemplateParent() :oritem;
			if(!oritem.getName().equals(name)){
				TemplateParent tem = templateDao.templateByName(oritem.getMerchantid(), status, name);
				if(null != tem){
					CommUtil.responseBuildInfo(106, "该模板信息已存在", datamap);
					return datamap;
				}
			}
			TemplateParent template = new TemplateParent();
			template.setId(id);
			template.setName(name);
			template.setRemark(remark);	
			template.setCommon1(common1);
			template.setCommon2(common2);
			template.setPermit(permit);
			template.setWalletpay(walletpay);
			template.setIfmonth(ifmonth);
			template.setIfalipay(ifalipay);
			template.setChargeInfo(chargeInfo);
			templateDao.updateParentTemplate(template);
			datamap.put("tempid", id);
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}


	/**
	 * separate
	 * @Description： 删除所有模板信息（主模板、子模板）（如果使用，不能删除）
	 * @author： origin 
	 */
	@Override
	public Object deleteTemplateData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer parentid =  CommUtil.toInteger(maparam.get("id"));
			TemplateParent temp = templateDao.getParentTemplateOne(parentid);
			temp = temp == null ? new TemplateParent() : temp;
			Integer grade =  CommUtil.toInteger(temp.getGrade());
			Equipment code = new Equipment();
			code.setTempid(parentid);
			List<Equipment> equcode = equipmentDao.selectEqucode(code);
			if (equcode!= null && equcode.size()>0) {
				datamap.put("code", 105);
				datamap.put("message", "删除失败，该模板在设备中被绑定使用");
				return datamap;
			}
			Integer status = CommUtil.toInteger(temp.getStatus());
			if(status.equals(3) || status.equals(4)){
				Area area = new Area();
				if(status.equals(3)) area.setTempid(parentid);// 3钱包模板  
				if(status.equals(4)) area.setTempid2(parentid);// 4在线卡模板
				List<Area> arealist = areaDao.selectByArea(area);
				if (arealist!= null && arealist.size()>0) {
					datamap.put("code", 105);
					datamap.put("message", "删除失败，该模板在小区中被绑定使用");
					return datamap;
				}
			}
			if(grade.equals(1)){
				Integer merid = CommUtil.toInteger(temp.getMerchantid());
				Integer statustype = CommUtil.toInteger(temp.getStatus());
				Integer stairid = parentid;
				List<TemplateParent> tempgrade = templateDao.templateGradeInfo(merid, statustype, stairid);
				for(TemplateParent item : tempgrade){
					Integer idgrad = CommUtil.toInteger(item.getId());
					templateDao.deleteParentTemplate(idgrad);//根据id删除
					templateSonDao.delSonTempmanage(idgrad);//根据模板id删除
				}
			}else{
				templateDao.deleteParentTemplate(parentid);//根据id删除
				templateSonDao.delSonTempmanage(parentid);//根据模板id删除
			}
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}


	/**
	 * separate
	 * @Description： 选中模板信息
	 * @author： origin 
	 */
	@Override
	public Object pitchOnTemplate(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer source =  CommUtil.toInteger(maparam.get("source"));
			Integer areaid =  CommUtil.toInteger(maparam.get("areaid"));
			String devicenum =  CommUtil.toString(maparam.get("devicenum"));
			Integer tempid =  CommUtil.toInteger(maparam.get("temid"));
			if(source==1){
				equipmentDao.updateTempidByEquipmentCode(devicenum, tempid);
			}else if(source==2){
				Area area = new Area();
				area.setId(areaid);
				area.setTempid(tempid);
				areaDao.updateByArea(area);
			}else if(source==3){
				Area area = new Area();
				area.setId(areaid);
				area.setTempid2(tempid);
				areaDao.updateByArea(area);
			}
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}


	/**
	 * separate
	 * @Description： 选中默认模板信息
	 * @author： origin 
	 */
	@Override
	public Object defaultPitchOnTemplate(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			User user = null;
			Integer paysource = CommUtil.forInteger(maparam.get("paysource"));
			if(paysource==0 || paysource==2){
				user = (User) request.getSession().getAttribute("admiin");
			}else{
				user = (User) request.getSession().getAttribute("user");
			}
			Integer type =  CommUtil.toInteger(maparam.get("type"));//
			Integer temid =  CommUtil.toInteger(maparam.get("temid"));
			Integer merid = CommUtil.toInteger(user.getId());
			templateDao.updatepartemcom( type, "0", merid);//将原有的默认模板取消
			//status` int(10) DEFAULT NULL COMMENT '模板类型0充电模板-1离线卡模板-2脉冲模板-3钱包模板-4在线卡模板',
			
			TemplateParent template = new TemplateParent();
			template.setId(temid);
			template.setCommon3("1");
			//template.setStatus(source);
			templateDao.updateParentTemplate(template);
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	/**
	 * separate
	 * @Description： 修改编辑子模板信息
	 * @author： origin 
	 */
	@Override
	public Object updateSecondTemplateData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer id =  CommUtil.toInteger(maparam.get("id"));
			String name =  CommUtil.toString(maparam.get("name"));
			TemplateSon oritem = templateSonDao.getInfoTemplateOne(id);
			oritem = oritem == null ? new TemplateSon() :oritem;
			if(!oritem.getName().equals(name)){
				TemplateSon tem = templateSonDao.subtemplatefind(id, name);
				if (tem != null) {
					CommUtil.responseBuildInfo(106, "该模板信息已存在", datamap);
					return datamap;
				}
			}
			Double money =  CommUtil.toDouble(maparam.get("money"));
			Integer chargeTime =  CommUtil.toInteger(maparam.get("chargeTime"));
			Integer chargeQuantity =  CommUtil.toInteger(maparam.get("chargeQuantity"));
			Double remark =  CommUtil.toDouble(maparam.get("remark"));
			String common1 =  CommUtil.toString(maparam.get("common1"));
			String common2 =  CommUtil.toString(maparam.get("common3"));
			String common3 =  CommUtil.toString(maparam.get("common3"));

			TemplateSon subclasscharge = new TemplateSon();
			subclasscharge.setId(id);
//			Double quantity = chargeQuantity * 100 ;
			if (money > 30000 || chargeQuantity > 30000 || chargeTime > 30000) {
				return 0;
			}
			subclasscharge.setName(name);
			subclasscharge.setMoney(money);
			subclasscharge.setChargeTime(chargeTime);
			subclasscharge.setChargeQuantity(chargeQuantity);
			subclasscharge.setRemark(remark);
			subclasscharge.setCommon1(common1);
			subclasscharge.setCommon2(common2);
			subclasscharge.setCommon3(common3);
			templateSonDao.updateSonTemplate(subclasscharge);
			datamap.put("tempson", id);
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	/**
	 * separate
	 * @Description： 删除子模板信息
	 * @author： origin 
	 */
	@Override
	public Object deleteSecondTemplateData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
//			User user = CommonConfig.getAdminReq(request);
			Integer id =  CommUtil.toInteger(maparam.get("id"));
			Integer isUpdate = CommUtil.toInteger(maparam.get("isUpdateChargeInfo"));
			if(isUpdate==1){
				String chargeinfo = CommUtil.toStringTrim(maparam.get("chargeInfo"));
				Integer tempid = CommUtil.toInteger(maparam.get("tempid"));
				TemplateParent temparen = new TemplateParent();
				temparen.setId(tempid);
				temparen.setChargeInfo(chargeinfo);
				templateDao.updateParentTemplate(temparen);
			}
			templateSonDao.deleteSonTempmanage(id);
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}


	/**
	 * separate
	 * @Description： 添加主模板
	 * @author： origin 
	 */
	@Override
	public Object insertFirstTemplateData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
//			User user = CommonConfig.getAdminReq(request);
			
			Integer merid =  CommUtil.toInteger(maparam.get("merid"));
			//模板类型0充电模板-1离线卡模板-2脉冲模板-3钱包模板-4在线卡模板
			Integer status =  CommUtil.toInteger(maparam.get("status"));
			String name =  CommUtil.toString(maparam.get("name"));
			
//			TemplateParent temp = templateDao.templateByName(merid, status, name);
//			if(null != temp){
//				CommUtil.responseBuildInfo(106, "该模板信息已存在", datamap);
//				return datamap;
//			}
//			
			boolean flas = verifyExist( name, merid, status);
			if(!flas){
				Integer permit =  CommUtil.toInteger(maparam.get("permit"));
				Integer walletpay =  CommUtil.toInteger(maparam.get("walletpay"));
				Integer ifmonth =  CommUtil.toInteger(maparam.get("ifmonth"));
				Integer ifalipay =  CommUtil.toInteger(maparam.get("ifalipay"));
				ifalipay = ifalipay == 0 ? 1 : ifalipay;
				String brandName =  CommUtil.toString(maparam.get("remark"));//模板品牌、公司
				String telephone =  CommUtil.toString(maparam.get("common1"));//电话
				//退费标准  1:按时间电量最小  2:时间最小  3:电量最小
				Integer common2 =  CommUtil.toInteger(maparam.get("common2"));
				Integer common3 =  CommUtil.toInteger(maparam.get("common3"));
				Integer grade =  CommUtil.toInteger(maparam.get("grade"));
				String chargeInfo =  CommUtil.toString(maparam.get("chargeinfo"));//充电详情
				if(grade.equals(1)){
					templateDao.insertClassifyTem(name+"(等级一)",name+"(等级二)",name+"(等级三)", merid,  status, 
							permit, walletpay, grade, brandName, telephone, common2);
					TemplateParent template = templateDao.templateByName( merid, status, name+"(等级一)");
					datamap.put("data", getTempDetails(template));
				}else {
					insertStairTempOperate( name, merid, status, permit, walletpay, null, grade, ifalipay, brandName, 
							telephone, common2, null, null, null,chargeInfo);
					
					TemplateParent template = templateDao.templateByName( merid, status, name);
					List<TemplateParent> list = new ArrayList<TemplateParent>();
					list.add(getTemplateInfo(template));
					datamap.put("data", list);
				}
				return CommUtil.responseBuildInfo(200, "成功", datamap);
			}else{
				CommUtil.responseBuildInfo(106, "该模板信息已存在", datamap);
				return datamap;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}


	/**
	 * separate
	 * @Description： 添加子模板	
	 * @author： origin 
	 */
	@Override
	public Object insertSecondTemplateData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer tempid =  CommUtil.toInteger(maparam.get("tempid"));
			Integer status =  CommUtil.toInteger(maparam.get("status"));
			Integer type =  CommUtil.toInteger(maparam.get("type"));
			String name =  CommUtil.toString(maparam.get("name"));
			Double money =  CommUtil.toDouble(maparam.get("money"));
			Integer chargeTime =  CommUtil.toInteger(maparam.get("chargeTime"));
			Integer chargeQuantity =  CommUtil.toInteger(maparam.get("chargeQuantity"));
			Double remark =  CommUtil.toDouble(maparam.get("remark"));
			String common1 =  CommUtil.toString(maparam.get("common1"));
			String common2 =  CommUtil.toString(maparam.get("common2"));
			String common3 =  CommUtil.toString(maparam.get("common3"));
			Integer isUpdate = CommUtil.toInteger(maparam.get("isUpdateChargeInfo"));
			if(isUpdate==1){
				String chargeinfo = CommUtil.toStringTrim(maparam.get("chargeInfo"));
				TemplateParent temparen = new TemplateParent();
				temparen.setId(tempid);
				temparen.setChargeInfo(chargeinfo);
				templateDao.updateParentTemplate(temparen);
			}
			if (money > 30000 || chargeQuantity > 30000 || chargeTime > 30000) {
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			TemplateSon template = templateSonDao.subtemplatefind( tempid,  name);
			if (template != null) {
				CommUtil.responseBuildInfo(106, "该模板信息已存在", datamap);
				return datamap;
			}
			TemplateSon subclass = new TemplateSon();
			subclass.setTempparid(tempid);
			subclass.setName(name);
			subclass.setStatus(status);
			subclass.setType(type);
			subclass.setMoney(money);
			subclass.setChargeTime(chargeTime);
//			chargeQuantity = chargeQuantity * 100;
			subclass.setChargeQuantity(chargeQuantity);
			subclass.setRemark(remark);
			subclass.setCommon1(common1);
			subclass.setCommon2(common2);
			subclass.setCommon3(common3);
			templateSonDao.insertSonTemplate(subclass);
			TemplateSon temson = templateSonDao.subtemplatefind(tempid, name);
			datamap.put("tempson", temson);
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}


	/**
	 * newest WeChat
	 * @Description：修改模板充电提示信息
	 * @author： origin   2019年12月3日 上午9:56:44 
	 */
	@Override
	public Object editTempForInfo(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer tempid =  CommUtil.toInteger(maparam.get("tempid"));
			String code =  CommUtil.toString(maparam.get("code"));
			StringBuffer chargedata = new StringBuffer();
			
			CodeSystemParam systemparam = codeSystemParamDao.selectCodeSystemParamByCode(code);
			
			Integer firstgearpower = 200;
			Integer secondgearpower = 400;
			Integer thirdgearpower = 600;
			Integer fourthgearpower = 800;
			Integer powertime2 = 75;
			Integer powertime3 = 50;
			Integer powertime4 = 25;
			
			if(systemparam!=null){
				firstgearpower = CommUtil.toInteger(systemparam.getPowerMax1());
				secondgearpower = CommUtil.toInteger(systemparam.getPowerMax2());
				thirdgearpower = CommUtil.toInteger(systemparam.getPowerMax3());
				fourthgearpower = CommUtil.toInteger(systemparam.getPowerMax4());
				powertime2 = CommUtil.toInteger(systemparam.getPowerTim2());
				powertime3 = CommUtil.toInteger(systemparam.getPowerTim3());
				powertime4 = CommUtil.toInteger(systemparam.getPowerTim4());
			}
			
			TemplateParent template = templateDao.getParentTemplateOne(tempid);
			if(template==null){
				CommUtil.responseBuildInfo(102, "模板不存在", datamap);
				return datamap;
			}
			List<TemplateSon> templateson = templateSonDao.getSonTemplateLists(tempid);
			if(templateson==null || templateson.isEmpty() || templateson.get(0)==null){
				CommUtil.responseBuildInfo(102, "子模板不存在或为空", datamap);
				return datamap;
			}
//			String tempchargeinfo = CommUtil.toString(template.getChargeInfo());
			Double money = CommUtil.toDouble(templateson.get(0).getMoney());
			Double time = CommUtil.toDouble(templateson.get(0).getChargeTime());
			Double chargetime1 = time;
			Double chargetime2 = CommUtil.toDouble(time * powertime2)/100;
			Double chargetime3 = CommUtil.toDouble(time * powertime3)/100;
			Double chargetime4 = CommUtil.toDouble(time * powertime4)/100;
			chargedata.append(money+"元/"+chargetime1+"分钟 (0W~"+firstgearpower+"W) \n");
			chargedata.append(money+"元/"+chargetime2+"分钟 ("+firstgearpower+"W~"+secondgearpower+"W) \n");
			chargedata.append(money+"元/"+chargetime3+"分钟 ("+secondgearpower+"W~"+thirdgearpower+"W) \n");
			chargedata.append(money+"元/"+chargetime4+"分钟 ("+thirdgearpower+"W~"+fourthgearpower+"W)");
			
			datamap.put("resultinfo", chargedata.toString());
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	/**
	 * @Description： 查询分等级模板
	 * @author： origin   2019年12月21日 下午5:03:23 
	 */
	@Override
	public List<TemplateParent> templateGradeInfo(Integer dealid, Integer status, Integer tempid) {
		List<TemplateParent> list = null;
		try {
			list = templateDao.templateGradeInfo(dealid, status, tempid);
			for(TemplateParent item : list){
				Object listSon = templateSonDao.getSonTemplateLists(CommUtil.toInteger(item.getId()));
				item.setGather(listSon);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<TemplateParent>();
		}
	}

	//=== v3设备模板  ============================================================================================
	
	/**
	 * @Description：添加或修改v3设备模板（主模板含子模板）
	 * @param request
	 * @author： origin
	 */
	@Override
	public Object insertAmendTemp(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			String paratem = request.getParameter("paratem");
			JSONObject jsonMap = JSONObject.fromObject(paratem);
			Integer tempid = CommUtil.toInteger(jsonMap.get("id"));
			Object result = null;
			if(tempid.equals(-1)){//添加
				result = insertVDeviceTemp(request);
			}else{//修改
				result = AmendTempVDeviceTemp(request);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	 public Object AmendTempVDeviceTemp(HttpServletRequest request) {
		 Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			String paratem = request.getParameter("paratem");
			JSONObject jsonMap = JSONObject.fromObject(paratem);
			Integer tempid = CommUtil.toInteger(jsonMap.get("id"));
			String temname = CommUtil.toString(jsonMap.get("temName"));
			String brandname = CommUtil.toString(jsonMap.get("loName"));
			String servephone = CommUtil.toString(jsonMap.get("tel"));
//				Integer walletpay = CommUtil.toInteger(jsonMap.get("walletpay"));
//				Integer permit = CommUtil.toInteger(jsonMap.get("permit"));
			Integer walletpay = CommUtil.toInteger(jsonMap.get("temporaryc"));
			Integer permit = CommUtil.toInteger(jsonMap.get("ref"));
			Integer ifalipay = CommUtil.toInteger(jsonMap.get("ifalipay"));
			ifalipay = ifalipay == 0 ? 1 : ifalipay;
			Integer rank = CommUtil.toInteger(jsonMap.get("rank"));
			rank = rank == 0 ? 720 : rank;
			String chargeinfo = CommUtil.toStringTrim(jsonMap.get("chargeInfo"));
//				TemplateParent template = templateDao.getParentTemplateOne(tempid);
			TemplateParent temppare = new TemplateParent();
			temppare.setId(tempid);
			temppare.setName(temname);
			temppare.setRemark(brandname);
			temppare.setCommon1(servephone);
			temppare.setWalletpay(walletpay);
			temppare.setIfalipay(ifalipay);
			temppare.setPermit(permit);
			temppare.setRank(rank);
			temppare.setChargeInfo(chargeinfo);
			Integer num = templateDao.updateParentTemplate(temppare);

			String tempowerlist = CommUtil.toString(jsonMap.get("mid1"));
			String temtimelist = CommUtil.toString(jsonMap.get("mid2"));
			String temmoneylist = CommUtil.toString(jsonMap.get("mid3"));
			JSONArray tempower = JSONArray.fromObject(tempowerlist);
			JSONArray temtime = JSONArray.fromObject(temtimelist);
			JSONArray temmoney = JSONArray.fromObject(temmoneylist);
			for (int i = 0; i < tempower.size(); i++) {
				JSONObject item = tempower.getJSONObject(i);
				Integer tempsonid = CommUtil.toInteger(item.get("id"));
				Double everymoney = CommUtil.toDouble(item.get("everymoney"));
				String powerstart = CommUtil.toString(item.get("powerstart"));
				String powerend = CommUtil.toString(item.get("powerend"));
				TemplateSon tempson = new TemplateSon();
				tempson.setId(tempsonid);
				tempson.setMoney(everymoney);
				tempson.setCommon1(powerstart);
				tempson.setCommon2(powerend);
				tempson.setChargeQuantity(0);
				templateSonDao.updateSonTemplate(tempson);
			}
			for (int i = 0; i < temtime.size(); i++) {
				JSONObject item = temtime.getJSONObject(i);
				Integer tempsonid = CommUtil.toInteger(item.get("id"));
				String showtitle = CommUtil.toString(item.get("showtitle"));
				Integer chargetime = CommUtil.toInteger(item.get("chargetime"));
				TemplateSon tempson = new TemplateSon();
				tempson.setId(tempsonid);
				tempson.setName(showtitle);
				tempson.setChargeTime(chargetime);
				tempson.setChargeQuantity(0);
				templateSonDao.updateSonTemplate(tempson);
			}
			for (int i = 0; i < temmoney.size(); i++) {
				JSONObject item = temmoney.getJSONObject(i);
				Integer tempsonid = CommUtil.toInteger(item.get("id"));
				String showmoney = CommUtil.toString(item.get("showmoney"));
				Double money = CommUtil.toDouble(item.get("money"));
				Integer chargetime = 0;
				Integer quantity = 0;
				TemplateSon tempson = new TemplateSon();
				tempson.setId(tempsonid);
				tempson.setName(showmoney);
				tempson.setMoney(money);
				tempson.setChargeQuantity(0);
				chargetime = CommUtil.toInteger(money * 60);
				tempson.setChargeTime(chargetime);
				quantity = CommUtil.toInteger(money * 100);
				tempson.setChargeQuantity(quantity);
				templateSonDao.updateSonTemplate(tempson);
			}
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
		return datamap;
	}


	public Object insertVDeviceTemp(HttpServletRequest request) {
		User dealer = (User) request.getSession().getAttribute("user");
		Integer dealid = CommUtil.toInteger(dealer.getId());
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			String paratem = request.getParameter("paratem");
			JSONObject jsonMap = JSONObject.fromObject(paratem);
			String temname = CommUtil.toString(jsonMap.get("temName"));
			Integer status = 6;
//			Integer status = CommUtil.toInteger(jsonMap.get("status"));
			String brandname = CommUtil.toString(jsonMap.get("loName"));
			String servephone = CommUtil.toString(jsonMap.get("tel"));
			Integer walletpay = CommUtil.toInteger(jsonMap.get("temporaryc"));
			Integer ifalipay = CommUtil.toInteger(jsonMap.get("ifalipay"));
			
			ifalipay = ifalipay == 0 ? 1 : ifalipay;
			Integer rank = CommUtil.toInteger(jsonMap.get("rank"));
			rank = rank == 0 ? 720 : rank;
			Integer permit = CommUtil.toInteger(jsonMap.get("ref"));
			String chargeinfo = CommUtil.toString(jsonMap.get("chargeInfo"));
			
			TemplateParent temppare = new TemplateParent();
			temppare.setStatus(status);
			temppare.setMerchantid(dealid);
			temppare.setName(temname);
			temppare.setRemark(brandname);
			temppare.setCommon1(servephone);
			temppare.setWalletpay(walletpay);
			temppare.setIfalipay(ifalipay);
			temppare.setPermit(permit);
			temppare.setRank(rank);
			temppare.setChargeInfo(chargeinfo);
			templateDao.insertStairTemp(temppare);
			
			TemplateParent template = templateDao.templateByName( dealid, status, temname);
			if(template== null) template = new TemplateParent();
			Integer tempid = CommUtil.toInteger(template.getId());
			datamap.put("tempid", tempid);
			datamap.put("template", template);
			if(tempid.equals(0)) return CommUtil.responseBuildInfo(201, "主模板添加不正确", datamap);
			String tempowerlist = CommUtil.toString(jsonMap.get("mid1"));
			String temtimelist = CommUtil.toString(jsonMap.get("mid2"));
			String temmoneylist = CommUtil.toString(jsonMap.get("mid3"));
			JSONArray tempower = JSONArray.fromObject(tempowerlist);
			JSONArray temtime = JSONArray.fromObject(temtimelist);
			JSONArray temmoney = JSONArray.fromObject(temmoneylist);
			for (int i = 0; i < tempower.size(); i++) {//功率
				JSONObject item = tempower.getJSONObject(i);
				Integer type = 1;
				Double paymoney = CommUtil.toDouble(item.get("everymoney"));
				String common1 = CommUtil.toString(item.get("powerstart"));
				String common2 = CommUtil.toString(item.get("powerend"));
				Integer chargetime = CommUtil.toInteger(item.get("chargetime"));
				String timeNew =  CommUtil.toString(System.currentTimeMillis()).substring(8);
				String tempname = "收费标准"+ timeNew;
				TemplateSon templateson = templateSonDao.subtemplatefind(tempid, tempname);
				if(templateson==null){
					TemplateSon tempson = new TemplateSon();
					tempson.setTempparid(tempid);
					tempson.setName(tempname);
					//tempson.setStatus(Integer.valueOf(1));
					tempson.setType(type);
					tempson.setMoney(paymoney);
					tempson.setChargeTime(chargetime);
					tempson.setChargeQuantity(0);
					tempson.setCommon1(common1);
					tempson.setCommon2(common2);
					templateSonDao.insertSonTemplate(tempson);
				}
			}
			for (int i = 0; i < temtime.size(); i++) {//时间模板
				JSONObject item = temtime.getJSONObject(i);
				Integer type = 2;
				Double paymoney = 0.00;
				String tempname = CommUtil.toString(item.get("showtitle"));
				Integer chargetime = CommUtil.toInteger(item.get("chargetime"));
				TemplateSon templateson = templateSonDao.subtemplatefind(tempid, tempname);
				if(templateson==null){
					TemplateSon tempson = new TemplateSon();
					tempson.setTempparid(tempid);
					tempson.setName(tempname);
					tempson.setType(type);
					tempson.setMoney(paymoney);
					tempson.setChargeTime(chargetime);
					tempson.setChargeQuantity(0);
					templateSonDao.insertSonTemplate(tempson);
				}
			}
			for (int i = 0; i < temmoney.size(); i++) {//金额模板
				JSONObject item = temmoney.getJSONObject(i);
				Integer type = 3;
				String tempname = CommUtil.toString(item.get("showmoney"));
				Double paymoney = CommUtil.toDouble(item.get("money"));
				Integer chargetime = 0;
				Integer quantity = 0;
				TemplateSon templateson = templateSonDao.subtemplatefind(tempid, tempname);
				if(templateson==null){
					TemplateSon tempson = new TemplateSon();
					tempson.setTempparid(tempid);
					tempson.setName(tempname);
					tempson.setType(type);
					tempson.setMoney(paymoney);
					chargetime = CommUtil.toInteger(paymoney * 60);
					tempson.setChargeTime(chargetime);
					quantity = CommUtil.toInteger(paymoney * 100);
					tempson.setChargeQuantity(quantity);
					templateSonDao.insertSonTemplate(tempson);
				}
			}
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
		return datamap;
	}
	
	/**
	 * @Description：
	 * @param request
	 * @author： origin
	 */
	@Override
	public Object additionAssignTemp(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			Integer type = CommUtil.toInteger(maparam.get("type"));
			Integer tempid = CommUtil.toInteger(maparam.get("parid"));
			String common1 = CommUtil.toString(maparam.get("common1"));
			String common2 = CommUtil.toString(maparam.get("common2"));
			Integer chargetime = CommUtil.toInteger(maparam.get("chargetime"));
			String tempname = CommUtil.toString(maparam.get("name"));
			Double paymoney = CommUtil.toDouble(maparam.get("money"));

			Integer isUpdate = CommUtil.toInteger(maparam.get("isUpdateChargeInfo"));
			String chargeinfo = CommUtil.toStringTrim(maparam.get("chargeInfo"));
			if(chargetime.equals(0)) chargetime = CommUtil.toInteger(paymoney*60);
			String timeNew =  CommUtil.toString(System.currentTimeMillis()).substring(8);
			if(type.equals(1)) tempname = "收费标准"+ timeNew;
			TemplateParent tempparen = templateDao.getParentTemplateOne(tempid);
			if(tempparen!=null){
				if(isUpdate==1){
					TemplateParent temparen = new TemplateParent();
					temparen.setId(tempid);
					temparen.setChargeInfo(chargeinfo);
					templateDao.updateParentTemplate(temparen);
				}
				TemplateSon template = templateSonDao.subtemplatefind(tempid, tempname);
				if(template==null){
					TemplateSon templateSon = new TemplateSon();
					templateSon.setTempparid(tempid);
					templateSon.setName(tempname);
					//templateSon.setStatus(Integer.valueOf(1));
					templateSon.setType(type);
					templateSon.setMoney(paymoney);
					templateSon.setChargeTime(chargetime);
					templateSon.setCommon1(common1);
					templateSon.setCommon2(common2);
					templateSonDao.insertSonTemplate(templateSon);
					TemplateSon tempdata = templateSonDao.subtemplatefind(tempid, tempname);
					tempdata = tempdata == null ? new TemplateSon() : tempdata;
					datamap.put("template", tempdata);
					CommUtil.responseBuildInfo(200, "成功", datamap);
				}else{
					datamap.put("template", template);
					CommUtil.responseBuildInfo(201, "该模板已存在", datamap);
				}
			}else{
				CommUtil.responseBuildInfo(201, "主模板不存在", datamap);
			}
		} catch (Exception e) {
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
			e.printStackTrace();
		}
		return datamap;
	}


	/**
	 * @Description：添加v3主模板
	 * @param request
	 * @author： origin
	 */
	@Override
	public Object insertVDeviceTem(HttpServletRequest request) {
		User dealer = (User) request.getSession().getAttribute("user");
		Integer dealid = CommUtil.toInteger(dealer.getId());
		Map<String, Object> datamap = new HashMap<String, Object>();
		TemplateParent templateParent = new TemplateParent();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			String name = CommUtil.toString(maparam.get("name"));
			String remark = CommUtil.toString(maparam.get("remark"));
			String common1 = CommUtil.toString(maparam.get("common1"));
			Integer walletpay = CommUtil.toInteger(maparam.get("walletpay"));
			Integer permit = CommUtil.toInteger(maparam.get("permit"));
			Integer status = CommUtil.toInteger(maparam.get("status"));
			String chargeinfo = CommUtil.toStringTrim(maparam.get("chargeInfo"));
			
			templateParent.setStatus(status);
			templateParent.setName(name);
			templateParent.setMerchantid(dealid);
			templateParent.setRemark(remark);
			templateParent.setPermit(permit);
			templateParent.setWalletpay(walletpay);
			templateParent.setCommon1(common1);
			templateParent.setChargeInfo(chargeinfo);
			templateDao.insertStairTemp(templateParent);
			
			TemplateParent template = templateDao.templateByName( dealid, status, name);
			if(template== null) template = new TemplateParent();
			datamap.put("temppart", template);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
			e.printStackTrace();
		}
		return datamap;
	}

	
	public Object insertVDeviceTem(Map<String, Object> parame, User dealer) {
		Integer dealid = CommUtil.toInteger(dealer.getId());
		Map<String, Object> datamap = new HashMap<String, Object>();
		TemplateParent templateParent = new TemplateParent();
		try {
			String name = CommUtil.toString(parame.get("name"));
			String remark = CommUtil.toString(parame.get("remark"));
			String common1 = CommUtil.toString(parame.get("common1"));
			Integer walletpay = CommUtil.toInteger(parame.get("walletpay"));
			Integer permit = CommUtil.toInteger(parame.get("permit"));
			Integer status = CommUtil.toInteger(parame.get("status"));
			
			templateParent.setStatus(status);
			templateParent.setName(name);
			templateParent.setMerchantid(dealid);
			templateParent.setRemark(remark);
			templateParent.setPermit(permit);
			templateParent.setWalletpay(walletpay);
			templateParent.setCommon1(common1);
			//templateDao.insertParentTemplate(templateParent);
			templateDao.insertStairTemp(templateParent);
			
			TemplateParent template = templateDao.templateByName( dealid, status, name);
			if(template== null) template = new TemplateParent();
			datamap.put("temppart", template);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
			e.printStackTrace();
		}
		return datamap;
	}	
	
	/**
	 * @Description：
	 * @param request
	 * @author： origin
	 * @createTime：2020年3月17日下午1:50:34
	 */
	@Override
	public Object updataVDeviceTem(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			String paratem = request.getParameter("paratem");
			JSONObject jsonMap = JSONObject.fromObject(paratem);
			Integer tempid = CommUtil.toInteger(jsonMap.get("id"));
			String temname = CommUtil.toString(jsonMap.get("temName"));
			String brandname = CommUtil.toString(jsonMap.get("loName"));
			String servephone = CommUtil.toString(jsonMap.get("tel"));
			Integer walletpay = CommUtil.toInteger(jsonMap.get("ref"));
			Integer permit = CommUtil.toInteger(jsonMap.get("temporaryc"));
//			TemplateParent template = templateDao.getParentTemplateOne(tempid);
			String chargeinfo = CommUtil.toStringTrim(jsonMap.get("chargeInfo"));
			TemplateParent temppare = new TemplateParent();
			temppare.setId(tempid);
			temppare.setName(temname);
			temppare.setRemark(brandname);
			temppare.setCommon1(servephone);
			temppare.setWalletpay(walletpay);
			temppare.setPermit(permit);
			temppare.setChargeInfo(chargeinfo);
			Integer num = templateDao.updateParentTemplate(temppare);

			String tempowerlist = CommUtil.toString(jsonMap.get("mid1"));
			String temtimelist = CommUtil.toString(jsonMap.get("mid2"));
			String temmoneylist = CommUtil.toString(jsonMap.get("mid3"));
//			String tempowerlist = CommUtil.toString(jsonMap.get("tempower"));
//			String temtimelist = CommUtil.toString(jsonMap.get("temtime"));
//			String temmoneylist = CommUtil.toString(jsonMap.get("temmoney"));
			JSONArray tempower = JSONArray.fromObject(tempowerlist);
			JSONArray temtime = JSONArray.fromObject(temtimelist);
			JSONArray temmoney = JSONArray.fromObject(temmoneylist);
			for (int i = 0; i < tempower.size(); i++) {
				JSONObject item = tempower.getJSONObject(i);
				Integer tempsonid = CommUtil.toInteger(item.get("id"));
				Double everymoney = CommUtil.toDouble(item.get("everymoney"));
				String powerstart = CommUtil.toString(item.get("powerstart"));
				String powerend = CommUtil.toString(item.get("powerend"));
				TemplateSon tempson = new TemplateSon();
				tempson.setId(tempsonid);
				tempson.setMoney(everymoney);
				tempson.setCommon1(powerstart);
				tempson.setCommon2(powerend);
				tempson.setChargeQuantity(0);
				templateSonDao.updateSonTemplate(tempson);
			}
			for (int i = 0; i < temtime.size(); i++) {
				JSONObject item = temtime.getJSONObject(i);
				Integer tempsonid = CommUtil.toInteger(item.get("id"));
				String showtitle = CommUtil.toString(item.get("showtitle"));
				Integer chargetime = CommUtil.toInteger(item.get("chargetime"));
				TemplateSon tempson = new TemplateSon();
				tempson.setId(tempsonid);
				tempson.setName(showtitle);
				tempson.setChargeTime(chargetime);
				tempson.setChargeQuantity(0);
				templateSonDao.updateSonTemplate(tempson);
			}
			for (int i = 0; i < temmoney.size(); i++) {
				JSONObject item = temmoney.getJSONObject(i);
				Integer tempsonid = CommUtil.toInteger(item.get("id"));
				String showmoney = CommUtil.toString(item.get("showmoney"));
				Double money = CommUtil.toDouble(item.get("money"));

				Integer chargetime = 0;
				Integer quantity = 0;
				TemplateSon tempson = new TemplateSon();
				tempson.setId(tempsonid);
				tempson.setName(showmoney);
				tempson.setMoney(money);
				chargetime = CommUtil.toInteger(money * 60);
				tempson.setChargeTime(chargetime);
				quantity = CommUtil.toInteger(money * 100);
				tempson.setChargeQuantity(quantity);
				templateSonDao.updateSonTemplate(tempson);
			}
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
		return datamap;
	}


	/**
	 * @Description：
	 * @param request
	 * @author： origin
	 * @createTime：2020年3月17日下午2:59:46
	 */
	@Override
	public Object selectVDeviceTem(HttpServletRequest request) {
//		User dealer = (User) request.getSession().getAttribute("user");
//		Integer dealid = CommUtil.toInteger(dealer.getId());
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			Integer tempid = CommUtil.toInteger(maparam.get("tempid"));
			TemplateParent temppare = templateDao.getParentTemplateOne(tempid);
			List<TemplateSon> tempson = templateSonDao.getSonTemplateLists(tempid);
			temppare.setGather(tempson);
			Map<String, Object> mapinfo = new HashMap<>();
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
			mapinfo.put("temppare", temppare);
			mapinfo.put("tempower", tempower);
			mapinfo.put("temtime", temtime);
			mapinfo.put("temmoney", temmoney);
			mapinfo.put("tempson", tempson);
			datamap.put("result", mapinfo);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
			e.printStackTrace();
		}
		return datamap;
	}


//=== employ 选择 ======================================================================================================
	/**
	 * @Description：如果模板不存在，复制添加默认模板到商户名下
	 * @param status  dealid  servePhone
	 * @author： origin
	 * @createTime：2020年3月24日上午10:10:00
	 */
	public Map<String, Object> copyDirectTemp(Integer status, Integer dealid, String servePhone){
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Integer num = templateDao.verifyTemplate( dealid, status);
			TemplateParent temp =  null;
			if(num.equals(0)){//判断不存在该类型模板
				Integer tempsysid = 0;
				Integer tempdealid = 0;
				List<TemplateParent> list = templateDao.getParentTemplateListByMerchantidwolf(0, status);
				list = list == null ? new ArrayList<TemplateParent>() : list;
				if(list.size()>0){
					temp = list.get(0);
					String name = CommUtil.trimToEmpty(temp.getName()).replace("系统", "默认");
					tempsysid = temp.getId();
					temp.setId(null);
					temp.setName(name);
					temp.setMerchantid(dealid);
					if(status.equals(0) || status.equals(6)) temp.setCommon1(servePhone);
					//templateDao.insertParentTemplate(temp);
					templateDao.insertStairTemp(temp);
					List<TemplateParent> listdeal = templateDao.getParentTemplateListByMerchantidwolf(dealid, status);
					TemplateParent tempdeal =  listdeal.get(0);
					tempdealid = tempdeal.getId();
					
					List<TemplateSon> tempson = templateSonDao.getSonTemplateLists(tempsysid);
					for(TemplateSon item : tempson){
						item.setId(null);
						item.setTempparid(tempdealid);
						templateSonDao.insertSonTemplate(item);
					}
					datamap.put("listdata", listdeal);
					datamap.put("listsondata", tempson);
					CommUtil.responseBuildInfo(200, "模板信息添加成功", datamap);
				}else{
					CommUtil.responseBuildInfo(200, "不存在系统模板", datamap);
				}
			}else{
				List<TemplateParent> listdatadeal = templateDao.getParentTemplateListByMerchantidwolf(dealid, status);
				datamap.put("listdata", listdatadeal);
				CommUtil.responseBuildInfo(200, "商户存在该类型模板", datamap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	
	/**
	 * employ 选择	ORIGIN
	 * @Description：根据主模板id查询单独一个主模板信息
	 * @author： origin
	 * @createTime：2020年3月18日下午1:43:11
	 */
	@Override
	public TemplateParent inquireDirectTemp(Integer tempid) {
		TemplateParent parentTemplateOne = templateDao.getParentTemplateOne(tempid);
		parentTemplateOne = parentTemplateOne == null ? new TemplateParent() : parentTemplateOne;
		return parentTemplateOne;
	}
	
	/**
	 * employ 选择	ORIGIN
	 * @Description：根据商户id查询所有同类型主模板信息
	 * @author： origin
	 * @createTime：2020年3月19日下午2:06:38
	 */
	@Override
	public List<TemplateParent> inquireTempByStatus(Integer dealid, Integer status) {
		try {
			List<TemplateParent> list = templateDao.getParentTemplateListByMerchantidwolf(dealid, status);
			list = list == null ? new ArrayList<TemplateParent>() : list;
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<TemplateParent>();
		}
	}
	

	/**
	 * @Description：查询获取到系统模板
	 * @author： origin ：2020年7月2日下午2:37:30
	 * @comment:
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> inquireSystemTempData(Integer status, String version) {
		Map<String, Object> systemTempData = new HashMap<String, Object>();
		try {
			 TemplateParent systemTemp = null;
			 List<TemplateParent> tempinfo = templateDao.getParentTemplateListByMerchantidwolf(0, status);
			 if(version.equals("02") || version.equals("07")){
				 for(TemplateParent item : tempinfo){
					 Integer teid = CommUtil.toInteger(item.getId());
					 if(teid.equals(1)) systemTemp = item;
				 }
			 } else if(version.equals("00") || version.equals("01") || version.equals("05") || version.equals("06")){
				 systemTemp = tempinfo.get(0);
			 }else{//03   04 和其他
				 systemTemp = tempinfo.get(0);
			 }
			 systemTemp = systemTemp == null ? new TemplateParent() : systemTemp;
			 systemTempData = JSON.parseObject(JSON.toJSONString(systemTemp), Map.class);
			 return systemTempData;
		} catch (Exception e) {
			e.printStackTrace();
			return systemTempData;
		}
	}

	public Map<String, Object> inquireSystemSonTempData(Integer tempid, Integer type) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<TemplateSon>  listSon = templateSonDao.getSonTemplateLists(tempid);
			if(type==1){//v3设备
				List<TemplateSon> tempower = new ArrayList<>();
				List<TemplateSon> temtime =  new ArrayList<>();
				List<TemplateSon> temmoney =  new ArrayList<>();
				for(TemplateSon item : listSon){
					Integer typeson = CommUtil.toInteger(item.getType());
					if(typeson.equals(1)){
						tempower.add(item);
					}else if(typeson.equals(2)){
						temtime.add(item);
					}else if(typeson.equals(3)){
						temmoney.add(item);
					} 
				}
				data.put("gather1", tempower);
				data.put("gather2", temtime);
				data.put("gather3", temmoney);
			}else{
				data.put("gather", listSon);
			}
			 return data;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}

	@Override
	public TemplateParent inquireTempData(Integer dealid, Integer status) {
		TemplateParent temp = new TemplateParent();
		try {
			temp.setMerchantid(dealid);
			temp.setStatus(status);
			List<TemplateParent> list = templateDao.selectpartemp(temp);
			if(list==null || list.size()==0){
				temp.setMerchantid(0);
				list = templateDao.selectpartemp(temp);
			}
			list = list == null ? new ArrayList<TemplateParent>() :list;
			if(list.size()==0) return new TemplateParent();
			TemplateParent tempdata = list.get(0);
			for(TemplateParent item : list){
				Integer isdefault = CommUtil.toInteger(item.getCommon3());
				if(isdefault.equals(1)){
					tempdata = item;
					break;
				}
			}
			return tempdata;
		} catch (Exception e) {
			e.printStackTrace();
			return new TemplateParent();
		}
	}

	/**
	 * @Description：电脑端添加或修改v3设备模板（主模板含子模板）
	 * @author： origin
	 * @createTime：2020年4月11日下午2:50:36
	 */
	@Override
	public Object insertOrModifyTemp(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			String paratem = request.getParameter("paratem");
			JSONObject jsonMap = JSONObject.fromObject(paratem);
			Integer tempid = CommUtil.toInteger(jsonMap.get("id"));
			Object result = null;
			if(tempid.equals(-1)){//添加
				result = insertOrTempData(request);
			}else{//修改
				result = modifyOrTempData(request);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	public Object insertOrTempData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			String paratem = request.getParameter("paratem");
			JSONObject jsonMap = JSONObject.fromObject(paratem);
			String temname = CommUtil.toString(jsonMap.get("name"));
			Integer dealid = CommUtil.toInteger(jsonMap.get("merid"));
			Integer status = 6;
			String brandname = CommUtil.toString(jsonMap.get("remark"));
			String servephone = CommUtil.toString(jsonMap.get("common1"));
			if("null".equals(brandname)) brandname = null;
			if("null".equals(servephone)) servephone = null;
			Integer ifalipay = CommUtil.toInteger(jsonMap.get("ifalipay"));
			ifalipay = ifalipay == 0 ? 1 : ifalipay;
			Integer walletpay = CommUtil.toInteger(jsonMap.get("walletpay"));
			Integer permit = CommUtil.toInteger(jsonMap.get("permit"));
			Integer rank = CommUtil.toInteger(jsonMap.get("rank"));
			rank = rank == 0 ? 720 : rank;
			String chargeinfo = CommUtil.toStringTrim(jsonMap.get("chargeInfo"));
			TemplateParent temppare = new TemplateParent();
			temppare.setStatus(status);
			temppare.setMerchantid(dealid);
			temppare.setName(temname);
			temppare.setRemark(brandname);
			temppare.setCommon1(servephone);
			temppare.setWalletpay(walletpay);
			temppare.setIfalipay(ifalipay);
			temppare.setPermit(permit);
			temppare.setRank(rank);
			temppare.setChargeInfo(chargeinfo);
			templateDao.insertStairTemp(temppare);
//			templateDao.insertParentTemplate(temppare);
			
			TemplateParent template = templateDao.templateByName( dealid, status, temname);
			if(template== null) template = new TemplateParent();
			Integer tempid = CommUtil.toInteger(template.getId());
			datamap.put("tempid", tempid);
			datamap.put("template", template);
			if(tempid.equals(0)) return CommUtil.responseBuildInfo(201, "主模板添加不正确", datamap);
			String tempowerlist = CommUtil.toString(jsonMap.get("gather1"));
			String temtimelist = CommUtil.toString(jsonMap.get("gather2"));
			String temmoneylist = CommUtil.toString(jsonMap.get("gather3"));
			JSONArray tempower = JSONArray.fromObject(tempowerlist);
			JSONArray temtime = JSONArray.fromObject(temtimelist);
			JSONArray temmoney = JSONArray.fromObject(temmoneylist);
			for (int i = 0; i < tempower.size(); i++) {//功率
				JSONObject item = tempower.getJSONObject(i);
				Integer type = 1;
				Double paymoney = CommUtil.toDouble(item.get("money"));
				String common1 = CommUtil.toString(item.get("common1"));
				String common2 = CommUtil.toString(item.get("common2"));
				Integer chargetime = CommUtil.toInteger(item.get("chargetime"));
				String timeNew =  CommUtil.toString(System.currentTimeMillis()).substring(8);
				String tempname = "收费标准"+ timeNew;
				TemplateSon templateson = templateSonDao.subtemplatefind(tempid, tempname);
				if(templateson==null){
					TemplateSon tempson = new TemplateSon();
					tempson.setTempparid(tempid);
					tempson.setName(tempname);
					//tempson.setStatus(Integer.valueOf(1));
					tempson.setType(type);
					tempson.setMoney(paymoney);
					tempson.setChargeTime(chargetime);
					tempson.setChargeQuantity(0);
					tempson.setCommon1(common1);
					tempson.setCommon2(common2);
					templateSonDao.insertSonTemplate(tempson);
				}
			}
			for (int i = 0; i < temtime.size(); i++) {//时间模板
				JSONObject item = temtime.getJSONObject(i);
				Integer type = 2;
				Double paymoney = 0.00;
				String tempname = CommUtil.toString(item.get("name"));
				Integer chargetime = CommUtil.toInteger(item.get("chargeTime"));
				TemplateSon templateson = templateSonDao.subtemplatefind(tempid, tempname);
				if(templateson==null){
					TemplateSon tempson = new TemplateSon();
					tempson.setTempparid(tempid);
					tempson.setName(tempname);
					tempson.setType(type);
					tempson.setMoney(paymoney);
					tempson.setChargeTime(chargetime);
					tempson.setChargeQuantity(0);
					templateSonDao.insertSonTemplate(tempson);
				}
			}
			for (int i = 0; i < temmoney.size(); i++) {//金额模板
				JSONObject item = temmoney.getJSONObject(i);
				Integer type = 3;
				String tempname = CommUtil.toString(item.get("name"));
				Double paymoney = CommUtil.toDouble(item.get("money"));
				Integer chargetime = 0;
				Integer quantity = 0;
				TemplateSon templateson = templateSonDao.subtemplatefind(tempid, tempname);
				if(templateson==null){
					TemplateSon tempson = new TemplateSon();
					tempson.setTempparid(tempid);
					tempson.setName(tempname);
					tempson.setType(type);
					tempson.setMoney(paymoney);
					chargetime = CommUtil.toInteger(paymoney * 60);
					tempson.setChargeTime(chargetime);
					quantity = CommUtil.toInteger(paymoney * 100);
					tempson.setChargeQuantity(quantity);
					templateSonDao.insertSonTemplate(tempson);
				}
			}
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
		return datamap;
	}
	
	public Object modifyOrTempData(HttpServletRequest request) {
		 Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			String paratem = request.getParameter("paratem");
			JSONObject jsonMap = JSONObject.fromObject(paratem);
			Integer tempid = CommUtil.toInteger(jsonMap.get("id"));
			String temname = CommUtil.toString(jsonMap.get("name"));
			String brandname = CommUtil.trimToEmpty(jsonMap.get("remark"));//trimToEmpty
			String servephone = CommUtil.trimToEmpty(jsonMap.get("common1"));
			if(brandname=="null") brandname = null;
			if(servephone=="null") servephone = null;
			Integer walletpay = CommUtil.toInteger(jsonMap.get("walletpay"));
			Integer permit = CommUtil.toInteger(jsonMap.get("permit"));
			Integer ifalipay = CommUtil.toInteger(jsonMap.get("ifalipay"));
			ifalipay = ifalipay == 0 ? 1 : ifalipay;
			Integer rank = CommUtil.toInteger(jsonMap.get("rank"));
			rank = rank == 0 ? 720 : rank;
			String chargeinfo = CommUtil.toStringTrim(jsonMap.get("chargeInfo"));
			TemplateParent temppare = new TemplateParent();
			temppare.setId(tempid);
			temppare.setName(temname);
			temppare.setRemark(brandname);
			temppare.setCommon1(servephone);
			temppare.setWalletpay(walletpay);
			temppare.setIfalipay(ifalipay);
			temppare.setPermit(permit);
			temppare.setRank(rank);
			temppare.setChargeInfo(chargeinfo);
			Integer num = templateDao.updateParentTemplate(temppare);

			String tempowerlist = CommUtil.toString(jsonMap.get("gather1"));
			String temtimelist = CommUtil.toString(jsonMap.get("gather2"));
			String temmoneylist = CommUtil.toString(jsonMap.get("gather3"));
			JSONArray tempower = JSONArray.fromObject(tempowerlist);
			JSONArray temtime = JSONArray.fromObject(temtimelist);
			JSONArray temmoney = JSONArray.fromObject(temmoneylist);
			for (int i = 0; i < tempower.size(); i++) {
				JSONObject item = tempower.getJSONObject(i);
				Integer tempsonid = CommUtil.toInteger(item.get("id"));
				Double everymoney = CommUtil.toDouble(item.get("money"));
				String powerstart = CommUtil.toString(item.get("common1"));
				String powerend = CommUtil.toString(item.get("common2"));
				TemplateSon tempson = new TemplateSon();
				tempson.setId(tempsonid);
				tempson.setMoney(everymoney);
				tempson.setCommon1(powerstart);
				tempson.setCommon2(powerend);
				tempson.setChargeQuantity(0);
				templateSonDao.updateSonTemplate(tempson);
			}
			for (int i = 0; i < temtime.size(); i++) {
				JSONObject item = temtime.getJSONObject(i);
				Integer tempsonid = CommUtil.toInteger(item.get("id"));
				String showtitle = CommUtil.toString(item.get("name"));
				Integer chargetime = CommUtil.toInteger(item.get("chargeTime"));
				TemplateSon tempson = new TemplateSon();
				tempson.setId(tempsonid);
				tempson.setName(showtitle);
				tempson.setChargeTime(chargetime);
				tempson.setChargeQuantity(0);
				templateSonDao.updateSonTemplate(tempson);
			}
			for (int i = 0; i < temmoney.size(); i++) {
				JSONObject item = temmoney.getJSONObject(i);
				Integer tempsonid = CommUtil.toInteger(item.get("id"));
				String showmoney = CommUtil.toString(item.get("name"));
				Double money = CommUtil.toDouble(item.get("money"));
				Integer chargetime = 0;
				Integer quantity = 0;
				TemplateSon tempson = new TemplateSon();
				tempson.setId(tempsonid);
				tempson.setName(showmoney);
				tempson.setMoney(money);
				tempson.setChargeQuantity(0);
				chargetime = CommUtil.toInteger(money * 60);
				tempson.setChargeTime(chargetime);
				quantity = CommUtil.toInteger(money * 100);
				tempson.setChargeQuantity(quantity);
				templateSonDao.updateSonTemplate(tempson);
			}
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
		return datamap;
	}


	@Override
	public short clacV3TimeBytemp(Integer tempparid, Double money) {
		List<TemplateSon> tempson = templateSonDao.getSonTemplateLists(tempparid);
		double baseMoney = 0.0;
		for (TemplateSon templateSon : tempson) {
			if (templateSon.getType() == 1) {
				baseMoney = templateSon.getMoney();
				break;
			}
		}
		short time = 0;
		Double clacTimeDouble = (money / baseMoney) * 60;
		clacTimeDouble = Math.ceil(clacTimeDouble) + 0.0;
		if (baseMoney != 0) {
			time = clacTimeDouble.shortValue();
		}
		return time;
	}

	/*-- *** 请暂时不要在*号范围内添加   thanks ***********************************************************   --*/
	/**
	 * ORIGGIN  ANEW AFRESH
	 * @Description：ORIGGIN  ANEW AFRESH  根据参数获取主模板信息 返回数据类型为list<实体类>多条数据
	 * @param id:模板id  merid:商户id  stairid:分等级使用，默认为0
	 * @param status: 模板类型
	 * @return 返回数据类型为list<实体类>多条数据
	 * @author： origin 	2020年7月2日下午6:09:45
	 */
	List<TemplateParent> queryStairTempListInfo(Integer id, Integer merid, Integer status, Integer stairid){
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ORIGGIN  ANEW AFRESH
	 * @Description：ORIGGIN  ANEW AFRESH 根据参数获取主模板信息 返回数据类型为 实体类（单条）数据
	 * @param id:模板id  merid:商户id  stairid:分等级使用，默认为0
	 * @param status: 模板类型
	 * @return 返回数据类型为 实体类（单条）数据
	 * @author： origin 	2020年7月2日下午6:09:45
	 */
	TemplateParent searchStairTempOneInfo(Integer id, Integer merid, Integer status, Integer stairid){
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ORIGGIN  ANEW AFRESH
	 * @Description：ORIGGIN  ANEW AFRESH  根据参数获取主模板信息 返回数据类型为list<map>多条数据
	 * @param id:模板id  merid:商户id  stairid:分等级使用，默认为0
	 * @param status: 模板类型
	 * @return 返回数据类型为list<map>多条数据
	 * @author： origin 	2020年7月2日下午6:09:45
	 */
	List<Map<String, Object>> inquireStairTempListInfo(Integer id, Integer merid, Integer status, Integer stairid){
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ORIGGIN  ANEW AFRESH
	 * @Description：ORIGGIN  ANEW AFRESH 根据参数获取主模板信息 返回数据类型为 map（单条）数据
	 * @param id:模板id  merid:商户id  stairid:分等级使用，默认为0
	 * @param status: 模板类型
	 * @return 返回数据类型为 map（单条）数据
	 * @author： origin 	2020年7月2日下午6:09:45
	 */
	Map<String, Object> selectStairTempOneInfo(Integer id, Integer merid, Integer status, Integer stairid){
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ORIGGIN  ANEW AFRESH
	 * @param merid:商户id 	status: 模板类型
	 * @return Integer 类型的数量值
	 * @author： origin 	2020年7月2日下午6:13:50
	 */
	Integer acquireTempNumber(Integer merid, Integer status){
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/*-- *** 请暂时不要在*号范围内添加   thanks ***********************************************************   --*/
	
	//==================================================================================================================
	public Map<String, Object> tempDefaultObje(List<TemplateSon> tempson) {
		Map<String, Object> map = new HashMap<String, Object>();
		int temp1 = 0;
		for (TemplateSon templateSon : tempson) {
			if (temp1 == 0 && !templateSon.getName().contains("1元")) {
				map.put("defaultchoose", templateSon.getId());
				map.put("defaultindex", temp1);
				break;
			} else if (temp1 == 1) {
				map.put("defaultchoose", templateSon.getId());
				map.put("defaultindex", temp1);
				break;
			}
			temp1++;
		}
		return map;
	}

/**********************************************************************************************/
	/**
	 * @Description: 添加模板信息
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月18日 下午2:07:46
	 * @common:   
	 */
	@Override
	public Object additionAndEditTemp(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			String param = request.getParameter("parameter");
			JSONObject jsonMap = JSONObject.fromObject(param);
			Integer tempid = CommUtil.toInteger(jsonMap.get("tempid"));
			Object result = null;
			if(tempid.equals(-1) || tempid== 0){//添加
				result = insertTempData(request);
			}else{//修改
				result = editTempData(request);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	public Object insertTempData(HttpServletRequest request) {
		
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			String paratem = request.getParameter("parameter");
			JSONObject jsonMap = JSONObject.fromObject(paratem);
			TemplateParent temppare = templateValuation(paratem);
			templateDao.insertStairTemp(temppare);
			Integer temid = temppare.getId();
			datamap.put("tempid", temid);
			String temsonlist = CommUtil.toString(jsonMap.get("tempson"));
			String tempowerlist = CommUtil.toString(jsonMap.get("tempower"));
			String temtimelist = CommUtil.toString(jsonMap.get("temtime"));
			String temmoneylist = CommUtil.toString(jsonMap.get("temmoney"));
			
			if(temsonlist!=null) insertTemplateSonData(temid, temsonlist);
			if(tempowerlist!=null) insertTemplateSonData(temid, tempowerlist);
			if(temtimelist!=null) insertTemplateSonData(temid, temtimelist);
			if(temmoneylist!=null) insertTemplateSonData(temid, temmoneylist);
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**
	 * @method_name: insertTemplateSonData
	 * @Description: 
	 * @param temid
	 * @param temsonlist
	 * @Author: origin  创建时间:2020年8月18日 下午6:58:43
	 * @common:   
	 */
	public Object insertTemplateSonData(Integer tempid, String tempsonlist) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			JSONArray tempsonvalue = JSONArray.fromObject(tempsonlist);
			for (int i = 0; i < tempsonvalue.size(); i++) {
				JSONObject item = tempsonvalue.getJSONObject(i);
				Map<String, String> datatemson = CommUtil.toHashMap(item);
				TemplateSon tempson = templatesonValuation(tempid, datatemson, i);
//				Integer type = CommUtil.toInteger(item.get("type"));
				String name = CommUtil.toString(item.get("name"));//titlename
				TemplateSon templateson = templateSonDao.subtemplatefind(tempid, name);
//				
				if(templateson==null){
					templateSonDao.insertTemplateSon(tempson);
				}
			}
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	

	public Object editTempData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try{
			User dealer = new User();
			String paratem = request.getParameter("parameter");
			JSONObject jsonMap = JSONObject.fromObject(paratem);
			TemplateParent temppare = templateValuation(paratem);
			Integer tempid = CommUtil.forInteger(jsonMap.get("tempid"));
			templateDao.updateParentTemplate(temppare);
			
			String temsonlist = CommUtil.toString(jsonMap.get("tempson"));
			String tempowerlist = CommUtil.toString(jsonMap.get("tempower"));
			String temtimelist = CommUtil.toString(jsonMap.get("temtime"));
			String temmoneylist = CommUtil.toString(jsonMap.get("temmoney"));
			
			if(temsonlist!=null) updateTemplateSonData(tempid, temsonlist);
			if(tempowerlist!=null) updateTemplateSonData(tempid, tempowerlist);
			if(temtimelist!=null) updateTemplateSonData(tempid, temtimelist);
			if(temmoneylist!=null) updateTemplateSonData(tempid, temmoneylist);
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}


	/**
	 * @method_name: updateTemplateSonData
	 * @Description: 
	 * @param tempid
	 * @param temsonlist
	 * @return 
	 * @Author: origin  创建时间:2020年8月20日 下午5:42:34
	 * @common:   
	 */
	public Map<String, Object> updateTemplateSonData(Integer tempid, String tempsonlist) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			JSONArray tempsonvalue = JSONArray.fromObject(tempsonlist);
			for (int i = 0; i < tempsonvalue.size(); i++) {
				JSONObject item = tempsonvalue.getJSONObject(i);
				Map<String, String> datatemson = CommUtil.toHashMap(item);
				TemplateSon tempson = templatesonValuation(tempid, datatemson, i);
				templateSonDao.updateSonTemplate(tempson);
			}
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		
	}


	/**
	 * @Description: 
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月20日 下午3:16:49
	 * @common:   
	 */
	@Override
	public Object additionTempSonData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer tempid = CommUtil.toInteger(maparam.get("tempid"));
			datamap.put("tempid", tempid);
			if(tempid==(-1)) return CommUtil.responseBuildInfo(200, "成功", datamap);
			String name = CommUtil.toString(maparam.get("name"));//titlename
			
			Map<String, String> datatemson = CommUtil.toHashMap(maparam);
			String num = CommUtil.toString(System.currentTimeMillis()).substring(4);
			TemplateSon tempson = templatesonValuation(tempid, datatemson, CommUtil.toInteger(num));
			TemplateSon templateson = templateSonDao.subtemplatefind(tempid, name);
			if(templateson==null){
				templateSonDao.insertTemplateSon(tempson);
				datamap.put("id", tempson.getId());
				datamap.put("tempson", tempson);
				CommUtil.responseBuildInfo(200, "成功", datamap);
			}else{
				CommUtil.responseBuildInfo(200, "模板已存在", datamap);
			}
			return datamap;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}


	/**
	 * @Description: 
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月20日 下午4:25:25
	 * @common:   
	 */
	@Override
	public Object deleteTemplateSonData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer id =  CommUtil.toInteger(maparam.get("id"));
			templateSonDao.deleteSonTempmanage(id);
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}


	/**
	 * @Description: 
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月24日 下午5:15:15
	 * @common:   
	 */
	@Override
	public Map<String, Object> templataPreview(HttpServletRequest request) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			String paratem = request.getParameter("paratem");
			JSONObject jsonMap = JSONObject.fromObject(paratem);
			TemplateParent template = templateValuation(paratem);
			resultdata.put("template", template);
			Integer tempid = CommUtil.forInteger(jsonMap.get("tempid"));
			String temsonlist = CommUtil.toString(jsonMap.get("tempson"));
			String tempowerlist = CommUtil.toString(jsonMap.get("tempower"));
			String temtimelist = CommUtil.toString(jsonMap.get("temtime"));
			String temmoneylist = CommUtil.toString(jsonMap.get("temmoney"));
			List<TemplateSon> listtempson = new ArrayList<>();
			List<TemplateSon> listtempower = new ArrayList<>();
			List<TemplateSon> listtemtime = new ArrayList<>();
			List<TemplateSon> listtemmoney = new ArrayList<>();
			if(temsonlist!=null){ 
				JSONArray tempsonvalue = JSONArray.fromObject(temsonlist);
				for (int i = 0; i < tempsonvalue.size(); i++) {
					JSONObject item = tempsonvalue.getJSONObject(i);
					Map<String, String> datatemson = CommUtil.toHashMap(item);
					TemplateSon tempson = templatesonValuation(tempid, datatemson, i);
					listtempson.add(tempson);
				}
			}
			if(tempowerlist!=null){
				JSONArray tempsonvalue = JSONArray.fromObject(tempowerlist);
				for (int i = 0; i < tempsonvalue.size(); i++) {
					JSONObject item = tempsonvalue.getJSONObject(i);
					Map<String, String> datatemson = CommUtil.toHashMap(item);
					TemplateSon tempson = templatesonValuation(tempid, datatemson, i);
					listtempower.add(tempson);
				}
			}
			if(temtimelist!=null){
				JSONArray tempsonvalue = JSONArray.fromObject(temtimelist);
				for (int i = 0; i < tempsonvalue.size(); i++) {
					JSONObject item = tempsonvalue.getJSONObject(i);
					Map<String, String> datatemson = CommUtil.toHashMap(item);
					TemplateSon tempson = templatesonValuation(tempid, datatemson, i);
					listtemtime.add(tempson);
				}
			}
			if(temmoneylist!=null){
				JSONArray tempsonvalue = JSONArray.fromObject(temmoneylist);
				for (int i = 0; i < tempsonvalue.size(); i++) {
					JSONObject item = tempsonvalue.getJSONObject(i);
					Map<String, String> datatemson = CommUtil.toHashMap(item);
					TemplateSon tempson = templatesonValuation(tempid, datatemson, i);
					listtemmoney.add(tempson);
				}
			}
			resultdata.put("listtempson", listtempson);
			resultdata.put("listtempower", listtempower);
			resultdata.put("listtemtime", listtemtime);
			resultdata.put("listtemmoney", listtemmoney);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultdata;
	}
	
	/**
	 * 模板赋值
	 */
	public TemplateParent templateValuation(String paratem) {
		try {
			User dealer = new User();
			JSONObject jsonMap = JSONObject.fromObject(paratem);
			Integer paysource = CommUtil.forInteger(jsonMap.get("paysource"));
			if(paysource==0 || paysource==2){
				dealer = (User) request.getSession().getAttribute("admiin");
			}else{
				dealer = (User) request.getSession().getAttribute("user");
			}
			Integer tempid = CommUtil.forInteger(jsonMap.get("tempid"));
			Integer dealid = CommUtil.forInteger(jsonMap.get("merid"));
			if(dealid==null) dealid = CommUtil.toInteger(dealer.getId());
			TemplateParent temppare = new TemplateParent();
			if(tempid!=null) temppare.setId(tempid);
			temppare.setName(CommUtil.toString(jsonMap.get("tempname")));
			temppare.setMerchantid(dealid);
			temppare.setStatus(CommUtil.forInteger(jsonMap.get("type")));
			temppare.setRemark(CommUtil.toString(jsonMap.get("brandname")));
			temppare.setCommon1(CommUtil.toString(jsonMap.get("servicecall")));
			temppare.setPermit(CommUtil.forInteger(jsonMap.get("ispermit")));
			temppare.setWalletpay(CommUtil.forInteger(jsonMap.get("walletpay")));
			temppare.setIfalipay(CommUtil.forInteger(jsonMap.get("ifalipay")));
			temppare.setIfmonth(CommUtil.forInteger(jsonMap.get("ifmonth")));
			temppare.setCommon2(CommUtil.forInteger(jsonMap.get("returnnorm")));
			temppare.setCommon3(CommUtil.toString(jsonMap.get("isdefault")));
			temppare.setGrade(CommUtil.forInteger(jsonMap.get("grade")));
			temppare.setRank(CommUtil.forInteger(jsonMap.get("rank")));
			temppare.setChargeInfo(CommUtil.toString(jsonMap.get("hintinfo")));
			temppare.setCreateTime(CommUtil.toDateTime());
			return temppare;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 子模板赋值
	 */
	public TemplateSon templatesonValuation(Integer tempid, Map<String, String> datatemson, Integer num) {
		try {
			Integer type = CommUtil.toInteger(datatemson.get("type"));
			String name = CommUtil.toString(datatemson.get("name"));//titlename
			Integer tempsonid = CommUtil.toInteger(datatemson.get("tempsonid"));
			Double paymoney = CommUtil.toDouble(datatemson.get("money"));
			paymoney = CommUtil.decimals(1, paymoney);
			Double accountmoney = CommUtil.toDouble(datatemson.get("accountmoney"));
			Integer chargetime = CommUtil.toInteger(datatemson.get("chargetime"));
			
			Double quantityobj = CommUtil.toDouble(datatemson.get("quantity"));
			Integer quantity = CommUtil.toInteger(CommUtil.decimals(1, quantityobj) * 100);
			//Integer quantity = CommUtil.toInteger(datatemson.get("quantity"));
			String powerstart = CommUtil.toString(datatemson.get("powerstart"));
			String powerend = CommUtil.toString(datatemson.get("powerend"));
			if(type==0){
				
			}else if(type==1){
				if(name==null) name = "收费标准"+ num;
			}else if(type==2){
				
			}else if(type==3){
				chargetime = CommUtil.toInteger(paymoney * 60);
			}
			Date datetime = new Date();
			TemplateSon tempson = new TemplateSon();
			tempson.setId(tempsonid);
			tempson.setTempparid(tempid);
			tempson.setName(name);
			tempson.setType(type);
			tempson.setMoney(paymoney);
			tempson.setRemark(accountmoney);
			tempson.setChargeTime(chargetime);
			tempson.setChargeQuantity(quantity);
			tempson.setCommon1(powerstart);
			tempson.setCommon2(powerend);
			tempson.setCreateTime(datetime);
			return tempson;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
/**********************************************************************************************/
	
	
	
	
	
	
	
	
	
	
	
	
}
