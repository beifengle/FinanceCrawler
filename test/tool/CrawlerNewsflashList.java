package tool;

import java.io.IOException;
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

import com.yysoft.dao.StockDAO;
import com.yysoft.entity.Finance;
import com.yysoft.entity.Stock;
import com.yysoft.entity.Newsflash;
import com.yysoft.util.ConfigParser;
import com.yysoft.util.StringUtils;

public class CrawlerNewsflashList {
	private static Logger logger = LogManager.getLogger(CrawlerNewFinance.class); //
	private static Logger errorlogger = LogManager.getLogger("fcErrorLoger"); //
	private static Logger htmllogger = LogManager.getLogger("fcHtmlLoger"); //

	
	public static void main(String[] args) {
		new ConfigParser();
		//down("sz000002","科顺股份","2017-09-30");
		downPage("2017-12-31");
		System.out.println("end!");
	}
	
	public static String downPage(String reportDate) {
		String financeTemp = null;//临时对象
		String responseContent = null;
		String urlLoad = "";// 要采集的URL
		urlLoad = "http://stock.jrj.com.cn/hotstock/htggdp.shtml" ;// 资产负债表页面链接
		
		
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
				parser(responseContent);
			} else {
				errorlogger.error("URL返回的内容为null：" + urlLoad);
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static  String parser(String responseContent) {
		Finance financeTemp = new Finance();
		Document doc = Jsoup.parse(responseContent);
		//Elements elements1 = doc.select("[class=t1]");
		Newsflash newsflash = new Newsflash();
		
		String Table = " stockwinweb.stock_web_newsflash";
		String sql="insert into "+Table +"(Title,Contents,Code,Name,PublishDate,Publish) values ";

		//select Title,Contents,Code,Name,PublishDate,Publish from stockwinweb.stock_web_newsflash
		
		ArrayList<Stock> stocksTab = StockDAO.getStockCodes();
		ArrayList<Newsflash> stocksNews = StockDAO.getLast50Newsflash();
		
		
		ArrayList<Newsflash> resultNewsflash = new ArrayList<Newsflash>();
		logger.info(stocksNews.size());

		
		Element elements1 = doc.select("[class=list-main]").get(0);
		//Element elements2 = doc.select("tbody").get(0);
		Elements trs = elements1.getElementsByTag("li");// 获取所有子tr标签
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
			
			if(tr.getElementsByTag("span").text().length()>10){
			String Newsflashdate=tr.getElementsByTag("span").text().substring(0,10);
			String NewsflashTitle = tr.getElementsByTag("a").text();
			String[] Titles = StringUtils.split(NewsflashTitle, "：");
			
			isExits= "0";
			if(Titles.length==2){
				
				newsflash = new Newsflash();
				newsflash.setPublishdate(Newsflashdate);
				newsflash.setName(Titles[0]);
				newsflash.setTitle(Titles[1]);
				

				for(Stock s:stocksTab){
					if(s.getName().equals(Titles[0])){
						newsflash.setCode(s.getCode());
						//logger.info(s.getCode());
						break;
					}
				}
				for(Newsflash news:stocksNews){

					if(news.getName().equals(Titles[0]) && news.getTitle().equals(Titles[1]) ){
						isExits= "1";
						break;
					}
				}
				if(isExits=="1"){
					logger.info("extis");
					continue; 
				} else {
		   	 	String rgex = "http://(.*?).shtml"; 
				String url= StringUtils.getSubUtilSimple(tr.getElementsByTag("a").outerHtml(), rgex);
				url ="http://"+url+".shtml";
				
				String NewsflashContent = CrawlerNewsflash.downPage(url);
				newsflash.setContents(NewsflashContent);	
				resultNewsflash.add(newsflash);
				
				String values = "('"+newsflash.getTitle()+"','"+newsflash.getContents()+"','"+newsflash.getCode()+"','"+newsflash.getName()+"','"+newsflash.getPublishdate()+"','0')";
		 	    
		 	    
		  	   String ExcelSql = sql+values;
			  //System.out.println("ExcelSql：" + ExcelSql);

		  	   StockDAO.runSql(ExcelSql);
				}

				
			}
			
			}
			
//			logger.info(code +"-财报日期(" + reportDate + ")-第 " + (count++) + " 个tr。");
			/*
			Elements tds = tr.getElementsByTag("td");// 获取所有子td标签
			int column=0;//列号
			for (Element td : tds) {
				String tdtext = td.text().replaceAll("--", "").trim().replaceAll(" ", "").replaceAll(",", "");
				data[line][column] = tdtext;
				logger.info(tdtext);
				column++;
			}
			*/
			line++;
		}
		//信息提取
		//StockDAO.insertNewsflashDB(resultNewsflash);


		return null;
	}

	

}
