package timelanguage;

import java.util.*;
import junit.framework.*;

public class TimeIntervalTest extends TestCase {
	private TimePoint dec19_2003 = TimePoint.atMidnight(2003, 12, 19);
	private TimePoint dec20_2003 = TimePoint.atMidnight(2003, 12, 20);
	private TimePoint dec21_2003 = TimePoint.atMidnight(2003, 12, 21);
	private TimePoint dec22_2003 = TimePoint.atMidnight(2003, 12, 22);
	private TimePoint dec23_2003 = TimePoint.atMidnight(2003, 12, 23);

	public void testDayOf() {
		TimePoint tempus = TimePoint.from(1984, 11, 22, 1);
		TimeInterval interval = TimeInterval.dayOf(tempus);
		assertEquals(interval.start, TimePoint.atMidnight(1984, 11, 22));
		assertEquals(interval.end, TimePoint.atMidnight(1984, 11, 23));
	}

	public void testBeforeClosed() {
			TimeInterval interval = TimeInterval.closed(dec20_2003, dec22_2003);
		// Only the upper end should matter for this test.
		assertFalse(interval.isBefore(dec21_2003));
		assertFalse(interval.isBefore(dec22_2003));
		assertTrue(interval.isBefore(dec23_2003));
	}

	public void testAfterClosed() {
		TimeInterval interval = TimeInterval.closed(dec20_2003, dec22_2003);
		// Only the lower end should matter for this test.
		assertTrue(interval.isAfter(dec19_2003));
		assertFalse(interval.isAfter(dec20_2003));
		assertFalse(interval.isAfter(dec21_2003));
	}

	public void testIncludesClosed() {
		TimeInterval interval = TimeInterval.closed(dec20_2003, dec22_2003);
		assertFalse(interval.includes(dec19_2003));
		assertTrue(interval.includes(dec20_2003));
		assertTrue(interval.includes(dec21_2003));
		assertTrue(interval.includes(dec22_2003));
		assertFalse(interval.includes(dec23_2003));
	}

	public void testBeforeOpen() {
		TimeInterval interval = TimeInterval.open(dec20_2003, dec22_2003);
		// Only the upper end should matter for this test.
		assertFalse(interval.isBefore(dec21_2003));
		assertTrue(interval.isBefore(dec22_2003));
		assertTrue(interval.isBefore(dec23_2003));
	}

	public void testAfterOpen() {
		TimeInterval interval = TimeInterval.open(dec20_2003, dec22_2003);
		// Only the lower end should matter for this test.
		assertTrue(interval.isAfter(dec19_2003));
		assertTrue(interval.isAfter(dec20_2003));
		assertFalse(interval.isAfter(dec21_2003));
	}

	public void testIncludesOpen() {
		TimeInterval interval = TimeInterval.open(dec20_2003, dec22_2003);
		assertFalse(interval.includes(dec19_2003));
		assertFalse(interval.includes(dec20_2003));
		assertTrue(interval.includes(dec21_2003));
		assertFalse(interval.includes(dec22_2003));
		assertFalse(interval.includes(dec23_2003));
	}

	public void testIncludesHalfOpen() {
		TimeInterval interval = TimeInterval.from(dec20_2003, true, dec22_2003, false);
		assertFalse(interval.includes(dec19_2003));
		assertTrue(interval.includes(dec20_2003));
		assertTrue(interval.includes(dec21_2003));
		assertFalse(interval.includes(dec22_2003));
		assertFalse(interval.includes(dec23_2003));
	}

	public void testLength() {
		TimeInterval interval = TimeInterval.open(dec20_2003, dec22_2003);
		Duration expectedDuration = Duration.days(2);
		assertEquals(expectedDuration, interval.length());
		assertEquals("2 days", interval.length().toDetailedString());
		
		
		TimePoint first = TimePoint.from(2004, 1, 1, 1, 1, 1, 1);
		TimePoint second = TimePoint.from(2004, 1, 6, 5, 4, 3, 2);
		interval = TimeInterval.closed(first, second);
		assertEquals("5 days, 4 hours, 3 minutes, 2 seconds, 1 millisecond", interval.length().toDetailedString());
	}

	public void testCreateFromDuration() {
		Duration twoDays = Duration.days(2);
		TimeInterval interval = TimeInterval.from(dec20_2003, true, twoDays, true);
		assertEquals(dec22_2003, interval.end);
	}

	public void testDaysIterator() {
		TimePoint start = TimePoint.from(2004, 2, 5, 10);
		TimePoint end = TimePoint.from(2004, 2, 8, 2);
		TimeInterval interval = TimeInterval.from(start, end);
		Iterator it = interval.daysIterator();
		assertTrue(it.hasNext());
		assertEquals(start, it.next());
		assertTrue(it.hasNext());
		assertEquals(TimePoint.from(2004, 2, 6, 10), it.next());
		assertTrue(it.hasNext());
		assertEquals(TimePoint.from(2004, 2, 7, 10), it.next());
		assertFalse(it.hasNext());
	}
	
}
