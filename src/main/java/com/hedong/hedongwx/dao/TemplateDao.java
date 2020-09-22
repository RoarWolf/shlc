package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.TemplateParent;

public interface TemplateDao {
	
	/**
	 * 查询指定对象模板信息（父类）
	 * @return
	 */
	List<TemplateParent> getParentTemplateList();

	//void insertParentTemplate(TemplateParent templateParent);

	Integer updateParentTemplate(TemplateParent templateParent);

	void deleteParentTemplate(Integer id);

	TemplateParent getParentTemplateOne(Integer id);
	
	List<TemplateParent> getParentTemplateListByMerchantid(@Param("merchantid")Integer merchantid, @Param("status")Integer status);
	
	List<TemplateParent> getParentTemplateListByMerchantidwolf(@Param("merchantid")Integer merchantid, @Param("status")Integer status);
	
	void insertDufaultTemplate(Integer uid);

	List<TemplateParent> getParentTemforOffLine(@Param("num")Integer num,@Param("merchantid")Integer merchantid, @Param("status")Integer status);

	//判断对应商户的模板数量
	int verifyTemplate(@Param("merid")Integer merid, @Param("status")Integer status);

	//根据函数插入离线模板
	void insertOfflineTemp(Integer merid);

	//根据函数插入脉冲模板
	void insertInCoinsTemp(Integer merid);
	
	//根据函数插入包月模板
	void insertMonthTemp(Integer merid);

	/**
	 * @Description： 根据函数据添加钱包模板
	 * @author： origin          
	 * @param merid
	 */
	void insertWalletTemp(Integer merid);

	/**
	 * @Description： 根据函数添加在线卡模板
	 * @author： origin          
	 * @param merid
	 */
	void insertOnlineTemp(Integer merid);
	
	void insertClassifyTem(@Param("temone")String temone,@Param("temtwo")String temtwo,@Param("temthree")String temthree,
			@Param("merid")Integer merid,@Param("status")Integer status,@Param("permit")Integer permit,
			@Param("walletype")Integer walletype,@Param("grade")Integer grade,@Param("remark")String remark,
			@Param("common")String common,@Param("default")Integer defaul);
	
	/**
	 * @Description： 通过模板名查询一级模板信息
	 * @author： origin          
	 * 创建时间：   2019年5月27日 下午5:32:57 
	 */
	TemplateParent templateByName(@Param("merid")Integer merid, @Param("status")Integer status, @Param("name")String name);

	/**
	 * @Description： 根据类型、商户修改默认
	 * @author： origin          
	 * 创建时间：   2019年5月28日 下午5:28:40 
	 */
	void updatepartemcom(@Param("status")Integer source, @Param("common3")String string, @Param("merid")Integer merid);

	/**
	 * @Description： 根据商户id和设备类型获取默认模板
	 * @author： origin          
	 * 创建时间：   2019年6月1日 下午1:52:37 
	 */
	List<TemplateParent> selectpartemp(TemplateParent temp);

	//=======================================================================================================
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月2日 下午5:12:20 
	 */
	Integer insertStairTemp(TemplateParent template);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月3日 上午11:09:57 
	 * @param i 
	 */
	TemplateParent templateGradeClassify(@Param("merchantid")Integer merchantid, @Param("status")Integer status, 
			@Param("stairid")Integer stairid, @Param("rank")Integer rank);

	/**
	 * @Description： 查询分等级模板
	 * @author： origin 创建时间：   2019年8月3日 下午5:13:06 
	 */
	List<TemplateParent> templateGradeInfo(@Param("merchantid")Integer merchantid, @Param("status")Integer status, 
			@Param("stairid")Integer stairid);
	
	/*-- *** 请暂时不要在*号范围内添加   thanks ***********************************************************   --*/
	/**
	 * ORIGGIN  ANEW AFRESH
	 * @Description：ORIGGIN  ANEW AFRESH  根据参数获取主模板信息 返回数据类型为list<实体类>多条数据
	 * @param id:模板id  merid:商户id  stairid:分等级使用，默认为0
	 * @param status: 模板类型
	 * @return 返回数据类型为list<实体类>多条数据
	 * @author： origin 	2020年7月2日下午6:09:45
	 */
	List<TemplateParent> queryStairTempListInfo(@Param("id")Integer id, @Param("merid")Integer merid, @Param("status")Integer status,  @Param("stairid")Integer stairid);
	
	/**
	 * ORIGGIN  ANEW AFRESH
	 * @Description：ORIGGIN  ANEW AFRESH 根据参数获取主模板信息 返回数据类型为 实体类（单条）数据
	 * @param id:模板id  merid:商户id  stairid:分等级使用，默认为0
	 * @param status: 模板类型
	 * @return 返回数据类型为 实体类（单条）数据
	 * @author： origin 	2020年7月2日下午6:09:45
	 */
	TemplateParent searchStairTempOneInfo(@Param("id")Integer id, @Param("merid")Integer merid, @Param("status")Integer status,  @Param("stairid")Integer stairid);
	
	/**
	 * ORIGGIN  ANEW AFRESH
	 * @Description：ORIGGIN  ANEW AFRESH  根据参数获取主模板信息 返回数据类型为list<map>多条数据
	 * @param id:模板id  merid:商户id  stairid:分等级使用，默认为0
	 * @param status: 模板类型
	 * @return 返回数据类型为list<map>多条数据
	 * @author： origin 	2020年7月2日下午6:09:45
	 */
	List<Map<String, Object>> inquireStairTempListInfo(@Param("id")Integer id, @Param("merid")Integer merid, @Param("status")Integer status,  @Param("stairid")Integer stairid);
	
	/**
	 * ORIGGIN  ANEW AFRESH
	 * @Description：ORIGGIN  ANEW AFRESH 根据参数获取主模板信息 返回数据类型为 map（单条）数据
	 * @param id:模板id  merid:商户id  stairid:分等级使用，默认为0
	 * @param status: 模板类型
	 * @return 返回数据类型为 map（单条）数据
	 * @author： origin 	2020年7月2日下午6:09:45
	 */
	Map<String, Object> selectStairTempOneInfo(@Param("id")Integer id, @Param("merid")Integer merid, @Param("status")Integer status,  @Param("stairid")Integer stairid);
	
	
	/**
	 * ORIGGIN  ANEW AFRESH
	 * @Description：获取对应商户名下该类型的主模板数量
	 * @param merid:商户id 	status: 模板类型
	 * @return Integer 类型的数量值
	 * @author： origin 	2020年7月2日下午6:13:50
	 */
	Integer acquireTempNumber(@Param("merid")Integer merid, @Param("status")Integer status);
	/*-- *** 请暂时不要在*号范围内添加   thanks ***********************************************************   --*/
	
	//=======================================================================================================
	
	
}
