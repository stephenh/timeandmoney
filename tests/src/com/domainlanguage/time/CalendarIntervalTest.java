/**
Copyright (c) 2004 Domain Language, Inc. (www.domainlanguage.com)

Permission is hereby granted, free of charge, to any person obtaining a 
copy of this software and associated documentation files (the "Software"), 
to deal in the Software without restriction, including without limitation 
the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom the 
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included 
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS 
OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
IN THE SOFTWARE.
*/

package com.domainlanguage.time;

import java.util.Iterator;
import java.util.TimeZone;

import junit.framework.TestCase;

public class CalendarIntervalTest extends TestCase {

	CalendarDate may1 = CalendarInterval.date(2004, 5, 1);
	CalendarDate may2 = CalendarInterval.date(2004, 5, 2);
	CalendarDate may3 = CalendarInterval.date(2004, 5, 3);
	CalendarDate may20 = CalendarInterval.date(2004, 5, 20);
	CalendarDate may31 = CalendarInterval.date(2004, 5, 31);
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
	
 	public void testEquals() {
	    assertTrue(may.equals(CalendarInterval.inclusive(may1, may31)));
	    assertFalse(may.equals(may1));
	    assertFalse(may.equals(CalendarInterval.inclusive(may1, may20)));
	  }

 	public void testDaysAdd() {
 		assertEquals(may20, may1.plusDays(19));
 	}
 	public void testDaysIterator() {
 		Iterator iter = CalendarInterval.inclusive(may1, may3).daysIterator();
 		assertTrue(iter.hasNext());
 		assertEquals(may1, iter.next());
 		assertTrue(iter.hasNext());
 		assertEquals(may2, iter.next());
 		assertTrue(iter.hasNext());
 		assertEquals(may3, iter.next());
 		assertFalse(iter.hasNext());
		
 	}

 	
}
