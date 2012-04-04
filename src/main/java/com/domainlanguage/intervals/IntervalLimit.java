/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.intervals;

import java.io.*;

class IntervalLimit<T extends Comparable<T>> implements Comparable<IntervalLimit<T>>, Serializable {
    
    private boolean closed;
    private T value;
    private boolean lower;
    
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
        return lower;
    }
    boolean isUpper() {
        return !lower;
    }
    boolean isClosed() {
        return closed;
    }
    boolean isOpen() {
        return !closed;
    }
    T getValue() {
        return value;
    }
    
    public int compareTo(IntervalLimit<T> other) {
        T otherValue=other.value;
        if (otherValue == value) return 0;
        if (value == null) {
            return lower ? -1 : 1;
        }
        if (otherValue == null) {
            return other.lower ? 1 : -1;
        }
        return value.compareTo(otherValue);
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    IntervalLimit() {}
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    boolean isForPersistentMapping_Closed() {
        return closed;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    void setForPersistentMapping_Closed(boolean closed) {
        this.closed = closed;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    boolean isForPersistentMapping_Lower() {
        return lower;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    void setForPersistentMapping_Lower(boolean lower) {
        this.lower = lower;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    T getForPersistentMapping_Value() {
        return value;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    void setForPersistentMapping_Value(T value) {
        this.value = value;
    }
}
