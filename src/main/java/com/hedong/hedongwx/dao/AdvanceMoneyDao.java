package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.AdvanceMoney;
import com.hedong.hedongwx.entity.Parameter;

public interface AdvanceMoneyDao {

	/**
	 * 根据实体类插入数据
	 * @param advanceMoney
	 * @return
	 */
	int insertAdvanceMoney(AdvanceMoney advanceMoney);

	/**
	 * 分页查询记录
	 * @param parameter
	 * @return
	 */
	List<Map<String, Object>> advanceMoneyByParam(Parameter parameter);

	/**
	 * 根据merid查询对应商户预支总额
	 * @param soure
	 * @param uid
	 * @return
	 */
	Map<String, Object> advanceMoneySum(@Param("soure")Integer soure, @Param("uid")Integer uid);

	/**
	 * 根据merid查询数据 soure： 0为查询商户 1为用户    uid为查询对象的id
	 * @param soure
	 * @param uid
	 * @return
	 */
	AdvanceMoney selectAdvanceById(@Param("soure")Integer soure, @Param("uid")Integer uid);

	/**
	 *
	 * @param uid
	 * @param money
	 * @return
	 */
	int updateAdvanceById(@Param("uid") Integer uid, @Param("money")Double money);

	/**
	 *
	 * @param uid
	 * @return
	 */
	Double selectMoneyByUid(Integer uid);

}
