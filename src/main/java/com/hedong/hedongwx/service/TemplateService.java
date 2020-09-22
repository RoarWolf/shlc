package com.hedong.hedongwx.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.User;

public interface TemplateService {
    
	//查询全部
	List<TemplateParent> getParentTemplateList();
	//添加
	//void insertParentTemplate(String name, Integer merchantid, String remark, String common1);
	void insertParentTemplate(HttpServletRequest request);
    //更改
	void updateParentTemplate(TemplateParent templateParent);
	//删除
	void deleteParentTemplate(Integer id);
	/** 通过商户id查询模板 */
	List<TemplateParent> getParentTemplateListByMerchantid(Integer merchantid, Integer status); 
	/** 通过商户id查询模板wolf版 */
	List<TemplateParent> getParentTemplateListByMerchantidwolf(Integer merchantid, Integer status); 
	/** 通过商户id只查询父模板wolf版 */
	List<TemplateParent> getParentTemplateListByMidwolf(Integer merchantid, Integer status);
	/** 根据用户id添加默认模板*/
	void insertDufaultTemplate(Integer uid);
	/** 判断用户是否有模板*/
	boolean merchantTempIfExist(Integer merchantid);
	

	List<TemplateParent> getParentTemforOffLine(Integer merchantid, Integer num);
	
	//查询全部
	List<TemplateSon> getSonTemplateList();
    //删除
	void deleteSonTempmanage(Integer id);
    //根据id查询单个
	TemplateSon getInfoTemplateOne(Integer id);
	
	//void deleteOffLine(Integer id);
	//query parenttemp by id
	TemplateParent getParentTemplateOne(Integer tempid);
	//添加
	void insertSonTemplate(TemplateSon templateSon);
	//更改
	String updateSonTemplate(HttpServletRequest request);
	
	//预览时查询子模板的内容
	List<TemplateSon> getSonTemp(Integer id);
	//根据设备code获取对应的模板
	List<TemplateSon> getEquSonTem(String code);
	
	//验证对应模板是否存在 status
	int verifyTemplate(Integer merid, Integer status);

	/**
	 * @Description： 根据函数插入离线模板
	 * @author： origin          
	 * @param id
	 */
	void insertOfflineTemp(Integer merid);
	
	/**
	 * @Description： 根据函数插入脉冲模板
	 * @author： origin          
	 * @param id
	 */
	void insertInCoinsTemp(Integer merid);

	/**
	 * @Description： 根据函数插入钱包模板
	 * @author： origin          
	 * @param id
	 */
	void insertWalletTemp(Integer merid);
	
	/**
	 * @Description： 根据函数插入在线卡模板
	 * @author： origin          
	 * @param id
	 */
	void insertOnlineTemp(Integer merid);
	
	/**
	 * @Description： 根据函数插入包月模板
	 * @author： origin          
	 * @param id
	 */
	void insertMonthTemp(Integer merid);
	
	/*** =====================================================================================================  */
	/**
	 * @Description：  添加一级充电模板
	 * @author： origin    
	 * @param walletpay2 
	 */
	void addStairCharge(Integer merid, String name, String brandName, String telephone,
			Integer standard, Integer permit, Integer WalletPay, Integer ifmonth);
	
	/**
	 * @Description： 修改充电模板的子类模板
	 * @author： origin   
	 */
	int updateSubclassCharge(Integer id, String name, Double money, String chargeTime, Integer chargeQuantity);
	
	/**
	  * @Description： 添加一级离线模板
	  * @author： origin   
	  */
	void addStairOffline(Integer merid, String name, String brandName, String telephone);
	
	/**
	 * @Description： 修改离线模板的子类模板
	 * @author： origin 
	 */
	int updateSubclassOffline(Integer id, String name, Double money, Double remark);

	/**
	  * @Description： 添加一级脉冲（投币）模板
	  * @author： origin   
	  */
	void addStairIncoins(Integer merid, String name, String brandName, String telephone, Integer permit, Integer walletpay);
	
	/**
	 * @Description： 修改脉冲（投币）模板的子类模板
	 * @author： origin 
	 */
	int updateSubclassIncoins(Integer id, String name, Double money, Double remark);
	
	/**
	  * @Description： 添加一级钱包模板
	  * @author： origin 
	  */
	void addStairWallet(Integer merid, String name, String brandName, String telephone);
	
	/**
	 * @Description： 修改钱包模板的子类模板
	 * @author： origin 
	 */
	int updateSubclassWwallet(Integer id, String name, Double money, Double remark);
	
	/**
	  * @Description： 添加一级在线卡模板
	  * @author： origin 
	  */
	void addStairOnline(Integer merid, String name, String brandName, String telephone);
	
	/**
	 * @Description： 修改在线卡模板的子类模板
	 * @author： origin 
	 */
	int updateSubclassOnline(Integer id, String name, Double money, Double remark);
	
	/**
	 * @Description： 模板的选择
	 * @author： origin          
	 * @param souce、obj、temid
	 * @return
	 */
	int templatechoice(Integer souce, String obj, Integer temid);
	
