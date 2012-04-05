/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.money;

import java.math.BigDecimal;
import java.util.Currency;

import com.domainlanguage.time.Duration;
import com.domainlanguage.time.TimeRate;

public class MoneyTimeRate {
  private final TimeRate rate;
  private final Currency currency;

  public MoneyTimeRate(Money money, Duration duration) {
    this.rate = new TimeRate(money.getAmount(), duration);
    this.currency = money.getCurrency();
  }

  public Money over(Duration duration) {
    return this.over(duration, BigDecimal.ROUND_UNNECESSARY);
  }

  public Money over(Duration duration, int roundRule) {
    return this.over(duration, this.rate.scale(), roundRule);
  }

  public Money over(Duration duration, int scale, int roundRule) {
    return Money.valueOf(this.rate.over(duration, scale, roundRule), this.currency);
  }

  public boolean equals(Object other) {
    try {
      return this.equals((MoneyTimeRate) other);
    } catch (ClassCastException ex) {
      return false;
    }
  }

  public boolean equals(MoneyTimeRate another) {
    return another != null && this.rate.equals(another.rate) && this.currency.equals(another.currency);
  }

  public String toString() {
    return this.rate.toString();
  }
}
