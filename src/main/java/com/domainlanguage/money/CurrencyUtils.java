package com.domainlanguage.money;

import java.util.Currency;

public class CurrencyUtils {

  /** Side-step Currency.getSymbol() only returning a symbol if given a matching locale. */
  public static String getSymbol(final Currency currency) {
    switch (currency.getCurrencyCode()) {
    case "USD":
      return "$";
    case "AUD":
      return "AU$";
    case "CAD":
      return "CA$";
    case "NZD":
      return "NZ$";
    case "BRL":
      return "R$";
    case "EUR":
      return "€";
    case "GBP":
      return "£";
    case "SEK":
    case "DKK":
    case "NOK":
      return "kr";
    case "SGD":
      return "S$";
    case "HKD":
      return "HK$";
    case "INR":
      return "₹";
    case "JPY":
      return "¥";
    case "ZAR":
      return "R";
    case "CHF":
      return "CHF";
    default:
      throw new IllegalStateException("Unsupported currency: " + currency);
    }
  }
}
