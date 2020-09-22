package com.hedong.hedongwx.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.Card;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.utils.PageUtils;

public interface CardService {
	/*** 卡记录 *************************************************************************/
	List<Card> getCardList();

	List<Card> getCardParamList(HttpServletRequest request);
	
	Card getCardParamId(Integer id);

	int deleteIdCard(Integer id);

	int updateCard(Integer id, Integer param);
	
	int updateState(HttpServletRequest request);

	int insertParam(HttpServletRequest request);
	
	List<Card> getCardListBy(Integer uid);//用户信息中根据uid查询卡
	
	List<Card> getCardListByStatus(Integer uid, Integer status);
	
	int SetupdateCard(Integer id, Integer status);//根据卡的id和给与的卡状态将卡修改为对应状态
	
	int setupdateCard(Integer uid, String cardID);//根据用户id和卡的cardID修改卡的信息（卡绑定操作）
	
	//*********************************************************************************************
	Card getCardByCardnum(String cardID);
	
	int updateCardByCardnum(Double cardMoney,String cardID);

	int setIcunbindCard(String cardID);//解绑IC卡

	void updateCardCheck(String cardID, Integer status);

	void checkCard(double money, String cardID);
	
	int updateRemark(Integer id, String remark);
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询在线卡信息
	PageUtils<Parameters> selectOnlinecard(HttpServletRequest request);

	
	


}
