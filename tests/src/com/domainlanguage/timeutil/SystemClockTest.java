/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import java.util.*;

import junit.framework.*;

import com.domainlanguage.time.*;

public class SystemClockTest extends TestCase {

    // TODO: This isn't much of a test, since the implementation is
    // so similar to the "expected" value. Maybe someone else
    // will have a better idea.
    public void testSystemClockTimeSource() {
        TimePoint now = SystemClock.now();
        TimePoint approxNow = TimePoint.from(new Date());
        assertEquals("This occasionally fails if the clock ticks during the test.", approxNow, now);

        //The following calls allow polymorphic substitution of TimeSources
        //either in applications or, more often, in testing.
        TimeSource source = SystemClock.timeSource();
        now = source.now();
        approxNow = TimePoint.from(new Date());
        assertTrue(now.until(approxNow).length().compareTo(Duration.milliseconds(5)) < 0);
    }

}