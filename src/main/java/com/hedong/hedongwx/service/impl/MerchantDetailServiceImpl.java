package com.hedong.hedongwx.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.dao.MerchantDetailDao;
import com.hedong.hedongwx.dao.UserDao;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.utils.CommUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class MerchantDetailServiceImpl implements MerchantDetailService{
	private final Logger logger = LoggerFactory.getLogger(MerchantDetailServiceImpl.class);
	@Autowired
	private MerchantDetailDao merchantDetailDao;
	@Autowired
	private UserDao userDao;
	
	@Override
	public int insertMerEarningDetail(Integer merid, Double money, Double balance, String ordernum, Date createTime, Integer paysource
			,Integer paytype, Integer status) {
		MerchantDetail merchantDetail = new MerchantDetail();
		merchantDetail.setMerid(merid);
		merchantDetail.setMoney(money);
		merchantDetail.setBalance(balance);
		merchantDetail.setOrdernum(ordernum);
		merchantDetail.setPaysource(paysource);
		merchantDetail.setCreateTime(createTime);
		merchantDetail.setPaytype(paytype);
		merchantDetail.setStatus(status);
		return merchantDetailDao.insertMerEarningDetail(merchantDetail);
	}

	@Override
	public List<MerchantDetail> selectMerDetailListByMerid(Integer merid) {
		return merchantDetailDao.selectMerDetailListByMerid(merid);
	}

	@Override
	public MerchantDetail selectMerEarnInfoById(Integer id) {
		return merchantDetailDao.selectMerEarnInfoById(id);
	}

	@Override
	public void merEearningCalculate(Integer merid, Double money, Integer type, String ordernum, Integer paysource,
			Integer paytype, Integer status) {
		try {
			User user = userDao.selectUserById(merid);
			user = user == null ? new User() : user;
			Double balance = CommUtil.toDouble(user.getEarnings());
		     //  1为加  0为减
			if(type==1){
				balance = CommUtil.addBig(balance, money);
			}else{
				balance = CommUtil.subBig(balance, money);
			}
			user.setEarnings(balance);
			//商户收益  1为加  0为减
			userDao.updateUserEarnings(type, money, user.getId());
			try {
				//商户收益总额  1为加  0为减
				userDao.updateMerAmount(type, money, user.getId());
			} catch (Exception e) {
				logger.warn("商户总计更改出错");
			}
			MerchantDetail merchantDetail = new MerchantDetail();
			merchantDetail.setMerid(merid);
			merchantDetail.setMoney(money);
			merchantDetail.setBalance(balance);
			merchantDetail.setOrdernum(ordernum);
			merchantDetail.setPaysource(paysource);
			merchantDetail.setCreateTime(new Date());
			merchantDetail.setPaytype(paytype);
			merchantDetail.setStatus(status);
			// 添加商家交易明细
			merchantDetailDao.insertMerEarningDetail(merchantDetail);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
	}
	/**
	 * @Description： 计算合伙人与商户分别收益的计算处理
	 * @author： origin 创建时间：2020年5月22日下午4:46:55
	 */
	public Map<String, Object> percentCalculateDispose(List<Map<String, Object>> partInfo, Integer merid, Double money) {
		Double mermoney = 0.00;//商户金额
		Double partmoney = 0.00;//合伙人金额
		Double tolpercent = 0.00;//分成比
		Map<String, Object> mapresult = new HashMap<>();
		List<Map<String, Object>> percentinfo = new ArrayList<>();
		try {
			if(partInfo.size()>0){//分成
				for(Map<String, Object> item : partInfo){
					Map<String, Object> mappartner = new HashMap<>();
					Integer partid = CommUtil.toInteger(item.get("partid"));
					Double percent = CommUtil.toDouble(item.get("percent"));
					Double partnetmoney = CommUtil.toDouble((money * (percent*100))/100);
					mappartner.put("partid", partid);
					mappartner.put("percent", percent);
					mappartner.put("money", partnetmoney);
					percentinfo.add(mappartner);
					tolpercent = tolpercent + percent;
				}
			}
			mermoney = CommUtil.toDouble(money * (1- tolpercent));
			partmoney = CommUtil.subBig(money, mermoney);
			Map<String, Object> mermap = new HashMap<>();
			mermap.put("merid", merid);
			mermap.put("percent", (1-tolpercent));
			mermap.put("money", mermoney);
			percentinfo.add(mermap);
			mapresult.put("mermoney", mermoney);
			mapresult.put("partmoney", partmoney);
			mapresult.put("percentinfo", percentinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapresult;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object dealerIncomeRefund(String comment, Integer merid, Double money, String ordernum,Integer paysource, Integer paytype, Integer paystatus) {
		Integer type = 0;
		List<Map<String, Object>> merchlist = new ArrayList<>();
		try {
			JSONArray jsona = JSONArray.fromObject(comment);
			for (int i = 0; i < jsona.size(); i++) {
				Map<String, Object> map = new HashMap<>();
				JSONObject item = jsona.getJSONObject(i);
				Integer partid = CommUtil.toInteger(item.get("partid"));
				Integer mercid = CommUtil.toInteger(item.get("merid"));
				Double partmoney = CommUtil.toDouble(item.get("money"));
				//合伙人信息
				if(partid != 0){
					merEearningCalculate(partid, partmoney, type, ordernum, paysource, paytype, paystatus);
			    //商户信息
				}else{
					merEearningCalculate(mercid, partmoney, type, ordernum, paysource, paytype, paystatus);
				}
				map = item;
				merchlist.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(CommUtil.getExceptInfo(e));
		}
		return JSON.toJSON(merchlist);
	}
	
}
