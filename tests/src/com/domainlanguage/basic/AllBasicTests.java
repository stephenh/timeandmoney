/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.basic;

import junit.framework.*;

public class AllBasicTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new TestSuite(IntervalTest.class));
		suite.addTest(new TestSuite(IntervalMapTest.class));
		suite.addTest(new TestSuite(RatioTest.class));
		return suite;
	}
}
