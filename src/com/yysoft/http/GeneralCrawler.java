package com.yysoft.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.util.Constant;

/**
 * 功能描述：返回所采集页面内容
 * 
 * @author Huanyan.Lu
 * @date:2015年8月25日
 * @time:上午10:11:54
 * @version:1.0
 */
public class GeneralCrawler {

	private static Logger logger = LogManager.getLogger(GeneralCrawler.class); // 记录日志

	/**
	 * 
	 * Description:返回指定URL页面内容的字符串
	 * 
	 * @param url
	 * @param encode
	 *            指定读取页面的编码方式 gbk或utf8
	 * @return
	 */
	public static String getPageString(String url, String encode) {

		logger.debug("url:" + url);
		// 创建HttpClient实例
		HttpClient httpClient = new HttpClient();
		// 设置编码参数
		if (encode != null) {
			httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encode);

		} else {
			encode = Constant.ENCODEGBK;
			httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encode);
		}
		// 忽略Cookies
		httpClient.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);

		// 创建GetMethod实例访问指定URL
		GetMethod getMethod = new GetMethod(url);
		try {
			// 访问指定URL并取得返回状态码
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode == 200) {// 返回成功状态码200
				// 读取页面HTML源码
				StringBuffer sb = new StringBuffer();
				InputStream in = getMethod.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, encode));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
					// logger.debug("返回的页面内容：" + line);
				}
				if (br != null)
					br.close();
				logger.debug(sb.toString());
				return sb.toString();

			} else {
				return null;
			}
		} catch (Exception ex) {
			logger.error(ex.getStackTrace());
			return null;
		}
	}

	/**
	 * 
	 * Description:返回指定URL页面内容的缓存字符流
	 * 
	 * @param url
	 * @param encode
	 *            指定读取页面的编码方式 gbk或utf8
	 * @return
	 */
	public static BufferedReader getPageBufferedReader(String url, String encode) {
//		logger.info("url:" + url);

		// 创建HttpClient实例
		HttpClient httpClient = new HttpClient();
		// 设置编码参数
		if (encode != null) {
			httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encode);

		} else {
			encode = "gbk";
			httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encode);
		}
		// 忽略Cookies
		httpClient.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);

		// 创建GetMethod实例访问指定URL
		GetMethod getMethod = new GetMethod(url);
		try {
			// 访问指定URL并取得返回状态码
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode == 200) {// 返回成功状态码200
				// 读取页面HTML源码
				InputStream in = getMethod.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, encode));

				return br;
			} else {
				return null;
			}
		} catch (Exception ex) {
			logger.error(ex.getStackTrace());
			return null;
		}
	}

	public static void main(String[] args) throws IOException {
		GeneralCrawler.getPageBufferedReader(" http://vip.stock.finance.sina.com.cn/q/go.php/vInvestConsult/kind/jyts/index.phtml?enctype=%B3%FD%C8%A8%B3%FD%CF%A2%C8%D5&tradedate=2015-10-14&symbol=%D6%A4%C8%AF%BC%F2%B3%C6%BB%F2%B4%FA%C2%EB",
				Constant.ENCODEGBK);
	}
}
