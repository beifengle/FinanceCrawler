package com.yysoft.extract;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yysoft.entity.Finance;
import com.yysoft.util.StringUtils;



/**
 * 功能描述：资产负债表页面数据提取
 * 
 * @author Huanyan.Lu
 * @date:2018年8月19日
 * @time:下午3:08:02
 * @version:1.0
 */
public class EastMoneyZCExtracter {
	private static Logger logger = LogManager.getLogger(EastMoneyZCExtracter.class); //

	/**
	 * Description:资产负债表页面数据提取
	 * 
	 * @param finance
	 *            提取信息实例
	 * @param data
	 *            读取的页面元信息
	 * @param code
	 *            股票代码
	 * @param name
	 *            股票简称
	 * @param reportDate
	 *            要提取信息的报告期
	 * @return
	 */
	public static Finance fetch(Finance finance, String data, String code, String name, String reportDate) {
		logger.info("提取的报告期" + reportDate + "===东方财富-资产负债表页面数据项部分赋值===");
		
		String f_date = "";
		JSONObject job = null;
		// 报告日期格式化转换为：2017/9/30
		String date[] = reportDate.split("-");
		date[1] = String.valueOf(Integer.parseInt(date[1]));
		date[2] = String.valueOf(Integer.parseInt(date[2]));
		f_date = date[0] + "/" + date[1] + "/" + date[2];
		JSONArray j_list;
		boolean isHaving = false;
		try {
			
			if (data.startsWith("[")) {
				j_list = new JSONArray(data);
				if (j_list.length() > 0) {
					for (int i = 0; i < j_list.length(); i++) {
						if (j_list.getJSONObject(i).getString("REPORTDATE").contains(f_date)) {
							job = j_list.getJSONObject(i);
							isHaving = true;
							break;
						}
						continue;
					}
					
				}
			} else {
				return null;
			}
			finance.setCode(code);
			finance.setName(name);
			finance.setReportDate(reportDate);// 报告期
			finance.setQuarter(StringUtils.getQuarterFromReport(reportDate));
			finance.setYear(finance.getReportDate().substring(0, 4));
			
			if(isHaving!=true){
				return null;
			}
			finance.setMonetaryCapital(job.getString("MONETARYFUND"));// 货币资金
			finance.setHFTFA(job.getString("FVALUEFASSET"));// 交易性金融资产
			finance.setNotesReceivable(job.getString("BILLREC"));// 应收票据净额
			finance.setAccountsReceivable(job.getString("ACCOUNTREC"));// 应收账款净额
			finance.setAdvanceReceipts(job.getString("ADVANCEPAY"));// 预付账款
			// finance.setDividendReceivable(data[10][i]);//应收股利净额
			finance.setInterestReceivable(job.getString("DIVIDENDREC"));// 应收利息
			finance.setOtherReceivables(job.getString("OTHERREC"));// 其他应收款
			finance.setINVENTORY(job.getString("INVENTORY"));// 存货
			finance.setLTPE(job.getString("LTDEFERASSET"));// 待摊费用
			// finance.setNCADWOY(data[15][i]);//一年内到期的非流动资产
			finance.setOCA(job.getString("OTHERLASSET"));// 其他流动资产
			 finance.setCurrentAssets(job.getString("SUMLASSET"));//流动资产合计
//			finance.setAFSFA(job.getString("SUMLASSET"));// 可供出售金融资产
			// finance.setHTMI(data[12][i]);//持有至到期投资
			// finance.setInvestmentPro(data[18][i]);//投资性房地产
			finance.setLTEI(job.getString("LTEQUITYINV"));// 长期股权投资
			// finance.setLTR(data[24][i]);//长期应收款
			finance.setFixedAssets(job.getString("FIXEDASSET"));// 固定资产
			finance.setProjectMaterials(job.getString("CONSTRUCTIONMATERIAL"));// 工程物资
			finance.setUCP(job.getString("CONSTRUCTIONPROGRESS"));// 在建工程
			finance.setDOFA(job.getString("LIQUIDATEFIXEDASSET"));// 固定资产清理
			// finance.setPBA(data[31][i]);//生产性生物资产
			// finance.setOAGA(data[33][i]);//油气资产
			finance.setIntangibleAssets(job.getString("INTANGIBLEASSET"));// 无形资产
			// finance.setDevelopmentCosts(data[35][i]);//开发支出
			// finance.setGOODWILL(data[19][i]);//商誉
			// finance.setLTPE(data[37][i]);//长期待摊费用
			finance.setDITA(job.getString("DEFERINCOMETAXASSET"));// 递延所得税资产
			finance.setONCA(job.getString("OTHERNONLASSET"));// 其他非流动资产
			finance.setNCA(job.getString("SUMNONLASSET"));// 非流动资产合计
			finance.setASSETS(job.getString("SUMASSET"));// 资产总计
			finance.setSTB(job.getString("STBORROW"));// 短期借款
			// finance.setHFTFL(data[25][i]);//交易性金融负债
			// finance.setNotesPayable(data[45][i]);//应付票据
			// finance.setAccountsPayable(data[46][i]);//应付账款

			finance.setSRP(job.getString("SALARYPAY"));// 应付职工薪酬
			finance.setTaxesPayable(job.getString("TAXPAY"));// 应交税费
			finance.setInterestPayable(job.getString("INTERESTPAY"));// 应付利息
			// finance.setDividendPayable(data[52][i]);//应付股利
			// finance.setOtherPayables(data[53][i]);//其他应付款
			finance.setEstimatedLiabilities(job.getString("ANTICIPATELIAB"));// 预计负债
			// finance.setNCLDWOY(data[57][i]);//一年内到期的非流动负债
			// finance.setBondsPayable(data[56][i]);//应付短期债券
			// finance.setOCL(data[58][i]);//其他流动负债
			// finance.setCurrentLiabilities(data[59][i]);//流动负债合计
			// finance.setLTB(data[61][i]);//长期借款
			finance.setBondsPayable(job.getString("BONDPAY"));// 应付债券
			// finance.setLTP(data[63][i]);//长期应付款
			// finance.setSpecialPayables(data[65][i]);//专项应付款
			finance.setDITL(job.getString("DEFERINCOMETAXLIAB"));// 递延所得税负债
			// finance.setONCL(data[69][i]);//其他非流动负债
			// finance.setNCL(data[70][i]);//非流动负债合计与长期负债合计一样
			// finance.setLTL(data[70][i]);//非流动负债合计与长期负债合计一样
			finance.setLIABILITIES(job.getString("SUMLIAB"));// 负债合计
			finance.setPUC(job.getString("SHARECAPITAL"));// 实收资本（或股本）
			finance.setCapitalReserves(job.getString("CAPITALRESERVE"));// 资本公积金
			finance.setSurplusReserves(job.getString("SURPLUSRESERVE"));// 盈余公积金
			finance.setRetainedProfits(job.getString("RETAINEDEARNING"));// 未分配利润
			// finance.setTreasuryStock(data[75][i]);//库存股
			// finance.setFCTD(data[74][i]);//外币报表折算差额
			finance.setMSI(job.getString("MINORITYEQUITY"));// 少数股东权益
			finance.setTEATTSOPC(job.getString("SUMPARENTEQUITY"));// 归属于母公司股东权益合计
			finance.setOwnersEquity(job.getString("SUMSHEQUITY"));// 所有者权益合计/股东权益合计
			finance.setLAOE(job.getString("SUMLIABSHEQUITY"));// 负债及所有者权益总计/负债及股东权益合计
			if (StringUtils.isEmpty(finance.getASSETS())) {
				finance.setASSETS(finance.getLAOE());
			}
			finance.setZCFlag(3);// 标记采集来源
			return finance;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

}
