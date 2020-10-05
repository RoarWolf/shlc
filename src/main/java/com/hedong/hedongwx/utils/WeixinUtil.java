package com.hedong.hedongwx.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.dao.UserHandler;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.wx.AccessToken;
import com.hedong.hedongwx.entity.wx.ApiTicket;
import com.hedong.hedongwx.entity.wx.Article;
import com.hedong.hedongwx.entity.wx.Message;
import com.hedong.hedongwx.entity.wx.Reply;
import com.hedong.hedongwx.entity.wx.TextMessage;
import com.hedong.hedongwx.entity.wx.menu.Button;
import com.hedong.hedongwx.entity.wx.menu.ClickButton;
import com.hedong.hedongwx.entity.wx.menu.Menu;
import com.hedong.hedongwx.entity.wx.menu.ViewButton;
import com.hedong.hedongwx.entity.wx.model.ImageMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import net.sf.json.JSONObject;

/**
 * 微信相关工具类
 */
public class WeixinUtil {
	
	public static final String APPID = "wx9b422ab1c06627b3";
	public static final String APPSECRET = "966ecd8ffb1a5e126da82fc7cf7ef73c";
	
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	
	private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	
	private static final String API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=ACCESS_TOKEN";
	
	private static final String WEBPAGE_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPIDARGU&redirect_uri=REDIURL&response_type=code&connect_redirect=1&scope=snsapi_userinfo&state=state#wechat_redirect";
	
	private static final String WEBPAGE_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPIDARGU&secret=APPSECRETARGU&code=CODEARGU&grant_type=authorization_code";
	
	private static final String WEBPAGE_REFRESHTOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPIDARGU&grant_type=refresh_token&refresh_token=REFRESHTOKENARGU";
	
	private static final String WEBPAGE_USERINFOR = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESSTOKEN&openid=OPENIDARGU&lang=zh_CN";
	
	private static final String WEBPAGE_EXAMINETOKEN = "https://api.weixin.qq.com/sns/auth?access_token=ACCESSTOKEN&openid=OPENIDARGU";
	
	private static final String APPLET_OPENID = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
	
