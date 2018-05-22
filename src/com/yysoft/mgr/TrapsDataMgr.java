package com.yysoft.mgr;
import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.dao.StockDAO;
import com.yysoft.entity.Finance;
import com.yysoft.entity.Stock;
import com.yysoft.util.ConfigParser;
import com.yysoft.util.DateUtils;

/**
 * 功能描述：定时补采财报。 5月1号，09月1号，11月1号各季报年报发布截止日期的第二天，全量遍历查找缺采的财报
 * @author Huanyan.Lu
 * @date:2016年5月16日
 * @time:上午10:41:48
 * @version:1.0
 */
public class TrapsDataMgr {
	
	private static Logger logger = LogManager.getLogger("fcCompletionLoger"); //
	private static Logger errorlogger = LogManager.getLogger("fcErrorLoger"); //
	
	/**
	 * Description:
	 * @param 
	 */
	public static void exec(){
		logger.info("\r\n");
		 String currDate = DateUtils.getDateTime(new Date());
		 //缺采的股票
		 ArrayList<Stock> stocks = null;
		 //存放采集结果
		 ArrayList<Finance> resultFinances = new ArrayList<Finance>();
		 Finance f = null;
		 int succ =0;//成功采集到的财报数量
		 int fail =0;//成功采集到的财报数量
		 int year = Integer.valueOf(currDate.substring(0, 4));
		 if(currDate.contains("-05-01") || currDate.contains("-05-04")){//5月1号 .(添加05-04，防止五一放假关机错过)
			 String completeReportDate[] = {(year-1)+"-12-31", year + "-03-31"};//需要补全的财报期
			 for(String reportDate:completeReportDate){
				 stocks = StockDAO.getStockCodesForGather(reportDate);
				 //开采遍历采集
				 for(Stock stock:stocks){
					 f = FinanceGather.down(stock.getCode(), stock.getName(), reportDate);
					 if(f!=null){
							logger.info(stock.getCode() + "---报告期：" + reportDate +"采集成功！(第一次采集)");
							resultFinances.add(f);
							succ++;
						}else{
							logger.info(stock.getCode() + "---报告期：" + reportDate +"采集失败！(第一次采集)");
							fail++;
						}
				 }
			 }
		 }else if(currDate.contains("-09-01") || currDate.contains("-11-01")){//，9月1号，11月1号
			 String reportDate = "";
			 if(currDate.contains("-09-01")){
				 reportDate = year + "-06-30";
			 }else if(currDate.contains("-11-01")){
				 reportDate = year + "-09-30";
			 }else{
				 errorlogger.info("补采日期异常：" + currDate );
				
			 }
			 stocks = StockDAO.getStockCodesForGather(reportDate);
			 //开采遍历采集
			 for(Stock stock:stocks){
				 f = FinanceGather.down(stock.getCode(), stock.getName(), reportDate);
				 if(f!=null){
						logger.info(stock.getCode() + "---报告期：" + reportDate +"采集成功！(第一次采集)");
						resultFinances.add(f);
						succ++;
					}else{
						logger.info(stock.getCode() + "---报告期：" + reportDate +"采集失败！(第一次采集)");
						fail++;
					}
			 }
		 }
		 
		//write Finance to db
		StockDAO.insertDB(resultFinances, false);
		logger.info("===采集成功的财报数量：" + succ);
		logger.info("===采集失败的财报数量：" + fail);
		logger.info("\r\n");
	}
	public static void main(String[] args) {
		logger.entry();
		new ConfigParser();
		exec();
		}
}
