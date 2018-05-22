package com.yysoft.mgr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tool.CrawlerNewFinance;
import tool.CrawlerNewsflash;

import com.yysoft.dao.StockDAO;
import com.yysoft.entity.Finance;
import com.yysoft.entity.Newsflash;
import com.yysoft.entity.Stock;
import com.yysoft.http.AcronymHttpUtils;
import com.yysoft.util.ConfigParser;
import com.yysoft.util.StringUtils;

public class CrawlerEbitda {

	private static Logger logger = LogManager.getLogger(CrawlerNewFinance.class); //
	private static Logger errorlogger = LogManager.getLogger("fcErrorLoger"); //
	private static Logger htmllogger = LogManager.getLogger("fcHtmlLoger"); //
	static DecimalFormat df6 = new DecimalFormat("######0.0000"); // 保留四位小数

	
	public static void main(String[] args) {
		new ConfigParser();
		//down("sz000002","科顺股份","2017-09-30");
		//downPage("sz000028");
		CralerByCode();
		/*
		System.out.println("end!");
		ArrayList<Stock> stocksTab = StockDAO.getStockCodes();
		for(Stock s:stocksTab){
			logger.info(s.getCode());

			//response = AcronymHttpUtils.gatherAcronym(s.getCode());
			if(!s.getCode().equals("sh600725")){
				downPage(s.getCode());
			}
		}
		*/
	}
	
	public static String downPage(String code) {
		String financeTemp = null;//临时对象
		String responseContent = null;
		String urlLoad = "";// 要采集的URL
		String shortcode=code.replace("sz","").replace("sh","");
		urlLoad = "http://stock.jrj.com.cn/share,"+shortcode+",lrfpbzy.shtml" ;// 
		//urlLoad = "http://stock.jrj.com.cn/hotstock/htggdp-2.shtml" ;// 
		
		logger.info("采集地址：" + urlLoad);
		@SuppressWarnings("resource")
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = null;
		HttpResponse response = null;
		HttpEntity entity = null;

		try {
			get = new HttpGet(urlLoad);
			response = httpClient.execute(get);
			entity = response.getEntity(); // 获取响应实体
			if (entity != null) {
				responseContent = EntityUtils.toString(entity, "gbk");
				//test
				if (responseContent.contains("页面没有找到")) {
					logger.info("页面返回'页面没有找到'！url=" + urlLoad);
					return null;
				}
				parser(responseContent,code);
			} else {
				errorlogger.error("URL返回的内容为null：" + urlLoad);
				return null;
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return null;
	}
	
	public static  String parser(String responseContent,String Code) {
		Finance financeTemp = new Finance();
		Document doc = Jsoup.parse(responseContent);
		//Elements elements1 = doc.select("[class=t1]");
		Newsflash newsflash = new Newsflash();
		
		String Table = " stockwinweb.stock_web_newsflash";
		String sql="insert into "+Table +"(Title,Contents,Code,Name,PublishDate,Publish) values ";

		//select Title,Contents,Code,Name,PublishDate,Publish from stockwinweb.stock_web_newsflash
		
		
		Elements elements1 = doc.select("[class=tabs2]");
		
		Element elements2 = elements1.select("tbody").get(0);
		Elements trs = elements2.getElementsByTag("tr");// 获取所有子tr标签

		
		
		if(trs.size()<=0){
			errorlogger.error( ",tr标签数量小于0");
			return null;
		}
		logger.info(trs.size());
		String isExits= "0";

		String data[][] = new String[trs.size()][9];//二维数组，存放提取数据项
		int line=0;//行号
		int count=1;

		for (Element tr : trs) {
//			logger.info(code +"-财报日期(" + reportDate + ")-第 " + (count++) + " 个tr。");
			Elements tds = tr.getElementsByTag("td");// 获取所有子td标签
			int column=0;//列号
			for (Element td : tds) {
				String tdtext = td.text().replaceAll("--", "").trim().replaceAll(" ", "").replaceAll(",", "");
				if(column<9){
					data[line][column] = tdtext;
					if (StringUtils.isNotEmpty(data[line][column])) {
						data[line][column] = data[line][column];
					}
				}
				//logger.info(tdtext);
				column++;
			}
			line++;
		}
		if(trs.size()==10){
			
			String EbitdaNum;
			Double dbnul;

			String ebitda=data[9][0];
			if(ebitda.length()> 0){
				dbnul = Double.parseDouble(ebitda)*100000000;
				EbitdaNum = df6.format(dbnul);
				ExecSql(Code,EbitdaNum,data[0][0]);
				

			}
            
			if(data[9][2]!=null){
			ebitda=data[9][2];
			if(ebitda.length()> 0){
				dbnul = Double.parseDouble(ebitda)*100000000;
				EbitdaNum = df6.format(dbnul);
				ExecSql(Code,EbitdaNum,data[0][2]);

			}
			}


			if(data[9][4]!=null){
			ebitda=data[9][4];
			if(ebitda.length()> 0){
				dbnul = Double.parseDouble(ebitda)*100000000;
				EbitdaNum = df6.format(dbnul);
				ExecSql(Code,EbitdaNum,data[0][4]);

			}
			}


			if(data[9][6]!=null){

			ebitda=data[9][6];
			if(ebitda.length()> 0){
				dbnul = Double.parseDouble(ebitda)*100000000;
				EbitdaNum = df6.format(dbnul);
				ExecSql(Code,EbitdaNum,data[0][6]);
				

			}
			}

			
		}


		return null;
	}
	public static void ExecSql(String code,String Ebitda,String reportdate) {
		String SqlExec=" update stock_financial set EBITDA='"+Ebitda+"' where code='"+code+"' and reportdate='"+reportdate+"'";
		StockDAO.runSql(SqlExec);
		
	}

	public static void CralerByCode(){
		File file=new File("D://s.txt");
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
			String line=null;
	        while((line=br.readLine())!=null){
	        	
	        	downPage(line);
	        	
	        }
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	

}
