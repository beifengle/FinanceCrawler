package com.yysoft.extract;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yysoft.entity.Finance;
import com.yysoft.util.StringUtils;

/**
 * 功能描述：现金流量表页面数据提取
 * @author Huanyan.Lu
 * @date:2018年3月20日
 * @time:下午3:08:02
 * @version:1.0
 */
public class SinaXJExtracter {
	private static Logger logger = LogManager.getLogger(SinaXJExtracter.class); //

	
	/**
	 * Description:现金流量表页面数据提取
	 * @param finance 提取信息实例
	 * @param data 读取的页面元信息
	 * @param code 股票代码
	 * @param name 股票简称
	 * @param reportDate 要提取信息的报告期
	 * @return
	 */
	public static  Finance fetch(Finance finance,String data[][],String code,String name,String reportDate){
		logger.info("提取的报告期"+ reportDate + "===Sina现金流量表页面数据项部分赋值===");
		String dateString = "";
		for(int i=0;i<data[0].length;i++){
			if(data[0][i]==null || !data[0][i].contains(reportDate)){
				continue;
			}
			dateString +=data[0][i] +",";
			finance.setCode(code);
			finance.setName(name);
			finance.setReportDate(reportDate);//报告期
			finance.setQuarter(StringUtils.getQuarterFromReport(reportDate));
			finance.setYear(finance.getReportDate().substring(0,4));
			
			if(data.length==77){//一般
				//判断此页关键值是否存在，不存在则不提取并返回null
				if(StringUtils.isEmpty(data[12][i]) || data[12][i].trim().equals("0")){//经营活动产生的现金流量净额为null
					return null;
				}
				finance.setCRFSOGAROS(data[3][i]);//销售商品、提供劳务收到的现金
				finance.setTRRD(data[4][i]);//收到的税费返还
				finance.setOCRCOA(data[5][i]);//收到的其他与经营活动有关的现金
				finance.setCPFGPASR(data[7][i]);//	购买商品、接受劳务支付的现金
				finance.setCPTAFE(data[8][i]);//	支付给职工以及为职工支付的现金
				finance.setCPFTAS(data[9][i]);//支付的各项税费
				finance.setCPFOOA(data[10][i]);//支付其他与经营活动有关的现金
				finance.setNCFFOA(data[12][i]);//经营活动产生的现金流量净额
				
				finance.setCRFDOI(data[14][i]);//收回投资收到的现金
				finance.setIIR(data[15][i]);//取得投资收益收到的现金
				finance.setNCFDOFAIAAOLTA(data[16][i]);//处置固定资产、无形资产和其他长期资产收回的现金净额
				finance.setNCRFDOSAOBU(data[17][i]);//处置子公司及其他营业单位收到的现金净额
				finance.setOCRCIA(data[18][i]);//收到其他与投资活动有关的现金
				finance.setCPFPACOFAIAAOLTA(data[20][i]);//购建固定资产、无形资产和其他长期资产支付的现金
				finance.setCPFAOI(data[21][i]);//投资支付的现金
				finance.setNCPFAOSAOBU(data[22][i]);//取得子公司及其他营业单位支付的现金净额
				finance.setCPFOIA(data[23][i]);//支付其他与投资活动有关的现金
				finance.setNCFFIA(data[25][i]);//投资活动产生的现金流量净额
				
				finance.setCRFCC(data[26][i]);//吸收投资收到的现金
				finance.setCRFMSIBS(data[28][i]);//子公司吸收少数股东投资收到的现金
				finance.setBorrowingsReceived(data[29][i]);//取得借款收到的现金
				finance.setOCRCFA(data[31][i]);//收到其他与筹资活动有关的现金
				finance.setCRFBI(data[30][i]);//发行债券收到的现金
				finance.setCROAB(data[33][i]);//偿还债务支付的现金
				finance.setCPFDAPDOIP(data[34][i]);//分配股利、利润或偿付利息支付的现金
				finance.setDAPPTMSBS(data[35][i]);//子公司支付给少数股东的股利
				finance.setCPFOFA(data[36][i]);//支付其他与筹资活动有关的现金
				finance.setNCFFFA(data[38][i]);//筹资活动产生的现金流量净额
				finance.setFERFCOCACE(data[39][i]);//汇率变动对现金的影响
				finance.setNIICACE(data[40][i]);//现金及现金等价物净增加额
				finance.setCEATBOTP(data[41][i]);//期初现金及现金等价物余额
				finance.setCEATEOTP(data[42][i]);//期末现金及现金等价物余额
			}else if(data.length==96){//银行
				//判断此页关键值是否存在，不存在则不提取并返回null
				if(StringUtils.isEmpty(data[16][i]) || data[16][i].trim().equals("0")){//经营活动产生的现金流量净额为null
					return null;
				}
//				finance.setCRFSOGAROS(data[3][i]);//销售商品、提供劳务收到的现金
//				finance.setTRRD(data[4][i]);//收到的税费返还
				finance.setOCRCOA(data[7][i]);//收到的其他与经营活动有关的现金
//				finance.setCPFGPASR(data[7][i]);//	购买商品、接受劳务支付的现金
				finance.setCPTAFE(data[11][i]);//	支付给职工以及为职工支付的现金
				finance.setCPFTAS(data[12][i]);//支付的各项税费
				finance.setCPFOOA(data[13][i]);//支付其他与经营活动有关的现金
				finance.setNCFFOA(data[16][i]);//经营活动产生的现金流量净额
				
				finance.setCRFDOI(data[18][i]);//收回投资收到的现金
				finance.setIIR(data[19][i]);//取得投资收益收到的现金
				finance.setNCFDOFAIAAOLTA(data[20][i]);//处置固定资产、无形资产和其他长期资产收回的现金净额
				finance.setNCRFDOSAOBU(data[21][i]);//处置子公司及其他营业单位收到的现金净额
				finance.setOCRCIA(data[22][i]);//收到其他与投资活动有关的现金
				finance.setCPFPACOFAIAAOLTA(data[20][i]);//购建固定资产、无形资产和其他长期资产支付的现金
				finance.setCPFAOI(data[24][i]);//投资支付的现金
//				finance.setNCPFAOSAOBU(data[21][i]);//取得子公司及其他营业单位支付的现金净额
				finance.setCPFOIA(data[26][i]);//支付其他与投资活动有关的现金
				finance.setNCFFIA(data[28][i]);//投资活动产生的现金流量净额
				
				finance.setCRFCC(data[30][i]);//吸收投资收到的现金
//				finance.setCRFMSIBS(data[28][i]);//子公司吸收少数股东投资收到的现金
//				finance.setBorrowingsReceived(data[29][i]);//取得借款收到的现金
				finance.setOCRCFA(data[34][i]);//收到其他与筹资活动有关的现金
				finance.setCRFBI(data[32][i]);//发行债券收到的现金
				finance.setCROAB(data[36][i]);//偿还债务支付的现金
				finance.setCPFDAPDOIP(data[37][i]);//分配股利、利润或偿付利息支付的现金
//				finance.setDAPPTMSBS(data[35][i]);//子公司支付给少数股东的股利
				finance.setCPFOFA(data[40][i]);//支付其他与筹资活动有关的现金
				finance.setNCFFFA(data[42][i]);//筹资活动产生的现金流量净额
				finance.setFERFCOCACE(data[43][i]);//汇率变动对现金的影响
				finance.setNIICACE(data[44][i]);//现金及现金等价物净增加额
				finance.setCEATBOTP(data[45][i]);//期初现金及现金等价物余额
				finance.setCEATEOTP(data[46][i]);//期末现金及现金等价物余额
			}else if(data.length==76){//证券
				//判断此页关键值是否存在，不存在则不提取并返回null
				if(StringUtils.isEmpty(data[15][i]) || data[15][i].trim().equals("0")){//经营活动产生的现金流量净额为null
					return null;
				}
//				finance.setCRFSOGAROS(data[3][i]);//销售商品、提供劳务收到的现金
//				finance.setTRRD(data[4][i]);//收到的税费返还
				finance.setOCRCOA(data[8][i]);//收到的其他与经营活动有关的现金
//				finance.setCPFGPASR(data[7][i]);//	购买商品、接受劳务支付的现金
				finance.setCPTAFE(data[10][i]);//	支付给职工以及为职工支付的现金
				finance.setCPFTAS(data[11][i]);//支付的各项税费
				finance.setCPFOOA(data[12][i]);//支付其他与经营活动有关的现金
				finance.setNCFFOA(data[15][i]);//经营活动产生的现金流量净额
				
				finance.setCRFDOI(data[17][i]);//收回投资收到的现金
				finance.setIIR(data[18][i]);//取得投资收益收到的现金
				finance.setNCFDOFAIAAOLTA(data[19][i]);//处置固定资产、无形资产和其他长期资产收回的现金净额
//				finance.setNCRFDOSAOBU(data[21][i]);//处置子公司及其他营业单位收到的现金净额
				finance.setOCRCIA(data[20][i]);//收到其他与投资活动有关的现金
				finance.setCPFPACOFAIAAOLTA(data[23][i]);//购建固定资产、无形资产和其他长期资产支付的现金
				finance.setCPFAOI(data[22][i]);//投资支付的现金
//				finance.setNCPFAOSAOBU(data[21][i]);//取得子公司及其他营业单位支付的现金净额
				finance.setCPFOIA(data[24][i]);//支付其他与投资活动有关的现金
				finance.setNCFFIA(data[26][i]);//投资活动产生的现金流量净额
				
				finance.setCRFCC(data[28][i]);//吸收投资收到的现金
//				finance.setCRFMSIBS(data[28][i]);//子公司吸收少数股东投资收到的现金
//				finance.setBorrowingsReceived(data[29][i]);//取得借款收到的现金
				finance.setOCRCFA(data[31][i]);//收到其他与筹资活动有关的现金
				finance.setCRFBI(data[30][i]);//发行债券收到的现金
				finance.setCROAB(data[33][i]);//偿还债务支付的现金
				finance.setCPFDAPDOIP(data[34][i]);//分配股利、利润或偿付利息支付的现金
//				finance.setDAPPTMSBS(data[35][i]);//子公司支付给少数股东的股利
				finance.setCPFOFA(data[35][i]);//支付其他与筹资活动有关的现金
				finance.setNCFFFA(data[37][i]);//筹资活动产生的现金流量净额
				finance.setFERFCOCACE(data[38][i]);//汇率变动对现金的影响
				finance.setNIICACE(data[39][i]);//现金及现金等价物净增加额
				finance.setCEATBOTP(data[40][i]);//期初现金及现金等价物余额
				finance.setCEATEOTP(data[41][i]);//期末现金及现金等价物余额
			}else if(data.length==82){//保险行业
				//判断此页关键值是否存在，不存在则不提取并返回null
				if(StringUtils.isEmpty(data[15][i]) || data[15][i].trim().equals("0")){//经营活动产生的现金流量净额为null
					return null;
				}
//				finance.setCRFSOGAROS(data[3][i]);//销售商品、提供劳务收到的现金
//				finance.setTRRD(data[4][i]);//收到的税费返还
				finance.setOCRCOA(data[5][i]);//收到的其他与经营活动有关的现金
//				finance.setCPFGPASR(data[7][i]);//	购买商品、接受劳务支付的现金
				finance.setCPTAFE(data[9][i]);//	支付给职工以及为职工支付的现金
				finance.setCPFTAS(data[11][i]);//支付的各项税费
				finance.setCPFOOA(data[12][i]);//支付其他与经营活动有关的现金
				finance.setNCFFOA(data[15][i]);//经营活动产生的现金流量净额
				
				finance.setCRFDOI(data[17][i]);//收回投资收到的现金
				finance.setIIR(data[18][i]);//取得投资收益收到的现金
				finance.setNCFDOFAIAAOLTA(data[19][i]);//处置固定资产、无形资产和其他长期资产收回的现金净额
//				finance.setNCRFDOSAOBU(data[21][i]);//处置子公司及其他营业单位收到的现金净额
				finance.setOCRCIA(data[21][i]);//收到其他与投资活动有关的现金
				finance.setCPFPACOFAIAAOLTA(data[25][i]);//购建固定资产、无形资产和其他长期资产支付的现金
				finance.setCPFAOI(data[23][i]);//投资支付的现金
//				finance.setNCPFAOSAOBU(data[21][i]);//取得子公司及其他营业单位支付的现金净额
				finance.setCPFOIA(data[27][i]);//支付其他与投资活动有关的现金
				finance.setNCFFIA(data[29][i]);//投资活动产生的现金流量净额
				
				finance.setCRFCC(data[31][i]);//吸收投资收到的现金
//				finance.setCRFMSIBS(data[28][i]);//子公司吸收少数股东投资收到的现金
//				finance.setBorrowingsReceived(data[29][i]);//取得借款收到的现金
				finance.setOCRCFA(data[34][i]);//收到其他与筹资活动有关的现金
				finance.setCRFBI(data[33][i]);//发行债券收到的现金
				finance.setCROAB(data[36][i]);//偿还债务支付的现金
				finance.setCPFDAPDOIP(data[37][i]);//分配股利、利润或偿付利息支付的现金
//				finance.setDAPPTMSBS(data[35][i]);//子公司支付给少数股东的股利
				finance.setCPFOFA(data[38][i]);//支付其他与筹资活动有关的现金
				finance.setNCFFFA(data[40][i]);//筹资活动产生的现金流量净额
				finance.setFERFCOCACE(data[41][i]);//汇率变动对现金的影响
				finance.setNIICACE(data[42][i]);//现金及现金等价物净增加额
				finance.setCEATBOTP(data[43][i]);//期初现金及现金等价物余额
				finance.setCEATEOTP(data[44][i]);//期末现金及现金等价物余额
			}else{
				logger.info("sina-现金流量表-数组长度匹配不到。data.length=" + data.length);
			}
			finance.setXJFlag(4);//标记采集来源
			return finance;
			
		}
		return null;
	}
	
}
