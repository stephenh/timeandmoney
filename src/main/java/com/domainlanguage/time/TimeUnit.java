/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.io.Serializable;
import java.util.Calendar;

public class TimeUnit implements Comparable<TimeUnit>, Serializable, TimeUnitConversionFactors {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  public static final TimeUnit millisecond = new TimeUnit(Type.millisecond, Type.millisecond, 1);
  public static final TimeUnit second = new TimeUnit(Type.second, Type.millisecond, TimeUnitConversionFactors.millisecondsPerSecond);
  public static final TimeUnit minute = new TimeUnit(Type.minute, Type.millisecond, TimeUnitConversionFactors.millisecondsPerMinute);
  public static final TimeUnit hour = new TimeUnit(Type.hour, Type.millisecond, TimeUnitConversionFactors.millisecondsPerHour);
  public static final TimeUnit day = new TimeUnit(Type.day, Type.millisecond, TimeUnitConversionFactors.millisecondsPerDay);
  public static final TimeUnit week = new TimeUnit(Type.week, Type.millisecond, TimeUnitConversionFactors.millisecondsPerWeek);
  public static final TimeUnit[] descendingMillisecondBased = {
    TimeUnit.week,
    TimeUnit.day,
    TimeUnit.hour,
    TimeUnit.minute,
    TimeUnit.second,
    TimeUnit.millisecond };
  public static final TimeUnit[] descendingMillisecondBasedForDisplay = {
    TimeUnit.day,
    TimeUnit.hour,
    TimeUnit.minute,
    TimeUnit.second,
    TimeUnit.millisecond };
  public static final TimeUnit month = new TimeUnit(Type.month, Type.month, 1);
  public static final TimeUnit quarter = new TimeUnit(Type.quarter, Type.month, TimeUnitConversionFactors.monthsPerQuarter);
  public static final TimeUnit year = new TimeUnit(Type.year, Type.month, TimeUnitConversionFactors.monthsPerYear);
  public static final TimeUnit[] descendingMonthBased = { TimeUnit.year, TimeUnit.quarter, TimeUnit.month };
  public static final TimeUnit[] descendingMonthBasedForDisplay = { TimeUnit.year, TimeUnit.month };

  private final Type type;
  private final Type baseType;
  private final int factor;

  private TimeUnit(Type type, Type baseType, int factor) {
    this.type = type;
    this.baseType = baseType;
    this.factor = factor;
  }

  TimeUnit baseUnit() {
    return baseType.equals(Type.millisecond) ? TimeUnit.millisecond : TimeUnit.month;
  }

  public boolean isConvertibleToMilliseconds() {
    return isConvertibleTo(TimeUnit.millisecond);
  }

  public boolean isConvertibleTo(TimeUnit other) {
    return baseType.equals(other.baseType);
  }

  @Override
  public int compareTo(TimeUnit other) {
    if (other.baseType.equals(baseType)) {
      return factor - other.factor;
    }
    if (baseType.equals(Type.month)) {
      return 1;
    }
    return -1;
  }

  int javaCalendarConstantForBaseType() {
    if (baseType.equals(Type.millisecond)) {
      return Calendar.MILLISECOND;
    }
    if (baseType.equals(Type.month)) {
      return Calendar.MONTH;
    }
    return 0;
  }

  @Override
  public String toString() {
    return type.name;
  }

  String toString(long quantity) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(quantity);
    buffer.append(" ");
    buffer.append(type.name);
    buffer.append(quantity == 1 ? "" : "s");
    return buffer.toString();
  }

  TimeUnit[] descendingUnits() {
    return isConvertibleToMilliseconds() ? TimeUnit.descendingMillisecondBased : TimeUnit.descendingMonthBased;
  }

  TimeUnit[] descendingUnitsForDisplay() {
    return isConvertibleToMilliseconds() ? TimeUnit.descendingMillisecondBasedForDisplay : TimeUnit.descendingMonthBasedForDisplay;
  }

  TimeUnit nextFinerUnit() {
    TimeUnit[] descending = descendingUnits();
    int index = -1;
    for (int i = 0; i < descending.length; i++) {
      if (descending[i].equals(this)) {
        index = i;
      }
    }
    if (index == descending.length - 1) {
      return null;
    }
    return descending[index + 1];
  }

  @Override
  public boolean equals(Object object) {
    //revisit: maybe use: Reflection.equalsOverClassAndNull(this, other)
    if (object == null || !(object instanceof TimeUnit)) {
      return false;
    }
    TimeUnit other = (TimeUnit) object;
    return baseType.equals(other.baseType) && factor == other.factor && type.equals(other.type);
  }

  @Override
  public int hashCode() {
    return factor + baseType.hashCode() + type.hashCode();
  }

  static class Type implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static final Type millisecond = new Type("millisecond");
    static final Type second = new Type("second");
    static final Type minute = new Type("minute");
    static final Type hour = new Type("hour");
    static final Type day = new Type("day");
    static final Type week = new Type("week");
    static final Type month = new Type("month");
    static final Type quarter = new Type("quarter");
    static final Type year = new Type("year");

    private final String name;

    Type(String name) {
      this.name = name;
    }

    public boolean equals(Object other) {
      try {
        return equals((Type) other);
      } catch (ClassCastException ex) {
        return false;
      }
    }

    public boolean equals(Type another) {
      return another != null && name.equals(another.name);
    }

    public int hashCode() {
      return name.hashCode();
    }
  }

  int getFactor() {
    return factor;
  }

  static TimeUnit exampleForPersistentMappingTesting() {
    return TimeUnit.second;
  }

  static Type exampleTypeForPersistentMappingTesting() {
    return Type.hour;
  }
}
