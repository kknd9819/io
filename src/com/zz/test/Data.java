package com.zz.test;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

public class Data implements Serializable {

	private static final long serialVersionUID = 423302750632404076L;

	/** 绝对路径 */
	private String absolutePath;

	/** 文件长度 */
	private long length;

	/** 数据包 */
	private byte[] bytes;

	/** 文件数量 */
	private long total;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
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

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
}
