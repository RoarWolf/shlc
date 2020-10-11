package com.hedong.hedongwx.thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.entity.ParseMsg;
import com.hedong.hedongwx.entity.SendData;
import com.hedong.hedongwx.utils.AESUtil;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.SendMsgUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Server {
	public static void main(String[] args) throws IOException {

	}
	
	@Autowired
	private SendMsgUtil sendMsgUtil;
	private static Logger logger = LoggerFactory.getLogger(Server.class);
	private static final List<Client> connections = Collections.synchronizedList(new ArrayList<Client>());
	private static ConcurrentHashMap<String, Client> clientMap = new ConcurrentHashMap<String, Client>();
	public static ConcurrentHashMap<String, String> codeAndIPMap = new ConcurrentHashMap<String, String>();
	private AsynchronousServerSocketChannel listener;
	private AsynchronousChannelGroup channelGroup;
	private final Queue<SendData> queue = new LinkedList<>();
	private int port;
	private Map<String, Object> msgMap = new HashMap<>();
	public static ParseMsg parseMsg = new ParseMsg();
	private static Map<String, Map> map = new HashMap<>();
	public static Map<String, Map<String,Map<String,String>>> stopmap = new HashMap<>();
	public static Map<String, List<String>> stateMap = new HashMap<>();
	public static Map<String, Map> infoMap = new HashMap<>();
	public static Map<String, Map<String,Map<String,String>>> recycleMap = new HashMap<>();
	public static Map<String, Map> isfreeMap = new HashMap<>();
	public static Map<String, Map<String,String>> everyportMap = new HashMap<>();
	public static Map<String, Map<String, String>> readsystemMap = new HashMap<>();
	public static Map<String,Map<String,Map<String,String>>> islockMap = new HashMap<>();
	public static Map<String,Map<String,String>> offlineMap = new HashMap<>();
	public static Map<String,Map<String,String>> inCoinsMap = new HashMap<>();
	public static Map<String,Map<String,String>> setSystemMap = new HashMap<>();
	public static short scanTime;
	
	public void createServer(int portParam) throws IOException {
		if (channelGroup == null) {
			channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(),
					Executors.defaultThreadFactory());
			port = portParam;
			listener = createListener(channelGroup);
		} else {
			return;
		}
	}

	/*
	 * Creates a listener and starts accepting connections
	 */
	private AsynchronousServerSocketChannel createListener(AsynchronousChannelGroup channelGroup) throws IOException {
		final AsynchronousServerSocketChannel listener = openChannel(channelGroup);
		listener.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		listener.bind(new InetSocketAddress(port));
		return listener;
	}

	private AsynchronousServerSocketChannel openChannel(AsynchronousChannelGroup channelGroup) throws IOException {
		return AsynchronousServerSocketChannel.open(channelGroup);
	}

	public void sendMsgTest(String clientId, ByteBuffer buffer) {

	}

	public static int sendMsg(String clientId, ByteBuffer buffer) {
		try {
			if (clientId != null) {
				if (clientMap.get(clientId) != null) {
					Client client = clientMap.get(clientId);
					client.sendMsg(buffer);
					return 1;
				} else {
					//equ.updateState((byte) 0, clientId);
					//sql更新
					System.out.println("连接已断开");
					return 0;
				}
			} else {
				System.out.println("无此连接");
				return 0;
			} 
		} catch (Exception e) {
			//equ.updateState((byte) 0, clientId);
			//sql更新
			System.out.println("连接已断开--数据发送异常");
			return 0;
		}
	}

	public boolean isOnline(String clientId) {
		return clientMap.contains(clientId);
	}
	
	public static Map<String, Map> getMap() {
		return map;
	}

	public void setMap(Map<String, Map> map) {
		this.map = map;
	}
	
	public static void setStateMap(Map<String, List<String>> stateMap) {
		Server.stateMap = stateMap;
	}
	
	public static void setInfoMap(Map<String, Map> infoMap) {
		Server.infoMap = infoMap;
	}
	
	public static Map<String, Map> getInfoMap() {
		return infoMap;
	}

	public static void setRecycleMap(Map<String, Map<String,Map<String,String>>> recycleMap) {
		Server.recycleMap = recycleMap;
	}

	public static void setIsfreeMap(Map<String, Map> isfreeMap) {
		Server.isfreeMap = isfreeMap;
	}

	public static void setEveryportMap(Map<String, Map<String,String>> everyportMap) {
		Server.everyportMap = everyportMap;
	}

	public static void setReadsystemMap(Map<String, Map<String, String>> readsystemMap) {
		Server.readsystemMap = readsystemMap;
	}

	/**
	 * Start accepting connections
	 */
	public void run() {

		// call accept to wait for connections, tell it to call our
		// CompletionHandler when there
		// is a new incoming connection
		listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
			@Override
			public void completed(AsynchronousSocketChannel result, Void attachment) {
				// request a new accept and handle the incoming connection
				listener.accept(null, this);
				try {
					System.out.println("新连接： " + result.getRemoteAddress().toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				handleNewConnection(result);
			}

			@Override
			public void failed(Throwable exc, Void attachment) {
				System.out.println("连接断开");
			}
		});

		System.out.println("Running on port : " + port);
		
		
//		new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				while (true) {
//					Set<Entry<String,Client>> entrySet = clientMap.entrySet();
//					for (Entry<String, Client> entry : entrySet) {
//						Client client = entry.getValue();
//						Date packTime = client.getPackTime();
//						if(new Date().getTime() - packTime.getTime() > 600000) {
//							Equipmenthandler equ = new Equipmenthandler();
//							//equ.updateState((byte) 0, entry.getKey());
//							CommonHandler.equipmess((byte) 0, Equipmenthandler.findUserEquipment(entry.getKey()), entry.getKey());
//							equ.updateCsq((byte) 0,"0", entry.getKey());
//							try {
//								client.channel.close();
//								connections.remove(client);
//								equ.addCodeOperateLog(entry.getKey(), 1, 2, Equipmenthandler.findUserEquipment(entry.getKey()), 0);
//								clientMap.remove(entry.getKey());
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}).start();
//		new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						Thread.sleep(120000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					List<String> codelist = Equipmenthandler.getAllSoftVersionIs00();
//					if (codelist != null && codelist.size() > 0) {
//						for (String code : codelist) {
//						}
//					}
//				}
//			}
//		}).start();
	}

	private void handleNewConnection(AsynchronousSocketChannel channel) {
		Client client = new Client(null, channel, new DataReader() {

			@Override
			public void beforeRead(Client client) {

			}

			@Override
			public void onData(Client client, ByteBuffer buffer, int bytes) {
				client.setPackTime(new Date());
				buffer.flip();
				short framestart = buffer.getShort();//帧起始
				if (framestart != 0x4a58) {
					try {
						logger.warn("ip:" + channel.getLocalAddress().toString() + "---桢起始上传有误");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				logger.info("桢起始上传没毛病");
				byte cmd = buffer.get();//命令
				logger.info("cmd=" + cmd);
				
				//-----桩号 start 编码BCD-----
				byte[] deviceDateBytes = new byte[8];
				buffer.get(deviceDateBytes);
				String devicenum = AESUtil.BCD_String(deviceDateBytes);
				System.out.println("devicenum===" + devicenum);
//				short operatorNum = buffer.getShort();//运营商编号
//				String operatorStr = DisposeUtil.completeNumIntHex((operatorNum & 0xffff), 4);
//				byte gunNum = buffer.get();//枪数
//				String gunStr = DisposeUtil.completeNumIntHex((gunNum & 0xff), 2);
//				//站点编号（1-999999）共3个字节
//				short siteBeforeTwo = buffer.getShort();//站点前两个字节
//				String siteBeforeTwoStr = DisposeUtil.completeNumIntHex((siteBeforeTwo & 0xffff), 4);
//				short siteAfterOne = buffer.getShort();//站点后一个字节
//				String siteAfterOneStr = DisposeUtil.completeNumIntHex((siteAfterOne & 0xff), 2);
//				//站内桩地址（1-9999） 2个字节
//				short siteLocal = buffer.getShort();
//				String siteLocalStr = DisposeUtil.completeNumIntHex((siteLocal & 0xffff), 4);
//				//桩号
//				String deviceNum = DisposeUtil.sumStr(operatorStr, gunStr, siteBeforeTwoStr, siteAfterOneStr,
//							siteLocalStr);
				//-----桩号 end-----
				
				byte encryptionWay = buffer.get();//加密方式 0x01-数据不加密，0x02-加密
				byte[] lenbytes = new byte[2];
				buffer.get(lenbytes);
				int datalen = DisposeUtil.converData(lenbytes);//数据域长度
				byte[] datas = new byte[datalen];
				try {
					buffer.get(datas);
				} catch (Exception e) {
					return;
				}
				buffer.position(14);
				byte[] dateBytes = new byte[6];
				buffer.get(dateBytes);
//				byte year = buffer.get();//年
//				byte month = buffer.get();//月
//				byte day = buffer.get();//日
//				byte hour = buffer.get();//时
//				byte minute = buffer.get();//分
//				byte second = buffer.get();//秒
				String deviceDataTime = DisposeUtil.disposeDate(dateBytes);
				//有无回复 1-有、2无
				if (cmd == 0x01) {//桩请求连接 1
					clientMap.put(devicenum, client);
					SendMsgUtil.parse_0x01(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
				} else if (cmd == 0x03) {//登录信息 2桩回复对时命令
					sendMsgUtil.parse_0x03(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
				} else if (cmd == 0x03) {//登录信息 2桩回复对时命令
					SendMsgUtil.parse_0x05(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
				} else if (cmd == 0x07) {//桩回复对时命令
					SendMsgUtil.parse_0x07(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
				} else if (cmd == 0x09) {//桩遥信
					SendMsgUtil.parse_0x09(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
				} else if (cmd == 0x0A) {//桩遥测
					SendMsgUtil.parse_0x0A(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
				} else if (cmd == 0x0C) {//桩心跳
					SendMsgUtil.parse_0x0C(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
				} else if (cmd == 0x1C) {//桩回复预约命令
					SendMsgUtil.parse_0x1C(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
				} else if (cmd == 0x1E) {//桩回复取消预约
					SendMsgUtil.parse_0x1E(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
					
				}
			}

			@Override
			public boolean acceptsMessages() {
				return false;
			}

		});
		client.run();
		connections.add(client);
	}

}

class Client {
	public String id;
	public AsynchronousSocketChannel channel;
	private DataReader callback;
	private Date packTime = new Date();
	
	public Date getPackTime() {
		return packTime;
	}

	public void setPackTime(Date packTime) {
		this.packTime = packTime;
	}

	public Client(String id, AsynchronousSocketChannel channel, DataReader callback) {
		this.id = id;
		this.channel = channel;
		this.callback = callback;
	}

	public void sendMsg(ByteBuffer buffer) {
		try {
			channel.write(buffer, 5000, TimeUnit.MILLISECONDS, buffer, new CompletionHandler<Integer, ByteBuffer>() {
				@Override
				public void completed(Integer result, ByteBuffer buffer) {
					if (buffer.hasRemaining()) {
						channel.write(buffer, buffer, this);
					} else {
						// Go back and check if there is new data to write
						//					 writeFromQueue();
					}
					System.out.println("send access");
				}

				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					System.out.println(exc.getMessage());
				}
			});
		} catch (Exception e) {
			try {
				System.out.println("写入数据失败：" + this.channel.getLocalAddress());
			} catch (IOException e1) {
				System.out.println("获取ip地址失败" + e1.getMessage());
			}
		}

	}

	public void read(CompletionHandler<Integer, ? super ByteBuffer> completionHandler) {
		ByteBuffer input = ByteBuffer.allocate(512);
		if (!channel.isOpen()) {
			return;
		}
		channel.read(input, input, completionHandler);
	}

	public void run() {
		Client client = this;
		client.read(new CompletionHandler<Integer, ByteBuffer>() {
			@Override
			public void completed(Integer result, ByteBuffer buffer) {
				// client.queueLogRecv(buffer, new Date(), client.getClientIp(),
				// null, client.getClientId(), null);
				// if result is negative or zero the connection has been closed
				// or something gone wrong
				if (result < 1) {
				} else {
					callback.onData(client, buffer, result);
					client.run();
				}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer buffer) {
				System.out.println("client close.");
			}
		});
	}

	protected void removeClient(Client client) {

	}
}

interface DataReader {
	void beforeRead(Client client);

	void onData(Client client, ByteBuffer buffer, int bytes);

	boolean acceptsMessages();
}