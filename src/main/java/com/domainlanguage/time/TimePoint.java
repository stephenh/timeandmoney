/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimePoint implements Comparable<TimePoint>, Serializable {

  private static final TimeZone GMT = TimeZone.getTimeZone("Universal");
  private static final long serialVersionUID = 1L;

  final long millisecondsFromEpoc;

  public static TimePoint atMidnightGMT(int year, int month, int date) {
    return TimePoint.atMidnight(year, month, date, TimePoint.GMT);
  }

  public static TimePoint atMidnight(int year, int month, int date, TimeZone zone) {
    return TimePoint.at(year, month, date, 0, 0, 0, 0, zone);
  }

  public static TimePoint atGMT(int year, int month, int date, int hour, int minute) {
    return TimePoint.atGMT(year, month, date, hour, minute, 0, 0);
  }

  public static TimePoint atGMT(int year, int month, int date, int hour, int minute, int second) {
    return TimePoint.atGMT(year, month, date, hour, minute, second, 0);
  }

  public static TimePoint at(int year, int month, int date, int hour, int minute, int second, TimeZone zone) {
    return TimePoint.at(year, month, date, hour, minute, second, 0, zone);
  }

  public static TimePoint atGMT(int year, int month, int date, int hour, int minute, int second, int millisecond) {
    return TimePoint.at(year, month, date, hour, minute, second, millisecond, TimePoint.GMT);
  }

  public static TimePoint at12hr(int year, int month, int date, int hour, String am_pm, int minute, int second, int millisecond, TimeZone zone) {
    return TimePoint.at(year, month, date, TimePoint.convertedTo24hour(hour, am_pm), minute, second, millisecond, zone);
  }

  private static int convertedTo24hour(int hour, String am_pm) {
    int translatedAmPm = "AM".equalsIgnoreCase(am_pm) ? 0 : 12;
    translatedAmPm -= hour == 12 ? 12 : 0;
    return hour + translatedAmPm;
  }

  public static TimePoint parseGMTFrom(String dateString, String pattern) {
    return TimePoint.parseFrom(dateString, pattern, TimePoint.GMT);
  }

  public static TimePoint parseFrom(String dateString, String pattern, TimeZone zone) {
    DateFormat format = new SimpleDateFormat(pattern);
    format.setTimeZone(zone);
    Date date = format.parse(dateString, new ParsePosition(0));
    return TimePoint.from(date);
  }

  public static TimePoint from(Date javaDate) {
    return TimePoint.from(javaDate.getTime());
  }

  public static TimePoint from(Calendar calendar) {
    return TimePoint.from(calendar.getTime());
  }

  public static TimePoint from(long milliseconds) {
    TimePoint result = new TimePoint(milliseconds);
    //assert FAR_FUTURE == null || result.isBefore(FAR_FUTURE);
    //assert FAR_PAST == null || result.isAfter(FAR_PAST);
    return result;
  }

  public static TimePoint at(int year, int month, int date, int hour, int minute, int second, int millisecond, TimeZone zone) {
    Calendar calendar = Calendar.getInstance(zone);
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DATE, date);
    calendar.set(Calendar.HOUR_OF_DAY, hour);
    calendar.set(Calendar.MINUTE, minute);
    calendar.set(Calendar.SECOND, second);
    calendar.set(Calendar.MILLISECOND, millisecond);
    return TimePoint.from(calendar);
  }

  private TimePoint(long milliseconds) {
    millisecondsFromEpoc = milliseconds;
  }

  public TimePoint backToMidnight(TimeZone zone) {
    return calendarDate(zone).asTimeInterval(zone).start();
  }

  public CalendarDate calendarDate(TimeZone zone) {
    return CalendarDate.from(this, zone);
  }

  public boolean isSameDayAs(TimePoint other, TimeZone zone) {
    return calendarDate(zone).equals(other.calendarDate(zone));
  }

  public boolean isBefore(TimePoint other) {
    return millisecondsFromEpoc < other.millisecondsFromEpoc;
  }

  public boolean isAfter(TimePoint other) {
    return millisecondsFromEpoc > other.millisecondsFromEpoc;
  }

  public TimePoint nextDay() {
    return plus(Duration.days(1));
  }

  public Date asJavaUtilDate() {
    return new Date(millisecondsFromEpoc);
  }

  public Calendar asJavaCalendar(TimeZone zone) {
    Calendar result = Calendar.getInstance(zone);
    result.setTime(asJavaUtilDate());
    return result;
  }

  public Calendar asJavaCalendar() {
    return asJavaCalendar(TimePoint.GMT);
  }

  public boolean isBefore(TimeInterval interval) {
    return interval.isAfter(this);
  }

  public boolean isAfter(TimeInterval interval) {
    return interval.isBefore(this);
  }

  public TimePoint plus(Duration duration) {
    return duration.addedTo(this);
  }

  public TimePoint minus(Duration duration) {
    return duration.subtractedFrom(this);
  }

  public TimeInterval until(TimePoint end) {
    return TimeInterval.over(this, end);
  }

  @Override
  public String toString() {
    return asJavaUtilDate().toString(); //for better readability
  }

  public String toString(String pattern, TimeZone zone) {
    DateFormat format = new SimpleDateFormat(pattern);
    format.setTimeZone(zone);
    return format.format(asJavaUtilDate());
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof TimePoint) {
      TimePoint other = (TimePoint) object;
      return other.millisecondsFromEpoc == millisecondsFromEpoc;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return (int) millisecondsFromEpoc;
  }

  @Override
  public int compareTo(TimePoint otherPoint) {
    if (isBefore(otherPoint)) {
      return -1;
    }
    if (isAfter(otherPoint)) {
      return 1;
    }
    return 0;
  }

}
