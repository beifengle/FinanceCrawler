package com.yysoft.extract;


import java.text.DecimalFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.entity.Finance;
import com.yysoft.util.StringUtils;

/**
 * 功能描述：利润分配表页面数据提取
 * @author Huanyan.Lu
 * @date:2018年3月18日
 * @time:下午3:08:02
 * @version:1.0
 */
public class SinaLRExtracter {
	private static Logger logger = LogManager.getLogger(SinaLRExtracter.class); //
	static DecimalFormat df6 = new DecimalFormat("######0.000000"); // 保留四位小数
	
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
		logger.info("提取的报告期:"+ reportDate + "===Sina利润分配表页面数据项信息提取===");
		String dateString = "";
		for(int i=0;i<data[0].length;i++){
			if(data[0][i]==null || !data[0][i].contains(reportDate)){
				continue;
			}
			dateString +=data[0][i] +",";
			finance.setCode(code);
			finance.setName(name);
			finance.setReportDate(reportDate);//报告期
			finance.setQuarter(StringUtils.getQuarterFromReport(reportDate));
			finance.setYear(finance.getReportDate().substring(0,4));
			
			if(data.length==31){//一般
				//判断此页关键值是否存在，不存在则不提取并返回null
				if(StringUtils.isEmpty(data[22][i])){//归属于母公司股东的净利润为null
					return null;
				}
				if(StringUtils.isEmpty(data[2][i]) && StringUtils.isEmpty(data[3][i])){//营业收入\营业总收入为null
					return null;
				}
				if(data[2][i].trim().equals("0") && data[3][i].trim().equals("0")){//营业收入\营业总收入为null
					return null;
				}
				finance.setTOI(data[2][i]);//营业总收入
				finance.setOperatingIncome(data[3][i]);//营业收入
				if(StringUtils.isEmpty(finance.getTOI())){
					finance.setTOI(finance.getOperatingIncome());
				}
				if(StringUtils.isEmpty(finance.getOperatingIncome())){
					finance.setOperatingIncome(finance.getTOI());
				}
				finance.setTOC(data[4][i]);//营业总成本
				finance.setOperatingCost(data[5][i]);//营业成本
				
				if(StringUtils.isEmpty(finance.getTOC())){
					finance.setTOC(finance.getOperatingCost());
				}
				if(StringUtils.isEmpty(finance.getOperatingCost())){
					finance.setOperatingCost(finance.getTOC());
				}
				
				finance.setBTAS(data[6][i]);//营业税金及附加
				finance.setMarketingExpen(data[7][i]);//销售费用
				finance.setAdminExpenses(data[8][i]);//管理费用
				finance.setFinancialExpenses(data[9][i]);//财务费用
				finance.setADL(data[10][i]);//资产减值损失
				finance.setInvestIncome(data[12][i]);//投资收益
				finance.setOperatingProfit(data[15][i]);//营业利润
				finance.setNOI(data[16][i]);//营业外收入
				finance.setNOE(data[17][i]);//营业外支出
				finance.setLFDONCA(data[18][i]);//非流动资产处置净损失
				finance.setTotalProfit(data[19][i]);//利润总额
				finance.setITE(data[20][i]);//所得税
				finance.setNetProfit(data[21][i]);//净利润
				finance.setMII(data[23][i]);//少数股东损益
				finance.setONPATP(data[22][i]);//归属于母公司股东的净利润
				
				if(StringUtils.isNotEmpty(data[25][i]) && !data[25][i].trim().equals("0")){
					finance.setBEPS(df6.format(Double.valueOf(data[25][i]) / 10000) + "" );//基本每股收益
				}
				if(StringUtils.isNotEmpty(data[26][i]) && !data[26][i].trim().equals("0")){
					finance.setDEPS(df6.format(Double.valueOf(data[26][i]) / 10000) + "" );//稀释每股收益
				}
			}else if(data.length==34){//银行业
				//判断此页关键值是否存在，不存在则不提取并返回null
				if(StringUtils.isEmpty(data[25][i])){//归属于母公司股东的净利润为null
					return null;
				}
				if(StringUtils.isEmpty(data[2][i]) ){//营业收入\营业总收入为null
					return null;
				}
				if(data[2][i].trim().equals("0") ){//营业收入\营业总收入为null
					return null;
				}
//				finance.setTOI(data[3][i]);//营业总收入
				finance.setOperatingIncome(data[2][i]);//营业收入
				
				if(StringUtils.isEmpty(finance.getTOI())){
					finance.setTOI(finance.getOperatingIncome());
				}
				if(StringUtils.isEmpty(finance.getOperatingIncome())){
					finance.setOperatingIncome(finance.getTOI());
				}
				
				finance.setTOC(data[14][i]);//营业总成本
				finance.setOperatingCost(data[14][i]);//营业成本/营业支出
				
				if(StringUtils.isEmpty(finance.getTOC())){
					finance.setTOC(finance.getOperatingCost());
				}
				if(StringUtils.isEmpty(finance.getOperatingCost())){
					finance.setOperatingCost(finance.getTOC());
				}
				
				finance.setBTAS(data[15][i]);//营业税金及附加
//				finance.setMarketingExpen(data[7][i]);//销售费用
				finance.setAdminExpenses(data[16][i]);//管理费用
//				finance.setFinancialExpenses(data[9][i]);//财务费用
				finance.setADL(data[17][i]);//资产减值损失
				finance.setInvestIncome(data[10][i]);//投资收益
				finance.setOperatingProfit(data[19][i]);//营业利润
				finance.setNOI(data[20][i]);//营业外收入
				finance.setNOE(data[21][i]);//营业外支出
//				finance.setLFDONCA(data[18][i]);//非流动资产处置净损失
				finance.setTotalProfit(data[22][i]);//利润总额
				finance.setITE(data[23][i]);//所得税
				finance.setNetProfit(data[24][i]);//净利润
				finance.setMII(data[26][i]);//少数股东损益
				finance.setONPATP(data[25][i]);//归属于母公司股东的净利润
				
				if(StringUtils.isNotEmpty(data[28][i]) && !data[28][i].trim().equals("0")){
					finance.setBEPS(df6.format(Double.valueOf(data[28][i]) / 10000) + "" );//基本每股收益
				}
				if(StringUtils.isNotEmpty(data[29][i]) && !data[29][i].trim().equals("0")){
					finance.setDEPS(df6.format(Double.valueOf(data[29][i]) / 10000) + "" );//稀释每股收益
				}
			}else if(data.length==35){//证券
				//判断此页关键值是否存在，不存在则不提取并返回null
				if(StringUtils.isEmpty(data[26][i])){//归属于母公司股东的净利润为null
					return null;
				}
				if(StringUtils.isEmpty(data[2][i]) && StringUtils.isEmpty(data[2][i])){//营业收入\营业总收入为null
					return null;
				}
				if(data[2][i].trim().equals("0") && data[2][i].trim().equals("0")){//营业收入\营业总收入为null
					return null;
				}
//				finance.setTOI(data[2][i]);//营业总收入
				finance.setOperatingIncome(data[2][i]);//营业收入
				
				if(StringUtils.isEmpty(finance.getTOI())){
					finance.setTOI(finance.getOperatingIncome());
				}
				if(StringUtils.isEmpty(finance.getOperatingIncome())){
					finance.setOperatingIncome(finance.getTOI());
				}
				
				finance.setTOC(data[15][i]);//营业总成本
				finance.setOperatingCost(data[15][i]);//营业成本
				
				if(StringUtils.isEmpty(finance.getTOC())){
					finance.setTOC(finance.getOperatingCost());
				}
				if(StringUtils.isEmpty(finance.getOperatingCost())){
					finance.setOperatingCost(finance.getTOC());
				}
				
				finance.setBTAS(data[16][i]);//营业税金及附加
//				finance.setMarketingExpen(data[7][i]);//销售费用
				finance.setAdminExpenses(data[17][i]);//管理费用
//				finance.setFinancialExpenses(data[9][i]);//财务费用
				finance.setADL(data[18][i]);//资产减值损失
				finance.setInvestIncome(data[10][i]);//投资收益
				finance.setOperatingProfit(data[20][i]);//营业利润
				finance.setNOI(data[21][i]);//营业外收入
				finance.setNOE(data[22][i]);//营业外支出
//				finance.setLFDONCA(data[18][i]);//非流动资产处置净损失
				finance.setTotalProfit(data[23][i]);//利润总额
				finance.setITE(data[24][i]);//所得税
				finance.setNetProfit(data[25][i]);//净利润
				finance.setMII(data[27][i]);//少数股东损益
				finance.setONPATP(data[26][i]);//归属于母公司股东的净利润
				
				if(StringUtils.isNotEmpty(data[29][i]) && !data[29][i].trim().equals("0")){
					finance.setBEPS(df6.format(Double.valueOf(data[29][i]) / 10000) + "" );//基本每股收益
				}
				if(StringUtils.isNotEmpty(data[30][i]) && !data[30][i].trim().equals("0")){
					finance.setDEPS(df6.format(Double.valueOf(data[30][i]) / 10000) + "" );//稀释每股收益
				}
			}else if(data.length==42){//保险行业
				//判断此页关键值是否存在，不存在则不提取并返回null
				if(StringUtils.isEmpty(data[33][i])){//归属于母公司股东的净利润为null
					return null;
				}
				if(StringUtils.isEmpty(data[2][i]) && StringUtils.isEmpty(data[2][i])){//营业收入\营业总收入为null
					return null;
				}
				if(data[2][i].trim().equals("0") && data[2][i].trim().equals("0")){//营业收入\营业总收入为null
					return null;
				}
				finance.setTOI(data[2][i]);//营业总收入
				finance.setOperatingIncome(data[2][i]);//营业收入
				
				if(StringUtils.isEmpty(finance.getTOI())){
					finance.setTOI(finance.getOperatingIncome());
				}
				if(StringUtils.isEmpty(finance.getOperatingIncome())){
					finance.setOperatingIncome(finance.getTOI());
				}
				
				finance.setTOC(data[13][i]);//营业总成本
				finance.setOperatingCost(data[13][i]);//营业成本
				
				if(StringUtils.isEmpty(finance.getTOC())){
					finance.setTOC(finance.getOperatingCost());
				}
				if(StringUtils.isEmpty(finance.getOperatingCost())){
					finance.setOperatingCost(finance.getTOC());
				}
				
				finance.setBTAS(data[21][i]);//营业税金及附加
//				finance.setMarketingExpen(data[7][i]);//销售费用
				finance.setAdminExpenses(data[23][i]);//管理费用
//				finance.setFinancialExpenses(data[9][i]);//财务费用
				finance.setADL(data[10][i]);//资产减值损失
				finance.setInvestIncome(data[12][i]);//投资收益
				finance.setOperatingProfit(data[15][i]);//营业利润
				finance.setNOI(data[28][i]);//营业外收入
				finance.setNOE(data[29][i]);//营业外支出
//				finance.setLFDONCA(data[18][i]);//非流动资产处置净损失
				finance.setTotalProfit(data[30][i]);//利润总额
				finance.setITE(data[31][i]);//所得税
				finance.setNetProfit(data[32][i]);//净利润
				finance.setMII(data[34][i]);//少数股东损益
				finance.setONPATP(data[33][i]);//归属于母公司股东的净利润
				
				if(StringUtils.isNotEmpty(data[36][i]) && !data[36][i].trim().equals("0")){
					finance.setBEPS(df6.format(Double.valueOf(data[36][i]) / 10000) + "" );//基本每股收益
				}
				if(StringUtils.isNotEmpty(data[37][i]) && !data[37][i].trim().equals("0")){
					finance.setDEPS(df6.format(Double.valueOf(data[37][i]) / 10000) + "" );//稀释每股收益
				}
			}else{
				logger.info("新浪-利润表-数组长度匹配不到。data.length=" + data.length);
			}
			
			finance.setLRFlag(4);//标记采集来源
			return finance;
			
		}
		return null;
	}
	
}
