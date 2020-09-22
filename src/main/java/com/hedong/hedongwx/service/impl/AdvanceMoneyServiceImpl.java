package com.hedong.hedongwx.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.dao.AdvanceMoneyDao;
import com.hedong.hedongwx.entity.AdvanceMoney;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.service.AdvanceMoneyService;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Service
public class AdvanceMoneyServiceImpl implements AdvanceMoneyService{
	
	@Autowired
	private AdvanceMoneyDao advanceMoneyDao;
	
	/**
	 * 根据实体类插入数据
	 */
	@Override
	public int insertAdvanceMoney(AdvanceMoney advanceMoney) {
		return advanceMoneyDao.insertAdvanceMoney(advanceMoney);
	}

	/**
	 * 根据merid查询对应商户预支金额(无用)
	 */
	@Override
	public PageUtils<Parameter> advanceMoneyByParam(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameter> page  = new PageUtils<>(numPerPage, currentPage);
		Parameter parameter = new Parameter();
		parameter.setUsername(request.getParameter("username"));
		parameter.setDealer(request.getParameter("merid"));
		parameter.setStartTime(request.getParameter("begintime"));
		parameter.setEndTime(request.getParameter("endtime"));
		
		List<Map<String, Object>> advanceMoney = advanceMoneyDao.advanceMoneyByParam(parameter);
		page.setTotalRows(advanceMoney.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameter.setNumPerPage(page.getNumPerPage());
		parameter.setStartIndex(page.getStartIndex());
		page.setListMap(advanceMoneyDao.advanceMoneyByParam(parameter));
		return page;
	}

	/**
	 * 根据merid查询对应商户预支总额
	 */
	@Override
	public Map<String, Object> advanceMoneySum(Integer soure, Integer uid) {
		return advanceMoneyDao.advanceMoneySum( soure, uid);
		
	}
	
	/**
	 * 根据merid查询数据 soure： 0为查询商户 1为用户    uid为查询对象的id
	 */
	@Override
	public boolean selectAdvanceById(Integer soure, Integer uid) {
		boolean verdict = false;
		AdvanceMoney advanceMoney = advanceMoneyDao.selectAdvanceById( soure, uid);
		if(advanceMoney!=null){
			verdict = true;
		}
		return verdict;
	}

	@Override
	public int updateAdvanceById(Integer uid, Double money) {
		return advanceMoneyDao.updateAdvanceById( uid, money);
	}

	@Override
	public Double selectMoneyByUid(Integer uid) {
		return advanceMoneyDao.selectMoneyByUid(uid);
	}
	
	
	
	
	
}
