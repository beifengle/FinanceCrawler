package com.yysoft.system;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.data.handle.DataExport;
import com.yysoft.data.handle.FinancialSkchange;
import com.yysoft.mgr.AcronymGather;
import com.yysoft.mgr.CrawlerNewFinance;
import com.yysoft.mgr.CrawlerNewsflashList;
import com.yysoft.mgr.DividendComparisonJRJ2;
import com.yysoft.mgr.DividendGather;
import com.yysoft.mgr.HistoricalReportMgr;
import com.yysoft.mgr.NewStockReport;
import com.yysoft.mgr.RealTimeDataMgr;
import com.yysoft.mgr.TrapsDataMgr;
import com.yysoft.util.ConfigParser;
import com.yysoft.util.Constant;
import com.yysoft.util.DateUtils;

public class runner {

	private static Logger logger = LogManager.getLogger("fcProcessLoger"); //执行流程日志
	static long startTime = 0;
	static long endTime = 0;
	static String currDate ="";
	public static void main(String[] args) {
		logger.entry();
		new ConfigParser();
		
		
		String time[] =null;
		if(StringUtils.isNotEmpty(Constant.STARTTIME)){
			time = Constant.STARTTIME.split(":");
		}
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time[0])); // 控制时
        calendar.set(Calendar.MINUTE, Integer.valueOf(time[1]));       // 控制分
        calendar.set(Calendar.SECOND, Integer.valueOf(time[2]));       // 控制秒
        Date time2 = calendar.getTime();         // 得出执行任务的时间,
        Timer timer = new Timer();
        
        
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	currDate = DateUtils.getDate(new Date());
            	logger.info("=====即将开始采集财报（" + currDate +")=====");
            	new ConfigParser();
            	
            	// 1、股票首字母缩写，采集更新stock_company name和acronym
            	logger.info("1-----股票拼音首字母采集（开始）-----");
            	startTime = System.currentTimeMillis();
            	AcronymGather.exec();
            	endTime = System.currentTimeMillis();
            	logger.info("1-----股票拼音首字母采集（结束）-----耗时：" +(endTime-startTime)/1000 +"s");
            	
            	//新股上市，采集之前几期财报
            	logger.info("2-----新上市股票财务报表采集（开始）-----");
            	startTime = System.currentTimeMillis();
            	HistoricalReportMgr.exec();
            	endTime = System.currentTimeMillis();
            	logger.info("2-----新上市股票财务报表采集（结束）-----耗时：" +(endTime-startTime)/1000 +"s");


            	// 3、current data (每天实时监控金融街和东方财富网是否有新财报发布)
            	logger.info("3-----个股新财务报表采集（开始）-----");
            	startTime = System.currentTimeMillis();
            	RealTimeDataMgr.exec();
            	endTime = System.currentTimeMillis();
            	logger.info("3-----个股新财务报表采集（结束）-----耗时：" +(endTime-startTime)/1000 +"s");
            	
            	// 4、 4月1号，9月1号，11月1号各季报年报发布截止日期的第二天，全量遍历查找缺采的财报
            	logger.info("4-----季末缺漏财务报表采集（开始）-----");
            	startTime = System.currentTimeMillis();
            	if(currDate.contains("/05/01") ||currDate.contains("/05/04") || currDate.contains("/09/01")||currDate.contains("/11/01")){
            		TrapsDataMgr.exec();
            	}else{
            		logger.info("采集日期没到： 5月1号，9月1号，11月1。");
            	}
            	endTime = System.currentTimeMillis();
            	logger.info("4-----季末缺漏财务报表采集（结束）-----耗时：" +(endTime-startTime)/1000 +"s");
            	

            	logger.info("5-----公司点评（开始）-----");
            	startTime = System.currentTimeMillis();
            	CrawlerNewsflashList.downPage("");
            	endTime = System.currentTimeMillis();
            	logger.info("5-----公司点评（结束）-----耗时：" +(endTime-startTime)/1000 +"s");

            	
            	
            	// 2、history data .
            	//财报采集出错,先屏蔽
            	/*
            	logger.info("2-----新上市股票财务报表采集（开始）-----");
            	startTime = System.currentTimeMillis();
            	HistoricalReportMgr.exec();
            	endTime = System.currentTimeMillis();
            	logger.info("2-----新上市股票财务报表采集（结束）-----耗时：" +(endTime-startTime)/1000 +"s");
        		
            	// 3、current data (每天实时监控金融街和东方财富网是否有新财报发布)
            	logger.info("3-----个股新财务报表采集（开始）-----");
            	startTime = System.currentTimeMillis();
            	RealTimeDataMgr.exec();
            	endTime = System.currentTimeMillis();
            	logger.info("3-----个股新财务报表采集（结束）-----耗时：" +(endTime-startTime)/1000 +"s");
            	
            	// 4、 4月1号，9月1号，11月1号各季报年报发布截止日期的第二天，全量遍历查找缺采的财报
            	logger.info("4-----季末缺漏财务报表采集（开始）-----");
            	startTime = System.currentTimeMillis();
            	if(currDate.contains("/05/01") ||currDate.contains("/05/04") || currDate.contains("/09/01")||currDate.contains("/11/01")){
            		TrapsDataMgr.exec();
            	}else{
            		logger.info("采集日期没到： 5月1号，9月1号，11月1。");
            	}
            	endTime = System.currentTimeMillis();
            	logger.info("4-----季末缺漏财务报表采集（结束）-----耗时：" +(endTime-startTime)/1000 +"s");
            	*/
         		// 5、 采集下来的元数据同步到计算中心 
            	logger.info("5-----新采集财务报表同步（开始）-----");
            	startTime = System.currentTimeMillis();
            	if(DateUtils.getDate(new Date()).contains(""))
        		DataExport.exec();
        		endTime = System.currentTimeMillis();
            	logger.info("5-----新采集财务报表同步（结束）-----耗时：" +(endTime-startTime)/1000 +"s");
            	
            	/*
            	 //采集金融界中的财报修正值,判断公司主体是否发生变化
        		logger.info("6-----财报修正值采集任务（开始）-----");
            	startTime = System.currentTimeMillis();
            	DividendComparisonJRJ2.gather2();
            	DividendComparisonJRJ2.gather();
            	//主体发生标记
            	FinancialSkchange.exec();
        		endTime = System.currentTimeMillis();
        		logger.info("6-----财报修正值采集任务（结束）-----耗时：" +(endTime-startTime)/1000 +"s");
            	*/
            	//6、每天更新和采集股息数据
            	logger.info("7-----个股股息采集任务（开始）-----");
            	startTime = System.currentTimeMillis();
            	DividendGather.gather();//金融界采集
            	DividendGather.gather2();//金融界中报采集
            	DividendGather.gatherEAST(DateUtils.getDateBefore(new Date(), 14));//东方财富:从今天往前两周时间开始采集.
            	DividendGather.exec2();
        		DividendGather.exec();
        		endTime = System.currentTimeMillis();
        		logger.info("7-----个股股息采集任务（结束）-----耗时：" +(endTime-startTime)/1000 +"s");
        		
            	logger.info("=====本轮财务报表采集结束=====");
            	logger.info("\r\n");
            }
        },time2, 1000 * 60 * 60 * Constant.TIMEDEDAY);// 这里设定将延时每天固定执行.
		logger.exit();
	}
}
