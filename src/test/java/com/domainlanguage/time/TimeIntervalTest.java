/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.domainlanguage.tests.SerializationTester;

public class TimeIntervalTest extends TestCase {

  private static final TimePoint dec19_2003 = TimePoint.atMidnightGMT(2003, 12, 19);
  private static final TimePoint dec20_2003 = TimePoint.atMidnightGMT(2003, 12, 20);
  private static final TimePoint dec21_2003 = TimePoint.atMidnightGMT(2003, 12, 21);
  private static final TimePoint dec22_2003 = TimePoint.atMidnightGMT(2003, 12, 22);
  private static final TimePoint dec23_2003 = TimePoint.atMidnightGMT(2003, 12, 23);

  public void testSerialization() {
    TimeInterval interval = TimeInterval.closed(dec20_2003, dec22_2003);
    SerializationTester.assertCanBeSerialized(interval);
  }

  public void testBeforeClosed() {
    TimeInterval interval = TimeInterval.closed(dec20_2003, dec22_2003);
    // Only the upper end should matter for this test.
    Assert.assertFalse(interval.isBefore(dec21_2003));
    Assert.assertFalse(interval.isBefore(dec22_2003));
    Assert.assertTrue(interval.isBefore(dec23_2003));
  }

  public void testAfterClosed() {
    TimeInterval interval = TimeInterval.closed(dec20_2003, dec22_2003);
    // Only the lower end should matter for this test.
    Assert.assertTrue(interval.isAfter(dec19_2003));
    Assert.assertFalse(interval.isAfter(dec20_2003));
    Assert.assertFalse(interval.isAfter(dec21_2003));
  }

  public void testIncludesClosed() {
    TimeInterval interval = TimeInterval.closed(dec20_2003, dec22_2003);
    Assert.assertFalse(interval.includes(dec19_2003));
    Assert.assertTrue(interval.includes(dec20_2003));
    Assert.assertTrue(interval.includes(dec21_2003));
    Assert.assertTrue(interval.includes(dec22_2003));
    Assert.assertFalse(interval.includes(dec23_2003));
  }

  public void testBeforeOpen() {
    TimeInterval interval = TimeInterval.open(dec20_2003, dec22_2003);
    // Only the upper end should matter for this test.
    Assert.assertFalse(interval.isBefore(dec21_2003));
    Assert.assertTrue(interval.isBefore(dec22_2003));
    Assert.assertTrue(interval.isBefore(dec23_2003));
  }

  public void testAfterOpen() {
    TimeInterval interval = TimeInterval.open(dec20_2003, dec22_2003);
    // Only the lower end should matter for this test.
    Assert.assertTrue(interval.isAfter(dec19_2003));
    Assert.assertTrue(interval.isAfter(dec20_2003));
    Assert.assertFalse(interval.isAfter(dec21_2003));
  }

  public void testIncludesOpen() {
    TimeInterval interval = TimeInterval.open(dec20_2003, dec22_2003);
    Assert.assertFalse(interval.includes(dec19_2003));
    Assert.assertFalse(interval.includes(dec20_2003));
    Assert.assertTrue(interval.includes(dec21_2003));
    Assert.assertFalse(interval.includes(dec22_2003));
    Assert.assertFalse(interval.includes(dec23_2003));
  }

  public void testIncludesHalfOpen() {
    TimeInterval interval = TimeInterval.over(dec20_2003, true, dec22_2003, false);
    Assert.assertFalse(interval.includes(dec19_2003));
    Assert.assertTrue(interval.includes(dec20_2003));
    Assert.assertTrue(interval.includes(dec21_2003));
    Assert.assertFalse(interval.includes(dec22_2003));
    Assert.assertFalse(interval.includes(dec23_2003));
  }

  public void testCreateWithDurationFrom() {
    Duration twoDays = Duration.days(2);
    TimeInterval following = TimeInterval.startingFrom(dec20_2003, true, twoDays, true);
    Assert.assertEquals("[ dec20", dec20_2003, following.start());
    Assert.assertEquals("dec 22]", dec22_2003, following.end());

  }

  public void testCreateWithDurationUntil() {
    Duration twoDays = Duration.days(2);
    TimeInterval preceding = TimeInterval.preceding(dec21_2003, true, twoDays, true);
    Assert.assertEquals("[ dec19", dec19_2003, preceding.start());
    Assert.assertEquals("dec21 )", dec21_2003, preceding.end());
  }

  public void testDefaultFromPoints() {
    /*       Default is closed start, open end [start, end)
             which is the most common convention. For example,
             Days include 12:00am at their start, but do not
             include the 12:00am that end them.
    */
    TimeInterval interval = TimeInterval.over(dec20_2003, dec22_2003);
    Assert.assertFalse(interval.includes(dec19_2003));
    Assert.assertTrue(interval.includes(dec20_2003));
    Assert.assertTrue(interval.includes(dec21_2003));
    Assert.assertFalse(interval.includes(dec22_2003));
    Assert.assertFalse(interval.includes(dec23_2003));
  }

