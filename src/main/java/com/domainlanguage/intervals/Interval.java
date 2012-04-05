/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The rules of this class are consistent with the common mathematical
 * definition of "interval". For a simple explanation, see
 * http://en.wikipedia.org/wiki/Interval_(mathematics)
 * 
 * Interval (and its "ConcreteInterval" subclass) can be used for any objects
 * that have a natural ordering reflected by implementing the Comparable
 * interface. For example, Integer implements Comparable, so if you want to
 * check if an Integer is within a range, make an Interval. Any class of yours
 * which implements Comparable can have intervals defined this way.
 */
public class Interval<T extends Comparable<T>> implements Comparable<Interval<T>>, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private final IntervalLimit<T> lowerLimitObject;
  private final IntervalLimit<T> upperLimitObject;

  public static <T extends Comparable<T>> Interval<T> closed(T lower, T upper) {
    return new Interval<T>(lower, true, upper, true);
  }

  public static <T extends Comparable<T>> Interval<T> open(T lower, T upper) {
    return new Interval<T>(lower, false, upper, false);
  }

  public static <T extends Comparable<T>> Interval<T> over(T lower, boolean lowerIncluded, T upper, boolean upperIncluded) {
    return new Interval<T>(lower, lowerIncluded, upper, upperIncluded);
  }

  Interval(IntervalLimit<T> lower, IntervalLimit<T> upper) {
    this.assertLowerIsLessThanOrEqualUpper(lower, upper);
    this.lowerLimitObject = lower;
    this.upperLimitObject = upper;
  }

  protected Interval(T lower, boolean isLowerClosed, T upper, boolean isUpperClosed) {
    this(IntervalLimit.lower(isLowerClosed, lower), IntervalLimit.upper(isUpperClosed, upper));
  }

  /** Returns the upper limit or null if open-ended. */
  public T upperLimit() {
    return this.upperLimitObject.getValue();
  }

  public boolean includesUpperLimit() {
    return this.upperLimitObject.isClosed();
  }

  public boolean hasUpperLimit() {
    return this.upperLimit() != null;
  }

  /** Returns the lower limit or null if open-ended. */
  public T lowerLimit() {
    return this.lowerLimitObject.getValue();
  }

  public boolean includesLowerLimit() {
    return this.lowerLimitObject.isClosed();
  }

  public boolean hasLowerLimit() {
    return this.lowerLimit() != null;
  }

  public Interval<T> newOfSameType(T lower, boolean isLowerClosed, T upper, boolean isUpperClosed) {
    return new Interval<T>(lower, isLowerClosed, upper, isUpperClosed);
  }

  public Interval<T> emptyOfSameType() {
    return this.newOfSameType(this.lowerLimit(), false, this.lowerLimit(), false);
  }

  public boolean includes(T value) {
    return !this.isBelow(value) && !this.isAbove(value);
  }

  public boolean covers(Interval<T> other) {
    boolean lowerPass = this.lowerLimit() == null
      || this.includes(other.lowerLimit())
      || (this.lowerLimit().compareTo(other.lowerLimit()) == 0 && !other.includesLowerLimit());
    boolean upperPass = this.upperLimit() == null
      || this.includes(other.upperLimit())
      || (this.upperLimit().compareTo(other.upperLimit()) == 0 && !other.includesUpperLimit());
    return lowerPass && upperPass;
  }

  public boolean within(Interval<T> other) {
    return other.covers(this);
  }

  public boolean isOpen() {
    return !this.includesLowerLimit() && !this.includesUpperLimit();
  }

  public boolean isClosed() {
    return this.includesLowerLimit() && this.includesUpperLimit();
  }

  public boolean isEmpty() {
    //TODO: Consider explicit empty interval
    //A 'degenerate' interval is an empty set, {}.
    return this.isOpen() && this.upperLimit().equals(this.lowerLimit());
  }

  public boolean isSingleElement() {
    if (!this.hasUpperLimit()) {
      return false;
    }
    if (!this.hasLowerLimit()) {
      return false;
    }
    //An interval containing a single element, {a}.
    return this.upperLimit().equals(this.lowerLimit()) && !this.isEmpty();
  }

  public boolean isBelow(T value) {
    if (!this.hasUpperLimit()) {
      return false;
    }
    int comparison = this.upperLimit().compareTo(value);
    return comparison < 0 || (comparison == 0 && !this.includesUpperLimit());
  }

  public boolean isAbove(T value) {
    if (!this.hasLowerLimit()) {
      return false;
    }
    int comparison = this.lowerLimit().compareTo(value);
    return comparison > 0 || (comparison == 0 && !this.includesLowerLimit());
  }

  public int compareTo(Interval<T> other) {
    if (this.lowerLimit() == null) {
      return (other.lowerLimit() == null) ? 0 : -1;
    }
    if (this.upperLimit() == null) {
      return (other.upperLimit() == null) ? 0 : 1;
    }

    if (!this.upperLimit().equals(other.upperLimit())) {
      return this.upperLimit().compareTo(other.upperLimit());
    }
    if (this.includesLowerLimit() && !other.includesLowerLimit()) {
      return -1;
    }
    if (!this.includesLowerLimit() && other.includesLowerLimit()) {
      return 1;
    }
    return this.lowerLimit().compareTo(other.lowerLimit());
  }

  public String toString() {
    if (this.isEmpty()) {
      return "{}";
    }
    if (this.isSingleElement()) {
      return "{" + this.lowerLimit().toString() + "}";
    }

    StringBuffer buffer = new StringBuffer();

    buffer.append(this.includesLowerLimit() ? "[" : "(");
    buffer.append(this.hasLowerLimit() ? this.lowerLimit().toString() : "Infinity");
    buffer.append(", ");
    buffer.append(this.hasUpperLimit() ? this.upperLimit().toString() : "Infinity");
    buffer.append(this.includesUpperLimit() ? "]" : ")");

    return buffer.toString();
  }

  private T lesserOfLowerLimits(Interval<T> other) {
    if (this.lowerLimit() == null) {
      return null;
    }
    int lowerComparison = this.lowerLimit().compareTo(other.lowerLimit());
    if (lowerComparison <= 0) {
      return this.lowerLimit();
    }
    return other.lowerLimit();
  }

  T greaterOfLowerLimits(Interval<T> other) {
    if (this.lowerLimit() == null) {
      return other.lowerLimit();
    }
    int lowerComparison = this.lowerLimit().compareTo(other.lowerLimit());
    if (lowerComparison >= 0) {
      return this.lowerLimit();
    }
    return other.lowerLimit();
  }

  T lesserOfUpperLimits(Interval<T> other) {
    if (this.upperLimit() == null) {
      return other.upperLimit();
    }
    int upperComparison = this.upperLimit().compareTo(other.upperLimit());
    if (upperComparison <= 0) {
      return this.upperLimit();
    }
    return other.upperLimit();
  }

  private T greaterOfUpperLimits(Interval<T> other) {
    if (this.upperLimit() == null) {
      return null;
    }
    int upperComparison = this.upperLimit().compareTo(other.upperLimit());
    if (upperComparison >= 0) {
      return this.upperLimit();
    }
    return other.upperLimit();
  }

  private boolean greaterOfLowerIncludedInIntersection(Interval<T> other) {
    T limit = this.greaterOfLowerLimits(other);
    return this.includes(limit) && other.includes(limit);
  }

  private boolean lesserOfUpperIncludedInIntersection(Interval<T> other) {
    T limit = this.lesserOfUpperLimits(other);
    return this.includes(limit) && other.includes(limit);
  }

  private boolean greaterOfLowerIncludedInUnion(Interval<T> other) {
    T limit = this.greaterOfLowerLimits(other);
    return this.includes(limit) || other.includes(limit);
  }

  private boolean lesserOfUpperIncludedInUnion(Interval<T> other) {
    T limit = this.lesserOfUpperLimits(other);
    return this.includes(limit) || other.includes(limit);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Interval<?>)) {
      return false;
    }
    @SuppressWarnings("unchecked")
    Interval<T> other = (Interval<T>) object;

    boolean thisEmpty = this.isEmpty();
    boolean otherEmpty = other.isEmpty();
    if (thisEmpty & otherEmpty) {
      return true;
    }
    if (thisEmpty ^ otherEmpty) {
      return false;
    }

    boolean thisSingle = this.isSingleElement();
    boolean otherSingle = other.isSingleElement();
    if (thisSingle & otherSingle) {
      return this.lowerLimit().equals(other.lowerLimit());
    }
    if (thisSingle ^ otherSingle) {
      return false;
    }

    return this.compareTo(other) == 0;
  }

  public int hashCode() {
    return this.lowerLimit().hashCode() ^ this.upperLimit().hashCode();
  }

  public boolean intersects(Interval<T> other) {
    int comparison = this.greaterOfLowerLimits(other).compareTo(this.lesserOfUpperLimits(other));
    if (comparison < 0) {
      return true;
    }
    if (comparison > 0) {
      return false;
    }
    return this.greaterOfLowerIncludedInIntersection(other) && this.lesserOfUpperIncludedInIntersection(other);
  }

  public Interval<T> intersect(Interval<T> other) {
    T intersectLowerBound = this.greaterOfLowerLimits(other);
    T intersectUpperBound = this.lesserOfUpperLimits(other);
    if (intersectLowerBound.compareTo(intersectUpperBound) > 0) {
      return this.emptyOfSameType();
    }
    return this.newOfSameType(
      intersectLowerBound,
      this.greaterOfLowerIncludedInIntersection(other),
      intersectUpperBound,
      this.lesserOfUpperIncludedInIntersection(other));
  }

  public Interval<T> gap(Interval<T> other) {
    if (this.intersects(other)) {
      return this.emptyOfSameType();
    }

    return this.newOfSameType(
      this.lesserOfUpperLimits(other),
      !this.lesserOfUpperIncludedInUnion(other),
      this.greaterOfLowerLimits(other),
      !this.greaterOfLowerIncludedInUnion(other));
  }

  /** see: http://en.wikipedia.org/wiki/Set_theoretic_complement */
  public List<Interval<T>> complementRelativeTo(Interval<T> other) {
    List<Interval<T>> intervalSequence = new ArrayList<Interval<T>>();
    if (!this.intersects(other)) {
      intervalSequence.add(other);
      return intervalSequence;
    }
    Interval<T> left = this.leftComplementRelativeTo(other);
    if (left != null) {
      intervalSequence.add(left);
    }
    Interval<T> right = this.rightComplementRelativeTo(other);
    if (right != null) {
      intervalSequence.add(right);
    }
    return intervalSequence;
  }

  private Interval<T> leftComplementRelativeTo(Interval<T> other) {
    if (this.includes(this.lesserOfLowerLimits(other))) {
      return null;
    }
    if (this.lowerLimit().equals(other.lowerLimit()) && !other.includesLowerLimit()) {
      return null;
    }
    return this.newOfSameType(other.lowerLimit(), other.includesLowerLimit(), this.lowerLimit(), !this.includesLowerLimit());
  }

  private Interval<T> rightComplementRelativeTo(Interval<T> other) {
    if (this.includes(this.greaterOfUpperLimits(other))) {
      return null;
    }
    if (this.upperLimit().equals(other.upperLimit()) && !other.includesUpperLimit()) {
      return null;
    }
    return this.newOfSameType(this.upperLimit(), !this.includesUpperLimit(), other.upperLimit(), other.includesUpperLimit());
  }

  private void assertLowerIsLessThanOrEqualUpper(IntervalLimit<T> lower, IntervalLimit<T> upper) {
    if (!(lower.isLower() && upper.isUpper() && lower.compareTo(upper) <= 0)) {
      throw new IllegalArgumentException(lower + " is not before or equal to " + upper);
    }
  }

}
