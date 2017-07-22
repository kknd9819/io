package com.zz.test;

/**
 * 操作系统磁盘根目录枚举
 * @author Troublemaker
 *
 */
public enum OSEnum {
	
	WINDOWS_C("C:\\"),WINDOWS_D("D:\\"),WINDOWS_E("E:\\"),
	WINDOWS_F("F:\\"),WINDOWS_G("G:\\"),WINDOWS_H("H:\\"),
	LINUX("\\");
	
	private String name;
	
	public String getName() {
		return name;
	}
	
	private OSEnum(String name) {
		this.name = name;
	}
}
