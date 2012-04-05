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
    assertLowerIsLessThanOrEqualUpper(lower, upper);
    lowerLimitObject = lower;
    upperLimitObject = upper;
  }

  protected Interval(T lower, boolean isLowerClosed, T upper, boolean isUpperClosed) {
    this(IntervalLimit.lower(isLowerClosed, lower), IntervalLimit.upper(isUpperClosed, upper));
  }

  /** Returns the upper limit or null if open-ended. */
  public T upperLimit() {
    return upperLimitObject.getValue();
  }

  public boolean includesUpperLimit() {
    return upperLimitObject.isClosed();
  }

  public boolean hasUpperLimit() {
    return upperLimit() != null;
  }

  /** Returns the lower limit or null if open-ended. */
  public T lowerLimit() {
    return lowerLimitObject.getValue();
  }

  public boolean includesLowerLimit() {
    return lowerLimitObject.isClosed();
  }

  public boolean hasLowerLimit() {
    return lowerLimit() != null;
  }

  public Interval<T> newOfSameType(T lower, boolean isLowerClosed, T upper, boolean isUpperClosed) {
    return new Interval<T>(lower, isLowerClosed, upper, isUpperClosed);
  }

  public Interval<T> emptyOfSameType() {
    return newOfSameType(lowerLimit(), false, lowerLimit(), false);
  }

  public boolean includes(T value) {
    return !isBelow(value) && !isAbove(value);
  }

  public boolean covers(Interval<T> other) {
    boolean lowerPass = lowerLimit() == null
      || includes(other.lowerLimit())
      || lowerLimit().compareTo(other.lowerLimit()) == 0
      && !other.includesLowerLimit();
    boolean upperPass = upperLimit() == null
      || includes(other.upperLimit())
      || upperLimit().compareTo(other.upperLimit()) == 0
      && !other.includesUpperLimit();
    return lowerPass && upperPass;
  }

  public boolean within(Interval<T> other) {
    return other.covers(this);
  }

  public boolean isOpen() {
    return !includesLowerLimit() && !includesUpperLimit();
  }

  public boolean isClosed() {
    return includesLowerLimit() && includesUpperLimit();
  }

  public boolean isEmpty() {
    //TODO: Consider explicit empty interval
    //A 'degenerate' interval is an empty set, {}.
    return isOpen() && upperLimit().equals(lowerLimit());
  }

  public boolean isSingleElement() {
    if (!hasUpperLimit()) {
      return false;
    }
    if (!hasLowerLimit()) {
      return false;
    }
    //An interval containing a single element, {a}.
    return upperLimit().equals(lowerLimit()) && !isEmpty();
  }

  public boolean isBelow(T value) {
    if (!hasUpperLimit()) {
      return false;
    }
    int comparison = upperLimit().compareTo(value);
    return comparison < 0 || comparison == 0 && !includesUpperLimit();
  }

  public boolean isAbove(T value) {
    if (!hasLowerLimit()) {
      return false;
    }
    int comparison = lowerLimit().compareTo(value);
    return comparison > 0 || comparison == 0 && !includesLowerLimit();
  }

  @Override
  public int compareTo(Interval<T> other) {
    if (lowerLimit() == null) {
      return other.lowerLimit() == null ? 0 : -1;
    }
    if (upperLimit() == null) {
      return other.upperLimit() == null ? 0 : 1;
    }

    if (!upperLimit().equals(other.upperLimit())) {
      return upperLimit().compareTo(other.upperLimit());
    }
    if (includesLowerLimit() && !other.includesLowerLimit()) {
      return -1;
    }
    if (!includesLowerLimit() && other.includesLowerLimit()) {
      return 1;
    }
    return lowerLimit().compareTo(other.lowerLimit());
  }

  public boolean intersects(Interval<T> other) {
    int comparison = greaterOfLowerLimits(other).compareTo(lesserOfUpperLimits(other));
    if (comparison < 0) {
      return true;
    }
    if (comparison > 0) {
      return false;
    }
    return greaterOfLowerIncludedInIntersection(other) && lesserOfUpperIncludedInIntersection(other);
  }

  public Interval<T> intersect(Interval<T> other) {
    T intersectLowerBound = greaterOfLowerLimits(other);
    T intersectUpperBound = lesserOfUpperLimits(other);
    if (intersectLowerBound.compareTo(intersectUpperBound) > 0) {
      return emptyOfSameType();
    }
    return newOfSameType(
      intersectLowerBound,
      greaterOfLowerIncludedInIntersection(other),
      intersectUpperBound,
      lesserOfUpperIncludedInIntersection(other));
  }

  public Interval<T> gap(Interval<T> other) {
    if (intersects(other)) {
      return emptyOfSameType();
    }
    return newOfSameType(
      lesserOfUpperLimits(other),
      !lesserOfUpperIncludedInUnion(other),
      greaterOfLowerLimits(other),
      !greaterOfLowerIncludedInUnion(other));
  }

  /** see: http://en.wikipedia.org/wiki/Set_theoretic_complement */
  public List<Interval<T>> complementRelativeTo(Interval<T> other) {
    List<Interval<T>> intervalSequence = new ArrayList<Interval<T>>();
    if (!intersects(other)) {
      intervalSequence.add(other);
      return intervalSequence;
    }
    Interval<T> left = leftComplementRelativeTo(other);
    if (left != null) {
      intervalSequence.add(left);
    }
    Interval<T> right = rightComplementRelativeTo(other);
    if (right != null) {
      intervalSequence.add(right);
    }
    return intervalSequence;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Interval<?>)) {
      return false;
    }
    @SuppressWarnings("unchecked")
    Interval<T> other = (Interval<T>) object;
    boolean thisEmpty = isEmpty();
    boolean otherEmpty = other.isEmpty();
    if (thisEmpty & otherEmpty) {
      return true;
    }
    if (thisEmpty ^ otherEmpty) {
      return false;
    }
    boolean thisSingle = isSingleElement();
    boolean otherSingle = other.isSingleElement();
    if (thisSingle & otherSingle) {
      return lowerLimit().equals(other.lowerLimit());
    }
    if (thisSingle ^ otherSingle) {
      return false;
    }
    return compareTo(other) == 0;
  }

  @Override
  public int hashCode() {
    return lowerLimit().hashCode() ^ upperLimit().hashCode();
  }

  @Override
  public String toString() {
    if (isEmpty()) {
      return "{}";
    }
    if (isSingleElement()) {
      return "{" + lowerLimit().toString() + "}";
    }
    StringBuffer buffer = new StringBuffer();
    buffer.append(includesLowerLimit() ? "[" : "(");
    buffer.append(hasLowerLimit() ? lowerLimit().toString() : "Infinity");
    buffer.append(", ");
    buffer.append(hasUpperLimit() ? upperLimit().toString() : "Infinity");
    buffer.append(includesUpperLimit() ? "]" : ")");
    return buffer.toString();
  }

  private T lesserOfLowerLimits(Interval<T> other) {
    if (lowerLimit() == null) {
      return null;
    }
    int lowerComparison = lowerLimit().compareTo(other.lowerLimit());
    if (lowerComparison <= 0) {
      return lowerLimit();
    }
    return other.lowerLimit();
  }

  private Interval<T> leftComplementRelativeTo(Interval<T> other) {
    if (includes(lesserOfLowerLimits(other))) {
      return null;
    }
    if (lowerLimit().equals(other.lowerLimit()) && !other.includesLowerLimit()) {
      return null;
    }
    return newOfSameType(other.lowerLimit(), other.includesLowerLimit(), lowerLimit(), !includesLowerLimit());
  }

  private Interval<T> rightComplementRelativeTo(Interval<T> other) {
    if (includes(greaterOfUpperLimits(other))) {
      return null;
    }
    if (upperLimit().equals(other.upperLimit()) && !other.includesUpperLimit()) {
      return null;
    }
    return newOfSameType(upperLimit(), !includesUpperLimit(), other.upperLimit(), other.includesUpperLimit());
  }

  private void assertLowerIsLessThanOrEqualUpper(IntervalLimit<T> lower, IntervalLimit<T> upper) {
    if (!(lower.isLower() && upper.isUpper() && lower.compareTo(upper) <= 0)) {
      throw new IllegalArgumentException(lower + " is not before or equal to " + upper);
    }
  }

  T greaterOfLowerLimits(Interval<T> other) {
    if (lowerLimit() == null) {
      return other.lowerLimit();
    }
    int lowerComparison = lowerLimit().compareTo(other.lowerLimit());
    if (lowerComparison >= 0) {
      return lowerLimit();
    }
    return other.lowerLimit();
  }

  T lesserOfUpperLimits(Interval<T> other) {
    if (upperLimit() == null) {
      return other.upperLimit();
    }
    int upperComparison = upperLimit().compareTo(other.upperLimit());
    if (upperComparison <= 0) {
      return upperLimit();
    }
    return other.upperLimit();
  }

  private T greaterOfUpperLimits(Interval<T> other) {
    if (upperLimit() == null) {
      return null;
    }
    int upperComparison = upperLimit().compareTo(other.upperLimit());
    if (upperComparison >= 0) {
      return upperLimit();
    }
    return other.upperLimit();
  }

  private boolean greaterOfLowerIncludedInIntersection(Interval<T> other) {
    T limit = greaterOfLowerLimits(other);
    return includes(limit) && other.includes(limit);
  }

  private boolean lesserOfUpperIncludedInIntersection(Interval<T> other) {
    T limit = lesserOfUpperLimits(other);
    return includes(limit) && other.includes(limit);
  }

  private boolean greaterOfLowerIncludedInUnion(Interval<T> other) {
    T limit = greaterOfLowerLimits(other);
    return includes(limit) || other.includes(limit);
  }

  private boolean lesserOfUpperIncludedInUnion(Interval<T> other) {
    T limit = lesserOfUpperLimits(other);
    return includes(limit) || other.includes(limit);
  }

}
