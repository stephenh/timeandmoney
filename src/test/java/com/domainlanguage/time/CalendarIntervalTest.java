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

  private CalendarDate may1 = CalendarDate.date(2004, 5, 1);
  private CalendarDate may2 = CalendarDate.date(2004, 5, 2);
  private CalendarDate may3 = CalendarDate.date(2004, 5, 3);
  private CalendarDate may14 = CalendarDate.date(2004, 5, 14);
  private CalendarDate may20 = CalendarDate.date(2004, 5, 20);
  private CalendarDate may31 = CalendarDate.date(2004, 5, 31);
  private CalendarDate apr15 = CalendarDate.date(2004, 4, 15);
  private CalendarDate jun1 = CalendarDate.date(2004, 6, 1);
  private CalendarInterval may = CalendarInterval.inclusive(2004, 5, 1, 2004, 5, 31);
  private TimeZone ct = TimeZone.getTimeZone("America/Chicago");

  public void testSerialization() {
    SerializationTester.assertCanBeSerialized(this.may);
  }

  public void testTranslationToTimeInterval() {
    TimeInterval day = this.may20.asTimeInterval(this.ct);
    Assert.assertEquals("May20Ct", TimePoint.atMidnight(2004, 5, 20, this.ct), day.start());
  }

  public void testIncludes() {
    Assert.assertFalse("apr15", this.may.includes(this.apr15));
    Assert.assertTrue("may1", this.may.includes(this.may1));
    Assert.assertTrue("may20", this.may.includes(this.may20));
    Assert.assertFalse("jun1", this.may.includes(this.jun1));
    Assert.assertTrue("may", this.may.covers(this.may));
  }

  public void testEquals() {
    Assert.assertTrue(this.may.equals(CalendarInterval.inclusive(this.may1, this.may31)));
    Assert.assertFalse(this.may.equals(this.may1));
    Assert.assertFalse(this.may.equals(CalendarInterval.inclusive(this.may1, this.may20)));
  }

  public void testDaysAdd() {
    Assert.assertEquals(this.may20, this.may1.plusDays(19));
  }

  public void testIterator() {
    int i = 0;
    for (@SuppressWarnings("unused")
    CalendarDate day : this.may1.through(this.may31)) {
      i++;
    }
    Assert.assertEquals(i, 31);
  }

  public void testDaysIterator() {
    Iterator<CalendarDate> iterator = CalendarInterval.inclusive(this.may1, this.may3).daysIterator();
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(this.may1, iterator.next());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(this.may2, iterator.next());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(this.may3, iterator.next());
    Assert.assertFalse(iterator.hasNext());

  }

  public void testSubintervalIterator() {
    CalendarInterval may1_3 = CalendarInterval.inclusive(this.may1, this.may3);
    Iterator<CalendarInterval> iterator = may1_3.subintervalIterator(Duration.days(1));
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(this.may1, iterator.next().start());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(this.may2, iterator.next().start());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(this.may3, iterator.next().start());
    Assert.assertFalse(iterator.hasNext());

    iterator = may1_3.subintervalIterator(Duration.days(2));
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(this.may1.through(this.may2), iterator.next());
    Assert.assertFalse(iterator.hasNext());

    try {
      iterator = may1_3.subintervalIterator(Duration.hours(25));
      Assert.fail("CalendarInterval should not accept subinterval length that is not a multiple of days.");
    } catch (IllegalArgumentException e) {
      Assert.assertTrue(true);
    }

    iterator = may1_3.subintervalIterator(Duration.months(1));
    Assert.assertFalse(iterator.hasNext());

    CalendarInterval apr15_jun1 = CalendarInterval.inclusive(this.apr15, this.jun1);
    iterator = apr15_jun1.subintervalIterator(Duration.months(1));
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(this.apr15.through(this.may14), iterator.next());
    Assert.assertFalse(iterator.hasNext());
  }

  public void testLength() {
    Assert.assertEquals(Duration.days(3), this.may1.through(this.may3).length());
    CalendarInterval may2002_july2004 = CalendarInterval.inclusive(2002, 5, 1, 2004, 7, 1);
    // (5/1/2002-4/30/2003) 365 days + (-4/30/2004) 366 + (5/1-7/31) 31+30+1 = 793 days
    Assert.assertEquals(Duration.days(793), may2002_july2004.length());
    Assert.assertEquals(Duration.months(26), may2002_july2004.lengthInMonths());
    Assert.assertEquals(Duration.months(1), this.apr15.through(this.may14).lengthInMonths());
  }

  public void testComplements() {
    CalendarInterval may1Onward = CalendarInterval.inclusive(this.may1, null);
    CalendarInterval may2Onward = CalendarInterval.inclusive(this.may2, null);
    List<Interval<CalendarDate>> complementList = may2Onward.complementRelativeTo(may1Onward);
    Assert.assertEquals(1, complementList.size());

    CalendarInterval complement = (CalendarInterval) complementList.iterator().next();
    Assert.assertTrue(complement.isClosed());
    Assert.assertEquals(this.may1, complement.start());
    Assert.assertEquals(this.may1, complement.end());
  }

  public void testSingleDateCalendarIntervalCompare() {
    CalendarInterval may1_may1 = CalendarInterval.inclusive(this.may1, this.may1);
    Assert.assertEquals(this.may1, may1_may1.start());
    Assert.assertEquals(this.may1, may1_may1.end());
    Assert.assertEquals(0, this.may1.compareTo(may1_may1.start()));
    Assert.assertEquals(0, may1_may1.start().compareTo(this.may1));
    CalendarInterval may1_may2 = CalendarInterval.inclusive(this.may1, this.may2);
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
