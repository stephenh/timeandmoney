/**
Copyright (c) 2004 Domain Language, Inc. (www.domainlanguage.com)

Permission is hereby granted, free of charge, to any person obtaining a 
copy of this software and associated documentation files (the "Software"), 
to deal in the Software without restriction, including without limitation 
the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom the 
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included 
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS 
OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
IN THE SOFTWARE.
*/

package com.domainlanguage.time;

import java.util.Iterator;

import junit.framework.TestCase;

public class BusinessCalendarTest extends TestCase {
	private BusinessCalendar businessCalendar() {
		BusinessCalendar cal = BusinessCalendar.defaultBusinessCalendar();
		cal.addHolidays(_HolidayDates.defaultHolidays());
		return cal;
	}
	
	public void testElapsedBusinessDays() {
		CalendarDate nov1 = CalendarDate.from(2004, 11, 1);
		CalendarDate nov30 = CalendarDate.from(2004, 11, 30);
		CalendarInterval interval = CalendarInterval.inclusive(nov1, nov30);
		assertEquals(30, interval.lengthInDays());
		// 1 holiday (Thanksgiving on a Thursday) + 8 weekend days.
		assertEquals(21, businessCalendar().getElapsedBusinessDays(interval));
	}
	
	public void testIsWeekend() {
		CalendarDate saturday = CalendarDate.from(2004, 1, 10);
		assertTrue(businessCalendar().isWeekend(saturday));

		CalendarDate sunday = saturday.nextDay();
		assertTrue(businessCalendar().isWeekend(sunday));

		CalendarDate day = sunday;
		for (int i = 0; i < 5; i++) {
			day = day.nextDay();
			assertFalse("it's a midweek day", businessCalendar().isWeekend(day));
		}
		day = day.nextDay();
		assertTrue("finally, the weekend is here...", businessCalendar().isWeekend(day));

		CalendarDate newYearEve = CalendarDate.from(2004, 1, 1); // it's a Thursday
		assertFalse("a holiday is not necessarily a weekend day", businessCalendar().isWeekend(newYearEve));
	}

	public void testIsHoliday() {
		CalendarDate newYearEve = CalendarDate.from(2004, 1, 1); // it's a Thursday
		assertTrue("New Years Eve is a holiday.", businessCalendar().isHoliday(newYearEve));

		assertFalse("The day after New Years Eve is not a holiday.", businessCalendar().isHoliday(newYearEve.nextDay()));
	}

	public void testIsBusinessDay() {
		CalendarDate day = CalendarDate.from(2004, 1, 12); // it's a Monday
		for (int i = 0; i < 5; i++) {
			assertTrue("another working day", businessCalendar().isBusinessDay(day));
			day = day.nextDay();
		}
		assertFalse("finally, saturday arrived ...", businessCalendar().isBusinessDay(day));
		assertFalse("... then sunday", businessCalendar().isBusinessDay(day.nextDay()));

		CalendarDate newYearEve = CalendarDate.from(2004, 1, 1); // it's a Thursday
		assertFalse("hey, it's a holiday", businessCalendar().isBusinessDay(newYearEve));
	}

	public void testNearestBusinessDay() {
		CalendarDate saturday = CalendarDate.from(2004, 1, 10);
		CalendarDate sunday = saturday.nextDay();
		CalendarDate monday = sunday.nextDay();
		assertEquals(monday, businessCalendar().nearestBusinessDay(saturday));
		assertEquals(monday, businessCalendar().nearestBusinessDay(sunday));
		assertEquals(monday, businessCalendar().nearestBusinessDay(monday));

		CalendarDate newYearEve = CalendarDate.from(2004, 1, 1); // it's a Thursday
		assertEquals("it's a holiday & a thursday; wait till friday", newYearEve.nextDay(), businessCalendar().nearestBusinessDay(newYearEve));

		CalendarDate christmas = CalendarDate.from(2004, 12, 24); // it's a Friday
		assertEquals("it's a holiday & a friday; wait till monday", CalendarDate.from(2004, 12, 27), businessCalendar().nearestBusinessDay(christmas));
	}
	
	public void testBusinessDaysIterator() {
		CalendarDate start = CalendarDate.from(2004, 2, 5);
		CalendarDate end = CalendarDate.from(2004, 2, 8);
		CalendarInterval interval = CalendarInterval.inclusive(start, end);
		Iterator it = businessCalendar().businessDaysIterator(interval);
		assertTrue(it.hasNext());
		assertEquals(start, it.next());
		assertTrue(it.hasNext());
		assertEquals(CalendarDate.from(2004, 2, 6), it.next());
		assertFalse(it.hasNext());
	}
	
}
