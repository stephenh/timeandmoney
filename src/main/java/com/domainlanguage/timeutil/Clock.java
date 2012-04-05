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
  private static TimeSource timeSource;
  private static TimeZone defaultTimeZone;

  public static TimeZone defaultTimeZone() {
    //There is no reasonable automatic default.
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

  public static CalendarDate today() {
    if (Clock.defaultTimeZone() == null) {
      throw new RuntimeException("CalendarDate cannot be computed without setting a default TimeZone.");
    }
    return Clock.now().calendarDate(Clock.defaultTimeZone());
  }

  public static void reset() {
    Clock.defaultTimeZone = null;
    Clock.timeSource = null;
  }
}
