package com.hedong.hedongwx.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.domain.Data;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.AreaRelevance;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.BasicsService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.MobileSendUtil;
import com.hedong.hedongwx.utils.StringUtil;

/**
 * @Description： 
 * @author  origin  创建时间：   2019年10月6日 上午10:02:29  
 */
@Service
public class BasicsServiceImpl implements BasicsService{
	
	

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private UserService userService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private EquipmentService equipmentService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * separate
	 * @Description： 获取手机验证码信息
	 * @author： origin 
	 */
	@Override
	public Object getCaptchaData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			String phone =  CommUtil.trimToEmpty(maparam.get("phone"));
			Boolean bool = existUser(phone);
			if(bool){
				CommUtil.responseBuildInfo(103, "无该手机号存在信息", datamap);
				return datamap;
			}
			String captcha = StringUtil.getRandNum();
			String[] params = { captcha, "3" };
			String[] cellphone = { phone };
			MobileSendUtil.TemplateMobileSend(params, cellphone);
			datamap.put("captcha", captcha);
			datamap.put("dataTime", new Data());
			datamap.put("sendtime", CommUtil.toDateTime());
			CommUtil.responseBuildInfo(200, "验证码发送成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	/**
	 * 判断用户表中是否存在该用户
	 * @return
	 * @throws Exception 
	 */
	public boolean existUser(String mobile) {
		User user =  userService.existAdmin(mobile);
		if(user==null) return true;
		return false;
	}

	/**
	 * separate
	 * @Description： 判断密码是否正确
	 * @author： origin 
	 */
	@Override
	public Object estimatePassword(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			String password =  CommUtil.toString(maparam.get("password"));
			String cipher = DigestUtils.md5Hex(password);
			User user = CommonConfig.getAdminReq(request);
			if(cipher.equals(user.getPassword())){
				CommUtil.responseBuildInfo(200, "成功", datamap);
			}else{
				CommUtil.responseBuildInfo(201, "密码输入不正确", datamap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * separate
	 * @Description： 获取所有商户信息，包含管理员
	 * @author： origin   2019年11月20日 上午9:58:08
	 */
	@Override
	public Object getAllDealerData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
//			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			List<Map<String, Object>> userList = userService.getUsertoSortList("0,2");//查询管理员与商户
			datamap.put("userList", userList);
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	//=== employ 选择 === 模板  ========================================================================================	
	
	/**
	 * employ 选择	ORIGIN
	 * @Description：获取设备模板信息
	 * @param 商户模板:tempid、小区id:areaid、商户id:dealid、设备硬件版本号：version
	 * @author： origin
	 * @return 主模板信息
	 * @createTime：2020年3月21日下午4:04:17
	 */
	@Override
	public  Map<String, Object> inquireDeviceTempData(Integer tempid, Integer dealid, String version) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			/*
			 设备硬件版本 00-默认出厂版本、01-十路智慧款、02-电轿款、03-脉冲投币、04-离线充值机、
			 05-16路智慧款、06-20路智慧款、07单路交流桩  08 v3设备款
			 */
			Integer tempstatus = 0;
			if(version.equals("03")){
				tempstatus = 2;
			} else if(version.equals("04")){
				tempstatus = 1;
			} else if(version.equals("08")){
				tempstatus = 6;
			}
			datamap = inquireDirectTempData( tempid,  dealid,  tempstatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datamap;
	}
	
	/**
	 * employ 选择	ORIGIN
	 * @Description：获取除设备模板的其他模板信息
	 * @param  小区id:areaid、商户id:dealid、模板状态：tempstatus
	 * @author： origin
	 * @return 主模板信息
	 * @createTime：2020年3月21日下午4:04:17
	 */
	@Override
	public  Map<String, Object> inquireOtherTempData(Integer areaid, Integer dealid, Integer tempstatus) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Integer tempid = null;
			Area areaData = areaService.selectByIdArea(areaid);
			//模板类型 0充电模板  1离线卡模板     2模拟投币模板（脉冲模板） 3钱包模板   4在线卡模板  5包月模板  6:v3模板
			if(tempstatus.equals(3)){//钱包模板
				tempid = CommUtil.toInteger(areaData.getTempid());
			} else if(tempstatus.equals(4)){//在线卡模板
				tempid = CommUtil.toInteger(areaData.getTempid2());
			} else if(tempstatus.equals(5)){//包月模板
				tempid = 0;
			}
			datamap = inquireDirectTempData( tempid,  dealid,  tempstatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datamap;
	}

	
	/**
	 * @Description：获取主模板信息
	 * @author： origin
	 * @createTime：2020年3月24日下午5:40:10
	 */
	public  Map<String, Object> inquireDirectTempData(Integer tempid, Integer dealid, Integer tempstatus) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			TemplateParent tempdata = null;
			Integer mercid = null;
			if(!CommUtil.toInteger(tempid).equals(0)){
				tempdata = templateService.inquireDirectTemp(tempid);
				mercid = CommUtil.toInteger(tempdata.getMerchantid());
			}
			if(tempid.equals(0) || !mercid.equals(dealid)){
				List<TemplateParent> tempinfo = templateService.inquireTempByStatus(dealid, tempstatus);
				if(tempinfo.size()>0){
					for(TemplateParent item : tempinfo){
						Integer defaultchoose = CommUtil.toInteger(item.getCommon3());
						if(defaultchoose.equals(1)){
							tempdata = item;
						}
					}
					if(tempdata == null) tempdata = tempinfo.get(0);
				}else{
					tempinfo = templateService.inquireTempByStatus(0, tempstatus);
					tempdata = tempinfo.get(0);
				}
			}
			datamap.put("tempdata", tempdata);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datamap;
	}
	
	/**
	 * employ 选择	ORIGIN
	 * @Description：获取子模板的模板信息
	 * @param 模板:tempid
	 * @return 
	 * @return 返回子(下级)模板的集合
	 * @author： origin
	 * @createTime：2020年3月24日下午5:52:10
	 */
	@Override
	public Map<String, Object> inquireSubordinateTempData(Integer tempid) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			List<TemplateSon> tempson = templateService.getSonTemplateLists(tempid);
			int temp1 = 0;
//			for (TemplateSon sontemp : tempson) {
//				if (temp1 == 0 && !sontemp.getName().contains("1元")) {
//					datamap.put("tempson", tempson);
//					model.addAttribute("defaultchoose", templateSon.getId());
//					model.addAttribute("defaultindex", temp1);
//					break;
//				} else if (temp1 == 1) {
//					datamap.put("tempson", tempson);
//					model.addAttribute("defaultchoose", templateSon.getId());
//					model.addAttribute("defaultindex", temp1);
//					break;
//				}
//				temp1++;
//			}
//			model.addAttribute("templatelist", templatelist);
			
			datamap.put("tempson", tempson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datamap;
	}
	
	/**
	 * employ 选择	ORIGIN
	 * @Description：获取小区服务电话信息
	 * @param  小区id:areaid
	 * @author： origin
	 */
	@Override
	public String inquireAreaServeTel(Integer areaid) {
		String phone = null;
		try {
			List<Map<String, Object>> result =  areaService.selectAreaRelev(areaid);
			for(Map<String, Object> item : result){
				Integer type = CommUtil.toInteger(item.get("type"));
				Integer manageid =  CommUtil.toInteger(item.get("partid"));
				if(type.equals(3) && !manageid.equals(0)){
					User manageuser = userService.selectUserById(manageid);
					phone = CommUtil.toString(manageuser.getPhoneNum());
				}
//				Integer type = CommUtil.toInteger(item.get("type"));
//				String remarkphone = CommUtil.toString(item.get("remark"));
//				Integer manageid =  CommUtil.toInteger(item.get("partid"));
//				if(type.equals(3) && remarkphone!=null){
//					phone = remarkphone;
//				}else if(type.equals(3) && !manageid.equals(0)){
//					User manageuser = userService.selectUserById(manageid);
//					String mansevphone = CommUtil.toString(manageuser.getServephone());
//					String manphone = CommUtil.toString(manageuser.getPhoneNum());
//					phone = mansevphone == null ? manphone : manphone;
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phone;
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
			if(phone==null && !areaid.equals(0)){
				List<Map<String, Object>> result =  areaService.selectAreaRelev(areaid);
				for(Map<String, Object> item : result){
					Integer type = CommUtil.toInteger(item.get("type"));
					Integer manageid =  CommUtil.toInteger(item.get("partid"));
					if(type.equals(3) && !manageid.equals(0)){
						User manageuser = userService.selectUserById(manageid);
						phone = CommUtil.toString(manageuser.getPhoneNum());
					}
				}
			}
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
	 * employ 选择	ORIGIN
	 * @Description：获取服务电话信息
	 * @param tempPhone:模板电话     managePhone:小区管理电话   servePhone:商户服务电话   phone:商户电话
	 * @return  String类型的服务电话 
	 * @author： origin
	 * @createTime：2020年3月24日下午5:49:48
	 */
	@Override
	public String getServephoneData(String tempPhone, String managePhone, String servePhone, String phone ){
		String servecall = null;
		try {
			servecall = CommUtil.toString(tempPhone);
			servecall = servecall == null ? managePhone : servecall;
			servecall = servecall == null ? servePhone : servecall;
			servecall = servecall == null ? phone : servecall;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return servecall;
	}

	/**
	 * employ 选择	ORIGIN
	 * @Description：设备获取使用模板（获取主模板信息）
	 * @param tempid:模板id  dealid:商户id  version:版本信息
	 * @author： origin
	 * @createTime：2020年5月5日下午7:17:09
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> inquireDirectTempData(Integer tempid, Integer dealid, String devicenum, String version) {
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
			}
			if(version==null){
				version = "00";
				System.out.println("ORIGIN  输出版本为空的原因(版本为空)     "+"tempid:"+tempid +"    dealid:"+dealid+"   devicenum:"+devicenum+"   version:"+version);
			}
			Integer mercid = null;
			tempid = CommUtil.toInteger(tempid);
			if(!tempid.equals(0)){//模板不为0时（即设备没有选中模板时）
				tempdata = templateService.inquireDirectTemp(tempid);
				mercid = CommUtil.toInteger(tempdata.getMerchantid());
				if(mercid!=0 && !mercid.equals(dealid)){
					if(!"在线卡".equals(version) && !"钱包".equals(version)){
						tempid = 0;
					}
				}
			} 
			if(tempid.equals(0)){//未绑定模板
				List<TemplateParent> tempinfo = templateService.inquireTempByStatus(0, tempstatus);
				if(version.equals("02") || version.equals("07")){
					for(TemplateParent item : tempinfo){
						Integer teid = CommUtil.toInteger(item.getId());
						if(teid.equals(1)){
							tempdata = item;
							break;
						}
					}
				} else if(version.equals("00") || version.equals("01") || version.equals("05") || version.equals("06")){
					tempdata = tempinfo.get(0);
				}else{//03   04 和其他
					tempdata = tempinfo.get(0);
				}
				equipmentService.updateTempidByEquipmentCode( devicenum, tempdata.getId());
			}
			if(CommUtil.toInteger(tempdata.getId()).equals(0)){
				System.out.println("ORIGIN  输出模板为空的原因2     "+"tempid:"+tempid +"    dealid:"+dealid+"   devicenum:"+devicenum+"   version:"+version);
			}
			datamap = JSON.parseObject(JSON.toJSONString(tempdata), Map.class);
//			datamap.put("tempdata", tempdata);
			return datamap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datamap;
	}
	
	
}
