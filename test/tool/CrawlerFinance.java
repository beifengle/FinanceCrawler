package tool;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


import com.yysoft.util.GeneralCrawler;
import com.yysoft.util.OutInfos;
import com.yysoft.dao.StockDAO;
import com.yysoft.data.handle.DataExport;
import com.yysoft.entity.Finance;
import com.yysoft.entity.Stock;
import com.yysoft.entity.StockDividends;
import com.yysoft.http.JRJHttpUtils;
import com.yysoft.http.QQHttpUtils;
import com.yysoft.mgr.TrapsDataMgr;
import com.yysoft.util.ConfigParser;
import com.yysoft.util.Constant;
import com.yysoft.util.StringUtils;


public class CrawlerFinance {
	
	private static Logger logger = LogManager.getLogger(CrawlerFinance.class); //

	public static void main(String[] args) {
		new ConfigParser();
        /*
		final Vector<Stock> stocks = StockDAO.getNeedHistoryFinanceDataCodes();
		for (Stock stock : stocks) {
			//logger.info(开始采集：" + stock.getCode());
			
				System.out.println(stock.getCode());	
				System.out.println(stock.getName());
				execFinance(stock.getCode(),stock.getName());				
		}
		*/
		//execFinance("sh600733","S*ST前锋");
		//execFinance("sh600527","江南高纤");
		//execFinance("sh600816","安信信托");



		execFinance("sh603618","杭电股份");
		execFinance("sz000737","南风化工");

		
		//execFinance("sz000661","长春高新");
		//execFinance("sz300476","胜宏科技");


		
		
		/*
    	//String ExcelSql="update  stock_historical_price set FAP=  close   where code='sz300598' and date >='2017-04-26';update  stock_historical_price set FAP=  close-0.1   where code='sz300598' and date <'2017-04-26';";
    	String ExcelSql="update  stock_historical_price set FAP=  close   where code='sz300598' and date >='2017-04-26';update  stock_historical_price set FAP=  close-0.1   where code='sz300598' and date <'2017-04-26'";
		System.out.println(ExcelSql);
  	   StockDAO.runSql(ExcelSql);
  	   */
		DataExport.exec();
	}
	
	
	public static void execFinance(String code,String name){
		
		//String url="http://data.eastmoney.com/DataCenter_V3/yjfp/getlist.ashx?js=var%20iBHyIrJc&pagesize=5000&page=1&sr=-1&sortType=SZZBL&mtk=%C8%AB%B2%BF%B9%C9%C6%B1&filter=(ReportingPeriod=^"+reportdate+"^)";
		//String url ="http://emweb.securities.eastmoney.com/PC_HSF10/FinanceAnalysis/MainTargetAjax?code="+code+"&type=1";
		String url ="http://emweb.securities.eastmoney.com/PC_HSF10/FinanceAnalysis/ReportDetailAjax?code="+code+"&ctype=0&type=0";
		String url2="http://emweb.securities.eastmoney.com/PC_HSF10/FinanceAnalysis/FinanceAnalysisAjax?code="+code+"&ctype=0";
		
		BufferedReader details;
		BufferedReader details2;
		//details=GeneralCrawler.getPageBufferedReader(url,Constant.ENCODEUTF);
		details=GeneralCrawler.getPageBufferedReader(url,"utf-8");
		details2=GeneralCrawler.getPageBufferedReader(url2,"utf-8");
		
		
		//解析
		parseSheetEast(code,name,details,details2);	

	}

