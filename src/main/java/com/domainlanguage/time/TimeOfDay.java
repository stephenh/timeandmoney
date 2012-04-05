/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

import java.util.TimeZone;

public class TimeOfDay {
  private final HourOfDay hour;
  private final MinuteOfHour minute;

  public static TimeOfDay hourAndMinute(int hour, int minute) {
    return new TimeOfDay(hour, minute);
  }

  private TimeOfDay(int hour, int minute) {
    this.hour = HourOfDay.value(hour);
    this.minute = MinuteOfHour.value(minute);
  }

  public CalendarMinute on(CalendarDate date) {
    return CalendarMinute.dateAndTimeOfDay(date, this);
  }

  public String toString() {
    return this.hour.toString() + ":" + this.minute.toString();
  }

  public boolean equals(Object anotherObject) {
    if (!(anotherObject instanceof TimeOfDay)) {
      return false;
    }
    return this.equals((TimeOfDay) anotherObject);
  }

  public boolean equals(TimeOfDay another) {
    if (another == null) {
      return false;
    }
    return this.hour.equals(another.hour) && this.minute.equals(another.minute);
  }

  public int hashCode() {
    return this.hour.hashCode() ^ this.minute.hashCode();
  }

  public boolean isAfter(TimeOfDay another) {
    return this.hour.isAfter(another.hour) || this.hour.equals(another) && this.minute.isAfter(another.minute);
  }

  public boolean isBefore(TimeOfDay another) {
    return this.hour.isBefore(another.hour) || this.hour.equals(another) && this.minute.isBefore(another.minute);
  }

  int getHour() {
    return this.hour.value();
  }

  int getMinute() {
    return this.minute.value();
  }

  public TimePoint asTimePointGiven(CalendarDate date, TimeZone timeZone) {
    CalendarMinute timeOfDayOnDate = this.on(date);
    return timeOfDayOnDate.asTimePoint(timeZone);
  }
}
