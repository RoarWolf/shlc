package com.hedong.hedongwx.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hedong.hedongwx.dao.MerchantDetailDao;
import com.hedong.hedongwx.dao.MeruserDao;
import com.hedong.hedongwx.dao.OperateRecordDao;
import com.hedong.hedongwx.dao.UserBankcardDao;
import com.hedong.hedongwx.dao.WithdrawDao;
import com.hedong.hedongwx.entity.Idcard;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.Meruser;
import com.hedong.hedongwx.entity.Operaterecord;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserBankcard;
import com.hedong.hedongwx.entity.Withdraw;
import com.hedong.hedongwx.service.IdcardService;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.service.MeruserService;
import com.hedong.hedongwx.service.UserBankcardService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;

import lombok.extern.slf4j.Slf4j;

/**
*author：RoarWolf
*createtime：2020年11月12日
*/

@Slf4j
@Service
public class MeruserServiceImpl implements MeruserService {
	
	public static Map<String, Long> withdrawMap = new HashMap<>();
	
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
	@Autowired
	private MerchantDetailDao merchantDetailDao;
	@Autowired
	private MerchantDetailService merchantDetailService;
	@Autowired
	private WithdrawDao withdrawDao;
	@Autowired
	private UserBankcardDao userBankcardDao;

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
			String cardimgBack, String areaname, String address, String bankcardnum,
			String bankname) {
		User user = userService.selectUserById(uid);
		if (user == null) {
			return CommUtil.responseBuildInfo(1001, "当前用户不存在", null);
		} else {
			String phoneNum = user.getPhoneNum();
//			Integer merid = meruserDao.selectMeruserByPhonenum(phoneNum);
//			if (merid != null && !merid.equals(uid)) {
//				return CommUtil.responseBuildInfo(1001, "已申请注册过桩主，请刷新页面查看是否申请成功", null);
//			}
			Integer bankcardnumUid = null;
			if (bankcardnum != null) {
				bankcardnumUid = userBankcardDao.selectBankcardnumUid(bankcardnum);
			}
			if (bankcardnumUid != null && !bankcardnumUid.equals(uid)) {
				return CommUtil.responseBuildInfo(1002, "银行卡已被注册", null);
			}
			Date createTime = new Date();
			Meruser meruser = new Meruser();
			meruser.setUid(uid);
			meruser.setPhonenum(phoneNum);
			meruser.setProvince(province);
			meruser.setCity(city);
			meruser.setCountry(country);
			meruser.setAreaname(areaname);
			meruser.setCreateTime(createTime);
			meruser.setAddress(address);
			meruser.setEnabled((byte) 0);
			Meruser selectMeruser = meruserDao.selectMeruserByUid(uid);
			if (selectMeruser == null) {
				meruserDao.insertMeruser(meruser);
			} else {
				meruserDao.updateMeruser(meruser);
			}
			Integer idcardByUid = idcardService.selectIdcardByUid(uid);
			Idcard idcard = new Idcard();
			idcard.setUid(uid);
			idcard.setIdCardnum(idcardnum);
			idcard.setCardimgFront(cardimgFront);
			idcard.setCardimgBack(cardimgBack);
			idcard.setCreateTime(createTime);
			if (idcardByUid == null) {
				idcardService.insertIdcard(idcard);
			} else {
				idcardService.updateIdcard(idcard);
			}
			if (user.getRealname() == null) {
				User user2 = new User();
				user2.setId(uid);
				user2.setRealname(realname);
				userService.updateUserById(user2);
			}
			UserBankcard userBankcard = new UserBankcard();
			userBankcard.setBankcardnum(bankcardnum);
			userBankcard.setBankname(bankname);
			userBankcard.setUserId(uid);
			userBankcard.setRealname(realname);
			if (bankcardnumUid == null) {
				userBankcardService.addUserBankcard(userBankcard);
			} else {
				userBankcardService.updateUserBankcard(userBankcard);
			}
			operateRecordDao.insertoperate("用户申请桩主", 0, uid, 0, 1, null, null);
			return CommUtil.responseBuildInfo(1000, "申请成功", null);
		}
	}

	@Override
	public Map<String,Object> updateMeruser(Meruser meruser) {
		try {
			Integer merid = meruserDao.selectMeruserByPhonenum(meruser.getPhonenum());
			if (merid != null && !meruser.getId().equals(merid)) {
				return CommUtil.responseBuildInfo(201, "手机号码重复", null);
			}
			int updateMeruser = meruserDao.updateMeruser(meruser);
			return CommUtil.responseBuildInfo(200, "修改成功", null);
		} catch (Exception e) {
			return CommUtil.responseBuildInfo(201, "修改失败", null);
		}
	}

	@Override
	public Map<String,Object> selectMeruserList(Meruser meruser) {
		if (meruser.getStartindex() == null || meruser.getStartindex() <= 0) {
			meruser.setStartindex(0);
		} else {
			meruser.setStartindex((meruser.getStartindex() - 1) * 10);
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
			startindex = 1;
		}
		startindex = (startindex - 1) * 10;
		pages = 10;
		List<Operaterecord> operateRecordlist = operateRecordDao.userOperateRecordlist(opename, objname, type, source, startTime, endTime, startindex, pages);
		map.put("recordlist", operateRecordlist);
		map.put("totalnum", operateRecordDao.operateRecordTotalnum(opename, objname, type, source, startTime, endTime));
		return CommUtil.responseBuildInfo(200, "查询成功", map);
	}

	@Override
	public Map<String, Object> updateOperaterecord(Integer id,Integer opeid, Integer type) {
		Integer objid = operateRecordDao.selectOperrecordById(id);
		int updateOperaterecord = operateRecordDao.updateOperaterecord(id, opeid, type);
		if (type == 1) {
			Meruser meruser = new Meruser();
			meruser.setUid(objid);
			meruser.setEnabled(type.byteValue());
			meruserDao.updateMeruser(meruser);
		} else {
			Meruser meruser = new Meruser();
			meruser.setUid(objid);
			meruser.setEnabled((byte) 3);
			meruserDao.updateMeruser(meruser);
		}
		return CommUtil.responseBuildInfo(200, "修改成功", null);
	}

	@Override
	public Map<String, Object> selectUserBankcardlist(Integer uid, Integer type) {
		Map<String, Object> map = new HashMap<>();
		List<UserBankcard> bankcardlist = userBankcardService.selectUserBankcardByUserid(uid, type);
		map.put("bankcardlist", bankcardlist);
		return CommUtil.responseBuildInfo(1000, "查询成功", map);
	}
	
	@Override
	public Map<String, Object> selectMerEarnDetaillist(Integer merid, Integer startnum, Integer datetype) {
		Map<String, Object> map = new HashMap<>();
		startnum = startnum * 10;
		String startTime = CommUtil.getPastDate(0);
		if (datetype == 2) {
			startTime = CommUtil.getPastDate(7);
		} else if (datetype == 3) {
			startTime = CommUtil.getPastDate(30);
		}
		List<MerchantDetail> merEarnDetaillist = merchantDetailDao.selectMerDetailListByMeridLimit(merid, startnum, startTime);
		map.put("merEarnDetaillist", merEarnDetaillist);
		map.put("listsize", merEarnDetaillist.size());
		Meruser meruser = meruserDao.selectMeruserByUid(merid);
		if (meruser != null) {
			map.put("surpMoney", meruser.getNowEarn());
		} else {
			map.put("surpMoney", 0.0);
		}
		return CommUtil.responseBuildInfo(1000, "查询成功", map);
	}
	
	@Transactional
	@Override
	public Map<String, Object> withdrawaccess(Integer bankcardid, Double money, Integer uid) {
		long nowtime = System.currentTimeMillis();
		if (DisposeUtil.checkMapIfHasValue(withdrawMap)) {
			Long updateTime = withdrawMap.get("uid" + uid);
			if (updateTime == null || updateTime == 0) {
				withdrawMap.put("uid" + uid, nowtime);
			} else {
				long calcTime = nowtime - updateTime;
				if (calcTime < 1500) {
					return CommUtil.responseBuildInfo(1004, "重复提交", null);
				} else {
					withdrawMap.put("uid" + uid, nowtime);
				}
			}
		} else {
			withdrawMap.put("uid" + uid, nowtime);
		}
		// 查询商家信息
		Meruser meruser = meruserDao.selectMeruserByUid(uid);
		// 参数的判断
		if (bankcardid == null || bankcardid == 0) {
			return CommUtil.responseBuildInfo(1001, "银行卡未录入", null);
		} else if (money == 0 || money < 5) {
			return CommUtil.responseBuildInfo(1002, "提现金额需大于5元", null);
		} else if (CommUtil.subBig(meruser.getNowEarn(), money) < 0) {
			return CommUtil.responseBuildInfo(1003, "提现金额超出余额，不予提现", null);
		} else {
			// 根据银行卡编号查询
			UserBankcard userBankcard = userBankcardService.selectUserBankcardByid(bankcardid);
			Withdraw withdraw = new Withdraw();
			String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + DisposeUtil.completeNum(uid.toString(), 8);
			// 订单
			withdraw.setWithdrawnum("425" + format);
			// 银行卡号
			String bankcardnum = userBankcard.getBankcardnum();
			// 设置参数
			withdraw.setBankcardnum(bankcardnum);
			withdraw.setBankname(userBankcard.getBankname());
			withdraw.setMoney(money);
			withdraw.setStatus(0);
			withdraw.setUserId(uid);
			// 获取商家的费率
			Integer feerate = userBankcard.getRate();
			// type:1-个人,2-对公
			// 商家提现的方式是个人直接到账
			if (userBankcard.getType() == 2) {
				feerate = userBankcard.getRate();
				withdraw.setStatus(4);
			}
			// 设置商家的费率
			if (feerate == null || feerate == 0) {
				withdraw.setServicecharge((money * 6 + 4 + 0.000)/1000);
			} else {
				withdraw.setServicecharge((money * feerate + 4 + 0.000)/1000);
			}
			// 设置提现前的金额
			withdraw.setUserMoney(meruser.getNowEarn());
			Double userMoney = meruser.getNowEarn();
			// 提现前的总额度减去提现的钱
			userMoney = userMoney * 100 - money * 100;
			// 更新商家的收益(减掉提现的钱)
			meruserDao.updateMerUserEarn(0, money, uid);
			Date date2 = new Date();
			withdraw.setCreateTime(date2);
			// 添加提现的订单信息
			withdrawDao.addWithdraw(withdraw);
			// 插入商家的收益明细
			merchantDetailService.insertMerEarningDetail(uid, money, userMoney / 100, withdraw.getWithdrawnum(), new Date(), MerchantDetail.WITHDRAWSOURCE, 0, MerchantDetail.WITHDRAWACCESS);
			return CommUtil.responseBuildInfo(1000, "提现成功", null);
		}
	}

	@Override
	public Map<String, Object> selectEarnWallet(Integer uid) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> merCollect = meruserDao.selectMerCollect(uid);
		List<Map<String,Object>> merCollectList = meruserDao.selectMerCollectList(uid, CommUtil.getPastDate(7));
		map.put("merCollect", merCollect);
		map.put("merCollectList", merCollectList);
		return CommUtil.responseBuildInfo(1000, "获取成功", map);
	}

	@Override
	public Map<String, Object> selectMerEarnList(Integer uid,Integer startnum, String datetime) {
		Map<String, Object> map = new HashMap<>();
		startnum = startnum * 10;
		String startTime = datetime + " 00:00:00";
		String endTime = datetime + " 23:59:59";
		
		List<MerchantDetail> merEarnlist = merchantDetailDao.selectEarnListByMeridLimit(uid, startnum,startTime,endTime);
		map.put("merEarnlist", merEarnlist);
		map.put("listsize", merEarnlist.size());
		return CommUtil.responseBuildInfo(1000, "获取成功", map);
	}

	@Override
	public void merCollect() {
		List<Integer> uidlist = meruserDao.selectNormalMeruser();
		String datetime = CommUtil.getPastDate(1);
		String startTime = datetime + " 00:00:00";
		String endTime = datetime + " 23:59:59";
		if (uidlist != null && uidlist.size() > 0) {
			for (Integer uid : uidlist) {
				Map<String, Object> merTotalEarn = meruserDao.selectMerTotalEarn(uid, startTime, endTime);
				Double charge_earn = (Double) merTotalEarn.get("allchargemoney");
				Double advertise_earn = (Double) merTotalEarn.get("alladvertisemoney");
				Integer totalnum = CommUtil.toInteger(merTotalEarn.get("totalnum"));
				Double withmoney = (Double) merTotalEarn.get("allwithdrawmoney");
				Double withrefundmoney = (Double) merTotalEarn.get("allrefundwithdrawmoney");
				Double withdraw_earn = CommUtil.subBig(withmoney, withrefundmoney);
				try {
					meruserDao.insertMerCollect(uid, charge_earn, advertise_earn, totalnum, withdraw_earn, datetime);
				} catch (Exception e) {
				}
			}
		}
	} 

}
