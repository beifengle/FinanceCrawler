package com.yysoft.extract;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.entity.Finance;
import com.yysoft.util.StringUtils;

/**
 * 功能描述：腾讯财经资产负债表页面数据提取
 * @author Huanyan.Lu
 * @date:2016年5月11日
 * @time:下午3:08:02
 * @version:1.0
 */
public class QQZCExtracter {
	private static Logger logger = LogManager.getLogger(QQZCExtracter.class); //

	
	/**
	 * Description:资产负债表页面数据提取
	 * @param finance 提取信息实例
	 * @param data 读取的页面元信息
	 * @param code 股票代码
	 * @param name 股票简称
	 * @param reportDate 要提取信息的报告期
	 * @return
	 */
	public static  Finance fetch(Finance finance,String data[][],String code,String name,String reportDate,boolean isBank){
		logger.info("提取的报告期："+ reportDate + "===腾讯财经资产负债表页面数据项部分赋值===");
		String dateString = "";
		for(int i=1;i<data[0].length;i++){
			if(data[0][i]==null || !data[0][i].contains(reportDate)){
				continue;
			}
			//有些页面的年度财报里季报重复，导致表格多了一列：http://stock.jrj.com.cn/share,002071,lrfpb_2014.shtml
			//在此做些过滤(部分页面日期重复多了一列相同，这时只过滤此情况)
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
//				if(StringUtils.isEmpty(data[33][i]) || data[33][i].trim().equals("0")){//资产总计为null
//					return null;
//				}
				finance.setMonetaryCapital(data[2][i]);// 货币资金
				finance.setHFTFA(data[3][i]);// 交易性金融资产
				finance.setNotesReceivable(data[4][i]);// 应收票据净额
				finance.setAccountsReceivable(data[5][i]);// 应收账款净额
				finance.setAdvanceReceipts(data[6][i]);// 预付账款
				finance.setInterestReceivable(data[7][i]);// 应收利息
				finance.setDividendReceivable(data[8][i]);// 应收股利净额
				finance.setOtherReceivables(data[9][i]);// 其他应收款
				finance.setINVENTORY(data[10][i]);// 存货
				// finance.setLTPE(data[27][i]);//待摊费用
				finance.setNCADWOY(data[11][i]);// 一年内到期的非流动资产
				finance.setOCA(data[12][i]);// 其他流动资产
				finance.setCurrentAssets(data[13][i]);// 流动资产合计
				finance.setAFSFA(data[15][i]);// 可供出售金融资产
				finance.setHTMI(data[16][i]);// 持有至到期投资
				finance.setInvestmentPro(data[19][i]);// 投资性房地产
				finance.setLTEI(data[18][i]);// 长期股权投资
				finance.setLTR(data[17][i]);// 长期应收款
				finance.setFixedAssets(data[20][i]);// 固定资产
				finance.setProjectMaterials(data[22][i]);// 工程物资
				finance.setUCP(data[21][i]);// 在建工程
				finance.setDOFA(data[23][i]);// 固定资产清理
				finance.setPBA(data[24][i]);// 生产性生物资产
				finance.setOAGA(data[25][i]);// 油气资产
				finance.setIntangibleAssets(data[26][i]);// 无形资产
				finance.setDevelopmentCosts(data[27][i]);// 开发支出
				finance.setGOODWILL(data[28][i]);// 商誉
				finance.setLTPE(data[29][i]);// 长期待摊费用
				finance.setDITA(data[30][i]);// 递延所得税资产
				finance.setONCA(data[31][i]);// 其他非流动资产
				finance.setNCA(data[32][i]);// 非流动资产合计
				finance.setASSETS(data[33][i]);// 资产总计
				finance.setSTB(data[35][i]);// 短期借款
				finance.setHFTFL(data[36][i]);// 交易性金融负债
				finance.setNotesPayable(data[37][i]);// 应付票据
				finance.setAccountsPayable(data[38][i]);// 应付账款
				
				finance.setSRP(data[40][i]);// 应付职工薪酬
				finance.setTaxesPayable(data[41][i]);// 应交税费
				finance.setInterestPayable(data[42][i]);// 应付利息
				finance.setDividendPayable(data[43][i]);// 应付股利
				finance.setOtherPayables(data[44][i]);// 其他应付款
				// finance.setEstimatedLiabilities(data[47][i]);//预计负债
				finance.setNCLDWOY(data[45][i]);// 一年内到期的非流动负债
				// finance.setBondsPayable(data[50][i]);//应付短期债券
				finance.setOCL(data[46][i]);// 其他流动负债
				finance.setCurrentLiabilities(data[47][i]);// 流动负债合计
				finance.setLTB(data[49][i]);// 长期借款
				finance.setBondsPayable(data[50][i]);// 应付债券
				finance.setLTP(data[51][i]);// 长期应付款
				finance.setSpecialPayables(data[52][i]);// 专项应付款
				finance.setDITL(data[53][i]);// 递延所得税负债
				finance.setONCL(data[54][i]);// 其他非流动负债
				finance.setNCL(data[55][i]);// 非流动负债合计:长期负债合计一样
				finance.setLTL(data[55][i]);// 长期负债合计:与非流动负债合计一样
				finance.setLIABILITIES(data[56][i]);// 负债合计
				finance.setPUC(data[58][i]);// 实收资本（或股本）
				finance.setCapitalReserves(data[59][i]);// 资本公积金
				finance.setSurplusReserves(data[61][i]);// 盈余公积金
				finance.setRetainedProfits(data[62][i]);// 未分配利润
				finance.setTreasuryStock(data[60][i]);// 库存股
				// finance.setFCTD(data[69][i]);//外币报表折算差额
				finance.setMSI(data[64][i]);// 少数股东权益
				finance.setTEATTSOPC(data[63][i]);// 归属于母公司股东权益合计
				finance.setOwnersEquity(data[65][i]);// 所有者权益合计
				finance.setLAOE(data[66][i]);// 负债及所有者权益总计
			}else{//银行类页面财报提取流程
				//因为腾讯是最后一道采集源，如果某字体空值其它字段有值则也要采，所以以下不用做处理。
//				if(StringUtils.isEmpty(data[46][i]) || data[46][i].trim().equals("0")){//资产总计为null
//					return null;
//				}
//				finance.setMonetaryCapital(data[1][i]);// 货币资金
				finance.setHFTFA(data[14][i]);// 交易性金融资产
//				finance.setNotesReceivable(data[3][i]);// 应收票据净额
				finance.setAccountsReceivable(data[18][i]);// 应收账款净额
				finance.setAdvanceReceipts(data[19][i]);// 预付账款
				finance.setInterestReceivable(data[20][i]);// 应收利息
//				finance.setDividendReceivable(data[7][i]);// 应收股利净额
				finance.setOtherReceivables(data[26][i]);// 其他应收款
//				finance.setINVENTORY(data[9][i]);// 存货
				finance.setLTPE(data[32][i]);//待摊费用
//				finance.setNCADWOY(data[10][i]);// 一年内到期的非流动资产
//				finance.setOCA(data[11][i]);// 其他流动资产
//				finance.setCurrentAssets(data[12][i]);// 流动资产合计
				finance.setAFSFA(data[24][i]);// 可供出售金融资产
				finance.setHTMI(data[25][i]);// 持有至到期投资
				finance.setInvestmentPro(data[30][i]);// 投资性房地产
				finance.setLTEI(data[28][i]);// 长期股权投资
				finance.setLTR(data[27][i]);// 长期应收款
				finance.setFixedAssets(data[34][i]);// 固定资产
//				finance.setProjectMaterials(data[20][i]);// 工程物资
				finance.setUCP(data[35][i]);// 在建工程
				finance.setDOFA(data[36][i]);// 固定资产清理
//				finance.setPBA(data[22][i]);// 生产性生物资产
//				finance.setOAGA(data[23][i]);// 油气资产
				finance.setIntangibleAssets(data[37][i]);// 无形资产
//				finance.setDevelopmentCosts(data[25][i]);// 开发支出
				finance.setGOODWILL(data[38][i]);// 商誉
				finance.setLTPE(data[39][i]);// 长期待摊费用
//				finance.setDITA(data[28][i]);// 递延所得税资产
//				finance.setONCA(data[29][i]);// 其他非流动资产
//				finance.setNCA(data[30][i]);// 非流动资产合计
				finance.setASSETS(data[46][i]);// 资产总计
//				finance.setSTB(data[32][i]);// 短期借款
				finance.setHFTFL(data[56][i]);// 交易性金融负债
//				finance.setNotesPayable(data[34][i]);// 应付票据
				finance.setAccountsPayable(data[68][i]);// 应付账款

//				finance.setSRP(data[37][i]);// 应付职工薪酬
				finance.setTaxesPayable(data[66][i]);// 应交税费
				finance.setInterestPayable(data[67][i]);// 应付利息
				finance.setDividendPayable(data[70][i]);// 应付股利
//				finance.setOtherPayables(data[41][i]);// 其他应付款
				 finance.setEstimatedLiabilities(data[73][i]);//预计负债
//				finance.setNCLDWOY(data[42][i]);// 一年内到期的非流动负债
				// finance.setBondsPayable(data[50][i]);//应付短期债券
//				finance.setOCL(data[43][i]);// 其他流动负债
//				finance.setCurrentLiabilities(data[44][i]);// 流动负债合计
//				finance.setLTB(data[45][i]);// 长期借款
//				finance.setBondsPayable(data[46][i]);// 应付债券
				finance.setLTP(data[75][i]);// 长期应付款
				finance.setSpecialPayables(data[69][i]);// 专项应付款
				finance.setDITL(data[78][i]);// 递延所得税负债
//				finance.setONCL(data[50][i]);// 其他非流动负债
//				finance.setNCL(data[51][i]);// 非流动负债合计:长期负债合计一样
//				finance.setLTL(data[51][i]);// 长期负债合计:与非流动负债合计一样
				finance.setLIABILITIES(data[80][i]);// 负债合计
				finance.setPUC(data[81][i]);// 实收资本（或股本）
				finance.setCapitalReserves(data[82][i]);// 资本公积金
				finance.setSurplusReserves(data[86][i]);// 盈余公积金
				finance.setRetainedProfits(data[89][i]);// 未分配利润
//				finance.setTreasuryStock(data[55][i]);// 库存股
				finance.setFCTD(data[91][i]);//外币报表折算差额
				finance.setMSI(data[95][i]);// 少数股东权益
				finance.setTEATTSOPC(data[94][i]);// 归属于母公司股东权益合计
				finance.setOwnersEquity(data[96][i]);// 所有者权益合计/股东权益合计
				finance.setLAOE(data[97][i]);// 负债及所有者权益总计/负债及股东权益合计
			}
			finance.setZCFlag(2);//标记采集来源为腾讯财经
			return finance;
			
		}
		return null;
	}
	
}
