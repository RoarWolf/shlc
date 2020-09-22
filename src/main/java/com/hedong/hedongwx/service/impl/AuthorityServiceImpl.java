package com.hedong.hedongwx.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.dao.DealerAuthorityDao;
import com.hedong.hedongwx.dao.UserDao;
import com.hedong.hedongwx.entity.DealerAuthority;
import com.hedong.hedongwx.service.AuthorityService;
import com.hedong.hedongwx.utils.CommUtil;

/**
 * @Description： 
 * @author  origin  创建时间：   2019年7月8日 下午4:01:16  
 */
@Service
public class AuthorityServiceImpl implements AuthorityService{
	
	@Autowired
	private DealerAuthorityDao dealerAuthorityDao;
	@Autowired
	private UserDao userDao;
	
	/**
	 * @Description： 开启指定商户某个消息权限（如果不存在添加之后开启）
	 */
//	public Map<String, Object> messSwitchAuthority(Integer id, Integer merid, Integer with, Integer equip, 
//			Integer order, Integer incoinrefund, Integer showincoins) {

	@Override
	public Map<String, Object> messSwitchAuthority(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer id = CommUtil.toInteger(maparam.get("id"));
			Integer merid = CommUtil.toInteger(maparam.get("merid"));
			Integer with = CommUtil.toInteger(maparam.get("with"));
			Integer equip = CommUtil.toInteger(maparam.get("equip"));
			Integer order = CommUtil.toInteger(maparam.get("order"));
			Integer incoinrefund = CommUtil.toInteger(maparam.get("incoinrefund"));
			Integer showincoins = CommUtil.toInteger(maparam.get("showincoins"));
			Integer incomemess = CommUtil.toInteger(maparam.get("incomemess"));
			if(incomemess.equals(0)) incomemess = 2;
			Map<String, Object> map = insertAuthority(merid);
			if(map.get("code").equals(200) || map.get("code").equals(201)){
				DealerAuthority author = new DealerAuthority();
				author.setMerid(merid);
				author.setWithmess(with);
				author.setEquipmess(equip);
				author.setOrdermess(order);
				author.setIncoinrefund(incoinrefund);
				author.setShowincoins(showincoins);
				author.setIncomemess(incomemess);
				dealerAuthorityDao.updateMeridAuthority(author);
				datamap.put("messsdata", dealerAuthorityDao.selectAuthority(null, merid));
				datamap.put("code", 200);
			}else{
				map.put("code", 400);
			}
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(400, "异常错误", datamap);
		}
	}
	
	/**
	 * @Description： 开启指定商户某个消息权限（如果不存在添加之后开启）
	 * @author： origin 创建时间：   2019年7月8日 下午4:03:54 
	 * @param merid:商户id   switchs:开关开闭    status:类型(指定对象)
	 */
	@Override
	public Map<String, Object> messAuthority( Integer merid, Integer switchs, Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		DealerAuthority author = new DealerAuthority();
		try {
			if(!existSwitch(merid)){
				author.setMerid(merid);
				dealerAuthorityDao.insertAuthority(author);
			}
			if(status==1){//1:提现通知、2订单通知、3设备上下线通知  开关
				author.setWithmess(switchs);
			}else if(status==2){
				author.setOrdermess(switchs);
			}else if(status==3){
				author.setEquipmess(switchs);
			}
			dealerAuthorityDao.updateMeridAuthority(author);
			map.put("authority", dealerAuthorityDao.selectAuthority(null, merid));
			map.put("code", 200);
			
		} catch (Exception e) {
			map.put("code", 400);
		}
		return map;
	}
	
	@Override
	public DealerAuthority selectMessSwitch(Integer merid) {
		DealerAuthority deaut = null;
		try {
			deaut = dealerAuthorityDao.selectAuthority(null, merid);
			if(deaut==null) deaut = new DealerAuthority();
		} catch (Exception e) {
			deaut = new DealerAuthority();
		}
		return deaut;
	}
	
	public Map<String, Object> insertAuthority(Integer merid){
		Map<String, Object> map = new HashMap<String, Object>();
		DealerAuthority author = new DealerAuthority();
		try {
			// 根据商家的id查询用户的开关信息
			DealerAuthority deaut = dealerAuthorityDao.selectAuthority(null, merid);
			if(deaut==null){
				author.setMerid(merid);
				// 插入
				dealerAuthorityDao.insertAuthority(author);
				map.put("code", 200);
			}else{
				map.put("code", 201);
			}
		} catch (Exception e) {
			map.put("code", 400);
		}
		return map;
		
	}
	
	@Override
	public DealerAuthority getAuthorList(Integer merid){
		DealerAuthority author = new DealerAuthority();
		try {
			author.setMerid(merid);
			List<DealerAuthority> deaut = dealerAuthorityDao.selectEntiyAuthority(author);
			Integer len = deaut.size();
			if(len>0) author = deaut.get(0);
			return author;
		} catch (Exception e) {
			return author;
		}
	}
	
	@Override
	public boolean existSwitch(Integer merid){
		try {
			DealerAuthority deaut = dealerAuthorityDao.selectAuthority(null, merid);
			if(deaut==null) return false;
			else return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public boolean existSwitchList(Integer merid){
		try {
			DealerAuthority author = new DealerAuthority();
			List<DealerAuthority> deaut = dealerAuthorityDao.selectEntiyAuthority(author);
			Integer len = deaut.size();
			if(len==0) return false;
			else return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean authorSwitch(Integer merid, Integer type){
		Boolean fole = false;
		Integer switchs = 2;
		try {
			DealerAuthority author = selectMessSwitch(merid);
//			DealerAuthority auth = getAuthorList(merid);
			if(type==1){
				switchs = author.getWithmess();
			}else if(type==2){
				switchs = author.getEquipmess();
			}else if(type==3){
				switchs = author.getOrdermess();
			}else if(type==3){
				switchs = author.getIncomemess();
			}
			if(switchs==1) fole = true;
		} catch (Exception e) {
			e.getMessage();
		}
		//return switchs;
		return fole;
	}
	
	@Override
	public Integer selectIncoinRefund(Integer merid) {
		return dealerAuthorityDao.selectIncoinRefund(merid);
	}

	/**
	 * separate
	 * @Description：开启指定商户某个消息权限（如果不存在添加之后开启）
	 * @author： origin   2019年11月8日 上午9:57:32 
	 */
	@Override
	public Object dealerauthoritySeting(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer id =  CommUtil.toInteger(maparam.get("id"));
			Integer merid =  CommUtil.toInteger(maparam.get("merid"));
			Integer withmess =  CommUtil.toInteger(maparam.get("with"));
			if(withmess.equals(0)) withmess = CommUtil.toInteger(maparam.get("withmess"));
			Integer incomemess =  CommUtil.toInteger(maparam.get("incomemess"));
			if(incomemess.equals(0)) incomemess = 2;
			Integer equip =  CommUtil.toInteger(maparam.get("equip"));
			Integer order =  CommUtil.toInteger(maparam.get("order"));
			Integer incoinrefund =  CommUtil.toInteger(maparam.get("incoinrefund"));
			Integer showincoins =  CommUtil.toInteger(maparam.get("showincoins"));
			// 自动提现
			Integer autoWithdraw = CommUtil.toInteger(maparam.get("autoWithdraw"));
			if(autoWithdraw.equals(0)) autoWithdraw = 2;
			// 商家自动续费
			Integer autopay = CommUtil.toInteger(maparam.get("autopay"));
			// 商家开启分摊
			Integer apportion = CommUtil.toInteger(maparam.get("apportion"));
			Map<String, Object> map = insertAuthority(merid);
			if(map.get("code").equals(200) || map.get("code").equals(201)){
				DealerAuthority author = new DealerAuthority();
				author.setMerid(merid);
				author.setWithmess(withmess);
				author.setEquipmess(equip);
				author.setOrdermess(order);
				author.setIncoinrefund(incoinrefund);
				author.setIncomemess(incomemess);
				author.setShowincoins(showincoins);
				author.setAutopay(autopay);
				author.setApportion(apportion);
				author.setAutoWithdraw(autoWithdraw);
				dealerAuthorityDao.updateMeridAuthority(author);
				datamap.put("messsdata", dealerAuthorityDao.selectAuthority(null, merid));
				CommUtil.responseBuildInfo(200, "成功", datamap);
			}else{
				CommUtil.responseBuildInfo(301, "异常错误", datamap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	

}
