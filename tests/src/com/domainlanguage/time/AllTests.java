package com.domainlanguage.time;

import junit.framework.*;

public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTest(new TestSuite(TimeUnitTest.class));
		
		suite.addTest(new TestSuite(TimePointTest.class));
		suite.addTest(new TestSuite(DurationTest.class));
		suite.addTest(new TestSuite(TimeIntervalTest.class));

		suite.addTest(new TestSuite(CalendarIntervalTest.class));
		suite.addTest(new TestSuite(CalendarDateTest.class));

		suite.addTest(new TestSuite(BusinessCalendarTest.class));

		return suite;
	}
}