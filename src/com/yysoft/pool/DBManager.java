package com.yysoft.pool;

public class DBManager {

	public static HConnectionPool connPoolH;
	
	//初始化数据库表
	static{
		// 创建数据库连接库对象
		connPoolH = new HConnectionPool();
		// 新建数据库连接库
		try {
			connPoolH.createPool();
			System.out.println("数据连接成功！");
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		System.out.println("ee");
	}
}
