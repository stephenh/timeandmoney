/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;


class FloatingDateSpecification extends AnnualDateSpecification {
	int month;
	int dayOfWeek;
	int occurrence;
	
	public FloatingDateSpecification(int month, int dayOfWeek,	int occurrence) {
		this.month = month;
		this.dayOfWeek = dayOfWeek;
		this.occurrence = occurrence;
	}

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

	public boolean isSatisfiedBy(CalendarDate date) {
		return ofYear(date.year).equals(date);
	}

}
