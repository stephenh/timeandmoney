/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Iterator;

class FloatingDateSpecification extends DateSpecification {
	int month;
	int dayOfWeek;
	int occurrence;
	
	public FloatingDateSpecification(int month, int dayOfWeek,	int occurrence) {
		this.month = month;
		this.dayOfWeek = dayOfWeek;
		this.occurrence = occurrence;
	}

	/* (non-Javadoc)
	 * @see com.domainlanguage.time.HolidayDerivation#ofYear(int)
	 */
	public CalendarDate ofYear(int year) {
		CalendarDate firstOfMonth = CalendarDate.date(year, month, 1);
		int dayOfWeekOffset = dayOfWeek - firstOfMonth.dayOfWeek();
		int dateOfFirstOccurrenceOfDayOfWeek = 0;
		if (dayOfWeekOffset < 0) {
			dateOfFirstOccurrenceOfDayOfWeek = dayOfWeekOffset + 8;
		} else {
			dateOfFirstOccurrenceOfDayOfWeek = dayOfWeekOffset + 1;
		}
		int date = ((occurrence - 1) * 7) + dateOfFirstOccurrenceOfDayOfWeek;
		return CalendarDate.date(year, month, date);
	}

	/* (non-Javadoc)
	 * @see com.domainlanguage.time.HolidayDerivation#isSatisfiedBy(com.domainlanguage.time.CalendarDate)
	 */
	public boolean isSatisfiedBy(CalendarDate date) {
		return ofYear(date.year).equals(date);
	}

	/* (non-Javadoc)
	 * @see com.domainlanguage.time.DateSpecification#firstIn(com.domainlanguage.time.CalendarInterval)
	 */
	public CalendarDate firstIn(CalendarInterval interval) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.domainlanguage.time.DateSpecification#iterateOver(com.domainlanguage.time.CalendarInterval)
	 */
	public Iterator iterateOver(CalendarInterval ylate2002_early2005) {
		// TODO Auto-generated method stub
		return null;
	}

}
