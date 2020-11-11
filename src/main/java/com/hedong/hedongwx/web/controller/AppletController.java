package com.hedong.hedongwx.web.controller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.AppletUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.WeiXinConfigParam;
import com.hedong.hedongwx.utils.WeixinUtil;
import com.hedong.hedongwx.utils.XMLUtil;

import net.sf.json.JSONObject;

@RequestMapping("/applet")
@Controller
public class AppletController {

	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private UserService userService;
	@Autowired
	private TemplateService templateService;
	private final Logger logger = LoggerFactory.getLogger(AppletController.class);

	/**
	 * 连接设备，并存数据库
	 * @param deviceId
	 * @param systemType
	 * @param deviceUuid
	 * @return
	 */
	@RequestMapping("/connectDevice")
	@ResponseBody
	public String connectDevice(String deviceId, int systemType, String deviceUuid) {
		try {
			if (systemType == 1) {
				String code = equipmentService.selectEndDeviceNum(2);
				Equipment equipment2 = equipmentService.selectBluetoothMac(code);
				if (equipment2.getDeviceIdIos() == null) {
					Equipment equipment = new Equipment();
					equipment.setCode(code);
					equipment.setDeviceIdIos(deviceId);
					equipmentService.updateEquipment(equipment);
					return "1";
				}
				return "0";
			}

			Equipment bluetoothExist = equipmentService.selectBluetoothExist(deviceId);
			if (bluetoothExist != null) {
				return "0";
			}
			String endCode = equipmentService.selectEndDeviceNum(null);
			String code = "000001";
			Equipment equipment = new Equipment();
			equipment.setCode(code);
			equipment.setDeviceIdAndroid(deviceId);
			equipment.setDeviceType(2);
			equipment.setHardversion("03");
			equipment.setHardversionnum("02");
			equipment.setSoftversionnum("00");
			equipment.setCreateTime(new Date());
			equipmentService.addEquipment(equipment);
			return "1";
		} catch (Exception e) {
			return "0" + e.getMessage();
		}
	}

	/**
	 * 获取设备id
	 * @param code
	 * @param systemType
	 * @return
	 */
	@RequestMapping("/findDeviceId")
	@ResponseBody
	public String findDeviceId(String code, int systemType) {
		Equipment equipment = equipmentService.selectBluetoothMac(code);
		if (equipment != null) {
			if (systemType == 1) {
				return equipment.getDeviceIdIos();
			} else if (systemType == 2) {
				return equipment.getDeviceIdAndroid();
			}
		}
		return "0";
	}

	@RequestMapping("/getUnionid")
	@ResponseBody
	public String getUnionid(String code) {
		try {
			String str = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WeiXinConfigParam.SMALLAPPID
					+ "&secret=" + WeiXinConfigParam.SMALLAPPSECRET + "&code=" + code
					+ "&grant_type=authorization_code";
			JSONObject doGetStr = WeixinUtil.doGetStr(str);
			if (!doGetStr.has("openid")) {
				return "erroruser";
			}
			System.out.println("===" + doGetStr.toString());
			String openid = doGetStr.getString("openid");
			String accesstoken = doGetStr.getString("access_token");

			String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accesstoken + "&openid="
					+ openid + "&lang=zh_CN";
			JSONObject doGetStr1 = WeixinUtil.doGetStr(getUserInfoUrl);
			System.out.println("---" + doGetStr1.toString());
			System.out.println("unionid===" + doGetStr1.getString("unionid"));
			return doGetStr1.getString("unionid");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "666";
		}
	}
	
	/**
	 * 查看user中是否存有小程序openid
	 * @param unionid
	 * @param openid
	 * @return
	 */
	@RequestMapping("/addOrUpdateUser")
	@ResponseBody
	public String addOrUpdateUser(String unionid,String openid) {
		if (unionid == null || openid == null) {
			return "0";
		}
		User user = userService.getUserByUnionid(unionid);
		if (user == null) {
			User appletUser = new User();
			appletUser.setUnionid(unionid);
			appletUser.setOpenidApplet(openid);
			appletUser.setCreateTime(new Date());
			userService.addUser(appletUser);
		} else {
			String openidApplet = user.getOpenidApplet();
			if (openidApplet == null || "".equals(openidApplet)) {
				User appletUser = new User();
				appletUser.setId(user.getId());
				appletUser.setOpenidApplet(openid);
				userService.updateUserById(appletUser);
			}
		}
		return "1"; 
	}

	@RequestMapping("/wolfDecode")
	@ResponseBody
	public String wolfDecode(String encryptedData, String iv, String code) {
		System.out.println("encryptedData： " + encryptedData);
		System.out.println("iv： " + iv);
		System.out.println("code： " + code);
		Map<String, String> map = new HashMap<>();
		// 小程序唯一标识 (在微信小程序管理后台获取)
		String wxspAppid = WeiXinConfigParam.SMALLAPPID;
		// 小程序的 app secret (在微信小程序管理后台获取)
		String wxspSecret = WeiXinConfigParam.SMALLAPPSECRET;
		// 授权（必填）
		String grant_type = "authorization_code";
		// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
		// 请求参数
		String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type="
				+ grant_type;
		// 发送请求
		String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
		System.out.println("---" + sr);
		// 解析相应内容（转换成json对象）
		try {
			Map<String,String> json = XMLUtil.doXMLParse(sr);
			// 获取会话密钥（session_key）
			String session_key = json.get("session_key");
			System.out.println("session_key: " + session_key);
			// 用户的唯一标识（openid）
			String openid = json.get("openid");
			map.put("openid", openid);
			// 2、对encryptedData加密数据进行AES解密
			String status = null;
			String result = AppletUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
			PrintWriter out = response.getWriter();
			if (null != result && result.length() > 0) {
				response.setCharacterEncoding("utf-8");
				JSONObject userInfoJSON = JSONObject.fromObject(result);
				map.put("unionid", userInfoJSON.getString("unionId"));
				status = "1";
			} else {
				map.put("openid", "");
				status = "0";
			}
			map.put("status", status);
			out.print(JSONObject.fromObject(map));
			System.out.println("给前端的map：" + map.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/getTemps")
	@ResponseBody
	public List<TemplateSon> getTemps(String code) {
		List<TemplateSon> equSonTem = templateService.getEquSonTem(code);
		if (equSonTem == null || equSonTem.size() == 0) {
			equSonTem = templateService.getSonTemplateLists(2);
		}
		return equSonTem;
	}
	
	@RequestMapping("/addLiveness")
	@ResponseBody
	public String addLiveness(String code,String openid) {
		equipmentService.updateEquLivenumByCode(code);
		return "1";
	}
	
	@RequestMapping("/queryCodeBindPhone")
	@ResponseBody
	public String queryCodeBindPhone(String code) {
		return equipmentService.queryCodeBindPhone(code);
	}
}
