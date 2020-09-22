package com.hedong.hedongwx.web.controller.webpc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.service.BasicsService;
import com.hedong.hedongwx.utils.CommUtil;
@Controller
@RequestMapping(value="/basicsInfo")
public class BasicsController {
	
	@Autowired
	private BasicsService basicsService;

	/**
	 * separate
	 * @Description： 获取所有商户信息，包含管理员
	 * @author： origin   2019年11月20日 上午9:58:08
	 */
	@RequestMapping(value = "/getAllDealerData")
	@ResponseBody
	public Object bindequ(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = basicsService.getAllDealerData(request);
		}
		return JSON.toJSON(result);
	}
	

	/**
	 * separate
	 * @Description： 获取手机验证码信息
	 * @author： origin 
	 */
	@RequestMapping(value="/getCaptchaData")
	@ResponseBody
    public Object getCaptchaData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		Map<String, Object> maparam = CommUtil.getRequestParam(request);
		Integer source =  CommUtil.toInteger(maparam.get("source"));
		if(source.equals(1)){
			result = basicsService.getCaptchaData(request);
		}else{
			if(CommonConfig.isExistSessionUser(request)){
				result = CommUtil.responseBuild(901, "session缓存失效", "");
			}else{
				result = basicsService.getCaptchaData(request);
			}
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description： 判断密码是否正确
	 * @author： origin 
	 */
	@RequestMapping(value="/estimatePassword")
	@ResponseBody
    public Object estimatePassword(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = basicsService.estimatePassword(request);
		}
		return JSON.toJSON(result);
	}
	
	@RequestMapping(value="/inquireDirectTempData")
	@ResponseBody
	public Object inquireDirectTempData(HttpServletRequest request, HttpServletResponse response) {
		Integer tempid = 0;
		Integer dealid = 1896;
		String code = "000027";
		String version = "03";
		Map<String, Object> result = basicsService.inquireDirectTempData(tempid, dealid, code, version);
		return JSON.toJSON(result);
	}
	
	
}
