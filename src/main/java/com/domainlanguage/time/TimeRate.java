/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

import java.math.BigDecimal;

public class TimeRate {

  private final BigDecimal quantity;
  private final Duration unit;

  public TimeRate(double quantity, Duration unit) {
    this(new BigDecimal(quantity), unit);
  }

  public TimeRate(String quantity, Duration unit) {
    this(new BigDecimal(quantity), unit);
  }

  public TimeRate(BigDecimal quantity, Duration unit) {
    this.quantity = quantity;
    this.unit = unit;
  }

  public BigDecimal over(Duration duration) {
    return over(duration, BigDecimal.ROUND_UNNECESSARY);
  }

  public BigDecimal over(Duration duration, int roundRule) {
    return over(duration, scale(), roundRule);
  }

  public BigDecimal over(Duration duration, int scale, int roundRule) {
    return duration.dividedBy(unit).times(quantity).decimalValue(scale, roundRule);
  }

  public int scale() {
    return quantity.scale();
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(quantity);
    buffer.append(" per ");
    buffer.append(unit);
    return buffer.toString();
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof TimeRate) {
      TimeRate other = (TimeRate) object;
      return quantity.equals(other.quantity) && unit.equals(other.unit);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return quantity.hashCode() ^ unit.hashCode();
  }
}
