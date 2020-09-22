package com.hedong.hedongwx.service.impl;

import java.util.*;

import org.springframework.stereotype.Service;

import com.github.wxpay.sdk.WXPay;
import com.hedong.hedongwx.service.WeChatMethodService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.WXPayConfigImpl;
import com.hedong.hedongwx.utils.WeiXinConfigParam;
import com.hedong.hedongwx.utils.XMLUtil;
/**
 * @Description: 微信公用方法借口   ServiceImpl
 * @Author: origin  创建时间:2020年7月20日 上午10:56:01
 * @common:
 */
@Service
public class WeChatMethodServiceImpl implements WeChatMethodService{
	
	/**
	 * 统一下单
	 * 应用场景： 除付款码支付场景以外，商户系统先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易会话标识后再按Native、
	 * JSAPI、APP等不同场景生成交易串调起支付。
	 * 是否需要证书：不需要
	 */
	public static String WXJSAPI_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	/**
	 * 查询订单
	 * 应用场景：该接口提供所有微信支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
	 * 需要调用查询接口的情况：
	 * ◆ 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知；
	 * ◆ 调用支付接口后，返回系统错误或未知交易状态情况；
	 * ◆ 调用被扫支付API，返回USERPAYING的状态；
	 * ◆ 调用关单或撤销接口API之前，需确认支付状态；
	 * 是否需要证书：不需要
	 */
	public static String WXJSAPI_ORDERQUERY = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	/**
	 * 关闭订单
	 * 应用场景：以下情况需要调用关单接口：商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；系统下单后，用户支付
	 * 超时，系统退出不再受理，避免用户继续，请调用关单接口。
	 * 是否需要证书：不需要
	 */
	public static String WXJSAPI_CLOSEORDER = "https://api.mch.weixin.qq.com/pay/closeorder";

	/**
	 * 申请退款
	 * 应用场景：当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，微信支付将在收到退款
	 * 请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
	 * 注意：
	 * 1.交易时间超过一年的订单无法提交退款；
	 * 2、微信支付退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。申请退款总金额不能超过订单金额。 
	 *    一笔退款失败后重新提交，请不要更换退款单号，请使用原商户退款单号。
	 * 3、请求频率限制：150qps，即每秒钟正常的申请退款请求次数不超过150次
	 *    错误或无效请求频率限制：6qps，即每秒钟异常或错误的退款申请请求不超过6次
	 * 4、每个支付订单的部分退款次数不能超过50次
	 * 5、如果同一个用户有多笔退款，建议分不同批次进行退款，避免并发退款导致退款失败
	 * 权限申请
	 * 服务商模式下，退款接口需要单独申请权限，指引链接：http://kf.qq.com/faq/170606Rnyq2u170606MJZNVB.html
	 * 是否需要证书：请求需要双向证书。 
	 */
	public static String WXJSAPI_REFUNDR = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	
	/**
	 * 查询退款
	 * 应用场景：提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款20分钟内到账，银行卡支付的退款3个工作日后重新
	 *        查询退款状态。
	 * 注意：如果单个支付订单部分退款次数超过20次请使用退款单号查询
	 * 分页查询
	 * 当一个订单部分退款超过10笔后，商户用微信订单号或商户订单号调退款查询API查询退款时，默认返回前10笔和total_refund_count（退
	 * 款单总笔数）。商户需要查询同一订单下超过10笔的退款单时，可传入订单号及offset来查询，微信支付会返回offset及后面的10笔，以此类推。
	 * 当商户传入的offset超过total_refund_count，则系统会返回报错PARAM_ERROR。
	 * 举例：
	 * 一笔订单下的退款单有36笔，当商户想查询第25笔时，可传入订单号及offset=24，微信支付平台会返回第25笔到第35笔的退款单信息，或商户
	 * 可直接传入退款单号查询退款
	 * 是否需要证书：不需要
	 */
	public static String WXJSAPI_REFUNDRQUERY = "https://api.mch.weixin.qq.com/pay/refundquery";

	/**
	 * 下载交易账单
	 * 应用场景：商户可以通过该接口下载历史交易清单。比如掉单、系统错误等导致商户侧和微信侧数据不一致，通过对账单核对后可校正支付状态。
	 * 注意：
	 * 1、微信侧未成功下单的交易不会出现在对账单中。支付成功后撤销的交易会出现在对账单中，跟原支付单订单号一致；
	 * 2、微信在次日9点启动生成前一天的对账单，建议商户10点后再获取；
	 * 3、对账单中涉及金额的字段单位为“元”。
	 * 4、对账单接口只能下载三个月以内的账单。
	 * 5、对账单是以商户号纬度来生成的，如一个商户号与多个appid有绑定关系，则使用其中任何一个appid都可以请求下载对账单。对账单中的appid
	 *    取自交易时候提交的appid，与请求下载对账单时使用的appid无关。
	 * 6、小微商户不单独提供对账单下载，如有需要，可在调取【下载对账单】API接口时不传sub_mch_id，获取服务商下全量特约商户（包括小微商户和
	 *    非小微商户）的对账单。
	 * 是否需要证书：不需要
	 */
	public static String WXJSAPI_DOWNLOADBILL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	
	/**
	 * 支付结果通用通知
	 * 应用场景：支付完成后，微信会把相关支付结果及用户信息通过数据流的形式发送给商户，商户需要接收处理，并按文档规范返回应答。
	 * 注意：
	 * 1、同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。
	 * 2、后台通知交互时，如果微信收到商户的应答不符合规范或超时，微信会判定本次通知失败，重新发送通知，直到成功为止（在通知一直不成功的情况下，
	 *    微信总共会发起多次通知，通知频率为15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h - 总计 24h4m），
	 *    但微信不保证通知最终一定能成功。
	 * 3、在订单状态不明或者没有收到微信支付结果通知的情况下，建议商户主动调用微信支付【查询订单API】确认订单状态。
	 * 特别提醒：
	 * 1、商户系统对于支付结果通知的内容一定要做签名验证,并校验返回的订单金额是否与商户侧的订单金额一致，防止数据泄漏导致出现“假通知”，造成资
	 *   金损失。
	 * 2、当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，如果处理过直接返回结果成功。在
	 *   对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
	 * 3、技术人员可登进微信商户后台扫描加入接口报警群，获取接口告警信息。
	 * 接口链接
	 * 该链接是通过【统一下单API】中提交的参数notify_url设置，如果链接无法访问，商户将无法接收到微信通知。
	 * 通知url必须为直接可访问的url，不能携带参数。示例：notify_url：“https://pay.weixin.qq.com/wxpay/pay.action”
	 * 是否需要证书：不需要
	 */
	public static String WXJSAPI_RESULT = "https://api.mch.weixin.qq.com/pay/downloadbill";
	
