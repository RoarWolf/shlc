package com.hedong.hedongwx.web.controller.hduser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.UserService;

@Controller
@RequestMapping(value = "/agency")//代理商管理
public class AgencyController {
	
	@Autowired
	private UserService userService;

	/**
	 * 进入首页
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index() {
		return "agency/index";
	}
	
	/**
	 * 查看所有绑定设备
	 * @return
	 */
	@RequestMapping(value = "/eqipmentall")
	public String eqipmentall() {
		return "equipment/equipmentList";
	}
	
	/**
	 * 查看订单详情
	 * @return
	 */
	@RequestMapping(value = "/orderdetail")
	public String orderdetail() {
		return "agency/orderdetail";
	}
	
	/**
	 * 添加绑定设备
	 * @return
	 */
	@RequestMapping(value = "/addeqipment")
	public String addeqipment() {
		return "agency/index";
	}
	
	/**
	 * 提现
	 * @return
	 */
	@RequestMapping(value = "/withdraw")
	public String withdraw() {
		return "agency/withdraw";
	}

	/**
	 * 进入注册页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/regist")
	public String regist(Model model) {
		model.addAttribute("rank", 3);
		return "agency/regist";
	}
	
	/**
	 * 注册成功
	 * @return
	 */
	@RequestMapping(value = "/save")
	public String save(User user) {
		userService.addUser(user);
		return "agency/index";
	}
}
