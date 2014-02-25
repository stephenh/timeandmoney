/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import java.util.TimeZone;

import com.domainlanguage.time.CalendarDate;
import com.domainlanguage.time.TimePoint;
import com.domainlanguage.time.TimeSource;

public class Clock {

  private static volatile TimeSource timeSource;
  private static volatile TimeZone defaultTimeZone;

  public static TimeZone defaultTimeZone() {
    if (Clock.defaultTimeZone == null) {
      Clock.defaultTimeZone = TimeZone.getDefault();
    }
    return Clock.defaultTimeZone;
  }

  public static void setDefaultTimeZone(TimeZone defaultTimeZone) {
    Clock.defaultTimeZone = defaultTimeZone;
  }

  public static TimeSource timeSource() {
    if (Clock.timeSource == null) {
      Clock.setTimeSource(SystemClock.timeSource());
    }
    return Clock.timeSource;
  }

  public static void setTimeSource(TimeSource timeSource) {
    Clock.timeSource = timeSource;
  }

  public static TimePoint now() {
    return Clock.timeSource().now();
  }

  public static CalendarDate today(TimeZone timeZone) {
    return Clock.now().calendarDate(timeZone);
  }

  public static CalendarDate today() {
    return Clock.now().calendarDate(Clock.defaultTimeZone());
  }

  public static void reset() {
    Clock.defaultTimeZone = null;
    Clock.timeSource = null;
  }
}
