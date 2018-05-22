package com.yysoft.mgr;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.util.ConfigParser;
import com.yysoft.util.StringUtils;
import com.yysoft.dao.StockDAO;
import com.yysoft.entity.Stock;
import com.yysoft.http.AcronymHttpUtils;

public class AcronymGather {
	
	private static Logger logger = LogManager.getLogger(AcronymGather.class); //
	/**
	 * Description:通过接口http://smartbox.gtimg.cn/s3/?q=000028&t=all返回内容并解析
	 *
	 */
	public static void exec(){
		logger.info("\r\n");
		logger.info("=====上市公司简称和首字母缩写数据采集=====");
		ArrayList<Stock> stocksTab = StockDAO.getStockCodes();
		logger.info("当前上市公司数量:" + stocksTab.size());
		String response = null;
		String name = null;//股票简称
		String acronym = null;//股票首字母缩写
		int count =0;
		for(Stock s:stocksTab){
			response = AcronymHttpUtils.gatherAcronym(s.getCode());
			if(StringUtils.isNotEmpty(response)&&response.length()>15){
				if(response.contains("^")){
					String res[] = response.split("\\^");
					for(int i=0;i<res.length;i++){
						if(res[i].contains("~GP")){
							response = res[i];
						}
					}
				}
				String res[] = response.split("~");
				name = unicodeToUtf8(res[2]);
				acronym = unicodeToUtf8(res[3]);
				//更新stock_company
				if(StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(acronym)){
					StockDAO.updateAcronym(name, acronym, s.getCode());
				}
				logger.info((++count)+"---首字母缩写采集：" + s.getCode()+"-" +acronym);
			}
		}
		logger.info("=====上市公司简称和首字母缩写数据采集=====结束");
		logger.info("\r\n");
	}
	
	/**
    *
    * @param theString
    * @return String
    */
   public static String unicodeToUtf8(String theString) {
       char aChar;
       int len = theString.length();
       StringBuffer outBuffer = new StringBuffer(len);
       for (int x = 0; x < len;) {
           aChar = theString.charAt(x++);
           if (aChar == '\\') {
               aChar = theString.charAt(x++);
               if (aChar == 'u') {
                   // Read the xxxx
                   int value = 0;
                   for (int i = 0; i < 4; i++) {
                       aChar = theString.charAt(x++);
                       switch (aChar) {
                       case '0':
                       case '1':
                       case '2':
                       case '3':
                       case '4':
                       case '5':
                       case '6':
                       case '7':
                       case '8':
                       case '9':
                           value = (value << 4) + aChar - '0';
                           break;
                       case 'a':
                       case 'b':
                       case 'c':
                       case 'd':
                       case 'e':
                       case 'f':
                           value = (value << 4) + 10 + aChar - 'a';
                           break;
                       case 'A':
                       case 'B':
                       case 'C':
                       case 'D':
                       case 'E':
                       case 'F':
                           value = (value << 4) + 10 + aChar - 'A';
                           break;
                       default:
                           throw new IllegalArgumentException(
                                   "Malformed   \\uxxxx   encoding.");
                       }
                   }
                   outBuffer.append((char) value);
               } else {
                   if (aChar == 't')
                       aChar = '\t';
                   else if (aChar == 'r')
                       aChar = '\r';
                   else if (aChar == 'n')
                       aChar = '\n';
                   else if (aChar == 'f')
                       aChar = '\f';
                   outBuffer.append(aChar);
               }
           } else
               outBuffer.append(aChar);
       }
       return outBuffer.toString();
   }


	public static void main(String[] args) {
		System.out.println(unicodeToUtf8("\u5c24\u6d1b\u5361"));
		new ConfigParser();
		exec();
	}
}
