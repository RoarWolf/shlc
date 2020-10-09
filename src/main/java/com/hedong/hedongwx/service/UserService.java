package com.hedong.hedongwx.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hedong.hedongwx.entity.Codestatistics;
import com.hedong.hedongwx.entity.MerAmount;
import com.hedong.hedongwx.entity.PackageMonth;
import com.hedong.hedongwx.entity.PackageMonthRecord;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.Privilege;
import com.hedong.hedongwx.entity.TradeRecord;
import com.hedong.hedongwx.entity.User;

public interface UserService {

	/**
	 * 添加用户
	 * @param user
	 */
	int addUser(User user);
	
	/**
	 * 添加用户
	 * @param user
	 */
	int addUserInfo(String openid);
	
	/**
	 * 删除用户
	 * @param id
	 */
	int deleteUserById(Integer id);
	
	/**
	 * 通过id查询用户
	 * @param id
	 * @return User
	 */
	User selectUserById(Integer id);
	
	/**
	 * 查询所有用户
	 * @return userList
	 */
	List<User> getUserList();
	
	/**
	 * 通过id修改用户
	 * @param id
	 */
	int updateUserById(User user);
	
	/**
	 * 通过等级查询所有用户
	 * @param rank
	 * @return userList
	 */
	List<User> getUserListByRank(Integer rank);
	
	/**
	 * 通过手机号和密码查询用户
	 * @param phoneNum
	 * @param password
	 * @return user
	 */
	User getUserByPhoneNumAndPassword(String phoneNum, String password);
	
	/**
	 * 通过手机号查询用户
	 * @param phoneNum
	 * @return user
	 */
	User getUserByPhoneNum(String phoneNum);
	
	/**
	 * 通过用户标识修改用户余额
	 * @param balance
	 * @param openid
	 */
	int updateBalanceByOpenid(Double walletmoney, Double givemoney, String openid,Double earnings);
	
	/**
	 * 通过用户名查询用户
	 * @param openid
	 * @return user
	 */
	User getUserByOpenid(String openid);
	
	/**
	 * 通过用户名查询用户
	 * @param openidApplet
	 * @return user
	 */
	User getUserByOpenidApplet(String openidApplet);
	
	/**
	 * 删除指定小区和其所有用户的关联
	 * @return
	 */
	int unbindUserAndAid(Integer aid);
	
	/**
	 * 根据传送的数据创建新的用户（注册用户）
	 * @param request
	 * @return 
	 */
	Object registerUser(HttpServletRequest request);
	
