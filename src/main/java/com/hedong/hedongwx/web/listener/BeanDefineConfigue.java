package com.hedong.hedongwx.web.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.hedong.hedongwx.service.ServerService;

@Component("BeanDefineConfigue")
public class BeanDefineConfigue implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private ServerService serverService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
//		serverService.serverStart();
	}
	
}