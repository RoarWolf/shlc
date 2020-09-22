package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.FeescaleRecord;

public interface FeescaleDao {
	
	//根据订单号查询缴费的详情
	List<Map<String, Object>> selectFeescaleDetails(@Param("ordernum")String ordernum);
	
	//查询总缴费的金额
	Map<String, Object> selectFeescaleTotalEarnings();
	
	// 插入设备收费标准信息
	void insertFeescal(@Param("merid") Integer merid, @Param("mername") String mername,
			@Param("netType") String nettype, @Param("equipmentType") String equipmenttype,@Param("feescale") Double feescale);

	// 系统的收费标准
	List<Map<String, Object>> getSystemFeescale();
	
	// 超级管理员修改系统模板
	void updateSystemFeescale(FeescaleRecord list);

	// 根据商家的id查询，是否有收费模板
	Map<String, Object> selectMerFeescaleByid(@Param("merid") Integer merid, @Param("netType") String netType,
			@Param("equipmentType") String equipmentType);

	// 根据商家的id查询所有设备类型的收费
	List<Map<String, Object>> getFeescalBymerid(@Param("merid") Integer merid);

	// 根据商家的id修改设备类型的收费标准
	void updateFeescaleBymerid(@Param("merid") Integer merid, @Param("netType") String netType,
			@Param("equipmentType") String equipmentType, @Param("feescale") Double feescale);

	// 商家设置开启自动续费和开启分摊
	void updateSwitch(@Param("merid") Integer merid, @Param("autoPay") Integer autoPay,
			@Param("apportion") Integer apportion,@Param("autoWithdraw") Integer autoWithdraw);

	// 超级管理员设置商家的负收入值
	void setMerMaxNegative(@Param("merid") Integer merid, @Param("maxNegative") Double maxNegative);

	// 更改设备的到期时间
	void setEquipmentExpire(@Param("code") String code, @Param("expirationTime") String expirationTime);
	
	// 根据小区id获取合伙人的数量
	Integer getAllPartner(@Param("id")Integer areaId);
	
	// 添加缴费记录信息
	void insertFeescaleRecord(FeescaleRecord feescaleRecord);
	
	// 获取缴费记录的总条数
	Integer selectFeescaleRecordCount(FeescaleRecord feescaleRecord);
	
	// 根据各种条件查看消费信息
	List<Map<String, Object>> selectFeescaleRecord(FeescaleRecord feescaleRecord);
	
	// 手机端普通用户查询缴费记录
	List<Map<String, Object>> MerSelectFeescaleRecord(FeescaleRecord feescaleRecord);
	
	// 用户的订单号
	List<Map<String, Object>> selectFeescaleRecordNum(FeescaleRecord feescaleRecord);
	
	//根据订单号变更订单的状态
	void updateFeescaleRecordStatue(@Param("ordernum")String ordernum);
	
	//根据订单号查询微信缴费记录
	List<Map<String, Object>> selectWxFeescaleRecord(@Param("ordernum")String ordernum);
	

	
}
