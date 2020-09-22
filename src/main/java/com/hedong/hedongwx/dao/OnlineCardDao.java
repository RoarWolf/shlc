package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.OnlineCard;
import com.hedong.hedongwx.entity.Parameters;

public interface OnlineCardDao {

	int insertOnlineCard(OnlineCard onlineCard);
	
	int updateOnlineCard(OnlineCard onlineCard);
	
	List<OnlineCard> selectOnlineCardList(Integer uid);
	
	OnlineCard selectOnlineCardById(Integer id);
	
	OnlineCard selectOnlineCardByCardID(String cardID);
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询在线卡信息
	List<Map<String, Object>> selectOnlinecard(Parameters parameters);
	
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
	List<OnlineCard> selectCardList(OnlineCard online);

	/**
	 * @Description： 在线卡计数
	 * @author： origin          
	 * 创建时间：   2019年6月10日 下午4:32:00 
	 */
	Integer onlinecount(OnlineCard online);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月12日 上午11:48:45 
	 */
	OnlineCard selectOnlineCardByFigure(@Param("figure")String oricard);

	/**
	 * @Description： 
	 * @author： origin 创建时间：   2019年7月13日 下午2:50:54 
	 */
	Integer insertEntiyOnline(OnlineCard onlineCard);
	
	/**
	 * 根据用户充值的卡号查询商家的信息
	 * @param cardNumber
	 * @return {@link Map}
	 */
	Map<String, Object> seleSubMerByCardNumber(@Param("cardId")String cardNumber);
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/

	/**
	 * @method_name: selectOnlineCardByCardNum
	 * @Description: 根据卡号查询在线卡相关信息
	 * @param cardNum 在线卡卡号
	 * @return
	 * @Author: origin  创建时间:2020年7月27日 下午6:10:49
	 * @common:   
	 */
	
	Map<String, Object> selectOnlineCardByCardNum(@Param("cardID")String cardNum);
}
