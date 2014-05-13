/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.domainlanguage.intervals.Interval;
import com.domainlanguage.tests.SerializationTester;

public class CalendarIntervalTest extends TestCase {

  private static final CalendarDate may1 = CalendarDate.date(2004, 5, 1);
  private static final CalendarDate may2 = CalendarDate.date(2004, 5, 2);
  private static final CalendarDate may3 = CalendarDate.date(2004, 5, 3);
  private static final CalendarDate may14 = CalendarDate.date(2004, 5, 14);
  private static final CalendarDate may20 = CalendarDate.date(2004, 5, 20);
  private static final CalendarDate may31 = CalendarDate.date(2004, 5, 31);
  private static final CalendarDate apr15 = CalendarDate.date(2004, 4, 15);
  private static final CalendarDate jun1 = CalendarDate.date(2004, 6, 1);
  private static final CalendarInterval may = CalendarInterval.inclusive(2004, 5, 1, 2004, 5, 31);
  private static final TimeZone ct = TimeZone.getTimeZone("America/Chicago");

  public void testSerialization() {
    SerializationTester.assertCanBeSerialized(may);
  }

  public void testTranslationToTimeInterval() {
    TimeInterval day = may20.asTimeInterval(ct);
    Assert.assertEquals("May20Ct", TimePoint.atMidnight(2004, 5, 20, ct), day.start());
  }

  public void testTranslationToTimeIntervalFromCalendarInterval() {
    TimeInterval days = may20.through(may31).asTimeInterval(ct);
    Assert.assertEquals("May20Ct", TimePoint.atMidnight(2004, 5, 20, ct), days.start());
    Assert.assertEquals("May31Ct", TimePoint.atMidnight(2004, 6, 1, ct), days.end());
  }

  public void testIncludes() {
    Assert.assertFalse("apr15", may.includes(apr15));
    Assert.assertTrue("may1", may.includes(may1));
    Assert.assertTrue("may20", may.includes(may20));
    Assert.assertFalse("jun1", may.includes(jun1));
    Assert.assertTrue("may", may.covers(may));
  }

  public void testEquals() {
    Assert.assertTrue(may.equals(CalendarInterval.inclusive(may1, may31)));
    Assert.assertFalse(may.equals(may1));
    Assert.assertFalse(may.equals(CalendarInterval.inclusive(may1, may20)));
  }

  public void testDaysAdd() {
    Assert.assertEquals(may20, may1.plusDays(19));
  }

  public void testIterator() {
    int i = 0;
    for (@SuppressWarnings("unused")
    CalendarDate day : may1.through(may31)) {
      i++;
    }
    Assert.assertEquals(i, 31);
  }

  public void testDaysIterator() {
    Iterator<CalendarDate> iterator = CalendarInterval.inclusive(may1, may3).daysIterator();
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(may1, iterator.next());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(may2, iterator.next());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(may3, iterator.next());
    Assert.assertFalse(iterator.hasNext());

  }

  public void testSubintervalIterator() {
    CalendarInterval may1_3 = CalendarInterval.inclusive(may1, may3);
    Iterator<CalendarInterval> iterator = may1_3.subintervalIterator(Duration.days(1));
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(may1, iterator.next().start());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(may2, iterator.next().start());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(may3, iterator.next().start());
    Assert.assertFalse(iterator.hasNext());

    iterator = may1_3.subintervalIterator(Duration.days(2));
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(may1.through(may2), iterator.next());
    Assert.assertFalse(iterator.hasNext());

    try {
      iterator = may1_3.subintervalIterator(Duration.hours(25));
      Assert.fail("CalendarInterval should not accept subinterval length that is not a multiple of days.");
    } catch (IllegalArgumentException e) {
      Assert.assertTrue(true);
    }

    iterator = may1_3.subintervalIterator(Duration.months(1));
    Assert.assertFalse(iterator.hasNext());

    CalendarInterval apr15_jun1 = CalendarInterval.inclusive(apr15, jun1);
    iterator = apr15_jun1.subintervalIterator(Duration.months(1));
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(apr15.through(may14), iterator.next());
    Assert.assertFalse(iterator.hasNext());
  }

  public void testLength() {
    Assert.assertEquals(Duration.days(3), may1.through(may3).length());
    CalendarInterval may2002_july2004 = CalendarInterval.inclusive(2002, 5, 1, 2004, 7, 1);
    // (5/1/2002-4/30/2003) 365 days + (-4/30/2004) 366 + (5/1-7/31) 31+30+1 = 793 days
    Assert.assertEquals(Duration.days(793), may2002_july2004.length());
    Assert.assertEquals(Duration.months(26), may2002_july2004.lengthInMonths());
    Assert.assertEquals(Duration.months(1), apr15.through(may14).lengthInMonths());
  }

  public void testThrough() {
    Assert.assertEquals(31, may1.through(may31).lengthInDaysInt());
  }

  public void testUntil() {
    Assert.assertEquals(30, may1.until(may31).lengthInDaysInt());
  }

  public void testComplements() {
    CalendarInterval may1Onward = CalendarInterval.inclusive(may1, null);
    CalendarInterval may2Onward = CalendarInterval.inclusive(may2, null);
    List<Interval<CalendarDate>> complementList = may2Onward.complementRelativeTo(may1Onward);
    Assert.assertEquals(1, complementList.size());

    CalendarInterval complement = (CalendarInterval) complementList.iterator().next();
    Assert.assertTrue(complement.isClosed());
    Assert.assertEquals(may1, complement.start());
    Assert.assertEquals(may1, complement.end());
  }

  public void testSingleDateCalendarIntervalCompare() {
    CalendarInterval may1_may1 = CalendarInterval.inclusive(may1, may1);
    Assert.assertEquals(may1, may1_may1.start());
    Assert.assertEquals(may1, may1_may1.end());
    Assert.assertEquals(0, may1.compareTo(may1_may1.start()));
    Assert.assertEquals(0, may1_may1.start().compareTo(may1));
    CalendarInterval may1_may2 = CalendarInterval.inclusive(may1, may2);
    Assert.assertTrue(may1_may2.compareTo(may1_may1) > 0);
  }

  public void testEverFromToString() {
    CalendarDate x = CalendarDate.from(2007, 6, 5);
    CalendarInterval i = CalendarInterval.everFrom(x);
    Assert.assertEquals("[2007-6-5, Infinity]", i.toString());
  }

  /*
   * [ 1792849 ] ConcreteCalendarInterval allows misordered limits
   */
  public void testBackwardCalendarIvalIntersection() {
    IllegalArgumentException problem = null;
    try {
      @SuppressWarnings("unused")
      Interval<CalendarDate> i = CalendarInterval.inclusive(2002, 1, 1, 1776, 7, 4);
    } catch (IllegalArgumentException error) {
      problem = error;
    }
    Assert.assertNotNull(problem);
  }
}
