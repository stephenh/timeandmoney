/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.money;

import java.util.Currency;

import junit.framework.TestCase;

public class MoneyTest extends TestCase {
	Money d15, d2_51, e2_51;
	
	public void setUp() {
		d15 = Money.dollars(15);
		d2_51 = Money.dollars(2.51);
		e2_51 = Money.valueOf(2.51, Currency.getInstance("EUR"));
	}

	public void testCreation() {
		Money.dollars(10);
	}

	public void testAmount() {
		assertEquals (2.51, d2_51.amount(), 1e-3);
	}

	public void testAdditionOfDifferentCurrencies() {
		try {
			d15.plus(e2_51);
			fail("added different currencies");
		} catch (Exception ignore) {}
	}	
	
	public void testDivide3() {
		 Money[] actual =  Money.dollars(100).divide(3);
		 Money[] expected = {Money.dollars(33.34), Money.dollars(33.33),Money.dollars(33.33)};
		 for (int i = 0;i<expected.length ; i++ ) {
		   assertEquals (expected[i], actual[i]);
		 }
	}

	public void testDivideAllButOne() {
		 Money[] expected =  Money.dollars(1.09).divide(10);
		 for (int i = 0; i<9 ; i++ ) {
		   assertEquals (expected[i], Money.dollars(0.11));
		 }
		 assertEquals(expected[9], Money.dollars(0.10));
	}
	
	public void testCloseNumbersNotEqual() {
		Money d2_51a = Money.dollars (2.515);
		Money d2_51b = Money.dollars (2.5149);
		assertFalse(d2_51a.equals(d2_51b));
	}
	
	public void testCompare() {
		assertTrue("d15 > d2_51", d15.greaterThan(d2_51));
		assertTrue("d2_51 < d15", d2_51.lessThan(d15));
		assertFalse("d15 > d15", d15.greaterThan(d15));
		assertFalse("d15 < d15", d15.lessThan(d15));
		try {
			d15.greaterThan(e2_51);
			fail();
		} catch (Exception ignore) {
			//Exception is correct behavior
		}
	}
	
	public void testDifferentCurrencyNotEqual() {
		assertFalse( d2_51.equals(e2_51));
	}
	
	public void testEquals() {
		Money d2_51a = Money.dollars (2.51);
		assertEquals (d2_51a, d2_51);
	}
	
//	public void xtestFormatPrinting() {
//	  // *** TBD **
//		assertEquals("$15.00", d15.formatString());
//		assertEquals("DM 2.51", m2_51.formatString());
//	}
	
	public void testHash() {
		Money d2_51a = Money.dollars (2.51);
		assertEquals (d2_51a.hashCode(), d2_51.hashCode());
	}
	
//	public void testLocalString() {
//		assertEquals("$15.00", d15.localString());
//		assertEquals("2,51 EUR", e2_51.localString());
//	}
	
	public void testNegate() {
		assertEquals(Money.dollars(-15), d15.negation());
		assertEquals(e2_51, e2_51.negation().negation());
	}

	public void testPositiveNegative() {
		assertTrue(d15.isPositive());
		assertTrue(Money.dollars(-10).isNegative());
		assertFalse(Money.dollars(0).isPositive());
		assertFalse(Money.dollars(0).isNegative());
	}
	
	public void testToString() {
		assertEquals("USD 15.0", d15.toString());
	}
	
	public void testRound() {
		Money dRounded = Money.dollars (1.2350);
		assertEquals (Money.dollars(1.24), dRounded);
	}
	
	public void testSubtraction() {
		assertEquals (Money.dollars (12.49), d15.minus(d2_51));
	}

}
