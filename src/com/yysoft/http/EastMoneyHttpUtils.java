package com.yysoft.http;

import java.io.BufferedReader;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.yysoft.entity.Finance;
import com.yysoft.util.GeneralCrawler;
import com.yysoft.util.OutInfos;
import com.yysoft.util.StringUtils;


public class EastMoneyHttpUtils {
	private static Logger logger = LogManager.getLogger("fcProcessLoger"); // 执行流程日志

	public static void main(String[] args) {

		Finance f =getFinanceByReportDate("sh600216", "中国联通","2017-09-30");
//		OutInfos.OutAllFieldsDemo(f);
		System.out.println("end!");
	}


	public static Finance getFinanceByReportDate(String code, String name,String reportDate) {

		// String
		// url="http://data.eastmoney.com/DataCenter_V3/yjfp/getlist.ashx?js=var%20iBHyIrJc&pagesize=5000&page=1&sr=-1&sortType=SZZBL&mtk=%C8%AB%B2%BF%B9%C9%C6%B1&filter=(ReportingPeriod=^"+reportdate+"^)";
		// String url
		// ="http://emweb.securities.eastmoney.com/PC_HSF10/FinanceAnalysis/MainTargetAjax?code="+code+"&type=1";
		String url = "http://emweb.securities.eastmoney.com/PC_HSF10/FinanceAnalysis/ReportDetailAjax?code=" + code
				+ "&ctype=0&type=0";
		String url2 = "http://emweb.securities.eastmoney.com/PC_HSF10/FinanceAnalysis/FinanceAnalysisAjax?code=" + code
				+ "&ctype=0";
//		System.out.println("url:" + url);
//		System.out.println("url2:" + url2);
		BufferedReader details;
		BufferedReader details2;
		// details=GeneralCrawler.getPageBufferedReader(url,Constant.ENCODEUTF);
		details = GeneralCrawler.getPageBufferedReader(url, "utf-8");
		details2 = GeneralCrawler.getPageBufferedReader(url2, "utf-8");

		// 解析
		ArrayList<Finance> fs = parseSheetEast(code, name, details, details2);
		if(fs.size()>0){
			for(Finance f:fs){
				if(f.getReportDate().contains(reportDate)){
					return f;
				}
			}
		}
		return null;
	}

