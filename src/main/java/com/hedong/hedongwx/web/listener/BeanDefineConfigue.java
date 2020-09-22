package com.hedong.hedongwx.web.listener;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.hedong.hedongwx.entity.MerAmount;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.ServerService;
import com.hedong.hedongwx.service.StatisticsService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.service.WithdrawService;
import com.hedong.hedongwx.utils.StringUtil;

@Component("BeanDefineConfigue")
public class BeanDefineConfigue implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private ServerService serverService;
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private UserService userService;
	@Autowired
	private InCoinsService inCoinsService;
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private WithdrawService withdrawService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		serverService.serverStart();
//		MqttPushClient.connect();
//		equipmentService.forEquCollet();
//		addMerEarnAmoun();
		/*String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();
		System.out.println(menu);
		int result;
		try {
			result = WeixinUtil.createMenu(WeixinUtil.getBasicAccessToken(), menu);
			if(result == 0){
				System.out.println("create menu access");
			}else {
				System.err.println("failure" + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	public void addMerEarnAmoun() {
		Parameters parameters = new Parameters();
		List<User> userList = userService.getUserList();
		for (User user : userList) {
			if ((user.getRank() != null && user.getRank() != 1) || (user.getEarnings() != null && user.getEarnings() > 0)) {
				parameters.setDealer(user.getId().toString());
				Map<String, Object> totalcoins = inCoinsService.selectcoinsup(parameters);
				String startTime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
				String endTime = StringUtil.toDateTime("yyyy-MM-dd 23:59:59");
				parameters.setStartTime(startTime);
				parameters.setEndTime(endTime);
				Map<String, Object> withdraw = withdrawService.selectwitmoney(user.getId());//提现金额与代提现金额
				double depomoney = 0.00;
				if(withdraw!=null) depomoney = (Double) withdraw.get("depomoney");
				Double allMoney = StringUtil.addBig(depomoney,StringUtil.togetBigDecimal(user.getEarnings()).doubleValue());
				MerAmount merAmount = userService.selectMerAmountByMerid(user.getId());
				Double allcoin = totalcoins == null ? 0 : totalcoins.get("incoinsmoney") == null ? 0 : StringUtil.togetBigDecimal(totalcoins.get("incoinsmoney")).doubleValue();
				String allcoinStr = allcoin + "";
				int idx = allcoinStr.lastIndexOf(".");
				String total_fee = allcoinStr.substring(0, idx);
				if (merAmount == null) {
					userService.insertMerAmount(user.getId(), allMoney, 0.0, Integer.parseInt(total_fee), 0);
				}
			}
		}
	}
}