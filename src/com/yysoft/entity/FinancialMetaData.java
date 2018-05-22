package com.yysoft.entity;

/**
 * stock_financial表
 * 
 * @author 作者 E-mail:
 * @date 创建时间：2016年1月20日 下午3:16:20
 * @version 1.0
 * @parameter
 * @return
 */
public class FinancialMetaData {
	private int id;// ,
	private String code;//
	private String name;//
	private int icode;
	private String iname;
	private int year;// 报告年份
	private int quarter;// 季度
	private int liveType;// 是否上市
	private String ReportDate;// 报告期
	private String announcedDate;// 上市日期
	private double lastClose;//收盘价
	private double high52wk;//52周最高（元）
	private double low52wk;//52周最低（元）
	private double ar;// 应收账款净额
	private double beps;// 基本每股收益
	private double capitalReserves;// 资本公积金
	private double currentAssets;// 流动资产合计
	private double currentDebt;// 流动负债合计
	private double cPFDAPDOIP;// 分配股利、利润或偿付利息支付的现金
	private double deps;// 稀释每股收益
	private double dividendPayable;// 应付股利
	private double dps;// 每股股息/分红
	private double inventory;// 存货净额
	private double longDebt;// 长期负债合计
	private double nca;// 非流动资产合计
	private double ncffoa;// 经营净现金流（元）
	private double ncffia;// 投资净现金流（元）
	private double ncfffa;// 筹资净现金流（元）
	private double niicace;// 现金及现金等价物净增加额
	private double np;// 净利润
	private double onpatp;// 归属于母公司所有者的净利润
	private double operatingProfit;// 营业利润
	private double operatingCost;// 营业成本
	private double operatingIncome;// 营业收入
	private double ownersEquity;// 所有者权益合计
	private double rp;// 未分配利润
	private double totalAssets;// 资产总计
	private double totalDebt;// 负债合计
	private double tEATTSOPC;// 归属于母公司所有者权益合计
	private double toi;// 营业总收入
	private double totalProfit;// 利润总额
	private double toc;// 营业总成本
	private double yield;// 投资收益
	private double totalCapital;// 总股本
	private double tradedACapital;// 流通A股本
	private double totalMarketValue;//总市值  
	private double tradedMarketValue;//流通市值  
	private double avgMarketValue;//年平均总市值
	private double operatingIncomeJRJ;//金融界最新财务表中营业收入
	private double onpatpJRJ;//金融界最新财务表中归属于母公司所有者的净利润
	private double tEATTSOPCJRJ;//金融界最新财务表中归属于母公司所有者权益合计
	private double NCFFOAJRJ;//金融界最新财务表中经营净现金流（元）
	private int skchgeflag;//公司主体是否发生变化,1:发生变化.
	private int sh50;//
	private int sh180;//
	private int sh380;//
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
	public int getIcode() {
		return icode;
	}
	public void setIcode(int icode) {
		this.icode = icode;
	}
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
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
	public int getLiveType() {
		return liveType;
	}
	public void setLiveType(int liveType) {
		this.liveType = liveType;
	}
	public String getReportDate() {
		return ReportDate;
	}
	public void setReportDate(String reportDate) {
		ReportDate = reportDate;
	}
	public String getAnnouncedDate() {
		return announcedDate;
	}
	public void setAnnouncedDate(String announcedDate) {
		this.announcedDate = announcedDate;
	}
	public double getLastClose() {
		return lastClose;
	}
	public void setLastClose(double lastClose) {
		this.lastClose = lastClose;
	}
	public double getHigh52wk() {
		return high52wk;
	}
	public void setHigh52wk(double high52wk) {
		this.high52wk = high52wk;
	}
	public double getLow52wk() {
		return low52wk;
	}
	public void setLow52wk(double low52wk) {
		this.low52wk = low52wk;
	}
	public double getAr() {
		return ar;
	}
	public void setAr(double ar) {
		this.ar = ar;
	}
	public double getBeps() {
		return beps;
	}
	public void setBeps(double beps) {
		this.beps = beps;
	}
	public double getCapitalReserves() {
		return capitalReserves;
	}
	public void setCapitalReserves(double capitalReserves) {
		this.capitalReserves = capitalReserves;
	}
	public double getCurrentAssets() {
		return currentAssets;
	}
	public void setCurrentAssets(double currentAssets) {
		this.currentAssets = currentAssets;
	}
	public double getCurrentDebt() {
		return currentDebt;
	}
	public void setCurrentDebt(double currentDebt) {
		this.currentDebt = currentDebt;
	}
	public double getcPFDAPDOIP() {
		return cPFDAPDOIP;
	}
	public void setcPFDAPDOIP(double cPFDAPDOIP) {
		this.cPFDAPDOIP = cPFDAPDOIP;
	}
	public double getDeps() {
		return deps;
	}
	public void setDeps(double deps) {
		this.deps = deps;
	}
	public double getDividendPayable() {
		return dividendPayable;
	}
	public void setDividendPayable(double dividendPayable) {
		this.dividendPayable = dividendPayable;
	}
	public double getDps() {
		return dps;
	}
	public void setDps(double dps) {
		this.dps = dps;
	}
	public double getInventory() {
		return inventory;
	}
	public void setInventory(double inventory) {
		this.inventory = inventory;
	}
	public double getLongDebt() {
		return longDebt;
	}
	public void setLongDebt(double longDebt) {
		this.longDebt = longDebt;
	}
	public double getNca() {
		return nca;
	}
	public void setNca(double nca) {
		this.nca = nca;
	}
	public double getNcffoa() {
		return ncffoa;
	}
	public void setNcffoa(double ncffoa) {
		this.ncffoa = ncffoa;
	}
	public double getNcffia() {
		return ncffia;
	}
	public void setNcffia(double ncffia) {
		this.ncffia = ncffia;
	}
	public double getNcfffa() {
		return ncfffa;
	}
	public void setNcfffa(double ncfffa) {
		this.ncfffa = ncfffa;
	}
	public double getNiicace() {
		return niicace;
	}
	public void setNiicace(double niicace) {
		this.niicace = niicace;
	}
	public double getNp() {
		return np;
	}
	public void setNp(double np) {
		this.np = np;
	}
	public double getOnpatp() {
		return onpatp;
	}
	public void setOnpatp(double onpatp) {
		this.onpatp = onpatp;
	}
	public double getOperatingProfit() {
		return operatingProfit;
	}
	public void setOperatingProfit(double operatingProfit) {
		this.operatingProfit = operatingProfit;
	}
	public double getOperatingCost() {
		return operatingCost;
	}
	public void setOperatingCost(double operatingCost) {
		this.operatingCost = operatingCost;
	}
	public double getOperatingIncome() {
		return operatingIncome;
	}
	public void setOperatingIncome(double operatingIncome) {
		this.operatingIncome = operatingIncome;
	}
	public double getOwnersEquity() {
		return ownersEquity;
	}
	public void setOwnersEquity(double ownersEquity) {
		this.ownersEquity = ownersEquity;
	}
	public double getRp() {
		return rp;
	}
	public void setRp(double rp) {
		this.rp = rp;
	}
	public double getTotalAssets() {
		return totalAssets;
	}
	public void setTotalAssets(double totalAssets) {
		this.totalAssets = totalAssets;
	}
	public double getTotalDebt() {
		return totalDebt;
	}
	public void setTotalDebt(double totalDebt) {
		this.totalDebt = totalDebt;
	}
	public double gettEATTSOPC() {
		return tEATTSOPC;
	}
	public void settEATTSOPC(double tEATTSOPC) {
		this.tEATTSOPC = tEATTSOPC;
	}
	public double getToi() {
		return toi;
	}
	public void setToi(double toi) {
		this.toi = toi;
	}
	public double getTotalProfit() {
		return totalProfit;
	}
	public void setTotalProfit(double totalProfit) {
		this.totalProfit = totalProfit;
	}
	public double getToc() {
		return toc;
	}
	public void setToc(double toc) {
		this.toc = toc;
	}
	public double getYield() {
		return yield;
	}
	public void setYield(double yield) {
		this.yield = yield;
	}
	public double getTotalCapital() {
		return totalCapital;
	}
	public void setTotalCapital(double totalCapital) {
		this.totalCapital = totalCapital;
	}
	public double getTradedACapital() {
		return tradedACapital;
	}
	public void setTradedACapital(double tradedACapital) {
		this.tradedACapital = tradedACapital;
	}
	public double getTotalMarketValue() {
		return totalMarketValue;
	}
	public void setTotalMarketValue(double totalMarketValue) {
		this.totalMarketValue = totalMarketValue;
	}
	public double getTradedMarketValue() {
		return tradedMarketValue;
	}
	public void setTradedMarketValue(double tradedMarketValue) {
		this.tradedMarketValue = tradedMarketValue;
	}
	public double getAvgMarketValue() {
		return avgMarketValue;
	}
	public void setAvgMarketValue(double avgMarketValue) {
		this.avgMarketValue = avgMarketValue;
	}
	public double getOperatingIncomeJRJ() {
		return operatingIncomeJRJ;
	}
	public void setOperatingIncomeJRJ(double operatingIncomeJRJ) {
		this.operatingIncomeJRJ = operatingIncomeJRJ;
	}
	public double getOnpatpJRJ() {
		return onpatpJRJ;
	}
	public void setOnpatpJRJ(double onpatpJRJ) {
		this.onpatpJRJ = onpatpJRJ;
	}
	public double gettEATTSOPCJRJ() {
		return tEATTSOPCJRJ;
	}
	public void settEATTSOPCJRJ(double tEATTSOPCJRJ) {
		this.tEATTSOPCJRJ = tEATTSOPCJRJ;
	}
	public double getNCFFOAJRJ() {
		return NCFFOAJRJ;
	}
	public void setNCFFOAJRJ(double nCFFOAJRJ) {
		NCFFOAJRJ = nCFFOAJRJ;
	}
	public int getSkchgeflag() {
		return skchgeflag;
	}
	public void setSkchgeflag(int skchgeflag) {
		this.skchgeflag = skchgeflag;
	}
	public int getSh50() {
		return sh50;
	}
	public void setSh50(int sh50) {
		this.sh50 = sh50;
	}
	public int getSh180() {
		return sh180;
	}
	public void setSh180(int sh180) {
		this.sh180 = sh180;
	}
	public int getSh380() {
		return sh380;
	}
	public void setSh380(int sh380) {
		this.sh380 = sh380;
	}
}