	public static ArrayList<Finance> parseSheetEast(String code, String name, BufferedReader br, BufferedReader br2) {

		ArrayList<Finance> resultFinances = new ArrayList<Finance>();

		Finance finance = null;
		Finance financeTemp = null;

		try {
			String jsonContent;
			String jsonContent2;
			jsonContent2 = br2.readLine();
			while ((jsonContent = br.readLine()) != null) {
				JSONObject jsonObject2 = new JSONObject(jsonContent);
				JSONObject jsonObjectzyzb = new JSONObject(jsonContent2);

				// int first = jsonContent.indexOf('[');
				// int second = jsonContent.lastIndexOf(']');

				// String result = jsonContent.substring(first,second+1);
				JSONObject jsonResult = new JSONObject(jsonObject2.getString("Result"));
				JSONObject jsonResult2 = new JSONObject(jsonObjectzyzb.getString("Result"));
				JSONArray zyzbjsonArray = new JSONArray(jsonResult2.getString("zyzb"));
				JSONObject zjcjsonobject = new JSONObject(jsonResult2.getString("bfbbb"));
				JSONObject zbjsonobject = new JSONObject(zjcjsonobject.getString("zb"));

				JSONArray zcfzjsonArray = new JSONArray(jsonResult.getString("zcfz0"));
				JSONArray lrjsonArray = new JSONArray(jsonResult.getString("lr0"));
				JSONArray xjlljsonArray = new JSONArray(jsonResult.getString("xjll0"));

//				System.out.println(zyzbjsonArray.length());
//
//				System.out.println(zcfzjsonArray.length());
				for (int j = 0; j < 5; j++) {
					finance = new Finance();
					finance.setCode(code);
					finance.setName(name);

					if (zcfzjsonArray.length() > j) {
						JSONObject jsonobject = zcfzjsonArray.getJSONObject(j);
						String reportdate = jsonobject.getString("date");
						if (reportdate.length() == 8) {
							reportdate = "20" + reportdate;
						}
//						System.out.println("reportdate=" + reportdate);
						finance.setReportDate(reportdate);
						finance.setQuarter(StringUtils.getQuarterFromReport(reportdate));
						finance.setYear(reportdate.substring(0, 4));

						finance.setMonetaryCapital(StringUtils.GetNunberByUnit(jsonobject.getString("hbzj")));
						finance.setAccountsReceivable(StringUtils.GetNunberByUnit(jsonobject.getString("yszk")));
						finance.setINVENTORY(StringUtils.GetNunberByUnit(jsonobject.getString("ch")));
						finance.setCurrentAssets(StringUtils.GetNunberByUnit(jsonobject.getString("ldzchj")));
						// finance.setCurrentAssets(StringUtils.GetNunberByUnit(zbjsonobject.getString("ldzc")));

						finance.setNCA(StringUtils.GetNunberByUnit(zbjsonobject.getString("fldzc")));

						finance.setLTEI(StringUtils.GetNunberByUnit(jsonobject.getString("cqgqtz")));
						finance.setFixedAssets(StringUtils.GetNunberByUnit(jsonobject.getString("gdzc")));
						finance.setIntangibleAssets(StringUtils.GetNunberByUnit(jsonobject.getString("wxzc")));

						finance.setASSETS(StringUtils.GetNunberByUnit(jsonobject.getString("zczj")));
						// finance.setASSETS(StringUtils.GetNunberByUnit(zbjsonobject.getString("zzc")));

						finance.setCurrentLiabilities(StringUtils.GetNunberByUnit(jsonobject.getString("ldfzhj")));
						finance.setLTL(StringUtils.GetNunberByUnit(jsonobject.getString("cqfzhj")));
						finance.setLIABILITIES(StringUtils.GetNunberByUnit(jsonobject.getString("fzhj")));
						finance.setPUC(StringUtils.GetNunberByUnit(jsonobject.getString("sszb")));
						finance.setCapitalReserves(StringUtils.GetNunberByUnit(jsonobject.getString("zbgjj")));
						finance.setOwnersEquity(StringUtils.GetNunberByUnit(jsonobject.getString("gdqyhj")));
						finance.setTEATTSOPC(StringUtils.GetNunberByUnit(jsonobject.getString("gdqyhj")));
					}

					if (zyzbjsonArray.length() > j) {
						JSONObject jsonobject = zyzbjsonArray.getJSONObject(j);
//						System.out.println(jsonobject.getString("mgjzc"));
						finance.setBEPS(StringUtils.GetNunberByUnit(jsonobject.getString("jbmgsy")));
						finance.setDEPS(StringUtils.GetNunberByUnit(jsonobject.getString("xsmgsy")));
						finance.setNAPS(StringUtils.GetNunberByUnit(jsonobject.getString("mgjzc")));
						finance.setPRP(StringUtils.GetNunberByUnit(jsonobject.getString("mgwfply")));
					}

					if (lrjsonArray.length() > j) {
						JSONObject jsonobject = lrjsonArray.getJSONObject(j);
						finance.setOperatingIncome(StringUtils.GetNunberByUnit(jsonobject.getString("yysr")));
						finance.setOperatingCost(StringUtils.GetNunberByUnit(jsonobject.getString("yycb")));
						finance.setMarketingExpen(StringUtils.GetNunberByUnit(jsonobject.getString("xsfy")));
						finance.setFinancialExpenses(StringUtils.GetNunberByUnit(jsonobject.getString("cwfy")));
						finance.setAdminExpenses(StringUtils.GetNunberByUnit(jsonobject.getString("glfy")));
						finance.setADL(StringUtils.GetNunberByUnit(jsonobject.getString("zcjzss")));
						finance.setInvestIncome(StringUtils.GetNunberByUnit(jsonobject.getString("tzsy")));

						finance.setOperatingProfit(StringUtils.GetNunberByUnit(jsonobject.getString("yylr")));
						finance.setTotalProfit(StringUtils.GetNunberByUnit(jsonobject.getString("lrze")));
						finance.setITE(StringUtils.GetNunberByUnit(jsonobject.getString("sds")));
						finance.setONPATP(StringUtils.GetNunberByUnit(jsonobject.getString("gsmgssyzjlr")));
					}

					if (xjlljsonArray.length() > j) {
						JSONObject jsonobject = xjlljsonArray.getJSONObject(j);
						finance.setCRFSOGAROS(StringUtils.GetNunberByUnit(jsonobject.getString("sddxj")));
						finance.setTRRD(StringUtils.GetNunberByUnit(jsonobject.getString("sddsffh")));
						finance.setOCRCOA(StringUtils.GetNunberByUnit(jsonobject.getString("sdqtdxj")));
						finance.setCPFGPASR(StringUtils.GetNunberByUnit(jsonobject.getString("gmspjslwzfdxj")));
						finance.setCPTAFE(StringUtils.GetNunberByUnit(jsonobject.getString("zfgzgyjwzgzfdxj")));
						finance.setCPFTAS(StringUtils.GetNunberByUnit(jsonobject.getString("zfdgxsf")));
						finance.setCPFOOA(StringUtils.GetNunberByUnit(jsonobject.getString("zfqtyjyhdygdxj")));

						finance.setNCFFOA(StringUtils.GetNunberByUnit(jsonobject.getString("jyhdcsdxjllje")));
						finance.setNCFDOFAIAAOLTA(StringUtils.GetNunberByUnit(jsonobject.getString("shdxjje")));
						finance.setCPFPACOFAIAAOLTA(StringUtils.GetNunberByUnit(jsonobject.getString("zfdxj")));
						finance.setNCFFIA(StringUtils.GetNunberByUnit(jsonobject.getString("tzhdcsdxjllje")));

						finance.setCRFCC(StringUtils.GetNunberByUnit(jsonobject.getString("xstzsddxj")));
						finance.setBorrowingsReceived(StringUtils.GetNunberByUnit(jsonobject.getString("qdjksddxj")));
						finance.setCROAB(StringUtils.GetNunberByUnit(jsonobject.getString("fpzfdxj")));
						finance.setNCFFFA(StringUtils.GetNunberByUnit(jsonobject.getString("czhdcsdxjllje")));

					}
					resultFinances.add(finance);
				}
			}

			// OutInfos.OutAllFieldsDemo(finance);//打印

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return resultFinances;
	}

}