	/**
	 * 交易保障
	 * 应用场景：商户在调用微信支付提供的相关接口时，会得到微信支付返回的相关信息以及获得整个接口的响应时间。为提高整体的服务水平，协助商户
	 *        一起提高服务质量，微信支付提供了相关接口调用耗时和返回信息的主动上报接口，微信支付可以根据商户侧上报的数据进一步优化网络部
	 *        署，完善服务监控，和商户更好的协作为用户提供更好的业务体验。
	 * 接口地址
	 * https://api.mch.weixin.qq.com/payitil/report
	 * 是否需要证书：不需要
	 */
	public static String WXJSAPI_REPORT = "https://api.mch.weixin.qq.com/payitil/report";
	

	/**
	 * 退款结果通知
	 * 应用场景：当商户申请的退款有结果后（退款状态为：退款成功、退款关闭、退款异常），微信会把相关结果发送给商户，商户需要接收处理，并返回应答。 
	 *    对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，微信会通过一定的策略定期重新发起通知，尽可能提高通知的成功率，
	 *    但微信不保证通知最终能成功（通知频率为15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h - 总计 24h4m）。
	 *    注意：同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。 
	 *    推荐的做法是，当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，如果处理过直接返
	 *    回结果成功。在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。 
	 *    特别说明：退款结果对重要的数据进行了加密，商户需要用商户秘钥进行解密后才能获得结果通知的内容
	 * 解密方式
	 * 解密步骤如下： 
	 * （1）对加密串A做base64解码，得到加密串B
	 * （2）对商户key做md5，得到32位小写key* ( key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置 )
	 * （3）用key*对加密串B做AES-256-ECB解密（PKCS7Padding）
	 * 接口链接
	 * 在申请退款接口中上传参数“notify_url”以开通该功能
	 * 如果链接无法访问，商户将无法接收到微信通知。 
	 * 通知url必须为直接可访问的url，不能携带参数。示例：notify_url：“https://pay.weixin.qq.com/wxpay/pay.action”
	 * 是否需要证书：不需要
	 */
	public static String WXJSAPI_REFUNDRE = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	
	public Map<String, String> getMerchantConfig(String ordernum, String traordernum, 
			String submchid){
	SortedMap<String, String> params = new TreeMap<>();
	params.put("appid", WeiXinConfigParam.FUWUAPPID);
	params.put("mch_id", WeiXinConfigParam.MCHID);
//	params.put("sub_appid", "");
	params.put("sub_mch_id", submchid);
	return params;
	
	}
	
//===========================================================================================================	
	/**
	 * @method_name: inquireWeChatOrder 
	 * @Description: 根据订单号从微信上查询微信订单信息
	 * @param ordernum 商户订单号   traordernum 交易订单号[微信订单号]  submchid：子商户id
	 * @return 微信订单详情
	 * @Author: origin  创建时间:2020年7月20日 下午6:31:35
	 * @common:
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> inquireWeChatOrder(String ordernum, String traordernum, 
			String submchid){
		try {
			if(CommUtil.trimToEmpty(submchid).equals("")) submchid = WeiXinConfigParam.SUBMCHID;
			SortedMap<String, String> params = new TreeMap<>();
			params.put("appid", WeiXinConfigParam.FUWUAPPID);
			params.put("mch_id", WeiXinConfigParam.MCHID);
//			params.put("sub_appid", "");
			params.put("sub_mch_id", submchid);
			if(!CommUtil.trimToEmpty(traordernum).equals(""))params.put("transaction_id", traordernum);
			if(!CommUtil.trimToEmpty(ordernum).equals(""))params.put("out_trade_no", ordernum);
			params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
			//------------------------------------------------------
			// 生成签名并放入map集合里
			String sign = HttpRequest.createSign("UTF-8", params);
			params.put("sign", sign);
			String url = WXJSAPI_ORDERQUERY;
			String canshu = HttpRequest.getRequestXml(params);
			String result = HttpRequest.sendPost(url, canshu);
			Map<String, String> map = XMLUtil.doXMLParse(result);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, String>();
		}
		
	}
	
	/**
	 * @method_name: inquireWeChatOrderDate 
	 * @Description: 根据查询出的微信订单信息判断并解析
	 * @param ordernum 商户订单号   traordernum 交易订单号[微信订单号]  submchid：子商户id
	 * @return 微信订单详情
	 * @Author: origin  创建时间:2020年7月20日 下午6:31:35
	 * @common:
	 */
	public Map<String, Object> WeChatOrderDateParse(Map<String, String> map){
		Map<String, Object> resultdate = new HashMap<String, Object>();
		try {
//			Map<String, String> map = inquireWeChatOrder(ordernum, traordernum, submchid);
			String returncode = CommUtil.trimToEmpty(map.get("return_code"));
			//String returnmsg = CommUtil.trimToEmpty(map.get("return_msg")).toUpperCase();
			resultdate.put("WeChatOrderDate", map);
			Map<String, Object> mapdate = new HashMap<String, Object>();
			if(returncode.contains("SUCCESS")){//成功后解析微信订单
				mapdate.put("服务商的APPID", map.get("appid"));
				mapdate.put("商户号", map.get("mch_id"));
				mapdate.put("子商户号", map.get("sub_mch_id"));
				mapdate.put("微信订单号", map.get("transaction_id"));
				mapdate.put("商户订单号", map.get("out_trade_no"));
				mapdate.put("随机字符串", map.get("nonce_str"));
				mapdate.put("签名", map.get("sign"));

				mapdate.put("返回信息", map.get("return_msg"));
				mapdate.put("业务结果", map.get("result_code"));
				mapdate.put("返回状态码", map.get("return_code"));
				
				mapdate.put("交易状态", map.get("trade_state"));
				mapdate.put("付款银行", map.get("bank_type"));
				mapdate.put("用户标识", map.get("openid"));
				
				mapdate.put("标价币种", map.get("fee_type"));
				mapdate.put("现金支付金额", map.get("cash_fee"));
				mapdate.put("现金支付货币类型", map.get("cash_fee_type"));
				mapdate.put("标价金额", map.get("total_fee"));
				mapdate.put("交易状态描述", map.get("trade_state_desc"));
				mapdate.put("交易类型", map.get("trade_type"));
				mapdate.put("是否关注公众账号", map.get("is_subscribe"));
				mapdate.put("支付完成时间", map.get("time_end"));
			}else{
				mapdate.put("返回信息", map.get("return_msg"));
				mapdate.put("业务结果", map.get("result_code"));
				mapdate.put("返回状态码", map.get("return_code"));
			}
			resultdate.put("OrderDateParse", mapdate);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(3000, "订单信息查询异常", resultdate);
		}
		return resultdate;
	}
	
