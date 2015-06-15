package com.domainlanguage.timeutil;

import java.util.TimeZone;

import com.domainlanguage.time.CalendarDate;
import com.domainlanguage.time.TimePoint;

/** A nicer interface around {@link FrozenClock}. */
public class Freeze {

  public static TimeZone defaultTimeZone = TimeZone.getDefault();

  static {
    Clock.setDefaultTimeZone(Freeze.defaultTimeZone);
  }

  public static void at(CalendarDate day) {
    FrozenClock.freeze(day.startAsTimePoint(Freeze.defaultTimeZone));
  }

  public static void at(TimePoint at) {
    FrozenClock.freeze(at);
  }

  public static void at(int year, int month, int day) {
    FrozenClock.freeze(TimePoint.atMidnight(year, month, day, Freeze.defaultTimeZone));
  }

  public static boolean isFrozen() {
    return FrozenClock.isFrozen();
  }

  public static void unfreeze() {
    Freeze.unfreeze();
  }

  public static void nextDay() {
    FrozenClock.freeze(Clock.today().nextDay().startAsTimePoint(Freeze.defaultTimeZone));
  }

}
