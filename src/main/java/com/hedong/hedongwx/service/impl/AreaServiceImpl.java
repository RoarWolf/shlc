package com.hedong.hedongwx.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.AreaDao;
import com.hedong.hedongwx.dao.EquipmentDao;
import com.hedong.hedongwx.dao.OnlineCardDao;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.AreaRelevance;
import com.hedong.hedongwx.entity.Areastatistics;
import com.hedong.hedongwx.entity.ChargeRecordCopy;
import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.UserService;

import net.sf.json.JSONObject;

@Service
public class AreaServiceImpl implements AreaService {
	
	public final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private EquipmentDao equipmentDao;
	@Autowired
	private OnlineCardDao onlineCardDao;
	@Autowired
	private UserService userService;
	

	@Override
	public int insertArea(Area area) {
		areaDao.insertArea(area);
		Integer aid = CommUtil.toInteger(area.getId());
		String areaOnlyCode = DisposeUtil.completeNum(aid.toString(),6);
		Area updateArea = new Area();
		area.setId(aid);
		area.setAreaOnlyCode(areaOnlyCode);
		return areaDao.updateByArea(updateArea);
	}
	
	//通过Parameter查询设备地点（小区）所有信息
	@Override
	public PageUtils<Parameters> selectByParame(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters param = new Parameters();

		User user = CommonConfig.getAdminReq(request);
		Integer rank = user.getLevel();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(rank!=0) param.setUid(user.getId());//获取用户
		param.setRemark(request.getParameter("name"));//小区名称
		param.setSource(request.getParameter("address"));//小区地址
		param.setDealer(request.getParameter("dealer"));//关联商户名称
		param.setMobile(request.getParameter("phoneNum"));//商户电话
		param.setRealname(request.getParameter("manarealname"));//副号名称
		param.setPhone(request.getParameter("manaphonenum"));//副号点
		param.setStartTime(request.getParameter("startTime"));
		param.setEndTime(request.getParameter("endTime"));
		List<Map<String, Object>> trade = areaDao.selectByParame(param);
		page.setTotalRows(trade.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		param.setPages(page.getNumPerPage());
		param.setStartnumber(page.getStartIndex());
		List<Map<String, Object>> tradepage = areaDao.selectByParame(param);
		for(Map<String, Object> item : tradepage){
			String tnumber = equipmentDao.selectEquCountByAid(StringUtil.getIntString(item.get("id").toString()));
			item.put("tnumber", tnumber);
			OnlineCard online = new OnlineCard();
			online.setAid((Integer) item.get("id"));
			Integer onlincount = onlineCardDao.onlinecount(online);
			item.put("onlincount", onlincount);
		}
		page.setListMap(tradepage);
		return page;
	}

	//通过实体类 Area 查询设备地点（小区）所有信息
	@Override
	public List<Area> selectByArea(Area area) {
		List<Area> aread =  areaDao.selectByArea(area);
		aread = aread == null ? new ArrayList<Area>() : aread;
		for(Area item : aread){
			String num = equipmentDao.selectEquCountByAid(item.getId());
			item.setEqucount(num);
		}
		return aread;
	}
	
	//通过实体类 Area 传入条件 删除 设备地点（小区）的信息
	@Override
	public void deleteAreaByAid(Integer aid) {
		try {
			Area area = new Area();
			area.setId(CommUtil.toInteger(aid));
			areaDao.deleteByArea(area);
		/*
			AreaRelevance arearele = new AreaRelevance();
			arearele.setAid(CommUtil.toInteger(aid));
			areaDao.deleteArReleByPara(arearele);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//通过实体类 Area 传入条件 修改 设备地点（小区）的信息
	@Override
	public int updateByArea(Area area) {
		return areaDao.updateByArea(area);
	}

	@Override
	public Area selectByIdArea(Integer id) {
		Area result = areaDao.selectByIdArea(id);
		return result = result == null ? new Area() : result ;
	}

	/**
	 * @Description： 根据实体类条件查询小区信息
	 * @author： origin 
	 */
	@Override
	public List<Area> selectAreaList(Area area) {
		return areaDao.selectByArea(area);
	}

	@Override
	public Map<String, Object> insetAreaInfo(User user, String name, String address) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(user == null) return CommonConfig.messg(400);
			Area area = new Area();
			area.setName(CommUtil.toString(name));
			area.setMerid(CommUtil.toInteger(user.getId()));
			List<Area> areainfo = areaDao.selectByArea(area);
			if(areainfo.size()==0){
				area.setAddress(address);
				area.setDivideinto(0.00);
				area.setCreateTime(new Date());
				areaDao.insertArea(area);
				map = CommonConfig.messg(200);
			}else{
				map.put("code", 202);
				map.put("messg", "小区名字已存在！");
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return CommonConfig.messg(403);
		}
	}

	@Override
	public Map<String, Object> updateByArea(User user, Integer aid, String name, String address, String phone, 
			Double divideinto) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Area area = new Area();
			if(phone!=null && !phone.equals("")){
				User manuser = userService.existAdmin(phone);
				if (manuser == null || manuser.getPhoneNum().equals(user.getPhoneNum())) {
					return map = CommonConfig.messg(402);
				}
				area.setManid(manuser.getId());
			}else{
				areaDao.updatepartner(null, 0, aid);
				divideinto = 0.00;
			}
			area.setId(aid);
			area.setName(name);
			area.setAddress(address);
			area.setDivideinto(divideinto/100);
			areaDao.updateByArea(area);
			
			map = CommonConfig.messg(200);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return map = CommonConfig.messg(403);
		}
	}

