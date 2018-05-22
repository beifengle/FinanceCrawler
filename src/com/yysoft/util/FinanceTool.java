package com.yysoft.util;

import java.text.DecimalFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.entity.Finance;
import com.yysoft.entity.FinanceLog;
/**
 * 功能描述：财报对象
 * @author Huanyan.Lu
 * @date:2018年3月31日
 * @time:上午11:30:16
 * @version:1.0
 */
public class FinanceTool {
	private static Logger logger = LogManager.getLogger("fcHisLoger"); //
	static DecimalFormat df4 = new DecimalFormat("######0.0000"); // 保留四位小数
	/**
	 * 合并三大数据源财报对象，以f1为主(最终值整合到f1)，值不为空即有效。
	 * @param f1
	 * @param f2
	 * @param f3
	 * @param fl
	 * @return
	 */
	public static Finance mergeFinances(Finance f1,Finance f2,Finance f3,FinanceLog fl){
		
		// 1.资产表相关关键值整合
		f1.setLAOE(mergeValues("负债及所有者权益总计",f1.getLAOE(),f2.getLAOE(),f3.getLAOE(),fl,1));
		f1.setOwnersEquity(mergeValues("所有者权益合计",f1.getOwnersEquity(),f2.getOwnersEquity(),f3.getOwnersEquity(),fl,1));
		f1.setMSI(mergeValues("少数股东权益",f1.getMSI(),f2.getMSI(),f3.getMSI(),fl,1));
		f1.setRetainedProfits(mergeValues("未分配利润",f1.getRetainedProfits(),f2.getRetainedProfits(),f3.getRetainedProfits(),fl,1));
		f1.setLIABILITIES(mergeValues("负债合计",f1.getLIABILITIES(),f2.getLIABILITIES(),f3.getLIABILITIES(),fl,1));
		f1.setASSETS(mergeValues("资产总计",f1.getASSETS(),f2.getASSETS(),f3.getASSETS(),fl,1));
		f1.setTEATTSOPC(mergeValues("归属于母公司股东权益合计",f1.getTEATTSOPC(),f2.getTEATTSOPC(),f3.getTEATTSOPC(),fl,1));
		f1.setCapitalReserves(mergeValues("资本公积金",f1.getCapitalReserves(),f2.getCapitalReserves(),f3.getCapitalReserves(),fl,1));
		f1.setAccountsReceivable(mergeValues("应收账款净额",f1.getAccountsReceivable(),f2.getAccountsReceivable(),f3.getAccountsReceivable(),fl,1));
		f1.setCurrentAssets(mergeValues("流动资产合计",f1.getCurrentAssets(),f2.getCurrentAssets(),f3.getCurrentAssets(),fl,1));//
		f1.setCurrentLiabilities(mergeValues("流动负债合计",f1.getCurrentLiabilities(),f2.getCurrentLiabilities(),f3.getCurrentLiabilities(),fl,1));
		f1.setDividendPayable(mergeValues("应付股利",f1.getDividendPayable(),f2.getDividendPayable(),f3.getDividendPayable(),fl,1));
		f1.setINVENTORY(mergeValues("存货",f1.getINVENTORY(),f2.getINVENTORY(),f3.getINVENTORY(),fl,1));
		f1.setLTL(mergeValues("长期负债合计",f1.getLTL(),f2.getLTL(),f3.getLTL(),fl,1));
		f1.setNCA(mergeValues("非流动资产合计",f1.getNCA(),f2.getNCA(),f3.getNCA(),fl,1));
		
		// 2.流量现金表相关关键值整合
		f1.setCEATEOTP(mergeValues("期末现金及现金等价物余额",f1.getCEATEOTP(),f2.getCEATEOTP(),f3.getCEATEOTP(),fl,2));
		f1.setCEATBOTP(mergeValues("期初现金及现金等价物余额",f1.getCEATBOTP(),f2.getCEATBOTP(),f3.getCEATBOTP(),fl,2));
		f1.setNIICACE(mergeValues("现金及现金等价物净增加额",f1.getNIICACE(),f2.getNIICACE(),f3.getNIICACE(),fl,2));
		f1.setNCFFOA(mergeValues("经营活动产生的现金流量净额",f1.getNCFFOA(),f2.getNCFFOA(),f3.getNCFFOA(),fl,2));
		f1.setNCFFIA(mergeValues("投资活动产生的现金流量净额",f1.getNCFFIA(),f2.getNCFFIA(),f3.getNCFFIA(),fl,2));
		f1.setNCFFFA(mergeValues("筹资活动产生的现金流量净额",f1.getNCFFFA(),f2.getNCFFFA(),f3.getNCFFFA(),fl,2));
		f1.setCPFDAPDOIP(mergeValues("分配股利、利润或偿付利息支付的现金",f1.getCPFDAPDOIP(),f2.getCPFDAPDOIP(),f3.getCPFDAPDOIP(),fl,2));
		
		
		// 3.利润表相关关键值整合
		f1.setDEPS(mergeValues("稀释每股收益",f1.getDEPS(),f2.getDEPS(),f3.getDEPS(),fl,3));
		f1.setBEPS(mergeValues("基本每股收益",f1.getBEPS(),f2.getBEPS(),f3.getBEPS(),fl,3));
		f1.setONPATP(mergeValues("归属于母公司股东的净利润",f1.getONPATP(),f2.getONPATP(),f3.getONPATP(),fl,3));
		f1.setMII(mergeValues("少数股东损益",f1.getMII(),f2.getMII(),f3.getMII(),fl,3));
		f1.setNetProfit(mergeValues("净利润",f1.getNetProfit(),f2.getNetProfit(),f3.getNetProfit(),fl,3));
		f1.setTotalProfit(mergeValues("利润总额",f1.getTotalProfit(),f2.getTotalProfit(),f3.getTotalProfit(),fl,3));
		f1.setOperatingProfit(mergeValues("营业利润",f1.getOperatingProfit(),f2.getOperatingProfit(),f3.getOperatingProfit(),fl,3));
		f1.setTOC(mergeValues("营业总成本",f1.getTOC(),f2.getTOC(),f3.getTOC(),fl,3));
		f1.setOperatingCost(mergeValues("营业成本",f1.getOperatingCost(),f2.getOperatingCost(),f3.getOperatingCost(),fl,3));
		f1.setOperatingIncome(mergeValues("营业收入",f1.getOperatingIncome(),f2.getOperatingIncome(),f3.getOperatingIncome(),fl,3));
		f1.setTOI(mergeValues("营业总收入",f1.getTOI(),f2.getTOI(),f3.getTOI(),fl,3));
		f1.setInvestIncome(mergeValues("投资收益",f1.getInvestIncome(),f2.getInvestIncome(),f3.getInvestIncome(),fl,3));//
		
		// 4 其它值
		f1.setNAPS(mergeValues("每股净资产",f1.getNAPS(),f2.getNAPS(),f3.getNAPS(),fl,3));
		f1.setPRP(mergeValues("每股未分配利润",f1.getPRP(),f2.getPRP(),f3.getPRP(),fl,3));
		f1.setDNNP(mergeValues("扣非净利润",f1.getDNNP(),f2.getDNNP(),f3.getDNNP(),fl,3));
		f1.setDNNPGR(mergeValues("扣非净利润同比增长",f1.getDNNPGR(),f2.getDNNPGR(),f3.getDNNPGR(),fl,3));
		
		// 把对象f1赋给f1的对象属性fl
		f1.setFl(fl);
		return f1;//合并结果以f1来源为主
		
	}
	/**
	 * 合并三个数据来源不同的值,且都有值的情况下按v1,v2,v3顺序优先选择。
	 * @param vName //值名称
	 * @param v1 //来源于金融街
	 * @param v2 //来源于新浪
	 * @param v3 //来源于东方财富
	 * @param fl
	 * @param reportType //财报类型：1-资产负债表；2-现金流量表；3-利润表
	 * @return
	 */
	public static String mergeValues(String vName,String v1,String v2,String v3,FinanceLog fl,int reportType){
		String desc = "";
		String log = "";
		 
		if(StringUtils.isNotEmpty(v1) && StringUtils.isNotEmpty(v2) && StringUtils.isNotEmpty(v3)){
			double vd1 =  Double.valueOf(v1);
			double vd2 =  Double.valueOf(v2);
			double vd3 =  Double.valueOf(v3);
			if(reportType==1){
				fl.setJRJZCFlag(2);
				if(!(df4.format(Double.valueOf(v1))==df4.format(Double.valueOf(v2)) && df4.format(Double.valueOf(v1))==df4.format(Double.valueOf(v3)))){
					log =  "[资产负债表]-" + vName +":" + v1 + "  (各网站原值：jrj=" + v1 + " ,sina=" + v2 + "  ,eastMoney=" + v3 + ")";
					desc = fl.getLOG() + log + "\r\n ";
					logger.info(log);
					fl.setLOG(desc);
				}
				v1 = df4.format(InspectorValues.selectBestValue(vd1, vd2, vd3));
				
				return v1;
			}else if(reportType==2){
				fl.setJRJXJFlag(2);
				log = "[现金流量表]-" + vName +":" + v1 + "  (各网站原值：jrj=" + v1 + " ,sina=" + v2 + "  ,eastMoney=" + v3 + ")";
				desc = fl.getLOG() + log + "\r\n ";
				logger.info(log);
				fl.setLOG(desc);
				v1 = df4.format(InspectorValues.selectBestValue(vd1, vd2, vd3));
				return v1;
			}else if(reportType==3){
				fl.setJRJLRFlag(2);
				log ="[利   润  表]-" + vName +":" + v1 + "  (各网站原值：jrj=" + v1 + " ,sina=" + v2 + "  ,eastMoney=" + v3 + ")";
				desc = fl.getLOG() + log + "\r\n ";
				logger.info(log);
				fl.setLOG(desc);
				v1 = df4.format(InspectorValues.selectBestValue(vd1, vd2, vd3));
				return v1;		
			}
		}else if(StringUtils.isNotEmpty(v1) && !(StringUtils.isNotEmpty(v2) && StringUtils.isNotEmpty(v3))){
			if(reportType==1){
				fl.setJRJZCFlag(2);
				log =  "[资产负债表]-" + vName +":" + v1 + "  (各网站原值：jrj=" + v1 + " ,sina=" + v2 + "  ,eastMoney=" + v3 + ")";
				desc = fl.getLOG() + log + "\r\n ";
				logger.info(log);
				fl.setLOG(desc);
				return v1;
			}else if(reportType==2){
				fl.setJRJXJFlag(2);
				log = "[现金流量表]-" + vName +":" + v1 + "  (各网站原值：jrj=" + v1 + " ,sina=" + v2 + "  ,eastMoney=" + v3 + ")";
				desc = fl.getLOG() + log + "\r\n ";
				logger.info(log);
				fl.setLOG(desc);
				return v1;
			}else if(reportType==3){
				fl.setJRJLRFlag(2);
				log ="[利   润  表]-" + vName +":" + v1 + "  (各网站原值：jrj=" + v1 + " ,sina=" + v2 + "  ,eastMoney=" + v3 + ")";
				desc = fl.getLOG() + log + "\r\n ";
				logger.info(log);
				fl.setLOG(desc);
				return v1;
			}
		}else if(StringUtils.isEmpty(v1) && StringUtils.isNotEmpty(v2)){
			if(reportType==1){
				fl.setSINAZCFlag(2);
				log = "[资产负债表]-" + vName +":" + v2 + "  (各网站原值：jrj=" + v1 + " ,sina=" + v2 + "  ,eastMoney=" + v3 + ")";
				desc = fl.getLOG() + log + "\r\n ";
				logger.info(log);
				fl.setLOG(desc);
				return v2;
			}else if(reportType==2){
				fl.setSINAXJFlag(2);
				log = "[现金流量表]-" + vName +":" + v2 + "  (各网站原值：jrj=" + v1 + " ,sina=" + v2 + "  ,eastMoney=" + v3 + ")";
				desc = fl.getLOG() + log + "\r\n ";
				logger.info(log);
				fl.setLOG(desc);
				return v2;
			}else if(reportType==3){
				fl.setSINALRFlag(2);
				log = "[利   润  表]-" + vName +":" + v2 + "  (各网站原值：jrj=" + v1 + " ,sina=" + v2 + "  ,eastMoney=" + v3 + ")";
				desc = fl.getLOG() + log + "\r\n ";
				logger.info(log);
				fl.setLOG(desc);
				return v2;
			}
		}else if(StringUtils.isEmpty(v1) && StringUtils.isEmpty(v2) && StringUtils.isNotEmpty(v3)){
			if(reportType==1){
				fl.setEASTZCFlag(2);
				log = "[资产负债表]-" + vName +":" + v3 + "  (各网站原值：jrj=" + v1 + " ,sina=" + v2 + "  ,eastMoney=" + v3 + ")";
				desc = fl.getLOG() + log + "\r\n ";
				logger.info(log);
				fl.setLOG(desc);
				return v3;
			}else if(reportType==2){
				fl.setEASTXJFlag(2);
				log = "[现金流量表]-" + vName +":" + v3 + "  (各网站原值：jrj=" + v1 + " ,sina=" + v2 + "  ,eastMoney=" + v3 + ")";
				desc = fl.getLOG() + log + "\r\n ";
				logger.info(log);
				fl.setLOG(desc);
				return v3;
			}else if(reportType==3){
				fl.setEASTLRFlag(2);
				log = "[利   润  表]-" + vName +":" + v3 + "  (各网站原值：jrj=" + v1 + " ,sina=" + v2 + "  ,eastMoney=" + v3 + ")";
				desc = fl.getLOG() + log + "\r\n ";
				logger.info(log);
				fl.setLOG(desc);
				return v3;
			}
		}else{
			log = "[三表值为空]-" + vName +":" + v1 + "  (各网站原值：jrj=" + v1 + " ,sina=" + v2 + "  ,eastMoney=" + v3 + ")";
			desc = fl.getLOG() + log + "\r\n ";
			logger.info(log);
			fl.setLOG(desc);
			return v1;
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		logger.entry();
		Finance f = new Finance();
		String vName = "p";
		String v1 = null;
		String v2 = "";
		String v3 = "p";
		String log = "\r\n" + vName +":" + v1 + "(jrj）," + v2 + "(sina）," + v3 + "(eastMoney）";
		System.out.println(StringUtils.isNotEmpty(v1)&&StringUtils.isNotEmpty(v3));
		 
	}
}
