package com.hedong.hedongwx.utils;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Description： 
 * @author  origin  创建时间：   2019年10月16日 下午2:19:50
 */
public class EmployUtil {
	
	public final Logger	logger	= LoggerFactory.getLogger(this.getClass());
	
	public static String strRepAdd(String strbud, String val, Integer divisor){
		StringBuilder strnew = new StringBuilder();
		Integer len = strbud.length();
		Integer mulnum = len/divisor;
		for(int i=0;i<=mulnum; i++){
			int a = i+1;
			String strs = null;
			if(i<mulnum){
				strs = strbud.substring(i*divisor, a*divisor);
				strnew.append(strs+val);
			}else{
				strs = strbud.substring(i*divisor); 
				strnew.append(strs);
			}
        }
		return strnew.toString();
    }
	
	/**
	 * @Description： 判断Object类型的数据是否为空或“”字符，是返回true、否返回false
	 * @param: Object val
	 * @author： origin 
	 */
	public static Boolean isEmptys(Object val){
		try {
			String str = val == null ? null : val.toString().trim();
			return (str == null) || (str.length() == 0);
		} catch (Exception e) {
			e.printStackTrace();
			return false; 
		}
	}

	/**
	 * @Description： 判断Object类型的数据是否为空或“”字符，是返回true、否返回false
	 * @param: Object val
	 * @author： origin 
	 */
	public static Boolean isEmpty(Object val){
		Boolean verdict = false;
		try {
			String str = val == null ? null : val.toString().trim();
			if((str == null) || (str.length() == 0)){
				verdict = true;
			}
			return verdict;
		} catch (Exception e) {
			e.printStackTrace();
			return verdict; 
		}
	}
	
	/**
	 * @Description： 判断String类型的字符串是否为空或“”字符，是返回true、否返回false
	 * @param: String str
	 * @author： origin 
	 */
	public static boolean isEmpty(String str){
		 return (str == null) || (str.length() == 0);
	}
	
	/**
	 * @Description： 判断String类型的字符串是否 不为 空或“”字符，是返回true、否返回false
	 * @param: String str
	 * @author： origin 
	 */
	public static boolean isNotEmpty(String str){
		 return !isEmpty(str);
	}
	//=============================================================
//	public static Double getAbsoluteDouble(Object value) {
//		Math.abs(value);
//		if (isNull(value))
//			return 0.00d;
//		return Double.parseDouble(value);
//	}
	//=============================================================
	
