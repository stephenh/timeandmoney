/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;

import java.util.*;

public class IntervalSequence<T extends Comparable<T>> {

    List<Interval<T>> intervals;

    public IntervalSequence() {
        intervals = new ArrayList<Interval<T>>();
    }
    
    public Iterator<Interval<T>> iterator() {
        return intervals.iterator();
    }

    public void add(Interval<T> interval) {
        intervals.add(interval);
        Collections.sort(intervals); 
    }

    public boolean isEmpty() {
        return intervals.isEmpty();
    }

    public IntervalSequence<T> gaps() {
        IntervalSequence<T> gaps = new IntervalSequence<T>();
        if (intervals.size() < 2)
            return new IntervalSequence<T>();
        for (int i = 1; i < intervals.size(); i++) {
            Interval<T> left = intervals.get(i - 1);
            Interval<T> right = intervals.get(i);
            Interval<T> gap = left.gap(right);
            if (!gap.isEmpty())
                gaps.add(gap);
        }
        return gaps;
    }

    public Interval<T> extent() {
        if (intervals.isEmpty())
            return null;
        //TODO: Add a creation method to Interval for empty(), if it can be
        // polymorphic.
        if (intervals.size() == 1)
            return intervals.get(0);
        Interval<T> left = intervals.get(0);
        Interval<T> right = intervals.get(intervals.size() - 1);
        return left.newOfSameType(left.lowerLimit(), left.includesLowerLimit(), right.upperLimit(), right.includesUpperLimit());
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private List getForPersistentMapping_Intervals() {
        return intervals;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Intervals(List intervals) {
        this.intervals = intervals;
    }
}