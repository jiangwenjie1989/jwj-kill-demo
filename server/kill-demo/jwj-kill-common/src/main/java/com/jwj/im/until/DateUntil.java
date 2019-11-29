package com.jwj.im.until;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 使用 {@link org.joda.time} 的时间工具类
 * 
 * @author Administrator
 *
 */
public class DateUntil {



	public static final String FORMAT_HH_MM = "HH:mm";

	public static final String FORMAT_HH_MM_SS = "HH:mm:ss";

	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

	public static final String D = "d";

	public static final String FORMAT_MMDD = "MM-dd";

	public static final String FORMAT_MMDD_HHMM_C = "MM月dd日 HH:mm";

	public static final String FORMAT_MD_HHMM_C = "M月d日 HH:mm";

	public static final String FORMAT_YYYMMDDHHMMSS = "yyyyMMddHHmmss";

	public static final String FORMAT_YYYMMDDHHMMSS_SSS = "yyyyMMddHHmmssSSS";

	public static final String FORMAT_YYYYMMDD = "yyyyMMdd";

	public static final String FORMAT_YYYYMMDD_HHMM = "yyyy-MM-dd HH:mm";

	public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy.MM.dd HH:mm";

	public static final String FORMAT_CN_YESTERDAY_HH_MM = "昨天 HH:mm";

	public static final String FORMAT_THIS_YEAR_MM_DD_HH_MM = "MM-dd HH:mm";

	public static final String FORMAT_LAST_YEAR_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

	/**
	 * 获取当前时间戳
	 * 
	 * @return {@link java.sql.Timestamp}
	 */
	public static Timestamp getNowTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 获取当前时间到 23:59:59秒时间差字符串
	 * 
	 * @return 0 小时 0 分 0 秒
	 */
	public static String betweenOfNowToEndDate() {
		DateTime now = new DateTime();
		DateTime end = now.millisOfDay().withMaximumValue();

		return betweenOfDate(now, end);
	}

	/**
	 * 获取2个时间时间差字符串
	 * 
	 * @return 0 小时 0 分 0 秒
	 */
	public static String betweenOfDate(DateTime start, DateTime end) {
		int hours = end.getHourOfDay() - start.getHourOfDay();
		int minutes = end.getMinuteOfHour() - start.getMinuteOfHour();
		int seconds = end.getSecondOfMinute() - start.getSecondOfMinute();

		return hours + "小时" + minutes + "分" + seconds + "秒";
	}

	/**
	 * 格式化当前时间
	 * 
	 * @param format
	 *            自定义格式
	 * @return
	 */
	public static String getNowTime(String format) {

		return LocalDateTime.now().toString(format);
	}

	/**
	 * 获取 当前 {@link java.util.Date} 格式的时间
	 * 
	 * @return
	 */
	public static Date getJdkNowDate() {

		return LocalDateTime.now().toDate();
	}

	/**
	 * 将时间戳 {@link java.sql.Timestamp}转换成 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String getFormatTime(Timestamp timestamp) {
		if (timestamp != null) {
			LocalDateTime local = LocalDateTime.fromDateFields(timestamp);
			return local.toString(FORMAT_YYYY_MM_DD_HH_MM_SS);
		}
		return "";
	}

	/**
	 * 将时间戳 {@link java.sql.Timestamp}转换成 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String getFormatTime(Timestamp timestamp, String format) {
		if (timestamp != null) {
			LocalDateTime local = LocalDateTime.fromDateFields(timestamp);
			return local.toString(format);
		}
		return "";
	}

	/**
	 * 将日期 {@link java.util.Date} 转换成 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getFormatTime(Date date, String format) {
		if (date != null) {
			LocalDateTime local = LocalDateTime.fromDateFields(date);
			return local.toString(format);
		}
		return "";

	}

	/**
	 * 将日期 {@link java.util.Date} 转换成 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getFormatTime(Date date) {
		if (date != null) {
			LocalDateTime local = LocalDateTime.fromDateFields(date);
			return local.toString(FORMAT_YYYY_MM_DD_HH_MM_SS);
		}
		return "";

	}

	/**
	 * 把特定的时间 转换成 字符串
	 * 
	 * @param datetime
	 *            {@link org.joda.time.DateTime}
	 * @param format
	 * @return
	 */
	public static String getFormat(DateTime datetime, String format) {
		if (datetime != null) {
			return datetime.toString(format);
		}
		return "";

	}

