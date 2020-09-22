package com.hedong.hedongwx.web.controller.computerpc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.DealerAuthority;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AuthorityService;
import com.hedong.hedongwx.service.FeescaleService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;

/**
 * @Description： 权限类
 * @author  origin  创建时间：   2019年7月8日 下午2:56:46  
 */
@Controller
@RequestMapping(value = "/allowAuthority")
public class AuthorityController {
	
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private FeescaleService feescaleService;
	@Autowired
	private UserService userService;
	
	/**
	 * @Description： 指定商户某个消息权限开关
	 * @author： origin 创建时间：   2019年7月25日 上午11:57:13
	 */
	@RequestMapping({ "/messSwitch" })
	@ResponseBody
	public Map<String, Object> messSwitchAuthority(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = authorityService.messSwitchAuthority(request);
		return result;
	}
	
	
	@RequestMapping({ "/settingSwitch" })
	@ResponseBody
	public Object settingSwitch(HttpServletRequest request, HttpServletResponse response){
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = userService.redactAccountInfo(request);
			result = authorityService.dealerauthoritySeting(request);
		}
		return result;
	}
	
	
	
	
	
	/**
	 * @Description： 根据商户id查询消息权限
	 * @author： origin 创建时间：   2019年7月25日 上午11:35:49
	 */
	@RequestMapping({ "/selectMessSwitch" })
	@ResponseBody
	public Map<String, Object> selectMessSwitch( Integer merid){
//		DealerAuthority result = authorityService.selectMessSwitch( merid);
//		return result;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DealerAuthority result = authorityService.selectMessSwitch( merid);
			User dealerdata = userService.selectUserById(merid);
			map.put("result", result);
			map.put("dealer", dealerdata);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * 商家设置自动缴费和分摊
	 * @return
	 */
	@RequestMapping("/Switch")
	@ResponseBody
	public Object merSetAutoPay(Integer merid,Integer autoPay, Integer apportion,Integer autowithdraw){
		Object result = null;
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			result = feescaleService.updateSwitch(merid, autoPay, apportion, autowithdraw);
			return result;
		}else{
			result = CommUtil.responseBuild(901, "session缓存失效", "");
			return result;
		}
		
			
		
	}	
	
}
