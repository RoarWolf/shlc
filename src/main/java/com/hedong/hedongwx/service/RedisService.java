package com.hedong.hedongwx.service;

import java.util.Map;

import com.hedong.hedongwx.entity.AllPortStatus;

public interface RedisService {
	    //五分钟给上传数据更新缓存
		public void upDateCache(String code,AllPortStatus allPortStatus);
		//根据设备号获取缓存中设备端口信息
		public Map<String,String> getEquipmentCacheInfo(String code);
		//根据设备号和端口获取一个端口的状态信息
		public String  getOnePortInfo(String code,String port);
		//将数据库的设备状态数据存进缓存中
		public void addEquipmentCache(String code);
		//根据设备号删除一个设备缓存
		public void delEquipmentCache(String code);
		//根据设备号和端口号删除一个端口缓存
		public void delEquipmentPortCache(String code,String port);
		
}
