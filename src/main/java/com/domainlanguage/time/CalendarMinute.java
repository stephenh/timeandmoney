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
    super();
    this.date = date;
    this.time = time;
  }

  public String toString() {
    return this.date.toString() + " at " + this.time.toString();
  }

  public boolean equals(Object anotherObject) {
    if (!(anotherObject instanceof CalendarMinute)) {
      return false;
    }
    return this.equals((CalendarMinute) anotherObject);
  }

  public boolean equals(CalendarMinute another) {
    if (another == null) {
      return false;
    }
    return this.date.equals(another.date) && this.time.equals(another.time);
  }

  public int hashCode() {
    return this.date.hashCode() ^ this.time.hashCode();
  }

  public TimePoint asTimePoint(TimeZone timeZone) {
    return TimePoint.at(this.date.getYear(), this.date.getMonth(), this.date.getDay(), this.time.getHour(), this.time.getMinute(), 0, 0, timeZone);
  }

}
