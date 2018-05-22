package com.yysoft.entity;
/**
 * 功能描述：标记财报采集源
 * @author Huanyan.Lu
 * @date:2018年3月25日
 * @time:下午12:00:04
 * @version:1.0
 */
public class FinanceLog {
	private String Code;//股票代码',
	private String Name;//股票名称',
	private String ReportDate;//报告期',
	private int JRJZCFlag;//金融街-资产负债表采集：0-没有采集到，1-有采集,2-使用',
	private int JRJXJFlag;//金融街-现金流量表采集：0-没有采集到，1-有采集,2-使用',
	private int JRJLRFlag;//金融街-利润分配表采集：0-没有采集到，1-有采集,2-使用',
	private int SINAZCFlag;//新浪-资产负债表采集：0-没有采集到，1-有采集,2-使用',
	private int SINAXJFlag;//新浪-现金流量表采集：0-没有采集到，1-有采集,2-使用',
	private int SINALRFlag;//新浪-利润分配表采集：0-没有采集到，1-有采集,2-使用',
	private int EASTZCFlag;//东方财富-资产负债表采集：0-没有采集到，1-有采集,2-使用',
	private int EASTXJFlag;//东方财富-现金流量表采集：0-没有采集到，1-有采集,2-使用',
	private int EASTLRFlag;//东方财富-利润分配表采集：0-没有采集到，1-有采集,2-使用',
	private String LOG;//备注--描述数据项异常信息',
	
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getReportDate() {
		return ReportDate;
	}
	public void setReportDate(String reportDate) {
		ReportDate = reportDate;
	}
	public int getJRJZCFlag() {
		return JRJZCFlag;
	}
	public void setJRJZCFlag(int jRJZCFlag) {
		JRJZCFlag = jRJZCFlag;
	}
	public int getJRJXJFlag() {
		return JRJXJFlag;
	}
	public void setJRJXJFlag(int jRJXJFlag) {
		JRJXJFlag = jRJXJFlag;
	}
	public int getJRJLRFlag() {
		return JRJLRFlag;
	}
	public void setJRJLRFlag(int jRJLRFlag) {
		JRJLRFlag = jRJLRFlag;
	}
	public int getSINAZCFlag() {
		return SINAZCFlag;
	}
	public void setSINAZCFlag(int sINAZCFlag) {
		SINAZCFlag = sINAZCFlag;
	}
	public int getSINAXJFlag() {
		return SINAXJFlag;
	}
	public void setSINAXJFlag(int sINAXJFlag) {
		SINAXJFlag = sINAXJFlag;
	}
	public int getSINALRFlag() {
		return SINALRFlag;
	}
	public void setSINALRFlag(int sINALRFlag) {
		SINALRFlag = sINALRFlag;
	}
	public int getEASTZCFlag() {
		return EASTZCFlag;
	}
	public void setEASTZCFlag(int eASTZCFlag) {
		EASTZCFlag = eASTZCFlag;
	}
	public int getEASTXJFlag() {
		return EASTXJFlag;
	}
	public void setEASTXJFlag(int eASTXJFlag) {
		EASTXJFlag = eASTXJFlag;
	}
	public int getEASTLRFlag() {
		return EASTLRFlag;
	}
	public void setEASTLRFlag(int eASTLRFlag) {
		EASTLRFlag = eASTLRFlag;
	}
	public String getLOG() {
		return LOG;
	}
	public void setLOG(String lOG) {
		LOG = lOG;
	}
}