	@Override
	public int updateAreaEarn(Integer edittype, Double money, Integer id,
			Double wallet,Double equ,Double card) {
		return areaDao.updateAreaEarn(edittype, money, id, wallet, equ, card);
	}

	@Override
	public int everydayAreaReset() {
		return areaDao.everydayAreaReset();
	}

	@Override
	public int insertAreastatistics(Integer aid, Double onlineEarn, Integer coinEarn, Double walletEarn, Double equEarn,
			Double cardEarn, Date createTime) {
		Areastatistics areastatistics = new Areastatistics();
		areastatistics.setAid(aid);
		areastatistics.setOnlineEarn(onlineEarn);
		areastatistics.setCoinEarn(coinEarn);
		areastatistics.setWalletEarn(walletEarn);
		areastatistics.setEquEarn(equEarn);
		areastatistics.setCardEarn(cardEarn);
		areastatistics.setCreateTime(createTime);
		return areaDao.insertAreastatistics(areastatistics);
	}

	@Override
	public List<Map<String, Object>> selectAllArea() {
		return areaDao.selectAllArea();
	}

	@Override
	public List<Areastatistics> selectAreastatisticsByAid(Integer aid, Integer beginnum, Integer neednum) {
		return areaDao.selectAreastatisticsByAid(aid, beginnum, neednum);
	}
	
	
//---------------------------------------------------------------------------------------------------------------
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年9月3日 下午6:07:29 
	 */
	public AreaRelevance selectAreaRelOnly(Integer aid, Integer partid, Integer sort, Integer type, Integer status) {
		return selectAreaRelSole(null, aid,  partid, null, sort,  type, status, null, null, null);
	}

	
	//实体类判断小区关联信息是否存在
	public Boolean boolAreaRelSole(Integer aid, Integer partid, Integer sort, Integer type, Integer status) {
		Boolean flas = true;
		try {
			AreaRelevance result = selectAreaRelOnly( aid, partid, sort, type, status);
			if(result==null) flas = false;
			return flas;
		} catch (Exception e) {
			return false;
		}
	}


