/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

class FixedDateSpecification extends DateSpecification {
	int month;
	int day;
	
	public FixedDateSpecification(int month, int day) {
		this.month = month;
		this.day = day;
	}

	/* (non-Javadoc)
	 * @see com.domainlanguage.time.HolidayDerivation#ofYear(int)
	 */
	public CalendarDate ofYear(int year) {
		return CalendarDate.date(year, month, day);
	}

	/* (non-Javadoc)
	 * @see com.domainlanguage.time.HolidayDerivation#isSatisfiedBy(com.domainlanguage.time.CalendarDate)
	 */
	public boolean isSatisfiedBy(CalendarDate date) {
		return day == date.day && month == date.month;
	}

}
