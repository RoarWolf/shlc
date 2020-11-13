package com.hedong.hedongwx.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hedong.hedongwx.dao.MeruserDao;
import com.hedong.hedongwx.dao.OperateRecordDao;
import com.hedong.hedongwx.entity.Idcard;
import com.hedong.hedongwx.entity.Meruser;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserBankcard;
import com.hedong.hedongwx.service.IdcardService;
import com.hedong.hedongwx.service.MeruserService;
import com.hedong.hedongwx.service.UserBankcardService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;

/**
*author：RoarWolf
*createtime：2020年11月12日
*/

@Service
public class MeruserServiceImpl implements MeruserService {
	
	@Autowired
	private MeruserDao meruserDao;
	@Autowired
	private UserService userService;
	@Autowired
	private IdcardService idcardService;
	@Autowired
	private UserBankcardService userBankcardService;
	@Autowired
	private OperateRecordDao operateRecordDao;

	@Transactional
	@Override
	public Map<String,Object> insertMeruser(Meruser meruser) {
		Integer merid = meruserDao.selectMeruserByPhonenum(meruser.getPhonenum());
		if (merid != null) {
			return CommUtil.responseBuildInfo(201, "账号已存在", null);
		} else {
			Map<String,Object> map = new HashMap<>();
			meruser.setCreateTime(new Date());
			int insertMeruser = meruserDao.insertMeruser(meruser);
			merid = meruserDao.selectMeruserByPhonenum(meruser.getPhonenum());
			map.put("merid", merid);
			return CommUtil.responseBuildInfo(200, "添加成功", map);
		}
	}
	
	@Transactional
	@Override
	public Map<String,Object> insertMeruserAndIdcard(Integer uid, Integer province, Integer city,
			Integer country, String realname, String idcardnum, String cardimgFront, 
			String cardimgBack, String areaname) {
		User user = userService.selectUserById(uid);
		if (user == null) {
			return CommUtil.responseBuildInfo(1001, "当前用户不存在", null);
		} else {
			String phoneNum = user.getPhoneNum();
			Date createTime = new Date();
			Meruser meruser = new Meruser();
			meruser.setUid(uid);
			meruser.setPhonenum(phoneNum);
			meruser.setProvince(province);
			meruser.setCity(city);
			meruser.setCountry(country);
			meruser.setAreaname(areaname);
			meruser.setCreateTime(createTime);
			meruserDao.insertMeruser(meruser);
			Idcard idcard = new Idcard();
			idcard.setUid(uid);
			idcard.setIdCardnum(idcardnum);
			idcard.setCardimgFront(cardimgFront);
			idcard.setCardimgBack(cardimgBack);
			idcard.setCreateTime(createTime);
			idcardService.insertIdcard(idcard);
			if (user.getRealname() == null) {
				User user2 = new User();
				user2.setId(uid);
				user2.setRealname(realname);
				userService.updateUserById(user2);
			}
			return CommUtil.responseBuildInfo(1000, "添加成功", null);
		}
	}

	@Override
	public Map<String,Object> updateMeruser(Meruser meruser) {
		int updateMeruser = meruserDao.updateMeruser(meruser);
		return CommUtil.responseBuildInfo(200, "修改成功", null);
	}

	@Override
	public Map<String,Object> selectMeruserList(Meruser meruser) {
		if (meruser.getStartindex() == null || meruser.getStartindex() <= 0) {
			meruser.setStartindex(0);
		} else {
			meruser.setStartindex(meruser.getStartindex() * 10);
		}
		List<Meruser> selectMeruserList = meruserDao.selectMeruserList(meruser);
		Map<String,Object> map = new HashMap<>();
		map.put("meruserlist", selectMeruserList);
		map.put("totalnum", meruserDao.selectTotalnum(meruser));
		return CommUtil.responseBuildInfo(200, "查询成功", map);
	}

	@Override
	public Map<String, Object> insertBankcard(Integer uid, String bankcardnum, String bankname) {
		if (!userBankcardService.checkBankcardExist(bankcardnum)) {
			UserBankcard bankcard = new UserBankcard();
			bankcard.setUserId(uid);
			bankcard.setBankcardnum(bankcardnum);
			bankcard.setBankname(bankname);
			userBankcardService.addUserBankcard(bankcard);
			return CommUtil.responseBuildInfo(1000, "添加成功", null);
		} else {
			return CommUtil.responseBuildInfo(1001, "添加失败，银行卡已存在", null);
		}
	}

	@Override
	public Map<String, Object> selectMeruserByUid(Integer uid) {
		Meruser meruser = meruserDao.selectMeruserByUid(uid);
		Map<String, Object> map = new HashMap<>();
		map.put("meruserinfo", meruser);
		return CommUtil.responseBuildInfo(1000, "查询成功", map);
	}

	@Override
	public Map<String, Object> userOperateRecordlist(String opename, String objname, Integer type, Integer source,
			String startTime, String endTime, Integer startindex, Integer pages) {
		Map<String, Object> map = new HashMap<>();
		if (startindex == null || startindex <= 0) {
			startindex = (startindex - 1) * 10;
		}
		pages = 10;
		List<Map<String,Object>> operateRecordlist = operateRecordDao.userOperateRecordlist(opename, objname, type, source, startTime, endTime, startindex, pages);
		map.put("recordlist", operateRecordlist);
		map.put("totalnum", operateRecordDao.operateRecordTotalnum(opename, objname, type, source, startTime, endTime));
		return CommUtil.responseBuildInfo(200, "查询成功", map);
	}

	@Override
	public Map<String, Object> updateOperaterecord(Integer id,Integer opeid, Integer type) {
		Integer objid = operateRecordDao.selectOperrecordById(id);
		int updateOperaterecord = operateRecordDao.updateOperaterecord(id, opeid, type);
		Meruser meruser = new Meruser();
		meruser.setUid(objid);
		meruser.setEnabled(type.byteValue());
		meruserDao.updateMeruser(meruser);
		return CommUtil.responseBuildInfo(200, "修改成功", null);
	}

	@Override
	public Map<String, Object> selectUserBankcardlist(Integer uid, Integer type) {
		Map<String, Object> map = new HashMap<>();
		List<UserBankcard> bankcardlist = userBankcardService.selectUserBankcardByUserid(uid, type);
		map.put("bankcardlist", bankcardlist);
		return CommUtil.responseBuildInfo(1000, "查询成功", map);
	}

}
