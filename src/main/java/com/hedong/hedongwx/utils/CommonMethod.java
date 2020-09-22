package com.hedong.hedongwx.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.entity.TemplateSon;
import com.alibaba.fastjson.JSONArray;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

/**
 * @Description: 
 * @Author: origin  创建时间:2020年7月20日 下午2:24:09
 * @common:   
 */

public class CommonMethod {


	//==================================================================================================	
	/**************************************************************************************************/

	//==================================================================================================	
	/**************************************************************************************************/
 	/**
 	 * 方案一:借助Set的特性进行去重
     * 去除重复数据
     * 由于Set的无序性，不会保持原来顺序
     * @param list
     */
    public static List<Map<String, Object>> distinct(List<Map<String, Object>> list) {
        final boolean sta = null != list && list.size() > 0;
        if (sta) {
            Set set = new HashSet();
            List doubleList= new ArrayList();
            set.addAll(list);
            doubleList.addAll(set);
            return doubleList;
        }
        return null;
    }
    
    //方案二 : 利用set集合特性保持顺序一致去重
    // Set去重并保持原先顺序的两种方法
    public static void delRepeat(List<String> list) {
    	   //方法一
        List<String> listNew = new ArrayList<String>(new TreeSet<String>(list));
        //方法二
        List<String> listNew2 = new ArrayList<String>(new LinkedHashSet<String>(list));
    }
    
    /**
     * 方案三 : 使用list自身方法remove()
     * 去除重复数据(一般不推荐)
     * 类似于冒泡排序
     * @param list
     */
  public static List<Map<String, Object>> distinctA(List<Map<String, Object>> list) {
        if (null != list && list.size() > 0) {
        //循环大的list集合
            for (int i = 0; i < list.size(); i++) {
            //得到list中每一个map
                Map map= list.get(i);
                for (int j = 1; j < list.size(); j++) {
                    Map map2= list.get(j);
                    if (map.equals(map2)) {
                        list.remove(j);
                        continue;
                    }
                }
            }
        }
        //得到最新移除重复元素的list
        return list;
    }
  
  //方案四 : 遍历List集合,将元素添加到另一个List集合中
	//遍历后判断赋给另一个list集合，保持原来顺序
	public static List<String> delRepeatB(List<String> list) {
		  List<String> listNew = new ArrayList<String>();
		  for (String str : list) {
		       if (!listNew.contains(str)) {
		           listNew.add(str);
		       }
		   }
		  return listNew ;
	}

//	//方案5 : 使用Java8特性去重
//	public static List<String> delRepeatC(List<String> list) {
//	     List<String> myList = listAll.stream().distinct().collect(Collectors.toList());
//		 return myList ;
//	}
	/*
	
	方法一:使用java8新特性stream进行List去重 
List newList = list.stream().distinct().collect(Collectors.toList()); 
System.out.println(“java8新特性stream去重:”+newList); 
list.add(39); 
	
	
	         for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); ) {
//                    System.out.println(i+"-"+list.get(i)+"-"+j+"！！！！"+list.get(j));
                if (i != j && list.get(i) == list.get(j)) {
//                    System.out.println(j+":"+list.get(j));
                    list.remove(j);
                } else {
                    j++;
                }
            }
        }

	 
	 
	 System.out.println(“双重for循环去重:”+list); 
list.add(39); 
方法三:set集合判断去重,不打乱顺序 
Set set1 = new HashSet(); 
List newList1 = new ArrayList(); 
for (Integer integer : list) { 
if(set1.add(integer)) { 
newList1.add(integer); 
} 
} 
System.out.println(“set集合判断去重:”+list); 
list.add(39); 
方法四:遍历后判断赋给另一个list集合 
List newList2 = new ArrayList(); 
for (Integer integer : list) { 
if(!newList2.contains(integer)){ 
newList2.add(integer); 
} 
} 
System.out.println(“赋值新list去重:”+newList2); 
list.add(39); 
方法五:set和list转换去重 
Set set2 = new HashSet(); 
List newList3 = new ArrayList(); 
set2.addAll(list); 
newList3.addAll(set2); 
System.out.println(“set和list转换去重:”+newList3);
	 */
//==================================================================================================	
/**************************************************************************************************/
	
