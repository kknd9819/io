package com.zz.io;

import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Client {

	/** 断点续传的身份令牌 */
	// private static String token;

	
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("127.0.0.1", 9000);
			if (socket.isConnected()) {
				System.out.println("已连接到服务器: " + socket.getInetAddress().getHostAddress());
				System.out.println("请输入磁盘路径,例如D:\\");
				Scanner scanner = new Scanner(System.in);
				String input = scanner.nextLine();
				scanner.close();
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				List<FileUtil> allFile = new FileUtil().getAllFile(input);
				for (Iterator<FileUtil> iterator = allFile.iterator(); iterator.hasNext();) {
					FileUtil f = iterator.next();
					FileInputStream fis = new FileInputStream(f.getAbsolutePath());
					int read = 0;
					byte[] bytes = new byte[8192];
					while((read = fis.read(bytes, 0, bytes.length)) != -1){
						byte[] b = new byte[read];
						b = bytes;
						Data data = new Data();
						data.setAbsolutePath(f.getAbsolutePath());
						data.setLength(f.getLength());
						data.setBytes(b);
						out.writeObject(data);
						out.flush();
					}
					fis.close(); // 文件输入流关闭
				}
				out.close(); // 数据输出流关闭
				socket.close(); // socket关闭
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
