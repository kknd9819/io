package com.zz.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

public class Client {

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("127.0.0.1", 9000);
		FileUtil fileUtil = new FileUtil();
		List<FileUtil> list = fileUtil.getAllFile("E:\\a\\");
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		for (Iterator<FileUtil> iterator = list.iterator(); iterator.hasNext();) {
			FileUtil f = iterator.next();
			BufferedInputStream input = new BufferedInputStream(new FileInputStream(f.getAbsolutePath()));
			int read = 0;
			byte[] bytes = new byte[8192];
			while ((read = input.read(bytes, 0, bytes.length)) != -1) {
				out.writeInt(list.size());
				out.writeUTF(f.getAbsolutePath());
				out.writeLong(f.getLength());			
				out.write(bytes, 0, read);
				out.flush();
			}
			input.close();
		}
		out.close();
		socket.close();
		System.out.println("客户端传输结束");
	}
}
