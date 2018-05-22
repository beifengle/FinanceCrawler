package com.yysoft.util;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OutInfos {
	private static Logger logger = LogManager.getLogger(OutInfos.class); //
	private static Logger errorlogger = LogManager.getLogger("fcErrorLoger"); //

	/**
	 * 打印输入对象所有属性 Description:
	 * 
	 * @param finance
	 */
	public static void OutAllFieldsDemo(Object finance) {
		Class<?> c = finance.getClass();
		String objectName = c.getName().substring(c.getName().lastIndexOf(".") + 1);
		Field[] fields = c.getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
		}
		String field = null;
		for (Field f : fields) {
			field = f.toString().substring(f.toString().lastIndexOf(".") + 1); // 取出属性名称
			try {
				logger.info(objectName + "." + field + " =" + f.get(finance));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				errorlogger.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 打印输入对象所有属性 Description:非空的都打印
	 * 
	 * @param finance
	 */
	public static void OutAllFieldsNotNullDemo(Object finance) {
		if (finance ==null){
			logger.info("finance Object is null! ");
		}else{
			Class<?> c = finance.getClass();
			String objectName = c.getName().substring(c.getName().lastIndexOf(".") + 1);
			Field[] fields = c.getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
			}
			String field = null;
			for (Field f : fields) {
				field = f.toString().substring(f.toString().lastIndexOf(".") + 1); // 取出属性名称
				try {
					if(f.get(finance)!=null){
						logger.info(objectName + "." + field + " =" + f.get(finance));
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					errorlogger.error(e.getMessage(), e);
				}
			}
		}
		
	}
}
