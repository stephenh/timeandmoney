/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.basic;

import java.math.BigDecimal;

public class Ratio {
	private BigDecimal numerator;
	private BigDecimal denominator;

	public Ratio(BigDecimal numerator, BigDecimal denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	public static Ratio of(BigDecimal numerator, BigDecimal denominator) {
		return new Ratio(numerator, denominator);
	}

	public BigDecimal value(int scale, int roundingRule) {
		return numerator.divide(denominator, scale, roundingRule);
	}

	public static Ratio of(long numerator, long denominator) {
		return new Ratio(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
	}
}
