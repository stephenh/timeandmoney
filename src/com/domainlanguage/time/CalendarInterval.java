/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Iterator;
import java.util.TimeZone;

import com.domainlanguage.basic.ComparableInterval;


public abstract class CalendarInterval extends ComparableInterval {

	public static CalendarDate date(int year, int month, int day) {
		return CalendarDate.from(year, month, day);
	}
	
	public static CalendarInterval inclusive(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
		CalendarDate startDate = CalendarDate.from(startYear, startMonth, startDay);
		CalendarDate endDate = CalendarDate.from(endYear, endMonth, endDay);
		return ConcreteCalendarInterval.from(startDate, endDate);
	}

	public static CalendarInterval inclusive(CalendarDate start, CalendarDate end) {
		return ConcreteCalendarInterval.from(start, end);
	}

	
	public abstract TimeInterval asTimeInterval(TimeZone zone);
	
	public boolean includesLowerLimit() {
		return true;
	}

	public boolean includesUpperLimit() {
		return true;
	}
	
	public CalendarDate start() {
		return (CalendarDate)lowerLimit();
	}

	public CalendarDate end() {
		return (CalendarDate)upperLimit();
	}

	public boolean equals(Object arg) {
		if (!(arg instanceof CalendarInterval)) return false;
		CalendarInterval other = (CalendarInterval) arg;
		return upperLimit().equals(other.upperLimit()) && lowerLimit().equals(other.lowerLimit());
	}
	
	public int hashCode() {
		return upperLimit().hashCode();
	}

	public static final CalendarInterval NEVER = CalendarDate.FAR_FUTURE;

	public CalendarInterval intersect(CalendarInterval other) {
		if (!intersects(other)) return NEVER;
	
		CalendarDate intersectLowerBound = (CalendarDate) greaterOfLowerLimits(other);
		CalendarDate intersectUpperBound = (CalendarDate) lesserOfUpperLimits(other);
	
		return inclusive(intersectLowerBound, intersectUpperBound);
	}

	public int lengthInDays() {
		Iterator iter = daysIterator();
		int count = 0;
		while(iter.hasNext()) {
			count++;
			iter.next();
		}
		return count;
	}
	
	public Iterator daysIterator() {
		final CalendarDate start = (CalendarDate)lowerLimit();
		final CalendarDate end = (CalendarDate)upperLimit();
		return new Iterator() {
			CalendarDate next = start;
			public boolean hasNext() {
				return !next.isAfter(end);
			}	
			public Object next() {
				Object current = next;
				next = next.plusDays(1);
				return current;
			}
			public void remove() {}
		};
	}
	
}
