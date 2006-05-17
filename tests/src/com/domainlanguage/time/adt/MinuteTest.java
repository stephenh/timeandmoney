/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time.adt;

import junit.framework.*;

public class MinuteTest extends TestCase {
    public void testSimple() {
        assertEquals(11, Minute.value(11).value());
        assertEquals(Minute.value(23), Minute.value(23));
    }
    public void testIllegalLessThanZero() {
        try {
            Minute.value(-1);
        }catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }
    public void testGreaterThan() {
        try {
            Hour.value(60);
        }catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }
    public void testLaterAfterEarlier() {
        Minute later = Minute.value(45);
        Minute earlier = Minute.value(15);
        assertTrue(later.after(earlier));
    }
    public void testEarlierAfterLater() {
        Minute earlier = Minute.value(15);
        Minute later = Minute.value(45);
        assertFalse(earlier.after(later));
    }
    public void testEqualAfterEqual() {
        Minute anMinute = Minute.value(45);
        Minute anotherMinute = Minute.value(45);
        assertFalse(anMinute.after(anotherMinute));
    }
    public void testLaterBeforeEarlier() {
        Minute later = Minute.value(45);
        Minute earlier = Minute.value(15);
        assertFalse(later.before(earlier));
    }
    public void testEarlierBeforeLater() {
        Minute earlier = Minute.value(15);
        Minute later = Minute.value(45);
        assertTrue(earlier.before(later));
    }
    public void testEqualBeforeEqual() {
        Minute anMinute = Minute.value(15);
        Minute anotherMinute = Minute.value(15);
        assertFalse(anMinute.before(anotherMinute));
    }
}
