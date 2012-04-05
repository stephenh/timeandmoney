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

  public TimePoint asTimePoint(TimeZone timeZone) {
    return TimePoint.at(date.getYear(), date.getMonth(), date.getDay(), time.getHour(), time.getMinute(), 0, 0, timeZone);
  }

  @Override
  public String toString() {
    return date.toString() + " at " + time.toString();
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof CalendarMinute) {
      CalendarMinute other = (CalendarMinute) object;
      return date.equals(other.date) && time.equals(other.time);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return date.hashCode() ^ time.hashCode();
  }

}
