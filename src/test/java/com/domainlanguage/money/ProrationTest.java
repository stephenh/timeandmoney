/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.money;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ProrationTest extends TestCase {

  public void testAllocate1() {
    long[] proportions = { 1, 1 };
    Money[] result = Proration.proratedOver(Money.dollars(0.01), proportions);
    Assert.assertEquals(Money.dollars(0.01), result[0]);
    Assert.assertEquals(Money.dollars(0), result[1]);
  }

  public void testProrateOver2() {
    long[] proportions = { 3, 7 };
    Money[] result = Proration.proratedOver(Money.dollars(0.05), proportions);
    Assert.assertEquals(Money.dollars(0.02), result[0]);
    Assert.assertEquals(Money.dollars(0.03), result[1]);
  }

  public void testProrateOver10() throws Exception {
    long[] proportions = { 17, 2, 1, 35, 35, 10 };
    Money[] result = Proration.proratedOver(Money.dollars(0.10), proportions);
    Assert.assertEquals(Money.dollars(0.02), result[0]);
    Assert.assertEquals(Money.dollars(0.01), result[1]);
    Assert.assertEquals(Money.dollars(0.00), result[2]);
    Assert.assertEquals(Money.dollars(0.03), result[3]);
    Assert.assertEquals(Money.dollars(0.03), result[4]);
    Assert.assertEquals(Money.dollars(0.01), result[5]);
    Money sum = Money.dollars(0.0);
    for (int i = 0; i < result.length; i++) {
      sum = sum.plus(result[i]);
    }
    Assert.assertEquals(Money.dollars(0.10), sum);
  }

  public void testProrateZeroTotal() {
    long[] proportions = { 3, 7 };
    Money[] result = Proration.proratedOver(Money.dollars(0), proportions);
    Assert.assertEquals(Money.dollars(0), result[0]);
    Assert.assertEquals(Money.dollars(0), result[1]);
  }

  public void testProrateZeroTotalInEuros() {
    long[] proportions = { 3, 7 };
    Money[] result = Proration.proratedOver(Money.euros(0), proportions);
    Assert.assertEquals(Money.euros(0), result[0]);
    Assert.assertEquals(Money.euros(0), result[1]);
  }

  public void testProrateTotalIndivisibleBy3() {
    Money[] actual = Proration.dividedEvenlyIntoParts(Money.dollars(100), 3);
    Money[] expected = { Money.dollars(33.34), Money.dollars(33.33), Money.dollars(33.33) };
    for (int i = 0; i < expected.length; i++) {
      Assert.assertEquals(expected[i], actual[i]);
    }
  }

  public void testProrateOnlyOneShortOfEven() {
    Money[] prorated = Proration.dividedEvenlyIntoParts(Money.dollars(1.09), 10);
    for (int i = 0; i < 9; i++) {
      Assert.assertEquals(Money.dollars(0.11), prorated[i]);
    }
    Assert.assertEquals(Money.dollars(0.10), prorated[9]);
  }

  public void testDistributeRemainder() {
    Money[] startingValues = new Money[4];
    startingValues[0] = Money.dollars(1.00);
    startingValues[1] = Money.dollars(2.00);
    startingValues[2] = Money.dollars(3.00);
    startingValues[3] = Money.dollars(4.00);
    Money[] result = Proration.distributeRemainderOver(startingValues, Money.dollars(0.02));
    Assert.assertEquals(Money.dollars(1.01), result[0]);
    Assert.assertEquals(Money.dollars(2.01), result[1]);
    Assert.assertEquals(Money.dollars(3.00), result[2]);
    Assert.assertEquals(Money.dollars(4.00), result[3]);
  }

  public void testSumMoney() {
    Money[] startingValues = new Money[4];
    startingValues[0] = Money.dollars(1.00);
    startingValues[1] = Money.dollars(2.00);
    startingValues[2] = Money.dollars(3.00);
    startingValues[3] = Money.dollars(4.00);
    Assert.assertEquals(Money.dollars(10.00), Proration.sum(startingValues));
  }

  public void testPartOfWhole() {
    Money total = Money.dollars(10.00);
    long portion = 3l;
    long whole = 9l;
    Assert.assertEquals(Money.dollars(3.33), Proration.partOfWhole(total, portion, whole));
  }

}
