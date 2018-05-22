package com.yysoft.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.dao.StockDAO;
import com.yysoft.entity.Finance;
import com.yysoft.util.OutInfos;
import com.yysoft.xmlparser.EastMoneyPageParser;
import com.yysoft.xmlparser.JRJPageParser;


/**
 * 功能描述：东方财富财报表采集入口.
 * 资产：http://emweb.securities.eastmoney.com/NewFinanceAnalysis/zcfzbAjax?companyType=4&reportDateType=0&reportType=1&code=sh600992
 * 现金流量表：http://emweb.securities.eastmoney.com/NewFinanceAnalysis/xjllbAjax?companyType=4&reportDateType=0&reportType=1&code=sz002839
 * 利润表：http://emweb.securities.eastmoney.com/NewFinanceAnalysis/lrbAjax?companyType=4&reportDateType=0&reportType=1&code=sh600992
 * @author Huanyan.Lu
 * @date:2018年3月25日
 * @time:上午11:30:16
 * @version:1.0
 */
@SuppressWarnings("deprecation")
public class EastMoneyHttpUtils2 {
	private static Logger logger = LogManager.getLogger(StockDAO.class); //
	private static Logger errorlogger = LogManager.getLogger("fcErrorLoger"); //

	/**
	 * Description: 三大财报采集入口
	 * @param code
	 *            要采集的股票代码
	 * @param name
	 *            要采集的股票简称
	 * @param reportType
	 *            要采集的财报表类型，1-资产负债表，2-现金流量表，3-公司利润表
	 * @param reportDate
	 *            要采集的财报报告期
	 * @param isBank
	 *            是否银行类:是-true ,否-false
	 * @return
	 */
	public static Finance downPage(Finance finance,String code, String name, int reportType, String reportDate) {
//		String urlCode = code.substring(2);
		Finance financeTemp = null;//临时对象
		String responseContent = null;
		String urlLoad = "";// 要采集的URL
//		int gatherYear = Integer.valueOf(reportDate.substring(0, 4));// 要采集的年份
		String typeName = "";//财报表类型名称
		if (reportType == 1) {
			urlLoad = "http://emweb.securities.eastmoney.com/NewFinanceAnalysis/zcfzbAjax?companyType=4&reportDateType=0&reportType=1&code=" + code;// 资产负债表页面链接
			typeName = "东方财富-资产负债表";
		} else if (reportType == 2) {
			urlLoad = "http://emweb.securities.eastmoney.com/NewFinanceAnalysis/xjllbAjax?companyType=4&reportDateType=0&reportType=1&code=" + code;// 现金流量表页面链接
			typeName = "东方财富-现金流量表";
		} else if (reportType == 3) {
			urlLoad = "http://emweb.securities.eastmoney.com/NewFinanceAnalysis/lrbAjax?companyType=4&reportDateType=0&reportType=1&code=" + code;// 利润分配表页面链接
			typeName = "东方财富-利润分配表";
		} else if (reportType == 4) {
			urlLoad = "http://emweb.securities.eastmoney.com/PC_HSF10/FinanceAnalysis/FinanceAnalysisAjax?code=" + code + "&ctype=0";// 利润分配表页面链接
			typeName = "东方财富-主要指标表";
		}
		logger.info(typeName + " 采集地址：" + urlLoad);
		@SuppressWarnings("resource")
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = null;
		HttpResponse response = null;
		HttpEntity entity = null;

		try {
			get = new HttpGet(urlLoad);
			response = httpClient.execute(get);
			entity = response.getEntity(); // 获取响应实体
			if (entity != null) {
				responseContent = EntityUtils.toString(entity, "gbk");
				//test
				if (responseContent.contains("页面没有找到")) {
					logger.info("页面返回'页面没有找到'！code=" + code + ",url=" + urlLoad);
					return null;
				}
				 if(reportType==4){
					 financeTemp = EastMoneyPageParser.parserZYZB(finance,responseContent, code,name, reportType, reportDate);
				 }else{
					 financeTemp = EastMoneyPageParser.parser(finance,responseContent, code,name, reportType, reportDate);
				 }
				 if(financeTemp!=null){
					 finance = financeTemp;
					 return finance;
				 }
			} else {
				errorlogger.error("URL返回的内容为null：" + urlLoad);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		logger.entry();
		Finance f = new Finance();
//		String name = "乐视网";
//		String code = "sz300104";
		String name = "中国联通";
		String code = "sh600050";
//		String name = "国金证券";
//		String code = "sh600109";
//		String name = "中国人寿";
//		String code = "sh601628";
//		String name = "张家港行";
//		String code = "sz002839";
		int reportType = 4;
		String reportDate = "2017-12-31";
		f = downPage(f,code,name,reportType,reportDate);
		OutInfos.OutAllFieldsDemo(f);
		System.out.println("end.");
	}
}