package com.hedong.hedongwx.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;


/**
 * 〈一句话功能简述〉<br> 
 * 〈MQTT接收消息处理〉
 *
 * @author lenovo
 * @create 2018/6/4
 * @since 1.0.0
 */
public class MqttSenderConfig {
 
    public static String username = "roarwolf";
 
    public static String password = "roarwolf";
 
    public static String hostUrl = "tcp://47.93.203.50:1883";
 
    public static String clientId = "mqttjs_f43e5daed0";
 
    public static String defaultTopic = "good";
 
    public static int completionTimeout = 10 ;   //连接超时
    
    public static int mqtt_keep_alive = 20 ;   //连接超时

}
