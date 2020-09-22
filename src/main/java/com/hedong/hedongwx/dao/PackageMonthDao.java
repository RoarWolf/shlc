package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.PackageMonth;
import com.hedong.hedongwx.entity.PackageMonthRecord;
import com.hedong.hedongwx.entity.Parameters;

public interface PackageMonthDao {

	//---------------------包月信息------------------------------------
	PackageMonth selectPackageMonthByUid(@Param("uid") Integer uid);
	
	int insertPackageMonth(PackageMonth packageMonth);
	
	int updatePackageMonth(PackageMonth packageMonth);
	
	int everydaynumReset();
	
	//----------------------包月记录------------------------------------
	int insertPackageMonthRecord(PackageMonthRecord packageMonthRecord);
	
	int updatePackageMonthRecord(PackageMonthRecord packageMonthRecord);
	
	PackageMonthRecord selectMonthRecordByOrdernum(@Param("ordernum") String ordernum,@Param("status") Integer status);
	
	List<PackageMonthRecord> selectMonthRecordByEntiy(PackageMonthRecord packageMonthRecord);
	
	List<PackageMonthRecord> selectMonthRecordListByUid(Integer uid);
	
	PackageMonthRecord selectMonthRecordById(Integer id);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月18日 下午6:51:52 
	 */
	List<Map<String, Object>> selectMonthRecordByParam(Parameters parame);
	
}
