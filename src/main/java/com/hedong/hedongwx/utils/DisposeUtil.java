package com.hedong.hedongwx.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.thread.Server;

/**
 * @author RoarWolf
 */
public class DisposeUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	public static void main(String[] args) {
//		byte[] bytes = new byte[]{(byte) 0xA6,(byte) 0xBB,0x41,0x31,0x32,0x33,0x34,0x35,0x36};
//		System.out.println(handleCarnum(bytes).trim());
	}
	
	public static List<String> quChong(List<String> list) {
	    List<String> newList  = new ArrayList<String>();
	    for (String str : list) {
	        if(!newList.contains(str))
	            newList.add(str);
	    }
	    return newList;
	}
	
	public static User getUserBySessionId(HttpServletRequest request) {
		System.out.println("获取用户=" + request.getSession().getId());
//		String userStr = JedisUtils.getnum(request.getSession().getId(), 2);
//		User user = JSONObject.toJavaObject((JSON)JSON.parse(userStr), User.class);
		User user = (User) request.getSession().getAttribute("user");
		return user;
	}
	
	public static void setUserBySessionId(String sessionId,User user) {
		JedisUtils.setnum(sessionId, JSON.toJSONString(user), 28800, 2);
	}
	
	/**
	 * 判断此map是否有值
	 * @param map
	 * @return
	 */
	public static boolean checkMapIfHasValue(Map map) {
		if (map == null || map.isEmpty() || map.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 获取判断设备断网时间
	 * @param temp 1、获取系统时间   2、获取手动断电时间
	 * @return
	 */
	public static int getOfflineTime(int temp) {
		try {
			Map<String, String> hgetAll = JedisUtils.hgetAll("sysTime");
			if (!checkMapIfHasValue(hgetAll)) {
				return noChongfu(temp);
			} else {
				if (temp == 1) {
					String sysOffline = hgetAll.get("sysOffline");
					if (sysOffline == null || "".equals(sysOffline)) {
						return 8 * 60;
					} else {
						return Integer.parseInt(sysOffline);
					}
				} else if (temp == 2) {
					String handOffline = hgetAll.get("handOffline");
					if (handOffline == null || "".equals(handOffline)) {
						return 30;
					} else {
						return Integer.parseInt(handOffline);
					}
				} else {
					return noChongfu(temp);
				}
			} 
		} catch (Exception e) {
			return noChongfu(temp);
		}
	}
	
	public static int noChongfu(int temp) {
		if (temp == 1) {
			return 8 * 60;
		} else {
			return 30;
		}
	}
	
	public static boolean checkTimeIfExceed(String code,int temp) {
		Map<String, String> diviceOffline = JedisUtils.hgetAll("diviceOffline");
		if (checkMapIfHasValue(diviceOffline)) {
			return false;
		} else {
			String updateTime = diviceOffline.get(code);
			if (updateTime == null || "".equals(updateTime)) {
				return false;
			} else {
				long currentTime = System.currentTimeMillis();
				long updateTimeLong = CommUtil.DateTime(updateTime,"yyyy-MM-dd HH:mm:ss").getTime();
				long differTime = currentTime - updateTimeLong;
				int offlineTime = getOfflineTime(temp);
				if (offlineTime > differTime) {
					return false;
				} else {
					return true;
				}
			}
		}
	}
	
	/**
    * @Description：获取某个目录下所有直接下级文件，不包括目录下的子目录的下的文件，所以不用递归获取
    * @Date：2020/4/29
    */
    public static List<String> getFiles(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].toString());
                //文件名，不包含路径
                String fileName = tempList[i].getName();
                long lastModified = tempList[i].lastModified();
                Date date = new Date(lastModified);
                String format = CommUtil.toDateTime("yyyy-MM-dd", date);
                System.out.println(format);
                String wolfDate = "2020-06-01";
                System.out.println(wolfDate.compareTo(format));
//                if ("新标签1.pld".equals(fileName)) {
//                	boolean delete = tempList[i].delete();
//                	if (delete) {
//                		System.out.println("新标签1.pld删除成功");
//                	} else {
//                		System.out.println("新标签1.pld删除失败");
//                	}
//                }
                System.out.println("---" + fileName);
            }
            if (tempList[i].isDirectory()) {
                //这里就不递归了，
            }
        }
        return files;
    }
    
    /**
     * @Description：删除多余的日志文件
     * @Date：2020/4/29
     */
    public static List<String> delectFile(String path, String wolfTime) {
    	List<String> files = new ArrayList<String>();
    	File file = new File(path);
    	File[] tempList = file.listFiles();
    	for (int i = 0; i < tempList.length; i++) {
    		if (tempList[i].isFile()) {
    			files.add(tempList[i].toString());
    			//文件名，不包含路径
    			String fileName = tempList[i].getName();
    			System.out.println("filename===" + fileName);
    			long lastModified = tempList[i].lastModified();
    			Date date = new Date(lastModified);
                String fileTime = CommUtil.toDateTime("yyyy-MM-dd", date);
                int compareTo = fileTime.compareTo(wolfTime);
                System.out.println("当前时间：" + wolfTime);
                System.out.println("文件时间：" + fileTime);
                System.out.println("时间比较：" + compareTo);
                if (compareTo < 0) {
                	boolean delete = tempList[i].delete();
                	if (delete) {
                		System.out.println(fileName + "删除成功");
                	} else {
                		System.out.println(fileName + "删除失败");
                	}
                }
    		}
//    		if (tempList[i].isDirectory()) {
    			//这里就不递归了，
//    		}
    	}
    	return files;
    }
    
    /**
     * 获取redis中所有端口状态数据
     * @param code
     * @param hardversion
     * @return
     */
    public static List<Map<String, String>> addPortStatus(String code,String hardversion) {
    	List<Map<String, String>> portStatus = new ArrayList<>();
		Map<String, String> codeRedisMap = JedisUtils.hgetAll(code);
		int len = 10;
		if (codeRedisMap != null) {
			if ("02".equals(hardversion)) {
				len = 2;
			} else if ("01".equals(hardversion)) {
				len = 10;
			} else if ("05".equals(hardversion)) {
				len = 16;
			} else if ("06".equals(hardversion)) {
				len = 20;
			} else if ("07".equals(hardversion)) {
				len = 1;
			}
		}
		for (int i = 1; i < len + 1; i++) {
			try {
				String portStatusStr = codeRedisMap.get(i + "");
				Map<String, String> portStatusMap = (Map<String, String>) JSON.parse(portStatusStr);
				portStatus.add(portStatusMap);
			} catch (Exception e) {
				break;
			}
		}
		return portStatus;
	}
    
    /**
     * 获取redis中端口状态数据
     * @param code
     * @param hardversion
     * @return
     */
    public static Map<String, String> addPortStatus(String code,int port) {
    	Map<String, String> codeRedisMap = JedisUtils.hgetAll(code);
		try {
			String portStatusStr = codeRedisMap.get(port + "");
			Map<String, String> portStatusMap = (Map<String, String>) JSON.parse(portStatusStr);
			return portStatusMap;
		} catch (Exception e) {
			return null;
		}
    }
    
    /**
     * 获取redis中端口状态数据
     * @param code
     * @param hardversion
     * @return
     */
    /**
     * 获取redis中端口状态数据
     * @param code
     * @param hardversion
     * @return
     */
    public static List<Integer> getPortStatusInt(String code,String hardversion) {
    	List<Integer> portStatus = new ArrayList<>();
    	Map<String, String> codeRedisMap = JedisUtils.hgetAll(code);
    	int len = 10;
    	if (codeRedisMap != null) {
    		if ("02".equals(hardversion) || "09".equals(hardversion)) {
    			len = 2;
    		} else if ("01".equals(hardversion)) {
    			len = 10;
    		} else if ("05".equals(hardversion)) {
    			len = 16;
    		} else if ("06".equals(hardversion) || "10".equals(hardversion)) {
    			len = 20;
    		}
    	}
    	for (int i = 1; i < len + 1; i++) {
    		Integer temp = null;
    		try {
    			String portStatusStr = codeRedisMap.get(i + "");
				Map<String, String> portStatusMap = (Map<String, String>) JSON.parse(portStatusStr);
				String string = portStatusMap.get("portStatus");
				temp = Integer.parseInt(string);
			} catch (Exception e) {
			}
    		if (temp != null) {
    			portStatus.add(temp);
    		}
    	}
    	return portStatus;
    }
    
    public static void logfile(String sWord) {
    	 FileWriter writer = null;
         try {
             writer = new FileWriter("D:/log/chargeCodeAndPort.log", true);
             writer.write(sWord + "\n");
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             if (writer != null) {
                 try {
                     writer.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }
    }
    
    /**
	 * 获取过去的日期
	 *
	 * @param past 具体数值
	 * @param flag 1、获取过去几天2、获取过去几月3、获取过去几年
	 * @return
	 */
	public static String getPastDate(int past,int flag) {
		Calendar calendar = Calendar.getInstance();
		int param = Calendar.DAY_OF_YEAR;
		if (flag == 1) {
			param = Calendar.DAY_OF_YEAR;
		} else if (flag == 2) {
			param = Calendar.MONTH;
		} else if (flag == 3) {
			param = Calendar.YEAR;
			
		}
		calendar.set(param, calendar.get(param) - past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String result = format.format(today);
		return result;
	}
	
	/**
	 * 根据版本号检测设备是否为v3设备
	 * @param hardversion
	 * @return
	 */
	public static boolean checkIfHasV3(String hardversion) {
		boolean flag = false;
		if ("08".equals(hardversion) || "09".equals(hardversion) || "10".equals(hardversion)) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 补全字符串所需的0，前补0，用于10进制转16进制位不够
	 * @param param 需要改动的字符串
	 * @param totalnum 总位数
	 * @return
	 */
	public static String completeNum(String param, int totalnum) {
		int length = param.length();
		if (length < totalnum) {
			String needStr = "";
			for (int i = length; i < totalnum; i++) {
				needStr += "0";
			}
			param = needStr + param;
		}
		return param;
	}
	
	public static void printDeviceDataInfo(String code, ByteBuffer buffer, boolean flag) {
		if (code != null && !"".equals(code)) {
			StringBuffer sb = new StringBuffer();
			while (buffer.hasRemaining()) {
				int b = buffer.get() & 0xff;
				String hexString = Integer.toHexString(b);
				if (hexString.length() == 1) {
					hexString = "0" + hexString;
				}
				sb.append(hexString);
			}
			String str = "";
			if (flag) {
				str = "接收设备上传指令：设备编号为-" + code + ": " + sb.toString();
			} else {
				str = "服务器发送指令：设备编号为-" + code + ": " + sb.toString();
			}
			logger.info(str);
			// logFile.logResult(logFile.DEVICEPATH, code, CommUtil.toDateTime()
			// + str);
		}
	}

	/**
	 * 补全字符串所需的0，前补0，用于10进制转16进制位不够
	 * 
	 * @param paramInt
	 *            需要转16进制的10进制
	 * @param totalnum
	 *            总位数
	 * @return
	 */
	public static String completeNumIntHex(int paramInt, int totalnum) {
		String param = intToHex(paramInt);
		int length = param.length();
		if (length < totalnum) {
			String needStr = "";
			for (int i = length; i < totalnum; i++) {
				needStr += "0";
			}
			param = needStr + param;
		}
		return param;
	}

	public static String intToHex(int i) {
		return Integer.toHexString(i);
	}

	public static int hexToInt(String str) {
		return Integer.parseInt(str, 16);
	}

	public static String disposeDate(byte[] dateBytes) {
		String year_str = completeNum(intToHex(dateBytes[0] & 0xff), 2);// 年只有0-99，需转成正常年，如20转为2020
		String month_str = completeNum(intToHex(dateBytes[1] & 0xff), 2);// 月
		String day_str = completeNum(intToHex(dateBytes[2] & 0xff), 2);// 日
		String hour_str = completeNum(intToHex(dateBytes[3] & 0xff), 2);// 时
		String minute_str = completeNum(intToHex(dateBytes[4] & 0xff), 2);// 分
		String second_str = completeNum(intToHex(dateBytes[5] & 0xff), 2);// 秒
		String normal_datestr = "20" + year_str + "-" + month_str + "-" + day_str + " " + hour_str + ":" + minute_str
				+ ":" + second_str;
		return normal_datestr;
	}

	public static String sumStr(String... strings) {
		String str = "";
		for (String string : strings) {
			str += string;
		}
		return str;
	}

	/**
	 * 获取时间中的 年月日时分秒
	 * @param type 1、年  2、月  3、日  4、时  5、分  6、秒
	 * 注：年分只取0-99，也就是2020，只取后面的20，操作需要减去2000
	 * @return
	 */
	public static int getDateTime(int type, int addnum) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime() + addnum * 1000); // 放入Date类型数据

		if (type == 1) {
			return calendar.get(Calendar.YEAR) - 2000; // 获取年份
		} else if (type == 2) {
			return calendar.get(Calendar.MONTH) + 1; // 获取月份
		} else if (type == 3) {
			return calendar.get(Calendar.DATE); // 获取日
		} else if (type == 4) {
			return calendar.get(Calendar.HOUR_OF_DAY); // 时（24小时制）
		} else if (type == 5) {
			return calendar.get(Calendar.MINUTE); // 分
		} else if (type == 6) {
			return calendar.get(Calendar.SECOND);
		} else {
			return 0;
		}
	}
	
	/**
	 * 获取时间
	 * @param type 方法getDateTime中有提到
	 * @param addnum 需要加的值
	 * @return
	 */
	public static byte[] getDateFlag(int type, int addnum) {
		byte[] bytes = new byte[6];
		for (int i = 1; i <= bytes.length; i++) {
			if (type == 6) {
				bytes[i-1] = (byte) getDateTime(i, addnum);
			} else {
				bytes[i-1] = (byte) getDateTime(i, 0);
			}
		}
		return bytes;
	}
	
	/**
	 * 将设备编号的16进制转为10进制
	 * @param devicenum
	 * @return
	 */
	public static byte[] disposeDevicenum(String devicenum) {
		byte[] bytes = new byte[8];
		for (int i = 0; i < devicenum.length(); i+=2) {
			String substring = devicenum.substring(i, i+2);
			bytes[i] = (byte) hexToInt(substring);
		}
		return bytes;
	}
	
	/**
	 * 补全byte数组
	 * @param bytes
	 * @param num
	 * @return
	 */
	public static byte[] complateBytes(byte[] bytes, int num) {
		byte[] bytesNum = new byte[num];
		for (int i = 0; i < bytes.length; i++) {
			bytesNum[i] = bytes[i];
		}
		return bytesNum;
	}
	
	/**
	 * 将double转为byte数组
	 * @param d
	 * @return
	 */
	public static byte[] double2Bytes(double d) {
		long value = Double.doubleToRawLongBits(d);
		byte[] byteRet = new byte[8];
		for (int i = 0; i < 8; i++) {
			byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
		}		
		return byteRet;
	}
	
	/**
	 * 将byte数组转为double
	 * @param bytes
	 * @return
	 */
	public static double bytes2Double(byte[] bytes) {
		String str = "";
		for (byte b : bytes) {
			str += completeNumIntHex(b, 2);
		}
		return (hexToInt(str) + 0.0)/10000;
	}
	
	/**
     * 将16进制转换为二进制
     * 
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
    
    public static String convertPhonenum(String phonenum) {
    	return phonenum.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
    
    /**
     * 将val值按照低字节在前高字节在后的顺序转为byte数组
     * @param val
     * @param num
     * @return
     */
    public static byte[] converIntData(int val, int num) {
    	byte[] bytes = new byte[num];
    	for (int j = 0; j < num; j++) {
			bytes[j] = (byte) (val >>> (8 * j));
		}
    	return bytes;
    }
    
    /**
     * 将byte数组按照低字节在前高字节在后的顺序解析
     * @param bytes
     * @return
     */
    public static int converData(byte[] bytes) {
    	int i = 0;
    	for (int j = 0; j < bytes.length; j++) {
			i += bytes[j] >>> (8 * j) & 0xff;
		}
    	return i;
    }
    
    /**
     * 将int值按照低字节在前高字节在后的顺序解析
     * @param bytes
     * @return
     */
    public static int converIntDataBackInt(int val, int num) {
    	int temp = 0;
    	byte[] bytes = new byte[num];
    	for (int j = 0; j < num; j++) {
    		temp += (byte) (val >>> (8 * j)) & 0xff;
		}
    	return temp;
    }
	
    /**
     *  1- 待机
		2- 等待连接
		3- 启动中
		4- 充电中 
		5- 停止中
		6- 预约中
		7- 占用中
		8- 测试中
		9- 故障中
		10- 定时充电中
		11- 充电完成
		12- 升级中
     * @param val
     * @return
     */
    public static String getPortStatus(int val) {
    	String str = "";
    	switch (val) {
		case 1:
			str = "待机";
			break;
		case 2:
			str = "等待连接";
			break;
		case 3:
			str = "启动中";
			break;
		case 4:
			str = "充电中 ";
			break;
		case 5:
			str = "停止中";
			break;
		case 6:
			str = "预约中";
			break;
		case 7:
			str = "占用中";
			break;
		case 8:
			str = "测试中";
			break;
		case 9:
			str = "故障中";
			break;
		case 10:
			str = "定时充电中";
			break;
		case 11:
			str = "充电完成";
			break;
		case 12:
			str = "升级中";
			break;

		default:
			str = "未知状态";
		}
    	return str;
    }
    
    /**
     *  1- 正常充电
		2- 轮充
		3- 大功率
		4- 超级充
		5- 电池维护
		6- 柔性充
     * @param val
     * @return
     */
    public static String getWorkWayInfo(int val) {
    	String str = "";
    	switch (val) {
    	case 1:
    		str = "正常充电";
    		break;
    	case 2:
    		str = "轮充";
    		break;
    	case 3:
    		str = "大功率";
    		break;
    	case 4:
    		str = "超级充";
    		break;
    	case 5:
    		str = "电池维护";
    		break;
    	case 6:
    		str = "柔性充";
    		break;
    		
    	default:
    		str = "未知状态";
    	}
    	return str;
    }
    
    /**
     * 处理车牌号
     * @param bytes
     * @return
     */
    public static String handleCarnum(byte[] bytes){
    	byte[] carlocalbytes = new byte[2];
    	byte[] carlocalnumbytes = new byte[1];
    	byte[] carnumbytes = new byte[6];
    	carlocalbytes[0] = bytes[1];
    	carlocalbytes[1] = bytes[0];
    	for (int i = 0; i < bytes.length; i++) {
			if (i >= 2 && i < 3) {
				carlocalnumbytes[i-2] = bytes[i];
			} else if (i >= 3) {
				System.out.println("i=" + i);
				carnumbytes[i-3] = bytes[i];
			}
		}
    	String carnum = "0";
		try {
			carnum = new String(carlocalbytes, "GBK") + new String(carlocalnumbytes) + new String(carnumbytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return carnum;
    }
    
}
