/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Calendar;
import java.util.TimeZone;

public class CalendarDate extends CalendarInterval {
	int year;
	int month; // January = 1, February = 2 ...
	int day;
	
	public static final CalendarDate FAR_FUTURE = from(9999, 9, 9);	

	static CalendarDate from(int year, int month, int day) {
		return new CalendarDate(year, month, day);
	}
	
	CalendarDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public static CalendarDate from(String dateString, String pattern) {
		TimeZone zone = TimeZone.getTimeZone("Universal");
		TimePoint point = TimePoint.parseFrom(dateString, pattern, zone);
		return CalendarDate.from(point, zone);
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

	public String toString() {
		return toString("yyyy-M-d"); //default for console
	}

	public String toString(String pattern) {
		TimeZone zone = TimeZone.getTimeZone("Universal");
		TimePoint point = startAsTimePoint(zone);
		return point.toString(pattern, zone);
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

	public int compareTo(Object other) {
		CalendarDate otherDate = (CalendarDate)other;
		if (this.isBefore(otherDate)) return -1;
		if (this.isAfter(otherDate)) return 1;
		return 0;
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

	public CalendarDate nextDay() {
		return this.plusDays(1);
	}

	public CalendarDate plusDays(int increment) {
		Calendar calendar = _asJavaCalendarUniversalZoneMidnight();
		calendar.add(Calendar.DATE, increment);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		return CalendarDate.from(year, month, day);
	}
	
	Calendar _asJavaCalendarUniversalZoneMidnight() {
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

	public CalendarInterval through(CalendarDate otherDate) {
		return CalendarInterval.inclusive(this, otherDate);
	}

	public Comparable upperLimit() {
		return this;
	}

	public Comparable lowerLimit() {
		return this;
	}
	
}
