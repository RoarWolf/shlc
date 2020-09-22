package com.hedong.hedongwx.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.dao.CardOrderDao;
import com.hedong.hedongwx.entity.CardOrder;
import com.hedong.hedongwx.service.CardOrderService;

@Service
public class CardOrderServiceImpl implements CardOrderService {

	@Autowired
	private CardOrderDao cardOrderDao;

	@Override
	public CardOrder payMoney(HttpServletRequest request, String format, Date date, Integer paytype) {
		CardOrder cardOrder = new CardOrder();
		cardOrder.setCardID(request.getParameter("cardnum"));
		cardOrder.setOrdernum(format);
		cardOrder.setExpenditure(Double.parseDouble(request.getParameter("choosemoney")));
		// cardOrder.setExpenditure(StringUtil.getDoubleString(request.getParameter("choosemoney")));
		cardOrder.setBegintime(date);
		cardOrder.setType(2);
		cardOrderDao.payMoneys(cardOrder);
		return null;
	}

	@Override
	public CardOrder queryMoneyByOrdernum(String ordernum) {
		return cardOrderDao.queryMoneyByOrdernum(ordernum);
	}

	@Override
	public int updateMoney(CardOrder money) {
		return cardOrderDao.updateMoney(money);
	}

	@Override
	public List<CardOrder> cardOrderRecord(Integer uid, Integer num) {
		return cardOrderDao.cardOrderRecord(uid);
	}

	@Override
	public List<CardOrder> selectOrderByCardID(String cardID) {
		return cardOrderDao.selectOrderByCardID(cardID);
	}

}
