/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import com.domainlanguage.testutil.SerializationTest;

import junit.framework.*;

public class DurationTest extends TestCase {

//	TODO Why does this test hang?
//    public void testSerialization() {
//    	SerializationTest.assertSerializationWorks(Duration.days(1));
//    }
	
	public void testAddMillisecondsToPoint() {
		TimePoint dec20At1 = TimePoint.atGMT(2003, 12, 20, 01, 0, 0, 0);
		TimePoint dec22At1 = TimePoint.atGMT(2003, 12, 22, 01, 0, 0, 0);
		Duration twoDays = Duration.days(2);
		assertEquals(dec22At1, twoDays.addedTo(dec20At1));
	}

	public void testAddMonthsToPoint() {
		TimePoint oct20At1 = TimePoint.atGMT(2003, 10, 20, 01, 0, 0, 0);
		TimePoint dec20At1 = TimePoint.atGMT(2003, 12, 20, 01, 0, 0, 0);
		Duration twoMonths = Duration.months(2);
		assertEquals(dec20At1, twoMonths.addedTo(oct20At1));
	}
	public void testSubtractMillisecondsFromPoint() {
		TimePoint dec20At1 = TimePoint.atGMT(2003, 12, 20, 01, 0, 0, 0);
		TimePoint dec18At1 = TimePoint.atGMT(2003, 12, 18, 01, 0, 0, 0);
		Duration twoDays = Duration.days(2);
		assertEquals(dec18At1, twoDays.subtractedFrom(dec20At1));
	}

	public void testSubtractMonthsFromPoint() {
		TimePoint oct20At1 = TimePoint.atGMT(2003, 10, 20, 01, 0, 0, 0);
		TimePoint dec20At1 = TimePoint.atGMT(2003, 12, 20, 01, 0, 0, 0);
		Duration twoMonths = Duration.months(2);
		assertEquals(oct20At1, twoMonths.subtractedFrom(dec20At1));

		TimePoint dec20At1_2001 = TimePoint.atGMT(2001, 12, 20, 01, 0, 0, 0);
		Duration twoYears = Duration.years(2);
		assertEquals(dec20At1_2001, twoYears.subtractedFrom(dec20At1));
	}

	public void testSubtractFromCalendarDate() {
		CalendarDate oct20 = CalendarDate.from(2003, 10, 20);
		CalendarDate dec20 = CalendarDate.from(2003, 12, 20);

		Duration twoMonths = Duration.months(2);
		assertEquals(oct20, twoMonths.subtractedFrom(dec20));

		Duration sixtyoneDays = Duration.days(61);
		assertEquals(oct20, sixtyoneDays.subtractedFrom(dec20));
		
		CalendarDate dec20_2001 = CalendarDate.from(2001, 12, 20);
		Duration twoYears = Duration.years(2);
		assertEquals(dec20_2001, twoYears.subtractedFrom(dec20));
	}

	public void testAddToCalendarDate() {
		CalendarDate oct20_2003 = CalendarDate.from(2003, 10, 20);
		CalendarDate dec20_2003 = CalendarDate.from(2003, 12, 20);

		Duration twoMonths = Duration.months(2);
		assertEquals(dec20_2003, twoMonths.addedTo(oct20_2003));

		Duration sixtyoneDays = Duration.days(61);
		assertEquals(dec20_2003, sixtyoneDays.addedTo(oct20_2003));
		
		CalendarDate dec20_2001 = CalendarDate.from(2001, 12, 20);
		Duration twoYears = Duration.years(2);
		assertEquals(dec20_2003, twoYears.addedTo(dec20_2001));
	}

	public void testConversionToBaseUnits() {
		Duration twoSeconds = Duration.seconds(2);
		assertEquals(2000, twoSeconds.inBaseUnits());
	}
	
	public void testEquals() {
		assertEquals(Duration.days(2), Duration.hours(48));
		assertEquals(Duration.years(1), Duration.quarters(4));
	}
	public void testAdd() {
		assertEquals(Duration.days(2), Duration.hours(24).plus(Duration.days(1)));
		assertEquals(Duration.months(4), Duration.months(1).plus(Duration.quarters(1)));
	}
	
	public void testSubtract() {
		assertEquals(Duration.days(2), Duration.days(3).minus(Duration.hours(24)));
		assertEquals(Duration.months(2), Duration.quarters(1).minus(Duration.months(1)));
	}
	
	
	public void testToNormalizedString() {
			assertEquals("2 days", Duration.days(2).toNormalizedString());
			Duration complicatedDuration = Duration.daysHoursMinutesSecondsMillis(5,4,3,2,1);
			assertEquals("5 days, 4 hours, 3 minutes, 2 seconds, 1 millisecond", complicatedDuration.toNormalizedString());
	}
	
	/**
	 * TODO More edge cases and exceptions (like nonconvertable units).
	 */
	public void testCompare() {
		Duration oneHour = Duration.hours(1);
		Duration twoHours = Duration.hours(2);
		Duration sixtyMinutes = Duration.minutes(60); 
		assertTrue(oneHour.compareTo(twoHours) < 0);
		assertTrue(oneHour.compareTo(sixtyMinutes) == 0);
		assertTrue(twoHours.compareTo(oneHour) > 0);
	}

}
