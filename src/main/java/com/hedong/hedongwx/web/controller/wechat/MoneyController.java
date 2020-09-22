package com.hedong.hedongwx.web.controller.wechat;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hedong.hedongwx.entity.Money;
import com.hedong.hedongwx.service.MoneyService;
import com.hedong.hedongwx.utils.CommUtil;

@Controller
@RequestMapping({ "/money" })
public class MoneyController {
	
	
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private HttpServletRequest request;
	
	//充电记录(多条记录查询)
	@RequestMapping({"/payMoneyRecord"})
	public String chaRecordList( Integer uid, Model model) {
		List<Money> payMoneyRecord = moneyService.MoneyRecordByid(uid);
		model.addAttribute("payMoneyRecord", payMoneyRecord);
		return "record/payMoneyRecord";
	}
	
	//充电记录详情(单条记录查询)
	@RequestMapping({"/payMoneyinfo"})
	public String chaRecordListOne( Integer id, Model model) {
		Money payMoneyinfo = moneyService.payMoneyinfo(CommUtil.toInteger(id));
		payMoneyinfo = payMoneyinfo == null ? new Money() :payMoneyinfo;
		String rank = request.getParameter("rank");
		System.out.println("rank======" + rank);
		model.addAttribute("rank", rank);
		model.addAttribute("payMoneyinfo", payMoneyinfo);
		return "record/payMoneyinfo";
	}
	
	
}
