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

  public boolean isAfter(TimeOfDay another) {
    return hour.isAfter(another.hour) || hour.equals(another) && minute.isAfter(another.minute);
  }

  public boolean isBefore(TimeOfDay another) {
    return hour.isBefore(another.hour) || hour.equals(another) && minute.isBefore(another.minute);
  }

  public TimePoint asTimePointGiven(CalendarDate date, TimeZone timeZone) {
    CalendarMinute timeOfDayOnDate = on(date);
    return timeOfDayOnDate.asTimePoint(timeZone);
  }

  @Override
  public String toString() {
    return hour.toString() + ":" + minute.toString();
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof TimeOfDay) {
      TimeOfDay other = (TimeOfDay) object;
      return hour.equals(other.hour) && minute.equals(other.minute);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return hour.hashCode() ^ minute.hashCode();
  }

  int getHour() {
    return hour.value();
  }

  int getMinute() {
    return minute.value();
  }
}
