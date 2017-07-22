package com.zz.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private static int threadNum = 0;
	private static ServerSocket serverSocket;

	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(9000);
			System.out.println("服务已启动,正在等待客户端的连接....");
			while(true){
				Socket socket = serverSocket.accept();
				System.out.println("有客户端连接: " + socket.getInetAddress().getHostAddress());
				threadNum++;
				FileThread thread = new FileThread(socket,threadNum);
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
