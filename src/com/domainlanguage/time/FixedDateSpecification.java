/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Iterator;

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

	/* (non-Javadoc)
	 * @see com.domainlanguage.time.DateSpecification#firstIn(com.domainlanguage.time.CalendarInterval)
	 */
	public CalendarDate firstIn(CalendarInterval interval) {
		CalendarDate firstTry = ofYear(interval.start().year);
		if (interval.includes(firstTry)) return firstTry;
		CalendarDate secondTry = ofYear(interval.start().year + 1);
		if (interval.includes(secondTry)) return secondTry;
		return null;
	}

	/* (non-Javadoc)
	 * @see com.domainlanguage.time.DateSpecification#iterateOver(com.domainlanguage.time.CalendarInterval)
	 */
	public Iterator iterateOver(final CalendarInterval interval) {
		return new Iterator() {
			CalendarDate next = firstIn(interval);
			public boolean hasNext() {
				return next != null;
			}	
			public Object next() {
				if (next == null) return null;
				Object current = next;
				next = next.plusMonths(12);
				if (!interval.includes(next)) next = null;
				return current;
			}
			public void remove() {}
		};
	}

}
