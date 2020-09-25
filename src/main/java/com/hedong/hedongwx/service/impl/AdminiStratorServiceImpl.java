package com.hedong.hedongwx.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.GeneralDetailDao;
import com.hedong.hedongwx.dao.MerchantDetailDao;
import com.hedong.hedongwx.dao.OperateRecordDao;
import com.hedong.hedongwx.dao.PackageMonthDao;
import com.hedong.hedongwx.dao.UserBankcardDao;
import com.hedong.hedongwx.dao.UserDao;
import com.hedong.hedongwx.dao.UserEquipmentDao;
import com.hedong.hedongwx.dao.WithdrawDao;
import com.hedong.hedongwx.entity.PackageMonth;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserBankcard;
import com.hedong.hedongwx.service.AdminiStratorService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Service
public class AdminiStratorServiceImpl implements AdminiStratorService{


	@Autowired
	private UserDao userDao;
	@Autowired
	private UserEquipmentDao userEquipmentDao;
	@Autowired
	private WithdrawDao withdrawDao;
	@Autowired
	private MerchantDetailDao merchantDetailDao;
	@Autowired
	private GeneralDetailDao generalDetailDao;
	@Autowired
	private UserBankcardDao userBankcardDao;
	@Autowired
	private OperateRecordDao operateRecordDao;
	@Autowired
	private PackageMonthDao packageMonthDao;
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * 查询管理员与商户信息
	 */
	@Override
	public PageUtils<Parameter> selectUserInfos(HttpServletRequest request) {

		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameter> page  = new PageUtils<>(numPerPage, currentPage);
		Parameter parameter = new Parameter();
		parameter.setUsername(request.getParameter("username"));
		parameter.setRealname(request.getParameter("realname"));
		parameter.setPhoneNum(request.getParameter("phoneNum"));
		parameter.setEqui(request.getParameter("feerate"));//费率
		String order = request.getParameter("order");
		if(order==null) order = "0"; 
		parameter.setParem(order);//收益
		parameter.setType("0,2");//用户类型（查询商户、和管理员）
		
		List<Map<String, Object>> admin = userDao.selectUserInfos(parameter);
		page.setTotalRows(admin.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameter.setNumPerPage(page.getNumPerPage());
		parameter.setStartIndex(page.getStartIndex());
		List<Map<String, Object>> userequ = userDao.selectUserInfos(parameter);
		for (Map<String, Object> item : userequ) {
			Map<String, Object> withdra = withdrawDao.selectwitmoney((Integer) item.get("id"));
			Map<String, Object> advance = userDao.arrearageMoneySum((Integer) item.get("id"));
			if(null==advance)item.put("advance", 0.0);
			else if(advance!=null)item.put("advance", advance.get("advance"));
			double earn = (double) item.get("earnings");
			if(withdra!=null){
				double depomoney = (double) withdra.get("depomoney");
				item.put("depomoney", depomoney);
				//if(earn==null) earn = 0.00;
				item.put("earntotal", depomoney + earn);
			}else{
				item.put("depomoney", 0);
				item.put("earntotal", earn);
			}
			
			Map<String, Object> ustoequ = userEquipmentDao.selectustoequ((Integer) item.get("id"));
			if(ustoequ==null){
				item.put("onlines", 0);
				item.put("totalline", 0);
			}else{
				BigDecimal onlines =  (BigDecimal) ustoequ.get("onlines");
				BigDecimal totalline = onlines.add((BigDecimal)ustoequ.get("disline"));
				item.put("onlines", onlines);
				item.put("totalline", totalline);
			}	
		}
		page.setListMap(userequ);
		return page;
	}
	
	/**
	 * 查询普通用户信息
	 */
	@Override
	public PageUtils<Parameter> selectGeneralInfo(HttpServletRequest request) {

		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameter> page  = new PageUtils<>(numPerPage, currentPage);
		Parameter parameter = new Parameter();
		parameter.setUsername(request.getParameter("username"));
		parameter.setRealname(request.getParameter("realname"));
		parameter.setPhoneNum(request.getParameter("phoneNum"));
		parameter.setPhoneNum(request.getParameter("areaname"));
		
		String murealname = request.getParameter("murealname");
		String rephoneNum = request.getParameter("rephoneNum");
		
		parameter.setDealer(murealname);
		parameter.setOrdernum(rephoneNum);
		Integer orderID = StringUtil.getStringInt(request.getParameter("orderID"));
		if(orderID!=null)parameter.setStatus(orderID.toString());//ID
		//parameter.setType("1");//用户类型（查询商户、和管理员）
		String sort = request.getParameter("sort");
		if(sort==null) sort = "0"; 
		parameter.setCode(sort);
		List<Map<String, Object>> admin = userDao.selectUserInfos(parameter);
		if(null==murealname) murealname = "";
		if(null==rephoneNum) rephoneNum = "";
		BigDecimal walletmoney = null;
		if(murealname!="" || rephoneNum!=""){
			walletmoney = new BigDecimal("0");
			for (Map<String, Object> item : admin) {
				BigDecimal balance = StringUtil.togetBigDecimal(item.get("balance"));
				walletmoney =  walletmoney.add(balance).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("walletmoney", walletmoney);
		page.setMap(map);
		page.setTotalRows(admin.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameter.setNumPerPage(page.getNumPerPage());
		parameter.setStartIndex(page.getStartIndex());
		List<Map<String, Object>> userequ = userDao.selectUserInfos(parameter);
		for (Map<String, Object> item : userequ) {
			String numerical = StringUtil.StringNumer(item.get("id").toString());//生成帐号
			item.put("numerical", numerical);
		}
		page.setListMap(userequ);
		return page;
	}

	
	@Override
	public List<Map<String, Object>> selectAdmini(HttpServletRequest request) {
		Parameters parame = new Parameters();
		String rank = request.getParameter("rank");
		if( null != rank && !rank.equals("-1")) parame.setLevel(rank);
		parame.setUid(Integer.parseInt(request.getParameter("id")));
		List<Map<String, Object>> userbank = userBankcardDao.selectUserBankinfo(parame);
		return userbank;
	}

	@Override
	public int updateUserById(User user) {
		return userDao.updateUserById(user);
	}
	
	/**
	 * 商户收益查询
	 */
	@Override
	public PageUtils<Parameters> selecearningsdetail(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		parameters.setDealer(CommUtil.toString(request.getParameter("merid")));
		parameters.setOrder(CommUtil.toString(request.getParameter("ordernum")));
		parameters.setSource(CommUtil.toString(request.getParameter("paysource")));
		parameters.setType(CommUtil.toString(request.getParameter("paytype")));
		parameters.setStatus(CommUtil.toString(request.getParameter("status")));
		parameters.setStartTime(CommUtil.toString(request.getParameter("startTime")));
		parameters.setEndTime(CommUtil.toString(request.getParameter("endTime")));
		List<Map<String, Object>> merchantdetail = merchantDetailDao.selecearningsdetail(parameters);
		page.setTotalRows(merchantdetail.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		page.setListMap(merchantDetailDao.selecearningsdetail(parameters));
		return page;
	}

	
	/**
	 * 根据id获取用户钱包明细
	 */
	@Override
	public PageUtils<Parameters> selecwalletdetail(HttpServletRequest request) {
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		parameters.setUid(Integer.parseInt(request.getParameter("uid")));
		Integer paysource =  CommUtil.toInteger(request.getParameter("paysource"));
		parameters.setOrder(CommUtil.toString(request.getParameter("ordernum")));
		if(paysource.equals(0)){
			
		}else if(paysource.equals(2)){
			parameters.setSource("2,3,4");
		}else{
			parameters.setSource(paysource.toString());
		}
		parameters.setStartTime(CommUtil.toString(request.getParameter("startTime")));
		parameters.setEndTime(CommUtil.toString(request.getParameter("endTime")));
		List<Map<String, Object>> walletdetail = generalDetailDao.selecwalletdetail(parameters);
		page.setTotalRows(walletdetail.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		List<Map<String, Object>> walletdetailinfo = generalDetailDao.selecwalletdetail(parameters);
		page.setListMap(walletdetailinfo);
		return page;
	}

	@Override
	public List<User> deleteAdminiStrator() {
		
		return null;
	}

	@Override
	public List<User> updateAdminiStrator() {
		
		return null;
	}

	@Override
	public List<User> insetAdminiStrator() {
		
		return null;
	}

	@Override
	public List<User> selectAdminList() {
		
		return null;
	}

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	
	/**
	 * 查询商户信息
	 */
	@Override
	public PageUtils<Parameters> selectDealerUserInfo(HttpServletRequest request) {
		User user = CommonConfig.getAdminReq(request);
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameter = new Parameters();
		Integer rank = CommUtil.toInteger(user.getLevel());// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(!rank.equals(0)) parameter.setUid(user.getId());//获取用户
		parameter.setUsername(request.getParameter("username"));
		parameter.setRealname(request.getParameter("realname"));
		parameter.setPhone(request.getParameter("phoneNum"));
		//parameter.setSort(request.getParameter("feerate"));//费率
		Integer income = CommUtil.toInteger(request.getParameter("order"));
		if(income.equals(1)){
			parameter.setParamete(" ORDER BY u.earnings DESC");
		}else if(income.equals(2)){
			parameter.setParamete(" ORDER BY u.earnings ASC");
		}else{
			parameter.setParamete(" ORDER BY u.create_time ASC");
		}
		Integer dealertype = CommUtil.toInteger(request.getParameter("source"));
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
			page.setListMap(dealerinfo);
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
			page.setListMap(dealerinfo);
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
			page.setListMap(dealerinfo);
		}
		return page;
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

//	@Override
//	public PageUtils<Parameters> selectDealerUserInfo(HttpServletRequest request) {
//		User user = CommonConfig.getAdminReq(request);
//		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
//		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
//		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
//		Parameters parameter = new Parameters();
//		parameter.setUsername(request.getParameter("username"));
//		parameter.setRealname(request.getParameter("realname"));
//		parameter.setPhone(request.getParameter("phoneNum"));
//		//parameter.setSort(request.getParameter("feerate"));//费率
//		Integer income = CommUtil.toInteger(request.getParameter("order"));
//		
//		if(income.equals(1)){
//			parameter.setParamete(" ORDER BY u.earnings DESC");
//		}else if(income.equals(2)){
//			parameter.setParamete(" ORDER BY u.earnings ASC");
//		}else{
//			parameter.setParamete(" ORDER BY u.create_time DESC");
//		}
//		Integer rank = user.getRank();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
//		if(rank!=0) parameter.setUid(user.getId().toString());//获取用户
//		List<Map<String, Object>> admin = userDao.selectDealerUserInfo(parameter);
//		
//		List<Map<String, Object>> valid = new ArrayList<Map<String, Object>>();
//		String source = request.getParameter("source");
//		if(null==source || source.equals("1")){
//			for (Map<String, Object> item : admin) {
//				List<UserBankcard> usban = userBankcardDao.selectUserBankcardByUserid((Integer) item.get("id"), 2);
//				Integer rate = 0;
//				Integer bankid = 0;
//				if(usban.size()>0){
//					rate = usban.get(0).getRate();
//					bankid = usban.get(0).getId();
//				}
//				item.put("rate", rate);
//				item.put("bankid", bankid);
//				Map<String, Object> withdra = withdrawDao.selectwitmoney((Integer) item.get("id"));//（提现金额）
//				double earntotal = 0;
//				if(withdra!=null){
//					earntotal = StringUtil.togetBigDecimal(withdra.get("depomoney")).add(StringUtil.togetBigDecimal(item.get("earnings"))).doubleValue();
//				}else if(null==withdra){
//					earntotal = (double) item.get("earnings");
//				}
//				Map<String, Object> ustoequ = userEquipmentDao.selectustoequ((Integer) item.get("id"));
//				if(earntotal!=0 || null != ustoequ){
////					Map<String, Object> advance = userDao.arrearageMoneySum((Integer) item.get("id"));//（欠账金额）
////					if(null==advance) item.put("advance", 0.0);
////					else if(advance!=null) item.put("advance", advance.get("advance"));
//					item.put("earntotal", earntotal);
//					if(ustoequ==null){
//						item.put("onlines", 0);
//						item.put("totalline", 0);
//					}else{
//						BigDecimal onlines =  (BigDecimal) ustoequ.get("onlines");
//						BigDecimal totalline = onlines.add((BigDecimal)ustoequ.get("disline"));
//						item.put("onlines", onlines);
//						item.put("totalline", totalline);
//					}
//					valid.add(item);
//				}
//			}
//			int tatolrow = valid.size();
//			page.setTotalRows(tatolrow);
//			page.setTotalPages();
//			page.setStart();
//			page.setEnd();
//			parameter.setPages(page.getNumPerPage());
//			parameter.setStartnumber(page.getStartIndex());
//			int fromIndex = page.getStartIndex(); 
//			int toIndex = fromIndex + page.getNumPerPage();
//			if(toIndex>tatolrow) toIndex = tatolrow;
//			page.setListMap(valid.subList(fromIndex, toIndex));
//		}else{
//			page.setTotalRows(admin.size());
//			page.setTotalPages();
//			page.setStart();
//			page.setEnd();
//			parameter.setPages(page.getNumPerPage());
//			parameter.setStartnumber(page.getStartIndex());
//			List<Map<String, Object>> userequ = userDao.selectDealerUserInfo(parameter);
//			for (Map<String, Object> item : userequ) {
//				Map<String, Object> withdra = withdrawDao.selectwitmoney((Integer) item.get("id"));//提现金额
////				Map<String, Object> advance = userDao.arrearageMoneySum((Integer) item.get("id"));//（欠款金额）
////				if(null==advance) item.put("advance", 0.0);
////				else if(advance!=null) item.put("advance", advance.get("advance"));
//				double earn = (double) item.get("earnings");//账户余额
//				if(withdra!=null){
//					double depomoney = (double) withdra.get("depomoney");//提现金额
//					item.put("depomoney", depomoney);
//					item.put("earntotal", depomoney + earn);
//				}else{
//					item.put("depomoney", 0);
//					item.put("earntotal", earn);
//				}
//				Map<String, Object> ustoequ = userEquipmentDao.selectustoequ((Integer) item.get("id"));
//				if(ustoequ==null){
//					item.put("onlines", 0);
//					item.put("totalline", 0);
//				}else{
//					BigDecimal onlines =  (BigDecimal) ustoequ.get("onlines");
//					BigDecimal totalline = onlines.add((BigDecimal)ustoequ.get("disline"));
//					item.put("onlines", onlines);
//					item.put("totalline", totalline);
//				}
//				
//			}
//			page.setListMap(userequ);
//		}
//		return page;
//	}

	/**
	 * 查询普通用户信息
	 */
	@Override
	public PageUtils<Parameters> selectGeneralUserInfo(HttpServletRequest request) {
		User user = CommonConfig.getAdminReq(request);
		int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
		int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
		PageUtils<Parameters> page  = new PageUtils<>(numPerPage, currentPage);
		Parameters parameters = new Parameters();
		
		parameters.setNickname(request.getParameter("username"));
		parameters.setUsername(request.getParameter("realname"));
		parameters.setPhone(request.getParameter("phoneNum"));
		parameters.setSource(request.getParameter("areaname"));
		parameters.setOrder(request.getParameter("orderID"));
		
		String murealname = request.getParameter("murealname");
		String rephoneNum = request.getParameter("rephoneNum");
		parameters.setDealer(murealname);
		parameters.setMobile(rephoneNum);
		
		Integer rankwallet = CommUtil.toInteger(request.getParameter("sort"));
		if(rankwallet.equals(1)){
			parameters.setParamete(" ORDER BY u.balance DESC");
		}else if(rankwallet.equals(2)){
			parameters.setParamete(" ORDER BY u.balance ASC");
		}
		Integer rank = user.getLevel();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
		if(rank!=0) parameters.setUid(user.getId());//获取用户
		List<Map<String, Object>> admin = userDao.selectGeneralUserInfo(parameters);
		if(null==murealname) murealname = "";
		if(null==rephoneNum) rephoneNum = "";
		BigDecimal walletmoney = null;
		if(murealname!="" || rephoneNum!="" || rank!=0){
			walletmoney = new BigDecimal("0.00");
			for (Map<String, Object> item : admin) {
				BigDecimal balance = StringUtil.togetBigDecimal(item.get("balance"));
				walletmoney =  walletmoney.add(balance).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("walletmoney", walletmoney);
		page.setMap(map);
		page.setTotalRows(admin.size());
		page.setTotalPages();
		page.setStart();
		page.setEnd();
		parameters.setPages(page.getNumPerPage());
		parameters.setStartnumber(page.getStartIndex());
		List<Map<String, Object>> userequ = userDao.selectGeneralUserInfo(parameters);
		for (Map<String, Object> item : userequ) {
			String numerical = StringUtil.StringNumer(item.get("id").toString());//生成帐号
			item.put("numerical", numerical);
		}
		page.setListMap(userequ);
		return page;
	}

	/**
	 * 根据商户查询合伙人信息
	 */
	@Override
	public List<User> selectPartnerInfo(HttpServletRequest request) {
		//User admin = CommonConfig.getAdminReq(request);
		String merid = request.getParameter("mreid");
		List<User> partner = userDao.selectPartner(StringUtil.getIntString(merid));
		return partner;
	}

	/** @origin */
	@Override
	public Map<String, Object> updateBankRate(Integer bankid, Integer rate) {
		User admin = (User) request.getSession().getAttribute("admin");
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			UserBankcard ubank = userBankcardDao.selectUserBankcardByid(bankid);
			if(ubank==null){
				map.put("code", 401);
				return map;
			}
			UserBankcard userBankcard = new UserBankcard();
			userBankcard.setId(bankid);
			userBankcard.setRate(rate);
			userBankcardDao.updateUserBankcard(userBankcard);
			UserBankcard usban = userBankcardDao.selectUserBankcardByid(bankid);
			operateRecordDao.insertoperate( "修改银行卡费率", admin.getId(), usban.getUserId(), 2, 0, null, null);
			map.put("code", 200);
			return map;
		} catch (Exception e) {
			map.put("code", 400);
			return map;
		}
	}
	
	
	@Override
	public Object selectPackagemonth(Integer montid, Integer uid) {
		PackageMonth pamont = packageMonthDao.selectPackageMonthByUid(uid);
		//PackageMonthRecord pamont = packageMonthDao.selectMonthRecordById(montid);
		return pamont = pamont == null ? new PackageMonth() : pamont;
	}
	
	
}
