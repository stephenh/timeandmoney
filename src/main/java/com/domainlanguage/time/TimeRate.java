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
    return this.over(duration, BigDecimal.ROUND_UNNECESSARY);
  }

  public BigDecimal over(Duration duration, int roundRule) {
    return this.over(duration, this.scale(), roundRule);
  }

  public BigDecimal over(Duration duration, int scale, int roundRule) {
    return duration.dividedBy(this.unit).times(this.quantity).decimalValue(scale, roundRule);
  }

  public boolean equals(Object another) {
    try {
      return this.equals((TimeRate) another);
    } catch (ClassCastException ex) {
      return false;
    }
  }

  public boolean equals(TimeRate another) {
    return another != null && this.quantity.equals(another.quantity) && this.unit.equals(another.unit);
  }

  public int scale() {
    return this.quantity.scale();
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(this.quantity);
    buffer.append(" per ");
    buffer.append(this.unit);
    return buffer.toString();
  }
}
