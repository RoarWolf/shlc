package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import com.hedong.hedongwx.entity.GeneralDetail;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.entity.Parameters;

public interface GeneralDetailDao {

	int insertGenWalletDetail(GeneralDetail generalDetail);

	List<Map<String, Object>> selecwalletdetail(Parameters parameters);
	
	List<GeneralDetail> selectGenWalletDetailByUid(Integer uid);
	
	GeneralDetail selectGenWalletInfoById(Integer id);

	List<Map<String, Object>> advanceDetail(Parameter parameter);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月24日 下午3:23:14 
	 */
	int insertGenDetail(GeneralDetail generalDetail);

	/**
	 * @Description：
	 * @author： origin
	 * @createTime：2020年2月24日下午3:11:54
	 * @comment:
	 */
	List<Map<String, Object>> selecuserwalletdetail(Parameters parameters);
}
