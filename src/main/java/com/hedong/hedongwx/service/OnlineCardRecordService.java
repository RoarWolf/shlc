package com.hedong.hedongwx.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.OnlineCardRecord;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.utils.PageUtils;

public interface OnlineCardRecordService {

	List<OnlineCardRecord> selectOnlineCardRecordListByCardID(String cardID);
	
	int insertOnlineCardRecord(Integer uid,Integer merid,String ordernum,String cardID,String code,Double balance,Double money,Double sendmoney,
		Double accountmoney,Double topupbalance, Double givebalance, Integer type,Integer status,Integer flag,Date createTime,Integer operid);
	
	OnlineCardRecord selectRecordByOrdernum(String ordernum, Integer type, Integer flag);
	
	int updateRecord(OnlineCardRecord onlineCardRecord);

	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//查询在线卡操作记录信息
	PageUtils<Parameters> selectOnlineOperation(HttpServletRequest request);
	
	//查询在线卡消费记录信息
	PageUtils<Parameters> selectOonlineConsume(HttpServletRequest request);
	
	//添加操作记录
	int additionOnlineCardRecord(OnlineCardRecord onlineCardRecord);
	
	/**
	 * @Description： 根据订单id查询
	 * @author： origin
	 */
	OnlineCardRecord selectOnlineCard(Integer id);
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/

	/**
	 * @Description： 根据实体类查询
	 * @author： origin 
	 */
	List<OnlineCardRecord> selectCardRecord(OnlineCardRecord onlin);

	/**
	 * @Description： 
	 * @author： origin          
	 * 创建时间：   2019年6月5日 上午11:40:04 
	 * @param cardNum 
	 */
	List<Map<String, Object>> icCardRecord(String cardNum, Integer merid, String beginTime, String endTime, Integer uid, Integer offset);

	/**
	 * @Description： 根据订单号更改状态
	 * @author： origin          
	 * 创建时间：   2019年6月15日 上午11:57:58 
	 */
	Integer updateonlinestatus(String ordernum, Byte status);

	/**
	 * @Description： 
	 * @author： origin          
	 */
	Map<String, Object> scanonlinewecpay( String card);

	/**
	 * @Description： 
	 * @author： origin          
	 */
	Map<String, Object> scanonlinealipay(String card);

	//=======================================================================================
	/**
	 * separate
	 * @Description：在线卡消费记录信息查看
	 * @author： origin 
	 */
	Object onlineCardRecordData(HttpServletRequest request);

	/**
	 * separate
	 * @Description：在线卡操作记录信息查看
	 * @author： origin 
	 */
	Object onlineCardOperateRecord(HttpServletRequest request);



	
}
