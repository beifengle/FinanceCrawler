package com.yysoft.data.handle;

import java.io.BufferedReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import com.yysoft.dao.StockDAO;
import com.yysoft.entity.FinancialMetaData;
import com.yysoft.entity.Stock;
import com.yysoft.entity.StockDividends;
import com.yysoft.util.StringUtils;

/** 
* @author  作者 E-mail: 
* @date 创建时间：2016年5月3日 下午2:11:51 
* @version 1.0 
* @parameter    
* @return  
*/
public class DividendComReponse {

	/**
	 * 功能描述：东方财富返回数据解析
	 * 
	 * @author sunbaixiang
	 * @date:2016年05月03日
	 * @time:上午9:19:56
	 * @version:1.0
	 */
	public static ArrayList<FinancialComparison> ParseSheetEAST(BufferedReader br, String year) {

		ArrayList<FinancialComparison> dividends = new ArrayList<FinancialComparison>();

		try {
			String line;
			line = br.readLine();

			line = line.replace("[", "");
			line = line.replace("]", "");
			line = line.replace("(", "");
			line = line.replace(")", "");
			line = line.replace("\"", "");
			String[] ss = line.split(",");

			for (int i = 0; i <= ss.length - 1; i += 19) { // ss.length-1
				FinancialComparison div = new FinancialComparison();
				div.setReportDay(year);
				String code = "";
				code = ss[i];
				if (code.indexOf('0') == 0 || code.indexOf('3') == 0) {
					code = "sz" + code;
				}
				if (code.indexOf('6') == 0) {
					code = "sh" + code;
				}
				div.setCode(code);
				String name="";
				name=ss[i+1];
				div.setName(name);
				double OIGR=0;
				if (!ss[i+5].equals("-")) {
					OIGR=Double.parseDouble(ss[i+5]);
				}else{
					OIGR=-1;
				}
				div.setOIGR(OIGR);
				div.setReportDay(year);
				double NPGR=0;
				if (!ss[i+8].equals("-")) {
					NPGR=Double.parseDouble(ss[i+8]);
				}else{
					NPGR=-1;
				}
				div.setNPGR(NPGR);
				double NAPS=0;
				if (!ss[i+10].equals("-")) {
					NAPS=Double.parseDouble(ss[i+10]);
				}else{
					NAPS=-1;
				}
				div.setNAPS(NAPS);
				double ROE=0;
				if (!ss[i+11].equals("-")) {
					ROE=Double.parseDouble(ss[i+11]);
				}else{
					ROE=-1;
				}
				div.setROE(ROE);
				double PNCFFOA=0;
				if (!ss[i+12].equals("-")) {
					PNCFFOA=Double.parseDouble(ss[i+12]);
				}else{
					PNCFFOA=-1;
				}
				div.setPNCFFOA(PNCFFOA);
				double GrossMagin=0;
				if (!ss[i+13].equals("-")) {
					GrossMagin=Double.parseDouble(ss[i+13]);
				}else{
					GrossMagin=-1;
				}
                div.setGrossMagin(GrossMagin);
				
                dividends.add(div);
				}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dividends;

	}
	/**
	 * 功能描述：金融界返回数据解析
	 * 
	 * @author bxsun
	 * @date:2015年10月13日
	 * @time:上午9:19:56
	 * @version:1.0
	 */
	public static ArrayList<FinancialComparison> ParseSheetJRJ(BufferedReader br, String year) {
		ArrayList<FinancialComparison> dividends = new ArrayList<FinancialComparison>();

		try {
			String line;
			line = br.readLine();

			line = StringUtils.getStrByReg("data:(.*?)]};", line);
			line = line.replace("[", "");
			line = line.replace("]", "");
			String[] ss = line.split(",");
			for (int i = 0; i <= ss.length - 1; i += 21) {
				FinancialComparison div = new FinancialComparison();
				div.setReportDay(year);

				String code = "";
				code = ss[i];
				code = code.replace("'", "");
				if (code.indexOf('0') == 0 || code.indexOf('3') == 0) {
					code = "sz" + code;
				}
				if (code.indexOf('6') == 0) {
					code = "sh" + code;
				}
				div.setCode(code);
				String name="";
				name=ss[i+1];
				name=name.replace("'", "");
				div.setName(name);
				double OIGR=0;
				if (!ss[i+16].equals("")) {
					OIGR=Double.parseDouble(ss[i+16]);
				}else{
					OIGR=-1;
				}
				div.setOIGR(OIGR);
				div.setReportDay(year);
				double NPGR=0;
				if (!ss[i+17].equals("")) {
					NPGR=Double.parseDouble(ss[i+17]);
				}else{
					NPGR=-1;
				}
				div.setNPGR(NPGR);
				double NAPS=0;
				if (!ss[i+5].equals("")) {
					NAPS=Double.parseDouble(ss[i+5]);
				}else{
					NAPS=-1;
				}
				div.setNAPS(NAPS);
				double ROE=0;
				if (!ss[i+6].equals("")) {
					ROE=Double.parseDouble(ss[i+6]);
				}else{
					ROE=-1;
				}
				div.setROE(ROE);
				double PNCFFOA=0;
				if (!ss[i+18].equals("")) {
					PNCFFOA=Double.parseDouble(ss[i+18]);
				}else{
					PNCFFOA=-1;
				}
				div.setPNCFFOA(PNCFFOA);
				
				dividends.add(div);

			}	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dividends;

	}

	/**
	 * 功能描述：金融界返回数据解析,采集表中营业收入,净利润
	 * 
	 * @author bxsun
	 * @date:2015年10月13日
	 * @time:上午9:19:56
	 * @version:1.0
	 */
	public static ArrayList<FinancialMetaData> ParseSheetJRJ2(BufferedReader br, String year) {
		ArrayList<FinancialMetaData> dividends = new ArrayList<FinancialMetaData>();

		try {
			String line;
			line = br.readLine();

			line = StringUtils.getStrByReg("data:(.*?)]};", line);
			line = line.replace("[", "");
			line = line.replace("]", "");
			String[] ss = line.split(",");
			for (int i = 0; i <= ss.length - 1; i += 21) {
				FinancialMetaData div = new FinancialMetaData();
				
				div.setReportDate(year);

				String code = "";
				code = ss[i];
				code = code.replace("'", "");
				if (code.indexOf('0') == 0 || code.indexOf('3') == 0) {
					code = "sz" + code;
				}
				if (code.indexOf('6') == 0) {
					code = "sh" + code;
				}
				div.setCode(code);
				String name="";
				name=ss[i+1];
				name=name.replace("'", "");
				div.setName(name);
				double OperatingIncomeJRJ=0;
				if (!ss[i+3].equals("")) {
					OperatingIncomeJRJ=Double.parseDouble(ss[i+3]);
				}else{
					OperatingIncomeJRJ=0;
				}
				div.setOperatingIncomeJRJ(OperatingIncomeJRJ);
				
				double ONPATPJRJ=0;
				if (!ss[i+4].equals("")) {
					ONPATPJRJ=Double.parseDouble(ss[i+4]);
				}else{
					ONPATPJRJ=0;
				}
				div.setOnpatpJRJ(ONPATPJRJ);
				double TEATTSOPCJRJ=0;
				if (!ss[i+5].equals("")) {
					TEATTSOPCJRJ=Double.parseDouble(ss[i+5]);
				}else{
					TEATTSOPCJRJ=0;
				}
				div.settEATTSOPCJRJ(TEATTSOPCJRJ);
				
				double NCFFOAJRJ=0;
				if (!ss[i+18].equals("")) {
					NCFFOAJRJ=Double.parseDouble(ss[i+18]);
				}else{
					NCFFOAJRJ=-1;
				}
				div.setNCFFOAJRJ(NCFFOAJRJ);
				
				
				dividends.add(div);

			}	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dividends;
	}

	/**
	 * 功能描述：东方财富返回数据解析
	 * 
	 * @author sunbaixiang
	 * @date:2016年05月03日
	 * @time:上午9:19:56
	 * @version:1.0
	 */
	public static ArrayList<FinancialMetaData> ParseSheetEAST2(BufferedReader br, String year) {

		ArrayList<FinancialMetaData> dividends = new ArrayList<FinancialMetaData>();

		try {
			String line;
			line = br.readLine();

			line = line.replace("[", "");
			line = line.replace("]", "");
			line = line.replace("(", "");
			line = line.replace(")", "");
			line = line.replace("\"", "");
			String[] ss = line.split(",");

			for (int i = 0; i <= ss.length - 1; i += 19) { // ss.length-1
				FinancialMetaData div = new FinancialMetaData();
				div.setReportDate(year);
				String code = "";
				code = ss[i];
				if (code.indexOf('0') == 0 || code.indexOf('3') == 0) {
					code = "sz" + code;
				}
				if (code.indexOf('6') == 0) {
					code = "sh" + code;
				}
				div.setCode(code);
				String name="";
				name=ss[i+1];
				div.setName(name);
				double OperatingIncomeJRJ=0;
				if (!ss[i+4].equals("")) {
					OperatingIncomeJRJ=Double.parseDouble(ss[i+4]);
				}else{
					OperatingIncomeJRJ=0;
				}
				div.setOperatingIncomeJRJ(OperatingIncomeJRJ);
				
				double ONPATPJRJ=0;
				if (!ss[i+7].equals("")) {
					ONPATPJRJ=Double.parseDouble(ss[i+7]);
				}else{
					ONPATPJRJ=0;
				}
				div.setOnpatpJRJ(ONPATPJRJ);
				double TEATTSOPCJRJ=0;
				if (!ss[i+10].equals("")) {
					TEATTSOPCJRJ=Double.parseDouble(ss[i+10]);
				}else{
					TEATTSOPCJRJ=0;
				}
				div.settEATTSOPCJRJ(TEATTSOPCJRJ);
				double NCFFOAJRJ=0;
				if (!ss[i+12].equals("-")) {
					NCFFOAJRJ=Double.parseDouble(ss[i+12]);
				}else{
					NCFFOAJRJ=-1;
				}
				div.setNCFFOAJRJ(NCFFOAJRJ);
				
                dividends.add(div);
				}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dividends;

	}
}
