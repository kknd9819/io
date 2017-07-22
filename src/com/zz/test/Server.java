package com.zz.test;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private static ServerSocket serverSocket;

	public static void main(String[] args) throws Exception {
		serverSocket = new ServerSocket(9000);		
		System.out.println("服务已启动，端口是" + serverSocket.getLocalPort());
		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println("有客户端连接,ip地址是: " + socket.getInetAddress().getHostAddress());
			Thread thread = new Thread(new Receive(socket));
			thread.start();
		}

	}

}
