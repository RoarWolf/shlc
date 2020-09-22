package com.hedong.hedongwx.web.controller.computerpc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.service.OperateRecordService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.PageUtils;

/**
 * @author  origin
 * 创建时间：   2019年5月24日 下午5:37:40  
 */
@Controller
@RequestMapping(value = "/pcoperate")
public class OperaterecordController {
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private OperateRecordService operateRecordService;
	
	/**
	 * @Description： 
	 * @author： origin  
	 */
	@RequestMapping("/useroperatelog")
	public Object userOperateRecord(HttpServletRequest request, HttpServletResponse response, Model model){
//	String dealer,String nickname, String name, Integer type, String startTime, String endTime, 
//	String numPerPage, String currentPage, HttpServletRequest request, HttpServletResponse response,  Model model) {
		PageUtils<Parameters> pageBean = operateRecordService.userOperateRecord(request);
		model.addAttribute("dealer", CommUtil.toString(request.getParameter("dealer")));
		model.addAttribute("nickname", CommUtil.toString(request.getParameter("nickname")));
		model.addAttribute("name", CommUtil.toString(request.getParameter("name")));
		model.addAttribute("type", CommUtil.toInteger(request.getParameter("type")));
		model.addAttribute("startTime", CommUtil.toString(request.getParameter("startTime")));
		model.addAttribute("endTime", CommUtil.toString(request.getParameter("startTime")));
	
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("operatetrcon", pageBean.getListMap());
		return "computer/useroperaterecord";
	}
	
	
	
	
}
