package com.zz.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileUtil {

	/** 绝对路径 */
	private String absolutePath;

	/** 文件长度 */
	private long length;

	/** 文件名 */
	private String fileName;

	/** 所有可读的文件信息 */
	private List<FileUtil> fileUtils = new ArrayList<FileUtil>();

	/**
	 * 获得所有可读的文件
	 * 
	 * @param rootPath
	 *            根路径
	 * @return
	 */
	public List<FileUtil> getAllFile(String rootPath) {
		File file = new File(rootPath);
		if (file.exists() && file.canRead()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File f : files) {
					if (f.isDirectory()) {
						getAllFile(f.getAbsolutePath());
					} else {
						FileUtil fileUtil = new FileUtil();
						fileUtil.setAbsolutePath(f.getAbsolutePath());
						fileUtil.setFileName(f.getName());
						fileUtil.setLength(f.length());
						fileUtils.add(fileUtil);
					}
				}
			}

		}
		return fileUtils;
	}

	public static void main(String[] args) {
		FileUtil fileUtil = new FileUtil();
		List<FileUtil> list = fileUtil.getAllFile("E:\\a\\");
		for (Iterator<FileUtil> iterator = list.iterator(); iterator.hasNext();) {
			FileUtil f = iterator.next();
			System.out.println("文件绝对路径: " + f.getAbsolutePath());
			System.out.println("文件名称: " + f.getFileName());
			System.out.println("文件长度: " + f.getLength());
			System.out.println("一共有多少文件 : " + list.size() + "个");
			System.out.println("=============================================");
		}
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<FileUtil> getFileUtils() {
		return fileUtils;
	}

	public void setFileUtils(List<FileUtil> fileUtils) {
		this.fileUtils = fileUtils;
	}

}
