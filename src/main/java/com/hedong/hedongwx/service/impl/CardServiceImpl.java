package com.hedong.hedongwx.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.CardDao;
import com.hedong.hedongwx.dao.CardRecordDao;
import com.hedong.hedongwx.entity.Card;
import com.hedong.hedongwx.entity.CardRecord;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.CardService;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;


@Service
public class CardServiceImpl implements CardService {

	@Autowired
	private CardDao cardDao;

	@Autowired
	private CardRecordDao cardRecordDao;
	
	@Override
	public List<Card> getCardList() {
		return cardDao.getList();
	}

	@Override
	public List<Card> getCardParamList(HttpServletRequest request) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("number", request.getParameter("number"));
		paramMap.put("cardID", request.getParameter("cardID"));
		paramMap.put("status", request.getParameter("status"));
		paramMap.put("startTime", request.getParameter("startTime"));
		paramMap.put("endTime", request.getParameter("endTime"));
		return cardDao.getCardParamList(paramMap);
	}
	
	@Override
	public List<Card> getCardListBy(Integer uid) {//用户信息中根据uid查询卡
		return  cardDao.getCardListBy(uid);
	}
	
	@Override
	public List<Card> getCardListByStatus(Integer uid, Integer status) {
		return  cardDao.getCardListByStatus(uid, status);
	}
	
// RZC ***************************************************************************
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
	
	@Override
	public int SetupdateCard(Integer id, Integer status) {//用户根据IC卡id来启用/挂失 IC卡 并记录到卡操作表中
		Card card = cardDao.getCardParamId(id);
		inseryRecord(card,status,0);
		return cardDao.updateCard( id, status);
	}
	
	@Override
	public int setIcunbindCard(String cardID) {//解绑IC卡
		inseryRecord(cardDao.getCardByCardnum(cardID), 5, 0);
		return cardDao.setupdateCard( null, cardID);
	}
	
	@Override
	public int setupdateCard(Integer uid, String cardID) {//将IC卡和用户关联起来
		int num = 0;
		Card card = cardDao.getCardByCardnum(cardID);
		if(card==null){
			Card cards = new Card();
			cards.setUid(uid);
			cards.setCardNum(cardID);
			cards.setCardID(cardID);
			cards.setCardMoney((double) 0);
			cards.setType(0);
			cards.setStatus(1);//默认为正式启用的卡片
			cardDao.insertParam(cards);
			
			inseryRecord(cardDao.getCardByCardnum(cardID), 0, 0);
			num = 1;
		}else{
			if(card.getUid()!=null){
				num = 2;
			}else if(card.getUid()==null){
				cardDao.setupdateCard( uid, cardID);
				inseryRecord(cardDao.getCardByCardnum(cardID), 0, 0);
				num = 1;
			}
		}
		return num;
	}
	

	
	@Override
	public Card getCardParamId(Integer id) {
		return cardDao.getCardParamId(id);
	}
	
	@Override
	public int deleteIdCard(Integer id) {
		return cardDao.deleteIdCard(id);
	}

	@Override
	public int updateCard(Integer id, Integer status) {
		return cardDao.updateCard(id, status);
	}

	@Override
	public int updateState(HttpServletRequest request) {
		Card card = Built(request);
		return cardDao.updateState(card);
	}

	@Override
	public int insertParam(HttpServletRequest request) {
		Card card = Built(request);
		return cardDao.insertParam(card);
	}

	
	public Card Built(HttpServletRequest request){
		Card card = new Card();
		card.setId(StringUtil.getIntString(request.getParameter("id")));
		card.setCardNum(request.getParameter("cardNum"));
		card.setCardMoney(StringUtil.getDoubleString(request.getParameter("cardMoney")));
		card.setCardID(request.getParameter("cardID"));
		card.setType(StringUtil.getIntString(request.getParameter("")));
		card.setStatus(StringUtil.getIntString(request.getParameter("status")));
		card.setCreationTime(request.getParameter("creationTime"));
		return card;
		
	}
	
	
	//*************************************************************************
	@Override
	public Card getCardByCardnum(String cardID) {
		return cardDao.getCardByCardnum(cardID);
	}

	@Override
	public int updateCardByCardnum(Double cardMoney, String cardID) {
		return cardDao.updateCardByCardnum(cardMoney, cardID);
	}
	
	@Override
	public void updateCardCheck(String cardID, Integer status) {
		Card card = new Card();
		card.setStatus(status);
		card.setCardID(cardID);
		cardDao.checkCard(card);
	}

	@Override
	public void checkCard(double money, String cardID) {
		Card cardfirst = cardDao.getCardByCardnum(cardID);
		double moneys = cardfirst.getCardMoney()+money;
		Card card = new Card();
		card.setCardID(cardID);
		card.setCardMoney(moneys);
		cardDao.checkCard(card);
	}

	@Override
	public int updateRemark(Integer id, String remark) {
		return cardDao.updateRemark(id, remark);
	}
	
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询在线卡信息
	@Override
	public PageUtils<Parameters> selectOnlinecard(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		User user = CommonConfig.getAdminReq(request);
		Integer rank = user.getRank();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(rank!=0) parameters.setUid(user.getId());//获取用户
		
		parameters.setOrder(request.getParameter("serialnumber"));//编号
		parameters.setCode(request.getParameter("cardnumber"));//卡号
		parameters.setNickname(request.getParameter("username"));//昵称
		parameters.setState(request.getParameter("status"));//状态
		parameters.setDealer(request.getParameter("dealer"));//商户
		parameters.setStartTime(request.getParameter("startTime"));
		parameters.setEndTime(request.getParameter("endTime"));
		List<Map<String, Object>> totalmap = cardDao.selectOnlinecard(parameters);
		page.setTotalRows(totalmap.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		List<Map<String, Object>> tradepage = cardDao.selectOnlinecard(parameters);
		page.setListMap(tradepage);
		return page;
	}






}
