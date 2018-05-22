package tool;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.dao.StockDAO;
import com.yysoft.entity.Finance;
import com.yysoft.entity.Stock;
import com.yysoft.mgr.FinanceGather;
import com.yysoft.util.ConfigParser;
import com.yysoft.util.ReportDateUtils;

/**
 * 功能描述：全量重新采集所有股票的财报数据，采集起始时间以上市前三年为基准
 * @author Huanyan.Lu
 * @date:2016年5月17日
 * @time:下午2:21:06
 * @version:1.0
 */
public class GatherAll {
	private static Logger logger = LogManager.getLogger(GatherAll.class); //
	public static void main(String[] args) {
		new ConfigParser();
		logger.info("\r\n");
		logger.info("===test===");
		 //company对象
		ArrayList<Stock> stocksTab = StockDAO.getStockCodes();
		logger.info("当前上市公司数量:" + stocksTab.size());
		ArrayList<String> reportDates = null;
		//存放采集结果
		ArrayList<Finance> resultFinances = new ArrayList<Finance>();
		//当前日期里已发布的最新的报告期
		String LastReportDate = ReportDateUtils.fetchLastReportDate();
		Finance f = null;
		int succ =0;//成功采集到的财报数量
		int fail =0;//成功采集到的财报数量
		int startYear = 0;//采集起始年份
		int count =0;//计数
		for(Stock stock:stocksTab){
			count++;
			startYear = Integer.valueOf(stock.getAnnouncedDate().substring(0, 4))-3;
			if(startYear<2002){
				startYear = 2002;
			}
			reportDates = ReportDateUtils.buildReportDates2(LastReportDate,startYear);
			for(String reportDate:reportDates){
				
//				if(!stock.getCode().contains("600980")  ){
//					continue;
//				}
//				if(!reportDate.contains("2010-09-30")){
//					continue;
//				}
				f = FinanceGather.down(stock.getCode(), stock.getName(), reportDate);
				if(f.getIsCapture()==1){
					resultFinances.add(f);
					succ++;
					logger.info(count+"-"+stock.getCode() + ",报告期：" + reportDate + "-采集成功。");
				}else{
					fail++;
					logger.info(count+"-"+stock.getCode() + ",报告期：" + reportDate + "-采集失败。");
				}
			}
		}
		//write Finance to dbDataExportRunner.java
		StockDAO.insertDB(resultFinances, false);
		logger.info("===采集成功的财报数量：" + succ);
		logger.info("===采集失败的财报数量：" + fail);
		logger.info("test=====结束");
	}
}
