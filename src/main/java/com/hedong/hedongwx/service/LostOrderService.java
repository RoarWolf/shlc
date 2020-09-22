package com.hedong.hedongwx.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface LostOrderService {

	void insertLostListOrder(List<Map<String, Object>> listcontent);

	Map<String, Object> inquireListLostInfo(HttpServletRequest request);

	Map<String, Object> updataLostRefundOrder(Integer id, String outrefundno, String wxrefundno, String refundfeemoney);

}
