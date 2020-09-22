package com.hedong.hedongwx.service.impl;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.hedong.hedongwx.utils.DBUtils;

@Component
public class ActiveMqConsumerImpl {
	
	@JmsListener(destination = "inqueueName")
	public void receiveMessage1(String buffer){
		System.out.println("inqueueName1接收" + buffer + buffer);
		if (buffer.contains("insert") || buffer.contains("update") || buffer.contains("delete")) {
			DBUtils.updateSqlData1(buffer);
		}
	}
	
//	@JmsListener(destination = "inqueueName")
//	public void receiveMessage2(String buffer){
//		System.out.println("inqueueName2接收" + buffer);
//		if (buffer.contains("insert") || buffer.contains("update") || buffer.contains("delete")) {
//			DBUtils.updateSqlData2(buffer);
//		}
//	}
//	@JmsListener(destination = "inqueueName")
//	public void receiveMessage3(String buffer){
//		System.out.println("inqueueName3接收" + buffer);
//		if (buffer.contains("insert") || buffer.contains("update") || buffer.contains("delete")) {
//			DBUtils.updateSqlData3(buffer);
//		}
//	}
//	@JmsListener(destination = "inqueueName")
//	public void receiveMessage4(String buffer){
//		System.out.println("inqueueName4接收" + buffer);
//		if (buffer.contains("insert") || buffer.contains("update") || buffer.contains("delete")) {
//			DBUtils.updateSqlData4(buffer);
//		}
//	}
//	@JmsListener(destination = "inqueueName")
//	public void receiveMessage5(String buffer){
//		System.out.println("inqueueName5接收" + buffer);
//		if (buffer.contains("insert") || buffer.contains("update") || buffer.contains("delete")) {
//			DBUtils.updateSqlData5(buffer);
//		}
//	}
//	@JmsListener(destination = "inqueueName")
//	public void receiveMessage6(String buffer){
//		System.out.println("inqueueName6接收" + buffer);
//		if (buffer.contains("insert") || buffer.contains("update") || buffer.contains("delete")) {
//			DBUtils.updateSqlData6(buffer);
//		}
//	}
//	@JmsListener(destination = "inqueueName")
//	public void receiveMessage7(String buffer){
//		System.out.println("inqueueName7接收" + buffer);
//		if (buffer.contains("insert") || buffer.contains("update") || buffer.contains("delete")) {
//			DBUtils.updateSqlData7(buffer);
//		}
//	}
//	@JmsListener(destination = "inqueueName")
//	public void receiveMessage8(String buffer){
//		System.out.println("inqueueName8接收" + buffer);
//		if (buffer.contains("insert") || buffer.contains("update") || buffer.contains("delete")) {
//			DBUtils.updateSqlData8(buffer);
//		}
//	}
//	@JmsListener(destination = "inqueueName")
//	public void receiveMessage9(String buffer){
//		System.out.println("inqueueName9接收" + buffer);
//		if (buffer.contains("insert") || buffer.contains("update") || buffer.contains("delete")) {
//			DBUtils.updateSqlData9(buffer);
//		}
//	}
//	
//	@JmsListener(destination = "inqueueName")
//	public void receiveMessage10(String buffer){
//		System.out.println("inqueueName10接收" + buffer);
//		if (buffer.contains("insert") || buffer.contains("update") || buffer.contains("delete")) {
//			DBUtils.updateSqlData10(buffer);
//		}
//	}
}
