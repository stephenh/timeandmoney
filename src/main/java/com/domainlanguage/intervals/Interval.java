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

    private IntervalLimit<T> lowerLimitObject;
    private IntervalLimit<T> upperLimitObject;
    
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
        this.lowerLimitObject=lower;
        this.upperLimitObject=upper;
    }

    protected Interval(T lower, boolean isLowerClosed, T upper, boolean isUpperClosed) {
        this(IntervalLimit.lower(isLowerClosed, lower), IntervalLimit.upper(isUpperClosed, upper));
    }
    
    //Warning: This method should generally be used for display
    //purposes and interactions with closely coupled classes.
    //Look for (or add) other methods to do computations.
    public T upperLimit() {
        return upperLimitObject.getValue();
    }

    //Warning: This method should generally be used for display
    //purposes and interactions with closely coupled classes.
    //Look for (or add) other methods to do computations.

    public boolean includesUpperLimit() {
        return upperLimitObject.isClosed();
    }

   //Warning: This method should generally be used for display
   //purposes and interactions with closely coupled classes.
   //Look for (or add) other methods to do computations.
    
    public boolean hasUpperLimit() {
        return upperLimit() != null;
    }
      
    //Warning: This method should generally be used for display
    //purposes and interactions with closely coupled classes.
    //Look for (or add) other methods to do computations.
    public T lowerLimit() {
        return lowerLimitObject.getValue();
    }
    
    //Warning: This method should generally be used for display
    //purposes and interactions with closely coupled classes.
    //Look for (or add) other methods to do computations.
    public boolean includesLowerLimit() {
        return lowerLimitObject.isClosed();
    }
    
    //Warning: This method should generally be used for display
    //purposes and interactions with closely coupled classes.
    //Look for (or add) other methods to do computations.
    public boolean hasLowerLimit() {
        return lowerLimit() != null;
    }
    
    public Interval<T> newOfSameType(T lower, boolean isLowerClosed, T upper, boolean isUpperClosed) {
        return new Interval<T>(lower,isLowerClosed,upper,isUpperClosed);
    }

    public Interval<T> emptyOfSameType() {
        return newOfSameType(lowerLimit(), false, lowerLimit(), false);
    }

    public boolean includes(T value) {
        return !this.isBelow(value) && !this.isAbove(value);
    }

    public boolean covers(Interval<T> other) {
        boolean lowerPass = lowerLimit() == null
            || this.includes(other.lowerLimit())
            || (lowerLimit().compareTo(other.lowerLimit()) == 0 && !other.includesLowerLimit());
        boolean upperPass = upperLimit() == null
            || this.includes(other.upperLimit())
            || (upperLimit().compareTo(other.upperLimit()) == 0 && !other.includesUpperLimit());
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
        if (!hasUpperLimit()) return false;
        if (!hasLowerLimit()) return false;
        //An interval containing a single element, {a}.
        return upperLimit().equals(lowerLimit()) && !isEmpty();
    }

    public boolean isBelow(T value) {
        if (!hasUpperLimit()) return false;
        int comparison = upperLimit().compareTo(value);
        return comparison < 0 || (comparison == 0 && !includesUpperLimit());
    }

    public boolean isAbove(T value) {
        if (!hasLowerLimit()) return false;
        int comparison = lowerLimit().compareTo(value);
        return comparison > 0 || (comparison == 0 && !includesLowerLimit());
    }

    public int compareTo(Interval<T> other) {
        if (lowerLimit() == null) {
            return (other.lowerLimit() == null) ? 0 : -1;
        }
        if (upperLimit() == null) {
            return (other.upperLimit() == null) ? 0 : 1;
        }

        if (!upperLimit().equals(other.upperLimit()))
            return upperLimit().compareTo(other.upperLimit());
        if (includesLowerLimit() && !other.includesLowerLimit())
            return -1;
        if (!includesLowerLimit() && other.includesLowerLimit())
            return 1;
        return lowerLimit().compareTo(other.lowerLimit());
    }

    public String toString() {
        if (isEmpty())
            return "{}";
        if (isSingleElement())
            return "{" + lowerLimit().toString() + "}";

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
        if (lowerComparison <= 0)
            return this.lowerLimit();
        return other.lowerLimit();
    }

    T greaterOfLowerLimits(Interval<T> other) {
        if (lowerLimit() == null) {
            return other.lowerLimit();
        }
        int lowerComparison = lowerLimit().compareTo(other.lowerLimit());
        if (lowerComparison >= 0)
            return this.lowerLimit();
        return other.lowerLimit();
    }

    T lesserOfUpperLimits(Interval<T> other) {
        if (upperLimit() == null) {
            return other.upperLimit();
        }
        int upperComparison = upperLimit().compareTo(other.upperLimit());
        if (upperComparison <= 0)
            return this.upperLimit();
        return other.upperLimit();
    }

    private T greaterOfUpperLimits(Interval<T> other) {
        if (upperLimit() == null) {
            return null;
        }
        int upperComparison = upperLimit().compareTo(other.upperLimit());
        if (upperComparison >= 0)
            return this.upperLimit();
        return other.upperLimit();
    }

    private boolean greaterOfLowerIncludedInIntersection(Interval<T> other) {
        T limit = greaterOfLowerLimits(other);
        return this.includes(limit) && other.includes(limit);
    }

    private boolean lesserOfUpperIncludedInIntersection(Interval<T> other) {
        T limit = lesserOfUpperLimits(other);
        return this.includes(limit) && other.includes(limit);
    }

    private boolean greaterOfLowerIncludedInUnion(Interval<T> other) {
        T limit = greaterOfLowerLimits(other);
        return this.includes(limit) || other.includes(limit);
    }

    private boolean lesserOfUpperIncludedInUnion(Interval<T> other) {
        T limit = lesserOfUpperLimits(other);
        return this.includes(limit) || other.includes(limit);
    }

    public boolean equals(Object other) {
        try {
            return equals((Interval<T>)other);
        } catch(ClassCastException ex) {
            return false;
        }
    }
    public boolean equals(Interval<T> other) {
        if (other == null) return false;
        
        boolean thisEmpty = this.isEmpty();
        boolean otherEmpty = other.isEmpty();
        if (thisEmpty & otherEmpty)
            return true;
        if (thisEmpty ^ otherEmpty)
            return false;

        boolean thisSingle = this.isSingleElement();
        boolean otherSingle = other.isSingleElement();
        if (thisSingle & otherSingle)
            return this.lowerLimit().equals(other.lowerLimit());
        if (thisSingle ^ otherSingle)
            return false;

        return compareTo(other) == 0;
    }

    public int hashCode() {
        return lowerLimit().hashCode() ^ upperLimit().hashCode();
    }

    public boolean intersects(Interval<T> other) {
        int comparison = greaterOfLowerLimits(other).compareTo(lesserOfUpperLimits(other));
        if (comparison < 0)
            return true;
        if (comparison > 0)
            return false;
        return greaterOfLowerIncludedInIntersection(other) && lesserOfUpperIncludedInIntersection(other);
    }

    public Interval<T> intersect(Interval<T> other) {
        T intersectLowerBound = greaterOfLowerLimits(other);
        T intersectUpperBound = lesserOfUpperLimits(other);
        if (intersectLowerBound.compareTo(intersectUpperBound) > 0)
            return emptyOfSameType();
        return newOfSameType(intersectLowerBound, greaterOfLowerIncludedInIntersection(other), intersectUpperBound, lesserOfUpperIncludedInIntersection(other));
    }

    public Interval<T> gap(Interval<T> other) {
        if (this.intersects(other))
            return this.emptyOfSameType();

        return newOfSameType(lesserOfUpperLimits(other), !lesserOfUpperIncludedInUnion(other), greaterOfLowerLimits(other), !greaterOfLowerIncludedInUnion(other));
    }

    /** see: http://en.wikipedia.org/wiki/Set_theoretic_complement */
    public List<Interval<T>> complementRelativeTo(Interval<T> other) {
        List<Interval<T>> intervalSequence = new ArrayList<Interval<T>>();
        if (!this.intersects(other)) {
            intervalSequence.add(other);
            return intervalSequence;
        }
        Interval<T> left = leftComplementRelativeTo(other);
        if (left != null)
            intervalSequence.add(left);
        Interval<T> right = rightComplementRelativeTo(other);
        if (right != null)
            intervalSequence.add(right);
        return intervalSequence;
    }

    private Interval<T> leftComplementRelativeTo(Interval<T> other) {
        if (this.includes(lesserOfLowerLimits(other)))
            return null;
        if (lowerLimit().equals(other.lowerLimit()) && !other.includesLowerLimit())
            return null;
        return newOfSameType(other.lowerLimit(), other.includesLowerLimit(), this.lowerLimit(), !this.includesLowerLimit());
    }

    private Interval<T> rightComplementRelativeTo(Interval<T> other) {
        if (this.includes(greaterOfUpperLimits(other)))
            return null;
        if (upperLimit().equals(other.upperLimit()) && !other.includesUpperLimit())
            return null;
        return newOfSameType(this.upperLimit(), !this.includesUpperLimit(), other.upperLimit(), other.includesUpperLimit());
    }
    private void assertLowerIsLessThanOrEqualUpper(IntervalLimit<T> lower,
            IntervalLimit<T> upper) {
        if (!(lower.isLower() && upper.isUpper() && lower.compareTo(upper) <= 0)) {
            throw new IllegalArgumentException(lower + " is not before or equal to " + upper);
        }
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    protected Interval() {
    }
    
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private T getForPersistentMapping_LowerLimit() {
        return lowerLimitObject.getForPersistentMapping_Value();
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_LowerLimit(T value) {
        if (lowerLimitObject == null)
            lowerLimitObject=IntervalLimit.lower(true, value);
        lowerLimitObject.setForPersistentMapping_Value(value);
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private boolean isForPersistentMapping_IncludesLowerLimit() {
        return !lowerLimitObject.isForPersistentMapping_Closed();
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_IncludesLowerLimit(boolean value) {
        if (lowerLimitObject == null)
            lowerLimitObject=IntervalLimit.lower(value, null);
        lowerLimitObject.setForPersistentMapping_Closed(!value);
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private Comparable getForPersistentMapping_UpperLimit() {
        return upperLimitObject.getForPersistentMapping_Value();
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_UpperLimit(T value) {
        if (upperLimitObject == null)
            upperLimitObject=IntervalLimit.upper(true, value);
        upperLimitObject.setForPersistentMapping_Value(value);
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private boolean isForPersistentMapping_IncludesUpperLimit() {
        return !upperLimitObject.isForPersistentMapping_Closed();
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_IncludesUpperLimit(boolean value) {
        if (upperLimitObject == null)
            upperLimitObject=IntervalLimit.upper(value, null);
        upperLimitObject.setForPersistentMapping_Closed(!value);
    }
}
