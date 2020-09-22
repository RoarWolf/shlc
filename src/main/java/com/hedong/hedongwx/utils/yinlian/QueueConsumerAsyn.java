package com.hedong.hedongwx.utils.yinlian;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 异步消费
 * @author zz
 *
 */
public class QueueConsumerAsyn {

	public static void main(String[] args) {
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.3.2:61616");
		try {
			// 获取连接
			Connection connection = factory.createConnection();
			// 打开连接
			connection.start();
			// 创建会话
			Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			// 创建队列
			Queue queue = session.createQueue("my");
			// 创建一个消费者
			MessageConsumer consumer = session.createConsumer(queue);
			consumer.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message) {
					if(message instanceof TextMessage){
						String text = "";
						try {
							text = ((TextMessage) message).getText();
						} catch (Exception e) {
							e.printStackTrace();
						}
						System.out.println("异步消费=="+text);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
