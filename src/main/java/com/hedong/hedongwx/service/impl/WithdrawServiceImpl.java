package com.hedong.hedongwx.service.impl;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.wxpay.sdk.WXPayUtil;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.WithdrawDao;
import com.hedong.hedongwx.entity.MerchantDetail;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.Withdraw;
import com.hedong.hedongwx.service.MerchantDetailService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.service.WithdrawService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.RSAUtil;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.TempMsgUtil;
import com.hedong.hedongwx.utils.WeChatWithdrawUtils;
import com.hedong.hedongwx.utils.WeiXinConfigParam;
import com.hedong.hedongwx.utils.WeixinUtil;
import com.hedong.hedongwx.utils.XMLUtil;
import net.sf.json.JSONObject;

@Service
public class WithdrawServiceImpl implements WithdrawService {
	
	@Autowired
	private WithdrawDao withdrawDao;
	@Autowired
	private UserService userService;
	@Autowired
	private MerchantDetailService merchantDetailService;
	@Autowired
	private HttpServletRequest request;
	private static Logger logger = LoggerFactory.getLogger(WithdrawServiceImpl.class);
	
	@Override
	public int addWithdraw(Withdraw withdraw) {
		return withdrawDao.addWithdraw(withdraw);
	}

	@Override
	public int updateWithdraw(Withdraw withdraw) {
		return withdrawDao.updateWithdraw(withdraw);
	}

	@Override
	public List<Withdraw> getWithdrawListByUserId(Integer userId) {
		List<Withdraw> withdraw = withdrawDao.getWithdrawListByUserId(userId);
		if(withdraw == null || withdraw.size() == 0) withdraw = new ArrayList<Withdraw>();
		return withdraw;
	}

	@Override
	public Withdraw selectExtractInfo(Integer id) {
		return  withdrawDao.selectExtractInfo(id);
	}

	@Override
	public List<Withdraw> getWithdrawList() {
		return withdrawDao.getWithdrawList();
	}
	