	public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
	
	
	/**
	 * @Description： 根据请求request，获取访问的查询字符串
	 *            注：比如客户端发送 http://localhost/test.do?a=b&c=d&e=f；通过request.getQueryString()得到的是 a=b&c=d&e=f
	 *               API中注释写到：post方法传的参数，getQueryString（）得不到，它只对get方法得到的数据有效。
	 * @param: HttpServletRequest request
	 * @author： origin 
	 */
	public String getInquireCharacter(HttpServletRequest request){
		String queryString = "";
		try {
			//获取查询字符串
		    queryString = request.getQueryString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryString ;
	}	
	
//	/**
//	 * @Description： post请求   	先获取到参数名的集合，在组装到Map里
//	 * @param: 
//	 * @author： origin   2019年10月16日 上午10:18:23
//	 */
//	private Map<String, String> getAllRequestParamori( HttpServletRequest request) {
//	    Map<String, String> res = new HashMap<String, String>();
//	    Enumeration<?> temp = request.getParameterNames();
//	    if (null != temp) {
//	        while (temp.hasMoreElements()) {
//	            String en = (String) temp.nextElement();
//	            String value = request.getParameter(en);
//	            res.put(en, value);
//	        }
//	    }
//	    return res;
//	}
	
	/**
	 * @Description： 根据请求request，获取访问的参数列表，并将参数放置与map集合中，生成key——value键值对形式返回
	 * 				  如果request请求不含参数，则生成一个的Map集合，并返回
	 * @param: HttpServletRequest request
	 * @author： origin 
	 */
	public static Map<String,Object> getRequestParam(HttpServletRequest request){
		Map<String,Object> res = new HashMap<String,Object>();
		try {
			Map<String,String[]> map = request.getParameterMap();
			if(map==null||map.isEmpty()){
				return new HashMap<String, Object>();
			}
			/*
			 Set 是java中一个存储不重复元素，且无序的集合类。
			 Map.keyset()，表示将map对象的所有key值已set集合的形式返回，因为map也是无序的，且key值也是不可重复的，
			 因此这里用set集合存储key并返回也符合规则。
			 */
			Set<String> keys = map.keySet();
			for(String key : keys){
				String[] value = map.get(key) ;
//				if(value!=null&&value.length>0){
//					res.put(key, value[0]) ;
//				}else{
//					res.put(key, "") ;
//				}
				String v = value!=null&&value.length>0?value[0]:"";
				res.put(key, v) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res ;
	}
	
	/**
	 * @Description： 根据请求request，获取访问的参数列表，并将参数放置与map集合中，生成key——value键值对形式返回
	 * 				  如果request请求不含参数，则返回一个只读的Map集合
	 * @param: HttpServletRequest request
	 * @author： origin 
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getRequestParamRead(HttpServletRequest request){
		Map<String,Object> res = new HashMap<String,Object>();
		try {
			Map<String,String[]> map = request.getParameterMap();
			if(map==null||map.isEmpty()){
				return Collections.EMPTY_MAP;//返回只读的空MAP集合
			}
			Set<String> keys = map.keySet();
			for(String key : keys){
				String[] value = map.get(key) ;
				String v = value!=null&&value.length>0?value[0]:"";
				res.put(key, v) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res ;
	}
	
	/**
	 * @Description： 判断List<Map<String, Object>>集合数据是否为空 ， 是则生成一个List对象并返回；不是则返回
	 * 				 List<Map<String, Object>>对象本身
	 * @param: List<Map<String, Object>> result
	 * @author： origin 
	 */
	public static List<Map<String, Object>> isListMapEmpty(List<Map<String, Object>> result){
		try {
			if(result==null||result.isEmpty()){
				return new ArrayList<>();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	/**
	 * @Description： 判断List<T>集合数据是否为空 ， 是则生成一个List对象并返回；不是则返回对象本身
	 * @param: List<T> result
	 * @author： origin 
	 */
	public static <T> List<T> isListEmpty(List<T> result){
		try {
			if(result==null||result.isEmpty()){
				return new ArrayList<>();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	/**
	 * @Description： 判断Map集合数据是否为空 ， 是则生成一个map对象并返回；不是则返回Map对象本身
	 * @param: Map<String, Object> result
	 * @author： origin 
	 */
	public static Map<String,Object> isMapEmpty(Map<String, Object> result){
		try {
			if(result==null||result.isEmpty()){
				return new HashMap<String, Object>();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String,Object>();
		}
	}
	
	/*
	 Map readOnlyMap = request.getParameterMap();  
			Map writeAbleMap = new HashMap();  
			writeAbleMap.putAll(readOnlyMap);  
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String,Object> getRequestParams(HttpServletRequest request){
		Map<String,Object> res = new HashMap<String,Object>();
		try {
			Map<String,String[]> map = request.getParameterMap();
			if(map==null||map.isEmpty()){
				return new HashMap<String, Object>();
			}
			Map readOnlyMap = request.getParameterMap();  
			Map writeAbleMap = new HashMap();  
			writeAbleMap.putAll(readOnlyMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res ;
	}
	
	/**
	 * @Description： 构建一个响应返回格式 
	 * @param: code:状态码、  message:提示内容信息、     body:需求的信息内容 
	 * @author： origin   2019年11月5日 下午5:10:33
	 */
	public Map<String, Object> buildResponse(Integer code, String message, Object body) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("ret", code);
		response.put("message", message);
		if(null == body || "".equals(body)){
			response.put("body", CommonConfig.DEFAULT_JSONOBJECT);
		}else{
			response.put("body", body);
		}
		logger.info("输出打印 ：      ["+JSON.toJSONString(response)+"]");
		return response;
	}
	
	/**
	 * @Description： 便利JSONArray类型数据，将其转换为List<Map<String, Object>>类型数据返回
	 * 				 如果JSONArray类型参数为空，则返回生成一个List集合，并返回
	 * @param: JSONArray json
	 * @author： origin 
	 */
	@SuppressWarnings({ "unchecked" })
	public static List<Map<String, Object>> traversalJSONArray(JSONArray json){
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			if(json.isEmpty()||json.size()<1){
				return list;//只读的List集合
			}else{
				for (int i = 0; i < json.size(); i++) {
					Map<String, Object> map = new HashMap<>();
					JSONObject item = json.getJSONObject(i);
					map = item;
					list.add(map);
				}
				return list ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return list;//只读的List集合
		}
	}
	
	/**
	 * @Description： 便利JSONArray类型数据，将其转换为List<Map<String, Object>>类型数据返回
	 * 				 如果JSONArray类型参数为空，则返回只读类型的List集合
	 * @param: JSONArray json
	 * @author： origin 
	 */
	@SuppressWarnings({ "unchecked" })
	public static List<Map<String, Object>> ergodicJSONArray(JSONArray json){
		try {
			if(json.isEmpty()||json.size()<1){
				return Collections.EMPTY_LIST;//只读的List集合
			}else{
				List<Map<String, Object>> list = new ArrayList<>();
				for (int i = 0; i < json.size(); i++) {
					Map<String, Object> map = new HashMap<>();
					JSONObject item = json.getJSONObject(i);
					map = item;
					list.add(map);
				}
				return list ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.EMPTY_LIST;//只读的List集合
		}
	}
	
	//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	
//	
//	public Map<String, Object> buildSuccessResponsePage(Page page, Object body){
//		Map<String, Object> result = new HashMap<String, Object>();
//		if(body != null){
//			result.put("resData", body);
//		}else{
//			result.put("resData", page.getResultList());
//		}
//		result.put("numPerPage", page.getNumPerPage());
//		result.put("totalRows", page.getTotalRows());
//		result.put("totalPages", page.getTotalPages());
//		result.put("currentPage", page.getCurrentPage());
//		result.put("startIndex", page.getStartIndex());
//		result.put("lastIndex", page.getLastIndex());
//		//result.put("resultList", page.getResultList());
//		return buildResponse(0, "�ɹ�", result);
//	}
//	
//	public Map<String, Object> buildSuccessResponsePage(Page page){
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("numPerPage", page.getNumPerPage());
//		result.put("totalRows", page.getTotalRows());
//		result.put("totalPages", page.getTotalPages());
//		result.put("currentPage", page.getCurrentPage());
//		result.put("startIndex", page.getStartIndex());
//		result.put("lastIndex", page.getLastIndex());
//		result.put("resultList", page.getResultList());
//		return buildResponse(0, "�ɹ�", result);
//	}
	
	public Map<String, Object> buildSuccessResponse(){
		return buildResponse(0, "成功", CommonConfig.DEFAULT_JSONOBJECT);
	}
	
	public Map<String, Object> buildSuccessResponse(Object body){
		return buildResponse(0, "成功", body);
	}
	
	public Map<String, Object> buildErrorResponse(){
		return buildResponse(-1, "失败", CommonConfig.DEFAULT_JSONOBJECT);
	}
	
	public Map<String, Object> buildErrorResponse(String msg){
		return buildResponse(-1, msg, CommonConfig.DEFAULT_JSONOBJECT);
	}
	
	/**
	 * 用户token验证过期
	 * @return
	 */
	public Map<String, Object> buildTokenResponse(){
		return buildResponse(-2, "用户信息验证过期", CommonConfig.DEFAULT_JSONOBJECT);
	}
	
	/**
	 *  用户token验证过期
	 * @param ret 0：验证通过   1验证失败  2异常
	 * @return
	 */
	public Map<String, Object> buildTokenResponse(int ret){
		if(ret == 1){
			return buildResponse(-2, "用户信息验证过期", CommonConfig.DEFAULT_JSONOBJECT);
		}
		return buildResponse(-1, "用户信息异常", CommonConfig.DEFAULT_JSONOBJECT);
	}
	
	
	
	
	
}
