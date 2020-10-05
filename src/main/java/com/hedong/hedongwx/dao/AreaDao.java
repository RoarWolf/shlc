package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.AreaRelevance;
import com.hedong.hedongwx.entity.Areastatistics;
import com.hedong.hedongwx.entity.Parameters;

public interface AreaDao {

	/**
	 * 添加小区信息
	 * @param area 小区
	 * @return {@link Integer}
	 */
	int insertArea(Area area);

	/**
	 * 根据条件查询小区信息
	 * @param area
	 * @return {@link List}
	 */
	List<Area> selectByArea(Area area);

	/**
	 * 根据小区id查询小区信息
	 * @param id 小区id
	 * @return {@link Area}
	 */
	Area selectByAreaId(Integer id);

	/**
	 *
	 * @param parame
	 * @return
	 */
	List<Map<String, Object>> selectByParame(Parameters parame);

	/**
	 *
	 * @param area
	 * @return
	 */
	int deleteByArea(Area area);

	/**
	 *
	 * @param area
	 * @return
	 */
	Integer updateByArea(Area area);

	/**
	 *
	 * @param id
	 * @return
	 */
	Area selectByIdArea(@Param("id")Integer id);

	/**
	 *
	 * @return
	 */
	int everydayAreaReset();

	/**
	 *
	 * @param areastatistics
	 * @return
	 */
	int insertAreastatistics(Areastatistics areastatistics);

	/**
	 *
	 * @param aid
	 * @param beginnum
	 * @param neednum
	 * @return
	 */
	List<Areastatistics> selectAreastatisticsByAid(@Param("aid")Integer aid,@Param("beginnum")Integer beginnum,
			@Param("neednum")Integer neednum);


	/**
	 * 通过商户id小区在线总计和当日总计
	 * @param parameter
	 * @return
	 */
	int updateAreaEarn(@Param("edittype")Integer edittype,@Param("money")Double money,@Param("id")Integer id,
			@Param("wallet")Double wallet,@Param("equ")Double equ,@Param("card")Double card);


	/**
	 * @Description：修改小区中钱包模板
	 * @author： origin          
	 * 创建时间：   2019年5月29日 下午4:21:28 
	 */
	Integer updatetempid(@Param("price")Integer price, @Param("tempid")Integer tempid);

	/**
	 * @Description： 修改小区中在线卡模板
	 * @author： origin          
	 * 创建时间：   2019年5月29日 下午4:21:34 
	 */
	Integer updatetotempid(@Param("price")Integer price, @Param("tempid")Integer tempid);

	/**
	 * @author origin 更改合伙人状态
	 * @param manid
	 * @param divideinto
	 * @param aid
	 * @return
	 */
	Integer updatepartner(@Param("manid")Integer manid, @Param("divideinto")Integer divideinto, @Param("aid")Integer aid);

	/**
	 *
	 * @return
	 */
	List<Area> selectAllArea();

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月19日 下午6:32:37 
	 */
	Map<String, Object> selectAreastatisticsByParam(Parameters paramet);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月22日 下午4:06:47 
	 */
	List<Map<String, Object>> selectAreastatisticsGroup(Parameters params);

	/**
	 * @Description： 查询小区关联的信息
	 * @author： origin 创建时间：   2019年8月31日 下午3:08:09 
	 */
	AreaRelevance selectAreaRelSole(AreaRelevance arearele);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月31日 下午3:56:57 
	 */
	void insertAreaReleInfo(AreaRelevance arearele);

	/**
	 * @Description： 删除小区合伙人(id：小区合伙关联记录 表id【hd_arearelevance】)
	 * @author： origin 创建时间：   2019年8月31日 下午4:56:27 
	 * @return 
	 */
	Integer deleteAreaReleById(Integer relatid);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月31日 下午5:01:42 
	 */
	void updateArReleById(AreaRelevance arearele);

	/**
	 * @Description：
	 * @author： origin 创建时间：   2019年9月4日 上午11:36:24 
	 * @param type 
	 */
	AreaRelevance selectAreaRelAlone(@Param("aid")Integer aid, @Param("partid")Integer partid, 
			@Param("type")Integer type, @Param("sort")Integer sort);

	/**
	 * @Description： 转移合伙人，从hd_area表查出原来含有的合伙人id信息
	 * @author： origin 创建时间：   2019年9月12日 下午2:49:30 
	 */
	List<Area> selectMainForArea();

	/**
	 * @Description： 根据实体类删除合伙人信息(id、小区id、合伙人id)
	 * @author： origin 创建时间：   2019年9月27日 上午11:11:26 
	 */
	void deleteArReleByPara(AreaRelevance arearele);

	/**
	 * @Description： 
	 * @param: 
	 * @author： origin   2019年9月29日 下午5:21:36 
	 */
	List<Map<String, Object>>  areaRelPartidInfo();

	/** 
	 * newest WeChat
	 * @Description： 查询商户的小区信息
	 * @author： origin   2019年11月29日 下午5:45:49 
	 */
	List<Map<String, Object>> inquireDealAreaData(Parameters parame);
	
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年9月5日 下午4:17:16 
	 */
	List<Map<String, Object>> selectaRearelInfo(Parameters parame);

	/**
	 * employ 选择	ORIGIN
	 * @Description：查询多条小区关联的信息
	 * @author： origin
	 * @createTime：2020年3月21日下午5:35:33
	 */
	List<Map<String, Object>> inquireAreaRelevancInfo(Parameters parame);
	
	/**
	 * ZZ4/27
	 * 根据小区的id查询设备数,快到期设备数,已到期设备数
	 * @return
	 */
	Map<String, Object> selectEquDataByaid(@Param("aid")Integer aid);

	/**
	 * employ 选择	ORIGIN
	 * @Description：根据小区id查询单个小区相关信息
	 * @param aid
	 * @author： origin ：2020年5月15日下午3:48:03
	 */
	Map<String, Object> inquireAreaInfo(Parameters parame);

	/**
	 * @Description：
	 * @author： origin ：2020年5月15日下午5:05:58
	 */
	Map<String, Object> inquireAreaUser(@Param("aid")Integer aid);

	/**
	 * @Description：
	 * @author： origin ：2020年5月15日下午5:05:58
	 */
	Integer inquireAreaDevicenum(@Param("aid")Integer aid);

	/**
	 * @Description：
	 * @author： origin ：2020年5月15日下午5:05:58
	 */
	Map<String, Object> inquireAreaOnlineCard(@Param("aid")Integer aid);
	
	List<Area> queryAreaRecently(@Param("lon")Double lon,@Param("lat")Double lat,@Param("distance")Double distance, @Param("startnum")Integer startnum, @Param("distanceSort")Integer distanceSort);
	
	List<Map<String, Object>> queryAreaRecently2(@Param("lon")Double lon,@Param("lat")Double lat,@Param("distance")Double distance, @Param("startnum")Integer startnum, @Param("distanceSort")Integer distanceSort);
}
