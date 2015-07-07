package com.domainlanguage.money;

import java.util.Currency;

import junit.framework.Assert;

import com.google.gwt.junit.client.GWTTestCase;

public class CurrencyTest extends GWTTestCase {

  public void testUsd() {
    Currency c = Currency.getInstance("USD");
    Assert.assertEquals("USD", c.getCurrencyCode());
    Assert.assertEquals(2, c.getDefaultFractionDigits());
    Assert.assertEquals("$", c.getSymbol());
    // Assert.assertEquals("USD", c.getSymbol(Locale.UK));
  }

  public void testGbp() {
    Currency c = Currency.getInstance("GBP");
    Assert.assertEquals("GBP", c.getCurrencyCode());
    Assert.assertEquals(2, c.getDefaultFractionDigits());
    Assert.assertEquals("£", c.getSymbol());
    // Assert.assertEquals("£", c.getSymbol(Locale.UK));
  }

  public void testEuros() {
    Currency c = Currency.getInstance("EUR");
    Assert.assertEquals("EUR", c.getCurrencyCode());
    Assert.assertEquals(2, c.getDefaultFractionDigits());
    Assert.assertEquals("€", c.getSymbol());
    // Assert.assertEquals("€", c.getSymbol(Locale.FRANCE));
  }

  @Override
  public String getModuleName() {
    return "com.domainlanguage.TestSuite";
  }

}
