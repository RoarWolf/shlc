package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.Parameters;

public interface EquipmentDao {
	
	int addEquipment(Equipment equipment);
	
	List<Equipment> getEquipmentList();
	
	Equipment getEquipmentById(String code);

	Equipment getEquipmentAndAreaById(String code);
	
	int updateEquLiveTimeByCode(Equipment equipment);
	
	int updateEquipment(Equipment equipment);
	
	int deleteEquipmentById(Integer id);
	
	int updateTempidByEquipmentCode(@Param("code")String code,@Param("tempid")Integer tempid);
	
	List<Equipment> findAll(@Param("startIndex")int startIndex, @Param("pageSize")int pageSize);
	
	int updataBindtypeByEquipmentCode(@Param("bindtype")Byte bindtype,@Param("code")String code);

	List<Equipment> selectEquipment(Equipment equipment);

	String selectdisequ();
	
	int updateEquHardversionByCode(@Param("code")String code,@Param("hardversion")String hardversion);
	
	List<Equipment> selectEqulistByAid(Integer aid);
	
	List<Equipment> selectUnbindEqulist(Integer uid);
	
	//查询商家小区下的设备并根据到期时间排序 ZZ
	List<Equipment> selectAreaEqulistOrderyByExpire(Integer aid);
	
	//查询商家未绑定得设备并根据到期时间排序 ZZ
	List<Equipment> selectUnbindEqulistOrderyByExpire(Integer uid);
	
	//根据小区号查询设备数
	String selectEquCountByAid(@Param("aid")Integer aid);

	List<Equipment> selectEquDisRelAid(@Param("uid")Integer uid);
	
	int updateEquAidByAid(Integer aid);
	
	List<Equipment> selectEquListByUidAndBindtype(@Param("uid")Integer uid,@Param("state")Byte state,
			@Param("source")String source, @Param("parameter")String parameter, @Param("startnum")Integer startnum, @Param("equnum")Integer equnum);
	
	Integer selectEquListByUidAndBindtypeNum(@Param("uid")Integer uid,@Param("state")Byte state);

	List<Map<String, Object>> selectEquipmentParameter(Parameters parameters);
	
	//查询用户可用商户关联管理员下的所有小区的所有设备编号
	List<String> selectCodeByAreaManNotNull(@Param("merid")Integer merid,@Param("manid")Integer manid);
	
	//查询用户可用商户所有小区下所有无管理员的设备以及未绑定小区的设备编号
	List<String> selectCodeByAreaManIsNull(@Param("merid")Integer merid,@Param("manid")Integer manid);
	
	//添加蓝牙设备
	int addBluetoothDevice(Equipment equipment);
	
	//查询最后一个设备编号
	String selectEndDeviceNum(@Param("deviceType") Integer deviceType);
	
	//查询蓝牙设备的id（MAC）
	Equipment selectBluetoothMac(@Param("code") String code);
	
	//查询蓝牙设备是否存在
	Equipment selectBluetoothExist(@Param("deviceId") String deviceId);
	
	//查询最近24小时活跃设备数量
	Integer selectLast24Hours();
	
	//查询最近30天活跃设备数量
	Integer selectLastMonth();
	
	int updateEquEarn(@Param("code") String code,@Param("money") Double money,@Param("type") Integer type);
	
	int everydayResetEquEarn();

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询设备列表 (跳转设备列表) 
	List<Map<String, Object>> selectEquList(Parameters parameters);
	
	//查询测试次数
	String selectEquiTestSeveral(String code);

	/**
	 * @Description： 根据用户id查询合伙人绑定的设备
	 * @author： origin   
	 */
	List<Equipment> selectpartnercode(@Param("manid") Integer id);

	/**
	 * @Description： 根据条件查询用户绑定的设备与作为合伙人的设备
	 * @author： origin   
	 */
	List<Map<String, Object>> selectrelatedcode(@Param("uid")Integer uid,@Param("state")Byte state,
			@Param("source")String source, @Param("parameter")String parameter, @Param("startnum")Integer startnum, @Param("equnum")Integer equnum);

	/**
	 * @Description： 
	 * @author： origin  
	 */
	List<Equipment> selectEqucode(Equipment equcade);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月13日 上午9:57:13 
	 */
	Integer updateEquipmentRemark(@Param("remark")String remark, @Param("code")String code);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月1日 下午3:00:59 
	 */
	Map<String, Object> slelectPortInfo(Parameters param);
	
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月30日 下午3:08:56 
	 * @param param 
	 */
	List<Map< String, Object>> slelectPortInfoList(Parameters param);
	
	
	List<String> slelecToPortINfo(@Param("equip")String code, @Param("merid")Integer merid);
	
