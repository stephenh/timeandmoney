/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.*;

import com.domainlanguage.util.*;

public abstract class AnnualDateSpecification extends DateSpecification {
	
	public abstract CalendarDate ofYear(int year);

	public CalendarDate firstOccurrenceIn(CalendarInterval interval) {
		CalendarDate firstTry = ofYear(interval.start().year);
		if (interval.covers(firstTry)) return firstTry;
		CalendarDate secondTry = ofYear(interval.start().year + 1);
		if (interval.covers(secondTry)) return secondTry;
		return null;
	}

	public Iterator iterateOver(final CalendarInterval interval) {
		final AnnualDateSpecification spec = this;
		return new ImmutableIterator() {
			CalendarDate next = firstOccurrenceIn(interval);
			int year = next.year;
			public boolean hasNext() {
				return next != null;
			}	
			public Object next() {				
				if (next == null) return null;
				Object current = next;
				year += 1;
				next = spec.ofYear(year);
				if (!interval.covers(next)) next = null;
				return current;
			}
		};
	}
}
