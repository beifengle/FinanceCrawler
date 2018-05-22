package com.yysoft.mgr;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.dao.StockDAO;
import com.yysoft.entity.Finance;
import com.yysoft.entity.FinanceLog;
import com.yysoft.http.EastMoneyHttpUtils;
import com.yysoft.http.EastMoneyHttpUtils2;
import com.yysoft.http.JRJHttpUtils;
import com.yysoft.http.QQHttpUtils;
import com.yysoft.http.SinaHttpUtils;
import com.yysoft.util.FinanceTool;
import com.yysoft.util.OutInfos;
import com.yysoft.util.StringUtils;

/**
 * 功能描述：财报采集主入口
 * @author Huanyan.Lu
 * @date:2016年5月12日
 * @time:下午4:50:15
 * @version:1.0
 */
public class FinanceGather {
	private static Logger logger = LogManager.getLogger("fcProcessLoger"); //执行流程日志
	/**
	 * Description:财报采集主入口.一个完整的财报包含三个独立采集的三个报表页面：资产负债表、流量现金表、利润分配表
	 * （每个报表以金融街的采集为主，若前者采集失败，则以后者腾讯财经采集为准，且字段信息有多少要多少）
	 * @param code 要采集财报的股票代码
	 * @param name 要采集财报的股票简称
	 * @param reportDate 要采集财报的报告期
	 * @return
	 */
	@SuppressWarnings("unused")
	public static Finance down(String code,String name,String reportDate){
		
		Finance finance = new Finance();
		boolean isBank =false;//是否是银行行业
		int count =0;//计算三大表是否都采集成功
		logger.info("----------");
		if(name.contains("银行")){
			isBank = true;
		}
		
		//FinanceLog 跟踪采集日志
		FinanceLog   fl = new FinanceLog();
		fl.setCode(code);
		fl.setName(name);
		fl.setReportDate(reportDate);
		fl.setLOG("");
		
		ArrayList<Finance> f_temps = new ArrayList<Finance>();//存放三大网站来源三大财报数据，最后存到financial_temp临时表
		//一. ==金融街采集===
		//最终三网站数据汇总到金融街实例对象上
		Finance f_jrj = new Finance();
		f_jrj.setCode(code);
		f_jrj.setName(name);
		f_jrj.setReportDate(reportDate);
		f_jrj.setQuarter(StringUtils.getQuarterFromReport(reportDate));
		f_jrj.setYear(f_jrj.getReportDate().substring(0,4));
		f_jrj.setWebsite("jrj");
		
		Finance f_temp = null;
		//1-资产负债页面采集
		f_temp = JRJHttpUtils.downPage(f_jrj, code, name, 1, reportDate);
		if(f_temp==null){
			logger.info(code+ ",报告期："+reportDate + " -资产负债表采集-失败！（采集源：金融街网）" );
		}else{
			logger.info(code+ ",报告期："+reportDate + " -资产负债表采集-成功！（采集源：金融街网）" );
			f_jrj = f_temp;
			fl.setJRJZCFlag(1);
		}
		//2-现金流量表
		f_temp = JRJHttpUtils.downPage(f_jrj, code, name, 2, reportDate);
		if(f_temp==null){
			logger.info(code+ ",报告期："+reportDate + " -现金流量表采集-失败！（采集源：金融街网）" );
		}else{
			logger.info(code+ ",报告期："+reportDate + " -现金流量表采集-成功！（采集源：金融街网）" );
			f_jrj = f_temp;
			fl.setJRJXJFlag(1);
		}
		//3-利润分配表
		f_temp = JRJHttpUtils.downPage(f_jrj, code, name, 3, reportDate);
		if(f_temp==null){
			logger.info(code+ ",报告期："+reportDate + " -利润分配表采集-失败！（采集源：金融街网）" );
		}else{
			logger.info(code+ ",报告期："+reportDate + " -利润分配表采集-成功！（采集源：金融街网）" );
			f_jrj = f_temp;
			fl.setJRJLRFlag(1);
		}
		
		if(fl.getJRJZCFlag()==0 && fl.getJRJXJFlag()==0 && fl.getJRJLRFlag()==0){
			f_jrj.setIsCapture(0);
		}else{
			f_jrj.setIsCapture(1);
		}
		f_temps.add(f_jrj);
		
		//2.===新浪财经采集===
		Finance f_sina = new Finance();
		f_sina.setCode(code);
		f_sina.setName(name);
		f_sina.setReportDate(reportDate);
		f_sina.setQuarter(StringUtils.getQuarterFromReport(reportDate));
		f_sina.setYear(f_jrj.getReportDate().substring(0,4));
		f_sina.setWebsite("sina");
		f_temp = null;
		//1-资产负债页面采集
		f_temp = SinaHttpUtils.downPage(f_sina, code, name, 1, reportDate);
		if(f_temp==null){
			logger.info(code+ ",报告期："+reportDate + " -资产负债表采集-失败！（采集源：新浪网）" );
		}else{
			logger.info(code+ ",报告期："+reportDate + " -资产负债表采集-成功！（采集源：新浪网）" );
			f_sina = f_temp;
			fl.setSINAZCFlag(1);
		}
		//2-现金流量表
		f_temp = SinaHttpUtils.downPage(f_sina, code, name, 2, reportDate);
		if(f_temp==null){
			logger.info(code+ ",报告期："+reportDate + " -现金流量表采集-失败！（采集源：新浪网）" );
		}else{
			logger.info(code+ ",报告期："+reportDate + " -现金流量表采集-成功！（采集源：新浪网）" );
			f_sina = f_temp;
			fl.setSINAXJFlag(1);
		}
		//3-利润分配表
		f_temp = SinaHttpUtils.downPage(f_sina, code, name, 3, reportDate);
		if(f_temp==null){
			logger.info(code+ ",报告期："+reportDate + " -利润分配表采集-失败！（采集源：新浪网）" );
		}else{
			logger.info(code+ ",报告期："+reportDate + " -利润分配表采集-成功！（采集源：新浪网）" );
			f_sina = f_temp;
			fl.setSINALRFlag(1);
		}
		
		if(fl.getSINAZCFlag()==0 && fl.getSINAXJFlag()==0 && fl.getSINALRFlag()==0){
			f_sina.setIsCapture(0);
		}else{
			f_sina.setIsCapture(1);
		}
		f_temps.add(f_sina);
		
		//3.===东方财富采集===
		Finance f_em = new Finance();
		f_em.setCode(code);
		f_em.setName(name);
		f_em.setReportDate(reportDate);
		f_em.setQuarter(StringUtils.getQuarterFromReport(reportDate));
		f_em.setYear(f_jrj.getReportDate().substring(0,4));
		f_em.setWebsite("东方财富");
		f_temp = null;
		//1-资产负债页面采集
		f_temp = EastMoneyHttpUtils2.downPage(f_em, code, name, 1, reportDate);
		if(f_temp==null){
			logger.info(code+ ",报告期："+reportDate + " -资产负债表采集-失败！（采集源：东方财富）" );
		}else{
			logger.info(code+ ",报告期："+reportDate + " -资产负债表采集-成功！（采集源：东方财富）" );
			f_em = f_temp;
			fl.setEASTZCFlag(1);
		}
		//2-现金流量表
		f_temp = EastMoneyHttpUtils2.downPage(f_em, code, name, 2, reportDate);
		if(f_temp==null){
			logger.info(code+ ",报告期："+reportDate + " -现金流量表采集-失败！（采集源：东方财富）" );
		}else{
			logger.info(code+ ",报告期："+reportDate + " -现金流量表采集-成功！（采集源：东方财富）" );
			f_em = f_temp;
			fl.setEASTXJFlag(1);
		}
		//3-利润分配表
		f_temp = EastMoneyHttpUtils2.downPage(f_em, code, name, 3, reportDate);
		if(f_temp==null){
			logger.info(code+ ",报告期："+reportDate + " -利润分配表采集-失败！（采集源：东方财富）" );
		}else{
			logger.info(code+ ",报告期："+reportDate + " -利润分配表采集-成功！（采集源：东方财富）" );
			f_em = f_temp;
			fl.setEASTLRFlag(1);
		}
		
		//3-2 主要指标表
		f_temp = EastMoneyHttpUtils2.downPage(f_em, code, name, 4, reportDate);
		if(f_temp==null){
			logger.info(code+ ",报告期："+reportDate + " -主要指标表采集-失败！（采集源：东方财富）" );
		}else{
			logger.info(code+ ",报告期："+reportDate + " -主要指标表采集-成功！（采集源：东方财富）" );
			f_em = f_temp;
			fl.setEASTLRFlag(1);
		}
		
		if(fl.getEASTZCFlag()==0 && fl.getEASTXJFlag()==0 && fl.getEASTLRFlag()==0){
			f_em.setIsCapture(0);
		}else{
			f_em.setIsCapture(1);
		}
		f_temps.add(f_em);
		///////////////////////////////////////////////////////////////////////////////
		
		
		//把三网数据保存到临时表
		finance.setFtemp(f_temps);
		
		if(f_jrj==null && f_sina==null && f_em==null){
			logger.info(code+ ",报告期："+reportDate + " ---三个网站源采集失败或无内容可采集。" );
			finance.setFl(fl);
			return finance;
		}
		

		
		logger.info(code+ ",报告期："+reportDate + " ---对所采集到的数据源进行整合。" );
		//合并三大采集数据来源
		finance = FinanceTool.mergeFinances(f_sina, f_jrj, f_em, fl);
		
		/**验证此股票报告期是否采集到数据,必须满足以下字段值有值为标准：1-满足;0-不满足
		  * 资产负责表：资产总计,归属母公司股东权益
		  * 利润表：营业收入,归属母公司净利润
         * 现金流量表：现金流量表
		 */
		if(StringUtils.isNotEmpty(finance.getASSETS()) && StringUtils.isNotEmpty(finance.getTEATTSOPC()) && StringUtils.isNotEmpty(finance.getOperatingIncome()) && StringUtils.isNotEmpty(finance.getONPATP())  && StringUtils.isNotEmpty(finance.getNIICACE())){
			finance.setIsCapture(1);
		}
		
		
		return finance;
	}
	public static void main(String[] args) {
//		String name = "乐视网";
//		String code = "sz300104";
//		String name = "工商银行";
//		String code = "sh601398";
//		String name = "国金证券";
//		String code = "sh600109";
//		String name = "中国人寿";
//		String code = "sh601628";
//		String name = "张家港行";
//		String code = "sz002839";
//		String name = "浦东金桥";
//		String code = "sz002922";
		String name = "小天鹅";
		String code = "sz000001";
		String reportDate = "2017-12-31";
		System.out.println("start.");
		Finance f = down(code,name,reportDate);
		OutInfos.OutAllFieldsDemo(f.getFl());
		System.out.println("end.");
		System.out.println("InvestIncome:" + f.getInvestIncome());
		System.out.println(Double.MAX_VALUE);
	}
}
