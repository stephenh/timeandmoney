/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

import java.math.*;

import junit.framework.*;

public class TimeRateTest extends TestCase {
    public void testSimpleRate() throws Exception {
        TimeRate rate = new TimeRate(new BigDecimal(100.00), Duration.minutes(1));
        assertEquals(new BigDecimal(6000.00), rate.over(Duration.hours(1)));
    }
    
    public void testRounding(){
        TimeRate rate = new TimeRate(new BigDecimal(100.00), Duration.minutes(3));
        try {
            rate.over(Duration.minutes(1));
            fail("ArtithmeticException should have been thrown. This case requires rounding.");
        } catch (ArithmeticException e) {}
    }
    public void testRoundingRate() throws Exception {
        TimeRate rate = new TimeRate(new BigDecimal("100.00"), Duration.minutes(3));
        assertEquals(new BigDecimal("33.33"), rate.over(Duration.minutes(1), BigDecimal.ROUND_DOWN));
    }

    public void testRoundingScalingRate() throws Exception {
        TimeRate rate = new TimeRate(new BigDecimal("100.00"), Duration.minutes(3));
        assertEquals(new BigDecimal("33.333"), rate.over(Duration.minutes(1), 3, BigDecimal.ROUND_DOWN));
    }
    public void testEquals() {
        BigDecimal amount=new BigDecimal(11);
        TimeRate rate=new TimeRate(amount, Duration.days(2));
        assertEquals(new TimeRate(new BigDecimal(11.00), Duration.days(2)), rate);
    }
}
