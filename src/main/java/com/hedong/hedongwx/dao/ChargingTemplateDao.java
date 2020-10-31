package com.hedong.hedongwx.dao;

import com.hedong.hedongwx.entity.ChargingTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChargingTemplateDao {

	List<ChargingTemplate> selectAllTemplate(@Param("parentId") Integer parentId);

	ChargingTemplate selectAllTemplateById(@Param("id") Integer id);

	/**
	 * 添加计费模板
	 * @param chargingTemplate
	 * @return
	 */
	int insertCharging(ChargingTemplate chargingTemplate);
	
	/**
	 * 修改计费模板
	 * @param chargingTemplate
	 * @return
	 */
	int updateChargingById(ChargingTemplate chargingTemplate);

	/**
	 * 修改计费模板默认状态
	 * @param chargingTemplate
	 * @return
	 */
	int updateChargingStatus();

	/**
	 * 删除计费模板
	 * @param id
	 * @return
	 */
	int delTemplateById(@Param("id") Integer id);

	/**
	 * 删除计费模板
	 * @param parentId
	 * @return
	 */
	int delTemplateByParentId(@Param("parentId") Integer parentId);

}
