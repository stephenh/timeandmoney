/**
 *
 * This code is provided under the following licence: 
 * MIT Licence
 * timeandmoney.sourceforge.net
 * Copyright 2004, Domain Language, Inc.
 */

package timelanguage;

import java.util.Calendar;

import junit.framework.TestCase;


public class TimeUnitTest extends TestCase {

	public void testToString() {
		assertEquals("month", TimeUnit.month.toString());
	}
	
	public void testConvertibleToMilliseconds() {
		assertTrue(TimeUnit.millisecond.isConvertibleToMilliseconds());
		assertTrue(TimeUnit.hour.isConvertibleToMilliseconds());
		assertTrue(TimeUnit.day.isConvertibleToMilliseconds());
		assertTrue(TimeUnit.week.isConvertibleToMilliseconds());
		assertFalse(TimeUnit.month.isConvertibleToMilliseconds());
		assertFalse(TimeUnit.year.isConvertibleToMilliseconds());
	}
	
	public void testComparison() {
		assertTrue(TimeUnit.hour.compareTo(TimeUnit.millisecond) >0);
		assertTrue(TimeUnit.day.compareTo(TimeUnit.hour) > 0);
		assertTrue(TimeUnit.month.compareTo(TimeUnit.day) > 0);
		assertTrue(TimeUnit.quarter.compareTo(TimeUnit.hour) > 0);
		assertTrue(TimeUnit.year.compareTo(TimeUnit.quarter) > 0);
	}
	
	public void testJavaCalendarConstantForBaseType() {
		assertEquals(Calendar.MILLISECOND, TimeUnit.millisecond.javaCalendarConstantForBaseType());
		assertEquals(Calendar.MILLISECOND, TimeUnit.hour.javaCalendarConstantForBaseType());
		assertEquals(Calendar.MILLISECOND, TimeUnit.day.javaCalendarConstantForBaseType());
		assertEquals(Calendar.MILLISECOND, TimeUnit.week.javaCalendarConstantForBaseType());
		assertEquals(Calendar.MONTH, TimeUnit.month.javaCalendarConstantForBaseType());
		assertEquals(Calendar.MONTH, TimeUnit.quarter.javaCalendarConstantForBaseType());
		assertEquals(Calendar.MONTH, TimeUnit.year.javaCalendarConstantForBaseType());
	}
}
