/**
 * 
 */
package com.hedong.hedongwx.service;

import java.util.Map;

/**
 * @Description: 
 * @Author: origin  创建时间:2020年7月25日 下午3:51:57
 * @common:   
 */

public interface CommonMethodService {

	/**
	 * @method_name: doWeChatRefund
	 * @Description: 退款统一测试
	 * @param orderid：订单号    ordersource：订单来源 [以交易记录为准]   password：退款类型   refundtype：密码
	 * @return
	 * @Author: origin  创建时间:2020年8月27日 下午7:08:15
	 * @common:
	 */
	Map<String, Object> doWeChatRefund(Integer orderid, Integer ordersource, Integer refundtype);

	
}
