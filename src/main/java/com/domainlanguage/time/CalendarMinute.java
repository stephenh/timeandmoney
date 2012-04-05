/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

import java.util.TimeZone;

public class CalendarMinute {
  private final CalendarDate date;
  private final TimeOfDay time;

  public static CalendarMinute dateHourAndMinute(int year, int month, int day, int hour, int minute) {
    return new CalendarMinute(CalendarDate.from(year, month, day), TimeOfDay.hourAndMinute(hour, minute));
  }

  public static CalendarMinute dateAndTimeOfDay(CalendarDate aDate, TimeOfDay aTime) {
    return new CalendarMinute(aDate, aTime);
  }

  private CalendarMinute(CalendarDate date, TimeOfDay time) {
    this.date = date;
    this.time = time;
  }

  @Override
  public String toString() {
    return date.toString() + " at " + time.toString();
  }

  @Override
  public boolean equals(Object anotherObject) {
    if (!(anotherObject instanceof CalendarMinute)) {
      return false;
    }
    return equals((CalendarMinute) anotherObject);
  }

  public boolean equals(CalendarMinute another) {
    if (another == null) {
      return false;
    }
    return date.equals(another.date) && time.equals(another.time);
  }

  @Override
  public int hashCode() {
    return date.hashCode() ^ time.hashCode();
  }

  public TimePoint asTimePoint(TimeZone timeZone) {
    return TimePoint.at(date.getYear(), date.getMonth(), date.getDay(), time.getHour(), time.getMinute(), 0, 0, timeZone);
  }

}
