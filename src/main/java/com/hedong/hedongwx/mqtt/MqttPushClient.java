package com.hedong.hedongwx.mqtt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.utils.PropertiesUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttPushClient {

    private MqttClient client;
    
    private static Map<String, MqttClient> map = new HashMap<>();

    private static volatile MqttPushClient mqttPushClient = null;
    private static Logger log = LoggerFactory.getLogger(MqttPushClient.class);

    public static MqttPushClient getInstance(){

        if(null == mqttPushClient){
            synchronized (MqttPushClient.class){
                if(null == mqttPushClient){
                    mqttPushClient = new MqttPushClient();
                }
            }

        }
        return mqttPushClient;

    }

    private MqttPushClient() {
        connect();
    }

    public static void connect(){
        try {
        	MqttClient client = new MqttClient(PropertiesUtil.MQTT_HOST, PropertiesUtil.MQTT_CLIENTID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName(PropertiesUtil.MQTT_USER_NAME);
            options.setPassword(PropertiesUtil.MQTT_PASSWORD.toCharArray());
            options.setConnectionTimeout(PropertiesUtil.MQTT_TIMEOUT);
            options.setKeepAliveInterval(PropertiesUtil.MQTT_KEEP_ALIVE);
            try {
                client.setCallback(new PushCallback());
                client.connect(options);
                System.out.println("mqtt已连接");
                map.put("wolf1", client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布，默认qos为0，非持久化
     * @param topic
     * @param pushMessage
     */
    public void publish(String topic,PushPayload pushMessage){
        publish(0, false, topic, pushMessage);
    }

    /**
     * 发布
     * @param qos
     * @param retained
     * @param topic
     * @param pushMessage
     */
    public void publish(int qos,boolean retained,String topic,PushPayload pushMessage){
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.toString().getBytes());
        MqttTopic mTopic = client.getTopic(topic);
        if(null == mTopic){
            log.error("topic not exist");
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

//	发布消息
	public static void publishMessage(String pubTopic,String message,int qos) {
		MqttClient client = map.get("wolf1");
		if(null != client&& client.isConnected()) {
//			System.out.println("发布消息   "+client.isConnected());
//			System.out.println("id:"+client.getClientId());
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setQos(qos);
			mqttMessage.setPayload(message.getBytes());
			
			MqttTopic topic = client.getTopic(pubTopic);
			
			if(null != topic) {
				try {
					MqttDeliveryToken publish = topic.publish(mqttMessage);
					if(!publish.isComplete()) {
						System.out.println("消息发布成功");
					}
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}else {
			connect();
		}
		
	}

    /**
     * 订阅某个主题，qos默认为0
     * @param topic
     */
    public void subscribe(String topic){
        subscribe(topic,0);
    }

    /**
     * 订阅某个主题
     * @param topic
     * @param qos
     */
    public void subscribe(String topic,int qos){
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendUpgradeData() {
    	String kdTopic = "good";
    	FileReader fileReader = null;
        FileInputStream fileInputStream = null;
        try {
			File file = new File("/usr/local/uploadfile/HD7KW.bin");
//			File file = new File("C:/Users/Lenovo/Desktop/HD7KW.bin");

			fileReader = new FileReader(file);

			fileInputStream = new FileInputStream(file);

			byte[] bytes = new byte[1024];
			char[] chars = new char[1024];
			int i;
			//字节流读取
			while ((i = fileInputStream.read(bytes)) != -1) {
				StringBuffer sb = new StringBuffer();
				for (int i1 = 0;i1 < i; i1++) {
					String hexByte = Integer.toHexString(bytes[i1] & 0xff);
					if (hexByte.length() < 2) {
						hexByte = "0" + hexByte;
					}
					System.out.print(hexByte + ",");
					sb.append(hexByte);
				}
				if (bytes.length%2 == 1) {
					sb.append("ff");
				}
				publishMessage(kdTopic, sb.toString(), 2);
				Thread.sleep(1000);
				System.out.println(System.currentTimeMillis());
			} 
			System.out.println("数据发送完毕");
		} catch (Exception e) {
		} finally {
			try {
				fileInputStream.close();
				fileReader.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
    }


    public static void main(String[] args) throws Exception {
        String kdTopic = "good";
//        PushPayload pushMessage = PushPayload.getPushPayloadBuider().setMobile("15345715326")
//                 .setContent("designModel")
//                .bulid();
//        MqttPushClient instance = MqttPushClient.getInstance();
        connect();
//        sendUpgradeData();
//        instance.publish(0, false, kdTopic, pushMessage);
//        instance.publishMessage(kdTopic, "like", 0);
//        FileReader fileReader = null;
//        FileInputStream fileInputStream = null;
//        try {
//			File file = new File("C:/Users/Lenovo/Desktop/HD7KW.bin");

//			fileReader = new FileReader(file);

//			fileInputStream = new FileInputStream(file);

//			byte[] bytes = new byte[16];
//			char[] chars = new char[1024];
//			int i;
			//字节流读取
//			while ((i = fileInputStream.read(bytes)) != -1) {
//				StringBuffer sb = new StringBuffer();
//				for (int i1 = 0;i1 < i; i1++) {
//					String hexByte = Integer.toHexString(bytes[i1] & 0xff);
//					if (hexByte.length() < 2) {
//						hexByte = "0" + hexByte;
//					}
//					System.out.println("hexByte===" + hexByte);
//					sb.append(hexByte);
//				}
//				if (bytes.length%2 == 1) {
//					sb.append("ff");
//				}
//				publishMessage(kdTopic, sb.toString(), 2);
//				Thread.sleep(100);
//				System.out.println(System.currentTimeMillis());
//			} 
//			System.out.println("数据发送完毕");
//		} catch (Exception e) {
//		} finally {
//			try {
//				fileInputStream.close();
//				fileReader.close();
//			} catch (Exception e2) {
				// TODO: handle exception
//			}
//		}
//        MqttClient mqttClient = map.get("wolf1");
//        mqttClient.disconnect();
    }
    
    public static void getByteString() {
    	
    }
}
