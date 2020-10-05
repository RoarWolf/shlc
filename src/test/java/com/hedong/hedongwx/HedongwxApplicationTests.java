package com.hedong.hedongwx;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.dao.AreaDao;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.ChargeRecordService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HedongwxApplicationTests {
	
	@Autowired
	public AreaDao areaDao;
	@Autowired
	public AreaService areaService;
	@Autowired
	public ChargeRecordService chargeRecordService;

	@Test
    public void dealerIncomeCollect(){
//		List<Map<String, Object>> queryAreaRecently = areaDao.queryAreaRecently2(113.568114, 34.8149006, null, 0);
//		for (Map<String, Object> area : queryAreaRecently) {
//			System.out.println(area.get("id") + "---" + JSON.toJSONString(area));
//		}
		Map<String, Object> insertChargeRecord = chargeRecordService.insertChargeRecord("0027021234561234", 1, 3, 1000, 1, 1, 1, 10.0);
		System.out.println(JSON.toJSONString(insertChargeRecord));
	}
	
}
