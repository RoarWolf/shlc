package com.hedong.hedongwx.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;

public class StringUtil {
	
	
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
	
	public static byte checkoutSum(byte[] array) {
		byte v = 0;
		for (int i = 0; i < array.length; i++) {
			v ^= array[i];
		}
		return v;
	}
	
	/**
	 * 将Map添加到list中
	 * @param len
	 * @param codeRedisMap
	 * @param portStatus
	 */
	public static void addPortStatus(int len,Map<String,String> codeRedisMap,List<Map<String,String>> portStatus) {
		for (int i = 1; i < len + 1; i++) {
			String portStatusStr = codeRedisMap.get(i + "");
			Map<String,String> portStatusMap = (Map<String, String>) JSON.parse(portStatusStr);
			portStatus.add(portStatusMap);
		}
	}
	
	//String转Integer
	public static Integer getStringInt(String value) {
		if (isNull(value))
			return null;
		return Integer.parseInt(value);
	}
	
	//String转Integer
	public static Integer getIntString(String value) {
		if (isNull(value))
			return 0;
		return Integer.parseInt(value);
	}
	
	//String转Double
	public static Double getDoubleString(String value) {
		if (isNull(value))
			return 0.00d;
		return Double.parseDouble(value);
	}
	
	//获取六位随机数字
    public static String getRandNum() {
        String randNum = new Random().nextInt(1000000)+"";
        if (randNum.length()!=6) {   //如果生成的不是6位数随机数则返回该方法继续生成
            return getRandNum();
        }
        return randNum;
    }
    //这个方法我也没有看懂
	public static String doubleToString(Double d) {
		String money1 = String.valueOf(d);
		int idx = money1.lastIndexOf(".");
		String total_fee = money1.substring(0, idx);
		return total_fee;
	}
    
    //long型毫秒时间比较，返回分钟  
	public static int comparTime(long firsttime, long lastTime){
		long comparTime = firsttime - lastTime;
		int time = (int) (comparTime/(1000*60));
		return time;
	}
	
	// --------------------------------------日期相关的字符串处理-------------------------------------
	// 按指定格式输出当前日期时间字符
	public static String toDateTime(String formatString) {
		if (StringUtil.isNull(formatString))
			return "";
		Date d = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(formatString);
		return formatter.format(d);
	}
	
	// 按指定格式输出日期时间字符
	public static String toDateTime(String formatString, Date d) {
		if (StringUtil.isNull(formatString))
			return "";
		if (d == null)
			return "";

		SimpleDateFormat formatter = new SimpleDateFormat(formatString);
		return formatter.format(d);
	}
	
