/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.*;

import com.domainlanguage.util.*;

public class CalendarDate extends CalendarInterval {
	public static final CalendarDate FAR_FUTURE = from(9999, 9, 9);	

	final int year;
	final int month; // 1 based:  January = 1, February = 2, ...
	final int day;
	
	public static CalendarDate from(int year, int month, int day) {
		return new CalendarDate(year, month, day);
	}
	
	public static CalendarDate from(String dateString, String pattern) {
		TimeZone arbitraryZone = TimeZone.getTimeZone("Universal"); 
		//Any timezone works, as long as the same one is used throughout.
		TimePoint point = TimePoint.parseFrom(dateString, pattern, arbitraryZone);
		return CalendarDate.from(point, arbitraryZone);
	}
	
	public static CalendarDate from(TimePoint timePoint, TimeZone zone) {
		Calendar calendar = timePoint.asJavaCalendar();
		calendar.setTimeZone(zone);
		return CalendarDate._from(calendar);
	}

	static CalendarDate _from(Calendar calendar) {
		//Use timezone already set in calendar.
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1; //T&M Lib counts January as 1
		int date = calendar.get(Calendar.DATE);
		return CalendarDate.from(year, month, date);
	}

	CalendarDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public String toString() {
		return toString("yyyy-M-d"); //default for console
	}

	public String toString(String pattern) {
		TimeZone arbitraryZone = TimeZone.getTimeZone("Universal"); 
		//Any timezone works, as long as the same one is used throughout.
		TimePoint point = startAsTimePoint(arbitraryZone);
		return point.toString(pattern, arbitraryZone);
	}
	
	public boolean isBefore(CalendarDate other) {
		if (year < other.year) return true;
		if (year > other.year) return false;
		if (month < other.month) return true;
		if (month > other.month) return false;
		return day < other.day;
	}

	public boolean isAfter(CalendarDate other) {
		return !isBefore(other) && !this.equals(other);
	}

	public int compareTo(Object other) {
		if (TypeCheck.is(other, CalendarDate.class)) {
			CalendarDate otherDate = (CalendarDate) other;
			if (this.isBefore(otherDate)) return -1;
			if (this.isAfter(otherDate)) return 1;
			return 0;
		}
		return super.compareTo(other);
	}

	public boolean equals(Object object) {
		//revisit: maybe use: Reflection.equalsOverClassAndNull(this, other)
		if (!(object instanceof CalendarDate)) return false;
		CalendarDate other = (CalendarDate) object;
		return 
			this.year == other.year &&
			this.month == other.month &&
			this.day == other.day;
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

	public CalendarDate nextDay() {
		return this.plusDays(1);
	}
	
	public CalendarDate previousDay() {
		return this.plusDays(-1);
	}
	
	public CalendarDate firstOfMonth() {
		return month().start();
	}
	
	public CalendarDate lastOfMonth() {
		return month().end();
	}

	public CalendarInterval month() {
		return CalendarInterval.month(year, month);
	}
	
	public CalendarInterval year() {
		return CalendarInterval.year(year);
	}
	
	public CalendarDate plusDays(int increment) {
		Calendar calendar = asJavaCalendarUniversalZoneMidnight();
		calendar.add(Calendar.DATE, increment);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		return CalendarDate.from(year, month, day);
	}

	public CalendarDate plusMonths(int increment) {
		Calendar calendar = asJavaCalendarUniversalZoneMidnight();
		calendar.add(Calendar.MONTH, increment);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		return CalendarDate.from(year, month, day);
	}
	
	public CalendarDate plus(Duration length) {
		return length.addedTo(this);
	}
	
	Calendar asJavaCalendarUniversalZoneMidnight() {
		TimeZone zone = TimeZone.getTimeZone("Universal");
		Calendar calendar = Calendar.getInstance(zone);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
	
	public TimeInterval asTimeInterval(TimeZone zone) {
		return TimeInterval.startingFrom(startAsTimePoint(zone), true, Duration.days(1), false);
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

	public CalendarInterval through(CalendarDate otherDate) {
		return CalendarInterval.inclusive(this, otherDate);
	}

	public Comparable upperLimit() {
		return this;
	}

	public Comparable lowerLimit() {
		return this;
	}

	public int dayOfWeek() {
		Calendar calendar = asJavaCalendarUniversalZoneMidnight();
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
}
