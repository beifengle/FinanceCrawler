package com.yysoft.data.handle;

import java.util.ArrayList;

import com.yysoft.dao.StockDAO;
import com.yysoft.entity.Finance;
import com.yysoft.entity.FinancialMetaData;
import com.yysoft.mgr.FinanceGather;
import com.yysoft.util.ConfigParser;

/**
 * @author bxsun
 * 
 *         对采集的历史缺漏财报数据进行补采
 *
 */
public class MissingFinancial {

	public static void exec() {
		//new ConfigParser();
		// 获取需要重采的数据信息
		ArrayList<FinancialMetaData> flist = StockDAO.getMissFinacial();

		int flag=0;
		//存放采集结果
	    ArrayList<Finance> resultFinances = new ArrayList<Finance>();
	    
		Finance f;
		for (FinancialMetaData fmd : flist) {
			String code=fmd.getCode();
			String name=fmd.getName();
			String reportDate=fmd.getReportDate();
			//test
			//if (!code.contains("300387")) {
           //   continue;				
		//	}
			f=FinanceGather.down(code, name, reportDate);

			if (f!=null) {
				resultFinances.add(f);
			}
		}
		
		StockDAO.insertDB(resultFinances, true);
		
		for(Finance fi:resultFinances){
		flag = StockDAO.importToDCFinance(fi);
		}
	}
	public static void main(String[] args) {
		new ConfigParser();
		exec();
	}
}