  public void testDefaultFromDuration() {
    /*       Default is closed start, open end [start, end)
             which is the most common convention. For example,
             Days include 12:00am at their start, but do not
             include the 12:00am that end them.
    */
    TimeInterval interval = TimeInterval.startingFrom(dec20_2003, Duration.hours(48));
    Assert.assertFalse(interval.includes(dec19_2003));
    Assert.assertTrue(interval.includes(dec20_2003));
    Assert.assertTrue(interval.includes(dec21_2003));
    Assert.assertFalse(interval.includes(dec22_2003));
    Assert.assertFalse(interval.includes(dec23_2003));
  }

  public void testEverFrom() {
    TimeInterval afterDec20 = TimeInterval.everFrom(dec20_2003);
    Assert.assertTrue(afterDec20.includes(TimePoint.atMidnightGMT(2062, 3, 5)));
    Assert.assertFalse(afterDec20.includes(TimePoint.atMidnightGMT(1776, 7, 4)));
    Assert.assertTrue(afterDec20.includes(dec20_2003));
  }

  public void testEverUntil() {
    TimeInterval afterDec20 = TimeInterval.everPreceding(dec20_2003);
    Assert.assertFalse(afterDec20.includes(TimePoint.atMidnightGMT(2062, 3, 5)));
    Assert.assertTrue(afterDec20.includes(TimePoint.atMidnightGMT(1776, 7, 4)));
    Assert.assertFalse(afterDec20.includes(dec20_2003));
  }

  public void testLength() {
    TimeInterval interval = TimeInterval.open(dec20_2003, dec22_2003);
    Assert.assertEquals(Duration.days(2), interval.length());

    TimePoint first = TimePoint.atGMT(2004, 1, 1, 1, 1, 1, 1);
    TimePoint second = TimePoint.atGMT(2004, 1, 6, 5, 4, 3, 2);
    interval = TimeInterval.closed(first, second);
    Duration expectedLength = Duration.daysHoursMinutesSecondsMilliseconds(5, 4, 3, 2, 1);
    Assert.assertEquals(expectedLength, interval.length());
  }

  public void testDaysIterator() {
    TimePoint start = TimePoint.atGMT(2004, 2, 5, 10, 0);
    TimePoint end = TimePoint.atGMT(2004, 2, 8, 2, 0);
    TimeInterval interval = TimeInterval.over(start, end);
    Iterator<TimePoint> it = interval.daysIterator();
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(start, it.next());
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(TimePoint.atGMT(2004, 2, 6, 10, 0), it.next());
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(TimePoint.atGMT(2004, 2, 7, 10, 0), it.next());
    Assert.assertFalse(it.hasNext());
  }

  public void testSubintervalIterator() {
    TimePoint d4_h10 = TimePoint.atGMT(2004, 2, 4, 10, 0);
    TimePoint d6_h10 = TimePoint.atGMT(2004, 2, 6, 10, 0);
    TimePoint d8_h10 = TimePoint.atGMT(2004, 2, 8, 10, 0);
    TimePoint d9_h2 = TimePoint.atGMT(2004, 2, 9, 2, 0);

    TimeInterval interval = TimeInterval.over(d4_h10, d9_h2);
    Iterator<TimeInterval> iterator = interval.subintervalIterator(Duration.days(2));
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(TimeInterval.over(d4_h10, d6_h10), iterator.next());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(TimeInterval.over(d6_h10, d8_h10), iterator.next());
    Assert.assertFalse(iterator.hasNext());
    Assert.assertNull(iterator.next());

    iterator = interval.subintervalIterator(Duration.weeks(1));
    Assert.assertFalse(iterator.hasNext());

    TimePoint h2 = d9_h2;
    TimePoint h3_m30 = TimePoint.atGMT(2004, 2, 9, 3, 30);
    TimePoint h5 = TimePoint.atGMT(2004, 2, 9, 5, 0);
    TimePoint h6_m30 = TimePoint.atGMT(2004, 2, 9, 6, 30);
    TimePoint h8 = TimePoint.atGMT(2004, 2, 9, 8, 0);

    TimeInterval interval2 = TimeInterval.over(h2, h8);
    iterator = interval2.subintervalIterator(Duration.minutes(90));
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(TimeInterval.over(h2, h3_m30), iterator.next());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(TimeInterval.over(h3_m30, h5), iterator.next());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(TimeInterval.over(h5, h6_m30), iterator.next());
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(TimeInterval.over(h6_m30, h8), iterator.next());
    Assert.assertFalse(iterator.hasNext());
    Assert.assertNull(iterator.next());
  }

  public void testIntersection() {
    TimeInterval i19_22 = TimeInterval.over(dec19_2003, dec22_2003);
    TimeInterval i20_23 = TimeInterval.over(dec20_2003, dec23_2003);
    TimeInterval intersection = i19_22.intersect(i20_23);
    Assert.assertEquals(dec20_2003, intersection.start());
    Assert.assertEquals(dec22_2003, intersection.end());
    Assert.assertTrue("intersects true", i19_22.intersects(i20_23));

    TimeInterval i19_21 = TimeInterval.over(dec19_2003, dec21_2003);
    TimeInterval i22_23 = TimeInterval.over(dec22_2003, dec23_2003);
    Assert.assertFalse("intersects false", i19_21.intersects(i22_23));
  }

}
