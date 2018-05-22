package com.yysoft.mgr;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.fabric.xmlrpc.base.Array;
import com.yysoft.dao.StockDAO;
import com.yysoft.data.handle.DataExport;
import com.yysoft.entity.Finance;
import com.yysoft.entity.Stock;
import com.yysoft.util.ConfigParser;
import com.yysoft.util.Constant;
import com.yysoft.util.DateUtils;
import com.yysoft.util.ReportDateUtils;

public class HistoricalReportMgr {

	private static Logger logger = LogManager.getLogger("fcHisLoger"); //

	public static void exec() {
		logger.info("===新股财务报表采集（开始）===");
		// 根据数据库表判断：采集尚未采集过的历史财报数据的股票
		final Vector<Stock> stocks = StockDAO.getNeedHistoryFinanceDataCodes();
		int sum = stocks.size();
		// 当前日期里已发布的最新的报告期
		String LastReportDate = ReportDateUtils.fetchLastReportDate();
		ArrayList<String> reportDates = null;
		// 存放采集结果
		ArrayList<Finance> resultFinances = new ArrayList<Finance>();
		// 计算成功采集的股票数量
		HashSet<String> countSucc = new HashSet<String>();
		Finance f = null;
		logger.info("新股数量：" + sum);
		int count = 1;
		int succCode = 0;// 计算成功采集的股票数量
		int failCode = 0;// 计算不成功采集的股票数量
		int succRecord = 0;// 成功采集的记录数
		int failRecord = 0;// 不成功采集的记录数
		for (Stock stock : stocks) {
			logger.info(count + "（共" + sum + "个）>>>>>开始采集：" + stock.getCode());
			reportDates = ReportDateUtils.buildReportDates(LastReportDate, Constant.HISTORYYEAR);
			f = new Finance();
			for (String reportDate : reportDates) {
				logger.info(stock.getCode() + ",报告期：" + reportDate + " -开始采集！");
				f = FinanceGather.down(stock.getCode(), stock.getName(), reportDate);
				//把三网数据保存到临时表
				StockDAO.insertDBTEMP(f.getFtemp());
				if (f.getIsCapture()==1) {
					succRecord++;
					resultFinances.add(f);
					countSucc.add(f.getCode());
					logger.info(stock.getCode() + ",报告期：" + reportDate + " -采集成功！");
				} else {
					failRecord++;
					logger.info(stock.getCode() + ",报告期：" + reportDate + " -采集失败！");
				}
			}
			count +=1;
		}
		// write to db.
		StockDAO.insertDB(resultFinances, true);
		succCode = countSucc.size();
		failCode = sum - countSucc.size();
		logger.info("===成功(新股数量)：" + succCode);
		logger.info("===失败(新股数量)：" + failCode);
		logger.info("===成功(财报记录数)：" + succRecord);
		logger.info("===失败(财报记录数)：" + failRecord);
		logger.info("===新股财务报表采集（结束）===");
		logger.info("\r\n");
	}

	public static void exec2() {
		new ConfigParser();
		ArrayList<Stock> stocks = StockDAO.getStockCodes();
		int sum = stocks.size();
		// 存放采集结果
		ArrayList<Finance> resultFinances = new ArrayList<Finance>();
		// 计算成功采集的股票数量
		HashSet<String> countSucc = new HashSet<String>();
		Finance f = null;
		logger.info("新股数量：" + sum);
		int count = 1;
		int succCode = 0;// 计算成功采集的股票数量
		int failCode = 0;// 计算不成功采集的股票数量
		int succRecord = 0;// 成功采集的记录数
		int failRecord = 0;// 不成功采集的记录数
		String reportDate = "2018-03-31";
		for (Stock stock : stocks) {
			f = new Finance();
			f = FinanceGather.down(stock.getCode(), stock.getName(), reportDate);
			if (f.getIsCapture()==1) {
				succRecord++;
				resultFinances.add(f);
				countSucc.add(f.getCode());
				logger.info(stock.getCode() + ",报告期：" + reportDate + " -采集成功！");
			} else {
				failRecord++;
				logger.info(stock.getCode() + ",报告期：" + reportDate + " -采集失败！");
			}
			count +=1;
		}
		// write to db.
		StockDAO.insertDB(resultFinances, true);
		succCode = countSucc.size();
		failCode = sum - countSucc.size();
		logger.info("===resultFinances：" + resultFinances.size());
		logger.info("===成功(新股数量)：" + succCode);
		logger.info("===失败(新股数量)：" + failCode);
		logger.info("===成功(财报记录数)：" + succRecord);
		logger.info("===失败(财报记录数)：" + failRecord);
		logger.info("===新股财务报表采集（结束）===");
		logger.info("\r\n");
		if (DateUtils.getDate(new Date()).contains(""))
			DataExport.exec();
	}
//	/**
//	 * 新股个股简介采集 
//	 * author:niuguanqun
//	 */
//	public static void exec3(){
//		new ConfigParser();
//		logger.info("===新股财务报表采集（开始）===");
//		// 根据数据库表判断：采集尚未采集过的历史财报数据的股票
//		final Vector<Stock> stocks = StockDAO.getNeedHistoryFinanceDataCodes();
//		//url的结果集
//		HashMap<String, String> urlMap = new HashMap<>();
//		for(Stock stock : stocks){
//			urlMap.put(CrawlerNewStockUpdate.getCrawlerURL(stock.getCode(), 1, 4), stock.getCode());
//		}
//		try {
//			CrawlerNewStockUpdate.getXueqiuPage(urlMap);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String[] args) {
		new ConfigParser();
		exec();
	}
}
