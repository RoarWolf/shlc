package com.hedong.hedongwx.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.service.ServerService;
import com.hedong.hedongwx.thread.Server;

@Service
public class ServerServiceImpl implements ServerService {
	
	// private SocketThread socketThread;
	// private Server server;

	public void serverStart() {
		int port = 14700;
		try {
			Server server = new Server(port);
			server.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
