package com.hedong.hedongwx.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hedong.hedongwx.utils.DisposeUtil;

@ServerEndpoint(value = "/wolfwebsocket")
@Component
public class MyWebSocketServer {

	private Logger logger = LoggerFactory.getLogger(MyWebSocketServer.class);
//	private static Session session;
	private static ConcurrentMap<String, Session> sessionMap = new ConcurrentHashMap<>();
	
	public static ConcurrentMap<String, MyWebSocketServer> webSocketMap = new ConcurrentHashMap<>();

	/**
	 * 连接建立后触发的方法
	 */
	@OnOpen
	public void onOpen(Session session) {
//		MyWebSocketServer.session = session;
		sessionMap.put(session.getId(), session);
		logger.info("onOpen" + session.getId());
		webSocketMap.put(session.getId(), this);
	}

	/**
	 * 连接关闭后触发的方法
	 */
	@OnClose
	public void onClose(Session session) {
		webSocketMap.remove(session.getId());
		sessionMap.remove(session.getId());
		logger.info("====== onClose:" + session.getId() + " ======");
	}

	/**
	 * 接收到客户端消息时触发的方法
	 */
	@OnMessage
	public void onMessage(String params, Session session) throws Exception {
		// 获取服务端到客户端的通道
		MyWebSocketServer myWebSocket = webSocketMap.get(session.getId());
		logger.info("收到来自" + session.getId() + "的消息" + params);
//		String result = "收到来自" + session.getId() + "的消息" + params;
		// 返回消息给Web Socket客户端（浏览器）
//		myWebSocket.sendMessage(session, 1, "connect true", result);
		if (params.contains("shzylc")) {
			
		}
	}

	/**
	 * 发生错误时触发的方法
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		logger.info(session.getId() + "连接发生错误" + error.getMessage());
		error.printStackTrace();
	}

	public void sendMessage(Session session, int status, String message, Object datas) throws IOException {
		JSONObject result = new JSONObject();
		result.put("status", status);
		result.put("message", message);
		result.put("datas", datas);
		session.getBasicRemote().sendText(result.toString());
	}

	public void wolfSendMsg(int status, String message, String datas) throws IOException {
		if (DisposeUtil.checkMapIfHasValue(sessionMap)) {
			Set<Entry<String,Session>> entrySet = sessionMap.entrySet();
			for (Entry<String, Session> entry : entrySet) {
				entry.getValue().getBasicRemote().sendText(datas);
			}
		}
	}
	
	public void wolfSendOrder(String code,int paysource,int paytype,String ordernum, double money) {
		try {
			JSONObject result = new JSONObject();
			result.put("code", code);
			result.put("type", paysource);
			result.put("paytype", paytype);
			result.put("order", ordernum.substring(ordernum.length() - 4));
			result.put("money", money);
			wolfSendMsg(200, "success", result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
