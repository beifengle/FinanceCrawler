package com.yysoft.mgr;

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
import org.jsoup.select.Elements;

import tool.CrawlerNewFinance;

import com.yysoft.entity.Finance;
import com.yysoft.util.ConfigParser;
import com.yysoft.util.StringUtils;

public class CrawlerNewsflash {
	private static Logger logger = LogManager.getLogger(CrawlerNewFinance.class); //
	private static Logger errorlogger = LogManager.getLogger("fcErrorLoger"); //
	private static Logger htmllogger = LogManager.getLogger("fcHtmlLoger"); //

	public static void main(String[] args) {
		new ConfigParser();
		//down("sz000002","科顺股份","2017-09-30");
		String content=downPage("http://stock.jrj.com.cn/hotstock/2018/02/05080024060374.shtml");
		System.out.println(content);
	}
	
	public static String downPage(String url) {
		String responseContent = null;
		
		
		logger.info("采集地址：" + url);
		@SuppressWarnings("resource")
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		String content=null;

		try {
			get = new HttpGet(url);
			response = httpClient.execute(get);
			entity = response.getEntity(); // 获取响应实体
			if (entity != null) {
				responseContent = EntityUtils.toString(entity, "gbk");
				//test
				if (responseContent.contains("页面没有找到")) {
					logger.info("页面返回'页面没有找到'！url=" + url);
					return null;
				}
				content = parser(responseContent);
			} else {
				errorlogger.error("URL返回的内容为null：" + url);
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	public static  String parser(String responseContent) {
		Finance financeTemp = new Finance();
		Document doc = Jsoup.parse(responseContent);
		Elements elements1 = doc.select("[name=description]");
   	 	String rgex = "【公告简述】(.*?)【相关公告】"; 
		return StringUtils.getSubUtilSimple(elements1.toString(), rgex);
	}
	

}
