package timelanguage;

import java.util.Iterator;

import junit.framework.TestCase;

/**
 * @author Eric Evans
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class BusinessCalendarTest extends TestCase {
	private BusinessCalendar businessCalendar() {
		BusinessCalendar cal = BusinessCalendar.defaultBusinessCalendar();
		cal.addHolidays(HolidayDates.defaultHolidays());
		return cal;
	}
	
	public void testBusinessWeek() throws Exception {
		TimePoint march25 = TimePoint.atMidnight(2004, 3, 25);
		TimePoint march27 = TimePoint.atMidnight(2004, 3, 27);
		TimeInterval interval = TimeInterval.over(march25, march27);
		int days = businessCalendar().getElapsedBusinessDays(interval);
		assertEquals(2, days);
	}
	
	public void testIsWeekend() {
		TimePoint saturday = TimePoint.atMidnight(2004, 1, 10);
		assertTrue(businessCalendar().isWeekend(saturday));

		TimePoint sunday = TimePoint.atMidnight(2004, 1, 11);
		assertTrue(businessCalendar().isWeekend(sunday));

		TimePoint day = sunday.nextDay();
		for (int i = 0; i < 5; i++) {
			assertFalse("it's a midweek day", businessCalendar().isWeekend(day));
			day = day.nextDay();
		}
		assertTrue("finally, the weekend is here...", businessCalendar().isWeekend(day));

		TimePoint newYearEve = TimePoint.atMidnight(2004, 1, 1); // it's a Thursday
		assertFalse("a holiday is not necessarily a weekend day", businessCalendar().isWeekend(newYearEve));
	}

	public void testIsHoliday() {
		TimePoint newYearEve = TimePoint.atMidnight(2004, 1, 1); // it's a Thursday
		assertTrue("hey, it's a holiday", businessCalendar().isHoliday(newYearEve));

		TimePoint theDayAfter = newYearEve.nextDay();
		assertFalse("a day later", businessCalendar().isHoliday(theDayAfter));
	}

	public void testIsBusinessDay() {
		TimePoint day = TimePoint.atMidnight(2004, 1, 12); // it's a Monday
		for (int i = 0; i < 5; i++) {
			assertTrue("another working day", businessCalendar().isBusinessDay(day));
			day = day.nextDay();
		}
		assertFalse("finally, saturday arrived ...", businessCalendar().isBusinessDay(day));
		assertFalse("... then sunday", businessCalendar().isBusinessDay(day.nextDay()));

		TimePoint newYearEve = TimePoint.atMidnight(2004, 1, 1); // it's a Thursday
		assertFalse("hey, it's a holiday", businessCalendar().isBusinessDay(newYearEve));
	}

	public void testNearestBusinessDay() {
		TimePoint saturday = TimePoint.atMidnight(2004, 1, 10);
		TimePoint sunday = saturday.nextDay();
		TimePoint monday = sunday.nextDay();
		assertEquals(monday, businessCalendar().nearestBusinessDay(saturday));
		assertEquals(monday, businessCalendar().nearestBusinessDay(sunday));
		assertEquals(monday, businessCalendar().nearestBusinessDay(monday));

		TimePoint newYearEve = TimePoint.atMidnight(2004, 1, 1); // it's a Thursday
		assertEquals("it's a holiday & a thursday; wait till friday", newYearEve.nextDay(), businessCalendar().nearestBusinessDay(newYearEve));

		TimePoint christmas = TimePoint.atMidnight(2004, 12, 24); // it's a Friday
		assertEquals("it's a holiday & a friday; wait till monday", TimePoint.atMidnight(2004, 12, 27), businessCalendar().nearestBusinessDay(christmas));
	}
	
	public void testBusinessDaysIterator() {
		TimePoint start = TimePoint.from(2004, 2, 5, 10);
		TimePoint end = TimePoint.from(2004, 2, 8, 2);
		TimeInterval interval = TimeInterval.over(start, end);
		Iterator it = businessCalendar().businessDaysIterator(interval);
		assertTrue(it.hasNext());
		assertEquals(start, it.next());
		assertTrue(it.hasNext());
		assertEquals(TimePoint.from(2004, 2, 6, 10), it.next());
		assertFalse(it.hasNext());
	}
	
}
