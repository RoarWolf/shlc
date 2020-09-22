package com.hedong.hedongwx.utils.yinlian;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * MQ同步消费
 * @author zz
 *
 */
public class QueueConsumer {
	
	
	public static void main(String[] args) {
		//创建一个连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.3.2:61616");
		try {
			//创建一个连接
			Connection	connection = connectionFactory.createConnection();
			//打开连接
			connection.start();
			//创建会话
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			//创建一个队列
			Queue queue = session.createQueue("my");
			//创建一个消费者
			MessageConsumer consumer = session.createConsumer(queue);
			while(true){
				//设置接收时间
				Message message = consumer.receiveNoWait();
				if(message != null){
					System.out.println("我收到了消息=="+message);
				}else{
					System.out.println("超时了..............");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
