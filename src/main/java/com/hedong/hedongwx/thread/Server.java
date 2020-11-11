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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Server {
	public static void main(String[] args) throws IOException {
	}
	
	private static Logger logger = LoggerFactory.getLogger(Server.class);
	public static List<String> devicenumList = new ArrayList<>();
	public static final List<Client> connections = Collections.synchronizedList(new ArrayList<Client>());
	public static ConcurrentHashMap<String, Client> clientMap = new ConcurrentHashMap<String, Client>();
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
		System.out.println("clientId===" + clientId);
		DisposeUtil.printDeviceDataInfo(clientId, buffer, false);
		buffer.position(0);
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
					devicenumList.remove(clientId);
					return 0;
				}
			} else {
				devicenumList.remove(clientId);
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
						logger.warn("ip:" + channel.getRemoteAddress() + "---桢起始上传有误");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				logger.info("桢起始上传没毛病");
				int cmd = buffer.get() & 0xff;//命令
				logger.info("cmd=" + DisposeUtil.intToHex(cmd));
				
				//-----桩号 start 编码BCD-----
				byte[] deviceDateBytes = new byte[8];
				buffer.get(deviceDateBytes);
				String devicenum = AESUtil.BCD_String(deviceDateBytes);
				buffer.position(0);
				DisposeUtil.printDeviceDataInfo(devicenum, buffer, true);
				buffer.position(11);
				//-----桩号 end-----
				
				byte encryptionWay = buffer.get();//加密方式 0x01-数据不加密，0x02-加密
				byte[] lenbytes = new byte[2];
				buffer.get(lenbytes);
				int datalen = DisposeUtil.converData(lenbytes);//数据域长度
				byte[] datas = new byte[datalen];
				try {
					buffer.get(datas);
				} catch (Exception e) {
					System.out.println("data不足");
					return;
				}
				buffer.position(2);
				byte[] sumdatas = new byte[datalen + 12];
				try {
					buffer.get(sumdatas);
				} catch (Exception e) {
					System.out.println("校验码-data不足");
					return;
				}
//				byte clacSumVal = SendMsgUtil.clacSumVal(sumdatas);
				byte clacSumVal = 0;
				byte sum = buffer.get();
				if ((sum & 0xff) != (clacSumVal & 0xff)) {
					System.out.println("校验码错误");
					return ;
				}
				buffer.position(14);
				byte[] dateBytes = new byte[6];
				buffer.get(dateBytes);
				String deviceDataTime = DisposeUtil.disposeDate(dateBytes);
				//有无回复 1-有、2无
//				if (cmd == 0x01) {//桩请求连接 1
//					clientMap.put(devicenum, client);
//					if (!devicenumList.contains(devicenum)) {
//						devicenumList.add(devicenum);
//					}
//					SendMsgUtil.parse_0x01(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//					System.out.println(111);
//				} else if (cmd == 0x03) {//登录信息
//					System.out.println(222);
//					sendMsgUtil.parse_0x03(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else if (cmd == 0x05) {//桩请求对时命令
//					System.out.println(333);
//					SendMsgUtil.parse_0x05(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else if (cmd == 0x07) {//桩回复对时命令
//					System.out.println(444);
//					SendMsgUtil.parse_0x07(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else if (cmd == 0x09) {//桩遥信
//					System.out.println(555);
//					SendMsgUtil.parse_0x09(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else if (cmd == 0x0A) {//桩遥测
//					System.out.println(666);
//					SendMsgUtil.parse_0x0A(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else if (cmd == 0x0C) {//桩心跳
//					System.out.println(777);
//					SendMsgUtil.parse_0x0C(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else if (cmd == 0x1C) {//桩回复预约命令
//					System.out.println(888);
//					SendMsgUtil.parse_0x1C(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else if (cmd == 0x1E) {//桩回复取消预约
//					System.out.println(999);
//					SendMsgUtil.parse_0x1E(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else if (cmd == 0x20) {//桩回复充电命令
//					System.out.println(101010);
//					SendMsgUtil.parse_0x20(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else if (cmd == 0x21) {//桩启动充电结果
//					System.out.println(111111);
//					System.out.println("桩启动充电结果 success");
//					SendMsgUtil.parse_0x21(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else if (cmd == 0x25) {//充电桩工作信息
//					System.out.println(121212);
//					SendMsgUtil.parse_0x25(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else if (cmd == 0x23) {//桩上送充电订单
//					System.out.println(131313);
//					sendMsgUtil.parse_0x23(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else if (cmd == 0x33) {//历史充电订单
//					System.out.println(141414);
//					SendMsgUtil.parse_0x33(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else if (cmd == 0x36) {//桩回复新 IP 地址设置
//					System.out.println(151515);
//					SendMsgUtil.parse_0x36(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else if (cmd == 0x38) {//桩回复计费模型设置
//					System.out.println(161616);
//					SendMsgUtil.parse_0x38(devicenum, channel, buffer, encryptionWay, datalen, deviceDataTime);
//				} else {
//					System.out.println(171717);
//					return;
//				}
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
				if (Server.clientMap.containsValue(client)) {
					Server.clientMap.remove(client);
				}
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