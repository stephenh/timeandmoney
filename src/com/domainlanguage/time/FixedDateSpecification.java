/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;


class FixedDateSpecification extends AnnualDateSpecification {
	int month;
	int day;
	
	public FixedDateSpecification(int month, int day) {
		this.month = month;
		this.day = day;
	}

	public CalendarDate ofYear(int year) {
		return CalendarDate.date(year, month, day);
	}

	public boolean isSatisfiedBy(CalendarDate date) {
		return day == date.day && month == date.month;
	}

}
