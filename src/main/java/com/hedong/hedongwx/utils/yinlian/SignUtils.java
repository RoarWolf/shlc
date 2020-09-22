/**
 * Project Name:payment
 * File Name:SignUtils.java
 * Package Name:cn.swiftpass.utils.payment.sign
 * Date:2014-6-27下午3:22:33
 *
*/

package com.hedong.hedongwx.utils.yinlian;

import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.hedong.hedongwx.utils.WeiXinConfigParam;



/**
 * ClassName:SignUtils
 * Function: 签名用的工具箱
 * Date:     2014-6-27 下午3:22:33 
 * @author    
 */
public class SignUtils {
	private static final Logger logger = LoggerFactory.getLogger(SignUtils.class);
    /** <一句话功能简述>
     * <功能详细描述>验证返回参数
     * @param params
     * @param key
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkParam(Map<String,String> params,String key){
        boolean result = false;
        if(params.containsKey("sign")){
            String sign = params.get("sign");
            params.remove("sign");
            StringBuilder buf = new StringBuilder((params.size() +1) * 10);
            SignUtils.buildPayParams(buf,params,false);
            String preStr = buf.toString();
            String signRecieve = MD5.sign(preStr, "&key=" + key, "utf-8");
            result = sign.equalsIgnoreCase(signRecieve);
        }
        return result;
    }
    
    /**
     * 过滤参数
     * @author  
     * @param sArray
     * @return
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>(sArray.size());
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }
    
    /** <一句话功能简述>
     * <功能详细描述>将map转成String
     * @param payParams
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String payParamsToString(Map<String, String> payParams){
        return payParamsToString(payParams,false);
    }
    
    public static String payParamsToString(Map<String, String> payParams,boolean encoding){
        return payParamsToString(new StringBuilder(),payParams,encoding);
    }
    /**
     * @author 
     * @param payParams
     * @return
     */
    public static String payParamsToString(StringBuilder sb,Map<String, String> payParams,boolean encoding){
        buildPayParams(sb,payParams,encoding);
        return sb.toString();
    }
    
    /**
     * @author 
     * @param payParams
     * @return
     */
    public static void buildPayParams(StringBuilder sb,Map<String, String> payParams,boolean encoding){
        List<String> keys = new ArrayList<String>(payParams.keySet());
        Collections.sort(keys);
        for(String key : keys){
            sb.append(key).append("=");
            if(encoding){
                sb.append(urlEncode(payParams.get(key)));
            }else{
                sb.append(payParams.get(key));
            }
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
    }
    
    public static String urlEncode(String str){
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Throwable e) {
            return str;
        } 
    }
    
    
    public static Element readerXml(String body,String encode) throws DocumentException {
        SAXReader reader = new SAXReader(false);
        InputSource source = new InputSource(new StringReader(body));
        source.setEncoding(encode);
        Document doc = reader.read(source);
        Element element = doc.getRootElement();
        return element;
    }
    
    public static  Map<String, String> sendPostToUnionpay(SortedMap<String, String> date){
		Map<String, String> param = SignUtils.paraFilter(date);
		StringBuilder buf = new StringBuilder((param.size() + 1) * 10);
		SignUtils.buildPayParams(buf, param, false);
	    String preStr = buf.toString();
	    String sign = MD5.sign(preStr,"&key="+WeiXinConfigParam.UNIONKEY, "utf-8");
	    date.put("sign", sign);//gateway.test.95516.com/gateway/api/order.do
		String url = "https://qra.95516.com/pay/gateway";
		String res = null;
	    CloseableHttpClient client =  null;
	    CloseableHttpResponse response = null;
	    Map<String, String> resultMap = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			StringEntity entityParams = new StringEntity(XmlUtils.parseXML(date), "utf-8");
			httpPost.setEntity(entityParams);
	        httpPost.setHeader("Content-Type", "text/xml;utf-8");
	        client = HttpClients.createDefault();
	        client.execute(httpPost);
	        response = client.execute(httpPost);
	        if (response != null && response.getEntity() != null) {
	        	resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
	        	res  = XmlUtils.toXml(resultMap);
	        	System.out.println("响应报文=="+res);
	        	if (!SignUtils.checkParam(resultMap, WeiXinConfigParam.UNIONKEY)) {
	            	  res = "验证签名不通过";
		              logger.info("验证签名不通过");
	            } else {
	                if ("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))) {
	                    String pay_info = resultMap.get("pay_info");
	                    logger.info("pay_info : " + pay_info);
	                    res = "ok";
	                    logger.info("ok");
	                } 
	            }
	        } else {
	        	logger.info("操作失败");
	            res = "操作失败";
	        }
			
		} catch (Exception e) {
			logger.error("操作失败，原因：",e);
            res = "系统异常";
		}finally {
	          try {
	        	  if (response != null) {
	                  response.close();
	              }
	              if (client != null) {
	                  client.close();
	              }
			} catch (Exception e2) {
				logger.info("发生异常操作失败");
				e2.printStackTrace();
			}
        }
        Map<String,String> result = new HashMap<String,String>();
        if("ok".equals(res)){
        	logger.info("OK="+res);
            result = resultMap;
        }else{
        	logger.info("status="+"500");
            result.put("status", "500");
            result.put("msg", res);
        }
    	return resultMap;
    }
}

