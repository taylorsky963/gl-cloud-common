package com.gl.myparadise.common.tools.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

public class DateTimeUtil {

	private static final long SECOND = 1000L;

	private DateTimeUtil() {
	}

	public static String dateformat = "yyyy-MM-dd HHmmss";
	public static String dateformat_yyyymmdd = "yyyy-MM-dd";

	public static String[] fecthAllTimeZoneIds() {

		Vector v = new Vector();

		String[] ids = TimeZone.getAvailableIDs();

		for (int i = 0; i < ids.length; i++) {

			v.add(ids[i]);

		}

		java.util.Collections.sort(v, String.CASE_INSENSITIVE_ORDER);

		v.copyInto(ids);

		v = null;

		return ids;
	}
	/**
	 * 获得当前时间 yyyy-dd-mm HH:MM:SS
	 * 
	 * @return
	 */
	public static String getNowDateTime() {
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		return format.format(new Date());
	}
	/**
	 * 获得当前日期 yyyy-dd-mm
	 * 
	 * @return
	 */
	public static String getNowDate() {
		SimpleDateFormat format = new SimpleDateFormat(dateformat_yyyymmdd);
		return format.format(new Date());
	}

	/**
	 * 字符串转换成时间
	 * 
	 * @return
	 */
	public static Date formatStringToDate(String date, String dateformat) {

		if (date == null && date.length() == 0) {
			return null;
		}

		if (dateformat == null && dateformat.length() == 0) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		try {
			return format.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 时间换成字符串转
	 * 
	 * @return
	 */
	public static String formatDateToString(Date date, String dateformat, String targetId) {

		if (date == null) {

			return null;

		}

		if (dateformat == null && dateformat.length() == 0) {

			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat(dateformat);

		if(targetId != null && !"".equals(targetId)){

			TimeZone timeZone = TimeZone.getTimeZone(targetId);

			format.setTimeZone(timeZone);

		}

		return format.format(date);

	}

	/**
	 * 时间换成字符串转
	 *
	 * @return
	 */
	public static String formatDateToString(Date date, String dateformat) {

		if (date == null) {

			return null;

		}

		if (dateformat == null && dateformat.length() == 0) {

			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat(dateformat);

		return format.format(date);

	}

	public static Date addTime(Date date, Long time) {

		Date now = new Date();

		long afterTime = (date.getTime() / SECOND) + time;

		now.setTime(afterTime * SECOND);

		return now;

	}

	public static Date reduceTime(Date date, Long time) {

		Date now = new Date();

		long afterTime = (date.getTime() / SECOND) - time;

		now.setTime(afterTime * SECOND);

		return now;
	}

	public static Date reduceNowTime(Long time) {

		Date now = new Date();

		long afterTime = (now.getTime() / SECOND) - time;

		now.setTime(afterTime * SECOND);

		return now;
	}

	public static Date addNowTime(Long time) {

		Date now = new Date();

		long afterTime = (now.getTime() / SECOND) + time;

		now.setTime(afterTime * SECOND);

		return now;
	}

	public static boolean addNowTimeCompare(Date date, Long time) {

		Date now = new Date();

		long afterTime = (date.getTime() / SECOND) + time;

		date.setTime(afterTime * SECOND);

		return now.after(date);
	}

	public static boolean addNowTimeCompare(Long pastTime, Long time) {

		Date now = new Date();

		long afterTime = pastTime + (time * SECOND);

		if(afterTime > now.getTime()){

			return true;

		}

		return false;
	}


	public static void main(String args[]){
		Date now = new Date();

		System.out.println(formatDateToString(now, "hh:mm:ss aa","Asia/Taipei"));

	}
}
