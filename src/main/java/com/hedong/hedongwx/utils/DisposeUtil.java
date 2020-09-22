package com.hedong.hedongwx.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.thread.Server;

/**
 * 
 * @author RoarWolf
 *
 */
public class DisposeUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	public static void main(String[] args) {
//		for (int i = 310100; i <= 310499; i++) {
//			for (int j = 1; j <= 20; j++) {
//				String str = "http://www.he360.com.cn/oauth2Portpay?codeAndPort=";
//				String str = "";
//				if (j >= 10) {
//					str = str + i + j;
//				} else {
//					str = str + i + "0" + j;
//				}
//				logfile(str);
//				System.out.println(str);
//			}
//		}
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
    
    public static void printDeviceDataInfo(String code, ByteBuffer buffer, boolean flag) {
    	StringBuffer sb = new StringBuffer();
    	while (buffer.hasRemaining()) {
    		int b = buffer.get() & 0xff;
    		String hexString = Integer.toHexString(b);
    		if (hexString.length() == 1) {
    			hexString = "0" + hexString;
    		}
    		sb.append(hexString);
    	}
    	if (code != null && !"".equals(code)) {
    		if (flag) {
    			String str = "接收设备上传指令：设备编号为-" + code + ": " + sb.toString();
//    			logFile.logResult(logFile.DEVICEPATH, code, CommUtil.toDateTime() + str);
    			logger.info(str);
    		} else {
    			String str = "服务器发送指令：设备编号为-" + code + ": " + sb.toString();
//    			logFile.logResult(logFile.DEVICEPATH, code, CommUtil.toDateTime() + str);
    			logger.info(str);
    		}
    	} else {
    		if (flag) {
    			String str = "接收设备上传指令：设备编号为-空" + ": " + sb.toString();
    			logger.info(str);
    		} else {
    			String str = "服务器发送指令：设备编号为-空" + ": " + sb.toString();
    			logger.info(str);
    		}
    	}
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
	
	public static String intToHex(int i) {
		return Integer.toHexString(i);
	}
	
	public static int hexToInt(String str) {
		return Integer.parseInt(str, 16);
	}
}