	// 返回日期时间Date 如2010-09-18 00:00:00
	public static Date DateTime(String time, String formatString){ 
		if (StringUtil.isNull(time))
		return null;
		if (time.equals(""))
		return null;
		SimpleDateFormat formatter = new SimpleDateFormat(formatString);
		try {
			return formatter.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	// 获取当前日期时间值         返回当前日期时间字符 如2010-09-18 14:12:15
	public static String getCurrentDateTime() {
		return toDateTime(new Date());
	}
	
	// 获取当前日期时间值         返回当前日期时间字符 如2010-09-18 14:12:15
	public static String toDateTime() {
		return toDateTime("yyyy-MM-dd HH:mm:ss");
	}
		
	// 返回日期时间字符 如2010-09-18 14:12:15
	public static String toDateTime(Date d) {
		return toDateTime("yyyy-MM-dd HH:mm:ss", d);
	}
	
	// 返回日期时间Date 如2010-09-18 00:00:00
	public static String beforeStringTime(String time){
		if (StringUtil.isNull(time)) return null;
		if (time.equals("")) return null;
		time = time + " 00:00:00";
		return time;
	}
	
	// 返回日期时间字符 如2010-09-18 23:59:59
	public static String aftertoStringTime(String time) {
		if (StringUtil.isNull(time)) return null;
		if (time.equals("")) return null;
		time = time + " 23:59:59";
		return time;
	}
	
	// 返回日期时间Date 如2010-09-18 00:00:00
	public static Date beforeDateTime(String time){ 
		return DateTime(beforeStringTime(time), "yyyy-MM-dd 00:00:00");
	}
	
	// 返回日期时间字符 如2010-09-18 23:59:59
	public static Date aftertoDateTime(String time) {
		return DateTime(aftertoStringTime(time), "yyyy-MM-dd 23:59:59");
	}
	
	//将long型的长整形时间转换为yyyy-MM-dd HH:mm:ss格式 （ 2010-09-18 14:12:15 ）
	public static String toDateTime(long t){
		Date d = new Date(t);
		return toDateTime("yyyy-MM-dd HH:mm:ss", d);
	}
	
	public static Integer minuteTime(Date begintime, Date endtime){
		long time = (endtime.getTime() - begintime.getTime())/1000;
		int minute = (int) (time/60);
		return minute;
	}
	
	//--------------------------------------比较处理 返回int类型数据 -------------------------------------
//-----------------------------------------------------------------------------------------------------	
	
	
	public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
		try {
			Calendar date = Calendar.getInstance();
			date.setTime(nowTime);
			Calendar begin = Calendar.getInstance();
			begin.setTime(beginTime);
			Calendar end = Calendar.getInstance();
			end.setTime(endTime);
			if (date.after(begin) && date.before(end)) {
				return true;
			} else if (nowTime.compareTo(beginTime) == 0 || nowTime.compareTo(endTime) == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			return false;
		}
	}
	
	//比较两个时间的大小  yyyy-MM-dd HH:mm:ss 类型的时间
    public static int compareStringTime(String startdate, String enddate) {
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
    
    //两个long型数据的比较
    public static int compare_longdate(long startdate, long enddate){
    	if (startdate < enddate) {
    		return 1;
    	} else if (startdate > enddate) {
    		return -1;
    	} else {
    		return 0;
    	}
    }
    
    //两个String型数据使用的比较
    public static int compareTo(String start, String end){
    	int num = start.compareTo(end);
		if(num>0) return 1;
		else if(num<0) return -1;
		else if(num==0) return 0;
		return num;
    }
    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static Date getPastTime(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        return today;
    }
    
	/**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }
    
   // java获取当前时间的前n天日期
  	public static String getnumterday(String format, String time, int num){
  		Calendar c = Calendar.getInstance(); 
  		Date date=null; 
  		try { 
  			date = new SimpleDateFormat(format).parse(time); 
  		} catch (ParseException e) { 
  			e.printStackTrace(); 
  		} 
  		c.setTime(date); 
  		int day=c.get(Calendar.DATE); 
  		c.set(Calendar.DATE,day-num); 

  		String dayBefore=new SimpleDateFormat(format).format(c.getTime()); 
  		return dayBefore; 
  	}	
    /******************************************************************************************************/
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
 
    public static String StringNumer(String number){
    	try {
    		StringBuilder mark = new StringBuilder("00000000");
    		int marknum = mark.length();
    		int num = number.length();
    		String numerical = mark.replace(marknum - num, marknum, number).toString();
    		return numerical;
		} catch (Exception e) {
			e.toString();
			return "00000000";
		}
    } 
    
    /**
     * @Description： Java中判断字符串是否为数字
     * @author： origin          
     * 创建时间：   2019年6月5日 上午9:37:41
     */
    public static boolean isNumeric(String str){
    	if(str==null || str.trim().equals("")) {
    		return false;
    	}
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();   
    }
    
	/**
	 * @Description： 计算合伙人与商户分别收益的计算处理
	 * @author： origin 创建时间：2020年5月22日下午4:46:55
	 */
	public static Map<String, Object> percentCalculateDispose(List<Map<String, Object>> partInfo, Integer merid, Double money) {
		Double mermoney = 0.00;//商户金额
		Double partmoney = 0.00;//合伙人金额
		Double tolpercent = 0.00;//分成比
		Map<String, Object> mapresult = new HashMap<>();
		List<Map<String, Object>> percentinfo = new ArrayList<>();
		try {
			if(partInfo.size()>0){//分成
				System.out.println("输出分成");
				for(Map<String, Object> item : partInfo){
					Map<String, Object> mappartner = new HashMap<>();
					Integer partid = CommUtil.toInteger(item.get("partid"));
					Double percent = CommUtil.toDouble(item.get("percent"));
					Double partnetmoney = CommUtil.toDouble((money * (percent*100))/100);
					mappartner.put("partid", partid);
					mappartner.put("percent", percent);
					mappartner.put("money", partnetmoney);
					percentinfo.add(mappartner);
					tolpercent = tolpercent + percent;
				}
			}
			mermoney = CommUtil.toDouble(money * (1- tolpercent));
			partmoney = CommUtil.subBig(money, mermoney);
			Map<String, Object> mermap = new HashMap<>();
			mermap.put("merid", merid);
			mermap.put("percent", (1-tolpercent));
			mermap.put("money", mermoney);
			percentinfo.add(mermap);
			mapresult.put("mermoney", mermoney);
			mapresult.put("partmoney", partmoney);
			mapresult.put("percentinfo", percentinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapresult;
		}
}
	
