package com.yysoft.data.handle;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.dao.StockDAO;
import com.yysoft.entity.Finance;
import com.yysoft.util.ConfigParser;


/**
 * 功能描述：把采集到的初始数据导入计算库表stock_finance
 * @author Huanyan.Lu
 * @date:2016年3月1日
 * @time:上午10:57:07
 * @version:1.0
 */
public class DataExport {
	private static Logger logger = LogManager.getLogger("fcImportLoger"); //
	public static void exec(){
		
		ArrayList<Finance> fis = StockDAO.getNewMetaFinance();
		int flag = 0;
		int count = 0;
		int num1=0;
		int num2=0;
		int num3=0;
		logger.info("=====新采集财报数据同步=====");
		
		
		for(Finance fi:fis){
			System.out.println(fi);
			count++;
			logger.info(count +"-财报数据同步-"  );
			flag = StockDAO.importToDCFinance(fi);
			if(flag==1){
				num1++;
				logger.info(fi.getCode() + ",年份-季度：" + fi.getYear() + "-" + fi.getQuarter()  +",成功同步(第一次采集)");
				StockDAO.updatePublishFinance(fi.getID());
			}else if(flag==2){
				num2++;
				logger.info(fi.getCode() + ",年份-季度：" + fi.getYear() + "-" + fi.getQuarter()  +",成功同步(第二次采集)");
				StockDAO.updatePublishFinance(fi.getID());
			}else{
				num3++;
				logger.info(fi.getCode() + ",年份-季度：" + fi.getYear() + "-" + fi.getQuarter()  +",同步失败！！！");
			}
		}
		logger.info("---数据同步结果---");
		logger.info("成功同步记录(第一次采集)：" + num1);
		logger.info("成功同步记录(第二次采集)：" + num2);
		logger.info("同步异常记录：" + num3);
		logger.info("财报总数量：" + fis.size());
	}
	public static void main(String[] args) {
		new ConfigParser();
		exec();
	}
}
