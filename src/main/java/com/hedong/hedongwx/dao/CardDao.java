package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Card;
import com.hedong.hedongwx.entity.Parameters;

public interface CardDao {

	List<Card> getList();

	Card getCardParamId(@Param("id") Integer id);

	List<Card> getCardParamList(Map<String, Object> paramMap);

	int deleteIdCard(Integer id);

	int updateCard(@Param("id")Integer id, @Param("status")Integer status);

	int updateState(Card card);

	int insertParam(Card card);

	List<Card> getCardListBy(Integer uid);
	
	List<Card> getCardListByStatus(@Param("uid")Integer uid, @Param("status")Integer status);
	
	int setupdateCard(@Param("uid") Integer uid, @Param("cardID") String cardID);//��IC�����û���������

	Card selectByCardID(Integer uid, String cardID);

	Card getCardByCardnum(String cardID);
	
	int updateCardByCardnum(@Param("cardMoney") Double cardMoney,@Param("cardID") String cardID);

	int checkCard(Card card);//审核卡操作
	
	int updateRemark(@Param("id") Integer id, @Param("remark") String remark);
	
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询在线卡信息
	List<Map<String, Object>> selectOnlinecard(Parameters parameters);

}