	@Override
	public List<Map<String, Object>> selectAreaRelev(Integer aid) {
		try {
			Parameters parame = new Parameters();
			parame.setState(CommUtil.toString(aid));
			List<Map<String, Object>> result =  areaDao.selectaRearelInfo(parame);
			return result =  result != null ? result : new ArrayList<Map<String, Object>>();
		} catch (Exception e) {
			return new ArrayList<Map<String, Object>>();
		}
	}

	@Override
	public List<Area> selectPartAreaById(Integer uid) {
		try {
			List<Area> list = new ArrayList<Area>();
			Parameters parame = new Parameters();
			parame.setSource(CommUtil.toString(uid));
			parame.setType("2");
			List<Map<String, Object>> result =  areaDao.selectaRearelInfo(parame);
			for(Map<String, Object> item : result){
				Integer aid = CommUtil.toInteger(item.get("aid"));
				Area area = areaDao.selectByIdArea(aid);
				String num = equipmentDao.selectEquCountByAid(aid);
				area.setEqucount(num);
				list.add(area);
			}
			return list;
		} catch (Exception e) {
			return new ArrayList<Area>();
		}
	}

	/**
	 * @Description： 转移合伙人，从hd_area表查出原来含有的合伙人id信息
	 * @author： origin 创建时间：   2019年9月12日 下午2:49:30 
	 */
	@Override
	public List<Area> selectMainForArea() {
		List<Area> result = areaDao.selectMainForArea();
		return result != null ? result : new ArrayList<Area>();
	}

