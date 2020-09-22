package com.hedong.hedongwx.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.UserService;

@Controller
@RequestMapping("/mui")
public class MuiController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/login")
	public String login() {
		return "mui/login";
	}
	
	@RequestMapping(value = "/reg")
	public String reg() {
		return "mui/reg";
	}

	@RequestMapping(value = "/loginverify", method = RequestMethod.GET)
	@ResponseBody
	public String loginverify(@RequestParam(value = "phoneNum") String phoneNum, @RequestParam(value = "pwd")String password) {
		User user = userService.getUserByPhoneNumAndPassword(phoneNum, password);
		request.getSession().setAttribute("user", user);
		if (user != null) {
			return "1";
		}
		return "0";
	}

	@RequestMapping(value = "/main")
	public String main(Model model) {
		User user = (User)request.getSession().getAttribute("user");
		model.addAttribute("phoneNum", user.getPhoneNum());
		return "mui/main";
	}

	@RequestMapping(value = "/setting")
	public String setting() {
		return "mui/setting";
	}
}
