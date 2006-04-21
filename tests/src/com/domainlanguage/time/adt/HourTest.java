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
}
