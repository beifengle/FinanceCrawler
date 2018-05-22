package com.yysoft.xmlparser;


import java.text.DecimalFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yysoft.entity.Finance;
import com.yysoft.extract.QQLRExtracter;
import com.yysoft.extract.QQXJExtracter;
import com.yysoft.extract.QQZCExtracter;
import com.yysoft.util.StringUtils;

/**
 * 功能描述：
 * 腾讯财经网站财报表页面解析器
 * @author Huanyan.Lu
 * @date:2015年12月28日
 * @time:下午6:35:15
 * @version:1.0
 */
public class QQPageParser {
	private static Logger logger = LogManager.getLogger(QQPageParser.class); //
	private static Logger errorlogger = LogManager.getLogger("fcErrorLoger"); //
	private static Logger htmllogger = LogManager.getLogger("fcHtmlLoger"); //
	static DecimalFormat df6 = new DecimalFormat("######0.000000"); // 保留四位小数

	
	/**
	 * Description:腾讯财经网站财报表页面数据提取器.要采集的财报表类型，1-资产负债表，2-现金流量表，3-公司利润表
	 * @param finance 提取信息对象实例
	 * @param responseContent 读取的HTML内容 
	 * @param code 股票代码
	 * @param name 股票简称
	 * @param reportType  要采集的财报表类型，1-资产负债表，2-现金流量表，3-公司利润表
	 * @param reportDate 要采集的财报报告期
	 * @return
	 */
	public static  Finance parser(Finance finance, String responseContent,String code,String name, int reportType, String reportDate,boolean isBank) {
		Finance financeTemp = new Finance();
		
		Document doc = Jsoup.parse(responseContent);
		Elements elements1 = doc.select("[class=page]");
		Element elements2 = elements1.select("tbody").get(2);
		Elements trs = elements2.getElementsByTag("tr");// 获取所有子tr标签
		logger.info("要解析的财报日期：" + reportDate + "子tr标签数量：" + trs.size());// 2015.12.29日，有68标准行。
		if (trs.size() <= 0) {
			errorlogger.error(reportDate + ",code=" + code + ",tr标签数量小于0");
			return null;
		}
		String data[][] = new String[trs.size()][7];// 二维数组，存放提取数据项.注意这里为了与金融街对齐，实际上只有4列，下列从弄了5列从第二列开始保存数据
		int line = 0;// 行号
		int count = 1;
		for (Element tr : trs) {
//			logger.info(code +"(财报日期:" + reportDate + ")-第 " + (count++) + " 个tr。");
			Elements tds = tr.getElementsByTag("td");// 获取所有子td标签
			logger.info(code + "(财报日期:" + reportDate +  ") "   + "data[" + line + "][0]:" + tr.getElementsByTag("th").text());
			int column = 1;// 列号
			for (Element td : tds) {
				String tdtext = td.text().replaceAll("--", "").trim().replaceAll("\\?", "").replaceAll(",", "");
				data[line][column] = tdtext;
				if (StringUtils.isNotEmpty(data[line][column])) {
					if (data[line][column].endsWith("万元")) {
						data[line][column] = data[line][column].replace("万", "").replace("元", "");
						data[line][column] = df6.format(Double.valueOf(data[line][column]) * 10000) + "";
					} else if (data[line][column].endsWith("元")) {
						data[line][column] = data[line][column].replace("元", "");
					}
				}
//				logger.info(code + "(财报日期：" + reportDate +  ") "   + "data[" + line + "][" + column + "]:" + tdtext);
				column++;
			}
			line++;
		}
		//赋值
		if(reportType==1){
			financeTemp = QQZCExtracter.fetch(finance,data,code,name,reportDate,isBank);//资产负债表页面
		}else if(reportType==2){
			financeTemp = QQXJExtracter.fetch(finance,data,code,name,reportDate,isBank);//现金流量表页面
		}else if(reportType==3){
			financeTemp = QQLRExtracter.fetch(finance,data,code,name,reportDate,isBank);//利润分配表页面
		}
		if(financeTemp!=null){
			return finance;
		}
		return null;
	}
	
}