	/**
	 * @Description： 根据小区id与合伙人id将合伙人数据存储进入对应表中
	 * @author： origin 创建时间：   2019年9月12日 下午3:00:20 
	 */
	@Override
	public void increasePartner(Integer aid, Integer mainid, Double percent) {
		try {
			Date time = new Date();
			insertAreaReleInfo( aid, mainid, percent, 1, 2, 1, null, time, time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description： 根据小区id和类型查询小区合伙人或管理信息
	 * @author： origin 创建时间：   2019年9月12日 下午3:43:58 
	 */
	@Override
	public List<Map<String, Object>> inquirePartnerInfo(Integer aid, Integer type) {
		try {
			Parameters parame = new Parameters();
			parame.setState(CommUtil.toString(aid));
			parame.setType(CommUtil.toString(type));
			parame.setSort("ORDER BY av.sort");
			List<Map<String, Object>> result = CommUtil.isListMapEmpty(areaDao.selectaRearelInfo(parame));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String, Object>>();
		}
	}

	/**
	 * @Description： 绑定小区合伙人(aid:小区id   type: 类型 1管理员  2合伙人  phone:添加对象的电话  percent:分成比)
	 * @author： origin 创建时间：   2019年9月27日 上午11:42:00
	 */
	@Override
	public Map<String, Object> bindAreaPartner(Integer aid, Integer type, String phone, Double percent) {
		percent = CommUtil.toDouble(percent)/100;
		Map<String, Object> map = new HashMap<>();
		User user = userService.existAdmin(phone);
		if(user == null || user.getSubMer().equals(1)){
			return map = CommUtil.responseBuild(201, "商户不存在或为特约商户", null, map);
		}
		if(user.getLevel()!=0 && user.getLevel()!=2){
			return map = CommUtil.responseBuild(201, "该账户不为商户", null, map);
		}
		try {
			if(CommUtil.toInteger(type)==1){
				AreaRelevance arearele = selectAreaRelOnly(aid, null, null, 3, 1);
				if(arearele==null){
					if(user!=null){
						Integer partnerid = CommUtil.toInteger(user.getId());
						insertAreaReleInfo(aid, partnerid, null, 1, 3, 1, phone, new Date(), new Date());
						AreaRelevance partInfo = selectAreaRelOnly(aid, partnerid, 1, 3, 1);
						map = CommUtil.responseBuild(200, "管理人添加成功", partInfo, map);
					}else if(user==null && CommUtil.toString(phone).length()>0){
						insertAreaReleInfo(aid, -1, null, 1, 3, 1, phone, new Date(), new Date());
						AreaRelevance partInfo = selectAreaRelOnly(aid, 0, 1, 3, 1);
						map = CommUtil.responseBuild(200, "管理人添加成功", partInfo, map);
					}
				}else{
					map = CommUtil.responseBuild(201, "管理人已存在", null, map);
				}
			}else if(CommUtil.toInteger(type)==2){
				List<Map<String, Object>> partInfo = inquirePartnerInfo(aid, 2);
				Integer size = partInfo.size();
				if(size<4){
					Integer partnerid = CommUtil.toInteger(user.getId());
					AreaRelevance part = selectAreaRelOnly(aid, partnerid, null, 2, 1);
					if(part==null){
						Date time = new Date();
						insertAreaReleInfo(aid, partnerid, percent, CommUtil.toInteger(size+1), 2, 1, null, time, time);
						AreaRelevance partrele = selectAreaRelOnly(aid, partnerid, null, 2, 1);
						map = CommUtil.responseBuild(200, "合伙人添加成功", partrele, map);
					}else{
						map = CommUtil.responseBuild(201, "合伙人已存在", null, map);	
					}
				}else{
					map = CommUtil.responseBuild(202, "合伙人添加到达限制个数", null, map);
				}
			}else{
				map = CommUtil.responseBuild(203, "添加出现错误", null, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map = CommUtil.responseBuild(403, "添加异常", null, map);
		}
		return map;
	}

	/**
	 * employ 选择	ORIGIN
	 * @Description：删除小区合伙人(id：小区合伙关联记录 表id【hd_arearelevance】)
	 * @author： origin 创建时间：   2019年9月27日 上午11:42:00
	 */
	@Override
	public Map<String, Object> removeAreaPartner(Integer id) {
		Map<String, Object> map = new HashMap<>();
		try {
			Integer num = areaDao.deleteAreaReleById(CommUtil.toInteger(id));
			if(num==1){
				map = CommUtil.responseBuild(200, "删除成功", null, map);
			}else{
				map = CommUtil.responseBuild(201, "删除失败或异常", null, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map = CommUtil.responseBuild(403, "异常错误", null, map);
		}
		return map;
	}

	/**
	 * employ 选择	ORIGIN
	 * @Description： 编辑修改小区信息
	 * @author： origin 创建时间：   2019年9月27日 下午3:47:08
	 */
	@Override
	public Object editAreaInfo(Integer aid, String name, String address) {
		Map<String, Object> map = new HashMap<>();
		try {
			Area area = new Area();
			area.setId(CommUtil.toInteger(aid));
			area.setName(CommUtil.toString(name));
			area.setAddress(CommUtil.toString(address));
			Integer num =areaDao.updateByArea(area);
			if(num==1){
				map = CommUtil.responseBuild(200, "删除成功", null, map);
			}else{
				map = CommUtil.responseBuild(201, "删除失败或异常", null, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map = CommUtil.responseBuild(403, "异常错误", null, map);
		}
		return map;
	}

	/**
	 * @Description： 修改绑定小区合伙人的分成比(aid:小区id   percent:分成比)
	 * @author： origin 创建时间：   2019年9月27日 上午11:42:00
	 */
	@Override
	public Map<String, Object> editBindAreaPartner(Integer id, Double percent) {
		Map<String, Object> mapdata = new HashMap<>();
		try {
			AreaRelevance arearele = new AreaRelevance();
			arearele.setId(CommUtil.toInteger(id));
			AreaRelevance relev = areaDao.selectAreaRelSole(arearele);
			if(relev!=null){
				AreaRelevance partner = new AreaRelevance();
				partner.setId(id);
				partner.setPercent(CommUtil.toDouble(percent)/100);
				partner.setOperatetime(new Date());
				areaDao.updateArReleById(partner);
				
				AreaRelevance relearea = new AreaRelevance();
				relearea.setId(CommUtil.toInteger(id));
				AreaRelevance result = areaDao.selectAreaRelSole(relearea);
				mapdata = CommUtil.responseBuild(200, "修改成功", result, mapdata);
			}else{
				mapdata = CommUtil.responseBuildInfo(201, "合伙人不存在或参数传递错误", mapdata);
			}
			return mapdata;
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(403, "异常错误", mapdata);
		}
	}

	/**
	 * @Description： 绑定小区的管理人员(aid:小区id  phone:添加对象的电话  nickname:昵称 name:姓名 )
	 * @author： origin 创建时间：   2019年9月27日 上午11:42:00
	 */
	@Override
	public Object bindAreaPartner(Integer aid, String nickname, String phone, String name) {
		Map<String, Object> map = new HashMap<>();
		User user = userService.existAdmin(phone);
		try {
			AreaRelevance arearele = selectAreaRelOnly(aid, null, null, 3, 1);
			if(arearele==null){
				if(user!=null){
					Integer partnerid = CommUtil.toInteger(user.getId());
					insertAreaReleInfo(aid, partnerid, null, 1, 3, 1, phone, new Date(), new Date());
					AreaRelevance partInfo = selectAreaRelOnly(aid, partnerid, 1, 3, 1);
					map = CommUtil.responseBuild(200, "管理人添加成功", partInfo, map);
				}else if(user==null && CommUtil.toString(phone).length()>0){
					insertAreaReleInfo(aid, -1, null, 1, 3, 1, phone, new Date(), new Date());
					AreaRelevance partInfo = selectAreaRelOnly(aid, 0, 1, 3, 1);
					map = CommUtil.responseBuild(200, "管理人添加成功", partInfo, map);
				}
			}else{
				map = CommUtil.responseBuild(201, "管理人已存在", null, map);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return map;
	}
	
	/**
	 * @Description： 修改绑定小区的管理人员(aid:小区id   type: 类型 1管理员  2合伙人  phone:添加对象的电话  percent:分成比)
	 * @author： origin  2019年9月30日 下午2:26:16 
	 */
	@Override
	public Object editBindAreaPartner(Integer id, Integer aid, Integer type, String phone) {
		Map<String, Object> map = new HashMap<>();
		try {
			AreaRelevance arearele = new AreaRelevance();
			arearele.setId(CommUtil.toInteger(id));
			AreaRelevance relev = areaDao.selectAreaRelSole(arearele);
			if(relev!=null){
				User user = userService.existAdmin(phone);
				arearele.setAid(CommUtil.toInteger(aid));
				if(user!=null){
					arearele.setPartid(CommUtil.toInteger(user.getId()));
				}else{
					arearele.setPartid(-1);
				}
//				arearele.setPercent(CommUtil.toDouble(percent)/100);
				arearele.setSort(relev.getSort());
				arearele.setType(relev.getType());
				arearele.setStatus(relev.getStatus());
				arearele.setRemark(phone);
				arearele.setOperatetime(new Date());
				areaDao.updateArReleById(arearele);
				AreaRelevance result = areaDao.selectAreaRelSole(arearele);
				map = CommUtil.responseBuild(200, "修改成功", result, map);
			}else{
				map = CommUtil.responseBuild(201, "修改失败或异常", null, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map = CommUtil.responseBuild(403, "异常错误", null, map);
		}
		return map;
	}

	/**
	 * @Description： 更改小区服务电话
	 * @author： origin   2019年10月10日 下午5:25:55 
	 */
	@Override
	public Map<String, Object> editAreaServicePhone(Integer id, Integer aid, String nick, String name, String mobile) {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("aid", aid);
			map.put("nick", nick);
			map.put("name", name);
			map.put("mobile", mobile);
			JSONObject json = JSONObject.fromObject(map);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	//=====================================================================================================
		/**
		 * separate
		 * @Description：小区管理信息
		 * @author： origin 
		 */
	@Override
	public Object areaManageData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			//====================================================
			Parameters parameters = new Parameters();
			parameters.setRemark(CommUtil.toString(maparam.get("areaname")));//小区名称
			parameters.setSource(CommUtil.toString(maparam.get("address")));//小区地址
			int count = areaDao.getTotalAreaByparam(parameters);
			page.setTotalRows(count);
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> areaManageInfo = CommUtil.isListMapEmpty(areaDao.selectByParame(parameters));
			for(Map<String, Object> item : areaManageInfo){


		/*		Map<String, Object>  areaonline = areaDao.inquireAreaOnlineCard(aid);
				Integer devicenum =  areaDao.inquireAreaDevicenum(aid);
				Map<String, Object>  areauser =  areaDao.inquireAreaUser(aid);*/
				
			/*	Integer onlinenum = CommUtil.toInteger(areaonline.get("count"));//在线卡表数
				Double ctopupbalance = CommUtil.toDouble(areaonline.get("topupbalance"));//金额
				Double csendmoney = CommUtil.toDouble(areaonline.get("sendmoney"));//赠送金额
				
				Integer areausernum = CommUtil.toInteger(areauser.get("count"));//小区用户数
				Double utopupbalance = CommUtil.toDouble(areauser.get("topupbalance"));//金额
				Double usendmoney = CommUtil.toDouble(areauser.get("sendmoney"));//赠送金额*/
				
				/*item.put("devicenum", devicenum);//设备数
				item.put("onlinenum", onlinenum);//在线数
				item.put("ctopupbalance", ctopupbalance);//金额
				item.put("csendmoney", csendmoney);//赠送金额
				item.put("areausernum", areausernum);//小区人数
				item.put("utopupbalance", utopupbalance);//金额
				item.put("usendmoney", usendmoney);//赠送用户金额*/
			/*	Parameters pareme = new Parameters();
				pareme.setState(aid.toString());
				pareme.setType("2");*/
				/*List<Map<String, Object>> arearelevanceData = CommUtil.isListMapEmpty(areaDao.selectaRearelInfo(pareme));
				item.put("partnersize", arearelevanceData.size());
				item.put("partner", arearelevanceData);*/
			}
			datamap.put("listdata", CommUtil.isListMapEmpty(areaManageInfo));
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/** 
	 * newest WeChat
	 * @Description： 查询商户的小区信息
	 * @author： origin   2019年11月29日 下午5:45:49 
	 */
	@Override
	public List<Map<String, Object>> inquireDealAreaData(Integer dealid) {
		try {
			Parameters parame = new Parameters();
			parame.setUid(dealid);
			List<Map<String, Object>> dealAreaData = areaDao.inquireDealAreaData(parame);
			return CommUtil.isListMapEmpty(dealAreaData);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String, Object>>();
		}
	}

	@Override
	public Area selectByAreaId(Integer id) {
		return areaDao.selectByAreaId(id);
	}
	
	//===========================================================================================================

	//实体类查询小区关联信息（单条）
	public AreaRelevance selectAreaRelSole(Integer id, Integer aid, Integer partid, Double percent,Integer sort, Integer type, 
			Integer status, String remark, Date createtime, Date operatetime) {
		AreaRelevance arearele = new AreaRelevance();
		try {
			arearele.setId(id);
			arearele.setAid(aid);
			arearele.setPartid(partid);
			arearele.setPercent(percent);
			arearele.setSort(sort);
			arearele.setType(type);
			arearele.setStatus(status);
			arearele.setRemark(remark);
			arearele.setCreatetime(createtime);
			arearele.setOperatetime(operatetime);
			AreaRelevance result = areaDao.selectAreaRelSole(arearele);
			return result;
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
			return null;
		}
	}
	
	//实体类插入小区关联信息
	public void insertAreaReleInfo(Integer aid, Integer partid, Double percent,Integer sort, Integer type, 
			Integer status, String remark, Date createtime, Date operatetime) {
		AreaRelevance arearele = new AreaRelevance();
		try {
			arearele.setAid(aid);
			arearele.setPartid(partid);
			arearele.setPercent(CommUtil.toDouble(percent));
			arearele.setSort(sort);
			arearele.setType(type);
			arearele.setStatus(status);
			arearele.setRemark(remark);
			arearele.setCreatetime(createtime);
			arearele.setOperatetime(operatetime);
			areaDao.insertAreaReleInfo(arearele);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	//实体类根据id修改小区关联信息
	public void updateAreaReleInfo(Integer id, Integer aid, Integer partid, Double percent,Integer sort, Integer type, 
			Integer status, String remark, Date createtime, Date operatetime) {
		AreaRelevance arearele = new AreaRelevance();
		try {
			arearele.setId(id);
			arearele.setAid(aid);
			arearele.setPartid(partid);
			arearele.setPercent(CommUtil.toDouble(percent));
			arearele.setSort(sort);
			arearele.setType(type);
			arearele.setStatus(status);
			arearele.setRemark(remark);
			arearele.setCreatetime(createtime);
			arearele.setOperatetime(operatetime);
			areaDao.updateArReleById(arearele);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	//===========================================================================================================
	
	/**
	 * employ 选择	ORIGIN
	 * @Description：查询多条小区关联的信息
	 * @author： origin
	 * @createTime：2020年3月21日下午5:36:13
	 */
	public List<Map<String, Object>> inquireAreaRelevancInfo(Parameters parame) {
		try {
			List<Map<String, Object>> result = CommUtil.isListMapEmpty(areaDao.inquireAreaRelevancInfo(parame));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String, Object>>();
		}
	}

	/**
	 * employ 选择	ORIGIN
	 * @Description：根据小区id查询单个小区相关信息
	 * @param aid
	 * @author： origin ：2020年5月15日下午3:48:03
	 */
	@Override
	public Map<String, Object> inquireSingleAreaInfo(Integer aid) {
		try {
			Parameters parame = new Parameters();
			parame.setOrder(CommUtil.toString(aid));
			Map<String, Object> areaInfo = areaDao.inquireAreaInfo(parame);
			return CommUtil.isMapEmpty(areaInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}

	/**
	 * employ 选择	ORIGIN
	 * @Description：
	 * @author： origin ：2020年5月15日下午5:05:58
	 */
	@Override
	public Map<String, Object>  inquireAreaOnlineCard(Integer aid) {
		try {
			Map<String, Object>  onlinenum = areaDao.inquireAreaOnlineCard(aid);
			return onlinenum;
		} catch (Exception e) {
			e.printStackTrace();
			return new  HashMap<>();
		}
	}

	/**
	 * employ 选择	ORIGIN
	 * @Description：
	 * @author： origin ：2020年5月15日下午5:05:58
	 */
	@Override
	public Integer inquireAreaDevicenum(Integer aid) {
		try {
			Integer devicenum = areaDao.inquireAreaDevicenum(aid);
			return devicenum;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * employ 选择	ORIGIN
	 * @Description：
	 * @author： origin ：2020年5月15日下午5:05:58
	 */
	@Override
	public Map<String, Object> inquireAreaUser(Integer aid) {
		try {
			Map<String, Object> areausernum = areaDao.inquireAreaUser(aid);
			return areausernum;
		} catch (Exception e) {
			e.printStackTrace();
			return  new  HashMap<>();
		}
	}

	/**
	 * @Description：设置小区相关模板
	 * @author： origin 2020年5月19日下午3:32:16
	 */
	@Override
	public Map<String, Object> areaTempDirectUpdate( Integer aid, Integer tempid, Integer type) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Area area = new Area();
			area.setId(aid);
			//type: 1 钱包   2在线卡
			if(type.equals(1)){
				area.setTempid(tempid);
			}else if(type.equals(2)){
				area.setTempid2(tempid);
			}
			areaDao.updateByArea(area);
			return CommUtil.responseBuildInfo(200, "成功", datamap); 
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	/**
	 * @author origin
	 * @param merid
	 * @return 根据id查询该商户是否为合伙人
	 */
	@Override
	public Map<String, Object> inquireAreaService(Integer merid) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			merid =  CommUtil.toInteger(merid);//金额
			Parameters parame = new Parameters();
			parame.setSource(CommUtil.toString(merid));
			List<Map<String, Object>> result =  CommUtil.isListMapEmpty(areaDao.selectaRearelInfo(parame));
			datamap.put("size", result.size());
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	//---------------------------------------------------------------------------------------------------------------
	
	@Override
	public void addAPartEarn(String code, double money, Integer merid, Integer aid, int type, int status) {
		//设备添加收益	(devicenum, paymoney, merid, aid, 1, 0);
		if (type == 1) {
			equipmentDao.updateEquEarn(code, money, status);
		}
		//小区添加收益
		try {
			if (aid != null && aid != 0) {
				if (type == 1) {
					areaDao.updateAreaEarn(status, money, aid,null,money,null);
				} else if (type == 2) {
					areaDao.updateAreaEarn(status, money, aid,money,null,null);
				} else if (type == 3) {
					areaDao.updateAreaEarn(status, money, aid,null,null,money);
				}
			} 
		} catch (Exception e) {
			logger.warn("小区修改余额错误===" + e.getMessage());
		}
	}

	@Override
	public Map<String,Object> queryAreaRecently(Double lon, Double lat, Double distance, Integer startnum, Integer distanceSort, String areaname) {
		Map<String,Object> map = new HashMap<>();
		try {
			List<Area> arealist = areaDao.queryAreaRecently(lon, lat, distance, startnum, distanceSort, areaname);
			if (arealist == null) {
				arealist = new ArrayList<>();
			} else {
				for (Area area : arealist) {
					area.setDCchargeMoney(1.2);
					area.setDCserverMoney(0.0);
				}
			}
			map.put("arealist", arealist);
			map.put("startnum", startnum + 1);
			map.put("listsize", arealist.size());
			return CommUtil.responseBuildInfo(1000, "获取成功", map);
		} catch (Exception e) {
			return CommUtil.responseBuildInfo(1002, "系统异常", null);
		}
	}

	@Override
	public Map<String, Object> queryAreaInfo(Double lon, Double lat, Integer id) {
		Map<String,Object> map = new HashMap<>();
		try {
			Area area = areaDao.queryAreaInfo(lon, lat, id);
			if (area != null) {
				area.setExAllnum(0);
				area.setExfreenum(0);
//				area.setDCchargeMoney(1.0);
//				area.setDCserverMoney(0.2);
//				area.setExchargeMoney(0.8);
//				area.setExserverMoney(0.1);
			}
			Map<String, String> billingParam = JedisUtils.hgetAll("billingInfo");
			int timenum = Integer.parseInt(billingParam.get("timenum"));
			if (timenum > 0) {
				String timeInfoStr = billingParam.get("timeInfo");
				List<Map<String, Object>> timeInfo = (List<Map<String, Object>>) JSON.parse(timeInfoStr);
				for (Map<String, Object> map2 : timeInfo) {
					Integer hour = (int) map2.get("hour");
					Integer minute = (int) map2.get("minute");
					int nowhour = DisposeUtil.getDateTime(4, 0);
					int nowminute = DisposeUtil.getDateTime(5, 0);
					BigDecimal chargefee = (BigDecimal) map2.get("chargefee");
					BigDecimal serverfee = (BigDecimal) map2.get("serverfee");
					area.setDCchargeMoney(chargefee.doubleValue());
					area.setDCserverMoney(serverfee.doubleValue());
					area.setExchargeMoney(chargefee.doubleValue());
					area.setExserverMoney(serverfee.doubleValue());
					if (hour > nowhour && minute > nowminute) {
						break;
					}
				}
			}
			map.put("areainfo", area);
			return CommUtil.responseBuildInfo(1000, "获取成功", map);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(1002, "系统异常", null);
		}
	}
	
}
