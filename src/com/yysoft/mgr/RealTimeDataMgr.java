package com.yysoft.mgr;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.dao.StockDAO;
import com.yysoft.data.handle.JRJFilter;
import com.yysoft.entity.Finance;
import com.yysoft.entity.ReportNotice;
import com.yysoft.entity.Stock;
import com.yysoft.http.HttpUtils;
import com.yysoft.util.ConfigParser;
import com.yysoft.util.DateUtils;
import com.yysoft.util.ReportDateUtils;

/**
 * 功能描述：实时财报数据采集
 * @author Huanyan.Lu
 * @date:2016年2月24日
 * @time:上午10:41:48
 * @version:1.0
 */
public class RealTimeDataMgr {
	
	private static Logger logger = LogManager.getLogger("fcRealTimeLoger"); //
	private static Logger errorlogger = LogManager.getLogger("fcErrorLoger"); //
	/**
     *   根据金融街网年报季报判断是否有财报更新
	 *	 http://stock.jrj.com.cn/action/getNoticeListByDiffCondition.jspa?vname=_notic_list&type=1&page=1&psize=3000
	 *	 http://stock.jrj.com.cn/stockprompt/stock_gegugonggao_list.shtml#type=1
	 *	  
	 *
	 */
	public static void exec(){
		logger.info("\r\n");
		logger.info("===沪深A股个股新财务报表采集===");
		 //company对象
		//new ConfigParser();
		ArrayList<Stock> stocksTab = StockDAO.getStockCodes();
		logger.info("当前上市公司数量:" + stocksTab.size());
		
		
		//jrj街页面年报、季报栏目
		HashSet<ReportNotice>  jrjReports = HttpUtils.loadJRJNoticeInfo();
		//东方财富财报发布栏目，要采集的报告期(采集近两个季报)
		String reportDates[] = DateUtils.getReportDateForCrawler();
		//东方财富网页面年报、季报栏目
		HashSet<ReportNotice>  eastmoneyReports = new HashSet<ReportNotice>(); 
		HashSet<ReportNotice>  rnsTemp =null;//东方财富网
		for(int i=0;i<reportDates.length-1;i++){
			rnsTemp = HttpUtils.loadEastmoneyNoticeInfo(reportDates[i]);
			eastmoneyReports.addAll(rnsTemp);
		}
		
		//合并、去重(金融街和东方财富网监视页面)
		jrjReports = mergeByCodeStr(jrjReports,eastmoneyReports);
		//过滤退市的、非沪深A股、非正式财报.
		jrjReports = JRJFilter.selectAJRJData(jrjReports, stocksTab);
		logger.info("当前监控页面的正式财报/公告的数量比:" + jrjReports.size() + "/3000");
		
		
		//需要重采的股票数量(对已采集过一次，做第二次采集)
		HashSet<ReportNotice> reGathercodes = StockDAO.reGather();
		logger.info("需要重采的股票数量：" + reGathercodes.size());
		
		//合并、去重
		jrjReports = mergeByObject(jrjReports,reGathercodes);
		
		 //stock_financial_gather_monitor对象
		ArrayList<ReportNotice> jrjTab = StockDAO.getJRJCodesTab();
		logger.info("监控页面正式财报历史数据库表记录总数:" + jrjTab.size());
		 //需要更新财报的股票
		HashSet<ReportNotice> newRNS = JRJFilter.removeDuplicate(jrjReports, jrjTab);
		//标记采集成功的财报
		HashSet<ReportNotice> succGathers = new HashSet<ReportNotice>();
		logger.info("基于监控页面历史数据的新资源总数：" + newRNS.size() );

		int succ =0;//成功采集到的财报数量
		int fail =0;//成功采集到的财报数量
		
		String reportDate ="";//要采集的季度
		//存放采集结果
		ArrayList<Finance> resultFinances = new ArrayList<Finance>();
		
		int count =0;//计数
		String gatherFlag = "(第一次采集)";
		//开始采集有更新的财报
		for(ReportNotice rn:newRNS){
			Finance f = null;
			count++;
			//test
//			if(!rn.getCode().contains("sh600777")){
//			continue;
//		}
			
			//已经采集过第二次，不再采集
			if(rn.getNum()==2){
				continue;
			}
			if(rn.getYear()==0 || rn.getQuarter()==0){
				errorlogger.info("所采集年度或季度编号转换有异常！");
				errorlogger.info("pdfurl:"+rn.getPdfURL());
				errorlogger.info("Title:"+rn.getTitle());
				errorlogger.info("Year:"+rn.getYear());
				errorlogger.info("Quarter:"+rn.getQuarter());
				continue;
			}
			reportDate = ReportDateUtils.convertReportDate(rn.getYear(), rn.getQuarter());
			logger.info("---"+ count + "---（共"+ newRNS.size() +"个）," + rn.getCode()+",报告期："+reportDate +"开始采集！");
			f = FinanceGather.down(rn.getCode(), rn.getName(), reportDate);
			//把三网数据保存到临时表
			StockDAO.insertDBTEMP(f.getFtemp());
			if(rn.getNum()==1){
				gatherFlag="(第二次采集)";
			}else{
				gatherFlag = "(第一次采集)";
			}
			// 判断是否成功采集到数据
			if(f.getIsCapture()==1){
				f.setNum(rn.getNum());
				logger.info("---"+ count + "---（共"+ newRNS.size() +"个）," + rn.getCode()+",报告期：" + reportDate +"  采集成功！"+gatherFlag);
				resultFinances.add(f);
				succ++;
				succGathers.add(rn);
			}else{
				logger.info("---" + count + "---（共"+ newRNS.size() +"个）," + rn.getCode()+",报告期：" + reportDate +"  采集失败！"+gatherFlag);
				fail++;
			}
			logger.info("\r\n");
		}
		
		//write Finance to db
		StockDAO.insertDB(resultFinances, true);
		
		//write monitor to db
		StockDAO.updateMonitorPage(succGathers);
		
		logger.info("===采集成功的财报数量：" + succ);
		logger.info("===采集失败的财报数量：" + fail);
		logger.info("\r\n");
	}
	
	
	/**
	 * Description:合并两个集合，以rn1为主
	 * @param rn1
	 * @param rn2
	 * @return
	 */
	public static HashSet<ReportNotice> mergeByCodeStr(HashSet<ReportNotice> rn1,HashSet<ReportNotice> rn2){
		String codePublish = "";
		for(ReportNotice rn:rn1){
			codePublish +=rn.getCode()+rn.getYear()+rn.getQuarter()+rn.getPublishDate()+",";
		}
		for(ReportNotice rn:rn2){
			if(!codePublish.contains(rn.getCode()+rn.getYear()+rn.getQuarter()+rn.getPublishDate())){
				rn1.add(rn);
				codePublish +=rn.getCode()+rn.getYear()+rn.getQuarter()+rn.getPublishDate()+",";
			}
		}
		return rn1;
	}
	
	/**
	 * Description:合并两个集合，以rn1为主,并修改标记。
	 * @param rn1
	 * @param rn2
	 * @return
	 */
	public static HashSet<ReportNotice> mergeByObject(HashSet<ReportNotice> rns1,HashSet<ReportNotice> rns2){
		String codePublish = "";
		for(ReportNotice rn1:rns1){
			codePublish  =rn1.getCode()+rn1.getYear()+rn1.getQuarter()+rn1.getPublishDate();
			for(ReportNotice rn2:rns2){
				if(codePublish.contains(rn2.getCode()+rn2.getYear()+rn2.getQuarter()+rn2.getPublishDate())){
					rn1.setNum(rn2.getNum());//数据库表中存在的值有1或2
				}
			}
		}
		return rns1;
	}
	
	public static void main(String[] args) {
		logger.entry();
		new ConfigParser();
		exec();
		}
}
