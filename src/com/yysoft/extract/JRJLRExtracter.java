package com.yysoft.extract;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.entity.Finance;
import com.yysoft.util.StringUtils;

/**
 * 功能描述：利润分配表页面数据提取
 * @author Huanyan.Lu
 * @date:2016年5月11日
 * @time:下午3:08:02
 * @version:1.0
 */
public class JRJLRExtracter {
	private static Logger logger = LogManager.getLogger(JRJLRExtracter.class); //

	
	/**
	 * Description:利润分配表页面数据提取
	 * @param finance 提取信息实例
	 * @param data 读取的页面元信息
	 * @param code 股票代码
	 * @param name 股票简称
	 * @param reportDate 要提取信息的报告期
	 * @return
	 */
	public static  Finance fetch(Finance finance,String data[][],String code,String name,String reportDate){
		logger.info("提取的报告期:"+ reportDate + "===金融街利润分配表页面数据项信息提取===");
		finance.setCode(code);
		finance.setName(name);
		finance.setReportDate(reportDate);//报告期
		finance.setQuarter(StringUtils.getQuarterFromReport(reportDate));
		finance.setYear(finance.getReportDate().substring(0,4));
		
		String dateString = "";
		for(int i=0;i<data[0].length;i++){
			if(data[0][i]==null || !data[0][i].contains(reportDate)){
				continue;
			}
			//有些页面的年度财报里季报重复，导致表格多了一列：http://stock.jrj.com.cn/share,002071,lrfpb_2014.shtml
			//在此做些过滤
			if(dateString.contains(data[0][i])){
				continue;
			}
			dateString +=data[0][i] +",";
			
			
			finance.setTOI(data[1][i]);//营业总收入
			finance.setOperatingIncome(data[2][i]);//营业收入
			
			if(StringUtils.isEmpty(finance.getTOI()) || finance.getTOI().equals("0.0000")){
				finance.setTOI(finance.getOperatingIncome());
			}
			if(StringUtils.isEmpty(finance.getOperatingIncome()) || finance.getOperatingIncome().equals("0.0000")){
				finance.setOperatingIncome(finance.getTOI());
			}
			
			finance.setTOC(data[3][i]);//营业总成本
			finance.setOperatingCost(data[4][i]);//营业成本
			
			if(StringUtils.isEmpty(finance.getTOC()) || finance.getTOC().equals("0.0000")){
				finance.setTOC(finance.getOperatingCost());
			}
			if(StringUtils.isEmpty(finance.getOperatingCost()) || finance.getOperatingCost().equals("0.0000")){
				finance.setOperatingCost(finance.getTOC());
			}
			
			finance.setBTAS(data[5][i]);//营业税金及附加
			finance.setMarketingExpen(data[6][i]);//销售费用
			finance.setAdminExpenses(data[7][i]);//管理费用
			finance.setFinancialExpenses(data[8][i]);//财务费用
			finance.setADL(data[9][i]);//资产减值损失
			finance.setInvestIncome(data[12][i]);//投资收益
			finance.setOperatingProfit(data[15][i]);//营业利润
			finance.setNOI(data[16][i]);//营业外收入
			finance.setNOE(data[17][i]);//营业外支出
			finance.setLFDONCA(data[18][i]);//非流动资产处置净损失
			finance.setTotalProfit(data[19][i]);//利润总额
			finance.setITE(data[20][i]);//所得税
			finance.setNetProfit(data[22][i]);//净利润
			finance.setMII(data[23][i]);//少数股东损益
			finance.setONPATP(data[24][i]);//归属于母公司股东的净利润
			finance.setBEPS(data[26][i]);//基本每股收益
			finance.setDEPS(data[27][i]);//稀释每股收益
			
			finance.setLRFlag(1);//标记采集来源
			return finance;
			
		}
		return null;
	}
	
}
