package com.domainlanguage.timeutil;

import java.util.TimeZone;

import com.domainlanguage.time.*;


public class SQLConversion {
	public static final String SQL_DATE_PATTERN = "#M/d/yyyy#";
	
	public static String toDateString(CalendarDate date) {
		return date.toString(SQL_DATE_PATTERN);
	}

	public static String toDateString(TimePoint point, TimeZone zone) {
		return point.toString(SQL_DATE_PATTERN, zone);
	}
}