	/**
	 * 返回今天的时间 格式 yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getNowDate() {
		return LocalDate.now().toString();
	}

	public static String getYesterday() {
		return LocalDate.now().minusDays(1).toString();
	}

	public static boolean isYesterday(Timestamp time){
		return getFormatTime(time, FORMAT_YYYY_MM_DD).equals(getYesterday());
	}

	/**
	 * 判断日期是否过期(对于当前时间)
	 * 
	 * @param datetime
	 * @return true 表示过期 false 表示未过期
	 */
	public static boolean isExpire(DateTime datetime) {

		return datetime.isAfterNow();
	}


	public static boolean isSameDay(Timestamp time1,Timestamp time2){
		Assert.notNull(time1, "时间戳不能为空");
		Assert.notNull(time2, "时间戳不能为空");
		return getFormatTime(time1, FORMAT_YYYYMMDD).equals(getFormatTime(time2, FORMAT_YYYYMMDD));
	}

	public static boolean isContinuous(Timestamp time1,Timestamp time2){
		Assert.notNull(time1, "时间戳不能为空");
		Assert.notNull(time2, "时间戳不能为空");
		String t1=getFormatTime(time1, FORMAT_YYYY_MM_DD);
		String t2=getFormatTime(time2, FORMAT_YYYY_MM_DD);
		return t1.equals(minusDay(t2, 1));
	}

	/**
	 * time1 大于  time2 为真
	 * @param time1  yyyy-MM-dd
	 * @param time2  yyyy-MM-dd
	 * @return
	 */
	public static boolean compare(String time1,String time2){
		return LocalDate.parse(time1).isAfter(LocalDate.parse(time2));
	}
	/**
	 * 是否是今天
	 * 
	 * @param timestamp
	 * @return
	 */
	public static boolean isToday(Timestamp timestamp) {

		DateTime input = new DateTime(timestamp);

		DateTime start =DateTime.now().millisOfDay().withMinimumValue();

		DateTime end = DateTime.now().millisOfDay().withMaximumValue();

		return input.isAfter(start)&&input.isBefore(end);
	}

	/**
	 * 是否是本周
	 * 
	 * @param timestamp
	 * @return
	 */
	public static boolean isWeek(Timestamp timestamp) {
		DateTime input = new DateTime(timestamp);

		DateTime start =DateTime.now().dayOfWeek().withMinimumValue();
		DateTime end = DateTime.now().dayOfWeek().withMaximumValue();

		return input.isAfter(start)&&input.isBefore(end);
	}

	/**
	 * 是否是本月
	 * 
	 * @param timestamp
	 * @return
	 */
	public static boolean isMonth(Timestamp timestamp) {
		DateTime input = new DateTime(timestamp);

		DateTime start =DateTime.now().dayOfMonth().withMinimumValue();
		DateTime end = DateTime.now().dayOfMonth().withMaximumValue();
		return input.isAfter(start)&&input.isBefore(end);
	}

	/**
	 * 输入 yyyy-MM-dd
	 * 
	 * @param date
	 * @return 输入的时间 是否在大于今天
	 */
	public static boolean isExpire(String date) {
		DateTime d = new DateTime(date);
		DateTime input = d.millisOfDay().withMaximumValue();
		return input.isAfterNow();
	}

	/**
	 * 输入 yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(String date) {
		String now = LocalDate.now().toString();
		return now.equals(date);
	}

	/**
	 * 指定日期减少一天
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static String minusDay(String date, long day) {
		LocalDate localdate = LocalDate.parse(date).minusDays(1);
		return localdate.toString();
	}

	public static boolean isWeekEnd(String date) {
		LocalDate ldate = LocalDate.parse(date);
		LocalDate ld = ldate.dayOfWeek().withMaximumValue();
		return ldate.equals(ld);
	}

	public static boolean isMonthEnd(String date) {
		LocalDate ldate = LocalDate.parse(date);
		LocalDate ld = ldate.dayOfMonth().withMaximumValue();
		return ldate.equals(ld);
	}

	/**
	 * 获取N分钟后的时间戳
	 * 
	 * @param minutes
	 * @return
	 */
	public static Timestamp getAddMinTime(int minutes) {
		long time = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(minutes);
		return new Timestamp(time);
	}

