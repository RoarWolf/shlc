package com.hedong.hedongwx.dao;

import java.util.List;

import com.hedong.hedongwx.entity.ServerSendMessage;

public interface ServerSendMessageMapper {

	public List<ServerSendMessage> getServerSendMessageList();
	public void addServerSendMessage(ServerSendMessage serverSendMessage);
	public void deleteServerSendMessageById(Integer id);
	public void deleteServerSendMessage();
}
