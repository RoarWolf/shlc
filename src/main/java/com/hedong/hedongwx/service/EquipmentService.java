package com.hedong.hedongwx.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

import com.hedong.hedongwx.entity.CodeSystemParam;
import com.hedong.hedongwx.entity.Codestatistics;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.EquipmentNew;
import com.hedong.hedongwx.entity.PageBean;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.Realchargerecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.utils.PageUtils;

public interface EquipmentService {

	int addEquipment(Equipment equipment);

	List<Equipment> getEquipmentList();

	Equipment getEquipmentById(String code);
	
	Equipment getEquipmentAndAreaById(String code);
	
	int updateEquLivenumByCode(String code);

	int updateEquipment(Equipment equipment);

	int deleteEquipmentById(Integer id);

	int updateTempidByEquipmentCode(String code, Integer tempid);
	
	PageBean<Equipment> findAllEquipmentPage(HttpServletRequest request);

	PageUtils<Parameters> selectEquipmentParameter(HttpServletRequest request);
	
	int updataBindtypeByEquipmentCode(Byte bindtype,String code);

	PageUtils<Equipment> selectEquipment(HttpServletRequest request);

	String selectdisequ();
	
	int updateEquHardversionByCode(String code,String hardversion);
	
	List<Equipment> selectEqulistByAid(Integer aid);
	
	//查询小区下的设备并按照设备得到期时间排序
	List<Equipment> selectAreaEqulistOrderyByExpire(Integer aid);
	
	List<Equipment> selectUnbindEqulist(Integer uid);

	/** @Description： 查询指定商户名下未绑定小区的设备信息 */
	List<Equipment> selectEquDisRelAid(Integer uid);
	
	List<Codestatistics> selectAllByCode(String code,Integer merid);
	
	List<Codestatistics> selectAllByCodeAndLimit(String code,Integer merid,Integer beginnum,Integer endnum);
	
	int updateEquAidByAid(Integer aid);
	
	List<Equipment> selectEquListByUidAndBindtype(Integer uid, Byte state, String source, String parameter, Integer startnum, Integer equnum);
	
	Integer selectEquListByUidAndBindtypeNum(Integer uid,Byte state);
	
	int insertCodeoperatelog(String code, Integer sort, Integer type, Integer merid, Integer opeid
			,String remark);
	
	//查询用户可用商户关联管理员下的所有小区的所有设备编号
	List<String> selectCodeByAreaManNotNull(Integer merid,Integer manid);
	
	//查询用户可用商户所有小区下所有无管理员的设备以及未绑定小区的设备编号
	List<String> selectCodeByAreaManIsNull(Integer merid,Integer manid);
	
	//添加设备系统参数
	int insertCodeSystemParam(CodeSystemParam codeSystemParam);
	
	//修改设备系统参数
	int updateCodeSystemParam(CodeSystemParam codeSystemParam);
	
	//查询设备系统参数
	CodeSystemParam selectCodeSystemParamByCode(String code);
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询设备列表 (跳转设备列表)
	PageUtils<Parameters> selectEquList(HttpServletRequest request);
	//查询蓝牙设备列表 (跳转设备列表)
	PageUtils<Parameters> selectBluetoothEquList(HttpServletRequest request);
	
	//根据设备号查询测试的次数
	String selectEquiTestSeveral(String code);
	
	//根据设备号更新测试的次数
	int updateEquiTestSeveral(String code, Integer several);
	
	//查询设备列表(根据uid查询对应商户下的设备；管理员为所有)
	List<Map<String, Object>> selectEquListByParam(Parameters parameters);
	
	//强制解绑时统计计算该设备的今日收益
	void codeCollectmoney(String code, Integer userId, Integer aid, String hardversion);

	//查询设备操作日志  @origin
	PageUtils<Parameters> selectCodeOperateLog(HttpServletRequest request);

	/**
	 * @Description： 根据用户id查询合伙人绑定的设备
	 * @author： origin   
	 */
	List<Equipment> selectpartnercode(Integer id);

	/**
	 * @Description： 根据条件查询用户绑定的设备与作为合伙人的设备
	 * @author： origin   
	 */
	List<Map<String, Object>> selectrelatedcode(Integer uid, Byte state, String source, String parameter, Integer startnum, Integer equnum);
	
	/**添加蓝牙设备*/
	int addBluetoothDevice(Equipment equipment);
	
	/**查询最后一个设备编号*/
	String selectEndDeviceNum(Integer deviceType);
	
	//查询蓝牙设备的id（MAC）
	Equipment selectBluetoothMac(String code);
	//查询蓝牙设备是否存在
	Equipment selectBluetoothExist(String deviceId);
	
	//查询最近24小时活跃设备数量
	Integer selectLast24Hours();
	
	//查询最近30天活跃设备数量
	Integer selectLastMonth();
	
