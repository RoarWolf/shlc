package com.hedong.hedongwx.web.controller.wechat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.StringUtil;

@Controller
@RequestMapping({ "/wctemplate" })
public class TemplateController {
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TemplateService templateService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping({ "/copyDirectTemp" })
	@ResponseBody
	public Object copyDirectTemp(Integer status, Integer dealid, String servePhone) {
		
		Map<String, Object> result = templateService.copyDirectTemp( status,  dealid, servePhone);
		return result;
	}
	
	/**
	 * @Description： 根据模板id获取对应的模板信息
	 * @author： origin   2019年10月12日 下午4:17:53
	 */
//	@RequestMapping({ "/genParallelismTemp" })
//	@ResponseBody
//	public Object genParallelismTemp(Integer tempid) {
//		Map<String, Object> result = (Map<String, Object>) templateService.genParallelismTemp ( tempid);
//		return result;
//	}
//	
	/**
	 * @Description： 根据条件查询使用的模板信息
	 * @author： origin   2019年10月12日 下午4:22:45
	 */
//	@RequestMapping({ "/genEmployTemp" })
//	@ResponseBody
//	public Object genEmployTemp(String devicenum, String val, Integer dealid, Integer status, Integer type) {
//		Map<String, Object> result = (Map<String, Object>) templateService.genEmployTemp( devicenum, val, dealid, type);
//		return result;
//	}
	