	/**
	 * @Description： 通过模板名查询一级模板信息
	 * @author： origin          
	 * 创建时间：   2019年5月27日 下午5:32:57 
	 */
	TemplateParent templateByName( Integer merid, Integer status, String name);
	
	/**
	 * @Description： 根据上级模板id和当前名字查询下级模板
	 * @author： origin          
	 * 创建时间：   2019年5月27日 下午5:58:03 
	 */
	TemplateSon subtemplatefind(Integer parid, String name);
	
	/**
	 * @Description： 模板的默认选择
	 * @author： origin          
	 * 创建时间：   2019年5月28日 下午3:04:39 
	 */
	Integer templateDefault( Integer merid, Integer source, Integer temid);
	
	/**
	 * @Description： 根据商户id和设备类型获取默认模板
	 * @author： origin          
	 * 创建时间：   2019年6月1日 上午11:55:01 
	 */
	Integer getDefaultPartemp(Integer merid, String hardversion);
	
	//========================================================================================================================================
	/**
	 * @Description： 添加充电分等级模板
	 * @author： origin 创建时间：   2019年8月2日 下午6:39:42 
	 */
	Map<String, Object> insertStairTempVal(String name, Integer merid, String remark, String common1, Integer status,
			Integer permit, Integer common2, Integer walletpay, Integer grade, Integer ifalipay,String chargeInfo);
	
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月2日 下午5:34:44 
	 */
	Map<String, Object> updateStairTemp(Integer id, String name, String remark, String common1, Integer permit, 
			Integer status, Integer common2, Integer walletpay, Integer grade);
	
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月2日 下午6:13:36 
	 */
	Map<String, Object> deleteStairTemp(Integer parid);
	
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月2日 下午6:33:07 
	 */
	Map<String, Object> deleteforcestairtemp(Integer parid);
	
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月3日 下午3:40:02 
	 */
	List<TemplateParent> selectStairTempVal(User user, Integer status);
	
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月3日 下午6:30:58 
	 */
	List<TemplateParent> getTempDetails(TemplateParent str);
	
	/**
	 * @Description：  merid, 0, temp.getId(), rank);
	 * @author： origin 创建时间：   2019年8月5日 下午3:06:58 
	 */
	TemplateParent templateGradeClassify(Integer merid, Integer status, Integer stairid, Integer rank);
	//-------------------------------------------------------------------------------------------------------
	/**
	 * @Description： 根据用户、商户、模板id、设备版本号判断获取对应的模板信息
	 * @author： origin 创建时间：   2019年8月12日 下午3:56:12 
	 */
	TemplateParent assignTempInfo(Integer id, Integer merid, Integer tempid, String hardversion);
	
	
	//========================================================================================================================================
	
	
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

	/**
	  * @Description： 添加一级包月模板
	  * @author： wolf 
	  */
	public void addPackageMonth(Integer merid, String name, String remark, String common1,Integer common2,String common3,Integer ifmonth);
	
	/**
	 * @Description： 修改包月模板的子类模板
	 * @author： wolf 
	 */
	public int updateSubclassPackageMonth(Integer id, String name, Double money, String common1);
	
	/**
	 * @Description： 根据设备类型获取指定模板
	 * @author： origin 创建时间：   2019年8月23日 下午5:35:51 
	 */
	List<TemplateParent> getAppointTempByType(String type, Integer merid);
	
	/**
	 * @Description： 根据设备类型信息和商户id查询指定类型设备使用的服务电话
	 * @param: val:数据信息，如设备版本号，小区id等    dealid:商户id、 type:类型
	 * @author： origin   2019年10月8日 下午3:54:55 
	 */
	Object genServicePhone(String devicenum, String val, Integer dealid, Integer status, Integer type);
	
	/**
	 * separate
	 * @Description： 查询设备模板信息
	 * @author： origin 
	 */
	Object inquireDeviceTemplateData(HttpServletRequest request);
	
	/**
	 * separate
	 * @Description： 修改主模板信息
	 * @author： origin 
	 */
	Object updateFirstTemplateData(HttpServletRequest request);
	
	/**
	 * separate
	 * @Description： 修改编辑子模板信息
	 * @author： origin 
	 */
	Object updateSecondTemplateData(HttpServletRequest request);
	
	/**
	 * separate
	 * @Description： 删除子模板信息
	 * @author： origin 
	 */
	Object deleteSecondTemplateData(HttpServletRequest request);
	
	/**
	 * separate
	 * @Description： 添加主模板
	 * @author： origin 
	 */
	Object insertFirstTemplateData(HttpServletRequest request);
	
	/**
	 * separate
	 * @Description： 添加子模板	
	 * @author： origin 
	 */
	Object insertSecondTemplateData(HttpServletRequest request);
	
	/**
	 * @Description： 查询分等级模板
	 * @author： origin   2019年12月21日 下午5:03:23 
	 */
	List<TemplateParent> templateGradeInfo(Integer dealid, Integer status, Integer tempid);
	
	/**
	 * @Description：
	 * @param request
	 * @author： origin
	 * @createTime：2020年3月17日下午1:50:34
	 */
	Object additionAssignTemp(HttpServletRequest request);
	
