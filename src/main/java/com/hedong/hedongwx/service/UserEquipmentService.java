package com.hedong.hedongwx.service;

import java.util.List;
import java.util.Map;

import com.hedong.hedongwx.entity.UserEquipment;

public interface UserEquipmentService {

	int addUserEquipment(UserEquipment userEquipment);

	int deleteUserEquipmentById(Integer id);
	
	int deleteUserEquipmentByEquipmentCode(String code);

	List<UserEquipment> findAllUserEquipment();

	List<UserEquipment> getUserEquipmentById(Integer id);
	
	/**
	 * 根据设备号查询用户设备信息
	 * @param code 设备号
	 * @return {@link UserEquipment}
	 */
	UserEquipment getUserEquipmentByCode(String code);

	//根据商户id查询绑定设备的在线与离线数量
	Map<String, Object> selectustoequ(Integer uid);

	/**
	 * @Description： 根据设备号查询出该设备所属商户、合伙人、分成比
	 * @author： origin   
	 */
	UserEquipment getDivideintoByCode(String code);

	/**
	 * @Description： 根据设备号查询出该设备所属的部分信息
	 * @author： origin 创建时间：   2019年9月15日 下午12:54:49 
	 */
	Map<String, Object> getEquipToUserByCode(String code);
	/**
	 * 根据设备号判断商户是否为特约商户
	 * @param request
	 * @return Object
	 */
	boolean subMerByCode(String code);
}
