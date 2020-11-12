package com.hedong.hedongwx.web.controller.webpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.entity.Meruser;
import com.hedong.hedongwx.service.MeruserService;
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
}
