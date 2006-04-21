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
}
