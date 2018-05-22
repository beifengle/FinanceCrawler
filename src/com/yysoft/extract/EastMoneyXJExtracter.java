package com.yysoft.extract;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yysoft.entity.Finance;
import com.yysoft.util.StringUtils;

/**
 * 功能描述：现金流量表页面数据提取
 * @author Huanyan.Lu
 * @date:2018年8月19日
 * @time:下午3:08:02
 * @version:1.0
 */
public class EastMoneyXJExtracter {
	private static Logger logger = LogManager.getLogger(EastMoneyXJExtracter.class); //

	
	/**
	 * Description:现金流量表页面数据提取
	 * @param finance 提取信息实例
	 * @param data 读取的页面元信息
	 * @param code 股票代码
	 * @param name 股票简称
	 * @param reportDate 要提取信息的报告期
	 * @return
	 */
	public static  Finance fetch(Finance finance,String data,String code,String name,String reportDate){
		logger.info("提取的报告期"+ reportDate + "===东方财富-现金流量表页面数据项部分赋值===");
		String f_date = "";
		JSONObject job = null;
		// 报告日期格式化转换为：2017/9/30
		String date[] = reportDate.split("-");
		date[1] = String.valueOf(Integer.parseInt(date[1]));
		date[2] = String.valueOf(Integer.parseInt(date[2]));
		f_date = date[0] + "/" + date[1] + "/" + date[2];
		JSONArray j_list;
		boolean isHaving = false;
		try {
			
			if (data.startsWith("[")) {
				j_list = new JSONArray(data);
				if (j_list.length() > 0) {
					for (int i = 0; i < j_list.length(); i++) {
						if (j_list.getJSONObject(i).getString("REPORTDATE").contains(f_date)) {
							job = j_list.getJSONObject(i);
							isHaving = true;
							break;
						}
						continue;
					}
				}
			} else {
				return null;
			}
			
			finance.setCode(code);
			finance.setName(name);
			finance.setReportDate(reportDate);//报告期
			finance.setQuarter(StringUtils.getQuarterFromReport(reportDate));
			finance.setYear(finance.getReportDate().substring(0,4));
			
			if(isHaving!=true){
				return null;
			}
			
			finance.setCRFSOGAROS(job.getString("SALEGOODSSERVICEREC"));//销售商品、提供劳务收到的现金
			finance.setTRRD(job.getString("TAXRETURNREC"));//收到的税费返还
			finance.setOCRCOA(job.getString("OTHEROPERATEREC"));//收到的其他与经营活动有关的现金
			finance.setCPFGPASR(job.getString("BUYGOODSSERVICEPAY"));//	购买商品、接受劳务支付的现金
			finance.setCPTAFE(job.getString("EMPLOYEEPAY"));//	支付给职工以及为职工支付的现金
			finance.setCPFTAS(job.getString("TAXPAY"));//支付的各项税费
			finance.setCPFOOA(job.getString("OTHEROPERATEPAY"));//支付其他与经营活动有关的现金
			finance.setNCFFOA(job.getString("NETOPERATECASHFLOW"));//经营活动产生的现金流量净额
			
			finance.setCRFDOI(job.getString("DISPOSALINVREC"));//收回投资收到的现金
			finance.setIIR(job.getString("INVINCOMEREC"));//取得投资收益收到的现金
			finance.setNCFDOFAIAAOLTA(job.getString("DISPFILASSETREC"));//处置固定资产、无形资产和其他长期资产收回的现金净额
//			finance.setNCRFDOSAOBU(data[16][i]);//处置子公司及其他营业单位收到的现金净额
			finance.setOCRCIA(job.getString("OTHERINVREC"));//收到其他与投资活动有关的现金
			finance.setCPFPACOFAIAAOLTA(job.getString("BUYFILASSETPAY"));//购建固定资产、无形资产和其他长期资产支付的现金
			finance.setCPFAOI(job.getString("INVPAY"));//投资支付的现金
//			finance.setNCPFAOSAOBU(data[21][i]);//取得子公司及其他营业单位支付的现金净额
			finance.setCPFOIA(job.getString("OTHERINVPAY"));//支付其他与投资活动有关的现金
			finance.setNCFFIA(job.getString("NETINVCASHFLOW"));//投资活动产生的现金流量净额
			
			finance.setCRFCC(job.getString("ACCEPTINVREC"));//吸收投资收到的现金
			finance.setCRFMSIBS(job.getString("SUBSIDIARYACCEPT"));//子公司吸收少数股东投资收到的现金
			finance.setBorrowingsReceived(job.getString("LOANREC"));//取得借款收到的现金
			finance.setOCRCFA(job.getString("LOANREC"));//收到其他与筹资活动有关的现金
			finance.setCRFBI(job.getString("ISSUEBONDREC"));//发行债券收到的现金
			finance.setCROAB(job.getString("CASHEQUIENDING"));//偿还债务支付的现金
			finance.setCPFDAPDOIP(job.getString("DIVIPROFITORINTPAY"));//分配股利、利润或偿付利息支付的现金
			finance.setDAPPTMSBS(job.getString("SUBSIDIARYACCEPT"));//子公司支付给少数股东的股利
			finance.setCPFOFA(job.getString("OTHERFINAPAY"));//支付其他与筹资活动有关的现金
			finance.setNCFFFA(job.getString("NETFINACASHFLOW"));//筹资活动产生的现金流量净额
			finance.setFERFCOCACE(job.getString("EFFECTEXCHANGERATE"));//汇率变动对现金的影响
			finance.setNIICACE(job.getString("NICASHEQUI"));//现金及现金等价物净增加额
			finance.setCEATBOTP(job.getString("CASHEQUIBEGINNING"));//期初现金及现金等价物余额
			finance.setCEATEOTP(job.getString("CASHEQUIENDING"));//期末现金及现金等价物余额
			
			finance.setXJFlag(3);//标记采集来源
			return finance;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
