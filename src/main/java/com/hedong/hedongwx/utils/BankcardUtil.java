package com.hedong.hedongwx.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
*author：RoarWolf
*createtime：2020年11月23日
*/

public class BankcardUtil {
	
	public static final String host = "http://ali-bankcard.showapi.com";
	public static final String path = "/bankcard";
	public static final String appcode = "5ee0c4c145474f628a974b0d068690d5";

	public static void main(String[] args) {
		checkBankcardInfo("6215982582010042122");
	}
	
	public static Map<String, Object> checkBankcardInfo(String bankcardnum) {
		Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    Map<String, String> querys = new HashMap<String, String>();
	    querys.put("kahao", bankcardnum);


	    try {
	    	/**
	    	* 重要提示如下:
	    	* HttpUtils请从
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* 下载
	    	*
	    	* 相应的依赖请参照
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	String sendGet = HttpUtils.sendGet(host, path, headers, querys);
	    	System.out.println(sendGet.toString());
	    	Map<String, Object> parse = (Map<String, Object>) JSON.parse(sendGet);
	    	JSONObject showapi_res_body = (JSONObject) parse.get("showapi_res_body");
	    	String ret_code = showapi_res_body.getString("ret_code");
	    	if ("-1".equals(ret_code)) {
	    		return CommUtil.responseBuildInfo(1001, showapi_res_body.getString("remark"), parse);
	    	} else {
	    		return CommUtil.responseBuildInfo(1000, "获取成功", parse);
	    	}
	    	//获取response的body
	    	//System.out.println(EntityUtils.toString(response.getEntity()));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return CommUtil.responseBuildInfo(1002, "系统异常", null);
	    }
	}
}
