package com.yysoft.util;

import java.util.ArrayList;
import java.util.Date;

/**
 * 功能描述：
 * @author Huanyan.Lu
 * @date:2016年5月12日
 * @time:下午2:56:02
 * @version:1.0
 */
public class ReportDateUtils {
	/**
	 * Description:获取当前系统日期的最新财报报告期
	 * @return
	 */
	public static String fetchLastReportDate(){
		String reportDate = "";
		int year = DateUtils.getYear(new Date());
		int month = DateUtils.getMonth(new Date());
		if(month>=1 && month<=3){//此时已公布的最新的发布的报告期为年报
			year = year-1;
			reportDate = year + "-12-31"; 
		}else if(month>3 && month<=6){//此时已公布的最新的发布的报告期为1季报
			reportDate = year + "-03-31"; 
		}else if(month>6 && month<=9){//此时已公布的最新的发布的报告期为2季报
			reportDate = year + "-06-30"; 
		}else if(month>9 && month<=12){//此时已公布的最新的发布的报告期为3季报
			reportDate = year + "-09-30"; 
		}
		return reportDate;
	}
	/**
	 * Description:生成要采集的（新上市股票）财报报告期
	 * @param endReportDate 截止财报报告期
	 * @param hisYear 要生成的历史财报年数
	 * @return
	 */
	public static ArrayList<String> buildReportDates(String endReportDate,int hisYear){
		ArrayList<String> lists = new ArrayList<String>();
		int endYear = Integer.valueOf(endReportDate.substring(0, 4));//采集截止财报年份
		int startYear = endYear - hisYear;//采集开始财报年份
		int q=0;//当前最新报告期季度编号
		if(endReportDate.contains("-03-31")){
			q=1;
		}else if(endReportDate.contains("-06-30")){
			q=2;
		}else if(endReportDate.contains("-09-30")){
			q=3;
		}else if(endReportDate.contains("-12-31")){
			q=4;
		}
		for(int year=endYear;year>=startYear;year--){
			for(;q>0;q--){
				lists.add(convertReportDate(year,q));
			}
			q=4;
		}
		return lists;
	}
	
	/**
	 * Description:生成要采集的（新上市股票）财报报告期
	 * @param endReportDate 截止财报报告期
	 * @param startYear 起始财报年份
	 * @return
	 */
	public static ArrayList<String> buildReportDates2(String endReportDate,int startYear){
		ArrayList<String> lists = new ArrayList<String>();
		int endYear = Integer.valueOf(endReportDate.substring(0, 4));//采集截止财报年份
		int q=0;//当前最新报告期季度编号
		if(endReportDate.contains("-03-31")){
			q=1;
		}else if(endReportDate.contains("-06-30")){
			q=2;
		}else if(endReportDate.contains("-09-30")){
			q=3;
		}else if(endReportDate.contains("-12-31")){
			q=4;
		}
		for(int year=endYear;year>=startYear;year--){
			for(;q>0;q--){
				lists.add(convertReportDate(year,q));
			}
			q=4;
		}
		return lists;
	}
	
	/**
	 * Description:返回输入的季度编号所对应的报告期：yyyy-MM-dd
	 * @param q
	 * @return
	 */
	public static String convertReportDate(int year ,int q){
		String reportDate ="";
		if(q==1){
			reportDate =year+"-03-31";
		}else if(q==2){
			reportDate =year+"-06-30";
		}else if(q==3){
			reportDate =year+"-09-30";
		}else if(q==4){
			reportDate =year+"-12-31";
		}
		return reportDate;
	}
}
