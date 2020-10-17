//package com.hedong.hedongwx;
//
//import java.util.Map;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.alibaba.fastjson.JSON;
//import com.hedong.hedongwx.dao.AdminDao;
//import com.hedong.hedongwx.dao.UserDao;
//import com.hedong.hedongwx.service.AdminService;
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
//	@Autowired
//	public AdminDao adminDao;
//	@Autowired
//	public AdminService adminService;
//
//	@Test
//    public void dealerIncomeCollect(){
//		Map<String, Object> selectAdminMenu = adminService.selectSetAdminMenu(1);
//		System.out.println("---" + JSON.toJSONString(selectAdminMenu));
//	}
//	
//}
