/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
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
	public void testNow() {
		TimePoint now = SystemClock.now();
		Date approxNow = new Date();
		assertEquals("if the clock does not tick during test ...", approxNow, now.asJavaUtilDate());
	}

}
