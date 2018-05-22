package com.yysoft.data.handle;

import java.io.BufferedReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.yysoft.dao.StockDAO;
import com.yysoft.entity.Stock;
import com.yysoft.entity.StockDividends;



/**
 * 采集东方财富分红转增信息 网址:http://data.eastmoney.com/gsrl/nbjb.html
 * 接口:http://datainterface.eastmoney.com/EM_DataCenter/JS.aspx?type=GSRL&sty=
 * GSRL&stat=20&fd=2016-01-22&sr=2&p=1&ps=50&callback=callback&_=1456728308032
 * 
 * @author bzl
 * @date:2016年02月29日
 * @time:下午15:19:56
 */
public class GatherDividends {
	private static Logger logger = LogManager.getLogger(GatherDividends.class); // 记录日志

	/**
	 * 解析
	 * 
	 * @author bzl
	 * @date:2016年02月29日
	 * @time:下午15:19:56
	 */
	public static void parseSheetEast(BufferedReader br, String date) {

		DecimalFormat df = new DecimalFormat("######0.0000");
		DecimalFormat df1 = new DecimalFormat("######0.00");
		String sql = "";
		try {
			String jsonContent;
			while ((jsonContent = br.readLine()) != null) {
				System.out.println(date);
				if (!(jsonContent.indexOf("stats:false") >= 0)) {
					jsonContent = jsonContent.replace("([{", "[{");
					jsonContent = jsonContent.replace("}])", "}]");
					JSONArray jsonArray = new JSONArray(jsonContent);
					int i = 0;
					int executeNum = 0;// 实施方案股票数量
					int holdersPublishNum = 0;// 股东大会预案公告股票数量
					int leadersPublishNum = 0;// 董事会公告股票数量
					int flag = 0;// 标志位,0-预案,1-决案
					/*
					 * SECUCODE 代码 EXDIVIDENDDATE 除权除息日 EXECUTECONTENT 实施方案说明
					 * PROGRESS 方案进度 LEADERSPUBLISHCONTENT 董事会预案说明 DIVIPAYDATE
					 * 派息转股日 SECURITYSHORTNAME 简称 HOLDERSPUBLISHCONTENT 股东大会预案说明
					 * HOLDERSPUBLISHDATE 股东大会预案公告日期 PAYYEAR 分配年度 EXECUTEDATE
					 * 实施方案公告日期 LEADERSPUBLISHDATE 董事会预案公告日期 RIGHTREGDATE 股权登记日
					 */

					for (i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonobject = jsonArray.getJSONObject(i);
						String code = jsonobject.getString("SECUCODE");
						String exdividendDate = jsonobject.getString("EXDIVIDENDDATE");// 除权除息日
						String executeContent = jsonobject.getString("EXECUTECONTENT");// 实施方案说明
						String reportDate = jsonobject.getString("PAYYEAR");// 报告期
						String holdersPublishDate = jsonobject.getString("HOLDERSPUBLISHDATE");// 股东大会预案公告日期
						String holdersPublishContent = jsonobject.getString("HOLDERSPUBLISHCONTENT");// 股东大会预案说明
						String leadersPublishDate = jsonobject.getString("LEADERSPUBLISHDATE");// 董事会预案公告日期
						String leadersPublishContent = jsonobject.getString("LEADERSPUBLISHCONTENT");// 董事会预案说明
						String rightregdate = jsonobject.getString("RIGHTREGDATE");// 股权登记日
						double div = 0; // 分红
						double increase = 0; // 送转
						if (code.indexOf("SZ") >= 0) {
							code = code.replace(".SZ", "");
							code = "sz" + code;
						} else if (code.indexOf("SH") >= 0) {
							code = code.replace(".SH", "");
							code = "sh" + code;
						}
						String[] report = reportDate.split("-");
						if (reportDate.indexOf("6-30") >= 0) {
							if (report.length > 0) {
								reportDate = report[0] + "-06-30";
							}
						}
						if (report[1].endsWith("12")) {
							// 决案
							if (!executeContent.equals("")) {
								div = GatherDividends.parseDividends(executeContent);
								div = Double.parseDouble(df.format(div));
								increase = GatherDividends.parseIncrease(executeContent);
								increase = Double.parseDouble(df.format(increase));
								flag = 1;
								String dividendCodeStr = "";
								ArrayList<StockDividends> dividendtab = StockDAO
										.getStockDividendsCode(Integer.parseInt(report[0]), 4);
								for (StockDividends s : dividendtab) {
									dividendCodeStr += s.getCode() + ",";
								} // 判断同期表中是否存在该code
								if (dividendCodeStr.contains(code)) {
									for (StockDividends s1 : dividendtab) {
										if (s1.getCode().equals(code)) {
											if (s1.getRecordDate().equals("") && !rightregdate.equals("")) {
												logger.info(code+"股权登记日出现差异");
												sql = "update stock_dividends set dividendPlanFlag=" + flag
														+ ",recordDate='" + rightregdate + "' where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
												StockDAO.runSql(sql);
											}
											if (s1.getDividendPerShare()!=div) {
												logger.info(code+"分红出现差异");
												sql="update stock_dividends set dividendPerShare="+div+",dividendPlanAbstract='10派" + df1.format(div * 10)
													+ "元' where `code`='" + code+ "' and year=" + report[0] + " and quarter=4 " ;
												StockDAO.runSql(sql);
											}
										}
									}
								} else {
									String stocksCodeStr = "";
									HashSet<Stock> stocktab = StockDAO.getStockCode(1, 1);
									for (Stock s : stocktab) {
										stocksCodeStr += s.getCode() + ",";
									}
									if (stocksCodeStr.contains(code)) {
										logger.info(code+"金融界数据缺失");
										// 有无分红
										if (div == 0) {
											sql = "insert into stock_dividends(code,name,year,quarter,recordDate,dividendPlan,dividendPlanFlag,dividendPlanAbstract,reportDate,announDate) values('"
													+ code + "',(select name from stock_company where code='" + code
													+ "'),'" + report[0] + "'," + 4 + ",'" + rightregdate + "','"
													+ executeContent + "'," + 1 + ",'不分配','" + reportDate + "','"
													+ holdersPublishDate + "')";
											StockDAO.runSql(sql);
										} else {
											sql = "insert into stock_dividends(code,name,year,quarter,recordDate,dividendPerShare,dividendPlan,dividendPlanFlag,dividendPlanAbstract,reportDate,announDate) values('"
													+ code + "',(select name from stock_company where code='" + code
													+ "'),'" + report[0] + "'," + 4 + ",'" + rightregdate + "'," + div
													+ ",'" + executeContent + "'," + 1 + ",'10派" + df1.format(div * 10)
													+ "元','" + reportDate + "','" + holdersPublishDate + "')";
											StockDAO.runSql(sql);
										}
										// 有无送转
										if (increase == 0) {
											sql = "update stock_dividends set dividendPlanIncrease='不送转',dividendIncrease=0  where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
											StockDAO.runSql(sql);
										} else {
											sql = "update stock_dividends set dividendPlanIncrease='10送转"
													+ increase + "股' ,dividendIncrease=" + increase + " where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
											StockDAO.runSql(sql);
										}
									}
								}
								executeNum++;
								System.out.println("-------------------------------------------");
								System.out.println("第" + (i + 1) + "支");
								System.out.println("状态：\t\t实施分配");
								System.out.println("股票代码：\t\t" + code);
								System.out.println("分配年度：\t\t" + reportDate);
								System.out.println("实施方案说明：\t" + executeContent);
								System.out.println("除权除息日：\t" + exdividendDate);
								System.out.println("每股股息：\t\t" + div);
								// System.out.println("sql："+sql);
							}
							// 股东大会预案
							else if (!holdersPublishContent.equals("")) {
								div = GatherDividends.parseDividends(holdersPublishContent);
								div = Double.parseDouble(df.format(div));
								increase = GatherDividends.parseIncrease(holdersPublishContent);
								increase = Double.parseDouble(df.format(increase));
								flag = 2;
								String dividendCodeStr = "";
								ArrayList<StockDividends> dividendtab = StockDAO
										.getStockDividendsCode(Integer.parseInt(report[0]), 4);
								for (StockDividends s : dividendtab) {
									dividendCodeStr += s.getCode() + ",";
								} // 判断同期表中是否存在该code ,存在则判断股息是否一致,不一致再做判断
								if (dividendCodeStr.contains(code)) {
									for (StockDividends s1 : dividendtab) {
										if (s1.getCode().equals(code)) {
											if (s1.getRecordDate().equals("") && !rightregdate.equals("")) {
												logger.info(code+"股权登记日出现差异");
												sql = "update stock_dividends set dividendPlanFlag=" + flag
														+ ",recordDate='" + rightregdate + "' where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
												StockDAO.runSql(sql);
											}
											if (s1.getDividendPerShare()!=div) {
												logger.info(code+"分红出现差异");
												sql="update stock_dividends set dividendPerShare="+div+",dividendPlanAbstract='10派" + df1.format(div * 10)
												+ "元' where `code`='" + code+ "' and year=" + report[0] + " and quarter=4 " ;
											StockDAO.runSql(sql);
											}
										}
									}
								} else {
									String stocksCodeStr = "";
									HashSet<Stock> stocktab = StockDAO.getStockCode(1, 1);
									for (Stock s : stocktab) {
										stocksCodeStr += s.getCode() + ",";
									}
									if (stocksCodeStr.contains(code)) {
										logger.info(code+"金融界数据缺失");
										if (div == 0) {
											sql = "insert into stock_dividends(code,name,year,quarter,recordDate,dividendPlan,dividendPlanFlag,dividendPlanAbstract,reportDate,announDate) values('"
													+ code + "',(select name from stock_company where code='" + code
													+ "'),'" + report[0] + "'," + 4 + ",'" + rightregdate + "','"
													+ holdersPublishContent + "'," + 0 + ",'不分配','" + reportDate + "','"
													+ holdersPublishDate + "')";
											StockDAO.runSql(sql);
										} else {
											sql = "insert into stock_dividends(code,name,year,quarter,recordDate,dividendPerShare,dividendPlan,dividendPlanFlag,dividendPlanAbstract,reportDate,announDate) values('"
													+ code + "',(select name from stock_company where code='" + code
													+ "'),'" + report[0] + "'," + 4 + ",'" + rightregdate + "'," + div
													+ ",'" + holdersPublishContent + "'," + 0 + ",'10派" + df1.format(div * 10)
													+ "元','" + reportDate + "','" + holdersPublishDate + "')";
											StockDAO.runSql(sql);
										}
										// 有无送转
										if (increase == 0) {
											sql = "update stock_dividends set dividendPlanIncrease='不送转' , dividendIncrease=0 where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
											StockDAO.runSql(sql);
										} else {
											sql = "update stock_dividends set dividendPlanIncrease='10送转"
													+ increase + "股' , dividendIncrease=" + increase + " where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
											StockDAO.runSql(sql);
										}
									}
								}

								holdersPublishNum++;
								System.out.println("-------------------------------------------");
								System.out.println("第" + (i + 1) + "支");
								System.out.println("状态：\t\t股东大会决议通过");
								System.out.println("股票代码：\t\t" + code);
								System.out.println("分配年度：\t\t" + reportDate);
								System.out.println("股东大会预案说明：\t" + holdersPublishContent);
								System.out.println("股东大会预案公告日期：\t" + holdersPublishDate);
								System.out.println("每股股息：\t\t" + div);
								// System.out.println("sql："+sql);
							}
							// 董事会预案
							else if (!leadersPublishContent.equals("")) {
								div = GatherDividends.parseDividends(leadersPublishContent);
								div = Double.parseDouble(df.format(div));
								increase = GatherDividends.parseIncrease(leadersPublishContent);
								increase = Double.parseDouble(df.format(increase));
								flag = 2;
								String dividendCodeStr = "";
								ArrayList<StockDividends> dividendtab = StockDAO
										.getStockDividendsCode(Integer.parseInt(report[0]), 4);
								for (StockDividends s : dividendtab) {
									dividendCodeStr += s.getCode() + ",";
								} // 判断同期表中是否存在该code ,存在则判断股息是否一致,不一致再做判断
								if (dividendCodeStr.contains(code)) {
									for (StockDividends s1 : dividendtab) {
										if (s1.getCode().equals(code)) {
											if (s1.getRecordDate().equals("") && !rightregdate.equals("")) {
												logger.info(code+"股权登记日出现差异");
												sql = "update stock_dividends set dividendPlanFlag=" + flag
														+ ",recordDate='" + rightregdate + "' where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
												StockDAO.runSql(sql);
											}
											if (s1.getDividendPerShare()!=div) {
												logger.info(code+"分红出现差异");
												sql="update stock_dividends set dividendPerShare="+div+",dividendPlanAbstract='10派" + df1.format(div * 10)
												+ "元' where `code`='" + code+ "' and year=" + report[0] + " and quarter=4 " ;
											    StockDAO.runSql(sql);
											}
										}
									}
								} else {
									String stocksCodeStr = "";
									HashSet<Stock> stocktab = StockDAO.getStockCode(1, 1);
									for (Stock s : stocktab) {
										stocksCodeStr += s.getCode() + ",";
									}
									if (stocksCodeStr.contains(code)) {
										logger.info(code+"金融界数据缺失");
										if (div == 0) {
											sql = "insert into stock_dividends(code,name,year,quarter,recordDate,dividendPlan,dividendPlanFlag,dividendPlanAbstract,reportDate,announDate) values('"
													+ code + "',(select name from stock_company where code='" + code
													+ "'),'" + report[0] + "'," + 4 + ",'" + "" + "','"
													+ leadersPublishContent + "'," + 2 + ",'不分配','" + reportDate + "','"
													+ leadersPublishDate + "')";
											StockDAO.runSql(sql);
										} else {
											sql = "insert into stock_dividends(code,name,year,quarter,recordDate,dividendPerShare,dividendPlan,dividendPlanFlag,dividendPlanAbstract,reportDate,announDate) values('"
													+ code + "',(select name from stock_company where code='" + code
													+ "'),'" + report[0] + "'," + 4 + ",'" + "" + "'," + div + ",'"
													+ leadersPublishContent + "'," + 2 + ",'10派" + df1.format(div * 10)
													+ "元','" + reportDate + "','" + leadersPublishDate + "')";
											StockDAO.runSql(sql);
										}
										// 有无送转
										if (increase == 0) {
											sql = "update stock_dividends set dividendPlanIncrease='不送转',dividendIncrease=0 where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
											StockDAO.runSql(sql);
										} else {
											sql = "update stock_dividends set dividendPlanIncrease='10送转"
													+ increase + "股', dividendIncrease=" + increase + " where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
											StockDAO.runSql(sql);
										}
									}
								}

								leadersPublishNum++;
								System.out.println("-------------------------------------------");
								System.out.println("第" + (i + 1) + "支");
								System.out.println("状态：\t\t董事会决议通过");
								System.out.println("股票代码：\t\t" + code);
								System.out.println("分配年度：\t\t" + reportDate);
								System.out.println("董事会预案说明：\t" + leadersPublishContent);
								System.out.println("董事会预案公告日期：\t" + leadersPublishDate);
								System.out.println("每股股息：\t\t" + div);
								// System.out.println("sql："+sql);
							}
						} else if (report[1].endsWith("06")) {
							// 决案
							if (!executeContent.equals("")) {
								div = GatherDividends.parseDividends(executeContent);
								div = Double.parseDouble(df.format(div));
								increase =GatherDividends.parseIncrease(executeContent);
								increase=Double.parseDouble(df.format(increase));
								flag = 1;
								String dividendCodeStr = "";
								ArrayList<StockDividends> dividendtab = StockDAO
										.getStockDividendsCode(Integer.parseInt(report[0]), 4);
								for (StockDividends s : dividendtab) {
									dividendCodeStr += s.getCode() + ",";
								} // 判断同期表中是否存在该code ,存在则判断股息是否一致,不一致再做判断
								if (dividendCodeStr.contains(code)) {
									for (StockDividends s1 : dividendtab) {
										if (s1.getCode().equals(code)) {
											logger.info(code+"股权登记日出现差异");
											if (s1.getRecordDate().equals("") && !rightregdate.equals("")) {
												sql = "update stock_dividends set dividendPlanFlag=" + flag
														+ ",recordDate='" + rightregdate + "' where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=2 ";
												StockDAO.runSql(sql);
											}
											if (s1.getDividendPerShare()!=div) {
												logger.info(code+"分红出现差异");
												sql="update stock_dividends set dividendPerShare="+div+",dividendPlanAbstract='10派" + df1.format(div * 10)
												+ "元' where `code`='" + code+ "' and year=" + report[0] + " and quarter=4 " ;
										     	StockDAO.runSql(sql);
											}
										}
									}
								} else {
									String stocksCodeStr = "";
									HashSet<Stock> stocktab = StockDAO.getStockCode(1, 1);
									for (Stock s : stocktab) {
										stocksCodeStr += s.getCode() + ",";
									}
									if (stocksCodeStr.contains(code)) {
										logger.info(code+"金融界数据缺失");
										if (div == 0) {
											sql = "insert into stock_dividends(code,name,year,quarter,recordDate,dividendPlan,dividendPlanFlag,dividendPlanAbstract,reportDate,announDate) values('"
													+ code + "',(select name from stock_company where code='" + code
													+ "'),'" + report[0] + "'," + 2 + ",'" + rightregdate + "','"
													+ executeContent + "'," + 1 + ",'不分配','" + reportDate + "','"
													+ holdersPublishDate + "')";
											StockDAO.runSql(sql);
										} else {
											sql = "insert into stock_dividends(code,name,year,quarter,recordDate,dividendPerShare,dividendPlan,dividendPlanFlag,dividendPlanAbstract,reportDate,announDate) values('"
													+ code + "',(select name from stock_company where code='" + code
													+ "'),'" + report[0] + "'," + 2 + ",'" + rightregdate + "'," + div
													+ ",'" + executeContent + "'," + 1 + ",'10派" + df1.format(div * 10)
													+ "元','" + reportDate + "','" + holdersPublishDate + "')";
											StockDAO.runSql(sql);
										}
										// 有无送转
										if (increase == 0) {
											sql = "update stock_dividends set dividendPlanIncrease='不送转', dividendIncrease=0 where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
											StockDAO.runSql(sql);
										} else {
											sql = "update stock_dividends set dividendPlanIncrease='10送转"
													+ increase + "股', dividendIncrease=" + increase + " where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
											StockDAO.runSql(sql);
										}
									}
								}
								executeNum++;
								System.out.println("-------------------------------------------");
								System.out.println("第" + (i + 1) + "支");
								System.out.println("状态：\t\t实施分配");
								System.out.println("股票代码：\t\t" + code);
								System.out.println("分配年度：\t\t" + reportDate);
								System.out.println("实施方案说明：\t" + executeContent);
								System.out.println("除权除息日：\t" + exdividendDate);
								System.out.println("每股股息：\t\t" + div);
								// System.out.println("sql："+sql);

							}
							// 股东大会预案
							else if (!holdersPublishContent.equals("")) {
								div = GatherDividends.parseDividends(holdersPublishContent);
								div = Double.parseDouble(df.format(div));
								increase=GatherDividends.parseDividends(holdersPublishContent);
								increase=Double.parseDouble(df.format(increase));
								flag = 2;
								String dividendCodeStr = "";
								ArrayList<StockDividends> dividendtab = StockDAO
										.getStockDividendsCode(Integer.parseInt(report[0]), 4);
								for (StockDividends s : dividendtab) {
									dividendCodeStr += s.getCode() + ",";
								} // 判断同期表中是否存在该code ,存在则判断股息是否一致,不一致再做判断
								if (dividendCodeStr.contains(code)) {
									for (StockDividends s1 : dividendtab) {
										if (s1.getCode().equals(code)) {
											if (s1.getRecordDate().equals("") && !rightregdate.equals("")) {
												logger.info(code+"股权登记日出现差异");
												sql = "update stock_dividends set dividendPlanFlag=" + flag
														+ ",recordDate='" + rightregdate + "' where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=2 ";
												StockDAO.runSql(sql);
											}
											if (s1.getDividendPerShare()!=div) {
												logger.info(code+"分红出现差异");
												sql="update stock_dividends set dividendPerShare="+div+",dividendPlanAbstract='10派" + df1.format(div * 10)
												+ "元' where `code`='" + code+ "' and year=" + report[0] + " and quarter=4 " ;
											    StockDAO.runSql(sql);
											}
										}
									}
								} else {
									String stocksCodeStr = "";
									HashSet<Stock> stocktab = StockDAO.getStockCode(1, 1);
									for (Stock s : stocktab) {
										stocksCodeStr += s.getCode() + ",";
									}
									if (stocksCodeStr.contains(code)) {
										logger.info(code+"金融界数据缺失");
										if (div == 0) {
											sql = "insert into stock_dividends(code,name,year,quarter,recordDate,dividendPlan,dividendPlanFlag,dividendPlanAbstract,reportDate,announDate) values('"
													+ code + "',(select name from stock_company where code='" + code
													+ "'),'" + report[0] + "'," + 2 + ",'" + rightregdate + "','"
													+ holdersPublishContent + "'," + 0 + ",'不分配','" + reportDate + "','"
													+ holdersPublishDate + "')";
											StockDAO.runSql(sql);
										} else {
											sql = "insert into stock_dividends(code,name,year,quarter,recordDate,dividendPerShare,dividendPlan,dividendPlanFlag,dividendPlanAbstract,reportDate,announDate) values('"
													+ code + "',(select name from stock_company where code='" + code
													+ "'),'" + report[0] + "'," + 2 + ",'" + rightregdate + "'," + div
													+ ",'" + holdersPublishContent + "'," + 0 + ",'10派" + df1.format(div * 10)
													+ "元','" + reportDate + "','" + holdersPublishDate + "')";
											StockDAO.runSql(sql);
										}
										// 有无送转
										if (increase == 0) {
											sql = "update stock_dividends set dividendPlanIncrease='不送转', dividendIncrease=0 where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
											StockDAO.runSql(sql);
										} else {
											sql = "update stock_dividends set dividendPlanIncrease='10送转"
													+ increase + "股', dividendIncrease=" + increase + " where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
											StockDAO.runSql(sql);
										}
									}
								}
								holdersPublishNum++;
								System.out.println("-------------------------------------------");
								System.out.println("第" + (i + 1) + "支");
								System.out.println("状态：\t\t股东大会决议通过");
								System.out.println("股票代码：\t\t" + code);
								System.out.println("分配年度：\t\t" + reportDate);
								System.out.println("股东大会预案说明：\t" + holdersPublishContent);
								System.out.println("股东大会预案公告日期：\t" + holdersPublishDate);
								System.out.println("每股股息：\t\t" + div);
								// System.out.println("sql："+sql);
							}
							// 董事会预案
							else if (!leadersPublishContent.equals("")) {
								div = GatherDividends.parseDividends(leadersPublishContent);
								div = Double.parseDouble(df.format(div));
								increase=GatherDividends.parseDividends(leadersPublishContent);
								increase=Double.parseDouble(df.format(increase));
								flag = 2;
								String dividendCodeStr = "";
								ArrayList<StockDividends> dividendtab = StockDAO
										.getStockDividendsCode(Integer.parseInt(report[0]), 4);
								for (StockDividends s : dividendtab) {
									dividendCodeStr += s.getCode() + ",";
								} // 判断同期表中是否存在该code ,存在则判断股息是否一致,不一致再做判断
								if (dividendCodeStr.contains(code)) {
									for (StockDividends s1 : dividendtab) {
										if (s1.getCode().equals(code)) {
											if (s1.getRecordDate().equals("") && !rightregdate.equals("")) {
												logger.info(code+"股权登记日出现差异");
												sql = "update stock_dividends set dividendPlanFlag=" + flag
														+ ",recordDate='" + rightregdate + "' where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=2 ";
												StockDAO.runSql(sql);
											}
											if (s1.getDividendPerShare()!=div) {
												logger.info(code+"分红出现差异");
												sql="update stock_dividends set dividendPerShare="+div+",dividendPlanAbstract='10派" + df1.format(div * 10)
												+ "元' where `code`='" + code+ "' and year=" + report[0] + " and quarter=4 " ;
											    StockDAO.runSql(sql);
											}
										}
									}
								} else {
									String stocksCodeStr = "";
									HashSet<Stock> stocktab = StockDAO.getStockCode(1, 1);
									for (Stock s : stocktab) {
										stocksCodeStr += s.getCode() + ",";
									}
									if (stocksCodeStr.contains(code)) {
										logger.info(code+"金融界数据缺失");
										if (div == 0) {
											sql = "insert into stock_dividends(code,name,year,quarter,recordDate,dividendPlan,dividendPlanFlag,dividendPlanAbstract,reportDate,announDate) values('"
													+ code + "',(select name from stock_company where code='" + code
													+ "'),'" + report[0] + "'," + 2 + ",'" + "" + "','"
													+ leadersPublishContent + "'," + 2 + ",'不分配','" + reportDate + "','"
													+ leadersPublishDate + "')";
											StockDAO.runSql(sql);
										} else {
											sql = "insert into stock_dividends(code,name,year,quarter,recordDate,dividendPerShare,dividendPlan,dividendPlanFlag,dividendPlanAbstract,reportDate,announDate) values('"
													+ code + "',(select name from stock_company where code='" + code
													+ "'),'" + report[0] + "'," + 2 + ",'" + "" + "'," + div + ",'"
													+ leadersPublishContent + "'," + 2 + ",'10派" + df1.format(div * 10)
													+ "元','" + reportDate + "','" + leadersPublishDate + "')";
											StockDAO.runSql(sql);
										}
										// 有无送转
										if (increase == 0) {
											sql = "update stock_dividends set dividendPlanIncrease='不送转' , dividendIncrease=0 where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
											StockDAO.runSql(sql);
										} else {
											sql = "update stock_dividends set dividendPlanIncrease='10送转"
													+ increase + "股' , dividendIncrease=" + increase + " where `code`='" + code
														+ "' and year=" + report[0] + " and quarter=4 ";
											StockDAO.runSql(sql);
										}
									}
								}
								leadersPublishNum++;
								System.out.println("-------------------------------------------");
								System.out.println("第" + (i + 1) + "支");
								System.out.println("状态：\t\t董事会决议通过");
								System.out.println("股票代码：\t\t" + code);
								System.out.println("分配年度：\t\t" + reportDate);
								System.out.println("董事会预案说明：\t" + leadersPublishContent);
								System.out.println("董事会预案公告日期：\t" + leadersPublishDate);
								System.out.println("每股股息：\t\t" + div);
								// System.out.println("sql："+sql);
							}
						}
					}
					System.out.println("-----------------------");
					System.out.println("日期\t" + date);
					System.out.println("发布信息\t共 " + i + "条");
					System.out.println("其中\t实施方案 " + executeNum + "条");
					System.out.println("\t股东大会公告预案 " + holdersPublishNum + "条");
					System.out.println("\t董事会公告预案 " + leadersPublishNum + "条");
					System.out.println("-----------------------");
					System.out.println();
					System.out.println();
					System.out.println();
				} else {
					System.out.println("-----------------------");
					System.out.println("日期\t" + date);
					System.out.println("无信息发布");
					System.out.println("-----------------------");
					System.out.println();
					System.out.println();
					System.out.println();
				}

			}

		} catch (Exception ex) {
			logger.error(ex.getStackTrace());
		}

	}

	// 派息
	public static double parseDividends(String content) {
		double value = 0;
		if (content.indexOf("派") >= 0) {
			String[] s1 = content.split("派");
			String[] s2 = s1[1].split("元");
			if (s2.length > 0) {
				value = Double.parseDouble(s2[0]) / 10;
			}
		}
		return value;
	}

	// 送转
	public static double parseIncrease(String content) {
		double value = 0;
		if (content.indexOf("转") >= 0) {
			String[] s1 = content.split("转");
			String s2 = s1[1].substring(0, 3);
			if (!s2.equals("")) {
				value = Double.parseDouble(s2);
			}
		}
		if (content.indexOf("送") >= 0) {
			String[] s1 = content.split("送");
			String s2 = s1[1].substring(0, 3);
			if (!s2.equals("")) {
				value += Double.parseDouble(s2);
			}
		}
		return value;
	}
}
