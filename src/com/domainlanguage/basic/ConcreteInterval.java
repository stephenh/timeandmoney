/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.basic;


public class ConcreteInterval extends Interval {
	private Comparable lowerLimit;
	private boolean includesLowerLimit;
	private Comparable upperLimit;
	private boolean includesUpperLimit;

	public ConcreteInterval(Comparable lower, boolean lowerIncluded, Comparable upper, boolean upperIncluded) {
//		assert lower.compareTo(upper) < 0;
		lowerLimit = lower;
		includesLowerLimit = lowerIncluded;
		upperLimit = upper;
		includesUpperLimit = upperIncluded;
	}
	

	public Comparable upperLimit() {
		return upperLimit;
	}
	public Comparable lowerLimit() {
		return lowerLimit;
	}
	public boolean includesLowerLimit() {
		return includesLowerLimit;
	}
	public boolean includesUpperLimit() {
		return includesUpperLimit;
	}

	public Interval intersect(Interval other) {
		Comparable intersectLowerBound = greaterOfLowerLimits(other);
		Comparable intersectUpperBound = lesserOfUpperLimits(other);
		if (intersectLowerBound.compareTo(intersectUpperBound) > 0) return open(intersectLowerBound, intersectLowerBound);

		return Interval.over(intersectLowerBound, greaterOfLowerIncluded(other), intersectUpperBound, lesserOfUpperIncluded(other));
	}

}
