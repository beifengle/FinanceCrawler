package com.yysoft.util;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 功能描述：配置文件解析器
 * @author Huanyan.Lu
 * @date:2015年9月17日
 * @time:上午10:16:14
 * @version:1.0
 */
public class ConfigParser {
	public static   int DELIST =10;//退市 
	static String configFilePath = "config.xml";//配置文件名
	private static Logger logger = LogManager.getLogger(ConfigParser.class); // 记录日志
	
	public ConfigParser(){
		init();
		logger.debug(configFilePath  + "程序参数配置初始化完成");
	}

	
	
	private static void init(){

		String configPath = System.getProperty("user.dir");
		String xmlFilePath = configPath + File.separator + configFilePath;
		 
		SAXReader saxReader=null;
		Document document=null;
		Element root=null;
		saxReader = new SAXReader();
		try {
			document=saxReader.read(xmlFilePath);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		root = (Element)document.getRootElement();
		//读取数据库配置dbconfig
		Element dbconfigsValues = root.element("dbconfig");
		Constant.DRIVER = dbconfigsValues.elementText("driver");
		Constant.USERNAME = dbconfigsValues.elementText("username");
		Constant.PASSWORD = dbconfigsValues.elementText("password");
		Constant.DBHOST = dbconfigsValues.elementText("dbhost");
		Constant.INITCONNSIZE = Integer.valueOf(dbconfigsValues.elementText("initConnectionsSize"));
		Constant.INCRECONNSIZE = Integer.valueOf(dbconfigsValues.elementText("increConnectionsSize"));
		Constant.MAXCONNS = Integer.valueOf(dbconfigsValues.elementText("maxConnections"));
		Constant.DCDB = dbconfigsValues.elementText("dcdb");
		Constant.WEBDB = dbconfigsValues.elementText("webdb");
		//读取通用配置config
		Element configsValues = root.element("config");
		Constant.IColumnCode =  configsValues.elementText("icolumncode") ;
		Constant.IColumnName = configsValues.elementText("icolumnname") ;
		Constant.HISTORYYEAR =  Integer.valueOf(configsValues.elementText("historyYear")) ;
		Constant.FILTERWORD =  configsValues.elementText("filterWord").replaceAll("，", "") ;
		Constant.STARTTIME = configsValues.elementText("startTime").replaceAll("：", ":");
		Constant.TIMEDEDAY = Integer.valueOf(configsValues.elementText("timeDelay").replaceAll("：", ":"));
		
		String data[] = Constant.FILTERWORD.split(",");
		Constant.wordsList = java.util.Arrays.asList(data);
		
		
	}
 
	
}
