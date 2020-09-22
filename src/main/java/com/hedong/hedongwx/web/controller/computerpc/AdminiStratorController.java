package com.hedong.hedongwx.web.controller.computerpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.ChargeRecord;
import com.hedong.hedongwx.entity.InCoins;
import com.hedong.hedongwx.entity.Money;
import com.hedong.hedongwx.entity.OfflineCard;
import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.OnlineCardRecord;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.Withdraw;
import com.hedong.hedongwx.service.AdminiStratorService;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.ChargeRecordService;
import com.hedong.hedongwx.service.GeneralDetailService;
import com.hedong.hedongwx.service.InCoinsService;
import com.hedong.hedongwx.service.MoneyService;
import com.hedong.hedongwx.service.OfflineCardService;
import com.hedong.hedongwx.service.OnlineCardRecordService;
import com.hedong.hedongwx.service.OnlineCardService;
import com.hedong.hedongwx.service.OperateRecordService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.service.WithdrawService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.MobileSendUtil;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;

@Controller
@RequestMapping(value = "/pcadminiStrator")
public class AdminiStratorController { //管理员（PC端)
	
	@Autowired
	private AdminiStratorService adminiStratorService;
	@Autowired
	private UserService userService;
	@Autowired
	private OnlineCardRecordService onlineCardRecordService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private ChargeRecordService chargeRecordService;
	@Autowired
	private InCoinsService inCoinsService;
	@Autowired
	private OfflineCardService offlineCardService;
	@Autowired
	private OnlineCardService onlineCardService;
	@Autowired
	private WithdrawService withdrawService;
	@Autowired
	private MoneyService moneyService;
	@Autowired
	private GeneralDetailService generalDetailService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private OperateRecordService operateRecordService;
	/*** 用户登录  ************************************************************************************/
	
	/**
	 * @Description： 更改总服务电话
	 * @author： origin   2019年10月10日 下午4:54:57 
	 */
	public Object editServicePhone(Integer id, String mobile) {
		User user =  userService.editServicePhone(id, mobile);
		return user;
	}
	
	/**
	 * @Description： 更改总服务电话
	 * @author： origin   2019年10月10日 下午4:54:57 
	 */
	public Object editAreaServicePhone(Integer id, Integer aid, String nick, String name, String mobile) {
		Map<String, Object> user =  areaService.editAreaServicePhone(id, aid, nick, name, mobile);
		return user;
	}

	/**
	 * @Description： 管理员虚拟充值——跳转到虚拟充值页面
	 * @author： origin 创建时间：   2019年7月16日 上午9:41:35
	 */
	@RequestMapping(value = "/virtualPayMoney")
	public Object mercVirtualPayMoney( Integer type, Integer uid, Model model){
		User user = (User) request.getSession().getAttribute("admin");
		type = (type == null ? 1 : type );
		Object order = null;
		if(type==1){
			order = userService.selectUserById(CommUtil.toInteger(uid));
		}else if(type==2){
			order = onlineCardService.selectOnlineCardById(CommUtil.toInteger(uid));
			User orderus = userService.selectUserById(((OnlineCard) order).getUid());
			model.addAttribute("useronline", orderus);
		}
		model.addAttribute("order", order);
		model.addAttribute("user", user);
		model.addAttribute("type", type);
		model.addAttribute("id", uid);
		return "/computer/virtualCharge";
	}
	
	/**
	 * @Description： 管理员虚拟充值——虚拟充值
	 * @author： origin 创建时间：   2019年7月10日 下午4:43:06
	 */
	@RequestMapping(value = "/virtualPay")
	@ResponseBody
	public Object mercVirtualPay( Double money, Double sendmoney, Integer type, Integer id, Integer status, Model model){
		User user = (User) request.getSession().getAttribute("admin");
		Object result = null;
		if(type==1){
			result = moneyService.mercVirtualMoneyPay(user, money, sendmoney, id, null, status);//id:充值钱包对象的id（用户id）
		}else if(type==2){
			OnlineCard online = onlineCardService.selectOnlineCardById(id);
			if(online.getRelevawalt()==2){
				result = onlineCardService.mercVirtualOnlinePay(user, money, sendmoney, id, status);//id:指定在线卡id
			}else{
				result = moneyService.mercVirtualMoneyPay(user, money, sendmoney, online.getUid(), online.getCardID(), status);//id:充值钱包对象的id（用户id）
			}
		}
		return result;
	}
	
	
	/**
	 * 获取验证码
	 * @param captcha, mobile
	 * @return
	 */
	@RequestMapping({ "/captcha" })
	@ResponseBody
	public String Captcha( String mobile) {
		boolean bool = existMobile(mobile);
		if(!bool) return "1";
		String captchaNum = StringUtil.getRandNum();
		String[] params = { captchaNum, "3" };
		String[] mobilephon = { mobile };
		MobileSendUtil.TemplateMobileSend(params, mobilephon);
		return captchaNum;
	}
	
