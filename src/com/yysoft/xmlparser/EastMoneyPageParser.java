package com.yysoft.xmlparser;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.yysoft.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yysoft.entity.Finance;
import com.yysoft.extract.EastMoneyLRExtracter;
import com.yysoft.extract.EastMoneyXJExtracter;
import com.yysoft.extract.EastMoneyZCExtracter;
import com.yysoft.extract.EastMoneyZYZBExtracter;
import com.yysoft.extract.JRJLRExtracter;
import com.yysoft.extract.JRJXJExtracter;
import com.yysoft.extract.JRJZCExtracter;

/**
 * 功能描述： 东方财富
 * 
 * @author Huanyan.Lu
 * @date:2015年12月28日
 * @time:下午6:35:15
 * @version:1.0
 */
public class EastMoneyPageParser {
	private static Logger logger = LogManager.getLogger(EastMoneyPageParser.class); //
	private static Logger errorlogger = LogManager.getLogger("fcErrorLoger"); //
	private static Logger htmllogger = LogManager.getLogger("fcHtmlLoger"); //
	static DecimalFormat df4 = new DecimalFormat("######0.0000"); // 保留四位小数

	/**
	 * Description:东方财富财报表页面数据解析器
	 * @param finance 提取信息对象实例
	 * @param responseContent 读取的HTML内容 
	 * @param code 股票代码
	 * @param name 股票简称
	 * @param reportType  要采集的财报表类型，1-资产负债表，2-现金流量表，3-公司利润表
	 * @param reportDate 要采集的财报报告期
	 * @return
	 */
	public static  Finance parser(Finance finance, String responseContent,String code,String name, int reportType, String reportDate) {
		
		responseContent = responseContent.replaceAll("\\\\\"", "\"");
		responseContent = responseContent.trim();
		responseContent = responseContent.substring(1, responseContent.length()-1);
		Finance financeTemp = new Finance();
//		信息提取
		if(reportType==1){
			financeTemp = EastMoneyZCExtracter.fetch(finance,responseContent,code,name,reportDate);//资产负债表页面
		}else if(reportType==2){
			financeTemp = EastMoneyXJExtracter.fetch(finance,responseContent,code,name,reportDate);//现金流量表页面
		}else if(reportType==3){
			financeTemp = EastMoneyLRExtracter.fetch(finance,responseContent,code,name,reportDate);//利润分配表页面
		}else if(reportType==4){
			financeTemp = EastMoneyZYZBExtracter.fetch(finance,responseContent,code,name,reportDate);//主要指标表页面
		}
		if(financeTemp!=null){
			finance = financeTemp;
			return finance;
		}
		return null;
	}
	
	/**
	 * Description:东方财富财报表页面数据解析器
	 * @param finance 提取信息对象实例
	 * @param responseContent 读取的HTML内容 
	 * @param code 股票代码
	 * @param name 股票简称
	 * @param reportType  要采集的财报表类型，1-资产负债表，2-现金流量表，3-公司利润表， 4-主要指标
	 * @param reportDate 要采集的财报报告期
	 * @return
	 */
	public static  Finance parserZYZB(Finance finance, String responseContent,String code,String name, int reportType, String reportDate) {
		
//		responseContent = responseContent.replaceAll("\\\\\"", "\"");
//		responseContent = responseContent.trim();
//		responseContent = responseContent.substring(1, responseContent.length()-1);
		Finance financeTemp = new Finance();
//		信息提取
		if(reportType==1){
			financeTemp = EastMoneyZCExtracter.fetch(finance,responseContent,code,name,reportDate);//资产负债表页面
		}else if(reportType==2){
			financeTemp = EastMoneyXJExtracter.fetch(finance,responseContent,code,name,reportDate);//现金流量表页面
		}else if(reportType==3){
			financeTemp = EastMoneyLRExtracter.fetch(finance,responseContent,code,name,reportDate);//利润分配表页面
		}else if(reportType==4){
			financeTemp = EastMoneyZYZBExtracter.fetch(finance,responseContent,code,name,reportDate);//主要指标表页面
		}
		if(financeTemp!=null){
			finance = financeTemp;
			return finance;
		}
		return null;
	}
}
