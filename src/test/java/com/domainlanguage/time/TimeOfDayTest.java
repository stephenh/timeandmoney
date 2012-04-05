/**
 * Copyright (c) 2006 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

import java.util.TimeZone;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * TimeOfDayTest
 *
 * @author davem
 */
public class TimeOfDayTest extends TestCase {

  private static final TimeZone CST = TimeZone.getTimeZone("CST");
  private CalendarDate feb17 = CalendarDate.from(2006, 2, 17);
  private TimeOfDay midnight = TimeOfDay.hourAndMinute(0, 0);
  private TimeOfDay morning = TimeOfDay.hourAndMinute(10, 20);
  private TimeOfDay noon = TimeOfDay.hourAndMinute(12, 0);
  private TimeOfDay afternoon = TimeOfDay.hourAndMinute(15, 40);
  private TimeOfDay twoMinutesBeforeMidnight = TimeOfDay.hourAndMinute(23, 58);

  /**
   * Constructs a TimeOfDayTest.
   */
  public TimeOfDayTest() {
    super();
  }

  /**
   * Constructs a TimeOfDayTest.
   * @param name
   */
  public TimeOfDayTest(String name) {
    super(name);
  }

  public void testOnStartOfDay() {
    CalendarMinute feb17AtStartOfDay = CalendarMinute.dateHourAndMinute(2006, 2, 17, 0, 0);
    Assert.assertEquals(feb17AtStartOfDay, this.midnight.on(this.feb17));
  }

  public void testOnMiddleOfDay() {
    CalendarMinute feb17AtMiddleOfDay = CalendarMinute.dateHourAndMinute(2006, 2, 17, 12, 0);
    Assert.assertEquals(feb17AtMiddleOfDay, this.noon.on(this.feb17));
  }

  public void testOnEndOfDay() {
    CalendarMinute feb17AtEndOfDay = CalendarMinute.dateHourAndMinute(2006, 2, 17, 23, 58);
    Assert.assertEquals(feb17AtEndOfDay, this.twoMinutesBeforeMidnight.on(this.feb17));
  }

  public void testEquals() {
    Assert.assertEquals(TimeOfDay.hourAndMinute(0, 0), this.midnight);
    Assert.assertEquals(TimeOfDay.hourAndMinute(10, 20), this.morning);
    Assert.assertEquals(TimeOfDay.hourAndMinute(12, 0), this.noon);
    Assert.assertEquals(TimeOfDay.hourAndMinute(15, 40), this.afternoon);
    Assert.assertEquals(TimeOfDay.hourAndMinute(23, 58), this.twoMinutesBeforeMidnight);
  }

  public void testHashCode() {
    Assert.assertEquals(TimeOfDay.hourAndMinute(0, 0).hashCode(), this.midnight.hashCode());
    Assert.assertEquals(TimeOfDay.hourAndMinute(10, 20).hashCode(), this.morning.hashCode());
    Assert.assertEquals(TimeOfDay.hourAndMinute(12, 0).hashCode(), this.noon.hashCode());
    Assert.assertEquals(TimeOfDay.hourAndMinute(15, 40).hashCode(), this.afternoon.hashCode());
    Assert.assertEquals(TimeOfDay.hourAndMinute(23, 58).hashCode(), this.twoMinutesBeforeMidnight.hashCode());
  }

  public void testAfterWithEarlierTimeOfDay() {
    Assert.assertTrue("expected twoMinutesBeforeMidnight to be after midnight", this.twoMinutesBeforeMidnight.isAfter(this.midnight));
    Assert.assertTrue("expected afternoon to be after morning", this.afternoon.isAfter(this.morning));
    Assert.assertTrue("expected noon to be after midnight", this.noon.isAfter(this.midnight));
  }

  public void testAfterWithLaterTimeOfDay() {
    Assert.assertFalse("expected midnight not after twoMinutesBeforeMidnight", this.midnight.isAfter(this.twoMinutesBeforeMidnight));
    Assert.assertFalse("expected morning not after afternoon", this.morning.isAfter(this.afternoon));
    Assert.assertFalse("expected noon not after twoMinutesBeforeMidnight", this.noon.isAfter(this.twoMinutesBeforeMidnight));
  }

  public void testAfterWithSameTimeOfDay() {
    Assert.assertFalse("expected midnight not after midnight", this.midnight.isAfter(this.midnight));
    Assert.assertFalse("expected morning not after morning", this.morning.isAfter(this.morning));
    Assert.assertFalse("expected afternoon not after afternoon", this.afternoon.isAfter(this.afternoon));
    Assert.assertFalse("expected noon not after noon", this.noon.isAfter(this.noon));
  }

  public void testBeforeWithEarlierTimeOfDay() {
    Assert.assertFalse("expected twoMinutesBeforeMidnight not after midnight", this.twoMinutesBeforeMidnight.isBefore(this.midnight));
    Assert.assertFalse("expected afternoon not after morning", this.afternoon.isBefore(this.morning));
    Assert.assertFalse("expected noon not after midnight", this.noon.isBefore(this.midnight));
  }

  public void testBeforeWithLaterTimeOfDay() {
    Assert.assertTrue("expected midnight not after twoMinutesBeforeMidnight", this.midnight.isBefore(this.twoMinutesBeforeMidnight));
    Assert.assertTrue("expected morning not after afternoon", this.morning.isBefore(this.afternoon));
    Assert.assertTrue("expected noon not after twoMinutesBeforeMidnight", this.noon.isBefore(this.twoMinutesBeforeMidnight));
  }

  public void testBeforeWithSameTimeOfDay() {
    Assert.assertFalse("expected midnight not after midnight", this.midnight.isBefore(this.midnight));
    Assert.assertFalse("expected morning not after morning", this.morning.isBefore(this.morning));
    Assert.assertFalse("expected afternoon not after afternoon", this.afternoon.isBefore(this.afternoon));
    Assert.assertFalse("expected noon not after noon", this.noon.isBefore(this.noon));
  }

  public void testGetHour() {
    Assert.assertEquals(0, this.midnight.getHour());
    Assert.assertEquals(10, this.morning.getHour());
    Assert.assertEquals(12, this.noon.getHour());
    Assert.assertEquals(15, this.afternoon.getHour());
    Assert.assertEquals(23, this.twoMinutesBeforeMidnight.getHour());
  }

  public void testGetMinute() {
    Assert.assertEquals(0, this.midnight.getMinute());
    Assert.assertEquals(20, this.morning.getMinute());
    Assert.assertEquals(0, this.noon.getMinute());
    Assert.assertEquals(40, this.afternoon.getMinute());
    Assert.assertEquals(58, this.twoMinutesBeforeMidnight.getMinute());
  }

  public void testAsTimePoint() {
    TimeOfDay fiveFifteen = TimeOfDay.hourAndMinute(17, 15);
    CalendarDate mayEleventh = CalendarDate.date(2006, 5, 11);
    TimePoint mayEleventhAtFiveFifteen = fiveFifteen.asTimePointGiven(mayEleventh, TimeOfDayTest.CST);
    Assert.assertEquals(TimePoint.at(2006, 5, 11, 17, 15, 0, 0, TimeOfDayTest.CST), mayEleventhAtFiveFifteen);
  }
}
