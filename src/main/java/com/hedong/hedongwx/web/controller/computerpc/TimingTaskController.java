package com.hedong.hedongwx.web.controller.computerpc;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.Statistics;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.StatisticsService;
import com.hedong.hedongwx.service.TimingCalculateService;
import com.hedong.hedongwx.service.WithdrawService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.StringUtil;

/**
 * @Description： 定时任务控制类
 * @author  origin 
 */
@Controller
@EnableScheduling
public class TimingTaskController {
	
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private WithdrawService withdrawService;
	@Autowired
	private TimingCalculateService timingCalculateService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	/**
	 * @method_name: dealerIncomeInfo
	 * @Description: 充值、设备收益分开计算商户汇总信息
	 * @param request  response
	 * @Author: origin  创建时间:2020年8月12日 下午3:59:56
	 * @common:  hd_collectdealerincome
	 */
	@RequestMapping(value="/dealerIncomeInfoTask")
	@ResponseBody
	public Object dealerIncomeInfo() {
		try {
			String time = StringUtil.getPastDate(1);
			System.out.println("【充值、设备收益分开计算商户汇总 时间】：      "+time +"  "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			List<Map<String, Object>> resultinfo = timingCalculateService.dealerIncomeInfo(time);
			System.out.println("【充值、设备收益分开计算商户汇总 时间  结束】：      "+CommUtil.toDateTime("yyyy-MM-dd HH:mm:ss"));
			return CommUtil.responseBuildInfo(200, "收益完成", null);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(201, "收益失败", null);
		}
	}
	
	/**
	 * @Description： 商户收益推送
	 * @author： origin    	
	 * //ORIGIN  ToUpload	商户收益推送   
	 */
//	@Scheduled(cron = "00 30 09 * * *")
	@RequestMapping("/dealerIncomePushTask")
	@ResponseBody
    public Object dealerIncomePush() {
		try {
			System.out.println("开始    商户收益推送  time  "+StringUtil.toDateTime());
			statisticsService.dealerIncomePush();
			System.out.println("结束   商户收益推送    time   "+StringUtil.toDateTime());
			return CommUtil.responseBuildInfo(200, "商户收益推送完成", null);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(201, "商户收益推送失败", null);
		}
	}
	
	/**
	 * @Description： 定时汇总每日金额情况
	 * @author： origin       
	 */
//	@Scheduled(cron = "00 00 03 * * *")
	@RequestMapping("/dealerIncomeCollectTask")
	@ResponseBody
    public Object dealerIncomeCollect() {
		try {
			String time = StringUtil.getPastDate(1);
			//商户收益汇总
			System.out.println("开始    "+"    time   "+StringUtil.toDateTime());
			statisticsService.dealerIncomeCollect(time);
			System.out.println("结束    "+"    time   "+StringUtil.toDateTime());
			return CommUtil.responseBuildInfo(200, "定时汇总每日金额情况完成", null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
			return CommUtil.responseBuildInfo(201, "定时汇总每日金额情况失败", null);
		}
	}
	
	/**
	 * @Description： 定时汇总每日设备金额情况
	 * @author： origin       
	 */
//	@Scheduled(cron = "00 00 03 * * *")
	@RequestMapping("/codeTimingcollectmoneyTask")
	@ResponseBody
    public Object codeTimingcollectmoney() {
		try {
			String time = StringUtil.getPastDate(1);
			//设备交易定时汇总每日金额情况
			System.out.println("开始     time  "+StringUtil.toDateTime());
			statisticsService.devivceTimingCollect(time);
			System.out.println("结束     time  "+StringUtil.toDateTime());
			return CommUtil.responseBuildInfo(200, "定时汇总每日设备金额情况完成", null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
			return CommUtil.responseBuildInfo(201, "定时汇总每日设备金额情况失败", null);
		}
	}
	
	/**
	 * @Description：设备定时更新电量、时间
	 * @author： origin       
	 */
//	@Scheduled(cron = "00 00 03 * * *")
	@RequestMapping("/dealerupdataquantityTask")
	@ResponseBody
    public Object dealerupdataquantity() {
		try {
			String time = StringUtil.getPastDate(2);
			//设备定时更新电量、时间
			System.out.println("开始     time  "+StringUtil.toDateTime());
			statisticsService.dealerupdataquantity(time);
			System.out.println("结束     time  "+StringUtil.toDateTime());
			return CommUtil.responseBuildInfo(200, "设备定时更新电量、时间完成", null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
			return CommUtil.responseBuildInfo(201, "设备定时更新电量、时间失败", null);
		}
	}
	
	/**
	 * @Description： 每日汇总收益信息
	 * @author： origin       
	 */
//	@Scheduled(cron = "00 00 03 * * *")
	@RequestMapping("/timingCollectMoneyTask")
	@ResponseBody
    public Object timingCollectMoney() {
		try {
			String time = StringUtil.getPastDate(1);
			Parameters parameters = new Parameters();
			parameters.setStartTime(time+" 00:00:00");
			parameters.setEndTime(time+" 23:59:59");
			Map<String, Object> timingcollect = statisticsService.timingCollectMoney(parameters);
			Map<String, Object> coinscollect = statisticsService.selectcoinsup(parameters);//根据条件汇总脉冲设备线下投币上传信息
			int wechatnum = CommUtil.toInteger(timingcollect.get("wechatnum"));
			int alipaynum = CommUtil.toInteger(timingcollect.get("alipaynum"));
			int wecappletnum = CommUtil.toInteger(timingcollect.get("wecappletnum"));
			int aliappletnum = CommUtil.toInteger(timingcollect.get("aliappletnum"));
			int unionpaynum = CommUtil.toInteger(timingcollect.get("unionpaynum"));
			
			int refwechatnum = CommUtil.toInteger(timingcollect.get("refwechatnum"));
			int refalipaynum = CommUtil.toInteger(timingcollect.get("refalipaynum"));
			int refwecappletnum = CommUtil.toInteger(timingcollect.get("refwecappletnum"));
			int refaliappletnum = CommUtil.toInteger(timingcollect.get("refaliappletnum"));
			int refunionpaynum = CommUtil.toInteger(timingcollect.get("refunionpaynum"));
			
			double wechatmoney = CommUtil.toDouble(timingcollect.get("wechatmoney"));
			double alipaymoney = CommUtil.toDouble(timingcollect.get("alipaymoney"));
			double wecappletmoney = CommUtil.toDouble(timingcollect.get("wecappletmoney"));
			double aliappletmoney = CommUtil.toDouble(timingcollect.get("aliappletmoney"));
			double unionpaymoney = CommUtil.toDouble(timingcollect.get("unionpaymoney"));
			
			double refwechatmoney = CommUtil.toDouble(timingcollect.get("refwechatmoney"));
			double refalipaymoney = CommUtil.toDouble(timingcollect.get("refalipaymoney"));
			double refwecappletmoney = CommUtil.toDouble(timingcollect.get("refwecappletmoney"));
			double refaliappletmoney = CommUtil.toDouble(timingcollect.get("refaliappletmoney"));
			double refunionpaymoney = CommUtil.toDouble(timingcollect.get("refunionpaymoney"));

			double wechattotalmoney = CommUtil.addBig(wechatmoney, wecappletmoney);
			double alipaytotalmoney = CommUtil.addBig(alipaymoney, aliappletmoney);
			double refwechattotalmoney = CommUtil.addBig(refwechatmoney, refwecappletmoney);
			double refalipaytotalmoney = CommUtil.addBig(refalipaymoney, refaliappletmoney);
			
			
			int incoins = CommUtil.toInteger(coinscollect.get("incoins"));
			double incoinsmoney = CommUtil.toDouble(coinscollect.get("incoinsmoney"));
			int ordertotal = wechatnum + alipaynum;
			double moneytotal = wechattotalmoney + alipaytotalmoney;
			
			Statistics statistics = new Statistics();
			statistics.setOrdertotal(ordertotal);
			statistics.setWecorder(wechatnum);
			statistics.setAliorder(alipaynum);
			statistics.setMoneytotal(moneytotal);
			statistics.setWecmoney(wechattotalmoney);
			statistics.setAlimoney(alipaytotalmoney);
			statistics.setWecretord(refwechatnum);
			statistics.setAliretord(refalipaynum);
			statistics.setWecretmoney(refwechattotalmoney);
			statistics.setAliretmoney(refalipaytotalmoney);
			statistics.setIncoinsorder(incoins);
			statistics.setIncoinsmoney(incoinsmoney);
			statistics.setCountTime(new SimpleDateFormat("yyyy-MM-dd").parse(time));
			statisticsService.insertStatis(statistics);
			return CommUtil.responseBuildInfo(200, "每日汇总收益信息成功", null);
		} catch (Exception e) {
			logger.info(CommUtil.getExceptInfo(e));
			return CommUtil.responseBuildInfo(201, "每日汇总收益信息失败", null);
		}
	}
	
	/**
	 * @Description： 设备到期提醒
	 * @author： ZZ         
	 * 创建时间：   2020年2月17日 下午1:55:15
	 */
//	@Scheduled(cron = "0 0 12 * * ?")// 每天中午12点触发 
	@RequestMapping("/expireRemindTask")
	@ResponseBody
	public Object expireRemind(){
		try {
			equipmentService.equipmetExpireRemind();
			return CommUtil.responseBuildInfo(200, "设备到期提醒成功", null);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return CommUtil.responseBuildInfo(201, "设备到期提醒失败", null);
		}
	}
	
	/**
	 * @Description:商家收益自动提现
	 * @author： ZZ
	 * 创建时间：   2020年6月9日 上午9:89:89(^_^)    
	 */
//	@Scheduled(cron = "0 0 9 * * ?")
	@RequestMapping("/autoWithdrawTask")
	@ResponseBody
	public Object autoWithdraw(){
		try {
			System.out.println("开始商家收益自动提现");
			withdrawService.merAutoWithdraw();
			return CommUtil.responseBuildInfo(200, "商家收益自动提现成功", null);
		} catch (Exception e) {
			logger.info("捕获异常:"+e);
			e.printStackTrace();
			return CommUtil.responseBuildInfo(201, "商家收益自动提现失败", null);
		}
	}
}
