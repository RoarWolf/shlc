package com.hedong.hedongwx.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.dao.GeneralDetailDao;
import com.hedong.hedongwx.entity.GeneralDetail;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.service.GeneralDetailService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Service
public class GeneralDetailServiceImpl implements GeneralDetailService {

	@Autowired
	private GeneralDetailDao generalDetailDao;

	@Override
	public int insertGenWalletDetail(Integer uid, Integer merid, Double paymoney, Double sendmoney, Double tomoney,  
			 Double balance, Double topupbalance, Double givebalance, String ordernum, Date createTime, Integer paysource) {
		GeneralDetail generalDetail = new GeneralDetail();
		generalDetail.setUid(uid);
		generalDetail.setMerid(merid);
		generalDetail.setMoney(CommUtil.toDouble(paymoney));
		generalDetail.setSendmoney(CommUtil.toDouble(sendmoney));
		generalDetail.setTomoney(CommUtil.toDouble(tomoney));
		generalDetail.setBalance(CommUtil.toDouble(balance));
		generalDetail.setTopupbalance(CommUtil.toDouble(topupbalance));
		generalDetail.setGivebalance(CommUtil.toDouble(givebalance));
		generalDetail.setOrdernum(ordernum);
		generalDetail.setPaysource(paysource);
		generalDetail.setCreateTime(createTime);
		return generalDetailDao.insertGenWalletDetail(generalDetail);
	}
	
	@Override
	public int insertGenDetail(Integer uid, Integer merid, Double paymoney, Double sendmoney, Double tomoney, Double balance,  
			Double topupbalance, Double givebalance, String ordernum, Date createTime, Integer paysource, String remark) {
		GeneralDetail generalDetail = new GeneralDetail();
		generalDetail.setUid(uid);
		generalDetail.setMerid(merid);
		generalDetail.setMoney(CommUtil.toDouble(paymoney));
		generalDetail.setSendmoney(CommUtil.toDouble(sendmoney));
		generalDetail.setTomoney(CommUtil.toDouble(tomoney));
		generalDetail.setBalance(CommUtil.toDouble(balance));
		generalDetail.setTopupbalance(CommUtil.toDouble(topupbalance));
		generalDetail.setGivebalance(CommUtil.toDouble(givebalance));
		generalDetail.setOrdernum(ordernum);
		generalDetail.setPaysource(paysource);
		generalDetail.setRemark(remark);
		generalDetail.setCreateTime(createTime);
		return generalDetailDao.insertGenDetail(generalDetail);
	}

	@Override
	public Map<String, Object> selectGenWalletDetailByUid(Integer uid, Integer startnum) {
		Map<String,Object> map = new HashMap<>();
		try {
			List<GeneralDetail> walletDetail = generalDetailDao.selectGenWalletDetailByUid(uid, startnum * 10);
			map.put("walletRecord", walletDetail);
			map.put("startnum", startnum++);
			if (walletDetail.size() > 0) {
				return CommUtil.responseBuildInfo(1000, "获取成功", map);
			} else {
				return CommUtil.responseBuild(1000, "获取成功", null);
			}
		} catch (Exception e) {
			return CommUtil.responseBuild(1002, "系统异常", null);
		}
	}

	@Override
	public GeneralDetail selectGenWalletInfoById(Integer id) {
		return generalDetailDao.selectGenWalletInfoById(id);
	}

	@Override
	public PageUtils<Parameter> advanceDetail(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));// 条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));// 页码
		PageUtils<Parameter> page = new PageUtils<>(numPerPage, currentPage);
		Parameter parameter = new Parameter();
		parameter.setOrdernum(request.getParameter("ordernum"));
		parameter.setDealer(request.getParameter("merid"));
		parameter.setStartTime(request.getParameter("startTime"));
		parameter.setEndTime(request.getParameter("endTime"));

		List<Map<String, Object>> advanceMoney = generalDetailDao.advanceDetail(parameter);
		page.setTotalRows(advanceMoney.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameter.setNumPerPage(page.getNumPerPage());
		parameter.setStartIndex(page.getStartIndex());
		page.setListMap(generalDetailDao.advanceDetail(parameter));
		return page;
	}

	/**
	 * @Description： 用户在商户名下消费详情
	 * @author： origin          
	 * 创建时间：   2019年6月5日 上午9:52:37 
	 */
	@Override
	public List<Map<String, Object>> touristMoneyChange(Integer merid, String begintime, String endtime, Integer uid,
			Integer offset) {
		List<Map<String, Object>> gendeta = new ArrayList<Map<String,Object>>();
		try {
			Parameter param = new Parameter();
			param.setEqui(CommUtil.toString(uid));//用户
			param.setDealer(merid.toString());//商户
			param.setStartTime(begintime);
			param.setEndTime(endtime);
			gendeta = generalDetailDao.advanceDetail(param);
			if(gendeta.size()>100){
				Parameter parame = new Parameter();
				parame.setEqui(CommUtil.toString(uid));//用户
				parame.setDealer(merid.toString());//商户
				parame.setStartIndex(1);
				parame.setNumPerPage(100);
				gendeta = generalDetailDao.advanceDetail(parame);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gendeta;
	}

	@Override
	public List<Map<String, Object>> selectWalletDetail(Integer uid) {
		Parameter param = new Parameter();
		param.setEqui(uid.toString());//用户
		List<Map<String, Object>> gendeta = generalDetailDao.advanceDetail(param);
		return gendeta;
	}

}
