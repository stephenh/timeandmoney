/**
 *
 * This code is provided under the following licence: 
 * MIT Licence
 * timeandmoney.sourceforge.net
 * Copyright 2004, Domain Language, Inc.
 */

package fundamental;


public class ComparableInterval {
	private Comparable lowerBound;
	private boolean lowerBoundIncluded;
	private Comparable upperBound;
	private boolean upperBoundIncluded;
	
	ComparableInterval(Comparable lower, boolean lowerIncluded, Comparable upper, boolean upperIncluded) {
		assert lower.compareTo(upper) < 0;
		lowerBound = lower;
		lowerBoundIncluded = lowerIncluded;
		upperBound = upper;
		upperBoundIncluded = upperIncluded;
	}
	
	public static ComparableInterval closed(Comparable lower, Comparable upper) {
		return new ComparableInterval(lower, true, upper, true);
	}
	
	public Comparable getLowerBound() {
		//Warning: This method should generally be used for display
		//purposes and interactions with closely coupled classes.
		//Look for (or add) other methods to do computations.
		return lowerBound;
	}
	public Comparable getUpperBound() {
		//Warning: This method should generally be used for display
		//purposes and interactions with closely coupled classes.
		//Look for (or add) other methods to do computations.
		return upperBound;
	}
	
	public boolean intersects(ComparableInterval other) {
		int comparison = greaterOfLowerBounds(other).compareTo(lesserOfUpperBounds(other));
		if (comparison < 0) return true;
		if (comparison > 0) return false;
		return greaterOfLowerIncluded(other) && lesserOfUpperIncluded(other);
	}
	
	private Comparable greaterOfLowerBounds(ComparableInterval other) {
		int lowerComparison = lowerBound.compareTo(other.lowerBound);
		if (lowerComparison >= 0) return this.lowerBound;
		return other.lowerBound;
	}

	private boolean greaterOfLowerIncluded(ComparableInterval other) {
		int lowerComparison = lowerBound.compareTo(other.lowerBound);
		if (lowerComparison > 0) return this.lowerBoundIncluded;
		if (lowerComparison < 0) return other.lowerBoundIncluded;
		return this.lowerBoundIncluded && other.lowerBoundIncluded;
	}

	private Comparable lesserOfUpperBounds(ComparableInterval other) {
		int upperComparison = upperBound.compareTo(other.upperBound);
		if (upperComparison <= 0) return this.upperBound;
		return other.upperBound;
	}

	private boolean lesserOfUpperIncluded(ComparableInterval other) {
		int upperComparison = upperBound.compareTo(other.upperBound);
		if (upperComparison < 0) return this.upperBoundIncluded;
		if (upperComparison > 0) return other.upperBoundIncluded;
		return this.upperBoundIncluded && other.upperBoundIncluded;
	}
	
	
	public boolean includes(Comparable value) {
		return !this.isBefore(value) && !this.isAfter(value);
	}

	public boolean includes(ComparableInterval other) {
		int lowerComparison = lowerBound.compareTo(other.lowerBound);
			boolean lowerPass = this.includes(other.lowerBound) ||
				(lowerComparison == 0 && !other.lowerBoundIncluded);

		int upperComparison = upperBound.compareTo(other.upperBound);
			boolean upperPass = this.includes(other.upperBound) ||
				(upperComparison == 0 && !other.upperBoundIncluded);
			
		return lowerPass && upperPass;
	
	}


	private boolean isBefore(Comparable value) {
		int comparison = upperBound.compareTo(value);
		return comparison < 0 ||
			(comparison == 0 && !upperBoundIncluded);
	}

	private boolean isAfter(Comparable value) {
		int comparison = lowerBound.compareTo(value);
		return comparison > 0 ||
			(comparison == 0 && !lowerBoundIncluded);
	}

}
