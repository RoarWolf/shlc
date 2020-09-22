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
}