	private static XStream xstream = new XStream(new XppDriver() {  
        public HierarchicalStreamWriter createWriter(Writer out) {  
            return new PrettyPrintWriter(out) {  
                // 对所有xml节点的转换都增加CDATA标记  
                boolean cdata = true;  
                @SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {  
                    super.startNode(name, clazz);  
                }  
                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {  
                        writer.write("<![CDATA[");  
                        writer.write(text);  
                        writer.write("]]>");  
                    } else {
                        writer.write(text);  
                    }  
                }  
            };  
        }  
    });  
	
	/**
	 * 将回复消息对象转换成xml字符串
	 * @param reply 回复消息对象
	 * @return 返回符合微信接口的xml字符串
	 */
	public static String replyToXml(Reply reply){
		String type = reply.getMsgType();
		if(Reply.TEXT.equals(type)){
			xstream.omitField(Reply.class, "articles");
			xstream.omitField(Reply.class, "articleCount");
			xstream.omitField(Reply.class, "musicUrl");
			xstream.omitField(Reply.class, "hQMusicUrl");
		}else if(Reply.MUSIC.equals(type)){
			xstream.omitField(Reply.class, "content");
			xstream.omitField(Reply.class, "musicUrl");
			xstream.omitField(Reply.class, "hQMusicUrl");
		}else if(Reply.NEWS.equals(type)){//图文信息回复
			xstream.omitField(Reply.class, "articles");
			xstream.omitField(Reply.class, "articleCount");
			xstream.omitField(Reply.class, "content");
			xstream.omitField(Reply.class, "title");
			xstream.omitField(Reply.class, "description");
			xstream.omitField(Reply.class, "picUrl");
			xstream.omitField(Reply.class, "url");
		}
		xstream.autodetectAnnotations(true);
		xstream.alias("xml", reply.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(reply);
	}
	
	public static String mapToXml(Map<String, String> map) {
		return xstream.toXML(map);
	}
	
	/*public static void mapToXml() {
		// 设备消息或事件
		if (MsgType.DEVICE_EVENT.equals(msgType)
		|| MsgType.DEVICE_TEXT.equals(msgType)) { 
		String reqContent = reqMap.get("Content"); 
		String deviceType = reqMap.get("DeviceType"); 
		String deviceID = reqMap.get("DeviceID"); 
		String sessionID = reqMap.get("SessionID"); 
		final String openID = reqMap.get("OpenID");
		// 设备事件推送
		if (MsgType.DEVICE_EVENT.equals(msgType)) { 
		String event = reqMap.get("Event");
		// 绑定/解绑事件
		if (MsgType.DeviceEvent.BIND.equals(event)
		|| MsgType.DeviceEvent.UNBIND.equals(event)) {
		// 存储用户和设备的绑定关系
		if(MsgType.DeviceEvent.BIND.equals(event)){ 
		DBMock.saveBoundInfo(reqMap);
		}else{
		DBMock.removeBoundInfo(reqMap.get("FromUserName"));
		}
		// 设备绑定/解绑事件可以回复空包体
		return "";
		}
		}
		// 收到设备消息
		if (MsgType.DEVICE_TEXT.equals(msgType)) {
		// Base64 解码
		byte[] reqRaw = Base64.decodeBase64(reqContent);
		// 反序列化
		BlueLight lightReq = BlueLight.parse(reqRaw);
		// 逻辑处理
		// demo 中 推送消息给用户微信
		String reqText = lightReq.body; 
		System.out.println("recv text:" + reqText);
		String transText = "收到设备发送的数据：";
		byte[] reqTextRaw = reqText.getBytes("UTF-8");
		if (reqTextRaw.length > 0 && reqTextRaw[reqTextRaw.length - 1] ==0) {
		// 推送给微信用户的内容去掉末尾的反斜杠零'\0'
		transText = transText + new String(reqTextRaw, 0,
		reqTextRaw.length - 1, "UTF-8");
		} else{
		transText = transText + reqText;
		}
		// 推送文本消息给微信
		MpApi.customSendText(openID, transText);
		// demo 中 回复 收到的内容给设备
		BlueLight lightResp = BlueLight.build(BlueLight.CmdId.SEND_TEXT_RESP, 
		reqText, lightReq.head.seq);
		// 序列化
		byte[] respRaw = lightResp.toBytes();
		// Base64 编码
		String respCon = Base64.encodeBase64String(respRaw);
		// 设备消息接口必须回复符合协议的 xml
		return XmlResp.buildDeviceText(toUser, fromUser, deviceType, deviceID, 
		respCon, sessionID);
		}
		}
	}*/
	
	/**
	 * 存储数据的Map转换为对应的Message对象
	 * @param map 存储数据的map
	 * @return 返回对应Message对象
	 */
	public static  Message  mapToMessage(Map<String,String> map){
		if(map == null) return null;
		String msgType = map.get("MsgType");
		Message message = new Message();
		message.setToUserName(map.get("ToUserName"));
		message.setFromUserName(map.get("FromUserName"));
		message.setCreateTime(new Date());
		message.setMsgType(msgType);
		message.setMsgId(map.get("MsgId"));
		if(Message.TEXT.equals(msgType)){
			message.setContent(map.get("Content"));
		}else if(Message.IMAGE.equals(msgType)){
			message.setPicUrl(map.get("PicUrl"));
		}else if(Message.LINK.equals(msgType)){
			message.setTitle(map.get("Title"));
			message.setDescription(map.get("Description"));
			message.setUrl(map.get("Url"));
		}else if(Message.LOCATION.equals(msgType)){
			message.setLocationX(map.get("Location_X"));
			message.setLocationY(map.get("Location_Y"));
			message.setScale(map.get("Scale"));
			message.setLabel(map.get("Label"));
		}else if(Message.EVENT.equals(msgType)){
			message.setEvent(map.get("Event"));
			message.setEventKey(map.get("EventKey"));
		}
		return message;
	}
	
	/**
	 * 存储数据的Map转换为对应的Message对象
	 * @param map 存储数据的map
	 * @return 返回对应Message对象
	 */
	public static  Message  jsonMapToMessage(Map<String,String> map){
		if(map == null) return null;
		String msgType = map.get("msg_type");
		Message message = new Message();
		message.setCreateTime(new Date());
		message.setMsgType(msgType);
		message.setSessionID(map.get("SessionID"));
		message.setDeviceID(map.get("device_id"));
		message.setDeviceType(map.get("device_type"));
		message.setOpenID(map.get("open_id"));
		return message;
	}
	
	/**
	 * 解析request中的xml 并将数据存储到一个Map中返回
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		try {
			InputStream inputStream = request.getInputStream();
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			Element root = document.getRootElement();
			List<Element> elementList = root.elements();
			for (Element e : elementList)
				//遍历xml将数据写入map
				map.put(e.getName(), e.getText());
			inputStream.close();
			inputStream = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	
	/**
	 * sha1加密算法
	 * @param key需要加密的字符串
	 * @return 加密后的结果
	 */
	public static String sha1(String key) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(key.getBytes());
			String pwd = new BigInteger(1, md.digest()).toString(16);
			return pwd;
		} catch (Exception e) {
			e.printStackTrace();
			return key;
		}
	}
	
	/**
	 * get请求
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject doGetStr(String url) throws ParseException, IOException{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		CloseableHttpResponse httpResponse = client.execute(httpGet);
		HttpEntity entity = httpResponse.getEntity();
		if(entity != null){
			String result = EntityUtils.toString(entity,"UTF-8");
			jsonObject = JSONObject.fromObject(result);
//			System.out.println(jsonObject);
		}
		return jsonObject;
	}
	
	/**
	 * POST请求
	 * @param url
	 * @param outStr
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject doPostStr(String url,String outStr) throws ParseException, IOException{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
		CloseableHttpResponse response = client.execute(httpPost);
		String result = EntityUtils.toString(response.getEntity(),"UTF-8");
		jsonObject = JSONObject.fromObject(result);
		return jsonObject;
	}
	
	/**
	 * 文件上传
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws KeyManagementException
	 */
	public static String upload(String filePath, String accessToken,String type) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			throw new IOException("文件不存在");
		}

		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);
		
		URL urlObj = new URL(url);
		//连接
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

		con.setRequestMethod("POST"); 
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); 

		//设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		//设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");

		//获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);

		//文件正文部分
		//把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();

		//结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线

		out.write(foot);

		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try {
			//定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		JSONObject jsonObj = JSONObject.fromObject(result);
		System.out.println(jsonObj);
		String typeName = "media_id";
		if(!"image".equals(type)){
			typeName = type + "_media_id";
		}
		String mediaId = jsonObj.getString(typeName);
		return mediaId;
	}
	
	/**
	 * 获取accessToken
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String getAccessToken() throws ParseException, IOException{
//		AccessToken token = new AccessToken();
		String access_token = "";
		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		JSONObject jsonObject = doGetStr(url);
		if(jsonObject!=null){
//			AccessToken.token = jsonObject.getString("access_token");
//			AccessToken.expiresIn = jsonObject.getInt("expires_in");
			long nowtime = (long)System.currentTimeMillis()+(long)(1000*6900);//获取当前时间戳
//			AccessToken.expirescutoff = nowtime;
			access_token = jsonObject.getString("access_token");
			AccessToken accessToken = new AccessToken();
			
			accessToken.setToken(jsonObject.getString("access_token"));
			accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			accessToken.setExpirescutoff(nowtime);
			JedisUtils.setnum("wolftoken", JSON.toJSONString(accessToken), 300, 3);
			/*String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();
			System.out.println(menu);
			int result = WeixinUtil.createMenu(jsonObject.getString("access_token"), menu);
			if(result == 0){
				System.out.println("create menu access");
			}else {
				System.err.println("failure" + result);
			}*/
			/*String path = "/usr/server1.jpg";
			try {
				WeixinUtil.upload(path, getBasicAccessToken(), "image");
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		}
		return access_token;
	}
	
	/*public static String getImageMediaid() {
		String path = "/usr/server1.jpg";
		String mediaId = "";
		try {
			mediaId = WeixinUtil.upload(path, getBasicAccessToken(), "image");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mediaId;
	}*/
	
	public static ApiTicket getApiTicket(String accessToken) throws ParseException, IOException {
		ApiTicket apiTicket = new ApiTicket();
		String url = API_TICKET_URL.replace("ACCESS_TOKEN", accessToken);
		JSONObject jsonObject = doGetStr(url);
		if(jsonObject != null) {
//			System.out.println(jsonObject);
			apiTicket.setErrcode(jsonObject.getInt("errcode"));
			apiTicket.setErrmsg(jsonObject.getString("errmsg"));
			apiTicket.setTicket(jsonObject.getString("ticket"));
			apiTicket.setExpires_in(jsonObject.getInt("expires_in"));
		}
		return apiTicket;
	}
	
	/** 
     * 文本消息对象转换成xml 
     *  
     * @param textMessage 文本消息对象 
     * @return xml 
     */  
    public static String textMessageToXml(TextMessage textMessage) {  
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);  
    }
    
    /** 
     * 图片消息对象转换成xml 
     *  
     * @param textMessage 文本消息对象 
     * @return xml 
     */  
    public static String imageMessageToXml(ImageMessage imageMessage) {  
    	xstream.alias("xml", imageMessage.getClass());
    	return xstream.toXML(imageMessage);  
    }
	
	/**
	 * 组装菜单
	 * @return
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		ClickButton button11 = new ClickButton();
		button11.setName("扫码充电");
		button11.setType("scancode_push");
		button11.setKey("11");
		
		/*ViewButton button21 = new ViewButton();
		button21.setName("使用说明");
		button21.setType("view");
		button21.setUrl("http://www.tengfuchong.com.cn/helpdoc");*/
		
		ViewButton button22 = new ViewButton();
		button22.setName("充电中心");
		button22.setType("view");
		button22.setUrl("http://www.tengfuchong.com.cn/chargingoauth2");

		Button sub_button = new Button();
		sub_button.setName("后台管理");
		
