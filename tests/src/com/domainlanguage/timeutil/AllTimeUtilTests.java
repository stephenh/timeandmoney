/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import junit.framework.*;

public class AllTimeUtilTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTest(new TestSuite(NetworkTimeSourceTest.class));
		suite.addTest(new TestSuite(SystemClockTest.class));

		return suite;
	}
}
