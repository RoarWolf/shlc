package com.hedong.hedongwx.web.controller.webpc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.FeescaleService;
import com.hedong.hedongwx.service.StatisticsService;
import com.hedong.hedongwx.service.SystemSetService;
import com.hedong.hedongwx.utils.CommUtil;

/**
 * @Description： 
 * @author  origin  创建时间：   2019年11月7日 上午11:30:26  
 */
@Controller
@RequestMapping(value = "/systemSetting")
public class SystemSettingController {
	
	
	@Autowired
	private SystemSetService  systemSetService;
	@Autowired
	private FeescaleService feescaleService;
	@Autowired
	private StatisticsService statisticsService;
	
	/**
	 * @Description：根据指定天数，处理商户的消耗电量和消耗时间
	 * @author： origin
	 * @createTime：2020年4月16日下午4:21:07
	 */
	@RequestMapping(value="/disposeDealerElect")
	@ResponseBody
    public Object disposeDealerElect(HttpServletRequest request, HttpServletResponse response) {
		try {
			Integer past = CommUtil.toInteger(request.getParameter("past"));
			Integer type = CommUtil.toInteger(request.getParameter("type"));
			Integer cutoff = CommUtil.toInteger(request.getParameter("cutoff"));
			if(type.equals(1)){
				String time = CommUtil.getPastDate(past);
				statisticsService.dealerupdataquantity(time);
			} else if (type.equals(2)){
				for(int i=past; i>=cutoff; i--){
					String time = CommUtil.getPastDate(i);
					statisticsService.dealerupdataquantity(time);
				}
			} else if (type.equals(3)){
				for(int i=1; i<=past; i++){
					String time = CommUtil.getPastDate(i);
					statisticsService.dealerupdataquantity(time);
				}
			}
			return "succeed";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @Description：根据商户id，type类型[1：商户收益  2：单个设备收益处理 3:商户所有设备处理] pastday:之前多少天开始计算到前一天  devicenum:设备号
	 * 				判断处理商户的汇总数据
	 * @author： origin
	 * @createTime：2020年4月16日下午4:21:07
	 */
	@RequestMapping(value="/selfDynamicCollect")
	@ResponseBody
    public Object selfDynamicCollect(Integer merid, Integer type, Integer pastday, String devicenum) {
		try {
			Object result = systemSetService.selfDynamicCollect(merid, type, pastday, devicenum);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @Description：根据商户id，判断处理商户近七天的汇总数据
	 * @author： origin
	 * @createTime：2020年4月16日下午4:21:07
	 */
	@RequestMapping(value="/selfmotionCollect")
	@ResponseBody
    public Object selfmotionCollect(HttpServletRequest request, HttpServletResponse response) {
		try {
			Object result = systemSetService.selfmotionCollect(request);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * separate
	 * @Description：系统模板
	 * @author： origin 
	 */
	@RequestMapping(value="/systemTemplateData")
	@ResponseBody
    public Object systemTemplateData(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  systemSetService.systemTemplateData(request);
		}
		return result;
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
		return result;
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
		return result;
	}
	
	/**
	 * 修改系统收费标准
	 * @param netMap
	 * @param blueMap
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/updateSystemFeescale")
	@ResponseBody
	public Object updateSystemFeescale(String netMap, String blueMap,HttpServletRequest request){
		Object result = null;
		User user=CommonConfig.getAdminReq(request);
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else if(user!=null && user.getRank()==0){
			Map<String,Map<String,Double>> net=new HashMap<String,Map<String,Double>>();
			Map<String, Map<String,Double>> blue=new HashMap<String,Map<String,Double>>();
			Map<String, Double> map=new HashMap<String,Double>();
			Map<String, Double> map2=new HashMap<String,Double>();
			Map netMapType = JSONObject.parseObject(netMap, Map.class);
			Map blueMapType = JSONObject.parseObject(blueMap, Map.class);
			
			Map netMap1=(Map) netMapType.get("00");
			Map blueMap1=(Map)blueMapType.get("01");
			Set<String> set=netMap1.keySet();
			Set<String> set1=blueMap1.keySet();
			for (String str : set) {
				map.put(str, CommUtil.toDouble(netMap1.get(str)));
			}
			for(String str1 :set1){
				map2.put(str1,CommUtil.toDouble(blueMap1.get(str1))); 
			}
			net.put("00", map);
			blue.put("01", map2);
			boolean success=feescaleService.updateSystemFeescale(net, blue);
			if (success) {
				result = CommUtil.responseBuild(200, "成功", "");
				return JSON.toJSON(result);
			} else {
				result = CommUtil.responseBuild(400, "失败", "");
				return JSON.toJSON(result);
			}
		}else{
			result=CommUtil.responseBuild(103, "无权修改","");
			return JSON.toJSON(result);
		}
		return user;
	}
}
