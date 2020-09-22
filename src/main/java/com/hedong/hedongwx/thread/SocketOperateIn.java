package com.hedong.hedongwx.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 多线程处理socket接收的数据
 * 
 * @author RoarWolf
 * 
 */
public class SocketOperateIn extends Thread {
	private Socket socket;

	public SocketOperateIn(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {

			InputStream in = socket.getInputStream();

			PrintWriter out = new PrintWriter(socket.getOutputStream());
			// OutputStream out = socket.getOutputStream();

			// BufferedReader wt = new BufferedReader(new
			// InputStreamReader(System.in));

			while (true) {
				// 读取客户端发送的信息
				String strXML = "";
				byte[] temp = new byte[1024];
				int length = 0;
				while ((length = in.read(temp)) != -1) {
					ByteBuffer buffer = ByteBuffer.wrap(temp);
					byte sop = buffer.get();
					System.out.println(Integer.toHexString(Byte.toUnsignedInt(sop)));
					byte len = buffer.get();
					byte cmd = buffer.get();
					byte result = buffer.get();
					byte[] data = new byte[len - 3];
					buffer.get(data, 0, len - 3);
					String msg = new String(data);
					System.out.println(msg);
					// byte[] bs = ServerInfoUtils.packagingMessage();
					// out.write(bs,0,bs.length);
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					System.out.println(sf.format(new Date()) + ":" + "ip" + socket.getInetAddress() + ":"
							+ socket.getPort() + "发送" + new String(temp, 0, length));
					break;
				}
				if ("end".equals(strXML)) {
					// System.out.println("准备关闭socket");
					break;
				}

				// System.out.println("客户端发来：" + strXML.toString());

				// MethodHandler mh = new
				// MethodHandler(ReadXML.readXML(strXML.toString()));
				// String resultXML = mh.getResultXML();
				// System.out.println("返回："+resultXML.toString());

				// if(!"".equals(resultXML)){
				// out.print(resultXML);
				// out.flush();
				// out.close();
				// }

			}
			socket.close();
			// System.out.println("socket stop.....");

		} catch (IOException ex) {

		} finally {

		}
	}
}