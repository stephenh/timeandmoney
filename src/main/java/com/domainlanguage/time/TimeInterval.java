/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Iterator;

import com.domainlanguage.intervals.Interval;
import com.domainlanguage.util.ImmutableIterator;

public class TimeInterval extends Interval<TimePoint> {

  private static final long serialVersionUID = 1L;

  public static TimeInterval over(TimePoint start, boolean closedStart, TimePoint end, boolean closedEnd) {
    return new TimeInterval(start, closedStart, end, closedEnd);
  }

  public static TimeInterval over(TimePoint start, TimePoint end) {
    //Uses the common default for time intervals, [start, end).
    return TimeInterval.over(start, true, end, false);
  }

  public static TimeInterval startingFrom(TimePoint start, boolean startClosed, Duration length, boolean endClosed) {
    TimePoint end = start.plus(length);
    return TimeInterval.over(start, startClosed, end, endClosed);
  }

  public static TimeInterval startingFrom(TimePoint start, Duration length) {
    //Uses the common default for time intervals, [start, end).
    return TimeInterval.startingFrom(start, true, length, false);
  }

  public static TimeInterval preceding(TimePoint end, boolean startClosed, Duration length, boolean endClosed) {
    TimePoint start = end.minus(length);
    return TimeInterval.over(start, startClosed, end, endClosed);
  }

  public static TimeInterval preceding(TimePoint end, Duration length) {
    //Uses the common default for time intervals, [start, end).
    return TimeInterval.preceding(end, true, length, false);
  }

  public static TimeInterval closed(TimePoint start, TimePoint end) {
    return TimeInterval.over(start, true, end, true);
  }

  public static TimeInterval open(TimePoint start, TimePoint end) {
    return TimeInterval.over(start, false, end, false);
  }

  public static TimeInterval everFrom(TimePoint start) {
    return TimeInterval.over(start, null);
  }

  public static TimeInterval everPreceding(TimePoint end) {
    return TimeInterval.over(null, end);
  }

  public TimeInterval(TimePoint start, boolean startIncluded, TimePoint end, boolean endIncluded) {
    super(start, startIncluded, end, endIncluded);
  }

  @Override
  public TimeInterval newOfSameType(TimePoint start, boolean isStartClosed, TimePoint end, boolean isEndClosed) {
    return new TimeInterval(start, isStartClosed, end, isEndClosed);
  }

  public boolean isBefore(TimePoint point) {
    return isBelow(point);
  }

  public boolean isAfter(TimePoint point) {
    return isAbove(point);
  }

  public Duration length() {
    long difference = end().millisecondsFromEpoc - start().millisecondsFromEpoc;
    return Duration.milliseconds(difference);
  }

  public Iterator<TimePoint> daysIterator() {
    return new ImmutableIterator<TimePoint>() {
      TimePoint next = TimeInterval.this.start();

      public boolean hasNext() {
        return TimeInterval.this.end().isAfter(next);
      }

      public TimePoint next() {
        TimePoint current = next;
        next = next.nextDay();
        return current;
      }
    };
  }

  public Iterator<TimeInterval> subintervalIterator(Duration subintervalLength) {
    final Duration segmentLength = subintervalLength;
    final Interval<TimePoint> totalInterval = this;
    return new ImmutableIterator<TimeInterval>() {
      TimeInterval next = segmentLength.startingFrom(TimeInterval.this.start());

      public boolean hasNext() {
        return totalInterval.covers(next);
      }

      public TimeInterval next() {
        if (!hasNext()) {
          return null;
        }
        TimeInterval current = next;
        next = segmentLength.startingFrom(next.end());
        return current;
      }
    };
  }

  public TimePoint start() {
    return lowerLimit();
  }

  public TimePoint end() {
    return upperLimit();
  }

  public TimeInterval intersect(TimeInterval interval) {
    return (TimeInterval) super.intersect(interval);
  }
}
