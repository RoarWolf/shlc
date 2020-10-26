package com.hedong.hedongwx.thread.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.hedong.hedongwx.utils.ReplyMsgUtil;

public class Client {

	public Socket socket;
	private AsynchronousSocketChannel channel;

	public Client() throws IOException {
		channel = AsynchronousSocketChannel.open();
		channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		Client client = new Client();
//		client.connect("127.0.0.1", 14700, new CompletionHandler<Void, AsynchronousSocketChannel>() {
//		client.connect("121.196.187.251", 14700, new CompletionHandler<Void, AsynchronousSocketChannel>() {
		client.connect("121.196.187.251", 14700, new CompletionHandler<Void, AsynchronousSocketChannel>() {
//		client.connect("139.224.255.156", 14700, new CompletionHandler<Void, AsynchronousSocketChannel>() {

			@Override
			public void completed(Void result, AsynchronousSocketChannel channel) {
				client.read(channel, client);

				System.out.println("连接成功");
				while (true) {
					ByteBuffer buffer = client.cmd_25();
					client.writeBuffer(buffer);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}

			@Override
			public void failed(Throwable exc, AsynchronousSocketChannel channel) {
				exc.printStackTrace();

			}
		});

		Thread.sleep(30000);
	}

	public void connect(String ip, int port, CompletionHandler<Void, AsynchronousSocketChannel> handler) {
		channel.connect(new InetSocketAddress(ip, port), this.channel, handler);
	}

	public ByteBuffer cmd_25() {
		ByteBuffer buffer = ByteBuffer.allocate(65522);
//		byte[] bytes = new byte[]{0x4a, 0x58, 0x01, 0x00 , 0x01 , 0x52 , 0x00 , 0x00 , 0x01 , 0x00 , 0x01 , 0x01 , 0x10 , 0x00 , 0x14 , 0x09 , 0x1d , 0x0b , 0x38 , 0x2a , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00 , 0x00, 0x5a};
//		byte[] bytes = new byte[]{(byte) 0x4a,(byte) 0x58,(byte) 0x23,(byte) 0x10,(byte) 0x27,(byte) 0x52,(byte) 0x01,(byte) 0x02,(byte) 0x03,(byte) 0x00,(byte) 0x01,(byte) 0x01,(byte) 0xb2,(byte) 0x00,(byte) 0x14,(byte) 0x0a,(byte) 0x0d,(byte) 0x13,(byte) 0x19,(byte) 0x06,(byte) 0x00,(byte) 0x01,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x31,(byte) 0x30,(byte) 0x32,(byte) 0x37,(byte) 0x35,(byte) 0x32,(byte) 0x30,(byte) 0x31,(byte) 0x30,(byte) 0x32,(byte) 0x30,(byte) 0x33,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x31,(byte) 0x32,(byte) 0x30,(byte) 0x31,(byte) 0x30,(byte) 0x31,(byte) 0x33,(byte) 0x31,(byte) 0x39,(byte) 0x32,(byte) 0x32,(byte) 0x31,(byte) 0x38,(byte) 0x36,(byte) 0x36,(byte) 0x30,(byte) 0x37,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x31,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x15,(byte) 0x00,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x4c,(byte) 0x4e,(byte) 0x42,(byte) 0x53,(byte) 0x43,(byte) 0x55,(byte) 0x33,(byte) 0x48,(byte) 0x33,(byte) 0x4a,(byte) 0x47,(byte) 0x33,(byte) 0x30,(byte) 0x31,(byte) 0x32,(byte) 0x36,(byte) 0x37,(byte) 0x14,(byte) 0x0a,(byte) 0x0d,(byte) 0x13,(byte) 0x16,(byte) 0x11,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x64,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x01,(byte) 0x14,(byte) 0x0a,(byte) 0x0d,(byte) 0x13,(byte) 0x16,(byte) 0x12,(byte) 0x01,(byte) 0x00,(byte) 0x00,(byte) 0x01,(byte) 0x01,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x06,(byte) 0x01,(byte) 0x00,(byte) 0x00,(byte) 0x01,(byte) 0x00,(byte) 0x00,(byte) 0x01,(byte) 0x00,(byte) 0x00,(byte) 0x01,(byte) 0x00,(byte) 0x00,(byte) 0x01,(byte) 0x00,(byte) 0x00,(byte) 0x01,(byte) 0x00,(byte) 0x00,(byte) 0xeb};
//		byte[] bytes = new byte[]{(byte) 0x4a,(byte) 0x58,(byte) 0x25,(byte) 0x10,(byte) 0x27,(byte) 0x52,(byte) 0x01,(byte) 0x02,(byte) 0x03,(byte) 0x00,(byte) 0x01,(byte) 0x01,(byte) 0x18,(byte) 0x00,(byte) 0x14,(byte) 0x0a,(byte) 0x0f,(byte) 0x15,(byte) 0x1f,(byte) 0x23,(byte) 0x00,(byte) 0x8d,(byte) 0x0f,(byte) 0xc9,(byte) 0x00,(byte) 0x20,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x2c,(byte) 0x01,(byte) 0x00,(byte) 0x00,(byte) 0x23,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x01,(byte) 0x04};
		byte[] bytes = new byte[]{(byte) 0x4a,(byte) 0x58,(byte) 0x0a,(byte) 0x10,(byte) 0x27,(byte) 0x52,(byte) 0x01,(byte) 0x02,(byte) 0x03,(byte) 0x00,(byte) 0x02,(byte) 0x01,(byte) 0x48,(byte) 0x00,(byte) 0x14,(byte) 0x0a,(byte) 0x14,(byte) 0x08,(byte) 0x25,(byte) 0x18,(byte) 0xe8,(byte) 0x0f,(byte) 0xd1,(byte) 0x0f,(byte) 0xe6,(byte) 0x0f,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0xe4,(byte) 0x57,(byte) 0x00,(byte) 0x00,(byte) 0x50,(byte) 0x48,(byte) 0x4b,(byte) 0x00,(byte) 0x04,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x02,(byte) 0x04,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x31,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x04,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x32,(byte) 0x46,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x04,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x11,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x04,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x32,(byte) 0x48,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x03};
		buffer.put(bytes);

		buffer.flip();
		return buffer;
	}

	private void writeBuffer(ByteBuffer buffer) {
		channel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
			@Override
			public void completed(Integer result, ByteBuffer buffer) {
				if (buffer.hasRemaining()) {
					channel.write(buffer, buffer, this);
				} else {
					// Go back and check if there is new data to write
				}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				exc.printStackTrace();
			}
		});
	}

	private void read(AsynchronousSocketChannel channel, Client client) {
		ByteBuffer input = ByteBuffer.allocate(1024);
		if (!channel.isOpen()) {
			return;
		}

		channel.read(input, input, new CompletionHandler<Integer, ByteBuffer>() {
			@Override
			public void completed(Integer result, ByteBuffer buffer) {
				// if result is negative or zero the connection has been closed
				// or something gone wrong
				if (result < 1) {
					try {
						channel.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					buffer.flip();
					System.out.print("收到：");
					byte cmd = buffer.get(2);
					System.out.println(cmd);
					if (cmd == 1) {
						client.channel.write(ReplyMsgUtil.reply_1());
					}
					read(channel, client);
				}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer buffer) {
				try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
