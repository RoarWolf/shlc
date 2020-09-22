package com.hedong.hedongwx.service;

import java.util.*;

/**
 * 
 * @Description: 微信公用方法借口
 * @Author: origin  创建时间:2020年7月20日 上午10:55:02
 * @common:
 */
public interface WeChatMethodService {
	
//=-=-= 从微信上查询付款订单信息  =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=	
	/**
	 * @Description：根据订单号查询该订单在微信中存不存在；信息存在，判断并解析 返回过来；不存在或错误返回错误信息
	 * @author origin
	 * @param ordernum 商户订单号   traordernum:交易订单号（微信订单号）  submchid:(服务商下的)子商户号
	 */
	Map<String, Object> inquireWeChatOrderData(String ordernum, String traordernum, String submchid);
	

	/**
	 * @Description：根据订单号查询该订单在微信中存不存在；信息存在，判断并解析 返回过来；不存在或错误返回错误信息
	 * @author origin
	 * @param ordernum 商户订单号   submchid:(服务商下的)子商户号
	 */
	Map<String, Object> inquireWeChatOrderByOrdernum(String ordernum, String submchid);
	

	/**
	 * @Description：根据订单号查询该订单在微信中存不存在；信息存在，判断并解析 返回过来；不存在或错误返回错误信息
	 * @author origin
	 * @param  traordernum:交易订单号（微信订单号）  submchid:(服务商下的)子商户号
	 */
	Map<String, Object> inquireWeChatOrderByTraorder( String traordernum, String submchid);
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=	

//=-=-= 从微信上查询退款订单信息  =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=	
	/**
	 * @method_name: inquireWXRefOrderData 
	 * @Description: 根据条件从微信上查询退款订单信息
	 * @param ordernum：商户订单号   traordernum：交易订单号[微信订单号] outrefundno：商户退款订单号   
	 * @paramrefundid 交易退款订单号[微信退款订单号]  submchid：子商户id
	 * @param offset：偏移量 [偏移量，当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录]
	 * @return 微信订单详情
	 * @Author: origin  创建时间:2020年7月20日 下午6:31:35
	 * @common:
	 */
	Map<String, Object> inquireWXRefOrderData(String submchid, String ordernum, String traordernum, String refundid,
		String outrefundno, String offset);
	
	/**
	 * @method_name: inquireWXRefOrderByOrdernum
	 * @Description: 根据商户订单号查询微信退款订单信息
	 * @param submchid： 子商户id
	 * @param ordernum：商户订单号
	 * @param offset：偏移量 [偏移量，当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录]
	 * @Author: origin  创建时间:2020年7月22日 下午2:41:28
	 * @common:
	 */
	Map<String, Object> inquireWXRefOrderByOrdernum(String submchid, String ordernum, String offset);
	
	/**
	 * @method_name: inquireWXRefOrderByTraorder
	 * @Description: 根据微信订单号查询微信退款订单信息
	 * @param submchid： 子商户id
	 * @param traordernum：交易订单号【微信订单号】
	 * @param offset：偏移量 [偏移量，当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录]
	 * @Author: origin  创建时间:2020年7月22日 下午2:44:26
	 * @common:
	 */
	Map<String, Object> inquireWXRefOrderByTraorder(String submchid, String traordernum, String offset);
	
	/**
	 * @method_name: inquireWXRefOrderByOutref
	 * @Description: 根据商户订单号查询微信退款订单信息
	 * @param submchid： 子商户id
	 * @param outrefundno：商户退款单号
	 * @param offset：偏移量 [偏移量，当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录]
	 * @Author: origin  创建时间:2020年7月22日 下午2:45:51
	 * @common:
	 */
	Map<String, Object> inquireWXRefOrderByOutref(String submchid, String outrefundno, String offset);
	
	/**
	 * @method_name: inquireWXRefOrderByRefundid
	 * @Description: 根据商户订单号查询微信退款订单信息
	 * @param submchid： 子商户id
	 * @param refundid：微信退款单号
	 * @param offset：偏移量 [偏移量，当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录]
	 * @Author: origin  创建时间:2020年7月22日 下午2:47:05
	 * @common:
	 */
	Map<String, Object> inquireWXRefOrderByRefundid(String submchid, String refundid, String offset);
	
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=	

//=-=-= 根据参数查询下载微信账单  =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=		
	/**
	 * 
	 * @method_name: downloadWeChatBill
	 * @Description: 根据参数查询下载微信账单，获取信息判断并解析
	 * @param time：查询交易订单的时间(对账单日期)   billtype 查询交易订单的账单类型[ALL，返回当日所有订单信息，默认值; SUCCESS，
	 * 		      返回当日成功支付的订单; REFUND，返回当日退款订单; RECHARGE_REFUND，返回当日充值退款订单;] appid：服务商的APPID 
	 *        mch_id：商户号   sub_appid：子商户公众账号ID   sub_mch_id：子商户号
	 * @Author: origin  创建时间:2020年7月21日 下午4:20:17
	 * @common:
	 */
	Map<String, Object> downloadWeChatBill(String time, Integer billtype, String appid, String mch_id,
			 String sub_appid, String sub_mch_id);
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=


//=-=-= 从微信上查询退款订单信息  =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=	
	Map<String, Object> WXPortionPayRefund(String ordernum, String submchid, String refundfee);
	

	Map<String, Object> WXPayRefund(String ordernum, String submchid);
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
