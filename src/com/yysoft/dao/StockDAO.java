package com.yysoft.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

import javax.management.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.yysoft.entity.Finance;
import com.yysoft.entity.FinancialMetaData;
import com.yysoft.entity.ReportNotice;
import com.yysoft.entity.Stock;
import com.yysoft.entity.Newsflash;
import com.yysoft.entity.StockDividends;
import com.yysoft.pool.DBManager;
import com.yysoft.util.Constant;
import com.yysoft.util.DateUtils;
import com.yysoft.util.InspectorValues;
import com.yysoft.util.StringUtils;

public class StockDAO {
	private static Logger logger = LogManager.getLogger(StockDAO.class); //
	private static Logger errorlogger = LogManager.getLogger("sqlErrorLoger"); //
	private static Logger loggerRT = LogManager.getLogger("fcRealTimeLoger"); //
	private static Logger loggerHis = LogManager.getLogger("fcHisLoger"); //
	private static Logger loggerDiff = LogManager.getLogger("fcDiffLoger"); //鍐呮伆鎬�
	static DecimalFormat df2 = new DecimalFormat("######0.00"); // 淇濈暀涓や綅灏忔暟
	static DecimalFormat df4 = new DecimalFormat("######0.0000"); // 淇濈暀鍥涗綅灏忔暟
	static DecimalFormat df6 = new DecimalFormat("######0.000000"); // 淇濈暀6浣嶅皬鏁�


