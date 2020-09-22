package com.hedong.hedongwx.web.controller.computerpc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Controller
@RequestMapping(value="/pcHousing")
public class HousingManageController {//区域(小区管理)
	
	@Autowired
	private AreaService areaService;
	@Autowired
	private UserService userService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private HttpServletRequest request;
	 
	//通过Parameters查询小区位置所有信息   /pcHousing/housingAdress
	@RequestMapping(value = { "/housingAdress" })
	public String selectByParame(HttpServletRequest request, HttpServletResponse response, Model model){
		PageUtils<Parameters> pageBean = areaService.selectByParame(request);
		model.addAttribute("name", request.getParameter("name"));
		model.addAttribute("address", request.getParameter("address"));
		model.addAttribute("dealer", request.getParameter("dealer"));
		model.addAttribute("phoneNum", request.getParameter("phoneNum"));
		model.addAttribute("manarealname", request.getParameter("manarealname"));
		model.addAttribute("manaphonenum", request.getParameter("manaphonenum"));
		model.addAttribute("startTime", request.getParameter("startTime"));
		model.addAttribute("endTime", request.getParameter("endTime"));
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("traderecord", pageBean.getListMap());
		return "computer/chargestation";
	}
	
	/**
	 * 通过小区id 删除小区信息、并同步删除小区名下合伙人信息
	 * @param request, response, model
	 * @return
	 */
	@RequestMapping(value = { "/deleteByArea" })
	@ResponseBody
	public Object deleteByArea(Integer id, Model model){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			areaService.deleteAreaByAid(id);
			equipmentService.updateEquAidByAid(id);
			map.put("code", 200);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", 403);
		}
		return map;
	}
	
	/**
	 * @Description： 根据实体类条件查询小区信息
	 * @author： origin   
	 */
	@RequestMapping("/selectarealist")
	@ResponseBody
	public List<Area> selectAreaList( Integer merid) {
		//CommonConfig.getAdminReq(request);
		User admin = (User) request.getSession().getAttribute("admin");
		Area area = new Area();
		if(admin.getRank() != 0) area.setMerid(merid);
		List<Area>  arealist =  areaService.selectAreaList(area);
		return arealist;
	}
	
	/**
	 * 根据id查询单个area数据信息
	 * @param id, model
	 * @return
	 */
	@RequestMapping("/selectByIdArea")
	@ResponseBody
	public Area selectByIdArea(Integer id, Model model) {
		Area arealist =  areaService.selectByIdArea(id);
		return arealist;
	}
	
	/**
	 * 通过实体类 Parameter 传入条件 修改 设备地点（小区）的信息
	 * @param request, response, model
	 * @return
	 */
	@RequestMapping(value = { "/updateByParame" })
	@ResponseBody
	public int updateByParame(HttpServletRequest request, HttpServletResponse response, Model model){
		User deruser = existMobile(request.getParameter("uphonenum"));//商户电话
		User manuser = existMobile(request.getParameter("managephone"));//商户电话
		if(deruser==null) return 2;
		if(manuser==null) return 3;
		Area area = new Area();
		area.setId(StringUtil.getIntString(request.getParameter("id")));
		area.setName(request.getParameter("name"));
		//area.setTempid(tempid);
		area.setAddress(request.getParameter("address"));
		area.setMerid(deruser.getId());
		area.setManid(manuser.getId());
		area.setUpdateTime(new Date());
		int areanum = areaService.updateByArea(area);
		return areanum;
	}
	
	/**
	 * 判断用户表中是否存在该用户
	 * @return
	 * @throws Exception 
	 */
	public User existMobile(String mobile) {
		User user =  userService.existAdmin(mobile);
		if(user==null) return null;
		return user;
	}
	
}


