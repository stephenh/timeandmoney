/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeandmoney;

import com.domainlanguage.basic.ComparableIntervalTest;
import com.domainlanguage.money.MoneyTest;

import junit.framework.*;
import junit.framework.TestCase;

public class AllTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTest(new TestSuite(ComparableIntervalTest.class));
		suite.addTest(new TestSuite(MoneyTest.class));
		suite.addTest(com.domainlanguage.time.AllTests.suite());

		return suite;
	}

}
