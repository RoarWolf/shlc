package com.hedong.hedongwx.web.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hedong.hedongwx.entity.AllPortStatus;
import com.hedong.hedongwx.service.AllPortStatusService;
import com.hedong.hedongwx.utils.JedisUtils;

@RequestMapping("/hdredis")
@RestController
public class RedisController {
	private final Logger logger = LoggerFactory.getLogger(RedisController.class);
	@Autowired
	private AllPortStatusService allPortStatusService;
	//根據設備號查詢緩存
	@RequestMapping("/PortInfo")
	public Map<String,Object> getEquipmentCache(String code){
		Map<String, String> codeMap=new ConcurrentHashMap<String, String>();
		Map<String, Object> resultMap = new HashMap<>();
		if(code != null && !"".equals(code)){
			//判断设备的号是否存在缓存中
			if(JedisUtils.exists(code)){
				codeMap=JedisUtils.hgetAll(code);
				resultMap.put("redisMap", codeMap);
			}else{
				logger.debug("緩存中不存在"+code);
			}
		}else{
			logger.debug("設備號不能爲空");
		}
		List<AllPortStatus> equipmentnum = allPortStatusService.findPortStatusListByEquipmentnum(code, 20);
		resultMap.put("mysqlMap", equipmentnum);
		return resultMap;
	}
	
	/**
	 * 存储设备的交互日志信息到Redis,设置过期时间
	 * @param code 设备号
	 * @param logs 日志信息
	 */
	@RequestMapping("/RedisLogs")
	public  void  RedisLog(String code, String logs){
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		String time = LocalDateTime.now().format(pattern);
		boolean b = JedisUtils.existsCode(code);
		//存在获取
		logger.info("设备"+code+"存在"+b);
		if(b){
			StringBuffer buffer = new StringBuffer(JedisUtils.getCode(code));
			buffer.append(time+":设备发送"+logs+"||");
			JedisUtils.setCode(code, buffer.toString(), 0);
		}else{
			JedisUtils.setCode(code, time+":设备发送"+logs+"||", 0);
		}
	}
	
	/**
	 * 根据设备号获取设备的日志信息
	 * @param code 设备号
	 * @return String
	 */
	@RequestMapping("/selectRedisLogs")
	public String selectRedisLogs(String code){
		return "";
	}
	
	
}