	public static void parseSheetEast(String code,String name,BufferedReader br,BufferedReader br2) {

		DecimalFormat df = new DecimalFormat("######0.0000");
		DecimalFormat df1 = new DecimalFormat("######0.00");
		String sql="insert into dev_stockwin.stock_historical_dividends(code,name,szzbl,zgbl,xjfh,yaggr,recordDate,cqcxr,stock_total,EarningsPerShare,NetAssetsPerShare,mggjj,mgwfply,jlytbzz,dividendPlan,dividendPlanFlag,reportDate) values ";
		
		ArrayList<Finance> resultFinances = new ArrayList<Finance>();

		Finance finance = null;
		Finance financeTemp = null;
		
		

		try {
			String jsonContent;
			String jsonContent2;
			jsonContent2 = br2.readLine();
			while ((jsonContent = br.readLine()) != null) {
				JSONObject jsonObject2 =new JSONObject(jsonContent);
				JSONObject jsonObjectzyzb =new JSONObject(jsonContent2);

				//int first = jsonContent.indexOf('[');
				//int second = jsonContent.lastIndexOf(']');

				//String result = jsonContent.substring(first,second+1);
				  JSONObject jsonResult = new JSONObject(jsonObject2.getString("Result"));
				  JSONObject jsonResult2 = new JSONObject(jsonObjectzyzb.getString("Result"));
				  
	 		     JSONArray zyzbjsonArray = new JSONArray(jsonResult2.getString("zyzb"));
	 		     JSONObject zjcjsonobject = new JSONObject(jsonResult2.getString("bfbbb"));
	 		    JSONObject zbjsonobject = new JSONObject(zjcjsonobject.getString("zb"));
	 		     
	 		     
 				 JSONArray zcfzjsonArray = new JSONArray(jsonResult.getString("zcfz0"));
 				 JSONArray lrjsonArray   = new JSONArray(jsonResult.getString("lr0"));
 				 JSONArray xjlljsonArray = new JSONArray(jsonResult.getString("xjll0"));
 				 
 				
 				System.out.println(zyzbjsonArray.length());	
					int i = 0;
					int executeNum = 0;// 实施方案股票数量
					int holdersPublishNum = 0;// 股东大会预案公告股票数量
					int leadersPublishNum = 0;// 董事会公告股票数量
					int flag = 0;// 标志位,0-预案,1-决案

		            String values="";
		            String ExcelSql="";
		            int k=0;
					System.out.println(zcfzjsonArray.length());
					for(int j=0;j<1;j++){
						finance = new Finance();
						finance.setCode(code);
						finance.setName(name);
						
						if(zcfzjsonArray.length()> j){
							JSONObject jsonobject = zcfzjsonArray.getJSONObject(j);
	                        String reportdate = jsonobject.getString("date");
	                        if(reportdate.length()==8){
	                        	reportdate = "20"+reportdate;
	                        }
	                        finance.setReportDate(reportdate);
	            			finance.setQuarter(StringUtils.getQuarterFromReport(reportdate));
	            			finance.setYear(reportdate.substring(0,4));

							finance.setMonetaryCapital(StringUtils.GetNunberByUnit(jsonobject.getString("hbzj")));
							finance.setAccountsReceivable(StringUtils.GetNunberByUnit(jsonobject.getString("yszk")));
							finance.setINVENTORY(StringUtils.GetNunberByUnit(jsonobject.getString("ch")));
							//finance.setCurrentAssets(StringUtils.GetNunberByUnit(jsonobject.getString("ldzchj")));
							finance.setCurrentAssets(StringUtils.GetNunberByUnit(zbjsonobject.getString("ldzc")));

							finance.setNCA(StringUtils.GetNunberByUnit(zbjsonobject.getString("fldzc")));
							
							finance.setLTEI(StringUtils.GetNunberByUnit(jsonobject.getString("cqgqtz")));
							finance.setFixedAssets(StringUtils.GetNunberByUnit(jsonobject.getString("gdzc")));
							finance.setIntangibleAssets(StringUtils.GetNunberByUnit(jsonobject.getString("wxzc")));
							
							//finance.setASSETS(StringUtils.GetNunberByUnit(jsonobject.getString("zczj")));
							finance.setASSETS(StringUtils.GetNunberByUnit(zbjsonobject.getString("zzc")));
							
							finance.setCurrentLiabilities(StringUtils.GetNunberByUnit(jsonobject.getString("ldfzhj")));
							finance.setLTL(StringUtils.GetNunberByUnit(jsonobject.getString("cqfzhj")));
							finance.setLIABILITIES(StringUtils.GetNunberByUnit(jsonobject.getString("fzhj")));
							finance.setPUC(StringUtils.GetNunberByUnit(jsonobject.getString("sszb")));
							finance.setCapitalReserves(StringUtils.GetNunberByUnit(jsonobject.getString("zbgjj")));
							finance.setOwnersEquity(StringUtils.GetNunberByUnit(jsonobject.getString("gdqyhj")));
							finance.setTEATTSOPC(StringUtils.GetNunberByUnit(jsonobject.getString("gdqyhj")));
						}

						
						
						if(zyzbjsonArray.length()> j){
							JSONObject jsonobject = zyzbjsonArray.getJSONObject(j);
			 				System.out.println(jsonobject.getString("mgjzc"));	
							finance.setBEPS(StringUtils.GetNunberByUnit(jsonobject.getString("jbmgsy")));
							finance.setDEPS(StringUtils.GetNunberByUnit(jsonobject.getString("xsmgsy")));
                            finance.setNAPS(StringUtils.GetNunberByUnit(jsonobject.getString("mgjzc")));
                            finance.setPRP(StringUtils.GetNunberByUnit(jsonobject.getString("mgwfply")));
						}
						
						if(lrjsonArray.length()> j){
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

						if(xjlljsonArray.length()> j){
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
						
						
						try {
						financeTemp = JRJHttpUtils.downPage(finance, code, name, 3, "2017-12-31");
						if(financeTemp!=null){
							finance = financeTemp;
						}
						} catch(Exception we){
							
						}
						
						System.out.println("NAPS");
						System.out.println(finance.getNAPS());

						
						resultFinances.add(finance);

					}

					
	
			}
			StockDAO.insertDB(resultFinances, true);

			//OutInfos.OutAllFieldsDemo(finance);//打印

		} catch (Exception ex) {
			//logger.error(ex.getMessage());
			System.out.println(ex.getMessage());
		}

	}


}