	//
	public static ArrayList<Newsflash> getLast50Newsflash() {

		ArrayList<Newsflash> news = new ArrayList<Newsflash>();
		Newsflash newsflash = new Newsflash();

		try {
			Connection con = DBManager.connPoolH.getConnection();
			String sql = "select id,code,name,Title from stockwinweb.stock_web_newsflash order by publishdate desc limit 50   ";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				newsflash = new Newsflash();
				newsflash.setId(rs.getInt(1));
				newsflash.setCode(rs.getString(2));
				newsflash.setName(rs.getString(3));
				newsflash.setTitle(rs.getString(4));
				news.add(newsflash);
			}
			rs.close();
			stmt.close();
			DBManager.connPoolH.returnConnection(con);

		} catch (SQLException e) {
			errorlogger.error(e.getMessage(), e);
		}
		return news;
	}

	
	public static ArrayList<Stock> getStockCodes() {

		ArrayList<Stock> stocks = new ArrayList<Stock>();
		Stock stock = new Stock();

		try {
			Connection con = DBManager.connPoolH.getConnection();
			String sql = "select `id`,`name`,`code`,`sectionJRJ`,`sectionJRJCode`,`sectionTecent`,"
					+ "`sectionTecentCode`,`boardType`,`bourseType`,`concepts`,`city`,"
					+ "`generalCapital`,`componentType`,`dealType`,`liveType`,announcedDate "
					+ "from stock_company where componentType =1 and liveType =1";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				stock = new Stock();
				stock.setId(rs.getInt(1));
				stock.setName(rs.getString(2));
				stock.setCode(rs.getString(3));
				stock.setComponentType(rs.getInt(13));
				stock.setAnnouncedDate(rs.getString(16));
				stocks.add(stock);
			}
			rs.close();
			stmt.close();
			DBManager.connPoolH.returnConnection(con);

		} catch (SQLException e) {
			errorlogger.error(e.getMessage(), e);
		}
		return stocks;
	}
	/**
	 * Description:鏌ユ壘褰撳墠鎶ュ憡鏈熷唴灏氭湯閲囬泦璐㈡姤鐨勮偂绁�
	 * @param reportDate
	 * @return
	 */
	public static ArrayList<Stock> getStockCodesForGather(String reportDate) {

		ArrayList<Stock> stocks = new ArrayList<Stock>();
		Stock stock = new Stock();

		try {
			Connection con = DBManager.connPoolH.getConnection();
			String sql = "SELECT id,code,name,componentType from stock_company where componentType=1 and liveType =1 and code not in(SELECT code from financial where ReportDate='"+reportDate+"')";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				stock = new Stock();
				stock.setId(rs.getInt(1));
				stock.setCode(rs.getString(2));
				stock.setName(rs.getString(3));
				stock.setComponentType(rs.getInt(4));
				stocks.add(stock);
			}
			rs.close();
			stmt.close();
			DBManager.connPoolH.returnConnection(con);

		} catch (SQLException e) {
			errorlogger.error(e.getMessage(), e);
		}
		return stocks;
	}
	/**
	 * 鑾峰彇闇�瑕侀噰闆嗗巻鍙茶储鎶ユ暟鎹殑鑲＄エ浠ｇ爜 Description:
	 * 
	 * @return
	 */
	public static Vector<Stock> getNeedHistoryFinanceDataCodes() {

		Vector<Stock> stocks = new Vector<Stock>();
		Stock stock = new Stock();
		String insertTable = "financial";
		try {
			Connection con = DBManager.connPoolH.getConnection();
			String sql = "SELECT `code`, `name` from stock_company a WHERE componentType=1"
					+ " and liveType =1 and a.`code` not in (SELECT Code from " + insertTable + " GROUP BY Code)";
			//閲囬泦鍘嗗彶鍊煎緱sql
//			String sql = "SELECT `code`, `name` from stock_company a WHERE componentType=1"
//					+ " and liveType =1 ";
//			String sql = "SELECT code, name from financial where (DNNP IS NULL OR DNNPGR IS NULL or DNNPGR = '--' OR DNNP = '--') and reportdate >= '2016-12-31' and code in(SELECT code from stock_company where liveTYPE = 1 and dealType = 0 and componentType = 1)";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				stock = new Stock();
				stock.setCode(rs.getString(1));
				stock.setName(rs.getString(2));
				stocks.add(stock);
			}
			rs.close();
			stmt.close();
			DBManager.connPoolH.returnConnection(con);

		} catch (SQLException e) {
			errorlogger.error(e.getMessage(), e);
		}
		return stocks;
	}

	public static void insertNewsflashDB(ArrayList<Newsflash> newsflash){
	
		
	}
	
	
	/**
	 * Description:
	 * 
	 * @param finances
	 * @param isDelBefore鍦ㄦ彃鍏ュ墠鏄惁鍏堝垹闄わ紝閬垮厤閲嶅
	 */
	public static void insertDB(ArrayList<Finance> finances, boolean isDelBefore) {
		Connection con = null;
		try {
			con = DBManager.connPoolH.getConnection();
			Statement stmt = null;
			String insertTable = "financial";
			String insertTableLog = Constant.WEBDB + ".stock_financial_log";
			if (isDelBefore == true) {
				con.setAutoCommit(false);// 鏇存敼榛樿浜嬪姟鏂瑰紡,浠ユ柟渚� 鍚庨潰鐨勫垹闄ゆ搷浣�
			}
			stmt = con.createStatement();
			PreparedStatement ps = con
					.prepareStatement("insert into " + insertTable + "(Code,Name,ReportDate,PDE,DFP,NIIDFP,PPL,ICR"
							+ ",IFIB,FinancialExpenses,LTBAOFI,PFBAOFI,NIIPFBAOFI,CROAB,HTMI,NCFFFA,NCFDOFAIAAOLTA"
							+ ",NIFDOTFA,NCRFDOSAOBU,RefundableDeposits,StatutoryDeposits,BWOFI,NIIDWCBAOFI,INVENTORY"
							+ ",SUBD,CBD,DITL,DITA,TermDeposits,SAL,SAA,STB,STI,LAATC,CRFBI,NCL,NCA,EFRA,CPFDAPDOIP"
							+ ",LIABILITIES,LAOE,ProjectMaterials,IFCIFV,CPFPACOFAIAAOLTA,CPFGPASR,FixedAssets,DOFA"
							+ ",AdminExpenses,TCIATMS,ONPATP,TOEATP,TEATTSOPC,PreciousMetal,NIIRBC,FEG,FERFCOCACE"
							+ ",MonetaryCapital,BEPS,PCTR,RSOICR,RSOE,RSOCP,CIUPR,TRR,HFTFL,HFTFA,BWCC,NCFFOA"
							+ ",NetProfit,DevelopmentCosts,AFSFA,NIICBDADTBAOFIS,NIILAATC,TotalProfit,NII,InterestIncome"
							+ ",InterestExpenses,CurrentLiabilities,CurrentAssets,FAPUATR,ASUATR,ClaimsPaid,NCP,CEATBOTP"
							+ ",CEATEOTP,OEOC,ONCL,ONCA,OtherLiabilities,OCL,OCA,OOC,OOP,OOI,OtherPayables"
							+ ",OtherReceivables,OtherAssets,OCI,NIFSTBB,SOPIAAJV,LFDONCA,RPI,MembershipFees,CER"
							+ ",CustomerDeposits,TreasuryStock,PRFLI,NIFECAMB,DTBAOFI,UPR,ClaimReserves,CBDS,PRFLIRFR"
							+ ",UPRRFR,CRRFR,LTRFHIRFR,LTHIPR,NIFSUB,PledgeBorrowings,CRFMSIBS,DAPPTMSBS"
							+ ",BorrowingsReceived,IIR,NCPFAOSAOBU,GOODWILL,MSI,MII,PBA,PUC,OCRCOA,OCRCIA,TRRD,OCRCFA"
							+ ",PRFOIC,NCRFRB,CRFDOI,IHCACR,NHCACI,HCACI,HCACE,ITE,OwnersEquity,ERSOICR,ENCIICR,NCFFIA"
							+ ",InvestIncome,InvestmentPro,CPFAOI,SURRENDERS,FCTD,RetainedProfits,IntangibleAssets"
							+ ",CBDADTBAOFIS,CRBEI,CRFCC,DEPS,CABWCB,NIICACE,NIIPFOFI,BFCB,NIIBFCB,MarketingExpen"
							+ ",CRFSOGAROS,DFL,DFA,GAAE,GRP,NCLDWOY,NCADWOY,EarnedPremium,SurplusReserves,OperatingCost"
							+ ",OperatingProfit,OperatingIncome,BTAS,NOI,NOE,TOC,TOI,OPANP,PDPA,DTR,DividendPayable"
							+ ",InterestPayable,ClaimsPayable,NotesPayable,HCACP,BondsPayable,AccountsPayable,SRP"
							+ ",TaxesPayable,PremiumReceivables,SubrogationReceivables,RCRR,RFR,DividendReceivable"
							+ ",InterestReceivable,NotesReceivable,AccountsReceivable,OAGA,PREPAYMENTS"
							+ ",EstimatedLiabilities,PRIA,AdvanceReceipts,UCP,LTPE,LTL,LTEI,LTB,LTI,LTP,LTR,LTIOB,PDP"
							+ ",CPFTAS,CPTAFE,IHCACP,CPFOFA,CPFOOA,CPFOIA,OICCP,NIIPL,SpecialReserves,SpecialPayables"
							+ ",CapitalReserves,ADL,ASSETS,TCI,Quarter,Year,CDate,XJFlag,LRFlag,ZCFlag,NAPS,PRP,DNNP,DNNPGR) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
							+ ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
							+ ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
							+ ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
							+ ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
							+ ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement ps_log = con
					.prepareStatement("insert into " + insertTableLog + "(`Code`,`Name`,`ReportDate`,`JRJZCFlag`,`JRJXJFlag`,`JRJLRFlag`,`SINAZCFlag`"
							+ ",`SINAXJFlag`,`SINALRFlag`,`EASTZCFlag`,`EASTXJFlag`,`EASTLRFlag`,`LOG`"
							+ ") values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (Finance f : finances) {
				ps_log.setString(1, f.getFl().getCode());
				ps_log.setString(2, f.getFl().getName());
				ps_log.setString(3, f.getFl().getReportDate());
				ps_log.setInt(4, f.getFl().getJRJZCFlag());
				ps_log.setInt(5, f.getFl().getJRJXJFlag());
				ps_log.setInt(6, f.getFl().getJRJLRFlag());
				ps_log.setInt(7, f.getFl().getSINAZCFlag());
				ps_log.setInt(8, f.getFl().getSINAXJFlag());
				ps_log.setInt(9, f.getFl().getSINALRFlag());
				ps_log.setInt(10, f.getFl().getEASTZCFlag());
				ps_log.setInt(11, f.getFl().getEASTXJFlag());
				ps_log.setInt(12, f.getFl().getEASTLRFlag());
				ps_log.setString(13, f.getFl().getLOG());
				
				ps.setString(1, f.getCode());
				ps.setString(2, f.getName());
				ps.setString(3, f.getReportDate());
				ps.setString(4, f.getPDE());
				ps.setString(5, f.getDFP());
				ps.setString(6, f.getNIIDFP());
				ps.setString(7, f.getPPL());
				ps.setString(8, f.getICR());
				ps.setString(9, f.getIFIB());
				ps.setString(10, f.getFinancialExpenses());
				ps.setString(11, f.getLTBAOFI());
				ps.setString(12, f.getPFBAOFI());
				ps.setString(13, f.getNIIPFBAOFI());
				ps.setString(14, f.getCROAB());
				//String HTMI=(f.getHTMI()==null ||f.getHTMI().equals("")?"":Double.parseDouble(f.getHTMI())*10000+"");
				ps.setString(15,f.getHTMI());
				ps.setString(16, f.getNCFFFA());
				ps.setString(17, f.getNCFDOFAIAAOLTA());
				ps.setString(18, f.getNIFDOTFA());
				ps.setString(19, f.getNCRFDOSAOBU());
				ps.setString(20, f.getRefundableDeposits());
				ps.setString(21, f.getStatutoryDeposits());
				ps.setString(22, f.getBWOFI());
				ps.setString(23, f.getNIIDWCBAOFI());
				//String INVENTORY=( f.getINVENTORY()==null || f.getINVENTORY().equals("")?"":Double.parseDouble( f.getINVENTORY())*10000+"");
				ps.setString(24,f.getINVENTORY());
				ps.setString(25, f.getSUBD());
				ps.setString(26, f.getCBD());
				//String DITL=( f.getDITL()==null || f.getDITL().equals("")?"":Double.parseDouble( f.getDITL())*10000+"");
				ps.setString(27,f.getDITL());
				//String  DITA=(f.getDITA()==null ||f.getDITA().equals("")?"":Double.parseDouble(f.getDITA())*10000+"");
				ps.setString(28,f.getDITA());
				ps.setString(29, f.getTermDeposits());
				ps.setString(30, f.getSAL());
				ps.setString(31, f.getSAA());
				//String  STB=(f.getSTB()==null ||f.getSTB().equals("")?"":Double.parseDouble(f.getSTB())*10000+"");
				ps.setString(32, f.getSTB());
				ps.setString(33, f.getSTI());
				ps.setString(34, f.getLAATC());
				ps.setString(35, f.getCRFBI());
				//String NCL=(f.getNCL()==null ||f.getNCL().equals("")?"":Double.parseDouble(f.getNCL())*10000+"");
				ps.setString(36,f.getNCL());
				//String NCA=(f.getNCA()==null || f.getNCA().equals("")?"":Double.parseDouble(f.getNCA())*10000+"");
				ps.setString(37,f.getNCA());
				ps.setString(38, f.getEFRA());
				ps.setString(39, f.getCPFDAPDOIP());
				//String LIABILITIES=(f.getLIABILITIES()==null || f.getLIABILITIES().equals("")?"":Double.parseDouble(f.getLIABILITIES())*10000+"");
				ps.setString(40,f.getLIABILITIES());
				//String LOAE=(f.getLAOE()==null ||f.getLAOE().equals("")?"":Double.parseDouble(f.getLAOE())*10000+"");
				ps.setString(41,f.getLAOE());
				//String ProjectMaterials=(f.getProjectMaterials()==null || f.getProjectMaterials().equals("")?"":Double.parseDouble(f.getProjectMaterials())*10000+"");
				ps.setString(42,f.getProjectMaterials());
				ps.setString(43, f.getIFCIFV());
				ps.setString(44, f.getCPFPACOFAIAAOLTA());
				ps.setString(45, f.getCPFGPASR());
				//String FixedAssets=(f.getFixedAssets()==null ||f.getFixedAssets().equals("")?"":Double.parseDouble(f.getFixedAssets())*10000+"");
				ps.setString(46, f.getFixedAssets());
				//String DOFA=(f.getDOFA()==null ||f.getDOFA().equals("")?"":Double.parseDouble(f.getDOFA())*10000+"");
				ps.setString(47,f.getDOFA());
				ps.setString(48, f.getAdminExpenses());
				ps.setString(49, f.getTCIATMS());
				ps.setString(50, f.getONPATP());
				ps.setString(51, f.getTOEATP());
				//String TEATTSOPC=(f.getTEATTSOPC()==null || f.getTEATTSOPC().equals("")?"":Double.parseDouble(f.getTEATTSOPC())*10000+"");
				ps.setString(52, f.getTEATTSOPC());
				ps.setString(53, f.getPreciousMetal());
				ps.setString(54, f.getNIIRBC());
				ps.setString(55, f.getFEG());
				ps.setString(56, f.getFERFCOCACE());
				//String MonetaryCapital=(f.getMonetaryCapital()==null ||f.getMonetaryCapital().equals("")?"":Double.parseDouble(f.getMonetaryCapital())*10000+"");
				ps.setString(57,f.getMonetaryCapital());
				ps.setString(58, f.getBEPS());//涓嶆敼
				ps.setString(59, f.getPCTR());
				ps.setString(60, f.getRSOICR());
				ps.setString(61, f.getRSOE());
				ps.setString(62, f.getRSOCP());
				ps.setString(63, f.getCIUPR());
				ps.setString(64, f.getTRR());
				//String HFTFL=(f.getHFTFL()==null ||f.getHFTFL().equals("")?"":Double.parseDouble(f.getHFTFL())*10000+"");
				ps.setString(65,f.getHFTFL());
				//String HFTFA=(f.getHFTFA()==null ||f.getHFTFA().equals("")?"":Double.parseDouble(f.getHFTFA())*10000+"");
				ps.setString(66, f.getHFTFA());
				ps.setString(67, f.getBWCC());
				ps.setString(68, f.getNCFFOA());
				ps.setString(69, f.getNetProfit());
				ps.setString(70, f.getDevelopmentCosts());
				//String AFSFA=(f.getAFSFA()==null || f.getAFSFA().equals("")?"":Double.parseDouble(f.getAFSFA())*10000+"");
				ps.setString(71,f.getAFSFA());
				ps.setString(72, f.getNIICBDADTBAOFIS());
				ps.setString(73, f.getNIILAATC());
				ps.setString(74, f.getTotalProfit());
				ps.setString(75, f.getNII());
				ps.setString(76, f.getInterestIncome());
				ps.setString(77, f.getInterestExpenses());
				//String CurrentLiabilities=(f.getCurrentLiabilities()==null ||f.getCurrentLiabilities().equals("")?"":Double.parseDouble(f.getCurrentLiabilities())*10000+"");
				ps.setString(78,f.getCurrentLiabilities());
				//String CurrentAssets=(f.getCurrentAssets()==null ||f.getCurrentAssets().equals("")?"":Double.parseDouble(f.getCurrentAssets())*10000+"");
				ps.setString(79,f.getCurrentAssets());
				ps.setString(80, f.getFAPUATR());
				ps.setString(81, f.getASUATR());
				ps.setString(82, f.getClaimsPaid());
				ps.setString(83, f.getNCP());
				ps.setString(84, f.getCEATBOTP());
				ps.setString(85, f.getCEATEOTP());
				ps.setString(86, f.getOEOC());
				ps.setString(87, f.getONCL());
				//String ONCA=( f.getONCA()==null || f.getONCA().equals("")?"":Double.parseDouble( f.getONCA())*10000+"");
				ps.setString(88,f.getONCA());
				ps.setString(89, f.getOtherLiabilities());
				//String OCL=( f.getOCL()==null ||  f.getOCL().equals("")?"":Double.parseDouble( f.getOCL())*10000+"");
				ps.setString(90,f.getOCL());
				//String OCA=( f.getOCA()==null || f.getOCA().equals("")?"":Double.parseDouble(f.getOCA())*10000+"");
				ps.setString(91,f.getOCA());
				ps.setString(92, f.getOOC());
				ps.setString(93, f.getOOP());
				ps.setString(94, f.getOOI());
				//String OtherPayables=(f.getOtherPayables()==null || f.getOtherPayables().equals("")?"":Double.parseDouble(f.getOtherPayables())*10000+"");
				ps.setString(95,f.getOtherPayables());
				//String OtherReceivables=(f.getOtherReceivables()==null || f.getOtherReceivables().equals("")?"":Double.parseDouble(f.getOtherReceivables())*10000+"");
				ps.setString(96,f.getOtherReceivables());
				ps.setString(97, f.getOtherAssets());
				ps.setString(98, f.getOCI());
				ps.setString(99, f.getNIFSTBB());
				ps.setString(100, f.getSOPIAAJV());
				ps.setString(101, f.getLFDONCA());
				ps.setString(102, f.getRPI());
				ps.setString(103, f.getMembershipFees());
				ps.setString(104, f.getCER());
				ps.setString(105, f.getCustomerDeposits());
				ps.setString(106, f.getTreasuryStock());
				ps.setString(107, f.getPRFLI());
				ps.setString(108, f.getNIFECAMB());
				ps.setString(109, f.getDTBAOFI());
				ps.setString(110, f.getUPR());
				ps.setString(111, f.getClaimReserves());
				ps.setString(112, f.getCBDS());
				ps.setString(113, f.getPRFLIRFR());
				ps.setString(114, f.getUPRRFR());
				ps.setString(115, f.getCRRFR());
				ps.setString(116, f.getLTRFHIRFR());
				ps.setString(117, f.getLTHIPR());
				ps.setString(118, f.getNIFSUB());
				ps.setString(119, f.getPledgeBorrowings());
				ps.setString(120, f.getCRFMSIBS());
				ps.setString(121, f.getDAPPTMSBS());
				ps.setString(122, f.getBorrowingsReceived());
				ps.setString(123, f.getIIR());
				ps.setString(124, f.getNCPFAOSAOBU());
				//String GOODWILL=(f.getGOODWILL()==null ||f.getGOODWILL().equals("")?"":Double.parseDouble(f.getGOODWILL())*10000+"");
				ps.setString(125,f.getGOODWILL());
				ps.setString(126, f.getMSI());
				ps.setString(127, f.getMII());
				ps.setString(128, f.getPBA());
				//String PUC=(f.getPUC()==null || f.getPUC().equals("")?"":Double.parseDouble(f.getPUC())*10000+"");
				ps.setString(129,f.getPUC());
				ps.setString(130, f.getOCRCOA());
				ps.setString(131, f.getOCRCIA());
				ps.setString(132, f.getTRRD());
				ps.setString(133, f.getOCRCFA());
				ps.setString(134, f.getPRFOIC());
				ps.setString(135, f.getNCRFRB());
				ps.setString(136, f.getCRFDOI());
				ps.setString(137, f.getIHCACR());
				ps.setString(138, f.getNHCACI());
				ps.setString(139, f.getHCACI());
				ps.setString(140, f.getHCACE());
				ps.setString(141, f.getITE());
				//String OwnersEquity=(f.getOwnersEquity()==null ||f.getOwnersEquity().equals("")?"":Double.parseDouble(f.getOwnersEquity())*10000+"");
				ps.setString(142,f.getOwnersEquity());
				ps.setString(143, f.getERSOICR());
				ps.setString(144, f.getENCIICR());
				ps.setString(145, f.getNCFFIA());
				//String InvestIncome=(f.getInvestIncome()==null ||f.getInvestIncome().equals("")?"":Double.parseDouble(f.getInvestIncome())*10000+"");
				ps.setString(146, f.getInvestIncome());
				//String InvestmentPro=(f.getInvestmentPro()==null ||f.getInvestmentPro().equals("")?"":Double.parseDouble(f.getInvestmentPro())*10000+"");
				ps.setString(147,f.getInvestmentPro());
				ps.setString(148, f.getCPFAOI());
				ps.setString(149, f.getSURRENDERS());
				ps.setString(150, f.getFCTD());
				//String RetainedProfits=(f.getRetainedProfits()==null || f.getRetainedProfits().equals("")?"":Double.parseDouble(f.getRetainedProfits())*10000+"");
				ps.setString(151,f.getRetainedProfits());
				//String IntangibleAssets=( f.getIntangibleAssets()==null ||  f.getIntangibleAssets().equals("")?"":Double.parseDouble( f.getIntangibleAssets())*10000+"");
				ps.setString(152,f.getIntangibleAssets());
				ps.setString(153, f.getCBDADTBAOFIS());
				ps.setString(154, f.getCRBEI());
				ps.setString(155, f.getCRFCC());
				ps.setString(156, f.getDEPS());//涓嶆敼
				ps.setString(157, f.getCABWCB());
				ps.setString(158, f.getNIICACE());
				ps.setString(159, f.getNIIPFOFI());
				ps.setString(160, f.getBFCB());
				ps.setString(161, f.getNIIBFCB());
				ps.setString(162, f.getMarketingExpen());
				ps.setString(163, f.getCRFSOGAROS());
				ps.setString(164, f.getDFL());
				ps.setString(165, f.getDFA());
				ps.setString(166, f.getGAAE());
				ps.setString(167, f.getGRP());
				//String NCLDWOY=( f.getNCLDWOY()==null || f.getNCLDWOY().equals("")?"":Double.parseDouble( f.getNCLDWOY())*10000+"");
				ps.setString(168,f.getNCLDWOY());
				ps.setString(169, f.getNCADWOY());
				ps.setString(170, f.getEarnedPremium());
				//String SurplusReserves=(f.getSurplusReserves()==null || f.getSurplusReserves().equals("")?"":Double.parseDouble(f.getSurplusReserves())*10000+"");
				ps.setString(171, f.getSurplusReserves());
				ps.setString(172, f.getOperatingCost());
				ps.setString(173, f.getOperatingProfit());
				ps.setString(174, f.getOperatingIncome());
				ps.setString(175, f.getBTAS());
				ps.setString(176, f.getNOI());
				ps.setString(177, f.getNOE());
				ps.setString(178, f.getTOC());
				ps.setString(179, f.getTOI());
				ps.setString(180, f.getOPANP());
				ps.setString(181, f.getPDPA());
				ps.setString(182, f.getDTR());
				//String DividendPayable=(f.getDividendPayable()==null ||f.getDividendPayable().equals("")?"":Double.parseDouble(f.getDividendPayable())*10000+"");
				ps.setString(183,f.getDividendPayable());
				//String InterestPayable=(f.getInterestPayable()==null ||f.getInterestPayable().equals("")?"":Double.parseDouble(f.getInterestPayable())*10000+"");
				ps.setString(184,f.getInterestPayable());
				ps.setString(185, f.getClaimsPayable());
				//String NotesPayable=(f.getNotesPayable()==null || f.getNotesPayable().equals("")?"":Double.parseDouble(f.getNotesPayable())*10000+"");
				ps.setString(186,f.getNotesPayable());
				ps.setString(187, f.getHCACP());
				ps.setString(188, f.getBondsPayable());
				//String  AccountsPayable=(f.getAccountsPayable()==null || f.getAccountsPayable().equals("")?"":Double.parseDouble(f.getAccountsPayable())*10000+"");
				ps.setString(189, f.getAccountsPayable());
				//String  SRP=(f.getSRP()==null ||f.getSRP().equals("")?"":Double.parseDouble(f.getSRP())*10000+"");
				ps.setString(190,f.getSRP());
				ps.setString(191, f.getTaxesPayable());
				ps.setString(192, f.getPremiumReceivables());
				ps.setString(193, f.getSubrogationReceivables());
				ps.setString(194, f.getRCRR());
				ps.setString(195, f.getRFR());
				//String DividendReceivable=(f.getDividendReceivable()==null ||f.getDividendReceivable().equals("")?"":Double.parseDouble(f.getDividendReceivable())*10000+"");
				ps.setString(196,f.getDividendReceivable());
				//String  InterestReceivable=(f.getInterestReceivable()==null ||f.getInterestReceivable().equals("")?"":Double.parseDouble(f.getInterestReceivable())*10000+"");
				ps.setString(197, f.getInterestReceivable());
				//String NotesReceivable=( f.getNotesReceivable()==null || f.getNotesReceivable().equals("")?"":Double.parseDouble(f.getNotesReceivable())*10000+"");
				ps.setString(198,f.getNotesReceivable());
				//String  AccountsReceivable=(f.getAccountsReceivable()==null || f.getAccountsReceivable().equals("")?"":Double.parseDouble(f.getAccountsReceivable())*10000+"");
				ps.setString(199, f.getAccountsReceivable());
				ps.setString(200, f.getOAGA());
				ps.setString(201, f.getPREPAYMENTS());
				ps.setString(202, f.getEstimatedLiabilities());
				ps.setString(203, f.getPRIA());
				//String AdvanceReceipts=(f.getAdvanceReceipts()==null ||f.getAdvanceReceipts().equals("")?"":Double.parseDouble(f.getAdvanceReceipts())*10000+"");
				ps.setString(204,f.getAdvanceReceipts());
				//String  UCP=(f.getUCP()==null || f.getUCP().equals("")?"":Double.parseDouble(f.getUCP())*10000+"");
				ps.setString(205,f.getUCP());
				//String  LTPE=( f.getLTPE()==null || f.getLTPE().equals("")?"":Double.parseDouble( f.getLTPE())*10000+"");
				ps.setString(206,f.getLTPE());
				//String LTL=(f.getLTL()==null ||f.getLTL().equals("")?"":Double.parseDouble(f.getLTL())*10000+"");
				ps.setString(207,f.getLTL());
				//String LTEI=(f.getLTEI()==null || f.getLTEI().equals("")?"":Double.parseDouble(f.getLTEI())*10000+"");
				ps.setString(208,f.getLTEI());
				//String LTB=(f.getLTB()==null ||f.getLTB().equals("")?"":Double.parseDouble(f.getLTB())*10000+"");
				ps.setString(209,f.getLTB());
				//String LTI=( f.getLTI()==null || f.getLTI().equals("")?"":Double.parseDouble(f.getLTI())*10000+"");
				ps.setString(210,f.getLTI());
				//String LTP=( f.getLTP()==null || f.getLTP().equals("")?"":Double.parseDouble( f.getLTP())*10000+"");
				ps.setString(211,f.getLTP());
				//String LTR=( f.getLTR()==null ||f.getLTR().equals("")?"":Double.parseDouble( f.getLTR())*10000+"");
				ps.setString(212, f.getLTR());
				ps.setString(213, f.getLTIOB());
				ps.setString(214, f.getPDP());
				ps.setString(215, f.getCPFTAS());
				ps.setString(216, f.getCPTAFE());
				ps.setString(217, f.getIHCACP());
				ps.setString(218, f.getCPFOFA());
				ps.setString(219, f.getCPFOOA());
				ps.setString(220, f.getCPFOIA());
				ps.setString(221, f.getOICCP());
				ps.setString(222, f.getNIIPL());
				ps.setString(223, f.getSpecialReserves());
				//String SpecialPayables=(f.getSpecialPayables()==null || f.getSpecialPayables().equals("")?"":Double.parseDouble(f.getSpecialPayables())*10000+"");
				ps.setString(224, f.getSpecialPayables());
				//String CapitalReserves=(f.getCapitalReserves()==null ||f.getCapitalReserves().equals("")?"":Double.parseDouble(f.getCapitalReserves())*10000+"");
				ps.setString(225,f.getCapitalReserves() );
				ps.setString(226, f.getADL());
				//String ASSETS=( f.getASSETS()==null || f.getASSETS().equals("")?"":Double.parseDouble( f.getASSETS())*10000+"");
				ps.setString(227,f.getASSETS());
				ps.setString(228, f.getTCI());
				ps.setString(229, f.getQuarter());
				ps.setString(230, f.getYear());
				ps.setString(231, DateUtils.formatDate(new Date()));
				ps.setInt(232, f.getXJFlag());
				ps.setInt(233, f.getLRFlag());
				ps.setInt(234, f.getZCFlag());
				ps.setString(235, f.getNAPS());
				ps.setString(236, f.getPRP());
				/*
				if(f.getDNNP().equals("--")){
					f.setDNNP("0");
				}
				if(f.getDNNPGR().equals("--")){
					f.setDNNPGR("0");
				}
				*/
				ps.setString(237, f.getDNNP());
				ps.setString(238, f.getDNNPGR());
				if (isDelBefore == true) {
					// 鍒ゆ柇鏄惁鎴愬姛閲囬泦鍒版暟鎹�
					if(f.getIsCapture()==1){
						//check
						InspectorValues.check(f);
						// del old data
						stmt.executeUpdate("delete from " + insertTable + " where code='" + f.getCode()
								+ "' and ReportDate='" + f.getReportDate() + "'");
						if (ps.executeUpdate() > -1) {
							loggerRT.info(f.getCode() + ",骞翠唤-瀛ｅ害锛�" + f.getYear() + "-" + f.getQuarter() + ",鎴愬姛閲囬泦缁撴灉鍐欏叆鏁版嵁搴擄紒" );
						}
					}
					if (ps_log.executeUpdate()>-1) {
						loggerRT.info(f.getCode() + ",骞翠唤-瀛ｅ害锛�" + f.getYear() + "-" + f.getQuarter() + ",鎴愬姛閲囬泦鏃ュ織鍐欏叆鏁版嵁搴擄紒" );
					}
					con.commit();// 鎻愪氦浜嬪姟

				} else {
					if(f.getIsCapture()==1){
						//check,鍐呮伆鎬ф鏌�
						InspectorValues.check(f);
						if (ps.executeUpdate() > -1) {
							loggerHis.info(f.getCode() + ",骞翠唤-瀛ｅ害锛�" + f.getYear() + "-" + f.getQuarter() + ",鎴愬姛閲囬泦缁撴灉鍐欏叆鏁版嵁搴擄紒" );
						}
					}
					if (ps_log.executeUpdate()>-1) {
						loggerHis.info(f.getCode() + ",骞翠唤-瀛ｅ害锛�" + f.getYear() + "-" + f.getQuarter() + ",鎴愬姛閲囬泦鏃ュ織鍐欏叆鏁版嵁搴擄紒" );
					}
				}
			}
			if (stmt != null) {
				stmt.close();
			}
			ps.close();
			ps_log.close();
			
		} catch (SQLException e) {
			errorlogger.error(e.getMessage(), e);
			try {
				// 鍥炴粴銆佸彇娑堝墠杩版搷浣�
				con.rollback();
			} catch (Exception e1) {
				errorlogger.error(e1.getMessage(), e1);
			}
		} finally {
			try {
				if (con != null) {
					con.setAutoCommit(true);// 鎭㈠浜嬪姟鐨勯粯璁ゆ彁浜ゆ柟寮�
					DBManager.connPoolH.returnConnection(con);
				}
			} catch (Exception e1) {
				errorlogger.error(e1.getMessage(), e1);
			}
		}
	}

	public static ArrayList<ReportNotice> getJRJCodesTab() {

		ArrayList<ReportNotice> rns = new ArrayList<ReportNotice>();
		ReportNotice rn = new ReportNotice();

		try {
			Connection con = DBManager.connPoolH.getConnection();
			String sql = "select `id`,`name`,`code`,`year`,`quarter`,`title`,"
					+ "`publishDate`,num from stock_financial_gather_monitor";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				rn = new ReportNotice();
				rn.setId(rs.getInt(1));
				rn.setName(rs.getString(2));
				rn.setCode(rs.getString(3));
				rn.setYear(rs.getInt(4));
				rn.setQuarter(rs.getInt(5));
				rn.setTitle(rs.getString(6));
				rn.setPublishDate(rs.getString(7));
				rn.setNum(rs.getInt(8));
				rns.add(rn);
			}
			rs.close();
			stmt.close();
			DBManager.connPoolH.returnConnection(con);

		} catch (SQLException e) {
			errorlogger.error(e.getMessage(), e);
		}
		return rns;
	}



	public static void updateMonitorPage(HashSet<ReportNotice> succGathers){
		
		try {
			Connection con = DBManager.connPoolH.getConnection();
			String updateSQL ="";
			Statement stmt = con.createStatement();
			for(ReportNotice rn:succGathers){
				if(rn.getNum()==0){//绗�1娆￠噰闆�
					updateSQL = "insert into stock_financial_gather_monitor" + " (cdate,code,name,year,quarter,title,publishDate,source) values(curdate(),'"
							+ rn.getCode() + "','" + rn.getName() + "'," + rn.getYear() + "," + rn.getQuarter() + ",'"
							+ rn.getTitle() + "','" + rn.getPublishDate() + "'," + rn.getSource() +  ")";
					stmt.executeUpdate(updateSQL);
				}else{
					//绗簩娆￠噰闆�
				}
			}
			//鏍囪涓虹浜屾閲囬泦
			updateSQL  = "UPDATE stock_financial_gather_monitor set num=2 where num=1";
			errorlogger.info(  "updateSQL:"+updateSQL);
			stmt.executeUpdate(updateSQL);
			stmt.close();
			DBManager.connPoolH.returnConnection(con);

		} catch (SQLException e) {
			errorlogger.error(e.getMessage(), e);
		}	
	}
	
	
	public static ArrayList<Finance> getNewMetaFinance() {

		ArrayList<Finance> fis = new ArrayList<Finance>();
		Finance fi = new Finance();

		try {
			Connection con = DBManager.connPoolH.getConnection();
			String sql = "select `Code`,`Name`,`Year`,`Quarter`,`ReportDate`,CurrentAssets"
					+ ",ASSETS,DividendPayable,CurrentLiabilities,LIABILITIES,TEATTSOPC,OwnersEquity"
					+ ",TOI,OperatingIncome,InvestIncome,OperatingProfit,TotalProfit,NetProfit"
					+ ",ONPATP,BEPS,DEPS,NCFFOA,CPFDAPDOIP,LTL,OperatingCost,TOC,INVENTORY"
					+ ",AccountsReceivable,NIICACE,CDate,ID,RetainedProfits,XJFlag,LRFlag,ZCFlag,CapitalReserves,NCA,NCFFIA,NCFFFA,NAPS,PRP,DNNP,DNNPGR from financial where PUBLISH =0  ORDER BY CDate ASC";
//			String sql = "select `Code`,`Name`,`Year`,`Quarter`,`ReportDate`,CurrentAssets"
//					+ ",ASSETS,DividendPayable,CurrentLiabilities,LIABILITIES,TEATTSOPC,OwnersEquity"
//					+ ",TOI,OperatingIncome,InvestIncome,OperatingProfit,TotalProfit,NetProfit"
//					+ ",ONPATP,BEPS,DEPS,NCFFOA,CPFDAPDOIP,LTL,OperatingCost,TOC,INVENTORY"
//					+ ",AccountsReceivable,NIICACE,CDate,ID,RetainedProfits,XJFlag,LRFlag,ZCFlag,CapitalReserves,NCA,NCFFIA,NCFFFA,NAPS,PRP,DNNP,DNNPGR from financial where (DNNP IS NOT NULL or DNNPGR IS NOT NULL) and " 
//					+ "code in (SELECT code from stock_financial where (DNNP is NULL or DNNPGR is null ) and (reportdate = '2017-12-31' or reportdate = '2017-09-30' or reportdate = '2017-06-30' or reportdate = '2017-03-31' or reportdate = '2016-12-31')) ORDER BY CDate ASC";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				fi = new Finance();
				fi.setCode(rs.getString(1));
				fi.setName(rs.getString(2));
				fi.setYear(rs.getString(3));
				fi.setQuarter(rs.getString(4));
				fi.setReportDate(rs.getString(5));
				fi.setCurrentAssets(rs.getString(6));
				fi.setASSETS(rs.getString(7));
				fi.setDividendPayable(rs.getString(8));
				fi.setCurrentLiabilities(rs.getString(9));
				fi.setLIABILITIES(rs.getString(10));
				fi.setTEATTSOPC(rs.getString(11));
				fi.setOwnersEquity(rs.getString(12));
				fi.setTOI(rs.getString(13));
				fi.setOperatingIncome(rs.getString(14));
				fi.setInvestIncome(rs.getString(15));
				fi.setOperatingProfit(rs.getString(16));
				fi.setTotalProfit(rs.getString(17));
				fi.setNetProfit(rs.getString(18));
				fi.setONPATP(rs.getString(19));
				fi.setBEPS(rs.getString(20));
				fi.setDEPS(rs.getString(21));
				fi.setNCFFOA(rs.getString(22));
				fi.setCPFDAPDOIP(rs.getString(23));
				fi.setLTL(rs.getString(24));
				fi.setOperatingCost(rs.getString(25));
				fi.setTOC(rs.getString(26));
				fi.setINVENTORY(rs.getString(27));
				fi.setAccountsReceivable(rs.getString(28));
				fi.setNIICACE(rs.getString(29));
				fi.setCDate(rs.getString(30));
				fi.setID(rs.getInt(31));
				fi.setRetainedProfits(rs.getString(32));
				fi.setXJFlag(rs.getInt(33));
				fi.setLRFlag(rs.getInt(34));
				fi.setZCFlag(rs.getInt(35));
				fi.setCapitalReserves(rs.getString(36));
				fi.setNCA(rs.getString(37));
				fi.setNCFFIA(rs.getString(38));
				fi.setNCFFFA(rs.getString(39));
				fi.setNAPS(rs.getString(40));
				fi.setPRP(rs.getString(41));
				fi.setDNNP(rs.getString(42));
				fi.setDNNPGR(rs.getString(43));
				fis.add(fi);
			}
			rs.close();
			stmt.close();
			DBManager.connPoolH.returnConnection(con);

		} catch (SQLException e) {
			errorlogger.error(e.getMessage(), e);
		}
		return fis;
	}

	/**
	 * Description:
	 * @param fi
	 * @return 0-鎻掑叆澶辫触锛�1-鎴愬姛鎻掑叆鏂版暟鎹��2-鎴愬姛鎻掑叆骞舵浛鎹㈡棫璁板綍
	 */
	public static int importToDCFinance(Finance fi) {
		Statement st = null;
		Connection con = null;
		String table ="stock_financial";
		int flag = 0;
		
		//同步时临时添加  niuguanqun
		if(fi.getReportDate() == null){
			return flag = -1;
		}
		//同步时临时添加  niuguanqun
		if(fi.getDNNPGR() == null || fi.getDNNPGR().equals("--")){
			fi.setDNNPGR("-1");
		}
		/*
		if(fi.getDNNPGR().equals("null")){
			fi.setDNNPGR("0");
		}
		
		if(fi.getDNNP().equals("null")){
			fi.setDNNP("0");
		}
		*/
		
		//06.03.11wilson瑙勫畾锛岃惀鏀跺拰钀ユ�绘敹锛岄偅涓�兼渶澶у氨鍒掑綊涓鸿惀鎬绘敹鐨勬渶缁堝�硷紝璁＄畻鏃跺彇钀ユ�绘敹鐨勫��
		if(StringUtils.getDouble6(fi.getOperatingIncome())>StringUtils.getDouble6(fi.getTOI())){
			fi.setTOI(fi.getOperatingIncome());
		}
		
		try {
			con = DBManager.connPoolH.getConnection();
			con.setAutoCommit(false);//鏇存敼榛樿浜嬪姟鏂瑰紡
			st = con.createStatement();
			String delSQL = "delete from " +table +" where code='" + fi.getCode() + "' and reportDate='" + fi.getReportDate() +"'" ;
			String sql = "insert into " + table 
					+ "(`Code`,`Name`,`Year`,`Quarter`,`ReportDate`,`AR`,`BEPS`"
					+ ",CapitalReserves,`CurrentAssets`,`CurrentDebt`,`CPFDAPDOIP`,`DEPS`"
					+ ",`DividendPayable`,`Inventory`,`LongDebt`,`NCA`,`NCFFOA`,`NCFFIA`"
					+ ",`NCFFFA`,`NIICACE`,`NP`,`ONPATP`"
					+ ",`OperatingProfit`,`OperatingCost`,`OperatingIncome`,`OwnersEquity`"
					+ ",`RP`,`TotalAssets`,`TotalDebt`,TEATTSOPC,TOI,TotalProfit,TOC,Yield,XJFlag,LRFlag,ZCFlag,NAPS,PRP,DNNP,DNNPGR) values('" + fi.getCode() + "','" + fi.getName() + "',"
					+ Integer.valueOf(fi.getYear()) + "," + Integer.valueOf(fi.getQuarter()) + ",'" + fi.getReportDate() + "',"
					+ StringUtils.getDouble6(fi.getAccountsReceivable()) + "," + StringUtils.getDouble6(fi.getBEPS())  + ","
					+ StringUtils.getDouble6(fi.getCapitalReserves()) + "," 
					+ StringUtils.getDouble6(fi.getCurrentAssets()) + "," + StringUtils.getDouble6(fi.getCurrentLiabilities()) + ","
					+ StringUtils.getDouble6(fi.getCPFDAPDOIP()) + "," + StringUtils.getDouble6(fi.getDEPS()) + ","
					+ StringUtils.getDouble6(fi.getDividendPayable()) + "," + StringUtils.getDouble6(fi.getINVENTORY()) + "," 
					+ StringUtils.getDouble6(fi.getLTL()) + "," + StringUtils.getDouble6(fi.getNCA()) + ","
					+ StringUtils.getDouble6(fi.getNCFFOA()) + "," + StringUtils.getDouble6(fi.getNCFFIA()) + ","
					+ StringUtils.getDouble6(fi.getNCFFFA()) + "," + StringUtils.getDouble6(fi.getNIICACE()) + "," 
					+ StringUtils.getDouble6(fi.getNetProfit()) + "," + StringUtils.getDouble6(fi.getONPATP()) + "," 
					+ StringUtils.getDouble6(fi.getOperatingProfit()) + "," + StringUtils.getDouble6(fi.getOperatingCost()) + ","
					+ StringUtils.getDouble6(fi.getOperatingIncome()) + "," + StringUtils.getDouble6(fi.getOwnersEquity()) + "," 
					+ StringUtils.getDouble6(fi.getRetainedProfits()) + "," + StringUtils.getDouble6(fi.getASSETS()) + ","
					+ StringUtils.getDouble6(fi.getLIABILITIES()) + "," + StringUtils.getDouble6(fi.getTEATTSOPC()) + ","
					+ StringUtils.getDouble6(fi.getTOI()) + "," + StringUtils.getDouble6(fi.getTotalProfit()) + "," 
					+ StringUtils.getDouble6(fi.getTOC())  + "," + StringUtils.getDouble6(fi.getInvestIncome()) + "," 
					+ Integer.valueOf(fi.getXJFlag()) + "," + Integer.valueOf(fi.getLRFlag()) + ","+ Integer.valueOf(fi.getZCFlag()) +","+StringUtils.getDouble6(fi.getNAPS())+","+StringUtils.getDouble6(fi.getPRP())+","
					+ StringUtils.getDouble6(fi.getDNNP())+ "," + StringUtils.getDouble6(fi.getDNNPGR()) + ")";
			
			
			sql = sql.replace("--", "-1");
			flag += st.executeUpdate(delSQL);
			flag += st.executeUpdate(sql);
			
			con.commit();//鎻愪氦浜嬪姟
			con.setAutoCommit(true);// 鎭㈠浜嬪姟鐨勯粯璁ゆ彁浜ゆ柟寮� 
			
			if (st != null) {
				st.close();
			}
		} catch (MySQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBManager.connPoolH.returnConnection(con);
		}
		return flag;
	}
	
	
	public static void updatePublishFinance(int id) {
		Statement st = null;
		Connection con = null;
		try {
			con = DBManager.connPoolH.getConnection();
			st = con.createStatement();
			String sql = "update financial set PUBLISH=1 where ID=" + id;
			 st.executeUpdate(sql);

			if (st != null) {
				st.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.connPoolH.returnConnection(con);
		}
	}
	
 
	
	/**
	 * Description:stock_financial_gather_basic 闇�瑕侀噸閲囩殑鑲＄エ锛坣um=1锛�
	 *
	 */
	public static HashSet<ReportNotice> reGather() {
		HashSet<ReportNotice> oldCodes = new HashSet<ReportNotice>();
		ReportNotice rn = null;
		Statement st = null;
		Connection con = null;
		try {
			con = DBManager.connPoolH.getConnection();
			st = con.createStatement();
			String sql = "SELECT id,cdate,`code`,`name`,`year`,`quarter`,title,publishDate,num from stock_financial_gather_monitor where  num =1 and  cdate>=(SELECT MAX(cdate) from stock_financial_gather_monitor)" ;
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				rn = new ReportNotice();
				rn.setId(rs.getInt(1));
				rn.setCdate(rs.getString(2));
				rn.setCode(rs.getString(3));
				rn.setName(rs.getString(4));
				rn.setYear(rs.getInt(5));
				rn.setQuarter(rs.getInt(6));
				rn.setTitle(rs.getString(7));
				rn.setPublishDate(rs.getString(8));
				rn.setNum(rs.getInt(9));
				oldCodes.add(rn);
			}
			if (st != null) {
				st.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.connPoolH.returnConnection(con);
		}
		return oldCodes;
	}
	
	/**
	 * Description:鏇存柊琛╯tock_compnay涓偂绁ㄧ畝鍐欏拰棣栧瓧姣嶇缉鍐�
	 *
	 */
	public static void updateAcronym(String name,String acronym,String code) {
		Statement st = null;
		Connection con = null;
		String table1 = Constant.DCDB+".stock_company";
		String table2 = Constant.WEBDB+".stock_company";
		try {
			con = DBManager.connPoolH.getConnection();
			st = con.createStatement();
			String sql = "update " + table1 + " set name='" + name + "',acronym='" + acronym +"' where componentType=1 and code='" +code +"'" ;
			
			st.executeUpdate(sql);
			sql = "update " + table2 + " set name='" + name + "',acronym='" + acronym +"' where componentType=1 and code='" +code +"'" ;
			st.executeUpdate(sql);
			if (st != null) {
				st.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.connPoolH.returnConnection(con);
		}
	}
	/**
	 * stock_dividends涓殑鎵�鏈夎偂绁ㄦ暟鎹�
	 * 
	 * @author bxsun
	 */
	public static ArrayList<StockDividends> getStockDividendsCode() {

		ArrayList<StockDividends> stocks = new ArrayList<StockDividends>();
		StockDividends stock = new StockDividends();
		StringBuffer sb = new StringBuffer();

		try {
			Connection con = DBManager.connPoolH.getConnection();
			/*String sql = "select a.`id`,a.`name`,a.`code`,a.`year`,a.`quarter`,a.`recordDate`,a.`dividendPerShare`,"
					+ "a.`dividendPlan`,a.`dividendPlanFlag`,a.`dividendPlanAbstract`,a.`dividendPlanIncrease`,"
					+ "a.`stock_total`,a.`reportDate`,a.announDate," 
					+ " from stock_dividends a,stock_company b where a.`code`=b.`code` and b.componentType=1 and a.reportDate='2015-12-31'";*/
			String sql="select `id`,`name`,`code`,`year`,`quarter`,`recordDate`,`dividendPerShare`,"
					+ "`dividendPlan`,`dividendPlanFlag`,`dividendPlanAbstract`,`dividendPlanIncrease`,"
					+ "`stock_total`,`reportDate`,announDate from stock_dividends where reportDate='2017-12-31' or reportDate='2016-12-31'";
			Statement stmt = con.createStatement();
			sb.append(sql);

			sql = sb.toString();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				stock = new StockDividends();
				stock.setName(rs.getString(2));
				stock.setCode(rs.getString(3));
				stock.setYear(rs.getInt(4));
				stock.setQuarter(rs.getInt(5));
				stock.setRecordDate(rs.getString(6));
				stock.setDividendPerShare(rs.getDouble(7));
				stock.setDividendPlan(rs.getString(8));
				stock.setDividendPlanFlag(rs.getInt(9));
				stock.setDividendPlanAbstract(rs.getString(10));
				stock.setDividendPlanIncrease(rs.getString(11));
				stock.setStock_total(rs.getDouble(12));
				stock.setReportDate(rs.getString(13));
				stock.setAnnounDate(rs.getString(14));
				stocks.add(stock);
			}
			rs.close();
			stmt.close();
			DBManager.connPoolH.returnConnection(con);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stocks;
	}
	
	/**
	 * 鑾峰彇鍚屾湡涓�,stock_dividends涓殑鎵�鏈夎偂绁ㄦ暟鎹�
	 * 
	 * @author bxsun
	 */
	public static ArrayList<StockDividends> getStockDividendsCode(int year, int quarter) {

		ArrayList<StockDividends> stocks = new ArrayList<StockDividends>();
		StockDividends stock = new StockDividends();
		StringBuffer sb = new StringBuffer();

		try {
			Connection con = DBManager.connPoolH.getConnection();
			String sql = "select `id`,`name`,`code`,`year`,`quarter`,`recordDate`,`dividendPerShare`,`dividendPlan`,`dividendPlanFlag` from stock_dividends where year="
					+ year + " and quarter=" + quarter + " ";
			Statement stmt = con.createStatement();
			sb.append(sql);

			sql = sb.toString();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				stock = new StockDividends();
				stock.setName(rs.getString(2));
				stock.setCode(rs.getString(3));
				stock.setRecordDate(rs.getString(6));
				stock.setDividendPerShare(rs.getDouble(7));
				stocks.add(stock);
			}
			rs.close();
			stmt.close();
			DBManager.connPoolH.returnConnection(con);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stocks;
	}
	
	/**
	 * 鏌ヨstock_capital_changetime琛ㄤ腑瀵瑰簲code鍜宒ate鎸夐檷搴忔帓鍒楃殑鑲℃湰鎬绘暟
	 * 
	 */
	public static double getStockTotal(String code, String date) {
		double stockTotal = 0;
		StringBuffer sb = new StringBuffer();
		try {
			Connection con = DBManager.connPoolH.getConnection();
			String sql = "select stock_total from stock_capital_changetime where code='" + code + "' and date<='" + date
					+ "' and stock_total>0 ORDER BY date DESC LIMIT 1";
			Statement stmt = con.createStatement();
			sb.append(sql);
			sql = sb.toString();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				stockTotal = rs.getDouble(1);
			}
			rs.close();
			stmt.close();
			DBManager.connPoolH.returnConnection(con);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stockTotal;
	}

	/**
	 * 鏌ヨstock_dividends琛ㄤ腑瀵瑰簲code鍜宒ate鐨勮偂鏈�绘暟
	 * 
	 */
	public static double getStockTotal2(String code, String date) {
		double stockTotal = 0;
		StringBuffer sb = new StringBuffer();
		try {
			Connection con = DBManager.connPoolH.getConnection();
			String sql = "select stock_total from stock_dividends where code='" + code + "' and reportDate='" + date
					+ "'";
			Statement stmt = con.createStatement();
			sb.append(sql);
			sql = sb.toString();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				stockTotal = rs.getDouble(1);
			}
			rs.close();
			stmt.close();
			DBManager.connPoolH.returnConnection(con);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stockTotal;
	}
	/**
	 * Description:鑾峰彇鑲＄エ
	 * 
	 * @param boardType
	 *            0涓轰富鏉�,1涓哄垱涓氭澘
	 * @param componentType
	 *            1-涓偂锛�0-澶х洏鎸囨暟
	 * @param dealType
	 *            1-鍋滅墝锛�0-宸插鐗岋紝姝ｅ父浜ゆ槗
	 * @param liveType
	 *            1-涓婂競锛�0-閫�甯�
	 * @param bourseType
	 *            0-娣卞競,1-娌競
	 * @return
	 */
	public static HashSet<Stock> getStockCode(Integer boardType, Integer componentType, Integer dealType,
			Integer liveType, Integer bourseType) {

		HashSet<Stock> stocks = new HashSet<Stock>();
		Stock stock = new Stock();
		StringBuffer sb = new StringBuffer();

		try {
			Connection con = DBManager.connPoolH.getConnection();
			String sql = "select `id`,`name`,`code`,`sectionJRJ`,`sectionJRJCode`,`sectionTecent`,`sectionTecentCode`,`boardType`,`bourseType`,`concepts`,`city`,`generalCapital`,`componentType`,`dealType`,`liveType`,`announcedDate` from stock_company where 1=1 ";
			Statement stmt = con.createStatement();
			sb.append(sql);
			if (boardType != null) {
				sb.append(" and boardType =" + boardType);
			}
			if (componentType != null) {
				sb.append(" and componentType =" + componentType);
			}
			if (dealType != null) {
				sb.append(" and dealType =" + dealType);
			}
			if (liveType != null) {
				sb.append(" and liveType =" + liveType);
			}
			if (bourseType != null) {
				sb.append(" and bourseType =" + bourseType);
			}
			sql = sb.toString();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				stock = new Stock();
				stock.setName(rs.getString(2));
				stock.setCode(rs.getString(3));
				stock.setComponentType(rs.getInt(13));
				stock.setAnnouncedDate(rs.getString(16));
				stocks.add(stock);
			}
			rs.close();
			stmt.close();
			DBManager.connPoolH.returnConnection(con);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stocks;
	}
	/**
	 * Description:鑾峰彇涓偂鐨勬墍鏈変笂甯傛垨閫�甯傚叕鍙镐俊鎭�
	 * 
	 * @param componentType
	 *            1-涓偂锛�0-澶х洏鎸囨暟
	 * @return
	 */
	public static HashSet<Stock> getStockCode(Integer componentType, Integer liveType) {
		return getStockCode(null, componentType, null, liveType, null);
	}

	/**
	 * 鎵цsql
	 * 
	 * @return
	 */
	public static void runSql(String sql) {

		try {
			Connection con = DBManager.connPoolH.getConnection();
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			DBManager.connPoolH.returnConnection(con);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//鏍规嵁鑲＄エ浠ｇ爜鍜屾姤鍛婃湡鑾峰彇
		public static StockDividends getDividend(String code,String reportDate){
			StockDividends stockDivi=null;
			StringBuffer sb = new StringBuffer();
			try {
				Connection con = DBManager.connPoolH.getConnection();
				String sql = "select dividendPerShare,recordDate from stock_dividends where code='"+code+"' and reportDate='"+reportDate+"'";
				Statement stmt = con.createStatement();
				sb.append(sql);
				sql = sb.toString();
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					stockDivi=new StockDividends();
					stockDivi.setDividendPerShare(rs.getDouble(1));
					stockDivi.setRecordDate(rs.getString(2));
				}
				rs.close();
				stmt.close();
				DBManager.connPoolH.returnConnection(con);

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return stockDivi;
		} 

		/*
		 * 閲囬泦鐨勮偂绁ㄦ槸鍚﹀瓨鍦ㄨ储鎶ヨ〃涓�
		 */
		public static boolean getFinancial(String code, String reportDate) {
			boolean flag = false;
			StringBuffer sb = new StringBuffer();
			try {
				Connection con = DBManager.connPoolH.getConnection();
				String sql = "select count(*) from stock_financial where Code='" + code + "' and ReportDate='" + reportDate
						+ "'";
				Statement stmt = con.createStatement();
				sb.append(sql);
				sql = sb.toString();
				ResultSet rs = stmt.executeQuery(sql);
				int count = 0;
				while (rs.next()) {
					count = rs.getInt(1);
					if (count > 0) {
						flag = true;
					}
				}
				rs.close();
				stmt.close();
				DBManager.connPoolH.returnConnection(con);

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return flag;
		}
		
		/**
		 * 鏌ヨstock_capital_changetime琛ㄤ腑瀵瑰簲code鍜宒ate鎸夐檷搴忔帓鍒楃殑鑲℃湰鎬绘暟
		 * 
		 */
		public static double getStockTotalT(String code, String date) {
			double stockTotal = 0;
			StringBuffer sb = new StringBuffer();
			try {
				Connection con = DBManager.connPoolH.getConnection();
				String sql = "select stock_total from stock_capital_changetime where code='" + code
						+ "' ORDER BY date ASC LIMIT 1";
				Statement stmt = con.createStatement();
				sb.append(sql);
				sql = sb.toString();
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					stockTotal = rs.getDouble(1);
				}
				rs.close();
				stmt.close();
				DBManager.connPoolH.returnConnection(con);

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return stockTotal;
		}
		
		/**
		 * lu 灏佽stock_financial琛ㄤ笂甯傚叕鍙告墍鏈夎储鍔℃暟鎹�,鍖呮嫭瀵瑰簲琛屼笟浠ｇ爜锛屽競鍊硷紝鑲℃湰 锛屾敹鐩樹环
		 * 
		 * @param beginYear
		 *            鏌ヨ璐㈡姤鐨勮捣濮嬪勾闄�
		 * @return
		 */
		public static ArrayList<FinancialMetaData> getMetaFinancial(int beginYear) {
			String icodeColumnCode = Constant.IColumnCode;
			String inameColumnName = Constant.IColumnName;

			String sql = "SELECT stock_price.`close`,stock_price.market_value,stock_price.stock_total,stock_price.a_traded_stock_total"
					+ ",stock_price.traded_market_value,stock_financial.`Code`,stock_financial.`Name`,stock_financial.ID"
					+ ",stock_financial.`Year`,stock_financial.`Quarter`,stock_financial.ReportDate"
					+ ",stock_financial.AR,stock_financial.BEPS,stock_financial.CapitalReserves"
					+ ",stock_financial.CurrentAssets,stock_financial.CurrentDebt, stock_financial.CPFDAPDOIP"
					+ ",stock_financial.DEPS,stock_financial.DividendPayable,stock_financial.DPS,stock_financial.Inventory"
					+ ",stock_financial.LongDebt, stock_financial.NCA,stock_financial.NCFFOA"
					+ ",stock_financial.NCFFIA,stock_financial.NCFFFA,stock_financial.NIICACE"
					+ ",stock_financial.NP,stock_financial.ONPATP,stock_financial.OperatingProfit"
					+ ",stock_financial.OperatingCost,stock_financial.OperatingIncome,stock_financial.OwnersEquity,stock_financial.RP, stock_financial.TotalAssets"
					+ ",stock_financial.TotalDebt,stock_financial.TEATTSOPC,stock_financial.TOI,stock_financial.TotalProfit,stock_financial.TOC,stock_financial.Yield,stock_financial.OperatingIncomeJRJ,stock_financial.ONPATPJRJ"
					+ ",stock_company." + icodeColumnCode
					+ ",stock_company.liveType,stock_company.sh50,stock_company.sh180,stock_company.sh380,stock_price.high_52wk,stock_price.low_52wk"
					+ ",stock_company.`announcedDate`,stock_price.avg_market_value ,stock_company." + inameColumnName
					+ " from  (stock_financial LEFT JOIN stock_company ON stock_financial.`Code` = stock_company.`code`) "
					+ " LEFT JOIN stock_price   ON (stock_financial.`Code` = stock_price.`code` and stock_financial.ReportDate = stock_price.date) "
					+ " WHERE `year`>=" + beginYear + " and stock_company.liveType=1  ORDER BY `code`,reportDate DESC";

			ArrayList<FinancialMetaData> list = new ArrayList<FinancialMetaData>();
			try {
				Connection con = DBManager.connPoolH.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				FinancialMetaData fd = null;

				while (rs.next()) {
					fd = new FinancialMetaData();

					fd.setLastClose(rs.getDouble(1));
					fd.setTotalMarketValue(rs.getDouble(2));
					fd.setTotalCapital(rs.getDouble(3));
					fd.setTradedACapital(rs.getDouble(4));
					fd.setTradedMarketValue(rs.getDouble(5));
					fd.setCode(rs.getString(6));
					fd.setName(rs.getString(7));
					fd.setId(rs.getInt(8));
					fd.setYear(rs.getInt(9));
					fd.setQuarter(rs.getInt(10));
					fd.setReportDate(rs.getString(11));
					fd.setAr(rs.getDouble(12));
					fd.setBeps(rs.getDouble(13));
					fd.setCapitalReserves(rs.getDouble(14));
					fd.setCurrentAssets(rs.getDouble(15));
					fd.setCurrentDebt(rs.getDouble(16));
					fd.setcPFDAPDOIP(rs.getDouble(17));
					fd.setDeps(rs.getDouble(18));
					fd.setDividendPayable(rs.getDouble(19));
					fd.setDps(rs.getDouble(20));
					fd.setInventory(rs.getDouble(21));
					fd.setLongDebt(rs.getDouble(22));
					fd.setNca(rs.getDouble(23));
					fd.setNcffoa(rs.getDouble(24));
					fd.setNcffia(rs.getDouble(25));
					fd.setNcfffa(rs.getDouble(26));
					fd.setNiicace(rs.getDouble(27));
					fd.setNp(rs.getDouble(28));
					fd.setOnpatp(rs.getDouble(29));
					fd.setOperatingProfit(rs.getDouble(30));
					fd.setOperatingCost(rs.getDouble(31));
					fd.setOperatingIncome(rs.getDouble(32));
					fd.setOwnersEquity(rs.getDouble(33));
					fd.setRp(rs.getDouble(34));
					fd.setTotalAssets(rs.getDouble(35));
					fd.setTotalDebt(rs.getDouble(36));
					fd.settEATTSOPC(rs.getDouble(37));
					fd.setToi(rs.getDouble(38));
					fd.setTotalProfit(rs.getDouble(39));
					fd.setToc(rs.getDouble(40));
					fd.setYield(rs.getDouble(41));
					fd.setOperatingIncomeJRJ(rs.getDouble(42));
					fd.setOnpatpJRJ(rs.getDouble(43));
					fd.setIcode(rs.getInt(44));
					fd.setLiveType(rs.getInt(45));
					fd.setSh50(rs.getInt(46));
					fd.setSh180(rs.getInt(47));
					fd.setSh380(rs.getInt(48));
					fd.setHigh52wk(rs.getDouble(49));
					fd.setLow52wk(rs.getDouble(50));
					fd.setAnnouncedDate(rs.getString(51));
					fd.setAvgMarketValue(rs.getDouble(52));
					fd.setIname(rs.getString(53));

					list.add(fd);
				}
				rs.close();
				stmt.close();
				DBManager.connPoolH.returnConnection(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return list;
		}
		
		/**
		 *鏌ユ壘涓夎〃缂哄け鐨勬暟鎹�
		 */
		public static ArrayList<FinancialMetaData> getMissFinacial(){
			ArrayList<FinancialMetaData> flist=new ArrayList<FinancialMetaData>();
			
			try {
				Connection con=DBManager.connPoolH.getConnection();
				Statement st=con.createStatement();
				String sql="select Code,Name,ReportDate from stock_financial where ZCFlag=0 or XJFlag=0 or LRFlag=0";
				ResultSet rs=st.executeQuery(sql);
				FinancialMetaData fmd=new FinancialMetaData();
 				while(rs.next()){
                 fmd=new FinancialMetaData();
                 fmd.setCode(rs.getString(1));
                 fmd.setName(rs.getString(2));
                 fmd.setReportDate(rs.getString(3));
                 
                 flist.add(fmd);
				}
			} catch (Exception e) {
               e.printStackTrace();
			}
			return flist;
		}
 		
		/**
		 * 澧炲姞涓偂绠�浠嬪睘鎬�
		 * author:niuguanqun
		 * 
		 */
		public static void updateStock(Stock stock, String code){
			String sql = "update stock_company set "
					+ "mainBusiness = '" + stock.getMainBusiness() + "', businessScope = '" + stock.getBusinessScope() + "', chairman = '" + stock.getChairman() + "', secretaries = '" + stock.getSecretaries() + "',registeredAddress = '"  + stock.getRegisteredAddress() + "', officeAddress = '" + stock.getOfficeAddress() + 
					"', postCode = '" + stock.getPostCode() + "', phone = '" + stock.getPhone() + "', email = '" + stock.getEmail() + "', url = '" + stock.getWebsite() + "',companyName = '" + stock.getCompanyName() + "' where code = '" + code + "'";
			try {
				Connection con = DBManager.connPoolH.getConnection();
				Statement st = con.createStatement();
				st.executeUpdate(sql);
				st.close();
				DBManager.connPoolH.returnConnection(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**
		 *	鏌ヨcompanyName涓簄ull鐨勫叕鍙稿疄浣�
		 *return 鍏徃瀹炰綋
		 *
		 *
		 */
		public static ArrayList<Stock> selectComNameNull(){
			ArrayList<Stock> vector = new ArrayList<>();
			String sql = "SELECT * from stock_company where companyname IS NULL AND (componentType = 1 and dealType = 0 and liveType = 1)";
			try {
				Connection con = DBManager.connPoolH.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				Stock stock = null;
				while(rs.next()){
					stock = new Stock();
					stock.setCode(rs.getString(3));
					stock.setName(rs.getString(2));
					vector.add(stock);
				}
				rs.close();
				st.close();
				DBManager.connPoolH.returnConnection(con);
			} catch (Exception e) {
				// TODO: handle exception
			}
			int a = vector.size();
			return vector;
			
		}
		/**
		 *瀵笵NNP琛ラ噰
		 */
		public static ArrayList<Finance> secrondCrawler(){
			ArrayList<Finance> arrayList = new ArrayList<>();
			String sql = "SELECT * from financial where DNNP IS NULL and reportdate like '%2017%' or '%2016-12-31%'";
			try {
				Connection con = DBManager.connPoolH.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				Finance finance = null;
				while(rs.next()){
					finance = new Finance();
					finance.setCode(rs.getString(3));
					finance.setName(rs.getString(4));
					arrayList.add(finance);
				}
				rs.close();
				st.close();
				DBManager.connPoolH.returnConnection(con);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return arrayList;
			
		}
		
		/**
		 * Description:financial_temp閲囬泦涓変釜琛ㄧ殑涓存椂鏁版嵁
		 * 
		 * @param finances
		 * @param isDelBefore鍦ㄦ彃鍏ュ墠鏄惁鍏堝垹闄わ紝閬垮厤閲嶅
		 */
		public static void insertDBTEMP(ArrayList<Finance> finances) {
			Connection con = null;
			try {
				con = DBManager.connPoolH.getConnection();
				Statement stmt = null;
				String insertTable = "financial_temp";
				
				stmt = con.createStatement();
				PreparedStatement ps = con
						.prepareStatement("insert into " + insertTable + "(Code,Name,ReportDate,PDE,DFP,NIIDFP,PPL,ICR"
								+ ",IFIB,FinancialExpenses,LTBAOFI,PFBAOFI,NIIPFBAOFI,CROAB,HTMI,NCFFFA,NCFDOFAIAAOLTA"
								+ ",NIFDOTFA,NCRFDOSAOBU,RefundableDeposits,StatutoryDeposits,BWOFI,NIIDWCBAOFI,INVENTORY"
								+ ",SUBD,CBD,DITL,DITA,TermDeposits,SAL,SAA,STB,STI,LAATC,CRFBI,NCL,NCA,EFRA,CPFDAPDOIP"
								+ ",LIABILITIES,LAOE,ProjectMaterials,IFCIFV,CPFPACOFAIAAOLTA,CPFGPASR,FixedAssets,DOFA"
								+ ",AdminExpenses,TCIATMS,ONPATP,TOEATP,TEATTSOPC,PreciousMetal,NIIRBC,FEG,FERFCOCACE"
								+ ",MonetaryCapital,BEPS,PCTR,RSOICR,RSOE,RSOCP,CIUPR,TRR,HFTFL,HFTFA,BWCC,NCFFOA"
								+ ",NetProfit,DevelopmentCosts,AFSFA,NIICBDADTBAOFIS,NIILAATC,TotalProfit,NII,InterestIncome"
								+ ",InterestExpenses,CurrentLiabilities,CurrentAssets,FAPUATR,ASUATR,ClaimsPaid,NCP,CEATBOTP"
								+ ",CEATEOTP,OEOC,ONCL,ONCA,OtherLiabilities,OCL,OCA,OOC,OOP,OOI,OtherPayables"
								+ ",OtherReceivables,OtherAssets,OCI,NIFSTBB,SOPIAAJV,LFDONCA,RPI,MembershipFees,CER"
								+ ",CustomerDeposits,TreasuryStock,PRFLI,NIFECAMB,DTBAOFI,UPR,ClaimReserves,CBDS,PRFLIRFR"
								+ ",UPRRFR,CRRFR,LTRFHIRFR,LTHIPR,NIFSUB,PledgeBorrowings,CRFMSIBS,DAPPTMSBS"
								+ ",BorrowingsReceived,IIR,NCPFAOSAOBU,GOODWILL,MSI,MII,PBA,PUC,OCRCOA,OCRCIA,TRRD,OCRCFA"
								+ ",PRFOIC,NCRFRB,CRFDOI,IHCACR,NHCACI,HCACI,HCACE,ITE,OwnersEquity,ERSOICR,ENCIICR,NCFFIA"
								+ ",InvestIncome,InvestmentPro,CPFAOI,SURRENDERS,FCTD,RetainedProfits,IntangibleAssets"
								+ ",CBDADTBAOFIS,CRBEI,CRFCC,DEPS,CABWCB,NIICACE,NIIPFOFI,BFCB,NIIBFCB,MarketingExpen"
								+ ",CRFSOGAROS,DFL,DFA,GAAE,GRP,NCLDWOY,NCADWOY,EarnedPremium,SurplusReserves,OperatingCost"
								+ ",OperatingProfit,OperatingIncome,BTAS,NOI,NOE,TOC,TOI,OPANP,PDPA,DTR,DividendPayable"
								+ ",InterestPayable,ClaimsPayable,NotesPayable,HCACP,BondsPayable,AccountsPayable,SRP"
								+ ",TaxesPayable,PremiumReceivables,SubrogationReceivables,RCRR,RFR,DividendReceivable"
								+ ",InterestReceivable,NotesReceivable,AccountsReceivable,OAGA,PREPAYMENTS"
								+ ",EstimatedLiabilities,PRIA,AdvanceReceipts,UCP,LTPE,LTL,LTEI,LTB,LTI,LTP,LTR,LTIOB,PDP"
								+ ",CPFTAS,CPTAFE,IHCACP,CPFOFA,CPFOOA,CPFOIA,OICCP,NIIPL,SpecialReserves,SpecialPayables"
								+ ",CapitalReserves,ADL,ASSETS,TCI,Quarter,Year,CDate,XJFlag,LRFlag,ZCFlag,NAPS,PRP,Website,IsCapture) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
								+ ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
								+ ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
								+ ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
								+ ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
								+ ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				for (Finance f : finances) {
					
					//financial 琛�
					ps.setString(1, f.getCode());
					ps.setString(2, f.getName());
					ps.setString(3, f.getReportDate());
					ps.setString(4, f.getPDE());
					ps.setString(5, f.getDFP());
					ps.setString(6, f.getNIIDFP());
					ps.setString(7, f.getPPL());
					ps.setString(8, f.getICR());
					ps.setString(9, f.getIFIB());
					ps.setString(10, f.getFinancialExpenses());
					ps.setString(11, f.getLTBAOFI());
					ps.setString(12, f.getPFBAOFI());
					ps.setString(13, f.getNIIPFBAOFI());
					ps.setString(14, f.getCROAB());
					//String HTMI=(f.getHTMI()==null ||f.getHTMI().equals("")?"":Double.parseDouble(f.getHTMI())*10000+"");
					ps.setString(15,f.getHTMI());
					ps.setString(16, f.getNCFFFA());
					ps.setString(17, f.getNCFDOFAIAAOLTA());
					ps.setString(18, f.getNIFDOTFA());
					ps.setString(19, f.getNCRFDOSAOBU());
					ps.setString(20, f.getRefundableDeposits());
					ps.setString(21, f.getStatutoryDeposits());
					ps.setString(22, f.getBWOFI());
					ps.setString(23, f.getNIIDWCBAOFI());
					//String INVENTORY=( f.getINVENTORY()==null || f.getINVENTORY().equals("")?"":Double.parseDouble( f.getINVENTORY())*10000+"");
					ps.setString(24,f.getINVENTORY());
					ps.setString(25, f.getSUBD());
					ps.setString(26, f.getCBD());
					//String DITL=( f.getDITL()==null || f.getDITL().equals("")?"":Double.parseDouble( f.getDITL())*10000+"");
					ps.setString(27,f.getDITL());
					//String  DITA=(f.getDITA()==null ||f.getDITA().equals("")?"":Double.parseDouble(f.getDITA())*10000+"");
					ps.setString(28,f.getDITA());
					ps.setString(29, f.getTermDeposits());
					ps.setString(30, f.getSAL());
					ps.setString(31, f.getSAA());
					//String  STB=(f.getSTB()==null ||f.getSTB().equals("")?"":Double.parseDouble(f.getSTB())*10000+"");
					ps.setString(32, f.getSTB());
					ps.setString(33, f.getSTI());
					ps.setString(34, f.getLAATC());
					ps.setString(35, f.getCRFBI());
					//String NCL=(f.getNCL()==null ||f.getNCL().equals("")?"":Double.parseDouble(f.getNCL())*10000+"");
					ps.setString(36,f.getNCL());
					//String NCA=(f.getNCA()==null || f.getNCA().equals("")?"":Double.parseDouble(f.getNCA())*10000+"");
					ps.setString(37,f.getNCA());
					ps.setString(38, f.getEFRA());
					ps.setString(39, f.getCPFDAPDOIP());
					//String LIABILITIES=(f.getLIABILITIES()==null || f.getLIABILITIES().equals("")?"":Double.parseDouble(f.getLIABILITIES())*10000+"");
					ps.setString(40,f.getLIABILITIES());
					//String LOAE=(f.getLAOE()==null ||f.getLAOE().equals("")?"":Double.parseDouble(f.getLAOE())*10000+"");
					ps.setString(41,f.getLAOE());
					//String ProjectMaterials=(f.getProjectMaterials()==null || f.getProjectMaterials().equals("")?"":Double.parseDouble(f.getProjectMaterials())*10000+"");
					ps.setString(42,f.getProjectMaterials());
					ps.setString(43, f.getIFCIFV());
					ps.setString(44, f.getCPFPACOFAIAAOLTA());
					ps.setString(45, f.getCPFGPASR());
					//String FixedAssets=(f.getFixedAssets()==null ||f.getFixedAssets().equals("")?"":Double.parseDouble(f.getFixedAssets())*10000+"");
					ps.setString(46, f.getFixedAssets());
					//String DOFA=(f.getDOFA()==null ||f.getDOFA().equals("")?"":Double.parseDouble(f.getDOFA())*10000+"");
					ps.setString(47,f.getDOFA());
					ps.setString(48, f.getAdminExpenses());
					ps.setString(49, f.getTCIATMS());
					ps.setString(50, f.getONPATP());
					ps.setString(51, f.getTOEATP());
					//String TEATTSOPC=(f.getTEATTSOPC()==null || f.getTEATTSOPC().equals("")?"":Double.parseDouble(f.getTEATTSOPC())*10000+"");
					ps.setString(52, f.getTEATTSOPC());
					ps.setString(53, f.getPreciousMetal());
					ps.setString(54, f.getNIIRBC());
					ps.setString(55, f.getFEG());
					ps.setString(56, f.getFERFCOCACE());
					//String MonetaryCapital=(f.getMonetaryCapital()==null ||f.getMonetaryCapital().equals("")?"":Double.parseDouble(f.getMonetaryCapital())*10000+"");
					ps.setString(57,f.getMonetaryCapital());
					ps.setString(58, f.getBEPS());//涓嶆敼
					ps.setString(59, f.getPCTR());
					ps.setString(60, f.getRSOICR());
					ps.setString(61, f.getRSOE());
					ps.setString(62, f.getRSOCP());
					ps.setString(63, f.getCIUPR());
					ps.setString(64, f.getTRR());
					//String HFTFL=(f.getHFTFL()==null ||f.getHFTFL().equals("")?"":Double.parseDouble(f.getHFTFL())*10000+"");
					ps.setString(65,f.getHFTFL());
					//String HFTFA=(f.getHFTFA()==null ||f.getHFTFA().equals("")?"":Double.parseDouble(f.getHFTFA())*10000+"");
					ps.setString(66, f.getHFTFA());
					ps.setString(67, f.getBWCC());
					ps.setString(68, f.getNCFFOA());
					ps.setString(69, f.getNetProfit());
					ps.setString(70, f.getDevelopmentCosts());
					//String AFSFA=(f.getAFSFA()==null || f.getAFSFA().equals("")?"":Double.parseDouble(f.getAFSFA())*10000+"");
					ps.setString(71,f.getAFSFA());
					ps.setString(72, f.getNIICBDADTBAOFIS());
					ps.setString(73, f.getNIILAATC());
					ps.setString(74, f.getTotalProfit());
					ps.setString(75, f.getNII());
					ps.setString(76, f.getInterestIncome());
					ps.setString(77, f.getInterestExpenses());
					//String CurrentLiabilities=(f.getCurrentLiabilities()==null ||f.getCurrentLiabilities().equals("")?"":Double.parseDouble(f.getCurrentLiabilities())*10000+"");
					ps.setString(78,f.getCurrentLiabilities());
					//String CurrentAssets=(f.getCurrentAssets()==null ||f.getCurrentAssets().equals("")?"":Double.parseDouble(f.getCurrentAssets())*10000+"");
					ps.setString(79,f.getCurrentAssets());
					ps.setString(80, f.getFAPUATR());
					ps.setString(81, f.getASUATR());
					ps.setString(82, f.getClaimsPaid());
					ps.setString(83, f.getNCP());
					ps.setString(84, f.getCEATBOTP());
					ps.setString(85, f.getCEATEOTP());
					ps.setString(86, f.getOEOC());
					ps.setString(87, f.getONCL());
					//String ONCA=( f.getONCA()==null || f.getONCA().equals("")?"":Double.parseDouble( f.getONCA())*10000+"");
					ps.setString(88,f.getONCA());
					ps.setString(89, f.getOtherLiabilities());
					//String OCL=( f.getOCL()==null ||  f.getOCL().equals("")?"":Double.parseDouble( f.getOCL())*10000+"");
					ps.setString(90,f.getOCL());
					//String OCA=( f.getOCA()==null || f.getOCA().equals("")?"":Double.parseDouble(f.getOCA())*10000+"");
					ps.setString(91,f.getOCA());
					ps.setString(92, f.getOOC());
					ps.setString(93, f.getOOP());
					ps.setString(94, f.getOOI());
					//String OtherPayables=(f.getOtherPayables()==null || f.getOtherPayables().equals("")?"":Double.parseDouble(f.getOtherPayables())*10000+"");
					ps.setString(95,f.getOtherPayables());
					//String OtherReceivables=(f.getOtherReceivables()==null || f.getOtherReceivables().equals("")?"":Double.parseDouble(f.getOtherReceivables())*10000+"");
					ps.setString(96,f.getOtherReceivables());
					ps.setString(97, f.getOtherAssets());
					ps.setString(98, f.getOCI());
					ps.setString(99, f.getNIFSTBB());
					ps.setString(100, f.getSOPIAAJV());
					ps.setString(101, f.getLFDONCA());
					ps.setString(102, f.getRPI());
					ps.setString(103, f.getMembershipFees());
					ps.setString(104, f.getCER());
					ps.setString(105, f.getCustomerDeposits());
					ps.setString(106, f.getTreasuryStock());
					ps.setString(107, f.getPRFLI());
					ps.setString(108, f.getNIFECAMB());
					ps.setString(109, f.getDTBAOFI());
					ps.setString(110, f.getUPR());
					ps.setString(111, f.getClaimReserves());
					ps.setString(112, f.getCBDS());
					ps.setString(113, f.getPRFLIRFR());
					ps.setString(114, f.getUPRRFR());
					ps.setString(115, f.getCRRFR());
					ps.setString(116, f.getLTRFHIRFR());
					ps.setString(117, f.getLTHIPR());
					ps.setString(118, f.getNIFSUB());
					ps.setString(119, f.getPledgeBorrowings());
					ps.setString(120, f.getCRFMSIBS());
					ps.setString(121, f.getDAPPTMSBS());
					ps.setString(122, f.getBorrowingsReceived());
					ps.setString(123, f.getIIR());
					ps.setString(124, f.getNCPFAOSAOBU());
					//String GOODWILL=(f.getGOODWILL()==null ||f.getGOODWILL().equals("")?"":Double.parseDouble(f.getGOODWILL())*10000+"");
					ps.setString(125,f.getGOODWILL());
					ps.setString(126, f.getMSI());
					ps.setString(127, f.getMII());
					ps.setString(128, f.getPBA());
					//String PUC=(f.getPUC()==null || f.getPUC().equals("")?"":Double.parseDouble(f.getPUC())*10000+"");
					ps.setString(129,f.getPUC());
					ps.setString(130, f.getOCRCOA());
					ps.setString(131, f.getOCRCIA());
					ps.setString(132, f.getTRRD());
					ps.setString(133, f.getOCRCFA());
					ps.setString(134, f.getPRFOIC());
					ps.setString(135, f.getNCRFRB());
					ps.setString(136, f.getCRFDOI());
					ps.setString(137, f.getIHCACR());
					ps.setString(138, f.getNHCACI());
					ps.setString(139, f.getHCACI());
					ps.setString(140, f.getHCACE());
					ps.setString(141, f.getITE());
					//String OwnersEquity=(f.getOwnersEquity()==null ||f.getOwnersEquity().equals("")?"":Double.parseDouble(f.getOwnersEquity())*10000+"");
					ps.setString(142,f.getOwnersEquity());
					ps.setString(143, f.getERSOICR());
					ps.setString(144, f.getENCIICR());
					ps.setString(145, f.getNCFFIA());
					//String InvestIncome=(f.getInvestIncome()==null ||f.getInvestIncome().equals("")?"":Double.parseDouble(f.getInvestIncome())*10000+"");
					ps.setString(146, f.getInvestIncome());
					//String InvestmentPro=(f.getInvestmentPro()==null ||f.getInvestmentPro().equals("")?"":Double.parseDouble(f.getInvestmentPro())*10000+"");
					ps.setString(147,f.getInvestmentPro());
					ps.setString(148, f.getCPFAOI());
					ps.setString(149, f.getSURRENDERS());
					ps.setString(150, f.getFCTD());
					//String RetainedProfits=(f.getRetainedProfits()==null || f.getRetainedProfits().equals("")?"":Double.parseDouble(f.getRetainedProfits())*10000+"");
					ps.setString(151,f.getRetainedProfits());
					//String IntangibleAssets=( f.getIntangibleAssets()==null ||  f.getIntangibleAssets().equals("")?"":Double.parseDouble( f.getIntangibleAssets())*10000+"");
					ps.setString(152,f.getIntangibleAssets());
					ps.setString(153, f.getCBDADTBAOFIS());
					ps.setString(154, f.getCRBEI());
					ps.setString(155, f.getCRFCC());
					ps.setString(156, f.getDEPS());//涓嶆敼
					ps.setString(157, f.getCABWCB());
					ps.setString(158, f.getNIICACE());
					ps.setString(159, f.getNIIPFOFI());
					ps.setString(160, f.getBFCB());
					ps.setString(161, f.getNIIBFCB());
					ps.setString(162, f.getMarketingExpen());
					ps.setString(163, f.getCRFSOGAROS());
					ps.setString(164, f.getDFL());
					ps.setString(165, f.getDFA());
					ps.setString(166, f.getGAAE());
					ps.setString(167, f.getGRP());
					//String NCLDWOY=( f.getNCLDWOY()==null || f.getNCLDWOY().equals("")?"":Double.parseDouble( f.getNCLDWOY())*10000+"");
					ps.setString(168,f.getNCLDWOY());
					ps.setString(169, f.getNCADWOY());
					ps.setString(170, f.getEarnedPremium());
					//String SurplusReserves=(f.getSurplusReserves()==null || f.getSurplusReserves().equals("")?"":Double.parseDouble(f.getSurplusReserves())*10000+"");
					ps.setString(171, f.getSurplusReserves());
					ps.setString(172, f.getOperatingCost());
					ps.setString(173, f.getOperatingProfit());
					ps.setString(174, f.getOperatingIncome());
					ps.setString(175, f.getBTAS());
					ps.setString(176, f.getNOI());
					ps.setString(177, f.getNOE());
					ps.setString(178, f.getTOC());
					ps.setString(179, f.getTOI());
					ps.setString(180, f.getOPANP());
					ps.setString(181, f.getPDPA());
					ps.setString(182, f.getDTR());
					//String DividendPayable=(f.getDividendPayable()==null ||f.getDividendPayable().equals("")?"":Double.parseDouble(f.getDividendPayable())*10000+"");
					ps.setString(183,f.getDividendPayable());
					//String InterestPayable=(f.getInterestPayable()==null ||f.getInterestPayable().equals("")?"":Double.parseDouble(f.getInterestPayable())*10000+"");
					ps.setString(184,f.getInterestPayable());
					ps.setString(185, f.getClaimsPayable());
					//String NotesPayable=(f.getNotesPayable()==null || f.getNotesPayable().equals("")?"":Double.parseDouble(f.getNotesPayable())*10000+"");
					ps.setString(186,f.getNotesPayable());
					ps.setString(187, f.getHCACP());
					ps.setString(188, f.getBondsPayable());
					//String  AccountsPayable=(f.getAccountsPayable()==null || f.getAccountsPayable().equals("")?"":Double.parseDouble(f.getAccountsPayable())*10000+"");
					ps.setString(189, f.getAccountsPayable());
					//String  SRP=(f.getSRP()==null ||f.getSRP().equals("")?"":Double.parseDouble(f.getSRP())*10000+"");
					ps.setString(190,f.getSRP());
					ps.setString(191, f.getTaxesPayable());
					ps.setString(192, f.getPremiumReceivables());
					ps.setString(193, f.getSubrogationReceivables());
					ps.setString(194, f.getRCRR());
					ps.setString(195, f.getRFR());
					//String DividendReceivable=(f.getDividendReceivable()==null ||f.getDividendReceivable().equals("")?"":Double.parseDouble(f.getDividendReceivable())*10000+"");
					ps.setString(196,f.getDividendReceivable());
					//String  InterestReceivable=(f.getInterestReceivable()==null ||f.getInterestReceivable().equals("")?"":Double.parseDouble(f.getInterestReceivable())*10000+"");
					ps.setString(197, f.getInterestReceivable());
					//String NotesReceivable=( f.getNotesReceivable()==null || f.getNotesReceivable().equals("")?"":Double.parseDouble(f.getNotesReceivable())*10000+"");
					ps.setString(198,f.getNotesReceivable());
					//String  AccountsReceivable=(f.getAccountsReceivable()==null || f.getAccountsReceivable().equals("")?"":Double.parseDouble(f.getAccountsReceivable())*10000+"");
					ps.setString(199, f.getAccountsReceivable());
					ps.setString(200, f.getOAGA());
					ps.setString(201, f.getPREPAYMENTS());
					ps.setString(202, f.getEstimatedLiabilities());
					ps.setString(203, f.getPRIA());
					//String AdvanceReceipts=(f.getAdvanceReceipts()==null ||f.getAdvanceReceipts().equals("")?"":Double.parseDouble(f.getAdvanceReceipts())*10000+"");
					ps.setString(204,f.getAdvanceReceipts());
					//String  UCP=(f.getUCP()==null || f.getUCP().equals("")?"":Double.parseDouble(f.getUCP())*10000+"");
					ps.setString(205,f.getUCP());
					//String  LTPE=( f.getLTPE()==null || f.getLTPE().equals("")?"":Double.parseDouble( f.getLTPE())*10000+"");
					ps.setString(206,f.getLTPE());
					//String LTL=(f.getLTL()==null ||f.getLTL().equals("")?"":Double.parseDouble(f.getLTL())*10000+"");
					ps.setString(207,f.getLTL());
					//String LTEI=(f.getLTEI()==null || f.getLTEI().equals("")?"":Double.parseDouble(f.getLTEI())*10000+"");
					ps.setString(208,f.getLTEI());
					//String LTB=(f.getLTB()==null ||f.getLTB().equals("")?"":Double.parseDouble(f.getLTB())*10000+"");
					ps.setString(209,f.getLTB());
					//String LTI=( f.getLTI()==null || f.getLTI().equals("")?"":Double.parseDouble(f.getLTI())*10000+"");
					ps.setString(210,f.getLTI());
					//String LTP=( f.getLTP()==null || f.getLTP().equals("")?"":Double.parseDouble( f.getLTP())*10000+"");
					ps.setString(211,f.getLTP());
					//String LTR=( f.getLTR()==null ||f.getLTR().equals("")?"":Double.parseDouble( f.getLTR())*10000+"");
					ps.setString(212, f.getLTR());
					ps.setString(213, f.getLTIOB());
					ps.setString(214, f.getPDP());
					ps.setString(215, f.getCPFTAS());
					ps.setString(216, f.getCPTAFE());
					ps.setString(217, f.getIHCACP());
					ps.setString(218, f.getCPFOFA());
					ps.setString(219, f.getCPFOOA());
					ps.setString(220, f.getCPFOIA());
					ps.setString(221, f.getOICCP());
					ps.setString(222, f.getNIIPL());
					ps.setString(223, f.getSpecialReserves());
					//String SpecialPayables=(f.getSpecialPayables()==null || f.getSpecialPayables().equals("")?"":Double.parseDouble(f.getSpecialPayables())*10000+"");
					ps.setString(224, f.getSpecialPayables());
					//String CapitalReserves=(f.getCapitalReserves()==null ||f.getCapitalReserves().equals("")?"":Double.parseDouble(f.getCapitalReserves())*10000+"");
					ps.setString(225,f.getCapitalReserves() );
					ps.setString(226, f.getADL());
					//String ASSETS=( f.getASSETS()==null || f.getASSETS().equals("")?"":Double.parseDouble( f.getASSETS())*10000+"");
					ps.setString(227,f.getASSETS());
					ps.setString(228, f.getTCI());
					ps.setString(229, f.getQuarter());
					ps.setString(230, f.getYear());
					ps.setString(231, DateUtils.formatDate(new Date()));
					ps.setInt(232, f.getXJFlag());
					ps.setInt(233, f.getLRFlag());
					ps.setInt(234, f.getZCFlag());
					ps.setString(235, f.getNAPS());
					ps.setString(236, f.getPRP());
					ps.setString(237, f.getWebsite());//缃戠珯锛氭柊娴�佷笢鏂硅储瀵屻�侀噾铻嶈',
					ps.setInt(238, f.getIsCapture());//鏄惁鎶撳彇鍒版暟鎹細Y/N',
					
					insertTable = "financial_temp";
					// del old data,涓�鏈堝垹涓�娆°��
					if (DateUtils.getDay(new Date())==10){
						stmt.executeUpdate("delete from " + insertTable + " where CDate<'" + DateUtils.formatDate(new Date()) + "'");
					}
					
					if (ps.executeUpdate() > -1) {
						loggerRT.info(f.getCode() + ",骞翠唤-瀛ｅ害锛�" + f.getYear() + "-" + f.getQuarter() + ",鎴愬姛閲囬泦涓存椂缁撴灉鍐欏叆鏁版嵁搴擄紒" );
					}
				}
				if (stmt != null) {
					stmt.close();
				}
				ps.close();

			} catch (SQLException e) {
				errorlogger.error(e.getMessage(), e);
			} 
		}
		
		//历史数据
		public static void updateDNNPAndDNNPGR(Finance finance){
			String sql = "update financial set DNNP = '" + finance.getDNNP() + "', DNNPGR = '" + finance.getDNNPGR() + 
					"' where code = '" + finance.getCode() + "' and reportdate = '" + finance.getReportDate() +"'";
			try {
				Connection con = DBManager.connPoolH.getConnection();
				Statement st = con.createStatement();
				st.executeUpdate(sql);
				st.close();
				con.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		public static ArrayList<Finance> selectDNNPAndDNNPGR(){
			ArrayList<Finance> arrayList = new ArrayList<>();
			Finance finance = null;
			String sql = "SELECT code,reporedate from stock_financial where (DNNP is NULL or DNNPGR is null ) and (reportdate = '2017-12-31' or reportdate = '2017-12-31' or reportdate = '2017-09-30' or reportdate = '2017-06-30' or reportdate = '2017-03-31' or reportdate = '2016-12-31')";
			try {
				Connection con = DBManager.connPoolH.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				while(rs.next()){
					finance = new Finance();
					finance.setCode(rs.getString(1));
					finance.setReportDate(rs.getString(2));
					arrayList.add(finance);
				}
				rs.close();
				st.close();
				con.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return arrayList;
		}
}
