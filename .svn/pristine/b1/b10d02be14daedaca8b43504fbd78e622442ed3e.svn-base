package com.yysoft.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.yysoft.dao.StockDAO;
import com.yysoft.entity.ReportNotice;
import com.yysoft.util.ConfigParser;
import com.yysoft.util.Constant;
import com.yysoft.util.DateUtils;
import com.yysoft.util.StringUtils;

public class HttpUtils {
	private static Logger logger = LogManager.getLogger(StockDAO.class); //
	private static Logger errorlogger = LogManager.getLogger("fcErrorLoger"); //

	
	public static final String doGet(String url) {
		String responseContent = null;
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse response = null;
			HttpEntity entity = null; //

			HttpGet get = new HttpGet(url);

			entity = httpClient.execute(get).getEntity(); //
			if (entity != null) {
				responseContent = EntityUtils.toString(entity, "gbk");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return responseContent;
	}

	/**
	 * 返回金融街指定页面所有年报季报数据 Description:
	 * 
	 * @return
	 */
	public static HashSet<ReportNotice> loadJRJNoticeInfo() {
		String encode = "UTF-8";
		String pageContent = doGet(
				"http://stock.jrj.com.cn/action/getNoticeListByDiffCondition.jspa?vname=_notic_list&type=1&page=1&psize=3000");

		JSONObject json = null;
		String lines[] = null;
		ReportNotice rn = null;
		HashSet<ReportNotice> rns = new HashSet<ReportNotice>();
		HashMap<String, ReportNotice> maps = new HashMap<String, ReportNotice>();
		pageContent = pageContent.replace("var _notic_list=", "");
		if (pageContent.length() < 220) {
			logger.info("当前列表页返回内容为空：" + pageContent);
		}
		try {
			json = new JSONObject(pageContent);
			pageContent = json.getString("data").replaceAll("\\[\\[", "").replaceAll("\\]\\]", "");
			lines = pageContent.replaceAll("\"", "").split("\\],\\[");
			logger.info("页面股票数量：" + lines.length);
			String lines2[] = null;
			String tempYear = "";
			String code = "";
			// 倒序遍历，可以把旧的相同股票替代为最新的季报
			for (int j = lines.length - 1; j >= 0; j--) {
				
				System.out.println("a--:" + lines[j]);
				System.out.println("a--:" + lines[j]);
				lines2 = lines[j].split(",");
				rn = new ReportNotice();
				rn.setPublishDate(lines2[1]);
				rn.setPdfURL(lines2[5]);
				rn.setSource(0);//标记来源：金融街
				if (DateUtils.compareDate(rn.getPublishDate()) == false) {
					continue;
				}
				rn.setTitle(lines2[2]);
				rn.setReType(lines2[6]);

				rn.setName(lines2[4]);
				tempYear = StringUtils.getStrByReg("：(.*?)年", lines2[2]);
				if(tempYear==null){
					continue;
				}
				tempYear = tempYear.replace("关于", "").trim().replace("截至", "");
				code = lines2[3];
				if (code.startsWith("60")) {
					rn.setCode("sh" + code);
				} else {
					rn.setCode("sz" + code);
				}
				// 通过配置文件中预置的过滤词，过滤非正式财报
				if (!isUseful(rn.getTitle(), rn.getPublishDate())) {
					logger.info("已过滤非正式财报");
					// OutInfos.OutAllFieldsDemo(rn);//打印当前对象
					continue;
				}
				System.out.println("b--");
				if (StringUtils.isNotBlank(tempYear) && StringUtils.isNumeric(tempYear)) {
						rn.setYear(Integer.valueOf(tempYear));
				} else {
					if(tempYear.contains("二零一四")){
						rn.setYear(2014);
					}else if(tempYear.contains("二零一五")){
						rn.setYear(2015);
					}else if(tempYear.contains("二零一六")){
						rn.setYear(2016);
					}else if(tempYear.contains("二零一七")){
						rn.setYear(2017);
					}else if(tempYear.contains("二零一八")){
						rn.setYear(2018);
					}else if(tempYear.contains("二零一九")){
						rn.setYear(2019);
					}else if(tempYear.contains("二零二零")){
						rn.setYear(2020);
					}else if(tempYear.contains("二零二一")){
						rn.setYear(2020);
					}else{
						rn.setYear(0);
					}
				}
				rn.setQuarter(getQuarter(lines2[2]));
				rn.setSource(0);
				// OutInfos.OutAllFieldsDemo(rn);//打印当前对象

				maps.put(rn.getYear() + rn.getCode(), rn);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Iterator iter = maps.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, ReportNotice> entry = (Map.Entry<String, ReportNotice>) iter.next();
			ReportNotice val = entry.getValue();
			rns.add(val);
		}
		return rns;
	}

	/**
	 * Description:返回季度序号
	 * @param title
	 * @return
	 */
	public static int getQuarter(String title) {
		int q = 0;
		if (title.contains("一季") || title.contains("1季")) {
			q = 1;
		} else if (title.contains("二季") || title.contains("半年") || title.contains("2季")) {
			q = 2;
		} else if (title.contains("三季") || title.contains("3季")) {
			q = 3;
		} else if (title.contains("四季") || title.contains("年度")|| title.contains("4季")|| title.contains("年报")) {
			q = 4;
		}else{
			errorlogger.info("季度编号无法转换，title：" + title);
		}
		return q;
	}

	
	/**
	 * Description:返回季度序号
	 * 
	 * @param reportadate
	 * @return
	 */
	public static int getQuarterFromReport(String reportadate) {
		int q = 0;
		if (reportadate.contains("03-31")) {
			q = 1;
		} else if (reportadate.contains("06-30")) {
			q = 2;
		} else if (reportadate.contains("09-30")) {
			q = 3;
		} else if (reportadate.contains("12-31")  ) {
			q = 4;
		}
		return q;
	}
	/**
	 * Description:通过公告标题判断财报是否正式、有效
	 * 
	 * @param title
	 * @return
	 */
	public static boolean isUseful(String title, String date) {
		for (String word : Constant.wordsList) {
			if (title.contains(word)) {// 标题包含要过滤的词，返回假
				logger.info(title + " (命中过滤词：" + word + ")");
				return false;
			}
		}
		logger.info(title + " (命中过滤词：无)-" + date);
		return true;
	}

	
	
	/**
	 * 返回东方财富网指定页面所有年报季报数据 Description:
	 * http://data.eastmoney.com/bbsj/201512/yjbb.html
	 * http://datainterface.eastmoney.com/EM_DataCenter/JS.aspx?type=SR&sty=YJBB&fd=2015-12-31&st=13&sr=-1&p=1&ps=800
	 * @return
	 */
	public static HashSet<ReportNotice> loadEastmoneyNoticeInfo(String reportDate) {
		String encode = "UTF-8";
		String pageContent = doGet(
				"http://www.eastmoney.com/");
		  pageContent = doGet(
				"http://datainterface.eastmoney.com/EM_DataCenter/JS.aspx?type=SR&sty=YJBB&fd=" + reportDate + "&st=13&sr=-1&p=1&ps=1000");
		System.out.println(pageContent); 
		ReportNotice rn = null;
		HashSet<ReportNotice> rns = new HashSet<ReportNotice>();
		HashMap<String, ReportNotice> maps = new HashMap<String, ReportNotice>();
		pageContent = pageContent.replace("\"])", "").replace("([\"", "");
		if (pageContent.contains("stats:false")) {
			logger.info("当前列表页返回内容为空：" + pageContent);
			return rns;
		}
		
			String[] response = pageContent.split("\",\"");
			String[] response2 = null;//具体的每个股票的发布信息
			logger.info("页面股票数量：" + response.length);
			String tempYear = "";
			String code = "";
			// 倒序遍历，可以把旧的相同股票替代为最新的季报
			for (int j = response.length - 1; j >= 0; j--) {
				response2 = response[j].split(",");
				rn = new ReportNotice();
				rn.setPublishDate(response2[16]);

				if (DateUtils.compareDate(rn.getPublishDate()) == false) {
					continue;
				}

				rn.setName(response2[1]);
				tempYear = response2[17].substring(0, 4);
				code = response2[0];
				if (code.startsWith("60")) {
					rn.setCode("sh" + code);
				} else {
					rn.setCode("sz" + code);
				}
				rn.setReType("年度报告");
				rn.setYear(Integer.valueOf(tempYear));
				rn.setQuarter(getQuarterFromReport(response2[17]));
				rn.setSource(1);
				// OutInfos.OutAllFieldsDemo(rn);//打印当前对象

				maps.put(rn.getYear() + rn.getCode(), rn);
			}
		Iterator iter = maps.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, ReportNotice> entry = (Map.Entry<String, ReportNotice>) iter.next();
			ReportNotice val = entry.getValue();
			rns.add(val);
		}
		return rns;
	}

	public static void main(String[] args) {
		new ConfigParser();
		HashSet<ReportNotice> rns=loadJRJNoticeInfo();
		System.out.println(rns.size());
//      historyData("sz300099", "尤洛卡", 0, 2015, 0, 3);
		// loadNoticeInfo();
//		getLastYearJRJ();
//		gatherAcronym("sz300099");
		//loadEastmoneyNoticeInfo("2016-06-30");
	}

}