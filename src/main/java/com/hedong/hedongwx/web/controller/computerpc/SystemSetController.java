package com.hedong.hedongwx.web.controller.computerpc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.service.BasicsService;
import com.hedong.hedongwx.service.CollectStatisticsService;
import com.hedong.hedongwx.service.SystemSetService;
import com.hedong.hedongwx.utils.CommUtil;

/**
 * @Description： 系统设置控制类
 * @author  origin  创建时间：   2019年9月11日 下午4:14:45  
 */
@Controller
@RequestMapping({ "/system" })
public class SystemSetController {
	
	@Autowired
	private static Logger logger = LoggerFactory.getLogger(SystemSetController.class);
	@Autowired
	private BasicsService basicsService;
	@Autowired
	private SystemSetService systemSetService;
	@Autowired
	private CollectStatisticsService collectStatisticsService;
	
	
	/**
	 * @Description： 跳转结算处理商户页面
	 * @author： origin   2019年9月29日 下午3:43:43
	 */
	@RequestMapping(value="/collectDealerElect")
	public Object collectDealerElect(Model model){
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "computer/settingConsumet";
	}
	
	
	@RequestMapping(value="/dealerIncomeCollect")
	public void dealerIncomeCollect( String time, Model model){
		try {
			
			collectStatisticsService.dealerIncomeCollect( time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/textgetServicePhone")
	public void getServicePhone(Model model){
		try {
			String val = "";
			Integer type = 0;
			Integer tempid = 0;
			Integer areaid = 0;
			Integer dealid = 0;
			systemSetService.getServicePhone( tempid, areaid, dealid);
			systemSetService.getServeTemp( val, type, areaid, dealid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description： 跳转结算处理页面
	 * @author： origin   2019年9月29日 下午3:43:43
	 */
	@RequestMapping(value="/computedata")
	public Object computedata(Model model){
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "computer/incomeSettlement";
	}	
	
	/**
	 * separate
	 * @Description：系统结算全部汇总
	 * @author： origin Settlement
	 */
	@RequestMapping(value="/calculateTotalCollect")
	@ResponseBody
    public Object calculateTotalCollect(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  systemSetService.calculateTotalCollect(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * separate
	 * @Description：系统结算个人汇总
	 * @author： origin Settlement
	 */
	@RequestMapping(value="/calculateAloneCollect")
	@ResponseBody
    public Object calculateAloneCollect(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  systemSetService.calculateAloneCollect(request);
		}
		return JSON.toJSON(result);
	}
	
	
}
