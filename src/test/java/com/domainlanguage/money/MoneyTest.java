/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import com.domainlanguage.base.Ratio;
import com.domainlanguage.base.Rounding;
import com.domainlanguage.tests.SerializationTester;

import junit.framework.Assert;
import junit.framework.TestCase;

public class MoneyTest extends TestCase {

  private static final Currency USD = Currency.getInstance("USD");
  private static final Currency JPY = Currency.getInstance("JPY");
  private static final Currency EUR = Currency.getInstance("EUR");

  private static final Money d15 = Money.valueOf(new BigDecimal("15.0"), MoneyTest.USD);
  private static final Money d2_51 = Money.valueOf(new BigDecimal("2.51"), MoneyTest.USD);
  private static final Money y50 = Money.valueOf(new BigDecimal("50"), MoneyTest.JPY);
  private static final Money e2_51 = Money.valueOf(new BigDecimal("2.51"), MoneyTest.EUR);
  private static final Money d100 = Money.valueOf(new BigDecimal("100.0"), MoneyTest.USD);

  public void testSerialization() {
    SerializationTester.assertCanBeSerialized(d15);
  }

  public void testCreationFromDouble() {
    Assert.assertEquals(d15, Money.valueOf(15.0, MoneyTest.USD));
    Assert.assertEquals(d2_51, Money.valueOf(2.51, MoneyTest.USD));
    Assert.assertEquals(y50, Money.valueOf(50.1, MoneyTest.JPY));
    Assert.assertEquals(d100, Money.valueOf(100, MoneyTest.USD));
  }

  public void testYen() {
    Assert.assertEquals("JPY 50", y50.toString());
    Money y80 = Money.valueOf(new BigDecimal("80"), MoneyTest.JPY);
    Money y30 = Money.valueOf(30, MoneyTest.JPY);
    Assert.assertEquals(y80, y50.plus(y30));
    Assert.assertEquals("mult", y80, y50.times(1.6));
  }

  public void testConstructor() throws Exception {
    Money d69_99 = new Money(new BigDecimal("69.99"), MoneyTest.USD);
    Assert.assertEquals(new BigDecimal("69.99"), d69_99.getAmount());
    Assert.assertEquals(MoneyTest.USD, d69_99.getCurrency());
    try {
      new Money(new BigDecimal("69.999"), MoneyTest.USD);
      Assert.fail("Money constructor shall never round, and shall not accept a value whose scale doesn't fit the Currency.");
    } catch (IllegalArgumentException correctResponse) {
    }
  }

  public void testDivide() {
    Assert.assertEquals(Money.dollars(33.33), d100.dividedBy(3));
    Assert.assertEquals(Money.dollars(16.67), d100.dividedBy(6));
  }

  public void testMultiply() {
    Assert.assertEquals(Money.dollars(150), d15.times(10));
    Assert.assertEquals(Money.dollars(1.5), d15.times(0.1));
    Assert.assertEquals(Money.dollars(70), d100.times(0.7));
  }

  public void testMultiplyRounding() {
    Assert.assertEquals(Money.dollars(66.67), d100.times(0.66666667));
    Assert.assertEquals(Money.dollars(66.66), d100.times(0.66666667, Rounding.DOWN));
  }

  public void testMultiplicationWithExplicitRounding() {
    Assert.assertEquals(Money.dollars(66.67), d100.times(new BigDecimal("0.666666"), Rounding.HALF_EVEN));
    Assert.assertEquals(Money.dollars(66.66), d100.times(new BigDecimal("0.666666"), Rounding.DOWN));
    Assert.assertEquals(Money.dollars(-66.66), d100.negated().times(new BigDecimal("0.666666"), Rounding.DOWN));
  }

  public void testMinimumIncrement() {
    Assert.assertEquals(Money.valueOf(0.01, MoneyTest.USD), d100.minimumIncrement());
    Assert.assertEquals(Money.valueOf(1, MoneyTest.JPY), y50.minimumIncrement());
  }

  public void testAdditionOfDifferentCurrencies() {
    try {
      d15.plus(e2_51);
      Assert.fail("added different currencies");
    } catch (Exception ignore) {
    }
  }

