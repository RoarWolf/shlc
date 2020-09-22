//package com.hedong.hedongwx;
//
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.alibaba.fastjson.JSON;
//import com.hedong.hedongwx.dao.CollectStatisticsDao;
//import com.hedong.hedongwx.entity.Parameters;
//import com.hedong.hedongwx.service.CollectStatisticsService;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class HedongwxApplicationTests {
//	
//	@Autowired
//	private CollectStatisticsDao collectStatisticsDao;
//
//	@Test
//    public void dealerIncomeCollect(){
//		Parameters parame = new Parameters();
//		String str = "2020-08-11";
//		parame.setStartTime(str + " 00:00:00");
//		parame.setEndTime(str + " 23:59:59");
//		List<Map<String,Object>> dealerIncomeCollect = collectStatisticsDao.dealerIncomeCollect(parame);
//		System.out.println(JSON.toJSON(dealerIncomeCollect));
//	}
//	
//}
