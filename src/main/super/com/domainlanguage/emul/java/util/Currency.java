package java.util;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A very basic/bare-bones implementation of java.util.Currency
 * for only the currencies that we need to support.
 */
public class Currency implements Serializable, IsSerializable {

  private static final Map<String, Currency> currencies = new HashMap<String, Currency>();

  static {
    currencies.put("USD", new Currency("USD", "$", 2));
    currencies.put("GBP", new Currency("GBP", "£", 2));
    currencies.put("EUR", new Currency("EUR", "€", 2));
    currencies.put("JPY", new Currency("JPY", "¥", 0));
    currencies.put("AUD", new Currency("AUD", "AU$", 2));
    currencies.put("CAD", new Currency("CAD", "CA$", 2));
    currencies.put("NZD", new Currency("NZD", "NZ$", 2));
    currencies.put("BRL", new Currency("BRL", "R$", 2));
    currencies.put("SEK", new Currency("SEK", "DKK", 2));
    currencies.put("NOK", new Currency("NOK", "kr", 2));
    currencies.put("SGD", new Currency("SGD", "S$", 2));
    currencies.put("HKD", new Currency("HKD", "HK$", 2));
    currencies.put("INR", new Currency("INR", "₹", 2));
    currencies.put("ZAR", new Currency("ZAR", "R", 2));
    currencies.put("CHF", new Currency("CHF", "CHF", 2));
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

  private Currency(String code, String symbol, int fractions) {
    this.code = code;
    this.symbol = symbol;
    this.fractions = fractions;
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

  @Override
  public String toString() {
    return code;
  }
}