	/**
	 * @Description： 根据条件查询使用的服务电话
	 * @author： origin   2019年10月12日 下午4:22:45
	 */
	@RequestMapping({ "/genServicePhone" })
	@ResponseBody
	public Object genServicePhone(String devicenum, String val, Integer dealid, Integer status, Integer type) {
		Map<String, Object> result = (Map<String, Object>) templateService.genServicePhone( devicenum, val, dealid, status, type);
		return result;
	}
	
	
	/**
	 * 查询模板，进入选择模板类型页面
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/getTempmanage" })
	public String chargetempmanage(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && user.getId()!=null) {
			verifyTemplate(0);
			verifyTemplate(1);
			verifyTemplate(2);
			verifyTemplate(3);
			verifyTemplate(4);
			verifyTemplate(5);
			return "merchant/template";
		} else {
			return "erroruser";
		}
	}
	
	/**
	 * 判断个人模板是否存在，存在跳过，不存在添加
	 */
	public void verifyTemplate(Integer type) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			if(user!=null && user.getId() != null){
				if(type==0){//充电
					int fale = templateService.verifyTemplate(user.getId(), 0);
					if (fale==0) templateService.insertDufaultTemplate(user.getId());
				}else if(type==1){//离线模板
					int fale = templateService.verifyTemplate(user.getId(), 1);
					if (fale==0) templateService.insertOfflineTemp(user.getId());
				}else if(type==2){//脉冲（投币）
					int fale = templateService.verifyTemplate(user.getId(), 2);
					if (fale==0)  templateService.insertInCoinsTemp(user.getId());
				}else if(type==3){//钱包
					int fale = templateService.verifyTemplate(user.getId(), 3);
					if (fale==0)  templateService.insertWalletTemp(user.getId());
				}else if(type==4){//在线卡
					int fale = templateService.verifyTemplate(user.getId(), 4);
					if (fale==0)  templateService.insertOnlineTemp(user.getId());
				}else if(type==5){//在线卡
					int fale = templateService.verifyTemplate(user.getId(), 5);
					if (fale==0)  templateService.insertMonthTemp(user.getId());
				}
			}
		} catch (Exception e) {
			logger.info(CommUtil.getExceptInfo(e));
		}
	}
	
	/**
	 * @Description： 模板的选择
	 * @author： origin          
	 * @param souce、obj、temid
	 * @return
	 */
	@RequestMapping({ "/templatechoice" })
	@ResponseBody
	public int templateChoice( Integer source, String obj, Integer temid) {
		Integer choice = templateService.templatechoice(source, obj, temid);
		return choice;
	}
	
	/**
	 * @Description： 模板的默认选择
	 * @author： origin          
	 * 创建时间：   2019年5月28日 下午3:04:09
	 */
	@RequestMapping({ "/templatedefault" })
	@ResponseBody
	public Map<String, Object> templateDefault( Integer source, Integer temid) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		templateService.templateDefault(user.getId(), source, temid);
		codemap.put("code", 200);
		return codemap;
	}
	//--------------------------------------------------------------------
	/**
	  * @Description： 添加(分等级)模板
	  * @author： origin   
	  */
	@RequestMapping({ "/insertstairtempval" })
	@ResponseBody
	public Map<String, Object> insertStairTempVal(String name, Integer merid, String remark, String common1, 
			Integer status, Integer common2, Integer permit, Integer walletpay, Integer grade, Integer ifalipay,String chargeInfo) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			if(merid==null && user!=null){
				merid = CommUtil.toInteger(user.getId());
			}
			Map<String, Object>  result = templateService.insertStairTempVal(name, merid, remark, common1, status, permit, common2, 
					walletpay, grade, ifalipay,chargeInfo);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	  * @Description： 删除一级模板
	  * @author： origin   
	  */
	@RequestMapping({ "/deletestairtemp" })
	@ResponseBody
	public Map<String, Object> deleteStairTemp(Integer parid){
		Map<String, Object>  result = templateService.deleteStairTemp( parid);
		return result;
	}

	/**
	  * @Description： 删除(强制)一级模板
	  * @author： origin   
	  */
	@RequestMapping({ "/deleteforcestairtemp" })
	@ResponseBody
	public Map<String, Object> deleteForceStairTemp(Integer parid){
		Map<String, Object>  result = templateService.deleteforcestairtemp( parid);
		return result;
	}
	
	/**
	  * @Description： 修改一级模板
	  * @author： origin   
	  */
	@RequestMapping({ "/updatestairtemp" })
	@ResponseBody
	public Map<String, Object> updateStairTemp(Integer id, String name, String remark, String common1, 
			Integer status, Integer common2, Integer permit, Integer walletpay, Integer grade){
		Map<String, Object>  result = templateService.updateStairTemp( id, name, remark, common1, permit, status, 
				common2,  walletpay,  grade);
			return result;
	}
	
	/**
	 * @Description： 删除模板
	 * @author： origin 创建时间：   2019年8月3日 下午5:18:46
	 */
	@RequestMapping({ "/selectstairtemp" })
	@ResponseBody
	public TemplateParent selectStairTemp( Integer id) {
		TemplateParent template = templateService.getParentTemplateOne(id);
		return template;
	}
	
	/**
	 * @Description： 查询模板信息
	 * @author： origin 创建时间：   2019年8月3日 下午5:19:02
	 */
	@RequestMapping({ "/selectstairtempval" })
	public String selectStairTempVal( Integer status, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		List<TemplateParent> templatelist = templateService.selectStairTempVal(user, status);
		List<TemplateParent> tempgather = new ArrayList<TemplateParent>();
//		Integer index = 0;
		for(TemplateParent str : templatelist){
//			index = index +1;
			int temp = 0;
			List<TemplateSon> sonTemplateLists = templateService.getSonTemp(str.getId());
			for (TemplateSon templateSon : sonTemplateLists) {
				if (templateSon.getStatus() != null && templateSon.getStatus() == 5) {
					temp = 1;
				}
			}
			str.setHasbagMonth(temp);
			if("1".equals(str.getCommon3())) str.setPitchon(1);
			str.setGather(sonTemplateLists);
			if(str.getGrade()==1 && str.getRank() ==1){
				tempgather = templateService.getTempDetails(str);
			}
        }
//		templatelist.remove(index-1);
		model.addAttribute("user", user);
		model.addAttribute("templatelist", templatelist);
		model.addAttribute("tempgather", tempgather);
		model.addAttribute("arecode", 0);
		model.addAttribute("source", 0);
		return "merchant/templatecharge";
	}
	
	/***origin***  充电模板 ============================================================================================= */
	
	/**
	 * @Description： 根据模板id查询主模板信息(一级模板)
	 * @author： origin 
	 */
	@RequestMapping({ "/getstairtemplate" })
	@ResponseBody
	public TemplateParent getStairTemplate( Integer id) {
		TemplateParent template = templateService.getParentTemplateOne(id);
		return template;
	}
	
	/**
	 * @Description： 查询获取一级充电模板且包含二级子模板
	 * @author： origin   
	 */
	@RequestMapping({ "/getstaircharge" })
	public String getStairCharge( Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantid(user.getId(), 0);
			model.addAttribute("user", user);
			model.addAttribute("templatelist", templatelist);
			model.addAttribute("arecode", 0);
			model.addAttribute("source", 0);
			return "merchant/templatecharge";
		} else {
			return "erroruser";
		}
	}
	
	 /**
	  * @Description： 添加一级充电模板
	  * @author： origin   
	  */
	@RequestMapping({ "/addstaircharge" })
	@ResponseBody
	public Map<String, Object> addStairCharge(String name, String remark, String common1, Integer common2, 
			Integer permit, Integer walletpay) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent tem = templateService.templateByName(user.getId(), 0, name);
		if(null == tem){
			templateService.addStairCharge(user.getId(), name, remark, common1, common2, permit, walletpay,2);
			TemplateParent temp = templateService.templateByName(user.getId(), 0, name);
			codemap.put("code", 200);
			codemap.put("temid", temp.getId());
		} else {
			codemap.put("code", 101);
		}
		return codemap;
	}
	
	 /**
	  * @Description： 修改一级充电模板
	  * @author： origin   
	  */
	@RequestMapping({ "/updatestaircharge" })
	@ResponseBody
	public Map<String, Object> updateStairCharge(Integer id, String name, String remark, String common1, 
			Integer common2, Integer permit, Integer ifalipay, Integer walletpay,String chargeInfo) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent oritem = templateService.getParentTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateParent tem = templateService.templateByName(user.getId(), 0, name);
			if(null != tem){
				codemap.put("code", 101);
				return codemap;
			}
		}
		TemplateParent template = new TemplateParent();
		template.setId(id);
		template.setName(name);
		template.setRemark(remark);
		template.setCommon1(common1);
		template.setCommon2(common2);
		template.setPermit(permit);
		template.setIfalipay(ifalipay);
		template.setWalletpay(walletpay);
		//接收充电说明
		template.setChargeInfo(chargeInfo);
		this.templateService.updateParentTemplate(template);
		codemap.put("code", 200);
		codemap.put("temid", id);
		return codemap;
	}
	
	/**
	  * @Description： 删除一级充电模板并同步删除该模板下的下级模板
	  * @author： origin   
	  */
	@RequestMapping({ "/deletestaircharge" })
	@ResponseBody
	public String deleteStairCharge(Integer id) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			this.templateService.deleteParentTemplate(id);
			return "1";
		} else {
			return "erroruser";
		}
	}
	
	/**  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  */

	/**
	 * @Description： 查询充电模板的子类模板
	 * @author： origin
	 */
	@RequestMapping({ "/getsubclasscharge" })
	@ResponseBody
	public TemplateSon getSubclassCharge(Integer id) {
		TemplateSon templateson = templateService.getInfoTemplateOne(id);
		return templateson;
	}
	
	/** 
	 * @Description： 添加充电模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/addsubclasscharge" })
	@ResponseBody
	public Map<String, Object> addSubclassCharge(Integer id, String name, Double money, Integer chargeTime, Double chargeQuantity) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		if (money > 30000 || chargeQuantity > 300 || chargeTime > 30000) {
			codemap.put("code", 102);
			return codemap;
		}
		TemplateSon tem = templateService.subtemplatefind(id, name);
		if (tem != null) {
			codemap.put("code", 101);
			return codemap;
		}
		TemplateSon subclass = new TemplateSon();
		subclass.setTempparid(id);
		subclass.setName(name);
		subclass.setStatus(Integer.valueOf(0));
		subclass.setMoney(Double.valueOf(money));
		subclass.setChargeTime(chargeTime);
		chargeQuantity = chargeQuantity * 100;
		subclass.setChargeQuantity(chargeQuantity.intValue());
		this.templateService.insertSonTemplate(subclass);
		TemplateSon temson = templateService.subtemplatefind(id, name);
		codemap.put("code", 200);
		codemap.put("ctemid", temson.getId());
		return codemap;
	}
	
	
	/**
	 * @Description： 修改充电模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/updatesubclasscharge" })
	@ResponseBody
	public Map<String, Object> updateSubclassCharge(Integer id, String name, Double money, String chargeTime, String chargeQuantity) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}

		TemplateSon oritem = templateService.getInfoTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateSon tem = templateService.subtemplatefind(id, name);
			if (tem != null) {
				codemap.put("code", 101);
				return codemap;
			}
		}
		double quantity = CommUtil.toDouble(chargeQuantity)*100;
		Integer quantityvalue = CommUtil.toInteger(quantity);
		int mun = templateService.updateSubclassCharge(id, name, money, chargeTime, quantityvalue);
		//TemplateSon temson = templateService.subtemplatefind(user.getId(), name);
		codemap.put("code", 200);
		codemap.put("ctemid", id);
		return codemap;
	}
	
		
	/**
	 * @Description： 删除充电模板的子类模板
	 * @author： origin          
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/deletesubclasscharge" })
	@ResponseBody
	public String deleteSubclassCharge(Integer id) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			this.templateService.deleteSonTempmanage(id);
			return "1";
		} else {
			return "erroruser";
		}
	}
	
	/***origin***  离线模板 ============================================================================================= */
	
	/**
	 * @Description： 查询获取一级离线模板且包含二级子模板
	 * @author： origin   
	 */
	@RequestMapping({ "/getstairoffline" })
	public String getStairOffline( Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantid(user.getId(), 1);
			model.addAttribute("user", user);
			model.addAttribute("templatelist", templatelist);
			model.addAttribute("arecode", 0);
			model.addAttribute("source", 0);
			return "merchant/templateoffline";
		} else {
			return "erroruser";
		}
	}
	
	 /**
	  * @Description： 添加一级离线模板
	  * @author： origin   
	  */
	@RequestMapping({ "/addstairoffline" })
	@ResponseBody
	public Map<String, Object> addStairOffline(String name, String remark, String common1) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent partem = templateService.templateByName(user.getId(), 1, name);
		if(partem !=null){
			codemap.put("code", 101);
			return codemap;
		}
		templateService.addStairOffline(user.getId(), name, remark, common1);
		TemplateParent partemp = templateService.templateByName(user.getId(), 1, name);
		codemap.put("code", 200);
		codemap.put("temid", partemp.getId());
		return codemap;
	}
	
	 /**
	  * @Description： 修改一级离线模板
	  * @author： origin   
	  */
	@RequestMapping({ "/updatestairoffline" })
	@ResponseBody
	public Map<String, Object> updateStairOffline(Integer id, String name, String remark, String common1) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent oritem = templateService.getParentTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateParent partem = templateService.templateByName(id, 1, name);
			if(partem !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		TemplateParent template = new TemplateParent();
		template.setName(name);
		template.setId(id);
		template.setRemark(remark);
		template.setCommon1(common1);
		this.templateService.updateParentTemplate(template);
		codemap.put("code", 200);
		codemap.put("temid", id);
		return codemap;
	}
	
	/**
	  * @Description： 删除一级离线模板并同步删除该模板下的下级模板
	  * @author： origin   
	  */
	@RequestMapping({ "/deletestairoffline" })
	@ResponseBody
	public String updateStairOffline(Integer id) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			this.templateService.deleteParentTemplate(id);
			return "1";
		} else {
			return "erroruser";
		}
	}
	
	/**  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  */

	/**
	 * @Description： 查询离线模板的子类模板
	 * @author： origin
	 */
	@RequestMapping({ "/getsubclassoffline" })
	@ResponseBody
	public TemplateSon getSubclassOffline(Integer id) {
		TemplateSon templateson = templateService.getInfoTemplateOne(id);
		return templateson;
	}
	
	/** 
	 * @Description： 添加离线模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/addsubclassoffline" })
	@ResponseBody
	public Map<String, Object> addSubclassOffline(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon sontem = templateService.subtemplatefind(id, name);
		if (sontem != null) {
			codemap.put("code", 101);
			return codemap;
		}
		TemplateSon subclass = new TemplateSon();
		subclass.setTempparid(id);
		subclass.setName(name);
		//subclass.setStatus(1);
		subclass.setMoney(money);
		subclass.setRemark(remark);
		this.templateService.insertSonTemplate(subclass);
		TemplateSon ctem = templateService.subtemplatefind(id, name);
		codemap.put("code", 200);
		codemap.put("ctemid", ctem.getId());
		return codemap;
	}
	
	/**
	 * @Description： 修改离线模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/updatesubclassoffline" })
	@ResponseBody
	public Map<String, Object> updateSubclassOffline(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon oritem = templateService.getInfoTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateSon temson = templateService.subtemplatefind(id, name);
			if(temson !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		templateService.updateSubclassOffline(id, name, money, remark);
		codemap.put("code", 200);
		codemap.put("ctemid", id);
		return codemap;
	}
		
	/**
	 * @Description： 删除离线模板的子类模板
	 * @author： origin          
	 */
	@RequestMapping({ "/deletesubclassoffline" })
	@ResponseBody
	public String deleteSubclassOffline(Integer id) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			this.templateService.deleteSonTempmanage(id);
			return "1";
		} else {
			return "erroruser";
		}
	}
	
	/***origin***  脉冲（投币）模板 ============================================================================================= */
	
	/**
	 * @Description： 查询获取一级脉冲（投币）模板且包含二级子模板
	 * @author： origin   
	 */
	@RequestMapping({ "/getstairincoins" })
	public String getStairIncoins( Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantid(user.getId(), 2);
			model.addAttribute("user", user);
			model.addAttribute("templatelist", templatelist);
			model.addAttribute("arecode", 0);
			model.addAttribute("source", 0);
			return "merchant/templateincoins";
		} else {
			return "erroruser";
		}
	}
	
	 /**
	  * @Description： 添加一级脉冲（投币）模板
	  * @author： origin   
	  */
	@RequestMapping({ "/addstairincoins" })
	@ResponseBody
	public Map<String, Object> addStairIncoins(String name, String remark, String common1, Integer permit, Integer walletpay) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent partem = templateService.templateByName(user.getId(), 2, name);
		if(partem !=null){
			codemap.put("code", 101);
			return codemap;
		}
		templateService.addStairIncoins(user.getId(), name, remark, common1, permit, walletpay);
		TemplateParent partemp = templateService.templateByName(user.getId(), 2, name);
		codemap.put("code", 200);
		codemap.put("temid", partemp.getId());
		return codemap;	
	}
	
	 /**
	  * @Description： 修改一级脉冲（投币）模板
	  * @author： origin   
	  */
	@RequestMapping({ "/updatestairincoins" })
	@ResponseBody
	public Map<String, Object> updateStairIncoins(Integer id, String name, String remark, String common1, Integer permit, Integer walletpay) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent oritem = templateService.getParentTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateParent partem = templateService.templateByName(user.getId(), 2, name);
			if(partem !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		TemplateParent template = new TemplateParent();
		template.setId(id);
		template.setName(name);
		template.setRemark(remark);
		template.setCommon1(common1);
		template.setPermit(permit);
		template.setWalletpay(walletpay);
		this.templateService.updateParentTemplate(template);
		codemap.put("code", 200);
		codemap.put("temid", id);
		return codemap;
	}
