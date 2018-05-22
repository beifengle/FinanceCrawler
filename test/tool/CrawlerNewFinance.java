package tool;

import java.io.IOException;

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

import com.yysoft.entity.Finance;
import com.yysoft.extract.JRJLRExtracter;
import com.yysoft.extract.JRJXJExtracter;
import com.yysoft.extract.JRJZCExtracter;
import com.yysoft.http.JRJHttpUtils;
import com.yysoft.http.QQHttpUtils;
import com.yysoft.util.ConfigParser;
import com.yysoft.util.OutInfos;
import com.yysoft.util.StringUtils;
import com.yysoft.xmlparser.JRJPageParser;

public class CrawlerNewFinance {

	private static Logger logger = LogManager.getLogger(CrawlerNewFinance.class); //
	private static Logger errorlogger = LogManager.getLogger("fcErrorLoger"); //
	private static Logger htmllogger = LogManager.getLogger("fcHtmlLoger"); //

	public static void main(String[] args) {
		new ConfigParser();
		//down("sz000002","科顺股份","2017-09-30");
		downPage("2017-12-31");
		System.out.println("end!");
	}
	
	public static Finance downPage(String reportDate) {
		Finance financeTemp = null;//临时对象
		String responseContent = null;
		String urlLoad = "";// 要采集的URL
		String gatherYear = reportDate.substring(0, 7).replace("-", "");// 要采集的年份
		urlLoad = "http://data.eastmoney.com/soft_new/bbsj/"+gatherYear+"/yysj/sjsj/1.html" ;// 资产负债表页面链接
		
		
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
	
	public static  Finance parser(String responseContent) {
		Finance financeTemp = new Finance();
		Document doc = Jsoup.parse(responseContent);
		//Elements elements1 = doc.select("[class=t1]");
		
		Elements elements1 = doc.select("[id=cont-yjyg1]");
		Element elements2 = doc.select("tbody").get(0);
		Elements trs = elements2.getElementsByTag("tr");// 获取所有子tr标签
		logger.info("子tr标签数量：" + trs.size());//2015.12.29日，有68标准行。
		if(trs.size()<=0){
			errorlogger.error( ",tr标签数量小于0");
			return null;
		}
		logger.info(trs.size());
        
		int trRows=trs.size();
		String data[][] = new String[trs.size()][9];//二维数组，存放提取数据项
		int line=0;//行号
		int count=1;
		for (Element tr : trs) {
//			logger.info(code +"-财报日期(" + reportDate + ")-第 " + (count++) + " 个tr。");
			Elements tds = tr.getElementsByTag("td");// 获取所有子td标签
			int column=0;//列号
			for (Element td : tds) {
				String tdtext = td.text().replaceAll("--", "").trim().replaceAll(" ", "").replaceAll(",", "");
				data[line][column] = tdtext;
				//logger.info(tdtext);
				column++;
			}
			line++;
		}
		for(int i=0;i<trRows;i++){
			String code=data[i][0];

			if(code.substring(0, 1).equals("6")){
				code="sh"+code;
			} else {
				code="sz"+code;
			}
			String stockname = data[i][1];
			String reportdate=data[i][6];
			if(reportdate.equals("-")){
				break;
			}
			if(Integer.parseInt(reportdate.replace("-", ""))>20180212){
				CrawlerFinance.execFinance(code, stockname);
			}
		}
		//信息提取

		return null;
	}
}
