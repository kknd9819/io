package com.zz.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
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
			ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			Data data = null;
			long total = 1;
			long length = 0;
			long fileLength = 0;
			while(true){
				data = (Data)input.readObject();
				if(total > data.getTotal()) break;		
				substring = getSubstring(data.getAbsolutePath());
				fileName = getFileName(data.getAbsolutePath());
				targetPath = getTargetPath(socket, OSEnum.WINDOWS_G.getName());
				File file = new File(targetPath);
				if(!file.exists()){
					file.getParentFile().mkdirs();
				}
				if(out == null){
					out = new BufferedOutputStream(new FileOutputStream(targetPath));
					fileLength = data.getLength();
				}
				if(length < fileLength){
					out.write(data.getBytes());
					out.flush();
					length += data.getBytes().length;
				} else {
					length = 0;
					length += data.getBytes().length;
					out.close();
					out = new BufferedOutputStream(new FileOutputStream(targetPath));
					fileLength = data.getLength();
					out.write(data.getBytes());
					out.flush();
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
