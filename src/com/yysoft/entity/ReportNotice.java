package com.yysoft.entity;

public class ReportNotice {
	private String code;//股票代码
	private String cdate;//采集时间
	private String name;//股票简称
	private String publishDate;//发布日期
	private String title;//标题
	private String reType;//报告类型
	private String pdfURL;//PDF文档地址
	private int id;//编号
	private int year;//财报年份
	private int quarter;//季度编号
	private int num;//采集次数
	private int source;//来源：0-金融街，1-东方财富网
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
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
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getReType() {
		return reType;
	}
	public void setReType(String reType) {
		this.reType = reType;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getCdate() {
		return cdate;
	}
	public void setCdate(String cdate) {
		this.cdate = cdate;
	}
	public String getPdfURL() {
		return pdfURL;
	}
	public void setPdfURL(String pdfURL) {
		this.pdfURL = pdfURL;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	
}
