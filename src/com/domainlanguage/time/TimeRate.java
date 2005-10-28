/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

import java.math.*;

import com.domainlanguage.util.*;


public class TimeRate {
	private BigDecimal quantity;
	private Duration unit;

	public TimeRate(double quantity, Duration unit) {
		this(new BigDecimal(quantity), unit);
	}

	public TimeRate(String quantity, Duration unit) {
		this(new BigDecimal(quantity), unit);
	}

	public TimeRate(BigDecimal quantity, Duration unit) {
		this.quantity = quantity;
		this.unit = unit;
	}

	public BigDecimal over(Duration duration) {
		return over(duration, BigDecimal.ROUND_UNNECESSARY);
	}

	public BigDecimal over(Duration duration, int roundRule) {
		return over(duration, scale(), roundRule);
	}

	public BigDecimal over(Duration duration, int scale, int roundRule) {
		return duration.dividedBy(unit).times(quantity).decimalValue(scale, roundRule);
	}

	public boolean equals(Object another) {
		if (!TypeCheck.sameClassOrBothNull(this, another))
			return false;
		TimeRate anotherRate = (TimeRate) another;
		return quantity.equals(anotherRate.quantity) && unit.equals(anotherRate.unit);
	}

	public int scale() {
		return quantity.scale();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(quantity);
		buffer.append(" per ");
		buffer.append(unit);
		return buffer.toString();
	}
}
