package com.hedong.hedongwx.web.controller.computerpc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.service.StatisticsService;
import com.hedong.hedongwx.service.TimingCalculateService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.StringUtil;

/**
 * @Description： 定时计算控制类
 * @author  origin
 */
@Controller
@RequestMapping({ "/allowcalculate" })
public class TimingCalculateController {
	
	@Autowired
	private TimingCalculateService timingCalculateService;
	@Autowired
	private StatisticsService statisticsService;
	

	
/*********************************************************************************************************/	
	
	@RequestMapping(value="/dealerIncomeInfoTask")
	@ResponseBody
	public Object dealerIncomeInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			String type =  CommUtil.toString(maparam.get("type"));
			String time =  CommUtil.toString(maparam.get("time"));
			Integer datanum = CommUtil.toInteger(maparam.get("datanum"));
			Integer paysource = CommUtil.toInteger(type);
			//商户收益汇总
			for(int i = datanum; i >= 0; i--){
				System.out.println("【calculateTotalCollect】汇总开始时间       "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
				String timeday = StringUtil.getnumterday("yyyy-MM-dd", time, i);
				if(paysource==1){//商户收益汇总
					System.out.println("【收益计算：商户汇总 时间】：      "+time +"  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
					List<Map<String, Object>> resultinfo = timingCalculateService.dealerIncomeInfo(timeday);
					System.out.println("【收益计算计算：商户汇总结束时间 】：      "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
				}else if(paysource==2){//设备收益汇总
					System.out.println("【收益计算计算：设备汇总 时间】：      "+time +"  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
					List<Map<String, Object>> resultinfo = timingCalculateService.dealerIncomeInfo(timeday);
					System.out.println("【收益计算计算：设备汇总结束时间 】：      "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
				}else if(paysource==3){//小区
					System.out.println("【收益计算计算：小区汇总 时间】：      "+time +"  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
					List<Map<String, Object>> resultinfo = timingCalculateService.dealerIncomeInfo(timeday);
					System.out.println("【收益计算计算：小区汇总结束时间 】：      "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
				}else {//所有收益汇总
					System.out.println("【收益计算计算：设备汇总 时间】：      "+time +"  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
					List<Map<String, Object>> resultdealer = timingCalculateService.dealerIncomeInfo(timeday);
					System.out.println("【收益计算计算：设备汇总 时间】：      "+time +"  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
					List<Map<String, Object>> resultdevice = timingCalculateService.dealerIncomeInfo(timeday);
					System.out.println("【收益计算计算：设备汇总 时间】：      "+time +"  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
					List<Map<String, Object>> resultarea = timingCalculateService.dealerIncomeInfo(timeday);
				}
			}
			System.out.println("方法汇总结束时间       "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			return CommUtil.responseBuildInfo(200, "结算完成", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
/*********************************************************************************************************/	
	
	
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
			//result =  amendDataService.calculateTotalCollect(request);
		}
		return JSON.toJSON(result);
	}
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
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
}
