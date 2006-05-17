/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time.adt;

import junit.framework.*;

public class HourTest extends TestCase {
    public void test24Simple() {
        assertEquals(22, Hour.value(22).value());
    }
    public void test12Simple() {
        assertEquals(Hour.value(22), Hour.value(10, "PM"));
        assertEquals(Hour.value(3), Hour.value(3, "am"));
    }
    public void test24IllegalLessThanZero() {
        try {
            Hour.value(-1);
        }catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }
    public void test24GreaterThan() {
        try {
            Hour.value(24);
        }catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }
    public void test12IllegalLessThanZero() {
        try {
            Hour.value(-1, "PM");
        }catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }
    public void test12GreaterThan() {
        try {
            Hour.value(13, "AM");
        }catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }
    public void test12BadAmPm() {
        try {
            Hour.value(5, "FD");
        }catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }
    public void testLaterAfterEarlier() {
        Hour later = Hour.value(8);
        Hour earlier = Hour.value(6);
        assertTrue(later.after(earlier));
    }
    public void testEarlierAfterLater() {
        Hour earlier = Hour.value(8);
        Hour later = Hour.value(20);
        assertFalse(earlier.after(later));
    }
    public void testEqualAfterEqual() {
        Hour anHour = Hour.value(8);
        Hour anotherHour = Hour.value(8);
        assertFalse(anHour.after(anotherHour));
    }
    public void testLaterBeforeEarlier() {
        Hour later = Hour.value(8);
        Hour earlier = Hour.value(6);
        assertFalse(later.before(earlier));
    }
    public void testEarlierBeforeLater() {
        Hour earlier = Hour.value(8);
        Hour later = Hour.value(20);
        assertTrue(earlier.before(later));
    }
    public void testEqualBeforeEqual() {
        Hour anHour = Hour.value(8);
        Hour anotherHour = Hour.value(8);
        assertFalse(anHour.before(anotherHour));
    }
}
