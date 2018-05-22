package tool;


import com.yysoft.entity.Finance;
import com.yysoft.http.JRJHttpUtils;
import com.yysoft.http.QQHttpUtils;
import com.yysoft.http.SinaHttpUtils;
import com.yysoft.util.ConfigParser;
import com.yysoft.util.OutInfos;

public class FinanceGather {
	
	
	public static Finance down(String code,String name,String reportDate){
		
		Finance finance = new Finance();
		Finance financeTemp = new Finance();
		boolean isBank =false;//是否是银行行业
		if(name.contains("银行")){
			isBank = true;
		}
		//1-资产负债页面采集
		financeTemp = JRJHttpUtils.downPage(finance, code, name, 1, reportDate);
		if(financeTemp==null){
			financeTemp = SinaHttpUtils.downPage(finance, code, name, 1, reportDate);

		}else{
			finance = financeTemp;
		}
		//2-现金流量表
		financeTemp = JRJHttpUtils.downPage(finance, code, name, 2, reportDate);
		if(financeTemp==null){

			financeTemp = SinaHttpUtils.downPage(finance, code, name, 2, reportDate);
			System.out.println("qq2!");

		}else{
			finance = financeTemp;
		}
		//3-公司利润表
		financeTemp = JRJHttpUtils.downPage(finance, code, name, 3, reportDate);
		if(financeTemp==null){
			financeTemp = SinaHttpUtils.downPage(finance, code, name, 3, reportDate);

			System.out.println("qq3!");

		}else{
			finance = financeTemp;
		}
		OutInfos.OutAllFieldsDemo(finance);//打印
//		OutInfos.OutAllFieldsNotNullDemo(finance);//打印非空
		return finance;
	}
	public static void main(String[] args) {
		new ConfigParser();
		down("sz000002","科顺股份","2017-09-30");
		System.out.println("end!");
	}
}
