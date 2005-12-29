/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;


class ConcreteInterval extends Interval {
	private Comparable lowerLimit;
	private boolean includesLowerLimit;
	private Comparable upperLimit;
	private boolean includesUpperLimit;

	public ConcreteInterval(Comparable lower, boolean lowerIncluded, Comparable upper, boolean upperIncluded) {
		assert lower.compareTo(upper) <= 0;
		lowerLimit = lower;
		includesLowerLimit = lowerIncluded;
		upperLimit = upper;
		includesUpperLimit = upperIncluded;
	}
	
	public Interval newOfSameType(Comparable lower, boolean isLowerClosed, Comparable upper, boolean isUpperClosed) {
		return new ConcreteInterval(lower, isLowerClosed, upper, isUpperClosed);
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

    private boolean isIncludesLowerLimitForPersistentMapping() {
        return includesLowerLimit;
    }

    private void setIncludesLowerLimitForPersistentMapping(boolean includesLowerLimit) {
        this.includesLowerLimit = includesLowerLimit;
    }

    private boolean isIncludesUpperLimitForPersistentMapping() {
        return includesUpperLimit;
    }

    private void setIncludesUpperLimitForPersistentMapping(boolean includesUpperLimit) {
        this.includesUpperLimit = includesUpperLimit;
    }

    private Comparable getLowerLimitForPersistentMapping() {
        return lowerLimit;
    }

    private void setLowerLimitForPersistentMapping(Comparable lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    private Comparable getUpperLimitForPersistentMapping() {
        return upperLimit;
    }

    private void setUpperLimitForPersistentMapping(Comparable upperLimit) {
        this.upperLimit = upperLimit;
    }

}
