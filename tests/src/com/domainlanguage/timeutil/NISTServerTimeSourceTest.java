/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import java.io.IOException;

import junit.framework.TestCase;

import com.domainlanguage.time.Duration;
import com.domainlanguage.time.TimePoint;
import com.domainlanguage.time.TimeSource;

public class NISTServerTimeSourceTest extends TestCase {

	//TODO: I don't know how to test this, except to see if it
	//runs to completion. Maybe someone else will have an idea.
	public void testNISTTimeSource() {
		TimePoint now = null;
		try {
			now = NISTServer.now();
			assertNotNull(now);
		} catch (IOException e) {
			fail("Usually this means there is no Internet connection: " + e);
		}

		//The following calls allow polymorphic substitution of TimeSources
		//either in applications or, more often, in testing.
		TimeSource source = NISTServer.timeSource();
		TimePoint approxNow = source.now();
		Duration delay = now.until(approxNow).length();
		assertTrue("This occasionally fails if the network is slow during the test.", delay.compareTo(Duration.seconds(3)) < 0);


	}
	
}
