package com.hedong.hedongwx.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.alibaba.fastjson.JSON;

public class PropertiesUtil {

	public static String MQTT_HOST;
	public static String MQTT_CLIENTID;
	public static String MQTT_USER_NAME;
	public static String MQTT_PASSWORD;
	public static int MQTT_TIMEOUT;
	public static int MQTT_KEEP_ALIVE;

//	public static final String ELASTIC_SEARCH_HOST;

//	public static final int ELASTIC_SEARCH_PORT;

//	public static final String ELASTIC_SEARCH_CLUSTER_NAME;

	static {
		 Properties mqttProperties = loadMqttProperties();
		System.out.println("mqtt===" + JSON.toJSONString(mqttProperties));
		MQTT_HOST = loadMqttProperties().getProperty("host");
		MQTT_CLIENTID = HttpRequest.createOrdernum(6);
		MQTT_USER_NAME = loadMqttProperties().getProperty("username");
		MQTT_PASSWORD = loadMqttProperties().getProperty("password");
		MQTT_TIMEOUT = Integer.valueOf(loadMqttProperties().getProperty("timeout"));
		MQTT_KEEP_ALIVE = Integer.valueOf(loadMqttProperties().getProperty("keepalive"));

	}

//	s 

	private static Properties loadMqttProperties() {
		InputStream inputstream = PropertiesUtil.class.getResourceAsStream("/mqtt.yml");
		Properties properties = new Properties();
		try {
			properties.load(inputstream);
			return properties;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (inputstream != null) {
					inputstream.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static Properties loadEsProperties() {
		InputStream inputstream = PropertiesUtil.class.getResourceAsStream("/elasticsearch.properties");
		Properties properties = new Properties();
		try {
			properties.load(inputstream);
			return properties;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (inputstream != null) {
					inputstream.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
