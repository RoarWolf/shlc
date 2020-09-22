package com.hedong.hedongwx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class HedongwxApplication extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		setRegisterErrorPageFilter(false);
		return builder.sources(HedongwxApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(HedongwxApplication.class, args);
//		test();
	}
	
//	private static void test(){
//        MqttPushClient.MQTT_HOST = "tcp://mqtt.com:1883";
//        MqttPushClient.MQTT_CLIENTID = "client";
//        MqttPushClient.MQTT_USERNAME = "username";
//        MqttPushClient.MQTT_PASSWORD = "password";
//        MqttPushClient client = MqttPushClient.getInstance();
//        client.subscribe("/#");
//    }
}
