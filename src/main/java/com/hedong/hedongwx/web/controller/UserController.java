package com.hedong.hedongwx.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserEquipmentService userEquipmentService;
	@Autowired
	private EquipmentService equipmentService;

	/**
	 * 用户列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		List<User> userList = userService.getUserList();
		model.addAttribute("userList", userList);
		return "user/userList";
	}

	/**
	 * 进入添加用户页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String addUser() {
		return "user/input";
	}

	/**
	 * 添加用户
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/addSucceed", method = RequestMethod.POST)
	public String addSucceed(User user) {
		userService.addUser(user);
		return "redirect:list";

	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete")
	public String deleteUser(@RequestParam("id") Integer id) {
		userService.deleteUserById(id);
		return "redirect:list";
	}

	@RequestMapping(value = "/findAllEquipment")
	public String findAllEquipment(@RequestParam(value = "id") Integer id, Model model) {
		if (id != null) {
			List<Equipment> equipmentList = new ArrayList<>();
			List<UserEquipment> ueid = userEquipmentService.getUserEquipmentById(id);
			for (UserEquipment userEquipment : ueid) {
				Equipment equipment = equipmentService.getEquipmentById(userEquipment.getEquipmentCode());
				equipmentList.add(equipment);
			}
			model.addAttribute("equipmentList", equipmentList);
			return "equipment/equipmentList";
		}
		return "index";
	}

	@RequestMapping(value = "/holdem")
	public String holdem() {
		return "/user/holdem";
	}
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	// -引导用户进入授权页面同意授权，获取code 
	@RequestMapping({ "/loginWeChat" })
	public Object loginWeChat(){
		String str = null;
		try {
			String url = URLEncoder.encode(CommonConfig.ZIZHUCHARGE+"/user/managewithdraw", "utf-8");
			str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx3debe4a9c562c52a&redirect_uri=" + url + "&response_type=code&scope=snsapi_userinfo&state=13589#wechat_redirect";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "redirect:" + str;
	}

}
