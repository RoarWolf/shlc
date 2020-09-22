package com.hedong.hedongwx.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.utils.PageUtils;

public interface OnlineCardService {

	int insertOnlineCard(OnlineCard onlineCard);

	int updateOnlineCard(OnlineCard onlineCard);
	
	List<OnlineCard> selectOnlineCardList(Integer uid);
	
	OnlineCard selectOnlineCardById(Integer id);
	
	OnlineCard selectOnlineCardByCardID(String cardID);

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询在线卡信息
	PageUtils<Parameters> selectOnlinecard(HttpServletRequest request);

	//卡操作——删除	   物理删除（删除该条数据）
	int removecardbyId(Integer id);
	
	//根据Id解除绑定用户
	int updateOnlineCarduIdbyId(Integer id);
	
	//根据用户id查询用户名下的所有在线卡信息
	List<Map<String, Object>> selectonlinecardbyuid(Integer uid);

	/**
	 * @Description： 根据卡号修改信息
	 * @author： origin  
	 */
	int updateOnlineCardBycard(OnlineCard oncard);

	/**
	 * @Description： 
	 * @author： origin   
	 */
	int insertOnline(OnlineCard sourcecard);

	/**
	 * @Description： 根据条件查询在线卡信息
	 * @author： origin          
	 * 创建时间：   2019年5月31日 下午4:20:32 
	 */
	List<OnlineCard> onlineCardList(Integer merid, String onlinecard);

	/**
	 * @Description： 扫码时修改卡号
	 * @author： origin          
	 */
	Map<String, Object> scanamendonline( Integer relevawalt, String oricard, String nowcard);

	/**
	 * @Description： 扫码绑定在线卡 	  @author： origin    2019年6月19日 下午2:49:02 
	 */
	Map<String, Object> scanbinding( Integer relevawalt, String cardNum);

	/**
	 * @Description： 商户虚拟充值在线卡
	 * @author： origin 创建时间：   2019年7月10日 下午2:26:20 
	 */
	 Map<String, Object> mercVirtualOnlinePay(User user, Double paymoney, Double sendmoney, Integer id, Integer status);
	
	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月15日 上午10:29:43 
	 */
	 Map<String, Object> mercVirtualReturn(Integer id);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月13日 下午3:56:04 
	 */
	OnlineCard selectOnlineCardByFigure(String cardNumber);

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//=============================================================================
	/**
	 * separate
	 * @Description： 虚拟充值在线卡操作
	 * @author： origin   2019年11月9日 上午11:47:04
	 */
	Object virtualTopOnline(HttpServletRequest request);

	/**
	 * separate
	 * @Description： 查询用户在线卡信息
	 * @author： origin   2019年11月9日 上午11:47:04
	 */
	Object inquireTouristOnlineData(HttpServletRequest request);

	/**
	 * separate
	 * @Description：在线卡卡信息查看
	 * @author： origin 
	 */
	Object onlineCardData(HttpServletRequest request);

	/**
	 * newest WeChat
	 * @Description： 查询在线卡信息
	 * @author： origin   2019年12月2日 上午11:11:02 
	 */
	Map<String, Object> inquireOnlineCardData(HttpServletRequest request);

	/**
	 * newest WeChat
	 * @Description： 商户绑定在线卡
	 * @author： origin   2019年12月2日 上午11:27:51 
	 */
	Map<String, Object> dealBindingOnlineCard(Integer merid, Integer areaid, String cardID, Integer status);

	/**
	 * @Description： 查询小区在线卡信息
	 * @author： origin   2019年12月6日 下午1:47:16 
	 */
	Map<String, Object> inquireOnlineInfo(HttpServletRequest request, User user);

	/**
	 * @Description： 
	 * @param: 
	 * @author： origin   2019年12月7日 下午4:20:00 
	 */
	List<Map<String, Object>> selectOonlineConsume(Parameters parame);

	/**
	 * @Description： 解除绑定的在线卡
	 * @author： origin   2019年12月23日 下午2:29:11 
	 */
	Object unbindingOnlineCard(HttpServletRequest request);

	/**
	 * @Description：删除绑定的在线卡
	 * @author： origin
	 * @createTime：2019年12月26日上午9:40:46
	 */
	Object deleteOnlineCard(HttpServletRequest request);

	/**
	 * @Description：
	 * @param tourid
	 * @param cardnum
	 * @return
	 * @author： origin
	 * @createTime：2020年3月25日下午6:11:48
	 * @comment:
	 */
	Map<String, Object> bindingOnlineByUid(Integer tourid, String cardnum);
	
	//=============================================================================
	/**
	 * 根据用户扫描的卡号判断商家是否为微信特约
	 * @param cardNumber
	 * @return
	 */
	Map<String, Object> seleSubMerByCardNumber(String cardNumber);

	/**
	 * @Description：获取在线卡数量
	 * @author： origin 2020年6月5日下午5:49:22
	 */
	Integer onlinecount(OnlineCard online);

	/**
	 * @method_name: selectOnlineCardByCardNum
	 * @Description: 根据卡号查询在线卡相关信息
	 * @param cardNum 在线卡卡号
	 * @return
	 * @Author: origin  创建时间:2020年7月27日 下午6:13:10
	 * @common:   
	 */
	Map<String, Object> selectOnlineCardByCardNum(String cardNum);
}
