package com.hedong.hedongwx.dao;

import com.hedong.hedongwx.entity.CodeSystemParam;

public interface CodeSystemParamDao {

	int insertCodeSystemParam(CodeSystemParam codeSystemParam);
	
	int updateCodeSystemParam(CodeSystemParam codeSystemParam);
	
	CodeSystemParam selectCodeSystemParamByCode(String code);
}
