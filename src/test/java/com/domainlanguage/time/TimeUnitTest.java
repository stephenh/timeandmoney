/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Calendar;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.domainlanguage.tests.SerializationTester;

public class TimeUnitTest extends TestCase {
  public static TimeUnit exampleForPersistentMappingTesting() {
    return TimeUnit.second;
  }

  public static TimeUnit.Type exampleTypeForPersistentMappingTesting() {
    return TimeUnit.Type.hour;
  }

  public void testSerialization() {
    SerializationTester.assertCanBeSerialized(TimeUnit.month);
  }

  public void testToString() {
    Assert.assertEquals("month", TimeUnit.month.toString());
  }

  public void testConvertibleToMilliseconds() {
    Assert.assertTrue(TimeUnit.millisecond.isConvertibleToMilliseconds());
    Assert.assertTrue(TimeUnit.hour.isConvertibleToMilliseconds());
    Assert.assertTrue(TimeUnit.day.isConvertibleToMilliseconds());
    Assert.assertTrue(TimeUnit.week.isConvertibleToMilliseconds());
    Assert.assertFalse(TimeUnit.month.isConvertibleToMilliseconds());
    Assert.assertFalse(TimeUnit.year.isConvertibleToMilliseconds());
  }

  public void testComparison() {
    Assert.assertEquals(0, TimeUnit.hour.compareTo(TimeUnit.hour));
    Assert.assertTrue(TimeUnit.hour.compareTo(TimeUnit.millisecond) > 0);
    Assert.assertTrue(TimeUnit.millisecond.compareTo(TimeUnit.hour) < 0);
    Assert.assertTrue(TimeUnit.day.compareTo(TimeUnit.hour) > 0);
    Assert.assertTrue(TimeUnit.hour.compareTo(TimeUnit.day) < 0);

    Assert.assertTrue(TimeUnit.month.compareTo(TimeUnit.day) > 0);
    Assert.assertTrue(TimeUnit.day.compareTo(TimeUnit.month) < 0);
    Assert.assertTrue(TimeUnit.quarter.compareTo(TimeUnit.hour) > 0);

    Assert.assertEquals(0, TimeUnit.month.compareTo(TimeUnit.month));
    Assert.assertTrue(TimeUnit.quarter.compareTo(TimeUnit.year) < 0);
    Assert.assertTrue(TimeUnit.year.compareTo(TimeUnit.quarter) > 0);
  }

  public void testJavaCalendarConstantForBaseType() {
    Assert.assertEquals(Calendar.MILLISECOND, TimeUnit.millisecond.javaCalendarConstantForBaseType());
    Assert.assertEquals(Calendar.MILLISECOND, TimeUnit.hour.javaCalendarConstantForBaseType());
    Assert.assertEquals(Calendar.MILLISECOND, TimeUnit.day.javaCalendarConstantForBaseType());
    Assert.assertEquals(Calendar.MILLISECOND, TimeUnit.week.javaCalendarConstantForBaseType());
    Assert.assertEquals(Calendar.MONTH, TimeUnit.month.javaCalendarConstantForBaseType());
    Assert.assertEquals(Calendar.MONTH, TimeUnit.quarter.javaCalendarConstantForBaseType());
    Assert.assertEquals(Calendar.MONTH, TimeUnit.year.javaCalendarConstantForBaseType());
  }

  public void testIsConvertableTo() {
    Assert.assertTrue(TimeUnit.hour.isConvertibleTo(TimeUnit.minute));
    Assert.assertTrue(TimeUnit.minute.isConvertibleTo(TimeUnit.hour));
    Assert.assertTrue(TimeUnit.year.isConvertibleTo(TimeUnit.month));
    Assert.assertTrue(TimeUnit.month.isConvertibleTo(TimeUnit.year));
    Assert.assertFalse(TimeUnit.month.isConvertibleTo(TimeUnit.hour));
    Assert.assertFalse(TimeUnit.hour.isConvertibleTo(TimeUnit.month));
  }

  public void testNextFinerUnit() {
    Assert.assertEquals(TimeUnit.minute, TimeUnit.hour.nextFinerUnit());
    Assert.assertEquals(TimeUnit.month, TimeUnit.quarter.nextFinerUnit());
  }
}
