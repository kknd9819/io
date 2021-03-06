package com.zz.io;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TransferClient {

	private static ArrayList<String> fileList = new ArrayList<String>();

//	private String sendFilePath = Constants.SEND_FILE_PATH;
	
	private static String IP_Adress = "127.0.0.1";

	private String ip2;

	/**
	 * 带参数的构造器，用户设定需要传送文件的文件夹
	 * 
	 * @param filePath
	 */
	public TransferClient(String filePath) {
		getFilePath(filePath);
	}

	/**
	 * 不带参数的构造器。使用默认的传送文件的文件夹
	 */
	public TransferClient() {
		OSEnum[] values = OSEnum.values();
		for(OSEnum e : values){
			getFilePath(e.getName());
		}
	}

	public void service(String ip) {
		ip2 = ip;
		if(ip2 != null ||!ip2.equals("")){
			IP_Adress = ip2;
		}
		ExecutorService executorService = Executors.newCachedThreadPool();
		Vector<Integer> vector = getRandom(fileList.size());
		for(Iterator<Integer> iterator = vector.iterator();iterator.hasNext();){
			String filePath = fileList.get(iterator.next().intValue());
			executorService.execute(sendFile(filePath));
		}
	}

	private void getFilePath(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				getFilePath(files[i].getAbsolutePath());
			} else {
				fileList.add(files[i].getAbsolutePath());
			}
		}
	}

	private Vector<Integer> getRandom(int size) {
		Vector<Integer> v = new Vector<Integer>();
		Random r = new Random();
		boolean b = true;
		while (b) {
			int i = r.nextInt(size);
			if (!v.contains(i))
				v.add(i);
			if (v.size() == size)
				b = false;
		}
		return v;
	}

	private static Runnable sendFile(final String filePath) {
		return new Runnable() {

			private Socket socket = null;
			private String ip = IP_Adress;
			private int port = Constants.DEFAULT_BIND_PORT;

			public void run() {	
				if (createConnection()) {
					System.out.println("开始发送文件:" + filePath);
					File file = new File(filePath);
					int bufferSize = 8192;
					byte[] buf = new byte[bufferSize];
					try {
						DataInputStream fis = new DataInputStream(
								new BufferedInputStream(new FileInputStream(filePath)));
						DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

						dos.writeUTF(file.getName());
						dos.flush();
						dos.writeLong(file.length());
						dos.flush();

						int read = 0;
						int passedlen = 0;
						long length = file.length(); // 获得要发送文件的长度
						while ((read = fis.read(buf)) != -1) {
							passedlen += read;
							System.out
									.println("已经完成文件 [" + file.getName() + "]百分比: " + passedlen * 100L / length + "%");
							dos.write(buf, 0, read);
						}

						dos.flush();
						fis.close();
						dos.close();
						socket.close();
						System.out.println("文件 " + filePath + "传输完成!");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			private boolean createConnection() {
				try {
					socket = new Socket(ip, port);
					System.out.println("连接服务器成功！");
					return true;
				} catch (Exception e) {
					System.exit(0);
					return false;
				}
			}

		};
	}

	public static void main(String[] args) {
		System.out.println("请输入服务器的ip地址或域名: ");
		Scanner input = new Scanner(System.in);
		String ip = input.nextLine();
		System.out.println("请输入你要拷贝的文件夹,例如 D:\\example\\");
		System.out.println("如果不输入则默认拷贝整个磁盘");
		String value = input.nextLine();
		input.close();
		if(value == null || value.equals("")){
			new TransferClient().service(ip);
		} else {
			new TransferClient(value).service(ip);
		}
		
	}
}