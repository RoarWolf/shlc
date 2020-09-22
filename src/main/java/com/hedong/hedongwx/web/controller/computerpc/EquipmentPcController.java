package com.hedong.hedongwx.web.controller.computerpc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.CodeSystemParam;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.Realchargerecord;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.service.AllPortRecordService;
import com.hedong.hedongwx.service.AllPortStatusService;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;


@Controller
@RequestMapping(value = "/pcequipment")
public class EquipmentPcController {

	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private UserService userService;
	@Autowired
	private AllPortStatusService allPortStatusService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private AllPortRecordService allPortRecordService;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private AreaService areaService;
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	
	/**
	 * @Description： 搜索指定类型的设备信息
	 * @author： origin 创建时间：   2019年8月23日 下午3:18:01
	 */
	@RequestMapping(value = "/inquireequipm")
	@ResponseBody
	public Object allowInquireEquipm(String type, Integer merid, Model model){
		Object result = equipmentService.selectAssignTypeEqui( merid, type);
		return result;
	}
	
	/**
	 * @Description： 搜索指定类型的设备信息
	 * @author： origin 创建时间：   2019年8月23日 下午3:18:01
	 */
	@RequestMapping(value = "/updateequitemp")
	@ResponseBody
	public Object updateEquiTemp(String codeList, Integer temid, Model model){
		Map<String, Object> map = new HashMap<>();
		try {
			List<String> list = CommUtil.jsonList(codeList);
			for (Object item : list){
				String code = CommUtil.toString(item);
				equipmentService.updateTempidByEquipmentCode(code, temid);
			}
			map = CommonConfig.messg(200);
		} catch (Exception e) {
			e.printStackTrace();
			map = CommonConfig.messg(403);
		}
		return JSON.toJSONString(map);
	}
	
	
	
	@RequestMapping(value = "/allowlookmapinfo")
	public String lookmapinfo(String lon, String lat, Model model){
		model.addAttribute("lon", lon);
		model.addAttribute("lat", lat);
		return "computer/mapshowinfo";
	}
	
	/**
	 * @Description： PC端查看系统模板
	 * @author： origin          
	 */
	@RequestMapping(value = "/lookSystemTemp")
	public String lookSystemTemp(Model model){
		//充电模板
		Object chargesontem = templateService.getSonTemp(0);

		List<TemplateParent>  templatelist = templateService.getParentTemplateListByMerchantidwolf(0, null);
		for(TemplateParent tem : templatelist){
			Integer status = tem.getStatus();
			//模板类型 0充电模板  1离线卡模板     2模拟投币模板（脉冲模板） 3钱包模板   4在线卡模板
			if(status==0){
				model.addAttribute("temelectriccar", tem);
			}else if(status==1){
				model.addAttribute("temoffline", tem);
			}else if(status==2){
				model.addAttribute("temincoins", tem);
			}else if(status==3){
				model.addAttribute("temwallet", tem);
			}else if(status==4){
				model.addAttribute("temonline", tem);
			}
		}
		model.addAttribute("chargesontem", chargesontem);
		model.addAttribute("templatelist", templatelist);
		return "computer/tempsystem";
	}
	
