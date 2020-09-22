package com.hedong.hedongwx.thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hedong.hedongwx.dao.CommonHandler;
import com.hedong.hedongwx.dao.Equipmenthandler;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.ParseMsg;
import com.hedong.hedongwx.entity.SendData;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.utils.DisposeUtil;
import com.hedong.hedongwx.utils.NewSendmsgUtil;
import com.hedong.hedongwx.utils.SendMsgUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Server2 {
	public static void main(String[] args) throws IOException {

//		Server server = new Server(14700);
//		server.run();

	}

	@Autowired
	private SendMsgUtil sendMsgUtil;
	@Autowired
	private EquipmentService equipmentService;
	private final Logger logger = LoggerFactory.getLogger(Server2.class);
	private static final List<Client2> connections = Collections.synchronizedList(new ArrayList<Client2>());
	private static ConcurrentHashMap<String, Client2> clientMap = new ConcurrentHashMap<String, Client2>();
	public static ConcurrentHashMap<String, String> codeAndIPMap = new ConcurrentHashMap<String, String>();
	private AsynchronousServerSocketChannel listener;
	private AsynchronousChannelGroup channelGroup;
	private final Queue<SendData> queue = new LinkedList<>();
	private int port;
	private Map<String, Object> msgMap = new HashMap<>();
	public static ParseMsg parseMsg = new ParseMsg();
	private static Map<String, Map> map = new HashMap<>();
	public static Map<String, Map<String,Map<String,String>>> stopmap = new HashMap<>();
	public static Map<String, String> stateMap = new HashMap<>();
	public static Map<String, Map> infoMap = new HashMap<>();
	public static Map<String, Map<String,Map<String,String>>> recycleMap = new HashMap<>();
	public static Map<String, Map> isfreeMap = new HashMap<>();
	public static Map<String, Map<String,String>> everyportMap = new HashMap<>();
	public static Map<String, Map<String, String>> readsystemMap = new HashMap<>();
	public static Map<String,Map<String,Map<String,String>>> islockMap = new HashMap<>();
	public static Map<String,Map<String,String>> offlineMap = new HashMap<>();
	public static Map<String,Map<String,String>> inCoinsMap = new HashMap<>();
	public static Map<String,Map<String,String>> setSystemMap = new HashMap<>();
	public static Map<String,Map<String,String>> maininfoMap = new HashMap<>();
	public static ConcurrentHashMap<Integer,Timer> timerMap = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<Integer,Integer> timerNumMap = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<Long,Timer> chargeTimerMap = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<Long,Integer> chargeTimerNumMap = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String,Map<String,String>> endChargeInfoMap = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String,String> beginChargeInfoMap = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String,Double> oncardBalance = new ConcurrentHashMap<>();
	/** 临界值  */
	public static Map<String, Map<String, String>> thresholdvalue = new HashMap<>();
	/** 上传当前充电桩温度（0x33）  */
	public static Map<String, Map<String, String>> uploadingMap = new HashMap<>();
	/** 获取当前设备的参数  */
	public static Map<String, Map<String, String>> getParameMap = new HashMap<>();
	/** 获取设备设置的参数  */
	public static Map<String, Map<String, String>> getArgumentMap = new HashMap<>();
	/** 报警  */
	public static Map<String, Map<String, String>> alarmMap = new HashMap<>();
	public static short scanTime;
	public static ConcurrentHashMap<String, String> newBeginChargeInfoMap = new ConcurrentHashMap<>();
	
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
		int deviceType = Equipmenthandler.getDeviceType(clientId);
		DisposeUtil.printDeviceDataInfo(clientId, buffer, false);
		buffer.position(0);
		if (deviceType == 1) {
			try {
				if (clientId != null) {
					if (clientMap.get(clientId) != null) {
						Client2 client = clientMap.get(clientId);
						client.sendMsg(buffer);
						return 1;
					} else {
						Equipmenthandler.updateState((byte) 0, "0", clientId);
						System.out.println("连接已断开");
						return 0;
					}
				} else {
					System.out.println("无此连接");
					return 0;
				} 
			} catch (Exception e) {
				Equipmenthandler.updateState((byte) 0, "0", clientId);
				System.out.println("连接已断开--数据发送异常");
				return 0;
			}
		} else {
//			System.out.println("蓝牙设备不发数据包===" + clientId);
			return 0;
		}
	}
	
	public void removeClient(String clientId) {
		logger.info("设备：" + clientId + "开始断开连接");
		if (clientMap.containsKey(clientId)) {
			Client2 client = clientMap.get(clientId);
			try {
				clientMap.remove(clientId);
				connections.remove(client);
				client.channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String hardversion = Equipmenthandler.getEquipmentHardversion(clientId);
			if (hardversion != null) {
				Equipmenthandler.redisDeciceStateAddOrRemove(0, clientId,hardversion);
			}
			logger.info("设备：" + clientId + "断开连接完成");
		} else {
			logger.info("设备：" + clientId + "断开连接失败");
		}
	}

	public boolean isOnline(String clientId) {
		return clientMap.contains(clientId);
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

		logger.info("Running on port : " + port);
//		System.out.println("Running on port : " + port);
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				while (true) {
					Set<Entry<String,Client2>> entrySet = clientMap.entrySet();
					for (Entry<String, Client2> entry : entrySet) {
						Client2 client = entry.getValue();
						Date packTime = client.getPackTime();
						if(new Date().getTime() - packTime.getTime() > 600000) {
							Equipmenthandler.updateState((byte) 0, "0", entry.getKey());
							CommonHandler.equipmess((byte) 0, Equipmenthandler.findUserEquipment(entry.getKey()), entry.getKey());
							try {
								client.channel.close();
								connections.remove(client);
								Equipmenthandler.addCodeOperateLog(entry.getKey(), 1, 2, Equipmenthandler.findUserEquipment(entry.getKey()), 0);
								clientMap.remove(entry.getKey());
							} catch (IOException e) {
								e.printStackTrace();
							}
							String hardversion = Equipmenthandler.getEquipmentHardversion(entry.getKey());
							if (hardversion != null) {
								Equipmenthandler.redisDeciceStateAddOrRemove(0, entry.getKey(),hardversion);
							}
						}
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
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
//							SendMsgUtil.send_0x99(code);
//						}
//					}
//				}
//			}
//		}).start();
	}

	private void handleNewConnection(AsynchronousSocketChannel channel) {
		Client2 client = new Client2(null, channel, new DataReader2() {

			@Override
			public void beforeRead(Client2 client) {

			}

			@Override
			public void onData(Client2 client, ByteBuffer buffer, int bytes) {
				client.setPackTime(new Date());
				buffer.flip();
				DisposeUtil.printDeviceDataInfo(SendMsgUtil.getCodeByIpaddr(channel), buffer, true);
				buffer.position(0);
				byte sop = buffer.get();
				if (sop == 0x66) {
					String codeByIpaddr = SendMsgUtil.getCodeByIpaddr(channel);
					int send_0x99 = SendMsgUtil.send_0x99(codeByIpaddr);
					if (send_0x99 == 0) {
						try {
							client.sendMsg(SendMsgUtil.send_0x85());
							System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss---").format(new Date()) + "设备连接错误，下发信息重新上传标识"
									+ "当前设备IP===" + channel.getRemoteAddress().toString());
						} catch (Exception e) {
							try {
								System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss---").format(new Date()) + "设备连接错误，下发信息重新上传标识，发送失败"
										+ "当前设备IP===" + client.channel.getRemoteAddress().toString());
							} catch (IOException e1) {
							}
							e.printStackTrace();
						}
					}
					return;
				} else if (sop == (byte)0xAA || sop == 0x55) {
					System.out.println("字节头正确，进行以下传输");
				} else {
					System.out.println("字节头传输错误，请重新发起请求");
					return;
				}
				System.out.printf("0x%02x ", sop);
				byte len = buffer.get();
				System.out.printf("0x%02x ", len);
				List<Byte> byteList = new ArrayList<>();
				while (buffer.hasRemaining()) {
					byteList.add(buffer.get());
				}
				if ((len & 0xff) == byteList.size()) {
					System.out.println("len正确");
				} else {
					System.out.println("len错误，以下数据不进行处理");
					return;
				}
				buffer.position(1);
				byte[] datas = new byte[512];
				buffer.get(datas, 0, (len & 0xff));
				byte sum = buffer.get();
				byte checkoutSum = SendMsgUtil.checkoutSum(datas);
				if (sum == checkoutSum) {
					System.out.println("数据发送验证成功");
				} else {
					System.out.println("数据发送验证失败");
					return;
				}
				buffer.position(2);
				byte cmd = buffer.get();//命令
				int addr = buffer.getInt();//从机地址
				String addrHexString = Integer.toHexString(addr);//从机地址转为16进制字符串
				addrHexString = DisposeUtil.completeNum(addrHexString, 8);//8位字符串不够补全
				byte result = buffer.get();
				
				if (cmd == 0x0A) {
					NewSendmsgUtil.parse_0x0A(channel, len, cmd, result, buffer, addrHexString);
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

class Client2 {
	public String id;
	public AsynchronousSocketChannel channel;
	private DataReader2 callback;
	private Date packTime = new Date();
	
	public Date getPackTime() {
		return packTime;
	}

	public void setPackTime(Date packTime) {
		this.packTime = packTime;
	}

	public Client2(String id, AsynchronousSocketChannel channel, DataReader2 callback) {
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
		ByteBuffer input = ByteBuffer.allocate(65522);
		if (!channel.isOpen()) {
			return;
		}
		channel.read(input, input, completionHandler);
	}

	public void run() {
		Client2 client = this;
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

	protected void removeClient(Client2 client) {

	}
}

interface DataReader2 {
	void beforeRead(Client2 client);

	void onData(Client2 client, ByteBuffer buffer, int bytes);

	boolean acceptsMessages();
}