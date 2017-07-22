package com.zz.io;

import java.io.Serializable;

public class Data implements Serializable {

	private static final long serialVersionUID = 7296088786938277792L;

	/** 绝对路径 */
	private String absolutePath;

	/** 文件长度 */
	private long length;

	/** 数据包 */
	private byte[] bytes;

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
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

}
