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
    Date expectedPrimitiveNow = new Date(1124679473000l);
    

    public void tearDown() throws Exception {
        super.tearDown();
        SystemClock.setNow(null);
    }

    public void testSystemClock() {
        SystemClock.setNow(expectedPrimitiveNow);
        TimePoint expectedNow = TimePoint.from(expectedPrimitiveNow);
        
        TimePoint now = SystemClock.now();
        
        assertEquals(expectedNow, now);
        assertEquals(expectedPrimitiveNow, now.asJavaUtilDate());
    }

    public void testSystemClockTimeSource() {
        // The following calls allow polymorphic substitution of TimeSources
        // either in applications or, more often, in testing.
        TimeSource source = SystemClock.timeSource();
        TimePoint expectedNow = TimePoint.from(new Date());
        TimePoint now = source.now();
        assertTrue(now.until(expectedNow).length().compareTo(
                Duration.milliseconds(50)) < 0);
    }

    
}