/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeandmoney;

import junit.framework.*;

public class AllTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();

		//Unit Tests
		suite.addTest(com.domainlanguage.basic.AllBasicTests.suite());
		suite.addTest(com.domainlanguage.time.AllTimeTests.suite());
		suite.addTest(com.domainlanguage.money.AllTests.suite());
		// The following suite isn't run by default because it calls
		// out to the network and my not complete in some situations
		//suite.addTest(com.domainlanguage.timeutil.AllTimeUtilTests.suite());

		//Usage Examples
		suite.addTest(new TestSuite(example.insuranceRates.CalculateRate.class));

		return suite;
	}

}
