package com.yysoft.extract;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yysoft.entity.Finance;
import com.yysoft.util.StringUtils;



/**
 * 功能描述：主要指标表页面数据提取
 * 
 * @author Huanyan.Lu
 * @date:2018年8月19日
 * @time:下午3:08:02
 * @version:1.0
 */
public class EastMoneyZYZBExtracter {
	private static Logger logger = LogManager.getLogger(EastMoneyZYZBExtracter.class); //

	/**
	 * Description:主要指标表页面数据提取
	 * 
	 * @param finance
	 *            提取信息实例
	 * @param data
	 *            读取的页面元信息
	 * @param code
	 *            股票代码
	 * @param name
	 *            股票简称
	 * @param reportDate
	 *            要提取信息的报告期
	 * @return
	 */
	public static Finance fetch(Finance finance, String data, String code, String name, String reportDate) {
		logger.info("提取的报告期" + reportDate + "===东方财富-主要指标表页面数据项部分赋值===");
		
		String f_date = "";
		
		JSONObject job = null;
		JSONArray j_list;
		boolean isHaving = false;
		try {
			if (data.startsWith("{")) {
				String data_zyzb = new JSONObject(new JSONObject(data).getString("Result")).getString("zyzb");
				
				j_list = new JSONArray(data_zyzb);
				if (j_list.length() > 0) {
					for (int i = 0; i < j_list.length(); i++) {
						if (j_list.getJSONObject(i).getString("date").contains(reportDate)) {
							job = j_list.getJSONObject(i);
							isHaving = true;
							break;
						}
						continue;
					}
					
				}
			} else {
				return null;
			}
			finance.setCode(code);
			finance.setName(name);
			finance.setReportDate(reportDate);// 报告期
			finance.setQuarter(StringUtils.getQuarterFromReport(reportDate));
			finance.setYear(finance.getReportDate().substring(0, 4));
			
			if(isHaving!=true){
				return null;
			}
			finance.setPRP(job.getString("mgwfply"));// 每股未分配利润
			finance.setNAPS(job.getString("mgjzc"));// 每股净资产
			finance.setDNNP(StringUtils.GetNunberByUnit(job.getString("kfjlr")));// 扣非净利润
			finance.setDNNPGR(job.getString("kfjlrtbzz"));// 扣非净利润同比增长率
			
			finance.setZCFlag(3);// 标记采集来源
			return finance;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

}