	/**
	 * @Description： uid, equid, port
	 * @author： origin 创建时间：   2019年7月30日 下午5:07:52 
	 */
	Integer insertPortdate(@Param("equip")String equip, @Param("merid")Integer merid, @Param("port")Integer port, @Param("uid")Integer member);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月30日 下午5:11:42 
	 */
	Integer deletePortdate(@Param("id")Integer id);

	/**
	 * @Description： 根据小区id获取对应在该小区下的设备信息
	 * @author： origin 创建时间：   2019年8月10日 下午5:25:31 
	 */
	List<Map<String, Object>> selfAreaEquipInfo(Parameters parame);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月10日 上午11:27:49 
	 */
	Map<String, Object> selectEquipAreaInfo(Parameters parame);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月20日 下午4:21:16 
	 */
	Map<String, Object> agentEquipCollect(Parameters paramet);
	
	/**
	 * 缴费成功更新设备的到期时间
	 * @Description：ZZ 
	 * @param code
	 */
	void updateExpirationTime(@Param("code")String code);
	
	/**设备到期提醒
	 * @Description：ZZ 
	 * @author： origin 创建时间：   2020年2月18日 上午11:21:16 
	 */
	List<Map<String, Object>> equipmetExpireRemind(Parameters parameters);
	
	/**删除设备的IMEI号
	 * @Description：ZZ 
	 * @param code
	 */
	void deleteEquipmentIMEIByCode (@Param("code")String code);
	
	/**超级管理员定制设备号
	 * @Description：ZZ 
	 * @param code
	 */
	void customizationCode(@Param("oldCode")String oldCode, @Param("newCode")String newCode);
	
	//--------------------------------------------------------------------------------
	/**
	 * 1-PC端首页设备数据
	 * @param merId 商家的id
	 * @return {@link Map}
	 */
	Map<String, Object> homePageEquip(@Param("merId")Integer merId);
	
	/**
	 * 2-PC端首页用户数据
	 * @param merId 商家的id
	 * @return {@link Map}
	 */
	Map<String, Object> homePageUser(@Param("merId")Integer merId);

	/**
	 * 3-PC端首页获取缴费信息和已提现总额,提现总手续费,待提现金额,和手续费
	 * @param merId 商家的id
	 * @return {@link Map}
	 */
	Map<String, Object> homePageFeescale(@Param("merId")Integer merId);


	/**
	 * 4-PC端首页今日收益数据
	 * @param merId 商家的id
	 * @return {@link Map}
	 */
	Map<String, Object> homePageTodayEarn(@Param("merId")Integer merId);

	/**
	 * 5-PC端首页历史总收益数据
	 * @param merId 商家的id
	 * @return {@link Map}
	 */
	Map<String, Object> homePageTotalEarn(@Param("merId")Integer merId);
	//-----------------------------------------------------------------------------------------------------
	/*-- *** 请暂时不要在*号范围内添加   thanks ***********************************************************   --*/
	/**
	 * ORIGGIN  ANEW AFRESH
	 * @Description：根据设备号查询设备信息与设备关联信息(如:设备绑定人员信息、设备所属小区信息)
	 * @param devicenum:设备号
	 * @author： origin 2020年6月20日下午5:09:54
	 * @comment:
	 */
	Map<String, Object> getDeviceRelevanceInfo(@Param("devicenum")String devicenum);
	/*-- ****************************************************************************************  --*/
	
	/**
	 * 查询设备的位置信息
	 * @return {@link List}
	 */
	List<Map<String, Object>> seleLocationData();

	/**
	 * @method_name: inquireDeviceCount
	 * @Description: 查询设备计数信息【 查询所有设备数量 的在线数量、领先数量、绑定数量、未绑定数量】
	 * @param merid:商户id[查询所有时，商户id为null]
	 * @return
	 * @Author: origin  创建时间:2020年9月3日 上午10:43:58
	 * @common:   
	 */
	Map<String, Object> inquireDeviceCount(@Param("userId")Integer merid);
	
	/**
	 * 分页查询符合条件的设备信息
	 * @param parameters 参数
	 * @return {@link List}
	 */
	List<Map<String, Object>> exportDeviceData(Parameters parameters);

	/**
	 * @method_name: getWarnParamsInfo
	 * @Description: 
	 * @param code
	 * @param merid
	 * @param opertype
	 * @Author: origin  创建时间:2020年9月12日 下午6:03:05
	 * @common:   
	 */
	void getWarnParamsInfo(@Param("code")String code, @Param("merid")Integer merid, @Param("opertype")Integer opertype);
	
	
}
