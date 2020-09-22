package com.hedong.hedongwx.config;

import java.io.IOException;
import java.net.URLEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.hedong.hedongwx.entity.wx.AccessToken;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.WeixinUtil;

import net.sf.json.JSONObject;


public class WeChatOpenPlatform {


	
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
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	/*** *** *** 获取方法  *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	
	//获取用户信息    getHedUserinfo
	public static JSONObject getOpenUserinfo(String code) throws ParseException, IOException{
		JSONObject accessToken = getOpenAccessToken(code);
		String openToken = accessToken.getString("access_token");
		String openid = accessToken.getString("openid");
		JSONObject openUserInfo = getOpenUserinfo(openToken, openid);
		return openUserInfo;
	}
	
	//验证token是否有效并获取
	public static String getToOpenToken(String code) throws ParseException, IOException{
		String opentoken = null;
		String nowTime = StringUtil.toDateTime();//获取当前时间
		String expiresTime = AccessToken.getOpenExpiresOut();
		int num = StringUtil.compareStringTime(nowTime, expiresTime);
		if(num==-1){
			opentoken = AccessToken.getOpenToken();
		}else{
			opentoken = getOpenToken(code);
		}
		return opentoken;
	}
	
	//获取code
	public static JSONObject getOpenAutoCode(String url){
		String str = CONNECT_CODE;
		String urls;
		JSONObject code = null;
		try {
			urls = URLEncoder.encode(url, "utf-8");
			str = str.replace("APPID", CommonConfig.OPEN_APPID).replace("REDIRECT_URI", urls);	
			code = WeixinUtil.doGetStr(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}
	
	//通过code获取access_token 并存储在实体类 
	//返回参数：access_token:接口调用凭证; expires_in:时间; refresh_token: 用户刷新access_token; 
	//openid:授权用户唯一标识; scope:用户授权的作用域，使用逗号（,）分隔
	public static String getOpenToken(String code) throws ParseException, IOException{
		String str = CONNECT_ACCESS_TOKEN;
		str = str.replace("APPID", CommonConfig.OPEN_APPID).replace("SECRET", CommonConfig.OPEN_APPSECRET).replace("CODE", code);
		JSONObject acctoken = WeixinUtil.doGetStr(str);
		String opentoken = null;
		if(acctoken!=null){
			opentoken = acctoken.getString("access_token");
			AccessToken.setOpenToken(opentoken);
			AccessToken.setOpenRefreshToken(acctoken.getString("refresh_token"));
			AccessToken.setOpenExpiresIn(acctoken.getString("expires_in"));
			long TimeInMillis = System.currentTimeMillis();
			String expiresTime = StringUtil.toDateTime(TimeInMillis+(6900*1000));
			AccessToken.setOpenExpiresOut(expiresTime);
		}
		return opentoken;
	}
	
	//通过code获取access_token 并存储在实体类 
	//返回参数：access_token:接口调用凭证; expires_in:时间; refresh_token: 用户刷新access_token; 
	//openid:授权用户唯一标识; scope:用户授权的作用域，使用逗号（,）分隔
	public static JSONObject getOpenAccessToken(String code) throws ParseException, IOException{
		String str = CONNECT_ACCESS_TOKEN;
		str = str.replace("APPID", CommonConfig.OPEN_APPID).replace("SECRET", CommonConfig.OPEN_APPSECRET).replace("CODE", code);
		JSONObject acctoken = WeixinUtil.doGetStr(str);
		String opentoken = null;
		if(acctoken!=null){
			opentoken = acctoken.getString("access_token");
			AccessToken.setOpenToken(opentoken);
			AccessToken.setOpenRefreshToken(acctoken.getString("refresh_token"));
			AccessToken.setOpenExpiresIn(acctoken.getString("expires_in"));
			long TimeInMillis = System.currentTimeMillis();
			String expiresTime = StringUtil.toDateTime(TimeInMillis+(6900*1000));
			AccessToken.setOpenExpiresOut(expiresTime);
		}
		return acctoken;
	}
	
	//检验授权凭证（access_token）是否有效
	//返回参数：errcode:0,errmsg:ok  
	public static boolean getOpenAuth(String accesstoken, String openid) throws ParseException, IOException{
		boolean fals = false;
		String str = CONNECT_AUTH_TOKEN;
		str = str.replace("ACCESS_TOKEN", accesstoken).replace("OPENID", openid);
		JSONObject openAuth = WeixinUtil.doGetStr(str);
		if(openAuth!=null){
			//String errcode = openAuth.getString("errcode");
			String errmsg = openAuth.getString("errmsg");
			if(errmsg.equals("ok")) fals = true;
		}
		return fals;
	}
	
	//刷新或续期access_token（refresh_token）并存储入实体类
	//返回参数：access_token:接口调用凭证; expires_in:时间; refresh_token: 用户刷新access_token; 
	//openid:授权用户唯一标识; scope:用户授权的作用域，使用逗号（,）分隔
	public static JSONObject getOpenRefreshToken(String refreshToken) throws ParseException, IOException{
		String str = CONNECT_REFRESH_TOKEN;
		str = str.replace("APPID", CommonConfig.OPEN_APPID).replace("REFRESH_TOKEN", refreshToken);
		JSONObject acctoken = WeixinUtil.doGetStr(str);
		String opentoken = null;
		if(acctoken!=null){
			opentoken = acctoken.getString("access_token");
			AccessToken.setOpenToken(opentoken);
			AccessToken.setOpenRefreshToken(acctoken.getString("refresh_token"));
			AccessToken.setOpenExpiresIn(acctoken.getString("expires_in"));
			long TimeInMillis = System.currentTimeMillis();
			String expiresTime = StringUtil.toDateTime(TimeInMillis+(7000*1000));
			AccessToken.setOpenExpiresOut(expiresTime);
		}
		return acctoken;
	}
	
	//获取用户个人信息（UnionID机制） userinfo  
	//返回参数：unionid:用户统一标识、openid、nickname、sex、province、city、country、headimgurl:用户头像、privilege:用户特权信息
	public static JSONObject getOpenUserinfo(String accesstoken, String openid) throws ParseException, IOException{
		String str = CONNECT_USERINFO;
		str = str.replace("ACCESS_TOKEN", accesstoken).replace("OPENID", openid);
		JSONObject acctoken = WeixinUtil.doGetStr(str);
		return acctoken;
	}
	
	/*** *** *** get和post执行方法 *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	
	/**
	 * get请求
	 * @param url
	 */
	public static JSONObject doGetStr(String url) throws ParseException, IOException{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		CloseableHttpResponse httpResponse = client.execute(httpGet);
		HttpEntity entity = httpResponse.getEntity();
		if(entity != null){
			String result = EntityUtils.toString(entity,"UTF-8");
			jsonObject = JSONObject.fromObject(result);
		}
		return jsonObject;
	}
	
	/**
	 * POST请求
	 * @param url
	 * @param outStr
	 */
	public static JSONObject doPostStr(String url,String outStr) throws ParseException, IOException{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
		CloseableHttpResponse response = client.execute(httpPost);
		String result = EntityUtils.toString(response.getEntity(),"UTF-8");
		jsonObject = JSONObject.fromObject(result);
		return jsonObject;
	}
	/*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
	
	
	
	
	
	
	
	
}
