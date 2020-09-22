package com.hedong.hedongwx.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.hedong.hedongwx.dao.ServerSendMessageDao;


/**
 * socket 线程类
 * 
 * @author RoarWolf
 */
public class SocketThread extends Thread {
	private ServerSocket serverSocket = null;
	private ServerSendMessageDao ssd = ServerSendMessageDao.getInstance();
	
	public SocketThread(ServerSocket serverScoket) {
		try {
			if (null == serverSocket) {
				this.serverSocket = new ServerSocket(4700);
				System.out.println("socket start");
			}
		} catch (Exception e) {
//			System.out.println("SocketThread创建socket服务出错");
			e.printStackTrace();
		}

	}
	
	public void run() {
		while (!this.isInterrupted()) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "接入");
				ssd.deleteServerSendMessage();
				if (null != socket && !socket.isClosed()) {
					// 处理接受的数据
					new SocketOperateIn(socket).start();
//					new SocketOperateOut(socket).start();
				}
//				if (true) {
//				}
				socket.setSoTimeout(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		closeSocketServer();
	}

	public void closeSocketServer() {
		try {
			if (null != serverSocket && !serverSocket.isClosed()) {
				System.out.println("服务器关闭");
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}