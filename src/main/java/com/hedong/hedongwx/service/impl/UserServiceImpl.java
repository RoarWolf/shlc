package com.hedong.hedongwx.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hedong.hedongwx.dao.*;
import com.hedong.hedongwx.entity.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.service.GeneralDetailService;
import com.hedong.hedongwx.service.MoneyService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.JedisUtils;
import com.hedong.hedongwx.utils.MD5Util;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.WeixinUtil;
import com.hedong.hedongwx.utils.WxpayUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TradeRecordDao tradeRecordDao;
	@Autowired
	private CodestatisticsDao codestatisticsDao;
	@Autowired
	private PackageMonthDao packageMonthDao;
	@Autowired
	private EquipmentDao equipmentDao;
	@Autowired
	private WithdrawDao withdrawDao;
	@Autowired
	private UserEquipmentDao userEquipmentDao;
	@Autowired
	private UserBankcardDao userBankcardDao;
	@Autowired
	private MerchantDetailDao merchantDetailDao;
	@Autowired
	private OperateRecordDao operateRecordDao;
	@Autowired
	private GeneralDetailDao generalDetailDao;
	@Autowired
	private DealerAuthorityDao dealerAuthorityDao;
	@Autowired
	private PrivilegeDao privilegeDao;
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private GeneralDetailService generalDetailService;

	@Autowired
	private AdminDao adminDao;
	
	
	@Transactional
	@Override
	public int addUser(User user) {
		return userDao.addUser(user);
	}
	
	@Transactional
	@Override
	public int addUserInfo(String openid) {
		User user = new User();
		user.setOpenid(openid);
		return userDao.addUser(user);
	}

	@Transactional
	@Override
	public int deleteUserById(Integer id) {
		return userDao.deleteUserById(id);
	}

	@Override
	public User selectUserById(Integer id) {
		return userDao.selectUserById(id);
	}

	@Transactional
	@Override
	public int updateUserById(User user) {
		return userDao.updateUserById(user);
	}

	@Override
	public List<User> getUserList() {
		return userDao.getUserList();
	}

	@Override
	public List<User> getUserListByRank(Integer rank) {
		return userDao.getUserListByRank(rank);
	}

	@Override
	public User getUserByPhoneNumAndPassword(String phoneNum, String password) {
		return userDao.getUserByPhoneNumAndPassword(phoneNum, password);
	}
	
	@Override
	public User getUserByPhoneNum(String phoneNum) {
		return userDao.getUserByPhoneNum(phoneNum);
	}

	@Override
	public User getUserByOpenid(String openid) {
		if(openid == null || "".equals(openid)){
			return null;
		}
		//查询平台用户
		User user = userDao.getUserByOpenid(openid);
		if(user == null){
			// 查询服务商下用户
			User user2 = userDao.selectSubUserByOpenid(openid);
			if(user2 == null){
				System.out.println("服务商和平台下都不存在用户");
				return null;
			}else{
				return user2;
			}
		}
		return user;
	}
	
	@Override
	public int unbindUserAndAid(Integer aid) {
		return userDao.unbindUserAndAid(aid);
	}

	
	/**
	 * @Description： 更新账户信息
	 * @author： origin   2019年11月26日 下午2:25:09 
	 */
	@Override
	public Object editAccountData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer id =  CommUtil.forInteger(maparam.get("id"));
			Integer payhint =  CommUtil.forInteger(maparam.get("payhint"));
			String phone =  CommUtil.toString(maparam.get("phone"));
			User user = new User();
			user.setId(id);
			user.setPhoneNum(phone);
			user.setPayhint(payhint);
			userDao.updateUserById(user);
			User account = userDao.selectUserById(id);
			datamap.put("listdata", account);
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**
	 * @Description：  更新账户自己信息
	 * @author： origin   2019年12月2日 下午5:05:41 
	 */
	@Override
	public Object editAccountAloneData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			Integer sourcepath =  CommUtil.forInteger(maparam.get("sourcepath"));
			Integer uid =  CommUtil.toInteger(maparam.get("uid"));
			Integer payhint =  CommUtil.forInteger(maparam.get("payhint"));
			User user = new User();
			user.setId(uid);
			user.setPayhint(payhint);
			userDao.updateUserById(user);
			User accountdata = userDao.selectUserById(uid);
			datamap.put("listdata", accountdata);
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	@Override
	public Object registerUser(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		String mobile = request.getParameter("mobile");
		String time = request.getParameter("time");
		String captcha = request.getParameter("captcha");
		String captchaNum = request.getParameter("captchaNum");
		String invitecode = request.getParameter("invitecode");
		int compTime = StringUtil.comparTime(new Date().getTime(),Long.parseLong(time));
		boolean flag = false;
		if(!invitecode.equals("888888")){
			//bindtype  设备绑定状态1:已绑定     0:未绑定 
			if (invitecode.length() == 6) {
				Equipment equip = equipmentDao.getEquipmentById(invitecode);
				equip = equip == null ? new Equipment() : equip;
				Byte bindtype = equip.getBindtype();
				if(bindtype==null || bindtype == 1){
					result.put("message", "邀请码不正确");
					return result;
				}
			} else if (invitecode.length() == 11) {
				User user = userDao.selectAdmin(invitecode);
				if (user == null) {
					result.put("message", "邀请码不正确");
					return result;
				} else if (user.getLevel() != 2) {
					result.put("message", "邀请码不正确");
					return result;
				} else {
					flag = true;
				}
			}else{
				result.put("message", "邀请码不正确");
				return result;
			}
		}
		if(existAdmin(mobile)!=null){
			result.put("message", "该手机号已存在");
		}else if(!captcha.equals(captchaNum)){
			result.put("message", "验证码码不正确");
		}else if(compTime>3){
			result.put("message", "验证码超时");
		}else{
			User user = (User)request.getSession().getAttribute("user");
			if(user!=null){
				String password = mobile.substring(mobile.length()-6);
				user.setPhoneNum(mobile);
				user.setServephone(mobile);
				user.setPassword(MD5Util.MD5Encode(password, "utf-8"));
				if (flag) {
					user.setLevel(1);
				} else {
					user.setLevel(2);
				}
				//if(StringUtil.getIntString(statuere)==1) user.setRank(2);
				userDao.registerUser(user);
				messSwitchAuthority(CommUtil.toInteger(user.getId()));
				result.put("code", 200);
			}else{
				result.put("message", "缓存超时，请重新登录");
			}
		}
		return result;
	}
	
	public void messSwitchAuthority( Integer merid){
		try {
			DealerAuthority deaut = dealerAuthorityDao.selectAuthority(null, merid);
			if(deaut==null){
				DealerAuthority author = new DealerAuthority();
				author.setMerid(merid);
				author.setShowincoins(2);
				dealerAuthorityDao.insertAuthority(author);
			}else{
				Integer showincoins = CommUtil.toInteger(deaut.getShowincoins());
				if(!showincoins.equals(2)){
					deaut.setMerid(merid);
					deaut.setShowincoins(2);
					dealerAuthorityDao.updateMeridAuthority(deaut);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 登录信息验证
	 * @param request， response
	 */
	@Override
	public String adminlogin(HttpServletRequest request, HttpServletResponse response) {
		String password = request.getParameter("Password");
		User admin = existAdmin(request.getParameter("phoneNum"));
		String argument = null;
		if(admin==null){
			argument = "不存在该用户，请重新输入";
		}else if(admin.getLevel()!=0 && admin.getLevel()!=2){
			argument = "该用户无登录权限，请重新输入";
		}else if(password==null){
			argument =  existCaptcha(request.getParameter("captcha"), request.getParameter("captchaNum"), request.getParameter("time"));
		}else if(password!=null){
			String pwd = DigestUtils.md5Hex(request.getParameter("Password"));
			if(!admin.getPassword().equals(pwd)){
				argument =  "用户名或密码错误";
			}else if(admin.getPassword().equals(pwd)){
				request.getSession().setAttribute("user",admin);
				request.getSession().setAttribute("admin",admin);
				if(admin.getLevel()==0) argument =  "0";
				if(admin.getLevel()==2) argument =  "1";
			}
		}
		return argument;
	}
	
	/**
	 * 判断用户表中是否存在该用户
	 * @return
	 * @throws Exception 
	 */
	public User existAdmin(String phoneNum) {
		User user = null;
		if(!StringUtils.isBlank(phoneNum)){
			user = userDao.selectAdmin(phoneNum);
		}
		return user;
	}

	/**
	 * 判断验证码时效和正确性
	 * @param captcha
	 * @param captchaNum
	 * @param time
	 * @return
	 */
	public String existCaptcha(String captcha, String captchaNum, String time) {
		String result =null;
		int compTime = StringUtil.comparTime(new Date().getTime(),Long.parseLong(time));
		if(compTime>3){
			result = "验证码过期！";
		}else if(!captcha.equals(captchaNum)){
			result = "验证码不正确";
		}
		return result;
	}

	@Override
	public int updateBalanceByOpenid(Double walletmoney, Double givemoney, String openid,Double earnings) {
		return userDao.updateBalanceByOpenid( walletmoney, givemoney, openid,earnings);
	}

	/**
	 * 查询管理员和商户信息
	 */
	@Override
	public List<Map<String, Object>> getUsertoSortList(String type) {
		Parameter parameter = new Parameter();
		parameter.setType(type);//用户类型（查询商户、和管理员）
		List<Map<String, Object>> usertosort = userDao.selectUserInfos(parameter);
		return usertosort;
	}

	/**
	 * 用户信息修改
	 */
	@Override
	public String saveuser(HttpServletRequest request) {
		String num = existCaptcha(request.getParameter("security"), request.getParameter("securities"), request.getParameter("securtime"));
		if(num!=null) return num;
		User admin = existAdmin(request.getParameter("phoneNum"));
		if(admin!=null){
			return "1";
		}
		User user = new User();
		user.setId(StringUtil.getIntString(request.getParameter("id")));
		user.setRealname(request.getParameter("realname"));
		user.setPhoneNum(request.getParameter("phoneNum"));
		user.setUpdateTime(new Date());
		userDao.updateUserById(user);
		
		return "0";
	}

	@Override
	public int bindUserBelongMerid(Integer uid, Integer merid) {
		return userDao.bindUserBelongMerid(uid, merid);
	}
	
	/**
	 * 商户的某个设备指定时间金额汇总
	 */
	@Override
	public Map<String, Object> agentnowmoneycollect(User user, String code, String begintime, String endtime) {
		Parameters parameters = new Parameters();
		parameters.setDealer(user.getId().toString());
		parameters.setStartTime(begintime);
		parameters.setEndTime(endtime);
		parameters.setCode(code);
		
		Map<String, Object> collectmoney = tradeRecordDao.timingCollectMoney(parameters);
		if(collectmoney!=null){
			int wechatnum = ((BigDecimal) collectmoney.get("wechatnum")).intValue();
			int alipaynum = ((BigDecimal) collectmoney.get("alipaynum")).intValue();
			double wechatmoney = (double) collectmoney.get("wechatmoney");
			double alipaymoney = (double) collectmoney.get("alipaymoney");
			double refwechatmoney = (double) collectmoney.get("refwechatmoney");
			double refalipaymoney = (double) collectmoney.get("refalipaymoney");
			int ordertotal = wechatnum + alipaynum;
			double moneytotal = wechatmoney + alipaymoney-refwechatmoney-refalipaymoney;
			collectmoney.put("wechatmoney", wechatmoney - refwechatmoney);
			collectmoney.put("alipaymoney", alipaymoney - refalipaymoney);
			collectmoney.put("ordertotal", ordertotal);
			collectmoney.put("moneytotal", moneytotal);
		}
		return collectmoney;
	}

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//根据Unionid查询用户(扫码登录使用)
	@Override
	public User getUserByUnionid(String unionid) {
		return userDao.getUserByUnionid(unionid);
	}
	
	/**
	 * 账号密码登录
	 */
	@Override
	public String accountlogin(String account, String password) {
		String argument = null;
		String pwdm = DigestUtils.md5Hex(password);
		User admin = existAdmin(account);
		if(admin==null){
			argument = "该用户不存在";
		}else if(admin.getLevel()!=0 && admin.getLevel()!=2){
			argument = "该用户无登录权限，请检查输入";
		}else if(!admin.getPassword().equals(pwdm)){
			argument =  "用户名或密码错误";
		}else{
			request.getSession().setAttribute("admin",admin);
			if(admin.getLevel()==0) argument =  "0";
			if(admin.getLevel()==2) argument =  "1";
		}
		return argument;
	}
	
	/**
	 * 短信验证码登录
	 */
	@Override
	public String codelogin(String mobile, String security, String authcode, String authtime) {
		String result =null;
		User admin = existAdmin(mobile);
		if(admin==null){
			result = "用户不存在！";
		}else if(admin.getLevel()!=0 && admin.getLevel()!=2){
			result = "该用户无登录权限，请检查输入";
		}else if(!security.equals(authcode)){
			result = "验证码不正确！";
		}else if(authtime!=null){
			long time = Long.parseLong(authtime);
			int compTime = StringUtil.comparTime(new Date().getTime(),time);
			if(compTime>3){
				result = "验证码过期！";
			}else{
				request.getSession().setAttribute("admin",admin);
				if(admin.getLevel()==0) result =  "0";
				if(admin.getLevel()==2) result =  "1";
			}
		}
		return result;
	}

	@Override
	public List<Codestatistics> selectAllByMerid(Integer merid,String begintime,String endtime) {
		return codestatisticsDao.selectAllByMerid(merid,begintime,endtime);
	}

	@Override
	public User getUserByOpenidApplet(String openidApplet) {
		return userDao.getUserByOpenidApplet(openidApplet);
	}
	
	/**
	 * @Description： 查询商户未体现金额
	 * @author： origin  
	 */
	@Override
	public Double earningsMoney(Integer uid) {
		return userDao.earningsMoney(uid);
	}

	/**
	 * new WChat
	 * @Description： 查询会员中心
	 * @author： origin          
	 * 创建时间：   2019年5月29日 下午6:14:37 
	 */
	@Override
	public Map<String, Object> selectmembercentre(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			Integer type =  CommUtil.toInteger(maparam.get("type"));
			String areaId =  CommUtil.toString(maparam.get("areaId"));
			String keywords =  CommUtil.toString(maparam.get("keywords"));
			User user = (User)request.getSession().getAttribute("user");
			Integer dealid = CommUtil.toInteger(user.getId());
			Parameters parame = new Parameters();
			parame.setUid(dealid);
			if(type.equals(1)){//会员号
				boolean flas = StringUtil.isNumeric(keywords);
				if(flas){
					Integer number = Integer.parseInt(keywords);
					parame.setCode(CommUtil.toString(number));	
				}
			}else if(type.equals(2)){//会员昵称
				parame.setNickname(keywords);
			}else if(type.equals(3)){//会员昵称
				parame.setPhone(keywords);
			}
			parame.setType(areaId);
			List<Map<String, Object>> membertotal = CommUtil.isListMapEmpty(userDao.selectmemberinfo(parame));
			Map<String, Object> memberbalance = CommUtil.isMapEmpty(userDao.selectmemberbalance(parame));
			Integer datasize = CommUtil.toInteger(membertotal.size());
			datamap.put("datasize", datasize);
			datamap.put("datamoney", CommUtil.toDouble(memberbalance.get("balance")));
			datamap.put("datasendmoney", CommUtil.toDouble(memberbalance.get("sendmoney")));
			int numPerPage =  CommUtil.toInteger(maparam.get("limit"));
			numPerPage = numPerPage == 0 ? 35 : numPerPage;
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			page.setTotalRows(datasize);
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parame.setPages(page.getNumPerPage());
			parame.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> member = CommUtil.isListMapEmpty(userDao.selectmemberinfo(parame));
			Parameters param = new Parameters();
			for(Map<String, Object> item : member){
				Integer uid = CommUtil.toInteger(item.get("id"));
				param.setDealer(CommUtil.toString(dealid));
				param.setUid(uid);
				List<Map<String, Object>> role = CommUtil.isListMapEmpty(roleDao.selectUserRoleList(param));
				if(role.size()>0){
					item.put("role", CommUtil.toInteger(role.get(0).get("proleid")));//角色id
					item.put("rolename", CommUtil.toString(role.get(0).get("rolename")));//角色名字
				}else{
					item.put("role", 5);
					item.put("rolename", "一级游客");
				}
			}
			datamap.put("datalist", member);
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	@Override
	public Map<String, Object> ajaxMemberCentre(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			Integer type =  CommUtil.toInteger(maparam.get("type"));
			String areaId =  CommUtil.toString(maparam.get("areaId"));
			String keywords =  CommUtil.toString(maparam.get("keywords"));
			User user = (User)request.getSession().getAttribute("user");
			Integer dealid = CommUtil.toInteger(user.getId());
			Parameters parame = new Parameters();
			parame.setUid(dealid);
			if(type.equals(1)){//会员号
				boolean flas = StringUtil.isNumeric(keywords);
				if(flas){
					Integer number = Integer.parseInt(keywords);
					parame.setCode(CommUtil.toString(number));	
				}
			}else if(type.equals(2)){//会员昵称
				parame.setNickname(keywords);
			}else if(type.equals(3)){//手机号
				parame.setPhone(keywords);
			}
			parame.setType(areaId);
			int numPerPage =  CommUtil.toInteger(maparam.get("limit"));
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			List<Map<String, Object>> membertotal = CommUtil.isListMapEmpty(userDao.selectmemberinfo(parame));
			Integer datasize = CommUtil.toInteger(membertotal.size());
			Map<String, Object> memberbalance = CommUtil.isMapEmpty(userDao.selectmemberbalance(parame));
			datamap.put("datasize", datasize);
			datamap.put("datamoney", CommUtil.toDouble(memberbalance.get("balance")));
			datamap.put("datasendmoney", CommUtil.toDouble(memberbalance.get("sendmoney")));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			page.setTotalRows(datasize);
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parame.setPages(page.getNumPerPage());
			parame.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> member = CommUtil.isListMapEmpty(userDao.selectmemberinfo(parame));
			Parameters param = new Parameters();
			for(Map<String, Object> item : member){
				Integer uid = CommUtil.toInteger(item.get("id"));
				param.setDealer(CommUtil.toString(dealid));
				param.setUid(uid);
				List<Map<String, Object>> role = roleDao.selectUserRoleList(param);
				if(role.size()>0){
					item.put("role", CommUtil.toInteger(role.get(0).get("proleid")));//角色id
					item.put("rolename", CommUtil.toString(role.get(0).get("rolename")));//角色名字
				}else{
					item.put("role", 5);
					item.put("rolename", "一级游客");
				}
			}
			datamap.put("datalist", member);
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			return CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	@Override
	public int updateUserEarnings(Integer edittype, Double money, Integer merid) {
		return userDao.updateUserEarnings(edittype, money, merid);
	}
	
	@Override
	public int updateMerAmount(Integer edittype, Double money, Integer merid) {
		return userDao.updateMerAmount(edittype, money, merid);
	}
	
	@Override
	public int insertMerAmount(Integer merid,Double totalOnlineEarn,Double nowOnlineEarn,
			Integer totalCoinsEarn,Integer nowCoinsEarn) {
		MerAmount merAmount = new MerAmount();
		merAmount.setMerid(merid);
		merAmount.setTotalOnlineEarn(totalOnlineEarn);
		merAmount.setTotalCoinsEarn(totalCoinsEarn);
		merAmount.setNowCoinsEarn(nowCoinsEarn);
		merAmount.setNowOnlineEarn(nowOnlineEarn);
		return userDao.insertMerAmount(merAmount);
	}
	
	@Override
	public MerAmount selectMerAmountByMerid(Integer merid) {
		return userDao.selectMerAmountByMerid(merid);
	}

	@Override
	public PackageMonth selectPackageMonthByUid(Integer uid) {
		return packageMonthDao.selectPackageMonthByUid(uid);
	}

	@Override
	public int insertPackageMonth(Integer uid,Integer surpnum,Integer todaysurpnum,Integer everydaynum,
			Integer changemonth, Integer everymonthnum, Integer time,Double elec, Date endTime, Integer monthnum) {
		PackageMonth pm = new PackageMonth();
		pm.setUid(uid);
		pm.setSurpnum(surpnum);
		pm.setTodaysurpnum(todaysurpnum);
		pm.setEverydaynum(everydaynum);
		pm.setMonthnum(changemonth);
		pm.setEverymonthnum(everymonthnum);
		pm.setTime(time);
		pm.setElec(elec);
		pm.setMonthnum(monthnum);
		pm.setEndTime(endTime);
		return packageMonthDao.insertPackageMonth(pm);
	}

	@Override
	public int updatePackageMonth(Integer uid,Integer surpnum,Integer todaysurpnum,Integer everydaynum,
			Integer changemonth, Integer everymonthnum, Integer time,Double elec, Date endTime, Integer monthnum) {
		PackageMonth pm = new PackageMonth();
		pm.setUid(uid);
		pm.setSurpnum(surpnum);
		pm.setTodaysurpnum(todaysurpnum);
		pm.setEverydaynum(everydaynum);
		pm.setMonthnum(changemonth);
		pm.setEverymonthnum(everymonthnum);
		pm.setTime(time);
		pm.setElec(elec);
		pm.setEndTime(endTime);
		pm.setMonthnum(monthnum);
		return packageMonthDao.updatePackageMonth(pm);
	}

	@Override
	public int updatePackageMonthByEntity(PackageMonth packageMonth) {
		return packageMonthDao.updatePackageMonth(packageMonth);
	}

	@Override
	public int insertPackageMonthRecord(Integer uid,Integer merid,String ordernum,Double money,
			Integer paysource,Integer status,Integer everydaynum,Integer changenum,Integer surpnum
			,Integer changemonth,Integer time,Double elec,Date createTime) {
		PackageMonthRecord pmr = new PackageMonthRecord();
		pmr.setUid(uid);
		pmr.setMerid(merid);
		pmr.setOrdernum(ordernum);
		pmr.setMoney(money);
		pmr.setPaysource(paysource);
		pmr.setStatus(status);
		pmr.setEverydaynum(everydaynum);
		pmr.setChangenum(changenum);
		pmr.setSurpnum(surpnum);
		pmr.setChangemonth(changemonth);
		pmr.setTime(time);
		pmr.setElec(elec);
		pmr.setCreateTime(createTime);
		return packageMonthDao.insertPackageMonthRecord(pmr);
	}

	@Override
	public int updatePackageMonthRecord(Integer id,Integer ifrefund,String ordernum,Integer status) {
		PackageMonthRecord pmr = new PackageMonthRecord();
		pmr.setId(id);
		pmr.setIfrefund(ifrefund);
		pmr.setOrdernum(ordernum);
		pmr.setStatus(status);
		return packageMonthDao.updatePackageMonthRecord(pmr);
	}

	@Override
	public PackageMonthRecord selectMonthRecordByOrdernum(String ordernum,	Integer status) {
		return packageMonthDao.selectMonthRecordByOrdernum(ordernum, status);
	}

	@Override
	public List<PackageMonthRecord> selectMonthRecordListByUid(Integer uid) {
		return packageMonthDao.selectMonthRecordListByUid(uid);
	}

	@Override
	public PackageMonthRecord selectMonthRecordById(Integer id) {
		return packageMonthDao.selectMonthRecordById(id);
	}

	@Override
	public int everydaynumReset() {
		return packageMonthDao.everydaynumReset();
	}
	
	@Override
	public Map<String, Object> memberRole(User user, String member, Integer role) {
		try {
			Integer merid = CommUtil.toInteger(user.getId());
			Parameters param = new Parameters();
			param.setDealer(merid.toString());
			param.setUid(Integer.parseInt(member));
			Map<String, Object> userrole = roleDao.selectUserRole(param);
			User tourist = userDao.selectUserById(CommUtil.toInteger(member));
			if(userrole==null || userrole.isEmpty() || userrole.size()==0){
				if(role==6 || role==7){
					roleDao.insertUserRole( merid, tourist.getId(), role);
				}
			}else{
				Integer userroleid = CommUtil.toInteger(userrole.get("pid"));
				if(role==5){
					roleDao.deleteUserRoleById(userroleid);
				}else{
					roleDao.updateUserRoleById(userroleid,role);
				}
			}
			return CommonConfig.messg(200);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonConfig.messg(403);
		}
	}

	@Override
	public int everydayResetNowEarn() {
		return userDao.everydayResetNowEarn();
	}

	@Override
	public Map<String, Object> selectUserRoleInfo(Integer id, Integer merid) {
		Parameters param = new Parameters();
		param.setUid(id);
		param.setDealer(merid.toString());
		Map<String, Object>  userole = roleDao.selectUserRole(param);
		return userole;
	}

	@Override
	public Codestatistics selectMerYestEarn(Integer merid, String countTime) {
		return codestatisticsDao.selectMerYestEarn(merid, countTime);
	}

	/**
	 * @Description： 更改总服务电话
	 * @author： origin   2019年10月10日 下午4:54:57 
	 */
	@Override
	public User editServicePhone(Integer id, String mobile) {
		User user = new User();
		try {//TODO 总服务电话未添加字段并修改
			user.setId(id);
			user.setPhoneNum(mobile);
			userDao.updateUserById(user);
			User userinfo = userDao.selectUserById(id);
			return userinfo;
		} catch (Exception e) {
			e.printStackTrace();
			return user;
		}
	}

	
	@Override
	public void editUserBalance(Double refundmoney, Integer type, Integer uid) {
		User userinfo = userDao.selectUserById(uid);
		Double modifymoney = 0.00;
		if(type==1){
			modifymoney = CommUtil.addBig(CommUtil.toDouble(userinfo.getBalance()), refundmoney);
		}else{
			modifymoney = CommUtil.subBig(CommUtil.toDouble(userinfo.getBalance()), refundmoney);
		}
		User user = new User();
		user.setId(CommUtil.toInteger(uid));
		user.setBalance(modifymoney);
		userDao.updateUserById(user);
	}

	//=== 前后台分离数据接口    ===========================================================================================
	
	/**
	 * 判断用户表中是否存在该用户
	 * @return
	 * @throws Exception 
	 */
	public Boolean isExistUser(String phone) {
		if(StringUtils.isBlank(phone)){
			return false;
		}
		User user = userDao.selectAdmin(phone);
		if(user==null){
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * 判断用户表中是否存在该用户
	 * @return
	 * @throws Exception 
	 */
	public Admin acquireUserData(String phone) {
		Admin user = null;
		if(!StringUtils.isBlank(phone)){
			user = adminDao.selectAdmin(phone);
		}
		return  user == null ? new Admin() : user;
	}
	
	/**
	 * separate
	 * @Description： 账户密码登录
	 * @author： origin  
	 */
	@Override
	public Map<String, Object> accountEnter(String accountnum, String password, Integer type) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			String md5word = DigestUtils.md5Hex(password);
			Admin account = acquireUserData(accountnum);
			String cipher = CommUtil.trimToEmpty(account.getEncryptPassword());
			if(account.getId()==null){
				return CommUtil.responseBuildInfo(101, "该用户不存在", datamap);
			} 
			if(!cipher.equals(md5word)){
				return CommUtil.responseBuildInfo(104, "密码不正确", datamap);
			}
			datamap.put("userName", account.getUsername());
			datamap.put("token", account.getId());
			request.getSession().setAttribute("user", account);
			request.getSession().setAttribute("admin", account);
			JedisUtils.set("admin", JSON.toJSONString(account),18000);
			return CommUtil.responseBuildInfo(200, "登录成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}
	
	/**
	 * separate
	 * @Description： 短信验证码登录
	 * @author： origin  
	 */
	@Override
	public Object captchaEnter(HttpServletRequest request) {
		/*Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			String phone =  CommUtil.toString(maparam.get("phone"));
			String captcha =  CommUtil.toString(maparam.get("captcha"));
			String authcode =  CommUtil.toString(maparam.get("authcode"));
			String sendtime =  CommUtil.toString(maparam.get("sendtime"));
			User account = acquireUserData(phone);
			if(account.getId()==null){
				return CommUtil.responseBuildInfo(101, "该用户不存在", datamap);
			} 
			if(!captcha.equals(authcode)){
				return CommUtil.responseBuildInfo(104, "验证码不正确", datamap);
			} 
			if(sendtime!=null){
//				long time = Long.parseLong(sendtime);
				Date times = CommUtil.DateTime(sendtime, "yyyy-MM-dd HH:mm:ss");
				int compTime = StringUtil.comparTime(new Date().getTime(),times.getTime());
				if(compTime>3){
					return CommUtil.responseBuildInfo(104, "验证码超时，请重新输入！", datamap);
				}
			}else{
				return CommUtil.responseBuildInfo(104, "时间验证失败！", datamap);
			}
			Integer rank = CommUtil.toInteger(account.getLevel());
			if(!rank.equals(0) && !rank.equals(2) && !rank.equals(3)){
				return CommUtil.responseBuildInfo(104, "该用户无登录权限，请重新输入", datamap);
			}
			if(rank==0){
				datamap.put("classify", "superAdmin");
			}else if(rank==2){
				datamap.put("classify", "Admin");
			}else if(rank==3){
				datamap.put("classify", "Agent");
			}
			datamap.put("userName", account.getUsername());
			datamap.put("token", account.getId());
			request.getSession().setAttribute("user", account);
			request.getSession().setAttribute("admin", account);
			return CommUtil.responseBuildInfo(200, "登录成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}*/
		return null;
	}
	
	/**
	 * separate
	 * @Description： 微信扫码登录
	 * @author： origin  
	 */
	@Override
	public Object wechatEnter(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		String unionid;
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			String code =  CommUtil.toString(maparam.get("code"));
			String state =  CommUtil.toString(maparam.get("state"));
			if(code==null){
				return CommUtil.responseBuildInfo(102, "参数code不存在", datamap);
			} 
			JSONObject userinfo = null;
			try {
//				userinfo = WeChatOpenPlatform.getOpenUserinfo(code);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(userinfo==null){
				return CommUtil.responseBuildInfo(103, "微信信息获取失败", datamap);
			} 
			unionid = CommUtil.toString(userinfo.getString("unionid"));
			User account = userDao.getUserByUnionid(unionid);
			if(account!=null){
				request.getSession().setAttribute("user", account);
				request.getSession().setAttribute("admin", account);
				Integer rank = CommUtil.toInteger(account.getLevel());
				if(!rank.equals(0) && !rank.equals(2) && !rank.equals(3)){
					return CommUtil.responseBuildInfo(104, "该用户无登录权限，请重新输入", datamap);
				}
				if(rank==0){
					datamap.put("classify", "superAdmin");
				}else if(rank==2){
					datamap.put("classify", "Admin");
				}else if(rank==3){
					datamap.put("classify", "Agent");
				}
				datamap.put("userName", account.getUsername());
				datamap.put("token", account.getId());
				return CommUtil.responseBuildInfo(200, "登录成功", datamap);
//				if(admin.getRank()==0) return "redirect:/pcstatistics/collectinfo";
//				else if(admin.getRank()==2) return "redirect:/pcstatistics/agentdatacollect";
			}else{
				datamap.put("code", 101);
				datamap.put("message", "不是管理员或商户，获取信息失败；</br>请先登录微信公众号商户中心，刷新当前页面在扫码登录。");
				return datamap;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
	}

	/**
	 * separate
	 * @Description： 获取商户信息
	 * @author： origin 
	 */
	@Override
	public Object getDealerListInfo(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage = CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			Parameters parameter = new Parameters();
			User user = CommonConfig.getAdminReq(request);
			Integer userrank = user.getLevel();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
			if(userrank!=0) parameter.setUid(user.getId());//获取用户
			Integer dealid = CommUtil.toInteger(maparam.get("merid"));
			if(!dealid.equals(0)) parameter.setUid(dealid);
			parameter.setUsername(CommUtil.toString(maparam.get("nick")));
			parameter.setRealname(CommUtil.toString(maparam.get("name")));
			parameter.setPhone(CommUtil.toString(maparam.get("phone")));
			Integer income = CommUtil.toInteger(maparam.get("income"));
			Integer dealertype = CommUtil.toInteger(maparam.get("type"));
			//根据收益排序：income为1从大到小，2是从小到大
			if(income.equals(1)){
				parameter.setParamete(" ORDER BY u.earnings DESC");
			}else if(income.equals(2)){
				parameter.setParamete(" ORDER BY u.earnings ASC");
			}else{
				parameter.setParamete(" ORDER BY u.create_time DESC");
			}
			//parameter.setSort(request.getParameter("feerate"));//费率
			//1是查询有效商户，2查询无效商户,3是查询微信特约商户
			if(dealertype.equals(1)){
				List<Map<String, Object>> dealerdata = userDao.inquireValidDealer(parameter);
				page.setTotalRows(dealerdata.size());
				page.setTotalPages();
				page.setStart();
				page.setEnd();
				parameter.setPages(page.getNumPerPage());
				parameter.setStartnumber(page.getStartIndex());
				List<Map<String, Object>> dealerinfo = userDao.inquireValidDealer(parameter);
				dealerinfo = dealerRelevanceInfo(dealerinfo);
				//查询代理商名下有效商家
				if(userrank.equals(3)){
					List<Map<String, Object>> listMer = userDao.selectValidMerByAgentId(parameter);
					dealerinfo.addAll(listMer);
				}
				datamap.put("listdata", dealerinfo);
			}else if(dealertype.equals(2)){
				List<Map<String, Object>> dealerdata = userDao.inquireDisvalidDealer(parameter);
				page.setTotalRows(dealerdata.size());
				page.setTotalPages();
				page.setStart();
				page.setEnd();
				parameter.setPages(page.getNumPerPage());
				parameter.setStartnumber(page.getStartIndex());
				List<Map<String, Object>> dealerinfo = userDao.inquireDisvalidDealer(parameter);
				dealerinfo = dealerRelevanceInfo(dealerinfo);
				datamap.put("listdata", dealerinfo);
				// 查询微信特约商户
			}else if(dealertype.equals(3)){
				parameter.setSubMer(1);
				List<Map<String, Object>> dealerdata = userDao.inquireValidDealer(parameter);
				page.setTotalRows(dealerdata.size());
				page.setTotalPages();
				page.setStart();
				page.setEnd();
				parameter.setPages(page.getNumPerPage());
				parameter.setStartnumber(page.getStartIndex());
				List<Map<String, Object>> dealerinfo = userDao.inquireValidDealer(parameter);
				dealerinfo = dealerRelevanceInfo(dealerinfo);
				datamap.put("listdata", dealerinfo);
			}else if(userrank != null && userrank.equals(3)){
				//第一次查询将代理商及名下的商家
				List<Map<String, Object>> dealerdata = userDao.selectMerByAgentId(parameter);
				page.setTotalRows(dealerdata.size());
				page.setTotalPages();
				page.setStart();
				page.setEnd();
				parameter.setPages(page.getNumPerPage());
				parameter.setStartnumber(page.getStartIndex());
				//代理商
				List<Map<String, Object>> dealerinfo = userDao.selectDealerUserInfo(parameter);
				//代理商及名下的商家
				List<Map<String, Object>> listmer = userDao.selectMerByAgentId(parameter);
				dealerdata = dealerRelevanceInfo(dealerinfo);
				listmer = dealerRelevanceInfo(listmer);
				dealerinfo.addAll(listmer);
				datamap.put("listdata", dealerdata);
				// 查询开通自动提现的商家(1:开启,2:关闭)
				// 老板想查看开启自动提现的商家
			}else if(dealertype.equals(4)){
				parameter.setAutoWithdraw(1);
				List<Map<String, Object>> dealerdata = userDao.inquireValidDealer(parameter);
				page.setTotalRows(dealerdata.size());
				page.setTotalPages();
				page.setStart();
				page.setEnd();
				parameter.setPages(page.getNumPerPage());
				parameter.setStartnumber(page.getStartIndex());
				List<Map<String, Object>> dealerinfo = userDao.inquireValidDealer(parameter);
				dealerinfo = dealerRelevanceInfo(dealerinfo);
				datamap.put("listdata", dealerinfo);
			}else{
				List<Map<String, Object>> dealerdata = userDao.selectDealerUserInfo(parameter);
				page.setTotalRows(dealerdata.size());
				page.setTotalPages();
				page.setStart();
				page.setEnd();
				parameter.setPages(page.getNumPerPage());
				parameter.setStartnumber(page.getStartIndex());
				List<Map<String, Object>> dealerinfo = userDao.selectDealerUserInfo(parameter);
				dealerinfo = dealerRelevanceInfo(dealerinfo);
				datamap.put("listdata", dealerinfo);
			}
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	/**
	 * @Description： 根据list信息，获取商户关联信息
	 * @author： origin   2019年11月6日 下午3:46:37
	 */
	public List<Map<String, Object>> dealerRelevanceInfo(List<Map<String, Object>> dealerdata){
		try {
			for (Map<String, Object> item : dealerdata) {
				Integer id = CommUtil.toInteger(item.get("id"));
				Double earnings = CommUtil.toDouble(item.get("earnings"));//提现金额
				List<UserBankcard> userbank = userBankcardDao.selectUserBankcardByUserid(id, 2);
				UserBankcard userbankdata = new UserBankcard();
				if(userbank != null && !userbank.isEmpty()){
					userbankdata = userbank.get(0);
				}
				item.put("rate", CommUtil.toInteger(userbankdata.getRate()));
				item.put("bankid", CommUtil.toInteger(userbankdata.getId()));
				Map<String, Object> withdra = CommUtil.isMapEmpty(withdrawDao.selectwitmoney(id));
//			Map<String, Object> advance = CommUtil.isMapEmpty(userDao.arrearageMoneySum((id);//（欠账金额）
//			item.put("advance", CommUtil.toDouble(advance.get("advance")));
				Double depomoney = CommUtil.toDouble(withdra.get("depomoney"));//提现金额
				item.put("depomoney", depomoney);
				item.put("earntotal", CommUtil.addBig(depomoney, earnings));
				
				Map<String, Object> dealerdeviceinfo = CommUtil.isMapEmpty(userEquipmentDao.selectustoequ(id));
				
				Integer online = CommUtil.toInteger(dealerdeviceinfo.get("onlines"));
				Integer disline = CommUtil.toInteger(dealerdeviceinfo.get("disline"));
				item.put("onlines", online);
				item.put("totalline", online + disline);
			}
			return dealerdata;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * separate
	 * @Description： 获取单个商户信息
	 * @author： origin  
	 */
	@Override
	public Object getDealerPersonInfo(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			User user = CommonConfig.getAdminReq(request);
			Parameters parameters = new Parameters();
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			
			
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	/**
	 * separate
	 * @Description： 获取单个商户信息
	 * @author： origin  
	 */
	@Override
	public Object redactAccountInfo(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			Integer uid =  CommUtil.toInteger(maparam.get("merid"));
			String phone =  CommUtil.toString(maparam.get("phone"));
			String servephone =  CommUtil.toString(maparam.get("servephone"));
			servephone = servephone == null ? "" : servephone;
			User account = new User();
			account.setId(uid);
//			account.setPhoneNum(phone);
			account.setServephone(servephone);
			userDao.updateUserById(account);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	/**
	 * separate
	 * @Description： 获取用户信息
	 * @author： origin 
	 */
	@Override
	public Object getAccountListInfo(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);

			Parameters parameters = new Parameters();
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			parameters.setNickname(CommUtil.toString(maparam.get("nick")));
			parameters.setUsername(CommUtil.toString(maparam.get("realname")));
			parameters.setPhone(CommUtil.toString(maparam.get("phone")));

			parameters.setMobile(CommUtil.toString(maparam.get("mobile")));
			int userTotal = userDao.selectGeneralUserTotal(parameters);

			page.setTotalRows(userTotal);
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> touristinfo = userDao.selectGeneralUserInfo(parameters);
			for (Map<String, Object> item : touristinfo) {
				String numerical = StringUtil.StringNumer(CommUtil.toString(item.get("id")));//生成帐号
				item.put("numerical", numerical);
			}
			datamap.put("touristinfo", touristinfo);
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
	 * @Description： 获取单个用户信息
	 * @author： origin  
	 */
	@Override
	public Object getAccountPersonInfo(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
//			User user = CommonConfig.getAdminReq(request);
			Parameters parameters = new Parameters();
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			
			
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * separate
	 * @Description： 商户收益明细查询
	 * @author： origin  
	 */
	@Override
	public Object dealerEarningsDetail(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
//			User user = CommonConfig.getAdminReq(request);
			Parameters parameters = new Parameters();
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);

			parameters.setDealer(CommUtil.toString(maparam.get("dealer")));
			parameters.setOrder(CommUtil.toString(maparam.get("order")));
			parameters.setSource(CommUtil.toString(maparam.get("source")));
			parameters.setType(CommUtil.toString(maparam.get("type")));
			parameters.setStatus(CommUtil.toString(maparam.get("status")));
			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
			List<Map<String, Object>> dealerDetail = merchantDetailDao.selecearningsdetail(parameters);
			page.setTotalRows(dealerDetail.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> dealerDetailinfo = merchantDetailDao.selecearningsdetail(parameters);
			datamap.put("listdata", dealerDetailinfo);
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * separate
	 * @Description： 查看商户银行卡信息
	 * @author： origin  
	 */
	@Override
	public Object dealerBankCardData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
//			User user = CommonConfig.getAdminReq(request);
			Parameters parameters = new Parameters();
			parameters.setLevel(CommUtil.toString(maparam.get("rank")));
			parameters.setUid((Integer)maparam.get("id"));
			List<Map<String, Object>> userbank = userBankcardDao.selectUserBankinfo(parameters);
			datamap.put("listdata", CommUtil.isListMapEmpty(userbank));
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * separate
	 * @Description： 修改指定商户个人费率
	 * @author： origin  
	 */
	@Override
	public Object editDealerPersonRate(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			User dealeruser = CommonConfig.getAdminReq(request);
			Integer id = CommUtil.toInteger(maparam.get("id"));
			User user = new User();
			user.setId(id);
			user.setFeerate(CommUtil.toInteger(maparam.get("rate")));
			userDao.updateUserById(user);
			operateRecordDao.insertoperate("修改商户费率", dealeruser.getId(), id, 2, 0, null, null);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * separate
	 * @Description： 修改指定商户对公费率
	 * @author： origin  
	 */
	@Override
	public Object editDealerCorporateRate(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			User dealeruser = CommonConfig.getAdminReq(request);
			Integer bankid = CommUtil.toInteger(maparam.get("bankid"));
			UserBankcard userbank = userBankcardDao.selectUserBankcardByid(bankid);
			if(userbank==null){
				CommUtil.responseBuildInfo(102, "查询的信息不存在", datamap);
				return datamap;
			}
			Integer rate = CommUtil.toInteger(maparam.get("rate"));
			UserBankcard userBankcard = new UserBankcard();
			userBankcard.setId(bankid);
			userBankcard.setRate(rate);
			userBankcardDao.updateUserBankcard(userBankcard);
			operateRecordDao.insertoperate( "修改银行卡费率", dealeruser.getId(), userbank.getUserId(), 2, 0, null, null);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * separate
	 * @Description： 客户强制被绑定商户
	 * @author： origin  
	 */
	@Override
	public Object touristBindingDealer(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			User touruser = CommonConfig.getAdminReq(request);
			int id =  CommUtil.toInteger(maparam.get("id"));
			int merid =  CommUtil.toInteger(maparam.get("merid"));
			User user = new User();
			user.setId(id);
			user.setMerid(merid);
			userDao.updateUserById(user);
			operateRecordDao.insertoperate( "绑定商户", touruser.getId(), id, 1, 0, null, null);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * separate
	 * @Description： 客户强制解绑商户
	 * @author： origin  
	 */
	@Override
	public Object tourisTunbindDealer(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			User touruser = CommonConfig.getAdminReq(request);
			int id =  CommUtil.toInteger(maparam.get("id"));
			User user = new User();
			user.setId(id);
			user.setMerid(0);
			user.setAid(0);
			userDao.updateUserById(user);
			operateRecordDao.insertoperate("解绑商户", touruser.getId(), id, 1, 0, null, null);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * separate
	 * @Description： 客户被强制绑定小区
	 * @author： origin  
	 */
	@Override
	public Object bindingArea(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			User touruser = CommonConfig.getAdminReq(request);
			int id =  CommUtil.toInteger(maparam.get("id"));
			int areaid =  CommUtil.toInteger(maparam.get("areaid"));
			Area area =  areaDao.selectByIdArea(areaid);
			int merid =  CommUtil.toInteger(area.getMerid());
			User user = new User();
			user.setId(id);
			user.setMerid(merid);
			user.setAid(areaid);
			userDao.updateUserById(user);
			operateRecordDao.insertoperate( "绑定小区", touruser.getId(), id, 1, 0, null, null);
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	
	/**
	 * separate
	 * @Description： 客户被强制解除小区绑定
	 * @author： origin  
	 */
	@Override
	public Object unbindArea(HttpServletRequest request) {
		 Map<String, Object> datamap = new HashMap<String, Object>();
			try {
				Map<String, Object> maparam = CommUtil.getRequestParam(request);
				if(maparam.isEmpty()){
					CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
					return datamap;
				}
				User touruser = CommonConfig.getAdminReq(request);
				int id =  CommUtil.toInteger(maparam.get("id"));
				User user = new User();
				user.setId(id);
//				user.setMerid(0);
//				user.setBalance(0.00);
				user.setAid(0);
				userDao.updateUserById(user);
				operateRecordDao.insertoperate( "解绑小区", touruser.getId(), id, 1, 0, null, null);
				CommUtil.responseBuildInfo(200, "成功", datamap);
			} catch (Exception e) {
				e.printStackTrace();
				CommUtil.responseBuildInfo(301, "异常错误", datamap);
			}
			return datamap;
	}

	/**
	 * separate
	 * @Description： 强制绑定小区或商户时查询对应信息
	 * @author： origin  
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object bindingInquireData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
//			User touruser = CommonConfig.getAdminReq(request);
			int source =  CommUtil.toInteger(maparam.get("source"));
			int merid =  CommUtil.toInteger(maparam.get("merid"));
			List list = new ArrayList<>();
			if(source==1){
				String nickname =  CommUtil.toString(maparam.get("nick"));
				String realName =  CommUtil.toString(maparam.get("realName"));
				String phone =  CommUtil.toString(maparam.get("phone"));
				Parameters parem = new Parameters();
				parem.setUsername(nickname);
				parem.setRealname(realName);
				parem.setPhone(phone);
				List<Map<String, Object>> useradmin = userDao.selectDealerUserInfo(parem);
				datamap.put("listdata", useradmin);
			}else if(source==2){
				Area area = new Area();
				if(merid!=0) area.setMerid(merid);
				List<Area> arealist =  areaDao.selectByArea(area);
				list.addAll(arealist);
				datamap.put("listdata", list);
			}
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * separate
	 * @Description： 查询用户钱包详细
	 * @author： origin   2019年11月9日 上午11:47:04
	 */
	@Override
	public Object touristWalletData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
//			User user = CommonConfig.getAdminReq(request);
			Parameters parameters = new Parameters();
			System.err.println(maparam.get("uid")+"----------------");
			parameters.setUid((Integer)maparam.get("uid"));
			Integer paysource =  CommUtil.toInteger(maparam.get("paysource"));
			parameters.setOrder(CommUtil.toString(maparam.get("order")));
			parameters.setMobile(CommUtil.toString(maparam.get("phone")));
			parameters.setDealer(CommUtil.toString(maparam.get("dealer")));
			parameters.setRealname(CommUtil.toString(maparam.get("realname")));
			if(paysource.equals(0)){
				
			}else if(paysource.equals(2)){
				parameters.setSource("2,3,4");
			}else{
				parameters.setSource(paysource.toString());
			}
			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
			List<Map<String, Object>> walletdetail = generalDetailDao.selecuserwalletdetail(parameters);
			page.setTotalRows(walletdetail.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameters.setPages(page.getNumPerPage());
			parameters.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> walletdetailinfo = generalDetailDao.selecuserwalletdetail(parameters);
			for(Map<String, Object> item : walletdetailinfo){
				Double paymoney = CommUtil.toDouble(item.get("money"));
				Double sendmoney = CommUtil.toDouble(item.get("sendmoney"));
				Double tomoney = CommUtil.toDouble(item.get("tomoney"));
				Double balance = CommUtil.toDouble(item.get("balance"));
				Double topupbalance = CommUtil.toDouble(item.get("topupbalance"));
				Double givebalance = CommUtil.toDouble(item.get("givebalance"));
				if(topupbalance.equals(0.00) && givebalance.equals(0.00)){
//					sendmoney = CommUtil.subBig(tomoney, paymoney);
					topupbalance = balance;
//					item.put("sendmoney", sendmoney);
					item.put("topupbalance", topupbalance);
				}
			}
			datamap.put("listdata", walletdetailinfo);
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
//	@Override
//	public Object touristWalletData(HttpServletRequest request) {
//		Map<String, Object> datamap = new HashMap<String, Object>();
//		try {
//			Map<String, Object> maparam = CommUtil.getRequestParam(request);
//			if(maparam.isEmpty()){
//				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
//				return datamap;
//			}
//			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
//			int currentPage =  CommUtil.toInteger(maparam.get("currentPage"));
//			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
////			User user = CommonConfig.getAdminReq(request);
//			Parameters parameters = new Parameters();
//			parameters.setUid(CommUtil.toString(maparam.get("uid")));
//			Integer paysource =  CommUtil.toInteger(maparam.get("paysource"));
//			parameters.setOrder(CommUtil.toString(maparam.get("order")));
//			if(paysource.equals(0)){
//				
//			}else if(paysource.equals(2)){
//				parameters.setSource("2,3,4");
//			}else{
//				parameters.setSource(paysource.toString());
//			}
//			parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
//			parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));
//			List<Map<String, Object>> walletdetail = generalDetailDao.selecwalletdetail(parameters);
//			page.setTotalRows(walletdetail.size());
//			page.setTotalPages();
//			page.setStart();
//			page.setEnd();
//			parameters.setPages(page.getNumPerPage());
//			parameters.setStartnumber(page.getStartIndex());
//			List<Map<String, Object>> walletdetailinfo = generalDetailDao.selecwalletdetail(parameters);
//			datamap.put("listdata", walletdetailinfo);
//			datamap.put("totalRows", page.getTotalRows());
//			datamap.put("totalPages", page.getTotalPages());
//			datamap.put("currentPage", page.getCurrentPage());
//			CommUtil.responseBuildInfo(200, "成功", datamap);
//		} catch (Exception e) {
//			e.printStackTrace();
//			CommUtil.responseBuildInfo(301, "异常错误", datamap);
//		}
//		return datamap;
//	}

	/**
	 * @Description：查询客户数量
	 * @author： origin
	 * @createTime：2019年12月31日下午2:50:53
	 */
	@Override
	public Integer inquireClientsNum(Integer dealid) {
		try {
			Parameters parame = new Parameters();
			if(!dealid.equals(0)) parame.setDealer(CommUtil.toString(dealid));
			Map<String, Object> clientsdata = CommUtil.isMapEmpty(userDao.inquireClientsNum(parame));
			Integer clientsnum = CommUtil.toInteger(clientsdata.get("clientsnum"));
			return clientsnum;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}

	//===============================================================================================================
	/**
	 * 管理员授权
	 */
	@Override
	public Object setAgent(Integer userId,Integer rank){
		if(userId != null && rank != null && rank != 0 ){
			//授权为2(普通商户)时判断
			if(rank == 2){
				Parameters parameters = new Parameters();
				parameters.setUid(userId);
				//查询代理商下有没有商户
				List<Map<String, Object>> users = userDao.selectMerByAgentId(parameters);
				if(users != null && !users.isEmpty()){
					return CommUtil.responseBuild(301, "授权失败", "代理商名下存在商家");
				}else{
					User user = new User();
					user.setId(userId);
					user.setLevel(rank);
					userDao.updateUserById(user);
					return CommUtil.responseBuild(200, "授权成功", "");
				}
			//授权为3(代理商)时判断
			}else if(rank == 3){
				//查询用户是不是已经被代理商绑定
				User user = userDao.selectUserById(userId);
				if(user.getAgentId() == 0){
					User user1 = new User();
					user1.setId(userId);
					user1.setLevel(rank);
					userDao.updateUserById(user1);
					return CommUtil.responseBuild(200, "授权成功", "");
				}else{
					return CommUtil.responseBuild(301, "授权失败", "商家被代理商绑定");
				}
			}else{
				return CommUtil.responseBuild(301, "授权失败", "参数错误");
			}
		}else{
			return CommUtil.responseBuild(301, "授权失败", "参数错误");
		}
	};
	/**
	 * 超级管理员查看代理商
	 */
	@Override
	public Object selectAgents(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Map<String, Object> datamap = new HashMap<String, Object>();
		Map<String, Object> maparam = CommUtil.getRequestParam(request);
		int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
		int currentPage = CommUtil.toInteger(maparam.get("currentPage"));
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameter = new Parameters();
		parameter.setUid((Integer)maparam.get("uid"));
		parameter.setUsername(CommUtil.toString(maparam.get("nick")));
		parameter.setRealname(CommUtil.toString(maparam.get("name")));
		parameter.setPhone(CommUtil.toString(maparam.get("phone")));
		//根据条件查询总条数
		Integer totalRows = userDao.selectAgentsCount(parameter);
		page.setTotalRows(totalRows);
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameter.setPages(page.getNumPerPage());
		parameter.setStartnumber(page.getStartIndex());
		//根据条件分页
		List<Map<String,Object>> listData = userDao.selectAgents(parameter);
		datamap.put("agentData", listData);
		datamap.put("totalRows", page.getTotalRows());
		datamap.put("totalPages", page.getTotalPages());
		datamap.put("currentPage", page.getCurrentPage());
		return datamap;
	}
	
	/**
	 * 超级管理员查看代理商下的商家
	 * 
	 */
	@Override
	public Object selectMerByAgentId(Parameters parameters) {
		// TODO Auto-generated method stub
		Map<String, Object> datamap = new HashMap<String, Object>();
		if(parameters != null && parameters.getUid() != null && !"".equals(parameters.getUid())){
			List<Map<String, Object>> dealerinfo = userDao.selectMerByAgentId(parameters);
			if(dealerinfo != null && !dealerinfo.isEmpty()){
				dealerinfo = dealerRelevanceInfo(dealerinfo);
			}else{
				dealerinfo = new ArrayList<>();
			}
			return datamap = CommUtil.responseBuild(200, "成功", dealerinfo);
		}else{
			return datamap = CommUtil.responseBuild(301, "异常错误", datamap);
		}
	}
	
	
	/**
	 * 绑定商家到代理商
	 * @param id 代理商id
	 * @param merId 商家id
	 */
	@Override
	public Object bindAgent(Integer id, Integer merId){
		if(id != null && merId != null){
			//绑定前判断是否有人绑定
			User user = userDao.selectUserById(merId);
			if(user != null && user.getAgentId() == 0 && user.getLevel() == 2){
				userDao.bindAgent(id, merId);
				return CommUtil.responseBuild(200, "成功", "");
			}else{
				return CommUtil.responseBuild(301, "失败", "商家信息错误");
			}
		}else{
			return CommUtil.responseBuild(301, "失败", "参数错误");
		}
	};
	
	/**
	 * 将绑定的商家解除
	 * @param merId 商家的id
	 */
	@Override
	public Object removeBindAgent(Integer merId){
		try {
			userDao.removeBindAgent(merId);
			return CommUtil.responseBuild(200, "成功", "");
		} catch (Exception e) {
			return CommUtil.responseBuild(301, "失败", "参数错误");
		}
	}

	/**
	 * separate
	 * @Description： 获取商户信息 (根据type不同，查询条件不同)
	 * @author： origin 
	 */
	@Override
	public Object getDealerInfo(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			Parameters parameter = new Parameters();
			Integer type = CommUtil.toInteger(maparam.get("type"));
			String condition = CommUtil.toString(maparam.get("search"));
			if(condition==null || type.equals(0)){
				return CommUtil.responseBuildInfo(102, "查询类型或数据不能为空", datamap);
			}
			if(type.equals(1)){//电话
				parameter.setPhone(condition);
			} else if(type.equals(2)){//昵称
				parameter.setUsername(condition);
			} else if(type.equals(3)){//姓名
				parameter.setRealname(condition);
			} 
			List<Map<String, Object>> dealerdata = CommUtil.isListMapEmpty(userDao.selectDealerUserInfo(parameter));
			if(dealerdata.size()>0){
				datamap.put("dealdata", dealerdata.get(0));
			}else{
				datamap.put("dealdata", new HashMap<String, Object>());
			}
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}
	/**
	 * 配置微信子商户的商户id(添加,修改,删除)
	 */
	@Override
	public Object configMchid(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			User user = new User();
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			Integer type =CommUtil.toInteger(maparam.get("type")) ;   //type:1是添加,2:是删除,3:是修改
		    String mchId = CommUtil.toString(maparam.get("submchid")) ;//商户的微信商户号
		    Integer merId =  CommUtil.toInteger(maparam.get("merid")); //商户的id
		    String subAppId = CommUtil.toString(maparam.get("subappid"));//商户appid
		    String appSecret = CommUtil.toString(maparam.get("appsecret"));
		    String keyWord = CommUtil.toString(maparam.get("keyword"));
		    String teYue = CommUtil.toString(maparam.get("teyue"));
		    String smallAppId = CommUtil.toString(maparam.get("SmallAppId"));
		    String SmallAppSecret = CommUtil.toString(maparam.get("smallappsecret")); 
			// 判断参数
			if(mchId == null || "".equals(mchId)){
				return CommUtil.responseBuildInfo(102, "输入的微信商户号错误", datamap);
			}else if(type == null){
				return CommUtil.responseBuildInfo(102, "商户操作类型错误", datamap);
			}else if(merId == null){
				return CommUtil.responseBuildInfo(102, "商户的id不存在", datamap);
			}else if(subAppId == null || "".equals(subAppId)){
				return CommUtil.responseBuildInfo(102, "subappid参数错误", datamap);
			}else if(type.equals(1)){
				// 根据Type为1:添加特约商户,2:删除特约商户,3:修改特约商户
				// 添加前的查询子商户号是否存在
				user.setSubMchId(mchId);
				//List<Map<String, Object> > subMer = userDao.selectSubMerByMchid(user);
				//if(subMer.isEmpty()){
				user.setId(merId);
				user.setSubAppId(subAppId);
				user.setType(1);
				user.setAppSecret(appSecret);
				user.setKeyWord(keyWord);
				user.setTeYue(teYue);
				user.setSmallAppId(smallAppId);
				user.setSmallAppSecret(SmallAppSecret);
				user.setCreateTime(new Date());
				// 添加微信特约商户的配置信息
				userDao.addSubMer(user);
				// 将商户标记为微信特约商户
				user.setSubMer(1);
				userDao.updateUserById(user);
				CommUtil.responseBuildInfo(200, "商户参数添加成功", datamap);
				//}else{
				//CommUtil.responseBuildInfo(200, "子商户号已经存在", datamap);
				//}
				return datamap;
			}else if(type.equals(2)){
				// 根据微信商户号,删除商户配置信息,将商户标记为非特约商户
				user.setSubMchId(mchId);
				userDao.delSubMerByMchid(user);
				user.setId(merId);
				user.setSubMer(0);
				userDao.updateUserById(user);
				CommUtil.responseBuildInfo(200, "商户参数删除成功", datamap);
				return datamap;
			}else if(type.equals(3)){
				// 根据商户号修改微信商户的配置信息
				user.setSubMchId(mchId);
				user.setSubAppId(subAppId);
				user.setType(1);
				user.setAppSecret(appSecret);
				user.setKeyWord(keyWord);
				user.setTeYue(teYue);
				user.setSmallAppId(smallAppId);
				user.setSmallAppSecret(SmallAppSecret);
				userDao.updateSubMerById(user);
				CommUtil.responseBuildInfo(200, "商户参数修改成功", datamap);
				return datamap;
			}else{
				return CommUtil.responseBuildInfo(102, "未知错误", datamap);
			}
		} catch (Exception e) {
			return CommUtil.responseBuildInfo(102, "未知错误", datamap);
		}
	}
	/**
	 * @Description：查询用户相关信息（如所属商户、小区等）
	 * @param tourid 用户id
	 * @author： origin
	 * @createTime：2020年4月9日上午10:48:51
	 */
	@Override
	public Map<String, Object> selectGeneralUserInfo(Integer tourid) {
		try {
			Parameters parame = new Parameters();
			parame.setOrder(tourid.toString());
			List<Map<String, Object>> touristinfo = userDao.selectGeneralUserInfo(parame);
			if(touristinfo==null) return new HashMap<String, Object>();
			Map<String, Object> tourist = touristinfo.get(0);
			return tourist;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	
	/**
	 * 根据设备号查询商户的微信配置信息
	 */
	@Override
	public Map<String, Object> selectConfigDataByCode(String code) {
		Map<String, Object> data = userDao.selectConfigDataByCode(code);
		if(data != null){
			return data;
		}else{
			return new HashMap<String, Object>();
		}
	}
	/**
	 * 根据商家的id查询特约商户的配置信息
	 */
	@Override
	public Map<String, Object> selectSubMerConfigById(Integer merId) {
		Map<String, Object> data = userDao.selectSubMerConfigById(merId);
		if(data != null){
			return data;
		}else{
			return new HashMap<String, Object>();
		}
	}

	@Override
	public void addSubUser(User user) {
		userDao.addSubUser(user);
	}

	@Override
	public User selectSubUserByOpenid(String subOpenid) {
		return userDao.selectSubUserByOpenid(subOpenid);
	}

	@Override
	public List<User> selectOnAutoWithdrawMer() {
		List<User> users = userDao.selectOnAutoWithdrawMer();
		if(users.size() > 0 && !users.isEmpty()){
			return users;
		}
		return new ArrayList<User>();
	}

	@Override
	public List<Privilege> selectUserPrivilege(Integer uid) {
		return privilegeDao.selectUserPrivilege(uid);
	}

	@Override
	public List<User> selectUserByRankMid(Integer rank, Integer merid) {
		return userDao.selectUserByRankMid(rank, merid);
	}

	@Override
	public int insertUserPrivliege(Integer uid, Integer pid) {
		return privilegeDao.insertUserPrivliege(uid, pid);
	}

	@Override
	public int deleteUserPrivliege(Integer uid, Integer pid) {
		return privilegeDao.deleteUserPrivliege(uid, pid);
	}

	@Override
	public List<Privilege> selectAllPrivilege(Integer id, Integer parentId) {
		return privilegeDao.selectAllPrivilege(id, parentId);
	}

	/**
	 * @Description：获取商户子账户信息
	 * @param request  response
	 * @return
	 * @author： origin  2020年7月8日上午9:01:26
	 * @comment: bypass account 
	 */
	@Override
	public Object getAccountData(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			int numPerPage =  CommUtil.toInteger(maparam.get("numPerPage"));
			int currentPage = CommUtil.toInteger(maparam.get("currentPage"));
			PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
			Parameters parameter = new Parameters();
			User user = (User) request.getSession().getAttribute("admin");//登录账户信息获取
			Integer merrank = user.getLevel();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
			if(merrank!=0) parameter.setUid(user.getId());//获取登录账户id
			Integer dealid = CommUtil.toInteger(maparam.get("merid"));
			if(!dealid.equals(0)) parameter.setUid(dealid);
			parameter.setUsername(CommUtil.toString(maparam.get("nick")));
			parameter.setRealname(CommUtil.toString(maparam.get("name")));
			parameter.setPhone(CommUtil.toString(maparam.get("phone")));
			parameter.setDealer(CommUtil.toString(maparam.get("dealer")));
			parameter.setMobile(CommUtil.toString(maparam.get("mobile")));
			parameter.setLevel("6");
			List<Map<String, Object>> listdata = userDao.inquireBypassAccount(parameter);
			page.setTotalRows(listdata.size());
			page.setTotalPages();
			page.setStart();
			page.setEnd();
			parameter.setPages(page.getNumPerPage());
			parameter.setStartnumber(page.getStartIndex());
			List<Map<String, Object>> listinfo = userDao.inquireBypassAccount(parameter);
			datamap.put("listdata", listinfo);
			datamap.put("totalRows", page.getTotalRows());
			datamap.put("totalPages", page.getTotalPages());
			datamap.put("currentPage", page.getCurrentPage());
			CommUtil.responseBuildInfo(200, "成功", datamap);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	/**
	 * @Description：解绑子账号绑定信息
	 * @param request
	 * @author： origin  2020年7月8日下午4:48:15
	 * @comment:
	 */
	@Override
	public Object addOrDelChileUser(HttpServletRequest request) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {

			Map<String, Object> maparam = CommUtil.getRequestParam(request);
			if(maparam.isEmpty()){
				CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
				return datamap;
			}
			User operuser = (User) request.getSession().getAttribute("admin");
			String type =  CommUtil.toString(maparam.get("type"));
			if ("1".equals(type)) {//绑定子账户
				String phone =  CommUtil.toString(maparam.get("phone"));
				String mobile =  CommUtil.toString(maparam.get("mobile"));
				if (phone.length()!=11 || mobile.length()!=11) {
					return CommUtil.responseBuildInfo(103, "子账户手机号或商户手机号输入不正确", datamap);
				}
				User touruser = existAdmin(phone);
				User meruser = existAdmin(mobile);
				if (touruser == null) {
					return CommUtil.responseBuildInfo(104, "子账户用户信息不存在", datamap);
				}else if (meruser == null) {
					return CommUtil.responseBuildInfo(105, "商户信息账号不存在", datamap);
				}
				Integer tourank =  CommUtil.toInteger(touruser.getLevel());
				Integer touid =  CommUtil.toInteger(touruser.getId());
				Integer dealid =  CommUtil.toInteger(touruser.getMerid());
				Integer merid =  CommUtil.toInteger(meruser.getId());
				if (!tourank.equals(1)) {
					CommUtil.responseBuildInfo(106, "当前子账号类型不为用户，故不可设置为子账号", datamap);
				}else if (!dealid.equals(0) && !dealid.equals(merid)) {
					CommUtil.responseBuildInfo(107, "子账号所属商户不属于指定商户，不可设置。请联系管理员处理", datamap);
				}else{
					User editUser = new User();
					editUser.setId(touid);
					editUser.setLevel(6);
					if(dealid.equals(0)) editUser.setMerid(merid);
					userDao.updateUserById(editUser);
					privilegeDao.insertUserPrivliege(touid, 3);
					privilegeDao.insertUserPrivliege(touid, 7);
					operateRecordDao.insertoperate( "子账户绑定商户", touid, operuser.getId(), 1, 0, null, null);
					CommUtil.responseBuildInfo(200, "添加成功", datamap);
				}
			} else if ("2".equals(type)) {//删除用户
				Integer id =  CommUtil.toInteger(maparam.get("id"));
				User editUser = new User();
				editUser.setId(id);
				editUser.setLevel(1);
				userDao.updateUserById(editUser);
				privilegeDao.deleteUserPrivliege(id, null);
				operateRecordDao.insertoperate( "子账户删除", operuser.getId(), id, 1, 0, null, null);
				return CommUtil.responseBuildInfo(200, "删除成功", datamap);
			} else {
				return CommUtil.responseBuildInfo(302, "参数类型传递错误，请联系管理进行后续处理", datamap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(301, "异常错误", datamap);
		}
		return datamap;
	}

	@Override
	public boolean checkUserIfRich(TradeRecord tradeRecord) {
		String comment = CommUtil.toString(tradeRecord.getComment());
		Integer merid = tradeRecord.getMerid();
		Double money = tradeRecord.getMoney();
		if(comment == null || "0".equals(comment)){
			Integer manid = tradeRecord.getManid();
			Double manmoney = tradeRecord.getManmoney();
			Double mermoney = tradeRecord.getMermoney();
			if (manid != null && manid != 0) {
				User manUser = userDao.selectUserById(manid);
				User merUser = userDao.selectUserById(merid);
				if ((manUser.getEarnings() >= manmoney) && (merUser.getEarnings() >= mermoney)) {
					return true;
				} else {
					return false;
				}
			} else {
				User merUser = userDao.selectUserById(merid);
				if ((merUser.getEarnings() >= money)) {
					return true;
				} else {
					return false;
				}
			}
		}else{
			JSONArray jsona = JSONArray.fromObject(comment);
			Boolean fsal = true;
			for (int i = 0; i < jsona.size(); i++) {
				JSONObject item = jsona.getJSONObject(i);
				Integer partid = CommUtil.toInteger(item.get("partid"));
				Integer mercid = CommUtil.toInteger(item.get("merid"));
				Double partmoney = CommUtil.toDouble(item.get("money"));
				if(partid != 0){
					User partUser = userDao.selectUserById(partid);
					if (partUser.getEarnings() >= partmoney) {
						return true;
					} else {
						fsal =  false;
						break;
					}
				}else{
					User merUser = userDao.selectUserById(mercid);
					if (merUser.getEarnings() >= partmoney) {
						return true;
					} else {
						fsal = false;
						break;
					}
				}
			}
			return fsal;
		}
	}

	@Transactional
	@Override
	public Map<String, Object> addUserByAuth_code(String auth_code,String username, String imageUrl) {
		try {
			JSONObject userOpenid = WeixinUtil.getUserOpenid(auth_code);
			System.out.println("授权：" + userOpenid.toString());
			if (!userOpenid.has("openid")) {
				return CommUtil.responseBuildInfo(1001, "授权code已使用或未接收到微信返openid", null);
			}
			String openid = userOpenid.getString("openid");
			User selectUser = userDao.getUserByOpenid(openid);
			if (selectUser == null) {
				User user = new User();
				user.setOpenid(openid.trim());
				user.setUsername(username);
				user.setImageUrl(imageUrl);
				user.setCreateTime(new Date());
				userDao.addUser(user);
				selectUser = userDao.getUserByOpenid(openid);
			}
			Map<String,Object> map = new HashMap<>();
			map.put("userinfo", selectUser);
			return CommUtil.responseBuildInfo(1000, "登陆成功", map);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(1002, "系统异常", null);
		}
	}
	
	@Override
	public Map<String, Object> queryUserInfo(Integer userid) {
		try {
			User user = userDao.selectUserById(userid);
			Map<String,Object> map = new HashMap<>();
			map.put("userinfo", user);
			if (user != null) {
				return CommUtil.responseBuildInfo(1000, "查询成功", map);
			} else {
				return CommUtil.responseBuildInfo(1001, "未查询到用户信息", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(1002, "系统异常", null);
		}
	}
	
	@Override
	public Map<String, Object> getUserWallet(Integer userid) {
		try {
			User user = userDao.selectUserById(userid);
			Map<String,Object> map = new HashMap<>();
			if (user != null) {
				map.put("balance", user.getBalance());
				return CommUtil.responseBuildInfo(1000, "查询成功", map);
			} else {
				map.put("balance", 0.0);
				return CommUtil.responseBuildInfo(1001, "未查询到用户信息", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(1002, "系统异常", null);
		}
	}

	@Override
	public Map<String, Object> walletAppointCharge(Integer userid, Double money,HttpServletRequest request) {
		try {
			String ordernum = HttpRequest.createOrdernum(-1);
			User user = userDao.selectUserById(userid);
			Map<String, Object> payParam = WxpayUtil.payParam(user.getOpenid(), "applet/wolfnotify", ordernum, request, money);
			double aftermoney = CommUtil.addBig(user.getBalance(), money);
			moneyService.payMoneys(userid, ordernum, 0, 0, money, 0.0, money, aftermoney, aftermoney, 0.0 , "钱包充值");
			return CommUtil.responseBuildInfo(1000, "后台请求成功", payParam);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(1001, "系统异常", null);
		}
	}

	@Override
	public Map<String, Object> walletCharge(Map<Object, Object> resultMap, String openid, String ordernum) {
		Money money = moneyService.queryMoneyByOrdernum(ordernum);
		if (money.getStatus() == null || money.getStatus() == 0) {//该订单处于预订单状态
			//修改本地数据
			Integer uid = CommUtil.toInteger(money.getUid());
			Double paymoney = CommUtil.toDouble(money.getMoney());//付款总额
			Double opertopupmoney = CommUtil.toDouble(paymoney);//操作总额
			Double opersendmoney = CommUtil.toDouble(money.getSendmoney());//操作充值金额
			Double opermoney = CommUtil.toDouble(money.getTomoney());//操作赠送金额
			
			String mremark = money.getRemark();//来源
			
			Integer orderid = CommUtil.toInteger(money.getId());
			Date time = new Date();
			String strtime = CommUtil.toDateTime(time);
			Integer paysource = MerchantDetail.WALLETSOURCE;
			Integer paytype = MerchantDetail.WEIXINPAY;
			Integer paystatus = MerchantDetail.NORMAL;
			Integer type = 1;
			Double mermoney = 0.00;
			
			User touuser = userDao.selectUserById(uid);
			Integer merid = CommUtil.toInteger(touuser.getMerid());
			//============================================================================
			Double topupmoney = CommUtil.toDouble(touuser.getBalance());//账户充值余额
			Double sendmoney = CommUtil.toDouble(touuser.getSendmoney());//账户赠送余额
			Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额（充值与赠送的合）
			//============================================================================
			Double topupbalance = CommUtil.addBig(opertopupmoney, topupmoney);//充值余额
			Double sendbalance = CommUtil.addBig(opersendmoney, sendmoney);//赠送余额
			Double accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
			//==========================================================================
			//1、充值记录修改
			money.setBalance(accountbalance);
			money.setStatus(1);
			moneyService.updateMoney(money);
			
			//2、添加钱包明细
			generalDetailService.insertGenDetail(uid, merid, opertopupmoney, opersendmoney, 
					opermoney, accountbalance, topupbalance, sendbalance, ordernum, time, 1, mremark);
			//3、修改用户金额
			userDao.updateBalanceByOpenid(topupbalance, sendbalance, touuser.getOpenid(),null);
		}
		return null;
	}
	
}
