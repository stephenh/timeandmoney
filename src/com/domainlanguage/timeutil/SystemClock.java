/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import java.util.*;

import com.domainlanguage.time.*;

public class SystemClock {
    private static Date now;
    
    public static TimeSource timeSource() {
        return new TimeSource() {
            public TimePoint now() {
                return SystemClock.now();
            }
        };
    }

    public static TimePoint now() {
        return TimePoint.from(now == null ? new Date() : now);
    }
    
    //Dependency injection to make sure tests return consistent results
    static void setNow(Date aDate) {
        now=aDate;
    }
}