	/**
	 * 获取时间差距
	 * 几秒前 几分钟前  几小时前 几天前
	 * @param timestamp
	 * @return
	 */
	public static String leaveTime(Timestamp timestamp) {
		DateTime dt = new DateTime(timestamp);
		DateTime now=new DateTime();
		long dtM=dt.getMillis();
		long nowM=now.getMillis();

		long leave=nowM-dtM;//毫秒
		long second=leave/1000;
		if(second<60){
			return second+"秒前";
		}else{
			long minute=second/60;
			if(minute<60){
				return minute+"分钟前";
			}else{
				long hour=minute/60;
				if(hour<24){
					return hour+"小时前";
				}else{
					long day=hour/24;
					if(day<30){
						return day+"天前";
					}
				}
			}
		}

		return getFormatTime(timestamp);
	}

	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0){
			return "00:00";
		} else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99) {
					return "99:59:59";
				}
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10){
			retStr = "0" + Integer.toString(i);
		}else{
			retStr = "" + i;
		}
		return retStr;
	}

	public static String transDateTime(Timestamp time) {
		String ret = "";

		Calendar current = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		Calendar yesterday = Calendar.getInstance();
		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);
		yesterday.set(Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);
		Calendar thisYear = Calendar.getInstance();
		thisYear.set(Calendar.YEAR, current.get(Calendar.YEAR));
		thisYear.set(Calendar.MONTH, current.get(Calendar.MONTH));
		thisYear.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
		thisYear.set(Calendar.HOUR_OF_DAY, 0);
		thisYear.set(Calendar.MINUTE, 0);
		thisYear.set(Calendar.SECOND, 0);
		Calendar lastYear = Calendar.getInstance();
		lastYear.set(Calendar.YEAR, current.get(Calendar.YEAR) - 1);
		lastYear.set(Calendar.MONTH, current.get(Calendar.MONTH));
		lastYear.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
		lastYear.set(Calendar.HOUR_OF_DAY, 0);
		lastYear.set(Calendar.MINUTE, 0);
		lastYear.set(Calendar.SECOND, 0);

		current.setTime(time);

		if (current.after(today)) {
			ret = getFormatTime(time, FORMAT_HH_MM);
		} else if (current.before(today) && current.after(yesterday)) {
			ret = getFormatTime(time, FORMAT_CN_YESTERDAY_HH_MM);
		} else if (current.getWeekYear() - thisYear.getWeekYear() == 0) {
			ret = getFormatTime(time, FORMAT_THIS_YEAR_MM_DD_HH_MM);
		} else if (current.getWeekYear() - thisYear.getWeekYear() < 0) {
			ret = getFormatTime(time, FORMAT_LAST_YEAR_YYYY_MM_DD_HH_MM);
		}
		return ret;
	}

	/** 
	 * 根据传入的时间获得当前时间所在周的第一天和第七天日期 
	 * @param tm 时间 
	 * @param firstday 周日作为周一为0，周一作为周一1。 
	 * @return 
	 */  
	public static List<Date> getWeek(Date tm,int firstday){  
		Calendar c = Calendar.getInstance();  
		c.setTime(tm);  
		if(c.get(Calendar.DAY_OF_WEEK)==1){  
			c.add(Calendar.DATE, -1);  
		}  
		List<Date> list = new ArrayList<Date>();  
		Calendar cf = Calendar.getInstance();  
		cf.setTime(c.getTime());  
		cf.set(Calendar.DAY_OF_WEEK, cf.getFirstDayOfWeek());  
		cf.add(Calendar.DATE, firstday);  
		Calendar ce = Calendar.getInstance();  
		ce.setTime(c.getTime());  
		ce.set(Calendar.DAY_OF_WEEK, cf.getFirstDayOfWeek()+6);  
		ce.add(Calendar.DATE, firstday);  
		list.add(cf.getTime());  
		list.add(ce.getTime());  
		return list;  
	}

	/**
	 * @Title: getThisWeekMonday  
	 * @Description: 本周的周一 
	 * @param date 当前时间
	 * @return
	 * @return String yyyy-MM-dd
	 * @author ycs  
	 * @date 2018年7月4日
	 */
	public static String getThisWeekMonday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0) {
			day_of_week = 7;
		}
		c.add(Calendar.DATE, -day_of_week + 1);
		return getFormatTime(c.getTime(), DateUntil.FORMAT_YYYY_MM_DD);
	}

	/**
	 * @Title: getThisWeekSunday  
	 * @Description: 本周的周日  
	 * @param date 当前时间
	 * @return
	 * @return String yyyy-MM-dd
	 * @author ycs 
	 * @date 2018年7月4日
	 */
	public static String getThisWeekSunday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0){
			day_of_week = 7;
		}
		c.add(Calendar.DATE, -day_of_week + 7);
		return getFormatTime(c.getTime(), DateUntil.FORMAT_YYYY_MM_DD);
	}


	/**
	 * @Title: geLastWeekMonday  
	 * @Description: 上周的周一 
	 * @param date 当前时间
	 * @return
	 * @return String yyyy-MM-dd
	 * @author ycs 
	 * @date 2018年7月4日
	 */
	public static String geLastWeekMonday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getThisWeekMondayDate(date));
		cal.add(Calendar.DATE, -7);
		return getFormatTime(cal.getTime(), DateUntil.FORMAT_YYYY_MM_DD);
	}

	/**
	 * @Title: geLastWeekSunday  
	 * @Description: 上周的周日
	 * @param date 当前时间
	 * @return
	 * @return String yyyy-MM-dd
	 * @author ycs 
	 * @date 2018年7月4日
	 */
	public static String geLastWeekSunday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getThisWeekMondayDate(date));
		cal.add(Calendar.DATE, -1);
		return getFormatTime(cal.getTime(), DateUntil.FORMAT_YYYY_MM_DD);
	}

	/**
	 * @Title: getThisWeekMonday  
	 * @Description: 本周的周一 
	 * @param date 当前时间
	 * @return
	 * @return Date
	 * @author ycs 
	 * @date 2018年7月4日
	 */
	public static Date getThisWeekMondayDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 获得当前日期是一个星期的第几天
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		// 获得当前日期是一个星期的第几天
		int day = cal.get(Calendar.DAY_OF_WEEK);
		// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
		return cal.getTime();
	}

	/**
	 * @Title: getNextWeekMonday  
	 * @Description: 下周周一  
	 * @param date 当前时间
	 * @return
	 * @return String yyyy-MM-dd
	 * @author ycs 
	 * @date 2018年7月4日
	 */
	public static String getNextWeekMonday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getThisWeekMondayDate(date));
		cal.add(Calendar.DATE, 7);
		return getFormatTime(cal.getTime(), DateUntil.FORMAT_YYYY_MM_DD);
	}

	/**
	 * @Title: getNextWeekMonday  
	 * @Description: 下周周日
	 * @param date 当前时间
	 * @return
	 * @return String yyyy-MM-dd
	 * @author ycs 
	 * @date 2018年7月4日
	 */
	public static String getNextWeekSunday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getThisWeekMondayDate(date));
		cal.add(Calendar.DATE, 13);
		return getFormatTime(cal.getTime(), DateUntil.FORMAT_YYYY_MM_DD);
	}

	/**
	 * @Title: getDateBefore  
	 * @Description: 得到几天前的时间
	 * @param date 某个时间
	 * @param day 前几天
	 * @param format 格式化
	 * @return Date
	 * @author ycs 
	 * @date 2018年7月18日
	 */
	public static String getDateBefore(Date date, int day, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) - day);
		return getFormatTime(cal.getTime(), format);
	}

	/**
	 * 得到当前时间的前N小时
	 * @param ihour N小时
	 * @return
	 */
	public static String getBeforeByHourTime(int ihour){ 
		String returnstr = "";    
		Calendar calendar = Calendar.getInstance();  
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - ihour);     
		SimpleDateFormat df = new SimpleDateFormat(DateUntil.FORMAT_YYYY_MM_DD_HH_MM_SS);   
		returnstr = df.format(calendar.getTime());    
		return returnstr;  
	} 

	/** 
	 * 获取指定时间的N分钟前/后的时间 
	 * @return 
	 */  
	public static String getTimeByMinute(Timestamp timestamp, String format, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(timestamp);
		calendar.add(Calendar.MINUTE, minute);  
		return new SimpleDateFormat(format).format(calendar.getTime());
	} 

	/**
	 * @Title: getDateBefore  
	 * @Description: 得到几天前的时间 
	 * @param timestamp 当前日期
	 * @param day 天数
	 * @return
	 * @return String
	 * @author ycs 
	 * @date 2018年11月5日
	 */
	public static String getDateBefore(Timestamp timestamp, String format, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(timestamp);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - day);
		return new SimpleDateFormat(format).format(calendar.getTime());
	}
	
	/**
	 * @Title: getDateAfter  
	 * @Description: 得到几天后的时间  
	 * @param timestamp 当前日期
	 * @param day 天数
	 * @return
	 * @return String
	 * @author ycs 
	 * @date 2018年11月5日
	 */
	public static String getDateAfter(Timestamp timestamp, String format, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(timestamp);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + day);
		return new SimpleDateFormat(format).format(calendar.getTime());
	}

	/*public static void main(String[] args) {
		//15分钟之后的时间
		Timestamp valueOf = Timestamp.valueOf(getTimeByMinute(getNowTimestamp(), 15));
		Timestamp nowTimestamp = getNowTimestamp();
		System.out.println(getDateBefore(nowTimestamp, 1));
		System.out.println(getTimeByMinute(nowTimestamp, 15));
	}*/
}
