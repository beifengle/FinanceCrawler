package com.yysoft.extract;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.entity.Finance;
import com.yysoft.util.StringUtils;

/**
 * 功能描述：sina财经资产负债表页面数据提取:http://money.finance.sina.com.cn/corp/go.php/vFD_BalanceSheet/stockid/002931/ctrl/2017/displaytype/4.phtml
 * @author Huanyan.Lu
 * @date:2018年3月26日
 * @time:下午3:08:02
 * @version:1.0
 */
public class SinaZCExtracter {
	private static Logger logger = LogManager.getLogger(SinaZCExtracter.class); //

	
	/**
	 * Description:资产负债表页面数据提取
	 * @param finance 提取信息实例
	 * @param data 读取的页面元信息
	 * @param code 股票代码
	 * @param name 股票简称
	 * @param reportDate 要提取信息的报告期
	 * @return
	 */
	public static  Finance fetch(Finance finance,String data[][],String code,String name,String reportDate){
		logger.info("提取的报告期"+ reportDate + "===sina资产负债表页面数据项部分赋值===");
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
			
			if(data.length==60){//证券
				//判断此页关键值是否存在，不存在则不提取并返回null
				if(StringUtils.isEmpty(data[24][i]) ||data[24][i].trim().equals("0")){//资产总计为null
					return null;
				}
//				finance.setMonetaryCapital(data[3][i]);//货币资金
				finance.setHFTFA(data[8][i]);//交易性金融资产
//				finance.setNotesReceivable(data[6][i]);//应收票据净额
//				finance.setAccountsReceivable(data[7][i]);//应收账款净额
//				finance.setAdvanceReceipts(data[8][i]);//预付账款
//				finance.setDividendReceivable(data[10][i]);//应收股利净额
				finance.setInterestReceivable(data[12][i]);//应收利息
//				finance.setOtherReceivables(data[11][i]);//其他应收款
//				finance.setINVENTORY(data[13][i]);//存货
//				finance.setLTPE(data[16][i]);//待摊费用
//				finance.setNCADWOY(data[15][i]);//一年内到期的非流动资产
//				finance.setOCA(data[18][i]);//其他流动资产
//				finance.setCurrentAssets(data[19][i]);//流动资产合计
				finance.setAFSFA(data[14][i]);//可供出售金融资产
				finance.setHTMI(data[15][i]);//持有至到期投资
				finance.setInvestmentPro(data[22][i]);//投资性房地产
				finance.setLTEI(data[16][i]);//长期股权投资
//				finance.setLTR(data[24][i]);//长期应收款
				finance.setFixedAssets(data[17][i]);//固定资产
//				finance.setProjectMaterials(data[29][i]);//工程物资
//				finance.setUCP(data[28][i]);//在建工程
//				finance.setDOFA(data[30][i]);//固定资产清理
//				finance.setPBA(data[31][i]);//生产性生物资产
//				finance.setOAGA(data[33][i]);//油气资产
				finance.setIntangibleAssets(data[18][i]);//无形资产
//				finance.setDevelopmentCosts(data[35][i]);//开发支出
				finance.setGOODWILL(data[20][i]);//商誉
//				finance.setLTPE(data[37][i]);//长期待摊费用
				finance.setDITA(data[21][i]);//递延所得税资产
//				finance.setONCA(data[39][i]);//其他非流动资产
//				finance.setNCA(data[40][i]);//非流动资产合计
				finance.setASSETS(data[24][i]);//资产总计
//				finance.setSTB(data[43][i]);//短期借款
				finance.setHFTFL(data[30][i]);//交易性金融负债
//				finance.setNotesPayable(data[45][i]);//应付票据
//				finance.setAccountsPayable(data[46][i]);//应付账款
				
				
				finance.setSRP(data[35][i]);//应付职工薪酬
				finance.setTaxesPayable(data[36][i]);//应交税费
				finance.setInterestPayable(data[38][i]);//应付利息
//				finance.setDividendPayable(data[52][i]);//应付股利
//				finance.setOtherPayables(data[53][i]);//其他应付款
				finance.setEstimatedLiabilities(data[42][i]);//预计负债
//				finance.setNCLDWOY(data[57][i]);//一年内到期的非流动负债
//				finance.setBondsPayable(data[56][i]);//应付短期债券
//				finance.setOCL(data[58][i]);//其他流动负债
//				finance.setCurrentLiabilities(data[59][i]);//流动负债合计
//				finance.setLTB(data[61][i]);//长期借款
				finance.setBondsPayable(data[40][i]);//应付债券
//				finance.setLTP(data[63][i]);//长期应付款
//				finance.setSpecialPayables(data[65][i]);//专项应付款
				finance.setDITL(data[41][i]);//递延所得税负债
//				finance.setONCL(data[69][i]);//其他非流动负债
//				finance.setNCL(data[70][i]);//非流动负债合计与长期负债合计一样
//				finance.setLTL(data[70][i]);//非流动负债合计与长期负债合计一样
				finance.setLIABILITIES(data[44][i]);//负债合计
//				finance.setPUC(data[73][i]);//实收资本（或股本）
				finance.setCapitalReserves(data[48][i]);//资本公积金
				finance.setSurplusReserves(data[51][i]);//盈余公积金
				finance.setRetainedProfits(data[52][i]);//未分配利润
//				finance.setTreasuryStock(data[75][i]);//库存股
//				finance.setFCTD(data[74][i]);//外币报表折算差额
				finance.setMSI(data[57][i]);//少数股东权益
				finance.setTEATTSOPC(data[56][i]);//归属于母公司股东权益合计
				finance.setOwnersEquity(data[58][i]);//所有者权益合计/股东权益合计
				finance.setLAOE(data[59][i]);//负债及所有者权益总计/负债及股东权益合计
				if(StringUtils.isEmpty(finance.getASSETS())){
					finance.setASSETS(finance.getLAOE());
				}
			}else if(data.length==59){//银行
				//判断此页关键值是否存在，不存在则不提取并返回null
				if(StringUtils.isEmpty(data[23][i]) ||data[23][i].trim().equals("0")){//资产总计为null
					return null;
				}
//				finance.setMonetaryCapital(data[3][i]);//货币资金
				finance.setHFTFA(data[7][i]);//交易性金融资产
//				finance.setNotesReceivable(data[6][i]);//应收票据净额
//				finance.setAccountsReceivable(data[7][i]);//应收账款净额
//				finance.setAdvanceReceipts(data[8][i]);//预付账款
//				finance.setDividendReceivable(data[10][i]);//应收股利净额
				finance.setInterestReceivable(data[10][i]);//应收利息
//				finance.setOtherReceivables(data[11][i]);//其他应收款
//				finance.setINVENTORY(data[13][i]);//存货
//				finance.setLTPE(data[16][i]);//待摊费用
//				finance.setNCADWOY(data[15][i]);//一年内到期的非流动资产
//				finance.setOCA(data[18][i]);//其他流动资产
//				finance.setCurrentAssets(data[19][i]);//流动资产合计
				finance.setAFSFA(data[13][i]);//可供出售金融资产
				finance.setHTMI(data[14][i]);//持有至到期投资
				finance.setInvestmentPro(data[21][i]);//投资性房地产
				finance.setLTEI(data[15][i]);//长期股权投资
//				finance.setLTR(data[24][i]);//长期应收款
				finance.setFixedAssets(data[17][i]);//固定资产
//				finance.setProjectMaterials(data[29][i]);//工程物资
//				finance.setUCP(data[28][i]);//在建工程
//				finance.setDOFA(data[30][i]);//固定资产清理
//				finance.setPBA(data[31][i]);//生产性生物资产
//				finance.setOAGA(data[33][i]);//油气资产
				finance.setIntangibleAssets(data[18][i]);//无形资产
//				finance.setDevelopmentCosts(data[35][i]);//开发支出
				finance.setGOODWILL(data[19][i]);//商誉
//				finance.setLTPE(data[37][i]);//长期待摊费用
				finance.setDITA(data[20][i]);//递延所得税资产
//				finance.setONCA(data[39][i]);//其他非流动资产
//				finance.setNCA(data[40][i]);//非流动资产合计
				finance.setASSETS(data[23][i]);//资产总计
//				finance.setSTB(data[43][i]);//短期借款
				finance.setHFTFL(data[30][i]);//交易性金融负债
//				finance.setNotesPayable(data[45][i]);//应付票据
//				finance.setAccountsPayable(data[46][i]);//应付账款
				
				
				finance.setSRP(data[33][i]);//应付职工薪酬
				finance.setTaxesPayable(data[34][i]);//应交税费
				finance.setInterestPayable(data[35][i]);//应付利息
//				finance.setDividendPayable(data[52][i]);//应付股利
//				finance.setOtherPayables(data[53][i]);//其他应付款
				finance.setEstimatedLiabilities(data[40][i]);//预计负债
//				finance.setNCLDWOY(data[57][i]);//一年内到期的非流动负债
//				finance.setBondsPayable(data[56][i]);//应付短期债券
//				finance.setOCL(data[58][i]);//其他流动负债
//				finance.setCurrentLiabilities(data[59][i]);//流动负债合计
//				finance.setLTB(data[61][i]);//长期借款
				finance.setBondsPayable(data[38][i]);//应付债券
//				finance.setLTP(data[63][i]);//长期应付款
//				finance.setSpecialPayables(data[65][i]);//专项应付款
				finance.setDITL(data[39][i]);//递延所得税负债
//				finance.setONCL(data[69][i]);//其他非流动负债
//				finance.setNCL(data[70][i]);//非流动负债合计与长期负债合计一样
//				finance.setLTL(data[70][i]);//非流动负债合计与长期负债合计一样
				finance.setLIABILITIES(data[42][i]);//负债合计
//				finance.setPUC(data[73][i]);//实收资本（或股本）
				finance.setCapitalReserves(data[47][i]);//资本公积金
				finance.setSurplusReserves(data[50][i]);//盈余公积金
				finance.setRetainedProfits(data[51][i]);//未分配利润
//				finance.setTreasuryStock(data[75][i]);//库存股
//				finance.setFCTD(data[74][i]);//外币报表折算差额
				finance.setMSI(data[56][i]);//少数股东权益
				finance.setTEATTSOPC(data[55][i]);//归属于母公司股东权益合计
				finance.setOwnersEquity(data[43][i]);//所有者权益合计/股东权益合计
				finance.setLAOE(data[58][i]);//负债及所有者权益总计/负债及股东权益合计
				if(StringUtils.isEmpty(finance.getASSETS())){
					finance.setASSETS(finance.getLAOE());
				}
			}else if(data.length==69){//保险
				//判断此页关键值是否存在，不存在则不提取并返回null
				if(StringUtils.isEmpty(data[29][i]) ||data[29][i].trim().equals("0")){//资产总计为null
					return null;
				}
				finance.setMonetaryCapital(data[3][i]);//货币资金
				finance.setHFTFA(data[5][i]);//交易性金融资产
//				finance.setNotesReceivable(data[6][i]);//应收票据净额
//				finance.setAccountsReceivable(data[7][i]);//应收账款净额
//				finance.setAdvanceReceipts(data[8][i]);//预付账款
//				finance.setDividendReceivable(data[10][i]);//应收股利净额
				finance.setInterestReceivable(data[9][i]);//应收利息
//				finance.setOtherReceivables(data[11][i]);//其他应收款
//				finance.setINVENTORY(data[13][i]);//存货
//				finance.setLTPE(data[16][i]);//待摊费用
//				finance.setNCADWOY(data[15][i]);//一年内到期的非流动资产
//				finance.setOCA(data[18][i]);//其他流动资产
//				finance.setCurrentAssets(data[19][i]);//流动资产合计
				finance.setAFSFA(data[16][i]);//可供出售金融资产
				finance.setHTMI(data[17][i]);//持有至到期投资
				finance.setInvestmentPro(data[26][i]);//投资性房地产
				finance.setLTEI(data[18][i]);//长期股权投资
//				finance.setLTR(data[24][i]);//长期应收款
				finance.setFixedAssets(data[21][i]);//固定资产
//				finance.setProjectMaterials(data[29][i]);//工程物资
//				finance.setUCP(data[28][i]);//在建工程
//				finance.setDOFA(data[30][i]);//固定资产清理
//				finance.setPBA(data[31][i]);//生产性生物资产
//				finance.setOAGA(data[33][i]);//油气资产
				finance.setIntangibleAssets(data[22][i]);//无形资产
//				finance.setDevelopmentCosts(data[35][i]);//开发支出
				finance.setGOODWILL(data[23][i]);//商誉
//				finance.setLTPE(data[37][i]);//长期待摊费用
				finance.setDITA(data[25][i]);//递延所得税资产
//				finance.setONCA(data[39][i]);//其他非流动资产
//				finance.setNCA(data[40][i]);//非流动资产合计
				finance.setASSETS(data[29][i]);//资产总计
//				finance.setSTB(data[43][i]);//短期借款
				finance.setHFTFL(data[33][i]);//交易性金融负债
//				finance.setNotesPayable(data[45][i]);//应付票据
//				finance.setAccountsPayable(data[46][i]);//应付账款
				
				
				finance.setSRP(data[40][i]);//应付职工薪酬
				finance.setTaxesPayable(data[41][i]);//应交税费
				finance.setInterestPayable(data[42][i]);//应付利息
//				finance.setDividendPayable(data[52][i]);//应付股利
//				finance.setOtherPayables(data[53][i]);//其他应付款
				finance.setEstimatedLiabilities(data[54][i]);//预计负债
//				finance.setNCLDWOY(data[57][i]);//一年内到期的非流动负债
//				finance.setBondsPayable(data[56][i]);//应付短期债券
//				finance.setOCL(data[58][i]);//其他流动负债
//				finance.setCurrentLiabilities(data[59][i]);//流动负债合计
//				finance.setLTB(data[61][i]);//长期借款
				finance.setBondsPayable(data[51][i]);//应付债券
//				finance.setLTP(data[63][i]);//长期应付款
//				finance.setSpecialPayables(data[65][i]);//专项应付款
				finance.setDITL(data[53][i]);//递延所得税负债
//				finance.setONCL(data[69][i]);//其他非流动负债
//				finance.setNCL(data[70][i]);//非流动负债合计与长期负债合计一样
//				finance.setLTL(data[70][i]);//非流动负债合计与长期负债合计一样
				finance.setLIABILITIES(data[56][i]);//负债合计
//				finance.setPUC(data[73][i]);//实收资本（或股本）
				finance.setCapitalReserves(data[59][i]);//资本公积金
				finance.setSurplusReserves(data[61][i]);//盈余公积金
				finance.setRetainedProfits(data[62][i]);//未分配利润
//				finance.setTreasuryStock(data[75][i]);//库存股
//				finance.setFCTD(data[74][i]);//外币报表折算差额
				finance.setMSI(data[66][i]);//少数股东权益
				finance.setTEATTSOPC(data[65][i]);//归属于母公司股东权益合计
				finance.setOwnersEquity(data[67][i]);//所有者权益合计/股东权益合计
				finance.setLAOE(data[68][i]);//负债及所有者权益总计/负债及股东权益合计
				if(StringUtils.isEmpty(finance.getASSETS())){
					finance.setASSETS(finance.getLAOE());
				}
			}else{
				//判断此页关键值是否存在，不存在则不提取并返回null
				if(StringUtils.isEmpty(data[41][i]) ||data[41][i].trim().equals("0")){//资产总计为null
					return null;
				}
				finance.setMonetaryCapital(data[3][i]);//货币资金
				finance.setHFTFA(data[4][i]);//交易性金融资产
				finance.setNotesReceivable(data[6][i]);//应收票据净额
				finance.setAccountsReceivable(data[7][i]);//应收账款净额
				finance.setAdvanceReceipts(data[8][i]);//预付账款
				finance.setDividendReceivable(data[10][i]);//应收股利净额
				finance.setInterestReceivable(data[9][i]);//应收利息
				finance.setOtherReceivables(data[11][i]);//其他应收款
				finance.setINVENTORY(data[13][i]);//存货
				finance.setLTPE(data[16][i]);//待摊费用
				finance.setNCADWOY(data[15][i]);//一年内到期的非流动资产
				finance.setOCA(data[18][i]);//其他流动资产
				finance.setCurrentAssets(data[19][i]);//流动资产合计
				finance.setAFSFA(data[22][i]);//可供出售金融资产
				finance.setHTMI(data[23][i]);//持有至到期投资
				finance.setInvestmentPro(data[26][i]);//投资性房地产
				finance.setLTEI(data[25][i]);//长期股权投资
				finance.setLTR(data[24][i]);//长期应收款
				finance.setFixedAssets(data[27][i]);//固定资产
				finance.setProjectMaterials(data[29][i]);//工程物资
				finance.setUCP(data[28][i]);//在建工程
				finance.setDOFA(data[30][i]);//固定资产清理
				finance.setPBA(data[31][i]);//生产性生物资产
				finance.setOAGA(data[33][i]);//油气资产
				finance.setIntangibleAssets(data[34][i]);//无形资产
				finance.setDevelopmentCosts(data[35][i]);//开发支出
				finance.setGOODWILL(data[36][i]);//商誉
				finance.setLTPE(data[37][i]);//长期待摊费用
				finance.setDITA(data[38][i]);//递延所得税资产
				finance.setONCA(data[39][i]);//其他非流动资产
				finance.setNCA(data[40][i]);//非流动资产合计
				finance.setASSETS(data[41][i]);//资产总计
				finance.setSTB(data[43][i]);//短期借款
				finance.setHFTFL(data[44][i]);//交易性金融负债
				finance.setNotesPayable(data[45][i]);//应付票据
				finance.setAccountsPayable(data[46][i]);//应付账款
				
				
				finance.setSRP(data[49][i]);//应付职工薪酬
				finance.setTaxesPayable(data[50][i]);//应交税费
				finance.setInterestPayable(data[51][i]);//应付利息
				finance.setDividendPayable(data[52][i]);//应付股利
				finance.setOtherPayables(data[53][i]);//其他应付款
//				finance.setEstimatedLiabilities(data[50][i]);//预计负债
				finance.setNCLDWOY(data[57][i]);//一年内到期的非流动负债
				finance.setBondsPayable(data[56][i]);//应付短期债券
				finance.setOCL(data[58][i]);//其他流动负债
				finance.setCurrentLiabilities(data[59][i]);//流动负债合计
				finance.setLTB(data[61][i]);//长期借款
				finance.setBondsPayable(data[62][i]);//应付债券
				finance.setLTP(data[63][i]);//长期应付款
				finance.setSpecialPayables(data[65][i]);//专项应付款
				finance.setDITL(data[67][i]);//递延所得税负债
				finance.setONCL(data[69][i]);//其他非流动负债
				finance.setNCL(data[70][i]);//非流动负债合计与长期负债合计一样
				finance.setLTL(data[70][i]);//非流动负债合计与长期负债合计一样
				finance.setLIABILITIES(data[71][i]);//负债合计
				finance.setPUC(data[73][i]);//实收资本（或股本）
				finance.setCapitalReserves(data[74][i]);//资本公积金
				finance.setSurplusReserves(data[78][i]);//盈余公积金
				finance.setRetainedProfits(data[80][i]);//未分配利润
				finance.setTreasuryStock(data[75][i]);//库存股
//				finance.setFCTD(data[74][i]);//外币报表折算差额
				finance.setMSI(data[82][i]);//少数股东权益
				finance.setTEATTSOPC(data[81][i]);//归属于母公司股东权益合计
				finance.setOwnersEquity(data[83][i]);//所有者权益合计
				finance.setLAOE(data[84][i]);//负债及所有者权益总计
				if(StringUtils.isEmpty(finance.getASSETS())){
					finance.setASSETS(finance.getLAOE());
				}
			}
			
			finance.setZCFlag(4);//标记采集来源
			return finance;
			
		}
		return null;
	}
	
}
