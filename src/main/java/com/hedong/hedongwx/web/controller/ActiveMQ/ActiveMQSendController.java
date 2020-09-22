package com.hedong.hedongwx.web.controller.ActiveMQ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hedong.hedongwx.service.ActiveMqProducer;
/**
 * 
 * @author RoarWolf
 *
 */
@Controller
@RequestMapping("wolfmqsend")
public class ActiveMQSendController {

	@Autowired
	private JmsMessagingTemplate jmsTemplate;
	@Autowired
	private ActiveMqProducer activeMqProducer;
	
	@RequestMapping("sendData")
	@ResponseBody
	public String sendData(String userid) throws InterruptedException {
//		ByteBuffer send_11 = SendMsgUtil.send_11();
		for (int i = 0; i < 10; i++) {
			System.out.println("i=" + i);
			activeMqProducer.sendMessage(userid, i + "山");
			Thread.sleep(1000);
		}
		return "1";
	}
}
