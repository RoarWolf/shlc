package com.hedong.hedongwx.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.AreaRelevance;
import com.hedong.hedongwx.entity.Areastatistics;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.utils.PageUtils;

public interface AreaService {

	int insertArea(Area area);
	
	List<Area> selectAllArea();
	
	//通过Parameter查询设备地点（小区）所有信息
	PageUtils<Parameters> selectByParame(HttpServletRequest request);
	
	//通过实体类 Area 查询设备地点（小区）所有信息
	List<Area> selectByArea(Area area);
	
	//根据小区id查询小区信息
	Area selectByAreaId(Integer id);
	
	//通过小区id删除小区信息并删除合伙人及小区管理员的信息
	void deleteAreaByAid(Integer aid);
	
	//通过实体类 Area 传入条件 修改 设备地点（小区）的信息
	int updateByArea(Area area);
	
	/**
	 * 修改小区收益
	 * @param edittype
	 * @param money
	 * @param id
	 * @param wallet
	 * @param equ
	 * @param card
	 * @return
	 */
	int updateAreaEarn(Integer edittype, Double money, Integer id,Double wallet,Double equ,Double card);
	
	/**
	 * 每日重置在线和投币收益
	 * @return
	 */
	int everydayAreaReset();
	
	/**
	 * 添加小区每日汇总
	 * @param aid
	 * @param onlineEarn
	 * @param coinEarn
	 * @param walletEarn
	 * @param equEarn
	 * @param cardEarn
	 * @param createTime
	 * @return
	 */
	int insertAreastatistics(Integer aid,Double onlineEarn,Integer coinEarn,Double walletEarn,
			Double equEarn,Double cardEarn,Date createTime);
	
	List<Areastatistics> selectAreastatisticsByAid(Integer aid,Integer beginnum,Integer neednum);

	/**
	 * @Description： 根据实体类条件查询小区信息
	 * @author： origin 
	 */
	List<Area> selectAreaList(Area area);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月12日 上午10:08:00 
	 */
	Map<String, Object> insetAreaInfo(User user, String name, String address);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月12日 上午11:57:51 
	 */
	Map<String, Object> updateByArea(User user, Integer aid, String name, String address, String phone, Double divideinto);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年9月5日 下午4:09:08 
	 */
	List<Map<String, Object>> selectAreaRelev(Integer aid);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年9月5日 下午5:29:17 
	 */
	List<Area> selectPartAreaById(Integer uid);

	/**
	 * @Description： 转移合伙人，从hd_area表查出原来含有的合伙人id信息
	 * @author： origin 创建时间：   2019年9月12日 下午2:49:30 
	 */
	List<Area> selectMainForArea();

	/**
	 * @Description： 根据小区id与合伙人id将合伙人数据存储进入对应表中
	 * @author： origin 创建时间：   2019年9月12日 下午3:00:20 
	 */
	void increasePartner(Integer aid, Integer mainid, Double percent);

	/**
	 * @Description： 根据小区id和类型查询小区合伙人或管理信息
	 * @author： origin 创建时间：   2019年9月12日 下午3:43:58 
	 */
	List<Map<String, Object>>  inquirePartnerInfo(Integer aid, Integer type);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年9月25日 上午11:08:27 
	 */
	void insertAreaReleInfo(Integer aid, Integer partid, Double percent,Integer sort, Integer type, Integer status, 
			String remark, Date datatime, Date datatime2);

	/**
	 * employ 选择	ORIGIN
	 * @Description： 绑定小区合伙人(aid:小区id   type: 类型 1管理员  2合伙人  phone:添加对象的电话  percent:分成比)
	 * @author： origin 创建时间：   2019年9月27日 上午11:58:38 
	 */
	Map<String, Object> bindAreaPartner(Integer aid, Integer type, String phone, Double percent);

