package com.yysoft.mgr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.istack.internal.logging.Logger;
import com.yysoft.dao.StockDAO;
import com.yysoft.entity.Stock;
import com.yysoft.util.SSLClient;

import javafx.print.JobSettings;

public class CrawlerNewStockUpdate {
	/**
	 *从雪球网获取个股简介 
	 * @throws Exception 
	 * 
	 */
	public static void getstock(int page, int size){
		HashMap<String, String> urlMap = new HashMap<>();
		ArrayList<Stock> arrayList = StockDAO.getStockCodes();
		for(Stock stock : arrayList){
			urlMap.put(getCrawlerURL(stock.getCode(), page, size), stock.getCode());
		}

		try {
			getXueqiuPage(urlMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static String getCrawlerURL(String code,int page,int size){
		String url = null;
		if(code!=null && !code.equals("")){
			return "https://xueqiu.com/stock/f10/compinfo.json?symbol="
		+code+"&page=" +page+"&size="+size;
		}
		return url;
		
	}
	
	public static void getXueqiuPage(HashMap<String,String> urlsMap) throws Exception {
		String responseContent = null;
		HttpClient httpClient =new SSLClient();
		HashMap<String, String> errorMap = new HashMap<>();
		int count=0;
		try {
			HttpGet get = new HttpGet("http://xueqiu.com/");
			System.out.println(get);
			HttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity(); // 获取响应实体
			if (entity != null) {
				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
			Iterator<Entry<String, String>> iter = urlsMap.entrySet().iterator();
	
			JSONObject json = null;
			while (iter.hasNext()) {
				count++;
				Map.Entry<String, String> entry = iter.next(); 
				String key = entry.getKey();//url
//				String key = "https://xueqiu.com/stock/f10/compinfo.json?symbol=sz000667&page=1&size=4";
				String val = entry.getValue();//code
				get = new HttpGet(key);
				response = httpClient.execute(get);
				entity = response.getEntity(); // 获取响应实体
				System.out.println("response:--"+response);
				Stock stock = null;
				String mainbusiness = null;
				String businessScope = null;
				JSONObject jsonObj = null;
				if (entity != null) {
					responseContent = EntityUtils.toString(entity, "UTF-8");
				    try {
				    	json = new JSONObject(responseContent);
				    	stock = new Stock();
				    	System.out.println(key);
				    	jsonObj = json.getJSONObject("tqCompInfo");
				    	System.out.println(jsonObj);
				    	mainbusiness = jsonObj.getString("majorbiz");
				    	if(mainbusiness.indexOf("'") != -1){
				    		mainbusiness = mainbusiness.replace("'", "''");
				    	}
				    	stock.setMainBusiness(jsonObj.getString("majorbiz"));
				    	businessScope = jsonObj.getString("bizscope");
				    	System.out.println("转义前"+businessScope);
				    	if(businessScope.indexOf("'") != -1){
				    		businessScope = businessScope.replace("'", "''");
				    	}
				    	stock.setBusinessScope(businessScope);
				    	System.out.println(businessScope);
				    	stock.setChairman(jsonObj.getString("chairman"));
				    	stock.setSecretaries(jsonObj.getString("bsecretary"));
				    	stock.setRegisteredAddress(jsonObj.getString("regaddr"));
				    	stock.setOfficeAddress(jsonObj.getString("officeaddr"));
				    	stock.setPostCode(jsonObj.getString("officezipcode"));
				    	stock.setPhone(jsonObj.getString("comptel"));
				    	stock.setEmail(jsonObj.getString("compemail"));
				    	stock.setWebsite(jsonObj.getString("compurl"));
				    	stock.setCompanyName(jsonObj.getString("compname"));
				    	System.out.println(stock);
				    	StockDAO.updateStock(stock,val);
				    } catch (Exception e){  
				    	try {
				    		if(key.indexOf("sh") != -1){
				    			key = key.replace("sh", "SH");
								entity = response.getEntity(); // 获取响应实体
					    		}else if(key.indexOf("SZ") != -1){
					    			key = key.replace("sz", "SZ");
					    		}
					    		get = new HttpGet(key);
					    		response = httpClient.execute(get);
					    		entity = response.getEntity();
					    		responseContent = EntityUtils.toString(entity, "UTF-8");
					    		json = new JSONObject(responseContent);
					    		jsonObj = json.getJSONObject("tqCompInfo");
					    		stock.setMainBusiness(jsonObj.getString("majorbiz"));
						    	stock.setMainBusiness(jsonObj.getString("majorbiz"));
						    	stock.setBusinessScope(jsonObj.getString("bizscope"));
						    	stock.setChairman(jsonObj.getString("chairman"));
						    	stock.setSecretaries(jsonObj.getString("bsecretary"));
						    	stock.setRegisteredAddress(jsonObj.getString("regaddr"));
						    	stock.setOfficeAddress(jsonObj.getString("officeaddr"));
						    	stock.setPostCode(jsonObj.getString("officezipcode"));
						    	stock.setPhone(jsonObj.getString("comptel"));
						    	stock.setEmail(jsonObj.getString("compemail"));
						    	stock.setWebsite(jsonObj.getString("compurl"));
						    	stock.setCompanyName(jsonObj.getString("compname"));
						    	StockDAO.updateStock(stock,val);
						} catch (Exception e2) {
							// TODO: handle exception
							errorMap.put(key, val);
						}
				    } 
				}
			}
		} catch (IOException e) {
		
			e.printStackTrace();
		} 
		finally {
			// 关闭连接,释放资源
			httpClient.getConnectionManager().shutdown();
		}
	}
}
	
	/**
	 *从雪球网获取个股简介 
	 * @throws Exception 
	 * 
	 */
/*	public static ArrayList<Stock> getstock() throws Exception{
		ArrayList<Stock> arrayList = new ArrayList<>();
		arrayList= StockDAO.InsertStock();
		Stock stock = new Stock();
		String code =arrayList.get(0).getCode();
//		String url = "https://xueqiu.com/stock/f10/compinfo.json?symbol=" + code + "&page=1&size=4&_=1521093753290";
		String url = getCrawlerURL(code, 1, 2);
		getInfo(url);
		return  null;
	}
	public static String getCrawlerURL(String code,int page,int size){
		String url = null;
		if(code!=null && !code.equals("")){
			return "https://xueqiu.com/stock/f10/compinfo.json?symbol="
		+code+"&page=" +page+"&size="+size;
		}
		return url;
	}
	public static ArrayList<Stock> getInfo(String urlInfo) throws IOException{
		URL url = new URL(urlInfo);
		String charset = "UTF-8";
		HttpURLConnection httpurl = (HttpURLConnection) url.openConnection();
		httpurl.setInstanceFollowRedirects(true);
//		httpurl.setRequestMethod("POST");
//		httpurl.setRequestProperty("Content-Type", "application/json");
//		httpurl.setRequestProperty("Accept", "application/json");
		InputStream is = httpurl.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,charset));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
         System.out.println(line);
        }
		is.close();
		br.close();
		 //获得网页源码
        return null;
	}
	
	public static ArrayList<Stock> getXueqiuPage(HashMap<String,String> urlsMap) throws Exception {
		ArrayList<Stock> capitals = new ArrayList<Stock>();
		ArrayList<Stock> errorCapitals = new ArrayList<Stock>();//采集异常的股票
		Stock capital = null;
		String responseContent = null;
		HttpClient httpClient =new SSLClient();
		int succPage=0;//成功返回有实质内容JSON的股票
		try {
			HttpGet get = new HttpGet("http://xueqiu.com/");

			HttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity(); // 获取响应实体
			if (entity != null) {
				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
			Iterator<Entry<String, String>> iter = urlsMap.entrySet().iterator();
			int count=0;
			while (iter.hasNext()) {
				count++;
				Map.Entry<String, String> entry = iter.next(); 
				Object key = entry.getKey();//url
				Object val = entry.getValue();//code
				get = new HttpGet(key.toString());
				response = httpClient.execute(get);
				entity = response.getEntity(); // 获取响应实体
				if (entity != null) {
					responseContent = EntityUtils.toString(entity, "UTF-8");
				    try {
				    	JSONObject json = new JSONObject(responseContent);
				        JSONArray array = new JSONObject(responseContent).getJSONArray("list");  
				        String dateTemp ="";
				        String date ="";
				        if(array.length()<=0){
				        	Thread.sleep(1*1000);
				        	response = httpClient.execute(new HttpGet(key.toString()));
				        	entity = response.getEntity();
				        	responseContent = EntityUtils.toString(entity, "UTF-8");
				        	json = new JSONObject(responseContent);
					        array = new JSONObject(responseContent).getJSONArray("list"); 
					        if(array.length()>0){
					        }else{
					        	errorCapitals.add(capital);
					        	continue;
					        }
				        }
				        for (int i = 0; i < array.length(); i++) {  
				            JSONObject object = (JSONObject) array.opt(i); 
				            capital = new Stock();
				            capital.setCode(val.toString());
				            dateTemp = object.getString("begindate");
				            if(dateTemp.length()>=8){
				            	date = dateTemp.substring(0, 4)+"-"+dateTemp.substring(4, 6) +"-" + dateTemp.substring(6, 8);
				            }else{
				            	date="0";
				            }
//				            capital.setDate(date);
//				            capital.setStock_total((long)(object.getDouble("totalshare")*10000));
//				            capital.setA_traded_stock_total((long)(object.getDouble("circaamt")*10000));
//				            capital.setA_stock_total((long)(object.getDouble("ask")*10000));
//				            capital.setB_stock_total((long)(object.getDouble("bsk")*10000));
//				            capital.setH_stock_total((long)(object.getDouble("hksk")*10000));
//				            capital.setSkchgexp((long)object.getLong("totalsharechg")*10000);
				            
//				            if(object.getString("skchgexp").contains("非公开增发")){
//				            	 // 当期非公开增发股数与上期总股本之比
////				            	 capital.setSkchgexpgreatrate((double)capital.getSkchgexp()/(capital.getStock_total()-capital.getSkchgexp()));
//				            }else{
////				            	 capital.setSkchgexp(0);
//				            }
				            
				            
				            
//				            + capital.getDate() +",总股本=" + capital.getStock_total() + ",A股流通股本="+capital.getA_traded_stock_total());
//				            if(capital.getA_traded_stock_total()<0){
//				            	continue;
//				            }
//				            if(capital.getStock_total()<0){
//				            	logger.info("总股本小于0" + capital.getStock_total());
//				            	continue;
//				            }
//				            capitals.add(capital);
//				            succPage++;
				        }
				   
				        
				    } catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
				}
				
			}
			System.out.println(responseContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpClient.getConnectionManager().shutdown();
		}
		return capitals;
	}*/

