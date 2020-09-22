package com.hedong.hedongwx.service;

public interface ActiveMqProducer {

	void sendMessage(String destination, String str);
}
