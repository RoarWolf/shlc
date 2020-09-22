package com.hedong.hedongwx.entity.wx;

public class TextMessage {

	// 接收方帐号（收到的OpenID）  
    private String ToUserName;  
    // 开发者微信号  
    private String FromUserName;  
    // 消息创建时间 （整型）  
    private long CreateTime;  
    // 消息类型（text/music/news）  
    private String MsgType;  
    // 位0x0001被标志时，星标刚收到的消息  
    private int FuncFlag;
    // 回复的消息内容  
    private String Content;
    // 公众号原始ID
    private String DeviceType;
    // 设备ID，第三方提供
    private String DeviceID;
    // 微信客户端生成的session id，用于request和response对应，因此响应中该字段第三方需要原封不变的带回
    private String SessionID;
    // 消息id，微信后台生成
    private String MsgId;
    // 微信用户账号的OpenID
    private String OpenID;
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public long getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public int getFuncFlag() {
		return FuncFlag;
	}
	public void setFuncFlag(int funcFlag) {
		FuncFlag = funcFlag;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getDeviceType() {
		return DeviceType;
	}
	public void setDeviceType(String deviceType) {
		DeviceType = deviceType;
	}
	public String getDeviceID() {
		return DeviceID;
	}
	public void setDeviceID(String deviceID) {
		DeviceID = deviceID;
	}
	public String getSessionID() {
		return SessionID;
	}
	public void setSessionID(String sessionID) {
		SessionID = sessionID;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	public String getOpenID() {
		return OpenID;
	}
	public void setOpenID(String openID) {
		OpenID = openID;
	}
    
}
