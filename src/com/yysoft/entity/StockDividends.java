package com.yysoft.entity;
/** 
* @author  bxsun 
* @date 创建时间：2016年4月7日 下午5:28:06 
* @version 1.0 
* @parameter    
* @return  
*/
public class StockDividends {

	private String code;//代码
	private String name;//名称
	private int icode;//行业代码
	private int year;//年度
	private int quarter;//季度
	private String reportDate;//股权登记日
	private double dividendPerShare;//股息
	private String dividendPlan;//分红转增方案
	private int dividendPlanFlag;//分红转增方案标志位,-1-无方案,0-股东大会预案,1-实施案,2-董事会预案
	private String dividendPlanAbstract;//分红派现方案简述
	private String dividendPlanIncrease;//分红转增方案简述
	private double stock_total;//'股本总数'
	private String announDate;
    private String recordDate;
	public String getCode() {
		return code;
	}
	public String getDividendPlan() {
		return dividendPlan;
	}
	public void setDividendPlan(String dividendPlan) {
		this.dividendPlan = dividendPlan;
	}
	public int getDividendPlanFlag() {
		return dividendPlanFlag;
	}
	public void setDividendPlanFlag(int dividendPlanFlag) {
		this.dividendPlanFlag = dividendPlanFlag;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getQuarter() {
		return quarter;
	}
	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public double getDividendPerShare() {
		return dividendPerShare;
	}
	public void setDividendPerShare(double dividendPerShare) {
		this.dividendPerShare = dividendPerShare;
	}
	public String getAnnounDate() {
		return announDate;
	}
	public void setAnnounDate(String announDate) {
		this.announDate = announDate;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public int getIcode() {
		return icode;
	}
	public void setIcode(int icode) {
		this.icode = icode;
	}
	public String getDividendPlanAbstract() {
		return dividendPlanAbstract;
	}
	public void setDividendPlanAbstract(String dividendPlanAbstract) {
		this.dividendPlanAbstract = dividendPlanAbstract;
	}
	public String getDividendPlanIncrease() {
		return dividendPlanIncrease;
	}
	public void setDividendPlanIncrease(String dividendPlanIncrease) {
		this.dividendPlanIncrease = dividendPlanIncrease;
	}
	public double getStock_total() {
		return stock_total;
	}
	public void setStock_total(double stock_total) {
		this.stock_total = stock_total;
	}
	
}
