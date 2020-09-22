package com.hedong.hedongwx.web.controller.computerpc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.Withdraw;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.service.WithdrawService;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.TempMsgUtil;
import com.hedong.hedongwx.utils.WeixinUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/pcmoney")
public class MoneywithdrawController {
	
	@Autowired
	private static Logger logger = LoggerFactory.getLogger(MoneywithdrawController.class);
	@Autowired
	private WithdrawService withdrawService;
	@Autowired
	private UserService userService;
	@Autowired
	private MerchantDetailService merchantDetailService;
	
	/**  提现记录 */
	@RequestMapping(value = "/managewithdraw")
	public String managewithdraw(HttpServletRequest request,  HttpServletResponse response, Model model) throws ParseException, IOException {
		PageUtils<Withdraw> pageBean = withdrawService.getAllWithdrawAndBankcard(request);
		model.addAttribute("withdrawList", pageBean.getList());
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("withdrawnum", request.getParameter("withdrawnum"));
		model.addAttribute("realname", request.getParameter("realname"));
		model.addAttribute("phoneNum", request.getParameter("phoneNum"));
		model.addAttribute("bankcardnum", request.getParameter("bankcardnum"));
		model.addAttribute("bankname", request.getParameter("bankname"));
		model.addAttribute("status", request.getParameter("status"));
		model.addAttribute("startTime", request.getParameter("startTime"));
		model.addAttribute("endTime", request.getParameter("endTime"));
		return "computer/managewithdraw";
	}
	
	@RequestMapping(value = "/setWithdrawStatusPass", method = RequestMethod.POST)
	@ResponseBody
	public Object setWithdrawStatus(Integer id, Integer status) {
		Object result = withdrawService.withdrawDispose(id, status);
		return result;
	}
	
	@RequestMapping(value = "/setWithdrawStatusFail", method = RequestMethod.POST)
	@ResponseBody
	public String setWithdrawStatusFail(Integer id, Integer status,Double money,Integer userId) {
		Withdraw withdraw = withdrawService.selectExtractInfo(id);
		if (withdraw != null) {
			if (withdraw.getStatus() != 0 && withdraw.getStatus() != 4) {
				return "0";
			} else {
				Date date = new Date();
				withdraw.setStatus(status);
				withdraw.setAccountTime(date);
				withdrawService.updateWithdraw(withdraw);
				if (status == 2) {
					User user = userService.selectUserById(withdraw.getUserId());
					if (user != null) {
						Double money2 = user.getEarnings();
						money2 = money2 * 100 + money * 100;
						user.setEarnings(money2 / 100);
						userService.updateUserById(user);
						merchantDetailService.insertMerEarningDetail(user.getId(), money, money2 / 100, withdraw.getWithdrawnum(), date, MerchantDetail.WITHDRAWSOURCE, 0, MerchantDetail.WITHDRAWFAIL);
						String bankcardnum = withdraw.getBankcardnum();
						try {
							String url = TempMsgUtil.TEMP_MSG_URL.replace("ACCESS_TOKEN", WeixinUtil.getBasicAccessToken());
							Double money1 = withdraw.getMoney();
							Double servicecharge = withdraw.getServicecharge();
							double round = Math.round((money1*100 - servicecharge*100)) + 0.0;					  
							JSONObject jsonbj = TempMsgUtil.information2(user.getOpenid(), TempMsgUtil.TEMP_ID2, CommonConfig.ZIZHUCHARGE + "/checkOrderDetail?ordernum=" + withdraw.getWithdrawnum(), 
									"提现已处理，将在近期到账，请注意查收", withdraw.getBankname() + "(" + bankcardnum.substring(bankcardnum.length()-4, bankcardnum.length()) + ")", round/100 + "", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), "如有疑问，请尽快联系客服");
							logger.info("输出jsonbj：   "+jsonbj);
							TempMsgUtil.PostSendMsg(jsonbj, url);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				return "1";
			}
		} else {
			return "0";
		}
	}
	
}
