package com.hedong.hedongwx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "com.mqtt")
@Setter
@Getter
public class MqttConfiguration {

    private String host;

    private String clientid;

    private String topic;

    private String username;

    private String password;

    private int timeout;

    private int keepalive;

}
