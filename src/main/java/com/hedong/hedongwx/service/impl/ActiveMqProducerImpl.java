package com.hedong.hedongwx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.service.ActiveMqProducer;

@Service("producer")
public class ActiveMqProducerImpl implements ActiveMqProducer {

	@Autowired  
	private JmsTemplate jmsTemplate;
	
	public void sendMessage(String destination, String str){
		System.out.println("activeMq发送信息：" + str);
		jmsTemplate.convertAndSend("inqueueName", str);
//		jmsTemplate.convertAndSend(str);
	}  

//	@JmsListener(destination="outqueueName")  
//	public void consumerMessage(String text){
//		number++;
//	    System.out.println("从outqueueName队列收到的回复报文为第" + number + "条:"+text);
//	} 
}
