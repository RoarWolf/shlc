package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.AdminiStrator;
import com.hedong.hedongwx.entity.MerAmount;
import com.hedong.hedongwx.entity.Parameter;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;

public interface UserDao {

	/**
	 * 添加用户
	 * @param user
	 */
	int addUser(User user);
	
	/**
	 * 删除用户
	 * @param id
	 * @return 
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
	 * 修改用户
	 * @param user
	 * @return 
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
	User getUserByPhoneNumAndPassword(@Param("phoneNum") String phoneNum, @Param("password") String password);
	
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
	int updateBalanceByOpenid(@Param("balance") Double balance, @Param("sendmoney") Double sendmoney, @Param("openid") String openid
			, @Param("earnings") Double earnings);
	
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
	 * 通过openid更新用户表信息
	 * @param user
	 * @return
	 */
	int registerUser(User user);
	
	/**
	 * 绑定普通用户所属商户
	 * @param uid
	 * @param merid
	 * @return
	 */
	int bindUserBelongMerid(@Param("uid") Integer uid,@Param("merid") Integer merid);
	
	/**
	 * 删除指定小区和其所有用户的关联
	 * @return
	 */
	int unbindUserAndAid(Integer aid);
	
	/**
	 * 通过phoneNum手机号判断该用户是否存在且唯一
	 * @param parameter
	 * @return
	 */
	User selectAdmin(@Param("phoneNum")String phoneNum);
	
	/**
	 * 通过商户id修改金额
	 * @param parameter
	 * @return
	 */
	int updateUserEarnings(@Param("edittype")Integer edittype,@Param("money")Double money,@Param("merid")Integer merid);
	
	/**
	 * 通过商户id修改商户总计表的在线总计和当日实时总计
	 * @param edittype
	 * @param money
	 * @param merid
	 * @return
	 */
	int updateMerAmount(@Param("edittype")Integer edittype,@Param("money")Double money,@Param("merid")Integer merid);
	
	/**
	 * 添加商户总计
	 * @param merAmount
	 * @return
	 */
	int insertMerAmount(MerAmount merAmount);
	
	int everydayResetNowEarn();
	
	/**
	 * 查询商户总计
	 * @param merid
	 * @return
	 */
	MerAmount selectMerAmountByMerid(Integer merid);
	
	//List<User> selectAdmin(@Param("phoneNum")String phoneNum);
	//-- ************************************************************************************************  --
	List<AdminiStrator> selectAdminiStrator(AdminiStrator adminiStrator);

	List<AdminiStrator> selectUserInfo(AdminiStrator adminiStrator);

	List<Map<String, Object>> selectUserInfos(Parameter parameter);
	
	//根据商户id查询对应用户的钱包金额(即商户欠账总金额)
	Map<String, Object> arrearageMoneySum(@Param("merid")Integer merid);

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	List<Map<String, Object>> selectDealerUserInfo(Parameters parameter);

	List<Map<String, Object>> selectGeneralUserInfo(Parameters parameters);
	
	//根据Unionid查询用户
	User getUserByUnionid(String unionid);

	/**
	 * @Description： 根据商户查询合伙人信息
	 * @author： origin
	 */
	List<User> selectPartner(Integer intString);

	/**
	 * @Description： 查询商户未体现金额
	 * @author： origin  
	 */
	Double earningsMoney(@Param("uid")Integer uid);

	/**
	 * @Description：根据商户id查询其名下商户 
	 * @author： origin          
	 * 创建时间：   2019年5月29日 下午6:18:44 
	 */
	List<Map<String, Object>> selectmemberinfo(Parameters parame);

	/**
	 * @Description： 查询有效商户信息
	 * @author： origin   2019年11月6日 下午3:43:34 
	 */
	List<Map<String, Object>> inquireValidDealer(Parameters parameter);
	
	/**
	 * @Description： 查询有效商户信息
	 * @author： origin   2019年11月6日 下午3:43:34 
	 */
	List<Map<String, Object>> inquireDisvalidDealer(Parameters parameter);

