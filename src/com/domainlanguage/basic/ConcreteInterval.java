/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.basic;


//refactor: since Interval is creating those, why expose this class?
//refactor: having TimeInterval as a sublass of this is a bad practice:
// in general, do not make concrete classes subclass from a concrete class
// (google for what issues arise from such practice)  (Benny)
public class ConcreteInterval extends Interval {
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

}