	/**
	 * @Description：添加v3主模板
	 * @author： origin
	 * @createTime：2020年3月17日下午1:50:34
	 */
	Object insertVDeviceTem(HttpServletRequest request);
	/**
	 * @Description：
	 * @param request
	 * @author： origin
	 * @createTime：2020年3月17日下午1:50:34
	 */
	Object updataVDeviceTem(HttpServletRequest request);
	/**
	 * @Description：
	 * @param request
	 * @author： origin
	 * @createTime：2020年3月17日下午2:59:46
	 */
	Object selectVDeviceTem(HttpServletRequest request);
	
//=== employ 选择 ======================================================================================================
	/**
	 * employ 选择	ORIGIN
	 * @Description：添加或修改v3设备模板（主模板含子模板）
	 * @author： origin
	 * @createTime：2020年3月20日上午10:53:34
	 */
	Object insertAmendTemp(HttpServletRequest request);
	
	/**
	 * employ 选择	ORIGIN
	 * @Description：根据主模板id查询单独一个主模板信息
	 * @author： origin
	 * @createTime：2020年3月18日下午1:43:11
	 */
	TemplateParent inquireDirectTemp(Integer tempid);
	
	/**
	 * employ 选择	ORIGIN
	 * @Description：根据商户id查询所有同类型主模板信息(且不是分等级的下级)
	 * @author： origin
	 * @createTime：2020年3月19日下午2:06:38
	 */
	List<TemplateParent> inquireTempByStatus(Integer dealid, Integer status);
	
	/**
	 * employ 选择	ORIGIN
	 * @Description：查询指定类型模板，如果有默认，则获取默认模板，没有默认获取第一个；如果不存在，则返回实体类空对象
	 * @author： origin
	 * @createTime：2020年3月19日下午2:06:38
	 */
	TemplateParent inquireTempData(Integer dealid, Integer status);
	
	/**
	 * employ 选择	ORIGIN
	 * @Description：根据主模板id【tempid】查询所有子模板信息
	 * @author： origin
	 * @createTime：2020年3月19日下午2:06:38
	 */
	List<TemplateSon> getSonTemplateLists(Integer id);
	
	/**
	 * @Description：如果模板不存在，复制添加默认模板到商户名下
	 * @param status  dealid  servePhone
	 * @author： origin
	 * @createTime：2020年3月24日上午11:58:42
	 */
	Map<String, Object> copyDirectTemp(Integer status, Integer dealid, String servePhone);
	
	/**
	 * @Description：电脑端添加或修改v3设备模板（主模板含子模板）
	 * @author： origin
	 * @createTime：2020年4月11日下午2:50:36
	 */
	Object insertOrModifyTemp(HttpServletRequest request);
	
	/**
	 * @Description：查询获取到系统模板
	 * @author： origin ：2020年7月2日下午2:37:30
	 * @comment:
	 */
	Map<String, Object> inquireSystemTempData(Integer status, String version);
//==================================================================================================================	
	
	/**
	 * 根据模板和金额计算应下发的时间
	 * @param tempparid
	 * @param money
	 * @return
	 */
	short clacV3TimeBytemp(Integer tempparid,Double money);
	
	/**
	 * 根据模板选择默认模板
	 * @param tempson 子模板
	 * @return {@link Map}
	 */
	 Map<String, Object> tempDefaultObje(List<TemplateSon> tempson);
	 
	 
	/**
	 * @method_name: additionTempData
	 * @Description: 添加模板信息【主模板和子模板一起添加】
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月18日 下午2:07:15
	 * @common:   
	 */
	Object additionAndEditTemp(HttpServletRequest request);
	
	/**
	 * @method_name: editTempForInfo
	 * @Description：修改模板充电提示信息
	 * @author： origin   2019年12月3日 上午9:56:44 
	 * @param request
	 * @Author: origin  创建时间:2019年12月3日 上午9:56:44 
	 * @common:
	 */
	Object editTempForInfo(HttpServletRequest request);
	
	/**
	 * @method_name: additionTempSonData
	 * @Description: 模板处理： 单独添加子模板
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月20日 下午3:16:06
	 * @common:   
	 */
	Object additionTempSonData(HttpServletRequest request);
	
	/**
	 * @method_name: deleteTemplateSonData
	 * @Description: 删除子模板信息
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月20日 下午4:32:32
	 * @common:
	 */
	Object deleteTemplateSonData(HttpServletRequest request);
	

	/**
	 * separate
	 * @Description： 删除所有模板信息（主模板、子模板）（如果使用，不能删除）
	 * @author： origin 
	 */
	Object deleteTemplateData(HttpServletRequest request);
	
	/**
	 * separate
	 * @Description： 选中模板信息
	 * @author： origin 
	 */
	Object pitchOnTemplate(HttpServletRequest request);
	
	/**
	 * separate
	 * @Description： 选中默认模板信息
	 * @author： origin 
	 */
	Object defaultPitchOnTemplate(HttpServletRequest request);
	
	/**
	 * @method_name: templataPreview
	 * @Description: 
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月24日 下午5:14:56
	 * @common:   
	 */
	Map<String, Object> templataPreview(HttpServletRequest request);
}
