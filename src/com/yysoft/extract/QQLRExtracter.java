package com.yysoft.extract;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.entity.Finance;
import com.yysoft.util.StringUtils;

/**
 * 功能描述：腾讯财经利润分配表页面数据提取
 * @author Huanyan.Lu
 * @date:2016年5月11日
 * @time:下午3:08:02
 * @version:1.0
 */
public class QQLRExtracter {
	private static Logger logger = LogManager.getLogger(QQLRExtracter.class); //

	
	/**
	 * Description:利润分配表页面数据提取
	 * @param finance 提取信息实例
	 * @param data 读取的页面元信息
	 * @param code 股票代码
	 * @param name 股票简称
	 * @param reportDate 要提取信息的报告期
	 * @return
	 */
	public static  Finance fetch(Finance finance,String data[][],String code,String name,String reportDate,boolean isBank){
		logger.info("提取的报告期："+ reportDate + "===腾讯财经现金流量表页面数据项部分信息提取。===");
		String dateString = "";
		for(int i=1;i<data[0].length;i++){
			if(data[0][i]==null || !data[0][i].contains(reportDate)){
				continue;
			}
			//有些页面的年度财报里季报重复，导致表格多了一列：http://stock.jrj.com.cn/share,002071,lrfpb_2014.shtml
			//在此做些过滤
			if(dateString.contains(data[0][i])){
				continue;
			}
			dateString +=data[0][i] +",";
			
			
			finance.setCode(code);
			finance.setName(name);
			finance.setReportDate(data[0][i]);//报告期
			finance.setQuarter(StringUtils.getQuarterFromReport(data[0][i]));
			finance.setYear(finance.getReportDate().substring(0,4));
			
			if(isBank==false){//非银行类页面（银行类页面比较特殊）
				//判断此页关键值是否存在，不存在则不提取并返回null
				//因为腾讯是最后一道采集源，如果某字体空值其它字段有值则也要采，所以以下不用做处理。
//				if(StringUtils.isEmpty(data[18][i])||data[18][i].trim().equals("0")){//归属于母公司股东的净利润为null
//					return null;
//				}
				finance.setTOI(data[1][i]);// 营业总收入
				finance.setOperatingIncome(data[1][i]);// 营业收入
				finance.setTOC(data[2][i]);// 营业总成本
				finance.setOperatingCost(data[2][i]);// 营业成本
				finance.setBTAS(data[3][i]);// 营业税金及附加
				finance.setMarketingExpen(data[4][i]);// 销售费用
				finance.setAdminExpenses(data[5][i]);// 管理费用
				finance.setFinancialExpenses(data[6][i]);// 财务费用
				finance.setADL(data[7][i]);// 资产减值损失
				finance.setInvestIncome(data[9][i]);// '投资收益
				finance.setOperatingProfit(data[11][i]);// 营业利润
				finance.setNOI(data[12][i]);// 营业外收入
				finance.setNOE(data[13][i]);// 营业外支出
				finance.setLFDONCA(data[14][i]);// 非流动资产处置净损失
				finance.setTotalProfit(data[15][i]);// 利润总额
				finance.setITE(data[16][i]);// 所得税
				finance.setNetProfit(data[17][i]);// 净利润
				finance.setMII(data[19][i]);// 少数股东损益
				finance.setONPATP(data[18][i]);// 归属于母公司股东的净利润
				finance.setBEPS(data[21][i]);// 基本每股收益
				finance.setDEPS(data[22][i]);// 稀释每股收益
			}else{//银行类页面财报提取流程
				
				//因为腾讯是最后一道采集源，如果某字体空值其它字段有值则也要采，所以以下不用做处理。
//				if(StringUtils.isEmpty(data[31][i]) || data[31][i].trim().equals("0")){//归属于母公司股东的净利润为null
//					return null;
//				}
				finance.setTOI(data[1][i]);// 营业总收入
				finance.setOperatingIncome(data[1][i]);// 营业收入
				finance.setTOC(data[18][i]);// 营业总成本
				finance.setOperatingCost(data[18][i]);// 营业成本
				finance.setBTAS(data[19][i]);// 营业税金及附加.
//				finance.setMarketingExpen(data[4][i]);// 销售费用
				finance.setAdminExpenses(data[20][i]);// 管理费用
//				finance.setFinancialExpenses(data[6][i]);// 财务费用
				finance.setADL(data[21][i]);// 资产减值损失
				finance.setInvestIncome(data[13][i]);// '投资收益/投资净收益
				finance.setOperatingProfit(data[25][i]);// 营业利润
				finance.setNOI(data[26][i]);// 营业外收入
				finance.setNOE(data[27][i]);// 营业外支出
//				finance.setLFDONCA(data[14][i]);// 非流动资产处置净损失
				finance.setTotalProfit(data[28][i]);// 利润总额
				finance.setITE(data[29][i]);// 所得税
				finance.setNetProfit(data[31][i]);// 净利润
				finance.setMII(data[30][i]);// 少数股东损益
				finance.setONPATP(data[31][i]);// 归属于母公司股东的净利润(银行的与上面的净利润值相同)
				finance.setBEPS(data[45][i]);// 基本每股收益
				finance.setDEPS(data[46][i]);// 稀释每股收益.
			}
			finance.setLRFlag(2);//标记采集来源为腾讯财经
			return finance;
			
		}
		return null;
	}
	
}
