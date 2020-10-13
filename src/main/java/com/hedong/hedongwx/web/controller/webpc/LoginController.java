package com.hedong.hedongwx.web.controller.webpc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.service.UserService;

/**
 * @Description： 
 * @author  origin  创建时间：   2019年10月18日 下午2:01:36  
 */
@Controller
@RequestMapping(value = "/webenter")
public class LoginController {

	@Autowired
	private UserService userService;
	
	@RequestMapping({ "/accountEnter" })
	@ResponseBody
	public Object accountEnter(String phone,  String password, Integer type){
		System.err.println("==============");
		Map<String, Object> result = userService.accountEnter( phone, password, type);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userInfo", result);
		return JSON.toJSON(map);
	}
	
	/**
	 * separate
	 * @Description： 短信验证码登录
	 * @author： origin  
	 */
	@RequestMapping({ "/captchaEnter" })
	@ResponseBody
	public Object captchaEnter(HttpServletRequest request,  HttpServletResponse response){
		Object result =  userService.captchaEnter(request);
		return JSON.toJSON(result);
	}
	
	//扫码登录(路径)  scan QR code
	/**
	 * separate
	 * @Description： 微信扫码登录
	 * @author： origin  
	 */
	@RequestMapping({ "/wechatEnter" })
	@ResponseBody
	public Object wechatEnter(HttpServletRequest request,  HttpServletResponse response){
		Object result =  userService.wechatEnter(request);
		return JSON.toJSON(result);
	}
	
	
	
	
	
	
	
}
