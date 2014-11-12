package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

/**
 * 日期系统公共方法
 * 
 * @version 1.0
 * @author open sourc added by yuezengguang
 */
public class DateUtil {

	public static final String CN_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String CN_DATE_FORMAT = "yyyy-MM-dd";
	private static FastDateFormat fdfWithoutTime = FastDateFormat.getInstance(CN_DATE_FORMAT, TimeZone.getDefault(), Locale.getDefault());

	public static long date2Long(Date date) {
		long longTime = -1;
		if (date != null)
			longTime = date.getTime() / 1000;
		return longTime;
	}

	public static long StringDate2Long(String stime) {
		Date date = String2Date(stime);
		return date2Long(date);
	}

	public static String String2HHMMSS(String stime) {
		if ("".equals(stime) || stime == null)
			return "";
		if (stime != null && stime.trim().length() <= 10) {
			return stime + " 00:00:00";
		} else {
			return stime;
		}
	}

	/**
	 * 把日期的时分秒删掉,返回字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String date2StringYMD(Date date) {
		String time = date.toString();
		return (time.split(" "))[0];
	}

	public static Date String2Date(String stime) {
		if (StringUtils.isEmpty(stime)) {
			return null;
		}

		Date date = null;
		if (stime != null && stime.trim().length() > 10) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				date = sdf.parse(stime);
			} catch (ParseException e) {
				System.out.println("Time format error.");
			}
		} else if (stime != null && stime.trim().length() <= 10) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = sdf.parse(stime);
			} catch (ParseException e) {
				System.out.println("Time format error.");
			}
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat();
			try {
				date = sdf.parse(stime);
			} catch (ParseException e) {
				System.out.println("Time format error.");
			}
		}
		return date;
	}

	public static String long2StringDate(long time) {
		SimpleDateFormat sf = new SimpleDateFormat(CN_TIME_FORMAT);
		return sf.format(long2Date(time));
	}

	public static String long2StringDateNoYear(long time) {
		SimpleDateFormat sf = new SimpleDateFormat("MM-dd HH:mm:ss");
		return sf.format(long2Date(time));
	}

	public static String long2StringDateNoTime(long time) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(long2Date(time));
	}

	public static String long2StringDateNoYearAndTime(long time) {
		SimpleDateFormat sf = new SimpleDateFormat("MM-dd");
		return sf.format(long2Date(time));
	}

	public static Date long2Date(long time) {
		return new Date(time * 1000);
	}

	public static String date2String(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sf = new SimpleDateFormat(CN_TIME_FORMAT);
		return sf.format(date);
	}

	public static String date2StringDate1(String s) {
		@SuppressWarnings("deprecation")
		Date date = new Date(s);
		SimpleDateFormat sf = new SimpleDateFormat(CN_TIME_FORMAT);
		return sf.format(date);
	}

	public static String date2StringDate(String s) {
		Date date = new Date(s);
		SimpleDateFormat sf = new SimpleDateFormat(CN_DATE_FORMAT);
		return sf.format(date);
	}

	public static String currentTime() {
		SimpleDateFormat sf = new SimpleDateFormat(CN_TIME_FORMAT);
		return sf.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return Date
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}
	
	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return String
	 */
	public static String getDayBefore(int day) {
		SimpleDateFormat sf = new SimpleDateFormat(CN_TIME_FORMAT);
		String todayString = sf.format(getDateBefore(new Date(), day));
		todayString = todayString.substring(0, 10) + " 00:00:00";
		return todayString;
	}

