/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import junit.framework.*;

public class AllTimeTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTest(new TestSuite(TimeUnitTest.class));
		
		suite.addTest(new TestSuite(TimePointTest.class));
		suite.addTest(new TestSuite(DurationTest.class));
		suite.addTest(new TestSuite(TimeIntervalTest.class));

		suite.addTest(new TestSuite(CalendarIntervalTest.class));
		suite.addTest(new TestSuite(CalendarDateTest.class));
		suite.addTest(new TestSuite(DateSpecificationTest.class));

		suite.addTest(new TestSuite(BusinessCalendarTest.class));

		return suite;
	}
}
