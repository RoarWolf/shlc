package com.hedong.hedongwx.web.controller.computerpc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.config.WeChatOpenPlatform;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.MobileSendUtil;
import com.hedong.hedongwx.utils.StringUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/pcadminlogin")
public class AdminiLoginController {//用户登录控制类
	
	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest request;
	
	/*** 用户登录  ************************************************************************************/
	//进入登录界面 扫二维码
	@RequestMapping({ "/index" })
	public String loginface() {
		return "computer/logincomputer";
	}
	
	//扫码登录(路径)  scan QR code 
	@RequestMapping({ "/scanQRCode" })
	public String scanQRCodes(Model model){		
		String code = this.request.getParameter("code");
		String state = this.request.getParameter("state");
		String unionid = null;
		if(code!=null){
			JSONObject userinfo = null;
			try {
				userinfo = WeChatOpenPlatform.getOpenUserinfo(code);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(userinfo==null && state.equals("1")) return "computer/logincomputer";
			if(userinfo==null && state.equals("2")) return "computer/login";
			unionid = userinfo.getString("unionid");
			System.out.println("unionid:  "+unionid);
			//登录判断用户是否存在或有权限
			User admin = userService.getUserByUnionid(unionid);
			if(admin!=null){
				request.getSession().setAttribute("admin",admin);
				if(admin.getLevel()==0){
					return "redirect:/pcstatistics/collectinfo";
				}else if(admin.getLevel()==2){
					return "redirect:/pcstatistics/agentdatacollect";
				}
			}else{
				model.addAttribute("hintInfo", "不是管理员或商户，获取信息失败；</br>请先登录微信公众号商户中心，刷新当前页面在扫码登录。");
				return "computer/loginHint";
			}
			model.addAttribute("hintInfo", "不是管理员或商户，获取信息失败；</br>请先登录微信公众号商户中心，刷新当前页面在扫码登录。");
			return "computer/loginHint";
		}else{
			model.addAttribute("hintInfo", "不是管理员或商户，获取信息失败；</br>请先登录微信公众号商户中心，刷新当前页面在扫码登录。");
			return "computer/loginHint";
		}
	}
	
	//点击登录（点击登录判断账户，进入指定页面）
	@RequestMapping({ "/login" })
	@ResponseBody
	public String adminlogin(HttpServletRequest request,  HttpServletResponse response){
		String result = userService.adminlogin(request, response);
		return result;
	}
	
	/**
	 * @Description： 账号登录【ajax判断登录 0管理员登录  1商户登录   和其他情况）
	 * @author： origin  
	 */
	@RequestMapping({ "/accountlogin" })
	@ResponseBody
	public String accountLogin(String account, String password){
		String result = userService.accountlogin(account, password);
		return result;
	}
	
	//短信验证码登录（登录判断账户，进入指定页面）  验证码 Verification code	security	authcode 
	@RequestMapping({ "/codelogin" })
	@ResponseBody
	public String codeLogin(String mobile, String security, String authcode, String authtime){
		String result = userService.codelogin(mobile, security, authcode, authtime);
		return result;
	}
	
	@RequestMapping({ "/getauthcode" })
	@ResponseBody
	public Map<String, Object> Captcha( String mobile) {
		Map<String, Object> code = new HashMap<String, Object>();
		boolean bool = existAccount(mobile);
		if(!bool){
			code.put("code", 602);
			return code;
		}
		String authcode = StringUtil.getRandNum();
		String[] params = { authcode, "3" };
		String[] mobilephon = { mobile };
		MobileSendUtil.TemplateMobileSend(params, mobilephon);
		code.put("authcode", authcode);
		return code;
	}
	
	
	@RequestMapping({ "/existaccount" })
	@ResponseBody
	public Map<String, Object> judgeAccount( String mobile) {
		Map<String, Object> code = new HashMap<String, Object>();
		User user =  userService.existAdmin(mobile);
		if(user!=null){
			code.put("code", 601);
		}else{
			code.put("code", 602);
		}
		return code;
	}
	
	//判断用户表中是否存在该用户
	public boolean existAccount(String mobile) {
		User user =  userService.existAdmin(mobile);
		System.out.println("user     "+user);
		if(user!=null) return true;
		return false;
	}
	
	@RequestMapping("/a")
	public String login() {
		return "computer/login";
	}
	
	//退出登录
	@RequestMapping({ "/adminlogout" })
	public Object logout(HttpServletRequest request,  HttpServletResponse response){
		User user = CommonConfig.getAdminReq(request);
		int rank = user.getLevel();
		request.getSession().invalidate();
		String path = "/pcadminlogin/index";
		if(rank==2){
			path = "/a";
		}
		return "redirect:"+path;
	}
}
