package com.hedong.hedongwx.utils;

import java.io.FileWriter;
import java.io.IOException;

import com.hedong.hedongwx.config.AlipayConfig;

public class logFile {
	
	public static final String DEVICEPATH = "/wolfdevicelogs";
	 /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String path, String fileName, String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(path + "/device" + fileName + "-" + CommUtil.toDateTime("yyyy-MM-dd") +".log", true);
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
}
