package tool;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.dao.StockDAO;
import com.yysoft.data.handle.DataExport;
import com.yysoft.entity.Finance;
import com.yysoft.entity.ReportNotice;
import com.yysoft.entity.Stock;
import com.yysoft.mgr.FinanceGather;
import com.yysoft.util.ConfigParser;
import com.yysoft.util.ReportDateUtils;

/**
 * 功能描述：采集单个股票的指定报告期的财务报告
 * 也可以在文件中D://t.txt 按代码+空格+股票简称  一行一个输入多个股票采集
 * @author Huanyan.Lu
 * @date:2016年5月17日
 * @time:下午2:21:06
 * @version:1.0
 */
public class GatherFinanceByCode {
	private static Logger logger = LogManager.getLogger(GatherFinanceByCode.class); //

	public static void main(String[] args) {
		new ConfigParser();
		logger.info("===start===");
		ArrayList<Finance> codeList = new ArrayList<Finance>();
		Finance ff = new Finance();
		String reportDate = "2018-03-31";

		
		File file=new File("D://t.txt");
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
			String line=null;
	        while((line=br.readLine())!=null){
	        	ff = new Finance();
	        	ff.setCode(line.split(" ")[0]);
	        	ff.setName(line.split(" ")[1]);
	        	ff.setReportDate(reportDate);
	        	codeList.add(ff);
	        }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		int succ = 0;// 成功采集到的财报数量
		int fail = 0;// 成功采集到的财报数量
		// 存放采集结果
		ArrayList<Finance> resultFinances = new ArrayList<Finance>();
		for(Finance f:codeList){
			
			f = FinanceGather.down(f.getCode(), f.getName(), reportDate);
			// 判断是否成功采集到数据
			if (f.getIsCapture()==1) {
				resultFinances.add(f);
				succ++;
				logger.info(f.getCode() + ",报告期：" + reportDate + "-采集成功。");
			} else {
				fail++;
				logger.info(f.getCode() + ",报告期：" + reportDate + "-采集失败。");
			}
		}
		// write to Finance  
		StockDAO.insertDB(resultFinances, true);
		
		
		//同步到生产数据库
		logger.info("---同步到生产数据库---");
//		DataExport.exec();
		logger.info("===采集成功的财报数量：" + succ);
		logger.info("===采集失败的财报数量：" + fail);
		logger.info("test=====结束");
	}
}
