package com.hedong.hedongwx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.TemplateSon;

public interface TemplateSonDao {
	
	
	
	/**
	 * 查询指定对象模板信息（子）
	 * @return
	 */


	Integer insertSonTemplate(TemplateSon templateSon);
	
	void deleteSonTempmanage(Integer id);

	void delSonTempmanage(Integer id);

	void updateSonTemplate(TemplateSon templateSon);

	List<TemplateSon> getSonTemplateList();
	
	List<TemplateSon> getSonTemplateLists(@Param(value="id")Integer id);

	TemplateSon getInfoTemplateOne(Integer id);

	List<TemplateSon> getEquSonTem(String code);

	/**
	 * @Description： 根据上级模板id和当前名字查询下级模板
	 * @author： origin          
	 * 创建时间：   2019年5月27日 下午6:00:09 
	 */
	TemplateSon subtemplatefind(@Param("temid")Integer parid, @Param("name")String name);

	/**
	 * @method_name: insertTemplateSon
	 * @Description: 
	 * @param tempson
	 * @Author: origin  创建时间:2020年8月19日 下午3:11:38
	 * @common:   
	 */
	Integer insertTemplateSon(TemplateSon tempson);	

	
	
	
	
	
	

}
