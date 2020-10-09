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
//import com.hedong.hedongwx.dao.AreaDao;
//import com.hedong.hedongwx.dao.UserDao;
//import com.hedong.hedongwx.entity.Area;
//import com.hedong.hedongwx.entity.User;
//import com.hedong.hedongwx.service.AreaService;
//import com.hedong.hedongwx.service.ChargeRecordService;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class HedongwxApplicationTests {
//	
//	@Autowired
//	public UserDao userDao;
//	@Autowired
//	public AreaService areaService;
//	@Autowired
//	public ChargeRecordService chargeRecordService;
//
//	@Test
//    public void dealerIncomeCollect(){
//		User user = userDao.getUserByOpenid("Y5FxSK0bHHOOUWVEGaAPbo_A");
//		System.out.println(JSON.toJSONString(user));
//		
//	}
//	
//}
