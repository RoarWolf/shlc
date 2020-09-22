package com.hedong.hedongwx.config;

/**
 * @Description: 
 * @Author: origin  创建时间:2020年8月27日 下午5:47:02
 * @common:   
 */
public class WeChatConfig {
	
	/*** *** *** 微信公众平台支付URL  *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	/** 统一下单 */
	public static String WXJSAPI_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	/**  查询订单 */
	public static String WXJSAPI_ORDERQUERY = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	/**  关闭订单 */
	public static String WXJSAPI_CLOSEORDER = "https://api.mch.weixin.qq.com/pay/closeorder";

	/**  申请退款 */
	public static String WXJSAPI_REFUNDR = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	
	/**  查询退款 */
	public static String WXJSAPI_REFUNDRQUERY = "https://api.mch.weixin.qq.com/pay/refundquery";

	/**  下载交易账单 */
	public static String WXJSAPI_DOWNLOADBILL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	
	/**  支付结果通用通知 */
	public static String WXJSAPI_RESULT = "https://api.mch.weixin.qq.com/pay/downloadbill";
	
	/**  交易保障 */
	public static String WXJSAPI_REPORT = "https://api.mch.weixin.qq.com/payitil/report";
	
	/**  退款结果通知 */
	public static String WXJSAPI_REFUNDRE = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	
//===========================================================================================================	
	/*** *** *** 微信开放平台URL  *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	//请求code(第三方使用网站应用授权登录前请注意已获取相应网页授权作用域（scope=snsapi_login），则可以通过在PC端打开链接)
	public static String CONNECT_CODE = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";
	
	//通过code获取access_token
	public static String CONNECT_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
										                                              
	//刷新或续期access_token使用
	public static String CONNECT_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	
	//检验授权凭证（access_token）是否有效
	public static String CONNECT_AUTH_TOKEN = "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";
	
	//获取用户个人信息（UnionID机制） userinfo
	public static String CONNECT_USERINFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
	
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	
	
}