	//根据设备号查询用户电话
	String queryCodeBindPhone(String code);
	
	int updateEquEarn(String code,Double money,Integer type);
	
	int everydayResetEquEarn();
	
	// 重置设备收益
	int resetEquEarn(String code);

	/** ============================================================================================================= */
	
	/**
	 * @Description：   根据条件查询用户消费信息
	 * @author： origin 
	 * @return hd_realchargerecord
	 */
	List<Realchargerecord> realChargeRecordList(Integer orderId);
	
	/**
	 * @Description：    根据条件查询数据的函数情况  1：根据充电id(chargeid)
	 * @author： origin     
	 */
	Map<String, Object> functionRecord(Integer orderId);

	/**根据实体类添加消费信息*/
	int insertRealRecord(Integer chargeid, Integer uid, Integer merid, String code, Integer port, Integer type,
			Integer chargetime, Integer surpluselec, Integer power, double money);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月13日 上午9:56:08 
	 */
	Integer updateEquipmentRemark( String remark, String code);
	
	List<Codestatistics> selectOneByCodeAndTime(String code, String countTime);
	
	void forEquCollet();

	/**
	 * @Description： 查询十路设备指定端口信息的情况
	 * @author： origin 创建时间：   2019年7月30日 下午3:04:20 
	 */
	List<Map< String, Object>> specifiedUsePort(Integer merid, String code);

	/**
	 * @Description： 添加指定端口使用对象  equip:设备号     port:端口号   member:会员号(id)
	 * @author： origin 创建时间：   2019年7月30日 下午5:04:58 
	 * @param user 
	 */
	Map< String, Object> addSpecifiedPort(User user, String equip, Integer port, String member);

	/**
	 * @Description： 指定记录对象id
	 * @author： origin 创建时间：   2019年7月30日 下午5:05:02 
	 */
	Map< String, Object> deleteSpecifiedPort(String memberList);

	/**
	 * @Description： 查询十路设备指定端口信息的情况
	 * @author： origin 创建时间：   2019年7月31日 下午5:05:06 
	 */
	List<Map< String, Object>> specifiedUsePortInfo(Integer merid, String code, Integer port);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月5日 上午11:46:49 
	 */
	List<Map<String, Object>>  assignportinfo(Integer uid, String code, Integer merid);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月5日 下午2:15:05 
	 */
	List<String> slelecToPortINfo(Integer merid, String code);

	/**
	 * @Description： 根据小区id获取对应在该小区下的设备信息
	 * @author： origin 创建时间：   2019年8月10日 下午5:25:31 
	 * @param numPerPage 
	 * @param page 
	 */
	PageUtils<List<Map<String, Object>>> selfAreaEquipInfo(Integer aid, Integer page, Integer numPerPage);
	
	
	PageUtils<List<Map<String, Object>>> selfAreaEquipMerInfo(Integer merid, Integer page, Integer numPerPage);

	/**
	 * @Description： 根据设备号查询设备信息与小区信息
	 * @author： origin 创建时间：   2019年8月10日 上午11:23:15 
	 */
	Map<String, Object>  selectEquipAreaInfo(String code);

	/**
	 * @method_name: selectAssignTypeEqui
	 * @Description：  搜索指定类型的设备信息
	 * @param merid  versions
	 * @author： origin 创建时间：   2019年8月23日 下午3:19:02 
	 * @return
	 * @common:
	 */
	List<Map<String, Object>>  selectAssignTypeEqui(Integer merid, String versions);

	/**
	 * @Description： 解绑设备并记录解绑信息
	 * @author： origin   2019年10月10日 下午3:40:45 
	 */
	Map<String, Object> unbindDevice(Integer operid, String devicenum);

	//===========================================================================================================
	/**
	 * separate
	 * @Description： 查询获取设备信息
	 * @param type:  1:正常设备	2:蓝牙设备 
	 * @author： origin 
	 */
	Object getDeviceData(HttpServletRequest request, Integer type);

