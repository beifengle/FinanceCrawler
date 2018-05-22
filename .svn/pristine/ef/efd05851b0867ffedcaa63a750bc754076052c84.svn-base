package com.yysoft.data.handle;

import java.util.ArrayList;

import com.mysql.jdbc.MiniAdmin;
import com.yysoft.dao.StockDAO;
import com.yysoft.entity.FinancialMetaData;
import com.yysoft.util.ConfigParser;

/**
 * @author 作者 E-mail: bsxun
 * @date 创建时间：2016年7月22日 上午10:02:12
 * @version 1.0
 * @parameter 给发生主体变化公司打上标记,季度影响一年,年度影响两年
 * @return
 */
public class FinancialSkchange {

	public static void exec() {

		// 当营业收入和净利润与修正值比较同时超过1%时,就表明公司主体发生变化

		ArrayList<FinancialMetaData> flist = StockDAO.getMetaFinancial(2015);

		for (FinancialMetaData fmd : flist) {

			String code = fmd.getCode();
			String reportDate = fmd.getReportDate();
			double OperatingIncome = fmd.getOperatingIncome();
			double OperatingIncomeJRJ = fmd.getOperatingIncomeJRJ();
			double Onpatp = fmd.getOnpatp();
			double OnpatpJRJ = fmd.getOnpatpJRJ();

			String date=(Integer.parseInt(reportDate.substring(0, 4))+1)+""+reportDate.substring(4);
			int count = 0;

			if (OperatingIncome != OperatingIncomeJRJ && OperatingIncome!=0 && OperatingIncomeJRJ!=0) {
				double a = (Math.abs(OperatingIncome) - Math.abs(OperatingIncomeJRJ)) / Math.abs(OperatingIncomeJRJ);
				if (Math.abs(a) > 0.01) {
					count += 1;
				}
			}
			if (Onpatp != OnpatpJRJ && Onpatp!=0 && OnpatpJRJ!=0) {
				double a = (Math.abs(Onpatp) - Math.abs(OnpatpJRJ)) / Math.abs(OnpatpJRJ);
				if (Math.abs(a) > 0.01) {
					count += 1;
				}
			}

			if (count==2) {
				String sql="update stock_financial set skchgeflag=1 where Code='"+code+"' and ReportDate='"+date+"'";
				StockDAO.runSql(sql);
			}
		}
	}
	public static void main(String[] args) {
		new ConfigParser();
		exec();
	}
}
