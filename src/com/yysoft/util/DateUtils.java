package com.yysoft.util;
 

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 
 * 功能描述：日期处理常用工具类
 * 
 * @author  
 * @Date Jul 19, 2014
 * @Time 9:47:53 AM
 * @version 1.0
 */
public class DateUtils {

	public static Date date = null;

	public static DateFormat dateFormat = null;

	public static Calendar calendar = null;

	/**
	 * 功能描述：格式化日期
	 * 
	 * @param dateStr
	 *            String 字符型日期
	 * @param format
	 *            String 格式
	 * @return Date 日期
	 */
	public static Date parseDate(String dateStr, String format) {
		try {
			dateFormat = new SimpleDateFormat(format);
			String dt = dateStr.replaceAll("-", "/");
			if ((!dt.equals("")) && (dt.length() < format.length())) {
				dt += format.substring(dt.length()).replaceAll("[YyMmDdHhSs]",
						"0");
			}
			date = (Date) dateFormat.parse(dt);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 功能描述：格式化日期
	 * 
	 * @param dateStr
	 *            String 字符型日期：YYYY-MM-DD 格式
	 * @return Date
	 */
	public static Date parseDate(String dateStr) {
		return parseDate(dateStr, "yyyy/MM/dd");
	}
	
	
	/**
	 * 功能描述：格式化日期
	 * 
	 * @param dateStr
	 *            String 字符型日期：YYYY-MM-DD 格式
	 * @return Date
	 */
	public static Date parseDate2(String dateStr) {
		return parseDate(dateStr, "yyyy-MM-DD");
	}

	/**
	 * 功能描述：格式化输出日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return 返回字符型日期
	 */
	public static String format(Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				dateFormat = new SimpleDateFormat(format);
				result = dateFormat.format(date);
			}
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 功能描述：
	 * 
	 * @param date
	 *            Date 日期
	 * @return
	 */
	public static String format(Date date) {
		return format(date, "yyyy/MM/dd");
	}
	public static String format2(Date date) {
		return format(date, "yyyy-MM-dd");
	}

	/**
	 * 功能描述：返回年份
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回年份
	 */
	public static int getYear(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 功能描述：返回月份
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回月份
	 */
	public static int getMonth(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 功能描述：返回日份
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回日份
	 */
	public static int getDay(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 功能描述：返回小时
	 * 
	 * @param date
	 *            日期
	 * @return 返回小时
	 */
	public static int getHour(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 功能描述：返回分钟
	 * 
	 * @param date
	 *            日期
	 * @return 返回分钟
	 */
	public static int getMinute(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 返回秒钟
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回秒钟
	 */
	public static int getSecond(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 功能描述：返回毫秒
	 * 
	 * @param date
	 *            日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	/**
	 * 功能描述：返回字符型日期
	 * 
	 * @param date
	 *            日期
	 * @return 返回字符型日期 yyyy/MM/dd 格式
	 */
	public static String getDate(Date date) {
		return format(date, "yyyy/MM/dd");
	}

	/**
	 * 功能描述：返回字符型时间
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回字符型时间 HH:mm:ss 格式
	 */
	public static String getTime(Date date) {
		return format(date, "HH:mm:ss");
	}

	/**
	 * 功能描述：返回字符型日期时间
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回字符型日期时间 yyyy-MM-dd HH:mm:ss 格式
	 */
	public static String getDateTime(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 功能描述：日期相加
	 * 
	 * @param date
	 *            Date 日期
	 * @param day
	 *            int 天数
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, int day) {
		calendar = Calendar.getInstance();
		long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
	
	public static Date sufDate(Date date, int hour) {
		calendar = Calendar.getInstance();
		long millis = getMillis(date) - ((long) hour) * 3600 * 1000;
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	/**
	 * 功能描述：日期相减
	 * 
	 * @param date
	 *            Date 日期
	 * @param date1
	 *            Date 日期
	 * @return 返回相减后的日期
	 */
	public static int diffDate(Date date, Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
	}

	/**
	 * 功能描述：取得指定月份的第一天
	 * 
	 * @param strdate
	 *            String 字符型日期
	 * @return String yyyy-MM-dd 格式
	 */
	public static String getMonthBegin(String strdate) {
		date = parseDate(strdate);
		return format(date, "yyyy-MM") + "-01";
	}

	/**
	 * 功能描述：取得指定月份的最后一天
	 * 
	 * @param strdate
	 *            String 字符型日期
	 * @return String 日期字符串 yyyy-MM-dd格式
	 */
	public static String getMonthEnd(String strdate) {
		date = parseDate(getMonthBegin(strdate));
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return formatDate(calendar.getTime());
	}

	/**
	 * 功能描述：常用的格式化日期
	 * 
	 * @param date
	 *            Date 日期
	 * @return String 日期字符串 yyyy-MM-dd格式
	 */
	public static String formatDate(Date date) {
		return formatDateByFormat(date, "yyyy-MM-dd");
	}

	/**
	 * 功能描述：以指定的格式来格式化日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return String 日期字符串
	 */
	public static String formatDateByFormat(Date date, String format) {
		String result = "";
		if (date != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	public static boolean dateCompare(Date d1,Date d2){
		
		long lTime = DateUtils.getMillis(d1) - DateUtils.getMillis(d2);
		if(lTime>0)
			return true;
		
		return false;
		
	}
	/**
	 * 比例两个日期年月日的大小
	 * @param date 2008-09-08
	 * @return
	 */
	public static boolean compareDate(String date){
		Date nowdate=new java.util.Date();  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date d;
		boolean flag =false;
		try {
			d = sdf.parse(date);
			flag = d.before(nowdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flag;
	}
	/**
	 * 获取指定日期的后一天
	 * @param specifiedDay 2014-05-06
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay){ 
		Calendar c = Calendar.getInstance(); 
		Date date=null; 
		try { 
		date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay); 
		} catch (ParseException e) { 
		e.printStackTrace(); 
		} 
		c.setTime(date); 
		int day=c.get(Calendar.DATE); 
		c.set(Calendar.DATE,day+1); 

		String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()); 
		return dayAfter; 
		} 
	
	public static long getMillionSeconds(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long m = 0;
		try {
			m = sdf.parse(date).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}
	
	/**
	 * 
	 * @param dateStr
	 * @return
	 * nagel add 20140828
	 */
    public static Calendar getCalendarOfStr(String dateStr) {
    	  dateStr = dateStr.replaceAll(" ", "");//替换空格
    	  Calendar calendar ;
    	  int year = 0, month = 0, day = 0;
    	  if(dateStr.contains("-")){
    	  String[] year_month_day = dateStr.split("-");
	    	   if (!"".equals(year_month_day[0])) {
	    	    year = Integer.parseInt(year_month_day[0]);
	    	   }
	    	   if (!"".equals(year_month_day[1])) {
	    	    month = Integer.parseInt(year_month_day[1]);
	    	   }
	    	   if (!"".equals(year_month_day[2])) {
	    	    day = Integer.parseInt(year_month_day[2]);
	    	   }
    	  }else{
    		  year = Integer.parseInt(dateStr.substring(0, 4));
    		  month = Integer.parseInt(dateStr.substring(4, 6));
    		  day = Integer.parseInt(dateStr.substring(6, 8));
    	  }
    	  
//    	  System.out.println("year: "+year+" month: "+month+" day: "+day);
    	  calendar = new GregorianCalendar(year, month - 1, day);

    	  return calendar;
    }
    
    //获取当前系统时间的年月日时分秒毫秒时间格式
    public static String getMillisecond(){
		String msg="";
		Date date = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss.SSS");
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		msg+= sdf.format(date);
		return msg;
    }
  //获取前两周的日期
  	 public static String getDateBefore(Date d,int day){
  				   Calendar now =Calendar.getInstance();
  				   now.setTime(d);
  				   now.set(Calendar.DATE,now.get(Calendar.DATE)-day);
  				   return  new SimpleDateFormat("yyyy-MM-dd").format(now.getTime());
  				  }	
    /**
     * Description:监控东方财富网财报发布页面的季度日期（一般只采最新两期的财报）。
     * @return
     */
    public static String[] getReportDateForCrawler(){
    	String[] dates = null;
    	int year =  getYear(new Date());
    	int month = getMonth(new Date());
    	if(month>=1 && month<=3) {
    		dates = new String[1];
    		dates[0]= (year-1) + "-12-31";
    	}else if(month ==4){
    		dates = new String[2];
    		dates[0]= (year-1) + "-12-31";
    		dates[1]= year + "-03-31";
    	}else if(month>=5 && month<=8){
    		dates = new String[1];
    		dates[0]= year + "-06-30";
    	}else if(month>=9 && month<=10){
    		dates = new String[1];
    		dates[0]= year + "-09-30";
    	}else if(month>=11 && month<=12){
    		dates = new String[1];
    		dates[0]= year + "-12-31";
    	}
    	return dates;
    	
    }
	public static void main(String[] args) throws ParseException {
//		Date d = new Date();
		// System.out.println(d.toString());
//		System.out.println(formatDate(d).toString());
		// System.out.println(getMonthBegin(formatDate(d).toString()));
		// System.out.println(getMonthBegin("2008/07/19"));
		// System.out.println(getMonthEnd("2008/07/19"));
//		System.out.println(addDate(d,15).toString());
//		System.out.println(getDateTime(new Date()));
//		System.out.println(getMillis(new Date()));

//		System.out.println(getSpecifiedDayAfter("2014-05-06"));
		
		System.out.println(getDay(new Date()));
//		System.out.println(getMillionSeconds("2014-06-26 05:05:05"));
//		System.out.println(System.currentTimeMillis());
//		Date edate = new SimpleDateFormat("yyyy-MM-dd").parse("2014-06-26");
//		System.out.println(DateUtil.getYear(new Date()));

//		getCalendarOfStr("20141225");
//		getCalendarOfStr("2014-12-25");
//	System.out.println(getHour(new Date()));
	}

}

