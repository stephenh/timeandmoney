/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.TimeZone;

import com.domainlanguage.testutil.SerializationTest;

import junit.framework.TestCase;

public class CalendarDateTest extends TestCase {
	
	CalendarDate feb17 = CalendarDate.from(2003, 2, 17);
	CalendarDate mar13 = CalendarDate.from(2003, 3, 13);
	TimeZone gmt = TimeZone.getTimeZone("Universal");
	TimeZone ct = TimeZone.getTimeZone("America/Chicago");

    public void testSerialization() {
    	SerializationTest.assertSerializationWorks(feb17);
    }
	
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
	
	public void testFormattedString() {
		assertEquals("2/17/2003", feb17.toString("M/d/yyyy"));
		//Now a nonsense pattern, to make sure it isn't succeeding by accident.
		assertEquals("#17-03/02 2003", feb17.toString("#d-yy/MM yyyy"));
	}

	public void testFromFormattedString() {
		assertEquals(feb17, CalendarDate.from("2/17/2003", "M/d/yyyy"));
		//Now a nonsense pattern, to make sure it isn't succeeding by accident.
		assertEquals(feb17, CalendarDate.from("#17-03/02 2003", "#d-yy/MM yyyy"));
	}
	
	public void testFromTimePoint() {
		TimePoint feb18Hour0Ct = TimePoint.atMidnight(2003, 2, 18, gmt);
		CalendarDate mapped = CalendarDate.from(feb18Hour0Ct, ct);
		assertEquals(CalendarDate.from(2003, 2, 17), mapped);
	}
//	public String getSqlString() {
//		_formatter.applyPattern("#M/d/yyyy#");
//		return _formatter.format(getTime());
//	}

	  public void testIncludes(){
	    assertTrue(feb17.includes(feb17));
	    assertFalse(feb17.includes(mar13));
	  }
	  
	  public void testToString() {
	  	CalendarDate date = CalendarInterval.date(2004, 5, 28);
	  	assertEquals("2004-5-28", date.toString());
	  }

}
