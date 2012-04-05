/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.io.Serializable;
import java.util.Calendar;

import com.domainlanguage.base.Ratio;

public class Duration implements Comparable<Duration>, Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final Duration NONE = Duration.milliseconds(0);

  private final long quantity;
  private final TimeUnit unit;

  public static Duration milliseconds(long howMany) {
    return Duration.of(howMany, TimeUnit.millisecond);
  }

  public static Duration seconds(int howMany) {
    return Duration.of(howMany, TimeUnit.second);
  }

  public static Duration minutes(int howMany) {
    return Duration.of(howMany, TimeUnit.minute);
  }

  public static Duration hours(int howMany) {
    return Duration.of(howMany, TimeUnit.hour);
  }

  public static Duration days(int howMany) {
    return Duration.of(howMany, TimeUnit.day);
  }

  public static Duration daysHoursMinutesSecondsMilliseconds(int days, int hours, int minutes, int seconds, long milliseconds) {
    Duration result = Duration.days(days);
    if (hours != 0) {
      result = result.plus(Duration.hours(hours));
    }
    if (minutes != 0) {
      result = result.plus(Duration.minutes(minutes));
    }
    if (seconds != 0) {
      result = result.plus(Duration.seconds(seconds));
    }
    if (milliseconds != 0) {
      result = result.plus(Duration.milliseconds(milliseconds));
    }
    return result;
  }

  public static Duration weeks(int howMany) {
    return Duration.of(howMany, TimeUnit.week);
  }

  public static Duration months(int howMany) {
    return Duration.of(howMany, TimeUnit.month);
  }

  public static Duration quarters(int howMany) {
    return Duration.of(howMany, TimeUnit.quarter);
  }

  public static Duration years(int howMany) {
    return Duration.of(howMany, TimeUnit.year);
  }

  private static Duration of(long howMany, TimeUnit unit) {
    return new Duration(howMany, unit);
  }

  public Duration(long quantity, TimeUnit unit) {
    this.assertQuantityPositiveOrZero(quantity);
    this.quantity = quantity;
    this.unit = unit;
  }

  long inBaseUnits() {
    return this.quantity * this.unit.getFactor();
  }

  public Duration plus(Duration other) {
    this.assertConvertible(other);
    long newQuantity = this.inBaseUnits() + other.inBaseUnits();
    return new Duration(newQuantity, this.unit.baseUnit());
  }

  public Duration minus(Duration other) {
    this.assertConvertible(other);
    this.assertGreaterThanOrEqualTo(other);
    long newQuantity = this.inBaseUnits() - other.inBaseUnits();
    return new Duration(newQuantity, this.unit.baseUnit());
  }

  public TimePoint addedTo(TimePoint point) {
    return this.addAmountToTimePoint(this.inBaseUnits(), point);
  }

  public TimePoint subtractedFrom(TimePoint point) {
    return this.addAmountToTimePoint(-1 * this.inBaseUnits(), point);
  }

  public CalendarDate addedTo(CalendarDate day) {
    //		only valid for days and larger units
    if (this.unit.compareTo(TimeUnit.day) < 0) {
      return day;
    }
    Calendar calendar = day.asJavaCalendarUniversalZoneMidnight();
    if (this.unit.equals(TimeUnit.day)) {
      calendar.add(Calendar.DATE, (int) this.quantity);
    } else {
      this.addAmountToCalendar(this.inBaseUnits(), calendar);
    }
    return CalendarDate._from(calendar);
  }

  public CalendarDate subtractedFrom(CalendarDate day) {
    //		only valid for days and larger units
    if (this.unit.compareTo(TimeUnit.day) < 0) {
      return day;
    }
    Calendar calendar = day.asJavaCalendarUniversalZoneMidnight();
    if (this.unit.equals(TimeUnit.day)) {
      calendar.add(Calendar.DATE, -1 * (int) this.quantity);
    } else {
      this.subtractAmountFromCalendar(this.inBaseUnits(), calendar);
    }
    return CalendarDate._from(calendar);
  }

  public Ratio dividedBy(Duration divisor) {
    this.assertConvertible(divisor);
    return Ratio.of(this.inBaseUnits(), divisor.inBaseUnits());
  }

  public boolean equals(Object object) {
    if (!(object instanceof Duration)) {
      return false;
    }
    Duration other = (Duration) object;
    if (!this.isConvertibleTo(other)) {
      return false;
    }
    return this.inBaseUnits() == other.inBaseUnits();
  }

  public String toString() {
    return this.toNormalizedString(this.unit.descendingUnitsForDisplay());
  }

  public String toNormalizedString() {
    return this.toNormalizedString(this.unit.descendingUnits());
  }

  public TimeUnit normalizedUnit() {
    TimeUnit[] units = this.unit.descendingUnits();
    long baseAmount = this.inBaseUnits();
    for (int i = 0; i < units.length; i++) {
      TimeUnit aUnit = units[i];
      long remainder = baseAmount % aUnit.getFactor();
      if (remainder == 0) {
        return aUnit;
      }
    }
    return null;

  }

  public int hashCode() {
    return (int) this.quantity;
  }

  public int compareTo(Duration other) {
    this.assertConvertible(other);
    long difference = this.inBaseUnits() - other.inBaseUnits();
    if (difference > 0) {
      return 1;
    }
    if (difference < 0) {
      return -1;
    }
    return 0;
  }

  public TimeInterval startingFrom(TimePoint start) {
    return TimeInterval.startingFrom(start, this);
  }

  public CalendarInterval startingFrom(CalendarDate start) {
    return CalendarInterval.startingFrom(start, this);
  }

  public TimeInterval preceding(TimePoint end) {
    return TimeInterval.preceding(end, this);
  }

  public boolean isGreaterThan(Duration other) {
    assert this.unit.isConvertibleTo(other.unit);
    return this.inBaseUnits() > other.inBaseUnits();
  }

  public boolean isLessThan(Duration other) {
    assert this.unit.isConvertibleTo(other.unit);
    return this.inBaseUnits() < other.inBaseUnits();
  }

  public boolean isGreaterThanOrEqualTo(Duration other) {
    assert this.unit.isConvertibleTo(other.unit);
    return this.inBaseUnits() >= other.inBaseUnits();
  }

  public boolean isLessThanOrEqualTo(Duration other) {
    assert this.unit.isConvertibleTo(other.unit);
    return this.inBaseUnits() <= other.inBaseUnits();
  }

  TimePoint addAmountToTimePoint(long amount, TimePoint point) {
    if (this.unit.isConvertibleToMilliseconds()) {
      return TimePoint.from(amount + point.millisecondsFromEpoc);
    } else {
      Calendar calendar = point.asJavaCalendar();
      this.addAmountToCalendar(amount, calendar);
      return TimePoint.from(calendar);
    }
  }

  void addAmountToCalendar(long amount, Calendar calendar) {
    if (this.unit.isConvertibleToMilliseconds()) {
      calendar.setTimeInMillis(calendar.getTimeInMillis() + amount);
    } else {
      this.assertAmountValid(amount);
      calendar.add(this.unit.javaCalendarConstantForBaseType(), (int) amount);
    }
  }

  void subtractAmountFromCalendar(long amount, Calendar calendar) {
    this.addAmountToCalendar(-1 * amount, calendar);
  }

  private void assertConvertible(Duration other) {
    if (!other.unit.isConvertibleTo(this.unit)) {
      throw new IllegalArgumentException(other.toString() + " is not convertible to: " + this.toString());
    }
  }

  private void assertQuantityPositiveOrZero(long quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity: " + quantity + " must be zero or positive");
    }
  }

  private void assertGreaterThanOrEqualTo(Duration other) {
    if (this.compareTo(other) < 0) {
      throw new IllegalArgumentException(this + " is before " + other);
    }
  }

  private void assertAmountValid(long amount) {
    if (!(amount >= Integer.MIN_VALUE && amount <= Integer.MAX_VALUE)) {
      throw new IllegalArgumentException(amount + " is not valid");
    }
  }

  private boolean isConvertibleTo(Duration other) {
    return this.unit.isConvertibleTo(other.unit);
  }

  private String toNormalizedString(TimeUnit[] units) {
    StringBuffer buffer = new StringBuffer();
    long remainder = this.inBaseUnits();
    boolean first = true;
    for (int i = 0; i < units.length; i++) {
      TimeUnit aUnit = units[i];
      long portion = remainder / aUnit.getFactor();
      if (portion > 0) {
        if (!first) {
          buffer.append(", ");
        } else {
          first = false;
        }
        buffer.append(aUnit.toString(portion));
      }
      remainder = remainder % aUnit.getFactor();
    }
    return buffer.toString();
  }

}
