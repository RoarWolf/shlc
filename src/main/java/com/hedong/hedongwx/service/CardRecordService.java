package com.hedong.hedongwx.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import com.hedong.hedongwx.entity.CardRecord;

public interface CardRecordService {

	int insetCardApply(Integer number, Integer id, String cause);

	List<CardRecord> getCardRecordList();

	List<CardRecord> getCardParamRecList(HttpServletRequest request);

	List<CardRecord> cardRecordCardID(String cardIdParam);

	List<CardRecord> cardRecordInfo();

	List<CardRecord> selectCardReliefInfo(HttpServletRequest request);

	List<CardRecord> transferInfo(HttpServletRequest request);

	CardRecord transferInfoOne(Integer id);
	
	int SetupdateCard(Integer id, Integer status);
	
	int checkInfo(Integer id, Integer uid, Integer target);

	List<CardRecord> submitApply(Integer uid, Integer num);//查询已提交的申请


	

	
}
