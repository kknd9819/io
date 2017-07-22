package com.zz.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;

import com.zz.io.OSEnum;

public class Receive implements Runnable{
	
	/**管道流*/
	private Socket socket;
	
	/** 截取路径 */
	private String substring;

	/** 文件名 */
	private String fileName;
	
	/** 目标路径 */
	private String targetPath; // 目标路径 = G:\127.0.0.1\盘符名称\文件夹路径\文件名.扩展名
	
	/**输出缓冲流*/
	private BufferedOutputStream out = null;

	@Override
	public void run() {
		try {
			DataInputStream input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			int read = 0;
			long fileLength = 0;
			long length = 0;
			int total = 0;
			byte[] bytes = new byte[8192];
			while(true){
				if(total > input.readInt()) break;
				String absolutePath = input.readUTF();
				long readLong = input.readLong();
				substring = getSubstring(absolutePath);
				fileName = getFileName(absolutePath);
				targetPath = getTargetPath(socket, OSEnum.WINDOWS_G.getName());
				File file = new File(targetPath);
				if(!file.exists()){
					file.getParentFile().mkdirs();
				}
				if(out == null){
					out = new BufferedOutputStream(new FileOutputStream(targetPath));
					fileLength = readLong;
				}
				if(length < fileLength){
					read = input.read(bytes, 0, bytes.length);
					if(read != -1){
						out.write(bytes,0,read);
						out.flush();
						length += read;
					}
				} else {
					length = 0;
					length += read;
					out.close();
					out = new BufferedOutputStream(new FileOutputStream(targetPath));
					fileLength = readLong;
					read = input.read(bytes, 0, bytes.length);
					if(read != -1){
						out.write(bytes,0,read);
						out.flush();
						length += read;
					}
					total ++;
				}
			}
			System.out.println("服务端接收完毕!");
			socket.shutdownInput();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Receive(Socket socket){
		this.socket = socket;
	}
	
	private String getFileName(String absolutePath) {
		return absolutePath.substring(absolutePath.lastIndexOf("\\") + 1, absolutePath.length());
	}

	private String getSubstring(String absolutePath) {
		return absolutePath.substring(0, 1)
				+ absolutePath.substring(2, absolutePath.length() - getFileName(absolutePath).length() - 1);
	}

	private String getTargetPath(Socket socket, String OSPath) {
		return OSPath + socket.getInetAddress().getHostAddress() + "\\" + substring + "\\" + fileName;
	}

	public String getSubstring() {
		return substring;
	}



	public void setSubstring(String substring) {
		this.substring = substring;
	}



	public String getFileName() {
		return fileName;
	}



	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	public String getTargetPath() {
		return targetPath;
	}



	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}


}
