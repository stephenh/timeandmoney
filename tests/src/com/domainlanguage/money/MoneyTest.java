package com.domainlanguage.money;

import junit.framework.*;

import java.math.*;
import java.util.Currency;

import com.domainlanguage.testutil.SerializationTest;


public class MoneyTest extends TestCase {
	private Money d15;
	private Money d2_51;
	private Money y50;
	private Money e2_51;
	private Money d100;
	
	private static Currency USD = Currency.getInstance("USD");
	private static Currency JPY = Currency.getInstance("JPY");
	private static Currency EUR = Currency.getInstance("EUR");

	public void setUp() {
		d15 = Money.valueOf(new BigDecimal("15.0"), USD);
		d2_51 = Money.valueOf(new BigDecimal("2.51"), USD);
		e2_51 = Money.valueOf(new BigDecimal("2.51"), EUR);
		y50 = Money.valueOf (new BigDecimal("50"), JPY);
		d100 = Money.valueOf(new BigDecimal("100.0"), USD);
	}

    public void testSerialization() {
    	SerializationTest.assertSerializationWorks(d15);
    }
	
	public void testCreationFromDouble() {
		assertEquals(d15, Money.valueOf (15.0, USD));
		assertEquals(d2_51, Money.valueOf (2.51, USD));
		assertEquals(y50, Money.valueOf (50.1, JPY));
		assertEquals(d100, Money.valueOf (100, USD));
	}

	public void testYen() {
		assertEquals("JPY 50", y50.toString());
		Money y80 = Money.valueOf (new BigDecimal("80"), JPY);
		Money y30 = Money.valueOf (30, JPY);
        assertEquals(y80, y50.plus(y30));
		assertEquals("mult", y80, y50.times(1.6));
    }



	public void testConstructor() throws Exception {
		Money d69_99 = new Money (new BigDecimal ("69.99"), USD);
		assertEquals(new BigDecimal("69.99"), d69_99.amount());
		assertEquals(USD, d69_99.currency());
		try {
			new Money (new BigDecimal("69.999"), USD);
			fail("Money constructor shall never round, and shall not accept a value whose scale doesn't fit the Currency.");
		} catch (IllegalArgumentException correctResponse) {}
	}


    public void testDivide() {
		assertEquals(Money.dollars(33.33), d100.dividedBy(3));
		assertEquals(Money.dollars(16.67), d100.dividedBy(6));
    }
	public void testMultiply() {
		assertEquals(Money.dollars(150), d15.times(10));
		assertEquals(Money.dollars(1.5), d15.times(0.1));
		assertEquals(Money.dollars(70), d100.times(0.7));
	}
	public void testMultiplyRounding() {
		assertEquals(Money.dollars(66.67), d100.times(0.66666667));
		assertEquals(Money.dollars(66.66), d100.times(0.66666667, BigDecimal.ROUND_DOWN));

	}
	public void testMultiplicationWithExplicitRounding() {
		assertEquals(Money.dollars(66.67), d100.times(new BigDecimal("0.666666"), BigDecimal.ROUND_HALF_EVEN));
		assertEquals(Money.dollars(66.66), d100.times(new BigDecimal("0.666666"), BigDecimal.ROUND_DOWN));
		assertEquals(Money.dollars(-66.66), d100.negated().times(new BigDecimal("0.666666"), BigDecimal.ROUND_DOWN));
	}

	public void testMinimumIncrement() {
		assertEquals(Money.valueOf(0.01, USD), d100.minimumIncrement());
		assertEquals(Money.valueOf(1, JPY), y50.minimumIncrement());
	}

	public void testAdditionOfDifferentCurrencies() {
		try {
			d15.plus(e2_51);
			fail("added different currencies");
		} catch (Exception ignore) {
		}
	}
	
	public void testDivisionByMoney() {
		assertEquals(new BigDecimal(2.50), Money.dollars(5.00).dividedBy(Money.dollars(2.00)).value(1, BigDecimal.ROUND_UNNECESSARY));
		assertEquals(new BigDecimal(1.25), Money.dollars(5.00).dividedBy(Money.dollars(4.00)).value(2, BigDecimal.ROUND_UNNECESSARY));
		assertEquals(new BigDecimal(5), Money.dollars(5.00).dividedBy(Money.dollars(1.00)).value(0, BigDecimal.ROUND_UNNECESSARY));
		try {
			Money.dollars(5.00).dividedBy(Money.dollars(2.00)).value(0, BigDecimal.ROUND_UNNECESSARY);
			fail("dividedBy(Money) does not allow rounding.");
		} catch(ArithmeticException correctBehavior) {}
		try {
			Money.dollars(10.00).dividedBy(Money.dollars(3.00)).value(5, BigDecimal.ROUND_UNNECESSARY);
			fail("dividedBy(Money) does not allow rounding.");
		} catch(ArithmeticException correctBehavior) {}
	}
	

	public void testCloseNumbersNotEqual() {
		Money d2_51a = Money.dollars(2.515);
		Money d2_51b = Money.dollars(2.5149);
		assertTrue(!d2_51a.equals(d2_51b));
	}

	public void testCompare() {
		assertTrue(d15.isGreaterThan(d2_51));
		assertTrue(d2_51.isLessThan(d15));
		assertTrue(!d15.isGreaterThan(d15));
		assertTrue(!d15.isLessThan(d15));
		try {
			d15.isGreaterThan(e2_51);
			fail();
		} catch (Exception correctBehavior) {
		}
	}

	public void testDifferentCurrencyNotEqual() {
		assertTrue(!d2_51.equals(e2_51));
	}

	public void testEquals() {
		Money d2_51a = Money.dollars(2.51);
		assertEquals(d2_51a, d2_51);
	}

//  TODO Need default formatted string, which Java Currency doesn't provide
//	public void xtestFormatPrinting() {
//		// *** TBD **
//		assertEquals("$15.00", d15.formatString());
//		assertEquals("DM 2.51", y2_51.formatString());
//	}

	public void testHash() {
		Money d2_51a = Money.dollars(2.51);
		assertEquals(d2_51a.hashCode(), d2_51.hashCode());
	}

// TODO: Formatted printing of Money
//	public void testLocalPrinting() {
//		assertEquals("$15.00", d15.localString());
//		assertEquals("2,51 DM", m2_51.localString());
//	}

	public void testNegation() {
		assertEquals(Money.dollars(-15), d15.negated());
		assertEquals(e2_51, e2_51.negated().negated());
	}

	public void testPositiveNegative() {
		assertTrue(d15.isPositive());
		assertTrue(Money.dollars(-10).isNegative());
		assertFalse(Money.dollars(0).isPositive());
		assertFalse(Money.dollars(0).isNegative());
		assertTrue(Money.dollars(0).isZero());
	}

	public void testPrint() {
		assertEquals("USD 15.00", d15.toString());
	}

	public void testRound() {
		Money dRounded = Money.dollars(1.2350);
		assertEquals(Money.dollars(1.24), dRounded);
	}

	public void testSubtraction() {
		assertEquals(Money.dollars(12.49), d15.minus(d2_51));
	}
	
	public void testIncremented() {
		assertEquals(Money.dollars(2.52), d2_51.incremented());
		assertEquals(Money.valueOf(51, JPY), y50.incremented());
	}
	
	public void xtestSimpleFloatProblem() {
		//<codeFragment name = "floatProblem">
		double val = 0.00;
		for (int i = 0; i < 10; i++) val += 0.10;
		System.out.println(val == 1.00);
        // </codeFragment>
	}
}
