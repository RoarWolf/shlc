//package com.hedong.hedongwx.web.controller;
//
//import java.io.IOException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.codec.binary.Base64;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.hedong.hedongwx.entity.ServerSendMessage;
//import com.hedong.hedongwx.entity.wx.Message;
//import com.hedong.hedongwx.entity.wx.TextMessage;
//import com.hedong.hedongwx.service.ServerSendMessageService;
//import com.hedong.hedongwx.utils.ByteUtils;
//import com.hedong.hedongwx.utils.CheckUtil;
//import com.hedong.hedongwx.utils.HttpRequest;
//import com.hedong.hedongwx.utils.MessageUtil;
//import com.hedong.hedongwx.utils.WeixinUtil;
//
//import net.sf.json.JSONObject;
//
//@Controller
//public class WeixinController {
//	
//	private final Logger logger = LoggerFactory.getLogger(WeixinController.class);
//	@Autowired
//	private ServerSendMessageService serverSendMessageService;
//	
//	// 微信公众平台验证url是否有效使用的接口
//	@RequestMapping(value = "/hdwx", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
//	@ResponseBody
//	public String initWeixinURL(HttpServletRequest request) {
//		String echostr = request.getParameter("echostr");
//		if (CheckUtil.checkSignature(request) && echostr != null) {
//			return echostr;
//		} else {
//			return "error";
//		}
//	}
//	
//	@RequestMapping(value = "/hdwx", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
//	@ResponseBody
//	public String replyMessage(HttpServletRequest request) {
//		if (CheckUtil.checkSignature(request)) {
//			Map<String, String> parseXml = WeixinUtil.parseXml(request);
//			Message message = WeixinUtil.mapToMessage(parseXml);
//			String msgType = message.getMsgType();
//			if (Message.DEVICE_TEXT.equals(msgType)) {
//				String content = message.getContent();
//				byte[] reqRaw = Base64.decodeBase64(content);
//				logger.info("解码===" + reqRaw);
//				TextMessage text = new TextMessage();
//				text.setFromUserName(message.getToUserName());
//				text.setToUserName(message.getFromUserName());
//				text.setCreateTime(new Date().getTime());
//				text.setMsgType(message.getMsgType());
//				text.setDeviceID(message.getDeviceID());
//				text.setDeviceType(message.getDeviceType());
//				String replyStr = "蓝牙设备绑定成功：DeviceID===" + message.getDeviceID();
//				text.setContent(Base64.encodeBase64String(replyStr.getBytes()));
//				text.setSessionID(message.getSessionID());
//				text.setMsgId(message.getMsgId());
//				text.setOpenID(message.getFromUserName());
//				return WeixinUtil.textMessageToXml(text);
//			}  else if(Message.DEVICE_EVENT.equals(msgType)) {
//				TextMessage text = new TextMessage();
//				text.setFromUserName(message.getToUserName());
//				text.setToUserName(message.getFromUserName());
//				text.setCreateTime(new Date().getTime());
//				text.setMsgType(Message.TEXT);
//				text.setContent("---绑定蓝牙设备---");
//				return WeixinUtil.textMessageToXml(text);
//			} else if (Message.TEXT.equals(msgType)) {
//				String content = message.getContent();
//				logger.info("content===" + content);
//				logger.info("contentQuChuEmoji===" + ByteUtils.replaceEmoji(content));
//				ServerSendMessage serverSendMessage = new ServerSendMessage();
//				serverSendMessage.setMessage(ByteUtils.replaceEmoji(content) + "success");
//				serverSendMessageService.addServerSendMessage(serverSendMessage);
//				TextMessage text = new TextMessage();
//				text.setFromUserName(message.getToUserName());
//				text.setToUserName(message.getFromUserName());
//				text.setCreateTime(new Date().getTime());
//				if ("1".equals(content) || content.contains("续充")) {
//					text.setMsgType(Message.TEXT);
//					text.setContent(MessageUtil.fristTextMessage());
//				} else if (content.contains("退款")) {
//					text.setMsgType(Message.TEXT);
//					text.setContent(MessageUtil.refundTextMessage());
//				} else if ("roarwolf".equals(content)) {
//					try {
//						System.out.println("accexxtoken===" + WeixinUtil.getBasicAccessToken());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				} else if (content.contains("A:")) {
//					try {
//						String url = "https://api.weixin.qq.com/device/transmsg?access_token=" + WeixinUtil.getBasicAccessToken();
//						SortedMap<String, String> params = new TreeMap<>();
//						params.put("device_type", "gh_44b70b6254a9");
//						params.put("device_id", "gh_44b70b6254a9_6b83f07db186355a");
//						params.put("open_id", message.getFromUserName());
//						Base64 base64 = new Base64();
//						content += "\r\n";
//						params.put("content", base64.encodeToString(content.substring(2).getBytes("UTF-8")));
//						JSONObject fromObject = JSONObject.fromObject(params);
//						logger.info(fromObject.toString());
////						String canshu = HttpRequest.getRequestXml(params);
//						String sr = HttpRequest.sendPost(url, fromObject.toString());
//						logger.info(sr);
//						text.setMsgType(Message.TEXT);
//						text.setContent("发送成功，等待接收...");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				} else {
//					text.setMsgType(Message.TEXT);
//					text.setContent(MessageUtil.initTextMessage());
//				}
//				return WeixinUtil.textMessageToXml(text);
//			} else if (Message.EVENT.equals(msgType)) {
//				TextMessage text = new TextMessage();
//				text.setFromUserName(message.getToUserName());
//				text.setToUserName(message.getFromUserName());
//				text.setCreateTime(new Date().getTime());
//				text.setMsgType(Message.TEXT);
//				text.setContent("感谢您关注：【和动充电站】");
//				return WeixinUtil.textMessageToXml(text);
//			} else {
//				TextMessage text = new TextMessage();
//				text.setFromUserName(message.getToUserName());
//				text.setToUserName(message.getFromUserName());
//				text.setCreateTime(new Date().getTime());
//				text.setMsgType(Message.TEXT);
//				text.setContent(MessageUtil.initTextMessage());
//				return WeixinUtil.textMessageToXml(text);
//			}
//		}
//		return "未识别";
//	}
//	
//	// 微信公众平台验证url是否有效使用的接口
//	@RequestMapping(value = "/hdwxdevice", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
//	@ResponseBody
//	public String initdeviceWeixinURL(HttpServletRequest request) {
//		String echostr = request.getParameter("echostr");
//		if (CheckUtil.checkSignature(request) && echostr != null) {
//			return echostr;
//		} else {
//			return "error";
//		}
//	}
//	
//	@RequestMapping(value = "/hdwxdevice", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
//	@ResponseBody
//	public String replyDevicedMessage(HttpServletRequest request) {
//			Map<String, String> map = new HashMap<>();
//			Message message = new Message();
//			String type = request.getContentType();
//			if (type.contains("json")) {
//				try {
//					String jsonString = ByteUtils.getRequestJsonString(request);
//					JSONObject fromObject = JSONObject.fromObject(jsonString);
//					map = (Map<String, String>)fromObject;
//					logger.info(jsonString);
//				} catch (IOException e1) {
//					logger.info(e1.getMessage());
//				}
//				message = WeixinUtil.jsonMapToMessage(map);
//			} else if (type.contains("xml")) {
//				map = WeixinUtil.parseXml(request);
//				message = WeixinUtil.mapToMessage(map);
//			}
//			logger.info(message.toString());
//			String msgType = message.getMsgType();
//			if (Message.DEVICE_TEXT.equals(msgType)) {
//				String content = message.getContent();
//				byte[] reqRaw = Base64.decodeBase64(content);
//				logger.info("解码===" + reqRaw);
//				TextMessage text = new TextMessage();
//				text.setFromUserName(message.getToUserName());
//				text.setToUserName(message.getFromUserName());
//				text.setCreateTime(new Date().getTime());
//				text.setMsgType(message.getMsgType());
//				text.setDeviceID(message.getDeviceID());
//				text.setDeviceType(message.getDeviceType());
//				String replyStr = "蓝牙设备绑定成功：DeviceID===" + message.getDeviceID();
//				text.setContent(Base64.encodeBase64String(replyStr.getBytes()));
//				text.setSessionID(message.getSessionID());
//				text.setMsgId(message.getMsgId());
//				text.setOpenID(message.getFromUserName());
//				return WeixinUtil.textMessageToXml(text);
//			}  else {
//				TextMessage text = new TextMessage();
//				text.setFromUserName(message.getDeviceType());
//				text.setToUserName(message.getOpenID());
//				text.setCreateTime(new Date().getTime());
//				text.setMsgType(Message.DEVICE_TEXT);
//				text.setDeviceID(message.getDeviceID());
//				text.setDeviceType(message.getDeviceType());
//				logger.info("msg_type===" + message.getMsgType());
//				String content = "";
//				if ("bind".equals(message.getEvent())) {
//					content = "---绑定蓝牙设备---";
//				} else if ("unbind".equals(message.getEvent())) {
//					content = "---解绑蓝牙设备---";
//				} else {
//					content = "未知操作！！！";
//				}
//				text.setContent(Base64.encodeBase64String(content.getBytes()));
//				text.setSessionID(message.getSessionID());
//				return WeixinUtil.textMessageToXml(text);
//			}
//	}
//	
//}
//
//
//
//
//
