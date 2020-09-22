package com.hedong.hedongwx.web.controller.webpc;

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
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;

/**
 * @Description： 权限类
 * @author  origin  创建时间：   2019年7月8日 下午2:56:46  
 */
@Controller
@RequestMapping(value = "/weballowPermission")
public class PermissionController {
	
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private UserService userService;
	
	/**
	 * @Description： 指定商户某个消息权限开关
	 * @author： origin 创建时间：   2019年7月25日 上午11:57:13
	 */
	@RequestMapping({ "/settingSwitch" })
	@ResponseBody
	public Object settingSwitch(HttpServletRequest request, HttpServletResponse response){
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
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
	
}

