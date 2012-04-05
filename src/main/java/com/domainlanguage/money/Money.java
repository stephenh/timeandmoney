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
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private static final Currency USD = Currency.getInstance("USD");
  private static final Currency EUR = Currency.getInstance("EUR");
  private static final int DEFAULT_ROUNDING_MODE = Rounding.HALF_EVEN;

  private final BigDecimal amount;
  private final Currency currency;

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

  /**
     * This creation method is safe to use. It will adjust scale, but will not
     * round off the amount.
     */
  public static Money valueOf(BigDecimal amount, Currency currency) {
    return Money.valueOf(amount, currency, Rounding.UNNECESSARY);
  }

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
    return (money == null) ? Money.dollars(0.00) : money;
  }

  /**
   * How best to handle access to the internals? It is needed for
   * database mapping, UI presentation, and perhaps a few other
   * uses. Yet giving public access invites people to do the
   * real work of the Money object elsewhere.
   * Here is an experimental approach, giving access with a 
   * warning label of sorts. Let us know how you like it.
   */
  public BigDecimal breachEncapsulationOfAmount() {
    return this.amount;
  }

  public Currency breachEncapsulationOfCurrency() {
    return this.currency;
  }

  /**
     * This probably should be Currency responsibility. Even then, it may need
     * to be customized for specialty apps because there are other cases, where
     * the smallest increment is not the smallest unit.
     */
  Money minimumIncrement() {
    BigDecimal one = new BigDecimal(1);
    BigDecimal increment = one.movePointLeft(this.currency.getDefaultFractionDigits());
    return Money.valueOf(increment, this.currency);
  }

  Money incremented() {
    return this.plus(this.minimumIncrement());
  }

  boolean hasSameCurrencyAs(Money arg) {
    return this.currency.equals(arg.currency);
  }

  public Money negated() {
    return Money.valueOf(this.amount.negate(), this.currency);
  }

  public Money abs() {
    return Money.valueOf(this.amount.abs(), this.currency);
  }

  public boolean isNegative() {
    return this.amount.compareTo(new BigDecimal(0)) < 0;
  }

  public boolean isNotNegative() {
    return !this.isNegative();
  }

  public boolean isPositive() {
    return this.amount.compareTo(new BigDecimal(0)) > 0;
  }

  public boolean isNotPositive() {
    return !this.isPositive();
  }

  public boolean isZero() {
    return this.equals(Money.valueOf(0.0, this.currency));
  }

  public boolean isNotZero() {
    return !this.isZero();
  }

  public Money plus(Money other) {
    this.assertHasSameCurrencyAs(other);
    return Money.valueOf(this.amount.add(other.amount), this.currency);
  }

  public Money minus(Money other) {
    return this.plus(other.negated());
  }

  public Money dividedBy(BigDecimal divisor, int roundingMode) {
    BigDecimal newAmount = this.amount.divide(divisor, roundingMode);
    return Money.valueOf(newAmount, this.currency);
  }

  public Money dividedBy(double divisor) {
    return this.dividedBy(divisor, Money.DEFAULT_ROUNDING_MODE);
  }

  public Money dividedBy(double divisor, int roundingMode) {
    return this.dividedBy(new BigDecimal(divisor), roundingMode);
  }

  public Ratio dividedBy(Money divisor) {
    this.assertHasSameCurrencyAs(divisor);
    return Ratio.of(this.amount, divisor.amount);
  }

  public Money applying(Ratio ratio, int roundingRule) {
    return this.applying(ratio, this.currency.getDefaultFractionDigits(), roundingRule);
  }

  public Money applying(Ratio ratio, int scale, int roundingRule) {
    BigDecimal newAmount = ratio.times(this.amount).decimalValue(scale, roundingRule);
    return Money.valueOf(newAmount, this.currency);
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
    return this.times(factor, Money.DEFAULT_ROUNDING_MODE);
  }

  /**
     * TODO: BigDecimal.multiply() scale is sum of scales of two multiplied
     * numbers. So what is scale of times?
     */
  public Money times(BigDecimal factor, int roundingMode) {
    return Money.valueOf(this.amount.multiply(factor), this.currency, roundingMode);
  }

  public Money times(double amount, int roundingMode) {
    return this.times(new BigDecimal(amount), roundingMode);
  }

  public Money times(double amount) {
    return this.times(new BigDecimal(amount));
  }

  public Money times(int i) {
    return this.times(new BigDecimal(i));
  }

  @Override
  public int compareTo(Money other) {
    if (!this.hasSameCurrencyAs(other)) {
      throw new IllegalArgumentException("Compare is not defined between different currencies");
    }
    return this.amount.compareTo(other.amount);
  }

  public boolean isGreaterThan(Money other) {
    return (this.compareTo(other) > 0);
  }

  public boolean isGreaterThanOrEqualTo(Money other) {
    return (this.compareTo(other) >= 0);
  }

  public boolean isLessThan(Money other) {
    return (this.compareTo(other) < 0);
  }

  public boolean isLessThanOrEqualTo(Money other) {
    return (this.compareTo(other) <= 0);
  }

  public boolean equals(Object other) {
    try {
      return this.equals((Money) other);
    } catch (ClassCastException ex) {
      return false;
    }
  }

  public boolean equals(Money other) {
    return other != null && this.hasSameCurrencyAs(other) && this.amount.equals(other.amount);
  }

  public int hashCode() {
    return this.amount.hashCode();
  }

  public String toString() {
    String symbol = this.currency.getSymbol();
    if (!"$".equals(symbol)) {
      symbol = symbol + " ";
    }
    return symbol + this.amount;
  }

  public String toString(Locale locale) {
    String symbol = this.currency.getSymbol(locale);
    if (!"$".equals(symbol)) {
      symbol = symbol + " ";
    }
    return symbol + this.amount;
  }

  public MoneyTimeRate per(Duration duration) {
    return new MoneyTimeRate(this, duration);
  }

  //  TODO: Provide some currency-dependent formatting. Java 1.4 Currency doesn't
  //  do it.
  //  public String formatString() {
  //      return currency.formatString(amount());
  //  }
  //  public String localString() {
  //      return currency.getFormat().format(amount());
  //  }

  BigDecimal getAmount() {
    return this.amount;
  }

  Currency getCurrency() {
    return this.currency;
  }

  private void assertHasSameCurrencyAs(Money aMoney) {
    if (!this.hasSameCurrencyAs(aMoney)) {
      throw new IllegalArgumentException(aMoney.toString() + " is not same currency as " + this.toString());
    }
  }

}
