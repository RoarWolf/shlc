package com.hedong.hedongwx.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.FeescaleRecord;
import com.hedong.hedongwx.entity.User;

public interface FeescaleService {
		//查询总缴费的金额
		Map<String, Object> selectFeescaleTotalEarnings();
		
		// 手机端根据商家的id查询设备类型的收费
		List<Map<String,Object>> getMerFeescale(Integer merid);
		
		// PC端根据商家的id查询设备类型的收费
		Object getFeescalBymerid(Integer merId,Model model);
		
		// 获取系统的收费标准
		Map<String, Object> getSystemFeescale();
		
		// 超级管理员修改系统的收费模板
		public boolean updateSystemFeescale(Map<String, Map<String,Double>> netMap, Map<String, Map<String,Double>> blueMap);	
		
		// 根据商家的id修改设备类型的收费标准
		public boolean updateFeescaleBymerid(Integer id,String userName, Map<String, Map<String,Double>> netType,Map<String, Map<String,Double>> blueType);
		
		// 商家设置是否开启自动续费和开启分摊
		public Object updateSwitch(Integer merid,Integer autoPay,Integer apportion,Integer autowithdraw);
		
		// 设置最大负收入
		public boolean setMerMaxNegative(Integer merid,Double maxMerNegative);
		
		// 设置设备的到期时间
		public boolean setEquipmentExpire(String code,String expireTime);
		
		// 根据商家的id查询商家的小区
		public List<Area> getAllAreaAndPartner(Area area);
		
		// 展示商家未绑定的设备
		public List<Equipment> getUnbindEquipment(Integer merId);
		
		// 缴费
		public Object merPayment(List<User> users, List<Equipment> equipmentNum,Integer paytype,Integer aid);
		
		// 插入消费记录信息
		public void insertFeescaleRecord(List<User> users ,List<Equipment> equipmentNum,Integer paytepe,Integer aid);
		
		// 查看缴费记录
		public Object selectFeescaleRecord(HttpServletRequest request);
		
		// 商家手机端查看缴费记录
		public Object merSelectFeescaleRecord(HttpServletRequest request);
		
		// pc端查询缴费详情
		Object selectFeescaleDetails(HttpServletRequest request);
		
		//根据订单号查询缴费信息(手机微信缴费)
		public List<Map<String, Object>> selectWxFeescaleRecord(String ordernum);
		
		//更改微信订单状态
		public void updateFeescaleRecordStatue(String ordernum);
		
		
		// 插入微信缴费记录
		public void insertWxFeescaleRecord(FeescaleRecord feescaleRecord);
		
		/**
		 * PC端首页测试
		 * @param request
		 * @return {@link Map}
		 */
		Map<String, Object> testPCIndex(HttpServletRequest request);
}
