package timelanguage;

import java.util.*;

import junit.framework.*;

public class TimePointTest extends TestCase {
	private static final String AM = "AM";
	private static final String PM = "PM";

	public void testCreation() {
		TimePoint expected = TimePoint.from(2004, 1, 1, 0, 0, 0, 0);
		assertEquals(expected, TimePoint.atMidnight(2004, 1, 1));
		assertEquals(expected, TimePoint.from(2004, 1, 1, 0));
		assertEquals(expected, TimePoint.from(2004, 1, 1, 12, AM));
		assertEquals(expected, TimePoint.from(Calendars.parseDate("2004/1/1", "yyyy/MM/dd")));
		assertEquals(TimePoint.from(2004, 1, 1, 12), TimePoint.from(2004, 1, 1, 12, PM));
	}

	private Date javaUtilDateDec20_2003() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear(); // non-deterministic without this!!!
		calendar.set(Calendar.YEAR, 2003);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.DATE, 20);
		return calendar.getTime();
	}

	public void testAsJavaUtilDate() {
		TimePoint dec20_2003 = TimePoint.atMidnight(2003, 12, 20);
		assertEquals(javaUtilDateDec20_2003(), dec20_2003.asJavaUtilDate());
	}

	public void testNow() {
		TimePoint now = TimePoint.now();
		Date approxNow = new Date();
		assertEquals("if the clock does not ticks during test ...", approxNow, now.asJavaUtilDate());
	}

	public void testEquals() {
		TimePoint createdFromJavaDate = TimePoint.from(javaUtilDateDec20_2003());
		TimePoint dec5_2003 = TimePoint.atMidnight(2003, 12, 5);
		TimePoint dec20_2003 = TimePoint.atMidnight(2003, 12, 20);
		assertEquals(createdFromJavaDate, dec20_2003);
		assertTrue(createdFromJavaDate.equals(dec20_2003));
		assertFalse(createdFromJavaDate.equals(dec5_2003));
	}

	public void testEqualsOverYearMonthDay() {
		TimePoint thePoint = TimePoint.from(2000, 1, 1, 8, AM);
		assertTrue("exactly the same", TimePoint.from(2000, 1, 1, 8, AM).isSameCalendarDayAs(thePoint));
		assertTrue("same second", TimePoint.from(2000, 1, 1, 8, 0, 0, 500).isSameCalendarDayAs(thePoint));
		assertTrue("same minute", TimePoint.from(2000, 1, 1, 8, 0, 30, 0).isSameCalendarDayAs(thePoint));
		assertTrue("same hour", TimePoint.from(2000, 1, 1, 8, 30, 0, 0).isSameCalendarDayAs(thePoint));
		assertTrue("same day", TimePoint.from(2000, 1, 1, 8, PM).isSameCalendarDayAs(thePoint));
		assertTrue("midnight (in the moring), start of same day", TimePoint.atMidnight(2000, 1, 1).isSameCalendarDayAs(thePoint));

		assertFalse("midnight (night), start of next day", TimePoint.atMidnight(2000, 1, 2).isSameCalendarDayAs(thePoint));
		assertFalse("next day", TimePoint.from(2000, 1, 2, 8, AM).isSameCalendarDayAs(thePoint));
		assertFalse("next month", TimePoint.from(2000, 2, 1, 8, AM).isSameCalendarDayAs(thePoint));
		assertFalse("next year", TimePoint.from(2001, 1, 1, 8, AM).isSameCalendarDayAs(thePoint));
	}

	public void testBeforeAfter() {
		TimePoint dec5_2003 = TimePoint.atMidnight(2003, 12, 5);
		TimePoint dec20_2003 = TimePoint.atMidnight(2003, 12, 20);
		assertTrue(dec5_2003.isBefore(dec20_2003));
		assertFalse(dec20_2003.isBefore(dec20_2003));
		assertFalse(dec20_2003.isBefore(dec5_2003));
		assertFalse(dec5_2003.isAfter(dec20_2003));
		assertFalse(dec20_2003.isAfter(dec20_2003));
		assertTrue(dec20_2003.isAfter(dec5_2003));

		TimePoint oneSecondLater = TimePoint.from(2003, 12, 20, 0, 0, 1, 0);
		assertTrue(dec20_2003.isBefore(oneSecondLater));
		assertFalse(dec20_2003.isAfter(oneSecondLater));
	}

	public void testIsWeekend() {
		TimePoint saturday = TimePoint.atMidnight(2004, 1, 10);
		assertTrue(saturday.isWeekend());

		TimePoint sunday = TimePoint.atMidnight(2004, 1, 11);
		assertTrue(sunday.isWeekend());

		TimePoint day = sunday.nextDay();
		for (int i = 0; i < 5; i++) {
			assertFalse("it's a midweek day", day.isWeekend());
			day = day.nextDay();
		}
		assertTrue("finally, the weekend is here...", day.isWeekend());

		TimePoint newYearEve = TimePoint.atMidnight(2004, 1, 1); // it's a Thursday
		assertFalse("a holiday is not a weekend day", newYearEve.isWeekend());
	}

	public void testIsHoliday() {
		TimePoint newYearEve = TimePoint.atMidnight(2004, 1, 1); // it's a Thursday
		assertTrue("hey, it's a holiday", newYearEve.isHoliday());

		TimePoint theDayAfter = newYearEve.nextDay();
		assertFalse("a day later", theDayAfter.isHoliday());
	}

	public void testIsBusinessDay() {
		TimePoint day = TimePoint.atMidnight(2004, 1, 12); // it's a Monday
		for (int i = 0; i < 5; i++) {
			assertTrue("another working day", day.isBusinessDay());
			day = day.nextDay();
		}
		assertFalse("finally, saturday arrived ...", day.isBusinessDay());
		assertFalse("... then sunday", day.nextDay().isBusinessDay());

		TimePoint newYearEve = TimePoint.atMidnight(2004, 1, 1); // it's a Thursday
		assertFalse("hey, it's a holiday", newYearEve.isBusinessDay());
	}

	public void testNearestBusinessDay() {
		TimePoint saturday = TimePoint.atMidnight(2004, 1, 10);
		TimePoint sunday = saturday.nextDay();
		TimePoint monday = sunday.nextDay();
		assertEquals(monday, saturday.nearestBusinessDay());
		assertEquals(monday, sunday.nearestBusinessDay());
		assertEquals(monday, monday.nearestBusinessDay());

		TimePoint newYearEve = TimePoint.atMidnight(2004, 1, 1); // it's a Thursday
		assertEquals("it's a holiday & a thursady; wait till friday", newYearEve.nextDay(), newYearEve.nearestBusinessDay());

		TimePoint christmas = TimePoint.atMidnight(2004, 12, 24); // it's a Friday
		assertEquals("it's a holiday & a friday; wait till monday", TimePoint.atMidnight(2004, 12, 27), christmas.nearestBusinessDay());
	}

	public void testIncrementDuration() {
		TimePoint dec20_2003 = TimePoint.atMidnight(2003, 12, 20);
		TimePoint dec22_2003 = TimePoint.atMidnight(2003, 12, 22);
		Duration twoDays = Duration.days(2);
		assertEquals(dec22_2003, dec20_2003.plus(twoDays));
	}

	public void testDecrementDuration() {
		TimePoint dec20_2003 = TimePoint.atMidnight(2003, 12, 20);
		TimePoint dec18_2003 = TimePoint.atMidnight(2003, 12, 18);
		Duration twoDays = Duration.days(2);
		assertEquals(dec18_2003, dec20_2003.minus(twoDays));
	}

	// This is only an integration test. 
	// The primary responsibility is in TimePeriod
	public void testBeforeAfterPeriod() {
		TimePoint dec19_2003 = TimePoint.atMidnight(2003, 12, 19);
		TimePoint dec20_2003 = TimePoint.atMidnight(2003, 12, 20);
		TimePoint dec21_2003 = TimePoint.atMidnight(2003, 12, 21);
		TimePoint dec22_2003 = TimePoint.atMidnight(2003, 12, 22);
		TimeInterval period = TimeInterval.closed(dec20_2003, dec22_2003);
		assertTrue(dec19_2003.isBefore(period));
		assertFalse(dec19_2003.isAfter(period));
		assertFalse(dec20_2003.isBefore(period));
		assertFalse(dec20_2003.isAfter(period));
		assertFalse(dec21_2003.isBefore(period));
		assertFalse(dec21_2003.isAfter(period));
	}

}