	/**
	 * PC端设备详情查看模板
	 */
	@RequestMapping(value = "/looktemp")
	public String lookTemp(String code, Integer merid,Model model){
		Equipment equipment = this.equipmentService.getEquipmentById(code);
		System.out.println("merid===" + merid);
		model.addAttribute("merid", merid);
		String url = "computer/tempcharge";
		if (equipment != null) {
			model.addAttribute("arecode", code);
			model.addAttribute("source", 1);
			List<TemplateParent> templatelist = null;
			if ("04".equals(equipment.getHardversion())) {
				templatelist = templateService.getParentTemplateListByMerchantidwolf(merid, 1);
				url = "computer/tempoffline";
				model.addAttribute("hardver", 3);
			} else if ("03".equals(equipment.getHardversion())) {
				templatelist = templateService.getParentTemplateListByMerchantidwolf(merid, 2);
				url = "computer/tempincoins";
				model.addAttribute("hardver", 2);
			} else {
				templatelist = templateService.getParentTemplateListByMerchantidwolf(merid, 0);
				model.addAttribute("hardver", 1);
			}
			List<TemplateParent> templatelist1 = new ArrayList<>();
			List<TemplateParent> tempgather = new ArrayList<TemplateParent>();
			if (templatelist != null && templatelist.size() > 0) {
				if (equipment.getTempid() != null) {
					for (int i = templatelist.size() - 1; i >= 0; i--) {
						TemplateParent templateParent = templatelist.get(i);
						if (equipment.getTempid().equals(templateParent.getId())) {
							templateParent.setPitchon(1);
							templatelist1.add(templateParent);
							templatelist.remove(templateParent);
						}
						if(CommUtil.toInteger(templateParent.getGrade())==1){
							tempgather = templateService.getTempDetails(templateParent);
						}
			        }
					templatelist1.addAll(templatelist);
					model.addAttribute("templatelist", templatelist1);
					model.addAttribute("tempgather", tempgather);
				} else {
					model.addAttribute("templatelist", templatelist);
				}
			}
		}
		return url;
	}
	/**
	 * 查询设备操作日志
	 * @author  origin          
	 * @version 创建时间：2019年2月25日  下午5:00:32
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/selectcodeoperatelog")
	public Object selectCodeOperateLog(HttpServletRequest request, HttpServletResponse response,  Model model) {
		PageUtils<Parameters> pageBean = equipmentService.selectCodeOperateLog(request);
		
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(null==startTime || startTime.equals(""))startTime = StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss", StringUtil.toDateTime(), 3);
		if(null==endTime || endTime.equals("")) endTime = StringUtil.getCurrentDateTime();
		//dealer:商户昵称   realname:商户姓名    phone:商户电话     nickname:操作人昵称    username:操作人名字  sort:种类   type:状态      startTime、endTime
		model.addAttribute("code", request.getParameter("code"));
		model.addAttribute("dealer", request.getParameter("dealer"));
		model.addAttribute("phone", request.getParameter("phone"));
		model.addAttribute("nickname", request.getParameter("nickname"));
		model.addAttribute("sort", request.getParameter("sort"));
		model.addAttribute("type", request.getParameter("type"));
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("operateLog", pageBean.getListMap());
		return "computer/codeoperatelog";
	}
	
	//查询设备列表 (跳转设备列表)
	@RequestMapping(value = "/selectEquList")
	public String selectEquList(HttpServletRequest request, HttpServletResponse response,Model model){
		PageUtils<Parameters> pageBean = equipmentService.selectEquList(request);
		String aid = request.getParameter("aid");
		if(aid==null) model.addAttribute("areastate", request.getParameter("areastate"));
		else model.addAttribute("areastate", request.getParameter("areaname"));
		
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("state", request.getParameter("state"));
		model.addAttribute("code", request.getParameter("code"));
		model.addAttribute("imei", request.getParameter("imei"));
		model.addAttribute("ccid", request.getParameter("ccid"));
		model.addAttribute("hardversion", request.getParameter("hardversion"));
		model.addAttribute("softversionnum", request.getParameter("softversionnum"));
		model.addAttribute("hardversionnum", request.getParameter("hardversionnum"));
		model.addAttribute("csq", request.getParameter("csq"));
		model.addAttribute("phoneNum", request.getParameter("phoneNum"));
		model.addAttribute("line", request.getParameter("line"));
		model.addAttribute("testnum", request.getParameter("testnum"));
		
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		model.addAttribute("equparam",pageBean.getMap());
		model.addAttribute("equipmentList",pageBean.getListMap());
		model.addAttribute("pageBean", pageBean);
		return "computer/equlist";
	}
	
	//查询蓝牙设备列表 (跳转设备列表)
	@RequestMapping(value = "/selectBluetoothList")
	public String selectBluetoothList(HttpServletRequest request, HttpServletResponse response,Model model){
		PageUtils<Parameters> pageBean = equipmentService.selectBluetoothEquList(request);
		String aid = request.getParameter("aid");
		if(aid==null) model.addAttribute("areastate", request.getParameter("areastate"));
		else model.addAttribute("areastate", request.getParameter("areaname"));
		
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("state", request.getParameter("state"));
		model.addAttribute("code", request.getParameter("code"));
		model.addAttribute("hardversion", request.getParameter("hardversion"));
		model.addAttribute("softversionnum", request.getParameter("softversionnum"));
		model.addAttribute("phoneNum", request.getParameter("phoneNum"));
		model.addAttribute("line", request.getParameter("line"));
		model.addAttribute("testnum", request.getParameter("testnum"));
		
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		model.addAttribute("equparam",pageBean.getMap());
		model.addAttribute("equipmentList",pageBean.getListMap());
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("last24Hours", equipmentService.selectLast24Hours());
		model.addAttribute("lastMonth", equipmentService.selectLastMonth());
		return "computer/bluetoothlist";
	}
	
	//查询设备日志记录
	@RequestMapping("/selectEquipmentLog")
	public Object selectEquipmentLog(HttpServletRequest request, HttpServletResponse response,  Model model) {
		String parm = request.getParameter("parm");

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(null==parm){
			endTime = StringUtil.getCurrentDateTime();
			startTime = StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss", endTime, 1);
		}
		PageUtils<Parameters> pageBean = allPortRecordService.selectEquipmentLog(request);
		model.addAttribute("equipmentnum", request.getParameter("equipmentnum"));
		model.addAttribute("port", request.getParameter("port"));
		model.addAttribute("status", request.getParameter("status"));
		model.addAttribute("time", request.getParameter("time"));
		model.addAttribute("power", request.getParameter("power"));
		model.addAttribute("elec", request.getParameter("elec"));
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("portRecord", pageBean.getListMap());
		if(null!=parm && parm.equals("0")){
			return "computer/allportRecordinfo";
		}else{
			return "computer/allportRecord";
		}
	}
	//更改设备版本号   @param code, hardversion
	@RequestMapping({ "/hardware" })
	@ResponseBody
	public Object tohardware( String code, String hardversion, Model model) throws Exception {
		Equipment equipment = new Equipment();
		equipment.setCode(code);
		equipment.setHardversion(hardversion);
//		try {
//			List<AllPortStatus> portStatusList = allPortStatusService.findPortStatusListByEquipmentnum(code, 20);
//			System.out.println("portStatusList===" + portStatusList);
//			if (portStatusList != null) {
//				int size = portStatusList.size();
//				if ("02".equals(hardversion)) {
//					editHardverAddPortStatus(size, code, 3, 2);
//				} else if ("01".equals(hardversion)) {
//					editHardverAddPortStatus(size, code, 11, 10);
//				} else if ("05".equals(hardversion)) {
//					editHardverAddPortStatus(size, code, 17, 16);
//				} else if ("06".equals(hardversion)) {
//					editHardverAddPortStatus(size, code, 21, 20);
//				} else {
//					editHardverAddPortStatus(size, code, 11, 10);
//				}
//			} else {
//				int size = 0;
//				if ("02".equals(hardversion)) {
//					editHardverAddPortStatus(size, code, 3, 2);
//				} else if ("01".equals(hardversion)) {
//					editHardverAddPortStatus(size, code, 11, 10);
//				} else if ("05".equals(hardversion)) {
//					editHardverAddPortStatus(size, code, 17, 16);
//				} else if ("06".equals(hardversion)) {
//					editHardverAddPortStatus(size, code, 21, 20);
//				} else {
//					editHardverAddPortStatus(size, code, 11, 10);
//				}
//			}
//		} catch (Exception e) {
//			System.out.println("修改版本号添加端口状态失败" + e.getMessage());
//		}
		if ("03".equals(hardversion)) {
			List<TemplateParent> templateList = templateService.getParentTemplateListByMerchantid(0, 2);
			equipment.setTempid(templateList.get(0).getId());
		} else if ("04".equals(hardversion)) {
			List<TemplateParent> templateList = templateService.getParentTemplateListByMerchantid(0, 1);
			equipment.setTempid(templateList.get(0).getId());
		} else if ("02".equals(hardversion)) {
			List<TemplateParent> templateList = templateService.getParentTemplateListByMerchantid(0, 0);
			equipment.setTempid(templateList.get(0).getId());
		} else {
			equipment.setTempid(0);
		}
		Equipment equipmentById = equipmentService.getEquipmentById(code);
		UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
		Integer merid = userEquipment == null ? 0 : userEquipment.getUserId();
		User user = (User) request.getSession().getAttribute("admin");
		if (user != null) {
			equipmentService.insertCodeoperatelog(code, 3, 1,merid, user.getId(), equipmentById.getHardversion());
		} else {
			equipmentService.insertCodeoperatelog(code, 3, 1, merid, 0, equipmentById.getHardversion());
		}
		return equipmentService.updateEquipment(equipment);
	}
	
	//判断密码是否正确    @param password
	@RequestMapping({ "/pwdjudge" })
	@ResponseBody
	public String pwdjudge( String password){
		User admin = (User) this.request.getSession().getAttribute("admin");
		String pwd = DigestUtils.md5Hex(password);
		if(!admin.getPassword().equals(pwd)){
			return "1";
		}
		return "0";
	}
	
	//判断密码是否正确    @param password
	public boolean judge( String password){
		User admin = (User) this.request.getSession().getAttribute("admin");
		String pwd = DigestUtils.md5Hex(password);
		if(!admin.getPassword().equals(pwd)){
			return false;
		}
		return true;
	}
	
	//重置设备测试次数
	@RequestMapping(value = "/resetTestSeveral")
	@ResponseBody
	public String resetTestSeveral(HttpServletRequest request, HttpServletResponse response,Model model){
		String code = request.getParameter("code");
		int num = equipmentService.updateEquiTestSeveral(code, 0);
		if(num==1) return "succeed";
		else return "error";
	}
	
	//设备详细信息   @param code
	@RequestMapping("/pctest")
	public String test( String code, Model model) {
		model.addAttribute("code", code);
		Equipment equipment = equipmentService.getEquipmentById(code);
		int neednum = 10;
		if ("05".equals(equipment.getHardversion())) {
			neednum = 16;
		} else if ("06".equals(equipment.getHardversion())) {
			neednum = 20;
		} else if ("02".equals(equipment.getHardversion())) {
			neednum = 2;
		}
//		List<AllPortStatus> allPortStatusList = allPortStatusService.findPortStatusListByEquipmentnum(code,neednum);
		List<Map<String, String>> allPortStatusList = DisposeUtil.addPortStatus(code, equipment.getHardversion());
		model.addAttribute("allPortStatusList", allPortStatusList);
		model.addAttribute("equipment", equipment);
		UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
		Integer merid = 0;
		if(userEquipment!=null) merid = CommUtil.toInteger(userEquipment.getUserId());
		Integer temp = CommUtil.toInteger(equipment.getTempid());
		if ( !temp.equals(null) && !temp.equals(0)) {
			TemplateParent templateParent = templateService.getParentTemplateOne(equipment.getTempid());
			if (templateParent != null) {
				if(CommUtil.toInteger(templateParent.getGrade()).equals(1)){
					List<TemplateParent> template = templateService.getTempDetails(templateParent);
					model.addAttribute("templist", template);
					model.addAttribute("temp", null);
				}else{
					model.addAttribute("temp", templateParent);
					model.addAttribute("templist", null);
				}
				model.addAttribute("merid", templateParent.getMerchantid());
			}else{
				List<TemplateParent> template = templateService.getAppointTempByType(equipment.getHardversion(), merid);
				model.addAttribute("temp", template.get(0));
			}
		}else{
			List<TemplateParent> template = templateService.getAppointTempByType(equipment.getHardversion(), merid);
			model.addAttribute("temp", template.get(0));
		}
		if (userEquipment == null) {
			model.addAttribute("username", "0");
			return "computer/test";
		}
		CodeSystemParam codeSystemParam = equipmentService.selectCodeSystemParamByCode(code);
		model.addAttribute("sysparam", codeSystemParam);
		User user = userService.selectUserById(CommUtil.toInteger(userEquipment.getUserId()));
		if (user != null) {
			model.addAttribute("username", user.getUsername());
			model.addAttribute("uid", user.getId());
		} else {
			model.addAttribute("username", "0");
			model.addAttribute("uid", 0);
		}
		return "computer/test";
	}
	
	//强制解绑     @param code 
	@RequestMapping({ "/disbinding" })
	public Object doRefund( String code, Model model) throws Exception {
		UserEquipment userequi = userEquipmentService.getUserEquipmentByCode(code);
		Equipment equipment = equipmentService.getEquipmentById(code);
		if (userequi != null) {
			User admin = (User) request.getSession().getAttribute("admin");
			Integer opeid = admin == null ? 0 : admin.getId();
			equipmentService.insertCodeoperatelog(code, 2, 2, userequi.getUserId(), opeid, userequi.getUserId() + "");
			userEquipmentService.deleteUserEquipmentByEquipmentCode(code);
			model.addAttribute("code", code);
			equipmentService.codeCollectmoney(code, userequi.getUserId(), equipment.getAid(), equipment.getHardversion());//强制解绑时统计计算该设备的今日收益
		}
		equipment.setBindtype((byte) 0);
		equipment.setSeveral(0);
		equipment.setAid(0);
		Integer temid = templateService.getDefaultPartemp( 0, equipment.getHardversion());
		equipment.setTempid(temid);
		equipment.setTotalOnlineEarn(0.0);
		equipment.setNowOnlineEarn(0.0);
		equipment.setTotalCoinsEarn(0);
		equipment.setNowCoinsEarn(0);
		equipmentService.updateEquipment(equipment);
		equipmentService.updateEquipmentRemark(null, code);
		return "redirect:/pcequipment/pctest?code=" + code;
	}
	
	/**
	 * @Description： 强制绑定时选择商户对象 	参数 code
	 * @author： origin          
	 */
	@RequestMapping(value = "/pcbindequ")
	public String bindequ( String code, Model model) {
		model.addAttribute("code", code);
		List<Map<String, Object>> userList = userService.getUsertoSortList("0,2");//查询管理员与商户
		model.addAttribute("userlist", userList);
		return "computer/constbind";
	}
	
