/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package example.holidayCalendar;

import java.util.*;

import junit.framework.*;

import com.domainlanguage.time.*;

public class HolidayCalendarExample extends TestCase {

	public void testDeriveBirthday() {
		//Calculate Martin Luther King, Jr.'s birthday, January 15, for the
		// year 2005:
		DateSpecification mlkBirthday = DateSpecification.fixed(1, 15);
		// Then you can do checks like
		CalendarDate jan15_2005 = CalendarDate.from(2005, 1, 15);
		assertTrue(mlkBirthday.isSatisfiedBy(jan15_2005));
		//Derive the date(s) for an interval
		CalendarDate mlk2005 = mlkBirthday.firstOccurrenceIn(CalendarInterval.year(2005));
		assertEquals(jan15_2005, mlk2005);
		// Calculate all the birthdays in his lifetime
		CalendarInterval mlkLifetime = CalendarInterval.inclusive(1929, 1, 15, 1968, 4, 4);
		Iterator mlkBirthdays = mlkBirthday.iterateOver(mlkLifetime);
		assertEquals(CalendarDate.from(1929, 1, 15), mlkBirthdays.next());
		assertEquals(CalendarDate.from(1930, 1, 15), mlkBirthdays.next());
		// etc.
		// By the way, to calculate how long MLK lived,
		assertEquals("14325 days", mlkLifetime.length().toString());
	}

}