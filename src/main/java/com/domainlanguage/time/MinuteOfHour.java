/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

public class MinuteOfHour {

  private final int value;

  public static MinuteOfHour value(int initial) {
    return new MinuteOfHour(initial);
  }

  private MinuteOfHour(int initial) {
    if (initial < 0 || initial > 59) {
      throw new IllegalArgumentException("Illegal value for minute: " + initial + ", please use a value between 0 and 59");
    }
    value = initial;
  }

  public boolean isAfter(MinuteOfHour another) {
    return value > another.value;
  }

  public boolean isBefore(MinuteOfHour another) {
    return value < another.value;
  }

  public int value() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof MinuteOfHour) {
      MinuteOfHour other = (MinuteOfHour) object;
      return value == other.value;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return value;
  }
}