	/**
	 * @Description： 强制将设备绑定到指定商户名下
	 * @author： origin          
	 * @param userId, code
	 * 创建时间：   2019年5月31日 下午6:29:57  更改
	 */
	@RequestMapping(value = "/pcuserbingequ")
	public String userbingequ(Integer userId,String code, Model model) {
		User user = (User) request.getSession().getAttribute("admin");
		Integer opeid = user == null ? 0 : user.getId();
		equipmentService.insertCodeoperatelog(code, 2, 1, userId, opeid, "0");
		UserEquipment userEquipment = new UserEquipment();
		userEquipment.setUserId(userId);
		userEquipment.setEquipmentCode(code);
		userEquipmentService.addUserEquipment(userEquipment);
		Equipment equipment = equipmentService.getEquipmentById(code);
		equipment.setBindtype((byte) 1);
		Integer temid = templateService.getDefaultPartemp( userId, equipment.getHardversion());
//		if ("03".equals(equipment.getHardversion())) {
//			List<TemplateParent> templateList = templateService.getParentTemplateListByMerchantid(0, 2);
//			equipment.setTempid(templateList.get(0).getId());
//		} else if ("04".equals(equipment.getHardversion())) {
//			List<TemplateParent> templateList = templateService.getParentTemplateListByMerchantid(0, 1);
//			equipment.setTempid(templateList.get(0).getId());
//		} else {
//			equipment.setTempid(0);
//		}
		equipment.setTempid(temid);
		equipmentService.updateEquipment(equipment);
		model.addAttribute("code", code);
		model.addAttribute("username", userService.selectUserById(userId).getUsername());
		return "redirect:/pcequipment/pctest?code=" + code;
	}
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	
	
	
	/**
	 * 跳转设备列表(查询设备列表)
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pcequlist")
	public String equlists(HttpServletRequest request, HttpServletResponse response,Model model){
		PageUtils<Parameters> pageBean = equipmentService.selectEquipmentParameter(request);
		List<Equipment> equipment = equipmentService.getEquipmentList();
		int online = 0;
		int binding = 0;
		for(Equipment eqm : equipment){
			if(eqm.getState()==1){//在线
				online += 1;
			}
			if(eqm.getBindtype()==1){//绑定
				binding += 1;
			}
		}
		model.addAttribute("online", online);
		model.addAttribute("disonline", equipment.size() - online);
		model.addAttribute("binding", binding);
		model.addAttribute("disbinding", equipment.size() - binding);
		model.addAttribute("totalRows", equipment.size());
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("state", request.getParameter("state"));
		model.addAttribute("code", request.getParameter("code"));
		model.addAttribute("imei", request.getParameter("imei"));
		model.addAttribute("ccid", request.getParameter("ccid"));
		model.addAttribute("hardversion", request.getParameter("hardversion"));
		model.addAttribute("softversionnum", request.getParameter("softversionnum"));
		model.addAttribute("csq", request.getParameter("csq"));
		model.addAttribute("phoneNum", request.getParameter("phoneNum"));
		model.addAttribute("line", request.getParameter("line"));
		model.addAttribute("equipmentList",pageBean.getListMap());
		return "computer/equlist";
	}
	
	
	/** ============================================================================================================= */
	/**
	 * @Description： 根据条件查询用户消费信息
	 * @param: orderId:订单id 	sort  1:正常充电   2:续充 
	 * @author： origin 
	 */
	@RequestMapping(value = "/powerBrokenLine")
	public String powerBrokenLine(Integer orderId, Model model) {
		Map<String, Object> result = new HashMap<>();
		try {
			orderId = CommUtil.toInteger(orderId);
			if(orderId==0){
				return "erroruser";
			}
			result = chargeRecordService.powerBrokenLine(CommUtil.toInteger(orderId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("orderId", result.get("orderId"));
		model.addAttribute("mapfunc", result.get("mapfunc"));
		model.addAttribute("mapinfo", result);
		model.addAttribute("realfisrt", result.get("realfisrt"));
		model.addAttribute("reallast", result.get("reallast"));
		model.addAttribute("realrecord", result.get("realrecord"));
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		return "comorder/realchargerecord";
	}
	
	/**
	 * @Description： 根据订单查询功率信息生成图像
	 * @author： origin 
	 */
	@RequestMapping(value = "/graphjson")
	@ResponseBody
	public Object graphjson(Integer orderId, Model model) {
		List<Realchargerecord> realrecord = new ArrayList<>();
		orderId = CommUtil.toInteger(orderId);
		if(orderId!=0){
			realrecord = equipmentService.realChargeRecordList(CommUtil.toInteger(orderId));
		}
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			if(realrecord.size()>0){
				ChargeRecord charge = chargeRecordService.chargeRecordOne(CommUtil.toInteger(orderId));
				Date begintime = charge.getBegintime();
				for(Realchargerecord pLog : realrecord){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("chargetime", pLog.getChargetime());
					map.put("surpluselec", pLog.getSurpluselec());
					map.put("power", pLog.getPower());
					String time = StringUtil.toDateTime("MM-dd HH:mm", pLog.getCreatetime());
					map.put("createtime", time);
					map.put("minuteTime", StringUtil.minuteTime(begintime, pLog.getCreatetime()));
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return list;
	}
	
//	public void editHardverAddPortStatus(int size,String code,int maxtemp,int needsize) {
//		if (size < needsize) {
//			for (int i = size + 1; i < maxtemp; i++) {
//				allPortStatusService.insertPortStatus(code, i);
//			}
//		}
//	}
	
	
}
