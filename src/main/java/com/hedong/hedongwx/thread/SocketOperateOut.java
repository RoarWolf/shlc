package com.hedong.hedongwx.thread;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.hedong.hedongwx.dao.ServerSendMessageDao;
import com.hedong.hedongwx.entity.ServerSendMessage;


/**
 * 多线程处理socket接收的数据
 * 
 * @author RoarWolf
 * 
 */
public class SocketOperateOut extends Thread {
	private Socket socket;
	private ServerSendMessageDao serverSendMessageDao = ServerSendMessageDao.getInstance();

	public SocketOperateOut(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			OutputStream out = socket.getOutputStream();

			List<ServerSendMessage> list = new ArrayList<>();
			while (true) {
				list = serverSendMessageDao.getList();
				if (list.size() > 0) {
					System.out.println(list.size());
					for (ServerSendMessage message : list) {
						String str = message.getMessage();
						System.out.println("------------" + str);
						byte[] bytes = str.getBytes();
						out.write(bytes, 0, bytes.length);
						serverSendMessageDao.deleteById(message.getId());
					}
				}
				/*
				 * try { Thread.sleep(1000l); } catch (InterruptedException e) {
				 * e.printStackTrace(); }
				 */
			}
		} catch (IOException ex) {

		} finally {

		}
	}
}