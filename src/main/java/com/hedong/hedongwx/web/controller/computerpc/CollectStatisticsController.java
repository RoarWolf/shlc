package com.hedong.hedongwx.web.controller.computerpc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hedong.hedongwx.service.CollectStatisticsService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.StringUtil;

/**
 * @Description： 汇总统计计算类
 * @author  origin  
 */
@Controller
@RequestMapping(value="/pcstatistics")
public class CollectStatisticsController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CollectStatisticsService collectStatisticsService;
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * @Description： 汇总商户收益
	 * @author： origin 创建时间：   2019年9月7日 下午7:28:28
	 */
	public void  collectDealerIncome(){
		try {
			String time = CommUtil.getPastDate(1);
			String startTime = time + " 00:00:00";
			String endTime = time + " 23:59:59";
//			Map<String, Object> result = collectStatisticsService.incomeCollect(startTime, endTime);
		} catch (Exception e) {
			logger.info(CommUtil.getExceptInfo(e));
		}
	}
	
	
	
	
	
	
	
	
	
}