	/**
	 * 根据条件分页导出设备数据
	 * @author zz
	 * @param request 参数
	 * @return {@link Object}
	 */
	Object exportDeviceData(HttpServletRequest request);
	/**
	 * separate
	 * @Description： 查询设备操作日志
	 * @author： origin 
	 */
	Object inquireDeviceOperationData(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 重置设备测试次数
	 * @author： origin 
	 */
	Object resetDeviceTestTime(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 设置设备版本号信息
	 * @author： origin 
	 */
	Object editDeviceHardversion(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 解绑设备
	 * @author： origin 
	 */
	Object disbindingDevice(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 绑定设备
	 * @author： origin 
	 */
	Object bindingDevice(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 查询商户名下同类型设备信息
	 * @author： origin   2019年11月21日 下午3:37:04
	 */
	Object inquireDeviceData(HttpServletRequest request);
	
	
	/**
	 * @method_name: updateDeviceTemplate
	 * @Description:  更改设备模板信息
	 * @param deviceList:设备号所属的list集合    tempid:模板id
	 * @return
	 * @Author: origin  创建时间:2020年8月22日 下午4:06:31
	 * @common:
	 */
	Object updateDeviceTemplate(HttpServletRequest request);

	/**
	 * employ 选择	ORIGIN
	 * @Description： 设置(修改)设备名字（备注）
	 * @author： origin   2019年12月17日 上午9:24:31 
	 */
	Object editDeviceName(HttpServletRequest request);

	/**
	 * separate
	 * @Description：搜索商户名下同类型设备信息（并按小区排序）
	 * @author： origin
	 * @createTime：2019年12月31日上午10:28:42
	 */
	Object searchDeviceData(HttpServletRequest request);
	
	/**
	 * 设备到期提醒
	 * @param parameters
	 * @return Object
	 */
	void equipmetExpireRemind();
	
	/**
	 * 更新设备到期时间增加一年
	 * @param code 设备号
	 */
	void updateExpirationTime(String code);
	
	/**
	 * 商家根据设备编号互换设备IMEI
	 * @param request
	 * @return {@link Object}
	 */
	Map<String, Object> merTranspositionImei(HttpServletRequest request);
	
	/**
	 * 管理员互换设备的IMEI
	 * @param request
	 * @return
	 */
	Object transpositionImei(HttpServletRequest request);
	
	/**
	 * 超级管理员定制设备号
	 * @param request
	 * @return
	 */
	Object customizationCode(HttpServletRequest request);

	/**
	 * @Description：搜索商户名下设备信息（并按小区排序）
	 * @param request
	 * @return
	 * @author： origin
	 * @createTime：2020年4月3日上午11:31:10
	 */
	Object searchDealerDeviceData(HttpServletRequest request);

	/**
	 * separate
	 * @Description：设备转移（从A名下转移到B名下）
	 * @author： origin
	 * @createTime：2020年4月3日下午4:09:00
	 */
	Object deviceDataTransfer(HttpServletRequest request);

	/**
	 * @Description：查询小区下的设备
	 * @param aid
	 * @author： origin2020年5月15日下午4:54:09
	 */
	List<Map<String, Object>> inquireAreaDeaviceInfo(Integer aid);
	
	
	//==============================================================================================================

	/*-- *** 请暂时不要在*号范围内添加   thanks ***********************************************************   --*/
	/**
	 * ORIGGIN  ANEW AFRESH
	 * @Description：根据设备号查询设备信息与设备关联信息(如:设备绑定人员信息、设备所属小区信息)
	 * @param devicenum:设备号
	 * @author： origin 2020年6月20日下午5:09:54
	 * @comment:
	 */
	Map<String, Object> getDeviceRelevanceInfo(String devicenum);
	/*-- ****************************************************************************************  --*/
	
	/**
	 * 银联APP扫设备编号(设备号或端口号)
	 * @param equCode 设备号
	 * @param codeAndPort 端口号
	 * @return {@link String}
	 */
	String unionScan(String equCode,String codeAndPort,Model model);
	
	/**
	 * 首页获取设备位置信息
	 * @return String
	 */
	String getLocationData();

	/**
	 * @method_name: inquireDeviceCount
	 * @Description: 查询设备计数信息【 查询所有设备数量 的在线数量、领先数量、绑定数量、未绑定数量】
	 * @param merid:商户id[查询所有时，商户id为null]
	 * @return
	 * @Author: origin  创建时间:2020年9月3日 上午10:43:58
	 * @common:   
	 */
	Map<String, Object> inquireDeviceCount(Integer merid);

	/**
	 * @method_name: getWarnParamsInfo
	 * @Description: 
	 * @param code
	 * @param merid
	 * @param i
	 * @param object
	 * @Author: origin  创建时间:2020年9月12日 下午5:59:43
	 * @common:   
	 */
	Map<String, Object> getWarnParamsInfo(String code, Integer merid, Integer opertype, Integer type);
	
	/**
	 * 添加充电桩

	 * @return
	 */
	Map<String, Object> insertEquipmentNew(EquipmentNew equ);

	/**
	 * @Author 修改充电桩信息
	 * @Description
	 * @Date 2020/10/11 19:33
	 * @Param [equ]
	 **/
	public Map<String, Object> updateEquipmentNew(EquipmentNew equ);

	/**
	 * 添加充电桩(设备主动上传)

	 * @return
	 */
	Map<String, Object> insertEquipmentNewData(String code, String hardversion, String softversion, String subHardversion,
			String subSoftversion, Integer dcModeltype, Integer dcModelnum, Integer dcModelpower, String location,
			BigDecimal lon, BigDecimal lat, String remark);

	/**
	 * 查询充电桩是否存在
	 * @param devicenum
	 * @return
	 */
	boolean selectDeviceExsit(String devicenum);
}
