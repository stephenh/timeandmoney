/**
 *
 * This code is provided under the following licence: 
 * MIT Licence
 * timeandmoney.sourceforge.net
 * Copyright 2004, Domain Language, Inc.
 */

package fundamental;


public class ConcreteComparableInterval extends ComparableInterval {
	private Comparable lowerLimit;
	private boolean includesLowerLimit;
	private Comparable upperLimit;
	private boolean includesUpperLimit;

	public ConcreteComparableInterval(Comparable lower, boolean lowerIncluded, Comparable upper, boolean upperIncluded) {
//		assert lower.compareTo(upper) < 0;
		lowerLimit = lower;
		includesLowerLimit = lowerIncluded;
		upperLimit = upper;
		includesUpperLimit = upperIncluded;
	}
	

	public Comparable upperLimit() {
		return lowerLimit;
	}
	public Comparable lowerLimit() {
		return upperLimit;
	}
	public boolean includesLowerLimit() {
		return includesLowerLimit;
	}
	public boolean includesUpperLimit() {
		return includesUpperLimit;
	}

	public ComparableInterval intersect(ComparableInterval other) {
		Comparable intersectLowerBound = greaterOfLowerLimits(other);
		Comparable intersectUpperBound = lesserOfUpperLimits(other);
		if (intersectLowerBound.compareTo(intersectUpperBound) > 0) return open(intersectLowerBound, intersectLowerBound);

		return ComparableInterval.over(intersectLowerBound, greaterOfLowerIncluded(other), intersectUpperBound, lesserOfUpperIncluded(other));
	}

}
