/**
 *
 * This code is provided under the following licence: 
 * MIT Licence
 * timeandmoney.sourceforge.net
 * Copyright 2004, Domain Language, Inc.
 */

/**
 * Definition consistent with mathematical "interval"
 * see http://en.wikipedia.org/wiki/Interval_(mathematics)
 * 
 */

package fundamental;


public abstract class ComparableInterval implements Comparable {
	
	public static ConcreteComparableInterval closed(Comparable lower, Comparable upper) {
		return new ConcreteComparableInterval(lower, true, upper, true);
	}

	public static ConcreteComparableInterval open(Comparable lower, Comparable upper) {
		return new ConcreteComparableInterval(lower, false, upper, false);
	}

	public static ConcreteComparableInterval over(Comparable lower, boolean lowerIncluded, Comparable upper, boolean upperIncluded) {
		return new ConcreteComparableInterval(lower, upperIncluded, upper, upperIncluded);
	}

	
	public abstract Comparable getLowerBound();
		//Warning: This method should generally be used for display
		//purposes and interactions with closely coupled classes.
		//Look for (or add) other methods to do computations.
	public abstract Comparable getUpperBound();
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
	
	public Comparable greaterOfLowerLimits(ComparableInterval other) {
		int lowerComparison = getLowerBound().compareTo(other.getLowerBound());
		if (lowerComparison >= 0) return this.getLowerBound();
		return other.getLowerBound();
	}

	public boolean greaterOfLowerIncluded(ComparableInterval other) {
		int lowerComparison = getLowerBound().compareTo(other.getLowerBound());
		if (lowerComparison > 0) return this.includesLowerLimit();
		if (lowerComparison < 0) return other.includesLowerLimit();
		return this.includesLowerLimit() && other.includesLowerLimit();
	}

	public Comparable lesserOfUpperLimits(ComparableInterval other) {
		int upperComparison = getUpperBound().compareTo(other.getUpperBound());
		if (upperComparison <= 0) return this.getUpperBound();
		return other.getUpperBound();
	}

	public boolean lesserOfUpperIncluded(ComparableInterval other) {
		int upperComparison = getUpperBound().compareTo(other.getUpperBound());
		if (upperComparison < 0) return this.includesUpperLimit();
		if (upperComparison > 0) return other.includesUpperLimit();
		return this.includesUpperLimit() && other.includesUpperLimit();
	}
	
	
	public boolean includes(Comparable value) {
		return !this.isBelow(value) && !this.isAbove(value);
	}

	public boolean includes(ComparableInterval other) {
		int lowerComparison = getLowerBound().compareTo(other.getLowerBound());
			boolean lowerPass = this.includes(other.getLowerBound()) ||
				(lowerComparison == 0 && !other.includesLowerLimit());

		int upperComparison = getUpperBound().compareTo(other.getUpperBound());
			boolean upperPass = this.includes(other.getUpperBound()) ||
				(upperComparison == 0 && !other.includesUpperLimit());
			
		return lowerPass && upperPass;
	
	}
	

	public boolean isBelow(Comparable value) {
		int comparison = getUpperBound().compareTo(value);
		return comparison < 0 ||
			(comparison == 0 && !includesUpperLimit());
	}

	public boolean isAbove(Comparable value) {
		int comparison = getLowerBound().compareTo(value);
		return comparison > 0 ||
			(comparison == 0 && !includesLowerLimit());
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(includesLowerLimit() ? "[" : "(");
		buffer.append(getLowerBound().toString());
		buffer.append(", ");
		buffer.append(getUpperBound().toString());
		buffer.append(includesUpperLimit() ? "]" : ")");
		return buffer.toString();
	}

	public int compareTo(Object arg) {
		ComparableInterval other = (ComparableInterval) arg;
		if (!getLowerBound().equals(other.getLowerBound())) return getLowerBound().compareTo(other.getLowerBound());
		if (includesLowerLimit() && !other.includesLowerLimit()) return -1;
		if (!includesLowerLimit() && other.includesLowerLimit()) return 1;
		return getUpperBound().compareTo(other.getUpperBound());
	}


}
