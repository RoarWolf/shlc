package com.hedong.hedongwx.web.controller.applet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.ChargeRecordService;
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
	@Autowired
	private ChargeRecordService chargeService;
	@Autowired
	private AreaService areaService;

	/**
	 * 获取用户openid
	 * @param code
	 * @return
	 */
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
	
	/**
	 * 查询用户钱包记录
	 * @param userid
	 * @param startnum
	 * @return
	 */
	@PostMapping("/getWalletRecord")
	public 	Object getWalletRecord(Integer userid, Integer startnum) {
		return generalDetailService.selectGenWalletDetailByUid(userid, startnum);
	}
	
	/**
	 * 查询用户订单记录
	 * @param userid
	 * @param startnum
	 * @param status
	 * @return
	 */
	@PostMapping("/getChargeRecord")
	public 	Object getChargeRecord(Integer userid, Integer startnum, Integer status) {
		return chargeService.queryChargeRecord(userid, status, startnum);
	}
	
	/**
	 * 查询用户订单信息
	 * @param userid
	 * @return
	 */
	@PostMapping("/getChargeRecordInfo")
	public 	Object getChargeRecordInfo(Integer orderid) {
		return chargeService.queryChargeRecordInfo(orderid);
	}
	
	/**
	 * 查询用户订单信息
	 * @param userid
	 * @return
	 */
	@PostMapping("/getAreaRecently")
	public 	Object getAreaRecently(Double lon, Double lat, Double distance, Integer startnum) {
		return areaService.queryAreaRecently(lon, lat, distance, startnum);
	}
}
