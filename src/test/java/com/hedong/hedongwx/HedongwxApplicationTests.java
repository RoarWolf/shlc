//package com.hedong.hedongwx;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.hedong.hedongwx.dao.EquipmentNewDao;
//import com.hedong.hedongwx.dao.UserDao;
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
//	public EquipmentNewDao equipmentNewDao;
//
//	@Test
//    public void dealerIncomeCollect(){
//		String selectDeviceExsit = equipmentNewDao.selectDeviceExsit("1027520102030001");
//		System.out.println("---" + selectDeviceExsit);
//		
//	}
//	
//}
