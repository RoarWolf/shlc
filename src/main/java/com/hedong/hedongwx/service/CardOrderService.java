package com.hedong.hedongwx.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.CardOrder;

public interface CardOrderService {

	CardOrder payMoney(HttpServletRequest request, String format, Date date, Integer i);

	CardOrder queryMoneyByOrdernum(String string);

	int updateMoney(CardOrder money);

	List<CardOrder> cardOrderRecord(Integer uid, Integer num);
	
	List<CardOrder> selectOrderByCardID(String cardID);

}
