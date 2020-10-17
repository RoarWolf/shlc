package com.hedong.hedongwx.utils;

/**
 * 微信回复信息msg
 * @author Administrator
 *
 */
public class MessageUtil {
	
	/**
	 * 总菜单
	 * @return
	 */
	public static String initTextMessage() {
		StringBuffer sb = new StringBuffer();
		sb.append("回复【续充】查看续充说明\n");
		sb.append("回复【退款】查看如何退款");
		/*sb.append("回复‘1’或‘续充’:查看续充说明\n");
		sb.append("回复‘2’或‘客服’:获取客服微信二维码\n");*/
		return sb.toString();
	}
	
	/**
	 * 续充说明
	 * @return
	 */
	public static String fristTextMessage() {
		StringBuffer sb = new StringBuffer();
		sb.append("续充说明:\n\n");
		sb.append("1.进入公众号【自助充电平台】\n");
		sb.append("2.点击下方菜单【正在充电】\n");
		sb.append("3.会出现自己正在充电的信息以及以往的充电记录\n");
		sb.append("4.点击续充，选择充电时间，点击立即支付则续充成功\n");
		sb.append("5.成功后会跳回正在充电信息页面，可以点击更新查看实时充电信息");
		return sb.toString();
	}
	
	/**
	 * 退款说明
	 * @return
	 */
	public static String refundTextMessage() {
		StringBuffer sb = new StringBuffer();
		sb.append("退款说明:\n\n");
		sb.append("把充电支付成功截图、充电站图片、以及充电插座图片和充电器图片发送给此公众号，退款会在1--3个工作日原路退回。");
		return sb.toString();
	}
	
}
