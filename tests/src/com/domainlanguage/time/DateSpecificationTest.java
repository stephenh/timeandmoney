/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Calendar;

import junit.framework.TestCase;

public class DateSpecificationTest extends TestCase {

	public void testFixedDate() {
		DateSpecification independenceDay = DateSpecification.fixed(7, 4);
		assertEquals(CalendarDate.date(2004, 7, 4), independenceDay.ofYear(2004));
		assertTrue(independenceDay.isSatisfiedBy(CalendarDate.date(2004, 7, 4)));
		assertFalse(independenceDay.isSatisfiedBy(CalendarDate.date(2004, 7, 3)));
		assertTrue(independenceDay.isSatisfiedBy(CalendarDate.date(1970, 7, 4)));
	}
	
	public void testNthWeekdayInMonth() {
		DateSpecification thanksgiving = DateSpecification.nthOccuranceOfWeekdayInMonth(11, Calendar.THURSDAY, 4);
		assertEquals(CalendarDate.date(2004, 11, 25), thanksgiving.ofYear(2004));
		assertTrue(thanksgiving.isSatisfiedBy(CalendarDate.date(2004, 11, 25)));
		assertFalse(thanksgiving.isSatisfiedBy(CalendarDate.date(2002, 11, 25)));		
		assertEquals(CalendarDate.date(2002, 11, 28), thanksgiving.ofYear(2002));
		assertTrue(thanksgiving.isSatisfiedBy(CalendarDate.date(2002, 11, 28)));		
	}
}
