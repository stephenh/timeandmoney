/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.basic;

import java.io.Serializable;

/**
 * The rules of this class are consistent with the common mathematical 
 * definition of "interval". For a simple explanation, see 
 * http://en.wikipedia.org/wiki/Interval_(mathematics)
 */

public abstract class ComparableInterval implements Comparable, Serializable {
	
	public static ConcreteComparableInterval closed(Comparable lower, Comparable upper) {
		return new ConcreteComparableInterval(lower, true, upper, true);
	}

	public static ConcreteComparableInterval open(Comparable lower, Comparable upper) {
		return new ConcreteComparableInterval(lower, false, upper, false);
	}

	public static ConcreteComparableInterval over(Comparable lower, boolean lowerIncluded, Comparable upper, boolean upperIncluded) {
		return new ConcreteComparableInterval(lower, lowerIncluded, upper, upperIncluded);
	}

	
	public abstract Comparable upperLimit();
		//Warning: This method should generally be used for display
		//purposes and interactions with closely coupled classes.
		//Look for (or add) other methods to do computations.
	public abstract Comparable lowerLimit();
		//Warning: This method should generally be used for display
		//purposes and interactions with closely coupled classes.
		//Look for (or add) other methods to do computations.
	public abstract boolean includesLowerLimit();
		//Warning: This method should generally be used for display
		//purposes and interactions with closely coupled classes.
		//Look for (or add) other methods to do computations.
	public abstract boolean includesUpperLimit();
		//Warning: This method should generally be used for display
		//purposes and interactions with closely coupled classes.
		//Look for (or add) other methods to do computations.
	

	public boolean intersects(ComparableInterval other) {
		int comparison = greaterOfLowerLimits(other).compareTo(lesserOfUpperLimits(other));
		if (comparison < 0) return true;
		if (comparison > 0) return false;
		return greaterOfLowerIncluded(other) && lesserOfUpperIncluded(other);
	}

	public boolean includes(Comparable value) {
		return !this.isBelow(value) && !this.isAbove(value);
	}

	public boolean includes(ComparableInterval other) {
		int lowerComparison = lowerLimit().compareTo(other.lowerLimit());
			boolean lowerPass = this.includes(other.lowerLimit()) ||
				(lowerComparison == 0 && !other.includesLowerLimit());

		int upperComparison = upperLimit().compareTo(other.upperLimit());
			boolean upperPass = this.includes(other.upperLimit()) ||
				(upperComparison == 0 && !other.includesUpperLimit());
			
		return lowerPass && upperPass;
	
	}
	
	public boolean isOpen() {
		return !includesLowerLimit() && !includesUpperLimit();
	}

	public boolean isClosed() {
		return includesLowerLimit() && includesUpperLimit();
	}
	
	public boolean isEmpty() {
		//Really a 'degenerate' interval, but the behavior 
		//of the interval will be like an empty set.
		return isOpen() && upperLimit().equals(lowerLimit());
		
	}
	
	public boolean isBelow(Comparable value) {
		int comparison = upperLimit().compareTo(value);
		return comparison < 0 ||
			(comparison == 0 && !includesUpperLimit());
	}

	public boolean isAbove(Comparable value) {
		int comparison = lowerLimit().compareTo(value);
		return comparison > 0 ||
			(comparison == 0 && !includesLowerLimit());
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(includesLowerLimit() ? "[" : "(");
		buffer.append(lowerLimit().toString());
		buffer.append(", ");
		buffer.append(upperLimit().toString());
		buffer.append(includesUpperLimit() ? "]" : ")");
		return buffer.toString();
	}

	public int compareTo(Object arg) {
		ComparableInterval other = (ComparableInterval) arg;
		if (!upperLimit().equals(other.upperLimit())) return upperLimit().compareTo(other.upperLimit());
		if (includesLowerLimit() && !other.includesLowerLimit()) return -1;
		if (!includesLowerLimit() && other.includesLowerLimit()) return 1;
		return lowerLimit().compareTo(other.lowerLimit());
	}

	/**
	 * The remaining methods are not meant for use by clients.
	 * They are public only for use by extentions (subclasses).
	 */
	
	public Comparable greaterOfLowerLimits(ComparableInterval other) {
		int lowerComparison = lowerLimit().compareTo(other.lowerLimit());
		if (lowerComparison >= 0) return this.lowerLimit();
		return other.lowerLimit();
	}

	public boolean greaterOfLowerIncluded(ComparableInterval other) {
		int lowerComparison = lowerLimit().compareTo(other.lowerLimit());
		if (lowerComparison > 0) return this.includesLowerLimit();
		if (lowerComparison < 0) return other.includesLowerLimit();
		return this.includesLowerLimit() && other.includesLowerLimit();
	}

	public Comparable lesserOfUpperLimits(ComparableInterval other) {
		int upperComparison = upperLimit().compareTo(other.upperLimit());
		if (upperComparison <= 0) return this.upperLimit();
		return other.upperLimit();
	}

	public boolean lesserOfUpperIncluded(ComparableInterval other) {
		int upperComparison = upperLimit().compareTo(other.upperLimit());
		if (upperComparison < 0) return this.includesUpperLimit();
		if (upperComparison > 0) return other.includesUpperLimit();
		return this.includesUpperLimit() && other.includesUpperLimit();
	}

}
