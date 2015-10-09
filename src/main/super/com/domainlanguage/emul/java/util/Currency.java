package java.util;

import java.io.Serializable;

/**
 * A very basic/bare-bones implementation of java.util.Currency
 * for only the currencies that we need to support.
 */
public class Currency implements Serializable {

  private static final Map<String, Currency> currencies = new HashMap<String, Currency>();

  static {
    currencies.put("USD", new Currency("USD", "$", 2, "United States Dollar"));
    currencies.put("GBP", new Currency("GBP", "£", 2, "British Pound"));
    currencies.put("EUR", new Currency("EUR", "€", 2, "Euro"));
    currencies.put("JPY", new Currency("JPY", "¥", 0, "Japanese Yen"));
    currencies.put("AUD", new Currency("AUD", "AU$", 2, "Australian Dollar"));
    currencies.put("CAD", new Currency("CAD", "CA$", 2, "Canadian Dollar"));
    currencies.put("NZD", new Currency("NZD", "NZ$", 2, "New Zealand Dollar"));
    currencies.put("BRL", new Currency("BRL", "R$", 2, "Brazilian Real"));
    currencies.put("DKK", new Currency("DKK", "kr", 2, "Danish Krone"));
    currencies.put("SEK", new Currency("SEK", "kr", 2, "Swedish Krona"));
    currencies.put("NOK", new Currency("NOK", "kr", 2, "Norwegian Krone"));
    currencies.put("SGD", new Currency("SGD", "S$", 2, "Singapore Dollar"));
    currencies.put("HKD", new Currency("HKD", "HK$", 2, "Hong Kong Dollar"));
    currencies.put("INR", new Currency("INR", "₹", 2, "Indian Rupee"));
    currencies.put("ZAR", new Currency("ZAR", "R", 2, "South African Rand"));
    currencies.put("CHF", new Currency("CHF", "CHF", 2, "Swiss Franc"));
  }

  public static Currency getInstance(String code) {
    Currency c = currencies.get(code);
    if (c == null) {
      throw new IllegalArgumentException("Unsupported currency " + code);
    }
    return c;
  }

  private final String code;
  private final String symbol;
  private final int fractions;
  private final String displayName;

  private Currency(String code, String symbol, int fractions, String displayName) {
    this.code = code;
    this.symbol = symbol;
    this.fractions = fractions;
    this.displayName = displayName;
  }

  public String getCurrencyCode() {
    return code;
  }

  public String getSymbol() {
    return symbol;
  }

  public String getSymbol(Locale locale) {
    throw new IllegalArgumentException("Not implemented");
  }

  public int getDefaultFractionDigits() {
    return fractions;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getDisplayName(Locale locale) {
    throw new IllegalArgumentException("Not implemented");
  }

  @Override
  public String toString() {
    return code;
  }
}
