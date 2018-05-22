package com.yysoft.extract;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.entity.Finance;
import com.yysoft.util.StringUtils;

/**
 * 功能描述：金融街资产负债表页面数据提取
 * @author Huanyan.Lu
 * @date:2016年5月11日
 * @time:下午3:08:02
 * @version:1.0
 */
public class JRJZCExtracter {
	private static Logger logger = LogManager.getLogger(JRJZCExtracter.class); //

	
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
		finance.setCode(code);
		finance.setName(name);
		finance.setReportDate(reportDate);//报告期
		finance.setQuarter(StringUtils.getQuarterFromReport(reportDate));
		finance.setYear(finance.getReportDate().substring(0,4));
		logger.info("提取的报告期"+ reportDate + "===金融街资产负债表页面数据项部分赋值===");
		String dateString = "";
		for(int i=0;i<data[0].length;i++){
			if(data[0][i]==null || !data[0][i].contains(reportDate)){
				continue;
			}
			//有些页面的年度财报里季报重复，导致表格多了一列：http://stock.jrj.com.cn/share,002071,lrfpb_2014.shtml
			//在此做些过滤(部分页面日期重复多了一列相同，这时只过滤此情况)
			if(dateString.contains(data[0][i])){
				continue;
			}
			dateString +=data[0][i] +",";
			
			if(data.length==50){//银行
//				finance.setMonetaryCapital(data[3][i]);//货币资金
				finance.setHFTFA(data[6][i]);//交易性金融资产
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
				finance.setAFSFA(data[11][i]);//可供出售金融资产
				finance.setHTMI(data[12][i]);//持有至到期投资
				finance.setInvestmentPro(data[18][i]);//投资性房地产
				finance.setLTEI(data[13][i]);//长期股权投资
//				finance.setLTR(data[24][i]);//长期应收款
				finance.setFixedAssets(data[15][i]);//固定资产
//				finance.setProjectMaterials(data[29][i]);//工程物资
//				finance.setUCP(data[28][i]);//在建工程
//				finance.setDOFA(data[30][i]);//固定资产清理
//				finance.setPBA(data[31][i]);//生产性生物资产
//				finance.setOAGA(data[33][i]);//油气资产
				finance.setIntangibleAssets(data[16][i]);//无形资产
//				finance.setDevelopmentCosts(data[35][i]);//开发支出
//				finance.setGOODWILL(data[19][i]);//商誉
//				finance.setLTPE(data[37][i]);//长期待摊费用
				finance.setDITA(data[17][i]);//递延所得税资产
//				finance.setONCA(data[39][i]);//其他非流动资产
//				finance.setNCA(data[40][i]);//非流动资产合计
				finance.setASSETS(data[20][i]);//资产总计
//				finance.setSTB(data[43][i]);//短期借款
				finance.setHFTFL(data[25][i]);//交易性金融负债
//				finance.setNotesPayable(data[45][i]);//应付票据
//				finance.setAccountsPayable(data[46][i]);//应付账款
				
				
				finance.setSRP(data[29][i]);//应付职工薪酬
				finance.setTaxesPayable(data[30][i]);//应交税费
				finance.setInterestPayable(data[31][i]);//应付利息
//				finance.setDividendPayable(data[52][i]);//应付股利
//				finance.setOtherPayables(data[53][i]);//其他应付款
				finance.setEstimatedLiabilities(data[35][i]);//预计负债
//				finance.setNCLDWOY(data[57][i]);//一年内到期的非流动负债
//				finance.setBondsPayable(data[56][i]);//应付短期债券
//				finance.setOCL(data[58][i]);//其他流动负债
//				finance.setCurrentLiabilities(data[59][i]);//流动负债合计
//				finance.setLTB(data[61][i]);//长期借款
				finance.setBondsPayable(data[33][i]);//应付债券
//				finance.setLTP(data[63][i]);//长期应付款
//				finance.setSpecialPayables(data[65][i]);//专项应付款
				finance.setDITL(data[34][i]);//递延所得税负债
//				finance.setONCL(data[69][i]);//其他非流动负债
//				finance.setNCL(data[70][i]);//非流动负债合计与长期负债合计一样
//				finance.setLTL(data[70][i]);//非流动负债合计与长期负债合计一样
				finance.setLIABILITIES(data[37][i]);//负债合计
//				finance.setPUC(data[73][i]);//实收资本（或股本）
				finance.setCapitalReserves(data[40][i]);//资本公积金
				finance.setSurplusReserves(data[41][i]);//盈余公积金
				finance.setRetainedProfits(data[42][i]);//未分配利润
//				finance.setTreasuryStock(data[75][i]);//库存股
//				finance.setFCTD(data[74][i]);//外币报表折算差额
				finance.setMSI(data[46][i]);//少数股东权益
				finance.setTEATTSOPC(data[47][i]);//归属于母公司股东权益合计
				finance.setOwnersEquity(data[48][i]);//所有者权益合计/股东权益合计
				finance.setLAOE(data[49][i]);//负债及所有者权益总计/负债及股东权益合计
				if(StringUtils.isEmpty(finance.getASSETS())){
					finance.setASSETS(finance.getLAOE());
				}
			}else if(data.length==81){//保险、证券、一般行业
				finance.setMonetaryCapital(data[2][i]);//货币资金
				finance.setHFTFA(data[3][i]);//交易性金融资产
				finance.setNotesReceivable(data[4][i]);//应收票据净额
				finance.setAccountsReceivable(data[5][i]);//应收账款净额
				finance.setAdvanceReceipts(data[6][i]);//预付账款
				finance.setDividendReceivable(data[7][i]);//应收股利净额
				finance.setInterestReceivable(data[8][i]);//应收利息
				finance.setOtherReceivables(data[9][i]);//其他应收款
				finance.setINVENTORY(data[10][i]);//存货
				finance.setLTPE(data[12][i]);//待摊费用
				finance.setNCADWOY(data[13][i]);//一年内到期的非流动资产
				finance.setOCA(data[14][i]);//其他流动资产
				finance.setCurrentAssets(data[16][i]);//流动资产合计
				finance.setAFSFA(data[18][i]);//可供出售金融资产
				finance.setHTMI(data[19][i]);//持有至到期投资
				finance.setInvestmentPro(data[20][i]);//投资性房地产
				finance.setLTEI(data[21][i]);//长期股权投资
				finance.setLTR(data[22][i]);//长期应收款
				finance.setFixedAssets(data[23][i]);//固定资产
				finance.setProjectMaterials(data[24][i]);//工程物资
				finance.setUCP(data[25][i]);//在建工程
				finance.setDOFA(data[26][i]);//固定资产清理
				finance.setPBA(data[27][i]);//生产性生物资产
				finance.setOAGA(data[28][i]);//油气资产
				finance.setIntangibleAssets(data[29][i]);//无形资产
				finance.setDevelopmentCosts(data[30][i]);//开发支出
				finance.setGOODWILL(data[31][i]);//商誉
				finance.setLTPE(data[32][i]);//长期待摊费用
				finance.setDITA(data[33][i]);//递延所得税资产
				finance.setONCA(data[34][i]);//其他非流动资产
				finance.setNCA(data[36][i]);//非流动资产合计
				finance.setASSETS(data[37][i]);//资产总计
				finance.setSTB(data[39][i]);//短期借款
				finance.setHFTFL(data[40][i]);//交易性金融负债
				finance.setNotesPayable(data[41][i]);//应付票据
				finance.setAccountsPayable(data[42][i]);//应付账款
				
				
				finance.setSRP(data[44][i]);//应付职工薪酬
				finance.setTaxesPayable(data[45][i]);//应交税费
				finance.setInterestPayable(data[46][i]);//应付利息
				finance.setDividendPayable(data[47][i]);//应付股利
				finance.setOtherPayables(data[48][i]);//其他应付款
//				finance.setEstimatedLiabilities(data[35][i]);//预计负债
				finance.setNCLDWOY(data[52][i]);//一年内到期的非流动负债
				finance.setBondsPayable(data[53][i]);//应付短期债券
				finance.setOCL(data[54][i]);//其他流动负债
				finance.setCurrentLiabilities(data[56][i]);//流动负债合计
				finance.setLTB(data[58][i]);//长期借款
				finance.setBondsPayable(data[59][i]);//应付债券
				finance.setLTP(data[60][i]);//长期应付款
				finance.setSpecialPayables(data[61][i]);//专项应付款
				finance.setDITL(data[62][i]);//递延所得税负债
				finance.setONCL(data[64][i]);//其他非流动负债
				finance.setNCL(data[66][i]);//非流动负债合计与长期负债合计一样
				finance.setLTL(data[66][i]);//非流动负债合计与长期负债合计一样
				finance.setLIABILITIES(data[67][i]);//负债合计
				finance.setPUC(data[69][i]);//实收资本（或股本）
				finance.setCapitalReserves(data[70][i]);//资本公积金
				finance.setSurplusReserves(data[71][i]);//盈余公积金
				finance.setRetainedProfits(data[72][i]);//未分配利润
				finance.setTreasuryStock(data[73][i]);//库存股
				finance.setFCTD(data[74][i]);//外币报表折算差额
				finance.setMSI(data[76][i]);//少数股东权益
				finance.setTEATTSOPC(data[77][i]);//归属于母公司股东权益合计
				finance.setOwnersEquity(data[79][i]);//所有者权益合计/股东权益合计
				finance.setLAOE(data[80][i]);//负债及所有者权益总计/负债及股东权益合计
				if(StringUtils.isEmpty(finance.getASSETS())){
					finance.setASSETS(finance.getLAOE());
				}
			}else{
				logger.info("金融街-资产负债表-数组长度匹配不到。data.length=" + data.length);
			}
			finance.setZCFlag(1);//标记采集来源
			return finance;
			
		}
		return null;
	}
	
}
