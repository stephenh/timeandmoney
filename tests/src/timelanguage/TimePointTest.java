package timelanguage;

import java.util.*;

import junit.framework.*;

public class TimePointTest extends TestCase {
	private static final String AM = "AM";
	private static final String PM = "PM";
	TimeZone gmt = TimeZone.getTimeZone("Universal");
	TimeZone pt = TimeZone.getTimeZone("America/Los_Angeles");
	TimeZone ct = TimeZone.getTimeZone("America/Chicago");

	TimePoint dec19_2003 = TimePoint.atMidnight(2003, 12, 19);
	TimePoint dec20_2003 = TimePoint.atMidnight(2003, 12, 20);
	TimePoint dec21_2003 = TimePoint.atMidnight(2003, 12, 21);
	TimePoint dec22_2003 = TimePoint.atMidnight(2003, 12, 22);

	
	public void testCreationWithDefaultTimeZone() {
		TimePoint expected = TimePoint.from(2004, 1, 1, 0, 0, 0, 0);
		assertEquals("at midnight", expected, TimePoint.atMidnight(2004, 1, 1));
		assertEquals("hours in 24hr clock", expected, TimePoint.from(2004, 1, 1, 0));
		assertEquals("hours in 12hr clock", expected, TimePoint.from(2004, 1, 1, 12, AM));
		assertEquals("date from formatted String", expected, TimePoint.from("2004/1/1", "yyyy/MM/dd"));
		assertEquals("pm hours in 12hr clock", TimePoint.from(2004, 1, 1, 12), TimePoint.from(2004, 1, 1, 12, PM));
	}
	
	public void testCreationWithTimeZone() {
		//TimePoints are based on miliseconds from the Epoc. They do not have a "timezone".
		//When that basic value needs to be converted to or from a date or hours and minutes,
		//then a Timezone must be specified or assumed. The default is always GMT. So creation
		//operations which don't pass any Timezone assume the date, hours and minutes are GMT.
		//The TimeLibrary does not use the default TimeZone operation in Java, the selection of
		//the appropriate Timezone is left to the application.
		
		TimePoint gmt10Hour = TimePoint.from(2004, 3, 5, 10, 10, 0, 0, gmt);
		TimePoint default10Hour = TimePoint.from(2004, 3, 5, 10, 10, 0, 0);
		TimePoint pt2Hour = TimePoint.from(2004, 3, 5, 2, 10, 0, 0, pt);
		
		assertEquals(gmt10Hour, default10Hour);
		assertEquals(gmt10Hour, pt2Hour);
		
		TimePoint gmt6Hour = TimePoint.from(2004, 3, 5, 6, 0, 0, 0, gmt);
		TimePoint ct0Hour = TimePoint.from(2004, 3, 5, 0, 0, 0, 0, ct);
		TimePoint ctMidnight = TimePoint.atMidnight(2004, 3, 5, ct);

		assertEquals(gmt6Hour, ct0Hour);
		assertEquals(gmt6Hour, ctMidnight);
		
	}

	public void testStringFormat() {
		TimePoint point = TimePoint.from(2004, 3, 12, 5, 3, 14, 0, pt);
		//Try stupid date/time format, so that it couldn't work by accident.
		assertEquals("3-04-12 3:5:14", point.toString("M-yy-d m:h:s", pt));
		assertEquals("3-04-12", point.toString("M-yy-d", pt));
	}

	private Date javaUtilDateDec20_2003() {
		Calendar calendar = Calendar.getInstance(gmt);
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
	
	public void testAtMidnight() {
		TimePoint threeOClock = TimePoint.from(2004, 11, 22, 3);
		assertEquals(TimePoint.atMidnight(2004, 11, 22), threeOClock.backToMidnight());
	}

	
	public void testFromString() {
		TimePoint expected = TimePoint.from(2004, 3, 29, 22, 44, 58, 0);
		String source = "2004-Mar-29 10:44:58 PM";
		String pattern = "yyyy-MMM-dd hh:mm:ss a";
		assertEquals(expected, TimePoint.from(source, pattern));
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


	public void testIncrementDuration() {
		Duration twoDays = Duration.days(2);
		assertEquals(dec22_2003, dec20_2003.plus(twoDays));
	}

	public void testDecrementDuration() {
		Duration twoDays = Duration.days(2);
		assertEquals(dec19_2003, dec21_2003.minus(twoDays));
	}

	// This is only an integration test. 
	// The primary responsibility is in TimePeriod
	public void testBeforeAfterPeriod() {
		TimeInterval period = TimeInterval.closed(dec20_2003, dec22_2003);
		assertTrue(dec19_2003.isBefore(period));
		assertFalse(dec19_2003.isAfter(period));
		assertFalse(dec20_2003.isBefore(period));
		assertFalse(dec20_2003.isAfter(period));
		assertFalse(dec21_2003.isBefore(period));
		assertFalse(dec21_2003.isAfter(period));
	}
	
	public void testNextDay() {
		assertEquals(dec20_2003, dec19_2003.nextDay());
	}
	
 	public void testCompare() {
 	    assertTrue(dec19_2003.compareTo(dec20_2003) < 0);
 	    assertTrue(dec20_2003.compareTo(dec19_2003) > 0);
 	    assertTrue(dec20_2003.compareTo(dec20_2003) == 0);
 	}


}
