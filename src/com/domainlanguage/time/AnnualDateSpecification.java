/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Iterator;

public abstract class AnnualDateSpecification extends DateSpecification {
	public abstract CalendarDate ofYear(int year);

	public CalendarDate firstOccurrenceIn(CalendarInterval interval) {
		CalendarDate firstTry = ofYear(interval.start().year);
		if (interval.includes(firstTry)) return firstTry;
		CalendarDate secondTry = ofYear(interval.start().year + 1);
		if (interval.includes(secondTry)) return secondTry;
		return null;
	}

	public Iterator iterateOver(final CalendarInterval interval) {
		return new Iterator() {
			CalendarDate next = firstOccurrenceIn(interval);
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
