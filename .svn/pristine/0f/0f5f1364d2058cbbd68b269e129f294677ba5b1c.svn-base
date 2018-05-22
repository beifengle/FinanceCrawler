package tool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Statement;
import java.text.DecimalFormat;

import com.yysoft.pool.DBManager;
import com.yysoft.util.ConfigParser;

/**
 * 把金融街采集到的源数据financial表简单加工处理后 批量导入到用于计算的表stock_financial
 * @author 作者 E-mail:
 * @date 创建时间：2015年10月19日 上午11:38:34
 * @version 1.0
 * @parameter
 * @return
 */
public class ImportFinanceToStockFinanceTable {
	private static Logger logger = LogManager.getLogger(ImportFinanceToStockFinanceTable.class); //记录日志 
	static DecimalFormat    df2   = new DecimalFormat("######0.00");  // 保留两位小数
	static DecimalFormat    df4   = new DecimalFormat("######0.0000");  //保留四位小数 
	static DecimalFormat    df6   = new DecimalFormat("######0.000000");  //保留四位小数 
	public static void main(String[] args) {
		int dataNum =1000;//每次插入表的数量上限
		new ConfigParser();
		try {
			Connection con = DBManager.connPoolH.getConnection();
			String setSQL = "select `Code`,`Name`,`Year`,`Quarter`,`ReportDate`,AccountsReceivable"
					+ ",BEPS,CapitalReserves,CurrentAssets,CurrentLiabilities,CPFDAPDOIP,DEPS"
					+ ",DividendPayable,INVENTORY,LTL,NCA,NCFFOA,NCFFIA"
					+ ",NCFFFA,NIICACE,NetProfit,ONPATP,OperatingProfit,OperatingCost,OperatingIncome,OwnersEquity,RetainedProfits"
					+ ",ASSETS,LIABILITIES,TEATTSOPC,TOI,TotalProfit,TOC,InvestIncome,XJFlag,LRFlag,ZCFlag from financial";
			Statement stmt = con.createStatement();
			Statement stmt2 = con.createStatement();
			ResultSet rs = stmt.executeQuery(setSQL);
			StringBuilder sb = new StringBuilder();
			String insertSQL = "insert into stock_financial"
					+ "(`Code`,`Name`,`Year`,`Quarter`"
					+ ",`ReportDate`,`AR`,`BEPS`"
					+ ",CapitalReserves,`CurrentAssets`"
					+ ",`CurrentDebt`,`CPFDAPDOIP`,`DEPS`"
					+ ",`DividendPayable`,`Inventory`"
					+ ",`LongDebt`,`NCA`,`NCFFOA`,`NCFFIA`,`NCFFFA`"
					+ ",`NIICACE`,`NP`,`ONPATP`"
					+ ",`OperatingProfit`,`OperatingCost`"
					+ ",`OperatingIncome`,`OwnersEquity`,`RP`,`TotalAssets`,`TotalDebt`,"
					+ "TEATTSOPC,TOI,TotalProfit,TOC,Yield,XJFlag,LRFlag,ZCFlag) values ";
			int flag =0;
			while (rs.next()) {
				
				flag ++;
				if(flag%dataNum==1){
					sb = new StringBuilder();
					sb.append(insertSQL);
				}
				sb.append( "('" + rs.getString(1)
						+"','"+ rs.getString(2) 
						+"',"+ rs.getInt(3)
						+","+ rs.getInt(4)
						+",'"+ rs.getString(5)
						+"',"+ rs.getDouble(6)
						+","+ rs.getDouble(7)
						+","+ rs.getDouble(8)
						+","+ rs.getDouble(9)
						+","+ rs.getDouble(10)
						+","+ rs.getDouble(11)
						+","+ rs.getDouble(12)
						+","+ rs.getDouble(13)
						+","+ rs.getDouble(14)
						+","+ rs.getDouble(15)
						+","+ rs.getDouble(16)
						+","+ rs.getDouble(17)
						+","+ rs.getDouble(18)
						+","+ rs.getDouble(19)
						+","+ rs.getDouble(20)
						+","+ rs.getDouble(21)
						+","+ rs.getDouble(22)
						+","+ rs.getDouble(23)
						+","+ rs.getDouble(24)
						+","+ rs.getDouble(25)//营业收入
						+","+ rs.getDouble(26)
						+","+ rs.getDouble(27)
						+","+ rs.getDouble(28)
						+","+ rs.getDouble(29)
						+","+ rs.getDouble(30)
						+","+ rs.getDouble(31)//营业总收入（如果营业收入比营业总收入值大，则把营业收入值赋予营业总收入）
						+","+ rs.getDouble(32)
						+","+ rs.getDouble(33)
						+","+rs.getDouble(34)
						+","+rs.getInt(35)
						+","+rs.getInt(36)
						+","+rs.getInt(37)
						+"),");
				
				if(flag%dataNum==0){
					stmt2.executeUpdate(sb.toString().substring(0, sb.toString().length()-1));
					System.out.println("成功插入数量累计："+ flag );
				}
				
				
			}
			if(flag%dataNum!=0){
				stmt2.executeUpdate(sb.toString().substring(0, sb.toString().length()-1));
				System.out.println("成功插入数量累计："+ flag );
			}
			if(stmt!=null){
				stmt.close();
			}
			rs.close();
			stmt2.close();
			DBManager.connPoolH.returnConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("end!");
	}
}
