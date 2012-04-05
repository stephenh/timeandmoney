/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class IntervalSequence<T extends Comparable<T>> {

  final List<Interval<T>> intervals = new ArrayList<Interval<T>>();

  public Iterator<Interval<T>> iterator() {
    return this.intervals.iterator();
  }

  public void add(Interval<T> interval) {
    this.intervals.add(interval);
    Collections.sort(this.intervals);
  }

  public boolean isEmpty() {
    return this.intervals.isEmpty();
  }

  public IntervalSequence<T> gaps() {
    IntervalSequence<T> gaps = new IntervalSequence<T>();
    if (this.intervals.size() < 2) {
      return new IntervalSequence<T>();
    }
    for (int i = 1; i < this.intervals.size(); i++) {
      Interval<T> left = this.intervals.get(i - 1);
      Interval<T> right = this.intervals.get(i);
      Interval<T> gap = left.gap(right);
      if (!gap.isEmpty()) {
        gaps.add(gap);
      }
    }
    return gaps;
  }

  public Interval<T> extent() {
    if (this.intervals.isEmpty()) {
      return null;
    }
    //TODO: Add a creation method to Interval for empty(), if it can be
    // polymorphic.
    if (this.intervals.size() == 1) {
      return this.intervals.get(0);
    }
    Interval<T> left = this.intervals.get(0);
    Interval<T> right = this.intervals.get(this.intervals.size() - 1);
    return left.newOfSameType(left.lowerLimit(), left.includesLowerLimit(), right.upperLimit(), right.includesUpperLimit());
  }

}
