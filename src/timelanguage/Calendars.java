package timelanguage;

import java.net.*;
import java.text.*;
import java.util.*;

abstract public class Calendars {
	static public final Calendar THE_BEGGINING = from(Long.MIN_VALUE);
	static public final Calendar THE_END = from(Long.MAX_VALUE);
    
	static public Calendar dayAfter(Calendar todayCalendar) {
		TimePoint todayPoint = TimePoint.from(todayCalendar);
		TimePoint tomorrowPoint = todayPoint.plus(Duration.days(1));
		return tomorrowPoint.asJavaCalendar();
//		Calendar tomorrowCalendar = (Calendar) today.clone();
//		tomorrow.add(Calendar.DATE, 1);
//		return tomorrow;
	}

	static public Calendar dayBefore(Calendar today) {
		Calendar yesterday = (Calendar) today.clone();
		yesterday.add(Calendar.DATE, -1);
		return yesterday;
	}

	static public String formatDate(Calendar calendar, String pattern) {
		if (calendar == null) return null;
		
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(calendar.getTime());
	}
	
	static public Calendar parseDate(String dateString, String pattern){
		DateFormat format = new SimpleDateFormat(pattern);
		Date date = format.parse(dateString, new ParsePosition(0));
		return from(date);
	}

	static public Calendar from(long milliseconds) {
		Calendar result = now();
		result.setTimeInMillis(milliseconds);
		return result;
	}

	static public Calendar from(Date date) {
		Calendar result = now();
		result.setTime(date);
		return result;
	}

	static public boolean isSameDay(TimePoint first, TimePoint second) {
		if (first.equals(second)) 
			return true;   
                 
		if ((first == null) || (second == null)) 
			return false;
            
		Calendar firstCalendar = first.asJavaCalendar();
		Calendar secondCalendar = second.asJavaCalendar();
		
		return 
			(firstCalendar.get(Calendar.YEAR) == secondCalendar.get(Calendar.YEAR)) &&
			(firstCalendar.get(Calendar.DAY_OF_YEAR) == secondCalendar.get(Calendar.DAY_OF_YEAR));
	}

	static public boolean isWeekend(Calendar day) {
		return 
			(day.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) ||
			(day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
	}

	static public Calendar now() {
		return new GregorianCalendar();
	}

	static public Calendar setToMidnight(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);	
		calendar.set(Calendar.MINUTE, 0);	
		calendar.set(Calendar.SECOND, 0);	
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}	

	static public Calendar tomorrow() {
		return dayAfter(now());
	}

	static public Calendar yesterday() {
		return dayBefore(now());
	}
    
	static private final String timeServer = System.getProperty("NIST.TIMESERVER", "time.nist.gov");
	static public String getTimeFromTimeServer() {
		try {
			byte buffer[] = new byte[256];
			Socket socket = new Socket(timeServer, 13);
			int length = socket.getInputStream().read(buffer);
			return new String(buffer, 0, length);
		} catch (Exception exception) {
			return null;
		}
	}

}
