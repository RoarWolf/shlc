package com.hedong.hedongwx.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.OfflineCard;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.utils.PageUtils;

public interface OfflineCardService {

	int insertQueryOfflineCardRecord(String ordernum,String equipmentnum,String openid);
	
	int updateQueryOfflineCardRecord(String ordernum,String cardID,double balance,Integer recycletype);
	
	OfflineCard selectOfflineCardByOrdernum(String ordernum);
	
	int insertChargeMoneyOfflineCardRecord(String ordernum, String equipmentnum, String cardID, 
			double chargemoney,double accountmoney,Integer handletype,Integer paytype,Integer uid,Integer status);
	
	int updateChargeMoneyOfflineCardRecord(String ordernum,double balance,Integer recycletype,Integer paytype);
	
	int deleteOfflineCardByOrdernum(String ordernum);

	PageUtils<OfflineCard> selectOrderInfo(HttpServletRequest request);
	
	int updateChargeMoneyOfflineCardRecordPaytype(OfflineCard offlineCard);
	
	int offlineCardRefund(Integer paytype,Integer id);
	
	OfflineCard selectOfflineCardById(Integer id);
	
	Double getOfflineTodayMoneyByUserid(String begintime, String endtime, String equipmentnum, Integer merchantid,
			String ordernum);
	
	List<OfflineCard> getOfflineByParma(String begintime, String endtime, String equipmentnum, Integer merchantid,
			String ordernum);
	
	OfflineCard gainOfflineData(String cardID);
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	PageUtils<Parameters> selectOfflineRecord(HttpServletRequest request);

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	
	//=========================================================================================
	/**
	 * separate
	 * @Description：离线充值机记录信息查看
	 * @author： origin 
	 */
	Object offlineRecordData(HttpServletRequest request);
	
	//=========================================================================================
}
