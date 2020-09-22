package com.hedong.hedongwx.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.dao.CardDao;
import com.hedong.hedongwx.dao.CardRecordDao;
import com.hedong.hedongwx.entity.Card;
import com.hedong.hedongwx.entity.CardRecord;
import com.hedong.hedongwx.service.CardRecordService;
import com.hedong.hedongwx.utils.StringUtil;

@Service
public class CardRecordServiceImpl implements CardRecordService{
	
	@Autowired
	private CardRecordDao cardRecordDao;
	@Autowired
	private CardDao cardDao;
	
	@Override
	public List<CardRecord> getCardRecordList() {//卡操作查询
		return cardRecordDao.getRecordList();
	}
	
	@Override 
	public List<CardRecord> getCardParamRecList(HttpServletRequest request) {
		CardRecord cardRecord = new CardRecord();
		cardRecord.setId(StringUtil.getIntString(request.getParameter("id")));
		cardRecord.setUsername(request.getParameter("username"));
		cardRecord.setCardID(request.getParameter("cardID"));
		cardRecord.setStatus(StringUtil.getIntString(request.getParameter("status")));
		cardRecord.setStartTime(request.getParameter("startTime"));
		cardRecord.setEndTime(request.getParameter("endTime"));
		return cardRecordDao.selectInfo(cardRecord);
	}
	
	@Override
	public List<CardRecord> cardRecordCardID(String cardID) {
		return cardRecordDao.cardRecordCardID(cardID);
	}

	//***************************************************************************************
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

	@Override
	public List<CardRecord> cardRecordInfo() {//查询用户的卡操作记录
		CardRecord cardRecord = new CardRecord();
		return cardRecordDao.selectInfo(cardRecord);
	}

	@Override
	public List<CardRecord> selectCardReliefInfo(HttpServletRequest request) {//查询用户提交的申请解卡记录
		CardRecord cardRecord = new CardRecord();
		String classify = request.getParameter("classify");
		if(classify==null || classify.equals("")){
			cardRecord.setStatus(3);
			cardRecord.setType(2);
		}else if(classify.equals("1")){
			String cardID = request.getParameter("cardID");
			String username = request.getParameter("username");
			int type = StringUtil.getIntString(request.getParameter("type"));
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			if(type!=-1) cardRecord.setType(type);
			cardRecord.setCardID(cardID);
			cardRecord.setUsername(username);
			cardRecord.setStartTime(startTime);
			cardRecord.setEndTime(endTime);
			cardRecord.setStatus(3);
		}
		return cardRecordDao.selectInfo(cardRecord);
	}

	@Override
	public List<CardRecord> transferInfo(HttpServletRequest request) {//迁移金额
		CardRecord cardRecord = new CardRecord();
		String classify = request.getParameter("classify");
		if(classify==null || classify.equals("")){
			cardRecord.setStatus(6);
			cardRecord.setType(2);
		}else if(classify.equals("1")){
			String cardID = request.getParameter("cardID");
			String username = request.getParameter("username");
			int type = StringUtil.getIntString(request.getParameter("type"));
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			if(type!=-1) cardRecord.setType(type);
			cardRecord.setCardID(cardID);
			cardRecord.setUsername(username);
			cardRecord.setStartTime(startTime);
			cardRecord.setEndTime(endTime);
			cardRecord.setStatus(6);
		}
		return cardRecordDao.selectInfo(cardRecord);
	}
	
	@Override
	public CardRecord transferInfoOne(Integer id) {
		return cardRecordDao.selectInfoOne(id);
	}
	
	@Override
	public int checkInfo(Integer id, Integer uid, Integer target) {
		CardRecord cardRecord = new CardRecord();
		if(target==0){
			cardRecord.setType(0);
		}else if(target==1) {  
			cardRecord.setType(1);
		} 
		cardRecord.setReplyTime(new Date());
		cardRecord.setId(id);
		cardRecord.setReplyid(uid);
		return cardRecordDao.updatRecord(cardRecord);
	}
	
	
	@Override
	public int SetupdateCard(Integer id, Integer status) {//用户根据IC卡id来启用/挂失 IC卡 并记录到卡操作表中
		Card card = cardDao.getCardParamId(id);
		inseryRecord(card,status,0);
		return cardDao.updateCard( id, status);
	}
	
	@Override
	public List<CardRecord> submitApply(Integer uid, Integer num) {//查询已提交的申请
		 List<CardRecord> cardRecord = null;
		if(num==0){ //查询所有未被处理的申请
			cardRecord = cardRecordDao.submitApply(uid, 0);
		}else if(num==1){
			cardRecord = cardRecordDao.submitApply(uid, 3);
		}else if(num==2){
			cardRecord = cardRecordDao.submitApply(uid, 6);
		}
		return cardRecord;
	}
	//***************************************************************************************
	
	public void inseryRecord(Card card, Integer status, Integer num){
		CardRecord crd = new CardRecord();
		crd.setUid(card.getUid());
		crd.setCid(card.getId());
		crd.setCardID(card.getCardID());
		crd.setBalance(card.getCardMoney());
		crd.setStatus(status);
		crd.setRecordTime(new Date());
		crd.setType(num);
		cardRecordDao.insertCardRecord(crd);
	}
	
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 	
	
	
	// - - - RZC 手机端 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	//解挂申请
	@Override
	public int insetCardApply(Integer number, Integer id, String cause) {
		Card card = cardDao.getCardParamId(id);
		CardRecord cRd = cardRecordDao.cardRecordByCardID(card.getCardID(), number, 2);
		if(cRd==null){
			CardRecord cardRecord = new CardRecord();
			cardRecord.setUid(card.getUid());
			cardRecord.setCid(card.getId());
			cardRecord.setCardID(card.getCardID());
			cardRecord.setBalance(card.getCardMoney());
			cardRecord.setRecordTime(new Date());
			
			if(number==3)cardRecord.setStatus(3);//解挂
			else if(number==4)cardRecord.setStatus(4);//异常处理
			cardRecord.setApplycase(cause);
			cardRecord.setType(2);
			return cardRecordDao.insertCardRecord(cardRecord);
		}else{
			return 2;
		}
	}





	
	
}
