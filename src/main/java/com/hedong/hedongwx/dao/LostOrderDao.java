package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Parameters;

public interface LostOrderDao {

	void insertLostListOrder(@Param("list")List<Map<String, Object>> listcontent);
	
	List<Map<String, Object>> inquireListLostInfo(Parameters parameters);

	Integer updataLostRefundOrder(@Param("id")Integer id, @Param("outrefundno")String outrefundno, @Param("wxrefundno")String wxrefundno, 
		@Param("refundfeemoney")String refundfeemoney, @Param("RefundType")String refundType, @Param("RefundStatus")String refundStatus);
	
}
