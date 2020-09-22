package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.UserEquipment;

public interface UserEquipmentDao {

	int addUserEquipment(UserEquipment userEquipment);
	
	int deleteUserEquipmentById(Integer id);
	
	int deleteUserEquipmentByEquipmentCode(String code);
	
	List<UserEquipment> findAllUserEquipment();
	
	List<UserEquipment> getUserEquipmentById(Integer id);
	
	UserEquipment getUserEquipmentByCode(String code);

	Map<String, Object> selectustoequ(@Param("userId")Integer userId);

	//@Description： 查询绑定的设备信息
	List<Map<String, Object>> coderelevance();

	//@Description：bonduser查询绑定设备的用户
	List<Map<String, Object>> distinctuid();

	//@Description：bonduser查询绑定设备合伙人信息
	List<Map<String, Object>> bindingcodepartnerinfo();

	/**
	 * @Description： 根据设备号查询出该设备所属商户、合伙人、分成比
	 * @author： origin   
	 */
	UserEquipment getDivideintoByCode(@Param("code")String code);

	/**
	 * @Description： 根据设备号查询出该设备所属的部分信息
	 * @author： origin 创建时间：   2019年9月15日 下午1:00:07 
	 */
	Map<String, Object> getEquipToUserByCode(String code);

	/**
	 * @Description：更改绑定设备的商户
	 * @param nowdealid 商户id  devicenum 设备号
	 * @author： origin
	 * @createTime：2020年4月6日下午2:56:45
	 */
	Integer updateUserEquipment(@Param("dealid")Integer nowdealid, @Param("code")String devicenum);
	
	/**
	 * 根据设备号查询用户是否为特约商户的信息
	 * @param code 设备号
	 * @return {@link Integer} 0:不是 1:是特约商户
	 */
	Integer selectSubMerByCode(@Param("code")String code);

	/**
	 * @Description：查询绑定设备的商户与未绑定设备的合伙人
	 * @author： origin
	 * @createTime：2020年4月17日下午4:56:28
	 */
	List<Map<String, Object>> inquireAllmeridInfo();
	
}
