package com.domainlanguage.timeutil;

import com.domainlanguage.time.TimePoint;
import com.domainlanguage.time.TimeSource;

/** A {@link TimeSource} for a frozen point in time.
 *
 * See {@link Freeze} for a nicer interface with helper methods.
 */
public class FrozenClock implements TimeSource {

  public static void freeze(TimePoint frozen) {
    Clock.setTimeSource(new FrozenClock(frozen));
  }

  public static void unfreeze() {
    Clock.setTimeSource(null);
  }

  public static boolean isFrozen() {
    return Clock.timeSource() instanceof FrozenClock;
  }

  private final TimePoint now;

  public FrozenClock(TimePoint now) {
    this.now = now;
  }

  public TimePoint now() {
    return now;
  }

}
