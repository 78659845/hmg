package com.oracle.gdms.util;

import java.util.ResourceBundle;

public class Factory {
	private Factory() {
		// TODO 自动生成的构造函数存根
	}

	private static Factory fac = new Factory();
	public static Factory getInstance() {
		return fac;
	}
	
	private static ResourceBundle rb = null;
	static {
		rb = ResourceBundle.getBundle("config/application");//读取配置文件
	}
	public Object getObject(String key) {
		String clsname = rb.getString(key);
		try {
			return Class.forName(clsname).newInstance();
		} catch (InstantiationException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}
}
