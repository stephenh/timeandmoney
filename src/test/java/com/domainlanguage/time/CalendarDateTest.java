/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Calendar;
import java.util.TimeZone;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.domainlanguage.tests.SerializationTester;

public class CalendarDateTest extends TestCase {
  private CalendarDate feb17 = CalendarDate.from(2003, 2, 17);
  private CalendarDate mar13 = CalendarDate.from(2003, 3, 13);
  private TimeZone gmt = TimeZone.getTimeZone("Universal");
  private TimeZone ct = TimeZone.getTimeZone("America/Chicago");

  public void testSerialization() {
    SerializationTester.assertCanBeSerialized(feb17);
  }

  public void testComparison() {
    Assert.assertTrue(feb17.isBefore(mar13));
    Assert.assertFalse(mar13.isBefore(feb17));
    Assert.assertFalse(feb17.isBefore(feb17));
    Assert.assertFalse(feb17.isAfter(mar13));
    Assert.assertTrue(mar13.isAfter(feb17));
    Assert.assertFalse(feb17.isAfter(feb17));
  }

  public void testStartAsTimePoint() {
    TimePoint feb17StartAsCt = feb17.startAsTimePoint(ct);
    TimePoint feb17Hour0Ct = TimePoint.atMidnight(2003, 2, 17, ct);
    Assert.assertEquals(feb17Hour0Ct, feb17StartAsCt);
  }

  public void testAsTimeInterval() {
    TimeInterval feb17AsCt = feb17.asTimeInterval(ct);
    TimePoint feb17Hour0Ct = TimePoint.atMidnight(2003, 2, 17, ct);
    TimePoint feb18Hour0Ct = TimePoint.atMidnight(2003, 2, 18, ct);
    Assert.assertEquals("start", feb17Hour0Ct, feb17AsCt.start());
    Assert.assertEquals("end", feb18Hour0Ct, feb17AsCt.end());
  }

  public void testFormattedString() {
    Assert.assertEquals("2/17/2003", feb17.toString("M/d/yyyy"));
    //Now a nonsense pattern, to make sure it isn't succeeding by accident.
    Assert.assertEquals("#17-03/02 2003", feb17.toString("#d-yy/MM yyyy"));
  }

  public void testFromFormattedString() {
    Assert.assertEquals(feb17, CalendarDate.from("2/17/2003", "M/d/yyyy"));
    //Now a nonsense pattern, to make sure it isn't succeeding by accident.
    Assert.assertEquals(feb17, CalendarDate.from("#17-03/02 2003", "#d-yy/MM yyyy"));
  }

  public void testFromTimePoint() {
    TimePoint feb18Hour0Ct = TimePoint.atMidnight(2003, 2, 18, gmt);
    CalendarDate mapped = CalendarDate.from(feb18Hour0Ct, ct);
    Assert.assertEquals(CalendarDate.from(2003, 2, 17), mapped);
  }

  public void testIncludes() {
    Assert.assertTrue(feb17.equals(feb17));
    Assert.assertFalse(feb17.equals(mar13));
  }

  public void testDayOfWeek() {
    CalendarDate date = CalendarDate.date(2004, 11, 6);
    Assert.assertEquals(Calendar.SATURDAY, date.dayOfWeek());
    date = CalendarDate.date(2007, 1, 1);
    Assert.assertEquals(Calendar.MONDAY, date.dayOfWeek());
  }

  public void testNextDay() {
    CalendarDate feb28_2004 = CalendarDate.date(2004, 2, 28);
    Assert.assertEquals(CalendarDate.date(2004, 2, 29), feb28_2004.nextDay());
    Assert.assertEquals(CalendarDate.date(2004, 3, 1), feb28_2004.nextDay().nextDay());
  }

  public void testPreviousDay() {
    CalendarDate mar1_2004 = CalendarDate.date(2004, 3, 1);
    Assert.assertEquals(CalendarDate.date(2004, 2, 29), mar1_2004.previousDay());
    Assert.assertEquals(CalendarDate.date(2004, 2, 28), mar1_2004.previousDay().previousDay());
  }

  public void testMonth() {
    CalendarDate nov6_2004 = CalendarDate.date(2004, 11, 6);
    CalendarInterval nov2004 = CalendarInterval.inclusive(2004, 11, 1, 2004, 11, 30);
    Assert.assertEquals(nov2004, nov6_2004.month());

    CalendarDate dec6_2004 = CalendarDate.date(2004, 12, 6);
    CalendarInterval dec2004 = CalendarInterval.inclusive(2004, 12, 1, 2004, 12, 31);
    Assert.assertEquals(dec2004, dec6_2004.month());

    CalendarDate feb9_2004 = CalendarDate.date(2004, 2, 9);
    CalendarInterval feb2004 = CalendarInterval.inclusive(2004, 2, 1, 2004, 2, 29);
    Assert.assertEquals(feb2004, feb9_2004.month());

    CalendarDate feb9_2003 = CalendarDate.date(2003, 2, 9);
    CalendarInterval feb2003 = CalendarInterval.inclusive(2003, 2, 1, 2003, 2, 28);
    Assert.assertEquals(feb2003, feb9_2003.month());

  }

  public void testToString() {
    CalendarDate date = CalendarDate.date(2004, 5, 28);
    Assert.assertEquals("2004-5-28", date.toString());
  }

  public void testConversionToJavaUtil() {
    Calendar expected = Calendar.getInstance(gmt);
    expected.set(Calendar.YEAR, 1969);
    expected.set(Calendar.MONTH, Calendar.JULY);
    expected.set(Calendar.DATE, 20);
    expected.set(Calendar.HOUR, 0);
    expected.set(Calendar.AM_PM, Calendar.AM);
    expected.set(Calendar.MINUTE, 0);
    expected.set(Calendar.SECOND, 0);
    expected.set(Calendar.MILLISECOND, 0);

    CalendarDate date = CalendarDate.from(1969, 7, 20);
    Calendar actual = date.asJavaCalendarUniversalZoneMidnight();
    Assert.assertEquals(expected.get(Calendar.HOUR), actual.get(Calendar.HOUR));
    Assert.assertEquals(expected.get(Calendar.AM_PM), actual.get(Calendar.AM_PM));
    Assert.assertEquals(expected.get(Calendar.HOUR_OF_DAY), actual.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(expected, actual);
  }

}
