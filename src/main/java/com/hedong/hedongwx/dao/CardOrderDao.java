package com.hedong.hedongwx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.CardOrder;

public interface CardOrderDao {

	void payMoneys(CardOrder cardOrder);
	

	CardOrder queryMoneyByOrdernum(String ordernum);

	int updateMoney(CardOrder money);

	List<CardOrder> cardOrderRecord(@Param("uid")Integer uid);
	
	List<CardOrder> selectOrderByCardID(String cardID);
}
