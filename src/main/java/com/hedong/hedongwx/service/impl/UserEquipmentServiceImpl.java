package com.hedong.hedongwx.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hedong.hedongwx.dao.UserEquipmentDao;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.utils.CommUtil;

@Service
public class UserEquipmentServiceImpl implements UserEquipmentService {

	@Autowired
	private UserEquipmentDao userEquipmentDao;
	
	@Transactional
	@Override
	public int addUserEquipment(UserEquipment userEquipment) {
		return userEquipmentDao.addUserEquipment(userEquipment);
	}

	@Transactional
	@Override
	public int deleteUserEquipmentById(Integer id) {
		return userEquipmentDao.deleteUserEquipmentById(id);
	}
	
	@Override
	public List<UserEquipment> findAllUserEquipment() {
		return userEquipmentDao.findAllUserEquipment();
	}

	@Override
	public List<UserEquipment> getUserEquipmentById(Integer id) {
		try {
			List<UserEquipment> result = userEquipmentDao.getUserEquipmentById(id);
			return result = result != null ? result : new ArrayList<UserEquipment>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<UserEquipment>();
		}
	}

	@Override
	public UserEquipment getUserEquipmentByCode(String code) {
		return userEquipmentDao.getUserEquipmentByCode(code);
	}

	@Override
	public int deleteUserEquipmentByEquipmentCode(String code) {
		return userEquipmentDao.deleteUserEquipmentByEquipmentCode(code);
	}

	//根据商户id查询绑定设备的在线与离线数量
	@Override
	public Map<String, Object> selectustoequ(Integer uid) {
		try {
			Map<String, Object> ustoequ = userEquipmentDao.selectustoequ(uid);
			Map<String, Object> codelines = new HashMap<String, Object>();
			Map<String, Object> devicelines = CommUtil.isMapEmpty(ustoequ);
			Integer online = CommUtil.toInteger(devicelines.get("onlines"));
			Integer disline = CommUtil.toInteger(devicelines.get("disline"));
			Integer onbinding = CommUtil.toInteger(devicelines.get("disbind"));
			Integer disbinding = CommUtil.toInteger(devicelines.get("onbind"));
			Integer devisenum = CommUtil.toInteger(onbinding + disbinding);
			codelines.put("onlines", online);
			codelines.put("disline", disline);
			codelines.put("onbinding", onbinding);
			codelines.put("disbinding", disbinding);
			codelines.put("totalline", devisenum);
			return codelines;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}

	/**
	 * @Description： 根据设备号查询出该设备所属商户、合伙人、分成比
	 * @author： origin   
	 */
	@Override
	public UserEquipment getDivideintoByCode(String code) {
		return userEquipmentDao.getDivideintoByCode(code);
	}

	/**
	 * @Description： 根据设备号查询出该设备所属的部分信息
	 * @author： origin 创建时间：   2019年9月15日 下午12:54:49 
	 */
	@Override
	public Map<String, Object> getEquipToUserByCode(String code) {
		try {
			Map<String, Object> result = userEquipmentDao.getEquipToUserByCode(code);
			return result = result != null ? result : new HashMap<String, Object>();
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	/**
	 * 根据设备号判断商户是否为微信特约商户
	 */
	@Override
	public boolean subMerByCode(String code) {
		if(code == null || "".equals(code)){
			System.out.println("设备号错误"+"=================="+code); 
			return false;
		}
		String subMerCode = new StringBuffer(code).substring(0,6);
		// 根据设备号查询商户的数据
		try{
			Integer data = CommUtil.toInteger(userEquipmentDao.selectSubMerByCode(subMerCode));
			// 1是微信特约商户 
			if(data.equals(1)){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Map<String, Object> insertUserEquipment(UserEquipment userEquipment) {
		UserEquipment userEquipment2 = userEquipmentDao.getUserEquipmentDevideByCodeUserid(userEquipment);
		if (userEquipment2 != null) {
			return CommUtil.responseBuildInfo(201, "关联已存在，可直接修改", null);
		}
		Double dividePercent = userEquipment.getDividePercent();
		Double totalDivide = userEquipmentDao.selectTotalDevideByCode(userEquipment.getEquipmentCode());
		if (totalDivide == null) {
			totalDivide = 0.0;
		}
		if (CommUtil.addBig(totalDivide, dividePercent) > 1) {
			return CommUtil.responseBuildInfo(201, "分成比大于1", null);
		} else {
			userEquipmentDao.insertUserEquipment(userEquipment);
			return CommUtil.responseBuildInfo(200, "添加成功", null);
		}
	}

	@Override
	public Map<String, Object> updateUserEquipmentDevide(UserEquipment userEquipment) {
		UserEquipment userEquipment2 = userEquipmentDao.getUserEquipmentDevideByCodeUserid(userEquipment);
		if (userEquipment2 == null) {
			return CommUtil.responseBuildInfo(201, "桩主和当前设备无关联，不可修改", null);
		}
		Double dividePercent = userEquipment.getDividePercent();
		Double totalDivide = userEquipmentDao.selectTotalDiviceByCodeDisuid(userEquipment);
		if (totalDivide == null) {
			totalDivide = 0.0;
		}
		if (CommUtil.addBig(totalDivide, dividePercent) > 1) {
			return CommUtil.responseBuildInfo(201, "分成比大于1", null);
		} else {
			userEquipmentDao.updateUserEquipmentDevide(userEquipment);
			return CommUtil.responseBuildInfo(200, "修改成功", null);
		}
	}

	@Override
	public Map<String, Object> deleteUserEquipment(UserEquipment userEquipment) {
		try {
			userEquipmentDao.deleteUserEquipment(userEquipment);
			return CommUtil.responseBuildInfo(200, "删除成功", null);
		} catch (Exception e) {
			return CommUtil.responseBuildInfo(201, "删除失败，系统异常", null);
		}
	}

	@Override
	public Map<String, Object> selectUserEquipmentlist(String devicenum) {
		List<UserEquipment> meruserlist = userEquipmentDao.getUserEquipmentDevideByCode(devicenum);
		if (meruserlist == null) {
			meruserlist = new ArrayList<>();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("meruserlist", meruserlist);
		map.put("totalnum", meruserlist.size());
		Double totalDivide = 0.0;
		for (UserEquipment userEquipment : meruserlist) {
			totalDivide = CommUtil.addBig(totalDivide, userEquipment.getDividePercent());
		}
		map.put("surplus", CommUtil.subBig(1.0, totalDivide));
		return CommUtil.responseBuildInfo(200, "查询成功", map);
	}
}
