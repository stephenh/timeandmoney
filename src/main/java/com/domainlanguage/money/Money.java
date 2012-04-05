/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.money;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.domainlanguage.base.Ratio;
import com.domainlanguage.base.Rounding;
import com.domainlanguage.time.Duration;

public class Money implements Comparable<Money>, Serializable {

  private static final long serialVersionUID = 1L;
  private static final Currency USD = Currency.getInstance("USD");
  private static final Currency EUR = Currency.getInstance("EUR");
  private static final int DEFAULT_ROUNDING_MODE = Rounding.HALF_EVEN;

  private final BigDecimal amount;
  private final Currency currency;

  /**
   * For convenience, an amount can be rounded to create a Money.
   */
  public static Money valueOf(BigDecimal rawAmount, Currency currency, int roundingMode) {
    BigDecimal amount = rawAmount.setScale(currency.getDefaultFractionDigits(), roundingMode);
    return new Money(amount, currency);
  }

  /**
   * WARNING: Because of the indefinite precision of double, this method must
   * round off the value.
   */
  public static Money valueOf(double dblAmount, Currency currency) {
    return Money.valueOf(dblAmount, currency, Money.DEFAULT_ROUNDING_MODE);
  }

  /**
   * Because of the indefinite precision of double, this method must round off
   * the value. This method gives the client control of the rounding mode.
   */
  public static Money valueOf(double dblAmount, Currency currency, int roundingMode) {
    BigDecimal rawAmount = new BigDecimal(dblAmount);
    return Money.valueOf(rawAmount, currency, roundingMode);
  }

  /**
   * WARNING: Because of the indefinite precision of double, thismethod must
   * round off the value.
   */
  public static Money dollars(double amount) {
    return Money.valueOf(amount, Money.USD);
  }

  /**
   * This creation method is safe to use. It will adjust scale, but will not
   * round off the amount.
   */
  public static Money dollars(BigDecimal amount) {
    return Money.valueOf(amount, Money.USD);
  }

  /**
   * This creation method is safe to use. It will adjust scale, but will not
   * round off the amount.
   */
  public static Money valueOf(BigDecimal amount, Currency currency) {
    return Money.valueOf(amount, currency, Rounding.UNNECESSARY);
  }

  /**
     * WARNING: Because of the indefinite precision of double, this method must
     * round off the value.
     */
  public static Money euros(double amount) {
    return Money.valueOf(amount, Money.EUR);
  }

  /**
     * This creation method is safe to use. It will adjust scale, but will not
     * round off the amount.
     */
  public static Money euros(BigDecimal amount) {
    return Money.valueOf(amount, Money.EUR);
  }

  public static Money sum(Collection<Money> monies) {
    //TODO Return Default Currency
    if (monies.isEmpty()) {
      return Money.dollars(0.00);
    }
    Iterator<Money> iterator = monies.iterator();
    Money sum = iterator.next();
    while (iterator.hasNext()) {
      Money each = iterator.next();
      sum = sum.plus(each);
    }
    return sum;
  }

  /**
   * Return the max of <code>a</code> or <code>0</code>
   */
  public static Money notBelowZero(Money a) {
    Money zero = Money.valueOf(0.00, a.getCurrency());
    return Money.max(zero, a);
  }

  /**
   * Return the max of <code>a</code> or <code>b</code>
   */
  public static Money max(Money a, Money b) {
    if (a.isGreaterThan(b)) {
      return a;
    } else {
      return b;
    }
  }

  /**
   * Return the min of <code>a</code> or <code>b</code>
   */
  public static Money min(Money a, Money b) {
    if (a.isLessThan(b)) {
      return a;
    } else {
      return b;
    }
  }

  /**
   * Return the sum of <code>money</code>
   */
  public static Money sum(List<Money> money) {
    Money total = Money.dollars(0.00);
    for (Iterator<Money> i = money.iterator(); i.hasNext();) {
      total = total.plus(i.next());
    }
    return total;
  }

  public static Money zeroIfNull(Money money) {
    return money == null ? Money.dollars(0.00) : money;
  }

  /**
   * The constructor does not complex computations and requires simple, inputs
   * consistent with the class invariant. Other creation methods are available
   * for convenience.
   */
  public Money(BigDecimal amount, Currency currency) {
    if (amount.scale() != currency.getDefaultFractionDigits()) {
      throw new IllegalArgumentException("Scale of amount does not match currency");
    }
    this.currency = currency;
    this.amount = amount;
  }

  public Money negated() {
    return Money.valueOf(amount.negate(), currency);
  }

  public Money abs() {
    return Money.valueOf(amount.abs(), currency);
  }

  public boolean isNegative() {
    return amount.compareTo(new BigDecimal(0)) < 0;
  }

