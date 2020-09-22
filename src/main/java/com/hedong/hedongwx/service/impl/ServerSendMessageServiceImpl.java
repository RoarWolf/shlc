package com.hedong.hedongwx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.dao.ServerSendMessageMapper;
import com.hedong.hedongwx.entity.ServerSendMessage;
import com.hedong.hedongwx.service.ServerSendMessageService;

@Service
public class ServerSendMessageServiceImpl implements ServerSendMessageService {

	@Autowired
	private ServerSendMessageMapper serverSendMessageMapper;
	
	@Override
	public List<ServerSendMessage> getServerSendMessageList() {
		return serverSendMessageMapper.getServerSendMessageList();
	}

	@Override
	public void addServerSendMessage(ServerSendMessage serverSendMessage) {
		serverSendMessageMapper.addServerSendMessage(serverSendMessage);
	}

	@Override
	public void deleteServerSendMessageById(Integer id) {
		serverSendMessageMapper.deleteServerSendMessageById(id);
	}

	@Override
	public void deleteServerSendMessage() {
		serverSendMessageMapper.deleteServerSendMessage();
	}

}
