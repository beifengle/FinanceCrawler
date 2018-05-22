package tool;




import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.data.handle.DataExport;
import com.yysoft.util.ConfigParser;

/**
 * 功能描述：把采集下来的元数据同步到生产计算用数据库表 stock_financial
 * @author Huanyan.Lu
 * @date:2016年5月17日
 * @time:下午3:02:05
 * @version:1.0
 */
public class DataExportRunner {
	private static Logger logger = LogManager.getLogger(DataExportRunner.class); //
	
	public static void main(String[] args) {
		logger.entry();
		new ConfigParser();
		DataExport.exec();
		logger.exit();
		
	}
}
