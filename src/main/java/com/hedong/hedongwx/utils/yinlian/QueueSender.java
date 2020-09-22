package com.hedong.hedongwx.utils.yinlian;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

/**
 * 消息队列的发送者
 * @author zz
 *
 */
public class QueueSender {
	
	public static void main(String[] args) {
		
		//创建连接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.3.2:61616");
		try {
			//从工厂对象中获得连接
			Connection connection = factory.createConnection();
			//开启一个会话
			Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			//创建一个队列
			Queue queue = session.createQueue("my");
			//创建一个生产者
			MessageProducer producer = session.createProducer(queue);
			//创建消息
			TextMessage message = new ActiveMQTextMessage();
			//写入消息
			while(true){
				message.setText("nihiuahihaihaihiahaihai");
				//发送消息
				producer.send(message);
				System.out.println("生生生生生生生生生生生生生生生生");
			}
			
			
			//producer.close();
			//session.close();
			//connection.close();
			//System.out.println("oooooooooooooooooo");
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
