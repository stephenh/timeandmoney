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

  static ConcreteCalendarInterval from(CalendarDate start, boolean startClosed, CalendarDate end, boolean endClosed) {
    // if we can, convert an open "2000-01-02)" to a closed "2000-01-01]" since that is more natural for calendar intervals
    if (!endClosed && end != null && end.isAfter(start)) {
      return new ConcreteCalendarInterval(start, startClosed, end.plusDays(-1), true);
    } else {
      return new ConcreteCalendarInterval(start, startClosed, end, endClosed);
    }
  }

  private ConcreteCalendarInterval(CalendarDate start, CalendarDate end) {
    super(start, start != null, end, end != null);
    ConcreteCalendarInterval.assertStartIsBeforeEnd(start, end);
  }

  private ConcreteCalendarInterval(CalendarDate start, boolean startClosed, CalendarDate end, boolean endClosed) {
    super(start, startClosed, end, endClosed);
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
