/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

public abstract class DateSpecification {

	public static DateSpecification fixed(int month, int day) {
		return new FixedDateSpecification(month, day);
	}
	
	public abstract CalendarDate ofYear(int year);
	
	public abstract boolean isSatisfiedBy(CalendarDate date);

	public static DateSpecification nthOccuranceOfWeekdayInMonth(int month, int dayOfWeek, int n) {
		return new FloatingDateSpecification(month, dayOfWeek, n);
	}

}