	/**
	 * 得到几天前的日期 格式如2012-02-13
	 * 
	 * @param day
	 * @return
	 */
	public static String getDayBeforeNoTime(int day) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(getDateBefore(new Date(), day));
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * 得到几天后的日期 格式如2012-02-13
	 * 
	 * @param day
	 * @return
	 */
	public static String getDateAfterNoTime(int day) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(getDateAfter(new Date(), day));
	}

	/**
	 * 获取今天的日期
	 * 
	 * @return
	 */
	public static String getTodayDateNoTime() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(new Date());
	}

	/**
	 * 获取7天前的日期
	 * 
	 * @return
	 */
	public static String getWeekAgoDateNoTime() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 7);
		return sf.format(c.getTime());
	}

	/**
	 * 将String转换为java.util.Date
	 * 
	 * @param strDate
	 * @param formatType
	 * @return
	 * @throws Exception
	 */
	public static java.util.Date string2UtilDate(String strDate, String formatType) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formatType);
			java.util.Date date = sdf.parse(strDate);
			return date;
		} catch (ParseException e) {
			throw new Exception(e.getMessage());
		}
	}

	public static String date2StringDate(Date date, String formatType) {
		if (formatType == null || "".equals(formatType)) {
			formatType = "yyyy-MM-dd";
		}
		SimpleDateFormat sf = new SimpleDateFormat(formatType);
		return sf.format(date);
	}

	public static String getMinDate(String date1, String date2) {
		if (date1 != null && date2 != null && !"".equals(date1) && !"".equals(date2)) {
			if (date1.compareTo(date2) < 0) {
				return date1;
			} else {
				return date2;
			}
		}
		return getTodayDateNoTime();
	}

	public static Map<String, String> getDefaultDateRange(Date d1, Date d2) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = null;
		String date2 = null;
		if (d1 != null) {
			date1 = sf.format(d1);
		}
		if (d2 != null) {
			date2 = sf.format(d2);
		}
		System.out.println("****************************");
		System.out.println(date1 + "-------------" + date2);
		System.out.println("****************************");
		Map<String, String> map = new HashMap<String, String>();
		String today = getTodayDateNoTime();
		String tempdate = null;// date2-6

		if (date1 == null || "".equals(date1) || date1.compareTo(today) > 0) {
			map.put("st", today);
			map.put("et", today);
		} else {
			if (date2 != null && !"".equals(date2) && date2.compareTo(today) < 0) {
				map.put("et", date2);
				try {
					tempdate = sf.format(getDateBefore(sf.parse(date2), 6));
					if (tempdate.compareTo(date1) < 0) {
						map.put("st", date1);
					} else {
						map.put("st", tempdate);
					}
				} catch (ParseException e) {
					e.printStackTrace();
					map.put("st", date2);
				}
			} else {
				map.put("et", today);
				try {
					tempdate = sf.format(getDateBefore(sf.parse(today), 6));
					if (tempdate.compareTo(date1) < 0) {
						map.put("st", date1);
					} else {
						map.put("st", tempdate);
					}
				} catch (ParseException e) {
					e.printStackTrace();
					map.put("st", today);
				}
			}
		}
		return map;
	}
	

	/**
	 * 获得今日零时时间(String类型 yyyy-MM-dd HH:mm:ss)
	 * 
	 * @return String
	 */
	public static String getTodayString() {
		Date today = new Date();
		String str = "";
		str = fdfWithoutTime.format(today);
		str = str + " 00:00:00";
		return str;
	}

	/**
	 * 将java.sql.Date转化为不带时间的String
	 * 
	 * @param date
	 * @return String
	 */
	public static String date2String(java.sql.Date date) {
		return fdfWithoutTime.format(date);
	}

	public static Date dayStart(Date date) {
		String str = fdfWithoutTime.format(date);
		str = str + " 00:00:00";
		return String2Date(str);
	}

	public static Date dayEnd(Date date) {
		String str = fdfWithoutTime.format(date);
		str = str + " 23:59:59";
		return String2Date(str);
	}
	
	public static String todayStartStr() {
		String str = fdfWithoutTime.format(now());
		str = str + " 00:00:00";
		return str;
	}

	public static String todayEndStr() {
		String str = fdfWithoutTime.format(now());
		str = str + " 23:59:59";
		return str;
	}

	public static Date now() {
		Date now = new Date();
		return now;
	}

	/**
	 * 取得系统时间往前duration小时的时间（字符串类型：yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param formatType
	 *            指定返回日期格式
	 * @return
	 */
	public static String getTimeBeforeDuration(Double duration, String formatType) {
		if ("".equals(formatType)) {
			formatType = CN_TIME_FORMAT;
		}
		java.util.Date date = new java.util.Date();
		long Time = date.getTime() - new Double(1000 * 60 * 60 * duration).longValue();

		date = new java.util.Date(Time);
		return date2String(date, formatType);
	}

	/**
	 * 将java.util.Date转换为formatType格式的String
	 * 
	 * @param date
	 * @param formatType
	 * @return
	 * @throws Exception
	 */
	public static String date2String(java.util.Date date, String formatType) {
		if ("".equals(formatType)) {
			formatType = CN_DATE_FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatType);
		String strDate = sdf.format(date);
		return strDate;
	}

	public static Date getDateFromTime(Integer time) {
		if (time == null) {
			return null;
		}
		return new java.util.Date(time * 1000L);
	}

	/**
	 * 取得当前日期，String格式
	 * 
	 * @param formatType
	 *            返回日期的格式，如yyyyMMdd
	 * @return
	 */
	public static String getNowDate4String(String formatType) {
		if ("".equals(formatType)) {
			formatType = CN_DATE_FORMAT;
		}
		return date2String(new java.util.Date(), formatType);
	}

	public static String getDateStrFromTime(Integer time) {
		if (time == null) {
			return "";
		}
		return date2String(new java.util.Date(time * 1000L), "yyyy-MM-dd HH:mm:ss");
	}

	public static void main(String[] args) {
		
		JSONObject jsonObj = JSONObject.fromObject("{name:'ok',id:1,userJoinTime:{nanos:0}}");
		System.out.println(jsonObj);
		System.out.println(((JSONObject)jsonObj.get("userJoinTime")).get("nanos"));
		
		System.out.println(getDayBefore(6));
		System.out.println(System.currentTimeMillis());
	}
	
	public static Date parse(String strDate, String pattern)throws ParseException{
		return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(
				pattern).parse(strDate);
	}
	
	public static List<String>  getDateRange(String d1, String d2) {
		List <String> list = new ArrayList();
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				
		String sd=d1;
		int i=0;
		while(true){
			if (d1.compareTo(d2) < 0) {					
				try {		
					list.add(i,"'"+d1+"'");
					i++;
					d1 = sf.format(getDateAfter(sf.parse(sd), i));	
				} catch (ParseException e) {				
					e.printStackTrace();
				}	
			}else{
				break;
			}
		}
		list.add(i,"'"+d2+"'");
		return list;
	}
	public static List<String> dateToWeek(String mdate,String time,String pt_bengin, String pt_end){
		List <String> list = new ArrayList();			
		String day="";	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");			
		Date now = new Date();	
		if(StringUtils.isEmpty(pt_end) && StringUtils.isEmpty(pt_bengin)){
			for(int i=0;i<Integer.parseInt(time);i++){
				day = "'"+sdf.format(util.DateUtil.getDateBefore(now,Integer.parseInt(time)-1-i))+"'";				
				list.add(i,day);				
			}		
		}	
			
		if(StringUtils.isNotEmpty(pt_bengin)){
			if(StringUtils.isEmpty(pt_end)){
				pt_end=sdf.format(new Date()); 
			}
					
			list=util.DateUtil.getDateRange(pt_bengin,pt_end);
		}
		 
		return list;
	 }
	public static List<String> indateToWeek(String mdate,String time,String pt_bengin, String pt_end){	
		List <String> inlist = new ArrayList();	
		String inday="";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");			
		Date now = new Date();	
		if(StringUtils.isEmpty(pt_end) && StringUtils.isEmpty(pt_bengin)){
			for(int i=0;i<Integer.parseInt(time);i++){				
				inday = sdf.format(util.DateUtil.getDateBefore(now,Integer.parseInt(time)-1-i));			
				inlist.add(i,inday);
			}		
		}				
		if(StringUtils.isNotEmpty(pt_bengin)){
			if(StringUtils.isEmpty(pt_end)){
				pt_end=sdf.format(new Date()); 
			}							
			inlist=util.DateUtil.getDateRange(pt_bengin,pt_end);
		}
		 
		return inlist;
	 }
}
