package timelanguage;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class  TimePoint {
	static public final TimePoint THE_BEGGINING = from("1/1/0000", "MM/dd/yyyy");
	static public final TimePoint THE_END = from("12/31/9999", "MM/dd/yyyy");

	final long millisecondsFromEpoc;
	
	public static TimePoint atMidnight(int year, int month, int date) {
		return from(year, month, date, 0, 0, 0, 0);
	}
	
	public static TimePoint from(int year, int month, int date, int hour, String am_pm) {
		return from(year, month, date, convertTo24hourCycle(hour, am_pm));
	}
	
	private static int convertTo24hourCycle(int hour, String am_pm) {
//		assert(am_pm.equalsIgnoreCase("AM") || am_pm.equalsIgnoreCase("PM"));		
		int translatedAmPm = (am_pm.equalsIgnoreCase("AM") ? 0 : 12);
		translatedAmPm -= (hour == 12) ? 12 : 0;
		return hour + translatedAmPm;
	}

	public static TimePoint from(int year, int month, int date, int hour) {
		return from(year, month, date, hour, 0, 0, 0);
	}
	
	public static TimePoint from(int year, int month, int date, int hour, int minute, int second, int millisecond) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DATE, date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, millisecond);
		return from(calendar);		
	}
	
	public static TimePoint from(Calendar calendar) {
		return from(calendar.getTime());
	}

	public static TimePoint from(String dateString, String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		Date date = format.parse(dateString, new ParsePosition(0));
		return from(date);
	}
	
	public static TimePoint from(Date javaDate) {
		return from(javaDate.getTime());
	}

	public static TimePoint now() {
		return from(new Date());
	}

	
	
	public static TimePoint from(long milliseconds) {
		return new TimePoint(milliseconds);
	}
	
	private TimePoint(long milliseconds) {
		this.millisecondsFromEpoc = milliseconds;
	}
	
	public boolean equals(Object otherPoint) {
		return 
			(otherPoint instanceof TimePoint) &&
			((TimePoint) otherPoint).millisecondsFromEpoc == this.millisecondsFromEpoc;
	}
	
	public int hashCode() {
		return (int) millisecondsFromEpoc;
	}

	public TimePoint backToMidnight() {
		Calendar cal =asJavaCalendar();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return TimePoint.from(cal);
	}
	
	public boolean isSameCalendarDayAs(TimePoint other) {
		Calendar thisDate = this.asJavaCalendar();		
		Calendar otherDate = other.asJavaCalendar();
		return
			thisDate.get(Calendar.YEAR) == otherDate.get(Calendar.YEAR) &&
			thisDate.get(Calendar.MONTH) == otherDate.get(Calendar.MONTH) &&
			thisDate.get(Calendar.DAY_OF_MONTH) == otherDate.get(Calendar.DAY_OF_MONTH);
	}

	public String toString() {
		return asJavaUtilDate().toString(); //for better readability
//		return String.valueOf(millisecondsFromEpoc);
	}

	public String toString(String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(asJavaUtilDate());
		
//		return String.valueOf(millisecondsFromEpoc);
	}
	
	public boolean isBetween(TimePoint fromPoint, TimePoint toPoint) {
		return TimeInterval.from(fromPoint, toPoint).includes(this);
	}

	public boolean isBefore(TimePoint other) {
		return this.millisecondsFromEpoc < other.millisecondsFromEpoc;
	}

	public boolean isAfter(TimePoint other) {
		return this.millisecondsFromEpoc > other.millisecondsFromEpoc;
	}

	public TimePoint nextDay() {
		return this.plus(Duration.days(1));
//		Calendar current = asJavaCalendar();
//		current.add(Calendar.DATE, 1);
//		return from(current);
	}
	
	public Date asJavaUtilDate() {
		return new Date(millisecondsFromEpoc);
	}

	public Calendar asJavaCalendar() {
		Calendar result = Calendar.getInstance();
		result.setTime(asJavaUtilDate());
		return result;
	}

	

	/** a convenience method */
	public boolean isBefore(TimeInterval interval) {
		return interval.isAfter(this);
	}

	/** a convenience method */
	public boolean isAfter(TimeInterval interval) {
		return interval.isBefore(this);
	}
	
	/** a convenience method */
	public TimePoint plus(Duration duration) {
		return duration.addedTo(this);
	}

	/** a convenience method */
	public TimePoint minus(Duration duration) {
		return duration.subtractedFrom(this);
	}
	
}
