package com.hedong.hedongwx.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.dao.AllPortStatusDao;
import com.hedong.hedongwx.entity.AllPortStatus;
import com.hedong.hedongwx.service.RedisService;
import com.hedong.hedongwx.utils.JedisUtils;

@Service
public class RedisServiceImpl implements RedisService{
	
	@Autowired
	public  AllPortStatusDao allPortStatusDao;
	
	//获取一个设备所有端口缓存信息
	@Override
	public Map<String, String> getEquipmentCacheInfo(String code) {
		// TODO Auto-generated method stub
		Map<String, String> codeMap=new ConcurrentHashMap<String, String>();
		//判断设备的号是否存在缓存中
		if(code != null && !"".equals(code) && JedisUtils.exists(code)){
			System.out.println(JedisUtils.exists(code)+"设备号存在");
			codeMap=JedisUtils.hgetAll(code);
		}else{
			//把数据的设备端口信息存入缓存
			addEquipmentCache(code);
		}
		return codeMap;
	}
	//根据设备号和端口号获取一个端口的缓存信息
	@Override
	public String getOnePortInfo(String code, String port) {
		// TODO Auto-generated method stub
		String value="";
		if(code !=null && !"".equals(code) && JedisUtils.exists(code)){
			if(port!=null && !"".equals(port) && JedisUtils.hexists(code, port)){
				value=JedisUtils.hget(code, port);
				System.out.println(value+"端口信息");
			}else{
				//端口不能为空
				AllPortStatus portStatus=allPortStatusDao.findPortStatusByEquipmentnumAndPort(code,Integer.parseInt(port));
				JedisUtils.upDateCache(code, JSON.toJSONString(portStatus.getPort()),JSON.toJSONString(portStatus));
				System.out.println("缓存中不存在端口信息，添加成功");
			}
		}else{
			addEquipmentCache(code);
			System.out.println("缓存中不存在设备信息，添加成功");
		}
		return value;
	}
	//从数据库中读取设备端口信息放入缓存
	@Override
	public  void addEquipmentCache(String code) {
		// TODO Auto-generated method stub
		if(code != null && !"".equals(code)){
			List<AllPortStatus> listPort=new ArrayList<AllPortStatus>();
			Map<String, String> codeMap=new ConcurrentHashMap<String,String>();
			listPort=allPortStatusDao.findPortStatusListByEquipmentnum(code,20);
			//端口信息不为空
			System.out.println(listPort);
			if(!listPort.isEmpty()){
				for (int i = 0; i < listPort.size(); i++) {
				    codeMap.put(JSON.toJSONString(listPort.get(i).getPort()), JSON.toJSONString(listPort.get(i)));
				}
				JedisUtils.hmset(code, codeMap);
				System.out.println(code+"设备添加缓存🆗");
			}else{
				System.out.println("数据库为空"+code);
			}
		}
	}
	//更新一个设备端口的缓存信息
	@Override
	public void upDateCache(String code, AllPortStatus allPortStatus) {
		// TODO Auto-generated method stub
		if(code != null && !"".equals(code) && JedisUtils.exists(code)){
			String portKey=allPortStatus.getPort()+"";
			//判断缓存中端口的键是否存在
			if(JedisUtils.hexists(code, portKey)){
				long ok=JedisUtils.upDateCache(code, portKey, JSON.toJSONString(allPortStatus));
				System.out.println(ok+"更新端口缓存成功");
			}else{
				//查询出端口的信息存入缓存
				AllPortStatus portStatus=allPortStatusDao.findPortStatusByEquipmentnumAndPort(code, allPortStatus.getPort());
				JedisUtils.upDateCache(code, JSON.toJSONString(portStatus.getPort()),JSON.toJSONString(portStatus));
				System.out.println("添加端口缓存成功");
			}
		}else{
			//查询出设备端口的信息存入缓存
			addEquipmentCache(code);
			System.out.println("添加设备缓存成功");
		}
	}
	//删除设备缓存
	@Override
	public void delEquipmentCache(String code) {
		// TODO Auto-generated method stub
		if(code !=null && !"".equals(code) && JedisUtils.exists(code)){
			JedisUtils.del(code);
		}else{
			System.out.println("删除失败设备号不存在");
		}
		
	}
	//删除端口缓存
	@Override
	public void delEquipmentPortCache(String code, String port) {
		// TODO Auto-generated method stub
		if(code !=null && !"".equals(code)){
			if(JedisUtils.hexists(code, port)){
				Long o=JedisUtils.hdel(code, port);
				System.out.println(o);
			}else {
				System.out.println("端口不存在");
			}
		}
	}

}
