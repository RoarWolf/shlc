package com.hedong.hedongwx.utils;

import java.io.IOException;

import org.json.JSONException;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

public class MobileSendUtil {
	// 短信应用SDK AppID
	public static int appid = 1400115371;
	// 短信应用SDK AppKey
	public static String appkey = "f56eb33ffe90a277cf3c0aa48783c2b5";
    // 需要发送短信的手机号码
//	public static String[] phoneNumbers = {"13290906458"};
    // 短信模板ID
//    public static int templateId = 161717;
    public static int templateId = 195638;
    // 签名
    // 真实的签名需要在短信控制台中申请，另外
    // 签名参数使用的是`签名内容`，而不是`签名ID`
//    public static String smsSign = "和动充电";
    public static String smsSign = "自助充电平台";


    // 指定模板ID单发短信
    public static SmsMultiSenderResult TemplateMobileSend(String[] params, String[] mobilephon){
    	SmsMultiSenderResult result =  null;
    	try {
            SmsMultiSender msender = new SmsMultiSender(appid, appkey);
            result =  msender.sendWithParam("86", mobilephon, templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            //System.out.println("返回值：   "+result);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
		return result;
    }
    
    
    
    
    
}
	
