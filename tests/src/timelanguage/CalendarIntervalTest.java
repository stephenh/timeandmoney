package timelanguage;

import java.util.TimeZone;

import junit.framework.TestCase;

public class CalendarIntervalTest extends TestCase {

	CalendarDate may1 = CalendarInterval.date(2004, 5, 1);
	CalendarDate may20 = CalendarInterval.date(2004, 5, 20);
	CalendarDate apr15 = CalendarInterval.date(2004, 4, 15);
	CalendarDate jun1 = CalendarInterval.date(2004, 6, 1);
	CalendarInterval may = CalendarInterval.inclusive(2004, 5, 1, 2004, 5, 31);
	TimeZone gmt = TimeZone.getTimeZone("Universal");
	TimeZone pt = TimeZone.getTimeZone("America/Los_Angeles");
	TimeZone ct = TimeZone.getTimeZone("America/Chicago");

	public void testTranslationToTimeInterval() {
		TimeInterval day = may20.asTimeInterval(ct);
		assertEquals("May20Ct", TimePoint.atMidnight(2004, 5, 20, ct), day.start());
//		assertEquals(TimePoint.atMidnight(2004, 5, 21, ct), day.end());
		
//		TimeInterval interval = CalendarInterval.inclusive(apr15, may20).asTimeInterval(pt);
//		assertEquals(TimePoint.atMidnight(2004, 4, 15, pst), interval.start());
//		assertEquals(TimePoint.atMidnight(2004, 5, 21, pt), interval.end());
		
	}

	public void testIncludes() {
		assertFalse("apr15", may.includes(apr15));
		assertTrue("may1", may.includes(may1));
		assertTrue("may20", may.includes(may20));
		assertFalse("jun1", may.includes(jun1));
		assertTrue("may", may.includes(may));
	}
}