	/**
	 * 判断用户表中是否存在该用户
	 * @return
	 * @throws Exception 
	 */
	public boolean existMobile(String mobile) {
		User user =  userService.existAdmin(mobile);
		if(user==null) return true;
		return false;
	}
	
	/**
	 * 判断密码是否正确
	 * @param password
	 * @return
	 */
	@RequestMapping({ "/verification" })
	@ResponseBody
	public String pwdjudge( String password){
		User admin = (User) this.request.getSession().getAttribute("admin");
		String pwd = DigestUtils.md5Hex(password);
		if(!admin.getPassword().equals(pwd)){
			return "1";
		}
		return "0";
	}
	
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询商户信息
	@RequestMapping(value = "/selectDealerUserInfo")
	public String selectDealerUserInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageUtils<Parameters> pageBean = adminiStratorService.selectDealerUserInfo(request);
		model.addAttribute("source", request.getParameter("source"));
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("realname", request.getParameter("realname"));
		model.addAttribute("phoneNum", request.getParameter("phoneNum"));
		model.addAttribute("order", request.getParameter("order"));
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("adminiStrator", pageBean.getListMap());
		return "computer/userRecord";
	}
	
	//查询普通用户信息
	@RequestMapping(value = "/selectGeneralUserInfo")
	public String selectGeneralUserInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageUtils<Parameters> pageBean = adminiStratorService.selectGeneralUserInfo(request);
		model.addAttribute("orderID", request.getParameter("orderID"));
		model.addAttribute("sort", request.getParameter("sort"));
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("realname", request.getParameter("realname"));
		model.addAttribute("phoneNum", request.getParameter("phoneNum"));
		model.addAttribute("murealname", request.getParameter("murealname"));
		model.addAttribute("rephoneNum", request.getParameter("rephoneNum"));
		model.addAttribute("areaname", request.getParameter("areaname"));
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		model.addAttribute("walletmoney", pageBean.getMap().get("walletmoney"));
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("adminiStrator", pageBean.getListMap());
		return "computer/userGeneral";
	}
	
	@RequestMapping(value = "/selectPackagemonth")
	@ResponseBody
	public Object selectPackagemonth(Integer montid, Integer uid, Model model) {
		Object result = adminiStratorService.selectPackagemonth(montid, uid);
		return result;
	}
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	
	/*** 查询 商户户信息  ********************************************************************************/
	
	/**
	 * 查询人员(商户与管理员)
	 * @param  request, response, model
	 * @return
	 */
	@RequestMapping(value = "/selectUserInfo")
	public String selectUserInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageUtils<Parameter> pageBean = adminiStratorService.selectUserInfos(request);
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("realname", request.getParameter("realname"));
		model.addAttribute("phoneNum", request.getParameter("phoneNum"));
		model.addAttribute("order", request.getParameter("order"));
		model.addAttribute("feerate", request.getParameter("feerate"));
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("adminiStrator", pageBean.getListMap());
		return "computer/userRecord";
	}
	
	/**
	 * 根据用户id查询商户收益详细
	 * @param request, response, model
	 * @return
	 */
	@RequestMapping(value = "/selecearningsdetail")
	public String selecearningsdetail(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageUtils<Parameters> pageBean = adminiStratorService.selecearningsdetail(request);
		User admin = userService.selectUserById(StringUtil.getIntString(request.getParameter("merid")));
		model.addAttribute("admin", admin);
		model.addAttribute("merid", request.getParameter("merid"));
		model.addAttribute("ordernum", request.getParameter("ordernum"));
		model.addAttribute("paysource", request.getParameter("paysource"));
		model.addAttribute("paytype", request.getParameter("paytype"));
		model.addAttribute("status", request.getParameter("status"));
		model.addAttribute("startTime", request.getParameter("startTime"));
		model.addAttribute("endTime", request.getParameter("endTime"));
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("earningsdetail", pageBean.getListMap());
		model.addAttribute("pageBean", pageBean);
		return "computer/merearningsdetail";
	}
	
	/**
	 * 根据订单号查询收益账单详情记录
	 */
	@RequestMapping(value = "/earningsorderdetail")
	public String meriAdvanceDetails(HttpServletRequest request, HttpServletResponse response, Model model) {
		String ordernum = request.getParameter("ordernum");
		String paysource = request.getParameter("paysource");
		Integer type = StringUtil.getIntString(request.getParameter("type"));
		if (paysource.equals("1")) {
			List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
			model.addAttribute("order", chargeRecord.get(0));
		} else if (paysource.equals("2")) {
			InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
			model.addAttribute("order", inCoins);
		} else if (paysource.equals("3")) {
			OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
			model.addAttribute("order", offlineCard);
		} else if (paysource.equals("4")) {
			Withdraw withdraw = withdrawService.selectWithdrawByOrdernum(ordernum);
			model.addAttribute("order", withdraw);
		} else if (paysource.equals("5")) {
			Money money = moneyService.queryMoneyByOrdernum(ordernum);
			model.addAttribute("order", money);
		} else if (paysource.equals("6")) {
			//TODO 在线卡不完全正确判断
			OnlineCardRecord cardRecord = null;
			if(type==1){
				cardRecord = onlineCardRecordService.selectRecordByOrdernum( ordernum, 3, null);
				if(cardRecord==null) cardRecord = onlineCardRecordService.selectRecordByOrdernum( ordernum, 6, null);
			}else if(type==2){
				cardRecord = onlineCardRecordService.selectRecordByOrdernum( ordernum, 5, 2);
				if(cardRecord==null) cardRecord = onlineCardRecordService.selectRecordByOrdernum( ordernum, 7, 2);
			}
			model.addAttribute("order", cardRecord);
		}
		model.addAttribute("paysource", paysource);
		return "computer/ordermerdetail";
	}
	
	/**
	 * 根据商户id查询商户欠款金额 明细
	 * @param request, response, model
	 * @return
	 */
	@RequestMapping(value = "/selectMeriAdvance")
	public String selectMeriAdvance(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageUtils<Parameter> pageBean = generalDetailService.advanceDetail(request);
		User admin = userService.selectUserById(StringUtil.getIntString(request.getParameter("merid")));
		model.addAttribute("admin", admin);
		model.addAttribute("ordernum", request.getParameter("ordernum"));
		model.addAttribute("paysource", request.getParameter("paysource"));
		model.addAttribute("paytype", request.getParameter("paytype"));
		model.addAttribute("status", request.getParameter("status"));
		model.addAttribute("startTime", request.getParameter("startTime"));
		model.addAttribute("endTime", request.getParameter("endTime"));
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("advanceMoney", pageBean.getListMap());
		model.addAttribute("pageBean", pageBean);
		return "computer/advancemoney";
	}

	
	/**
	 * 修改指定人员费率
	 * @param id, feerate, model
	 * @return
	 */
	@RequestMapping(value = "/updateStratorfeerate")
	@ResponseBody
	public int updateStratorfeerate(Integer id, Integer feerate, Model model) {
		User admin = (User) request.getSession().getAttribute("admin");
		User user = new User();
		user.setId(id);
		user.setFeerate(feerate);
		operateRecordService.insertoperate("修改商户费率",admin.getId(),id,2,0,null,null);
		return adminiStratorService.updateUserById(user);
	}
	
	/**
	 * @Description： 更改对公账户费率
	 * @author： origin          
	 * 创建时间：   2019年7月8日 下午12:55:23
	 */
	@RequestMapping(value = "/updateBankRate")
	@ResponseBody
	public Map<String, Object> updateBankRate(Integer bankid, Integer rate){
		Map<String, Object> result = new HashMap<String, Object>();
		result = adminiStratorService.updateBankRate(bankid,rate);
		return result;
	}
	
	/**
	 * 查询人员（银行卡信息）
	 * @param  request, response, model
	 * @return
	 */
	@RequestMapping(value = "/selectAdminiStrator")
	public String selectAdminiStrator(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Map<String, Object>> userbank = adminiStratorService.selectAdmini(request);
		model.addAttribute("userbank", userbank);
		return "computer/userBank";
	}
	
	
	
	/**
	 * 查询人员(普通用户)
	 * @param request, response, model
	 * @return
	 */
	@RequestMapping(value = "/selectGeneral")
	public String selectUserGeneral(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageUtils<Parameter> pageBean = adminiStratorService.selectGeneralInfo(request);
		model.addAttribute("username", request.getParameter("username"));
		model.addAttribute("realname", request.getParameter("realname"));
		model.addAttribute("phoneNum", request.getParameter("phoneNum"));
		model.addAttribute("orderID", request.getParameter("orderID"));
		model.addAttribute("sort", request.getParameter("sort"));
		model.addAttribute("murealname", request.getParameter("murealname"));
		model.addAttribute("rephoneNum", request.getParameter("rephoneNum"));
		model.addAttribute("areaname", request.getParameter("areaname"));
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("walletmoney", pageBean.getMap().get("walletmoney"));
		model.addAttribute("adminiStrator", pageBean.getListMap());
 		return "computer/userGeneral";
	}
	
	/**
	 * @Description： 绑定时查询对应信息
	 * @author： origin 
	 */
	@RequestMapping(value = "/selectbindinginfo")
	public String selectByIdArea(Integer uid, Integer source, Integer merid,  Model model) {
		if(source==1){
			List<User> useradmin = userService.getUserListByRank(0);
			List<User> userlist = userService.getUserListByRank(2);
			List<User> list = new ArrayList<User>();
			list.addAll(useradmin);
			list.addAll(userlist);
			model.addAttribute("order", list);
		}else if(source==2){
			Area area = new Area();
			if(merid!=0) area.setMerid(merid);
			List<Area> arealist =  areaService.selectAreaList(area);
			model.addAttribute("order", arealist);
		}
		model.addAttribute("uid", uid);
		model.addAttribute("source", source);
		return "computer/userbinding";
		
	}
	
	/**
	 * 强制解绑商户 (根据用户id解绑商户)
	 * @param request, response, model
	 * @return
	 */
	@RequestMapping(value = "/genunbindmer")
	@ResponseBody
	public int genunbindmer( Integer uid, Model model) {
		User admin = (User) request.getSession().getAttribute("admin");
		User user = new User();
		user.setId(uid);
		user.setMerid(0);
		user.setAid(0);
		int unbind = userService.updateUserById(user);
		operateRecordService.insertoperate("解绑商户",admin.getId(),uid,1,0,null,null);
		return unbind;
	}
	
	/** 
	 * @Description： 强制绑定商户 
	 * @author： origin           
	 * @param uid
	 */
	@RequestMapping(value = "/bindingagent")
	public String bindingAgent( Integer uid, Integer merid) {
		User admin = (User) request.getSession().getAttribute("admin");
		User user = new User();
		user.setId(uid);
		user.setMerid(merid);
		userService.updateUserById(user);
		operateRecordService.insertoperate("绑定商户",admin.getId(),uid,1,0,null,null);
		return "redirect:/pcadminiStrator/selectGeneralUserInfo";
	}
	
	/** 
	 * @Description： 强制解绑小区 (根据用户id解绑，赋值小区为0)
	 * @author： origin           
	 * @param uid
	 */
	@RequestMapping(value = "/unbindarea")
	@ResponseBody
	public int unbindArea( Integer uid) {
		User admin = (User) request.getSession().getAttribute("admin");
		User user = new User();
		user.setId(uid);
		user.setAid(0);
//		user.setMerid(0);
//		user.setBalance(0.00);
		int unbind = userService.updateUserById(user);
		operateRecordService.insertoperate("绑定小区",admin.getId(),uid,1,0,null,null);
		return unbind;
	}

	/** 
	 * @Description： 强制绑定小区 
	 * @author： origin           
	 * @param uid
	 */
	@RequestMapping(value = "/bindingarea")
	public String bindingArea( Integer uid, Integer areaid) {
		User admin = (User) request.getSession().getAttribute("admin");
		Area area =  areaService.selectByIdArea(areaid);
		User user = new User();
		user.setId(uid);
		user.setMerid(area.getMerid());
		user.setAid(areaid);
		userService.updateUserById(user);
		operateRecordService.insertoperate("绑定小区",admin.getId(),uid,1,0,null,null);
		return "redirect:/pcadminiStrator/selectGeneralUserInfo";
	}
	
	/**
	 * 根据用户id查询钱包详细
	 * @param request, response, model
	 * @return
	 */
	@RequestMapping(value = "/selecwalletdetail")
	public String selecWalletInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageUtils<Parameters> pageBean = adminiStratorService.selecwalletdetail(request);
		User admin =  userService.selectUserById(StringUtil.getIntString(request.getParameter("uid")));
		model.addAttribute("admin", admin);
		model.addAttribute("ordernum", request.getParameter("ordernum"));
		model.addAttribute("paysource", request.getParameter("paysource"));
		model.addAttribute("startTime", request.getParameter("startTime"));
		model.addAttribute("endTime", request.getParameter("endTime"));
		model.addAttribute("current", (pageBean.getCurrentPage()-1)*10);
		model.addAttribute("walletdetail", pageBean.getListMap());
		model.addAttribute("pageBean", pageBean);
		return "computer/userwalletdetail";
	}
	
	/**
	 * 钱包明细根据订单号和来源查询该条订单
	 * @param request, response, model
	 * @return
	 */
	@RequestMapping(value = "/genwalletdetail")
	public String genwalletdetail(String ordernum, String paysource, Model model) {
		///** 金额来源 1、充值-2、充电-3、投币-4、离线卡充值-5、退款到钱包 6、钱包退款 7虚拟充值  8虚拟退款 */
		//http://localhost/pcadminiStrator/genwalletdetail?genre=1&ordernum=20190713193252926502265&paysource=7
		if (paysource.equals("1") || paysource.equals("5") || paysource.equals("6") || paysource.equals("7") || paysource.equals("8") ) {
			Money money = moneyService.queryMoneyByOrdernum(ordernum);
			model.addAttribute("order", money);
		} else if (paysource.equals("2")) {
			List<ChargeRecord> chargeRecord = chargeRecordService.getOrderByOrdernum(ordernum);
			model.addAttribute("order", chargeRecord.get(0));
		} else if (paysource.equals("3")) {
			InCoins inCoins = inCoinsService.selectInCoinsRecordByOrdernum(ordernum);
			model.addAttribute("order", inCoins);
		} else if (paysource.equals("4")) {
			OfflineCard offlineCard = offlineCardService.selectOfflineCardByOrdernum(ordernum);
			model.addAttribute("order", offlineCard);
		} else if (paysource.equals("9")) {
			OnlineCardRecord cardRecord = new OnlineCardRecord();
			cardRecord = onlineCardRecordService.selectRecordByOrdernum( ordernum, 1, null);
			if(cardRecord==null) cardRecord = onlineCardRecordService.selectRecordByOrdernum( ordernum, 2, null);
			model.addAttribute("order", cardRecord);
		}
		model.addAttribute("paysource", paysource);
		model.addAttribute("ordernum", ordernum);
		model.addAttribute("genre", request.getParameter("genre"));
		return "computer/ordergendetail";
	}
	
	/**
	 * @Description: 获取用户显示页面
	 * @author： origin 
	 */
	@RequestMapping(value = "/getuserinfo")
	public String getUserInfo( Model model) {
		return "computer/userinfo";
	}
	
	
	/**
	 * @Description： 根据手机号获取用户信息
	 * @author： origin 
	 */
	@RequestMapping(value = "/getaccountbyph")
	@ResponseBody
	public User getAccount(String phone, Model model) {
		User user =  userService.existAdmin(phone);
		if(null==user) user = new User();
		return user;
	}
	
	/**
	 * @Description： 根据用户id获取用户信息
	 * @author： origin 
	 */
	@RequestMapping(value = "/getaccountbyId")
	@ResponseBody
	public User getAccountById( Integer uid, Model model) {
		User user =  userService.selectUserById(uid);
		if(null==user) user = new User();
		return user;
	}
	
	/**
	 * @Description：   根据商户查询合伙人信息
	 * @author： origin 
	 * @return
	 */
	@RequestMapping(value = "/selectpartnerinfo")
	public String selectPartnerInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<User> pageBean = adminiStratorService.selectPartnerInfo(request);
		model.addAttribute("admin", CommonConfig.getAdminReq(request));
		model.addAttribute("pageBean", pageBean);
		return "computer/userRecord";
	}
	
	
	
	
	
}