	@Override
	public PageUtils<Withdraw> getAllWithdrawAndBankcard(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Withdraw> page  = new PageUtils<>(numPerPage, currentPage);
		Withdraw withdraw = new Withdraw(); 
		String status = request.getParameter("status");
		
		if(null!=status && !status.equals("-1") ) withdraw.setStatus(StringUtil.getIntString(status));
		withdraw.setPhoneNum(request.getParameter("phoneNum"));
		withdraw.setRealname(request.getParameter("realname"));
		
		withdraw.setWithdrawnum(request.getParameter("withdrawnum"));
		withdraw.setBankcardnum(request.getParameter("bankcardnum"));
		withdraw.setBankname(request.getParameter("bankname"));
		withdraw.setBegintime(request.getParameter("startTime"));
		withdraw.setEndtime(request.getParameter("endTime"));
		List<Withdraw> withdrawfir = withdrawDao.getAllWithdrawAndBankcard(withdraw);
		page.setTotalRows(withdrawfir.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd(); 
		withdraw.setNumPerPage(page.getNumPerPage());
		withdraw.setStartIndex(page.getStartIndex());
		page.setList(withdrawDao.getAllWithdrawAndBankcard(withdraw));
		return page;
	}
	
	@Override
	public Withdraw getEndRecordByUserId(Integer userId) {
		return withdrawDao.getEndRecordByUserId(userId);
	}

	/**
	 * @Description： 查询提现总和与手续费总和
	 * @author： origin          
	 * @param withdraw
	 */
	@Override
	public Map<String, Object>  selectmoneysum(Withdraw withdraw) {
		Map<String, Object> withd = withdrawDao.selectmoneysum(withdraw);
		return withd != null ? withd : new HashMap<String, Object>();
	}

	@Override
	public Withdraw selectWithdrawByOrdernum(String ordernum) {
		return withdrawDao.selectWithdrawByOrdernum(ordernum);
	}
	
	@Override
	public Map<String, Object> selectwitmoney(Integer uid) {
		try {
			Map<String, Object> result = withdrawDao.selectwitmoney(uid);
			return result != null ? result : new HashMap<String, Object>();
		} catch (Exception e) {
			e.printStackTrace();
			return  new HashMap<String, Object>();
		}
	}
	
	/**
	 * @Description： 查询待提现金额
	 * @author： origin 
	 */
	@Override
	public Map<String, Object> pendingmoney(Integer merid, String status, String createTime, String accountTime) {
		try {
			Map<String, Object> result = withdrawDao.pendingmoney( merid, status, createTime, accountTime);
			return result != null ? result : new HashMap<String, Object>();
		} catch (Exception e) {
			e.printStackTrace();
			return  new HashMap<String, Object>();
		}
	}
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	@Override
	public PageUtils<Parameters> selectWithdrawRecord(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		User user = CommonConfig.getAdminReq(request);
		Integer rank = user.getLevel();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(rank!=0) parameters.setUid(user.getId());//获取用户
		String startTime = request.getParameter("startTime");
		if(startTime==null) startTime = StringUtil.getPastDate(30) + " 00:00:00";
		String endTime = request.getParameter("endTime");
		if(endTime==null) endTime = StringUtil.toDateTime();
		String status = request.getParameter("status");
		if(null!=status && !status.equals("-1") ) parameters.setStatus(request.getParameter("status"));
		parameters.setOrder(request.getParameter("withdrawnum"));
		parameters.setUsername(request.getParameter("realname"));
		parameters.setPhone(request.getParameter("phoneNum"));
		parameters.setNumber(request.getParameter("bankcardnum"));//银行卡号
		parameters.setParamete(request.getParameter("bankname"));
		String type = request.getParameter("type");
		parameters.setType(type);
		parameters.setStartTime(startTime);
		parameters.setEndTime(endTime);
		
		List<Map<String, Object>> withdrawrec = withdrawDao.selectWithdrawRecord(parameters);
		page.setTotalRows(withdrawrec.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		page.setListMap(withdrawDao.selectWithdrawRecord(parameters));
		return page; 
	}

	@Override
	public Double selectAllMoneyByUidAndTime(Integer uid, String begintime, String endtime) {
		return withdrawDao.selectAllMoneyByUidAndTime(uid, begintime, endtime);
	}

	@Override
	public List<Withdraw> getWithdrawListByBankcardnum(String bankcardnum) {
		return withdrawDao.getWithdrawListByBankcardnum(bankcardnum);
	}

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月10日 下午6:58:02 
	 */
	@Override
	public Object withdrawDispose(Integer id, Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Withdraw withdraw = withdrawDao.selectExtractInfo(id);
			if (withdraw == null) {
				map = CommonConfig.messg(401);
				return map;
			}
			Date date = new Date();
			Integer state = withdraw.getStatus();
			if(state==0 || state == 4){
				withdraw.setStatus(status);
				withdraw.setAccountTime(date);
				withdrawDao.updateWithdraw(withdraw);
				String bankcardnum = withdraw.getBankcardnum();
				User user = userService.selectUserById(withdraw.getUserId());
				map = CommonConfig.messg(200);
				try {
					String url = TempMsgUtil.TEMP_MSG_URL.replace("ACCESS_TOKEN", WeixinUtil.getBasicAccessToken());
					Double money = withdraw.getMoney();
					Double servicecharge = withdraw.getServicecharge();
					double round = Math.round((money*100 - servicecharge*100)) + 0.0;					  
					JSONObject jsonbj = TempMsgUtil.information2(user.getOpenid(), TempMsgUtil.TEMP_ID2, CommonConfig.ZIZHUCHARGE + "/checkOrderDetail?ordernum=" + withdraw.getWithdrawnum(), 
							"提现已处理，将在近期到账，请注意查收", withdraw.getBankname() + "(" + bankcardnum.substring(bankcardnum.length()-4, bankcardnum.length()) + ")", round/100 + "", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), "如有疑问，请尽快联系客服");
					logger.info("输出jsonbj：   "+jsonbj);
					TempMsgUtil.PostSendMsg(jsonbj, url);
				} catch (Exception e) {
					map.put("code", 406);
					map.put("messg", "推送消息失败");
				}
			}else{
				map.put("code", 405);
				map.put("messg", "订单状态不对");
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map = CommonConfig.messg(403);
			logger.error(e.getMessage());
			return map;
		}
	}

	//=====================================================================================
	/**
	 * separate
	 * @Description：提现记录信息查看
	 * @author： origin 
	 */
	@Override
	public Object withdrawRecordData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			User user = CommonConfig.getAdminReq(request);
			Parameters parameters = new Parameters();
			Integer rank = CommUtil.toInteger(user.getLevel());
			if(!rank.equals(0)) parameters.setUid(user.getId());
			
			String status = CommUtil.toString(maparam.get("status"));
			parameters.setStatus(status);
			parameters.setOrder(CommUtil.toString(maparam.get("ordernum")));
			parameters.setUsername(CommUtil.toString(maparam.get("realname")));
			parameters.setPhone(CommUtil.toString(maparam.get("phone")));
			parameters.setNumber(CommUtil.toString(maparam.get("bankcardnum")));//银行卡号
			parameters.setParamete(CommUtil.toString(maparam.get("bankname")));
			String type = CommUtil.toString(maparam.get("type"));
			parameters.setType(type);

			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
//			parameters.setStartTime(CommUtil.getRelevantDate(maparam.get("startTime"), 30, 1));
//			parameters.setEndTime(CommUtil.getRelevantDate(maparam.get("endTime"), 0, 3));
			
			List<Map<String, Object>> withdrawData = withdrawDao.selectWithdrawRecord(parameters);
			page.setTotalRows(withdrawData.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> withdrawInfo = withdrawDao.selectWithdrawRecord(parameters);
			for(Map<String, Object> item : withdrawInfo){
				Double withdrawmoney = CommUtil.toDouble(item.get("money"));
				Double servicecharge = CommUtil.toDouble(item.get("servicecharge"));
				Double arrivemoney = CommUtil.subBig(withdrawmoney, servicecharge);
				item.put("arrivemoney", arrivemoney);
			}
			datamap.put("listdata", CommUtil.isListMapEmpty(withdrawInfo));
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**
	 * separate
	 * @Description：  提现判断
	 * @author： origin   2019年11月15日 下午6:00:58
	 */
	@Override
	public Object withdrawResolve(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer id =  CommUtil.toInteger(maparam.get("id"));
			Integer status =  CommUtil.toInteger(maparam.get("status"));
//			Double money =  CommUtil.toDouble(maparam.get("money"));
			Withdraw withdraw = withdrawDao.selectExtractInfo(id);
			if (withdraw == null) withdraw = new Withdraw();
			Integer withstatus = CommUtil.toInteger(withdraw.getStatus());
			if(!withstatus.equals(0) && !withstatus.equals(4)){
				CommUtil.responseBuildInfo(105, "不是待处理类型的订单", datamap);
				return datamap;
			}else{
//				money =  money == null ? CommUtil.toDouble(withdraw.getMoney()) : money;
				Double money =  CommUtil.toDouble(withdraw.getMoney());
				Double servicecharge = CommUtil.toDouble(withdraw.getServicecharge());
				Date date = new Date();
				withdraw.setStatus(status);
				withdraw.setAccountTime(date);
				withdrawDao.updateWithdraw(withdraw);
				String bankcardnum = withdraw.getBankcardnum();
				Double round = CommUtil.subBig(money, servicecharge)*100;
//				double round = Math.round((money*100 - servicecharge*100)) + 0.0;					  
				User user = userService.selectUserById(withdraw.getUserId());
				String fistinfo = "";
				String url = TempMsgUtil.TEMP_MSG_URL.replace("ACCESS_TOKEN", WeixinUtil.getBasicAccessToken());
				if (status.equals(1)){
					fistinfo = "提现已处理，将在近期到账，请注意查收";
				}else if (status.equals(2)) {
					fistinfo = "提现被拒绝，请联系管理员查询原因";
					if (user != null) {
						Double earnings = CommUtil.toDouble(user.getEarnings());
						earnings =CommUtil.addBig(earnings, money);
						user.setEarnings(earnings);
						userService.updateUserById(user);
						merchantDetailService.insertMerEarningDetail(user.getId(), money, earnings, withdraw.getWithdrawnum(), date, MerchantDetail.WITHDRAWSOURCE, 0, MerchantDetail.WITHDRAWFAIL);
					}
				}
				try {
					JSONObject jsonbj = TempMsgUtil.information2(user.getOpenid(), TempMsgUtil.TEMP_ID2, CommonConfig.ZIZHUCHARGE + "/checkOrderDetail?ordernum=" + withdraw.getWithdrawnum(), 
							fistinfo, withdraw.getBankname() + "(" + bankcardnum.substring(bankcardnum.length()-4, bankcardnum.length()) + ")", round/100 + "", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), "如有疑问，请尽快联系客服");
					logger.info("输出jsonbj：   "+jsonbj);
					TempMsgUtil.PostSendMsg(jsonbj, url);
				} catch (Exception e) {
					datamap.put("code", 406);
					datamap.put("messgs", "推送消息失败");
				}
				return CommUtil.responseBuildInfo(200, "成功", datamap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	/**
	 * 商家自动提现到微信零钱
	 * 
	 */
	@Override
	public void merAutoWithdraw() {
		// 查询所有的开启自动提现收益大于5元的商家
		List<User> users = userService.selectOnAutoWithdrawMer();
		SimpleDateFormat ordera = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		logger.info("开始商家的自动提现=="+ordera.format(System.currentTimeMillis()));
		// 没有商家开启,直接跳过
		if(users.size() == 0) return;
		for (int i = 0; i < users.size(); i++) {
			// 获取商家的收益信息
			User user = users.get(i);
			logger.info("开始处理商家=="+user.getId()+"==数据==="+ordera.format(System.currentTimeMillis()));
			// 订单
			String format = "425" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+user.getId();
			Integer random=(int)(Math.random()*90000)+10000;
			String order = format+random;
			// 存放用户的提现信息
			Map<String,String> hashMap = new ConcurrentHashMap<String, String>();
			// 商家的费率
			Integer feerate = user.getFeerate();
			// 商家提现的钱
			Double money = null;
			// 商家的真实姓名
			String realName = user.getRealname();
			if(realName == null || "".equals(realName)){
				logger.info("商家:"+user.getId()+"不存在真实姓名");
				continue;
			}
			// 商家得收益大于1000元,提现1000元
			// 商家得收益大于5元小于1000,全部提现
			if(user.getEarnings() > 2000.00){
				money = 2000.00;
			}else{
				money = user.getEarnings();
			}
			Double calcServMoney = (money * feerate + 4 + 0.000)/10;
			Double round = Math.round(calcServMoney) + 0.0;
			Double calcMoney = money * 100 - round.intValue();
			// 商家的openid
			hashMap.put("openid", user.getOpenid());
			// 商家的订单号
			hashMap.put("ordernum", order);
			// 真实姓名
			hashMap.put("realname", realName);
			// 用户提现的钱
			hashMap.put("money", calcMoney.intValue() + "");
			// 信息
			hashMap.put("info", "自动提现");
			// IP地址
			//hashMap.put("ip", WxPayController.getIpAddress(request));
			try {
				// 提现的请求
				logger.info("商家:"+user.getId()+"开始请求微信接口提现==时间:"+ordera.format(System.currentTimeMillis()));
				String result = WeChatWithdrawUtils.withdrawRequestOnce(hashMap, 3000, 3000, true);
				// 提现的信息
				@SuppressWarnings("unchecked")
				Map<String, String> resultMap = XMLUtil.doXMLParse(result);
				// 获取微信请求回来的信息
				if (resultMap.containsKey("result_code") && "SUCCESS".equals(resultMap.getOrDefault("result_code", ""))) {
					logger.info("商家:"+realName+"在:"+order+"自动提现:"+calcMoney/100+"元--到微信零钱提现成功");
					Withdraw withdraw = new Withdraw();
					// 费率为空或0
					if (feerate == null || feerate == 0) {
						// 计算服务费
						withdraw.setServicecharge((money * 6 + 4 + 0.000)/1000);
					} else {
						// 计算服务费
						withdraw.setServicecharge((money * feerate + 4 + 0.000)/1000);
					}
					// 商家的钱
					Double userMoney = user.getEarnings();
					// 用户的钱减提现的钱
					userMoney = userMoney * 100 - money * 100;
					// 修改用户提现后的金额
					userService.updateUserEarnings(0, money, user.getId());
					// 添加提现记录
					withdraw.setCreateTime(new Date());
					withdraw.setWithdrawnum(order);
					withdraw.setBankcardnum("0");
					withdraw.setBankname("微信零钱");
					withdraw.setMoney(money);
					withdraw.setStatus(3);
					withdraw.setUserId(user.getId());
					withdraw.setUserMoney(user.getEarnings());
					withdrawDao.addWithdraw(withdraw);
					// 添加用户资金变动明细
					merchantDetailService.insertMerEarningDetail(user.getId(), money, userMoney / 100, withdraw.getWithdrawnum(), new Date(), MerchantDetail.WITHDRAWSOURCE, 0, MerchantDetail.WITHDRAWACCESS);
					String url = TempMsgUtil.TEMP_MSG_URL.replace("ACCESS_TOKEN", WeixinUtil.getBasicAccessToken());
					Double money1 = withdraw.getMoney();
					Double servicecharge = withdraw.getServicecharge();
					double round1 = Math.round((money1*100 - servicecharge*100)) + 0.0;
					JSONObject jsonbj = TempMsgUtil.information2(user.getOpenid(), TempMsgUtil.TEMP_ID2, CommonConfig.ZIZHUCHARGE+"/checkOrderDetail?ordernum=" + withdraw.getWithdrawnum(), 
							"自动提现已到账", "微信零钱", round1/100 + "元", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "如有疑问，请尽快联系客服");
//					logger.info("输出jsonbj：   "+jsonbj);
					// 发送提现信息
					TempMsgUtil.PostSendMsg(jsonbj, url);
					logger.info("商家:"+user.getId()+"完成微信提现=====时间:"+ordera.format(System.currentTimeMillis()));
				} else {
					logger.info("用户" + user.getRealname() + "---微信零钱提现失败---");
					String url = TempMsgUtil.TEMP_MSG_URL.replace("ACCESS_TOKEN", WeixinUtil.getBasicAccessToken());
					JSONObject jsonbj = TempMsgUtil.information2(user.getOpenid(), TempMsgUtil.TEMP_ID2, CommonConfig.ZIZHUCHARGE+"/oauth2login", 
							"自动提现失败-"+resultMap.get("err_code_des"), "微信零钱", calcMoney/100 + "元", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "如有疑问，请尽快联系客服");
//					logger.info("输出jsonbj：   "+jsonbj);
					TempMsgUtil.PostSendMsg(jsonbj, url);
					logger.info("商家:"+user.getId()+"发送微信请求结束=====时间:"+ordera.format(System.currentTimeMillis()));
				}
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.warn(e.getMessage());
				System.out.println("商家:"+user.getId()+"出现异常===时间:"+ordera.format(System.currentTimeMillis()));
				String url;
				try {
					url = TempMsgUtil.TEMP_MSG_URL.replace("ACCESS_TOKEN", WeixinUtil.getBasicAccessToken());
					JSONObject jsonbj = TempMsgUtil.information2(user.getOpenid(), TempMsgUtil.TEMP_ID2, CommonConfig.ZIZHUCHARGE+"/oauth2login", 
							"自动提现失败", "微信零钱", calcMoney/100 + "元", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "如有疑问，请尽快联系客服");
//					logger.info("输出jsonbj：   "+jsonbj);
					logger.info("用户" + user.getRealname() + "---微信零钱提现失败---");
					TempMsgUtil.PostSendMsg(jsonbj, url);
				} catch (Exception e1) {
					logger.warn(e1.getMessage());
				}
			}
		}
	}

	/**
	 * 提现到个人银行卡(不成功后手动进行处理)
	 * 添加事务,发生异常直接回滚
	 */
	@Override
	@Transactional
	public Object withdrawPersonBank(String bankcardnum, String withDrawNum) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			// 参数判断
			if(bankcardnum == null || "".equals(bankcardnum)){
				CommUtil.responseBuildInfo(102, "传参错误", datamap);
				return datamap;
			}
			if(withDrawNum == null || "".equals(withDrawNum)){
				CommUtil.responseBuildInfo(102, "传参错误", datamap);
				return datamap;
			}
			// 根据订单号和银行卡号查询待确认的订单
			Map<String,Object> withdraw = withdrawDao.selectWithdrawByBankId(bankcardnum,withDrawNum);
			if(withdraw == null){
				CommUtil.responseBuildInfo(102, "订单不存在", datamap);
				return datamap;
			}
			// 银行的名字
			String bankName = CommUtil.toString(withdraw.get("bankname"));
			// 商家提现的钱
			Double money = CommUtil.toDouble(withdraw.get("money"));
			// 商家的真实姓名
			String merRealName = CommUtil.toString(withdraw.get("realname"));
			// 服务费
			Double serviceCharge = CommUtil.toDouble(withdraw.get("servicecharge"));
			// 待提现的订单主键id
			Integer withDrawId = CommUtil.toInteger(withdraw.get("id"));
			// 商家openid
			String openid = CommUtil.toString(withdraw.get("openid"));
			// 付款金额(提现的钱减去手续费)
			Double merUseMoney = CommUtil.subBig(money*100.00, serviceCharge*100.00);
            if(merRealName != null && bankName != null){
				// 调用微信接口进行企业打款到银行卡
				SortedMap<String, String> params = new TreeMap<String,String>();
				params.put("mch_id", WeiXinConfigParam.SUBMCHID);
				params.put("partner_trade_no", withDrawNum);
				params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
				//定义自己公钥的路径
				String keyfile = "/usr/local/elk/9951publicPKCS8.pem"; 
				PublicKey pub = RSAUtil.getPubKey(keyfile,"RSA"); 
				//rsa是微信付款到银行卡要求我们填充的字符串
				String rsa ="RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING";
				// 进行加密
				byte[] estr=RSAUtil.encrypt(bankcardnum.getBytes(),pub,2048, 11,rsa);   //对银行账号进行base加密
				String bankNum = Base64.encodeBase64String(estr);
				byte[] useName = RSAUtil.encrypt(merRealName.getBytes(),pub,2048, 11,rsa);
				String realName = Base64.encodeBase64String(useName);
				// 加密的银行卡号
	            params.put("enc_bank_no", bankNum);
				// 加密后的收款人姓名
	            params.put("enc_true_name", realName);
				// 收款方开户行(微信对银行的编码)
				params.put("bank_code", WXPayUtil.getBankCode(bankName));
				// 商家提现的钱
				params.put("amount", merUseMoney.intValue()+"");
				// 描述
				params.put("desc", "自动提现到银行卡");
				String sign = HttpRequest.createWithdrawSign("UTF-8", params);
				params.put("sign", sign);
				// 发送带有证书的post请求
				String result = WeChatWithdrawUtils.withdrawToPersonBank(params, 3000, 3000, true);
				// 提现的信息转成Map
				Map<String, String> resultMap = XMLUtil.doXMLParse(result);
				// 请求成功获取微信返回的信息
				if(resultMap.get("result_code").toString().equalsIgnoreCase("SUCCESS")){
					// 开始处理业务
					Withdraw withdrawData = new Withdraw();
					Date date = new Date();
					withdrawData.setId(withDrawId);
					withdrawData.setStatus(1);
					withdrawData.setAccountTime(date);
					withdrawDao.updateWithdraw(withdrawData);
					// 发送提现信息
					String info = "您的提现申请已受理,预计1~3个工作日到账,周末节假日顺延,请以实际到账时间为准。";
					String url = TempMsgUtil.TEMP_MSG_URL.replace("ACCESS_TOKEN", WeixinUtil.getBasicAccessToken());
					JSONObject jsonbj = TempMsgUtil.information2(openid, TempMsgUtil.TEMP_ID2, CommonConfig.ZIZHUCHARGE+"/checkOrderDetail?ordernum=" + withDrawNum, 
							info ,  bankName+ "(" + bankcardnum.substring(bankcardnum.length()-4, bankcardnum.length()) + ")", merUseMoney/100+"(元)", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "如有疑问，请尽快联系客服");
					TempMsgUtil.PostSendMsg(jsonbj, url);
					logger.info("商家:"+merRealName+"完成提现到银行=====时间:"+System.currentTimeMillis()+"钱=="+merUseMoney/100);
					return CommUtil.responseBuildInfo(200, "成功", datamap);
				}else{
					// 发送提现信息
					logger.info("用户:" + merRealName + "---微信提现银行卡失败---");
					logger.info("错误信息=="+resultMap.get("err_code_des"));
					String url = TempMsgUtil.TEMP_MSG_URL.replace("ACCESS_TOKEN", WeixinUtil.getBasicAccessToken());
					JSONObject jsonbj = TempMsgUtil.information2(openid, TempMsgUtil.TEMP_ID2, CommonConfig.ZIZHUCHARGE+"/oauth2login", 
							"自动提现失败-"+resultMap.get("err_code_des"),  bankName+ "(" + bankcardnum.substring(bankcardnum.length()-4, bankcardnum.length()) + ")",  merUseMoney/100+"(元)", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "如有疑问，请尽快联系客服");
					TempMsgUtil.PostSendMsg(jsonbj, url);
					logger.info("商家:"+merRealName+"发送微信请求结束=====时间:"+System.currentTimeMillis());
					return CommUtil.responseBuildInfo(102, "请求微信错误", datamap);
				}
			}else{
				return CommUtil.responseBuildInfo(102, "商家信息错误", datamap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(102, "捕获异常", datamap);
		}
	}
}
