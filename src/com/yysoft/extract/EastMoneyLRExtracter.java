package com.yysoft.extract;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yysoft.entity.Finance;
import com.yysoft.util.StringUtils;

/**
 * 功能描述：利润分配表页面数据提取
 * @author Huanyan.Lu
 * @date:2018年8月19日
 * @time:下午3:08:02
 * @version:1.0
 */
public class EastMoneyLRExtracter {
	private static Logger logger = LogManager.getLogger(EastMoneyLRExtracter.class); //

	
	/**
	 * Description:利润分配表页面数据提取
	 * @param finance 提取信息实例
	 * @param data 读取的页面元信息
	 * @param code 股票代码
	 * @param name 股票简称
	 * @param reportDate 要提取信息的报告期
	 * @return
	 */
	public static  Finance fetch(Finance finance,String data,String code,String name,String reportDate){
		logger.info("提取的报告期:"+ reportDate + "===东方财富-利润分配表页面数据项信息提取===");
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
			finance.setReportDate(reportDate);//报告期
			finance.setQuarter(StringUtils.getQuarterFromReport(reportDate));
			finance.setYear(finance.getReportDate().substring(0,4));
			
			if(isHaving!=true){
				return null;
			}
			
			finance.setTOI(job.getString("TOTALOPERATEREVE"));//营业总收入
			finance.setOperatingIncome(job.getString("OPERATEREVE"));//营业收入
			
			if(StringUtils.isEmpty(finance.getTOI()) || finance.getTOI().equals("0.0000")){
				finance.setTOI(finance.getOperatingIncome());
			}
			if(StringUtils.isEmpty(finance.getOperatingIncome()) || finance.getOperatingIncome().equals("0.0000")){
				finance.setOperatingIncome(finance.getTOI());
			}
			
			finance.setTOC(job.getString("TOTALOPERATEEXP"));//营业总成本
			finance.setOperatingCost(job.getString("OPERATEEXP"));//营业成本
			
			if(StringUtils.isEmpty(finance.getTOC()) || finance.getTOC().equals("0.0000")){
				finance.setTOC(finance.getOperatingCost());
			}
			if(StringUtils.isEmpty(finance.getOperatingCost()) || finance.getOperatingCost().equals("0.0000")){
				finance.setOperatingCost(finance.getTOC());
			}
			
			finance.setBTAS(job.getString("OPERATETAX"));//营业税金及附加
			finance.setMarketingExpen(job.getString("SALEEXP"));//销售费用
			finance.setAdminExpenses(job.getString("MANAGEEXP"));//管理费用
			finance.setFinancialExpenses(job.getString("FINANCEEXP"));//财务费用
			finance.setADL(job.getString("ASSETDEVALUELOSS"));//资产减值损失
			finance.setInvestIncome(job.getString("INVESTINCOME"));//投资收益
			finance.setOperatingProfit(job.getString("OPERATEPROFIT"));//营业利润
			finance.setNOI(job.getString("NONOPERATEREVE"));//营业外收入
			finance.setNOE(job.getString("NONOPERATEEXP"));//营业外支出
			finance.setLFDONCA(job.getString("NONLASSETNETLOSS"));//非流动资产处置净损失
			finance.setTotalProfit(job.getString("SUMPROFIT"));//利润总额
			finance.setITE(job.getString("INCOMETAX"));//所得税
			finance.setNetProfit(job.getString("NETPROFIT"));//净利润
			finance.setMII(job.getString("MINORITYINCOME"));//少数股东损益
			finance.setONPATP(job.getString("PARENTNETPROFIT"));//归属于母公司股东的净利润
			finance.setBEPS(job.getString("BASICEPS"));//基本每股收益
			finance.setDEPS(job.getString("DILUTEDEPS"));//稀释每股收益
			
			finance.setLRFlag(3);//标记采集来源
			return finance;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
			
		return null;
	}
	
}