//		ViewButton button30 = new ViewButton();
//		button30.setName("售后登陆");
//		button30.setType("view");
//		button30.setUrl("http://www.tengfuchong.com.cn/oauth2loginChild");
		
		ViewButton button31 = new ViewButton();
		button31.setName("商家登陆");
		button31.setType("view");
		button31.setUrl("http://www.tengfuchong.com.cn/oauth2login");
		
		ViewButton button32 = new ViewButton();
		button32.setName("使用说明");
		button32.setType("view");
		button32.setUrl("http://www.tengfuchong.com.cn/helpdoc");
		
//		ViewButton button34 = new ViewButton();
//		button34.setName("个人中心");
//		button34.setType("view");
//		button34.setUrl("http://www.tengfuchong.com.cn/generalLogin");
		
		/*ViewButton button24 = new ViewButton();
		button24.setName("后台管理");
		button24.setType("view");
		button24.setUrl("http://www.he360.com.cn/user/list");*/
		
		Button[] buttons = new Button[]{button31,button32};
		sub_button.setSub_button(buttons);
		
		menu.setButton(new Button[]{button11,button22,sub_button});
		return menu;
	}
	
	public static int createMenu(String token,String menu) throws ParseException, IOException{
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doPostStr(url, menu);
		if(jsonObject != null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	
	public static JSONObject queryMenu(String token) throws ParseException, IOException{
		String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doGetStr(url);
		return jsonObject;
	}
	
	public static int deleteMenu(String token) throws ParseException, IOException{
		String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doGetStr(url);
		int result = 0;
		if(jsonObject != null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	
	
	/**
	 * 引导用户进入授权页面同意授权，获取code
	 * 在确保微信公众账号拥有授权作用域（scope参数）的权限的前提下（服务号获得高级接口后，默认拥有scope参数中的snsapi_base和
	 * snsapi_userinfo），引导关注者打开如下页面
	 * code说明 ： code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
	 * @return
	 */
	public static String loginWeChats(String urls){
		String str = WEBPAGE_AUTHORIZE;
		try {
			String url = URLEncoder.encode(urls, "utf-8");
			str = str.replace("APPIDARGU", APPID).replace("REDIURL", url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//		System.out.println("输出str_code：      "+str);
		return "redirect:" + str;
	}
	
	/**
	 * 用户同意授权，通过code换取网页授权access_token
	 * 注意，这里通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token（该access_token用于调用其他接口）不同。
	 * @param code 填写第一步获取的code参数
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static JSONObject WeChatToken(String code) throws ParseException, IOException{
		String str = WEBPAGE_TOKEN;
		str = str.replace("APPIDARGU", APPID).replace("APPSECRETARGU", APPSECRET).replace("CODEARGU", code);
		JSONObject acctoken = WeixinUtil.doGetStr(str);
//		System.out.println("输出acctoken：      "+acctoken);
		return acctoken;
	}
	
	/**
	 * 通过设备号查询微信特约商户的appid授权
	 * 通过code换取网页授权access_token
	 * 注意，这里通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token（该access_token用于调用其他接口）不同。
	 * @param code 填写第一步获取的code参数
	 * @param devicenum 设备号
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static JSONObject WeChatTokenSubMer(String code, String devicenum) throws ParseException, IOException{
		String str = WEBPAGE_TOKEN;
		// 根据商户的设备号查询商户的APPID,APPSECRET
		User subMer = UserHandler.selectSubMerData(devicenum);
		String appid = null;
		String appsecret = null;
		if(subMer != null ){
			appid = subMer.getSubAppId();
			appsecret = subMer.getAppSecret();
			System.out.println("特约商户的Appid=="+(appid != null)+"密钥===="+(appsecret != null));
		}
		// 使用微信特约商户的密钥和appid
		str = str.replace("APPIDARGU", appid).replace("APPSECRETARGU", appsecret).replace("CODEARGU", code);
		JSONObject acctoken = WeixinUtil.doGetStr(str);
//		System.out.println("输出acctoken：      "+acctoken);
		return acctoken;
	}
	
	/**
	 * 通过商家的id查找特约商户配置信息
	 * 通过code换取网页授权access_token
	 * 注意，这里通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token（该access_token用于调用其他接口）不同。
	 * @param code 填写第一步获取的code参数
	 * @param devicenum 设备号
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static JSONObject WeChatTokenSubMerByMerId(String code, Integer merId) throws ParseException, IOException{
		String str = WEBPAGE_TOKEN;
		// 根据商户的设备号查询商户的APPID,APPSECRET
		User subMer = UserHandler.selectSubMerConfigByMerId(merId);
		String appid = null;
		String appsecret = null;
		if(subMer != null ){
			appid = subMer.getSubAppId();
			appsecret = subMer.getAppSecret();
		}
		System.out.println("特约商户的Appid=="+(appid != null)+"密钥===="+(appsecret != null));
		// 使用微信特约商户的密钥和appid
		str = str.replace("APPIDARGU", appid).replace("APPSECRETARGU", appsecret).replace("CODEARGU", code);
		JSONObject acctoken = WeixinUtil.doGetStr(str);
//		System.out.println("输出acctoken：      "+acctoken);
		return acctoken;
	}
	
	
	/**
	 * 刷新access_token（如果需要）
	 * 由于access_token拥有较短的有效期，当access_token超时后，可以使用refresh_token进行刷新，refresh_token有效期为30天，
	 * 当refresh_token失效之后，需要用户重新授权。
	 * @param refreshtoken   填写通过access_token获取到的refresh_token参数
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public JSONObject refreshToken(String refreshtoken) throws ParseException, IOException{
		String str = WEBPAGE_REFRESHTOKEN;
		str = str.replace("APPIDARGU", APPID).replace("REFRESHTOKENARGU", refreshtoken);
		JSONObject refToken = WeixinUtil.doGetStr(str);
//		System.out.println("输出str_token：      "+refToken);
		return refToken;
	}
	
	/**
	 * 拉取用户信息(需scope为 snsapi_userinfo)
	 * 如果网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openid拉取用户信息了。
	 * @param accessToken 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	 * @param openid 用户的唯一标识   注：lang	返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static JSONObject getuserinfo(String accessToken, String openid) throws ParseException, IOException{
		String str = WEBPAGE_USERINFOR;
		str = str.replace("ACCESSTOKEN", accessToken).replace("OPENIDARGU", openid);
		JSONObject userinfo = WeixinUtil.doGetStr(str);
//		System.out.println("输出acctoken：      "+userinfo);
		return userinfo;
	}
	
	/**
	 * 检验授权凭证（access_token）是否有效
	 * @param accessToken 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	 * @param openid 用户的唯一标识
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public  static JSONObject examineToken(String accessToken, String openid) throws ParseException, IOException{
		String str = WEBPAGE_EXAMINETOKEN;
		str = str.replace("ACCESSTOKEN", accessToken).replace("OPENIDARGU", openid);
		JSONObject examineToken = WeixinUtil.doGetStr(str);
//		System.out.println("输出str_token：      "+examineToken);
		return examineToken;
	}
	
	/**
	 * 检验授权凭证（access_token）是否有效
	 * @param accessToken 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	 * @param openid 用户的唯一标识
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public  static JSONObject getUserOpenid(String code) throws ParseException, IOException{
		String str = APPLET_OPENID;
		str = str.replace("APPID", APPID).replace("SECRET", APPSECRET).replace("JSCODE", code);
		JSONObject examineToken = WeixinUtil.doGetStr(str);
//		System.out.println("输出str_token：      "+examineToken);
		return examineToken;
	}
	
	/**
	 * 获取token，（判断token是否存在或者过期，是则重新获取，否则直接返回token对象）
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String getBasicAccessToken() throws ParseException, IOException{
		String getnum = JedisUtils.getnum("wolftoken", 3);
		AccessToken accessToken = JSON.toJavaObject((JSON)JSON.parse(getnum), AccessToken.class);
		if(accessToken==null){//获取token
			return getAccessToken();
		}else{
			String token = accessToken.getToken();
			long expirescutoff = accessToken.getExpirescutoff();
			long nowtime = System.currentTimeMillis();//获取当前时间戳
			int num = StringUtil.compare_longdate(nowtime,expirescutoff);
			if(num>0){
				return token;
			}else{
				return getAccessToken();
			}
		}
	}
	
	
}