	/**
	 * 登录判断
	 * @param request
	 * @param response
	 * @return
	 */
	String adminlogin(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 登录信息验证
	 * @param request
	 * @param response
	 * @return
	 */

	List<Map<String, Object>> getUsertoSortList(String type);

	/**
	 * 用户信息修改
	 * @param request
	 */
	String saveuser(HttpServletRequest request);

	User existAdmin(String mobile);
	
	int bindUserBelongMerid(Integer uid,Integer merid);
	
	//商户的某个设备指定时间金额汇总
	Map<String, Object> agentnowmoneycollect(User user, String code, String begintime, String endtime);
	
	//通过用户查询每日收益
	List<Codestatistics> selectAllByMerid(Integer merid,String begintime,String endtime);
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//根据Unionid查询用户
	User getUserByUnionid(String unionid);
	
	//账号登录
	String accountlogin(String account, String password);
	
	//短信验证码登录
	String codelogin(String mobile, String security, String authcode, String authtime);

	/**
	 * @Description： 查询商户未体现金额
	 * @author： origin  
	 */
	Double earningsMoney(Integer uid);

	/**
	 * 通过商户id修改金额
	 * @param parameter
	 * @return
	 */
	int updateUserEarnings(Integer edittype,Double money,Integer merid);
	
	/**
	 * 通过商户id修改金额
	 * @param parameter
	 * @return
	 */
	int updateMerAmount(Integer edittype,Double money,Integer merid);
	
	/**
	 * 添加商户总计
	 * @param merAmount
	 * @return
	 */
	int insertMerAmount(Integer merid,Double totalOnlineEarn,Double nowOnlineEarn,
			Integer totalCoinsEarn,Integer nowCoinsEarn);
	
	/**
	 * 查询商户总计
	 * @param merid
	 * @return
	 */
	MerAmount selectMerAmountByMerid(Integer merid);
	
	/**
	 * 每日重置当天总计
	 * @return
	 */
	int everydayResetNowEarn();
	
	/**
	 * 根据用户id查询包月信息
	 * @param uid
	 * @return
	 */
	PackageMonth selectPackageMonthByUid(Integer uid);
	
	/**
	 * 添加包月信息
	 * @param changemonth 
	 * @param packageMonth
	 * @return
	 */
	int insertPackageMonth(Integer uid,Integer surpnum,Integer todaysurpnum,Integer everydaynum,
			Integer changemonth, Integer everymonthnum, Integer time,Double elec, Date endTime,Integer monthnum);
	
	/**
	 * 修改包月信息
	 * @param packageMonth
	 * @return
	 */
	int updatePackageMonth(Integer uid,Integer surpnum,Integer todaysurpnum,Integer everydaynum,
			Integer changemonth, Integer everymonthnum, Integer time,Double elec, Date endTime,Integer monthnum);
	
	/**
	 * 修改包月信息
	 * @param packageMonth
	 * @return
	 */
	int updatePackageMonthByEntity(PackageMonth packageMonth);
	
	/**
	 * 每日重置当日剩余次数
	 * @return
	 */
	int everydaynumReset();
	
	/**
	 * 添加包月记录
	 * @return
	 */
	int insertPackageMonthRecord(Integer uid,Integer merid,String ordernum,Double money,
			Integer paysource,Integer status,Integer everydaynum,Integer changenum,Integer surpnum
			,Integer changemonth,Integer time,Double elec,Date createTime);
	
	/**
	 * 修改包月记录
	 * @return
	 */
	int updatePackageMonthRecord(Integer id,Integer ifrefund,String ordernum,Integer status);
	
	/**
	 * 根据订单号查询包月记录
	 * @param ordernum
	 * @return
	 */
	PackageMonthRecord selectMonthRecordByOrdernum(String ordernum,	Integer status);
	
	/**
	 * 通过用户id查询包月信息记录
	 * @param uid
	 * @return
	 */
	List<PackageMonthRecord> selectMonthRecordListByUid(Integer uid);
	
	/**
	 * 通过id查询包月记录
	 * @param id
	 * @return
	 */
	PackageMonthRecord selectMonthRecordById(Integer id);
	
	/**
	 * 根据商户id和时间查询部分字段（线上金额、投币金额）
	 * @param merid
	 * @param countTime
	 * @return
	 */
	Codestatistics selectMerYestEarn(Integer merid, String countTime);
	
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月1日 下午6:03:05 
	 */
	Map<String, Object> memberRole(User user, String member, Integer role);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年8月5日 上午11:31:48 
	 */
	Map<String, Object> selectUserRoleInfo(Integer id, Integer merid);

	/**
	 * @Description： 更改总服务电话
	 * @author： origin   2019年10月10日 下午4:54:57 
	 */
	User editServicePhone(Integer id, String mobile);

	/**
	 * @Description： 变更用户钱包金额信息
	 * @author： origin   2019年10月18日 上午10:03:03 
	 */
	void editUserBalance(Double refundmoney, Integer type, Integer uid);

	//=== 前后台分离数据接口    ===========================================================================================
	
	
	/**
	 * @Description： 
	 * @param: 
	 * @author： origin   2019年10月18日 下午4:05:38 
	 * @return 
	 */
	Map<String, Object> accountEnter(String phone, String password, Integer type);
	
	/**
	 * separate
	 * @Description： 短信验证码登录
	 * @author： origin  
	 */
	Object captchaEnter(HttpServletRequest request);
	
	/**
	 * separate
	 * @Description： 微信扫码登录
	 * @author： origin  
	 */
	Object wechatEnter(HttpServletRequest request);
	
	/**
	 * separate
	 * @Description： 获取商户信息
	 * @author： origin 
	 */
	Object getDealerListInfo(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 获取单个商户信息
	 * @author： origin  
	 */
	Object getDealerPersonInfo(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 获取用户信息
	 * @author： origin 
	 */
	Object getAccountListInfo(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 获取单个用户信息
	 * @author： origin  
	 */
	Object getAccountPersonInfo(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 商户收益明细查询
	 * @author： origin  
	 */
	Object dealerEarningsDetail(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 查看商户银行卡信息
	 * @author： origin  
	 */
	Object dealerBankCardData(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 修改指定商户个人费率
	 * @author： origin  
	 */
	Object editDealerPersonRate(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 修改指定商户对公费率
	 * @author： origin  
	 */
	Object editDealerCorporateRate(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 客户强制被绑定商户
	 * @author： origin  
	 */
	Object touristBindingDealer(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 客户强制解绑商户
	 * @author： origin  
	 */
	Object tourisTunbindDealer(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 客户被强制绑定小区
	 * @author： origin  
	 */
	Object bindingArea(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 客户被强制解除小区绑定
	 * @author： origin  
	 */
	Object unbindArea(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 强制绑定小区或商户时查询对应信息
	 * @author： origin  
	 */
	Object bindingInquireData(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 查询用户钱包详细
	 * @author： origin   2019年11月9日 上午11:47:04
	 */
	Object touristWalletData(HttpServletRequest request);

	/**
	 * @Description： 更新账户手机信息
	 * @author： origin   2019年11月26日 下午2:25:09 
	 */
	Object editAccountData(HttpServletRequest request);

	/**
	 * @Description：  更新账户自己信息
	 * @author： origin   2019年12月2日 下午5:05:41 
	 */
	Object editAccountAloneData(HttpServletRequest request);

	/**
	 * @Description： 查询会员中心
	 * @author： origin   2019年12月10日 下午1:45:58 
	 */
	Map<String, Object> selectmembercentre(HttpServletRequest request);

	/**
	 * @Description： ajax 查询会员中心
	 * @author： origin   2019年12月10日 下午3:30:51 
	 */
	Map<String, Object> ajaxMemberCentre(HttpServletRequest request);

	/**
	 * @Description：查询客户数量
	 * @author： origin
	 * @createTime：2019年12月31日下午2:50:53
	 */
	Integer inquireClientsNum(Integer dealid);

	
	
	
	
	//===================================================================================================================
	/**
	 * 超级管理员授权
	 * @param merId 用户id
	 * @param rank 等级
	 * @return Object
	 */
	
	Object setAgent(Integer userId,Integer rank);
	
	/**
	 * 超级管理员查看代理商
	 * @param parameters
	 * @return List<Map<String, Object>>
	 */
	Object selectAgents(HttpServletRequest request);
	
	/**
	 * 根据代理商的id,查询名下的商家
	 * @param parameters
	 * @return List<Map<String,Object>>
	 */
	Object selectMerByAgentId(Parameters parameters);
	
	
	/**
	 * 将商家绑定到代理商
	 * @param id 代理商id
	 * @param merId 商家id
	 */
	Object bindAgent(Integer id, Integer merId);
	
	/**
	 * 将商家从代理商中解除
	 * @param merId
	 */
	Object removeBindAgent(Integer merId);

	/**
	 * @Description：修改账户信息
	 * @author： origin 	2020年3月3日下午4:32:57
	 */
	Object redactAccountInfo(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 获取商户信息 (根据type不同，查询条件不同)
	 * @author： origin 
	 */
	Object getDealerInfo(HttpServletRequest request);
	
	/**
	 * 配置微信子商户的商户id
	 * @param request
	 * @return Object
	 */
	Object configMchid(HttpServletRequest request);

	/**
	 * @Description：查询用户相关信息（如所属商户、小区等）
	 * @param tourid 用户id
	 * @author： origin
	 * @createTime：2020年4月9日上午10:48:51
	 */
	Map<String, Object> selectGeneralUserInfo(Integer tourid);
	
	/**
	 * 根据设备号查询商户的配置信息
	 * @param code 设备号
	 * @return Map<String, Object>
	 */
	Map<String, Object> selectConfigDataByCode(String code);	
	
	/**
	 * 根据商家的id查询微信特约商户的配置信息
	 * @param merId
	 * @return Map<String, Object>
	 */
	Map<String, Object> selectSubMerConfigById(Integer merId);
	
	/**
	 * 添加特约商户下的用户
	 * @param user
	 */
	void addSubUser(User user);
	
	/**
	 * 根据服务商平台下openid用户查询用户信息
	 * @param subOpenid
	 * @return {@link User}
	 */
	User selectSubUserByOpenid(String subOpenid);
	
	/**
	 * 查询所有开启自动提现收益大于5小于1千的商家
	 * @return {@link List}
	 */
	List<User> selectOnAutoWithdrawMer();
	
	/**
	 * 根据子账号id获取权限
	 * @param uid
	 * @return
	 */
	List<Privilege> selectUserPrivilege(Integer uid);
	
	/**
	 * 查询当前商户下所有子账户
	 * @param rank
	 * @param merid
	 * @return
	 */
	List<User> selectUserByRankMid(Integer rank,Integer merid);
	
	/**
	 * 添加子账号权限
	 * @param uid
	 * @param pid
	 * @return
	 */
	int insertUserPrivliege(Integer uid,Integer pid);
	
	/**
	 * 删除子账号权限
	 * @param uid
	 * @param pid
	 * @return
	 */
	int deleteUserPrivliege(Integer uid,Integer pid);

	/**
	 * 根据子账号id获取权限
	 * @param uid
	 * @return
	 */
	List<Privilege> selectAllPrivilege(Integer id, Integer parentId);

	/**
	 * @Description：获取商户子账户信息
	 * @param request  response
	 * @return
	 * @author： origin  2020年7月8日上午9:01:26
	 * @comment: bypass account 
	 */
	Object getAccountData(HttpServletRequest request);

	/**
	 * @Description：解绑子账号绑定信息
	 * @param request
	 * @author： origin  2020年7月8日下午4:48:15
	 * @comment:
	 */
	Object addOrDelChileUser(HttpServletRequest request);

	/**
	 * 根据订单判断商家的收益是否充足
	 * @param tradeRecord
	 * @return {@link Boolean}
	 */
	boolean checkUserIfRich(TradeRecord tradeRecord);
	
	/**
	 * 用户登陆
	 * @param auth_code
	 * @param username
	 * @param imageUrl
	 * @return
	 */
	Map<String, Object> addUserByAuth_code(String auth_code,String username, String imageUrl);
	
	/**
	 * 查询用户信息
	 * @param userid
	 * @return
	 */
	Map<String, Object> queryUserInfo(Integer userid);
	
	/**
	 * 用户钱包充值（预订单）
	 * @param userid
	 * @param openid
	 * @param money
	 * @param request
	 * @return
	 */
	Map<String, Object> walletAppointCharge(Integer userid, Double money,HttpServletRequest request);
	
	/**
	 * 用户钱包充值（微信支付回调）
	 * @param resultMap
	 * @param openid
	 * @param ordernum
	 * @return
	 */
	Map<String, Object> walletCharge(Map<Object,Object> resultMap, String openid, String ordernum);
	
}
