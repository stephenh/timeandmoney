/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.*;

import junit.framework.*;

import com.domainlanguage.tests.*;

public class CalendarIntervalTest extends TestCase {

    CalendarDate may1 = CalendarInterval.date(2004, 5, 1);
    CalendarDate may2 = CalendarInterval.date(2004, 5, 2);
    CalendarDate may3 = CalendarInterval.date(2004, 5, 3);
    CalendarDate may14 = CalendarInterval.date(2004, 5, 14);
    CalendarDate may20 = CalendarInterval.date(2004, 5, 20);
    CalendarDate may31 = CalendarInterval.date(2004, 5, 31);
    CalendarDate apr15 = CalendarInterval.date(2004, 4, 15);
    CalendarDate jun1 = CalendarInterval.date(2004, 6, 1);
    CalendarInterval may = CalendarInterval.inclusive(2004, 5, 1, 2004, 5, 31);
    TimeZone gmt = TimeZone.getTimeZone("Universal");
    TimeZone pt = TimeZone.getTimeZone("America/Los_Angeles");
    TimeZone ct = TimeZone.getTimeZone("America/Chicago");

    public void testSerialization() {
        SerializationTester.assertCanBeSerialized(may);
    }

    public void testTranslationToTimeInterval() {
        TimeInterval day = may20.asTimeInterval(ct);
        assertEquals("May20Ct", TimePoint.atMidnight(2004, 5, 20, ct), day.start());
        //		assertEquals(TimePoint.atMidnight(2004, 5, 21, ct), day.end());

        //		TimeInterval interval = CalendarInterval.inclusive(apr15,
        // may20).asTimeInterval(pt);
        //		assertEquals(TimePoint.atMidnight(2004, 4, 15, pst),
        // interval.start());
        //		assertEquals(TimePoint.atMidnight(2004, 5, 21, pt), interval.end());

    }

    public void testIncludes() {
        assertFalse("apr15", may.covers(apr15));
        assertTrue("may1", may.covers(may1));
        assertTrue("may20", may.covers(may20));
        assertFalse("jun1", may.covers(jun1));
        assertTrue("may", may.covers(may));
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

    public void testSubintervalIterator() {
        CalendarInterval may1_3 = CalendarInterval.inclusive(may1, may3);
        Iterator iter = may1_3.subintervalIterator(Duration.days(1));
        assertTrue(iter.hasNext());
        assertEquals(may1, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(may2, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(may3, iter.next());
        assertFalse(iter.hasNext());

        iter = may1_3.subintervalIterator(Duration.days(2));
        assertTrue(iter.hasNext());
        assertEquals(may1.through(may2), iter.next());
        assertFalse(iter.hasNext());

        try {
            iter = may1_3.subintervalIterator(Duration.hours(25));
            fail("CalendarInterval should not accept subinterval length that is not a multiple of days.");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        iter = may1_3.subintervalIterator(Duration.months(1));
        assertFalse(iter.hasNext());

        CalendarInterval apr15_jun1 = CalendarInterval.inclusive(apr15, jun1);
        iter = apr15_jun1.subintervalIterator(Duration.months(1));
        assertTrue(iter.hasNext());
        assertEquals(apr15.through(may14), iter.next());
        assertFalse(iter.hasNext());
    }

    public void testLength() {
        assertEquals(Duration.days(3), may1.through(may3).length());
        CalendarInterval may2002_july2004 = CalendarInterval.inclusive(2002, 5, 1, 2004, 7, 1);
        // (5/1/2002-4/30/2003) 365 days + (-4/30/2004) 366 + (5/1-7/31) 31+30+1
        // = 793 days
        assertEquals(Duration.days(793), may2002_july2004.length());
        assertEquals(Duration.months(26), may2002_july2004.lengthInMonths());
        assertEquals(Duration.months(1), apr15.through(may14).lengthInMonths());
    }

    public void testComplements() {
        CalendarInterval may1Onward = CalendarInterval.inclusive(may1, CalendarDate.FAR_FUTURE);
        CalendarInterval may2Onward = CalendarInterval.inclusive(may2, CalendarDate.FAR_FUTURE);

        List complementList = may2Onward.complementRelativeTo(may1Onward);
        assertEquals(1, complementList.size());
        CalendarInterval complement = (CalendarInterval) complementList.iterator().next();
        assertTrue(complement.isClosed());
        assertEquals(may1, complement);
    }

    public void testSingleDateCalendarIntervalCompare() {
        CalendarInterval may1_may1 = CalendarInterval.inclusive(may1, may1);
        assertEquals(may1, may1_may1);
        assertEquals(0, may1.compareTo(may1_may1));
        assertEquals(0, may1_may1.compareTo(may1));
        CalendarInterval may1_may2 = CalendarInterval.inclusive(may1, may2);
        assertTrue(may1.compareTo(may1_may2) < 0);
        assertTrue(may1_may2.compareTo(may1) > 0);
    }
}