	/**
	 * employ 选择	ORIGIN
	 * @Description：删除小区合伙人(id：小区合伙关联记录 表id【hd_arearelevance】)
	 * @author： origin 创建时间：   2019年9月27日 上午11:42:00
	 */
	Map<String, Object> removeAreaPartner(Integer id);

	/**
	 * employ 选择	ORIGIN
	 * @Description： 编辑修改小区信息
	 * @author： origin 创建时间：   2019年9月27日 下午3:47:08
	 */
	Object editAreaInfo(Integer aid, String name, String address);

	/**
	 * employ 选择	ORIGIN
	 * @Description： 修改绑定小区合伙人的分成比(aid:小区id   percent:分成比)
	 * @author： origin 创建时间：   2019年9月27日 上午11:42:00
	 */
	Map<String, Object> editBindAreaPartner(Integer id, Double percent);

	/**
	 * @Description： 修改绑定小区的管理人员(aid:小区id   type: 类型 1管理员  2合伙人  phone:添加对象的电话  percent:分成比)
	 * @author： origin  2019年9月30日 下午2:26:16 
	 */
	Object editBindAreaPartner(Integer id, Integer aid, Integer type, String phone);

	/**
	 * @Description： 绑定小区的管理人员(aid:小区id  phone:添加对象的电话  nickname:昵称 name:姓名 )
	 * @author： origin 创建时间：   2019年9月27日 上午11:42:00
	 */
	Object bindAreaPartner(Integer aid, String nickname, String phone, String name);

	/**
	 * @Description： 更改小区服务电话
	 * @author： origin   2019年10月10日 下午5:25:55 
	 */
	Map<String, Object> editAreaServicePhone(Integer id, Integer aid, String nick, String name, String mobile);

	
	//=====================================================================================================
	
	/**
	 * employ 选择	ORIGIN
	 * @Description：查询单条记录
	 * @param areaid
	 * @return  返回实体类小区信息
	 * @author： origin   2020年3月24日下午5:17:22
	 */
	Area selectByIdArea(Integer areaid);
	
	/**
	 * separate
	 * @Description：小区管理信息
	 * @author： origin 
	 */
	Object areaManageData(HttpServletRequest request);

	/** 
	 * newest WeChat
	 * @Description： 查询商户名下的小区信息 
	 * @author： origin   2019年11月29日 下午5:45:49 
	 */
	List<Map<String, Object>> inquireDealAreaData(Integer dealid);

	/**
	 * employ 选择	ORIGIN
	 * @Description：根据小区id查询单个小区相关信息
	 * @param aid
	 * @author： origin ：2020年5月15日下午3:48:03
	 */
	Map<String, Object> inquireSingleAreaInfo(Integer aid);

	/**
	 * @Description：
	 * @author： origin ：2020年5月15日下午5:05:38
	 */
	Map<String, Object>  inquireAreaOnlineCard(Integer aid);

	/**
	 * @Description：
	 * @author： origin 2020年5月15日下午5:05:52
	 */
	Integer inquireAreaDevicenum(Integer aid);

	/**
	 * @Description：
	 * @author： origin ：2020年5月15日下午5:05:58
	 */
	Map<String, Object>  inquireAreaUser(Integer aid);

	/**
	 * @param type 
	 * @Description：设置小区相关模板
	 * @author： origin 2020年5月19日下午3:32:16
	 */
	Map<String, Object> areaTempDirectUpdate(Integer aid, Integer tempid, Integer type);
	
	/**
	 * @author origin
	 * @param merid
	 * @return 根据id查询该商户是否为合伙人
	 */
	Map<String, Object> inquireAreaService(Integer merid);

	/**
	 * 
	 * @param code
	 * @param money
	 * @param merid
	 * @param aid
	 * @param type
	 * @param status
	 */
	void addAPartEarn(String code, double money, Integer merid, Integer aid, int type, int status);
	
	//=====================================================================================================
	
	Map<String,Object> queryAreaRecently(Double lon, Double lat, Double distance, Integer startnum);
}
