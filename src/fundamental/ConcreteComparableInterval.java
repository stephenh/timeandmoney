/**
 *
 * This code is provided under the following licence: 
 * MIT Licence
 * timeandmoney.sourceforge.net
 * Copyright 2004, Domain Language, Inc.
 */

package fundamental;


public class ConcreteComparableInterval extends ComparableInterval {
	private Comparable lowerBound;
	private boolean lowerBoundIncluded;
	private Comparable upperBound;
	private boolean upperBoundIncluded;

	public ConcreteComparableInterval(Comparable lower, boolean lowerIncluded, Comparable upper, boolean upperIncluded) {
		assert lower.compareTo(upper) < 0;
		lowerBound = lower;
		lowerBoundIncluded = lowerIncluded;
		upperBound = upper;
		upperBoundIncluded = upperIncluded;
	}
	

	public Comparable getLowerBound() {
		return lowerBound;
	}
	public Comparable getUpperBound() {
		return upperBound;
	}
	public boolean isLowerBoundIncluded() {
		return lowerBoundIncluded;
	}
	public boolean isUpperBoundIncluded() {
		return upperBoundIncluded;
	}

	public ComparableInterval intersect(ComparableInterval other) {
		Comparable intersectLowerBound = greaterOfLowerBounds(other);
		Comparable intersectUpperBound = lesserOfUpperBounds(other);
		if (intersectLowerBound.compareTo(intersectUpperBound) > 0) return open(intersectLowerBound, intersectLowerBound);

		return ComparableInterval.over(intersectLowerBound, greaterOfLowerIncluded(other), intersectUpperBound, lesserOfUpperIncluded(other));
	}

}
