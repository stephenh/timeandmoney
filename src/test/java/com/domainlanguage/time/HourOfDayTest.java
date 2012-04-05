/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

import junit.framework.Assert;
import junit.framework.TestCase;

public class HourOfDayTest extends TestCase {
  public void test24Simple() {
    Assert.assertEquals(22, HourOfDay.value(22).value());
  }

  public void test12Simple() {
    Assert.assertEquals(HourOfDay.value(22), HourOfDay.value(10, "PM"));
    Assert.assertEquals(HourOfDay.value(3), HourOfDay.value(3, "am"));
  }

  public void test24IllegalLessThanZero() {
    try {
      HourOfDay.value(-1);
    } catch (IllegalArgumentException ex) {
      return;
    }
    Assert.fail("Illegal Argument Not Caught");
  }

  public void test24GreaterThan() {
    try {
      HourOfDay.value(24);
    } catch (IllegalArgumentException ex) {
      return;
    }
    Assert.fail("Illegal Argument Not Caught");
  }

  public void test12IllegalLessThanZero() {
    try {
      HourOfDay.value(-1, "PM");
    } catch (IllegalArgumentException ex) {
      return;
    }
    Assert.fail("Illegal Argument Not Caught");
  }

  public void test12GreaterThan() {
    try {
      HourOfDay.value(13, "AM");
    } catch (IllegalArgumentException ex) {
      return;
    }
    Assert.fail("Illegal Argument Not Caught");
  }

  public void test12BadAmPm() {
    try {
      HourOfDay.value(5, "FD");
    } catch (IllegalArgumentException ex) {
      return;
    }
    Assert.fail("Illegal Argument Not Caught");
  }

  public void testLaterAfterEarlier() {
    HourOfDay later = HourOfDay.value(8);
    HourOfDay earlier = HourOfDay.value(6);
    Assert.assertTrue(later.isAfter(earlier));
  }

  public void testEarlierAfterLater() {
    HourOfDay earlier = HourOfDay.value(8);
    HourOfDay later = HourOfDay.value(20);
    Assert.assertFalse(earlier.isAfter(later));
  }

  public void testEqualAfterEqual() {
    HourOfDay anHour = HourOfDay.value(8);
    HourOfDay anotherHour = HourOfDay.value(8);
    Assert.assertFalse(anHour.isAfter(anotherHour));
  }

  public void testLaterBeforeEarlier() {
    HourOfDay later = HourOfDay.value(8);
    HourOfDay earlier = HourOfDay.value(6);
    Assert.assertFalse(later.isBefore(earlier));
  }

  public void testEarlierBeforeLater() {
    HourOfDay earlier = HourOfDay.value(8);
    HourOfDay later = HourOfDay.value(20);
    Assert.assertTrue(earlier.isBefore(later));
  }

  public void testEqualBeforeEqual() {
    HourOfDay anHour = HourOfDay.value(8);
    HourOfDay anotherHour = HourOfDay.value(8);
    Assert.assertFalse(anHour.isBefore(anotherHour));
  }
}
