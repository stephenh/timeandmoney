package timelanguage;

import java.util.TimeZone;

import junit.framework.TestCase;

public class CalendarDateTest extends TestCase {
	
	CalendarDate feb17 = CalendarDate.from(2003, 2, 17);
	CalendarDate mar13 = CalendarDate.from(2003, 3, 13);
	TimeZone gmt = TimeZone.getTimeZone("Universal");
	TimeZone ct = TimeZone.getTimeZone("America/Chicago");

	public void testComparison() {
		assertTrue(feb17.isBefore(mar13));
		assertFalse(mar13.isBefore(feb17));
		assertFalse(feb17.isBefore(feb17));
		assertFalse(feb17.isAfter(mar13));
		assertTrue(mar13.isAfter(feb17));
		assertFalse(feb17.isAfter(feb17));
	}
	
	public void testStartAsTimePoint() {
		TimePoint feb17StartAsCt = feb17.startAsTimePoint(ct);
		TimePoint feb17Hour0Ct = TimePoint.atMidnight(2003, 2, 17, ct);
		assertEquals(feb17Hour0Ct, feb17StartAsCt);
	}

	public void testAsTimeInterval() {
		TimeInterval feb17AsCt = feb17.asTimeInterval(ct);
		TimePoint feb17Hour0Ct = TimePoint.atMidnight(2003, 2, 17, ct);
		TimePoint feb18Hour0Ct = TimePoint.atMidnight(2003, 2, 18, ct);
		assertEquals("start", feb17Hour0Ct, feb17AsCt.start());
		assertEquals("end", feb18Hour0Ct, feb17AsCt.end());
	}
	
}
