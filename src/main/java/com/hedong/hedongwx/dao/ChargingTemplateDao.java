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
	 * 删除计费模板
	 * @param id
	 * @return
	 */
	int delTemplateById(@Param("id") Integer id,@Param("parentId") Integer parentId);

}