  public boolean isNotNegative() {
    return !isNegative();
  }

  public boolean isPositive() {
    return amount.compareTo(new BigDecimal(0)) > 0;
  }

  public boolean isNotPositive() {
    return !isPositive();
  }

  public boolean isZero() {
    return equals(Money.valueOf(0.0, currency));
  }

  public boolean isNotZero() {
    return !isZero();
  }

  public Money plus(Money other) {
    assertHasSameCurrencyAs(other);
    return Money.valueOf(amount.add(other.amount), currency);
  }

  public Money minus(Money other) {
    return plus(other.negated());
  }

  public Money dividedBy(BigDecimal divisor, int roundingMode) {
    BigDecimal newAmount = amount.divide(divisor, roundingMode);
    return Money.valueOf(newAmount, currency);
  }

  public Money dividedBy(double divisor) {
    return dividedBy(divisor, Money.DEFAULT_ROUNDING_MODE);
  }

  public Money dividedBy(double divisor, int roundingMode) {
    return dividedBy(new BigDecimal(divisor), roundingMode);
  }

  public Ratio dividedBy(Money divisor) {
    assertHasSameCurrencyAs(divisor);
    return Ratio.of(amount, divisor.amount);
  }

  public Money applying(Ratio ratio, int roundingRule) {
    return applying(ratio, currency.getDefaultFractionDigits(), roundingRule);
  }

  public Money applying(Ratio ratio, int scale, int roundingRule) {
    BigDecimal newAmount = ratio.times(amount).decimalValue(scale, roundingRule);
    return Money.valueOf(newAmount, currency);
  }

  /**
     * TODO: Many apps require carrying extra precision in intermediate
     * calculations. The use of Ratio is a beginning, but need a comprehensive
     * solution. Currently, an invariant of Money is that the scale is the
     * currencies standard scale, but this will probably have to be suspended or
     * elaborated in intermediate calcs, or handled with defered calculations
     * like Ratio.
     */

  public Money times(BigDecimal factor) {
    return times(factor, Money.DEFAULT_ROUNDING_MODE);
  }

  /**
     * TODO: BigDecimal.multiply() scale is sum of scales of two multiplied
     * numbers. So what is scale of times?
     */
  public Money times(BigDecimal factor, int roundingMode) {
    return Money.valueOf(amount.multiply(factor), currency, roundingMode);
  }

  public Money times(double amount, int roundingMode) {
    return times(new BigDecimal(amount), roundingMode);
  }

  public Money times(double amount) {
    return times(new BigDecimal(amount));
  }

  public Money times(int i) {
    return times(new BigDecimal(i));
  }

  public boolean isGreaterThan(Money other) {
    return compareTo(other) > 0;
  }

  public boolean isGreaterThanOrEqualTo(Money other) {
    return compareTo(other) >= 0;
  }

  public boolean isLessThan(Money other) {
    return compareTo(other) < 0;
  }

  public boolean isLessThanOrEqualTo(Money other) {
    return compareTo(other) <= 0;
  }

  @Override
  public int compareTo(Money other) {
    if (!hasSameCurrencyAs(other)) {
      throw new IllegalArgumentException("Compare is not defined between different currencies");
    }
    return amount.compareTo(other.amount);
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof Money) {
      Money other = (Money) object;
      return hasSameCurrencyAs(other) && amount.equals(other.amount);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return amount.hashCode();
  }

  @Override
  public String toString() {
    String symbol = currency.getSymbol();
    if (!"$".equals(symbol)) {
      symbol = symbol + " ";
    }
    return symbol + amount;
  }

  public String toString(Locale locale) {
    String symbol = currency.getSymbol(locale);
    if (!"$".equals(symbol)) {
      symbol = symbol + " ";
    }
    return symbol + amount;
  }

  public MoneyTimeRate per(Duration duration) {
    return new MoneyTimeRate(this, duration);
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  /**
   * This probably should be Currency responsibility. Even then, it may need
   * to be customized for specialty apps because there are other cases, where
   * the smallest increment is not the smallest unit.
   */
  Money minimumIncrement() {
    BigDecimal one = new BigDecimal(1);
    BigDecimal increment = one.movePointLeft(currency.getDefaultFractionDigits());
    return Money.valueOf(increment, currency);
  }

  Money incremented() {
    return plus(minimumIncrement());
  }

  boolean hasSameCurrencyAs(Money arg) {
    return currency.equals(arg.currency);
  }

  private void assertHasSameCurrencyAs(Money aMoney) {
    if (!hasSameCurrencyAs(aMoney)) {
      throw new IllegalArgumentException(aMoney.toString() + " is not same currency as " + toString());
    }
  }

}
