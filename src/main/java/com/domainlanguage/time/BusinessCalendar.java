/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.domainlanguage.util.ImmutableIterator;

public class BusinessCalendar {

  private final Set<CalendarDate> holidays;

  /** Should be rewritten for each particular organization */
  static Set<CalendarDate> defaultHolidays() {
    return new HashSet<CalendarDate>();
  }

  public BusinessCalendar() {
    holidays = BusinessCalendar.defaultHolidays();
  }

  public void addHolidays(Set<CalendarDate> days) {
    holidays.addAll(days);
  }

  public int getElapsedBusinessDays(CalendarInterval interval) {
    int tally = 0;
    Iterator<CalendarDate> iterator = businessDaysOnly(interval.daysIterator());
    while (iterator.hasNext()) {
      iterator.next();
      tally += 1;
    }
    return tally;
  }

  /*
   * @deprecated
   */
  public CalendarDate nearestBusinessDay(CalendarDate day) {
    if (isBusinessDay(day)) {
      return day;
    } else {
      return nextBusinessDay(day);
    }
  }

  public boolean isHoliday(CalendarDate day) {
    return holidays.contains(day);
  }

  public boolean isWeekend(CalendarDate day) {
    Calendar calday = day.asJavaCalendarUniversalZoneMidnight();
    return calday.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calday.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
  }

  public boolean isBusinessDay(CalendarDate day) {
    return !isWeekend(day) && !isHoliday(day);
  }

  public Iterator<CalendarDate> businessDaysOnly(final Iterator<CalendarDate> calendarDays) {
    return new ImmutableIterator<CalendarDate>() {
      CalendarDate lookAhead = null;
      {
        next();
      }

      public boolean hasNext() {
        return lookAhead != null;
      }

      public CalendarDate next() {
        CalendarDate next = lookAhead;
        lookAhead = nextBusinessDate();
        return next;
      }

      private CalendarDate nextBusinessDate() {
        CalendarDate result = null;
        do {
          result = calendarDays.hasNext() ? (CalendarDate) calendarDays.next() : null;
        } while (!(result == null || BusinessCalendar.this.isBusinessDay(result)));
        return result;
      }
    };
  }

  public CalendarDate plusBusinessDays(CalendarDate startDate, int numberOfDays) {
    if (numberOfDays < 0) {
      throw new IllegalArgumentException("Negative numberOfDays not supported");
    }
    Iterator<CalendarDate> iterator = CalendarInterval.everFrom(startDate).daysIterator();
    return nextNumberOfBusinessDays(numberOfDays, iterator);
  }

  public CalendarDate minusBusinessDays(CalendarDate startDate, int numberOfDays) {
    if (numberOfDays < 0) {
      throw new IllegalArgumentException("Negative numberOfDays not supported");
    }
    Iterator<CalendarDate> iterator = CalendarInterval.everPreceding(startDate).daysInReverseIterator();
    return nextNumberOfBusinessDays(numberOfDays, iterator);
  }

  public CalendarDate nextBusinessDay(CalendarDate startDate) {
    if (isBusinessDay(startDate)) {
      return plusBusinessDays(startDate, 1);
    } else {
      return plusBusinessDays(startDate, 0);
    }
  }

  private CalendarDate nextNumberOfBusinessDays(int numberOfDays, Iterator<CalendarDate> calendarDays) {
    Iterator<CalendarDate> businessDays = businessDaysOnly(calendarDays);
    CalendarDate result = null;
    for (int i = 0; i <= numberOfDays; i++) {
      result = businessDays.next();
    }
    return result;
  }

}
