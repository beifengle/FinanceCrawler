package tool;
import com.yysoft.mgr.RealTimeDataMgr;
import com.yysoft.util.ConfigParser;

public class RealTimeTest {
	public static void main(String[] args) {
		new ConfigParser();
		RealTimeDataMgr.exec();
		System.out.println("end!");
	}
}
