/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.basic;

import java.math.BigDecimal;

import com.domainlanguage.basic.Ratio;

import junit.framework.TestCase;

public class RatioTest extends TestCase {

	public void testBigDecimalRatio() {
		Ratio r3over2 = Ratio.of(new BigDecimal(3), new BigDecimal(2));
		BigDecimal result = r3over2.decimalValue(1, BigDecimal.ROUND_UNNECESSARY);
		assertEquals(new BigDecimal("1.5"), result);

		Ratio r10over3 = Ratio.of(new BigDecimal(10), new BigDecimal(3));
		result = r10over3.decimalValue(3, BigDecimal.ROUND_DOWN);
		assertEquals(new BigDecimal("3.333"), result);

		result = r10over3.decimalValue(3, BigDecimal.ROUND_UP);
		assertEquals(new BigDecimal("3.334"), result);
		
		Ratio rManyDigits = Ratio.of(new BigDecimal("9.001"), new BigDecimal(3));
		result = rManyDigits.decimalValue(6, BigDecimal.ROUND_UP);
		assertEquals(new BigDecimal("3.000334"), result);
		
	}
	
	public void testLongRatio() {
		Ratio rManyDigits = Ratio.of(9001l, 3000l);
		BigDecimal result = rManyDigits.decimalValue(6, BigDecimal.ROUND_UP);
		assertEquals(new BigDecimal("3.000334"), result);
	}	
	
}
