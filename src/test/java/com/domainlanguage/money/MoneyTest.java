/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.domainlanguage.base.Ratio;
import com.domainlanguage.base.Rounding;
import com.domainlanguage.tests.SerializationTester;

public class MoneyTest extends TestCase {
  private static Currency USD = Currency.getInstance("USD");
  private static Currency JPY = Currency.getInstance("JPY");
  private static Currency EUR = Currency.getInstance("EUR");

  private Money d15;
  private Money d2_51;
  private Money y50;
  private Money e2_51;
  private Money d100;

  public void setUp() {
    this.d15 = Money.valueOf(new BigDecimal("15.0"), MoneyTest.USD);
    this.d2_51 = Money.valueOf(new BigDecimal("2.51"), MoneyTest.USD);
    this.e2_51 = Money.valueOf(new BigDecimal("2.51"), MoneyTest.EUR);
    this.y50 = Money.valueOf(new BigDecimal("50"), MoneyTest.JPY);
    this.d100 = Money.valueOf(new BigDecimal("100.0"), MoneyTest.USD);
  }

  public void testSerialization() {
    SerializationTester.assertCanBeSerialized(this.d15);
  }

  public void testCreationFromDouble() {
    Assert.assertEquals(this.d15, Money.valueOf(15.0, MoneyTest.USD));
    Assert.assertEquals(this.d2_51, Money.valueOf(2.51, MoneyTest.USD));
    Assert.assertEquals(this.y50, Money.valueOf(50.1, MoneyTest.JPY));
    Assert.assertEquals(this.d100, Money.valueOf(100, MoneyTest.USD));
  }

  public void testYen() {
    Assert.assertEquals("JPY 50", this.y50.toString());
    Money y80 = Money.valueOf(new BigDecimal("80"), MoneyTest.JPY);
    Money y30 = Money.valueOf(30, MoneyTest.JPY);
    Assert.assertEquals(y80, this.y50.plus(y30));
    Assert.assertEquals("mult", y80, this.y50.times(1.6));
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
    Assert.assertEquals(Money.dollars(33.33), this.d100.dividedBy(3));
    Assert.assertEquals(Money.dollars(16.67), this.d100.dividedBy(6));
  }

  public void testMultiply() {
    Assert.assertEquals(Money.dollars(150), this.d15.times(10));
    Assert.assertEquals(Money.dollars(1.5), this.d15.times(0.1));
    Assert.assertEquals(Money.dollars(70), this.d100.times(0.7));
  }

  public void testMultiplyRounding() {
    Assert.assertEquals(Money.dollars(66.67), this.d100.times(0.66666667));
    Assert.assertEquals(Money.dollars(66.66), this.d100.times(0.66666667, Rounding.DOWN));
  }

  public void testMultiplicationWithExplicitRounding() {
    Assert.assertEquals(Money.dollars(66.67), this.d100.times(new BigDecimal("0.666666"), Rounding.HALF_EVEN));
    Assert.assertEquals(Money.dollars(66.66), this.d100.times(new BigDecimal("0.666666"), Rounding.DOWN));
    Assert.assertEquals(Money.dollars(-66.66), this.d100.negated().times(new BigDecimal("0.666666"), Rounding.DOWN));
  }

  public void testMinimumIncrement() {
    Assert.assertEquals(Money.valueOf(0.01, MoneyTest.USD), this.d100.minimumIncrement());
    Assert.assertEquals(Money.valueOf(1, MoneyTest.JPY), this.y50.minimumIncrement());
  }

  public void testAdditionOfDifferentCurrencies() {
    try {
      this.d15.plus(this.e2_51);
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
    Assert.assertTrue(this.d15.isGreaterThan(this.d2_51));
    Assert.assertTrue(this.d2_51.isLessThan(this.d15));
    Assert.assertTrue(!this.d15.isGreaterThan(this.d15));
    Assert.assertTrue(!this.d15.isLessThan(this.d15));
    try {
      this.d15.isGreaterThan(this.e2_51);
      Assert.fail();
    } catch (Exception correctBehavior) {
    }
  }

  public void testDifferentCurrencyNotEqual() {
    Assert.assertTrue(!this.d2_51.equals(this.e2_51));
  }

  public void testEquals() {
    Money d2_51a = Money.dollars(2.51);
    Assert.assertEquals(d2_51a, this.d2_51);
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
    Assert.assertEquals(d2_51a.hashCode(), this.d2_51.hashCode());
  }

  public void testNegation() {
    Assert.assertEquals(Money.dollars(-15), this.d15.negated());
    Assert.assertEquals(this.e2_51, this.e2_51.negated().negated());
  }

  public void testPositiveNegative() {
    Assert.assertTrue(this.d15.isPositive());
    Assert.assertTrue(Money.dollars(-10).isNegative());
    Assert.assertFalse(Money.dollars(0).isPositive());
    Assert.assertFalse(Money.dollars(0).isNegative());
    Assert.assertTrue(Money.dollars(0).isZero());
  }

  public void testPrint() {
    Assert.assertEquals("$15.00", this.d15.toString(Locale.US));
    Assert.assertEquals("USD 15.00", this.d15.toString(Locale.UK));
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
    Assert.assertEquals(Money.dollars(12.49), this.d15.minus(this.d2_51));
  }

  public void testApplyRatio() {
    Ratio oneThird = Ratio.of(1, 3);
    Money result = Money.dollars(100).applying(oneThird, 1, Rounding.UP);
    Assert.assertEquals(Money.dollars(33.40), result);
  }

  public void testIncremented() {
    Assert.assertEquals(Money.dollars(2.52), this.d2_51.incremented());
    Assert.assertEquals(Money.valueOf(51, MoneyTest.JPY), this.y50.incremented());
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
