package com.domainlanguage.money;


public class NumberFormatter {
  public static String commas(final Money amount) {
    return CurrencyTypeUtils.getSymbol(amount.getCurrency())
        + commas(amount.getAmount().doubleValue(), amount.getCurrency().getDefaultFractionDigits());
  }

  /** comma separate numbers */
  public static String commas(final long n) {
    return NumberUtils.format(n);
  }

  public static String commas(final Long n) {
    return n == null ? "-" : NumberUtils.format(n);
  }

  public static String commas(final Integer n) {
    return n == null ? "-" : NumberUtils.format(n.longValue());
  }

  public static String commas(final double value, final int decimalPlaces) {
    return NumberUtils.format(value, decimalPlaces);
  }

}
