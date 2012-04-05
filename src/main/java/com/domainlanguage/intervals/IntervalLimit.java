/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.intervals;

import java.io.Serializable;

class IntervalLimit<T extends Comparable<T>> implements Comparable<IntervalLimit<T>>, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private final boolean closed;
  private final T value;
  private final boolean lower;

  static <T extends Comparable<T>> IntervalLimit<T> upper(boolean closed, T value) {
    return new IntervalLimit<T>(closed, false, value);
  }

  static <T extends Comparable<T>> IntervalLimit<T> lower(boolean closed, T value) {
    return new IntervalLimit<T>(closed, true, value);
  }

  IntervalLimit(boolean closed, boolean lower, T value) {
    this.closed = closed;
    this.lower = lower;
    this.value = value;
  }

  boolean isLower() {
    return this.lower;
  }

  boolean isUpper() {
    return !this.lower;
  }

  boolean isClosed() {
    return this.closed;
  }

  boolean isOpen() {
    return !this.closed;
  }

  T getValue() {
    return this.value;
  }

  public int compareTo(IntervalLimit<T> other) {
    T otherValue = other.value;
    if (otherValue == this.value) {
      return 0;
    }
    if (this.value == null) {
      return this.lower ? -1 : 1;
    }
    if (otherValue == null) {
      return other.lower ? 1 : -1;
    }
    return this.value.compareTo(otherValue);
  }
}
