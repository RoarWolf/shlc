package com.hedong.hedongwx.web.controller.webpc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.entity.Admin;
import com.hedong.hedongwx.entity.District;
import com.hedong.hedongwx.entity.Meruser;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.DistrictService;
import com.hedong.hedongwx.service.MeruserService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.JedisUtils;

/**
*author：RoarWolf
*createtime：2020年11月12日
*/

@Controller
@RequestMapping("/webMeruser")
public class MeruserController {
	
	@Autowired
	private MeruserService meruserService;
	@Autowired
	private DistrictService districtService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private UserService userService;

	@RequestMapping("/insertMeruser")
	@ResponseBody
	public Object insertMeruser(Meruser meruser) {
		Object result = null;
		if(JedisUtils.get("admin")==null){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  meruserService.insertMeruser(meruser);
		}
		return JSON.toJSON(result);
	}
	
	@RequestMapping("/updateMeruser")
	@ResponseBody
	public Object updateMeruser(Meruser meruser) {
		Object result = null;
		if(JedisUtils.get("admin")==null){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  meruserService.updateMeruser(meruser);
		}
		return JSON.toJSON(result);
	}
	
	@RequestMapping("/queryMeruserlist")
	@ResponseBody
	public Object queryMeruserlist(Meruser meruser) {
		Object result = null;
		if(JedisUtils.get("admin")==null){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  meruserService.selectMeruserList(meruser);
		}
		return JSON.toJSON(result);
	}
	
	@RequestMapping("/queryUserOperlist")
	@ResponseBody
	public Object queryUserOperlist(String opename,
			String objname,Integer type,Integer source,
			String startTime,String endTime,
			Integer startindex,Integer pages) {
		Object result = null;
		if(JedisUtils.get("admin")==null){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  meruserService.userOperateRecordlist(opename, objname, type, source, startTime, endTime, startindex, null);
		}
		return JSON.toJSON(result);
	}
	
	@RequestMapping("/editUserOper")
	@ResponseBody
	public Object editUserOper(Integer id,Integer type) {
		Object result = null;
		if(JedisUtils.get("admin")==null){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			Admin admin = (Admin) request.getSession().getAttribute("admin");
			result =  meruserService.updateOperaterecord(id, admin.getId(), type);
		}
		return JSON.toJSON(result);
	}
	
	@RequestMapping("/queryDistrictlist")
	@ResponseBody
	public Object queryDistrictlist(String districtId, String levelType) {
		Object result = null;
		if(JedisUtils.get("admin")==null){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			try {
				Map<String,Object> map = new HashMap<>();
				District district = new District();
				district.setParentId(districtId);
				district.setLevelType(levelType);
				List<District> districtlist = districtService.selectDistrictByParam(district);
				map.put("districtlist", districtlist);
				result =  CommUtil.responseBuildInfo(1000, "获取成功", map);
			} catch (Exception e) {
				result = CommUtil.responseBuildInfo(1001, "添加失败，系统异常", null);
			}
		}
		return JSON.toJSON(result);
	}
	
	@RequestMapping("/queryUserByPhonenum")
	@ResponseBody
	public Object queryUserByPhonenum(String phonenum) {
		Object result = null;
		if(JedisUtils.get("admin")==null){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			try {
				Map<String,Object> map = new HashMap<>();
				User user = userService.getUserByPhoneNum(phonenum);
				map.put("userinfo", user);
				result =  CommUtil.responseBuildInfo(200, "获取成功", map);
			} catch (Exception e) {
				result = CommUtil.responseBuildInfo(201, "查询失败，系统异常", null);
			}
		}
		return JSON.toJSON(result);
	}
}
