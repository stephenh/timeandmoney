package timelanguage;

import java.util.Calendar;
import java.util.TimeZone;

public class CalendarDate extends CalendarInterval implements Comparable {
	int year;
	int month; // January = 1, February = 2 ...
	int day;

	static CalendarDate from(int year, int month, int day) {
		return new CalendarDate(year, month, day);
	}
	
	CalendarDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public boolean isBefore(CalendarDate other) {
		if (year < other.year) return true;
		if (year > other.year) return false;
		if (month < other.month) return true;
		if (month > other.month) return false;
		return day < other.day;
	}

	public boolean isAfter(CalendarDate other) {
		return !(isBefore(other) || equals(other));
	}

	public boolean equals(Object other) {
		if (!(other instanceof CalendarDate)) return false;
		CalendarDate otherDate = ((CalendarDate)other);
		return (year == otherDate.year) &&
			(month == otherDate.month) &&
			(day == otherDate.day);
	}

	public int hashCode() {
		return year * month * day;
	}

	public CalendarDate start() {
		return this;
	}

	public CalendarDate end() {
		return this;
	}

	public TimeInterval asTimeInterval(TimeZone zone) {
		return TimeInterval.from(startAsTimePoint(zone), true, Duration.days(1), false);
	}
	
	public TimePoint startAsTimePoint(TimeZone zone) {
		Calendar calendar = Calendar.getInstance(zone);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return TimePoint.from(calendar);
	}

	public int compareTo(Object other) {
		CalendarDate otherDate = (CalendarDate)other;
		if (this.isBefore(otherDate)) return -1;
		if (this.isAfter(otherDate)) return 1;
		return 0;
	}
	
}
