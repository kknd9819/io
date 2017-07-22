package com.zz.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * 线程处理过程
 * 
 * @author X-man
 *
 */
public class FileThread extends Thread {

	/** 线程编号 */
	private int threadNum;

	/** 套接字 */
	private Socket socket;

	/** 文件工具类 */
	private FileUtil fileUtil;

	/** 目标路径 */
	private String targetPath; // 目标路径 = G:\127.0.0.1\盘符名称\文件夹路径\文件名.扩展名

	/** 文件输出流 */
	private FileOutputStream out;

	/** 对象输入流 */
	private ObjectInputStream input;

	/** 截取路径 */
	private String substring;

	/** 文件名 */
	private String fileName;

	@Override
	public void run() {
		System.out.println("线程{" + threadNum + "}：正在传输" + socket.getInetAddress().getHostAddress() + "的数据...");
		try {
			input = new ObjectInputStream(socket.getInputStream());
			Data data = (Data)input.readObject();
			substring = getSubstring(data.getAbsolutePath());
			fileName = getFileName(data.getAbsolutePath());
			targetPath = getTargetPath(socket, OSEnum.WINDOWS_G.getName());
			File file = new File(targetPath);
			if(!file.exists()){
				file.getParentFile().mkdirs();
			}
			out = new FileOutputStream(targetPath);
			out.write(data.getBytes());
			out.flush();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		while (true) {
			try {
				Data data = (Data) input.readObject();
				
			} catch (Exception e) {
				break;
			}
		}
		System.out.println("线程{" + threadNum + "}：" + socket.getInetAddress().getHostAddress() + "传输结束！");

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

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public FileThread(Socket socket, int threadNum) {
		this.socket = socket;
		this.threadNum = threadNum;
	}
}
