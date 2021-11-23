package com.oa.core.helper;

import com.oa.core.util.Const;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期辅助类
 * 
 * @author zxd
 * 
 */
public class DateHelper {

	public static Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}
	public static String timeNum() {
		long num = System.currentTimeMillis();
		return String.valueOf(num);
	}
	public static java.sql.Date getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return convert(sdf.format(new Date()));
	}

	public static String getYearMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(new Date());
	}
	
	public static String getYearMonthDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	public static String getYearMonthDayHourMinutes() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		return sdf.format(new Date());
	}
	public static String getYearMonthDayHourMinutesSecond() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	public static String getYMD() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	public static String getYM() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(new Date());
	}
	public static String getYMnew() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		return sdf.format(new Date());
	}

	public static String getYear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(new Date());
	}

	public static String getYMDHM() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new Date());
	}

	public static String getYMDHMS() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
//得到几天之前的数据
	public static String getold7(String time,int day) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd号");
		Date datetime = sdf.parse(time);
		Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
		calendar.setTime(datetime);
		calendar.add(Calendar.DATE, -day);
		return sdf.format(calendar.getTime());
	}
	
	/**
	 * 将字符串转换成sql时间
	 * @param dateStr
	 * @return
	 */
	public static java.sql.Date convert(String dateStr) {
		try {
			if(dateStr!=null && !dateStr.equals("")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date utilDate = sdf.parse(dateStr);
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				return sqlDate;
			}else{
				return null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将字符串转换成sql时间
	 * @param dateStr
	 * @return
	 */
	public static java.sql.Date convert(String dateStr,String type) {
		if(dateStr==null || dateStr.equals("")) {
			dateStr = getYMDHMS();
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(type);
			Date utilDate = sdf.parse(dateStr);
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			return sqlDate;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将字符串转换成sql时间
	 * @param dateStr
	 * @return
	 */
	public static java.sql.Timestamp convertTimestamp(String dateStr) {
		if(dateStr!=null && !dateStr.equals("")) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(Const.YEAR_MONTH_DAY_HH_MM_SS);
				Date utilDate = sdf.parse(dateStr);
				java.sql.Timestamp time = new java.sql.Timestamp(utilDate.getTime());
				return time;
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}else{
			return null;
		}
	}
	/**
	 * 获取现在时间
	 *
	 * @return返回字符串格式 yyyy-MM-dd
	 */
	public static String getStringDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}
	/**
   * 获取现在时间
   *
   * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
   */
	public static String getStringDateTime(Date datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(datetime);
		return dateString;
	}


	/**
	 *  获得当天0点时间
	 * @return
	 */
	public static Date getTimesmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();


	}
	/**
	 *  获得昨天0点时间
	 * @return
	 */
	public static Date getYesterdaymorning() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getTimesmorning().getTime()-3600*24*1000);
		return cal.getTime();
	}
	/**
	 *  获得当天近7天时间
	 * @return
	 */
	public static Date getWeekFromNow() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis( getTimesmorning().getTime()-3600*24*1000*7);
		return cal.getTime();
	}

	/**
	 *  获得当天24点时间
	 * @return
	 */
	public static Date getTimesnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 *  获得本周一0点时间
	 * @return
	 */
	public static Date getTimesWeekmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTime();
	}

	/**
	 *  获得本周日24点时间
	 * @return
	 */
	public static Date getTimesWeeknight() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getTimesWeekmorning());
		cal.add(Calendar.DAY_OF_WEEK, 7);
		return cal.getTime();
	}
	/**
	 *  获得上周一0点时间
	 * @return
	 */
	public static Date getTimesLastWeekmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.setTime(getTimesWeekmorning());
		cal.set(Calendar.WEEK_OF_MONTH,cal.get(Calendar.WEEK_OF_MONTH) - 1);
		return cal.getTime();
	}
	
	/**
	 *  获得上周日24点时间
	 * @return
	 */
	public static Date getTimesLastWeeknight() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getTimesLastWeekmorning());
		cal.add(Calendar.DAY_OF_WEEK, 7);
		return cal.getTime();
	}
	/**
	 *  获得上周五15点时间
	 * @return
	 */
	public static Timestamp getTimesLastFridayAt15() {
		Calendar cal = Calendar.getInstance();
		Date date=getTimesLastWeekmorning();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_WEEK, 4);
		cal.setTimeInMillis(cal.getTimeInMillis() +15*60*60*1000);
		return new Timestamp(cal.getTime().getTime());
	}
	/**
	 *  获得上周周X x:00点时间
	 * @return
	 * @param index 从0开始，0代表周一
	 * @param hour
	 * @return
	 */
	public static Timestamp getTimesLastWeekAtX(int index,int hour) {
		Calendar cal = Calendar.getInstance();
		Date date=getTimesLastWeekmorning();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_WEEK, index);
		cal.setTimeInMillis(cal.getTimeInMillis() +hour*60*60*1000);
		return new Timestamp(cal.getTime().getTime());
	}
	
	/**
	 *  获得本周五15点时间
	 * @return
	 */
	public static Timestamp getTimesFridayAt15() {
		Calendar cal = Calendar.getInstance();
		Date date=getTimesWeekmorning();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_WEEK, 4);
		cal.setTimeInMillis(cal.getTimeInMillis() +15*60*60*1000);
		return new Timestamp(cal.getTime().getTime());
	}
	/**
	 *  获得本周X x:00点时间
	 * @param index 代表周几，从0开始，0代表周一
	 * @param hour 输入小时，1代表1点
	 * @return
	 */
	public static Timestamp getTimesWeekAtX(int index,int hour) {
		Calendar cal = Calendar.getInstance();
		Date date=getTimesWeekmorning();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_WEEK, index);
		cal.setTimeInMillis(cal.getTimeInMillis() +hour*60*60*1000);
		return new Timestamp(cal.getTime().getTime());
	}
	
	/**
	 *  获得本月第一天0点时间
	 * @return
	 */
	public static Date getTimesMonthmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	/**
	 *  获得本月最后一天24点时间
	 * @return
	 */
	public static Date getTimesMonthnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 24);
		return cal.getTime();
	}

	/**
	 * 获得上月开始时间
	 * @return
	 */
	public static Date getLastMonthStartMorning() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getTimesMonthmorning());
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}
	/**
	 * 当前季度的开始时间
	 *
	 * @return
	 */
	public static Date getCurrentQuarterStartTime() {
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3) {
				c.set(Calendar.MONTH, 0);
			}else if (currentMonth >= 4 && currentMonth <= 6) {
				c.set(Calendar.MONTH, 3);
			}else if (currentMonth >= 7 && currentMonth <= 9) {
				c.set(Calendar.MONTH, 4);
			}else if (currentMonth >= 10 && currentMonth <= 12) {
				c.set(Calendar.MONTH, 9);
			}
			c.set(Calendar.DATE, 1);
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前季度的结束时间，即2012-03-31 23:59:59
	 *
	 * @return
	 */
	public static Date getCurrentQuarterEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentQuarterStartTime());
		cal.add(Calendar.MONTH, 3);
		return cal.getTime();
	}

	/**
	 * 得到指定月的天数
	 *
	 * @return
	 * */
	public static int getMonthLastDay(int year, int month)
	{
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);//把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}


	public static Date getCurrentYearStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));
		return cal.getTime();
	}

	public static Date getCurrentYearEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentYearStartTime());
		cal.add(Calendar.YEAR, 1);
		return cal.getTime();
	}

	public static Date getLastYearStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentYearStartTime());
		cal.add(Calendar.YEAR, -1);
		return cal.getTime();
	}
	
	public static String getTimeDifference(String dateStr) {
		if(dateStr==null||"".equals(dateStr)){
			return "";
		}
		String timeDifference="";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		try {
			Date date = df.parse(dateStr);
			
			long l = now.getTime() - date.getTime();
			
			long day = l / (24 * 60 * 60 * 1000);

			long hour = (l / (60 * 60 * 1000) - day * 24);

			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);

	//		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			if (day == 0 && hour == 0) {
				timeDifference = min + "分钟";
			} else if (day == 0 && hour > 0) {
				timeDifference = "" + hour + "小时";
			} else if ((day / 365) > 0) {
				if ((day % 365) < 30) {
					timeDifference = "" + (day / 365) + "年";
				} else {
					timeDifference = "" + (day / 365) + "年" + (day % 365) / 30+ "个月";
				}
			} else if ((day / 30) > 0) {
				if ((day % 30) == 0) {
					timeDifference = "" + (day / 30) + "个月";
				} else {
					timeDifference = "" + (day / 30) + "个月" + (day % 30) + "天";
				}
			} else if (hour == 0) {
				timeDifference = "" + day + "天";
			} else {
				timeDifference = "" + day + "天" + hour + "小时";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeDifference;
	}

	/**
	 * 获得时间差<br>
	 * 后传入的时间减去前一个传入的时间
	 * @param timeStr
	 * @param timeEnd
	 * @return 时间差
	 */
	public static Long getTimeDifference(Timestamp timeStr, Timestamp timeEnd) {
		if (null==timeStr||null==timeEnd){
			return null;
		}
		return timeEnd.getTime()-timeStr.getTime();
	}


	/**
	 * 判断指定时间是否在当前时间之前
	 * @param dateStr 形如2012-12-24参数
	 * @return
	 */
	public static boolean getDateDifference(String dateStr,String format) {
		if(dateStr==null||"".equals(dateStr)){
			return false;
		}
		SimpleDateFormat df = new SimpleDateFormat(format);
		try {
			Date date = df.parse(dateStr);
			//为true 则表示在当前时间之前，反之则在后
			return date.before(df.parse(df.format(new Date())));
		} catch (ParseException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static String getDateDifference(String dateStr) {
		if(getDateDifference(dateStr,"yyyy-MM-dd")){
			return "<span class='red'>"+dateStr+"</span>";
		}
		return dateStr;
	}
	/**
	 * 格式化时间
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateString(Date date,String format) {
		SimpleDateFormat timedf=new SimpleDateFormat(format);
		String time=timedf.format(date);
		return time;
	}
	/**
	 * 格式化时间
	 * @param date java.sql.Timestamp
	 * @param format
	 * @return
	 */
	public static String getDateString(Timestamp date,String format) {
		SimpleDateFormat timedf=new SimpleDateFormat(format);
		String time=timedf.format(date);
		return time;
	}
	
	public static Timestamp getSpecifiedTime(Timestamp timestamp, String time) {
		Date date = new Date(timestamp.getTime());
		Date specifiedTimeTemp = null;
		Timestamp specifiedTime = null;
		try {
			String dateStr = getDateString(date, "yyyy-MM-dd");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			specifiedTimeTemp = sdf.parse(dateStr + " "+time);
			specifiedTime=new Timestamp(specifiedTimeTemp.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return specifiedTime;
	}

	/**
	 * 格式化从数据库取出的date类型字符串
	 * */
	public static String getSqlDate(String date){
		if(date!=null && date.length()>10){
			date = date.substring(0,10);
			String type = "yyyy-MM-dd";
			Date sqldate = convert(date,type);
			return getDateString(sqldate,type);
		}else{
			return date==null?"":date;
		}

	}
	public static String getSqlDateTime(String dateTime){
		if(dateTime!=null && dateTime.length()>19){
			dateTime = dateTime.substring(0,19);
			String type = "yyyy-MM-dd HH:mm:ss";
			Date sqldate = convert(dateTime,type);
			return getDateString(sqldate,type);
		}else{
			return dateTime==null?"":dateTime;
		}
	}

	public static double getHour(String starDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return getHour(sdf, starDate, endDate);
	}
	public static double getHour(SimpleDateFormat sdf,String starDate, String endDate) {
		double hour = 0.0;
		try {
			Date dateStart = sdf.parse(starDate);
			Date dateEnd = sdf.parse(endDate);
			Calendar calStart = Calendar.getInstance();
			Calendar calEnd = Calendar.getInstance();
			calStart.setTime(dateStart);
			calEnd.setTime(dateEnd);
			hour = new Long(calEnd.getTimeInMillis() - calStart.getTimeInMillis()) / (3600 * 1000 * 1.0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return hour;
		}
	}

	public static int getWeekOfThisDate() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK);
		if (w < 0) {
			w = 0;
		}
		return w;
	}

	public static String getWeekOfDate(Date date) {
		if(date==null){
			date = new Date();
		}
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekDays[w];
	}

}
