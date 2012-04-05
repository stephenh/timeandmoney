/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.money;

import java.math.*;
import java.util.Arrays;
import java.util.List;

import com.domainlanguage.base.*;

public class Proration {

	static Money sum(Money[] elements) {
		Money sum = Money.valueOf(0, elements[0].getCurrency());
		for (int i = 0; i < elements.length; i++) 
			sum = sum.plus(elements[i]);
		return sum;
	}

	static BigDecimal sum(BigDecimal[] elements) {
		BigDecimal sum = new BigDecimal(0);
		for (int i = 0; i < elements.length; i++) 
			sum = sum.add(elements[i]);
		return sum;
	}
	
	static Ratio[] ratios(BigDecimal[] proportions) {
		BigDecimal total = sum(proportions);
		Ratio[] ratios = new Ratio[proportions.length];
		for (int i = 0; i < ratios.length; i++) 
			ratios[i] = Ratio.of(proportions[i], total);
		return ratios;
	}

	private static int defaultScaleForIntermediateCalculations(Money total) {
		return total.getCurrency().getDefaultFractionDigits() + 1;
	}

	public static Money[] dividedEvenlyIntoParts(Money total, int n) {
		Money lowResult = total.dividedBy(BigDecimal.valueOf(n), Rounding.DOWN);
		Money[] lowResults = new Money[n];
		for (int i = 0; i < n; i++) lowResults[i] = lowResult;
		Money remainder = total.minus(sum(lowResults));
		return distributeRemainderOver(lowResults, remainder);
	}

	public static Money[] proratedOver(Money total, long[] longProportions) {
		BigDecimal[] proportions = new BigDecimal[longProportions.length];
		for (int i = 0; i < longProportions.length; i++) {
			proportions[i] = BigDecimal.valueOf(longProportions[i]);
		}
		return proratedOver(total, proportions);
	}
	
	public static Money[] proratedOver(Money total, BigDecimal[] proportions) {
		return proratedOver(total, ratios(proportions));
	}

	public static Money[] proratedOver(Money total, Ratio[] ratios) {
		return proratedOver(total, Arrays.asList(ratios));
	}

	public static Money[] proratedOver(Money total, List<Ratio> ratios) {
		Money[] simpleResult = new Money[ratios.size()];
		int scale = defaultScaleForIntermediateCalculations(total);

		// Check to see if the the total is a multiple of all the denominators,
		// in which case simple multiplication is preferable to avoid rounding.
		// Note: the cool thing to do would be to reduce the fractions, get the
		// lowest common denominator, and see if total is a multiple of that.
		// But we do the simple thing for now as that is all CBAS is passing in.
		boolean dividesEvenly = true;
		for (int i = 0; i < ratios.size(); i++) {
			if (!ratios.get(i).isMultipleOfDenominator(total.breachEncapsulationOfAmount())) {
				dividesEvenly = false;
				break;
			}
		}
		if (dividesEvenly) {
			for (int i = 0; i < ratios.size(); i++) {
				simpleResult[i] = Money.dollars(ratios.get(i).times(total.breachEncapsulationOfAmount()).decimalValue(scale, Rounding.DOWN));
			}
			return simpleResult;
		}

		for (int i = 0; i < ratios.size(); i++) {
			BigDecimal multiplier = ratios.get(i).decimalValue(scale, Rounding.DOWN);
			simpleResult[i] = total.times(multiplier, Rounding.DOWN);
		}
		Money remainder = total.minus(sum(simpleResult));
		return distributeRemainderOver(simpleResult, remainder);
	}

	public static Money partOfWhole(Money total, long portion, long whole) {
		return partOfWhole(total, Ratio.of(portion, whole));
	}

	public static Money partOfWhole(Money total, Ratio ratio) {
		int scale = defaultScaleForIntermediateCalculations(total);
		BigDecimal multiplier = ratio.decimalValue(scale, Rounding.DOWN);
		return total.times(multiplier, Rounding.DOWN);
	}
	
	static Money[] distributeRemainderOver(Money[] amounts, Money remainder) {
		int increments = remainder.dividedBy(remainder.minimumIncrement()).decimalValue(0, Rounding.UNNECESSARY).intValue();
		assertAmountsLengthLessThanOrEqualTo(amounts, increments);

		Money[] results = new Money[amounts.length];
		for (int i = 0; i < amounts.length; i++)
			results[i] = amounts[i];
		for (int i = 0; i < increments; i++)
			results[i % results.length] = results[i % results.length].incremented();
		return results;
	}

    private static void assertAmountsLengthLessThanOrEqualTo(Money[] amounts,
            int increments) {
        if (increments > amounts.length) {
		    throw new IllegalArgumentException();
		}
    }
	
}
