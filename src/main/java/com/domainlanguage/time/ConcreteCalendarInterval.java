/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.TimeZone;

class ConcreteCalendarInterval extends CalendarInterval {

  private static final long serialVersionUID = 1L;

  static ConcreteCalendarInterval from(CalendarDate start, CalendarDate end) {
    return new ConcreteCalendarInterval(start, end);
  }

  ConcreteCalendarInterval(CalendarDate start, CalendarDate end) {
    super(start, true, end, true);
    ConcreteCalendarInterval.assertStartIsBeforeEnd(start, end);
  }

  public TimeInterval asTimeInterval(TimeZone zone) {
    TimePoint startPoint = lowerLimit().asTimeInterval(zone).start();
    TimePoint endPoint = upperLimit().asTimeInterval(zone).end();
    return TimeInterval.over(startPoint, endPoint);
  }

  private static void assertStartIsBeforeEnd(CalendarDate start, CalendarDate end) {
    if (start != null && end != null && start.compareTo(end) > 0) {
      throw new IllegalArgumentException(start + " is not before or equal to " + end);
    }
  }

}
