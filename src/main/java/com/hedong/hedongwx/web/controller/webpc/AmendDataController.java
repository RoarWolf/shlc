package com.hedong.hedongwx.web.controller.webpc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.service.AmendDataService;
import com.hedong.hedongwx.service.CommonMethodService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.StatisticsService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.TradeRecordService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

/**
 * ORIGGIN  ANEW AFRESH
 * @Description： 
 * @author  origin  创建时间：   2020年6月12日 下午5:42:13  
 */
@Controller
@RequestMapping(value = "/allowamendInfo")
public class AmendDataController {
	
	@Autowired
	private AmendDataService amendDataService;
	
	@Autowired
	private TemplateService templateService;

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private CommonMethodService commonMethodService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@RequestMapping(value="/doWeChatRefund")
	@ResponseBody
    public Object doWeChatRefund(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer orderid =  CommUtil.toInteger(maparam.get("orderid"));
			Integer ordersource =  CommUtil.toInteger(maparam.get("ordersource"));
			Integer refundtype =  CommUtil.toInteger(maparam.get("refundtype"));
			//Integer orderid, Integer ordersource, Integer refundtype
			Map<String, Object> result = commonMethodService.doWeChatRefund(orderid, ordersource, refundtype);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return datamap;
		}
	}
	
	
	/**
	 * @Description：模板处理
	 * @author： origin Settlement
	 */
	@RequestMapping(value="/additionAndEditTemp")
	@ResponseBody
    public Object additionAndEditTemp(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  templateService.additionAndEditTemp(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * @Description：结算全部汇总
	 * @author： origin Settlement
	 */
	@RequestMapping(value="/calculateCollect")
	@ResponseBody
    public Object calculateCollect(HttpServletRequest request, HttpServletResponse response) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result =  amendDataService.calculateCollect(request);
		}
		return JSON.toJSON(result);
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
			result =  amendDataService.calculateTotalCollect(request);
		}
		return JSON.toJSON(result);
	}
	
	/**
	 * @method_name: dealerIncomeInfo
	 * @Description: 充值、设备分开收益计算商户汇总信息
	 * @param request  response
	 * @Author: origin  创建时间:2020年8月12日 下午3:59:56
	 * @common:  hd_collectdealerincome
	 */
	@RequestMapping(value="/dealerIncomeInfo")
	@ResponseBody
	public Map<String, Object> dealerIncomeInfo(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultData = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", resultData);
				return resultData;
			}
			String time = CommUtil.toString(maparam.get("time"));
			Integer datanum = CommUtil.toInteger(maparam.get("datanum"));
			//商户收益汇总
			for(int i = datanum; i >= 0; i--){
				//商户收益汇总
				System.out.println("【dealerIncomeInfo】汇总开始时间        "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
				String startTimes = StringUtil.getnumterday("yyyy-MM-dd", time, i);
				System.out.println("【dealerIncomeInfo】汇总时间       "+startTimes);
				
				List<Map<String, Object>> resultinfo = amendDataService.dealerIncomeInfo(startTimes);
				
				System.out.println("【dealerIncomeInfo】时间       "+startTimes+"  号，商户数据汇总完成   ");
				System.out.println("【dealerIncomeInfo】汇总结束时间       "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			}
			return CommUtil.responseBuild(200, "定时汇总每日金额情况完成", null);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuild(201, "定时汇总每日金额情况失败", null);
		}
	}
	
	/**
	 * @method_name: dealerIncomeData
	 * @Description: 收益计算商户汇总信息
	 * @param request  response
	 * @Author: origin  创建时间:2020年8月12日 下午3:59:01
	 * @common:  hd_codestatistics_rzc
	 */
	@RequestMapping(value="/dealerIncomeData")
	@ResponseBody
	public Map<String, Object> dealerIncomeData(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultData = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", resultData);
				return resultData;
			}
			String time = CommUtil.toString(maparam.get("time"));
			Integer datanum = CommUtil.toInteger(maparam.get("datanum"));
			//商户收益汇总
			for(int i = datanum; i >= 0; i--){
				//商户收益汇总
				System.out.println("【dealerIncomeData】汇总开始时间       "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
				String startTimes = StringUtil.getnumterday("yyyy-MM-dd", time, i);
				System.out.println("【dealerIncomeData】汇总时间       "+startTimes);
				
				List<Map<String, Object>> resultinfo = amendDataService.dealerIncomeData(startTimes);
				
				System.out.println("【dealerIncomeData】时间       "+startTimes+"  号，商户数据汇总完成   ");
				System.out.println("【dealerIncomeData】汇总结束时间       "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			}
			return CommUtil.responseBuild(200, "定时汇总每日金额情况完成", null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
			return CommUtil.responseBuild(201, "定时汇总每日金额情况失败", null);
		}
	}
	
	
	
	
	@RequestMapping(value="/dealerIncomeCollect")
	@ResponseBody
	public Map<String, Object> dealerIncomeCollect(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultData = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", resultData);
				return resultData;
			}
			String time = CommUtil.toString(maparam.get("time"));
			String startTime = time;
			if(startTime==null)startTime = "2020-05-31 00:00:00";
			String endTime = "2020-04-16 00:00:00";
			int diffe = differentDay(endTime, startTime);
			int datanum = 0;
			//商户收益汇总
			for(int i=diffe;i>=0;i--){
				datanum = i;
				String startTimes = StringUtil.getnumterday("yyyy-MM-dd", startTime, datanum);
				statisticsService.dealerIncomeCollect(startTimes);
				System.out.println("时间       "+startTimes+"  号，商户数据汇总完成   ");
			}
			
			return CommUtil.responseBuildInfo(200, "定时汇总每日金额情况完成", null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
			return CommUtil.responseBuildInfo(201, "定时汇总每日金额情况失败", null);
		}
	}
	
	 /**
     * 时间比较（比较制定两个时间的差）
     * @author  origin          
     * @version 创建时间：2019年3月5日  下午2:30:37
     */
	public static Integer differentDay(String startTime, String endTime){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
		try {
			startDate = sdf.parse(startTime);
			endDate=sdf.parse(endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
        Calendar stcalen = Calendar.getInstance();
        stcalen.setTime(startDate);
        Calendar encalen = Calendar.getInstance();
        encalen.setTime(endDate);
        int strday= stcalen.get(Calendar.DAY_OF_YEAR);//当前年的第几天     
        int endday = encalen.get(Calendar.DAY_OF_YEAR);
        int stryear = stcalen.get(Calendar.YEAR);//当前年 
        int endyear = encalen.get(Calendar.YEAR);
        int timeDistance = 0 ;
        if(stryear != endyear){//不同年
            for(int i=stryear; i<endyear; i ++){
                if(i%4==0 && i%100!=0 || i%400==0){//闰年
                    timeDistance += 366;
                }else{//不是闰年
                    timeDistance += 365;
                }
            }
            timeDistance += endday-strday;
            return timeDistance;
        }else{//同年
            timeDistance = endday-strday;
            return timeDistance;
        }
    }
	
	/**
	 * @method_name: inquireDealerIncomeData
	 * @Description: 查询商户收益汇总信息（设备收益与充值收益分开记录）
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月13日 下午5:50:46
	 * @common:
	 */
	@RequestMapping(value="/inquireDealerIncomeData")
	@ResponseBody
	public Object inquireDealerIncomeData(HttpServletRequest request) {
		Object result = null;
		if(CommonConfig.isExistSessionUser(request)){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{
			result = amendDataService.inquireDealerIncomeData(request);
		}
		return result;
	}
	
	//==================================================================================================
	//扫码充电需要信息
	//==================================================================================================
	/*
	 0：获取微信返回的code信息与传过来的existuser存在信息
	 1:判断设备号是否存在
	 2：根据设备号获取设备相关信息
	         设备名、设备线状态、设备绑定状态、硬件版本（version）、软件版本、模板id、测试次数、到期时间、
	         小区名字、小区地址、小区id
	         设备绑定的商户、  设备绑定的商户的电话、  设备绑定的商户的服务电话、  设备绑定的商户的oppenid、  设备绑定的商户的id
	         绑定的商户是否为特约商户
	         扫码用户所对于扫码设备所在平台相对应的oppenid
	         根据对应信息获取相对应的用户信息（主要在于设备如果属于特约商户）
	         用户id、商户ad、用户充值金额、用户赠送金额、iswallet
	        品牌(brandname)，模板服务电话
	   
	 3：获取设备到期时间，判断是否该缴费了
	 4：检验用户信息，如果数据库中没有，则添加、如果存在则处理他（）
	 5：获取模板（主模板与子模板）信息和对应的信息
	 6：判断该用户是否关注公众号
	 7：判断端口信息状态
	 
	// 检查用户和清除     checkUserAndDispose(openid,accesstoken,equcode);
	 */
	//==================================================================================================
}