/**
  * @Description： 删除一级脉冲（投币）模板并同步删除该模板下的下级模板
  * @author： origin   
  */
	@RequestMapping({ "/deletestairincoins" })
	@ResponseBody
	public String updateStairIncoins(Integer id) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			this.templateService.deleteParentTemplate(id);
			return "1";
		} else {
			return "erroruser";
		}
	}
	
	/**  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  */

	/**
	 * @Description： 查询脉冲（投币）模板的子类模板
	 * @author： origin
	 */
	@RequestMapping({ "/getsubclassincoins" })
	@ResponseBody
	public TemplateSon getSubclassIncoins(Integer id) {
		TemplateSon templateson = templateService.getInfoTemplateOne(id);
		return templateson;
	}
	
	/** 
	 * @Description： 添加脉冲（投币）模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/addsubclassincoins" })
	@ResponseBody
	public Map<String, Object> addSubclassIncoins(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon temson = templateService.subtemplatefind(id, name);
		if(temson !=null){
			codemap.put("code", 101);
			return codemap;
		}
		TemplateSon subclass = new TemplateSon();
		subclass.setTempparid(id);
		subclass.setName(name);
		//subclass.setStatus(Integer.valueOf(0));
		subclass.setMoney(money);
		subclass.setRemark(remark);
		this.templateService.insertSonTemplate(subclass);
		TemplateSon ctem = templateService.subtemplatefind(id, name);
		codemap.put("code", 200);
		codemap.put("ctemid", ctem.getId());
		return codemap;
	}
	
	
	/**
	 * @Description： 修改脉冲（投币）模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/updatesubclassincoins" })
	@ResponseBody
	public Map<String, Object> updateSubclassIncoins(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon oritem = templateService.getInfoTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateSon temson = templateService.subtemplatefind(id, name);
			if(temson !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		int mun = templateService.updateSubclassIncoins(id, name, CommUtil.decimals(0, money), CommUtil.decimals(0, remark));
		codemap.put("code", 200);
		codemap.put("ctemid", id);
		return codemap;
	}
	
		
	/**
	 * @Description： 删除脉冲（投币）模板的子类模板
	 * @author： origin          
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/deletesubclassincoins" })
	@ResponseBody
	public String deleteSubclassIncoins(Integer id) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			this.templateService.deleteSonTempmanage(id);
			return "1";
		} else {
			return "erroruser";
		}
	}
	
	/***origin***  钱包模板 ============================================================================================= */
	
	/**
	 * @Description： 查询获取一级钱包模板且包含二级子模板
	 * @author： origin   
	 */
	@RequestMapping({ "/getstairwallet" })
	public String getStairWallet( Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantid(user.getId(), 3);
			model.addAttribute("user", user);
			model.addAttribute("templatelist", templatelist);
			model.addAttribute("arecode", 0);
			model.addAttribute("source", 0);
			return "merchant/templatewallet";
		} else {
			return "erroruser";
		}
	}
	
	 /**
	  * @Description： 添加一级钱包模板
	  * @author： origin   , Integer permit, Integer walletpay
	  */
	@RequestMapping({ "/addstairwallet" })
	@ResponseBody
	public Map<String, Object> addStairWallet(Integer type, Integer merid, String name, String remark, String common1) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		if(!CommUtil.toInteger(type).equals(1)){
			User user = (User) request.getSession().getAttribute("user");
			if (null == user) {
				codemap.put("code", 100);
				return codemap;
			}
			merid = CommUtil.toInteger(user.getId());
		}
		TemplateParent partem = templateService.templateByName(merid, 3, name);
		if(partem !=null){
			codemap.put("code", 101);
			return codemap;
		}
		templateService.addStairWallet(merid, name, remark, common1);
		TemplateParent partemp = templateService.templateByName(merid, 3, name);
		codemap.put("code", 200);
		codemap.put("temid", partemp.getId());
		return codemap;
	}
	
	 /**
	  * @Description： 修改一级钱包模板
	  * @author： origin   , Integer permit, Integer walletpay
	  */
	@RequestMapping({ "/updatestairwallet" })
	@ResponseBody
	public Map<String, Object> updateStairWallet(Integer id, String name, String remark, String common1) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent oritem = templateService.getParentTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateParent partem = templateService.templateByName(user.getId(), 3, name);
			if(partem !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		TemplateParent template = new TemplateParent();
		template.setId(id);
		template.setName(name);
		template.setRemark(remark);
		template.setCommon1(common1);
//			template.setPermit(permit);
//			template.setWalletpay(walletpay);
		this.templateService.updateParentTemplate(template);
		codemap.put("code", 200);
		codemap.put("temid", id);
		return codemap;
	}
	
	
	/**
	  * @Description： 删除一级钱包模板并同步删除该模板下的下级模板
	  * @author： origin   
	  */
	@RequestMapping({ "/deletestairwallet" })
	@ResponseBody
	public Map<String, Object> updateStairWallet(Integer id) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) return CommUtil.responseBuildInfo(201, "商户信息未获取到", datamap);
			templateService.deleteParentTemplate(id);
			return CommUtil.responseBuildInfo(200, "删除成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  */

	/**
	 * @Description： 查询钱包模板的子类模板
	 * @author： origin
	 */
	@RequestMapping({ "/getsubclasswwallet" })
	@ResponseBody
	public TemplateSon getSubclassWwallet(Integer id) {
		TemplateSon templateson = templateService.getInfoTemplateOne(id);
		return templateson;
	}
	
	/** 
	 * @Description： 添加钱包模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/addsubclasswwallet" })
	@ResponseBody
	public Map<String, Object> addSubclassWwallet(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon temson = templateService.subtemplatefind(id, name);
		if(temson !=null){
			codemap.put("code", 101);
			return codemap;
		}
		TemplateSon subclass = new TemplateSon();
		subclass.setTempparid(id);
		subclass.setName(name);
		//subclass.setStatus(Integer.valueOf(0));
		subclass.setMoney(money);
		subclass.setRemark(remark);
		this.templateService.insertSonTemplate(subclass);
		TemplateSon ctem = templateService.subtemplatefind(id, name);
		codemap.put("code", 200);
		codemap.put("ctemid", ctem.getId());
		return codemap;
	}
	
	/**
	 * @Description： 修改钱包模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/updatesubclasswwallet" })
	@ResponseBody
	public Map<String, Object> updateSubclassWwallet(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon oritem = templateService.getInfoTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateSon temson = templateService.subtemplatefind(id, name);
			if(temson !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		int mun = templateService.updateSubclassWwallet(id, name, money, remark);
		codemap.put("code", 200);
		codemap.put("ctemid", id);
		return codemap;
	}
		
	/**
	 * @Description： 删除钱包模板的子类模板
	 * @author： origin
	 */
	@RequestMapping({ "/deletesubclasswwallet" })
	@ResponseBody
	public Map<String, Object> deleteSubclassWwallet(Integer id) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) return CommUtil.responseBuildInfo(201, "商户信息未获取到", datamap);
			templateService.deleteSonTempmanage(id);
			return CommUtil.responseBuildInfo(200, "删除成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/***origin***  在线卡模板 ============================================================================================= */
	
	/**
	 * @Description： 查询获取一级在线卡模板且包含二级子模板
	 * @author： origin   
	 */
	@RequestMapping({ "/getstaironline" })
	public String getStairOnline( Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantid(user.getId(), 4);
			model.addAttribute("user", user);
			model.addAttribute("templatelist", templatelist);
			model.addAttribute("arecode", 0);
			model.addAttribute("source", 0);
			return "merchant/templateonline";
		} else {
			return "erroruser";
		}
	}
	
	 /**
	  * @Description： 添加一级在线卡模板
	  * @author： origin   
	  */
	@RequestMapping({ "/addstaironline" })
	@ResponseBody
	public Map<String, Object> addStairOnline( Integer type, Integer merid, String name, String remark, String common1) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		if(!CommUtil.toInteger(type).equals(1)){
			User user = (User) request.getSession().getAttribute("user");
			if (null == user) {
				codemap.put("code", 100);
				return codemap;
			}
			merid = CommUtil.toInteger(user.getId());
		}
		TemplateParent partem = templateService.templateByName(merid, 4, name);
		if(partem !=null){
			codemap.put("code", 101);
			return codemap;
		}
		templateService.addStairOnline(merid, name, remark, common1);
		TemplateParent partemp = templateService.templateByName(merid, 4, name);
		codemap.put("code", 200);
		codemap.put("temid", partemp.getId());
		return codemap;	
	}
	
	 /**
	  * @Description： 修改一级在线卡模板
	  * @author： origin   
	  */
	@RequestMapping({ "/updatestaironline" })
	@ResponseBody
	public Map<String, Object> updateStairOnline(Integer id, String name, String remark, String common1) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent oritem = templateService.getParentTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateParent partem = templateService.templateByName(user.getId(), 4, name);
			if(partem !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		TemplateParent template = new TemplateParent();
		template.setId(id);
		template.setName(name);
		template.setRemark(remark);
		template.setCommon1(common1);
		this.templateService.updateParentTemplate(template);
		codemap.put("code", 200);
		codemap.put("temid", id);
		return codemap;
	}
	
	/**
	  * @Description： 删除一级在线卡模板并同步删除该模板下的下级模板
	  * @author： origin   
	  */
	@RequestMapping({ "/deletestaironline" })
	@ResponseBody
	public Map<String, Object> updateStairOnine(Integer id) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) return CommUtil.responseBuildInfo(201, "商户信息未获取到", datamap);
			templateService.deleteParentTemplate(id);
			return CommUtil.responseBuildInfo(200, "删除成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  */

	/**
	 * @Description： 查询在线卡模板的子类模板
	 * @author： origin
	 */
	@RequestMapping({ "/getsubclassonline" })
	@ResponseBody
	public TemplateSon getSubclassOnline(Integer id) {
		TemplateSon templateson = templateService.getInfoTemplateOne(id);
		return templateson;
	}
	
	/** 
	 * @Description： 添加在线卡模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/addsubclassonline" })
	@ResponseBody
	public Map<String, Object> addSubclassOnline(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon temson = templateService.subtemplatefind(id, name);
		if(temson !=null){
			codemap.put("code", 101);
			return codemap;
		}
		TemplateSon subclass = new TemplateSon();
		subclass.setTempparid(id);
		subclass.setName(name);
		//subclass.setStatus(Integer.valueOf(0));
		subclass.setMoney(money);
		subclass.setRemark(remark);
		this.templateService.insertSonTemplate(subclass);
		TemplateSon ctem = templateService.subtemplatefind(id, name);
		codemap.put("code", 200);
		codemap.put("ctemid", ctem.getId());
		return codemap;
	}
	
	/**
	 * @Description： 修改在线卡模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/updatesubclassonline" })
	@ResponseBody
	public Map<String, Object> updateSubclassOnline(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon oritem = templateService.getInfoTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateSon temson = templateService.subtemplatefind(id, name);
			if(temson !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		int mun = templateService.updateSubclassOnline(id, name, money, remark);
		codemap.put("code", 200);
		codemap.put("ctemid", id);
		return codemap;
	}
		
	/**
	 * @Description： 删除在线卡模板的子类模板
	 * @author： origin          
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/deletesubclassonline" })
	@ResponseBody
	public Map<String, Object> deleteSubclassOnline(Integer id) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) return CommUtil.responseBuildInfo(201, "商户信息未获取到", datamap);
			templateService.deleteSonTempmanage(id);
			return CommUtil.responseBuildInfo(200, "删除成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
/***origin***  包月模板 ============================================================================================= */
	
	/**
	 * @Description： 查询获取一级包月模板且包含二级子模板
	 * @author： wolf   
	 */
	@RequestMapping({ "/getPackageMonth" })
	public String getPackageMonth(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantid(user.getId(), 5);
			if (templatelist == null || templatelist.size() == 0) {
				model.addAttribute("templatelist", null);
			} else {
				model.addAttribute("templatelist", templatelist);
			}
			model.addAttribute("user", user);
			model.addAttribute("arecode", 0);
			model.addAttribute("source", 0);
			return "merchant/templateMonth";
		} else {
			return "erroruser";
		}
	}
	
	/**
	  * @Description： 添加一级包月模板
	  * @author： wolf   
	  */
	@RequestMapping({ "/addPackageMonth" })
	@ResponseBody
	public Map<String, Object> addPackageMonth(String name, String remark, String common1,Integer common2,String common3,Integer ifmonth) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		System.out.println("remark===" + remark);
		System.out.println("common1===" + common1);
		System.out.println("common2===" + common2);
		System.out.println("common3===" + common3);
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent partem = templateService.templateByName(user.getId(), 5, name);
		if(partem !=null){
			codemap.put("code", 101);
			return codemap;
		}
		templateService.addPackageMonth(user.getId(), name, remark, common1, common2, common3, ifmonth);
		TemplateParent partemp = templateService.templateByName(user.getId(), 5, name);
		codemap.put("code", 200);
		codemap.put("temid", partemp.getId());
		return codemap;	
	}
	
	 /**
	  * @Description： 修改一级包月模板
	  * @author： wolf   
	  */
	@RequestMapping({ "/updatePackageMonth" })
	@ResponseBody
	public Map<String, Object> updatePackageMonth(Integer id, String name, String remark, String common1,Integer common2,String common3,Integer ifmonth) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent oritem = templateService.getParentTemplateOne(id);
		System.out.println(oritem);
		if(!name.equals(oritem.getName())){
			TemplateParent partem = templateService.templateByName(user.getId(), 5, name);
			if(partem !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		TemplateParent template = new TemplateParent();
		template.setId(id);
		template.setName(name);
		template.setRemark(remark);
		template.setCommon1(common1);
		template.setCommon2(common2);
		template.setCommon3(common3);
		template.setIfmonth(ifmonth);
		this.templateService.updateParentTemplate(template);
		codemap.put("code", 200);
		codemap.put("temid", id);
		return codemap;
	}
	
	/**
	  * @Description： 删除一级包月模板并同步删除该模板下的下级模板
	  * @author： wolf   
	  */
	@RequestMapping({ "/deletePackageMonth" })
	@ResponseBody
	public String deletePackageMonth(Integer id) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			this.templateService.deleteParentTemplate(id);
			return "1";
		} else {
			return "erroruser";
		}
	}
	
	/** 
	 * @Description： 添加包月模板的子类模板
	 * @author： wolf 
	 */
	@RequestMapping({ "/addSubclassPackageMonth" })
	@ResponseBody
	public Map<String, Object> addSubclassPackageMonth(Integer id, String name, Double money, String common1) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon temson = templateService.subtemplatefind(id, name);
		if(temson !=null){
			codemap.put("code", 101);
			return codemap;
		}
		TemplateSon subclass = new TemplateSon();
		subclass.setTempparid(id);
		subclass.setName(name);
		subclass.setMoney(money);
		subclass.setCommon1(common1);
		this.templateService.insertSonTemplate(subclass);
		TemplateSon ctem = templateService.subtemplatefind(id, name);
		codemap.put("code", 200);
		codemap.put("ctemid", ctem.getId());
		return codemap;
	}
	
	/**
	 * @Description： 修改包月模板的子类模板
	 * @author： wolf 
	 */
	@RequestMapping({ "/updatesubclassPackageMonth" })
	@ResponseBody
	public Map<String, Object> updatesubclassPackageMonth(Integer id, String name, Double money, String common1) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon oritem = templateService.getInfoTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateSon temson = templateService.subtemplatefind(id, name);
			if(temson !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		int mun = templateService.updateSubclassPackageMonth(id, name, money, common1);
		codemap.put("code", 200);
		codemap.put("ctemid", id);
		return codemap;
	}
		
	/**
	 * @Description： 删除包月模板的子类模板
	 * @author： wolf          
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/deletesubclassPackageMonth" })
	@ResponseBody
	public String deletesubclassPackageMonth(Integer id) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			templateService.deleteSonTempmanage(id);
			return "1";
		} else {
			return "erroruser";
		}
	}
	//
	/** 
	 * @Description： 添加充电模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/addSubclassMonthCharge" })
	@ResponseBody
	public Map<String, Object> addSubclassMonthCharge(Integer id, String name, Double money, Integer chargeTime, Double chargeQuantity) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		if (money > 30000 || chargeQuantity > 300 || chargeTime > 30000) {
			codemap.put("code", 102);
			return codemap;
		}
		TemplateSon tem = templateService.subtemplatefind(id, name);
		if (tem != null) {
			codemap.put("code", 101);
			return codemap;
		}
		TemplateSon subclass = new TemplateSon();
		subclass.setTempparid(id);
		subclass.setName(name);
		subclass.setStatus(Integer.valueOf(0));
		subclass.setMoney(Double.valueOf(money));
		subclass.setChargeTime(chargeTime);
		chargeQuantity = chargeQuantity * 100;
		subclass.setChargeQuantity(chargeQuantity.intValue());
		subclass.setStatus(5);
		this.templateService.insertSonTemplate(subclass);
		TemplateSon temson = templateService.subtemplatefind(id, name);
		codemap.put("code", 200);
		codemap.put("ctemid", temson.getId());
		return codemap;
	}

//===@origin PC端模板处理===============================================================================================================
	
	/**
	 * @Description： 模板的默认选择
	 * @author： origin          
	 * 创建时间：   2019年5月28日 下午3:04:09
	 */
	@RequestMapping({ "/allowtemplatedefault" })
	@ResponseBody
	public Map<String, Object> templateDefault( Integer source, Integer merid, Integer temid) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		Integer choice = templateService.templateDefault(merid, source, temid);
		codemap.put("code", 200);
		return codemap;
	}
	//--------------------------------------------------------------------
	/**
	 * @Description：  删除一级模板并同步删除该模板下的下级模板
	 * @author： origin          
	 * 创建时间：   2019年5月29日 下午2:36:41
	 */
	@RequestMapping({ "/allowdeletestair" })
	@ResponseBody
	public Map<String, Object> allowdeleteStair(Integer id) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		this.templateService.deleteParentTemplate(id);
		codemap.put("code", 200);
		return codemap;
	}
	
	/**
	 * @Description： 查询模板的子类模板
	 * @author： origin
	 */
	@RequestMapping({ "/allowgetsubclass" })
	@ResponseBody
	public TemplateSon allowgetSubclass(Integer id) {
		TemplateSon templateson = templateService.getInfoTemplateOne(id);
		return templateson;
	}
	/**
	 * @Description： 删除模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/allowdeletesubclass" })
	@ResponseBody
	public Map<String, Object> allowdeleteSubclassCharge(Integer id) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		this.templateService.deleteSonTempmanage(id);
		codemap.put("code", 200);
		return codemap;
	}
	
	/***origin***  充电模板 ============================================================================================= */
	
	/**
	 * @Description： 根据模板id查询主模板信息(一级模板)
	 * @author： origin 
	 */
	@RequestMapping({ "/allowstairtemplate" })
	@ResponseBody
	public TemplateParent allowStairTemplate( Integer id) {
		TemplateParent template = templateService.getParentTemplateOne(id);
		return template;
	}
	
	/**
	 * @Description： 查询获取一级充电模板且包含二级子模板
	 * @author： origin   
	 */
	@RequestMapping({ "/allowgetstaircharge" })
	public String allowgetStairCharge( Model model) {
		User user = (User) request.getSession().getAttribute("admin");
		if (user != null) {
			List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantid(user.getId(), 0);
			model.addAttribute("user", user);
			model.addAttribute("templatelist", templatelist);
			model.addAttribute("arecode", 0);
			model.addAttribute("source", 0);
			return "merchant/templatecharge";
		} else {
			return "erroruser";
		}
	}
	
	 /**
	  * @Description： 添加一级充电模板
	  * @author： origin   
	  */
	@RequestMapping({ "/allowaddstaircharge" })
	@ResponseBody
	public Map<String, Object> getaddStairCharge(Integer merid, String name, String remark, String common1, Integer common2, Integer permit, Integer walletpay,Integer ifmonth) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent tem = templateService.templateByName(merid, 0, name);
		if(null == tem){
			templateService.addStairCharge(merid, name, remark, common1, common2, permit, walletpay,ifmonth);
			TemplateParent temp = templateService.templateByName(merid, 0, name);
			codemap.put("code", 200);
			codemap.put("temid", temp.getId());
		} else {
			codemap.put("code", 101);
		}
		return codemap;
	}
	
	 /**
	  * @Description： 修改一级充电模板
	  * @author： origin   
	  */
	@RequestMapping({ "/allowupdatestaircharge" })
	@ResponseBody
	public Map<String, Object> allowupdateStairCharge(Integer id, String name, String remark, String common1, Integer common2, Integer permit, Integer walletpay,Integer ifmonth) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent oritem = templateService.getParentTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateParent tem = templateService.templateByName(oritem.getMerchantid(), 0, name);
			if(null != tem){
				codemap.put("code", 101);
				return codemap;
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
		this.templateService.updateParentTemplate(template);
		codemap.put("code", 200);
		codemap.put("temid", id);
		return codemap;
	}
	
	/**  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  */


	
	/** 
	 * @Description： 添加充电模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/allowaddsubclasscharge" })
	@ResponseBody
	public Map<String, Object> allowaddSubclassCharge(Integer id, String name, Double money, Integer chargeTime, Double chargeQuantity) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		if (money > 30000 || chargeQuantity > 300 || chargeTime > 30000) {
			codemap.put("code", 102);
			return codemap;
		}
		TemplateSon tem = templateService.subtemplatefind(id, name);
		if (tem != null) {
			codemap.put("code", 101);
			return codemap;
		}
		TemplateSon subclass = new TemplateSon();
		subclass.setTempparid(id);
		subclass.setName(name);
		subclass.setStatus(Integer.valueOf(0));
		subclass.setMoney(Double.valueOf(money));
		subclass.setChargeTime(chargeTime);
		chargeQuantity = chargeQuantity * 100;
		subclass.setChargeQuantity(chargeQuantity.intValue());
		this.templateService.insertSonTemplate(subclass);
		TemplateSon temson = templateService.subtemplatefind(id, name);
		codemap.put("code", 200);
		codemap.put("ctemid", temson.getId());
		return codemap;
	}
	
	
	/**
	 * @Description： 修改充电模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/allowupdatesubclasscharge" })
	@ResponseBody
	public Map<String, Object> allowupdateSubclassCharge(Integer id, String name, Double money, String chargeTime, String chargeQuantity) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}

		TemplateSon oritem = templateService.getInfoTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateSon tem = templateService.subtemplatefind(id, name);
			if (tem != null) {
				codemap.put("code", 101);
				return codemap;
			}
		}
		double quantity = CommUtil.toDouble(chargeQuantity)*100;
		Integer quantityvalue = CommUtil.toInteger(quantity);
		int mun = templateService.updateSubclassCharge(id, name, money, chargeTime, quantityvalue);
		//TemplateSon temson = templateService.subtemplatefind(user.getId(), name);
		codemap.put("code", 200);
		codemap.put("ctemid", id);
		return codemap;
	}
	
	/***origin***  离线模板 ============================================================================================= */
	
	/**
	 * @Description： 查询获取一级离线模板且包含二级子模板
	 * @author： origin   
	 */
	@RequestMapping({ "/allowgetstairoffline" })
	public String allowgetStairOffline(Integer merid, Model model) {
		User user = (User) request.getSession().getAttribute("admin");
		if (user != null) {
			List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantid(merid, 1);
			model.addAttribute("user", user);
			model.addAttribute("templatelist", templatelist);
			model.addAttribute("arecode", 0);
			model.addAttribute("source", 0);
			return "merchant/templateoffline";
		} else {
			return "erroruser";
		}
	}
	
	 /**
	  * @Description： 添加一级离线模板
	  * @author： origin   
	  */
	@RequestMapping({ "/allowaddstairoffline" })
	@ResponseBody
	public Map<String, Object> allowaddStairOffline(Integer merid, String name, String remark, String common1) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent partem = templateService.templateByName(merid, 1, name);
		if(partem !=null){
			codemap.put("code", 101);
			return codemap;
		}
		templateService.addStairOffline(merid, name, remark, common1);
		TemplateParent partemp = templateService.templateByName(merid, 1, name);
		codemap.put("code", 200);
		codemap.put("temid", partemp.getId());
		return codemap;
	}
	
	 /**
	  * @Description： 修改一级离线模板
	  * @author： origin   
	  */
	@RequestMapping({ "/allowupdatestairoffline" })
	@ResponseBody
	public Map<String, Object> allowupdateStairOffline(Integer id, String name, String remark, String common1) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent oritem = templateService.getParentTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateParent partem = templateService.templateByName(oritem.getMerchantid(), 1, name);
			if(partem !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		TemplateParent template = new TemplateParent();
		template.setName(name);
		template.setId(id);
		template.setRemark(remark);
		template.setCommon1(common1);
		this.templateService.updateParentTemplate(template);
		codemap.put("code", 200);
		codemap.put("temid", id);
		return codemap;
	}
	
	/**  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  */
	/**
	 * @Description： 添加离线模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/allowaddsubclassoffline" })
	@ResponseBody
	public Map<String, Object> allowaddSubclassOffline(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon sontem = templateService.subtemplatefind(id, name);
		if (sontem != null) {
			codemap.put("code", 101);
			return codemap;
		}
		TemplateSon subclass = new TemplateSon();
		subclass.setTempparid(id);
		subclass.setName(name);
		//subclass.setStatus(1);
		subclass.setMoney(money);
		subclass.setRemark(remark);
		this.templateService.insertSonTemplate(subclass);
		TemplateSon ctem = templateService.subtemplatefind(id, name);
		codemap.put("code", 200);
		codemap.put("ctemid", ctem.getId());
		return codemap;
	}
	
	/**
	 * @Description： 修改离线模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/allowupdatesubclassoffline" })
	@ResponseBody
	public Map<String, Object> allowupdateSubclassOffline(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon oritem = templateService.getInfoTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateSon temson = templateService.subtemplatefind(id, name);
			if(temson !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		templateService.updateSubclassOffline(id, name, money, remark);
		codemap.put("code", 200);
		codemap.put("ctemid", id);
		return codemap;
	}
	
	/***origin***  脉冲（投币）模板 ============================================================================================= */
	
	 /**
	  * @Description： 添加一级脉冲（投币）模板
	  * @author： origin   
	  */
	@RequestMapping({ "/allowaddstairincoins" })
	@ResponseBody
	public Map<String, Object> allowaddStairIncoins(Integer merid, String name, String remark, String common1, Integer permit, Integer walletpay) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent partem = templateService.templateByName(merid, 2, name);
		if(partem !=null){
			codemap.put("code", 101);
			return codemap;
		}
		templateService.addStairIncoins(merid, name, remark, common1, permit, walletpay);
		TemplateParent partemp = templateService.templateByName(merid, 2, name);
		codemap.put("code", 200);
		codemap.put("temid", partemp.getId());
		return codemap;	
	}
	
	 /**
	  * @Description： 修改一级脉冲（投币）模板
	  * @author： origin   
	  */
	@RequestMapping({ "/allowupdatestairincoins" })
	@ResponseBody
	public Map<String, Object> allowupdateStairIncoins(Integer id, String name, String remark, String common1, Integer permit, Integer walletpay) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent oritem = templateService.getParentTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateParent partem = templateService.templateByName(oritem.getMerchantid(), 2, name);
			if(partem !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		TemplateParent template = new TemplateParent();
		template.setId(id);
		template.setName(name);
		template.setRemark(remark);
		template.setCommon1(common1);
		template.setPermit(permit);
		template.setWalletpay(walletpay);
		this.templateService.updateParentTemplate(template);
		codemap.put("code", 200);
		codemap.put("temid", id);
		return codemap;
	}
	
	/**  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  */
	/** 
	 * @Description： 添加脉冲（投币）模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/allowaddsubclassincoins" })
	@ResponseBody
	public Map<String, Object> allowaddSubclassIncoins(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon temson = templateService.subtemplatefind(id, name);
		if(temson !=null){
			codemap.put("code", 101);
			return codemap;
		}
		TemplateSon subclass = new TemplateSon();
		subclass.setTempparid(id);
		subclass.setName(name);
		//subclass.setStatus(Integer.valueOf(0));
		subclass.setMoney(money);
		subclass.setRemark(remark);
		this.templateService.insertSonTemplate(subclass);
		TemplateSon ctem = templateService.subtemplatefind(id, name);
		codemap.put("code", 200);
		codemap.put("ctemid", ctem.getId());
		return codemap;
	}
	
	
	/**
	 * @Description： 修改脉冲（投币）模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/allowupdatesubclassincoins" })
	@ResponseBody
	public Map<String, Object> allowupdateSubclassIncoins(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon oritem = templateService.getInfoTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateSon temson = templateService.subtemplatefind(id, name);
			if(temson !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		int mun = templateService.updateSubclassIncoins(id, name, money, remark);
		codemap.put("code", 200);
		codemap.put("ctemid", id);
		return codemap;
	}
	
//===@origin ========================================================================================	
	
	 /**
	  * @Description： 修改一级钱包模板
	  * @author： origin   
	  */
	@RequestMapping({ "/allowupstairwallet" })
	@ResponseBody
	public Map<String, Object> allowupdatestairwallet(Integer id, String name, String remark, String common1) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent oritem = templateService.getParentTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateParent partem = templateService.templateByName(oritem.getMerchantid(), 3, name);
			if(partem !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		TemplateParent template = new TemplateParent();
		template.setId(id);
		template.setName(name);
		template.setRemark(remark);
		template.setCommon1(common1);
		this.templateService.updateParentTemplate(template);
		codemap.put("code", 200);
		codemap.put("temid", id);
		return codemap;
	}
	
	/**  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  */
	/** 
	 * @Description： 添加钱包模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/allowaddsubclasswallet" })
	@ResponseBody
	public Map<String, Object> allowaddsubclasswallet(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon temson = templateService.subtemplatefind(id, name);
		if(temson !=null){
			codemap.put("code", 101);
			return codemap;
		}
		TemplateSon subclass = new TemplateSon();
		subclass.setTempparid(id);
		subclass.setName(name);
		//subclass.setStatus(Integer.valueOf(0));
		subclass.setMoney(money);
		subclass.setRemark(remark);
		this.templateService.insertSonTemplate(subclass);
		TemplateSon ctem = templateService.subtemplatefind(id, name);
		codemap.put("code", 200);
		codemap.put("ctemid", ctem.getId());
		return codemap;
	}
	
	
	/**
	 * @Description： 修改钱包模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/allowupsubclasswallet" })
	@ResponseBody
	public Map<String, Object> allowupsubclasswallet(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon oritem = templateService.getInfoTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateSon temson = templateService.subtemplatefind(id, name);
			if(temson !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		int mun = templateService.updateSubclassIncoins(id, name, money, remark);
		codemap.put("code", 200);
		codemap.put("ctemid", id);
		return codemap;
	}
	
	/***origin***  在线卡模板 ============================================================================================= */
	
	
	 /**
	  * @Description： 修改一级在线卡模板
	  * @author： origin   
	  */
	@RequestMapping({ "/allowupstaironline" })
	@ResponseBody
	public Map<String, Object> allowupstaironline(Integer id, String name, String remark, String common1) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateParent oritem = templateService.getParentTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateParent partem = templateService.templateByName(oritem.getMerchantid(), 4, name);
			if(partem !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		TemplateParent template = new TemplateParent();
		template.setId(id);
		template.setName(name);
		template.setRemark(remark);
		template.setCommon1(common1);
		this.templateService.updateParentTemplate(template);
		codemap.put("code", 200);
		codemap.put("temid", id);
		return codemap;
	}
	
	/**  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  */
	/** 
	 * @Description： 添加在线卡模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/allowaddsubclassonline" })
	@ResponseBody
	public Map<String, Object> allowaddsubclassonline(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon temson = templateService.subtemplatefind(id, name);
		if(temson !=null){
			codemap.put("code", 101);
			return codemap;
		}
		TemplateSon subclass = new TemplateSon();
		subclass.setTempparid(id);
		subclass.setName(name);
		//subclass.setStatus(Integer.valueOf(0));
		subclass.setMoney(money);
		subclass.setRemark(remark);
		this.templateService.insertSonTemplate(subclass);
		TemplateSon ctem = templateService.subtemplatefind(id, name);
		codemap.put("code", 200);
		codemap.put("ctemid", ctem.getId());
		return codemap;
	}
	
	
	/**
	 * @Description： 修改在线卡模板的子类模板
	 * @author： origin 
	 */
	@RequestMapping({ "/allowupsubclassonline" })
	@ResponseBody
	public Map<String, Object> allowupsubclassonline(Integer id, String name, Double money, Double remark) {
		Map<String, Object> codemap = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("admin");
		if (null == user) {
			codemap.put("code", 100);
			return codemap;
		}
		TemplateSon oritem = templateService.getInfoTemplateOne(id);
		if(!oritem.getName().equals(name)){
			TemplateSon temson = templateService.subtemplatefind(id, name);
			if(temson !=null){
				codemap.put("code", 101);
				return codemap;
			}
		}
		int mun = templateService.updateSubclassIncoins(id, name, money, remark);
		codemap.put("code", 200);
		codemap.put("ctemid", id);
		return codemap;
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/***origin***  finally模板===================================================================================== */
	
	
	
	
	
	/**
	 * @Description： 根据id查询指定父模板信息
	 * @author： origin  
	 */
	@RequestMapping({ "/getParentTemplateOne" })
	@ResponseBody
	public TemplateParent getParentTemplateOne(Integer id,Model model) {
		TemplateParent template = templateService.getParentTemplateOne(id);
		return template;
	}
	
/** ================================================================================================================= */	
	/**
	 * 查询获取充电模板（主模板和包含的子模板）
	 */
	@RequestMapping({ "/getParentTempmanage" })
	public String getParentTempmanage(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			List<TemplateParent> templatelist = templateService.getParentTemplateListByMerchantid(user.getId(), 0);
			model.addAttribute("user", user);
			model.addAttribute("templatelist", templatelist);
			return "merchant/templatecharge";
			//return "merchant/chargetempmanage";
		} else {
			return "erroruser";
		}
	}
	
	/**
	 * 添加充电模板（主）
	 */
	@RequestMapping({ "/addTempmanage" })
	@ResponseBody
	public String addParentTempmanage(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		
		if (user != null) {
			this.templateService.insertParentTemplate(request);
			return "1";
		} else {
			return "erroruser";
		}
	}
	
	/**A
	 * 修改充电模板数据（主）/修改离线模板数据（主）/修改模拟投币模板数据（主）
	 * @param id, name, remark, common1, permit, walletpay
	 */
	@RequestMapping({ "/updateTempmanage" })
	@ResponseBody
	public String alterChargeTempmanage(Integer id, String name, String remark, String common1, Integer permit, Integer walletpay) {
		TemplateParent templateParent = new TemplateParent();
		templateParent.setId(id);
		templateParent.setName(name);
		templateParent.setRemark(remark);
		templateParent.setCommon1(common1);
		templateParent.setPermit(permit);
		templateParent.setWalletpay(walletpay);
		this.templateService.updateParentTemplate(templateParent);
		return "1";
	}
	
	/**
	 * 删除充电模板（主），同步删除下级模板
	 * @param id
	 */
	@RequestMapping({ "/deleteTempmanage" })
	public String deleteChargeTempmanage(Integer id, Model model) {
		this.templateService.deleteParentTemplate(id);
		return "redirect:/wctemplate/getParentTempmanage";
	}
	
	/*** 充电模板（子） *******************************************************************************************/
	
	/**
	 * 添加充电子模板（主模板下级）
	 * @param id, name, money, chargeTime, chargeQuantity
	 * @return
	 */
	@RequestMapping({ "/addSonTempmanage" })
	@ResponseBody
	public String addTempmanage(Integer id, String name, Double money, Integer chargeTime, Double chargeQuantity) {
		if (money > 30000 || chargeQuantity > 30000 || chargeTime > 30000) {
			return "0";
		}
		TemplateSon templateSon = new TemplateSon();
		templateSon.setTempparid(id);
		templateSon.setName(name);
		templateSon.setStatus(Integer.valueOf(0));
		templateSon.setMoney(Double.valueOf(money));
		templateSon.setChargeTime(chargeTime);
		chargeQuantity = chargeQuantity * 100;
		templateSon.setChargeQuantity(chargeQuantity.intValue());
		this.templateService.insertSonTemplate(templateSon);
		return "1";
	}
	
	/**A
	 * 充电模板子类模板的单条查询
	 * @param id, model
	 * @return
	 */
	@RequestMapping({ "/selectOneCharger" })
	@ResponseBody
	public TemplateSon selectOneCharger(Integer id, Model model) {
		TemplateSon templatesonlist = templateService.getInfoTemplateOne(id);
		return templatesonlist ;
	}
	
	/**A
	 * 更改子类模板（充电子类）
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/updateSonTempmanage" })
	@ResponseBody
	public String updateSonTempmanage(HttpServletRequest request, HttpServletResponse response, Model mode) {
		return templateService.updateSonTemplate(request);
	}
	
	/**
	 * 删除指定子类充电模板
	 * @param id, model
	 * @return
	 */
	@RequestMapping({ "/deleteSonTempmanage" })
	public String deleteSonTempmanage(String id, Model model) {
		this.templateService.deleteSonTempmanage(StringUtil.getIntString(id));
		return "redirect:/wctemplate/getParentTempmanage";
	}

	/*****************************************************************************************************/
	
	/**
	 *  充电模板数据信息预览
	 * @param id, model
	 * @return
	 */
	@RequestMapping(value = "/preview")
	public String preview(Integer id, Model model) {
		List<TemplateSon> templatelist = templateService.getSonTemp(id);
		TemplateParent template = templateService.getParentTemplateOne(id);
		model.addAttribute("template", template);
		model.addAttribute("templatelist", templatelist);
		model.addAttribute("user", request.getSession().getAttribute("user"));
		return "merchant/preview";
	}
	
	// 查询获取充电设置模板（子）
	@RequestMapping({ "/getSonTempmanage" })
	public String chargetempmanageson(Model model) {
		List<TemplateSon> templatelist = this.templateService.getSonTemplateList();
		model.addAttribute("templatelist", templatelist);
		return "merchant/chargetempmanage";
	}
	/*****************************************************************************************************/
	/*** 离线卡模板（主） *************************************************************************************/
	
	/**
	 * 查询获取离线卡模板（主模板和既包含的子模板）
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/getofflinetemplate" })
	public String getgetOfflineTemplate(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			List<TemplateParent> templatelist = templateService.getParentTemforOffLine(user.getId(), 1);
			model.addAttribute("user", user);
			model.addAttribute("templatelist", templatelist);
			return "merchant/templateoffline";
		} else {
			return "erroruser";
		}
	}
	
	/**A
	 * 删除离线卡模板（主），同步删除下级模板
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/offLineTemDelete" })
	public String offLineTemDelete(Integer id, Model model) {
		this.templateService.deleteParentTemplate(id);
		return "redirect:/wctemplate/getofflinetemplate";
	}
	
	/**A
	 * 添加离线卡模板（主）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({ "/offLineTemAdd" })
	public String addOffLine(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		System.out.println("offLineTemAdd");
		if (user != null) {
			this.templateService.insertParentTemplate(request);
			return "redirect:/wctemplate/getofflinetemplate";
		} else {
			return "erroruser";
		}
	}
	/*** 离线卡子类模板  *******************************************************************************************/
	
	/**A
	 * 添加离线卡子类模板（主模板下级）
	 * @param id, name, money, remark
	 * @return
	 */
	@RequestMapping({ "/insertOffLine" })
	public String insertOffLine(Integer id, String name, Double money, Double remark) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			TemplateSon templateSon = new TemplateSon();
			templateSon.setTempparid(id);
			templateSon.setName(name);
			templateSon.setStatus(Integer.valueOf(1));
			templateSon.setMoney(Double.valueOf(money));
			templateSon.setRemark(remark);
			this.templateService.insertSonTemplate(templateSon);
			return "redirect:/wctemplate/getofflinetemplate";
		} else {
			return "erroruser";
		}
	}
	
	/**
	 * 离线卡子类模板的单条查询
	 * @param id, model
	 * @return
	 */
	@RequestMapping({ "/selectOneOffLine" })
	@ResponseBody
	public TemplateSon selectOneOffLine(Integer id, Model model) {
		TemplateSon editOffLine = templateService.getInfoTemplateOne(id);
		return editOffLine ;
	}
	
	/**A
	 * 离线卡子类模板的修改
	 * @param request, response, model
	 * @return
	 */
	@RequestMapping({ "/updateOffLine" })
	@ResponseBody
	public Object updateOffLine(HttpServletRequest request, HttpServletResponse response, Model model) {
		 Object num = templateService.updateSonTemplate(request);
		return num;
	}
	
	/**A
	 * 删除指定的离线卡子类模板
	 * @param id, model
	 * @return
	 */
	@RequestMapping({ "/deleteOffLine" })
	public String deleteOffLine(Integer id, Model model) {
		this.templateService.deleteSonTempmanage(id);
		return "redirect:/wctemplate/getofflinetemplate";
	}
	
	/*****************************************************************************************************/
	//***********************************************************************************************************
	
	
	/*** 模拟投币模板（主） *******************************************************************************************/
	/**
	 * 查询获取模拟投币模板（主模板和既包含的子模板）
	 * @param model
	 * @return coins
	 */
	@RequestMapping({ "/getcoinstemplate" })
	public String getcoinstemplate(Model model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			List<TemplateParent> templatelist = templateService.getParentTemforOffLine(user.getId(), 2);
			model.addAttribute("user", user);
			model.addAttribute("templatelist", templatelist);
			//return "merchant/coinstemplates";
			return "merchant/templateincoins";
		} else {
			return "erroruser";
		}
	}
	
	/**A
	 * 删除模拟投币模板（主），同步删除下级模板
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/coinsTemDelete" })
	public String coinsTemDelete(Integer id, Model model) {
		this.templateService.deleteParentTemplate(id);
		return "redirect:/wctemplate/getcoinstemplate";
	}
	
	/**
	 * 添加模拟投币模板（主）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({ "/coinsTemInsert" })
	public String coinsTemInsert(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		System.out.println("offLineTemAdd");
		if (user != null) {
			this.templateService.insertParentTemplate(request);
			return "redirect:/wctemplate/getcoinstemplate";
		} else {
			return "erroruser";
		}
	}
	/*** 模拟投币子类模板  *******************************************************************************************/
	
	/**A
	 * 添加模拟投币子类模板（主模板下级）
	 * @param id, name, money, remark
	 * @return
	 */
	@RequestMapping({ "/insertsoncoins" })
	public String insertsoncoins(Integer id, String name, Double money, Double remark) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			TemplateSon templateSon = new TemplateSon();
			templateSon.setTempparid(id);
			templateSon.setName(name);
			templateSon.setStatus(Integer.valueOf(1));
			templateSon.setMoney(Double.valueOf(money));
			templateSon.setRemark(remark);
			this.templateService.insertSonTemplate(templateSon);
			return "redirect:/wctemplate/getofflinetemplate";
		} else {
			return "erroruser";
		}
	}
	
	/**
	 * 模拟投币子类模板的单条查询
	 * @param id, model
	 * @return
	 */
	@RequestMapping({ "/selectOneSonCoins" })
	@ResponseBody
	public TemplateSon selectOneSonCoins(Integer id, Model model) {
		TemplateSon editOffLine = templateService.getInfoTemplateOne(id);
		return editOffLine ;
	}
	
	/**A
	 * 模拟投币子类模板的修改
	 * @param request, response, model
	 * @return
	 */
	@RequestMapping({ "/updateSonCoins" })
	@ResponseBody
	public Object updateSonCoins(HttpServletRequest request, HttpServletResponse response, Model model) {
		 Object num = templateService.updateSonTemplate(request);
		return num;
	}
	
	/**A
	 * 删除指定的模拟投币子类模板
	 * @param id, model
	 * @return
	 */
	@RequestMapping({ "/deleteCoins" })
	public String deleteCoins(Integer id, Model model) {
		this.templateService.deleteSonTempmanage(id);
		return "redirect:/wctemplate/getofflinetemplate";
	}
	
	/*****************************************************************************************************/
	/**
	 * @Description：添加或修改v3设备模板（主模板含子模板）
	 * @author： origin
	 * @createTime：2020年4月11日下午2:47:46
	 */
	@RequestMapping({ "/insertAmendTemp" })
	@ResponseBody
	public Object insertAmendTemp(HttpServletRequest request, HttpServletResponse response, Model model) {
		Object result = templateService.insertAmendTemp(request);
		return result ;
	}
	
	/**
	 * @Description：添加v3主模板
	 * @author： origin
	 * @createTime：2020年4月11日下午2:46:16
	 */
	@RequestMapping({ "/insertVDeviceTem" })
	@ResponseBody
	public Object insertVDeviceTem(HttpServletRequest request, HttpServletResponse response, Model model) {
		Object result = templateService.insertVDeviceTem(request);
		return result ;
	}
	
	/**
	 * @Description：添加指定zi模板信息（主要应用于v3模板）
	 * @author： origin
	 * @createTime：2020年4月11日下午2:44:05
	 */
	@RequestMapping({ "/additionAssignTemp" })
	@ResponseBody
	public Object additionAssignTemp(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Integer tempid = CommUtil.toInteger(request.getParameter("parid"));
			if(tempid.equals(-1)){
				return CommUtil.responseBuildInfo(210, "没有主模板id", datamap);
			}else{
				Object result = templateService.additionAssignTemp(request);
				return result ;
			}
		} catch (Exception e) { 
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	@RequestMapping({ "/updataVDeviceTem" })
	@ResponseBody
	public Object updataVDeviceTem(HttpServletRequest request, HttpServletResponse response, Model model) {
		Object result = templateService.updataVDeviceTem(request);
		return result ;
	}
	

	@RequestMapping({ "/selectVDeviceTem" })
	@ResponseBody
	public Object selectVDeviceTem(HttpServletRequest request, HttpServletResponse response, Model model) {
		Object result = templateService.selectVDeviceTem(request);
		return result ;
	}
	
	/**
	 * 删除指定子类充电模板
	 * @param id, model
	 * @return
	 */
	@RequestMapping({ "/deleteVDeviceTem" })
	@ResponseBody
//	public Object deleteVDeviceTem(String id, Model model) {
	public Object deleteVDeviceTem(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			Integer temsid = CommUtil.toInteger(maparam.get("id"));
			if(temsid.equals(-1)){
				CommUtil.responseBuildInfo(210, "模板id不正确", datamap);
			}else{
				Integer isUpdate = CommUtil.toInteger(maparam.get("isUpdateChargeInfo"));
				if(isUpdate==1){
					String chargeinfo = CommUtil.toStringTrim(maparam.get("chargeInfo"));
					Integer tempid = CommUtil.toInteger(maparam.get("tempid"));
					TemplateParent temparen = new TemplateParent();
					temparen.setId(tempid);
					temparen.setChargeInfo(chargeinfo);
					templateService.updateParentTemplate(temparen);
				}
				templateService.deleteSonTempmanage(temsid);
				CommUtil.responseBuildInfo(200, "成功", datamap);
			}
		} catch (Exception e) { 
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
			e.printStackTrace();
		}
		return datamap;
	}
	
	/**
	  * @Description： 删除v3主模板
	  * @author： origin   
	  */
		@RequestMapping({ "/deleteMainDeviceTemp" })
		@ResponseBody
		public Object deleteMainDeviceTemp(Integer id) {
			Map<String, Object> datamap = new HashMap<String, Object>();
			try {
				id = CommUtil.toInteger(id);
				User user = (User) request.getSession().getAttribute("user");
				if (user == null) return CommUtil.responseBuildInfo(302, "未获取到商户信息", datamap);
				templateService.deleteParentTemplate(id);
				CommUtil.responseBuildInfo(200, "成功", datamap);
			} catch (Exception e) { 
				CommUtil.responseBuildInfo(301, "异常错误", datamap);
				e.printStackTrace();
			}
			return datamap;
		}
}