	/**
	 * @method_name: inquireOrderDataForWX 
	 * @Description: 根据查询出的微信订单信息判断并解析
	 * @param ordernum 商户订单号   traordernum 交易订单号[微信订单号]  submchid：子商户id
	 * @return 微信订单详情
	 * @Author: origin  创建时间:2020年7月20日 下午6:31:35
	 * @common:
	 */
	@Override
	public Map<String, Object> inquireWeChatOrderData(String ordernum, String traordernum, String submchid) {
		try {
			Map<String, String> map = inquireWeChatOrder( ordernum, traordernum, submchid);
			Map<String, Object> resultData = WeChatOrderDateParse(map);
			return resultData;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	
	/**
	 * @method_name: inquireOrderDataForWX 
	 * @Description: 根据查询出的微信订单信息判断并解析
	 * @param ordernum 商户订单号    submchid：子商户id
	 * @return 微信订单详情
	 * @Author: origin  创建时间:2020年7月20日 下午6:31:35
	 * @common:
	 */
	@Override
	public Map<String, Object> inquireWeChatOrderByOrdernum(String ordernum, String submchid) {
		try {
			Map<String, String> map = inquireWeChatOrder( ordernum, null, submchid);
			Map<String, Object> resultData = WeChatOrderDateParse(map);
			return resultData;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	
	/**
	 * @method_name: inquireOrderDataForWX 
	 * @Description: 根据查询出的微信订单信息判断并解析
	 * @param traordernum 交易订单号[微信订单号]  submchid：子商户id
	 * @return 微信订单详情
	 * @Author: origin  创建时间:2020年7月20日 下午6:31:35
	 * @common:
	 */
	@Override
	public Map<String, Object> inquireWeChatOrderByTraorder( String traordernum, String submchid) {
		try {
			Map<String, String> map = inquireWeChatOrder( null, traordernum, submchid);
			Map<String, Object> resultData = WeChatOrderDateParse(map);
			return resultData;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	
	
	/**
	 * @method_name: downloadbill
	 * @Description: 
	 * @param time：查询交易订单的时间(对账单日期)   billtype 查询交易订单的账单类型[ALL，返回当日所有订单信息，默认值; SUCCESS，
	 * 		      返回当日成功支付的订单; REFUND，返回当日退款订单; RECHARGE_REFUND，返回当日充值退款订单;] appid：服务商的APPID 
	 *        mch_id：商户号   sub_appid：子商户公众账号ID   sub_mch_id：子商户号
	 * @Author: origin  创建时间:2020年7月21日 下午4:23:57
	 * @common:
	 */
	public String downloadWeChatBillData(String time, Integer billtype, String appid, String mch_id,
			 String sub_appid, String sub_mch_id){
		try {
			String bill_date = time.replace("-", "");
			String bill_type = "ALL";//返回当日所有订单信息（不含充值退款订单）
			billtype = CommUtil.toInteger(billtype);
			if(billtype.equals(1)){//返回当日成功支付的订单（不含充值退款订单）
				bill_type = "SUCCESS";
			}else if(billtype.equals(2)){//返回当日退款订单（不含充值退款订单）
				bill_type = "REFUND";
			}else if(billtype.equals(3)){//返回当日充值退款订单
				bill_type = "RECHARGE_REFUND";
			}
			//-------------------------------------------------
			if(CommUtil.trimToEmpty(appid).equals("")) appid = WeiXinConfigParam.FUWUAPPID;
			if(CommUtil.trimToEmpty(mch_id).equals("")) mch_id = WeiXinConfigParam.MCHID;
			SortedMap<String, String> params = new TreeMap<>();
			params.put("appid", appid);
			params.put("mch_id", mch_id);
			if(!CommUtil.trimToEmpty(sub_appid).equals("")) params.put("sub_appid", sub_appid);
			if(!CommUtil.trimToEmpty(sub_mch_id).equals("")) params.put("sub_mch_id", sub_mch_id);
			params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
			params.put("bill_date", bill_date);
			params.put("bill_type", bill_type);
//			params.put("tar_type", "");
			//------------------------------------------------------
			// 生成签名并放入map集合里
			String sign = HttpRequest.createSign("UTF-8", params);
			params.put("sign", sign);
			String url = WXJSAPI_DOWNLOADBILL;
			String canshu = HttpRequest.getRequestXml(params);
			String result = HttpRequest.sendPost(url, canshu);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @method_name: WeChatBillDateParse
	 * @Description: 微信下载订单解析处理
	 * @param time：查询交易订单的时间(对账单日期)   billtype 查询交易订单的账单类型[ALL，返回当日所有订单信息，默认值; SUCCESS，
	 * 		      返回当日成功支付的订单; REFUND，返回当日退款订单; RECHARGE_REFUND，返回当日充值退款订单;] appid：服务商的APPID 
	 *        mch_id：商户号   sub_appid：子商户公众账号ID   sub_mch_id：子商户号
	 * @Author: origin  创建时间:2020年7月21日 下午5:47:30
	 * @common:
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> WeChatBillDateParse(String time, Integer billtype, String appid, String mch_id,
			 String sub_appid, String sub_mch_id){
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			String result = downloadWeChatBillData(time, billtype, appid, mch_id, sub_appid, sub_mch_id);
			if(result.contains("<xml>")){
				Map<String, String> map = XMLUtil.doXMLParse(result);
				String returncode = CommUtil.trimToEmpty(map.get("return_code"));
				String returnmsg = CommUtil.trimToEmpty(map.get("return_msg")).toUpperCase();
				String errorcode = CommUtil.trimToEmpty(map.get("error_code"));
				String message = null;
				if(errorcode.equals("100") || errorcode.equals("20003")){
					message = "下载失败。系统超时;请尝试再次查询。";
				} else if(errorcode.equals("20001")){
					message = "参数不正确。请求参数未按要求进行填写。";
				} else if(errorcode.equals("20002")){
					message = "账单未生成或不存在。当前商户号没有已成交的订单或对账单尚未生成。";
				} else if(errorcode.equals("20007")){
					message = "当前商户号账单API权限已经关闭。";
				} else if(errorcode.equals("20100")){
					message = "下载失败。系统错误。";
				} else{
					message = "下载失败！未知错误。";
				} 
				resultdata.put("returncode", returncode);
				resultdata.put("returnmsg", returnmsg);
				resultdata.put("errorcode", errorcode);
				resultdata.put("message", message);
			}else{
				String headername = result.substring(0, result.indexOf("`"));//表头数据
				List<String> listheader = Arrays.asList(headername.split(","));
				resultdata.put("listheadername", listheader);
				String tradeMsg = result.substring(result.indexOf("`"));//除表头所有数据
				//获取数据
				String contentInfo = tradeMsg.substring(0, tradeMsg.indexOf("总"));//数据内容
				String[] tradeArray = contentInfo.split("``");//将数据内容  根据``来区分 转换为数组
				List<Map<String, Object>> listcontent = new ArrayList<>();
				List<String> listWeChartOrder = new ArrayList<>();
				for (String tradeDetailInfo : tradeArray) {
					String[] tradeDetailArray = tradeDetailInfo.split(",");
					String WXordernum = CommUtil.toString(tradeDetailArray[6].replace("`", ""));
					listWeChartOrder.add(WXordernum);
					Map<String, Object> mapdata = new HashMap<String, Object>();
					mapdata.put("creattime", CommUtil.toString(tradeDetailArray[0].replace("`", "")));// 交易时间
					mapdata.put("commonId", CommUtil.toString(tradeDetailArray[1].replace("`", "")));// 公众账号ID
					mapdata.put("businessnum", CommUtil.toString(tradeDetailArray[2].replace("`", "")));// 商户号
					mapdata.put("childBusinessnum", CommUtil.toString(tradeDetailArray[3].replace("`", "")));// 子商户号
					mapdata.put("equipmentnum", CommUtil.toString(tradeDetailArray[4].replace("`", "")));// 设备号
					mapdata.put("WxOrderNo", tradeDetailArray[5].replace("`", ""));// 微信订单号
					mapdata.put("BusinessOrderNo", tradeDetailArray[6].replace("`", ""));// 商户订单号
					mapdata.put("UserIdentity", tradeDetailArray[7].replace("`", ""));// 用户标识
					mapdata.put("TransType", tradeDetailArray[8].replace("`", ""));// 交易类型
					mapdata.put("TransStatus", tradeDetailArray[9].replace("`", ""));// 交易状态
					mapdata.put("PaymentBank", tradeDetailArray[10].replace("`", ""));// 付款银行
					mapdata.put("Currency", tradeDetailArray[11].replace("`", ""));// 货币种类
					mapdata.put("TotalAmount", tradeDetailArray[12].replace("`", ""));// 总金额
					mapdata.put("RedEnvelopesAmount", tradeDetailArray[13].replace("`", ""));// 企业红包金额
					mapdata.put("WxRefundNo", tradeDetailArray[14].replace("`", ""));// 微信退款单号
					mapdata.put("BusinessRefundNo", tradeDetailArray[15].replace("`", ""));// 商户退款单号
					mapdata.put("RefundAmount", tradeDetailArray[16].replace("`", ""));// 退款金额
					mapdata.put("RedEnvelopesRefundAmount", tradeDetailArray[17].replace("`", ""));// 企业红包退款金额
					mapdata.put("RefundType", tradeDetailArray[18].replace("`", ""));// 退款类型 	ORIGINAL—原路退款 BALANCE—转退到用户的微信支付零钱 如果该行数据为订单，则留空
					mapdata.put("RefundStatus", tradeDetailArray[19].replace("`", ""));// 退款状态  生成账单文件时该笔退款的状态、后续不会更新，如果该行数据为订单，则留空 
																						//SUCCES—退款成功  FAIL—退款失败  PROCESSING—退款处理中
					mapdata.put("BusinessName", tradeDetailArray[20].replace("`", ""));// 商品名称
					mapdata.put("BusinessData", tradeDetailArray[21].replace("`", ""));// 商户数据包
					mapdata.put("Fee", tradeDetailArray[22].replace("`", ""));// 手续费
					mapdata.put("Rate", tradeDetailArray[23].replace("`", "") + "%");// 费率
					mapdata.put("CreateDate", new Date());
					listcontent.add(mapdata);
				}
				resultdata.put("listcontentData", listcontent);
				String totalinfo = tradeMsg.substring(tradeMsg.indexOf("总"));//汇总数据（汇总表头和内容）
				String totalheader = totalinfo.substring(0, totalinfo.indexOf("`"));//汇总表头名字信息[String]
				List<String> listtotalheader = Arrays.asList(totalheader.split(","));//汇总表头名字信息[JSSON]
				resultdata.put("listtotalheadername", listtotalheader);
				String totalcontent = totalinfo.substring(totalinfo.indexOf("`")).replace("`", "");//汇总数据信息[String]
				List<String> listtotalcontent = Arrays.asList(totalcontent.split(","));//汇总数据信息[JSSON]
				resultdata.put("listtotalcontentData", listtotalcontent);
				resultdata.put("listWeChartOrder", listWeChartOrder);
				CommUtil.responseBuildInfo(200, "SUCCESS", resultdata);
			}
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	
	/**
	 * @method_name: downloadWeChatBill
	 * @Description: 根据参数查询下载微信账单，获取信息判断并解析
	 * @param time：查询交易订单的时间(对账单日期)   billtype 查询交易订单的账单类型[ALL，返回当日所有订单信息，默认值; SUCCESS，
	 * 		      返回当日成功支付的订单; REFUND，返回当日退款订单; RECHARGE_REFUND，返回当日充值退款订单;] appid：服务商的APPID 
	 *        mch_id：商户号   sub_appid：子商户公众账号ID   sub_mch_id：子商户号
	 * @Author: origin  创建时间:2020年7月21日 下午5:49:05
	 * @common:
	 */
	@Override
	public Map<String, Object> downloadWeChatBill(String time, Integer billtype, String appid, String mch_id,
			String sub_appid, String sub_mch_id) {
		Map<String, Object> result = WeChatBillDateParse(time, billtype, appid, mch_id, sub_appid, sub_mch_id);
		return result;
	}

	/**
	 * @method_name: inquireWeChatRefundOrder 
	 * @Description: 根据订单号从微信上查询退款订单信息
	 * @param ordernum 商户订单号   traordernum 交易订单号[微信订单号]  submchid：子商户id
	 * @return 微信订单详情
	 * @Author: origin  创建时间:2020年7月20日 下午6:31:35
	 * @common:
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> inquireWeChatRefundOrder( String submchid, String ordernum, 
			String traordernum, String refundid, String outrefundno, String offset){
		try {
			if(CommUtil.trimToEmpty(submchid).equals("")) submchid = WeiXinConfigParam.SUBMCHID;
			SortedMap<String, String> params = new TreeMap<>();
			params.put("appid", WeiXinConfigParam.FUWUAPPID);
			params.put("mch_id", WeiXinConfigParam.MCHID);
//			params.put("sub_appid", "");
			params.put("sub_mch_id", submchid);
			
			//同时存在优先级为： refund_id > out_refund_no > transaction_id > out_trade_no
			if(!CommUtil.trimToEmpty(refundid).equals(""))params.put("refund_id", refundid);
			if(!CommUtil.trimToEmpty(outrefundno).equals(""))params.put("out_refund_no", outrefundno);
			if(!CommUtil.trimToEmpty(traordernum).equals(""))params.put("transaction_id", traordernum);
			if(!CommUtil.trimToEmpty(ordernum).equals(""))params.put("out_trade_no", ordernum);
			if(!CommUtil.trimToEmpty(offset).equals(""))params.put("offset", offset);
			params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
			//------------------------------------------------------
			// 生成签名并放入map集合里
			String sign = HttpRequest.createSign("UTF-8", params);
			params.put("sign", sign);
			String url = WXJSAPI_REFUNDRQUERY;
			String canshu = HttpRequest.getRequestXml(params);
			String result = HttpRequest.sendPost(url, canshu);
			Map<String, String> map = XMLUtil.doXMLParse(result);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @method_name: inquireWeChatOrderDate 
	 * @Description: 根据查询出的微信订单信息判断并解析
	 * @param ordernum 商户订单号   traordernum 交易订单号[微信订单号]  submchid：子商户id
	 * @return 微信订单详情
	 * @Author: origin  创建时间:2020年7月20日 下午6:31:35
	 * @common:
	 */
	public Map<String, Object> WeChatRefundOrderDateParse(Map<String, String> map){
		Map<String, Object> resultdate = new HashMap<String, Object>();
		try {
//			Map<String, String> map = inquireWeChatRefundOrder( submchid, ordernum, traordernum, refundid, outrefundno, offset);
			String returncode = CommUtil.trimToEmpty(map.get("return_code"));
			resultdate.put("WeChatOrderDate", map);
			Map<String, Object> mapdate = new HashMap<String, Object>();
			if(returncode.contains("SUCCESS")){
				mapdate.put("服务商的APPID", map.get("appid"));
				mapdate.put("商户号", map.get("mch_id"));
				mapdate.put("子商户号", map.get("sub_mch_id"));
				mapdate.put("微信订单号", map.get("transaction_id"));
				mapdate.put("商户订单号", map.get("out_trade_no"));
				mapdate.put("随机字符串", map.get("nonce_str"));
				mapdate.put("签名", map.get("sign"));
				mapdate.put("返回信息", map.get("return_msg"));
				mapdate.put("业务结果", map.get("result_code"));
				mapdate.put("返回状态码", map.get("return_code"));
				
				mapdate.put("订单金额", map.get("total_fee"));
				mapdate.put("现金支付金额", map.get("cash_fee"));
				mapdate.put("退款笔数", map.get("refund_count"));
				mapdate.put("退款金额", map.get("refund_fee"));
				Integer refundcount = CommUtil.toInteger(map.get("refund_count"));
				for(int i = 0; i< refundcount; i++){
					String timenum = CommUtil.toString(i +1);
					String name = "第 "+timenum+" 笔";
					String refundfee = "refund_fee_"+i;
					String buoutrefundno = "out_refund_no_"+i;
					String wxrefundid = "refund_id_"+i;
					String refundstatus = "refund_status_"+i;
					String refundaccount = "refund_account_"+i;
					String refundchannel = "refund_channel_"+i;
					String refundsuccesstime = "refund_success_time_"+i;
					String refundrecvaccout = "refund_recv_accout_"+i;
					
					mapdate.put(name +"申请退款金额", map.get(refundfee));
					mapdate.put(name +"商户退款单号", map.get(buoutrefundno));
					mapdate.put(name +"微信退款单号", map.get(wxrefundid));
					mapdate.put(name +"退款状态", map.get(refundstatus));
					mapdate.put(name +"退款资金来源", map.get(refundaccount));
					mapdate.put(name +"退款渠道", map.get(refundchannel));
					mapdate.put(name +"退款成功时间", map.get(refundsuccesstime));
					mapdate.put(name +"退款入账账户", map.get(refundrecvaccout));
				}
			}else{
				mapdate.put("返回信息", map.get("return_msg"));
				mapdate.put("业务结果", map.get("result_code"));
				mapdate.put("返回状态码", map.get("return_code"));
			}
			resultdate.put("OrderDateParse", mapdate);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(3000, "订单信息查询异常", resultdate);
		}
		return resultdate;
	}
	
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
	@Override
	public Map<String, Object> inquireWXRefOrderData(String submchid, String ordernum, String traordernum,
			String refundid, String outrefundno, String offset) {
		try {
			Map<String, String> map = inquireWeChatRefundOrder( submchid, ordernum, traordernum, refundid, outrefundno, offset);
			Map<String, Object> resultData = WeChatRefundOrderDateParse(map);
			return resultData;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	
	/**
	 * @method_name: inquireWXRefOrderByOrdernum
	 * @Description: 根据商户订单号查询微信退款订单信息
	 * @param submchid： 子商户id
	 * @param ordernum：商户订单号
	 * @param offset：偏移量 [偏移量，当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录]
	 * @Author: origin  创建时间:2020年7月22日 下午2:41:28
	 * @common:
	 */
	@Override
	public Map<String, Object> inquireWXRefOrderByOrdernum(String submchid, String ordernum, String offset) {
		try {
			Map<String, String> map = inquireWeChatRefundOrder( submchid, ordernum, null, null, null, offset);
			Map<String, Object> resultData = WeChatRefundOrderDateParse(map);
			return resultData;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	
	/**
	 * @method_name: inquireWXRefOrderByTraorder
	 * @Description: 根据微信订单号查询微信退款订单信息
	 * @param submchid： 子商户id
	 * @param traordernum：交易订单号【微信订单号】
	 * @param offset：偏移量 [偏移量，当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录]
	 * @Author: origin  创建时间:2020年7月22日 下午2:44:26
	 * @common:
	 */
	@Override
	public Map<String, Object> inquireWXRefOrderByTraorder(String submchid, String traordernum, String offset) {
		try {
			Map<String, String> map = inquireWeChatRefundOrder( submchid, null, traordernum, null, null, offset);
			Map<String, Object> resultData = WeChatRefundOrderDateParse(map);
			return resultData;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	
	/**
	 * @method_name: inquireWXRefOrderByOutref
	 * @Description: 根据商户订单号查询微信退款订单信息
	 * @param submchid： 子商户id
	 * @param outrefundno：商户退款单号
	 * @param offset：偏移量 [偏移量，当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录]
	 * @Author: origin  创建时间:2020年7月22日 下午2:45:51
	 * @common:
	 */
	@Override
	public Map<String, Object> inquireWXRefOrderByOutref(String submchid, String outrefundno, String offset) {
		try {
			Map<String, String> map = inquireWeChatRefundOrder( submchid, null, null, null, outrefundno, offset);
			Map<String, Object> resultData = WeChatRefundOrderDateParse(map);
			return resultData;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	
	/**
	 * @method_name: inquireWXRefOrderByRefundid
	 * @Description: 根据商户订单号查询微信退款订单信息
	 * @param submchid： 子商户id
	 * @param refundid：微信退款单号
	 * @param offset：偏移量 [偏移量，当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录]
	 * @Author: origin  创建时间:2020年7月22日 下午2:47:05
	 * @common:
	 */
	@Override
	public Map<String, Object> inquireWXRefOrderByRefundid(String submchid, String refundid, String offset) {
		try {
			Map<String, String> map = inquireWeChatRefundOrder( submchid, null, null, refundid, null, offset);
			Map<String, Object> resultData = WeChatRefundOrderDateParse(map);
			return resultData;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	

	@Override
	public Map<String, Object> WXPortionPayRefund(String ordernum, String submchid, String refundfee){
		try {
			Map<String, Object> resultData = WeChatPayRefund( ordernum, submchid, null, refundfee);
			return resultData;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	

	@Override
	public Map<String, Object> WXPayRefund(String ordernum, String submchid){
		try {
			Map<String, Object> resultData = WeChatPayRefund( ordernum, submchid, null, null);
			return resultData;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}
	
	public Map<String, Object> WeChatPayRefund(String ordernum, String submchid, String totalfee, String refundfee){
		Map<String, Object> datamap = new HashMap<>();
		try {
			WXPayConfigImpl config = WXPayConfigImpl.getInstance();
			WXPay wxpay = new WXPay(config);
			// 根据id查询充电信息
			String out_trade_no = CommUtil.trimToEmpty(ordernum);
			String total_fee = CommUtil.trimToEmpty(totalfee);
			String refund_fee = CommUtil.trimToEmpty(refundfee);
			if(CommUtil.trimToEmpty(submchid).equals("")) submchid = WeiXinConfigParam.SUBMCHID;
			//------------------------------------------------------
			Map<String, String> resultMap = inquireWeChatOrder( ordernum, null, submchid);
			datamap.put("resultMap", resultMap);
			
			if(total_fee.equals("")) total_fee = CommUtil.toString(resultMap.get("total_fee"));
			if(refund_fee.equals("")) refund_fee = CommUtil.toString(resultMap.get("total_fee"));
//			if(total_fee>refund_fee){
//				CommUtil.responseBuildInfo(3003, "ERROR,退款金额不能大于该笔订单金额", datamap);
//			}
			// 退款后的业务处理	contains
			String returncode = CommUtil.trimToEmpty(resultMap.get("return_code"));
			String tradestate = CommUtil.trimToEmpty(resultMap.get("trade_state"));
			if (returncode.equals("SUCCESS")) {//表示该订单存在
				if(tradestate.equals("SUCCESS") || tradestate.equals("REFUND")){//支付成功或部分退款
					Integer refundcount = CommUtil.toInteger(resultMap.get("refund_count"));
					String outtradeno = out_trade_no = "t"+refundcount+"_" + out_trade_no;
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("appid", WeiXinConfigParam.FUWUAPPID);
					data.put("mch_id", WeiXinConfigParam.MCHID);
					data.put("sub_mch_id", submchid);
					data.put("transaction_id", resultMap.get("transaction_id"));
					data.put("out_trade_no", out_trade_no);// 定单号
					data.put("out_refund_no", outtradeno);//商户退款单号
					data.put("total_fee", total_fee);
					data.put("refund_fee", refund_fee);
					data.put("refund_fee_type", "CNY");
					data.put("op_user_id", config.getMchID());
					Map<String, String> r = wxpay.refund(data);
					datamap.put("resultrefMap", r);
					// 处理退款后的订单 成功
					if ("SUCCESS".equals(r.get("result_code"))) {
						String outrefundno = CommUtil.toString(r.get("out_refund_no"));
						String wxrefundno = CommUtil.toString(r.get("refund_id"));
						String refundfeemoney = CommUtil.toString(r.get("refund_fee"));
						CommUtil.responseBuildInfo(200, "SUCCESS", datamap);
					} else if ("FAIL".equals(r.get("result_code"))) {
						CommUtil.responseBuildInfo(3001, "ERROR,退款失败", datamap);
					}
				}else if(tradestate.equals("NOTPAY") || tradestate.equals("CLOSED") || tradestate.equals("REVOKED")){
					CommUtil.responseBuildInfo(3003, "ERROR,该订单未支付或已撤销，不能退费。", datamap);
				}else if(tradestate.equals("USERPAYING") || tradestate.equals("PAYERROR")){
					CommUtil.responseBuildInfo(3003, "ERROR,该订单支付失败或正在支付中，不能退费。", datamap);
				}else{
					CommUtil.responseBuildInfo(3003, "ERROR,退费失败。", datamap);
				}
			}else{
				CommUtil.responseBuildInfo(3003, "ERROR,未查到该订单，无法退费。", datamap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(3003, "ERROR,服务器发生错误", datamap);
		}
		return datamap;
	}
	
	
	
	/*

			mapinfo.put("news", news);
			StringBuffer hint = new StringBuffer(returncode);
			hint.append(";").append(errorcode).append(";").append(returnmsg).append(";");
			String hintinfo = null;
			if(returnmsg.equals(("SYSTEMERROR").toUpperCase())){
				hintinfo = "下载失败;系统超时;请尝试再次查询。";
			}else if(returnmsg.equals(("sign error").toUpperCase())){
				hintinfo = "签名错误;请求参数未按要求进行填写;签名错误，请重新检查参数和签名密钥是否正确";
			}else if(returnmsg.equals(("nonce_str too long").toUpperCase())){
				hintinfo = "参数nonce_str错误;请求参数未按要求填写;参数nonce_str长度超长";
			}else if(returnmsg.equals(("invalid tar_type, Only GZIP supported").toUpperCase())){
				hintinfo = "参数tar_type错误;请求参数未按指引进行填写;请重新检查参数invalid tar_typ是否正确";
			}else if(returnmsg.equals(("invalid bill_type").toUpperCase())){
				hintinfo = "参数bill_type错误;请求参数未按指引进行填写;请重新检查参数bill_type是否正确";
			}else if(returnmsg.equals(("invalid bill_date").toUpperCase())){
				hintinfo = "参数bill_date错误;请求参数未按指引进行填写;请重新检查参数bill_date是否符合要求";
			}else if(returnmsg.equals(("require POST method").toUpperCase())){
				hintinfo = "请求方式错误;请求方式不符合要求;请求检查参数请求方式是否为post";
			}else if(returnmsg.equals(("empty post data").toUpperCase())){
				hintinfo = "请求报文错误;请求报文为空;请重新检查请求报文是否正确";
			}else if(returnmsg.equals(("data format error").toUpperCase())){
				hintinfo = "参数格式错误;请求参数要求为xml格式;请重新检查请求参数格式是否为xml";
			}else if(returnmsg.equals(("missing parameter").toUpperCase())){
				hintinfo = "缺少参数;有必传的参数未上传;请重新检查是否所有必传参数都上传了，且不为空";
			}else if(returnmsg.equals(("invalid appid").toUpperCase())){
				hintinfo = "appid错误;请求参数appid有误;请重新检查参数appid是否正确";
			}else if(returnmsg.equals(("invalid parameter").toUpperCase())){
				hintinfo = "参数错误;有未知的请求参数;请重新检查是否所有参数都与文档相符";
			}else if(returnmsg.equals(("No Bill Exist").toUpperCase())){
				hintinfo = "账单不存在;当前商户号没有已成交的订单，不生成对账单;请检查当前商户号在指定日期内是否有成功的交易。";
			}else if(returnmsg.equals(("Bill Creating").toUpperCase())){
				hintinfo = "账单未生成;当前商户号没有已成交的订单或对账单尚未生成;请先检查当前商户号在指定日期内是否有成功的交易，如指定日期有交易则表示账单正在生成中，请在上午10点以后再下载。";
			}else if(returnmsg.equals("当前商户号账单API权限已经关闭")){
				hintinfo = "当前商户号账单API权限已经关闭;当前商户号账单API权限已经关闭;当前商户号账单API权限已经关闭，请联系微信支付解决";
			}else if(returnmsg.equals(("system error").toUpperCase())){
				hintinfo = "下载失败;系统超时;请尝试再次查询。";
			}else{
				hintinfo = "下载失败;未知原因。";
			}
			hint.append(hintinfo);
			mapinfo.put("hint", hint);
			String message = errorcode + hintinfo;
			mapinfo.put("message", message);
			CommUtil.responseBuildInfo(101, message, mapinfo);
		}else{
			String headername = result.substring(0, result.indexOf("`"));//表头数据
			List<String> listheader = Arrays.asList(headername.split(","));
			mapinfo.put("listheader", listheader);
			String tradeMsg = result.substring(result.indexOf("`"));//除表头所有数据
			//获取数据
			String contentInfo = tradeMsg.substring(0, tradeMsg.indexOf("总"));//数据内容
			String[] tradeArray = contentInfo.split("``");//将数据内容  根据``来区分 转换为数组
			List<Map<String, Object>> listcontent = new ArrayList<>();
			List<String> listWeChartOrder = new ArrayList<>();
			for (String tradeDetailInfo : tradeArray) {
				String[] tradeDetailArray = tradeDetailInfo.split(",");
				String WXordernum = CommUtil.toString(tradeDetailArray[6].replace("`", ""));
				listWeChartOrder.add(WXordernum);
				Map<String, Object> mapdata = new HashMap<String, Object>();
				mapdata.put("creattime", CommUtil.toString(tradeDetailArray[0].replace("`", "")));// 交易时间
				mapdata.put("commonId", CommUtil.toString(tradeDetailArray[1].replace("`", "")));// 公众账号ID
				mapdata.put("businessnum", CommUtil.toString(tradeDetailArray[2].replace("`", "")));// 商户号
				mapdata.put("childBusinessnum", CommUtil.toString(tradeDetailArray[3].replace("`", "")));// 子商户号
				mapdata.put("equipmentnum", CommUtil.toString(tradeDetailArray[4].replace("`", "")));// 设备号
				mapdata.put("WxOrderNo", tradeDetailArray[5].replace("`", ""));// 微信订单号
				mapdata.put("BusinessOrderNo", tradeDetailArray[6].replace("`", ""));// 商户订单号
				mapdata.put("UserIdentity", tradeDetailArray[7].replace("`", ""));// 用户标识
				mapdata.put("TransType", tradeDetailArray[8].replace("`", ""));// 交易类型
				mapdata.put("TransStatus", tradeDetailArray[9].replace("`", ""));// 交易状态
				mapdata.put("PaymentBank", tradeDetailArray[10].replace("`", ""));// 付款银行
				mapdata.put("Currency", tradeDetailArray[11].replace("`", ""));// 货币种类
				mapdata.put("TotalAmount", tradeDetailArray[12].replace("`", ""));// 总金额
				mapdata.put("RedEnvelopesAmount", tradeDetailArray[13].replace("`", ""));// 企业红包金额
				mapdata.put("WxRefundNo", tradeDetailArray[14].replace("`", ""));// 微信退款单号
				mapdata.put("BusinessRefundNo", tradeDetailArray[15].replace("`", ""));// 商户退款单号
				mapdata.put("RefundAmount", tradeDetailArray[16].replace("`", ""));// 退款金额
				mapdata.put("RedEnvelopesRefundAmount", tradeDetailArray[17].replace("`", ""));// 企业红包退款金额
				mapdata.put("RefundType", tradeDetailArray[18].replace("`", ""));// 退款类型
				mapdata.put("RefundStatus", tradeDetailArray[19].replace("`", ""));// 退款状态
				mapdata.put("BusinessName", tradeDetailArray[20].replace("`", ""));// 商品名称
				mapdata.put("BusinessData", tradeDetailArray[21].replace("`", ""));// 商户数据包
				mapdata.put("Fee", tradeDetailArray[22].replace("`", ""));// 手续费
				mapdata.put("Rate", tradeDetailArray[23].replace("`", "") + "%");// 费率
				mapdata.put("CreateDate", new Date());
				listcontent.add(mapdata);
			}
			mapinfo.put("listcontent", listcontent);
			mapinfo.put("listWeChartOrder", listWeChartOrder);
			
			List<String> lostWxOrder = new ArrayList<>();
			
			lostWxOrder.addAll(listWeChartOrder); 
			List<Map<String, Object>> lostcontent = new ArrayList<>();
			for(int i=0; i< lostWxOrder.size(); i++){
				String lowxorder = lostWxOrder.get(i);
				for(int j=0; j< listcontent.size(); j++){
					String BusinessName = CommUtil.toString(listcontent.get(j).get("BusinessName"));
					String wxorder = CommUtil.toString(listcontent.get(j).get("BusinessOrderNo"));
					if(lowxorder.equals(wxorder) && !BusinessName.contains("微信设备缴费")) lostcontent.add(listcontent.get(j));
				}
			}
			CommUtil.responseBuildInfo(200, "SUCCESS", mapinfo);
		}
		return mapinfo;
	}	
*/
	
}