  public void testDivisionByMoney() {
    Assert.assertEquals(new BigDecimal(2.50), Money.dollars(5.00).dividedBy(Money.dollars(2.00)).decimalValue(1, Rounding.UNNECESSARY));
    Assert.assertEquals(new BigDecimal(1.25), Money.dollars(5.00).dividedBy(Money.dollars(4.00)).decimalValue(2, Rounding.UNNECESSARY));
    Assert.assertEquals(new BigDecimal(5), Money.dollars(5.00).dividedBy(Money.dollars(1.00)).decimalValue(0, Rounding.UNNECESSARY));
    try {
      Money.dollars(5.00).dividedBy(Money.dollars(2.00)).decimalValue(0, Rounding.UNNECESSARY);
      Assert.fail("dividedBy(Money) does not allow rounding.");
    } catch (ArithmeticException correctBehavior) {
    }
    try {
      Money.dollars(10.00).dividedBy(Money.dollars(3.00)).decimalValue(5, Rounding.UNNECESSARY);
      Assert.fail("dividedBy(Money) does not allow rounding.");
    } catch (ArithmeticException correctBehavior) {
    }
  }

  public void testCloseNumbersNotEqual() {
    Money d2_51a = Money.dollars(2.515);
    Money d2_51b = Money.dollars(2.5149);
    Assert.assertTrue(!d2_51a.equals(d2_51b));
  }

  public void testCompare() {
    Assert.assertTrue(d15.isGreaterThan(d2_51));
    Assert.assertTrue(d2_51.isLessThan(d15));
    Assert.assertTrue(!d15.isGreaterThan(d15));
    Assert.assertTrue(!d15.isLessThan(d15));
    try {
      d15.isGreaterThan(e2_51);
      Assert.fail();
    } catch (Exception correctBehavior) {
    }
  }

  public void testDifferentCurrencyNotEqual() {
    Assert.assertTrue(!d2_51.equals(e2_51));
  }

  public void testEquals() {
    Money d2_51a = Money.dollars(2.51);
    Assert.assertEquals(d2_51a, d2_51);
  }

  public void testEqualsNull() {
    Money d2_51a = Money.dollars(2.51);
    Object objectNull = null;
    Assert.assertFalse(d2_51a.equals(objectNull));

    //This next test seems just like the previous, but it's not
    //The Java Compiler early binds message sends and
    //it will bind the next call to equals(Money) and
    //the previous will bind to equals(Object)
    //I renamed the original equals(Money) to
    //equalsMoney(Money) to prevent wrong binding.
    Money moneyNull = null;
    Assert.assertFalse(d2_51a.equals(moneyNull));
  }

  public void testHash() {
    Money d2_51a = Money.dollars(2.51);
    Assert.assertEquals(d2_51a.hashCode(), d2_51.hashCode());
  }

  public void testNegation() {
    Assert.assertEquals(Money.dollars(-15), d15.negated());
    Assert.assertEquals(e2_51, e2_51.negated().negated());
  }

  public void testPositiveNegative() {
    Assert.assertTrue(d15.isPositive());
    Assert.assertTrue(Money.dollars(-10).isNegative());
    Assert.assertFalse(Money.dollars(0).isPositive());
    Assert.assertFalse(Money.dollars(0).isNegative());
    Assert.assertTrue(Money.dollars(0).isZero());
  }

  public void testPrint() {
    Assert.assertEquals("$15.00", d15.toString());
    Assert.assertEquals("15.00", d15.toNumericString());
    Assert.assertEquals("$15.00", d15.toString(Locale.US));
    Assert.assertEquals("USD 15.00", d15.toString(Locale.UK));
    Assert.assertEquals("JPY 50", y50.toString());
    Assert.assertEquals("50", y50.toNumericString());
  }

  // TODO: Formatted printing of Money
  //	public void testLocalPrinting() {
  //		assertEquals("$15.00", d15.localString());
  //		assertEquals("2,51 DM", m2_51.localString());
  //	}

  public void testRound() {
    Money dRounded = Money.dollars(1.2350);
    Assert.assertEquals(Money.dollars(1.24), dRounded);
  }

  public void testSubtraction() {
    Assert.assertEquals(Money.dollars(12.49), d15.minus(d2_51));
  }

  public void testApplyRatio() {
    Ratio oneThird = Ratio.of(1, 3);
    Money result = Money.dollars(100).applying(oneThird, 1, Rounding.UP);
    Assert.assertEquals(Money.dollars(33.40), result);
  }

  public void testIncremented() {
    Assert.assertEquals(Money.dollars(2.52), d2_51.incremented());
    Assert.assertEquals(Money.valueOf(51, MoneyTest.JPY), y50.incremented());
  }

  public void testFractionalPennies() {
    //        CurrencyPolicy(USD, 0.0025);
    //        Smallest unit.unit Any Money based on this CurrencyPolicy must be some multiple of the
    //        smallest unit. "Scale" is insufficient, because the limit is not always a number of demial places.
    //        Money someFee = Money.dollars(0.0025);
    //        Money wholeMoney = someFee.times(4);
    //        assertEquals(Money.dollars(0.01), wholeMoney);

  }

}
