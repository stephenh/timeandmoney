/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 * 
 * Ratio represents the unitless division of two quantities of the same
 * type. The key to its usefulness is that it defers the calculation of 
 * a decimal value for the ratio. An object which has responsibility for
 * the two values in the ratio and understands their quantities can create
 * the ratio, which can then be used by any client in a unitless form, so
 * that the client is not required to understand the units of the quantity.
 * At the same time, this gives control of the precision and rounding rules
 * to the client, when the time comes to compute a decimal value for the
 * ratio. The client typically has the responsibilities that enable an
 * appropriate choice of these parameters. 
 *  
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

	public BigDecimal decimalValue(int scale, int roundingRule) {
		return numerator.divide(denominator, scale, roundingRule);
	}

	public static Ratio of(long numerator, long denominator) {
		return new Ratio(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
	}
}
