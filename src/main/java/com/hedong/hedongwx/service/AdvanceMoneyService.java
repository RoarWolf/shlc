package com.hedong.hedongwx.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.AdvanceMoney;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.utils.PageUtils;

public interface AdvanceMoneyService {
	
	//根据实体类插入数据
	int insertAdvanceMoney(AdvanceMoney advanceMoney);

	//根据merid查询对应商户预支金额
	PageUtils<Parameter> advanceMoneyByParam(HttpServletRequest request);

	//根据merid查询对应商户预支总额
	Map<String, Object> advanceMoneySum(Integer soure, Integer uid);
	
	//根据merid查询数据 soure： 0为查询商户 1为用户    uid为查询对象的id
	boolean selectAdvanceById(Integer soure, Integer uid);
	//根据用户id修改金额
	int updateAdvanceById(Integer uid, Double money);
	
	//通过用户id查询商户欠账金额
	Double selectMoneyByUid(Integer uid);
	
	
}
