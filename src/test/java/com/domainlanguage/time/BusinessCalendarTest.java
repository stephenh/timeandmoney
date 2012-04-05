/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

public class BusinessCalendarTest extends TestCase {

  private BusinessCalendar businessCalendar() {
    BusinessCalendar cal = new BusinessCalendar();
    cal.addHolidays(_HolidayDates.defaultHolidays());
    return cal;
  }

  public void testElapsedBusinessDays() {
    CalendarDate nov1 = CalendarDate.from(2004, 11, 1);
    CalendarDate nov30 = CalendarDate.from(2004, 11, 30);
    CalendarInterval interval = CalendarInterval.inclusive(nov1, nov30);
    Assert.assertEquals(Duration.days(30), interval.length());
    // 1 holiday (Thanksgiving on a Thursday) + 8 weekend days.
    Assert.assertEquals(21, businessCalendar().getElapsedBusinessDays(interval));
  }

  public void testIsWeekend() {
    CalendarDate saturday = CalendarDate.from(2004, 1, 10);
    Assert.assertTrue(businessCalendar().isWeekend(saturday));

    CalendarDate sunday = saturday.nextDay();
    Assert.assertTrue(businessCalendar().isWeekend(sunday));

    CalendarDate day = sunday;
    for (int i = 0; i < 5; i++) {
      day = day.nextDay();
      Assert.assertFalse("it's a midweek day", businessCalendar().isWeekend(day));
    }
    day = day.nextDay();
    Assert.assertTrue("finally, the weekend is here...", businessCalendar().isWeekend(day));

    CalendarDate newYearEve = CalendarDate.from(2004, 1, 1); // it's a
    Assert.assertFalse("a holiday is not necessarily a weekend day", businessCalendar().isWeekend(newYearEve));
  }

  public void testIsHoliday() {
    CalendarDate newYearEve = CalendarDate.from(2004, 1, 1); // it's a
    Assert.assertTrue("New Years Eve is a holiday.", businessCalendar().isHoliday(newYearEve));
    Assert.assertFalse("The day after New Years Eve is not a holiday.", businessCalendar().isHoliday(newYearEve.nextDay()));
  }

  public void testIsBusinessDay() {
    CalendarDate day = CalendarDate.from(2004, 1, 12); // it's a Monday
    for (int i = 0; i < 5; i++) {
      Assert.assertTrue("another working day", businessCalendar().isBusinessDay(day));
      day = day.nextDay();
    }
    Assert.assertFalse("finally, saturday arrived ...", businessCalendar().isBusinessDay(day));
    Assert.assertFalse("... then sunday", businessCalendar().isBusinessDay(day.nextDay()));

    CalendarDate newYearEve = CalendarDate.from(2004, 1, 1); // it's a
    Assert.assertFalse("hey, it's a holiday", businessCalendar().isBusinessDay(newYearEve));
  }

  public void testNearestBusinessDay() {
    CalendarDate saturday = CalendarDate.from(2004, 1, 10);
    CalendarDate sunday = saturday.nextDay();
    CalendarDate monday = sunday.nextDay();
    Assert.assertEquals(monday, businessCalendar().nearestBusinessDay(saturday));
    Assert.assertEquals(monday, businessCalendar().nearestBusinessDay(sunday));
    Assert.assertEquals(monday, businessCalendar().nearestBusinessDay(monday));

    CalendarDate newYearEve = CalendarDate.from(2004, 1, 1); // it's a
    Assert.assertEquals("it's a holiday & a thursday; wait till friday", newYearEve.nextDay(), businessCalendar().nearestBusinessDay(newYearEve));

    CalendarDate christmas = CalendarDate.from(2004, 12, 24); // it's a
    Assert.assertEquals(
      "it's a holiday & a friday; wait till monday",
      CalendarDate.from(2004, 12, 27),
      businessCalendar().nearestBusinessDay(christmas));
  }

  public void testBusinessDaysIterator() {
    CalendarDate start = CalendarDate.from(2004, 2, 5);
    CalendarDate end = CalendarDate.from(2004, 2, 8);
    CalendarInterval interval = CalendarInterval.inclusive(start, end);
    Iterator<CalendarDate> it = businessCalendar().businessDaysOnly(interval.daysIterator());
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(start, it.next());
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(CalendarDate.from(2004, 2, 6), it.next());
    Assert.assertFalse(it.hasNext());
  }

  public void testNextBusinessDayOverWeekend() {
    CalendarDate friday = CalendarDate.from(2006, 06, 16);
    CalendarDate monday = CalendarDate.from(2006, 06, 19);
    CalendarDate actual = businessCalendar().nextBusinessDay(friday);
    Assert.assertEquals(monday, actual);
  }

  public void testNextBusinessDayOverWeekday() {
    CalendarDate monday = CalendarDate.from(2006, 06, 19);
    CalendarDate tuesday = CalendarDate.from(2006, 06, 20);
    CalendarDate actual = businessCalendar().nextBusinessDay(monday);
    Assert.assertEquals(tuesday, actual);
  }

  public void testPlusBusinessDayZero() {
    CalendarDate monday = CalendarDate.from(2006, 06, 19);
    CalendarDate actual = businessCalendar().plusBusinessDays(monday, 0);
    Assert.assertEquals(monday, actual);
  }

  public void testPlusNonBusinessDayZero() {
    CalendarDate saturday = CalendarDate.from(2006, 06, 17);
    CalendarDate monday = CalendarDate.from(2006, 06, 19);
    CalendarDate actual = businessCalendar().plusBusinessDays(saturday, 0);
    Assert.assertEquals(monday, actual);

  }

  public void testMinusNonBusinessDayZero() {
    CalendarDate saturday = CalendarDate.from(2006, 06, 17);
    CalendarDate friday = CalendarDate.from(2006, 06, 16);
    CalendarDate actual = businessCalendar().minusBusinessDays(saturday, 0);
    Assert.assertEquals(friday, actual);

  }

  public void testBusinessDayReverseIterator() {
    CalendarDate friday = CalendarDate.from(2006, 06, 16);
    CalendarDate nextTuesday = CalendarDate.from(2006, 06, 20);
    CalendarInterval interval = CalendarInterval.inclusive(friday, nextTuesday);
    Iterator<CalendarDate> it = businessCalendar().businessDaysOnly(interval.daysInReverseIterator());
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(nextTuesday, it.next());
    Assert.assertTrue(it.hasNext());
    CalendarDate nextMonday = CalendarDate.from(2006, 06, 19);
    Assert.assertEquals(nextMonday, it.next());
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(friday, it.next());
    Assert.assertFalse(it.hasNext());
  }

}
