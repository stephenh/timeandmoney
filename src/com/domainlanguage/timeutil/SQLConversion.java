/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import java.util.TimeZone;

import com.domainlanguage.time.*;

//TODO No tests for this class! (And this class really needed?)
public class SQLConversion {
	public static final String SQL_DATE_PATTERN = "#M/d/yyyy#";
	
	public static String toDateString(CalendarDate date) {
		return date.toString(SQL_DATE_PATTERN);
	}

	public static String toDateString(TimePoint point, TimeZone zone) {
		return point.toString(SQL_DATE_PATTERN, zone);
	}
}