	// --------------------------------------日期处理-------------------------------------
	/**
	 * @method_name: getTime
	 * @Description: 获取当前日期时间值        
	 * @return  返回当前日期时间字符 如2010-09-18 14:12:15
	 * @Author: origin  创建时间:2020年9月2日 上午10:35:33
	 * @common:
	 */
	public static String getTime() {
		return getTime("yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * @method_name: getTime
	 * @Description: 将指定Date时间转换为String类型时间
	 * @param date 指定Date时间
	 * @return：返回yyyy-MM-dd HH:mm:ss类型String格式日期时间字符 如2010-09-18 14:12:15
	 * @Author: origin  创建时间:2020年9月2日 上午10:44:02
	 * @common:
	 */
	public static String getTime(Date date) {
		return getTime(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * @method_name: getTime
	 * @Description: 获取指定格式的日期时间
	 * @param format:日期的格式
	 * @return：对应个的String类型的日期
	 * @Author: origin  创建时间:2020年9月2日 上午10:36:02
	 * @common:
	 */
	public static String getTime(String format) {
		try {
			if (StringUtil.isNull(format))
			return "";
			Date d = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(d);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * @Description：  将Date类型转换为指定类型（format）的String时间
	 * @author： origin 
	 * @return 返回String类型日期时间
	 */
	public static String getTime( Date date, String format) {
		try {
			if (StringUtil.isNull(format))
				return "";
			if (date == null)
				return "";
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
    
	/**
	 * @method_name: getPastTime
	 * @Description: 获取过去第几天的String类型日期
	 * @param past：相比今天过去的几天
	 * @return 返回String类型日期时间
	 * @Author: origin  创建时间:2020年9月2日 下午2:32:06
	 * @common:
	 */
    public static String getPastTime(Integer past) {
    	return getPastTime(past, "yyyy-MM-dd");
    }
    
    /**
     * @method_name: getPastTime
     * @Description:  获取过去第几天的String类型日期
     * @param past：相比今天过去的几天  format:日期的格式
     * @return 返回String类型日期时间
     * @Author: origin  创建时间:2020年9月2日 下午2:33:37
     * @common:
     */
    public static String getPastTime(Integer past, String format) {
    	try {
    		Date today = getPastDate(past);
//	        Calendar calendar = Calendar.getInstance();
//	        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
//	        Date today = calendar.getTime();
	        SimpleDateFormat formatter = new SimpleDateFormat(format);
	        String result = formatter.format(today);
	        return result;
    	} catch (Exception e) {
			e.toString();
			return null;
		}
    }
    
	/**
	 * @method_name: isLtNowTime (小于)
	 * @Description: 判断指定时间是否小于当前时间
	 * @param date 指定的时间
	 * @Author: origin  创建时间:2020年7月24日 下午6:20:30
	 * @common:
	 */
	public static boolean isLtNowTime(String time) {
		Date date = getDate(time);
		return isLtNowDate(date);
	}
	
	/**
	 * @method_name: isGtNowTime （大于）
	 * @Description: 判断指定时间不为空且是否小于当前时间
	 * @param date 指定的时间
	 * @Author: origin  创建时间:2020年7月24日 下午6:20:30
	 * @common:
	 */
	public static boolean isGtNowTime(String time) {
		Date date = getDate(time);
		return isGtNowDate(date);
	}
	
	
	//=-==========================================================================================
	
	/**
	 * @method_name: getDate
	 * @Description: 获取当前日期时间值        
	 * @return  返回当前日期Date类型时间字符 如
	 * @Author: origin  创建时间:2020年9月2日 上午10:35:33
	 * @common:
	 */
	public static Date getDate() {
		return new Date();
	}
	
	/**
	 * @method_name: getDate
	 * @Description: 将指定String时间转换为Date类型时间
	 * @param time 指定String时间
	 * @return：返回yyyy-MM-dd HH:mm:ss类型Date格式日期时间字符 如
	 * @Author: origin  创建时间:2020年9月2日 上午10:44:02
	 * @common:
	 */
	public static Date getDate(String time) {
		return getDate(time, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * @method_name: getDate
	 * @Description: 将String类型转换为指定类型（format）的Data时间
	 * @param time 指定String时间   format:日期的格式
	 * @return 返回指定格式类型Date格式日期时间字符 如
	 * @Author: origin  创建时间:2020年9月2日 下午6:46:34
	 * @common:
	 */
	public static Date getDate(String time, String format){ 
		try {
			if (StringUtil.isNull(time))
			return null;
			if (time.equals(""))
			return null;
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
			return new Date();
		}
	}
		
    /**
     * @method_name: getPastDate
     * @Description: 获取过去第几天的Date类型日期
	 * @param past：相比今天过去的几天
	 * @return 返回Date类型日期时间
     * @Author: origin  创建时间:2020年9月2日 下午2:42:57
     * @common:
     */
    public static Date getPastDate(int past) {
    	try {
    		Calendar calendar = Calendar.getInstance();
    		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
    		Date today = calendar.getTime();
    		return today;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

    /**
     * @method_name: getPastTime
     * @Description:  获取过去第几天的Date类型日期
     * @param past：相比今天过去的几天  format:日期的格式
     * @return 返回Date类型日期时间
     * @Author: origin  创建时间:2020年9月2日 下午2:33:37
     * @common:
     */
    public static Date getPastDate(Integer past, String format) {
    	try {
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
	        Date today = calendar.getTime();
	        SimpleDateFormat formatter = new SimpleDateFormat(format);
	        String daystr = formatter.format(today);
			return formatter.parse(daystr);
    	} catch (Exception e) {
			e.toString();
			return null;
		}
    }
	
	/**
	 * @method_name: isLtNowTime (小于)
	 * @Description: 判断指定时间是否小于当前时间
	 * @param date 指定的时间
	 * @Author: origin  创建时间:2020年7月24日 下午6:20:30
	 * @common:
	 */
	public static boolean isLtNowDate(Date date) {
		boolean flag = false;
		try {
			Date nowtime = new Date();
			//指定时间不为空且当前时间大于指定时间
			if(date != null && nowtime.after(date)){
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * @method_name: isGtNowTime （大于）
	 * @Description: 判断指定时间不为空且是否小于当前时间
	 * @param date 指定的时间
	 * @Author: origin  创建时间:2020年7月24日 下午6:20:30
	 * @common:
	 */
	public static boolean isGtNowDate(Date date) {
		boolean flag = false;
		try {
			Date nowtime = new Date();
			if(date != null && nowtime.before(date)){
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	//===============================================================================
    
    /**
     * @Description：  获取相应时间
     * @param: time:输入的时间		numerical:天数（获取几天前时间）   type:类型 1:凌晨0点时间、2:一天结束时间  3:当前时间
     * @author： origin   2019年11月8日 下午5:25:57
     */
    public static String getTime( Integer numerical, Integer type) {
    	try {
			String val;
			if(type.equals(1)){
				val = getPastTime(numerical, "yyyy-MM-dd 00:00:00");
			}else if(type.equals(2)){
				val = getPastTime(numerical, "yyyy-MM-dd 23:59:59");
			}else{
				val = getTime();
			}
	        return val;
    	} catch (Exception e) {
			e.toString();
			return null;
		}
    }
    
    public static Integer compareStringTime(String startdate, String enddate) {
    	int num = 2;
		SimpleDateFormat  sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		try {
        	Date sd = sf.parse(startdate);
            Date ed = sf.parse(enddate);
            if (sd.getTime() > ed.getTime()) {
            	num = 1;
            } else if (sd.getTime() < ed.getTime()) {
            	num = -1;
            } else {
            	num = 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return num;
    }
    
    public static Integer compareTime(String startdate, String enddate) {
		SimpleDateFormat  sf = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
        	Date sd = sf.parse(startdate);
            Date ed = sf.parse(enddate);
//            long differtime = (sd.getTime() - ed.getTime());
            long sdtime = sd.getTime();
            long edtime = ed.getTime();
            long differtime = (sdtime - edtime);
            Integer time = (int) (differtime/(1000*60*60*24));
            return time;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * 时间比较（比较制定两个时间的差）
     * @author  origin          
     * @version 创建时间：2019年3月5日  下午2:30:37
     */
	public static Integer differentDay(String startTime, String endTime){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
		try {
			startDate = sdf.parse(startTime);
			endDate=sdf.parse(endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
        Calendar stcalen = Calendar.getInstance();
        stcalen.setTime(startDate);
        Calendar encalen = Calendar.getInstance();
        encalen.setTime(endDate);
        Integer strday= stcalen.get(Calendar.DAY_OF_YEAR);//当前年的第几天     
        Integer endday = encalen.get(Calendar.DAY_OF_YEAR);
        Integer stryear = stcalen.get(Calendar.YEAR);//当前年 
        Integer endyear = encalen.get(Calendar.YEAR);
        Integer timeDistance = 0 ;
        if(stryear != endyear){//不同年
            for(int i=stryear; i<endyear; i ++){
                if(i%4==0 && i%100!=0 || i%400==0){//闰年
                    timeDistance += 366;
                }else{//不是闰年
                    timeDistance += 365;
                }
            }
            timeDistance += endday-strday;
            return timeDistance;
        }else{//同年
            timeDistance = endday-strday;
            return timeDistance;
        }
    }
	
	public static boolean isToday(Date inputJudgeDate) {
		boolean flag = false;
		//获取当前系统时间
		long longDate = System.currentTimeMillis();
		Date nowDate = new Date(longDate);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = dateFormat.format(nowDate);
		String subDate = format.substring(0, 10);
		//定义每天的24h时间范围
		String beginTime = subDate + " 00:00:00";
		String endTime = subDate + " 23:59:59";
		Date paseBeginTime = null;
		Date paseEndTime = null;
		try {
			paseBeginTime = dateFormat.parse(beginTime);
			paseEndTime = dateFormat.parse(endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(inputJudgeDate.after(paseBeginTime) && inputJudgeDate.before(paseEndTime)) {
			flag = true;
		}
		return flag;
	}

	public static boolean isTime(Date date, Integer hour) {
		boolean flag = false;
		//获取当前系统时间
		long longDate = System.currentTimeMillis();
		long datetime = date.getTime();
		long secondDiffer = (longDate - datetime)/1000;
//		minute
		long hourDiffer = secondDiffer/3600;
		if(hourDiffer>=2){
			flag = true;
		}
		return flag;
	}
	
	//ORIGIN5
	// --------------------------------------处理-------------------------------------
	/**
	 * @method_name: getRequestParam
	 * @Description: 将获取的request对象转换为Map<String,Object>格式数据
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月29日 下午3:58:12
	 * @common:
	 */
	public static Map<String,Object> getRequestParam(HttpServletRequest request){
		Map<String,Object> res = new HashMap<String,Object>();
		try {
			Map<String,String[]> map = request.getParameterMap();
			if(map==null||map.isEmpty()){
				return new HashMap<String, Object>();
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
	 * @method_name: getParameterMap  【从request中获得参数Map，并返回可读的Map 】
	 * @Description:  前端传给了后台的参数，没有对应的实体类去接受，可以用request去接受，然后通过这个方法转换成map
	 * @param request
	 * @return
	 * @Author: origin  创建时间:2020年8月29日 下午3:14:03
	 * @common:
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })  
    public static Map getParameterMap(HttpServletRequest request) {  
        // 参数Map  
        Map properties = request.getParameterMap();  
        // 返回值Map  
        Map returnMap = new HashMap();  
        Iterator entries = properties.entrySet().iterator();  
        Map.Entry entry;  
        String name = "";  
        String value = "";  
        while (entries.hasNext()) {  
            entry = (Map.Entry) entries.next();  
            name = (String) entry.getKey();  
            Object valueObj = entry.getValue();  
            if(null == valueObj){  
                value = "";  
            }else if(valueObj instanceof String[]){  
                String[] values = (String[])valueObj;  
                for(int i=0;i<values.length;i++){  
                    value = values[i] + ",";  
                }  
                value = value.substring(0, value.length()-1);  
            }else{  
                value = valueObj.toString();  
            }  
            returnMap.put(name, value);  
        }  
        return returnMap;  
    } 
    
    //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    /**
     * @method_name: responseBuild
     * @Description: 响应值构建
     * @param code：返回状态码      message：返回提示消息     map：指定的Map集合
     * @return
     * @Author: origin  创建时间:2020年9月2日 下午4:01:51
     * @common:
     */
	public static Map<String, Object> responseBuild(Integer code, String message, Map<String, Object> map){
		map = map == null ? new HashMap<String, Object>() : map;
		try {
			map.put("returncode", toInteger(code));
			map.put("returnmessage", toString(message));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * @method_name: responseBuild
     * @Description: 响应值构建
     * @param code：返回状态码      message：返回提示消息     result：输出的结果信息
	 * @return
	 * @Author: origin  创建时间:2020年9月2日 下午4:03:17
	 * @common:
	 */
	public static Map<String, Object> responseBuild(Integer code, String message, Object result){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("returncode", toInteger(code));
			map.put("returnmessage", toString(message));
			map.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
    
	/**
	 * @Description：金额（充值、赠送）消费逻辑处理
	 * @param topupmoney:充值金额    sendmoney:赠送金额    consumemoney:消费金额     
	 * 		  operationtype:操作类型[0:扣费    1:回收退费]
	 * @author： origin 2020年6月28日上午11:25:32
	 * 2000	处理数据成功
	 * 3000  处理出现异常(异常错误)
	 * 3002	处理类型错误（传过来的处理类型不正确）
	 * 3003	在线卡金额不足
	 */
	public static Map<String, Object> moneyDispose( Double topupmoney, Double sendmoney, Double consumemoney, Integer operationtype) {
		Map<String, Object> resultdata = new HashMap<>();
		try {
			//数据处理
			//============================================================================
			Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额（充值与赠送的合）
			//============================================================================
			Double opermoney = consumemoney;//操作总额
			Double opertopupmoney = 0.00;	//操作充值金额
			Double opersendmoney = 0.00;	//操作赠送金额
			//==========================================================================
			Double topupbalance = 0.00;//充值余额
			Double sendbalance = 0.00;//赠送余额
			Double accountbalance = 0.00;//账户余额
			//==========================================================================
			if(operationtype==0){
				Double comparemoney = CommUtil.subBig(accountmoney, opermoney); //总额、操作比较金额差
				if(comparemoney>=0){//金额足够，扣费
					Double contrastmoney = CommUtil.subBig(topupmoney, opermoney); //充值、操作比较金额差
					if(contrastmoney>=0){//此时说明用户充值金额足够
						opertopupmoney = opermoney;
						topupbalance = contrastmoney;
						opersendmoney = 0.00;
						sendbalance = sendmoney;
					}else{
						opertopupmoney = topupmoney;
						topupbalance = 0.00;
						opersendmoney = CommUtil.toDouble(Math.abs(contrastmoney));
						sendbalance = CommUtil.subBig(sendmoney, opersendmoney);
					}
					resultdata.put("code", 2000);
					resultdata.put("message", "金额扣费完成");
				}else{
					topupbalance = topupmoney;
					sendbalance = sendmoney;
					resultdata.put("code", 3003);
					resultdata.put("message", "金额不足");
				}
			}else if (operationtype==1){
				if(topupmoney>0) {
					opertopupmoney = opermoney;
					topupbalance = CommUtil.addBig(topupmoney, opertopupmoney);
					opersendmoney = 0.00;
					sendbalance = sendmoney;
				}else{
					opertopupmoney = 0.00;
					topupbalance = 0.00;
					opersendmoney = opermoney;
					sendbalance = CommUtil.addBig(sendmoney, opersendmoney);
				}
				resultdata.put("code", 2000);
				resultdata.put("message", "回收金额成功");
			}else{
				topupbalance = topupmoney;
				sendbalance = sendmoney;
				resultdata.put("code", 3002);
				resultdata.put("message", "处理类型错误");
			}
			accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
			resultdata.put("topupmoney", topupmoney);
			resultdata.put("sendmoney", sendmoney);
			resultdata.put("accountmoney", accountmoney);
			resultdata.put("opermoney", opermoney);
			resultdata.put("opertopupmoney", opertopupmoney);
			resultdata.put("opersendmoney", opersendmoney);
			resultdata.put("topupbalance", topupbalance);
			resultdata.put("sendbalance", sendbalance);
			resultdata.put("accountbalance", accountbalance);
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			resultdata.put("code", 3000);
			resultdata.put("message", "处理出现异常");
			return resultdata;
		}
	}
	
	/*  消费金额（充值、赠送）
	//============================================================================
 	Double topupmoney = CommUtil.toDouble(userBalance);//充值金额
	Double sendmoney = CommUtil.toDouble(usersendmoney);//赠送金额
	Double accountmoney = CommUtil.addBig(userBalance, usersendmoney);//账户金额
	//============================================================================
	Double opermoney = CommUtil.toDouble(chargeRecord.getExpenditure());//操作总额
	Double opertopupmoney = 0.00;	//操作充值金额
	Double opersendmoney = 0.00;	//操作赠送金额
	//============================================================================
	Double topupbalance = 0.00;//充值余额
	Double sendbalance = 0.00;//赠送余额
	//============================================================================
	
	Double comparemoney = CommUtil.subBig(accountmoney, opermoney); //总额、操作比较金额差
	if(comparemoney>=0){//金额足够，扣费
		Double contrastmoney = CommUtil.subBig(topupmoney, opermoney); //充值、操作比较金额差
		if(contrastmoney>=0){//此时说明用户充值金额足够
			opertopupmoney = opermoney;
			topupbalance = contrastmoney;
			opersendmoney = 0.00;
			sendbalance = sendmoney;
		}else{
			opertopupmoney = topupmoney;
			topupbalance = 0.00;
			opersendmoney = CommUtil.toDouble(Math.abs(contrastmoney));
			sendbalance = CommUtil.subBig(sendmoney, opersendmoney);
		}
	}else{//金额不足
		topupbalance = topupmoney;
		sendbalance = sendmoney;
	}
	Double accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
 */
	
	/**
	 * @Description：消费金额（充值、赠送）逻辑处理
	 * @param topupmoney:充值金额    sendmoney:赠送金额    consumemoney:消费金额     
	 * 		  operationtype:操作类型[0:扣费    1:回收退费]
	 * @author： origin 2020年6月28日上午11:25:32
	 * 2000	处理数据成功
	 * 3000  处理出现异常(异常错误)
	 * 3002	处理类型错误（传过来的处理类型不正确）
	 * 3003	在线卡金额不足
	 */
	public static Map<String, Object> consumemMoneyDispose( Double topupmoney, Double sendmoney, Double consumemoney) {
		Map<String, Object> resultdata = new HashMap<>();
		try {
			//数据处理
			//============================================================================
			Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额（充值与赠送的合）
			//============================================================================
			Double opermoney = consumemoney;//操作总额
			Double opertopupmoney = 0.00;	//操作充值金额
			Double opersendmoney = 0.00;	//操作赠送金额
			//==========================================================================
			Double topupbalance = 0.00;//充值余额
			Double sendbalance = 0.00;//赠送余额
			Double accountbalance = 0.00;//账户余额
			//==========================================================================
			Double comparemoney = CommUtil.subBig(accountmoney, opermoney); //总额、操作比较金额差
			if(comparemoney>=0){//金额足够，扣费
				Double contrastmoney = CommUtil.subBig(topupmoney, opermoney); //充值、操作比较金额差
				if(contrastmoney>=0){//此时说明用户充值金额足够
					opertopupmoney = opermoney;
					topupbalance = contrastmoney;
					opersendmoney = 0.00;
					sendbalance = sendmoney;
				}else{
					opertopupmoney = topupmoney;
					topupbalance = 0.00;
					opersendmoney = CommUtil.toDouble(Math.abs(contrastmoney));
					sendbalance = CommUtil.subBig(sendmoney, opersendmoney);
				}
				resultdata.put("code", 2000);
				resultdata.put("message", "金额扣费完成");
			}else{
				topupbalance = topupmoney;
				sendbalance = sendmoney;
				resultdata.put("code", 3003);
				resultdata.put("message", "金额不足");
			}
			accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
			resultdata.put("topupmoney", topupmoney);
			resultdata.put("sendmoney", sendmoney);
			resultdata.put("accountmoney", accountmoney);
			resultdata.put("opermoney", opermoney);
			resultdata.put("opertopupmoney", opertopupmoney);
			resultdata.put("opersendmoney", opersendmoney);
			resultdata.put("topupbalance", topupbalance);
			resultdata.put("sendbalance", sendbalance);
			resultdata.put("accountbalance", accountbalance);
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			resultdata.put("code", 3000);
			resultdata.put("message", "处理出现异常");
			return resultdata;
		}
	}
	
	/*			
			 topupmoney:充值金额    	 sendmoney:赠送金额     
	  		 consumemoney:消费金额   operationtype:操作类型[0:扣费    1:回收退费]
	Map<String, Object> resultmoney  = moneyDispose(topupmoney, sendmoney, consumemoney, operationtype);
	Map<String, Object> resultmoney  = consumemMoneyDispose(topupmoney, sendmoney, consumemoney);
	Map<String, Object> resultmoney  = recycleMoneyDispose(topupmoney, sendmoney, consumemoney);
	Double topupmoney = CommUtil.toDouble(resultmoney.get("topupmoney"));//充值金额
	Double sendmoney = CommUtil.toDouble(resultmoney.get("sendmoney"));//赠送金额
	Double accountmoney = CommUtil.toDouble(resultmoney.get("accountmoney"));//账户金额
	Double opermoney = CommUtil.toDouble(resultmoney.get("opermoney"));//操作总额
	Double opertopupmoney = CommUtil.toDouble(resultmoney.get("opertopupmoney"));//操作充值金额
	Double opersendmoney = CommUtil.toDouble(resultmoney.get("opersendmoney"));//操作赠送金额
	Double topupbalance = CommUtil.toDouble(resultmoney.get("topupbalance"));//充值余额
	Double sendbalance = CommUtil.toDouble(resultmoney.get("sendbalance"));//赠送余额
	Double accountbalance = CommUtil.toDouble(resultmoney.get("accountbalance"));//账户余额
	
	*/
	
	/*  回收金额（充值、赠送）
	 	Double topupmoney = CommUtil.toDouble(userBalance);//充值金额
		Double sendmoney = CommUtil.toDouble(usersendmoney);//赠送金额
		Double accountmoney = CommUtil.addBig(userBalance, usersendmoney);//账户金额
		//============================================================================
		Double opermoney = CommUtil.toDouble(chargeRecord.getExpenditure());//操作总额
		Double opertopupmoney = 0.00;	//操作充值金额
		Double opersendmoney = 0.00;	//操作赠送金额
		//==========================================================================
		Double topupbalance = 0.00;//充值余额
		Double sendbalance = 0.00;//赠送余额
		if(topupmoney>0) {
			opertopupmoney = opermoney;
			topupbalance = CommUtil.addBig(topupmoney, opertopupmoney);
			opersendmoney = 0.00;
			sendbalance = sendmoney;
		}else{
			opertopupmoney = 0.00;
			topupbalance = 0.00;
			opersendmoney = opermoney;
			sendbalance = CommUtil.addBig(sendmoney, opersendmoney);
		}
		Double accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
		
	 */
	/**
	 * @Description：回收金额（充值、赠送）逻辑处理
	 * @param topupmoney:充值金额    sendmoney:赠送金额    consumemoney:消费金额     
	 * 		  operationtype:操作类型[0:扣费    1:回收退费]
	 * @author： origin 2020年6月28日上午11:25:32
	 * 2000	处理数据成功
	 * 3000  处理出现异常(异常错误)
	 * 3002	处理类型错误（传过来的处理类型不正确）
	 * 3003	在线卡金额不足
	 */
	public static Map<String, Object> recycleMoneyDispose( Double topupmoney, Double sendmoney, Double consumemoney) {
		Map<String, Object> resultdata = new HashMap<>();
		try {
			//数据处理
			//============================================================================
			Double accountmoney = CommUtil.addBig(topupmoney, sendmoney);//账户金额（充值与赠送的合）
			//============================================================================
			Double opermoney = consumemoney;//操作总额
			Double opertopupmoney = 0.00;	//操作充值金额
			Double opersendmoney = 0.00;	//操作赠送金额
			//==========================================================================
			Double topupbalance = 0.00;//充值余额
			Double sendbalance = 0.00;//赠送余额
			Double accountbalance = 0.00;//账户余额
			//==========================================================================
			if(topupmoney>0) {
				opertopupmoney = opermoney;
				topupbalance = CommUtil.addBig(topupmoney, opertopupmoney);
				opersendmoney = 0.00;
				sendbalance = sendmoney;
			}else{
				opertopupmoney = 0.00;
				topupbalance = 0.00;
				opersendmoney = opermoney;
				sendbalance = CommUtil.addBig(sendmoney, opersendmoney);
			}
			accountbalance = CommUtil.addBig(topupbalance, sendbalance);//账户余额
			resultdata.put("topupmoney", topupmoney);
			resultdata.put("sendmoney", sendmoney);
			resultdata.put("accountmoney", accountmoney);
			resultdata.put("opermoney", opermoney);
			resultdata.put("opertopupmoney", opertopupmoney);
			resultdata.put("opersendmoney", opersendmoney);
			resultdata.put("topupbalance", topupbalance);
			resultdata.put("sendbalance", sendbalance);
			resultdata.put("accountbalance", accountbalance);
			resultdata.put("code", 2000);
			resultdata.put("message", "回收金额成功");
			return resultdata;
		} catch (Exception e) {
			e.printStackTrace();
			resultdata.put("code", 3000);
			resultdata.put("message", "处理出现异常");
			return resultdata;
		}
	}
	
	//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    
	public static Map<String,Object> isEmpty(Map<String, Object> result){
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
	
	public static List<Map<String, Object>> isEmpty(List<Map<String, Object>> result){
		try {
			if(result==null||result.isEmpty()){
				return new ArrayList<Map<String, Object>>();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String, Object>>();
		}
	}
	

	
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
	
	public static boolean isNull(String stringVal) {
		return isNull(stringVal, true);
	}
	
	/**
	 * 判断某个字符是否为空，空字符串作为可选项
	 * 
	 * @param stringVal
	 * @param includeBlankString
	 * @return
	 */
	public static boolean isNull(String stringVal, boolean includeBlankString) {
		if (stringVal == null)
			return true;
		if (includeBlankString && stringVal.trim().equals(""))
			return true;
		return false;
	}
/**************************************************************************************************/
//==================================================================================================	
	/**
	 * 取余
	 * @param val 
	 * @param obj
	 * @return
	 */
	public static Integer getRemainder(Integer val, Integer obj){
		Integer remainder = 0;
		try {
//			remainder = (val & obj);
			remainder = (val & (obj-1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return remainder;
	}
	
	
	// --------------------------------------基础类型数据处理-------------------------------------
	public static Integer forInteger(Object val){
		if (val == null || val.toString().trim() == ""){
			return null;
		}else{
			return forLong(val).intValue();
		}
	}
	public static Long forLong(Object val){
		if (val == null || val.toString().trim() == ""){
			return null;
		}else{
			return forDouble(val).longValue();
		}
	}
		
	public static Double forDouble(Object val){
		if (val == null || val.toString().trim() == ""){
			return null;
		}else{
			try {
				Double dol = Double.valueOf(val.toString().trim());
				BigDecimal bde = new BigDecimal(dol);
				dol = bde.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();   
				return dol;
			} catch (Exception e) {
				return null;
			}
		}
	}
		 
	public static Float forFloat(Object val){
		if (val == null || val.toString().trim() == ""){
			return null;
		}else{
			return forDouble(val).floatValue();
		}
	}
	
	
	// --------------------------------------基础类型数据处理2-------------------------------------
	public static Integer toInteger(Object val){
		return toLong(val).intValue();
	}

	public static Long toLong(Object val){
		return toDouble(val).longValue();
	}
	
	public static Double toDouble(Object val){
		if (val == null){
			return 0.00D;
		}else if (val == ""){
			return 0.00D;
		}else if (val.toString().trim() == ""){
			return 0.00D;
		}
		try {
			Double dol = Double.valueOf(val.toString().trim());
			BigDecimal bde = new BigDecimal(dol);
			dol = bde.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();   
			return dol;
		} catch (Exception e) {
			return 0.00D;
		}
	}
	 
	public static Float toFloat(Object val){
		return toDouble(val).floatValue();
	}
	
	
	
	
	public static BigDecimal toBigDecimal(Object val){
		BigDecimal ret = new BigDecimal("0");
		if (val == null){
			return ret;
		}
		try {
			return new BigDecimal(val.toString().trim());
		} catch (Exception e) {
			return ret;
		}
	}
	

/**************************************************************************************************/
//==================================================================================================	
	
	/**
	 * @Description：根据设备号(或字段)判断模板类型
	 * @param versions   0:出厂默认  1:离线充值机  2:脉冲投币  3:钱包模板  4:系统在线卡充值  5:系统包月模板  6:系统v3模板  
	 * 					 7:十路智慧款（16路智慧款、20路智慧款） 8:系统大功率模板   9:系统电轿款模板（单路交流桩）
	 * @return Integer 类型的 status返回值
	 * @author： origin  2020年7月4日下午6:18:59
	 * @comment:
	 */
	public static String createTempName(String versions){
		String name = "模板";
		try {
			/** 设备硬件版本 00-默认出厂版本、01-十路智慧款、02-电轿款、03-脉冲投币、04-离线充值机、
			 * 05-16路智慧款、06-20路智慧款、07单路交流桩 、08新版10路智慧款V3*/
			if(versions=="00"){//00:出厂默认
				name = "默认充电模板";
			}else if(versions=="01" || versions=="05" || versions=="06"){//01:十路智慧款   05:16路智慧款   06:20路智慧款
				name = "充电模板";
			}else if(versions=="02"){//02:电轿款   07:单路交流桩
				name = "电轿款模板";
			}else if(versions=="03"){//03:脉冲投币
				name = "脉冲模板";
			}else if(versions=="04"){//04:离线充值机
				name = "离线充值机模板";
			}else if(versions=="08"){//08:新版10路智慧款V3
				name = "v3模板";
			}else if(versions=="钱包"){
				name = "钱包模板";
			}else if(versions=="在线卡"){
				name = "在线卡模板";
			}else if(versions=="包月"){
				name = "包月模板";
			}else if(versions=="大功率"){
				name = "大功率模板";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name ;
	}
	
	
	/**
	 * @method_name: getEquipmentnum
	 * @Description: 根据扫码信息获取六位的设备号信息或者六位的设备号与端口号信息【扫码内容必须传递为6、7、8位数字】
	 * @param equipmentnuminfor  设备号信息
	 * @return
	 * @Author: origin  创建时间:2020年8月29日 下午2:56:55
	 * @common:
	 */
	public static Map<String, Object> getEquipmentnum(String equipmentnuminfor) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			String devicenum = CommUtil.trimToEmpty(equipmentnuminfor);
			Integer equlengch = CommUtil.toInteger(devicenum);
			String equipmentnum = null;
			String port = null;
			if(equlengch.equals(6)){
				equipmentnum =  equipmentnuminfor;
				resultdata.put("QRcodeType", "device");
			}else if(equlengch.equals(7) || equlengch.equals(8)){
				equipmentnum =  equipmentnuminfor.substring( 0, 6);
				port = CommUtil.toString(equipmentnuminfor.substring(6));
				resultdata.put("QRcodeType","port");
			}else{
				return CommUtil.responseBuildInfo(2004, "设备号传递不正确", resultdata);
			}
			resultdata.put("port", port);
			resultdata.put("equipmentnum", equipmentnum);
			CommUtil.responseBuildInfo(200, "成功", resultdata);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(3000, "异常错误", resultdata);
		}
		return resultdata;
	}
	
	/**
	 * @method_name: verifyEquipment
	 * @Description: 校验设备是否可以使用（如：在线状态、绑定与否、是否到期）
	 * @param bindtype 绑定状态 0:未绑定   1:绑定
	 * @param state 在线状态 0: 不在线   1:在线
	 * @param expirationtime : 设备到期时间
	 * @return Map<String, Object> resultdata 类型数据；判断code不为200 全部为不可使用
	 * @Author: origin  创建时间:2020年7月25日 下午3:56:01
	 * @common:   
	 */
	public static Map<String, Object> verifyEquipment(Integer deviceType, Integer bindtype, Integer state, Date expirationtime) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			bindtype =  CommUtil.toInteger(bindtype);
			state =  CommUtil.toInteger(state);
			if(deviceType!=2){//设备类型   1:联网模块   2:蓝牙模块
				if(state==0) return CommUtil.responseBuildInfo(2001, "设备离线", resultdata);
				if(bindtype==0) return CommUtil.responseBuildInfo(2002, "设备未绑定", resultdata);
			}
			//设备到期时间不为空且当前时间大于到期时间
			if(expirationtime != null && new Date().after(expirationtime)){
				resultdata.put("expirationtime", expirationtime);
				resultdata.put("expirationstatus", 0);
				return CommUtil.responseBuildInfo(2003, "设备已到期", resultdata);
			}
			return CommUtil.responseBuildInfo(200, "成功", resultdata);
		} catch (Exception e) {
			e.printStackTrace();
			return CommUtil.responseBuildInfo(3000, "异常错误", resultdata);
		}
	}
	
	/**
	 * @method_name: getTempDefaultObje
	 * @Description: 获取模板默认指定对象的索引和选中的对象
	 * @param tempson 需要获得索引的子模板
	 * @param hardversion 设备硬件版本号
	 * @return
	 * @Author: origin  创建时间:2020年7月25日 下午4:09:01
	 * @common:
	 */
	public static Map<String, Object> getTempDefaultObje( List<TemplateSon> tempson, String hardversion) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		int index = 0;
		if(tempson.size()>1){
			if("08".equals(hardversion)){
				for (TemplateSon templateSon : tempson) {
					if (templateSon.getName().contains("2元")) {
						resultdata.put("defaultchoose", templateSon.getId());
						resultdata.put("defaultindex", index);
						break;
					}
					index++;
				}
				if(resultdata.get("defaultchoose")==null){
					resultdata.put("defaultchoose", tempson.get(0).getId());
					resultdata.put("defaultindex", 0);
				}
			}else{
				for (TemplateSon templateSon : tempson) {
					if (index == 0 && !templateSon.getName().contains("1元")) {
						resultdata.put("defaultchoose", templateSon.getId());
						resultdata.put("defaultindex", index);
						break;
					} else if (index == 1) {
						resultdata.put("defaultchoose", templateSon.getId());
						resultdata.put("defaultindex", index);
						break;
					}
					index++;
				}
			}
		}else if(tempson.size()==1){
			resultdata.put("defaultchoose", tempson.get(0).getId());
			resultdata.put("defaultindex", 0);
		}else{
			resultdata.put("defaultchoose", null);
			resultdata.put("defaultindex", -1);
		}
		return resultdata;
	}
	

/**************************************************************************************************/
//==================================================================================================	

    
	/**
	 * @method_name: gainVersionsType
	 * @Description: 根据版本号获取赋值同类型设备的所有版本信息
	 * @param versions
	 * @Author: origin  创建时间:2020年8月29日 下午2:39:16
	 * @common:
	 */
	public static String gainVersionsType(String versions){
		String versiontype = null;
		try {
			if("00".equals(versions) || "01".equals(versions) || "05".equals(versions) || "06".equals(versions)){//00:出厂默认  01:十路智慧款   05:16路智慧款   06:20路智慧款
				versiontype = "00,01,05,06";
			}else if("02".equals(versions)){//02:电轿款   07:单路交流桩
				versiontype = "02,07";
			}else if("03".equals(versions)){//03:脉冲投币
				versiontype = "03";
			}else if("04".equals(versions)){//04:离线充值机
				versiontype = "04";
			}else if("08".equals(versions)){//08:新版10路智慧款V3
				versiontype = "08";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versiontype ;
	}

	/**
	 * @Description：根据设备号(或字段)判断模板类型
	 * @param versions   0:出厂默认  1:离线充值机  2:脉冲投币  3:钱包模板  4:系统在线卡充值  5:系统包月模板  6:系统v3模板  
	 * 					 7:十路智慧款（16路智慧款、20路智慧款） 8:系统大功率模板   9:系统电轿款模板（单路交流桩）
	 * @return Integer 类型的 status返回值
	 * @author： origin  2020年7月4日下午6:18:59
	 * @comment:
	 */
	public static Integer gainTempStatus(String versions){
		Integer status = 0;
		try {
			/*
			 模板类型 Status
			 0:出厂默认  1:离线充值机  2:脉冲投币  3:钱包模板  4:系统在线卡充值  5:系统包月模板  6:系统v3模板  7:十路智慧款（16路智慧款、20路智慧款）
			 8:系统大功率模板   9:系统电轿款模板（单路交流桩）
			 设备版本：
	 		 00:默认出厂版本    01:十路智慧款     02:电轿款     03:脉冲投币    04:离线充值机    05:16路智慧款    06:20路智慧款    07:单路交流桩    
	 		 08:新版10路智慧款V3
			 */
			if(versions=="00"){//00:出厂默认
				status = 0;
			}else if(versions=="01" || versions=="05" || versions=="06"){//01:十路智慧款   05:16路智慧款   06:20路智慧款
				status = 7;
			}else if(versions=="02"){//02:电轿款   07:单路交流桩
				status = 9;
			}else if(versions=="03"){//03:脉冲投币
				status = 2;
			}else if(versions=="04"){//04:离线充值机
				status = 1;
			}else if(versions=="08"){//08:新版10路智慧款V3
				status = 6;
			}else if(versions=="钱包"){
				status = 3;
			}else if(versions=="在线卡"){
				status = 4;
			}else if(versions=="包月"){
				status = 5;
			}else if(versions=="大功率"){
				status = 8;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status ;
	}
	
	/*
	 * Integer paysource = CommUtil.toInteger(trade.getPaysource());
			Integer status = CommUtil.toInteger(trade.getStatus());
			Integer paytype = CommUtil.toInteger(trade.getPaytype());
	 */
	public static Integer tradeConvert(Integer paysource, Integer status, Integer paytype){
		try {
//			/** 支付来源1、充电模块-2、脉冲模块-3、离线充值机-4、钱包-5、在线卡-6、包月支付*/
//			private Integer paysource;
//			/** 支付方式1、钱包2、微信3、支付宝 、4微信小程序、5支付宝小程序 6虚拟充值*/
//			private Integer paytype;
//			/** 订单状态1、正常2、退款 */
			if(paysource==1){//充电
				//消费类型： '1:钱包  2:微信  3:支付宝  4:包月下发数据 5:投币 6:离线卡 7:在线卡 8:支付宝小程序'
				if(paytype.equals(5) && status==1){
					paytype = 8;
				}else if(paytype.equals(5) && status==2){
					paytype = 9;
				}
				status = 0;
			}else if(paysource==2){//脉冲
				status = 7;
			}else if(paysource==3){//离线充值机
				status = 9;
			}else if(paysource==4){//钱包
				status = 2;
			}else if(paysource==5){//在线卡
				status = 1;
			}else if(paysource==6){//包月
				status = 6;
			}else if(paysource==7){
				status = 3;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status ;
	}
	

	// --------------------------------------日期相关的字符串处理-------------------------------------
	//ORIGINdss
	
	
	public static List<String> StringList(String val) {
		List<String> list = new ArrayList<String>();
		JSONArray listObject = JSONArray.parseArray(val);
        for (Object item : listObject){
        	list.add(CommUtil.toString(item));
        }
		return list;
	}
	
	// 字符串处理
	public static List<String> jsonList(String val) {
		List<String> list = new ArrayList<String>();
		val = val.replace("[", "").replace("]", "").replace( "\"", "");
		//["000008", "000010", "000012"]
		Integer a = val.length();
		for (int i = 0; i < a; i++) {
			if(val.indexOf(",")!=-1){
				Integer index = val.indexOf(",");
				list.add(val.substring(0, index));
				val = val.substring(index+1);
			}else{
				list.add(val);
				a = 0;
			}
		}
		return list;
	}
	
	public static JSON toJson(Map<String, Object> map){
		Map<String, String> newmap = new HashMap<>();
        for(String s : map.keySet()){ 
        	newmap.put(s, map.get(s).toString());
        }
        JSONObject jsonMap = JSONObject.fromObject(newmap);
		return jsonMap;
	 }
	
	public static JSON toListJson(List<Map<String, Object>> listmap){
		JSONArray json = new JSONArray();
		for(Map<String,Object> item : listmap){
			Map<String, String> newmap = new HashMap<>();
            for(String s : item.keySet()){ 
            	newmap.put(s, item.get(s).toString());
            }
            JSONObject jsonMap = JSONObject.fromObject(newmap);
			json.add(jsonMap);
        }
		return (JSON) json;
	 }
	
	public static String toForHex(Integer val) {
		StringBuffer str = new StringBuffer("00000000");
		//str.append("0X");toUpperCase
		try {
			String sca = Integer.toHexString(val).toUpperCase();
			Integer num = sca.length();
			Integer marknum = str.length();
			if(sca.length()<8) sca = str.replace(marknum-num, marknum, sca).toString();
			return sca.toString();
		} catch (Exception e) {
			return str.toString();
		}
	}
	
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
	
	public static Boolean isEmpty(Object val){
		try {
			String str = val == null ? null : val.toString().trim();
			return (str == null) || (str.length() == 0);
		} catch (Exception e) {
			e.printStackTrace();
			return false; 
		}
	}

	public static String toString(Object val){
		String ts = toStringTrim(val);
		return isEmpty(ts) ? null : ts;
	}
	
	public static boolean isEmpty(String str){
		 return (str == null) || (str.length() == 0);
	}
	
	 public static String toStringTrim(Object str){
		 return str == null ? null : str.toString().trim();
	 }
	 
	 public static String trimToEmpty(Object str){
		 return str == null ? "" : str.toString().trim();
	 }
	
	
	
	 // 进行加法运算（保留两位小数）
    public static double addBig(double d1, double d2){
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.add(b2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    // 进行减法运算
    public static double subBig(double d1, double d2){
    	BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.subtract(b2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    

    //保留小数
    public static double decimals(Integer digit, double value){
    	BigDecimal val = new BigDecimal(value);
        return val.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    public static BigDecimal togetBigDecimal(Object value) {
        BigDecimal ret = new BigDecimal("0");
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {//String类型
                ret = new BigDecimal((String) value).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {//double
                ret = new BigDecimal(((Number) value).doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
            }
        }
        return ret;
    }
	
	public static String getExceptInfo(Exception e){
		String str = "";
		try {
			str = "【输出异常信息】   "+e.toString()+ "\r\n";
			StackTraceElement[] trace = e.getStackTrace();
			for (StackTraceElement s : trace) {
				if(s.toString().contains("com.hedong")){
					str += "\tat " + s + "\r\n";
				}
			}
		} catch (Exception ex) {
			str = "【异常信息转换输出错误】   "+ex.toString();
		}
		return str;
	}
	
	@SuppressWarnings("rawtypes")
	public static HashMap<String, Object> toHashMaps(Object object) {  
	       HashMap<String, Object> data = new HashMap<String, Object>();  
	       // 将json字符串转换成jsonObject  
	       JSONObject jsonObject = JSONObject.fromObject(object);  
	       Iterator it = jsonObject.keys();  
	       // 遍历jsonObject数据，添加到Map对象  
	       while (it.hasNext()){  
	           String key = String.valueOf(it.next());  
	           Object value = jsonObject.get(key);  
	           data.put(key, value);  
	       }  
	       return data;  
	   }  
	
	
	@SuppressWarnings("rawtypes")
	public static HashMap<String, String> toHashMap(Object object) {  
	       HashMap<String, String> data = new HashMap<String, String>();  
	       // 将json字符串转换成jsonObject  
	       JSONObject jsonObject = JSONObject.fromObject(object);  
	       Iterator it = jsonObject.keys();  
	       // 遍历jsonObject数据，添加到Map对象  
	       while (it.hasNext())  
	       {  
	           String key = String.valueOf(it.next());  
	           String value = (String) jsonObject.get(key);  
	           data.put(key, value);  
	       }  
	       return data;  
	   }  
	/****************************************************************************************/

	
//	public static Map<String, Object> get(String version, String code, Integer port, Double money3, elec time, elec elec) {
//		Map<String, Object> resultdata = new HashMap<String, Object>();
//		try {
//			if ("07".equals(version) || "08".equals(version)) {
//				logger.info("port=" + port + "--money3=" + money3 + "--time=" + time + "--elec=" + elec + "--code" + code);
//				SendMsgUtil.send_0x27((byte)port, (short)(money3 / 10), (short)time, (short)elec, code, (byte)1);
//				if ("08".equals(version)) {
//					double clacMoney = money;
//					if (clacMoney == 0) {
//						clacMoney = user.getBalance();
//					}
//					SendMsgUtil.resetChargeData(code, port, uid, clacMoney, 0);
//				}
////				WolfHttpRequest.sendNewChargePaydata(port, time, money/10 + "", chargeRecord.getQuantity() + "", code, 1, 0);
//			} else {
//				SendMsgUtil.send_0x14(portchoose, (short) (money3 / 10), time, elec, code);// 支付完成充电开始
////				WolfHttpRequest.sendChargePaydata(port, time, money/10 + "", chargeRecord.getQuantity() + "", code, 0);
//			}
//			Timer timer = new Timer();
//			long session_id = System.currentTimeMillis();
//			timer.schedule(new TimerTask() {
//				@Override
//				public void run() {
//					chargepayTask(code, (byte) port, chargeRecord.getDurationtime(), chargeRecord.getQuantity(), session_id, format);
//				}
//			}, 60000);
//			Server.chargeTimerNumMap.put(session_id, 2);
//			System.out.println("定时任务已开启");
//		} catch (Exception e) {
//			StackTraceElement[] stackTrace = e.getStackTrace();
//			logger.warn("设备编号：--" + code + "--设备端口：--" + port + "--充电异常---" + stackTrace[0].getLineNumber());
//			e.printStackTrace();
//		}
//		return resultdata;
//	}
//	
	
//	@SuppressWarnings("unchecked")
//	public static void addPortStatus(int len,Map<String,String> codeRedisMap,List<Map<String,String>> portStatus) {
//		for (int i = 1; i < len + 1; i++) {
//			String portStatusStr = codeRedisMap.get(i + "");
//			Map<String,String> portStatusMap = (Map<String, String>) JSON.parse(portStatusStr);
//			
//			portStatus.add(portStatusMap);
//		}
//	}
	
	public static Map<String, Object> historyStatisticsData(String devicenum, String hardversion) {
		Map<String, Object> resultdata = new HashMap<String, Object>();
		try {
			Map<String, String> codeRedisMap = JedisUtils.hgetAll(devicenum);
			List<Map<String, String>> portStatus = new ArrayList<>();
			if (codeRedisMap != null) {
				if ("02".equals(hardversion)) {
					StringUtil.addPortStatus(2, codeRedisMap, portStatus);
				} else if ("01".equals(hardversion)) {
					StringUtil.addPortStatus(10, codeRedisMap, portStatus);
				} else if ("05".equals(hardversion)) {
					StringUtil.addPortStatus(16, codeRedisMap, portStatus);
				} else if ("06".equals(hardversion)) {
					StringUtil.addPortStatus(20, codeRedisMap, portStatus);
				} else {
					StringUtil.addPortStatus(10, codeRedisMap, portStatus);
				}
			}
			if (portStatus.size() > 0) {
				resultdata.put("allPortSize", portStatus.size());
				Map<String, String> map = portStatus.get(0);
				String updateTimeStr = map.get("updateTime");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long time = sdf.parse(updateTimeStr).getTime();
//						long time = portStatus.get(0).getUpdateTime().getTime(); 
				long currentTime = System.currentTimeMillis();
				if ((currentTime - time) > 300000) {
					resultdata.put("flag", true);
				} else {
					resultdata.put("flag", false);
				}
				resultdata.put("portList", portStatus);
			} else {
				resultdata.put("allPortSize", 0);
				resultdata.put("flag", true);
				resultdata.put("portList", null);
			}
			CommUtil.responseBuildInfo(200, "成功", resultdata);
		} catch (Exception e) {
			e.printStackTrace();
			CommUtil.responseBuildInfo(3000, "异常错误", resultdata);
		}
		return resultdata;
	}
	
    /**
     * 验证是否是正确合法的手机号码
     * 
     * @param telephone
     *            需要验证的打手机号码
     * @return 合法返回true，不合法返回false
     * */
    public static boolean isCellPhoneNo(String telephone) {
        if (CommUtil.trimToEmpty(telephone)!="") {
            return false;
        }
        if (telephone.length() != 11) {
            return false;
        }
        Pattern pattern = Pattern.compile("^1[3,5]\\d{9}||18[6,8,9]\\d{8}$");
        Matcher matcher = pattern.matcher(telephone);
 
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
    
	/**
	 * 通用判断
	 * @param telNum
	 * @return
	 */
	public static boolean isMobiPhoneNum(String telNum){
		String regex = "^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(telNum);
        return m.matches();
	}
	
	/**
	 * 更严格的判断
	 * @param mobiles
	 * @return  /^1[3|4|5|7|8]\d{9}$/
	 */
	public static boolean isMobileNum(String telNum){
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(telNum);
		return m.matches();
	}
	
	

    /**
     * 大陆号码或香港号码都可以
     * @param str
     * @return 符合规则返回true
     * @throws PatternSyntaxException
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     *
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147,149
     * 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的)
     * 166
     * 17+3,5,6,7,8
     * 18+任意数
     * 198,199
     * @param str
     * @return 正确返回true
     * @throws PatternSyntaxException
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     * @param str
     * @return 正确返回true
     * @throws PatternSyntaxException
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

	

}
