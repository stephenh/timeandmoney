/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.money;

import java.math.BigDecimal;

public class Proration {
	public Money[] dividedEvenlyInto(Money total, int n) {
		Money lowResult = total.dividedBy(BigDecimal.valueOf(n), BigDecimal.ROUND_DOWN);
		Money[] lowResults = new Money[n];
		for (int i = 0; i < n; i++) lowResults[i] = lowResult;
		Money remainder = total.minus(sum(lowResults));
		return distributeRemainderOver(lowResults, remainder);
	}

	public Money[] proratedOver(Money total, long[] longProportions) {
		BigDecimal[] proportions = new BigDecimal[longProportions.length];
		for (int i = 0; i < longProportions.length; i++) {
			proportions[i] = BigDecimal.valueOf(longProportions[i]);
		}
		return proratedOver(total, proportions);
	}
	
	public Money[] proratedOver(Money total, BigDecimal[] proportions) {
		Money[] simpleResult = new Money[proportions.length];

		BigDecimal proportionsTotal = sum(proportions);
		for (int i = 0; i < proportions.length; i++) {
			simpleResult[i] = total.times(proportions[i]).dividedBy(proportionsTotal, BigDecimal.ROUND_DOWN);
		}
		
		Money remainder = total.minus(sum(simpleResult));
		return distributeRemainderOver(simpleResult, remainder);
	}
	
	static Money[] distributeRemainderOver(Money[] amounts, Money remainder) {
		int increments = remainder.dividedBy(remainder.minimumIncrement(), 0).intValue();
		assert increments <= amounts.length; 

		Money[] results = new Money[amounts.length];
		for (int i = 0; i < increments; i++) {
			results[i] = amounts[i].incremented();
		}
		for (int i = increments; i < amounts.length; i++) {
			results[i] = amounts[i];
		}
		
		return results; 
	}
	
	static Money sum(Money[] elements) {
		Money sum = Money.valueOf(0, elements[0].currency());
		for (int i = 0; i < elements.length; i++) {
			sum = sum.plus(elements[i]);
		}
		return sum;
	}

	static BigDecimal sum(BigDecimal[] elements) {
		BigDecimal sum = new BigDecimal(0);
		for (int i = 0; i < elements.length; i++) {
			sum = sum.add(elements[i]);
		}
		return sum;
	}
	
}
