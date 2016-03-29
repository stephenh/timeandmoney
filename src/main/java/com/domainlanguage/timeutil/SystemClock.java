/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import java.util.Date;

import com.domainlanguage.time.TimePoint;
import com.domainlanguage.time.TimeSource;

public class SystemClock {

  private static TimePoint nowHighPoint = TimePoint.now();

  public static TimeSource timeSource() {
    return new TimeSource() {
      public TimePoint now() {
        return SystemClock.now();
      }
    };
  }

  public static synchronized TimePoint now() {
    TimePoint now = TimePoint.from(new Date());
    // We've seen clock drift backwards due to ntp adjustments in production,
    // and it can cause obscure bugs where code will do:
    //
    // t1 = clock.now();
    // ...
    // t2 = clock.now();
    // ...
    // interval = t1.until(t2); // fails due to t2 being *before* t1
    //
    // Rather than defensively code against this in each spot that uses
    // clock.now() and TimeIntervals, it's easier to just have SystemClock
    // self-enforce the contract of time not moving backwards.
    if (now.isBefore(nowHighPoint)) {
      return nowHighPoint;
    }
    nowHighPoint = now;
    return now;
  }
}
