package com.hedong.hedongwx.web.listener;
/*package com.hedong.hdwx.web.listener;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import com.hd.iot.server.Server;
import com.hedong.hdwx.service.ServerService;
import com.hedong.thread.SocketThread;

*//**
 * 将socket service随tomcat启动
 * 
 * @author RoarWolf
 *//*
@Configuration
public class SocketServiceLoader implements ApplicationListener<ApplicationStartedEvent>  {
	// socket server 线程
	private SocketThread socketThread;
	
	@Autowired
	private ServerService serverService;
	

	@Override
	public void onApplicationEvent(ApplicationStartedEvent arg0) {
		System.out.println(".........in");
		if (null == socketThread) {
			// 新建线程类
			socketThread = new SocketThread(null);
			// 启动线程
			socketThread.start();
		}	
		int port = 5000;
        System.out.println("Running on port " + port);
        Server server;
		try {
			server = new Server(port);
			server.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
		serverService.serverStart();
	}
}
*/