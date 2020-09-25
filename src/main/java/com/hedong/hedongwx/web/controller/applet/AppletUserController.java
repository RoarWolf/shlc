package com.hedong.hedongwx.web.controller.applet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hedong.hedongwx.service.GeneralDetailService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.WeixinUtil;

import net.sf.json.JSONObject;

/**
 * 用户操作
 * @author RoarWolf
 *
 */
@RequestMapping("/applet")
@RestController
public class AppletUserController {
	
	@Autowired
	private GeneralDetailService generalDetailService;

	@PostMapping("/getopenid")
	public 	Object getopenid(String code) {
		try {
			JSONObject userOpenid = WeixinUtil.getUserOpenid(code);
			if (!userOpenid.has("openid")) {
				return CommUtil.responseBuild(1001, "code is error", userOpenid.toString());
			}
			System.out.println("===" + userOpenid.toString());
			return CommUtil.responseBuild(1000, "success", userOpenid.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return CommUtil.responseBuild(1002, "request fail: system error", null);
		}
	}
	
	@PostMapping("/getWalletRecord")
	public 	Object getWalletRecord(Integer userid, Integer startnum) {
		return generalDetailService.selectGenWalletDetailByUid(userid, startnum);
	}
}