	/**
	 * @Description： 
	 * @param: 
	 * @author： origin   2019年12月10日 下午2:25:41 
	 */
	Map<String, Object> selectmemberbalance(Parameters parame);

	/**
	 * @Description：
	 * @author： origin
	 * @createTime：2019年12月31日下午2:55:36
	 * @comment:
	 */
	Map<String, Object> inquireClientsNum(Parameters parame);

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	
	/**
	 * 超级管理员查看代理商
	 * @param parameters
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> selectAgents(Parameters parameters);
	
	/**
	 * 超级管理员查看代理商数
	 * @param parameters
	 * @return Integer
	 */
	Integer selectAgentsCount(Parameters parameters);
	
	
	/**
	 * 超级管理员根据代理商的id,查询名下的商家
	 * @param parameters
	 * @return List<Map<String,Object>>
	 */
	List<Map<String,Object>> selectMerByAgentId(Parameters parameters);
	
	
	/**
	 * 将商家绑定到代理商
	 * @param id 代理商id
	 * @param merId 商家id
	 */
	void bindAgent(@Param("agentId")Integer id, @Param("merId")Integer merId);
	
	/**
	 * 将商家从代理商中解除绑定
	 * @param merId 商家id
	 */
	void removeBindAgent(@Param("merId")Integer merId);
	
	/**
	 * 查询代理商名下有效的商家
	 * @param parameters
	 * @return List<Map<String,Object>>
	 */
	List<Map<String,Object>> selectValidMerByAgentId(Parameters parameters);
	
	
	/**
	 * 查询用户设备表中有效商家的id
	 * @param 
	 * @return List<Map<String,Object>>
	 */
	List<Map<String, Object>> selectValidMerId();
	
	/**
	 * 超级管理员配置微信子商户信息
	 * @param user
	 */
	void addSubMer(User user);
	
	/**
	 * 超级管理员删除子商户的信息
	 * @param user
	 */
	void updateSubMerById(User user);
	
	/**
	 * 根据商户id或者微信商户号查询多条信息
	 * @param user
	 * @return
	 */
	List<Map<String, Object>> selectSubMerByMchid(User user);
	
	/**
	 * 根据微信商户号删除商户的配置信息
	 * @param user
	 */
	void delSubMerByMchid(User user);
	
	/**
	 * 根据设备号查询商户微信配置信息
	 * @param code 设备号
	 * @return 
	 */
	Map<String, Object> selectConfigDataByCode(@Param("code") String code);
	
	/**
	 * 根据商户的id号查询商户微信配置信息
	 * @param merId 商户id
	 * @return
	 */
	Map<String, Object> selectSubMerConfigById(@Param("merId")Integer merId);
	
	/**
	 * 添加特约商户用户
	 * @param user 用户
	 */
	void addSubUser(User user);
	
	/**
	 * 根据服务商下的openid关联查询用户
	 * @param openid 
	 * @return {@link Use}
	 */
	User selectSubUserByOpenid(@Param("subOpenid")String subOpenid); 
	
	/**
	 * 查询所有开启自动提现收益大于5的商家
	 * @return {@link List}
	 */
	List<User> selectOnAutoWithdrawMer();
	
	/**
	 * 查询当前商户下所有子账户
	 * @param rank
	 * @param rank
	 * @return
	 */
	List<User> selectUserByRankMid(@Param("rank")Integer rank,@Param("merid")Integer merid);

	/**
	 * @Description：
	 * @param parameter
	 * @author： origin 2020年7月8日下午3:05:50
	 * @comment:
	 */
	List<Map<String, Object>> inquireBypassAccount(Parameters parameter);
	
	/**
	 * 根据设备号查询商家信息和设备信息
	 * @author zz 
	 * @param code 设备号
	 * @return {@link Map}
	 */
	Map<String, Object> selectUserByCode(@Param("code")String code);

	/**
	 * @method_name: amountStatisticsInfo
	 * @Description: 
	 * @param object
	 * @Author: origin  创建时间:2020年9月8日 下午3:14:47
	 * @common:   
	 */
	Map<String, Object>  amountStatisticsInfo(@Param("merid")Integer merid);
}

